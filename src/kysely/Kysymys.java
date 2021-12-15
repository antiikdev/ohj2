/**
 * Kysymys-luokka
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kysymys-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 *
 */
public class Kysymys {
    private int id;
    private int koehenkiloNro;
    private String kysymys;
    private String vastausVaihtoehdot;
    private String vastaus;
    
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
     * Taytto tyhjille kysymystiedoille  
     * @param numero kysymyksen
     */
    public void taytaEsimKysymysTiedot(int numero) {
        this.koehenkiloNro = numero;
        this.kysymys = "Syota kysymys";
        this.vastausVaihtoehdot = "Syota vastausvaihtoehdot";
        this.vastaus = "Syota vastaus";
    }
    
    
    /**
     * Tulostetaan kysymyksen tiedot
     * @param out tietovirta johon tulostetaan
     * @throws TallennaException jos poikkeus
     */
    public void tulosta(PrintStream out) throws TallennaException  {
        out.println(id + "|" + koehenkiloNro + "|"  + kysymys + "|" +
                    vastausVaihtoehdot + "|" + vastaus);
    }
    
    
    /**
     * Tulostetaan kysymyksen tiedot
     * @throws TallennaException jos poikkeus
     * @return tulostus kysymyksesta
     */
    public String tulosta() throws TallennaException  {
        String tulos = id + "|" + koehenkiloNro + "|"  + kysymys + "|" +
                    vastausVaihtoehdot + "|" + vastaus;
        return tulos;
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
     * @return koehenkiloNro palautus
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
     * Kysymys-luokan otsikot per atribuutti tyyppi
     * @param k minkï¿½ kentï¿½n kysymys halutaan
     * @return valitun kentï¿½n kysymysteksti
     */
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "koehenkiloNro";
            case 2:
                return "Kysymys";
            case 3:
                return "Vastausvaihtoehdot";
            case 4:
                return "Vastaus";
            default:
                return "???";
        }
    }

    
    /**
     * @param k kentta jonka sisalta otetaan
     * @return kentan sisalto
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
                return vastausVaihtoehdot;
            case 4:
                return vastaus;
            default:
                return "???";
        	}
        }
        
        /**
         * Asettaa arvot
         * @param k mita muokataan
         * @param s mita pistetaan
         * @return tyhjaa tai virheen
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
                        vastausVaihtoehdot = Mjonot.erotaEx(sb, '§', vastausVaihtoehdot);
                    } catch (NumberFormatException ex) {
                        return "Virhe ("+st+")";
                    }
                    return null;

                case 4:
                    try {
                        vastaus = Mjonot.erotaEx(sb, '§', vastaus);
                    } catch (NumberFormatException ex) {
                        return "virhe ("+st+")";
                    }
                    return null;
                    
                default:
                    return "Vaara kentan indeksi";
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
    * <pre name="test">
    * Kysymys kys = new Kysymys();
    * kys.parse("1|2|kissako|a) joo b) ei|a) joo");
    * String tulos = kys.toString();
    * tulos === "1|2|kissako|a) joo b) ei|a) joo";
    * </pre>
    */
   @Override
   public String toString() {
       return "" +
               getId() + "|" +
               getKoehenkiloNro() + "|" +
               kysymys + "|" +
               vastausVaihtoehdot + "|" +
               vastaus;
   }
   
   
   /**
    * Kysymyksen tiedot selvitetaan,
    * erotellaan |-merkilla merkkijonosta.
    * Huolehtii, etta seuraavaNro on suurempi kuin tuleva id-numero
    * @param rivi merkkijonorivi joka luetaan
    * <pre name="test">
    * Kysymys kys = new Kysymys();
    * kys.parse("1|2|kissako|a) joo b) ei|a) joo");
    * kys.anna(2) === "kissako";
    * kys.anna(4) === "a) joo";
    * </pre>
    */
   public void parse(String rivi) {
       StringBuilder sb = new StringBuilder(rivi);
       setId(Mjonot.erota(sb, '|', getId()));
       setKoehenkiloNro(Mjonot.erota(sb, '|', getKoehenkiloNro()));
       kysymys = Mjonot.erota(sb, '|', kysymys);
       vastausVaihtoehdot = Mjonot.erota(sb, '|', vastausVaihtoehdot);
       vastaus = Mjonot.erota(sb, '|', vastaus);
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
