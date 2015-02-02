package jas.ufd;

import jas.arith.*;
import jas.poly.*;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.util.ArrayList;
import java.util.List;

public class HenselUtil_ModLong {
    /**
     * Modular Hensel lifting algorithm on coefficients. Let p =
     * f_i.ring.coFac.modul() and assume C == prod_{0,...,n-1} f_i mod p with
     * gcd(f_i,f_j) == 1 mod p for i != j
     *
     * @param C monic integer polynomial
     * @param F = [f_0,...,f_{n-1}] list of monic modular polynomials.
     * @param k approximation exponent.
     * @return [g_0, ..., g_{n-1}] with C = prod_{0,...,n-1} g_i mod p^k.
     */
    @SuppressWarnings("unchecked")
    public static List<GenPolynomial<ModLong>> liftHenselMonic(GenPolynomial<JasBigInteger> C, List<GenPolynomial<ModLong>> F, long k) {
        if (C == null || C.isZERO() || F == null || F.size() == 0) {
            throw new IllegalArgumentException("C must be nonzero and F must be nonempty");
        }
        GenPolynomialRing<JasBigInteger> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        List<GenPolynomial<ModLong>> lift = new ArrayList<>(F.size());
        GenPolynomialRing<ModLong> pfac = F.get(0).ring;
        RingFactory<ModLong> pcfac = pfac.coFac;
        ModularRingFactory<ModLong> PF = (ModularRingFactory<ModLong>) pcfac;
        JasBigInteger P = PF.getIntegerModul();
        int n = F.size();
        if (n == 1) { // lift F_0, this case will probably never be used
            GenPolynomial<ModLong> f = F.get(0);
            ModularRingFactory<ModLong> mcfac;
            if (ModLongRing.MAX_LONG.compareTo(P.getVal()) > 0) {
                mcfac = (ModularRingFactory) new ModLongRing(P.getVal());
            } else {
                mcfac = (ModularRingFactory) new ModIntegerRing(P.getVal());
            }
            GenPolynomialRing<ModLong> mfac = new GenPolynomialRing<>(mcfac, fac);
            f = PolyUtil.fromIntegerCoefficients(mfac, PolyUtil_ModLong.integerFromModularCoefficients(fac, f));
            lift.add(f);
            return lift;
        }

        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        List<GenPolynomial<JasBigInteger>> Fi = PolyUtil_ModLong.integerFromModularCoefficients(ifac, F);

        List<GenPolynomial<ModLong>> S = liftExtendedEuclidean(F, k + 1); // lift works for any k, TODO: use this
        // adjust coefficients
        List<GenPolynomial<ModLong>> Sx = PolyUtil.fromIntegerCoefficients(pfac, PolyUtil
                .integerFromModularCoefficients(ifac, S));
        try {
            HenselUtil.isExtendedEuclideanLift(F, Sx);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        List<GenPolynomial<JasBigInteger>> Si = PolyUtil_ModLong.integerFromModularCoefficients(ifac, S);

        // approximate mod p^i
        ModularRingFactory<ModLong> mcfac = PF;
        JasBigInteger p = mcfac.getIntegerModul();
        JasBigInteger modul = p;
        GenPolynomialRing<ModLong> mfac = new GenPolynomialRing<>(mcfac, fac);
        List<GenPolynomial<ModLong>> Sp = PolyUtil.fromIntegerCoefficients(mfac, Si);
        //System.out.println("Sp = " + Sp);
        for (int i = 1; i < k; i++) {
            //System.out.println("i = " + i);
            GenPolynomial<JasBigInteger> e = fac.getONE();
            for (GenPolynomial<JasBigInteger> fi : Fi) {
                e = e.multiply(fi);
            }
            e = C.subtract(e);
            //System.out.println("\ne = " + e);
            if (e.isZERO()) {
                break;
            }
            try {
                e = e.divide(modul);
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                throw ex;
            }
            //System.out.println("e = " + e);
            // move to in Z_p[x]
            GenPolynomial<ModLong> c = PolyUtil.fromIntegerCoefficients(mfac, e);
            //System.out.println("c = " + c + ": " + c.ring.coFac);

            List<GenPolynomial<ModLong>> s = new ArrayList<>(S.size());
            int j = 0;
            for (GenPolynomial<ModLong> f : Sp) {
                f = f.multiply(c);
                //System.out.println("f = " + f + " : " + f.ring.coFac);
                //System.out.println("F,i = " + F.get(j) + " : " + F.get(j).ring.coFac);
                f = f.remainder(F.get(j++));
                //System.out.println("f = " + f + " : " + f.ring.coFac);
                s.add(f);
            }
            //System.out.println("s = " + s);
            List<GenPolynomial<JasBigInteger>> si = PolyUtil_ModLong.integerFromModularCoefficients(ifac, s);
            //System.out.println("si = " + si);

            List<GenPolynomial<JasBigInteger>> Fii = new ArrayList<>(F.size());
            j = 0;
            for (GenPolynomial<JasBigInteger> f : Fi) {
                f = f.sum(si.get(j++).multiply(modul));
                Fii.add(f);
            }
            //System.out.println("Fii = " + Fii);
            Fi = Fii;
            modul = modul.multiply(p);
        }
        // setup ring mod p^k
        modul = Power.positivePower(p, k);
        if (ModLongRing.MAX_LONG.compareTo(modul.getVal()) > 0) {
            mcfac = (ModularRingFactory) new ModLongRing(modul.getVal());
        } else {
            mcfac = (ModularRingFactory) new ModIntegerRing(modul.getVal());
        }
        //System.out.println("mcfac = " + mcfac);
        mfac = new GenPolynomialRing<>(mcfac, fac);
        lift = PolyUtil.fromIntegerCoefficients(mfac, Fi);
        //System.out.println("lift = " + lift + ": " + lift.get(0).ring.coFac);
        return lift;
    }

    /**
     * Constructing and lifting algorithm for extended Euclidean relation. Let p
     * = A_i.ring.coFac.modul() and assume gcd(A_i,A_j) == 1 mod p, i != j.
     *
     * @param A list of modular GenPolynomials
     * @param k desired approximation exponent p^k.
     * @return [s_0, ..., s_n-1] with sum_i s_i * B_i = 1 mod p^k, with B_i =
     * prod_{i!=j} A_j.
     */
    private static List<GenPolynomial<ModLong>> liftExtendedEuclidean(List<GenPolynomial<ModLong>> A, long k) {
        if (A == null || A.size() == 0) {
            throw new IllegalArgumentException("A must be non null and non empty");
        }
        GenPolynomialRing<ModLong> fac = A.get(0).ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        GenPolynomial<ModLong> zero = fac.getZERO();
        int r = A.size();
        List<GenPolynomial<ModLong>> Q = new ArrayList<>(r);
        for (GenPolynomial<ModLong> aA1 : A) {
            Q.add(zero);
        }
        //System.out.println("A = " + A);
        Q.set(r - 2, A.get(r - 1));
        for (int j = r - 3; j >= 0; j--) {
            GenPolynomial<ModLong> q = A.get(j + 1).multiply(Q.get(j + 1));
            Q.set(j, q);
        }
        //System.out.println("Q = " + Q);
        List<GenPolynomial<ModLong>> B = new ArrayList<>(r + 1);
        List<GenPolynomial<ModLong>> lift = new ArrayList<>(r);
        for (GenPolynomial<ModLong> aA : A) {
            B.add(zero);
            lift.add(zero);
        }
        GenPolynomial<ModLong> one = fac.getONE();
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        B.add(0, one);
        //System.out.println("B(0) = " + B.get(0));
        GenPolynomial<ModLong> b = one;
        for (int j = 0; j < r - 1; j++) {
            //System.out.println("Q("+(j)+") = " + Q.get(j));
            //System.out.println("A("+(j)+") = " + A.get(j));
            //System.out.println("B("+(j)+") = " + B.get(j));
            List<GenPolynomial<ModLong>> S = liftDiophant(Q.get(j), A.get(j), B.get(j), k);
            //System.out.println("\nSb = " + S);
            b = S.get(0);
            GenPolynomial<ModLong> bb = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong
                    .integerFromModularCoefficients(ifac, b));
            B.set(j + 1, bb);
            lift.set(j, S.get(1));
            //System.out.println("B("+(j+1)+") = " + B.get(j+1));
        }
        //System.out.println("liftb = " + lift);
        lift.set(r - 1, b);
        //System.out.println("B("+(r-1)+") = " + B.get(r-1) + " : " +  B.get(r-1).ring.coFac + ", b = " +  b + " : " +  b.ring.coFac);
        //System.out.println("B = " + B);
        //System.out.println("liftb = " + lift);
        return lift;
    }

    /**
     * Constructing and lifting algorithm for extended Euclidean relation. Let p
     * = A.ring.coFac.modul() and assume gcd(A,B) == 1 mod p.
     *
     * @param A modular GenPolynomial
     * @param B modular GenPolynomial
     * @param k desired approximation exponent p^k.
     * @return [s, t] with s A + t B = 1 mod p^k.
     */
    @SuppressWarnings("unchecked")
    private static GenPolynomial<ModLong>[] liftExtendedEuclidean(
            GenPolynomial<ModLong> A, GenPolynomial<ModLong> B, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B);
        }
        GenPolynomialRing<ModLong> fac = A.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // start with extended Euclidean relation mod p
        GenPolynomial<ModLong>[] gst;
        try {
            gst = A.egcd(B);
            if (!gst[0].isONE()) {
                throw new RuntimeException("A and B not coprime, gcd = " + gst[0] + ", A = " + A + ", B = " + B);
            }
        } catch (ArithmeticException e) {
            throw new RuntimeException("coefficient error " + e);
        }
        GenPolynomial<ModLong> S = gst[1];
        GenPolynomial<ModLong> T = gst[2];

        // setup integer polynomial ring
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        GenPolynomial<JasBigInteger> one = ifac.getONE();
        GenPolynomial<JasBigInteger> Ai = PolyUtil_ModLong.integerFromModularCoefficients(ifac, A);
        GenPolynomial<JasBigInteger> Bi = PolyUtil_ModLong.integerFromModularCoefficients(ifac, B);
        GenPolynomial<JasBigInteger> Si = PolyUtil_ModLong.integerFromModularCoefficients(ifac, S);
        GenPolynomial<JasBigInteger> Ti = PolyUtil_ModLong.integerFromModularCoefficients(ifac, T);
        //System.out.println("Ai = " + Ai);
        //System.out.println("Bi = " + Bi);
        //System.out.println("Si = " + Si);
        //System.out.println("Ti = " + Ti);

        // approximate mod p^i
        ModularRingFactory<ModLong> mcfac = (ModularRingFactory<ModLong>) fac.coFac;
        JasBigInteger p = mcfac.getIntegerModul();
        JasBigInteger modul = p;
        GenPolynomialRing<ModLong> mfac; // = new GenPolynomialRing<MOD>(mcfac, fac);
        for (int i = 1; i < k; i++) {
            // e = 1 - s a - t b in Z[x]
            GenPolynomial<JasBigInteger> e = one.subtract(Si.multiply(Ai)).subtract(Ti.multiply(Bi));
            //System.out.println("\ne = " + e);
            if (e.isZERO()) {
                break;
            }
            e = e.divide(modul);
            // move to Z_p[x] and compute next approximation
            GenPolynomial<ModLong> c = PolyUtil.fromIntegerCoefficients(fac, e);
            //System.out.println("c = " + c + ": " + c.ring.coFac);
            GenPolynomial<ModLong> s = S.multiply(c);
            GenPolynomial<ModLong> t = T.multiply(c);
            //System.out.println("s = " + s + ": " + s.ring.coFac);
            //System.out.println("t = " + t + ": " + t.ring.coFac);

            GenPolynomial<ModLong>[] QR = s.quotientRemainder(B); // watch for ordering
            GenPolynomial<ModLong> q = QR[0];
            s = QR[1];
            t = t.sum(q.multiply(A));
            //System.out.println("s = " + s + ": " + s.ring.coFac);
            //System.out.println("t = " + t + ": " + t.ring.coFac);

            GenPolynomial<JasBigInteger> si = PolyUtil_ModLong.integerFromModularCoefficients(ifac, s);
            GenPolynomial<JasBigInteger> ti = PolyUtil_ModLong.integerFromModularCoefficients(ifac, t);
            //System.out.println("si = " + si);
            //System.out.println("ti = " + si);
            // add approximation to solution
            Si = Si.sum(si.multiply(modul));
            Ti = Ti.sum(ti.multiply(modul));
            //System.out.println("Si = " + Si);
            //System.out.println("Ti = " + Ti);
            modul = modul.multiply(p);
            //System.out.println("modul = " + modul + ", " + p + "^" + k + ", p^k = " + Power.power(new JasBigInteger(),p,k));
        }
        //System.out.println("Si = " + Si + ", Ti = " + Ti);
        // setup ring mod p^i
        if (ModLongRing.MAX_LONG.compareTo(modul.getVal()) > 0) {
            mcfac = (ModularRingFactory) new ModLongRing(modul.getVal());
        } else {
            mcfac = (ModularRingFactory) new ModIntegerRing(modul.getVal());
        }
        //System.out.println("mcfac = " + mcfac);
        mfac = new GenPolynomialRing<>(mcfac, fac);
        S = PolyUtil.fromIntegerCoefficients(mfac, Si);
        T = PolyUtil.fromIntegerCoefficients(mfac, Ti);
        //System.out.println("S = " + S + ": " + S.ring.coFac);
        //System.out.println("T = " + T + ": " + T.ring.coFac);
        List<GenPolynomial<ModLong>> AP = new ArrayList<>();
        AP.add(B);
        AP.add(A);
        List<GenPolynomial<ModLong>> SP = new ArrayList<>();
        SP.add(S);
        SP.add(T);
        if (!HenselUtil.isExtendedEuclideanLift(AP, SP)) {
            System.out.println("isExtendedEuclideanLift: false");
        }
        GenPolynomial<ModLong>[] rel = (GenPolynomial<ModLong>[]) new GenPolynomial[2];
        rel[0] = S;
        rel[1] = T;
        return rel;
    }

    /**
     * Modular diophantine equation solution and lifting algorithm. Let p =
     * A_i.ring.coFac.modul() and assume gcd(A,B) == 1 mod p.
     *
     * @param A modular GenPolynomial, mod p^k
     * @param B modular GenPolynomial, mod p^k
     * @param C modular GenPolynomial, mod p^k
     * @param k desired approximation exponent p^k.
     * @return [s, t] with s A' + t B' = C mod p^k, with A' = B, B' = A.
     */
    private static List<GenPolynomial<ModLong>> liftDiophant(
            GenPolynomial<ModLong> A, GenPolynomial<ModLong> B, GenPolynomial<ModLong> C, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B + ", C = " + C);
        }
        List<GenPolynomial<ModLong>> sol = new ArrayList<>();
        GenPolynomialRing<ModLong> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        //System.out.println("C = " + C);
        GenPolynomial<ModLong> zero = fac.getZERO();
        for (int i = 0; i < 2; i++) {
            sol.add(zero);
        }
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        for (Monomial<ModLong> m : C) {
            //System.out.println("monomial = " + m);
            long e = m.e.getVal(0);
            List<GenPolynomial<ModLong>> S = liftDiophant(A, B, e, k);
            //System.out.println("Se = " + S);
            ModLong a = m.c;
            //System.out.println("C.fac = " + fac.toScript());
            a = fac.coFac.fromInteger(a.getSymmetricInteger().getVal());
            int i = 0;
            for (GenPolynomial<ModLong> d : S) {
                //System.out.println("d = " + d);
                d = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong.integerFromModularCoefficients(ifac, d));
                d = d.multiply(a);
                d = sol.get(i).sum(d);
                //System.out.println("d = " + d);
                sol.set(i++, d);
            }
            //System.out.println("sol = " + sol + ", for " + m);
        }
        //GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<JasBigInteger>(new JasBigInteger(), fac);
        A = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong.integerFromModularCoefficients(ifac, A));
        B = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong.integerFromModularCoefficients(ifac, B));
        C = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong.integerFromModularCoefficients(ifac, C));
        GenPolynomial<ModLong> y = B.multiply(sol.get(0)).sum(A.multiply(sol.get(1)));
        if (!y.equals(C)) {
            System.out.println("A = " + A + ", B = " + B);
            System.out.println("s1 = " + sol.get(0) + ", s2 = " + sol.get(1));
            System.out.println("Error: A*r1 + B*r2 = " + y + " : " + fac.coFac);
        }
        return sol;
    }


    /**
     * Modular diophantine equation solution and lifting algorithm. Let p =
     * A_i.ring.coFac.modul() and assume gcd(A,B) == 1 mod p.
     *
     * @param A modular GenPolynomial
     * @param B modular GenPolynomial
     * @param e exponent for x^e
     * @param k desired approximation exponent p^k.
     * @return [s, t] with s A' + t B' = x^e mod p^k, with A' = B, B' = A.
     */
    private static List<GenPolynomial<ModLong>> liftDiophant(
            GenPolynomial<ModLong> A, GenPolynomial<ModLong> B, long e, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B);
        }
        List<GenPolynomial<ModLong>> sol = new ArrayList<>();
        GenPolynomialRing<ModLong> fac = A.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // lift EE relation to p^k
        GenPolynomial<ModLong>[] lee = liftExtendedEuclidean(B, A, k);
        GenPolynomial<ModLong> s1 = lee[0];
        GenPolynomial<ModLong> s2 = lee[1];
        if (e == 0L) {
            sol.add(s1);
            sol.add(s2);
            //System.out.println("sol@0 = " + sol);
            return sol;
        }
        fac = s1.ring;
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        A = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong.integerFromModularCoefficients(ifac, A));
        B = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModLong.integerFromModularCoefficients(ifac, B));

        GenPolynomial<ModLong> xe = fac.univariate(0, e);
        GenPolynomial<ModLong> q = s1.multiply(xe);
        GenPolynomial<ModLong>[] QR = q.quotientRemainder(A);
        q = QR[0];
        //System.out.println("ee coeff qr = " + Arrays.toString(QR));
        GenPolynomial<ModLong> r1 = QR[1];
        GenPolynomial<ModLong> r2 = s2.multiply(xe).sum(q.multiply(B));
        //System.out.println("r1 = " + r1 + ", r2 = " + r2);
        sol.add(r1);
        sol.add(r2);
        //System.out.println("sol@"+ e + " = " + sol);
        GenPolynomial<ModLong> y = B.multiply(r1).sum(A.multiply(r2));
        if (!y.equals(xe)) {
            System.out.println("A = " + A + ", B = " + B);
            System.out.println("r1 = " + r1 + ", r2 = " + r2);
            System.out.println("Error: A*r1 + B*r2 = " + y);
        }
        return sol;
    }
}
