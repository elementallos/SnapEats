package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import progettoap.Data;
import progettoap.Database;
import progettoap.EmailSender;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class RifStage2Controller implements Initializable {

    private Database db = null;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> foodName;
    @FXML
    private TableColumn<Data, Float> price;
    @FXML
    private TableColumn<Data, Integer> amount;
    
    
    @FXML
    private Label ultimoOrdineData;
    
    private String tableName = "ordini_cibo";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String tableName = "ordini_cibo";
        String ordini = "movimenti";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTableOrdini(ordini);
        createTable();
        loadDataOnTable();
        
        ultimoOrdineData.setText(getUltimoOrdineData(ordini));
        ultimoOrdineData.setWrapText(true);
        
        db.closeConnection();
    }
    
    private void createTable(){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progettoap", "root", "");
            Statement stmt = conn.createStatement();
        ) {		      
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" 
                    + "id int NOT NULL,"
                    + "nome_alimento varchar(40),"
                    + "prezzo float,"
                    + "quantita int,"
                    + "data varchar(10),"
                    + "PRIMARY KEY (id)"
                    + ");";
            stmt.executeUpdate(sql); 	  
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void createTableOrdini(String tableName){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progettoap", "root", "");
            Statement stmt = conn.createStatement();
        ) {		      
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" 
                    + "id int NOT NULL,"
                    + "entrate_lordo float,"
                    + "entrate_netto float,"
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
    
    
    private void loadDataOnTable(){
        String sql = "SELECT * FROM " + tableName;
        ObservableList<Data> dataList = FXCollections.observableArrayList();

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);


            // Define table columns and map them to Data class properties
            foodName.setCellValueFactory(new PropertyValueFactory<>("foodName"));
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            price.setCellFactory(column -> new TableCell<Data, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            });
            amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

            // Add columns to TableView
            /*table.getColumns().add(foodName);
            table.getColumns().add(price);
            table.getColumns().add(amount);*/

            while (resultSet.next()) {
                Data data = new Data(
                    resultSet.getString("nome_alimento"),
                    resultSet.getFloat("prezzo"),
                    resultSet.getInt("quantita")
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
    
    
    private String getUltimoOrdineData(String tableName){
        String res = "< ULTIMO ORDINE >\n";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        
        try{
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " ORDER BY ID DESC LIMIT 1");
            
            if (resultSet.next()) {
               res = res + "Data: " + resultSet.getString("data");
               res = res + "\nOra: " + resultSet.getString("ora");
            }
        }
        
        catch(Exception e){
            System.out.println(e);
        }
        
        db.closeConnection();
        return res;
    }
    
    @FXML
    public void ordina(ActionEvent event) throws SQLException{
        //  svuota il database 'ordini_cibo'
            
        //  quando si clicca il pulsante 'ordina' si eseguirà in automatico anche contatta fornitore
        //  il metodo che contatta il fornitore prepara la lista per poi inserirla in una mail da mandare al rifornitore
        ArrayList<String[]> emailBody = getEmail();
        sendEmail(emailBody);
        svuotaDatabase();
    }
    
    private ArrayList<String[]> getEmail(){
        ArrayList<String[]> content = new ArrayList<>();
        
        String sql = "SELECT * FROM " + tableName;

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                content.add(new String[] {resultSet.getString("nome_alimento"), Integer.toString(resultSet.getInt("quantita"))});
            }
        }
        
        catch(Exception e){
            System.out.println(e);
        }
        
        return content;
    }
    
    public void svuotaDatabase() throws SQLException {
        String sql = "TRUNCATE TABLE " + tableName;
        
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/progettoap", "root", "");
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
        
        loadDataOnTable();
    }
    
    private void sendEmail(ArrayList<String[]> data){
        
        // Create the table format
        StringBuilder table = new StringBuilder();
        table.append("<table>");
        for (String[] row : data) {
            table.append("<tr>");
            for (String cell : row) {
                table.append("<td>").append(cell).append("</td>");
            }
            table.append("</tr>");
        }
        table.append("</table>");

        // Set the email body to the table format
        String emailBody = "<html><body>" + table.toString() + "</body></html>";
        
        String rifornitore = "ardeangabbo@gmail.com";
        EmailSender emailer = new EmailSender();
        emailer.sendEmail(rifornitore, emailBody);
    }

    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Rifornimenti.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void modItems(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AurelioPappada.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
