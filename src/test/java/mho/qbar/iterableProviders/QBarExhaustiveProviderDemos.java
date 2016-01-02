package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;

import static mho.qbar.testing.QBarTesting.QEP;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class QBarExhaustiveProviderDemos extends QBarDemos {
    public QBarExhaustiveProviderDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoRangeUp_Rational() {
        for (Rational r : take(MEDIUM_LIMIT, P.rationals())) {
            System.out.println("rangeUp(" + r + ") = " + its(QEP.rangeUp(r)));
        }
    }

    private void demoRangeDown_Rational() {
        for (Rational r : take(MEDIUM_LIMIT, P.rationals())) {
            System.out.println("rangeDown(" + r + ") = " + its(QEP.rangeDown(r)));
        }
    }

    private void demoRange_Rational_Rational() {
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(QEP.range(p.a, p.b)));
        }
    }

    private void demoRationalsIn() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("rationalsIn(" + a + ") = " + its(QEP.rationalsIn(a)));
        }
    }

    private void demoRationalsNotIn() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("rationalsNotIn(" + a + ") = " + its(QEP.rationalsNotIn(a)));
        }
    }

    private void demoRationalVectors_int() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("rationalVectors(" + i + ") = " + its(QEP.rationalVectors(i)));
        }
    }

    private void demoRationalVectorsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("rationalVectorsAtLeast(" + i + ") = " + its(QEP.rationalVectorsAtLeast(i)));
        }
    }

    private void demoReducedRationalVectors_int() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("reducedRationalVectors(" + i + ") = " + its(QEP.reducedRationalVectors(i)));
        }
    }

    private void demoReducedRationalVectorsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("reducedRationalVectorsAtLeast(" + i + ") = " +
                    its(QEP.reducedRationalVectorsAtLeast(i)));
        }
    }

    private void demoRationalMatrices_int_int() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("rationalMatrices(" + p.a + ", " + p.b + ") = " + its(QEP.rationalMatrices(p.a, p.b)));
        }
    }
}
