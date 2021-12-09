package kysely;
/**
 * HUOM! Vastaukset- ja Vastaus-luokat EIVAT OLE KAYTOSSA!
 * Tata luokkaa kaytettiin HT1-6 vaiheissa tayttamaan parityon vaatimukset
 * kahdesta tietorakenteesta.
 * Ohjelma toteuttaa samat toiminnallisuudet HT7-vaihessa Kysymykset-Kysymys-luokilla.
 */
/**
 * Vastaukset-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 * @version 3 Nov 2021
 *
 */

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
 * Vastaukset-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 * @version 9 Nov 2021
 *
 */
public class Vastaukset {
    private String tiedostonPerusNimi = "";
    // private boolean muutettu = false;
    
 // Tietorakenteiden perintahierrarkiasta ylimmaisen Collection > Iterable
    private Collection<Vastaus> alkiot = new ArrayList<Vastaus>();
    
    
    /**
     * Vastaukset alustaminen
     */
    public Vastaukset() {
        // ei tarvitse alustaa
    }
    
    
    /**
     * Lisataan vastaus
     * @param vas lisattava vastaus
     */
    public void lisaa(Vastaus vas) {
        // Tietorakenteesta loytyy valmiiksi .add-metodi lisaamiseen
        alkiot.add(vas);
        // muutettu = true;
    }
    
    
    /**
     * Etsitaan koehenkilon vastaukset
     * @param koehenkiloN koehenkilon keta etsitaan
     * @return loydetyista lista
     * @example
     * <pre name="test">
     *  #import java.util.*;
     *  Vastaukset vassarit = new Vastaukset();
     *  Vastaus vas1 = new Vastaus(1); vassarit.lisaa(vas1);
     *  Vastaus vas2 = new Vastaus(2); vassarit.lisaa(vas2);
     *  Vastaus vas3 = new Vastaus(1); vassarit.lisaa(vas3);
     *  Vastaus vas4 = new Vastaus(2); vassarit.lisaa(vas4);
     *  
     *  List<Vastaus> loydetyt;
     *  loydetyt = vassarit.annaVastaukset(6);
     *  loydetyt.size() === 0;
     *  loydetyt = vassarit.annaVastaukset(1);
     *  loydetyt.size() === 2;
     *  loydetyt.get(0) == vas1 === true;
     * </pre>
     */
    public List<Vastaus> annaVastaukset(int koehenkiloN) {
        ArrayList<Vastaus> loydetyt = new ArrayList<Vastaus>();
        for (Vastaus vas: alkiot) // Toimii iteraattorin ansiosta
            if (vas.getKoehenkiloNro() == koehenkiloN) loydetyt.add(vas);
        return loydetyt;
    }
    
    //HT6
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
        // String nimi = "tutkimus/vastaukset.dat";
        if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "vastaukset.dat";
        try (Scanner fi = new Scanner(new FileInputStream(new File(tiedostonPerusNimi)))) {
            
            String rivi = "";
            while ( fi.hasNext() ) {
                rivi = rivi.trim();
                rivi = fi.nextLine();
                Vastaus vas = new Vastaus();
                vas.parse(rivi);
                lisaa(vas);
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
     * id| koehenkiloNro| vastaus  |kysymysTyyppi|vastausVaihtoehdot|
     * 1|k000|b) tiikeri           |monivalinta|a) kissa, b) tiikeri|
     * 2|k001|5                    |likert|0: en pida, 5: paljon|
     * </pre>
     * @throws TallennaException poikkeus jos tallennus epäonnistuu
     */
    public void tallenna() throws TallennaException {
        // if ( !muutettu ) return;
        
        if ( tiedostonPerusNimi.length() <= 0 ) tiedostonPerusNimi = "vastaukset.dat";
        File ftied = new File(tiedostonPerusNimi);
        // Vaihtoehto (HT6): jos ehtii backup-tiedostojen tekemisen (esim. harrastukset.java)
        
        // HT6 tallentaminen:
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (Vastaus vas : this.alkiot) {
                fo.println(vas.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new TallennaException("Tiedosto " + ftied.getName() + " ei aukea");
        }
        // muutettu = false;
    }
    
    
    
    /**
     * Testi paaohjelma vastaukset-luokalle
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Vastaukset vastaukset = new Vastaukset();
        
        try {
            vastaukset.lueTiedostosta();
        } catch (TallennaException ex) {
            System.err.println(ex);
        }
        
        Vastaus vassari1 = new Vastaus();
        vassari1.taytaEsimVastausTiedot(1);
        Vastaus vassari2 = new Vastaus();
        vassari2.taytaEsimVastausTiedot(2);
        Vastaus vassari3 = new Vastaus();
        vassari3.taytaEsimVastausTiedot(3);
        Vastaus vassari4 = new Vastaus();
        vassari4.taytaEsimVastausTiedot(4);
        
        vastaukset.lisaa(vassari1);
        vastaukset.lisaa(vassari2);
        vastaukset.lisaa(vassari3);
        vastaukset.lisaa(vassari4);
        
        System.out.println("----- Vastaukset testausta -----");
        List<Vastaus> vastaukset2 = vastaukset.annaVastaukset(2);

        try {
            for (Vastaus vas : vastaukset2) {
                // System.out.print(kys.getKoehenkiloNro() + " ");
                vas.tulosta(System.out);
            }
        } catch (TallennaException e) {
            e.printStackTrace();
        }
        
        try {
            vastaukset.tallenna();
        } catch (TallennaException e) {
            e.printStackTrace();
        }
        
    }
}
