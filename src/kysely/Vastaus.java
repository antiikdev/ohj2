package kysely;
/**
 * HUOM! Vastaukset- ja Vastaus-luokat EIVAT OLE KAYTOSSA!
 * Tata luokkaa kaytettiin HT1-6 vaiheissa tayttamaan parityon vaatimukset
 * kahdesta tietorakenteesta.
 * Ohjelma toteuttaa samat toiminnallisuudet HT7-vaihessa Kysymykset-Kysymys-luokilla.
 */
/**
 * Vastaus-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 * @version 3 Nov 2021
 *
 */

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Vastaus-luokka
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
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
        this.vastausVaihtoehdot = "A) Kissa, B) Tiikeri"; //t√§t√§ tietoa ei varmaan tarvii
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
     public String getVastaus(int k) {
         switch (k) {
             case 0:
                 return "id";
             case 1:
                 return "koehenkiloNro";
             case 2:
                 return "vastaus";
             case 3:
                 return "vastausTyyppi";
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
                 return vastaus;
             case 3:
                 return vastausTyyppi;
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
                     vastaus = st;
                     return null;
                 case 3:
                     try {
                         vastausTyyppi = Mjonot.erotaEx(sb, 'ß', vastausTyyppi);
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
