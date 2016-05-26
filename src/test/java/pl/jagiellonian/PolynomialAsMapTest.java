package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.Models.PolynomialAsMap;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void replaceExpressionTest() {
        //-3*x_1^5*x_2^1*x_3^2*x_4^0 + 9*x_1^1*x_2^4*x_3^0*x_4^2 + 2*x_1^0*x_2^0*x_3^0*x_4^0 + 1*x_1^-4*x_2^5*x_3^0*x_4^0
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(5, 1, 2)), -3);
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 4, 0, 2)), 3);
        polynomialMap.put(new ArrayList<>(Arrays.asList(0, 0, 0)), 2);
        polynomialMap.put(new ArrayList<>(Arrays.asList(-4, 5, 0)), 1);

        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialMap);
        //{[5, 1, 2]=-3, [-4, 5, 0]=1, [1, 4, 0, 2]=3, [0, 0, 0]=2}
        assertEquals(4, polynomialAsMap.getPolynomialMap().size());
        polynomialAsMap.replaceExpression("x_1^5", "-2x_2^-1x_3^0");
        //{[0, 0, 2]=6, [-4, 5, 0]=1, [1, 4, 0, 2]=3, [0, 0, 0]=2}
        polynomialAsMap.replaceExpression("x_1^-4x_2^5", "x_3^2");
        //{[0, 0, 2]=7, [1, 4, 0, 2]=3, [0, 0, 0]=2}
        polynomialAsMap.replaceExpression("x_1^1x_2^4", "-2x_4^2");
        //{[0, 0, 2]=7, [0, 0, 0, 4]=-6, [0, 0, 0]=2}
        polynomialAsMap.replaceExpression("x_3^2", "1");
        //{[0, 0, 0, 4]=-6, [0, 0, 0]=9}
        polynomialAsMap.replaceExpression("-6x_4^4", "-1");
        //{[0, 0, 0, 0]=-1, [0, 0, 0]=9}
        polynomialAsMap.replaceExpression("9", "1x_4^0");
        assertEquals(1, polynomialAsMap.getPolynomialMap().size());
        for (List<Integer> integers : polynomialAsMap.getPolynomialMap().keySet()) {
            for (Integer integer : integers) {
                assertEquals(0, integer.intValue());
            }
        }
        assertEquals(0, polynomialAsMap.getPolynomialMap().get(new ArrayList<>(Collections.nCopies(4, 0))).intValue());
    }
}
