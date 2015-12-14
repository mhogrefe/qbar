package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
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
import static mho.qbar.objects.Rational.sum;
import static mho.qbar.testing.QBarTesting.QEP;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_CHARS = "-/0123456789";
    private static final BigInteger ASCII_ALPHANUMERIC_COUNT = BigInteger.valueOf(36);

    public RationalDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoGetNumerator() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("getNumerator(" + r + ") = " + r.getNumerator());
        }
    }

    private void demoGetDenominator() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("getDenominator(" + r + ") = " + r.getDenominator());
        }
    }

    private void demoOf_BigInteger_BigInteger() {
        initialize();
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers(), P.nonzeroBigIntegers()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoOf_long_long() {
        initialize();
        for (Pair<Long, Long> p : take(LIMIT, P.pairs(P.longs(), P.nonzeroLongs()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoOf_int_int() {
        initialize();
        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.integers(), P.nonzeroIntegers()))) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoOf_BigInteger() {
        initialize();
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_long() {
        initialize();
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("of(" + l + ") = " + of(l));
        }
    }

    private void demoOf_int() {
        initialize();
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_BinaryFraction() {
        initialize();
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            System.out.println("of(" + bf + ") = " + of(bf));
        }
    }

    private void demoOf_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + of(f));
        }
    }

    private void demoOf_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + of(d));
        }
    }

    private void demoOfExact_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("ofExact(" + f + ") = " + ofExact(f));
        }
    }

    private void demoOfExact_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("ofExact(" + d + ") = " + ofExact(d));
        }
    }

    private void demoOf_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("of(" + bd + ") = " + of(bd));
        }
    }

    private void demoIsInteger() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isInteger() ? "" : "not ") + "an integer");
        }
    }

    private void demoBigIntegerValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("bigIntegerValue(" + p.a + ", " + p.b + ") = " + p.a.bigIntegerValue(p.b));
        }
    }

    private void demoBigIntegerValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("bigIntegerValue(" + r + ") = " + r.bigIntegerValue());
        }
    }

    private void demoFloor() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("floor(" + r + ") = " + r.floor());
        }
    }

    private void demoCeiling() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("ceil(" + r + ") = " + r.ceiling());
        }
    }

    private void demoBigIntegerValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.bigIntegers()))) {
            System.out.println("bigIntegerValueExact(" + r + ") = " + r.bigIntegerValueExact());
        }
    }

    private void demoByteValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.bytes()))) {
            System.out.println("byteValueExact(" + r + ") = " + r.byteValueExact());
        }
    }

    private void demoShortValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.shorts()))) {
            System.out.println("shortValueExact(" + r + ") = " + r.shortValueExact());
        }
    }

    private void demoIntValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.integers()))) {
            System.out.println("intValueExact(" + r + ") = " + r.intValueExact());
        }
    }

    private void demoLongValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.longs()))) {
            System.out.println("longValueExact(" + r + ") = " + r.longValueExact());
        }
    }

    private void demoIsPowerOfTwo() {
        initialize();
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            System.out.println(r + " is " + (r.isPowerOfTwo() ? "a" : "not a") + " power of 2");
        }
    }

    private void demoIsBinaryFraction() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isBinaryFraction() ? "a" : "not a") + " binary fraction");
        }
    }

    private void demoBinaryFractionValueExact() {
        initialize();
        for (Rational r : take(LIMIT, filterInfinite(Rational::isBinaryFraction, P.rationals()))) {
            System.out.println("binaryFractionValueExact(" + r + ") = " + r.binaryFractionValueExact());
        }
    }

    private void demoBinaryExponent() {
        initialize();
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            System.out.println("binaryExponent(" + r + ") = " + r.binaryExponent());
        }
    }

    private void demoIsEqualToFloat() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isEqualToFloat() ? "" : "not ") + "equal to a float");
        }
    }

    private void demoIsEqualToDouble() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isEqualToDouble() ? "" : "not ") + "equal to a double");
        }
    }

    private void demoFloatValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToFloat(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("floatValue(" + p.a + ", " + p.b + ") = " + p.a.floatValue(p.b));
        }
    }

    private void demoFloatValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("floatValue(" + r + ") = " + r.floatValue());
        }
    }

    private void demoFloatValueExact() {
        initialize();
        Iterable<Rational> rs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("floatValueExact(" + r + ") = " + r.floatValueExact());
        }
    }

    private void demoDoubleValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToDouble(),
                P.pairs(P.rationals(), P.roundingModes()));
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("doubleValue(" + p.a + ", " + p.b + ") = " + p.a.doubleValue(p.b));
        }
    }

    private void demoDoubleValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("doubleValue(" + r + ") = " + r.doubleValue());
        }
    }

    private void demoDoubleValueExact() {
        initialize();
        Iterable<Rational> rs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !FloatingPointUtils.isNegativeZero(d), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("doubleValueExact(" + r + ") = " + r.doubleValueExact());
        }
    }

    private void demoHasTerminatingBaseExpansion() {
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

    private void demoBigDecimalValueByPrecision_int_RoundingMode() {
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

    private void demoBigDecimalValueByScale_int_RoundingMode() {
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

    private void demoBigDecimalValueByPrecision_int() {
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

    private void demoBigDecimalValueByScale_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairsSquareRootOrder(P.rationals(), P.integersGeometric()))) {
            System.out.println("bigDecimalValueByScale(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByScale(p.b));
        }
    }

    private void demoBigDecimalValueExact() {
        initialize();
        Iterable<Rational> rs = filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("bigDecimalValueExact(" + r + ") = " + r.bigDecimalValueExact());
        }
    }

    private void demoBitLength() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("bitLength(" + r + ") = " + r.bitLength());
        }
    }

    private void demoAdd() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("-(" + r + ") = " + r.negate());
        }
    }

    private void demoAbs() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("|" + r + "| = " + r.abs());
        }
    }

    private void demoSignum() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("signum(" + r + ") = " + r.signum());
        }
    }

    private void demoSubtract() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoInvert() {
        initialize();
        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            System.out.println("1/(" + r + ") = " + r.invert());
        }
    }

    private void demoDivide_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        initialize();
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(rs));
        }
    }

    private void demoProduct() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Π(" + listString + ") = " + product(rs));
        }
    }

    private void demoDelta_finite() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).listsAtLeast(1, P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(rs)));
        }
    }

    private void demoDelta_infinite() {
        initialize();
        for (Iterable<Rational> rs : take(MEDIUM_LIMIT, P.prefixPermutations(QEP.rationals()))) {
            String listString = tail(init(its(rs)));
            System.out.println("Δ(" + listString + ") = " + its(delta(rs)));
        }
    }

    private void demoHarmonicNumber() {
        initialize();
        for (int i : take(MEDIUM_LIMIT, P.positiveIntegersGeometric())) {
            System.out.println("H_" + i + " = " + harmonicNumber(i));
        }
    }

    private void demoPow() {
        initialize();
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> p.a != ZERO || p.b >= 0,
                P.pairs(P.rationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoFractionalPart() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("fractionalPart(" + r + ") = " + r.fractionalPart());
        }
    }

    private void demoRoundToDenominator() {
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

    private void demoContinuedFraction() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("continuedFraction(" + r + ") = " + toList(r.continuedFraction()));
        }
    }

    private void demoFromContinuedFraction() {
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

    private void demoConvergents() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("convergents(" + r + ") = " + toList(r.convergents()));
        }
    }

    private void demoPositionalNotation() {
        initialize();
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.withElement(ZERO, P.withScale(4).positiveRationals()),
                P.rangeUp(IntegerUtils.TWO)
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("positionalNotation(" + p.a + ", " + p.b + ") = " + p.a.positionalNotation(p.b));
        }
    }

    private void demoFromPositionalNotation() {
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

    private void demoDigits() {
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

    private void demoToStringBase_BigInteger() {
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

    private void demoToStringBase_BigInteger_int() {
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

    private void demoFromStringBase() {
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

    private void demoCancelDenominators() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.withScale(4).lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("cancelDenominators(" + listString + ") = " + cancelDenominators(rs));
        }
    }

    private void demoEquals_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            //noinspection ObjectEqualsNull
            System.out.println(r + (r.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("hashCode(" + r + ") = " + r.hashCode());
        }
    }

    private void demoCompareTo() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        initialize();
        for (String s : take(LIMIT, P.strings(RATIONAL_CHARS))) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        initialize();
        for (String s : take(LIMIT, P.strings(RATIONAL_CHARS))) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r);
        }
    }
}
