package pl.jagiellonian.Parsers;

import org.junit.Test;
import pl.jagiellonian.Parsers.ExpressionParser.ParsedExpression;

import static org.junit.Assert.*;

public class ExpressionParserTest {

    @Test
    public void testParseExpression() throws Exception {
        String expression = "(-8)x_4x_1^65x_4";
        ParsedExpression parsedExpression = ExpressionParser.parseExpression(expression);
        assertEquals(-8, parsedExpression.getConstant().get().intValue());
        assertEquals(2, parsedExpression.getVariables().size());
        assertEquals(65, parsedExpression.getVariablePower(1));
        assertEquals(2, parsedExpression.getVariablePower(4));
        parsedExpression.putVariable(1, 7);
        assertEquals(7, parsedExpression.getVariablePower(1));
    }

    @Test
    public void testIsMultipleExpression() throws Exception {
        String expression;
        expression = "(-8)";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "-8";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "8";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "(-8)x_4x_1^65x_4";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "(-8)*x_4x_1^65x_4";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "x_4x_1^65x_4";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "x_4*x_1^65*x_4";
        assertTrue(ExpressionParser.isSingleExpression(expression));
        expression = "x_4+x_1^65*x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "(-8)+x_4x_1^65x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "+8x_4x_1^65x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "+8x_4 x_1^65x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "+8x_4x_1^6 5x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "+8x_4x_1^65/x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "+8x_4x_1^65sx_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "+8x_4xx_1^65x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "*x_1^65x_4";
        assertFalse(ExpressionParser.isSingleExpression(expression));
        expression = "";
        assertFalse(ExpressionParser.isSingleExpression(expression));
    }
}