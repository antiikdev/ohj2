package kysely.test;
// Generated by ComTest BEGIN
import kysely.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import java.io.File;
import java.util.Iterator;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2021.12.14 17:52:40 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class KoehenkilotTest {



  // Generated by ComTest BEGIN
  /** 
   * testLisaa48 
   * @throws TallennaException when error
   */
  @Test
  public void testLisaa48() throws TallennaException {    // Koehenkilot: 48
    Koehenkilot koeapinat = new Koehenkilot(); 
    Koehenkilo cheeta = new Koehenkilo(), apina = new Koehenkilo(); 
    assertEquals("From: Koehenkilot line: 52", 0, koeapinat.getLkm()); 
    koeapinat.lisaa(cheeta); assertEquals("From: Koehenkilot line: 53", 1, koeapinat.getLkm()); 
    koeapinat.lisaa(apina); assertEquals("From: Koehenkilot line: 54", 2, koeapinat.getLkm()); 
    assertEquals("From: Koehenkilot line: 55", cheeta, koeapinat.anna(0)); 
    assertEquals("From: Koehenkilot line: 56", apina, koeapinat.anna(1)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta175 
   * @throws TallennaException when error
   */
  @Test
  public void testLueTiedostosta175() throws TallennaException {    // Koehenkilot: 175
    Koehenkilot kaniinit = new Koehenkilot(); 
    Koehenkilo kani = new Koehenkilo(), pupu = new Koehenkilo(); 
    kani.taytaEsimTiedot(); 
    pupu.taytaEsimTiedot(); 
    kaniinit.lueTiedostosta(); 
    kaniinit.lisaa(kani); 
    kaniinit.lisaa(pupu); 
    kaniinit.tallenna(); 
    kaniinit = new Koehenkilot();  // poistetaan aikaisemmat
    kaniinit.lueTiedostosta(); 
    Iterator<Koehenkilo> iteroi = kaniinit.iterator(); 
    assertEquals("From: Koehenkilot line: 191", kani, iteroi.next()); 
    assertEquals("From: Koehenkilot line: 192", pupu, iteroi.next()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testKoehenkilotIterator245 
   * @throws TallennaException when error
   */
  @Test
  public void testKoehenkilotIterator245() throws TallennaException {    // Koehenkilot: 245
    Koehenkilot koehenkilot = new Koehenkilot(); 
    Koehenkilo kissa1 = new Koehenkilo(), kissa2 = new Koehenkilo(); 
    kissa1.rekisteroi(); kissa2.rekisteroi(); 
    koehenkilot.lisaa(kissa1); 
    koehenkilot.lisaa(kissa2); 
    koehenkilot.lisaa(kissa1); 
    StringBuffer ids = new StringBuffer(30); 
    for (Koehenkilo koehenkilo:koehenkilot)
    ids.append(" "+koehenkilo.getKoehenkiloNro()); 
    String tulos = " " + kissa1.getKoehenkiloNro() + " " + kissa2.getKoehenkiloNro() + " " + kissa1.getKoehenkiloNro(); 
    assertEquals("From: Koehenkilot line: 264", tulos, ids.toString()); 
    Iterator<Koehenkilo> i=koehenkilot.iterator(); 
    assertEquals("From: Koehenkilot line: 267", true, i.next() == kissa1); 
    assertEquals("From: Koehenkilot line: 268", true, i.next() == kissa2); 
    assertEquals("From: Koehenkilot line: 269", true, i.next() == kissa1); 
    try {
    i.next(); 
    fail("Koehenkilot: 271 Did not throw NoSuchElementException");
    } catch(NoSuchElementException _e_){ _e_.getMessage(); }
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testEtsi324 
   * @throws TallennaException when error
   */
  @Test
  public void testEtsi324() throws TallennaException {    // Koehenkilot: 324
    Koehenkilot koehenkilot = new Koehenkilot(); 
    Koehenkilo kissa1 = new Koehenkilo(); kissa1.parse("1|k100|m|11-15"); 
    Koehenkilo kissa2 = new Koehenkilo(); kissa2.parse("2|k101|f|15-21"); 
    Koehenkilo kissa3 = new Koehenkilo(); kissa3.parse("3|k102|m|5-11"); 
    Koehenkilo kissa4 = new Koehenkilo(); kissa4.parse("4|k103|m|15-21"); 
    Koehenkilo kissa5 = new Koehenkilo(); kissa5.parse("5|k104|f|5-11"); 
    koehenkilot.lisaa(kissa1); koehenkilot.lisaa(kissa2); koehenkilot.lisaa(kissa3); koehenkilot.lisaa(kissa4); koehenkilot.lisaa(kissa5); 
    List<Koehenkilo> loytyneet; 
    loytyneet = (List<Koehenkilo>)koehenkilot.etsi("f",2); 
    assertEquals("From: Koehenkilot line: 335", 2, loytyneet.size()); 
    assertEquals("From: Koehenkilot line: 336", true, loytyneet.get(0) == kissa2); 
    assertEquals("From: Koehenkilot line: 337", true, loytyneet.get(1) == kissa5); 
    loytyneet = (List<Koehenkilo>)koehenkilot.etsi("*4*",1); 
    assertEquals("From: Koehenkilot line: 340", 1, loytyneet.size()); 
    assertEquals("From: Koehenkilot line: 341", true, loytyneet.get(0) == kissa5); 
    loytyneet = (List<Koehenkilo>)koehenkilot.etsi("*11*",3); 
    assertEquals("From: Koehenkilot line: 344", 3, loytyneet.size()); 
    assertEquals("From: Koehenkilot line: 345", true, loytyneet.get(0) == kissa1); 
    assertEquals("From: Koehenkilot line: 346", true, loytyneet.get(1) == kissa3); 
    assertEquals("From: Koehenkilot line: 347", true, loytyneet.get(2) == kissa5); 
    loytyneet = (List<Koehenkilo>)koehenkilot.etsi(null,-1); 
    assertEquals("From: Koehenkilot line: 350", 5, loytyneet.size()); 
  } // Generated by ComTest END
}