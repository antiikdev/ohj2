/**
 * 
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Kysymys-luokka
 * @author Antiik & Doomslizer
 * @version 3 Nov 2021
 *
 */
public class Kysymys {
    private int id;
    private int koehenkiloNro;
    private String kysymys;
    private String kysymysTyyppi;
    private String vastausVaihtoehdot;
    
    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan kysymys
     */
    public Kysymys() {
        // ei tarvi alustaa
    }
    
    /**
     * Alustetaan id-numero kysymykselle
     * @param id kysymyksen numero
     */
    public Kysymys(int id) {
        this.id = id;
    }
    
    
    /**
     * ESIMERKKI taytto kysymystiedoille  
     * @param idNumero henkilon id-numero, joka vastannut kysymykseen
     */
    public void taytaEsimKysymysTiedot(int idNumero) {
        this.id = idNumero;
        this.kysymys = "Mika seuraavista on kissapetoelain?";
        this.kysymysTyyppi = "Monivalinta";
        this.vastausVaihtoehdot = "A) Kissa, B) Tiikeri";
    }
    
    
    /**
     * Tulostetaan kysymyksen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(kysymys + " " + kysymysTyyppi + " " + vastausVaihtoehdot);
    }
    
    
    /**
     * Tulostetaan koehenkilon tiedot
     * @param os tulostettava tietovirta
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Kysymykselle seuraava id-numero
     * @return kysymyksen uusi id-numero
     * @example
     * <pre name="test">
     *   Kysymys k1 = new Kysymys();
     *   k1.getId() === 0;
     *   k1.rekisteroi();
     *   Kysymys k2 = new Kysymys();
     *   k2.rekisteroi();
     *   int n1 = k1.getId();
     *   int n2 = k2.getId();
     *   n1 === n2-1;
     * </pre>

     */
    public int rekisteroi() {
        this.id = seuraavaNro;
        seuraavaNro++;
        return this.id;
    }
    
    
    /**
     * Palautetaan kysymyksen id-numero
     * @return kysymyksen id
     */
    public int getId() {
        return this.id;
    }
    
    
    /**
     * Mille koehenkilolle kysymys kuuluu
     * @return koehenkilon id-numero
     */
    public int getKoehenkiloNro() {
        return this.koehenkiloNro;
    }
    

    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kysymys kyssari = new Kysymys();
        kyssari.taytaEsimKysymysTiedot(1);
        kyssari.tulosta(System.out);
    
    }

}
