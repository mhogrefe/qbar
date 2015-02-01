package jas.ufd;

import jas.arith.*;
import jas.structure.RingElem;
import jas.structure.RingFactory;

class GCDFactory {
    @SuppressWarnings("unchecked")
    public static <C extends RingElem<C>> GreatestCommonDivisorAbstract<C> getImplementation(RingFactory<C> fac) {
        GreatestCommonDivisorAbstract ufd;
        if (fac instanceof JasBigInteger) {
            ufd = new GreatestCommonDivisorModular<ModInteger>();
        } else if (fac instanceof ModIntegerRing) {
            ufd = new GreatestCommonDivisorModEval<ModInteger>();
        } else if (fac instanceof ModLongRing) {
            ufd = new GreatestCommonDivisorModEval<ModLong>();
        } else if (fac instanceof BigRational) {
            ufd = new GreatestCommonDivisorSubres<BigRational>();
        } else {
            ufd = new GreatestCommonDivisorSubres<C>();
        }

        return ufd;
    }

    @SuppressWarnings("unchecked")
    public static <C extends RingElem<C>> GreatestCommonDivisorAbstract<C> getProxy(RingFactory<C> fac) {
        return GCDFactory.getImplementation(fac);
    }

}
