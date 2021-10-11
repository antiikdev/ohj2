package vaihe2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * Kysely-ohjelman paaohjelma
 * @author Antiikdev & Doomslizer
 * @version 28.9.2021
 *
 */
public class KyselyMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("KyselyGUIView.fxml"));
            final Pane root = ldr.load();
            //final KyselyGUIController kyselyCtrl = (KyselyGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("kysely.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("kysely");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}