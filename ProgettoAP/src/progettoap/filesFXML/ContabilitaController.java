/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import progettoap.Data;
import progettoap.Database;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class ContabilitaController implements Initializable {

    private Database db = null;
    private String tableName = "movimenti";
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label listaMov;
     
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> date;
    @FXML
    private TableColumn<Data, String> time;
    @FXML
    private TableColumn<Data, Float> in;
    @FXML
    private TableColumn<Data, Float> out;
     
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // FIXME
        listaMov.setFont(new Font("Consolas", 20));
        listaMov.setText("lista movimenti -->");
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTable();
        loadDataOnTable("");
        
        db.closeConnection();
    }
    
    private void createTable(){
        try(Connection conn = db.connect();
            Statement stmt = conn.createStatement();
        ) {		      
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" 
                    + "id int NOT NULL,"
                    + "entrate_lordo float,"
                    + "entrate_netto float,"
                    + "iva float,"
                    + "uscite_pagamenti float,"
                    + "uscite_impiegati float,"
                    + "uscite_rifornimenti float,"
                    + "uscite_tot float,"
                    + "data varchar(10),"
                    + "ora varchar(5),"
                    + "PRIMARY KEY (id)"
                    + ");";
            stmt.executeUpdate(sql); 	  
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadDataOnTable(String mod){
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String dataAttuale = dateObj.format(formatter);
        
        String[] dateParts = dataAttuale.split("/");
        String day = dateParts[1];
        String month = dateParts[0];
        String year = dateParts[2];
        
        String pattern = null;
        switch(mod){
            case "gg":
                pattern = day + "/" + month + "/" + year;
                break;
            case "mm":
                pattern = "%" + month + "/" + year;
                break;
            case "aa":
                pattern = "%" + year;
                break;
            default:
                break;
        }
        
        String sql = "SELECT * FROM " + tableName;
        if(pattern != null){
            sql = "SELECT * FROM " + tableName + " WHERE data LIKE '" + pattern + "'";
        }
        ObservableList<Data> dataList = FXCollections.observableArrayList();

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            // Define table columns and map them to Data class properties
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            time.setCellValueFactory(new PropertyValueFactory<>("time"));
            in.setCellValueFactory(new PropertyValueFactory<>("inNetto"));
            out.setCellValueFactory(new PropertyValueFactory<>("outTot"));

            while (resultSet.next()) {
                Data data = new Data(
                    resultSet.getFloat("entrate_netto"),
                    resultSet.getFloat("uscite_tot"),
                    resultSet.getString("data"),
                    resultSet.getString("ora")
                );
                dataList.add(data);
            }

            // Add data to TableView
            table.setItems(dataList);
        }

        catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    
    
    
    
    @FXML
    public void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void movimenti(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Movimenti.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void stipendi(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Stipendi.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    // update the list
    @FXML
    public void showMovDD(ActionEvent event) {
        listaMov.setText("Giornaliero");
        loadDataOnTable("gg");
    }
    
    @FXML
    public void showMovMM(ActionEvent event) {
        listaMov.setText("Mensile");
        loadDataOnTable("mm");
    }
    
    @FXML
    public void showMovAA(ActionEvent event) {
        listaMov.setText("Annuale");
        loadDataOnTable("aa");
    }
}
