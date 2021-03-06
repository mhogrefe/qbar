package jas.ufd;

import jas.arith.JasBigInteger;
import jas.arith.ModLong;
import jas.poly.GenPolynomial;

public class HenselApprox {
    public final GenPolynomial<JasBigInteger> A;

    public final GenPolynomial<JasBigInteger> B;

    private final GenPolynomial<ModLong> Am;

    private final GenPolynomial<ModLong> Bm;

    public HenselApprox(GenPolynomial<JasBigInteger> A, GenPolynomial<JasBigInteger> B, GenPolynomial<ModLong> Am,
                        GenPolynomial<ModLong> Bm) {
        this.A = A;
        this.B = B;
        this.Am = Am;
        this.Bm = Bm;
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
        HenselApprox a = null;
        try {
            a = (HenselApprox) B;
        } catch (ClassCastException ignored) {
        }
        return A.equals(a.A) && B.equals(a.B) && Am.equals(a.Am) && Bm.equals(a.Bm);
    }
}
