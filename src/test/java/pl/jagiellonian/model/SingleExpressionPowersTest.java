package pl.jagiellonian.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

public class SingleExpressionPowersTest {
    private SingleExpressionPowers first;
    private SingleExpressionPowers second;
    private SingleExpressionPowers third;

    @Before
    public void init() {
        first = new SingleExpressionPowers();
        first.putPower(new VariableInMap("first"), 3);
        first.putPower(new VariableInMap("second"), 5);
        second = new SingleExpressionPowers();
        second.putPower(new VariableInMap("first"), -4);
        second.putPower(new VariableInMap("second"), 7);
        second.putPower(new VariableInMap("third"), 2);
        third = new SingleExpressionPowers();
        third.putPower(new VariableInMap("second"), 5);
        third.putPower(new VariableInMap("first"), 3);
    }

    @Test
    public void testGetPower() throws Exception {
        assertEquals(3, first.getPower(new VariableInMap("first")).intValue());
        assertEquals(2, second.getPower(new VariableInMap("third")).intValue());
        assertEquals(-4, second.getPower(new VariableInMap("first")).intValue());
    }

    @Test
    public void testGetVariables() throws Exception {
        Set<Map.Entry<VariableInMap, Integer>> powersSet = second.getPowersSet();
        assertEquals(3, powersSet.size());
        Set<VariableInMap> set = second.getVariables();
        assertEquals(3, set.size());
        Set<VariableInMap> variables = new HashSet<>();
        variables.add(new VariableInMap("third"));
        variables.add(new VariableInMap("first"));
        variables.add(new VariableInMap("second"));
        for (VariableInMap variableInMap : set) {
            assertTrue(variables.contains(variableInMap));
        }
        for (Map.Entry<VariableInMap, Integer> variableInMap : powersSet) {
            assertTrue(variables.contains(variableInMap.getKey()));
        }
    }

    @Test
    public void testPutAndAddPower() throws Exception {
        VariableInMap v = new VariableInMap("first");
        assertEquals(3, first.getPower(v).intValue());
        first.addPower(v, 12);
        assertEquals(15, first.getPower(v).intValue());
        first.putPower(v, 3);
        assertEquals(3, first.getPower(v).intValue());
    }

    @Test
    public void testEquals() throws Exception {
        assertTrue(first.equals(third));
        assertTrue(third.equals(first));
        assertFalse(first.equals(second));
        assertFalse(second.equals(third));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(first.hashCode(), third.hashCode());
        assertNotEquals(second.hashCode(), third.hashCode());
        assertNotEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("first^3*second^5", first.toString());
        assertEquals("third^2*first^-4*second^7", second.toString());
        assertEquals("first^3*second^5", third.toString());
    }
}