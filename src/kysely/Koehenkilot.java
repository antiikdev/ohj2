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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fi.jyu.mit.ohj2.Mjonot;
import fi.jyu.mit.ohj2.WildChars;

/**
 * Koehenkilot-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 *
 */
public class Koehenkilot implements Iterable<Koehenkilo> {
    
    private static final int MAX_KOEHENKILOITA = 8;
    private int lkm = 0;
    private Koehenkilo[] alkiot;
    private String tiedostonPerusNimi = "";
    
    
    /**
     * Luodaan alustava taulukko
     */
    public Koehenkilot() {
        alkiot = new Koehenkilo[MAX_KOEHENKILOITA];
    }
    
    
    /**
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
    
    //Haku TODO testit
    /**
     * Luokka koehenkiloiden iteroimiseen
     * @author TopiK
     * @version 12.12.2021
     *
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
     * @param hakuehto mitä haetaan
     * @param k etsittävän indeksi
     * @return tietorakenteen löydetyistä
     */
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
