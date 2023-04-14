package progettoap;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FoodOrdering {

    private String dbHost;
    private String dbUser;
    private String dbPassword;
    private String dbName;
    private String foodTable;
    private String ordersTable;

    public FoodOrdering(String dbHost, String dbUser, String dbPassword, String dbName, String foodTable, String ordersTable) {
        this.dbHost = dbHost;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
        this.foodTable = foodTable;
        this.ordersTable = ordersTable;
    }

    // method for retrieving data from the database
    public ArrayList<String> getFoodNames(int numItems) throws SQLException {
        // create a database connection
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + dbName, dbUser, dbPassword);

        // retrieve food names from the database
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nome_alimento FROM " + foodTable);
        ArrayList<String> foodNames = new ArrayList<>();
        while (rs.next()) {
            foodNames.add(rs.getString("nome_alimento"));
        }

        // select random food names
        ArrayList<String> selectedNames = new ArrayList<>(numItems);
        Collections.shuffle(foodNames);
        for (int i = 0; i < numItems && i < foodNames.size(); i++) {
            selectedNames.add(foodNames.get(i));
        }

        // close the database connection
        rs.close();
        stmt.close();
        conn.close();

        return selectedNames;
    }

    // method for putting data in the ordini_cibo table
    public void insertOrders(ArrayList<String> foodNames, int minQuantity, int maxQuantity) throws SQLException {
        if (dbHost == null || dbName == null || dbUser == null || dbPassword == null) {
            throw new IllegalArgumentException("Database connection parameters cannot be null.");
        }

        // create a database connection
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + dbName, dbUser, dbPassword);

        // insert orders into the database
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO " + ordersTable + "(nome_alimento, prezzo, quantita, data) VALUES (?, ?, ?, ?)");
        Random rand = new Random();
        for (String foodName : foodNames) {
            float price = rand.nextFloat() * 10 + 5; // set a random price between 5 and 15
            int quantity = rand.nextInt(maxQuantity - minQuantity + 1) + minQuantity; // set a random quantity between minQuantity and maxQuantity
            pstmt.setString(1, foodName);
            pstmt.setFloat(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, getCurrentDate()); // assuming getCurrentDate() returns the current date in the format dd/mm/yyyy
            pstmt.executeUpdate();
        }

        // close the database connection
        pstmt.close();
        conn.close();
    }





    // helper method to get the current date in the format dd/mm/yyyy
    private String getCurrentDate() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = new java.util.Date();
        return formatter.format(date);
    }


    public static void main(String[] args) throws SQLException {
        FoodOrdering foodOrdering = new FoodOrdering("localhost", "root", "password", "progettoap", "dispensa", "ordini_cibo");
        ArrayList<String> foodNames = foodOrdering.getFoodNames(10);
        foodOrdering.insertOrders(foodNames, 25, 50);
    }
}
