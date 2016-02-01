package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Matrix.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class MatrixDemos extends QBarDemos {
    private static final @NotNull String MATRIX_CHARS = " #,-0123456789[]";

    public MatrixDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoRows() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("rows(" + m + ") = " + toList(m.rows()));
        }
    }

    private void demoColumns() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("columns(" + m + ") = " + toList(m.columns()));
        }
    }

    private void demoRow() {
        Iterable<Pair<Matrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.withScale(4).matrices()),
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<Matrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("row(" + p.a + ", " + p.b + ") = " + p.a.row(p.b));
        }
    }

    private void demoColumn() {
        Iterable<Pair<Matrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.withScale(4).matrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
        );
        for (Pair<Matrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("column(" + p.a + ", " + p.b + ") = " + p.a.column(p.b));
        }
    }

    private void demoGet() {
        Iterable<Triple<Matrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.withScale(4).matrices()),
                        m -> P.uniformSample(
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
                        )
                )
        );
        for (Triple<Matrix, Integer, Integer> t : take(LIMIT, ts)) {
            System.out.println("get(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.get(t.b, t.c));
        }
    }

    private void demoFromRows() {
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, Vector.ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("fromRows(" + listString + ") = " + fromRows(vs));
        }
    }

    private void demoFromColumns() {
        Iterable<List<Vector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).vectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, Vector.ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<Vector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("fromColumns(" + listString + ") = " + fromColumns(vs));
        }
    }

    private void demoMaxElementBitLength() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("maxElementBitLength(" + m + ") = " + m.maxElementBitLength());
        }
    }

    private void demoHeight() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("height(" + m + ") = " + m.height());
        }
    }

    private void demoWidth() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("width(" + m + ") = " + m.width());
        }
    }

    private void demoIsSquare() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println(m + " is " + (m.isSquare() ? "" : "not ") + "square");
        }
    }

    private void demoIsZero() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println(m + " is " + (m.isZero() ? "" : "not ") + "zero");
        }
    }

    private void demoZero() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("zero(" + p.a + ", " + p.b + ") = " + zero(p.a, p.b));
        }
    }

    private void demoIsIdentity() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println(m + " is " + (m.isIdentity() ? "" : "not ") + "an identity matrix");
        }
    }

    private void demoIdentity() {
        for (int i : take(SMALL_LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            System.out.println("identity(" + i + ") = " + identity(i));
        }
    }

    private void demoSubmatrix() {
        Iterable<Triple<Matrix, List<Integer>, List<Integer>>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        P.withScale(4).matrices(),
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
        for (Triple<Matrix, List<Integer>, List<Integer>> t : take(LIMIT, ts)) {
            System.out.println("submatrix(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.submatrix(t.b, t.c));
        }
    }

    private void demoTranspose() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("transpose(" + m + ") = " + m.transpose());
        }
    }

    private void demoConcat() {
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(P.withScale(4).matrices(t.a, t.c), P.withScale(4).matrices(t.b, t.c))
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(p.a, 0), zero(p.b, 0)),
                                P.pairs(P.withScale(4).naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            System.out.println("concat(" + p.a + ", " + p.b + ") = " + p.a.concat(p.b));
        }
    }

    private void demoAugment() {
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
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
                                t -> P.pairs(P.withScale(4).matrices(t.a, t.b), P.withScale(4).matrices(t.a, t.c))
                        )
                ),
                P.choose(
                        map(
                                p -> new Pair<>(zero(0, p.a), zero(0, p.b)),
                                P.pairs(P.withScale(4).naturalIntegersGeometric())
                        ),
                        map(
                                i -> {
                                    Matrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            System.out.println(p.a + " | " + p.b + " = " + p.a.augment(p.b));
        }
    }

    private void demoAdd() {
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> m : take(MEDIUM_LIMIT, ps)) {
            System.out.println(m.a + " + " + m.b + " = " + m.a.add(m.b));
        }
    }

    private void demoNegate() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("-" + m + " = " + m.negate());
        }
    }

    private void demoSubtract() {
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).matrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    Matrix m = Matrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<Matrix, Matrix> m : take(MEDIUM_LIMIT, ps)) {
            System.out.println(m.a + " - " + m.b + " = " + m.a.subtract(m.b));
        }
    }

    private void demoMultiply_BigInteger() {
        Iterable<Pair<Matrix, BigInteger>> ps = P.pairs(P.withScale(4).matrices(), P.bigIntegers());
        for (Pair<Matrix, BigInteger> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        Iterable<Pair<Matrix, Integer>> ps = P.pairs(P.withScale(4).matrices(), P.integers());
        for (Pair<Matrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Vector() {
        Iterable<Pair<Matrix, Vector>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(
                                        P.withScale(4).matrices(p.a, p.b),
                                        P.withScale(4).vectors(p.b)
                                )
                        )
                ),
                P.choose(
                        map(
                                i -> new Pair<>(zero(i, 0), Vector.ZERO_DIMENSIONAL),
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(v -> new Pair<>(zero(0, v.dimension()), v), P.withScale(4).vectorsAtLeast(1))
                )
        );
        for (Pair<Matrix, Vector> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_Matrix() {
        Iterable<Pair<Matrix, Matrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.triples(P.withScale(4).positiveIntegersGeometric()),
                                t -> P.pairs(P.withScale(4).matrices(t.a, t.b), P.withScale(4).matrices(t.b, t.c))
                        )
                ),
                P.choose(
                    P.choose(
                            map(
                                    m -> new Pair<>(m, zero(m.width(), 0)),
                                    filterInfinite(m -> m.height() != 0 && m.width() != 0, P.withScale(4).matrices())
                            ),
                            map(
                                    m -> new Pair<>(zero(0, m.height()), m),
                                    filterInfinite(m -> m.height() != 0 && m.width() != 0, P.withScale(4).matrices())
                            )
                    ),
                    map(
                            p -> new Pair<>(zero(p.a, 0), zero(0, p.b)),
                            P.pairs(P.withScale(4).positiveIntegersGeometric())
                    )
                )
        );
        for (Pair<Matrix, Matrix> p : take(LIMIT, ps)) {
            System.out.println(p.a + " * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<Matrix, Integer>> ps = P.pairs(P.withScale(4).matrices(), P.naturalIntegersGeometric());
        for (Pair<Matrix, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoEquals_Matrix() {
        for (Pair<Matrix, Matrix> p : take(LIMIT, P.pairs(P.withScale(4).matrices()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            //noinspection ObjectEqualsNull
            System.out.println(m + (m.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Matrix m : take(LIMIT, P.withScale(4).matrices())) {
            System.out.println("hashCode(" + m + ") = " + m.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Matrix, Matrix> p : take(LIMIT, P.pairs(P.withScale(4).matrices()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(MATRIX_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(MATRIX_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (Matrix m : take(LIMIT, P.matrices())) {
            System.out.println(m);
        }
    }
}
