package pl.jagiellonian;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.jagiellonian.implementation.LexicographicOrderSorter;
import pl.jagiellonian.implementation.LexicographicOrderTester;
import pl.jagiellonian.implementation.TreeExpression;
import pl.jagiellonian.interfaces.IVariable;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class LexicographicOrderTesterTest {

    private LexicographicOrderTester tester = new LexicographicOrderTester(new LexicographicOrderSorter());

    @Test
    @Parameters(method = "getExpressions")
    public void comparatorTest(String expr1, String expr2, boolean expectedResult) {
        // given
        IVariable variable1 = TreeExpression.parse(expr1);
        IVariable variable2 = TreeExpression.parse(expr2);

        // when
        boolean result = tester.test(variable1, variable2);

        // then
        assertEquals(expectedResult, result);
    }

    @SuppressWarnings("unused")
    private Object getExpressions() {
        return new Object[][]{
                // equals
                {"x", "x", true},
                {"x_1*y", "y*x_1", true},
                {"q*w*e*r*t*y", "y*t*r*e*w*q", true},
                {"q*w*e*r*t*y", "w*y*q*r*e*t", true},
                {"x^3*y^2*z^3", "z^3*y^2*x^3", true},
                {"x*y*x", "x*x*y", true},
                {"2*y*1*x", "2*x*1*y", true},
                {"6*z^3*1*x^4", "6*x^4*1*z^3", true},
                {"x*x", "x^2", true},
                {"x*(y*z)", "x*y*z", true},

                // not equals
                {"x", "y", false},
                {"x*y", "x*z", false},
                {"x*y*z", "x^2*y*z", false},
                {"x", "x*y", false},
        };
    }
}