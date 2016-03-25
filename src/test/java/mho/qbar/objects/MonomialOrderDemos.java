package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.MonomialOrder.findIn;
import static mho.qbar.objects.MonomialOrder.read;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class MonomialOrderDemos extends QBarDemos {
    private static final @NotNull String MONOMIAL_ORDER_CHARS = "EGLRVX";

    public MonomialOrderDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoCompare() {
        Iterable<Triple<MonomialOrder, ExponentVector, ExponentVector>> ts = P.triples(
                P.monomialOrders(),
                P.exponentVectors(),
                P.exponentVectors()
        );
        for (Triple<MonomialOrder, ExponentVector, ExponentVector> t : take(LIMIT, ts)) {
            System.out.println(t.a + ": " + t.b + " " + Ordering.compare(t.a, t.b, t.c).toChar() + " " + t.c);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(MONOMIAL_ORDER_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(MONOMIAL_ORDER_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }
}
