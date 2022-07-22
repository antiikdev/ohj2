/**
 * 
 */
package kysely;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Koehenkilo-luokka
 * @author Antiikdev
 * @author Doomslizer
 * @version 7 Dec 2021
 */
public class Koehenkilo {
    private int     koehenkiloNro;
    private String  nimi            = ""; // JOS halutaan nimeta koehenkiloita
    private String  sukupuoli       = ""; // alustetaan tyhjaksi, jotta ei NULL
    private String  ikaryhma        = "";
    
    private static int seuraavaNro    = 1;
    
    
// ------------------------------------------------------------------
// --------------- Java ja tietokannat - vaihe 6 --------------------
// ------------------------------------------------------------------
    
    /**
     * Testien toimintaa varten sisältöjen vertailuun
     */
    @Override
    public boolean equals(Object koehenkilo) {
        if (koehenkilo == null) return false;
        return this.toString().equals(koehenkilo.toString());
    }
    
    
    /**
     * Java vaatimus equals-metodille, koska Override
     */
    @Override
    public int hashCode() {
        return koehenkiloNro;
    }
    
    
    /**
     * Tietokannan luontilauseke koehenkilon taululle
     * @return koehenkilotaulun luontilauseke
     */
    public String annaLuontilauseke() {
        return "CREATE TABLE Koehenkilot (" +
                "koehenkiloNro INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "nimi VARCHAR(100) NOT NULL, " +
                "sukupuoli VARCHAR(10), " +
                "ikaryhma VARCHAR(100)" +
                  // "PRIMARY KEY (koehenkiloID)" + 
                  ")";
    }
    
    
    /**
     * Koehenkilon lisayslause
     * @param con tietokantayhteys
     * @return koehenkilon lisayslauseke
     * @throws SQLException jos lausekkeen luonnissa on ongelmia
     */
    public PreparedStatement annaLisayslauseke(Connection con) throws SQLException {
        PreparedStatement sql = con.prepareStatement("INSERT INTO Koehenkilot" +
                "(koehenkiloNro, nimi, sukupuoli, ikaryhma) " +
                "VALUES (?, ?, ?, ?)");
                       
                // Syötetään kentät välttääksemme SQL injektiot!
                if ( koehenkiloNro != 0 ) sql.setInt(1, koehenkiloNro); else sql.setString(1, null);
                sql.setString(2, nimi);
                sql.setString(3, sukupuoli);
                sql.setString(4, ikaryhma);
                
                return sql;
    }

    
    /**
     * Tarkistetaan onko koehenkiloNro muuttunut lisäyksessä
     * @param rs lisäyslauseen ResultSet
     * @throws SQLException jos tulee jotakin vikaa
     */
    public void tarkistaId(ResultSet rs) throws SQLException {
        if ( !rs.next() ) return;
        int id = rs.getInt(1);
        if ( id == koehenkiloNro ) return;
        setKoehenkiloNro(id);
    }

    
    /** 
    * Ottaa koehenkilon tiedot ResultSetistä
    * @param tulokset mistä tiedot otetaan
    * @throws SQLException jos jokin menee väärin
    */
    public void parse(ResultSet tulokset) throws SQLException {
        setKoehenkiloNro(tulokset.getInt("koehenkiloNro"));
        nimi = tulokset.getString("nimi");
        sukupuoli = tulokset.getString("sukupuoli");
        ikaryhma = tulokset.getString("ikaryhma");
    }


         
// ------------------------------------------------------------------
// ------------------------------------------------------------------
    
    
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
        ikaryhma = "18-25";
    }
    
    
    /**
     * 
     * Tulostetaan koehenkilon tiedot
     * @param out tietovirtaan tulostus
     */
    public void tulosta(PrintStream out) {
        out.print(koehenkiloNro + "|" + nimi + "|" + sukupuoli + "|" + ikaryhma);
        out.println();
    }
    
    /**
     * 
     * Tulostetaan koehenkilon tiedot
     * @return koehenkilon tiedot
     */
    public String tulosta() {
        String tiedot = koehenkiloNro + "|" + nimi + "|" + sukupuoli + "|" + ikaryhma;
        return tiedot;
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
    
    
// -------------------------------------------------------------
// ------------------------- HT7-vaihe -------------------------
// -------------------------------------------------------------
    
    /**
     * Palauttaa koehenkilon nimen
     * @return koehenkilon nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Palauttaa koehenkilon sukupuolen
     * @return koehenkilon sukupuoli
     */
	public String getSukupuoli() {
		return sukupuoli;
	}

   /**
     * Palauttaa koehenkilon ikaryhman
     * @return koehenkilon ikaryhma
     */
	public String getIkaryhma() {
		return ikaryhma;
	}
    
	/**
	 * Asettaa nimen
	 * @param s nimi
	 * @return null jos ok, "virhe" jos virhe
	 */
	public String setNimi(String s) {
		if ( s.equals("") ) return "Syota arvo!";
		nimi = s;
		return null;
	}

	/**
	 * Asettaa sukupuolen
	 * @param s sukupuoli
	 * @return null jos ok, "virhe" jos virhe
	 */
	public String setSukupuoli(String s) {	
		sukupuoli = s;
		if ( s.equals("") ) return "Syota arvo!";
		if ( !s.equals("m") || !s.equals("f") || !s.equals("e")) {
			return "Syota m, f tai e!";
		}
		return null;
	}

	/**
	 * Asettaa ikaryhman
	 * @param s ikäryhmä
	 * @return null jos ok, "virhe" jos virhe
	 */
	public String setIkaryhma(String s) {
		if ( s.equals("") ) return "Syota arvo!";
		ikaryhma = s;
		return null;
	}
	

// -------------------------------------------------------------
// -------------------------------------------------------------

	
 // -------------------------------------------------------------
 // ------------------------- HT6-vaihe -------------------------
 // -------------------------------------------------------------
    /**
     * Asettaa koehenkilolle numeron ja varmistaa etta
     * seuraava numero on aina suurempi kuin tahan mennessa suurin.
     * @param nr asetettava koehenkilonumero
     */
    private void setKoehenkiloNro(int nr) {
        koehenkiloNro = nr;
        if (koehenkiloNro >= seuraavaNro) seuraavaNro = koehenkiloNro + 1; 
    }
    
    
    /**
     * Tulostetaan merkkijonona
     * @example
     * <pre name="test">
     *   Koehenkilo koehenkilo = new Koehenkilo();
     *   koehenkilo.parse("1|k100|f|15-21");
     *   koehenkilo.toString().startsWith("1|k100|f|15-21") === true;
     * </pre>  

     */
    @Override
    public String toString() {
        return "" +
                getKoehenkiloNro() + "|" +
                nimi + "|" +
                sukupuoli + "|" +
                ikaryhma;
    }
    
    
    /**
     * Koehenkilon tiedot selvitetaan,
     * erotellaan |-merkilla merkkijonosta.
     * Huolehtii, etta seuraavaNro on suurempi kuin tuleva koehenkiloNro
     * @param rivi merkkijonorivi joka luetaan
     * @example
     * <pre name="test">
     *   Koehenkilo koehenkilo = new Koehenkilo();
     *   koehenkilo.parse("3|k100|f|15-21");
     *   koehenkilo.getKoehenkiloNro() === 3;
     *   koehenkilo.toString().startsWith("3|k100|f|15-21") === true;
     *
     *   koehenkilo.rekisteroi();
     *   int n = koehenkilo.getKoehenkiloNro();
     *   koehenkilo.parse(""+(n+20));
     *   koehenkilo.rekisteroi();
     *   koehenkilo.getKoehenkiloNro() === n+20+1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setKoehenkiloNro(Mjonot.erota(sb, '|', getKoehenkiloNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        sukupuoli = Mjonot.erota(sb, '|', sukupuoli);
        ikaryhma = Mjonot.erota(sb, '|', ikaryhma);
    }
    
    
    //Haku
    /**
     * Antaa k:n kentän sisällön
     * @param k monesko kenttä
     * @return kentän sisällön
     */
    public String anna(int k) {
        switch (k) {
            case 0: return "koehenkiloNro";
            case 1: return "nimi";
            case 2: return "sukupuoli";
            case 3: return "ikaryhma";
            default: return "Tyhäm";
        }
    }
    
    
    /**
     * Eka kenttä joka on sopiva kysyttäväksi
     * @return ekan kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }



// -------------------------------------------------------------
// -------------------------------------------------------------
    
    
    /**
     * Paaohjelma testaukseen
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
