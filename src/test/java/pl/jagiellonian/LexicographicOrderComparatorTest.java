package pl.jagiellonian;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.jagiellonian.implementation.LexicographicOrderComparator;
import pl.jagiellonian.implementation.Variable;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.interfaces.MonomialComparator;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class LexicographicOrderComparatorTest {

    private MonomialComparator comparator = new LexicographicOrderComparator();

    @Test
    @Parameters(method = "getExpressions")
    public void comparatorTest(String expr1, String expr2, boolean expectedResult) {
        // given
        IVariable variable1 = new Variable(expr1);
        IVariable variable2 = new Variable(expr2);

        // when
        boolean result = comparator.compare(variable1, variable2);

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
                {"x^*3*y^2*z^3", "z^3*y^2*x^3", true},
                {"x*y*x", "x*x*y", true},

                // not equals
                {"x", "y", false},
                {"x*y", "x*z", false},
                {"x*y*z", "x^2*y*z", false},

                // not sure :)
                {"x*x", "x^2", false},
                {"x*(y*z)", "x*y*z", false},
        };
    }
}