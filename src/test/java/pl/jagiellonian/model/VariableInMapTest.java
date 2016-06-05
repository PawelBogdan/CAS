package pl.jagiellonian.model;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VariableInMapTest {
    private VariableInMap variableInMapFirst = new VariableInMap("first");
    private VariableInMap variableInMapSecond = new VariableInMap("second");
    private VariableInMap variableInMapThird = new VariableInMap("first");

    @Test
    public void testGetName() throws Exception {
        assertEquals("first", variableInMapFirst.getName());
    }

    @Test
    public void testEquals() throws Exception {
        assertFalse(variableInMapFirst.equals(variableInMapSecond));
        assertFalse(variableInMapSecond.equals(variableInMapFirst));
        assertTrue(variableInMapFirst.equals(variableInMapThird));
        assertTrue(variableInMapThird.equals(variableInMapFirst));
    }

    @Test
    public void testHashCode() throws Exception {
        assertNotEquals(variableInMapFirst.hashCode(), variableInMapSecond.hashCode());
        assertEquals(variableInMapFirst.hashCode(), variableInMapThird.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("first", variableInMapFirst.toString());
    }
}