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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class RifStage2Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Label ultimoOrdineData;
    // inserire dati tableview
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ultimoOrdineData.setText(getUltimoOrdineData());
        ultimoOrdineData.setWrapText(true);
    }  
    
    private String getUltimoOrdineData(){
        return "<data ultimo ordine effetuato>"; // da sostituire!
    }
    
    @FXML
    public void ordina(ActionEvent event){
        System.out.println("Ordine effetuato!");
    }
    
    @FXML
    public void contattaFornitore(ActionEvent event){
        System.out.println("Fornitore contattato!");
    }
    
    @FXML
    public void indietro(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Rifornimenti.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
