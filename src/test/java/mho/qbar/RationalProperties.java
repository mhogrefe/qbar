package mho.qbar;

import mho.haskellesque.iterables.ExhaustiveProvider;
import mho.haskellesque.iterables.IterableProvider;
import mho.haskellesque.iterables.RandomProvider;
import mho.haskellesque.math.BasicMath;
import mho.haskellesque.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.Random;

import static mho.haskellesque.iterables.IterableUtils.*;
import static mho.qbar.Rational.*;
import static org.junit.Assert.*;

public class RationalProperties {
    private static boolean USE_RANDOM = true;
    private static final String NECESSARY_CHARS = "-/0123456789";
    private static final int LIMIT = 10000;

    private static IterableProvider P;
    private static Iterable<Rational> RATIONALS;

    private static void initialize() {
        P = USE_RANDOM ?
                new RandomProvider(new Random(7706916639046193098L)) :
                new ExhaustiveProvider();
        Iterable<Rational> RATIONALS = USE_RANDOM ?
                Rational.RATIONALS :
                Rational.RATIONALS;  //todo
    }

    @Test
    public void testAllProperties() {
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("Testing " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            
            ofBigIntegerBigIntegerProperties();
            ofIntIntProperties();
            ofBigIntegerProperties();
            ofIntProperties();
            ofFloatProperties();
            ofDoubleProperties();
            ofBigDecimalProperties();
//            negateProperties();
//            invertProperties();
//            absProperties();
//            signumProperties();
//            addProperties();
//            subtractProperties();
//            multiplyRationalRationalProperties();
//            multiplyBigIntegerProperties();
//            multiplyIntProperties();
//            divideRationalRationalProperties();
//        divideBigIntegerProperties();
//        divideIntProperties();
//        powProperties1();
//        powProperties2();
//        powProperties3();
//        floorProperties();
//        ceilingProperties();
//        fractionalPartProperties();
//        roundProperties();
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

    public static void ofBigIntegerBigIntegerProperties() {
        initialize();
        System.out.println("testing of(BigInteger, BigInteger) properties...");
        Iterable<Pair<BigInteger, BigInteger>> it = filter(
                p -> !p.b.equals(BigInteger.ZERO),
                P.pairs(P.bigIntegers())
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, it)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = of(p.a, p.b);
            validate(r);
            assertEquals(p.toString(), of(p.a).divide(p.b), r);
        }
    }

    public static void ofIntIntProperties() {
        initialize();
        System.out.println("testing of(int, int) properties...");
        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        Iterable<Pair<Integer, Integer>> it = filter(p -> p.b != 0, P.pairs(P.integers()));
        for (Pair<Integer, Integer> p : take(LIMIT, it)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = of(p.a, p.b);
            validate(r);
            assertEquals(p.toString(), of(p.a).divide(p.b), r);
            assertTrue(p.toString(), r.getNumerator().compareTo(minInt) >= 0);
            assertTrue(p.toString(), r.getNumerator().compareTo(maxInt) <= 0);
            assertTrue(p.toString(), r.getDenominator().compareTo(minInt) >= 0);
            assertTrue(p.toString(), r.getDenominator().compareTo(maxInt) <= 0);
        }
    }

    public static void ofBigIntegerProperties() {
        initialize();
        System.out.println("testing of(BigInteger) properties...");
        for (BigInteger bi : take(LIMIT, P.bigIntegers())) {
            Rational r = of(bi);
            validate(r);
            assertEquals(bi.toString(), r.getDenominator(), BigInteger.ONE);
        }
    }

    public static void ofIntProperties() {
        initialize();
        System.out.println("testing of(int) properties...");
        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Rational r = of(i);
            validate(r);
            assertEquals(Integer.toString(i), r.getDenominator(), BigInteger.ONE);
            assertTrue(Integer.toString(i), r.getNumerator().compareTo(minInt) >= 0);
            assertTrue(Integer.toString(i), r.getNumerator().compareTo(maxInt) <= 0);
        }
    }

    public static void ofFloatProperties() {
        initialize();
        System.out.println("testing of(float) properties...");
        BigInteger denominatorLimit = BigInteger.ONE.shiftLeft(149);
        BigInteger numeratorLimit = BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE.shiftLeft(104));
        Iterable<Float> it = filter(f -> Float.isFinite(f) && !Float.isNaN(f), P.floats());
        for (float f : take(LIMIT, it)) {
            Rational r = of(f);
            validate(r);
            if (f != -0.0f) {
                fae(Float.toString(f), f, r.toFloat());
            }
            assertTrue(Float.toString(f), BasicMath.isAPowerOfTwo(r.getDenominator()));
            assertTrue(Float.toString(f), r.getDenominator().compareTo(denominatorLimit) <= 0);
            assertTrue(Float.toString(f), r.getNumerator().compareTo(numeratorLimit) <= 0);
        }

        it = filter(f -> Float.isFinite(f) && !Float.isNaN(f) && f != -0.0f, P.floats());
        for (float f : take(LIMIT, it)) {
            Rational r = of(f);
            fae(Float.toString(f), f, r.toFloat());
        }
    }

    public static void ofDoubleProperties() {
        initialize();
        System.out.println("testing of(double) properties...");
        BigInteger denominatorLimit = BigInteger.ONE.shiftLeft(1074);
        BigInteger numeratorLimit = BigInteger.ONE.shiftLeft(1024).subtract(BigInteger.ONE.shiftLeft(971));
        Iterable<Double> it = filter(d -> Double.isFinite(d) && !Double.isNaN(d), P.doubles());
        for (double d : take(LIMIT, it)) {
            Rational r = of(d);
            validate(r);
            if (d != -0.0) {
                dae(Double.toString(d), d, r.toDouble());
            }
            assertTrue(Double.toString(d), BasicMath.isAPowerOfTwo(r.getDenominator()));
            assertTrue(Double.toString(d), r.getDenominator().compareTo(denominatorLimit) <= 0);
            assertTrue(Double.toString(d), r.getNumerator().compareTo(numeratorLimit) <= 0);
        }

        it = filter(d -> Double.isFinite(d) && !Double.isNaN(d) && d != -0.0, P.doubles());
        for (double d : take(LIMIT, it)) {
            Rational r = of(d);
            dae(Double.toString(d), d, r.toDouble());
        }
    }

    public static void ofBigDecimalProperties() {
        initialize();
        System.out.println("testing of(BigDecimal) properties...");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Rational r = of(bd);
            validate(r);
            assertEquals(bd.toString(), bd.stripTrailingZeros(), r.toBigDecimal().stripTrailingZeros());
            assertTrue(bd.toString(), r.hasTerminatingDecimalExpansion());
        }
    }
//
//    public static void negateProperties() {
//        initialize();
//        System.out.println("testing negate properties...");
//        for (Rational r : take(LIMIT, RATIONALS)) {
//            Rational negativeR = r.negate();
//            validate(negativeR);
//            Assert.assertEquals(r.toString(), r, negativeR.negate());
//            Assert.assertTrue(Rational.add(r, negativeR) == Rational.ZERO);
//        }
//        Iterable<Rational> g = new FilteredIterable<>(
//                RATIONALS,
//                r -> r != Rational.ZERO
//        );
//
//        for (Rational r : g.iterate(limit)) {
//            Rational negativeR = r.negate();
//            Assert.assertTrue(r.toString(), !r.equals(negativeR));
//        }
//    }
//
//    public static void invertProperties(int limit) {
//        initialize();
//        System.out.println("testing invert properties...");
//        Iterable<Rational> g = new FilteredIterable<>(
//                RATIONALS,
//                r -> r != Rational.ZERO
//        );
//        for (Rational r : g.iterate(limit)) {
//            Rational inverseR = r.invert();
//            validate(inverseR);
//            Assert.assertEquals(r.toString(), r, inverseR.invert());
//            Assert.assertTrue(Rational.multiply(r, inverseR) == Rational.ONE);
//            Assert.assertTrue(inverseR != Rational.ZERO);
//        }
//
//        g = new FilteredIterable<>(
//                RATIONALS,
//                r -> r != Rational.ZERO && r.abs() != Rational.ONE
//        );
//        for (Rational r : g.iterate(limit)) {
//            Rational inverseR = r.invert();
//            Assert.assertTrue(r.toString(), !r.equals(inverseR));
//        }
//    }
//
//    public static void absProperties(int limit) {
//        initialize();
//        System.out.println("testing abs properties...");
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Rational absR = r.abs();
//            validate(absR);
//            Assert.assertEquals(r.toString(), absR, absR.abs());
//            Assert.assertTrue(r.toString(), absR.compareTo(Rational.ZERO) >= 0);
//        }
//    }
//
//    public static void signumProperties(int limit) {
//        initialize();
//        System.out.println("testing signum properties...");
//        for (Rational r : RATIONALS.iterate(limit)) {
//            int signumR = r.signum();
//            Assert.assertEquals(r.toString(), signumR, Integer.signum(r.compareTo(Rational.ZERO)));
//            Assert.assertTrue(r.toString(), signumR == -1 || signumR == 0 || signumR == 1);
//        }
//    }
//
//    public static void addProperties(int limit) {
//        initialize();
//        System.out.println("testing add properties...");
//        Iterable<Pair<Rational, Rational>> g = P.pairs(RATIONALS);
//        for (Pair<Rational, Rational> p : g.iterate(limit)) {
//            Rational sum = Rational.add(p.a, p.b);
//            validate(sum);
//            Assert.assertEquals(p.toString(), sum, Rational.add(p.b, p.a));
//        }
//
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Assert.assertEquals(r.toString(), Rational.add(Rational.ZERO, r), r);
//            Assert.assertEquals(r.toString(), Rational.add(r, Rational.ZERO), r);
//            Assert.assertTrue(r.toString(), Rational.add(r, r.negate()) == Rational.ZERO);
//        }
//
//        for (Triple<Rational, Rational, Rational> t : new SameTripleIterable<>(RATIONALS).iterate(limit)) {
//            Rational sum1 = Rational.add(Rational.add(t.a, t.b), t.c);
//            Rational sum2 = Rational.add(t.a, Rational.add(t.b, t.c));
//            Assert.assertEquals(t.toString(), sum1, sum2);
//        }
//    }
//
//    public static void subtractProperties() {
//        initialize();
//        System.out.println("testing subtract properties...");
//        for (Pair<Rational, Rational> p : P.pairs(RATIONALS).iterate(limit)) {
//            Rational difference = Rational.subtract(p.a, p.b);
//            validate(difference);
//            Assert.assertEquals(p.toString(), difference, Rational.subtract(p.b, p.a).negate());
//            Assert.assertEquals(p.toString(), p.a, Rational.add(difference, p.b));
//        }
//
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Assert.assertEquals(r.toString(), Rational.subtract(Rational.ZERO, r), r.negate());
//            Assert.assertEquals(r.toString(), Rational.subtract(r, Rational.ZERO), r);
//            Assert.assertTrue(r.toString(), Rational.subtract(r, r) == Rational.ZERO);
//        }
//    }
//
//    public static void multiplyRationalRationalProperties() {
//        initialize();
//        System.out.println("testing multiply(Rational, Rational) properties...");
//        for (Pair<Rational, Rational> p : P.pairs(RATIONALS).iterate(limit)) {
//            Rational product = Rational.multiply(p.a, p.b);
//            validate(product);
//            Assert.assertEquals(p.toString(), product, Rational.multiply(p.b, p.a));
//        }
//
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Assert.assertEquals(r.toString(), Rational.multiply(Rational.ONE, r), r);
//            Assert.assertEquals(r.toString(), Rational.multiply(r, Rational.ONE), r);
//            Assert.assertTrue(r.toString(), Rational.multiply(Rational.ZERO, r) == Rational.ZERO);
//            Assert.assertTrue(r.toString(), Rational.multiply(r, Rational.ZERO) == Rational.ZERO);
//        }
//
//        Iterable<Rational> g = new FilteredIterable<>(
//                RATIONALS,
//                r -> r != Rational.ZERO
//        );
//        for (Rational r : g.iterate(limit)) {
//            Assert.assertTrue(r.toString(), Rational.multiply(r, r.invert()) == Rational.ONE);
//        }
//
//        for (Triple<Rational, Rational, Rational> t : new SameTripleIterable<>(RATIONALS).iterate(limit)) {
//            Rational product1 = Rational.multiply(Rational.multiply(t.a, t.b), t.c);
//            Rational product2 = Rational.multiply(t.a, Rational.multiply(t.b, t.c));
//            Assert.assertEquals(t.toString(), product1, product2);
//        }
//
//        for (Triple<Rational, Rational, Rational> t : new SameTripleIterable<>(RATIONALS).iterate(limit)) {
//            Rational expression1 = Rational.multiply(Rational.add(t.a, t.b), t.c);
//            Rational expression2 = Rational.add(Rational.multiply(t.a, t.c), Rational.multiply(t.b, t.c));
//            Assert.assertEquals(t.toString(), expression1, expression2);
//            Rational expression3 = Rational.multiply(t.c, Rational.add(t.a, t.b));
//            Rational expression4 = Rational.add(Rational.multiply(t.c, t.a), Rational.multiply(t.c, t.b));
//            Assert.assertEquals(t.toString(), expression3, expression4);
//        }
//    }
//
//    public static void multiplyBigIntegerProperties(int limit) {
//        initialize();
//        System.out.println("testing multiply(BigInteger) properties...");
//        Iterable<Pair<Rational, BigInteger>> g = new PairIterable<>(RATIONALS, P.bigIntegers());
//        for (Pair<Rational, BigInteger> p : g.iterate(limit)) {
//            Rational product = p.a.multiply(p.b);
//            validate(product);
//            Assert.assertEquals(p.toString(), product, Rational.multiply(Rational.of(p.b), p.a));
//        }
//
//        for (BigInteger bi : P.bigIntegers().iterate(limit)) {
//            Assert.assertEquals(bi.toString(), Rational.ONE.multiply(bi), Rational.of(bi));
//            Assert.assertTrue(bi.toString(), Rational.ZERO.multiply(bi) == Rational.ZERO);
//        }
//
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Assert.assertEquals(r.toString(), r.multiply(BigInteger.ONE), r);
//            Assert.assertTrue(r.toString(), r.multiply(BigInteger.ZERO) == Rational.ZERO);
//        }
//
//        Iterable<BigInteger> big = new FilteredIterable<>(
//                P.bigIntegers(),
//                bi -> !bi.equals(BigInteger.ZERO)
//        );
//        for (BigInteger bi : big.iterate(limit)) {
//            Assert.assertTrue(bi.toString(), Rational.of(bi).invert().multiply(bi) == Rational.ONE);
//        }
//
//        Iterable<Rational> rg = RATIONALS;
//        for (Triple<Rational, Rational, BigInteger> t : triples(rg, rg, P.bigIntegers()).iterate(limit)) {
//            Rational expression1 = Rational.add(t.a, t.b).multiply(t.c);
//            Rational expression2 = Rational.add(t.a.multiply(t.c), t.b.multiply(t.c));
//            Assert.assertEquals(t.toString(), expression1, expression2);
//        }
//    }
//
//    public static void multiplyIntProperties(int limit) {
//        System.out.println("testing multiply(int) properties...");
//        Iterable<Pair<Rational, Integer>> g = new PairIterable<>(RATIONALS, P.integers());
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational product = p.a.multiply(p.b);
//            validate(product);
//            Assert.assertEquals(p.toString(), product, Rational.multiply(Rational.of(p.b), p.a));
//        }
//
//        for (int i : P.integers().iterate(limit)) {
//            Assert.assertEquals(Integer.toString(i), Rational.ONE.multiply(i), Rational.of(i));
//            Assert.assertTrue(Integer.toString(i), Rational.ZERO.multiply(i) == Rational.ZERO);
//        }
//
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Assert.assertEquals(r.toString(), r.multiply(1), r);
//            Assert.assertTrue(r.toString(), r.multiply(0) == Rational.ZERO);
//        }
//
//        Iterable<Integer> ig = new FilteredIterable<>(
//                P.integers(),
//                bi -> bi != 0
//        );
//        for (int i : ig.iterate(limit)) {
//            Assert.assertTrue(Integer.toString(i), Rational.of(i).invert().multiply(i) == Rational.ONE);
//        }
//
//        Iterable<Rational> rg = RATIONALS;
//        for (Triple<Rational, Rational, Integer> t : new TripleIterable<>(rg, rg, P.integers()).iterate(limit)) {
//            Rational expression1 = Rational.add(t.a, t.b).multiply(t.c);
//            Rational expression2 = Rational.add(t.a.multiply(t.c), t.b.multiply(t.c));
//            Assert.assertEquals(t.toString(), expression1, expression2);
//        }
//    }
//
//    public static void divideRationalRationalProperties(int limit) {
//        System.out.println("testing divide(Rational, Rational) properties...");
//        Iterable<Pair<Rational, Rational>> g = new FilteredIterable<Pair<Rational, Rational>>(
//                P.pairs(RATIONALS),
//                p -> p.b != Rational.ZERO
//        );
//        for (Pair<Rational, Rational> p : g.iterate(limit)) {
//            Rational quotient = Rational.divide(p.a, p.b);
//            validate(quotient);
//            Assert.assertEquals(p.toString(), p.a, Rational.multiply(quotient, p.b));
//        }
//
//        g = new FilteredIterable<Pair<Rational, Rational>>(
//                P.pairs(RATIONALS),
//                p -> p.a != Rational.ZERO && p.b != Rational.ZERO
//        );
//        for (Pair<Rational, Rational> p : g.iterate(limit)) {
//            Assert.assertEquals(p.toString(), Rational.divide(p.a, p.b), Rational.divide(p.b, p.a).invert());
//        }
//
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Assert.assertEquals(r.toString(), Rational.divide(r, Rational.ONE), r);
//        }
//
//        Iterable<Rational> rg = new FilteredIterable<Rational>(
//                RATIONALS,
//                r -> r != Rational.ZERO
//        );
//        for (Rational r : rg.iterate(limit)) {
//            Assert.assertEquals(r.toString(), Rational.divide(Rational.ONE, r), r.invert());
//            Assert.assertTrue(r.toString(), Rational.divide(r, r) == Rational.ONE);
//        }
//    }

//    public static void divideBigIntegerProperties(int limit) {
//        Iterable<Pair<Rational, BigInteger>> g = new PairIterable<>(
//                RATIONALS,
//                new FilteredIterable<BigInteger>(
//                        P.bigIntegers(),
//                        bi -> !bi.equals(BigInteger.ZERO)
//                )
//        );
//        for (Pair<Rational, BigInteger> p : g.iterate(limit)) {
//            Rational quotient = p.a.divide(p.b);
//            validate(quotient);
//            Assert.assertEquals(p.toString(), p.a, quotient.multiply(p.b));
//            if (p.a == Rational.ONE) {
//                Assert.assertEquals(p.toString(), quotient, Rational.of(p.b).invert());
//            }
//            if (p.b.equals(BigInteger.ONE)) {
//                Assert.assertEquals(p.toString(), quotient, p.a);
//            }
//            if (p.a.equals(Rational.of(p.b))) {
//                Assert.assertTrue(p.toString(), quotient == Rational.ONE);
//            }
//        }
//
//        g = new PairIterable<>(
//                new FilteredIterable<Rational>(
//                        RATIONALS,
//                        r -> r != Rational.ZERO
//                ),
//                new FilteredIterable<BigInteger>(
//                        P.bigIntegers(),
//                        bi -> !bi.equals(BigInteger.ZERO)
//                )
//        );
//        for (Pair<Rational, BigInteger> p : g.iterate(limit)) {
//            Assert.assertEquals(p.toString(), p.a.divide(p.b), Rational.divide(Rational.of(p.b), p.a).invert());
//        }
//    }
//
//    public static void divideIntProperties(int limit) {
//        Iterable<Pair<Rational, Integer>> g = new PairIterable<>(
//                RATIONALS, new FilteredIterable<Integer>(P.integers(), i -> i != 0)
//        );
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational quotient = p.a.divide(p.b);
//            validate(quotient);
//            if (p.a != Rational.ZERO) {
//                Assert.assertEquals(p.toString(), quotient, Rational.divide(Rational.of(p.b), p.a).invert());
//            }
//            Assert.assertEquals(p.toString(), p.a, quotient.multiply(p.b));
//            if (p.a == Rational.ONE) {
//                Assert.assertEquals(p.toString(), quotient, Rational.of(p.b).invert());
//            }
//            if (p.b == 1) {
//                Assert.assertEquals(p.toString(), quotient, p.a);
//            }
//            if (p.a.equals(Rational.of(p.b))) {
//                Assert.assertTrue(p.toString(), quotient == Rational.ONE);
//            }
//        }
//    }
//
//    public static void powProperties1(int limit) {
//        Iterable<Pair<Rational, Integer>> g = new FilteredIterable<Pair<Rational, Integer>>(
//                new PairIterable<>(RATIONALS, P.integers()),
//                p -> p.b >= 0 || p.a != Rational.ZERO
//        );
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational r = p.a.pow(p.b);
//            validate(r);
//            if (p.a == Rational.ZERO && p.b != 0) {
//                Assert.assertTrue(p.toString(), r == Rational.ZERO);
//            }
//            if (p.b == 0) {
//                Assert.assertTrue(p.toString(), r == Rational.ONE);
//            }
//            if (p.b == 1) {
//                Assert.assertEquals(p.toString(), r, p.a);
//            }
//            if (p.b == -1) {
//                Assert.assertEquals(p.toString(), r, p.a.invert());
//            }
//            if (p.b == 2) {
//                Assert.assertEquals(p.toString(), r, Rational.multiply(p.a, p.a));
//            }
//            if (p.a != Rational.ZERO) {
//                Assert.assertEquals(p.toString(), r, p.a.pow(-p.b).invert());
//                Assert.assertEquals(p.toString(), r, p.a.invert().pow(-p.b));
//            }
//        }
//    }
//
//    public static void powProperties2(int limit) {
//        Iterable<Integer> ig = P.integers();
//        for (Triple<Rational, Integer, Integer> t : new TripleIterable<>(RATIONALS, ig, ig).iterate(limit)) {
//            if (t.a != Rational.ZERO || t.b >= 0 && t.c >= 0) {
//                Rational expression1 = Rational.multiply(t.a.pow(t.b), t.a.pow(t.c));
//                Rational expression2 = t.a.pow(t.b + t.c);
//                Assert.assertEquals(t.toString(), expression1, expression2);
//                if (t.a != Rational.ZERO || t.c == 0) {
//                    Rational expression3 = Rational.divide(t.a.pow(t.b), t.a.pow(t.c));
//                    Rational expression4 = t.a.pow(t.b - t.c);
//                    Assert.assertEquals(t.toString(), expression3, expression4);
//                }
//                Rational expression5 = t.a.pow(t.b).pow(t.c);
//                Rational expression6 = t.a.pow(t.b * t.c);
//                Assert.assertEquals(t.toString(), expression5, expression6);
//            }
//        }
//    }
//
//    public static void powProperties3(int limit) {
//        Iterable<Rational> rg = RATIONALS;
//        for (Triple<Rational, Rational, Integer> t : new TripleIterable<>(rg, rg, P.integers()).iterate(limit)) {
//            if (t.a != Rational.ZERO && t.b != Rational.ZERO || t.c >= 0) {
//                Rational expression1 = Rational.multiply(t.a, t.b).pow(t.c);
//                Rational expression2 = Rational.multiply(t.a.pow(t.c), t.b.pow(t.c));
//                Assert.assertEquals(t.toString(), expression1, expression2);
//            }
//            if (t.b != Rational.ZERO && (t.a != Rational.ZERO || t.c >= 0)) {
//                Rational expression3 = Rational.divide(t.a, t.b).pow(t.c);
//                Rational expression4 = Rational.divide(t.a.pow(t.c), t.b.pow(t.c));
//                Assert.assertEquals(t.toString(), expression3, expression4);
//            }
//        }
//    }
//
//    public static void floorProperties(int limit) {
//        for (Rational r : RATIONALS.iterate(limit)) {
//            BigInteger floor = r.floor();
//            Assert.assertTrue(r.toString(), Rational.of(floor).compareTo(r) <= 0);
//            Assert.assertTrue(r.toString(), Rational.subtract(r, Rational.of(floor)).compareTo(Rational.ONE) <= 0);
//        }
//    }
//
//    public static void ceilingProperties(int limit) {
//        for (Rational r : RATIONALS.iterate(limit)) {
//            BigInteger ceiling = r.ceiling();
//            Assert.assertTrue(r.toString(), Rational.of(ceiling).compareTo(r) >= 0);
//            Assert.assertTrue(r.toString(), Rational.subtract(Rational.of(ceiling), r).compareTo(Rational.ONE) <= 0);
//        }
//    }
//
//    public static void fractionalPartProperties(int limit) {
//        for (Rational r : RATIONALS.iterate(limit)) {
//            Rational fractionalPart = r.fractionalPart();
//            validate(fractionalPart);
//            Assert.assertTrue(r.toString(), fractionalPart.compareTo(Rational.ZERO) >= 0);
//            Assert.assertTrue(r.toString(), fractionalPart.compareTo(Rational.ONE) < 0);
//            Assert.assertEquals(r.toString(), Rational.add(Rational.of(r.floor()), fractionalPart), r);
//        }
//    }
//
//    public static void roundProperties(int limit) {
//        Iterable<Pair<Rational, RoundingMode>> g = new FilteredIterable<Pair<Rational, RoundingMode>>(
//                new PairIterable<>(RATIONALS, Iterables.roundingModes()),
//                p -> p.b != RoundingMode.UNNECESSARY || p.a.getDenominator.equals(BigInteger.ONE));
//        for (Pair<Rational, RoundingMode> p : g.iterate(limit)) {
//            BigInteger rounded = p.a.round(p.b);
//            Assert.assertTrue(p.toString(), rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
//            Assert.assertTrue(p.toString(), Rational.subtract(p.a, Rational.of(rounded)).abs().compareTo(Rational.ONE) < 0);
//            if (p.b == RoundingMode.UNNECESSARY) {
//                Assert.assertEquals(p.toString(), p.a.getNumerator(), rounded);
//                Assert.assertEquals(p.toString(), p.a.getDenominator, BigInteger.ONE);
//            }
//            if (p.b == RoundingMode.FLOOR) {
//                Assert.assertEquals(p.toString(), rounded, p.a.floor());
//            }
//            if (p.b == RoundingMode.CEILING) {
//                Assert.assertEquals(p.toString(), rounded, p.a.ceiling());
//            }
//            if (p.b == RoundingMode.DOWN) {
//                Assert.assertTrue(p.toString(), Rational.of(rounded).abs().compareTo(p.a.abs()) <= 0);
//            }
//            if (p.b == RoundingMode.UP) {
//                Assert.assertTrue(p.toString(), Rational.of(rounded).abs().compareTo(p.a.abs()) >= 0);
//            }
//            if (p.b == RoundingMode.HALF_DOWN || p.b == RoundingMode.HALF_UP || p.b == RoundingMode.HALF_EVEN) {
//                Assert.assertTrue(p.toString(), Rational.subtract(p.a, Rational.of(rounded)).abs().compareTo(Rational.of(1, 2)) <= 0);
//                Rational fractionalPart = p.a.abs().fractionalPart();
//                if (fractionalPart.compareTo(Rational.of(1, 2)) < 0) {
//                    Assert.assertEquals(p.toString(), rounded, p.a.round(RoundingMode.DOWN));
//                } else if (fractionalPart.compareTo(Rational.of(1, 2)) > 0) {
//                    Assert.assertEquals(p.toString(), rounded, p.a.round(RoundingMode.UP));
//                } else {
//                    if (p.b == RoundingMode.HALF_DOWN) {
//                        Assert.assertEquals(p.toString(), rounded, p.a.round(RoundingMode.DOWN));
//                    }
//                    if (p.b == RoundingMode.HALF_UP) {
//                        Assert.assertEquals(p.toString(), rounded, p.a.round(RoundingMode.UP));
//                    }
//                    if (p.b == RoundingMode.HALF_EVEN) {
//                        Assert.assertEquals(p.toString(), rounded.testBit(0), false);
//                    }
//                }
//            }
//        }
//    }
//
//    public static void roundToDenominatorProperties(int limit) {
//        Iterable<Triple<Rational, BigInteger, RoundingMode>> g =
//                new FilteredIterable<Triple<Rational, BigInteger, RoundingMode>>(
//                        new TripleIterable<>(RATIONALS, Iterables.positiveBigIntegers(), Iterables.roundingModes()),
//                        p -> p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator).equals(BigInteger.ZERO));
//        for (Triple<Rational, BigInteger, RoundingMode> t : g.iterate(limit)) {
//            Rational rounded = t.a.roundToDenominator(t.b, t.c);
//            Assert.assertTrue(t.toString(), rounded == Rational.ZERO || rounded.signum() == t.a.signum());
//            if (t.b.equals(BigInteger.ONE)) {
//                Assert.assertEquals(t.toString(), rounded.getNumerator(), t.a.round(t.c));
//                Assert.assertEquals(t.toString(), rounded.getDenominator, BigInteger.ONE);
//            }
//            Assert.assertTrue(t.toString(), Rational.subtract(t.a, rounded).abs().compareTo(Rational.of(BigInteger.ONE, t.b)) < 0);
//            if (t.c == RoundingMode.UNNECESSARY) {
//                Assert.assertEquals(t.toString(), t.a.multiply(t.b).getDenominator, BigInteger.ONE);
//            }
//            if (t.c == RoundingMode.FLOOR) {
//                Assert.assertTrue(t.toString(), rounded.compareTo(t.a) <= 0);
//            }
//            if (t.c == RoundingMode.CEILING) {
//                Assert.assertTrue(t.toString(), rounded.compareTo(t.a) >= 0);
//            }
//            if (t.c == RoundingMode.DOWN) {
//                Assert.assertTrue(t.toString(), rounded.abs().compareTo(t.a.abs()) <= 0);
//            }
//            if (t.c == RoundingMode.UP) {
//                Assert.assertTrue(t.toString(), rounded.abs().compareTo(t.a.abs()) >= 0);
//            }
//            if (t.c == RoundingMode.HALF_DOWN || t.c == RoundingMode.HALF_UP || t.c == RoundingMode.HALF_EVEN) {
//                Assert.assertTrue(t.toString(), Rational.subtract(t.a, rounded).abs().compareTo(Rational.of(t.b).multiply(2).invert()) <= 0);
//                Rational fractionalPart = t.a.abs().multiply(t.b).fractionalPart();
//                if (fractionalPart.compareTo(Rational.of(1, 2)) < 0) {
//                    Assert.assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.DOWN));
//                } else if (fractionalPart.compareTo(Rational.of(1, 2)) > 0) {
//                    Assert.assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.UP));
//                } else {
//                    if (t.c == RoundingMode.HALF_DOWN) {
//                        Assert.assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.DOWN));
//                    }
//                    if (t.c == RoundingMode.HALF_UP) {
//                        Assert.assertEquals(t.toString(), rounded, t.a.roundToDenominator(t.b, RoundingMode.UP));
//                    }
//                    if (t.c == RoundingMode.HALF_EVEN) {
//                        Assert.assertEquals(t.toString(), rounded.multiply(t.b).getNumerator().testBit(0), false);
//                    }
//                }
//            }
//        }
//    }
//
//    public static void shiftLeftProperties(int limit) {
//        Iterable<Pair<Rational, Integer>> g = new PairIterable<>(RATIONALS, P.integers());
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational shifted = p.a.shiftLeft(p.b);
//            validate(shifted);
//            Assert.assertEquals(p.toString(), p.a.negate().shiftLeft(p.b), shifted.negate());
//            Assert.assertEquals(p.toString(), shifted, p.a.shiftRight(-p.b));
//            if (p.b >= 0) {
//                Assert.assertEquals(p.toString(), shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
//            }
//        }
//    }
//
//    public static void shiftRightProperties(int limit) {
//        Iterable<Pair<Rational, Integer>> g = new PairIterable<>(RATIONALS, P.integers());
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            Rational shifted = p.a.shiftRight(p.b);
//            validate(shifted);
//            Assert.assertEquals(p.toString(), p.a.negate().shiftRight(p.b), shifted.negate());
//            Assert.assertEquals(p.toString(), shifted, p.a.shiftLeft(-p.b));
//            if (p.b >= 0) {
//                Assert.assertEquals(p.toString(), shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
//            }
//        }
//    }
//
//    public static void binaryExponentProperties(int limit) {
//        for (Rational r : Rational.positiveRationals().iterate(limit)) {
//            int exponent = r.binaryExponent();
//            Rational power = Rational.ONE.shiftLeft(exponent);
//            Assert.assertTrue(r.toString(), power.compareTo(r) <= 0);
//            Assert.assertTrue(r.toString(), r.compareTo(power.shiftLeft(1)) < 0);
//        }
//    }
//
//    public static void toFloatProperties(int limit) {
//        for (Rational r : RATIONALS.iterate(limit)) {
//            float f = r.toFloat();
//            Assert.assertEquals(r.toString(), f, r.toFloat(RoundingMode.HALF_EVEN));
//        }
//    }
//
//    public static void toFloatRoundingModeProperties(int limit) {
//        Iterable<Pair<Rational, RoundingMode>> g = new FilteredIterable<Pair<Rational, RoundingMode>>(
//                new PairIterable<>(RATIONALS, Iterables.roundingModes()),
//                p -> p.b != RoundingMode.UNNECESSARY
//                        || Rational.of(p.a.toFloat(RoundingMode.FLOOR)).equals(p.a));
//        for (Pair<Rational, RoundingMode> p : g.iterate(limit)) {
//            float rounded = p.a.toFloat(p.b);
//            Assert.assertTrue(p.toString(), !Float.isNaN(rounded));
//            Assert.assertTrue(p.toString(), rounded == 0.0 || Math.signum(rounded) == p.a.signum());
//            float successor = MathUtils.successor(rounded);
//            float predecessor = MathUtils.predecessor(rounded);
//            float up = p.a.signum() == -1 ? predecessor : successor;
//            float down = p.a.signum() == -1 ? successor : predecessor;
//            if (p.b == RoundingMode.UNNECESSARY) {
//                Assert.assertEquals(p.toString(), p.a, Rational.of(rounded));
//                Assert.assertTrue(p.toString(), Float.isFinite(rounded));
//                Assert.assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.FLOOR) {
//                Assert.assertTrue(p.toString(), Rational.of(rounded).compareTo(p.a) <= 0);
//                Assert.assertTrue(p.toString(), Rational.of(successor).compareTo(p.a) > 0);
//                if (p.a.compareTo(Rational.ZERO) >= 0 && p.a.compareTo(Rational.SMALLEST_FLOAT) < 0) {
//                    Assert.assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT.negate()) < 0) {
//                    Assert.assertTrue(p.toString(), rounded < 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT) >= 0) {
//                    Assert.assertEquals(p.toString(), rounded, Float.MAX_VALUE);
//                }
//                Assert.assertTrue(p.toString(), rounded < 0 || Float.isFinite(rounded));
//                Assert.assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.CEILING) {
//                Assert.assertTrue(p.toString(), Rational.of(rounded).compareTo(p.a) >= 0);
//                Assert.assertTrue(p.toString(), Rational.of(predecessor).compareTo(p.a) < 0);
//                if (p.a == Rational.ZERO) {
//                    Assert.assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(Rational.ZERO) < 0 && p.a.compareTo(Rational.SMALLEST_FLOAT.negate()) > 0) {
//                    Assert.assertEquals(p.toString(), rounded, -0.0f);
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT) > 0) {
//                    Assert.assertTrue(p.toString(), rounded > 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT.negate()) <= 0) {
//                    Assert.assertEquals(p.toString(), rounded, -Float.MAX_VALUE);
//                }
//                Assert.assertTrue(p.toString(), rounded > 0 || Float.isFinite(rounded));
//            }
//            if (p.b == RoundingMode.DOWN) {
//                Assert.assertTrue(p.toString(), Rational.of(rounded).abs().compareTo(p.a.abs()) <= 0);
//                Assert.assertTrue(p.toString(), Rational.of(up).abs().compareTo(p.a.abs()) > 0);
//                if (p.a.compareTo(Rational.ZERO) >= 0 && p.a.compareTo(Rational.SMALLEST_FLOAT) < 0) {
//                    Assert.assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(Rational.ZERO) < 0 && p.a.compareTo(Rational.SMALLEST_FLOAT.negate()) > 0) {
//                    Assert.assertEquals(p.toString(), rounded, -0.0f);
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT) >= 0) {
//                    Assert.assertEquals(p.toString(), rounded, Float.MAX_VALUE);
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT.negate()) <= 0) {
//                    Assert.assertEquals(p.toString(), rounded, -Float.MAX_VALUE);
//                }
//                Assert.assertTrue(p.toString(), Float.isFinite(rounded));
//            }
//            if (p.b == RoundingMode.UP) {
//                Assert.assertTrue(p.toString(), Rational.of(rounded).abs().compareTo(p.a.abs()) >= 0);
//                if (p.a != Rational.ZERO) {
//                    Assert.assertTrue(p.toString(), Rational.of(down).abs().compareTo(p.a.abs()) < 0);
//                }
//                if (p.a == Rational.ZERO) {
//                    Assert.assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT) > 0) {
//                    Assert.assertTrue(p.toString(), rounded > 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(Rational.LARGEST_FLOAT.negate()) < 0) {
//                    Assert.assertTrue(p.toString(), rounded < 0 && Float.isInfinite(rounded));
//                }
//                Assert.assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.HALF_DOWN || p.b == RoundingMode.HALF_UP || p.b == RoundingMode.HALF_EVEN) {
//                boolean equidistant = false;
//                if (Float.isFinite(rounded) && rounded != Float.MAX_VALUE && rounded != -Float.MAX_VALUE) {
//                    Rational distance = Rational.subtract(Rational.of(rounded), p.a).abs();
//                    Rational predDistance = Rational.subtract(Rational.of(predecessor), p.a).abs();
//                    Rational succDistance = Rational.subtract(Rational.of(successor), p.a).abs();
//                    Assert.assertEquals(p.toString(), Rational.min(distance, predDistance, succDistance), distance);
//                    if (distance.equals(predDistance) || distance.equals(succDistance)) {
//                        equidistant = true;
//                    }
//                }
//                //TODO
//                if (p.b == RoundingMode.HALF_DOWN) {
//                    if (equidistant) {
//                        Assert.assertEquals(p.toString(), rounded, p.a.toFloat(RoundingMode.DOWN));
//                    }
//                } else if (p.b == RoundingMode.HALF_UP) {
//                    if (equidistant) {
//                        Assert.assertEquals(p.toString(), rounded, p.a.toFloat(RoundingMode.UP));
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
        assertEquals(message, Float.floatToIntBits(f1), Float.floatToIntBits(f2));
    }

    private static void dae(String message, double d1, double d2) {
        assertEquals(message, Double.doubleToLongBits(d1), Double.doubleToLongBits(d2));
    }
}
