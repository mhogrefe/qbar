package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mho.qbar.objects.Rational.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalDemos {
    private static final boolean USE_RANDOM = false;
    private static final @NotNull QBarExhaustiveProvider EP = QBarExhaustiveProvider.INSTANCE;
    private static final @NotNull String RATIONAL_CHARS = "-/0123456789";
    private static int LIMIT;
    private static final int DENOMINATOR_CUTOFF = 1000000;
    private static final int SMALLER_LIMIT = 500;
    private static final int SMALL_LIMIT = 1000;
    private static final int MEDIUM_LIMIT = 3000;
    private static final BigInteger ASCII_ALPHANUMERIC_COUNT = BigInteger.valueOf(36);
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

    private static void demoGetNumerator() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("getNumerator(" + r + ") = " + r.getNumerator());
        }
    }

    private static void demoGetDenominator() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("getDenominator(" + r + ") = " + r.getDenominator());
        }
    }

    private static void demoOf_BigInteger_BigInteger() {
        initialize();
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers(), P.nonzeroBigIntegers()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private static void demoOf_long_long() {
        initialize();
        for (Pair<Long, Long> p : take(LIMIT, P.pairs(P.longs(), P.nonzeroLongs()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private static void demoOf_int_int() {
        initialize();
        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.integers(), P.nonzeroIntegers()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private static void demoOf_BigInteger() {
        initialize();
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private static void demoOf_long() {
        initialize();
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("of(" + l + ") = " + of(l));
        }
    }

    private static void demoOf_int() {
        initialize();
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private static void demoOf_BinaryFraction() {
        initialize();
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            System.out.println("of(" + bf + ") = " + of(bf));
        }
    }

    private static void demoOf_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + of(f));
        }
    }

    private static void demoOf_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + of(d));
        }
    }

    private static void demoOfExact_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("ofExact(" + f + ") = " + ofExact(f));
        }
    }

    private static void demoOfExact_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("ofExact(" + d + ") = " + ofExact(d));
        }
    }

    private static void demoOf_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("of(" + bd + ") = " + of(bd));
        }
    }

    private static void demoIsInteger() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isInteger() ? "" : "not ") + "an integer");
        }
    }

    private static void demoBigIntegerValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("bigIntegerValue(" + p.a + ", " + p.b + ") = " + p.a.bigIntegerValue(p.b));
        }
    }

    private static void demoBigIntegerValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("bigIntegerValue(" + r + ") = " + r.bigIntegerValue());
        }
    }

    private static void demoFloor() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("floor(" + r + ") = " + r.floor());
        }
    }

    private static void demoCeiling() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("ceil(" + r + ") = " + r.ceiling());
        }
    }

    private static void demoBigIntegerValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.bigIntegers()))) {
            System.out.println("bigIntegerValueExact(" + r + ") = " + r.bigIntegerValueExact());
        }
    }

    private static void demoByteValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.bytes()))) {
            System.out.println("byteValueExact(" + r + ") = " + r.byteValueExact());
        }
    }

    private static void demoShortValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.shorts()))) {
            System.out.println("shortValueExact(" + r + ") = " + r.shortValueExact());
        }
    }

    private static void demoIntValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.integers()))) {
            System.out.println("intValueExact(" + r + ") = " + r.intValueExact());
        }
    }

    private static void demoLongValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.longs()))) {
            System.out.println("longValueExact(" + r + ") = " + r.longValueExact());
        }
    }

    private static void demoIsPowerOfTwo() {
        initialize();
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            System.out.println(r + " is " + (r.isPowerOfTwo() ? "a" : "not a") + " power of 2");
        }
    }

    private static void demoIsBinaryFraction() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isBinaryFraction() ? "a" : "not a") + " binary fraction");
        }
    }

    private static void demoBinaryFractionValueExact() {
        initialize();
        for (Rational r : take(LIMIT, filterInfinite(Rational::isBinaryFraction, P.rationals()))) {
            System.out.println("binaryFractionValueExact(" + r + ") = " + r.binaryFractionValueExact());
        }
    }

    private static void demoBinaryExponent() {
        initialize();
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            System.out.println("binaryExponent(" + r + ") = " + r.binaryExponent());
        }
    }

    private static void demoIsEqualToFloat() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isEqualToFloat() ? "" : "not ") + "equal to a float");
        }
    }

    private static void demoIsEqualToDouble() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isEqualToDouble() ? "" : "not ") + "equal to a double");
        }
    }

    private static void demoFloatValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToFloat(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("floatValue(" + p.a + ", " + p.b + ") = " + p.a.floatValue(p.b));
        }
    }

    private static void demoFloatValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("floatValue(" + r + ") = " + r.floatValue());
        }
    }

    private static void demoFloatValueExact() {
        initialize();
        Iterable<Rational> rs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("floatValueExact(" + r + ") = " + r.floatValueExact());
        }
    }

    private static void demoDoubleValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToDouble(),
                P.pairs(P.rationals(), P.roundingModes()));
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("doubleValue(" + p.a + ", " + p.b + ") = " + p.a.doubleValue(p.b));
        }
    }

    private static void demoDoubleValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("doubleValue(" + r + ") = " + r.doubleValue());
        }
    }

    private static void demoDoubleValueExact() {
        initialize();
        Iterable<Rational> rs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !FloatingPointUtils.isNegativeZero(d), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("doubleValueExact(" + r + ") = " + r.doubleValueExact());
        }
    }

    private static void demoHasTerminatingBaseExpansion() {
        initialize();
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.rationals(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.hasTerminatingBaseExpansion(p.b) ? " has " : " doesn't have ") +
                    "a terminating base-" + p.b + " expansion");
        }
    }

    private static void demoBigDecimalValueByPrecision_int_RoundingMode() {
        initialize();
        Iterable<Triple<Rational, Integer, RoundingMode>> ts = filterInfinite(
                t -> {
                    try {
                        t.a.bigDecimalValueByPrecision(t.b, t.c);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.triples(P.rationals(), P.naturalIntegersGeometric(), P.roundingModes())
        );
        for (Triple<Rational, Integer, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByPrecision(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByPrecision(t.b, t.c));
        }
    }

    private static void demoBigDecimalValueByScale_int_RoundingMode() {
        initialize();
        Iterable<Triple<Rational, Integer, RoundingMode>> ts = filterInfinite(
                t -> t.c != RoundingMode.UNNECESSARY || t.a.multiply(TEN.pow(t.b)).isInteger(),
                P.triples(P.rationals(), P.integersGeometric(), P.roundingModes())
        );
        for (Triple<Rational, Integer, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByScale(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByScale(t.b, t.c));
        }
    }

    private static void demoBigDecimalValueByPrecision_int() {
        initialize();
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> {
                    try {
                        p.a.bigDecimalValueByPrecision(p.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.pairsSquareRootOrder(P.rationals(), P.naturalIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            System.out.println("bigDecimalValueByPrecision(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByPrecision(p.b));
        }
    }

    private static void demoBigDecimalValueByScale_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairsSquareRootOrder(P.rationals(), P.integersGeometric()))) {
            System.out.println("bigDecimalValueByScale(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByScale(p.b));
        }
    }

    private static void demoBigDecimalValueExact() {
        initialize();
        Iterable<Rational> rs = filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("bigDecimalValueExact(" + r + ") = " + r.bigDecimalValueExact());
        }
    }

    private static void demoBitLength() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("bitLength(" + r + ") = " + r.bitLength());
        }
    }

    private static void demoAdd() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private static void demoNegate() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("-(" + r + ") = " + r.negate());
        }
    }

    private static void demoAbs() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("|" + r + "| = " + r.abs());
        }
    }

    private static void demoSignum() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("signum(" + r + ") = " + r.signum());
        }
    }

    private static void demoSubtract() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private static void demoMultiply_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoMultiply_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private static void demoInvert() {
        initialize();
        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            System.out.println("1/(" + r + ") = " + r.invert());
        }
    }

    private static void demoDivide_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoDivide_BigInteger() {
        initialize();
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoDivide_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private static void demoShiftLeft() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private static void demoShiftRight() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private static void demoSum() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(rs));
        }
    }

    private static void demoProduct() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Π(" + listString + ") = " + product(rs));
        }
    }

    private static void demoDelta_finite() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).listsAtLeast(1, P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(rs)));
        }
    }

    private static void demoDelta_infinite() {
        initialize();
        for (Iterable<Rational> rs : take(SMALL_LIMIT, P.prefixPermutations(EP.rationals()))) {
            String listString = tail(init(its(rs)));
            System.out.println("Δ(" + listString + ") = " + its(delta(rs)));
        }
    }

    private static void demoHarmonicNumber() {
        initialize();
        for (int i : take(SMALL_LIMIT, P.positiveIntegersGeometric())) {
            System.out.println("H_" + i + " = " + harmonicNumber(i));
        }
    }

    private static void demoPow() {
        initialize();
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> p.a != ZERO || p.b >= 0,
                P.pairs(P.rationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private static void demoFractionalPart() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("fractionalPart(" + r + ") = " + r.fractionalPart());
        }
    }

    private static void demoRoundToDenominator() {
        initialize();
        Iterable<Triple<Rational, BigInteger, RoundingMode>> ts = filterInfinite(
                p -> p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.triples(P.rationals(), P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("roundToDenominator(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.roundToDenominator(t.b, t.c));
        }
    }

    private static void demoContinuedFraction() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("continuedFraction(" + r + ") = " + toList(r.continuedFraction()));
        }
    }

    private static void demoFromContinuedFraction() {
        initialize();
        Iterable<List<BigInteger>> iss = map(
                p -> toList(cons(p.a, p.b)),
                P.pairs(P.bigIntegers(), P.withScale(4).lists(P.positiveBigIntegers()))
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            String listString = tail(init(is.toString()));
            System.out.println("fromContinuedFraction(" + listString + ") = " + fromContinuedFraction(is));
        }
    }

    private static void demoConvergents() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("convergents(" + r + ") = " + toList(r.convergents()));
        }
    }

    private static void demoPositionalNotation() {
        initialize();
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.withElement(ZERO, P.withScale(4).positiveRationals()),
                P.rangeUp(IntegerUtils.TWO)
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("positionalNotation(" + p.a + ", " + p.b + ") = " + p.a.positionalNotation(p.b));
        }
    }

    private static void demoFromPositionalNotation() {
        initialize();
        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> ps =
                P.dependentPairsInfinite(
                        P.withScale(8).rangeUp(IntegerUtils.TWO),
                        b -> {
                            Iterable<BigInteger> range = P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE));
                            return P.triples(
                                    P.withScale(4).lists(range),
                                    P.withScale(4).lists(range),
                                    P.withScale(4).listsAtLeast(1, range)
                            );
                        }
                );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps)) {
            System.out.println("fromPositionalNotation(" + p.a + ", " + p.b.a + ", " + p.b.b + ", " + p.b.c + ") = " +
                    fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c));
        }
    }

    private static void demoDigits() {
        initialize();
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = P.pairsSquareRootOrder(
                P.withElement(ZERO, P.positiveRationals()),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            System.out.println("digits(" + p.a + ", " + p.b + ") = " + new Pair<>(digits.a.toString(), its(digits.b)));
        }
    }

    private static void demoToStringBase_BigInteger() {
        initialize();
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = filterInfinite(
                q -> q.a.hasTerminatingBaseExpansion(q.b),
                P.pairsSquareRootOrder(
                        P.rationals(),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("toStringBase(" + p.a + ", " + p.b + ") = " + p.a.toStringBase(p.b));
        }
    }

    private static void demoToStringBase_BigInteger_int() {
        initialize();
        //noinspection Convert2MethodRef
        Iterable<Triple<Rational, BigInteger, Integer>> ts = P.triples(
                P.rationals(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.integersGeometric()
        );
        for (Triple<Rational, BigInteger, Integer> t : take(LIMIT, ts)) {
            System.out.println("toStringBase(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.toStringBase(t.b, t.c));
        }
    }

    private static void demoFromStringBase() {
        initialize();
        Map<Integer, String> baseChars = new HashMap<>();
        baseChars.put(0, ".-()0123456789");
        String chars = ".-0";
        for (int i = 1; i < ASCII_ALPHANUMERIC_COUNT.intValueExact(); i++) {
            chars += IntegerUtils.toDigit(i);
            baseChars.put(i + 1, chars);
        }
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, String>> ps = P.dependentPairsInfiniteSquareRootOrder(
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                b -> filterInfinite(
                        s -> {
                            try {
                                fromStringBase(s, b);
                                return true;
                            } catch (IllegalArgumentException e) {
                                return false;
                            }
                        },
                        P.strings(baseChars.get(Ordering.le(b, ASCII_ALPHANUMERIC_COUNT) ? b.intValueExact() : 0))
                )
        );
        for (Pair<BigInteger, String> p : take(LIMIT, ps)) {
            System.out.println("fromStringBase(" + p.a + ", " + p.b + ") = " + fromStringBase(p.b, p.a));
        }
    }

    private static void demoCancelDenominators() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("cancelDenominators(" + listString + ") = " + cancelDenominators(rs));
        }
    }

    private static void demoEquals_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private static void demoEquals_null() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            //noinspection ObjectEqualsNull
            System.out.println(r + (r.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private static void demoHashCode() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("hashCode(" + r + ") = " + r.hashCode());
        }
    }

    private static void demoCompareTo() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
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
        for (String s : take(LIMIT, P.strings(RATIONAL_CHARS))) {
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
        for (String s : take(LIMIT, P.strings(RATIONAL_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private static void demoToString() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r);
        }
    }
}
