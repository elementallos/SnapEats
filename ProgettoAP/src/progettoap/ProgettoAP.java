/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap;

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
        stage.setTitle("SnapEats");
        stage.getIcons().add(new Image(ProgettoAP.class.getResourceAsStream("/progettoap/images/snapeats_logo_rounded.png")));
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("filesFXML/AurelioPappada.fxml"));
        
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
    
}

/*
Tribute to Jupie (september 12, 2023)
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

[ https://www.asciiart.eu/animals/rabbits#:~:text=/%7C%20%20%20%20%20%20__%0A*%20%20%20%20%20%20%20%20%20%20%20%20%20%2B%20%20%20%20%20%20/%20%7C%20%20%20%2C%2D~%20/%20%20%20%20%20%20%20%20%20%20%20%20%20%2B%0A%20%20%20%20%20.%20%20%20%20%20%20%20%20%20%20%20%20%20%20Y%20%3A%7C%20%20//%20%20/%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%20%20%20%20%20%20%20%20*%0A%20%20%20%20%20%20%20%20%20.%20%20%20%20%20%20%20%20%20%20%7C%20jj,I%20%20%20%20%20!%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%5D%5C%20%20%20%20%20%20_%5C%20%20%20%20/%22%5C%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20(%22%20~%2D%2D%2D%2D(%20~%20%20%20Y.%20%20)%0A%20%20%20%20%20%20%20%20%20%20~~~~~~~~~~~~~~~~~~~~~~~~~~ ]
*/