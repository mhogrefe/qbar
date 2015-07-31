package jas.poly;

import jas.structure.RingElem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RelationTable for solvable polynomials. This class maintains the
 * non-commutative multiplication relations of solvable polynomial rings. The
 * table entries are initialized with relations of the form x<sub>j</sub> *
 * x<sub>i</sub> = p<sub>ij</sub>. During multiplication the relations are
 * updated by relations of the form x<sub>j</sub><sup>k</sup> *
 * x<sub>i</sub><sup>l</sup> = p<sub>ijkl</sub>. If no relation for
 * x<sub>j</sub> * x<sub>i</sub> is found in the table, this multiplication is
 * assumed to be commutative x<sub>i</sub> x<sub>j</sub>. Can also be used for
 * relations between coefficients and main variables.
 *
 * @author Heinz Kredel
 */

class RelationTable<C extends RingElem<C>> implements Serializable {
    /**
     * The data structure for the relations.
     */
    private final Map<List<Integer>, List> table;

    /**
     * The factory for the solvable polynomial ring.
     */
    private final GenSolvablePolynomialRing<C> ring;

    /**
     * Constructor for RelationTable requires ring factory. Note: This
     * constructor is called within the constructor of the ring factory, so
     * methods of this class can only be used after the other constructor has
     * terminated.
     *
     * @param r solvable polynomial ring factory.
     */
    public RelationTable(GenSolvablePolynomialRing<C> r) {
        table = new HashMap<>();
        ring = r;
        if (ring == null) {
            throw new IllegalArgumentException("RelationTable no ring");
        }
    }

    /**
     * Hash code for this relation table.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int h;
        h = ring.hashCode();
        h = 31 * h + table.hashCode();
        return h;
    }

    /**
     * Get the String representation.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        List v;
        StringBuilder s = new StringBuilder("RelationTable[");
        boolean first = true;
        for (List<Integer> k : table.keySet()) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            s.append(k.toString());
            v = table.get(k);
            s.append("=");
            s.append(v.toString());
        }
        s.append("]");
        return s.toString();
    }
}
