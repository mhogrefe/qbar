package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.MonomialOrder.readStrict;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class MonomialOrderDemos extends QBarDemos {
    private static final @NotNull String MONOMIAL_ORDER_CHARS = "EGLRVX";

    public MonomialOrderDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoCompare() {
        Iterable<Triple<MonomialOrder, Monomial, Monomial>> ts = P.triples(
                P.monomialOrders(),
                P.monomials(),
                P.monomials()
        );
        for (Triple<MonomialOrder, Monomial, Monomial> t : take(LIMIT, ts)) {
            System.out.println(t.a + ": " + t.b + " " + Ordering.compare(t.a, t.b, t.c).toChar() + " " + t.c);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(MONOMIAL_ORDER_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }
}
