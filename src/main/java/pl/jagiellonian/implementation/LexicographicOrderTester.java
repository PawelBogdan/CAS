package pl.jagiellonian.implementation;

import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.interfaces.MonomialTester;
import pl.jagiellonian.interfaces.Sorter;

public class LexicographicOrderTester implements MonomialTester {

    private final Sorter<IVariable> sorter;

    public LexicographicOrderTester(Sorter<IVariable> sorter) {
        this.sorter = sorter;
    }

    /**
     * @param var1 first {@link IVariable} or {@link ITreeExpression} to test
     * @param var2 second {@link IVariable} or {@link ITreeExpression} to test
     * @return true if both expressions are equals by lexicographic order; false if not or any given parameter
     * is not an expression
     */
    @Override
    public boolean test(IVariable var1, IVariable var2) {
        IVariable var1Sorted = sorter.sort(var1);
        IVariable var2Sorted = sorter.sort(var2);
        if (!var1Sorted.isExpression() && !var2Sorted.isExpression()) {
            return var1.toString().equals(var2.toString());
        }
        if (!var1Sorted.isExpression() || !var2Sorted.isExpression()) {
            return false;
        }
        ITreeExpression expr1 = (ITreeExpression) var1Sorted;
        ITreeExpression expr2 = (ITreeExpression) var2Sorted;
        return expr1.toString().equals(expr2.toString());
    }
}
