package mho.qbar.iterableProviders;

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
        simpleProviderHelper(QEP.rangeUp(Rational.read(a).get()), output);
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
        simpleProviderHelper(QEP.rangeDown(Rational.read(a).get()), output);
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
        simpleProviderHelper(QEP.range(Rational.read(a).get(), Rational.read(b).get()), output);
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
        simpleProviderHelper(QEP.rationalsIn(Interval.read(a).get()), output);
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
        simpleProviderHelper(QEP.rationalsNotIn(Interval.read(a).get()), output);
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
        return Readers.readList(Variable::read).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Variable::read).apply(s).get();
    }
}
