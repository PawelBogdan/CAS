package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.implementation.Expander;
import pl.jagiellonian.implementation.TreeExpression;
import pl.jagiellonian.implementation.Variable;
import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.utils.Operation;

import static org.junit.Assert.assertEquals;

public class TreeExpressionExpanderTest {

    @Test
    public void twoComponentsTest() {
        ITreeExpression tree = new TreeExpression(Operation.ADD, new Variable("1"));
        tree = tree.mult("x");
        tree = tree.sub("2");
        ITreeExpression tree2 = new TreeExpression(Operation.ADD, new Variable("1"));
        tree2 = tree2.mult("x");
        tree2 = tree2.add("6");
        tree = tree.mult(tree2);
        Expander expander = new Expander();
        String expected = "(1.0)*(x)^2+(4.0)*x+-12.0";

        assertEquals(expected, expander.expand(tree).toString());
    }

    @Test
    public void threeComponentsTest() {
        ITreeExpression tree = new TreeExpression(Operation.ADD, new Variable("1"));
        tree = tree.mult("x");
        tree = tree.sub("2");
        ITreeExpression tree2 = new TreeExpression(Operation.ADD, new Variable("1"));
        tree2 = tree2.mult("x");
        tree2 = tree2.add("6");
        ITreeExpression tree3 = new TreeExpression(Operation.SUB, new Variable("6"));
        tree3 = tree3.mult("x");
        tree3 = tree3.sub("5");
        tree = tree.mult(tree2);
        tree = tree.mult(tree3);
        Expander expander = new Expander();
        String expected = "(6.0)*(x)^3+(19.0)*(x)^2+(-92.0)*x+60.0";

        assertEquals(expected, expander.expand(tree).toString());
    }

    @Test
    public void fourComponentsTest() {
        ITreeExpression tree = new TreeExpression(Operation.ADD, new Variable("-6"));
        tree = tree.mult("x");
        tree = tree.sub("24");
        ITreeExpression tree2 = new TreeExpression(Operation.ADD, new Variable("1"));
        tree2 = tree2.mult("x");
        tree2 = tree2.sub("10");
        ITreeExpression tree3 = new TreeExpression(Operation.ADD, new Variable("6"));
        tree3 = tree3.mult("x");
        tree3 = tree3.sub("3");
        ITreeExpression tree4 = new TreeExpression(Operation.ADD, new Variable("-2"));
        tree4 = tree4.mult("x");
        tree4 = tree4.sub("5");
        tree = tree.mult(tree2);
        tree = tree.mult(tree3);
        tree = tree.mult(tree4);
        Expander expander = new Expander();
        String expected = "(72.0)*(x)^4+(-288.0)*(x)^3+(-3834.0)*(x)^2+(-5220.0)*x+3600.0";

        assertEquals(expected, expander.expand(tree).toString());
    }

    @Test
    public void multipleComponentsTest() {
        ITreeExpression tree = new TreeExpression(Operation.ADD, new Variable("-1"));
        tree = tree.mult("x");
        tree = tree.add("6");
        ITreeExpression tree2 = new TreeExpression(Operation.ADD, new Variable("-2"));
        tree2 = tree2.mult("x");
        tree2 = tree2.add("4");
        ITreeExpression tree3 = new TreeExpression(Operation.ADD, new Variable("6"));
        tree3 = tree3.mult("x");
        tree3 = tree3.sub("13");
        ITreeExpression tree4 = new TreeExpression(Operation.ADD, new Variable("-4"));
        tree4 = tree4.mult("x");
        tree4 = tree4.add("5");
        ITreeExpression tree5 = new TreeExpression(Operation.ADD, new Variable("-3"));
        tree5 = tree5.mult("x");
        tree5 = tree5.sub("2");
        ITreeExpression tree6 = new TreeExpression(Operation.ADD, new Variable("2"));
        tree6 = tree6.mult("x");
        tree6 = tree6.add("10");
        ITreeExpression tree7 = new TreeExpression(Operation.ADD, new Variable("1"));
        tree7 = tree7.mult("x");
        tree7 = tree7.add("2");
        ITreeExpression tree8 = new TreeExpression(Operation.ADD, new Variable("-5"));
        tree8 = tree8.mult("x");
        tree8 = tree8.sub("3");
        ITreeExpression tree9 = new TreeExpression(Operation.ADD, new Variable("1"));
        tree9 = tree9.mult("x");
        tree9 = tree9.add("11");
        tree = tree.mult(tree2);
        tree = tree.mult(tree3);
        tree = tree.mult(tree4);
        tree = tree.mult(tree5);
        tree = tree.mult(tree6);
        tree = tree.mult(tree7);
        tree = tree.mult(tree8);
        tree = tree.mult(tree9);
        Expander expander = new Expander();
        String expected = "(-1440.0)*(x)^9+(-11304.0)*(x)^8+(97516.0)*(x)^7+(408068.0)*(x)^6" +
                "+(-1491980.0)*(x)^5+(-1924636.0)*(x)^4+(5544544.0)*(x)^3+(2407712.0)*(x)^2+(-4178880.0)*x+-2059200.0";

        assertEquals(expected, expander.expand(tree).toString());
    }

}