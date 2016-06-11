package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.Models.PolynomialAsMap;
import pl.jagiellonian.utils.MonomialOrder;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by szymon_k on 2016-06-10.
 */
public class MapMonomialOrderTest {

    @Test
    public void lexicographicOrder() {
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 3, 2)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(3, 2, 1)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(1, 2, 3)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(2, 1, 1)), 1);

        PolynomialAsMap polynomial = new PolynomialAsMap(polynomialMap);
        polynomial.setMonomialOrder(MonomialOrder.LEXICOGRAPHIC);
        String output = polynomial.toString();
        assertEquals("x_1^3*x_2^2*x_3 + x_1^2*x_2*x_3 + x_1*x_2^3*x_3^2 + x_1*x_2^2*x_3^3", output);
    }

    @Test
    public void gradedLexicographicOrder() {
        Map<List<Integer>, Integer> polynomialMap = new HashMap<>();
        polynomialMap.put(new ArrayList<>(Arrays.asList(2, 1, 0)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(2, 0, 1)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(3, 2, 1)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(3, 1, 3)), 1);
        polynomialMap.put(new ArrayList<>(Arrays.asList(2, 0, 2)), 1);

        PolynomialAsMap polynomial = new PolynomialAsMap(polynomialMap);
        polynomial.setMonomialOrder(MonomialOrder.GRADED_LEXICOGRAPHIC);
        String output = polynomial.toString();
        assertEquals("x_1^3*x_2*x_3^3 + x_1^3*x_2^2*x_3 + x_1^2*x_3^2 + x_1^2*x_2 + x_1^2*x_3", output);
    }
}
