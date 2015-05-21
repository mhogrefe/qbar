package jas.structure;

public interface MonoidElem<C extends MonoidElem<C>> {
    boolean isONE();

    boolean isUnit();

    C multiply(C S);

    C divide(C S);

    C remainder(C S);

    C inverse();
}
