package jas.ufd;

import jas.arith.JasBigInteger;
import jas.arith.ModInteger;
import jas.poly.GenPolynomial;

public class HenselApprox_ModInteger {
    public final GenPolynomial<JasBigInteger> A;

    public final GenPolynomial<JasBigInteger> B;

    private final GenPolynomial<ModInteger> Am;

    private final GenPolynomial<ModInteger> Bm;

    public HenselApprox_ModInteger(GenPolynomial<JasBigInteger> A, GenPolynomial<JasBigInteger> B, GenPolynomial<ModInteger> Am,
                        GenPolynomial<ModInteger> Bm) {
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
        if (!(B instanceof HenselApprox_ModInteger)) {
            return false;
        }
        HenselApprox_ModInteger a = null;
        try {
            a = (HenselApprox_ModInteger) B;
        } catch (ClassCastException ignored) {
        }
        return A.equals(a.A) && B.equals(a.B) && Am.equals(a.Am) && Bm.equals(a.Bm);
    }
}
