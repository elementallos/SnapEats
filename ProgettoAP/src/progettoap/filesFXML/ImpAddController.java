/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import progettoap.Data;
import progettoap.Database;

/**
 * FXML Controller class
 *
 * @author HP
 * 
 * add
 * del
 * mod
 * 
 */
public class ImpAddController implements Initializable {
    private Database db = null;
    private String tableName = "impiegati";
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private TextField n, c, cf, ddn, t, e;
    @FXML
    private Label errMsg;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errMsg.setVisible(false);
        String tableName = "impiegati";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTable(tableName);
        
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
                    + "data_nascita varchar(10),"
                    + "codice_fiscale varchar(16),"
                    + "telefono varchar(15),"
                    + "email varchar(60),"
                    + "PRIMARY KEY (id)"
                    + ");";
            stmt.executeUpdate(sql); 	  
        } catch (Exception e) {
            System.out.println(e);
        }
    }   
    
    @FXML
    public void add(ActionEvent event) throws SQLException{
        if(checkIfAllFilled() != true){
            // stampa il messaggio di errore
            errMsg.setVisible(true);
        }
        
        else{
            errMsg.setVisible(false);
            // aggiungi il nuovo impiegato
            acceptIncomingData();
            
            // pulisci tutti i campi
            clearAll();
        }
    }
    
    private void acceptIncomingData() throws SQLException{
        String sql = "INSERT INTO " + tableName + " (id, nome, cognome, data_nascita, codice_fiscale, telefono, email) VALUES (" + getNextFreeId() + ", '" +
                n.getText() + "', '" +
                c.getText() + "', '" +
                ddn.getText() + "', '" +
                cf.getText() + "', '" +
                t.getText() + "', '" +
                e.getText() + "')";

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            int rowsInserted = statement.executeUpdate(sql);

            if (rowsInserted > 0) {
                System.out.println("A new row has been inserted");
            }
        }

        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public int getNextFreeId() throws SQLException {
        // create the connection
        Connection connection = db.connect();

        // create a SQL SELECT statement to get the maximum ID value
        String sql = "SELECT MAX(id) FROM " + tableName;

        // execute the SELECT statement and get the result set
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // get the maximum ID value
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }

        // return the next free ID (the maximum ID value plus 1)
        return maxId + 1;
    }


    
    private void clearAll(){
        n.clear();
        c.clear();
        cf.clear();
        ddn.clear();
        t.clear();
        e.clear();
    }
    
    private boolean checkIfAllFilled(){
        boolean go = true;
        if(     n.getText().equals("") == true ||
                c.getText().equals("") == true ||
                cf.getText().equals("") == true ||
                ddn.getText().equals("") == true ||
                t.getText().equals("") == true ||
                e.getText().equals("") == true){
            go = false;
        }
        return go;
    }

    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Impiegati.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
