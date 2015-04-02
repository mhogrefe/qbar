package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
public class PolynomialDemos {
    private static final boolean USE_RANDOM = false;
    private static final String POLYNOMIAL_CHARS = "*+-0123456789^x";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    public static void demoIterator() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    public static void demoApply_BigInteger() {
        initialize();
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    public static void demoApply_Rational() {
        initialize();
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    public static void demoToRationalPolynomial() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("toRationalPolynomial(" + p + ") = " + p.toRationalPolynomial());
        }
    }

    public static void demoCoefficient() {
        initialize();
        Iterable<Pair<Polynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.polynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.polynomials(), ((QBarRandomProvider) P).naturalIntegersGeometric(10));
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    public static void demoOf_List_BigInteger() {
        initialize();
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    public static void demoOf_BigInteger() {
        initialize();
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    public static void demoOf_BigInteger_int() {
        initialize();
        Iterable<Pair<BigInteger, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.bigIntegers(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.bigIntegers(), ((QBarRandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    public static void demoDegree() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    public static void demoLeading() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("leading(" + p + ") = " + p.leading());
        }
    }

    public static void demoAdd() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    public static void demoNegate() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    public static void demoAbs() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("|" + p + "| = " + p.abs());
        }
    }

    public static void demoSignum() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("sgn(" + p + ") = " + p.signum());
        }
    }

    public static void demoSubtract() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    public static void demoMultiply_Polynomial() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_int() {
        initialize();
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric(50);
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    public static void demoSum() {
        initialize();
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(ps));
        }
    }

    public static void demoProduct() {
        initialize();
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    public static void demoDelta() {
        initialize();
        for (List<Polynomial> ps : take(LIMIT, P.listsAtLeast(1, P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(ps)));
        }
    }

    public static void demoPow() {
        initialize();
        Iterable<Pair<Polynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.polynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.polynomials(), ((QBarRandomProvider) P).naturalIntegersGeometric(5));
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    public static void demoSubstitute() {
        initialize();
        Iterable<Polynomial> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.polynomials();
        } else {
            ps = ((QBarRandomProvider) P).polynomialsBySize(16);
        }
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(ps))) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    public static void demoIsMonic() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    public static void demoIsPrimitive() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p + " is " + (p.isPrimitive() ? "" : "not ") + "primitive");
        }
    }

    public static void demoContentAndPrimitive() {
        initialize();
        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            System.out.println("contentAndPrimitive(" + p + ") = " + p.contentAndPrimitive());
        }
    }

    public static void demoEquals_Polynomial() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    public static void demoEquals_null() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    public static void demoHashCode() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    public static void demoCompareTo() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    public static void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    public static void demoRead_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(POLYNOMIAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(POLYNOMIAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private static void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoFindIn_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(POLYNOMIAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(POLYNOMIAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoToString() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p);
        }
    }
}
