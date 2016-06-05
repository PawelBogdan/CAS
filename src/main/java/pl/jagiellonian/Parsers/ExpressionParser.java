package pl.jagiellonian.Parsers;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    private static final Pattern VARIABLE_IN_EXPRESSION = Pattern.compile("x_([1-9][0-9]*(?:\\^-?[0-9]+)*)");
    private static final Pattern CONSTANT_IN_EXPRESSION = Pattern.compile("\\(?(-?[1-9][0-9]*)\\)?(.*)");
    private static final Pattern SINGLE_EXPRESSION = Pattern.compile("((\\*?)x_(?:[1-9][0-9]*(?:\\^-?[0-9]+)*))*");

    private ExpressionParser() {
    }

    public static ParsedExpression parseExpression(String expression) {
        expression = expression.trim();
        ParsedExpression parsedExpression = new ParsedExpression();
        Matcher constantMatcher = CONSTANT_IN_EXPRESSION.matcher(expression);
        if (constantMatcher.matches()) {
            parsedExpression.setConstant(Integer.parseInt(constantMatcher.group(1)));
        }
        Matcher variableMatcher = VARIABLE_IN_EXPRESSION.matcher(expression);
        while (variableMatcher.find()) {
            String variable = variableMatcher.group(1);
            String[] split = variable.split("\\^");
            int index = Integer.parseInt(split[0]);
            int power = parsedExpression.getVariablePower(index);
            if (split.length > 1) {
                power += Integer.parseInt(split[1]);
            } else {
                power++;
            }
            parsedExpression.putVariable(index, power);
        }
        return parsedExpression;
    }

    public static boolean isSingleExpression(String expression) {
        if (expression.length() == 0) {
            return false;
        }
        Matcher constantMatcher = CONSTANT_IN_EXPRESSION.matcher(expression);
        if (constantMatcher.matches()) {
            expression = constantMatcher.group(2);
            if (expression.startsWith("*")) {
                expression = expression.replaceFirst("\\*", "");
            }
        }
        Matcher variablesMatcher = SINGLE_EXPRESSION.matcher(expression);
        return variablesMatcher.matches() && !expression.startsWith("*");
    }

    public static class ParsedExpression {
        private Optional<Integer> constant = Optional.empty();
        private final Map<Integer, Integer> variables = new HashMap<>();

        public void setConstant(int constant) {
            this.constant = Optional.of(constant);
        }

        public int getVariablePower(int index) {
            return variables.getOrDefault(index, 0);
        }

        public void putVariable(int index, int value) {
            variables.put(index, value);
        }

        public Optional<Integer> getConstant() {
            return constant;
        }

        public Set<Integer> getVariables() {
            return variables.keySet();
        }
    }
}
