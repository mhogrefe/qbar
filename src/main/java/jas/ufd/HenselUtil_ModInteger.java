package jas.ufd;

import com.sun.org.apache.xpath.internal.operations.Mod;
import jas.arith.*;
import jas.poly.*;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.util.ArrayList;
import java.util.List;

public class HenselUtil_ModInteger {

    /**
     * Constructing and lifting algorithm for extended Euclidean relation. Let p
     * = A_i.ring.coFac.modul() and assume gcd(A_i,A_j) == 1 mod p, i != j.
     *
     * @param A list of modular GenPolynomials
     * @param k desired approximation exponent p^k.
     * @return [s_0, ..., s_n-1] with sum_i s_i * B_i = 1 mod p^k, with B_i =
     * prod_{i!=j} A_j.
     */
    private static List<GenPolynomial<ModInteger>> liftExtendedEuclidean(
            List<GenPolynomial<ModInteger>> A, long k) {
        if (A == null || A.size() == 0) {
            throw new IllegalArgumentException("A must be non null and non empty");
        }
        GenPolynomialRing<ModInteger> fac = A.get(0).ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        GenPolynomial<ModInteger> zero = fac.getZERO();
        int r = A.size();
        List<GenPolynomial<ModInteger>> Q = new ArrayList<>(r);
        for (GenPolynomial<ModInteger> aA1 : A) {
            Q.add(zero);
        }
        //System.out.println("A = " + A);
        Q.set(r - 2, A.get(r - 1));
        for (int j = r - 3; j >= 0; j--) {
            GenPolynomial<ModInteger> q = A.get(j + 1).multiply(Q.get(j + 1));
            Q.set(j, q);
        }
        //System.out.println("Q = " + Q);
        List<GenPolynomial<ModInteger>> B = new ArrayList<>(r + 1);
        List<GenPolynomial<ModInteger>> lift = new ArrayList<>(r);
        for (GenPolynomial<ModInteger> aA : A) {
            B.add(zero);
            lift.add(zero);
        }
        GenPolynomial<ModInteger> one = fac.getONE();
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        B.add(0, one);
        //System.out.println("B(0) = " + B.get(0));
        GenPolynomial<ModInteger> b = one;
        for (int j = 0; j < r - 1; j++) {
            List<GenPolynomial<ModInteger>> S = liftDiophant(Q.get(j), A.get(j), B.get(j), k);
            b = S.get(0);
            GenPolynomial<ModInteger> bb = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModInteger
                    .integerFromModularCoefficients(ifac, b));
            B.set(j + 1, bb);
            lift.set(j, S.get(1));
        }
        lift.set(r - 1, b);
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
    private static GenPolynomial<ModInteger>[] liftExtendedEuclidean(
            GenPolynomial<ModInteger> A, GenPolynomial<ModInteger> B, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B);
        }
        GenPolynomialRing<ModInteger> fac = A.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // start with extended Euclidean relation mod p
        GenPolynomial<ModInteger>[] gst;
        try {
            gst = A.egcd(B);
            if (!gst[0].isONE()) {
                throw new RuntimeException("A and B not coprime, gcd = " + gst[0] + ", A = " + A + ", B = " + B);
            }
        } catch (ArithmeticException e) {
            throw new RuntimeException("coefficient error " + e);
        }
        GenPolynomial<ModInteger> S = gst[1];
        GenPolynomial<ModInteger> T = gst[2];

        // setup integer polynomial ring
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        GenPolynomial<JasBigInteger> one = ifac.getONE();
        GenPolynomial<JasBigInteger> Ai = PolyUtil_ModInteger.integerFromModularCoefficients(ifac, A);
        GenPolynomial<JasBigInteger> Bi = PolyUtil_ModInteger.integerFromModularCoefficients(ifac, B);
        GenPolynomial<JasBigInteger> Si = PolyUtil_ModInteger.integerFromModularCoefficients(ifac, S);
        GenPolynomial<JasBigInteger> Ti = PolyUtil_ModInteger.integerFromModularCoefficients(ifac, T);

        // approximate mod p^i
        ModularRingFactory<ModInteger> mcfac = (ModularRingFactory<ModInteger>) fac.coFac;
        JasBigInteger p = mcfac.getIntegerModul();
        JasBigInteger modul = p;
        GenPolynomialRing<ModInteger> mfac; // = new GenPolynomialRing<MOD>(mcfac, fac);
        for (int i = 1; i < k; i++) {
            // e = 1 - s a - t b in Z[x]
            GenPolynomial<JasBigInteger> e = one.subtract(Si.multiply(Ai)).subtract(Ti.multiply(Bi));
            //System.out.println("\ne = " + e);
            if (e.isZERO()) {
                break;
            }
            e = e.divide(modul);
            // move to Z_p[x] and compute next approximation
            GenPolynomial<ModInteger> c = PolyUtil.fromIntegerCoefficients(fac, e);
            GenPolynomial<ModInteger> s = S.multiply(c);
            GenPolynomial<ModInteger> t = T.multiply(c);
            GenPolynomial<ModInteger>[] QR = s.quotientRemainder(B); // watch for ordering
            GenPolynomial<ModInteger> q = QR[0];
            s = QR[1];
            t = t.sum(q.multiply(A));
            GenPolynomial<JasBigInteger> si = PolyUtil_ModInteger.integerFromModularCoefficients(ifac, s);
            GenPolynomial<JasBigInteger> ti = PolyUtil_ModInteger.integerFromModularCoefficients(ifac, t);
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
        List<GenPolynomial<ModInteger>> AP = new ArrayList<>();
        AP.add(B);
        AP.add(A);
        List<GenPolynomial<ModInteger>> SP = new ArrayList<>();
        SP.add(S);
        SP.add(T);
        if (!HenselUtil.isExtendedEuclideanLift(AP, SP)) {
            System.out.println("isExtendedEuclideanLift: false");
        }
        GenPolynomial<ModInteger>[] rel = (GenPolynomial<ModInteger>[]) new GenPolynomial[2];
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
    private static List<GenPolynomial<ModInteger>> liftDiophant(
            GenPolynomial<ModInteger> A, GenPolynomial<ModInteger> B, GenPolynomial<ModInteger> C, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B + ", C = " + C);
        }
        List<GenPolynomial<ModInteger>> sol = new ArrayList<>();
        GenPolynomialRing<ModInteger> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        //System.out.println("C = " + C);
        GenPolynomial<ModInteger> zero = fac.getZERO();
        for (int i = 0; i < 2; i++) {
            sol.add(zero);
        }
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        for (Monomial<ModInteger> m : C) {
            //System.out.println("monomial = " + m);
            long e = m.e.getVal(0);
            List<GenPolynomial<ModInteger>> S = liftDiophant(A, B, e, k);
            //System.out.println("Se = " + S);
            ModInteger a = m.c;
            //System.out.println("C.fac = " + fac.toScript());
            a = fac.coFac.fromInteger(a.getSymmetricInteger().getVal());
            int i = 0;
            for (GenPolynomial<ModInteger> d : S) {
                //System.out.println("d = " + d);
                d = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, d));
                d = d.multiply(a);
                d = sol.get(i).sum(d);
                //System.out.println("d = " + d);
                sol.set(i++, d);
            }
            //System.out.println("sol = " + sol + ", for " + m);
        }
        //GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<JasBigInteger>(new JasBigInteger(), fac);
        A = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModInteger.integerFromModularCoefficients(ifac, A));
        B = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModInteger.integerFromModularCoefficients(ifac, B));
        C = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModInteger.integerFromModularCoefficients(ifac, C));
        GenPolynomial<ModInteger> y = B.multiply(sol.get(0)).sum(A.multiply(sol.get(1)));
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
    private static List<GenPolynomial<ModInteger>> liftDiophant(
            GenPolynomial<ModInteger> A, GenPolynomial<ModInteger> B, long e, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B);
        }
        List<GenPolynomial<ModInteger>> sol = new ArrayList<>();
        GenPolynomialRing<ModInteger> fac = A.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // lift EE relation to p^k
        GenPolynomial<ModInteger>[] lee = liftExtendedEuclidean(B, A, k);
        GenPolynomial<ModInteger> s1 = lee[0];
        GenPolynomial<ModInteger> s2 = lee[1];
        if (e == 0L) {
            sol.add(s1);
            sol.add(s2);
            //System.out.println("sol@0 = " + sol);
            return sol;
        }
        fac = s1.ring;
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        A = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModInteger.integerFromModularCoefficients(ifac, A));
        B = PolyUtil.fromIntegerCoefficients(fac, PolyUtil_ModInteger.integerFromModularCoefficients(ifac, B));

        GenPolynomial<ModInteger> xe = fac.univariate(0, e);
        GenPolynomial<ModInteger> q = s1.multiply(xe);
        GenPolynomial<ModInteger>[] QR = q.quotientRemainder(A);
        q = QR[0];
        //System.out.println("ee coeff qr = " + Arrays.toString(QR));
        GenPolynomial<ModInteger> r1 = QR[1];
        GenPolynomial<ModInteger> r2 = s2.multiply(xe).sum(q.multiply(B));
        //System.out.println("r1 = " + r1 + ", r2 = " + r2);
        sol.add(r1);
        sol.add(r2);
        //System.out.println("sol@"+ e + " = " + sol);
        GenPolynomial<ModInteger> y = B.multiply(r1).sum(A.multiply(r2));
        if (!y.equals(xe)) {
            System.out.println("A = " + A + ", B = " + B);
            System.out.println("r1 = " + r1 + ", r2 = " + r2);
            System.out.println("Error: A*r1 + B*r2 = " + y);
        }
        return sol;
    }
}
