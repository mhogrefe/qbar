package mho.qbar.objects;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.qbar.objects.Rational.*;
import static mho.wheels.misc.Readers.findBigDecimalIn;

public class RationalDemos {
    private static final boolean USE_RANDOM = false;
    private static final String RATIONAL_CHARS = "-/0123456789";
    private static final int SMALL_LIMIT = 1000;
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    public static void demoGetNumerator() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("getNumerator(" + r + ") = " + r.getNumerator());
        }
    }

    public static void demoGetDenominator() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("getDenominator(" + r + ") = " + r.getDenominator());
        }
    }

    public static void demoOf_BigInteger_BigInteger() {
        initialize();
        Iterable<Pair<BigInteger, BigInteger>> ps = filter(
                p -> {
                    assert p.b != null;
                    return !p.b.equals(BigInteger.ZERO);
                },
                P.pairs(P.bigIntegers())
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    public static void demoOf_long_long() {
        initialize();
        Iterable<Pair<Long, Long>> ps = filter(p -> p.b != 0, P.pairs(P.longs()));
        for (Pair<Long, Long> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    public static void demoOf_int_int() {
        initialize();
        Iterable<Pair<Integer, Integer>> ps = filter(p -> p.b != 0, P.pairs(P.integers()));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    public static void demoOf_BigInteger() {
        initialize();
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    public static void demoOf_long() {
        initialize();
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("of(" + l + ") = " + of(l));
        }
    }

    public static void demoOf_int() {
        initialize();
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    public static void demoOf_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + of(f));
        }
    }

    public static void demoOf_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + of(d));
        }
    }

    public static void demoOfExact_float() {
        initialize();
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("ofExact(" + f + ") = " + ofExact(f));
        }
    }

    public static void demoOfExact_double() {
        initialize();
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("ofExact(" + d + ") = " + ofExact(d));
        }
    }

    public static void demoOf_BigDecimal() {
        initialize();
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("of(" + bd + ") = " + of(bd));
        }
    }

    public static void demoIsInteger() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + " is " + (r.isInteger() ? "" : "not ") + "an integer");
        }
    }

    public static void demoBigIntegerValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.isInteger();
                },
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("bigIntegerValue(" + p.a + ", " + p.b + ") = " + p.a.bigIntegerValue(p.b));
        }
    }

    public static void demoBigIntegerValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("bigIntegerValue(" + r + ") = " + r.bigIntegerValue());
        }
    }

    public static void demoBigIntegerValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.bigIntegers()))) {
            System.out.println("bigIntegerValueExact(" + r + ") = " + r.bigIntegerValueExact());
        }
    }

    public static void demoByteValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.bytes()))) {
            System.out.println("byteValueExact(" + r + ") = " + r.byteValueExact());
        }
    }

    public static void demoShortValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.shorts()))) {
            System.out.println("shortValueExact(" + r + ") = " + r.shortValueExact());
        }
    }

    public static void demoIntValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.integers()))) {
            System.out.println("intValueExact(" + r + ") = " + r.intValueExact());
        }
    }

    public static void demoLongValueExact() {
        initialize();
        for (Rational r : take(LIMIT, map(Rational::of, P.longs()))) {
            System.out.println("longValueExact(" + r + ") = " + r.longValueExact());
        }
    }

    public static void demoHasTerminatingDecimalExpansion() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r + (r.hasTerminatingDecimalExpansion() ? " has " : " does not have ") +
                    "a terminating decimal expansion");
        }
    }

    public static void demoBigDecimalValue_int_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, Pair<Integer, RoundingMode>>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<Integer, RoundingMode>>) P.pairs(P.naturalIntegers(), P.roundingModes())
            );
        } else {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<Integer, RoundingMode>>) P.pairs(
                            ((RandomProvider) P).naturalIntegersGeometric(20),
                            P.roundingModes()
                    )
            );
        }
        ps = filter(
                p -> {
                    try {
                        assert p.a != null;
                        assert p.b != null;
                        assert p.b.a != null;
                        assert p.b.b != null;
                        p.a.bigDecimalValue(p.b.a, p.b.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                ps
        );
        for (Pair<Rational, Pair<Integer, RoundingMode>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            System.out.println("bigDecimalValue(" + p.a + ", " + p.b.a + ", " + p.b.b + ") = " +
                    p.a.bigDecimalValue(p.b.a, p.b.b));
        }
    }

    public static void demoBigDecimalValue_int() {
        initialize();
        Iterable<Pair<Rational, Integer>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = ((ExhaustiveProvider) P).pairsSquareRootOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        ps = filter(
                p -> {
                    try {
                        assert p.a != null;
                        assert p.b != null;
                        p.a.bigDecimalValue(p.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                ps
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("bigDecimalValue(" + p.a + ", " + p.b + ") = " + p.a.bigDecimalValue(p.b));
        }
    }

    public static void demoBigDecimalValueExact() {
        initialize();
        Iterable<Rational> rs = filter(Rational::hasTerminatingDecimalExpansion, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("bigDecimalValueExact(" + r + ") = " + r.bigDecimalValueExact());
        }
    }

    public static void demoBinaryExponent() {
        initialize();
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            System.out.println("binaryExponent(" + r + ") = " + r.binaryExponent());
        }
    }

    public static void demoFloatValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || ofExact(p.a.floatValue(RoundingMode.FLOOR)).equals(p.a);
                },
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("floatValue(" + p.a + ", " + p.b + ") = " + p.a.floatValue(p.b));
        }
    }

    public static void demoFloatValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("floatValue(" + r + ") = " + r.floatValue());
        }
    }

    public static void demoFloatValueExact() {
        initialize();
        Iterable<Rational> rs = map(
                Rational::ofExact,
                filter(f -> !Float.isNaN(f) && Float.isFinite(f) && !f.equals(-0.0f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("floatValueExact(" + r + ") = " + r.floatValueExact());
        }
    }

    public static void demoDoubleValue_RoundingMode() {
        initialize();
        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || ofExact(p.a.floatValue(RoundingMode.FLOOR)).equals(p.a);
                },
                P.pairs(P.rationals(), P.roundingModes()));
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("doubleValue(" + p.a + ", " + p.b + ") = " + p.a.doubleValue(p.b));
        }
    }

    public static void demoDoubleValue() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("doubleValue(" + r + ") = " + r.doubleValue());
        }
    }

    public static void demoDoubleValueExact() {
        initialize();
        Iterable<Rational> rs = map(
                Rational::ofExact,
                filter(d -> !Double.isNaN(d) && Double.isFinite(d) && !d.equals(-0.0), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            System.out.println("doubleValueExact(" + r + ") = " + r.doubleValueExact());
        }
    }

    public static void demoNegate() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("-(" + r + ") = " + r.negate());
        }
    }

    public static void demoInvert() {
        initialize();
        for (Rational r : take(LIMIT, filter(s -> s != ZERO, P.rationals()))) {
            System.out.println("1/(" + r + ") = " + r.invert());
        }
    }

    public static void demoAbs() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("|" + r + "| = " + r.abs());
        }
    }

    public static void demoSignum() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("sgn(" + r + ") = " + r.signum());
        }
    }

    public static void demoAdd() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " + " + p.b + " = " + p.a.add(p.b));
        }
    }

    public static void demoSubtract() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    public static void demoMultiply_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_BigInteger() {
        initialize();
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoMultiply_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void demoDivide_Rational() {
        initialize();
        Iterable<Pair<Rational, Rational>> ps = filter(p -> p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoDivide_BigInteger() {
        initialize();
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.rationals(),
                filter(bi -> !bi.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoDivide_int() {
        initialize();
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), filter(i -> i != 0, P.integers())))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void demoSum() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(rs));
        }
    }

    public static void demoProduct() {
        initialize();
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Π(" + listString + ") = " + product(rs));
        }
    }

    public static void demoDelta() {
        initialize();
        for (List<Rational> rs : take(LIMIT, filter(xs -> !xs.isEmpty(), P.lists(P.rationals())))) {
            String listString = tail(init(rs.toString()));
            System.out.println("Δ(" + listString + ") = " + IterableUtils.toString(20, delta(rs)));
        }
    }

    public static void demoHarmonicNumber() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof ExhaustiveProvider) {
            is = P.positiveIntegers();
        } else {
            is = ((RandomProvider) P).positiveIntegersGeometric(100);
        }
        for (int i : take(SMALL_LIMIT, is)) {
            System.out.println("H_" + i + " = " + harmonicNumber(i));
        }
    }

    public static void demoPow() {
        initialize();
        Iterable<Integer> exps;
        if (P instanceof ExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((RandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<Rational, Integer>> ps = filter(p -> p.b >= 0 || p.a != ZERO, P.pairs(P.rationals(), exps));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    public static void demoFloor() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("floor(" + r + ") = " + r.floor());
        }
    }

    public static void demoCeiling() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("ceil(" + r + ") = " + r.ceiling());
        }
    }

    public static void demoFractionalPart() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("fractionalPart(" + r + ") = " + r.fractionalPart());
        }
    }

    public static void demoRoundToDenominator() {
        initialize();
        Iterable<Triple<Rational, BigInteger, RoundingMode>> ts = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO);
                },
                P.triples(P.rationals(), P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            System.out.println("roundToDenominator(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.roundToDenominator(t.b, t.c));
        }
    }

    public static void demoShiftLeft() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    public static void demoShiftRight() {
        initialize();
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    public static void demoContinuedFraction() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("continuedFraction(" + r + ") = " + r.continuedFraction());
        }
    }

    public static void demoFromContinuedFraction() {
        initialize();
        Iterable<List<BigInteger>> iss = map(
                p -> {
                    assert p.b != null;
                    return toList(cons(p.a, p.b));
                },
                (Iterable<Pair<BigInteger, List<BigInteger>>>) P.pairs(
                        P.bigIntegers(),
                        P.lists(P.positiveBigIntegers())
                )
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            String listString = tail(init(is.toString()));
            System.out.println("fromContinuedFraction(" + listString + ") = " + fromContinuedFraction(is));
        }
    }

    public static void demoConvergents() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("convergents(" + r + ") = " + toList(r.convergents()));
        }
    }

    public static void demoPositionalNotation() {
        initialize();
        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = ((ExhaustiveProvider) P).pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).positiveRationals(8)),
                    map(i -> BigInteger.valueOf(i + 2), ((RandomProvider) P).naturalIntegersGeometric(20))
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println("positionalNotation(" + p.a + ", " + p.b + ") = " + p.a.positionalNotation(p.b));
        }
    }

//    public static void fromPositionalNotationDemo() {
//        Generator<BigInteger> big = new FilteredGenerator<>(
//                POSITIVE_BIG_INTEGERS,
//                bi -> bi.compareTo(BigInteger.ONE) > 0
//        );
//        Generator<Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> tg = new SameTripleGenerator<>(new ListGenerator<>(Generators.naturalBigIntegers()));
//        Generator<Pair<Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>, BigInteger>> g = new FilteredGenerator<>(
//                new SubExponentialPairGenerator<Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>, BigInteger>(tg, big),
//                p -> {
//                    if (p.a.c.isEmpty()) return false;
//                    for (BigInteger digit : p.a.a) {
//                        if (digit.compareTo(p.b) >= 0) return false;
//                    }
//                    for (BigInteger digit : p.a.b) {
//                        if (digit.compareTo(p.b) >= 0) return false;
//                    }
//                    for (BigInteger digit : p.a.c) {
//                        if (digit.compareTo(p.b) >= 0) return false;
//                    }
//                    return true;
//                }
//        );
//        for (Pair<Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>, BigInteger> t : g.iterate(limit)) {
//            System.out.println("fromPositionalNotation(" + t.b + ", " + t.a.a + ", " + t.a.b + ", " + t.a.c + ") = " + Rational.fromPositionalNotation(t.b, t.a.a, t.a.b, t.a.c));
//        }
//    }
//
//    public static void digitsDemo() {
//        Generator<BigInteger> big = new FilteredGenerator<>(
//                POSITIVE_BIG_INTEGERS,
//                bi -> bi.compareTo(BigInteger.ONE) > 0
//        );
//        Generator<Pair<Rational, BigInteger>> g = new SubExponentialPairGenerator<>(Rational.nonnegativeRationals(), big);
//        for (Pair<Rational, BigInteger> p : g.iterate(limit)) {
//            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
//            String beforeDecimalString = digits.a.toString();
//            List<String> afterDecimalStrings = new ArrayList<>();
//            int i = 0;
//            for (BigInteger digit : digits.b) {
//                if (i >= 20) {
//                    afterDecimalStrings.add("...");
//                    break;
//                }
//                afterDecimalStrings.add(digit.toString());
//                i++;
//            }
//            String resultString = "(" + beforeDecimalString + ", " + afterDecimalStrings + ")";
//            System.out.println("digits(" + p.a + ", " + p.b + ") = " + resultString);
//        }
//    }

    public static void demoEquals_Rational() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    public static void demoEquals_null() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            //noinspection ObjectEqualsNull
            System.out.println(r + (r.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    public static void demoHashCode() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("hashCode(" + r + ") = " + r.hashCode());
        }
    }

    public static void demoCompareTo() {
        initialize();
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    public static void demoRead() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    public static void demoRead_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(RATIONAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private static void demoFindIn() {
        initialize();
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoFindIn_targeted() {
        initialize();
        Iterable<Character> cs;
        if (P instanceof ExhaustiveProvider) {
            cs = fromString(RATIONAL_CHARS);
        } else {
            cs = ((RandomProvider) P).uniformSample(RATIONAL_CHARS);
        }
        for (String s : take(LIMIT, P.strings(cs))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    public static void demoToString() {
        initialize();
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println(r);
        }
    }
}
