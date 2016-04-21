package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.implementation.Variable;
import pl.jagiellonian.interfaces.IVariable;
import static org.junit.Assert.assertEquals;

/**
 * Created by Z-DNA on 18.04.16.
 */
public class TreeExpressionTest {

    @Test
    public void treeExpressionTest() {
        IVariable x = new Variable("x");
        IVariable y = new Variable("y");
        IVariable z = new Variable("z");

        System.out.println(x.add(y).mult(z).pow("2").add(x.mult(y)));
        assertEquals("((x+y)*z)^2+x*y", x.add(y).mult(z).pow("2").add(x.mult(y)).toString());

        System.out.println(x.pow("2").add(y.mult(x.pow("2"))).add(y.pow("3")).mult("2"));
        assertEquals("(x^2+y*x^2+y^3)*2", x.pow("2").add(y.mult(x.pow("2"))).add(y.pow("3")).mult("2").toString());

        System.out.println(x.add("2").pow("3").add(y.sub("3").mult("2").div("4")));
        assertEquals("(x+2)^3+(y-3)*2/4", x.add("2").pow("3").add(y.sub("3").mult("2").div("4")).toString());
    }

    @Test
    public void addTest(){
        IVariable x = new Variable("x");
        IVariable y = new Variable("y");
        IVariable z = new Variable("z");

        System.out.println(x.add(y.add(z)));
        assertEquals("x+y+z", x.add(y.add(z)).toString());

        System.out.println(x.add(y).add(z));
        assertEquals("x+y+z", x.add(y).add(z).toString());
    }

    @Test
    public void subTest(){
        IVariable x = new Variable("x");
        IVariable y = new Variable("y");
        IVariable z = new Variable("z");

        System.out.println(x.sub(y.sub(z)));
        assertEquals("x-(y-z)", x.sub(y.sub(z)).toString());

        System.out.println(x.sub(y).sub(z));
        assertEquals("x-y-z", x.sub(y).sub(z).toString());
    }

    @Test
    public void multTest(){
        IVariable x = new Variable("x");
        IVariable y = new Variable("y");
        IVariable z = new Variable("z");

        System.out.println(x.mult(y.mult(z)));
        assertEquals("x*y*z", x.mult(y.mult(z)).toString());

        System.out.println(x.mult(y).mult(z));
        assertEquals("x*y*z", x.mult(y).mult(z).toString());
    }

    @Test
    public void divTest(){
        IVariable x = new Variable("x");
        IVariable y = new Variable("y");
        IVariable z = new Variable("z");

        System.out.println(x.div(y.div(z)));
        assertEquals("x/(y/z)", x.div(y.div(z)).toString());

        System.out.println(x.div(y).div(z));
        assertEquals("x/y/z", x.div(y).div(z).toString());
    }

    @Test
    public void powTest(){
        IVariable x = new Variable("x");
        IVariable y = new Variable("y");
        IVariable z = new Variable("z");

        System.out.println(x.pow(y.pow(z)));
        assertEquals("x^y^z", x.pow(y.pow(z)).toString());

        System.out.println(x.pow(y).pow(z));
        assertEquals("(x^y)^z", x.pow(y).pow(z).toString());
    }


}
