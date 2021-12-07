package vaihe2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import kysely.Kysely;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * Paaohjelma Kysely-ohjelman kaynnistamiseksi 
 * @author Antiikdev & Doomslizer
 * @version 28.9.2021; 22.10.2021, 22.11.2021
 *
 */
public class KyselyMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("KyselyGUIView.fxml"));
            final Pane root = ldr.load();
            final KyselyGUIController kyselyCtrl = (KyselyGUIController) ldr.getController();
            
            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("kysely.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kysely");
            
            // -------- HT5-vaihetta - Osoitetaan uusi kysely -----
            Kysely kysely = new Kysely();
            kyselyCtrl.setKysely(kysely);
            
            primaryStage.show();
            //  --------------------------------------------------   

            
            //  --------  HT6-vaihetta - tiedoston luku -------
            // HT6: Ilmoitetaan suoraan tassa versiossa juuressa oleva tiednimi:
            kyselyCtrl.lueTiedosto("koehenkilot.dat");
            
            /** Vaihtoehtoinen (otettu mallia (HT6): KerhoMain.java):
            // Parameters ilmeisesti ottaa komentorivilta tiedostonimen?
            Application.Parameters params = getParameters();
            if ( params.getRaw().size() > 0 )
                kyselyCtrl.lueTiedosto(params.getRaw().get(0));
            else
                if ( !kyselyCtrl.avaa() ) Platform.exit();
             */
            
            } catch(Exception e) {
                e.printStackTrace();
            }
            
            //  --------------------------------------------------   
    }

    
    /**
     * Kaynnistetaan kayttoliittyma
     * @param args komentorivin parametrit
     */
    public static void main(String[] args) {
        launch(args);
    }
}