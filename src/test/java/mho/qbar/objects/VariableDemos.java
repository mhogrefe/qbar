package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.qbar.objects.Variable.of;
import static mho.qbar.objects.Variable.readStrict;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.nicePrint;

@SuppressWarnings("UnusedDeclaration")
public class VariableDemos extends QBarDemos {
    private static final @NotNull String VARIABLE_CHARS =
            charsToString(ExhaustiveProvider.INSTANCE.rangeIncreasing('a', 'z'));

    public VariableDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoGetIndex() {
        for (Variable v : take(MEDIUM_LIMIT, P.variables())) {
            System.out.println("getIndex(" + v + ") = " + v.getIndex());
        }
    }

    private void demoOf() {
        for (int i : take(MEDIUM_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoEquals_Variable() {
        for (Pair<Variable, Variable> p : take(LIMIT, P.pairs(P.variables()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Variable v : take(MEDIUM_LIMIT, P.variables())) {
            //noinspection ObjectEqualsNull
            System.out.println(v + (v.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Variable v : take(MEDIUM_LIMIT, P.variables())) {
            System.out.println("hashCode(" + v + ") = " + v.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Variable, Variable> p : take(LIMIT, P.pairs(P.variables()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(VARIABLE_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (Variable v : take(MEDIUM_LIMIT, P.variables())) {
            System.out.println(v);
        }
    }
}
