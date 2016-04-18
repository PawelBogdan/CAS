package pl.jagiellonian;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Z-DNA on 18.04.16.
 */
public class Source {

    private static final Logger logger = LogManager.getLogger(Source.class);

    public static void main(String[] args) {
        logger.debug("Hello in main");
        PolynomialFunction p = new PolynomialFunction(new double[]{2.5, 2D, 5D});
        logger.debug(p.toString());
    }
}
