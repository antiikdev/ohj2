package vaihe2;

import javafx.application.Application;
import javafx.stage.Stage;
import kysely.Kysely;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * Paaohjelma Kysely-ohjelman kaynnistamiseksi 
 * @author Antiikdev & Doomslizer
 * @version 28.9.2021; 22.10.2021
 *
 */
public class KyselyMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("KyselyGUIView.fxml"));
            final Pane root = ldr.load();
            final KyselyGUIController kyselyCtrl = (KyselyGUIController) ldr.getController();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("kysely.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("kysely");   
            
            // Osoitetaan uusi kysely...
            Kysely kysely = new Kysely();
            kyselyCtrl.setKysely(kysely);
            
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Kaynnistetaan kayttoliittyma
     * @param args komentorivin parametrit
     */
    public static void main(String[] args) {
        launch(args);
    }
}