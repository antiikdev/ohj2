/**
 * 
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Koehenkilo-luokka
 * @author Antiik & Doomslizer
 * @version 21 Oct 2021
 *
 */
public class Koehenkilo {
    private int     koehenkiloNro;
    private String  nimi            = ""; // JOS halutaan nimeta koehenkiloita
    private String  sukupuoli       = ""; // alustetaan tyhjaksi, jotta ei NULL
    private String  ikaryhma        = "";
     
    private static int seuraavaNro    = 1;
    
    
    /**
     * Alustetaan koehenkilo tyhjaksi
     */
    public Koehenkilo() {
        // Alustuslauseet hoitaa
    }
    
    
    /**
     * Arvotaan satunnainen kokoknaisluku valille:
     * @param ala arvonnan alaraja
     * @param yla arvonnan ylaraja
     * @return satunnainen luku ala ja yla valilta
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    
    /**
     * Taytetaan esimerkkitiedot
     */
    public void taytaEsimTiedot() {
        nimi = "k" + rand(100, 999);
        sukupuoli = "f";
        ikaryhma = "21-28";
    }
    
    
    /**
     * 
     * Tulostetaan koehenkilon tiedot
     * @param out tietovirtaan tulostus
     */
    public void tulosta(PrintStream out) {
        out.println("Koehenkilonumero " + koehenkiloNro);
        out.println("Nimi " + nimi);
        out.println("Sukupuoli " + sukupuoli);
        out.println("Ikaryhma " + ikaryhma);
    }
    
    
    /**
     * 
     * MYOHEMPAA kayttoa varten yksinkertaisia
     * tulostuksia koehenkilon tiedoista
     * @param os tietovirtaan tulostus
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Rekisteroidaan koehenkilo
     * @return koehenkilon numeron
     * @example
     * <pre name="test">
     *  Koehenkilo p01 = new Koehenkilo();
     *  p01.getKoehenkiloNro()  === 0;
     *  p01.rekisteroi();
     *  Koehenkilo p02 = new Koehenkilo();
     *  p02.rekisteroi();
     *  int n1 = p01.getKoehenkiloNro();
     *  int n2 = p02.getKoehenkiloNro();
     *  n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.koehenkiloNro = seuraavaNro;
        seuraavaNro++;
        return this.koehenkiloNro;
        
    }
    
    
    /**
     * Palauttaa koehenkilon numeron
     * @return koehenkilon numeron
     */
    public int getKoehenkiloNro() {
        return koehenkiloNro;
    }
    
    
    /**
     * Palauttaa koehenkilon nimen
     * @return koehenkilon nimi
     */
    public String getNimi() {
        return nimi;
    }

    
    /**
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Koehenkilo p01 = new Koehenkilo();
        Koehenkilo p02 = new Koehenkilo();
        
        p01.rekisteroi();
        p02.rekisteroi();
        
        p01.tulosta(System.out);
        p01.taytaEsimTiedot();
        p01.tulosta(System.out);
        
        p02.tulosta(System.out);
        p02.taytaEsimTiedot();
        p02.tulosta(System.out);
    }

}