package progettoap;

public class Order {
    private int id;
    private String foodName;
    private float price;
    private int quantity;
    private String date;

    public Order(int id, String foodName, float price, int quantity, String date) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
    }

    public Order(int id, String foodName, float price, String date) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }
}

