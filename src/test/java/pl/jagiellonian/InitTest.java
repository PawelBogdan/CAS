package pl.jagiellonian;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * Created by Z-DNA on 18.04.16.
 */
public class InitTest {

    private static final Logger logger = LogManager.getLogger(InitTest.class);

    @Test
    public void test() {
        logger.debug("Hello in test");
        PolynomialFunction p = new PolynomialFunction(new double[]{2.5, 2D, 5D});
        logger.debug(p.toString());

        Source.main(null);
    }
}
