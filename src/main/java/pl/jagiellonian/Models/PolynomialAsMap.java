package pl.jagiellonian.Models;

import java.util.List;
import java.util.Map;

/**
 * Created by lukaszrzepka on 11.05.2016.
 */
public class PolynomialAsMap {
    private List<Map<List<Integer>, Integer>> polynomialList;

    public PolynomialAsMap(List<Map<List<Integer>, Integer>> polynomialList) {
        this.polynomialList = polynomialList;
    }

    public List<Map<List<Integer>, Integer>> getPolynomialList() {
        return polynomialList;
    }

    public void setPolynomialList(List<Map<List<Integer>, Integer>> polynomialList) {
        this.polynomialList = polynomialList;
    }

    @Override
    public String toString() {
        //TODO
        return "PolynomialAsMap{" +
                "polynomialList=" + polynomialList +
                '}';
    }
}
