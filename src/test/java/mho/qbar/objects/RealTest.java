package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Real.*;
import static mho.wheels.testing.Testing.TINY_LIMIT;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.aeqitLimit;
import static org.junit.Assert.fail;

public class RealTest {
    private static final @NotNull Real NEGATIVE_FOUR_THIRDS = of(Rational.of(-4, 3));
    private static final @NotNull Real NEGATIVE_ONE_HALF = ONE_HALF.negate();
    private static final @NotNull Real THREE_HALVES = of(Rational.of(3, 2));
    private static final @NotNull Real NEGATIVE_THREE_HALVES = of(Rational.of(-3, 2));

    private static void constant_helper(@NotNull Real input, @NotNull String output) {
        input.validate();
        aeq(input, output);
    }

    private static void constant_helper(@NotNull Rational input, @NotNull String output) {
        aeq(input, output);
    }

    @Test
    public void testConstants() {
        constant_helper(ZERO, "0");
        constant_helper(ONE, "1");
        constant_helper(TEN, "10");
        constant_helper(TWO, "2");
        constant_helper(NEGATIVE_ONE, "-1");
        constant_helper(ONE_HALF, "0.5");
        constant_helper(SQRT_TWO, "1.41421356237309504880...");
        constant_helper(PHI, "1.61803398874989484820...");
        constant_helper(E, "2.71828182845904523536...");
        constant_helper(PI, "3.14159265358979323846...");

        constant_helper(DEFAULT_RESOLUTION, "1/1267650600228229401496703205376");
    }

    private static void of_Rational_helper(@NotNull String input, @NotNull String output) {
        Real x = of(Rational.readStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("0", "0");
        of_Rational_helper("1", "1");
        of_Rational_helper("2", "2");
        of_Rational_helper("-2", "-2");
        of_Rational_helper("5/3", "1.66666666666666666666...");
        of_Rational_helper("-5/3", "-1.66666666666666666666...");
    }

    private static void of_BigInteger_helper(@NotNull String input) {
        Real x = of(Readers.readBigIntegerStrict(input).get());
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_BigInteger() {
        of_BigInteger_helper("0");
        of_BigInteger_helper("1");
        of_BigInteger_helper("-1");
        of_BigInteger_helper("23");
        of_BigInteger_helper("-23");
    }

    private static void of_long_helper(long input) {
        Real x = of(input);
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_long() {
        of_long_helper(0L);
        of_long_helper(1L);
        of_long_helper(-1L);
        of_long_helper(23L);
        of_long_helper(-23L);
    }

    private static void of_int_helper(int input) {
        Real x = of(input);
        x.validate();
        aeq(x, input);
    }

    @Test
    public void testOf_int() {
        of_int_helper(0);
        of_int_helper(1);
        of_int_helper(-1);
        of_int_helper(23);
        of_int_helper(-23);
    }

    private static void of_BinaryFraction_helper(@NotNull String input, @NotNull String output) {
        Real x = of(BinaryFraction.readStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testOf_BinaryFraction() {
        of_BinaryFraction_helper("0", "0");
        of_BinaryFraction_helper("1", "1");
        of_BinaryFraction_helper("11", "11");
        of_BinaryFraction_helper("5 >> 20", "0.00000476837158203125");
        of_BinaryFraction_helper("5 << 20", "5242880");
        of_BinaryFraction_helper("-1", "-1");
        of_BinaryFraction_helper("-11", "-11");
        of_BinaryFraction_helper("-5 >> 20", "-0.00000476837158203125");
        of_BinaryFraction_helper("-5 << 20", "-5242880");
    }

    private static void of_float_helper(float f, @NotNull String output) {
        Optional<Real> x = of(f);
        if (x.isPresent()) {
            x.get().validate();
        }
        aeq(x, output);
    }

    @Test
    public void testOf_float() {
        of_float_helper(0.0f, "Optional[0]");
        of_float_helper(1.0f, "Optional[1]");
        of_float_helper(13.0f, "Optional[13]");
        of_float_helper(-5.0f, "Optional[-5]");
        of_float_helper(1.5f, "Optional[1.5]");
        of_float_helper(0.15625f, "Optional[0.15625]");
        of_float_helper(0.1f, "Optional[0.1]");
        of_float_helper(1.0f / 3.0f, "Optional[0.33333334]");
        of_float_helper(1.0e10f, "Optional[10000000000]");
        of_float_helper(1.0e30f, "Optional[1000000000000000000000000000000]");
        of_float_helper((float) Math.PI, "Optional[3.1415927]");
        of_float_helper((float) Math.E, "Optional[2.7182817]");
        of_float_helper((float) Math.sqrt(2), "Optional[1.4142135]");
        of_float_helper(Float.MIN_VALUE, "Optional[0.00000000000000000000...]");
        of_float_helper(-Float.MIN_VALUE, "Optional[-0.00000000000000000000...]");
        of_float_helper(Float.MIN_NORMAL, "Optional[0.00000000000000000000...]");
        of_float_helper(-Float.MIN_NORMAL, "Optional[-0.00000000000000000000...]");
        of_float_helper(Float.MAX_VALUE, "Optional[340282350000000000000000000000000000000]");
        of_float_helper(-Float.MAX_VALUE, "Optional[-340282350000000000000000000000000000000]");

        of_float_helper(Float.POSITIVE_INFINITY, "Optional.empty");
        of_float_helper(Float.NEGATIVE_INFINITY, "Optional.empty");
        of_float_helper(Float.NaN, "Optional.empty");
    }

    private static void of_double_helper(double d, @NotNull String output) {
        Optional<Real> x = of(d);
        if (x.isPresent()) {
            x.get().validate();
        }
        aeq(x, output);
    }

    @Test
    public void testOf_double() {
        of_double_helper(0.0, "Optional[0]");
        of_double_helper(1.0, "Optional[1]");
        of_double_helper(13.0, "Optional[13]");
        of_double_helper(-5.0, "Optional[-5]");
        of_double_helper(1.5, "Optional[1.5]");
        of_double_helper(0.15625, "Optional[0.15625]");
        of_double_helper(0.1, "Optional[0.1]");
        of_double_helper(1.0 / 3.0, "Optional[0.3333333333333333]");
        of_double_helper(1.0e10, "Optional[10000000000]");
        of_double_helper(1.0e30, "Optional[1000000000000000000000000000000]");
        of_double_helper(Math.PI, "Optional[3.141592653589793]");
        of_double_helper(Math.E, "Optional[2.718281828459045]");
        of_double_helper(Math.sqrt(2), "Optional[1.4142135623730951]");
        of_double_helper(Double.MIN_VALUE, "Optional[0.00000000000000000000...]");
        of_double_helper(-Double.MIN_VALUE, "Optional[-0.00000000000000000000...]");
        of_double_helper(Double.MIN_NORMAL, "Optional[0.00000000000000000000...]");
        of_double_helper(-Double.MIN_NORMAL, "Optional[-0.00000000000000000000...]");
        of_double_helper(Double.MAX_VALUE,
                "Optional[179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000]");
        of_double_helper(-Double.MAX_VALUE,
                "Optional[-17976931348623157000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000]");

        of_double_helper(Double.POSITIVE_INFINITY, "Optional.empty");
        of_double_helper(Double.NEGATIVE_INFINITY, "Optional.empty");
        of_double_helper(Double.NaN, "Optional.empty");
    }

    private static void ofExact_float_helper(float f, @NotNull String output) {
        Optional<Real> x = ofExact(f);
        if (x.isPresent()) {
            x.get().validate();
        }
        aeq(x, output);
    }

    @Test
    public void testOfExact_float() {
        ofExact_float_helper(0.0f, "Optional[0]");
        ofExact_float_helper(1.0f, "Optional[1]");
        ofExact_float_helper(13.0f, "Optional[13]");
        ofExact_float_helper(-5.0f, "Optional[-5]");
        ofExact_float_helper(1.5f, "Optional[1.5]");
        ofExact_float_helper(0.15625f, "Optional[0.15625]");
        ofExact_float_helper(0.1f, "Optional[0.10000000149011611938...]");
        ofExact_float_helper(1.0f / 3.0f, "Optional[0.33333334326744079589...]");
        ofExact_float_helper(1.0e10f, "Optional[10000000000]");
        ofExact_float_helper(1.0e30f, "Optional[1000000015047466219876688855040]");
        ofExact_float_helper((float) Math.PI, "Optional[3.14159274101257324218...]");
        ofExact_float_helper((float) Math.E, "Optional[2.71828174591064453125]");
        ofExact_float_helper((float) Math.sqrt(2), "Optional[1.41421353816986083984...]");
        ofExact_float_helper(Float.MIN_VALUE, "Optional[0.00000000000000000000...]");
        ofExact_float_helper(-Float.MIN_VALUE, "Optional[-0.00000000000000000000...]");
        ofExact_float_helper(Float.MIN_NORMAL, "Optional[0.00000000000000000000...]");
        ofExact_float_helper(-Float.MIN_NORMAL, "Optional[-0.00000000000000000000...]");
        ofExact_float_helper(Float.MAX_VALUE, "Optional[340282346638528859811704183484516925440]");
        ofExact_float_helper(-Float.MAX_VALUE, "Optional[-340282346638528859811704183484516925440]");

        ofExact_float_helper(Float.POSITIVE_INFINITY, "Optional.empty");
        ofExact_float_helper(Float.NEGATIVE_INFINITY, "Optional.empty");
        ofExact_float_helper(Float.NaN, "Optional.empty");
    }

    private static void ofExact_double_helper(double d, @NotNull String output) {
        Optional<Real> x = ofExact(d);
        if (x.isPresent()) {
            x.get().validate();
        }
        aeq(x, output);
    }

    @Test
    public void testOfExact_double() {
        ofExact_double_helper(0.0, "Optional[0]");
        ofExact_double_helper(1.0, "Optional[1]");
        ofExact_double_helper(13.0, "Optional[13]");
        ofExact_double_helper(-5.0, "Optional[-5]");
        ofExact_double_helper(1.5, "Optional[1.5]");
        ofExact_double_helper(0.15625, "Optional[0.15625]");
        ofExact_double_helper(0.1, "Optional[0.10000000000000000555...]");
        ofExact_double_helper(1.0 / 3.0, "Optional[0.33333333333333331482...]");
        ofExact_double_helper(1.0e10, "Optional[10000000000]");
        ofExact_double_helper(1.0e30, "Optional[1000000000000000019884624838656]");
        ofExact_double_helper(Math.PI, "Optional[3.14159265358979311599...]");
        ofExact_double_helper(Math.E, "Optional[2.71828182845904509079...]");
        ofExact_double_helper(Math.sqrt(2), "Optional[1.41421356237309514547...]");
        ofExact_double_helper(Double.MIN_VALUE, "Optional[0.00000000000000000000...]");
        ofExact_double_helper(-Double.MIN_VALUE, "Optional[-0.00000000000000000000...]");
        ofExact_double_helper(Double.MIN_NORMAL, "Optional[0.00000000000000000000...]");
        ofExact_double_helper(-Double.MIN_NORMAL, "Optional[-0.00000000000000000000...]");
        ofExact_double_helper(Double.MAX_VALUE,
                "Optional[179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558" +
                "632766878171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389" +
                "328944075868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881" +
                "250404026184124858368]");
        ofExact_double_helper(-Double.MAX_VALUE,
                "Optional[-17976931348623157081452742373170435679807056752584499659891747680315726078002853876058955" +
                "863276687817154045895351438246423432132688946418276846754670353751698604991057655128207624549009038" +
                "932894407586850845513394230458323690322294816580855933212334827479782620414472316873817718091929988" +
                "1250404026184124858368]");

        ofExact_double_helper(Double.POSITIVE_INFINITY, "Optional.empty");
        ofExact_double_helper(Double.NEGATIVE_INFINITY, "Optional.empty");
        ofExact_double_helper(Double.NaN, "Optional.empty");
    }

    private static void of_BigDecimal_helper(@NotNull String input, @NotNull String output) {
        Real x = of(Readers.readBigDecimalStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testOf_BigDecimal() {
        of_BigDecimal_helper("0", "0");
        of_BigDecimal_helper("1", "1");
        of_BigDecimal_helper("3", "3");
        of_BigDecimal_helper("-5", "-5");
        of_BigDecimal_helper("0.1", "0.1");
        of_BigDecimal_helper("3.14159", "3.14159");
        of_BigDecimal_helper("-2.718281828459045", "-2.718281828459045");
        of_BigDecimal_helper("1E-14", "0.00000000000001");
        of_BigDecimal_helper("1000000000000000", "1000000000000000");
        of_BigDecimal_helper("1E+15", "1000000000000000");
    }

    private static void fuzzyRepresentation_helper(@NotNull String input, @NotNull String output) {
        Real x = fuzzyRepresentation(Rational.readStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testFuzzyRepresentation() {
        fuzzyRepresentation_helper("0", "~0");
        fuzzyRepresentation_helper("1", "+...");
        fuzzyRepresentation_helper("-1", "-...");
        fuzzyRepresentation_helper("2", "+...");
        fuzzyRepresentation_helper("-2", "-...");
        fuzzyRepresentation_helper("11", "1?....");
        fuzzyRepresentation_helper("-11", "-1?....");
        fuzzyRepresentation_helper("5/4", "1.2...");
        fuzzyRepresentation_helper("-5/4", "-1.2...");
        fuzzyRepresentation_helper("5/3", "1.66666666666666666666...");
        fuzzyRepresentation_helper("-5/3", "-1.66666666666666666666...");
    }

    private static void leftFuzzyRepresentation_helper(@NotNull String input, @NotNull String output) {
        Real x = leftFuzzyRepresentation(Rational.readStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testLeftFuzzyRepresentation() {
        leftFuzzyRepresentation_helper("0", "~0");
        leftFuzzyRepresentation_helper("1", "+...");
        leftFuzzyRepresentation_helper("-1", "-1.00000000000000000000...");
        leftFuzzyRepresentation_helper("2", "+...");
        leftFuzzyRepresentation_helper("-2", "-2.00000000000000000000...");
        leftFuzzyRepresentation_helper("11", "1?....");
        leftFuzzyRepresentation_helper("-11", "-11.00000000000000000000...");
        leftFuzzyRepresentation_helper("5/4", "1.2...");
        leftFuzzyRepresentation_helper("-5/4", "-1.25000000000000000000...");
        leftFuzzyRepresentation_helper("5/3", "1.66666666666666666666...");
        leftFuzzyRepresentation_helper("-5/3", "-1.66666666666666666666...");
    }

    private static void rightFuzzyRepresentation_helper(@NotNull String input, @NotNull String output) {
        Real x = rightFuzzyRepresentation(Rational.readStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testRightFuzzyRepresentation() {
        rightFuzzyRepresentation_helper("0", "~0");
        rightFuzzyRepresentation_helper("1", "1.00000000000000000000...");
        rightFuzzyRepresentation_helper("-1", "-...");
        rightFuzzyRepresentation_helper("2", "2.00000000000000000000...");
        rightFuzzyRepresentation_helper("-2", "-...");
        rightFuzzyRepresentation_helper("11", "11.00000000000000000000...");
        rightFuzzyRepresentation_helper("-11", "-1?....");
        rightFuzzyRepresentation_helper("5/4", "1.25000000000000000000...");
        rightFuzzyRepresentation_helper("-5/4", "-1.2...");
        rightFuzzyRepresentation_helper("5/3", "1.66666666666666666666...");
        rightFuzzyRepresentation_helper("-5/3", "-1.66666666666666666666...");
    }

    private static void iterator_helper(@NotNull Real input, int limit, @NotNull String output) {
        aeqitLimit(limit, input, output);
    }

    private static void iterator_helper(@NotNull Real input, @NotNull String output) {
        iterator_helper(input, TINY_LIMIT, output);
    }

    @Test
    public void testIterator() {
        iterator_helper(ZERO,
                "[[0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0]," +
                " [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], [0, 0], ...]");
        iterator_helper(ONE,
                "[[1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1]," +
                " [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], [1, 1], ...]");
        iterator_helper(NEGATIVE_ONE,
                "[[-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1]," +
                " [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1], [-1, -1]," +
                " [-1, -1], [-1, -1], ...]");
        iterator_helper(NEGATIVE_FOUR_THIRDS,
                "[[-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3]," +
                " [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3]," +
                " [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], [-4/3, -4/3], ...]");
        iterator_helper(SQRT_TWO,
                "[[0, 4], [0, 2], [1, 2], [1, 3/2], [5/4, 3/2], [11/8, 3/2], [11/8, 23/16], [45/32, 23/16]," +
                " [45/32, 91/64], [181/128, 91/64], [181/128, 363/256], [181/128, 725/512], [181/128, 1449/1024]," +
                " [181/128, 2897/2048], [181/128, 5793/4096], [11585/8192, 5793/4096], [11585/8192, 23171/16384]," +
                " [11585/8192, 46341/32768], [92681/65536, 46341/32768], [185363/131072, 46341/32768], ...]");
        iterator_helper(E,
                "[[2, 4], [5/2, 7/2], [8/3, 3], [65/24, 67/24], [163/60, 41/15], [1957/720, 653/240]," +
                " [685/252, 6851/2520], [109601/40320, 109603/40320], [98641/36288, 11743/4320]," +
                " [9864101/3628800, 9864103/3628800], [13563139/4989600, 54252557/19958400]," +
                " [260412269/95800320, 144673483/53222400], [8463398743/3113510400, 1057924843/389188800]," +
                " [47395032961/17435658240, 236975164807/87178291200]," +
                " [888656868019/326918592000, 592437912013/217945728000]," +
                " [56874039553217/20922789888000, 8124862793317/2988969984000]," +
                " [7437374403113/2736057139200, 241714668101173/88921857024000]," +
                " [17403456103284421/6402373705728000, 5801152034428141/2134124568576000]," +
                " [82666416490601/30411275102208, 165332832981202001/60822550204416000]," +
                " [6613313319248080001/2432902008176640000, 508716409172929231/187146308321280000], ...]");
        iterator_helper(PI, 5,
                "[[281476/89625, 651864872/204778785]," +
                " [1339760982986645756/426459285655703125, 15305839961353732690848/4871956171187883640625]," +
                " [90159814330711160623850657403076/28698760246862382335602001953125," +
                " 206000752128714602309315467175106616/65572075362441045655676878142578125]," +
                " [11950827742763724038336441397624078583300672396/3804066618602651140286948940075677032470703125," +
                " 1820381950672004239437026206052012466686191144368/579445571523205428758215494416167327391357421875" +
                "]," +
                " [1574353035058098242816726960753383747979148678087888676629836/50113213540244890249873419393114017" +
                "4949627402324676513671875," +
                " 3597144788622195741816151737039073386719688033186645699212537048/114500674825293135038520783566162" +
                "6317331906673927513885498046875], ...]");

        iterator_helper(fuzzyRepresentation(Rational.ZERO),
                "[[-1, 1], [-1/2, 1/2], [-1/4, 1/4], [-1/8, 1/8], [-1/16, 1/16], [-1/32, 1/32], [-1/64, 1/64]," +
                " [-1/128, 1/128], [-1/256, 1/256], [-1/512, 1/512], [-1/1024, 1/1024], [-1/2048, 1/2048]," +
                " [-1/4096, 1/4096], [-1/8192, 1/8192], [-1/16384, 1/16384], [-1/32768, 1/32768]," +
                " [-1/65536, 1/65536], [-1/131072, 1/131072], [-1/262144, 1/262144], [-1/524288, 1/524288], ...]");
        iterator_helper(leftFuzzyRepresentation(Rational.ZERO),
                "[[-1, 0], [-1/2, 0], [-1/4, 0], [-1/8, 0], [-1/16, 0], [-1/32, 0], [-1/64, 0], [-1/128, 0]," +
                " [-1/256, 0], [-1/512, 0], [-1/1024, 0], [-1/2048, 0], [-1/4096, 0], [-1/8192, 0], [-1/16384, 0]," +
                " [-1/32768, 0], [-1/65536, 0], [-1/131072, 0], [-1/262144, 0], [-1/524288, 0], ...]");
        iterator_helper(rightFuzzyRepresentation(Rational.ZERO),
                "[[0, 1], [0, 1/2], [0, 1/4], [0, 1/8], [0, 1/16], [0, 1/32], [0, 1/64], [0, 1/128], [0, 1/256]," +
                " [0, 1/512], [0, 1/1024], [0, 1/2048], [0, 1/4096], [0, 1/8192], [0, 1/16384], [0, 1/32768]," +
                " [0, 1/65536], [0, 1/131072], [0, 1/262144], [0, 1/524288], ...]");
    }

    private static void match_helper(@NotNull Real r, @NotNull List<Real> targets, int output) {
        aeq(r.match(targets), output);
    }

    private static void match_fail_helper(@NotNull Real r, @NotNull List<Real> targets) {
        try {
            r.match(targets);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testMatch() {
        List<Real> targets = Arrays.asList(ZERO, ONE, NEGATIVE_ONE, NEGATIVE_FOUR_THIRDS, SQRT_TWO, E, PI);

        match_helper(ZERO, targets, 0);
        match_helper(ONE, targets, 1);
        match_helper(NEGATIVE_ONE, targets, 2);
        match_helper(NEGATIVE_FOUR_THIRDS, targets, 3);
        match_helper(SQRT_TWO, targets, 4);
        match_helper(E, targets, 5);
        match_helper(PI, targets, 6);

        match_fail_helper(ONE, Collections.emptyList());
        match_fail_helper(of(100), targets);
    }

    private static void isExactInteger_helper(@NotNull Real input, boolean output) {
        aeq(input.isExactInteger(), output);
    }

    @Test
    public void testIsExactInteger() {
        isExactInteger_helper(ZERO, true);
        isExactInteger_helper(ONE, true);
        isExactInteger_helper(NEGATIVE_ONE, true);
        isExactInteger_helper(ONE_HALF, false);
        isExactInteger_helper(NEGATIVE_FOUR_THIRDS, false);
        isExactInteger_helper(SQRT_TWO, false);
        isExactInteger_helper(E, false);
        isExactInteger_helper(PI, false);
        isExactInteger_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExactInteger_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExactInteger_helper(rightFuzzyRepresentation(Rational.ZERO), false);
        isExactInteger_helper(fuzzyRepresentation(Rational.ONE), false);
        isExactInteger_helper(leftFuzzyRepresentation(Rational.ONE), false);
        isExactInteger_helper(rightFuzzyRepresentation(Rational.ONE), false);
    }

    private static void bigIntegerValueUnsafe_RoundingMode_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull String output
    ) {
        aeq(x.bigIntegerValueUnsafe(Readers.readRoundingModeStrict(rm).get()), output);
    }

    private static void bigIntegerValueUnsafe_RoundingMode_fail_helper(@NotNull Real x, @NotNull String rm) {
        try {
            x.bigIntegerValueUnsafe(Readers.readRoundingModeStrict(rm).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValueUnsafe_RoundingMode() {
        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "UP", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "UP", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "UP", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "UP", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "UP", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "UP", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "UP", "4");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE), "UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF), "UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), "UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(3, 2)), "UP", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-3, 2)), "UP", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "UP", "-2");

        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "DOWN", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "DOWN", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ZERO), "DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE), "DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF), "DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), "DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(3, 2)), "DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-3, 2)), "DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "DOWN", "-1");

        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "CEILING", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "CEILING", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "CEILING", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "CEILING", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "CEILING", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "CEILING", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "CEILING", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "CEILING", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "CEILING", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "CEILING", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "CEILING", "4");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ZERO), "CEILING", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE), "CEILING", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "CEILING", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF), "CEILING", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), "CEILING", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(3, 2)), "CEILING", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-3, 2)), "CEILING", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "CEILING", "-1");

        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "FLOOR", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "FLOOR", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "FLOOR", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "FLOOR", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "FLOOR", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "FLOOR", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "FLOOR", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "FLOOR", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "FLOOR", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "FLOOR", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "FLOOR", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "FLOOR", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE), "FLOOR", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "FLOOR", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF), "FLOOR", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), "FLOOR", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(3, 2)), "FLOOR", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-3, 2)), "FLOOR", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "FLOOR", "-2");

        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "HALF_UP", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "HALF_UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "HALF_UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "HALF_UP", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "HALF_UP", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "HALF_UP", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "HALF_UP", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ZERO), "HALF_UP", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_UP", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE_HALF), "HALF_UP", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE_HALF.negate()), "HALF_UP",
                "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.of(3, 2)), "HALF_UP", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.of(-3, 2)), "HALF_UP", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "HALF_UP", "-1");

        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "HALF_DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "HALF_DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "HALF_DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "HALF_DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "HALF_DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "HALF_DOWN", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "HALF_DOWN", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ZERO), "HALF_DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "HALF_DOWN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE_HALF.negate()), "HALF_DOWN",
                "0");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.of(3, 2)), "HALF_DOWN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.of(-3, 2)), "HALF_DOWN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "HALF_DOWN", "-1");

        bigIntegerValueUnsafe_RoundingMode_helper(ZERO, "HALF_EVEN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE, "HALF_EVEN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_EVEN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_EVEN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_ONE_HALF, "HALF_EVEN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(THREE_HALVES, "HALF_EVEN", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_THREE_HALVES, "HALF_EVEN", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_EVEN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_EVEN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(E, "HALF_EVEN", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(PI, "HALF_EVEN", "3");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ZERO), "HALF_EVEN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_EVEN", "1");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_EVEN", "-1");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "HALF_EVEN", "0");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE_HALF.negate()), "HALF_EVEN",
                "0");
        bigIntegerValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.of(3, 2)), "HALF_EVEN", "2");
        bigIntegerValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.of(-3, 2)), "HALF_EVEN", "-2");
        bigIntegerValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.of(-4, 3)), "HALF_EVEN", "-1");

        bigIntegerValueUnsafe_RoundingMode_fail_helper(ONE_HALF, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(NEGATIVE_ONE_HALF, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(THREE_HALVES, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(NEGATIVE_THREE_HALVES, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(NEGATIVE_FOUR_THIRDS, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(SQRT_TWO, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(E, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(PI, "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(leftFuzzyRepresentation(Rational.ZERO), "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(rightFuzzyRepresentation(Rational.ZERO), "UNNECESSARY");
        bigIntegerValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.ZERO), "UNNECESSARY");
    }

    private static void bigIntegerValue_RoundingMode_Rational_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull Rational r,
            @NotNull String output
    ) {
        aeq(x.bigIntegerValue(Readers.readRoundingModeStrict(rm).get(), r), output);
    }

    private static void bigIntegerValue_RoundingMode_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull Rational r
    ) {
        try {
            x.bigIntegerValue(Readers.readRoundingModeStrict(rm).get(), r);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigIntegerValue_RoundingMode() {
        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "UP", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "UP", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "UP", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "UP", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "UP", DEFAULT_RESOLUTION, "Optional[-2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "UP", DEFAULT_RESOLUTION, "Optional[-2]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "UP", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "UP", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "UP", DEFAULT_RESOLUTION, "Optional[4]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[2]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "DOWN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "DOWN", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "DOWN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "DOWN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "DOWN", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "DOWN", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "DOWN", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "DOWN", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "CEILING", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "CEILING", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "CEILING", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "CEILING", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "CEILING", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "CEILING", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "CEILING", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "CEILING", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "CEILING", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "CEILING", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "CEILING", DEFAULT_RESOLUTION, "Optional[4]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[2]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "FLOOR", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "FLOOR", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "FLOOR", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "FLOOR", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "FLOOR", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "FLOOR", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "FLOOR", DEFAULT_RESOLUTION,
                "Optional[-2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "FLOOR", DEFAULT_RESOLUTION,
                "Optional[-2]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "FLOOR", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "FLOOR", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "FLOOR", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "HALF_UP", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_UP", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "HALF_UP", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "HALF_UP", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "HALF_UP", DEFAULT_RESOLUTION,
                "Optional[-2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "HALF_UP", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "HALF_UP", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "HALF_UP", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "HALF_DOWN", DEFAULT_RESOLUTION,
                "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "HALF_DOWN", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "HALF_DOWN", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(ZERO, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_ONE_HALF, "HALF_EVEN", DEFAULT_RESOLUTION,
                "Optional[0]");
        bigIntegerValue_RoundingMode_Rational_helper(THREE_HALVES, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_THREE_HALVES, "HALF_EVEN", DEFAULT_RESOLUTION,
                "Optional[-2]");
        bigIntegerValue_RoundingMode_Rational_helper(NEGATIVE_FOUR_THIRDS, "HALF_EVEN", DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_RoundingMode_Rational_helper(SQRT_TWO, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_RoundingMode_Rational_helper(E, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(PI, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE_HALF.negate()),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(3, 2)),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-3, 2)),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigIntegerValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-4, 3)),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigIntegerValue_RoundingMode_Rational_helper(PI.shiftLeft(10), "FLOOR", Rational.ONE, "Optional.empty");

        bigIntegerValue_RoundingMode_Rational_fail_helper(ZERO, "HALF_EVEN", Rational.ZERO);
        bigIntegerValue_RoundingMode_Rational_fail_helper(ZERO, "HALF_EVEN", Rational.NEGATIVE_ONE);

        bigIntegerValue_RoundingMode_Rational_fail_helper(ONE_HALF, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(NEGATIVE_ONE_HALF, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(THREE_HALVES, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(NEGATIVE_THREE_HALVES, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(NEGATIVE_FOUR_THIRDS, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(SQRT_TWO, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(E, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(PI, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigIntegerValue_RoundingMode_Rational_fail_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
        bigIntegerValue_RoundingMode_Rational_fail_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
        bigIntegerValue_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
    }

    private static void bigIntegerValueUnsafe_helper(@NotNull Real x, @NotNull String output) {
        aeq(x.bigIntegerValueUnsafe(), output);
    }

    @Test
    public void testBigIntegerValueUnsafe() {
        bigIntegerValueUnsafe_helper(ZERO, "0");
        bigIntegerValueUnsafe_helper(ONE, "1");
        bigIntegerValueUnsafe_helper(NEGATIVE_ONE, "-1");
        bigIntegerValueUnsafe_helper(ONE_HALF, "0");
        bigIntegerValueUnsafe_helper(NEGATIVE_ONE_HALF, "0");
        bigIntegerValueUnsafe_helper(THREE_HALVES, "2");
        bigIntegerValueUnsafe_helper(NEGATIVE_THREE_HALVES, "-2");
        bigIntegerValueUnsafe_helper(NEGATIVE_FOUR_THIRDS, "-1");
        bigIntegerValueUnsafe_helper(SQRT_TWO, "1");
        bigIntegerValueUnsafe_helper(E, "3");
        bigIntegerValueUnsafe_helper(PI, "3");
        bigIntegerValueUnsafe_helper(fuzzyRepresentation(Rational.ZERO), "0");
        bigIntegerValueUnsafe_helper(fuzzyRepresentation(Rational.ONE), "1");
        bigIntegerValueUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "-1");
        bigIntegerValueUnsafe_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "0");
        bigIntegerValueUnsafe_helper(rightFuzzyRepresentation(Rational.ONE_HALF.negate()), "0");
        bigIntegerValueUnsafe_helper(rightFuzzyRepresentation(Rational.of(3, 2)), "2");
        bigIntegerValueUnsafe_helper(leftFuzzyRepresentation(Rational.of(-3, 2)), "-2");
        bigIntegerValueUnsafe_helper(fuzzyRepresentation(Rational.of(-4, 3)), "-1");
    }

    private static void bigIntegerValue_Rational_helper(@NotNull Real x, @NotNull Rational r, @NotNull String output) {
        aeq(x.bigIntegerValue(r), output);
    }

    private static void bigIntegerValue_Rational_fail_helper(@NotNull Real x, @NotNull Rational r) {
        try {
            x.bigIntegerValue(r);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigIntegerValue() {
        bigIntegerValue_Rational_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_Rational_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_Rational_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_Rational_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_Rational_helper(NEGATIVE_ONE_HALF, DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_Rational_helper(THREE_HALVES, DEFAULT_RESOLUTION, "Optional[2]");
        bigIntegerValue_Rational_helper(NEGATIVE_THREE_HALVES, DEFAULT_RESOLUTION, "Optional[-2]");
        bigIntegerValue_Rational_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-1]");
        bigIntegerValue_Rational_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_Rational_helper(E, DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_Rational_helper(PI, DEFAULT_RESOLUTION, "Optional[3]");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[0]");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[1]");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), DEFAULT_RESOLUTION,
                "Optional[-1]");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION, "Optional.empty");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), DEFAULT_RESOLUTION,
                "Optional.empty");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.of(3, 2)), DEFAULT_RESOLUTION, "Optional.empty");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.of(-3, 2)), DEFAULT_RESOLUTION, "Optional.empty");
        bigIntegerValue_Rational_helper(fuzzyRepresentation(Rational.of(-4, 3)), DEFAULT_RESOLUTION, "Optional[-1]");

        bigIntegerValue_Rational_helper(PI.shiftLeft(100), Rational.ONE, "Optional.empty");

        bigIntegerValue_Rational_fail_helper(ZERO, Rational.ZERO);
        bigIntegerValue_Rational_fail_helper(ZERO, Rational.NEGATIVE_ONE);
    }

    private static void floorUnsafe_helper(@NotNull Real x, @NotNull String output) {
        aeq(x.floorUnsafe(), output);
    }

    @Test
    public void testFloorUnsafe_RoundingMode() {
        floorUnsafe_helper(ZERO, "0");
        floorUnsafe_helper(ONE, "1");
        floorUnsafe_helper(NEGATIVE_ONE, "-1");
        floorUnsafe_helper(ONE_HALF, "0");
        floorUnsafe_helper(NEGATIVE_ONE_HALF, "-1");
        floorUnsafe_helper(THREE_HALVES, "1");
        floorUnsafe_helper(NEGATIVE_THREE_HALVES, "-2");
        floorUnsafe_helper(NEGATIVE_FOUR_THIRDS, "-2");
        floorUnsafe_helper(SQRT_TWO, "1");
        floorUnsafe_helper(E, "2");
        floorUnsafe_helper(PI, "3");
        floorUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), "0");
        floorUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), "1");
        floorUnsafe_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "-1");
        floorUnsafe_helper(fuzzyRepresentation(Rational.ONE_HALF), "0");
        floorUnsafe_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), "-1");
        floorUnsafe_helper(fuzzyRepresentation(Rational.of(3, 2)), "1");
        floorUnsafe_helper(fuzzyRepresentation(Rational.of(-3, 2)), "-2");
        floorUnsafe_helper(fuzzyRepresentation(Rational.of(-4, 3)), "-2");
    }

    private static void floor_helper(@NotNull Real x, @NotNull Rational r, @NotNull String output) {
        aeq(x.floor(r), output);
    }

    private static void floor_fail_helper(@NotNull Real x, @NotNull Rational r) {
        try {
            x.floor(r);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFloor() {
        floor_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0]");
        floor_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        floor_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1]");
        floor_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[0]");
        floor_helper(NEGATIVE_ONE_HALF, DEFAULT_RESOLUTION, "Optional[-1]");
        floor_helper(THREE_HALVES, DEFAULT_RESOLUTION, "Optional[1]");
        floor_helper(NEGATIVE_THREE_HALVES, DEFAULT_RESOLUTION, "Optional[-2]");
        floor_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-2]");
        floor_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1]");
        floor_helper(E, DEFAULT_RESOLUTION, "Optional[2]");
        floor_helper(PI, DEFAULT_RESOLUTION, "Optional[3]");
        floor_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        floor_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional.empty");
        floor_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), DEFAULT_RESOLUTION, "Optional.empty");
        floor_helper(fuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION, "Optional[0]");
        floor_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), DEFAULT_RESOLUTION, "Optional[-1]");
        floor_helper(fuzzyRepresentation(Rational.of(3, 2)), DEFAULT_RESOLUTION, "Optional[1]");
        floor_helper(fuzzyRepresentation(Rational.of(-3, 2)), DEFAULT_RESOLUTION, "Optional[-2]");
        floor_helper(fuzzyRepresentation(Rational.of(-4, 3)), DEFAULT_RESOLUTION, "Optional[-2]");

        floor_helper(PI.shiftLeft(10), Rational.ONE, "Optional.empty");

        floor_fail_helper(ZERO, Rational.ZERO);
        floor_fail_helper(ZERO, Rational.NEGATIVE_ONE);
    }

    private static void ceilingUnsafe_helper(@NotNull Real x, @NotNull String output) {
        aeq(x.ceilingUnsafe(), output);
    }

    @Test
    public void testCeilingUnsafe() {
        ceilingUnsafe_helper(ZERO, "0");
        ceilingUnsafe_helper(ONE, "1");
        ceilingUnsafe_helper(NEGATIVE_ONE, "-1");
        ceilingUnsafe_helper(ONE_HALF, "1");
        ceilingUnsafe_helper(NEGATIVE_ONE_HALF, "0");
        ceilingUnsafe_helper(THREE_HALVES, "2");
        ceilingUnsafe_helper(NEGATIVE_THREE_HALVES, "-1");
        ceilingUnsafe_helper(NEGATIVE_FOUR_THIRDS, "-1");
        ceilingUnsafe_helper(SQRT_TWO, "2");
        ceilingUnsafe_helper(E, "3");
        ceilingUnsafe_helper(PI, "4");
        ceilingUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), "0");
        ceilingUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), "1");
        ceilingUnsafe_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "-1");
        ceilingUnsafe_helper(fuzzyRepresentation(Rational.ONE_HALF), "1");
        ceilingUnsafe_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), "0");
        ceilingUnsafe_helper(fuzzyRepresentation(Rational.of(3, 2)), "2");
        ceilingUnsafe_helper(fuzzyRepresentation(Rational.of(-3, 2)), "-1");
        ceilingUnsafe_helper(fuzzyRepresentation(Rational.of(-4, 3)), "-1");
    }

    private static void ceiling_helper(@NotNull Real x, @NotNull Rational r, @NotNull String output) {
        aeq(x.ceiling(r), output);
    }

    private static void ceiling_fail_helper(@NotNull Real x, @NotNull Rational r) {
        try {
            x.ceiling(r);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCeiling() {
        ceiling_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0]");
        ceiling_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        ceiling_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1]");
        ceiling_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[1]");
        ceiling_helper(NEGATIVE_ONE_HALF, DEFAULT_RESOLUTION, "Optional[0]");
        ceiling_helper(THREE_HALVES, DEFAULT_RESOLUTION, "Optional[2]");
        ceiling_helper(NEGATIVE_THREE_HALVES, DEFAULT_RESOLUTION, "Optional[-1]");
        ceiling_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-1]");
        ceiling_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[2]");
        ceiling_helper(E, DEFAULT_RESOLUTION, "Optional[3]");
        ceiling_helper(PI, DEFAULT_RESOLUTION, "Optional[4]");
        ceiling_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        ceiling_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional.empty");
        ceiling_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), DEFAULT_RESOLUTION, "Optional.empty");
        ceiling_helper(fuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION, "Optional[1]");
        ceiling_helper(fuzzyRepresentation(Rational.ONE_HALF.negate()), DEFAULT_RESOLUTION, "Optional[0]");
        ceiling_helper(fuzzyRepresentation(Rational.of(3, 2)), DEFAULT_RESOLUTION, "Optional[2]");
        ceiling_helper(fuzzyRepresentation(Rational.of(-3, 2)), DEFAULT_RESOLUTION, "Optional[-1]");
        ceiling_helper(fuzzyRepresentation(Rational.of(-4, 3)), DEFAULT_RESOLUTION, "Optional[-1]");

        ceiling_helper(PI.shiftLeft(10), Rational.ONE, "Optional.empty");

        ceiling_fail_helper(ZERO, Rational.ZERO);
        ceiling_fail_helper(ZERO, Rational.NEGATIVE_ONE);
    }

    private static void bigIntegerValueExact_helper(@NotNull Real input) {
        aeq(input.bigIntegerValueExact(), input);
    }

    private static void bigIntegerValueExact_fail_helper(@NotNull Real input) {
        try {
            input.bigIntegerValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigIntegerValueExact() {
        bigIntegerValueExact_helper(ZERO);
        bigIntegerValueExact_helper(ONE);
        bigIntegerValueExact_helper(NEGATIVE_ONE);

        bigIntegerValueExact_fail_helper(ONE_HALF);
        bigIntegerValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        bigIntegerValueExact_fail_helper(SQRT_TWO);
        bigIntegerValueExact_fail_helper(E);
        bigIntegerValueExact_fail_helper(PI);
        bigIntegerValueExact_fail_helper(fuzzyRepresentation(Rational.ZERO));
        bigIntegerValueExact_fail_helper(leftFuzzyRepresentation(Rational.ZERO));
        bigIntegerValueExact_fail_helper(rightFuzzyRepresentation(Rational.ZERO));
        bigIntegerValueExact_fail_helper(fuzzyRepresentation(Rational.ONE));
        bigIntegerValueExact_fail_helper(leftFuzzyRepresentation(Rational.ONE));
        bigIntegerValueExact_fail_helper(rightFuzzyRepresentation(Rational.ONE));
    }

    private static void byteValueExact_helper(@NotNull Real input) {
        aeq(input.byteValueExact(), input);
    }

    private static void byteValueExact_fail_helper(@NotNull Real input) {
        try {
            input.byteValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testByteValueExact() {
        byteValueExact_helper(ONE);
        byteValueExact_helper(ZERO);
        byteValueExact_helper(NEGATIVE_ONE);
        byteValueExact_helper(of(23));
        byteValueExact_helper(of(8));

        byteValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        byteValueExact_fail_helper(SQRT_TWO);
        byteValueExact_fail_helper(of(1000));
    }

    private static void shortValueExact_helper(@NotNull Real input) {
        aeq(input.shortValueExact(), input);
    }

    private static void shortValueExact_fail_helper(@NotNull Real input) {
        try {
            input.shortValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShortValueExact() {
        shortValueExact_helper(ONE);
        shortValueExact_helper(ZERO);
        shortValueExact_helper(NEGATIVE_ONE);
        shortValueExact_helper(of(23));
        shortValueExact_helper(of(8));

        shortValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        shortValueExact_fail_helper(SQRT_TWO);
        shortValueExact_fail_helper(of(100000));
    }

    private static void intValueExact_helper(@NotNull Real input) {
        aeq(input.intValueExact(), input);
    }

    private static void intValueExact_fail_helper(@NotNull Real input) {
        try {
            input.intValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testIntValueExact() {
        intValueExact_helper(ONE);
        intValueExact_helper(ZERO);
        intValueExact_helper(NEGATIVE_ONE);
        intValueExact_helper(of(23));
        intValueExact_helper(of(8));

        intValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        intValueExact_fail_helper(SQRT_TWO);
        intValueExact_fail_helper(of(10000000000L));
    }

    private static void longValueExact_helper(@NotNull Real input) {
        aeq(input.longValueExact(), input);
    }

    private static void longValueExact_fail_helper(@NotNull Real input) {
        try {
            input.longValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testLongValueExact() {
        longValueExact_helper(ONE);
        longValueExact_helper(ZERO);
        longValueExact_helper(NEGATIVE_ONE);
        longValueExact_helper(of(23));
        longValueExact_helper(of(8));

        longValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        longValueExact_fail_helper(SQRT_TWO);
        longValueExact_fail_helper(of(new BigInteger("10000000000000000000")));
    }

    private static void isExactIntegerPowerOfTwo_helper(@NotNull Real input, boolean output) {
        aeq(input.isExactIntegerPowerOfTwo(), output);
    }

    @Test
    public void testIsExactIntegerPowerOfTwo() {
        isExactIntegerPowerOfTwo_helper(ZERO, false);
        isExactIntegerPowerOfTwo_helper(ONE, true);
        isExactIntegerPowerOfTwo_helper(ONE_HALF, true);
        isExactIntegerPowerOfTwo_helper(of(8), true);
        isExactIntegerPowerOfTwo_helper(of(Rational.of(1, 8)), true);
        isExactIntegerPowerOfTwo_helper(NEGATIVE_ONE, false);
        isExactIntegerPowerOfTwo_helper(NEGATIVE_FOUR_THIRDS, false);
        isExactIntegerPowerOfTwo_helper(SQRT_TWO, false);
        isExactIntegerPowerOfTwo_helper(E, false);
        isExactIntegerPowerOfTwo_helper(PI, false);
        isExactIntegerPowerOfTwo_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExactIntegerPowerOfTwo_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExactIntegerPowerOfTwo_helper(rightFuzzyRepresentation(Rational.ZERO), false);
    }

    private static void roundUpToIntegerPowerOfTwoUnsafe_helper(@NotNull Real input, @NotNull String output) {
        aeq(input.roundUpToIntegerPowerOfTwoUnsafe(), output);
    }

    private static void roundUpToIntegerPowerOfTwoUnsafe_fail_helper(@NotNull Real input) {
        try {
            input.roundUpToIntegerPowerOfTwoUnsafe();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundUpToIntegerPowerOfTwoUnsafe() {
        roundUpToIntegerPowerOfTwoUnsafe_helper(ONE, "1");
        roundUpToIntegerPowerOfTwoUnsafe_helper(ONE_HALF, "1 >> 1");
        roundUpToIntegerPowerOfTwoUnsafe_helper(of(8), "1 << 3");
        roundUpToIntegerPowerOfTwoUnsafe_helper(of(Rational.of(1, 8)), "1 >> 3");
        roundUpToIntegerPowerOfTwoUnsafe_helper(SQRT_TWO, "1 << 1");
        roundUpToIntegerPowerOfTwoUnsafe_helper(E, "1 << 2");
        roundUpToIntegerPowerOfTwoUnsafe_helper(PI, "1 << 2");
        roundUpToIntegerPowerOfTwoUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), "1");
        roundUpToIntegerPowerOfTwoUnsafe_helper(leftFuzzyRepresentation(Rational.TWO), "1 << 1");
        roundUpToIntegerPowerOfTwoUnsafe_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "1 >> 1");

        roundUpToIntegerPowerOfTwoUnsafe_fail_helper(ZERO);
        roundUpToIntegerPowerOfTwoUnsafe_fail_helper(NEGATIVE_ONE);
        roundUpToIntegerPowerOfTwoUnsafe_fail_helper(NEGATIVE_FOUR_THIRDS);
        roundUpToIntegerPowerOfTwoUnsafe_fail_helper(PI.negate());
        roundUpToIntegerPowerOfTwoUnsafe_fail_helper(leftFuzzyRepresentation(Rational.ZERO));
    }

    private static void roundUpToIntegerPowerOfTwo_helper(
            @NotNull Real x,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(x.roundUpToIntegerPowerOfTwo(resolution), output);
    }

    private static void roundUpToIntegerPowerOfTwo_fail_helper(@NotNull Real x, @NotNull Rational resolution) {
        try {
            x.roundUpToIntegerPowerOfTwo(resolution);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundUpToIntegerPowerOfTwo() {
        roundUpToIntegerPowerOfTwo_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        roundUpToIntegerPowerOfTwo_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[1 >> 1]");
        roundUpToIntegerPowerOfTwo_helper(of(8), DEFAULT_RESOLUTION, "Optional[1 << 3]");
        roundUpToIntegerPowerOfTwo_helper(of(Rational.of(1, 8)), DEFAULT_RESOLUTION, "Optional[1 >> 3]");
        roundUpToIntegerPowerOfTwo_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1 << 1]");
        roundUpToIntegerPowerOfTwo_helper(E, DEFAULT_RESOLUTION, "Optional[1 << 2]");
        roundUpToIntegerPowerOfTwo_helper(PI, DEFAULT_RESOLUTION, "Optional[1 << 2]");
        roundUpToIntegerPowerOfTwo_helper(leftFuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[1]");
        roundUpToIntegerPowerOfTwo_helper(leftFuzzyRepresentation(Rational.TWO), DEFAULT_RESOLUTION,
                "Optional[1 << 1]");
        roundUpToIntegerPowerOfTwo_helper(leftFuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION,
                "Optional[1 >> 1]");
        roundUpToIntegerPowerOfTwo_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional.empty");
        roundUpToIntegerPowerOfTwo_helper(fuzzyRepresentation(Rational.TWO), DEFAULT_RESOLUTION, "Optional.empty");
        roundUpToIntegerPowerOfTwo_helper(fuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION,
                "Optional.empty");

        roundUpToIntegerPowerOfTwo_helper(E, Rational.TEN, "Optional.empty");

        roundUpToIntegerPowerOfTwo_fail_helper(ZERO, DEFAULT_RESOLUTION);
        roundUpToIntegerPowerOfTwo_fail_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION);
        roundUpToIntegerPowerOfTwo_fail_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION);
        roundUpToIntegerPowerOfTwo_fail_helper(PI.negate(), DEFAULT_RESOLUTION);
        roundUpToIntegerPowerOfTwo_fail_helper(leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION);
    }

    private static void isExactBinaryFraction_helper(@NotNull Real input, boolean output) {
        aeq(input.isExactBinaryFraction(), output);
    }

    @Test
    public void testIsExactBinaryFraction() {
        isExactBinaryFraction_helper(ZERO, true);
        isExactBinaryFraction_helper(ONE, true);
        isExactBinaryFraction_helper(ONE_HALF, true);
        isExactBinaryFraction_helper(of(8), true);
        isExactBinaryFraction_helper(TEN, true);
        isExactBinaryFraction_helper(of(Rational.of(1, 8)), true);
        isExactBinaryFraction_helper(NEGATIVE_ONE, true);
        isExactBinaryFraction_helper(NEGATIVE_FOUR_THIRDS, false);
        isExactBinaryFraction_helper(SQRT_TWO, false);
        isExactBinaryFraction_helper(E, false);
        isExactBinaryFraction_helper(PI, false);
        isExactBinaryFraction_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExactBinaryFraction_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExactBinaryFraction_helper(rightFuzzyRepresentation(Rational.ZERO), false);
    }

    private static void binaryFractionValueExact_helper(@NotNull Real input, @NotNull String output) {
        aeq(input.binaryFractionValueExact(), output);
    }

    private static void binaryFractionValueExact_fail_helper(@NotNull Real input) {
        try {
            input.binaryFractionValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBinaryFractionValueExact() {
        binaryFractionValueExact_helper(ZERO, "0");
        binaryFractionValueExact_helper(ONE, "1");
        binaryFractionValueExact_helper(NEGATIVE_ONE, "-1");
        binaryFractionValueExact_helper(ONE_HALF, "1 >> 1");
        binaryFractionValueExact_helper(of(8), "1 << 3");
        binaryFractionValueExact_helper(of(10), "5 << 1");

        binaryFractionValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        binaryFractionValueExact_fail_helper(SQRT_TWO);
        binaryFractionValueExact_fail_helper(E);
        binaryFractionValueExact_fail_helper(PI);
        binaryFractionValueExact_fail_helper(fuzzyRepresentation(Rational.ZERO));
        binaryFractionValueExact_fail_helper(leftFuzzyRepresentation(Rational.ZERO));
        binaryFractionValueExact_fail_helper(rightFuzzyRepresentation(Rational.ZERO));
        binaryFractionValueExact_fail_helper(fuzzyRepresentation(Rational.ONE));
        binaryFractionValueExact_fail_helper(leftFuzzyRepresentation(Rational.ONE));
        binaryFractionValueExact_fail_helper(rightFuzzyRepresentation(Rational.ONE));
    }

    private static void isExact_helper(@NotNull Real input, boolean output) {
        aeq(input.isExact(), output);
    }

    @Test
    public void testIsExact() {
        isExact_helper(ZERO, true);
        isExact_helper(ONE, true);
        isExact_helper(NEGATIVE_ONE, true);
        isExact_helper(NEGATIVE_FOUR_THIRDS, true);
        isExact_helper(SQRT_TWO, false);
        isExact_helper(E, false);
        isExact_helper(PI, false);

        isExact_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExact_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExact_helper(rightFuzzyRepresentation(Rational.ZERO), false);
    }

    private static void rationalValueExact_helper(@NotNull Real input, @NotNull String output) {
        aeq(input.rationalValueExact(), output);
    }

    @Test
    public void testRationalValueExact() {
        rationalValueExact_helper(ZERO, "Optional[0]");
        rationalValueExact_helper(ONE, "Optional[1]");
        rationalValueExact_helper(NEGATIVE_ONE, "Optional[-1]");
        rationalValueExact_helper(NEGATIVE_FOUR_THIRDS, "Optional[-4/3]");
        rationalValueExact_helper(SQRT_TWO, "Optional.empty");
        rationalValueExact_helper(E, "Optional.empty");
        rationalValueExact_helper(PI, "Optional.empty");

        rationalValueExact_helper(fuzzyRepresentation(Rational.ZERO), "Optional.empty");
        rationalValueExact_helper(leftFuzzyRepresentation(Rational.ZERO), "Optional.empty");
        rationalValueExact_helper(rightFuzzyRepresentation(Rational.ZERO), "Optional.empty");
    }

    private static void binaryExponentUnsafe_helper(@NotNull Real input, int output) {
        aeq(input.binaryExponentUnsafe(), output);
    }

    private static void binaryExponentUnsafe_fail_helper(@NotNull Real input) {
        try {
            input.binaryExponentUnsafe();
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBinaryExponentUnsafe() {
        binaryExponentUnsafe_helper(ONE, 0);
        binaryExponentUnsafe_helper(ONE_HALF, -1);
        binaryExponentUnsafe_helper(of(8), 3);
        binaryExponentUnsafe_helper(of(Rational.of(1, 8)), -3);
        binaryExponentUnsafe_helper(SQRT_TWO, 0);
        binaryExponentUnsafe_helper(E, 1);
        binaryExponentUnsafe_helper(PI, 1);
        binaryExponentUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), 0);
        binaryExponentUnsafe_helper(rightFuzzyRepresentation(Rational.TWO), 1);
        binaryExponentUnsafe_helper(rightFuzzyRepresentation(Rational.ONE_HALF), -1);

        binaryExponentUnsafe_fail_helper(ZERO);
        binaryExponentUnsafe_fail_helper(NEGATIVE_ONE);
        binaryExponentUnsafe_fail_helper(NEGATIVE_FOUR_THIRDS);
        binaryExponentUnsafe_fail_helper(PI.negate());
        binaryExponentUnsafe_fail_helper(leftFuzzyRepresentation(Rational.ZERO));
    }

    private static void binaryExponent_helper(@NotNull Real x, @NotNull Rational resolution, @NotNull String output) {
        aeq(x.binaryExponent(resolution), output);
    }

    private static void binaryExponent_fail_helper(@NotNull Real x, @NotNull Rational resolution) {
        try {
            x.binaryExponent(resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBinaryExponent() {
        binaryExponent_helper(ONE, DEFAULT_RESOLUTION, "Optional[0]");
        binaryExponent_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[-1]");
        binaryExponent_helper(of(8), DEFAULT_RESOLUTION, "Optional[3]");
        binaryExponent_helper(of(Rational.of(1, 8)), DEFAULT_RESOLUTION, "Optional[-3]");
        binaryExponent_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[0]");
        binaryExponent_helper(E, DEFAULT_RESOLUTION, "Optional[1]");
        binaryExponent_helper(PI, DEFAULT_RESOLUTION, "Optional[1]");
        binaryExponent_helper(rightFuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[0]");
        binaryExponent_helper(rightFuzzyRepresentation(Rational.TWO), DEFAULT_RESOLUTION, "Optional[1]");
        binaryExponent_helper(rightFuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION, "Optional[-1]");
        binaryExponent_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional.empty");
        binaryExponent_helper(fuzzyRepresentation(Rational.TWO), DEFAULT_RESOLUTION, "Optional.empty");
        binaryExponent_helper(fuzzyRepresentation(Rational.ONE_HALF), DEFAULT_RESOLUTION, "Optional.empty");
        binaryExponent_helper(rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        binaryExponent_helper(E, Rational.TEN, "Optional.empty");

        binaryExponent_fail_helper(ZERO, DEFAULT_RESOLUTION);
        binaryExponent_fail_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION);
        binaryExponent_fail_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION);
        binaryExponent_fail_helper(PI.negate(), DEFAULT_RESOLUTION);
        binaryExponent_fail_helper(leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION);
    }

    private static void isExactAndEqualToFloat_helper(@NotNull Real input, boolean output) {
        aeq(input.isExactAndEqualToFloat(), output);
    }

    @Test
    public void testIsExactAndEqualToFloat() {
        isExactAndEqualToFloat_helper(ZERO, true);
        isExactAndEqualToFloat_helper(ONE, true);
        isExactAndEqualToFloat_helper(ONE_HALF, true);
        isExactAndEqualToFloat_helper(of(8), true);
        isExactAndEqualToFloat_helper(TEN, true);
        isExactAndEqualToFloat_helper(of(Rational.of(1, 8)), true);
        isExactAndEqualToFloat_helper(NEGATIVE_ONE, true);
        isExactAndEqualToFloat_helper(NEGATIVE_FOUR_THIRDS, false);
        isExactAndEqualToFloat_helper(SQRT_TWO, false);
        isExactAndEqualToFloat_helper(E, false);
        isExactAndEqualToFloat_helper(PI, false);
        isExactAndEqualToFloat_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExactAndEqualToFloat_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExactAndEqualToFloat_helper(rightFuzzyRepresentation(Rational.ZERO), false);
    }

    private static void isExactAndEqualToDouble_helper(@NotNull Real input, boolean output) {
        aeq(input.isExactAndEqualToDouble(), output);
    }

    @Test
    public void testIsExactAndEqualToDouble() {
        isExactAndEqualToDouble_helper(ZERO, true);
        isExactAndEqualToDouble_helper(ONE, true);
        isExactAndEqualToDouble_helper(ONE_HALF, true);
        isExactAndEqualToDouble_helper(of(8), true);
        isExactAndEqualToDouble_helper(TEN, true);
        isExactAndEqualToDouble_helper(of(Rational.of(1, 8)), true);
        isExactAndEqualToDouble_helper(NEGATIVE_ONE, true);
        isExactAndEqualToDouble_helper(NEGATIVE_FOUR_THIRDS, false);
        isExactAndEqualToDouble_helper(SQRT_TWO, false);
        isExactAndEqualToDouble_helper(E, false);
        isExactAndEqualToDouble_helper(PI, false);
        isExactAndEqualToDouble_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExactAndEqualToDouble_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExactAndEqualToDouble_helper(rightFuzzyRepresentation(Rational.ZERO), false);
    }

    private static void floatValueUnsafe_RoundingMode_helper(@NotNull Real x, @NotNull String rm, float output) {
        aeq(x.floatValueUnsafe(Readers.readRoundingModeStrict(rm).get()), output);
    }

    private static void floatValueUnsafe_RoundingMode_fail_helper(@NotNull Real x, @NotNull String rm) {
        try {
            x.floatValueUnsafe(Readers.readRoundingModeStrict(rm).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFloatValueUnsafe_RoundingMode() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0f)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        floatValueUnsafe_RoundingMode_helper(ZERO, "UP", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "UP", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "UP", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "UP", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "UP", -1.3333334f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "UP", 1.4142137f);
        floatValueUnsafe_RoundingMode_helper(E, "UP", 2.718282f);
        floatValueUnsafe_RoundingMode_helper(PI, "UP", 3.1415927f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE), "UP", 1.0f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "UP", -1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "UP", 1.0000001f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "UP", -1.0000001f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "DOWN", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "DOWN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "DOWN", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "DOWN", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "DOWN", -1.3333333f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "DOWN", 1.4142135f);
        floatValueUnsafe_RoundingMode_helper(E, "DOWN", 2.7182817f);
        floatValueUnsafe_RoundingMode_helper(PI, "DOWN", 3.1415925f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE), "DOWN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "DOWN", -1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "DOWN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "DOWN", -1.0f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "CEILING", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "CEILING", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "CEILING", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "CEILING", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "CEILING", -1.3333333f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "CEILING", 1.4142137f);
        floatValueUnsafe_RoundingMode_helper(E, "CEILING", 2.718282f);
        floatValueUnsafe_RoundingMode_helper(PI, "CEILING", 3.1415927f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE), "CEILING", 1.0f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "CEILING", -1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "CEILING", 1.0000001f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "CEILING", -1.0f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "FLOOR", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "FLOOR", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "FLOOR", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "FLOOR", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "FLOOR", -1.3333334f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "FLOOR", 1.4142135f);
        floatValueUnsafe_RoundingMode_helper(E, "FLOOR", 2.7182817f);
        floatValueUnsafe_RoundingMode_helper(PI, "FLOOR", 3.1415925f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "FLOOR", 0.0f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE), "FLOOR", 1.0f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "FLOOR", -1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "FLOOR", 1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "FLOOR", -1.0000001f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "HALF_UP", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "HALF_UP", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_UP", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_UP", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_UP", -1.3333334f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_UP", 1.4142135f);
        floatValueUnsafe_RoundingMode_helper(E, "HALF_UP", 2.7182817f);
        floatValueUnsafe_RoundingMode_helper(PI, "HALF_UP", 3.1415927f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "HALF_UP", 0.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_UP", 1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_UP", -1.0f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(positiveBetween), "HALF_UP", 1.0000001f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(negativeBetween), "HALF_UP", -1.0000001f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "HALF_DOWN", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "HALF_DOWN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_DOWN", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_DOWN", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_DOWN", -1.3333334f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_DOWN", 1.4142135f);
        floatValueUnsafe_RoundingMode_helper(E, "HALF_DOWN", 2.7182817f);
        floatValueUnsafe_RoundingMode_helper(PI, "HALF_DOWN", 3.1415927f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "HALF_DOWN", 0.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_DOWN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_DOWN", -1.0f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(positiveBetween), "HALF_DOWN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(negativeBetween), "HALF_DOWN", -1.0f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "HALF_EVEN", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "HALF_EVEN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_EVEN", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_EVEN", 0.5f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_EVEN", -1.3333334f);
        floatValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_EVEN", 1.4142135f);
        floatValueUnsafe_RoundingMode_helper(E, "HALF_EVEN", 2.7182817f);
        floatValueUnsafe_RoundingMode_helper(PI, "HALF_EVEN", 3.1415927f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "HALF_EVEN", 0.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_EVEN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_EVEN", -1.0f);
        floatValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(positiveBetween), "HALF_EVEN", 1.0f);
        floatValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(negativeBetween), "HALF_EVEN", -1.0f);

        floatValueUnsafe_RoundingMode_helper(ZERO, "UNNECESSARY", 0.0f);
        floatValueUnsafe_RoundingMode_helper(ONE, "UNNECESSARY", 1.0f);
        floatValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "UNNECESSARY", -1.0f);
        floatValueUnsafe_RoundingMode_helper(ONE_HALF, "UNNECESSARY", 0.5f);

        floatValueUnsafe_RoundingMode_fail_helper(NEGATIVE_FOUR_THIRDS, "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(SQRT_TWO, "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(E, "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(PI, "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.ZERO), "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.ONE), "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(positiveBetween), "UNNECESSARY");
        floatValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(negativeBetween), "UNNECESSARY");
    }

    private static void floatValue_RoundingMode_Rational_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull Rational r,
            @NotNull String output
    ) {
        aeq(x.floatValue(Readers.readRoundingModeStrict(rm).get(), r), output);
    }

    private static void floatValue_RoundingMode_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull Rational r
    ) {
        try {
            x.floatValue(Readers.readRoundingModeStrict(rm).get(), r);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFloatValue_RoundingMode_Rational() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0f)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        floatValue_RoundingMode_Rational_helper(ZERO, "UP", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "UP", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "UP", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "UP", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333334]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "UP", DEFAULT_RESOLUTION, "Optional[1.4142137]");
        floatValue_RoundingMode_Rational_helper(E, "UP", DEFAULT_RESOLUTION, "Optional[2.718282]");
        floatValue_RoundingMode_Rational_helper(PI, "UP", DEFAULT_RESOLUTION, "Optional[3.1415927]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1.0000001]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.0000001]"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "DOWN", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "DOWN", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "DOWN", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "DOWN", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "DOWN", DEFAULT_RESOLUTION, "Optional[1.4142135]");
        floatValue_RoundingMode_Rational_helper(E, "DOWN", DEFAULT_RESOLUTION, "Optional[2.7182817]");
        floatValue_RoundingMode_Rational_helper(PI, "DOWN", DEFAULT_RESOLUTION, "Optional[3.1415925]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "CEILING", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "CEILING", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "CEILING", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "CEILING", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "CEILING", DEFAULT_RESOLUTION, "Optional[1.4142137]");
        floatValue_RoundingMode_Rational_helper(E, "CEILING", DEFAULT_RESOLUTION, "Optional[2.718282]");
        floatValue_RoundingMode_Rational_helper(PI, "CEILING", DEFAULT_RESOLUTION, "Optional[3.1415927]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.0000001]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "FLOOR", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "FLOOR", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "FLOOR", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "FLOOR", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333334]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "FLOOR", DEFAULT_RESOLUTION, "Optional[1.4142135]");
        floatValue_RoundingMode_Rational_helper(E, "FLOOR", DEFAULT_RESOLUTION, "Optional[2.7182817]");
        floatValue_RoundingMode_Rational_helper(PI, "FLOOR", DEFAULT_RESOLUTION, "Optional[3.1415925]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "FLOOR", DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "FLOOR", DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "FLOOR", DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "FLOOR", DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "FLOOR", DEFAULT_RESOLUTION,
                "Optional[-1.0000001]"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "HALF_UP", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_UP", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_UP", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333334]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1.4142135]");
        floatValue_RoundingMode_Rational_helper(E, "HALF_UP", DEFAULT_RESOLUTION, "Optional[2.7182817]");
        floatValue_RoundingMode_Rational_helper(PI, "HALF_UP", DEFAULT_RESOLUTION, "Optional[3.1415927]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333334]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[1.4142135]");
        floatValue_RoundingMode_Rational_helper(E, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[2.7182817]");
        floatValue_RoundingMode_Rational_helper(PI, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[3.1415927]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333334]"
        );
        floatValue_RoundingMode_Rational_helper(SQRT_TWO, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[1.4142135]");
        floatValue_RoundingMode_Rational_helper(E, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[2.7182817]");
        floatValue_RoundingMode_Rational_helper(PI, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3.1415927]");
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        floatValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        floatValue_RoundingMode_Rational_helper(ZERO, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_RoundingMode_Rational_helper(ONE, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_RoundingMode_Rational_helper(ONE_HALF, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[0.5]");

        floatValue_RoundingMode_Rational_helper(PI, "FLOOR", Rational.ONE, "Optional.empty");

        floatValue_RoundingMode_Rational_fail_helper(ZERO, "HALF_EVEN", Rational.ZERO);
        floatValue_RoundingMode_Rational_fail_helper(ZERO, "HALF_EVEN", Rational.NEGATIVE_ONE);

        floatValue_RoundingMode_Rational_fail_helper(NEGATIVE_FOUR_THIRDS, "UNNECESSARY", DEFAULT_RESOLUTION);
        floatValue_RoundingMode_Rational_fail_helper(SQRT_TWO, "UNNECESSARY", DEFAULT_RESOLUTION);
        floatValue_RoundingMode_Rational_fail_helper(E, "UNNECESSARY", DEFAULT_RESOLUTION);
        floatValue_RoundingMode_Rational_fail_helper(PI, "UNNECESSARY", DEFAULT_RESOLUTION);
        floatValue_RoundingMode_Rational_fail_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
        floatValue_RoundingMode_Rational_fail_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
        floatValue_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
    }

    private static void floatValueUnsafe_helper(@NotNull Real x, float output) {
        aeq(x.floatValueUnsafe(), output);
    }

    @Test
    public void testFloatValueUnsafe() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0f)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        floatValueUnsafe_helper(ZERO, 0.0f);
        floatValueUnsafe_helper(ONE, 1.0f);
        floatValueUnsafe_helper(NEGATIVE_ONE, -1.0f);
        floatValueUnsafe_helper(ONE_HALF, 0.5f);
        floatValueUnsafe_helper(NEGATIVE_FOUR_THIRDS, -1.3333334f);
        floatValueUnsafe_helper(SQRT_TWO, 1.4142135f);
        floatValueUnsafe_helper(E, 2.7182817f);
        floatValueUnsafe_helper(PI, 3.1415927f);
        floatValueUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), 0.0f);
        floatValueUnsafe_helper(fuzzyRepresentation(Rational.ONE), 1.0f);
        floatValueUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), -1.0f);
        floatValueUnsafe_helper(leftFuzzyRepresentation(positiveBetween), 1.0f);
        floatValueUnsafe_helper(rightFuzzyRepresentation(negativeBetween), -1.0f);
    }

    private static void floatValue_Rational_helper(@NotNull Real x, @NotNull Rational r, @NotNull String output) {
        aeq(x.floatValue(r), output);
    }

    private static void floatValue_Rational_fail_helper(@NotNull Real x, @NotNull Rational r) {
        try {
            x.floatValue(r);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFloatValue_Rational() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0f)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        floatValue_Rational_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0.0]");
        floatValue_Rational_helper(ONE, DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_Rational_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_Rational_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[0.5]");
        floatValue_Rational_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-1.3333334]");
        floatValue_Rational_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1.4142135]");
        floatValue_Rational_helper(E, DEFAULT_RESOLUTION, "Optional[2.7182817]");
        floatValue_Rational_helper(PI, DEFAULT_RESOLUTION, "Optional[3.1415927]");
        floatValue_Rational_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        floatValue_Rational_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[1.0]");
        floatValue_Rational_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), DEFAULT_RESOLUTION, "Optional[-1.0]");
        floatValue_Rational_helper(fuzzyRepresentation(positiveBetween), DEFAULT_RESOLUTION, "Optional.empty");
        floatValue_Rational_helper(fuzzyRepresentation(negativeBetween), DEFAULT_RESOLUTION, "Optional.empty");

        floatValue_Rational_helper(PI, Rational.ONE, "Optional.empty");

        floatValue_Rational_fail_helper(ZERO, Rational.ZERO);
        floatValue_Rational_fail_helper(ZERO, Rational.NEGATIVE_ONE);
    }

    private static void floatValueExact_helper(@NotNull Real x, float output) {
        aeq(x.floatValueExact(), output);
    }

    private static void floatValueExact_fail_helper(@NotNull Real x) {
        try {
            x.floatValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testFloatValueExact() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0f)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        floatValueExact_helper(ZERO, 0.0f);
        floatValueExact_helper(ONE, 1.0f);
        floatValueExact_helper(NEGATIVE_ONE, -1.0f);
        floatValueExact_helper(ONE_HALF, 0.5f);

        floatValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        floatValueExact_fail_helper(SQRT_TWO);
        floatValueExact_fail_helper(E);
        floatValueExact_fail_helper(PI);
        floatValueExact_fail_helper(fuzzyRepresentation(Rational.ZERO));
        floatValueExact_fail_helper(fuzzyRepresentation(Rational.ONE));
        floatValueExact_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE));
        floatValueExact_fail_helper(fuzzyRepresentation(positiveBetween));
        floatValueExact_fail_helper(fuzzyRepresentation(negativeBetween));
    }

    private static void doubleValueUnsafe_RoundingMode_helper(@NotNull Real x, @NotNull String rm, double output) {
        aeq(x.doubleValueUnsafe(Readers.readRoundingModeStrict(rm).get()), output);
    }

    private static void doubleValueUnsafe_RoundingMode_fail_helper(@NotNull Real x, @NotNull String rm) {
        try {
            x.doubleValueUnsafe(Readers.readRoundingModeStrict(rm).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDoubleValueUnsafe_RoundingMode() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        doubleValueUnsafe_RoundingMode_helper(ZERO, "UP", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "UP", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "UP", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "UP", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "UP", -1.3333333333333335);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "UP", 1.4142135623730951);
        doubleValueUnsafe_RoundingMode_helper(E, "UP", 2.7182818284590455);
        doubleValueUnsafe_RoundingMode_helper(PI, "UP", 3.1415926535897936);
        doubleValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE), "UP", 1.0);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "UP", -1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "UP", 1.0000000000000002);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "UP", -1.0000000000000002);

        doubleValueUnsafe_RoundingMode_helper(ZERO, "DOWN", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "DOWN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "DOWN", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "DOWN", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "DOWN", -1.3333333333333333);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "DOWN", 1.414213562373095);
        doubleValueUnsafe_RoundingMode_helper(E, "DOWN", 2.718281828459045);
        doubleValueUnsafe_RoundingMode_helper(PI, "DOWN", 3.141592653589793);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE), "DOWN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "DOWN", -1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "DOWN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "DOWN", -1.0);

        doubleValueUnsafe_RoundingMode_helper(ZERO, "CEILING", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "CEILING", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "CEILING", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "CEILING", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "CEILING", -1.3333333333333333);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "CEILING", 1.4142135623730951);
        doubleValueUnsafe_RoundingMode_helper(E, "CEILING", 2.7182818284590455);
        doubleValueUnsafe_RoundingMode_helper(PI, "CEILING", 3.1415926535897936);
        doubleValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.ONE), "CEILING", 1.0);
        doubleValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "CEILING", -1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "CEILING", 1.0000000000000002);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "CEILING", -1.0);

        doubleValueUnsafe_RoundingMode_helper(ZERO, "FLOOR", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "FLOOR", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "FLOOR", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "FLOOR", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "FLOOR", -1.3333333333333335);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "FLOOR", 1.414213562373095);
        doubleValueUnsafe_RoundingMode_helper(E, "FLOOR", 2.718281828459045);
        doubleValueUnsafe_RoundingMode_helper(PI, "FLOOR", 3.141592653589793);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "FLOOR", 0.0);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ONE), "FLOOR", 1.0);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "FLOOR", -1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(positiveBetween), "FLOOR", 1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(negativeBetween), "FLOOR", -1.0000000000000002);

        doubleValueUnsafe_RoundingMode_helper(ZERO, "HALF_UP", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "HALF_UP", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_UP", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_UP", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_UP", -1.3333333333333333);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_UP", 1.4142135623730951);
        doubleValueUnsafe_RoundingMode_helper(E, "HALF_UP", 2.718281828459045);
        doubleValueUnsafe_RoundingMode_helper(PI, "HALF_UP", 3.141592653589793);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "HALF_UP", 0.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_UP", 1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_UP", -1.0);
        doubleValueUnsafe_RoundingMode_helper(
                rightFuzzyRepresentation(positiveBetween),
                "HALF_UP",
                1.0000000000000002
        );
        doubleValueUnsafe_RoundingMode_helper(
                leftFuzzyRepresentation(negativeBetween),
                "HALF_UP",
                -1.0000000000000002
        );

        doubleValueUnsafe_RoundingMode_helper(ZERO, "HALF_DOWN", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "HALF_DOWN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_DOWN", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_DOWN", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_DOWN", -1.3333333333333333);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_DOWN", 1.4142135623730951);
        doubleValueUnsafe_RoundingMode_helper(E, "HALF_DOWN", 2.718281828459045);
        doubleValueUnsafe_RoundingMode_helper(PI, "HALF_DOWN", 3.141592653589793);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "HALF_DOWN", 0.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_DOWN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_DOWN", -1.0);
        doubleValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(positiveBetween), "HALF_DOWN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(negativeBetween), "HALF_DOWN", -1.0);

        doubleValueUnsafe_RoundingMode_helper(ZERO, "HALF_EVEN", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "HALF_EVEN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "HALF_EVEN", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "HALF_EVEN", 0.5);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, "HALF_EVEN", -1.3333333333333333);
        doubleValueUnsafe_RoundingMode_helper(SQRT_TWO, "HALF_EVEN", 1.4142135623730951);
        doubleValueUnsafe_RoundingMode_helper(E, "HALF_EVEN", 2.718281828459045);
        doubleValueUnsafe_RoundingMode_helper(PI, "HALF_EVEN", 3.141592653589793);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), "HALF_EVEN", 0.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.ONE), "HALF_EVEN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "HALF_EVEN", -1.0);
        doubleValueUnsafe_RoundingMode_helper(leftFuzzyRepresentation(positiveBetween), "HALF_EVEN", 1.0);
        doubleValueUnsafe_RoundingMode_helper(rightFuzzyRepresentation(negativeBetween), "HALF_EVEN", -1.0);

        doubleValueUnsafe_RoundingMode_helper(ZERO, "UNNECESSARY", 0.0);
        doubleValueUnsafe_RoundingMode_helper(ONE, "UNNECESSARY", 1.0);
        doubleValueUnsafe_RoundingMode_helper(NEGATIVE_ONE, "UNNECESSARY", -1.0);
        doubleValueUnsafe_RoundingMode_helper(ONE_HALF, "UNNECESSARY", 0.5);

        doubleValueUnsafe_RoundingMode_fail_helper(NEGATIVE_FOUR_THIRDS, "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(SQRT_TWO, "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(E, "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(PI, "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.ZERO), "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.ONE), "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(positiveBetween), "UNNECESSARY");
        doubleValueUnsafe_RoundingMode_fail_helper(fuzzyRepresentation(negativeBetween), "UNNECESSARY");
    }

    private static void doubleValue_RoundingMode_Rational_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull Rational r,
            @NotNull String output
    ) {
        aeq(x.doubleValue(Readers.readRoundingModeStrict(rm).get(), r), output);
    }

    private static void doubleValue_RoundingMode_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String rm,
            @NotNull Rational r
    ) {
        try {
            x.doubleValue(Readers.readRoundingModeStrict(rm).get(), r);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testDoubleValue_RoundingMode_Rational() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        doubleValue_RoundingMode_Rational_helper(ZERO, "UP", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "UP", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "UP", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "UP", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333335]"
        );
        doubleValue_RoundingMode_Rational_helper(SQRT_TWO, "UP", DEFAULT_RESOLUTION, "Optional[1.4142135623730951]");
        doubleValue_RoundingMode_Rational_helper(E, "UP", DEFAULT_RESOLUTION, "Optional[2.7182818284590455]");
        doubleValue_RoundingMode_Rational_helper(PI, "UP", DEFAULT_RESOLUTION, "Optional[3.1415926535897936]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1.0000000000000002]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.0000000000000002]"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "DOWN", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "DOWN", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "DOWN", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "DOWN", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333333]"
        );
        doubleValue_RoundingMode_Rational_helper(SQRT_TWO, "DOWN", DEFAULT_RESOLUTION, "Optional[1.414213562373095]");
        doubleValue_RoundingMode_Rational_helper(E, "DOWN", DEFAULT_RESOLUTION, "Optional[2.718281828459045]");
        doubleValue_RoundingMode_Rational_helper(PI, "DOWN", DEFAULT_RESOLUTION, "Optional[3.141592653589793]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "CEILING", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "CEILING", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "CEILING", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "CEILING", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333333]"
        );
        doubleValue_RoundingMode_Rational_helper(
                SQRT_TWO,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.4142135623730951]"
        );
        doubleValue_RoundingMode_Rational_helper(E, "CEILING", DEFAULT_RESOLUTION, "Optional[2.7182818284590455]");
        doubleValue_RoundingMode_Rational_helper(PI, "CEILING", DEFAULT_RESOLUTION, "Optional[3.1415926535897936]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.0000000000000002]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "FLOOR", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "FLOOR", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "FLOOR", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "FLOOR", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333335]"
        );
        doubleValue_RoundingMode_Rational_helper(SQRT_TWO, "FLOOR", DEFAULT_RESOLUTION, "Optional[1.414213562373095]");
        doubleValue_RoundingMode_Rational_helper(E, "FLOOR", DEFAULT_RESOLUTION, "Optional[2.718281828459045]");
        doubleValue_RoundingMode_Rational_helper(PI, "FLOOR", DEFAULT_RESOLUTION, "Optional[3.141592653589793]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.0000000000000002]"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "HALF_UP", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_UP", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_UP", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333333]"
        );
        doubleValue_RoundingMode_Rational_helper(
                SQRT_TWO,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1.4142135623730951]"
        );
        doubleValue_RoundingMode_Rational_helper(E, "HALF_UP", DEFAULT_RESOLUTION, "Optional[2.718281828459045]");
        doubleValue_RoundingMode_Rational_helper(PI, "HALF_UP", DEFAULT_RESOLUTION, "Optional[3.141592653589793]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333333]"
        );
        doubleValue_RoundingMode_Rational_helper(
                SQRT_TWO,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.4142135623730951]"
        );
        doubleValue_RoundingMode_Rational_helper(E, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[2.718281828459045]");
        doubleValue_RoundingMode_Rational_helper(PI, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[3.141592653589793]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3333333333333333]"
        );
        doubleValue_RoundingMode_Rational_helper(
                SQRT_TWO,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1.4142135623730951]"
        );
        doubleValue_RoundingMode_Rational_helper(E, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[2.718281828459045]");
        doubleValue_RoundingMode_Rational_helper(PI, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3.141592653589793]");
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ONE),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.NEGATIVE_ONE),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.0]"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(positiveBetween),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        doubleValue_RoundingMode_Rational_helper(
                fuzzyRepresentation(negativeBetween),
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        doubleValue_RoundingMode_Rational_helper(ZERO, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_RoundingMode_Rational_helper(ONE, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_RoundingMode_Rational_helper(NEGATIVE_ONE, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_RoundingMode_Rational_helper(ONE_HALF, "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[0.5]");

        doubleValue_RoundingMode_Rational_helper(PI, "FLOOR", Rational.ONE, "Optional.empty");

        doubleValue_RoundingMode_Rational_fail_helper(ZERO, "HALF_EVEN", Rational.ZERO);
        doubleValue_RoundingMode_Rational_fail_helper(ZERO, "HALF_EVEN", Rational.NEGATIVE_ONE);

        doubleValue_RoundingMode_Rational_fail_helper(NEGATIVE_FOUR_THIRDS, "UNNECESSARY", DEFAULT_RESOLUTION);
        doubleValue_RoundingMode_Rational_fail_helper(SQRT_TWO, "UNNECESSARY", DEFAULT_RESOLUTION);
        doubleValue_RoundingMode_Rational_fail_helper(E, "UNNECESSARY", DEFAULT_RESOLUTION);
        doubleValue_RoundingMode_Rational_fail_helper(PI, "UNNECESSARY", DEFAULT_RESOLUTION);
        doubleValue_RoundingMode_Rational_fail_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
        doubleValue_RoundingMode_Rational_fail_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
        doubleValue_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );
    }

    private static void doubleValueUnsafe_helper(@NotNull Real x, double output) {
        aeq(x.doubleValueUnsafe(), output);
    }

    @Test
    public void testDoubleValueUnsafe() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        doubleValueUnsafe_helper(ZERO, 0.0);
        doubleValueUnsafe_helper(ONE, 1.0);
        doubleValueUnsafe_helper(NEGATIVE_ONE, -1.0);
        doubleValueUnsafe_helper(ONE_HALF, 0.5);
        doubleValueUnsafe_helper(NEGATIVE_FOUR_THIRDS, -1.3333333333333333);
        doubleValueUnsafe_helper(SQRT_TWO, 1.4142135623730951);
        doubleValueUnsafe_helper(E, 2.718281828459045);
        doubleValueUnsafe_helper(PI, 3.141592653589793);
        doubleValueUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), 0.0);
        doubleValueUnsafe_helper(fuzzyRepresentation(Rational.ONE), 1.0);
        doubleValueUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), -1.0);
        doubleValueUnsafe_helper(leftFuzzyRepresentation(positiveBetween), 1.0);
        doubleValueUnsafe_helper(rightFuzzyRepresentation(negativeBetween), -1.0);
    }

    private static void doubleValue_Rational_helper(@NotNull Real x, @NotNull Rational r, @NotNull String output) {
        aeq(x.doubleValue(r), output);
    }

    private static void doubleValue_Rational_fail_helper(@NotNull Real x, @NotNull Rational r) {
        try {
            x.doubleValue(r);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testDoubleValue_Rational() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        doubleValue_Rational_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0.0]");
        doubleValue_Rational_helper(ONE, DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_Rational_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_Rational_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[0.5]");
        doubleValue_Rational_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-1.3333333333333333]");
        doubleValue_Rational_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1.4142135623730951]");
        doubleValue_Rational_helper(E, DEFAULT_RESOLUTION, "Optional[2.718281828459045]");
        doubleValue_Rational_helper(PI, DEFAULT_RESOLUTION, "Optional[3.141592653589793]");
        doubleValue_Rational_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        doubleValue_Rational_helper(fuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[1.0]");
        doubleValue_Rational_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), DEFAULT_RESOLUTION, "Optional[-1.0]");
        doubleValue_Rational_helper(fuzzyRepresentation(positiveBetween), DEFAULT_RESOLUTION, "Optional.empty");
        doubleValue_Rational_helper(fuzzyRepresentation(negativeBetween), DEFAULT_RESOLUTION, "Optional.empty");

        doubleValue_Rational_helper(PI, Rational.ONE, "Optional.empty");

        doubleValue_Rational_fail_helper(ZERO, Rational.ZERO);
        doubleValue_Rational_fail_helper(ZERO, Rational.NEGATIVE_ONE);
    }

    private static void doubleValueExact_helper(@NotNull Real x, double output) {
        aeq(x.doubleValueExact(), output);
    }

    private static void doubleValueExact_fail_helper(@NotNull Real x) {
        try {
            x.doubleValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDoubleValueExact() {
        Rational positiveBetween =
                Rational.ONE.add(Rational.ofExact(FloatingPointUtils.successor(1.0)).get()).shiftRight(1);
        Rational negativeBetween = positiveBetween.negate();

        doubleValueExact_helper(ZERO, 0.0);
        doubleValueExact_helper(ONE, 1.0);
        doubleValueExact_helper(NEGATIVE_ONE, -1.0);
        doubleValueExact_helper(ONE_HALF, 0.5);

        doubleValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        doubleValueExact_fail_helper(SQRT_TWO);
        doubleValueExact_fail_helper(E);
        doubleValueExact_fail_helper(PI);
        doubleValueExact_fail_helper(fuzzyRepresentation(Rational.ZERO));
        doubleValueExact_fail_helper(fuzzyRepresentation(Rational.ONE));
        doubleValueExact_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE));
        doubleValueExact_fail_helper(fuzzyRepresentation(positiveBetween));
        doubleValueExact_fail_helper(fuzzyRepresentation(negativeBetween));
    }

    private static void bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
            @NotNull Real x,
            int precision,
            @NotNull String rm,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByPrecisionUnsafe(precision, Readers.readRoundingModeStrict(rm).get()), output);
    }

    private static void bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
            @NotNull Real x,
            int precision,
            @NotNull String roundingMode
    ) {
        try {
            x.bigDecimalValueByPrecisionUnsafe(precision, Readers.readRoundingModeStrict(roundingMode).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByPrecisionUnsafe_int_RoundingMode() {
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "FLOOR", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "CEILING", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "DOWN", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "UP", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "HALF_DOWN", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "HALF_UP", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "HALF_EVEN", "0");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 0, "UNNECESSARY", "0");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "FLOOR", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "CEILING", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "DOWN", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "UP", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "HALF_DOWN", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "HALF_UP", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "HALF_EVEN", "0.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ZERO, 4, "UNNECESSARY", "0.000");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "FLOOR", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "CEILING", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "DOWN", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "UP", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "HALF_DOWN", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "HALF_UP", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "HALF_EVEN", "1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 0, "UNNECESSARY", "1");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "FLOOR", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "CEILING", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "DOWN", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "UP", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "HALF_DOWN", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "HALF_UP", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "HALF_EVEN", "1.000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE, 4, "UNNECESSARY", "1.000");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "FLOOR", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "CEILING", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "DOWN", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "UP", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "HALF_DOWN", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "HALF_UP", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "HALF_EVEN", "0.5");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "UNNECESSARY", "0.5");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "FLOOR", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "CEILING", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "DOWN", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "UP", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "HALF_DOWN", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "HALF_UP", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "HALF_EVEN", "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "UNNECESSARY", "0.5000");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "FLOOR", "-2");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "CEILING", "-1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "DOWN", "-1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "UP", "-2");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "HALF_DOWN", "-1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "HALF_UP", "-1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "HALF_EVEN", "-1");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "FLOOR", "-1.4");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "CEILING", "-1.3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "DOWN", "-1.3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "UP", "-1.4");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "HALF_DOWN", "-1.3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "HALF_UP", "-1.3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "HALF_EVEN", "-1.3");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "FLOOR", "-1.34");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "CEILING", "-1.33");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "DOWN", "-1.33");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "UP", "-1.34");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "HALF_DOWN", "-1.33");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "HALF_UP", "-1.33");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "HALF_EVEN", "-1.33");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "FLOOR", "-1.334");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "CEILING", "-1.333");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "DOWN", "-1.333");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "UP", "-1.334");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "HALF_DOWN", "-1.333");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "HALF_UP", "-1.333");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 4, "HALF_EVEN", "-1.333");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "FLOOR", "3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "CEILING", "4");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "DOWN", "3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "UP", "4");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "HALF_DOWN", "3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "HALF_UP", "3");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 1, "HALF_EVEN", "3");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "FLOOR", "3.1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "CEILING", "3.2");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "DOWN", "3.1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "UP", "3.2");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "HALF_DOWN", "3.1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "HALF_UP", "3.1");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 2, "HALF_EVEN", "3.1");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "FLOOR", "3.14");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "CEILING", "3.15");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "DOWN", "3.14");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "UP", "3.15");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "HALF_DOWN", "3.14");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "HALF_UP", "3.14");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 3, "HALF_EVEN", "3.14");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "FLOOR", "3.141");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "CEILING", "3.142");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "DOWN", "3.141");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "UP", "3.142");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "HALF_DOWN", "3.142");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "HALF_UP", "3.142");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(PI, 4, "HALF_EVEN", "3.142");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(123)),
                3,
                "UP",
                "123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(123)),
                3,
                "DOWN",
                "123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(123)),
                3,
                "CEILING",
                "123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(123)),
                3,
                "FLOOR",
                "123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "HALF_UP",
                "123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "HALF_DOWN",
                "123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "HALF_EVEN",
                "123"
        );

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(-123)),
                3,
                "UP",
                "-123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-123)),
                3,
                "DOWN",
                "-123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-123)),
                3,
                "CEILING",
                "-123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(-123)),
                3,
                "FLOOR",
                "-123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "HALF_UP",
                "-123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "HALF_DOWN",
                "-123"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "HALF_EVEN",
                "-123"
        );

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "UP",
                "1.24E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "DOWN",
                "1.23E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "CEILING",
                "1.24E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "FLOOR",
                "1.23E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(1235)),
                3,
                "HALF_UP",
                "1.24E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(1235)),
                3,
                "HALF_DOWN",
                "1.23E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(1235)),
                3,
                "HALF_EVEN",
                "1.24E+3"
        );

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "UP",
                "-1.24E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "DOWN",
                "-1.23E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "CEILING",
                "-1.23E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "FLOOR",
                "-1.24E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-1235)),
                3,
                "HALF_UP",
                "-1.24E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(-1235)),
                3,
                "HALF_DOWN",
                "-1.23E+3"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-1235)),
                3,
                "HALF_EVEN",
                "-1.24E+3"
        );

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "FLOOR");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "CEILING");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "DOWN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "UP");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "HALF_DOWN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "HALF_UP");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "HALF_EVEN");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 0, "UNNECESSARY");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 1, "UNNECESSARY");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 2, "UNNECESSARY");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 3, "UNNECESSARY");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 4, "UNNECESSARY");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, 5, "UNNECESSARY");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "FLOOR");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "CEILING");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "DOWN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "UP");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "HALF_DOWN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "HALF_UP");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "HALF_EVEN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(ONE, -1, "UNNECESSARY");

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "FLOOR");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "CEILING");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "DOWN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "UP");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "HALF_DOWN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "HALF_UP");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "HALF_EVEN");
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(PI, -1, "UNNECESSARY");
    }

    private static void bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
            @NotNull Real x,
            int precision,
            @NotNull String rm,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByPrecision(precision, Readers.readRoundingModeStrict(rm).get(), resolution), output);
    }

    private static void bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
            @NotNull Real x,
            int precision,
            @NotNull String roundingMode,
            @NotNull Rational resolution
    ) {
        try {
            x.bigDecimalValueByPrecision(precision, Readers.readRoundingModeStrict(roundingMode).get(), resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByPrecision_int_RoundingMode_Rational() {
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ZERO,
                4,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE,
                4,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0.5]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.4]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.4]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.34]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.34]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.334]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.334]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(PI, 1, "FLOOR", DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                1,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[4]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(PI, 1, "DOWN", DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(PI, 1, "UP", DEFAULT_RESOLUTION, "Optional[4]");
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                1,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                1,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                1,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                2,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                2,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[3.2]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                2,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(PI, 2, "UP", DEFAULT_RESOLUTION, "Optional[3.2]");
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                2,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                2,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                2,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[3.15]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(PI, 3, "UP", DEFAULT_RESOLUTION, "Optional[3.15]");
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[3.141]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.141]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                PI,
                4,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1.24E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.23E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.24E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1.23E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION, "Optional.empty"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.24E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.23E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.23E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.24E+3]"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "FLOOR", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "CEILING", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "DOWN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "UP", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "HALF_DOWN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "HALF_UP", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "HALF_EVEN", DEFAULT_RESOLUTION);

        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 0, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 1, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 2, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 3, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 4, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, 5, "UNNECESSARY", DEFAULT_RESOLUTION);

        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "FLOOR", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "CEILING", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "DOWN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "UP", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "HALF_DOWN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "HALF_UP", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "HALF_EVEN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ONE, -1, "UNNECESSARY", DEFAULT_RESOLUTION);

        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "FLOOR", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "CEILING", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "DOWN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "UP", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "HALF_DOWN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "HALF_UP", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "HALF_EVEN", DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(PI, -1, "UNNECESSARY", DEFAULT_RESOLUTION);

        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ZERO, 0, "FLOOR", Rational.ZERO);
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(ZERO, 0, "FLOOR", Rational.NEGATIVE_ONE);
    }

    private static void bigDecimalValueByPrecisionUnsafe_int_helper(
            @NotNull Real x,
            int precision,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByPrecisionUnsafe(precision), output);
    }

    private static void bigDecimalValueByPrecisionUnsafe_int_fail_helper(@NotNull Real x, int precision) {
        try {
            x.bigDecimalValueByPrecisionUnsafe(precision);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByPrecisionUnsafe_int() {
        bigDecimalValueByPrecisionUnsafe_int_helper(ZERO, 0, "0");
        bigDecimalValueByPrecisionUnsafe_int_helper(ZERO, 4, "0.000");
        bigDecimalValueByPrecisionUnsafe_int_helper(ONE, 0, "1");
        bigDecimalValueByPrecisionUnsafe_int_helper(ONE, 4, "1.000");
        bigDecimalValueByPrecisionUnsafe_int_helper(ONE_HALF, 0, "0.5");
        bigDecimalValueByPrecisionUnsafe_int_helper(ONE_HALF, 4, "0.5000");
        bigDecimalValueByPrecisionUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 1, "-1");
        bigDecimalValueByPrecisionUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 2, "-1.3");
        bigDecimalValueByPrecisionUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 3, "-1.33");
        bigDecimalValueByPrecisionUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 4, "-1.333");
        bigDecimalValueByPrecisionUnsafe_int_helper(PI, 1, "3");
        bigDecimalValueByPrecisionUnsafe_int_helper(PI, 2, "3.1");
        bigDecimalValueByPrecisionUnsafe_int_helper(PI, 3, "3.14");
        bigDecimalValueByPrecisionUnsafe_int_helper(PI, 4, "3.142");

        bigDecimalValueByPrecisionUnsafe_int_helper(fuzzyRepresentation(Rational.of(123)), 3, "123");
        bigDecimalValueByPrecisionUnsafe_int_helper(fuzzyRepresentation(Rational.of(-123)), 3, "-123");
        bigDecimalValueByPrecisionUnsafe_int_helper(rightFuzzyRepresentation(Rational.of(1235)), 3, "1.24E+3");
        bigDecimalValueByPrecisionUnsafe_int_helper(leftFuzzyRepresentation(Rational.of(-1235)), 3, "-1.24E+3");

        bigDecimalValueByPrecisionUnsafe_int_fail_helper(PI, 0);
        bigDecimalValueByPrecisionUnsafe_int_fail_helper(ONE, -1);
        bigDecimalValueByPrecisionUnsafe_int_fail_helper(PI, -1);
    }

    private static void bigDecimalValueByPrecision_int_Rational_helper(
            @NotNull Real x,
            int precision,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByPrecision(precision, resolution), output);
    }

    private static void bigDecimalValueByPrecision_int_Rational_fail_helper(
            @NotNull Real x,
            int precision,
            @NotNull Rational resolution
    ) {
        try {
            x.bigDecimalValueByPrecision(precision, resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByPrecision_int_Rational() {
        bigDecimalValueByPrecision_int_Rational_helper(ZERO, 0, DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByPrecision_int_Rational_helper(ZERO, 4, DEFAULT_RESOLUTION, "Optional[0.000]");
        bigDecimalValueByPrecision_int_Rational_helper(ONE, 0, DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByPrecision_int_Rational_helper(ONE, 4, DEFAULT_RESOLUTION, "Optional[1.000]");
        bigDecimalValueByPrecision_int_Rational_helper(ONE_HALF, 0, DEFAULT_RESOLUTION, "Optional[0.5]");
        bigDecimalValueByPrecision_int_Rational_helper(ONE_HALF, 4, DEFAULT_RESOLUTION, "Optional[0.5000]");
        bigDecimalValueByPrecision_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 1, DEFAULT_RESOLUTION, "Optional[-1]");
        bigDecimalValueByPrecision_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 2, DEFAULT_RESOLUTION, "Optional[-1.3]");
        bigDecimalValueByPrecision_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 3, DEFAULT_RESOLUTION, "Optional[-1.33]");
        bigDecimalValueByPrecision_int_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                4,
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByPrecision_int_Rational_helper(PI, 1, DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByPrecision_int_Rational_helper(PI, 2, DEFAULT_RESOLUTION, "Optional[3.1]");
        bigDecimalValueByPrecision_int_Rational_helper(PI, 3, DEFAULT_RESOLUTION, "Optional[3.14]");
        bigDecimalValueByPrecision_int_Rational_helper(PI, 4, DEFAULT_RESOLUTION, "Optional[3.142]");
        bigDecimalValueByPrecision_int_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                3,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                3,
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );
        bigDecimalValueByPrecision_int_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                3,
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );
        bigDecimalValueByPrecision_int_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                3,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                3,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByPrecision_int_Rational_fail_helper(PI, 0, DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_Rational_fail_helper(ONE, -1, DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_Rational_fail_helper(PI, -1, DEFAULT_RESOLUTION);

        bigDecimalValueByPrecision_int_Rational_fail_helper(ZERO, 0, Rational.ZERO);
        bigDecimalValueByPrecision_int_Rational_fail_helper(ZERO, 0, Rational.NEGATIVE_ONE);
    }
}
