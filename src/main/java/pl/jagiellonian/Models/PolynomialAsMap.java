package pl.jagiellonian.Models;

import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private static final Pattern VARIABLE = Pattern.compile("x_([1-9][0-9]*)");

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

    public int degree(String variable) {
        Matcher matcher = VARIABLE.matcher(variable);
        if (matcher.matches()) {
            int index = Integer.parseInt(matcher.group(1)) - 1;
            OptionalInt max = polynomialMap.keySet().parallelStream()
                    .filter(integers -> index < integers.size() && integers.get(index) > 0)
                    .mapToInt(integers -> integers.parallelStream().mapToInt(value -> value).sum())
                    .max();
            if (max.isPresent()) {
                return max.getAsInt();
            }
        }
        throw new WrongFormatException();
    }

    public int degree(List<String> variables) {
        OptionalInt max = variables.parallelStream().mapToInt(this::degree).max();
        return max.isPresent() ? max.getAsInt() : 0;
    }

    @Override
    public String toString() {
        //TODO
        return polynomialMap.toString();
    }
}
