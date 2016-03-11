package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.testing.QBarTesting.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.fail;

public class QBarRandomProviderTest {
    private static QBarRandomProvider P;

    @Before
    public void initialize() {
        P = QBarRandomProvider.example();
    }

    private static void rationalHelper(
            @NotNull Iterable<Rational> xs,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        List<Rational> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfRationals(sample), sampleMean);
        aeq(meanOfIntegers(toList(map(Rational::bitLength, sample))), bitSizeMean);
    }

    private static void positiveRationals_helper(
            int meanBitSize,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(meanBitSize).positiveRationals(), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void positiveRationals_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).positiveRationals();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositiveRationals() {
        positiveRationals_helper(4, "QBarRandomProvider_positiveRationals_i", 10.860889705920956, 3.7748699999675455);
        positiveRationals_helper(
                16,
                "QBarRandomProvider_positiveRationals_ii",
                1.5280779930028705E26,
                15.179776000001873
        );
        positiveRationals_helper(
                32,
                "QBarRandomProvider_positiveRationals_iii",
                9.896359254271288E54,
                30.97147700002087
        );
        positiveRationals_fail_helper(3);
        positiveRationals_fail_helper(0);
        positiveRationals_fail_helper(-1);
    }

    private static void negativeRationals_helper(
            int meanBitSize,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(meanBitSize).negativeRationals(), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void negativeRationals_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).negativeRationals();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNegativeRationals() {
        negativeRationals_helper(4, "QBarRandomProvider_negativeRationals_i", -10.860889705920956, 3.7748699999675455);
        negativeRationals_helper(
                16,
                "QBarRandomProvider_negativeRationals_ii",
                -1.5280779930028705E26,
                15.179776000001873
        );
        negativeRationals_helper(
                32,
                "QBarRandomProvider_negativeRationals_iii",
                -9.896359254271288E54,
                30.97147700002087
        );
        negativeRationals_fail_helper(3);
        negativeRationals_fail_helper(0);
        negativeRationals_fail_helper(-1);
    }

    private static void nonzeroRationals_helper(
            int meanBitSize,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(meanBitSize).nonzeroRationals(), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void nonzeroRationals_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).nonzeroRationals();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNonzeroRationals() {
        nonzeroRationals_helper(4, "QBarRandomProvider_nonzeroRationals_i", 0.39737016317796847, 3.775415999967471);
        nonzeroRationals_helper(
                16,
                "QBarRandomProvider_nonzeroRationals_ii",
                -7.897237376910241E21,
                15.175030000002636
        );
        nonzeroRationals_helper(
                32,
                "QBarRandomProvider_nonzeroRationals_iii",
                -3.8257920286392754E59,
                30.96837000002125
        );
        nonzeroRationals_fail_helper(3);
        nonzeroRationals_fail_helper(0);
        nonzeroRationals_fail_helper(-1);
    }

    private static void rationals_helper(
            int meanBitSize,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(meanBitSize).rationals(), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void rationals_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).rationals();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationals() {
        rationals_helper(3, "QBarRandomProvider_rationals_i", -0.4022959305532067, 2.779838999989692);
        rationals_helper(16, "QBarRandomProvider_rationals_ii", 1.6133070384934913E30, 15.814283999994494);
        rationals_helper(32, "QBarRandomProvider_rationals_iii", -3.784942556617747E71, 31.82849000002398);
        rationals_fail_helper(2);
        rationals_fail_helper(0);
        rationals_fail_helper(-1);
    }

    private static void nonNegativeRationalsLessThanOne_helper(
            int meanBitSize,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(meanBitSize).nonNegativeRationalsLessThanOne(), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void nonNegativeRationalsLessThanOne_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).nonNegativeRationalsLessThanOne();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNonNegativeRationalsLessThanOne() {
        nonNegativeRationalsLessThanOne_helper(
                4,
                "QBarRandomProvider_nonNegativeRationalsLessThanOne_i",
                0.25006056333167953,
                3.025887000003045
        );
        nonNegativeRationalsLessThanOne_helper(
                16,
                "QBarRandomProvider_nonNegativeRationalsLessThanOne_ii",
                0.43779120952560585,
                14.830439999963309
        );
        nonNegativeRationalsLessThanOne_helper(
                32,
                "QBarRandomProvider_nonNegativeRationalsLessThanOne_iii",
                0.4684325154820962,
                30.684408000047792
        );
        nonNegativeRationalsLessThanOne_fail_helper(3);
        nonNegativeRationalsLessThanOne_fail_helper(0);
        nonNegativeRationalsLessThanOne_fail_helper(-1);
    }

    private static void rangeUp_Rational_helper(
            int scale,
            @NotNull String a,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(scale).rangeUp(Rational.read(a).get()), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void rangeUp_Rational_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rangeUp(Rational.read(a).get());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRangeUp_Rational() {
        rangeUp_Rational_helper(4, "0", "QBarRandomProvider_rangeUp_Rational_i", 7.230916348852299, 3.083149999986345);
        rangeUp_Rational_helper(
                32,
                "0",
                "QBarRandomProvider_rangeUp_Rational_ii",
                2.30035078376369E59,
                30.041032000029976
        );
        rangeUp_Rational_helper(
                4,
                "1",
                "QBarRandomProvider_rangeUp_Rational_iii",
                8.230916348876454,
                4.258722999974617
        );
        rangeUp_Rational_helper(
                32,
                "1",
                "QBarRandomProvider_rangeUp_Rational_iv",
                2.30035078376369E59,
                37.66929400000752
        );
        rangeUp_Rational_helper(
                4,
                "2",
                "QBarRandomProvider_rangeUp_Rational_v",
                9.23091634887471,
                4.784373000006757
        );
        rangeUp_Rational_helper(
                32,
                "2",
                "QBarRandomProvider_rangeUp_Rational_vi",
                2.30035078376369E59,
                38.19239900001273
        );
        rangeUp_Rational_helper(
                4,
                "-2",
                "QBarRandomProvider_rangeUp_Rational_vii",
                5.230916348808398,
                4.015000999973354
        );
        rangeUp_Rational_helper(
                32,
                "-2",
                "QBarRandomProvider_rangeUp_Rational_viii",
                2.30035078376369E59,
                37.930548000008635
        );
        rangeUp_Rational_helper(
                4,
                "5/3",
                "QBarRandomProvider_rangeUp_Rational_ix",
                8.89758301551532,
                6.751185999974823
        );
        rangeUp_Rational_helper(
                32,
                "5/3",
                "QBarRandomProvider_rangeUp_Rational_x",
                2.30035078376369E59,
                40.232672000039464
        );
        rangeUp_Rational_helper(
                4,
                "-5/3",
                "QBarRandomProvider_rangeUp_Rational_xi",
                5.564249682144554,
                5.86250299994541
        );
        rangeUp_Rational_helper(
                32,
                "-5/3",
                "QBarRandomProvider_rangeUp_Rational_xii",
                2.30035078376369E59,
                39.967830000046504
        );
        rangeUp_Rational_fail_helper(3, "0");
        rangeUp_Rational_fail_helper(-1, "0");
    }

    private static void rangeDown_Rational_helper(
            int scale,
            @NotNull String a,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(scale).rangeDown(Rational.read(a).get()), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void rangeDown_Rational_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rangeDown(Rational.read(a).get());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRangeDown_Rational() {
        rangeDown_Rational_helper(
                4,
                "0",
                "QBarRandomProvider_rangeDown_Rational_i",
                -7.230916348852299,
                3.083149999986345
        );
        rangeDown_Rational_helper(
                32,
                "0",
                "QBarRandomProvider_rangeDown_Rational_ii",
                -2.30035078376369E59,
                30.041032000029976
        );
        rangeDown_Rational_helper(
                4,
                "1",
                "QBarRandomProvider_rangeDown_Rational_iii",
                -6.230916348820594,
                3.3941509999804
        );
        rangeDown_Rational_helper(
                32,
                "1",
                "QBarRandomProvider_rangeDown_Rational_iv",
                -2.30035078376369E59,
                37.41008700000551
        );
        rangeDown_Rational_helper(
                4,
                "2",
                "QBarRandomProvider_rangeDown_Rational_v",
                -5.230916348808398,
                4.015000999973354
        );
        rangeDown_Rational_helper(
                32,
                "2",
                "QBarRandomProvider_rangeDown_Rational_vi",
                -2.30035078376369E59,
                37.930548000008635
        );
        rangeDown_Rational_helper(
                4,
                "-2",
                "QBarRandomProvider_rangeDown_Rational_vii",
                -9.23091634887471,
                4.784373000006757
        );
        rangeDown_Rational_helper(
                32,
                "-2",
                "QBarRandomProvider_rangeDown_Rational_viii",
                -2.30035078376369E59,
                38.19239900001273
        );
        rangeDown_Rational_helper(
                4,
                "5/3",
                "QBarRandomProvider_rangeDown_Rational_ix",
                -5.564249682144554,
                5.86250299994541
        );
        rangeDown_Rational_helper(
                32,
                "5/3",
                "QBarRandomProvider_rangeDown_Rational_x",
                -2.30035078376369E59,
                39.967830000046504
        );
        rangeDown_Rational_helper(
                4,
                "-5/3",
                "QBarRandomProvider_rangeDown_Rational_xi",
                -8.89758301551532,
                6.751185999974823
        );
        rangeDown_Rational_helper(
                32,
                "-5/3",
                "QBarRandomProvider_rangeDown_Rational_xii",
                -2.30035078376369E59,
                40.232672000039464
        );
        rangeDown_Rational_fail_helper(3, "0");
        rangeDown_Rational_fail_helper(-1, "0");
    }

    private static void range_Rational_Rational_helper(
            int scale,
            @NotNull String a,
            @NotNull String b,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(
                P.withScale(scale).range(Rational.read(a).get(), Rational.read(b).get()),
                output,
                sampleMean,
                bitSizeMean
        );
        P.reset();
    }

    private static void range_Rational_Rational_fail_helper(int scale, @NotNull String a, @NotNull String b) {
        try {
            P.withScale(scale).range(Rational.read(a).get(), Rational.read(b).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRange_Rational_Rational() {
        range_Rational_Rational_helper(
                4,
                "0",
                "0",
                "QBarRandomProvider_range_Rational_Rational_i",
                0.0,
                1.000000000007918
        );
        range_Rational_Rational_helper(
                32,
                "0",
                "0",
                "QBarRandomProvider_range_Rational_Rational_ii",
                0.0,
                1.000000000007918
        );
        range_Rational_Rational_helper(
                4,
                "5/3",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_iii",
                1.6666666666766063,
                4.999999999895295
        );
        range_Rational_Rational_helper(
                32,
                "5/3",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_iv",
                1.6666666666766063,
                4.999999999895295
        );
        range_Rational_Rational_helper(
                4,
                "0",
                "1",
                "QBarRandomProvider_range_Rational_Rational_v",
                0.43764761498431315,
                2.771136999989421
        );
        range_Rational_Rational_helper(
                32,
                "0",
                "1",
                "QBarRandomProvider_range_Rational_Rational_vi",
                0.4855748611217715,
                29.795897000025235
        );
        range_Rational_Rational_helper(
                4,
                "-1",
                "0",
                "QBarRandomProvider_range_Rational_Rational_vii",
                -0.562352385009498,
                2.89502099998304
        );
        range_Rational_Rational_helper(
                32,
                "-1",
                "0",
                "QBarRandomProvider_range_Rational_Rational_viii",
                -0.5144251388771651,
                29.8250019999991
        );
        range_Rational_Rational_helper(
                4,
                "1/3",
                "1/2",
                "QBarRandomProvider_range_Rational_Rational_ix",
                0.40627460249656705,
                5.237116000060525
        );
        range_Rational_Rational_helper(
                32,
                "1/3",
                "1/2",
                "QBarRandomProvider_range_Rational_Rational_x",
                0.41426247685353923,
                33.696076999997224
        );
        range_Rational_Rational_helper(
                4,
                "-1",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_xi",
                0.1670603066320713,
                4.350258999958008
        );
        range_Rational_Rational_helper(
                32,
                "-1",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_xii",
                0.29486629632591527,
                31.072729000007964
        );
        range_Rational_Rational_fail_helper(3, "0", "1");
        range_Rational_Rational_fail_helper(-1, "0", "1");
        range_Rational_Rational_fail_helper(4, "1", "0");
        range_Rational_Rational_fail_helper(4, "1/2", "1/3");
    }

    private static void intervalHelper(@NotNull Iterable<Interval> xs, @NotNull String output, double bitSizeMean) {
        List<Interval> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(Interval::bitLength, sample))), bitSizeMean);
    }

    private static void finitelyBoundedIntervals_helper(int meanBitSize, @NotNull String output, double bitSizeMean) {
        intervalHelper(P.withScale(meanBitSize).finitelyBoundedIntervals(), output, bitSizeMean);
        P.reset();
    }

    private static void finitelyBoundedIntervals_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).finitelyBoundedIntervals();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testFinitelyBoundedIntervals() {
        finitelyBoundedIntervals_helper(6, "QBarRandomProvider_finitelyBoundedIntervals_i", 5.139956999987673);
        finitelyBoundedIntervals_helper(16, "QBarRandomProvider_finitelyBoundedIntervals_ii", 15.568145999994938);
        finitelyBoundedIntervals_helper(32, "QBarRandomProvider_finitelyBoundedIntervals_iii", 31.618885000011975);
        finitelyBoundedIntervals_fail_helper(5);
        finitelyBoundedIntervals_fail_helper(0);
        finitelyBoundedIntervals_fail_helper(-1);
    }

    private static void intervals_helper(int meanBitSize, @NotNull String output, double bitSizeMean) {
        intervalHelper(P.withScale(meanBitSize).intervals(), output, bitSizeMean);
        P.reset();
    }

    private static void intervals_fail_helper(int meanBitSize) {
        try {
            P.withScale(meanBitSize).intervals();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testIntervals() {
        intervals_helper(6, "QBarRandomProvider_intervals_i", 3.146318999988329);
        intervals_helper(16, "QBarRandomProvider_intervals_ii", 12.45336999999764);
        intervals_helper(32, "QBarRandomProvider_intervals_iii", 28.10473700001665);
        intervals_fail_helper(5);
        intervals_fail_helper(0);
        intervals_fail_helper(-1);
    }

    private static void rationalsIn_helper(
            int scale,
            @NotNull String a,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(P.withScale(scale).rationalsIn(Interval.read(a).get()), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void rationalsIn_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rationalsIn(Interval.read(a).get());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalsIn() {
        rationalsIn_helper(4, "[0, 0]", "QBarRandomProvider_rationalsIn_i", 0.0, 1.000000000007918);
        rationalsIn_helper(32, "[0, 0]", "QBarRandomProvider_rationalsIn_ii", 0.0, 1.000000000007918);
        rationalsIn_helper(
                4,
                "[1, 1]",
                "QBarRandomProvider_rationalsIn_iii",
                1.000000000007918,
                2.000000000015836
        );
        rationalsIn_helper(
                32,
                "[1, 1]",
                "QBarRandomProvider_rationalsIn_iv",
                1.000000000007918,
                2.000000000015836
        );
        rationalsIn_helper(
                4,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_rationalsIn_v",
                4459.875622663981,
                3.869968999988822
        );
        rationalsIn_helper(
                32,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_rationalsIn_vi",
                -3.784942556617747E71,
                31.82849000002398
        );
        rationalsIn_helper(
                4,
                "[1, 4]",
                "QBarRandomProvider_rationalsIn_vii",
                2.3129428449434144,
                4.068563999969932
        );
        rationalsIn_helper(
                32,
                "[1, 4]",
                "QBarRandomProvider_rationalsIn_viii",
                2.456724583363463,
                31.33094400000747
        );
        rationalsIn_helper(
                4,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsIn_ix",
                -6.7309163488312524,
                4.3459919999995815
        );
        rationalsIn_helper(
                32,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsIn_x",
                -2.30035078376369E59,
                37.958985000008944
        );
        rationalsIn_helper(
                4,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsIn_xi",
                7.73091634876883,
                5.011827999992238
        );
        rationalsIn_helper(
                32,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsIn_xii",
                2.30035078376369E59,
                38.21444000001194
        );
        rationalsIn_fail_helper(3, "[0, 0]");
        rationalsIn_fail_helper(-1, "[0, 0]");
    }

    private static void rationalsNotIn_helper(
            int scale,
            @NotNull String a,
            @NotNull String output,
            double sampleMean,
            double bitSizeMean
    ) {
        rationalHelper(
                P.withScale(scale).rationalsNotIn(Interval.read(a).get()),
                output,
                sampleMean,
                bitSizeMean
        );
        P.reset();
    }

    private static void rationalsNotIn_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rationalsNotIn(Interval.read(a).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalsNotIn() {
        rationalsNotIn_helper(
                4,
                "[0, 0]",
                "QBarRandomProvider_rationalsNotIn_i",
                -1514.3998931402477,
                4.699111999987303
        );
        rationalsNotIn_helper(
                32,
                "[0, 0]",
                "QBarRandomProvider_rationalsNotIn_ii",
                -3.784942556617747E71,
                32.0148380000219
        );
        rationalsNotIn_helper(
                4,
                "[1, 1]",
                "QBarRandomProvider_rationalsNotIn_iii",
                3405.7680966854173,
                4.0221769999945876
        );
        rationalsNotIn_helper(
                32,
                "[1, 1]",
                "QBarRandomProvider_rationalsNotIn_iv",
                -3.784942556617747E71,
                31.911038000025872
        );
        rationalsNotIn_helper(
                4,
                "[1, 4]",
                "QBarRandomProvider_rationalsNotIn_v",
                -69.41248621922566,
                4.310511999997506
        );
        rationalsNotIn_helper(
                32,
                "[1, 4]",
                "QBarRandomProvider_rationalsNotIn_vi",
                -4.8445650251622075E53,
                38.611619000014095
        );
        rationalsNotIn_helper(
                4,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsNotIn_vii",
                9.925816351081805,
                5.681415999961638
        );
        rationalsNotIn_helper(
                32,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsNotIn_viii",
                2.30035078376369E59,
                39.36069900002264
        );
        rationalsNotIn_helper(
                4,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsNotIn_ix",
                -8.92581635109024,
                4.794851000002666
        );
        rationalsNotIn_helper(
                32,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsNotIn_x",
                -2.30035078376369E59,
                39.096911000018515
        );
        rationalsNotIn_fail_helper(3, "[0, 0]");
        rationalsNotIn_fail_helper(-1, "[0, 0]");
        rationalsNotIn_fail_helper(4, "(-Infinity, Infinity)");
    }

    private static void vectors_helper(
            @NotNull Iterable<Vector> input,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        List<Vector> sample = toList(take(DEFAULT_SAMPLE_SIZE, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(Vector::dimension, sample))), meanDimension);
        aeq(meanOfIntegers(toList(concatMap(v -> map(BigInteger::bitLength, v), sample))), meanCoordinateBitSize);
        P.reset();
    }

    private static void vectors_int_helper(
            int scale,
            int dimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        vectors_helper(P.withScale(scale).vectors(dimension), output, meanDimension, meanCoordinateBitSize);
    }

    private static void vectors_int_fail_helper(int scale, int dimension) {
        try {
            P.withScale(scale).vectors(dimension);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testVectors_int() {
        vectors_int_helper(1, 0, "QBarRandomProvider_vectors_int_i", 0.0, 0.0);
        vectors_int_helper(5, 3, "QBarRandomProvider_vectors_int_ii", 2.9999999999775233, 4.884982000065708);
        vectors_int_helper(10, 8, "QBarRandomProvider_vectors_int_iii", 8.000000000063345, 9.922074749910346);
        vectors_int_fail_helper(1, -1);
        vectors_int_fail_helper(0, 0);
    }

    private static void vectors_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        vectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).vectors(),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void vectors_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).vectors();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testVectors() {
        vectors_helper(1, 1, "QBarRandomProvider_vectors_i", 1.0006929999977097, 0.8337352214893942);
        vectors_helper(5, 3, "QBarRandomProvider_vectors_ii", 3.00101199999147, 4.8827315585850855);
        vectors_helper(32, 8, "QBarRandomProvider_vectors_iii", 8.007115000016897, 31.973857750448634);
        vectors_fail_helper(0, 1);
        vectors_fail_helper(1, 0);
    }

    private static void vectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        vectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).vectorsAtLeast(minDimension),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void vectorsAtLeast_fail_helper(int scale, int secondaryScale, int minDimension) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).vectorsAtLeast(minDimension);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testVectorsAtLeast() {
        vectorsAtLeast_helper(1, 1, 0, "QBarRandomProvider_vectorsAtLeast_i", 1.0006929999977097, 0.8337352214893942);
        vectorsAtLeast_helper(5, 3, 0, "QBarRandomProvider_vectorsAtLeast_ii", 3.00101199999147, 4.8827315585850855);
        vectorsAtLeast_helper(5, 3, 1, "QBarRandomProvider_vectorsAtLeast_iii", 3.0018729999898364, 4.884003087404542);
        vectorsAtLeast_helper(5, 3, 2, "QBarRandomProvider_vectorsAtLeast_iv", 3.0005879999687126, 4.88591636041098);
        vectorsAtLeast_helper(10, 8, 0, "QBarRandomProvider_vectorsAtLeast_v", 7.983070000016452, 9.924974853054824);
        vectorsAtLeast_helper(10, 8, 3, "QBarRandomProvider_vectorsAtLeast_vi", 8.003411000000233, 9.922824405726274);
        vectorsAtLeast_helper(10, 8, 7, "QBarRandomProvider_vectorsAtLeast_vii", 8.00086800006071, 9.92217406943892);
        vectorsAtLeast_fail_helper(1, 1, 1);
        vectorsAtLeast_fail_helper(1, 1, 2);
        vectorsAtLeast_fail_helper(0, 1, 0);
        vectorsAtLeast_fail_helper(3, 2, -1);
    }

    private static void rationalVectors_helper(
            @NotNull Iterable<RationalVector> input,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        List<RationalVector> sample = toList(take(DEFAULT_SAMPLE_SIZE, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(RationalVector::dimension, sample))), meanDimension);
        aeq(meanOfIntegers(toList(concatMap(v -> map(Rational::bitLength, v), sample))), meanCoordinateBitSize);
        P.reset();
    }

    private static void rationalVectors_int_helper(
            int scale,
            int dimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).rationalVectors(dimension),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void rationalVectors_int_fail_helper(int scale, int dimension) {
        try {
            P.withScale(scale).rationalVectors(dimension);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalVectors_int() {
        rationalVectors_int_helper(3, 0, "QBarRandomProvider_rationalVectors_int_i", 0.0, 0.0);
        rationalVectors_int_helper(
                5,
                3,
                "QBarRandomProvider_rationalVectors_int_ii",
                2.9999999999775233,
                4.807125333432253
        );
        rationalVectors_int_helper(
                10,
                8,
                "QBarRandomProvider_rationalVectors_int_iii",
                8.000000000063345,
                9.833448374912013
        );
        rationalVectors_int_fail_helper(3, -1);
        rationalVectors_int_fail_helper(2, 0);
    }

    private static void rationalVectors_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalVectors(),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void rationalVectors_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalVectors();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalVectors() {
        rationalVectors_helper(3, 1, "QBarRandomProvider_rationalVectors_i", 0.9992619999977276, 2.7824344366226037);
        rationalVectors_helper(5, 3, "QBarRandomProvider_rationalVectors_ii", 3.0012729999915444, 4.8070228865365285);
        rationalVectors_helper(32, 8, "QBarRandomProvider_rationalVectors_iii", 7.994579000016383, 31.793640290456956);
        rationalVectors_fail_helper(2, 1);
        rationalVectors_fail_helper(3, 0);
    }

    private static void rationalVectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalVectorsAtLeast(minDimension),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void rationalVectorsAtLeast_fail_helper(int scale, int secondaryScale, int minDimension) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalVectorsAtLeast() {
        rationalVectorsAtLeast_helper(
                3,
                1,
                0,
                "QBarRandomProvider_rationalVectorsAtLeast_i",
                0.9992619999977276,
                2.7824344366226037
        );
        rationalVectorsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_rationalVectorsAtLeast_ii",
                3.0012729999915444,
                4.8070228865365285
        );
        rationalVectorsAtLeast_helper(
                5,
                3,
                1,
                "QBarRandomProvider_rationalVectorsAtLeast_iii",
                2.9984189999898554,
                4.808294971497336
        );
        rationalVectorsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_rationalVectorsAtLeast_iv",
                3.0014039999687,
                4.806596179622499
        );
        rationalVectorsAtLeast_helper(
                10,
                8,
                0,
                "QBarRandomProvider_rationalVectorsAtLeast_v",
                8.013159000016772,
                9.830493816441328
        );
        rationalVectorsAtLeast_helper(
                10,
                8,
                3,
                "QBarRandomProvider_rationalVectorsAtLeast_vi",
                8.010402000000056,
                9.831849886724278
        );
        rationalVectorsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_rationalVectorsAtLeast_vii",
                8.001091000060933,
                9.833612191283711
        );
        rationalVectorsAtLeast_fail_helper(3, 1, 1);
        rationalVectorsAtLeast_fail_helper(3, 1, 2);
        rationalVectorsAtLeast_fail_helper(2, 1, 0);
        rationalVectorsAtLeast_fail_helper(3, 2, -1);
    }

    private static void reducedRationalVectors_int_helper(
            int scale,
            int dimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).reducedRationalVectors(dimension),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void reducedRationalVectors_int_fail_helper(int scale, int dimension) {
        try {
            P.withScale(scale).reducedRationalVectors(dimension);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testReducedRationalVectors_int() {
        reducedRationalVectors_int_helper(1, 0, "QBarRandomProvider_reducedRationalVectors_int_i", 0.0, 0.0);
        reducedRationalVectors_int_helper(
                2,
                1,
                "QBarRandomProvider_reducedRationalVectors_int_ii",
                1.000000000007918,
                1.2499729999900737
        );
        reducedRationalVectors_int_helper(
                5,
                3,
                "QBarRandomProvider_reducedRationalVectors_int_iii",
                2.9999999999775233,
                6.870228666794007
        );
        reducedRationalVectors_int_helper(
                10,
                8,
                "QBarRandomProvider_reducedRationalVectors_int_iv",
                8.000000000063345,
                16.692675999563708
        );
        reducedRationalVectors_int_fail_helper(1, -1);
        reducedRationalVectors_int_fail_helper(0, 0);
    }

    private static void reducedRationalVectors_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).reducedRationalVectors(),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void reducedRationalVectors_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).reducedRationalVectors();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testReducedRationalVectors() {
        reducedRationalVectors_helper(
                1,
                1,
                "QBarRandomProvider_reducedRationalVectors_i",
                0.6515059999979717,
                1.6404990898138534
        );
        reducedRationalVectors_helper(
                5,
                3,
                "QBarRandomProvider_reducedRationalVectors_ii",
                2.5163809999931646,
                7.688697379443966
        );
        reducedRationalVectors_helper(
                32,
                8,
                "QBarRandomProvider_reducedRationalVectors_iii",
                8.044995000003032,
                56.723622948818225
        );
        reducedRationalVectors_fail_helper(0, 1);
        reducedRationalVectors_fail_helper(1, 0);
    }

    private static void reducedRationalVectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).reducedRationalVectorsAtLeast(minDimension),
                output,
                meanDimension,
                meanCoordinateBitSize
        );
    }

    private static void reducedRationalVectorsAtLeast_fail_helper(int scale, int secondaryScale, int minDimension) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).reducedRationalVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testReducedRationalVectorsAtLeast() {
        reducedRationalVectorsAtLeast_helper(
                1,
                1,
                0,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_i",
                0.6515059999979717,
                1.6404990898138534
        );
        reducedRationalVectorsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_ii",
                2.5163809999931646,
                7.688697379443966
        );
        reducedRationalVectorsAtLeast_helper(
                5,
                3,
                1,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_iii",
                3.6060739999871925,
                7.173386070062897
        );
        reducedRationalVectorsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_iv",
                3.2424949999676405,
                7.015494549601162
        );
        reducedRationalVectorsAtLeast_helper(
                10,
                8,
                0,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_v",
                7.934091000004385,
                17.246808487906456
        );
        reducedRationalVectorsAtLeast_helper(
                10,
                8,
                3,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_vi",
                8.239842999997473,
                16.818875068721916
        );
        reducedRationalVectorsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_reducedRationalVectorsAtLeast_vii",
                8.00514600006095,
                16.693559867603597
        );
        reducedRationalVectorsAtLeast_fail_helper(1, 1, 1);
        reducedRationalVectorsAtLeast_fail_helper(1, 1, 2);
        reducedRationalVectorsAtLeast_fail_helper(0, 1, 0);
        reducedRationalVectorsAtLeast_fail_helper(1, 1, -1);
    }

    private static void polynomialVectors_helper(
            @NotNull Iterable<PolynomialVector> input,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<PolynomialVector> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(PolynomialVector::dimension, sample))), meanDimension);
        aeq(meanOfIntegers(toList(concatMap(v -> map(Polynomial::degree, v), sample))), meanCoordinateDegree);
        aeq(meanOfIntegers(
                toList(concatMap(v -> concatMap(p -> map(BigInteger::bitLength, p), v), sample))),
                meanCoordinateCoefficientBitSize
        );
        P.reset();
    }

    private static void polynomialVectors_int_helper(
            int scale,
            int secondaryScale,
            int dimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomialVectors(dimension),
                output,
                meanDimension,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void polynomialVectors_int_fail_helper(int scale, int secondaryScale, int dimension) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).polynomialVectors(dimension);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomialVectors_int() {
        polynomialVectors_int_helper(1, 0, 0, "QBarRandomProvider_polynomialVectors_int_i", 0.0, 0.0, 0.0);
        polynomialVectors_int_helper(
                5,
                3,
                3,
                "QBarRandomProvider_polynomialVectors_int_ii",
                3.000000000005079,
                2.841233333337271,
                5.0814477988870035
        );
        polynomialVectors_int_fail_helper(0, 0, 2);
        polynomialVectors_int_fail_helper(1, -1, 2);
        polynomialVectors_int_fail_helper(1, 0, -1);
    }

    private static void polynomialVectors_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .polynomialVectors(),
                output,
                meanDimension,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void polynomialVectors_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale).polynomialVectors();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomialVectors() {
        polynomialVectors_helper(
                1,
                0,
                1,
                "QBarRandomProvider_polynomialVectors_i",
                1.0080899999996153,
                -0.3357438323961038,
                1.2442692233026915
        );
        polynomialVectors_helper(
                5,
                3,
                2,
                "QBarRandomProvider_polynomialVectors_ii",
                2.0116100000000747,
                2.863313465336309,
                5.084824254831631
        );
        polynomialVectors_fail_helper(0, 0, 1);
        polynomialVectors_fail_helper(1, -1, 1);
        polynomialVectors_fail_helper(1, 0, 0);
    }

    private static void polynomialVectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            int minDimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .polynomialVectorsAtLeast(minDimension),
                output,
                meanDimension,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void polynomialVectorsAtLeast_fail_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            int minDimension
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .polynomialVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomialVectorsAtLeast() {
        polynomialVectorsAtLeast_helper(
                1,
                0,
                1,
                0,
                "QBarRandomProvider_polynomialVectorsAtLeast_i",
                1.0080899999996153,
                -0.3357438323961038,
                1.2442692233026915
        );
        polynomialVectorsAtLeast_helper(
                5,
                3,
                1,
                0,
                "QBarRandomProvider_polynomialVectorsAtLeast_ii",
                1.006299999999629,
                2.8524992546957577,
                5.083236818273093
        );
        polynomialVectorsAtLeast_helper(
                5,
                3,
                2,
                1,
                "QBarRandomProvider_polynomialVectorsAtLeast_iii",
                2.0001200000002344,
                2.8603683778951354,
                5.081219240520803
        );
        polynomialVectorsAtLeast_helper(
                5,
                3,
                3,
                2,
                "QBarRandomProvider_polynomialVectorsAtLeast_iv",
                3.005030000001571,
                2.8498517485643293,
                5.080032535425985
        );
        polynomialVectorsAtLeast_fail_helper(0, 0, 1, 0);
        polynomialVectorsAtLeast_fail_helper(1, -1, 1, 0);
        polynomialVectorsAtLeast_fail_helper(1, 0, 0, -1);
        polynomialVectorsAtLeast_fail_helper(1, 0, 1, -1);
    }

    private static void rationalPolynomialVectors_helper(
            @NotNull Iterable<RationalPolynomialVector> input,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<RationalPolynomialVector> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(RationalPolynomialVector::dimension, sample))), meanDimension);
        aeq(meanOfIntegers(toList(concatMap(v -> map(RationalPolynomial::degree, v), sample))), meanCoordinateDegree);
        aeq(meanOfIntegers(
                toList(concatMap(v -> concatMap(p -> map(Rational::bitLength, p), v), sample))),
                meanCoordinateCoefficientBitSize
        );
        P.reset();
    }

    private static void rationalPolynomialVectors_int_helper(
            int scale,
            int secondaryScale,
            int dimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialVectors(dimension),
                output,
                meanDimension,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void rationalPolynomialVectors_int_fail_helper(int scale, int secondaryScale, int dimension) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialVectors(dimension);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomialVectors_int() {
        rationalPolynomialVectors_int_helper(
                3,
                0,
                0,
                "QBarRandomProvider_rationalPolynomialVectors_int_i",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialVectors_int_helper(
                5,
                3,
                3,
                "QBarRandomProvider_rationalPolynomialVectors_int_ii",
                3.000000000005079,
                2.856606666670599,
                4.960392642253054
        );
        rationalPolynomialVectors_int_fail_helper(2, 0, 2);
        rationalPolynomialVectors_int_fail_helper(3, -1, 2);
        rationalPolynomialVectors_int_fail_helper(3, 0, -1);
    }

    private static void rationalPolynomialVectors_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .rationalPolynomialVectors(),
                output,
                meanDimension,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void rationalPolynomialVectors_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .rationalPolynomialVectors();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomialVectors() {
        rationalPolynomialVectors_helper(
                3,
                0,
                1,
                "QBarRandomProvider_rationalPolynomialVectors_i",
                1.0022699999996099,
                -0.2237620601232904,
                3.2825964010289757
        );
        rationalPolynomialVectors_helper(
                5,
                3,
                2,
                "QBarRandomProvider_rationalPolynomialVectors_ii",
                2.0040200000000756,
                2.85786568996148,
                4.961487419860152
        );
        rationalPolynomialVectors_fail_helper(2, 0, 1);
        rationalPolynomialVectors_fail_helper(3, -1, 1);
        rationalPolynomialVectors_fail_helper(3, 0, 0);
    }

    private static void rationalPolynomialVectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            int minDimension,
            @NotNull String output,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .rationalPolynomialVectorsAtLeast(minDimension),
                output,
                meanDimension,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void rationalPolynomialVectorsAtLeast_fail_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            int minDimension
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .rationalPolynomialVectorsAtLeast(minDimension);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomialVectorsAtLeast() {
        rationalPolynomialVectorsAtLeast_helper(
                3,
                0,
                1,
                0,
                "QBarRandomProvider_rationalPolynomialVectorsAtLeast_i",
                1.0022699999996099,
                -0.2237620601232904,
                3.2825964010289757
        );
        rationalPolynomialVectorsAtLeast_helper(
                5,
                3,
                1,
                0,
                "QBarRandomProvider_rationalPolynomialVectorsAtLeast_ii",
                1.001089999999618,
                2.8693224385427754,
                4.963606933180434
        );
        rationalPolynomialVectorsAtLeast_helper(
                5,
                3,
                2,
                1,
                "QBarRandomProvider_rationalPolynomialVectorsAtLeast_iii",
                2.0056700000002503,
                2.8553799977069874,
                4.962938822759252
        );
        rationalPolynomialVectorsAtLeast_helper(
                5,
                3,
                3,
                2,
                "QBarRandomProvider_rationalPolynomialVectorsAtLeast_iv",
                3.000630000001584,
                2.852697600166462,
                4.962965333734074
        );
        rationalPolynomialVectorsAtLeast_fail_helper(2, 0, 1, 0);
        rationalPolynomialVectorsAtLeast_fail_helper(3, -1, 1, 0);
        rationalPolynomialVectorsAtLeast_fail_helper(3, 0, 0, -1);
        rationalPolynomialVectorsAtLeast_fail_helper(3, 0, 1, -1);
    }

    private static void matrices_helper(
            @NotNull Iterable<Matrix> input,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        List<Matrix> sample = toList(take(DEFAULT_SAMPLE_SIZE, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(m -> m.height() * m.width(), sample))), meanElementCount);
        aeq(
                meanOfIntegers(
                        toList(concatMap(m -> concatMap(v -> map(BigInteger::bitLength, v), m.rows()), sample))
                ),
                meanCoordinateBitSize
        );
        P.reset();
    }

    private static void matrices_int_int_helper(
            int scale,
            int height,
            int width,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        matrices_helper(
                P.withScale(scale).matrices(height, width),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void matrices_int_int_fail_helper(int scale, int height, int width) {
        try {
            P.withScale(scale).matrices(height, width);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMatrices_int_int() {
        matrices_int_int_helper(1, 0, 0, "QBarRandomProvider_matrices_int_int_i", 0.0, 0.0);
        matrices_int_int_helper(5, 0, 0, "QBarRandomProvider_matrices_int_int_ii", 0.0, 0.0);
        matrices_int_int_helper(10, 0, 0, "QBarRandomProvider_matrices_int_int_iii", 0.0, 0.0);
        matrices_int_int_helper(1, 0, 3, "QBarRandomProvider_matrices_int_int_iv", 0.0, 0.0);
        matrices_int_int_helper(5, 0, 3, "QBarRandomProvider_matrices_int_int_v", 0.0, 0.0);
        matrices_int_int_helper(10, 0, 3, "QBarRandomProvider_matrices_int_int_vi", 0.0, 0.0);
        matrices_int_int_helper(3, 3, 0, "QBarRandomProvider_matrices_int_int_vii", 0.0, 0.0);
        matrices_int_int_helper(5, 3, 0, "QBarRandomProvider_matrices_int_int_viii", 0.0, 0.0);
        matrices_int_int_helper(10, 3, 0, "QBarRandomProvider_matrices_int_int_ix", 0.0, 0.0);
        matrices_int_int_helper(
                1,
                1,
                1,
                "QBarRandomProvider_matrices_int_int_x",
                1.000000000007918,
                0.8333389999976124
        );
        matrices_int_int_helper(
                5,
                1,
                1,
                "QBarRandomProvider_matrices_int_int_xi",
                1.000000000007918,
                4.889747000000939
        );
        matrices_int_int_helper(
                10,
                1,
                1,
                "QBarRandomProvider_matrices_int_int_xii",
                1.000000000007918,
                9.918277000004432
        );
        matrices_int_int_helper(
                1,
                2,
                2,
                "QBarRandomProvider_matrices_int_int_xiii",
                4.000000000031672,
                0.8338739999974718
        );
        matrices_int_int_helper(
                5,
                2,
                2,
                "QBarRandomProvider_matrices_int_int_xiv",
                4.000000000031672,
                4.8827345000031865
        );
        matrices_int_int_helper(
                10,
                2,
                2,
                "QBarRandomProvider_matrices_int_int_xv",
                4.000000000031672,
                9.922179249978022
        );
        matrices_int_int_helper(
                1,
                3,
                4,
                "QBarRandomProvider_matrices_int_int_xvi",
                11.999999999910093,
                0.8332544167092473
        );
        matrices_int_int_helper(
                5,
                3,
                4,
                "QBarRandomProvider_matrices_int_int_xvii",
                11.999999999910093,
                4.8834181664187115
        );
        matrices_int_int_helper(
                10,
                3,
                4,
                "QBarRandomProvider_matrices_int_int_xviii",
                11.999999999910093,
                9.92268041633897
        );
        matrices_int_int_fail_helper(0, 0, 0);
        matrices_int_int_fail_helper(0, 0, 1);
        matrices_int_int_fail_helper(0, 1, 0);
        matrices_int_int_fail_helper(0, 1, 1);
        matrices_int_int_fail_helper(1, -1, 0);
        matrices_int_int_fail_helper(1, -1, 1);
        matrices_int_int_fail_helper(1, 0, -1);
        matrices_int_int_fail_helper(1, 1, -1);
        matrices_int_int_fail_helper(1, -1, -1);
    }

    private static void matrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        matrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).matrices(),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void matrices_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).matrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMatrices() {
        matrices_helper(1, 2, "QBarRandomProvider_matrices_i", 1.9983549999915837, 0.8338733608407535);
        matrices_helper(5, 3, "QBarRandomProvider_matrices_ii", 3.3335839999865646, 4.882194059052771);
        matrices_helper(10, 8, "QBarRandomProvider_matrices_iii", 8.189681000015133, 9.921846650751569);
        matrices_fail_helper(0, 2);
        matrices_fail_helper(1, 1);
        matrices_fail_helper(1, -1);
    }

    private static void squareMatrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        matrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).squareMatrices(),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void squareMatrices_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).squareMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquareMatrices() {
        squareMatrices_helper(2, 2, "QBarRandomProvider_squareMatrices_i", 3.0031260000035953, 1.833468525820871);
        squareMatrices_helper(5, 3, "QBarRandomProvider_squareMatrices_ii", 4.821780000018768, 4.883059368202011);
        squareMatrices_helper(10, 8, "QBarRandomProvider_squareMatrices_iii", 13.49856700000753, 9.923964892149032);
        squareMatrices_fail_helper(1, 2);
        squareMatrices_fail_helper(2, 1);
        squareMatrices_fail_helper(2, -1);
    }

    private static void invertibleMatrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        matrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).invertibleMatrices(),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void invertibleMatrices_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).invertibleMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testInvertibleMatrices() {
        invertibleMatrices_helper(
                2,
                2,
                "QBarRandomProvider_invertibleMatrices_i",
                2.9828460000023997,
                1.9746480374547664
        );
        invertibleMatrices_helper(
                5,
                3,
                "QBarRandomProvider_invertibleMatrices_ii",
                5.099691000015061,
                4.999944310336789
        );
        invertibleMatrices_fail_helper(1, 2);
        invertibleMatrices_fail_helper(2, 1);
        invertibleMatrices_fail_helper(2, -1);
    }

    private static void rationalMatrices_helper(
            @NotNull Iterable<RationalMatrix> input,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        List<RationalMatrix> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(m -> m.height() * m.width(), sample))), meanElementCount);
        aeq(
                meanOfIntegers(toList(concatMap(m -> concatMap(v -> map(Rational::bitLength, v), m.rows()), sample))),
                meanCoordinateBitSize
        );
        P.reset();
    }

    private static void rationalMatrices_int_int_helper(
            int scale,
            int height,
            int width,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).rationalMatrices(height, width),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void rationalMatrices_int_int_fail_helper(int scale, int height, int width) {
        try {
            P.withScale(scale).rationalMatrices(height, width);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalMatrices_int_int() {
        rationalMatrices_int_int_helper(3, 0, 0, "QBarRandomProvider_rationalMatrices_int_int_i", 0.0, 0.0);
        rationalMatrices_int_int_helper(5, 0, 0, "QBarRandomProvider_rationalMatrices_int_int_ii", 0.0, 0.0);
        rationalMatrices_int_int_helper(10, 0, 0, "QBarRandomProvider_rationalMatrices_int_int_iii", 0.0, 0.0);
        rationalMatrices_int_int_helper(3, 0, 3, "QBarRandomProvider_rationalMatrices_int_int_iv", 0.0, 0.0);
        rationalMatrices_int_int_helper(5, 0, 3, "QBarRandomProvider_rationalMatrices_int_int_v", 0.0, 0.0);
        rationalMatrices_int_int_helper(10, 0, 3, "QBarRandomProvider_rationalMatrices_int_int_vi", 0.0, 0.0);
        rationalMatrices_int_int_helper(3, 3, 0, "QBarRandomProvider_rationalMatrices_int_int_vii", 0.0, 0.0);
        rationalMatrices_int_int_helper(5, 3, 0, "QBarRandomProvider_rationalMatrices_int_int_viii", 0.0, 0.0);
        rationalMatrices_int_int_helper(10, 3, 0, "QBarRandomProvider_rationalMatrices_int_int_ix", 0.0, 0.0);
        rationalMatrices_int_int_helper(
                3,
                1,
                1,
                "QBarRandomProvider_rationalMatrices_int_int_x",
                0.9999999999980838,
                2.7791000000017902
        );
        rationalMatrices_int_int_helper(
                5,
                1,
                1,
                "QBarRandomProvider_rationalMatrices_int_int_xi",
                0.9999999999980838,
                4.8117800000005735
        );
        rationalMatrices_int_int_helper(
                10,
                1,
                1,
                "QBarRandomProvider_rationalMatrices_int_int_xii",
                0.9999999999980838,
                9.869249999999408
        );
        rationalMatrices_int_int_helper(
                3,
                2,
                2,
                "QBarRandomProvider_rationalMatrices_int_int_xiii",
                3.999999999992335,
                2.7808624999918714
        );
        rationalMatrices_int_int_helper(
                5,
                2,
                2,
                "QBarRandomProvider_rationalMatrices_int_int_xiv",
                3.999999999992335,
                4.8148274999920595
        );
        rationalMatrices_int_int_helper(
                10,
                2,
                2,
                "QBarRandomProvider_rationalMatrices_int_int_xv",
                3.999999999992335,
                9.847875000001702
        );
        rationalMatrices_int_int_helper(
                3,
                3,
                4,
                "QBarRandomProvider_rationalMatrices_int_int_xvi",
                12.000000000020316,
                2.7812433333100226
        );
        rationalMatrices_int_int_helper(
                5,
                3,
                4,
                "QBarRandomProvider_rationalMatrices_int_int_xvii",
                12.000000000020316,
                4.808581666635954
        );
        rationalMatrices_int_int_helper(
                10,
                3,
                4,
                "QBarRandomProvider_rationalMatrices_int_int_xviii",
                12.000000000020316,
                9.83849583331666
        );
        rationalMatrices_int_int_fail_helper(2, 0, 0);
        rationalMatrices_int_int_fail_helper(2, 0, 1);
        rationalMatrices_int_int_fail_helper(2, 1, 0);
        rationalMatrices_int_int_fail_helper(2, 1, 1);
        rationalMatrices_int_int_fail_helper(3, -1, 0);
        rationalMatrices_int_int_fail_helper(3, -1, 1);
        rationalMatrices_int_int_fail_helper(3, 0, -1);
        rationalMatrices_int_int_fail_helper(3, 1, -1);
        rationalMatrices_int_int_fail_helper(3, -1, -1);
    }

    private static void rationalMatrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalMatrices(),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void rationalMatrices_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalMatrices() {
        rationalMatrices_helper(3, 2, "QBarRandomProvider_rationalMatrices_i", 3.0089700000010176, 2.781300577932931);
        rationalMatrices_helper(5, 3, "QBarRandomProvider_rationalMatrices_ii", 3.362760000001472, 4.8159131189959234);
        rationalMatrices_helper(10, 8, "QBarRandomProvider_rationalMatrices_iii", 8.16995000000032, 9.840558387733228);
        rationalMatrices_fail_helper(2, 2);
        rationalMatrices_fail_helper(3, 1);
        rationalMatrices_fail_helper(3, -1);
    }

    private static void squareRationalMatrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).squareRationalMatrices(),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void squareRationalMatrices_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).squareRationalMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquareRationalMatrices() {
        squareRationalMatrices_helper(
                3,
                2,
                "QBarRandomProvider_squareRationalMatrices_i",
                4.039990000000922,
                2.778798957428391
        );
        squareRationalMatrices_helper(
                5,
                3,
                "QBarRandomProvider_squareRationalMatrices_ii",
                4.8123099999997105,
                4.8100080834360694
        );
        squareRationalMatrices_helper(
                10,
                8,
                "QBarRandomProvider_squareRationalMatrices_iii",
                13.395589999994662,
                9.83388861560576
        );
        squareRationalMatrices_fail_helper(2, 2);
        squareRationalMatrices_fail_helper(3, 1);
        squareRationalMatrices_fail_helper(3, -1);
    }

    private static void invertibleRationalMatrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).invertibleRationalMatrices(),
                output,
                meanElementCount,
                meanCoordinateBitSize
        );
    }

    private static void invertibleRationalMatrices_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).invertibleRationalMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testInvertibleRationalMatrices() {
        invertibleRationalMatrices_helper(
                3,
                2,
                "QBarRandomProvider_invertibleRationalMatrices_i",
                4.212430000000649,
                2.937190647688096
        );
        invertibleRationalMatrices_helper(
                5,
                3,
                "QBarRandomProvider_invertibleRationalMatrices_ii",
                5.094529999999642,
                4.902308947073661
        );
        invertibleRationalMatrices_fail_helper(2, 2);
        invertibleRationalMatrices_fail_helper(3, 1);
        invertibleRationalMatrices_fail_helper(3, -1);
    }

    private static void polynomialMatrices_helper(
            @NotNull Iterable<PolynomialMatrix> input,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<PolynomialMatrix> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(m -> m.height() * m.width(), sample))), meanElementCount);
        aeq(
                meanOfIntegers(
                        toList(concatMap(m -> concatMap(v -> map(Polynomial::degree, v), m.rows()), sample))
                ),
                meanCoordinateDegree
        );
        aeq(
                meanOfIntegers(
                        toList(
                                concatMap(
                                        m -> concatMap(
                                                v -> concatMap(p -> map(BigInteger::bitLength, p), v),
                                                m.rows()
                                        ),
                                        sample
                                )
                        )
                ),
                meanCoordinateCoefficientBitSize
        );
        P.reset();
    }

    private static void polynomialMatrices_int_int_helper(
            int scale,
            int secondaryScale,
            int height,
            int width,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomialMatrices(height, width),
                output,
                meanElementCount,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void polynomialMatrices_int_int_fail_helper(int scale, int secondaryScale, int height, int width) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).polynomialMatrices(height, width);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomialMatrices_int_int() {
        polynomialMatrices_int_int_helper(
                1,
                0,
                0,
                0,
                "QBarRandomProvider_polynomialMatrices_int_int_i",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                0,
                0,
                "QBarRandomProvider_polynomialMatrices_int_int_ii",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                0,
                3,
                "QBarRandomProvider_polynomialMatrices_int_int_iii",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                0,
                3,
                "QBarRandomProvider_polynomialMatrices_int_int_iv",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                3,
                0,
                "QBarRandomProvider_polynomialMatrices_int_int_v",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                3,
                0,
                "QBarRandomProvider_polynomialMatrices_int_int_vi",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                1,
                1,
                "QBarRandomProvider_polynomialMatrices_int_int_vii",
                0.9999999999980838,
                -0.32367000000033347,
                1.244895243446822
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                1,
                1,
                "QBarRandomProvider_polynomialMatrices_int_int_viii",
                0.9999999999980838,
                1.8524199999994737,
                5.144137258885894
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                2,
                2,
                "QBarRandomProvider_polynomialMatrices_int_int_ix",
                3.999999999992335,
                -0.33104500000017434,
                1.246769214673558
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                2,
                2,
                "QBarRandomProvider_polynomialMatrices_int_int_x",
                3.999999999992335,
                1.857595000003193,
                5.132331558515564
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                3,
                4,
                "QBarRandomProvider_polynomialMatrices_int_int_xi",
                12.000000000020316,
                -0.33313666666926983,
                1.248762864952446
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                3,
                4,
                "QBarRandomProvider_polynomialMatrices_int_int_xii",
                12.000000000020316,
                1.8581416666733686,
                5.124858226625802
        );
        polynomialMatrices_int_int_fail_helper(1, 0, 0, -1);
        polynomialMatrices_int_int_fail_helper(1, 0, -1, 0);
        polynomialMatrices_int_int_fail_helper(1, -1, 0, 0);
        polynomialMatrices_int_int_fail_helper(0, 0, 0, 0);
    }

    private static void polynomialMatrices_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .polynomialMatrices(),
                output,
                meanElementCount,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void polynomialMatrices_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .polynomialMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomialMatrices() {
        polynomialMatrices_helper(
                1,
                0,
                2,
                "QBarRandomProvider_polynomialMatrices_i",
                2.0046799999997904,
                -0.3324770038113362,
                1.2498486739347054
        );
        polynomialMatrices_helper(
                5,
                2,
                3,
                "QBarRandomProvider_polynomialMatrices_ii",
                3.321210000001476,
                1.858843614221727,
                5.134214234689339
        );

        polynomialMatrices_fail_helper(0, 0, 2);
        polynomialMatrices_fail_helper(1, -1, 2);
        polynomialMatrices_fail_helper(1, 0, 1);
    }

    private static void squarePolynomialMatrices_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .squarePolynomialMatrices(),
                output,
                meanElementCount,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void squarePolynomialMatrices_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .squarePolynomialMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquarePolynomialMatrices() {
        squarePolynomialMatrices_helper(
                2,
                0,
                2,
                "QBarRandomProvider_squarePolynomialMatrices_i",
                2.970210000000717,
                -0.19636995364005183,
                2.292021198601562
        );
        squarePolynomialMatrices_helper(
                5,
                1,
                3,
                "QBarRandomProvider_squarePolynomialMatrices_ii",
                4.829039999999811,
                0.8766483607524744,
                5.212007622686154
        );
        squarePolynomialMatrices_helper(
                10,
                2,
                8,
                "QBarRandomProvider_squarePolynomialMatrices_iii",
                13.574059999994397,
                1.9278793522265476,
                10.172884877796545
        );

        squarePolynomialMatrices_fail_helper(1, 0, 2);
        squarePolynomialMatrices_fail_helper(2, -1, 2);
        squarePolynomialMatrices_fail_helper(2, 0, 1);
    }

    private static void rationalPolynomialMatrices_helper(
            @NotNull Iterable<RationalPolynomialMatrix> input,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<RationalPolynomialMatrix> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(m -> m.height() * m.width(), sample))), meanElementCount);
        aeq(
                meanOfIntegers(
                        toList(concatMap(m -> concatMap(v -> map(RationalPolynomial::degree, v), m.rows()), sample))
                ),
                meanCoordinateDegree
        );
        aeq(
                meanOfIntegers(
                        toList(
                                concatMap(
                                        m -> concatMap(
                                                v -> concatMap(p -> map(Rational::bitLength, p), v),
                                                m.rows()
                                        ),
                                        sample
                                )
                        )
                ),
                meanCoordinateCoefficientBitSize
        );
        P.reset();
    }

    private static void rationalPolynomialMatrices_int_int_helper(
            int scale,
            int secondaryScale,
            int height,
            int width,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialMatrices(height, width),
                output,
                meanElementCount,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void rationalPolynomialMatrices_int_int_fail_helper(
            int scale,
            int secondaryScale,
            int height,
            int width
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialMatrices(height, width);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomialMatrices_int_int() {
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                0,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_i",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                0,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_ii",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                0,
                3,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_iii",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                0,
                3,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_iv",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                3,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_v",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                3,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_vi",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                1,
                1,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_vii",
                0.9999999999980838,
                -0.22369000000018416,
                3.272416946837637
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                1,
                1,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_viii",
                0.9999999999980838,
                1.863679999999461,
                5.007410045814251
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                2,
                2,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_ix",
                3.999999999992335,
                -0.2205149999991518,
                3.2797006998225884
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                2,
                2,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_x",
                3.999999999992335,
                1.8569400000032832,
                5.003229854370796
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                3,
                4,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_xi",
                12.000000000020316,
                -0.22018500000217706,
                3.2814545757482825
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                3,
                4,
                "QBarRandomProvider_rationalPolynomialMatrices_int_int_xii",
                12.000000000020316,
                1.856026666673493,
                5.001508505901974
        );
        rationalPolynomialMatrices_int_int_fail_helper(3, 0, 0, -1);
        rationalPolynomialMatrices_int_int_fail_helper(3, 0, -1, 0);
        rationalPolynomialMatrices_int_int_fail_helper(3, -1, 0, 0);
        rationalPolynomialMatrices_int_int_fail_helper(2, 0, 0, 0);
    }

    private static void rationalPolynomialMatrices_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .rationalPolynomialMatrices(),
                output,
                meanElementCount,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void rationalPolynomialMatrices_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .rationalPolynomialMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomialMatrices() {
        rationalPolynomialMatrices_helper(
                3,
                0,
                2,
                "QBarRandomProvider_rationalPolynomialMatrices_i",
                3.029410000001085,
                -0.2227958579398594,
                3.277140927676659
        );
        rationalPolynomialMatrices_helper(
                5,
                2,
                3,
                "QBarRandomProvider_rationalPolynomialMatrices_ii",
                3.33301000000145,
                1.8598624066504423,
                5.000145825345007
        );

        rationalPolynomialMatrices_fail_helper(2, 0, 2);
        rationalPolynomialMatrices_fail_helper(3, -1, 2);
        rationalPolynomialMatrices_fail_helper(3, 0, 1);
    }

    private static void squareRationalPolynomialMatrices_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .squareRationalPolynomialMatrices(),
                output,
                meanElementCount,
                meanCoordinateDegree,
                meanCoordinateCoefficientBitSize
        );
    }

    private static void squareRationalPolynomialMatrices_fail_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .squareRationalPolynomialMatrices();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquareRationalPolynomialMatrices() {
        squareRationalPolynomialMatrices_helper(
                3,
                0,
                2,
                "QBarRandomProvider_squareRationalPolynomialMatrices_i",
                4.066370000000857,
                -0.2180691870136281,
                3.280631018804514
        );
        squareRationalPolynomialMatrices_helper(
                5,
                1,
                3,
                "QBarRandomProvider_squareRationalPolynomialMatrices_ii",
                4.755019999999816,
                0.8735799218534637,
                5.06617307847439
        );
        squareRationalPolynomialMatrices_helper(
                10,
                2,
                8,
                "QBarRandomProvider_squareRationalPolynomialMatrices_iii",
                13.398729999994606,
                1.9599462038549331,
                9.955424573932891
        );

        squareRationalPolynomialMatrices_fail_helper(2, 0, 2);
        squareRationalPolynomialMatrices_fail_helper(3, -1, 2);
        squareRationalPolynomialMatrices_fail_helper(3, 0, 1);
    }

    private static void polynomials_helper(
            int defaultSampleSize,
            @NotNull Iterable<Polynomial> input,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        List<Polynomial> sample = toList(take(defaultSampleSize, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(Polynomial::degree, sample))), meanDegree);
        aeq(meanOfIntegers(toList(concatMap(p -> map(BigInteger::bitLength, p), sample))), meanCoefficientBitSize);
        P.reset();
    }

    private static void polynomials_helper(
            @NotNull Iterable<Polynomial> input,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(DEFAULT_SAMPLE_SIZE, input, output, meanDegree, meanCoefficientBitSize);
    }

    private static void polynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).polynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void polynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).polynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomials_int() {
        polynomials_int_helper(1, -1, "QBarRandomProvider_polynomials_int_i", -1.000000000007918, 0.0);
        polynomials_int_helper(5, 3, "QBarRandomProvider_polynomials_int_ii", 2.9999999999775233, 5.125149750005694);
        polynomials_int_helper(10, 8, "QBarRandomProvider_polynomials_int_iii", 8.000000000063345, 10.031244555547099);
        polynomials_int_fail_helper(0, -1);
        polynomials_int_fail_helper(1, -2);
    }

    private static void polynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void polynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).polynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomials() {
        polynomials_helper(1, 0, "QBarRandomProvider_polynomials_i", -0.33297599999650446, 1.2495277531222848);
        polynomials_helper(5, 3, "QBarRandomProvider_polynomials_ii", 2.850227999989568, 5.076649486766766);
        polynomials_helper(10, 8, "QBarRandomProvider_polynomials_iii", 7.919980000002783, 10.01935105242132);
        polynomials_fail_helper(0, 0);
        polynomials_fail_helper(1, -1);
    }

    private static void polynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void polynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).polynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPolynomialsAtLeast() {
        polynomialsAtLeast_helper(
                1,
                0,
                -1,
                "QBarRandomProvider_polynomialsAtLeast_i",
                -0.33297599999650446,
                1.2495277531222848
        );
        polynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_polynomialsAtLeast_ii",
                2.850227999989568,
                5.076649486766766
        );
        polynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_polynomialsAtLeast_iii",
                2.9960619999915905,
                5.127906423927053
        );
        polynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_polynomialsAtLeast_iv",
                3.0029149999686346,
                5.125061111715066
        );
        polynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_polynomialsAtLeast_v",
                7.919980000002783,
                10.01935105242132
        );
        polynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_polynomialsAtLeast_vi",
                8.004555000013825,
                10.032780076379966
        );
        polynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_polynomialsAtLeast_vii",
                8.000700000060865,
                10.031968846717554
        );
        polynomialsAtLeast_fail_helper(0, 0, -1);
        polynomialsAtLeast_fail_helper(1, -1, -1);
        polynomialsAtLeast_fail_helper(1, 3, 3);
        polynomialsAtLeast_fail_helper(1, 1, -2);
    }

    private static void primitivePolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).primitivePolynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void primitivePolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).primitivePolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPrimitivePolynomials_int() {
        primitivePolynomials_int_helper(
                1,
                0,
                "QBarRandomProvider_primitivePolynomials_int_i",
                0.0,
                0.5000349999935414
        );
        primitivePolynomials_int_helper(
                1,
                1,
                "QBarRandomProvider_primitivePolynomials_int_ii",
                1.000000000007918,
                1.0773089999900907
        );
        primitivePolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_primitivePolynomials_int_iii",
                2.9999999999775233,
                5.133858750005687
        );
        primitivePolynomials_int_helper(
                10,
                8,
                "QBarRandomProvider_primitivePolynomials_int_iv",
                8.000000000063345,
                10.031551999991857
        );
        primitivePolynomials_int_fail_helper(0, 0);
        primitivePolynomials_int_fail_helper(1, -1);
    }

    private static void primitivePolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).primitivePolynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void primitivePolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).primitivePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPrimitivePolynomials() {
        primitivePolynomials_helper(
                1,
                1,
                "QBarRandomProvider_primitivePolynomials_i",
                1.3170959999927159,
                0.9483072777233684
        );
        primitivePolynomials_helper(
                5,
                3,
                "QBarRandomProvider_primitivePolynomials_ii",
                4.135319999990011,
                5.005227717069355
        );
        primitivePolynomials_helper(
                10,
                8,
                "QBarRandomProvider_primitivePolynomials_iii",
                9.45433400000822,
                10.00177486274624
        );
        primitivePolynomials_fail_helper(0, 1);
        primitivePolynomials_fail_helper(1, 0);
    }

    private static void primitivePolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).primitivePolynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void primitivePolynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).primitivePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPrimitivePolynomialsAtLeast() {
        primitivePolynomialsAtLeast_helper(
                1,
                1,
                -1,
                "QBarRandomProvider_primitivePolynomialsAtLeast_i",
                1.3170959999927159,
                0.9483072777233684
        );
        primitivePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "QBarRandomProvider_primitivePolynomialsAtLeast_ii",
                1.3170959999927159,
                0.9483072777233684
        );
        primitivePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_primitivePolynomialsAtLeast_iii",
                4.135319999990011,
                5.005227717069355
        );
        primitivePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_primitivePolynomialsAtLeast_iv",
                4.135319999990011,
                5.005227717069355
        );
        primitivePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_primitivePolynomialsAtLeast_v",
                3.0893119999680687,
                5.1270032708742885
        );
        primitivePolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_primitivePolynomialsAtLeast_vi",
                9.45433400000822,
                10.00177486274624
        );
        primitivePolynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_primitivePolynomialsAtLeast_vii",
                8.22605800001186,
                10.031010968999196
        );
        primitivePolynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_primitivePolynomialsAtLeast_viii",
                8.002345000060796,
                10.03215340004299
        );
        primitivePolynomialsAtLeast_fail_helper(1, 0, -1);
        primitivePolynomialsAtLeast_fail_helper(0, 1, -1);
        primitivePolynomialsAtLeast_fail_helper(1, 0, 0);
        primitivePolynomialsAtLeast_fail_helper(0, 1, 0);
        primitivePolynomialsAtLeast_fail_helper(1, 3, 3);
        primitivePolynomialsAtLeast_fail_helper(1, 1, -2);
    }

    private static void positivePrimitivePolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).positivePrimitivePolynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void positivePrimitivePolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).positivePrimitivePolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositivePrimitivePolynomials_int() {
        positivePrimitivePolynomials_int_helper(
                1,
                0,
                "QBarRandomProvider_positivePrimitivePolynomials_int_i",
                0.0,
                1.000000000007918
        );
        positivePrimitivePolynomials_int_helper(
                1,
                1,
                "QBarRandomProvider_positivePrimitivePolynomials_int_ii",
                1.000000000007918,
                1.2789054999949967
        );
        positivePrimitivePolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_positivePrimitivePolynomials_int_iii",
                2.9999999999775233,
                5.177311750004235
        );
        positivePrimitivePolynomials_int_helper(
                10,
                8,
                "QBarRandomProvider_positivePrimitivePolynomials_int_iv",
                8.000000000063345,
                10.042622555550155
        );
        positivePrimitivePolynomials_int_fail_helper(0, 0);
        positivePrimitivePolynomials_int_fail_helper(1, -1);
    }

    private static void positivePrimitivePolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitivePolynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void positivePrimitivePolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitivePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositivePrimitivePolynomials() {
        positivePrimitivePolynomials_helper(
                1,
                1,
                "QBarRandomProvider_positivePrimitivePolynomials_i",
                1.319364999992647,
                1.1321792818114707
        );
        positivePrimitivePolynomials_helper(
                5,
                3,
                "QBarRandomProvider_positivePrimitivePolynomials_ii",
                4.136915999990028,
                5.037304094541455
        );
        positivePrimitivePolynomials_helper(
                10,
                8,
                "QBarRandomProvider_positivePrimitivePolynomials_iii",
                9.446762000009086,
                10.014196839384036
        );
        positivePrimitivePolynomials_fail_helper(0, 1);
        positivePrimitivePolynomials_fail_helper(1, 0);
    }

    private static void positivePrimitivePolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitivePolynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void positivePrimitivePolynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitivePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositivePrimitivePolynomialsAtLeast() {
        positivePrimitivePolynomialsAtLeast_helper(
                1,
                1,
                -1,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_i",
                1.319364999992647,
                1.1321792818114707
        );
        positivePrimitivePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_ii",
                1.319364999992647,
                1.1321792818114707
        );
        positivePrimitivePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_iii",
                4.136915999990028,
                5.037304094541455
        );
        positivePrimitivePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_iv",
                4.136915999990028,
                5.037304094541455
        );
        positivePrimitivePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_v",
                3.088038999968085,
                5.164304939233438
        );
        positivePrimitivePolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_vi",
                9.446762000009086,
                10.014196839384036
        );
        positivePrimitivePolynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_vii",
                8.223359000011806,
                10.039688685940462
        );
        positivePrimitivePolynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_positivePrimitivePolynomialsAtLeast_viii",
                8.001185000061044,
                10.047496746236824
        );
        positivePrimitivePolynomialsAtLeast_fail_helper(1, 0, -1);
        positivePrimitivePolynomialsAtLeast_fail_helper(0, 1, -1);
        positivePrimitivePolynomialsAtLeast_fail_helper(1, 0, 0);
        positivePrimitivePolynomialsAtLeast_fail_helper(0, 1, 0);
        positivePrimitivePolynomialsAtLeast_fail_helper(1, 3, 3);
        positivePrimitivePolynomialsAtLeast_fail_helper(1, 1, -2);
    }

    private static void squareFreePolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).squareFreePolynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void squareFreePolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).squareFreePolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquareFreePolynomials_int() {
        squareFreePolynomials_int_helper(
                1,
                0,
                "QBarRandomProvider_squareFreePolynomials_int_i",
                0.0,
                1.666559999999875
        );
        squareFreePolynomials_int_helper(
                1,
                1,
                "QBarRandomProvider_squareFreePolynomials_int_ii",
                0.9999999999980838,
                1.2471400000007853
        );
        squareFreePolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_squareFreePolynomials_int_iii",
                3.000000000005079,
                5.200314999992706
        );
        squareFreePolynomials_int_fail_helper(0, 0);
        squareFreePolynomials_int_fail_helper(1, -1);
    }

    private static void squareFreePolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).squareFreePolynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void squareFreePolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).squareFreePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquareFreePolynomials() {
        squareFreePolynomials_helper(
                1,
                0,
                "QBarRandomProvider_squareFreePolynomials_i",
                0.8634199999996628,
                1.3381041311144288
        );
        squareFreePolynomials_helper(
                5,
                3,
                "QBarRandomProvider_squareFreePolynomials_ii",
                3.9696700000014955,
                5.119983821854156
        );
        squareFreePolynomials_fail_helper(0, 0);
        squareFreePolynomials_fail_helper(1, -1);
    }

    private static void squareFreePolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).squareFreePolynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void squareFreePolynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).squareFreePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testSquareFreePolynomialsAtLeast() {
        squareFreePolynomialsAtLeast_helper(
                1,
                0,
                -1,
                "QBarRandomProvider_squareFreePolynomialsAtLeast_i",
                0.8634199999996628,
                1.3381041311144288
        );
        squareFreePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "QBarRandomProvider_squareFreePolynomialsAtLeast_ii",
                0.8642899999996785,
                1.3383593754208227
        );
        squareFreePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_squareFreePolynomialsAtLeast_iii",
                3.9696700000014955,
                5.119983821854156
        );
        squareFreePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_squareFreePolynomialsAtLeast_iv",
                2.9729500000011333,
                5.180837916413763
        );
        squareFreePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_squareFreePolynomialsAtLeast_v",
                3.006200000001613,
                5.203389745910559
        );
        squareFreePolynomialsAtLeast_fail_helper(1, -1, -1);
        squareFreePolynomialsAtLeast_fail_helper(0, 0, -1);
        squareFreePolynomialsAtLeast_fail_helper(1, -1, -1);
        squareFreePolynomialsAtLeast_fail_helper(0, 0, -1);
        squareFreePolynomialsAtLeast_fail_helper(1, 3, 3);
        squareFreePolynomialsAtLeast_fail_helper(1, 1, -2);
    }

    private static void positivePrimitiveSquareFreePolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).positivePrimitiveSquareFreePolynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void positivePrimitiveSquareFreePolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).positivePrimitiveSquareFreePolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositivePrimitiveSquareFreePolynomials_int() {
        positivePrimitiveSquareFreePolynomials_int_helper(
                1,
                0,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomials_int_i",
                0.0,
                0.9999999999980838
        );
        positivePrimitiveSquareFreePolynomials_int_helper(
                1,
                1,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomials_int_ii",
                0.9999999999980838,
                1.2849400000016107
        );
        positivePrimitiveSquareFreePolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomials_int_iii",
                3.000000000005079,
                5.233984999991867
        );
        positivePrimitiveSquareFreePolynomials_int_fail_helper(0, 0);
        positivePrimitiveSquareFreePolynomials_int_fail_helper(1, -1);
    }

    private static void positivePrimitiveSquareFreePolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitiveSquareFreePolynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void positivePrimitiveSquareFreePolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitiveSquareFreePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositivePrimitiveSquareFreePolynomials() {
        positivePrimitiveSquareFreePolynomials_helper(
                1,
                1,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomials_i",
                1.1869899999997364,
                1.1991778654697347
        );
        positivePrimitiveSquareFreePolynomials_helper(
                5,
                3,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomials_ii",
                4.111800000001742,
                5.070521147155589
        );
        positivePrimitiveSquareFreePolynomials_fail_helper(0, 0);
        positivePrimitiveSquareFreePolynomials_fail_helper(1, -1);
    }

    private static void positivePrimitiveSquareFreePolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale)
                        .positivePrimitiveSquareFreePolynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(
            int scale,
            int secondaryScale,
            int minDegree
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale)
                    .positivePrimitiveSquareFreePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositivePrimitiveSquareFreePolynomialsAtLeast() {
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                1,
                1,
                -1,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomialsAtLeast_i",
                1.1869899999997364,
                1.1991778654697347
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomialsAtLeast_ii",
                1.1869899999997364,
                1.1991778654697347
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomialsAtLeast_iii",
                4.111800000001742,
                5.070521147155589
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomialsAtLeast_iv",
                4.111800000001742,
                5.070521147155589
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_positivePrimitiveSquareFreePolynomialsAtLeast_v",
                3.0810300000016992,
                5.204828192899828
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(1, 0, -1);
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(0, 1, -1);
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(1, -1, -1);
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(0, 1, 1);
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(1, 3, 3);
        positivePrimitiveSquareFreePolynomialsAtLeast_fail_helper(1, 1, -2);
    }

    private static void irreduciblePolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).irreduciblePolynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void irreduciblePolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).irreduciblePolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testIrreduciblePolynomials_int() {
        irreduciblePolynomials_int_helper(
                1,
                0,
                "QBarRandomProvider_irreduciblePolynomials_int_i",
                0.0,
                0.9999999999980838
        );
        irreduciblePolynomials_int_helper(
                1,
                1,
                "QBarRandomProvider_irreduciblePolynomials_int_ii",
                0.9999999999980838,
                1.2849400000016107
        );
        irreduciblePolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_irreduciblePolynomials_int_iii",
                3.000000000005079,
                5.445429999991603
        );
        irreduciblePolynomials_int_fail_helper(0, 0);
        irreduciblePolynomials_int_fail_helper(1, -1);
    }

    private static void irreduciblePolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).irreduciblePolynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void irreduciblePolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).irreduciblePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testIrreduciblePolynomials() {
        irreduciblePolynomials_helper(
                2,
                2,
                "QBarRandomProvider_irreduciblePolynomials_i",
                1.0023799999996275,
                2.030174092832156
        );
        irreduciblePolynomials_helper(
                5,
                3,
                "QBarRandomProvider_irreduciblePolynomials_ii",
                2.4175200000008608,
                5.069922048738439
        );
        irreduciblePolynomials_fail_helper(1, 2);
        irreduciblePolynomials_fail_helper(2, 1);
    }

    private static void irreduciblePolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 100,
                P.withScale(scale).withSecondaryScale(secondaryScale).irreduciblePolynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void irreduciblePolynomialsAtLeast_fail_helper(
            int scale,
            int secondaryScale,
            int minDegree
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).irreduciblePolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testIrreduciblePolynomialsAtLeast() {
        irreduciblePolynomialsAtLeast_helper(
                2,
                2,
                -1,
                "QBarRandomProvider_irreduciblePolynomialsAtLeast_i",
                1.0019999999999425,
                2.0269230769228126
        );
        irreduciblePolynomialsAtLeast_helper(
                2,
                2,
                0,
                "QBarRandomProvider_irreduciblePolynomialsAtLeast_ii",
                1.0019999999999425,
                2.0269230769228126
        );
        irreduciblePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_irreduciblePolynomialsAtLeast_iii",
                2.4041999999999892,
                5.046090123964768
        );
        irreduciblePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_irreduciblePolynomialsAtLeast_iv",
                2.4041999999999892,
                5.046090123964768
        );
        irreduciblePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_irreduciblePolynomialsAtLeast_v",
                2.997899999999992,
                5.4128167287826034
        );
        irreduciblePolynomialsAtLeast_fail_helper(2, 1, -1);
        irreduciblePolynomialsAtLeast_fail_helper(1, 2, -1);
        irreduciblePolynomialsAtLeast_fail_helper(0, 2, 1);
        irreduciblePolynomialsAtLeast_fail_helper(2, 2, 2);
        irreduciblePolynomialsAtLeast_fail_helper(2, 2, -2);
        irreduciblePolynomialsAtLeast_fail_helper(5, 3, 3);
    }

    private static void rationalPolynomials_helper(
            @NotNull Iterable<RationalPolynomial> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        List<RationalPolynomial> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(RationalPolynomial::degree, sample))), meanDegree);
        aeq(meanOfIntegers(toList(concatMap(p -> map(Rational::bitLength, p), sample))), meanCoefficientBitSize);
        P.reset();
    }

    private static void rationalPolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).rationalPolynomials(degree),
                output,
                topSampleCount,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void rationalPolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).rationalPolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomials_int() {
        rationalPolynomials_int_helper(
                3,
                -1,
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...]",
                "{0=100000}",
                -0.9999999999980838,
                0.0
        );
        rationalPolynomials_int_helper(
                5,
                3,
                "[-x^3-29/4*x^2-x+1/36, -2*x^3+1/89*x^2-53/47*x+5/2, -2/683*x^3-22*x^2-4*x-5/6, -4*x^3-1/50*x," +
                " -1/4*x^3-1/16*x^2+2*x+12757/4452, -718/59937*x^3+3*x^2-6268*x-1, x^3-2/25*x^2-1/38*x+1," +
                " 17/24*x^3+14*x^2+2*x-5/6, x^3-x+1/7, -1/7*x^3+2*x^2+x+15/619, 14/3*x^3-29/19562*x-1/7," +
                " 1/6*x^3+1/4*x^2+10*x, -1/6*x^3+16*x^2+8*x-9/4, -2/5*x^3-2/11*x^2-24/47*x+11/2," +
                " -7/41*x^3+3*x^2+1/10*x-7/2, -22/3*x^3-x^2+1/2*x-3/11, -2/5*x^3-1/21*x^2+1/3*x-3623/3," +
                " 87*x^3+1/3*x^2+x+1/6, 468*x^3+2/5*x^2-1/2*x-1/13, 1/1790*x^3+1/6*x^2+45/7*x-26, ...]",
                "{x^3=35, -x^3=33, 2*x^3=18, 3*x^3=17, x^3-1=14, -x^3+x=14, x^3-x=14, x^3+1=13, -1/2*x^3=13," +
                " 1/2*x^3=13}",
                3.000000000005079,
                5.000859999992796
        );
        rationalPolynomials_int_helper(
                10,
                8,
                "[2/3*x^8-10*x^7-70/8797*x^6-67/5785*x^5-117/219224*x^4+1/25*x^3-2*x^2+2304798/125*x+21/13," +
                " -1/6268*x^8-8/5*x^7-2/35*x^6+1/47084144*x^4-239/978*x^3+2/227*x^2-4551*x+62," +
                " 122224/59*x^8+3/238*x^7-15/13*x^3-8/249*x^2-238/5*x-504064/13," +
                " -98*x^8+11*x^7+438*x^6+2/47*x^5+1/12*x^4+479*x^3-1130/15659*x^2-7/3839*x+19," +
                " 3*x^8-70/167*x^7+139/9*x^6-1/5*x^5-30*x^4+1/15*x^3-97/41*x^2+1/926*x+1/6," +
                " -7*x^8-7*x^7+23/3*x^6-49/5*x^5-1/5*x^4+2260*x^3+1/37*x^2+23/7*x+2/23," +
                " -26/1537*x^8+13/31*x^7-3*x^6+3954017/6454*x^5+1/344*x^4+x^2+9/13*x+49465/2," +
                " 5395/2*x^8+1/9*x^7+20*x^6-x^5-34/15*x^4-1/7*x^3+1/3*x^2+6312/23*x+13/5," +
                " 33*x^8+1/59*x^7+1/11*x^6-5656/31*x^5-3618*x^4+1/69*x^3-125/308*x^2-149/7*x+3," +
                " 237/832*x^8+19/11*x^7-1/4*x^6+111*x^5+3/112*x^4+246/48401*x^3-x^2+x-9/76," +
                " -1/3*x^8-5/567*x^7-12*x^6+5*x^5-2/4079803*x^4-58/111*x^3+3/5*x^2+14*x+1/4," +
                " -43124/2505*x^8+1/3*x^7-2/31*x^6-1/22*x^5-15/1706*x^4+1/519*x^3-20672/9*x^2+280/11*x+21," +
                " -1/2*x^8-3/11075*x^6+3/3361*x^5+2/11*x^4-4*x^3-3/2*x^2-x+3/2," +
                " -2588/93967625*x^8-1/46542*x^7-30725/11*x^6+1/2*x^4-1/40*x^3-383*x^2+1/78*x+53/9," +
                " 13*x^8-893924/13*x^7-9/2*x^6+37*x^5-40*x^4+1/63*x^3+186*x^2-78*x+1/454," +
                " -203/177*x^8-2*x^7+71/2*x^5-3239437/3*x^4+7/131*x^3+79/3*x^2+17*x-1," +
                " -1/6*x^8+1/6829*x^7+3/79*x^6-246*x^5+1/11*x^4-x^3-157/3*x^2-209/10*x+2/3," +
                " -2/483*x^8-9/37841*x^7+30413/72*x^6+11/4*x^5+3/38*x^4+4*x^3-24*x^2+6*x-4," +
                " -x^8-x^7+40*x^5+3/46*x^4-x^3-2/63*x^2+244/5*x-24/7," +
                " 1/5*x^8-1/18*x^7+x^6-5/6*x^5-740186/147*x^4-3/80*x^3+x-1/2, ...]",
                "{2/3*x^8-10*x^7-70/8797*x^6-67/5785*x^5-117/219224*x^4+1/25*x^3-2*x^2+2304798/125*x+21/13=1," +
                " -1/6268*x^8-8/5*x^7-2/35*x^6+1/47084144*x^4-239/978*x^3+2/227*x^2-4551*x+62=1," +
                " 122224/59*x^8+3/238*x^7-15/13*x^3-8/249*x^2-238/5*x-504064/13=1," +
                " -98*x^8+11*x^7+438*x^6+2/47*x^5+1/12*x^4+479*x^3-1130/15659*x^2-7/3839*x+19=1," +
                " 3*x^8-70/167*x^7+139/9*x^6-1/5*x^5-30*x^4+1/15*x^3-97/41*x^2+1/926*x+1/6=1," +
                " -7*x^8-7*x^7+23/3*x^6-49/5*x^5-1/5*x^4+2260*x^3+1/37*x^2+23/7*x+2/23=1," +
                " -26/1537*x^8+13/31*x^7-3*x^6+3954017/6454*x^5+1/344*x^4+x^2+9/13*x+49465/2=1," +
                " 5395/2*x^8+1/9*x^7+20*x^6-x^5-34/15*x^4-1/7*x^3+1/3*x^2+6312/23*x+13/5=1," +
                " 33*x^8+1/59*x^7+1/11*x^6-5656/31*x^5-3618*x^4+1/69*x^3-125/308*x^2-149/7*x+3=1," +
                " 237/832*x^8+19/11*x^7-1/4*x^6+111*x^5+3/112*x^4+246/48401*x^3-x^2+x-9/76=1}",
                7.99999999998467,
                9.892031111041902
        );
        rationalPolynomials_int_fail_helper(2, -1);
        rationalPolynomials_int_fail_helper(1, -2);
    }

    private static void rationalPolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomials(),
                output,
                topSampleCount,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void rationalPolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomials() {
        rationalPolynomials_helper(
                3,
                1,
                "[x^3-2/7*x^2+x+477, 1, 0, -x^7-x^5-14/3*x^2-x+1/3, -27/8*x^8+167/17*x^6-x^4+x^3+6, x^2-6," +
                " -2/7*x^9+4/21*x^8-5*x^7+10*x^4+x^2-x+1/3, 1, -10*x-3," +
                " -1/2*x^12-5*x^10+1/3*x^6-2*x^5-1/2*x^4-1/6*x^2-5/19*x-2/3, 3*x, 4*x, x-1/2, 0, 0, 0, -1, -3," +
                " -4*x^8-5*x^4-2/3*x^3-x, -1/5, ...]",
                "{0=44006, -1=2610, 1=2588, -1/2=691, 2=689, -1/3=672, 1/2=636, 3=635, 1/3=633, -3=629}",
                0.687210000000254,
                3.113726210728528
        );
        rationalPolynomials_helper(
                5,
                3,
                "[-29/4*x^3-1/10*x^2-20*x+10/3, -5/6*x^4-1/427*x^3-89/2*x^2+47/3*x-2/53, 0," +
                " 1/19*x^7-7/2*x^6-4*x^5-1/50*x^3-2/683*x-1/2, -1/367*x^3-24*x^2+9/2*x+4," +
                " 3*x^8-6268*x^7-x^6-1/4*x^5-1/16*x^4+2*x^3+12757/4452*x^2-10*x-6/17, 0, 2," +
                " -1/7*x^4-1/7*x^3+2*x^2-1/35*x+363, 0," +
                " -24/47*x^10+11/2*x^9-1/6*x^8+16*x^7+8*x^6-9/4*x^5+1/6*x^4+1/4*x^3+10*x^2+14/3, 0, 1, 0, -x+1/2," +
                " 1/2, 468*x^5+2/5*x^4+2/3*x^2-13/4*x+9, 7*x^2-1/45*x-1, 1/2*x+1, -7/9*x^5+2/7*x^4+1/48*x^3-1/7*x+3," +
                " ...]",
                "{0=22970, -1=1064, 1=1011, 3=354, 1/3=353, 1/2=345, -1/3=334, 2=330, -2=323, -3=322}",
                2.8709299999999094,
                4.96048494805455
        );
        rationalPolynomials_helper(
                10,
                8,
                "[1/6*x^34-98*x^33+11*x^32+438*x^31+2/47*x^30+1/12*x^29+479*x^28-1130/15659*x^27-7/3839*x^26+" +
                "19*x^25+122224/59*x^24+3/238*x^23-15/13*x^19-8/249*x^18-238/5*x^17-504064/13*x^16-1/6268*x^15-" +
                "8/5*x^14-2/35*x^13+1/47084144*x^11-239/978*x^10+2/227*x^9-4551*x^8+62*x^7+2/3*x^6-10*x^5-" +
                "70/8797*x^4-2/171*x^3+25/53*x^2-1/2*x-489/2, -26/97*x+4510/21649," +
                " -7*x^13-7*x^12+23/3*x^11-49/5*x^10-1/5*x^9+2260*x^8+1/37*x^7+23/7*x^6+2/23*x^5+3*x^4-70/167*x^3+" +
                "139/9*x^2-1/5*x-30, -15*x^7-26/1537*x^6+13/31*x^5-3*x^4+3954017/6454*x^3-x^2+29*x-115/11969," +
                " -4/329*x+3/38, -x^9-34/15*x^8-1/7*x^7+1/3*x^6+6312/23*x^5+13/5*x^4-1760*x^3+3*x^2+1/21*x+8/21," +
                " 1/69*x^6-125/308*x^5-149/7*x^4+3*x^3+5395/2*x^2+1/9*x+20, 47*x+1/3608," +
                " -9/76*x^3+33*x^2+1/59*x+1/1158, 3/112*x^2+246/48401*x-1, 14*x^4+1/4*x^3+237/832*x^2+19/11*x-1/4," +
                " -1/3*x^7-5/567*x^6-12*x^5+5*x^4-2/4079803*x^3-399/631*x^2-58759/58*x-3/4, 0," +
                " 1/519*x^3-20672/9*x^2+280/11*x+53," +
                " 1/2*x^17-1/40*x^16-383*x^15+1/78*x^14+53/9*x^13-1/2*x^12-3/11075*x^10+3/3361*x^9+2/11*x^8-4*x^7-" +
                "3/2*x^6-x^5+3/2*x^4-43124/2505*x^3+1/3*x^2-2/31*x-3/22, 0," +
                " 1/454*x^13-729/2*x^11-13/3473*x^10-5/14*x^9-3*x^8+5/7*x^7-1083*x^6-6299/2*x^5-x^4-" +
                "2588/93967625*x^3+6*x^2-11/28*x-1/1029, -2/101*x^4-1/2*x^3+63/8*x^2+2*x-1/186," +
                " -x^27+40*x^25+3/46*x^24-x^23-2/63*x^22+244/5*x^21-24/7*x^20-2/483*x^19-9/37841*x^18+30413/72*x^17+" +
                "11/4*x^16+3/38*x^15+4*x^14-24*x^13+6*x^12-4*x^11-1/6*x^10+1/6829*x^9+3/79*x^8-246*x^7+1/11*x^6-x^5-" +
                "157/3*x^4-209/10*x^3+2/3*x^2-203/177*x, 1/48, ...]",
                "{0=10701, 1=210, -1=190, 2=101, -1/3=99, -2=93, 1/2=90, 1/3=79, -1/2=74, -3=71}",
                7.88165000000161,
                9.88691740842155
        );
        rationalPolynomials_fail_helper(2, 0);
        rationalPolynomials_fail_helper(1, -1);
    }

    private static void rationalPolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void rationalPolynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalPolynomialsAtLeast() {
        rationalPolynomialsAtLeast_helper(
                3,
                0,
                -1,
                "[1/2*x^2+x+3/221, 0, -2/3*x+3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3/4*x, 0, 0, 0, 0, ...]",
                "{0=60965, -1=2775, 1=2768, -3=738, 3=729, 1/3=714, 1/2=697, -1/3=686, 2=677, -1/2=661}",
                -0.22369000000018416,
                3.272416946837637
        );
        rationalPolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[-29/4*x^3-1/10*x^2-20*x+10/3, -5/6*x^4-1/427*x^3-89/2*x^2+47/3*x-2/53, 0," +
                " 1/19*x^7-7/2*x^6-4*x^5-1/50*x^3-2/683*x-1/2, -1/367*x^3-24*x^2+9/2*x+4," +
                " 3*x^8-6268*x^7-x^6-1/4*x^5-1/16*x^4+2*x^3+12757/4452*x^2-10*x-6/17, 0, 2," +
                " -1/7*x^4-1/7*x^3+2*x^2-1/35*x+363, 0," +
                " -24/47*x^10+11/2*x^9-1/6*x^8+16*x^7+8*x^6-9/4*x^5+1/6*x^4+1/4*x^3+10*x^2+14/3, 0, 1, 0, -x+1/2," +
                " 1/2, 468*x^5+2/5*x^4+2/3*x^2-13/4*x+9, 7*x^2-1/45*x-1, 1/2*x+1, -7/9*x^5+2/7*x^4+1/48*x^3-1/7*x+3," +
                " ...]",
                "{0=22970, -1=1064, 1=1011, 3=354, 1/3=353, 1/2=345, -1/3=334, 2=330, -2=323, -3=322}",
                2.8709299999999094,
                4.96048494805455
        );
        rationalPolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[-53/47*x^6+5/2*x^5-x^4-29/4*x^3-1/10*x^2-20*x+10/3, -22*x^4-4*x^3-5/6*x^2-1/427*x-89/2," +
                " -4*x^4-1/50*x^2-2/683, 2/3*x-5/7, 2*x^7+12757/4452*x^6+17/10*x^4-1/367*x^3-1/6*x^2-1/2," +
                " -1/4*x-1/4, 5, -5/6*x^5+x^4-2/25*x^3-1/38*x^2+x-3/718, 1/7*x^2+17/24*x-1/14, -5/4," +
                " -29/19562*x^9-1/7*x^8-1/7*x^7+2*x^6+x^5+15/619*x^4+x^3+6, 1/22, 1/4*x^2+10*x," +
                " 11/2*x^3-1/6*x^2+16*x+8, 41, -1, 1/2*x^5-3/11*x^4+1/3*x-33, 1/3*x^2+1/38, x^2+1/6*x-2/5," +
                " -13/4*x+7/87, ...]",
                "{1=1704, -1=1616, -2=584, 1/3=578, 3=575, -1/3=570, 2=565, -3=554, 1/2=553, -1/2=517}",
                3.0100400000011516,
                5.000735653514337
        );
        rationalPolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[5/2*x^5-x^4-29/4*x^3-x^2+1/36*x+1/13, -2*x^2+1/89*x-21/47, -2/683*x^2-22*x-4, 1/19*x^2-7/2*x-2," +
                " 2*x^7+12757/4452*x^6+17/10*x^4-1/367*x^3-24*x^2+9/2*x+4, -6268*x^3-x^2-1/4*x-1/4," +
                " 14*x^7+2*x^6-5/6*x^5+x^4-2/25*x^3-1/38*x^2+x-3/718, -3/13*x^2-8*x+6/17, -1/35*x^2+363*x-1," +
                " 1/6*x^3+1/4*x^2+10*x, 16*x^2+8*x-5/4, -2/11*x^2-1/182*x, 1/10*x^3-7/2*x^2-2/5*x-2/11," +
                " -197/15*x^2-7/41*x+1, 1/2*x^2-3/11*x-3/2, -2/5*x^4-1/21*x^3+1/3*x^2+1/38," +
                " -1/13*x^3+87*x^2+1/3*x+1, 468*x^2+2/5*x-1/2, 1/6*x^2+45/7*x-10, 3*x^3-1/115*x^2+1/2*x, ...]",
                "{x^2=113, -x^2=89, -2*x^2=43, x^2+x=43, 1/2*x^2=39, x^2-x=36, -1/2*x^2=34, 3*x^2=34, 1/3*x^2=34," +
                " -x^2-1=34}",
                2.9997700000015786,
                5.00013000747141
        );
        rationalPolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "[1/6*x^34-98*x^33+11*x^32+438*x^31+2/47*x^30+1/12*x^29+479*x^28-1130/15659*x^27-7/3839*x^26+" +
                "19*x^25+122224/59*x^24+3/238*x^23-15/13*x^19-8/249*x^18-238/5*x^17-504064/13*x^16-1/6268*x^15-" +
                "8/5*x^14-2/35*x^13+1/47084144*x^11-239/978*x^10+2/227*x^9-4551*x^8+62*x^7+2/3*x^6-10*x^5-" +
                "70/8797*x^4-2/171*x^3+25/53*x^2-1/2*x-489/2, -26/97*x+4510/21649," +
                " -7*x^13-7*x^12+23/3*x^11-49/5*x^10-1/5*x^9+2260*x^8+1/37*x^7+23/7*x^6+2/23*x^5+3*x^4-70/167*x^3+" +
                "139/9*x^2-1/5*x-30, -15*x^7-26/1537*x^6+13/31*x^5-3*x^4+3954017/6454*x^3-x^2+29*x-115/11969," +
                " -4/329*x+3/38, -x^9-34/15*x^8-1/7*x^7+1/3*x^6+6312/23*x^5+13/5*x^4-1760*x^3+3*x^2+1/21*x+8/21," +
                " 1/69*x^6-125/308*x^5-149/7*x^4+3*x^3+5395/2*x^2+1/9*x+20, 47*x+1/3608," +
                " -9/76*x^3+33*x^2+1/59*x+1/1158, 3/112*x^2+246/48401*x-1, 14*x^4+1/4*x^3+237/832*x^2+19/11*x-1/4," +
                " -1/3*x^7-5/567*x^6-12*x^5+5*x^4-2/4079803*x^3-399/631*x^2-58759/58*x-3/4, 0," +
                " 1/519*x^3-20672/9*x^2+280/11*x+53," +
                " 1/2*x^17-1/40*x^16-383*x^15+1/78*x^14+53/9*x^13-1/2*x^12-3/11075*x^10+3/3361*x^9+2/11*x^8-4*x^7-" +
                "3/2*x^6-x^5+3/2*x^4-43124/2505*x^3+1/3*x^2-2/31*x-3/22, 0," +
                " 1/454*x^13-729/2*x^11-13/3473*x^10-5/14*x^9-3*x^8+5/7*x^7-1083*x^6-6299/2*x^5-x^4-" +
                "2588/93967625*x^3+6*x^2-11/28*x-1/1029, -2/101*x^4-1/2*x^3+63/8*x^2+2*x-1/186," +
                " -x^27+40*x^25+3/46*x^24-x^23-2/63*x^22+244/5*x^21-24/7*x^20-2/483*x^19-9/37841*x^18+30413/72*x^17+" +
                "11/4*x^16+3/38*x^15+4*x^14-24*x^13+6*x^12-4*x^11-1/6*x^10+1/6829*x^9+3/79*x^8-246*x^7+1/11*x^6-" +
                "x^5-157/3*x^4-209/10*x^3+2/3*x^2-203/177*x, 1/48, ...]",
                "{0=10701, 1=210, -1=190, 2=101, -1/3=99, -2=93, 1/2=90, 1/3=79, -1/2=74, -3=71}",
                7.88165000000161,
                9.88691740842155
        );
        rationalPolynomialsAtLeast_helper(
                10,
                8,
                2,
                "[2/3*x^7-10*x^6-70/8797*x^5-2/171*x^4+25/53*x^3-1/2*x^2-233/2*x+85/76574," +
                " -1/6268*x^9-8/5*x^8-2/35*x^7+1/47084144*x^5-239/978*x^4-92/15*x^3+9/2*x^2+2*x+1/967," +
                " -7/3839*x^9+19*x^8+122224/59*x^7+3/238*x^6-15/13*x^2-8/249*x-238/5," +
                " 2/23*x^16+3*x^15-70/167*x^14+139/9*x^13-1/5*x^12-30*x^11+1/15*x^10-97/41*x^9+1/926*x^8+1/6*x^7-" +
                "98*x^6+11*x^5+438*x^4+2/47*x^3+1/12*x^2+479*x-43798/3," +
                " 23/3*x^7+766/17*x^6+29/730*x^5+2*x^4-1/10*x^3-42*x^2-37/2*x+15," +
                " 3954017/6454*x^5-x^4+29*x^3-115/11969*x^2+1/78*x+1/11, -15*x^4-26/1537*x^3-11/9*x^2+47*x+97/3," +
                " 5/4*x^3+13/53*x^2-1/8*x-841, -34/15*x^7-1/7*x^6+1/3*x^5+6312/23*x^4+13/5*x^3-1760*x^2+3*x+2/21," +
                " 1/9*x^3-1/6*x^2+1498/115*x-1/58," +
                " 237/832*x^18+19/11*x^17-1/4*x^16+111*x^15+3/112*x^14+246/48401*x^13-x^12+x^11-9/76*x^10+33*x^9+" +
                "1/59*x^8+1/11*x^7-5656/31*x^6-3618*x^5+1/69*x^4-125/308*x^3-3/2*x^2+3/67*x-8/3," +
                " -2/4079803*x^5-399/631*x^4-58759/58*x^3-3/4*x^2+3*x-41/118," +
                " -x^15+3/2*x^14-43124/2505*x^13+1/3*x^12-2/31*x^11-1/22*x^10-15/1706*x^9+1/519*x^8-20672/9*x^7+" +
                "280/11*x^6+21*x^5-1/3*x^4-394/13*x^3-20/41*x^2-1/350*x+1/12," +
                " -383*x^9+1/78*x^8+53/9*x^7-1/2*x^6-3/11075*x^4+3/3361*x^3+2/11*x^2-4*x-3/2," +
                " -1/46542*x^3-30725/11*x^2+1/2, -5/14*x^6-3*x^5+5/7*x^4-1083*x^3-6299/2*x^2-x-2588/93967625," +
                " 186*x^8-78*x^7+1/454*x^6-58/3*x^5-1/24*x^4+21/473*x^3-5*x^2-492971/371*x-17809/31," +
                " 1/5*x^3-2/101*x^2-1/2*x+127/8, 79/3*x^4+17*x^3-x^2+13*x-1942500/13," +
                " -209/10*x^6+2/3*x^5-203/177*x^4-2*x^3+71/2*x-387/93709, ...]",
                "{3*x^2=2, 2/3*x^7-10*x^6-70/8797*x^5-2/171*x^4+25/53*x^3-1/2*x^2-233/2*x+85/76574=1," +
                " -1/6268*x^9-8/5*x^8-2/35*x^7+1/47084144*x^5-239/978*x^4-92/15*x^3+9/2*x^2+2*x+1/967=1," +
                " -7/3839*x^9+19*x^8+122224/59*x^7+3/238*x^6-15/13*x^2-8/249*x-238/5=1," +
                " 2/23*x^16+3*x^15-70/167*x^14+139/9*x^13-1/5*x^12-30*x^11+1/15*x^10-97/41*x^9+1/926*x^8+1/6*x^7-" +
                "98*x^6+11*x^5+438*x^4+2/47*x^3+1/12*x^2+479*x-43798/3=1," +
                " 23/3*x^7+766/17*x^6+29/730*x^5+2*x^4-1/10*x^3-42*x^2-37/2*x+15=1," +
                " 3954017/6454*x^5-x^4+29*x^3-115/11969*x^2+1/78*x+1/11=1, -15*x^4-26/1537*x^3-11/9*x^2+47*x+97/3=1," +
                " 5/4*x^3+13/53*x^2-1/8*x-841=1, -34/15*x^7-1/7*x^6+1/3*x^5+6312/23*x^4+13/5*x^3-1760*x^2+3*x+2/21=1}",
                7.982850000001959,
                9.893852173886888
        );
        rationalPolynomialsAtLeast_helper(
                10,
                8,
                7,
                "[-4551*x^10+62*x^9+2/3*x^8-10*x^7-70/8797*x^6-67/5785*x^5-117/219224*x^4+1/25*x^3-2*x^2+" +
                "2304798/125*x+1/13, 1/12*x^7+479*x^6-1130/15659*x^5-7/3839*x^4+19*x^3+122224/59*x^2+3/238*x-1/3," +
                " -30*x^8+1/15*x^7-97/41*x^6+1/926*x^5+1/6*x^4-98*x^3+11*x^2+438*x+2/47," +
                " 1/37*x^7+23/7*x^6+2/23*x^5+3*x^4-70/167*x^3+139/9*x^2-1/5*x-1/12," +
                " 49465/2*x^7-7*x^6-7*x^5+23/3*x^4-49/5*x^3-1/5*x^2+2260*x-1/106," +
                " -26/1537*x^7+13/31*x^6-3*x^5+3954017/6454*x^4+1/344*x^3+x+9/13," +
                " -1760*x^8+3*x^7+1/21*x^6+8/21*x^5+16/21*x^4-x^3-4/329*x^2+3/38*x-2/15," +
                " 1/69*x^11-125/308*x^10-149/7*x^9+3*x^8+5395/2*x^7+1/9*x^6+20*x^5-x^4-34/15*x^3-1/7*x^2+1/3*x+" +
                "6312/23, 246/48401*x^9-x^8+x^7-9/76*x^6+33*x^5+1/59*x^4+1/11*x^3-5656/31*x^2-3618*x-3/8528," +
                " 3/5*x^7+14*x^6+1/4*x^5+237/832*x^4+19/11*x^3-1/4*x^2+111*x+3/112," +
                " 21*x^7-1/3*x^6-5/567*x^5-12*x^4+5*x^3-2/4079803*x^2-58/111*x+4/6117," +
                " 3/2*x^8-43124/2505*x^7+1/3*x^6-2/31*x^5-1/22*x^4-15/1706*x^3+1/519*x^2-20672/9*x+152/11," +
                " 53/9*x^8-1/2*x^7-11075/3*x^6+7457/3*x^5+134/3*x^4-5/14*x^3+19/7*x^2-2/3*x-1/40," +
                " -1/46542*x^7-30725/11*x^6+1/2*x^4-1/40*x^3-383*x^2+1/78*x-51/55," +
                " 17*x^8-3*x^7+2/15779*x^6+1/5*x^5-2/101*x^4-1/2*x^3+63/8*x^2+2*x-1/186," +
                " 2/3*x^7-203/177*x^6-2*x^5+71/2*x^3-3239437/3*x^2+7/131*x+47/3," +
                " -1/6*x^7+1/6829*x^6+3/79*x^5-246*x^4+1/11*x^3-x^2-157/3*x-209/10," +
                " -2/63*x^10+244/5*x^9-24/7*x^8-2/483*x^7-9/37841*x^6+30413/72*x^5+11/4*x^4+3/38*x^3+4*x^2-24*x+6," +
                " -3/80*x^9+x^7-1/2*x^6-x^5-x^4+40*x^2+3/46*x-1," +
                " 2*x^7+1/26*x^6-5/8*x^5+1/5*x^4-1/18*x^3+x^2-5/6*x-740186/147, ...]",
                "{-4551*x^10+62*x^9+2/3*x^8-10*x^7-70/8797*x^6-67/5785*x^5-117/219224*x^4+1/25*x^3-2*x^2+" +
                "2304798/125*x+1/13=1," +
                " 1/12*x^7+479*x^6-1130/15659*x^5-7/3839*x^4+19*x^3+122224/59*x^2+3/238*x-1/3=1," +
                " -30*x^8+1/15*x^7-97/41*x^6+1/926*x^5+1/6*x^4-98*x^3+11*x^2+438*x+2/47=1," +
                " 1/37*x^7+23/7*x^6+2/23*x^5+3*x^4-70/167*x^3+139/9*x^2-1/5*x-1/12=1," +
                " 49465/2*x^7-7*x^6-7*x^5+23/3*x^4-49/5*x^3-1/5*x^2+2260*x-1/106=1," +
                " -26/1537*x^7+13/31*x^6-3*x^5+3954017/6454*x^4+1/344*x^3+x+9/13=1," +
                " -1760*x^8+3*x^7+1/21*x^6+8/21*x^5+16/21*x^4-x^3-4/329*x^2+3/38*x-2/15=1," +
                " 1/69*x^11-125/308*x^10-149/7*x^9+3*x^8+5395/2*x^7+1/9*x^6+20*x^5-x^4-34/15*x^3-1/7*x^2+1/3*x+" +
                "6312/23=1, 246/48401*x^9-x^8+x^7-9/76*x^6+33*x^5+1/59*x^4+1/11*x^3-5656/31*x^2-3618*x-3/8528=1," +
                " 3/5*x^7+14*x^6+1/4*x^5+237/832*x^4+19/11*x^3-1/4*x^2+111*x+3/112=1}",
                8.000649999998087,
                9.892801075463433
        );
        rationalPolynomialsAtLeast_fail_helper(2, 0, -1);
        rationalPolynomialsAtLeast_fail_helper(3, -1, -1);
        rationalPolynomialsAtLeast_fail_helper(3, 3, 3);
        rationalPolynomialsAtLeast_fail_helper(3, 1, -2);
    }

    private static void monicRationalPolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).monicRationalPolynomials(degree),
                output,
                topSampleCount,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void monicRationalPolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).monicRationalPolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonicRationalPolynomials_int() {
        monicRationalPolynomials_int_helper(
                1,
                0,
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]",
                "{1=100000}",
                0.0,
                1.9999999999961675
        );
        monicRationalPolynomials_int_helper(
                1,
                1,
                "[x-2/3, x, x+1, x-1/19, x-1, x, x, x+1/115, x-9, x-11/4, x+4, x-38, x, x+1/6, x-77/13, x, x, x-2," +
                " x-5/11, x-1, ...]",
                "{x=35824, x-1=8888, x+1=8884, x-3=2279, x+3=2269, x+2=2250, x-1/2=2234, x+1/2=2229, x-1/3=2209," +
                " x-2=2199}",
                0.9999999999980838,
                2.3964350000032097
        );
        monicRationalPolynomials_int_helper(
                5,
                3,
                "[x^3+1/9*x^2-1/9, x^3-23/15*x^2-5663/10*x-19/15, x^3-4551*x^2+62, x^3-45325468*x^2+1/4*x-1513/2," +
                " x^3-122/5*x^2-394/5*x-238/5, x^3-224*x^2+62*x-1, x^3+x^2-x-1, x^3+91/11*x^2+122224/11*x+111/11," +
                " x^3+114/223*x^2-1/223*x-15659/223, x^3-23/219*x^2-1/438*x+47/438, x^3-10*x^2-7, x^3-1/4*x^2," +
                " x^3+27*x^2-11815*x-38, x^3+3/2*x-249, x^3+9/23*x^2-17/23*x+766/23, x^3-51/20161*x^2-78/20161*x," +
                " x^3+1/172*x^2-1/344, x^3+5/31*x^2+33/31*x+37/31, x^3-117/5*x^2+13/5*x+8/5, x^3-3/13*x^2+3/13, ...]",
                "{x^3=95, x^3-x=42, x^3+1=41, x^3-x^2=40, x^3-1=34, x^3+x^2=33, x^3+x=32, x^3+1/2*x^2=23, x^3+2=20," +
                " x^3-x^2-1=20}",
                3.000000000005079,
                7.7314374999814275
        );
        monicRationalPolynomials_int_helper(
                10,
                8,
                "[x^8+136/9*x^7+14/81*x^6-1/27*x^5+1/81*x^4-2/81*x^3+1/81*x^2+56/81*x+566/81," +
                " x^8-739/101342*x^7-55530517/50671*x^5-1/50671*x^4-253/50671*x^3+87/101342*x^2-15/50671*x+" +
                "47/101342," +
                " x^8+1/5*x^7-394953281/15*x^6+8772546*x^5+302074/3*x^4-17/15*x^3+16276/15*x^2-2894/5*x+" +
                "345112274/15," +
                " x^8-2693627/4432651024*x^7+152929/13297953072*x^6+491/1662244134*x^5-1/13297953072*x^3+" +
                "1673/6648976536*x^2-1/6648976536*x+3064121/13297953072," +
                " x^8+184204/266253*x^7+241/122886*x^6+1/1597518*x^5-1/532506*x^4-1/532506*x^2-1/1597518*x-" +
                "41349439/1597518," +
                " x^8-47175570438*x^7-11/7*x^6+1524767947173/7*x^5-x^3-1/7*x^2+31/7*x-2304971396104/7," +
                " x^8+397143/112*x^7+25/56*x^6-1/56*x^5-27/112*x^4-115761/56*x^3+1/112*x^2+3/56*x+17/112," +
                " x^8+32022602/7*x^7+698*x^6+60/7*x^4+3/7*x^3+629/7*x^2+114/7*x-23/7," +
                " x^8-158/9*x^7-4663/36*x^6-17/12*x^5-304875/4*x^4-145/12*x^3+9941/18*x^2-13369365953/36*x+1/18," +
                " x^8-51/398077*x^7-3/398077*x^6+973/398077*x^5-5/398077*x^4-82699/398077*x^3-" +
                "202206016023/398077*x^2+1/398077*x+262/398077," +
                " x^8-29255*x^6-1/5*x^5-31*x^4-19*x^3-1/5*x^2-6158262286914/5*x+486/5," +
                " x^8+5994968441128/203*x^6-47/29*x^5+6/203*x^4-15/29*x^3-6/203*x^2-12/203*x-2/29," +
                " x^8+209/2*x^6-23971/2*x^5+451/2*x^4+13/2*x^3-39797650871*x^2-100013067105*x-43," +
                " x^8-195/782*x^7-54/1955*x^5-404/391*x^4-7/3910*x^3+1/3910*x^2-3/1955*x+42/1955," +
                " x^8-3/58*x^7-39/58*x^6+26/29*x^5+3/29*x^4+527/58*x^3-2733/58*x^2-2025207833/29*x," +
                " x^8+1/199*x^7+2/2985*x^6+1/2985*x^5-30547/995*x^4-2/199*x^2-7/2985*x-4/2985," +
                " x^8-3/2804*x^7-1538/701*x^6-1423414505/2804*x^5+37841/2804*x^4-37/701*x^3-6/701*x^2+717/2804*x+" +
                "1/1402, x^8-1/430*x^7+4633/215*x^6+3/860*x^5+7/430*x^4-37/215*x^3+254284349/860*x^2+3/430*x+3/86," +
                " x^8-7/79211053*x^7+5/79211053*x^6+1/158422106*x^5-13/316844212*x^4-23384504/79211053*x^3-" +
                "467/316844212*x^2+7251/158422106, x^8-1/5*x^7+308602588/5*x^6+1/5*x^3-1/5*x-54/5, ...]",
                "{x^8+136/9*x^7+14/81*x^6-1/27*x^5+1/81*x^4-2/81*x^3+1/81*x^2+56/81*x+566/81=1," +
                " x^8-739/101342*x^7-55530517/50671*x^5-1/50671*x^4-253/50671*x^3+87/101342*x^2-15/50671*x+" +
                "47/101342=1," +
                " x^8+1/5*x^7-394953281/15*x^6+8772546*x^5+302074/3*x^4-17/15*x^3+16276/15*x^2-2894/5*x+" +
                "345112274/15=1," +
                " x^8-2693627/4432651024*x^7+152929/13297953072*x^6+491/1662244134*x^5-1/13297953072*x^3+" +
                "1673/6648976536*x^2-1/6648976536*x+3064121/13297953072=1," +
                " x^8+184204/266253*x^7+241/122886*x^6+1/1597518*x^5-1/532506*x^4-1/532506*x^2-1/1597518*x-" +
                "41349439/1597518=1," +
                " x^8-47175570438*x^7-11/7*x^6+1524767947173/7*x^5-x^3-1/7*x^2+31/7*x-2304971396104/7=1," +
                " x^8+397143/112*x^7+25/56*x^6-1/56*x^5-27/112*x^4-115761/56*x^3+1/112*x^2+3/56*x+17/112=1," +
                " x^8+32022602/7*x^7+698*x^6+60/7*x^4+3/7*x^3+629/7*x^2+114/7*x-23/7=1," +
                " x^8-158/9*x^7-4663/36*x^6-17/12*x^5-304875/4*x^4-145/12*x^3+9941/18*x^2-13369365953/36*x+1/18=1," +
                " x^8-51/398077*x^7-3/398077*x^6+973/398077*x^5-5/398077*x^4-82699/398077*x^3-" +
                "202206016023/398077*x^2+1/398077*x+262/398077=1}",
                7.99999999998467,
                17.083579999891224
        );
        monicRationalPolynomials_int_fail_helper(0, 0);
        monicRationalPolynomials_int_fail_helper(1, -1);
    }

    private static void monicRationalPolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).monicRationalPolynomials(),
                output,
                topSampleCount,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void monicRationalPolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).monicRationalPolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonicRationalPolynomials() {
        monicRationalPolynomials_helper(
                1,
                1,
                "[x^3+85*x+3, x-2, 1, x^2+x-10, x+1/7, 1, 1, x^4+x^3-11/4*x^2-1/4, 1, 1, x^2+x-1, 1," +
                " x^2+4/13*x-1/13, x^2-2/7, x^3-2*x^2-x, x^3-3/11*x^2+1/11*x-2/11, 1, 1, 1, 1, ...]",
                "{1=38761, x=9737, x+1=2467, x^2=2455, x-1=2386, x^3=657, x+1/2=628, x^2-x=627, x^2-1=625, x^2+1=625}",
                1.3250399999998215,
                2.3070570828933272
        );
        monicRationalPolynomials_helper(
                5,
                3,
                "[x^6+1/9*x^5-1/9*x^3-233/9*x^2+1256222/9*x+85/9, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " x^20-1/62*x^19+5/62*x^18-61/31*x^17-197/31*x^16-119/31*x^15-13/62*x^14-120960/31*x^13+" +
                "3909/31*x^12+41/62*x^11-32290/31*x^10+5/62*x^8-2/31*x^7-19/62*x^6-1/31*x^5-1/62*x^4+1/31*x^3-" +
                "2226/31, x^3-4/633*x^2+1/633*x-13/633, x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, x-242/3," +
                " x^2+15/7*x-23/7, x^7-1/2*x^5+13/2*x^3+9/2*x^2-x+115001/2, x^5+8/13*x^4-1/13*x^3-329/13*x," +
                " x^8-3/13*x^7+3/13*x^5-21/13*x^4-x^2+8/13*x+9/13, x^4-2*x^3+3347*x^2+9*x+3," +
                " x^4+6/5209*x^3-1/5209*x^2-3/5209*x-149/5209, x^4-4045/2*x^3-154/3*x^2-61/6*x+1/2," +
                " x^8-19/18566*x^6+31/18566*x^4-2828/9283*x^3-1809/9283*x-168/9283, x^4-59/33*x^3-4514/11*x+106/3," +
                " x^4+x^3-48401*x+10, x^5+86/3*x^4+4843/3*x^3-1/3*x^2+37*x+4/3," +
                " x^5-832/3*x^4+79*x^3+7/3*x^2+19/3*x-46834, ...]",
                "{1=6028, x=809, x-1=346, x+1=299, x-1/3=136, x-1/2=136, x+2=131, x+3=131, x+1/3=127, x+1/2=125}",
                4.141790000001693,
                8.108606146870954
        );
        monicRationalPolynomials_helper(
                10,
                8,
                "[x^31-5510881/2764346*x^30-2/4146519*x^29-19/8293038*x^28-8/4146519*x^27+9/1382173*x^26-" +
                "1202/4146519*x^24+1965582442977535455/2764346*x^23-239/8293038*x^22-1/8293038*x^21+1/4146519*x^20-" +
                "1/2764346*x^19-116/4146519*x^18+1/4146519*x^17+27/2764346*x^16+204/1382173*x^15+7/4146519*x^14-" +
                "1/2764346*x^13+1/8293038*x^12-1/4146519*x^11+1/8293038*x^10+28/4146519*x^9+283/4146519*x^8-" +
                "437219/8293038*x^7-33182/4146519*x^6-62/4146519*x^5-2313382/4146519*x^4-645698713/8293038*x^3-" +
                "3/212642*x^2+110/4146519*x-2129/8293038," +
                " x^16-2693627/4432651024*x^15+152929/13297953072*x^14+491/1662244134*x^13-1/13297953072*x^11+" +
                "1673/6648976536*x^10-1/6648976536*x^9+3064121/13297953072*x^8-835/554081378*x^7-" +
                "1005/4432651024*x^5+5/13297953072*x^4-3/4432651024*x^3+640780313507/1108162756*x^2-9/277040689*x," +
                " x^18+2/10025*x^17-434063/10025*x^16+1/2005*x^15+1/10025*x^14-736/10025*x^13+7182/10025*x^12-" +
                "7/10025*x^11+3/10025*x^10-248/10025*x^9-13/10025*x^8+1597518/10025*x^7+1105224/10025*x^6+" +
                "3133/10025*x^5+1/10025*x^4-3/10025*x^3-3/10025*x-4/10025," +
                " x^12-2304971396104/31*x^11-355615/31*x^10-3640006/31*x^9-3421/31*x^8+25689/31*x^7+524/31*x^6-" +
                "789/31*x^5+3/31*x^4+663885/31*x^3+9/31*x^2-21118/31*x-14/31," +
                " x^6-127/7*x^5-5/7*x^4-1684/7*x^3+36/7*x^2-632/7*x-4177, x+54/36587," +
                " x^12+262*x^11-2312*x^10+24*x^9+65*x^8-1060*x^7-40*x^6+22*x^3-117129*x^2-15528*x-130," +
                " x^9-1/11*x^8+1/33*x^7+995620/33*x^6-3117942350/33*x^5-1/11*x^4+23/33*x^3-7/33*x^2-52735763/11*x+" +
                "5473/33," +
                " x^10+5*x^9+1402/3*x^8-1/2*x^7-3076/3*x^6-1423414505/6*x^5+37841/6*x^4-74/3*x^3-4*x^2+239/2*x+2/3," +
                " x^8-7665/811*x^7+2851/1622*x^6+3/3244*x^5+1/3244*x^4+17503/1622*x^3+26/811*x^2-17/3244*x-" +
                "19286905/1622," +
                " x^12-3578/438297*x^11-9859388/438297*x^10-22/146099*x^9-14/438297*x^8-317/438297*x^7-" +
                "109/438297*x^6+11/438297*x^5+134274687199375/146099*x^3-208090/438297*x^2+4/146099*x+12/146099," +
                " x^5+1/308602588*x^2-1/154301294, x^4+3*x^3+4*x^2+128423*x+91," +
                " x^8-40/530566274871838627*x^7-1522584726/530566274871838627*x^6+8978/530566274871838627*x^5-" +
                "42/530566274871838627*x^4-2/530566274871838627*x^2+2541395/530566274871838627*x," +
                " x^43+940665/10141*x^41-192/10141*x^40+61/10141*x^38-11/10141*x^37-15687/10141*x^36+1/10141*x^35+" +
                "1782/10141*x^34+5379/10141*x^33-3/10141*x^32-1066/10141*x^30+319661334/10141*x^29-10/10141*x^28+" +
                "901/10141*x^27-205195/10141*x^26+21920/10141*x^25+3930/10141*x^24+43/10141*x^23+" +
                "1322424107679/10141*x^22+83481365755/10141*x^21+3/10141*x^20-655648/10141*x^18-532936/10141*x^17-" +
                "9532/10141*x^16-1/10141*x^15-2471048/10141*x^14+2657104/10141*x^13+13/10141*x^12-1/10141*x^11+" +
                "249/10141*x^10-154687192/10141*x^9+4/10141*x^8+3/10141*x^6+45331/10141*x^5-9647/10141*x^4-" +
                "77774/10141*x^3+10/10141*x^2-2/10141, x^5-7/4*x^4-226081757405/4*x^3-3/4*x^2-1778987/4*x-397/4," +
                " x^17+5/36056*x^16-135597/36056*x^15-3/18028*x^14+831/18028*x^13-1/9014*x^12-5201/9014*x^11+" +
                "1618873/18028*x^10+9/18028*x^9+43/18028*x^8-867/36056*x^7-179/36056*x^6-1/36056*x^5-87/36056*x^4-" +
                "25/18028*x^2+7/36056*x+3247609/9014, x^5-43/819*x^4+84904/63*x^3+829289/819*x^2-55/819*x+6/91," +
                " x^35+19/161*x^34+10533/7*x^33-3/161*x^31-495015/161*x^30-2/161*x^29+405597/161*x^28+661/161*x^27+" +
                "30/23*x^26-6/161*x^25+31/161*x^23+6/23*x^22+30/161*x^21+12/161*x^20+6/161*x^18+51/161*x^17-" +
                "6/161*x^16-538014882259/161*x^15+1969/161*x^14-1/161*x^13+8/161*x^12+3554/161*x^11-150263/161*x^10+" +
                "168244/161*x^8-122221910/161*x^7-29/161*x^6+53/161*x^5-31/161*x^4-246/161*x^3+1/161*x^2-6/161*x+" +
                "6729/161," +
                " x^10+262/47*x^9-9/47*x^8-76/47*x^6-58416/47*x^5-56/47*x^4-37/47*x^3-3/47*x^2+2/47*x-1531/47, ...]",
                "{1=1210, x=106, x+1=47, x-1=36, x-3=27, x+3=25, x-2=23, x+1/2=21, x-1/2=19, x+2=17}",
                9.458719999998882,
                17.390248519873563
        );
        monicRationalPolynomials_fail_helper(0, 1);
        monicRationalPolynomials_fail_helper(1, 0);
    }

    private static void monicRationalPolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).monicRationalPolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void monicRationalPolynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).monicRationalPolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonicRationalPolynomialsAtLeast() {
        monicRationalPolynomialsAtLeast_helper(
                1,
                1,
                -1,
                "[x^3+85*x+3, x-2, 1, x^2+x-10, x+1/7, 1, 1, x^4+x^3-11/4*x^2-1/4, 1, 1, x^2+x-1, 1," +
                " x^2+4/13*x-1/13, x^2-2/7, x^3-2*x^2-x, x^3-3/11*x^2+1/11*x-2/11, 1, 1, 1, 1, ...]",
                "{1=38761, x=9737, x+1=2467, x^2=2455, x-1=2386, x^3=657, x+1/2=628, x^2-x=627, x^2-1=625, x^2+1=625}",
                1.3250399999998215,
                2.3070570828933272
        );
        monicRationalPolynomialsAtLeast_helper(
                1,
                1,
                0,
                "[x^3+85*x+3, x-2, 1, x^2+x-10, x+1/7, 1, 1, x^4+x^3-11/4*x^2-1/4, 1, 1, x^2+x-1, 1," +
                " x^2+4/13*x-1/13, x^2-2/7, x^3-2*x^2-x, x^3-3/11*x^2+1/11*x-2/11, 1, 1, 1, 1, ...]",
                "{1=38761, x=9737, x+1=2467, x^2=2455, x-1=2386, x^3=657, x+1/2=628, x^2-x=627, x^2-1=625, x^2+1=625}",
                1.3250399999998215,
                2.3070570828933272
        );
        monicRationalPolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[x^6+1/9*x^5-1/9*x^3-233/9*x^2+1256222/9*x+85/9, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " x^20-1/62*x^19+5/62*x^18-61/31*x^17-197/31*x^16-119/31*x^15-13/62*x^14-120960/31*x^13+" +
                "3909/31*x^12+41/62*x^11-32290/31*x^10+5/62*x^8-2/31*x^7-19/62*x^6-1/31*x^5-1/62*x^4+1/31*x^3-" +
                "2226/31, x^3-4/633*x^2+1/633*x-13/633, x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, x-242/3," +
                " x^2+15/7*x-23/7, x^7-1/2*x^5+13/2*x^3+9/2*x^2-x+115001/2, x^5+8/13*x^4-1/13*x^3-329/13*x," +
                " x^8-3/13*x^7+3/13*x^5-21/13*x^4-x^2+8/13*x+9/13, x^4-2*x^3+3347*x^2+9*x+3," +
                " x^4+6/5209*x^3-1/5209*x^2-3/5209*x-149/5209, x^4-4045/2*x^3-154/3*x^2-61/6*x+1/2," +
                " x^8-19/18566*x^6+31/18566*x^4-2828/9283*x^3-1809/9283*x-168/9283, x^4-59/33*x^3-4514/11*x+106/3," +
                " x^4+x^3-48401*x+10, x^5+86/3*x^4+4843/3*x^3-1/3*x^2+37*x+4/3," +
                " x^5-832/3*x^4+79*x^3+7/3*x^2+19/3*x-46834, ...]",
                "{1=6028, x=809, x-1=346, x+1=299, x-1/3=136, x-1/2=136, x+2=131, x+3=131, x+1/3=127, x+1/2=125}",
                4.141790000001693,
                8.108606146870954
        );
        monicRationalPolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[x^6+1/9*x^5-1/9*x^3-233/9*x^2+1256222/9*x+85/9, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " x^20-1/62*x^19+5/62*x^18-61/31*x^17-197/31*x^16-119/31*x^15-13/62*x^14-120960/31*x^13+" +
                "3909/31*x^12+41/62*x^11-32290/31*x^10+5/62*x^8-2/31*x^7-19/62*x^6-1/31*x^5-1/62*x^4+1/31*x^3-" +
                "2226/31, x^3-4/633*x^2+1/633*x-13/633, x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, x-242/3," +
                " x^2+15/7*x-23/7, x^7-1/2*x^5+13/2*x^3+9/2*x^2-x+115001/2, x^5+8/13*x^4-1/13*x^3-329/13*x," +
                " x^8-3/13*x^7+3/13*x^5-21/13*x^4-x^2+8/13*x+9/13, x^4-2*x^3+3347*x^2+9*x+3," +
                " x^4+6/5209*x^3-1/5209*x^2-3/5209*x-149/5209, x^4-4045/2*x^3-154/3*x^2-61/6*x+1/2," +
                " x^8-19/18566*x^6+31/18566*x^4-2828/9283*x^3-1809/9283*x-168/9283, x^4-59/33*x^3-4514/11*x+106/3," +
                " x^4+x^3-48401*x+10, x^5+86/3*x^4+4843/3*x^3-1/3*x^2+37*x+4/3," +
                " x^5-832/3*x^4+79*x^3+7/3*x^2+19/3*x-46834, ...]",
                "{1=6028, x=809, x-1=346, x+1=299, x-1/3=136, x-1/2=136, x+2=131, x+3=131, x+1/3=127, x+1/2=125}",
                4.141790000001693,
                8.108606146870954
        );
        monicRationalPolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[x^2+30*x-46, x^3-45325468*x^2+1/4*x-1001/2, x^2-4/633*x+1/633, x^3-1/3*x-1/3," +
                " x^2+122224/91*x+47/91, x^2-1/114*x-15659/114, x^3+1/12*x^2+223/12, x^2+2/47*x-3254/47," +
                " x^5+27*x^4-11815*x^3-38*x^2-5*x+7, x^3+15/7*x^2-15/7*x+1/7, x^2-365/383*x+13/766," +
                " x^3+344/1856865*x^2+2/1856865*x-1/1856865, x^4+37/33*x^3-1/33*x^2-1/11*x+6454/33," +
                " x^5-26/1537*x^4+5/1537*x^3-11/1537*x^2+79/1537, x^2+16/13*x, x^3+12*x^2+21*x," +
                " x^4+3/2216*x^3+13/2216*x^2-28/277, x^2-5/3*x+7/6, x^2-2*x+3347, x^4+2/435*x^2-809/87*x-118/1305," +
                " ...]",
                "{x^2=262, x^2+x=117, x^2-1=112, x^2-x=110, x^2+1=104, x^2+1/3*x=60, x^2-1/2*x=58, x^2+x-1=55," +
                " x^2-2*x=53, x^2-1/2=53}",
                3.090710000001697,
                7.769076761722806
        );
        monicRationalPolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "[x^31-5510881/2764346*x^30-2/4146519*x^29-19/8293038*x^28-8/4146519*x^27+9/1382173*x^26-" +
                "1202/4146519*x^24+1965582442977535455/2764346*x^23-239/8293038*x^22-1/8293038*x^21+1/4146519*x^20-" +
                "1/2764346*x^19-116/4146519*x^18+1/4146519*x^17+27/2764346*x^16+204/1382173*x^15+7/4146519*x^14-" +
                "1/2764346*x^13+1/8293038*x^12-1/4146519*x^11+1/8293038*x^10+28/4146519*x^9+283/4146519*x^8-" +
                "437219/8293038*x^7-33182/4146519*x^6-62/4146519*x^5-2313382/4146519*x^4-645698713/8293038*x^3-" +
                "3/212642*x^2+110/4146519*x-2129/8293038," +
                " x^16-2693627/4432651024*x^15+152929/13297953072*x^14+491/1662244134*x^13-1/13297953072*x^11+" +
                "1673/6648976536*x^10-1/6648976536*x^9+3064121/13297953072*x^8-835/554081378*x^7-" +
                "1005/4432651024*x^5+5/13297953072*x^4-3/4432651024*x^3+640780313507/1108162756*x^2-9/277040689*x," +
                " x^18+2/10025*x^17-434063/10025*x^16+1/2005*x^15+1/10025*x^14-736/10025*x^13+7182/10025*x^12-" +
                "7/10025*x^11+3/10025*x^10-248/10025*x^9-13/10025*x^8+1597518/10025*x^7+1105224/10025*x^6+" +
                "3133/10025*x^5+1/10025*x^4-3/10025*x^3-3/10025*x-4/10025," +
                " x^12-2304971396104/31*x^11-355615/31*x^10-3640006/31*x^9-3421/31*x^8+25689/31*x^7+524/31*x^6-" +
                "789/31*x^5+3/31*x^4+663885/31*x^3+9/31*x^2-21118/31*x-14/31," +
                " x^6-127/7*x^5-5/7*x^4-1684/7*x^3+36/7*x^2-632/7*x-4177, x+54/36587," +
                " x^12+262*x^11-2312*x^10+24*x^9+65*x^8-1060*x^7-40*x^6+22*x^3-117129*x^2-15528*x-130," +
                " x^9-1/11*x^8+1/33*x^7+995620/33*x^6-3117942350/33*x^5-1/11*x^4+23/33*x^3-7/33*x^2-52735763/11*x+" +
                "5473/33," +
                " x^10+5*x^9+1402/3*x^8-1/2*x^7-3076/3*x^6-1423414505/6*x^5+37841/6*x^4-74/3*x^3-4*x^2+239/2*x+2/3," +
                " x^8-7665/811*x^7+2851/1622*x^6+3/3244*x^5+1/3244*x^4+17503/1622*x^3+26/811*x^2-17/3244*x-" +
                "19286905/1622," +
                " x^12-3578/438297*x^11-9859388/438297*x^10-22/146099*x^9-14/438297*x^8-317/438297*x^7-" +
                "109/438297*x^6+11/438297*x^5+134274687199375/146099*x^3-208090/438297*x^2+4/146099*x+12/146099," +
                " x^5+1/308602588*x^2-1/154301294, x^4+3*x^3+4*x^2+128423*x+91," +
                " x^8-40/530566274871838627*x^7-1522584726/530566274871838627*x^6+8978/530566274871838627*x^5-" +
                "42/530566274871838627*x^4-2/530566274871838627*x^2+2541395/530566274871838627*x," +
                " x^43+940665/10141*x^41-192/10141*x^40+61/10141*x^38-11/10141*x^37-15687/10141*x^36+1/10141*x^35+" +
                "1782/10141*x^34+5379/10141*x^33-3/10141*x^32-1066/10141*x^30+319661334/10141*x^29-10/10141*x^28+" +
                "901/10141*x^27-205195/10141*x^26+21920/10141*x^25+3930/10141*x^24+43/10141*x^23+" +
                "1322424107679/10141*x^22+83481365755/10141*x^21+3/10141*x^20-655648/10141*x^18-532936/10141*x^17-" +
                "9532/10141*x^16-1/10141*x^15-2471048/10141*x^14+2657104/10141*x^13+13/10141*x^12-1/10141*x^11+" +
                "249/10141*x^10-154687192/10141*x^9+4/10141*x^8+3/10141*x^6+45331/10141*x^5-9647/10141*x^4-" +
                "77774/10141*x^3+10/10141*x^2-2/10141, x^5-7/4*x^4-226081757405/4*x^3-3/4*x^2-1778987/4*x-397/4," +
                " x^17+5/36056*x^16-135597/36056*x^15-3/18028*x^14+831/18028*x^13-1/9014*x^12-5201/9014*x^11+" +
                "1618873/18028*x^10+9/18028*x^9+43/18028*x^8-867/36056*x^7-179/36056*x^6-1/36056*x^5-87/36056*x^4-" +
                "25/18028*x^2+7/36056*x+3247609/9014, x^5-43/819*x^4+84904/63*x^3+829289/819*x^2-55/819*x+6/91," +
                " x^35+19/161*x^34+10533/7*x^33-3/161*x^31-495015/161*x^30-2/161*x^29+405597/161*x^28+661/161*x^27+" +
                "30/23*x^26-6/161*x^25+31/161*x^23+6/23*x^22+30/161*x^21+12/161*x^20+6/161*x^18+51/161*x^17-" +
                "6/161*x^16-538014882259/161*x^15+1969/161*x^14-1/161*x^13+8/161*x^12+3554/161*x^11-150263/161*x^10+" +
                "168244/161*x^8-122221910/161*x^7-29/161*x^6+53/161*x^5-31/161*x^4-246/161*x^3+1/161*x^2-6/161*x+" +
                "6729/161," +
                " x^10+262/47*x^9-9/47*x^8-76/47*x^6-58416/47*x^5-56/47*x^4-37/47*x^3-3/47*x^2+2/47*x-1531/47, ...]",
                "{1=1210, x=106, x+1=47, x-1=36, x-3=27, x+3=25, x-2=23, x+1/2=21, x-1/2=19, x+2=17}",
                9.458719999998882,
                17.390248519873563
        );
        monicRationalPolynomialsAtLeast_helper(
                10,
                8,
                2,
                "[x^5-454294/105065295*x^4-622/105065295*x^3-181982/21013059*x^2+2764346/35021765*x-" +
                "13899489/35021765, x^5+1/4*x^4-575*x^3+9/2*x^2-1283/4*x-931083/2," +
                " x^14-17/1510370*x^13+8138/755185*x^12-4341/755185*x^11+172556137/755185*x^10+50671/755185*x^9-" +
                "739/1510370*x^8-55530517/755185*x^6-1/755185*x^5-253/755185*x^4+87/1510370*x^3-3/151037*x^2+" +
                "47/1510370*x-12525687/151037," +
                " x^7-36/640780313507*x^6+796582/1922340940521*x^5+5/2563121254028*x^3+1/2563121254028*x^2-" +
                "394953281/7689363762084*x+588968/640780313507," +
                " x^6-2693627/4432651024*x^5+152929/13297953072*x^4+491/1662244134*x^3-1/13297953072*x+" +
                "15/4432651024, x^5-3*x^4-3*x^2-x-5928689," +
                " x^7-1/1026*x^6+1/2394*x^5-124/3591*x^4-13/7182*x^3+88751/399*x^2+184204/1197*x+10/3591," +
                " x^5+7/17*x^4-330228993066/17*x^3-11/17*x^2+1524767947173/17*x+1/17," +
                " x^18-13369365953/19882*x^17+1/9941*x^16-375/19882*x^15-589/19882*x^14+22058738851/9941*x^13+" +
                "207/19882*x^11-198077/19882*x^10-956783/19882*x^9-672/9941*x^8+27/19882*x^7+7/19882*x^6+" +
                "16011301/9941*x^5+2443/9941*x^4+30/9941*x^2+3/19882*x+3701/19882, x^5-29255*x^3-1/5*x^2-31*x-447/5," +
                " x^4-35/2*x^3-x^2-2*x-1/3, x^7-1572/193*x^6+281/772*x^5+1/386*x^4+209/772*x^2-23971/772*x+707/772," +
                " x^8-11637*x^7+252307688*x^6+33/2*x^5-3/2*x^4+1/2*x^3+497810*x^2-1558971175*x-5/2," +
                " x^3-6*x^2+84*x-164, x^2+1/2*x-222713/2, x^2+2985/2*x+7/2," +
                " x^5-163963/100*x^3+1/20*x^2-1/100*x+3750327/25," +
                " x^7-247*x^6-253781*x^5+128*x^4-12*x^3+2214*x^2-151684*x," +
                " x^5-154687192/249*x^4+4/249*x^3+1/83*x+3889/249, x^4-655648/3*x^2-532936/3*x-5908, ...]",
                "{x^2=11, x^2+x=7, x^2-x+1=6, x^2-x=4, x^2+1/3*x=4, x^2+1/2*x=4, x^2-1/2=4, x^2-x-1=4, x^2+7*x-1=3," +
                " x^2+1/2=3}",
                8.244310000001628,
                17.22049022589926
        );
        monicRationalPolynomialsAtLeast_helper(
                10,
                8,
                7,
                "[x^10+283/28*x^9-437219/56*x^8-16591/14*x^7-31/14*x^6-1156691/14*x^5-645698713/56*x^4-117/56*x^3+" +
                "55/14*x^2-337/56*x-2870934583/56," +
                " x^7-1202/27*x^5+655194147659178485/6*x^4-239/54*x^3-1/54*x^2+1/27*x-1/18," +
                " x^10+50671/172556137*x^9-739/345112274*x^8-105371/327431*x^6-1/172556137*x^5-253/172556137*x^4+" +
                "87/345112274*x^3-15/172556137*x^2+47/345112274*x-1293218/172556137," +
                " x^9+15/3186328*x^7+3/3186328*x^6-394953281/3186328*x^5+65794095/1593164*x^4+755185/1593164*x^3-" +
                "17/3186328*x^2+4069/796582*x-757/1593164," +
                " x^7+184204/266253*x^6+241/122886*x^5+1/1597518*x^4-1/532506*x^3-1/532506*x," +
                " x^7+1/5*x^6-736/5*x^5+7182/5*x^4-7/5*x^3+3/5*x^2-248/5*x-1," +
                " x^11+1/73765*x^10-21118/663885*x^9-2/221295*x^8-39554/663885*x^7-4/221295*x^6-419/663885*x^5-" +
                "34/44259*x^4-2/132777*x^3+2005/132777*x^2+2/663885*x-171919/663885," +
                " x^8+7/17*x^7-330228993066/17*x^6-11/17*x^5+1524767947173/17*x^4-7/17*x^2-1/17*x+7/17," +
                " x^10+114/629*x^9-23/629*x^8+112/629*x^7+397143/629*x^6+50/629*x^5-2/629*x^4-27/629*x^3-" +
                "231522/629*x^2+3/629*x," +
                " x^9-198077/207*x^8-956783/207*x^7-448/69*x^6+3/23*x^5+7/207*x^4+32022602/207*x^3+4886/207*x^2+" +
                "155/207," +
                " x^7+22/36587*x^6-1/36587*x^5-109310/36587*x^4-1543/36587*x^3-174785977915/36587*x^2-" +
                "2322630/36587*x-146860746/36587," +
                " x^7-1/7326531916*x^6+93/7326531916*x^5+398077/29306127664*x^4-51/29306127664*x^3-" +
                "3/29306127664*x^2+973/29306127664*x-5/29306127664," +
                " x^7+5994968441128/203*x^5-47/29*x^4+6/203*x^3-15/29*x^2-6/203*x-12/203," +
                " x^7-7/23*x^6-158207289/23*x^5+1377/23*x^4+772/23*x^3-6288/23*x^2+281/23*x+1/23," +
                " x^7-91641*x^6-30*x^4-7*x^3-4*x^2+58*x-3," +
                " x^7-148/37841*x^6-24/37841*x^5+717/37841*x^4+2/37841*x^3+2985/37841*x^2+15/37841*x+1/37841," +
                " x^9+14/3*x^8-148/3*x^7+254284349/3*x^6+2*x^5+10*x^4+2804/3*x^3-x^2-6152/3*x-349672681/3," +
                " x^8-13/2*x^7-46769008*x^6-467/2*x^5+7251*x^3+430*x^2-x+5170," +
                " x^7+21493072257233/5767973249*x^6+1/5767973249*x^5-106/5767973249*x^4+2245/5767973249*x^3+" +
                "316844212/5767973249*x^2-28/5767973249*x+20/5767973249," +
                " x^8+35006*x^7+104*x^6-17*x^5-21796594*x^4-3111*x^3-240*x^2-11, ...]",
                "{x^10+283/28*x^9-437219/56*x^8-16591/14*x^7-31/14*x^6-1156691/14*x^5-645698713/56*x^4-117/56*x^3+" +
                "55/14*x^2-337/56*x-2870934583/56=1," +
                " x^7-1202/27*x^5+655194147659178485/6*x^4-239/54*x^3-1/54*x^2+1/27*x-1/18=1," +
                " x^10+50671/172556137*x^9-739/345112274*x^8-105371/327431*x^6-1/172556137*x^5-253/172556137*x^4+" +
                "87/345112274*x^3-15/172556137*x^2+47/345112274*x-1293218/172556137=1," +
                " x^9+15/3186328*x^7+3/3186328*x^6-394953281/3186328*x^5+65794095/1593164*x^4+755185/1593164*x^3-" +
                "17/3186328*x^2+4069/796582*x-757/1593164=1," +
                " x^7+184204/266253*x^6+241/122886*x^5+1/1597518*x^4-1/532506*x^3-1/532506*x=1," +
                " x^7+1/5*x^6-736/5*x^5+7182/5*x^4-7/5*x^3+3/5*x^2-248/5*x-1=1," +
                " x^11+1/73765*x^10-21118/663885*x^9-2/221295*x^8-39554/663885*x^7-4/221295*x^6-419/663885*x^5-" +
                "34/44259*x^4-2/132777*x^3+2005/132777*x^2+2/663885*x-171919/663885=1," +
                " x^8+7/17*x^7-330228993066/17*x^6-11/17*x^5+1524767947173/17*x^4-7/17*x^2-1/17*x+7/17=1," +
                " x^10+114/629*x^9-23/629*x^8+112/629*x^7+397143/629*x^6+50/629*x^5-2/629*x^4-27/629*x^3-" +
                "231522/629*x^2+3/629*x=1," +
                " x^9-198077/207*x^8-956783/207*x^7-448/69*x^6+3/23*x^5+7/207*x^4+32022602/207*x^3+4886/207*x^2+15" +
                "5/207=1}",
                8.002689999998019,
                17.146973848853506
        );
        monicRationalPolynomialsAtLeast_fail_helper(1, 0, -1);
        monicRationalPolynomialsAtLeast_fail_helper(0, 1, -1);
        monicRationalPolynomialsAtLeast_fail_helper(1, 0, 0);
        monicRationalPolynomialsAtLeast_fail_helper(0, 1, 0);
        monicRationalPolynomialsAtLeast_fail_helper(1, 3, 3);
        monicRationalPolynomialsAtLeast_fail_helper(1, 1, -2);
    }

    private static void variables_helper(int mean, @NotNull String output, @NotNull String topSampleCount) {
        List<Variable> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(mean).variables()));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        P.reset();
    }

    private static void variables_fail_helper(int mean) {
        try {
            P.withScale(mean).variables();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testVariables() {
        variables_helper(
                1,
                "[d, c, j, c, c, a, b, d, a, a, c, c, c, a, c, a, a, b, b, a, ...]",
                "{a=499125, b=250897, c=124849, d=62518, e=31407, f=15634, g=7825, h=3926, i=1896, j=956}"
        );
        variables_helper(
                10,
                "[kk, m, k, i, gg, y, b, h, a, r, u, a, k, i, d, e, a, b, d, g, ...]",
                "{a=90519, b=82798, c=75630, d=68355, e=62062, f=56573, g=51318, h=46453, i=42422, j=38243}"
        );
        variables_helper(
                100,
                "[tttttt, a, nnn, hh, ff, oooooo, ii, mmmmm, aa, gggg, i, lll, k, p, a, hhhhhh, kk, dddddddddddd," +
                " wwwww, nnn, ...]",
                "{a=9916, c=9740, b=9715, e=9630, d=9504, f=9377, g=9328, h=9252, j=9073, i=9025}"
        );
        variables_fail_helper(0);
        variables_fail_helper(-1);
        variables_fail_helper(Integer.MAX_VALUE);
    }

    private static <T> void simpleProviderHelper(
            @NotNull Iterable<T> xs,
            @NotNull String output,
            @NotNull String sampleCountOutput
    ) {
        List<T> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeqit(sampleCount(sample).entrySet(), sampleCountOutput);
        P.reset();
    }

    @Test
    public void testMonomialOrders() {
        simpleProviderHelper(
                P.monomialOrders(),
                "[GRLEX, GRLEX, GREVLEX, GRLEX, LEX, GRLEX, GRLEX, GRLEX, GRLEX, GRLEX, LEX, GRLEX, GRLEX, GREVLEX," +
                " GRLEX, GREVLEX, LEX, GREVLEX, GRLEX, GRLEX, ...]",
                "[GRLEX=333313, GREVLEX=333615, LEX=333072]"
        );
    }

    private static void exponentVectorHelper(
            @NotNull Iterable<ExponentVector> xs,
            @NotNull String output,
            @NotNull String topSampleCount,
            double degreeMean
    ) {
        List<ExponentVector> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(ExponentVector::degree, sample))), degreeMean);
    }

    private static void exponentVectors_helper(
            int scale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double degreeMean
    ) {
        exponentVectorHelper(P.withScale(scale).exponentVectors(), output, topSampleCount, degreeMean);
        P.reset();
    }

    private static void exponentVectors_fail_helper(int scale) {
        try {
            P.withScale(scale).exponentVectors();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testExponentVectors() {
        exponentVectors_helper(
                1,
                "[a^2*b^9*c^2, b, c^2, a, 1, a^2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, a^4, b, a^3*b*c^5*d^3*e*f, ...]",
                "{1=666515, a=83441, a^2=42011, a^3=20629, b=20569, a^4=10580, a*b=10523, b^2=10383, a*b^2=5193," +
                " a^2*b=5149}",
                1.0010019999980002
        );
        exponentVectors_helper(
                8,
                "[a^27*b^9*c^9*d^5*e^29*f^18, a^5, 1, 1, a^10*b^13*d^10*e^6*f^3*g^3*i, a^5*b^10*c^8*d^3*e^7, a^2, a," +
                " a^7*b^48*c^11, a^5, 1, a*b^4, 1, 1, a^15*b^20*c^16, a^9, a^15, a^21*b^2*c^8," +
                " a^30*b^5*c^32*d^5*e*f^4*g^13*h^8*i^7, a^7*c^2*d^5*e^6, ...]",
                "{1=272869, a=20020, a^2=17990, a^3=15944, a^4=14310, a^5=12346, a^6=11025, a^7=9815, a^8=8817," +
                " a^9=7855}",
                24.01019300000956
        );
        exponentVectors_helper(
                32,
                "[a^17*b^47*c^25*e^19, a^41, a^6*b^15, a^16*b^23*c^24*d^9*e^84*f^32*g^28, a^2," +
                " a^38*b^3*c^12*d^21*e^45*f^34*g^28*h^14, 1, a^10*b^2*c^24*d^40*e^37," +
                " a^82*b^31*c^19*d^31*e^19*f^36*g^44*h^43*i^168, a^14*b^7*c^3*d^31, a^7*b^121*c^23*d^122*e^19, a^74," +
                " a^7*b^6*c^16*d^51*e^10*f^7*g^31*h^9*i^18, a^9*b^17*c*d^73, a^7*b^29*c^21*d^3*e^55," +
                " a^12*b^35*c^10*d^64, a^9*b^48*c^24*d^170, a^59, a^18*b^32*c^22, 1, ...]",
                "{1=146150, a=3659, a^2=3603, a^3=3516, a^4=3320, a^5=3299, a^6=3164, a^7=3018, a^8=2987, a^9=2851}",
                192.2389910000015
        );
        exponentVectors_fail_helper(0);
        exponentVectors_fail_helper(-1);
    }

    private static void exponentVectors_List_Variable_helper(
            int scale,
            @NotNull String variables,
            @NotNull String output,
            @NotNull String topSampleCount,
            double degreeMean
    ) {
        exponentVectorHelper(
                P.withScale(scale).exponentVectors(readVariableList(variables)),
                output,
                topSampleCount,
                degreeMean
        );
        P.reset();
    }

    private static void exponentVectors_List_Variable_fail_helper(int scale, @NotNull String variables) {
        try {
            P.withScale(scale).exponentVectors(readVariableListWithNulls(variables));
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testExponentVectors_List_Variable() {
        exponentVectors_List_Variable_helper(
                1,
                "[]",
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]",
                "{1=1000000}",
                0.0
        );
        exponentVectors_List_Variable_helper(
                8,
                "[]",
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]",
                "{1=1000000}",
                0.0
        );
        exponentVectors_List_Variable_helper(
                1,
                "[a]",
                "[a^3, a^2, a^9, a^2, a^2, 1, a, a^3, 1, 1, a^2, a^2, a^2, 1, a^2, 1, 1, a, a, 1, ...]",
                "{1=499125, a=250897, a^2=124849, a^3=62518, a^4=31407, a^5=15634, a^6=7825, a^7=3926," +
                " a^8=1896, a^9=956}",
                1.0008359999977228
        );
        exponentVectors_List_Variable_helper(
                8,
                "[a]",
                "[a^31, a^9, a^9, a^5, a^29, a^18, a, a^5, 1, a^17, a^13, 1, a^10, a^6, a^3, a^3, 1, a, a^2, a^5," +
                " ...]",
                "{1=110949, a=98973, a^2=87810, a^3=78512, a^4=69401, a^5=61358, a^6=54691, a^7=48553, a^8=43415," +
                " a^9=38254}",
                7.996049000016875
        );
        exponentVectors_List_Variable_helper(
                1,
                "[x, y]",
                "[x^2*y^3, x^2*y^9, y^2, x^3*y, 1, x^2*y^2, y^2, y^2, x, y, x^2*y, y^2, x, x, 1, y, 1, 1, 1, 1, ...]",
                "{1=250050, y=125653, x=124331, y^2=62471, x*y=62445, x^2=62377, x^3=31539, x*y^2=31263," +
                " x^2*y=31206, y^3=31114}",
                1.9999819999876247
        );
        exponentVectors_List_Variable_helper(
                8,
                "[x, y]",
                "[x^9*y^31, x^5*y^9, x^18*y^29, x^5*y, x^17, y^13, x^6*y^10, x^3*y^3, x, x^5*y^2, x^8*y^10, x^7*y^3," +
                " x^2*y^3, x^7*y, x^11*y^48, x^6*y^2, y^6, x^4*y^4, x^20*y^21, x*y^16, ...]",
                "{1=12290, x=10930, y=10903, y^2=9872, x^2=9814, x*y=9770, x*y^2=8796, x^2*y=8714, y^3=8654," +
                " x^3=8551}",
                15.985479999996784
        );
        exponentVectors_List_Variable_helper(
                1,
                "[a, b, c]",
                "[a^9*b^2*c^3, b^2*c^2, b^3*c, a^2*b^2, a^2*c^2, a, a*c, b^2*c^2, b, c, c, 1, 1, 1, a^4*b^4*c, a," +
                " b^2, a^3*b^6*c, a^3*b^5*c, a^3*b*c, ...]",
                "{1=124658, c=62644, a=62464, b=62432, a*c=31428, c^2=31305, b*c=31261, a^2=31133, b^2=31035," +
                " a*b=31027}",
                3.0025289999869127
        );
        exponentVectors_List_Variable_helper(
                8,
                "[a, b, c]",
                "[a^9*b^9*c^31, a^18*b^29*c^5, b^5*c, b^13*c^17, a^3*b^6*c^10, a*c^3, a^10*b^5*c^2, a^7*b^3*c^8," +
                " a*b^2*c^3, a^11*b^48*c^7, a^6*b^6*c^2, a^4*b^4, a^16*b^20*c^21, b^9*c, a^2*b^21*c^15," +
                " a^30*b^3*c^8, a^5*b^32*c^5, a^13*b^4*c, a^10*b^7*c^8, a^5*b^2, ...]",
                "{1=1356, c=1258, a=1223, b=1194, b*c=1138, b^2=1132, c^2=1085, a*b=1064, a^2=1049, a*c=1022}",
                23.976022000016762
        );
        exponentVectors_List_Variable_fail_helper(0, "[a, b]");
        exponentVectors_List_Variable_fail_helper(-1, "[a, b]");
        exponentVectors_List_Variable_fail_helper(1, "[a, a]");
        exponentVectors_List_Variable_fail_helper(1, "[b, a]");
    }

    private static void multivariatePolynomials_helper(
            @NotNull Iterable<MultivariatePolynomial> input,
            @NotNull String output,
            double meanTermCount,
            double meanDegree
    ) {
        List<MultivariatePolynomial> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(MultivariatePolynomial::termCount, sample))), meanTermCount);
        aeq(meanOfIntegers(toList(map(MultivariatePolynomial::degree, sample))), meanDegree);
        P.reset();
    }

    private static void multivariatePolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanTermCount,
            double meanDegree
    ) {
        multivariatePolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).multivariatePolynomials(),
                output,
                meanTermCount,
                meanDegree
        );
        P.reset();
    }

    private static void multivariatePolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).multivariatePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMultivariatePolynomials() {
        multivariatePolynomials_helper(
                2,
                2,
                "QBarRandomProvider_multivariatePolynomials_i",
                0.8646899999996744,
                0.7044800000003882
        );
        multivariatePolynomials_helper(
                5,
                3,
                "QBarRandomProvider_multivariatePolynomials_ii",
                2.1347100000003274,
                5.328380000001406
        );
        multivariatePolynomials_helper(
                10,
                8,
                "QBarRandomProvider_multivariatePolynomials_iii",
                6.196000000000041,
                7.59621000000156
        );

        multivariatePolynomials_fail_helper(1, 2);
        multivariatePolynomials_fail_helper(2, 1);
    }

    private static double meanOfIntegers(@NotNull List<Integer> xs) {
        int size = xs.size();
        return sumDouble(map(i -> (double) i / size, xs));
    }

    private static double meanOfRationals(@NotNull List<Rational> xs) {
        int size = xs.size();
        return sumDouble(map(r -> r.doubleValue() / size, xs));
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readList(Variable::read).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Variable::read).apply(s).get();
    }
}
