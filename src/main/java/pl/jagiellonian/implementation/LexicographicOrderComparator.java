package pl.jagiellonian.implementation;

import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.interfaces.MonomialComparator;
import pl.jagiellonian.utils.Operation;

import java.util.ArrayList;
import java.util.List;

public class LexicographicOrderComparator implements MonomialComparator {
    @Override
    public boolean compare(IVariable var1, IVariable var2) {
        IVariable var1Sorted = sort(var1);
        IVariable var2Sorted = sort(var2);
        if (!var1Sorted.isExpression() && !var2Sorted.isExpression()) {
            return var1.toString().equals(var2.toString());
        }
        if (!var1Sorted.isExpression() || !var2Sorted.isExpression()) {
            return false;
        }
        ITreeExpression expr1 = (ITreeExpression) var1Sorted;
        ITreeExpression expr2 = (ITreeExpression) var2Sorted;
        if (expr1.getChildren().size() != expr2.getChildren().size()) {
            return false;
        }
        for (int i = 0; i < expr1.getChildren().size(); i++) {
            if (!expr1.getChildren().get(i).toString().equals(expr2.getChildren().get(i).toString())) {
                return false;
            }
        }
        return true;
    }

    private IVariable sort(IVariable var) {
        if (!var.isExpression()) {
            return var;
        }
        ITreeExpression expr = (ITreeExpression) var;
        List<IVariable> allNodes = getAllNodes(expr);
        allNodes.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
        IVariable result = allNodes.get(0);
        for (int i = 1; i < allNodes.size(); i++) {
            result = result.mult(allNodes.get(i));
        }
        return result;
    }

    private List<IVariable> getAllNodes(ITreeExpression expression) {
        if (expression.getOperation() == Operation.POW) {
            List<IVariable> nodes = new ArrayList<>();
            for (int i = 0; i < Integer.valueOf(expression.getChildren().get(1).toString()); i++) {
                nodes.add(expression.getChildren().get(0));
            }
            return nodes;
        }
        List<IVariable> nodes = new ArrayList<>();
        for (IVariable variable : expression.getChildren()) {
            if (variable.isExpression()) {
                nodes.addAll(getAllNodes((ITreeExpression) variable));
            } else {
                nodes.add(variable);
            }
        }
        return nodes;
    }
}
