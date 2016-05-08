package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
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
        propertiesFromRoot_BigInteger();
        propertiesFromRoot_Rational();
        propertiesMaxCoefficientBitLength();
        propertiesDegree();
        propertiesLeading();
        propertiesMultiplyByPowerOfX();
        compareImplementationsMultiplyByPowerOfX();
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
        propertiesDivideExact_BigInteger();
        propertiesDivideExact_int();
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
        propertiesEvenPseudoDivide();
        compareImplementationsEvenPseudoDivide();
        propertiesEvenPseudoRemainder();
        compareImplementationsEvenPseudoRemainder();
        propertiesIsDivisibleBy();
        compareImplementationsIsDivisibleBy();
        propertiesDivideExact_Polynomial();
        compareImplementationsDivideExact_Polynomial();
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
        compareImplementationsFactor();
        propertiesIsIrreducible();
        compareImplementationsIsIrreducible(false);
        compareImplementationsIsIrreducible(true);
        propertiesInterpolate();
        compareImplementationsInterpolate();
        propertiesCompanionMatrix();
        propertiesCoefficientMatrix();
        propertiesAugmentedCoefficientMatrix();
        propertiesDeterminant();
        compareImplementationsDeterminant();
        propertiesSylvesterMatrix();
        propertiesResultant();
        propertiesSylvesterHabichtMatrix();
        compareImplementationsSylvesterHabichtMatrix();
        propertiesSignedSubresultantCoefficient();
        propertiesSylvesterHabichtPolynomialMatrix();
        compareImplementationsSylvesterHabichtPolynomialMatrix();
        propertiesSignedSubresultant();
        propertiesSignedSubresultantSequence();
        compareImplementationsSignedSubresultantSequence();
        propertiesPrimitiveSignedPseudoRemainderSequence();
        propertiesAbbreviatedSignedSubresultantSequence();
        propertiesRootBound();
        propertiesPowerOfTwoRootBound();
        propertiesRootCount_Interval();
        propertiesRootCount();
        compareImplementationsRootCount();
        propertiesIsolatingInterval();
        propertiesPowerOfTwoIsolatingInterval();
        propertiesIsolatingIntervals();
        compareImplementationsIsolatingIntervals();
        propertiesPowerOfTwoIsolatingIntervals();
        compareImplementationsPowerOfTwoIsolatingIntervals();
        propertiesReflect();
        propertiesTranslate();
        propertiesSpecialTranslate();
        compareImplementationsSpecialTranslate();
        propertiesPositivePrimitiveTranslate();
        compareImplementationsPositivePrimitiveTranslate();
        propertiesStretch();
        compareImplementationsStretch();
        propertiesPositivePrimitiveStretch();
        compareImplementationsPositivePrimitiveStretch();
        propertiesShiftRootsLeft();
        compareImplementationsShiftRootsLeft();
        propertiesShiftRootsRight();
        compareImplementationsShiftRootsRight();
        propertiesPositiveShiftRootsLeft();
        compareImplementationsPositivePrimitiveShiftRootsLeft();
        propertiesPositiveShiftRootsRight();
        compareImplementationsPositivePrimitiveShiftRootsRight();
        propertiesInvertRoots();
        propertiesAddRoots();
        propertiesMultiplyRoots();
        propertiesPowerTable();
        compareImplementationsPowerTable();
        propertiesRootPower();
        compareImplementationsRootPower();
        propertiesRealRoots();
        compareImplementationsRealRoots();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict_String();
        propertiesReadStrict_int_String();
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
            assertEquals(p, q, of(p.a).multiplyByPowerOfX(p.b));
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

    private void propertiesFromRoot_BigInteger() {
        initialize("fromRoot(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Polynomial p = fromRoot(i);
            p.validate();
            assertEquals(i, p.degree(), 1);
            assertTrue(i, p.isMonic());
            inverse(Polynomial::fromRoot, (Polynomial q) -> q.coefficient(0).negate(), i);
        }
    }

    private void propertiesFromRoot_Rational() {
        initialize("fromRoot(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Polynomial p = fromRoot(r);
            p.validate();
            assertEquals(r, p.degree(), 1);
            assertTrue(r, p.isPrimitive());
            assertEquals(r, p.signum(), 1);
            inverse(
                    Polynomial::fromRoot,
                    (Polynomial q) -> Rational.of(q.coefficient(0).negate(), q.coefficient(1)),
                    r
            );
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

    private static @NotNull Polynomial multiplyByPowerOfX_alt(@NotNull Polynomial a, int p) {
        return a.multiply(of(BigInteger.ONE, p));
    }

    private void propertiesMultiplyByPowerOfX() {
        initialize("multiplyByPowerOfX(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Polynomial q = p.a.multiplyByPowerOfX(p.b);
            q.validate();
            assertEquals(p, multiplyByPowerOfX_alt(p.a, p.b), q);
        }

        ps = P.pairsLogarithmicOrder(P.withScale(4).polynomialsAtLeast(0), P.withScale(4).naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            assertEquals(p, p.a.multiplyByPowerOfX(p.b).degree(), p.a.degree() + p.b);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.multiplyByPowerOfX(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsMultiplyByPowerOfX() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> multiplyByPowerOfX_alt(p.a, p.b));
        functions.put("standard", p -> p.a.multiplyByPowerOfX(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        compareImplementations("multiplyByPowerOfX(int)", take(LIMIT, ps), functions);
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

        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.nonzeroBigIntegers()))) {
            inverse(q -> q.multiply(p.b), (Polynomial q) -> q.divideExact(p.b), p.a);
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

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.nonzeroIntegers()))) {
            inverse(q -> q.multiply(p.b), (Polynomial q) -> q.divideExact(p.b), p.a);
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

    private void propertiesDivideExact_BigInteger() {
        initialize("divideExact(BigInteger)");
        Iterable<Pair<Polynomial, BigInteger>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.polynomials(), P.nonzeroBigIntegers())
        );
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, ps)) {
            Polynomial quotient = p.a.divideExact(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            assertEquals(p, quotient, p.a.divideExact(of(p.b)));
            inverse(q -> q.divideExact(p.b), (Polynomial q) -> q.multiply(p.b), p.a);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            fixedPoint(j -> j.divideExact(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.divideExact(BigInteger.ONE), p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.divideExact(BigInteger.ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDivideExact_int() {
        initialize("divideExact(int)");
        Iterable<Pair<Polynomial, Integer>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.polynomials(), P.nonzeroIntegers())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Polynomial quotient = p.a.divideExact(p.b);
            quotient.validate();
            assertTrue(p, quotient.degree() == p.a.degree());
            assertEquals(p, quotient, p.a.divideExact(of(BigInteger.valueOf(p.b))));
            inverse(q -> q.divideExact(p.b), (Polynomial q) -> q.multiply(p.b), p.a);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            fixedPoint(j -> j.divideExact(i), ZERO);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            fixedPoint(q -> q.divideExact(1), p);
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.divideExact(0);
                fail(p);
            } catch (ArithmeticException ignored) {}
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

            Pair<RationalPolynomial, RationalPolynomial> quotRem =
                        p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
            assertEquals(p, absolutePseudoQuotRem.a.constantFactor().b, quotRem.a.constantFactor().b);
            if (absolutePseudoQuotRem.b != ZERO) {
                assertEquals(p, absolutePseudoQuotRem.b.constantFactor().b, quotRem.b.constantFactor().b);
            }
            assertEquals(p, absolutePseudoQuotRem.a.signum(), quotRem.a.signum());
            assertEquals(p, absolutePseudoQuotRem.b.signum(), quotRem.b.signum());
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
        functions.put("standard", p -> p.a.absolutePseudoDivide(p.b));
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

    private static @NotNull Pair<Polynomial, Polynomial> evenPseudoDivide_simplest(
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
        int d = m - n + 1;
        if ((d & 1) != 0) d++;
        BigInteger factor = b.leading().get().pow(d);
        Pair<RationalPolynomial, RationalPolynomial> quotRem =
                a.multiply(factor).toRationalPolynomial().divide(b.toRationalPolynomial());
        return new Pair<>(quotRem.a.toPolynomial(), quotRem.b.toPolynomial());
    }

    private void propertiesEvenPseudoDivide() {
        initialize("evenPseudoDivide(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Pair<Polynomial, Polynomial> evenPseudoQuotRem = p.a.evenPseudoDivide(p.b);
            assertEquals(p, evenPseudoQuotRem, evenPseudoDivide_simplest(p.a, p.b));
            Polynomial quotient = evenPseudoQuotRem.a;
            assertNotNull(p, quotient);
            Polynomial remainder = evenPseudoQuotRem.b;
            assertNotNull(p, remainder);
            quotient.validate();
            remainder.validate();
            int d = p.a.degree() - p.b.degree() + 1;
            if ((d & 1) != 0) d++;
            BigInteger factor = p.b.leading().get().pow(d);
            assertEquals(p, quotient.multiply(p.b).add(remainder), p.a.multiply(factor));
            assertTrue(p, remainder.degree() < p.b.degree());

            Pair<RationalPolynomial, RationalPolynomial> quotRem =
                        p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
            assertEquals(p, evenPseudoQuotRem.a.constantFactor().b, quotRem.a.constantFactor().b);
            if (evenPseudoQuotRem.b != ZERO) {
                assertEquals(p, evenPseudoQuotRem.b.constantFactor().b, quotRem.b.constantFactor().b);
            }
            assertEquals(p, evenPseudoQuotRem.a.signum(), quotRem.a.signum());
            assertEquals(p, evenPseudoQuotRem.b.signum(), quotRem.b.signum());
        }

        ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.positivePrimitivePolynomials(), P.positivePrimitivePolynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Pair<Polynomial, Polynomial> evenPseudoQuotRem = p.a.evenPseudoDivide(p.b);
            assertEquals(p, evenPseudoQuotRem.a.signum(), 1);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroBigIntegers()))) {
            Pair<Polynomial, Polynomial> evenPseudoQuotRem = of(p.a).evenPseudoDivide(of(p.b));
            assertEquals(p, evenPseudoQuotRem.b, ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.evenPseudoDivide(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.evenPseudoDivide(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsEvenPseudoDivide() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Pair<Polynomial, Polynomial>>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> evenPseudoDivide_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.evenPseudoDivide(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("evenPseudoDivide(Polynomial)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial evenPseudoRemainder_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        return a.evenPseudoDivide(b).b;
    }

    private void propertiesEvenPseudoRemainder() {
        initialize("evenPseudoRemainder(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial evenPseudoRemainder = p.a.evenPseudoRemainder(p.b);
            evenPseudoRemainder.validate();
            assertEquals(p, evenPseudoRemainder, evenPseudoRemainder_simplest(p.a, p.b));
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroBigIntegers()))) {
            assertEquals(p, of(p.a).evenPseudoRemainder(of(p.b)), ZERO);
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.evenPseudoRemainder(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomials())) {
            try {
                p.evenPseudoRemainder(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsEvenPseudoRemainder() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> evenPseudoRemainder_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.evenPseudoRemainder(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() >= q.b.degree(),
                P.pairs(P.polynomials(), P.polynomialsAtLeast(0))
        );
        compareImplementations("evenPseudoRemainder(Polynomial)", take(LIMIT, ps), functions);
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

    private static @NotNull Polynomial divideExact_Polynomial_simplest(@NotNull Polynomial a, @NotNull Polynomial b) {
        Pair<RationalPolynomial, RationalPolynomial> quotRem =
                a.toRationalPolynomial().divide(b.toRationalPolynomial());
        if (quotRem.b != RationalPolynomial.ZERO || !quotRem.a.onlyHasIntegralCoefficients()) {
            throw new ArithmeticException();
        }
        return quotRem.a.toPolynomial();
    }

    private void propertiesDivideExact_Polynomial() {
        initialize("divideExact(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.a),
                P.pairs(P.polynomialsAtLeast(1), P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial quotient = p.a.divideExact(p.b);
            quotient.validate();
            assertEquals(p, divideExact_Polynomial_simplest(p.a, p.b), quotient);
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
                    return quotRem.b != RationalPolynomial.ZERO || !quotRem.a.onlyHasIntegralCoefficients();
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

    private void compareImplementationsDivideExact_Polynomial() {
        Map<String, Function<Pair<Polynomial, Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divideExact_Polynomial_simplest(p.a, p.b));
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
        if (!quotRem.a.onlyHasIntegralCoefficients() || !quotRem.b.onlyHasIntegralCoefficients()) {
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
                    return qr.a.onlyHasIntegralCoefficients() && qr.b.onlyHasIntegralCoefficients();
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
                    return !qr.a.onlyHasIntegralCoefficients() || !qr.b.onlyHasIntegralCoefficients();
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
                    return qr.a.onlyHasIntegralCoefficients() && qr.b.onlyHasIntegralCoefficients();
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

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.trivialPseudoRemainderSequence(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
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

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.trivialPseudoRemainderSequence(ZERO), Collections.singletonList(p));
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.primitivePseudoRemainderSequence(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
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

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.trivialPseudoRemainderSequence(ZERO), Collections.singletonList(p));
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.subresultantSequence(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
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

    private static boolean isRelativelyPrimeTo_alt2(@NotNull Polynomial a, @NotNull Polynomial b) {
        if (a == ZERO && b == ZERO) {
            throw new ArithmeticException();
        }
        return a.degree() == 0 || b.degree() == 0 ||
                a != ZERO && b != ZERO && !a.resultant(b).equals(BigInteger.ZERO);
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
            assertEquals(p, isRelativelyPrimeTo, isRelativelyPrimeTo_alt2(p.a, p.b));
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
        functions.put("alt2", p -> isRelativelyPrimeTo_alt2(p.a, p.b));
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
        for (Polynomial p : take(LIMIT, P.positivePrimitivePolynomialsAtLeast(0))) {
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

    private void compareImplementationsFactor() {
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

    private void propertiesCompanionMatrix() {
        initialize("companionMatrix()");
        for (Polynomial p : take(LIMIT, filterInfinite(Polynomial::isMonic, P.polynomials()))) {
            Matrix companionMatrix = p.companionMatrix();
            assertTrue(
                    p,
                    companionMatrix.submatrix(toList(range(1, p.degree() - 1)), toList(range(0, p.degree() - 2)))
                            .isIdentity()
            );
        }

        for (Polynomial p : take(LIMIT, filterInfinite(Polynomial::isMonic, P.polynomialsAtLeast(1)))) {
            Matrix companionMatrix = p.companionMatrix();
            assertTrue(
                    p,
                    companionMatrix.submatrix(Collections.singletonList(0), toList(range(0, p.degree() - 2))).isZero()
            );
        }

        Iterable<Polynomial> ps = filterInfinite(
                Polynomial::isMonic,
                P.withScale(4).withSecondaryScale(4).polynomials()
        );
        for (Polynomial p : take(LIMIT, ps)) {
            inverse(Polynomial::companionMatrix, Matrix::characteristicPolynomial, p);
        }

        for (Polynomial p : take(LIMIT, filterInfinite(Polynomial::isMonic, P.polynomials(1)))) {
            Matrix companionMatrix = p.companionMatrix();
            assertEquals(p, companionMatrix.height(), 1);
            assertEquals(p, companionMatrix.width(), 1);
            assertEquals(p, companionMatrix.get(0, 0), p.coefficient(0).negate());
        }

        for (Polynomial p : take(LIMIT, filterInfinite(q -> !q.isMonic(), P.polynomials()))) {
            try {
                p.companionMatrix();
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesCoefficientMatrix() {
        initialize("coefficientMatrix(List<Polynomial>)");
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.listsAtLeast(1, P.polynomials())
                )
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            Matrix coefficientMatrix = coefficientMatrix(ps);
            assertEquals(ps, coefficientMatrix.height(), ps.size());
            assertTrue(ps, coefficientMatrix.height() <= coefficientMatrix.width());
            if (!ps.isEmpty()) {
                assertEquals(ps, coefficientMatrix.width(), maximum(map(Polynomial::degree, ps)) + 1);
                assertFalse(
                        ps,
                        coefficientMatrix.submatrix(toList(range(0, ps.size() - 1)), Collections.singletonList(0))
                                .isZero()
                );
            }
        }

        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            List<Polynomial> ps = new ArrayList<>();
            for (int j = i - 1; j >= 0; j--) {
                ps.add(of(BigInteger.ONE, j));
            }
            assertEquals(i, coefficientMatrix(ps), Matrix.identity(i));
        }

        Iterable<List<Polynomial>> pssFail = filterInfinite(
                ps -> ps.size() > maximum(map(Polynomial::degree, ps)) + 1,
                P.listsAtLeast(1, P.polynomials())
        );
        for (List<Polynomial> ps : take(LIMIT, pssFail)) {
            try {
                coefficientMatrix(ps);
                fail(ps);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesAugmentedCoefficientMatrix() {
        initialize("augmentedCoefficientMatrix(List<Polynomial>)");
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.listsAtLeast(1, P.polynomials())
                )
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            PolynomialMatrix m = augmentedCoefficientMatrix(ps);
            assertEquals(ps, m.height(), ps.size());
            assertTrue(ps, m.height() <= m.width());
        }

        Iterable<List<Polynomial>> pssFail = filterInfinite(
                ps -> ps.size() > maximum(map(Polynomial::degree, ps)) + 1,
                P.listsAtLeast(1, P.polynomials())
        );
        for (List<Polynomial> ps : take(LIMIT, pssFail)) {
            try {
                augmentedCoefficientMatrix(ps);
                fail(ps);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull Polynomial determinant_simplest(@NotNull List<Polynomial> ps) {
        return augmentedCoefficientMatrix(ps).determinant();
    }

    private void propertiesDeterminant() {
        initialize("determinant(List<Polynomial>)");
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.listsAtLeast(1, P.polynomials())
                )
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            Polynomial det = determinant(ps);
            assertEquals(ps, determinant_simplest(ps), det);
        }

        Iterable<Pair<List<Polynomial>, Pair<Integer, Integer>>> ps = P.dependentPairs(
                filterInfinite(
                        qs -> qs.size() <= maximum(map(Polynomial::degree, qs)) + 1,
                        P.listsAtLeast(2, P.polynomials())
                ),
                qs -> P.subsetPairs(P.range(0, qs.size() - 1))
        );
        for (Pair<List<Polynomial>, Pair<Integer, Integer>> p : take(LIMIT, ps)) {
            List<Polynomial> rows = toList(p.a);
            Collections.swap(rows, p.b.a, p.b.b);
            assertEquals(p, determinant(rows), determinant(p.a).negate());
        }

        Iterable<Pair<Pair<List<Polynomial>, BigInteger>, Integer>> ps2 = P.dependentPairs(
                P.pairs(
                        filterInfinite(
                                qs -> qs.size() <= maximum(map(Polynomial::degree, qs)) + 1,
                                P.listsAtLeast(1, P.polynomials())
                        ),
                        P.nonzeroBigIntegers()
                ),
                p -> P.range(0, p.a.size() - 1)
        );
        for (Pair<Pair<List<Polynomial>, BigInteger>, Integer> p : take(LIMIT, ps2)) {
            List<Polynomial> rows = toList(p.a.a);
            rows.set(p.b, rows.get(p.b).multiply(p.a.b));
            assertEquals(p, determinant(rows), determinant(p.a.a).multiply(p.a.b));
        }

        Iterable<List<Polynomial>> pssFail = filterInfinite(
                qs -> qs.size() > maximum(map(Polynomial::degree, qs)) + 1,
                P.listsAtLeast(1, P.polynomials())
        );
        for (List<Polynomial> qs : take(LIMIT, pssFail)) {
            try {
                determinant(qs);
                fail(ps);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsDeterminant() {
        Map<String, Function<List<Polynomial>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::determinant_simplest);
        functions.put("standard", Polynomial::determinant);
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.listsAtLeast(1, P.polynomials())
                )
        );
        compareImplementations("determinant(List<Polynomial>)", take(SMALL_LIMIT, pss), functions);
    }

    private void propertiesSylvesterMatrix() {
        initialize("sylvesterMatrix(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomialsAtLeast(0)))) {
            Matrix m = p.a.sylvesterMatrix(p.b);
            assertTrue(p, m.isSquare());
            assertEquals(p, m.width(), p.a.degree() + p.b.degree());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertTrue(p, p.sylvesterMatrix(ONE).isIdentity());
            assertTrue(p, ONE.sylvesterMatrix(p).isIdentity());
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            try {
                p.sylvesterMatrix(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
            try {
                ZERO.sylvesterMatrix(p);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesResultant() {
        initialize("resultant(Polynomial)");
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.polynomialsAtLeast(0)))) {
            BigInteger resultant = p.a.resultant(p.b);
            assertNotEquals(p, resultant.equals(BigInteger.ZERO), p.a.isRelativelyPrimeTo(p.b));
            BigInteger reverseResultant = resultant;
            if ((p.a.degree() * p.b.degree() % 2) != 0) reverseResultant = reverseResultant.negate();
            assertEquals(p, p.b.resultant(p.a), reverseResultant);
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.resultant(ONE), BigInteger.ONE);
            assertEquals(p, ONE.resultant(p), BigInteger.ONE);
        }

        Iterable<Triple<Polynomial, Polynomial, Polynomial>> ts = P.withScale(4).triples(P.polynomialsAtLeast(0));
        for (Triple<Polynomial, Polynomial, Polynomial> t : take(LIMIT, ts)) {
            assertEquals(t, t.a.multiply(t.b).resultant(t.c), t.a.resultant(t.c).multiply(t.b.resultant(t.c)));
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            try {
                p.resultant(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
            try {
                ZERO.resultant(p);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Matrix sylvesterHabichtMatrix_alt(@NotNull Polynomial p, @NotNull Polynomial q, int j) {
        if (p == ZERO || q == ZERO || p.degree() <= q.degree() || j < 0 || j > q.degree()) {
            throw new IllegalArgumentException();
        }
        List<Polynomial> ps = new ArrayList<>();
        for (int i = q.degree() - j - 1; i >= 0; i--) {
            ps.add(p.multiplyByPowerOfX(i));
        }
        for (int i = 0; i < p.degree() - j; i++) {
            ps.add(q.multiplyByPowerOfX(i));
        }
        return coefficientMatrix(ps);
    }

    private void propertiesSylvesterHabichtMatrix() {
        initialize("sylvesterHabichtMatrix(Polynomial, int)");
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            Matrix m = t.a.sylvesterHabichtMatrix(t.b, t.c);
            assertEquals(t, m, sylvesterHabichtMatrix_alt(t.a, t.b, t.c));
            assertEquals(t, m.height(), t.a.degree() + t.b.degree() - 2 * t.c);
            assertEquals(t, m.width(), t.a.degree() + t.b.degree() - t.c);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() <= p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.sylvesterHabichtMatrix(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> filterInfinite(i -> i < 0 || i > p.b.degree(), P.integers())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.sylvesterHabichtMatrix(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomialsAtLeast(0), P.positiveIntegers()))) {
            try {
                p.a.sylvesterHabichtMatrix(ZERO, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}

            try {
                ZERO.sylvesterHabichtMatrix(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSylvesterHabichtMatrix() {
        Map<String, Function<Triple<Polynomial, Polynomial, Integer>, Matrix>> functions = new LinkedHashMap<>();
        functions.put("alt", t -> sylvesterHabichtMatrix_alt(t.a, t.b, t.c));
        functions.put("standard", t -> t.a.sylvesterHabichtMatrix(t.b, t.c));
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        compareImplementations("sylvesterHabichtMatrix(Polynomial, int)", take(SMALL_LIMIT, ts), functions);
    }

    private void propertiesSignedSubresultantCoefficient() {
        initialize("signedSubresultantCoefficient(Polynomial, int)");
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.a.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            t.a.signedSubresultantCoefficient(t.b, t.c);
        }

        ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            assertEquals(
                    t,
                    t.a.signedSubresultantCoefficient(t.b, t.c),
                    t.a.signedSubresultant(t.b, t.c).coefficient(t.c)
            );
            assertEquals(
                    t,
                    t.a.signedSubresultant(t.b, t.c).degree() == t.c,
                    !t.a.signedSubresultantCoefficient(t.b, t.c).equals(BigInteger.ZERO)
            );
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() <= p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.a.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.signedSubresultantCoefficient(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> filterInfinite(i -> i < 0 || i > p.a.degree(), P.integers())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.signedSubresultantCoefficient(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomialsAtLeast(0), P.positiveIntegers()))) {
            try {
                p.a.signedSubresultantCoefficient(ZERO, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}

            try {
                ZERO.signedSubresultantCoefficient(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull PolynomialMatrix sylvesterHabichtPolynomialMatrix_alt(
            @NotNull Polynomial p,
            @NotNull Polynomial q,
            int j
    ) {
        if (p == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (q == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        int pDegree = p.degree();
        int qDegree = q.degree();
        if (pDegree <= qDegree) {
            throw new IllegalArgumentException("this must have a degree greater than that. p: " + p + ", q: " + q);
        }
        if (j < 0) {
            throw new IllegalArgumentException("j cannot be negative. Invalid j: " + j);
        }
        if (j > qDegree) {
            throw new IllegalArgumentException("j cannot be greater than the degree of q. j: " + j + ", q: " + q);
        }
        List<Polynomial> ps = new ArrayList<>();
        for (int i = qDegree - j - 1; i >= 0; i--) {
            ps.add(p.multiplyByPowerOfX(i));
        }
        for (int i = 0; i < pDegree - j; i++) {
            ps.add(q.multiplyByPowerOfX(i));
        }
        return augmentedCoefficientMatrix(ps);
    }

    private void propertiesSylvesterHabichtPolynomialMatrix() {
        initialize("sylvesterHabichtPolynomialMatrix(Polynomial, int)");
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            PolynomialMatrix m = t.a.sylvesterHabichtPolynomialMatrix(t.b, t.c);
            assertEquals(t, m, sylvesterHabichtPolynomialMatrix_alt(t.a, t.b, t.c));
            assertTrue(t, m.isSquare());
            assertEquals(t, m.width(), t.a.degree() + t.b.degree() - 2 * t.c);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() <= p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.sylvesterHabichtPolynomialMatrix(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> filterInfinite(i -> i < 0 || i > p.b.degree(), P.integers())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.sylvesterHabichtPolynomialMatrix(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomialsAtLeast(0), P.positiveIntegers()))) {
            try {
                p.a.sylvesterHabichtPolynomialMatrix(ZERO, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}

            try {
                ZERO.sylvesterHabichtPolynomialMatrix(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSylvesterHabichtPolynomialMatrix() {
        Map<String, Function<Triple<Polynomial, Polynomial, Integer>, PolynomialMatrix>> functions =
                new LinkedHashMap<>();
        functions.put("alt", t -> sylvesterHabichtPolynomialMatrix_alt(t.a, t.b, t.c));
        functions.put("standard", t -> t.a.sylvesterHabichtPolynomialMatrix(t.b, t.c));
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.b.degree())
                )
        );
        compareImplementations("sylvesterHabichtPolynomialMatrix(Polynomial, int)", take(SMALL_LIMIT, ts), functions);
    }

    private void propertiesSignedSubresultant() {
        initialize("signedSubresultant(Polynomial, int)");
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.a.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            Polynomial p = t.a.signedSubresultant(t.b, t.c);
            p.validate();
            assertTrue(t, p.degree() <= t.c);
            if (p != ZERO) {
                int i = t.c + 1;
                int j = p.degree();
                Polynomial sResPJMinus1 = j == 0 ? ZERO : t.a.signedSubresultant(t.b, j - 1);
                int k = sResPJMinus1.degree();
                if (j == 0 || sResPJMinus1 == ZERO) {
                    assertEquals(
                            t,
                            p.constantFactor().b,
                            t.a.gcd(t.b).constantFactor().b
                    );
                    for (int l = 1; l < j; l++) {
                        assertEquals(t, t.a.signedSubresultant(t.b, l), ZERO);
                    }
                } else {
                    Polynomial sResPKMinus1 = k == 0 ? ZERO : t.a.signedSubresultant(t.b, k - 1);
                    Polynomial sResPIMinus1 = t.a.signedSubresultant(t.b, i - 1);
                    Polynomial left = sResPKMinus1.multiply(
                            t.a.signedSubresultantCoefficient(t.b, j)
                                    .multiply(sResPIMinus1.leading().orElse(BigInteger.ONE))
                    );
                    Polynomial right = sResPIMinus1.multiply(
                            t.a.signedSubresultantCoefficient(t.b, k)
                                    .multiply(sResPJMinus1.leading().orElse(BigInteger.ONE))
                    ).remainderExact(sResPJMinus1).negate();
                    assertEquals(
                            t,
                            left == ZERO ? ZERO : left.contentAndPrimitive().b,
                            right == ZERO ? ZERO : right.contentAndPrimitive().b
                    );
                }
                if (j <= t.b.degree() && k < j - 1) {
                    for (int l = k + 1; l < j - 1; l++) {
                        assertTrue(t, t.a.signedSubresultant(t.b, l) == ZERO);
                    }
                    BigInteger sk = sResPJMinus1.leading().orElse(BigInteger.ONE).pow(j - k)
                            .divide(t.a.signedSubresultantCoefficient(t.b, j).pow(j - k -1));
                    if (!MathUtils.reversePermutationSign(j - k)) {
                        sk = sk.negate();
                    }
                    Polynomial left = (k == -1 ? ZERO : t.a.signedSubresultant(t.b, k)).multiply(sResPJMinus1.leading()
                            .orElse(BigInteger.ONE));
                    Polynomial right = sResPJMinus1.multiply(sk);
                    assertEquals(t, left, right);
                }
            }
        }

        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() > p.b.degree(),
                P.pairs(P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial q = p.b.multiply(p.b.leading().get().pow(p.a.degree() - p.b.degree() - 1));
            if (!MathUtils.reversePermutationSign(p.a.degree() - p.b.degree())) q = q.negate();
            assertEquals(p, p.a.signedSubresultant(p.b, p.b.degree()), q);
        }

        Iterable<Triple<Polynomial, Polynomial, Integer>> tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() <= p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> P.range(0, p.a.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.signedSubresultant(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        tsFail = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(p -> p.a.degree() > p.b.degree(), P.pairs(P.polynomialsAtLeast(0))),
                        p -> filterInfinite(i -> i < 0 || i > p.a.degree(), P.integers())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.signedSubresultant(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomialsAtLeast(0), P.positiveIntegers()))) {
            try {
                p.a.signedSubresultant(ZERO, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}

            try {
                ZERO.signedSubresultant(p.a, p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull List<Polynomial> signedSubresultantSequence_alt(
            @NotNull Polynomial a,
            @NotNull Polynomial b
    ) {
        if (a == ZERO) {
            throw new ArithmeticException("a cannot be zero.");
        }
        if (b == ZERO) {
            throw new ArithmeticException("b cannot be zero.");
        }
        List<Polynomial> sequence = new ArrayList<>();
        for (int i = a.degree(); i >= 0; i--) {
            sequence.add(a.signedSubresultant(b, i));
        }
        return sequence;
    }

    private void propertiesSignedSubresultantSequence() {
        initialize("signedSubresultantSequence(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() > q.b.degree(),
                P.pairs(P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            List<Polynomial> sequence = p.a.signedSubresultantSequence(p.b);
            sequence.forEach(Polynomial::validate);
            assertEquals(p, sequence, signedSubresultantSequence_alt(p.a, p.b));
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() <= q.b.degree(),
                P.pairs(P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.signedSubresultantSequence(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            try {
                p.signedSubresultantSequence(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
            try {
                ZERO.signedSubresultantSequence(p);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsSignedSubresultantSequence() {
        Map<String, Function<Pair<Polynomial, Polynomial>, List<Polynomial>>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> signedSubresultantSequence_alt(p.a, p.b));
        functions.put("standard", p -> p.a.signedSubresultantSequence(p.b));
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() > q.b.degree(),
                P.pairs(P.withScale(4).polynomialsAtLeast(0))
        );
        compareImplementations("signedSubresultantSequence(Polynomial)", take(SMALL_LIMIT, ps), functions);
    }

    private void propertiesPrimitiveSignedPseudoRemainderSequence() {
        initialize("primitiveSignedPseudoRemainderSequence(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            List<Polynomial> sequence = p.a.primitiveSignedPseudoRemainderSequence(p.b);
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

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            assertEquals(p, p.trivialPseudoRemainderSequence(ZERO), Collections.singletonList(p));
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() < q.b.degree(),
                P.pairs(P.polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.primitiveSignedPseudoRemainderSequence(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesAbbreviatedSignedSubresultantSequence() {
        initialize("abbreviatedSignedSubresultantSequence(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                q -> q.a.degree() > q.b.degree(),
                P.pairs(P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            List<Polynomial> sequence = p.a.abbreviatedSignedSubresultantSequence(p.b);
            sequence.forEach(Polynomial::validate);
            assertTrue(p, decreasing(map(Polynomial::degree, sequence)));
        }

        Iterable<Pair<Polynomial, Polynomial>> psFail = filterInfinite(
                q -> q.a.degree() <= q.b.degree(),
                P.pairs(P.polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, psFail)) {
            try {
                p.a.abbreviatedSignedSubresultantSequence(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            try {
                p.abbreviatedSignedSubresultantSequence(ZERO);
                fail(p);
            } catch (ArithmeticException ignored) {}
            try {
                ZERO.abbreviatedSignedSubresultantSequence(p);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRootBound() {
        initialize("rootBound()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Interval rootBound = p.rootBound();
            assertTrue(p, rootBound.isFinitelyBounded());
            Rational left = rootBound.getLower().get();
            Rational right = rootBound.getUpper().get();
            assertEquals(p, left, right.negate());

            int leftSign = (p.degree() & 1) == 0 ? p.signum() : -p.signum();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(left), QEP.rangeDown(left)))) {
                assertEquals(p, p.signum(r), leftSign);
            }
            int rightSign = p.signum();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(right), QEP.rangeUp(right)))) {
                assertEquals(p, p.signum(r), rightSign);
            }
        }
    }

    private void propertiesPowerOfTwoRootBound() {
        initialize("powerOfTwoRootBound()");
        for (Polynomial p : take(LIMIT, P.polynomialsAtLeast(0))) {
            Interval rootBound = p.powerOfTwoRootBound();
            assertTrue(p, rootBound.isFinitelyBounded());
            assertTrue(p, rootBound.contains(p.rootBound()));
            Rational left = rootBound.getLower().get();
            Rational right = rootBound.getUpper().get();
            assertTrue(p, right.isPowerOfTwo());
            assertEquals(p, left, right.negate());

            int leftSign = (p.degree() & 1) == 0 ? p.signum() : -p.signum();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(left), QEP.rangeDown(left)))) {
                assertEquals(p, p.signum(r), leftSign);
            }
            int rightSign = p.signum();
            for (Rational r : take(TINY_LIMIT, filterInfinite(s -> !s.equals(right), QEP.rangeUp(right)))) {
                assertEquals(p, p.signum(r), rightSign);
            }
        }
    }

    private void propertiesRootCount_Interval() {
        initialize("rootCount(Interval)");
        for (Pair<Polynomial, Interval> p : take(LIMIT, P.pairs(P.squareFreePolynomialsAtLeast(0), P.intervals()))) {
            int rootCount = p.a.rootCount(p.b);
            assertTrue(p, rootCount >= 0);
            assertTrue(p, rootCount <= p.a.degree());
        }

        for (Interval a : take(LIMIT, P.intervals())) {
            try {
                ZERO.rootCount(a);
                fail(a);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static int rootCount_alt(@NotNull Polynomial p) {
        return p.rootCount(p.rootBound());
    }

    private void propertiesRootCount() {
        for (Polynomial p : take(LIMIT, P.squareFreePolynomialsAtLeast(0))) {
            int rootCount = p.rootCount();
            assertEquals(p, rootCount, rootCount_alt(p));
            assertTrue(p, rootCount >= 0);
            assertTrue(p, rootCount <= p.degree());
        }
    }

    private void compareImplementationsRootCount() {
        Map<String, Function<Polynomial, Integer>> functions = new LinkedHashMap<>();
        functions.put("alt", PolynomialProperties::rootCount_alt);
        functions.put("standard", Polynomial::rootCount);
        compareImplementations("rootCount()", take(LIMIT, P.squareFreePolynomialsAtLeast(0)), functions);
    }

    private void propertiesIsolatingInterval() {
        initialize("isolatingInterval()");
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).squareFreePolynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Interval interval = p.a.isolatingInterval(p.b);
            assertTrue(p, interval.isFinitelyBounded());
            assertEquals(p, p.a.rootCount(interval), 1);
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            try {
                ZERO.isolatingInterval(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairs(
                P.squareFreePolynomialsAtLeast(1),
                P.negativeIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.isolatingInterval(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        psFail = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.squareFreePolynomialsAtLeast(1)),
                q -> P.rangeUpGeometric(q.rootCount())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.isolatingInterval(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesPowerOfTwoIsolatingInterval() {
        initialize("powerOfTwoIsolatingInterval()");
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).squareFreePolynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Interval interval = p.a.powerOfTwoIsolatingInterval(p.b);
            assertTrue(p, interval.isFinitelyBounded());
            assertTrue(p, interval.getLower().get().isBinaryFraction());
            assertTrue(p, interval.getUpper().get().isBinaryFraction());
            assertEquals(p, p.a.rootCount(interval), 1);
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            try {
                ZERO.powerOfTwoIsolatingInterval(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairs(
                P.squareFreePolynomialsAtLeast(1),
                P.negativeIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.powerOfTwoIsolatingInterval(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }

        psFail = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.squareFreePolynomialsAtLeast(1)),
                q -> P.rangeUpGeometric(q.rootCount())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.powerOfTwoIsolatingInterval(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull List<Interval> isolatingIntervals_alt(@NotNull Polynomial p) {
        return toList(map(p::isolatingInterval, range(0, p.rootCount() - 1)));
    }

    private void propertiesIsolatingIntervals() {
        initialize("isolatingIntervals()");
        for (Polynomial p : take(LIMIT, P.withScale(4).withSecondaryScale(4).squareFreePolynomials())) {
            List<Interval> intervals = p.isolatingIntervals();
            assertEquals(p, intervals.size(), p.rootCount());
            for (Interval a : intervals) {
                assertTrue(p, a.isFinitelyBounded());
                assertEquals(p, p.rootCount(a), 1);
            }
        }

        for (Polynomial p : take(LIMIT, P.polynomials(0))) {
            assertTrue(p, p.isolatingIntervals().isEmpty());
        }
    }

    private void compareImplementationsIsolatingIntervals() {
        Map<String, Function<Polynomial, List<Interval>>> functions = new LinkedHashMap<>();
        functions.put("alt", PolynomialProperties::isolatingIntervals_alt);
        functions.put("standard", Polynomial::isolatingIntervals);
        Iterable<Polynomial> ps = P.withScale(4).withSecondaryScale(4).squareFreePolynomials();
        compareImplementations("isolatingIntervals()", take(LIMIT, ps), functions);
    }

    private static @NotNull List<Interval> powerOfTwoIsolatingIntervals_alt(@NotNull Polynomial p) {
        return toList(map(p::powerOfTwoIsolatingInterval, range(0, p.rootCount() - 1)));
    }

    private void propertiesPowerOfTwoIsolatingIntervals() {
        initialize("powerOfTwoIsolatingIntervals()");
        for (Polynomial p : take(LIMIT, P.withScale(4).withSecondaryScale(4).squareFreePolynomials())) {
            List<Interval> intervals = p.powerOfTwoIsolatingIntervals();
            assertEquals(p, intervals.size(), p.rootCount());
            for (Interval a : intervals) {
                assertTrue(p, a.isFinitelyBounded());
                assertTrue(p, a.getLower().get().isBinaryFraction());
                assertTrue(p, a.getUpper().get().isBinaryFraction());
                assertEquals(p, p.rootCount(a), 1);
            }
        }

        for (Polynomial p : take(LIMIT, P.polynomials(0))) {
            assertTrue(p, p.powerOfTwoIsolatingIntervals().isEmpty());
        }
    }

    private void compareImplementationsPowerOfTwoIsolatingIntervals() {
        Map<String, Function<Polynomial, List<Interval>>> functions = new LinkedHashMap<>();
        functions.put("alt", PolynomialProperties::powerOfTwoIsolatingIntervals_alt);
        functions.put("standard", Polynomial::powerOfTwoIsolatingIntervals);
        Iterable<Polynomial> ps = P.withScale(4).withSecondaryScale(4).squareFreePolynomials();
        compareImplementations("powerOfTwoIsolatingIntervals()", take(LIMIT, ps), functions);
    }

    private void propertiesReflect() {
        initialize("reflect()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial reflected = p.reflect();
            reflected.validate();
            assertEquals(p, p.degree(), reflected.degree());
            assertEquals(p, p.signum(), reflected.signum());
            involution(Polynomial::reflect, p);
        }

        for (Polynomial p : take(LIMIT, P.withScale(4).irreduciblePolynomials())) {
            assertTrue(p, p.reflect().isIrreducible());
        }

        for (List<Rational> rs : take(LIMIT, P.bags(P.rationals()))) {
            Polynomial p = product(map(Polynomial::fromRoot, rs));
            List<Rational> negativeRs = toList(map(Rational::negate, rs));
            Polynomial negativeRootsP = product(map(Polynomial::fromRoot, negativeRs));
            assertEquals(rs, p.reflect(), negativeRootsP);
        }
    }

    private void propertiesTranslate() {
        initialize("translate(BigInteger)");
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.polynomials(), P.bigIntegers()))) {
            Polynomial translated = p.a.translate(p.b);
            translated.validate();
            assertEquals(p, p.a.degree(), translated.degree());
            assertEquals(p, p.a.signum(), translated.signum());
            inverse(q -> q.translate(p.b), (Polynomial q) -> q.translate(p.b.negate()), p.a);
        }

        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q, q.translate(p.b));
        }

        Iterable<Pair<Polynomial, BigInteger>> ps = P.pairs(P.withScale(4).irreduciblePolynomials(), P.bigIntegers());
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.translate(p.b).isIrreducible());
        }

        Iterable<Pair<BigInteger, List<Rational>>> qs = P.pairs(P.bigIntegers(), P.withScale(4).bags(P.rationals()));
        for (Pair<BigInteger, List<Rational>> p : take(LIMIT, qs)) {
            Polynomial q = product(map(Polynomial::fromRoot, p.b));
            List<Rational> translatedRs = toList(map(r -> r.add(Rational.of(p.a)), p.b));
            Polynomial translatedRootsP = product(map(Polynomial::fromRoot, translatedRs));
            assertEquals(p, q.translate(p.a), translatedRootsP);
        }
    }

    private static @NotNull Polynomial specialTranslate_simplest(@NotNull Polynomial p, @NotNull Rational t) {
        if (p.degree() < 1 || t == Rational.ZERO) return p;
        return p.toRationalPolynomial().translate(t).multiply(t.getDenominator().pow(p.degree())).toPolynomial();
    }

    private void propertiesSpecialTranslate() {
        initialize("specialTranslate(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            Polynomial translated = p.a.specialTranslate(p.b);
            translated.validate();
            assertEquals(p, specialTranslate_simplest(p.a, p.b), translated);
            assertEquals(p, p.a.degree(), translated.degree());
            assertEquals(p, p.a.signum(), translated.signum());
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q, q.specialTranslate(p.b));
        }
    }

    private void compareImplementationsSpecialTranslate() {
        Map<String, Function<Pair<Polynomial, Rational>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> specialTranslate_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.specialTranslate(p.b));
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.polynomials(), P.rationals());
        compareImplementations("specialTranslate(Rational)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial positivePrimitiveTranslate_simplest(
            @NotNull Polynomial p,
            @NotNull Rational t
    ) {
        if (p == ZERO) return ZERO;
        if (p.degree() == 0) return ONE;
        return p.toRationalPolynomial().translate(t).constantFactor().b;
    }

    private void propertiesPositivePrimitiveTranslate() {
        initialize("positivePrimitiveTranslate(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.rationals()))) {
            Polynomial translated = p.a.positivePrimitiveTranslate(p.b);
            translated.validate();
            assertEquals(p, positivePrimitiveTranslate_simplest(p.a, p.b), translated);
            assertEquals(p, p.a.degree(), translated.degree());
            assertTrue(p, translated == ZERO || translated.isPrimitive());
            assertNotEquals(p, translated.signum(), -1);
        }

        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.positivePrimitivePolynomials(), P.rationals()))) {
            inverse(
                    q -> q.positivePrimitiveTranslate(p.b),
                    (Polynomial q) -> q.positivePrimitiveTranslate(p.b.negate()),
                    p.a
            );
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.rationals()))) {
            Polynomial translated = of(p.a).positivePrimitiveTranslate(p.b);
            assertTrue(p, translated == ZERO || translated == ONE);
        }

        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).irreduciblePolynomials(),
                P.rationals()
        );
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.positivePrimitiveTranslate(p.b).isIrreducible());
        }

        Iterable<Pair<Rational, List<Rational>>> qs = P.pairs(P.rationals(), P.withScale(4).bags(P.rationals()));
        for (Pair<Rational, List<Rational>> p : take(LIMIT, qs)) {
            Polynomial q = product(map(Polynomial::fromRoot, p.b));
            List<Rational> translatedRs = toList(map(r -> r.add(p.a), p.b));
            Polynomial translatedRootsP = product(map(Polynomial::fromRoot, translatedRs));
            assertEquals(p, q.positivePrimitiveTranslate(p.a), translatedRootsP);
        }
    }

    private void compareImplementationsPositivePrimitiveTranslate() {
        Map<String, Function<Pair<Polynomial, Rational>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> specialTranslate_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.specialTranslate(p.b));
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.polynomials(), P.rationals());
        compareImplementations("positivePrimitiveTranslate(Rational)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial stretch_simplest(@NotNull Polynomial p, @NotNull Rational f) {
        if (p.degree() < 1 || f == Rational.ONE) return p;
        return p.toRationalPolynomial().stretch(f).multiply(f.getNumerator().pow(p.degree())).toPolynomial();
    }

    private void propertiesStretch() {
        initialize("stretch(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.positiveRationals()))) {
            Polynomial stretched = p.a.stretch(p.b);
            stretched.validate();
            assertEquals(p, stretch_simplest(p.a, p.b), stretched);
            assertEquals(p, p.a.degree(), stretched.degree());
            assertEquals(p, p.a.signum(), stretched.signum());
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.positiveRationals()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q, q.specialTranslate(p.b));
        }
    }

    private void compareImplementationsStretch() {
        Map<String, Function<Pair<Polynomial, Rational>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> specialTranslate_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.specialTranslate(p.b));
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.polynomials(), P.rationals());
        compareImplementations("stretch(Rational)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial positivePrimitiveStretch_simplest(@NotNull Polynomial p, @NotNull Rational f) {
        if (p == ZERO) return ZERO;
        if (p.degree() == 0) return ONE;
        return p.toRationalPolynomial().stretch(f).constantFactor().b;
    }

    private void propertiesPositivePrimitiveStretch() {
        initialize("positivePrimitiveStretch(Rational)");
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.polynomials(), P.positiveRationals()))) {
            Polynomial stretched = p.a.positivePrimitiveStretch(p.b);
            stretched.validate();
            assertEquals(p, positivePrimitiveStretch_simplest(p.a, p.b), stretched);
            assertEquals(p, p.a.degree(), stretched.degree());
            assertTrue(p, stretched == ZERO || stretched.isPrimitive());
            assertNotEquals(p, stretched.signum(), -1);
        }

        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.positivePrimitivePolynomials(), P.positiveRationals());
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            inverse(
                    q -> q.positivePrimitiveStretch(p.b),
                    (Polynomial q) -> q.positivePrimitiveStretch(p.b.invert()),
                    p.a
            );
        }

        for (Pair<BigInteger, Rational> p : take(LIMIT, P.pairs(P.bigIntegers(), P.positiveRationals()))) {
            Polynomial stretched = of(p.a).positivePrimitiveStretch(p.b);
            assertTrue(p, stretched == ZERO || stretched == ONE);
        }

        ps = P.pairs(P.withScale(4).withSecondaryScale(4).irreduciblePolynomials(), P.positiveRationals());
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.positivePrimitiveStretch(p.b).isIrreducible());
        }

        Iterable<Pair<Rational, List<Rational>>> qs = P.pairs(
                P.positiveRationals(),
                P.withScale(4).bags(P.rationals())
        );
        for (Pair<Rational, List<Rational>> p : take(LIMIT, qs)) {
            Polynomial q = product(map(Polynomial::fromRoot, p.b));
            List<Rational> stretchedRs = toList(map(r -> r.multiply(p.a), p.b));
            Polynomial stretchedRootsP = product(map(Polynomial::fromRoot, stretchedRs));
            assertEquals(p, q.positivePrimitiveStretch(p.a), stretchedRootsP);
        }
    }

    private void compareImplementationsPositivePrimitiveStretch() {
        Map<String, Function<Pair<Polynomial, Rational>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> stretch_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.stretch(p.b));
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.polynomials(), P.positiveRationals());
        compareImplementations("stretch(Rational)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial shiftRootsLeft_simplest(@NotNull Polynomial p, int bits) {
        return p.stretch(Rational.ONE.shiftLeft(bits));
    }

    private void propertiesShiftRootsLeft() {
        initialize("shiftRootsLeft(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            Polynomial shifted = p.a.shiftRootsLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRootsLeft_simplest(p.a, p.b));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.signum(), shifted.signum());
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegers()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q, q.shiftRootsLeft(p.b));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.shiftRootsLeft(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsShiftRootsLeft() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRootsLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRootsLeft(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.polynomials(), P.naturalIntegersGeometric());
        compareImplementations("shiftRootsLeft(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial shiftRootsRight_simplest(@NotNull Polynomial p, int bits) {
        return p.stretch(Rational.ONE.shiftRight(bits));
    }

    private void propertiesShiftRootsRight() {
        initialize("shiftRootsRight(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            Polynomial shifted = p.a.shiftRootsRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRootsRight_simplest(p.a, p.b));
            assertEquals(p, p.a.degree(), shifted.degree());
            assertEquals(p, p.a.signum(), shifted.signum());
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegers()))) {
            Polynomial q = of(p.a);
            assertEquals(p, q, q.shiftRootsRight(p.b));
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.shiftRootsRight(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsShiftRootsRight() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRootsRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftRootsRight(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.polynomials(), P.naturalIntegersGeometric());
        compareImplementations("shiftRootsRight(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial positivePrimitiveShiftRootsLeft_simplest(@NotNull Polynomial p, int bits) {
        return p.positivePrimitiveStretch(Rational.ONE.shiftLeft(bits));
    }

    private void propertiesPositiveShiftRootsLeft() {
        initialize("positivePrimitiveShiftRootsLeft(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            Polynomial shifted = p.a.positivePrimitiveShiftRootsLeft(p.b);
            shifted.validate();
            assertEquals(p, positivePrimitiveShiftRootsLeft_simplest(p.a, p.b), shifted);
            assertEquals(p, p.a.degree(), shifted.degree());
            assertTrue(p, shifted == ZERO || shifted.isPrimitive());
            assertNotEquals(p, shifted.signum(), -1);
        }

        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(
                P.positivePrimitivePolynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            inverse(
                    q -> q.positivePrimitiveShiftRootsLeft(p.b),
                    (Polynomial q) -> q.positivePrimitiveShiftRootsRight(p.b),
                    p.a
            );
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegersGeometric()))) {
            Polynomial stretched = of(p.a).positivePrimitiveShiftRootsLeft(p.b);
            assertTrue(p, stretched == ZERO || stretched == ONE);
        }

        ps = P.pairs(P.withScale(4).withSecondaryScale(4).irreduciblePolynomials(), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.positivePrimitiveShiftRootsLeft(p.b).isIrreducible());
        }

        Iterable<Pair<Integer, List<Rational>>> qs = P.pairs(
                P.naturalIntegersGeometric(),
                P.withScale(4).bags(P.rationals())
        );
        for (Pair<Integer, List<Rational>> p : take(LIMIT, qs)) {
            Polynomial q = product(map(Polynomial::fromRoot, p.b));
            List<Rational> shiftedRs = toList(map(r -> r.shiftLeft(p.a), p.b));
            Polynomial shiftedRootsP = product(map(Polynomial::fromRoot, shiftedRs));
            assertEquals(p, q.positivePrimitiveShiftRootsLeft(p.a), shiftedRootsP);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.positivePrimitiveShiftRootsLeft(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsPositivePrimitiveShiftRootsLeft() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> positivePrimitiveShiftRootsLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.positivePrimitiveShiftRootsLeft(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.polynomials(), P.naturalIntegersGeometric());
        compareImplementations("positivePrimitiveShiftRootsLeft(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial positivePrimitiveShiftRootsRight_simplest(@NotNull Polynomial p, int bits) {
        return p.positivePrimitiveStretch(Rational.ONE.shiftRight(bits));
    }

    private void propertiesPositiveShiftRootsRight() {
        initialize("positivePrimitiveShiftRootsRight(int)");
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.naturalIntegersGeometric()))) {
            Polynomial shifted = p.a.positivePrimitiveShiftRootsRight(p.b);
            shifted.validate();
            assertEquals(p, positivePrimitiveShiftRootsRight_simplest(p.a, p.b), shifted);
            assertEquals(p, p.a.degree(), shifted.degree());
            assertTrue(p, shifted == ZERO || shifted.isPrimitive());
            assertNotEquals(p, shifted.signum(), -1);
        }

        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(
                P.positivePrimitivePolynomials(),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            inverse(
                    q -> q.positivePrimitiveShiftRootsRight(p.b),
                    (Polynomial q) -> q.positivePrimitiveShiftRootsLeft(p.b),
                    p.a
            );
        }

        for (Pair<BigInteger, Integer> p : take(LIMIT, P.pairs(P.bigIntegers(), P.naturalIntegersGeometric()))) {
            Polynomial stretched = of(p.a).positivePrimitiveShiftRootsRight(p.b);
            assertTrue(p, stretched == ZERO || stretched == ONE);
        }

        ps = P.pairs(P.withScale(4).withSecondaryScale(4).irreduciblePolynomials(), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.positivePrimitiveShiftRootsRight(p.b).isIrreducible());
        }

        Iterable<Pair<Integer, List<Rational>>> qs = P.pairs(
                P.naturalIntegersGeometric(),
                P.withScale(4).bags(P.rationals())
        );
        for (Pair<Integer, List<Rational>> p : take(LIMIT, qs)) {
            Polynomial q = product(map(Polynomial::fromRoot, p.b));
            List<Rational> shiftedRs = toList(map(r -> r.shiftRight(p.a), p.b));
            Polynomial shiftedRootsP = product(map(Polynomial::fromRoot, shiftedRs));
            assertEquals(p, q.positivePrimitiveShiftRootsRight(p.a), shiftedRootsP);
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegers()))) {
            try {
                p.a.positivePrimitiveShiftRootsRight(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsPositivePrimitiveShiftRootsRight() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> positivePrimitiveShiftRootsRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.positivePrimitiveShiftRootsRight(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.polynomials(), P.naturalIntegersGeometric());
        compareImplementations("positivePrimitiveShiftRootsRight(int)", take(LIMIT, ps), functions);
    }

    private void propertiesInvertRoots() {
        initialize("invertRoots()");
        for (Polynomial p : take(LIMIT, P.polynomials())) {
            Polynomial inverted = p.invertRoots();
            inverted.validate();
            assertTrue(p, p.degree() >= inverted.degree());
            assertNotEquals(p, inverted.signum(), -1);
        }

        Iterable<Polynomial> ps = filterInfinite(
                q -> q.signum() != -1 && !q.coefficient(0).equals(BigInteger.ZERO),
                P.polynomials()
        );
        for (Polynomial p : take(LIMIT, ps)) {
            involution(Polynomial::invertRoots, p);
        }

        for (Polynomial p : take(LIMIT, P.withScale(4).irreduciblePolynomials())) {
            assertTrue(p, p.invertRoots().isIrreducible());
        }

        for (List<Rational> rs : take(LIMIT, P.bags(P.nonzeroRationals()))) {
            Polynomial p = product(map(Polynomial::fromRoot, rs));
            List<Rational> invertedRs = toList(map(Rational::invert, rs));
            Polynomial invertedRootsP = product(map(Polynomial::fromRoot, invertedRs));
            assertEquals(rs, p.invertRoots(), invertedRootsP);
        }
    }

    private void propertiesAddRoots() {
        initialize("addRoots(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(P.withScale(4).withSecondaryScale(0).polynomials());
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial q = p.a.addRoots(p.b);
            q.validate();
            assertTrue(p, q.isPrimitive());
            assertNotEquals(p, q.signum(), -1);
            commutative(Polynomial::addRoots, p);
        }

        ps = filterInfinite(
                p -> MathUtils.gcd(p.a.degree(), p.b.degree()) == 1,
                P.pairs(P.withScale(4).withSecondaryScale(2).irreduciblePolynomialsAtLeast(1))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.addRoots(p.b).isIrreducible());
        }

        Iterable<Pair<List<Rational>, List<Rational>>> ps2 = P.pairs(P.withScale(1).bags(P.withScale(3).rationals()));
        for (Pair<List<Rational>, List<Rational>> p : take(LIMIT, ps2)) {
            Polynomial a = product(map(Polynomial::fromRoot, p.a));
            Polynomial b = product(map(Polynomial::fromRoot, p.b));
            List<Rational> sumRs = toList(map(q -> q.a.add(q.b), EP.pairsLex(p.a, p.b)));
            Polynomial sumRootsP = product(map(Polynomial::fromRoot, sumRs));
            assertEquals(p, a.addRoots(b), sumRootsP);
        }
    }

    private void propertiesMultiplyRoots() {
        initialize("multiplyRoots(Polynomial)");
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(P.withScale(4).withSecondaryScale(0).polynomials());
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            Polynomial q = p.a.multiplyRoots(p.b);
            q.validate();
            assertTrue(p, q.isPrimitive());
            assertNotEquals(p, q.signum(), -1);
            commutative(Polynomial::multiplyRoots, p);
        }

        //todo is this true in general?
        ps = filterInfinite(
                p -> MathUtils.gcd(p.a.degree(), p.b.degree()) == 1,
                P.pairs(filterInfinite(
                        p -> !p.equals(X) && p.rootCount() > 0,
                        P.withScale(4).withSecondaryScale(2).irreduciblePolynomialsAtLeast(1))
                )
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            assertTrue(p, p.a.multiplyRoots(p.b).isIrreducible());
        }

        Iterable<Pair<List<Rational>, List<Rational>>> ps2 = P.pairs(P.withScale(1).bags(P.withScale(3).rationals()));
        for (Pair<List<Rational>, List<Rational>> p : take(LIMIT, ps2)) {
            Polynomial a = product(map(Polynomial::fromRoot, p.a));
            Polynomial b = product(map(Polynomial::fromRoot, p.b));
            List<Rational> productRs = toList(map(q -> q.a.multiply(q.b), EP.pairsLex(p.a, p.b)));
            Polynomial productRootsP = product(map(Polynomial::fromRoot, productRs));
            assertEquals(p, a.multiplyRoots(b), productRootsP);
        }
    }

    private static @NotNull List<Polynomial> powerTable_simplest(@NotNull Polynomial p, int maxPower) {
        return toList(map(p::rootPower, range(0, maxPower)));
    }

    private static @NotNull List<Polynomial> powerTable_alt(@NotNull Polynomial p, int maxPower) {
        return toList(map(i -> of(BigInteger.ONE, i).remainderExact(p), range(0, maxPower)));
    }

    private void propertiesPowerTable() {
        initialize("powerTable(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.monicPolynomialsAtLeast(1),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            List<Polynomial> powers = p.a.powerTable(p.b);
            assertEquals(p, powerTable_simplest(p.a, p.b), powers);
            assertEquals(p, powerTable_alt(p.a, p.b), powers);
            assertEquals(p, powers.size(), p.b + 1);
            assertTrue(p, all(q -> q.degree() < p.a.degree(), powers));
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairsLogarithmicOrder(
                filterInfinite(p -> p.degree() < 1 || !p.isMonic(), P.polynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.powerTable(p.b);
                fail(p);
            } catch (UnsupportedOperationException ignored) {}
        }

        psFail = P.pairsLogarithmicOrder(P.monicPolynomialsAtLeast(1), P.negativeIntegers());
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.powerTable(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsPowerTable() {
        Map<String, Function<Pair<Polynomial, Integer>, List<Polynomial>>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> powerTable_simplest(p.a, p.b));
        functions.put("alt", p -> powerTable_alt(p.a, p.b));
        functions.put("standard", p -> p.a.powerTable(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.monicPolynomialsAtLeast(1),
                P.naturalIntegersGeometric()
        );
        compareImplementations("powerTable(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull Polynomial rootPower_simplest(@NotNull Polynomial p, int power) {
        return last(p.powerTable(power));
    }

    private void propertiesRootPower() {
        initialize("rootPower(int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.monicPolynomialsAtLeast(1), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Polynomial rootPower = p.a.rootPower(p.b);
            assertEquals(p, rootPower_simplest(p.a, p.b), rootPower);
            assertTrue(p, rootPower.degree() < p.a.degree());
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairsLogarithmicOrder(
                filterInfinite(p -> p.degree() < 1 || !p.isMonic(), P.polynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rootPower(p.b);
                fail(p);
            } catch (UnsupportedOperationException ignored) {}
        }

        psFail = P.pairsLogarithmicOrder(P.monicPolynomialsAtLeast(1), P.negativeIntegers());
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.rootPower(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsRootPower() {
        Map<String, Function<Pair<Polynomial, Integer>, Polynomial>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> rootPower_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.rootPower(p.b));
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.monicPolynomialsAtLeast(1),
                P.naturalIntegersGeometric()
        );
        compareImplementations("rootPower(int)", take(LIMIT, ps), functions);
    }

    private static @NotNull List<Algebraic> realRoots_simplest(@NotNull Polynomial p) {
        int rootCount = p.rootCount();
        List<Algebraic> roots = new ArrayList<>();
        for (int i = 0; i < rootCount; i++) {
            roots.add(Algebraic.of(p, i));
        }
        return roots;
    }

    private void propertiesRealRoots() {
        initialize("realRoots()");
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            List<Algebraic> realRoots = p.realRoots();
            realRoots.forEach(Algebraic::validate);
            assertEquals(p, realRoots, realRoots_simplest(p));
            assertTrue(p, increasing(realRoots));
            assertEquals(p, realRoots.size(), p.rootCount());
        }

        for (Polynomial p : take(LIMIT, P.polynomials(0))) {
            assertTrue(p, p.realRoots().isEmpty());
        }

        //todo p(x)=0
    }

    private void compareImplementationsRealRoots() {
        Map<String, Function<Polynomial, List<Algebraic>>> functions = new LinkedHashMap<>();
        functions.put("simplest", PolynomialProperties::realRoots_simplest);
        functions.put("standard", Polynomial::realRoots);
        compareImplementations("realRoots()", take(LIMIT, P.withScale(4).polynomialsAtLeast(0)), functions);
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

    private void propertiesReadStrict_String() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                POLYNOMIAL_CHARS,
                P.polynomials(),
                Polynomial::readStrict,
                Polynomial::validate,
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

        Iterable<Pair<Polynomial, Integer>> ps2 = filterInfinite(
                p -> p.a.degree() <= p.b,
                P.pairsLogarithmicOrder(P.polynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<Polynomial> op = readStrict(p.b, p.a.toString());
            Polynomial q = op.get();
            q.validate();
            assertEquals(p, q, p.a);
        }

        ps2 = filterInfinite(
                p -> p.a.degree() > p.b,
                P.pairsLogarithmicOrder(P.polynomials(), P.positiveIntegersGeometric())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps2)) {
            Optional<Polynomial> op = readStrict(p.b, p.a.toString());
            assertFalse(p, op.isPresent());
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, POLYNOMIAL_CHARS, P.polynomials(), Polynomial::readStrict);

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            homomorphic(Polynomial::of, Function.identity(), BigInteger::toString, Polynomial::toString, i);
        }
    }
}
