package pl.jagiellonian.model;

public class VariableInMap {
    private final String name;

    public VariableInMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableInMap)) return false;

        VariableInMap variable = (VariableInMap) o;

        return name != null ? name.equals(variable.name) : variable.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
