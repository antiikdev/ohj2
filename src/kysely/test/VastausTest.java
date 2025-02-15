package kysely.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import kysely.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2021.12.14 21:25:50 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class VastausTest {



  // Generated by ComTest BEGIN
  /** testRekisteroi92 */
  @Test
  public void testRekisteroi92() {    // Vastaus: 92
    Vastaus v1 = new Vastaus(); 
    assertEquals("From: Vastaus line: 94", 0, v1.getId()); 
    v1.rekisteroi(); 
    Vastaus v2 = new Vastaus(); 
    v2.rekisteroi(); 
    int n1 = v1.getId(); 
    int n2 = v2.getId(); 
    assertEquals("From: Vastaus line: 100", n2-1, n1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnna170 */
  @Test
  public void testAnna170() {    // Vastaus: 170
    Vastaus vas = new Vastaus(); 
    vas.parse("1|1|kissa|monivalinta|a) kissa b) pissa"); 
    assertEquals("From: Vastaus line: 173", "1", vas.anna(0)); 
    assertEquals("From: Vastaus line: 174", "1", vas.anna(1)); 
    assertEquals("From: Vastaus line: 175", "kissa", vas.anna(2)); 
    assertEquals("From: Vastaus line: 176", "monivalinta", vas.anna(3)); 
    assertEquals("From: Vastaus line: 177", "a) kissa b) pissa", vas.anna(4)); 
  } // Generated by ComTest END
}