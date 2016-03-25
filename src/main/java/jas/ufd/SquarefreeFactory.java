package jas.ufd;

import jas.arith.JasBigInteger;
import jas.arith.ModIntegerRing;
import jas.arith.ModLongRing;
import jas.poly.GenPolynomialRing;
import jas.structure.RingElem;
import jas.structure.RingFactory;

class SquarefreeFactory {
    @SuppressWarnings("unchecked")
    public static <C extends RingElem<C>> SquarefreeAbstract<C> getImplementation(RingFactory<C> fac) {
        SquarefreeAbstract/*raw type<C>*/ufd;
        GenPolynomialRing pfac;
        if (fac instanceof JasBigInteger) {
            ufd = new SquarefreeRingChar0<>(fac);
        } else if (fac instanceof ModIntegerRing) {
            ufd = new SquarefreeFiniteFieldCharP<>(fac);
        } else if (fac instanceof ModLongRing) {
            ufd = new SquarefreeFiniteFieldCharP<>(fac);
        } else if (fac instanceof GenPolynomialRing) {
            pfac = (GenPolynomialRing) fac;
            if (pfac.characteristic().signum() == 0) {
                ufd = new SquarefreeRingChar0<>(pfac.coFac);
            } else {
                ufd = new SquarefreeFiniteFieldCharP<>(pfac.coFac);
            }
        } else if (fac.isField()) {
            ufd = new SquarefreeFiniteFieldCharP<>(fac);
        } else if (fac.characteristic().signum() == 0) {
            ufd = new SquarefreeRingChar0<>(fac);
        } else {
            throw new IllegalArgumentException("no squarefree factorization implementation for "
                    + fac.getClass().getName());
        }
        return (SquarefreeAbstract<C>) ufd;
    }
}
