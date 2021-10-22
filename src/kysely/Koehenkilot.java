/**
 * 
 */
package kysely;

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
    
    
    /**
     * Luodaan alaustava taulukko
     * TODO: taulukon kasvattaminen
     */
    public Koehenkilot() {
        alkiot = new Koehenkilo[MAX_KOEHENKILOITA];
    }
    
    
    /**
     * @param koehenkilo uusi
     * @throws TallennaException jos tietorakenne on jo taynna
     */
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        if ( lkm >= alkiot.length) throw new TallennaException("Liikaa alkioita");
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
        
        try {
            koehenkilot.lisaa(p01);
            koehenkilot.lisaa(p02);
            koehenkilot.lisaa(p02);
            koehenkilot.lisaa(p02);
            koehenkilot.lisaa(p02);
            koehenkilot.lisaa(p02);
            koehenkilot.lisaa(p02);
        } catch (TallennaException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("--- Koehenkilot testi ---");
        
        for (int i = 0; i < koehenkilot.getLkm(); i++) {
            Koehenkilo koehenkilo = koehenkilot.anna(i);
            System.out.println("Koehenkilo indeksi: " + i);
            koehenkilo.tulosta(System.out);
        }
    }

}
