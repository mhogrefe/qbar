package jas.arith;

/**
 * Interface with getInteger and getSymmetricInteger methods.
 *
 * @author Heinz Kredel
 */

public interface Modular {
    /**
     * Return a symmetric JasBigInteger from this Element.
     *
     * @return a symmetric JasBigInteger of this.
     */
    JasBigInteger getSymmetricInteger();
}
