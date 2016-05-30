package pl.jagiellonian.interfaces;

import pl.jagiellonian.utils.Operation;

import java.util.List;

/**
 * Created by Z-DNA on 20.04.16.
 */
public interface ITreeExpression extends IVariable{
    ITreeExpression addChild(IVariable child);
    IVariable getLastChild();
    IVariable setLastChild(IVariable child);
    List<IVariable> getChildren();
    List<IVariable> getAllNodes(ITreeExpression expression);
    Operation getOperation();
    Integer getPriority();
    ITreeExpression setParenthesis();
    Boolean getParenthesis();
}
