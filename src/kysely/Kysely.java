/**
 * 
 */
package kysely;

import java.util.List;

/**
 * Kysely-luokka
 * Sisalto: Class-responsibility-collaboration (CRC) cards
 * @author Antiik & Doomslizer
 * @version 22 Oct 2021
 *
 */
public class Kysely {
    private Koehenkilot koehenkilot = new Koehenkilot();
    private final Kysymykset kysymykset = new Kysymykset();
    
    /**
     * Lisataan koehenkilo
     * @param koehenkilo koehenkilo
     * @throws TallennaException jos lisaysta ei voida tehda
     *
     */
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        this.koehenkilot.lisaa(koehenkilo);
    }
    
    
    /**
     * Lisataan kysymys
     * @param kysymys lisattava kysymys
     * TODO: testit
     */
    public void lisaa(Kysymys kysymys) {
        this.kysymykset.lisaa(kysymys);
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
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kysely kysely = new Kysely();
        
        Koehenkilo p01 = new Koehenkilo();
        Koehenkilo p02 = new Koehenkilo();
        p01.rekisteroi();
        p01.taytaEsimTiedot();
        p02.rekisteroi();
        p02.taytaEsimTiedot();
        
        // Virheiden kasittely, sailotaan eli tallennetaan
        // TallennaException paikkaan
        try {
            kysely.lisaa(p01);
            kysely.lisaa(p02);
            kysely.lisaa(p02);
            kysely.lisaa(p02);
            kysely.lisaa(p02);
            kysely.lisaa(p02);
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
