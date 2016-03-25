package jas.arith;

import jas.structure.RingElem;
import jas.structure.RingFactory;

/**
 * Modular ring factory interface. Defines chinese remainder method and get
 * modul method.
 *
 * @author Heinz Kredel
 */
public interface ModularRingFactory<C extends RingElem<C> & Modular> extends RingFactory<C> {
    /**
     * Return the JasBigInteger modul for the factory.
     *
     * @return a JasBigInteger of this.modul.
     */
    JasBigInteger getIntegerModul();
}
