package pl.jagiellonian.implementation;

import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;
import pl.jagiellonian.utils.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Expander {

    /**
     * @param iTreeExpression - {@link ITreeExpression} expression to expand
     * @return {@link ITreeExpression} expanded polynomial
     */
    public ITreeExpression expand(ITreeExpression iTreeExpression) {
        return divideIntoMonomials(iTreeExpression);
    }

    private ITreeExpression divideIntoMonomials(ITreeExpression iTreeExpression) {
        ITreeExpression child0 = (ITreeExpression) iTreeExpression.getChildren().get(0);
        ITreeExpression child1 = (ITreeExpression) iTreeExpression.getChildren().get(1);

        List<Double> firstList;
        List<Double> secondList = convertToDoubleArray(new ArrayList<>(), 0, child1);

        if(isMultiplication(child0)) {
            firstList = convertToDoubleArray(new ArrayList<>(), 0, divideIntoMonomials(child0));
        }
        else {
            firstList = convertToDoubleArray(new ArrayList<>(), 0, child0);
        }

        return convertToTreePolynomial(multiplyCoefficients(firstList, secondList));
    }

    private boolean isMultiplication(ITreeExpression iTreeExpression) {
        return iTreeExpression.getOperation() == Operation.MULT;
    }


    private List<Double> convertToDoubleArray(List<Double> list, int depth, ITreeExpression iTreeExpression) {
        List<IVariable> listChildren = iTreeExpression.getChildren();

        if(depth == 0) {   //wyraz wolny
            Operation sign = iTreeExpression.getOperation();
            IVariable iVariable = listChildren.get(1);
            double doubleVar = Double.parseDouble(iVariable.toString());
            if(sign == Operation.SUB) {
                doubleVar = - doubleVar;
            }
            list.add(doubleVar);
            ITreeExpression next = (ITreeExpression) listChildren.get(0);
            convertToDoubleArray(list, ++depth, next);
        }
        else if (depth == 1) {  // x
            ITreeExpression ite = (ITreeExpression) iTreeExpression.getChildren().get(0);
            if(isNumber(ite)) {
                list.add(Double.parseDouble(ite.getAllNodes().get(0).toString()));
            }
            else {
                ITreeExpression var = (ITreeExpression) listChildren.get(1);
                list.add(Double.parseDouble(var.getAllNodes().get(0).toString()));
                ITreeExpression next = (ITreeExpression) listChildren.get(0);
                convertToDoubleArray(list, ++depth, next);
            }
        }
        else {
            if(listChildren.size() == 1) {
                ITreeExpression ite = (ITreeExpression) iTreeExpression.getChildren().get(0);
                ITreeExpression var1 = (ITreeExpression) ite.getChildren().get(0);
                list.add(Double.parseDouble(var1.getAllNodes().get(0).toString()));
                return list;
            }
            else {
                ITreeExpression ite = (ITreeExpression) iTreeExpression.getChildren().get(0);
                if (!isNumber(ite)) {
                    ITreeExpression var = (ITreeExpression) listChildren.get(1);
                    ITreeExpression var1 = (ITreeExpression) var.getChildren().get(0);
                    list.add(Double.parseDouble(var1.getAllNodes().get(0).toString()));
                    ITreeExpression next = (ITreeExpression) listChildren.get(0);
                    convertToDoubleArray(list, ++depth, next);
                } else {
                    ITreeExpression var = (ITreeExpression) listChildren.get(0);
                    list.add(Double.parseDouble(var.getAllNodes().get(0).toString()));
                    return list;
                }
            }
        }
        return list;
    }

    private ITreeExpression convertToTreePolynomial(List<Double> list) {
        Collections.reverse(list);
        int degree = list.size() - 1;

        Iterator it = list.iterator();
        ITreeExpression iTreeExpression = null;

        while(it.hasNext()) {
            ITreeExpression variable;

            if(degree == 0) {
                variable = new TreeExpression(Operation.ADD, new Variable(it.next().toString()));
            }
            else if (degree == 1) {
                ITreeExpression value = new TreeExpression(Operation.ADD, new Variable(it.next().toString()));
                variable = value.mult("x");
            }
            else {
                ITreeExpression value = new TreeExpression(Operation.ADD, new Variable(it.next().toString()));
                variable = new TreeExpression(Operation.POW, new Variable("x"));
                variable = variable.pow(degree + "");
                variable = value.mult(variable);
            }

            degree--;

            if(iTreeExpression == null) {
                iTreeExpression = new TreeExpression(Operation.ADD, variable);
            }
            else {
                iTreeExpression = iTreeExpression.add(variable);
            }
        }

        return iTreeExpression;
    }

    private boolean isNumber(ITreeExpression iTreeExpression) {
        return iTreeExpression.getAllNodes().size() == 1;
    }

    private List<Double> multiplyCoefficients(List<Double> first, List<Double> second){
        int totalLength = first.size() + second.size() - 1;
        List<Double> resultAsArray = new ArrayList<>(totalLength);
        double[] result = new double[totalLength];
        for (int i = 0; i < first.size(); i++)
            for (int j = 0; j < second.size(); j++) {
                result[i + j] += first.get(i) * second.get(j);
            }
        for(double d: result){
            resultAsArray.add(d);
        }
        return resultAsArray;
    }
}
