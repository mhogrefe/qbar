package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.qbar.objects.RationalPolynomial.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.ge;
import static mho.wheels.ordering.Ordering.max;
import static mho.wheels.testing.Testing.*;

public class RationalPolynomialProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";

    public RationalPolynomialProperties() {
        super("RationalPolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesApply_Rational();
        compareImplementationsApply_Rational();
        propertiesApply_Algebraic();
        propertiesOnlyHasIntegralCoefficients();
        propertiesToPolynomial();
        propertiesCoefficient();
        propertiesOf_List_Rational();
        propertiesOf_Rational();
        propertiesOf_Rational_int();
        propertiesMaxCoefficientBitLength();
        propertiesDegree();
        propertiesLeading();
        propertiesMultiplyByPowerOfX();
        compareImplementationsMultiplyByPowerOfX();
        propertiesAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSignum_Rational();
        compareImplementationsSignum_Rational();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_RationalPolynomial();
        compareImplementationsMultiply_RationalPolynomial();
        propertiesMultiply_Rational();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesDivide_Rational();
        propertiesDivide_BigInteger();
        propertiesDivide_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesDelta();
        propertiesPow();
        compareImplementationsPow();
        propertiesSubstitute();
        compareImplementationsSubstitute();
        propertiesDifferentiate();
        propertiesIsMonic();
        propertiesMakeMonic();
        propertiesConstantFactor();
        propertiesDivide_RationalPolynomial();
        compareImplementationsDivide_RationalPolynomial();
        propertiesIsDivisibleBy();
        compareImplementationsIsDivisibleBy();
        propertiesRemainderSequence();
        propertiesSignedRemainderSequence();
        propertiesPowerSums();
        propertiesFromPowerSums();
        propertiesInterpolate();
        propertiesReflect();
        propertiesTranslate();
        propertiesStretch();
        propertiesPowerTable();
        compareImplementationsPowerTable();
        propertiesRootPower();
        compareImplementationsRootPower();
        propertiesRealRoots();
        propertiesEquals();
        propertiesCompanionMatrix();
        propertiesCoefficientMatrix();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict_String();
        propertiesReadStrict_int_String();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            List<Rational> rs = toList(p);
            assertTrue(p, all(Objects::nonNull, rs));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Rational> ss) -> of(ss), p);
            testNoRemove(p);
            testHasNext(p);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            List<Rational> rs = toList(p);
            assertTrue(p, last(rs) != Rational.ZERO);
        }
    }

    private static @NotNull Rational apply_naive(@NotNull RationalPolynomial p, @NotNull Rational x) {
        return Rational.sum(
                toList(
                        zipWith(
                                (c, i) -> c == Rational.ZERO ? Rational.ZERO : x.pow(i).multiply(c),
                                p,
                                ExhaustiveProvider.INSTANCE.naturalIntegers()
                        )
                )
        );
    }

    private void propertiesApply_Rational() {
        initialize("apply(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p, y, apply_naive(p.a, p.b));
        }

        for (Rational i : take(LIMIT, P.rationals())) {
            assertEquals(i, ZERO.apply(i), Rational.ZERO);
            fixedPoint(X, i);
            assertEquals(i, of(Rational.NEGATIVE_ONE, 1).apply(i), i.negate());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.apply(Rational.ZERO), p.coefficient(0));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.apply(Rational.ONE), Rational.sum(toList(p)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).apply(p.b), p.a);
            assertEquals(p, of(Arrays.asList(p.a, Rational.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p, of(Arrays.asList(p.a.negate(), Rational.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.rationals()))) {
            assertEquals(p, of(Rational.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private void compareImplementationsApply_Rational() {
        Map<String, Function<Pair<RationalPolynomial, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> apply_naive(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.rationals());
        compareImplementations("apply(Rational)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesApply_Algebraic() {
        initialize("apply(Algebraic)");
        Iterable<Pair<RationalPolynomial, Algebraic>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (Pair<RationalPolynomial, Algebraic> p : take(SMALL_LIMIT, ps)) {
            Algebraic x = p.a.apply(p.b);
            x.validate();
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, ZERO.apply(x), Algebraic.ZERO);
            fixedPoint(X::apply, x);
            assertEquals(x, of(Rational.NEGATIVE_ONE, 1).apply(x), x.negate());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.apply(Algebraic.ZERO), Algebraic.of(p.coefficient(0)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.apply(Algebraic.ONE), Algebraic.of(Rational.sum(toList(p))));
        }

        for (Pair<Rational, Algebraic> p : take(LIMIT, P.pairs(P.rationals(), P.algebraics()))) {
            assertEquals(p, of(p.a).apply(p.b), Algebraic.of(p.a));
            assertEquals(p, of(Arrays.asList(p.a, Rational.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p, of(Arrays.asList(p.a.negate(), Rational.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }
    }

    private void propertiesOnlyHasIntegralCoefficients() {
        initialize("onlyHasIntegralCoefficients()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.onlyHasIntegralCoefficients();
        }

        for (RationalPolynomial p : take(LIMIT, map(Polynomial::toRationalPolynomial, P.polynomials()))) {
            assertTrue(p, p.onlyHasIntegralCoefficients());
        }
    }

    private void propertiesToPolynomial() {
        initialize("toPolynomial()");
        for (RationalPolynomial p : take(LIMIT, map(Polynomial::toRationalPolynomial, P.polynomials()))) {
            Polynomial q = p.toPolynomial();
            assertEquals(p, p.toString(), q.toString());
            assertEquals(p, p.degree(), q.degree());
            inverse(RationalPolynomial::toPolynomial, Polynomial::toRationalPolynomial, p);
        }

        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(
                map(Polynomial::toRationalPolynomial, P.polynomials()),
                P.bigIntegers()
        );
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.apply(Rational.of(p.b)).bigIntegerValueExact(), p.a.toPolynomial().apply(p.b));
        }

        Iterable<RationalPolynomial> psFail = filterInfinite(
                p -> any(c -> !c.isInteger(), p),
                P.rationalPolynomials()
        );
        for (RationalPolynomial p : take(LIMIT, psFail)) {
            try {
                p.toPolynomial();
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.rationalPolynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        ps = filterInfinite(
                q -> q.b <= q.a.degree(),
                P.pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegersGeometric())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        ps = filterInfinite(
                q -> q.b > q.a.degree(),
                P.pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegersGeometric())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.coefficient(p.b), Rational.ZERO);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.coefficient(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesOf_List_Rational() {
        initialize("of(List<Rational>)");
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalPolynomial p = of(rs);
            p.validate();
            assertTrue(rs, p.degree() < rs.size());
        }

        Iterable<List<Rational>> rss = filterInfinite(
                rs -> rs.isEmpty() || last(rs) != Rational.ZERO,
                P.lists(P.rationals())
        );
        for (List<Rational> rs : take(LIMIT, rss)) {
            fixedPoint(ss -> toList(of(ss)), rs);
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                of(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            RationalPolynomial p = of(r);
            p.validate();
            assertTrue(r, p.degree() == 0 || p.degree() == -1);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, of(r).coefficient(0), r);
        }
    }

    private void propertiesOf_Rational_int() {
        initialize("of(Rational, int)");
        Iterable<Pair<Rational, Integer>> ps = P.pairsLogarithmicOrder(
                P.rationals(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = of(p.a, p.b);
            q.validate();
            assertEquals(p, q, of(p.a).multiplyByPowerOfX(p.b));
        }

        ps = P.pairsLogarithmicOrder(P.nonzeroRationals(), P.naturalIntegersGeometric());
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = of(p.a, p.b);
            assertTrue(p, all(c -> c == Rational.ZERO, init(q)));
            assertEquals(p, q.degree(), p.b);
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            assertTrue(i, of(Rational.ZERO, i) == ZERO);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMaxCoefficientBitLength() {
        initialize("maxCoefficientBitLength()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertTrue(p, p.maxCoefficientBitLength() >= 0);
            homomorphic(
                    RationalPolynomial::negate,
                    Function.identity(),
                    RationalPolynomial::maxCoefficientBitLength,
                    RationalPolynomial::maxCoefficientBitLength,
                    p
            );
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }
    }

    private void propertiesLeading() {
        initialize("leading()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.leading();
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            Rational leading = p.leading().get();
            assertNotEquals(p, leading, Rational.ZERO);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            RationalPolynomial p = of(r);
            assertEquals(r, p.leading().get(), p.coefficient(0));
        }
    }

    private static @NotNull RationalPolynomial multiplyByPowerOfX_alt(@NotNull RationalPolynomial a, int p) {
        return a.multiply(of(Rational.ONE, p));
    }

    private void propertiesMultiplyByPowerOfX() {
        initialize("multiplyByPowerOfX(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = p.a.multiplyByPowerOfX(p.b);
            q.validate();
            assertEquals(p, multiplyByPowerOfX_alt(p.a, p.b), q);
        }

        ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomialsAtLeast(0),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiplyByPowerOfX(p.b).degree(), p.a.degree() + p.b);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.multiplyByPowerOfX(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsMultiplyByPowerOfX() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> multiplyByPowerOfX_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiplyByPowerOfX(p.b));
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("multiplyByPowerOfX(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesAdd() {
        initialize("add(RationalPolynomial)");
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= max(p.a.degree(), p.b.degree()));
            commutative(RationalPolynomial::add, p);
            inverse(q -> q.add(p.b), (RationalPolynomial q) -> q.subtract(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::add,
                    RationalPolynomial::add,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, ZERO.add(p), p);
            assertEquals(p, p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts2 = P.triples(
                P.rationalPolynomials()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts2)) {
            associative(RationalPolynomial::add, t);
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial negative = p.negate();
            negative.validate();
            assertEquals(p, negative.degree(), p.degree());
            involution(RationalPolynomial::negate, p);
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            assertEquals(p, p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::negate,
                    RationalPolynomial::negate,
                    r
            );
        }

        for (RationalPolynomial p : take(LIMIT, filterInfinite(q -> q != ZERO, P.rationalPolynomials()))) {
            RationalPolynomial negative = p.negate();
            assertNotEquals(p, p, negative);
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial abs = p.abs();
            abs.validate();
            assertEquals(p, abs.degree(), p.degree());
            idempotent(RationalPolynomial::abs, p);
            assertNotEquals(p, abs.signum(), -1);
            assertTrue(p, ge(abs, ZERO));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(RationalPolynomial::of, RationalPolynomial::of, Rational::abs, RationalPolynomial::abs, r);
            assertEquals(r, of(r).abs(), of(r.abs()));
        }
    }

    private void propertiesSignum() {
        initialize("signum()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int signum = p.signum();
            assertEquals(p, signum, Ordering.compare(p, ZERO).toInt());
            assertTrue(p, signum == -1 || signum == 0 || signum == 1);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(RationalPolynomial::of, Function.identity(), Rational::signum, RationalPolynomial::signum, r);
        }
    }

    private static int signum_Rational_simplest(@NotNull RationalPolynomial p, @NotNull Rational x) {
        return p.apply(x).signum();
    }

    private static int signum_Rational_alt(@NotNull RationalPolynomial p, @NotNull Rational x) {
        return Rational.sumSign(
                toList(
                        zipWith(
                                (c, i) -> c == Rational.ZERO ? Rational.ZERO : x.pow(i).multiply(c),
                                p,
                                ExhaustiveProvider.INSTANCE.naturalIntegers()
                        )
                )
        );
    }

    private void propertiesSignum_Rational() {
        initialize("signum(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            int signum = p.a.signum(p.b);
            assertEquals(p, signum, signum_Rational_simplest(p.a, p.b));
            assertEquals(p, signum, signum_Rational_alt(p.a, p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.signum(r), 0);
            assertEquals(r, X.signum(r), r.signum());
            assertEquals(r, of(Rational.NEGATIVE_ONE, 1).signum(r), -r.signum());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.signum(Rational.ZERO), p.coefficient(0).signum());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.signum(Rational.ONE), Rational.sumSign(toList(p)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).signum(p.b), p.a.signum());
        }
    }

    private void compareImplementationsSignum_Rational() {
        Map<String, Function<Pair<RationalPolynomial, Rational>, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> signum_Rational_simplest(p.a, p.b));
        functions.put("alt", p -> signum_Rational_alt(p.a, p.b));
        functions.put("standard", p -> p.a.signum(p.b));
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.rationals());
        compareImplementations("signum(Rational)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalPolynomial subtract_simplest(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(RationalPolynomial)");
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(RationalPolynomial::subtract, RationalPolynomial::negate, p);
            inverse(q -> q.subtract(p.b), (RationalPolynomial q) -> q.add(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.subtract(t.b).apply(t.c), t.a.apply(t.c).subtract(t.b.apply(t.c)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::subtract,
                    RationalPolynomial::subtract,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            fixedPoint(q -> q.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        compareImplementations(
                "subtract(RationalPolynomial)",
                take(LIMIT, P.pairs(P.rationalPolynomials())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull RationalPolynomial multiply_RationalPolynomial_alt(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        if (a == ZERO || b == ZERO) return ZERO;
        if (a == ONE) return b;
        if (b == ONE) return a;
        int p = a.degree();
        int q = b.degree();
        if (p < q) {
            return multiply_RationalPolynomial_alt(b, a);
        }
        int r = p + q;
        List<Rational> productCoefficients = new ArrayList<>();
        int k = 0;
        for (; k <= q; k++) {
            Rational coefficient = Rational.ZERO;
            for (int i = 0; i <= k; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k < p; k++) {
            Rational coefficient = Rational.ZERO;
            for (int i = 0; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k <= r; k++) {
            Rational coefficient = Rational.ZERO;
            for (int i = k - p; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        return of(productCoefficients);
    }

    private void propertiesMultiply_RationalPolynomial() {
        initialize("multiply(RationalPolynomial)");
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_RationalPolynomial_alt(p.a, p.b));
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            commutative(RationalPolynomial::multiply, p);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(ONE::multiply, p);
            fixedPoint(q -> q.multiply(ONE), p);
            fixedPoint(q -> q.multiply(p), ZERO);
            fixedPoint(p::multiply, ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts2 = P.triples(
                P.rationalPolynomials()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts2)) {
            associative(RationalPolynomial::multiply, t);
            leftDistributive(RationalPolynomial::add, RationalPolynomial::multiply, t);
            rightDistributive(RationalPolynomial::add, RationalPolynomial::multiply, t);
        }

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.monicRationalPolynomials()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            assertTrue(p, product.isMonic());
        }
    }

    private void compareImplementationsMultiply_RationalPolynomial() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("alt", p -> multiply_RationalPolynomial_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        compareImplementations(
                "multiply(RationalPolynomial)",
                take(LIMIT, P.pairs(P.rationalPolynomials())),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == Rational.ZERO || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationals(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ONE.multiply(r), of(r));
            fixedPoint(j -> j.multiply(r), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.multiply(Rational.ONE), p);
            assertTrue(p, p.multiply(Rational.ZERO) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts2 = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.bigIntegers()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(Rational.of(p.b))));
            assertEquals(p, product, of(Rational.of(p.b)).multiply(p.a));
        }

        Iterable<Triple<RationalPolynomial, BigInteger, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.bigIntegers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, BigInteger, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.multiply(BigInteger.ONE), p);
            assertTrue(p, p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, BigInteger>> ts2 = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.bigIntegers()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, BigInteger> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.integers()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(Rational.of(p.b)));
            assertEquals(p, product, of(Rational.of(p.b)).multiply(p.a));
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.integers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(BigInteger.valueOf(t.b)));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::multiply,
                    RationalPolynomial::multiply,
                    p
            );
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.multiply(1), p);
            assertTrue(p, p.multiply(0) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Integer>> ts2 = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.integers()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Integer> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.nonzeroRationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalPolynomial q) -> q.multiply(p.b), p.a);
            assertEquals(p, quotient, p.a.multiply(p.b.invert()));
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.nonzeroRationals(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::divide,
                    RationalPolynomial::divide,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.divide(Rational.ONE), p);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            fixedPoint(p -> p.divide(r), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(Rational.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(P.rationalPolynomials(), P.nonzeroBigIntegers());
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalPolynomial q) -> q.multiply(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, BigInteger, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.nonzeroBigIntegers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, BigInteger, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::divide,
                    RationalPolynomial::divide,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.divide(BigInteger.ONE), p);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(p -> p.divide(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(BigInteger.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), P.nonzeroIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            inverse(q -> q.divide(p.b), (RationalPolynomial q) -> q.multiply(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.nonzeroIntegers(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::divide,
                    RationalPolynomial::divide,
                    p
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.divide(1), p);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(p -> p.divide(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(0);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull RationalPolynomial shiftLeft_simplest(@NotNull RationalPolynomial p, int bits) {
        if (bits < 0) {
            return p.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return p.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), P.integersGeometric());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
            inverse(q -> q.shiftLeft(p.b), (RationalPolynomial q) -> q.shiftRight(p.b), p.a);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.shiftLeft(0), p);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.integersGeometric(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::shiftLeft,
                    RationalPolynomial::shiftLeft,
                    p
            );
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.rationalPolynomials(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull RationalPolynomial shiftRight_simplest(@NotNull RationalPolynomial p, int bits) {
        if (bits < 0) {
            return p.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return p.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), P.integersGeometric());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
            inverse(q -> q.shiftRight(p.b), (RationalPolynomial q) -> q.shiftLeft(p.b), p.a);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            fixedPoint(q -> q.shiftRight(0), p);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.integersGeometric(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftRight(t.b).apply(t.c), t.a.apply(t.c).shiftRight(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::shiftRight,
                    RationalPolynomial::shiftRight,
                    p
            );
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRight(p.b));
        compareImplementations(
                "shiftRight(int)",
                take(LIMIT, P.pairs(P.rationalPolynomials(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull RationalPolynomial sum_simplest(@NotNull Iterable<RationalPolynomial> xs) {
        return foldl(RationalPolynomial::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<RationalPolynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalPolynomials(),
                RationalPolynomial::add,
                RationalPolynomial::sum,
                RationalPolynomial::validate,
                true,
                true
        );

        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            RationalPolynomial sum = sum(ps);
            assertEquals(ps, sum, sum_simplest(ps));
            assertTrue(ps, ps.isEmpty() || sum.degree() <= Ordering.maximum(map(RationalPolynomial::degree, ps)));
        }

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps = P.pairs(
                P.lists(P.rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a).apply(p.b), Rational.sum(toList(map(q -> q.apply(p.b), p.a))));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    RationalPolynomial::of,
                    Rational::sum,
                    RationalPolynomial::sum,
                    rs
            );
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<RationalPolynomial>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalPolynomialProperties::sum_simplest);
        functions.put("standard", RationalPolynomial::sum);
        compareImplementations(
                "sum(Iterable<RationalPolynomial>)",
                take(LIMIT, P.lists(P.rationalPolynomials())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull RationalPolynomial product_simplest(@NotNull Iterable<RationalPolynomial> xs) {
        if (any(Objects::isNull, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(RationalPolynomial::multiply, ONE, xs);
    }

    private static @NotNull RationalPolynomial product_alt(@NotNull List<RationalPolynomial> xs) {
        if (any(x -> x == ZERO, xs)) return ZERO;
        List<Rational> productCoefficients =
                toList(replicate(sumInteger(toList(map(RationalPolynomial::degree, xs))) + 1, Rational.ZERO));
        List<List<Pair<Rational, Integer>>> selections = toList(
                map(p -> toList(zip(p, ExhaustiveProvider.INSTANCE.naturalIntegers())), xs)
        );
        outer:
        for (List<Pair<Rational, Integer>> selection : EP.cartesianProduct(selections)) {
            Rational coefficient = Rational.ONE;
            int exponent = 0;
            for (Pair<Rational, Integer> p : selection) {
                if (p.a.equals(Rational.ZERO)) continue outer;
                coefficient = coefficient.multiply(p.a);
                exponent += p.b;
            }
            productCoefficients.set(exponent, productCoefficients.get(exponent).add(coefficient));
        }
        return of(productCoefficients);
    }

    private void propertiesProduct() {
        initialize("product(Iterable<RationalPolynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.withScale(4).rationalPolynomials(),
                RationalPolynomial::multiply,
                RationalPolynomial::product,
                RationalPolynomial::validate,
                true,
                true
        );

        for (List<RationalPolynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).rationalPolynomials()))) {
            RationalPolynomial product = product(ps);
            assertTrue(
                    ps,
                    any(p -> p == ZERO, ps) ||
                            product.degree() == IterableUtils.sumInteger(toList(map(RationalPolynomial::degree, ps)))
            );
        }

        Iterable<List<RationalPolynomial>> pss = P.withScale(1).lists(P.withSecondaryScale(1).rationalPolynomials());
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            RationalPolynomial product = product(ps);
            assertEquals(ps, product, product_simplest(ps));
            assertEquals(ps, product, product_alt(ps));
        }

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps2 = P.pairs(
                P.withScale(4).lists(P.withScale(4).rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps2)) {
            assertEquals(p, product(p.a).apply(p.b), Rational.product(toList(map(q -> q.apply(p.b), p.a))));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.withScale(4).rationals()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    RationalPolynomial::of,
                    Rational::product,
                    RationalPolynomial::product,
                    rs
            );
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<RationalPolynomial>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalPolynomialProperties::product_simplest);
        functions.put("alt", RationalPolynomialProperties::product_alt);
        functions.put("standard", RationalPolynomial::product);
        compareImplementations(
                "product(Iterable<RationalPolynomial>)",
                take(LIMIT, P.withScale(1).lists(P.withSecondaryScale(1).rationalPolynomials())),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<RationalPolynomial>)");
        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.rationalPolynomials(),
                P.rationalPolynomials(),
                RationalPolynomial::negate,
                RationalPolynomial::subtract,
                RationalPolynomial::delta,
                RationalPolynomial::validate
        );

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps = P.pairs(
                P.listsAtLeast(1, P.rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps)) {
            aeqit(p.toString(), map(q -> q.apply(p.b), delta(p.a)), Rational.delta(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.listsAtLeast(1, P.rationals()))) {
            homomorphic(
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    ss -> toList(map(RationalPolynomial::of, ss)),
                    ss -> toList(Rational.delta(ss)),
                    qs -> toList(delta(qs)),
                    rs
            );
        }
    }

    private static @NotNull RationalPolynomial pow_simplest(@NotNull RationalPolynomial a, int p) {
        return product(toList(replicate(p, a)));
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).naturalIntegersGeometric(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        Iterable<Pair<Rational, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.rationals(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    RationalPolynomial::of,
                    Rational::pow,
                    RationalPolynomial::pow,
                    p
            );
        }

        for (int i : take(LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            fixedPoint(p -> p.pow(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.withScale(4).withSecondaryScale(4).rationalPolynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            fixedPoint(q -> q.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

        Iterable<Triple<RationalPolynomial, Integer, Integer>> ts2 = P.triples(
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).naturalIntegersGeometric(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Triple<RationalPolynomial, Integer, Integer> t : take(LIMIT, ts2)) {
            RationalPolynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            RationalPolynomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            RationalPolynomial expression5 = t.a.pow(t.b).pow(t.c);
            RationalPolynomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Integer>> ts3 = P.triples(
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(3).withSecondaryScale(1).rationalPolynomials(),
                P.withScale(1).naturalIntegersGeometric()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Integer> t : take(LIMIT, ts3)) {
            RationalPolynomial expression1 = t.a.multiply(t.b).pow(t.c);
            RationalPolynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalPolynomial substitute_naive(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        return sum(
                toList(
                        zipWith(
                                (c, i) -> c == Rational.ZERO ? ZERO : b.pow(i).multiply(c),
                                a,
                                ExhaustiveProvider.INSTANCE.naturalIntegers()
                        )
                )
        );
    }

    private void propertiesSubstitute() {
        initialize("substitute(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials()
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            RationalPolynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(p, p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree());
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.withScale(4).withSecondaryScale(4).rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<Rational, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationals(), P.rationalPolynomials()))) {
            RationalPolynomial q = of(p.a);
            assertEquals(p, q.substitute(p.b), q);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p, p.substitute(ONE), of(Rational.sum(toList(p))));
            fixedPoint(q -> q.substitute(X), p);
        }
    }

    private void compareImplementationsSubstitute() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("naïve", p -> substitute_naive(p.a, p.b));
        functions.put("standard", p -> p.a.substitute(p.b));
        compareImplementations(
                "substitute(RationalPolynomial)",
                take(LIMIT, P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomials())),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesDifferentiate() {
        initialize("differentiate()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial derivative = p.differentiate();
            derivative.validate();
            assertEquals(p, last(take(p.degree() + 2, iterate(RationalPolynomial::differentiate, p))), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials(0))) {
            assertEquals(p, p.differentiate(), ZERO);
        }
    }

    private void propertiesIsMonic() {
        initialize("isMonic()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            boolean isMonic = p.isMonic();
            Optional<Rational> leading = p.leading();
            assertEquals(p, isMonic, leading.isPresent() && leading.get() == Rational.ONE);
        }
    }

    private void propertiesMakeMonic() {
        initialize("makeMonic()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            RationalPolynomial monic = p.makeMonic();
            monic.validate();
            assertEquals(p, monic.degree(), p.degree());
            assertTrue(p, monic.isMonic());
            idempotent(RationalPolynomial::makeMonic, p);
            assertEquals(p, p.negate().makeMonic(), monic);
            assertTrue(
                    p,
                    same(
                            zipWith(
                                    Rational::divide,
                                    filter(r -> r != Rational.ZERO, p),
                                    filter(r -> r != Rational.ZERO, monic)
                            )
                    )
            );
        }

        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(
                P.rationalPolynomialsAtLeast(0),
                P.nonzeroRationals()
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial monic = p.a.makeMonic();
            assertEquals(p, p.a.multiply(p.b).makeMonic(), monic);
        }
    }

    private void propertiesConstantFactor() {
        initialize("constantFactor()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            Pair<Rational, Polynomial> contentAndPrimitive = p.constantFactor();
            Rational constantFactor = contentAndPrimitive.a;
            assertNotNull(p, constantFactor);
            Polynomial absolutePrimitive = contentAndPrimitive.b;
            assertEquals(p, absolutePrimitive.degree(), p.degree());
            assertNotNull(p, absolutePrimitive);
            assertTrue(p, absolutePrimitive.isPrimitive());
            assertEquals(p, absolutePrimitive.toRationalPolynomial().multiply(constantFactor), p);
            assertEquals(p, absolutePrimitive.signum(), 1);
            idempotent(q -> q.constantFactor().b.toRationalPolynomial(), p);
        }
    }

    private static @NotNull Pair<RationalPolynomial, RationalPolynomial> divide_RationalPolynomial_simplest(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        RationalPolynomial quotient = ZERO;
        RationalPolynomial remainder = a;
        while (remainder != ZERO && remainder.degree() >= b.degree()) {
            RationalPolynomial t = of(
                    remainder.leading().get().divide(b.leading().get()),
                    remainder.degree() - b.degree()
            );
            quotient = quotient.add(t);
            remainder = remainder.subtract(t.multiply(b));
        }
        return new Pair<>(quotient, remainder);
    }

    private void propertiesDivide_RationalPolynomial() {
        initialize("divide(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = p.a.divide(p.b);
            assertEquals(p, quotRem, divide_RationalPolynomial_simplest(p.a, p.b));
            RationalPolynomial quotient = quotRem.a;
            assertNotNull(p, quotient);
            RationalPolynomial remainder = quotRem.b;
            assertNotNull(p, remainder);
            quotient.validate();
            remainder.validate();
            assertEquals(p, quotient.multiply(p.b).add(remainder), p.a);
            assertTrue(p, remainder.degree() < p.b.degree());
        }

        Iterable<Quadruple<RationalPolynomial, RationalPolynomial, Rational, Rational>> qs = P.quadruples(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0),
                P.rationals(),
                P.nonzeroRationals()
        );
        for (Quadruple<RationalPolynomial, RationalPolynomial, Rational, Rational> q : take(LIMIT, qs)) {
            assertEquals(q, q.a.multiply(q.c).divide(q.b.multiply(q.d)).b, q.a.divide(q.b).b.multiply(q.c));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = of(p.a).divide(of(p.b));
            assertEquals(p, quotRem.a, of(p.a.divide(p.b)));
            assertEquals(p, quotRem.b, ZERO);
        }

        ps = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.rationalPolynomials(), P.rationalPolynomialsAtLeast(0))
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = p.a.divide(p.b);
            assertEquals(p, quotRem.a, ZERO);
            assertEquals(p, quotRem.b, p.a);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivide_RationalPolynomial() {
        Map<
                String,
                Function<Pair<RationalPolynomial, RationalPolynomial>, Pair<RationalPolynomial, RationalPolynomial>>
        > functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divide_RationalPolynomial_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.divide(p.b));
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        compareImplementations("divide(RationalPolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static boolean isDivisibleBy_simplest(@NotNull RationalPolynomial a, @NotNull RationalPolynomial b) {
        return a.divide(b).b == ZERO;
    }

    private void propertiesIsDivisibleBy() {
        initialize("isDivisibleBy(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            boolean isDivisible = p.a.isDivisibleBy(p.b);
            assertEquals(p, isDivisible, isDivisibleBy_simplest(p.a, p.b));
            assertEquals(p, isDivisible, p.a.equals(p.a.divide(p.b).a.multiply(p.b)));
            assertTrue(p, p.a.multiply(p.b).isDivisibleBy(p.b));
        }

        ps = P.pairs(P.rationalPolynomials(), P.rationalPolynomials(0));
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.isDivisibleBy(p.b));
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0),
                P.nonzeroRationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            boolean isDivisible = t.a.isDivisibleBy(t.b);
            assertEquals(t, isDivisible, t.a.multiply(t.c).isDivisibleBy(t.b));
            assertEquals(t, isDivisible, t.a.isDivisibleBy(t.b.multiply(t.c)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.isDivisibleBy(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsIsDivisibleBy() {
        Map<String, Function<Pair<RationalPolynomial, RationalPolynomial>, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> isDivisibleBy_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.isDivisibleBy(p.b));
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                P.rationalPolynomialsAtLeast(0)
        );
        compareImplementations("isDivisibleBy(RationalPolynomial)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesRemainderSequence() {
        initialize("remainderSequence(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            List<RationalPolynomial> sequence = p.a.remainderSequence(p.b);
            sequence.forEach(RationalPolynomial::validate);
            assertFalse(p, sequence.isEmpty());
            assertNotEquals(p, last(sequence), ZERO);
            Polynomial pa = p.a == ZERO ? Polynomial.ZERO : p.a.constantFactor().b;
            Polynomial pb = p.b == ZERO ? Polynomial.ZERO : p.b.constantFactor().b;
            assertEquals(p, last(sequence).constantFactor().b, pa.gcd(pb));
        }

        ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            Polynomial pa = p.a == ZERO ? Polynomial.ZERO : p.a.constantFactor().b;
            Polynomial pb = p.b == ZERO ? Polynomial.ZERO : p.b.constantFactor().b;
            assertEquals(
                    p,
                    toList(map(q -> q == ZERO ? Polynomial.ZERO : q.constantFactor().b, p.a.remainderSequence(p.b))),
                    toList(map(Polynomial::abs, pa.primitivePseudoRemainderSequence(pb)))
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.remainderSequence(ZERO), Collections.singletonList(p));
            assertEquals(p, ZERO.remainderSequence(p), Arrays.asList(ZERO, p));
        }
    }

    private void propertiesSignedRemainderSequence() {
        initialize("signedRemainderSequence(RationalPolynomial)");
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            List<RationalPolynomial> sequence = p.a.signedRemainderSequence(p.b);
            sequence.forEach(RationalPolynomial::validate);
            assertFalse(p, sequence.isEmpty());
            assertNotEquals(p, last(sequence), ZERO);
            Polynomial pa = p.a == ZERO ? Polynomial.ZERO : p.a.constantFactor().b;
            Polynomial pb = p.b == ZERO ? Polynomial.ZERO : p.b.constantFactor().b;
            assertEquals(p, last(sequence).constantFactor().b, pa.gcd(pb));
            assertEquals(
                    p,
                    toList(map(RationalPolynomial::abs, sequence)),
                    toList(map(RationalPolynomial::abs, p.a.remainderSequence(p.b)))
            );
        }

        ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            List<RationalPolynomial> sequence = p.a.signedRemainderSequence(p.b);
            Polynomial pa = p.a == ZERO ? Polynomial.ZERO : p.a.constantFactor().b;
            Polynomial pb = p.b == ZERO ? Polynomial.ZERO : p.b.constantFactor().b;
            if (p.a.signum() == -1) pa = pa.negate();
            if (p.b.signum() == -1) pb = pb.negate();
            List<Polynomial> psprs = pa.primitiveSignedPseudoRemainderSequence(pb);
            assertEquals(
                    p,
                    toList(map(q -> q == ZERO ? Polynomial.ZERO : q.constantFactor().b, sequence)),
                    toList(map(Polynomial::abs, psprs))
            );
            assertEquals(p, toList(map(RationalPolynomial::signum, sequence)), toList(map(Polynomial::signum, psprs)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.signedRemainderSequence(ZERO), Collections.singletonList(p));
            assertEquals(p, ZERO.signedRemainderSequence(p), Arrays.asList(ZERO, p));
        }
    }

    private void propertiesPowerSums() {
        initialize("powerSums()");
        for (RationalPolynomial p : take(LIMIT, P.monicRationalPolynomials())) {
            List<Rational> sums = p.powerSums();
            assertFalse(p, sums.isEmpty());
            assertNotEquals(p, head(sums).signum(), -1);
            inverse(RationalPolynomial::powerSums, RationalPolynomial::fromPowerSums, p);
        }

        for (List<Rational> rs : take(LIMIT, P.withScale(4).bags(P.rationals()))) {
            List<RationalPolynomial> factors = new ArrayList<>();
            //noinspection Convert2streamapi
            for (Rational r : rs) {
                factors.add(of(Arrays.asList(r.negate(), Rational.ONE)));
            }
            List<Rational> sums = product(factors).powerSums();
            for (int i = 0; i <= rs.size(); i++) {
                int p = i;
                assertEquals(rs, sums.get(i), Rational.sum(toList(map(r -> r.pow(p), rs))));
            }
        }

        for (RationalPolynomial p : take(LIMIT, P.monicRationalPolynomials(1))) {
            assertEquals(p, p.powerSums(), Arrays.asList(Rational.ONE, p.coefficient(0).negate()));
        }

        for (RationalPolynomial p : take(LIMIT, filterInfinite(q -> !q.isMonic(), P.rationalPolynomials()))) {
            try {
                p.powerSums();
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFromPowerSums() {
        initialize("fromPowerSums(List<Rational>)");
        Iterable<List<Rational>> rss = map(
                rs -> toList(cons(Rational.of(rs.size()), rs)),
                P.withScale(4).lists(P.rationals())
        );
        for (List<Rational> rs : take(LIMIT, rss)) {
            RationalPolynomial p = fromPowerSums(rs);
            p.validate();
            assertTrue(rs, p.isMonic());
            inverse(RationalPolynomial::fromPowerSums, RationalPolynomial::powerSums, rs);
        }

        Iterable<List<Rational>> rssFail = filterInfinite(
                ss -> !head(ss).equals(Rational.of(ss.size() - 1)),
                P.listsAtLeast(1, P.rationals())
        );
        for (List<Rational> rs : take(LIMIT, rssFail)) {
            try {
                fromPowerSums(rs);
                fail(rs);
            } catch (IllegalArgumentException ignored) {}
        }

        rssFail = map(rs -> toList(cons(Rational.of(rs.size()), rs)), P.listsWithElement(null, P.rationals()));
        for (List<Rational> rs : take(LIMIT, rssFail)) {
            try {
                fromPowerSums(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }

        for (List<Rational> rs : take(LIMIT, map(ss -> toList(cons(null, ss)), P.lists(P.rationals())))) {
            try {
                fromPowerSums(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesInterpolate() {
        initialize("interpolate(List<Pair<Rational, Rational>>)");
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
            RationalPolynomial p = interpolate(ps);
            p.validate();
            for (Pair<Rational, Rational> point : ps) {
                assertEquals(ps, p.apply(point.a), point.b);
            }
        }

        Iterable<Pair<RationalPolynomial, List<Rational>>> ps = P.dependentPairsInfinite(
                P.withScale(4).rationalPolynomials(),
                p -> P.withScale(p.degree() + 2).distinctListsAtLeast(p.degree() + 1, P.withScale(4).rationals())
        );
        for (Pair<RationalPolynomial, List<Rational>> p : take(LIMIT, ps)) {
            List<Pair<Rational, Rational>> points = toList(map(r -> new Pair<>(r, p.a.apply(r)), p.b));
            assertEquals(p, interpolate(points), p.a);
        }

        Iterable<List<Pair<Rational, Rational>>> pssFail = filterInfinite(
                qs -> (qs.contains(null) || any(q -> q.a == null || q.b == null, filter(Objects::nonNull, qs)))
                        && unique(map(q -> q.a, filter(Objects::nonNull, qs))),
                P.lists(P.withScale(2).withNull(P.pairs(P.withScale(2).withNull(P.rationals()))))
        );
        for (List<Pair<Rational, Rational>> qs : take(LIMIT, pssFail)) {
            try {
                interpolate(qs);
                fail(qs);
            } catch (NullPointerException ignored) {}
        }

        pssFail = filterInfinite(qs -> !unique(map(q -> q.a, qs)), P.lists(P.pairs(P.rationals())));
        for (List<Pair<Rational, Rational>> qs : take(LIMIT, pssFail)) {
            try {
                interpolate(qs);
                fail(qs);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCompanionMatrix() {
        initialize("companionMatrix()");
        for (RationalPolynomial p : take(LIMIT, P.monicRationalPolynomials())) {
            RationalMatrix companionMatrix = p.companionMatrix();
            assertTrue(
                    p,
                    companionMatrix.submatrix(
                            p.degree() < 2 ?
                                    Collections.emptyList() :
                                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(1, p.degree() - 1)),
                            p.degree() < 2 ?
                                    Collections.emptyList() :
                                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, p.degree() - 2))
                    ).isIdentity()
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.monicRationalPolynomialsAtLeast(1))) {
            RationalMatrix companionMatrix = p.companionMatrix();
            assertTrue(
                    p,
                    companionMatrix.submatrix(
                            Collections.singletonList(0),
                            p.degree() < 2 ?
                                    Collections.emptyList() :
                                    toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, p.degree() - 2))
                    ).isZero()
            );
        }

        for (RationalPolynomial p : take(LIMIT, P.withScale(4).withSecondaryScale(4).monicRationalPolynomials())) {
            inverse(RationalPolynomial::companionMatrix, RationalMatrix::characteristicPolynomial, p);
        }

        for (RationalPolynomial p : take(LIMIT, P.monicRationalPolynomials(1))) {
            RationalMatrix companionMatrix = p.companionMatrix();
            assertEquals(p, companionMatrix.height(), 1);
            assertEquals(p, companionMatrix.width(), 1);
            assertEquals(p, companionMatrix.get(0, 0), p.coefficient(0).negate());
        }

        for (RationalPolynomial p : take(LIMIT, filterInfinite(q -> !q.isMonic(), P.rationalPolynomials()))) {
            try {
                p.companionMatrix();
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCoefficientMatrix() {
        initialize("coefficientMatrix(List<RationalPolynomial>)");
        Iterable<List<RationalPolynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= Ordering.maximum(map(RationalPolynomial::degree, ps)) + 1,
                        P.listsAtLeast(1, P.rationalPolynomials())
                )
        );
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            RationalMatrix coefficientMatrix = coefficientMatrix(ps);
            assertEquals(ps, coefficientMatrix.height(), ps.size());
            assertTrue(ps, coefficientMatrix.height() <= coefficientMatrix.width());
            if (!ps.isEmpty()) {
                assertEquals(ps, coefficientMatrix.width(), Ordering.maximum(map(RationalPolynomial::degree, ps)) + 1);
                assertFalse(
                        ps,
                        coefficientMatrix.submatrix(
                                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, ps.size() - 1)),
                                Collections.singletonList(0)
                        ).isZero()
                );
            }
        }

        Iterable<List<RationalPolynomial>> pssFail = filterInfinite(
                ps -> ps.size() > Ordering.maximum(map(RationalPolynomial::degree, ps)) + 1,
                P.listsAtLeast(1, P.rationalPolynomials())
        );
        for (List<RationalPolynomial> ps : take(LIMIT, pssFail)) {
            try {
                coefficientMatrix(ps);
                fail(ps);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesReflect() {
        initialize("reflect()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial reflected = p.reflect();
            assertEquals(p, p.signum(), reflected.signum());
            involution(RationalPolynomial::reflect, p);
        }
    }

    private void propertiesTranslate() {
        initialize("translate(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            RationalPolynomial translated = p.a.translate(p.b);
            translated.validate();
            assertEquals(p, p.a.degree(), translated.degree());
            assertEquals(p, p.a.signum(), translated.signum());
            inverse(q -> q.translate(p.b), (RationalPolynomial q) -> q.translate(p.b.negate()), p.a);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            RationalPolynomial q = of(p.a);
            assertEquals(p, q, q.translate(p.b));
        }

        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.monicRationalPolynomials(), P.rationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.translate(p.b).isMonic());
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationals(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            RationalPolynomial translated = t.a.translate(t.b);
            assertEquals(t, translated.apply(t.c), t.a.apply(t.c.subtract(t.b)));
        }
    }

    private void propertiesStretch() {
        initialize("stretch(Rational)");
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.positiveRationals());
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial stretched = p.a.stretch(p.b);
            stretched.validate();
            assertEquals(p, p.a.degree(), stretched.degree());
            assertEquals(p, p.a.signum(), stretched.signum());
            inverse(q -> q.stretch(p.b), (RationalPolynomial q) -> q.stretch(p.b.invert()), p.a);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.positiveRationals()))) {
            RationalPolynomial q = of(p.a);
            assertEquals(p, q, q.stretch(p.b));
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.positiveRationals(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            RationalPolynomial stretched = t.a.stretch(t.b);
            assertEquals(t, stretched.apply(t.c), t.a.apply(t.c.divide(t.b)));
        }

        Iterable<Pair<RationalPolynomial, Rational>> psFail = P.pairs(
                P.rationalPolynomials(),
                P.rangeDown(Rational.ZERO)
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, psFail)) {
            try {
                p.a.stretch(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull List<RationalPolynomial> powerTable_simplest(@NotNull RationalPolynomial p, int maxPower) {
        return toList(map(p::rootPower, ExhaustiveProvider.INSTANCE.rangeIncreasing(0, maxPower)));
    }

    private static @NotNull List<RationalPolynomial> powerTable_alt(@NotNull RationalPolynomial p, int maxPower) {
        return toList(
                map(i -> of(Rational.ONE, i).divide(p).b, ExhaustiveProvider.INSTANCE.rangeIncreasing(0, maxPower))
        );
    }

    private void propertiesPowerTable() {
        initialize("powerTable(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomialsAtLeast(1),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            List<RationalPolynomial> powers = p.a.powerTable(p.b);
            assertEquals(p, powerTable_simplest(p.a, p.b), powers);
            assertEquals(p, powerTable_alt(p.a, p.b), powers);
            assertEquals(p, powers.size(), p.b + 1);
            assertTrue(p, all(q -> q.degree() < p.a.degree(), powers));
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairsLogarithmicOrder(
                filterInfinite(p -> p.degree() < 1, P.rationalPolynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.powerTable(p.b);
                fail(p);
            } catch (UnsupportedOperationException ignored) {}
        }

        psFail = P.pairsLogarithmicOrder(P.rationalPolynomialsAtLeast(1), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.powerTable(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsPowerTable() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, List<RationalPolynomial>>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> powerTable_simplest(p.a, p.b));
        functions.put("alt", p -> powerTable_alt(p.a, p.b));
        functions.put("standard", p -> p.a.powerTable(p.b));
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomialsAtLeast(1),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("powerTable(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private static @NotNull RationalPolynomial rootPower_simplest(@NotNull RationalPolynomial p, int power) {
        return last(p.powerTable(power));
    }

    private void propertiesRootPower() {
        initialize("rootPower(int)");
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialsAtLeast(1),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial rootPower = p.a.rootPower(p.b);
            assertEquals(p, rootPower_simplest(p.a, p.b), rootPower);
            assertTrue(p, rootPower.degree() < p.a.degree());
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairsLogarithmicOrder(
                filterInfinite(p -> p.degree() < 1, P.rationalPolynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rootPower(p.b);
                fail(p);
            } catch (UnsupportedOperationException ignored) {}
        }

        psFail = P.pairsLogarithmicOrder(P.rationalPolynomialsAtLeast(1), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rootPower(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsRootPower() {
        Map<String, Function<Pair<RationalPolynomial, Integer>, RationalPolynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> rootPower_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.rootPower(p.b));
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).rationalPolynomialsAtLeast(1),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("rootPower(int)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesRealRoots() {
        initialize("realRoots()");
        for (RationalPolynomial p : take(LIMIT, P.withScale(4).rationalPolynomialsAtLeast(0))) {
            List<Algebraic> realRoots = p.realRoots();
            realRoots.forEach(Algebraic::validate);
            assertTrue(p, Ordering.increasing(realRoots));
            assertEquals(p, realRoots.size(), p.constantFactor().b.rootCount());
            for (Algebraic root : realRoots) {
                assertEquals(p, p.apply(root), Algebraic.ZERO);
            }
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials(0))) {
            assertTrue(p, p.realRoots().isEmpty());
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(RationalPolynomial)");
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.subtract(p.b).signum());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            homomorphic(
                    RationalPolynomial::of,
                    RationalPolynomial::of,
                    Function.identity(),
                    Rational::compareTo,
                    RationalPolynomial::compareTo,
                    p
            );
        }

        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filterInfinite(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.rationalPolynomials()))
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            homomorphic(
                    RationalPolynomial::degree,
                    RationalPolynomial::degree,
                    Function.identity(),
                    RationalPolynomial::compareTo,
                    Integer::compareTo,
                    p
            );
        }
    }

    private void propertiesReadStrict_String() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_POLYNOMIAL_CHARS,
                P.rationalPolynomials(),
                RationalPolynomial::readStrict,
                RationalPolynomial::validate,
                false,
                true
        );
    }

    private void propertiesReadStrict_int_String() {
        initialize("readStrict(int, String)");

        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.positiveIntegersGeometric());
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            readStrict(p.b, p.a);
        }

        Iterable<Pair<RationalPolynomial, Integer>> ps2 = filterInfinite(
                p -> p.a.degree() <= p.b,
                P.pairsLogarithmicOrder(P.rationalPolynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<RationalPolynomial> op = readStrict(p.b, p.a.toString());
            RationalPolynomial q = op.get();
            q.validate();
            assertEquals(p, q, p.a);
        }

        ps2 = filterInfinite(
                p -> p.a.degree() > p.b,
                P.pairsLogarithmicOrder(P.rationalPolynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<RationalPolynomial> op = readStrict(p.b, p.a.toString());
            assertFalse(p, op.isPresent());
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(
                LIMIT,
                RATIONAL_POLYNOMIAL_CHARS,
                P.rationalPolynomials(),
                RationalPolynomial::readStrict
        );

        for (Rational r : take(LIMIT, P.rationals())) {
            homomorphic(
                    RationalPolynomial::of,
                    Function.identity(),
                    Rational::toString,
                    RationalPolynomial::toString,
                    r
            );
        }
    }
}
