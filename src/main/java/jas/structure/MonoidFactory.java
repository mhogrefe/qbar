package jas.structure;

/**
 * Monoid factory interface. Defines get one and tests for associativity and
 * commutativity.
 *
 * @author Heinz Kredel
 */

public interface MonoidFactory<C extends MonoidElem<C>> extends ElemFactory<C> {
    /**
     * Get the constant one for the MonoidElem.
     *
     * @return 1.
     */
    public C getONE();
}
