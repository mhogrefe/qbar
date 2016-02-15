package jas.poly;

import jas.structure.RingElem;
import mho.wheels.structures.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * Iterator over monomials of a polynomial.
 * Adaptor for val.entrySet().iterator().
 *
 * @author Heinz Kredel
 */
class PolyIterator<C extends RingElem<C>> implements Iterator<Pair<Long, C>> {
    /**
     * Internal iterator over polynomial map.
     */
    private final Iterator<Map.Entry<Long, C>> ms;

    /**
     * Constructor of polynomial iterator.
     *
     * @param m SortetMap of a polynomial.
     */
    public PolyIterator(SortedMap<Long, C> m) {
        ms = m.entrySet().iterator();
    }

    /**
     * Test for availability of a next monomial.
     *
     * @return true if the iteration has more monomials, else false.
     */
    public boolean hasNext() {
        return ms.hasNext();
    }

    /**
     * Get next monomial element.
     *
     * @return next monomial.
     */
    public Pair<Long, C> next() {
        SortedMap.Entry<Long, C> next = ms.next();
        return new Pair<>(next.getKey(), next.getValue());
    }

    /**
     * Remove the last monomial returned from underlying set if allowed.
     */
    public void remove() {
        ms.remove();
    }
}
