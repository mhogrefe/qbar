package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

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
                m -> P.uniformSample(toList(range(0, m.height() - 1)))
        );
        for (Pair<RationalPolynomialMatrix, Integer> p : take(LIMIT, ps)) {
            System.out.println("row(" + p.a + ", " + p.b + ") = " + p.a.row(p.b));
        }
    }

    private void demoColumn() {
        Iterable<Pair<RationalPolynomialMatrix, Integer>> ps = P.dependentPairs(
                filterInfinite(m -> m.width() > 0, P.withScale(4).rationalPolynomialMatrices()),
                m -> P.uniformSample(toList(range(0, m.width() - 1)))
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
                                toList(EP.pairsLex(range(0, m.height() - 1), toList(range(0, m.width() - 1))))
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
            String listString = tail(init(vs.toString()));
            System.out.println("fromRows(" + listString + ") = " + fromRows(vs));
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
            String listString = tail(init(vs.toString()));
            System.out.println("fromColumns(" + listString + ") = " + fromColumns(vs));
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
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_MATRIX_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoFindIn() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_targeted() {
        for (String s : take(LIMIT, P.strings(RATIONAL_POLYNOMIAL_MATRIX_CHARS))) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoToString() {
        for (RationalPolynomialMatrix m : take(LIMIT, P.rationalPolynomialMatrices())) {
            System.out.println(m);
        }
    }
}
