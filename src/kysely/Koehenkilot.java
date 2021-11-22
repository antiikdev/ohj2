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
import java.util.Scanner;

/**
 * Koehenkilot-luokka
 * @author Antiik & Doomslizer
 * @version 21 Oct 2021
 *
 */
public class Koehenkilot {
    
    private static final int MAX_KOEHENKILOITA = 8;
    private int lkm = 0;
    private Koehenkilo[] alkiot;
    private String tiedostonPerusNimi = "";
    
    
    /**
     * Luodaan alaustava taulukko
     * TODO: taulukon kasvattaminen
     */
    public Koehenkilot() {
        alkiot = new Koehenkilo[MAX_KOEHENKILOITA];
    }
    
    
    /**
     * Lisataan uusi koehenkilo
     * @param koehenkilo uusi
     * @throws TallennaException jos tietorakenne on jo taynna
     */
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        if ( lkm > alkiot.length ) throw new TallennaException("Liikaa alkioita");
        if ( lkm >= alkiot.length) {
            Koehenkilo[] uusi = new Koehenkilo[lkm + 10]; // Kasvatetaan +10
            for (int i = 0; i < lkm; i++) {
                uusi[i] = this.alkiot[i];
            }
            this.alkiot = uusi;
        }
        this.alkiot[this.lkm] = koehenkilo;
        lkm++;
    }
    
    
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
// -------------------------- HT6-vaihe --------------------------
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
     * 1|k100|m|15-21|
     * 2|k101|f|21-28|
     * </pre>
     * @throws TallennaException poikkeus jos tallennus epäonnistuu
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
    
    /**
     * Paaohjelma luokan testaamiseen
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
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

    }

}
