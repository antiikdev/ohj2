/**
 * 
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

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
     * Alustetaan tietyn koehenkilon Kysymys
     * @param koehenkiloN kysymyksen viitenumero
     */
    public Kysymys(int koehenkiloN) {
        this.koehenkiloNro = koehenkiloN;
    }
    
    
    /**
     * ESIMERKKI taytto kysymystiedoille  
     * @param numero kysymyksen
     */
    public void taytaEsimKysymysTiedot(int numero) {
        this.koehenkiloNro = numero;
        this.kysymys = "Kissapetoelain?";
        this.kysymysTyyppi = "Monivalinta";
        this.vastausVaihtoehdot = "a)Kissa,b)Tiikeri";
    }
    
    
    /**
     * Tulostetaan kysymyksen tiedot
     * @param out tietovirta johon tulostetaan
     * @throws TallennaException jos poikkeus
     */
    public void tulosta(PrintStream out) throws TallennaException  {
        out.println(id + "|" + koehenkiloNro + "|"  + kysymys + "|" +
                    kysymysTyyppi + "|" + vastausVaihtoehdot);
    }
    
    
    /**
     * Tulostetaan koehenkilon tiedot
     * @param os tulostettava tietovirta
     * @throws TallennaException jos poikkeus
     */
    public void tulosta(OutputStream os) throws TallennaException {
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
     * Palautetaan mille koehenkilolle kysymys kuuluu
     * @return jäsenen id
     */
    public int getKoehenkiloNro() {
        return this.koehenkiloNro;
    }

    
// -------------------------------------------------------------
// ------------------------- HT6-vaihe -------------------------
// -------------------------------------------------------------
  
   /**
    * Asettaa id numeron
    * @param nr asetettava id
    */
   private void setId(int nr) {
       id = nr; 
   }
   
   
   /**
    * Asettaa koehenkilolle numeron
    * @param nr asetettava numero
    */
   private void setKoehenkiloNro(int nr) {
       koehenkiloNro = nr; 
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
               kysymys + "|" +
               kysymysTyyppi + "|" +
               vastausVaihtoehdot;
   }
   
   
   /**
    * Kysymyksen tiedot selvitetaan,
    * erotellaan |-merkilla merkkijonosta.
    * Huolehtii, etta seuraavaNro on suurempi kuin tuleva id-numero
    * @param rivi merkkijonorivi joka luetaan
    * TODO: Testit
    */
   public void parse(String rivi) {
       StringBuilder sb = new StringBuilder(rivi);
       setId(Mjonot.erota(sb, '|', getId()));
       setKoehenkiloNro(Mjonot.erota(sb, '|', getKoehenkiloNro()));
       kysymys = Mjonot.erota(sb, '|', kysymys);
       kysymysTyyppi = Mjonot.erota(sb, '|', kysymysTyyppi);
       vastausVaihtoehdot = Mjonot.erota(sb, '|', vastausVaihtoehdot);
   }
   
// -------------------------------------------------------------
// -------------------------------------------------------------
    

    /**
     * Paaohjelma testausta varten
     * @param args ei kaytossa
     */
    public static void main(String[] args) {
        Kysymys kyssari = new Kysymys();
        kyssari.taytaEsimKysymysTiedot(1);
        
        try {
            kyssari.tulosta(System.out);
        } catch (TallennaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
    }

}
