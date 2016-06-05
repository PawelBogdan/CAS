package pl.jagiellonian.Models;

import pl.jagiellonian.utils.MonomialOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private Map<List<Integer>, Integer> polynomialMap;
    private MonomialOrder monomialOrder = MonomialOrder.GRADED_LEXICOGRAPHIC;

    public PolynomialAsMap(Map<List<Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
    }

    public Map<List<Integer>, Integer> getPolynomialMap() {
        return polynomialMap;
    }

    public void setPolynomialMap(Map<List<Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
    }

    /**
     * Sets the monomial order to be applied in {@link #toString()} method.
     * Default is {@link MonomialOrder#GRADED_LEXICOGRAPHIC}.
     */
    public void setMonomialOrder(MonomialOrder monomialOrder) {
        this.monomialOrder = monomialOrder;
    }

    public PolynomialAsMap multipleWith(PolynomialAsMap myPolynomial) {
        Map<List<Integer>, Integer> polynomialMap = myPolynomial.getPolynomialMap();
        Map<List<Integer>, Integer> polynomialResult = new HashMap<>();

        for (Map.Entry<List<Integer>, Integer> polynomial : polynomialMap.entrySet()) {
            List<Integer> monomialPowers = polynomial.getKey();
            Integer monomialCoefficient = polynomial.getValue();

            for (Map.Entry<List<Integer>, Integer> thisPolynomial : this.polynomialMap.entrySet()) {
                List<Integer> thisMonomialPowers = thisPolynomial.getKey();
                Integer thisMonomialCoefficient = thisPolynomial.getValue();

                List<Integer> monomialPowersResult = new ArrayList<>();

                for (int i = 0; i < thisMonomialPowers.size(); i++) {
                    monomialPowersResult.add(thisMonomialPowers.get(i) + monomialPowers.get(i));
                }

                Integer monomialResultCoefficient = thisMonomialCoefficient * monomialCoefficient;
                polynomialResult.put(monomialPowersResult, monomialResultCoefficient);
            }
        }
        PolynomialAsMap polynomialAsMap = new PolynomialAsMap(polynomialResult);
        return polynomialAsMap;
    }

    @Override
    public String toString() {
        switch (monomialOrder) {
            case LEXICOGRAPHIC:
                return "";

            case GRADED_LEXICOGRAPHIC:
            default:
                return "";
        }
    }
}
