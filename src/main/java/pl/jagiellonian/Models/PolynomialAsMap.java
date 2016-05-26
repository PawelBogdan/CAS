package pl.jagiellonian.Models;

import pl.jagiellonian.Parsers.ExpressionParser.ParsedExpression;
import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;

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
        return new PolynomialAsMap(polynomialResult);
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
            int initialConstant = polynomialMap.get(variablePowers);
            if (parsedExpressionToReplace.getConstant().isPresent() && parsedExpressionToReplace.getConstant().get().equals(polynomialMap.get(variablePowers))) {
                insertExpressionIntoMap(afterReplacement, variablePowers, initialConstant);
                continue;
            }
            int size = variablePowers.size();
            for (Integer index : indexesToReplace) {
                if (index > size || variablePowers.get(index - 1) != parsedExpressionToReplace.getVariablePower(index)) {
                    insertExpressionIntoMap(afterReplacement, variablePowers, initialConstant);
                    continue VariablePowers;
                }
            }

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
                            initialConstant * parsedReplacementExpression.getConstant().get() :
                    initialConstant;
            insertExpressionIntoMap(afterReplacement, powers, constant);
        }
        setPolynomialMap(afterReplacement);
    }

    private void insertExpressionIntoMap(Map<List<Integer>, Integer> resultMap, List<Integer> powers, int constant) {
        if (resultMap.get(powers) != null) {
            int existingConstant = resultMap.get(powers);
            resultMap.put(powers, existingConstant + constant);
        } else {
            resultMap.put(powers, constant);
        }
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
