package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.Models.PolynomialAsMap;

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


        //(-(b c^2) + 4 (a c^3) - 3 c^2)
        Map<List<Integer>, Integer> monomials = new HashMap<>();
        monomials.put(new ArrayList<>(Arrays.asList(0,1,2)), -1);
        monomials.put(new ArrayList<>(Arrays.asList(1,0,3)), 4);
        monomials.put(new ArrayList<>(Arrays.asList(0,0,2)), -3);

        Map<Map<List<Integer>, Integer>, Integer> polynomials = new HashMap<>();
        polynomials.put(monomials, polynomialCoefficient1);

        PolynomialAsMap polynomialMap1 = new PolynomialAsMap(polynomials);


        //(3 (b c) - a^2 b^2)
        Map<List<Integer>, Integer> monomials2 = new HashMap<>();
        monomials2.put(new ArrayList<>(Arrays.asList(0,1,1)), 3);
        monomials2.put(new ArrayList<>(Arrays.asList(2,2,0)), -1);

        Map<Map<List<Integer>, Integer>, Integer> polynomials2 = new HashMap<>();
        polynomials2.put(monomials2, polynomialCoefficient2);


        //(-(b c^2) + 4 (a c^3) - 3 c^2) * (3 (b c) - a^2 b^2)
        PolynomialAsMap polynomialAsMap2 = polynomialMap1.multipleWith(polynomials2);
        assertEquals("{{[0, 2, 3]=-3, [2, 3, 2]=1, [0, 1, 3]=-9, [2, 2, 2]=3, [1, 1, 4]=12, [3, 2, 3]=-4}=1}", polynomialAsMap2.toString());
        System.out.println(polynomialAsMap2);
        //-4 a^3 b^2 c^3   +   a^2 b^3 c^2   +   3 a^2 b^2 c^2   +   12 a b c^4   -   3 b^2 c^3   -   9 b c^3
    }
}
