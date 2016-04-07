package mho.qbar.iterableProviders;

import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.objects.Variable;
import mho.qbar.testing.QBarDemos;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;

import java.util.List;

import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class QBarRandomProviderDemos extends QBarDemos {
    public QBarRandomProviderDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoPositiveRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("positiveRationals(" + rp + ") = " + its(rp.positiveRationals()));
        }
    }

    private void demoNegativeRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("negativeRationals(" + rp + ") = " + its(rp.negativeRationals()));
        }
    }

    private void demoNonzeroRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("nonzeroRationals(" + rp + ") = " + its(rp.nonzeroRationals()));
        }
    }

    private void demoRationals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 3,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("rationals(" + rp + ") = " + its(rp.rationals()));
        }
    }

    private void demoNonNegativeRationalsLessThanOne() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 4,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("nonNegativeRationalsLessThanOne(" + rp + ") = " +
                    its(rp.nonNegativeRationalsLessThanOne()));
        }
    }

    private void demoRangeUp_Rational() {
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeUp(" + p.a + ", " + p.b + ") = " + its(p.a.rangeUp(p.b)));
        }
    }

    private void demoRangeDown_Rational() {
        Iterable<Pair<QBarRandomProvider, Rational>> ps = P.pairs(
                filterInfinite(rp -> rp.getScale() >= 4, P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()),
                P.rationals()
        );
        for (Pair<QBarRandomProvider, Rational> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rangeDown(" + p.a + ", " + p.b + ") = " + its(p.a.rangeDown(p.b)));
        }
    }

    private void demoRange_Rational_Rational() {
        Iterable<Triple<QBarRandomProvider, Rational, Rational>> ts = filterInfinite(
                t -> le(t.b, t.c),
                P.triples(
                        filterInfinite(
                                rp -> rp.getScale() >= 4,
                                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                        ),
                        P.rationals(),
                        P.rationals()
                )
        );
        for (Triple<QBarRandomProvider, Rational, Rational> t : take(MEDIUM_LIMIT, ts)) {
            System.out.println("range(" + t.a + ", " + t.b + ", " + t.c + ") = " + its(t.a.range(t.b, t.c)));
        }
    }

    private void demoFinitelyBoundedIntervals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("finitelyBoundedIntervals(" + rp + ") = " + its(rp.finitelyBoundedIntervals()));
        }
    }

    private void demoIntervals() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 6,
                P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("intervals(" + rp + ") = " + its(rp.intervals()));
        }
    }

    private void demoRationalsIn() {
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 4,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.intervals()
        );
        for (Pair<QBarRandomProvider, Interval> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalsIn(" + p.a + ", " + p.b + ") = " + its(p.a.rationalsIn(p.b)));
        }
    }

    private void demoRationalsNotIn() {
        Iterable<Pair<QBarRandomProvider, Interval>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 4,
                        P.qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                filterInfinite(a -> !a.equals(Interval.ALL), P.intervals())
        );
        for (Pair<QBarRandomProvider, Interval> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalsNotIn(" + p.a + ", " + p.b + ") = " + its(p.a.rationalsNotIn(p.b)));
        }
    }

    private void demoVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("vectors(" + p.a + ", " + p.b + ") = " + its(p.a.vectors(p.b)));
        }
    }

    private void demoVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("vectors(" + rp + ") = " + its(rp.vectors()));
        }
    }

    private void demoVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("vectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.vectorsAtLeast(p.b)));
        }
    }

    private void demoRationalVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() >= 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalVectors(" + p.a + ", " + p.b + ") = " + its(p.a.rationalVectors(p.b)));
        }
    }

    private void demoRationalVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("rationalVectors(" + rp + ") = " + its(rp.rationalVectors()));
        }
    }

    private void demoRationalVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalVectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.rationalVectorsAtLeast(p.b)));
        }
    }

    private void demoReducedRationalVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("reducedRationalVectors(" + p.a + ", " + p.b + ") = " +
                    its(p.a.reducedRationalVectors(p.b)));
        }
    }

    private void demoReducedRationalVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("reducedRationalVectors(" + rp + ") = " + its(rp.reducedRationalVectors()));
        }
    }

    private void demoReducedRationalVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("reducedRationalVectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.reducedRationalVectorsAtLeast(p.b)));
        }
    }

    private void demoPolynomialVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("polynomialVectors(" + p.a + ", " + p.b + ") = " + its(p.a.polynomialVectors(p.b)));
        }
    }

    private void demoPolynomialVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() > 0,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("polynomialVectors(" + rp + ") = " + its(rp.polynomialVectors()));
        }
    }

    private void demoPolynomialVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                                P.withScale(4).qbarRandomProviders()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("polynomialVectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.polynomialVectorsAtLeast(p.b)));
        }
    }

    private void demoRationalPolynomialVectors_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("rationalPolynomialVectors(" + p.a + ", " + p.b + ") = " +
                    its(p.a.rationalPolynomialVectors(p.b)));
        }
    }

    private void demoRationalPolynomialVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() > 0,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("rationalPolynomialVectors(" + rp + ") = " + its(rp.rationalPolynomialVectors()));
        }
    }

    private void demoRationalPolynomialVectorsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getTertiaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                                P.withScale(4).qbarRandomProviders()
                        ),
                        P.withScale(4).naturalIntegersGeometric()
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("rationalPolynomialVectorsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.rationalPolynomialVectorsAtLeast(p.b)));
        }
    }

    private void demoMatrices_int_int() {
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            System.out.println("matrices(" + t.a + ", " + t.b + ", " + t.c + ") = " + its(t.a.matrices(t.b, t.c)));
        }
    }

    private void demoMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("matrices(" + rp + ") = " + its(rp.matrices()));
        }
    }

    private void demoSquareMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("squareMatrices(" + rp + ") = " + its(rp.squareMatrices()));
        }
    }

    private void demoInvertibleMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("invertibleMatrices(" + rp + ") = " + its(rp.invertibleMatrices()));
        }
    }

    private void demoRationalMatrices_int_int() {
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            System.out.println("rationalMatrices(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    its(t.a.rationalMatrices(t.b, t.c)));
        }
    }

    private void demoRationalMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("rationalMatrices(" + rp + ") = " + its(rp.rationalMatrices()));
        }
    }

    private void demoSquareRationalMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 2,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("squareRationalMatrices(" + rp + ") = " + its(rp.squareRationalMatrices()));
        }
    }

    private void demoInvertibleRationalMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 2,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("invertibleRationalMatrices(" + rp + ") = " + its(rp.invertibleRationalMatrices()));
        }
    }

    private void demoPolynomialMatrices_int_int() {
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            System.out.println("polynomialMatrices(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    its(t.a.polynomialMatrices(t.b, t.c)));
        }
    }

    private void demoPolynomialMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("polynomialMatrices(" + rp + ") = " + its(rp.polynomialMatrices()));
        }
    }

    private void demoSquarePolynomialMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("squarePolynomialMatrices(" + rp + ") = " + its(rp.squarePolynomialMatrices()));
        }
    }

    private void demoRationalPolynomialMatrices_int_int() {
        Iterable<Triple<QBarRandomProvider, Integer, Integer>> ts = P.triples(
                filterInfinite(
                        rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                        P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Triple<QBarRandomProvider, Integer, Integer> t : take(SMALL_LIMIT, ts)) {
            System.out.println("rationalPolynomialMatrices(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    its(t.a.rationalPolynomialMatrices(t.b, t.c)));
        }
    }

    private void demoRationalPolynomialMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("rationalPolynomialMatrices(" + rp + ") = " + its(rp.rationalPolynomialMatrices()));
        }
    }

    private void demoSquareRationalPolynomialMatrices() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0 && rp.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("squareRationalPolynomialMatrices(" + rp + ") = " +
                    its(rp.squareRationalPolynomialMatrices()));
        }
    }

    private void demoPolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("polynomials(" + p.a + ", " + p.b + ") = " + its(p.a.polynomials(p.b)));
        }
    }

    private void demoPolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("polynomials(" + rp + ") = " + its(rp.polynomials()));
        }
    }

    private void demoPolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("polynomialsAtLeast(" + p.a + ", " + p.b + ") = " + its(p.a.polynomialsAtLeast(p.b)));
        }
    }

    private void demoPrimitivePolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("primitivePolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.primitivePolynomials(p.b)));
        }
    }

    private void demoPrimitivePolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("primitivePolynomials(" + rp + ") = " + its(rp.primitivePolynomials()));
        }
    }

    private void demoPrimitivePolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b && p.a.getSecondaryScale() > 0,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("primitivePolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.primitivePolynomialsAtLeast(p.b)));
        }
    }

    private void demoPositivePrimitivePolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("positivePrimitivePolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.positivePrimitivePolynomials(p.b)));
        }
    }

    private void demoPositivePrimitivePolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("positivePrimitivePolynomials(" + rp + ") = " + its(rp.positivePrimitivePolynomials()));
        }
    }

    private void demoPositivePrimitivePolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b && p.a.getSecondaryScale() > 0,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("positivePrimitivePolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.positivePrimitivePolynomialsAtLeast(p.b)));
        }
    }

    private void demoMonicPolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("monicPolynomials(" + p.a + ", " + p.b + ") = " + its(p.a.monicPolynomials(p.b)));
        }
    }

    private void demoMonicPolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("monicPolynomials(" + rp + ") = " + its(rp.monicPolynomials()));
        }
    }

    private void demoMonicPolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("monicPolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.monicPolynomialsAtLeast(p.b)));
        }
    }

    private void demoSquareFreePolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("squareFreePolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.squareFreePolynomials(p.b)));
        }
    }

    private void demoSquareFreePolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() >= 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("squareFreePolynomials(" + rp + ") = " + its(rp.squareFreePolynomials()));
        }
    }

    private void demoSquareFreePolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b && p.a.getSecondaryScale() >= 0,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("squareFreePolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.squareFreePolynomialsAtLeast(p.b)));
        }
    }

    private void demoPositivePrimitiveSquareFreePolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("positivePrimitiveSquareFreePolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.positivePrimitiveSquareFreePolynomials(p.b)));
        }
    }

    private void demoPositivePrimitiveSquareFreePolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("positivePrimitiveSquareFreePolynomials(" + rp + ") = " +
                    its(rp.positivePrimitiveSquareFreePolynomials()));
        }
    }

    private void demoPositivePrimitiveSquareFreePolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b && p.a.getSecondaryScale() > 0,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("positivePrimitiveSquareFreePolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.positivePrimitiveSquareFreePolynomialsAtLeast(p.b)));
        }
    }

    private void demoIrreduciblePolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(1).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("irreduciblePolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.irreduciblePolynomials(p.b)));
        }
    }

    private void demoIrreduciblePolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("irreduciblePolynomials(" + rp + ") = " + its(rp.irreduciblePolynomials()));
        }
    }

    private void demoIrreduciblePolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b && p.a.getSecondaryScale() > 0,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 2 && rp.getSecondaryScale() >= 2,
                                P.withScale(1).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("irreduciblePolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.irreduciblePolynomialsAtLeast(p.b)));
        }
    }

    private void demoRationalPolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() >= 3,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).rangeUpGeometric(-1)
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalPolynomials(" + p.a + ", " + p.b + ") = " + its(p.a.rationalPolynomials(p.b)));
        }
    }

    private void demoRationalPolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() >= 3 && rp.getSecondaryScale() >= 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("rationalPolynomials(" + rp + ") = " + its(rp.rationalPolynomials()));
        }
    }

    private void demoRationalPolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() >= 3,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(MEDIUM_LIMIT, ps)) {
            System.out.println("rationalPolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.rationalPolynomialsAtLeast(p.b)));
        }
    }

    private void demoMonicRationalPolynomials_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairsSquareRootOrder(
                filterInfinite(
                        rp -> rp.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("monicRationalPolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.monicRationalPolynomials(p.b)));
        }
    }

    private void demoMonicRationalPolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                rp -> rp.getScale() > 0 && rp.getSecondaryScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("monicRationalPolynomials(" + rp + ") = " + its(rp.monicRationalPolynomials()));
        }
    }

    private void demoMonicRationalPolynomialsAtLeast() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = filterInfinite(
                p -> p.a.getSecondaryScale() > p.b && p.a.getSecondaryScale() > 0,
                P.pairsSquareRootOrder(
                        filterInfinite(
                                rp -> rp.getScale() > 0,
                                P.withScale(4).qbarRandomProvidersDefaultTertiaryScale()
                        ),
                        P.withScale(4).rangeUpGeometric(-1)
                )
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("monicRationalPolynomialsAtLeast(" + p.a + ", " + p.b + ") = " +
                    its(p.a.monicRationalPolynomialsAtLeast(p.b)));
        }
    }

    private void demoVariables() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("variables(" + rp + ") = " + its(rp.variables()));
        }
    }

    private void demoMonomialOrders() {
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, P.qbarRandomProvidersDefault())) {
            System.out.println("monomialOrders(" + rp + ") = " + its(rp.monomialOrders()));
        }
    }

    private void demoExponentVectors() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0,
                P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("exponentVectors(" + rp + ") = " + its(rp.exponentVectors()));
        }
    }

    private void demoExponentVectors_List_Variable() {
        Iterable<Pair<QBarRandomProvider, List<Variable>>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(SMALL_LIMIT, ps)) {
            System.out.println("exponentVectors(" + p.a + ", " + p.b + ") = " + its(p.a.exponentVectors(p.b)));
        }
    }

    private void demoMultivariatePolynomials() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() >= 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                P.withScale(4).qbarRandomProviders()
        );
        for (QBarRandomProvider rp : take(MEDIUM_LIMIT, rps)) {
            System.out.println("multivariatePolynomials(" + rp + ") = " + its(rp.multivariatePolynomials()));
        }
    }

    private void demoMultivariatePolynomials_List_Variable() {
        Iterable<Pair<QBarRandomProvider, List<Variable>>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() >= 2 && s.getSecondaryScale() > 0 && s.getTertiaryScale() >= 2,
                        P.withScale(4).qbarRandomProviders()
                ),
                P.subsets(P.variables())
        );
        for (Pair<QBarRandomProvider, List<Variable>> p : take(SMALL_LIMIT, ps)) {
            System.out.println("multivariatePolynomials(" + p.a + ", " + p.b + ") = " +
                    its(p.a.multivariatePolynomials(p.b)));
        }
    }

    private void demoPositiveAlgebraics_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("positiveAlgebraics(" + p.a + ", " + p.b + ") = " + its(p.a.positiveAlgebraics(p.b)));
        }
    }

    private void demoPositiveAlgebraics() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("positiveAlgebraics(" + rp + ") = " + its(rp.positiveAlgebraics()));
        }
    }

    private void demoNegativeAlgebraics_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("negativeAlgebraics(" + p.a + ", " + p.b + ") = " + its(p.a.negativeAlgebraics(p.b)));
        }
    }

    private void demoNegativeAlgebraics() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("negativeAlgebraics(" + rp + ") = " + its(rp.negativeAlgebraics()));
        }
    }

    private void demoNonzeroAlgebraics_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("nonzeroAlgebraics(" + p.a + ", " + p.b + ") = " + its(p.a.nonzeroAlgebraics(p.b)));
        }
    }

    private void demoNonzeroAlgebraics() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("nonzeroAlgebraics(" + rp + ") = " + its(rp.nonzeroAlgebraics()));
        }
    }

    private void demoAlgebraics_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("algebraics(" + p.a + ", " + p.b + ") = " + its(p.a.algebraics(p.b)));
        }
    }

    private void demoAlgebraics() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("algebraics(" + rp + ") = " + its(rp.algebraics()));
        }
    }

    private void demoNonNegativeAlgebraicsLessThanOne_int() {
        Iterable<Pair<QBarRandomProvider, Integer>> ps = P.pairs(
                filterInfinite(
                        s -> s.getScale() > 0,
                        P.withScale(4).qbarRandomProvidersDefaultSecondaryAndTertiaryScale()
                ),
                P.withScale(4).positiveIntegersGeometric()
        );
        for (Pair<QBarRandomProvider, Integer> p : take(SMALL_LIMIT, ps)) {
            System.out.println("nonNegativeAlgebraicsLessThanOne(" + p.a + ", " + p.b + ") = " +
                    its(p.a.nonNegativeAlgebraicsLessThanOne(p.b)));
        }
    }

    private void demoNonNegativeAlgebraicsLessThanOne() {
        Iterable<QBarRandomProvider> rps = filterInfinite(
                s -> s.getScale() > 0 && s.getSecondaryScale() >= 4,
                P.withScale(2).qbarRandomProvidersDefaultTertiaryScale()
        );
        for (QBarRandomProvider rp : take(SMALL_LIMIT, rps)) {
            System.out.println("nonNegativeAlgebraicsLessThanOne(" + rp + ") = " +
                    its(rp.nonNegativeAlgebraicsLessThanOne()));
        }
    }
}
