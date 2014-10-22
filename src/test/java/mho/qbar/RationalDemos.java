package mho.qbar;

import mho.haskellesque.iterables.ExhaustiveProvider;
import mho.haskellesque.iterables.IterableProvider;
import mho.haskellesque.iterables.RandomProvider;
import mho.haskellesque.structures.Pair;
import mho.haskellesque.structures.Triple;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

import static mho.haskellesque.iterables.IterableUtils.*;

public class RationalDemos {
    private static final boolean USE_RANDOM = false;
    private static final IterableProvider P = USE_RANDOM ?
            new RandomProvider(new Random(7706916639046193098L)) :
            new ExhaustiveProvider();

    private static Iterable<Rational> RATIONALS = USE_RANDOM ?
            Rational.RATIONALS :
            Rational.RATIONALS;  //todo
    
    private static final String NECESSARY_CHARS = "-/0123456789";
    private static final int LIMIT = 10000;

    public static void ofBigIntegerBigIntegerDemo() {
        Iterable<Pair<BigInteger, BigInteger>> it = filter(
                p -> !p.b.equals(BigInteger.ZERO),
                P.pairs(P.bigIntegers())
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, it)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + Rational.of(p.a, p.b));
        }
    }

    public static void ofIntIntDemo() {
        Iterable<Pair<Integer, Integer>> it = filter(p -> p.b != 0, P.pairs(P.integers()));
        for (Pair<Integer, Integer> p : take(LIMIT, it)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + Rational.of(p.a, p.b));
        }
    }

    public static void ofBigIntegerDemo() {
        for (BigInteger bi : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + bi + ") = " + Rational.of(bi));
        }
    }

    public static void ofIntDemo() {
        for (int bi : take(LIMIT, P.integers())) {
            System.out.println("of(" + bi + ") = " + Rational.of(bi));
        }
    }

    public static void ofFloatDemo() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + Rational.of(f));
        }
    }

    public static void ofDoubleDemo() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + Rational.of(d));
        }
    }

//    public static void ofBigDecimalDemo() {
//        for (BigDecimal bd : Generators.bigDecimals().iterate(limit)) {
//            System.out.println("of(" + bd + ") = " + Rational.of(bd));
//        }
//    }

    public static void negateDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("-(" + r + ") = " + r.negate());
        }
    }

    public static void invertDemo() {
        for (Rational r : take(LIMIT, filter(s -> s != Rational.ZERO, RATIONALS))) {
            System.out.println("1/(" + r + ") = " + r.invert());
        }
    }

    public static void absDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("|" + r + "| = " + r.abs());
        }
    }

    public static void signumDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("sgn(" + r + ") = " + r.signum());
        }
    }

    public static void addDemo() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(RATIONALS))) {
            System.out.println(p.a + " + " + p.b + " = " + Rational.add(p.a, p.b));
        }
    }

    public static void subtractDemo() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(RATIONALS))) {
            System.out.println(p.a + " - " + p.b + " = " + Rational.subtract(p.a, p.b));
        }
    }

    public static void multiplyRationalRationalDemo() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(RATIONALS))) {
            System.out.println(p.a + " * " + p.b + " = " + Rational.multiply(p.a, p.b));
        }
    }

    public static void multiplyBigIntegerDemo() {
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(RATIONALS, P.bigIntegers()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void multiplyIntDemo() {
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(RATIONALS, P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    public static void divideRationalRationalDemo() {
        Iterable<Pair<Rational, Rational>> it = filter(p -> p.b != Rational.ZERO, P.pairs(RATIONALS));
        for (Pair<Rational, Rational> p : take(LIMIT, it)) {
            System.out.println(p.a + " / " + p.b + " = " + Rational.divide(p.a, p.b));
        }
    }

    public static void divideBigIntegerDemo() {
        Iterable<Pair<Rational, BigInteger>> it = P.pairs(
                RATIONALS,
                filter(bi -> !bi.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, it)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void divideIntDemo() {
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(RATIONALS, filter(i -> i != 0, P.integers())))) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    public static void powDemo() {
        Iterable<Pair<Rational, Integer>> it = filter(
                p -> p.b >= 0 || p.a != Rational.ZERO,
                P.pairs(RATIONALS, P.integers())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, it)) {
            assert p.a != null;
            assert p.b != null;
            System.out.println(p.a + " ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    public static void floorDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("floor(" + r + ") = " + r.floor());
        }
    }

    public static void ceilingDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("ceil(" + r + ") = " + r.ceiling());
        }
    }

    public static void fractionalPartDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("fractionalPart(" + r + ") = " + r.fractionalPart());
        }
    }

    public static void roundDemo() {
        Iterable<Pair<Rational, RoundingMode>> it = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.getDenominator().equals(BigInteger.ONE),
                P.pairs(RATIONALS, P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, it)) {
            System.out.println("round(" + p.a + ", " + p.b + ") = " + p.a.round(p.b));
        }
    }

    public static void roundToDenominatorDemo() {
        Iterable<Triple<Rational, BigInteger, RoundingMode>> it = filter(
                p -> p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.triples(RATIONALS, P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, it)) {
            System.out.println("round(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.roundToDenominator(t.b, t.c));
        }
    }

    public static void shiftLeftDemo() {
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(RATIONALS, P.integers()))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    public static void shiftRightDemo() {
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(RATIONALS, P.integers()))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    public static void binaryExponentDemo() {
        for (Rational r : take(LIMIT, Rational.POSITIVE_RATIONALS)) {
            System.out.println("binaryExponent(" + r + ") = " + r.binaryExponent());
        }
    }

    public static void toFloatDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("toFloat(" + r + ") = " + r.toFloat());
        }
    }

    public static void toFloatRoundingModeDemo() {
        Iterable<Pair<Rational, RoundingMode>> it = filter(
                p -> p.b != RoundingMode.UNNECESSARY || Rational.of(p.a.toFloat(RoundingMode.FLOOR)).equals(p.a),
                P.pairs(RATIONALS, P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, it)) {
            System.out.println("toFloat(" + p.a + ", " + p.b + ") = " + p.a.toFloat(p.b));
        }
    }

    public static void toDoubleDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("toDouble(" + r + ") = " + r.toDouble());
        }
    }

    public static void toDoubleRoundingModeDemo() {
        Iterable<Pair<Rational, RoundingMode>> it = filter(
                p -> p.b != RoundingMode.UNNECESSARY || Rational.of(p.a.toFloat(RoundingMode.FLOOR)).equals(p.a),
                P.pairs(RATIONALS, P.roundingModes()));
        for (Pair<Rational, RoundingMode> p : take(LIMIT, it)) {
            System.out.println("toDouble(" + p.a + ", " + p.b + ") = " + p.a.toDouble(p.b));
        }
    }

    public static void hasTerminatingDecimalExpansionDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("hasTerminatingDecimalExpansion(" + r + ") = " + r.hasTerminatingDecimalExpansion());
        }
    }

    public static void toBigDecimalDemo() {
        Iterable<Rational> it = filter(Rational::hasTerminatingDecimalExpansion, RATIONALS);
        for (Rational r : take(LIMIT, it)) {
            System.out.println("toBigDecimal(" + r + ") = " + r.toBigDecimal());
        }
    }

//    public static void toBigDecimalPrecisionDemo() {
//        Iterable<Pair<Rational, Integer>> g = filter(
//                new SubExponentialPairGenerator<>(
//                        RATIONALS,
//                        Generators.naturalIntegers()
//                ),
//                p -> p.b != 0 || p.a.hasTerminatingDecimalExpansion());
//        for (Pair<Rational, Integer> p : g.iterate(limit)) {
//            System.out.println("toBigDecimal(" + p.a + ", " + p.b + ") = " + p.a.toBigDecimal(p.b));
//        }
//    }

//    public static void toBigDecimalPrecisionRoundingMode() {
//        Generator<Pair<Rational, Integer>> pg = new FilteredGenerator<Pair<Rational, Integer>>(
//                new SubExponentialPairGenerator<>(
//                        RATIONALS,
//                        Generators.naturalIntegers()
//                ),
//                p -> p.b != 0 || p.a.hasTerminatingDecimalExpansion());
//        Generator<Pair<Pair<Rational, Integer>, RoundingMode>> g = new FilteredGenerator<Pair<Pair<Rational,Integer>, RoundingMode>>(
//                pairs(pg, Generators.roundingModes()),
//                p -> p.b != RoundingMode.UNNECESSARY ||
//                        (p.a.a.hasTerminatingDecimalExpansion() &&
//                                p.a.b >= p.a.a.numberOfDecimalDigits()));
//        for (Pair<Pair<Rational, Integer>, RoundingMode> p : g.iterate(limit)) {
//            System.out.println("toBigDecimal(" + p.a.a + ", " + p.a.b + ", " + p.b + ") = " +
//                    p.a.a.toBigDecimal(p.a.b, p.b));
//        }
//    }

    public static void equalsRationalDemo() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(RATIONALS))) {
            System.out.println("equals(" + p.a + ", " + p.b + ") = " + p.a.equals(p.b));
        }
    }

    public static void equalsNullDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            //noinspection ObjectEqualsNull
            System.out.println("equals(" + r + ", null) = " + r.equals(null));
        }
    }

    public static void hashCodeDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("hashCode(" + r + ") = " + r.hashCode());
        }
    }

    public static void compareToDemo() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(RATIONALS))) {
            System.out.println("compareTo(" + p.a + ", " + p.b + ") = " + p.a.compareTo(p.b));
        }
    }

    public static void continuedFractionDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("continuedFraction(" + r + ") = " + r.continuedFraction());
        }
    }

//    public static void fromContinuedFractionDemo() {
//        Iterable<List<BigInteger>> it = pairs(
//                P.bigIntegers(),
//                new ListGenerator<BigInteger>(POSITIVE_BIG_INTEGERS)
//        ).map(
//                p -> {
//                    List<BigInteger> bis = new ArrayList<>();
//                    bis.add(p.a);
//                    bis.addAll(p.b);
//                    return bis;
//                },
//                bis -> {
//                    List<BigInteger> tail = new ArrayList<>();
//                    for (int i = 1; i < bis.size(); i++) {
//                        tail.add(bis.get(i));
//                    }
//                    return Pair.of(bis.get(0), tail);
//                },
//                bis -> {
//                    if (bis.isEmpty()) return false;
//                    for (int i = 1; i < bis.size(); i++) {
//                        if (bis.get(i).signum() != 1) return false;
//                    }
//                    return true;
//                }
//        );
//        for (List<BigInteger> bis : bisg.iterate(limit)) {
//            String bisString = bis.toString();
//            bisString = bisString.substring(1, bisString.length() - 1);
//            System.out.println("fromContinuedFraction(" + bisString + ") = " + Rational.fromContinuedFraction(bis));
//        }
//    }

    public static void convergentsDemo() {
        for (Rational r : take(LIMIT, RATIONALS)) {
            System.out.println("convergents(" + r + ") = " + r.convergents());
        }
    }

//    public static void positionalNotationDemo() {
//        Generator<BigInteger> big = new FilteredGenerator<>(
//                POSITIVE_BIG_INTEGERS,
//                bi -> bi.compareTo(BigInteger.ONE) > 0
//        );
//        Generator<Pair<Rational, BigInteger>> g = new SubExponentialPairGenerator<>(Rational.nonnegativeRationals(), big);
//        for (Pair<Rational, BigInteger> p : g.iterate(limit)) {
//            System.out.println("positionalNotation(" + p.a + ", " + p.b + " = " + p.a.positionalNotation(p.b));
//        }
//    }
//
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

//    public static void readDemo() {
//        readDemo(limit, false, false);
//    }
//
//    public static void readDemo(, boolean necessaryChars, boolean skipNullResults) {
//        Generator<String> g = new FilteredGenerator<>(
//                (necessaryChars ? Generators.strings(NECESSARY_CHARS) : Generators.strings()),
//                s -> !s.endsWith("/0"));
//        for (String s : g.iterate(limit)) {
//            Rational r = Rational.read(s);
//            if (!skipNullResults || r != null) System.out.println("read(" + s + ") = " + r);
//        }
//    }
}
