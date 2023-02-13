/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class ContabilitaController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label listaMov;
    @FXML
    private Label recentMov;
    
    @FXML
    private Button movDD;
    @FXML
    private Button movSS;
    @FXML
    private Button movMM;   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaMov.setFont(new Font("Consolas", 20));
        listaMov.setText("lista movimenti -->");
        recentMov.setText("movimento piu recente -->");
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
    
    @FXML
    public void stipendi(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Stipendi.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    // update the list
    @FXML
    public void showMovDD(ActionEvent event) {
        listaMov.setText(getMovFromFile(0));
    }
    
    @FXML
    public void showMovSS(ActionEvent event) {
        listaMov.setText(getMovFromFile(1));
    }
    
    @FXML
    public void showMovMM(ActionEvent event) {
        listaMov.setText(getMovFromFile(2));
    }
    
    private String getMovFromFile(int mod){
        /*  0: daily
            1: weekly
            2: monthly
         */
        switch (mod) {
            case 0:
                return "Giornaliero -->";
            case 1:
                return "Settimanale -->";
            case 2:
                return "Mensile -->";
            default:
                return "";
        }
    }
}
