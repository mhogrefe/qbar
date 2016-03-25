package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.io.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static mho.qbar.objects.Algebraic.findIn;
import static mho.qbar.objects.Algebraic.read;
import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class AlgebraicDemos extends QBarDemos {
    private static final @NotNull String ALGEBRAIC_CHARS = " ()*+-/0123456789^foqrstx";

    public AlgebraicDemos(boolean useRandom) {
        super(useRandom);
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
        for (Algebraic x : take(LIMIT, P.withScale(4).algebraics())) {
            System.out.println(x);
        }
    }
}
