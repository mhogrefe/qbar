package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.qbar.objects.RationalPolynomial.sum;
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

    private void demoApply_Rational() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.withScale(4).rationalPolynomials(), P.rationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoApply_Algebraic() {
        Iterable<Pair<RationalPolynomial, Algebraic>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Pair<RationalPolynomial, Algebraic> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoOnlyHasIntegralCoefficients() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println(p + (p.onlyHasIntegralCoefficients() ? " only has " : " doesn't only have ") +
                    "integral coefficients");
        }
    }

    private void demoToPolynomial() {
        for (RationalPolynomial p : take(LIMIT, map(Polynomial::toRationalPolynomial, P.withScale(4).polynomials()))) {
            System.out.println("toPolynomial(" + p + ") = " + p.toPolynomial());
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
            System.out.println("of(" + middle(rs.toString()) + ") = " + of(rs));
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

    private void demoMaxCoefficientBitLength() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("maxCoefficientBitLength(" + p + ") = " + p.maxCoefficientBitLength());
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

    private void demoMultiplyByPowerOfX() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("multiplyByPowerOfX(" + p.a + ", " + p.b + ") = " + p.a.multiplyByPowerOfX(p.b));
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

    private void demoSignum_Rational() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.withScale(4).rationalPolynomials(), P.rationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("signum(" + p.a + ", " + p.b + ") = " + p.a.signum(p.b));
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
            System.out.println("Σ(" + middle(ps.toString()) + ") = " + sum(ps));
        }
    }

    private void demoProduct() {
        for (List<RationalPolynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).rationalPolynomials()))) {
            System.out.println("Π(" + middle(ps.toString()) + ") = " + product(ps));
        }
    }

    private void demoDelta() {
        Iterable<List<RationalPolynomial>> pss = P.withScale(4).listsAtLeast(1, P.withScale(4).rationalPolynomials());
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            System.out.println("Δ(" + middle(ps.toString()) + ") = " + its(delta(ps)));
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
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    private void demoMakeMonic() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomialsAtLeast(0))) {
            System.out.println("makeMonic(" + p + ") = " + p.makeMonic());
        }
    }

    private void demoConstantFactor() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomialsAtLeast(0))) {
            System.out.println("constantFactor(" + p + ") = " + p.constantFactor());
        }
    }

    private void demoDivide_RationalPolynomial() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).rationalPolynomialsAtLeast(0)
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / (" + p.b + ") = " + p.a.divide(p.b));
        }
    }

    private void demoIsDivisibleBy_RationalPolynomial() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).rationalPolynomialsAtLeast(0)
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " is " + (p.a.isDivisibleBy(p.b) ? "" : "not ") + "divisible by " + p.b);
        }
    }

    private void demoRemainderSequence() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(SMALL_LIMIT, ps)) {
            System.out.println("remainderSequence(" + p.a + ", " + p.b + ") = " + p.a.remainderSequence(p.b));
        }
    }

    private void demoSignedRemainderSequence() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(SMALL_LIMIT, ps)) {
            System.out.println("signedRemainderSequence(" + p.a + ", " + p.b + ") = " +
                    p.a.signedRemainderSequence(p.b));
        }
    }

    private void demoPowerSums() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).monicRationalPolynomials())) {
            System.out.println("powerSums(" + p + ") = " + p.powerSums());
        }
    }

    private void demoFromPowerSums() {
        Iterable<List<Rational>> rss = map(
                rs -> toList(cons(Rational.of(rs.size()), rs)),
                P.withScale(4).lists(P.rationals())
        );
        for (List<Rational> rs : take(LIMIT, rss)) {
            System.out.println("fromPowerSums(" + middle(rs.toString()) + ") = " + fromPowerSums(rs));
        }
    }

    private void demoInterpolate() {
        Iterable<List<Pair<Rational, Rational>>> pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.withScale(4).distinctListsAtLeast(1, P.withScale(4).rationals()),
                                rs -> P.lists(rs.size(), P.withScale(4).rationals())
                        )
                )
        );
        for (List<Pair<Rational, Rational>> ps : take(LIMIT, pss)) {
            System.out.println("interpolate(" + middle(ps.toString()) + ") = " + interpolate(ps));
        }
    }

    private void demoCompanionMatrix() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).monicRationalPolynomials())) {
            System.out.println("companionMatrix(" + p + ") = " + p.companionMatrix());
        }
    }

    private void demoCoefficientMatrix() {
        Iterable<List<RationalPolynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= Ordering.maximum(map(RationalPolynomial::degree, ps)) + 1,
                        P.withScale(4).listsAtLeast(1, P.withScale(4).rationalPolynomials())
                )
        );
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            System.out.println("coefficientMatrix(" + middle(ps.toString()) + ") = " + coefficientMatrix(ps));
        }
    }

    private void demoReflect() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("reflect(" + p + ") = " + p.reflect());
        }
    }

    private void demoTranslate() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).rationals()
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("translate(" + p.a + ", " + p.b + ") = " + p.a.translate(p.b));
        }
    }

    private void demoStretch() {
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("stretch(" + p.a + ", " + p.b + ") = " + p.a.stretch(p.b));
        }
    }

    private void demoPowerTable() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomialsAtLeast(1),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("powerTable(" + p.a + ", " + p.b + ") = " + p.a.powerTable(p.b));
        }
    }

    private void demoRootPower() {
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialsAtLeast(1),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("rootPower(" + p.a + ", " + p.b + ") = " + p.a.rootPower(p.b));
        }
    }

    private void demoRealRoots() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomialsAtLeast(0))) {
            System.out.println("realRoots(" + p + ") = " + p.realRoots());
        }
    }

    private void demoEquals_RationalPolynomial() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(P.withScale(4).rationalPolynomials());
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private void demoCompareTo() {
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(P.withScale(4).rationalPolynomials());
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b) + " " + p.b);
        }
    }

    private void demoReadStrict_String() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_String_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.positiveIntegersGeometric());
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.b + ", " + nicePrint(p.a) + ") = " + readStrict(p.b, p.a));
        }
    }

    private void demoReadStrict_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.strings(RATIONAL_POLYNOMIAL_CHARS),
                P.positiveIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.b + ", " + p.a + ") = " + readStrict(p.b, p.a));
        }
    }

    private void demoToString() {
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomials())) {
            System.out.println(p);
        }
    }
}
