package pl.jagiellonian.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private Map<List<Integer>, Integer> polynomialMap;

    public PolynomialAsMap(Map<List<Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
    }

    public Map<List<Integer>, Integer> getPolynomialMap() {
        return polynomialMap;
    }

    public void setPolynomialMap(Map<List<Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
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
        StringBuilder polynomialStringBuilder = new StringBuilder();

        for (Map.Entry<List<Integer>, Integer> monomial : this.polynomialMap.entrySet()) {
            List<Integer> monomialPowers = monomial.getKey();
            Integer monomialCoefficient = monomial.getValue();

            if (monomialCoefficient == 1 || monomialCoefficient == -1) {
                if (monomialCoefficient > 0) {
                    polynomialStringBuilder.append("+");
                } else {
                    polynomialStringBuilder.append("-");
                }
            } else if (monomialCoefficient != 0) {
                if (monomialCoefficient > 0) {
                    polynomialStringBuilder.append("+");
                }
                polynomialStringBuilder.append(monomialCoefficient).append("*");
            }

            for (int i = 0; i < monomialPowers.size(); i++) {
                if (monomialPowers.get(i) == 1) {
                    if (i + 1 == monomialPowers.size()) {
                        polynomialStringBuilder.append("x_").append(i + 1);
                    } else {
                        polynomialStringBuilder.append("x_").append(i+1).append("*");
                    }
                } else if (monomialPowers.get(i) != 0) {
                    if (i + 1 == monomialPowers.size()) {
                        polynomialStringBuilder.append("x_").append(i+1).append("^").append(monomialPowers.get(i));
                    } else {
                        polynomialStringBuilder.append("x_").append(i+1).append("^").append(monomialPowers.get(i)).append("*");
                    }
                }
            }
        }

        if (polynomialStringBuilder.charAt(0) == '+') {
            polynomialStringBuilder.deleteCharAt(0);
        }
        if (polynomialStringBuilder.charAt(polynomialStringBuilder.length() - 1) == '*') {
            polynomialStringBuilder.deleteCharAt(polynomialStringBuilder.length() - 1);
        }

        return polynomialStringBuilder.toString();
    }
}
