package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.function.Function;

import static mho.qbar.objects.Algebraic.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class AlgebraicProperties extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_CHARS = " ()*+-/0123456789^foqrstx";

    public AlgebraicProperties() {
        super("Algebraic");
    }

    @Override
    protected void testBothModes() {
        propertiesOf_Polynomial_int();
        propertiesOf_Rational();
        propertiesOf_BigInteger();
        propertiesOf_int();
        propertiesIsInteger();
        propertiesBigIntegerValue_RoundingMode();
        propertiesBigIntegerValue();
        propertiesFloor();
        propertiesCeiling();
        propertiesBigIntegerValueExact();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesRead();
        propertiesFindIn();
        propertiesToString();
    }

    private void propertiesOf_Polynomial_int() {
        initialize("of(Polynomial, int)");
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).polynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            Algebraic x = of(p.a, p.b);
            x.validate();
            assertTrue(p, x.degree() <= p.a.degree());
            assertTrue(p, x.rootIndex() <= p.b);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x, of(x.minimalPolynomial(), x.rootIndex()));
        }

        for (int i : take(LIMIT, P.naturalIntegersGeometric())) {
            try {
                of(Polynomial.ZERO, i);
                fail(i);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(), P.negativeIntegersGeometric()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.polynomials(0), P.naturalIntegersGeometric()))) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Pair<Polynomial, Integer>> psFail = P.pairs(
                filterInfinite(p -> p.rootCount() == 0, P.withScale(4).squareFreePolynomials()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                of(p.a, p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            Algebraic x = of(r);
            x.validate();
            assertTrue(r, x.isRational());
        }
    }

    private void propertiesOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Algebraic x = of(i);
            x.validate();
            assertTrue(i, x.isInteger());
        }
    }

    private void propertiesOf_int() {
        Algebraic lowerLimit = of(Integer.MIN_VALUE);
        Algebraic upperLimit = of(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Algebraic x = of(i);
            x.validate();
            assertTrue(i, x.isInteger());
            assertTrue(i, ge(x, lowerLimit));
            assertTrue(i, le(x, upperLimit));
        }
    }

    private void propertiesIsInteger() {
        initialize("isInteger()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x.isInteger(), of(x.floor()).equals(x));
            homomorphic(Algebraic::negate, Function.identity(), Algebraic::isInteger, Algebraic::isInteger, x);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i, of(i).isInteger());
        }
    }

    private void propertiesBigIntegerValue_RoundingMode() {
        initialize("bigIntegerValue(RoundingMode)");
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(LIMIT, ps)) {
            BigInteger rounded = p.a.bigIntegerValue(p.b);
            assertTrue(p, rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
            //todo assertTrue(p, lt(p.a.subtract(of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).bigIntegerValue(RoundingMode.UNNECESSARY), i);
        }

        for (Algebraic x : take(LIMIT, P.algebraics())) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.FLOOR), x.floor());
            assertEquals(x, x.bigIntegerValue(RoundingMode.CEILING), x.ceiling());
            //todo
//            assertTrue(x, le(of(x.bigIntegerValue(RoundingMode.DOWN)).abs(), x.abs()));
//            assertTrue(x, ge(of(x.bigIntegerValue(RoundingMode.UP)).abs(), x.abs()));
//            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_DOWN))).abs(), ONE_HALF));
//            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_UP))).abs(), ONE_HALF));
//            assertTrue(x, le(x.subtract(of(x.bigIntegerValue(RoundingMode.HALF_EVEN))).abs(), ONE_HALF));
        }

//        for (Algebraic x : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.algebraics()))) {
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.DOWN));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.DOWN));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.DOWN));
//        }
//
//        for (Rational r : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.UP));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.UP));
//            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.UP));
//        }

        //odd multiples of 1/2
        Iterable<Algebraic> xs = map(
                i -> of(Rational.of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO)),
                P.bigIntegers()
        );
        for (Algebraic x : take(LIMIT, xs)) {
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_DOWN), x.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(x, x.bigIntegerValue(RoundingMode.HALF_UP), x.bigIntegerValue(RoundingMode.UP));
            assertFalse(x, x.bigIntegerValue(RoundingMode.HALF_EVEN).testBit(0));
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
            try {
                x.bigIntegerValue(RoundingMode.UNNECESSARY);
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigIntegerValue() {
        initialize("bigIntegerValue()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            BigInteger rounded = x.bigIntegerValue();
            assertTrue(x, rounded.equals(BigInteger.ZERO) || rounded.signum() == x.signum());
            //todo assertTrue(x, le(x.subtract(of(x.bigIntegerValue())).abs(), ONE_HALF));
        }

//        for (Rational r : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
//            assertEquals(r, r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.DOWN));
//        }
//
//        for (Rational r : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
//            assertEquals(r, r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.UP));
//        }

        //odd multiples of 1/2
        Iterable<Algebraic> xs = map(
                i -> of(Rational.of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO)),
                P.bigIntegers()
        );
        for (Algebraic x : take(LIMIT, xs)) {
            assertFalse(x, x.bigIntegerValue().testBit(0));
        }
    }

    private void propertiesFloor() {
        initialize("floor()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            BigInteger floor = x.floor();
            assertTrue(x, le(of(floor), x));
            //todo assertTrue(x, le(x.subtract(of(floor)), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).floor(), i);
        }
    }

    private void propertiesCeiling() {
        initialize("ceiling()");
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            BigInteger ceiling = x.ceiling();
            assertTrue(x, ge(of(ceiling), x));
            //todo assertTrue(x, le(of(ceiling).subtract(x), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).ceiling(), i);
        }
    }

    private void propertiesBigIntegerValueExact() {
        initialize("bigIntegerValueExact()");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Algebraic x = of(i);
            assertEquals(i, x.bigIntegerValueExact(), i);
            inverse(Algebraic::bigIntegerValueExact, Algebraic::of, x);
            homomorphic(
                    Algebraic::negate,
                    BigInteger::negate,
                    Algebraic::bigIntegerValueExact,
                    Algebraic::bigIntegerValueExact,
                    x
            );
        }

        for (Algebraic x : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.algebraics()))) {
            try {
                x.bigIntegerValueExact();
                fail(x);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::algebraics);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::algebraics);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Algebraic)");
        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::algebraics);
    }

    private void propertiesRead() {
        initialize("read(String)");
        QBarTesting.propertiesReadHelper(
                LIMIT,
                P,
                ALGEBRAIC_CHARS,
                P.algebraics(),
                Algebraic::read,
                Algebraic::validate,
                false
        );
    }

    private void propertiesFindIn() {
        initialize("findIn(String)");
        propertiesFindInHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.algebraics(),
                Algebraic::read,
                Algebraic::findIn,
                Algebraic::validate
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, ALGEBRAIC_CHARS, P.algebraics(), Algebraic::read);
    }
}
