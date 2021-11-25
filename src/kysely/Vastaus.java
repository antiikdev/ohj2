package kysely;
import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Vastaus-luokka
 * @author Antiik & Doomslizer
 * @version 9 Nov 2021
 *
 */
public class Vastaus {
    private int id;
    private int koehenkiloNro;
    private String vastaus;
    private String vastausTyyppi;
    private String vastausVaihtoehdot; //Varmaan turha
    
    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan vastaus
     */
    public Vastaus() {
        // ei tarvi alustaa
    }
    

    /**
     * Alustetaan tietyn koehenkilon vastaus
     * @param koehenkiloN vastauksen viitenumero
     */
    public Vastaus(int koehenkiloN) {
        this.koehenkiloNro = koehenkiloN;
    }
    
    
    /**
     * ESIMERKKI taytto vastaustiedoille  
     * @param koehenkiloN henkilon numero, joka vastannut vastauksen
     */
    public void taytaEsimVastausTiedot(int koehenkiloN) {
        this.koehenkiloNro = koehenkiloN;
        this.vastaus = "A) Kissa";
        this.vastausTyyppi = "Monivalinta";
        this.vastausVaihtoehdot = "A) Kissa, B) Tiikeri"; //tätä tietoa ei varmaan tarvii
    }
    
    
    /**
     * Tulostetaan vastauksen tiedot
     * @param out tietovirta johon tulostetaan
     * @throws TallennaException poikkeustilanteessa
     */
    public void tulosta(PrintStream out) throws TallennaException {
        out.println(id + "|" + koehenkiloNro + "|" + vastaus + "|"
                        + vastausTyyppi + "|" + vastausVaihtoehdot); // -vastausvaihtoehdot
    }
    
    
    /**
     * Tulostetaan koehenkilon tiedot
     * @param os tulostettava tietovirta
     * @throws TallennaException poikkeustilanteessa
     */
    public void tulosta(OutputStream os) throws TallennaException {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Vastaukselle seuraava id-numero
     * @return vastauksen uusi id-numero
     * @example
     * <pre name="test">
     *   Vastaus v1 = new Vastaus();
     *   v1.getId() === 0;
     *   v1.rekisteroi();
     *   Vastaus v2 = new Vastaus();
     *   v2.rekisteroi();
     *   int n1 = v1.getId();
     *   int n2 = v2.getId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.id = seuraavaNro;
        seuraavaNro++;
        return this.id;
    }
    
    
    /**
     * Palautetaan vastauksen id-numero
     * @return kysymyksen id
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Mille koehenkilolle vastaus kuuluu
     * @return koehenkilon id-numero
     */
    public int getKoehenkiloNro() {
        return this.koehenkiloNro;
    }
    
    // HT6
    /**
     * Asettaa id numeron
     * @param nro asetettava id
     */
    private void setId(int nro) {
        id = nro; 
    }
    
    
    /**
     * Asettaa koehenkilolle numeron
     * @param nro asetettava numero
     */
    private void setKoehenkiloNro(int nro) {
        koehenkiloNro = nro; 
    }
    
    
    /**
     * Tulostetaan merkkijonona
     * TODO: Testit
     */
    @Override
    public String toString() {
        return "" +
                getId() + "|" +
                getKoehenkiloNro() + "|" +
                vastaus + "|" +
                vastausTyyppi + "|" +
                vastausVaihtoehdot;
    }
    
    
    /**
     * Vastauksen tiedot selvitetaan,
     * erotellaan |-merkilla merkkijonosta.
     * Huolehtii, etta seuraavaNro on suurempi kuin tuleva id-numero
     * @param rivi merkkijonorivi joka luetaan
     * TODO: Testit
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        setKoehenkiloNro(Mjonot.erota(sb, '|', getKoehenkiloNro()));
        vastaus = Mjonot.erota(sb, '|', vastaus);
        vastausTyyppi = Mjonot.erota(sb, '|', vastausTyyppi);
        vastausVaihtoehdot = Mjonot.erota(sb, '|', vastausVaihtoehdot);
    }
    

    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Vastaus vassari = new Vastaus();
        vassari.taytaEsimVastausTiedot(1);
        try
        {
            vassari.tulosta(System.out);
        } catch (TallennaException e)
        {
            e.printStackTrace();
        }
        
    
    }
}
