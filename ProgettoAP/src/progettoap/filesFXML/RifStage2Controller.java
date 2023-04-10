package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
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
    private TableColumn<Data, Float> amount;
    
    
    @FXML
    private Label ultimoOrdineData;
    
    private String tableName = "ordini_cibo";
    private ObservableList<Data> list = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String tableName = "ordini_cibo";
        String ordini = "movimenti";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTableOrdini(ordini);
        createTable();
        list = loadDataOnTable();
        
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
                    + "quantita float,"
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
    
    
    private ObservableList loadDataOnTable(){
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
            amount.setCellValueFactory(new PropertyValueFactory<>("quantity"));

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
            
            return dataList;
        }

        catch(Exception e){
            System.out.println(e);
        }
        
        return null;
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
        if(emailBody.isEmpty() != true){
            //sendEmail(emailBody);
            sendInfoToMovimenti();
            updateFoodInventory(list);
            svuotaDatabase();
        }else{
            System.out.println("Cannot order empty list!");
        }
    }
    
    public void updateFoodInventory(ObservableList<Data> dataList) {
        try {
            // Establish database connection
            Connection conn = db.connect();

            // Loop through each item in the ObservableList
            for (Data data : dataList) {
                // Retrieve the current quantity of the food from the database
                PreparedStatement stmt = conn.prepareStatement("SELECT quantita_disponibile FROM dispensa WHERE nome_alimento = ?");
                stmt.setString(1, data.getFoodName());
                ResultSet rs = stmt.executeQuery();
                int currentQuantity = 0;
                if (rs.next()) {
                    currentQuantity = rs.getInt("quantita_disponibile");
                }

                // Calculate the new quantity of the food
                double newQuantity = currentQuantity + data.getQuantity();

                // Update the database with the new quantity of the food
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE dispensa SET quantita_disponibile = ? WHERE nome_alimento = ?");
                updateStmt.setDouble(1, newQuantity);
                updateStmt.setString(2, data.getFoodName());
                updateStmt.executeUpdate();
            }

            // Close database connection
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    
    private void sendInfoToMovimenti() {
        float total = 0;

        try (Connection conn = db.connect();) {
            // Create a statement to execute the SQL query
            String query = "SELECT prezzo, quantita FROM " + tableName;
            Statement stmt = conn.createStatement();

            // Execute the SQL query and iterate through the results
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // Add the product of prezzo and quantita to the total
                float prezzo = rs.getFloat("prezzo");
                int quantita = rs.getInt("quantita");
                total += prezzo * quantita;
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
            
            String query = "INSERT INTO movimenti(id, uscite_rifornimenti, uscite_tot, data, ora) VALUES ("
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
        
        // when it's done, an alert pops out
        alert(rifornitore);
    }
    
    public void alert(String rifornitore){
        Alert alert = new Alert(AlertType.INFORMATION,"");
        alert.setTitle("Email Sender");
        alert.setHeaderText("Mail mandata con successo!");
        alert.setContentText("La mail è stata mandata con successo al rifornitore.\n\nRifornitore: " + rifornitore);
        alert.showAndWait();
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
