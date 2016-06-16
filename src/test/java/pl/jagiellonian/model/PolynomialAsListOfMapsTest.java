package pl.jagiellonian.model;

import org.junit.Before;
import org.junit.Test;
import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class PolynomialAsListOfMapsTest {

    private PolynomialAsListOfMaps polynomialAsListOfMaps;

    @Before
    public void init() {
        polynomialAsListOfMaps = createPolynomialAsListOfMaps();
    }

    @Test
    public void testGetExpressions() throws Exception {
        List<SingleExpression> polynomialExpressions = polynomialAsListOfMaps.getExpressions();
        List<SingleExpression> expressions = createPolynomialAsListOfMaps().getExpressions();
        assertEquals(expressions.size(), polynomialExpressions.size());
        for (SingleExpression expression : polynomialExpressions) {
            assertTrue(expressions.contains(expression));
        }
    }

    @Test
    public void testParse() throws Exception {
        String expression = "-2x^2+(-1)c^1d^-13+5*g*c+abc";
        PolynomialAsListOfMaps parse = PolynomialAsListOfMaps.parse(expression);
        assertEquals(4, parse.size());
        assertEquals("1.0*abc^1+-1.0*c^1*d^-13+5.0*c^1*g^1+-2.0*x^2", parse.toString());
        Set<String> set = new HashSet<>();
        set.add("-1.0*c^1*d^-13");
        set.add("5.0*c^1*g^1");
        set.add("-2.0*x^2");
        set.add("1.0*abc^1");
        for (SingleExpression singleExpression : parse.getExpressions()) {
            assertTrue(set.contains(singleExpression.toString()));
        }
    }

    @Test(expected = WrongFormatException.class)
    public void testParseEmpty() throws Exception {
        String expression = "";
        PolynomialAsListOfMaps.parse(expression);
    }

    @Test(expected = WrongFormatException.class)
    public void testParseInvalid() throws Exception {
        String expression = "x*c+3/x^4";
        PolynomialAsListOfMaps.parse(expression);
    }

    @Test
    public void testAddToPolynomial() throws Exception {
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("example"), -5);
        expressions.add(new SingleExpression(powers, -3));
        PolynomialAsListOfMaps polynomial = new PolynomialAsListOfMaps(expressions);
        polynomial.add(polynomialAsListOfMaps);
        assertEquals(1, polynomial.size());
        assertEquals("-1.0*first^3*second^8", polynomial.toString());
    }

    @Test
    public void testAddTwoPolynomials() throws Exception {
        PolynomialAsListOfMaps added = PolynomialAsListOfMaps.add(polynomialAsListOfMaps, polynomialAsListOfMaps);
        assertEquals(2, added.size());
        assertEquals("-2.0*first^3*second^8+6.0*example^-5", added.toString());
    }

    @Test
    public void testMultiple() throws Exception {
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("example"), -5);
        expressions.add(new SingleExpression(powers, -3));
        PolynomialAsListOfMaps polynomial = new PolynomialAsListOfMaps(expressions);
        polynomial.multiple(polynomialAsListOfMaps);
        assertEquals(2, polynomial.size());
        assertEquals("3.0*first^3*example^-5*second^8+-9.0*example^-10", polynomial.toString());
    }

    @Test(expected = WrongFormatException.class)
    public void testDegreeWrongVariable() throws Exception {
        polynomialAsListOfMaps.degree("oth123+321er");
    }

    @Test(expected = WrongFormatException.class)
    public void testDegreeWrongVariable2() throws Exception {
        polynomialAsListOfMaps.degree("2first");
    }

    @Test(expected = WrongFormatException.class)
    public void testDegreeWrongVariable3() throws Exception {
        polynomialAsListOfMaps.degree(" ");
    }

    @Test
    public void testDegree() throws Exception {
        PolynomialAsListOfMaps polynomial = getAddedPolynomial();
        assertEquals(0, polynomial.degree("example"));
        assertEquals(4, polynomial.degree("first"));
        assertEquals(2, polynomial.degree("x"));
        assertEquals(8, polynomial.degree("second"));
        assertEquals(0, polynomial.degree("other"));
    }

    @Test(expected = WrongFormatException.class)
    public void testDegreeWithSetWrongVariable() throws Exception {
        polynomialAsListOfMaps.degree(new HashSet<>(Arrays.asList("2first", "first")));
    }

    @Test(expected = WrongFormatException.class)
    public void testDegreeWithSetWrongVariable2() throws Exception {
        polynomialAsListOfMaps.degree(new HashSet<>(Arrays.asList(" ", "first")));
    }

    @Test
    public void testDegreeWithSet() throws Exception {
        PolynomialAsListOfMaps polynomial = getAddedPolynomial();
        assertEquals(4, polynomial.degree(new HashSet<>(Arrays.asList("example", "first"))));
        assertEquals(11, polynomial.degree(new HashSet<>(Arrays.asList("second", "first"))));
        assertEquals(6, polynomial.degree(new HashSet<>(Arrays.asList("x", "first"))));
        assertEquals(2, polynomial.degree(new HashSet<>(Arrays.asList("x", "x"))));
        assertEquals(0, polynomial.degree(new HashSet<>(Arrays.asList("other", "other2"))));
    }

    @Test(expected = WrongFormatException.class)
    public void testSubstituteMultipleExpression() {
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        expressions.add(new SingleExpression(powers));
        expressions.add(new SingleExpression(powers));
        PolynomialAsListOfMaps toReplace = new PolynomialAsListOfMaps(expressions);
        polynomialAsListOfMaps.substitute(toReplace, polynomialAsListOfMaps);
    }

    @Test
    public void testSubstitute() throws Exception {
        PolynomialAsListOfMaps polynomial = getAddedPolynomial();
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("x"), 1);
        expressions.add(new SingleExpression(powers));
        PolynomialAsListOfMaps toReplace = new PolynomialAsListOfMaps(expressions);
        polynomial.substitute(toReplace, new PolynomialAsListOfMaps(createPolynomialAsListOfMaps().getExpressions()));
        assertEquals(5, polynomial.size());
        assertEquals(
                "-6.0*first^7*example^-5*second^8+1.0*first^10*second^16+-1.0*first^3*second^8+9.0*first^4*example^-10+3.0*example^-5",
                polynomial.toString());
    }

    @Test
    public void testSubstituteOnlyConstant() throws Exception {
        PolynomialAsListOfMaps polynomial = getAddedPolynomial();
        List<SingleExpression> expressions = new ArrayList<>();
        expressions.add(new SingleExpression(new SingleExpressionPowers(), 2));
        PolynomialAsListOfMaps toReplace = new PolynomialAsListOfMaps(expressions);
        polynomial.substitute(toReplace, new PolynomialAsListOfMaps(createPolynomialAsListOfMaps().getExpressions()));
        assertEquals(3, polynomial.size());
        assertEquals(
                "2.0*x^2*first^4+-1.0*first^3*second^8+3.0*example^-5",
                polynomial.toString());
    }

    @Test
    public void testSubstituteOnlyConstant2() throws Exception {
        PolynomialAsListOfMaps polynomialReplacement = getAddedPolynomial();
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("z"), 2);
        expressions.add(new SingleExpression(powers, 2.2));
        PolynomialAsListOfMaps polynomial = new PolynomialAsListOfMaps(expressions);
        List<SingleExpression> expression = new ArrayList<>();
        SingleExpressionPowers powersToReplace = new SingleExpressionPowers();
        powersToReplace.putPower(new VariableInMap("z"), 2);
        expression.add(new SingleExpression(powersToReplace));
        PolynomialAsListOfMaps toReplace = new PolynomialAsListOfMaps(expression);
        polynomial.substitute(toReplace, polynomialReplacement);
        assertEquals(3, polynomial.size());
        assertEquals("2.4*x^2*first^4+-1.2*first^3*second^8+3.6*example^-5", polynomial.toString());
    }

    @Test
    public void testSubstituteOnlyConstant3() throws Exception {
        PolynomialAsListOfMaps polynomialReplacement = getAddedPolynomial();
        List<SingleExpression> expressions = new ArrayList<>();
        expressions.add(new SingleExpression(new SingleExpressionPowers(), 2));
        PolynomialAsListOfMaps polynomial = new PolynomialAsListOfMaps(expressions);
        List<SingleExpression> expression = new ArrayList<>();
        expression.add(new SingleExpression(new SingleExpressionPowers(), 1));
        PolynomialAsListOfMaps toReplace = new PolynomialAsListOfMaps(expression);
        polynomial.substitute(toReplace, polynomialReplacement);
        assertEquals(6, polynomial.size());
        assertEquals("1.0*first^6*second^16+-6.0*first^3*second^8*example^-5+12.0*x^2*first^4*example^-5+9.0*example^-10+-4.0*x^2*first^7*second^8+4.0*x^4*first^8", polynomial.toString());
    }

    @Test
    public void testSubstituteOnlyConstant4() throws Exception {
        PolynomialAsListOfMaps polynomialReplacement = getAddedPolynomial();
        List<SingleExpression> expressions = new ArrayList<>();
        expressions.add(new SingleExpression(new SingleExpressionPowers(), 2.2));
        PolynomialAsListOfMaps polynomial = new PolynomialAsListOfMaps(expressions);
        List<SingleExpression> expression = new ArrayList<>();
        expression.add(new SingleExpression(new SingleExpressionPowers(), 1));
        PolynomialAsListOfMaps toReplace = new PolynomialAsListOfMaps(expression);
        polynomial.substitute(toReplace, polynomialReplacement);
        assertEquals(6, polynomial.size());
        assertEquals("0.2*first^6*second^16+-1.2*first^3*second^8*example^-5+2.4*x^2*first^4*example^-5+1.8*example^-10+-0.8*x^2*first^7*second^8+0.8*x^4*first^8", polynomial.toString());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("3.0*example^-5+-1.0*first^3*second^8", polynomialAsListOfMaps.toString());
    }

    private PolynomialAsListOfMaps getAddedPolynomial() {
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("x"), 2);
        powers.putPower(new VariableInMap("first"), 4);
        expressions.add(new SingleExpression(powers, 2));
        PolynomialAsListOfMaps polynomial = new PolynomialAsListOfMaps(expressions);
        polynomial.add(polynomialAsListOfMaps);
        assertEquals("2.0*x^2*first^4+-1.0*first^3*second^8+3.0*example^-5", polynomial.toString());
        return polynomial;
    }

    private PolynomialAsListOfMaps createPolynomialAsListOfMaps() {
        List<SingleExpression> expressions = new ArrayList<>();
        SingleExpressionPowers powers = new SingleExpressionPowers();
        powers.putPower(new VariableInMap("example"), -5);
        expressions.add(new SingleExpression(powers, 3));
        SingleExpressionPowers powers2 = new SingleExpressionPowers();
        powers2.putPower(new VariableInMap("first"), 3);
        powers2.putPower(new VariableInMap("second"), 8);
        expressions.add(new SingleExpression(powers2, -1));
        return new PolynomialAsListOfMaps(expressions);
    }
}