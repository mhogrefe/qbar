package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Polynomial.*;
import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings("UnusedDeclaration")
public class PolynomialDemos extends QBarDemos {
    private static final @NotNull String POLYNOMIAL_CHARS = "*+-0123456789^x";

    public PolynomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private void demoApply_BigInteger() {
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoApply_Rational() {
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoToRationalPolynomial() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("toRationalPolynomial(" + p + ") = " + p.toRationalPolynomial());
        }
    }

    private void demoCoefficient() {
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

    private void demoOf_List_BigInteger() {
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_BigInteger_int() {
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

    private void demoDegree() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private void demoLeading() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("leading(" + p + ") = " + p.leading());
        }
    }

    private void demoAdd() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private void demoAbs() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("|" + p + "| = " + p.abs());
        }
    }

    private void demoSignum() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("sgn(" + p + ") = " + p.signum());
        }
    }

    private void demoSubtract() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Polynomial() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
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

    private void demoSum() {
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(ps));
        }
    }

    private void demoProduct() {
        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private void demoDelta() {
        for (List<Polynomial> ps : take(LIMIT, P.listsAtLeast(1, P.polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(delta(ps)));
        }
    }

    private void demoPow() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.withScale(5).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoSubstitute() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(16).polynomials()))) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoDifferentiate() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("differentiate(" + p + ") = " + p.differentiate());
        }
    }

    private void demoIsMonic() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    private void demoIsPrimitive() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p + " is " + (p.isPrimitive() ? "" : "not ") + "primitive");
        }
    }

    private void demoContentAndPrimitive() {
        for (Polynomial p : take(LIMIT, filter(q -> q != ZERO, P.polynomials()))) {
            System.out.println("contentAndPrimitive(" + p + ") = " + p.contentAndPrimitive());
        }
    }

    private void demoEquals_Polynomial() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            System.out.println(p);
        }
    }
}
