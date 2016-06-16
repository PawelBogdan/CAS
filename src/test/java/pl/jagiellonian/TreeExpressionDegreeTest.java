package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.implementation.TreeExpression;
import pl.jagiellonian.implementation.Variable;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static pl.jagiellonian.implementation.TreeExpression.parse;

public class TreeExpressionDegreeTest {

    @Test
    public void oneVariableSimpleTest() {
        String exp = "x^2+1";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(2, tree.degree(new Variable("x")));
    }

    @Test
    public void oneVariableParenthesisTest() {
        String exp = "(x^2+1)^2";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(4, tree.degree(new Variable("x")));
    }

    @Test
    public void oneVariableAndConstantsTest() {
        String exp = "x^2*y^3+x*y";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(2, tree.degree(new Variable("x")));
    }

    @Test
    public void oneVariableMultipleTimesTest() {
        String exp = "x*x*x";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(3, tree.degree(new Variable("x")));
    }

    @Test
    public void variablesListSimpleTest() {
        String exp = "x^2*y^3+x*y";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(5, tree.degree(Arrays.asList(new Variable("x"), new Variable("y"))));
    }

    @Test
    public void variablesListParenthesisTest() {
        String exp = "(x^2*y^3+x*y)^2";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(10, tree.degree(Arrays.asList(new Variable("x"), new Variable("y"))));
    }

    @Test
    public void variablesListMultipleTimesTest() {
        String exp = "x*y*x*y";
        TreeExpression tree = (TreeExpression)parse(exp);
        assertEquals(4, tree.degree(Arrays.asList(new Variable("x"), new Variable("y"))));
    }

}
