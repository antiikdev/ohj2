/**
 * 
 */
package kysely;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
// import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fi.jyu.mit.ohj2.Mjonot;
import fi.jyu.mit.ohj2.WildChars;

import static kysely.Kanta.alustaKanta;

/**
 * Koehenkilot-luokka
 * @author Antiikdev
 * @author Doomslizer
 *
 */
public class Koehenkilot implements Iterable<Koehenkilo> {
    
    // TIETOKANTA-vaiheen Comtestit, jossa tuhotaan mahd tietok.
    // ja luodaan uusi Koehenkilot-luokka
    /*
     * Alustuksia ja puhdistuksia testi� varten
     * @example
     * <pre name="testJAVA">
     * #import java.io.*;
     * #import java.util.*;
     * 
     * private Koehenkilot koehenkilot;
     * private String tiedNimi;
     * private File ftied;
     * 
     * @Before
     * public void alusta() throws TallennaException { 
     *    tiedNimi = "testikysely";
     *    ftied = new File(tiedNimi+".db");
     *    ftied.delete();
     *    koehenkilot = new Koehenkilot(tiedNimi);
     * }   
     *
     * @After
     * public void siivoa() {
     *    ftied.delete();
     * }   
     * </pre>
     */ 
    
    
    private static final int MAX_KOEHENKILOITA = 8;
    private int lkm = 0;
    private Koehenkilo[] alkiot;
    private String tiedostonPerusNimi = "";
    
    
// ------------------------------------------------------------------
// --------------- Java ja tietokannat - vaihe 6 --------------------
// ------------------------------------------------------------------
    
    private Kanta kanta;
    private static Koehenkilo apukoehenkilo = new Koehenkilo();

    /**
     * Tarkistetaan etta kannassa koehenkiloiden tarvitsema taulu
     * @param nimi tietokannan nimi
     * @throws TallennaException jos jokin menee pieleen
     */
    public Koehenkilot(String nimi) throws TallennaException {
        kanta = alustaKanta(nimi);
        try ( Connection con = kanta.annaKantayhteys() ) {
            // Hankitaan tietokannan metadata ja tarkistetaan siita onko
            // Koehenkilot nimista taulua olemassa.
            // Jos ei ole, luodaan se. Ei puututa tassa siihen, onko
            // mahdollisesti olemassa olevalla taululla oikea rakenne,
            // kayttaja saa kuulla siita virheilmoituksen kautta
            DatabaseMetaData meta = con.getMetaData();
            
            try ( ResultSet taulu = meta.getTables(null, null, "Koehenkilot", null) ) {
                if ( !taulu.next() ) {
                    // Luodaan Koehenkilot taulu
                    try ( PreparedStatement sql = con.prepareStatement(apukoehenkilo.annaLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
            
        } catch ( SQLException e ) {
            throw new TallennaException("Ongelmia tietokannan kanssa:" + e.getMessage());
        }
    }
    
    
    /**
     * Lisaa uuden koehenkilon tietorakenteeseen.  Ottaa koehenkilon omistukseensa.
     * @param koehenkilo lisattavan koehenkilon viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws TallennaException jos tietorakenne on jo taynna
     * @example
     * <pre name="test">
     * #THROWS TallennaException 
     * 
     * Collection<Koehenkilo> loytyneet = koehenkilot.etsi("", 1);
     * loytyneet.size() === 0;
     * 
     * Koehenkilo aku1 = new Koehenkilo(), aku2 = new Koehenkilo();
     * koehenkilot.lisaa(aku1); 
     * koehenkilot.lisaa(aku2);
     *  
     * loytyneet = koehenkilot.etsi("", 1);
     * loytyneet.size() === 2;
     * 
     * // Unique constraint ei hyvaksy
     * koehenkilot.lisaa(aku1); #THROWS TallennaException
     * Koehenkilo aku3 = new Koehenkilo(); Koehenkilo aku4 = new Koehenkilo();
     * Koehenkilo aku5 = new Koehenkilo();
     * koehenkilot.lisaa(aku3); 
     * koehenkilot.lisaa(aku4); 
     * koehenkilot.lisaa(aku5); 

     * loytyneet = koehenkilot.etsi("", 1);
     * loytyneet.size() === 5;
     * Iterator<Koehenkilo> i = loytyneet.iterator();
     * i.next() === aku1;
     * i.next() === aku2;
     * i.next() === aku3;
     * </pre>
     */
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        try ( Connection con = kanta.annaKantayhteys(); PreparedStatement sql = koehenkilo.annaLisayslauseke(con) ) {
            sql.executeUpdate();
            try ( ResultSet rs = sql.getGeneratedKeys() ) {
                koehenkilo.tarkistaId(rs);
            }   
            
        } catch (SQLException e) {
            throw new TallennaException("Ongelmia tietokannan kanssa:" + e.getMessage());
        }
    }
    
    
    /**
     * Palauttaa koehenkilot listassa
     * @param hakuehto hakuehto  
     * @param k etsittavan kentan indeksi 
     * @return koehenkilot listassa
     * @throws TallennaException jos tietokannan kanssa ongelmia
     * @example
     * <pre name="test">
     * #THROWS TallennaException
     * Koehenkilo aku1 = new Koehenkilo(); aku1.taytaEsimTiedot(); 
     * Koehenkilo aku2 = new Koehenkilo(); aku2.taytaEsimTiedot(); 
     * koehenkilot.lisaa(aku1);
     * koehenkilot.lisaa(aku2);
     * koehenkilot.lisaa(aku2);  #THROWS TallennaException  // ei saa lisata sama id:ta uudelleen
     * Collection<Koehenkilo> loytyneet = koehenkilot.etsi(aku1.getNimi(), 1);
     * loytyneet.size() === 1;
     * loytyneet.iterator().next() === aku1;
     * loytyneet = koehenkilot.etsi(aku2.getNimi(), 1);
     * loytyneet.size() === 1;
     * loytyneet.iterator().next() === aku2;
     * loytyneet = koehenkilot.etsi("", 15); #THROWS TallennaException
     *
     * ftied.delete();
     * </pre>
     */
    public Collection<Koehenkilo> etsi(String hakuehto, int k) throws TallennaException {
        String ehto = hakuehto;
        String kysymys = apukoehenkilo.anna(k);
        if ( k < 0 ) { kysymys = apukoehenkilo.anna(0); ehto = ""; }
        // Avataan yhteys tietokantaan try .. with lohkossa.
        try ( Connection con = kanta.annaKantayhteys();
              PreparedStatement sql = con.prepareStatement("SELECT * FROM Koehenkilot WHERE " + kysymys + " LIKE ?") ) {
            ArrayList<Koehenkilo> loytyneet = new ArrayList<Koehenkilo>();
            
            sql.setString(1, "%" + ehto + "%");
            try ( ResultSet tulokset = sql.executeQuery() ) {
                while ( tulokset.next() ) {
                    Koehenkilo j = new Koehenkilo();
                    j.parse(tulokset);
                    loytyneet.add(j);
                }
            }
            return loytyneet;
        } catch ( SQLException e ) {
            throw new TallennaException("Ongelmia tietokannan kanssa:" + e.getMessage());
        }
    }

// ------------------------------------------------------------------
// ------------------------------------------------------------------

    
    /**
     * HUOM! ALKUPERAINEN Koehenkilot muodostoja
     * Luodaan alustava taulukko
     */ 
    public Koehenkilot() {
        alkiot = new Koehenkilo[MAX_KOEHENKILOITA];
    }
    
    
    /**
     * HUOM! ALKUPERAINEN lisaa-luokka ENNEN TIETOKANTAA
     * Lisataan uusi koehenkilo
     * @param koehenkilo uusi
     * @throws TallennaException jos tietorakenne on jo taynna
     * @example
     * <pre name="test">
     *  #THROWS TallennaException
     *  Koehenkilot koeapinat = new Koehenkilot();
     *  Koehenkilo cheeta = new Koehenkilo(), apina = new Koehenkilo();
     *  koeapinat.getLkm() === 0;
     *  koeapinat.lisaa(cheeta); koeapinat.getLkm() === 1;
     *  koeapinat.lisaa(apina); koeapinat.getLkm() === 2;
     *  koeapinat.anna(0) === cheeta;
     *  koeapinat.anna(1) === apina;
     * </pre>
     */
    /*
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        if ( lkm > alkiot.length ) throw new TallennaException("Liikaa alkioita");
        // HT6: Kasvatetaan taulukko (+10)
        if ( lkm >= alkiot.length) {
            Koehenkilo[] uusi = new Koehenkilo[lkm + 10]; 
            for (int i = 0; i < lkm; i++) {
                uusi[i] = this.alkiot[i];
            }
            this.alkiot = uusi;
        }
        this.alkiot[this.lkm] = koehenkilo;
        lkm++;
    }
    */
    
    
    /**
     * Palauttaa viitteen i:teen alkioon
     * @param i alkio
     * @return alkion viite
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Koehenkilo anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lkm <= i)
            throw new IndexOutOfBoundsException("Laitoin indeksi: " + i);
        return alkiot[i];
    }
    
     
    /**
     * Palauttaa kyselyn koehenkiloiden lukumaaran
     * @return koehenkiloiden lukumaara
     */
    public int getLkm() {
        return lkm;
    }
    
// ---------------------------------------------------------------
// ---------- HT7-vaihe tietojen muokkaus ja poisto  -------------
// ---------------------------------------------------------------
     
     /**
      * Poistaa koehenkilon
      * @param koehenkilo joka poistetaan
      * @throws TallennaException jos virhe
      */
     public void poistaKoehenkilo(Koehenkilo koehenkilo) throws TallennaException {
    	 if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "koehenkilot.dat";
    	 File otied = new File(tiedostonPerusNimi);
         File itied = new File("TempKoehenkilot.dat"); // uusi tiedosto
    	 String rivi = koehenkilo.toString();
    	 
    	 try (Scanner fi = new Scanner(new FileInputStream(otied))) {
    		 try (PrintStream fo = new PrintStream(new FileOutputStream(itied, false))) {
             
			 // Luetaan tiedosta rivi (s) kerrallaan koehenkilot
    			 while ( fi.hasNext() ) {
    				 String s = "";
    				 s = fi.nextLine();
             	
             // Jos poistettava rivi (s) sisaltaa poistettavan, hypataan yli, eika kirjoiteta 
	                 if ( s.equals(rivi) ) continue;
	                 fo.println(s);      
    			 }	
             
         } catch ( FileNotFoundException e) {
             throw new TallennaException("Ei  saa luettua tiedostoa: " + tiedostonPerusNimi);
         }
    	 } catch (FileNotFoundException ex) {
             throw new TallennaException("Tiedosto " + itied.getName() + " ei aukea");
         }
         // Poistetaan alkuperainen tiedosto ja nimetaan uusi tiedosto korvaamaan alkuperainen
    	 otied.delete();
         itied.renameTo(otied);
         // lkm?
     }

     
// ---------------------------------------------------------------
// ---------------------------------------------------------------    

     
// ---------------------------------------------------------------
// ---------- HT6-vaihe (tiedoston tallennus ja luku -------------
// ---------------------------------------------------------------
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }

    
    /**
     * Lukee koehenkilot tiedostosta
     * @throws TallennaException jos lukeminen epaonnistuu
     * @example
     * <pre name="test">
     *  #THROWS TallennaException
     *  #import java.io.File;
     *  #import java.util.Iterator;
     *  
     *  Koehenkilot kaniinit = new Koehenkilot();
     *  Koehenkilo kani = new Koehenkilo(), pupu = new Koehenkilo();
     *  kani.taytaEsimTiedot();
     *  pupu.taytaEsimTiedot();
     *  kaniinit.lueTiedostosta();
     *  kaniinit.lisaa(kani);
     *  kaniinit.lisaa(pupu);
     *  kaniinit.tallenna();
     *  kaniinit = new Koehenkilot(); // poistetaan aikaisemmat
     *  kaniinit.lueTiedostosta();
     *  Iterator<Koehenkilo> iteroi = kaniinit.iterator();
     *  iteroi.next() === kani;
     *  iteroi.next() === pupu;
     * </pre>
     */
    public void lueTiedostosta() throws TallennaException {
        // throw new TallennaException("Ei osata viela lukea tiedostoa " + hakemisto);
        // String nimi = "tutkimus/koehenkilot.dat";
        if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "koehenkilot.dat";
        
        try (Scanner fi = new Scanner(new FileInputStream(new File(tiedostonPerusNimi)))) {
            while ( fi.hasNext() ) {
                String s = "";
                s = fi.nextLine();
                Koehenkilo koehenkilo = new Koehenkilo();
                koehenkilo.parse(s);
                lisaa(koehenkilo);
            }
        } catch ( FileNotFoundException e) {
            throw new TallennaException("Ei  saa luettua tiedostoa: " + tiedostonPerusNimi);
        }
    }
    
    
    /**
     * TALLENNETAAN koehenkilot tiedostoon
     * Tiedostomuoto:
     * <pre>
     * id|nimi|sukupuoli|ikaryhma|
     * 1|k100|  m       |15-21|
     * 2|k101|  f       |21-28|
     * </pre>
     * @throws TallennaException poikkeus jos tallennus epaonnistuu
     */
    public void tallenna() throws TallennaException {
        if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "koehenkilot.dat";
        File ftied = new File(tiedostonPerusNimi);
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (int i = 0; i < getLkm(); i++) {
                Koehenkilo koehenkilo = anna(i);
                fo.println(koehenkilo.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new TallennaException("Tiedosto " + ftied.getName() + " ei aukea");
        }
    }
// ---------------------------------------------------------------
// ---------------------------------------------------------------    
    
    //Haku
    /**
     * Luokka koehenkiloiden iteroimiseen
     * @author TopiK
     * @version 12.12.2021
     * @example
     * <pre name="test">
     * #THROWS TallennaException
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Koehenkilot koehenkilot = new Koehenkilot();
     * Koehenkilo kissa1 = new Koehenkilo(), kissa2 = new Koehenkilo();
     * kissa1.rekisteroi(); kissa2.rekisteroi();
     * 
     * koehenkilot.lisaa(kissa1);
     * koehenkilot.lisaa(kissa2);
     * koehenkilot.lisaa(kissa1);
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Koehenkilo koehenkilo:koehenkilot)
     *      ids.append(" "+koehenkilo.getKoehenkiloNro());
     *      
     * String tulos = " " + kissa1.getKoehenkiloNro() + " " + kissa2.getKoehenkiloNro() + " " + kissa1.getKoehenkiloNro();
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Koehenkilo> i=koehenkilot.iterator();
     * i.next() == kissa1 === true;
     * i.next() == kissa2 === true;
     * i.next() == kissa1 === true;
     * 
     * i.next(); #THROWS NoSuchElementException
     * </pre>
     */
    public class KoehenkilotIterator implements Iterator<Koehenkilo> {
     private int kohdalla = 0;
     
     /**
      * Onko olemassa seuraavaa
      * @see java.util.Iterator#hasNext()
      * @return true jos on seuraava
      */
     @Override
     public boolean hasNext() {
         return kohdalla < getLkm();
     }
     
     /**
      * Annetaan seuraava
      * @return seuraava
      * @throws NoSuchElementException jos ei ole seuraavaa
      * @see java.util.Iterator#next()
      */
     @Override
     public Koehenkilo next() throws NoSuchElementException {
         if (!hasNext()) throw new NoSuchElementException("Ei oo enempää");
         return anna(kohdalla++);
     }
     
     /**
      * Tuhoamista ei toteutettu
      * @throws UnsupportedOperationException aina
      * @see  java.util.Iterator#remove()
      */
     @Override
     public void remove() throws UnsupportedOperationException {
         throw new UnsupportedOperationException("Ei poistella");
     }
    }
    
    /**
     * Palutetaan iteraattori koehenkilöistään
     * @return koehenkilö iteraattori
     */
    @Override
    public Iterator<Koehenkilo> iterator() {
        return new KoehenkilotIterator();
    }
    
    
    /**
     * HUOM! ALKUPERAINEN lisaa-luokka ENNEN TIETOKANTAA
     * Palauttaa hakuehtoon vastaavien koehenkilöiden viitteet
     * @param hakuehto mitä haetaan
     * @param k etsittävän indeksi
     * @return tietorakenteen löydetyistä
     * <pre name="test">
     * #THROWS TallennaException
     * Koehenkilot koehenkilot = new Koehenkilot();
     * Koehenkilo kissa1 = new Koehenkilo(); kissa1.parse("1|k100|m|11-15");
     * Koehenkilo kissa2 = new Koehenkilo(); kissa2.parse("2|k101|f|15-21");
     * Koehenkilo kissa3 = new Koehenkilo(); kissa3.parse("3|k102|m|5-11");
     * Koehenkilo kissa4 = new Koehenkilo(); kissa4.parse("4|k103|m|15-21");
     * Koehenkilo kissa5 = new Koehenkilo(); kissa5.parse("5|k104|f|5-11");
     * koehenkilot.lisaa(kissa1); koehenkilot.lisaa(kissa2); koehenkilot.lisaa(kissa3); koehenkilot.lisaa(kissa4); koehenkilot.lisaa(kissa5);
     * List<Koehenkilo> loytyneet;
     * loytyneet = (List<Koehenkilo>)koehenkilot.etsi("f",2);
     * loytyneet.size() === 2;
     * loytyneet.get(0) == kissa2 === true;
     * loytyneet.get(1) == kissa5 === true;
     * 
     * loytyneet = (List<Koehenkilo>)koehenkilot.etsi("*4*",1);
     * loytyneet.size() === 1;
     * loytyneet.get(0) == kissa5 === true;
     * 
     * loytyneet = (List<Koehenkilo>)koehenkilot.etsi("*11*",3);
     * loytyneet.size() === 3;
     * loytyneet.get(0) == kissa1 === true;
     * loytyneet.get(1) == kissa3 === true;
     * loytyneet.get(2) == kissa5 === true;
     * 
     * loytyneet = (List<Koehenkilo>)koehenkilot.etsi(null,-1);
     * loytyneet.size() === 5;
     * </pre>
     */
    /*
    public Collection<Koehenkilo> etsi(String hakuehto, int k) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        int hk = k;
        if (hk < 0) hk = 1;
        Collection<Koehenkilo> loytyneet = new ArrayList<Koehenkilo>();
        for (Koehenkilo koehenkilo : this) {
            if (WildChars.onkoSamat(koehenkilo.anna(hk), ehto)) loytyneet.add(koehenkilo);
        }
        
        
        return loytyneet;
    }
    */
    
    
    /**
     * Paaohjelma luokan testaamiseen
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        try {
            new File("kokeilu.db").delete();
            Koehenkilot koehenkilot = new Koehenkilot("kokeilu");
    
            Koehenkilo aku = new Koehenkilo(), aku2 = new Koehenkilo();
            aku.taytaEsimTiedot();
            //aku2.rekisteroi();
            aku2.taytaEsimTiedot();
            
            koehenkilot.lisaa(aku);
            koehenkilot.lisaa(aku2);
            aku2.tulosta(System.out);
            
            System.out.println("=========== Koehenkilot testi =============");

            // Ongelma ratkaistu:
            // .etsi (koehenkilo.anna) antoi switch defaultin, koska
            // puuttui case 0 tietokannan attribuutti koehenkiloNro
            // (malli esim.: jasen.getKysymys)
            int i = 0;
            for (Koehenkilo koehenkilo:koehenkilot.etsi("", -1)) {
                System.out.println("Koehenkilo nro: " + i++);
                koehenkilo.tulosta(System.out);
            }
            
            new File("kokeilu.db").delete();
        } catch ( TallennaException ex ) {
            System.out.println(ex.getMessage());
        }
        
        /*
         * HUOM! ALKUPERAINEN testipaaohjelma ennen TIETOKANTAA
         * 
        Koehenkilot koehenkilot = new Koehenkilot();
        
        try {
            koehenkilot.lueTiedostosta();
        } catch (TallennaException ex) {
            System.err.println(ex);
        }
        
        Koehenkilo p01 = new Koehenkilo();
        Koehenkilo p02 = new Koehenkilo();
        p01.rekisteroi();
        p01.taytaEsimTiedot();
        p02.rekisteroi();
        p02.taytaEsimTiedot();
        
        try {
            koehenkilot.lisaa(p01);
            koehenkilot.lisaa(p02);
            
            // System.out.println("--- Koehenkilot testi ---");
            
            for (int i = 0; i < koehenkilot.getLkm(); i++) {
                Koehenkilo koehenkilo = koehenkilot.anna(i);
                // System.out.println("Koehenkilo indeksi: " + i);
                koehenkilo.tulosta(System.out);
            }
            
        } catch (TallennaException e) {
            System.err.println(e.getMessage());
        }
        
        try {
            koehenkilot.tallenna();
        } catch (TallennaException e) {
            e.printStackTrace();
        }
        */

    }

}
