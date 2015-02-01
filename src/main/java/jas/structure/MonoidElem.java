package jas.structure;

public interface MonoidElem<C extends MonoidElem<C>> {
    public boolean isONE();

    public boolean isUnit();

    public C multiply(C S);

    public C divide(C S);

    public C remainder(C S);

    public C inverse();
}
