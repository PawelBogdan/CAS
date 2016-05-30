package pl.jagiellonian.implementation;

import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.interfaces.MonomialTester;
import pl.jagiellonian.interfaces.Sorter;

public class LexicographicOrderTester implements MonomialTester {

    private Sorter<IVariable> sorter;

    public LexicographicOrderTester(Sorter sorter) {
        this.sorter = sorter;
    }

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
