package model;

public class FilterOption {
    private String value;
    private String label;
    private int productCount;

    public FilterOption() {
    }

    public FilterOption(String value, String label, int productCount) {
        this.value = value;
        this.label = label;
        this.productCount = productCount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
