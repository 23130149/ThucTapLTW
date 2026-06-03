package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.DBProperties;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

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
}
