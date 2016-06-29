package pl.jagiellonian.interfaces;

import pl.jagiellonian.utils.Operation;

import java.util.List;

/**
 * Created by Z-DNA on 20.04.16.
 */
public interface ITreeExpression extends IVariable {
    ITreeExpression addChild(IVariable child);

    IVariable getLastChild();

    IVariable setLastChild(IVariable child);

    List<IVariable> getAllNodes();

    Integer getPriority();

    ITreeExpression setParenthesis();

    Boolean getParenthesis();

    Operation getOperation();

    List<IVariable> getChildren();

    int degree(IVariable variable);
    int degree(List<IVariable> variables);
}
