/**
 * 
 */
package kysely;

/**
 * Kyselytutkimus
 * Sisalto: Class-responsibility-collaboration (CRC) cards
 * @author Antiik & Doomslizer
 * @version 22 Oct 2021
 *
 */
public class Kysely {
    private Koehenkilot koehenkilot = new Koehenkilot();
    
    
    /**
     * Lisataan koehenkilo
     * @param koehenkilo koehenkilo
     * @throws TallennaException jos lisaysta ei voida tehda
     *
     */
    // TODO: TallennaException?
    public void lisaa(Koehenkilo koehenkilo) throws TallennaException {
        this.koehenkilot.lisaa(koehenkilo);
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
