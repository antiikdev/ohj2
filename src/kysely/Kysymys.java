/**
 * 
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kysymys-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
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
     * @return j√§senen id
     */
    public int getKoehenkiloNro() {
        return this.koehenkiloNro;
    }
    
    
// -------------------------------------------------------------
// ------------------------- HT7-vaihe -------------------------
// -------------------------------------------------------------

    /**
     * @return ensimmainen kayttajan syotettavan kentan indeksi
     */
    public int ekaKentta() {
    	return 2;
    }
    
    /**
     * @return kysymyksen kenttien lukumaara
     */
    public int getKenttia() {
    	return 5;
    }
    
    /**
     * @param k mink‰ kent‰n kysymys halutaan
     * @return valitun kent‰n kysymysteksti
     */
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "koehenkiloNro";
            case 2:
                return "kysymys";
            case 3:
                return "kysymysTyyppi";
            case 4:
                return "vastausVaihtoehdot";
            default:
                return "???";
        }
    }

    
    /**
     * @param k kentta jonka sisalta otetaan
     * @return kentan sisalto
     * TODO: testi
     */
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + id;
            case 1:
                return "" + koehenkiloNro;
            case 2:
                return kysymys;
            case 3:
                return kysymysTyyppi;
            case 4:
                return vastausVaihtoehdot;
            default:
                return "???";
        	}
        }
        
        /*
         * Asettaa arvot
         */
        public String aseta(int k, String s) {
            String st = s.trim();
            StringBuffer sb = new StringBuffer(st);
            switch (k) {
                case 0:
                    setId(Mjonot.erota(sb, '$', getId()));
                    return null;
                case 1:
                    koehenkiloNro = Mjonot.erota(sb, '$', koehenkiloNro);
                    return null;
                case 2:
                    kysymys = st;
                    return null;
                case 3:
                    try {
                        kysymysTyyppi = Mjonot.erotaEx(sb, 'ß', kysymysTyyppi);
                    } catch (NumberFormatException ex) {
                        return "Virhe ("+st+")";
                    }
                    return null;

                case 4:
                    try {
                        vastausVaihtoehdot = Mjonot.erotaEx(sb, 'ß', vastausVaihtoehdot);
                    } catch (NumberFormatException ex) {
                        return "virhe ("+st+")";
                    }
                    return null;
                    
                default:
                    return "V‰‰r‰ kent‰n indeksi";
            }
    }
    
// -------------------------------------------------------------
// -------------------------------------------------------------
     
    
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
