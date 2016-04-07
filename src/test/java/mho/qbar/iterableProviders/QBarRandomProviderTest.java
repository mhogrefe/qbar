package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.testing.QBarTesting.aeqMapQBarLog;
import static mho.qbar.testing.QBarTesting.aeqitLimitQBarLog;
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

    private static void monicPolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).monicPolynomials(degree),
                output,
                meanDimension,
                meanCoefficientBitSize
        );
    }

    private static void monicPolynomials_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).monicPolynomials(degree);
            fail();
        } catch (IllegalArgumentException | IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonicPolynomials_int() {
        monicPolynomials_int_helper(
                1,
                0,
                "QBarRandomProvider_monicPolynomials_int_i",
                0.0,
                1.000000000007918
        );
        monicPolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_monicPolynomials_int_ii",
                2.9999999999775233,
                3.9137364999168973
        );
        monicPolynomials_int_helper(
                10,
                8,
                "QBarRandomProvider_monicPolynomials_int_iii",
                8.000000000063345,
                8.930733111273517
        );

        monicPolynomials_int_fail_helper(0, -1);
        monicPolynomials_int_fail_helper(1, -1);
    }

    private static void monicPolynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).monicPolynomials(),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void monicPolynomials_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).monicPolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonicPolynomials() {
        monicPolynomials_helper(
                1,
                1,
                "QBarRandomProvider_monicPolynomials_i",
                1.0006929999977097,
                0.9168388153632843
        );
        monicPolynomials_helper(
                5,
                3,
                "QBarRandomProvider_monicPolynomials_ii",
                3.00101199999147,
                3.9122941895036285
        );
        monicPolynomials_helper(
                10,
                8,
                "QBarRandomProvider_monicPolynomials_iii",
                7.983070000016452,
                8.931442034672816
        );

        monicPolynomials_fail_helper(0, 1);
        monicPolynomials_fail_helper(1, 0);
    }

    private static void monicPolynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).monicPolynomialsAtLeast(minDegree),
                output,
                meanDegree,
                meanCoefficientBitSize
        );
    }

    private static void monicPolynomialsAtLeast_fail_helper(int scale, int secondaryScale, int minDegree) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).monicPolynomialsAtLeast(minDegree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonicPolynomialsAtLeast() {
        monicPolynomialsAtLeast_helper(
                1,
                1,
                -1,
                "QBarRandomProvider_polynomialsAtLeast_i",
                1.0006929999977097,
                0.9168388153632843
        );
        monicPolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_polynomialsAtLeast_ii",
                3.00101199999147,
                3.9122941895036285
        );
        monicPolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_polynomialsAtLeast_iii",
                3.00101199999147,
                3.9122941895036285
        );
        monicPolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_polynomialsAtLeast_iv",
                3.0005879999687126,
                3.914580056823708
        );
        monicPolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_polynomialsAtLeast_v",
                7.983070000016452,
                8.931442034672816
        );
        monicPolynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_polynomialsAtLeast_vi",
                8.01187900001392,
                8.93072132885421
        );
        monicPolynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_polynomialsAtLeast_vii",
                8.00086800006071,
                8.930916996180777
        );

        monicPolynomialsAtLeast_fail_helper(0, 0, -1);
        monicPolynomialsAtLeast_fail_helper(1, -1, -1);
        monicPolynomialsAtLeast_fail_helper(1, 3, 3);
        monicPolynomialsAtLeast_fail_helper(1, 1, -2);
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
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        List<RationalPolynomial> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(RationalPolynomial::degree, sample))), meanDegree);
        aeq(meanOfIntegers(toList(concatMap(p -> map(Rational::bitLength, p), sample))), meanCoefficientBitSize);
        P.reset();
    }

    private static void rationalPolynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).rationalPolynomials(degree),
                output,
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
                "QBarRandomProvider_rationalPolynomials_int_i",
                -0.9999999999980838,
                0.0
        );
        rationalPolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_rationalPolynomials_int_ii",
                3.000000000005079,
                5.000859999992796
        );
        rationalPolynomials_int_helper(
                10,
                8,
                "QBarRandomProvider_rationalPolynomials_int_iii",
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
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomials(),
                output,
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
                "QBarRandomProvider_rationalPolynomials_i",
                0.687210000000254,
                3.113726210728528
        );
        rationalPolynomials_helper(
                5,
                3,
                "QBarRandomProvider_rationalPolynomials_ii",
                2.8709299999999094,
                4.96048494805455
        );
        rationalPolynomials_helper(
                10,
                8,
                "QBarRandomProvider_rationalPolynomials_iii",
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
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialsAtLeast(minDegree),
                output,
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
                "QBarRandomProvider_rationalPolynomialsAtLeast_i",
                -0.22369000000018416,
                3.272416946837637
        );
        rationalPolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_rationalPolynomialsAtLeast_ii",
                2.8709299999999094,
                4.96048494805455
        );
        rationalPolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_rationalPolynomialsAtLeast_iii",
                3.0100400000011516,
                5.000735653514337
        );
        rationalPolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_rationalPolynomialsAtLeast_iv",
                2.9997700000015786,
                5.00013000747141
        );
        rationalPolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_rationalPolynomialsAtLeast_v",
                7.88165000000161,
                9.88691740842155
        );
        rationalPolynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_rationalPolynomialsAtLeast_vi",
                7.982850000001959,
                9.893852173886888
        );
        rationalPolynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_rationalPolynomialsAtLeast_vii",
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
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).monicRationalPolynomials(degree),
                output,
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
                "QBarRandomProvider_monicRationalPolynomials_int_i",
                0.0,
                1.9999999999961675
        );
        monicRationalPolynomials_int_helper(
                1,
                1,
                "QBarRandomProvider_monicRationalPolynomials_int_ii",
                0.9999999999980838,
                2.3964350000032097
        );
        monicRationalPolynomials_int_helper(
                5,
                3,
                "QBarRandomProvider_monicRationalPolynomials_int_iii",
                3.000000000005079,
                7.7314374999814275
        );
        monicRationalPolynomials_int_helper(
                10,
                8,
                "QBarRandomProvider_monicRationalPolynomials_int_iv",
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
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).monicRationalPolynomials(),
                output,
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
                "QBarRandomProvider_monicRationalPolynomials_i",
                1.3250399999998215,
                2.3070570828933272
        );
        monicRationalPolynomials_helper(
                5,
                3,
                "QBarRandomProvider_monicRationalPolynomials_ii",
                4.141790000001693,
                8.108606146870954
        );
        monicRationalPolynomials_helper(
                10,
                8,
                "QBarRandomProvider_monicRationalPolynomials_iii",
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
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalPolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).monicRationalPolynomialsAtLeast(minDegree),
                output,
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
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_i",
                1.3250399999998215,
                2.3070570828933272
        );
        monicRationalPolynomialsAtLeast_helper(
                1,
                1,
                0,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_ii",
                1.3250399999998215,
                2.3070570828933272
        );
        monicRationalPolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_iii",
                4.141790000001693,
                8.108606146870954
        );
        monicRationalPolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_iv",
                4.141790000001693,
                8.108606146870954
        );
        monicRationalPolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_v",
                3.090710000001697,
                7.769076761722806
        );
        monicRationalPolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_vi",
                9.458719999998882,
                17.390248519873563
        );
        monicRationalPolynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_vii",
                8.244310000001628,
                17.22049022589926
        );
        monicRationalPolynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_monicRationalPolynomialsAtLeast_viii",
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

    private static void variables_helper(int mean, @NotNull String output) {
        List<Variable> sample = toList(take(DEFAULT_SAMPLE_SIZE, P.withScale(mean).variables()));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        variables_helper(1, "QBarRandomProvider_variables_i");
        variables_helper(10, "QBarRandomProvider_variables_ii");
        variables_helper(100, "QBarRandomProvider_variables_iii");
        variables_fail_helper(0);
        variables_fail_helper(-1);
        variables_fail_helper(Integer.MAX_VALUE);
    }

    private static <T> void simpleProviderHelper(@NotNull Iterable<T> xs, @NotNull String output) {
        List<T> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(sampleCount(sample), output);
        P.reset();
    }

    @Test
    public void testMonomialOrders() {
        simpleProviderHelper(P.monomialOrders(), "QBarRandomProvider_monomialOrders");
    }

    private static void exponentVectorHelper(
            @NotNull Iterable<ExponentVector> xs,
            @NotNull String output,
            double degreeMean
    ) {
        List<ExponentVector> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(ExponentVector::degree, sample))), degreeMean);
    }

    private static void exponentVectors_helper(int scale, @NotNull String output, double degreeMean) {
        exponentVectorHelper(P.withScale(scale).exponentVectors(), output, degreeMean);
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
        exponentVectors_helper(1, "QBarRandomProvider_exponentVectors_i", 1.0010019999980002);
        exponentVectors_helper(8, "QBarRandomProvider_exponentVectors_ii", 24.01019300000956);
        exponentVectors_helper(32, "QBarRandomProvider_exponentVectors_iii", 192.2389910000015);
        exponentVectors_fail_helper(0);
        exponentVectors_fail_helper(-1);
    }

    private static void exponentVectors_List_Variable_helper(
            int scale,
            @NotNull String variables,
            @NotNull String output,
            double degreeMean
    ) {
        exponentVectorHelper(P.withScale(scale).exponentVectors(readVariableList(variables)), output, degreeMean);
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
        exponentVectors_List_Variable_helper(1, "[]", "QBarRandomProvider_exponentVectors_List_Variable_i", 0.0);
        exponentVectors_List_Variable_helper(8, "[]", "QBarRandomProvider_exponentVectors_List_Variable_ii", 0.0);
        exponentVectors_List_Variable_helper(
                1,
                "[a]",
                "QBarRandomProvider_exponentVectors_List_Variable_iii",
                1.0008359999977228
        );
        exponentVectors_List_Variable_helper(
                8,
                "[a]",
                "QBarRandomProvider_exponentVectors_List_Variable_iv",
                7.996049000016875
        );
        exponentVectors_List_Variable_helper(
                1,
                "[x, y]",
                "QBarRandomProvider_exponentVectors_List_Variable_v",
                1.9999819999876247
        );
        exponentVectors_List_Variable_helper(
                8,
                "[x, y]",
                "QBarRandomProvider_exponentVectors_List_Variable_vi",
                15.985479999996784
        );
        exponentVectors_List_Variable_helper(
                1,
                "[a, b, c]",
                "QBarRandomProvider_exponentVectors_List_Variable_vii",
                3.0025289999869127
        );
        exponentVectors_List_Variable_helper(
                8,
                "[a, b, c]",
                "QBarRandomProvider_exponentVectors_List_Variable_viii",
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
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        List<MultivariatePolynomial> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(MultivariatePolynomial::termCount, sample))), meanTermCount);
        aeq(meanOfIntegers(toList(map(MultivariatePolynomial::degree, sample))), meanDegree);
        aeq(meanOfIntegers(toList(concatMap(ts -> map(t -> t.b.bitLength(), ts), sample))), meanCoefficientBitSize);
        P.reset();
    }

    private static void multivariatePolynomials_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanTermCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        multivariatePolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale).
                        multivariatePolynomials(),
                output,
                meanTermCount,
                meanDegree,
                meanCoefficientBitSize
        );
        P.reset();
    }

    private static void multivariatePolynomials_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .multivariatePolynomials();
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
                1,
                2,
                "QBarRandomProvider_multivariatePolynomials_i",
                0.7250899999997853,
                0.34225999999973494,
                1.6671999338014827
        );
        multivariatePolynomials_helper(
                5,
                4,
                3,
                "QBarRandomProvider_multivariatePolynomials_ii",
                1.9789300000000163,
                12.226700000004135,
                4.8293370659824255
        );
        multivariatePolynomials_helper(
                10,
                5,
                8,
                "QBarRandomProvider_multivariatePolynomials_iii",
                5.723920000000159,
                38.116899999997244,
                9.913007169906928
        );

        multivariatePolynomials_fail_helper(2, 1, 1);
        multivariatePolynomials_fail_helper(2, 0, 2);
        multivariatePolynomials_fail_helper(1, 1, 2);
    }

    private static void multivariatePolynomials_List_Variable_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String variables,
            @NotNull String output,
            double meanTermCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        multivariatePolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .multivariatePolynomials(readVariableList(variables)),
                output,
                meanTermCount,
                meanDegree,
                meanCoefficientBitSize
        );
        P.reset();
    }

    private static void multivariatePolynomials_List_Variable_fail_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String variables
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .multivariatePolynomials(readVariableListWithNulls(variables));
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMultivariatePolynomials_List_Variable() {
        multivariatePolynomials_List_Variable_helper(
                2,
                1,
                2,
                "[]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_i",
                0.667929999999595,
                -0.3320700000001913,
                2.748207147455013
        );
        multivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_ii",
                0.9090499999984977,
                -0.09094999999999744,
                10.947516638247059
        );
        multivariatePolynomials_List_Variable_helper(
                2,
                1,
                2,
                "[a]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_iii",
                0.7632599999997848,
                0.2646199999998356,
                1.6640070225082857
        );
        multivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[a]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_iv",
                5.387540000000384,
                19.702560000001554,
                9.928789391778363
        );
        multivariatePolynomials_List_Variable_helper(
                2,
                1,
                2,
                "[x, y]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_v",
                0.9100499999996577,
                0.8940900000006846,
                1.660117575956004
        );
        multivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[x, y]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_vi",
                7.064559999999852,
                32.12952000000327,
                9.908040982025039
        );
        multivariatePolynomials_List_Variable_helper(
                2,
                1,
                2,
                "[a, b, c]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_vii",
                0.9648699999996267,
                1.4850999999995709,
                1.66630737819548
        );
        multivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[a, b, c]",
                "QBarRandomProvider_multivariatePolynomials_List_Variable_viii",
                7.172659999999885,
                43.42678999999441,
                9.902224558273495
        );
        multivariatePolynomials_List_Variable_fail_helper(1, 1, 2, "[]");
        multivariatePolynomials_List_Variable_fail_helper(2, 0, 2, "[]");
        multivariatePolynomials_List_Variable_fail_helper(2, 1, 1, "[]");
        multivariatePolynomials_List_Variable_fail_helper(1, 1, 2, "[a]");
        multivariatePolynomials_List_Variable_fail_helper(2, 0, 2, "[a]");
        multivariatePolynomials_List_Variable_fail_helper(2, 1, 1, "[a]");
    }

    private static void algebraics_helper(
            @NotNull Iterable<Algebraic> input,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        List<Algebraic> sample = toList(take(DEFAULT_SAMPLE_SIZE / 100, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(Algebraic::degree, sample))), meanDegree);
        aeq(
                meanOfIntegers(toList(concatMap(p -> map(BigInteger::bitLength, p.minimalPolynomial()), sample))),
                meanCoefficientBitSize
        );
        aeq(meanOfAlgebraics(sample), meanValue);
        P.reset();
    }

    private static void positiveAlgebraics_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).positiveAlgebraics(degree),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void positiveAlgebraics_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).positiveAlgebraics(degree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositiveAlgebraics_int() {
        positiveAlgebraics_int_helper(
                1,
                1,
                "QBarRandomProvider_positiveAlgebraics_int_i",
                0.9999999999999062,
                1.5375500000002322,
                6.315925658558994
        );
        positiveAlgebraics_int_helper(
                5,
                1,
                "QBarRandomProvider_positiveAlgebraics_int_ii",
                0.9999999999999062,
                5.578900000000063,
                1.8103721442127808E11
        );
        positiveAlgebraics_int_helper(
                1,
                2,
                "QBarRandomProvider_positiveAlgebraics_int_iii",
                1.9999999999998124,
                1.62333333333318,
                5.42559674046746
        );
        positiveAlgebraics_int_helper(
                5,
                2,
                "QBarRandomProvider_positiveAlgebraics_int_iv",
                1.9999999999998124,
                5.802966666668402,
                6.734225118513345E11
        );
        positiveAlgebraics_int_helper(
                1,
                3,
                "QBarRandomProvider_positiveAlgebraics_int_v",
                3.0000000000004805,
                1.3488000000002673,
                3.7230896778068794
        );
        positiveAlgebraics_int_helper(
                5,
                3,
                "QBarRandomProvider_positiveAlgebraics_int_vi",
                3.0000000000004805,
                5.455049999999257,
                8.64470511038894E8
        );
        positiveAlgebraics_int_fail_helper(0, 1);
        positiveAlgebraics_int_fail_helper(1, 0);
    }

    private static void positiveAlgebraics_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).positiveAlgebraics(),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void positiveAlgebraics_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).positiveAlgebraics();
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testPositiveAlgebraics() {
        positiveAlgebraics_helper(
                1,
                4,
                "QBarRandomProvider_positiveAlgebraics_i",
                1.9975999999998322,
                1.4489925273554884,
                35.35899085466008
        );
        positiveAlgebraics_helper(
                5,
                6,
                "QBarRandomProvider_positiveAlgebraics_ii",
                2.9775000000002008,
                5.409729729729074,
                5.1291800819379814E10
        );
        positiveAlgebraics_fail_helper(0, 4);
        positiveAlgebraics_fail_helper(1, 3);
    }

    private static void negativeAlgebraics_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).negativeAlgebraics(degree),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void negativeAlgebraics_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).negativeAlgebraics(degree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNegativeAlgebraics_int() {
        negativeAlgebraics_int_helper(
                1,
                1,
                "QBarRandomProvider_negativeAlgebraics_int_i",
                0.9999999999999062,
                1.8904500000005682,
                -6.315925658558994
        );
        negativeAlgebraics_int_helper(
                5,
                1,
                "QBarRandomProvider_negativeAlgebraics_int_ii",
                0.9999999999999062,
                5.741199999999953,
                -1.8103721442127808E11
        );
        negativeAlgebraics_int_helper(
                1,
                2,
                "QBarRandomProvider_negativeAlgebraics_int_iii",
                1.9999999999998124,
                1.6303666666665126,
                -5.42559674046746
        );
        negativeAlgebraics_int_helper(
                5,
                2,
                "QBarRandomProvider_negativeAlgebraics_int_iv",
                1.9999999999998124,
                5.806200000001735,
                -6.734225118513345E11
        );
        negativeAlgebraics_int_helper(
                1,
                3,
                "QBarRandomProvider_negativeAlgebraics_int_v",
                3.0000000000004805,
                1.4807000000002934,
                -3.7230896778068794
        );
        negativeAlgebraics_int_helper(
                5,
                3,
                "QBarRandomProvider_negativeAlgebraics_int_vi",
                3.0000000000004805,
                5.495149999999198,
                -8.64470511038894E8
        );
        negativeAlgebraics_int_fail_helper(0, 1);
        negativeAlgebraics_int_fail_helper(1, 0);
    }

    private static void negativeAlgebraics_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).negativeAlgebraics(),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void negativeAlgebraics_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).negativeAlgebraics();
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNegativeAlgebraics() {
        negativeAlgebraics_helper(
                1,
                4,
                "QBarRandomProvider_negativeAlgebraics_i",
                1.9975999999998322,
                1.6003469442224292,
                -35.35899085466008
        );
        negativeAlgebraics_helper(
                5,
                6,
                "QBarRandomProvider_negativeAlgebraics_ii",
                2.9775000000002008,
                5.447316153362046,
                -5.1291800819379814E10
        );
        negativeAlgebraics_fail_helper(0, 4);
        negativeAlgebraics_fail_helper(1, 3);
    }

    private static void nonzeroAlgebraics_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).nonzeroAlgebraics(degree),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void nonzeroAlgebraics_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).nonzeroAlgebraics(degree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNonzeroAlgebraics_int() {
        nonzeroAlgebraics_int_helper(
                1,
                1,
                "QBarRandomProvider_nonzeroAlgebraics_int_i",
                0.9999999999999062,
                1.7221500000003995,
                0.8935028033939707
        );
        nonzeroAlgebraics_int_helper(
                5,
                1,
                "QBarRandomProvider_nonzeroAlgebraics_int_ii",
                0.9999999999999062,
                5.564949999999988,
                1.7969149681278552E11
        );
        nonzeroAlgebraics_int_helper(
                1,
                2,
                "QBarRandomProvider_nonzeroAlgebraics_int_iii",
                1.9999999999998124,
                1.6340999999998453,
                -1.3432208068582625
        );
        nonzeroAlgebraics_int_helper(
                5,
                2,
                "QBarRandomProvider_nonzeroAlgebraics_int_iv",
                1.9999999999998124,
                5.7860000000016685,
                -3.434101876855312E14
        );
        nonzeroAlgebraics_int_helper(
                1,
                3,
                "QBarRandomProvider_nonzeroAlgebraics_int_v",
                3.0000000000004805,
                1.4121750000002737,
                0.8349869306633381
        );
        nonzeroAlgebraics_int_helper(
                5,
                3,
                "QBarRandomProvider_nonzeroAlgebraics_int_vi",
                3.0000000000004805,
                5.427124999999181,
                -3.996771507650188E9
        );
        nonzeroAlgebraics_int_fail_helper(0, 1);
        nonzeroAlgebraics_int_fail_helper(1, 0);
    }

    private static void nonzeroAlgebraics_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).nonzeroAlgebraics(),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void nonzeroAlgebraics_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).nonzeroAlgebraics();
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNonzeroAlgebraics() {
        nonzeroAlgebraics_helper(
                1,
                4,
                "QBarRandomProvider_nonzeroAlgebraics_i",
                2.185799999999937,
                1.508035658233894,
                2.805596585809969
        );
        nonzeroAlgebraics_helper(
                5,
                6,
                "QBarRandomProvider_nonzeroAlgebraics_ii",
                3.0289000000002093,
                5.422050683809328,
                1.069223599469271E15
        );
        nonzeroAlgebraics_fail_helper(0, 4);
        nonzeroAlgebraics_fail_helper(1, 3);
    }

    private static void algebraics_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).algebraics(degree),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void algebraics_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).algebraics(degree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testAlgebraics_int() {
        algebraics_int_helper(
                1,
                1,
                "QBarRandomProvider_algebraics_int_i",
                0.9999999999999062,
                1.2992500000001745,
                0.9995577505177307
        );
        algebraics_int_helper(
                5,
                1,
                "QBarRandomProvider_algebraics_int_ii",
                0.9999999999999062,
                5.323499999999987,
                1.796919469664356E11
        );
        algebraics_int_helper(
                1,
                2,
                "QBarRandomProvider_algebraics_int_iii",
                1.9999999999998124,
                1.6340999999998453,
                -1.3432208068582625
        );
        algebraics_int_helper(
                5,
                2,
                "QBarRandomProvider_algebraics_int_iv",
                1.9999999999998124,
                5.7860000000016685,
                -3.434101876855312E14
        );
        algebraics_int_helper(
                1,
                3,
                "QBarRandomProvider_algebraics_int_v",
                3.0000000000004805,
                1.4121750000002737,
                0.8349869306633381
        );
        algebraics_int_helper(
                5,
                3,
                "QBarRandomProvider_algebraics_int_vi",
                3.0000000000004805,
                5.427124999999181,
                -3.996771507650188E9
        );
        algebraics_int_fail_helper(0, 1);
        algebraics_int_fail_helper(1, 0);
    }

    private static void algebraics_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).algebraics(),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void algebraics_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).algebraics();
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testAlgebraics() {
        algebraics_helper(
                1,
                4,
                "QBarRandomProvider_algebraics_i",
                1.9695999999998353,
                1.3931505926726353,
                0.020347302119350985
        );
        algebraics_helper(
                5,
                6,
                "QBarRandomProvider_algebraics_ii",
                3.0044000000002047,
                5.37798421736037,
                1.0692235994147452E15
        );
        algebraics_fail_helper(0, 4);
        algebraics_fail_helper(1, 3);
    }

    private static void nonNegativeAlgebraicsLessThanOne_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).nonNegativeAlgebraicsLessThanOne(degree),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void nonNegativeAlgebraicsLessThanOne_int_fail_helper(int scale, int degree) {
        try {
            P.withScale(scale).nonNegativeAlgebraicsLessThanOne(degree);
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNonNegativeAlgebraicsLessThanOne_int() {
        nonNegativeAlgebraicsLessThanOne_int_helper(
                1,
                1,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_int_i",
                0.9999999999999062,
                0.8129999999999316,
                0.07732253219575332
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                5,
                1,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_int_ii",
                0.9999999999999062,
                4.739450000000113,
                0.13594263152384084
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                1,
                2,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_int_iii",
                1.9999999999998124,
                1.5923999999998508,
                0.4365036437757487
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                5,
                2,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_int_iv",
                1.9999999999998124,
                5.77880000000158,
                0.1939323481207809
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                1,
                3,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_int_v",
                3.0000000000004805,
                1.334525000000266,
                0.5251921484950018
        );
        nonNegativeAlgebraicsLessThanOne_int_helper(
                5,
                3,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_int_vi",
                3.0000000000004805,
                5.384199999999304,
                0.2333289111213644
        );
        nonNegativeAlgebraicsLessThanOne_int_fail_helper(0, 1);
        nonNegativeAlgebraicsLessThanOne_int_fail_helper(1, 0);
    }

    private static void nonNegativeAlgebraicsLessThanOne_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).nonNegativeAlgebraicsLessThanOne(),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void nonNegativeAlgebraicsLessThanOne_fail_helper(int scale, int secondaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).nonNegativeAlgebraicsLessThanOne();
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testNonNegativeAlgebraicsLessThanOne() {
        nonNegativeAlgebraicsLessThanOne_helper(
                1,
                4,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_i",
                1.9699999999998343,
                1.1894276094277596,
                0.2781035980325599
        );
        nonNegativeAlgebraicsLessThanOne_helper(
                5,
                6,
                "QBarRandomProvider_nonNegativeAlgebraicsLessThanOne_ii",
                2.9635000000001956,
                5.271704301752999,
                0.20190432263300273
        );
        nonNegativeAlgebraicsLessThanOne_fail_helper(0, 4);
        nonNegativeAlgebraicsLessThanOne_fail_helper(1, 3);
    }

    private static double meanOfIntegers(@NotNull List<Integer> xs) {
        int size = xs.size();
        return sumDouble(map(i -> (double) i / size, xs));
    }

    private static double meanOfRationals(@NotNull List<Rational> xs) {
        int size = xs.size();
        return sumDouble(map(r -> r.doubleValue() / size, xs));
    }

    private static double meanOfAlgebraics(@NotNull List<Algebraic> xs) {
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
