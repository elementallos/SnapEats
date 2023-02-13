/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap.filesFXML;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rdngrl05a04h501o
 */
public class LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    @FXML
    public void login(ActionEvent event) throws IOException {
        if(username.getText().equals("admin") == true && password.getText().equals("admin") == true){
            root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        username.clear();
        password.clear();
    }
}
