package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.io.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Optional;

import static mho.qbar.objects.Algebraic.findIn;
import static mho.qbar.objects.Algebraic.of;
import static mho.qbar.objects.Algebraic.read;
import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.map;
import static mho.wheels.iterables.IterableUtils.take;
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
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            System.out.println(x + " is " + (x.isBinaryFraction() ? "a" : "not a") + " binary fraction");
        }
    }

    private void demoBinaryFractionValueExact() {
        for (Algebraic x : take(LIMIT, map(bf -> of(Rational.of(bf)), P.binaryFractions()))) {
            System.out.println("binaryFractionValueExact(" + x + ") = " + x.binaryFractionValueExact());
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

    private void demoRead_String() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_String_targeted() {
        for (String s : take(LIMIT, P.strings(ALGEBRAIC_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.rangeUpGeometric(2));
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("read(" + p.b + ", " + nicePrint(p.a) + ") = " + read(p.b, p.a));
        }
    }

    private void demoRead_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.strings(ALGEBRAIC_CHARS),
                P.rangeUpGeometric(2)
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("read(" + p.b + ", " + p.a + ") = " + read(p.b, p.a));
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

    private void demoFindIn_String() {
        Iterable<String> ss = filterInfinite(
                s -> !Readers.genericFindIn(AlgebraicDemos::badString).apply(s).isPresent(),
                P.strings()
        );
        for (String s : take(LIMIT, ss)) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_String_targeted() {
        Iterable<String> ss = filterInfinite(
                s -> !Readers.genericFindIn(AlgebraicDemos::badString).apply(s).isPresent(),
                P.strings(ALGEBRAIC_CHARS)
        );
        for (String s : take(LIMIT, ss)) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(AlgebraicDemos::badString).apply(s).isPresent(),
                        P.strings()
                ),
                P.rangeUpGeometric(2)
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("findIn(" + p.b + ", " + nicePrint(p.a) + ") = " + findIn(p.b, p.a));
        }
    }

    private void demoFindIn_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(AlgebraicDemos::badString).apply(s).isPresent(),
                        P.strings(ALGEBRAIC_CHARS)
                ),
                P.rangeUpGeometric(2)
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("findIn(" + p.b + ", " + p.a + ") = " + findIn(p.b, p.a));
        }
    }

    private void demoToString() {
        for (Algebraic x : take(LIMIT, P.algebraics())) {
            System.out.println(x);
        }
    }
}
