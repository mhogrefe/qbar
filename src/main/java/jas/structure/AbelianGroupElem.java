package jas.structure;

public interface AbelianGroupElem<C extends AbelianGroupElem<C>> extends Comparable<C> {
    public boolean isZERO();

    public int signum();

    public C sum(C S);

    public C subtract(C S);

    public C negate();

    public C abs();
}
