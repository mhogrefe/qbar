package jas.structure;

public interface AbelianGroupElem<C extends AbelianGroupElem<C>> extends Comparable<C> {
    boolean isZERO();

    int signum();

    C sum(C S);

    C subtract(C S);

    C negate();

    C abs();
}
