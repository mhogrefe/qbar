package mho.qbar;

import mho.haskellesque.iterables.ExhaustiveProvider;
import mho.haskellesque.iterables.IterableProvider;
import mho.haskellesque.iterables.RandomProvider;
import mho.haskellesque.math.BasicMath;
import mho.haskellesque.ordering.Ordering;
import mho.haskellesque.structures.Pair;
import mho.haskellesque.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

import static mho.haskellesque.iterables.IterableUtils.*;
import static mho.haskellesque.ordering.Ordering.*;
import static mho.qbar.Rational.*;
import static mho.qbar.Rational.of;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RationalProperties {
    private static boolean USE_RANDOM = true;
    private static final String NECESSARY_CHARS = "-/0123456789";
    private static final int LIMIT = 10000;

    private static IterableProvider P;
    private static Iterable<Rational> T_RATIONALS;

    private static void initialize() {
        if (USE_RANDOM) {
            RandomProvider randomProvider = new RandomProvider(new Random(7706916639046193098L));
            P = randomProvider;
            T_RATIONALS = randomRationals(randomProvider);
        } else {
            P = new ExhaustiveProvider();
            T_RATIONALS = RATIONALS;
        }
    }

    @Test
    public void testAllProperties() {
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("Testing " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            
            properties_Of_BigInteger_BigInteger();
            propertiesOf_int_int();
            propertiesOf_BigInteger();
            propertiesOf_int();
            propertiesOf_float();
            propertiesOf_double();
            propertiesOf_BigDecimal();
            propertiesNegate();
            propertiesInvert();
            propertiesAbs();
            propertiesSignum();
            propertiesAdd();
            propertiesSubtract();
            propertiesMultiply_Rational_Rational();
            propertiesMultiply_BigInteger();
            propertiesMultiply_int();
            propertiesDivide_Rational_Rational();
            propertiesDivide_BigInteger();
            propertiesDivide_int();
            propertiesPow();
            propertiesFloor();
            propertiesCeiling();
            propertiesFractionalPart();
            propertiesRound();
//        roundToDenominatorProperties();
//        shiftLeftProperties();
//        shiftRightProperties();
//        binaryExponentProperties();
//        toFloatProperties();
//        toFloatRoundingModeProperties();
            System.out.println();
        }
        System.out.println("Done");
    }

    public static void properties_Of_BigInteger_BigInteger() {
        initialize();
        System.out.println("testing of(BigInteger, BigInteger) properties...");

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
            Rational r = of(p.a, p.b);
            validate(r);
            assertEquals(p.toString(), of(p.a).divide(p.b), r);
        }
    }

    public static void propertiesOf_int_int() {
        initialize();
        System.out.println("testing of(int, int) properties...");

        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        Iterable<Pair<Integer, Integer>> ps = filter(p -> p.b != 0, P.pairs(P.integers()));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = of(p.a, p.b);
            validate(r);
            assertEquals(p.toString(), of(p.a).divide(p.b), r);
            assertTrue(p.toString(), ge(r.getNumerator(), minInt));
            assertTrue(p.toString(), le(r.getNumerator(), maxInt));
            assertTrue(p.toString(), ge(r.getDenominator(), minInt));
            assertTrue(p.toString(), le(r.getDenominator(), maxInt));
        }
    }

    public static void propertiesOf_BigInteger() {
        initialize();
        System.out.println("testing of(BigInteger) properties...");

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Rational r = of(i);
            validate(r);
            assertEquals(i.toString(), r.getDenominator(), BigInteger.ONE);
        }
    }

    public static void propertiesOf_int() {
        initialize();
        System.out.println("testing of(int) properties...");

        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Rational r = of(i);
            validate(r);
            assertEquals(Integer.toString(i), r.getDenominator(), BigInteger.ONE);
            assertTrue(Integer.toString(i), ge(r.getNumerator(), minInt));
            assertTrue(Integer.toString(i), le(r.getNumerator(), maxInt));
        }
    }

    public static void propertiesOf_float() {
        initialize();
        System.out.println("testing of(float) properties...");

        BigInteger denominatorLimit = BigInteger.ONE.shiftLeft(149);
        BigInteger numeratorLimit = BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE.shiftLeft(104));
        Iterable<Float> fs = filter(f -> Float.isFinite(f) && !Float.isNaN(f), P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = of(f);
            assert r != null;
            validate(r);
            if (f != -0.0f) {
                fae(Float.toString(f), f, r.toFloat());
            }
            assertTrue(Float.toString(f), BasicMath.isAPowerOfTwo(r.getDenominator()));
            assertTrue(Float.toString(f), le(r.getDenominator(), denominatorLimit));
            assertTrue(Float.toString(f), le(r.getNumerator(), numeratorLimit));
        }

        for (float f : take(LIMIT, P.ordinaryFloats())) {
            Rational r = of(f);
            assert r != null;
            fae(Float.toString(f), f, r.toFloat());
        }
    }

    public static void propertiesOf_double() {
        initialize();
        System.out.println("testing of(double) properties...");

        BigInteger denominatorLimit = BigInteger.ONE.shiftLeft(1074);
        BigInteger numeratorLimit = BigInteger.ONE.shiftLeft(1024).subtract(BigInteger.ONE.shiftLeft(971));
        Iterable<Double> ds = filter(d -> Double.isFinite(d) && !Double.isNaN(d), P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = of(d);
            assert r != null;
            validate(r);
            if (d != -0.0) {
                dae(Double.toString(d), d, r.toDouble());
            }
            assertTrue(Double.toString(d), BasicMath.isAPowerOfTwo(r.getDenominator()));
            assertTrue(Double.toString(d), le(r.getDenominator(), denominatorLimit));
            assertTrue(Double.toString(d), le(r.getNumerator(), numeratorLimit));
        }

        for (double d : take(LIMIT, P.ordinaryDoubles())) {
            Rational r = of(d);
            assert r != null;
            dae(Double.toString(d), d, r.toDouble());
        }
    }

    public static void propertiesOf_BigDecimal() {
        initialize();
        System.out.println("testing of(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Rational r = of(bd);
            validate(r);
            assertEquals(bd.toString(), bd.stripTrailingZeros(), r.toBigDecimal().stripTrailingZeros());
            assertTrue(bd.toString(), r.hasTerminatingDecimalExpansion());
        }
    }

    public static void propertiesNegate() {
        initialize();
        System.out.println("testing negate() properties...");

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            Rational negativeR = r.negate();
            validate(negativeR);
            assertEquals(r.toString(), r, negativeR.negate());
            assertTrue(add(r, negativeR) == ZERO);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            Rational negativeR = r.negate();
            assertTrue(r.toString(), !r.equals(negativeR));
        }
    }

    public static void propertiesInvert() {
        initialize();
        System.out.println("testing invert() properties...");

        Iterable<Rational> rs = filter(r -> r != ZERO, T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            Rational inverseR = r.invert();
            validate(inverseR);
            assertEquals(r.toString(), r, inverseR.invert());
            assertTrue(multiply(r, inverseR) == ONE);
            assertTrue(inverseR != ZERO);
        }

        rs = filter(r -> r != ZERO && r.abs() != ONE, T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            Rational inverseR = r.invert();
            assertTrue(r.toString(), !r.equals(inverseR));
        }
    }

    public static void propertiesAbs() {
        initialize();
        System.out.println("testing abs() properties...");

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            Rational absR = r.abs();
            validate(absR);
            assertEquals(r.toString(), absR, absR.abs());
            assertTrue(r.toString(), ge(absR, ZERO));
        }
    }

    public static void propertiesSignum() {
        initialize();
        System.out.println("testing signum() properties...");

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            int signumR = r.signum();
            assertEquals(r.toString(), signumR, Ordering.compare(r, ZERO).toInt());
            assertTrue(r.toString(), signumR == -1 || signumR == 0 || signumR == 1);
        }
    }

    public static void propertiesAdd() {
        initialize();
        System.out.println("testing add(Rational, Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(T_RATIONALS))) {
            assert p.a != null;
            assert p.b != null;
            Rational sum = add(p.a, p.b);
            validate(sum);
            assertEquals(p.toString(), sum, add(p.b, p.a));
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), add(ZERO, r), r);
            assertEquals(r.toString(), add(r, ZERO), r);
            assertTrue(r.toString(), add(r, r.negate()) == ZERO);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(T_RATIONALS))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational sum1 = add(add(t.a, t.b), t.c);
            Rational sum2 = add(t.a, add(t.b, t.c));
            assertEquals(t.toString(), sum1, sum2);
        }
    }

    public static void propertiesSubtract() {
        initialize();
        System.out.println("testing subtract(Rational, Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(T_RATIONALS))) {
            assert p.a != null;
            assert p.b != null;
            Rational difference = subtract(p.a, p.b);
            validate(difference);
            assertEquals(p.toString(), difference, subtract(p.b, p.a).negate());
            assertEquals(p.toString(), p.a, add(difference, p.b));
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), subtract(ZERO, r), r.negate());
            assertEquals(r.toString(), subtract(r, ZERO), r);
            assertTrue(r.toString(), subtract(r, r) == ZERO);
        }
    }

    public static void propertiesMultiply_Rational_Rational() {
        initialize();
        System.out.println("testing multiply(Rational, Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(T_RATIONALS))) {
            assert p.a != null;
            assert p.b != null;
            Rational product = multiply(p.a, p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply(p.b, p.a));
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), multiply(ONE, r), r);
            assertEquals(r.toString(), multiply(r, ONE), r);
            assertTrue(r.toString(), multiply(ZERO, r) == ZERO);
            assertTrue(r.toString(), multiply(r, ZERO) == ZERO);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), multiply(r, r.invert()) == ONE);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(T_RATIONALS))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational product1 = multiply(multiply(t.a, t.b), t.c);
            Rational product2 = multiply(t.a, multiply(t.b, t.c));
            assertEquals(t.toString(), product1, product2);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(T_RATIONALS))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = multiply(add(t.a, t.b), t.c);
            Rational expression2 = add(multiply(t.a, t.c), multiply(t.b, t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    public static void propertiesMultiply_BigInteger() {
        initialize();
        System.out.println("testing multiply(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(T_RATIONALS, P.bigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply(p.a, of(p.b)));
            assertEquals(p.toString(), product, multiply(of(p.b), p.a));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), ONE.multiply(i), of(i));
            assertTrue(i.toString(), ZERO.multiply(i) == ZERO);
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), r.multiply(BigInteger.ONE), r);
            assertTrue(r.toString(), r.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<BigInteger> bis = filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers());
        for (BigInteger i : take(LIMIT, bis)) {
            assertTrue(i.toString(), of(i).invert().multiply(i) == ONE);
        }

        Iterable<Rational> rs = T_RATIONALS;
        for (Triple<Rational, Rational, BigInteger> t : take(LIMIT, P.triples(rs, rs, P.bigIntegers()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = add(t.a, t.b).multiply(t.c);
            Rational expression2 = add(t.a.multiply(t.c), t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    public static void propertiesMultiply_int() {
        initialize();
        System.out.println("testing multiply(int) properties...");

        for (Pair<Rational, Integer> p :take(LIMIT, P.pairs(T_RATIONALS, P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply(p.a, of(p.b)));
            assertEquals(p.toString(), product, multiply(of(p.b), p.a));
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(Integer.toString(i), ONE.multiply(i), of(i));
            assertTrue(Integer.toString(i), ZERO.multiply(i) == ZERO);
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), r.multiply(1), r);
            assertTrue(r.toString(), r.multiply(0) == ZERO);
        }

        Iterable<Integer> is = filter(i -> i != 0, P.integers());
        for (int i : take(LIMIT, is)) {
            assertTrue(Integer.toString(i), of(i).invert().multiply(i) == ONE);
        }

        Iterable<Rational> rs = T_RATIONALS;
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, P.triples(rs, rs, P.integers()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = add(t.a, t.b).multiply(t.c);
            Rational expression2 = add(t.a.multiply(t.c), t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    public static void propertiesDivide_Rational_Rational() {
        initialize();
        System.out.println("testing divide(Rational, Rational) properties...");

        Iterable<Pair<Rational, Rational>> ps = filter(p -> p.b != ZERO, P.pairs(T_RATIONALS));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = divide(p.a, p.b);
            validate(quotient);
            assertEquals(p.toString(), p.a, multiply(quotient, p.b));
        }

        ps = filter(p -> p.a != ZERO && p.b != ZERO, P.pairs(T_RATIONALS));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), divide(p.a, p.b), divide(p.b, p.a).invert());
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), divide(r, ONE), r);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), divide(ONE, r), r.invert());
            assertTrue(r.toString(), divide(r, r) == ONE);
        }
    }

    public static void propertiesDivide_BigInteger() {
        initialize();
        System.out.println("testing divide(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                T_RATIONALS,
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs(
                (Iterable<Rational>) filter(r -> r != ZERO, T_RATIONALS),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), divide(of(p.b), p.a).invert());
        }
        
        Iterable<BigInteger> bis = filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers());
        for (BigInteger i : take(LIMIT, bis)) {
            assertEquals(i.toString(), ONE.divide(i), of(i).invert());
            assertEquals(i.toString(), of(i).divide(i), ONE);
        }
        
        for (Rational r : take(LIMIT, RATIONALS)) {
            assertEquals(r.toString(), r.divide(BigInteger.ONE), r);      
        }
    }

    public static void propertiesDivide_int() {
        initialize();
        System.out.println("testing divide(int) properties...");

        Iterable<Pair<Rational, Integer>> ps = P.pairs(T_RATIONALS, filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs((Iterable<Rational>) filter(r -> r != ZERO, T_RATIONALS), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), divide(of(p.b), p.a).invert());
        }

        Iterable<Integer> is = filter(i -> i != 0, P.integers());
        for (int i : take(LIMIT, is)) {
            assertEquals(Integer.toString(i), ONE.divide(i), of(i).invert());
            assertEquals(Integer.toString(i), of(i).divide(i), ONE);
        }

        for (Rational r : take(LIMIT, RATIONALS)) {
            assertEquals(r.toString(), r.divide(1), r);
        }
    }

    public static void propertiesPow() {
        initialize();
        System.out.println("testing pow(int) properties...");

        Iterable<Integer> exps;
        if (P instanceof ExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((RandomProvider) P).integersGeometric(20);
        }

        Iterable<Pair<Rational, Integer>> ps = filter(p -> p.b >= 0 || p.a != ZERO, P.pairs(T_RATIONALS, exps));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = p.a.pow(p.b);
            validate(r);
        }

        ps = P.pairs(filter(r -> r != ZERO, T_RATIONALS), exps);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = p.a.pow(p.b);
            assertEquals(p.toString(), r, p.a.pow(-p.b).invert());
            assertEquals(p.toString(), r, p.a.invert().pow(-p.b));
        }

        Iterable<Integer> pexps;
        if (P instanceof ExhaustiveProvider) {
            pexps = P.positiveIntegers();
        } else {
            pexps = ((RandomProvider) P).positiveIntegersGeometric(20);
        }
        for (int i : take(LIMIT, pexps)) {
            assertTrue(Integer.toString(i), ZERO.pow(i) == ZERO);
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertTrue(r.toString(), r.pow(0) == ONE);
            assertEquals(r.toString(), r.pow(1), r);
            assertEquals(r.toString(), r.pow(2), multiply(r, r));
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.pow(-1), r.invert());
        }

        Iterable<Triple<Rational, Integer, Integer>> ts1 = filter(
                p -> p.b >= 0 && p.c >= 0 || p.a != ZERO,
                P.triples(T_RATIONALS, exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = multiply(t.a.pow(t.b), t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b + t.c);
            assertEquals(t.toString(), expression1, expression2);
        }

        ts1 = filter(
                t -> t.a != ZERO || t.c == 0 && t.b >= 0,
                P.triples(T_RATIONALS, exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = divide(t.a.pow(t.b), t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b - t.c);
            assertEquals(t.toString(), expression1, expression2);
        }

        ts1 = filter(
                t -> t.a != ZERO || t.b >= 0 && t.c >= 0,
                P.triples(T_RATIONALS, exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression5 = t.a.pow(t.b).pow(t.c);
            Rational expression6 = t.a.pow(t.b * t.c);
            assertEquals(t.toString(), expression5, expression6);
        }

        Iterable<Triple<Rational, Rational, Integer>> ts2 = filter(
                t -> t.a != ZERO && t.b != ZERO || t.c >= 0,
                P.triples(T_RATIONALS, T_RATIONALS, exps)
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = multiply(t.a, t.b).pow(t.c);
            Rational expression2 = multiply(t.a.pow(t.c), t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }

        ts2 = filter(
                t -> t.a != ZERO || t.c >= 0,
                P.triples(T_RATIONALS, filter(r -> r != ZERO, T_RATIONALS), exps)
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = divide(t.a, t.b).pow(t.c);
            Rational expression2 = divide(t.a.pow(t.c), t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    public static void propertiesFloor() {
        initialize();
        System.out.println("testing floor() properties...");

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            BigInteger floor = r.floor();
            assertTrue(r.toString(), le(of(floor), r));
            assertTrue(r.toString(), le(subtract(r, of(floor)), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).floor(), i);
        }
    }

    public static void propertiesCeiling() {
        initialize();
        System.out.println("testing ceiling() properties...");

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            BigInteger ceiling = r.ceiling();
            assertTrue(r.toString(), ge(of(ceiling), r));
            assertTrue(r.toString(), le(subtract(of(ceiling), r), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).ceiling(), i);
        }
    }

    public static void propertiesFractionalPart() {
        initialize();
        System.out.println("testing fractionalPart() properties...");

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            Rational fractionalPart = r.fractionalPart();
            validate(fractionalPart);
            assertTrue(r.toString(), ge(fractionalPart, ZERO));
            assertTrue(r.toString(), lt(fractionalPart, ONE));
            assertEquals(r.toString(), add(of(r.floor()), fractionalPart), r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).fractionalPart(), ZERO);
        }
    }

    public static void propertiesRound() {
        initialize();
        System.out.println("testing round(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.getDenominator().equals(BigInteger.ONE);
                },
                P.pairs(T_RATIONALS, P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            BigInteger rounded = p.a.round(p.b);
            assertTrue(p.toString(), rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
            assertTrue(p.toString(), lt(subtract(p.a, of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).round(RoundingMode.UNNECESSARY), i);
        }

        for (Rational r : take(LIMIT, T_RATIONALS)) {
            assertEquals(r.toString(), r.round(RoundingMode.FLOOR), r.floor());
            assertEquals(r.toString(), r.round(RoundingMode.CEILING), r.ceiling());
            assertTrue(r.toString(), le(of(r.round(RoundingMode.DOWN)).abs(), r.abs()));
            assertTrue(r.toString(), ge(of(r.round(RoundingMode.UP)).abs(), r.abs()));
            assertTrue(r.toString(), le(subtract(r, of(r.round(RoundingMode.HALF_DOWN))).abs(), of(1, 2)));
            assertTrue(r.toString(), le(subtract(r, of(r.round(RoundingMode.HALF_UP))).abs(), of(1, 2)));
            assertTrue(r.toString(), le(subtract(r, of(r.round(RoundingMode.HALF_EVEN))).abs(), of(1, 2)));
        }

        Iterable<Rational> rs = filter(r -> lt(r.abs().fractionalPart(), of(1, 2)), T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.round(RoundingMode.HALF_DOWN), r.round(RoundingMode.DOWN));
            assertEquals(r.toString(), r.round(RoundingMode.HALF_UP), r.round(RoundingMode.DOWN));
            assertEquals(r.toString(), r.round(RoundingMode.HALF_EVEN), r.round(RoundingMode.DOWN));
        }

        rs = filter(r -> gt(r.abs().fractionalPart(), of(1, 2)), T_RATIONALS);
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.round(RoundingMode.HALF_DOWN), r.round(RoundingMode.UP));
            assertEquals(r.toString(), r.round(RoundingMode.HALF_UP), r.round(RoundingMode.UP));
            assertEquals(r.toString(), r.round(RoundingMode.HALF_EVEN), r.round(RoundingMode.UP));
        }

        //odd multiples of 1/2
        rs = map(i -> of(i.shiftLeft(1).add(BigInteger.ONE), BigInteger.valueOf(2)), P.bigIntegers());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.round(RoundingMode.HALF_DOWN), r.round(RoundingMode.DOWN));
            assertEquals(r.toString(), r.round(RoundingMode.HALF_UP), r.round(RoundingMode.UP));
            assertFalse(r.toString(), r.round(RoundingMode.HALF_EVEN).testBit(0));
        }
    }

    public static void propertiesRoundToDenominator() {
        initialize();
        System.out.println("testing roundToDenominator(BigInteger, RoundingMode) properties...");

        Iterable<Triple<Rational, BigInteger, RoundingMode>> ts = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO);
                },
                P.triples(T_RATIONALS, P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational rounded = t.a.roundToDenominator(t.b, t.c);
            validate(rounded);
            assertEquals(t.toString(), t.b.mod(rounded.getDenominator()), BigInteger.ZERO);
            assertTrue(t.toString(), rounded == ZERO || rounded.signum() == t.a.signum());
            assertTrue(t.toString(), lt(subtract(t.a, rounded).abs(), of(BigInteger.ONE, t.b)));
            if (t.c == RoundingMode.HALF_DOWN || t.c == RoundingMode.HALF_UP || t.c == RoundingMode.HALF_EVEN) {
                Rational fractionalPart = t.a.abs().multiply(t.b).fractionalPart();
                if (fractionalPart.compareTo(of(1, 2)) < 0) {
                    assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.DOWN));
                } else if (fractionalPart.compareTo(of(1, 2)) > 0) {
                    assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.UP));
                } else {
                    if (t.c == RoundingMode.HALF_DOWN) {
                        assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.DOWN));
                    }
                    if (t.c == RoundingMode.HALF_UP) {
                        assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.UP));
                    }
                    if (t.c == RoundingMode.HALF_EVEN) {
                        assertEquals(t.toString(), rounded.multiply(t.b).getNumerator().testBit(0), false);
                    }
                }
            }
        }

        Iterable<Pair<Rational, RoundingMode>> ps1 = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.getDenominator().equals(BigInteger.ONE);
                },
                P.pairs(T_RATIONALS, P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps1)) {
            assert p.a != null;
            assert p.b != null;
            Rational rounded = p.a.roundToDenominator(BigInteger.ONE, p.b);
            assertEquals(p.toString(), rounded.getNumerator(), p.a.round(p.b));
            assertEquals(p.toString(), rounded.getDenominator(), BigInteger.ONE);
        }

        Iterable<Pair<Rational, BigInteger>> ps2 = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO);
                },
                P.pairs(T_RATIONALS, P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
            assertTrue(p.toString(), p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY).equals(p.a));
        }

        ps2 = P.pairs(T_RATIONALS, P.positiveBigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
            assertTrue(p.toString(), le(p.a.roundToDenominator(p.b, RoundingMode.FLOOR), p.a));
            assertTrue(p.toString(), ge(p.a.roundToDenominator(p.b, RoundingMode.CEILING), p.a));
            assertTrue(p.toString(), le(p.a.roundToDenominator(p.b, RoundingMode.DOWN).abs(), p.a.abs()));
            assertTrue(p.toString(), ge(p.a.roundToDenominator(p.b, RoundingMode.UP).abs(), p.a.abs()));
            assertTrue(
                    p.toString(),
                    le(
                            subtract(p.a, p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
            assertTrue(
                    p.toString(),
                    le(
                            subtract(p.a, p.a.roundToDenominator(p.b, RoundingMode.HALF_UP)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
            assertTrue(
                    p.toString(),
                    le(
                            subtract(p.a, p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
        }

        ps2 = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return lt((Rational) p.a.abs().multiply(p.b).fractionalPart(), of(1, 2));
                },
                P.pairs(T_RATIONALS, P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN),
                    p.a.roundToDenominator(p.b, RoundingMode.DOWN)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_UP),
                    p.a.roundToDenominator(p.b, RoundingMode.DOWN)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN),
                    p.a.roundToDenominator(p.b, RoundingMode.DOWN)
            );
        }

        ps2 = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return gt((Rational) p.a.abs().multiply(p.b).fractionalPart(), of(1, 2));
                },
                P.pairs(T_RATIONALS, P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN),
                    p.a.roundToDenominator(p.b, RoundingMode.UP)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_UP),
                    p.a.roundToDenominator(p.b, RoundingMode.UP)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN),
                    p.a.roundToDenominator(p.b, RoundingMode.UP)
            );
        }

//        ps2 = filter(
//                a,
//                b
//        );
    }

//    public static void shiftLeftProperties() {
//        Iterable<Pair<Rational, Integer>> g = P.pairs(T_RATIONALS, P.integers());
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational shifted = p.a.shiftLeft(p.b);
//            validate(shifted);
//            assertEquals(p.toString(), p.a.negate().shiftLeft(p.b), shifted.negate());
//            assertEquals(p.toString(), shifted, p.a.shiftRight(-p.b));
//            if (p.b >= 0) {
//                assertEquals(p.toString(), shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
//            }
//        }
//    }
//
//    public static void shiftRightProperties() {
//        Iterable<Pair<Rational, Integer>> g = P.pairs(T_RATIONALS, P.integers());
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational shifted = p.a.shiftRight(p.b);
//            validate(shifted);
//            assertEquals(p.toString(), p.a.negate().shiftRight(p.b), shifted.negate());
//            assertEquals(p.toString(), shifted, p.a.shiftLeft(-p.b));
//            if (p.b >= 0) {
//                assertEquals(p.toString(), shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
//            }
//        }
//    }
//
//    public static void binaryExponentProperties() {
//        for (Rational r : positiveRationals().iterate(limit)) {
//            int exponent = r.binaryExponent();
//            Rational power = ONE.shiftLeft(exponent);
//            assertTrue(r.toString(), power.compareTo(r) <= 0);
//            assertTrue(r.toString(), r.compareTo(power.shiftLeft(1)) < 0);
//        }
//    }
//
//    public static void toFloatProperties() {
//        for (Rational r : T_RATIONALS.iterate(limit)) {
//            float f = r.toFloat();
//            assertEquals(r.toString(), f, r.toFloat(RoundingMode.HALF_EVEN));
//        }
//    }
//
//    public static void toFloatRoundingModeProperties() {
//        Iterable<Pair<Rational, RoundingMode>> g = new FilteredIterable<Pair<Rational, RoundingMode>>(
//                P.pairs(T_RATIONALS, Iterables.roundingModes()),
//                p -> p.b != RoundingMode.UNNECESSARY
//                        || of(p.a.toFloat(RoundingMode.FLOOR)).equals(p.a));
//        for (Pair<Rational, RoundingMode> p : g.iterate(limit)) {
//            float rounded = p.a.toFloat(p.b);
//            assertTrue(p.toString(), !Float.isNaN(rounded));
//            assertTrue(p.toString(), rounded == 0.0 || Math.signum(rounded) == p.a.signum());
//            float successor = MathUtils.successor(rounded);
//            float predecessor = MathUtils.predecessor(rounded);
//            float up = p.a.signum() == -1 ? predecessor : successor;
//            float down = p.a.signum() == -1 ? successor : predecessor;
//            if (p.b == RoundingMode.UNNECESSARY) {
//                assertEquals(p.toString(), p.a, of(rounded));
//                assertTrue(p.toString(), Float.isFinite(rounded));
//                assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.FLOOR) {
//                assertTrue(p.toString(), of(rounded).compareTo(p.a) <= 0);
//                assertTrue(p.toString(), of(successor).compareTo(p.a) > 0);
//                if (p.a.compareTo(ZERO) >= 0 && p.a.compareTo(SMALLEST_FLOAT) < 0) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) < 0) {
//                    assertTrue(p.toString(), rounded < 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) >= 0) {
//                    assertEquals(p.toString(), rounded, Float.MAX_VALUE);
//                }
//                assertTrue(p.toString(), rounded < 0 || Float.isFinite(rounded));
//                assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.CEILING) {
//                assertTrue(p.toString(), of(rounded).compareTo(p.a) >= 0);
//                assertTrue(p.toString(), of(predecessor).compareTo(p.a) < 0);
//                if (p.a == ZERO) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(ZERO) < 0 && p.a.compareTo(SMALLEST_FLOAT.negate()) > 0) {
//                    assertEquals(p.toString(), rounded, -0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) > 0) {
//                    assertTrue(p.toString(), rounded > 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) <= 0) {
//                    assertEquals(p.toString(), rounded, -Float.MAX_VALUE);
//                }
//                assertTrue(p.toString(), rounded > 0 || Float.isFinite(rounded));
//            }
//            if (p.b == RoundingMode.DOWN) {
//                assertTrue(p.toString(), of(rounded).abs().compareTo(p.a.abs()) <= 0);
//                assertTrue(p.toString(), of(up).abs().compareTo(p.a.abs()) > 0);
//                if (p.a.compareTo(ZERO) >= 0 && p.a.compareTo(SMALLEST_FLOAT) < 0) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(ZERO) < 0 && p.a.compareTo(SMALLEST_FLOAT.negate()) > 0) {
//                    assertEquals(p.toString(), rounded, -0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) >= 0) {
//                    assertEquals(p.toString(), rounded, Float.MAX_VALUE);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) <= 0) {
//                    assertEquals(p.toString(), rounded, -Float.MAX_VALUE);
//                }
//                assertTrue(p.toString(), Float.isFinite(rounded));
//            }
//            if (p.b == RoundingMode.UP) {
//                assertTrue(p.toString(), of(rounded).abs().compareTo(p.a.abs()) >= 0);
//                if (p.a != ZERO) {
//                    assertTrue(p.toString(), of(down).abs().compareTo(p.a.abs()) < 0);
//                }
//                if (p.a == ZERO) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) > 0) {
//                    assertTrue(p.toString(), rounded > 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) < 0) {
//                    assertTrue(p.toString(), rounded < 0 && Float.isInfinite(rounded));
//                }
//                assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.HALF_DOWN || p.b == RoundingMode.HALF_UP || p.b == RoundingMode.HALF_EVEN) {
//                boolean equidistant = false;
//                if (Float.isFinite(rounded) && rounded != Float.MAX_VALUE && rounded != -Float.MAX_VALUE) {
//                    Rational distance = subtract(of(rounded), p.a).abs();
//                    Rational predDistance = subtract(of(predecessor), p.a).abs();
//                    Rational succDistance = subtract(of(successor), p.a).abs();
//                    assertEquals(p.toString(), min(distance, predDistance, succDistance), distance);
//                    if (distance.equals(predDistance) || distance.equals(succDistance)) {
//                        equidistant = true;
//                    }
//                }
//                //TODO
//                if (p.b == RoundingMode.HALF_DOWN) {
//                    if (equidistant) {
//                        assertEquals(p.toString(), rounded, p.a.toFloat(RoundingMode.DOWN));
//                    }
//                } else if (p.b == RoundingMode.HALF_UP) {
//                    if (equidistant) {
//                        assertEquals(p.toString(), rounded, p.a.toFloat(RoundingMode.UP));
//                    }
//                }
//            }
//        }
//    }

    private static void validate(@NotNull Rational r) {
        assertEquals(r.toString(), r.getNumerator().gcd(r.getDenominator()), BigInteger.ONE);
        assertEquals(r.toString(), r.getDenominator().signum(), 1);
        if (r.equals(ZERO)) assertTrue(r.toString(), r == ZERO);
        if (r.equals(ONE)) assertTrue(r.toString(), r == ONE);
    }

    private static void fae(String message, float f1, float f2) {
        assertEquals(message, Float.toString(f1), Float.toString(f2));
    }

    private static void dae(String message, double d1, double d2) {
        assertEquals(message, Double.toString(d1), Double.toString(d2));
    }
}
