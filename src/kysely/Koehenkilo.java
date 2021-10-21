/**
 * 
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Koehenkilo-luokka
 * @author ilkka & topi
 * @version 21 Oct 2021
 *
 */
public class Koehenkilo {
    private int     koehenkiloNro;
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
     * Taytetaan esimerkkitiedot
     */
    public void taytaEsimTiedot() {
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
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Koehenkilo p01 = new Koehenkilo();
        Koehenkilo p02 = new Koehenkilo();
        
        // p01.rekisteroi();
        // p02.rekisteroi();
        
        p01.tulosta(System.out);
        // p01.taytaEsimTiedot();
        p01.tulosta(System.out);
        
        p02.tulosta(System.out);
        // p02.taytaEsimTiedot();
        p02.tulosta(System.out);
    }

}
