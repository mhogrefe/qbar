package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

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
        iterator_helper(of(Rational.of(-4, 3)),
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

    private static void isExact_helper(@NotNull Real input, boolean output) {
        aeq(input.isExact(), output);
    }

    @Test
    public void testIsExact() {
        isExact_helper(ZERO, true);
        isExact_helper(ONE, true);
        isExact_helper(NEGATIVE_ONE, true);
        isExact_helper(of(Rational.of(-4, 3)), true);
        isExact_helper(SQRT_TWO, false);
        isExact_helper(E, false);
        isExact_helper(PI, false);

        isExact_helper(fuzzyRepresentation(Rational.ZERO), false);
        isExact_helper(leftFuzzyRepresentation(Rational.ZERO), false);
        isExact_helper(rightFuzzyRepresentation(Rational.ZERO), false);
    }

    private static void rationalValue_helper(@NotNull Real input, @NotNull String output) {
        aeq(input.rationalValue(), output);
    }

    @Test
    public void testRationalValue() {
        rationalValue_helper(ZERO, "Optional[0]");
        rationalValue_helper(ONE, "Optional[1]");
        rationalValue_helper(NEGATIVE_ONE, "Optional[-1]");
        rationalValue_helper(of(Rational.of(-4, 3)), "Optional[-4/3]");
        rationalValue_helper(SQRT_TWO, "Optional.empty");
        rationalValue_helper(E, "Optional.empty");
        rationalValue_helper(PI, "Optional.empty");

        rationalValue_helper(fuzzyRepresentation(Rational.ZERO), "Optional.empty");
        rationalValue_helper(leftFuzzyRepresentation(Rational.ZERO), "Optional.empty");
        rationalValue_helper(rightFuzzyRepresentation(Rational.ZERO), "Optional.empty");
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
        List<Real> targets = Arrays.asList(ZERO, ONE, NEGATIVE_ONE, of(Rational.of(-4, 3)), SQRT_TWO, E, PI);

        match_helper(ZERO, targets, 0);
        match_helper(ONE, targets, 1);
        match_helper(NEGATIVE_ONE, targets, 2);
        match_helper(of(Rational.of(-4, 3)), targets, 3);
        match_helper(SQRT_TWO, targets, 4);
        match_helper(E, targets, 5);
        match_helper(PI, targets, 6);

        match_fail_helper(ONE, Collections.emptyList());
        match_fail_helper(of(100), targets);
    }
}
