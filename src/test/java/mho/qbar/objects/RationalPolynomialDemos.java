package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalPolynomialDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";

    public RationalPolynomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private void demoApply() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.withScale(4).rationalPolynomials(), P.rationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoCoefficient() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private void demoOf_List_Rational() {
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("of(" + listString + ") = " + of(rs));
        }
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private void demoOf_Rational_int() {
        Iterable<Pair<Rational, Integer>> ps = P.pairsLogarithmicOrder(
                P.rationals(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoDegree() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private void demoLeading() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("leading(" + p + ") = " + p.leading());
        }
    }

    private void demoAdd() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(P.withScale(4).rationalPolynomials());
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private void demoAbs() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("|" + p + "| = " + p.abs());
        }
    }

    private void demoSignum() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("signum(" + p + ") = " + p.signum());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(P.withScale(4).rationalPolynomials());
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_RationalPolynomial() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(P.withScale(4).rationalPolynomials());
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.withScale(4).rationalPolynomials(), P.rationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.bigIntegers()
        );
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.withScale(4).rationalPolynomials(), P.integers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoDivide_Rational() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.nonzeroRationals()
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / (" + p.b + ") = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.nonzeroIntegers()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).integersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).integersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        for (List<RationalPolynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).rationalPolynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(ps));
        }
    }

    private void demoProduct() {
        for (List<RationalPolynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).rationalPolynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private void demoDelta() {
        Iterable<List<RationalPolynomial>> pss = P.withScale(4).listsAtLeast(1, P.withScale(4).rationalPolynomials());
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(ps)));
        }
    }

    private void demoPow() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoSubstitute() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials()
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoDifferentiate() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("differentiate(" + p + ") = " + p.differentiate());
        }
    }

    private void demoIsMonic() {
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    private void demoMakeMonic() {
        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            System.out.println("makeMonic(" + p + ") = " + p.makeMonic());
        }
    }

    private void demoContentAndPrimitive() {
        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            System.out.println("contentAndPrimitive(" + p + ") = " + p.contentAndPrimitive());
        }
    }

    private void demoDivide_RationalPolynomial() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(q -> q != ZERO, P.rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / (" + p.b + ") = " + p.a.divide(p.b));
        }
    }

    private void demoEquals_RationalPolynomial() {
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            System.out.println(p);
        }
    }
}
