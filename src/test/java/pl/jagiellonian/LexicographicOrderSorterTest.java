package pl.jagiellonian;

import org.junit.Test;
import pl.jagiellonian.implementation.LexicographicOrderSorter;
import pl.jagiellonian.implementation.TreeExpression;
import pl.jagiellonian.interfaces.IVariable;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Luq on 2016-05-30.
 */
public class LexicographicOrderSorterTest {

    private LexicographicOrderSorter lexicographicOrderSorter = new LexicographicOrderSorter();

    @Test
    public void sort() throws Exception {
        // given
        IVariable variable1 = TreeExpression.parse("z*y*a*x");
        String expectedVariableString = "a*x*y*z";

        // when
        IVariable sortedVariable = lexicographicOrderSorter.sort(variable1);

        // then
        assertEquals(expectedVariableString, sortedVariable.toString());
    }

}