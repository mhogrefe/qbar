package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.qbar.objects.RationalPolynomial.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class RationalPolynomialProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";

    public RationalPolynomialProperties() {
        super("RationalPolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesApply();
        compareImplementationsApply();
        propertiesCoefficient();
        propertiesOf_List_Rational();
        propertiesOf_Rational();
        propertiesOf_Rational_int();
        propertiesDegree();
        propertiesLeading();
        propertiesAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_RationalPolynomial();
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
        //propertiesProduct();
        propertiesDelta();
//        propertiesPow();
//        compareImplementationsPow();
        propertiesSubstitute();
        compareImplementationsSubstitute();
        propertiesIsMonic();
        propertiesMakeMonic();
        propertiesContentAndPrimitive();
        propertiesDivide_RationalPolynomial();
        compareImplementationsDivide_RationalPolynomial();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            List<Rational> rs = toList(p);
            assertTrue(p, all(r -> r != null, rs));
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
                zipWith((c, i) -> c == Rational.ZERO ? Rational.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private void propertiesApply() {
        initialize("apply(Rational)");
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p, y, apply_naive(p.a, p.b));
        }

        for (Rational i : take(LIMIT, P.rationals())) {
            assertEquals(i, ZERO.apply(i), Rational.ZERO);
            fixedPoint(X::apply, i);
            assertEquals(i, of(Rational.NEGATIVE_ONE, 1).apply(i), i.negate());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p, p.apply(Rational.ZERO), p.coefficient(0));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.apply(Rational.ONE), Rational.sum(p));
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

    private void compareImplementationsApply() {
        Map<String, Function<Pair<RationalPolynomial, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> apply_naive(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        Iterable<Pair<RationalPolynomial, Rational>> ps = P.pairs(P.rationalPolynomials(), P.rationals());
        compareImplementations("apply(Rational)", take(LIMIT, ps), functions);
    }

    private void propertiesCoefficient() {
        initialize("");
        System.out.println("\t\ttesting coefficient(int) properties...");

        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationalPolynomials(), P.withScale(10).naturalIntegersGeometric());
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filter(q -> q.b <= q.a.degree(), ps))) {
            assertEquals(p, p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filter(q -> q.b > q.a.degree(), ps))) {
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
        initialize("");
        System.out.println("\t\ttesting of(List<Rational>) properties");

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalPolynomial p = of(rs);
            p.validate();
            assertTrue(rs, p.degree() < rs.size());
        }

        Iterable<List<Rational>> rss = filter(rs -> rs.isEmpty() || last(rs) != Rational.ZERO, P.lists(P.rationals()));
        for (List<Rational> rs : take(LIMIT, rss)) {
            assertEquals(rs, toList(of(rs)), rs);
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                of(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_Rational() {
        initialize("");
        System.out.println("\t\ttesting of(Rational) properties");

        for (Rational r : take(LIMIT, P.rationals())) {
            RationalPolynomial p = of(r);
            p.validate();
            assertTrue(r, p.degree() == 0 || p.degree() == -1);
        }

        for (Rational r : take(LIMIT, filter(j -> j != Rational.ZERO, P.rationals()))) {
            assertEquals(r, of(r).coefficient(0), r);
        }
    }

    private void propertiesOf_Rational_int() {
        initialize("");
        System.out.println("\t\ttesting of(Rational, int) properties");

        Iterable<Pair<Rational, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsLogarithmicOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationals(), P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = of(p.a, p.b);
            q.validate();
        }

        for (Pair<Rational, Integer> p : take(LIMIT, filter(q -> q.a != Rational.ZERO, ps))) {
            RationalPolynomial q = of(p.a, p.b);
            List<Rational> coefficients = toList(q);
            assertEquals(p, length(filter(r -> r != Rational.ZERO, coefficients)), 1);
            assertEquals(p, q.degree(), p.b.intValue());
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

    private void propertiesDegree() {
        initialize("");
        System.out.println("\t\ttesting degree() properties");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }
    }

    private void propertiesLeading() {
        initialize("");
        System.out.println("\t\ttesting leading() properties");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.leading();
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            Rational leading = p.leading().get();
            assertNotEquals(p, leading, Rational.ZERO);
        }

        for (Rational r : take(LIMIT, filter(j -> j != Rational.ZERO, P.rationals()))) {
            RationalPolynomial p = of(r);
            assertEquals(r, p.leading().get(), p.coefficient(0));
        }
    }

    private void propertiesAdd() {
        initialize("");
        System.out.println("\t\ttesting add(Polynomial) properties...");

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, sum, p.b.add(p.a));
            assertEquals(p, sum.subtract(p.b), p.a);
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
            assertEquals(p, of(p.a).add(of(p.b)), of(p.a.add(p.b)));
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
            RationalPolynomial sum1 = t.a.add(t.b).add(t.c);
            RationalPolynomial sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t, sum1, sum2);
        }
    }

    private void propertiesNegate() {
        initialize("");
        System.out.println("\t\ttesting negate() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial negative = p.negate();
            negative.validate();
            assertEquals(p, negative.degree(), p.degree());
            assertEquals(p, p, negative.negate());
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            assertEquals(p, p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).negate(), of(r.negate()));
        }

        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            RationalPolynomial negative = p.negate();
            assertNotEquals(p, p, negative);
        }
    }

    private void propertiesAbs() {
        initialize("");
        System.out.println("\t\ttesting abs() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial abs = p.abs();
            abs.validate();
            assertEquals(p, abs.degree(), p.degree());
            assertEquals(p, abs, abs.abs());
            assertNotEquals(p, abs.signum(), -1);
            assertTrue(p, ge(abs, ZERO));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).abs(), of(r.abs()));
        }
    }

    private void propertiesSignum() {
        initialize("");
        System.out.println("\t\ttesting signum() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int signum = p.signum();
            assertEquals(p, signum, Ordering.compare(p, ZERO).toInt());
            assertTrue(p, signum == -1 || signum == 0 || signum == 1);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).signum(), r.signum());
        }
    }

    private static @NotNull RationalPolynomial subtract_simplest(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("");
        System.out.println("\t\ttesting subtract(RationalPolynomial) properties...");

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            assertEquals(p, difference, p.b.subtract(p.a).negate());
            assertEquals(p, p.a, difference.add(p.b));
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
            assertEquals(p, of(p.a).subtract(of(p.b)), of(p.a.subtract(p.b)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            assertEquals(p, p.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        initialize("");
        System.out.println("\t\tcomparing subtract(RationalPolynomial) implementations...");

        long totalTime = 0;
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            long time = System.nanoTime();
            subtract_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            long time = System.nanoTime();
            p.a.subtract(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesMultiply_RationalPolynomial() {
        initialize("");
        System.out.println("\t\ttesting multiply(RationalPolynomial) properties...");

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            assertEquals(p, product, p.b.multiply(p.a));
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
            assertEquals(p, of(p.a).multiply(of(p.b)), of(p.a.multiply(p.b)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, ONE.multiply(p), p);
            assertEquals(p, p.multiply(ONE), p);
            assertTrue(p, ZERO.multiply(p) == ZERO);
            assertTrue(p, p.multiply(ZERO) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts2 = P.triples(
                P.rationalPolynomials()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts2)) {
            RationalPolynomial product1 = t.a.multiply(t.b).multiply(t.c);
            RationalPolynomial product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t, product1, product2);
            RationalPolynomial expression1 = t.a.add(t.b).multiply(t.c);
            RationalPolynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_Rational() {
        initialize("");
        System.out.println("\t\ttesting multiply(Rational) properties...");

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
            assertEquals(p, of(p.a).multiply(p.b), of(p.a.multiply(p.b)));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ONE.multiply(r), of(r));
            assertTrue(r, ZERO.multiply(r) == ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.multiply(Rational.ONE), p);
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
        initialize("");
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

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
            assertEquals(p, of(p.a).multiply(p.b), of(p.a.multiply(p.b)));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
            assertTrue(i, ZERO.multiply(i) == ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.multiply(BigInteger.ONE), p);
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
        initialize("");
        System.out.println("\t\ttesting multiply(int) properties...");

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
            assertEquals(p, of(p.a).multiply(p.b), of(p.a.multiply(BigInteger.valueOf(p.b))));
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(Rational.of(i)));
            assertTrue(i, ZERO.multiply(i) == ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.multiply(1), p);
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
        initialize("");
        System.out.println("\t\ttesting divide(Rational) properties...");

        Iterable<Pair<RationalPolynomial, Rational>> ps = filter(
                p -> p.b != Rational.ZERO,
                P.pairs(P.rationalPolynomials(), P.rationals())
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            assertEquals(p, p.a, quotient.multiply(p.b));
            assertEquals(p, quotient, p.a.multiply(p.b.invert()));
        }

        Iterable<Triple<RationalPolynomial, Rational, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                filter(r -> r != Rational.ZERO, P.rationals()),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Rational, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        Iterable<Pair<Rational, Rational>> ps2 = P.pairs(
                P.rationals(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<Rational, Rational> p : take(LIMIT, ps2)) {
            assertEquals(p, of(p.a).divide(p.b), of(p.a.divide(p.b)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.divide(Rational.ONE), p);
        }

        Iterable<Rational> rs = filter(r -> r != Rational.ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r, ZERO.divide(r), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(Rational.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_BigInteger() {
        initialize("");
        System.out.println("\t\ttesting divide(BigInteger) properties...");

        Iterable<Pair<RationalPolynomial, BigInteger>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<RationalPolynomial, BigInteger> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            assertEquals(p, p.a, quotient.multiply(p.b));
        }

        Iterable<Triple<RationalPolynomial, BigInteger, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers()),
                P.rationals()
        );
        for (Triple<RationalPolynomial, BigInteger, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        Iterable<Pair<Rational, BigInteger>> ps2 = P.pairs(
                P.rationals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assertEquals(p, of(p.a).divide(p.b), of(p.a.divide(p.b)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.divide(BigInteger.ONE), p);
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertEquals(i, ZERO.divide(i), ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            try {
                p.divide(BigInteger.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivide_int() {
        initialize("");
        System.out.println("\t\ttesting divide(int) properties...");

        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(i -> i != 0, P.integers())
        );
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial quotient = p.a.divide(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            assertEquals(p, p.a, quotient.multiply(p.b));
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                filter(i -> i != 0, P.integers()),
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.divide(t.b).apply(t.c), t.a.apply(t.c).divide(t.b));
        }

        Iterable<Pair<Rational, Integer>> ps2 = P.pairs(P.rationals(), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps2)) {
            assertEquals(p, of(p.a).divide(p.b), of(p.a.divide(BigInteger.valueOf(p.b))));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.divide(1), p);
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertEquals(i, ZERO.divide(i), ZERO);
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
        initialize("");
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.withScale(50).integersGeometric();
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            RationalPolynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.shiftLeft(0), p);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                is,
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            assertEquals(p, of(p.a).shiftLeft(p.b), of(p.a.shiftLeft(p.b)));
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = P.withScale(50).naturalIntegersGeometric();
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            RationalPolynomial shifted = p.a.shiftLeft(p.b);
            assertEquals(p, shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftLeft() {
        initialize("");
        System.out.println("\t\tcomparing shiftLeft(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.withScale(50).integersGeometric();
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            long time = System.nanoTime();
            shiftLeft_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            long time = System.nanoTime();
            p.a.shiftLeft(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull RationalPolynomial shiftRight_simplest(@NotNull RationalPolynomial p, int bits) {
        if (bits < 0) {
            return p.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return p.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("");
        System.out.println("\t\ttesting shiftRight(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.withScale(50).integersGeometric();
        }
        Iterable<Pair<RationalPolynomial, Integer>> ps = P.pairs(P.rationalPolynomials(), is);
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            aeqit(p.toString(), map(Rational::signum, p.a), map(Rational::signum, shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.shiftRight(0), p);
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                is,
                P.rationals()
        );
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftRight(t.b).apply(t.c), t.a.apply(t.c).shiftRight(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            assertEquals(p, of(p.a).shiftRight(p.b), of(p.a.shiftRight(p.b)));
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = P.withScale(50).naturalIntegersGeometric();
        }
        ps = P.pairs(P.rationalPolynomials(), is);
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial shifted = p.a.shiftRight(p.b);
            assertEquals(p, shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftRight() {
        initialize("");
        System.out.println("\t\tcomparing shiftRight(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = P.withScale(50).integersGeometric();
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            long time = System.nanoTime();
            shiftRight_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, P.pairs(P.rationalPolynomials(), is))) {
            long time = System.nanoTime();
            p.a.shiftRight(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull RationalPolynomial sum_simplest(@NotNull Iterable<RationalPolynomial> xs) {
        return foldl(RationalPolynomial::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("");
        System.out.println("\t\ttesting sum(Iterable<RationalPolynomial>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationalPolynomials(),
                RationalPolynomial::add,
                RationalPolynomial::sum,
                rp -> {},
                true,
                true
        );

        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            RationalPolynomial sum = sum(ps);
            assertTrue(ps, ps.isEmpty() || sum.degree() <= maximum(map(RationalPolynomial::degree, ps)));
            assertEquals(ps, sum, sum_simplest(ps));
        }

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps = P.pairs(
                P.lists(P.rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps)) {
            assertEquals(p, sum(p.a).apply(p.b), Rational.sum(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            assertEquals(rs, sum(map(RationalPolynomial::of, rs)), of(Rational.sum(rs)));
        }
    }

    private void compareImplementationsSum() {
        initialize("");
        System.out.println("\t\tcomparing sum(Iterable<RationalPolynomial) implementations...");

        long totalTime = 0;
        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            long time = System.nanoTime();
            sum_simplest(ps);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (List<RationalPolynomial> ps : take(LIMIT, P.lists(P.rationalPolynomials()))) {
            long time = System.nanoTime();
            sum(ps);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesProduct() {
        initialize("");
        System.out.println("\t\ttesting product(Iterable<RationalPolynomial>) properties...");

        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.withScale(10).rationalPolynomials(),
                RationalPolynomial::multiply,
                RationalPolynomial::product,
                rp -> {},
                true,
                true
        );

        Iterable<List<RationalPolynomial>> pss;
        if (P instanceof QBarExhaustiveProvider) {
            pss = P.lists(P.rationalPolynomials());
        } else {
            pss = P.lists(P.withScale(10).rationalPolynomials());
        }
        for (List<RationalPolynomial> ps : take(LIMIT, pss)) {
            RationalPolynomial product = product(ps);
            assertTrue(
                    ps,
                    any(p -> p == ZERO, ps) ||
                            product.degree() == IterableUtils.sumInteger(map(RationalPolynomial::degree, ps))
            );
        }

        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, P.pairs(pss, P.rationals()))) {
            assertEquals(p, product(p.a).apply(p.b), Rational.product(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            assertEquals(rs, product(map(RationalPolynomial::of, rs)), of(Rational.product(rs)));
        }
    }

    private void propertiesDelta() {
        initialize("");
        System.out.println("\t\ttesting delta(Iterable<RationalPolynomial>) properties...");

        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.rationalPolynomials(),
                P.rationalPolynomials(),
                RationalPolynomial::negate,
                RationalPolynomial::subtract,
                RationalPolynomial::delta,
                rp -> {}
        );

        Iterable<Pair<List<RationalPolynomial>, Rational>> ps = P.pairs(
                P.listsAtLeast(1, P.rationalPolynomials()),
                P.rationals()
        );
        for (Pair<List<RationalPolynomial>, Rational> p : take(LIMIT, ps)) {
            aeqit(p.toString(), map(q -> q.apply(p.b), delta(p.a)), Rational.delta(map(q -> q.apply(p.b), p.a)));
        }

        for (List<Rational> rs : take(LIMIT, P.listsAtLeast(1, P.rationals()))) {
            aeqit(
                    rs.toString(),
                    delta(map(RationalPolynomial::of, rs)),
                    map(RationalPolynomial::of, Rational.delta(rs))
            );
        }
    }

    private static @NotNull RationalPolynomial pow_simplest(@NotNull RationalPolynomial a, int p) {
        return product(replicate(p, a));
    }

    private void propertiesPow() {
        initialize("");
        System.out.println("\t\ttesting pow(int) properties...");

        Iterable<Integer> exps;
        Iterable<RationalPolynomial> rps;
        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            rps = P.rationalPolynomials();
            exps = P.naturalIntegers();
            ps = P.pairsLogarithmicOrder(rps, exps);
        } else {
            rps = P.withScale(10).rationalPolynomials();
            exps = P.withScale(5).naturalIntegersGeometric();
            ps = P.pairs(rps, exps);
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<RationalPolynomial, Integer, Rational>> ts1 = P.triples(rps, exps, P.rationals());
        for (Triple<RationalPolynomial, Integer, Rational> t : take(LIMIT, ts1)) {
            assertEquals(t, t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), exps))) {
            assertEquals(p, of(p.a).pow(p.b), of(p.a.pow(p.b)));
        }

        Iterable<Integer> pexps;
        if (P instanceof QBarExhaustiveProvider) {
            pexps = P.positiveIntegers();
        } else {
            pexps = P.withScale(20).positiveIntegersGeometric();
        }
        for (int i : take(LIMIT, pexps)) {
            assertTrue(i, ZERO.pow(i) == ZERO);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            assertEquals(p, p.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

            //todo fix hanging
//        if (P instanceof QBarRandomProvider) {
//            exps = P.withScale(2).naturalIntegersGeometric();
//        }
//        Iterable<Triple<RationalPolynomial, Integer, Integer>> ts2 = P.triples(rps, exps, exps);
//        for (Triple<RationalPolynomial, Integer, Integer> t : take(LIMIT, ts2)) {
//            System.out.println(t);
//            RationalPolynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
//            RationalPolynomial expression2 = t.a.pow(t.b + t.c);
//            assertEquals(t, expression1, expression2);
//            RationalPolynomial expression5 = t.a.pow(t.b).pow(t.c);
//            RationalPolynomial expression6 = t.a.pow(t.b * t.c);
//            assertEquals(t, expression5, expression6);
//        }
//
//        for (Triple<RationalPolynomial, RationalPolynomial, Integer> t : take(LIMIT, P.triples(rps, rps, exps))) {
//            RationalPolynomial expression1 = t.a.multiply(t.b).pow(t.c);
//            RationalPolynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
//            assertEquals(t, expression1, expression2);
//        }
    }

    private void compareImplementationsPow() {
        initialize("");
        System.out.println("\t\tcomparing pow(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> exps;
        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.naturalIntegers();
            ps = P.pairsLogarithmicOrder(P.rationalPolynomials(), exps);
        } else {
            exps = P.withScale(5).naturalIntegersGeometric();
            ps = P.pairs(P.withScale(10).rationalPolynomials(), exps);
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            pow_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.pow(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull RationalPolynomial substitute_naive(
            @NotNull RationalPolynomial a,
            @NotNull RationalPolynomial b
    ) {
        return sum(zipWith((c, i) -> c == Rational.ZERO ? ZERO : b.pow(i).multiply(c), a, rangeUp(0)));
    }

    private void propertiesSubstitute() {
        initialize("");
        System.out.println("\t\ttesting substitute(RationalPolynomial) properties...");

        Iterable<RationalPolynomial> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.rationalPolynomials();
        } else {
            ps = P.withScale(6).rationalPolynomials();
        }
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(ps))) {
            RationalPolynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(p, p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree());
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(ps, ps, P.rationals());
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<Rational, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationals(), P.rationalPolynomials()))) {
            assertEquals(p, of(p.a).substitute(p.b), of(p.a));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p, p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p, p.substitute(ONE), of(Rational.sum(p)));
            assertEquals(p, p.substitute(X), p);
        }
    }

    private void compareImplementationsSubstitute() {
        initialize("");
        System.out.println("\t\tcomparing substitute(RationalPolynomial) implementations...");

        long totalTime = 0;
        Iterable<RationalPolynomial> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.rationalPolynomials();
        } else {
            ps = P.withScale(6).rationalPolynomials();
        }
        if (P instanceof QBarExhaustiveProvider) {
            for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(ps))) {
                long time = System.nanoTime();
                substitute_naive(p.a, p.b);
                totalTime += (System.nanoTime() - time);
            }
            System.out.println("\t\t\tnaïve: " + ((double) totalTime) / 1e9 + " s");
        } else {
            System.out.println("\t\t\tnaïve: too long");
        }

        totalTime = 0;
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(ps))) {
            long time = System.nanoTime();
            p.a.substitute(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesIsMonic() {
        initialize("");
        System.out.println("\t\ttesting isMonic() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.isMonic();
        }
    }

    private void propertiesMakeMonic() {
        initialize("");
        System.out.println("\t\ttesting makeMonic() properties...");

        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            RationalPolynomial monic = p.makeMonic();
            monic.validate();
            assertEquals(p, monic.degree(), p.degree());
            assertTrue(p, monic.isMonic());
            assertEquals(p, monic.makeMonic(), monic);
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
                filter(q -> q != ZERO, P.rationalPolynomials()),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, ps)) {
            RationalPolynomial monic = p.a.makeMonic();
            assertEquals(p, p.a.multiply(p.b).makeMonic(), monic);
        }
    }

    private void propertiesContentAndPrimitive() {
        initialize("");
        System.out.println("\t\ttesting contentAndPrimitive() properties...");

        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            Pair<Rational, Polynomial> contentAndPrimitive = p.contentAndPrimitive();
            Rational content = contentAndPrimitive.a;
            assertNotNull(p, content);
            Polynomial primitive = contentAndPrimitive.b;
            assertEquals(p, primitive.degree(), p.degree());
            assertNotNull(p, primitive);
            assertNotEquals(p, content, BigInteger.ZERO);
            assertTrue(p, primitive.isPrimitive());
            assertEquals(p, primitive.toRationalPolynomial().multiply(content), p);

            Pair<BigInteger, Polynomial> primitiveCP = primitive.contentAndPrimitive();
            assertEquals(p, primitiveCP.a, BigInteger.ONE);
            assertEquals(p, primitive, primitiveCP.b);
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
        initialize("");
        System.out.println("\t\ttesting divide(RationalPolynomial) properties...");

        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(q -> q != ZERO, P.rationalPolynomials())
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
            assertTrue(p, p.b == ZERO || remainder.degree() < p.b.degree());
        }

        Iterable<Pair<Rational, Rational>> ps2 = P.pairs(
                P.rationals(),
                filter(r -> r != Rational.ZERO, P.rationals())
        );
        for (Pair<Rational, Rational> p : take(LIMIT, ps2)) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = of(p.a).divide(of(p.b));
            assertEquals(p, quotRem.a, of(p.a.divide(p.b)));
            assertTrue(p, quotRem.b == ZERO);
        }

        ps = filter(q -> q.a.degree() < q.b.degree(), ps);
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            Pair<RationalPolynomial, RationalPolynomial> quotRem = p.a.divide(p.b);
            assertTrue(p, quotRem.a == ZERO);
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
        initialize("");
        System.out.println("\t\tcomparing divide(RationalPolynomial) implementations...");

        long totalTime = 0;
        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = P.pairs(
                P.rationalPolynomials(),
                filter(q -> q != ZERO, P.rationalPolynomials())
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            divide_RationalPolynomial_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.divide(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private void propertiesEquals() {
        initialize("");
        System.out.println("\t\ttesting equals(Object) properties...");

        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).equals(of(p.b)), p.a.equals(p.b));
        }
    }

    private void propertiesHashCode() {
        initialize("");
        System.out.println("\t\ttesting hashCode() properties...");

        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);
    }

    private void propertiesCompareTo() {
        initialize("");
        System.out.println("\t\ttesting compareTo(RationalPolynomial) properties...");

        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationalPolynomials);

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            int compare = p.a.compareTo(p.b);
            assertEquals(p, p.a.subtract(p.b).signum(), compare);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, of(p.a).compareTo(of(p.b)), p.a.compareTo(p.b));
        }

        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filter(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.rationalPolynomials()))
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            assertEquals(p, compare(p.a, p.b), compare(p.a.degree(), p.b.degree()));
        }
    }

    private void propertiesRead() {
        initialize("");
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            Optional<RationalPolynomial> op = read(p.toString());
            assertEquals(p, op.get(), p);
        }

        Iterable<String> ss = filter(s -> read(MEDIUM_LIMIT, s).isPresent(), P.strings(RATIONAL_POLYNOMIAL_CHARS));
        for (String s : take(LIMIT, ss)) {
            Optional<RationalPolynomial> op = read(s);
            op.get().validate();
        }
    }

    private void propertiesFindIn() {
        initialize("");
        System.out.println("\t\ttesting findIn(String) properties...");

        propertiesFindInHelper(
                LIMIT, P.getWheelsProvider(),
                P.rationalPolynomials(),
                s -> read(MEDIUM_LIMIT, s),
                s -> findIn(MEDIUM_LIMIT, s),
                rp -> {}
        );
    }

    private void propertiesToString() {
        initialize("");
        System.out.println("\t\ttesting toString() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            String s = p.toString();
            assertTrue(p, isSubsetOf(s, RATIONAL_POLYNOMIAL_CHARS));
            Optional<RationalPolynomial> readP = read(s);
            assertTrue(p, readP.isPresent());
            assertEquals(p, readP.get(), p);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r).toString(), r.toString());
        }
    }
}
