package pl.jagiellonian.Models;

import pl.jagiellonian.Parsers.ExpressionParser.ParsedExpression;
import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;
import java.util.stream.Collectors;

import static pl.jagiellonian.Parsers.ExpressionParser.isMultipleExpression;
import static pl.jagiellonian.Parsers.ExpressionParser.parseExpression;

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

    public void replaceExpression(String expressionToReplace, String replacementExpression) {
        if (isMultipleExpression(expressionToReplace) || isMultipleExpression(replacementExpression)) {
            throw new WrongFormatException();
        }
        ParsedExpression parsedExpressionToReplace = parseExpression(expressionToReplace);
        ParsedExpression parsedReplacementExpression = parseExpression(replacementExpression);
        Set<Integer> indexesToReplace = parsedExpressionToReplace.getVariables();
        Set<Integer> indexesReplacement = parsedReplacementExpression.getVariables();

        Map<List<Integer>, Integer> afterReplacement = new HashMap<>();
        VariablePowers:
        for (List<Integer> variablePowers : polynomialMap.keySet()) {
            if (parsedExpressionToReplace.getConstant().isPresent() && parsedExpressionToReplace.getConstant().get() != polynomialMap.get(variablePowers)) {
                if (afterReplacement.get(variablePowers) != null) {
                    int existingConstant = afterReplacement.get(variablePowers);
                    afterReplacement.put(variablePowers, polynomialMap.get(variablePowers) + existingConstant);
                } else {
                    afterReplacement.put(variablePowers, polynomialMap.get(variablePowers));
                }
                continue;
            }
            int size = variablePowers.size();
            for (Integer index : indexesToReplace) {
                if (index > size || variablePowers.get(index - 1) != parsedExpressionToReplace.getVariablePower(index)) {
                    if (afterReplacement.get(variablePowers) != null) {
                        int existingConstant = afterReplacement.get(variablePowers);
                        afterReplacement.put(variablePowers, polynomialMap.get(variablePowers) + existingConstant);
                    } else {
                        afterReplacement.put(variablePowers, polynomialMap.get(variablePowers));
                    }
                    continue VariablePowers;
                }
            }
            int startingConstant = polynomialMap.get(variablePowers);
            size = findMaximumVariableIndex(size, parsedReplacementExpression.getVariables());
            List<Integer> powers = new ArrayList<>(Collections.nCopies(size, 0));
            for (int i = 0; i < variablePowers.size(); i++) {
                if (!indexesToReplace.contains(i + 1)) {
                    powers.set(i, variablePowers.get(i));
                }
            }
            for (Integer index : indexesReplacement) {
                powers.set(index - 1, powers.get(index - 1) + parsedReplacementExpression.getVariablePower(index));
            }
            int constant = parsedReplacementExpression.getConstant().isPresent() ?
                    parsedExpressionToReplace.getConstant().isPresent() ?
                            parsedReplacementExpression.getConstant().get() :
                            startingConstant * parsedReplacementExpression.getConstant().get() :
                    startingConstant;
            if (afterReplacement.get(powers) != null) {
                int existingConstant = afterReplacement.get(powers);
                afterReplacement.put(powers, existingConstant + constant);
            } else {
                afterReplacement.put(powers, constant);
            }
        }
        setPolynomialMap(afterReplacement);
    }

    private int findMaximumVariableIndex(int size, Set<Integer> variables) {
        OptionalInt max = variables.parallelStream().mapToInt(value -> value).max();
        return max.isPresent() ? max.getAsInt() > size ? max.getAsInt() : size : size;
    }

    @Override
    public String toString() {
        //TODO
        return polynomialMap.toString();
    }
}
