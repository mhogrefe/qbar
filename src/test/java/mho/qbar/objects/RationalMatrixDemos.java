package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static mho.qbar.objects.RationalMatrix.*;
import static mho.qbar.objects.RationalVector.ZERO_DIMENSIONAL;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RationalMatrixDemos extends QBarDemos {
    private static final @NotNull String RATIONAL_MATRIX_CHARS = " #,-/0123456789[]";

    public RationalMatrixDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoRows() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("rows(" + m + ") = " + toList(m.rows()));
        }
    }

    private void demoColumns() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("columns(" + m + ") = " + toList(m.columns()));
        }
    }

    private void demoRow() {
        Iterable<Pair<RationalMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.height() > 0, P.withScale(4).rationalMatrices()),
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("row(" + p.a + ", " + p.b + ") = " + p.a.row(p.b));
        }
    }

    private void demoColumn() {
        Iterable<Pair<RationalMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.withScale(4).rationalMatrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
        );
        for (Pair<RationalMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("column(" + p.a + ", " + p.b + ") = " + p.a.column(p.b));
        }
    }

    private void demoGet() {
        Iterable<Triple<RationalMatrix, Integer, Integer>> ts = map(
                p -> new Triple<>(p.a, p.b.a, p.b.b),
                P.dependentPairs(
                        filterInfinite(m -> m.height() > 0 && m.width() > 0, P.withScale(4).rationalMatrices()),
                        m -> P.uniformSample(
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
                        )
                )
        );
        for (Triple<RationalMatrix, Integer, Integer> t : take(LIMIT, ts)) {
            System.out.println("get(" + t.a + ", " + t.b + ", " + t.c + ") = " + t.a.get(t.b, t.c));
        }
    }

    private void demoFromRows() {
        Iterable<List<RationalVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).rationalVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("fromRows(" + listString + ") = " + fromRows(vs));
        }
    }

    private void demoFromColumns() {
        Iterable<List<RationalVector>> vss = P.chooseLogarithmicOrder(
                map(
                        p -> p.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.withScale(4).lists(p.a, P.withScale(4).rationalVectors(p.b))
                        )
                ),
                map(i -> toList(replicate(i, ZERO_DIMENSIONAL)), P.withScale(4).positiveIntegersGeometric())
        );
        for (List<RationalVector> vs : take(LIMIT, vss)) {
            String listString = tail(init(vs.toString()));
            System.out.println("fromColumns(" + listString + ") = " + fromColumns(vs));
        }
    }

    private void demoHeight() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("height(" + m + ") = " + m.height());
        }
    }

    private void demoWidth() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("width(" + m + ") = " + m.width());
        }
    }

    private void demoIsSquare() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println(m + " is " + (m.isSquare() ? "" : "not ") + "square");
        }
    }

    private void demoIsZero() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println(m + " is " + (m.isZero() ? "" : "not ") + "zero");
        }
    }

    private void demoZero() {
        for (Pair<Integer, Integer> p : take(SMALL_LIMIT, P.pairs(P.withScale(4).naturalIntegersGeometric()))) {
            System.out.println("zero(" + p.a + ", " + p.b + ") = " + zero(p.a, p.b));
        }
    }

    private void demoIsIdentity() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println(m + " is " + (m.isIdentity() ? "" : "not ") + "an identity matrix");
        }
    }

    private void demoIdentity() {
        for (int i : take(SMALL_LIMIT, P.withScale(4).positiveIntegersGeometric())) {
            System.out.println("identity(" + i + ") = " + identity(i));
        }
    }

    private void demoTranspose() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("transpose(" + m + ") = " + m.transpose());
        }
    }

    private void demoAugment() {
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
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
                                        P.withScale(4).rationalMatrices(t.a, t.b),
                                        P.withScale(4).rationalMatrices(t.a, t.c)
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
                                    RationalMatrix m = zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, ps)) {
            System.out.println(p.a + " | " + p.b + " = " + p.a.augment(p.b));
        }
    }

    private void demoAdd() {
        Iterable<Pair<RationalMatrix, RationalMatrix>> ps = P.chooseLogarithmicOrder(
                map(
                        q -> q.b,
                        P.dependentPairsInfiniteSquareRootOrder(
                                P.pairs(P.withScale(4).positiveIntegersGeometric()),
                                p -> P.pairs(P.withScale(4).rationalMatrices(p.a, p.b))
                        )
                ),
                P.choose(
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(0, i);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).naturalIntegersGeometric()
                        ),
                        map(
                                i -> {
                                    RationalMatrix m = RationalMatrix.zero(i, 0);
                                    return new Pair<>(m, m);
                                },
                                P.withScale(4).positiveIntegersGeometric()
                        )
                )
        );
        for (Pair<RationalMatrix, RationalMatrix> m : take(MEDIUM_LIMIT, ps)) {
            System.out.println(m.a + " + " + m.b + " = " + m.a.add(m.b));
        }
    }

    private void demoNegate() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("-" + m + " = " + m.negate());
        }
    }

    private void demoEquals_RationalMatrix() {
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, P.pairs(P.withScale(4).rationalMatrices()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            //noinspection ObjectEqualsNull
            System.out.println(m + (m.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (RationalMatrix m : take(LIMIT, P.withScale(4).rationalMatrices())) {
            System.out.println("hashCode(" + m + ") = " + m.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<RationalMatrix, RationalMatrix> p : take(LIMIT, P.pairs(P.withScale(4).rationalMatrices()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_MATRIX_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_MATRIX_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (RationalMatrix m : take(LIMIT, P.rationalMatrices())) {
            System.out.println(m);
        }
    }
}
