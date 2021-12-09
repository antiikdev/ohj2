/**
 * 
 */
package kysely;

import java.io.File;
import java.util.List;

/**
 * Kysely-luokka
 * Sisalto: Class-responsibility-collaboration (CRC) cards
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 * @version 7 Dec 2021
 */
public class Kysely {
    private Koehenkilot koehenkilot = new Koehenkilot();
    private Kysymykset kysymykset = new Kysymykset();
    private Vastaukset vastaukset = new Vastaukset();
    
    /**
     * Lisataan koehenkilo
     * @param koehenkilo koehenkilo
     * @throws TallennaException jos lisaysta ei voida tehda
     */
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        this.koehenkilot.lisaa(koehenkilo);
    }
    
    
    /**
     * Lisataan kysymys
     * @param kysymys lisattava kysymys
     * @throws TallennaException jos ongelmia
     */
    public void lisaa(Kysymys kysymys) throws TallennaException {
        this.kysymykset.lisaa(kysymys);
    }
    
    /**
     * Lisataan vastaus
     * @param vastaus lisattava vastaus
     * @throws TallennaException jos ongelmia
     */
    public void lisaa(Vastaus vastaus) throws TallennaException {
        this.vastaukset.lisaa(vastaus);
    }
    
    /**
     * @return koehenkiloiden lukumaaran
     */
    public int getKoehenkiloita() {
        return this.koehenkilot.getLkm();
    }
    
    
    /**
     * Antaa Kyselyn i:n koehenkilon
     * @param i monesko koehenkilo (alkaa 0:sta)
     * @return koehenkilo paikasta i
     */
    public Koehenkilo annaKoehenkilo(int i) {
        return koehenkilot.anna(i);
    }
    
    
    /**
     * Antaa koehenkilon kysymykset 
     * @param koehenkilo jonka kysymyksia haetaan
     * @return tietorakenne, jossa viitteet loydettyihin kysymyksiin
     * @example
     * <pre name="test">
     *  #import java.util.*;
     *  
     *  Kysely kysely = new Kysely();
     *  Koehenkilo k01 = new Koehenkilo(); Koehenkilo k02 = new Koehenkilo();
     *  Koehenkilo k03 = new Koehenkilo();
     *  
     *  k01.rekisteroi(); k02.rekisteroi(); k03.rekisteroi();
     *  int id1 = k01.getKoehenkiloNro(); int id2 = k02.getKoehenkiloNro();
     *  
     *  Kysymys kys1 = new Kysymys(id1); kysely.lisaa(kys1);
     *  Kysymys kys2 = new Kysymys(id2); kysely.lisaa(kys2);
     *  
     *  List<Kysymys> loydetyt;
     *  loydetyt = kysely.annaKysymykset(k03);
     *  loydetyt.size() === 0;
     *  loydetyt = kysely.annaKysymykset(k01);
     *  loydetyt.size() === 1;
     *  loydetyt.get(0) == kys1 === true;
     * </pre>
     */
    public List<Kysymys> annaKysymykset(Koehenkilo koehenkilo) {
        return kysymykset.annaKysymykset(koehenkilo.getKoehenkiloNro());
    }
    
    /**
     * Antaa koehenkilon vastaukset 
     * @param koehenkilo jonka vastauksia haetaan
     * @return tietorakenne, jossa viitteet loydettyihin vastauksiin
     * @example
     * <pre name="test">
     *  #import java.util.*;
     *  
     *  Kysely kysely = new Kysely();
     *  Koehenkilo k01 = new Koehenkilo(); Koehenkilo k02 = new Koehenkilo();
     *  Koehenkilo k03 = new Koehenkilo();
     *  
     *  k01.rekisteroi(); k02.rekisteroi(); k03.rekisteroi();
     *  int id1 = k01.getKoehenkiloNro(); int id2 = k02.getKoehenkiloNro();
     *  
     *  Vastaus vas1 = new Vastaus(id1); kysely.lisaa(vas1);
     *  Vastaus vas2 = new Vastaus(id2); kysely.lisaa(vas2);
     *  
     *  List<Vastaus> loydetyt;
     *  loydetyt = kysely.annaVastaukset(k03);
     *  loydetyt.size() === 0;
     *  loydetyt = kysely.annaVastaukset(k01);
     *  loydetyt.size() === 1;
     *  loydetyt.get(0) == kys1 === true;
     * </pre>
     */
    public List<Vastaus> annaVastaukset(Koehenkilo koehenkilo) {
        return vastaukset.annaVastaukset(koehenkilo.getKoehenkiloNro());
    }
    
    
// ---------------------------------------------------------------
// ---------- HT7-vaihe tietojen tallennus ja poisto -------------
// ---------------------------------------------------------------
    
    /**
     * Korvaa tai lisaa kysymyksen
     * @param kysymys viite
     * @throws TallennaException jos taynna
     */
    public void korvaaTaiLisaa(Kysymys kysymys) throws TallennaException { 
        kysymykset.korvaaTaiLisaa(kysymys); 
    }    
    
	/**
	 * Poistaa koehenkilon kyselysta
	 * @param koehenkilo joka poistetaan
	 * @throws TallennaException jos virhe
	 */
	public void poistaKoehenkilo(Koehenkilo koehenkilo) throws TallennaException {
		koehenkilot.poistaKoehenkilo(koehenkilo);
	}
    
// ---------------------------------------------------------------  
// ---------------------------------------------------------------   

    
// ---------------------------------------------------------------
// ---------- HT6-vaihe (tiedoston tallennus ja luku -------------
// ---------------------------------------------------------------
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        // Vaihtoehto: kansioon tiedoston luonti?
        // File dir = new File(nimi);
        // dir.mkdirs();
        // String hakemistonNimi = "";
        // if ( !nimi.isEmpty() ) hakemistonNimi = "koehenkilot.dat";
        koehenkilot.setTiedostonPerusNimi(nimi);

        // Vaihtoehto: Kysymykset ja vastaukset
        
    }

    
    /**
     * @param nimi tiedoston josta luetaan
     * @throws TallennaException virhe talteen
     */
    public void lueTiedostosta(String nimi) throws TallennaException {
        // tyhjennetaan jos olemassa oleva
        koehenkilot = new Koehenkilot(); 
        kysymykset = new Kysymykset();
        vastaukset = new Vastaukset();

        setTiedosto(nimi);
        koehenkilot.lueTiedostosta();
        kysymykset.lueTiedostosta();
        vastaukset.lueTiedostosta();
    }
    
    
    /**
     * Tallentaa tiedot tiedostoon
     * Tallenttaa kyselyn tiedot tiedostoon.  
     * @throws TallennaException jos tallettamisessa ongelmia
     */
    public void tallenna() throws TallennaException {
        // HT6: tallennetaan kaikki tietueet ja
        // otetaan kaikista talteen virheet jos niita loytyy
        String virhe = "";
        
        // Koehenkilot tallennus
        try {
            koehenkilot.tallenna();
        } catch ( TallennaException ex ) {
            virhe = ex.getMessage();
        }
        
        // Kysymykset tallennus
        try {
            kysymykset.tallenna();
        } catch ( TallennaException ex ) {
            virhe += ex.getMessage();
        }
        
        try {
            vastaukset.tallenna();
        } catch ( TallennaException ex ) {
            virhe += ex.getMessage();
        }
        
        
        if ( !"".equals(virhe) ) throw new TallennaException(virhe);
        
    }
// ---------------------------------------------------------------  
// ---------------------------------------------------------------    
    
    
    /**
     * Paaohjelma testaamiseen
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kysely kysely = new Kysely();
        
        // Tiedostosta lukemisen testaus:
        String tiednimi = "koehenkilot.dat";
        try {
            kysely.lueTiedostosta(tiednimi);
        } catch (TallennaException ex) {
            System.err.println(ex);
        }
        
        Koehenkilo p01 = new Koehenkilo();
        Koehenkilo p02 = new Koehenkilo();
        p01.rekisteroi();
        p01.taytaEsimTiedot();
        p02.rekisteroi();
        p02.taytaEsimTiedot();
        
        // Virheiden kasittely, sailotaan eli tallennetaan: TallennaException
        try {
            kysely.lisaa(p01);
            kysely.lisaa(p02);
        } catch (TallennaException e) {
            System.err.println(e.getMessage());
        }
        
        for (int i = 0; i < kysely.getKoehenkiloita(); i++) {
            Koehenkilo koehenkilo = kysely.annaKoehenkilo(i);
            koehenkilo.tulosta(System.out);
        }
    }

}
