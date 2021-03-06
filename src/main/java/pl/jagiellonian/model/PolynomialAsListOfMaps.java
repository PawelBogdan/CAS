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
    private final static String CONSTANT = "\\(?(-?[1-9][0-9]*)\\)?\\*?";
    private final static Pattern CONSTANT_IN_EXPRESSION = Pattern.compile("(?:" + CONSTANT + ")?" + "(.*)");
    private final static String VARIABLE = "\\*?(?:([a-zA-Z]+_?[0-9]*)(?:\\^(-?[0-9]+))?)";
    private final static Pattern VARIABLE_IN_EXPRESSION = Pattern.compile(VARIABLE);
    private final static String VARIABLES = format("%s(?:\\*%s)*", VARIABLE, VARIABLE);
    private final static Pattern VALID_EXPRESSION = Pattern.compile(format("(%s{1}(?:%s)*|(?:%s)+)(?:\\+(%s{1}(?:%s)*|(?:%s)+))*", CONSTANT, VARIABLES, VARIABLES, CONSTANT, VARIABLES, VARIABLES));

    protected List<SingleExpression> expressions = new ArrayList<>();

    private PolynomialAsListOfMaps() {
    }

    private PolynomialAsListOfMaps(SingleExpression expressions) {
        this.expressions.add(expressions);
    }

    public PolynomialAsListOfMaps(List<SingleExpression> expressions) {
        this.expressions = new ArrayList<>(expressions);
    }

    public int size() {
        return expressions.size();
    }

    public List<SingleExpression> getExpressions() {
        return new ArrayList<>(expressions);
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
        constantMatcher.matches();
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

    private void simplifyExpressions() {
        Set<SingleExpressionPowers> powers = expressions.parallelStream()
                .map(SingleExpression::getPowers)
                .collect(Collectors.toSet());
        List<SingleExpression> expressions = new ArrayList<>();
        for (SingleExpressionPowers power : powers) {
            double constant = this.expressions.parallelStream()
                    .filter(singleExpression -> singleExpression.getPowers().equals(power))
                    .mapToDouble(SingleExpression::getConstant)
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
        expressions.addAll(first.getExpressions());
        expressions.addAll(second.getExpressions());
        PolynomialAsListOfMaps result = new PolynomialAsListOfMaps();
        result.expressions = expressions;
        result.simplifyExpressions();
        return result;
    }

    public void multiple(PolynomialAsListOfMaps that) {
        expressions = multiple(this, that).expressions;
    }

    @SuppressWarnings("Convert2streamapi")
    public static PolynomialAsListOfMaps multiple(PolynomialAsListOfMaps first, PolynomialAsListOfMaps second) {
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
        first.getPowers().getPowersSet().stream().forEach(result::putPower);
        second.getPowers().getPowersSet().stream().forEach(result::addPower);
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

    public int degree(Set<String> names) {
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

    public void substitute(PolynomialAsListOfMaps polynomialToReplace, PolynomialAsListOfMaps replacementPolynomial) {
        if (polynomialToReplace.getExpressions().size() != 1) {
            throw new WrongFormatException();
        }
        SingleExpression singleExpression = polynomialToReplace.getExpressions().get(0);
        SingleExpressionPowers powersToReplace = singleExpression.getPowers();

        List<PolynomialAsListOfMaps> fragments = new ArrayList<>();
        VariablePowers:
        for (SingleExpression expression : expressions) {
            SingleExpressionPowers expressionPowers = expression.getPowers();
            int matchPower = Integer.MAX_VALUE;

            for (Map.Entry<VariableInMap, Integer> entry : powersToReplace.getPowersSet()) {
                if (expressionPowers.getPower(entry.getKey()) == null || expressionPowers.getPower(entry.getKey()) < entry.getValue()) {
                    fragments.add(new PolynomialAsListOfMaps(expression));
                    continue VariablePowers;
                } else {
                    int matching = expressionPowers.getPower(entry.getKey()) / entry.getValue();
                    if (matching < matchPower) {
                        matchPower = matching;
                    }
                }
            }

            if (matchPower != Integer.MAX_VALUE) {
                SingleExpressionPowers newPowers = new SingleExpressionPowers();
                for (Map.Entry<VariableInMap, Integer> entry : expressionPowers.getPowersSet()) {
                    int power = entry.getValue() - matchPower * powersToReplace.getPower(entry.getKey());
                    if (power != 0) {
                        newPowers.putPower(entry.getKey(), power);
                    }
                }

                for (int i = 1; i < matchPower; i++) {
                    replacementPolynomial.multiple(replacementPolynomial);
                }
                double newConstant = expression.getConstant() - matchPower * singleExpression.getConstant();

                PolynomialAsListOfMaps afterMultiplying = multiple(
                        new PolynomialAsListOfMaps(new SingleExpression(newPowers, newConstant != 0 ? newConstant : 1)),
                        replacementPolynomial);
                fragments.add(afterMultiplying);
            } else {
                if (expressionPowers.size() == 0) {
                    int matching = (int) (expression.getConstant() / singleExpression.getConstant());

                    for (int i = 1; i < matching; i++) {
                        replacementPolynomial.multiple(replacementPolynomial);
                    }
                    double remainingConstant = expression.getConstant() - matching * singleExpression.getConstant();
                    if (remainingConstant != 0) {
                        fragments.add(multiple(
                                new PolynomialAsListOfMaps(
                                        new SingleExpression(
                                                new SingleExpressionPowers(), remainingConstant)),
                                replacementPolynomial));
                    } else {
                        fragments.add(replacementPolynomial);
                    }
                } else {
                    fragments.add(new PolynomialAsListOfMaps(expression));
                }
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
