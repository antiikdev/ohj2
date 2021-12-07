package kysely;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille
 * @author Antiikdev (ilkka.a.kotilainen@gmail.com)
 * @author Doomslizer (topi.val.kari@student.jyu.fi)
 * @version 22 Oct 2021
 *
 */
public class TallennaException extends Exception {
    
    // Tama Javassa koska: lahettajan ja vastaanottajan paassa
    // pitaa olla tieto luokan versionumeron vertailusta verkon yli
    private static final long serialVersionUID = 1L;
    
    
    
    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeukessa
     * kaytettava viesti
     * @param viesti poikkeuksien viesti
     */
    public TallennaException(String viesti) {
        super(viesti);
    }
    
}
