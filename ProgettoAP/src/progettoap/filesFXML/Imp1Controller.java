/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap.filesFXML;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import progettoap.FileLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rdngrl05a04h501o
 */
public class Imp1Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String selectedFile;
    private FileChooser chooser = new FileChooser();
    private String filePath = "../fileData/";
    
    @FXML
    private TextField btn1;
    @FXML
    private TextField btn2;
    @FXML
    private TextField btn3;
    @FXML
    private TextField btn4;
    @FXML
    private TextField btn5;
    @FXML
    private TextField btn6;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void reset(){
        btn1.clear();
        btn2.clear();
        btn3.clear();
        btn4.clear();
        btn5.clear();
        btn6.clear();
    }
    
    @FXML
    public void caricaCV() throws IOException{
        
        String nome, cognome;
        nome = btn1.getText();
        cognome = btn2.getText();
        String filename = null;
            
        if(nome.equals("")!=true && cognome.equals("")!=true){
            File f = chooser.showOpenDialog(null);
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            selectedFile = f.getAbsolutePath();
            System.out.println(f);
            String newPath = "src/progettoap/fileData/";
            File dir=new File(newPath);
            if(!dir.exists()){
                dir.mkdirs();
            }
            File sourceFile, destinationFile;
            String extension = selectedFile.substring(selectedFile.lastIndexOf('.') + 1);


            // fetch person info (name and surname)
            nome = nome.toLowerCase();
            cognome = cognome.toLowerCase();
            filename = nome + "_" + cognome;
            FileLoader CVData = new FileLoader(filename);
        
        
            filePath += filename + "." + extension;
            sourceFile = new File(selectedFile);
            destinationFile = new File(newPath + filename + "." + extension);
            //Files.copy(sourceFile.toPath(),destinationFile.toPath());
            copyFileUsingStream(sourceFile,destinationFile);
        }else{
            showAlert();
        }
    }
    
    private void copyFileUsingStream(File source, File dest)  {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    private void showAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Informazioni mancanti");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Inserire almeno \'nome\' e \'cognome\'");

        alert.showAndWait();
    }
    
    @FXML
    public void save(ActionEvent event) throws IOException {
        reset();
        System.out.println("Salvato!");
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
