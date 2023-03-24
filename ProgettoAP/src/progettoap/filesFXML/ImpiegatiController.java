/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import progettoap.Data;
import progettoap.DataWriter;
import progettoap.Database;

/**
 * FXML Controller class
 *
 * @author rdngrl05a04h501o
 */
public class ImpiegatiController implements Initializable {
    
    private Database db = null;
    String tableName = "impiegati";
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, Integer> id;
    @FXML
    private TableColumn<Data, String> nome;
    @FXML
    private TableColumn<Data, String> cognome;
    
    @FXML
    private Label info;
    
    private int impID = 0;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTable();
        loadDataOnTable();
        setDetails(0);
        
        db.closeConnection();
        
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = table.getSelectionModel().getSelectedIndex();
                impID = selectedIndex;
                
                setDetails(impID);
            }
        });

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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);


            // Define table columns and map them to Data class properties
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            cognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

            while (resultSet.next()) {
                Data data = new Data(
                    resultSet.getInt("id"),
                    resultSet.getString("nome"),
                    resultSet.getString("cognome")
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
    
    private void setDetails(int impID){
        String res = "";
        
        try{
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id = " + (impID + 1));
            
            if (resultSet.next()) {
                res = "Nome:\t" + resultSet.getString("nome") +
                        "\nCog.:\t" + resultSet.getString("cognome") +
                        "\nDDN:\t" + resultSet.getString("data_nascita") +
                        "\nCF:\t" + resultSet.getString("codice_fiscale") +
                        "\nCell.:\t" + resultSet.getString("telefono") +
                        "\n\n" + resultSet.getString("email");
            }
        }
        
        catch(Exception e){
            System.out.println(e);
        }
        
        info.setText(res);
    }
    
    @FXML
    public void del(ActionEvent event){
        boolean go = showAlert();
        if(go == false){
            return;
        }
        
        String sql = "DELETE FROM " + tableName + " WHERE id = " + (impID + 1);

        try{
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            int rowsInserted = statement.executeUpdate(sql);

            if (rowsInserted > 0) {
                System.out.println("A row has been deleted");
            }
        }

        catch(Exception e){
            System.out.println(e);
        }
        
        setDetails(--impID);
        loadDataOnTable();
    }
    
    private boolean showAlert(){
        boolean bool = false;
        
        int choice = JOptionPane.showOptionDialog(
            null, // parent component (null for default)
            "Vuoi confermare l'operazione?", // message to display
            "Conferma", // title of dialog box
            JOptionPane.YES_NO_OPTION, // option type
            JOptionPane.QUESTION_MESSAGE, // message type
            null, // icon (null for default)
            new String[] { "Conferma", "Annulla" }, // text for each button
            "default"); // default button (null for none)

        if (choice == JOptionPane.YES_OPTION) {
            System.out.println("L'utente ha confermato l'operazione.");
            bool = true;
        } else if (choice == JOptionPane.NO_OPTION) {
            System.out.println("L'utente ha annullato l'operazione.");
        }
        
        return bool;
    }
    
    
    
    
    @FXML
    public void mod(ActionEvent event) throws IOException {
        
        DataWriter writer = new DataWriter("data.dat");
        writer.writeIntToFile(impID);


        root = FXMLLoader.load(getClass().getResource("ImpiegatoModifica.fxml"));
        stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 
    
    @FXML
    public void add(ActionEvent event) throws IOException {
        System.out.println(impID);
        
        root = FXMLLoader.load(getClass().getResource("ImpiegatoAggiungi.fxml"));
        stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
}
