package pl.jagiellonian.Models;

import pl.jagiellonian.utils.MonomialOrder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private Map<List<Integer>, Integer> polynomialMap;
    private MonomialOrder monomialOrder;

    public PolynomialAsMap(Map<List<Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
        this.monomialOrder = MonomialOrder.LEXICOGRAPHIC;
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
        final Comparator<List<Integer>> exponentsComparator = new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int temp;
                for (int i = 0; i < o1.size(); i++) {
                    temp = Integer.compare(o1.get(i), o2.get(i));
                    if (temp != 0) return -temp;
                }
                return 0;
            }
        };

        String output = "";
        switch (monomialOrder) {
            case LEXICOGRAPHIC:
                output = polynomialMap.entrySet().stream()
                            .sorted((o1, o2) -> exponentsComparator.compare(o1.getKey(), o2.getKey()))
                            .map(PolynomialAsMap::getMonomialFromEntry)
                            .collect(Collectors.joining(" "));
                break;
            case GRADED_LEXICOGRAPHIC:

                break;
        }
        return output.startsWith("+ ") ? output.substring(2) : output;
    }

    private static String getMonomialFromEntry(Map.Entry<List<Integer>, Integer> entry) {
        final StringBuilder builder = new StringBuilder();

        boolean skipValue = Math.abs(entry.getValue()) == 1;
        if (entry.getValue() > 0) {
            builder.append("+ ");
            if (!skipValue) builder.append(entry.getValue());
        }
        else if (entry.getValue() < 0) {
            builder.append("- ");
            if (!skipValue) builder.append(Math.abs(entry.getValue()));
        }
        else return "";

        for (int i = 0; i < entry.getKey().size(); i++) {
            if (entry.getKey().get(i) != 0) {
                if (!skipValue) builder.append("*");
                builder.append("x_" + (i+1));
                if (entry.getKey().get(i) != 1) {
                    builder.append("^" + entry.getKey().get(i));
                }
                skipValue = false;
            }
        }
        return builder.toString();
    }
}
