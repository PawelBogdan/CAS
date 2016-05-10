package pl.jagiellonian.interfaces;

import java.util.Map;

/**
 * Created by Z-DNA on 20.04.16.
 */
public interface ITreeExpression extends IVariable{
    ITreeExpression addChild(IVariable child);
    IVariable getLastChild();
    IVariable setLastChild(IVariable child);
    Integer getPriority();
    ITreeExpression setParenthesis();
    Boolean getParenthesis();
}
