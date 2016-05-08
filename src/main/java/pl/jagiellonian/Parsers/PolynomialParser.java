package pl.jagiellonian.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lukaszrzepka on 02.05.2016.
 */
public class PolynomialParser {
    private MonomialParser monomialParser;

    public PolynomialParser() {
        this.monomialParser = new MonomialParser();
    };

    /**
     *
     * @param expression "-22*x_1^3*x_2^33*b+x_3^12*b^2"
     * @param variables ["x_1", "x_2", "x_3", "b"]
     * @return
     */
    public List<Map<List<Integer>, Integer>> fromStringToList(String expression, List<String> variables) {
        List<String> monomialList = new ArrayList<>();
        List<Map<List<Integer>, Integer>> polynomialList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[\\+-]?([1-9][0-9]*\\*)?([a-zA-Z](_[1-9][0-9]*)?(\\^[1-9][0-9]*)?\\*?)+");
        Matcher matcher = pattern.matcher(expression);
        if (!isPolynomial(expression)) {
            //TODO
        } else {
            while (matcher.find()) {
                monomialList.add(matcher.group(0));
            }
        }
        for (String monomial : monomialList) {
            polynomialList.add(this.monomialParser.fromStringToMap(monomial, variables));
        }

        return polynomialList;
    }

    public boolean isPolynomial(String expression) {
        Pattern pattern = Pattern.compile( "([\\+-]?([1-9][0-9]*\\*)?([a-zA-Z](_[1-9][0-9]*)?(\\^[1-9][0-9]*)?\\*?)+)+");
        return pattern.matcher(expression).matches();
    }
}
