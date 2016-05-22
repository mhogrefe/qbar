package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.qbar.testing.QBarTesting;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static mho.qbar.objects.Algebraic.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class AlgebraicDemos extends QBarDemos {
    private static final @NotNull String ALGEBRAIC_CHARS = " ()*+-/0123456789^foqrstx";

    public AlgebraicDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoOf_Polynomial_int() {
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).polynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_long() {
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("of(" + l + ") = " + of(l));
        }
    }

    private void demoOf_int() {
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_BinaryFraction() {
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            System.out.println("of(" + bf + ") = " + of(bf));
        }
    }

    private void demoOf_float() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + of(f));
        }
    }

    private void demoOf_double() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + of(d));
        }
    }

    private void demoOfExact_float() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("ofExact(" + f + ") = " + ofExact(f));
        }
    }

    private void demoOfExact_double() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("ofExact(" + d + ") = " + ofExact(d));
        }
    }

    private void demoOf_BigDecimal() {
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("of(" + bd + ") = " + of(bd));
        }
    }

    private void demoIsInteger() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x + " is " + (x.isInteger() ? "" : "not ") + "an integer");
        }
    }

    private void demoBigIntegerValue_RoundingMode() {
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.withScale(4).algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("bigIntegerValue(" + p.a + ", " + p.b + ") = " + p.a.bigIntegerValue(p.b));
        }
    }

    private void demoBigIntegerValue() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("bigIntegerValue(" + x + ") = " + x.bigIntegerValue());
        }
    }

    private void demoFloor() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("floor(" + x + ") = " + x.floor());
        }
    }

    private void demoCeiling() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("ceiling(" + x + ") = " + x.ceiling());
        }
    }

    private void demoBigIntegerValueExact() {
        for (Algebraic x : take(LIMIT, map(Algebraic::of, P.bigIntegers()))) {
            System.out.println("bigIntegerValueExact(" + x + ") = " + x.bigIntegerValueExact());
        }
    }

    private void demoByteValueExact() {
        for (Algebraic x : take(LIMIT, map(Algebraic::of, P.bytes()))) {
            System.out.println("byteValueExact(" + x + ") = " + x.byteValueExact());
        }
    }

    private void demoShortValueExact() {
        for (Algebraic x : take(LIMIT, map(Algebraic::of, P.shorts()))) {
            System.out.println("shortValueExact(" + x + ") = " + x.shortValueExact());
        }
    }

    private void demoIntValueExact() {
        for (Algebraic x : take(LIMIT, map(Algebraic::of, P.integers()))) {
            System.out.println("intValueExact(" + x + ") = " + x.intValueExact());
        }
    }

    private void demoLongValueExact() {
        for (Algebraic x : take(LIMIT, map(Algebraic::of, P.longs()))) {
            System.out.println("longValueExact(" + x + ") = " + x.longValueExact());
        }
    }

    private void demoIsIntegerPowerOfTwo() {
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            System.out.println(x + " is " + (x.isIntegerPowerOfTwo() ? "an" : "not an") + " integer power of 2");
        }
    }

    private void demoRoundUpToIntegerPowerOfTwo() {
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            System.out.println("roundUpToIntegerPowerOfTwo(" + x + ") = " + x.roundUpToIntegerPowerOfTwo());
        }
    }

    private void demoIsBinaryFraction() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x + " is " + (x.isBinaryFraction() ? "a" : "not a") + " binary fraction");
        }
    }

    private void demoBinaryFractionValueExact() {
        for (Algebraic x : take(LIMIT, map(bf -> of(Rational.of(bf)), P.binaryFractions()))) {
            System.out.println("binaryFractionValueExact(" + x + ") = " + x.binaryFractionValueExact());
        }
    }

    private void demoIsRational() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x + " is " + (x.isRational() ? "rational" : "irrational"));
        }
    }

    private void demoIsAlgebraicInteger() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x + " is " + (x.isAlgebraicInteger() ? "an" : "not an") + " algebraic integer");
        }
    }

    private void demoRationalValueExact() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics(1))) {
            System.out.println("rationalValueExact(" + x + ") = " + x.rationalValueExact());
        }
    }

    private void demoRealValue() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("realValue(" + x + ") = " + x.realValue());
        }
    }

    private void demoBinaryExponent() {
        for (Algebraic x : take(LIMIT, P.positiveAlgebraics())) {
            System.out.println("binaryExponent(" + x + ") = " + x.binaryExponent());
        }
    }

    private void demoIsEqualToFloat() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x + " is " + (x.isEqualToFloat() ? "" : "not ") + "equal to a float");
        }
    }

    private void demoIsEqualToDouble() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x + " is " + (x.isEqualToDouble() ? "" : "not ") + "equal to a double");
        }
    }

    private void demoFloatValue_RoundingMode() {
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToFloat(),
                P.pairs(P.withScale(4).algebraics(), P.roundingModes())
        );
        for (Pair<Algebraic, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("floatValue(" + p.a + ", " + p.b + ") = " + p.a.floatValue(p.b));
        }
    }

    private void demoFloatValue() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("floatValue(" + x + ") = " + x.floatValue());
        }
    }

    private void demoFloatValueExact() {
        Iterable<Algebraic> xs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f), P.floats())
        );
        for (Algebraic x : take(LIMIT, xs)) {
            System.out.println("floatValueExact(" + x + ") = " + x.floatValueExact());
        }
    }

    private void demoDoubleValue_RoundingMode() {
        Iterable<Pair<Algebraic, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToDouble(),
                P.pairs(P.withScale(4).algebraics(), P.roundingModes()));
        for (Pair<Algebraic, RoundingMode> p : take(LIMIT, ps)) {
            System.out.println("doubleValue(" + p.a + ", " + p.b + ") = " + p.a.doubleValue(p.b));
        }
    }

    private void demoDoubleValue() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("doubleValue(" + x + ") = " + x.doubleValue());
        }
    }

    private void demoDoubleValueExact() {
        Iterable<Algebraic> xs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !FloatingPointUtils.isNegativeZero(d), P.doubles())
        );
        for (Algebraic x : take(LIMIT, xs)) {
            System.out.println("doubleValueExact(" + x + ") = " + x.doubleValueExact());
        }
    }

    private void demoHasTerminatingBaseExpansion() {
        //noinspection Convert2MethodRef
        Iterable<Pair<Algebraic, BigInteger>> ps = P.pairs(
                P.withScale(4).algebraics(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.hasTerminatingBaseExpansion(p.b) ? " has " : " doesn't have ") +
                    "a terminating base-" + p.b + " expansion");
        }
    }

    private void demoBigDecimalValueByPrecision_int_RoundingMode() {
        Iterable<Triple<Algebraic, Integer, RoundingMode>> ts = filterInfinite(
                t -> {
                    try {
                        t.a.bigDecimalValueByPrecision(t.b, t.c);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.triples(P.withScale(4).algebraics(), P.naturalIntegersGeometric(), P.roundingModes())
        );
        for (Triple<Algebraic, Integer, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByPrecision(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByPrecision(t.b, t.c));
        }
    }

    private void demoBigDecimalValueByScale_int_RoundingMode() {
        Iterable<Triple<Algebraic, Integer, RoundingMode>> ts = filterInfinite(
                t -> t.c != RoundingMode.UNNECESSARY ||
                        t.a.isRational() && t.a.multiply(Rational.TEN.pow(t.b)).isInteger(),
                P.triples(P.withScale(4).algebraics(), P.integersGeometric(), P.roundingModes())
        );
        for (Triple<Algebraic, Integer, RoundingMode> t : take(LIMIT, ts)) {
            System.out.println("bigDecimalValueByScale(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.bigDecimalValueByScale(t.b, t.c));
        }
    }

    private void demoBigDecimalValueByPrecision_int() {
        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                p -> {
                    try {
                        p.a.bigDecimalValueByPrecision(p.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                P.pairsSquareRootOrder(P.withScale(4).algebraics(), P.naturalIntegersGeometric())
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            System.out.println("bigDecimalValueByPrecision(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByPrecision(p.b));
        }
    }

    private void demoBigDecimalValueByScale_int() {
        Iterable<Pair<Algebraic, Integer>> ps = P.pairsSquareRootOrder(
                P.withScale(4).algebraics(),
                P.integersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(LIMIT, ps)) {
            System.out.println("bigDecimalValueByScale(" + p.a + ", " + p.b + ") = " +
                    p.a.bigDecimalValueByScale(p.b));
        }
    }

    private void demoBigDecimalValueExact() {
        Iterable<Algebraic> xs = map(
                Algebraic::of,
                filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals())
        );
        for (Algebraic x : take(LIMIT, xs)) {
            System.out.println("bigDecimalValueExact(" + x + ") = " + x.bigDecimalValueExact());
        }
    }

    private void demoMinimalPolynomial() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("minimalPolynomial(" + x + ") = " + x.minimalPolynomial());
        }
    }

    private void demoRootIndex() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("rootIndex(" + x + ") = " + x.rootIndex());
        }
    }

    private void demoDegree() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("degree(" + x + ") = " + x.degree());
        }
    }

    private void demoIsolatingInterval() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("isolatingInterval(" + x + ") = " + x.isolatingInterval());
        }
    }

    private void demoMinimalPolynomialRootCount() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("minimalPolynomialRootCount(" + x + ") = " + x.minimalPolynomialRootCount());
        }
    }

    private void demoIntervalExtension() {
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.subsetPairs(P.withScale(4).algebraics()))) {
            System.out.println("intervalExtension(" + p.a + ", " + p.b + ") = " + intervalExtension(p.a, p.b));
        }
    }

    private void demoNegate() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("-(" + x + ") = " + x.negate());
        }
    }

    private void demoAbs() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("|" + x + "| = " + x.abs());
        }
    }

    private void demoSignum() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("signum(" + x + ") = " + x.signum());
        }
    }

    private void demoAdd_BigInteger() {
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoAdd_Rational() {
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.rationals()))) {
            System.out.println("(" + p.a + ") + " + p.b + " = " + p.a.add(p.b));
        }
    }

    private void demoAdd_Algebraic() {
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).withSecondaryScale(4).algebraics()))) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private void demoSubtract_BigInteger() {
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoSubtract_Rational() {
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.rationals()))) {
            System.out.println("(" + p.a + ") - " + p.b + " = " + p.a.subtract(p.b));
        }
    }

    private void demoSubtract_Algebraic() {
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).withSecondaryScale(4).algebraics()))) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.integers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.rationals()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Algebraic() {
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).withSecondaryScale(4).algebraics()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoInvert() {
        for (Algebraic x : take(LIMIT, P.withScale(4).nonzeroAlgebraics())) {
            System.out.println("1/(" + x + ") = " + x.invert());
        }
    }

    private void demoDivide_int() {
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.nonzeroIntegers()))) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<Algebraic, BigInteger>> ps = P.pairs(P.withScale(4).algebraics(), P.nonzeroBigIntegers());
        for (Pair<Algebraic, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_Rational() {
        for (Pair<Algebraic, Rational> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.nonzeroRationals()))) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_Algebraic() {
        Iterable<Pair<Algebraic, Algebraic>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).algebraics(),
                P.withScale(4).withSecondaryScale(4).nonzeroAlgebraics()
        );
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / (" + p.b + ") = " + p.a.divide(p.b));
        }
    }

    private void demoShiftLeft() {
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.integersGeometric()))) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        for (Pair<Algebraic, Integer> p : take(LIMIT, P.pairs(P.withScale(4).algebraics(), P.integersGeometric()))) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoSum() {
        Iterable<List<Algebraic>> xss = P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics());
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            String listString = tail(init(xs.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(xs));
        }
    }

    private void demoProduct() {
        Iterable<List<Algebraic>> xss = P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics());
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            String listString = tail(init(xs.toString()));
            System.out.println("Π(" + listString + ") = " + product(xs));
        }
    }

    private void demoSumSign() {
        Iterable<List<Algebraic>> xss = P.withScale(1).lists(P.withScale(1).withSecondaryScale(4).algebraics());
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            String listString = tail(init(xs.toString()));
            System.out.println("sumSign(" + listString + ") = " + sumSign(xs));
        }
    }

    private void demoDelta_finite() {
        Iterable<List<Algebraic>> xss = P.withScale(2).listsAtLeast(
                1,
                P.withScale(1).withSecondaryScale(4).algebraics()
        );
        for (List<Algebraic> xs : take(SMALL_LIMIT, xss)) {
            String listString = tail(init(xs.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(xs)));
        }
    }

    private void demoDelta_infinite() {
        for (Iterable<Algebraic> xs : take(SMALL_LIMIT, P.prefixPermutations(QBarTesting.QEP.algebraics()))) {
            String listString = tail(init(its(xs)));
            System.out.println("Δ(" + listString + ") = " + its(delta(xs)));
        }
    }

    private void demoPow() {
        Iterable<Pair<Algebraic, Integer>> ps = filterInfinite(
                p -> p.a != ZERO || p.b >= 0,
                P.pairsSquareRootOrder(
                        P.withScale(1).withSecondaryScale(4).algebraics(),
                        P.withScale(1).integersGeometric()
                )
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoEquals_Algebraic() {
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).algebraics()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            //noinspection ObjectEqualsNull
            System.out.println(x + (x.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println("hashCode(" + x + ") = " + x.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Algebraic, Algebraic> p : take(LIMIT, P.pairs(P.withScale(4).algebraics()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoReadStrict_String() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_String_targeted() {
        for (String s : take(LIMIT, P.strings(ALGEBRAIC_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.rangeUpGeometric(2));
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.b + ", " + nicePrint(p.a) + ") = " + readStrict(p.b, p.a));
        }
    }

    private void demoReadStrict_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.strings(ALGEBRAIC_CHARS),
                P.rangeUpGeometric(2)
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("readStrict(" + p.b + ", " + p.a + ") = " + readStrict(p.b, p.a));
        }
    }

    private void demoToString() {
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            System.out.println(x);
        }
    }
}
