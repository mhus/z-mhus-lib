package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.util.MapValue;
import de.mhus.lib.core.operation.util.SuccessfulForceMap;
import de.mhus.lib.tests.TestCase;

public class OperationTest extends TestCase {

    @Test
    @SuppressWarnings("deprecation")
    public void testForceMapResult1() {
        SuccessfulForceMap res = new SuccessfulForceMap("path", "msg", 0);
        res.put("key", "value");
        System.out.println(res);
        assertTrue(res.getResult() instanceof MapValue);
        assertNotNull( ((MapValue)res.getResult()).getValue() );
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testForceMapResult2() {
        Operation oper = new TestOperation();
        SuccessfulForceMap res = new SuccessfulForceMap(oper, "msg");
        res.put("key", "value");
        System.out.println(res);
        assertTrue(res.getResult() instanceof MapValue);
        assertNotNull( ((MapValue)res.getResult()).getValue() );
    }

}
