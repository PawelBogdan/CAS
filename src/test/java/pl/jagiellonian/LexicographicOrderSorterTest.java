package pl.jagiellonian;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.jagiellonian.implementation.LexicographicOrderSorter;
import pl.jagiellonian.implementation.TreeExpression;
import pl.jagiellonian.interfaces.IVariable;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Luq on 2016-05-30.
 */
@RunWith(JUnitParamsRunner.class)
public class LexicographicOrderSorterTest {

    private final LexicographicOrderSorter lexicographicOrderSorter = new LexicographicOrderSorter();

    @Test
    @Parameters(method = "getExpressionBeforeAndAfterSorting")
    public void sort(String expression, String expectedSortedExpression) throws Exception {
        // given
        IVariable variable = TreeExpression.parse(expression);

        // when
        IVariable sortedVariable = lexicographicOrderSorter.sort(variable);

        // then
        assertEquals(expectedSortedExpression, sortedVariable.toString());
    }

    @SuppressWarnings("unused")
    private Object getExpressionBeforeAndAfterSorting() {
        return new Object[][]{
                {"x^2", "x^2"},
                {"(x^2)^3", "(x^2)^3"},
                {"z*y*a*x", "a*x*y*z"},
                {"a*x*y*z", "a*x*y*z"},
                {"z*y*x*a", "a*x*y*z"},
                {"z*(y*x)*a", "a*x*y*z"},
                {"a^3*a*a^2", "a^6"},
                {"x^3*a^2*x*a^2", "a^4*x^4"},
                {"y^2*x*a^2", "a^2*x*y^2"},
        };
    }
}