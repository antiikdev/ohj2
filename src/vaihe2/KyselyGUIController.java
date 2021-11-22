package vaihe2;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import kysely.Koehenkilo;
import kysely.Kysely;
import kysely.Kysymys;
import kysely.TallennaException;
import kysely.Vastaus;

/**
 * Luokka kyselyn käyttöliittymän tapahtumien kontrolloimiseksi
 * @author Antiikdev & Doomslizer
 * @version 28.9.2021 - ensimmainen GUI-versio
 * @version 11.10.2021 - nappainten toimimattomuus ilmoitukset lisatty
 * @version 3.11.2021 - kysymykset lisatty
 *
 */
public class KyselyGUIController implements Initializable {
    
    @FXML private ListChooser<Koehenkilo> chooserKoehenkilot; 
    @FXML private ScrollPane panelKoehenkilo;
    
    
    /**
     * Alustetaan
     * @param url url
     * @param bundle bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    
// ================== TOIMINNOT ========================
    
    // -------- MENUBAR ITEMIT: Tiedosto -------
    @FXML void handleAvaaKysely() {
        ModalController.showModal(KyselyGUIController.class.getResource("KyselyKaynnistysView.fxml"), "Kysely", null, "");
    }
    
    @FXML void handleTallenna() {
        // HT6: tallennus
        tallenna();
        // Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    @FXML void handleLopeta() {
        // HT6: Tallennetaan ja lopetetaan
        tallenna();
        Platform.exit();
        // Dialogs.showMessageDialog("Älä mene njet njet!");
    }
    
    // ---------- MENUBAR ITEMIT: Muokkaa ----------
    @FXML void handleLisaaUusi() {
        lisaaUusiKoehenkiloKyselyyn();
        // Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    @FXML void handlePoistaTama() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    // ---------- MENUBAR ITEMIT: Tietoja ---------- 
    @FXML void handleApua() {
        Dialogs.showMessageDialog("Apua ei ole saatavilla!");
    }
    
    @FXML void handleTiedot() {
        Dialogs.showMessageDialog("Kysely-ohjelma, ver. X, (c) Antiikdev & Doomslizer, 2021");
    }
    
    
// ----------------------------------------------------------------------------
// HT5: Koehenkiloiden, kysymysten ja vastausten lisaaminen
// ----------------------------------------------------------------------------
    @FXML void handleUusiKoehenkilo() {
        // Dialogs.showMessageDialog("Uuden vastauksen lisäys ei vielä toimi!");
        lisaaUusiKoehenkiloKyselyyn();
    }
    
    @FXML void handleTallennaTama() {
        tallenna();
        // Dialogs.showMessageDialog("Vastauksen tallennus ei vielä toimi!");
    }
    
    @FXML void handleUusiKysymys() {
        lisaaUusiKysymys();
    }
    
    @FXML void handleUusiVastaus() {
        lisaaUusiVastaus();
    }
// ----------------------------------------------------------------------------    
    

// =======================================================================
//  Tasta eteenpain kayttoliittymaan EI suoraan liittyvaa koodia
//=======================================================================
    
// ----------------------------------------------------------------------------
// HT6: Koehenkiloiden, kysymysten ja vastausten lisaaminen (tiedostoon luku ja kirjoittaminen)
//      - sisaltaa myos muokattuja metodeja aikaisemmista tyovaiheista. 
// ----------------------------------------------------------------------------
    
    // HT6: Esitelty alla "kyselynimi"-attribuutti
    private String kyselynimi = "";
    private Kysely kysely;
    private Koehenkilo koehenkiloKohdalla;
    private TextArea areaKoehenkilo = new TextArea(); // Poistetaan lopuksi tama tekstialue
   
   /**
    * Alustaa kyselyn lukemalla sen valitun nimisestä tiedostosta
    * @param nimi tiedosto josta luetaan
    * @return null jos onnistuu, muuten virhe tekstina
    */
    protected String lueTiedosto(String nimi) {
        // Tassa voisi asettaa kyselynimen, jolle "kyselynimi"-attribuuttikin olisi valmiina:
        if ( nimi.length() < 0) kyselynimi = "koehenkilot.dat";
        else kyselynimi = nimi;
        // setTitle("Kysely - " + kyselynimi);
        
        try {
            kysely.lueTiedostosta(kyselynimi);
            hae(0);
            return null;
        } catch (TallennaException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }
    
    
    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        // String uusinimi = KyselyKaynnistysController.kysyNimi(null, kyselynimi);
        // if (uusinimi == null) return false;
        lueTiedosto(kyselynimi);
        return true;
    }

    
    /**
     * Tietojen tallennus
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    private String tallenna() {
        try {
            kysely.tallenna();
            return null;
        } catch (TallennaException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    
    /**
     * 
     */
    protected void naytaJasen() {
        koehenkiloKohdalla = chooserKoehenkilot.getSelectedObject();

        if (koehenkiloKohdalla == null) {
            areaKoehenkilo.clear();
            return;
        }

        areaKoehenkilo.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaKoehenkilo)) {
            tulosta(os,koehenkiloKohdalla); 
        }
    }
    
    
    /*
     * Naytetaan koehenkilo ja kysymykset
     */
    private void tulosta(PrintStream os, final Koehenkilo koehenkilo) {
        os.println("----------------------");
        koehenkilo.tulosta(os);
        os.println("----------------------");
        
        // TODO: HT6 kysymykset ja vastaukset
        try {
            List<Kysymys> kysymykset = kysely.annaKysymykset(koehenkilo);
            for (Kysymys kys:kysymykset) 
                kys.tulosta(os);     
        } catch (TallennaException ex) {
            Dialogs.showMessageDialog("Kysymysten hakemisessa ongelmia! " + ex.getMessage());
        }  
    }
    
    
    /** 
     * Lisaa uuden kysymyksen koehenkilolle
     */ 
    public void lisaaUusiKysymys() { 
        if ( koehenkiloKohdalla == null ) return;
        Kysymys kys = new Kysymys();
        kys.rekisteroi();
        kys.taytaEsimKysymysTiedot(koehenkiloKohdalla.getKoehenkiloNro());
        try {
            kysely.lisaa(kys);
        } catch (TallennaException e) {
            Dialogs.showMessageDialog("Ongelmia lisaamisessa! " + e.getMessage());
        }
        hae(koehenkiloKohdalla.getKoehenkiloNro());
    } 
    
    
    // TODO: lisaaUusiVastaus
    
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
    

// ----------------------------------------------------------------------------   
// HT5 tai sitä aikaisempaa koodia 
// ----------------------------------------------------------------------------   
    
    /**
     * Muut alustukset ja Gridpanelin tilalle tekstikentta,
     * johon koehenkilon tiedot. Myos koehenkilolistan kuuntelijan alustus
     */
    protected void alusta() {
        panelKoehenkilo.setContent(areaKoehenkilo);
        areaKoehenkilo.setFont(new Font("Courier New", 12));
        panelKoehenkilo.setFitToHeight(true);
        chooserKoehenkilot.clear(); 
        chooserKoehenkilot.addSelectionListener(e -> naytaKoehenkilo());
    }
    
    
    /*
     * Naytetaan koehenkilo
     */
    private void naytaKoehenkilo() {
        koehenkiloKohdalla = chooserKoehenkilot.getSelectedObject();
        
        if (koehenkiloKohdalla == null) return;
        
        areaKoehenkilo.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaKoehenkilo)) {
                tulosta(os, koehenkiloKohdalla);
        }
    }
    
    
    /**
     * Haetaan jasenten tiedot listaan
     * @param knro koehenkilon numero, joka aktivoidaan haun jälkeen
     */
    private void hae (int knro) {
        chooserKoehenkilot.clear();
        
        int index = 0;
        for (int i = 0; i < kysely.getKoehenkiloita(); i++) {
            Koehenkilo koehenkilo = kysely.annaKoehenkilo(i);
            if (koehenkilo.getKoehenkiloNro() == knro) index = i;
            chooserKoehenkilot.add(koehenkilo.getNimi(), koehenkilo);
        }
        chooserKoehenkilot.setSelectedIndex(index); // muutosviesti
    }
    
    /*
     * Lisaa kyselyyn uuden vastauksen
     */
    private void lisaaUusiKoehenkiloKyselyyn() {
        Koehenkilo koehenkilo = new Koehenkilo();
        koehenkilo.rekisteroi();
        koehenkilo.taytaEsimTiedot(); // TODO: Korvattava dialogilla
        try {
            kysely.lisaa(koehenkilo);
        } catch (TallennaException e) {
            Dialogs.showMessageDialog("Ongelmia uuden vastauksen luomisessa.");
            return;
        }
        hae(koehenkilo.getKoehenkiloNro());
    }
    

    /** 
     * Lisaa uuden vastauksen koehenkilolle
     */ 
    public void lisaaUusiVastaus() { 
        if ( koehenkiloKohdalla == null ) return;
        Vastaus vas = new Vastaus();
        vas.rekisteroi();
        vas.taytaEsimVastausTiedot(koehenkiloKohdalla.getKoehenkiloNro());
        kysely.lisaa(vas);
        hae(koehenkiloKohdalla.getKoehenkiloNro());
    } 
    
    
    /**
     * Asetetaan kaytettava kysely
     * @param kysely jota kaytetaan
     */
    public void setKysely(Kysely kysely) {
        this.kysely = kysely;
    }
    
        
}