package pl.jagiellonian.model;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SingleExpressionPowers {
    private final Map<VariableInMap, Integer> powers = new HashMap<>();

    public Integer getPower(VariableInMap variable) {
        return powers.get(variable);
    }

    public Set<Map.Entry<VariableInMap, Integer>> getPowersSet() {
        return powers.entrySet();
    }

    public Set<VariableInMap> getVariables() {
        return powers.keySet();
    }

    public void putPower(VariableInMap variable, int power) {
        powers.put(variable, power);
    }

    public void addPower(VariableInMap variable, Integer power) {
        Integer variablePower = powers.get(variable);
        if (variablePower != null) {
            powers.put(variable, variablePower + power);
        } else {
            powers.put(variable, power);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleExpressionPowers)) return false;

        SingleExpressionPowers that = (SingleExpressionPowers) o;
        Set<Map.Entry<VariableInMap, Integer>> thisEntrySet = powers.entrySet();
        Set<Map.Entry<VariableInMap, Integer>> thatEntrySet = that.powers.entrySet();

        return thisEntrySet.equals(thatEntrySet);
    }

    @Override
    public int hashCode() {
        return powers.hashCode();
    }

    @Override
    public String toString() {
        List<String> variables = powers.entrySet().stream()
                .map(entry -> format("%s^%d", entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return String.join("*", variables);
    }
}
