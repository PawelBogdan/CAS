package pl.jagiellonian.Models;

import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                int i = 0;
                for (; i < thisMonomialPowers.size(); i++) {
                    if (monomialPowers.size() > i) {
                        monomialPowersResult.add(thisMonomialPowers.get(i) + monomialPowers.get(i));
                    } else {
                        monomialPowersResult.add(thisMonomialPowers.get(i));
                    }
                }
                for (; i < monomialPowers.size(); i++) {
                    monomialPowersResult.add(monomialPowers.get(i));
                }

                Integer monomialResultCoefficient = thisMonomialCoefficient * monomialCoefficient;
                polynomialResult.put(monomialPowersResult, monomialResultCoefficient);
            }
        }
        return new PolynomialAsMap(polynomialResult);
    }

    public PolynomialAsMap addPolynomial(PolynomialAsMap polynomialAsMap) {
        Map<List<Integer>, Integer> polynomialToAddMap = new HashMap<>(polynomialAsMap.getPolynomialMap());
        for (Map.Entry<List<Integer>, Integer> entry : polynomialMap.entrySet()) {
            if (polynomialToAddMap.get(entry.getKey()) != null) {
                polynomialToAddMap.put(entry.getKey(), polynomialToAddMap.get(entry.getKey()) + entry.getValue());
            } else {
                polynomialToAddMap.put(entry.getKey(), entry.getValue());
            }
        }
        return new PolynomialAsMap(polynomialToAddMap);
    }

    /**
     * Method for replacing single expression in polynomial.
     * @param polynomialToReplace Single expression to replace as PolynomialAsMap object
     * @param polynomialReplacement PolynomialAsMap object to insert instead of replaced expression
     * @throws WrongFormatException When PolynomialAsMap to replace is more then single expression
     */
    public void substitute(PolynomialAsMap polynomialToReplace, PolynomialAsMap polynomialReplacement) {
        if (polynomialToReplace.polynomialMap.size() != 1) {
            throw new WrongFormatException();
        }
        Map.Entry<List<Integer>, Integer> toReplaceEntry = polynomialToReplace.polynomialMap.entrySet().iterator().next();
        int maxIndexToReplace = toReplaceEntry.getKey().size();

        List<PolynomialAsMap> fragments = new ArrayList<>();
        VariablePowers:
        for (Map.Entry<List<Integer>, Integer> entry : polynomialMap.entrySet()) {

            int size = entry.getKey().size();
            if (maxIndexToReplace > size) {
                fragments.add(createSinglePolynomial(entry.getKey(), entry.getValue()));
                continue;
            }
            int matchPower = Integer.MAX_VALUE;
            for (int i = 0; i < maxIndexToReplace; i++) {
                int power = toReplaceEntry.getKey().get(i);
                if (power > entry.getKey().get(i)) {
                    fragments.add(createSinglePolynomial(entry.getKey(), entry.getValue()));
                    continue VariablePowers;
                }
                if (entry.getKey().get(i) / power < matchPower) {
                    matchPower = entry.getKey().get(i) / power;
                }
            }

            if (matchPower < Integer.MAX_VALUE) {
                PolynomialAsMap multipliedToReplace = multipleReplacement(polynomialToReplace, matchPower);
                int constantToReplace = entry.getValue() - multipliedToReplace.getPolynomialMap().values().iterator().next();
                if (constantToReplace == 0) {
                    continue;
                }

                PolynomialAsMap beforeMultiplying = createSinglePolynomial(subtractPowers(entry.getKey(), multipliedToReplace.getPolynomialMap().keySet().iterator().next()), constantToReplace);
                fragments.add(beforeMultiplying.multipleWith(polynomialReplacement));
            } else if (toReplaceEntry.getValue() != null) {
                fragments.add(createSinglePolynomial(entry.getKey(), entry.getValue() - toReplaceEntry.getValue()).multipleWith(polynomialReplacement));
            } else {
                fragments.add(createSinglePolynomial(entry.getKey(), entry.getValue()));
            }
        }
        PolynomialAsMap result = new PolynomialAsMap(new HashMap<>());
        for (PolynomialAsMap polynomialAsMap : fragments) {
            result = result.addPolynomial(polynomialAsMap);
        }
        setPolynomialMap(result.getPolynomialMap());
    }

    private List<Integer> subtractPowers(List<Integer> powers, List<Integer> toSubtract) {
        for (int i = 0; i < toSubtract.size(); i++) {
            powers.set(i, powers.get(i) - toSubtract.get(i));
        }
        return powers;
    }

    private PolynomialAsMap multipleReplacement(PolynomialAsMap replacement, Integer matchPower) {
        PolynomialAsMap multipliedReplacement = replacement;
        for (int i = 1; i < matchPower; i++) {
            multipliedReplacement = multipliedReplacement.multipleWith(replacement);
        }
        return multipliedReplacement;
    }

    private PolynomialAsMap createSinglePolynomial(List<Integer> variablePowers, int constant) {
        Map<List<Integer>, Integer> map = new HashMap<>();
        map.put(variablePowers, constant);
        return new PolynomialAsMap(map);
    }

    public int degree(String variable) {
        Matcher matcher = VARIABLE.matcher(variable);
        if (matcher.matches()) {
            int index = Integer.parseInt(matcher.group(1)) - 1;
            return polynomialMap.keySet().parallelStream()
                    .filter(degreeList -> index < degreeList.size())
                    .mapToInt(degreeList -> degreeList.get(index))
                    .max().orElse(0);
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

        return polynomialMap.keySet().parallelStream()
                .mapToInt(degreeList ->
                        indexes.parallelStream()
                                .filter(index -> index < degreeList.size() && degreeList.get(index) > 0)
                                .mapToInt(degreeList::get)
                                .sum())
                .max().orElse(0);
    }

    @Override
    public String toString() {
        //TODO
        return polynomialMap.toString();
    }
}
