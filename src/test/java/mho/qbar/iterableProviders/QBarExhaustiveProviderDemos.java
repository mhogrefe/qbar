package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.objects.Variable;
import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;

import java.util.List;

import static mho.qbar.testing.QBarTesting.QEP;
import static mho.wheels.iterables.IterableUtils.*;
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

    private void demoVectors_int() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("vectors(" + i + ") = " + its(QEP.vectors(i)));
        }
    }

    private void demoVectorsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("vectorsAtLeast(" + i + ") = " + its(QEP.vectorsAtLeast(i)));
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

    private void demoPolynomialVectors_int() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("polynomialVectors(" + i + ") = " + its(QEP.polynomialVectors(i)));
        }
    }

    private void demoPolynomialVectorsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("polynomialVectorsAtLeast(" + i + ") = " + its(QEP.polynomialVectorsAtLeast(i)));
        }
    }

    private void demoRationalPolynomialVectors_int() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("rationalPolynomialVectors(" + i + ") = " + its(QEP.rationalPolynomialVectors(i)));
        }
    }

    private void demoRationalPolynomialVectorsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.naturalIntegersGeometric())) {
            System.out.println("rationalPolynomialVectorsAtLeast(" + i + ") = " +
                    its(QEP.rationalPolynomialVectorsAtLeast(i)));
        }
    }

    private void demoMatrices_int_int() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("matrices(" + p.a + ", " + p.b + ") = " + its(QEP.matrices(p.a, p.b)));
        }
    }

    private void demoRationalMatrices_int_int() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("rationalMatrices(" + p.a + ", " + p.b + ") = " + its(QEP.rationalMatrices(p.a, p.b)));
        }
    }

    private void demoPolynomialMatrices_int_int() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("polynomialMatrices(" + p.a + ", " + p.b + ") = " +
                    its(QEP.polynomialMatrices(p.a, p.b)));
        }
    }

    private void demoRationalPolynomialMatrices_int_int() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("rationalPolynomialMatrices(" + p.a + ", " + p.b + ") = " +
                    its(QEP.rationalPolynomialMatrices(p.a, p.b)));
        }
    }

    private void demoPolynomials_int() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("polynomials(" + i + ") = " + its(QEP.polynomials(i)));
        }
    }

    private void demoPolynomialsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("polynomialsAtLeast(" + i + ") = " + its(QEP.polynomialsAtLeast(i)));
        }
    }

    private void demoPrimitivePolynomials_int() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("primitivePolynomials(" + i + ") = " + its(QEP.primitivePolynomials(i)));
        }
    }

    private void demoPrimitivePolynomialsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("primitivePolynomialsAtLeast(" + i + ") = " + its(QEP.primitivePolynomialsAtLeast(i)));
        }
    }

    private void demoPositivePrimitivePolynomials_int() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("positivePrimitivePolynomials(" + i + ") = " +
                    its(QEP.positivePrimitivePolynomials(i)));
        }
    }

    private void demoPositivePrimitivePolynomialsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("positivePrimitivePolynomialsAtLeast(" + i + ") = " +
                    its(QEP.positivePrimitivePolynomialsAtLeast(i)));
        }
    }

    private void demoSquareFreePolynomials_int() {
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            System.out.println("squareFreePolynomials(" + i + ") = " + its(QEP.squareFreePolynomials(i)));
        }
    }

    private void demoSquareFreePolynomialsAtLeast() {
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            System.out.println("squareFreePolynomialsAtLeast(" + i + ") = " +
                    its(QEP.squareFreePolynomialsAtLeast(i)));
        }
    }

    private void demoPositivePrimitiveSquareFreePolynomials_int() {
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            System.out.println("positivePrimitiveSquareFreePolynomials(" + i + ") = " +
                    its(QEP.positivePrimitiveSquareFreePolynomials(i)));
        }
    }

    private void demoPositivePrimitiveSquareFreePolynomialsAtLeast() {
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            System.out.println("positivePrimitiveSquareFreePolynomialsAtLeast(" + i + ") = " +
                    its(QEP.positivePrimitiveSquareFreePolynomialsAtLeast(i)));
        }
    }

    private void demoIrreduciblePolynomials_int() {
        for (int i : take(TINY_LIMIT, P.withScale(4).rangeUpGeometric(-1))) {
            System.out.println("irreduciblePolynomials(" + i + ") = " + its(QEP.irreduciblePolynomials(i)));
        }
    }

    private void demoIrreduciblePolynomialsAtLeast() {
        for (int i : take(TINY_LIMIT, P.withScale(1).rangeUpGeometric(-1))) {
            System.out.println("irreduciblePolynomialsAtLeast(" + i + ") = " +
                    its(QEP.irreduciblePolynomialsAtLeast(i)));
        }
    }

    private void demoRationalPolynomials_int() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("rationalPolynomials(" + i + ") = " + its(QEP.rationalPolynomials(i)));
        }
    }

    private void demoRationalPolynomialsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("rationalPolynomialsAtLeast(" + i + ") = " + its(QEP.rationalPolynomialsAtLeast(i)));
        }
    }

    private void demoMonicRationalPolynomials_int() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("monicRationalPolynomials(" + i + ") = " + its(QEP.monicRationalPolynomials(i)));
        }
    }

    private void demoMonicRationalPolynomialsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("monicRationalPolynomialsAtLeast(" + i + ") = " +
                    its(QEP.monicRationalPolynomialsAtLeast(i)));
        }
    }

    private void demoExponentVectors_List_Variable() {
        for (List<Variable> vs : take(MEDIUM_LIMIT, P.subsets(P.variables()))) {
            String listString = tail(init(vs.toString()));
            System.out.println("exponentVectors(" + listString + ") = " + its(QEP.exponentVectors(vs)));
        }
    }

    private void demoMultivariatePolynomials_List_Variable() {
        for (List<Variable> vs : take(MEDIUM_LIMIT, P.subsets(P.variables()))) {
            String listString = tail(init(vs.toString()));
            System.out.println("multivariatePolynomials(" + listString + ") = " +
                    its(QEP.multivariatePolynomials(vs)));
        }
    }

    private void demoPositiveAlgebraics_int() {
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            System.out.println("positiveAlgebraics(" + i + ") = " + its(QEP.positiveAlgebraics(i)));
        }
    }

    private void demoNegativeAlgebraics_int() {
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            System.out.println("negativeAlgebraics(" + i + ") = " + its(QEP.negativeAlgebraics(i)));
        }
    }

    private void demoNonzeroAlgebraics_int() {
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            System.out.println("nonzeroAlgebraics(" + i + ") = " + its(QEP.nonzeroAlgebraics(i)));
        }
    }

    private void demoAlgebraics_int() {
        for (int i : take(TINY_LIMIT, P.withScale(2).positiveIntegersGeometric())) {
            System.out.println("algebraics(" + i + ") = " + its(QEP.algebraics(i)));
        }
    }

    private void demoNonNegativeAlgebraicsLessThanOne_int() {
        for (int i : take(TINY_LIMIT / 2, P.withScale(2).positiveIntegersGeometric())) {
            System.out.println("nonNegativeAlgebraicsLessThanOne(" + i + ") = " +
                    its(QEP.nonNegativeAlgebraicsLessThanOne(i)));
        }
    }
}
