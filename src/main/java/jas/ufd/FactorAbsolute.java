package jas.ufd;

import jas.structure.RingElem;
import jas.structure.RingFactory;

public abstract class FactorAbsolute<C extends RingElem<C>> extends FactorAbstract<C> {
    FactorAbsolute(RingFactory<C> cfac) {
        super(cfac);
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
