package pl.jagiellonian.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.interfaces.Sorter;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Luq on 2016-05-30.
 */
public class LexicographicOrderSorter implements Sorter<IVariable> {

    /**
     * @param var - {@link IVariable} or {@link ITreeExpression} expression 
     * @return {@link IVariable} or {@link ITreeExpression} sorted lexicographic
     */
    public IVariable sort(IVariable var) {
        if (!var.isExpression()) {
            return var;
        }
        ITreeExpression expr = (ITreeExpression) var;
        List<IVariable> allNodes = expr.getAllNodes();

        List<IVariable> reducedNodes = replaceVariablesMultiplicationWithExponentiation(allNodes);

        reducedNodes.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

        return getVariablesMultiplication(reducedNodes);
    }

    private List<IVariable> replaceVariablesMultiplicationWithExponentiation(List<IVariable> allNodes) {
        Map<String, Long> namesFrequency = allNodes.stream().collect(groupingBy(IVariable::toString, counting()));

        List<String> names = new ArrayList<>(namesFrequency.keySet());
        List<IVariable> nodesPowered = new ArrayList<>();
        for (String name : names) {
            long exponent = namesFrequency.get(name);
            IVariable variable = exponent == 1 ? new Variable(name) :
                    new Variable(name).pow(String.valueOf(namesFrequency.get(name)));
            nodesPowered.add(variable);
        }
        return nodesPowered;
    }

    private IVariable getVariablesMultiplication(List<IVariable> nodesPowered) {
        IVariable result = new ArrayList<>(nodesPowered).get(0);
        for (int i = 1; i < nodesPowered.size(); i++) {
            result = result.mult(nodesPowered.get(i));
        }
        return result;
    }
}
