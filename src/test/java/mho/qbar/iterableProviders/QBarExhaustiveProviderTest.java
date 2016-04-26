package mho.qbar.iterableProviders;

import mho.qbar.objects.Algebraic;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.qbar.objects.Variable;
import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.testing.QBarTesting.QEP;
import static mho.qbar.testing.QBarTesting.aeqitLimitQBarLog;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class QBarExhaustiveProviderTest {
    private static <T> void simpleProviderHelper(@NotNull Iterable<T> xs, @NotNull String output) {
        aeqitLimitQBarLog(SMALLER_LIMIT, xs, output);
        testNoRemove(SMALLER_LIMIT, xs);
    }

    @Test
    public void testPositiveRationals() {
        simpleProviderHelper(QEP.positiveRationals(), "QBarExhaustiveProvider_positiveRationals");
    }

    @Test
    public void testNegativeRationals() {
        simpleProviderHelper(QEP.negativeRationals(), "QBarExhaustiveProvider_negativeRationals");
    }

    @Test
    public void testNonzeroRationals() {
        simpleProviderHelper(QEP.nonzeroRationals(), "QBarExhaustiveProvider_nonzeroRationals");
    }

    @Test
    public void testRationals() {
        simpleProviderHelper(QEP.rationals(), "QBarExhaustiveProvider_rationals");
    }

    @Test
    public void testNonNegativeRationalsLessThanOne() {
        simpleProviderHelper(QEP.nonNegativeRationalsLessThanOne(),
                "QBarExhaustiveProvider_nonNegativeRationalsLessThanOne");
    }

    private static void rangeUp_Rational_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rangeUp(Rational.readStrict(a).get()), output);
    }

    @Test
    public void testRangeUp_Rational() {
        rangeUp_Rational_helper("0", "QBarExhaustiveProvider_rangeUp_Rational_i");
        rangeUp_Rational_helper("1", "QBarExhaustiveProvider_rangeUp_Rational_ii");
        rangeUp_Rational_helper("2", "QBarExhaustiveProvider_rangeUp_Rational_iii");
        rangeUp_Rational_helper("-2", "QBarExhaustiveProvider_rangeUp_Rational_iv");
        rangeUp_Rational_helper("5/3", "QBarExhaustiveProvider_rangeUp_Rational_v");
        rangeUp_Rational_helper("-5/3", "QBarExhaustiveProvider_rangeUp_Rational_vi");
    }

    private static void rangeDown_Rational_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rangeDown(Rational.readStrict(a).get()), output);
    }

    @Test
    public void testRangeDown_Rational() {
        rangeDown_Rational_helper("0", "QBarExhaustiveProvider_rangeDown_Rational_i");
        rangeDown_Rational_helper("1", "QBarExhaustiveProvider_rangeDown_Rational_ii");
        rangeDown_Rational_helper("2", "QBarExhaustiveProvider_rangeDown_Rational_iii");
        rangeDown_Rational_helper("-2", "QBarExhaustiveProvider_rangeDown_Rational_iv");
        rangeDown_Rational_helper("5/3", "QBarExhaustiveProvider_rangeDown_Rational_v");
        rangeDown_Rational_helper("-5/3", "QBarExhaustiveProvider_rangeDown_Rational_vi");
    }

    private static void range_Rational_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        simpleProviderHelper(QEP.range(Rational.readStrict(a).get(), Rational.readStrict(b).get()), output);
    }

    @Test
    public void testRange_Rational_Rational() {
        range_Rational_Rational_helper("1", "0", "QBarExhaustiveProvider_range_Rational_Rational_i");
        range_Rational_Rational_helper("1/2", "1/3", "QBarExhaustiveProvider_range_Rational_Rational_ii");
        range_Rational_Rational_helper("0", "0", "QBarExhaustiveProvider_range_Rational_Rational_iii");
        range_Rational_Rational_helper("5/3", "5/3", "QBarExhaustiveProvider_range_Rational_Rational_iv");
        range_Rational_Rational_helper("0", "1", "QBarExhaustiveProvider_range_Rational_Rational_v");
        range_Rational_Rational_helper("-1", "0", "QBarExhaustiveProvider_range_Rational_Rational_vi");
        range_Rational_Rational_helper("1/3", "1/2", "QBarExhaustiveProvider_range_Rational_Rational_vii");
        range_Rational_Rational_helper("-1", "5/3", "QBarExhaustiveProvider_range_Rational_Rational_viii");
    }

    @Test
    public void testFinitelyBoundedIntervals() {
        simpleProviderHelper(QEP.finitelyBoundedIntervals(), "QBarExhaustiveProvider_finitelyBoundedIntervals");
    }

    @Test
    public void testIntervals() {
        simpleProviderHelper(QEP.intervals(), "QBarExhaustiveProvider_intervals");
    }

    private static void rationalsIn_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rationalsIn(Interval.readStrict(a).get()), output);
    }

    @Test
    public void testRationalsIn() {
        rationalsIn_helper("[0, 0]", "QBarExhaustiveProvider_rationalsIn_i");
        rationalsIn_helper("[1, 1]", "QBarExhaustiveProvider_rationalsIn_ii");
        rationalsIn_helper("(-Infinity, Infinity)", "QBarExhaustiveProvider_rationalsIn_iii");
        rationalsIn_helper("[1, 4]", "QBarExhaustiveProvider_rationalsIn_iv");
        rationalsIn_helper("(-Infinity, 1/2]", "QBarExhaustiveProvider_rationalsIn_v");
        rationalsIn_helper("[1/2, Infinity)", "QBarExhaustiveProvider_rationalsIn_vi");
    }

    private static void rationalsNotIn_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rationalsNotIn(Interval.readStrict(a).get()), output);
    }

    @Test
    public void testRationalsNotIn() {
        rationalsNotIn_helper("[0, 0]", "QBarExhaustiveProvider_rationalsNotIn_i");
        rationalsNotIn_helper("[1, 1]", "QBarExhaustiveProvider_rationalsNotIn_ii");
        rationalsNotIn_helper("(-Infinity, Infinity)", "QBarExhaustiveProvider_rationalsNotIn_iii");
        rationalsNotIn_helper("[1, 4]", "QBarExhaustiveProvider_rationalsNotIn_iv");
        rationalsNotIn_helper("(-Infinity, 1/2]", "QBarExhaustiveProvider_rationalsNotIn_v");
        rationalsNotIn_helper("[1/2, Infinity)", "QBarExhaustiveProvider_rationalsNotIn_vi");
    }

    private static void vectors_int_helper(int dimension, @NotNull String output) {
        simpleProviderHelper(QEP.vectors(dimension), output);
    }

    private static void vectors_int_fail_helper(int dimension) {
        try {
            QEP.vectors(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testVectors_int() {
        vectors_int_helper(0, "QBarExhaustiveProvider_vectors_int_i");
        vectors_int_helper(1, "QBarExhaustiveProvider_vectors_int_ii");
        vectors_int_helper(2, "QBarExhaustiveProvider_vectors_int_iii");
        vectors_int_helper(3, "QBarExhaustiveProvider_vectors_int_iv");
        vectors_int_helper(10, "QBarExhaustiveProvider_vectors_int_v");
        vectors_int_fail_helper(-1);
    }

    @Test
    public void testVectors() {
        simpleProviderHelper(QEP.vectors(), "QBarExhaustiveProvider_vectors");
    }

    private static void vectorsAtLeast_helper(int minDimension, @NotNull String output) {
        simpleProviderHelper(QEP.vectorsAtLeast(minDimension), output);
    }

    private static void vectorsAtLeast_fail_helper(int minDimension) {
        try {
            QEP.vectorsAtLeast(minDimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testVectorsAtLeast() {
        vectorsAtLeast_helper(0, "QBarExhaustiveProvider_vectorsAtLeast_i");
        vectorsAtLeast_helper(1, "QBarExhaustiveProvider_vectorsAtLeast_ii");
        vectorsAtLeast_helper(2, "QBarExhaustiveProvider_vectorsAtLeast_iii");
        vectorsAtLeast_helper(3, "QBarExhaustiveProvider_vectorsAtLeast_iv");
        vectorsAtLeast_helper(10, "QBarExhaustiveProvider_vectorsAtLeast_v");
        vectorsAtLeast_fail_helper(-1);
    }

    private static void rationalVectors_int_helper(int dimension, @NotNull String output) {
        simpleProviderHelper(QEP.rationalVectors(dimension), output);
    }

    private static void rationalVectors_int_fail_helper(int dimension) {
        try {
            QEP.rationalVectors(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalVectors_int() {
        rationalVectors_int_helper(0, "QBarExhaustiveProvider_rationalVectors_int_i");
        rationalVectors_int_helper(1, "QBarExhaustiveProvider_rationalVectors_int_ii");
        rationalVectors_int_helper(2, "QBarExhaustiveProvider_rationalVectors_int_iii");
        rationalVectors_int_helper(3, "QBarExhaustiveProvider_rationalVectors_int_iv");
        rationalVectors_int_helper(10, "QBarExhaustiveProvider_rationalVectors_int_v");
        rationalVectors_int_fail_helper(-1);
    }

    @Test
    public void testRationalVectors() {
        simpleProviderHelper(QEP.rationalVectors(), "QBarExhaustiveProvider_rationalVectors");
    }

    private static void rationalVectorsAtLeast_helper(int minDimension, @NotNull String output) {
        simpleProviderHelper(QEP.rationalVectorsAtLeast(minDimension), output);
    }

    private static void rationalVectorsAtLeast_fail_helper(int minDimension) {
        try {
            QEP.rationalVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalVectorsAtLeast() {
        rationalVectorsAtLeast_helper(0, "QBarExhaustiveProvider_rationalVectorsAtLeast_i");
        rationalVectorsAtLeast_helper(1, "QBarExhaustiveProvider_rationalVectorsAtLeast_ii");
        rationalVectorsAtLeast_helper(2, "QBarExhaustiveProvider_rationalVectorsAtLeast_iii");
        rationalVectorsAtLeast_helper(3, "QBarExhaustiveProvider_rationalVectorsAtLeast_iv");
        rationalVectorsAtLeast_helper(10, "QBarExhaustiveProvider_rationalVectorsAtLeast_v");
        rationalVectorsAtLeast_fail_helper(-1);
    }

    private static void reducedRationalVectors_int_helper(int dimension, @NotNull String output) {
        simpleProviderHelper(QEP.reducedRationalVectors(dimension), output);
    }

    private static void reducedRationalVectors_int_fail_helper(int dimension) {
        try {
            QEP.reducedRationalVectors(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testReducedRationalVectors_int() {
        reducedRationalVectors_int_helper(0, "QBarExhaustiveProvider_reducedRationalVectors_int_i");
        reducedRationalVectors_int_helper(1, "QBarExhaustiveProvider_reducedRationalVectors_int_ii");
        reducedRationalVectors_int_helper(2, "QBarExhaustiveProvider_reducedRationalVectors_int_iii");
        reducedRationalVectors_int_helper(3, "QBarExhaustiveProvider_reducedRationalVectors_int_iv");
        reducedRationalVectors_int_helper(10, "QBarExhaustiveProvider_reducedRationalVectors_int_v");
        reducedRationalVectors_int_fail_helper(-1);
    }

    @Test
    public void testReducedRationalVectors() {
        simpleProviderHelper(QEP.reducedRationalVectors(), "QBarExhaustiveProvider_reducedRationalVectors");
    }

    private static void reducedRationalVectorsAtLeast_helper(int minDimension, @NotNull String output) {
        simpleProviderHelper(QEP.reducedRationalVectorsAtLeast(minDimension), output);
    }

    private static void reducedRationalVectorsAtLeast_fail_helper(int minDimension) {
        try {
            QEP.reducedRationalVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testReducedRationalVectorsAtLeast() {
        reducedRationalVectorsAtLeast_helper(0, "QBarExhaustiveProvider_reducedRationalVectorsAtLeast_i");
        reducedRationalVectorsAtLeast_helper(1, "QBarExhaustiveProvider_reducedRationalVectorsAtLeast_ii");
        reducedRationalVectorsAtLeast_helper(2, "QBarExhaustiveProvider_reducedRationalVectorsAtLeast_iii");
        reducedRationalVectorsAtLeast_helper(3, "QBarExhaustiveProvider_reducedRationalVectorsAtLeast_iv");
        reducedRationalVectorsAtLeast_helper(10, "QBarExhaustiveProvider_reducedRationalVectorsAtLeast_v");
        reducedRationalVectorsAtLeast_fail_helper(-1);
    }

    private static void polynomialVectors_int_helper(int dimension, @NotNull String output) {
        simpleProviderHelper(QEP.polynomialVectors(dimension), output);
    }

    private static void polynomialVectors_int_fail_helper(int dimension) {
        try {
            QEP.polynomialVectors(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPolynomialVectors_int() {
        polynomialVectors_int_helper(0, "QBarExhaustiveProvider_polynomialVectors_int_i");
        polynomialVectors_int_helper(1, "QBarExhaustiveProvider_polynomialVectors_int_ii");
        polynomialVectors_int_helper(2, "QBarExhaustiveProvider_polynomialVectors_int_iii");
        polynomialVectors_int_helper(3, "QBarExhaustiveProvider_polynomialVectors_int_iv");
        polynomialVectors_int_helper(10, "QBarExhaustiveProvider_polynomialVectors_int_v");
        polynomialVectors_int_fail_helper(-1);
    }

    @Test
    public void testPolynomialVectors() {
        simpleProviderHelper(QEP.polynomialVectors(), "QBarExhaustiveProvider_polynomialVectors");
    }

    private static void polynomialVectorsAtLeast_helper(int minDimension, @NotNull String output) {
        simpleProviderHelper(QEP.polynomialVectorsAtLeast(minDimension), output);
    }

    private static void polynomialVectorsAtLeast_fail_helper(int minDimension) {
        try {
            QEP.polynomialVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPolynomialVectorsAtLeast() {
        polynomialVectorsAtLeast_helper(0, "QBarExhaustiveProvider_polynomialVectorsAtLeast_i");
        polynomialVectorsAtLeast_helper(1, "QBarExhaustiveProvider_polynomialVectorsAtLeast_ii");
        polynomialVectorsAtLeast_helper(2, "QBarExhaustiveProvider_polynomialVectorsAtLeast_iii");
        polynomialVectorsAtLeast_helper(3, "QBarExhaustiveProvider_polynomialVectorsAtLeast_iv");
        polynomialVectorsAtLeast_helper(10, "QBarExhaustiveProvider_polynomialVectorsAtLeast_v");
        polynomialVectorsAtLeast_fail_helper(-1);
    }

    private static void rationalPolynomialVectors_int_helper(int dimension, @NotNull String output) {
        simpleProviderHelper(QEP.rationalPolynomialVectors(dimension), output);
    }

    private static void rationalPolynomialVectors_int_fail_helper(int dimension) {
        try {
            QEP.rationalPolynomialVectors(dimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalPolynomialVectors_int() {
        rationalPolynomialVectors_int_helper(0, "QBarExhaustiveProvider_rationalPolynomialVectors_int_i");
        rationalPolynomialVectors_int_helper(1, "QBarExhaustiveProvider_rationalPolynomialVectors_int_ii");
        rationalPolynomialVectors_int_helper(2, "QBarExhaustiveProvider_rationalPolynomialVectors_int_iii");
        rationalPolynomialVectors_int_helper(3, "QBarExhaustiveProvider_rationalPolynomialVectors_int_iv");
        rationalPolynomialVectors_int_helper(10, "QBarExhaustiveProvider_rationalPolynomialVectors_int_v");
        rationalPolynomialVectors_int_fail_helper(-1);
    }

    @Test
    public void testRationalPolynomialVectors() {
        simpleProviderHelper(QEP.rationalPolynomialVectors(), "QBarExhaustiveProvider_rationalPolynomialVectors");
    }

    private static void rationalPolynomialVectorsAtLeast_helper(int minDimension, @NotNull String output) {
        simpleProviderHelper(QEP.rationalPolynomialVectorsAtLeast(minDimension), output);
    }

    private static void rationalPolynomialVectorsAtLeast_fail_helper(int minDimension) {
        try {
            QEP.rationalPolynomialVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalPolynomialVectorsAtLeast() {
        rationalPolynomialVectorsAtLeast_helper(0, "QBarExhaustiveProvider_rationalPolynomialVectorsAtLeast_i");
        rationalPolynomialVectorsAtLeast_helper(1, "QBarExhaustiveProvider_rationalPolynomialVectorsAtLeast_ii");
        rationalPolynomialVectorsAtLeast_helper(2, "QBarExhaustiveProvider_rationalPolynomialVectorsAtLeast_iii");
        rationalPolynomialVectorsAtLeast_helper(3, "QBarExhaustiveProvider_rationalPolynomialVectorsAtLeast_iv");
        rationalPolynomialVectorsAtLeast_helper(10, "QBarExhaustiveProvider_rationalPolynomialVectorsAtLeast_v");
        rationalPolynomialVectorsAtLeast_fail_helper(-1);
    }

    private static void matrices_int_int_helper(int height, int width, @NotNull String output) {
        simpleProviderHelper(QEP.matrices(height, width), output);
    }

    private static void matrices_int_int_fail_helper(int height, int width) {
        try {
            QEP.matrices(height, width);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testMatrices_int_int() {
        matrices_int_int_helper(0, 0, "QBarExhaustiveProvider_matrices_int_int_i");
        matrices_int_int_helper(0, 3, "QBarExhaustiveProvider_matrices_int_int_ii");
        matrices_int_int_helper(3, 0, "QBarExhaustiveProvider_matrices_int_int_iii");
        matrices_int_int_helper(1, 1, "QBarExhaustiveProvider_matrices_int_int_iv");
        matrices_int_int_helper(2, 2, "QBarExhaustiveProvider_matrices_int_int_v");
        matrices_int_int_helper(3, 4, "QBarExhaustiveProvider_matrices_int_int_vi");
        matrices_int_int_fail_helper(-1, 0);
        matrices_int_int_fail_helper(-1, 1);
        matrices_int_int_fail_helper(0, -1);
        matrices_int_int_fail_helper(1, -1);
        matrices_int_int_fail_helper(-1, -1);
    }

    @Test
    public void testMatrices() {
        simpleProviderHelper(QEP.matrices(), "QBarExhaustiveProvider_matrices");
    }

    @Test
    public void testSquareMatrices() {
        simpleProviderHelper(QEP.squareMatrices(), "QBarExhaustiveProvider_squareMatrices");
    }

    @Test
    public void testInvertibleMatrices() {
        simpleProviderHelper(QEP.invertibleMatrices(), "QBarExhaustiveProvider_invertibleMatrices");
    }

    private static void rationalMatrices_int_int_helper(int height, int width, @NotNull String output) {
        simpleProviderHelper(QEP.rationalMatrices(height, width), output);
    }

    private static void rationalMatrices_int_int_fail_helper(int height, int width) {
        try {
            QEP.rationalMatrices(height, width);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalMatrices_int_int() {
        rationalMatrices_int_int_helper(0, 0, "QBarExhaustiveProvider_rationalMatrices_int_int_i");
        rationalMatrices_int_int_helper(0, 3, "QBarExhaustiveProvider_rationalMatrices_int_int_ii");
        rationalMatrices_int_int_helper(3, 0, "QBarExhaustiveProvider_rationalMatrices_int_int_iii");
        rationalMatrices_int_int_helper(1, 1, "QBarExhaustiveProvider_rationalMatrices_int_int_iv");
        rationalMatrices_int_int_helper(2, 2, "QBarExhaustiveProvider_rationalMatrices_int_int_v");
        rationalMatrices_int_int_helper(3, 4, "QBarExhaustiveProvider_rationalMatrices_int_int_vi");
        rationalMatrices_int_int_fail_helper(-1, 0);
        rationalMatrices_int_int_fail_helper(-1, 1);
        rationalMatrices_int_int_fail_helper(0, -1);
        rationalMatrices_int_int_fail_helper(1, -1);
        rationalMatrices_int_int_fail_helper(-1, -1);
    }

    @Test
    public void testRationalMatrices() {
        simpleProviderHelper(QEP.rationalMatrices(), "QBarExhaustiveProvider_rationalMatrices");
    }

    @Test
    public void testSquareRationalMatrices() {
        simpleProviderHelper(QEP.squareRationalMatrices(), "QBarExhaustiveProvider_squareRationalMatrices");
    }

    @Test
    public void testInvertibleRationalMatrices() {
        simpleProviderHelper(QEP.invertibleRationalMatrices(), "QBarExhaustiveProvider_invertibleRationalMatrices");
    }

    private static void polynomialMatrices_int_int_helper(int height, int width, @NotNull String output) {
        simpleProviderHelper(QEP.polynomialMatrices(height, width), output);
    }

    private static void polynomialMatrices_int_int_fail_helper(int height, int width) {
        try {
            QEP.polynomialMatrices(height, width);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPolynomialMatrices_int_int() {
        polynomialMatrices_int_int_helper(0, 0, "QBarExhaustiveProvider_polynomialMatrices_int_int_i");
        polynomialMatrices_int_int_helper(0, 3, "QBarExhaustiveProvider_polynomialMatrices_int_int_ii");
        polynomialMatrices_int_int_helper(3, 0, "QBarExhaustiveProvider_polynomialMatrices_int_int_iii");
        polynomialMatrices_int_int_helper(1, 1, "QBarExhaustiveProvider_polynomialMatrices_int_int_iv");
        polynomialMatrices_int_int_helper(2, 2, "QBarExhaustiveProvider_polynomialMatrices_int_int_v");
        polynomialMatrices_int_int_helper(3, 4, "QBarExhaustiveProvider_polynomialMatrices_int_int_vi");
        polynomialMatrices_int_int_fail_helper(-1, 0);
        polynomialMatrices_int_int_fail_helper(-1, 1);
        polynomialMatrices_int_int_fail_helper(0, -1);
        polynomialMatrices_int_int_fail_helper(1, -1);
        polynomialMatrices_int_int_fail_helper(-1, -1);
    }

    @Test
    public void testPolynomialMatrices() {
        simpleProviderHelper(QEP.polynomialMatrices(), "QBarExhaustiveProvider_polynomialMatrices");
    }

    @Test
    public void testSquarePolynomialMatrices() {
        simpleProviderHelper(QEP.squarePolynomialMatrices(), "QBarExhaustiveProvider_squarePolynomialMatrices");
    }

    private static void rationalPolynomialMatrices_int_int_helper(int height, int width, @NotNull String output) {
        simpleProviderHelper(QEP.rationalPolynomialMatrices(height, width), output);
    }

    private static void rationalPolynomialMatrices_int_int_fail_helper(int height, int width) {
        try {
            QEP.rationalPolynomialMatrices(height, width);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalPolynomialMatrices_int_int() {
        rationalPolynomialMatrices_int_int_helper(0, 0, "QBarExhaustiveProvider_rationalPolynomialMatrices_int_int_i");
        rationalPolynomialMatrices_int_int_helper(0, 3,
                "QBarExhaustiveProvider_rationalPolynomialMatrices_int_int_ii");
        rationalPolynomialMatrices_int_int_helper(3, 0,
                "QBarExhaustiveProvider_rationalPolynomialMatrices_int_int_iii");
        rationalPolynomialMatrices_int_int_helper(1, 1,
                "QBarExhaustiveProvider_rationalPolynomialMatrices_int_int_iv");
        rationalPolynomialMatrices_int_int_helper(2, 2, "QBarExhaustiveProvider_rationalPolynomialMatrices_int_int_v");
        rationalPolynomialMatrices_int_int_helper(3, 4,
                "QBarExhaustiveProvider_rationalPolynomialMatrices_int_int_vi");
        rationalPolynomialMatrices_int_int_fail_helper(-1, 0);
        rationalPolynomialMatrices_int_int_fail_helper(-1, 1);
        rationalPolynomialMatrices_int_int_fail_helper(0, -1);
        rationalPolynomialMatrices_int_int_fail_helper(1, -1);
        rationalPolynomialMatrices_int_int_fail_helper(-1, -1);
    }

    @Test
    public void testRationalPolynomialMatrices() {
        simpleProviderHelper(QEP.rationalPolynomialMatrices(), "QBarExhaustiveProvider_rationalPolynomialMatrices");
    }

    @Test
    public void testSquareRationalPolynomialMatrices() {
        simpleProviderHelper(QEP.squareRationalPolynomialMatrices(),
                "QBarExhaustiveProvider_squareRationalPolynomialMatrices");
    }

    private static void polynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.polynomials(degree), output);
    }

    private static void polynomials_int_fail_helper(int degree) {
        try {
            QEP.polynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPolynomials_int() {
        polynomials_int_helper(-1, "QBarExhaustiveProvider_polynomials_int_i");
        polynomials_int_helper(0, "QBarExhaustiveProvider_polynomials_int_ii");
        polynomials_int_helper(1, "QBarExhaustiveProvider_polynomials_int_iii");
        polynomials_int_helper(2, "QBarExhaustiveProvider_polynomials_int_iv");
        polynomials_int_helper(9, "QBarExhaustiveProvider_polynomials_int_v");
        polynomials_int_fail_helper(-2);
    }

    @Test
    public void testPolynomials() {
        simpleProviderHelper(QEP.polynomials(), "QBarExhaustiveProvider_polynomials");
    }

    private static void polynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.polynomialsAtLeast(minDegree), output);
    }

    private static void polynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.polynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPolynomialsAtLeast() {
        polynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_polynomialsAtLeast_i");
        polynomialsAtLeast_helper(0, "QBarExhaustiveProvider_polynomialsAtLeast_ii");
        polynomialsAtLeast_helper(1, "QBarExhaustiveProvider_polynomialsAtLeast_iii");
        polynomialsAtLeast_helper(2, "QBarExhaustiveProvider_polynomialsAtLeast_iv");
        polynomialsAtLeast_helper(9, "QBarExhaustiveProvider_polynomialsAtLeast_v");
        polynomialsAtLeast_fail_helper(-2);
    }

    private static void primitivePolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.primitivePolynomials(degree), output);
    }

    private static void primitivePolynomials_int_fail_helper(int degree) {
        try {
            QEP.primitivePolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPrimitivePolynomials_int() {
        primitivePolynomials_int_helper(-1, "QBarExhaustiveProvider_primitivePolynomials_int_i");
        primitivePolynomials_int_helper(0, "QBarExhaustiveProvider_primitivePolynomials_int_ii");
        primitivePolynomials_int_helper(1, "QBarExhaustiveProvider_primitivePolynomials_int_iii");
        primitivePolynomials_int_helper(2, "QBarExhaustiveProvider_primitivePolynomials_int_iv");
        primitivePolynomials_int_helper(8, "QBarExhaustiveProvider_primitivePolynomials_int_v");
        primitivePolynomials_int_fail_helper(-2);
    }

    @Test
    public void testPrimitivePolynomials() {
        simpleProviderHelper(QEP.primitivePolynomials(), "QBarExhaustiveProvider_primitivePolynomials");
    }

    private static void primitivePolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.primitivePolynomialsAtLeast(minDegree), output);
    }

    private static void primitivePolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.primitivePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPrimitivePolynomialsAtLeast() {
        primitivePolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_primitivePolynomialsAtLeast_i");
        primitivePolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_primitivePolynomialsAtLeast_ii");
        primitivePolynomialsAtLeast_helper(1, "QBarExhaustiveProvider_primitivePolynomialsAtLeast_iii");
        primitivePolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_primitivePolynomialsAtLeast_iv");
        primitivePolynomialsAtLeast_helper(8, "QBarExhaustiveProvider_primitivePolynomialsAtLeast_v");
        primitivePolynomialsAtLeast_fail_helper(-2);
    }

    private static void positivePrimitivePolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.positivePrimitivePolynomials(degree), output);
    }

    private static void positivePrimitivePolynomials_int_fail_helper(int degree) {
        try {
            QEP.positivePrimitivePolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositivePrimitivePolynomials_int() {
        positivePrimitivePolynomials_int_helper(-1, "QBarExhaustiveProvider_positivePrimitivePolynomials_int_i");
        positivePrimitivePolynomials_int_helper(0, "QBarExhaustiveProvider_positivePrimitivePolynomials_int_ii");
        positivePrimitivePolynomials_int_helper(1, "QBarExhaustiveProvider_positivePrimitivePolynomials_int_iii");
        positivePrimitivePolynomials_int_helper(2, "QBarExhaustiveProvider_positivePrimitivePolynomials_int_iv");
        positivePrimitivePolynomials_int_helper(8, "QBarExhaustiveProvider_positivePrimitivePolynomials_int_v");
        positivePrimitivePolynomials_int_fail_helper(-2);
    }

    @Test
    public void testPositivePrimitivePolynomials() {
        simpleProviderHelper(QEP.positivePrimitivePolynomials(),
                "QBarExhaustiveProvider_positivePrimitivePolynomials");
    }

    private static void positivePrimitivePolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.positivePrimitivePolynomialsAtLeast(minDegree), output);
    }

    private static void positivePrimitivePolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.positivePrimitivePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositivePrimitivePolynomialsAtLeast() {
        positivePrimitivePolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_positivePrimitivePolynomialsAtLeast_i");
        positivePrimitivePolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_positivePrimitivePolynomialsAtLeast_ii");
        positivePrimitivePolynomialsAtLeast_helper(1,
                "QBarExhaustiveProvider_positivePrimitivePolynomialsAtLeast_iii");
        positivePrimitivePolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_positivePrimitivePolynomialsAtLeast_iv");
        positivePrimitivePolynomialsAtLeast_helper(8, "QBarExhaustiveProvider_positivePrimitivePolynomialsAtLeast_v");
        positivePrimitivePolynomialsAtLeast_fail_helper(-2);
    }

    private static void monicPolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.monicPolynomials(degree), output);
    }

    private static void monicPolynomials_int_fail_helper(int degree) {
        try {
            QEP.monicPolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testMonicPolynomials_int() {
        monicPolynomials_int_helper(-1, "QBarExhaustiveProvider_monicPolynomials_int_i");
        monicPolynomials_int_helper(0, "QBarExhaustiveProvider_monicPolynomials_int_ii");
        monicPolynomials_int_helper(1, "QBarExhaustiveProvider_monicPolynomials_int_iii");
        monicPolynomials_int_helper(2, "QBarExhaustiveProvider_monicPolynomials_int_iv");
        monicPolynomials_int_helper(9, "QBarExhaustiveProvider_monicPolynomials_int_v");
        monicPolynomials_int_fail_helper(-2);
    }

    @Test
    public void testMonicPolynomials() {
        simpleProviderHelper(QEP.monicPolynomials(), "QBarExhaustiveProvider_monicPolynomials");
    }

    private static void monicPolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.monicPolynomialsAtLeast(minDegree), output);
    }

    private static void monicPolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.monicPolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testMonicPolynomialsAtLeast() {
        monicPolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_monicPolynomialsAtLeast_i");
        monicPolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_monicPolynomialsAtLeast_ii");
        monicPolynomialsAtLeast_helper(1, "QBarExhaustiveProvider_monicPolynomialsAtLeast_iii");
        monicPolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_monicPolynomialsAtLeast_iv");
        monicPolynomialsAtLeast_helper(9, "QBarExhaustiveProvider_monicPolynomialsAtLeast_v");
        monicPolynomialsAtLeast_fail_helper(-2);
    }

    private static void squareFreePolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.squareFreePolynomials(degree), output);
    }

    private static void squareFreePolynomials_int_fail_helper(int degree) {
        try {
            QEP.squareFreePolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testSquareFreePolynomials_int() {
        squareFreePolynomials_int_helper(-1, "QBarExhaustiveProvider_squareFreePolynomials_int_i");
        squareFreePolynomials_int_helper(0, "QBarExhaustiveProvider_squareFreePolynomials_int_ii");
        squareFreePolynomials_int_helper(1, "QBarExhaustiveProvider_squareFreePolynomials_int_iii");
        squareFreePolynomials_int_helper(2, "QBarExhaustiveProvider_squareFreePolynomials_int_iv");
        squareFreePolynomials_int_helper(8, "QBarExhaustiveProvider_squareFreePolynomials_int_v");
        squareFreePolynomials_int_fail_helper(-2);
    }

    @Test
    public void testSquareFreePolynomials() {
        simpleProviderHelper(QEP.squareFreePolynomials(), "QBarExhaustiveProvider_squareFreePolynomials");
    }

    private static void squareFreePolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.squareFreePolynomialsAtLeast(minDegree), output);
    }

    private static void squareFreePolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.squareFreePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testSquareFreePolynomialsAtLeast() {
        squareFreePolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_squareFreePolynomialsAtLeast_i");
        squareFreePolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_squareFreePolynomialsAtLeast_ii");
        squareFreePolynomialsAtLeast_helper(1, "QBarExhaustiveProvider_squareFreePolynomialsAtLeast_iii");
        squareFreePolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_squareFreePolynomialsAtLeast_iv");
        squareFreePolynomialsAtLeast_helper(8, "QBarExhaustiveProvider_squareFreePolynomialsAtLeast_v");
        squareFreePolynomialsAtLeast_fail_helper(-2);
    }

    private static void positivePrimitiveSquareFreePolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.positivePrimitiveSquareFreePolynomials(degree), output);
    }

    private static void positivePrimitiveSquareFreePolynomials_int_fail_helper(int degree) {
        try {
            QEP.positivePrimitiveSquareFreePolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositivePrimitiveSquareFreePolynomials_int() {
        positivePrimitiveSquareFreePolynomials_int_helper(-1,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomials_int_i");
        positivePrimitiveSquareFreePolynomials_int_helper(0,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomials_int_ii");
        positivePrimitiveSquareFreePolynomials_int_helper(1,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomials_int_iii");
        positivePrimitiveSquareFreePolynomials_int_helper(2,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomials_int_iv");
        positivePrimitiveSquareFreePolynomials_int_helper(8,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomials_int_v");
        positivePrimitiveSquareFreePolynomials_int_fail_helper(-2);
    }

    @Test
    public void testPositivePrimitiveSquareFreePolynomials() {
        simpleProviderHelper(QEP.positivePrimitiveSquareFreePolynomials(),
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomials");
    }

    private static void positivePrimitiveSquareFreePolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.positivePrimitiveSquareFreePolynomialsAtLeast(minDegree), output);
    }

    private static void positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.positivePrimitiveSquareFreePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositivePrimitiveSquareFreePolynomialsAtLeast() {
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(-1,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomialsAtLeast_i");
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(0,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomialsAtLeast_ii");
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(1,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomialsAtLeast_iii");
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(2,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomialsAtLeast_iv");
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(8,
                "QBarExhaustiveProvider_positivePrimitiveSquareFreePolynomialsAtLeast_v");
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(-2);
    }

    private static void irreduciblePolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.irreduciblePolynomials(degree), output);
    }

    private static void irreduciblePolynomials_int_fail_helper(int degree) {
        try {
            QEP.irreduciblePolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIrreduciblePolynomials_int() {
        irreduciblePolynomials_int_helper(-1, "QBarExhaustiveProvider_irreduciblePolynomials_int_i");
        irreduciblePolynomials_int_helper(0, "QBarExhaustiveProvider_irreduciblePolynomials_int_ii");
        irreduciblePolynomials_int_helper(1, "QBarExhaustiveProvider_irreduciblePolynomials_int_iii");
        irreduciblePolynomials_int_helper(2, "QBarExhaustiveProvider_irreduciblePolynomials_int_iv");
        irreduciblePolynomials_int_helper(8, "QBarExhaustiveProvider_irreduciblePolynomials_int_v");
        irreduciblePolynomials_int_fail_helper(-2);
    }

    @Test
    public void testIrreduciblePolynomials() {
        simpleProviderHelper(QEP.irreduciblePolynomials(), "QBarExhaustiveProvider_irreduciblePolynomials");
    }

    private static void irreduciblePolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.irreduciblePolynomialsAtLeast(minDegree), output);
    }

    private static void irreduciblePolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.irreduciblePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIrreduciblePolynomialsAtLeast() {
        irreduciblePolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_irreduciblePolynomialsAtLeast_i");
        irreduciblePolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_irreduciblePolynomialsAtLeast_ii");
        irreduciblePolynomialsAtLeast_helper(1, "QBarExhaustiveProvider_irreduciblePolynomialsAtLeast_iii");
        irreduciblePolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_irreduciblePolynomialsAtLeast_iv");
        irreduciblePolynomialsAtLeast_helper(8, "QBarExhaustiveProvider_irreduciblePolynomialsAtLeast_v");
        irreduciblePolynomialsAtLeast_fail_helper(-2);
    }

    private static void rationalPolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.rationalPolynomials(degree), output);
    }

    private static void rationalPolynomials_int_fail_helper(int degree) {
        try {
            QEP.rationalPolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalPolynomials_int() {
        rationalPolynomials_int_helper(-1, "QBarExhaustiveProvider_rationalPolynomials_int_i");
        rationalPolynomials_int_helper(0, "QBarExhaustiveProvider_rationalPolynomials_int_ii");
        rationalPolynomials_int_helper(1, "QBarExhaustiveProvider_rationalPolynomials_int_iii");
        rationalPolynomials_int_helper(2, "QBarExhaustiveProvider_rationalPolynomials_int_iv");
        rationalPolynomials_int_helper(9, "QBarExhaustiveProvider_rationalPolynomials_int_v");
        rationalPolynomials_int_fail_helper(-2);
    }

    @Test
    public void testRationalPolynomials() {
        simpleProviderHelper(QEP.rationalPolynomials(), "QBarExhaustiveProvider_rationalPolynomials");
    }

    private static void rationalPolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.rationalPolynomialsAtLeast(minDegree), output);
    }

    private static void rationalPolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.rationalPolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationalPolynomialsAtLeast() {
        rationalPolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_rationalPolynomialsAtLeast_i");
        rationalPolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_rationalPolynomialsAtLeast_ii");
        rationalPolynomialsAtLeast_helper(1, "QBarExhaustiveProvider_rationalPolynomialsAtLeast_iii");
        rationalPolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_rationalPolynomialsAtLeast_iv");
        rationalPolynomialsAtLeast_helper(9, "QBarExhaustiveProvider_rationalPolynomialsAtLeast_v");
        rationalPolynomialsAtLeast_fail_helper(-2);
    }

    private static void monicRationalPolynomials_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.monicRationalPolynomials(degree), output);
    }

    private static void monicRationalPolynomials_int_fail_helper(int degree) {
        try {
            QEP.monicRationalPolynomials(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testMonicRationalPolynomials_int() {
        monicRationalPolynomials_int_helper(-1, "QBarExhaustiveProvider_monicRationalPolynomials_int_i");
        monicRationalPolynomials_int_helper(0, "QBarExhaustiveProvider_monicRationalPolynomials_int_ii");
        monicRationalPolynomials_int_helper(1, "QBarExhaustiveProvider_monicRationalPolynomials_int_iii");
        monicRationalPolynomials_int_helper(2, "QBarExhaustiveProvider_monicRationalPolynomials_int_iv");
        monicRationalPolynomials_int_helper(8, "QBarExhaustiveProvider_monicRationalPolynomials_int_v");
        monicRationalPolynomials_int_fail_helper(-2);
    }

    @Test
    public void testMonicRationalPolynomials() {
        simpleProviderHelper(QEP.monicRationalPolynomials(), "QBarExhaustiveProvider_monicRationalPolynomials");
    }

    private static void monicRationalPolynomialsAtLeast_helper(int minDegree, @NotNull String output) {
        simpleProviderHelper(QEP.monicRationalPolynomialsAtLeast(minDegree), output);
    }

    private static void monicRationalPolynomialsAtLeast_fail_helper(int minDegree) {
        try {
            QEP.monicRationalPolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testMonicRationalPolynomialsAtLeast() {
        monicRationalPolynomialsAtLeast_helper(-1, "QBarExhaustiveProvider_monicRationalPolynomialsAtLeast_i");
        monicRationalPolynomialsAtLeast_helper(0, "QBarExhaustiveProvider_monicRationalPolynomialsAtLeast_ii");
        monicRationalPolynomialsAtLeast_helper(1, "QBarExhaustiveProvider_monicRationalPolynomialsAtLeast_iii");
        monicRationalPolynomialsAtLeast_helper(2, "QBarExhaustiveProvider_monicRationalPolynomialsAtLeast_iv");
        monicRationalPolynomialsAtLeast_helper(8, "QBarExhaustiveProvider_monicRationalPolynomialsAtLeast_v");
        monicRationalPolynomialsAtLeast_fail_helper(-2);
    }

    @Test
    public void testVariables() {
        simpleProviderHelper(QEP.variables(), "QBarExhaustiveProvider_variables");
    }

    @Test
    public void testMonomialOrders() {
        simpleProviderHelper(QEP.monomialOrders(), "QBarExhaustiveProvider_monomialOrders");
    }

    @Test
    public void testExponentVectors() {
        simpleProviderHelper(QEP.exponentVectors(), "QBarExhaustiveProvider_exponentVectors");
    }

    private static void exponentVectors_List_Variable_helper(@NotNull String variables, @NotNull String output) {
        simpleProviderHelper(QEP.exponentVectors(readVariableList(variables)), output);
    }

    private static void exponentVectors_List_Variable_fail_helper(@NotNull String variables) {
        try {
            QEP.exponentVectors(readVariableListWithNulls(variables));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testExponentVectors_List_Variable() {
        exponentVectors_List_Variable_helper("[]", "QBarExhaustiveProvider_exponentVectors_List_Variable_i");
        exponentVectors_List_Variable_helper("[a]", "QBarExhaustiveProvider_exponentVectors_List_Variable_ii");
        exponentVectors_List_Variable_helper("[x]", "QBarExhaustiveProvider_exponentVectors_List_Variable_iii");
        exponentVectors_List_Variable_helper("[ooo]", "QBarExhaustiveProvider_exponentVectors_List_Variable_iv");
        exponentVectors_List_Variable_helper("[x, y]", "QBarExhaustiveProvider_exponentVectors_List_Variable_v");
        exponentVectors_List_Variable_helper("[x, y, z]", "QBarExhaustiveProvider_exponentVectors_List_Variable_vi");

        exponentVectors_List_Variable_fail_helper("[a, a]");
        exponentVectors_List_Variable_fail_helper("[b, a]");
        exponentVectors_List_Variable_fail_helper("[a, null]");
    }

    @Test
    public void testMultivariatePolynomials() {
        simpleProviderHelper(QEP.multivariatePolynomials(), "QBarExhaustiveProvider_multivariatePolynomials");
    }

    private static void multivariatePolynomials_List_Variable_helper(
            @NotNull String variables,
            @NotNull String output
    ) {
        simpleProviderHelper(QEP.multivariatePolynomials(readVariableList(variables)), output);
    }

    private static void multivariatePolynomials_List_Variable_fail_helper(@NotNull String variables) {
        try {
            QEP.multivariatePolynomials(readVariableListWithNulls(variables));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testMultivariatePolynomials_List_Variable() {
        multivariatePolynomials_List_Variable_helper("[]",
                "QBarExhaustiveProvider_multivariatePolynomials_List_Variable_i");
        multivariatePolynomials_List_Variable_helper("[a]",
                "QBarExhaustiveProvider_multivariatePolynomials_List_Variable_ii");
        multivariatePolynomials_List_Variable_helper("[x]",
                "QBarExhaustiveProvider_multivariatePolynomials_List_Variable_iii");
        multivariatePolynomials_List_Variable_helper("[ooo]",
                "QBarExhaustiveProvider_multivariatePolynomials_List_Variable_iv");
        multivariatePolynomials_List_Variable_helper("[x, y]",
                "QBarExhaustiveProvider_multivariatePolynomials_List_Variable_v");
        multivariatePolynomials_List_Variable_helper("[x, y, z]",
                "QBarExhaustiveProvider_multivariatePolynomials_List_Variable_vi");

        multivariatePolynomials_List_Variable_fail_helper("[a, a]");
        multivariatePolynomials_List_Variable_fail_helper("[b, a]");
        multivariatePolynomials_List_Variable_fail_helper("[a, null]");
    }

    private static void positiveAlgebraics_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.positiveAlgebraics(degree), output);
    }

    private static void positiveAlgebraics_int_fail_helper(int degree) {
        try {
            QEP.positiveAlgebraics(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositiveAlgebraics_int() {
        positiveAlgebraics_int_helper(1, "QBarExhaustiveProvider_positiveAlgebraics_int_i");
        positiveAlgebraics_int_helper(2, "QBarExhaustiveProvider_positiveAlgebraics_int_ii");
        positiveAlgebraics_int_helper(3, "QBarExhaustiveProvider_positiveAlgebraics_int_iii");
        positiveAlgebraics_int_helper(4, "QBarExhaustiveProvider_positiveAlgebraics_int_iv");
        positiveAlgebraics_int_helper(10, "QBarExhaustiveProvider_positiveAlgebraics_int_v");

        positiveAlgebraics_int_fail_helper(0);
        positiveAlgebraics_int_fail_helper(-1);
    }

    @Test
    public void testPositiveAlgebraics() {
        simpleProviderHelper(QEP.positiveAlgebraics(), "QBarExhaustiveProvider_positiveAlgebraics");
    }

    private static void negativeAlgebraics_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.negativeAlgebraics(degree), output);
    }

    private static void negativeAlgebraics_int_fail_helper(int degree) {
        try {
            QEP.negativeAlgebraics(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNegativeAlgebraics_int() {
        negativeAlgebraics_int_helper(1, "QBarExhaustiveProvider_negativeAlgebraics_int_i");
        negativeAlgebraics_int_helper(2, "QBarExhaustiveProvider_negativeAlgebraics_int_ii");
        negativeAlgebraics_int_helper(3, "QBarExhaustiveProvider_negativeAlgebraics_int_iii");
        negativeAlgebraics_int_helper(4, "QBarExhaustiveProvider_negativeAlgebraics_int_iv");
        negativeAlgebraics_int_helper(10, "QBarExhaustiveProvider_negativeAlgebraics_int_v");

        negativeAlgebraics_int_fail_helper(0);
        negativeAlgebraics_int_fail_helper(-1);
    }

    @Test
    public void testNegativeAlgebraics() {
        simpleProviderHelper(QEP.negativeAlgebraics(), "QBarExhaustiveProvider_negativeAlgebraics");
    }

    private static void nonzeroAlgebraics_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.nonzeroAlgebraics(degree), output);
    }

    private static void nonzeroAlgebraics_int_fail_helper(int degree) {
        try {
            QEP.nonzeroAlgebraics(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNonzeroAlgebraics_int() {
        nonzeroAlgebraics_int_helper(1, "QBarExhaustiveProvider_nonzeroAlgebraics_int_i");
        nonzeroAlgebraics_int_helper(2, "QBarExhaustiveProvider_nonzeroAlgebraics_int_ii");
        nonzeroAlgebraics_int_helper(3, "QBarExhaustiveProvider_nonzeroAlgebraics_int_iii");
        nonzeroAlgebraics_int_helper(4, "QBarExhaustiveProvider_nonzeroAlgebraics_int_iv");
        nonzeroAlgebraics_int_helper(10, "QBarExhaustiveProvider_nonzeroAlgebraics_int_v");

        nonzeroAlgebraics_int_fail_helper(0);
        nonzeroAlgebraics_int_fail_helper(-1);
    }

    @Test
    public void testNonzeroAlgebraics() {
        simpleProviderHelper(QEP.nonzeroAlgebraics(), "QBarExhaustiveProvider_nonzeroAlgebraics");
    }

    private static void algebraics_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.algebraics(degree), output);
    }

    private static void algebraics_int_fail_helper(int degree) {
        try {
            QEP.algebraics(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testAlgebraics_int() {
        algebraics_int_helper(1, "QBarExhaustiveProvider_algebraics_int_i");
        algebraics_int_helper(2, "QBarExhaustiveProvider_algebraics_int_ii");
        algebraics_int_helper(3, "QBarExhaustiveProvider_algebraics_int_iii");
        algebraics_int_helper(4, "QBarExhaustiveProvider_algebraics_int_iv");
        algebraics_int_helper(10, "QBarExhaustiveProvider_algebraics_int_v");

        algebraics_int_fail_helper(0);
        algebraics_int_fail_helper(-1);
    }

    @Test
    public void testAlgebraics() {
        simpleProviderHelper(QEP.algebraics(), "QBarExhaustiveProvider_algebraics");
    }

    private static void nonNegativeAlgebraicsLessThanOne_int_helper(int degree, @NotNull String output) {
        simpleProviderHelper(QEP.nonNegativeAlgebraicsLessThanOne(degree), output);
    }

    private static void nonNegativeAlgebraicsLessThanOne_int_fail_helper(int degree) {
        try {
            QEP.nonNegativeAlgebraicsLessThanOne(degree);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNonNegativeAlgebraicsLessThanOne_int() {
        nonNegativeAlgebraicsLessThanOne_int_helper(
                1,
                "QBarExhaustiveProvider_nonNegativeAlgebraicsLessThanOne_int_i"
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                2,
                "QBarExhaustiveProvider_nonNegativeAlgebraicsLessThanOne_int_ii"
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                3,
                "QBarExhaustiveProvider_nonNegativeAlgebraicsLessThanOne_int_iii"
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                4,
                "QBarExhaustiveProvider_nonNegativeAlgebraicsLessThanOne_int_iv"
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                10,
                "QBarExhaustiveProvider_nonNegativeAlgebraicsLessThanOne_int_v"
        );

        nonNegativeAlgebraicsLessThanOne_int_fail_helper(0);
        nonNegativeAlgebraicsLessThanOne_int_fail_helper(-1);
    }

    @Test
    public void testNonNegativeAlgebraicsLessThanOne() {
        simpleProviderHelper(QEP.nonNegativeAlgebraicsLessThanOne(),
                "QBarExhaustiveProvider_nonNegativeAlgebraicsLessThanOne");
    }

    private static void rangeUp_int_Algebraic_helper(int degree, @NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rangeUp(degree, Algebraic.readStrict(a).get()), output);
    }

    private static void rangeUp_int_Algebraic_fail_helper(int degree, @NotNull String a) {
        try {
            QEP.rangeUp(degree, Algebraic.readStrict(a).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRangeUp_int_Algebraic() {
        rangeUp_int_Algebraic_helper(1, "0", "QBarExhaustiveProvider_rangeUp_int_Algebraic_i");
        rangeUp_int_Algebraic_helper(2, "0", "QBarExhaustiveProvider_rangeUp_int_Algebraic_ii");
        rangeUp_int_Algebraic_helper(3, "0", "QBarExhaustiveProvider_rangeUp_int_Algebraic_iii");

        rangeUp_int_Algebraic_helper(1, "1", "QBarExhaustiveProvider_rangeUp_int_Algebraic_iv");
        rangeUp_int_Algebraic_helper(2, "1", "QBarExhaustiveProvider_rangeUp_int_Algebraic_v");
        rangeUp_int_Algebraic_helper(3, "1", "QBarExhaustiveProvider_rangeUp_int_Algebraic_vi");

        rangeUp_int_Algebraic_helper(1, "1/2", "QBarExhaustiveProvider_rangeUp_int_Algebraic_vii");
        rangeUp_int_Algebraic_helper(2, "1/2", "QBarExhaustiveProvider_rangeUp_int_Algebraic_viii");
        rangeUp_int_Algebraic_helper(3, "1/2", "QBarExhaustiveProvider_rangeUp_int_Algebraic_ix");

        rangeUp_int_Algebraic_helper(1, "-4/3", "QBarExhaustiveProvider_rangeUp_int_Algebraic_x");
        rangeUp_int_Algebraic_helper(2, "-4/3", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xi");
        rangeUp_int_Algebraic_helper(3, "-4/3", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xii");

        rangeUp_int_Algebraic_helper(1, "sqrt(2)", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xiii");
        rangeUp_int_Algebraic_helper(2, "sqrt(2)", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xiv");
        rangeUp_int_Algebraic_helper(3, "sqrt(2)", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xv");

        rangeUp_int_Algebraic_helper(1, "-sqrt(2)", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xvi");
        rangeUp_int_Algebraic_helper(2, "-sqrt(2)", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xvii");
        rangeUp_int_Algebraic_helper(3, "-sqrt(2)", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xviii");

        rangeUp_int_Algebraic_helper(1, "(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xix");
        rangeUp_int_Algebraic_helper(2, "(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xx");
        rangeUp_int_Algebraic_helper(3, "(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xxi");

        rangeUp_int_Algebraic_helper(1, "root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xxii");
        rangeUp_int_Algebraic_helper(2, "root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xxiii");
        rangeUp_int_Algebraic_helper(3, "root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeUp_int_Algebraic_xxiv");

        rangeUp_int_Algebraic_fail_helper(0, "1/2");
        rangeUp_int_Algebraic_fail_helper(-1, "1/2");
        rangeUp_int_Algebraic_fail_helper(0, "sqrt(2)");
        rangeUp_int_Algebraic_fail_helper(-1, "sqrt(2)");
    }

    private static void rangeUp_Algebraic_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rangeUp(Algebraic.readStrict(a).get()), output);
    }

    @Test
    public void testRangeUp_Algebraic() {
        rangeUp_Algebraic_helper("0", "QBarExhaustiveProvider_rangeUp_Algebraic_i");
        rangeUp_Algebraic_helper("1", "QBarExhaustiveProvider_rangeUp_Algebraic_ii");
        rangeUp_Algebraic_helper("1/2", "QBarExhaustiveProvider_rangeUp_Algebraic_iii");
        rangeUp_Algebraic_helper("-4/3", "QBarExhaustiveProvider_rangeUp_Algebraic_iv");
        rangeUp_Algebraic_helper("sqrt(2)", "QBarExhaustiveProvider_rangeUp_Algebraic_v");
        rangeUp_Algebraic_helper("-sqrt(2)", "QBarExhaustiveProvider_rangeUp_Algebraic_vi");
        rangeUp_Algebraic_helper("(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeUp_Algebraic_vii");
        rangeUp_Algebraic_helper("root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeUp_Algebraic_viii");
    }

    private static void rangeDown_int_Algebraic_helper(int degree, @NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rangeDown(degree, Algebraic.readStrict(a).get()), output);
    }

    private static void rangeDown_int_Algebraic_fail_helper(int degree, @NotNull String a) {
        try {
            QEP.rangeDown(degree, Algebraic.readStrict(a).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRangeDown_int_Algebraic() {
        rangeDown_int_Algebraic_helper(1, "0", "QBarExhaustiveProvider_rangeDown_int_Algebraic_i");
        rangeDown_int_Algebraic_helper(2, "0", "QBarExhaustiveProvider_rangeDown_int_Algebraic_ii");
        rangeDown_int_Algebraic_helper(3, "0", "QBarExhaustiveProvider_rangeDown_int_Algebraic_iii");

        rangeDown_int_Algebraic_helper(1, "1", "QBarExhaustiveProvider_rangeDown_int_Algebraic_iv");
        rangeDown_int_Algebraic_helper(2, "1", "QBarExhaustiveProvider_rangeDown_int_Algebraic_v");
        rangeDown_int_Algebraic_helper(3, "1", "QBarExhaustiveProvider_rangeDown_int_Algebraic_vi");

        rangeDown_int_Algebraic_helper(1, "1/2", "QBarExhaustiveProvider_rangeDown_int_Algebraic_vii");
        rangeDown_int_Algebraic_helper(2, "1/2", "QBarExhaustiveProvider_rangeDown_int_Algebraic_viii");
        rangeDown_int_Algebraic_helper(3, "1/2", "QBarExhaustiveProvider_rangeDown_int_Algebraic_ix");

        rangeDown_int_Algebraic_helper(1, "-4/3", "QBarExhaustiveProvider_rangeDown_int_Algebraic_x");
        rangeDown_int_Algebraic_helper(2, "-4/3", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xi");
        rangeDown_int_Algebraic_helper(3, "-4/3", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xii");

        rangeDown_int_Algebraic_helper(1, "sqrt(2)", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xiii");
        rangeDown_int_Algebraic_helper(2, "sqrt(2)", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xiv");
        rangeDown_int_Algebraic_helper(3, "sqrt(2)", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xv");

        rangeDown_int_Algebraic_helper(1, "-sqrt(2)", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xvi");
        rangeDown_int_Algebraic_helper(2, "-sqrt(2)", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xvii");
        rangeDown_int_Algebraic_helper(3, "-sqrt(2)", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xviii");

        rangeDown_int_Algebraic_helper(1, "(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xix");
        rangeDown_int_Algebraic_helper(2, "(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xx");
        rangeDown_int_Algebraic_helper(3, "(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xxi");

        rangeDown_int_Algebraic_helper(1, "root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xxii");
        rangeDown_int_Algebraic_helper(2, "root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xxiii");
        rangeDown_int_Algebraic_helper(3, "root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeDown_int_Algebraic_xxiv");

        rangeDown_int_Algebraic_fail_helper(0, "1/2");
        rangeDown_int_Algebraic_fail_helper(-1, "1/2");
        rangeDown_int_Algebraic_fail_helper(0, "sqrt(2)");
        rangeDown_int_Algebraic_fail_helper(-1, "sqrt(2)");
    }

    private static void rangeDown_Algebraic_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.rangeDown(Algebraic.readStrict(a).get()), output);
    }

    @Test
    public void testRangeDown_Algebraic() {
        rangeDown_Algebraic_helper("0", "QBarExhaustiveProvider_rangeDown_Algebraic_i");
        rangeDown_Algebraic_helper("1", "QBarExhaustiveProvider_rangeDown_Algebraic_ii");
        rangeDown_Algebraic_helper("1/2", "QBarExhaustiveProvider_rangeDown_Algebraic_iii");
        rangeDown_Algebraic_helper("-4/3", "QBarExhaustiveProvider_rangeDown_Algebraic_iv");
        rangeDown_Algebraic_helper("sqrt(2)", "QBarExhaustiveProvider_rangeDown_Algebraic_v");
        rangeDown_Algebraic_helper("-sqrt(2)", "QBarExhaustiveProvider_rangeDown_Algebraic_vi");
        rangeDown_Algebraic_helper("(1+sqrt(5))/2", "QBarExhaustiveProvider_rangeDown_Algebraic_vii");
        rangeDown_Algebraic_helper("root 0 of x^5-x-1", "QBarExhaustiveProvider_rangeDown_Algebraic_viii");
    }

    private static void range_int_Algebraic_Algebraic_helper(
            int degree,
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        simpleProviderHelper(QEP.range(degree, Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get()), output);
    }

    private static void range_int_Algebraic_Algebraic_fail_helper(int degree, @NotNull String a, @NotNull String b) {
        try {
            QEP.range(degree, Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRange_int_Algebraic_Algebraic() {
        range_int_Algebraic_Algebraic_helper(1, "0", "0", "QBarExhaustiveProvider_int_range_Algebraic_Algebraic_i");
        range_int_Algebraic_Algebraic_helper(2, "0", "0", "QBarExhaustiveProvider_int_range_Algebraic_Algebraic_ii");
        range_int_Algebraic_Algebraic_helper(3, "0", "0", "QBarExhaustiveProvider_int_range_Algebraic_Algebraic_iii");

        range_int_Algebraic_Algebraic_helper(1, "1", "1", "QBarExhaustiveProvider_int_range_Algebraic_Algebraic_iv");
        range_int_Algebraic_Algebraic_helper(2, "1", "1", "QBarExhaustiveProvider_int_range_Algebraic_Algebraic_v");
        range_int_Algebraic_Algebraic_helper(3, "1", "1", "QBarExhaustiveProvider_int_range_Algebraic_Algebraic_vi");

        range_int_Algebraic_Algebraic_helper(
                1,
                "sqrt(2)",
                "sqrt(2)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_vii"
        );
        range_int_Algebraic_Algebraic_helper(
                1,
                "sqrt(2)",
                "sqrt(2)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_viii"
        );
        range_int_Algebraic_Algebraic_helper(
                1,
                "sqrt(2)",
                "sqrt(2)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_ix"
        );

        range_int_Algebraic_Algebraic_helper(1, "1", "2", "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_x");
        range_int_Algebraic_Algebraic_helper(2, "1", "2", "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xi");
        range_int_Algebraic_Algebraic_helper(3, "1", "2", "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xii");

        range_int_Algebraic_Algebraic_helper(
                1,
                "-4/3",
                "1/2",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xiii"
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                "-4/3",
                "1/2",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xiv"
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                "-4/3",
                "1/2",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xv"
        );

        range_int_Algebraic_Algebraic_helper(
                1,
                "1",
                "sqrt(2)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xvi"
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                "1",
                "sqrt(2)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xvii"
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                "1",
                "sqrt(2)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xviii"
        );

        range_int_Algebraic_Algebraic_helper(
                1,
                "sqrt(2)",
                "sqrt(3)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xix"
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                "sqrt(2)",
                "sqrt(3)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xx"
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                "sqrt(2)",
                "sqrt(3)",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxi"
        );

        range_int_Algebraic_Algebraic_helper(
                1,
                "0",
                "256",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxii"
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                "0",
                "256",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxiii"
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                "0",
                "256",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxiv"
        );

        range_int_Algebraic_Algebraic_helper(
                1,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxv"
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxvi"
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarExhaustiveProvider_range_int_Algebraic_Algebraic_xxvii"
        );

        range_int_Algebraic_Algebraic_fail_helper(0, "0", "1");
        range_int_Algebraic_Algebraic_fail_helper(-1, "0", "1");
        range_int_Algebraic_Algebraic_fail_helper(1, "1", "0");
        range_int_Algebraic_Algebraic_fail_helper(1, "6369051672525773/4503599627370496", "sqrt(2)");
    }

    private static void range_Algebraic_Algebraic_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        simpleProviderHelper(QEP.range(Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get()), output);
    }

    private static void range_Algebraic_Algebraic_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            QEP.range(Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRange_Algebraic_Algebraic() {
        range_Algebraic_Algebraic_helper("0", "0", "QBarExhaustiveProvider_range_Algebraic_Algebraic_i");
        range_Algebraic_Algebraic_helper("1", "1", "QBarExhaustiveProvider_range_Algebraic_Algebraic_ii");
        range_Algebraic_Algebraic_helper("sqrt(2)", "sqrt(2)", "QBarExhaustiveProvider_range_Algebraic_Algebraic_iii");
        range_Algebraic_Algebraic_helper("1", "2", "QBarExhaustiveProvider_range_Algebraic_Algebraic_iv");
        range_Algebraic_Algebraic_helper("-4/3", "1/2", "QBarExhaustiveProvider_range_Algebraic_Algebraic_v");
        range_Algebraic_Algebraic_helper("1", "sqrt(2)", "QBarExhaustiveProvider_range_Algebraic_Algebraic_vi");
        range_Algebraic_Algebraic_helper("sqrt(2)", "sqrt(3)", "QBarExhaustiveProvider_range_Algebraic_Algebraic_vii");
        range_Algebraic_Algebraic_helper("0", "256", "QBarExhaustiveProvider_range_Algebraic_Algebraic_viii");
        range_Algebraic_Algebraic_helper(
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarExhaustiveProvider_range_Algebraic_Algebraic_ix"
        );

        range_Algebraic_Algebraic_fail_helper("1", "0");
        range_Algebraic_Algebraic_fail_helper("6369051672525773/4503599627370496", "sqrt(2)");
    }

    private static void algebraicsIn_int_Interval_helper(int degree, @NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.algebraicsIn(degree, Interval.readStrict(a).get()), output);
    }

    private static void algebraics_int_Interval_fail_helper(int degree, @NotNull String a) {
        try {
            QEP.algebraicsIn(degree, Interval.readStrict(a).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testAlgebraicsIn_int_Interval() {
        algebraicsIn_int_Interval_helper(1, "[0, 0]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_i");
        algebraicsIn_int_Interval_helper(2, "[0, 0]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_ii");
        algebraicsIn_int_Interval_helper(3, "[0, 0]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_iii");

        algebraicsIn_int_Interval_helper(1, "[1, 1]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_iv");
        algebraicsIn_int_Interval_helper(2, "[1, 1]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_v");
        algebraicsIn_int_Interval_helper(3, "[1, 1]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_vi");

        algebraicsIn_int_Interval_helper(
                1,
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_vii"
        );
        algebraicsIn_int_Interval_helper(
                2,
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_viii"
        );
        algebraicsIn_int_Interval_helper(
                3,
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_ix"
        );

        algebraicsIn_int_Interval_helper(1, "[1, 4]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_x");
        algebraicsIn_int_Interval_helper(2, "[1, 4]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_xi");
        algebraicsIn_int_Interval_helper(3, "[1, 4]", "QBarExhaustiveProvider_algebraicsIn_int_Interval_xii");

        algebraicsIn_int_Interval_helper(
                1,
                "(-Infinity, 1/2]",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_xiii"
        );
        algebraicsIn_int_Interval_helper(
                2,
                "(-Infinity, 1/2]",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_xiv"
        );
        algebraicsIn_int_Interval_helper(
                3,
                "(-Infinity, 1/2]",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_xv"
        );

        algebraicsIn_int_Interval_helper(
                1,
                "[1/2, Infinity)",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_xvi"
        );
        algebraicsIn_int_Interval_helper(
                2,
                "[1/2, Infinity)",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_xvii"
        );
        algebraicsIn_int_Interval_helper(
                3,
                "[1/2, Infinity)",
                "QBarExhaustiveProvider_algebraicsIn_int_Interval_xviii"
        );

        algebraics_int_Interval_fail_helper(0, "[0, 1]");
        algebraics_int_Interval_fail_helper(-1, "[0, 1]");
    }

    private static void algebraicsIn_Interval_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.algebraicsIn(Interval.readStrict(a).get()), output);
    }

    @Test
    public void testAlgebraicsIn_Interval() {
        algebraicsIn_Interval_helper("[0, 0]", "QBarExhaustiveProvider_algebraicsIn_Interval_i");
        algebraicsIn_Interval_helper("[1, 1]", "QBarExhaustiveProvider_algebraicsIn_Interval_ii");
        algebraicsIn_Interval_helper("(-Infinity, Infinity)", "QBarExhaustiveProvider_algebraicsIn_Interval_iii");
        algebraicsIn_Interval_helper("[1, 4]", "QBarExhaustiveProvider_algebraicsIn_Interval_iv");
        algebraicsIn_Interval_helper("(-Infinity, 1/2]", "QBarExhaustiveProvider_algebraicsIn_Interval_v");
        algebraicsIn_Interval_helper("[1/2, Infinity)", "QBarExhaustiveProvider_algebraicsIn_Interval_vi");
    }

    private static void algebraicsNotIn_int_Interval_helper(int degree, @NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.algebraicsNotIn(degree, Interval.readStrict(a).get()), output);
    }

    private static void algebraicsNotIn_int_Interval_fail_helper(int degree, @NotNull String a) {
        try {
            QEP.algebraicsNotIn(degree, Interval.readStrict(a).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testAlgebraicsNotIn_int_Interval() {
        algebraicsNotIn_int_Interval_helper(1, "[0, 0]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_i");
        algebraicsNotIn_int_Interval_helper(2, "[0, 0]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_ii");
        algebraicsNotIn_int_Interval_helper(3, "[0, 0]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_iii");

        algebraicsNotIn_int_Interval_helper(1, "[1, 1]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_iv");
        algebraicsNotIn_int_Interval_helper(2, "[1, 1]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_v");
        algebraicsNotIn_int_Interval_helper(3, "[1, 1]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_vi");

        algebraicsNotIn_int_Interval_helper(
                1,
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_vii"
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_viii"
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_ix"
        );

        algebraicsNotIn_int_Interval_helper(1, "[1, 4]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_x");
        algebraicsNotIn_int_Interval_helper(2, "[1, 4]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xi");
        algebraicsNotIn_int_Interval_helper(3, "[1, 4]", "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xii");

        algebraicsNotIn_int_Interval_helper(
                1,
                "(-Infinity, 1/2]",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xiii"
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                "(-Infinity, 1/2]",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xiv"
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                "(-Infinity, 1/2]",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xv"
        );

        algebraicsNotIn_int_Interval_helper(
                1,
                "[1/2, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xvi"
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                "[1/2, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xvii"
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                "[1/2, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_int_Interval_xviii"
        );

        algebraicsNotIn_int_Interval_fail_helper(0, "[0, 1]");
        algebraicsNotIn_int_Interval_fail_helper(-1, "[0, 1]");
    }

    private static void algebraicsNotIn_Interval_helper(@NotNull String a, @NotNull String output) {
        simpleProviderHelper(QEP.algebraicsNotIn(Interval.readStrict(a).get()), output);
    }

    @Test
    public void testAlgebraicsNotIn_Interval() {
        algebraicsNotIn_Interval_helper("[0, 0]", "QBarExhaustiveProvider_algebraicsNotIn_Interval_i");
        algebraicsNotIn_Interval_helper("[1, 1]", "QBarExhaustiveProvider_algebraicsNotIn_Interval_ii");
        algebraicsNotIn_Interval_helper(
                "(-Infinity, Infinity)",
                "QBarExhaustiveProvider_algebraicsNotIn_Interval_iii"
        );
        algebraicsNotIn_Interval_helper("[1, 4]", "QBarExhaustiveProvider_algebraicsNotIn_Interval_iv");
        algebraicsNotIn_Interval_helper("(-Infinity, 1/2]", "QBarExhaustiveProvider_algebraicsNotIn_Interval_v");
        algebraicsNotIn_Interval_helper("[1/2, Infinity)", "QBarExhaustiveProvider_algebraicsNotIn_Interval_vi");
    }

    @Test
    public void testEquals() {
        //noinspection EqualsWithItself
        assertTrue(QEP.equals(QEP));
        //noinspection ObjectEqualsNull
        assertFalse(QEP.equals(null));
        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(QEP.equals("hello"));
    }

    @Test
    public void testHashCode() {
        aeq(QEP.hashCode(), 0);
    }

    @Test
    public void testToString() {
        aeq(QEP, "QBarExhaustiveProvider");
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Variable::readStrict).apply(s).get();
    }
}
