package kysely;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Kysymys-luokka
 * @author Antiik & Doomslizer
 * @version 9 Nov 2021
 *
 */
public class Vastaus {
    private int id;
    private int koehenkiloNro;
    private String vastaus;
    private String vastausTyyppi;
    private String vastausVaihtoehdot;
    
    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan vastaus
     */
    public Vastaus() {
        // ei tarvi alustaa
    }
    
    /**
     * Alustetaan id-numero vastaukselle
     * @param id vastauksen numero
     */
    public Vastaus(int id) {
        this.id = id;
    }
    
    
    /**
     * ESIMERKKI taytto vastaustiedoille  
     * @param idNumero henkilon id-numero, joka vastannut vastauksen
     */
    public void taytaEsimVastausTiedot(int idNumero) {
        this.id = idNumero;
        this.vastaus = "A) Kissa";
        this.vastausTyyppi = "Monivalinta";
        this.vastausVaihtoehdot = "A) Kissa, B) Tiikeri";
    }
    
    
    /**
     * Tulostetaan vastauksen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Vastaus: " + vastaus + " " + vastausTyyppi + " Vaihtoehdot: " + vastausVaihtoehdot);
    }
    
    
    /**
     * Tulostetaan koehenkilon tiedot
     * @param os tulostettava tietovirta
     */
    public void tulosta(OutputStream os) {
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
    
    
    

    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Vastaus vassari = new Vastaus();
        vassari.taytaEsimVastausTiedot(1);
        vassari.tulosta(System.out);
    
    }
}
