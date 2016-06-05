package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.Models.PolynomialAsMap;
import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by lukaszrzepka on 24.05.2016.
 */
public class PolynomialAsMapTest {
    @Test
    public void multiplePolynomialsTest() {
        Integer polynomialCoefficient1 = 1;
        Integer polynomialCoefficient2 = 1;


        // -(b c^2) + 4 (a c^3) - 3 c^2
        Map<List<Integer>, Integer> polynomial1 = new HashMap<>();
        polynomial1.put(new ArrayList<>(Arrays.asList(0, 1, 2)), -1);
        polynomial1.put(new ArrayList<>(Arrays.asList(1, 0, 3)), 4);
        polynomial1.put(new ArrayList<>(Arrays.asList(0, 0, 2)), -3);

        PolynomialAsMap polynomialMap1 = new PolynomialAsMap(polynomial1);


        // 3 (b c) - a^2 b^2
        Map<List<Integer>, Integer> polynomial2 = new HashMap<>();
        polynomial2.put(new ArrayList<>(Arrays.asList(0, 1, 1)), 3);
        polynomial2.put(new ArrayList<>(Arrays.asList(2, 2, 0)), -1);


        // (-(b c^2) + 4 (a c^3) - 3 c^2) * (3 (b c) - a^2 b^2)
        PolynomialAsMap polynomialAsMap2 = polynomialMap1.multipleWith(new PolynomialAsMap(polynomial2));
        assertEquals("{[0, 2, 3]=-3, [2, 3, 2]=1, [0, 1, 3]=-9, [2, 2, 2]=3, [1, 1, 4]=12, [3, 2, 3]=-4}", polynomialAsMap2.toString());
        System.out.println(polynomialAsMap2);
        //-4 a^3 b^2 c^3   +   a^2 b^3 c^2   +   3 a^2 b^2 c^2   +   12 a b c^4   -   3 b^2 c^3   -   9 b c^3
    }

    @Test
    public void substituteTest() {
        //-3*x_1^5*x_2^1*x_3^2*x_4^0 + 9*x_1^1*x_2^4*x_3^0*x_4^2 + 2*x_1^0*x_2^0*x_3^0*x_4^0 + 1*x_1^-4*x_2^5*x_3^0*x_4^0
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(5, 1, 2)), -3);
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 4, 0, 2)), 3);
        polynomialMap.put(new ArrayList<>(Collections.emptyList()), 2);
        polynomialMap.put(new ArrayList<>(Arrays.asList(-4, 5, 0)), 1);

        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialMap);
        //{[5, 1, 2]=-3, [-4, 5, 0]=1, [1, 4, 0, 2]=3, []=2}
        assertEquals(4, polynomialAsMap.getPolynomialMap().size());
        polynomialAsMap.substitute("x_1^5", "-2x_2^-1x_3^0");
        //{[0, 0, 2]=6, [-4, 5, 0]=1, [1, 4, 0, 2]=3, []=2}
        polynomialAsMap.substitute("x_1^-4x_2^5", "x_3^2");
        //{[0, 0, 2]=7, [1, 4, 0, 2]=3, []=2}
        polynomialAsMap.substitute("x_1^1x_2^4", "-2x_4^2");
        //{[0, 0, 2]=7, [0, 0, 0, 4]=-6, []=2}
        polynomialAsMap.substitute("x_3^2", "1");
        //{[0, 0, 0, 4]=-6, []=9}
        polynomialAsMap.substitute("-6x_4^4", "-1");
        //{[]=8}
        polynomialAsMap.substitute("8", "1x_4^0");
        //{[]=1}
        assertEquals(1, polynomialAsMap.getPolynomialMap().size());
        assertEquals(0, polynomialAsMap.getPolynomialMap().keySet().iterator().next().size());
        assertEquals(1, polynomialAsMap.getPolynomialMap().get(new ArrayList<>(Collections.emptyList())).intValue());
    }

    @Test
    public void substituteComplexTest() {
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(5, 1, 2)), -3);
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 4, 0, 2)), 3);
        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialMap);
        //{[5, 1, 2]=-3, [1, 4, 0, 2]=3}
        polynomialAsMap.substitute("x_2^2", "2x_4^3");
        //{[5, 1, 2]=-3, [1, 0, 0, 14]=48}
    }

    @Test(expected = WrongFormatException.class)
    public void substituteInvalidExpressionTest(){
        String expression ="2x_2+b+c+9x_7";
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialMap);
        polynomialAsMap.substitute(expression, expression);
    }

    @Test
    public void findVariablesTest() {
        List<String> variables = new PolynomialAsMap(null).findVariables("5x_3^1x_1x_876^56");
        assertEquals(3, variables.size());
        assertEquals("x_3", variables.get(0));
        assertEquals("x_1", variables.get(1));
        assertEquals("x_876", variables.get(2));
    }

    @Test
    public void degreeValidVariableTest() {
        // 3x_2x_3^2+(-1)x_1^2x_3^4+(-1)x_1^3x_2^2
        Map<List<Integer>, Integer> map = new HashMap<>();
        map.put(new ArrayList<>(Arrays.asList(0, 1, 2)), 3);
        map.put(new ArrayList<>(Arrays.asList(2, 0, 4)), -1);
        map.put(new ArrayList<>(Arrays.asList(3, 2, 0)), -1);

        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(map);
        assertEquals(3, polynomialAsMap.degree("x_1"));
        assertEquals(2, polynomialAsMap.degree("x_2"));
        assertEquals(4, polynomialAsMap.degree("x_3"));
        assertEquals(0, polynomialAsMap.degree("x_5"));
        assertEquals(0, polynomialAsMap.degree("x_91234"));
        assertEquals(0, polynomialAsMap.degree(new ArrayList<>()));
        assertEquals(5, polynomialAsMap.degree(new ArrayList<>(Arrays.asList("x_1", "x_2"))));
        assertEquals(4, polynomialAsMap.degree(new ArrayList<>(Arrays.asList("x_2", "x_3"))));
        assertEquals(6, polynomialAsMap.degree(new ArrayList<>(Arrays.asList("x_1", "x_3"))));
        assertEquals(6, polynomialAsMap.degree(new ArrayList<>(Arrays.asList("x_1", "x_2", "x_3"))));
    }

    @Test(expected = WrongFormatException.class)
    public void degreeLetterVariableTest() {
        //x_2x_3^2
        Map<List<Integer>, Integer> map = new HashMap<>();
        map.put(new ArrayList<>(Arrays.asList(0, 1, 2)), 3);

        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(map);
        polynomialAsMap.degree("a");
    }

    @Test(expected = WrongFormatException.class)
    public void degreeDigitAsVariableTest() {
        //x_2x_3^2
        Map<List<Integer>, Integer> map = new HashMap<>();
        map.put(new ArrayList<>(Arrays.asList(5, 4, 2, 9)), -1);

        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(map);
        polynomialAsMap.degree(new ArrayList<>(Arrays.asList("x_2", "5", "x_3")));
    }
}