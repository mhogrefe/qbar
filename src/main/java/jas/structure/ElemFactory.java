package jas.structure;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

public interface ElemFactory<C> extends Serializable {
    public boolean isFinite();

    public C fromInteger(long a);

    public C fromInteger(BigInteger a);

    public C random(int n, Random random);
}
