/**
 * 
 */
package kysely;

/**
 * @author ilkka
 * @version 21 Oct 2021
 *
 */
public class Koehenkilot {
    
    private static final int MAX_KOEHENKILOITA = 5;
    private int lkm = 0;
    private Koehenkilo[] alkiot;
    
    
    /**
     * Luodaan laustava taulukko
     * TODO: taulukon kasvattaminen
     */
    public Koehenkilot() {
        alkiot = new Koehenkilo[MAX_KOEHENKILOITA];
    }
    
    
    /**
     * @param koehenkilo uusi
     * TODO: TallennaException? Testit?
     */
    public void lisaa(Koehenkilo koehenkilo) {
        if ( lkm >= alkiot.length) return;
        this.alkiot[this.lkm] = koehenkilo;
        lkm++;
    }
    
    
    /**
     * Palauttaa viitteen i:teen alkioon
     * @param i alkio
     * @return alkion viite
     */
    public Koehenkilo anna(int i) {
        return alkiot[i];
    }
    
    
    
    /**
     * Palauttaa kyselyn koehenkiloiden lukumaaran
     * @return koehenkiloiden lukumaara
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Koehenkilot koehenkilot = new Koehenkilot();
        Koehenkilo p01 = new Koehenkilo();
        Koehenkilo p02 = new Koehenkilo();
        p01.rekisteroi();
        p01.taytaEsimTiedot();
        p02.rekisteroi();
        p02.taytaEsimTiedot();
        
        // TODO: try-catch?
        koehenkilot.lisaa(p01);
        koehenkilot.lisaa(p02);
        
        System.out.println("--- Koehenkilot testi ---");
        
        for (int i = 0; i < koehenkilot.getLkm(); i++) {
            Koehenkilo koehenkilo = koehenkilot.anna(i);
            System.out.println("Koehenkilo indeksi: " + i);
            koehenkilo.tulosta(System.out);
        }
    }

}
