package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings("UnusedDeclaration")
public class PolynomialDemos {
    private static final boolean USE_RANDOM = false;
    private static final @NotNull String POLYNOMIAL_CHARS = "*+-0123456789^x";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = QBarRandomProvider.example();
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    private static void demoIterator() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private static void demoApply_BigInteger() {
        initialize();
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private static void demoApply_Rational() {
        initialize();
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private static void demoToRationalPolynomial() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("toRationalPolynomial(" + p + ") = " + p.toRationalPolynomial());
        }
    }

    private static void demoCoefficient() {
        initialize();
        Iterable<Pair<Polynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.polynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.polynomials(), P.withScale(10).naturalIntegersGeometric());
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private static void demoOf_List_BigInteger() {
        initialize();
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private static void demoOf_BigInteger() {
        initialize();
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private static void demoOf_BigInteger_int() {
        initialize();
        Iterable<Pair<BigInteger, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.bigIntegers(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.bigIntegers(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private static void demoDegree() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private static void demoLeading() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("leading(" + p + ") = " + p.leading());
        }
    }

    private static void demoAdd() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private static void demoNegate() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private static void demoAbs() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("|" + p + "| = " + p.abs());
        }
    }

    private static void demoSignum() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("sgn(" + p + ") = " + p.signum());
        }
    }

    private static void demoSubtract() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private static void demoMultiply_Polynomial() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_int() {
        initialize();
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = P.withScale(50).naturalIntegersGeometric();
        }
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), is))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private static void demoSum() {
        initialize();
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(ps));
        }
    }

    private static void demoProduct() {
        initialize();
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private static void demoDelta() {
        initialize();
        for (List<Polynomial> ps : take(LIMIT, P.listsAtLeast(1, P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(ps)));
        }
    }

    private static void demoPow() {
        initialize();
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.withScale(5).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private static void demoSubstitute() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(16).polynomials()))) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private static void demoDifferentiate() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("differentiate(" + p + ") = " + p.differentiate());
        }
    }

    private static void demoIsMonic() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    private static void demoIsPrimitive() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p + " is " + (p.isPrimitive() ? "" : "not ") + "primitive");
        }
    }

    private static void demoContentAndPrimitive() {
        initialize();
        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            System.out.println("contentAndPrimitive(" + p + ") = " + p.contentAndPrimitive());
        }
    }

    private static void demoEquals_Polynomial() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private static void demoEquals_null() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private static void demoHashCode() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private static void demoCompareTo() {
        initialize();
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
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

    private static void demoFindIn_targeted() {
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

    private static void demoToString() {
        initialize();
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p);
        }
    }
}
