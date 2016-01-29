package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Vector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class VectorDemos extends QBarDemos {
    private static final @NotNull String VECTOR_CHARS = " ,-0123456789[]";

    public VectorDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("toList(" + v + ") = " + toList(v));
        }
    }

    private void demoGet() {
        Iterable<Pair<Vector, Integer>> ps = P.dependentPairs(
                P.withScale(4).vectorsAtLeast(1),
                v -> P.uniformSample(toList(range(0, v.dimension() - 1)))
        );
        for (Pair<Vector, Integer> p : take(LIMIT, ps)) {
            System.out.println("get(" + p.a + ", " + p.b + ") = " + p.a.get(p.b));
        }
    }

    private void demoOf_List_BigInteger() {
        for (List<BigInteger> is : take(LIMIT, P.withScale(4).lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoDimension() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("dim(" + v + ") = " + v.dimension());
        }
    }

    private void demoEquals_Vector() {
        for (Pair<Vector, Vector> p : take(LIMIT, P.pairs(P.withScale(4).vectors()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Vector v : take(LIMIT, P.withScale(4).vectors())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Vector, Vector> p : take(LIMIT, P.pairs(P.withScale(4).vectors()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(VECTOR_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(VECTOR_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (Vector v : take(LIMIT, P.vectors())) {
            System.out.println(v);
        }
    }
}
