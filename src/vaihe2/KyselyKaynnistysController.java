package vaihe2;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;

/**
 * Luokka kyselyn aloitusruudun tapahtumien kontrolloimiseksi
 * @author Antiikdev & Doomslizer
 * @created 14.10.2021
 * @version 14.10.2021
 *
 */
public class KyselyKaynnistysController implements ModalControllerInterface<String>
{    
    //ok nappi
    @FXML void ok() {
        Dialogs.showMessageDialog("Ei oo mittää!");
    }

    //close nappi
    @FXML void sulje() {
        Dialogs.showMessageDialog("Et oo menossa minnekään!");
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }

}