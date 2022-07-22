/**
 * 
 */
package kysely;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static kysely.Kanta.alustaKanta;

/**
 * Kysymykset-luokka
 * @author Antiikdev
 * @author Doomslizer
 *
 */
public class Kysymykset {
    
    /*
     * Alustuksia ja puhdistuksia testia varten
     * @example
     * <pre name="testJAVA">
     * #import java.io.*;
     * #import java.util.*;
     * 
     * private Kysymykset kyssarit;
     * private String tiedNimi;
     * private File ftied;
     * 
     * @Before
     * public void alusta() throws TallennaException { 
     *    tiedNimi = "testikysely";
     *    ftied = new File(tiedNimi+".db");
     *    ftied.delete();
     *    kyssarit = new Kysymykset(tiedNimi);
     * }   
     *
     * @After
     * public void siivoa() {
     *    ftied.delete();
     * }   
     * </pre>
     */ 
    
    
    private String tiedostonPerusNimi = "";
    // private boolean muutettu = false;
    
    // Tietorakenteiden perintahierrarkiasta ylimmaisen Collection > Iterable
    private Collection<Kysymys> alkiot = new ArrayList<Kysymys>();
    
    
// ------------------------------------------------------------------
// --------------- Java ja tietokannat - vaihe 6 --------------------
// ------------------------------------------------------------------
 
    private static Kysymys apukysymys = new Kysymys();
    private Kanta kanta;
    
    /**
     * Tarkistetaan etta kannassa kysymysten tarvitsema taulu
     * @param nimi tietokannan nimi
     * @throws TallennaException jos jokin menee pieleen
     */
    public Kysymykset(String nimi) throws TallennaException {
        kanta = alustaKanta(nimi);
        try ( Connection con = kanta.annaKantayhteys() ) {
            DatabaseMetaData meta = con.getMetaData();
            try ( ResultSet taulu = meta.getTables(null, null, "Kysymykset", null) ) {
                if ( !taulu.next() ) {
                    // Luodaan kysymykset taulu
                    try ( PreparedStatement sql = con.prepareStatement(apukysymys.annaLuontilauseke()) ) {
                        sql.execute();
                    }
                }
            }
                
        } catch ( SQLException e ) {
            throw new TallennaException("Ongelmia tietokannan kanssa:" + e.getMessage());
        }
    }
    
    
    /**
     * Listaan uusi kysymys kyselyyn
     * @param kys lisattava kysymys 
     * @throws TallennaException jos tietokantayhteyden kanssa ongelmia
     * @example
     * <pre name="test">
     * #THROWS TallennaException
     * </pre>
     */
    public void lisaa(Kysymys kys) throws TallennaException {
        try ( Connection con = kanta.annaKantayhteys(); PreparedStatement sql = kys.annaLisayslauseke(con) ) {
            sql.executeUpdate();
            try ( ResultSet rs = sql.getGeneratedKeys() ) {
                kys.tarkistaId(rs);
             }   
        } catch (SQLException e) {
            throw new TallennaException("Ongelmia tietokannan kanssa:" + e.getMessage());
        }
    }
    
    
    /**
     * Haetaan kaikki koehenkilo kysymykset
     * @param tunnusnro koehenkilon tunnusnumero jolle kysymksia haetaan
     * @return tietorakenne jossa viiteet loydetteyihin kysymyksiin
     * @throws TallennaException jos kysymysten hakeminen tietokannasta epaonnistuu
     * @example
     * <pre name="test">
     * #THROWS TallennaException
     *  
     *  Kysymys pitsi21 = new Kysymys(2); pitsi21.taytaEsimKysymysTiedot(2); kyssarit.lisaa(pitsi21);
     *  Kysymys pitsi11 = new Kysymys(1); pitsi11.taytaEsimKysymysTiedot(1); kyssarit.lisaa(pitsi11);
     *  Kysymys pitsi22 = new Kysymys(2); pitsi22.taytaEsimKysymysTiedot(2); kyssarit.lisaa(pitsi22);
     *  Kysymys pitsi12 = new Kysymys(1); pitsi12.taytaEsimKysymysTiedot(1); kyssarit.lisaa(pitsi12);
     *  Kysymys pitsi23 = new Kysymys(2); pitsi23.taytaEsimKysymysTiedot(2); kyssarit.lisaa(pitsi23);
     *  Kysymys pitsi51 = new Kysymys(5); pitsi51.taytaEsimKysymysTiedot(5); kyssarit.lisaa(pitsi51);
     *  
     *  
     *  List<Kysymys> loytyneet;
     *  loytyneet = kyssarit.annaKysymykset(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kyssarit.annaKysymykset(1);
     *  loytyneet.size() === 2; 
     *  
     *  loytyneet.get(0) === pitsi11;
     *  loytyneet.get(1) === pitsi12;
     *  
     *  loytyneet = kyssarit.annaKysymykset(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) === pitsi51;
     * </pre> 
     */
    public List<Kysymys> annaKysymykset(int tunnusnro) throws TallennaException {
        List<Kysymys> loydetyt = new ArrayList<Kysymys>();
        
        try ( Connection con = kanta.annaKantayhteys();
              PreparedStatement sql = con.prepareStatement("SELECT * FROM Kysymykset WHERE koehenkiloNro = ?")
                ) {
            sql.setInt(1, tunnusnro);
            try ( ResultSet tulokset = sql.executeQuery() )  {
                while ( tulokset.next() ) {
                    Kysymys kys = new Kysymys();
                    kys.parse(tulokset);
                    loydetyt.add(kys);
                }
            }
            
        } catch (SQLException e) {
            throw new TallennaException("Ongelmia tietokannan kanssa:" + e.getMessage());
        }
        return loydetyt;
    }
    
// ------------------------------------------------------------------
// ------------------------------------------------------------------
    
    
    /**
     * Kysymykset alustaminen
     */
    public Kysymykset() {
        // ei tarvitse alustaa
    }
    
    
    /**
     * HUOM! TIETOKANTA-vaiheessa luotu uusi lisaa-metodi (tama on vanha)
     * Lisataan kysymys
     * @param kys lisattava kysymys
     * @throws TallennaException jos ongelmia
     */
    /*
    public void lisaa(Kysymys kys) throws TallennaException {
        // Tietorakenteesta loytyy valmiiksi .add-metodi lisaamiseen
        alkiot.add(kys);
        // muutettu = true;
    }
    */
    
    /**
     * HUOM! TIETOKANTA-vaiheessa tama metodi on korvattu uudella
     * Etsitaan koehenkilon kysymykset
     * @param koehenkiloN jolla haetaan
     * @return loydetyista lista
     * @example
     * <pre name="test">
     *  #THROWS TallennaException
     *  #import java.util.*;
     *  Kysymykset kyssarit = new Kysymykset();
     *  Kysymys kys1 = new Kysymys(1); kyssarit.lisaa(kys1);
     *  Kysymys kys2 = new Kysymys(2); kyssarit.lisaa(kys2);
     *  Kysymys kys3 = new Kysymys(1); kyssarit.lisaa(kys3);
     *  Kysymys kys4 = new Kysymys(2); kyssarit.lisaa(kys4);
     *  
     *  List<Kysymys> loydetyt;
     *  loydetyt = kyssarit.annaKysymykset(6);
     *  loydetyt.size() === 0;
     *  loydetyt = kyssarit.annaKysymykset(1);
     *  loydetyt.size() === 2;
     *  loydetyt.get(0) == kys1 === true;
     * </pre>
     */
    /*
    public List<Kysymys> annaKysymykset(int koehenkiloN) {
        ArrayList<Kysymys> loydetyt = new ArrayList<Kysymys>();
        for (Kysymys kys : alkiot)
            if ( kys.getKoehenkiloNro() == koehenkiloN ) loydetyt.add(kys);
        return loydetyt;
    }
    */
    
// ---------------------------------------------------------------
// ---------- HT7-vaihe (kysymysten nayttaminen ja muokkaus ------
// ---------------------------------------------------------------�
    
    /**
     * Korvaa tai lisaa kysymyksen
     * @param kysymys korvaava tai lisattava kysymys
     * @throws TallennaException jos virheita
     */
    public void korvaaTaiLisaa(Kysymys kysymys) throws TallennaException {
        int id = kysymys.getId();
        for (int i = 0; i < getLkm(); i++) {
            if (((ArrayList<Kysymys>) alkiot).get(i).getId() == id) {
                ((ArrayList<Kysymys>) alkiot).set(i, kysymys);
                // muutettu = true;
                return;
            }
        }
        lisaa(kysymys);
    }
    

    /**
     * Kyselyn kysymysten maara
     * @return kysymysten maaran
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Iteraattori kysymyksille
     * @return kysymys
     */
    public Iterator<Kysymys> iterator() {
        return alkiot.iterator();
    }
    
    
    /**
     * Poistaa koehenkilon valitun kysymykset
     * @param kysymys joka poistetaan
     */
    public void poista(Kysymys kysymys) {
    	alkiot.remove(kysymys);
    	// if (ret) muutettu = true;
    	// return retu;
    }
    
    
    /**
     * Poistaa tietyn koehenkilon kaikki kysymykset ja vastaukset
     * @param tunnusNro viite koehenkilon jonka kys-vas poistetaan
     */
    public void poistaKoehenkilonKysVas(int tunnusNro) {
    	for (Iterator<Kysymys> iter = alkiot.iterator(); iter.hasNext();) {
    		Kysymys kys = iter.next();
    		if (kys.getKoehenkiloNro() == tunnusNro) {
    			iter.remove();
    		}
    	}
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
     * Lukee koehenkilot tiedostosta
     * @throws TallennaException jos lukeminen epaonnistuu
     * @example
     * <pre name="test">
     *  #THROWS TallennaException
     * 	#import java.io.File;
     * 	#import java.util.Iterator;
     * 	Kysymykset kyssarit = new Kysymykset();
     * 	Kysymys kys1 = new Kysymys(); kys1.taytaEsimKysymysTiedot(1);
     * 	Kysymys kys2 = new Kysymys(); kys2.taytaEsimKysymysTiedot(2);
     * 	Kysymys kys3 = new Kysymys(); kys3.taytaEsimKysymysTiedot(3);
     * 	kyssarit.lueTiedostosta();
     * 	kyssarit.lisaa(kys1);
     * 	kyssarit.lisaa(kys2);
     * 	kyssarit.lisaa(kys3);
     * 	kyssarit.tallenna();
     * 	kyssarit = new Kysymykset();
     * 	kyssarit.lueTiedostosta();
     * 	Iterator<Kysymys> i = kyssarit.iterator();
     * 	i.next().toString() === kys1.toString();
     * 	i.next().toString() === kys2.toString();
     * 	i.next().toString() === kys3.toString();
     * </pre>
     */
    public void lueTiedostosta() throws TallennaException {
        // throw new TallennaException("Ei osata viela lukea tiedostoa " + hakemisto);
        // String nimi = "tutkimus/kysymykset.dat";
        if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "kysymykset.dat";
        try (Scanner fi = new Scanner(new FileInputStream(new File(tiedostonPerusNimi)))) {
            
            String rivi = "";
            while ( fi.hasNext() ) {
                rivi = rivi.trim();
                rivi = fi.nextLine();
                Kysymys kys = new Kysymys();
                kys.parse(rivi);
                lisaa(kys);
            }
            // muutettu = false;
            
        } catch ( FileNotFoundException e) {
            throw new TallennaException("Ei  saa luettua tiedostoa: " + tiedostonPerusNimi);
        }
    }
    
    
    /**
     * TALLENNETAAN koehenkilot tiedostoon
     * Tiedostomuoto:
     * <pre>
     * id| koehenkiloNro| kysymys                   |kysymysTyyppi|vastausVaihtoehdot|
     * 1|k000|Mika seuraavista on kissa?|monivalinta|a) kissa, b) tiikeri|
     * 2|k001|Kuinka paljon pidat kissoista?|likert|0: en pida, 5: paljon|
     * </pre>
     * @throws TallennaException poikkeus jos tallennus epäonnistuu
     */
    public void tallenna() throws TallennaException {
        // if ( !muutettu ) return;
        
        if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "kysymykset.dat";
        File ftied = new File(tiedostonPerusNimi);
        // Vaihtoehto (HT6): jos ehtii backup-tiedostojen tekemisen (esim. harrastukset.java)
        
        // HT6 tallentaminen:
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (Kysymys kys : this.alkiot) {
                fo.println(kys.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new TallennaException("Tiedosto " + ftied.getName() + " ei aukea");
        }
        // muutettu = false;
    }
// ---------------------------------------------------------------
// ---------------------------------------------------------------        
    
    
    /**
     * Testi paaohjelma kysymykset-luokalle
     * @param args ei kaytossa
     * @throws TallennaException jos ongelmia
     */
    public static void main(String[] args) throws TallennaException {
        try {
            Kysymykset kyssarit = new Kysymykset("kokeilu");
            Kysymys pitsi1 = new Kysymys();
            pitsi1.taytaEsimKysymysTiedot(2);
            Kysymys pitsi2 = new Kysymys();
            pitsi2.taytaEsimKysymysTiedot(1);
            Kysymys pitsi3 = new Kysymys();
            pitsi3.taytaEsimKysymysTiedot(2);
            Kysymys pitsi4 = new Kysymys();
            pitsi4.taytaEsimKysymysTiedot(2);

            kyssarit.lisaa(pitsi1);
            kyssarit.lisaa(pitsi2);
            kyssarit.lisaa(pitsi3);
            kyssarit.lisaa(pitsi2);
            kyssarit.lisaa(pitsi4);
            
            System.out.println("========= Kysymykset testi ==========");
    
            List<Kysymys> kysymykset2;
            
            kysymykset2 = kyssarit.annaKysymykset(2);
            
            for (Kysymys kys : kysymykset2) {
                System.out.print(kys.getId() + " ");
                kys.tulosta(System.out);
            }
            
            new File("kokeilu.db").delete();
        } catch (TallennaException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        /*
         * HUOM! ALKUPERAINEN testipaaohjelma ennen TIETOKANTAA
         * 
        Kysymykset kysymykset = new Kysymykset();
        
        try {
            kysymykset.lueTiedostosta();
        } catch (TallennaException ex) {
            System.err.println(ex);
        }
        
        Kysymys kyssari1 = new Kysymys();
        kyssari1.taytaEsimKysymysTiedot(1);
        Kysymys kyssari2 = new Kysymys();
        kyssari2.taytaEsimKysymysTiedot(2);
        Kysymys kyssari3 = new Kysymys();
        kyssari3.taytaEsimKysymysTiedot(2);
        Kysymys kyssari4 = new Kysymys();
        kyssari4.taytaEsimKysymysTiedot(2);
        
        kysymykset.lisaa(kyssari1);
        kysymykset.lisaa(kyssari2);
        kysymykset.lisaa(kyssari3);
        kysymykset.lisaa(kyssari4);

        // System.out.println("----- Kysymykset testausta -----");
        List<Kysymys> kysymykset2 = kysymykset.annaKysymykset(2);

        try {
            for (Kysymys kys : kysymykset2) {
                // System.out.print(kys.getKoehenkiloNro() + " ");
                kys.tulosta(System.out);
            }
        } catch (TallennaException e) {
            e.printStackTrace();
        }
        
        try {
            kysymykset.tallenna();
        } catch (TallennaException e) {
            e.printStackTrace();
        }
       */
    }
    
}
