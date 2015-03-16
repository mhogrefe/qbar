package jas.poly;

import jas.structure.RingElem;

import java.util.Map;

/**
 * Monomial class.
 * Represents pairs of exponent vectors and coefficients.
 * Adaptor for Map.Entry.
 *
 * @author Heinz Kredel
 */

public final class Monomial<C extends RingElem<C>> {

    /**
     * Exponent of monomial.
     */
    public final ExpVector e;


    /**
     * Coefficient of monomial.
     */
    public final C c;


    /**
     * Constructor of monomial.
     *
     * @param me a MapEntry.
     */
    public Monomial(Map.Entry<ExpVector, C> me) {
        this(me.getKey(), me.getValue());
    }


    /**
     * Constructor of monomial.
     *
     * @param e exponent.
     * @param c coefficient.
     */
    private Monomial(ExpVector e, C c) {
        this.e = e;
        this.c = c;
    }

    /**
     * String representation of Monomial.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return c.toString() + " " + e.toString();
    }

}
