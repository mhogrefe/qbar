package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
public class RationalPolynomialDemos {
    private static final boolean USE_RANDOM = false;
    private static final String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = QBarRandomProvider.EXAMPLE;
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    private static void demoIterator() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private static void demoApply() {
        initialize();
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private static void demoCoefficient() {
        initialize();
        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationalPolynomials(), P.withScale(10).naturalIntegersGeometric());
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private static void demoOf_List_Rational() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("of(" + listString + ") = " + of(rs));
        }
    }

    private static void demoOf_Rational() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private static void demoOf_Rational_int() {
        initialize();
        Iterable<Pair<Rational, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsLogarithmicOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationals(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private static void demoDegree() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private static void demoLeading() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("leading(" + p + ") = " + p.leading());
        }
    }

    private static void demoAdd() {
        initialize();
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private static void demoNegate() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private static void demoAbs() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("|" + p + "| = " + p.abs());
        }
    }

    private static void demoSignum() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("sgn(" + p + ") = " + p.signum());
        }
    }

    private static void demoSubtract() {
        initialize();
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private static void demoMultiply_RationalPolynomial() {
        initialize();
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_Rational() {
        initialize();
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_int() {
        initialize();
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.integers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoDivide_Rational() {
        initialize();
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoDivide_BigInteger() {
        initialize();
        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoDivide_int() {
        initialize();
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(i -> i != 0, P.integers())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.integersGeometric();
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private static void demoShiftRight() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.integersGeometric();
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private static void demoSum() {
        initialize();
        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(ps));
        }
    }

    private static void demoProduct() {
        initialize();
        Iterable<List<RationalPolynomial>> pss;
        if (P instanceof QBarExhaustiveProvider) {
            pss = P.lists(P.rationalPolynomials());
        } else {
            pss = P.lists(P.withScale(10).rationalPolynomials());
        }
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private static void demoDelta() {
        initialize();
        for (List<RationalPolynomial> ps : take(LIMIT, P.listsAtLeast(1, P.rationalPolynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(ps)));
        }
    }

    private static void demoIsMonic() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    private static void demoMakeMonic() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            System.out.println("makeMonic(" + p + ") = " + p.makeMonic());
        }
    }

    private static void demoContentAndPrimitive() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            System.out.println("contentAndPrimitive(" + p + ") = " + p.contentAndPrimitive());
        }
    }

    private static void demoDivide_RationalPolynomial() {
        initialize();
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(q -> q != ZERO, P.rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / (" + p.b + ") = " + p.a.divide(p.b));
        }
    }

    private static void demoPow() {
        initialize();
        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(
                    P.withScale(10).rationalPolynomials(),
                    P.withScale(5).naturalIntegersGeometric()
            );
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private static void demoSubstitute() {
        initialize();
        Iterable<RationalPolynomial> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.rationalPolynomials();
        } else {
            ps = P.withScale(6).rationalPolynomials();
        }
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(ps))) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private static void demoDifferentiate() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("differentiate(" + p + ") = " + p.differentiate());
        }
    }

    private static void demoEquals_RationalPolynomial() {
        initialize();
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private static void demoEquals_null() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private static void demoHashCode() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private static void demoCompareTo() {
        initialize();
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private static void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private static void demoRead_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_POLYNOMIAL_CHARS);
        } else {
            cs = P.uniformSample(RATIONAL_POLYNOMIAL_CHARS);
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

    private static void demoFindIn_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_POLYNOMIAL_CHARS);
        } else {
            cs = P.uniformSample(RATIONAL_POLYNOMIAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private static void demoToString() {
        initialize();
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println(p);
        }
    }
}