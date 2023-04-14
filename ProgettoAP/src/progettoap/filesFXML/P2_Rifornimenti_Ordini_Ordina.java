package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import progettoap.Database;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class P2_Rifornimenti_Ordini_Ordina extends P2_Rifornimenti_Ordini implements Initializable {

    private Database db = null;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label foo;
    @FXML
    private TextField bar;
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> foodName;
    @FXML
    private TableColumn<Data, Integer> avalAmount;
    @FXML
    private TableColumn<Data, Integer> usedAmount;
    
    
    private String tableName = "ordini_cibo";
    private int index = 0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        loadDataOnTable("dispensa");
        setDetails(0);
        db.closeConnection();
        
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = table.getSelectionModel().getSelectedIndex();
                index = selectedIndex;
                
                setDetails(index);
            }
        });
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
            foodName.setCellValueFactory(new PropertyValueFactory<>("foodName"));
            avalAmount.setCellValueFactory(new PropertyValueFactory<>("avalAmount"));
            usedAmount.setCellValueFactory(new PropertyValueFactory<>("usedAmount"));

            while (resultSet.next()) {
                Data data = new Data(
                    resultSet.getInt("id"),
                    resultSet.getString("nome_alimento"),
                    resultSet.getInt("quantita_disponibile"),
                    resultSet.getInt("quantita_utilizzata")
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
    
    private void setDetails(int index){
        String txt = (String) table.getColumns().get(0).getCellObservableValue(index).getValue();
        foo.setText(txt);
        
        float quantita = 0;
        bar.setText(Float.toString(quantita));
    }
    
    public void salva() throws SQLException{
        try{
            int quantity = Integer.parseInt(bar.getText()); // if text is wrong, there will be no changes
            
            // now change value from database to the given one
            acceptIncomingData(quantity);
            
            // show message
            System.out.println("Item added to your database");
            
        }catch(NumberFormatException e){
            System.out.println(e);
        }
    }
    
    private void acceptIncomingData(int incomingData) throws SQLException {
        String nameOfFood = (String) table.getColumns().get(0).getCellObservableValue(index).getValue();
        float price = getPrice(nameOfFood);

        try (Connection connection = db.connect()) {
            PreparedStatement checkStatement = connection.prepareStatement("SELECT nome_alimento FROM " + tableName + " WHERE nome_alimento = ?");
            checkStatement.setString(1, nameOfFood);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // food item already exists, update its quantity and price
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + tableName + " SET quantita = ?, prezzo = ? WHERE nome_alimento = ?");
                updateStatement.setInt(1, incomingData);
                updateStatement.setFloat(2, price);
                updateStatement.setString(3, nameOfFood);
                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Food item updated");
                }
            } else {
                // food item doesn't exist, insert new row with current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String currentDate = dateFormat.format(date);

                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " (nome_alimento, quantita, prezzo, data) VALUES (?, ?, ?, ?)");
                insertStatement.setString(1, nameOfFood);
                insertStatement.setInt(2, incomingData);
                insertStatement.setFloat(3, price);
                insertStatement.setString(4, currentDate);
                int rowsInserted = insertStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("New food item inserted");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    private float getPrice(String foodName) throws SQLException {
        float price = 0;
        String sql = "SELECT prezzo FROM dispensa WHERE nome_alimento = ?";
        try {
            Connection connection = db.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, foodName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                price = resultSet.getFloat("prezzo");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return price;
    }


    
    
    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("P2_Rifornimenti_Ordini.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
