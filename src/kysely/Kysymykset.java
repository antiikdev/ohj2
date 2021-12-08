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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Kysymykset-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 * @version 3 Nov 2021
 *
 */
public class Kysymykset {
    
    private String tiedostonPerusNimi = "";
    // private boolean muutettu = false;
    
    // Tietorakenteiden perintahierrarkiasta ylimmaisen Collection > Iterable
    private Collection<Kysymys> alkiot = new ArrayList<Kysymys>();
    
    
    /**
     * Kysymykset alustaminen
     */
    public Kysymykset() {
        // ei tarvitse alustaa
    }
    
    
    /**
     * Lisataan kysymys
     * @param kys lisattava kysymys
     */
    public void lisaa(Kysymys kys) {
        // Tietorakenteesta loytyy valmiiksi .add-metodi lisaamiseen
        alkiot.add(kys);
        // muutettu = true;
    }
    
    
    /**
     * Etsitaan koehenkilon kysymykset
     * @param koehenkiloN jolla haetaan
     * @return loydetyista lista
     * @example
     * <pre name="test">
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
    public List<Kysymys> annaKysymykset(int koehenkiloN) {
        ArrayList<Kysymys> loydetyt = new ArrayList<Kysymys>();
        for (Kysymys kys : alkiot)
            if ( kys.getKoehenkiloNro() == koehenkiloN ) loydetyt.add(kys);
        return loydetyt;
    }
    
    
// ---------------------------------------------------------------
// ---------- HT7-vaihe (kysymysten nayttaminen ja muokkaus ------
// ---------------------------------------------------------------�
    
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
     */
    public int getLkm() {
        return alkiot.size();
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
     */
    public static void main(String[] args) {
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
    }
    
}
