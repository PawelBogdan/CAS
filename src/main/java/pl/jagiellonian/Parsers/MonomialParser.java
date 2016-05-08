package pl.jagiellonian.Parsers;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lukaszrzepka on 02.05.2016.
 */
public class MonomialParser {
    public MonomialParser(){};

    /**
     *
     * @param expression "-22*x_1^3*x_2^33*b"
     * @param variables ["x_1", "x_2", "x_3", "b"]
     * @return
     */
    public Map<List<Integer>, Integer> fromStringToMap(String expression, List<String> variables) {
        Map<List<Integer>, Integer> map = new HashMap<>();
        List<Integer> keys = null;
        String constant = null;

        expression = removeAllSpaces(expression);

        Pattern pattern = Pattern.compile("[\\+-]?([1-9][0-9]*\\*)?([a-zA-Z](_[1-9][0-9]*)?(\\^[1-9][0-9]*)?\\*?)");
        Matcher matcher = pattern.matcher(expression);
        if (!isMonomial(expression)) {
            //TODO
        } else {
            keys = new ArrayList<>(Collections.nCopies(variables.size(), 0));

            boolean isFirstLoop = true;
            while (matcher.find()) {
                String power = "1";
                String variable = null;

                if (matcher.group(0) != null && isFirstLoop) {
                    constant = getSignFromString(matcher.group(0));
                }
                if (isFirstLoop) {
                    if (matcher.group(1) != null) {
                        constant += getConstantFromString(matcher.group(1));
                    } else {
                        constant += "1";
                    }
                }
                if (matcher.group(2) != null) {
                    variable = getVariableFromString(matcher.group(2));
                }
                if (matcher.group(4) != null) {
                    power = getPowerFromString(matcher.group(4));
                }

                if (variable != null) {
                    updatePowersInKeysForMap(keys, power, variable, variables);
                }

                Integer variableIndex = variables.indexOf(variable);
//                System.out.println("constant=" + constant + " variable=" + variable + " power=" + power + " index=" + variableIndex);
//                System.out.println("list with keys: " + keys);
                isFirstLoop = false;
            }
        }
        map.put(keys, Integer.valueOf(constant));
        return map;
    }

    private void updatePowersInKeysForMap(List<Integer> list, String power, String variable, List<String> variables) {
        Integer variableIndex = variables.indexOf(variable);
        list.set(variableIndex, Integer.parseInt(power));
    }

    private String removeAllSpaces(String monomial) {
        return monomial.replaceAll("\\s", "");
    }

    private String getConstantFromString(String expression) {
//        System.out.println("constant to parse=" + expression);
        Pattern pattern = Pattern.compile("([1-9][0-9]*)");
        Matcher matcher = pattern.matcher(expression);
        String result = null;
        if (matcher.find()) {
            result = matcher.group(0);
        }
        if (result == null) {
            return "1";
        }
        else {
            return result;
        }
    }

    private String getVariableFromString(String expression) {
//        System.out.println("variable to parse=" + expression);
        Pattern pattern = Pattern.compile("[a-zA-Z](_[1-9][0-9]*)?");
        Matcher matcher = pattern.matcher(expression);
        String result = null;
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    private String getPowerFromString(String expression) {
//        System.out.println("power to parse=" + expression);
        Pattern pattern = Pattern.compile("[1-9][0-9]*");
        Matcher matcher = pattern.matcher(expression);
        String result = null;
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    private String getSignFromString(String expression) {
//        System.out.println("sign to parse=" + expression);
        Pattern pattern = Pattern.compile("[\\+-]");
        Matcher matcher = pattern.matcher(expression);
        String result = null;
        if (matcher.find()) {
            result = matcher.group(0);
        }
        if (result == null ) {
            result = "+";
        }
        return result;
    }


    public boolean isMonomial(String expression) {
        Pattern pattern = Pattern.compile( "[\\+-]?([1-9][0-9]*\\*)?([a-zA-Z](_[1-9][0-9]*)?(\\^[1-9][0-9]*)?\\*?)+");
        return pattern.matcher(expression).matches();
    }
}
