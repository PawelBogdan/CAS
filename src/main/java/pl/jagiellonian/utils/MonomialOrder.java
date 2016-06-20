package pl.jagiellonian.utils;

/**
 * Created by tehol on 05.06.16.
 */
public enum MonomialOrder {
    /**
     * First compares exponents of x_1 in the monomials, and in case of equality compares exponents of x_2, and so forth.
     */
    LEXICOGRAPHIC,

    /**
     * Compares the total degree (sum of all exponents), and in case of a tie applies lexicographic order.
     */
    GRADED_LEXICOGRAPHIC
}
