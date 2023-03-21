/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
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
import javafx.stage.Stage;
import progettoap.Data;
import progettoap.Database;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class StipendiController implements Initializable {
    private Database db = null;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label bilancio;
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> name;
    @FXML
    private TableColumn<Data, String> surname;
    @FXML
    private TableColumn<Data, Float> hours;
    @FXML
    private TableColumn<Data, Float> salary;
    @FXML
    private TableColumn<Data, Float> h;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String tableName = "stipendi";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTable(tableName);
        loadDataOnTable(tableName);
        outputBalance();
        
        db.closeConnection();
    }
    
    private void createTable(String tableName){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progettoap", "root", "");
            Statement stmt = conn.createStatement();
        ) {		      
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" 
                    + "id int NOT NULL,"
                    + "nome varchar(40),"
                    + "cognome varchar(40),"
                    + "ore_lavorate float,"
                    + "stipendio float,"
                    + "paga_oraria float,"
                    + "PRIMARY KEY (id)"
                    + ");";
            stmt.executeUpdate(sql); 	  
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void loadDataOnTable(String tableName){
        String sql = "SELECT * FROM " + tableName;
        ObservableList<Data> dataList = FXCollections.observableArrayList();

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            // Define table columns and map them to Data class properties
            name.setCellValueFactory(new PropertyValueFactory<>("nome"));
            surname.setCellValueFactory(new PropertyValueFactory<>("cognome"));
            hours.setCellValueFactory(new PropertyValueFactory<>("oreLavorate"));
            salary.setCellValueFactory(new PropertyValueFactory<>("stipendio"));
            h.setCellValueFactory(new PropertyValueFactory<>("pagaOraria"));

            // Add columns to TableView
            /*table.getColumns().add(name);
            table.getColumns().add(surname);
            table.getColumns().add(hours);
            table.getColumns().add(salary);*/

            while (resultSet.next()) {
                Data data = new Data(
                    resultSet.getString("nome"),
                    resultSet.getString("cognome"),
                    resultSet.getFloat("ore_lavorate"),
                    resultSet.getFloat("stipendio"),
                    resultSet.getFloat("paga_oraria")
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
    
    
    private void outputBalance(){
        try{
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(stipendio) FROM stipendi");
            
            // to change!!
            double sum = 0;
            while (resultSet.next()){
                float c = resultSet.getFloat(1);
                sum = sum+c;
            }
            
            double bal = sum * 1.5;
            bilancio.setText(
                    "Bilancio totale:        € " + bal + "\n" +
                    "Stipendi da pagare:     € "+ sum);
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
        root = FXMLLoader.load(getClass().getResource("Contabilita.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 
}
