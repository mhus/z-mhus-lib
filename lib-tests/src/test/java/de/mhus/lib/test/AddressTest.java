package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.util.Address;
import de.mhus.lib.core.util.Address.SALUTATION;
import de.mhus.lib.tests.TestCase;

public class AddressTest extends TestCase {

    @Test
    public void testSalutationDe() {
        {
            SALUTATION sal = Address.toSalutation("Herr");
            System.out.println(sal);
            assertEquals(SALUTATION.MR, sal);
            String txt = Address.toSalutationString(sal, Locale.GERMAN);
            System.out.println(txt);
            assertEquals("Herr", txt);
        }
        {
            SALUTATION sal = Address.toSalutation("frau");
            System.out.println(sal);
            assertEquals(SALUTATION.MRS, sal);
            String txt = Address.toSalutationString(sal, Locale.GERMAN);
            System.out.println(txt);
            assertEquals("Frau", txt);
        }
        
    }
    
    
}
