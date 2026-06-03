package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.UserAddressDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.UserAddress;
import service.GhnService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@WebServlet("/api/shipping-fee")
public class ShippingFeeApiController extends HttpServlet {

    private final UserAddressDao addressDao = new UserAddressDao();
    private final GhnService ghnService = new GhnService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writeError(response, "UNAUTHORIZED");
            return;
        }

        Integer addressId = parseInteger(request.getParameter("addressId"));

        if (addressId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(response, "INVALID_ADDRESS");
            return;
        }

        UserAddress address = addressDao.findById(addressId);

        if (address == null || address.getUserId() != user.getUserId()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writeError(response, "ADDRESS_NOT_FOUND");
            return;
        }

        if (!hasGhnAddressCode(address)) {
            fillMissingGhnAddressCode(address);
        }

        ShippingFeeResult result = calculateShippingFee(address);
        double distanceKm = estimateDistanceKm(buildShipAddress(address));

        JsonObject json = new JsonObject();
        json.addProperty("shippingFee", result.shippingFee);
        json.addProperty("distanceKm", distanceKm);
        json.addProperty("source", result.source);
        if (result.error != null) {
            json.addProperty("error", result.error);
        }

        response.getWriter().write(json.toString());
    }

    private ShippingFeeResult calculateShippingFee(UserAddress address) {
        if (hasGhnAddressCode(address)) {
            try {
                return new ShippingFeeResult(ghnService.calculateFee(address.getDistrictId(), address.getWardCode()), "GHN", null);
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                return new ShippingFeeResult(calculateFeeByDistance(estimateDistanceKm(buildShipAddress(address))), "FALLBACK", e.getMessage());
            }
        }

        return new ShippingFeeResult(calculateFeeByDistance(estimateDistanceKm(buildShipAddress(address))), "FALLBACK", "MISSING_GHN_ADDRESS_CODE");
    }

    private boolean hasGhnAddressCode(UserAddress address) {
        return address != null
                && address.getDistrictId() != null
                && address.getDistrictId() > 0
                && address.getWardCode() != null
                && !address.getWardCode().isBlank();
    }

    private void fillMissingGhnAddressCode(UserAddress address) {
        try {
            JsonObject province = findProvince(address.getProvince());
            if (province == null) {
                return;
            }

            int provinceId = province.get("ProvinceID").getAsInt();
            JsonObject district = findDistrict(provinceId, address.getDistrict());
            if (district == null) {
                return;
            }

            int districtId = district.get("DistrictID").getAsInt();
            JsonObject ward = findWard(districtId, address.getDistrict());
            if (ward == null) {
                return;
            }

            String wardCode = ward.get("WardCode").getAsString();
            address.setProvinceId(provinceId);
            address.setDistrictId(districtId);
            address.setWardCode(wardCode);
            addressDao.updateGhnCodes(address.getUserAddressId(), address.getUserId(), provinceId, districtId, wardCode);
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private JsonObject findProvince(String provinceName) throws IOException, InterruptedException {
        JsonArray items = JsonParser.parseString(ghnService.getProvinces()).getAsJsonObject().getAsJsonArray("data");
        return findByNames(items, "ProvinceName", provinceName);
    }

    private JsonObject findDistrict(int provinceId, String districtText) throws IOException, InterruptedException {
        JsonArray items = JsonParser.parseString(ghnService.getDistricts(provinceId)).getAsJsonObject().getAsJsonArray("data");
        return findByNames(items, "DistrictName", districtText);
    }

    private JsonObject findWard(int districtId, String districtText) throws IOException, InterruptedException {
        JsonArray items = JsonParser.parseString(ghnService.getWards(districtId)).getAsJsonObject().getAsJsonArray("data");
        return findByNames(items, "WardName", districtText);
    }

    private JsonObject findByNames(JsonArray items, String nameKey, String text) {
        String normalizedText = normalizeForMatch(text);

        for (JsonElement element : items) {
            JsonObject item = element.getAsJsonObject();
            String name = item.has(nameKey) ? item.get(nameKey).getAsString() : "";
            if (matches(normalizedText, name)) {
                return item;
            }

            JsonArray extensions = item.getAsJsonArray("NameExtension");
            if (extensions == null) {
                continue;
            }

            for (JsonElement extension : extensions) {
                if (matches(normalizedText, extension.getAsString())) {
                    return item;
                }
            }
        }

        return null;
    }

    private boolean matches(String normalizedText, String candidate) {
        String normalizedCandidate = normalizeForMatch(candidate);
        return !normalizedCandidate.isBlank()
                && (normalizedText.contains(normalizedCandidate) || normalizedCandidate.contains(normalizedText));
    }

    private String normalizeForMatch(String value) {
        if (value == null) {
            return "";
        }

        return java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9]+", " ")
                .toLowerCase()
                .trim();
    }

    private BigDecimal calculateFeeByDistance(double distanceKm) {
        if (distanceKm <= 0) {
            return BigDecimal.valueOf(30000);
        }

        BigDecimal baseFee = BigDecimal.valueOf(20000);
        BigDecimal feePerKm = BigDecimal.valueOf(4000);

        if (distanceKm <= 5) {
            return baseFee;
        }

        BigDecimal extraDistance = BigDecimal.valueOf(distanceKm - 5);
        BigDecimal extraFee = extraDistance.multiply(feePerKm);

        return baseFee.add(extraFee).setScale(0, RoundingMode.HALF_UP);
    }

    private double estimateDistanceKm(String shipAddress) {
        String address = shipAddress == null ? "" : shipAddress.toLowerCase();

        if (address.contains("thủ đức") || address.contains("quận 1") || address.contains("quận 3")) {
            return 5;
        }

        if (address.contains("hồ chí minh") || address.contains("tp hcm") || address.contains("tphcm")) {
            return 10;
        }

        if (address.contains("bình dương") || address.contains("đồng nai")) {
            return 25;
        }

        return 15;
    }

    private String buildShipAddress(UserAddress addr) {
        return String.join(", ",
                safe(addr.getStreet()),
                safe(addr.getDistrict()),
                safe(addr.getProvince()),
                safe(addr.getCountry())
        );
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void writeError(HttpServletResponse response, String error) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("error", error);
        response.getWriter().write(json.toString());
    }

    private static class ShippingFeeResult {
        private final BigDecimal shippingFee;
        private final String source;
        private final String error;

        private ShippingFeeResult(BigDecimal shippingFee, String source, String error) {
            this.shippingFee = shippingFee;
            this.source = source;
            this.error = error;
        }
    }
}
