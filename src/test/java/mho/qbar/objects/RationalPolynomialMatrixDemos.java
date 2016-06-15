package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.RationalPolynomialMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalPolynomialMatrixDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_POLYNOMIAL_MATRIX_CHARS = " #*+,-/0123456789[]^x";

    public RationalPolynomialMatrixDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoRows() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("rows(" + m + ") = " + toList(m.rows()));
        }
    }

    private void demoColumns() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("columns(" + m + ") = " + toList(m.columns()));
        }
    }

    private void demoRow() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.withScale(4).rationalPolynomialMatrices()),
                m -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.height() - 1)))
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("row(" + p.a + ", " + p.b + ") = " + p.a.row(p.b));
        }
    }

    private void demoColumn() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.withScale(4).rationalPolynomialMatrices()),
                m -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.width() - 1)))
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("column(" + p.a + ", " + p.b + ") = " + p.a.column(p.b));
        }
    }

    private void demoOnlyHasIntegralElements() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println(m + (m.onlyHasIntegralElements() ? " only has " : " doesn't only have ") +
                    "integral elements");
        }
    }

    private void demoToPolynomialMatrix() {
        Iterable<RationalPolynomialMatrix> ms = map(
                PolynomialMatrix::toRationalPolynomialMatrix,
                P.withScale(4).polynomialMatrices()
        );
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            System.out.println("toPolynomialMatrix(" + m + ") = " + m.toPolynomialMatrix());
        }
    }

    private void demoGet() {
        Iterable<Triple<RationalPolynomialMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(
                                m -> m.height() > 0 && m.width() > 0,
                                P.withScale(4).rationalPolynomialMatrices()
                        ),
                        m -> P.uniformSample(
                                toList(
                                        EP.pairsLex(
                                                ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.height() - 1),
                                                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.width() - 1))
                                        )
                                )
                        )
                )
        );
        for (Triple<RationalPolynomialMatrix, Integer, Integer> t : take(LIMIT, ts)) {
            System.out.println("get(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.get(t.b, t.c));
        }
    }

    private void demoFromRows() {
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).rationalPolynomialVectors(p.b))
                        )
                ),
                map(
                        i -> toList(replicate(i, RationalPolynomialVector.ZERO_DIMENSIONAL)),
                        P.withScale(4).positiveIntegersGeometric()
                )
        );
        for (List<RationalPolynomialVector> vs : take(SMALL_LIMIT, vss)) {
            System.out.println("fromRows(" + middle(vs.toString()) + ") = " + fromRows(vs));
        }
    }

    private void demoFromColumns() {
        Iterable<List<RationalPolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).rationalPolynomialVectors(p.b))
                        )
                ),
                map(
                        i -> toList(replicate(i, RationalPolynomialVector.ZERO_DIMENSIONAL)),
                        P.withScale(4).positiveIntegersGeometric()
                )
        );
        for (List<RationalPolynomialVector> vs : take(SMALL_LIMIT, vss)) {
            System.out.println("fromColumns(" + middle(vs.toString()) + ") = " + fromColumns(vs));
        }
    }

    private void demoMaxElementBitLength() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("maxElementBitLength(" + m + ") = " + m.maxElementBitLength());
        }
    }

    private void demoHeight() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("height(" + m + ") = " + m.height());
        }
    }

    private void demoWidth() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("width(" + m + ") = " + m.width());
        }
    }

    private void demoIsSquare() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println(m + " is " + (m.isSquare() ? "" : "not ") + "square");
        }
    }

    private void demoIsZero() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println(m + " is " + (m.isZero() ? "" : "not ") + "zero");
        }
    }

    private void demoZero() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("zero(" + p.a + ", " + p.b + ") = " + zero(p.a, p.b));
        }
    }

    private void demoIsIdentity() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println(m + " is " + (m.isIdentity() ? "" : "not ") + "an identity matrix");
        }
    }

    private void demoIdentity() {
        for (int i : take(SMALL_LIMIT, P.withScale(4).naturalIntegersGeometric())) {
            System.out.println("identity(" + i + ") = " + identity(i));
        }
    }

    private void demoTrace() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).squareRationalPolynomialMatrices())) {
            System.out.println("trace(" + m + ") = " + m.trace());
        }
    }

    private void demoSubmatrix() {
        Iterable<Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).rationalPolynomialMatrices(),
                        m -> {
                            List<Integer> allRows = toList(EP.range(0, m.height() - 1));
                            List<Integer> allColumns = toList(EP.range(0, m.width() - 1));
                            return P.pairs(
                                    map(bs -> toList(select(bs, allRows)), P.lists(m.height(), P.booleans())),
                                    map(bs -> toList(select(bs, allColumns)), P.lists(m.width(), P.booleans()))
                            );
                        }
                )
        );
        for (Triple<RationalPolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            System.out.println("submatrix(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.submatrix(t.b, t.c));
        }
    }

    private void demoTranspose() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("transpose(" + m + ") = " + m.transpose());
        }
    }

    private void demoConcat() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                filterInfinite(
                                        t -> t.a != 0 || t.b != 0,
                                        P.triples(
                                                P.withScale(4).naturalIntegersGeometric(),
                                                P.withScale(4).naturalIntegersGeometric(),
                                                P.withScale(4).positiveIntegersGeometric()
                                        )
                                ),
                                t -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(t.a, t.c),
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(p.b, 0)),
                                P.pairs(P.withScale(4).naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(SMALL_LIMIT, ps)) {
            System.out.println("concat(" + p.a + ", " + p.b + ") = " + p.a.concat(p.b));
        }
    }

    private void demoAugment() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                filterInfinite(
                                        t -> t.b != 0 || t.c != 0,
                                        P.triples(
                                                P.withScale(4).positiveIntegersGeometric(),
                                                P.withScale(4).naturalIntegersGeometric(),
                                                P.withScale(4).naturalIntegersGeometric()
                                        )
                                ),
                                t -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(t.a, t.b),
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(t.a, t.c)
                                )
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(0, p.a), zero(0, p.b)),
                                P.pairs(P.withScale(4).naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " | " + p.b + " = " + p.a.augment(p.b));
        }
    }

    private void demoAdd() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> m : take(SMALL_LIMIT, ps)) {
            System.out.println(m.a + " + " + m.b + " = " + m.a.add(m.b));
        }
    }

    private void demoNegate() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("-" + m + " = " + m.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalPolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> m : take(SMALL_LIMIT, ps)) {
            System.out.println(m.a + " - " + m.b + " = " + m.a.subtract(m.b));
        }
    }

    private void demoMultiply_RationalPolynomial() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomial>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.withScale(4).rationalPolynomials()
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Rational() {
        Iterable<Pair<RationalPolynomialMatrix, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.rationals()
        );
        for (Pair<RationalPolynomialMatrix, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<RationalPolynomialMatrix, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.bigIntegers()
        );
        for (Pair<RationalPolynomialMatrix, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.integers()
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoDivide_Rational() {
        Iterable<Pair<RationalPolynomialMatrix, Rational>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.nonzeroRationals()
        );
        for (Pair<RationalPolynomialMatrix, Rational> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_BigInteger() {
        Iterable<Pair<RationalPolynomialMatrix, BigInteger>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.nonzeroBigIntegers()
        );
        for (Pair<RationalPolynomialMatrix, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoDivide_int() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.nonzeroIntegers()
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " / " + p.b + " = " + p.a.divide(p.b));
        }
    }

    private void demoMultiply_RationalPolynomialVector() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialVector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(p.a, p.b),
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialVectors(p.b)
                                )
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), RationalPolynomialVector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                v -> new Pair<>(zero(0, v.dimension()), v),
                                P.withScale(4).withSecondaryScale(4).rationalPolynomialVectorsAtLeast(1)
                        )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialVector> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_RationalPolynomialMatrix() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.withScale(4).positiveIntegersGeometric()),
                                t -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(t.a, t.b),
                                        P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices()
                                    )
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).rationalPolynomialMatrices()
                                    )
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.withScale(4).positiveIntegersGeometric())
                    )
                )
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoShiftRight() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices(),
                P.naturalIntegersGeometric()
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " >> " + p.b + " = " + p.a.shiftRight(p.b));
        }
    }

    private void demoDeterminant() {
        Iterable<RationalPolynomialMatrix> ms =
                P.withScale(4).withSecondaryScale(4).squareRationalPolynomialMatrices();
        for (RationalPolynomialMatrix m : take(LIMIT, ms)) {
            System.out.println("det(" + m + ") = " + m.determinant());
        }
    }

    private void demoEquals_RationalPolynomialMatrix() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices()
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            //noinspection ObjectEqualsNull
            System.out.println(m + (m.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.withScale(4).rationalPolynomialMatrices())) {
            System.out.println("hashCode(" + m + ") = " + m.hashCode());
        }
    }

    private void demoCompareTo() {
        Iterable<Pair<RationalPolynomialMatrix, RationalPolynomialMatrix>> ps = P.pairs(
                P.withScale(4).rationalPolynomialMatrices()
        );
        for (Pair<RationalPolynomialMatrix, RationalPolynomialMatrix> p : take(LIMIT, ps)) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b) + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_MATRIX_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            System.out.println(m);
        }
    }
}
