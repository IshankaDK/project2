package view.tm;


public class PurchaseTM {
    private String code;
    private String description;
    private double qty;
    private double price;
    private double total;

    public PurchaseTM(String code, String description, double qty, double price, double total) {
        this.setCode(code);
        this.setDescription(description);
        this.setQty(qty);
        this.setPrice(price);
        this.setTotal(total);
    }

    public PurchaseTM() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
