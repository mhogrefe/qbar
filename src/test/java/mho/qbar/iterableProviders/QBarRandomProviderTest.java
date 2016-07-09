package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.random.IsaacPRNG;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static mho.qbar.testing.QBarTesting.aeqMapQBarLog;
import static mho.qbar.testing.QBarTesting.aeqitLimitQBarLog;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class QBarRandomProviderTest {
    private static QBarRandomProvider P;
    private static QBarRandomProvider Q;
    private static QBarRandomProvider R;

    @Before
    public void initialize() {
        P = QBarRandomProvider.example();
        Q = new QBarRandomProvider(toList(replicate(IsaacPRNG.SIZE, 0)));
        R = new QBarRandomProvider(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(1, IsaacPRNG.SIZE)));
    }

    @Test
    public void testConstructor() {
        QBarRandomProvider rp = new QBarRandomProvider();
        rp.validate();
        aeq(rp.getScale(), 32);
        aeq(rp.getSecondaryScale(), 8);
        aeq(rp.getTertiaryScale(), 2);
    }

    private static void constructor_List_Integer_helper(@NotNull List<Integer> input, @NotNull String output) {
        QBarRandomProvider rp = new QBarRandomProvider(input);
        rp.validate();
        aeq(rp, output);
    }

    private static void constructor_List_Integer_fail_helper(@NotNull String input) {
        try {
            new QBarRandomProvider(readIntegerListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testConstructor_List_Integer() {
        constructor_List_Integer_helper(toList(replicate(IsaacPRNG.SIZE, 0)),
                "QBarRandomProvider[@-7948823947390831374, 32, 8, 2]");
        constructor_List_Integer_helper(toList(ExhaustiveProvider.INSTANCE.rangeIncreasing(1, IsaacPRNG.SIZE)),
                "QBarRandomProvider[@2449928962525148503, 32, 8, 2]");
        constructor_List_Integer_helper(toList(ExhaustiveProvider.INSTANCE.rangeDecreasing(-IsaacPRNG.SIZE, -1)),
                "QBarRandomProvider[@3417306423260907531, 32, 8, 2]");

        constructor_List_Integer_fail_helper("[]");
        constructor_List_Integer_fail_helper("[1, 2, 3]");
    }

    @Test
    public void testExample() {
        QBarRandomProvider rp = QBarRandomProvider.example();
        rp.validate();
        aeq(rp, "QBarRandomProvider[@-8800290164235921060, 32, 8, 2]");
    }

    private static void getScale_helper(@NotNull QBarRandomProvider rp, int scale) {
        aeq(rp.getScale(), scale);
    }

    private static void getScale_helper(int scale) {
        aeq(new QBarRandomProvider().withScale(scale).getScale(), scale);
    }

    @Test
    public void testGetScale() {
        getScale_helper(P, 32);
        getScale_helper(100);
        getScale_helper(3);
        getScale_helper(-3);
    }

    private static void getSecondaryScale_helper(@NotNull QBarRandomProvider rp, int secondaryScale) {
        aeq(rp.getSecondaryScale(), secondaryScale);
    }

    private static void getSecondaryScale_helper(int secondaryScale) {
        aeq(new QBarRandomProvider().withSecondaryScale(secondaryScale).getSecondaryScale(), secondaryScale);
    }

    @Test
    public void testGetSecondaryScale() {
        getSecondaryScale_helper(P, 8);
        getSecondaryScale_helper(100);
        getSecondaryScale_helper(3);
        getSecondaryScale_helper(-3);
    }

    private static void getTertiaryScale_helper(@NotNull QBarRandomProvider rp, int tertiaryScale) {
        aeq(rp.getTertiaryScale(), tertiaryScale);
    }

    private static void getTertiaryScale_helper(int tertiaryScale) {
        aeq(new QBarRandomProvider().withTertiaryScale(tertiaryScale).getTertiaryScale(), tertiaryScale);
    }

    @Test
    public void testGetTertiaryScale() {
        getTertiaryScale_helper(P, 2);
        getTertiaryScale_helper(100);
        getTertiaryScale_helper(3);
        getTertiaryScale_helper(-3);
    }

    private static void getSeed_helper(@NotNull QBarRandomProvider rp, @NotNull String seed) {
        aeq(rp.getSeed(), seed);
    }

    @Test
    public void testGetSeed() {
        getSeed_helper(
                P,
                "[-1740315277, -1661427768, 842676458, -1268128447, -121858045, 1559496322, -581535260, -1819723670," +
                " -334232530, 244755020, -534964695, 301563516, -1795957210, 1451814771, 1299826235, -666749112," +
                " -1729602324, -565031294, 1897952431, 1118663606, -299718943, -1499922009, -837624734, 1439650052," +
                " 312777359, -1140199484, 688524765, 739702138, 1480517762, 1622590976, 835969782, -204259962," +
                " -606452012, -1671898934, 368548728, -333429570, -1477682221, -638975525, -402896626, 1106834480," +
                " -1454735450, 1532680389, 1878326075, 1597781004, 619389131, -898266263, 1900039432, 1228960795," +
                " 1091764975, -1435988581, 1465994011, -241076337, 980038049, -821307198, -25801148, -1278802989," +
                " -290171171, 1063693093, 1718162965, -297113539, -1723402396, 1063795076, 1779331877, 1606303707," +
                " 1342330210, -2115595746, -718013617, 889248973, 1553964562, -2000156621, 1009070370, 998677106," +
                " 309828058, -816607592, 347096084, -565436493, -1836536982, -39909763, -1384351460, 586300570," +
                " -1545743273, -118730601, -1026888351, -643914920, 159473612, -509882909, 2003784095, -1582123439," +
                " 1199200850, -980627072, 589064158, 1351400003, 1083549876, -1039880174, 1634495699, -1583272739," +
                " 1765688283, -316629870, 577895752, -145082312, -645859550, 1496562313, 1970005163, -104842168," +
                " 285710655, 970623004, 375952155, -1114509491, 9760898, 272385973, 1160942220, 79933456, 642681904," +
                " -1291288677, -238849129, 1196057424, -587416967, -2000013062, 953214572, -2003974223, -179005208," +
                " -1599818904, 1963556499, -1494628627, 293535669, -1033907228, 1690848472, 1958730707, 1679864529," +
                " -450182832, -1398178560, 2092043951, 892850383, 662556689, -1954880564, -1297875796, -562200510," +
                " 1753810661, 612072956, -1182875, 294510681, -485063306, 1608426289, 1466734719, 2978810," +
                " -2134449847, 855495682, -1563923271, -306227772, 147934567, 926758908, 1903257258, 1602676310," +
                " -1151393146, 303067731, -1371065668, 1908028886, -425534720, 1241120323, -2101606174, 545122109," +
                " 1781213901, -146337786, -1205949803, -235261172, 1019855899, -193216104, -1286568040, -294909212," +
                " 1086948319, 1903298288, 2119132684, -581936319, -2070422261, 2086926428, -1303966999, -1365365119," +
                " -1891227288, 346044744, 488440551, -790513873, -2045294651, -1270631847, -2126290563, -1816128137," +
                " 1473769929, 784925032, 292983675, -325413283, -2117417065, 1156099828, -1188576148, -1134724577," +
                " 937972245, -924106996, 1553688888, 324720865, 2001615528, 998833644, 137816765, 1901776632," +
                " 2000206935, 942793606, -1742718537, 1909590681, -1332632806, -1355397404, 152253803, -193623640," +
                " 1601921213, -427930872, 1154642563, 1204629137, 581648332, 1921167008, 2054160403, -1709752639," +
                " -402951456, 1597748885, 351809052, -1039041413, 1958075309, 1071372680, 1249922658, -2077011328," +
                " -2088560037, 643876593, -691661336, 2124992669, -534970427, 1061266818, -1731083093, 195764083," +
                " 1773077546, 304479557, 244603812, 834384133, 1684120407, 1493413139, 1731211584, -2062213553," +
                " -270682579, 44310291, 564559440, 957643125, 1374924466, 962420298, 1319979537, 1206138289," +
                " -948832823, -909756549, -664108386, -1355112330, -125435854, -1502071736, -790593389]"
        );
        getSeed_helper(
                Q,
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0," +
                " 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]"
        );
        getSeed_helper(
                R,
                "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27," +
                " 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51," +
                " 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75," +
                " 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99," +
                " 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118," +
                " 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137," +
                " 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156," +
                " 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175," +
                " 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194," +
                " 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213," +
                " 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232," +
                " 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251," +
                " 252, 253, 254, 255, 256]"
        );
    }

    private static void withScale_helper(@NotNull QBarRandomProvider rp, int scale, @NotNull String output) {
        QBarRandomProvider s = rp.withScale(scale);
        s.validate();
        aeq(s, output);
    }

    @Test
    public void testWithScale() {
        withScale_helper(P, 100, "QBarRandomProvider[@-8800290164235921060, 100, 8, 2]");
        withScale_helper(Q, 3, "QBarRandomProvider[@-7948823947390831374, 3, 8, 2]");
        withScale_helper(R, 0, "QBarRandomProvider[@2449928962525148503, 0, 8, 2]");
        withScale_helper(R, -3, "QBarRandomProvider[@2449928962525148503, -3, 8, 2]");
    }

    private static void withSecondaryScale_helper(
            @NotNull QBarRandomProvider rp,
            int secondaryScale,
            @NotNull String output
    ) {
        QBarRandomProvider s = rp.withSecondaryScale(secondaryScale);
        s.validate();
        aeq(s, output);
    }

    @Test
    public void testWithSecondaryScale() {
        withSecondaryScale_helper(P, 100, "QBarRandomProvider[@-8800290164235921060, 32, 100, 2]");
        withSecondaryScale_helper(Q, 3, "QBarRandomProvider[@-7948823947390831374, 32, 3, 2]");
        withSecondaryScale_helper(R, 0, "QBarRandomProvider[@2449928962525148503, 32, 0, 2]");
        withSecondaryScale_helper(R, -3, "QBarRandomProvider[@2449928962525148503, 32, -3, 2]");
    }

    private static void withTertiaryScale_helper(
            @NotNull QBarRandomProvider rp,
            int tertiaryScale,
            @NotNull String output
    ) {
        QBarRandomProvider s = rp.withTertiaryScale(tertiaryScale);
        s.validate();
        aeq(s, output);
    }

    @Test
    public void testWithTertiaryScale() {
        withTertiaryScale_helper(P, 100, "QBarRandomProvider[@-8800290164235921060, 32, 8, 100]");
        withTertiaryScale_helper(Q, 3, "QBarRandomProvider[@-7948823947390831374, 32, 8, 3]");
        withTertiaryScale_helper(R, 0, "QBarRandomProvider[@2449928962525148503, 32, 8, 0]");
        withTertiaryScale_helper(R, -3, "QBarRandomProvider[@2449928962525148503, 32, 8, -3]");
    }

    @Test
    public void testCopy() {
        QBarRandomProvider copy = P.copy();
        copy.validate();
        assertEquals(P, copy);
        head(P.integers());
        assertEquals(P, copy);
    }

    @Test
    public void testDeepCopy() {
        QBarRandomProvider copy = P.deepCopy();
        copy.validate();
        assertEquals(P, copy);
        head(P.integers());
        assertNotEquals(P, copy);
    }

    @Test
    public void testReset() {
        QBarRandomProvider PDependent = P.withScale(10);
        QBarRandomProvider original = P.deepCopy();
        QBarRandomProvider dependent = original.withScale(10);
        assertEquals(PDependent, dependent);
        head(P.integers());
        assertNotEquals(P, original);
        assertNotEquals(PDependent, dependent);
        P.reset();
        P.validate();
        assertEquals(P, original);
        assertEquals(PDependent, dependent);
    }

    private static void getId_helper(@NotNull QBarRandomProvider rp, long id) {
        aeq(rp.getId(), id);
    }

    @Test
    public void testGetId() {
        getId_helper(P, -8800290164235921060L);
        head(P.integers()); // change P state
        getId_helper(P, -6220528511995005615L);
        getId_helper(Q, -7948823947390831374L);
        getId_helper(R, 2449928962525148503L);
        P.reset();
    }

    private static void rationals_helper(
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
        rationals_helper(P.withScale(meanBitSize).positiveRationals(), output, sampleMean, bitSizeMean);
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
        rationals_helper(P.withScale(meanBitSize).negativeRationals(), output, sampleMean, bitSizeMean);
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
        rationals_helper(P.withScale(meanBitSize).nonzeroRationals(), output, sampleMean, bitSizeMean);
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
        rationals_helper(P.withScale(meanBitSize).rationals(), output, sampleMean, bitSizeMean);
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
        rationals_helper(P.withScale(meanBitSize).nonNegativeRationalsLessThanOne(), output, sampleMean, bitSizeMean);
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
        rationals_helper(P.withScale(scale).rangeUp(Rational.readStrict(a).get()), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void rangeUp_Rational_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rangeUp(Rational.readStrict(a).get());
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
        rationals_helper(P.withScale(scale).rangeDown(Rational.readStrict(a).get()), output, sampleMean, bitSizeMean);
        P.reset();
    }

    private static void rangeDown_Rational_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rangeDown(Rational.readStrict(a).get());
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
        rationals_helper(
                P.withScale(scale).range(Rational.readStrict(a).get(), Rational.readStrict(b).get()),
                output,
                sampleMean,
                bitSizeMean
        );
        P.reset();
    }

    private static void range_Rational_Rational_fail_helper(int scale, @NotNull String a, @NotNull String b) {
        try {
            P.withScale(scale).range(Rational.readStrict(a).get(), Rational.readStrict(b).get());
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

    private static void intervals_helper(@NotNull Iterable<Interval> xs, @NotNull String output, double bitSizeMean) {
        List<Interval> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(Interval::bitLength, sample))), bitSizeMean);
    }

    private static void finitelyBoundedIntervals_helper(int meanBitSize, @NotNull String output, double bitSizeMean) {
        intervals_helper(P.withScale(meanBitSize).finitelyBoundedIntervals(), output, bitSizeMean);
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
        intervals_helper(P.withScale(meanBitSize).intervals(), output, bitSizeMean);
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
        rationals_helper(
                P.withScale(scale).rationalsIn(Interval.readStrict(a).get()),
                output,
                sampleMean,
                bitSizeMean
        );
        P.reset();
    }

    private static void rationalsIn_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rationalsIn(Interval.readStrict(a).get());
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
        rationals_helper(
                P.withScale(scale).rationalsNotIn(Interval.readStrict(a).get()),
                output,
                sampleMean,
                bitSizeMean
        );
        P.reset();
    }

    private static void rationalsNotIn_fail_helper(int scale, @NotNull String a) {
        try {
            P.withScale(scale).rationalsNotIn(Interval.readStrict(a).get());
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
                "QBarRandomProvider_monicPolynomialsAtLeast_i",
                1.0006929999977097,
                0.9168388153632843
        );
        monicPolynomialsAtLeast_helper(
                5,
                3,
                -1,
                "QBarRandomProvider_monicPolynomialsAtLeast_ii",
                3.00101199999147,
                3.9122941895036285
        );
        monicPolynomialsAtLeast_helper(
                5,
                3,
                0,
                "QBarRandomProvider_monicPolynomialsAtLeast_iii",
                3.00101199999147,
                3.9122941895036285
        );
        monicPolynomialsAtLeast_helper(
                5,
                3,
                2,
                "QBarRandomProvider_monicPolynomialsAtLeast_iv",
                3.0005879999687126,
                3.914580056823708
        );
        monicPolynomialsAtLeast_helper(
                10,
                8,
                -1,
                "QBarRandomProvider_monicPolynomialsAtLeast_v",
                7.983070000016452,
                8.931442034672816
        );
        monicPolynomialsAtLeast_helper(
                10,
                8,
                2,
                "QBarRandomProvider_monicPolynomialsAtLeast_vi",
                8.01187900001392,
                8.93072132885421
        );
        monicPolynomialsAtLeast_helper(
                10,
                8,
                7,
                "QBarRandomProvider_monicPolynomialsAtLeast_vii",
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

    private static void monomialHelper(@NotNull Iterable<Monomial> xs, @NotNull String output, double degreeMean) {
        List<Monomial> sample = toList(take(DEFAULT_SAMPLE_SIZE, xs));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(Monomial::degree, sample))), degreeMean);
    }

    private static void monomials_helper(int scale, @NotNull String output, double degreeMean) {
        monomialHelper(P.withScale(scale).monomials(), output, degreeMean);
        P.reset();
    }

    private static void monomials_fail_helper(int scale) {
        try {
            P.withScale(scale).monomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonomials() {
        monomials_helper(1, "QBarRandomProvider_monomials_i", 1.0010019999980002);
        monomials_helper(8, "QBarRandomProvider_monomials_ii", 24.01019300000956);
        monomials_helper(32, "QBarRandomProvider_monomials_iii", 192.2389910000015);

        monomials_fail_helper(0);
        monomials_fail_helper(-1);
    }

    private static void monomials_List_Variable_helper(
            int scale,
            @NotNull String variables,
            @NotNull String output,
            double degreeMean
    ) {
        monomialHelper(P.withScale(scale).monomials(readVariableList(variables)), output, degreeMean);
        P.reset();
    }

    private static void monomials_List_Variable_fail_helper(int scale, @NotNull String variables) {
        try {
            P.withScale(scale).monomials(readVariableListWithNulls(variables));
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testMonomials_List_Variable() {
        monomials_List_Variable_helper(1, "[]", "QBarRandomProvider_monomials_List_Variable_i", 0.0);
        monomials_List_Variable_helper(8, "[]", "QBarRandomProvider_monomials_List_Variable_ii", 0.0);
        monomials_List_Variable_helper(1, "[a]", "QBarRandomProvider_monomials_List_Variable_iii", 1.0008359999977228);
        monomials_List_Variable_helper(8, "[a]", "QBarRandomProvider_monomials_List_Variable_iv", 7.996049000016875);
        monomials_List_Variable_helper(
                1,
                "[x, y]",
                "QBarRandomProvider_monomials_List_Variable_v",
                1.9999819999876247
        );
        monomials_List_Variable_helper(
                8,
                "[x, y]",
                "QBarRandomProvider_monomials_List_Variable_vi",
                15.985479999996784
        );
        monomials_List_Variable_helper(
                1,
                "[a, b, c]",
                "QBarRandomProvider_monomials_List_Variable_vii",
                3.0025289999869127
        );
        monomials_List_Variable_helper(
                8,
                "[a, b, c]",
                "QBarRandomProvider_monomials_List_Variable_viii",
                23.976022000016762
        );

        monomials_List_Variable_fail_helper(0, "[a, b]");
        monomials_List_Variable_fail_helper(-1, "[a, b]");
        monomials_List_Variable_fail_helper(1, "[a, a]");
        monomials_List_Variable_fail_helper(1, "[b, a]");
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

    private static void rationalMultivariatePolynomials_helper(
            @NotNull Iterable<RationalMultivariatePolynomial> input,
            @NotNull String output,
            double meanTermCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        List<RationalMultivariatePolynomial> sample = toList(take(DEFAULT_SAMPLE_SIZE / 10, input));
        aeqitLimitQBarLog(TINY_LIMIT, sample, output);
        aeqMapQBarLog(topSampleCount(DEFAULT_TOP_COUNT, sample), output);
        aeq(meanOfIntegers(toList(map(RationalMultivariatePolynomial::termCount, sample))), meanTermCount);
        aeq(meanOfIntegers(toList(map(RationalMultivariatePolynomial::degree, sample))), meanDegree);
        aeq(meanOfIntegers(toList(concatMap(ts -> map(t -> t.b.bitLength(), ts), sample))), meanCoefficientBitSize);
        P.reset();
    }

    private static void rationalMultivariatePolynomials_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output,
            double meanTermCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalMultivariatePolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale).
                        rationalMultivariatePolynomials(),
                output,
                meanTermCount,
                meanDegree,
                meanCoefficientBitSize
        );
        P.reset();
    }

    private static void rationalMultivariatePolynomials_fail_helper(int scale, int secondaryScale, int tertiaryScale) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .rationalMultivariatePolynomials();
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalMultivariatePolynomials() {
        rationalMultivariatePolynomials_helper(
                4,
                1,
                2,
                "QBarRandomProvider_rationalMultivariatePolynomials_i",
                1.0861099999993993,
                1.015780000000281,
                3.7767905644876443
        );
        rationalMultivariatePolynomials_helper(
                5,
                4,
                3,
                "QBarRandomProvider_rationalMultivariatePolynomials_ii",
                1.9657200000000141,
                12.086050000003906,
                4.7071556477973235
        );
        rationalMultivariatePolynomials_helper(
                10,
                5,
                8,
                "QBarRandomProvider_rationalMultivariatePolynomials_iii",
                5.694160000000104,
                38.12063999999753,
                9.361257850138248
        );

        rationalMultivariatePolynomials_fail_helper(4, 1, 1);
        rationalMultivariatePolynomials_fail_helper(4, 0, 2);
        rationalMultivariatePolynomials_fail_helper(3, 1, 2);
    }

    private static void rationalMultivariatePolynomials_List_Variable_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String variables,
            @NotNull String output,
            double meanTermCount,
            double meanDegree,
            double meanCoefficientBitSize
    ) {
        rationalMultivariatePolynomials_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                        .rationalMultivariatePolynomials(readVariableList(variables)),
                output,
                meanTermCount,
                meanDegree,
                meanCoefficientBitSize
        );
        P.reset();
    }

    private static void rationalMultivariatePolynomials_List_Variable_fail_helper(
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String variables
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale)
                    .rationalMultivariatePolynomials(readVariableListWithNulls(variables));
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRationalMultivariatePolynomials_List_Variable() {
        rationalMultivariatePolynomials_List_Variable_helper(
                4,
                1,
                2,
                "[]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_i",
                0.7762399999991021,
                -0.223760000000083,
                4.693393795734666
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_ii",
                0.9491599999983151,
                -0.050840000000004895,
                10.344314973240962
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                4,
                1,
                2,
                "[a]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_iii",
                1.1443799999995272,
                0.8916500000001601,
                3.783070308818268
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[a]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_iv",
                5.386610000000372,
                19.68029000000129,
                9.360677680395968
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                4,
                1,
                2,
                "[x, y]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_v",
                1.3659099999998103,
                1.8545599999994904,
                3.7687988227625344
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[x, y]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_vi",
                7.060319999999874,
                32.140500000003314,
                9.343324381901137
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                4,
                1,
                2,
                "[a, b, c]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_vii",
                1.4471499999998754,
                2.7178799999996937,
                3.7768372317956787
        );
        rationalMultivariatePolynomials_List_Variable_helper(
                10,
                9,
                8,
                "[a, b, c]",
                "QBarRandomProvider_rationalMultivariatePolynomials_List_Variable_viii",
                7.183649999999779,
                43.50401999999417,
                9.338188803713221
        );

        rationalMultivariatePolynomials_List_Variable_fail_helper(3, 1, 2, "[]");
        rationalMultivariatePolynomials_List_Variable_fail_helper(4, 0, 2, "[]");
        rationalMultivariatePolynomials_List_Variable_fail_helper(4, 1, 1, "[]");
        rationalMultivariatePolynomials_List_Variable_fail_helper(3, 1, 2, "[a]");
        rationalMultivariatePolynomials_List_Variable_fail_helper(4, 0, 2, "[a]");
        rationalMultivariatePolynomials_List_Variable_fail_helper(4, 1, 1, "[a]");
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

    private static void rangeUp_int_Algebraic_helper(
            int scale,
            int degree,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).rangeUp(degree, Algebraic.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void rangeUp_int_Algebraic_fail_helper(int scale, int degree, @NotNull String a) {
        try {
            P.withScale(scale).rangeUp(degree, Algebraic.readStrict(a).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRangeUp_int_Algebraic() {
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "0",
                "QBarRandomProvider_rangeUp_int_Algebraic_i",
                0.9999999999999062,
                1.498200000000242,
                84.23177467163389
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "0",
                "QBarRandomProvider_rangeUp_int_Algebraic_ii",
                1.9999999999998124,
                3.7781333333344684,
                5709398.069194358
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "0",
                "QBarRandomProvider_rangeUp_int_Algebraic_iii",
                3.0000000000004805,
                5.455049999999257,
                8.64470511038894E8
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "1",
                "QBarRandomProvider_rangeUp_int_Algebraic_iv",
                0.9999999999999062,
                1.974550000000421,
                85.23177467164439
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "1",
                "QBarRandomProvider_rangeUp_int_Algebraic_v",
                1.9999999999998124,
                5.689966666668448,
                5709399.06919433
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "1",
                "QBarRandomProvider_rangeUp_int_Algebraic_vi",
                3.0000000000004805,
                9.48944999999995,
                8.644705120388933E8
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "1/2",
                "QBarRandomProvider_rangeUp_int_Algebraic_vii",
                0.9999999999999062,
                2.4287000000003247,
                84.73177467163964
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "1/2",
                "QBarRandomProvider_rangeUp_int_Algebraic_viii",
                1.9999999999998124,
                6.690066666667999,
                5709398.569194343
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "1/2",
                "QBarRandomProvider_rangeUp_int_Algebraic_ix",
                3.0000000000004805,
                11.05069999999989,
                8.644705115388935E8
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "-4/3",
                "QBarRandomProvider_rangeUp_int_Algebraic_x",
                0.9999999999999062,
                3.3127500000009356,
                82.8984413382926
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "-4/3",
                "QBarRandomProvider_rangeUp_int_Algebraic_xi",
                1.9999999999998124,
                8.398400000001752,
                5709396.735860994
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "-4/3",
                "QBarRandomProvider_rangeUp_int_Algebraic_xii",
                3.0000000000004805,
                13.853600000000249,
                8.644705097055638E8
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "sqrt(2)",
                "QBarRandomProvider_rangeUp_int_Algebraic_xiii",
                0.9999999999999062,
                2.700500000000383,
                3807.162064531441
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "sqrt(2)",
                "QBarRandomProvider_rangeUp_int_Algebraic_xiv",
                1.9999999999998124,
                4.696600000001639,
                5739586.425742524
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "sqrt(2)",
                "QBarRandomProvider_rangeUp_int_Algebraic_xv",
                3.0000000000004805,
                8.071624999999141,
                1.3715720110823574E9
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "-sqrt(2)",
                "QBarRandomProvider_rangeUp_int_Algebraic_xvi",
                0.9999999999999062,
                2.284850000000405,
                3824.9293955742887
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "-sqrt(2)",
                "QBarRandomProvider_rangeUp_int_Algebraic_xvii",
                1.9999999999998124,
                4.944666666668346,
                5739661.069661236
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "-sqrt(2)",
                "QBarRandomProvider_rangeUp_int_Algebraic_xviii",
                3.0000000000004805,
                8.614774999999732,
                1.6093864688990707E9
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeUp_int_Algebraic_xix",
                0.9999999999999062,
                2.695050000000318,
                3830.093024294524
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeUp_int_Algebraic_xx",
                1.9999999999998124,
                4.718900000001644,
                5739665.9549176125
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeUp_int_Algebraic_xxi",
                3.0000000000004805,
                8.059499999999113,
                1.6095571599144523E9
        );
        rangeUp_int_Algebraic_helper(
                2,
                1,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeUp_int_Algebraic_xxii",
                0.9999999999999062,
                2.7718500000004407,
                3672.795142000348
        );
        rangeUp_int_Algebraic_helper(
                3,
                2,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeUp_int_Algebraic_xxiii",
                1.9999999999998124,
                4.806933333334986,
                5718091.368998707
        );
        rangeUp_int_Algebraic_helper(
                5,
                3,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeUp_int_Algebraic_xxiv",
                3.0000000000004805,
                8.231474999999374,
                1.3633367415551941E9
        );

        rangeUp_int_Algebraic_fail_helper(1, 1, "0");
        rangeUp_int_Algebraic_fail_helper(1, 1, "sqrt(2)");
        rangeUp_int_Algebraic_fail_helper(2, 0, "0");
        rangeUp_int_Algebraic_fail_helper(2, 0, "sqrt(2)");
    }

    private static void rangeUp_Algebraic_helper(
            int scale,
            int secondaryScale,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rangeUp(Algebraic.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void rangeUp_Algebraic_fail_helper(int scale, int secondaryScale, @NotNull String a) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rangeUp(Algebraic.readStrict(a).get());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRangeUp_Algebraic() {
        rangeUp_Algebraic_helper(
                2,
                4,
                "0",
                "QBarRandomProvider_rangeUp_Algebraic_i",
                1.4891999999998753,
                1.689739675397475,
                3191.303321396886
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "0",
                "QBarRandomProvider_rangeUp_Algebraic_ii",
                2.6172000000001407,
                4.833185889638937,
                4.154123027551921E8
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "1",
                "QBarRandomProvider_rangeUp_Algebraic_iii",
                1.4891999999998753,
                2.893821308050309,
                3192.3033213971453
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "1",
                "QBarRandomProvider_rangeUp_Algebraic_iv",
                2.6172000000001407,
                9.624654428840733,
                4.154123037551911E8
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "1/2",
                "QBarRandomProvider_rangeUp_Algebraic_v",
                1.4891999999998753,
                3.9683834163580003,
                3191.8033213969998
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "1/2",
                "QBarRandomProvider_rangeUp_Algebraic_vi",
                2.6172000000001407,
                12.08664160123919,
                4.1541230325520384E8
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "-4/3",
                "QBarRandomProvider_rangeUp_Algebraic_vii",
                1.4891999999998753,
                5.89944560501263,
                3189.9699880633475
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "-4/3",
                "QBarRandomProvider_rangeUp_Algebraic_viii",
                2.6172000000001407,
                16.10723764237551,
                4.1541230142185754E8
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "sqrt(2)",
                "QBarRandomProvider_rangeUp_Algebraic_ix",
                2.0328999999998527,
                3.920010550957848,
                3528.9139334533047
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "sqrt(2)",
                "QBarRandomProvider_rangeUp_Algebraic_x",
                3.1214000000002127,
                9.67588683457136,
                1.3532927609576285E11
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "-sqrt(2)",
                "QBarRandomProvider_rangeUp_Algebraic_xi",
                2.016599999999843,
                4.322813763841376,
                3629.1717532191783
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "-sqrt(2)",
                "QBarRandomProvider_rangeUp_Algebraic_xii",
                3.085800000000207,
                10.759826716921934,
                1.3532928229945216E11
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeUp_Algebraic_xiii",
                2.0126999999998416,
                3.8373551963361314,
                3633.8491446396874
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeUp_Algebraic_xiv",
                3.080400000000204,
                9.589427507107784,
                1.4353376257335956E11
        );
        rangeUp_Algebraic_helper(
                2,
                4,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeUp_Algebraic_xv",
                2.02979999999985,
                4.085781239685819,
                3487.0888234543086
        );
        rangeUp_Algebraic_helper(
                5,
                6,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeUp_Algebraic_xvi",
                3.1316000000002244,
                9.809105431308584,
                1.353236136770036E11
        );

        rangeUp_Algebraic_fail_helper(1, 4, "0");
        rangeUp_Algebraic_fail_helper(2, 3, "0");
        rangeUp_Algebraic_fail_helper(1, 4, "sqrt(2)");
        rangeUp_Algebraic_fail_helper(2, 3, "sqrt(2)");
    }

    private static void rangeDown_int_Algebraic_helper(
            int scale,
            int degree,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).rangeDown(degree, Algebraic.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void rangeDown_int_Algebraic_fail_helper(int scale, int degree, @NotNull String a) {
        try {
            P.withScale(scale).rangeDown(degree, Algebraic.readStrict(a).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRangeDown_int_Algebraic() {
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "0",
                "QBarRandomProvider_rangeDown_int_Algebraic_i",
                0.9999999999999062,
                1.633100000000353,
                -84.23177467163389
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "0",
                "QBarRandomProvider_rangeDown_int_Algebraic_ii",
                1.9999999999998124,
                3.782733333334475,
                -5709398.069194358
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "0",
                "QBarRandomProvider_rangeDown_int_Algebraic_iii",
                3.0000000000004805,
                5.495149999999198,
                -8.64470511038894E8
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "1",
                "QBarRandomProvider_rangeDown_int_Algebraic_iv",
                0.9999999999999062,
                1.7891500000003526,
                -83.2317746716207
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "1",
                "QBarRandomProvider_rangeDown_int_Algebraic_v",
                1.9999999999998124,
                5.551400000001746,
                -5709397.069194312
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "1",
                "QBarRandomProvider_rangeDown_int_Algebraic_vi",
                3.0000000000004805,
                9.368149999999867,
                -8.644705100388921E8
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "1/2",
                "QBarRandomProvider_rangeDown_int_Algebraic_vii",
                0.9999999999999062,
                2.2382500000001997,
                -83.73177467162557
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "1/2",
                "QBarRandomProvider_rangeDown_int_Algebraic_viii",
                1.9999999999998124,
                6.572900000001334,
                -5709397.569194325
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "1/2",
                "QBarRandomProvider_rangeDown_int_Algebraic_ix",
                3.0000000000004805,
                10.9421249999999,
                -8.644705105388954E8
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "-4/3",
                "QBarRandomProvider_rangeDown_int_Algebraic_x",
                0.9999999999999062,
                3.5817000000010797,
                -85.5651080049707
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "-4/3",
                "QBarRandomProvider_rangeDown_int_Algebraic_xi",
                1.9999999999998124,
                8.549900000001749,
                -5709399.402527662
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "-4/3",
                "QBarRandomProvider_rangeDown_int_Algebraic_xii",
                3.0000000000004805,
                13.986050000000267,
                -8.644705123722265E8
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "sqrt(2)",
                "QBarRandomProvider_rangeDown_int_Algebraic_xiii",
                0.9999999999999062,
                2.22240000000036,
                -3824.9293955742887
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "sqrt(2)",
                "QBarRandomProvider_rangeDown_int_Algebraic_xiv",
                1.9999999999998124,
                4.905833333335024,
                -5739661.069661236
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "sqrt(2)",
                "QBarRandomProvider_rangeDown_int_Algebraic_xv",
                3.0000000000004805,
                8.609424999999735,
                -1.6093864688990707E9
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "-sqrt(2)",
                "QBarRandomProvider_rangeDown_int_Algebraic_xvi",
                0.9999999999999062,
                2.8637000000004535,
                -3807.162064531441
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "-sqrt(2)",
                "QBarRandomProvider_rangeDown_int_Algebraic_xvii",
                1.9999999999998124,
                4.748766666668305,
                -5739586.425742524
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "-sqrt(2)",
                "QBarRandomProvider_rangeDown_int_Algebraic_xviii",
                3.0000000000004805,
                8.092699999999128,
                -1.3715720110823574E9
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeDown_int_Algebraic_xix",
                0.9999999999999062,
                2.2823000000003493,
                -3801.7716342246304
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeDown_int_Algebraic_xx",
                1.9999999999998124,
                4.942100000001704,
                -5734727.37444215
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeDown_int_Algebraic_xxi",
                3.0000000000004805,
                8.680574999999886,
                -1.3711390150556831E9
        );
        rangeDown_int_Algebraic_helper(
                2,
                1,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeDown_int_Algebraic_xxii",
                0.9999999999999062,
                2.180350000000384,
                -3830.6867365177854
        );
        rangeDown_int_Algebraic_helper(
                3,
                2,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeDown_int_Algebraic_xxiii",
                1.9999999999998124,
                4.913166666668392,
                -5739964.136815155
        );
        rangeDown_int_Algebraic_helper(
                5,
                3,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeDown_int_Algebraic_xxiv",
                3.0000000000004805,
                8.61179999999959,
                -1.6096137229755445E9
        );

        rangeDown_int_Algebraic_fail_helper(1, 1, "0");
        rangeDown_int_Algebraic_fail_helper(1, 1, "sqrt(2)");
        rangeDown_int_Algebraic_fail_helper(2, 0, "0");
        rangeDown_int_Algebraic_fail_helper(2, 0, "sqrt(2)");
    }

    private static void rangeDown_Algebraic_helper(
            int scale,
            int secondaryScale,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).rangeDown(Algebraic.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void rangeDown_Algebraic_fail_helper(int scale, int secondaryScale, @NotNull String a) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).rangeDown(Algebraic.readStrict(a).get());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRangeDown_Algebraic() {
        rangeDown_Algebraic_helper(
                2,
                4,
                "0",
                "QBarRandomProvider_rangeDown_Algebraic_i",
                1.4891999999998753,
                1.7534950988266715,
                -3191.303321396886
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "0",
                "QBarRandomProvider_rangeDown_Algebraic_ii",
                2.6172000000001407,
                4.8656695786801984,
                -4.154123027551921E8
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "1",
                "QBarRandomProvider_rangeDown_Algebraic_iii",
                1.4891999999998753,
                2.7616503294226673,
                -3190.303321396575
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "1",
                "QBarRandomProvider_rangeDown_Algebraic_iv",
                2.6172000000001407,
                9.509123078625043,
                -4.1541230175519615E8
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "1/2",
                "QBarRandomProvider_rangeDown_Algebraic_v",
                1.4891999999998753,
                3.8475012052058495,
                -3190.8033213967096
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "1/2",
                "QBarRandomProvider_rangeDown_Algebraic_vi",
                2.6172000000001407,
                11.991512772310747,
                -4.154123022551839E8
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "-4/3",
                "QBarRandomProvider_rangeDown_Algebraic_vii",
                1.4891999999998753,
                6.081954041458041,
                -3192.6366547303555
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "-4/3",
                "QBarRandomProvider_rangeDown_Algebraic_viii",
                2.6172000000001407,
                16.23261085922862,
                -4.1541230408852875E8
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "sqrt(2)",
                "QBarRandomProvider_rangeDown_Algebraic_ix",
                2.016599999999843,
                4.28041503679756,
                -3629.1717532191783
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "sqrt(2)",
                "QBarRandomProvider_rangeDown_Algebraic_x",
                3.085800000000207,
                10.751701013265372,
                -1.3532928229945216E11
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "-sqrt(2)",
                "QBarRandomProvider_rangeDown_Algebraic_xi",
                2.0328999999998527,
                4.011704968841656,
                -3528.9139334533047
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "-sqrt(2)",
                "QBarRandomProvider_rangeDown_Algebraic_xii",
                3.1214000000002127,
                9.697602756345448,
                -1.3532927609576285E11
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeDown_Algebraic_xiii",
                2.0332999999998567,
                4.450037912504573,
                -3524.858940776642
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "(1+sqrt(5))/2",
                "QBarRandomProvider_rangeDown_Algebraic_xiv",
                3.1247000000002156,
                10.93054040294162,
                -1.3532927125837888E11
        );
        rangeDown_Algebraic_helper(
                2,
                4,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeDown_Algebraic_xv",
                1.972999999999836,
                4.144904137235691,
                -3649.7593267785173
        );
        rangeDown_Algebraic_helper(
                5,
                6,
                "root 0 of x^5-x-1",
                "QBarRandomProvider_rangeDown_Algebraic_xvi",
                3.0208000000002033,
                10.588539594109465,
                -1.4353474295949368E11
        );

        rangeDown_Algebraic_fail_helper(1, 4, "0");
        rangeDown_Algebraic_fail_helper(2, 3, "0");
        rangeDown_Algebraic_fail_helper(1, 4, "sqrt(2)");
        rangeDown_Algebraic_fail_helper(2, 3, "sqrt(2)");
    }

    private static void range_int_Algebraic_Algebraic_helper(
            int scale,
            int degree,
            @NotNull String a,
            @NotNull String b,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).range(degree, Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void range_int_Algebraic_Algebraic_fail_helper(
            int scale,
            int degree,
            @NotNull String a,
            @NotNull String b
    ) {
        try {
            P.withScale(scale).range(degree, Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRange_int_Algebraic_Algebraic() {
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "0",
                "0",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_i",
                0.9999999999999062,
                0.4999999999999531,
                0.0
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "1",
                "1",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_ii",
                0.9999999999999062,
                0.4999999999999531,
                0.9999999999999062
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "sqrt(2)",
                "sqrt(2)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_iii",
                1.9999999999998124,
                0.666666666666603,
                1.4142135623732863
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "1",
                "2",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_iv",
                0.9999999999999062,
                1.7716500000005564,
                1.5714904365556832
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "1",
                "2",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_v",
                1.9999999999998124,
                6.464100000001767,
                1.2636062267064716
        );
        range_int_Algebraic_Algebraic_helper(
                5,
                3,
                "1",
                "2",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_vi",
                3.0000000000004805,
                10.745325000000308,
                1.2333289111213646
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "-4/3",
                "1/2",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_vii",
                0.9999999999999062,
                2.6329000000004843,
                -0.2856008663144127
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "-4/3",
                "1/2",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_viii",
                1.9999999999998124,
                11.052333333334872,
                -0.8500552510381606
        );
        range_int_Algebraic_Algebraic_helper(
                5,
                3,
                "-4/3",
                "1/2",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_ix",
                3.0000000000004805,
                18.012824999999765,
                -0.905563662944164
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "1",
                "sqrt(2)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_x",
                0.9999999999999062,
                3.010250000000132,
                1.0624788662940414
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "1",
                "sqrt(2)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xi",
                1.9999999999998124,
                8.034600000001536,
                1.117936264881995
        );
        range_int_Algebraic_Algebraic_helper(
                5,
                3,
                "1",
                "sqrt(2)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xii",
                3.0000000000004805,
                13.112225000000253,
                1.1009430656713397
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "sqrt(2)",
                "sqrt(3)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xiii",
                0.9999999999999062,
                5.082900000000749,
                1.514070630423939
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "sqrt(2)",
                "sqrt(3)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xiv",
                1.9999999999998124,
                9.999966666668053,
                1.529534444697405
        );
        range_int_Algebraic_Algebraic_helper(
                5,
                3,
                "sqrt(2)",
                "sqrt(3)",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xv",
                3.0000000000004805,
                16.432500000000115,
                1.5308866474552778
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "0",
                "256",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xvi",
                0.9999999999999062,
                3.929049999999635,
                146.30155175827122
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "0",
                "256",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xvii",
                1.9999999999998124,
                10.698566666667713,
                67.48319403685356
        );
        range_int_Algebraic_Algebraic_helper(
                5,
                3,
                "0",
                "256",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xviii",
                3.0000000000004805,
                15.777650000001412,
                59.732201247069284
        );
        range_int_Algebraic_Algebraic_helper(
                2,
                1,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xix",
                0.9999999999999062,
                53.53240000001064,
                1.4142135623732863
        );
        range_int_Algebraic_Algebraic_helper(
                3,
                2,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xx",
                1.9999999999998124,
                110.2035666666688,
                1.4142135623732863
        );
        range_int_Algebraic_Algebraic_helper(
                5,
                3,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarRandomProvider_range_int_Algebraic_Algebraic_xxi",
                3.0000000000004805,
                166.45632500001085,
                1.4142135623732863
        );

        range_int_Algebraic_Algebraic_fail_helper(1, 1, "0", "0");
        range_int_Algebraic_Algebraic_fail_helper(1, 1, "0", "1");
        range_int_Algebraic_Algebraic_fail_helper(2, 0, "0", "0");
        range_int_Algebraic_Algebraic_fail_helper(2, 0, "0", "1");
        range_int_Algebraic_Algebraic_fail_helper(2, 1, "sqrt(2)", "sqrt(2)");
        range_int_Algebraic_Algebraic_fail_helper(2, 2, "1", "1");
        range_int_Algebraic_Algebraic_fail_helper(2, 1, "1", "0");
        range_int_Algebraic_Algebraic_fail_helper(2, 1, "6369051672525773/4503599627370496", "sqrt(2)");
    }

    private static void range_Algebraic_Algebraic_helper(
            int scale,
            int secondaryScale,
            @NotNull String a,
            @NotNull String b,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale)
                        .range(Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void range_Algebraic_Algebraic_fail_helper(
            int scale,
            int secondaryScale,
            @NotNull String a,
            @NotNull String b
    ) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale)
                    .range(Algebraic.readStrict(a).get(), Algebraic.readStrict(b).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testRange_Algebraic_Algebraic() {
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "0",
                "0",
                "QBarRandomProvider_range_Algebraic_Algebraic_i",
                0.9999999999999062,
                0.4999999999999531,
                0.0
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "0",
                "0",
                "QBarRandomProvider_range_Algebraic_Algebraic_ii",
                0.9999999999999062,
                0.4999999999999531,
                0.0
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "1",
                "1",
                "QBarRandomProvider_range_Algebraic_Algebraic_iii",
                0.9999999999999062,
                0.4999999999999531,
                0.9999999999999062
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "1",
                "1",
                "QBarRandomProvider_range_Algebraic_Algebraic_iv",
                0.9999999999999062,
                0.4999999999999531,
                0.9999999999999062
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "sqrt(2)",
                "sqrt(2)",
                "QBarRandomProvider_range_Algebraic_Algebraic_v",
                1.9999999999998124,
                0.666666666666603,
                1.4142135623732863
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "sqrt(2)",
                "sqrt(2)",
                "QBarRandomProvider_range_Algebraic_Algebraic_vi",
                1.9999999999998124,
                0.666666666666603,
                1.4142135623732863
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "1",
                "2",
                "QBarRandomProvider_range_Algebraic_Algebraic_vii",
                1.489099999999876,
                3.1445100638773686,
                1.6347287727960884
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "1",
                "2",
                "QBarRandomProvider_range_Algebraic_Algebraic_viii",
                2.588900000000133,
                10.358967928892671,
                1.3651357582742665
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "-4/3",
                "1/2",
                "QBarRandomProvider_range_Algebraic_Algebraic_ix",
                1.489099999999876,
                6.948053513316408,
                -0.16966391654034724
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "-4/3",
                "1/2",
                "QBarRandomProvider_range_Algebraic_Algebraic_x",
                2.588900000000133,
                20.455459890217735,
                -0.6639177764971208
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "1",
                "sqrt(2)",
                "QBarRandomProvider_range_Algebraic_Algebraic_xi",
                1.966899999999836,
                6.640972058377491,
                1.1153223254680313
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "1",
                "sqrt(2)",
                "QBarRandomProvider_range_Algebraic_Algebraic_xii",
                2.9583000000001802,
                15.061996311547738,
                1.0897817689953515
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "sqrt(2)",
                "sqrt(3)",
                "QBarRandomProvider_range_Algebraic_Algebraic_xiii",
                2.4251000000000182,
                13.350500715312839,
                1.538251090003969
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "sqrt(2)",
                "sqrt(3)",
                "QBarRandomProvider_range_Algebraic_Algebraic_xiv",
                3.460500000000252,
                24.696894966928625,
                1.529109887409632
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "0",
                "256",
                "QBarRandomProvider_range_Algebraic_Algebraic_xv",
                1.489099999999876,
                7.831344662729657,
                162.49056583581194
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "0",
                "256",
                "QBarRandomProvider_range_Algebraic_Algebraic_xvi",
                2.588900000000133,
                18.526094346457267,
                93.47475411821799
        );
        range_Algebraic_Algebraic_helper(
                2,
                4,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarRandomProvider_range_Algebraic_Algebraic_xvii",
                1.4997999999998755,
                108.51020081602859,
                1.4142135623732863
        );
        range_Algebraic_Algebraic_helper(
                5,
                6,
                "sqrt(2)",
                "6369051672525773/4503599627370496",
                "QBarRandomProvider_range_Algebraic_Algebraic_xviii",
                2.5786000000001814,
                232.16358352426488,
                1.4142135623732863
        );

        range_Algebraic_Algebraic_fail_helper(1, 4, "0", "1");
        range_Algebraic_Algebraic_fail_helper(2, 3, "0", "1");
        range_Algebraic_Algebraic_fail_helper(1, 4, "0", "0");
        range_Algebraic_Algebraic_fail_helper(2, 3, "0", "0");
        range_Algebraic_Algebraic_fail_helper(2, 4, "1", "0");
        range_Algebraic_Algebraic_fail_helper(2, 4, "1/2", "1/3");
        range_Algebraic_Algebraic_fail_helper(2, 4, "6369051672525773/4503599627370496", "sqrt(2)");
    }

    private static void algebraicsIn_int_Interval_helper(
            int scale,
            int degree,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).algebraicsIn(degree, Interval.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void algebraicsIn_int_Interval_fail_helper(int scale, int degree, @NotNull String a) {
        try {
            P.withScale(scale).algebraicsIn(degree, Interval.readStrict(a).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testAlgebraicsIn_int_Interval() {
        algebraicsIn_int_Interval_helper(
                2,
                1,
                "[0, 0]",
                "QBarRandomProvider_algebraicsIn_int_Interval_i",
                0.9999999999999062,
                0.4999999999999531,
                0.0
        );
        algebraicsIn_int_Interval_helper(
                2,
                1,
                "[1, 1]",
                "QBarRandomProvider_algebraicsIn_int_Interval_ii",
                0.9999999999999062,
                0.4999999999999531,
                0.9999999999999062
        );
        algebraicsIn_int_Interval_helper(
                2,
                1,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_algebraicsIn_int_Interval_iii",
                0.9999999999999062,
                2.2974500000003526,
                39.69600603361856
        );
        algebraicsIn_int_Interval_helper(
                3,
                2,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_algebraicsIn_int_Interval_iv",
                1.9999999999998124,
                3.7552333333344534,
                52184.05751035433
        );
        algebraicsIn_int_Interval_helper(
                5,
                3,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_algebraicsIn_int_Interval_v",
                3.0000000000004805,
                5.427124999999181,
                -3.996771507650188E9
        );
        algebraicsIn_int_Interval_helper(
                2,
                1,
                "[1, 4]",
                "QBarRandomProvider_algebraicsIn_int_Interval_vi",
                0.9999999999999062,
                1.9656500000003794,
                2.7144713096673136
        );
        algebraicsIn_int_Interval_helper(
                3,
                2,
                "[1, 4]",
                "QBarRandomProvider_algebraicsIn_int_Interval_vii",
                1.9999999999998124,
                6.610233333335171,
                1.7908186801193844
        );
        algebraicsIn_int_Interval_helper(
                5,
                3,
                "[1, 4]",
                "QBarRandomProvider_algebraicsIn_int_Interval_viii",
                3.0000000000004805,
                11.086425000000498,
                1.6999867333640908
        );
        algebraicsIn_int_Interval_helper(
                2,
                1,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsIn_int_Interval_ix",
                0.9999999999999062,
                2.2382500000001997,
                -83.73177467162557
        );
        algebraicsIn_int_Interval_helper(
                3,
                2,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsIn_int_Interval_x",
                1.9999999999998124,
                6.572900000001334,
                -5709397.569194325
        );
        algebraicsIn_int_Interval_helper(
                5,
                3,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsIn_int_Interval_xi",
                3.0000000000004805,
                10.9421249999999,
                -8.644705105388954E8
        );
        algebraicsIn_int_Interval_helper(
                2,
                1,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsIn_int_Interval_xii",
                0.9999999999999062,
                2.4287000000003247,
                84.73177467163964
        );
        algebraicsIn_int_Interval_helper(
                3,
                2,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsIn_int_Interval_xiii",
                1.9999999999998124,
                6.690066666667999,
                5709398.569194343
        );
        algebraicsIn_int_Interval_helper(
                5,
                3,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsIn_int_Interval_xiv",
                3.0000000000004805,
                11.05069999999989,
                8.644705115388935E8
        );

        algebraicsIn_int_Interval_fail_helper(1, 1, "[0, 0]");
        algebraicsIn_int_Interval_fail_helper(1, 1, "[1, 2]");
        algebraicsIn_int_Interval_fail_helper(2, 0, "[1, 2]");
        algebraicsIn_int_Interval_fail_helper(1, 1, "(-Infinity, 1/2]");
        algebraicsIn_int_Interval_fail_helper(2, 0, "(-Infinity, 1/2]");
        algebraicsIn_int_Interval_fail_helper(1, 1, "[1/2, Infinity)");
        algebraicsIn_int_Interval_fail_helper(2, 0, "[1/2, Infinity)");
        algebraicsIn_int_Interval_fail_helper(1, 1, "(-Infinity, Infinity)");
        algebraicsIn_int_Interval_fail_helper(2, 0, "(-Infinity, Infinity)");
        algebraicsIn_int_Interval_fail_helper(2, 2, "[1, 1]");
    }

    private static void algebraicsIn_Interval_helper(
            int scale,
            int secondaryScale,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).algebraicsIn(Interval.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void algebraicsIn_Interval_fail_helper(int scale, int secondaryScale, @NotNull String a) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).algebraicsIn(Interval.readStrict(a).get());
            fail();
        } catch (IllegalStateException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testAlgebraicsIn_Interval() {
        algebraicsIn_Interval_helper(
                2,
                4,
                "[0, 0]",
                "QBarRandomProvider_algebraicsIn_Interval_i",
                0.9999999999999062,
                0.4999999999999531,
                0.0
        );
        algebraicsIn_Interval_helper(
                5,
                6,
                "[0, 0]",
                "QBarRandomProvider_algebraicsIn_Interval_ii",
                0.9999999999999062,
                0.4999999999999531,
                0.0
        );
        algebraicsIn_Interval_helper(
                2,
                4,
                "[1, 1]",
                "QBarRandomProvider_algebraicsIn_Interval_iii",
                0.9999999999999062,
                0.4999999999999531,
                0.9999999999999062
        );
        algebraicsIn_Interval_helper(
                5,
                6,
                "[1, 1]",
                "QBarRandomProvider_algebraicsIn_Interval_iv",
                0.9999999999999062,
                0.4999999999999531,
                0.9999999999999062
        );
        algebraicsIn_Interval_helper(
                2,
                4,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_algebraicsIn_Interval_v",
                1.989399999999832,
                2.3949287482440096,
                -164.1120230067727
        );
        algebraicsIn_Interval_helper(
                5,
                6,
                "(-Infinity, Infinity)",
                "QBarRandomProvider_algebraicsIn_Interval_vi",
                3.0044000000002047,
                5.37798421736037,
                1.0692235994147452E15
        );
        algebraicsIn_Interval_helper(
                2,
                4,
                "[1, 4]",
                "QBarRandomProvider_algebraicsIn_Interval_vii",
                1.489099999999876,
                3.594271021653457,
                2.9041863183884464
        );
        algebraicsIn_Interval_helper(
                5,
                6,
                "[1, 4]",
                "QBarRandomProvider_algebraicsIn_Interval_viii",
                2.588900000000133,
                11.268856752766192,
                2.0954072748228487
        );
        algebraicsIn_Interval_helper(
                2,
                4,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsIn_Interval_ix",
                1.4891999999998753,
                3.8475012052058495,
                -3190.8033213967096
        );
        algebraicsIn_Interval_helper(
                5,
                6,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsIn_Interval_x",
                2.6172000000001407,
                11.991512772310747,
                -4.154123022551839E8
        );
        algebraicsIn_Interval_helper(
                2,
                4,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsIn_Interval_xi",
                1.4891999999998753,
                3.9683834163580003,
                3191.8033213969998
        );
        algebraicsIn_Interval_helper(
                5,
                6,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsIn_Interval_xii",
                2.6172000000001407,
                12.08664160123919,
                4.1541230325520384E8
        );

        algebraicsIn_Interval_fail_helper(1, 4, "[0, 0]");
        algebraicsIn_Interval_fail_helper(2, 3, "[0, 0]");
        algebraicsIn_Interval_fail_helper(1, 4, "[1, 2]");
        algebraicsIn_Interval_fail_helper(2, 3, "[1, 2]");
        algebraicsIn_Interval_fail_helper(1, 4, "(-Infinity, 1/2]");
        algebraicsIn_Interval_fail_helper(2, 3, "(-Infinity, 1/2]");
        algebraicsIn_Interval_fail_helper(1, 4, "[1/2, Infinity)");
        algebraicsIn_Interval_fail_helper(2, 3, "[1/2, Infinity)");
        algebraicsIn_Interval_fail_helper(1, 4, "(-Infinity, Infinity)");
        algebraicsIn_Interval_fail_helper(2, 3, "(-Infinity, Infinity)");
    }

    private static void algebraicsNotIn_int_Interval_helper(
            int scale,
            int degree,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).algebraicsNotIn(degree, Interval.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void algebraicsNotIn_int_Interval_fail_helper(int scale, int degree, @NotNull String a) {
        try {
            P.withScale(scale).algebraicsNotIn(degree, Interval.readStrict(a).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testAlgebraicsNotIn_int_Interval() {
        algebraicsNotIn_int_Interval_helper(
                2,
                1,
                "[0, 0]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_i",
                0.9999999999999062,
                2.645250000000337,
                50.39908570881847
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                2,
                "[0, 0]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_ii",
                1.9999999999998124,
                3.7552333333344534,
                52184.05751035433
        );
        algebraicsNotIn_int_Interval_helper(
                5,
                3,
                "[0, 0]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_iii",
                3.0000000000004805,
                5.427124999999181,
                -3.996771507650188E9
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                1,
                "[1, 1]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_iv",
                0.9999999999999062,
                2.4121500000003344,
                49.290536391595545
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                2,
                "[1, 1]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_v",
                1.9999999999998124,
                3.7552333333344534,
                52184.05751035433
        );
        algebraicsNotIn_int_Interval_helper(
                5,
                3,
                "[1, 1]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_vi",
                3.0000000000004805,
                5.427124999999181,
                -3.996771507650188E9
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                1,
                "[1, 4]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_vii",
                0.9999999999999062,
                3.438100000000282,
                -3108.748223724041
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                2,
                "[1, 4]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_viii",
                1.9999999999998124,
                5.809266666668504,
                -3539.231983662671
        );
        algebraicsNotIn_int_Interval_helper(
                5,
                3,
                "[1, 4]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_ix",
                3.0000000000004805,
                9.71932500000014,
                -1.0504580850318748E11
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                1,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_x",
                0.9999999999999062,
                3.881150000000748,
                113.85791502223523
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                2,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_xi",
                1.9999999999998124,
                6.690066666667999,
                5709398.569194343
        );
        algebraicsNotIn_int_Interval_helper(
                5,
                3,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_xii",
                3.0000000000004805,
                11.05069999999989,
                8.644705115388935E8
        );
        algebraicsNotIn_int_Interval_helper(
                2,
                1,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_xiii",
                0.9999999999999062,
                3.5017000000005405,
                -112.8579150222334
        );
        algebraicsNotIn_int_Interval_helper(
                3,
                2,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_xiv",
                1.9999999999998124,
                6.572900000001334,
                -5709397.569194325
        );
        algebraicsNotIn_int_Interval_helper(
                5,
                3,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsNotIn_int_Interval_xv",
                3.0000000000004805,
                10.9421249999999,
                -8.644705105388954E8
        );

        algebraicsNotIn_int_Interval_fail_helper(1, 1, "[0, 0]");
        algebraicsNotIn_int_Interval_fail_helper(2, 0, "[0, 0]");
        algebraicsNotIn_int_Interval_fail_helper(1, 1, "[1, 2]");
        algebraicsNotIn_int_Interval_fail_helper(2, 0, "[1, 2]");
        algebraicsNotIn_int_Interval_fail_helper(1, 1, "(-Infinity, 1/2]");
        algebraicsNotIn_int_Interval_fail_helper(2, 0, "(-Infinity, 1/2]");
        algebraicsNotIn_int_Interval_fail_helper(1, 1, "[1/2, Infinity)");
        algebraicsNotIn_int_Interval_fail_helper(2, 0, "[1/2, Infinity)");
        algebraicsNotIn_int_Interval_fail_helper(2, 1, "(-Infinity, Infinity)");
        algebraicsNotIn_int_Interval_fail_helper(2, 2, "(-Infinity, Infinity)");
    }

    private static void algebraicsNotIn_Interval_helper(
            int scale,
            int secondaryScale,
            @NotNull String a,
            @NotNull String output,
            double meanDegree,
            double meanCoefficientBitSize,
            double meanValue
    ) {
        algebraics_helper(
                P.withScale(scale).withSecondaryScale(secondaryScale).algebraicsNotIn(Interval.readStrict(a).get()),
                output,
                meanDegree,
                meanCoefficientBitSize,
                meanValue
        );
        P.reset();
    }

    private static void algebraicsNotIn_Interval_fail_helper(int scale, int secondaryScale, @NotNull String a) {
        try {
            P.withScale(scale).withSecondaryScale(secondaryScale).algebraicsNotIn(Interval.readStrict(a).get());
            fail();
        } catch (IllegalStateException | IllegalArgumentException ignored) {}
        finally {
            P.reset();
        }
    }

    @Test
    public void testAlgebraicsNotIn_Interval() {
        algebraicsNotIn_Interval_helper(
                2,
                4,
                "[0, 0]",
                "QBarRandomProvider_algebraicsNotIn_Interval_i",
                2.0843999999998784,
                2.5155621838929787,
                -163.84853791231663
        );
        algebraicsNotIn_Interval_helper(
                5,
                6,
                "[0, 0]",
                "QBarRandomProvider_algebraicsNotIn_Interval_ii",
                3.0289000000002093,
                5.422050683809328,
                1.069223599469271E15
        );
        algebraicsNotIn_Interval_helper(
                2,
                4,
                "[1, 1]",
                "QBarRandomProvider_algebraicsNotIn_Interval_iii",
                2.017499999999839,
                2.4310853355425603,
                -164.373436683675
        );
        algebraicsNotIn_Interval_helper(
                5,
                6,
                "[1, 1]",
                "QBarRandomProvider_algebraicsNotIn_Interval_iv",
                3.015500000000206,
                5.397061387124492,
                1.0692235994147276E15
        );
        algebraicsNotIn_Interval_helper(
                2,
                4,
                "[1, 4]",
                "QBarRandomProvider_algebraicsNotIn_Interval_v",
                1.9910999999998333,
                5.011066162948756,
                -162.91159960084028
        );
        algebraicsNotIn_Interval_helper(
                5,
                6,
                "[1, 4]",
                "QBarRandomProvider_algebraicsNotIn_Interval_vi",
                2.9929000000001955,
                11.208895790027752,
                -8.119390882713261E9
        );
        algebraicsNotIn_Interval_helper(
                2,
                4,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsNotIn_Interval_vii",
                1.9958999999998324,
                6.033312193330977,
                3380.063897862303
        );
        algebraicsNotIn_Interval_helper(
                5,
                6,
                "(-Infinity, 1/2]",
                "QBarRandomProvider_algebraicsNotIn_Interval_viii",
                3.028700000000205,
                13.533373048378317,
                1.0350739053452423E9
        );
        algebraicsNotIn_Interval_helper(
                2,
                4,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsNotIn_Interval_ix",
                1.9958999999998324,
                5.820421242364609,
                -3379.0638978623783
        );
        algebraicsNotIn_Interval_helper(
                5,
                6,
                "[1/2, Infinity)",
                "QBarRandomProvider_algebraicsNotIn_Interval_x",
                3.028700000000205,
                13.42611760617619,
                -1.0350739043452431E9
        );

        algebraicsNotIn_Interval_fail_helper(1, 4, "[0, 0]");
        algebraicsNotIn_Interval_fail_helper(2, 3, "[0, 0]");
        algebraicsNotIn_Interval_fail_helper(1, 4, "[1, 2]");
        algebraicsNotIn_Interval_fail_helper(2, 3, "[1, 2]");
        algebraicsNotIn_Interval_fail_helper(1, 4, "(-Infinity, 1/2]");
        algebraicsNotIn_Interval_fail_helper(2, 3, "(-Infinity, 1/2]");
        algebraicsNotIn_Interval_fail_helper(1, 4, "[1/2, Infinity)");
        algebraicsNotIn_Interval_fail_helper(2, 3, "[1/2, Infinity)");
        algebraicsNotIn_Interval_fail_helper(2, 4, "(-Infinity, Infinity)");
    }

    @Test
    public void testEquals() {
        List<QBarRandomProvider> xs = Arrays.asList(
                QBarRandomProvider.example(),
                Q.withScale(3).withSecondaryScale(0).withTertiaryScale(-1),
                R.withScale(0).withSecondaryScale(10).withTertiaryScale(5)
        );
        List<QBarRandomProvider> ys = Arrays.asList(
                QBarRandomProvider.example(),
                Q.withScale(3).withSecondaryScale(0).withTertiaryScale(-1),
                R.withScale(0).withSecondaryScale(10).withTertiaryScale(5)
        );
        testEqualsHelper(xs, ys);
        //noinspection EqualsBetweenInconvertibleTypes
        assertFalse(P.equals("hello"));
    }

    private static void hashCode_helper(
            @NotNull QBarRandomProvider rp,
            int scale,
            int secondaryScale,
            int tertiaryScale,
            int output
    ) {
        aeq(
                rp.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale).hashCode(),
                output
        );
    }

    @Test
    public void testHashCode() {
        hashCode_helper(P, 32, 8, 2, -1291053760);
        hashCode_helper(Q, 3, 0, -1, 1656156693);
        hashCode_helper(R, 0, 10, 5, -453720855);
    }

    private static void toString_helper(
            @NotNull QBarRandomProvider rp,
            int scale,
            int secondaryScale,
            int tertiaryScale,
            @NotNull String output
    ) {
        aeq(rp.withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale), output);
    }

    @Test
    public void testToString() {
        toString_helper(P, 32, 8, 2, "QBarRandomProvider[@-8800290164235921060, 32, 8, 2]");
        toString_helper(Q, 3, 0, -1, "QBarRandomProvider[@-7948823947390831374, 3, 0, -1]");
        toString_helper(R, 0, 10, 5, "QBarRandomProvider[@2449928962525148503, 0, 10, 5]");
    }

    private static double meanOfIntegers(@NotNull List<Integer> xs) {
        int size = xs.size();
        return sumDouble(toList(map(i -> (double) i / size, xs)));
    }

    private static double meanOfRationals(@NotNull List<Rational> xs) {
        int size = xs.size();
        return sumDouble(toList(map(r -> r.doubleValue() / size, xs)));
    }

    private static double meanOfAlgebraics(@NotNull List<Algebraic> xs) {
        int size = xs.size();
        return sumDouble(toList(map(r -> r.doubleValue() / size, xs)));
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Variable::readStrict).apply(s).get();
    }
}
