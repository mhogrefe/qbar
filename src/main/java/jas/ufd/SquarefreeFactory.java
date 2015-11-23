package jas.ufd;

import jas.arith.JasBigInteger;
import jas.arith.ModIntegerRing;
import jas.arith.ModLongRing;
import jas.poly.GenPolynomialRing;
import jas.structure.RingElem;
import jas.structure.RingFactory;

class SquarefreeFactory {
    @SuppressWarnings("unchecked")
    private static <C extends RingElem<C>> SquarefreeAbstract<C> getImplementationPoly(
            GenPolynomialRing<C> fac) {
        if (fac.characteristic().signum() == 0) {
            return new SquarefreeRingChar0<>(fac.coFac);
        }
        if (fac.coFac.isFinite()) {
            return new SquarefreeFiniteFieldCharP<>(fac.coFac);
        }
        throw new IllegalArgumentException("no squarefree factorization " + fac.coFac);
    }

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
            ufd = getImplementationPoly(pfac);
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
