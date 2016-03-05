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
        rationalPolynomials_int_helper(-1, "[0]");
        rationalPolynomials_int_helper(0,
                "[1, 1/2, 1/3, 1/4, -1, -1/2, 2, -1/3, -1/4, 2/3, 1/5, 1/6, 1/7, 1/8, -1/5, -1/6, 2/5, -1/7, -1/8," +
                " 2/7, -2, 3, 3/2, -2/3, 3/4, -3, -3/2, 4, -3/4, 4/3, -2/5, 3/5, -2/7, 3/7, 3/8, -3/5, 4/5, -3/7," +
                " -3/8, 4/7, 1/9, 1/10, 1/11, 1/12, -1/9, -1/10, 2/9, -1/11, -1/12, 2/11, ...]");
        rationalPolynomials_int_helper(1,
                "[x, x+1, 1/2*x, 1/3*x, 1/2*x+1, 1/3*x+1, x+1/2, x+1/3, 1/2*x+1/2, 1/3*x+1/2, 1/2*x+1/3, 1/3*x+1/3," +
                " 1/4*x, -x, 1/4*x+1, -x+1, -1/2*x, 2*x, -1/2*x+1, 2*x+1, 1/4*x+1/2, -x+1/2, 1/4*x+1/3, -x+1/3," +
                " -1/2*x+1/2, 2*x+1/2, -1/2*x+1/3, 2*x+1/3, x+1/4, x-1, 1/2*x+1/4, 1/3*x+1/4, 1/2*x-1, 1/3*x-1," +
                " x-1/2, x+2, 1/2*x-1/2, 1/3*x-1/2, 1/2*x+2, 1/3*x+2, 1/4*x+1/4, -x+1/4, 1/4*x-1, -x-1, -1/2*x+1/4," +
                " 2*x+1/4, -1/2*x-1, 2*x-1, 1/4*x-1/2, -x-1/2, ...]");
        rationalPolynomials_int_helper(2,
                "[x^2, x^2+x, x^2+1, x^2+x+1, 1/2*x^2, 1/3*x^2, 1/2*x^2+x, 1/3*x^2+x, 1/2*x^2+1, 1/3*x^2+1," +
                " 1/2*x^2+x+1, 1/3*x^2+x+1, x^2+1/2*x, x^2+1/3*x, x^2+1/2*x+1, x^2+1/3*x+1, 1/2*x^2+1/2*x," +
                " 1/3*x^2+1/2*x, 1/2*x^2+1/3*x, 1/3*x^2+1/3*x, 1/2*x^2+1/2*x+1, 1/3*x^2+1/2*x+1, 1/2*x^2+1/3*x+1," +
                " 1/3*x^2+1/3*x+1, x^2+1/2, x^2+x+1/2, x^2+1/3, x^2+x+1/3, 1/2*x^2+1/2, 1/3*x^2+1/2, 1/2*x^2+x+1/2," +
                " 1/3*x^2+x+1/2, 1/2*x^2+1/3, 1/3*x^2+1/3, 1/2*x^2+x+1/3, 1/3*x^2+x+1/3, x^2+1/2*x+1/2," +
                " x^2+1/3*x+1/2, x^2+1/2*x+1/3, x^2+1/3*x+1/3, 1/2*x^2+1/2*x+1/2, 1/3*x^2+1/2*x+1/2," +
                " 1/2*x^2+1/3*x+1/2, 1/3*x^2+1/3*x+1/2, 1/2*x^2+1/2*x+1/3, 1/3*x^2+1/2*x+1/3, 1/2*x^2+1/3*x+1/3," +
                " 1/3*x^2+1/3*x+1/3, 1/4*x^2, -x^2, ...]");
        rationalPolynomials_int_helper(9,
                "[x^9, x^9+x^8, x^9+x^7, x^9+x^8+x^7, x^9+x^6, x^9+x^8+x^6, x^9+x^7+x^6, x^9+x^8+x^7+x^6, x^9+x^5," +
                " x^9+x^8+x^5, x^9+x^7+x^5, x^9+x^8+x^7+x^5, x^9+x^6+x^5, x^9+x^8+x^6+x^5, x^9+x^7+x^6+x^5," +
                " x^9+x^8+x^7+x^6+x^5, x^9+x^4, x^9+x^8+x^4, x^9+x^7+x^4, x^9+x^8+x^7+x^4, x^9+x^6+x^4," +
                " x^9+x^8+x^6+x^4, x^9+x^7+x^6+x^4, x^9+x^8+x^7+x^6+x^4, x^9+x^5+x^4, x^9+x^8+x^5+x^4," +
                " x^9+x^7+x^5+x^4, x^9+x^8+x^7+x^5+x^4, x^9+x^6+x^5+x^4, x^9+x^8+x^6+x^5+x^4, x^9+x^7+x^6+x^5+x^4," +
                " x^9+x^8+x^7+x^6+x^5+x^4, x^9+x^3, x^9+x^8+x^3, x^9+x^7+x^3, x^9+x^8+x^7+x^3, x^9+x^6+x^3," +
                " x^9+x^8+x^6+x^3, x^9+x^7+x^6+x^3, x^9+x^8+x^7+x^6+x^3, x^9+x^5+x^3, x^9+x^8+x^5+x^3," +
                " x^9+x^7+x^5+x^3, x^9+x^8+x^7+x^5+x^3, x^9+x^6+x^5+x^3, x^9+x^8+x^6+x^5+x^3, x^9+x^7+x^6+x^5+x^3," +
                " x^9+x^8+x^7+x^6+x^5+x^3, x^9+x^4+x^3, x^9+x^8+x^4+x^3, ...]");
        rationalPolynomials_int_fail_helper(-2);
    }

    @Test
    public void testRationalPolynomials() {
        simpleProviderHelper(QEP.rationalPolynomials(),
                "[0, 1, 1/2, x, 1/3, 1/4, -1, x^2, -1/2, x+1, 2, -1/3, 1/2*x, -1/4, 2/3, 1/3*x, 1/5, x^3, 1/6," +
                " 1/2*x+1, 1/7, x^2+x, 1/8, 1/3*x+1, -1/5, -1/6, 2/5, -1/7, x+1/2, -1/8, 2/7, -2, x^2+1, 3, x+1/3," +
                " 3/2, x^4, -2/3, 1/2*x+1/2, 3/4, -3, 1/3*x+1/2, -3/2, x^3+x^2, 4, 1/2*x+1/3, -3/4, x^2+x+1, 4/3," +
                " 1/3*x+1/3, ...]");
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
        rationalPolynomialsAtLeast_helper(-1,
                "[0, 1, 1/2, x, 1/3, 1/4, -1, x^2, -1/2, x+1, 2, -1/3, 1/2*x, -1/4, 2/3, 1/3*x, 1/5, x^3, 1/6," +
                " 1/2*x+1, 1/7, x^2+x, 1/8, 1/3*x+1, -1/5, -1/6, 2/5, -1/7, x+1/2, -1/8, 2/7, -2, x^2+1, 3, x+1/3," +
                " 3/2, x^4, -2/3, 1/2*x+1/2, 3/4, -3, 1/3*x+1/2, -3/2, x^3+x^2, 4, 1/2*x+1/3, -3/4, x^2+x+1, 4/3," +
                " 1/3*x+1/3, ...]");
        rationalPolynomialsAtLeast_helper(0,
                "[1, 1/2, x, 1/3, 1/4, -1, x^2, -1/2, x+1, 2, -1/3, 1/2*x, -1/4, 2/3, 1/3*x, 1/5, x^3, 1/6, 1/2*x+1," +
                " 1/7, x^2+x, 1/8, 1/3*x+1, -1/5, -1/6, 2/5, -1/7, x+1/2, -1/8, 2/7, -2, x^2+1, 3, x+1/3, 3/2, x^4," +
                " -2/3, 1/2*x+1/2, 3/4, -3, 1/3*x+1/2, -3/2, x^3+x^2, 4, 1/2*x+1/3, -3/4, x^2+x+1, 4/3, 1/3*x+1/3," +
                " -2/5, ...]");
        rationalPolynomialsAtLeast_helper(1,
                "[x, x^2, x+1, 1/2*x, 1/3*x, x^3, 1/2*x+1, x^2+x, 1/3*x+1, x+1/2, x^2+1, x+1/3, x^4, 1/2*x+1/2," +
                " 1/3*x+1/2, x^3+x^2, 1/2*x+1/3, x^2+x+1, 1/3*x+1/3, 1/4*x, 1/2*x^2, -x, 1/4*x+1, 1/3*x^2, -x+1," +
                " -1/2*x, 1/2*x^2+x, 2*x, x^3+x, -1/2*x+1, 1/3*x^2+x, 2*x+1, x^5, 1/4*x+1/2, 1/2*x^2+1, -x+1/2," +
                " 1/4*x+1/3, 1/3*x^2+1, -x+1/3, x^4+x^3, -1/2*x+1/2, 1/2*x^2+x+1, 2*x+1/2, x^3+x^2+x, -1/2*x+1/3," +
                " 1/3*x^2+x+1, 2*x+1/3, x+1/4, x^2+1/2*x, x-1, ...]");
        rationalPolynomialsAtLeast_helper(2,
                "[x^2, x^3, x^2+x, x^2+1, x^4, x^3+x^2, x^2+x+1, 1/2*x^2, 1/3*x^2, 1/2*x^2+x, x^3+x, 1/3*x^2+x, x^5," +
                " 1/2*x^2+1, 1/3*x^2+1, x^4+x^3, 1/2*x^2+x+1, x^3+x^2+x, 1/3*x^2+x+1, x^2+1/2*x, x^3+1, x^2+1/3*x," +
                " x^2+1/2*x+1, x^4+x^2, x^3+x^2+1, x^2+1/3*x+1, x^6, 1/2*x^2+1/2*x, 1/3*x^2+1/2*x, 1/2*x^2+1/3*x," +
                " x^3+x+1, 1/3*x^2+1/3*x, x^5+x^4, 1/2*x^2+1/2*x+1, 1/3*x^2+1/2*x+1, x^4+x^3+x^2, 1/2*x^2+1/3*x+1," +
                " x^3+x^2+x+1, 1/3*x^2+1/3*x+1, 1/2*x^3, x^2+1/2, 1/3*x^3, x^2+x+1/2, 1/2*x^3+x^2, x^2+1/3, x^4+x," +
                " 1/3*x^3+x^2, x^2+x+1/3, 1/2*x^2+1/2, 1/2*x^3+x, ...]");
        rationalPolynomialsAtLeast_helper(9,
                "[x^9, x^10, x^9+x^8, x^9+x^7, x^11, x^10+x^9, x^9+x^8+x^7, x^9+x^6, x^10+x^8, x^9+x^8+x^6, x^12," +
                " x^9+x^7+x^6, x^11+x^10, x^10+x^9+x^8, x^9+x^8+x^7+x^6, x^9+x^5, x^10+x^7, x^9+x^8+x^5," +
                " x^9+x^7+x^5, x^11+x^9, x^10+x^9+x^7, x^9+x^8+x^7+x^5, x^13, x^9+x^6+x^5, x^10+x^8+x^7," +
                " x^9+x^8+x^6+x^5, x^12+x^11, x^9+x^7+x^6+x^5, x^11+x^10+x^9, x^10+x^9+x^8+x^7, x^9+x^8+x^7+x^6+x^5," +
                " x^9+x^4, x^10+x^6, x^9+x^8+x^4, x^9+x^7+x^4, x^11+x^8, x^10+x^9+x^6, x^9+x^8+x^7+x^4, x^9+x^6+x^4," +
                " x^10+x^8+x^6, x^9+x^8+x^6+x^4, x^12+x^10, x^9+x^7+x^6+x^4, x^11+x^10+x^8, x^10+x^9+x^8+x^6," +
                " x^9+x^8+x^7+x^6+x^4, x^14, x^9+x^5+x^4, x^10+x^7+x^6, x^9+x^8+x^5+x^4, ...]");
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
        monicRationalPolynomials_int_helper(-1, "[]");
        monicRationalPolynomials_int_helper(0, "[1]");
        monicRationalPolynomials_int_helper(1,
                "[x, x+1, x+1/2, x-1, x+2, x-1/2, x+1/3, x+1/4, x-1/3, x+2/3, x-1/4, x-2, x+3, x+3/2, x-3, x+4," +
                " x-3/2, x-2/3, x+3/4, x+4/3, x-3/4, x+1/5, x+1/6, x-1/5, x+2/5, x-1/6, x+1/7, x+1/8, x-1/7, x+2/7," +
                " x-1/8, x-2/5, x+3/5, x-3/5, x+4/5, x-2/7, x+3/7, x+3/8, x-3/7, x+4/7, x-3/8, x-4, x+5, x+5/2, x-5," +
                " x+6, x-5/2, x-4/3, x+5/3, x+5/4, ...]");
        monicRationalPolynomials_int_helper(2,
                "[x^2, x^2+x, x^2+1, x^2+x+1, x^2+1/2*x, x^2+1/2, x^2+1/2*x+1/2, x^2-x, x^2+2*x, x^2-x+1, x^2+2*x+1," +
                " x^2-1/2*x, x^2-1/2*x+1/2, x^2+x+1/2, x^2-1, x^2+x-1, x^2+2, x^2+x+2, x^2-1/2, x^2+1/2*x-1/2," +
                " x^2+1/2*x+1, x^2-x-1, x^2+2*x-1, x^2-x+2, x^2+2*x+2, x^2-1/2*x-1/2, x^2+x-1/2, x^2-1/2*x+1," +
                " x^2+1/3*x, x^2+1/3, x^2+1/3*x+1/3, x^2+1/4*x, x^2+1/4, x^2+1/4*x+1/4, x^2-1/3*x, x^2+2/3*x," +
                " x^2-1/3*x+1/3, x^2+2/3*x+1/3, x^2-1/4*x, x^2-1/4*x+1/4, x^2+1/2*x+1/4, x^2-1/3, x^2+1/3*x-1/3," +
                " x^2+2/3, x^2+1/3*x+2/3, x^2-1/4, x^2+1/4*x-1/4, x^2+1/4*x+1/2, x^2-1/3*x-1/3, x^2+2/3*x-1/3, ...]");
        monicRationalPolynomials_int_helper(8,
                "[x^8, x^8+x^7, x^8+x^6, x^8+x^7+x^6, x^8+x^5, x^8+x^7+x^5, x^8+x^6+x^5, x^8+x^7+x^6+x^5, x^8+x^4," +
                " x^8+x^7+x^4, x^8+x^6+x^4, x^8+x^7+x^6+x^4, x^8+x^5+x^4, x^8+x^7+x^5+x^4, x^8+x^6+x^5+x^4," +
                " x^8+x^7+x^6+x^5+x^4, x^8+x^3, x^8+x^7+x^3, x^8+x^6+x^3, x^8+x^7+x^6+x^3, x^8+x^5+x^3," +
                " x^8+x^7+x^5+x^3, x^8+x^6+x^5+x^3, x^8+x^7+x^6+x^5+x^3, x^8+x^4+x^3, x^8+x^7+x^4+x^3," +
                " x^8+x^6+x^4+x^3, x^8+x^7+x^6+x^4+x^3, x^8+x^5+x^4+x^3, x^8+x^7+x^5+x^4+x^3, x^8+x^6+x^5+x^4+x^3," +
                " x^8+x^7+x^6+x^5+x^4+x^3, x^8+x^2, x^8+x^7+x^2, x^8+x^6+x^2, x^8+x^7+x^6+x^2, x^8+x^5+x^2," +
                " x^8+x^7+x^5+x^2, x^8+x^6+x^5+x^2, x^8+x^7+x^6+x^5+x^2, x^8+x^4+x^2, x^8+x^7+x^4+x^2," +
                " x^8+x^6+x^4+x^2, x^8+x^7+x^6+x^4+x^2, x^8+x^5+x^4+x^2, x^8+x^7+x^5+x^4+x^2, x^8+x^6+x^5+x^4+x^2," +
                " x^8+x^7+x^6+x^5+x^4+x^2, x^8+x^3+x^2, x^8+x^7+x^3+x^2, ...]");
        monicRationalPolynomials_int_fail_helper(-2);
    }

    @Test
    public void testMonicRationalPolynomials() {
        simpleProviderHelper(QEP.monicRationalPolynomials(),
                "[1, x, x^2, x+1, x^3, x^2+x, x+1/2, x-1, x^2+1, x+2, x^4, x-1/2, x^3+x^2, x^2+x+1, x+1/3, x^3+x," +
                " x^2+1/2*x, x+1/4, x^5, x-1/3, x^2+1/2, x+2/3, x^4+x^3, x-1/4, x^3+x^2+x, x^2+1/2*x+1/2, x-2," +
                " x^2-x, x+3, x^3+1, x^2+2*x, x+3/2, x-3, x^2-x+1, x+4, x^4+x^2, x-3/2, x^3+x^2+1, x^2+2*x+1, x^6," +
                " x-2/3, x^2-1/2*x, x^3+x+1, x+3/4, x^5+x^4, x^2-1/2*x+1/2, x+4/3, x^4+x^3+x^2, x-3/4, x^3+x^2+x+1," +
                " ...]");
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
        monicRationalPolynomialsAtLeast_helper(-1,
                "[1, x, x^2, x+1, x^3, x^2+x, x+1/2, x-1, x^2+1, x+2, x^4, x-1/2, x^3+x^2, x^2+x+1, x+1/3, x^3+x," +
                " x^2+1/2*x, x+1/4, x^5, x-1/3, x^2+1/2, x+2/3, x^4+x^3, x-1/4, x^3+x^2+x, x^2+1/2*x+1/2, x-2," +
                " x^2-x, x+3, x^3+1, x^2+2*x, x+3/2, x-3, x^2-x+1, x+4, x^4+x^2, x-3/2, x^3+x^2+1, x^2+2*x+1, x^6," +
                " x-2/3, x^2-1/2*x, x^3+x+1, x+3/4, x^5+x^4, x^2-1/2*x+1/2, x+4/3, x^4+x^3+x^2, x-3/4, x^3+x^2+x+1," +
                " ...]");
        monicRationalPolynomialsAtLeast_helper(0,
                "[1, x, x^2, x+1, x^3, x^2+x, x+1/2, x-1, x^2+1, x+2, x^4, x-1/2, x^3+x^2, x^2+x+1, x+1/3, x^3+x," +
                " x^2+1/2*x, x+1/4, x^5, x-1/3, x^2+1/2, x+2/3, x^4+x^3, x-1/4, x^3+x^2+x, x^2+1/2*x+1/2, x-2," +
                " x^2-x, x+3, x^3+1, x^2+2*x, x+3/2, x-3, x^2-x+1, x+4, x^4+x^2, x-3/2, x^3+x^2+1, x^2+2*x+1, x^6," +
                " x-2/3, x^2-1/2*x, x^3+x+1, x+3/4, x^5+x^4, x^2-1/2*x+1/2, x+4/3, x^4+x^3+x^2, x-3/4, x^3+x^2+x+1," +
                " ...]");
        monicRationalPolynomialsAtLeast_helper(1,
                "[x, x^2, x+1, x^3, x^2+x, x+1/2, x-1, x^2+1, x+2, x^4, x-1/2, x^3+x^2, x^2+x+1, x+1/3, x^3+x," +
                " x^2+1/2*x, x+1/4, x^5, x-1/3, x^2+1/2, x+2/3, x^4+x^3, x-1/4, x^3+x^2+x, x^2+1/2*x+1/2, x-2," +
                " x^2-x, x+3, x^3+1, x^2+2*x, x+3/2, x-3, x^2-x+1, x+4, x^4+x^2, x-3/2, x^3+x^2+1, x^2+2*x+1, x^6," +
                " x-2/3, x^2-1/2*x, x^3+x+1, x+3/4, x^5+x^4, x^2-1/2*x+1/2, x+4/3, x^4+x^3+x^2, x-3/4, x^3+x^2+x+1," +
                " x^2+x+1/2, ...]");
        monicRationalPolynomialsAtLeast_helper(2,
                "[x^2, x^3, x^2+x, x^2+1, x^4, x^3+x^2, x^2+x+1, x^3+x, x^2+1/2*x, x^5, x^2+1/2, x^4+x^3, x^3+x^2+x," +
                " x^2+1/2*x+1/2, x^2-x, x^3+1, x^2+2*x, x^2-x+1, x^4+x^2, x^3+x^2+1, x^2+2*x+1, x^6, x^2-1/2*x," +
                " x^3+x+1, x^5+x^4, x^2-1/2*x+1/2, x^4+x^3+x^2, x^3+x^2+x+1, x^2+x+1/2, x^2-1, x^2+x-1, x^2+2," +
                " x^4+x, x^3+1/2*x^2, x^2+x+2, x^2-1/2, x^3+1/2*x, x^2+1/2*x-1/2, x^5+x^3, x^4+x^3+x," +
                " x^3+1/2*x^2+1/2*x, x^2+1/2*x+1, x^7, x^2-x-1, x^3+1/2, x^2+2*x-1, x^2-x+2, x^4+x^2+x," +
                " x^3+1/2*x^2+1/2, x^2+2*x+2, ...]");
        monicRationalPolynomialsAtLeast_helper(8,
                "[x^8, x^9, x^8+x^7, x^8+x^6, x^10, x^9+x^8, x^8+x^7+x^6, x^8+x^5, x^9+x^7, x^8+x^7+x^5, x^11," +
                " x^8+x^6+x^5, x^10+x^9, x^9+x^8+x^7, x^8+x^7+x^6+x^5, x^8+x^4, x^9+x^6, x^8+x^7+x^4, x^8+x^6+x^4," +
                " x^10+x^8, x^9+x^8+x^6, x^8+x^7+x^6+x^4, x^12, x^8+x^5+x^4, x^9+x^7+x^6, x^8+x^7+x^5+x^4," +
                " x^11+x^10, x^8+x^6+x^5+x^4, x^10+x^9+x^8, x^9+x^8+x^7+x^6, x^8+x^7+x^6+x^5+x^4, x^8+x^3, x^9+x^5," +
                " x^8+x^7+x^3, x^8+x^6+x^3, x^10+x^7, x^9+x^8+x^5, x^8+x^7+x^6+x^3, x^8+x^5+x^3, x^9+x^7+x^5," +
                " x^8+x^7+x^5+x^3, x^11+x^9, x^8+x^6+x^5+x^3, x^10+x^9+x^7, x^9+x^8+x^7+x^5, x^8+x^7+x^6+x^5+x^3," +
                " x^13, x^8+x^4+x^3, x^9+x^6+x^5, x^8+x^7+x^4+x^3, ...]");
        monicRationalPolynomialsAtLeast_fail_helper(-2);
    }

    @Test
    public void testVariables() {
        simpleProviderHelper(QEP.variables(),
                "[a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, aa, bb, cc, dd, ee," +
                " ff, gg, hh, ii, jj, kk, ll, mm, nn, oo, pp, qq, rr, ss, tt, uu, vv, ww, xx, ...]");
    }

    @Test
    public void testMonomialOrders() {
        simpleProviderHelper(QEP.monomialOrders(), "[LEX, GRLEX, GREVLEX]");
    }

    @Test
    public void testExponentVectors() {
        simpleProviderHelper(QEP.exponentVectors(),
                "[1, a, b, a^2, a*b, c, b^2, a^3, a^2*b, b*c, d, a*c, a*b^2, c^2, b^3, a^4, a^3*b, b^2*c, c*d," +
                " a*b*c, a*d, e, b*d, a^2*c, a^2*b^2, b*c^2, d^2, a*c^2, a*b^3, c^3, b^4, a^5, a^4*b, b^3*c, c^2*d," +
                " a*b^2*c, a*c*d, d*e, b*c*d, a^2*b*c, a^2*d, b*e, f, a*e, a*b*d, c*e, b^2*d, a^3*c, a^3*b^2," +
                " b^2*c^2, ...]");
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
        exponentVectors_List_Variable_helper("[]", "[1]");
        exponentVectors_List_Variable_helper("[a]",
                "[1, a, a^2, a^3, a^4, a^5, a^6, a^7, a^8, a^9, a^10, a^11, a^12, a^13, a^14, a^15, a^16, a^17," +
                " a^18, a^19, a^20, a^21, a^22, a^23, a^24, a^25, a^26, a^27, a^28, a^29, a^30, a^31, a^32, a^33," +
                " a^34, a^35, a^36, a^37, a^38, a^39, a^40, a^41, a^42, a^43, a^44, a^45, a^46, a^47, a^48, a^49," +
                " ...]");
        exponentVectors_List_Variable_helper("[x]",
                "[1, x, x^2, x^3, x^4, x^5, x^6, x^7, x^8, x^9, x^10, x^11, x^12, x^13, x^14, x^15, x^16, x^17," +
                " x^18, x^19, x^20, x^21, x^22, x^23, x^24, x^25, x^26, x^27, x^28, x^29, x^30, x^31, x^32, x^33," +
                " x^34, x^35, x^36, x^37, x^38, x^39, x^40, x^41, x^42, x^43, x^44, x^45, x^46, x^47, x^48, x^49," +
                " ...]");
        exponentVectors_List_Variable_helper("[ooo]",
                "[1, ooo, ooo^2, ooo^3, ooo^4, ooo^5, ooo^6, ooo^7, ooo^8, ooo^9, ooo^10, ooo^11, ooo^12, ooo^13," +
                " ooo^14, ooo^15, ooo^16, ooo^17, ooo^18, ooo^19, ooo^20, ooo^21, ooo^22, ooo^23, ooo^24, ooo^25," +
                " ooo^26, ooo^27, ooo^28, ooo^29, ooo^30, ooo^31, ooo^32, ooo^33, ooo^34, ooo^35, ooo^36, ooo^37," +
                " ooo^38, ooo^39, ooo^40, ooo^41, ooo^42, ooo^43, ooo^44, ooo^45, ooo^46, ooo^47, ooo^48, ooo^49," +
                " ...]");
        exponentVectors_List_Variable_helper("[x, y]",
                "[1, x, y, x*y, x^2, x^3, x^2*y, x^3*y, y^2, x*y^2, y^3, x*y^3, x^2*y^2, x^3*y^2, x^2*y^3, x^3*y^3," +
                " x^4, x^5, x^4*y, x^5*y, x^6, x^7, x^6*y, x^7*y, x^4*y^2, x^5*y^2, x^4*y^3, x^5*y^3, x^6*y^2," +
                " x^7*y^2, x^6*y^3, x^7*y^3, y^4, x*y^4, y^5, x*y^5, x^2*y^4, x^3*y^4, x^2*y^5, x^3*y^5, y^6, x*y^6," +
                " y^7, x*y^7, x^2*y^6, x^3*y^6, x^2*y^7, x^3*y^7, x^4*y^4, x^5*y^4, ...]");
        exponentVectors_List_Variable_helper("[x, y, z]",
                "[1, x, y, x*y, z, x*z, y*z, x*y*z, x^2, x^3, x^2*y, x^3*y, x^2*z, x^3*z, x^2*y*z, x^3*y*z, y^2," +
                " x*y^2, y^3, x*y^3, y^2*z, x*y^2*z, y^3*z, x*y^3*z, x^2*y^2, x^3*y^2, x^2*y^3, x^3*y^3, x^2*y^2*z," +
                " x^3*y^2*z, x^2*y^3*z, x^3*y^3*z, z^2, x*z^2, y*z^2, x*y*z^2, z^3, x*z^3, y*z^3, x*y*z^3, x^2*z^2," +
                " x^3*z^2, x^2*y*z^2, x^3*y*z^2, x^2*z^3, x^3*z^3, x^2*y*z^3, x^3*y*z^3, y^2*z^2, x*y^2*z^2, ...]");

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
