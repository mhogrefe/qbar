package jas.structure;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public interface ElemFactory<C> extends Serializable {
    boolean isFinite();

    C fromInteger(long a);

    C fromInteger(BigInteger a);

    C random(int n, Random random);
}
