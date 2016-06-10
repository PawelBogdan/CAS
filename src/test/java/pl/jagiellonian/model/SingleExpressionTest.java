package pl.jagiellonian.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SingleExpressionTest {
    private SingleExpression singleExpression;

    @Before
    public void init(){
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("example"), -5);
        singleExpression = new SingleExpression(powers, 3);
    }

    @Test
    public void testGetPowers() throws Exception {
        assertEquals(-5, singleExpression.getPowers().getPower(new VariableInMap("example")).intValue());
    }

    @Test
    public void testIsConstantPresent() throws Exception {
        SingleExpressionPowers powers = new SingleExpressionPowers();
        SingleExpression expression = new SingleExpression(powers);
        assertFalse(expression.isConstantPresent());
        assertTrue(singleExpression.isConstantPresent());
    }

    @Test
    public void testGetConstant() throws Exception {
        SingleExpressionPowers powers = new SingleExpressionPowers();
        SingleExpression expression = new SingleExpression(powers);
        assertEquals(1., expression.getConstant(), 0);
        assertEquals(3., singleExpression.getConstant(), 0);
    }

    @Test
    public void testEquals() throws Exception {
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("example"), -5);
        SingleExpression expression = new SingleExpression(powers, 3);
        assertTrue(expression.equals(singleExpression));
        assertTrue(singleExpression.equals(expression));
    }

    @Test
    public void testHashCode() throws Exception {
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("example"), -5);
        assertNotEquals(new SingleExpression(powers).hashCode(), singleExpression.hashCode());
        assertEquals(new SingleExpression(powers, 3).hashCode(), singleExpression.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("3.0*example^-5", singleExpression.toString());
    }
}