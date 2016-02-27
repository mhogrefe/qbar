package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.qbar.testing.QBarTesting;
import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

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
        QBarTesting.aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        QBarTesting.aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        positiveRationals_helper(4, "QBarRandomProvider_positiveRationals_4", 10.860889705920956, 3.7748699999675455);
        positiveRationals_helper(
                16,
                "QBarRandomProvider_positiveRationals_16",
                1.5280779930028705E26,
                15.179776000001873
        );
        positiveRationals_helper(
                32,
                "QBarRandomProvider_positiveRationals_32",
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
        negativeRationals_helper(4, "QBarRandomProvider_negativeRationals_4", -10.860889705920956, 3.7748699999675455);
        negativeRationals_helper(
                16,
                "QBarRandomProvider_negativeRationals_16",
                -1.5280779930028705E26,
                15.179776000001873
        );
        negativeRationals_helper(
                32,
                "QBarRandomProvider_negativeRationals_32",
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
        nonzeroRationals_helper(4, "QBarRandomProvider_nonzeroRationals_4", 0.39737016317796847, 3.775415999967471);
        nonzeroRationals_helper(
                16,
                "QBarRandomProvider_nonzeroRationals_16",
                -7.897237376910241E21,
                15.175030000002636
        );
        nonzeroRationals_helper(
                32,
                "QBarRandomProvider_nonzeroRationals_32",
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
        rationals_helper(3, "QBarRandomProvider_rationals_3", -0.4022959305532067, 2.779838999989692);
        rationals_helper(16, "QBarRandomProvider_rationals_16", 1.6133070384934913E30, 15.814283999994494);
        rationals_helper(32, "QBarRandomProvider_rationals_32", -3.784942556617747E71, 31.82849000002398);
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
                "QBarRandomProvider_nonNegativeRationalsLessThanOne_4",
                0.25006056333167953,
                3.025887000003045
        );
        nonNegativeRationalsLessThanOne_helper(
                16,
                "QBarRandomProvider_nonNegativeRationalsLessThanOne_16",
                0.43779120952560585,
                14.830439999963309
        );
        nonNegativeRationalsLessThanOne_helper(
                32,
                "QBarRandomProvider_nonNegativeRationalsLessThanOne_32",
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
        rangeUp_Rational_helper(
                4,
                "0",
                "QBarRandomProvider_rangeUp_Rational_4_0",
                7.230916348852299,
                3.083149999986345
        );
        rangeUp_Rational_helper(
                32,
                "0",
                "QBarRandomProvider_rangeUp_Rational_32_0",
                2.30035078376369E59,
                30.041032000029976
        );
        rangeUp_Rational_helper(
                4,
                "1",
                "QBarRandomProvider_rangeUp_Rational_4_1",
                8.230916348876454,
                4.258722999974617
        );
        rangeUp_Rational_helper(
                32,
                "1",
                "QBarRandomProvider_rangeUp_Rational_32_1",
                2.30035078376369E59,
                37.66929400000752
        );
        rangeUp_Rational_helper(
                4,
                "2",
                "QBarRandomProvider_rangeUp_Rational_4_2",
                9.23091634887471,
                4.784373000006757
        );
        rangeUp_Rational_helper(
                32,
                "2",
                "QBarRandomProvider_rangeUp_Rational_32_2",
                2.30035078376369E59,
                38.19239900001273
        );
        rangeUp_Rational_helper(
                4,
                "-2",
                "QBarRandomProvider_rangeUp_Rational_4_-2",
                5.230916348808398,
                4.015000999973354
        );
        rangeUp_Rational_helper(
                32,
                "-2",
                "QBarRandomProvider_rangeUp_Rational_32_-2",
                2.30035078376369E59,
                37.930548000008635
        );
        rangeUp_Rational_helper(
                4,
                "5/3",
                "QBarRandomProvider_rangeUp_Rational_4_5/3",
                8.89758301551532,
                6.751185999974823
        );
        rangeUp_Rational_helper(
                32,
                "5/3",
                "QBarRandomProvider_rangeUp_Rational_32_5/3",
                2.30035078376369E59,
                40.232672000039464
        );
        rangeUp_Rational_helper(
                4,
                "-5/3",
                "QBarRandomProvider_rangeUp_Rational_4_-5/3",
                5.564249682144554,
                5.86250299994541
        );
        rangeUp_Rational_helper(
                32,
                "-5/3",
                "QBarRandomProvider_rangeUp_Rational_32_-5/3",
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
                "QBarRandomProvider_rangeDown_Rational_4_0",
                -7.230916348852299,
                3.083149999986345
        );
        rangeDown_Rational_helper(
                32,
                "0",
                "QBarRandomProvider_rangeDown_Rational_32_0",
                -2.30035078376369E59,
                30.041032000029976
        );
        rangeDown_Rational_helper(
                4,
                "1",
                "QBarRandomProvider_rangeDown_Rational_4_1",
                -6.230916348820594,
                3.3941509999804
        );
        rangeDown_Rational_helper(
                32,
                "1",
                "QBarRandomProvider_rangeDown_Rational_32_1",
                -2.30035078376369E59,
                37.41008700000551
        );
        rangeDown_Rational_helper(
                4,
                "2",
                "QBarRandomProvider_rangeDown_Rational_4_2",
                -5.230916348808398,
                4.015000999973354
        );
        rangeDown_Rational_helper(
                32,
                "2",
                "QBarRandomProvider_rangeDown_Rational_32_2",
                -2.30035078376369E59,
                37.930548000008635
        );
        rangeDown_Rational_helper(
                4,
                "-2",
                "QBarRandomProvider_rangeDown_Rational_4_-2",
                -9.23091634887471,
                4.784373000006757
        );
        rangeDown_Rational_helper(
                32,
                "-2",
                "QBarRandomProvider_rangeDown_Rational_32_-2",
                -2.30035078376369E59,
                38.19239900001273
        );
        rangeDown_Rational_helper(
                4,
                "5/3",
                "QBarRandomProvider_rangeDown_Rational_4_5/3",
                -5.564249682144554,
                5.86250299994541
        );
        rangeDown_Rational_helper(
                32,
                "5/3",
                "QBarRandomProvider_rangeDown_Rational_32_5/3",
                -2.30035078376369E59,
                39.967830000046504
        );
        rangeDown_Rational_helper(
                4,
                "-5/3",
                "QBarRandomProvider_rangeDown_Rational_4_-5/3",
                -8.89758301551532,
                6.751185999974823
        );
        rangeDown_Rational_helper(
                32,
                "-5/3",
                "QBarRandomProvider_rangeDown_Rational_32_-5/3",
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
                "QBarRandomProvider_range_Rational_Rational_4_0_0",
                0.0,
                1.000000000007918
        );
        range_Rational_Rational_helper(
                32,
                "0",
                "0",
                "QBarRandomProvider_range_Rational_Rational_32_0_0",
                0.0,
                1.000000000007918
        );
        range_Rational_Rational_helper(
                4,
                "5/3",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_4_5/3_5/3",
                1.6666666666766063,
                4.999999999895295
        );
        range_Rational_Rational_helper(
                32,
                "5/3",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_32_5/3_5/3",
                1.6666666666766063,
                4.999999999895295
        );
        range_Rational_Rational_helper(
                4,
                "0",
                "1",
                "QBarRandomProvider_range_Rational_Rational_4_0_1",
                0.43764761498431315,
                2.771136999989421
        );
        range_Rational_Rational_helper(
                32,
                "0",
                "1",
                "QBarRandomProvider_range_Rational_Rational_32_0_1",
                0.4855748611217715,
                29.795897000025235
        );
        range_Rational_Rational_helper(
                4,
                "-1",
                "0",
                "QBarRandomProvider_range_Rational_Rational_4_-1_0",
                -0.562352385009498,
                2.89502099998304
        );
        range_Rational_Rational_helper(
                32,
                "-1",
                "0",
                "QBarRandomProvider_range_Rational_Rational_32_-1_0",
                -0.5144251388771651,
                29.8250019999991
        );
        range_Rational_Rational_helper(
                4,
                "1/3",
                "1/2",
                "QBarRandomProvider_range_Rational_Rational_4_1/3_1/2",
                0.40627460249656705,
                5.237116000060525
        );
        range_Rational_Rational_helper(
                32,
                "1/3",
                "1/2",
                "QBarRandomProvider_range_Rational_Rational_32_1/3_1/2",
                0.41426247685353923,
                33.696076999997224
        );
        range_Rational_Rational_helper(
                4,
                "-1",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_4_-1_5/3",
                0.1670603066320713,
                4.350258999958008
        );
        range_Rational_Rational_helper(
                32,
                "-1",
                "5/3",
                "QBarRandomProvider_range_Rational_Rational_32_-1_5/3",
                0.29486629632591527,
                31.072729000007964
        );
        range_Rational_Rational_fail_helper(3, "0", "1");
        range_Rational_Rational_fail_helper(-1, "0", "1");
        range_Rational_Rational_fail_helper(4, "1", "0");
        range_Rational_Rational_fail_helper(4, "1/2", "1/3");
    }

    private static void intervalHelper(
            @NotNull Iterable<Interval> xs,
            @NotNull String output,
            @NotNull String topSampleCount,
            double bitSizeMean
    ) {
        List<Interval> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(Interval::bitLength, sample))), bitSizeMean);
    }

    private static void finitelyBoundedIntervals_helper(
            int meanBitSize,
            @NotNull String output,
            @NotNull String topSampleCount,
            double bitSizeMean
    ) {
        intervalHelper(P.withScale(meanBitSize).finitelyBoundedIntervals(), output, topSampleCount, bitSizeMean);
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
        finitelyBoundedIntervals_helper(
                6,
                "[[5, 221], [-1/2, 0], [0, 5/6], [0, 2], [-3/4, 0], [0, 1/6], [-14/3, 0], [-6, 0], [-1, 0]," +
                " [-16, -1/4], [0, 0], [0, 167/17], [0, 1], [0, 0], [0, 1/3], [-1, 1], [0, 10], [0, 0], [-5, 4/21]," +
                " [-1, 0], ...]",
                "{[0, 0]=224347, [0, 1]=55754, [-1, 0]=55558, [-1, 1]=14214, [0, 1/2]=14146, [1, 1]=14140," +
                " [-1/2, 0]=14088, [0, 3]=14072, [-3, 0]=14048, [-1, -1]=14002}",
                5.139956999987673
        );
        finitelyBoundedIntervals_helper(
                16,
                "[[-5/4, -1], [0, 1/2], [-1, 0], [-59/3, 1493/24932], [0, 3], [-6/1201, 5/19], [-24, -7/29], [0, 1]," +
                " [31/7787, 3/5], [1/21, 1/10], [-49/8, -2/937], [-3453, -10], [-739/31, 1/7], [-1/9, 3217/7]," +
                " [-5/11, 3/2], [-3, -2/13], [-14/3, 1/3], [-15, 1/87], [-42, 2/9], [-46/177, 11969], ...]",
                "{[0, 0]=11462, [-1, 0]=4695, [0, 1]=4595, [1, 1]=1879, [-1, -1]=1857, [0, 2]=1830, [-3, 0]=1828," +
                " [-2, 0]=1823, [-1, 1]=1805, [0, 1/2]=1791}",
                15.568145999994938
        );
        finitelyBoundedIntervals_helper(
                32,
                "[[-243045529/1963, -409/7232], [-47/15, 1/2], [-70/512703, 25056015375/8548], [-4/9, -1/7790]," +
                " [-238/29, 78/155], [-72/31, 1419/29086], [68578/49, 514016/3], [-2591362/23, 23]," +
                " [-1/122, 4266773419/17], [-455, -3656/177], [449/6, 3346], [0, 856/475357657], [-3, 3133/841]," +
                " [733/37, 2144/21], [-53/27, -15/53], [-2719/26, 5929/34], [3/332735, 26/5]," +
                " [-277/11, -1373/46121], [-3/19, 56820544/123], [-192790039594/15, 17/54], ...]",
                "{[0, 0]=1021, [-1, 0]=438, [0, 1]=419, [0, 2]=209, [0, 1/3]=208, [0, 1/2]=206, [-1/2, 0]=201," +
                " [-1, -1]=197, [0, 3]=195, [-1, 1]=190}",
                31.618885000011975
        );
        finitelyBoundedIntervals_fail_helper(5);
        finitelyBoundedIntervals_fail_helper(0);
        finitelyBoundedIntervals_fail_helper(-1);
    }

    private static void intervals_helper(
            int meanBitSize,
            @NotNull String output,
            @NotNull String topSampleCount,
            double bitSizeMean
    ) {
        intervalHelper(P.withScale(meanBitSize).intervals(), output, topSampleCount, bitSizeMean);
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
        intervals_helper(
                6,
                "[[1, 13], [0, 0], (-Infinity, 0], (-Infinity, Infinity), [-1/2, 0], (-Infinity, Infinity)," +
                " [-3/4, 0], [1/6, Infinity), [0, Infinity), (-Infinity, 1], (-Infinity, -1], [-1, 0]," +
                " (-Infinity, 0], [0, 6], (-Infinity, Infinity), [0, Infinity), (-Infinity, -2], (-Infinity, 0]," +
                " (-Infinity, 0], (-Infinity, Infinity), ...]",
                "{(-Infinity, Infinity)=136618, (-Infinity, 0]=98468, [0, Infinity)=97756, [0, 0]=70368," +
                " (-Infinity, -1]=24761, (-Infinity, 1]=24665, [-1, Infinity)=24632, [1, Infinity)=24487," +
                " [0, 1]=17600, [-1, 0]=17450}",
                3.146318999988329
        );
        intervals_helper(
                16,
                "[(-Infinity, 1/41], [-2/533, Infinity), [-221/3755, 1/18], (-Infinity, 2/13], [-1, Infinity)," +
                " [1493/24932, Infinity), (-Infinity, 1], [-1/6, 0], [0, 3], [-1/89, -1/1201], [3/19, 30/13]," +
                " [-1/29, 5/146], [-26/3, 0], (-Infinity, 1], [7/7787, 14/11], [3/5, 1], (-Infinity, 26]," +
                " [-49/8, 0], [31, Infinity), (-Infinity, -2/9], ...]",
                "{(-Infinity, Infinity)=25200, [0, Infinity)=13341, (-Infinity, 0]=13229, [0, 0]=7053," +
                " (-Infinity, -1]=5441, [-1, Infinity)=5387, (-Infinity, 1]=5376, [1, Infinity)=5369, [-1, 0]=2931," +
                " [0, 1]=2918}",
                12.45336999999764
        );
        intervals_helper(
                32,
                "[[-3/94, 5/4], [-31/15, 1/2], [-38/512703, 25056015375/8548], (-Infinity, -2/67], [-1/9, -1/7790]," +
                " [-15086/29, 410411/24], [1/7, 15017244/161], (-Infinity, -3/458722], [-10397738, -40/31]," +
                " [395/29086, 3217/571], [35810/49, 251872/3], [-1018498/23, 7], [-1/122, 4266773419/17]," +
                " [-584/177, 3346], (-Infinity, 23/475357657], [-3, -1/3], [573/841, 477/37], [-53/27, -3/53]," +
                " [-10/3, 3/25], [-1695/26, 3881/34], ...]",
                "{(-Infinity, Infinity)=6930, (-Infinity, 0]=2390, [0, Infinity)=2350, [1, Infinity)=1025," +
                " (-Infinity, -1]=1022, (-Infinity, 1]=1018, [-1, Infinity)=1006, [0, 0]=764, (-Infinity, 1/2]=496," +
                " (-Infinity, 2]=494}",
                28.10473700001665
        );
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
        rationalsIn_helper(4, "[0, 0]", "QBarRandomProvider_rationalsIn_4_[0,_0]", 0.0, 1.000000000007918);
        rationalsIn_helper(32, "[0, 0]", "QBarRandomProvider_rationalsIn_32_[0,_0]", 0.0, 1.000000000007918);
        rationalsIn_helper(
                4,
                "[1, 1]",
                "QBarRandomProvider_rationalsIn_4_[1,_1]",
                1.000000000007918,
                2.000000000015836
        );
        rationalsIn_helper(
                32,
                "[1, 1]",
                "QBarRandomProvider_rationalsIn_32_[1,_1]",
                1.000000000007918,
                2.000000000015836
        );
        rationalsIn_helper(
                4,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_rationalsIn_4_(-Infinity,_Infinity)",
                4459.875622663981,
                3.869968999988822
        );
        rationalsIn_helper(
                32,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_rationalsIn_32_(-Infinity,_Infinity)",
                -3.784942556617747E71,
                31.82849000002398
        );
        rationalsIn_helper(
                4,
                "[1, 4]",
                "QBarRandomProvider_rationalsIn_4_[1,_4]",
                2.3129428449434144,
                4.068563999969932
        );
        rationalsIn_helper(
                32,
                "[1, 4]",
                "QBarRandomProvider_rationalsIn_32_[1,_4]",
                2.456724583363463,
                31.33094400000747
        );
        rationalsIn_helper(
                4,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsIn_4_(-Infinity,_1/2]",
                -6.7309163488312524,
                4.3459919999995815
        );
        rationalsIn_helper(
                32,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsIn_32_(-Infinity,_1/2]",
                -2.30035078376369E59,
                37.958985000008944
        );
        rationalsIn_helper(
                4,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsIn_4_[1/2,_Infinity)",
                7.73091634876883,
                5.011827999992238
        );
        rationalsIn_helper(
                32,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsIn_32_[1/2,_Infinity)",
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
                "QBarRandomProvider_rationalsNotIn_4_[0,_0]",
                -1514.3998931402477,
                4.699111999987303
        );
        rationalsNotIn_helper(
                32,
                "[0, 0]",
                "QBarRandomProvider_rationalsNotIn_32_[0,_0]",
                -3.784942556617747E71,
                32.0148380000219
        );
        rationalsNotIn_helper(
                4,
                "[1, 1]",
                "QBarRandomProvider_rationalsNotIn_4_[1,_1]",
                3405.7680966854173,
                4.0221769999945876
        );
        rationalsNotIn_helper(
                32,
                "[1, 1]",
                "QBarRandomProvider_rationalsNotIn_32_[1,_1]",
                -3.784942556617747E71,
                31.911038000025872
        );
        rationalsNotIn_helper(
                4,
                "[1, 4]",
                "QBarRandomProvider_rationalsNotIn_4_[1,_4]",
                -69.41248621922566,
                4.310511999997506
        );
        rationalsNotIn_helper(
                32,
                "[1, 4]",
                "QBarRandomProvider_rationalsNotIn_32_[1,_4]",
                -4.8445650251622075E53,
                38.611619000014095
        );
        rationalsNotIn_helper(
                4,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsNotIn_4_(-Infinity,_1/2]",
                9.925816351081805,
                5.681415999961638
        );
        rationalsNotIn_helper(
                32,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_rationalsNotIn_32_(-Infinity,_1/2]",
                2.30035078376369E59,
                39.36069900002264
        );
        rationalsNotIn_helper(
                4,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsNotIn_4_[1/2,_Infinity)",
                -8.92581635109024,
                4.794851000002666
        );
        rationalsNotIn_helper(
                32,
                "[1/2, Infinity)",
                "QBarRandomProvider_rationalsNotIn_32_[1/2,_Infinity)",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        List<Vector> sample = toList(take(DEFAULT_SAMPLE_SIZE, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(Vector::dimension, sample))), meanDimension);
        aeq(meanOfIntegers(toList(concatMap(v -> map(BigInteger::bitLength, v), sample))), meanCoordinateBitSize);
        P.reset();
    }

    private static void vectors_int_helper(
            int scale,
            int dimension,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        vectors_helper(
                P.withScale(scale).vectors(dimension),
                output,
                topSampleCount,
                meanDimension,
                meanCoordinateBitSize
        );
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
        vectors_int_helper(
                1,
                0,
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}",
                0.0,
                0.0
        );
        vectors_int_helper(
                5,
                3,
                "[[21, 21, 1256222], [-233, -1, 0], [1, 9, -117], [-1661016, -6, -1], [-67, -39244, -576]," +
                " [-28, -38, -16989], [-46, 30, 1], [2, -6, 0], [0, -8, 2], [-1, 62, 0], [-4551, 1, 0], [9, 1, -92]," +
                " [-7, 1, -1], [-70, -3026, 1], [-181301872, 4, 54644], [-2404, 0, 0], [2, -1, -2], [-19, -4, 5]," +
                " [0, -64580, 41], [7818, -241920, -13], ...]",
                "{[0, 0, 0]=4724, [-1, 0, 0]=1980, [1, 0, 0]=1956, [0, -1, 0]=1955, [0, 0, -1]=1908, [0, 0, 1]=1893," +
                " [0, 1, 0]=1864, [-1, 0, 1]=876, [-1, 1, 0]=865, [0, 0, 3]=838}",
                2.9999999999775233,
                4.884982000065708
        );
        vectors_int_helper(
                10,
                8,
                "[[-47968091191, -337, 220, -117, -645698713, -4626764, -124, -66364]," +
                " [-437219, 566, 56, 1, -2, 1, -3, 14], [1224, 81, 2, -232, -3, 2, -1, -239]," +
                " [5896747328932606365, -2404, 0, 54, -16, -19, -4, -16532643]," +
                " [8293038, -909910, -622, -454294, 105065295, -4, 249, 5]," +
                " [368648822968, 3, 51089054789, 11, 0, -7, -289302, -1283]," +
                " [18, -2300, 1, 4, -49, -58148006, 47, -30]," +
                " [87, -506, -2, -111061034, 0, -739, 101342, 345112274]," +
                " [-8682, 16276, -17, 1510370, 131588190, -394953281, 3, 15]," +
                " [0, 3186328, -432, 7689363762084, -9, 5, -3015, 0]," +
                " [-20040, 3064121, -2, 3346, -1, 0, 3928, 152929]," +
                " [-8080881, 13297953072, -41349439, -1, -3, 0, -3, 1]," +
                " [3133, 1105224, 1597518, -13, -248, 3, -7, 7182], [-736, 1, 5, -434063, 2, 10025, -10, -510]," +
                " [-419, -12, -39554, -6, -21118, 9, 663885, 3]," +
                " [-789, 524, 25689, -3421, -3640006, -355615, -2304971396104, 31]," +
                " [-1, -7, 0, 1524767947173, -11, -330228993066, 7, 17], [6, 1, -231522, -27, -2, 50, 397143, 112]," +
                " [-23, 114, 629, 3, 60, 0, 4886, 32022602], [7, 27, -1344, -956783, -198077, 207, 0, 44117477702]," +
                " ...]",
                "{[-47968091191, -337, 220, -117, -645698713, -4626764, -124, -66364]=1," +
                " [-437219, 566, 56, 1, -2, 1, -3, 14]=1, [1224, 81, 2, -232, -3, 2, -1, -239]=1," +
                " [5896747328932606365, -2404, 0, 54, -16, -19, -4, -16532643]=1," +
                " [8293038, -909910, -622, -454294, 105065295, -4, 249, 5]=1," +
                " [368648822968, 3, 51089054789, 11, 0, -7, -289302, -1283]=1," +
                " [18, -2300, 1, 4, -49, -58148006, 47, -30]=1," +
                " [87, -506, -2, -111061034, 0, -739, 101342, 345112274]=1," +
                " [-8682, 16276, -17, 1510370, 131588190, -394953281, 3, 15]=1," +
                " [0, 3186328, -432, 7689363762084, -9, 5, -3015, 0]=1}",
                8.000000000063345,
                9.922074749910346
        );
        vectors_int_fail_helper(1, -1);
        vectors_int_fail_helper(0, 0);
    }

    private static void vectors_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        vectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).vectors(),
                output,
                topSampleCount,
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
        vectors_helper(
                1,
                1,
                "[[3, 85, 0], [0], [], [0, 3, 0], [0], [0], [1], [], [-2, 1], [], [], [], [0], [], [], [], [], []," +
                " [], [], ...]",
                "{[]=499792, [0]=124943, [0, 0]=31375, [1]=31261, [-1]=31175, [3]=8002, [1, 0]=7924, [-2]=7910," +
                " [0, 0, 0]=7851, [0, -1]=7828}",
                1.0006929999977097,
                0.8337352214893942
        );
        vectors_helper(
                5,
                3,
                "[[85, 1256222, -233, -1, 0, 1], [-240, -1661016, -6, -1, -67]," +
                " [-3737, -576, -28, -38, -16989, -46], [6], [4], [1, 0, -8, 2], [94, 0, -4551], [-1]," +
                " [4, -92, -7, 1], [-239], [-210, 1, -181301872, 4]," +
                " [-4452, 0, 0, 2, -1, -2, -19, -4, 5, 0, -64580, 41, 7818, -241920, -13, -238, -394, -122, 5, -1]," +
                " [1, -224, 1, -4, 633, 17], [0, -15, -13, 5, -2708], [-1], [1], [0], [47]," +
                " [38, 91, 11, 0, -7, 16127, -618, -15659, -1, 114, 223, 0], [52, -152], ...]",
                "{[]=249257, [0]=31028, [1]=13227, [-1]=12977, [3]=5462, [-3]=5433, [-2]=5359, [2]=5313," +
                " [0, 0]=4115, [-4]=2308}",
                3.00101199999147,
                4.8827315585850855
        );
        vectors_helper(
                32,
                8,
                "[[112685255093845, -20981157, -394044, -604470, 182, -3788, 1637, 201836, -232, -30," +
                " -59743696475830428383511, -293956, -189307490089308637090, 28955, 3348172294715720, 11, -2910742," +
                " -24835, 50, -508, -1466398006247, -46, -13562, -695976, 4819934, 5542398, -234," +
                " 9534886336953625471936400, -363877104, -91712856, 3]," +
                " [-3241761293, -1, -2974, 808289, 20996462030935, 2477023059, 31279287, 6240, 1915]," +
                " [14242780, -147386711184, -30509515310], [31177, 28088, -280884356308748050741099869955642846]," +
                " [30148888131318, -24354707416857, 4589433597664470]," +
                " [747, 15387, 0, -870085049018158987528554667, -440769353]," +
                " [1771, 354213973, -177017, -26700086386, -12261695942506, -5226619389982," +
                " -26164044723935471006011785430908281959257296841800], [-2312, 66], [1863609074]," +
                " [-21, 3533, 2640853904, -238223674434949, -2226596240743388127, 270474552951993, -7490, -180," +
                " 778760208529133742411612890940263260, 877624, 4937397919070268197576074897664947128, -145215," +
                " 17201896100941161198814, -1755, -59]," +
                " [391, 2238317906063783, -429, 40, -418444443, -147, 85257, 937, -56022]," +
                " [36099312811800304420, 154, 384966332, 549943, 1, -15115243678996539, 8641]," +
                " [10437, 488, -7577458850986978217, -1264, -114873277475663, 5452104," +
                " -594016125101088808832060264942027504116335596926564, 225357211879550424, 681190, 1683229699," +
                " 2663972, -10589134346, 117525398, 872051284356696732231572275, 7189772234, -488344, 2175870," +
                " 609281197727896401432736269590, -2, 24989403365040835132702950033040639860, 165, -16597]," +
                " [182099, 18, -10, 4882, -3850444, 634839268453353265487425588455314257, 0, 324901632], []," +
                " [-1169158301]," +
                " [1696870787, -936, 3, 3028852536, -2, -316, -2081, -65824, 31941758203, -1798563486," +
                " -686163800713560015, -36327313, 3604983285930, -3]," +
                " [-101421586673, 6084290, -1139190946650, 258843373182338179731772247]," +
                " [-723856176, -373860779532, 4, -4350514, 13113, -30, -48725, -14, 4939, 346611," +
                " 22657668673578743481669315318275, 323619155433539138, -2008339699722968147989446021, -26803, -91," +
                " 22, 7142464997215, -2]," +
                " [5, 9682071, 480733578, -7072171, -92794838, 266501333814584625267172, 1769496824093704, -1," +
                " 894753, -21558614, 891405023649, -7878220384814, 102, 1668, -940, 53278803552732], ...]",
                "{[]=110872, [0]=3041, [-1]=1505, [1]=1366, [-2]=704, [3]=686, [-3]=679, [2]=677, [-7]=365, [-4]=361}",
                8.007115000016897,
                31.973857750448634
        );
        vectors_fail_helper(0, 1);
        vectors_fail_helper(1, 0);
    }

    private static void vectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDimension,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        vectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).vectorsAtLeast(minDimension),
                output,
                topSampleCount,
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
        vectorsAtLeast_helper(
                1,
                1,
                0,
                "[[3, 85, 0], [0], [], [0, 3, 0], [0], [0], [1], [], [-2, 1], [], [], [], [0], [], [], [], [], []," +
                " [], [], ...]",
                "{[]=499792, [0]=124943, [0, 0]=31375, [1]=31261, [-1]=31175, [3]=8002, [1, 0]=7924, [-2]=7910," +
                " [0, 0, 0]=7851, [0, -1]=7828}",
                1.0006929999977097,
                0.8337352214893942
        );
        vectorsAtLeast_helper(
                5,
                3,
                0,
                "[[85, 1256222, -233, -1, 0, 1], [-240, -1661016, -6, -1, -67]," +
                " [-3737, -576, -28, -38, -16989, -46], [6], [4], [1, 0, -8, 2], [94, 0, -4551], [-1]," +
                " [4, -92, -7, 1], [-239], [-210, 1, -181301872, 4]," +
                " [-4452, 0, 0, 2, -1, -2, -19, -4, 5, 0, -64580, 41, 7818, -241920, -13, -238, -394, -122, 5, -1]," +
                " [1, -224, 1, -4, 633, 17], [0, -15, -13, 5, -2708], [-1], [1], [0], [47]," +
                " [38, 91, 11, 0, -7, 16127, -618, -15659, -1, 114, 223, 0], [52, -152], ...]",
                "{[]=249257, [0]=31028, [1]=13227, [-1]=12977, [3]=5462, [-3]=5433, [-2]=5359, [2]=5313," +
                " [0, 0]=4115, [-4]=2308}",
                3.00101199999147,
                4.8827315585850855
        );
        vectorsAtLeast_helper(
                5,
                3,
                1,
                "[[85, 1256222, -233, -1, 0], [0, 25], [-13, -1661016], [0, -2]," +
                " [-170316, -576, -28, -38, -16989, -46, 30], [4, -6], [0], [0, 2, -1, 62], [-4551, 1], [17]," +
                " [0, -92], [-3], [-6, -70, -3026], [-986608240, 4]," +
                " [-4452, 0, 0, 2, -1, -2, -19, -4, 5, 0, -64580, 41, 7818, -241920, -13]," +
                " [-1, -394, -122, 5, -1, 62], [-13, 1], [-1, 633], [-56, -15, -13, 5, -2708], [-1], ...]",
                "{[0]=55647, [-1]=23294, [1]=22999, [3]=9763, [-3]=9654, [-2]=9654, [2]=9577, [0, 0]=6291," +
                " [-7]=4025, [6]=4007}",
                3.0018729999898364,
                4.884003087404542
        );
        vectorsAtLeast_helper(
                5,
                3,
                2,
                "[[1, 21, 1256222, -233, -1], [0, 1], [5, -117], [-55384, -6, -1, -67, -39244, -576, -28, -38]," +
                " [-8797, -46], [6, 1, 2], [-1, 0, 0], [-4, 2], [0, 62], [0, -4551], [-1, 9, 1], [-28, -7, 1]," +
                " [0, -239], [-2002, 1, -181301872], [251252, -2404, 0, 0, 2], [0, -2], [-3, -4, 5, 0]," +
                " [-22652, 41], [3722, -241920], [1, -46, -394, -122, 5, -1], ...]",
                "{[0, 0]=13873, [0, -1]=5865, [0, 1]=5837, [-1, 0]=5832, [1, 0]=5636, [-3, 0]=2489, [1, 1]=2485," +
                " [1, -1]=2438, [-1, -1]=2407, [0, 2]=2394}",
                3.0005879999687126,
                4.88591636041098
        );
        vectorsAtLeast_helper(
                10,
                8,
                0,
                "[[-2129, 220, -117, -645698713, -4626764, -124, -66364, -437219, 566, 56, 1, -2, 1, -3, 14, 1224, " +
                "81, 2, -232, -3, 2, -1, -239, 5896747328932606365, -2404, 0, 54, -16, -19, -4, -16532643]," +
                " [-1434198, -622, -454294, 105065295, -4, 249, 5, 368648822968, 3, 51089054789, 11, 0, -7, -289302," +
                " -1283, 18, -2300, 1, 4], [-125256870, 47, -30, 87]," +
                " [-8, -111061034, 0, -739, 101342, 345112274, -8682]," +
                " [-49, 1510370, 131588190, -394953281, 3, 15, 0, 3186328, -432, 7689363762084, -9], [-7111, 0]," +
                " [3064121, -2, 3346, -1, 0, 3928, 152929, -8080881, 13297953072, -41349439, -1, -3], [], [7, 3133]," +
                " [0, 1597518, -13, -248, 3, -7, 7182, -736, 1, 5, -434063, 2, 10025, -10, -510, -419]," +
                " [-72322, -6, -21118, 9]," +
                " [13, -789, 524, 25689, -3421, -3640006, -355615, -2304971396104, 31, -1, -7, 0, 1524767947173," +
                " -11, -330228993066, 7, 17, 6, 1]," +
                " [-43, -2, 50, 397143, 112, -23, 114, 629, 3, 60, 0, 4886, 32022602, 7, 27]," +
                " [-1481071, -198077, 207, 0, 44117477702, -589, -375, 2, -13369365953]," +
                " [-435, -2743875, -51, -4663, -632, 36, -1684, -5, -127, 7, 34, 12544, -51, -281078474]," +
                " [-764745235361, -1543, -109310, -1, 22, 36587, 32359, -130, -15528, -117129, 22, 0, 0, -40, -1060," +
                " 65, 24, -2312], [5, -202206016023, -82699, -5, 973, -3, -51, 398077, 372]," +
                " [1, 12126258480, -11617]," +
                " [-1, -142246, 18553, -11821607, 486, -6158262286914, -1, -95, -155, -1, -146275, 0, 5, -14, -12," +
                " -6, -105, 6, -329, 5994968441128, 0, 203], [-337465087682, -79595301742, 13, 451, -23971], ...]",
                "{[]=111229, [0]=9009, [1]=4179, [-1]=4153, [-2]=1862, [3]=1856, [-3]=1852, [2]=1842, [-7]=873," +
                " [6]=849}",
                7.983070000016452,
                9.924974853054824
        );
        vectorsAtLeast_helper(
                10,
                8,
                3,
                "[[590028540, -593, 220, -117, -645698713, -4626764, -124, -66364]," +
                " [-427, 566, 56, 1, -2, 1, -3, 14, 1224, 81, 2, -232, -3, 2, -1, -239, 5896747328932606365]," +
                " [-3, 22, -16, -19, -4, -16532643, 8293038, -909910, -622, -454294, 105065295, -4, 249, 5]," +
                " [7871570104, 3, 51089054789, 11, 0, -7, -289302, -1283]," +
                " [-4348, 1, 4, -49, -58148006, 47, -30, 87, -506, -2]," +
                " [-98, 0, -739, 101342, 345112274, -8682, 16276, -17, 1510370, 131588190, -394953281, 3, 15, 0," +
                " 3186328, -432, 7689363762084, -9, 5, -3015, 0, -20040, 3064121, -2, 3346, -1, 0]," +
                " [344, 152929, -8080881, 13297953072, -41349439], [-7, 0, -3], [5181, 1105224, 1597518]," +
                " [-1016, 3, -7, 7182, -736, 1, 5], [-171919, 2, 10025, -10, -510]," +
                " [-6, -12, -39554, -6, -21118, 9, 663885, 3], [-149, 524, 25689, -3421]," +
                " [-3978, -355615, -2304971396104, 31, -1, -7, 0, 1524767947173, -11, -330228993066, 7, 17]," +
                " [7, -231522, -27, -2, 50, 397143, 112], [-1, 114, 629, 3, 60, 0, 4886, 32022602, 7, 27]," +
                " [-1481071, -198077, 207, 0, 44117477702, -589, -375, 2, -13369365953, 19882, -435, -2743875, -51]," +
                " [-567, -632, 36, -1684, -5, -127], [34, 12544, -51, -281078474, -2322630]," +
                " [414322270, -1543, -109310, -1, 22, 36587, 32359, -130, -15528, -117129, 22], ...]",
                "{[0, 0, 0]=127, [0, -1, 0]=67, [-1, 0, 0]=65, [0, 1, 0]=59, [0, 0, 1]=52, [0, 0, -1]=50," +
                " [1, 0, 0]=48, [-1, 0, 1]=37, [0, 1, 1]=34, [1, 0, -1]=34}",
                8.003411000000233,
                9.922824405726274
        );
        vectorsAtLeast_helper(
                10,
                8,
                7,
                "[[-2870934583, -337, 220, -117, -645698713, -4626764, -124, -66364, -437219, 566]," +
                " [8, 1, -2, 1, -3, 14, 1224, 81], [-872, -3, 2, -1, -239, 5896747328932606365, -2404, 0, 54]," +
                " [-8, -19, -4, -16532643, 8293038, -909910, -622]," +
                " [-192150, 105065295, -4, 249, 5, 368648822968, 3, 51089054789]," +
                " [7, 0, -7, -289302, -1283, 18, -2300, 1]," +
                " [-152, -58148006, 47, -30, 87, -506, -2, -111061034, 0, -739, 101342]," +
                " [210894546, -8682, 16276, -17, 1510370, 131588190, -394953281]," +
                " [3, 15, 0, 3186328, -432, 7689363762084, -9], [1, -3015, 0, -20040, 3064121, -2, 3346, -1]," +
                " [5976, 152929, -8080881, 13297953072, -41349439, -1, -3]," +
                " [-3, 1, 3133, 1105224, 1597518, -13, -248]," +
                " [-15, 7182, -736, 1, 5, -434063, 2, 10025, -10, -510, -419]," +
                " [-4, -39554, -6, -21118, 9, 663885, 3]," +
                " [-789, 524, 25689, -3421, -3640006, -355615, -2304971396104, 31]," +
                " [0, -11, 0, 1524767947173, -11, -330228993066, 7], [9, 6, 1, -231522, -27, -2, 50]," +
                " [69463, 112, -23, 114, 629, 3, 60, 0]," +
                " [790, 32022602, 7, 27, -1344, -956783, -198077, 207, 0, 44117477702]," +
                " [-333, -375, 2, -13369365953, 19882, -435, -2743875], ...]",
                "{[-2870934583, -337, 220, -117, -645698713, -4626764, -124, -66364, -437219, 566]=1," +
                " [8, 1, -2, 1, -3, 14, 1224, 81]=1, [-872, -3, 2, -1, -239, 5896747328932606365, -2404, 0, 54]=1," +
                " [-8, -19, -4, -16532643, 8293038, -909910, -622]=1," +
                " [-192150, 105065295, -4, 249, 5, 368648822968, 3, 51089054789]=1," +
                " [7, 0, -7, -289302, -1283, 18, -2300, 1]=1," +
                " [-152, -58148006, 47, -30, 87, -506, -2, -111061034, 0, -739, 101342]=1," +
                " [210894546, -8682, 16276, -17, 1510370, 131588190, -394953281]=1," +
                " [3, 15, 0, 3186328, -432, 7689363762084, -9]=1, [1, -3015, 0, -20040, 3064121, -2, 3346, -1]=1}",
                8.00086800006071,
                9.92217406943892
        );
        vectorsAtLeast_fail_helper(1, 1, 1);
        vectorsAtLeast_fail_helper(1, 1, 2);
        vectorsAtLeast_fail_helper(0, 1, 0);
        vectorsAtLeast_fail_helper(3, 2, -1);
    }

    private static void rationalVectors_helper(
            @NotNull Iterable<RationalVector> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        List<RationalVector> sample = toList(take(DEFAULT_SAMPLE_SIZE, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(RationalVector::dimension, sample))), meanDimension);
        aeq(meanOfIntegers(toList(concatMap(v -> map(Rational::bitLength, v), sample))), meanCoordinateBitSize);
        P.reset();
    }

    private static void rationalVectors_int_helper(
            int scale,
            int dimension,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).rationalVectors(dimension),
                output,
                topSampleCount,
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
        rationalVectors_int_helper(
                3,
                0,
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}",
                0.0,
                0.0
        );
        rationalVectors_int_helper(
                5,
                3,
                "[[1/36, -1, -29/4], [-1, 5/2, -53/47], [1/89, -2, -5/6], [-4, -22, -2/683], [0, -1/50, 0]," +
                " [-4, -7/2, 1/19], [-1/2, 0, -1/6], [-1/367, 17/10, 0], [12757/4452, 2, -1/16], [-1/4, -1, -6268]," +
                " [3, -718/59937, 1], [-1/38, -2/25, 1], [-5/6, 2, 14], [17/24, 1/7, -1], [0, 1, 15/619]," +
                " [1, 2, -1/7], [-1/7, -29/19562, 0], [14/3, 0, 10], [1/4, 1/6, -9/4], [8, 16, -1/6], ...]",
                "{[0, 0, 0]=4772, [0, 0, 1]=1645, [0, 1, 0]=1621, [1, 0, 0]=1612, [-1, 0, 0]=1603, [0, 0, -1]=1597," +
                " [0, -1, 0]=1591, [3, 0, 0]=583, [1, 1, 0]=575, [0, 1, 1]=570}",
                2.9999999999775233,
                4.807125333432253
        );
        rationalVectors_int_helper(
                10,
                8,
                "[[21/13, 2304798/125, -2, 1/25, -117/219224, -67/5785, -70/8797, -10]," +
                " [2/3, 62, -4551, 2/227, -239/978, 1/47084144, 0, -2/35]," +
                " [-8/5, -1/6268, -504064/13, -238/5, -8/249, -15/13, 0, 0]," +
                " [0, 3/238, 122224/59, 19, -7/3839, -1130/15659, 479, 1/12]," +
                " [2/47, 438, 11, -98, 1/6, 1/926, -97/41, 1/15], [-30, -1/5, 139/9, -70/167, 3, 2/23, 23/7, 1/37]," +
                " [2260, -1/5, -49/5, 23/3, -7, -7, 49465/2, 9/13]," +
                " [1, 0, 1/344, 3954017/6454, -3, 13/31, -26/1537, -15]," +
                " [1/3, -841, -1/8, 13/53, 5/4, -21, 3/1760, 0], [13/5, 6312/23, 1/3, -1/7, -34/15, -1, 20, 1/9]," +
                " [5395/2, 3, -149/7, -125/308, 1/69, -3618, -5656/31, 1/11]," +
                " [1/59, 33, -9/76, 1, -1, 246/48401, 3/112, 111]," +
                " [-1/4, 19/11, 237/832, 1/4, 14, 3/5, -58/111, -2/4079803]," +
                " [5, -12, -5/567, -1/3, 21, 280/11, -20672/9, 1/519]," +
                " [-15/1706, -1/22, -2/31, 1/3, -43124/2505, 3/2, -1, -3/2]," +
                " [-4, 2/11, 3/3361, -3/11075, 0, -1/2, 53/9, 1/78]," +
                " [-383, -1/40, 1/2, 0, -30725/11, -1/46542, -2588/93967625, -1]," +
                " [-6299/2, -1083, 5/7, -3, -5/14, -13/3473, -729/2, 0]," +
                " [1/454, -78, 186, 1/63, -40, 37, -9/2, -893924/13]," +
                " [13, -1, 17, 79/3, 7/131, -3239437/3, 71/2, 0], ...]",
                "{[21/13, 2304798/125, -2, 1/25, -117/219224, -67/5785, -70/8797, -10]=1," +
                " [2/3, 62, -4551, 2/227, -239/978, 1/47084144, 0, -2/35]=1," +
                " [-8/5, -1/6268, -504064/13, -238/5, -8/249, -15/13, 0, 0]=1," +
                " [0, 3/238, 122224/59, 19, -7/3839, -1130/15659, 479, 1/12]=1," +
                " [2/47, 438, 11, -98, 1/6, 1/926, -97/41, 1/15]=1," +
                " [-30, -1/5, 139/9, -70/167, 3, 2/23, 23/7, 1/37]=1," +
                " [2260, -1/5, -49/5, 23/3, -7, -7, 49465/2, 9/13]=1," +
                " [1, 0, 1/344, 3954017/6454, -3, 13/31, -26/1537, -15]=1," +
                " [1/3, -841, -1/8, 13/53, 5/4, -21, 3/1760, 0]=1, [13/5, 6312/23, 1/3, -1/7, -34/15, -1, 20, 1/9]=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalVectors(),
                output,
                topSampleCount,
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
        rationalVectors_helper(
                3,
                1,
                "[[3/221, 1, 1/2], [], [3, -2/3], [], [], [3], [], [0], [], [0], [], [], [], [], [], [], [], [0]," +
                " [0, -3/4], [], ...]",
                "{[]=500592, [0]=89734, [1]=22304, [-1]=22152, [0, 0]=15992, [3]=5725, [2]=5665, [1/2]=5635," +
                " [-1/3]=5629, [-3]=5622}",
                0.9992619999977276,
                2.7824344366226037
        );
        rationalVectors_helper(
                5,
                3,
                "[[10/3, -20, -1/10, -29/4, -1, 5/2], [-89/2, -1/427, -5/6, -4, -22, -2/683, 0], [0, -4, -7/2]," +
                " [19, 2], [0, -1/6, -1/367, 17/10, 0]," +
                " [-4452, 1, -1/4, -1, -6268, 3, -718/59937, 1, -1/38, -2/25, 1, -5/6, 2, 14, 17/24, 1/7, -1, 0, 1," +
                " 15/619, 1], [], [2, -1/7, -1/7, -29/19562, 0], [0, 10, 1/4, 1/6, -9/4], [], [-1/119, 8]," +
                " [0, -1/6, 11/2, -24/47, -2/11], [-7/2, 1/10, 3], [-7/41]," +
                " [1/3, 0, 0, 0, -17, 1/3, 0, 0, -3/11, 1/2, -1, -22/3, -3623/3], [1/2, -2/5], [1], [7/87], [-3/13]," +
                " [-1/2, 2/5, 468, -26], ...]",
                "{[]=250130, [0]=31725, [1]=10514, [-1]=10460, [0, 0]=4066, [1/3]=3614, [-2]=3585, [-1/2]=3511," +
                " [3]=3510, [-3]=3479}",
                3.0012729999915444,
                4.8070228865365285
        );
        rationalVectors_helper(
                32,
                8,
                "[[-15747517/2734092316245, -28331/46647, -232/15, 1/2, 276254993974227357/8548, -1/211, 12/35," +
                " -22652/243969555, 148267/24, 4379/132063031896244604, 3/533422527609160, 3, -1/122038962404," +
                " -33/712, -65857659355/672, -2268840, 16722136057826907622552/7, 15," +
                " -1706054384/1080390895815402467, -9/17, -11207, -3, -156/5, 2269/945835525, -5/1016, 7/31," +
                " -4688/65165, -1818/329, 3/726476, -60543402068/171, 253/62474849347023913473]," +
                " [-1928/233, 5604689743/6127, 235/5749], [124, -3737/84262208]," +
                " [2/217270739949325, -333/631, -2631947713/11153542, -5/207, 2304/827, -146860746/326157911," +
                " -361027568105/8283961]," +
                " [-151456133/1102, 3/221919869658758635, -365558/52611, -1/219, -1018454567/1766," +
                " 177327876612602193/138109844, 0], [9831/9545, -77060316718/843]," +
                " [-110263446, -5976420267/2755, -41956/3409, 4/6681, 358741282/10368272926801]," +
                " [767343046307022877, -5/591, -22/5], [-89801/53077, -4/127, -6]," +
                " [1/4, 23465/4, 205/218, -213070534709022875/702, 2/3470639612785, 17/860, -197069913331771/6," +
                " 197/6665, 86543021/35, -112/75533, -36406879723/227065385, 7/1459746, 1/43, -45/12438862, -2/635]," +
                " [47/983125734144, 36/457, -13165/11, -6720, -3098148526549/2439, -28085047/21652, 3/19315," +
                " 340862/3809, 10601/128827517, 158/38989, 447082221779799, 38262936027245/7717, -73/144," +
                " 44090397799/24, -3717923047/12, 20273/5, -3/53]," +
                " [-828/69322987, -2561, -1673935675203/34, -36327313, 183900620/3, -1351/51, -10107826575/4693298," +
                " 8/135785, -653/723856176, 44597161200/56163343, 1/604, 571230169274/157," +
                " -272537553430931180164/27, -18, -8/58163, 616306/3228761, -6/34712945, 1/26007795," +
                " 19623/20795898263], [179/517959818, -25/11, 299041/1336]," +
                " [-65/196, 6158349839/555, 39327858286/51545, -8147/3495, -6768885/26293454], []," +
                " [18511/6, 1969/1130164703346515194624, 3681/2, 14/575, -83/26, 774921559/42, -491148," +
                " 1004789521/199143, -14/1468977, 1769/11, -5/276, 2/183519, -3183901/51, -1/10540979," +
                " -22735273909316/3, 31597/654745433, -25/3]," +
                " [-61/78640740, 85/48312, 16841264489/5, -3/21707687219147, 36/53, -27/16342]," +
                " [1260/32194717, -1154/57, -1634/1010689533077, -4756437963, 247985/604881319716, -7/2, 2290/9," +
                " -1/81, 6, 97/9408237], [945606/236815, 3], [], ...]",
                "{[]=110958, [0]=590, [-1]=268, [1]=258, [-1/3]=146, [1/2]=144, [3]=142, [-2]=134, [-3]=125," +
                " [-1/2]=123}",
                7.994579000016383,
                31.793640290456956
        );
        rationalVectors_fail_helper(2, 1);
        rationalVectors_fail_helper(3, 0);
    }

    private static void rationalVectorsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDimension,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalVectorsAtLeast(minDimension),
                output,
                topSampleCount,
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
                "[[3/221, 1, 1/2], [], [3, -2/3], [], [], [3], [], [0], [], [0], [], [], [], [], [], [], [], [0]," +
                " [0, -3/4], [], ...]",
                "{[]=500592, [0]=89734, [1]=22304, [-1]=22152, [0, 0]=15992, [3]=5725, [2]=5665, [1/2]=5635," +
                " [-1/3]=5629, [-3]=5622}",
                0.9992619999977276,
                2.7824344366226037
        );
        rationalVectorsAtLeast_helper(
                5,
                3,
                0,
                "[[10/3, -20, -1/10, -29/4, -1, 5/2], [-89/2, -1/427, -5/6, -4, -22, -2/683, 0], [0, -4, -7/2]," +
                " [19, 2], [0, -1/6, -1/367, 17/10, 0]," +
                " [-4452, 1, -1/4, -1, -6268, 3, -718/59937, 1, -1/38, -2/25, 1, -5/6, 2, 14, 17/24, 1/7, -1, 0, 1," +
                " 15/619, 1], [], [2, -1/7, -1/7, -29/19562, 0], [0, 10, 1/4, 1/6, -9/4], [], [-1/119, 8]," +
                " [0, -1/6, 11/2, -24/47, -2/11], [-7/2, 1/10, 3], [-7/41]," +
                " [1/3, 0, 0, 0, -17, 1/3, 0, 0, -3/11, 1/2, -1, -22/3, -3623/3], [1/2, -2/5], [1], [7/87], [-3/13]," +
                " [-1/2, 2/5, 468, -26], ...]",
                "{[]=250130, [0]=31725, [1]=10514, [-1]=10460, [0, 0]=4066, [1/3]=3614, [-2]=3585, [-1/2]=3511," +
                " [3]=3510, [-3]=3479}",
                3.0012729999915444,
                4.8070228865365285
        );
        rationalVectorsAtLeast_helper(
                5,
                3,
                1,
                "[[10/3, -20, -1/10, -29/4, -1], [-2/53, 47/3, -89/2, -1/427], [5/9, -4, -22, -2/683], [1/2]," +
                " [0, -4], [-5/7], [3/19, -1/2, 0], [-1/367, 17/10], [-2404], [1], [-1/4], [5], [0]," +
                " [-3/718, 1, -1/38], [-1/5, 1, -5/6], [-1/14, 17/24, 1/7], [-5/4], [6, 0, 0, 1, 15/619]," +
                " [-1/35, 2], [-1, -1/7], ...]",
                "{[0]=56309, [1]=18833, [-1]=18812, [0, 0]=6471, [2]=6373, [-1/2]=6343, [1/3]=6337, [-2]=6275," +
                " [1/2]=6238, [3]=6225}",
                2.9984189999898554,
                4.808294971497336
        );
        rationalVectorsAtLeast_helper(
                5,
                3,
                2,
                "[[1/13, 1/36, -1, -29/4, -1], [3/2, -53/47], [-89/2, -1/427, -5/6, -4, -22, -2/683], [1/2, 1/2]," +
                " [-18, 1], [2/3, 19, 2], [5/2, -24], [-1/367, 17/10], [-2404, 1], [-1/4, -1]," +
                " [-1148, 3, -718/59937, 1, -1/38], [-1/25, 1], [-5/6, 2], [6, 17/24], [-1, 0], [0, 1]," +
                " [3/619, 1, 2, -1/7], [-1/3, -29/19562, 0], [0, 10, 1/4], [-9/4, 8, 16], ...]",
                "{[0, 0]=14361, [1, 0]=4898, [-1, 0]=4846, [0, -1]=4780, [0, 1]=4654, [0, 2]=1642, [-3, 0]=1621," +
                " [0, -2]=1613, [0, 1/2]=1609, [-1/2, 0]=1603}",
                3.0014039999687,
                4.806596179622499
        );
        rationalVectorsAtLeast_helper(
                10,
                8,
                0,
                "[[-489/2, -1/2, 25/53, -2/171, -70/8797, -10, 2/3, 62, -4551, 2/227, -239/978, 1/47084144, 0," +
                " -2/35, -8/5, -1/6268, -504064/13, -238/5, -8/249, -15/13, 0, 0, 0, 3/238, 122224/59, 19, -7/3839," +
                " -1130/15659, 479, 1/12, 2/47], [438, 11], [-98, 1/6, 1/926]," +
                " [-58/97, 1/15, -30, -1/5, 139/9, -70/167, 3, 2/23, 23/7, 1/37, 2260, -1/5]," +
                " [-49/5, 23/3, -7, -7, 49465/2, 9/13, 1, 0, 1/344, 3954017/6454, -3, 13/31, -26/1537, -15, 1/3," +
                " -841, -1/8, 13/53, 5/4, -21, 3/1760], [], [21/5]," +
                " [26, 3, 15/34, -1, 20, 1/9, 5395/2, 3, -149/7, -125/308, 1/69, -3618]," +
                " [47, -19, -29926, -59/17, 2, 3763/9], [2, -1, 246/48401, 3/112, 111, -1/4], []," +
                " [-140502/19, 11/237, -1344/5, -25/118, 3, -3/4, -58759/58, -399/631, -2/4079803, 5]," +
                " [-20/41, -394/13, -1/3, 21, 280/11], [-187/834]," +
                " [1/519, -15/1706, -1/22, -2/31, 1/3, -43124/2505, 3/2, -1, -3/2, -4, 2/11, 3/3361, -3/11075, 0]," +
                " [-2, -4, 2/21], [-219/55, 1/78, -383], [1/2], []," +
                " [-1/1029, -11/28, 6, -2588/93967625, -1, -6299/2, -1083, 5/7, -3, -5/14, -13/3473, -729/2], ...]",
                "{[]=111082, [0]=5072, [-1]=2073, [1]=2024, [1/2]=924, [3]=924, [-3]=914, [2]=886, [-2]=865," +
                " [-1/3]=835}",
                8.013159000016772,
                9.830493816441328
        );
        rationalVectorsAtLeast_helper(
                10,
                8,
                3,
                "[[85/76574, -233/2, -1/2, 25/53, -2/171, -70/8797, -10, 2/3]," +
                " [1/967, 2, 9/2, -92/15, -239/978, 1/47084144, 0, -2/35, -8/5]," +
                " [-97348/41, 16010/10161, -238/5, -8/249]," +
                " [-56/23, 0, 0, 0, 3/238, 122224/59, 19, -7/3839, -1130/15659], [1/18, 479, 1/12, 2/47, 438]," +
                " [-7, -10/3, 2462/21649, -26/97, 1/15, -30, -1/5]," +
                " [-5/14, -11815/27, 3, 2/23, 23/7, 1/37, 2260, -1/5, -49/5, 23/3, -7]," +
                " [-115/11969, 29, -1, 3954017/6454, -3, 13/31], [-19/9, -26/1537, -15], [-841, -1/8, 13/53, 5/4]," +
                " [2/21, 3, -1760, 13/5, 6312/23, 1/3, -1/7, -34/15], [-1/58, 1498/115, -1/6, 1/9]," +
                " [-8/3, 3/67, -3/2, -125/308, 1/69, -3618, -5656/31, 1/11, 1/59, 33, -9/76, 1, -1, 246/48401," +
                " 3/112, 111], [4843/54, 5/3, 19/11]," +
                " [-2368/5, -25/118, 3, -3/4, -58759/58, -399/631, -2/4079803, 5, -12, -5/567, -1/3]," +
                " [3/13, 1/152, -7], [-20672/9, 1/519, -15/1706, -1/22, -2/31, 1/3, -43124/2505, 3/2, -1, -3/2]," +
                " [0, 2/11, 3/3361, -3/11075, 0, -1/2]," +
                " [174, 146/383, 1/2, 0, -30725/11, -1/46542, -2588/93967625, -1, -6299/2]," +
                " [2/571, 0, 5/7, -3, -5/14, -13/3473, -729/2, 0, 1/454, -78], ...]",
                "{[0, 0, 0]=28, [0, -1, 0]=11, [0, 0, -1]=10, [1, 0, 0]=9, [0, 0, 1/2]=8, [1, -1, 0]=8," +
                " [-1, 0, 0]=7, [0, 0, 1/3]=7, [0, 0, 1]=7, [-1, 1, 0]=7}",
                8.010402000000056,
                9.831849886724278
        );
        rationalVectorsAtLeast_helper(
                10,
                8,
                7,
                "[[1/13, 2304798/125, -2, 1/25, -117/219224, -67/5785, -70/8797, -10, 2/3, 62]," +
                " [-17, 2/227, -239/978, 1/47084144, 0, -2/35, -8/5, -1/6268, -504064/13, -238/5, -8/249, -15/13, 0," +
                " 0], [1/3, 238/9949, 229/11, -1/3, -1130/15659, 479, 1/12]," +
                " [2/47, 438, 11, -98, 1/6, 1/926, -97/41, 1/15]," +
                " [-1/3, -1/5, 139/9, -70/167, 3, 2/23, 23/7, 1/37, 2260, -1/5, -49/5, 23/3, -7, -7]," +
                " [-46/51, 49465/2, 9/13, 1, 0, 1/344, 3954017/6454, -3]," +
                " [37/33, 13/31, -26/1537, -15, 1/3, -841, -1/8], [13/53, 5/4, -21, 3/1760, 0, 13/5, 6312/23, 1/3]," +
                " [-34/15, -1, 20, 1/9, 5395/2, 3, -149/7, -125/308]," +
                " [1/69, -3618, -5656/31, 1/11, 1/59, 33, -9/76], [-1, 246/48401, 3/112, 111, -1/4, 19/11, 237/832]," +
                " [3/25, 1/4, 14, 3/5, -58/111, -2/4079803, 5]," +
                " [-1, -5/567, -1/3, 21, 280/11, -20672/9, 1/519, -15/1706, -1/22]," +
                " [-31, -1, 3332/3, -43124/2505, 3/2, -1, -3/2, -4, 2/11, 3/3361]," +
                " [-1/11075, 0, -1/2, 53/9, 1/78, -383, -1/40, 1/2, 0]," +
                " [-1029/11, -1/46542, -2588/93967625, -1, -6299/2, -1083, 5/7, -3, -5/14, -13/3473, -729/2, 0," +
                " 1/454], [-46, 186, 1/63, -40, 37, -9/2, -893924/13]," +
                " [1, -1, 17, 79/3, 7/131, -3239437/3, 71/2, 0, -2, -203/177, 2/3]," +
                " [-209/10, -157/3, -1, 1/11, -246, 3/79, 1/6829], [-1/6, -4, 6, -24, 4, 3/38, 11/4], ...]",
                "{[1/13, 2304798/125, -2, 1/25, -117/219224, -67/5785, -70/8797, -10, 2/3, 62]=1," +
                " [-17, 2/227, -239/978, 1/47084144, 0, -2/35, -8/5, -1/6268, -504064/13, -238/5, -8/249, -15/13, 0," +
                " 0]=1, [1/3, 238/9949, 229/11, -1/3, -1130/15659, 479, 1/12]=1," +
                " [2/47, 438, 11, -98, 1/6, 1/926, -97/41, 1/15]=1," +
                " [-1/3, -1/5, 139/9, -70/167, 3, 2/23, 23/7, 1/37, 2260, -1/5, -49/5, 23/3, -7, -7]=1," +
                " [-46/51, 49465/2, 9/13, 1, 0, 1/344, 3954017/6454, -3]=1," +
                " [37/33, 13/31, -26/1537, -15, 1/3, -841, -1/8]=1," +
                " [13/53, 5/4, -21, 3/1760, 0, 13/5, 6312/23, 1/3]=1," +
                " [-34/15, -1, 20, 1/9, 5395/2, 3, -149/7, -125/308]=1, [1/69, -3618, -5656/31, 1/11, 1/59, 33," +
                " -9/76]=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).reducedRationalVectors(dimension),
                output,
                topSampleCount,
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
        reducedRationalVectors_int_helper(
                1,
                0,
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=1000000}",
                0.0,
                0.0
        );
        reducedRationalVectors_int_helper(
                2,
                1,
                "[[1], [0], [0], [1], [0], [0], [1], [0], [0], [1], [1], [1], [0], [0], [0], [0], [1], [1], [0]," +
                " [0], ...]",
                "{[0]=750027, [1]=249973}",
                1.000000000007918,
                1.2499729999900737
        );
        reducedRationalVectors_int_helper(
                5,
                3,
                "[[1, 1, 1256222/21], [1, 9, -117], [1, 1/9, -92/9], [1, -1/2, -1], [1, -40320/1303, -13/7818]," +
                " [1, -1/5, 62/5], [1, 17/633, -8/211], [1, 1, 111], [1, 91/122224, 11/122224], [1, 223/114, 0]," +
                " [1, 12, -152], [1, 47/2, -1/2], [1, 0, -4/7], [1, -13/51455, -97/102910], [1, 0, 15/41]," +
                " [1, -498, 3], [0, 1, -15/2], [1, 7/15, 0], [0, 1, -730/29], [1, -17/766, 9/766], ...]",
                "{[0, 0, 0]=11640, [0, 0, 1]=4833, [1, 0, 0]=4814, [0, 1, 0]=4750, [1, 1, 0]=2075, [1, -1, 0]=2057," +
                " [1, 0, -1]=2053, [0, 1, 1]=2021, [0, 1, -1]=2011, [1, 0, 1]=1978}",
                2.9999999999775233,
                6.870228666794007
        );
        reducedRationalVectors_int_helper(
                10,
                8,
                "[[1, 9/136, 1/612, -29/153, -1/408, 1/612, -1/1224, -239/1224]," +
                " [1, -2404/5896747328932606365, 0, 6/655194147659178485, -16/5896747328932606365," +
                " -1/310355122575400335, -4/5896747328932606365, -5510881/1965582442977535455]," +
                " [1, -454955/4146519, -311/4146519, -227147/4146519, 35021765/2764346, -2/4146519, 83/2764346," +
                " 5/8293038]," +
                " [1, 3/368648822968, 51089054789/368648822968, 11/368648822968, 0, -7/368648822968," +
                " -144651/184324411484, -1283/368648822968], [1, -1150/9, 1/18, 2/9, -49/18, -29074003/9, 47/18," +
                " -5/3]," +
                " [1, -506/87, -2/87, -111061034/87, 0, -739/87, 101342/87, 345112274/87]," +
                " [0, 1, -54/398291, 1922340940521/796582, -9/3186328, 5/3186328, -3015/3186328, 0]," +
                " [1, 1105224/3133, 122886/241, -1/241, -248/3133, 3/3133, -7/3133, 7182/3133]," +
                " [1, 1/6, -38587, -9/2, -1/3, 25/3, 132381/2, 56/3]," +
                " [1, 27/7, -192, -956783/7, -198077/7, 207/7, 0, 44117477702/7]," +
                " [1, -51/12544, -140539237/6272, -1161315/6272, -174785977915/12544, -1543/12544, -54655/6272," +
                " -1/12544], [1, 36587/22, 32359/22, -65/11, -7764/11, -117129/22, 1, 0]," +
                " [1, -1026377047819/81, -1/486, -95/486, -155/486, -1/486, -146275/486, 0]," +
                " [1, -14/5, -12/5, -6/5, -21, 6/5, -329/5, 5994968441128/5]," +
                " [0, 1, -86/203, -28575162030/29, -79595301742/203, 13/203, 451/203, -23971/203]," +
                " [1, 0, 2/209, 281/209, -6288/209, 772/209, 1377/209, -158207289/209]," +
                " [1, -11637/252307688, 1/252307688, -1/504615376, -61/504615376, 21/126153844, -3/252307688," +
                " 1/504615376], [1, 717/2, -12, -74, 37841/2, -1423414505/2, -3076, -3/2]," +
                " [1, 15/1402, 3/1402, 254284349/2804, -37/701, 7/1402, 3/2804, 4633/701]," +
                " [1, -7/5, 79211053/5, 449/4, -53/10, 1/20, 21493072257233/20, 5767973249/20], ...]",
                "{[1, 9/136, 1/612, -29/153, -1/408, 1/612, -1/1224, -239/1224]=1," +
                " [1, -2404/5896747328932606365, 0, 6/655194147659178485, -16/5896747328932606365," +
                " -1/310355122575400335, -4/5896747328932606365, -5510881/1965582442977535455]=1," +
                " [1, -454955/4146519, -311/4146519, -227147/4146519, 35021765/2764346, -2/4146519, 83/2764346," +
                " 5/8293038]=1," +
                " [1, 3/368648822968, 51089054789/368648822968, 11/368648822968, 0, -7/368648822968," +
                " -144651/184324411484, -1283/368648822968]=1, [1, -1150/9, 1/18, 2/9, -49/18, -29074003/9, 47/18," +
                " -5/3]=1," +
                " [1, -506/87, -2/87, -111061034/87, 0, -739/87, 101342/87, 345112274/87]=1," +
                " [0, 1, -54/398291, 1922340940521/796582, -9/3186328, 5/3186328, -3015/3186328, 0]=1," +
                " [1, 1105224/3133, 122886/241, -1/241, -248/3133, 3/3133, -7/3133, 7182/3133]=1," +
                " [1, 1/6, -38587, -9/2, -1/3, 25/3, 132381/2, 56/3]=1, [1, 27/7, -192, -956783/7, -198077/7, 207/7," +
                " 0, 44117477702/7]=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).reducedRationalVectors(),
                output,
                topSampleCount,
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
                "[[1, 85/3, 0], [0], [], [0], [0], [1], [], [], [], [], [0], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=646910, [0]=161250, [0, 0]=40554, [1]=40456, [1, 0]=10142, [0, 0, 0]=10071, [0, 1]=10042," +
                " [0, 0, 1]=2582, [1, 1]=2546, [1, 0, 0]=2540}",
                0.6515059999979717,
                1.6404990898138534
        );
        reducedRationalVectors_helper(
                5,
                3,
                "[[1, 1256222/85, -233/85, -1/85, 0, 1/85], [1, 0, -8, 2], [1, 0, -4551/94], [1, -23, -7/4, 1/4]," +
                " [1, -224, 1, -4, 633, 17], [1], [0]," +
                " [1, 91/38, 11/38, 0, -7/38, 16127/38, -309/19, -15659/38, -1/38, 3, 223/38, 0]," +
                " [0, 1, -1/47, -46/47], [], [1, 7/2, -7, 0, 0, -1/2, 2, 0], [1, -5/267, -38/267, -11815/267]," +
                " [1, 1/2, -15/2], [], [1, 0, -37/15], [], [0], [], [], [0, 1], ...]",
                "{[]=470607, [0]=58940, [1]=24540, [0, 0]=7449, [1, 0]=3085, [0, 1]=3084, [1, 1]=1300, [1, -1]=1284," +
                " [0, 0, 0]=944, [1, -2]=569}",
                2.5163809999931646,
                7.688697379443966
        );
        reducedRationalVectors_helper(
                32,
                8,
                "[[1, -20981157/112685255093845, -56292/16097893584835, -120894/22537051018769, 26/16097893584835," +
                " -3788/112685255093845, 1637/112685255093845, 201836/112685255093845, -232/112685255093845," +
                " -6/22537051018769, -8534813782261489769073/16097893584835, -293956/112685255093845," +
                " -37861498017861727418/22537051018769, 5791/22537051018769, 669634458943144/22537051018769," +
                " 11/112685255093845, -2910742/112685255093845, -4967/22537051018769, 10/22537051018769," +
                " -508/112685255093845, -1466398006247/112685255093845, -46/112685255093845, -13562/112685255093845," +
                " -695976/112685255093845, 688562/16097893584835, 5542398/112685255093845, -234/112685255093845," +
                " 1906977267390725094387280/22537051018769, -363877104/112685255093845, -91712856/112685255093845," +
                " 3/112685255093845], [1, 28088/31177, -280884356308748050741099869955642846/31177]," +
                " [1, 5129/249, 0, -870085049018158987528554667/747, -440769353/747]," +
                " [1, 354213973/1771, -177017/1771, -26700086386/1771, -12261695942506/1771, -5226619389982/1771," +
                " -26164044723935471006011785430908281959257296841800/1771]," +
                " [1, 2238317906063783/391, -429/391, 40/391, -24614379/23, -147/391, 85257/391, 937/391," +
                " -56022/391]," +
                " [1, 77/18049656405900152210, 96241583/9024828202950076105, 549943/36099312811800304420," +
                " 1/36099312811800304420, -15115243678996539/36099312811800304420, 8641/36099312811800304420]," +
                " [1, 488/10437, -7577458850986978217/10437, -1264/10437, -16410468210809/1491, 259624/497," +
                " -594016125101088808832060264942027504116335596926564/10437, 75119070626516808/3479, 681190/10437," +
                " 1683229699/10437, 2663972/10437, -1512733478/1491, 117525398/10437," +
                " 872051284356696732231572275/10437, 7189772234/10437, -488344/10437, 725290/3479," +
                " 609281197727896401432736269590/10437, -2/10437, 8329801121680278377567650011013546620/3479," +
                " 55/3479, -2371/1491]," +
                " [1, 18/182099, -10/182099, 4882/182099, -3850444/182099," +
                " 634839268453353265487425588455314257/182099, 0, 324901632/182099], []," +
                " [1, -936/1696870787, 3/1696870787, 3028852536/1696870787, -2/1696870787, -316/1696870787," +
                " -2081/1696870787, -65824/1696870787, 31941758203/1696870787, -1798563486/1696870787," +
                " -686163800713560015/1696870787, -36327313/1696870787, 3604983285930/1696870787, -3/1696870787]," +
                " [1, 9682071/5, 480733578/5, -7072171/5, -92794838/5, 266501333814584625267172/5," +
                " 1769496824093704/5, -1/5, 894753/5, -21558614/5, 891405023649/5, -7878220384814/5, 102/5, 1668/5," +
                " -188, 53278803552732/5]," +
                " [1, 3910533309/31715, -88601367501441/31715, -17/31715, -27357495316208181536/6343," +
                " -151749080509887783/31715, 2258590018/31715, -9/31715, 123313/31715, 130602/31715," +
                " 127574955676719714878/31715, 874863/31715, -12878245112759/6343, -28/31715, 222300496832713/6343]," +
                " [1, -82225/1142, 1/2284, 276679131525/2284, -1217648118681/2284, 0, 126941/2284, 3367094955/2284," +
                " -15992035/2284, 8342133675/2284, -53917/2284, -31/2284, 19/2284, 199/1142, 377/1142," +
                " -91819053/571, -517466029689/2284, -1335249976973/1142, -8304458835/2284]," +
                " [1, 137359716886/123397233917, -7/246794467834, -551926226/123397233917, -447/123397233917," +
                " -29342551484/123397233917, 128137/246794467834, -426357100/123397233917, -50814301/246794467834," +
                " -3411892935022680178222611/246794467834, 1883041170577/246794467834, 103/246794467834," +
                " 253999061/123397233917]," +
                " [1, -1818879152193024859175861313217/10, -19/5, -358742414013/10, -201996658779576257904273017/10," +
                " -2401150362079/10, 1714090686103/10, 1755031779/10]," +
                " [1, 5/36040063567761689113515657603159," +
                " -33512750433853002423052858379/36040063567761689113515657603159," +
                " -4397/36040063567761689113515657603159, -358200/4004451507529076568168406400351," +
                " -23/36040063567761689113515657603159, 14/36040063567761689113515657603159," +
                " 258731/36040063567761689113515657603159, -1/36040063567761689113515657603159," +
                " 5580710311396322088020/36040063567761689113515657603159]," +
                " [1, 1158729049124093/71687617974830126667362242022040869961072]," +
                " [1, 49168/4813895, 9891/4813895, 93497/4813895, 826809212/4813895, 8095246076/962779," +
                " 132349/4813895, 3/4813895]," +
                " [1, -26/3595, -14897072894669/7190, 2229100293382556809498174488822267/7190, -1948860002391/7190," +
                " 2206331/3595, -27/7190, 50648/719, -18619725585032/719, -2593552/3595," +
                " -97373748543303886659020906/3595, 117/3595, 2130213834129027837/3595, -98248/3595," +
                " -1414120186086285753/7190, -123441841/7190, -544481/1438, -311435027250611510375387183111/7190," +
                " 260010/719, 9259220024110/719, -1795198244309/7190, -165868189/7190, 5520663138/3595, -1/3595," +
                " 27730733/3595, 981456528/3595, 97783895616969/7190, -31/7190, -99657/7190]," +
                " [1, -492435319384353042220/7, 21491/7, -8/7, -1504/7, 695/7, 828538420282164027294/7, 10/7, 23/7," +
                " 14794958775771960096768523454396488/7, 504729897831/7, 57402707133/7, -834137689685106/7," +
                " -4934668368816606725285750326/7, 54430077583015014883/7, 567260189/7," +
                " -2585192056432476043973583191, 154904596135599213/7, -795216/7, 1191950632705689/7," +
                " -77242986265689401/7, 1831515498/7, -4030862160261852/7, -48/7, -328381074425630002650/7, 1/7," +
                " -34786/7, -97782/7, -240657/7, 15887403613/7, 3/7," +
                " 3896715263304259601547967726364084469918797170192517201146822923199/7, 0, 207/7, -934435/7, 0," +
                " -253210880, -18650453184623817188103/7, -2097903802423269689025/7], ...]",
                "{[]=230958, [0]=6296, [1]=2883, [0, 0]=157, [0, 1]=103, [1, 0]=64, [1, 1]=45, [1, -1]=38," +
                " [1, -3]=31, [1, -2]=23}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateBitSize
    ) {
        rationalVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).reducedRationalVectorsAtLeast(minDimension),
                output,
                topSampleCount,
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
                "[[1, 85/3, 0], [0], [], [0], [0], [1], [], [], [], [], [0], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=646910, [0]=161250, [0, 0]=40554, [1]=40456, [1, 0]=10142, [0, 0, 0]=10071, [0, 1]=10042," +
                " [0, 0, 1]=2582, [1, 1]=2546, [1, 0, 0]=2540}",
                0.6515059999979717,
                1.6404990898138534
        );
        reducedRationalVectorsAtLeast_helper(
                5,
                3,
                0,
                "[[1, 1256222/85, -233/85, -1/85, 0, 1/85], [1, 0, -8, 2], [1, 0, -4551/94], [1, -23, -7/4, 1/4]," +
                " [1, -224, 1, -4, 633, 17], [1], [0]," +
                " [1, 91/38, 11/38, 0, -7/38, 16127/38, -309/19, -15659/38, -1/38, 3, 223/38, 0]," +
                " [0, 1, -1/47, -46/47], [], [1, 7/2, -7, 0, 0, -1/2, 2, 0], [1, -5/267, -38/267, -11815/267]," +
                " [1, 1/2, -15/2], [], [1, 0, -37/15], [], [0], [], [], [0, 1], ...]",
                "{[]=470607, [0]=58940, [1]=24540, [0, 0]=7449, [1, 0]=3085, [0, 1]=3084, [1, 1]=1300, [1, -1]=1284," +
                " [0, 0, 0]=944, [1, -2]=569}",
                2.5163809999931646,
                7.688697379443966
        );
        reducedRationalVectorsAtLeast_helper(
                5,
                3,
                1,
                "[[1, 1256222/85, -233/85, -1/85, 0], [0], [0, 1, -1/2, 31], [1, 1]," +
                " [1, 91/38, 11/38, 0, -7/38, 16127/38, -309/19, -15659/38, -1/38, 3], [0, 1, -1/47, -46/47]," +
                " [1, -6, 1, 2462, 102910], [0], [1, -5/267], [0], [0], [1, -730/29], [0, 0], [0]," +
                " [1, -2/115001, 9/115001, 13/115001, 0, -1/115001, 0], [1, 371373/376, 23417/940, -3/1880]," +
                " [1, 0, -11/79, 5/79], [0], [0, 0, 1, 1/2, -11], [1, 13/16], ...]",
                "{[0]=161152, [1]=67072, [0, 0]=17734, [0, 1]=7523, [1, 0]=7470, [1, 1]=3127, [1, -1]=3097," +
                " [0, 0, 0]=1975, [1, 3]=1370, [1, -1/3]=1324}",
                3.6060739999871925,
                7.173386070062897
        );
        reducedRationalVectorsAtLeast_helper(
                5,
                3,
                2,
                "[[1, 21, 1256222, -233, -1], [0, 1], [1, -117/5], [1, 1/6, 1/3], [1, -46, -394, -122, 5, -1]," +
                " [1, -112/15, 1/30], [1, -8, -5, -13/3, 5/3], [1, 0, -1, 16127/7], [1, 0, -4/3], [0, 1, 1, -15]," +
                " [1, -730/29], [1, 23/3, -1/3], [1, 46834/152929, -3/152929, -1/152929]," +
                " [1, 33/5, 1, 31/5, 0, -11/5], [1, -26/3, 1537/3], [1, 1/2], [1, 8/3], [1, 12/5]," +
                " [1, 15/14504, -5/7252, 3/7252, 0, 3/14504], [1, 3347/25], ...]",
                "{[0, 0]=37658, [0, 1]=16039, [1, 0]=15641, [1, 1]=6640, [1, -1]=6610, [0, 0, 0]=3091," +
                " [1, 1/2]=2786, [1, 2]=2775, [1, -3]=2768, [1, 1/3]=2768}",
                3.2424949999676405,
                7.015494549601162
        );
        reducedRationalVectorsAtLeast_helper(
                10,
                8,
                0,
                "[[1, -2/3064121, 3346/3064121, -1/3064121, 0, 3928/3064121, 152929/3064121, -8080881/3064121," +
                " 13297953072/3064121, -41349439/3064121, -1/3064121, -3/3064121], [], [1, 3133/7]," +
                " [0, 1, -1/122886, -124/798759, 1/532506, -7/1597518, 399/88751, -368/798759, 1/1597518, 5/1597518," +
                " -434063/1597518, 1/798759, 10025/1597518, -5/798759, -85/266253, -419/1597518]," +
                " [1, -789/13, 524/13, 25689/13, -3421/13, -3640006/13, -27355, -177305492008, 31/13, -1/13, -7/13," +
                " 0, 1524767947173/13, -11/13, -330228993066/13, 7/13, 17/13, 6/13, 1/13]," +
                " [1, -202206016023/5, -82699/5, -1, 973/5, -3/5, -51/5, 398077/5, 372/5], [1, 12126258480, -11617]," +
                " [1, -158207289/5473, -7/5473, 23/5473, -3/5473, -3117942350/5473, 995620/5473, 1/5473, -3/5473]," +
                " [1, -3879/173581048, 1/520743144, -1/1041486288], [1, -2/69, 1/207], [], []," +
                " [1, 0, -184109803/457, -2733/10054, 527/10054, 3/5027, 26/5027, -39/10054]," +
                " [1, 1/2, 15/4, 2985/4, 1/2, 717/4, -6, -37, 37841/4, -1423414505/4, -1538, -3/4, 701, 15/2, 3/2]," +
                " [1, 0, 0, -9/2, -1/12, 0, 1/12, 0, 0, 77150647/3, -1/12, 5/12, -163963/12, 0]," +
                " [1, -151684/99, 246/11, -4/33, 128/99]," +
                " [1, 586/7889853413, 3/15779706826, -243/15779706826, -12944/7889853413, 307/15779706826," +
                " 31/7889853413, -23/7889853413, 233/15779706826]," +
                " [1, 260/2629, -1443175/15774, -251/15774, 7/5258, -1525/7887, 1/15774, -7/2629, 0," +
                " 1255391308/7887, -643/15774, -4/2629, 59/15774, 128423/15774], [1, 1/7, 46/7]," +
                " [1, 23/212171250400, -843/424342500800, 1/212171250400, -1/26521406300, -1123/212171250400," +
                " 508279/84868500160], ...]",
                "{[]=227395, [0]=18415, [1]=8415, [0, 0]=1471, [0, 1]=729, [1, 0]=708, [1, 1]=324, [1, -1]=305," +
                " [1, -3]=154, [1, 2]=146}",
                7.934091000004385,
                17.246808487906456
        );
        reducedRationalVectorsAtLeast_helper(
                10,
                8,
                3,
                "[[1, -593/590028540, 11/29501427, -39/196676180, -645698713/590028540, -1156691/147507135," +
                " -31/147507135, -16591/147507135]," +
                " [1, 3/7871570104, 51089054789/7871570104, 11/7871570104, 0, -7/7871570104, -144651/3935785052," +
                " -1283/7871570104], [1, 152929/344, -8080881/344, 1662244134/43, -41349439/344]," +
                " [1, -231522/7, -27/7, -2/7, 50/7, 397143/7, 16], [1, 6272/17, -3/2, -140539237/17, -1161315/17]," +
                " [1, -1543/414322270, -10931/41432227, -1/414322270, 11/207161135, 36587/414322270," +
                " 32359/414322270, -13/41432227, -7764/207161135, -117129/414322270, 11/207161135]," +
                " [1, 8/11, -2312/33]," +
                " [1, 1/34, -202206016023/34, -82699/34, -5/34, 973/34, -3/34]," +
                " [1, 372/1970941, -4/1970941, 29306127664/1970941, -11617/1970941, 2119739845/1970941, -1/1970941," +
                " -142246/1970941, 18553/1970941], [1, -3/71, -3117942350/71, 995620/71, 1/71, -3/71]," +
                " [1, -3879/173581048, 1/520743144, -1/1041486288, -61/1041486288, 7/86790524, -1/173581048," +
                " 1/1041486288, -7/1041486288], [1, 2985/7, 2/7], [0, 1, -112, -3111], [1, -1/308602588," +
                " 5/308602588]," +
                " [1, 0, 5102/8827923952273863, -8/2942641317424621, 718/8827923952273863, 16/8827923952273863," +
                " 66986289128/8827923952273863, 1708/8827923952273863, 0], [1, -77343596, 249/2]," +
                " [1, 5, 2657104, -2471048]," +
                " [1, 1322424107679/4024470779, 43/4024470779, 3930/4024470779, 21920/4024470779," +
                " -205195/4024470779, 901/4024470779, -10/4024470779, 319661334/4024470779, -1066/4024470779]," +
                " [1, -8/7177, 49130/7177, -1920/7177, 109/7177]," +
                " [1, 0, -44/21, -1342/105, -184/105, 246706/35, 215539/105, 2/105, -61/105, 0, 4/105," +
                " -1053840368744/105, 34978808/105, 251521461162, -7/15, -710206652679/7, 0, -34/15, 18/35], ...]",
                "{[0, 0, 0]=261, [0, 1, 0]=120, [0, 0, 1]=114, [1, 0, 0]=105, [1, 0, -1]=66, [0, 1, 1]=60," +
                " [0, 1, -1]=56, [1, -1, 0]=51, [1, 0, 1]=51, [1, 1, 0]=42}",
                8.239842999997473,
                16.818875068721916
        );
        reducedRationalVectorsAtLeast_helper(
                10,
                8,
                7,
                "[[1, 1/8, -1/4, 1/8, -3/8, 7/4, 153, 81/8], [1, 0, -1, -289302/7, -1283/7, 18/7, -2300/7, 1/7]," +
                " [1, -1447/35149091, 8138/105447273, -17/210894546, 755185/105447273, 21931365/35149091," +
                " -394953281/210894546], [1, 5, 0, 3186328/3, -144, 2563121254028, -3]," +
                " [1, -3015, 0, -20040, 3064121, -2, 3346, -1]," +
                " [1, 152929/5976, -2693627/1992, 554081378/249, -41349439/5976, -1/5976, -1/1992]," +
                " [1, 2/3, 1/9, -77174/3, -3, -2/9, 50/9]," +
                " [1, 112/69463, -23/69463, 114/69463, 629/69463, 3/69463, 60/69463, 0]," +
                " [1, 16011301/395, 7/790, 27/790, -672/395, -956783/790, -198077/790, 207/790, 0, 22058738851/395]," +
                " [1, -51/12544, -140539237/6272, -1161315/6272, -174785977915/12544, -1543/12544, -54655/6272]," +
                " [0, 1, 36587/54, 32359/54, -65/27, -2588/9, -39043/18], [1, 0, 0, -20/7, -530/7, 65/14, 12/7]," +
                " [1, -14/5, -12/5, -6/5, -21, 6/5, -329/5]," +
                " [1, 0, 203/160856798945, -86/160856798945, -40005226842/32171359789, -79595301742/160856798945," +
                " 13/160856798945, 451/160856798945, -23971/160856798945, 209/160856798945, 0, 2/160856798945]," +
                " [1, -2096/51, 772/153, 9, -52735763/51, -7/153, 23/153], [0, 1, 15/4, 2985/4, 1/2, 717/4, -6]," +
                " [1, -148/254284349, 14/254284349, 3/254284349, 18532/254284349, -2/254284349, 860/254284349]," +
                " [1, 0, -467/6310, -46769008/3155, -13/6310, 1/3155, 2/631, -14/3155]," +
                " [1, 2245/182626484, -53/91313242, 1/182626484, 21493072257233/182626484, 5767973249/182626484," +
                " 1/182626484, -3/182626484, 0]," +
                " [1, -210/11, 1622/803, -6593/1606, 6/803, 6/803, -104045/803, 402824061598125/1606], ...]",
                "{[1, 1/8, -1/4, 1/8, -3/8, 7/4, 153, 81/8]=1, [1, 0, -1, -289302/7, -1283/7, 18/7, -2300/7, 1/7]=1," +
                " [1, -1447/35149091, 8138/105447273, -17/210894546, 755185/105447273, 21931365/35149091," +
                " -394953281/210894546]=1, [1, 5, 0, 3186328/3, -144, 2563121254028, -3]=1," +
                " [1, -3015, 0, -20040, 3064121, -2, 3346, -1]=1," +
                " [1, 152929/5976, -2693627/1992, 554081378/249, -41349439/5976, -1/5976, -1/1992]=1," +
                " [1, 2/3, 1/9, -77174/3, -3, -2/9, 50/9]=1," +
                " [1, 112/69463, -23/69463, 114/69463, 629/69463, 3/69463, 60/69463, 0]=1," +
                " [1, 16011301/395, 7/790, 27/790, -672/395, -956783/790, -198077/790, 207/790, 0," +
                " 22058738851/395]=1," +
                " [1, -51/12544, -140539237/6272, -1161315/6272, -174785977915/12544, -1543/12544, -54655/6272]=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<PolynomialVector> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomialVectors(dimension),
                output,
                topSampleCount,
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
        polynomialVectors_int_helper(
                1,
                0,
                0,
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=100000}",
                0.0,
                0.0,
                0.0
        );
        polynomialVectors_int_helper(
                5,
                3,
                3,
                "[[-x^3-233*x^2+1256222*x+85, 0, 0]," +
                " [25, -39244*x^4-67*x^3-x^2-6*x-3758168, 2*x^7+x^6+30*x^5-46*x^4-16989*x^3-38*x^2-12*x-1]," +
                " [-8*x^2+1, 62*x-3, 0]," +
                " [-70*x^8-x^7+x^6-7*x^5-92*x^4+x^3+9*x^2+4, -x^8+2*x^7-2404*x^4+54644*x^3+4*x^2-181301872*x+3," +
                " 41*x^4-64580*x^3+5*x-24]," +
                " [x^9-224*x^8+62*x^7-x^6+5*x^5-122*x^4-394*x^3-238*x^2-13*x-504064, 17*x^2+633*x," +
                " -2708*x^4+5*x^3-13*x^2-15*x], [-1, 0, 7]," +
                " [-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+122224," +
                " -x^12+47*x^11+2*x^10-7350*x^9-2*x^8-4*x^7-152*x^6+12*x^5+x^4+223*x^2+114*x-5," +
                " -4*x^4+7*x^2-2*x+438]," +
                " [-5*x^20+4*x^18-x^17-14*x^14+15*x^13+41*x^11-97*x^10-26*x^9+102910*x^8+2462*x^7+x^6-10*x^5-7*x^3-" +
                "2275*x^2-158, x^4+27*x^3-11815*x^2-38*x-5, -37*x^7+7*x^5+15*x^4-15*x^3+2*x^2+5]," +
                " [-106, 0, 29*x^2+4], [-1, 1856865*x+1880, -31], [0, x, 8*x^4-x^3-329*x-4]," +
                " [-13*x^3+8*x^2+5*x-138, 0, x^5+12*x^4+13*x^3-3*x^2+7], [15*x-34, 0, -58]," +
                " [986*x-4, x^3+x^2-2*x+9491, -61*x^5+17*x^4+5209*x^3+6*x^2-x-7]," +
                " [x^10+33*x^9-59*x^8-13542*x^6+26766*x^5+18566*x^4-19*x^2+47, 0, 17*x^4+x^3-227*x-9]," +
                " [x^2+3*x, 246*x^4-233*x," +
                " x^14+73*x^13+4*x^11-x^10+3*x^9+86*x^8+4843*x^7-x^6+111*x^5+206*x^4+x^3+x^2]," +
                " [x^14+2*x^13+126*x^12+14*x^10+8*x^9+x^7-54*x^6-9*x^5+3*x^4-832*x^3+237*x^2+7*x+35, -22501*x+28," +
                " 37796775*x^4-2*x^3+6*x^2-631*x-911]," +
                " [-138*x^8-222*x^7-12*x^6-606*x^5-12*x^3+3*x+1245, -1656*x^2-183*x-1, -x^4-3*x^3-6*x+1048]," +
                " [-1, 53, 0]," +
                " [-2*x^8+5*x^7-12480*x^6-62*x^5-322*x^4-115*x^3-7*x, -2567*x^5+x^4-57908*x^3-146*x^2+425*x, 1], ...]",
                "{[0, 0, 0]=1211, [0, 0, -1]=82, [0, 0, 1]=75, [-1, 0, 0]=72, [1, 0, 0]=67, [0, -1, 0]=67," +
                " [0, 1, 0]=62, [3, 0, 0]=37, [0, 0, 3]=34, [-2, 0, 0]=33}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .polynomialVectors(),
                output,
                topSampleCount,
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
                "[[0, 1, 0], [x, 0], [], [], [0], [], [], [], [], [], [], [], [], [], [], [-x^4+x^3-x^2+19*x-5]," +
                " [0, 0], [], [], [], ...]",
                "{[]=49551, [0]=16890, [0, 0]=5598, [0, 0, 0]=1881, [-1]=1061, [1]=972, [0, 0, 0, 0]=655," +
                " [0, 1]=368, [1, 0]=358, [0, -1]=352}",
                1.0080899999996153,
                -0.3357438323961038,
                1.2442692233026915
        );
        polynomialVectors_helper(
                5,
                3,
                2,
                "[[-x^2-233*x+6499102, 0, 0, 25], [-x^2-6*x-3758168]," +
                " [2*x^13-8*x^12-6*x^9+2*x^8+x^7+30*x^6-46*x^5-16989*x^4-38*x^3-28*x^2-320*x, 94, 0," +
                " -70*x^8-x^7+x^6-7*x^5-92*x^4+x^3+9*x^2+4, -x^8+2*x^7-2404*x^4+54644*x^3+4*x^2-181301872*x+3," +
                " 41*x^4-64580*x^3+5*x-24], []," +
                " [-504064, -394*x^2-46*x+1, -4*x^5+x^4-224*x^3+62*x^2-x+13," +
                " -x^6-2708*x^5+5*x^4-13*x^3-15*x^2-24*x+49, 0, 0, 7, -618*x^6+16127*x^5-7*x^4+11*x^2+91*x+122224," +
                " -x^12+47*x^11+2*x^10-7350*x^9-2*x^8-4*x^7-152*x^6+12*x^5+x^4+223*x^2+114*x-5," +
                " -4*x^4+7*x^2-2*x+438]," +
                " [41*x^11-97*x^10-26*x^9+102910*x^8+2462*x^7+x^6-10*x^5-7*x^3-2275*x^2-158, 0, 0, -x^3-30," +
                " 71*x^2-3*x-2], [-11815*x-206], [-498*x+5], [0, -x^4-37*x^3+7*x+23], [29*x^2+4]," +
                " [-51*x^2-78*x+2, 37*x^12-x^11-3*x^10+46834*x^9+1856865*x^8+344*x^7+2*x^6-x^4+13*x^2+9*x-6]," +
                " [1537*x^3-26*x^2+3*x-1, 0, -38, -329*x, 0, 16, -13*x^3+8*x^2+5*x-138], [], []," +
                " [1, 21, 15*x^2+2216*x+9, -2, 15*x-34, 0], [], [], [], [], [], ...]",
                "{[]=33212, [0]=5094, [0, 0]=780, [-1]=284, [1]=281, [2]=121, [-2]=119, [-3]=119, [0, 0, 0]=109," +
                " [3]=106}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        polynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .polynomialVectorsAtLeast(minDimension),
                output,
                topSampleCount,
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
                "[[0, 1, 0], [x, 0], [], [], [0], [], [], [], [], [], [], [], [], [], [], [-x^4+x^3-x^2+19*x-5]," +
                " [0, 0], [], [], [], ...]",
                "{[]=49551, [0]=16890, [0, 0]=5598, [0, 0, 0]=1881, [-1]=1061, [1]=972, [0, 0, 0, 0]=655," +
                " [0, 1]=368, [1, 0]=358, [0, -1]=352}",
                1.0080899999996153,
                -0.3357438323961038,
                1.2442692233026915
        );
        polynomialVectorsAtLeast_helper(
                5,
                3,
                1,
                0,
                "[[0, -x^2-233*x+6499102, 0], [], [], [], [], [], [-6*x^2-1661016*x-240], [], [], [], []," +
                " [-16989*x^4-38*x^3-28*x^2-576*x-170316, -6*x^4+2*x^3+x^2+14*x], [], [0, -x^2+2*x], []," +
                " [x^2-2503*x+1]," +
                " [-7*x^2-92*x+4, -6, 4*x^3-181301872*x^2+x-3026," +
                " x^12-224*x^11+62*x^10-x^9+5*x^8-122*x^7-394*x^6-238*x^5-13*x^4-241920*x^3+7818*x^2+41*x], [], []," +
                " [], ...]",
                "{[]=49867, [0]=5744, [0, 0]=663, [-1]=323, [1]=315, [2]=149, [3]=143, [-2]=123, [-3]=115," +
                " [0, 0, 0]=72}",
                1.006299999999629,
                2.8524992546957577,
                5.083236818273093
        );
        polynomialVectorsAtLeast_helper(
                5,
                3,
                2,
                1,
                "[[0, -x^2-233*x+6499102, 0, 0], [25]," +
                " [-x^2-6*x-3758168, 30*x^6-46*x^5-16989*x^4-38*x^3-28*x^2-576*x-170316, 4], [1, 0], [-x^2+2*x]," +
                " [-92*x+4], [-x+2, 4*x^3-181301872*x^2+x-3026]," +
                " [-19*x^6-2*x^5-x^4+2*x^3-4452, 5*x, 0," +
                " -4*x^13+x^12-224*x^11+62*x^10-x^9+5*x^8-122*x^7-394*x^6-238*x^5-13*x^4-241920*x^3+7818*x^2+41*x," +
                " -x^6-2708*x^5+5*x^4-13*x^3-15*x^2-24*x+49], [0, 7]," +
                " [16127*x^5-7*x^4+11*x^2+91*x+122224]," +
                " [-x-43798, 12*x^3+x^2+735, -x^5+47*x^4+2*x^3-7350*x^2-2*x-8, -4*x^4+7*x^2-2*x+438," +
                " -5*x^20+4*x^18-x^17-14*x^14+15*x^13+41*x^11-97*x^10-26*x^9+102910*x^8+2462*x^7+x^6-10*x^5-7*x^3-" +
                "2275*x^2-158]," +
                " [-11815*x^2-38*x-5, -23, -37*x^2+15, -106], [29*x^2+4]," +
                " [-51*x^2-78*x+2, 37*x^12-x^11-3*x^10+46834*x^9+1856865*x^8+344*x^7+2*x^6-x^4+13*x^2+9*x-6," +
                " 5*x^4-11*x^3+31*x+21], [-15*x^3-x^2+2561], [x], [16, -13*x^3+8*x^2+5*x-138, 0]," +
                " [12*x^4+13*x^3-3*x^2+7]," +
                " [15*x^10+3*x^8+6*x^6-10*x^5+15*x^4+2216*x^3+3*x^2+21*x-1, -26*x^4-x^3+15*x-1]," +
                " [-4, 25*x^7-6*x^5+12*x^2-54*x-794, 5209*x^7+6*x^6-x^5-3*x^4-67*x^3+x^2+x-8], ...]",
                "{[0]=11481, [0, 0]=1309, [1]=658, [-1]=636, [2]=307, [3]=264, [-3]=261, [-2]=260, [0, 0, 0]=149," +
                " [-7]=122}",
                2.0001200000002344,
                2.8603683778951354,
                5.081219240520803
        );
        polynomialVectorsAtLeast_helper(
                5,
                3,
                3,
                2,
                "[[0, -x^2-233*x+6499102, 0, 0, 25]," +
                " [-x^2-6*x-3758168, 30*x^6-46*x^5-16989*x^4-38*x^3-28*x^2-576*x-170316, 4, -8*x^2+1]," +
                " [-3, 9*x^4+x^2-2503*x+1]," +
                " [0, x^6-3026*x^5-70*x^4-x^3+x^2-3*x-1," +
                " -224*x^24+62*x^23-x^22+5*x^21-122*x^20-394*x^19-238*x^18-13*x^17-241920*x^16+7818*x^15+41*x^14-" +
                "64580*x^13+5*x^11-4*x^10-19*x^9-2*x^8-x^7+2*x^6-2404*x^3+54644*x^2+x+1, -24]," +
                " [-2708*x^5+5*x^4-13*x^3-15*x^2-24*x+49, -1, 0], [0, 238]," +
                " [x^11+223*x^9+114*x^8-x^7-15659*x^6-618*x^5+16127*x^4-7*x^3+11*x+229, -2*x^2-4*x-280," +
                " -98*x^10-16624788*x^9-4*x^8+7*x^6-2*x^5+438*x^4-46*x^3-x^2+47*x+4]," +
                " [41*x^9-97*x^8-26*x^7+102910*x^6+2462*x^5+x^4-10*x^3-7*x, 0], [-x^3-30, 71*x^2-3*x-2]," +
                " [27*x^2-11815*x-206, -1778], [0, 0, -x^4-37*x^3+7*x+23, x^5-10*x^4+1236*x^2+2*x+1]," +
                " [-17*x^2+766*x-2778, -x+39]," +
                " [0, -51*x^2-78*x+2, 37*x^12-x^11-3*x^10+46834*x^9+1856865*x^8+344*x^7+2*x^6-x^4+13*x^2+9*x-6," +
                " 5*x^4-11*x^3+31*x+21], [-15*x^3-x^2+2561, 0], [-38, -329*x], [16, -13*x^3+8*x^2+5*x-138]," +
                " [x^5+12*x^4+13*x^3-3*x^2+7, 15*x-34], [-58, 986*x-4], [-6*x^4+12*x-118, 0, x^3+x^2-2*x+9491]," +
                " [17*x^4+5209*x^3+6*x^2-x-7, 1305*x^4+6*x^2-12135*x-564], ...]",
                "{[0, 0]=2623, [0, 0, 0]=303, [-1, 0]=161, [1, 0]=151, [0, -1]=146, [0, 1]=135, [-2, 0]=64," +
                " [0, 3]=64, [0, 2]=62, [-3, 0]=59}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<RationalPolynomialVector> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalPolynomialVectors(dimension),
                output,
                topSampleCount,
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
                "[[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], ...]",
                "{[]=100000}",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialVectors_int_helper(
                5,
                3,
                3,
                "[[-29/4*x^3-1/10*x^2-20*x+10/3, -5/6*x^4-1/427*x^3-89/2*x^2+47/3*x-2/53, 0]," +
                " [1/19*x^7-7/2*x^6-4*x^5-1/50*x^3-2/683*x-1/2, -1/367*x^3-24*x^2+9/2*x+4," +
                " 3*x^8-6268*x^7-x^6-1/4*x^5-1/16*x^4+2*x^3+12757/4452*x^2-10*x-6/17]," +
                " [0, 2, -1/7*x^4-1/7*x^3+2*x^2-1/35*x+363]," +
                " [0, -24/47*x^10+11/2*x^9-1/6*x^8+16*x^7+8*x^6-9/4*x^5+1/6*x^4+1/4*x^3+10*x^2+14/3, 0]," +
                " [1, 0, -x+1/2], [1/2, 468*x^5+2/5*x^4+2/3*x^2-13/4*x+9, 7*x^2-1/45*x-1]," +
                " [1/2*x+1, -7/9*x^5+2/7*x^4+1/48*x^3-1/7*x+3, 0], [6*x+1/69, 0, -1/4*x^4+8*x^2+257]," +
                " [-1/8*x-3, 1/53*x^3-x^2+x-53/5, 1/44]," +
                " [x-1/21, -28/5*x^7+3*x^5-1/22*x^4-7/10*x^3+1/2*x^2+1/23*x, -13/6]," +
                " [-9/2*x^4-1/3*x^3+5/7*x^2-3/2, -1/14*x^8-4/3*x^7+1/3*x^6+x^5+2/3763*x^4-2*x^3-4*x^2+8/3*x-1," +
                " -x^4-1/9*x^3-1/5*x^2-3*x-1/5], [9/29, -3*x+1, -111*x^6-1/2*x^5-2/67*x^4+19/99*x^3-1/12*x+1/5]," +
                " [-55/4*x^2+1/40*x-73/7, 0, -399*x^2+18/7*x-1/12]," +
                " [-1/3*x^6+1/7*x^5-20*x^4-1/5341*x^3+5/23*x^2-15*x, 1/7*x^5-x^4+2/199*x^2+2*x-9/632, 0]," +
                " [-7/3*x^7-31/2*x^6-824/27*x^5+23/402*x^4+31/2*x^3+7/8*x^2+3*x-3, 1/11," +
                " x^5-1/3*x^4+3/2*x^3-13/25*x^2-30*x-11/371], [0, 0, -1/7*x]," +
                " [-7/5*x^2-5/14*x+11/31, -x^10+1/59*x^9-115/174*x^8-x^7-1/4*x^6+x^5-1/5*x^4+2/3*x^3+x-61/141, 3]," +
                " [6, -1/3, 0], [-45, 213*x^8-x^7+2*x^6+x^5+2*x^4-1/3*x^2+3*x+3, 0]," +
                " [-x^4+371/5*x^3-2/3*x^2+1/853, -2*x-5, 0], ...]",
                "{[0, 0, 0]=1194, [0, 0, -1]=60, [0, 1, 0]=53, [0, 0, 1]=53, [1, 0, 0]=52, [0, -1, 0]=51," +
                " [-1, 0, 0]=50, [1/3, 0, 0]=26, [0, -3, 0]=26, [0, 0, -3]=24}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .rationalPolynomialVectors(),
                output,
                topSampleCount,
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
                "[[x+477, -2/3*x+1, 0], [], [0], [], [], [], [], [], [-1/15], [0], [], [], [], [0, 0, -1, 0]," +
                " [0, 0, -2], [], [0, 0], [], [], [-1], ...]",
                "{[]=49756, [0]=15336, [0, 0]=4725, [0, 0, 0]=1387, [1]=748, [-1]=704, [0, 0, 0, 0]=451, [0, 1]=240," +
                " [1, 0]=211, [-1, 0]=199}",
                1.0022699999996099,
                -0.2237620601232904,
                3.2825964010289757
        );
        rationalPolynomialVectors_helper(
                5,
                3,
                2,
                "[[-x^2+1/36*x-31/10, 0, -5/6*x^6-2*x^5+1/89*x^4-53/47*x^3+5/2*x^2-x-1, 0]," +
                " [-1/50*x^3-2/683*x-1/2, 0], [], [], []," +
                " [0, 2/3*x-5/7, -1/16*x^8+2*x^7+12757/4452*x^6+17/10*x^4-1/367*x^3-24*x^2+9/2*x+4], []," +
                " [-6268*x-1, x-3/718], [0], [-1/5, 14*x^2+2*x-5/6, -x^2+1/7*x+49/24, 0, 0], [2]," +
                " [16*x^12+8*x^11-9/4*x^10+1/6*x^9+1/4*x^8+10*x^7+14/3*x^5-29/19562*x^3-1/7*x^2+x+19/2, 1," +
                " -197/15*x^7-7/41*x^6+3*x^5+1/10*x^4-7/2*x^3-2/5*x^2-2/11*x-2/11," +
                " -3623/3*x^11-22/3*x^10-x^9+1/2*x^8-3/11*x^7-1/14*x^4+41*x^3-x^2-2/17*x], [1/6*x^2-2/5*x-2/21]," +
                " [1/3]," +
                " [-3/13, 2/5*x-1/2, 45/7*x^2-26*x, -1/115*x^6+1/2*x^5+23/3*x^2-5/17*x+3/1790," +
                " -1/4*x^13+8*x^11+257*x^9-1/7*x^8+6*x^7-7/9*x^6+2/7*x^5+1/48*x^4-1/7*x^2+3*x-3/7, -1/8*x-3," +
                " 1/53*x^3-x^2+x-53/5], [2/25, 0]," +
                " [-1/3*x-28, 4*x^2+2/3*x+3, 1/2*x-15/13, 3*x-6, -28/5*x+1/6, -13/6, -9/2*x^4-1/3*x^3+5/7*x^2-3/2," +
                " -1/14*x^8-4/3*x^7+1/3*x^6+x^5+2/3763*x^4-2*x^3-4*x^2+8/3*x-1]," +
                " [-3*x-1/5, 0, 0, -631/3*x^6-3*x^5+x^4+29*x^3+15*x+9]," +
                " [13/3, 0, 19/99*x, -399*x^9+18/7*x^8-1/12*x^7-55/4*x^6+1/40*x^5-41/7*x^4-111*x^2+5*x+232/3," +
                " -1/3*x^6+1/7*x^5-20*x^4-1/5341*x^3+5/23*x^2-15*x], [2*x-9/632, 0, 11*x^3+1/7*x-3], ...]",
                "{[]=33309, [0]=5105, [0, 0]=706, [-1]=240, [1]=212, [0, 0, 0]=139, [2]=89, [-3]=86, [3]=84," +
                " [-1/3]=83}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        rationalPolynomialVectors_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .rationalPolynomialVectorsAtLeast(minDimension),
                output,
                topSampleCount,
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
                "[[x+477, -2/3*x+1, 0], [], [0], [], [], [], [], [], [-1/15], [0], [], [], [], [0, 0, -1, 0]," +
                " [0, 0, -2], [], [0, 0], [], [], [-1], ...]",
                "{[]=49756, [0]=15336, [0, 0]=4725, [0, 0, 0]=1387, [1]=748, [-1]=704, [0, 0, 0, 0]=451, [0, 1]=240," +
                " [1, 0]=211, [-1, 0]=199}",
                1.0022699999996099,
                -0.2237620601232904,
                3.2825964010289757
        );
        rationalPolynomialVectorsAtLeast_helper(
                5,
                3,
                1,
                0,
                "[[0, -x^2+1/36*x-31/10, 0], [1/89*x^4-53/47*x^3+5/2*x^2-x-1, -22*x^3-4*x^2-5/6*x-1/427], []," +
                " [1/19*x^2-7/2*x]," +
                " [9/2*x, 2*x^6+12757/4452*x^5+17/10*x^3-1/367*x^2-1/6*x, -x-1/4," +
                " 14*x^9+2*x^8-5/6*x^7+x^6-2/25*x^5-1/38*x^4+x^3-718/59937*x^2+3*x, -x^2+1/7*x+49/24], [], [0], []," +
                " [], [], [0, 14/3*x^7-29/19562*x^5-1/7*x^4-1/7*x^3+2*x^2-1/35*x+363, -x^2-13*x-1/2]," +
                " [-2/5*x^6-2/11*x^5-24/47*x^4+11/2*x^3-1/6*x^2+16*x+8, -197/15*x^3-7/41*x^2+3*x+3/10," +
                " -3623/3*x^11-22/3*x^10-x^9+1/2*x^8-3/11*x^7-1/14*x^4+41*x^3-x^2-2/17*x, 1/2," +
                " 468*x^5+2/5*x^4+2/3*x^2-13/4*x+9], []," +
                " [0, 1/6*x^2+45/7*x, 1/48*x^9-1/7*x^7+3*x^6-1/115*x^5+1/2*x^4+23/3*x-9/17," +
                " 1/44*x^15-53/3*x^14-x^13+5/3*x^12+13/53*x^11-1/8*x^10-3*x^9-8/65*x^8-1/4*x^7+8*x^5+257*x^3-" +
                "1/7*x^2+6*x+7/11, x-1/21], [], [3*x^5-1/22*x^4-7/10*x^3+1/2*x^2+1/23*x], [], []," +
                " [x^4-154*x^3+11*x^2-28/5*x+1/6], [], ...]",
                "{[]=49988, [0]=5788, [0, 0]=673, [-1]=275, [1]=272, [1/3]=94, [-2]=86, [-1/3]=86, [1/2]=84," +
                " [-1/2]=83}",
                1.001089999999618,
                2.8693224385427754,
                4.963606933180434
        );
        rationalPolynomialVectorsAtLeast_helper(
                5,
                3,
                2,
                1,
                "[[0, -x^2+1/36*x-31/10, 0, -5/6*x^6-2*x^5+1/89*x^4-53/47*x^3+5/2*x^2-x-1]," +
                " [1/19*x^7-7/2*x^6-4*x^5-1/50*x^3-2/683*x-1/2]," +
                " [9/2*x, 2*x^6+12757/4452*x^5+17/10*x^3-1/367*x^2-1/6*x, -x-1/4," +
                " 14*x^9+2*x^8-5/6*x^7+x^6-2/25*x^5-1/38*x^4+x^3-718/59937*x^2+3*x, -x^2+1/7*x+49/24, 0], [0, 2]," +
                " [-1/35*x+363, -1/7*x-1/7, 0]," +
                " [11/2*x^9-1/6*x^8+16*x^7+8*x^6-9/4*x^5+1/6*x^4+1/4*x^3+10*x^2+14/3, -2/11]," +
                " [1/10*x^2-3/7, -17*x^5-197/15*x-7/41], [1/2*x^4-3/11*x^3+2/3]," +
                " [-2/5*x^4-1/21*x^3+1/3*x^2+5/7719, 0, 0], [0, 2/5*x^4-1/2*x^3-1/13*x^2+87*x+1/3]," +
                " [45/7*x^2-26*x, -1/115*x^6+1/2*x^5+23/3*x^2-5/17*x+3/1790], [-1/8*x-3]," +
                " [1/53*x^3-x^2+x-53/5, 1/44], [x-1/21], [-28/5*x^7+3*x^5-1/22*x^4-7/10*x^3+1/2*x^2+1/23*x]," +
                " [-3/2*x^4+13/15*x^3+7/3*x^2+x-282], [-1/2*x-13/6], [-1/3*x^2+5/7*x, 25*x^2-9/2*x-172/7], [-1]," +
                " [9/29], ...]",
                "{[0]=11659, [0, 0]=1367, [1]=522, [-1]=517, [1/2]=198, [-3]=179, [-2]=176, [-1/2]=174, [2]=169," +
                " [-1/3]=167}",
                2.0056700000002503,
                2.8553799977069874,
                4.962938822759252
        );
        rationalPolynomialVectorsAtLeast_helper(
                5,
                3,
                3,
                2,
                "[[0, -x^2+1/36*x-31/10, 0, -5/6*x^6-2*x^5+1/89*x^4-53/47*x^3+5/2*x^2-x-1, 0]," +
                " [-7/2*x^6-4*x^5-1/50*x^3-2/683*x-1/2, 19], [0, 9/2]," +
                " [x^10-2/25*x^9-1/38*x^8+x^7-718/59937*x^6+3*x^5-6268*x^4-x^3-1/4*x^2+x-4452," +
                " 1/7*x^6+17/24*x^5+14*x^4+2*x^3-5/6*x^2-1/84*x+6, 0, 0], [0, 363]," +
                " [19/2, 0, 14/3*x^3-29/19562*x-1/7]," +
                " [-13*x-1/2, -24/47*x^6+11/2*x^5-1/6*x^4+16*x^3+8*x^2+2/3*x-2]," +
                " [1/3*x^10-17*x^9-197/15*x^5-7/41*x^4+3*x^3+1/10*x^2-3/7, 0, 1/2*x^3-3/11*x^2-3/2*x-1/3]," +
                " [-2/5*x^4-1/21*x^3+1/3*x^2+5/7719, 0, 0, 2], [-1/2*x^2-1/13*x+87, 45/7*x^2-26*x+468]," +
                " [1/2*x+1, -7/9*x^5+2/7*x^4+1/48*x^3-1/7*x+3, 0], [0, -1/7*x^2+6*x+425/13, 0]," +
                " [-8/65*x^3-6/257, 13/53*x+1/2, 1/53*x^2-x+3, 1/44]," +
                " [x-1/21, -28/5*x^7+3*x^5-1/22*x^4-7/10*x^3+1/2*x^2+1/23*x], [-3/2*x^4+13/15*x^3+7/3*x^2+x-282, 0]," +
                " [-13/6, -9/2*x^4-1/3*x^3+5/7*x^2-3/2," +
                " -1/14*x^8-4/3*x^7+1/3*x^6+x^5+2/3763*x^4-2*x^3-4*x^2+8/3*x-1]," +
                " [-3*x-1/5, 0, 0, -631/3*x^6-3*x^5+x^4+29*x^3+15*x+9, -2/67*x^3+19/99*x^2+13/3," +
                " -1/12*x^3-55/4*x^2+1/40*x]," +
                " [2/199*x^10+2*x^9-5/632*x^8-1/3*x^7+1/7*x^6-20*x^5+23/14*x^4-1/5*x^3-2*x^2+7/2*x-399," +
                " -7/3*x^7-31/2*x^6-824/27*x^5+23/402*x^4+31/2*x^3+7/8*x^2+3*x-3, 1/11]," +
                " [-1/3*x^4+3/2*x^3-13/25*x^2-30*x-11/371, 14*x^4+31/9*x^3+2/11*x^2-1/7*x]," +
                " [-141*x^2-4/125*x+3/83, 1/59*x^5-115/174*x^4-x^3-1/4*x^2+x-1/5, -1/2], ...]",
                "{[0, 0]=2687, [0, 0, 0]=267, [-1, 0]=131, [0, -1]=122, [1, 0]=115, [0, 1]=114, [1/3, 0]=55," +
                " [2, 0]=48, [-3, 0]=46, [0, 0, 0, 0]=46}",
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
        QBarTesting.aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        QBarTesting.aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
        matrices_int_int_helper(
                1,
                0,
                0,
                "QBarRandomProvider_matrices_1_0_0",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                5,
                0,
                0,
                "QBarRandomProvider_matrices_5_0_0",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                10,
                0,
                0,
                "QBarRandomProvider_matrices_10_0_0",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                1,
                0,
                3,
                "QBarRandomProvider_matrices_1_0_3",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                5,
                0,
                3,
                "QBarRandomProvider_matrices_5_0_3",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                10,
                0,
                3,
                "QBarRandomProvider_matrices_10_0_3",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                3,
                3,
                0,
                "QBarRandomProvider_matrices_3_3_0",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                5,
                3,
                0,
                "QBarRandomProvider_matrices_5_3_0",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                10,
                3,
                0,
                "QBarRandomProvider_matrices_10_3_0",
                0.0,
                0.0
        );
        matrices_int_int_helper(
                1,
                1,
                1,
                "QBarRandomProvider_matrices_1_1_1",
                1.000000000007918,
                0.8333389999976124
        );
        matrices_int_int_helper(
                5,
                1,
                1,
                "QBarRandomProvider_matrices_5_1_1",
                1.000000000007918,
                4.889747000000939
        );
        matrices_int_int_helper(
                10,
                1,
                1,
                "QBarRandomProvider_matrices_10_1_1",
                1.000000000007918,
                9.918277000004432
        );
        matrices_int_int_helper(
                1,
                2,
                2,
                "QBarRandomProvider_matrices_1_2_2",
                4.000000000031672,
                0.8338739999974718
        );
        matrices_int_int_helper(
                5,
                2,
                2,
                "QBarRandomProvider_matrices_5_2_2",
                4.000000000031672,
                4.8827345000031865
        );
        matrices_int_int_helper(
                10,
                2,
                2,
                "QBarRandomProvider_matrices_10_2_2",
                4.000000000031672,
                9.922179249978022
        );
        matrices_int_int_helper(
                1,
                3,
                4,
                "QBarRandomProvider_matrices_1_3_4",
                11.999999999910093,
                0.8332544167092473
        );
        matrices_int_int_helper(
                5,
                3,
                4,
                "QBarRandomProvider_matrices_5_3_4",
                11.999999999910093,
                4.8834181664187115
        );
        matrices_int_int_helper(
                10,
                3,
                4,
                "QBarRandomProvider_matrices_10_3_4",
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
        matrices_helper(
                1,
                2,
                "QBarRandomProvider_matrices_1_2",
                1.9983549999915837,
                0.8338733608407535
        );
        matrices_helper(
                5,
                3,
                "QBarRandomProvider_matrices_5_3",
                3.3335839999865646,
                4.882194059052771
        );
        matrices_helper(
                10,
                8,
                "QBarRandomProvider_matrices_10_8",
                8.189681000015133,
                9.921846650751569
        );
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
        squareMatrices_helper(
                2,
                2,
                "QBarRandomProvider_squareMatrices_2_2",
                3.0031260000035953,
                1.833468525820871
        );
        squareMatrices_helper(
                5,
                3,
                "QBarRandomProvider_squareMatrices_5_3",
                4.821780000018768,
                4.883059368202011
        );
        squareMatrices_helper(
                10,
                8,
                "QBarRandomProvider_squareMatrices_10_8",
                13.49856700000753,
                9.923964892149032
        );
        squareMatrices_fail_helper(1, 2);
        squareMatrices_fail_helper(2, 1);
        squareMatrices_fail_helper(2, -1);
    }

    private static void rationalMatrices_helper(
            @NotNull Iterable<RationalMatrix> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        List<RationalMatrix> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
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
            @NotNull String topSampleCount,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).rationalMatrices(height, width),
                output,
                topSampleCount,
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
        rationalMatrices_int_int_helper(
                3,
                0,
                0,
                "[[]#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0," +
                " []#0, []#0, []#0, []#0, ...]",
                "{[]#0=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                5,
                0,
                0,
                "[[]#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0," +
                " []#0, []#0, []#0, []#0, ...]",
                "{[]#0=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                10,
                0,
                0,
                "[[]#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0, []#0," +
                " []#0, []#0, []#0, []#0, ...]",
                "{[]#0=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                3,
                0,
                3,
                "[[]#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3," +
                " []#3, []#3, []#3, []#3, ...]",
                "{[]#3=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                5,
                0,
                3,
                "[[]#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3," +
                " []#3, []#3, []#3, []#3, ...]",
                "{[]#3=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                10,
                0,
                3,
                "[[]#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3, []#3," +
                " []#3, []#3, []#3, []#3, ...]",
                "{[]#3=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                3,
                3,
                0,
                "[[[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []]," +
                " [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []]," +
                " [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], ...]",
                "{[[], [], []]=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                5,
                3,
                0,
                "[[[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []]," +
                " [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []]," +
                " [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], ...]",
                "{[[], [], []]=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                10,
                3,
                0,
                "[[[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []]," +
                " [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []]," +
                " [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], [[], [], []], ...]",
                "{[[], [], []]=100000}",
                0.0,
                0.0
        );
        rationalMatrices_int_int_helper(
                3,
                1,
                1,
                "[[[5]], [[221]], [[1]], [[-2/7]], [[1]], [[-2/3]], [[1]], [[0]], [[-1/2]], [[0]], [[0]], [[5/6]]," +
                " [[0]], [[2]], [[-3/4]], [[0]], [[0]], [[1/6]], [[-1/15]], [[-1]], ...]",
                "{[[0]]=36064, [[1]]=9116, [[-1]]=8847, [[2]]=2249, [[1/2]]=2249, [[-3]]=2231, [[1/3]]=2210," +
                " [[-1/2]]=2198, [[3]]=2187, [[-2]]=2186}",
                0.9999999999980838,
                2.7791000000017902
        );
        rationalMatrices_int_int_helper(
                5,
                1,
                1,
                "[[[1/36]], [[-1]], [[-29/4]], [[-1]], [[5/2]], [[-53/47]], [[1/89]], [[-2]], [[-5/6]], [[-4]]," +
                " [[-22]], [[-2/683]], [[0]], [[-1/50]], [[0]], [[-4]], [[-7/2]], [[1/19]], [[-1/2]], [[0]], ...]",
                "{[[0]]=16723, [[1]]=5651, [[-1]]=5622, [[1/3]]=1918, [[3]]=1891, [[-3]]=1884, [[2]]=1879," +
                " [[-2]]=1864, [[1/2]]=1845, [[-1/2]]=1825}",
                0.9999999999980838,
                4.8117800000005735
        );
        rationalMatrices_int_int_helper(
                10,
                1,
                1,
                "[[[21/13]], [[2304798/125]], [[-2]], [[1/25]], [[-117/219224]], [[-67/5785]], [[-70/8797]]," +
                " [[-10]], [[2/3]], [[62]], [[-4551]], [[2/227]], [[-239/978]], [[1/47084144]], [[0]], [[-2/35]]," +
                " [[-8/5]], [[-1/6268]], [[-504064/13]], [[-238/5]], ...]",
                "{[[0]]=5084, [[1]]=2166, [[-1]]=2165, [[2]]=913, [[-1/2]]=897, [[3]]=889, [[-3]]=884, [[1/2]]=883," +
                " [[-2]]=856, [[1/3]]=855}",
                0.9999999999980838,
                9.869249999999408
        );
        rationalMatrices_int_int_helper(
                3,
                2,
                2,
                "[[[5, 221], [1, -2/7]], [[1, -2/3], [1, 0]], [[-1/2, 0], [0, 5/6]], [[0, 2], [-3/4, 0]]," +
                " [[0, 1/6], [-1/15, -1]], [[-14/3, 0], [0, -1]], [[0, -1], [-6, 0]], [[-1, 0], [-16, -1/4]]," +
                " [[0, 0], [1, -1]], [[0, 167/17], [0, -27/8]], [[8/3, -6], [0, 1]], [[0, 0], [0, 1/3]]," +
                " [[-1, 1], [0, 10]], [[0, 0], [-5, 4/21]], [[-2/7, -1], [-1, 0]], [[3, 0], [3/14, 0]]," +
                " [[25, 0], [3/10, 0]], [[-1/3, -5/19], [-1/6, 0]], [[-1/2, -2], [1/3, 0]], [[0, 0], [-5, 0]], ...]",
                "{[[0, 0], [0, 0]]=1651, [[-1, 0], [0, 0]]=428, [[0, 0], [-1, 0]]=424, [[0, 0], [0, 1]]=424," +
                " [[0, 0], [0, -1]]=412, [[0, -1], [0, 0]]=408, [[1, 0], [0, 0]]=401, [[0, 0], [1, 0]]=400," +
                " [[0, 1], [0, 0]]=383, [[0, 1], [1, 0]]=128}",
                3.999999999992335,
                2.7808624999918714
        );
        rationalMatrices_int_int_helper(
                5,
                2,
                2,
                "[[[1/36, -1], [-29/4, -1]], [[5/2, -53/47], [1/89, -2]], [[-5/6, -4], [-22, -2/683]]," +
                " [[0, -1/50], [0, -4]], [[-7/2, 1/19], [-1/2, 0]], [[-1/6, -1/367], [17/10, 0]]," +
                " [[12757/4452, 2], [-1/16, -1/4]], [[-1, -6268], [3, -718/59937]], [[1, -1/38], [-2/25, 1]]," +
                " [[-5/6, 2], [14, 17/24]], [[1/7, -1], [0, 1]], [[15/619, 1], [2, -1/7]]," +
                " [[-1/7, -29/19562], [0, 14/3]], [[0, 10], [1/4, 1/6]], [[-9/4, 8], [16, -1/6]]," +
                " [[11/2, -24/47], [-2/11, -2/5]], [[-7/2, 1/10], [3, -7/41]], [[-197/15, 0], [0, 0]]," +
                " [[-17, 1/3], [0, 0]], [[-3/11, 1/2], [-1, -22/3]], ...]",
                "{[[0, 0], [0, 0]]=83, [[0, 1], [0, 0]]=41, [[1, 0], [0, 0]]=33, [[0, 0], [0, 1]]=31," +
                " [[0, 0], [-1, 0]]=31, [[-1, 0], [0, 0]]=30, [[0, -1], [0, 0]]=30, [[0, 0], [1, 0]]=30," +
                " [[0, 0], [0, -1]]=26, [[0, 0], [0, 2]]=17}",
                3.999999999992335,
                4.8148274999920595
        );
        rationalMatrices_int_int_helper(
                10,
                2,
                2,
                "[[[21/13, 2304798/125], [-2, 1/25]], [[-117/219224, -67/5785], [-70/8797, -10]]," +
                " [[2/3, 62], [-4551, 2/227]], [[-239/978, 1/47084144], [0, -2/35]]," +
                " [[-8/5, -1/6268], [-504064/13, -238/5]], [[-8/249, -15/13], [0, 0]]," +
                " [[0, 3/238], [122224/59, 19]], [[-7/3839, -1130/15659], [479, 1/12]], [[2/47, 438], [11, -98]]," +
                " [[1/6, 1/926], [-97/41, 1/15]], [[-30, -1/5], [139/9, -70/167]], [[3, 2/23], [23/7, 1/37]]," +
                " [[2260, -1/5], [-49/5, 23/3]], [[-7, -7], [49465/2, 9/13]], [[1, 0], [1/344, 3954017/6454]]," +
                " [[-3, 13/31], [-26/1537, -15]], [[1/3, -841], [-1/8, 13/53]], [[5/4, -21], [3/1760, 0]]," +
                " [[13/5, 6312/23], [1/3, -1/7]], [[-34/15, -1], [20, 1/9]], ...]",
                "{[[0, 1], [0, 1]]=2, [[0, 0], [0, 2]]=2, [[-1, 0], [0, 0]]=2, [[21/13, 2304798/125], [-2, 1/25]]=1," +
                " [[-117/219224, -67/5785], [-70/8797, -10]]=1, [[2/3, 62], [-4551, 2/227]]=1," +
                " [[-239/978, 1/47084144], [0, -2/35]]=1, [[-8/5, -1/6268], [-504064/13, -238/5]]=1," +
                " [[-8/249, -15/13], [0, 0]]=1, [[0, 3/238], [122224/59, 19]]=1}",
                3.999999999992335,
                9.847875000001702
        );
        rationalMatrices_int_int_helper(
                3,
                3,
                4,
                "[[[5, 221, 1, -2/7], [1, -2/3, 1, 0], [-1/2, 0, 0, 5/6]]," +
                " [[0, 2, -3/4, 0], [0, 1/6, -1/15, -1], [-14/3, 0, 0, -1]]," +
                " [[0, -1, -6, 0], [-1, 0, -16, -1/4], [0, 0, 1, -1]]," +
                " [[0, 167/17, 0, -27/8], [8/3, -6, 0, 1], [0, 0, 0, 1/3]]," +
                " [[-1, 1, 0, 10], [0, 0, -5, 4/21], [-2/7, -1, -1, 0]]," +
                " [[3, 0, 3/14, 0], [25, 0, 3/10, 0], [-1/3, -5/19, -1/6, 0]]," +
                " [[-1/2, -2, 1/3, 0], [0, 0, -5, 0], [-1/2, 0, 0, 0]]," +
                " [[3, -1/5, 0, 0], [68, 1, -1/3, 0], [0, -3, 3/2, -5]]," +
                " [[0, -5, 0, 0], [0, -4, 0, 0], [0, 0, 1/3, 0]]," +
                " [[-1, -1/2, 20, 0], [1, 0, 1, 0], [1/2, -1/2, -2, 0]]," +
                " [[3, 0, -4, -1], [3, 0, -1, 0], [1/6, 0, 1, 0]]," +
                " [[0, 0, -13/4, 1], [1, -1, -1/2, 0], [1/10, 3, -7/283, 0]]," +
                " [[0, 0, -1, 0], [-3/11, 3, 2/5, 3], [0, 3, -2, 12]]," +
                " [[0, 1/2, -1, 0], [-1, 1/3, 0, -1/9], [0, 0, 1, 3/5]]," +
                " [[-1/56, 0, 0, 2/15], [1/3, -2, 1, -1/3], [1, -1/2, -1/4, 1]]," +
                " [[1, 0, 0, -3], [0, 2, 0, 0], [-1, 1/2, -1, 0]]," +
                " [[0, -7/2, 1/3, 0], [-1, 2, 1/7, -1/8], [-59/3, -1/9, 1/21, -1]]," +
                " [[-1, -1, 0, 0], [-1/3, 0, -5, 0], [0, -1/17, -1, -8/3]]," +
                " [[-1/4, 1, 0, 16], [-1, 1/3, 1, 0], [0, -1/2, 1/7, 1]]," +
                " [[-1, 1, 0, 1/3], [7, -1, -1, 1], [1, 2/3, -3, 0]], ...]",
                "{[[5, 221, 1, -2/7], [1, -2/3, 1, 0], [-1/2, 0, 0, 5/6]]=1," +
                " [[0, 2, -3/4, 0], [0, 1/6, -1/15, -1], [-14/3, 0, 0, -1]]=1," +
                " [[0, -1, -6, 0], [-1, 0, -16, -1/4], [0, 0, 1, -1]]=1," +
                " [[0, 167/17, 0, -27/8], [8/3, -6, 0, 1], [0, 0, 0, 1/3]]=1," +
                " [[-1, 1, 0, 10], [0, 0, -5, 4/21], [-2/7, -1, -1, 0]]=1," +
                " [[3, 0, 3/14, 0], [25, 0, 3/10, 0], [-1/3, -5/19, -1/6, 0]]=1," +
                " [[-1/2, -2, 1/3, 0], [0, 0, -5, 0], [-1/2, 0, 0, 0]]=1," +
                " [[3, -1/5, 0, 0], [68, 1, -1/3, 0], [0, -3, 3/2, -5]]=1," +
                " [[0, -5, 0, 0], [0, -4, 0, 0], [0, 0, 1/3, 0]]=1," +
                " [[-1, -1/2, 20, 0], [1, 0, 1, 0], [1/2, -1/2, -2, 0]]=1}",
                12.000000000020316,
                2.7812433333100226
        );
        rationalMatrices_int_int_helper(
                5,
                3,
                4,
                "[[[1/36, -1, -29/4, -1], [5/2, -53/47, 1/89, -2], [-5/6, -4, -22, -2/683]]," +
                " [[0, -1/50, 0, -4], [-7/2, 1/19, -1/2, 0], [-1/6, -1/367, 17/10, 0]]," +
                " [[12757/4452, 2, -1/16, -1/4], [-1, -6268, 3, -718/59937], [1, -1/38, -2/25, 1]]," +
                " [[-5/6, 2, 14, 17/24], [1/7, -1, 0, 1], [15/619, 1, 2, -1/7]]," +
                " [[-1/7, -29/19562, 0, 14/3], [0, 10, 1/4, 1/6], [-9/4, 8, 16, -1/6]]," +
                " [[11/2, -24/47, -2/11, -2/5], [-7/2, 1/10, 3, -7/41], [-197/15, 0, 0, 0]]," +
                " [[-17, 1/3, 0, 0], [-3/11, 1/2, -1, -22/3], [-3623/3, 1/3, -1/21, -2/5]]," +
                " [[1/6, 1, 1/3, 87], [-1/13, -1/2, 2/5, 468], [-26, 45/7, 1/6, 1/1790]]," +
                " [[-5/17, 23/3, 0, 0], [1/2, -1/115, 3, -1/7], [0, 1/48, 2/7, -7/9]]," +
                " [[6, -1/7, 257, 0], [8, 0, -1/4, -8/65], [-3, -1/8, 13/53, 5/3]]," +
                " [[-1, -53/3, 1/44, 1/2], [0, 5, -1/2, -3/4], [1/3, -15/13, 1/2, -7/10]]," +
                " [[-1/22, 3, 0, -28/5], [11, -154, 1, 7/3], [13/15, -3/2, 0, 1/1733]]," +
                " [[3/29, -1/2, 0, 5/7], [-1/3, -9/2, 25, 0], [-1, 8/3, -4, -2]]," +
                " [[2/3763, 1, 1/3, -4/3], [-1/14, 0, -1/3, 5], [9, 15, 0, 29]]," +
                " [[1, -3, -631/3, -18], [5/3, 0, 19/99, -2/67], [-1/2, -111, 0, -41/7]]," +
                " [[1/40, -55/4, -1/12, 18/7], [-399, 7/2, -2, -1/5], [23/14, -20, 1/7, -1/3]]," +
                " [[-5/632, 2, 2/199, 0], [-1, 1/7, 0, 11], [-3, 3, 7/8, 31/2]]," +
                " [[23/402, -824/27, -31/2, -7/3], [-3, -1/14, -1/7, 0], [-11, 13/7, -30, -13/25]]," +
                " [[3/2, -1/3, 1, -1/4], [0, -1/7, 2/11, 31/9], [14, -1/7, -83/4, -141]]," +
                " [[0, 1, -1/4, -1], [-115/174, 1/59, -1, 3], [1/7, 6, 0, -3/2]], ...]",
                "{[[1/36, -1, -29/4, -1], [5/2, -53/47, 1/89, -2], [-5/6, -4, -22, -2/683]]=1," +
                " [[0, -1/50, 0, -4], [-7/2, 1/19, -1/2, 0], [-1/6, -1/367, 17/10, 0]]=1," +
                " [[12757/4452, 2, -1/16, -1/4], [-1, -6268, 3, -718/59937], [1, -1/38, -2/25, 1]]=1," +
                " [[-5/6, 2, 14, 17/24], [1/7, -1, 0, 1], [15/619, 1, 2, -1/7]]=1," +
                " [[-1/7, -29/19562, 0, 14/3], [0, 10, 1/4, 1/6], [-9/4, 8, 16, -1/6]]=1," +
                " [[11/2, -24/47, -2/11, -2/5], [-7/2, 1/10, 3, -7/41], [-197/15, 0, 0, 0]]=1," +
                " [[-17, 1/3, 0, 0], [-3/11, 1/2, -1, -22/3], [-3623/3, 1/3, -1/21, -2/5]]=1," +
                " [[1/6, 1, 1/3, 87], [-1/13, -1/2, 2/5, 468], [-26, 45/7, 1/6, 1/1790]]=1," +
                " [[-5/17, 23/3, 0, 0], [1/2, -1/115, 3, -1/7], [0, 1/48, 2/7, -7/9]]=1," +
                " [[6, -1/7, 257, 0], [8, 0, -1/4, -8/65], [-3, -1/8, 13/53, 5/3]]=1}",
                12.000000000020316,
                4.808581666635954
        );
        rationalMatrices_int_int_helper(
                10,
                3,
                4,
                "[[[21/13, 2304798/125, -2, 1/25], [-117/219224, -67/5785, -70/8797, -10], [2/3, 62, -4551, 2/227]]," +
                " [[-239/978, 1/47084144, 0, -2/35], [-8/5, -1/6268, -504064/13, -238/5], [-8/249, -15/13, 0, 0]]," +
                " [[0, 3/238, 122224/59, 19], [-7/3839, -1130/15659, 479, 1/12], [2/47, 438, 11, -98]]," +
                " [[1/6, 1/926, -97/41, 1/15], [-30, -1/5, 139/9, -70/167], [3, 2/23, 23/7, 1/37]]," +
                " [[2260, -1/5, -49/5, 23/3], [-7, -7, 49465/2, 9/13], [1, 0, 1/344, 3954017/6454]]," +
                " [[-3, 13/31, -26/1537, -15], [1/3, -841, -1/8, 13/53], [5/4, -21, 3/1760, 0]]," +
                " [[13/5, 6312/23, 1/3, -1/7], [-34/15, -1, 20, 1/9], [5395/2, 3, -149/7, -125/308]]," +
                " [[1/69, -3618, -5656/31, 1/11], [1/59, 33, -9/76, 1], [-1, 246/48401, 3/112, 111]]," +
                " [[-1/4, 19/11, 237/832, 1/4], [14, 3/5, -58/111, -2/4079803], [5, -12, -5/567, -1/3]]," +
                " [[21, 280/11, -20672/9, 1/519], [-15/1706, -1/22, -2/31, 1/3], [-43124/2505, 3/2, -1, -3/2]]," +
                " [[-4, 2/11, 3/3361, -3/11075], [0, -1/2, 53/9, 1/78], [-383, -1/40, 1/2, 0]]," +
                " [[-30725/11, -1/46542, -2588/93967625, -1], [-6299/2, -1083, 5/7, -3]," +
                " [-5/14, -13/3473, -729/2, 0]]," +
                " [[1/454, -78, 186, 1/63], [-40, 37, -9/2, -893924/13], [13, -1, 17, 79/3]]," +
                " [[7/131, -3239437/3, 71/2, 0], [-2, -203/177, 2/3, -209/10], [-157/3, -1, 1/11, -246]]," +
                " [[3/79, 1/6829, -1/6, -4], [6, -24, 4, 3/38], [11/4, 30413/72, -9/37841, -2/483]]," +
                " [[-24/7, 244/5, -2/63, -1], [3/46, 40, 0, -1], [-1, -1/2, 1, 0]]," +
                " [[-3/80, -740186/147, -5/6, 1], [-1/18, 1/5, -5/8, 1/26], [2, -33/134, 2906/83, -1/5]]," +
                " [[-1/11, -273454834/17, 37/4, -1/83490], [-23/117, 4/10689, 36/11, -31532]," +
                " [-1/4, 1/45, -379/102162, -13/191]]," +
                " [[-1/54, -2, 1, -61/221], [-28, -4/9, 1064, 1/36], [-1610/3, -159, 2/60847, 11/6]]," +
                " [[-49194, -12/52567, 1, 119/2], [1/11, 721/26, 5/14, 4052], [1/4, 5/371, -1384595/76, 14/3]], ...]",
                "{[[21/13, 2304798/125, -2, 1/25], [-117/219224, -67/5785, -70/8797, -10]," +
                " [2/3, 62, -4551, 2/227]]=1," +
                " [[-239/978, 1/47084144, 0, -2/35], [-8/5, -1/6268, -504064/13, -238/5], [-8/249, -15/13, 0, 0]]=1," +
                " [[0, 3/238, 122224/59, 19], [-7/3839, -1130/15659, 479, 1/12], [2/47, 438, 11, -98]]=1," +
                " [[1/6, 1/926, -97/41, 1/15], [-30, -1/5, 139/9, -70/167], [3, 2/23, 23/7, 1/37]]=1," +
                " [[2260, -1/5, -49/5, 23/3], [-7, -7, 49465/2, 9/13], [1, 0, 1/344, 3954017/6454]]=1," +
                " [[-3, 13/31, -26/1537, -15], [1/3, -841, -1/8, 13/53], [5/4, -21, 3/1760, 0]]=1," +
                " [[13/5, 6312/23, 1/3, -1/7], [-34/15, -1, 20, 1/9], [5395/2, 3, -149/7, -125/308]]=1," +
                " [[1/69, -3618, -5656/31, 1/11], [1/59, 33, -9/76, 1], [-1, 246/48401, 3/112, 111]]=1," +
                " [[-1/4, 19/11, 237/832, 1/4], [14, 3/5, -58/111, -2/4079803], [5, -12, -5/567, -1/3]]=1," +
                " [[21, 280/11, -20672/9, 1/519], [-15/1706, -1/22, -2/31, 1/3], [-43124/2505, 3/2, -1, -3/2]]=1}",
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
            @NotNull String topSampleCount,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rationalMatrices(),
                output,
                topSampleCount,
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
        rationalMatrices_helper(
                3,
                2,
                "[[[477, 1, -2/7], [1, -2/3, 1]], [[-1/2]], []#0, [[0]]," +
                " [[2, -3/4, 0, 0, 1/6], [-1/15, -1, -14/3, 0, 0]], [[]], [[0, 0], [-1, -6], [0, -1]], [[]]," +
                " [[0, -1/4, 0, 0, 1, -1, 0]], [[167/17], [0]], [[0, 1], [-1, -1], [1/38, 0], [1/3, -1]], []#2," +
                " [[-1, -1]], [[21/2, -2/7]], [[]], []#1, [[-1], [0]], [[0, 0, 25]], [[3/10]]," +
                " [[1/9, -1/3], [-5/19, -1/6], [0, -1/2]], ...]",
                "{[[0]]=6725, []#0=6237, []#1=4195, [[]]=3155, []#2=2836, []#3=1915, [[-1]]=1681, [[], []]=1580," +
                " [[1]]=1571, [[0], [0]]=1226}",
                3.0089700000010176,
                2.781300577932931
        );
        rationalMatrices_helper(
                5,
                3,
                "[[[10/3, -20, -1/10], [-29/4, -1, 5/2]], [[-3/47]], [[-25/2, -1/427, -5/6, -4]]," +
                " [[-1, -2/683], [0, -1/50]], [[]], [[0]], []#1, [[1/3, 19]], [[0]], [[-2/367]], [[1/50]]," +
                " [[1/2760, -6/17]], [[1, 12757/4452]], [[]], [[-1/4]]," +
                " [[-1/14460, 0, 3], [-718/59937, 1, -1/38], [-2/25, 1, -5/6]], [[-1/14]]," +
                " [[1/17, -8], [-3/13, -1]], [[0, 1]], [[1, 2]], ...]",
                "{[]#0=4502, [[0]]=3558, []#1=3040, []#2=1992, []#3=1404, [[]]=1371, [[-1]]=1202, [[1]]=1163," +
                " []#4=949, [[], []]=688}",
                3.362760000001472,
                4.8159131189959234
        );
        rationalMatrices_helper(
                10,
                8,
                "[[[6499102/125, -2, 1/25, -117/219224, -67/5785, -70/8797], [-10, 2/3, 62, -4551, 2/227, -239/978]," +
                " [1/47084144, 0, -2/35, -8/5, -1/6268, -504064/13]]," +
                " [[-238/5, -8/249, -15/13, 0], [0, 0, 3/238, 122224/59], [19, -7/3839, -1130/15659, 479]," +
                " [1/12, 2/47, 438, 11]], [[-7], [-10/3], [2462/21649]], [[-234/41, 1/15, -30]], [[4, -5/39]]," +
                " [[-206/167, 3, 2/23]], []#6, [[2260, -1/5, -49/5, 23/3, -7], [-7, 49465/2, 9/13, 1, 0]]," +
                " [[79602, -3/5, 97/3, 47, -11/9, -26/1537, -15, 1/3]," +
                " [-841, -1/8, 13/53, 5/4, -21, 3/1760, 0, 13/5]], [[424/23]], [[26], [3]], [[-34/15], [-1]]," +
                " [[1498/115, -1/6, 1/9, 5395/2]]," +
                " [[-149/7, -125/308, 1/69, -3618, -5656/31, 1/11], [1/59, 33, -9/76, 1, -1, 246/48401]," +
                " [3/112, 111, -1/4, 19/11, 237/832, 1/4]]," +
                " [[3/5, -58/111, -2/4079803], [5, -12, -5/567], [-1/3, 21, 280/11], [-20672/9, 1/519, -15/1706]," +
                " [-1/22, -2/31, 1/3], [-43124/2505, 3/2, -1]], [[-3/2, -4], [2/11, 3/3361], [-3/11075, 0]]," +
                " [[-2, -4, 2/21, 174], [146/383, 1/2, 0, -30725/11]], [[-1/46542]]," +
                " [[-253/249, -2588/93967625], [-1, -6299/2]], [[-1083]], ...]",
                "{[]#0=2046, []#1=1585, []#2=1160, []#3=840, []#4=653, [[0]]=546, []#5=481, []#6=398, [[]]=295," +
                " []#7=271}",
                8.16995000000032,
                9.840558387733228
        );
        rationalMatrices_fail_helper(2, 2);
        rationalMatrices_fail_helper(3, 1);
        rationalMatrices_fail_helper(3, -1);
    }

    private static void squareRationalMatrices_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanElementCount,
            double meanCoordinateBitSize
    ) {
        rationalMatrices_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).squareRationalMatrices(),
                output,
                topSampleCount,
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
                "[[[3/221, 1], [1/2, 1]], [[1]], [[0, 0], [5/6, 0]], [[0]], [[-3/4, 0], [0, 1/6]], []#0," +
                " [[-2, -14/3, 0, 0], [-1, 0, -1, -6], [0, -1, 0, -16], [-1/4, 0, 0, 1]], []#0, [[-1]], []#0," +
                " [[-49/2, -27/8, 8/3, -6, 0, 1, 0], [0, 0, 1/3, -1, 1, 0, 10], [0, 0, -5, 4/21, -2/7, -1, -1]," +
                " [0, 3, 0, 3/14, 0, 25, 0], [3/10, 0, -1/3, -5/19, -1/6, 0, -1/2], [-2, 1/3, 0, 0, 0, -5, 0]," +
                " [-1/2, 0, 0, 0, 3, -1/5, 0]], []#0," +
                " [[0, 1, -1/3, 0, 0, -3, 3/2, -5], [0, -5, 0, 0, 0, -4, 0, 0], [0, 0, 1/3, 0, -1, -1/2, 20, 0]," +
                " [1, 0, 1, 0, 1/2, -1/2, -2, 0], [3, 0, -4, -1, 3, 0, -1, 0], [1/6, 0, 1, 0, 0, 0, -13/4, 1]," +
                " [1, -1, -1/2, 0, 1/10, 3, -7/283, 0], [0, 0, -1, 0, -3/11, 3, 2/5, 3]], [[7]], []#0, []#0," +
                " [[0, 0, 1/2, -1], [0, -1, 1/3, 0], [-1/9, 0, 0, 1], [3/5, -1/56, 0, 0]], [[1/3]], [[1]], [[1]]," +
                " ...]",
                "{[]#0=33397, [[0]]=12011, [[-1]]=2934, [[1]]=2887, [[3]]=774, [[-1/3]]=774, [[2]]=747," +
                " [[-1/2]]=746, [[-2]]=725, [[1/2]]=718}",
                4.039990000000922,
                2.778798957428391
        );
        squareRationalMatrices_helper(
                5,
                3,
                "[[[1/13, 1/36], [-1, -29/4]], []#0, []#0, [[1/2]], [[1/89]], [[-5/6, -4], [-22, -2/683]], [[1/2]]," +
                " [[-2, 1], [-3/7, 2/3]]," +
                " [[-1/2, 0, -1/6, -1/367], [17/10, 0, 12757/4452, 2], [-1/16, -1/4, -1, -6268]," +
                " [3, -718/59937, 1, -1/38]], [[-1/5, 1], [-5/6, 2]], [[6/17, -8], [-3/13, -1]], [[363]]," +
                " [[19/2, 1], [-1/7, -29/19562]], [[0, 10], [1/4, 1/6]], [[8, 16], [-1/6, 11/2]], []#0," +
                " [[1/2, 11/2, -7/2, 1/10, 3], [-7/41, -197/15, 0, 0, 0], [-17, 1/3, 0, 0, -3/11]," +
                " [1/2, -1, -22/3, -3623/3, 1/3], [-1/21, -2/5, 1/6, 1, 1/3]], [[7, -1/13], [-1/2, 2/5]]," +
                " [[8, -26, 45/7], [1/6, 1/1790, -5/17], [23/3, 0, 0]], [[1]], ...]",
                "{[]#0=20027, [[0]]=6761, [[1]]=2266, [[-1]]=2171, [[2]]=782, [[1/3]]=772, [[3]]=763, [[-3]]=750," +
                " [[-1/2]]=747, [[-1/3]]=730}",
                4.8123099999997105,
                4.8100080834360694
        );
        squareRationalMatrices_helper(
                10,
                8,
                "[[[85/76574, -233/2, -1/2], [25/53, -2/171, -70/8797], [-10, 2/3, 62]]," +
                " [[-17, 2/227], [-239/978, 1/47084144]]," +
                " [[-4452, -35/4, -1/6268, -504064/13, -238/5, -8/249, -15/13, 0, 0, 0, 3/238, 122224/59, 19," +
                " -7/3839, -1130/15659]," +
                " [479, 1/12, 2/47, 438, 11, -98, 1/6, 1/926, -97/41, 1/15, -30, -1/5, 139/9, -70/167, 3]," +
                " [2/23, 23/7, 1/37, 2260, -1/5, -49/5, 23/3, -7, -7, 49465/2, 9/13, 1, 0, 1/344, 3954017/6454]," +
                " [-3, 13/31, -26/1537, -15, 1/3, -841, -1/8, 13/53, 5/4, -21, 3/1760, 0, 13/5, 6312/23, 1/3]," +
                " [-1/7, -34/15, -1, 20, 1/9, 5395/2, 3, -149/7, -125/308, 1/69, -3618, -5656/31, 1/11, 1/59, 33]," +
                " [-9/76, 1, -1, 246/48401, 3/112, 111, -1/4, 19/11, 237/832, 1/4, 14, 3/5, -58/111, -2/4079803, 5]," +
                " [-12, -5/567, -1/3, 21, 280/11, -20672/9, 1/519, -15/1706, -1/22, -2/31, 1/3, -43124/2505, 3/2," +
                " -1, -3/2]," +
                " [-4, 2/11, 3/3361, -3/11075, 0, -1/2, 53/9, 1/78, -383, -1/40, 1/2, 0, -30725/11, -1/46542," +
                " -2588/93967625]," +
                " [-1, -6299/2, -1083, 5/7, -3, -5/14, -13/3473, -729/2, 0, 1/454, -78, 186, 1/63, -40, 37]," +
                " [-9/2, -893924/13, 13, -1, 17, 79/3, 7/131, -3239437/3, 71/2, 0, -2, -203/177, 2/3, -209/10," +
                " -157/3]," +
                " [-1, 1/11, -246, 3/79, 1/6829, -1/6, -4, 6, -24, 4, 3/38, 11/4, 30413/72, -9/37841, -2/483]," +
                " [-24/7, 244/5, -2/63, -1, 3/46, 40, 0, -1, -1, -1/2, 1, 0, -3/80, -740186/147, -5/6]," +
                " [1, -1/18, 1/5, -5/8, 1/26, 2, -33/134, 2906/83, -1/5, -1/11, -273454834/17, 37/4, -1/83490," +
                " -23/117, 4/10689]," +
                " [36/11, -31532, -1/4, 1/45, -379/102162, -13/191, -1/54, -2, 1, -61/221, -28, -4/9," +
                " 1064, 1/36, -1610/3]," +
                " [-159, 2/60847, 11/6, -49194, -12/52567, 1, 119/2, 1/11, 721/26, 5/14, 4052, 1/4, 5/371," +
                " -1384595/76, 14/3]]," +
                " [[-2, 173/1162, 1/11, 1/4, -14/39], [0, -1/3, -1, -1/1018, -1]," +
                " [980391/28, 11/3, 30/19, -341, 149464593856/29], [1/41, -4/3, -69, -6, 99]," +
                " [-1/2271, 3/25, 127/3, -234/7, -1/1040299]]," +
                " [[0, 1/24, -91/46521, 1/103, 51], [7/2, -2351/9647, 71/177, 0, -61/10434]," +
                " [-3/13, 6/19, -80943/22, 13/161, 1/7], [180/118457, -7, -1, 3219962, 437921]," +
                " [-1/107, 34/95, 1/928, -79/12, -161/19]]," +
                " [[-1, 68/13, 1, 1/3], [-99/53, -3747/10, 1, -29/3], [61, -1/3, -77/3, -7/5624]," +
                " [-1/3, 9/2, 1813/2, -16/229]]," +
                " [[-103/45, 45, -7/789, -7/30], [-42283/7, 1879486/627, -2721/4, -23/24]," +
                " [53674773437, 1/10679, 41, -32/15], [0, 1, 215539/12, -1006]], [[-506/15]]," +
                " [[1045/5855452, 1, -19/54, -3/8, -529, -4/24537, 36637/4, -8/23, -1/863, 1901/6, -3096309, -5/3," +
                " -381, -5/3, 1/32, 196/29, -5/4, -3/59, 1/10, 9/122]," +
                " [-185/7, 1/55, 3, -43/54, 1/188272, -73/3, 13/124925, -1/20, 230/3, 2/7, 2, 1, -116/7, -1/58," +
                " -2/3, 41/3, -1, -7/13, -155/4, 4/75]," +
                " [8, -56, 743/8, -1/1969, -6/359383, 222572441, -14/51, 14, 42/31, -1/2, -1, 149/10195, 0," +
                " -6029/15, 0, 49/8, -12/161, 0, -23817/4, 25869/2]," +
                " [-11, -1, 15/10023956, -8357/7, -48/43, 0, -2/20257, 1, 1673/15, -1/9, 1, -3/13, 4/3, 3070/7," +
                " 2/4283, 5, -1509/100, -1481/123, 5/2, -1/98]," +
                " [-1538, -8, 3/5, -2209/3, 5, 9, 0, 1/4, -26456/5, -12978, 1/15, 42, 25/21, -25/61, -41, -29915, 4," +
                " 5/6, 118348/63, -71/64]," +
                " [3/15160, -1/25, -102/13, 3/2, 7, -2/7, -39/17, 0, -220, -105/289, -938653/63, -1, 10, -5/2, -9/2," +
                " 7, -4, -5/7, 53/7, 0]," +
                " [36275, 2, 10, -11, 0, 478/3, -11, 1388, 18754, -2, -25/8, -6/11, 0, 5/124, 1/11, -59/4, -23/6," +
                " -6, 30, -8/61]," +
                " [-14/221, -3/169, -1/517467, -1/3, -4/2361, -8/11, 9/14, 3/4, -7/568, -5/7, -6, 244, -17/344," +
                " -1/594, -1/14, 1/17, -28/239, -10522907/169, 0, 15/7]," +
                " [1, 1/15, 5/32, 7779/53, -1586/9, -3/2, -102/19, 2/7, -11, 30/7, 546/5, -58, -28/117, -34/883," +
                " -2/13, -433/5, 62/161, -3622/10014655, 0, -2019/7]," +
                " [-1/32170, 0, -3/35, 1/493, -1/2, 6, 1093/401, -7/3, 61199/15, 15/2, -1/40, -3, -10991/2, 15/7," +
                " -13/2, 14, 0, -2/3, 1087529/15, 1/20]," +
                " [1/3, 23/14610, -1/61851, 1/215, -28/19, 877447, 3, -3, -18/5, 8/177, 1/2, 3/4, 9/507515, 10/3," +
                " 3299, 3/5, -6555095741/48, 17/13, 1/9, 1]," +
                " [251/11, 168/41, -15/7, -423/7, -475/63, 130748/5, 639726/5, -1/6, 0, -952629/7, -2, -2," +
                " 19047/125351, -35/2, 2/25, -3/22, -10229070/13, 13/9, 0, -1/180]," +
                " [2/213, 1, -63787, 1, 1073/43, -2/3, 18097/57, 1/3, -2700/43, 8/5, 1/2, 975, 1/2, -1/5, 98/218937," +
                " 31/3, -3/25208, -49, 73, 4/39]," +
                " [249, -1, -2/43, -1/107, 1958/13, -2/7, 104/7, -30/11, -52, 1/7477, 148, 2/51, 7/7730, -1/3," +
                " 110/41, 1/64704, 1/140, 1525225/73, 0, 78]," +
                " [14077/3, 0, 1/3, 11712/11, 3/121, 5/3, -3, 431/9, -2, -5/4363, 1/76, 13/6, -1/33, 0, -2/57," +
                " -43/3, 0, 475/56, -37817/3, -17/2]," +
                " [-3/16, -1/3, 338/133, -9/2, -18, 5890, 1/3, 0, -1/35, 2, 47/242, 49, 0, 53, -417, 9769/15," +
                " 479711/751, -1/6238, -3, -26]," +
                " [-1/2, -2, 7/114253, -13/53956, -5/173, -559, -44219/8023, 4144689/4087, -7/3, 620, 14, 1, 3," +
                " -3/2, 375, -1/8567, 2/857, -7292, -2/31, -2/15]," +
                " [3, 2/15, -17/13, 0, 2, 1/203, -7/1838, 1/3, 9/36025, -51564/13, -1/41, -25, 1/4, 2670, 11/16," +
                " -4/3, 0, 1, 2/4617, -313/3]," +
                " [2781/160, 5375/7, -15, -1/25, -1/1318, 0, 1/17, -7/8171, 1/83, -95, 17/6, -3/56, -4/175, 1/762," +
                " -503/48, 6/5, -1, 2/9, -1/12, 53/7]," +
                " [-1/7342, -129, 525, -30, 233, 1/263, -466/15, 4263/10, -157/3437, 40243/2, 10065, -1/44, 123/391," +
                " -53/3, 1253/14111, 0, -25/24, -3139/14, 2823802215/4, 0]], []#0," +
                " [[-2975153/5]]," +
                " [[0, -3/5, 3/7, -1/3], [-11281/6888, 105409/247, -1301/227, 0], [-1529/3, -10/11, 13/12, -181]," +
                " [30, 1/50, 65/7, 5/14]], [[7/4]]," +
                " [[-3313, -1/6955, 1167/4, -14/55, 1/2], [3/18067, -1, 1, 330, 14/167]," +
                " [-2, -13/96848, -10, 1/177, 46/9], [1/174, 51/7, -47/3, -31/21, -1/25]," +
                " [-1, -15/301, -2/381409, 124, 818/7]]," +
                " [[73/247, 1/44, 5/3286, -241620/61, -5651], [-3/55937, 19/6665, -3/53, 0, 0]," +
                " [-26/7, 1/15, -671/2, -1/4, -811/5780], [1/2, -13/36, -1/353, 29/21, -1]," +
                " [-7/9, -15472/141, -2/27, 499/351, 109/8351]]," +
                " [[-75/52, -2/3, -1/8], [-5/7, 1/6092863, 31/3], [-36/7, -1/15, 1/2]]," +
                " [[-2/39, 0, 25/122, -3716/75, -13/8], [-4/113, -21/2, 25/79, 0, 4200286]," +
                " [7, 1/7, 1699/136, -1, 303/86], [5/481216, -31/5, -56, -24/53, -1/9]," +
                " [-15/3016, 0, 6, -14/531, -27]], [[9]], [[0]], [[-29/73]], ...]",
                "{[]#0=10082, [[0]]=1545, [[1]]=667, [[-1]]=601, [[2]]=273, [[-3]]=261, [[1/2]]=260, [[-1/2]]=260," +
                " [[3]]=258, [[-2]]=248}",
                13.395589999994662,
                9.83388861560576
        );
        squareRationalMatrices_fail_helper(2, 2);
        squareRationalMatrices_fail_helper(3, 1);
        squareRationalMatrices_fail_helper(3, -1);
    }

    private static void polynomialMatrices_helper(
            @NotNull Iterable<PolynomialMatrix> input,
            @NotNull String output,
            double meanElementCount,
            double meanCoordinateDegree,
            double meanCoordinateCoefficientBitSize
    ) {
        List<PolynomialMatrix> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        QBarTesting.aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        QBarTesting.aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
                "QBarRandomProvider_polynomialMatrices_1_0_0_0",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                0,
                0,
                "QBarRandomProvider_polynomialMatrices_5_2_0_0",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                0,
                3,
                "QBarRandomProvider_polynomialMatrices_1_0_0_3",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                0,
                3,
                "QBarRandomProvider_polynomialMatrices_5_2_0_3",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                3,
                0,
                "QBarRandomProvider_polynomialMatrices_1_0_3_0",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                3,
                0,
                "QBarRandomProvider_polynomialMatrices_5_2_3_0",
                0.0,
                0.0,
                0.0
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                1,
                1,
                "QBarRandomProvider_polynomialMatrices_1_0_1_1",
                0.9999999999980838,
                -0.32367000000033347,
                1.244895243446822
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                1,
                1,
                "QBarRandomProvider_polynomialMatrices_5_2_1_1",
                0.9999999999980838,
                1.8524199999994737,
                5.144137258885894
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                2,
                2,
                "QBarRandomProvider_polynomialMatrices_1_0_2_2",
                3.999999999992335,
                -0.33104500000017434,
                1.246769214673558
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                2,
                2,
                "QBarRandomProvider_polynomialMatrices_5_2_2_2",
                3.999999999992335,
                1.857595000003193,
                5.132331558515564
        );
        polynomialMatrices_int_int_helper(
                1,
                0,
                3,
                4,
                "QBarRandomProvider_polynomialMatrices_1_0_3_4",
                12.000000000020316,
                -0.33313666666926983,
                1.248762864952446
        );
        polynomialMatrices_int_int_helper(
                5,
                2,
                3,
                4,
                "QBarRandomProvider_polynomialMatrices_5_2_3_4",
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
                "QBarRandomProvider_polynomialMatrices_1_0_2",
                2.0046799999997904,
                -0.3324770038113362,
                1.2498486739347054
        );
        polynomialMatrices_helper(
                5,
                2,
                3,
                "QBarRandomProvider_polynomialMatrices_5_2_3",
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
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale).squareMatrices();
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
                "QBarRandomProvider_squarePolynomialMatrices_2_0_2",
                2.970210000000717,
                -0.19636995364005183,
                2.292021198601562
        );
        squarePolynomialMatrices_helper(
                5,
                1,
                3,
                "QBarRandomProvider_squarePolynomialMatrices_5_1_3",
                4.829039999999811,
                0.8766483607524744,
                5.212007622686154
        );
        squarePolynomialMatrices_helper(
                10,
                2,
                8,
                "QBarRandomProvider_squarePolynomialMatrices_10_2_8",
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
        QBarTesting.aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        QBarTesting.aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
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
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_0_0",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                0,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_0_0",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                0,
                3,
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_0_3",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                0,
                3,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_0_3",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                3,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_3_0",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                3,
                0,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_3_0",
                0.0,
                0.0,
                0.0
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                1,
                1,
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_1_1",
                0.9999999999980838,
                -0.22369000000018416,
                3.272416946837637
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                1,
                1,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_1_1",
                0.9999999999980838,
                1.863679999999461,
                5.007410045814251
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                2,
                2,
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_2_2",
                3.999999999992335,
                -0.2205149999991518,
                3.2797006998225884
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                2,
                2,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_2_2",
                3.999999999992335,
                1.8569400000032832,
                5.003229854370796
        );
        rationalPolynomialMatrices_int_int_helper(
                3,
                0,
                3,
                4,
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_3_4",
                12.000000000020316,
                -0.22018500000217706,
                3.2814545757482825
        );
        rationalPolynomialMatrices_int_int_helper(
                5,
                2,
                3,
                4,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_3_4",
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
                "QBarRandomProvider_rationalPolynomialMatrices_1_0_2",
                3.029410000001085,
                -0.2227958579398594,
                3.277140927676659
        );
        rationalPolynomialMatrices_helper(
                5,
                2,
                3,
                "QBarRandomProvider_rationalPolynomialMatrices_5_2_3",
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
                    .squareRationalMatrices();
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
                "QBarRandomProvider_squareRationalPolynomialMatrices_2_0_2",
                4.066370000000857,
                -0.2180691870136281,
                3.280631018804514
        );
        squareRationalPolynomialMatrices_helper(
                5,
                1,
                3,
                "QBarRandomProvider_squareRationalPolynomialMatrices_5_1_3",
                4.755019999999816,
                0.8735799218534637,
                5.06617307847439
        );
        squareRationalPolynomialMatrices_helper(
                10,
                2,
                8,
                "QBarRandomProvider_squareRationalPolynomialMatrices_10_2_8",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        List<Polynomial> sample = toList(take(defaultSampleSize, input));
        aeqitLimit(TINY_LIMIT, sample, output);
        aeq(topSampleCount(DEFAULT_TOP_COUNT, sample), topSampleCount);
        aeq(meanOfIntegers(toList(map(Polynomial::degree, sample))), meanDegree);
        aeq(meanOfIntegers(toList(concatMap(p -> map(BigInteger::bitLength, p), sample))), meanCoefficientBitSize);
        P.reset();
    }

    private static void polynomials_helper(
            @NotNull Iterable<Polynomial> input,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(DEFAULT_SAMPLE_SIZE, input, output, topSampleCount, meanDegree, meanCoefficientBitSize);
    }

    private static void polynomials_int_helper(
            int scale,
            int degree,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).polynomials(degree),
                output,
                topSampleCount,
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
        polynomials_int_helper(
                1,
                -1,
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...]",
                "{0=1000000}",
                -1.000000000007918,
                0.0
        );
        polynomials_int_helper(
                5,
                3,
                "[-233*x^3+1256222*x^2+21*x+21, 9*x^3+x^2-1, -x^3-6*x^2-1661016*x-117, -28*x^3-576*x^2-39244*x-67," +
                " 30*x^3-46*x^2-16989*x-38, -x^3+2*x^2-8*x, x^3-4551*x^2+62, -92*x^3+x^2+9*x, -70*x^3-x^2+x-7," +
                " 4*x^3-181301872*x^2+x-3026, -19*x^3-2*x^2-x+2, -64580*x^3+5*x-4, -13*x^3-241920*x^2+7818*x+41," +
                " 5*x^3-122*x^2-394*x-238, x^3-224*x^2+62*x-1, -24*x^3+17*x^2+633*x-4, -2708*x^3+5*x^2-13*x-15," +
                " x^3+x^2-x-1, 11*x^3+91*x^2+122224*x+111, -618*x^3+16127*x^2-7*x, ...]",
                "{x^3=403, -x^3=382, 2*x^3=194, x^3-x=184, x^3-x^2=178, -x^3+x^2=178, -x^3-x^2=172, -x^3-x=166," +
                " 3*x^3=165, x^3+x^2=164}",
                2.9999999999775233,
                5.125149750005694
        );
        polynomials_int_helper(
                10,
                8,
                "[-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-47968091191," +
                " 81*x^8+1224*x^7+14*x^6-3*x^5+x^4-2*x^3+x^2+56*x+566," +
                " -454294*x^8-622*x^7-909910*x^6+8293038*x^5-16532643*x^4-4*x^3-19*x^2-16*x+54," +
                " -58148006*x^8-49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-7," +
                " 101342*x^8-739*x^7-111061034*x^5-2*x^4-506*x^3+87*x^2-30*x+47," +
                " 15*x^8+3*x^7-394953281*x^6+131588190*x^5+1510370*x^4-17*x^3+16276*x^2-8682*x+345112274," +
                " -20040*x^8-3015*x^6+5*x^5-9*x^4+7689363762084*x^3-432*x^2+3186328*x," +
                " 13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+3064121," +
                " 1597518*x^8+1105224*x^7+3133*x^6+x^5-3*x^4-3*x^2-x-41349439," +
                " -434063*x^8+5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-13," +
                " -21118*x^8-6*x^7-39554*x^6-12*x^5-419*x^4-510*x^3-10*x^2+10025*x+2," +
                " -355615*x^8-3640006*x^7-3421*x^6+25689*x^5+524*x^4-789*x^3+3*x^2+663885*x+9," +
                " 7*x^8-330228993066*x^7-11*x^6+1524767947173*x^5-7*x^3-x^2+31*x-2304971396104," +
                " 112*x^8+397143*x^7+50*x^6-2*x^5-27*x^4-231522*x^3+x^2+6*x+17," +
                " 7*x^8+32022602*x^7+4886*x^6+60*x^4+3*x^3+629*x^2+114*x-23," +
                " -375*x^8-589*x^7+44117477702*x^6+207*x^4-198077*x^3-956783*x^2-1344*x+27," +
                " 36*x^8-632*x^7-4663*x^6-51*x^5-2743875*x^4-435*x^3+19882*x^2-13369365953*x+2," +
                " -2322630*x^8-281078474*x^7-51*x^6+12544*x^5+34*x^4+7*x^3-127*x^2-5*x-1684," +
                " -15528*x^8-130*x^7+32359*x^6+36587*x^5+22*x^4-x^3-109310*x^2-1543*x-174785977915," +
                " -2312*x^8+24*x^7+65*x^6-1060*x^5-40*x^4+22*x-117129, ...]",
                "{-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-47968091191=1," +
                " 81*x^8+1224*x^7+14*x^6-3*x^5+x^4-2*x^3+x^2+56*x+566=1," +
                " -454294*x^8-622*x^7-909910*x^6+8293038*x^5-16532643*x^4-4*x^3-19*x^2-16*x+54=1," +
                " -58148006*x^8-49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-7=1," +
                " 101342*x^8-739*x^7-111061034*x^5-2*x^4-506*x^3+87*x^2-30*x+47=1," +
                " 15*x^8+3*x^7-394953281*x^6+131588190*x^5+1510370*x^4-17*x^3+16276*x^2-8682*x+345112274=1," +
                " -20040*x^8-3015*x^6+5*x^5-9*x^4+7689363762084*x^3-432*x^2+3186328*x=1," +
                " 13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+3064121=1," +
                " 1597518*x^8+1105224*x^7+3133*x^6+x^5-3*x^4-3*x^2-x-41349439=1," +
                " -434063*x^8+5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-13=1}",
                8.000000000063345,
                10.031244555547099
        );
        polynomials_int_fail_helper(0, -1);
        polynomials_int_fail_helper(1, -2);
    }

    private static void polynomials_helper(
            int scale,
            int secondaryScale,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomials(),
                output,
                topSampleCount,
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
        polynomials_helper(
                1,
                0,
                "[0, 1, 0, x-2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, x, 0, -2*x^5-x^4+x^3-x^2+19*x-5, ...]",
                "{0=666743, 1=41642, -1=41585, 3=10615, -x=10437, -2=10427, -3=10377, 2=10365, x=10340, 2*x=2716}",
                -0.33297599999650446,
                1.2495277531222848
        );
        polynomials_helper(
                5,
                3,
                "[-x^3-233*x^2+1256222*x+85, 0, 0, 25, -39244*x^4-67*x^3-x^2-6*x-3758168," +
                " 2*x^7+x^6+30*x^5-46*x^4-16989*x^3-38*x^2-12*x-1, -8*x^2+1, 62*x-3, 0," +
                " -70*x^8-x^7+x^6-7*x^5-92*x^4+x^3+9*x^2+4, -x^8+2*x^7-2404*x^4+54644*x^3+4*x^2-181301872*x+3," +
                " 41*x^4-64580*x^3+5*x-24, x^9-224*x^8+62*x^7-x^6+5*x^5-122*x^4-394*x^3-238*x^2-13*x-504064," +
                " 17*x^2+633*x, -2708*x^4+5*x^3-13*x^2-15*x, -1, 0, 7, -618*x^6+16127*x^5-7*x^4+11*x^2+91*x+122224," +
                " -x^12+47*x^11+2*x^10-7350*x^9-2*x^8-4*x^7-152*x^6+12*x^5+x^4+223*x^2+114*x-5, ...]",
                "{0=230720, 1=12921, -1=12830, 3=5399, 2=5363, -3=5361, -2=5351, 7=2234, -7=2233, 5=2216}",
                2.850227999989568,
                5.076649486766766
        );
        polynomials_helper(
                10,
                8,
                "[-454294*x^34-622*x^33-909910*x^32+8293038*x^31-16532643*x^30-4*x^29-19*x^28-16*x^27+54*x^26-" +
                "2404*x^24+5896747328932606365*x^23-239*x^22-x^21+2*x^20-3*x^19-232*x^18+2*x^17+81*x^16+1224*x^15+" +
                "14*x^14-3*x^13+x^12-2*x^11+x^10+56*x^9+566*x^8-437219*x^7-66364*x^6-124*x^5-4626764*x^4-" +
                "645698713*x^3-117*x^2+220*x-2129," +
                " 3*x^8-394953281*x^7+131588190*x^6+1510370*x^5-17*x^4+16276*x^3-8682*x^2+345112274*x+363486," +
                " 7689363762084*x^3-432*x^2+3186328*x+1, -3015*x^2+x-1, 0," +
                " -3*x^13-3*x^11-x^10-41349439*x^9+13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-" +
                "2*x+3064121, 5181," +
                " -39554*x^17-12*x^16-419*x^15-510*x^14-10*x^13+10025*x^12+2*x^11-434063*x^10+5*x^9+x^8-736*x^7+" +
                "7182*x^6-7*x^5+3*x^4-248*x^3-13*x^2+1597518*x, 663885*x^2+9*x-97902, 524*x-1813," +
                " x^14+6*x^13+17*x^12+7*x^11-330228993066*x^10-11*x^9+1524767947173*x^8-7*x^6-x^5+31*x^4-" +
                "2304971396104*x^3-355615*x^2-3640006*x-15709," +
                " -198077*x^17-956783*x^16-1344*x^15+27*x^14+7*x^13+32022602*x^12+4886*x^11+60*x^9+3*x^8+629*x^7+" +
                "114*x^6-23*x^5+112*x^4+397143*x^3+50*x^2-2*x-43," +
                " -435*x^7+19882*x^6-13369365953*x^5+2*x^4-375*x^3-589*x^2+44117477702*x+1," +
                " -130*x^20+32359*x^19+36587*x^18+22*x^17-x^16-109310*x^15-1543*x^14-174785977915*x^13-2322630*x^12-" +
                "281078474*x^11-51*x^10+12544*x^9+34*x^8+7*x^7-127*x^6-5*x^5-1684*x^4+36*x^3-632*x^2-4663*x-138," +
                " -82699*x^12-202206016023*x^11+x^10+262*x^9-2312*x^8+24*x^7+65*x^6-1060*x^5-40*x^4+22*x-248201," +
                " -51*x^2-3*x+3533, -6*x^2-12*x-38, -200026134210*x^6-86*x^5+203*x^4+5994968441128*x^2-329*x+22," +
                " 3910*x^32-975*x^31-108*x^29-4040*x^28-7*x^27+x^26-6*x^25+84*x^24-61*x^23-x^22+2*x^21-23274*x^20+" +
                "504615376*x^19+33*x^18-3*x^17+x^16+995620*x^15-3117942350*x^14-3*x^13+23*x^12-7*x^11-" +
                "158207289*x^10+1377*x^9+772*x^8-6288*x^7+281*x^6+2*x^5+209*x^3-23971*x^2+451*x+21, 0, ...]",
                "{0=108679, -1=4199, 1=4029, -2=1846, 3=1834, 2=1816, -3=1807, 7=877, -4=867, -5=855}",
                7.919980000002783,
                10.01935105242132
        );
        polynomials_fail_helper(0, 0);
        polynomials_fail_helper(1, -1);
    }

    private static void polynomialsAtLeast_helper(
            int scale,
            int secondaryScale,
            int minDegree,
            @NotNull String output,
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).polynomialsAtLeast(minDegree),
                output,
                topSampleCount,
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
                "[0, 1, 0, x-2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, x, 0, -2*x^5-x^4+x^3-x^2+19*x-5, ...]",
                "{0=666743, 1=41642, -1=41585, 3=10615, -x=10437, -2=10427, -3=10377, 2=10365, x=10340, 2*x=2716}",
                -0.33297599999650446,
                1.2495277531222848
        );
        polynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[-x^3-233*x^2+1256222*x+85, 0, 0, 25, -39244*x^4-67*x^3-x^2-6*x-3758168," +
                " 2*x^7+x^6+30*x^5-46*x^4-16989*x^3-38*x^2-12*x-1, -8*x^2+1, 62*x-3, 0," +
                " -70*x^8-x^7+x^6-7*x^5-92*x^4+x^3+9*x^2+4, -x^8+2*x^7-2404*x^4+54644*x^3+4*x^2-181301872*x+3," +
                " 41*x^4-64580*x^3+5*x-24, x^9-224*x^8+62*x^7-x^6+5*x^5-122*x^4-394*x^3-238*x^2-13*x-504064," +
                " 17*x^2+633*x, -2708*x^4+5*x^3-13*x^2-15*x, -1, 0, 7, -618*x^6+16127*x^5-7*x^4+11*x^2+91*x+122224," +
                " -x^12+47*x^11+2*x^10-7350*x^9-2*x^8-4*x^7-152*x^6+12*x^5+x^4+223*x^2+114*x-5, ...]",
                "{0=230720, 1=12921, -1=12830, 3=5399, 2=5363, -3=5361, -2=5351, 7=2234, -7=2233, 5=2216}",
                2.850227999989568,
                5.076649486766766
        );
        polynomialsAtLeast_helper(
                5,
                3,
                0,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, -6*x^2-1661016*x-13, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " -3026*x-239, 4*x-986608240," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56, 238*x," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38," +
                " 2*x^4-7350*x^3-2*x^2-4*x-280, 31, -46, -10*x^10-7*x^8-2275*x^7-98*x^5-16624788*x^4-4*x^3+7*x-6," +
                " -97*x^3-26*x^2+102910*x+4510, 25, -1, -5*x^2+1, -11815*x^3-38*x^2-5*x+7, x+7, ...]",
                "{1=20659, -1=20647, -2=8811, -3=8736, 3=8696, 2=8572, 6=3645, 5=3627, 4=3612, -6=3584}",
                2.9960619999915905,
                5.127906423927053
        );
        polynomialsAtLeast_helper(
                5,
                3,
                2,
                "[-117*x^2+9*x+1, -16989*x^8-38*x^7-28*x^6-576*x^5-39244*x^4-67*x^3-x^2-6*x-55384, x^2+30*x-46," +
                " -x^2+2*x-4, -4551*x^2+30, -92*x^3+x^2+9*x-1, -70*x^3-x^2+x-3, 4*x^3-181301872*x^2+x-2002," +
                " -2*x^6-x^5+2*x^4-2404*x+2517, -64580*x^4+5*x^2-4*x-3, -13*x^3-241920*x^2+7818*x+9," +
                " -224*x^6+62*x^5-x^4+5*x^3-122*x^2-394*x-14, 633*x^2-4*x+1, -2708*x^5+5*x^4-13*x^3-15*x^2-24*x+3," +
                " 3*x^3-x-1, 91*x^2+122224*x+47, -618*x^4+16127*x^3-7*x^2+7, 114*x^2-x-15659, 12*x^3+x^2+223," +
                " -2*x^2-4*x-152, ...]",
                "{x^2=1178, -x^2=1163, -x^2-x=550, x^2+x=516, -2*x^2=502, x^2+1=499, -x^2+1=488, 2*x^2=485," +
                " x^2-x=478, -x^2-1=478}",
                3.0029149999686346,
                5.125061111715066
        );
        polynomialsAtLeast_helper(
                10,
                8,
                -1,
                "[-454294*x^34-622*x^33-909910*x^32+8293038*x^31-16532643*x^30-4*x^29-19*x^28-16*x^27+54*x^26-" +
                "2404*x^24+5896747328932606365*x^23-239*x^22-x^21+2*x^20-3*x^19-232*x^18+2*x^17+81*x^16+1224*x^15+" +
                "14*x^14-3*x^13+x^12-2*x^11+x^10+56*x^9+566*x^8-437219*x^7-66364*x^6-124*x^5-4626764*x^4-" +
                "645698713*x^3-117*x^2+220*x-2129," +
                " 3*x^8-394953281*x^7+131588190*x^6+1510370*x^5-17*x^4+16276*x^3-8682*x^2+345112274*x+363486," +
                " 7689363762084*x^3-432*x^2+3186328*x+1, -3015*x^2+x-1, 0," +
                " -3*x^13-3*x^11-x^10-41349439*x^9+13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+" +
                "3064121, 5181," +
                " -39554*x^17-12*x^16-419*x^15-510*x^14-10*x^13+10025*x^12+2*x^11-434063*x^10+5*x^9+x^8-736*x^7+" +
                "7182*x^6-7*x^5+3*x^4-248*x^3-13*x^2+1597518*x, 663885*x^2+9*x-97902, 524*x-1813," +
                " x^14+6*x^13+17*x^12+7*x^11-330228993066*x^10-11*x^9+1524767947173*x^8-7*x^6-x^5+31*x^4-" +
                "2304971396104*x^3-355615*x^2-3640006*x-15709," +
                " -198077*x^17-956783*x^16-1344*x^15+27*x^14+7*x^13+32022602*x^12+4886*x^11+60*x^9+3*x^8+629*x^7+" +
                "114*x^6-23*x^5+112*x^4+397143*x^3+50*x^2-2*x-43," +
                " -435*x^7+19882*x^6-13369365953*x^5+2*x^4-375*x^3-589*x^2+44117477702*x+1," +
                " -130*x^20+32359*x^19+36587*x^18+22*x^17-x^16-109310*x^15-1543*x^14-174785977915*x^13-2322630*x^12-" +
                "281078474*x^11-51*x^10+12544*x^9+34*x^8+7*x^7-127*x^6-5*x^5-1684*x^4+36*x^3-632*x^2-4663*x-138," +
                " -82699*x^12-202206016023*x^11+x^10+262*x^9-2312*x^8+24*x^7+65*x^6-1060*x^5-40*x^4+22*x-248201," +
                " -51*x^2-3*x+3533, -6*x^2-12*x-38, -200026134210*x^6-86*x^5+203*x^4+5994968441128*x^2-329*x+22," +
                " 3910*x^32-975*x^31-108*x^29-4040*x^28-7*x^27+x^26-6*x^25+84*x^24-61*x^23-x^22+2*x^21-23274*x^20+" +
                "504615376*x^19+33*x^18-3*x^17+x^16+995620*x^15-3117942350*x^14-3*x^13+23*x^12-7*x^11-" +
                "158207289*x^10+1377*x^9+772*x^8-6288*x^7+281*x^6+2*x^5+209*x^3-23971*x^2+451*x+21, 0, ...]",
                "{0=108679, -1=4199, 1=4029, -2=1846, 3=1834, 2=1816, -3=1807, 7=877, -4=867, -5=855}",
                7.919980000002783,
                10.01935105242132
        );
        polynomialsAtLeast_helper(
                10,
                8,
                2,
                "[-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-593*x+590028540, -19*x^2-16*x+22," +
                " 105065295*x^5-454294*x^4-622*x^3-909910*x^2+8293038*x-41698467," +
                " 4*x^5+x^4-2300*x^3+18*x^2-1283*x-1862166," +
                " 1510370*x^14-17*x^13+16276*x^12-8682*x^11+345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-" +
                "506*x^4+87*x^3-30*x^2+47*x-125256870," +
                " 7689363762084*x^7-432*x^6+3186328*x^5+15*x^3+3*x^2-394953281*x+7067616," +
                " -2*x^6+3064121*x^5-20040*x^4-3015*x^2+x-1, 13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+45," +
                " x^5-3*x^4-3*x^2-x-5928689, 7182*x^7-7*x^6+3*x^5-248*x^4-13*x^3+1597518*x^2+1105224*x+20," +
                " -434063*x^3+5*x^2+x-480, -6*x^6-39554*x^5-12*x^4-419*x^3-510*x^2-10*x+43421," +
                " -x^12+31*x^11-2304971396104*x^10-355615*x^9-3640006*x^8-3421*x^7+25689*x^6+524*x^5-789*x^4+3*x^3+" +
                "663885*x^2+9*x-50, 17*x^5+7*x^4-330228993066*x^3-11*x^2+1524767947173*x+1," +
                " -23*x^7+112*x^6+397143*x^5+50*x^4-2*x^3-27*x^2-231522*x+7," +
                " 19882*x^18-13369365953*x^17+2*x^16-375*x^15-589*x^14+44117477702*x^13+207*x^11-198077*x^10-" +
                "956783*x^9-1344*x^8+27*x^7+7*x^6+32022602*x^5+4886*x^4+60*x^2+3*x+3701, -51*x^2-2743875*x-179," +
                " -127*x^5-5*x^4-1684*x^3+36*x^2-632*x-567," +
                " -1543*x^6-174785977915*x^5-2322630*x^4-281078474*x^3-51*x^2+12544*x+34," +
                " -130*x^5+32359*x^4+36587*x^3+22*x^2-x-23210, ...]",
                "{-x^2=62, x^2=55, -x^2+x=35, -x^2-1=32, -2*x^2=31, 2*x^2=31, -x^2+1=30, -x^2-x=30, x^2+x=25," +
                " x^2-x=25}",
                8.004555000013825,
                10.032780076379966
        );
        polynomialsAtLeast_helper(
                10,
                8,
                7,
                "[56*x^10+566*x^9-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-" +
                "2870934583, -232*x^8+2*x^7+81*x^6+1224*x^5+14*x^4-3*x^3+x^2-4*x," +
                " 54*x^7-2404*x^5+5896747328932606365*x^4-239*x^3-x^2+2*x-3," +
                " -454294*x^7-622*x^6-909910*x^5+8293038*x^4-16532643*x^3-4*x^2-19*x-8," +
                " -49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-3," +
                " 345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-506*x^4+87*x^3-30*x^2+47*x-2586436," +
                " 3186328*x^9+15*x^7+3*x^6-394953281*x^5+131588190*x^4+1510370*x^3-17*x^2+16276*x-1514," +
                " -2*x^8+3064121*x^7-20040*x^6-3015*x^4+5*x^3-9*x^2+7689363762084*x-176," +
                " -41349439*x^7+13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+1298," +
                " 1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x, 5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-5," +
                " 663885*x^11+9*x^10-21118*x^9-6*x^8-39554*x^7-12*x^6-419*x^5-510*x^4-10*x^3+10025*x^2+2*x-171919," +
                " -2304971396104*x^7-355615*x^6-3640006*x^5-3421*x^4+25689*x^3+524*x^2-789*x+1," +
                " 17*x^8+7*x^7-330228993066*x^6-11*x^5+1524767947173*x^4-7*x^2-x+7," +
                " 629*x^10+114*x^9-23*x^8+112*x^7+397143*x^6+50*x^5-2*x^4-27*x^3-231522*x^2+3*x," +
                " 207*x^9-198077*x^8-956783*x^7-1344*x^6+27*x^5+7*x^4+32022602*x^3+4886*x^2+155," +
                " -435*x^7+19882*x^6-13369365953*x^5+2*x^4-375*x^3-589*x^2+44117477702*x," +
                " -51*x^11+12544*x^10+34*x^9+7*x^8-127*x^7-5*x^6-1684*x^5+36*x^4-632*x^3-4663*x^2-51*x-253507," +
                " 36587*x^7+22*x^6-x^5-109310*x^4-1543*x^3-174785977915*x^2-2322630*x-146860746," +
                " -40*x^7+22*x^4-117129*x^3-15528*x^2-130*x+15975, ...]",
                "{56*x^10+566*x^9-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-" +
                "2870934583=1, -232*x^8+2*x^7+81*x^6+1224*x^5+14*x^4-3*x^3+x^2-4*x=1," +
                " 54*x^7-2404*x^5+5896747328932606365*x^4-239*x^3-x^2+2*x-3=1," +
                " -454294*x^7-622*x^6-909910*x^5+8293038*x^4-16532643*x^3-4*x^2-19*x-8=1," +
                " -49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-3=1," +
                " 345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-506*x^4+87*x^3-30*x^2+47*x-2586436=1," +
                " 3186328*x^9+15*x^7+3*x^6-394953281*x^5+131588190*x^4+1510370*x^3-17*x^2+16276*x-1514=1," +
                " -2*x^8+3064121*x^7-20040*x^6-3015*x^4+5*x^3-9*x^2+7689363762084*x-176=1," +
                " -41349439*x^7+13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+1298=1," +
                " 1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).primitivePolynomials(degree),
                output,
                topSampleCount,
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
                "[-1, -1, -1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, 1, ...]",
                "{1=500035, -1=499965}",
                0.0,
                0.5000349999935414
        );
        primitivePolynomials_int_helper(
                1,
                1,
                "[3*x-2, x, -x-1, -x-1, -2*x+5, x+1, 19*x-1, x-1, -2*x-1, -x-1, x, x, -2*x-1, -x, 115*x+1, x-9," +
                " 4*x-11, x+4, x-38, -3*x-1, ...]",
                "{-x=179547, x=179320, -x-1=45223, -x+1=44731, x+1=44696, x-1=44377, -3*x-1=11370, x+3=11281," +
                " -2*x-1=11258, -3*x+1=11239}",
                1.000000000007918,
                1.0773089999900907
        );
        primitivePolynomials_int_helper(
                5,
                3,
                "[-233*x^3+1256222*x^2+21*x+21, 9*x^3+x^2-1, -x^3-6*x^2-1661016*x-117, -28*x^3-576*x^2-39244*x-67," +
                " 30*x^3-46*x^2-16989*x-38, -x^3+2*x^2-8*x, x^3-4551*x^2+62, -92*x^3+x^2+9*x, -70*x^3-x^2+x-7," +
                " 4*x^3-181301872*x^2+x-3026, -19*x^3-2*x^2-x+2, -64580*x^3+5*x-4, -13*x^3-241920*x^2+7818*x+41," +
                " 5*x^3-122*x^2-394*x-238, x^3-224*x^2+62*x-1, -24*x^3+17*x^2+633*x-4, -2708*x^3+5*x^2-13*x-15," +
                " x^3+x^2-x-1, 11*x^3+91*x^2+122224*x+111, -618*x^3+16127*x^2-7*x, ...]",
                "{x^3=439, -x^3=411, x^3-x=199, x^3-x^2=191, x^3+x^2=190, -x^3+x^2=185, -x^3-x^2=180, -x^3+1=180," +
                " x^3+1=176, -x^3-x=173}",
                2.9999999999775233,
                5.133858750005687
        );
        primitivePolynomials_int_helper(
                10,
                8,
                "[-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-47968091191," +
                " 81*x^8+1224*x^7+14*x^6-3*x^5+x^4-2*x^3+x^2+56*x+566," +
                " -454294*x^8-622*x^7-909910*x^6+8293038*x^5-16532643*x^4-4*x^3-19*x^2-16*x+54," +
                " -58148006*x^8-49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-7," +
                " 101342*x^8-739*x^7-111061034*x^5-2*x^4-506*x^3+87*x^2-30*x+47," +
                " 15*x^8+3*x^7-394953281*x^6+131588190*x^5+1510370*x^4-17*x^3+16276*x^2-8682*x+345112274," +
                " -20040*x^8-3015*x^6+5*x^5-9*x^4+7689363762084*x^3-432*x^2+3186328*x," +
                " 13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+3064121," +
                " 1597518*x^8+1105224*x^7+3133*x^6+x^5-3*x^4-3*x^2-x-41349439," +
                " -434063*x^8+5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-13," +
                " -21118*x^8-6*x^7-39554*x^6-12*x^5-419*x^4-510*x^3-10*x^2+10025*x+2," +
                " -355615*x^8-3640006*x^7-3421*x^6+25689*x^5+524*x^4-789*x^3+3*x^2+663885*x+9," +
                " 7*x^8-330228993066*x^7-11*x^6+1524767947173*x^5-7*x^3-x^2+31*x-2304971396104," +
                " 112*x^8+397143*x^7+50*x^6-2*x^5-27*x^4-231522*x^3+x^2+6*x+17," +
                " 7*x^8+32022602*x^7+4886*x^6+60*x^4+3*x^3+629*x^2+114*x-23," +
                " -375*x^8-589*x^7+44117477702*x^6+207*x^4-198077*x^3-956783*x^2-1344*x+27," +
                " 36*x^8-632*x^7-4663*x^6-51*x^5-2743875*x^4-435*x^3+19882*x^2-13369365953*x+2," +
                " -2322630*x^8-281078474*x^7-51*x^6+12544*x^5+34*x^4+7*x^3-127*x^2-5*x-1684," +
                " -15528*x^8-130*x^7+32359*x^6+36587*x^5+22*x^4-x^3-109310*x^2-1543*x-174785977915," +
                " -2312*x^8+24*x^7+65*x^6-1060*x^5-40*x^4+22*x-117129, ...]",
                "{-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-47968091191=1," +
                " 81*x^8+1224*x^7+14*x^6-3*x^5+x^4-2*x^3+x^2+56*x+566=1," +
                " -454294*x^8-622*x^7-909910*x^6+8293038*x^5-16532643*x^4-4*x^3-19*x^2-16*x+54=1," +
                " -58148006*x^8-49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-7=1," +
                " 101342*x^8-739*x^7-111061034*x^5-2*x^4-506*x^3+87*x^2-30*x+47=1," +
                " 15*x^8+3*x^7-394953281*x^6+131588190*x^5+1510370*x^4-17*x^3+16276*x^2-8682*x+345112274=1," +
                " -20040*x^8-3015*x^6+5*x^5-9*x^4+7689363762084*x^3-432*x^2+3186328*x=1," +
                " 13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+3064121=1," +
                " 1597518*x^8+1105224*x^7+3133*x^6+x^5-3*x^4-3*x^2-x-41349439=1," +
                " -434063*x^8+5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-13=1}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).primitivePolynomials(),
                output,
                topSampleCount,
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
                "[x^3+85*x+3, -2*x^2+3, x-2, 1, x^2+x-10, 7*x+1, -5*x-1, 1, -2*x^4-x^2-x, -1, -6*x-1, 1, -1," +
                " 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1, -x^4-3*x-6, ...]",
                "{-1=195521, 1=193755, -x=49328, x=48744, x-1=12299, -x-1=12280, -x^2=12183, x+1=12182, -x+1=12160," +
                " x^2=12069}",
                1.3170959999927159,
                0.9483072777233684
        );
        primitivePolynomials_helper(
                5,
                3,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, -6*x^2-1661016*x-13, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " -3026*x-239," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38," +
                " -10*x^10-7*x^8-2275*x^7-98*x^5-16624788*x^4-4*x^3+7*x-6, -97*x^3-26*x^2+102910*x+4510, -1," +
                " -5*x^2+1, -11815*x^3-38*x^2-5*x+7, x+7, 3*x-242, 7*x^2+15*x-23, -730*x+29," +
                " 2*x^7-x^5+13*x^3+9*x^2-2*x+115001," +
                " -26*x^11+5*x^10-11*x^9+31*x^7+5*x^6+33*x^5+37*x^4-x^3-3*x^2+46834*x+1109, -x^2+257, ...]",
                "{-1=30204, 1=30148, x=3854, -x=3816, -x+1=1589, x-1=1584, -x-1=1570, x+1=1566, -2*x+1=696," +
                " -3*x-1=688}",
                4.135319999990011,
                5.005227717069355
        );
        primitivePolynomials_helper(
                10,
                8,
                "[8293038*x^31-16532643*x^30-4*x^29-19*x^28-16*x^27+54*x^26-2404*x^24+5896747328932606365*x^23-" +
                "239*x^22-x^21+2*x^20-3*x^19-232*x^18+2*x^17+81*x^16+1224*x^15+14*x^14-3*x^13+x^12-2*x^11+x^10+" +
                "56*x^9+566*x^8-437219*x^7-66364*x^6-124*x^5-4626764*x^4-645698713*x^3-117*x^2+220*x-2129," +
                " -2300*x^16+18*x^15-1283*x^14-289302*x^13-7*x^12+11*x^10+51089054789*x^9+3*x^8+368648822968*x^7+" +
                "5*x^6+249*x^5-4*x^4+105065295*x^3-454294*x^2-366*x+1, -49*x+20," +
                " 13297953072*x^16-8080881*x^15+152929*x^14+3928*x^13-x^11+3346*x^10-2*x^9+3064121*x^8-20040*x^7-" +
                "3015*x^5+5*x^4-9*x^3+7689363762084*x^2-432*x," +
                " 10025*x^18+2*x^17-434063*x^16+5*x^15+x^14-736*x^13+7182*x^12-7*x^11+3*x^10-248*x^9-13*x^8+" +
                "1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x-4, -12*x^3-419*x^2-111*x-1," +
                " 31*x^12-2304971396104*x^11-355615*x^10-3640006*x^9-3421*x^8+25689*x^7+524*x^6-789*x^5+3*x^4+" +
                "663885*x^3+9*x^2-21118*x-14," +
                " -2743875*x^34-435*x^33+19882*x^32-13369365953*x^31+2*x^30-375*x^29-589*x^28+44117477702*x^27+" +
                "207*x^25-198077*x^24-956783*x^23-1344*x^22+27*x^21+7*x^20+32022602*x^19+4886*x^18+60*x^16+3*x^15+" +
                "629*x^14+114*x^13-23*x^12+112*x^11+397143*x^10+50*x^9-2*x^8-27*x^7-231522*x^6+x^5+6*x^4+17*x^3+" +
                "7*x^2-330228993066*x-123, 7*x^6-127*x^5-5*x^4-1684*x^3+36*x^2-632*x-29239," +
                " -109310*x^6-1543*x^5-174785977915*x^4-2322630*x^3-281078474*x^2-51*x+32842, 36587*x+54," +
                " x^12+262*x^11-2312*x^10+24*x^9+65*x^8-1060*x^7-40*x^6+22*x^3-117129*x^2-15528*x-130," +
                " -105*x^27-6*x^26-12*x^25-14*x^24+5*x^23-146275*x^21-x^20-155*x^19-95*x^18-x^17-6158262286914*x^16+" +
                "486*x^15-11821607*x^14+18553*x^13-142246*x^12-x^11+2119739845*x^10-11617*x^9+29306127664*x^8-4*x^7+" +
                "372*x^6+398077*x^5-51*x^4-3*x^3+973*x^2-5*x-279307, -6288*x^2+281*x," +
                " 33*x^9-3*x^8+x^7+995620*x^6-3117942350*x^5-3*x^4+23*x^3-7*x^2-158207289*x+5473," +
                " -30*x^24-7*x^23-4*x^22+58*x^21-3*x^20-39*x^19+52*x^18+6*x^17+527*x^16-2733*x^15-4050415666*x^14+" +
                "3910*x^12-975*x^11-108*x^9-4040*x^8-7*x^7+x^6-6*x^5+84*x^4-61*x^3-x^2+2*x-23274," +
                " 6*x^10+30*x^9+2804*x^8-3*x^7-6152*x^6-1423414505*x^5+37841*x^4-148*x^3-24*x^2+717*x+4," +
                " -240*x^23-3*x^21+x^20+5767973249*x^19+21493072257233*x^18+x^17-106*x^16+2245*x^15+316844212*x^14-" +
                "28*x^13+20*x^12+2*x^11-13*x^10-93538016*x^9-467*x^8+14502*x^6+860*x^5-2*x^4+18532*x^3+3*x^2+14*x-" +
                "276, 3244*x^8-30660*x^7+5702*x^6+3*x^5+x^4+35006*x^3+104*x^2-17*x-38573810," +
                " 438297*x^12-3578*x^11-9859388*x^10-66*x^9-14*x^8-317*x^7-109*x^6+11*x^5+402824061598125*x^3-" +
                "208090*x^2+12*x+36, ...]",
                "{1=6077, -1=6020, x=486, -x=472, -x-1=241, x-1=228, -x+1=218, x+1=217, 2*x-1=111, x-2=110}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).primitivePolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
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
                "[x^3+85*x+3, -2*x^2+3, x-2, 1, x^2+x-10, 7*x+1, -5*x-1, 1, -2*x^4-x^2-x, -1, -6*x-1, 1, -1," +
                " 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1, -x^4-3*x-6, ...]",
                "{-1=195521, 1=193755, -x=49328, x=48744, x-1=12299, -x-1=12280, -x^2=12183, x+1=12182, -x+1=12160," +
                " x^2=12069}",
                1.3170959999927159,
                0.9483072777233684
        );
        primitivePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "[x^3+85*x+3, -2*x^2+3, x-2, 1, x^2+x-10, 7*x+1, -5*x-1, 1, -2*x^4-x^2-x, -1, -6*x-1, 1, -1," +
                " 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1, -x^4-3*x-6, ...]",
                "{-1=195521, 1=193755, -x=49328, x=48744, x-1=12299, -x-1=12280, -x^2=12183, x+1=12182, -x+1=12160," +
                " x^2=12069}",
                1.3170959999927159,
                0.9483072777233684
        );
        primitivePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, -6*x^2-1661016*x-13, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " -3026*x-239," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38," +
                " -10*x^10-7*x^8-2275*x^7-98*x^5-16624788*x^4-4*x^3+7*x-6, -97*x^3-26*x^2+102910*x+4510, -1," +
                " -5*x^2+1, -11815*x^3-38*x^2-5*x+7, x+7, 3*x-242, 7*x^2+15*x-23, -730*x+29," +
                " 2*x^7-x^5+13*x^3+9*x^2-2*x+115001," +
                " -26*x^11+5*x^10-11*x^9+31*x^7+5*x^6+33*x^5+37*x^4-x^3-3*x^2+46834*x+1109, -x^2+257, ...]",
                "{-1=30204, 1=30148, x=3854, -x=3816, -x+1=1589, x-1=1584, -x-1=1570, x+1=1566, -2*x+1=696," +
                " -3*x-1=688}",
                4.135319999990011,
                5.005227717069355
        );
        primitivePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, -6*x^2-1661016*x-13, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " -3026*x-239," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38," +
                " -10*x^10-7*x^8-2275*x^7-98*x^5-16624788*x^4-4*x^3+7*x-6, -97*x^3-26*x^2+102910*x+4510, -1," +
                " -5*x^2+1, -11815*x^3-38*x^2-5*x+7, x+7, 3*x-242, 7*x^2+15*x-23, -730*x+29," +
                " 2*x^7-x^5+13*x^3+9*x^2-2*x+115001," +
                " -26*x^11+5*x^10-11*x^9+31*x^7+5*x^6+33*x^5+37*x^4-x^3-3*x^2+46834*x+1109, -x^2+257, ...]",
                "{-1=30204, 1=30148, x=3854, -x=3816, -x+1=1589, x-1=1584, -x-1=1570, x+1=1566, -2*x+1=696," +
                " -3*x-1=688}",
                4.135319999990011,
                5.005227717069355
        );
        primitivePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[-117*x^2+9*x+1, -16989*x^8-38*x^7-28*x^6-576*x^5-39244*x^4-67*x^3-x^2-6*x-55384, x^2+30*x-46," +
                " -x^2+2*x-4, -92*x^3+x^2+9*x-1, -70*x^3-x^2+x-3, 4*x^3-181301872*x^2+x-2002," +
                " -2*x^6-x^5+2*x^4-2404*x+2517, -64580*x^4+5*x^2-4*x-3, -13*x^3-241920*x^2+7818*x+9," +
                " -224*x^6+62*x^5-x^4+5*x^3-122*x^2-394*x-14, 633*x^2-4*x+1, -2708*x^5+5*x^4-13*x^3-15*x^2-24*x+3," +
                " 3*x^3-x-1, 91*x^2+122224*x+47, -618*x^4+16127*x^3-7*x^2+7, 114*x^2-x-15659, 12*x^3+x^2+223," +
                " 47*x^2+2*x-3254, -26*x^4+102910*x^3+2462*x^2+x-6, ...]",
                "{x^2=1311, -x^2=1305, -x^2-x=612, x^2+1=574, x^2+x=563, -x^2+1=550, x^2-x=533, -x^2-1=530," +
                " x^2-1=519, -x^2+x=504}",
                3.0893119999680687,
                5.1270032708742885
        );
        primitivePolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "[8293038*x^31-16532643*x^30-4*x^29-19*x^28-16*x^27+54*x^26-2404*x^24+5896747328932606365*x^23-239*" +
                "x^22-x^21+2*x^20-3*x^19-232*x^18+2*x^17+81*x^16+1224*x^15+14*x^14-3*x^13+x^12-2*x^11+x^10+56*x^9+" +
                "566*x^8-437219*x^7-66364*x^6-124*x^5-4626764*x^4-645698713*x^3-117*x^2+220*x-2129," +
                " -2300*x^16+18*x^15-1283*x^14-289302*x^13-7*x^12+11*x^10+51089054789*x^9+3*x^8+368648822968*x^7+" +
                "5*x^6+249*x^5-4*x^4+105065295*x^3-454294*x^2-366*x+1, -49*x+20," +
                " 13297953072*x^16-8080881*x^15+152929*x^14+3928*x^13-x^11+3346*x^10-2*x^9+3064121*x^8-20040*x^7-" +
                "3015*x^5+5*x^4-9*x^3+7689363762084*x^2-432*x," +
                " 10025*x^18+2*x^17-434063*x^16+5*x^15+x^14-736*x^13+7182*x^12-7*x^11+3*x^10-248*x^9-13*x^8+" +
                "1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x-4, -12*x^3-419*x^2-111*x-1," +
                " 31*x^12-2304971396104*x^11-355615*x^10-3640006*x^9-3421*x^8+25689*x^7+524*x^6-789*x^5+3*x^4+" +
                "663885*x^3+9*x^2-21118*x-14," +
                " -2743875*x^34-435*x^33+19882*x^32-13369365953*x^31+2*x^30-375*x^29-589*x^28+44117477702*x^27+" +
                "207*x^25-198077*x^24-956783*x^23-1344*x^22+27*x^21+7*x^20+32022602*x^19+4886*x^18+60*x^16+3*x^15+" +
                "629*x^14+114*x^13-23*x^12+112*x^11+397143*x^10+50*x^9-2*x^8-27*x^7-231522*x^6+x^5+6*x^4+17*x^3+" +
                "7*x^2-330228993066*x-123, 7*x^6-127*x^5-5*x^4-1684*x^3+36*x^2-632*x-29239," +
                " -109310*x^6-1543*x^5-174785977915*x^4-2322630*x^3-281078474*x^2-51*x+32842, 36587*x+54," +
                " x^12+262*x^11-2312*x^10+24*x^9+65*x^8-1060*x^7-40*x^6+22*x^3-117129*x^2-15528*x-130," +
                " -105*x^27-6*x^26-12*x^25-14*x^24+5*x^23-146275*x^21-x^20-155*x^19-95*x^18-x^17-6158262286914*x^16+" +
                "486*x^15-11821607*x^14+18553*x^13-142246*x^12-x^11+2119739845*x^10-11617*x^9+29306127664*x^8-4*x^7+" +
                "372*x^6+398077*x^5-51*x^4-3*x^3+973*x^2-5*x-279307, -6288*x^2+281*x," +
                " 33*x^9-3*x^8+x^7+995620*x^6-3117942350*x^5-3*x^4+23*x^3-7*x^2-158207289*x+5473," +
                " -30*x^24-7*x^23-4*x^22+58*x^21-3*x^20-39*x^19+52*x^18+6*x^17+527*x^16-2733*x^15-4050415666*x^14+" +
                "3910*x^12-975*x^11-108*x^9-4040*x^8-7*x^7+x^6-6*x^5+84*x^4-61*x^3-x^2+2*x-23274," +
                " 6*x^10+30*x^9+2804*x^8-3*x^7-6152*x^6-1423414505*x^5+37841*x^4-148*x^3-24*x^2+717*x+4," +
                " -240*x^23-3*x^21+x^20+5767973249*x^19+21493072257233*x^18+x^17-106*x^16+2245*x^15+316844212*x^14-" +
                "28*x^13+20*x^12+2*x^11-13*x^10-93538016*x^9-467*x^8+14502*x^6+860*x^5-2*x^4+18532*x^3+3*x^2+14*x-" +
                "276, 3244*x^8-30660*x^7+5702*x^6+3*x^5+x^4+35006*x^3+104*x^2-17*x-38573810," +
                " 438297*x^12-3578*x^11-9859388*x^10-66*x^9-14*x^8-317*x^7-109*x^6+11*x^5+402824061598125*x^3-" +
                "208090*x^2+12*x+36, ...]",
                "{1=6077, -1=6020, x=486, -x=472, -x-1=241, x-1=228, -x+1=218, x+1=217, 2*x-1=111, x-2=110}",
                9.45433400000822,
                10.00177486274624
        );
        primitivePolynomialsAtLeast_helper(
                10,
                8,
                2,
                "[-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-593*x+590028540, -19*x^2-16*x+22," +
                " 105065295*x^5-454294*x^4-622*x^3-909910*x^2+8293038*x-41698467," +
                " 4*x^5+x^4-2300*x^3+18*x^2-1283*x-1862166," +
                " 1510370*x^14-17*x^13+16276*x^12-8682*x^11+345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-" +
                "506*x^4+87*x^3-30*x^2+47*x-125256870," +
                " 7689363762084*x^7-432*x^6+3186328*x^5+15*x^3+3*x^2-394953281*x+7067616," +
                " -2*x^6+3064121*x^5-20040*x^4-3015*x^2+x-1, 13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+45," +
                " x^5-3*x^4-3*x^2-x-5928689, 7182*x^7-7*x^6+3*x^5-248*x^4-13*x^3+1597518*x^2+1105224*x+20," +
                " -434063*x^3+5*x^2+x-480, -6*x^6-39554*x^5-12*x^4-419*x^3-510*x^2-10*x+43421," +
                " -x^12+31*x^11-2304971396104*x^10-355615*x^9-3640006*x^8-3421*x^7+25689*x^6+524*x^5-789*x^4+3*x^3+" +
                "663885*x^2+9*x-50, 17*x^5+7*x^4-330228993066*x^3-11*x^2+1524767947173*x+1," +
                " -23*x^7+112*x^6+397143*x^5+50*x^4-2*x^3-27*x^2-231522*x+7," +
                " 19882*x^18-13369365953*x^17+2*x^16-375*x^15-589*x^14+44117477702*x^13+207*x^11-198077*x^10-" +
                "956783*x^9-1344*x^8+27*x^7+7*x^6+32022602*x^5+4886*x^4+60*x^2+3*x+3701, -51*x^2-2743875*x-179," +
                " -127*x^5-5*x^4-1684*x^3+36*x^2-632*x-567," +
                " -1543*x^6-174785977915*x^5-2322630*x^4-281078474*x^3-51*x^2+12544*x+34," +
                " -130*x^5+32359*x^4+36587*x^3+22*x^2-x-23210, ...]",
                "{-x^2=64, x^2=59, -x^2+x=37, -x^2-1=33, -x^2+1=32, -x^2-x=30, 2*x^2+x=26, x^2+x=25, x^2-x=25," +
                " x^2-1=23}",
                8.22605800001186,
                10.031010968999196
        );
        primitivePolynomialsAtLeast_helper(
                10,
                8,
                7,
                "[56*x^10+566*x^9-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-" +
                "2870934583, -232*x^8+2*x^7+81*x^6+1224*x^5+14*x^4-3*x^3+x^2-4*x," +
                " 54*x^7-2404*x^5+5896747328932606365*x^4-239*x^3-x^2+2*x-3," +
                " -454294*x^7-622*x^6-909910*x^5+8293038*x^4-16532643*x^3-4*x^2-19*x-8," +
                " -49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-3," +
                " 345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-506*x^4+87*x^3-30*x^2+47*x-2586436," +
                " 3186328*x^9+15*x^7+3*x^6-394953281*x^5+131588190*x^4+1510370*x^3-17*x^2+16276*x-1514," +
                " -2*x^8+3064121*x^7-20040*x^6-3015*x^4+5*x^3-9*x^2+7689363762084*x-176," +
                " -41349439*x^7+13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+1298," +
                " 1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x, 5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-5," +
                " 663885*x^11+9*x^10-21118*x^9-6*x^8-39554*x^7-12*x^6-419*x^5-510*x^4-10*x^3+10025*x^2+2*x-171919," +
                " -2304971396104*x^7-355615*x^6-3640006*x^5-3421*x^4+25689*x^3+524*x^2-789*x+1," +
                " 17*x^8+7*x^7-330228993066*x^6-11*x^5+1524767947173*x^4-7*x^2-x+7," +
                " 629*x^10+114*x^9-23*x^8+112*x^7+397143*x^6+50*x^5-2*x^4-27*x^3-231522*x^2+3*x," +
                " 207*x^9-198077*x^8-956783*x^7-1344*x^6+27*x^5+7*x^4+32022602*x^3+4886*x^2+155," +
                " -435*x^7+19882*x^6-13369365953*x^5+2*x^4-375*x^3-589*x^2+44117477702*x," +
                " -51*x^11+12544*x^10+34*x^9+7*x^8-127*x^7-5*x^6-1684*x^5+36*x^4-632*x^3-4663*x^2-51*x-253507," +
                " 36587*x^7+22*x^6-x^5-109310*x^4-1543*x^3-174785977915*x^2-2322630*x-146860746," +
                " -40*x^7+22*x^4-117129*x^3-15528*x^2-130*x+15975, ...]",
                "{56*x^10+566*x^9-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-" +
                "2870934583=1, -232*x^8+2*x^7+81*x^6+1224*x^5+14*x^4-3*x^3+x^2-4*x=1," +
                " 54*x^7-2404*x^5+5896747328932606365*x^4-239*x^3-x^2+2*x-3=1," +
                " -454294*x^7-622*x^6-909910*x^5+8293038*x^4-16532643*x^3-4*x^2-19*x-8=1," +
                " -49*x^7+4*x^6+x^5-2300*x^4+18*x^3-1283*x^2-289302*x-3=1," +
                " 345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-506*x^4+87*x^3-30*x^2+47*x-2586436=1," +
                " 3186328*x^9+15*x^7+3*x^6-394953281*x^5+131588190*x^4+1510370*x^3-17*x^2+16276*x-1514=1," +
                " -2*x^8+3064121*x^7-20040*x^6-3015*x^4+5*x^3-9*x^2+7689363762084*x-176=1," +
                " -41349439*x^7+13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+1298=1," +
                " 1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).positivePrimitivePolynomials(degree),
                output,
                topSampleCount,
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
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]",
                "{1=1000000}",
                0.0,
                1.000000000007918
        );
        positivePrimitivePolynomials_int_helper(
                1,
                1,
                "[3*x-2, x, x+1, 19*x-1, x-1, x, x, 115*x+1, x-9, 4*x-11, x+4, x-38, x, 6*x+1, 13*x-77, x, x, x-2," +
                " 11*x-5, x-1, ...]",
                "{x=359251, x+1=89458, x-1=89376, x-3=22517, x+3=22448, 2*x-1=22437, x+2=22344, x-2=22303," +
                " 3*x-1=22302, 2*x+1=22263}",
                1.000000000007918,
                1.2789054999949967
        );
        positivePrimitivePolynomials_int_helper(
                5,
                3,
                "[9*x^3+x^2-1, 30*x^3-46*x^2-16989*x-38, x^3-4551*x^2+62, 4*x^3-181301872*x^2+x-3026," +
                " 5*x^3-122*x^2-394*x-238, x^3-224*x^2+62*x-1, x^3+x^2-x-1, 11*x^3+91*x^2+122224*x+111," +
                " 223*x^3+114*x^2-x-15659, 438*x^3-46*x^2-x+47, x^3-10*x^2-7, 4*x^3-x^2, x^3+27*x^2-11815*x-38," +
                " 2*x^3+3*x-498, 23*x^3+9*x^2-17*x+766, 20161*x^3-51*x^2-78*x, 344*x^3+2*x^2-1," +
                " 31*x^3+5*x^2+33*x+37, 5*x^3-117*x^2+13*x+8, 13*x^3-3*x^2+3, ...]",
                "{x^3=859, x^3-x=374, x^3-x^2=355, x^3-1=351, x^3+x^2=348, x^3+1=327, x^3+x=323, x^3-x-1=168," +
                " 2*x^3-x=167, x^3-x^2-1=165}",
                2.9999999999775233,
                5.177311750004235
        );
        positivePrimitivePolynomials_int_helper(
                10,
                8,
                "[81*x^8+1224*x^7+14*x^6-3*x^5+x^4-2*x^3+x^2+56*x+566," +
                " 101342*x^8-739*x^7-111061034*x^5-2*x^4-506*x^3+87*x^2-30*x+47," +
                " 15*x^8+3*x^7-394953281*x^6+131588190*x^5+1510370*x^4-17*x^3+16276*x^2-8682*x+345112274," +
                " 13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+3064121," +
                " 1597518*x^8+1105224*x^7+3133*x^6+x^5-3*x^4-3*x^2-x-41349439," +
                " 7*x^8-330228993066*x^7-11*x^6+1524767947173*x^5-7*x^3-x^2+31*x-2304971396104," +
                " 112*x^8+397143*x^7+50*x^6-2*x^5-27*x^4-231522*x^3+x^2+6*x+17," +
                " 7*x^8+32022602*x^7+4886*x^6+60*x^4+3*x^3+629*x^2+114*x-23," +
                " 36*x^8-632*x^7-4663*x^6-51*x^5-2743875*x^4-435*x^3+19882*x^2-13369365953*x+2," +
                " 398077*x^8-51*x^7-3*x^6+973*x^5-5*x^4-82699*x^3-202206016023*x^2+x+262," +
                " 5*x^8-146275*x^6-x^5-155*x^4-95*x^3-x^2-6158262286914*x+486," +
                " 203*x^8+5994968441128*x^6-329*x^5+6*x^4-105*x^3-6*x^2-12*x-14," +
                " 2*x^8+209*x^6-23971*x^5+451*x^4+13*x^3-79595301742*x^2-200026134210*x-86," +
                " 3910*x^8-975*x^7-108*x^5-4040*x^4-7*x^3+x^2-6*x+84," +
                " 58*x^8-3*x^7-39*x^6+52*x^5+6*x^4+527*x^3-2733*x^2-4050415666*x," +
                " 2985*x^8+15*x^7+2*x^6+x^5-91641*x^4-30*x^2-7*x-4," +
                " 2804*x^8-3*x^7-6152*x^6-1423414505*x^5+37841*x^4-148*x^3-24*x^2+717*x+2," +
                " 860*x^8-2*x^7+18532*x^6+3*x^5+14*x^4-148*x^3+254284349*x^2+6*x+30," +
                " 316844212*x^8-28*x^7+20*x^6+2*x^5-13*x^4-93538016*x^3-467*x^2+14502," +
                " 5*x^8-x^7+308602588*x^6+x^3-x-54, ...]",
                "{81*x^8+1224*x^7+14*x^6-3*x^5+x^4-2*x^3+x^2+56*x+566=1," +
                " 101342*x^8-739*x^7-111061034*x^5-2*x^4-506*x^3+87*x^2-30*x+47=1," +
                " 15*x^8+3*x^7-394953281*x^6+131588190*x^5+1510370*x^4-17*x^3+16276*x^2-8682*x+345112274=1," +
                " 13297953072*x^8-8080881*x^7+152929*x^6+3928*x^5-x^3+3346*x^2-2*x+3064121=1," +
                " 1597518*x^8+1105224*x^7+3133*x^6+x^5-3*x^4-3*x^2-x-41349439=1," +
                " 7*x^8-330228993066*x^7-11*x^6+1524767947173*x^5-7*x^3-x^2+31*x-2304971396104=1," +
                " 112*x^8+397143*x^7+50*x^6-2*x^5-27*x^4-231522*x^3+x^2+6*x+17=1," +
                " 7*x^8+32022602*x^7+4886*x^6+60*x^4+3*x^3+629*x^2+114*x-23=1," +
                " 36*x^8-632*x^7-4663*x^6-51*x^5-2743875*x^4-435*x^3+19882*x^2-13369365953*x+2=1," +
                " 398077*x^8-51*x^7-3*x^6+973*x^5-5*x^4-82699*x^3-202206016023*x^2+x+262=1}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitivePolynomials(),
                output,
                topSampleCount,
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
                "[x^3+85*x+3, x-2, 1, x^2+x-10, 7*x+1, 1, 1, 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1," +
                " 7*x^2-2, x^3-2*x^2-x, 11*x^3-3*x^2+x-2, 1, 1, 1, 1, ...]",
                "{1=388581, x=97371, x+1=24510, x^2=24476, x-1=24469, x+2=6241, x^3=6238, 2*x+1=6185, x^2+1=6178," +
                " 3*x-1=6144}",
                1.319364999992647,
                1.1321792818114707
        );
        positivePrimitivePolynomials_helper(
                5,
                3,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, 3*x-242," +
                " 7*x^2+15*x-23, 2*x^7-x^5+13*x^3+9*x^2-2*x+115001, 13*x^5+8*x^4-x^3-329*x," +
                " 13*x^8-3*x^7+3*x^5-21*x^4-13*x^2+8*x+9, x^4-2*x^3+3347*x^2+9*x+3, 5209*x^4+6*x^3-x^2-3*x-149," +
                " 6*x^4-12135*x^3-308*x^2-61*x+3, 18566*x^8-19*x^6+31*x^4-5656*x^3-3618*x-336," +
                " 33*x^4-59*x^3-13542*x+1166, x^4+x^3-48401*x+10, 3*x^5+86*x^4+4843*x^3-x^2+111*x+4," +
                " 3*x^5-832*x^4+237*x^3+7*x^2+19*x-140502, ...]",
                "{1=60411, x=7609, x-1=3205, x+1=3188, x-3=1370, 2*x-1=1337, 3*x+1=1335, x+2=1314, 2*x+1=1303," +
                " x-2=1303}",
                4.136915999990028,
                5.037304094541455
        );
        positivePrimitivePolynomials_helper(
                10,
                8,
                "[8293038*x^31-16532643*x^30-4*x^29-19*x^28-16*x^27+54*x^26-2404*x^24+5896747328932606365*x^23-" +
                "239*x^22-x^21+2*x^20-3*x^19-232*x^18+2*x^17+81*x^16+1224*x^15+14*x^14-3*x^13+x^12-2*x^11+x^10+" +
                "56*x^9+566*x^8-437219*x^7-66364*x^6-124*x^5-4626764*x^4-645698713*x^3-117*x^2+220*x-2129," +
                " 13297953072*x^16-8080881*x^15+152929*x^14+3928*x^13-x^11+3346*x^10-2*x^9+3064121*x^8-20040*x^7-" +
                "3015*x^5+5*x^4-9*x^3+7689363762084*x^2-432*x," +
                " 10025*x^18+2*x^17-434063*x^16+5*x^15+x^14-736*x^13+7182*x^12-7*x^11+3*x^10-248*x^9-13*x^8+" +
                "1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x-4," +
                " 31*x^12-2304971396104*x^11-355615*x^10-3640006*x^9-3421*x^8+25689*x^7+524*x^6-789*x^5+3*x^4+" +
                "663885*x^3+9*x^2-21118*x-14, 7*x^6-127*x^5-5*x^4-1684*x^3+36*x^2-632*x-29239, 36587*x+54," +
                " x^12+262*x^11-2312*x^10+24*x^9+65*x^8-1060*x^7-40*x^6+22*x^3-117129*x^2-15528*x-130," +
                " 33*x^9-3*x^8+x^7+995620*x^6-3117942350*x^5-3*x^4+23*x^3-7*x^2-158207289*x+5473," +
                " 6*x^10+30*x^9+2804*x^8-3*x^7-6152*x^6-1423414505*x^5+37841*x^4-148*x^3-24*x^2+717*x+4," +
                " 3244*x^8-30660*x^7+5702*x^6+3*x^5+x^4+35006*x^3+104*x^2-17*x-38573810," +
                " 438297*x^12-3578*x^11-9859388*x^10-66*x^9-14*x^8-317*x^7-109*x^6+11*x^5+402824061598125*x^3-" +
                "208090*x^2+12*x+36, 308602588*x^5+x^2-2, x^4+3*x^3+4*x^2+128423*x+91," +
                " 530566274871838627*x^8-40*x^7-1522584726*x^6+8978*x^5-42*x^4-2*x^2+2541395*x," +
                " 10141*x^43+940665*x^41-192*x^40+61*x^38-11*x^37-15687*x^36+x^35+1782*x^34+5379*x^33-3*x^32-" +
                "1066*x^30+319661334*x^29-10*x^28+901*x^27-205195*x^26+21920*x^25+3930*x^24+43*x^23+" +
                "1322424107679*x^22+83481365755*x^21+3*x^20-655648*x^18-532936*x^17-9532*x^16-x^15-2471048*x^14+" +
                "2657104*x^13+13*x^12-x^11+249*x^10-154687192*x^9+4*x^8+3*x^6+45331*x^5-9647*x^4-77774*x^3+10*x^2-2," +
                " 4*x^5-7*x^4-226081757405*x^3-3*x^2-1778987*x-397," +
                " 36056*x^17+5*x^16-135597*x^15-6*x^14+1662*x^13-4*x^12-20804*x^11+3237746*x^10+18*x^9+86*x^8-" +
                "867*x^7-179*x^6-x^5-87*x^4-50*x^2+7*x+12990436, 819*x^5-43*x^4+1103752*x^3+829289*x^2-55*x+54," +
                " 161*x^35+19*x^34+242259*x^33-3*x^31-495015*x^30-2*x^29+405597*x^28+661*x^27+210*x^26-6*x^25+" +
                "31*x^23+42*x^22+30*x^21+12*x^20+6*x^18+51*x^17-6*x^16-538014882259*x^15+1969*x^14-x^13+8*x^12+" +
                "3554*x^11-150263*x^10+168244*x^8-122221910*x^7-29*x^6+53*x^5-31*x^4-246*x^3+x^2-6*x+6729," +
                " 47*x^10+262*x^9-9*x^8-76*x^6-58416*x^5-56*x^4-37*x^3-3*x^2+2*x-1531, ...]",
                "{1=12153, x=962, x-1=454, x+1=446, x-3=218, x-2=212, x+2=203, x+3=194, 2*x-1=192, 3*x+1=178}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitivePolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
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
                "[x^3+85*x+3, x-2, 1, x^2+x-10, 7*x+1, 1, 1, 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1," +
                " 7*x^2-2, x^3-2*x^2-x, 11*x^3-3*x^2+x-2, 1, 1, 1, 1, ...]",
                "{1=388581, x=97371, x+1=24510, x^2=24476, x-1=24469, x+2=6241, x^3=6238, 2*x+1=6185, x^2+1=6178," +
                " 3*x-1=6144}",
                1.319364999992647,
                1.1321792818114707
        );
        positivePrimitivePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "[x^3+85*x+3, x-2, 1, x^2+x-10, 7*x+1, 1, 1, 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1," +
                " 7*x^2-2, x^3-2*x^2-x, 11*x^3-3*x^2+x-2, 1, 1, 1, 1, ...]",
                "{1=388581, x=97371, x+1=24510, x^2=24476, x-1=24469, x+2=6241, x^3=6238, 2*x+1=6185, x^2+1=6178," +
                " 3*x-1=6144}",
                1.319364999992647,
                1.1321792818114707
        );
        positivePrimitivePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, 3*x-242," +
                " 7*x^2+15*x-23, 2*x^7-x^5+13*x^3+9*x^2-2*x+115001, 13*x^5+8*x^4-x^3-329*x," +
                " 13*x^8-3*x^7+3*x^5-21*x^4-13*x^2+8*x+9, x^4-2*x^3+3347*x^2+9*x+3, 5209*x^4+6*x^3-x^2-3*x-149," +
                " 6*x^4-12135*x^3-308*x^2-61*x+3, 18566*x^8-19*x^6+31*x^4-5656*x^3-3618*x-336," +
                " 33*x^4-59*x^3-13542*x+1166, x^4+x^3-48401*x+10, 3*x^5+86*x^4+4843*x^3-x^2+111*x+4," +
                " 3*x^5-832*x^4+237*x^3+7*x^2+19*x-140502, ...]",
                "{1=60411, x=7609, x-1=3205, x+1=3188, x-3=1370, 2*x-1=1337, 3*x+1=1335, x+2=1314, 2*x+1=1303," +
                " x-2=1303}",
                4.136915999990028,
                5.037304094541455
        );
        positivePrimitivePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, 3*x-242," +
                " 7*x^2+15*x-23, 2*x^7-x^5+13*x^3+9*x^2-2*x+115001, 13*x^5+8*x^4-x^3-329*x," +
                " 13*x^8-3*x^7+3*x^5-21*x^4-13*x^2+8*x+9, x^4-2*x^3+3347*x^2+9*x+3, 5209*x^4+6*x^3-x^2-3*x-149," +
                " 6*x^4-12135*x^3-308*x^2-61*x+3, 18566*x^8-19*x^6+31*x^4-5656*x^3-3618*x-336," +
                " 33*x^4-59*x^3-13542*x+1166, x^4+x^3-48401*x+10, 3*x^5+86*x^4+4843*x^3-x^2+111*x+4," +
                " 3*x^5-832*x^4+237*x^3+7*x^2+19*x-140502, ...]",
                "{1=60411, x=7609, x-1=3205, x+1=3188, x-3=1370, 2*x-1=1337, 3*x+1=1335, x+2=1314, 2*x+1=1303," +
                " x-2=1303}",
                4.136915999990028,
                5.037304094541455
        );
        positivePrimitivePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[x^2+30*x-46, 4*x^3-181301872*x^2+x-2002, 633*x^2-4*x+1, 3*x^3-x-1, 91*x^2+122224*x+47," +
                " 114*x^2-x-15659, 12*x^3+x^2+223, 47*x^2+2*x-3254, x^5+27*x^4-11815*x^3-38*x^2-5*x+7," +
                " 7*x^3+15*x^2-15*x+1, 766*x^2-730*x+13, 1856865*x^3+344*x^2+2*x-1, 33*x^4+37*x^3-x^2-3*x+6454," +
                " 1537*x^5-26*x^4+5*x^3-11*x^2+79, 13*x^2+16*x, x^3+12*x^2+21*x, 2216*x^4+3*x^3+13*x^2-224," +
                " 6*x^2-10*x+7, x^2-2*x+3347, 1305*x^4+6*x^2-12135*x-118, ...]",
                "{x^2=2633, x^2+x=1137, x^2+1=1115, x^2-x=1057, x^2-1=1045, 3*x^2+x=512, x^2+x-1=498, x^2+3=474," +
                " x^2-x+1=468, 2*x^2+x=464}",
                3.088038999968085,
                5.164304939233438
        );
        positivePrimitivePolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "[8293038*x^31-16532643*x^30-4*x^29-19*x^28-16*x^27+54*x^26-2404*x^24+5896747328932606365*x^23-" +
                "239*x^22-x^21+2*x^20-3*x^19-232*x^18+2*x^17+81*x^16+1224*x^15+14*x^14-3*x^13+x^12-2*x^11+x^10+" +
                "56*x^9+566*x^8-437219*x^7-66364*x^6-124*x^5-4626764*x^4-645698713*x^3-117*x^2+220*x-2129," +
                " 13297953072*x^16-8080881*x^15+152929*x^14+3928*x^13-x^11+3346*x^10-2*x^9+3064121*x^8-20040*x^7-" +
                "3015*x^5+5*x^4-9*x^3+7689363762084*x^2-432*x," +
                " 10025*x^18+2*x^17-434063*x^16+5*x^15+x^14-736*x^13+7182*x^12-7*x^11+3*x^10-248*x^9-13*x^8+" +
                "1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x-4," +
                " 31*x^12-2304971396104*x^11-355615*x^10-3640006*x^9-3421*x^8+25689*x^7+524*x^6-789*x^5+3*x^4+" +
                "663885*x^3+9*x^2-21118*x-14, 7*x^6-127*x^5-5*x^4-1684*x^3+36*x^2-632*x-29239, 36587*x+54," +
                " x^12+262*x^11-2312*x^10+24*x^9+65*x^8-1060*x^7-40*x^6+22*x^3-117129*x^2-15528*x-130," +
                " 33*x^9-3*x^8+x^7+995620*x^6-3117942350*x^5-3*x^4+23*x^3-7*x^2-158207289*x+5473," +
                " 6*x^10+30*x^9+2804*x^8-3*x^7-6152*x^6-1423414505*x^5+37841*x^4-148*x^3-24*x^2+717*x+4," +
                " 3244*x^8-30660*x^7+5702*x^6+3*x^5+x^4+35006*x^3+104*x^2-17*x-38573810," +
                " 438297*x^12-3578*x^11-9859388*x^10-66*x^9-14*x^8-317*x^7-109*x^6+11*x^5+402824061598125*x^3-" +
                "208090*x^2+12*x+36, 308602588*x^5+x^2-2, x^4+3*x^3+4*x^2+128423*x+91," +
                " 530566274871838627*x^8-40*x^7-1522584726*x^6+8978*x^5-42*x^4-2*x^2+2541395*x," +
                " 10141*x^43+940665*x^41-192*x^40+61*x^38-11*x^37-15687*x^36+x^35+1782*x^34+5379*x^33-3*x^32-" +
                "1066*x^30+319661334*x^29-10*x^28+901*x^27-205195*x^26+21920*x^25+3930*x^24+43*x^23+" +
                "1322424107679*x^22+83481365755*x^21+3*x^20-655648*x^18-532936*x^17-9532*x^16-x^15-2471048*x^14+" +
                "2657104*x^13+13*x^12-x^11+249*x^10-154687192*x^9+4*x^8+3*x^6+45331*x^5-9647*x^4-77774*x^3+10*x^2-2," +
                " 4*x^5-7*x^4-226081757405*x^3-3*x^2-1778987*x-397," +
                " 36056*x^17+5*x^16-135597*x^15-6*x^14+1662*x^13-4*x^12-20804*x^11+3237746*x^10+18*x^9+86*x^8-" +
                "867*x^7-179*x^6-x^5-87*x^4-50*x^2+7*x+12990436, 819*x^5-43*x^4+1103752*x^3+829289*x^2-55*x+54," +
                " 161*x^35+19*x^34+242259*x^33-3*x^31-495015*x^30-2*x^29+405597*x^28+661*x^27+210*x^26-6*x^25+" +
                "31*x^23+42*x^22+30*x^21+12*x^20+6*x^18+51*x^17-6*x^16-538014882259*x^15+1969*x^14-x^13+8*x^12+" +
                "3554*x^11-150263*x^10+168244*x^8-122221910*x^7-29*x^6+53*x^5-31*x^4-246*x^3+x^2-6*x+6729," +
                " 47*x^10+262*x^9-9*x^8-76*x^6-58416*x^5-56*x^4-37*x^3-3*x^2+2*x-1531, ...]",
                "{1=12153, x=962, x-1=454, x+1=446, x-3=218, x-2=212, x+2=203, x+3=194, 2*x-1=192, 3*x+1=178}",
                9.446762000009086,
                10.014196839384036
        );
        positivePrimitivePolynomialsAtLeast_helper(
                10,
                8,
                2,
                "[105065295*x^5-454294*x^4-622*x^3-909910*x^2+8293038*x-41698467," +
                " 4*x^5+x^4-2300*x^3+18*x^2-1283*x-1862166," +
                " 1510370*x^14-17*x^13+16276*x^12-8682*x^11+345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-" +
                "506*x^4+87*x^3-30*x^2+47*x-125256870," +
                " 7689363762084*x^7-432*x^6+3186328*x^5+15*x^3+3*x^2-394953281*x+7067616," +
                " 13297953072*x^6-8080881*x^5+152929*x^4+3928*x^3-x+45, x^5-3*x^4-3*x^2-x-5928689," +
                " 7182*x^7-7*x^6+3*x^5-248*x^4-13*x^3+1597518*x^2+1105224*x+20," +
                " 17*x^5+7*x^4-330228993066*x^3-11*x^2+1524767947173*x+1," +
                " 19882*x^18-13369365953*x^17+2*x^16-375*x^15-589*x^14+44117477702*x^13+207*x^11-198077*x^10-" +
                "956783*x^9-1344*x^8+27*x^7+7*x^6+32022602*x^5+4886*x^4+60*x^2+3*x+3701," +
                " 5*x^5-146275*x^3-x^2-155*x-447, 6*x^4-105*x^3-6*x^2-12*x-2," +
                " 772*x^7-6288*x^6+281*x^5+2*x^4+209*x^2-23971*x+707," +
                " 2*x^8-23274*x^7+504615376*x^6+33*x^5-3*x^4+x^3+995620*x^2-3117942350*x-5, x^3-6*x^2+84*x-164," +
                " 2*x^2+x-222713, 2*x^2+2985*x+7, 100*x^5-163963*x^3+5*x^2-x+15001308," +
                " x^7-247*x^6-253781*x^5+128*x^4-12*x^3+2214*x^2-151684*x, 249*x^5-154687192*x^4+4*x^3+3*x+3889," +
                " 3*x^4-655648*x^2-532936*x-17724, ...]",
                "{x^2=123, x^2-x=59, x^2+x=51, x^2-1=51, x^2+x+1=39, 2*x^2+x=37, x^2+1=36, 2*x^2+1=31, 3*x^2-1=30," +
                " x^2-2=30}",
                8.223359000011806,
                10.039688685940462
        );
        positivePrimitivePolynomialsAtLeast_helper(
                10,
                8,
                7,
                "[56*x^10+566*x^9-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-" +
                "2870934583, 54*x^7-2404*x^5+5896747328932606365*x^4-239*x^3-x^2+2*x-3," +
                " 345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-506*x^4+87*x^3-30*x^2+47*x-2586436," +
                " 3186328*x^9+15*x^7+3*x^6-394953281*x^5+131588190*x^4+1510370*x^3-17*x^2+16276*x-1514," +
                " 1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x, 5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-5," +
                " 663885*x^11+9*x^10-21118*x^9-6*x^8-39554*x^7-12*x^6-419*x^5-510*x^4-10*x^3+10025*x^2+2*x-171919," +
                " 17*x^8+7*x^7-330228993066*x^6-11*x^5+1524767947173*x^4-7*x^2-x+7," +
                " 629*x^10+114*x^9-23*x^8+112*x^7+397143*x^6+50*x^5-2*x^4-27*x^3-231522*x^2+3*x," +
                " 207*x^9-198077*x^8-956783*x^7-1344*x^6+27*x^5+7*x^4+32022602*x^3+4886*x^2+155," +
                " 36587*x^7+22*x^6-x^5-109310*x^4-1543*x^3-174785977915*x^2-2322630*x-146860746," +
                " 29306127664*x^7-4*x^6+372*x^5+398077*x^4-51*x^3-3*x^2+973*x-5," +
                " 203*x^7+5994968441128*x^5-329*x^4+6*x^3-105*x^2-6*x-12," +
                " 23*x^7-7*x^6-158207289*x^5+1377*x^4+772*x^3-6288*x^2+281*x+1," +
                " x^7-91641*x^6-30*x^4-7*x^3-4*x^2+58*x-3, 37841*x^7-148*x^6-24*x^5+717*x^4+2*x^3+2985*x^2+15*x+1," +
                " 3*x^9+14*x^8-148*x^7+254284349*x^6+6*x^5+30*x^4+2804*x^3-3*x^2-6152*x-349672681," +
                " 2*x^8-13*x^7-93538016*x^6-467*x^5+14502*x^3+860*x^2-2*x+10340," +
                " 5767973249*x^7+21493072257233*x^6+x^5-106*x^4+2245*x^3+316844212*x^2-28*x+20," +
                " x^8+35006*x^7+104*x^6-17*x^5-21796594*x^4-3111*x^3-240*x^2-11, ...]",
                "{56*x^10+566*x^9-437219*x^8-66364*x^7-124*x^6-4626764*x^5-645698713*x^4-117*x^3+220*x^2-337*x-" +
                "2870934583=1, 54*x^7-2404*x^5+5896747328932606365*x^4-239*x^3-x^2+2*x-3=1," +
                " 345112274*x^10+101342*x^9-739*x^8-111061034*x^6-2*x^5-506*x^4+87*x^3-30*x^2+47*x-2586436=1," +
                " 3186328*x^9+15*x^7+3*x^6-394953281*x^5+131588190*x^4+1510370*x^3-17*x^2+16276*x-1514=1," +
                " 1597518*x^7+1105224*x^6+3133*x^5+x^4-3*x^3-3*x=1," +
                " 5*x^7+x^6-736*x^5+7182*x^4-7*x^3+3*x^2-248*x-5=1," +
                " 663885*x^11+9*x^10-21118*x^9-6*x^8-39554*x^7-12*x^6-419*x^5-510*x^4-10*x^3+10025*x^2+2*x-171919=1," +
                " 17*x^8+7*x^7-330228993066*x^6-11*x^5+1524767947173*x^4-7*x^2-x+7=1," +
                " 629*x^10+114*x^9-23*x^8+112*x^7+397143*x^6+50*x^5-2*x^4-27*x^3-231522*x^2+3*x=1," +
                " 207*x^9-198077*x^8-956783*x^7-1344*x^6+27*x^5+7*x^4+32022602*x^3+4886*x^2+155=1}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).squareFreePolynomials(degree),
                output,
                topSampleCount,
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
                "[5, 221, 1, -2, 3, 1, -1, -1, 1, -1, -1, 5, -2, 1, 1, 21, -1, 19, -1, 1, ...]",
                "{-1=25061, 1=24828, 3=6332, -2=6326, 2=6246, -3=6236, 4=1637, 5=1628, -7=1607, 6=1561}",
                0.0,
                1.666559999999875
        );
        squareFreePolynomials_int_helper(
                1,
                1,
                "[3*x-2, x, -x-1, -x-1, -2*x+5, x+1, 21*x, 19*x-1, x-1, -2*x-1, 2*x, 3*x, -6*x, -x-1, -2*x, x, x," +
                " 6*x, -2*x-1, -2*x-2, ...]",
                "{-x=12564, x=12483, -x+1=3179, 3*x=3176, -2*x=3142, -x-1=3114, x+1=3112, x-1=3103, 2*x=3079," +
                " -3*x=2988}",
                0.9999999999980838,
                1.2471400000007853
        );
        squareFreePolynomials_int_helper(
                5,
                3,
                "[-233*x^3+1256222*x^2+21*x+21, 9*x^3+x^2-1, -x^3-6*x^2-1661016*x-117, -28*x^3-576*x^2-39244*x-67," +
                " 30*x^3-46*x^2-16989*x-38, -x^3+2*x^2-8*x, x^3-4551*x^2+62, -92*x^3+x^2+9*x, -70*x^3-x^2+x-7," +
                " 4*x^3-181301872*x^2+x-3026, -19*x^3-2*x^2-x+2, -64580*x^3+5*x-4, -13*x^3-241920*x^2+7818*x+41," +
                " 5*x^3-122*x^2-394*x-238, x^3-224*x^2+62*x-1, -24*x^3+17*x^2+633*x-4, -2708*x^3+5*x^2-13*x-15," +
                " 11*x^3+91*x^2+122224*x+111, -618*x^3+16127*x^2-7*x, 223*x^3+114*x^2-x-15659, ...]",
                "{x^3+1=21, -x^3+1=20, -x^3-x=19, x^3-x=18, -x^3+x=18, x^3+x=17, x^3-1=15, -x^3-1=13, -x^3+x^2+x=11," +
                " -x^3-x+1=11}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).squareFreePolynomials(),
                output,
                topSampleCount,
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
                "[1, x-2, 9, x, -2*x^5-x^4+x^3-x^2+19*x-5, 2*x, 7, x, -3, -1, -32, -2*x, -2*x, 1," +
                " -2*x^6+x^5+4*x^4+4*x^3-11*x^2+x-49, x-1, 1, 4*x-1, -x^3+13*x^2-77*x-6, -x^3-x^2+2*x, ...]",
                "{1=13508, -1=13310, 3=3428, -2=3386, -x=3371, x=3370, 2=3315, -3=3238, 2*x=879, 4=863}",
                0.8634199999996628,
                1.3381041311144288
        );
        squareFreePolynomials_helper(
                5,
                3,
                "[-x^3-233*x^2+1256222*x+85, 25, -39244*x^4-67*x^3-x^2-6*x-3758168," +
                " 2*x^7+x^6+30*x^5-46*x^4-16989*x^3-38*x^2-12*x-1, -8*x^2+1, 62*x-3," +
                " -70*x^8-x^7+x^6-7*x^5-92*x^4+x^3+9*x^2+4, -x^8+2*x^7-2404*x^4+54644*x^3+4*x^2-181301872*x+3," +
                " 41*x^4-64580*x^3+5*x-24, x^9-224*x^8+62*x^7-x^6+5*x^5-122*x^4-394*x^3-238*x^2-13*x-504064," +
                " 17*x^2+633*x, -2708*x^4+5*x^3-13*x^2-15*x, -1, 7, -618*x^6+16127*x^5-7*x^4+11*x^2+91*x+122224," +
                " -x^12+47*x^11+2*x^10-7350*x^9-2*x^8-4*x^7-152*x^6+12*x^5+x^4+223*x^2+114*x-5," +
                " -4*x^4+7*x^2-2*x+438," +
                " -5*x^20+4*x^18-x^17-14*x^14+15*x^13+41*x^11-97*x^10-26*x^9+102910*x^8+2462*x^7+x^6-10*x^5-7*x^3-" +
                "2275*x^2-158, x^4+27*x^3-11815*x^2-38*x-5, -37*x^7+7*x^5+15*x^4-15*x^3+2*x^2+5, ...]",
                "{-1=1779, 1=1746, -2=712, 3=706, -3=692, 2=671, 5=328, 4=309, -6=308, -4=300}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).squareFreePolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
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
                "[1, x-2, 9, x, -2*x^5-x^4+x^3-x^2+19*x-5, 2*x, 7, x, -3, -1, -32, -2*x, -2*x, 1," +
                " -2*x^6+x^5+4*x^4+4*x^3-11*x^2+x-49, x-1, 1, 4*x-1, -x^3+13*x^2-77*x-6, -x^3-x^2+2*x, ...]",
                "{1=13508, -1=13310, 3=3428, -2=3386, -x=3371, x=3370, 2=3315, -3=3238, 2*x=879, 4=863}",
                0.8634199999996628,
                1.3381041311144288
        );
        squareFreePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "[x^3+85*x+3, -4, -2*x^2+3, x-2, 1, x^2+x-10, 37, 7*x+1, -5*x-1, 2*x, 1, -2*x^4-x^2-x, -3, -1," +
                " -6*x-1, 2*x, 1, -2*x, -2, -3, ...]",
                "{-1=13356, 1=13336, 3=3391, 2=3353, -x=3338, x=3316, -3=3296, -2=3290, -3*x=919, 4=861}",
                0.8642899999996785,
                1.3383593754208227
        );
        squareFreePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[-x^3-233*x^2+1256222*x+85, 25, -39244*x^4-67*x^3-x^2-6*x-3758168," +
                " 2*x^7+x^6+30*x^5-46*x^4-16989*x^3-38*x^2-12*x-1, -8*x^2+1, 62*x-3," +
                " -70*x^8-x^7+x^6-7*x^5-92*x^4+x^3+9*x^2+4, -x^8+2*x^7-2404*x^4+54644*x^3+4*x^2-181301872*x+3," +
                " 41*x^4-64580*x^3+5*x-24, x^9-224*x^8+62*x^7-x^6+5*x^5-122*x^4-394*x^3-238*x^2-13*x-504064," +
                " 17*x^2+633*x, -2708*x^4+5*x^3-13*x^2-15*x, -1, 7, -618*x^6+16127*x^5-7*x^4+11*x^2+91*x+122224," +
                " -x^12+47*x^11+2*x^10-7350*x^9-2*x^8-4*x^7-152*x^6+12*x^5+x^4+223*x^2+114*x-5," +
                " -4*x^4+7*x^2-2*x+438," +
                " -5*x^20+4*x^18-x^17-14*x^14+15*x^13+41*x^11-97*x^10-26*x^9+102910*x^8+2462*x^7+x^6-10*x^5-7*x^3-2" +
                "275*x^2-158, x^4+27*x^3-11815*x^2-38*x-5, -37*x^7+7*x^5+15*x^4-15*x^3+2*x^2+5, ...]",
                "{-1=1779, 1=1746, -2=712, 3=706, -3=692, 2=671, 5=328, 4=309, -6=308, -4=300}",
                3.9696700000014955,
                5.119983821854156
        );
        squareFreePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, -6*x^2-1661016*x-13, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " -3026*x-239, 4*x-986608240," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56, 238*x," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38," +
                " 2*x^4-7350*x^3-2*x^2-4*x-280, 31, -46, -10*x^10-7*x^8-2275*x^7-98*x^5-16624788*x^4-4*x^3+7*x-6," +
                " -97*x^3-26*x^2+102910*x+4510, 25, -1, -5*x^2+1, -11815*x^3-38*x^2-5*x+7, x+7, ...]",
                "{-1=2116, 1=2082, -2=890, 3=889, -3=840, 2=832, -7=386, -6=379, 4=371, 5=365}",
                2.9729500000011333,
                5.180837916413763
        );
        squareFreePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[-117*x^2+9*x+1, -16989*x^8-38*x^7-28*x^6-576*x^5-39244*x^4-67*x^3-x^2-6*x-55384, x^2+30*x-46," +
                " -x^2+2*x-4, -4551*x^2+30, -92*x^3+x^2+9*x-1, -70*x^3-x^2+x-3, 4*x^3-181301872*x^2+x-2002," +
                " -2*x^6-x^5+2*x^4-2404*x+2517, -64580*x^4+5*x^2-4*x-3, -13*x^3-241920*x^2+7818*x+9," +
                " -224*x^6+62*x^5-x^4+5*x^3-122*x^2-394*x-14, 633*x^2-4*x+1, -2708*x^5+5*x^4-13*x^3-15*x^2-24*x+3," +
                " 3*x^3-x-1, 91*x^2+122224*x+47, -618*x^4+16127*x^3-7*x^2+7, 114*x^2-x-15659, 12*x^3+x^2+223," +
                " -2*x^2-4*x-152, ...]",
                "{-x^2-x=61, -x^2-1=60, x^2+x=59, x^2-x=53, x^2-1=50, -x^2+1=47, x^2+1=42, -x^2+x=42, x^2-2*x=35," +
                " -2*x^2+1=29}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).positivePrimitiveSquareFreePolynomials(degree),
                output,
                topSampleCount,
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
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]",
                "{1=100000}",
                0.0,
                0.9999999999980838
        );
        positivePrimitiveSquareFreePolynomials_int_helper(
                1,
                1,
                "[3*x-2, x, x+1, 19*x-1, x-1, x, x, 115*x+1, x-9, 4*x-11, x+4, x-38, x, 6*x+1, 13*x-77, x, x, x-2," +
                " 11*x-5, x-1, ...]",
                "{x=35824, x-1=8888, x+1=8884, x-3=2279, x+3=2269, x+2=2250, 2*x-1=2234, 2*x+1=2229, 3*x-1=2209," +
                " x-2=2199}",
                0.9999999999980838,
                1.2849400000016107
        );
        positivePrimitiveSquareFreePolynomials_int_helper(
                5,
                3,
                "[9*x^3+x^2-1, 30*x^3-46*x^2-16989*x-38, x^3-4551*x^2+62, 4*x^3-181301872*x^2+x-3026," +
                " 5*x^3-122*x^2-394*x-238, x^3-224*x^2+62*x-1, 11*x^3+91*x^2+122224*x+111, 223*x^3+114*x^2-x-15659," +
                " 438*x^3-46*x^2-x+47, x^3-10*x^2-7, x^3+27*x^2-11815*x-38, 2*x^3+3*x-498, 23*x^3+9*x^2-17*x+766," +
                " 20161*x^3-51*x^2-78*x, 344*x^3+2*x^2-1, 31*x^3+5*x^2+33*x+37, 5*x^3-117*x^2+13*x+8," +
                " 13*x^3-3*x^2+3, 15*x^3+2216*x^2+3*x+13, 3*x^3+6*x-10, ...]",
                "{x^3-x=44, x^3+1=41, x^3-1=34, x^3+x=33, x^3-x^2-1=22, x^3-3*x=21, x^3+2=20, x^3-2*x=19," +
                " 2*x^3-x=19, 3*x^3+1=18}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).positivePrimitiveSquareFreePolynomials(),
                output,
                topSampleCount,
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
                "[x^3+85*x+3, x-2, 1, x^2+x-10, 7*x+1, 1, 1, 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1," +
                " 7*x^2-2, x^3-2*x^2-x, 11*x^3-3*x^2+x-2, 1, 1, 1, 1, ...]",
                "{1=41456, x=10397, x+1=2632, x-1=2539, 2*x+1=683, x+2=679, x^2-1=672, x^2+1=671, x^2-x=665, x-3=650}",
                1.1869899999997364,
                1.1991778654697347
        );
        positivePrimitiveSquareFreePolynomials_helper(
                5,
                3,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, 3*x-242," +
                " 7*x^2+15*x-23, 2*x^7-x^5+13*x^3+9*x^2-2*x+115001, 13*x^5+8*x^4-x^3-329*x," +
                " 13*x^8-3*x^7+3*x^5-21*x^4-13*x^2+8*x+9, x^4-2*x^3+3347*x^2+9*x+3, 5209*x^4+6*x^3-x^2-3*x-149," +
                " 6*x^4-12135*x^3-308*x^2-61*x+3, 18566*x^8-19*x^6+31*x^4-5656*x^3-3618*x-336," +
                " 33*x^4-59*x^3-13542*x+1166, x^4+x^3-48401*x+10, 3*x^5+86*x^4+4843*x^3-x^2+111*x+4," +
                " 3*x^5-832*x^4+237*x^3+7*x^2+19*x-140502, ...]",
                "{1=6102, x=822, x-1=350, x+1=303, 3*x-1=139, 2*x-1=138, x+3=135, x+2=133, 3*x+1=131, x-3=125}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale)
                        .positivePrimitiveSquareFreePolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
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
                "[x^3+85*x+3, x-2, 1, x^2+x-10, 7*x+1, 1, 1, 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1," +
                " 7*x^2-2, x^3-2*x^2-x, 11*x^3-3*x^2+x-2, 1, 1, 1, 1, ...]",
                "{1=41456, x=10397, x+1=2632, x-1=2539, 2*x+1=683, x+2=679, x^2-1=672, x^2+1=671, x^2-x=665, x-3=650}",
                1.1869899999997364,
                1.1991778654697347
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                1,
                1,
                0,
                "[x^3+85*x+3, x-2, 1, x^2+x-10, 7*x+1, 1, 1, 4*x^4+4*x^3-11*x^2-1, 1, 1, x^2+x-1, 1, 13*x^2+4*x-1," +
                " 7*x^2-2, x^3-2*x^2-x, 11*x^3-3*x^2+x-2, 1, 1, 1, 1, ...]",
                "{1=41456, x=10397, x+1=2632, x-1=2539, 2*x+1=683, x+2=679, x^2-1=672, x^2+1=671, x^2-x=665, x-3=650}",
                1.1869899999997364,
                1.1991778654697347
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, 3*x-242," +
                " 7*x^2+15*x-23, 2*x^7-x^5+13*x^3+9*x^2-2*x+115001, 13*x^5+8*x^4-x^3-329*x," +
                " 13*x^8-3*x^7+3*x^5-21*x^4-13*x^2+8*x+9, x^4-2*x^3+3347*x^2+9*x+3, 5209*x^4+6*x^3-x^2-3*x-149," +
                " 6*x^4-12135*x^3-308*x^2-61*x+3, 18566*x^8-19*x^6+31*x^4-5656*x^3-3618*x-336," +
                " 33*x^4-59*x^3-13542*x+1166, x^4+x^3-48401*x+10, 3*x^5+86*x^4+4843*x^3-x^2+111*x+4," +
                " 3*x^5-832*x^4+237*x^3+7*x^2+19*x-140502, ...]",
                "{1=6102, x=822, x-1=350, x+1=303, 3*x-1=139, 2*x-1=138, x+3=135, x+2=133, 3*x+1=131, x-3=125}",
                4.111800000001742,
                5.070521147155589
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[9*x^6+x^5-x^3-233*x^2+1256222*x+85, x^7-7*x^6-92*x^5+x^4+9*x^3+x-17," +
                " 62*x^20-x^19+5*x^18-122*x^17-394*x^16-238*x^15-13*x^14-241920*x^13+7818*x^12+41*x^11-64580*x^10+" +
                "5*x^8-4*x^7-19*x^6-2*x^5-x^4+2*x^3-4452, 633*x^3-4*x^2+x-13," +
                " x^7-x^6-x^5-2708*x^4+5*x^3-13*x^2-15*x-56," +
                " x^12+223*x^10+114*x^9-x^8-15659*x^7-618*x^6+16127*x^5-7*x^4+11*x^2+91*x+38, x+7, 3*x-242," +
                " 7*x^2+15*x-23, 2*x^7-x^5+13*x^3+9*x^2-2*x+115001, 13*x^5+8*x^4-x^3-329*x," +
                " 13*x^8-3*x^7+3*x^5-21*x^4-13*x^2+8*x+9, x^4-2*x^3+3347*x^2+9*x+3, 5209*x^4+6*x^3-x^2-3*x-149," +
                " 6*x^4-12135*x^3-308*x^2-61*x+3, 18566*x^8-19*x^6+31*x^4-5656*x^3-3618*x-336," +
                " 33*x^4-59*x^3-13542*x+1166, x^4+x^3-48401*x+10, 3*x^5+86*x^4+4843*x^3-x^2+111*x+4," +
                " 3*x^5-832*x^4+237*x^3+7*x^2+19*x-140502, ...]",
                "{1=6102, x=822, x-1=350, x+1=303, 3*x-1=139, 2*x-1=138, x+3=135, x+2=133, 3*x+1=131, x-3=125}",
                4.111800000001742,
                5.070521147155589
        );
        positivePrimitiveSquareFreePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[x^2+30*x-46, 4*x^3-181301872*x^2+x-2002, 633*x^2-4*x+1, 3*x^3-x-1, 91*x^2+122224*x+47," +
                " 114*x^2-x-15659, 12*x^3+x^2+223, 47*x^2+2*x-3254, x^5+27*x^4-11815*x^3-38*x^2-5*x+7," +
                " 7*x^3+15*x^2-15*x+1, 766*x^2-730*x+13, 1856865*x^3+344*x^2+2*x-1, 33*x^4+37*x^3-x^2-3*x+6454," +
                " 1537*x^5-26*x^4+5*x^3-11*x^2+79, 13*x^2+16*x, x^3+12*x^2+21*x, 2216*x^4+3*x^3+13*x^2-224," +
                " 6*x^2-10*x+7, x^2-2*x+3347, 1305*x^4+6*x^2-12135*x-118, ...]",
                "{x^2+x=117, x^2-1=112, x^2-x=110, x^2+1=105, 3*x^2+x=61, 2*x^2-x=58, x^2+x-1=56, x^2-2*x=54," +
                " 2*x^2-1=53, x^2+2=52}",
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
            @NotNull String topSampleCount,
            double meanDimension,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).irreduciblePolynomials(degree),
                output,
                topSampleCount,
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
                "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...]",
                "{1=100000}",
                0.0,
                0.9999999999980838
        );
        irreduciblePolynomials_int_helper(
                1,
                1,
                "[3*x-2, x, x+1, 19*x-1, x-1, x, x, 115*x+1, x-9, 4*x-11, x+4, x-38, x, 6*x+1, 13*x-77, x, x, x-2," +
                " 11*x-5, x-1, ...]",
                "{x=35824, x-1=8888, x+1=8884, x-3=2279, x+3=2269, x+2=2250, 2*x-1=2234, 2*x+1=2229, 3*x-1=2209," +
                " x-2=2199}",
                0.9999999999980838,
                1.2849400000016107
        );
        irreduciblePolynomials_int_helper(
                5,
                3,
                "[9*x^3+x^2-1, 30*x^3-46*x^2-16989*x-38, x^3-4551*x^2+62, 4*x^3-181301872*x^2+x-3026," +
                " 5*x^3-122*x^2-394*x-238, x^3-224*x^2+62*x-1, 11*x^3+91*x^2+122224*x+111, 223*x^3+114*x^2-x-15659," +
                " 438*x^3-46*x^2-x+47, x^3-10*x^2-7, x^3+27*x^2-11815*x-38, 2*x^3+3*x-498, 23*x^3+9*x^2-17*x+766," +
                " 344*x^3+2*x^2-1, 31*x^3+5*x^2+33*x+37, 5*x^3-117*x^2+13*x+8, 13*x^3-3*x^2+3," +
                " 15*x^3+2216*x^2+3*x+13, 3*x^3+6*x-10, 17*x^3+5209*x^2+6*x-1, ...]",
                "{x^3-x^2-1=24, x^3+2=23, 3*x^3+1=20, x^3+x^2+1=20, x^3+3=19, x^3+x-1=17, x^3-x^2+1=17, 2*x^3-1=16," +
                " x^3+x^2-1=16, 2*x^3+1=15}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 10,
                P.withScale(scale).withSecondaryScale(secondaryScale).irreduciblePolynomials(),
                output,
                topSampleCount,
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
                "[x^2+31*x-21, x+1, x^5+2*x^4-3*x^3-3*x^2-52*x-1, 1, 1, x+1, 2*x^2-12*x-1, 87*x-2, 25*x-3, 41*x-1," +
                " 1, x+1, x+2, 1, 1, 1, x-2, 1, 3*x+29, 1, ...]",
                "{1=50134, x=4182, x+1=1382, x-1=1379, 2*x-1=484, 2*x+1=460, x+3=457, 3*x-1=455, x-2=452, 3*x+1=445}",
                1.0023799999996275,
                2.030174092832156
        );
        irreduciblePolynomials_helper(
                5,
                3,
                "[x^3+30*x^2-46*x-16989, 1, 62*x^3-x^2+2*x-8, x-7," +
                " 122224*x^9+111*x^8+x^7+x^6-x^5-x^4-2708*x^3+5*x^2-13*x-15, 1, 9*x^3+x-17, x-3026, 15*x^3+41*x-97," +
                " 71*x-5, 1, 1, 3*x-74, x^3+27*x^2-11815*x-206, 13*x^4+9*x^3-2*x^2+20161*x-51," +
                " 1537*x^6-26*x^5+5*x^4-11*x^3+31*x+21, 15*x-1, 29*x^3+x-10," +
                " 6*x^8-12135*x^7-308*x^6-61*x^5+17*x^4+5209*x^3+6*x^2-x-7, 1, ...]",
                "{1=19803, x=1144, x+1=515, x-1=471, 3*x+1=226, 2*x+1=215, x+3=215, 2*x-1=213, 3*x-1=200, x-3=192}",
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
            @NotNull String topSampleCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        polynomials_helper(
                DEFAULT_SAMPLE_SIZE / 100,
                P.withScale(scale).withSecondaryScale(secondaryScale).irreduciblePolynomialsAtLeast(minDegree),
                output,
                topSampleCount,
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
                "[x^2+31*x-21, x+1, x^5+2*x^4-3*x^3-3*x^2-52*x-1, 1, 1, x+1, 2*x^2-12*x-1, 87*x-2, 25*x-3, 41*x-1," +
                " 1, x+1, x+2, 1, 1, 1, x-2, 1, 3*x+29, 1, ...]",
                "{1=5047, x=428, x+1=144, x-1=120, 2*x-1=58, x+2=52, x-2=47, 2*x+1=47, x-3=45, 3*x-1=43}",
                1.0019999999999425,
                2.0269230769228126
        );
        irreduciblePolynomialsAtLeast_helper(
                2,
                2,
                0,
                "[x^2+31*x-21, x+1, x^5+2*x^4-3*x^3-3*x^2-52*x-1, 1, 1, x+1, 2*x^2-12*x-1, 87*x-2, 25*x-3, 41*x-1," +
                " 1, x+1, x+2, 1, 1, 1, x-2, 1, 3*x+29, 1, ...]",
                "{1=5047, x=428, x+1=144, x-1=120, 2*x-1=58, x+2=52, x-2=47, 2*x+1=47, x-3=45, 3*x-1=43}",
                1.0019999999999425,
                2.0269230769228126
        );
        irreduciblePolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "[x^3+30*x^2-46*x-16989, 1, 62*x^3-x^2+2*x-8, x-7," +
                " 122224*x^9+111*x^8+x^7+x^6-x^5-x^4-2708*x^3+5*x^2-13*x-15, 1, 9*x^3+x-17, x-3026, 15*x^3+41*x-97," +
                " 71*x-5, 1, 1, 3*x-74, x^3+27*x^2-11815*x-206, 13*x^4+9*x^3-2*x^2+20161*x-51," +
                " 1537*x^6-26*x^5+5*x^4-11*x^3+31*x+21, 15*x-1, 29*x^3+x-10," +
                " 6*x^8-12135*x^7-308*x^6-61*x^5+17*x^4+5209*x^3+6*x^2-x-7, 1, ...]",
                "{1=1986, x=114, x+1=52, x-1=51, 2*x+1=26, x+3=25, x-2=22, 3*x+1=22, x+2=21, 3*x-1=20}",
                2.4041999999999892,
                5.046090123964768
        );
        irreduciblePolynomialsAtLeast_helper(
                5,
                3,
                0,
                "[x^3+30*x^2-46*x-16989, 1, 62*x^3-x^2+2*x-8, x-7," +
                " 122224*x^9+111*x^8+x^7+x^6-x^5-x^4-2708*x^3+5*x^2-13*x-15, 1, 9*x^3+x-17, x-3026, 15*x^3+41*x-97," +
                " 71*x-5, 1, 1, 3*x-74, x^3+27*x^2-11815*x-206, 13*x^4+9*x^3-2*x^2+20161*x-51," +
                " 1537*x^6-26*x^5+5*x^4-11*x^3+31*x+21, 15*x-1, 29*x^3+x-10," +
                " 6*x^8-12135*x^7-308*x^6-61*x^5+17*x^4+5209*x^3+6*x^2-x-7, 1, ...]",
                "{1=1986, x=114, x+1=52, x-1=51, 2*x+1=26, x+3=25, x-2=22, 3*x+1=22, x+2=21, 3*x-1=20}",
                2.4041999999999892,
                5.046090123964768
        );
        irreduciblePolynomialsAtLeast_helper(
                5,
                3,
                2,
                "[x^5-3026*x^4-70*x^3-x^2+x-7, 7818*x^2+41*x-97348, 5*x^2-122*x-394," +
                " 11*x^8+91*x^7+122224*x^6+111*x^5+x^4+x^3-x^2-x-148, 5*x^2-13*x-15, 9*x^2-17*x+766," +
                " 20161*x^2-51*x-78, 13*x^2+9*x-1, 2*x^2-1, x^6+12*x^5+13*x^4-3*x^3+3*x-21, 46834*x^2+1856865*x+216," +
                " 26766*x^3+18566*x^2-19, x^2-2*x+3347, 3763*x^2+1, 14*x^2+17*x+1, 33*x^3-59*x^2-13542," +
                " 7*x^3+19*x^2-74966*x+1, x^4+2*x^3+126*x^2+2, 3*x^2+86*x+4843, 21*x^2+3*x-1, ...]",
                "{x^2+1=15, x^2-2=12, 2*x^2-1=9, x^2-3=7, 3*x^2-1=7, x^2+x+3=7, x^2+2=6, x^2-x+1=6, x^2-x-1=6," +
                " x^2+3=6}",
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
                "{1=499125, a=250897, a^2=124849, a^3=62518, a^4=31407, a^5=15634, a^6=7825, a^7=3926, a^8=1896" +
                ", a^9=956}",
                1.0008359999977228
        );
        exponentVectors_List_Variable_helper(
                8,
                "[a]",
                "[a^31, a^9, a^9, a^5, a^29, a^18, a, a^5, 1, a^17, a^13, 1, a^10, a^6, a^3, a^3, 1, a, a^2, a^5," +
                " ...]",
                "{1=110949, a=98973, a^2=87810, a^3=78512, a^4=69401, a^5=61358, a^6=54691, a^7=48553, a^8=43415" +
                ", a^9=38254}",
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
                "{1=124658, c=62644, a=62464, b=62432, a*c=31428, c^2=31305, b*c=31261, a^2=31133, b^2=31035" +
                ", a*b=31027}",
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
