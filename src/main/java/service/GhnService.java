package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.DBProperties;
import model.Order;
import model.OrderItem;
import model.UserAddress;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class GhnService {
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final String baseUrl = DBProperties.get("ghn.base_url");
    private final String token = DBProperties.get("ghn.token");
    private final String shopId = DBProperties.get("ghn.shop_id");

    public String getProvinces() throws IOException, InterruptedException {
        return get("/master-data/province", null);
    }

    public String getDistricts(int provinceId) throws IOException, InterruptedException {
        return get("/master-data/district", "province_id=" + provinceId);
    }

    public String getWards(int districtId) throws IOException, InterruptedException {
        return get("/master-data/ward", "district_id=" + districtId);
    }

    public BigDecimal calculateFee(int toDistrictId, String toWardCode) throws IOException, InterruptedException {
        int fromDistrictId = Integer.parseInt(DBProperties.get("ghn.from_district_id"));
        int serviceId = getAvailableServiceId(fromDistrictId, toDistrictId);

        JsonObject body = new JsonObject();
        body.addProperty("from_district_id", fromDistrictId);
        body.addProperty("from_ward_code", DBProperties.get("ghn.from_ward_code"));
        body.addProperty("service_id", serviceId);
        body.addProperty("to_district_id", toDistrictId);
        body.addProperty("to_ward_code", toWardCode);
        body.addProperty("height", 10);
        body.addProperty("length", 20);
        body.addProperty("weight", 500);
        body.addProperty("width", 15);
        body.addProperty("insurance_value", 0);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/v2/shipping-order/fee"))
                .header("Content-Type", "application/json")
                .header("Token", token)
                .header("ShopId", shopId)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("GHN error: " + response.body());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        int fee = json.getAsJsonObject("data").get("total").getAsInt();

        return BigDecimal.valueOf(normalizeFee(fee));
    }

    public GhnOrderResult createOrder(Order order, UserAddress address, List<OrderItem> items)
            throws IOException, InterruptedException {
        if (address == null || address.getDistrictId() == null || address.getWardCode() == null || address.getWardCode().isBlank()) {
            throw new IOException("Địa chỉ chưa có mã GHN");
        }

        int fromDistrictId = Integer.parseInt(DBProperties.get("ghn.from_district_id"));
        int serviceId = getAvailableServiceId(fromDistrictId, address.getDistrictId());
        int totalQuantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
        int weight = Math.max(500, Math.min(30000, totalQuantity * 500));
        int orderValue = order.getTotalPrice() == null ? 0 : order.getTotalPrice().intValue();

        JsonObject body = new JsonObject();
        body.addProperty("payment_type_id", 1);
        body.addProperty("note", order.getNote() == null ? "" : order.getNote());
        body.addProperty("required_note", "KHONGCHOXEMHANG");
        body.addProperty("from_name", DBProperties.get("ghn.from_name"));
        body.addProperty("from_phone", DBProperties.get("ghn.from_phone"));
        body.addProperty("from_address", DBProperties.get("ghn.from_address"));
        body.addProperty("from_ward_name", DBProperties.get("ghn.from_ward_name"));
        body.addProperty("from_district_name", DBProperties.get("ghn.from_district_name"));
        body.addProperty("from_province_name", DBProperties.get("ghn.from_province_name"));
        body.addProperty("to_name", order.getShipName());
        body.addProperty("to_phone", order.getShipPhone());
        body.addProperty("to_address", order.getShipAddress());
        body.addProperty("to_ward_code", address.getWardCode());
        body.addProperty("to_district_id", address.getDistrictId());
        body.addProperty("cod_amount", order.getPaymentMethodId() == 1 ? Math.min(orderValue, 10000000) : 0);
        body.addProperty("content", "Sản phẩm handmade");
        body.addProperty("weight", weight);
        body.addProperty("length", 20);
        body.addProperty("width", 15);
        body.addProperty("height", 10);
        body.addProperty("insurance_value", Math.min(orderValue, 5000000));
        body.addProperty("service_id", serviceId);
        body.addProperty("service_type_id", 2);
        body.addProperty("client_order_code", order.getOrderCode());

        JsonArray orderItems = new JsonArray();
        for (OrderItem item : items) {
            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("name", item.getProductName());
            jsonItem.addProperty("code", String.valueOf(item.getProductId()));
            jsonItem.addProperty("quantity", item.getQuantity());
            jsonItem.addProperty("price", item.getUnitPrice() == null ? 0 : item.getUnitPrice().intValue());
            jsonItem.addProperty("length", 20);
            jsonItem.addProperty("width", 15);
            jsonItem.addProperty("height", 10);
            jsonItem.addProperty("weight", 500);
            orderItems.add(jsonItem);
        }
        body.add("items", orderItems);

        JsonObject data = post("/v2/shipping-order/create", body).getAsJsonObject("data");
        return new GhnOrderResult(
                getString(data, "order_code"),
                getString(data, "status"),
                parseDateTime(getString(data, "expected_delivery_time")),
                null
        );
    }

    public GhnOrderResult getOrderDetail(String orderCode) throws IOException, InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("order_code", orderCode);
        JsonObject data = post("/v2/shipping-order/detail", body).getAsJsonObject("data");
        return new GhnOrderResult(
                getString(data, "order_code"),
                getString(data, "status"),
                parseDateTime(getString(data, "leadtime")),
                parseDateTime(getString(data, "finish_date"))
        );
    }

    private int normalizeFee(int fee) {
        return fee - (fee % 10);
    }

    private int getAvailableServiceId(int fromDistrictId, int toDistrictId) throws IOException, InterruptedException {
        String response = get("/v2/shipping-order/available-services",
                "shop_id=" + shopId + "&from_district=" + fromDistrictId + "&to_district=" + toDistrictId);

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        JsonArray services = json.getAsJsonArray("data");

        if (services == null || services.isEmpty()) {
            String configuredServiceId = DBProperties.get("ghn.service_id");
            if (configuredServiceId != null && !configuredServiceId.isBlank()) {
                return Integer.parseInt(configuredServiceId);
            }
            throw new IOException("GHN service not found");
        }

        return services.get(0).getAsJsonObject().get("service_id").getAsInt();
    }

    private String get(String path, String query) throws IOException, InterruptedException {
        String url = baseUrl + path + (query == null || query.isBlank() ? "" : "?" + query);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Token", token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("GHN error: " + response.body());
        }

        return response.body();
    }

    private JsonObject post(String path, JsonObject body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("Content-Type", "application/json")
                .header("Token", token)
                .header("ShopId", shopId)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("GHN error: " + response.body());
        }

        return JsonParser.parseString(response.body()).getAsJsonObject();
    }

    private String getString(JsonObject object, String name) {
        if (object == null || !object.has(name) || object.get(name).isJsonNull()) {
            return null;
        }
        return object.get(name).getAsString();
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value).toLocalDateTime();
        } catch (Exception ignored) {
            try {
                return LocalDateTime.parse(value);
            } catch (Exception ignoredAgain) {
                return null;
            }
        }
    }

    public static class GhnOrderResult {
        private final String orderCode;
        private final String status;
        private final LocalDateTime leadtime;
        private final LocalDateTime finishDate;

        public GhnOrderResult(String orderCode, String status, LocalDateTime leadtime, LocalDateTime finishDate) {
            this.orderCode = orderCode;
            this.status = status;
            this.leadtime = leadtime;
            this.finishDate = finishDate;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public String getStatus() {
            return status;
        }

        public LocalDateTime getLeadtime() {
            return leadtime;
        }

        public LocalDateTime getFinishDate() {
            return finishDate;
        }
    }
}
