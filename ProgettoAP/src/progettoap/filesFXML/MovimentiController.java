package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
public class MovimentiController implements Initializable {

    private Database db = null;
    private String tableName = "movimenti";
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    // inserire dati tableview
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, Integer> id;
    @FXML
    private TableColumn<Data, Double> a, b, c, d, e, f, g;
    @FXML
    private TableColumn<Data, String> date, time;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", tableName
        );
        loadDataOnTable();
        
        db.closeConnection();
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
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            a.setCellValueFactory(new PropertyValueFactory<>("inNetto"));
            b.setCellValueFactory(new PropertyValueFactory<>("inLordo"));
            c.setCellValueFactory(new PropertyValueFactory<>("iva"));
            d.setCellValueFactory(new PropertyValueFactory<>("outPag"));
            e.setCellValueFactory(new PropertyValueFactory<>("outImp"));
            f.setCellValueFactory(new PropertyValueFactory<>("outRif"));
            g.setCellValueFactory(new PropertyValueFactory<>("outTot"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            time.setCellValueFactory(new PropertyValueFactory<>("time"));

            while (resultSet.next()) {
                Data data = new Data(
                        resultSet.getInt("id"),
                        resultSet.getFloat("entrate_lordo"),
                        resultSet.getFloat("entrate_netto"),
                        resultSet.getFloat("iva"),
                        resultSet.getFloat("uscite_pagamenti"),
                        resultSet.getFloat("uscite_impiegati"),
                        resultSet.getFloat("uscite_rifornimenti"),
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
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Contabilita.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
