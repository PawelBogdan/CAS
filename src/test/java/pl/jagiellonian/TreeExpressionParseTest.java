package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.exceptions.WrongFormatException;

import static org.junit.Assert.*;
import static pl.jagiellonian.implementation.TreeExpression.parse;

/**
 * Created by Z-DNA on 20.04.16.
 */
public class TreeExpressionParseTest {

    @Test
    public void initTest(){
        String exp;

        exp = "a*x^2";
        assertEquals(exp.replaceAll("\\s", ""), parse(exp).toString());

        exp = "X1 + a1*X1^2*X2 + a2*X2*X3*X4";
        assertEquals(exp.replaceAll("\\s", ""), parse(exp).toString());

        exp = "X1 + (a1*X1+a2*X2)^3";
        assertEquals(exp.replaceAll("\\s", ""), parse(exp).toString());

        exp = "(a1*X1+a2*X2)^3 + X1";
        assertEquals(exp.replaceAll("\\s", ""), parse(exp).toString());

        exp = "(a1 *X1 +a2* X2)^(2+4+5)";
        assertEquals(exp.replaceAll("\\s", ""), parse(exp).toString());

        exp = "(a1 *X_1 +a2* X2)^(2+4+5)-(a1*X_1_5+a_2*X2)^(a1*X_1+a2*X2)^3 + X1 + (a1*X_1+a2*X2)/(g5+kk7^2)";
        assertEquals(exp.replaceAll("\\s", ""), parse(exp).toString());
    }

    @Test(expected = WrongFormatException.class)
    public void ex01(){
        String exp;

        exp = "_x";
        parse(exp);
    }

    @Test(expected = WrongFormatException.class)
    public void ex02(){
        String exp;

        exp = "x__1";
        parse(exp);
    }

    @Test(expected = WrongFormatException.class)
    public void ex03(){
        String exp;

        exp = "x_";
        parse(exp);
    }

    @Test(expected = WrongFormatException.class)
    public void ex04(){
        String exp;

        exp = "1x";
        parse(exp);
    }

    @Test(expected = WrongFormatException.class)
    public void ex05(){
        String exp;

        exp = "x**x";
        parse(exp);
    }
}
