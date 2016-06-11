package pl.jagiellonian.utils;

/**
 * Created by Z-DNA on 20.04.16.
 */
public enum Operation {
    ADD("+", 3, true, true),
    SUB("-", 3, true, false),
    DIV("/", 2, true, false),
    MULT("*", 2, true, true),
    POW("^", 1, false, true);

    private String sign;
    private Integer priority;
    private Boolean leftAssociativity;
    private Boolean rightAssociativity;

    public static Operation getOperation(String sign){
        for(Operation o:Operation.values()){
            if(o.getSign().equals(sign)){
                return o;
            }
        }
        return null;
    }

    public String getSign(){
        return sign;
    }
    public Integer getPriority() {return priority;}
    Operation(String sign, Integer priority, Boolean leftAssociativity, Boolean rightAssociativity){
        this.sign = sign;
        this.priority = priority;
        this.leftAssociativity = leftAssociativity;
        this.rightAssociativity = rightAssociativity;
    }

    public Boolean isRightAssociativity() {
        return rightAssociativity;
    }

    public Boolean isLeftAssociativity() {
        return leftAssociativity;
    }
}
