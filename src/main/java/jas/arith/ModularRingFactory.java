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

    //
    //Chinese remainder algorithm. Assert c.modul >= a.modul and c.modul *
    //a.modul = this.modul.
    //
    //@param c  modular.
    //@param ci inverse of c.modul in ring of a.
    //@param a  other ModLong.
    //@return S, with S mod c.modul == c and S mod a.modul == a.
    //
    C chineseRemainder(C c, C ci, C a);
}
