package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.PolynomialMatrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class PolynomialMatrixDemos extends QBarDemos {
    private static final @NotNull String POLYNOMIAL_MATRIX_CHARS = " #*+,-0123456789[]^x";

    public PolynomialMatrixDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoRows() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("rows(" + m + ") = " + toList(m.rows()));
        }
    }

    private void demoColumns() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("columns(" + m + ") = " + toList(m.columns()));
        }
    }

    private void demoRow() {
        Iterable<Pair<PolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.withScale(4).polynomialMatrices()),
                m -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.height() - 1)))
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("row(" + p.a + ", " + p.b + ") = " + p.a.row(p.b));
        }
    }

    private void demoColumn() {
        Iterable<Pair<PolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.withScale(4).polynomialMatrices()),
                m -> P.uniformSample(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.width() - 1)))
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("column(" + p.a + ", " + p.b + ") = " + p.a.column(p.b));
        }
    }

    private void demoToRationalPolynomialMatrix() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("toRationalPolynomialMatrix(" + m + ") = " + m.toRationalPolynomialMatrix());
        }
    }

    private void demoGet() {
        Iterable<Triple<PolynomialMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.withScale(4).polynomialMatrices()),
                        m -> P.uniformSample(
                                toList(
                                        EP.pairsLex(
                                                ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.height() - 1),
                                                toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(0, m.width() - 1)))
                                )
                        )
                )
        );
        for (Triple<PolynomialMatrix, Integer, Integer> t : take(LIMIT, ts)) {
            System.out.println("get(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.get(t.b, t.c));
        }
    }

    private void demoFromRows() {
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.lists(p.a, P.withScale(4).polynomialVectors(p.b))
                        )
                ),
                map(
                        i -> toList(replicate(i, PolynomialVector.ZERO_DIMENSIONAL)),
                        P.withScale(4).positiveIntegersGeometric()
                )
        );
        for (List<PolynomialVector> vs : take(SMALL_LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("fromRows(" + listString + ") = " + fromRows(vs));
        }
    }

    private void demoFromColumns() {
        Iterable<List<PolynomialVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).polynomialVectors(p.b))
                        )
                ),
                map(
                        i -> toList(replicate(i, PolynomialVector.ZERO_DIMENSIONAL)),
                        P.withScale(4).positiveIntegersGeometric()
                )
        );
        for (List<PolynomialVector> vs : take(SMALL_LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("fromColumns(" + listString + ") = " + fromColumns(vs));
        }
    }

    private void demoMaxElementBitLength() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("maxElementBitLength(" + m + ") = " + m.maxElementBitLength());
        }
    }

    private void demoHeight() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("height(" + m + ") = " + m.height());
        }
    }

    private void demoWidth() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("width(" + m + ") = " + m.width());
        }
    }

    private void demoIsSquare() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println(m + " is " + (m.isSquare() ? "" : "not ") + "square");
        }
    }

    private void demoIsZero() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println(m + " is " + (m.isZero() ? "" : "not ") + "zero");
        }
    }

    private void demoZero() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("zero(" + p.a + ", " + p.b + ") = " + zero(p.a, p.b));
        }
    }

    private void demoIsIdentity() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println(m + " is " + (m.isIdentity() ? "" : "not ") + "an identity matrix");
        }
    }

    private void demoIdentity() {
        for (int i : take(SMALL_LIMIT, P.withScale(4).naturalIntegersGeometric())) {
            System.out.println("identity(" + i + ") = " + identity(i));
        }
    }

    private void demoTrace() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).squarePolynomialMatrices())) {
            System.out.println("trace(" + m + ") = " + m.trace());
        }
    }

    private void demoSubmatrix() {
        Iterable<Triple<PolynomialMatrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).polynomialMatrices(),
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
        for (Triple<PolynomialMatrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            System.out.println("submatrix(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.submatrix(t.b, t.c));
        }
    }

    private void demoTranspose() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("transpose(" + m + ") = " + m.transpose());
        }
    }

    private void demoConcat() {
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
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
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(t.a, t.c),
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(t.b, t.c)
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
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(SMALL_LIMIT, ps)) {
            System.out.println("concat(" + p.a + ", " + p.b + ") = " + p.a.concat(p.b));
        }
    }

    private void demoAugment() {
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
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
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(t.a, t.b),
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(t.a, t.c)
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
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " | " + p.b + " = " + p.a.augment(p.b));
        }
    }

    private void demoAdd() {
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).withSecondaryScale(4).polynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> m : take(SMALL_LIMIT, ps)) {
            System.out.println(m.a + " + " + m.b + " = " + m.a.add(m.b));
        }
    }

    private void demoNegate() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("-" + m + " = " + m.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).withSecondaryScale(4).polynomialMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    PolynomialMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> m : take(SMALL_LIMIT, ps)) {
            System.out.println(m.a + " - " + m.b + " = " + m.a.subtract(m.b));
        }
    }

    private void demoMultiply_Polynomial() {
        Iterable<Pair<PolynomialMatrix, Polynomial>> ps = P.pairs(
                P.withScale(4).polynomialMatrices(),
                P.withScale(4).polynomials()
        );
        for (Pair<PolynomialMatrix, Polynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<PolynomialMatrix, BigInteger>> ps = P.pairs(
                P.withScale(4).polynomialMatrices(),
                P.bigIntegers()
        );
        for (Pair<PolynomialMatrix, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<PolynomialMatrix, Integer>> ps = P.pairs(P.withScale(4).polynomialMatrices(), P.integers());
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_PolynomialVector() {
        Iterable<Pair<PolynomialMatrix, PolynomialVector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(p.a, p.b),
                                        P.withScale(4).withSecondaryScale(4).polynomialVectors(p.b)
                                )
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), PolynomialVector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                v -> new Pair<>(zero(0, v.dimension()), v),
                                P.withScale(4).withSecondaryScale(4).polynomialVectorsAtLeast(1)
                        )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialVector> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_PolynomialMatrix() {
        Iterable<Pair<PolynomialMatrix, PolynomialMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.withScale(4).positiveIntegersGeometric()),
                                t -> P.pairs(
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(t.a, t.b),
                                        P.withScale(4).withSecondaryScale(4).polynomialMatrices(t.b, t.c)
                                )
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).polynomialMatrices()
                                    )
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(
                                            m -> m.height() != 0 && m.width() != 0,
                                            P.withScale(4).withSecondaryScale(4).polynomialMatrices()
                                    )
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.withScale(4).positiveIntegersGeometric())
                    )
                )
        );
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(SMALL_LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<PolynomialMatrix, Integer>> ps = P.pairs(
                P.withScale(4).polynomialMatrices(),
                P.naturalIntegersGeometric()
        );
        for (Pair<PolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoDeterminant() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).squarePolynomialMatrices())) {
            System.out.println("det(" + m + ") = " + m.determinant());
        }
    }

    private void demoEquals_PolynomialMatrix() {
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, P.pairs(P.withScale(4).polynomialMatrices()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            //noinspection ObjectEqualsNull
            System.out.println(m + (m.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (PolynomialMatrix m : take(LIMIT, P.withScale(4).polynomialMatrices())) {
            System.out.println("hashCode(" + m + ") = " + m.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<PolynomialMatrix, PolynomialMatrix> p : take(LIMIT, P.pairs(P.withScale(4).polynomialMatrices()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b) + " " + p.b);
        }
    }

    private void demoReadStrict() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("readStrict(" + nicePrint(s) + ") = " + readStrict(s));
        }
    }

    private void demoReadStrict_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_MATRIX_CHARS))) {
            System.out.println("readStrict(" + s + ") = " + readStrict(s));
        }
    }

    private void demoToString() {
        for (PolynomialMatrix m : take(LIMIT, P.polynomialMatrices())) {
            System.out.println(m);
        }
    }
}
