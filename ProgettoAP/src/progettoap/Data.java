/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettoap;

/**
 *
 * @author HP
 */
public class Data {

    private Integer id = null;
    private String foodName = null;
    private Integer avalAmount = null;
    private Integer usedAmount = null;
    private float price = 0;
    private Integer amount = null;

    public Data(int id, String foodName, int avalAmount, int usedAmount) {
        this.id = id;
        this.foodName = foodName;
        this.avalAmount = avalAmount;
        this.usedAmount = usedAmount;
    }
    
    public Data(String foodName, float price, int amount) {
        this.foodName = foodName;
        this.price = price;
        this.amount = amount;
    }

    
    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getAvalAmount() {
        return avalAmount;
    }

    public void setAvalAmount(int avalAmount) {
        this.avalAmount = avalAmount;
    }

    public int getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(int usedAmount) {
        this.usedAmount = usedAmount;
    }

}

