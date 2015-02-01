package jas.ufd;

import jas.arith.JasBigInteger;
import jas.arith.Modular;
import jas.poly.GenPolynomial;
import jas.structure.RingElem;

import java.io.Serializable;

public class HenselApprox<MOD extends RingElem<MOD> & Modular> implements Serializable {
    public final GenPolynomial<JasBigInteger> A;

    public final GenPolynomial<JasBigInteger> B;

    private final GenPolynomial<MOD> Am;

    private final GenPolynomial<MOD> Bm;

    public HenselApprox(GenPolynomial<JasBigInteger> A, GenPolynomial<JasBigInteger> B, GenPolynomial<MOD> Am,
                        GenPolynomial<MOD> Bm) {
        this.A = A;
        this.B = B;
        this.Am = Am;
        this.Bm = Bm;
    }

    @Override
    public String toString() {
        return A.toString() + "," + B.toString() + "," + Am.toString() + "," + Bm.toString();
    }

    @Override
    public int hashCode() {
        int h = A.hashCode();
        h = 37 * h + B.hashCode();
        h = 37 * h + Am.hashCode();
        h = 37 * h + Bm.hashCode();
        return h;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object B) {
        if (!(B instanceof HenselApprox)) {
            return false;
        }
        HenselApprox<MOD> a = null;
        try {
            a = (HenselApprox<MOD>) B;
        } catch (ClassCastException ignored) {
        }
        return A.equals(a.A) && B.equals(a.B) && Am.equals(a.Am) && Bm.equals(a.Bm);
    }
}
