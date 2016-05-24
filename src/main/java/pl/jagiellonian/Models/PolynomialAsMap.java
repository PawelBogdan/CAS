package pl.jagiellonian.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private Map<Map<List<Integer>, Integer>, Integer> polynomialMap;

    public PolynomialAsMap(Map<Map<List<Integer>, Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
    }

    public Map<Map<List<Integer>, Integer>, Integer> getPolynomialMap() {
        return polynomialMap;
    }

    public void setPolynomialMap(Map<Map<List<Integer>, Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
    }

    public PolynomialAsMap multipleWith(Map<Map<List<Integer>, Integer>, Integer> polynomialMap) {
        Map<Map<List<Integer>, Integer>, Integer> polynomialsResult = new HashMap<>();
        Map<List<Integer>, Integer> monomialResult = new HashMap<>();

        for (Map.Entry<Map<List<Integer>, Integer>, Integer> polynomials : polynomialMap.entrySet()) {
            Map<List<Integer>, Integer> polynomial = polynomials.getKey();
            Integer polynomialCoefficient = polynomials.getValue();

            for (Map.Entry<List<Integer>, Integer> monomial : polynomial.entrySet()) {
                List<Integer> monomialPowers = monomial.getKey();
                Integer monomialCoefficient = monomial.getValue();

                for (Map.Entry<Map<List<Integer>, Integer>, Integer> thisPolynomialMap : this.polynomialMap.entrySet()) {
                    Map<List<Integer>, Integer> thisPolynomial = thisPolynomialMap.getKey();
                    Integer thisPolynomialCoefficient = thisPolynomialMap.getValue();

                    for (Map.Entry<List<Integer>, Integer> thisMonomial : thisPolynomial.entrySet()) {
                        List<Integer> thisMonomialPowers = thisMonomial.getKey();
                        Integer thisMonomialCoefficient = thisMonomial.getValue();

                        List<Integer> monomialPowersResult = new ArrayList<>();

                        for (int i = 0; i < thisMonomialPowers.size(); i++) {
                            monomialPowersResult.add(thisMonomialPowers.get(i) + monomialPowers.get(i));
                        }

                        Integer monomialResultCoefficient = thisMonomialCoefficient * monomialCoefficient;
                        monomialResult.put(monomialPowersResult, monomialResultCoefficient);

                    }
                }
            }
        }
        Integer polynomialResultCoefficient = 1;
        polynomialsResult.put(monomialResult, polynomialResultCoefficient);

        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialsResult);

        return polynomialAsMap;
    }

    @Override
    public String toString() {
        //TODO
        return polynomialMap.toString();
    }
}
