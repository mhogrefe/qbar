package mho.qbar.objects;

import jas.JasApi;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.Polynomial.*;
import static mho.qbar.objects.Polynomial.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class PolynomialProperties extends QBarTestProperties {
    private static final @NotNull String POLYNOMIAL_CHARS = "*+-0123456789^x";

    public PolynomialProperties() {
        super("Polynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterator();
        propertiesApply_BigInteger();
        compareImplementationsApply_BigInteger();
        propertiesApply_Rational();
        compareImplementationsApply_Rational();
        propertiesSpecialApply();
        compareImplementationsSpecialApply();
        propertiesToRationalPolynomial();
        propertiesCoefficient();
        propertiesOf_List_BigInteger();
        propertiesOf_BigInteger();
        propertiesOf_BigInteger_int();
        propertiesMaxCoefficientBitLength();
        propertiesDegree();
        propertiesLeading();
        propertiesAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSignum_BigInteger();
        compareImplementationsSignum_BigInteger();
        propertiesSignum_Rational();
        compareImplementationsSignum_Rational();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_Polynomial();
        compareImplementationsMultiply_Polynomial();
        propertiesMultiply_BigInteger();
        propertiesMultiply_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
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
        propertiesIsPrimitive();
        propertiesContentAndPrimitive();
        propertiesConstantFactor();
        propertiesPseudoDivide();
        compareImplementationsPseudoDivide();
        propertiesPseudoRemainder();
        compareImplementationsPseudoRemainder();
        propertiesAbsolutePseudoDivide();
        compareImplementationsAbsolutePseudoDivide();
        propertiesAbsolutePseudoRemainder();
        compareImplementationsAbsolutePseudoRemainder();
        propertiesIsDivisibleBy();
        compareImplementationsIsDivisibleBy();
        propertiesDivideExact();
        compareImplementationsDivideExact();
        propertiesRemainderExact();
        compareImplementationsRemainderExact();
        propertiesTrivialPseudoRemainderSequence();
        propertiesPrimitivePseudoRemainderSequence();
        propertiesSubresultantSequence();
        propertiesGcd_Polynomial();
        compareImplementationsGcd_Polynomial1();
        compareImplementationsGcd_Polynomial2();
        propertiesGcd_List_Polynomial();
        propertiesLcm();
        propertiesIsRelativelyPrimeTo();
        compareImplementationsIsRelativelyPrimeTo();
        propertiesIsSquareFree();
        compareImplementationsIsSquareFree();
        propertiesSquareFreePart();
        compareImplementationsSquareFreePart();
        propertiesSquareFreeFactor();
        propertiesFactor();
        compareImplementationsFactor1();
        compareImplementationsFactor2();
        propertiesIsIrreducible();
        compareImplementationsIsIrreducible(false);
        compareImplementationsIsIrreducible(true);
        propertiesInterpolate();
        compareImplementationsInterpolate();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead_String();
        propertiesRead_int_String();
        propertiesFindIn_String();
        propertiesFindIn_int_String();
        propertiesToString();
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            List<BigInteger> is = toList(p);
            assertTrue(p, all(r -> r != null, is));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<BigInteger> js) -> of(js), p);
            testNoRemove(p);
            testHasNext(p);
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            List<BigInteger> is = toList(p);
            assertTrue(p, !last(is).equals(BigInteger.ZERO));
        }
    }

    private static @NotNull BigInteger apply_BigInteger_naive(@NotNull Polynomial p, @NotNull BigInteger x) {
        return sumBigInteger(
                zipWith((c, i) -> c.equals(BigInteger.ZERO) ? BigInteger.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private void propertiesApply_BigInteger() {
        initialize("apply(BigInteger)");
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            BigInteger y = p.a.apply(p.b);
            assertEquals(p, y, apply_BigInteger_naive(p.a, p.b));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ZERO.apply(i), BigInteger.ZERO);
            fixedPoint(X::apply, i);
            assertEquals(i, of(IntegerUtils.NEGATIVE_ONE, 1).apply(i), i.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.apply(BigInteger.ZERO), p.coefficient(0));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.apply(BigInteger.ONE), sumBigInteger(p));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p, of(p.a).apply(p.b), p.a);
            assertEquals(p, of(Arrays.asList(p.a, BigInteger.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p, of(Arrays.asList(p.a.negate(), BigInteger.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        for (Pair<Integer, BigInteger> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.bigIntegers()))) {
            assertEquals(p, of(BigInteger.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private void compareImplementationsApply_BigInteger() {
        Map<String, Function<Pair<Polynomial, BigInteger>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> apply_BigInteger_naive(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        compareImplementations("apply(BigInteger)", take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers())), functions);
    }

    private static @NotNull Rational apply_Rational_simplest(@NotNull Polynomial p, @NotNull Rational x) {
        return p.toRationalPolynomial().apply(x);
    }

    private static @NotNull Rational apply_Rational_naive(@NotNull Polynomial p, @NotNull Rational x) {
        return Rational.sum(
                zipWith((c, i) -> c.equals(BigInteger.ZERO) ? Rational.ZERO : x.pow(i).multiply(c), p, rangeUp(0))
        );
    }

    private static @NotNull Rational apply_Rational_alt(@NotNull Polynomial p, @NotNull Rational x) {
        return foldr((c, y) -> x.multiply(y).add(Rational.of(c)), Rational.ZERO, p);
    }

    private static @NotNull Rational apply_Rational_alt2(@NotNull Polynomial p, @NotNull Rational x) {
        if (x.isInteger()) {
            return Rational.of(p.apply(x.bigIntegerValueExact()));
        } else {
            return foldr((c, y) -> x.multiply(y).add(Rational.of(c)), Rational.ZERO, p);
        }
    }

    private void propertiesApply_Rational() {
        initialize("apply(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p, y, apply_Rational_simplest(p.a, p.b));
            assertEquals(p, y, apply_Rational_naive(p.a, p.b));
            assertEquals(p, y, apply_Rational_alt(p.a, p.b));
            assertEquals(p, y, apply_Rational_alt2(p.a, p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.apply(r), Rational.ZERO);
            fixedPoint(X::apply, r);
            assertEquals(r, of(IntegerUtils.NEGATIVE_ONE, 1).apply(r), r.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.apply(Rational.ZERO), Rational.of(p.coefficient(0)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.apply(Rational.ONE), Rational.of(sumBigInteger(p)));
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            assertEquals(p, of(p.a).apply(p.b), Rational.of(p.a));
            assertEquals(p, of(Arrays.asList(p.a, BigInteger.ONE)).apply(p.b), p.b.add(Rational.of(p.a)));
            assertEquals(
                    p,
                    of(Arrays.asList(p.a.negate(), BigInteger.ONE)).apply(p.b),
                    p.b.subtract(Rational.of(p.a))
            );
            assertEquals(p, of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.rationals()))) {
            assertEquals(p, of(BigInteger.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private void compareImplementationsApply_Rational() {
        Map<String, Function<Pair<Polynomial, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> apply_Rational_simplest(p.a, p.b));
        functions.put("naïve", p -> apply_Rational_naive(p.a, p.b));
        functions.put("alt", p -> apply_Rational_alt(p.a, p.b));
        functions.put("alt2", p -> apply_Rational_alt2(p.a, p.b));
        functions.put("standard", p -> p.a.apply(p.b));
        compareImplementations("apply(Rational)", take(LIMIT, P.pairs(P.polynomials(), P.rationals())), functions);
    }

    private static @NotNull BigInteger specialApply_simplest(@NotNull Polynomial p, @NotNull Rational x) {
        if (p == ZERO) {
            return BigInteger.ZERO;
        } else {
            return p.apply(x).multiply(x.getDenominator().pow(p.degree())).bigIntegerValueExact();
        }
    }

    private void propertiesSpecialApply() {
        initialize("specialApply(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            BigInteger y = p.a.specialApply(p.b);
            assertEquals(p, y, specialApply_simplest(p.a, p.b));
            if (p.a != ZERO) {
                assertTrue(
                        p,
                        y.bitLength() <=
                                p.a.maxCoefficientBitLength() + p.b.bitLength() * p.a.degree() +
                                BigInteger.valueOf(p.a.degree() + 1).bitLength()
                );
            }
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p, p.a.specialApply(Rational.of(p.b)), p.a.apply(p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.specialApply(r), BigInteger.ZERO);
            assertEquals(r, X.specialApply(r), r.getNumerator());
            assertEquals(r, of(IntegerUtils.NEGATIVE_ONE, 1).specialApply(r), r.getNumerator().negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.specialApply(Rational.ZERO), p.coefficient(0));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.specialApply(Rational.ONE), sumBigInteger(p));
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            assertEquals(p, of(p.a).specialApply(p.b), p.a);
        }

        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(P.naturalIntegersGeometric(), P.rationals()))) {
            assertEquals(p, of(BigInteger.ONE, p.a).specialApply(p.b), p.b.getNumerator().pow(p.a));
        }
    }

    private void compareImplementationsSpecialApply() {
        Map<String, Function<Pair<Polynomial, Rational>, BigInteger>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> specialApply_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.specialApply(p.b));
        compareImplementations(
                "specialApply(Rational)",
                take(LIMIT, P.pairs(P.polynomials(), P.rationals())),
                functions
        );
    }

    private void propertiesToRationalPolynomial() {
        initialize("toRationalPolynomial()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            RationalPolynomial rp = p.toRationalPolynomial();
            assertEquals(p, p.toString(), rp.toString());
            assertEquals(p, p.degree(), rp.degree());
            inverse(Polynomial::toRationalPolynomial, RationalPolynomial::toPolynomial, p);
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p, p.a.apply(p.b), p.a.toRationalPolynomial().apply(Rational.of(p.b)).bigIntegerValueExact());
        }

        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            assertEquals(p, p.a.apply(p.b), p.a.toRationalPolynomial().apply(p.b));
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.polynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        ps = filterInfinite(
                q -> q.b <= q.a.degree(),
                P.pairsLogarithmicOrder(P.polynomials(), P.naturalIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        ps = filterInfinite(
                q -> q.b > q.a.degree(),
                P.pairsLogarithmicOrder(P.polynomials(), P.naturalIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.coefficient(p.b), BigInteger.ZERO);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.coefficient(p.b);
                fail(p);
            } catch (IndexOutOfBoundsException ignored) {}
        }
    }

    private void propertiesOf_List_BigInteger() {
        initialize("of(List<BigInteger>)");
        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            Polynomial p = of(is);
            p.validate();
            assertTrue(is, p.degree() < is.size());
        }

        Iterable<List<BigInteger>> iss = filterInfinite(
                is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                P.lists(P.bigIntegers())
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            fixedPoint(js -> toList(of(js)), is);
        }

        for (List<BigInteger> is : take(LIMIT, P.listsWithElement(null, P.bigIntegers()))) {
            try {
                of(is);
                fail(is);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Polynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() == 0 || p.degree() == -1);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, of(i).coefficient(0), i);
        }
    }

    private void propertiesOf_BigInteger_int() {
        initialize("of(BigInteger, int)");
        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            Polynomial q = of(p.a, p.b);
            q.validate();
        }

        ps = P.pairsLogarithmicOrder(P.nonzeroBigIntegers(), P.naturalIntegersGeometric());
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            Polynomial q = of(p.a, p.b);
            assertTrue(p, all(c -> c.equals(BigInteger.ZERO), init(q)));
            assertEquals(p, q.degree(), p.b);
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            assertTrue(i, of(BigInteger.ZERO, i) == ZERO);
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.negativeIntegers()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesMaxCoefficientBitLength() {
        initialize("maxCoefficientBitLength()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertTrue(p, p.maxCoefficientBitLength() >= 0);
            homomorphic(
                    Polynomial::negate,
                    Function.identity(),
                    Polynomial::maxCoefficientBitLength,
                    Polynomial::maxCoefficientBitLength,
                    p
            );
        }
    }

    private void propertiesDegree() {
        initialize("degree()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int degree = p.degree();
            assertTrue(p, degree >= -1);
        }
    }

    private void propertiesLeading() {
        initialize("leading()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.leading();
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            BigInteger leading = p.leading().get();
            assertNotEquals(p, leading, BigInteger.ZERO);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            Polynomial p = of(i);
            assertEquals(i, p.leading().get(), p.coefficient(0));
        }
    }

    private void propertiesAdd() {
        initialize("add(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial sum = p.a.add(p.b);
            sum.validate();
            assertTrue(p, sum.degree() <= max(p.a.degree(), p.b.degree()));
            commutative(Polynomial::add, p);
            inverse(q -> q.add(p.b), (Polynomial q) -> q.subtract(p.b), p.a);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Polynomial::of, Polynomial::of, Polynomial::of, BigInteger::add, Polynomial::add, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ZERO.add(p), p);
            assertEquals(p, p.add(ZERO), p);
            assertTrue(p, p.add(p.negate()) == ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            associative(Polynomial::add, t);
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial negative = p.negate();
            negative.validate();
            assertEquals(p, negative.degree(), p.degree());
            involution(Polynomial::negate, p);
            assertTrue(p, p.add(negative) == ZERO);
        }

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            assertEquals(p, p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Polynomial::of, BigInteger::negate, Polynomial::negate, i);
        }

        for (Polynomial p : take(LIMIT, filterInfinite(q -> q != ZERO, P.polynomials()))) {
            Polynomial negative = p.negate();
            assertNotEquals(p, p, negative);
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial abs = p.abs();
            abs.validate();
            assertEquals(p, abs.degree(), p.degree());
            idempotent(Polynomial::abs, p);
            assertNotEquals(p, abs.signum(), -1);
            assertTrue(p, ge(abs, ZERO));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Polynomial::of, BigInteger::abs, Polynomial::abs, i);
        }
    }

    private void propertiesSignum() {
        initialize("signum()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            int signum = p.signum();
            assertEquals(p, signum, compare(p, ZERO).toInt());
            assertTrue(p, signum == -1 || signum == 0 || signum == 1);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Function.identity(), BigInteger::signum, Polynomial::signum, i);
        }
    }

    private static int signum_BigInteger_alt(@NotNull Polynomial p, @NotNull BigInteger x) {
        return sumSignBigInteger(
                toList(
                        zipWith(
                                (c, i) -> c.equals(BigInteger.ZERO) ? BigInteger.ZERO : x.pow(i).multiply(c),
                                p,
                                rangeUp(0)
                        )
                )
        );
    }

    private static int signum_BigInteger_alt2(@NotNull Polynomial p, @NotNull BigInteger x) {
        if (p == ZERO) return 0;
        BigInteger partialResult = foldr((c, y) -> y.multiply(x).add(c), BigInteger.ZERO, tail(p)).multiply(x);
        return Integer.signum(partialResult.compareTo(head(p).negate()));
    }

    private void propertiesSignum_BigInteger() {
        initialize("signum(BigInteger)");
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            int signum = p.a.signum(p.b);
            assertEquals(p, signum, signum_BigInteger_alt(p.a, p.b));
            assertEquals(p, signum, signum_BigInteger_alt2(p.a, p.b));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ZERO.signum(i), 0);
            assertEquals(i, X.signum(i), i.signum());
            assertEquals(i, of(IntegerUtils.NEGATIVE_ONE, 1).signum(i), -i.signum());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.signum(BigInteger.ZERO), p.coefficient(0).signum());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.signum(BigInteger.ONE), sumSignBigInteger(toList(p)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            assertEquals(p, of(p.a).signum(p.b), p.a.signum());
        }
    }

    private void compareImplementationsSignum_BigInteger() {
        Map<String, Function<Pair<Polynomial, BigInteger>, Integer>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> signum_BigInteger_alt(p.a, p.b));
        functions.put("alt2", p -> signum_BigInteger_alt2(p.a, p.b));
        functions.put("standard", p -> p.a.signum(p.b));
        Iterable<Pair<Polynomial, BigInteger>> ps = P.pairs(P.polynomials(), P.bigIntegers());
        compareImplementations("signum(BigInteger)", take(LIMIT, ps), functions);
    }

    private static int signum_Rational_simplest(@NotNull Polynomial p, @NotNull Rational x) {
        return p.apply(x).signum();
    }

    private static int signum_Rational_alt(@NotNull Polynomial p, @NotNull Rational x) {
        return Rational.sumSign(
                toList(
                        zipWith(
                                (c, i) -> c.equals(BigInteger.ZERO) ? Rational.ZERO : x.pow(i).multiply(c),
                                p,
                                rangeUp(0)
                        )
                )
        );
    }

    private static int signum_Rational_alt2(@NotNull Polynomial p, @NotNull Rational x) {
        if (p == ZERO) return 0;
        Rational partialResult = foldr(
                (c, y) -> y.multiply(x).add(Rational.of(c)),
                Rational.ZERO,
                tail(p)
        ).multiply(x);
        return Integer.signum(partialResult.compareTo(Rational.of(head(p).negate())));
    }

    private static int signum_Rational_alt3(@NotNull Polynomial p, @NotNull Rational x) {
        if (p == ZERO) return 0;
        int degree = p.degree();
        if (degree == 0) return p.coefficient(0).signum();
        BigInteger numerator = x.getNumerator();
        BigInteger denominator = x.getDenominator();
        BigInteger result = last(p);
        BigInteger multiplier = BigInteger.ONE;
        for (int i = p.degree() - 1; i > 0; i--) {
            multiplier = multiplier.multiply(denominator);
            result = result.multiply(numerator).add(p.coefficient(i).multiply(multiplier));
        }
        multiplier = multiplier.multiply(denominator);
        result = result.multiply(numerator);
        return result.compareTo(p.coefficient(0).multiply(multiplier).negate());
    }

    private void propertiesSignum_Rational() {
        initialize("signum(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            int signum = p.a.signum(p.b);
            assertEquals(p, signum, signum_Rational_simplest(p.a, p.b));
            assertEquals(p, signum, signum_Rational_alt(p.a, p.b));
            assertEquals(p, signum, signum_Rational_alt2(p.a, p.b));
            assertEquals(p, signum, signum_Rational_alt3(p.a, p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.signum(r), 0);
            assertEquals(r, X.signum(r), r.signum());
            assertEquals(r, of(IntegerUtils.NEGATIVE_ONE, 1).signum(r), -r.signum());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.signum(Rational.ZERO), p.coefficient(0).signum());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.signum(Rational.ONE), sumSignBigInteger(toList(p)));
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            assertEquals(p, of(p.a).signum(p.b), p.a.signum());
        }
    }

    private void compareImplementationsSignum_Rational() {
        Map<String, Function<Pair<Polynomial, Rational>, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> signum_Rational_simplest(p.a, p.b));
        functions.put("alt", p -> signum_Rational_alt(p.a, p.b));
        functions.put("alt2", p -> signum_Rational_alt2(p.a, p.b));
        functions.put("alt3", p -> signum_Rational_alt3(p.a, p.b));
        functions.put("standard", p -> p.a.signum(p.b));
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.polynomials(), P.rationals());
        compareImplementations("signum(Rational)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial subtract_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial difference = p.a.subtract(p.b);
            difference.validate();
            assertTrue(p, difference.degree() <= max(p.a.degree(), p.b.degree()));
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(Polynomial::subtract, Polynomial::negate, p);
            inverse(q -> q.subtract(p.b), (Polynomial q) -> q.add(p.b), p.a);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.subtract(t.b).apply(t.c), t.a.apply(t.c).subtract(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Polynomial::of, Polynomial::of, Polynomial::of, BigInteger::subtract, Polynomial::subtract, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, ZERO.subtract(p), p.negate());
            fixedPoint(q -> q.subtract(ZERO), p);
            assertTrue(p, p.subtract(p) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        compareImplementations("subtract(Polynomial)", take(LIMIT, P.pairs(P.polynomials())), functions);
    }

    private static @NotNull Polynomial multiply_Polynomial_alt(@NotNull Polynomial a, @NotNull Polynomial b) {
        if (a == ZERO || b == ZERO) return ZERO;
        if (a == ONE) return b;
        if (b == ONE) return a;
        int p = a.degree();
        int q = b.degree();
        if (p < q) {
            return multiply_Polynomial_alt(b, a);
        }
        int r = p + q;
        List<BigInteger> productCoefficients = new ArrayList<>();
        int k = 0;
        for (; k <= q; k++) {
            BigInteger coefficient = BigInteger.ZERO;
            for (int i = 0; i <= k; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k < p; k++) {
            BigInteger coefficient = BigInteger.ZERO;
            for (int i = 0; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        for (; k <= r; k++) {
            BigInteger coefficient = BigInteger.ZERO;
            for (int i = k - p; i <= q; i++) {
                coefficient = coefficient.add(a.coefficient(k - i).multiply(b.coefficient(i)));
            }
            productCoefficients.add(coefficient);
        }
        return of(productCoefficients);
    }

    private void propertiesMultiply_Polynomial() {
        initialize("multiply(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_Polynomial_alt(p.a, p.b));
            assertTrue(p, p.a == ZERO || p.b == ZERO || product.degree() == p.a.degree() + p.b.degree());
            commutative(Polynomial::multiply, p);
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials(), P.polynomialsAtLeast(0)))) {
            inverse(q -> q.multiply(p.b), (Polynomial q) -> q.divideExact(p.b), p.a);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(Polynomial::of, Polynomial::of, Polynomial::of, BigInteger::multiply, Polynomial::multiply, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(ONE::multiply, p);
            fixedPoint(q -> q.multiply(ONE), p);
            fixedPoint(q -> q.multiply(p), ZERO);
            fixedPoint(p::multiply, ZERO);
        }

        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, P.triples(P.polynomials()))) {
            associative(Polynomial::multiply, t);
            leftDistributive(Polynomial::add, Polynomial::multiply, t);
            rightDistributive(Polynomial::add, Polynomial::multiply, t);
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.positivePrimitivePolynomials()))) {
            Polynomial product = p.a.multiply(p.b);
            assertTrue(p, product.signum() == 1 && product.isPrimitive());
        }
    }

    private void compareImplementationsMultiply_Polynomial() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> multiply_Polynomial_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        compareImplementations("multiply(Polynomial)", take(LIMIT, P.pairs(P.polynomials())), functions);
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b.equals(BigInteger.ZERO) || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        Iterable<Triple<Polynomial, BigInteger, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.bigIntegers(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, BigInteger, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(t.b));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(
                    Polynomial::of,
                    Function.identity(),
                    Polynomial::of,
                    BigInteger::multiply,
                    Polynomial::multiply,
                    p
            );
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.multiply(BigInteger.ONE), p);
            assertTrue(p, p.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts2 = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.integers()))) {
            Polynomial product = p.a.multiply(p.b);
            product.validate();
            assertTrue(p, p.b == 0 || product.degree() == p.a.degree());
            assertEquals(p, product, p.a.multiply(of(BigInteger.valueOf(p.b))));
            assertEquals(p, product, of(BigInteger.valueOf(p.b)).multiply(p.a));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.integers(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).apply(t.c), t.a.apply(t.c).multiply(BigInteger.valueOf(t.b)));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.integers()))) {
            homomorphic(
                    Polynomial::of,
                    Function.identity(),
                    Polynomial::of,
                    (i, j) -> i.multiply(BigInteger.valueOf(j)),
                    Polynomial::multiply,
                    p
            );
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(BigInteger.valueOf(i)));
            fixedPoint(j -> j.multiply(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.multiply(1), p);
            assertTrue(p, p.multiply(0) == ZERO);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> ts2 = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.integers()
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.add(t.b).multiply(t.c);
            Polynomial expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t, expression1, expression2);
        }
    }

    private static @NotNull Polynomial shiftLeft_simplest(@NotNull Polynomial p, int bits) {
        return p.multiply(BigInteger.ONE.shiftLeft(bits));
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            Polynomial shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted.degree(), p.a.degree());
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            aeqit(p.toString(), map(BigInteger::signum, p.a), map(BigInteger::signum, shifted));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.negate().shiftLeft(p.b), shifted.negate());
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.shiftLeft(0), p);
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.naturalIntegersGeometric(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.shiftLeft(t.b).apply(t.c), t.a.apply(t.c).shiftLeft(t.b));
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegersGeometric()))) {
            homomorphic(
                    Polynomial::of,
                    Function.identity(),
                    Polynomial::of,
                    BigInteger::shiftLeft,
                    Polynomial::shiftLeft,
                    p
            );
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.shiftLeft(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric())),
                functions
        );
    }

    private static @NotNull Polynomial sum_simplest(@NotNull Iterable<Polynomial> xs) {
        return foldl(Polynomial::add, ZERO, xs);
    }

    private void propertiesSum() {
        initialize("sum(Iterable<Polynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::add,
                Polynomial::sum,
                Polynomial::validate,
                true,
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial sum = sum(ps);
            sum.validate();
            assertEquals(ps, sum, sum_simplest(ps));
            assertTrue(ps, ps.isEmpty() || sum.degree() <= maximum(map(Polynomial::degree, ps)));
        }

        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, P.pairs(P.lists(P.polynomials()), P.bigIntegers()))) {
            assertEquals(p, sum(p.a).apply(p.b), sumBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            homomorphic(
                    js -> toList(map(Polynomial::of, js)),
                    Polynomial::of,
                    IterableUtils::sumBigInteger,
                    Polynomial::sum,
                    is
            );
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::sum_simplest);
        functions.put("standard", Polynomial::sum);
        compareImplementations("sum(Iterable<Polynomial>)", take(LIMIT, P.lists(P.polynomials())), functions);
    }

    private static @NotNull Polynomial product_simplest(@NotNull Iterable<Polynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(Polynomial::multiply, ONE, xs);
    }

    private static @NotNull Polynomial product_alt(@NotNull List<Polynomial> xs) {
        if (any(x -> x == ZERO, xs)) return ZERO;
        List<BigInteger> productCoefficients =
                toList(replicate(sumInteger(map(Polynomial::degree, xs)) + 1, BigInteger.ZERO));
        List<List<Pair<BigInteger, Integer>>> selections = toList(map(p -> toList(zip(p, rangeUp(0))), xs));
        outer:
        for (List<Pair<BigInteger, Integer>> selection : EP.cartesianProduct(selections)) {
            BigInteger coefficient = BigInteger.ONE;
            int exponent = 0;
            for (Pair<BigInteger, Integer> p : selection) {
                if (p.a.equals(BigInteger.ZERO)) continue outer;
                coefficient = coefficient.multiply(p.a);
                exponent += p.b;
            }
            productCoefficients.set(exponent, productCoefficients.get(exponent).add(coefficient));
        }
        return of(productCoefficients);
    }

    private void propertiesProduct() {
        initialize("product(Iterable<Polynomial>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                Polynomial::multiply,
                Polynomial::product,
                Polynomial::validate,
                true,
                true
        );

        for (List<Polynomial> ps : take(LIMIT, P.lists(P.polynomials()))) {
            Polynomial product = product(ps);
            assertTrue(
                    ps,
                    any(p -> p == ZERO, ps) ||
                            product.degree() == IterableUtils.sumInteger(map(Polynomial::degree, ps))
            );
        }

        for (List<Polynomial> ps : take(LIMIT, P.withScale(1).lists(P.withSecondaryScale(1).polynomials()))) {
            Polynomial product = product(ps);
            assertEquals(ps, product, product_simplest(ps));
            assertEquals(ps, product, product_alt(ps));
        }

        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, P.pairs(P.lists(P.polynomials()), P.bigIntegers()))) {
            assertEquals(p, product(p.a).apply(p.b), productBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.lists(P.bigIntegers()))) {
            homomorphic(
                    js -> toList(map(Polynomial::of, js)),
                    Polynomial::of,
                    IterableUtils::productBigInteger,
                    Polynomial::product,
                    is
            );
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::product_simplest);
        functions.put("alt", PolynomialProperties::product_alt);
        functions.put("standard", Polynomial::product);
        compareImplementations(
                "product(Iterable<Polynomial>)",
                take(LIMIT, P.withScale(1).lists(P.withSecondaryScale(1).polynomials())),
                functions
        );
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Polynomial>)");
        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.polynomials(),
                P.polynomials(),
                Polynomial::negate,
                Polynomial::subtract,
                Polynomial::delta,
                Polynomial::validate
        );

        Iterable<Pair<List<Polynomial>, BigInteger>> ps = P.pairs(P.listsAtLeast(1, P.polynomials()), P.bigIntegers());
        for (Pair<List<Polynomial>, BigInteger> p : take(LIMIT, ps)) {
            aeqit(p.toString(), map(q -> q.apply(p.b), delta(p.a)), deltaBigInteger(map(q -> q.apply(p.b), p.a)));
        }

        for (List<BigInteger> is : take(LIMIT, P.listsAtLeast(1, P.bigIntegers()))) {
            homomorphic(
                    js -> toList(map(Polynomial::of, js)),
                    js -> toList(map(Polynomial::of, js)),
                    js -> toList(deltaBigInteger(js)),
                    qs -> toList(delta(qs)),
                    is
            );
        }
    }

    private static @NotNull Polynomial pow_simplest(@NotNull Polynomial a, int p) {
        return product(replicate(p, a));
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Polynomial q = p.a.pow(p.b);
            q.validate();
            assertTrue(p, p.a == ZERO || q.degree() == p.a.degree() * p.b);
            assertEquals(p, q, pow_simplest(p.a, p.b));
        }

        Iterable<Triple<Polynomial, Integer, BigInteger>> ts = P.triples(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Integer, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.pow(t.b).apply(t.c), t.a.apply(t.c).pow(t.b));
        }

        Iterable<Pair<BigInteger, Integer>> ps2 = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps2)) {
            homomorphic(Polynomial::of, Function.identity(), Polynomial::of, BigInteger::pow, Polynomial::pow, p);
        }

        for (int i : take(LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            fixedPoint(p -> p.pow(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            assertTrue(p, p.pow(0) == ONE);
            fixedPoint(q -> q.pow(1), p);
            assertEquals(p, p.pow(2), p.multiply(p));
        }

        Iterable<Triple<Polynomial, Integer, Integer>> ts2 = P.triples(
                P.withScale(4).withSecondaryScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Polynomial, Integer, Integer> t : take(LIMIT, ts2)) {
            Polynomial expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Polynomial expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            Polynomial expression5 = t.a.pow(t.b).pow(t.c);
            Polynomial expression6 = t.a.pow(t.b * t.c);
            assertEquals(t, expression5, expression6);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> ts3 = P.triples(
                P.withScale(4).polynomials(),
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts3)) {
            Polynomial expression1 = t.a.multiply(t.b).pow(t.c);
            Polynomial expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.pow(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("pow(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial substitute_naive(@NotNull Polynomial a, @NotNull Polynomial b) {
        return sum(zipWith((c, i) -> c.equals(BigInteger.ZERO) ? ZERO : b.pow(i).multiply(c), a, rangeUp(0)));
    }

    private void propertiesSubstitute() {
        initialize("substitute(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial substituted = p.a.substitute(p.b);
            substituted.validate();
            assertTrue(p, p.b == ZERO || substituted == ZERO || substituted.degree() == p.a.degree() * p.b.degree());
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomials(),
                P.bigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.substitute(t.b).apply(t.c), t.a.apply(t.b.apply(t.c)));
        }

        for (Pair<BigInteger, Polynomial> p : take(LIMIT, P.pairs(P.bigIntegers(), P.polynomials()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q.substitute(p.b), q);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertEquals(p, p.substitute(ZERO), p == ZERO ? ZERO : of(p.coefficient(0)));
            assertEquals(p, p.substitute(ONE), of(sumBigInteger(p)));
            fixedPoint(q -> q.substitute(X), p);
        }
    }

    private void compareImplementationsSubstitute() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("naïve", p -> substitute_naive(p.a, p.b));
        functions.put("standard", p -> p.a.substitute(p.b));
        compareImplementations(
                "substitute(Polynomial)",
                take(LIMIT, P.pairs(P.withScale(4).polynomials())),
                functions
        );
    }

    private void propertiesDifferentiate() {
        initialize("differentiate()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial derivative = p.differentiate();
            derivative.validate();
            assertEquals(p, last(take(p.degree() + 2, iterate(Polynomial::differentiate, p))), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials(0))) {
            assertEquals(p, p.differentiate(), ZERO);
        }
    }

    private void propertiesIsMonic() {
        initialize("isMonic()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            boolean isMonic = p.isMonic();
            Optional<BigInteger> leading = p.leading();
            assertEquals(p, isMonic, leading.isPresent() && leading.get().equals(BigInteger.ONE));
        }
    }

    private void propertiesIsPrimitive() {
        initialize("isPrimitive()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            boolean isPrimitive = p.isPrimitive();
            assertEquals(p, isPrimitive, p.negate().isPrimitive());
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.primitivePolynomials()))) {
            assertTrue(p, p.a.multiply(p.b).isPrimitive());
        }
    }

    private void propertiesContentAndPrimitive() {
        initialize("contentAndPrimitive()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Pair<BigInteger, Polynomial> contentAndPrimitive = p.contentAndPrimitive();
            BigInteger content = contentAndPrimitive.a;
            assertNotNull(p, content);
            Polynomial primitive = contentAndPrimitive.b;
            assertNotNull(p, primitive);
            primitive.validate();
            assertEquals(p, primitive.degree(), p.degree());
            assertTrue(p, primitive.isPrimitive());
            assertEquals(p, primitive.multiply(content), p);
            assertEquals(p, content.signum(), 1);
            idempotent(q -> q.contentAndPrimitive().b, p);
            assertEquals(p, content, p.constantFactor().a.abs());
        }
    }

    private void propertiesConstantFactor() {
        initialize("constantFactor()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Pair<BigInteger, Polynomial> contentAndPrimitive = p.constantFactor();
            BigInteger constantFactor = contentAndPrimitive.a;
            assertNotNull(p, constantFactor);
            Polynomial absolutePrimitive = contentAndPrimitive.b;
            assertNotNull(p, absolutePrimitive);
            absolutePrimitive.validate();
            assertEquals(p, absolutePrimitive.degree(), p.degree());
            assertNotEquals(p, constantFactor, BigInteger.ZERO);
            assertTrue(p, absolutePrimitive.isPrimitive());
            assertEquals(p, absolutePrimitive.multiply(constantFactor), p);
            assertEquals(p, absolutePrimitive.signum(), 1);
            idempotent(q -> q.constantFactor().b, p);
            assertEquals(p, absolutePrimitive, p.contentAndPrimitive().b.abs());
        }
    }

    private static @NotNull Pair<Polynomial, Polynomial> pseudoDivide_simplest(
            @NotNull Polynomial a,
            @NotNull Polynomial b
    ) {
        if (b == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = a.degree();
        int n = b.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than the degree of that. this: " +
                    a + ", that: " + b);
        }
        BigInteger factor = b.leading().get().pow(m - n + 1);
        Pair<RationalPolynomial, RationalPolynomial> quotRem =
                a.multiply(factor).toRationalPolynomial().divide(b.toRationalPolynomial());
        return new Pair<>(quotRem.a.toPolynomial(), quotRem.b.toPolynomial());
    }

    private static @NotNull Pair<Polynomial, Polynomial> pseudoDivide_alt(
            @NotNull Polynomial a,
            @NotNull Polynomial b
    ) {
        if (b == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = a.degree();
        int n = b.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than the degree of that. this: " +
                    a + ", that: " + b);
        }
        List<BigInteger> q = new ArrayList<>();
        List<BigInteger> r = toList(a);
        BigInteger bLeading = b.leading().get();
        if (m < n) return new Pair<>(ZERO, a);
        for (int k = m - n; k >= 0; k--) {
            q.add(r.get(n + k).multiply(bLeading.pow(k)));
            for (int j = n + k - 1; j >= k; j--) {
                r.set(j, bLeading.multiply(r.get(j)).subtract(r.get(n + k).multiply(b.coefficient(j - k))));
            }
            for (int j = k - 1; j >= 0; j--) {
                r.set(j, bLeading.multiply(r.get(j)));
            }
        }
        return new Pair<>(of(reverse(q)), of(toList(take(n, r))));
    }

    private void propertiesPseudoDivide() {
        initialize("pseudoDivide(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Pair<Polynomial, Polynomial> pseudoQuotRem = p.a.pseudoDivide(p.b);
            assertEquals(p, pseudoQuotRem, pseudoDivide_simplest(p.a, p.b));
            assertEquals(p, pseudoQuotRem, pseudoDivide_alt(p.a, p.b));
            Polynomial quotient = pseudoQuotRem.a;
            assertNotNull(p, quotient);
            Polynomial remainder = pseudoQuotRem.b;
            assertNotNull(p, remainder);
            quotient.validate();
            remainder.validate();
            BigInteger factor = p.b.leading().get().pow(p.a.degree() - p.b.degree() + 1);
            assertEquals(p, quotient.multiply(p.b).add(remainder), p.a.multiply(factor));
            assertTrue(p, remainder.degree() < p.b.degree());

            if (pseudoQuotRem.b != ZERO) {
                Pair<RationalPolynomial, RationalPolynomial> quotRem =
                        p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                assertEquals(p, pseudoQuotRem.a.constantFactor().b, quotRem.a.constantFactor().b);
                assertEquals(p, pseudoQuotRem.b.constantFactor().b, quotRem.b.constantFactor().b);
            }
        }

        ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.positivePrimitivePolynomials(), P.positivePrimitivePolynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Pair<Polynomial, Polynomial> pseudoQuotRem = p.a.pseudoDivide(p.b);
            assertEquals(p, pseudoQuotRem.a.signum(), 1);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroBigIntegers()))) {
            Pair<Polynomial, Polynomial> pseudoQuotRem = of(p.a).pseudoDivide(of(p.b));
            assertEquals(p, pseudoQuotRem.a, of(p.a));
            assertEquals(p, pseudoQuotRem.b, ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.pseudoDivide(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.pseudoDivide(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPseudoDivide() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Pair<Polynomial, Polynomial>>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> pseudoDivide_simplest(p.a, p.b));
        functions.put("alt", p -> pseudoDivide_alt(p.a, p.b));
        functions.put("standard", p -> p.a.pseudoDivide(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("pseudoDivide(Polynomial)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial pseudoRemainder_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.pseudoDivide(b).b;
    }

    private void propertiesPseudoRemainder() {
        initialize("pseudoRemainder(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial pseudoRemainder = p.a.pseudoRemainder(p.b);
            pseudoRemainder.validate();
            assertEquals(p, pseudoRemainder, pseudoRemainder_simplest(p.a, p.b));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroBigIntegers()))) {
            assertEquals(p, of(p.a).pseudoRemainder(of(p.b)), ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.pseudoRemainder(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.pseudoRemainder(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPseudoRemainder() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pseudoRemainder_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pseudoRemainder(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("pseudoRemainder(Polynomial)", take(LIMIT, ps), functions);
    }

    private static @NotNull Pair<Polynomial, Polynomial> absolutePseudoDivide_simplest(
            @NotNull Polynomial a,
            @NotNull Polynomial b
    ) {
        if (b == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int m = a.degree();
        int n = b.degree();
        if (m < n) {
            throw new ArithmeticException("The degree of this must be greater than the degree of that. this: " +
                    a + ", that: " + b);
        }
        BigInteger factor = b.leading().get().abs().pow(m - n + 1);
        Pair<RationalPolynomial, RationalPolynomial> quotRem =
                a.multiply(factor).toRationalPolynomial().divide(b.toRationalPolynomial());
        return new Pair<>(quotRem.a.toPolynomial(), quotRem.b.toPolynomial());
    }

    private void propertiesAbsolutePseudoDivide() {
        initialize("absolutePseudoDivide(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Pair<Polynomial, Polynomial> absolutePseudoQuotRem = p.a.absolutePseudoDivide(p.b);
            assertEquals(p, absolutePseudoQuotRem, absolutePseudoDivide_simplest(p.a, p.b));
            Polynomial quotient = absolutePseudoQuotRem.a;
            assertNotNull(p, quotient);
            Polynomial remainder = absolutePseudoQuotRem.b;
            assertNotNull(p, remainder);
            quotient.validate();
            remainder.validate();
            BigInteger factor = p.b.leading().get().pow(p.a.degree() - p.b.degree() + 1).abs();
            assertEquals(p, quotient.multiply(p.b).add(remainder), p.a.multiply(factor));
            assertTrue(p, remainder.degree() < p.b.degree());

            if (absolutePseudoQuotRem.b != ZERO) {
                Pair<RationalPolynomial, RationalPolynomial> quotRem =
                        p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                assertEquals(p, absolutePseudoQuotRem.a.constantFactor().b, quotRem.a.constantFactor().b);
                assertEquals(p, absolutePseudoQuotRem.b.constantFactor().b, quotRem.b.constantFactor().b);
                assertEquals(p, absolutePseudoQuotRem.a.signum(), quotRem.a.signum());
                assertEquals(p, absolutePseudoQuotRem.b.signum(), quotRem.b.signum());
            }
        }

        ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.positivePrimitivePolynomials(), P.positivePrimitivePolynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Pair<Polynomial, Polynomial> absolutePseudoQuotRem = p.a.absolutePseudoDivide(p.b);
            assertEquals(p, absolutePseudoQuotRem.a.signum(), 1);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroBigIntegers()))) {
            Pair<Polynomial, Polynomial> absolutePseudoQuotRem = of(p.a).absolutePseudoDivide(of(p.b));
            assertEquals(p, absolutePseudoQuotRem.a.abs(), of(p.a.abs()));
            assertEquals(p, absolutePseudoQuotRem.b, ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.absolutePseudoDivide(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.absolutePseudoDivide(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsAbsolutePseudoDivide() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Pair<Polynomial, Polynomial>>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> absolutePseudoDivide_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pseudoDivide(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("absolutePseudoDivide(Polynomial)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial absolutePseudoRemainder_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.absolutePseudoDivide(b).b;
    }

    private void propertiesAbsolutePseudoRemainder() {
        initialize("absolutePseudoRemainder(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial absolutePseudoRemainder = p.a.absolutePseudoRemainder(p.b);
            absolutePseudoRemainder.validate();
            assertEquals(p, absolutePseudoRemainder, absolutePseudoRemainder_simplest(p.a, p.b));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroBigIntegers()))) {
            assertEquals(p, of(p.a).absolutePseudoRemainder(of(p.b)), ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.absolutePseudoRemainder(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.absolutePseudoRemainder(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsAbsolutePseudoRemainder() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> absolutePseudoRemainder_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.absolutePseudoRemainder(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("absolutePseudoRemainder(Polynomial)", take(LIMIT, ps), functions);
    }

    private static boolean isDivisibleBy_alt(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.toRationalPolynomial().divide(b.toRationalPolynomial()).b == RationalPolynomial.ZERO;
    }

    private void propertiesIsDivisibleBy() {
        initialize("isDivisibleBy(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials(), P.polynomialsAtLeast(0)))) {
            boolean isDivisible = p.a.isDivisibleBy(p.b);
            assertEquals(p, isDivisible, isDivisibleBy_alt(p.a, p.b));
            RationalPolynomial ar = p.a.toRationalPolynomial();
            RationalPolynomial br = p.b.toRationalPolynomial();
            assertEquals(p, isDivisible, ar.equals(p.a.toRationalPolynomial().divide(br).a.multiply(br)));
            assertTrue(p, p.a.multiply(p.b).isDivisibleBy(p.b));
        }

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials(), P.polynomials(0)))) {
            assertTrue(p, p.a.isDivisibleBy(p.b));
        }

        Iterable<Triple<Polynomial, Polynomial, BigInteger>> ts = P.triples(
                P.polynomials(),
                P.polynomialsAtLeast(0),
                P.nonzeroBigIntegers()
        );
        for (Triple<Polynomial, Polynomial, BigInteger> t : take(LIMIT, ts)) {
            boolean isDivisible = t.a.isDivisibleBy(t.b);
            assertEquals(t, isDivisible, t.a.multiply(t.c).isDivisibleBy(t.b));
            assertEquals(t, isDivisible, t.a.isDivisibleBy(t.b.multiply(t.c)));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.isDivisibleBy(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsIsDivisibleBy() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Boolean>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> isDivisibleBy_alt(p.a, p.b));
        functions.put("standard", p -> p.a.isDivisibleBy(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(P.polynomials(), P.polynomialsAtLeast(0));
        compareImplementations("isDivisibleBy(Polynomial)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial divideExact_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        Pair<RationalPolynomial, RationalPolynomial> quotRem =
                a.toRationalPolynomial().divide(b.toRationalPolynomial());
        if (quotRem.b != RationalPolynomial.ZERO || !quotRem.a.hasIntegralCoefficients()) {
            throw new ArithmeticException();
        }
        return quotRem.a.toPolynomial();
    }

    private void propertiesDivideExact() {
        initialize("divideExact(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.a),
                P.pairs(P.polynomialsAtLeast(1), P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial quotient = p.a.divideExact(p.b);
            quotient.validate();
            assertEquals(p, divideExact_simplest(p.a, p.b), quotient);
            inverse(q -> q.divideExact(p.b), (Polynomial q) -> q.multiply(p.b), p.a);
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.divideExact(p), ONE);
            fixedPoint(q -> q.divideExact(ONE), p);
            fixedPoint(q -> q.divideExact(p), ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                p -> {
                    Pair<RationalPolynomial, RationalPolynomial> quotRem =
                            p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                    return quotRem.b != RationalPolynomial.ZERO || !quotRem.a.hasIntegralCoefficients();
                },
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.divideExact(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.divideExact(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivideExact() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divideExact_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.divideExact(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.a),
                P.pairs(P.polynomialsAtLeast(1), P.polynomials())
        );
        compareImplementations("divideExact(Polynomial)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial remainderExact_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        Pair<RationalPolynomial, RationalPolynomial> quotRem =
                a.toRationalPolynomial().divide(b.toRationalPolynomial());
        if (!quotRem.a.hasIntegralCoefficients() || !quotRem.b.hasIntegralCoefficients()) {
            throw new ArithmeticException();
        }
        return quotRem.b.toPolynomial();
    }

    private void propertiesRemainderExact() {
        initialize("remainderExact(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> {
                    Pair<RationalPolynomial, RationalPolynomial> qr =
                            p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                    return qr.a.hasIntegralCoefficients() && qr.b.hasIntegralCoefficients();
                },
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial remainder = p.a.remainderExact(p.b);
            remainder.validate();
            assertEquals(p, remainderExact_simplest(p.a, p.b), remainder);
        }

        ps = map(p -> new Pair<>(p.a.multiply(p.b), p.a), P.pairs(P.polynomialsAtLeast(1), P.polynomials()));
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.remainderExact(p.b), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            fixedPoint(q -> q.remainderExact(p), ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                p -> {
                    Pair<RationalPolynomial, RationalPolynomial> qr =
                            p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                    return !qr.a.hasIntegralCoefficients() || !qr.b.hasIntegralCoefficients();
                },
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.remainderExact(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsRemainderExact() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> remainderExact_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.remainderExact(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> {
                    Pair<RationalPolynomial, RationalPolynomial> qr =
                            p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                    return qr.a.hasIntegralCoefficients() && qr.b.hasIntegralCoefficients();
                },
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("remainderExact(Polynomial)", take(LIMIT, ps), functions);
    }

    private void propertiesTrivialPseudoRemainderSequence() {
        initialize("trivialPseudoRemainderSequence(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(2).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            List<Polynomial> sequence = p.a.trivialPseudoRemainderSequence(p.b);
            sequence.forEach(Polynomial::validate);
            assertFalse(p, sequence.isEmpty());
            assertNotEquals(p, last(sequence), ZERO);
            assertEquals(p, last(sequence).constantFactor().b, p.a.gcd(p.b));
            assertEquals(
                    p,
                    toList(map(q -> q == ZERO ? ZERO : q.contentAndPrimitive().b, sequence)),
                    p.a.primitivePseudoRemainderSequence(p.b)
            );
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.trivialPseudoRemainderSequence(ZERO), Collections.singletonList(p));
        }
    }

    private void propertiesPrimitivePseudoRemainderSequence() {
        initialize("primitivePseudoRemainderSequence(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            List<Polynomial> sequence = p.a.primitivePseudoRemainderSequence(p.b);
            sequence.forEach(Polynomial::validate);
            assertFalse(p, sequence.isEmpty());
            assertNotEquals(p, last(sequence), ZERO);
            assertTrue(p, all(q -> q == ZERO || q.isPrimitive(), sequence));
            assertEquals(p, last(sequence).constantFactor().b, p.a.gcd(p.b));
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(
                    p,
                    p.primitivePseudoRemainderSequence(ZERO),
                    Collections.singletonList(p.contentAndPrimitive().b)
            );
        }
    }

    private void propertiesSubresultantSequence() {
        initialize("subresultantSequence(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            List<Polynomial> sequence = p.a.subresultantSequence(p.b);
            sequence.forEach(Polynomial::validate);
            assertFalse(p, sequence.isEmpty());
            assertNotEquals(p, last(sequence), ZERO);
            assertEquals(p, last(sequence).constantFactor().b, p.a.gcd(p.b));
            assertEquals(
                    p,
                    toList(map(q -> q == ZERO ? ZERO : q.constantFactor().b, sequence)),
                    toList(map(Polynomial::abs, p.a.primitivePseudoRemainderSequence(p.b)))
            );
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.subresultantSequence(ZERO), Collections.singletonList(p));
        }
    }

    private static @NotNull Polynomial gcd_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        if (a == ZERO && b == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        if (a == ZERO) return b.constantFactor().b;
        if (b == ZERO) return a.constantFactor().b;
        if (a.degree() == 0 || b.degree() == 0) return ONE;
        return product(intersect(a.constantFactor().b.factor(), b.constantFactor().b.factor()));
    }

    private static @NotNull Polynomial gcd_alt(@NotNull Polynomial x, @NotNull Polynomial y) {
        if (x == ZERO && y == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        if (x == ZERO) return y.constantFactor().b;
        if (y == ZERO) return x.constantFactor().b;
        if (x == ONE || y == ONE) return ONE;
        RationalPolynomial a;
        RationalPolynomial b;
        if (x.degree() >= y.degree()) {
            a = x.toRationalPolynomial();
            b = y.toRationalPolynomial();
        } else {
            a = y.toRationalPolynomial();
            b = x.toRationalPolynomial();
        }
        while (true) {
            RationalPolynomial remainder = a.divide(b).b;
            if (remainder == RationalPolynomial.ZERO) {
                return b.constantFactor().b;
            } else {
                a = b;
                b = remainder;
            }
        }
    }

    private static @NotNull Polynomial gcd_alt2(@NotNull Polynomial x, @NotNull Polynomial y) {
        if (x == ZERO && y == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        if (x == ZERO) return y.constantFactor().b;
        if (y == ZERO) return x.constantFactor().b;
        if (x == ONE || y == ONE) return ONE;
        Polynomial a;
        Polynomial b;
        if (x.degree() >= y.degree()) {
            a = x;
            b = y;
        } else {
            a = y;
            b = x;
        }
        while (true) {
            Polynomial remainder = a.pseudoRemainder(b);
            if (remainder == ZERO) {
                return b.constantFactor().b;
            } else {
                a = b;
                b = remainder;
            }
        }
    }

    private static @NotNull Polynomial gcd_alt3(@NotNull Polynomial x, @NotNull Polynomial y) {
        if (x == ZERO && y == ZERO) {
            throw new ArithmeticException("this and that cannot both be zero.");
        }
        if (x == ZERO) return y.constantFactor().b;
        if (y == ZERO) return x.constantFactor().b;
        if (x == ONE || y == ONE) return ONE;
        Polynomial a;
        Polynomial b;
        if (x.degree() >= y.degree()) {
            a = x.contentAndPrimitive().b;
            b = y.contentAndPrimitive().b;
        } else {
            a = y.contentAndPrimitive().b;
            b = x.contentAndPrimitive().b;
        }
        while (true) {
            Polynomial remainder = a.pseudoRemainder(b);
            if (remainder == ZERO) {
                return b.abs();
            } else {
                a = b;
                b = remainder.contentAndPrimitive().b;
            }
        }
    }

    private void propertiesGcd_Polynomial() {
        initialize("gcd(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial gcd = p.a.gcd(p.b);
            gcd.validate();
            assertEquals(p, gcd.signum(), 1);
            assertTrue(p, gcd.isPrimitive());
            Polynomial ap = p.a == ZERO ? ZERO : p.a.constantFactor().b;
            Polynomial bp = p.b == ZERO ? ZERO : p.b.constantFactor().b;
            assertEquals(p, gcd, ap.gcd(bp));
            assertTrue(p, p.a.isDivisibleBy(gcd));
            assertTrue(p, p.b.isDivisibleBy(gcd));
            commutative(Polynomial::gcd, p);
        }

        ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).withSecondaryScale(2).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial gcd = p.a.gcd(p.b);
            assertEquals(p, gcd, gcd_simplest(p.a, p.b));
            assertEquals(p, gcd, gcd_alt(p.a, p.b));
            assertEquals(p, gcd, gcd_alt2(p.a, p.b));
            assertEquals(p, gcd, gcd_alt3(p.a, p.b));
        }

        for (Polynomial p : take(LIMIT, P.positivePrimitivePolynomials())) {
            fixedPoint(q -> q.gcd(ZERO), p);
            fixedPoint(ZERO::gcd, p);
            fixedPoint(p::gcd, ONE);
            fixedPoint(q -> q.gcd(p), ONE);
            fixedPoint(q -> q.gcd(q), p);
        }

        Iterable<Triple<Polynomial, Polynomial, Polynomial>> ts = P.subsetTriples(
                P.withScale(4).irreduciblePolynomialsAtLeast(1)
        );
        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).gcd(t.b.multiply(t.c)), t.b);
        }
    }

    private void compareImplementationsGcd_Polynomial1() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> gcd_simplest(p.a, p.b));
        functions.put("alt", p -> gcd_alt(p.a, p.b));
        functions.put("alt2", p -> gcd_alt2(p.a, p.b));
        functions.put("standard", p -> p.a.gcd(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).withSecondaryScale(2).polynomials())
        );
        compareImplementations("gcd(Polynomial)", take(LIMIT, ps), functions);
    }

    private void compareImplementationsGcd_Polynomial2() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("alt3", p -> gcd_alt3(p.a, p.b));
        functions.put("standard", p -> p.a.gcd(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.polynomials())
        );
        compareImplementations("gcd(Polynomial)", take(SMALL_LIMIT, ps), functions);
    }

    private void propertiesGcd_List_Polynomial() {
        initialize("gcd(List<Polynomial>)");
        Iterable<List<Polynomial>> pss = filterInfinite(p -> any(q -> q != ZERO, p), P.lists(P.polynomials()));
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            Polynomial gcd = gcd(ps);
            gcd.validate();
            assertEquals(ps, gcd.signum(), 1);
            assertTrue(ps, gcd.isPrimitive());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, gcd(Collections.singletonList(p)), p.constantFactor().b);
        }

        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a != ZERO || q.b != ZERO,
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            assertEquals(p, gcd(Arrays.asList(p.a, p.b)), p.a.gcd(p.b));
        }

        Iterable<List<Polynomial>> psFail = filterInfinite(
                qs -> any(q -> q != ZERO, qs),
                P.listsWithElement(null, P.polynomials())
        );
        for (List<Polynomial> qs : take(LIMIT, psFail)) {
            try {
                gcd(qs);
                fail(qs);
            } catch (NullPointerException | IllegalArgumentException ignored) {}
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            try {
                gcd(toList(replicate(i, ZERO)));
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLcm() {
        initialize("lcm(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            Polynomial lcm = p.a.lcm(p.b);
            lcm.validate();
            assertNotEquals(p, lcm.signum(), -1);
            assertTrue(p, lcm == ZERO || lcm.isPrimitive());
            assertTrue(p, p.a == ZERO || lcm.isDivisibleBy(p.a));
            assertTrue(p, p.b == ZERO || lcm.isDivisibleBy(p.b));
            commutative(Polynomial::lcm, p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(p::lcm, ZERO);
            fixedPoint(q -> q.lcm(p), ZERO);
            Polynomial ppp = p == ZERO ? ZERO : p.constantFactor().b;
            assertEquals(p, p.lcm(ONE), ppp);
            assertEquals(p, ONE.lcm(p), ppp);
        }
    }

    private static boolean isRelativelyPrimeTo_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        if (a == ZERO && b == ZERO) {
            throw new ArithmeticException();
        }
        return a.degree() == 0 || b.degree() == 0 ||
                a != ZERO && b != ZERO &&
                isEmpty(intersect(a.constantFactor().b.factor(), b.constantFactor().b.factor()));
    }

    private static boolean isRelativelyPrimeTo_alt(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.gcd(b) == ONE;
    }

    private void propertiesIsRelativelyPrimeTo() {
        initialize("isRelativelyPrimeTo(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            commutative(Polynomial::isRelativelyPrimeTo, p);
        }

        ps = filterInfinite(p -> p.a != ZERO || p.b != ZERO, P.pairs(P.withScale(4).polynomials()));
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            boolean isRelativelyPrimeTo = p.a.isRelativelyPrimeTo(p.b);
            assertEquals(p, isRelativelyPrimeTo, isRelativelyPrimeTo_simplest(p.a, p.b));
            assertEquals(p, isRelativelyPrimeTo, isRelativelyPrimeTo_alt(p.a, p.b));
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            assertTrue(p, p.isRelativelyPrimeTo(ONE));
            assertTrue(p, ONE.isRelativelyPrimeTo(p));
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(1))) {
            assertFalse(p, p.isRelativelyPrimeTo(ZERO));
            assertFalse(p, ZERO.isRelativelyPrimeTo(p));
        }
    }

    private void compareImplementationsIsRelativelyPrimeTo() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> isRelativelyPrimeTo_simplest(p.a, p.b));
        functions.put("alt", p -> isRelativelyPrimeTo_alt(p.a, p.b));
        functions.put("standard", p -> p.a.isRelativelyPrimeTo(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).polynomials())
        );
        compareImplementations("isRelativelyPrimeTo(Polynomial)", take(SMALL_LIMIT, ps), functions);
    }

    private static boolean isSquareFree_simplest(@NotNull Polynomial p) {
        return p != ZERO && unique(p.factor());
    }

    private void propertiesIsSquareFree() {
        initialize("isSquareFree()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            p.isSquareFree();
        }

        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            assertEquals(p, p.isSquareFree(), isSquareFree_simplest(p));
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(1))) {
            assertFalse(p, p.pow(2).isSquareFree());
        }

        for (Polynomial p : take(LIMIT, P.polynomials(0))) {
            assertTrue(p, p.isSquareFree());
        }
    }

    private void compareImplementationsIsSquareFree() {
        Map<String, Function<Polynomial, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::isSquareFree_simplest);
        functions.put("standard", Polynomial::isSquareFree);
        compareImplementations("isSquareFree()", take(SMALL_LIMIT, P.withScale(4).polynomials()), functions);
    }

    private static @NotNull Polynomial squareFreePart_simplest(@NotNull Polynomial p) {
        switch (p.degree()) {
            case -1: throw new ArithmeticException();
            case 0: return ONE;
            default: return product(nub(p.constantFactor().b.factor()));
        }
    }

    private void propertiesSquareFreePart() {
        initialize("squareFreePart()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Polynomial sfp = p.squareFreePart();
            sfp.validate();
            assertEquals(p, sfp.signum(), 1);
            assertTrue(p, sfp.isPrimitive());
            assertTrue(p, sfp.isSquareFree());
            assertTrue(p, p.isDivisibleBy(sfp));
        }

        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            assertEquals(p, p.squareFreePart(), squareFreePart_simplest(p));
        }
    }

    private void compareImplementationsSquareFreePart() {
        Map<String, Function<Polynomial, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::squareFreePart_simplest);
        functions.put("standard", Polynomial::squareFreePart);
        compareImplementations("squareFreePart()", take(SMALL_LIMIT, P.withScale(4).polynomialsAtLeast(0)), functions);
    }

    private void propertiesSquareFreeFactor() {
        initialize("squareFreeFactor()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            List<Polynomial> factors = p.squareFreeFactor();
            factors.forEach(Polynomial::validate);
            Polynomial ppp = p.constantFactor().b;
            assertEquals(p, product(factors), ppp);
            for (Polynomial factor : factors) {
                assertEquals(p, factor.signum(), 1);
                assertTrue(p, factor.isPrimitive());
                assertTrue(p, factor.isSquareFree());
            }
        }
    }

    //this uses Eisenstein's criterion. It's too slow for inputs with large coefficients because of prime factorization
    private static @NotNull List<Polynomial> factor_alt(@NotNull Polynomial p) {
        if (p == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (p == ONE) return Collections.emptyList();
        BigInteger gcd = MathUtils.gcd(init(p));
        if (gt(gcd, BigInteger.ONE)) {
            boolean eisenstein = any(
                    n -> !last(p).mod(n).equals(BigInteger.ZERO) &&
                            !head(p).mod(n.pow(2)).equals(BigInteger.ZERO),
                    toList(nub(MathUtils.primeFactors(gcd)))
            );
            if (eisenstein) {
                Pair<BigInteger, Polynomial> cf = p.constantFactor();
                return cf.a.equals(BigInteger.ONE) ? Collections.singletonList(p) : Arrays.asList(of(cf.a), cf.b);
            }
        }
        //noinspection RedundantCast
        return sort((Iterable<Polynomial>) map(Polynomial::of, JasApi.factorPolynomial(toList(p))));
    }

    private static boolean queueIsOptimal(@NotNull PriorityQueue<Pair<BigInteger, List<BigInteger>>> queue) {
        for (Pair<BigInteger, List<BigInteger>> entry : queue) {
            if (entry.b.size() > 4) return false;
        }
        return true;
    }

    private static @NotNull List<Polynomial> kroneckerHelper(@NotNull Polynomial p) {
        if (p.degree() == 1) {
            return Collections.singletonList(p);
        }
        int choiceCount = p.degree() / 2 + 1;
        PriorityQueue<Pair<BigInteger, List<BigInteger>>> choiceQueue = new PriorityQueue<>(
            (a, b) -> {
                if (a.b.size() > b.b.size()) return -1;
                if (a.b.size() < b.b.size()) return 1;
                return -a.a.abs().compareTo(b.a.abs());
            }
        );
        int countdown = choiceCount * 100;
        for (BigInteger x : ExhaustiveProvider.INSTANCE.bigIntegers()) {
            BigInteger y = p.apply(x);
            if (!y.equals(BigInteger.ZERO)) {
                List<BigInteger> yFactors = MathUtils.factors(y.abs());
                yFactors = toList(concat(yFactors, map(BigInteger::negate, yFactors)));
                if (choiceQueue.size() < choiceCount) {
                    choiceQueue.offer(new Pair<>(x, yFactors));
                } else {
                    Pair<BigInteger, List<BigInteger>> currentWorstChoice = choiceQueue.peek();
                    if (yFactors.size() < currentWorstChoice.b.size()) {
                        choiceQueue.poll();
                        choiceQueue.offer(new Pair<>(x, yFactors));
                    }
                }
                if (choiceQueue.size() == choiceCount && (queueIsOptimal(choiceQueue) || countdown <= 0)) {
                    break;
                }
            }
            countdown--;
        }
        Pair<Iterable<BigInteger>, Iterable<List<BigInteger>>> choiceLists = unzip(choiceQueue);
        List<BigInteger> xs = toList(choiceLists.a);
        //noinspection RedundantCast
        Iterable<List<Pair<BigInteger, BigInteger>>> choices = map(
                ys -> toList(zip(xs, ys)),
                ExhaustiveProvider.INSTANCE.cartesianProduct((List<List<BigInteger>>) toList(choiceLists.b))
        );
        for (List<Pair<BigInteger, BigInteger>> choice : choices) {
            Polynomial f = interpolate(choice).constantFactor().b;
            if (f != ONE && p.isDivisibleBy(f)) {
                return toList(concat(kroneckerHelper(f), kroneckerHelper(p.divideExact(f))));
            }
        }
        return Collections.singletonList(p);
    }

    private static @NotNull List<Polynomial> factor_Yun_Kronecker(@NotNull Polynomial p) {
        Pair<BigInteger, Polynomial> cf = p.constantFactor();
        List<Polynomial> squareFreeFactors = cf.b.squareFreeFactor();
        List<Polynomial> factors = new ArrayList<>();
        if (!cf.a.equals(BigInteger.ONE)) {
            factors.add(of(cf.a));
        }
        for (Polynomial squareFreeFactor : squareFreeFactors) {
            factors.addAll(kroneckerHelper(squareFreeFactor));
        }
        return sort(factors);
    }

    private void propertiesFactor() {
        initialize("factor()");
        boolean oldUseFactorCache = USE_FACTOR_CACHE;
        USE_FACTOR_CACHE = false;

        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            List<Polynomial> factors = p.factor();
            factors.forEach(Polynomial::validate);
            assertEquals(p, factors, factor_alt(p));
            assertFalse(p, any(q -> q == ZERO, factors));
            assertTrue(p, weaklyIncreasing(factors));
            assertEquals(p, product(factors), p);
            if (p != ONE) {
                boolean firstFactorConstant = head(factors).degree() == 0;
                assertTrue(
                        p,
                        all(
                                q -> q.degree() > 0 && q.signum() == 1 && q.isIrreducible(),
                                firstFactorConstant ? tail(factors) : factors
                        )
                );
            }
        }

        Iterable<Polynomial> ps = filterInfinite(
                q -> q.degree() < 7,
                P.withScale(1).withSecondaryScale(1).polynomialsAtLeast(0)
        );
        for (Polynomial p : take(LIMIT, ps)) {
            List<Polynomial> factors = p.factor();
            assertEquals(p, factors, factor_Yun_Kronecker(p));
        }

        Iterable<Pair<Polynomial, Polynomial>> ps2 = P.bagPairs(P.withScale(4).irreduciblePolynomialsAtLeast(1));
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps2)) {
            assertEquals(p, p.a.multiply(p.b).factor(), Pair.toList(p));
        }

        USE_FACTOR_CACHE = oldUseFactorCache;
    }

    private void compareImplementationsFactor1() {
        boolean oldUseFactorCache = USE_FACTOR_CACHE;
        USE_FACTOR_CACHE = false;

        Map<String, Function<Polynomial, List<Polynomial>>> functions = new LinkedHashMap<>();
        functions.put("alt", PolynomialProperties::factor_alt);
        functions.put("standard", Polynomial::factor);
        compareImplementations("factor()", take(SMALL_LIMIT, P.withScale(4).polynomialsAtLeast(0)), functions);

        USE_FACTOR_CACHE = oldUseFactorCache;
    }

    private void compareImplementationsFactor2() {
        boolean oldUseFactorCache = USE_FACTOR_CACHE;
        USE_FACTOR_CACHE = false;

        Map<String, Function<Polynomial, List<Polynomial>>> functions = new LinkedHashMap<>();
        functions.put("Yun-Kronecker", PolynomialProperties::factor_Yun_Kronecker);
        functions.put("standard", Polynomial::factor);
        Iterable<Polynomial> ps = filterInfinite(
                q -> q.degree() < 7,
                P.withScale(1).withSecondaryScale(1).polynomialsAtLeast(0)
        );
        compareImplementations("factor()", take(SMALL_LIMIT, ps), functions);

        USE_FACTOR_CACHE = oldUseFactorCache;
    }

    private static boolean isIrreducible_simplest(@NotNull Polynomial p) {
        if (p == ZERO) {
            throw new ArithmeticException("p cannot be zero.");
        }
        return p == ONE || p.degree() > 0 && p.factor().size() == 1;
    }

    private void propertiesIsIrreducible() {
        initialize("isIrreducible()");
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            boolean isIrreducible = p.isIrreducible();
            if (isIrreducible) {
                assertTrue(p, p.isPrimitive());
                assertTrue(p, p.signum() == 1);
            }
        }

        for (Polynomial p : take(SMALL_LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            assertEquals(p, p.isIrreducible(), isIrreducible_simplest(p));
        }

        for (Polynomial p : take(LIMIT, filterInfinite(q -> q != ONE, P.withScale(4).polynomialsAtLeast(0)))) {
            assertFalse(p, p.multiply(X).isIrreducible());
        }

        for (Polynomial p : take(LIMIT, filterInfinite(q -> q != ONE, P.polynomials(0)))) {
            assertFalse(p, p.isIrreducible());
        }
    }

    private void compareImplementationsIsIrreducible(boolean useFactorCache) {
        boolean oldUseFactorCache = USE_FACTOR_CACHE;
        USE_FACTOR_CACHE = useFactorCache;
        System.out.println("\t\tUSE_FACTOR_CACHE = " + USE_FACTOR_CACHE);

        Map<String, Function<Polynomial, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::isIrreducible_simplest);
        functions.put("standard", Polynomial::isIrreducible);
        compareImplementations("isIrreducible()", take(SMALL_LIMIT, P.withScale(4).polynomialsAtLeast(0)), functions);

        USE_FACTOR_CACHE = oldUseFactorCache;
    }

    private static @NotNull RationalPolynomial interpolate_simplest(
            @NotNull List<Pair<BigInteger, BigInteger>> points
    ) {
        return RationalPolynomial.interpolate(
                toList(map(p -> new Pair<>(Rational.of(p.a), Rational.of(p.b)), points))
        );
    }

    private void propertiesInterpolate() {
        initialize("interpolate(List<Pair<BigInteger, BigInteger>>)");
        Iterable<List<Pair<BigInteger, BigInteger>>> pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.withScale(4).distinctListsAtLeast(1, P.withScale(4).bigIntegers()),
                                rs -> P.lists(rs.size(), P.withScale(4).bigIntegers())
                        )
                )
        );
        for (List<Pair<BigInteger, BigInteger>> ps : take(LIMIT, pss)) {
            RationalPolynomial p = interpolate(ps);
            assertEquals(ps, p, interpolate_simplest(ps));
            p.validate();
            for (Pair<BigInteger, BigInteger> point : ps) {
                assertEquals(ps, p.apply(Rational.of(point.a)).bigIntegerValueExact(), point.b);
            }
        }

        Iterable<Pair<Polynomial, List<BigInteger>>> ps = P.dependentPairsInfinite(
                P.withScale(1).withSecondaryScale(1).polynomials(),
                p -> P.withScale(p.degree() + 2).distinctListsAtLeast(p.degree() + 1, P.withScale(1).bigIntegers())
        );
        for (Pair<Polynomial, List<BigInteger>> p : take(LIMIT, ps)) {
            List<Pair<BigInteger, BigInteger>> points = toList(map(r -> new Pair<>(r, p.a.apply(r)), p.b));
            assertEquals(p, interpolate(points).toPolynomial(), p.a);
        }

        Iterable<List<Pair<BigInteger, BigInteger>>> pssFail = filterInfinite(
                qs -> (qs.contains(null) || any(q -> q.a == null || q.b == null, filter(q -> q != null, qs)))
                        && unique(map(q -> q.a, filter(q -> q != null, qs))),
                P.lists(P.withScale(2).withNull(P.pairs(P.withScale(2).withNull(P.bigIntegers()))))
        );
        for (List<Pair<BigInteger, BigInteger>> qs : take(LIMIT, pssFail)) {
            try {
                interpolate(qs);
                fail(qs);
            } catch (NullPointerException ignored) {}
        }

        pssFail = filterInfinite(qs -> !unique(map(q -> q.a, qs)), P.lists(P.pairs(P.bigIntegers())));
        for (List<Pair<BigInteger, BigInteger>> qs : take(LIMIT, pssFail)) {
            try {
                interpolate(qs);
                fail(qs);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsInterpolate() {
        Map<String, Function<List<Pair<BigInteger, BigInteger>>, RationalPolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::interpolate_simplest);
        functions.put("standard", Polynomial::interpolate);
        Iterable<List<Pair<BigInteger, BigInteger>>> pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.withScale(4).distinctListsAtLeast(1, P.withScale(4).bigIntegers()),
                                rs -> P.lists(rs.size(), P.withScale(4).bigIntegers())
                        )
                )
        );
        compareImplementations("interpolate(List<Pair<BigInteger, BigInteger>>)", take(SMALL_LIMIT, pss), functions);
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::polynomials);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::polynomials);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Polynomial)");
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::polynomials);

        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomials()))) {
            assertEquals(p, p.a.compareTo(p.b), p.a.subtract(p.b).signum());
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            homomorphic(
                    Polynomial::of,
                    Polynomial::of,
                    Function.identity(),
                    BigInteger::compareTo,
                    Polynomial::compareTo,
                    p
            );
        }

        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.polynomials()))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            homomorphic(
                    Polynomial::degree,
                    Polynomial::degree,
                    Function.identity(),
                    Polynomial::compareTo,
                    Integer::compareTo,
                    p
            );
        }
    }

    private void propertiesRead_String() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                POLYNOMIAL_CHARS,
                P.polynomials(),
                Polynomial::read,
                Polynomial::validate,
                false
        );
    }

    private void propertiesRead_int_String() {
        initialize("read(int, String)");

        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.positiveIntegersGeometric());
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            read(p.b, p.a);
        }

        Iterable<Pair<Polynomial, Integer>> ps2 = filterInfinite(
                p -> p.a.degree() <= p.b,
                P.pairsLogarithmicOrder(P.polynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<Polynomial> op = read(p.b, p.a.toString());
            Polynomial q = op.get();
            q.validate();
            assertEquals(p, q, p.a);
        }

        ps2 = filterInfinite(
                p -> p.a.degree() > p.b,
                P.pairsLogarithmicOrder(P.polynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<Polynomial> op = read(p.b, p.a.toString());
            assertFalse(p, op.isPresent());
        }
    }

    private static @NotNull Optional<String> badString(@NotNull String s) {
        boolean seenX = false;
        boolean seenXCaret = false;
        int exponentDigitCount = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'x') {
                seenX = true;
            } else if (seenX && c == '^') {
                seenXCaret = true;
            } else if (seenXCaret && c >= '0' && c <= '9') {
                exponentDigitCount++;
                if (exponentDigitCount > 3) return Optional.of("");
            } else {
                seenX = false;
                seenXCaret = false;
                exponentDigitCount = 0;
            }
        }
        return Optional.empty();
    }

    private void propertiesFindIn_String() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.polynomials(),
                s -> badString(s).isPresent() ? Optional.empty() : read(s),
                s -> badString(s).isPresent() ? Optional.empty() : findIn(s),
                Polynomial::validate
        );
    }

    private void propertiesFindIn_int_String() {
        initialize("findIn(int, String)");
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(PolynomialProperties::badString).apply(s).isPresent(),
                        P.strings()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            findIn(p.b, p.a);
        }

        Iterable<Pair<Integer, String>> ps2 = P.dependentPairsInfiniteLogarithmicOrder(
                P.positiveIntegersGeometric(),
                i -> P.stringsWithSubstrings(
                        map(Object::toString, filterInfinite(p -> p.degree() <= i, P.polynomials()))
                )
        );
        for (Pair<Integer, String> p : take(LIMIT, ps2)) {
            Optional<Pair<Polynomial, Integer>> op = findIn(p.a, p.b);
            Pair<Polynomial, Integer> q = op.get();
            assertNotNull(p, q.a);
            assertNotNull(p, q.b);
            q.a.validate();
            assertTrue(p, q.b >= 0 && q.b < p.b.length());
            String before = take(q.b, p.b);
            assertFalse(p, findIn(before).isPresent());
            String during = q.a.toString();
            assertTrue(p, p.b.substring(q.b).startsWith(during));
            String after = drop(q.b + during.length(), p.b);
            assertTrue(p, after.isEmpty() || !read(during + head(after)).isPresent());
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, POLYNOMIAL_CHARS, P.polynomials(), Polynomial::read);

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Function.identity(), BigInteger::toString, Polynomial::toString, i);
        }
    }
}
