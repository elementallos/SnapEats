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
public class P2_RifornimentiController implements Initializable {

    private Database db = null;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, Integer> id;
    @FXML
    private TableColumn<Data, String> foodName;
    @FXML
    private TableColumn<Data, Integer> avalAmount;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String tableName = "dispensa";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        createTable(tableName);
        loadDataOnTable(tableName);
        
        db.closeConnection();
    }
    
    private void createTable(String tableName){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/progettoap", "root", "");
            Statement stmt = conn.createStatement();
        ) {		      
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" 
                    + "id int NOT NULL,"
                    + "nome_alimento varchar(40),"
                    + "quantita_disponibile float,"
                    + "quantita_utilizzata float,"
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);


            // Define table columns and map them to Data class properties
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            foodName.setCellValueFactory(new PropertyValueFactory<>("foodName"));
            avalAmount.setCellValueFactory(new PropertyValueFactory<>("avalAmount"));

            // Add columns to TableView
            /*table.getColumns().add(id);
            table.getColumns().add(foodName);
            table.getColumns().add(avalAmount);
            table.getColumns().add(usedAmount);*/

            while (resultSet.next()) {
                Data data = new Data(
                    resultSet.getInt("id"),
                    resultSet.getString("nome_alimento"),
                    resultSet.getInt("quantita_disponibile")
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
    
    private String getPreviewData(){
        return "<preview>"; // sostituire!
    }
    
    @FXML
    public void magazzino(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("P2_Rifornimenti_Magazzino.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 
    
    @FXML
    public void rifornimenti(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("P2_Rifornimenti_Ordini.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
        root = FXMLLoader.load(getClass().getResource("MAIN_MainApp.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
