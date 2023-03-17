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
    private Float price = null;
    private Integer amount = null;
    
    // per stipendi
    private String nome = null;
    private String cognome = null;
    private Float oreLavorate = null;
    private Float stipendio = null;

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

    public Data(String nome, String cognome, float oreLavorate, float stipendio){
        this.nome = nome;
        this.cognome = cognome;
        this.oreLavorate = oreLavorate;
        this.stipendio = stipendio;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Float getOreLavorate() {
        return oreLavorate;
    }

    public void setOreLavorate(Float oreLavorate) {
        this.oreLavorate = oreLavorate;
    }

    public Float getStipendio() {
        return stipendio;
    }

    public void setStipendio(Float stipendio) {
        this.stipendio = stipendio;
    }

}

