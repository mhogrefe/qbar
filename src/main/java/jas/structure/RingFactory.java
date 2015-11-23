package jas.structure;

import java.math.BigInteger;

/**
 * Ring factory interface. Defines test for field and access of characteristic.
 *
 * @author Heinz Kredel
 */
public interface RingFactory<C extends RingElem<C>> extends MonoidFactory<C>, AbelianGroupFactory<C> {
    /**
     * Query if this ring is a field. May return false if it is to hard to
     * determine if this ring is a field.
     *
     * @return true if it is known that this ring is a field, else false.
     */
    boolean isField();

    /**
     * Characteristic of this ring.
     *
     * @return characteristic of this ring.
     */
    BigInteger characteristic();
}
