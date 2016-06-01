package pl.jagiellonian;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.jagiellonian.exceptions.WrongDifferentiationVariable;
import pl.jagiellonian.implementation.DerivativeCalculator;
import pl.jagiellonian.implementation.LexicographicOrderSorter;
import pl.jagiellonian.implementation.TreeExpression;
import pl.jagiellonian.interfaces.IVariable;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class DerivativeCalculatorTest {

    private DerivativeCalculator derivativeCalculator = new DerivativeCalculator(new LexicographicOrderSorter());

    @Test(expected = WrongDifferentiationVariable.class)
    @Parameters({"2", "x^2"})
    public void differentiationWithWrongVariableTest(String variable) {
        IVariable monomial = TreeExpression.parse("x*y");
        derivativeCalculator.differentiate(monomial, TreeExpression.parse(variable));
    }

    @Test
    @Parameters(method = "getParameters")
    public void differentiationTest(String monomialName, String variableName, String expectedResult) throws Exception {
        // given
        IVariable monomial = TreeExpression.parse(monomialName);
        IVariable variable = TreeExpression.parse(variableName);

        // when
        IVariable result = derivativeCalculator.differentiate(monomial, variable);

        // then
        assertEquals(expectedResult, result.toString());
    }

    @SuppressWarnings("unused")
    private Object getParameters() {
        return new Object[][]{
                {"x", "y", "0"},
                {"x^2", "y", "0"},
                {"x", "x", "1"},
                {"x*y", "x", "y"},
                {"x^2", "x", "2*x"},
                {"a^3*a*a^2", "a", "6*a^5"},
                {"x^3*a^2", "a", "2*a*x^3"},
                {"x^3*a^4", "a", "4*a^3*x^3"},
        };
    }
}