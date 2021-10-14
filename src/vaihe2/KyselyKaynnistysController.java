package vaihe2;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;

/**
 * Luokka kyselyn aloitusruudun tapahtumien kontrolloimiseksi
 * @author Antiikdev & Doomslizer
 * @created 14.10.2021
 * @version 14.10.2021
 *
 */
public class KyselyKaynnistysController
{    
    //ok nappi
    @FXML void ok()
    {
        Dialogs.showMessageDialog("Ei oo mittää!");
    }

    //close nappi
    @FXML void sulje()
    {
        Dialogs.showMessageDialog("Et oo menossa minnekään!");
    }

}