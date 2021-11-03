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
    @FXML void avaaKysely() {
        ModalController.showModal(KyselyGUIController.class.getResource("KyselyKaynnistysView.fxml"), "Kysely", null, "");
    }
    
    @FXML void tallenna() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    @FXML void lopeta() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    // ---------- MENUBAR ITEMIT: Muokkaa ----------
    @FXML void lisaaUusi() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    @FXML void poistaTama() {
        Dialogs.showMessageDialog("Ei vielä toimi!");
    }
    
    // ---------- MENUBAR ITEMIT: Tietoja ---------- 
    @FXML void apua() {
        Dialogs.showMessageDialog("Apua ei ole saatavilla!");
    }
    
    @FXML void tiedot() {
        Dialogs.showMessageDialog("Kysely-ohjelma, ver. X, (c) Antiikdev & Doomslizer, 2021");
    }
    
    
    // -------------------------------------------------------
    // Koehenkiloiden, kysymysten ja vastausten lisaaminen (HT5)
    // -------------------------------------------------------
    @FXML void uusiKoehenkilo() {
        // Dialogs.showMessageDialog("Uuden vastauksen lisäys ei vielä toimi!");
        lisaaUusiKoehenkiloKyselyyn();
    }
    
    @FXML void tallennaTama() {
        Dialogs.showMessageDialog("Vastauksen tallennus ei vielä toimi!");
    }
    
    @FXML void uusiKysymys() {
        lisaaUusiKysymys();
    }
    
    
    
// =============================================================
// TASTA ETEENPAIN KAYTTOLIITTYMAAN SUORAAN LIITTYVAA KOODIA
// =============================================================
    
    // Esitellaan kysely-attribuutti
    private Kysely kysely;
    private Koehenkilo koehenkiloKohdalla;
    private TextArea areaKoehenkilo = new TextArea(); // Poistetaan lopuksi tama tekstialue
    
    
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
    
    
    /*
     * Naytetaan koehenkilo ja kysymykset
     */
    private void tulosta(PrintStream os, final Koehenkilo koehenkilo) {
        os.println("----------------------");
        koehenkilo.tulosta(os);
        List<Kysymys> kysymykset = kysely.annaKysymykset(koehenkilo);
        for (Kysymys kys: kysymykset)
            kys.tulosta(os);
        os.println("----------------------");
        
    }
    
    
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
     * Lisaa uuden kysymyksen koehenkilolle
     */ 
    public void lisaaUusiKysymys() { 
        if ( koehenkiloKohdalla == null ) return;
        Kysymys kys = new Kysymys();
        kys.rekisteroi();
        kys.taytaEsimKysymysTiedot(koehenkiloKohdalla.getKoehenkiloNro());
        kysely.lisaa(kys);
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