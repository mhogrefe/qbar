package jas.ufd;

import jas.arith.*;
import jas.poly.*;
import jas.structure.RingElem;

import java.util.ArrayList;
import java.util.List;

/**
 * Hensel utilities for ufd.
 *
 * @author Heinz Kredel
 */

class HenselUtil {


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
    @Deprecated
    private static <MOD extends RingElem<MOD> & Modular> GenPolynomial<MOD>[] liftExtendedEuclidean(
            GenPolynomial<MOD> A, GenPolynomial<MOD> B, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B);
        }
        GenPolynomialRing<MOD> fac = A.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // start with extended Euclidean relation mod p
        GenPolynomial<MOD>[] gst;
        try {
            gst = A.egcd(B);
            if (!gst[0].isONE()) {
                throw new RuntimeException("A and B not coprime, gcd = " + gst[0] + ", A = " + A + ", B = " + B);
            }
        } catch (ArithmeticException e) {
            throw new RuntimeException("coefficient error " + e);
        }
        GenPolynomial<MOD> S = gst[1];
        GenPolynomial<MOD> T = gst[2];

        // setup integer polynomial ring
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        GenPolynomial<JasBigInteger> one = ifac.getONE();
        GenPolynomial<JasBigInteger> Ai = PolyUtil.integerFromModularCoefficients(ifac, A);
        GenPolynomial<JasBigInteger> Bi = PolyUtil.integerFromModularCoefficients(ifac, B);
        GenPolynomial<JasBigInteger> Si = PolyUtil.integerFromModularCoefficients(ifac, S);
        GenPolynomial<JasBigInteger> Ti = PolyUtil.integerFromModularCoefficients(ifac, T);
        //System.out.println("Ai = " + Ai);
        //System.out.println("Bi = " + Bi);
        //System.out.println("Si = " + Si);
        //System.out.println("Ti = " + Ti);

        // approximate mod p^i
        ModularRingFactory<MOD> mcfac = (ModularRingFactory<MOD>) fac.coFac;
        JasBigInteger p = mcfac.getIntegerModul();
        JasBigInteger modul = p;
        GenPolynomialRing<MOD> mfac; // = new GenPolynomialRing<MOD>(mcfac, fac);
        for (int i = 1; i < k; i++) {
            // e = 1 - s a - t b in Z[x]
            GenPolynomial<JasBigInteger> e = one.subtract(Si.multiply(Ai)).subtract(Ti.multiply(Bi));
            //System.out.println("\ne = " + e);
            if (e.isZERO()) {
                break;
            }
            e = e.divide(modul);
            // move to Z_p[x] and compute next approximation
            GenPolynomial<MOD> c = PolyUtil.fromIntegerCoefficients(fac, e);
            //System.out.println("c = " + c + ": " + c.ring.coFac);
            GenPolynomial<MOD> s = S.multiply(c);
            GenPolynomial<MOD> t = T.multiply(c);
            //System.out.println("s = " + s + ": " + s.ring.coFac);
            //System.out.println("t = " + t + ": " + t.ring.coFac);

            GenPolynomial<MOD>[] QR = s.quotientRemainder(B); // watch for ordering
            GenPolynomial<MOD> q = QR[0];
            s = QR[1];
            t = t.sum(q.multiply(A));
            //System.out.println("s = " + s + ": " + s.ring.coFac);
            //System.out.println("t = " + t + ": " + t.ring.coFac);

            GenPolynomial<JasBigInteger> si = PolyUtil.integerFromModularCoefficients(ifac, s);
            GenPolynomial<JasBigInteger> ti = PolyUtil.integerFromModularCoefficients(ifac, t);
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
        List<GenPolynomial<MOD>> AP = new ArrayList<>();
        AP.add(B);
        AP.add(A);
        List<GenPolynomial<MOD>> SP = new ArrayList<>();
        SP.add(S);
        SP.add(T);
        if (!HenselUtil.isExtendedEuclideanLift(AP, SP)) {
            System.out.println("isExtendedEuclideanLift: false");
        }
        GenPolynomial<MOD>[] rel = (GenPolynomial<MOD>[]) new GenPolynomial[2];
        rel[0] = S;
        rel[1] = T;
        return rel;
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
    @Deprecated
    private static <MOD extends RingElem<MOD> & Modular> List<GenPolynomial<MOD>> liftDiophant(
            GenPolynomial<MOD> A, GenPolynomial<MOD> B, long e, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B);
        }
        List<GenPolynomial<MOD>> sol = new ArrayList<>();
        GenPolynomialRing<MOD> fac = A.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // lift EE relation to p^k
        GenPolynomial<MOD>[] lee = liftExtendedEuclidean(B, A, k);
        GenPolynomial<MOD> s1 = lee[0];
        GenPolynomial<MOD> s2 = lee[1];
        if (e == 0L) {
            sol.add(s1);
            sol.add(s2);
            //System.out.println("sol@0 = " + sol);
            return sol;
        }
        fac = s1.ring;
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        A = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, A));
        B = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, B));

        GenPolynomial<MOD> xe = fac.univariate(0, e);
        GenPolynomial<MOD> q = s1.multiply(xe);
        GenPolynomial<MOD>[] QR = q.quotientRemainder(A);
        q = QR[0];
        //System.out.println("ee coeff qr = " + Arrays.toString(QR));
        GenPolynomial<MOD> r1 = QR[1];
        GenPolynomial<MOD> r2 = s2.multiply(xe).sum(q.multiply(B));
        //System.out.println("r1 = " + r1 + ", r2 = " + r2);
        sol.add(r1);
        sol.add(r2);
        //System.out.println("sol@"+ e + " = " + sol);
        GenPolynomial<MOD> y = B.multiply(r1).sum(A.multiply(r2));
        if (!y.equals(xe)) {
            System.out.println("A = " + A + ", B = " + B);
            System.out.println("r1 = " + r1 + ", r2 = " + r2);
            System.out.println("Error: A*r1 + B*r2 = " + y);
        }
        return sol;
    }

    /**
     * Modular extended Euclidean relation lifting test.
     *
     * @param A list of GenPolynomials
     * @param S = [s_0,...,s_{n-1}] list of GenPolynomial
     * @return true if prod_{0,...,n-1} s_i * B_i = 1 mod p^e, with B_i =
     * prod_{i!=j} A_j, else false.
     */
    public static <MOD extends RingElem<MOD> & Modular> boolean isExtendedEuclideanLift(
            List<GenPolynomial<MOD>> A, List<GenPolynomial<MOD>> S) {
        GenPolynomialRing<MOD> fac = A.get(0).ring;
        GenPolynomial<MOD> C = fac.getONE();
        return isDiophantLift(A, S, C);
    }


    /**
     * Modular Diophant relation lifting test.
     *
     * @param A list of GenPolynomials
     * @param S = [s_0,...,s_{n-1}] list of GenPolynomials
     * @param C = GenPolynomial
     * @return true if prod_{0,...,n-1} s_i * B_i = C mod p^k, with B_i =
     * prod_{i!=j} A_j, else false.
     */
    private static <MOD extends RingElem<MOD> & Modular> boolean isDiophantLift(
            List<GenPolynomial<MOD>> A, List<GenPolynomial<MOD>> S, GenPolynomial<MOD> C) {
        GenPolynomialRing<MOD> fac = A.get(0).ring;
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        List<GenPolynomial<MOD>> B = new ArrayList<>(A.size());
        int i = 0;
        for (GenPolynomial<MOD> ai : A) {
            GenPolynomial<MOD> b = fac.getONE();
            int j = 0;
            for (GenPolynomial<MOD> aj : A) {
                if (i != j /*!ai.equals(aj)*/) {
                    b = b.multiply(aj);
                }
                j++;
            }
            //System.out.println("b = " + b);
            b = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, b));
            B.add(b);
            i++;
        }
        //System.out.println("B = " + B);
        // check mod p^e
        GenPolynomial<MOD> t = fac.getZERO();
        i = 0;
        for (GenPolynomial<MOD> a : B) {
            GenPolynomial<MOD> b = S.get(i++);
            b = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, b));
            GenPolynomial<MOD> s = a.multiply(b);
            t = t.sum(s);
        }
        return t.equals(C);
    }


}
