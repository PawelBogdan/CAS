package pl.jagiellonian.implementation;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolynomialAsListOfMaps {
    private List<SingleExpression> expressions;

    public PolynomialAsListOfMaps(List<SingleExpression> expressions) {
        this.expressions = expressions;
    }

    public List<SingleExpression> getExpressions() {
        return expressions;
    }

    public class SingleExpression {
        private final Pair<SingleExpressionPowers, Integer> expression;

        public SingleExpression(Pair<SingleExpressionPowers, Integer> expression) {
            this.expression = expression;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SingleExpression)) return false;

            SingleExpression that = (SingleExpression) o;

            return expression.equals(that.expression);

        }

        @Override
        public int hashCode() {
            return expression.hashCode();
        }
    }

    public class SingleExpressionPowers {
        private final Map<Variable, Integer> powers = new HashMap<>();

        public int getPower(Variable variable) {
            return powers.get(variable);
        }

        public void putPower(Variable variable, int power) {
            powers.put(variable, power);
        }

        public void addPower(Variable variable, int power) {
            Integer variablePower = powers.get(variable);
            if (variablePower != null) {
                powers.put(variable, variablePower + power);
            } else {
                powers.put(variable, power);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SingleExpressionPowers)) return false;

            SingleExpressionPowers that = (SingleExpressionPowers) o;

            return powers.equals(that.powers);
        }

        @Override
        public int hashCode() {
            return powers.hashCode();
        }
    }

    public class Variable{
        private final String name;

        public Variable(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Variable)) return false;

            Variable variable = (Variable) o;

            return name != null ? name.equals(variable.name) : variable.name == null;

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
