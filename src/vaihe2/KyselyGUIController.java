package vaihe2;

// import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringAndObject;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import kysely.Koehenkilo;
import kysely.Kysely;
import kysely.Kysymykset;
import kysely.Kysymys;
import kysely.TallennaException;
import kysely.Vastaus;

/**
 * Luokka kyselyn kayttoliittyman tapahtumien kontrolloimiseksi
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 *
 */
public class KyselyGUIController implements Initializable {
    
    @FXML private ListChooser<Koehenkilo> chooserKoehenkilot; 
    @FXML private ScrollPane panelKoehenkilo;
    
    // HT7-vaihe:
    @FXML private TextField editNimi;
    @FXML private TextField editSukupuoli;
    @FXML private TextField editIkaryhma;
    @FXML private StringGrid<Kysymys> tableKysymykset;
    @FXML private StringGrid<Vastaus> tableVastaukset;
    //private ComboBoxChooser<Koehenkilo> cbKentat;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private TextField hakuehto;
    
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
    }
    
    @FXML void handleLopeta() {
        // HT6: Tallennetaan ja lopetetaan
        tallenna();
        Platform.exit();
    }
    
    // ---------- MENUBAR ITEMIT: Muokkaa ----------
    @FXML void handleLisaaUusi() {
        lisaaUusiKoehenkiloKyselyyn();
    }
    
    @FXML void handlePoistaTama() throws TallennaException {
    	poistaKoehenkilo();
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
    
    @FXML void handleHakuehto() {
     //Dialogs.showMessageDialog("Haku ei vielä toimi!");
     hae(0);
    }
// ----------------------------------------------------------------------------    
    

// =======================================================================
//  Tasta eteenpain kayttoliittymaan EI suoraan liittyvaa koodia
//=======================================================================

// ----------------------------------------------------------------------------
// HT7: Koehenkiloiden (+kys/vas) tiedot ja muokkaus paaikkunassa
//       - sisaltaa myos muokattuja metodeja aikaisemmista tyovaiheista. 
// ----------------------------------------------------------------------------    
    
    private TextField edits[];
    private static Kysymys apukysymys = new Kysymys();
    // private static Vastaus apuvastaus = new Vastaus(); // HT7: ei kaytossa
    private static Koehenkilo apukoehenkilo = new Koehenkilo();
    
    /**
     * Muut alustukset ja Gridpanelin tilalle tekstikentta,
     * johon koehenkilon tiedot. Myos koehenkilolistan kuuntelijan alustus
     */
    protected void alusta() {
	// Poistettu HT7:ssa (HT6 origin):
        // 	panelKoehenkilo.setContent(areaKoehenkilo); 
        // 	areaKoehenkilo.setFont(new Font("Courier New", 12));
    	
	// Clear ja kuuntelu nayttaa koehenkilo
        chooserKoehenkilot.clear(); 
        chooserKoehenkilot.addSelectionListener(e -> naytaKoehenkilo());
        
    // Koehenkilon tietojen muutokset GUI:ssa:
        edits = new TextField[] {editNimi, editSukupuoli, editIkaryhma};
        int i = 0;
        for (TextField edit : edits) {
        	final int k = ++i;
        	edit.setOnKeyReleased(e -> kasitteleMuutosKoehenkiloon(k, (TextField)(e.getSource())));
    	panelKoehenkilo.setFitToHeight(true);
        }
        
	// ----- Alustetaan KYSYMYSTAULUKON otsikot omaan grid tableen -----
        int eka = apukysymys.ekaKentta();
        int lkm = apukysymys.getKenttia();
        String[] headings = new String[lkm-eka];
        for (int j=0, k=eka; k<lkm; j++, k++) headings[j] = apukysymys.getKysymys(k);       
        tableKysymykset.initTable(headings);
        tableKysymykset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableKysymykset.setEditable(false); 
        tableKysymykset.setPlaceholder(new Label("Ei viela kysymysta"));
        
        // KYSYMYSTEN muokkaus
        tableKysymykset.setTableMenuButtonVisible(true);
        tableKysymykset.setEditable(true);
        tableKysymykset.setOnGridLiveEdit((g, koehenkilo, defValue, r, c, edit) -> {
            String virhe = koehenkilo.aseta(c+eka,defValue);
            if ( virhe == null ) {
                try {
					kysely.korvaaTaiLisaa(koehenkilo);
				} catch (TallennaException ex) {
					// virhe
					ex.printStackTrace();
				}
                edit.setStyle(null);
                Dialogs.setToolTipText(edit,"");
            } else {
            	edit.setStyle("-fx-background-color: red");
                Dialogs.setToolTipText(edit,virhe);
            }
            return defValue;
        });
    }
    
    
    /**
     * Kasittelee muutokset koehenkilon tietoihin ja
     * kasittelee virheet
     * @param k kenttanumero
     * @param edit editointi
     */
    private void kasitteleMuutosKoehenkiloon(int k, TextField edit) {
		if ( koehenkiloKohdalla == null ) return;
		String s = edit.getText();
		String virhe = null;
        switch (k) {
	        case 1 : virhe = koehenkiloKohdalla.setNimi(s); break;
	        case 2 : virhe = koehenkiloKohdalla.setSukupuoli(s); break;
	        case 3 : virhe = koehenkiloKohdalla.setIkaryhma(s); break;
        default:
        }
        if (virhe != null) ilmoitaVirhe(virhe); 
	}
    
    
    /**
     * Ilmoittaa virheen kayttajalle
     * @param virhe teksti joka ilmoitetaan
     */
    private void ilmoitaVirhe(String virhe) {
    	if ( virhe == null || virhe.isEmpty() ) return;
    	else Dialogs.showMessageDialog(virhe);
    }


	/**
     * Nayttaa koehenkiloiden kysymykset
     * @param koehenkilo joka naytetaan
     */
    private void naytaKysymykset(Koehenkilo koehenkilo) {
    	tableKysymykset.clear();
    	if (koehenkilo == null) return;
    	 
    	List<Kysymys> kysymykset = kysely.annaKysymykset(koehenkilo);
		if ( kysymykset.size() == 0 ) return;
		for ( Kysymys kys : kysymykset )
			naytaKysymys(kys);
    }
    
    
    /**
     * Naytetaan Kysymys per koehenkilo
     * @param kysymys joka naytetaan
     */
    private void naytaKysymys(Kysymys kys) {
    	int kenttia = kys.getKenttia();
        String[] rivi = new String[kenttia-kys.ekaKentta()];
        for (int i=0, k=kys.ekaKentta(); k<kenttia; i++, k++)
        	rivi[i] = kys.anna(k);
        tableKysymykset.add(kys, rivi);
    }

    
    /*
     * Naytetaan koehenkilo
     * HT7 MUOKATTU
     */
    private void naytaKoehenkilo() {
        koehenkiloKohdalla = chooserKoehenkilot.getSelectedObject();
        if (koehenkiloKohdalla == null) return;
        /*// HT6, H7 poistettu
        areaKoehenkilo.setText("");
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaKoehenkilo)) {
                tulosta(os, koehenkiloKohdalla);
        }
        */
        // HT7: koehenkilon nayttaminen kohdalla:
        editNimi.setText(koehenkiloKohdalla.getNimi());
        editSukupuoli.setText(koehenkiloKohdalla.getSukupuoli());
        editIkaryhma.setText(koehenkiloKohdalla.getIkaryhma());
        naytaKysymykset(koehenkiloKohdalla);
        // TODO (Ilkka): naytaVastaukset(); vaatisi GUI muokkausta, mutta
        // SceneBuilder ei toimi ja fxml-tied muokkaus ei ole onnistunut
    }
    
    
    /*
     * Lisaa kyselyyn uuden koehenkilon
     */
    private void lisaaUusiKoehenkiloKyselyyn() {
        Koehenkilo uusi = new Koehenkilo();
        uusi.rekisteroi();
        uusi.taytaEsimTiedot();
        try {
            kysely.lisaa(uusi);
        } catch (TallennaException e) {
            Dialogs.showMessageDialog("Ongelmia uuden Koehenkilon luomisessa.");
            return;
        }
        hae(uusi.getKoehenkiloNro());
    }
    
    
    /**
     * Poistaa koehenkilon kyselysta
     * @throws TallennaException jos virhe
     */
    private void poistaKoehenkilo() throws TallennaException {
    	koehenkiloKohdalla = chooserKoehenkilot.getSelectedObject();
        if (koehenkiloKohdalla == null) return;
        // Koehenkilon rivin poistaminen tiedostosta:
    	kysely.poistaKoehenkilo(koehenkiloKohdalla);
    	// Lue tiedosto uudestaan
    	lueTiedosto(kyselynimi);
    }
    
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
    
    
// ----------------------------------------------------------------------------
// HT6: Koehenkiloiden, kysymysten ja vastausten lisaaminen (tiedostoon luku ja kirjoittaminen)
//      - sisaltaa myos muokattuja metodeja aikaisemmista tyovaiheista. 
// ----------------------------------------------------------------------------
    
    // HT6: Esitelty alla "kyselynimi"-attribuutti
    private String kyselynimi = "";
    private Kysely kysely;
    private Koehenkilo koehenkiloKohdalla;
    
    // private TextArea areaKoehenkilo = new TextArea(); //HT6, poistettu HT7
   
   /**
    * Alustaa kyselyn lukemalla sen valitun nimisesta tiedostosta
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
     * Kysytaan tiedoston nimi ja luetaan se
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
    
    
    /*
     * Naytetaan koehenkilo ja kysymykset
     * TODO: supresswarning asetettu HT7, tarvetta?
     */
    @SuppressWarnings("unused")
	private void tulosta(PrintStream os, final Koehenkilo koehenkilo) {
        os.println("----------------------");
        koehenkilo.tulosta(os);
        os.println("----------------------");
        
        try {
            List<Kysymys> kysymykset = kysely.annaKysymykset(koehenkilo);
            for (Kysymys kys:kysymykset) 
                kys.tulosta(os);     
        } catch (TallennaException ex) {
            Dialogs.showMessageDialog("Kysymysten hakemisessa ongelmia! " + ex.getMessage());
        }
        try {
            List<Vastaus> vastaukset = kysely.annaVastaukset(koehenkilo);
            for (Vastaus vas:vastaukset) 
                vas.tulosta(os);     
        } catch (TallennaException ex) {
            Dialogs.showMessageDialog("Vastausten hakemisessa ongelmia! " + ex.getMessage());
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
    
    
    /** 
     * Lisaa uuden vastauksen koehenkilolle
     */ 
    public void lisaaUusiVastaus() { 
        if ( koehenkiloKohdalla == null ) return;
        Vastaus vas = new Vastaus();
        vas.rekisteroi();
        vas.taytaEsimVastausTiedot(koehenkiloKohdalla.getKoehenkiloNro());
        try {
            kysely.lisaa(vas);
        } catch (TallennaException e) {
            Dialogs.showMessageDialog("Ongelmia lisaamisessa! " + e.getMessage());
        }
        hae(koehenkiloKohdalla.getKoehenkiloNro());
    } 
    
// ----------------------------------------------------------------------------
// ----------------------------------------------------------------------------
    

// ----------------------------------------------------------------------------   
// HT5 tai sita aikaisempaa koodia  
// ----------------------------------------------------------------------------   

    /**
     * Haetaan koehenkiloiden tiedot listaan
     * @param knr koehenkilon numero, joka aktivoidaan haun jalkeen
     */
    private void hae (int knr) {
        chooserKoehenkilot.clear();
        int knro = knr;
        if (knro <= 0)
        {
            Koehenkilo kohdalla = koehenkiloKohdalla;
            if (kohdalla != null) knro = kohdalla.getKoehenkiloNro();
        }
        int index = 0;
        
        //haku
        int k = cbKentat.getSelectedIndex() + apukoehenkilo.ekaKentta();
        String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
        
        Collection<Koehenkilo> koehenkilot;
        try {
            koehenkilot = kysely.etsi(ehto, k);
            int i = 0;
            for (Koehenkilo koehenkilo:koehenkilot)
            {
                if (koehenkilo.getKoehenkiloNro() == knro) index = i;
                chooserKoehenkilot.add(koehenkilo.getNimi(), koehenkilo);
                i++;
            }
        } catch (TallennaException ex) {
            Dialogs.showMessageDialog("Koehenkilön hakemisessa ongelmia! " + ex.getMessage());
        }
        
        //for (int i = 0; i < kysely.getKoehenkiloita(); i++) {
            //Koehenkilo koehenkilo = kysely.annaKoehenkilo(i);
            //if (koehenkilo.getKoehenkiloNro() == knro) index = i;
            //chooserKoehenkilot.add(koehenkilo.getNimi(), koehenkilo);
        //}
        chooserKoehenkilot.setSelectedIndex(index); // muutosviesti
    }
    
    
    //Vanha
    /** 
     * Lisaa uuden vastauksen koehenkilolle
     */ 
    //public void lisaaUusiVastaus() { 
        //if ( koehenkiloKohdalla == null ) return;
        //Vastaus vas = new Vastaus();
        //vas.rekisteroi();
        //vas.taytaEsimVastausTiedot(koehenkiloKohdalla.getKoehenkiloNro());
        //kysely.lisaa(vas);
        //hae(koehenkiloKohdalla.getKoehenkiloNro());
    //} 
    
    
    /**
     * Asetetaan kaytettava kysely
     * @param kysely jota kaytetaan
     */
    public void setKysely(Kysely kysely) {
        this.kysely = kysely;
    }
    
        
}