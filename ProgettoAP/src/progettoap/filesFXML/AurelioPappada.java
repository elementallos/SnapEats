package progettoap.filesFXML;

import com.mysql.cj.Session;
import com.mysql.cj.protocol.Message;
import java.io.IOException;
import java.net.PasswordAuthentication;
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
import progettoap.FoodOrdering;
import progettoap.Order;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class AurelioPappada extends RifStage2Controller implements Initializable {

    private Database db = null;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    // inserire dati tableview
    @FXML
    private TableView<Order> table;
    @FXML
    private TableColumn<Order, String> foodName;
    @FXML
    private TableColumn<Order, Float> price;
    @FXML
    private TableColumn<Order, Integer> quantity;
    
    
    private String tableName = "ordini_cibo";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String tableName = "ordini_cibo";
        String ordini = "movimenti";
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        loadDataOnTable();
        
        db.closeConnection();
    }
    
    
    private void loadDataOnTable() {
        String sql = "SELECT * FROM " + tableName;
        ObservableList<Order> orderList = FXCollections.observableArrayList();

        try {
            // create the connection
            Connection connection = db.connect();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            
            foodName.setCellValueFactory(new PropertyValueFactory<>("foodName"));
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            price.setCellFactory(column -> new TableCell<Order, Float>() {
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
            quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            while (resultSet.next()) {
                Order order = new Order(
                    resultSet.getInt("id"),
                    resultSet.getString("nome_alimento"),
                    resultSet.getFloat("prezzo"),
                    (int) (Math.random() * 26 + 25), // Generate a random quantity between 25 and 50
                    resultSet.getString("data")
                );
                orderList.add(order);
            }

            // Add data to TableView
            table.setItems(orderList);
        } 
        
        catch (Exception e) {
            System.out.println(e);
        }
    }


    public void random_items(ActionEvent event) throws SQLException{
        svuotaDatabase();
        
        // now add items
        append_random_items(event);
    }
    
    public void append_random_items(ActionEvent event) throws SQLException{
        // retrieve data
        FoodOrdering food = new FoodOrdering(
                "localhost:3306",
                "root",
                "",
                "progettoap",
                "dispensa",
                "ordini_cibo"
        );
        
        int Min = 10, Max = 30;
        int n = Min + (int)(Math.random() * ((Max - Min) + 1));
        ArrayList<String> list = food.getFoodNames(n);
        
        // upload data
        food.insertOrders(list, Min, Max);
        loadDataOnTable();
    }
    
    public void clear_items(ActionEvent event) throws SQLException{
        svuotaDatabase();
        loadDataOnTable();
    }
    
    
    
    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("RfStage2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
