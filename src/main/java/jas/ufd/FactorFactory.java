package jas.ufd;

import jas.arith.ModIntegerRing;
import jas.arith.ModLongRing;
import jas.poly.GenPolynomialRing;
import jas.structure.RingElem;
import jas.structure.RingFactory;

public class FactorFactory {

    @SuppressWarnings("unchecked")
    public static <C extends RingElem<C>> FactorAbstract<C> getImplementation(RingFactory<C> fac) {
        FactorAbstract/*raw type<C>*/ufd;
        GenPolynomialRing pfac;
        if (fac instanceof ModIntegerRing) {
            ufd = new FactorModular(fac);
        } else if (fac instanceof ModLongRing) {
            ufd = new FactorModular(fac);
        } else if (fac instanceof GenPolynomialRing) {
            pfac = (GenPolynomialRing) fac;
            ufd = getImplementation(pfac.coFac);
        } else {
            throw new IllegalArgumentException("no factorization implementation for "
                    + fac.getClass().getName());
        }
        //
        return (FactorAbstract<C>) ufd;
    }

}
