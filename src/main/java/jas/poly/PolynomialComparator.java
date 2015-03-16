package jas.poly;

import jas.structure.RingElem;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for polynomials.
 *
 * @param <C> coefficient type
 * @author Heinz Kredel.
 */
public class PolynomialComparator<C extends RingElem<C>> implements Serializable,
        Comparator<GenPolynomial<C>> {


    private final TermOrder tord;


    private final boolean reverse;


    /**
     * Constructor.
     *
     * @param t TermOrder.
     */
    public PolynomialComparator(TermOrder t) {
        tord = t;
        this.reverse = false;
    }


    //
    //Compare polynomials.
    //
    //@param p1 first polynomial.
    //@param p2 second polynomial.
    //@return 0 if ( p1 == p2 ), -1 if ( p1 < p2 ) and +1 if ( p1 > p2 ).
    //
    public int compare(GenPolynomial<C> p1, GenPolynomial<C> p2) {
        // check if p1.tord = p2.tord = tord ?
        int s = p1.compareTo(p2);
        //System.out.println("p1.compareTo(p2) = " + s);
        if (reverse) {
            return -s;
        }
        return s;
    }


    /**
     * Equals test of comparator.
     *
     * @param o other object.
     * @return true if this = o, else false.
     */
    @Override
    public boolean equals(Object o) {
        PolynomialComparator pc;
        try {
            pc = (PolynomialComparator) o;
        } catch (ClassCastException ignored) {
            return false;
        }
        return pc != null && tord.equals(pc.tord);
    }


    /**
     * Hash code for this PolynomialComparator.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return tord.hashCode();
    }


    /**
     * toString.
     */
    @Override
    public String toString() {
        return "PolynomialComparator(" + tord + ")";
    }

}
