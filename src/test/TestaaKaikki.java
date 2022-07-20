/**
 * 
 */
package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite, kaikki testaukset, kysely-ohjelmalle
 * @author Antiik & Doomslizer
 * @version 3 Nov 2021
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
    // kysely.test.KyselyTest.class, // error?
    // kysely.test.KoehenkilotTest.class, // error?
    kysely.test.KoehenkiloTest.class,
    kysely.test.KysymyksetTest.class,
    kysely.test.KysymysTest.class,
    kysely.test.VastauksetTest.class,
    kysely.test.VastausTest.class

})
    
    public class TestaaKaikki {
        //
}
