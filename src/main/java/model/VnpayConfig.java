package model;

public class VnpayConfig {
    public static final String VERSION = "2.1.0";
    public static final String COMMAND = "pay";
    public static final String CURR_CODE = "VND";
    public static final String ORDER_TYPE = "other";
    public static final String LOCALE = "vn";

    public static final String PAY_URL = getConfig("VNPAY_PAY_URL", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
    public static final String TMN_CODE = getConfig("VNPAY_TMN_CODE", "0ONN2OE9");
    public static final String HASH_SECRET = getConfig("VNPAY_HASH_SECRET", "P8GNLCN3KLRBAYZF57ZJU16JF3UIS81L");

    public static final String RETURN_PATH = "/vnpay-return";
    public static final String IPN_PATH = "/vnpay-ipn";

    private static String getConfig(String key, String defaultValue) {
        String propertyValue = System.getProperty(key);
        if (propertyValue != null && !propertyValue.trim().isEmpty()) {
            return propertyValue.trim();
        }

        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return defaultValue;
    }
}
