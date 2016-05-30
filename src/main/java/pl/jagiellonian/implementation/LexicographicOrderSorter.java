package pl.jagiellonian.implementation;

import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.interfaces.Sorter;

import java.util.List;

/**
 * Created by Luq on 2016-05-30.
 */
public class LexicographicOrderSorter implements Sorter<IVariable> {

    public IVariable sort(IVariable var) {
        if (!var.isExpression()) {
            return var;
        }
        ITreeExpression expr = (ITreeExpression) var;
        List<IVariable> allNodes = expr.getAllNodes();
        allNodes.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
        IVariable result = allNodes.get(0);
        for (int i = 1; i < allNodes.size(); i++) {
            result = result.mult(allNodes.get(i));
        }
        return result;
    }
}
