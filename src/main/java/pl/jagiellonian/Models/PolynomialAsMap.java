package pl.jagiellonian.Models;

import pl.jagiellonian.Parsers.ExpressionParser.ParsedExpression;
import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.jagiellonian.Parsers.ExpressionParser.isSingleExpression;
import static pl.jagiellonian.Parsers.ExpressionParser.parseExpression;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private static final Pattern VARIABLE_NAME = Pattern.compile("(x_[1-9][0-9]*)");
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

    public void substitute(String expressionToReplace, String replacementExpressionString) {
        if (!isSingleExpression(expressionToReplace)) {
            throw new WrongFormatException();
        }
        ParsedExpression parsedExpressionToReplace = parseExpression(expressionToReplace);
        List<String> variables = findVariables(replacementExpressionString);

        ParsedExpression replacementExpression = parseExpression(replacementExpressionString);
        PolynomialAsMap replacement = parsedExpressionToPolynomialAsMap(replacementExpression);
        Set<Integer> indexesToReplace = parsedExpressionToReplace.getVariables();

        List<PolynomialAsMap> fragments = new ArrayList<>();
        VariablePowers:
        for (Map.Entry<List<Integer>, Integer> entry : polynomialMap.entrySet()) {
            int initialConstant = entry.getValue();
            int finalConstant = initialConstant;
            if (parsedExpressionToReplace.getConstant().isPresent()) {
                finalConstant = initialConstant / parsedExpressionToReplace.getConstant().get();
            }
            int size = entry.getKey().size();
            for (Integer index : indexesToReplace) {
                if (index > size || entry.getKey().get(index - 1) < parsedExpressionToReplace.getVariablePower(index)) {
                    fragments.add(createSinglePolynomial(entry.getKey(), initialConstant));
                    continue VariablePowers;
                }
            }

            Integer matchPower = getMaximumMatch(parsedExpressionToReplace, indexesToReplace, fragments, entry.getKey(), initialConstant);
            if (matchPower == null) {
                continue;
            }

            if (matchPower < Integer.MAX_VALUE) {
                PolynomialAsMap multipliedReplacement = multipleReplacement(replacement, matchPower);

                List<Integer> powers = getRemainingPowers(parsedExpressionToReplace, variables, indexesToReplace, entry.getKey(), size, matchPower, multipliedReplacement);

                Map<List<Integer>, Integer> map = new HashMap<>();
                map.put(powers, finalConstant);
                PolynomialAsMap multiplied = multipliedReplacement.multipleWith(new PolynomialAsMap(map));
                fragments.add(multiplied);
            } else if (parsedExpressionToReplace.getConstant().isPresent()) {
                fragments.add(createSinglePolynomial(entry.getKey(), finalConstant));
            }
        }

        PolynomialAsMap result = new PolynomialAsMap(new HashMap<>());
        for (PolynomialAsMap polynomialAsMap : fragments) {
            result = result.addPolynomial(polynomialAsMap);
        }
        setPolynomialMap(result.getPolynomialMap());
    }

    private List<Integer> getRemainingPowers(ParsedExpression parsedExpressionToReplace, List<String> variables, Set<Integer> indexesToReplace, List<Integer> variablePowers, int size, Integer matchPower, PolynomialAsMap multipliedReplacement) {
        int replacementDegree = multipliedReplacement.degree(variables);
        List<Integer> powers = Stream.generate(() -> 0).limit(size > replacementDegree ? size : replacementDegree).collect(Collectors.toList());
        for (int i = 0; i < variablePowers.size(); i++) {
            if (!indexesToReplace.contains(i + 1)) {
                powers.set(i, variablePowers.get(i));
            } else {
                powers.set(i, variablePowers.get(i) - matchPower * parsedExpressionToReplace.getVariablePower(i + 1));
            }
        }
        int end = powers.size();
        for (int i = end - 1; i >= 0; i--) {
            if (powers.get(i) == 0) {
                end--;
            } else {
                break;
            }
        }
        return powers.subList(0, end);
    }

    private PolynomialAsMap multipleReplacement(PolynomialAsMap replacement, Integer matchPower) {
        PolynomialAsMap multipliedReplacement = replacement;
        for (int i = 1; i < matchPower; i++) {
            multipliedReplacement = multipliedReplacement.multipleWith(replacement);
        }
        return multipliedReplacement;
    }

    private Integer getMaximumMatch(ParsedExpression parsedExpressionToReplace, Set<Integer> indexesToReplace, List<PolynomialAsMap> fragments, List<Integer> variablePowers, int initialConstant) {
        Integer matchPower = Integer.MAX_VALUE;
        for (int i = 0; i < variablePowers.size(); i++) {
            if (indexesToReplace.contains(i + 1)) {
                int power = variablePowers.get(i);
                int matchCount = power / parsedExpressionToReplace.getVariablePower(i + 1);
                if (matchCount == 0) {
                    fragments.add(createSinglePolynomial(variablePowers, initialConstant));
                    return null;
                }
                if (matchPower > matchCount) {
                    matchPower = matchCount;
                }
            }
        }
        return matchPower;
    }

    private PolynomialAsMap parsedExpressionToPolynomialAsMap(ParsedExpression replacementExpression) {
        Map<List<Integer>, Integer> map = new HashMap<>();
        int max = replacementExpression.getVariables().parallelStream().max(Integer::compareTo).orElse(0);
        List<Integer> powers = Stream.generate(() -> 0).limit(max).collect(Collectors.toList());
        for (Integer integer : replacementExpression.getVariables()) {
            powers.set(integer - 1, replacementExpression.getVariablePower(integer));
        }
        map.put(powers, replacementExpression.getConstant().orElse(1));
        return new PolynomialAsMap(map);
    }

    private PolynomialAsMap createSinglePolynomial(List<Integer> variablePowers, int constant) {
        Map<List<Integer>, Integer> map = new HashMap<>();
        map.put(variablePowers, constant);
        return new PolynomialAsMap(map);
    }

    public List<String> findVariables(String replacementExpression) {
        List<String> variables = new ArrayList<>();
        Matcher matcher = VARIABLE_NAME.matcher(replacementExpression);
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

    private int findMaximumVariableIndex(int size, Set<Integer> variables) {
        OptionalInt max = variables.parallelStream().mapToInt(value -> value).max();
        return max.isPresent() ? max.getAsInt() > size ? max.getAsInt() : size : size;
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
