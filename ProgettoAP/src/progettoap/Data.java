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

    private Integer id;
    private String foodName;
    private Integer avalAmount;
    private Integer usedAmount;

    public Data(int id, String foodName, int avalAmount, int usedAmount) {
        this.id = id;
        this.foodName = foodName;
        this.avalAmount = avalAmount;
        this.usedAmount = usedAmount;
    }

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

