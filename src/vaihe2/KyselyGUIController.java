package vaihe2;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;

/**
 * Luokka kyselyn käyttöliittymän tapahtumien kontrolloimiseksi
 * @author Antiikdev & Doomslizer
 * @created 28.9.2021
 * @version 11.10.2021
 *
 */
public class KyselyGUIController {
    
    // MENUBAR ITEMIT: Tiedosto
    @FXML void avaaKysely() {
        ModalController.showModal(KyselyGUIController.class.getResource("KyselyKaynnistysView.fxml"), "Kysely", null, "");
    }
    
    @FXML void tallenna() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    @FXML void lopeta() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    // MENUBAR ITEMIT: Muokkaa
    @FXML void lisaaUusiVastaus() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    @FXML void poistaVastaus() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    // MENUBAR ITEMIT: Muokkaa
    @FXML void apua() {
        Dialogs.showMessageDialog("Apua ei ole saatavilla!");
    }
    
    @FXML void tiedot() {
        Dialogs.showMessageDialog("Kysely-ohjelma, ver. X, (c) Antiikdev & Doomslizer, 2021");
    }
    
    
    // VASTAUSTEN KASITTELY
    @FXML void uusiVastaus() {
        Dialogs.showMessageDialog("Uuden vastauksen lisäys ei vielä toimi!");
    }
    
    @FXML void tallennaVastaus() {
        Dialogs.showMessageDialog("Vastauksen tallennus ei vielä toimi!");
    }
    
        
}