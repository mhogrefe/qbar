package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;

import java.util.List;

import static mho.qbar.testing.QBarTesting.QEP;
import static mho.wheels.iterables.IterableUtils.*;
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
        for (Pair<Rational, Rational> p : take(MEDIUM_LIMIT, P.bagPairs(P.rationals()))) {
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

    private void demoMonicPolynomials_int() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("monicPolynomials(" + i + ") = " + its(QEP.monicPolynomials(i)));
        }
    }

    private void demoMonicPolynomialsAtLeast() {
        for (int i : take(SMALL_LIMIT, P.rangeUpGeometric(-1))) {
            System.out.println("monicPolynomialsAtLeast(" + i + ") = " + its(QEP.monicPolynomialsAtLeast(i)));
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

    private void demoMonomials_List_Variable() {
        for (List<Variable> vs : take(MEDIUM_LIMIT, P.subsets(P.variables()))) {
            System.out.println("monomials(" + middle(vs.toString()) + ") = " + its(QEP.monomials(vs)));
        }
    }

    private void demoMultivariatePolynomials_List_Variable() {
        for (List<Variable> vs : take(MEDIUM_LIMIT, P.subsets(P.variables()))) {
            System.out.println("multivariatePolynomials(" + middle(vs.toString()) + ") = " +
                    its(QEP.multivariatePolynomials(vs)));
        }
    }

    private void demoRationalMultivariatePolynomials_List_Variable() {
        for (List<Variable> vs : take(MEDIUM_LIMIT, P.subsets(P.variables()))) {
            System.out.println("rationalMultivariatePolynomials(" + middle(vs.toString()) + ") = " +
                    its(QEP.rationalMultivariatePolynomials(vs)));
        }
    }

    private void demoCleanRealRangeUp() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            System.out.println("cleanRealRangeUp(" + x + ") = " + its(QEP.cleanRealRangeUp(x)));
        }
    }

    private void demoRealRangeUp() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            System.out.println("realRangeUp(" + x + ") = " + its(QEP.realRangeUp(x)));
        }
    }

    private void demoCleanRealRangeDown() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            System.out.println("cleanRealRangeDown(" + x + ") = " + its(QEP.cleanRealRangeDown(x)));
        }
    }

    private void demoRealRangeDown() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            System.out.println("realRangeDown(" + x + ") = " + its(QEP.realRangeDown(x)));
        }
    }

    private void demoCleanRealRange() {
        for (Pair<Algebraic, Algebraic> p : take(MEDIUM_LIMIT, P.bagPairs(P.withScale(4).algebraics()))) {
            System.out.println("cleanRealRange(" + p.a + ", " + p.b + ") = " + its(QEP.cleanRealRange(p.a, p.b)));
        }
    }

    private void demoRealRange() {
        for (Pair<Algebraic, Algebraic> p : take(MEDIUM_LIMIT, P.bagPairs(P.withScale(4).algebraics()))) {
            System.out.println("realRange(" + p.a + ", " + p.b + ") = " + its(QEP.realRange(p.a, p.b)));
        }
    }

    private void demoCleanRealsIn() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("cleanRealsIn(" + a + ") = " + its(QEP.cleanRealsIn(a)));
        }
    }

    private void demoRealsIn() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("realsIn(" + a + ") = " + its(QEP.realsIn(a)));
        }
    }

    private void demoCleanRealsNotIn() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("cleanRealsNotIn(" + a + ") = " + its(QEP.cleanRealsNotIn(a)));
        }
    }

    private void demoRealsNotIn() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("realsNotIn(" + a + ") = " + its(QEP.realsNotIn(a)));
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

    private void demoRangeUp_int_Algebraic() {
        Iterable<Pair<Algebraic, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).algebraics(),
                P.withScale(2).positiveIntegersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeUp(" + p.b + ", " + p.a + ") = " + its(QEP.rangeUp(p.b, p.a)));
        }
    }

    private void demoRangeUp_Algebraic() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            System.out.println("rangeUp(" + x + ") = " + its(QEP.rangeUp(x)));
        }
    }

    private void demoRangeDown_int_Algebraic() {
        Iterable<Pair<Algebraic, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).algebraics(),
                P.withScale(2).positiveIntegersGeometric()
        );
        for (Pair<Algebraic, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeDown(" + p.b + ", " + p.a + ") = " + its(QEP.rangeDown(p.b, p.a)));
        }
    }

    private void demoRangeDown_Algebraic() {
        for (Algebraic x : take(MEDIUM_LIMIT, P.withScale(4).algebraics())) {
            System.out.println("rangeDown(" + x + ") = " + its(QEP.rangeDown(x)));
        }
    }

    private void demoRange_int_Algebraic_Algebraic() {
        Iterable<Triple<Integer, Algebraic, Algebraic>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsLogarithmicOrder(
                        P.bagPairs(P.withScale(4).algebraics()),
                        P.withScale(2).positiveIntegersGeometric()
                )
        );
        for (Triple<Integer, Algebraic, Algebraic> t : take(SMALL_LIMIT, ts)) {
            System.out.println("range(" + t.a + ", " + t.b + ", " + t.c + ") = " + its(QEP.range(t.a, t.b, t.c)));
        }
    }

    private void demoRange_Algebraic_Algebraic() {
        for (Pair<Algebraic, Algebraic> p : take(MEDIUM_LIMIT, P.bagPairs(P.withScale(4).algebraics()))) {
            System.out.println("range(" + p.a + ", " + p.b + ") = " + its(QEP.range(p.a, p.b)));
        }
    }

    private void demoAlgebraicsIn_int_Interval() {
        Iterable<Pair<Interval, Integer>> ps = P.pairsLogarithmicOrder(
                P.intervals(),
                P.withScale(2).positiveIntegersGeometric()
        );
        for (Pair<Interval, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("algebraicsIn(" + p.b + ", " + p.a + ") = " + its(QEP.algebraicsIn(p.b, p.a)));
        }
    }

    private void demoAlgebraicsIn_Interval() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("algebraicsIn(" + a + ") = " + its(QEP.algebraicsIn(a)));
        }
    }

    private void demoAlgebraicsNotIn_int_Interval() {
        Iterable<Pair<Interval, Integer>> ps = P.pairsLogarithmicOrder(
                P.intervals(),
                P.withScale(2).positiveIntegersGeometric()
        );
        for (Pair<Interval, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("algebraicsNotIn(" + p.b + ", " + p.a + ") = " + its(QEP.algebraicsNotIn(p.b, p.a)));
        }
    }

    private void demoAlgebraicsNotIn_Interval() {
        for (Interval a : take(MEDIUM_LIMIT, P.intervals())) {
            System.out.println("algebraicsNotIn(" + a + ") = " + its(QEP.algebraicsNotIn(a)));
        }
    }

    private void demoRationalMultiplesOfPiInRange() {
        Iterable<Pair<AlgebraicAngle, AlgebraicAngle>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (Pair<AlgebraicAngle, AlgebraicAngle> p : take(SMALL_LIMIT, ps)) {
            System.out.println("rationalMultiplesOfPiInRange(" + p.a + ", " + p.b + ") = " +
                    its(QEP.rationalMultiplesOfPiInRange(p.a, p.b)));
        }
    }

    private void demoQBarRandomProvidersFixedScales() {
        for (Triple<Integer, Integer, Integer> t : take(SMALL_LIMIT, P.triples(P.integersGeometric()))) {
            System.out.println("qbarRandomProvidersFixedScales(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    its(P.qbarRandomProvidersFixedScales(t.a, t.b, t.c)));
        }
    }
}
