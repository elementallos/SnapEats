/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rdngrl05a04h501o
 */
public class Imp2Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private TextField btn1;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void reset(){
        btn1.clear();
    }
    
    @FXML
    public void save(ActionEvent event) throws IOException {
        reset();
        System.out.println("Impiegato licenziato!");
    }
    
    @FXML
    public void aggiungi(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Imp1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void licenzia(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Imp2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void modifica(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Imp3.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
