package kysely;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Vastaukset-luokka
 * @author Antiik & Doomslizer
 * @version 9 Nov 2021
 *
 */
public class Vastaukset {
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
     * @param vas lisattava kysymys
     */
    public void lisaa(Vastaus vas) {
        // Tietorakenteesta loytyy valmiiksi .add-metodi lisaamiseen
        alkiot.add(vas);
    }
    
    
    /**
     * Etsitaan koehenkilon vastaukset
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
    public List<Vastaus> annaVastaukset(int id) {
        ArrayList<Vastaus> loydetyt = new ArrayList<Vastaus>();
        for (Vastaus vas: alkiot) // Toimii iteraattorin ansiosta
            if (vas.getId() == id) loydetyt.add(vas);
        return loydetyt;
    }
    
    
    /**
     * Testi paaohjelma kysymykset-luokalle
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Vastaukset vastaukset = new Vastaukset();
        
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
        
        List<Vastaus> vastaukset2 = vastaukset.annaVastaukset(4);
        for (Vastaus vas: vastaukset2) {
            System.out.print(vas.getKoehenkiloNro() + " ");
            vas.tulosta(System.out);
        }
        
    }
}
