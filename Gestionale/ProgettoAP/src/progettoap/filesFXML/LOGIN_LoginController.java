/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap.filesFXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rdngrl05a04h501o
 */
public class LOGIN_LoginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    login();
                } catch (IOException ex) {
                    Logger.getLogger(LOGIN_LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    login();
                } catch (IOException ex) {
                    Logger.getLogger(LOGIN_LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    } 

    @FXML
    public void login() throws IOException {
        if(username.getText().equals("admin") == true && password.getText().equals("admin") == true){
            root = FXMLLoader.load(getClass().getResource("MAIN_MainApp.fxml"));
            stage = (Stage)username.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        username.clear();
        password.clear();
    }

}
