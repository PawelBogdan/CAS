package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.implementation.Variable;
import pl.jagiellonian.interfaces.ITreeExpression;
import pl.jagiellonian.interfaces.IVariable;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static pl.jagiellonian.implementation.TreeExpression.parse;

/**
 * Created by Z-DNA on 21.04.16.
 */
public class TreeExpressionExpandTest {

    @Test
    public void initExpandTest(){
        String exp = "a*x^2";
        Map sub = new HashMap<>();
        sub.put("a", exp);
        sub.put("x", "x+y-z^2");
        assertEquals("(a*x^2)*(x+y-z^2)^2", parse(exp).expand(sub).toString());

        IVariable x = new Variable("x");
        assertEquals("x+y-z^2", x.expand(sub).toString());

        IVariable y = new Variable("y");
        assertEquals(y.toString(), y.expand(sub).toString());
    }
}
