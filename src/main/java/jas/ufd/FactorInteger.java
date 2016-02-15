package jas.ufd;

import jas.arith.*;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.PolyUtil;
import jas.structure.Power;
import jas.util.KsubSet;

import java.math.BigInteger;
import java.util.*;

public class FactorInteger {
    final GreatestCommonDivisorModular engine;

    private final SquarefreeAbstract<JasBigInteger> sengine;

    /**
     * Factorization engine for modular base coefficients.
     */
    private final FactorModular<ModLong> mfactor;

    /**
     * Gcd engine for modular base coefficients.
     */
    private final GreatestCommonDivisorAbstract<ModLong> mengine;

    @SuppressWarnings("unchecked")
    public FactorInteger() {
        engine = new GreatestCommonDivisorModular();
        sengine = new SquarefreeRingChar0<>(JasBigInteger.ONE);
        mfactor = new FactorModular(new ModLongRing(13));
        mengine = new GreatestCommonDivisorModEval<>();
    }

    /**
     * Univariate GenPolynomial factorization.
     *
     * @param P GenPolynomial in one variable.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i**e_i.
     */
    public SortedMap<GenPolynomial<JasBigInteger>, Long> baseFactors(GenPolynomial<JasBigInteger> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        GenPolynomialRing<JasBigInteger> pfac = P.ring;
        SortedMap<GenPolynomial<JasBigInteger>, Long> factors = new TreeMap<>();
        if (P.isZERO()) {
            return factors;
        }
        if (P.isConstant()) {
            factors.put(P, 1L);
            return factors;
        }
        JasBigInteger c;
        if (pfac.coFac.isField()) { //pfac.characteristic().signum() > 0
            c = P.leadingBaseCoefficient();
        } else {
            c = engine.baseContent(P);
            // move sign to the content
            if (P.signum() < 0 && c.signum() > 0) {
                c = c.negate();
                //P = P.negate();
            }
        }
        if (!c.isONE()) {
            GenPolynomial<JasBigInteger> pc = pfac.getONE().multiply(c);
            factors.put(pc, 1L);
            P = P.divide(c); // make primitive or monic
        }
        SortedMap<GenPolynomial<JasBigInteger>, Long> facs = sengine.baseSquarefreeFactors(P);
        if (facs == null || facs.size() == 0) {
            facs = new TreeMap<>();
            facs.put(P, 1L);
        }
        for (Map.Entry<GenPolynomial<JasBigInteger>, Long> me : facs.entrySet()) {
            GenPolynomial<JasBigInteger> g = me.getKey();
            Long k = me.getValue(); //facs.get(g);
            //System.out.println("g       = " + g);
            if (pfac.coFac.isField() && !g.leadingBaseCoefficient().isONE()) {
                g = g.monic(); // how can this happen?
            }
            if (g.degree() <= 1) {
                if (!g.isONE()) {
                    factors.put(g, k);
                }
            } else {
                List<GenPolynomial<JasBigInteger>> sfacs = baseFactorsSquarefree(g);
                for (GenPolynomial<JasBigInteger> h : sfacs) {
                    Long j = factors.get(h); // evtl. constants
                    if (j != null) {
                        k += j;
                    }
                    if (!h.isONE()) {
                        factors.put(h, k);
                    }
                }
            }
        }
        //System.out.println("factors = " + factors);
        return factors;
    }

    /**
     * GenPolynomial base factorization of a squarefree polynomial.
     *
     * @param P squarefree and primitive! GenPolynomial.
     * @return [p_1, ..., p_k] with P = prod_{i=1, ..., k} p_i.
     */
    @SuppressWarnings("unchecked")
    public List<GenPolynomial<JasBigInteger>> baseFactorsSquarefree(GenPolynomial<JasBigInteger> P) {
        if (P == null) {
            throw new IllegalArgumentException();
        }
        List<GenPolynomial<JasBigInteger>> factors = new ArrayList<>();
        if (P.isZERO()) {
            return factors;
        }
        if (P.isONE()) {
            factors.add(P);
            return factors;
        }
        GenPolynomialRing<JasBigInteger> pfac = P.ring;
        if (!engine.baseContent(P).isONE()) {
            throw new IllegalArgumentException();
        }
        if (P.degree() <= 1L) { // linear is irreducible
            factors.add(P);
            return factors;
        }
        // compute norm
        JasBigInteger an = P.maxNorm();
        JasBigInteger ac = P.leadingBaseCoefficient();
        //compute factor coefficient bounds
        long degv = P.degreeVector();
        int degi = (int) P.degree();
        JasBigInteger M = an.multiply(PolyUtil.factorBound(degv));
        M = M.multiply(ac.abs().multiply(ac.fromInteger(8)));
        //System.out.println("M = " + M);
        //M = M.multiply(M); // test

        //initialize prime list and degree vector
        PrimeList primes = new PrimeList(PrimeList.Range.small);
        int pn = 30; //primes.size();
        ModularRingFactory<ModLong> cofac;
        GenPolynomial<ModLong> am = null;
        GenPolynomialRing<ModLong> mfac = null;
        final int TT = 5; // 7
        List<GenPolynomial<ModLong>>[] modfac = new List[TT];
        List<GenPolynomial<ModLong>> mlist = null;
        int i = 0;
        Iterator<BigInteger> pit = primes.iterator();
        pit.next(); // skip p = 2
        pit.next(); // skip p = 3
        ModLong nf = null;
        for (int k = 0; k < TT; k++) {
            if (k == TT - 1) { // -2
                primes = new PrimeList(PrimeList.Range.medium);
                pit = primes.iterator();
            }
            if (k == TT + 1) { // -1
                primes = new PrimeList(PrimeList.Range.large);
                pit = primes.iterator();
            }
            while (pit.hasNext()) {
                BigInteger p = pit.next();
                //System.out.println("next run ++++++++++++++++++++++++++++++++++");
                if (++i >= pn) {
                    throw new ArithmeticException("prime list exhausted");
                }
                if (ModLongRing.MAX_LONG.compareTo(p) > 0) {
                    cofac = (ModularRingFactory) new ModLongRing(p);
                } else {
                    cofac = (ModularRingFactory) new ModIntegerRing(p);
                }

                nf = cofac.fromInteger(ac.getVal());
                if (nf.isZERO()) {
                    //System.out.println("unlucky prime (nf) = " + p);
                    continue;
                }
                // initialize polynomial factory and map polynomial
                mfac = new GenPolynomialRing<>(cofac, pfac);
                am = PolyUtil.fromIntegerCoefficients(mfac, P);
                if (am.degreeVector() != degv) {
                    //System.out.println("unlucky prime (deg) = " + p);
                    continue;
                }
                GenPolynomial<ModLong> ap = PolyUtil.baseDeriviative(am);
                if (ap.isZERO()) {
                    //System.out.println("unlucky prime (a')= " + p);
                    continue;
                }
                GenPolynomial<ModLong> g = mengine.baseGcd(am, ap);
                if (g.isONE()) {
                    //System.out.println("**lucky prime = " + p);
                    break;
                }
            }
            // now am is squarefree mod p, make monic and factor mod p
            if (!nf.isONE()) {
                //System.out.println("nf = " + nf);
                am = am.divide(nf); // make monic
            }
            mlist = mfactor.baseFactorsSquarefree(am);
            if (mlist.size() <= 1) {
                factors.add(P);
                return factors;
            }
            if (!nf.isONE()) {
                GenPolynomial<ModLong> mp = mfac.getONE(); //mlist.get(0);
                //System.out.println("mp = " + mp);
                mp = mp.multiply(nf);
                //System.out.println("mp = " + mp);
                mlist.add(0, mp); // set(0,mp);
            }
            modfac[k] = mlist;
        }

        // search shortest factor list
        int min = Integer.MAX_VALUE;
        BitSet AD = null;
        for (int k = 0; k < TT; k++) {
            List<Long> ev = PolyUtil.leadingExpVector(modfac[k]);
            BitSet D = factorDegrees(ev, degi);
            if (AD == null) {
                AD = D;
            } else {
                AD.and(D);
            }
            int s = modfac[k].size();

            //System.out.println("mod s = " + s);
            if (s < min) {
                min = s;
                mlist = modfac[k];
            }
        }

        if (mlist.size() <= 1) {
            factors.add(P);
            return factors;
        }
        if (AD.cardinality() <= 2) { // only one possible factor
            factors.add(P);
            return factors;
        }
        if (P.leadingBaseCoefficient().isONE()) {
            try {
                mlist = PolyUtil.monic(mlist);
                factors = searchFactorsMonic(P, M, mlist, AD); // does now work in all cases
            } catch (RuntimeException e) {
                factors = searchFactorsNonMonic(P, M, mlist, AD);
            }
        } else {
            factors = searchFactorsNonMonic(P, M, mlist, AD);
        }
        return normalizeFactorization(factors);
    }

    /**
     * Factor search with modular Hensel lifting algorithm. Let p =
     * f_i.ring.coFac.modul() i = 0, ..., n-1 and assume C == prod_{0,...,n-1}
     * f_i mod p with ggt(f_i,f_j) == 1 mod p for i != j
     *
     * @param C GenPolynomial.
     * @param M bound on the coefficients of g_i as factors of C.
     * @param F = [f_0,...,f_{n-1}] List&lt;GenPolynomial&gt;.
     * @param D bit set of possible factor degrees.
     * @return [g_0, ..., g_{n-1}] = lift(C,F), with C = prod_{0,...,n-1} g_i mod
     * p**e. <b>Note:</b> does not work in all cases.
     */
    List<GenPolynomial<JasBigInteger>> searchFactorsMonic(
            GenPolynomial<JasBigInteger> C,
            JasBigInteger M,
            List<GenPolynomial<ModLong>> F,
            BitSet D
    ) {
        //System.out.println("*** monic factor combination ***");
        if (C == null || C.isZERO() || F == null || F.size() == 0) {
            throw new IllegalArgumentException("C must be nonzero and F must be nonempty");
        }
        GenPolynomialRing<JasBigInteger> pfac = C.ring;
        List<GenPolynomial<JasBigInteger>> factors = new ArrayList<>(F.size());
        List<GenPolynomial<ModLong>> lift;

        //MOD nf = null;
        GenPolynomial<ModLong> ct = F.get(0);
        if (ct.isConstant()) {
            //nf = ct.leadingBaseCoefficient();
            F.remove(ct);
            //System.out.println("=== nf = " + nf);
            if (F.size() <= 1) {
                factors.add(C);
                return factors;
            }
        }
        //System.out.println("modlist  = " + mlist); // includes not ldcf
        ModularRingFactory<ModLong> mcfac = (ModularRingFactory<ModLong>) ct.ring.coFac;
        JasBigInteger m = mcfac.getIntegerModul();
        long k = 1;
        JasBigInteger pi = m;
        while (pi.compareTo(M) < 0) {
            k++;
            pi = pi.multiply(m);
        }

        lift = HenselUtil.liftHenselMonic(C, F, k);
        GenPolynomialRing<ModLong> mpfac = lift.get(0).ring;
        int dl = (lift.size() + 1) / 2;
        GenPolynomial<JasBigInteger> u = C;
        for (int j = 1; j <= dl; j++) {
            KsubSet<GenPolynomial<ModLong>> ps = new KsubSet<>(lift, j);
            for (List<GenPolynomial<ModLong>> flist : ps) {
                if (!D.get((int) degreeSum(flist))) {
                    continue;
                }
                GenPolynomial<ModLong> mtrial = Power.multiply(mpfac, flist);
                mtrial.degree();
                GenPolynomial<JasBigInteger> trial = PolyUtil.integerFromModularCoefficients(pfac, mtrial);
                trial = engine.basePrimitivePart(trial);
                if (PolyUtil.baseSparsePseudoRemainder(u, trial).isZERO()) {
                    factors.add(trial);
                    u = PolyUtil.basePseudoDivide(u, trial); //u.divide( trial );
                    lift = removeOnce(lift, flist);
                    dl = (lift.size() + 1) / 2;
                    j = 0; // since j++
                    break;
                }
            }
        }
        if (!u.isONE() && !u.equals(C)) {
            factors.add(u);
        }
        if (factors.size() == 0) {
            factors.add(C);
        }
        return normalizeFactorization(factors);
    }

    static <T> List<T> removeOnce(List<T> a, List<T> b) {
        List<T> res = new ArrayList<>();
        res.addAll(a);
        b.forEach(res::remove);
        return res;
    }

    List<GenPolynomial<JasBigInteger>> normalizeFactorization(List<GenPolynomial<JasBigInteger>> F) {
        if (F == null || F.size() <= 1) {
            return F;
        }
        List<GenPolynomial<JasBigInteger>> Fp = new ArrayList<>(F.size());
        GenPolynomial<JasBigInteger> f0 = F.get(0);
        for (int i = 1; i < F.size(); i++) {
            GenPolynomial<JasBigInteger> fi = F.get(i);
            if (fi.signum() < 0) {
                fi = fi.negate();
                f0 = f0.negate();
            }
            Fp.add(fi);
        }
        if (!f0.isONE()) {
            Fp.add(0, f0);
        }
        return Fp;
    }

    /**
     * Factor search with modular Hensel lifting algorithm. Let p =
     * f_i.ring.coFac.modul() i = 0, ..., n-1 and assume C == prod_{0,...,n-1}
     * f_i mod p with ggt(f_i,f_j) == 1 mod p for i != j
     *
     * @param C GenPolynomial.
     * @param M bound on the coefficients of g_i as factors of C.
     * @param F = [f_0,...,f_{n-1}] List&lt;GenPolynomial&gt;.
     * @param D bit set of possible factor degrees.
     * @return [g_0, ..., g_{n-1}] = lift(C,F), with C = prod_{0,...,n-1} g_i mod
     * p**e.
     */
    List<GenPolynomial<JasBigInteger>> searchFactorsNonMonic(
            GenPolynomial<JasBigInteger> C,
            JasBigInteger M,
            List<GenPolynomial<ModLong>> F,
            BitSet D
    ) {
        if (C == null || C.isZERO() || F == null || F.size() == 0) {
            throw new IllegalArgumentException("C must be nonzero and F must be nonempty");
        }
        GenPolynomialRing<JasBigInteger> pfac = C.ring;
        List<GenPolynomial<JasBigInteger>> factors = new ArrayList<>(F.size());
        List<GenPolynomial<ModLong>> mlist = F;
        ModLong nf;
        GenPolynomial<ModLong> ct = mlist.get(0);
        if (ct.isConstant()) {
            nf = ct.leadingBaseCoefficient();
            mlist.remove(ct);
            if (mlist.size() <= 1) {
                factors.add(C);
                return factors;
            }
        } else {
            nf = ct.ring.coFac.getONE();
        }
        GenPolynomialRing<ModLong> mfac = ct.ring;
        GenPolynomial<ModLong> Pm = PolyUtil.fromIntegerCoefficients(mfac, C);
        GenPolynomial<JasBigInteger> PP = C;
        int dl = (mlist.size() + 1) / 2;
        GenPolynomial<JasBigInteger> u = PP;
        GenPolynomial<ModLong> um = Pm;
        HenselApprox ilist;
        for (int j = 1; j <= dl; j++) {
            KsubSet<GenPolynomial<ModLong>> ps = new KsubSet<>(mlist, j);
            for (List<GenPolynomial<ModLong>> flist : ps) {
                if (!D.get((int) degreeSum(flist))) {
                    continue;
                }
                GenPolynomial<ModLong> trial = mfac.getONE().multiply(nf);
                for (GenPolynomial<ModLong> fk : flist) {
                    trial = trial.multiply(fk);
                }
                trial.degree();
                GenPolynomial<ModLong> cofactor = um.divide(trial);
                try {
                    ilist = HenselUtil.liftHenselQuadratic(PP, M, trial, cofactor);
                } catch (RuntimeException e) {
                    continue;
                }
                GenPolynomial<JasBigInteger> itrial = ilist.A;
                GenPolynomial<JasBigInteger> icofactor = ilist.B;
                itrial = engine.basePrimitivePart(itrial);
                if (PolyUtil.baseSparsePseudoRemainder(u, itrial).isZERO()) {
                    factors.add(itrial);
                    u = icofactor;
                    PP = u; // fixed finally on 2009-05-03
                    um = cofactor;
                    mlist = removeOnce(mlist, flist);
                    dl = (mlist.size() + 1) / 2;
                    j = 0; // since j++
                    break;
                }
            }
        }
        if (!u.isONE() && !u.equals(C)) {
            factors.add(u);
        }
        if (factors.size() == 0) {
            factors.add(PP);
        }
        return normalizeFactorization(factors);
    }

    /**
     * Sum of all degrees.
     *
     * @param L univariate polynomial list.
     * @return sum deg(p) for p in L.
     */
    private static long degreeSum(List<GenPolynomial<ModLong>> L) {
        long s = 0L;
        for (GenPolynomial<ModLong> p : L) {
            long d = p.leadingExpVector();
            s += d;
        }
        return s;
    }

    public static BitSet factorDegrees(List<Long> E, int deg) {
        BitSet D = new BitSet(deg + 1);
        D.set(0); // constant factor
        for (Long e : E) {
            int i = e.intValue();
            BitSet s = new BitSet(deg + 1);
            for (int k = 0; k < deg + 1 - i; k++) { // shift by i places
                s.set(i + k, D.get(k));
            }
            //System.out.println("s = " + s);
            D.or(s);
            //System.out.println("D = " + D);
        }
        return D;
    }
}
