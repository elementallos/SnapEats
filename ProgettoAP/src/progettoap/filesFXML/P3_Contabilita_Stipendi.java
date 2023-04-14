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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import progettoap.Data;
import progettoap.DataReader;
import progettoap.DataWriter;
import progettoap.Database;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class P3_Contabilita_Stipendi implements Initializable {
    private Database db = null;
    private int impID = 0;
    private String tableName = "impiegati";
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label bilancio;
    @FXML
    private Label data;
    
    @FXML
    private TextField hh, ss;
    
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
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTable();
        loadDataOnTable();
        double val = outputBalance();
        outputData(0);
        
        db.closeConnection();
        
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = table.getSelectionModel().getSelectedIndex();
                impID = selectedIndex;
                
                outputData(impID);
            }
        });
        
        // scalare i soldi dal bilancio per pagare gli indipendenti
        try{
            String filename = "balance.dat";
            if(checkFirstDayOfMonth() == true){
                sendInfoToMovimenti();
                DataWriter w = new DataWriter(filename);
                DataReader r = new DataReader(filename);
                
                double newVal = r.readDoubleFromFile() - val;
                w.writeDoubleToFile(newVal, true);
                
                resetOreLavorate();
                loadDataOnTable();
                outputBalance();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    private void sendInfoToMovimenti() {
        float total = 0;

        try (Connection conn = db.connect();) {
            // Create a statement to execute the SQL query
            String query = "SELECT stipendio FROM " + tableName;
            Statement stmt = conn.createStatement();

            // Execute the SQL query and iterate through the results
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                float stipendio = rs.getInt("stipendio");
                total += stipendio;
            }
        }
        
        catch (Exception e) {
            System.err.println(e);
        }
        
        
        // now add the new transaction on the database
        try (Connection conn = db.connect();) {
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();
            
            String query = "INSERT INTO movimenti(id, uscite_impiegati, uscite_tot, data, ora) VALUES ("
                    + getNextFreeId() + ", "
                    + total + ", "
                    + total + ", '"
                    + date.format(now) + "', '"
                    + time.format(now)
                    + "')";
            
            PreparedStatement updateStmt = conn.prepareStatement(query);
            updateStmt.executeUpdate();
        }
        
        catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public int getNextFreeId() throws SQLException {
        // create the connection
        Connection connection = db.connect();

        // create a SQL SELECT statement to get the maximum ID value
        String sql = "SELECT MAX(id) FROM movimenti";

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
    
    public void salva() throws SQLException{
        float newSal = Float.parseFloat(hh.getText()) * Float.parseFloat(ss.getText());
        String sql = "UPDATE " + tableName + " SET " + 
                "ore_lavorate = '" + hh.getText() + "', " +
                "paga_oraria = '" + ss.getText() + "', " +
                "stipendio = '" + Float.toString(newSal) + "'" +
                " WHERE id = " + (impID + 1);

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            int rowsInserted = statement.executeUpdate(sql);

            if (rowsInserted > 0) {
                System.out.println("A row has been updated");
            }
            
            loadDataOnTable();
            outputBalance();
        }

        catch(Exception e){
            System.out.println(e);
        }
    }
    
    private void createTable(){
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
    
    private void loadDataOnTable(){
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
    
    
    private double outputBalance(){
        try{
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(stipendio) FROM " + tableName);
            
            double sum = 0;
            while (resultSet.next()){
                float c = resultSet.getFloat(1);
                sum = (double) c;
            }
            
            DataReader reader = new DataReader("balance.dat");
            double bal = reader.readDoubleFromFile();
            bilancio.setText(
                    "Bilancio totale:        € " + bal + "\n" +
                    "Stipendi da pagare:     € "+ sum);
            
            return sum;
        }
        
        catch(Exception e){
            System.out.println(e);
        }
        
        return 0;
    }
    
    private void outputData(int index){
        String res = "";
        
        try{
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id = " + (impID + 1));
            
            if (resultSet.next()) {
                res = "NOME:\n" + resultSet.getString("nome") +
                        "\nCOGNOME:\n" + resultSet.getString("cognome");
                hh.setText(Float.toString(resultSet.getFloat("ore_lavorate")));
                ss.setText(Float.toString(resultSet.getFloat("paga_oraria")));
            }
        }
        
        catch(Exception e){
            System.out.println(e);
        }
        
        data.setText(res);
    }
    
    private boolean checkFirstDayOfMonth() {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.getDayOfMonth() == 1) {
            return true;
        }
        return false;
    }
    
    public void resetOreLavorate() {
        try {
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE " + tableName + " SET ore_lavorate = 0, stipendio = 0");

            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    
    
    
    
    
    @FXML
    public void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LOGIN_Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 
    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("P3_Contabilita.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 
}
