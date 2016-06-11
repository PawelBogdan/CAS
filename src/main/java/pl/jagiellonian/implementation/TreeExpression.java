package pl.jagiellonian.implementation;

import com.google.common.base.Joiner;
import pl.jagiellonian.exceptions.WrongFormatException;
import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.utils.Operation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.jagiellonian.utils.Operation.*;

/**
 * Created by Z-DNA on 20.04.16.
 */
public class TreeExpression implements ITreeExpression {
    private Operation operation;
    private Boolean parenthesis = false;
    private List<IVariable> children;

    public TreeExpression(Operation operation, IVariable variable) {
        this.operation = operation;
        children = new LinkedList<>();
        if(variable instanceof ITreeExpression){
            if(((ITreeExpression) variable).getPriority() > getPriority() || !operation.isLeftAssociativity()){
                ((ITreeExpression)variable).setParenthesis();
            }
            children.add(variable);
        } else {
            children.add(variable.isExpression()?parse(variable.toString()):variable);
        }
    }

    public TreeExpression(Operation operation, IVariable variable, IVariable variable2) {
        this(operation, variable);
        addChild(variable2);
    }

    public static IVariable parse(String expression) {
        /**
         * -- ex: parse given expression --
         * <code>
         *     parse("a*x^2+b*x+c");
         * </code>
         *
         * @param expression - polynomial expression
         * @return {@link IVariable} or {@link ITreeExpression} from given string
         */
        Boolean parenthesis = false;
        expression = expression.replaceAll("\\s", "");
        //System.out.println(expression);
        Pattern exprPattern = Pattern.compile("(\\((.*?)\\)|([a-zA-Z]+(_?[a-zA-Z1-9]+)*)|[1-9]+)(([\\+-/\\^\\*])(\\((.*?)\\)|([a-zA-Z]+(_?[a-zA-Z1-9]+)*)|[1-9]+))*");
        if(!exprPattern.matcher(expression).matches()){
            throw new WrongFormatException();
        } else {
            Matcher m = Pattern.compile("\\(([^\\(\\)]+)\\)").matcher(expression);
            if(m.matches()){
                parenthesis = true;
                expression = m.group(1);
            }
        }

        Pattern varPattern = Pattern.compile("(\\((.*?)\\)|([a-zA-Z]+(_?[a-zA-Z1-9]+)*)|[1-9]+)([\\+-/\\^\\*])?");
        Matcher m = varPattern.matcher(expression);
        List<IVariable> variables = new LinkedList<>();
        List<Operation> operations= new LinkedList<>();
        while(m.find()){
            variables.add(new Variable(m.group(1)));
            if(m.group(5)!= null && !m.group(5).equals("")) {
                operations.add(Operation.getOperation(m.group(5)));
            }
        }
        //System.out.println(variables.toString());
        //System.out.println(operations.toString());

        if(variables.size() == 1){
            return variables.get(0);
        }

        ITreeExpression tree = new TreeExpression(operations.remove(0), variables.remove(0), variables.remove(0));

        IVariable var;
        Operation operation;
        while(operations.size()>0){
            operation = operations.remove(0);
            var = variables.remove(0);
            if(operation.getPriority() < tree.getPriority()){
                tree.setLastChild(new TreeExpression(operation, tree.getLastChild(), var));
            } else {
                tree = new TreeExpression(operation, tree, var);
            }
        }
        return parenthesis?tree.setParenthesis():tree;
    }

    public ITreeExpression addChild(IVariable child){
        if(child instanceof ITreeExpression){
            if(((ITreeExpression) child).getPriority() > getPriority() || !operation.isRightAssociativity()){
                ((ITreeExpression)child).setParenthesis();
            }
            children.add(child);
            return this;
        }
        children.add(child.isExpression()?parse(child.toString()):child);
        return this;
    }

    @Override
    public IVariable getLastChild() {
        IVariable lastChild = children.get(children.size()-1);
        if(lastChild instanceof ITreeExpression && !((ITreeExpression) lastChild).getParenthesis()){
            return ((ITreeExpression)lastChild).getLastChild();
        } else {
            return lastChild;
        }
    }

    @Override
    public IVariable setLastChild(IVariable child) {
        IVariable lastChild = children.get(children.size()-1);
        if(lastChild instanceof ITreeExpression && !((ITreeExpression) lastChild).getParenthesis()){
            return ((ITreeExpression)lastChild).setLastChild(child);
        } else {
            return children.set(children.size()-1, child);
        }
    }

    @Override
    public ITreeExpression setParenthesis(){
        this.parenthesis = true;
        return this;
    }

    @Override
    public Boolean getParenthesis(){
        return parenthesis;
    }

    /**
     * This method return the highest degree for list of params
     * using recursive function
     *
     * @param variables list of {@link IVariable} to find in expression
     * @return int the highest degree for list of param
     */
    @Override
    public int degree(List<IVariable> variables) {
        return recursionDegree(variables, this, 0);
    }

    /**
     * This method return the highest degree for one param
     * using recursive function
     *
     * @param variable param {@link IVariable} to find in expression
     * @return int the highest degree for one param
     */
    @Override
    public int degree(IVariable variable) {
        List<IVariable> list = new ArrayList<>();
        list.add(variable);
        return recursionDegree(list, this, 0);
    }

    /**
     * This function calculate the highest degree for expression
     * visiting tree in recursion
     *
     * @param search list of {@link IVariable} to find in expression
     * @param variable root element
     * @param degree current degree
     * @return int the highest degree for list of param
     */
    private int recursionDegree(List<IVariable> search, IVariable variable, int degree) {
        if (variable.isExpression()) {
            TreeExpression treeExpression = (TreeExpression) variable;
            switch (treeExpression.getOperation()) {
                case ADD:
                case SUB:
                    return Math.max(recursionDegree(search, treeExpression.getFirstChild(), degree),
                            recursionDegree(search, treeExpression.getChildren().get(1), degree));
                case DIV:
                    return recursionDegree(search, treeExpression.getFirstChild(), degree)
                            - recursionDegree(search, treeExpression.getChildren().get(1), degree);
                case MULT:
                    return recursionDegree(search, treeExpression.getFirstChild(), degree)
                            + recursionDegree(search, treeExpression.getChildren().get(1), degree);
                case POW:
                    if (treeExpression.getChildren().get(1).isExpression()) {
                        throw new WrongFormatException();
                    }
                    if (treeExpression.getFirstChild().isExpression()) {
                        return recursionDegree(search, treeExpression.getFirstChild(), degree)
                                * Integer.valueOf(treeExpression.getChildren().get(1).getName());
                    } else {
                        if (search.contains(treeExpression.getFirstChild())) {
                            return Integer.valueOf(treeExpression.getChildren().get(1).getName());
                        } else {
                            return 0;
                        }
                    }
            }
        } else {
            if (search.contains(variable)) {
                return 1;
            } else {
                return 0;
            }
        }

        return 0;
    }

    public IVariable getFirstChild() {
        return this.children.get(0);
    }

    public List<IVariable> getChildren() {
        return this.children;
    }

    public Operation getOperation() {
        return this.operation;
    }

    @Override
    public ITreeExpression expand(Map<String, String> map) {
        for (int i = 0; i < children.size(); i++) {
            IVariable child = children.get(i);
            if(child instanceof ITreeExpression){
                child.expand(map);
            } else {
                if(map.containsKey(child.toString())){
                    IVariable newChild = parse(map.get(child.toString()));
                    if(newChild instanceof ITreeExpression){
                        ((ITreeExpression) newChild).setParenthesis();
                    }
                    children.set(i, newChild);
                }
            }
        }
        return this;
    }

    @Override
    public Integer getPriority(){
        return operation.getPriority();
    }

    @Override
    public String toString(){
        return parenthesis ?"("+Joiner.on(operation.getSign()).join(children)+")":Joiner.on(operation.getSign()).join(children);
    }

    @Override
    public Boolean isExpression() {
        return true;
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

    @Override
    public String getName() {
        return null;
    }
}
