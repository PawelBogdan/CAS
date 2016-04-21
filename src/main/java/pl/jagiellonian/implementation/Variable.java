package pl.jagiellonian.implementation;

import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;

import java.util.regex.Pattern;

import static pl.jagiellonian.utils.Operation.*;

/**
 * Created by Z-DNA on 20.04.16.
 */
public class Variable implements IVariable{
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public Boolean isExpression(){
        return Pattern.compile("^\\(.+\\)$").matcher(name).matches();
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public ITreeExpression add(IVariable variable) {
        return new TreeExpression(ADD, this).addChild(variable);
    }

    @Override
    public ITreeExpression sub(IVariable variable) {
        return new TreeExpression(SUB, this).addChild(variable);
    }

    @Override
    public ITreeExpression mult(IVariable variable) {
        return new TreeExpression(MULT, this).addChild(variable);
    }

    @Override
    public ITreeExpression div(IVariable variable) {
        return new TreeExpression(DIV, this).addChild(variable);
    }

    @Override
    public ITreeExpression pow(IVariable variable) {
        return new TreeExpression(POW, this).addChild(variable);
    }

    @Override
    public ITreeExpression add(String variable) {
        return new TreeExpression(ADD, this).addChild(new Variable(variable));
    }

    @Override
    public ITreeExpression sub(String variable) {
        return new TreeExpression(SUB, this).addChild(new Variable(variable));
    }

    @Override
    public ITreeExpression mult(String variable) {
        return new TreeExpression(MULT, this).addChild(new Variable(variable));
    }

    @Override
    public ITreeExpression div(String variable) {
        return new TreeExpression(DIV, this).addChild(new Variable(variable));
    }

    @Override
    public ITreeExpression pow(String variable) {
        return new TreeExpression(POW, this).addChild(new Variable(variable));
    }
}
