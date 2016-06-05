package pl.jagiellonian.model;

import pl.jagiellonian.exceptions.WrongFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class PolynomialAsListOfMaps {
    private final static String CONSTANT = "(?:\\(?(-?[1-9][0-9]*)\\)?\\*?)?";
    private final static Pattern CONSTANT_IN_EXPRESSION = Pattern.compile(CONSTANT + "(.*)");
    private final static String VARIABLE = "(?:([a-zA-Z]+_?[0-9]*)(?:\\^(-?[0-9]+))?)";
    private final static Pattern VARIABLE_IN_EXPRESSION = Pattern.compile(VARIABLE);
    private final static String VARIABLES = format("%s(?:\\*%s)*", VARIABLE, VARIABLE);
    private final static Pattern VALID_EXPRESSION = Pattern.compile(format("(%s{1}(?:%s)*|(?:%s)+)(?:\\+(%s{1}(?:%s)*|(?:%s)+))*", CONSTANT, VARIABLES, VARIABLES, CONSTANT, VARIABLES, VARIABLES));
    private final static Pattern SINGLE_EXPRESSION = Pattern.compile(format("(%s{1}(?:%s)*|(?:%s)+)", CONSTANT, VARIABLES, VARIABLES));

    private List<SingleExpression> expressions = new ArrayList<>();

    public PolynomialAsListOfMaps() {
    }

    public List<SingleExpression> getExpressions() {
        return expressions;
    }

    public static PolynomialAsListOfMaps parse(String expression) {
        expression = expression.replaceAll("\\s+", "");
        Matcher matcher = VALID_EXPRESSION.matcher(expression);
        if (!matcher.matches()) {
            throw new WrongFormatException();
        }
        String[] singleExpressions = expression.split("\\+");
        PolynomialAsListOfMaps result = new PolynomialAsListOfMaps();
        for (String singleExpressionString : singleExpressions) {
            SingleExpression singleExpression = parseSingleExpression(singleExpressionString);
            if (singleExpression != null) {
                result.expressions.add(singleExpression);
            }
        }
        result.simplifyExpressions();
        return result;
    }

    private static SingleExpression parseSingleExpression(String singleExpressionString) {
        Matcher constantMatcher = CONSTANT_IN_EXPRESSION.matcher(singleExpressionString);
        if (constantMatcher.matches()) {
            String constant = constantMatcher.group(1);
            Matcher variableMatcher = VARIABLE_IN_EXPRESSION.matcher(constantMatcher.group(2));
            SingleExpressionPowers singleExpressionPowers = new SingleExpressionPowers();
            while (variableMatcher.find()) {
                String power = variableMatcher.group(2);
                singleExpressionPowers.addPower(new VariableInMap(variableMatcher.group(1)), power != null ? Integer.parseInt(power) : 1);
            }
            return constant != null ?
                    new SingleExpression(singleExpressionPowers, Integer.parseInt(constant)) :
                    new SingleExpression(singleExpressionPowers);
        }
        return null;
    }

    private void simplifyExpressions() {
        Set<SingleExpressionPowers> powers = expressions.parallelStream()
                .map(SingleExpression::getPowers)
                .collect(Collectors.toSet());
        List<SingleExpression> expressions = new ArrayList<>();
        for (SingleExpressionPowers power : powers) {
            int constant = expressions.parallelStream()
                    .filter(singleExpression -> singleExpression.getPowers().equals(power))
                    .mapToInt(SingleExpression::getConstant)
                    .sum();
            if (constant != 0) {
                expressions.add(new SingleExpression(power, constant));
            }
        }
        this.expressions = expressions;
    }

    public void add(PolynomialAsListOfMaps that) {
        expressions.addAll(that.expressions);
        simplifyExpressions();
    }

    public static PolynomialAsListOfMaps add(PolynomialAsListOfMaps first, PolynomialAsListOfMaps second) {
        List<SingleExpression> expressions = new ArrayList<>();
        expressions.addAll(first.expressions);
        expressions.addAll(second.expressions);
        PolynomialAsListOfMaps result = new PolynomialAsListOfMaps();
        result.expressions = expressions;
        result.simplifyExpressions();
        return result;
    }

    public void multiple(PolynomialAsListOfMaps that) {
        expressions = multiple(this, that).expressions;
    }

    @SuppressWarnings("Convert2streamapi")
    private static PolynomialAsListOfMaps multiple(PolynomialAsListOfMaps first, PolynomialAsListOfMaps second) {
        List<SingleExpression> expressions = new ArrayList<>();
        for (SingleExpression firstExpression : first.getExpressions()) {
            for (SingleExpression secondExpression : second.getExpressions()) {
                expressions.add(multipleExpressions(firstExpression, secondExpression));
            }
        }
        PolynomialAsListOfMaps result = new PolynomialAsListOfMaps();
        result.expressions = expressions;
        result.simplifyExpressions();
        return result;
    }

    private static SingleExpression multipleExpressions(SingleExpression first, SingleExpression second) {
        SingleExpressionPowers result = new SingleExpressionPowers();
        first.getPowers().getPowersSet().stream().forEach(entry -> result.putPower(entry.getKey(), entry.getValue()));
        second.getPowers().getPowersSet().stream().forEach(entry -> result.addPower(entry.getKey(), entry.getValue()));
        return new SingleExpression(result, first.getConstant() * second.getConstant());
    }

    public int degree(String name) {
        if (VARIABLE_IN_EXPRESSION.matcher(name).matches()) {
            VariableInMap variable = new VariableInMap(name);
            return expressions.parallelStream()
                    .mapToInt(expression -> {
                        SingleExpressionPowers powers = expression.getPowers();
                        return powers.getPower(variable);
                    }).max().orElse(0);
        }
        throw new WrongFormatException();
    }

    public int degree(List<String> names) {
        for (String name : names) {
            if (!VARIABLE_IN_EXPRESSION.matcher(name).matches()) {
                throw new WrongFormatException();
            }
        }
        List<VariableInMap> variables = names.parallelStream()
                .map(VariableInMap::new)
                .collect(Collectors.toList());

        return expressions.parallelStream()
                .mapToInt(expression -> {
                    SingleExpressionPowers powers = expression.getPowers();
                    return variables.parallelStream()
                            .mapToInt(powers::getPower).sum();
                }).max().orElse(0);
    }

    @SuppressWarnings("ConstantConditions")
    public void substitute(String expressionToReplace, String replacementExpressionString) {
        if (!SINGLE_EXPRESSION.matcher(expressionToReplace).matches()) {
            throw new WrongFormatException();
        }
        PolynomialAsListOfMaps replacement = PolynomialAsListOfMaps.parse(replacementExpressionString);
        SingleExpression toReplacement = parseSingleExpression(expressionToReplace);
        SingleExpressionPowers powers = toReplacement.getPowers();

        List<PolynomialAsListOfMaps> fragments = new ArrayList<>();
        VariablePowers:
        for (SingleExpression expression : expressions) {
            SingleExpressionPowers expressionPowers = expression.getPowers();
            int matchPower = Integer.MAX_VALUE;

            for (Map.Entry<VariableInMap, Integer> entry : powers.getPowersSet()) {
                if (expressionPowers.getPower(entry.getKey()) == null || expressionPowers.getPower(entry.getKey()) < entry.getValue()) {
                    PolynomialAsListOfMaps fragment = new PolynomialAsListOfMaps();
                    fragment.expressions.add(expression);
                    fragments.add(fragment);
                    continue VariablePowers;
                } else {
                    int matching = expressionPowers.getPower(entry.getKey()) / entry.getValue();
                    if (matching > matchPower) {
                        matchPower = matching;
                    }
                }
            }

            if (matchPower != Integer.MAX_VALUE) {
                for (int i = 1; i < matchPower; i++) {
                    replacement.multiple(replacement);
                }
                SingleExpressionPowers newPowers = new SingleExpressionPowers();
                for (Map.Entry<VariableInMap, Integer> entry : expressionPowers.getPowersSet()) {
                    if (powers.getPower(entry.getKey()) != null) {
                        int power = entry.getValue() - matchPower * powers.getPower(entry.getKey());
                        if (power != 0) {
                            newPowers.putPower(entry.getKey(), power);
                        }
                    } else {
                        newPowers.putPower(entry.getKey(), entry.getValue());
                    }
                }
                PolynomialAsListOfMaps fragment = new PolynomialAsListOfMaps();
                if (replacement.getExpressions().get(0).isConstantPresent()) {
                    int replacementConstant = replacement.getExpressions().get(0).getConstant();
                    fragment.expressions.add(new SingleExpression(newPowers, (expression.getConstant() - replacementConstant) / replacementConstant));
                }else{
                    fragment.expressions.add(new SingleExpression(newPowers, expression.getConstant()));
                }
                fragment.multiple(replacement);
                fragments.add(fragment);
            } else {
                PolynomialAsListOfMaps fragment = new PolynomialAsListOfMaps();
                fragment.expressions.add(new SingleExpression(expressionPowers, toReplacement.getConstant()));
                fragments.add(fragment);
            }
        }
        PolynomialAsListOfMaps result = new PolynomialAsListOfMaps();
        fragments.forEach(result::add);
        expressions = result.expressions;
    }

    @Override
    public String toString() {
        List<String> polynomial = expressions.stream()
                .map(SingleExpression::toString)
                .collect(Collectors.toList());
        return String.join("+", polynomial);
    }
}
