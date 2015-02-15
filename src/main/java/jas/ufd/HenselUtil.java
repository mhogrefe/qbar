package jas.ufd;

import jas.arith.*;
import jas.poly.*;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Hensel utilities for ufd.
 *
 * @author Heinz Kredel
 */

class HenselUtil {
    /**
     * Modular quadratic Hensel lifting algorithm on coefficients. Let p =
     * A.ring.coFac.modul() = B.ring.coFac.modul() and assume C == A*B mod p
     * with gcd(A,B) == 1 mod p and S A + T B == 1 mod p. See algorithm 6.1. in
     * Geddes et.al. and algorithms 3.5.{5,6} in Cohen. Quadratic version, as it
     * also lifts S A + T B == 1 mod p^{e+1}.
     *
     * @param C GenPolynomial
     * @param A GenPolynomial
     * @param B other GenPolynomial
     * @param S GenPolynomial
     * @param T GenPolynomial
     * @param M bound on the coefficients of A1 and B1 as factors of C.
     * @return [A1, B1] = lift(C,A,B), with C = A1 * B1.
     */
    @SuppressWarnings("unchecked")
    private static <MOD extends RingElem<MOD> & Modular> HenselApprox<MOD> liftHenselQuadratic(
            GenPolynomial<JasBigInteger> C, JasBigInteger M, GenPolynomial<MOD> A, GenPolynomial<MOD> B,
            GenPolynomial<MOD> S, GenPolynomial<MOD> T) {
        if (C == null || C.isZERO()) {
            return new HenselApprox<>(C, C, A, B);
        }
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero");
        }
        GenPolynomialRing<JasBigInteger> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // setup factories
        GenPolynomialRing<MOD> pfac = A.ring;
        RingFactory<MOD> p = pfac.coFac;
        ModularRingFactory<MOD> P = (ModularRingFactory<MOD>) p;
        ModularRingFactory<MOD> Q = (ModularRingFactory<MOD>) p;
        JasBigInteger Qi = Q.getIntegerModul();
        JasBigInteger M2 = M.multiply(M.fromInteger(2));
        JasBigInteger Mq = Qi;
        GenPolynomialRing<MOD> qfac;
        qfac = new GenPolynomialRing<>(Q, pfac);

        // normalize c and a, b factors, assert p is prime
        GenPolynomial<JasBigInteger> Ai;
        GenPolynomial<JasBigInteger> Bi;
        JasBigInteger c = C.leadingBaseCoefficient();
        C = C.multiply(c); // sic
        MOD a = A.leadingBaseCoefficient();
        if (!a.isONE()) { // A = A.monic();
            A = A.divide(a);
            S = S.multiply(a);
        }
        MOD b = B.leadingBaseCoefficient();
        if (!b.isONE()) { // B = B.monic();
            B = B.divide(b);
            T = T.multiply(b);
        }
        MOD cm = P.fromInteger(c.getVal());
        A = A.multiply(cm);
        B = B.multiply(cm);
        T = T.divide(cm);
        S = S.divide(cm);
        Ai = PolyUtil.integerFromModularCoefficients(fac, A);
        Bi = PolyUtil.integerFromModularCoefficients(fac, B);
        // replace leading base coefficients
        ExpVector ea = Ai.leadingExpVector();
        ExpVector eb = Bi.leadingExpVector();
        Ai.doPutToMap(ea, c);
        Bi.doPutToMap(eb, c);

        // polynomials mod p
        GenPolynomial<MOD> Ap;
        GenPolynomial<MOD> Bp;
        GenPolynomial<MOD> A1p = A;
        GenPolynomial<MOD> B1p = B;
        GenPolynomial<MOD> Ep;
        GenPolynomial<MOD> Sp = S;
        GenPolynomial<MOD> Tp = T;

        // polynomials mod q
        GenPolynomial<MOD> Aq;
        GenPolynomial<MOD> Bq;

        // polynomials over the integers
        GenPolynomial<JasBigInteger> E;
        GenPolynomial<JasBigInteger> Ea;
        GenPolynomial<JasBigInteger> Eb;
        GenPolynomial<JasBigInteger> Ea1;
        GenPolynomial<JasBigInteger> Eb1;
        GenPolynomial<JasBigInteger> Si;
        GenPolynomial<JasBigInteger> Ti;

        Si = PolyUtil.integerFromModularCoefficients(fac, S);
        Ti = PolyUtil.integerFromModularCoefficients(fac, T);

        Aq = PolyUtil.fromIntegerCoefficients(qfac, Ai);
        Bq = PolyUtil.fromIntegerCoefficients(qfac, Bi);

        while (Mq.compareTo(M2) < 0) {
            // compute E=(C-AB)/q over the integers
            E = C.subtract(Ai.multiply(Bi));
            if (E.isZERO()) {
                break;
            }
            E = E.divide(Qi);
            // E mod p
            Ep = PolyUtil.fromIntegerCoefficients(qfac, E);
            //
            //if (Ep.isZERO()) {
            //System.out.println("leaving on zero error");
            //??
            //??break;
            //}

            // construct approximation mod p
            Ap = Sp.multiply(Ep); // S,T ++ T,S
            Bp = Tp.multiply(Ep);
            GenPolynomial<MOD>[] QR;
            //
            QR = Ap.quotientRemainder(Bq);
            GenPolynomial<MOD> Qp;
            GenPolynomial<MOD> Rp;
            Qp = QR[0];
            Rp = QR[1];
            //
            A1p = Rp;
            B1p = Bp.sum(Aq.multiply(Qp));

            // construct q-adic approximation, convert to integer
            Ea = PolyUtil.integerFromModularCoefficients(fac, A1p);
            Eb = PolyUtil.integerFromModularCoefficients(fac, B1p);
            Ea1 = Ea.multiply(Qi);
            Eb1 = Eb.multiply(Qi);
            Ea = Ai.sum(Eb1); // Eb1 and Ea1 are required
            Eb = Bi.sum(Ea1); //--------------------------
            assert (Ea.degree(0) + Eb.degree(0) <= C.degree(0));
            //if ( Ea.degree(0)+Eb.degree(0) > C.degree(0) ) { // debug
            //   throw new RuntimeException("deg(A)+deg(B) > deg(C)");
            //}
            Ai = Ea;
            Bi = Eb;

            // gcd representation factors error --------------------------------
            // compute E=(1-SA-TB)/q over the integers
            E = fac.getONE();
            E = E.subtract(Si.multiply(Ai)).subtract(Ti.multiply(Bi));
            E = E.divide(Qi);
            // E mod q
            Ep = PolyUtil.fromIntegerCoefficients(qfac, E);
            //

            // construct approximation mod q
            Ap = Sp.multiply(Ep); // S,T ++ T,S
            Bp = Tp.multiply(Ep);
            QR = Bp.quotientRemainder(Aq); // Ai == A mod p ?
            Qp = QR[0];
            Rp = QR[1];
            B1p = Rp;
            A1p = Ap.sum(Bq.multiply(Qp));

            // construct q-adic approximation, convert to integer
            Ea = PolyUtil.integerFromModularCoefficients(fac, A1p);
            Eb = PolyUtil.integerFromModularCoefficients(fac, B1p);
            Ea1 = Ea.multiply(Qi);
            Eb1 = Eb.multiply(Qi);
            Ea = Si.sum(Ea1); // Eb1 and Ea1 are required
            Eb = Ti.sum(Eb1); //--------------------------
            Si = Ea;
            Ti = Eb;

            // prepare for next iteration
            Mq = Qi;
            Qi = Q.getIntegerModul().multiply(Q.getIntegerModul());
            if (ModLongRing.MAX_LONG.compareTo(Qi.getVal()) > 0) {
                Q = (ModularRingFactory) new ModLongRing(Qi.getVal());
            } else {
                Q = (ModularRingFactory) new ModIntegerRing(Qi.getVal());
            }
            //Q = new ModIntegerRing(Qi.getVal());
            //System.out.println("Q = " + Q + ", from Q = " + Mq);

            qfac = new GenPolynomialRing<>(Q, pfac);

            Aq = PolyUtil.fromIntegerCoefficients(qfac, Ai);
            Bq = PolyUtil.fromIntegerCoefficients(qfac, Bi);
            Sp = PolyUtil.fromIntegerCoefficients(qfac, Si);
            Tp = PolyUtil.fromIntegerCoefficients(qfac, Ti);
        }
        GreatestCommonDivisorAbstract<JasBigInteger> ufd = new GreatestCommonDivisorPrimitive<>();

        // remove normalization if possible
        JasBigInteger ai = ufd.baseContent(Ai);
        Ai = Ai.divide(ai);
        JasBigInteger bi;
        try {
            bi = c.divide(ai);
            Bi = Bi.divide(bi); // divide( c/a )
        } catch (RuntimeException e) {
            throw new RuntimeException("no exact lifting possible " + e);
        }
        return new HenselApprox<>(Ai, Bi, A1p, B1p);
    }


    /**
     * Modular quadratic Hensel lifting algorithm on coefficients. Let p =
     * A.ring.coFac.modul() = B.ring.coFac.modul() and assume C == A*B mod p
     * with gcd(A,B) == 1 mod p. See algorithm 6.1. in Geddes et.al. and
     * algorithms 3.5.{5,6} in Cohen. Quadratic version.
     *
     * @param C GenPolynomial
     * @param A GenPolynomial
     * @param B other GenPolynomial
     * @param M bound on the coefficients of A1 and B1 as factors of C.
     * @return [A1, B1] = lift(C,A,B), with C = A1 * B1.
     */
    @SuppressWarnings("unchecked")
    public static <MOD extends RingElem<MOD> & Modular> HenselApprox<MOD> liftHenselQuadratic(
            GenPolynomial<JasBigInteger> C, JasBigInteger M, GenPolynomial<MOD> A, GenPolynomial<MOD> B) {
        if (C == null || C.isZERO()) {
            return new HenselApprox<>(C, C, A, B);
        }
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero");
        }
        GenPolynomialRing<JasBigInteger> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        // one Hensel step on part polynomials
        try {
            GenPolynomial<MOD>[] gst = A.egcd(B);
            if (!gst[0].isONE()) {
                throw new RuntimeException("A and B not coprime, gcd = " + gst[0] + ", A = " + A + ", B = " + B);
            }
            GenPolynomial<MOD> s = gst[1];
            GenPolynomial<MOD> t = gst[2];
            return HenselUtil.liftHenselQuadratic(C, M, A, B, s, t);
        } catch (ArithmeticException e) {
            throw new RuntimeException("coefficient error " + e);
        }
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
     * Constructing and lifting algorithm for extended Euclidean relation. Let p
     * = A_i.ring.coFac.modul() and assume gcd(A_i,A_j) == 1 mod p, i != j.
     *
     * @param A list of modular GenPolynomials
     * @param k desired approximation exponent p^k.
     * @return [s_0, ..., s_n-1] with sum_i s_i * B_i = 1 mod p^k, with B_i =
     * prod_{i!=j} A_j.
     */
    private static <MOD extends RingElem<MOD> & Modular> List<GenPolynomial<MOD>> liftExtendedEuclidean(
            List<GenPolynomial<MOD>> A, long k) {
        if (A == null || A.size() == 0) {
            throw new IllegalArgumentException("A must be non null and non empty");
        }
        GenPolynomialRing<MOD> fac = A.get(0).ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        GenPolynomial<MOD> zero = fac.getZERO();
        int r = A.size();
        List<GenPolynomial<MOD>> Q = new ArrayList<>(r);
        for (GenPolynomial<MOD> aA1 : A) {
            Q.add(zero);
        }
        //System.out.println("A = " + A);
        Q.set(r - 2, A.get(r - 1));
        for (int j = r - 3; j >= 0; j--) {
            GenPolynomial<MOD> q = A.get(j + 1).multiply(Q.get(j + 1));
            Q.set(j, q);
        }
        //System.out.println("Q = " + Q);
        List<GenPolynomial<MOD>> B = new ArrayList<>(r + 1);
        List<GenPolynomial<MOD>> lift = new ArrayList<>(r);
        for (GenPolynomial<MOD> aA : A) {
            B.add(zero);
            lift.add(zero);
        }
        GenPolynomial<MOD> one = fac.getONE();
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        B.add(0, one);
        //System.out.println("B(0) = " + B.get(0));
        GenPolynomial<MOD> b = one;
        for (int j = 0; j < r - 1; j++) {
            //System.out.println("Q("+(j)+") = " + Q.get(j));
            //System.out.println("A("+(j)+") = " + A.get(j));
            //System.out.println("B("+(j)+") = " + B.get(j));
            List<GenPolynomial<MOD>> S = liftDiophant(Q.get(j), A.get(j), B.get(j), k);
            //System.out.println("\nSb = " + S);
            b = S.get(0);
            GenPolynomial<MOD> bb = PolyUtil.fromIntegerCoefficients(fac, PolyUtil
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
     * Modular diophantine equation solution and lifting algorithm. Let p =
     * A_i.ring.coFac.modul() and assume gcd(A,B) == 1 mod p.
     *
     * @param A modular GenPolynomial, mod p^k
     * @param B modular GenPolynomial, mod p^k
     * @param C modular GenPolynomial, mod p^k
     * @param k desired approximation exponent p^k.
     * @return [s, t] with s A' + t B' = C mod p^k, with A' = B, B' = A.
     */
    private static <MOD extends RingElem<MOD> & Modular> List<GenPolynomial<MOD>> liftDiophant(
            GenPolynomial<MOD> A, GenPolynomial<MOD> B, GenPolynomial<MOD> C, long k) {
        if (A == null || A.isZERO() || B == null || B.isZERO()) {
            throw new IllegalArgumentException("A and B must be nonzero, A = " + A + ", B = " + B + ", C = " + C);
        }
        List<GenPolynomial<MOD>> sol = new ArrayList<>();
        GenPolynomialRing<MOD> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        //System.out.println("C = " + C);
        GenPolynomial<MOD> zero = fac.getZERO();
        for (int i = 0; i < 2; i++) {
            sol.add(zero);
        }
        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        for (Monomial<MOD> m : C) {
            //System.out.println("monomial = " + m);
            long e = m.e.getVal(0);
            List<GenPolynomial<MOD>> S = liftDiophant(A, B, e, k);
            //System.out.println("Se = " + S);
            MOD a = m.c;
            //System.out.println("C.fac = " + fac.toScript());
            a = fac.coFac.fromInteger(a.getSymmetricInteger().getVal());
            int i = 0;
            for (GenPolynomial<MOD> d : S) {
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
        A = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, A));
        B = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, B));
        C = PolyUtil.fromIntegerCoefficients(fac, PolyUtil.integerFromModularCoefficients(ifac, C));
        GenPolynomial<MOD> y = B.multiply(sol.get(0)).sum(A.multiply(sol.get(1)));
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
    private static <MOD extends RingElem<MOD> & Modular> boolean isExtendedEuclideanLift(
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
    @Deprecated
    public static <MOD extends RingElem<MOD> & Modular>
    List<GenPolynomial<MOD>> liftHenselMonic(GenPolynomial<JasBigInteger> C, List<GenPolynomial<MOD>> F, long k) {
        if (C == null || C.isZERO() || F == null || F.size() == 0) {
            throw new IllegalArgumentException("C must be nonzero and F must be nonempty");
        }
        GenPolynomialRing<JasBigInteger> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        List<GenPolynomial<MOD>> lift = new ArrayList<>(F.size());
        GenPolynomialRing<MOD> pfac = F.get(0).ring;
        RingFactory<MOD> pcfac = pfac.coFac;
        ModularRingFactory<MOD> PF = (ModularRingFactory<MOD>) pcfac;
        JasBigInteger P = PF.getIntegerModul();
        int n = F.size();
        if (n == 1) { // lift F_0, this case will probably never be used
            GenPolynomial<MOD> f = F.get(0);
            ModularRingFactory<MOD> mcfac;
            if (ModLongRing.MAX_LONG.compareTo(P.getVal()) > 0) {
                mcfac = (ModularRingFactory) new ModLongRing(P.getVal());
            } else {
                mcfac = (ModularRingFactory) new ModIntegerRing(P.getVal());
            }
            GenPolynomialRing<MOD> mfac = new GenPolynomialRing<>(mcfac, fac);
            f = PolyUtil.fromIntegerCoefficients(mfac, PolyUtil.integerFromModularCoefficients(fac, f));
            lift.add(f);
            return lift;
        }

        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        List<GenPolynomial<JasBigInteger>> Fi = PolyUtil.integerFromModularCoefficients(ifac, F);

        List<GenPolynomial<MOD>> S = liftExtendedEuclidean(F, k + 1); // lift works for any k, TODO: use this
        // adjust coefficients
        List<GenPolynomial<MOD>> Sx = PolyUtil.fromIntegerCoefficients(pfac, PolyUtil
                .integerFromModularCoefficients(ifac, S));
        try {
            HenselUtil.isExtendedEuclideanLift(F, Sx);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        List<GenPolynomial<JasBigInteger>> Si = PolyUtil.integerFromModularCoefficients(ifac, S);

        // approximate mod p^i
        ModularRingFactory<MOD> mcfac = PF;
        JasBigInteger p = mcfac.getIntegerModul();
        JasBigInteger modul = p;
        GenPolynomialRing<MOD> mfac = new GenPolynomialRing<>(mcfac, fac);
        List<GenPolynomial<MOD>> Sp = PolyUtil.fromIntegerCoefficients(mfac, Si);
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
            GenPolynomial<MOD> c = PolyUtil.fromIntegerCoefficients(mfac, e);
            //System.out.println("c = " + c + ": " + c.ring.coFac);

            List<GenPolynomial<MOD>> s = new ArrayList<>(S.size());
            int j = 0;
            for (GenPolynomial<MOD> f : Sp) {
                f = f.multiply(c);
                //System.out.println("f = " + f + " : " + f.ring.coFac);
                //System.out.println("F,i = " + F.get(j) + " : " + F.get(j).ring.coFac);
                f = f.remainder(F.get(j++));
                //System.out.println("f = " + f + " : " + f.ring.coFac);
                s.add(f);
            }
            //System.out.println("s = " + s);
            List<GenPolynomial<JasBigInteger>> si = PolyUtil.integerFromModularCoefficients(ifac, s);
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
    public static List<GenPolynomial<ModInteger>> liftHenselMonic_Integer(GenPolynomial<JasBigInteger> C, List<GenPolynomial<ModInteger>> F, long k) {
        if (C == null || C.isZERO() || F == null || F.size() == 0) {
            throw new IllegalArgumentException("C must be nonzero and F must be nonempty");
        }
        GenPolynomialRing<JasBigInteger> fac = C.ring;
        if (fac.nvar != 1) { // todo assert
            throw new IllegalArgumentException("polynomial ring not univariate");
        }
        List<GenPolynomial<ModInteger>> lift = new ArrayList<>(F.size());
        GenPolynomialRing<ModInteger> pfac = F.get(0).ring;
        RingFactory<ModInteger> pcfac = pfac.coFac;
        ModularRingFactory<ModInteger> PF = (ModularRingFactory<ModInteger>) pcfac;
        JasBigInteger P = PF.getIntegerModul();
        int n = F.size();
        if (n == 1) { // lift F_0, this case will probably never be used
            GenPolynomial<ModInteger> f = F.get(0);
            ModularRingFactory<ModInteger> mcfac;
            if (ModLongRing.MAX_LONG.compareTo(P.getVal()) > 0) {
                mcfac = (ModularRingFactory) new ModLongRing(P.getVal());
            } else {
                mcfac = (ModularRingFactory) new ModIntegerRing(P.getVal());
            }
            GenPolynomialRing<ModInteger> mfac = new GenPolynomialRing<>(mcfac, fac);
            f = PolyUtil.fromIntegerCoefficients(mfac, PolyUtil.integerFromModularCoefficients_Integer(fac, f));
            lift.add(f);
            return lift;
        }

        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        List<GenPolynomial<JasBigInteger>> Fi = PolyUtil.integerFromModularCoefficients_Integer(ifac, F);

        List<GenPolynomial<ModInteger>> S = liftExtendedEuclidean(F, k + 1); // lift works for any k, TODO: use this
        // adjust coefficients
        List<GenPolynomial<ModInteger>> Sx = PolyUtil.fromIntegerCoefficients(pfac, PolyUtil
                .integerFromModularCoefficients(ifac, S));
        try {
            HenselUtil.isExtendedEuclideanLift(F, Sx);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        List<GenPolynomial<JasBigInteger>> Si = PolyUtil.integerFromModularCoefficients_Integer(ifac, S);

        // approximate mod p^i
        ModularRingFactory<ModInteger> mcfac = PF;
        JasBigInteger p = mcfac.getIntegerModul();
        JasBigInteger modul = p;
        GenPolynomialRing<ModInteger> mfac = new GenPolynomialRing<>(mcfac, fac);
        List<GenPolynomial<ModInteger>> Sp = PolyUtil.fromIntegerCoefficients(mfac, Si);
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
            GenPolynomial<ModInteger> c = PolyUtil.fromIntegerCoefficients(mfac, e);
            //System.out.println("c = " + c + ": " + c.ring.coFac);

            List<GenPolynomial<ModInteger>> s = new ArrayList<>(S.size());
            int j = 0;
            for (GenPolynomial<ModInteger> f : Sp) {
                f = f.multiply(c);
                //System.out.println("f = " + f + " : " + f.ring.coFac);
                //System.out.println("F,i = " + F.get(j) + " : " + F.get(j).ring.coFac);
                f = f.remainder(F.get(j++));
                //System.out.println("f = " + f + " : " + f.ring.coFac);
                s.add(f);
            }
            //System.out.println("s = " + s);
            List<GenPolynomial<JasBigInteger>> si = PolyUtil.integerFromModularCoefficients(ifac, s);
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
    public static List<GenPolynomial<ModLong>> liftHenselMonic_Long(GenPolynomial<JasBigInteger> C, List<GenPolynomial<ModLong>> F, long k) {
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
            f = PolyUtil.fromIntegerCoefficients(mfac, PolyUtil.integerFromModularCoefficients_Long(fac, f));
            lift.add(f);
            return lift;
        }

        GenPolynomialRing<JasBigInteger> ifac = new GenPolynomialRing<>(new JasBigInteger(), fac);
        List<GenPolynomial<JasBigInteger>> Fi = PolyUtil.integerFromModularCoefficients_Long(ifac, F);

        List<GenPolynomial<ModLong>> S = liftExtendedEuclidean(F, k + 1); // lift works for any k, TODO: use this
        // adjust coefficients
        List<GenPolynomial<ModLong>> Sx = PolyUtil.fromIntegerCoefficients(pfac, PolyUtil
                .integerFromModularCoefficients(ifac, S));
        try {
            HenselUtil.isExtendedEuclideanLift(F, Sx);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        List<GenPolynomial<JasBigInteger>> Si = PolyUtil.integerFromModularCoefficients_Long(ifac, S);

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
            List<GenPolynomial<JasBigInteger>> si = PolyUtil.integerFromModularCoefficients_Long(ifac, s);
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
}