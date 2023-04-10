/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author rdngrl05a04h501o
 */
public class ProgettoAP extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        createBalance();
        
        stage.setTitle("SnapEats");
        stage.getIcons().add(new Image(ProgettoAP.class.getResourceAsStream("/progettoap/images/snapeats_logo_rounded.png")));
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("filesFXML/MainApp.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void createBalance(){
        String filename = "balance.dat";
        try {
            DataWriter balance = new DataWriter(filename);
            DataReader actualBal = new DataReader(filename);
            double value = checkFirstDayOfMonth();
            double newBal = actualBal.readDoubleFromFile() + value;
            
            balance.writeDoubleToFile(newBal, false);
            //balance.writeDoubleToFile(56770.0, true);
        } catch (IOException ex) {
            Logger.getLogger(ProgettoAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double checkFirstDayOfMonth() {
        LocalDate currentDate = LocalDate.now();

        if (currentDate.getDayOfMonth() == 1) {
            Random random = new Random();
            double randomDouble = 45000 + (60000 - 45000) * random.nextDouble();
            return randomDouble;
        }
        return 0;
    }
}

/*
Tribute to Jupie (september 12, 2022)
Thanks to Aurelio Pappadà and Gabriel Ardean for contributing to this project.

< I wish the best for everyone! >




                      /|      __
*             +      / |   ,-~ /             +
     .              Y :|  //  /                .         *
         .          | jj /( .^     *
               *    >-"~"-v"              .        *        .
*                  /       Y
   .     .        jo  o    |     .            +
                 ( ~T~     j                     +     .
      +           >._-' _./         +
               /| ;-"~ _  l
  .           / l/ ,-"~    \     +
              \//\/      .- \
       +       Y        /    Y          "thank you Ardean"
               l       I     !                  "thank you Pappadà"
               ]\      _\    /"\
              (" ~----( ~   Y.  )
          ~~~~~~~~~~~~~~~~~~~~~~~~~~

[ https://www.asciiart.eu ]
*/