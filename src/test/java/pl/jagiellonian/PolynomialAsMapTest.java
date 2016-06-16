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
        assertEquals("-3*x_2^2*x_3^3+x_1^2*x_2^3*x_3^2-9*x_2*x_3^3+3*x_1^2*x_2^2*x_3^2+12*x_1*x_2*x_3^4-4*x_1^3*x_2^2*x_3^3", polynomialAsMap2.toString());
        System.out.println(polynomialAsMap2);
        //-4 a^3 b^2 c^3   +   a^2 b^3 c^2   +   3 a^2 b^2 c^2   +   12 a b c^4   -   3 b^2 c^3   -   9 b c^3
    }

    @Test
    public void substituteComplexTest() {
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(5, 1, 2)), -3);
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 4, 0, 2)), 3);
        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialMap);
        //{[5, 1, 2]=-3, [1, 4, 0, 2]=3}

        Map<List<Integer>, Integer> polynomialToReplace = new HashMap<>();
        polynomialToReplace.put(new ArrayList<>(Arrays.asList(3, 1, 2)), -2);
        PolynomialAsMap polynomialAsMapToReplace = new PolynomialAsMap(polynomialToReplace);
        //{[3, 1, 2]=-3}

        Map<List<Integer>, Integer> polynomialReplacement = new HashMap<>();
        polynomialReplacement.put(new ArrayList<>(Arrays.asList(5, 3, -1, 4)), 2);
        PolynomialAsMap polynomialAsMapReplacement = new PolynomialAsMap(polynomialReplacement);
        //{[5, 3, -1, 4]=2}

        polynomialAsMap.substitute(polynomialAsMapToReplace, polynomialAsMapReplacement);
        Map<List<Integer>, Integer> polynomialTest = polynomialAsMap.getPolynomialMap();
        assertEquals(2, polynomialTest.size());
        List<Integer> first = new ArrayList<>(Arrays.asList(1, 4, 0, 2));
        List<Integer> second = new ArrayList<>(Arrays.asList(7, 3, -1, 4));
        assertEquals(3, polynomialTest.get(first).intValue());
        assertEquals(-2, polynomialTest.get(second).intValue());
        Iterator iterator = polynomialTest.keySet().iterator();
        assertEquals(first, iterator.next());
        assertEquals(second, iterator.next());
    }

    @Test
    public void substituteWithZeroTest() {
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 4)), 3);
        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialMap);

        Map<List<Integer>, Integer> polynomialToReplace = new HashMap<>();
        polynomialToReplace.put(new ArrayList<>(Arrays.asList(1, 2)), 3);
        PolynomialAsMap polynomialAsMapToReplace = new PolynomialAsMap(polynomialToReplace);

        Map<List<Integer>, Integer> polynomialReplacement = new HashMap<>();
        polynomialReplacement.put(new ArrayList<>(Arrays.asList(5, 3, -1, 4)), 2);
        PolynomialAsMap polynomialAsMapReplacement = new PolynomialAsMap(polynomialReplacement);
        //{[5, 3, -1, 4]=2}

        polynomialAsMap.substitute(polynomialAsMapToReplace, polynomialAsMapReplacement);
        Map<List<Integer>, Integer> resultMap = new HashMap<>();
        assertEquals(resultMap, polynomialAsMap.getPolynomialMap());
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

    @Test
    public void toStringTest() {
        // -(b c^2) + 4 (a c^3) - 3 c^2
        Map<List<Integer>, Integer> polynomial1 = new HashMap<>();
        polynomial1.put(new ArrayList<>(Arrays.asList(0, 1, 2)), -1);
        polynomial1.put(new ArrayList<>(Arrays.asList(1, 0, 3)), 4);
        polynomial1.put(new ArrayList<>(Arrays.asList(0, 0, 2)), -3);

        PolynomialAsMap polynomialMap1 = new PolynomialAsMap(polynomial1);
        assertEquals("-x_2*x_3^2-3*x_3^2+4*x_1*x_3^3", polynomialMap1.toString());
        System.out.println(polynomialMap1.toString());

        // 3 (b c) - a^2 b^2
        Map<List<Integer>, Integer> polynomial2 = new HashMap<>();
        polynomial2.put(new ArrayList<>(Arrays.asList(0, 1, 1)), 3);
        polynomial2.put(new ArrayList<>(Arrays.asList(2, 2, 0)), -1);

        PolynomialAsMap polynomialMap2 = new PolynomialAsMap(polynomial2);
        assertEquals("3*x_2*x_3-x_1^2*x_2^2", polynomialMap2.toString());
        System.out.println(polynomialMap2.toString());
    }
}
