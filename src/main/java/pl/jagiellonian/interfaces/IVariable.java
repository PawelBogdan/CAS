package pl.jagiellonian.interfaces;

/**
 * Created by Z-DNA on 20.04.16.
 */
public interface IVariable{
    Boolean isExpression();
    ITreeExpression add(IVariable variable);
    ITreeExpression sub(IVariable variable);
    ITreeExpression mult(IVariable variable);
    ITreeExpression div(IVariable variable);
    ITreeExpression pow(IVariable variable);
    ITreeExpression add(String variable);
    ITreeExpression sub(String variable);
    ITreeExpression mult(String variable);
    ITreeExpression div(String variable);
    ITreeExpression pow(String variable);
}
