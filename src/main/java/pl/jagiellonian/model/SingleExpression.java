package pl.jagiellonian.model;

public class SingleExpression {
    private final SingleExpressionPowers powers;
    private final Integer constant;

    public SingleExpression(SingleExpressionPowers powers) {
        this.powers = powers;
        constant = null;
    }

    public SingleExpression(SingleExpressionPowers powers, int constant) {
        this.powers = powers;
        this.constant = constant;
    }

    public SingleExpressionPowers getPowers() {
        return powers;
    }

    public boolean isConstantPresent() {
        return constant != null;
    }

    public int getConstant() {
        return constant != null ? constant : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleExpression)) return false;

        SingleExpression that = (SingleExpression) o;
        return powers.equals(that.powers) && constant == that.constant;
    }

    @Override
    public int hashCode() {
        int result = powers.hashCode();
        result = 31 * result + (constant != null ? constant : 1);
        return result;
    }

    @Override
    public String toString() {
        String powersString = powers.toString();
        return powersString.length() > 0 ? constant + "*" + powersString : constant + "";
    }
}
