package pl.jagiellonian.Models;

import pl.jagiellonian.exceptions.WrongFormatException;
import pl.jagiellonian.utils.MonomialOrder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private static final Pattern VARIABLE = Pattern.compile("x_([1-9][0-9]*)");
    private static final Comparator<List<Integer>> lexicographicComparator = (o1, o2) -> {
        int temp;
        for (int i = 0; i < o1.size(); i++) {
            temp = Integer.compare(o1.get(i), o2.get(i));
            if (temp != 0) return -temp;
        }
        return 0;
    };
    private static final Comparator<List<Integer>> gradedComparator = (o1, o2) -> {
        int temp = Integer.compare(o1.stream().mapToInt(Integer::intValue).sum(), o2.stream().mapToInt(Integer::intValue).sum());
        if (temp != 0) return -temp;
        for (int i = 0; i < o1.size(); i++) {
            temp = Integer.compare(o1.get(i), o2.get(i));
            if (temp != 0) return -temp;
        }
        return 0;
    };
    private Map<List<Integer>, Integer> polynomialMap;
    private MonomialOrder monomialOrder;

    public PolynomialAsMap(Map<List<Integer>, Integer> polynomialMap) {
        this.polynomialMap = polynomialMap;
        this.monomialOrder = MonomialOrder.GRADED_LEXICOGRAPHIC;
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

    public int degree(String variable) {
        Matcher matcher = VARIABLE.matcher(variable);
        if (matcher.matches()) {
            int index = Integer.parseInt(matcher.group(1)) - 1;
            int max = polynomialMap.keySet().parallelStream()
                    .filter(degreeList -> index < degreeList.size())
                    .mapToInt(degreeList -> degreeList.get(index))
                    .max().orElse(0);
            return max;
        }
        throw new WrongFormatException();
    }

    public int degree(List<String> variables) {
        List<Integer> indexes = new ArrayList<>();
        for (String variable : variables) {
            Matcher matcher = VARIABLE.matcher(variable);
            if (matcher.matches()) {
                indexes.add(Integer.parseInt(matcher.group(1)) - 1);
            } else {
                throw new WrongFormatException();
            }
        }

        int max = polynomialMap.keySet().parallelStream()
                .mapToInt(degreeList ->
                        indexes.parallelStream()
                                .filter(index -> index < degreeList.size() && degreeList.get(index) > 0)
                                .mapToInt(degreeList::get)
                                .sum())
                .max().orElse(0);
        return max;
    }

    @Override
    public String toString() {
        String output = "";
        switch (monomialOrder) {
            case LEXICOGRAPHIC:
                output = polynomialMap.entrySet().stream()
                        .sorted((o1, o2) -> lexicographicComparator.compare(o1.getKey(), o2.getKey()))
                        .map(PolynomialAsMap::getMonomialFromEntry)
                        .collect(Collectors.joining(" "));
                break;
            case GRADED_LEXICOGRAPHIC:
                output = polynomialMap.entrySet().stream()
                        .sorted((o1, o2) -> gradedComparator.compare(o1.getKey(), o2.getKey()))
                        .map(PolynomialAsMap::getMonomialFromEntry)
                        .collect(Collectors.joining(" "));
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
        } else if (entry.getValue() < 0) {
            builder.append("- ");
            if (!skipValue) builder.append(Math.abs(entry.getValue()));
        } else return "";

        for (int i = 0; i < entry.getKey().size(); i++) {
            if (entry.getKey().get(i) != 0) {
                if (!skipValue) builder.append("*");
                builder.append("x_" + (i + 1));
                if (entry.getKey().get(i) != 1) {
                    builder.append("^" + entry.getKey().get(i));
                }
                skipValue = false;
            }
        }
        return builder.toString();
    }
}
