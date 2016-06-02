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
