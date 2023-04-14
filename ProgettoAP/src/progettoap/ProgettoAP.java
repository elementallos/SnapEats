/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettoap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    private Database db = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        db = new Database(
                "jdbc:mysql://localhost:3306/progettoap", "root", "", "movimenti"
        );
        
        createBalance();
        
        stage.setTitle("SnapEats");
        stage.getIcons().add(new Image(ProgettoAP.class.getResourceAsStream("/progettoap/images/snapeats_logo_rounded.png")));
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("filesFXML/LOGIN_Login.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
        db.closeConnection();
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
            if(value != 0){
                sendInfoToMovimenti(newBal);
                newBal = calcolaNetto(newBal)[0];
            }
            
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
    
    private void sendInfoToMovimenti(double inLordo) {
        Random random = new Random();
        double bills = 1500 + (3000 - 1500) * random.nextDouble();
        
        // now add the new transaction on the database
        try (Connection conn = db.connect();) {
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();
            
            String query = "INSERT INTO movimenti(id, entrate_lordo, entrate_netto, iva, uscite_pagamenti, uscite_tot, data, ora) VALUES ("
                    + getNextFreeId() + ", "
                    + inLordo + ", "
                    + calcolaNetto(inLordo)[0] + ", " // entrate netto
                    + (int) calcolaNetto(inLordo)[1] + ", " // iva
                    + bills + ", "
                    + bills + ", '"
                    + date.format(now) + "', '"
                    + time.format(now)
                    + "')";
            
            PreparedStatement updateStmt = conn.prepareStatement(query);
            updateStmt.executeUpdate();
        }
        
        catch (Exception e) {
            System.err.println(e);
        }
    }
    
    private double[] calcolaNetto(double in){
        double[] tmp = new double[2];
        if(in <= 15000){
            tmp[1] = 23;
            tmp[0] = in - ((in/100) * tmp[1]);
        }
        
        else if(in > 15000 && in <= 28000){
            tmp[1] = 23;
            tmp[0] = in - ((in/100) * tmp[1]);
        }
        
        else if(in > 28000 && in <= 50000){
            tmp[1] = 38;
            tmp[0] = in - ((in/100) * tmp[1]);
        }
        
        else if(in > 50000){
            tmp[1] = 43;
            tmp[0] = in - ((in/100) * tmp[1]);
        }
        
        return tmp;
    }
    
    public int getNextFreeId() throws SQLException {
        // create the connection
        Connection connection = db.connect();

        // create a SQL SELECT statement to get the maximum ID value
        String sql = "SELECT MAX(id) FROM movimenti";

        // execute the SELECT statement and get the result set
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // get the maximum ID value
        int maxId = 0;
        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }

        // return the next free ID (the maximum ID value plus 1)
        return maxId + 1;
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