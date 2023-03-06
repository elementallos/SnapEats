/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package progettoap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author HP
 */
public class Database {
    
    // connection to database
    private String url = null;
    private String root = null;
    private String password = null;
    
    // connection
    private Connection connection = null;
    
    // fetch data from specified table
    private String tableName = null;
    
    
    // constructors
    public Database(){}
    public Database(String url, String root, String password, String tableName){
        this.url = url;
        this.root = root;
        this.password = password;
        
        this.tableName = tableName;
    }

    
    // methods
    public Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    url, root, password
            );
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return connection;
    }
    
    public void closeConnection(){
        try{
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    // getters and setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
