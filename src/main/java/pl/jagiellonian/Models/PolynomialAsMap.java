package pl.jagiellonian.Models;

import com.google.common.annotations.VisibleForTesting;
import pl.jagiellonian.Parsers.ExpressionParser.ParsedExpression;
import pl.jagiellonian.Parsers.MonomialParser;
import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.jagiellonian.Parsers.ExpressionParser.isSingleExpression;
import static pl.jagiellonian.Parsers.ExpressionParser.parseExpression;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private static final Pattern VARIABLE_NAME = Pattern.compile("(x_[1-9][0-9]*)");
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

    public PolynomialAsMap addPolynomial(PolynomialAsMap polynomialAsMap) {
        Map<List<Integer>, Integer> polynomialMap = polynomialAsMap.getPolynomialMap();
        Map<List<Integer>, Integer> polynomialResult = new HashMap<>();

        //TODO

        return new PolynomialAsMap(polynomialResult);
    }

    public void replaceExpression(String expressionToReplace, String replacementExpression) {
        if (!isSingleExpression(expressionToReplace) || !isSingleExpression(replacementExpression)) {
            throw new WrongFormatException();
        }
        ParsedExpression parsedExpressionToReplace = parseExpression(expressionToReplace);
        List<String> variables = findVariables(replacementExpression);
        PolynomialAsMap replacement = new PolynomialAsMap(new MonomialParser().fromStringToMap(replacementExpression, variables));
        Set<Integer> indexesToReplace = parsedExpressionToReplace.getVariables();

        List<PolynomialAsMap> fragments = new ArrayList<>();
        VariablePowers:
        for (List<Integer> variablePowers : polynomialMap.keySet()) {
            int initialConstant = polynomialMap.get(variablePowers);
            int finalConstant;
            if (parsedExpressionToReplace.getConstant().isPresent()) {
                finalConstant = initialConstant / parsedExpressionToReplace.getConstant().get();
            }
            int size = variablePowers.size();
            for (Integer index : indexesToReplace) {
                if (index > size || variablePowers.get(index - 1) < parsedExpressionToReplace.getVariablePower(index)) {
                    Map<List<Integer>, Integer> map = new HashMap<>();
                    map.put(variablePowers, polynomialMap.get(variablePowers));
                    fragments.add(new PolynomialAsMap(map));
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

    @Override
    public String toString() {
        //TODO
        return polynomialMap.toString();
    }
}
