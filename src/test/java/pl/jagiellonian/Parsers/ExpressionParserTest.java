package pl.jagiellonian.Parsers;

import org.junit.Test;
import pl.jagiellonian.Parsers.ExpressionParser.ParsedExpression;

import static org.junit.Assert.*;

public class ExpressionParserTest {

    @Test
    public void testParseExpression() throws Exception {
        String expression = "(-8)x_4x_1^65x_4";
        ParsedExpression parsedExpression = ExpressionParser.parseExpression(expression);
        assertEquals(-8, parsedExpression.getConstant());
        assertEquals(2, parsedExpression.getVariables().size());
        assertEquals(65, parsedExpression.getVariablePower(1));
        assertEquals(2, parsedExpression.getVariablePower(4));
    }
}