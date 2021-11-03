/**
 * 
 */
package kysely;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Kysymykset-luokka
 * @author Antiik & Doomslizer
 * @version 3 Nov 2021
 *
 */
public class Kysymykset {
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
    }
    
    
    /**
     * Etsitaan koehenkilon kysymykset
     * @param id koehenkilon keta etsitaan
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
    public List<Kysymys> annaKysymykset(int id) {
        ArrayList<Kysymys> loydetyt = new ArrayList<Kysymys>();
        for (Kysymys kys: alkiot) // Toimii iteraattorin ansiosta
            if (kys.getId() == id) loydetyt.add(kys);
        return loydetyt;
    }
    
    
    /**
     * Testi paaohjelma kysymykset-luokalle
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kysymykset kysymykset = new Kysymykset();
        
        Kysymys kyssari1 = new Kysymys();
        kyssari1.taytaEsimKysymysTiedot(1);
        Kysymys kyssari2 = new Kysymys();
        kyssari2.taytaEsimKysymysTiedot(2);
        Kysymys kyssari3 = new Kysymys();
        kyssari3.taytaEsimKysymysTiedot(3);
        Kysymys kyssari4 = new Kysymys();
        kyssari4.taytaEsimKysymysTiedot(4);
        
        kysymykset.lisaa(kyssari1);
        kysymykset.lisaa(kyssari2);
        kysymykset.lisaa(kyssari3);
        kysymykset.lisaa(kyssari4);
        
        System.out.println("----- Kysymykset testausta -----");
        
        List<Kysymys> kysymykset2 = kysymykset.annaKysymykset(4);
        for (Kysymys kys: kysymykset2) {
            System.out.print(kys.getKoehenkiloNro() + " ");
            kys.tulosta(System.out);
        }

    }
}
