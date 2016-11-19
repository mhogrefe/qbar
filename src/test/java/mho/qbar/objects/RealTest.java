package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
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

        aeq(PRIME_CONSTANT.toStringBaseUnsafe(IntegerUtils.TWO, TINY_LIMIT), "0.01101010001010001010...");
        constant_helper(PRIME_CONSTANT, "0.41468250985111166024...");
        aeq(THUE_MORSE.toStringBaseUnsafe(IntegerUtils.TWO, TINY_LIMIT), "0.01101001100101101001...");
        constant_helper(THUE_MORSE, "0.41245403364010759778...");
        aeq(KOLAKOSKI.toStringBaseUnsafe(IntegerUtils.TWO, TINY_LIMIT), "0.01100101101100100110...");
        constant_helper(KOLAKOSKI, "0.39725359638973963812...");

        constant_helper(CONTINUED_FRACTION_CONSTANT, "0.69777465796400798200...");
        constant_helper(CAHEN, "0.64341054628833802618...");

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
        fuzzyRepresentation_helper("1", "~1");
        fuzzyRepresentation_helper("-1", "~-1");
        fuzzyRepresentation_helper("2", "~2");
        fuzzyRepresentation_helper("-2", "~-2");
        fuzzyRepresentation_helper("11", "~11");
        fuzzyRepresentation_helper("-11", "~-11");
        fuzzyRepresentation_helper("5/4", "~1.25");
        fuzzyRepresentation_helper("-5/4", "~-1.25");
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
        leftFuzzyRepresentation_helper("0", "-0.00000000000000000000...");
        leftFuzzyRepresentation_helper("1", "0.99999999999999999999...");
        leftFuzzyRepresentation_helper("-1", "-1.00000000000000000000...");
        leftFuzzyRepresentation_helper("2", "1.99999999999999999999...");
        leftFuzzyRepresentation_helper("-2", "-2.00000000000000000000...");
        leftFuzzyRepresentation_helper("11", "10.99999999999999999999...");
        leftFuzzyRepresentation_helper("-11", "-11.00000000000000000000...");
        leftFuzzyRepresentation_helper("5/4", "1.24999999999999999999...");
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
        rightFuzzyRepresentation_helper("0", "0.00000000000000000000...");
        rightFuzzyRepresentation_helper("1", "1.00000000000000000000...");
        rightFuzzyRepresentation_helper("-1", "-0.99999999999999999999...");
        rightFuzzyRepresentation_helper("2", "2.00000000000000000000...");
        rightFuzzyRepresentation_helper("-2", "-1.99999999999999999999...");
        rightFuzzyRepresentation_helper("11", "11.00000000000000000000...");
        rightFuzzyRepresentation_helper("-11", "-10.99999999999999999999...");
        rightFuzzyRepresentation_helper("5/4", "1.25000000000000000000...");
        rightFuzzyRepresentation_helper("-5/4", "-1.24999999999999999999...");
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
                "[[281476/89625, 651864872/204778785], [1231847548/392109375, 670143059704/213311234375]," +
                " [25406862797788/8087255859375, 5277328977275528/1679825970703125]," +
                " [82897734054435918961108/26387168299946630859375," +
                " 17218914588448662618112448/5480950692586369095703125]," +
                " [3367720447004809249690544948/1071978712185331878662109375," +
                " 1538940477244015526362193851552/489859968149906737928466796875], ...]");

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

        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "FLOOR"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "CEILING"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "DOWN"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "UP"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_DOWN"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_UP"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_EVEN"
        );
        bigDecimalValueByPrecisionUnsafe_int_RoundingMode_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "UNNECESSARY"
        );

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
                DEFAULT_RESOLUTION,
                "Optional.empty"
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

        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "FLOOR",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "CEILING",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "DOWN",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "UP",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION
        );
        bigDecimalValueByPrecision_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );

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
        bigDecimalValueByPrecisionUnsafe_int_fail_helper(fuzzyRepresentation(Rational.ZERO), 0);
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

        bigDecimalValueByPrecision_int_Rational_fail_helper(fuzzyRepresentation(Rational.ZERO), 0, DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_Rational_fail_helper(PI, 0, DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_Rational_fail_helper(ONE, -1, DEFAULT_RESOLUTION);
        bigDecimalValueByPrecision_int_Rational_fail_helper(PI, -1, DEFAULT_RESOLUTION);

        bigDecimalValueByPrecision_int_Rational_fail_helper(ZERO, 0, Rational.ZERO);
        bigDecimalValueByPrecision_int_Rational_fail_helper(ZERO, 0, Rational.NEGATIVE_ONE);
    }

    private static void bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
            @NotNull Real x,
            int scale,
            @NotNull String rm,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByScaleUnsafe(scale, Readers.readRoundingModeStrict(rm).get()), output);
    }

    private static void bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(
            @NotNull Real x,
            int scale,
            @NotNull String roundingMode
    ) {
        try {
            x.bigDecimalValueByScaleUnsafe(scale, Readers.readRoundingModeStrict(roundingMode).get());
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByScaleUnsafe_int_RoundingMode() {
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "FLOOR", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "CEILING", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "DOWN", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "UP", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "HALF_DOWN", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "HALF_UP", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "HALF_EVEN", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 0, "UNNECESSARY", "0");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "FLOOR", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "CEILING", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "DOWN", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "UP", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "HALF_DOWN", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "HALF_UP", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "HALF_EVEN", "0.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, 3, "UNNECESSARY", "0.000");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "FLOOR", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "CEILING", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "DOWN", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "UP", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "HALF_DOWN", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "HALF_UP", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "HALF_EVEN", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ZERO, -3, "UNNECESSARY", "0E+3");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "FLOOR", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "CEILING", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "DOWN", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "UP", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "HALF_DOWN", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "HALF_UP", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "HALF_EVEN", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 0, "UNNECESSARY", "1");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "FLOOR", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "CEILING", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "DOWN", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "UP", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "HALF_DOWN", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "HALF_UP", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "HALF_EVEN", "1.000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, 3, "UNNECESSARY", "1.000");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "FLOOR", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "CEILING", "1E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "DOWN", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "UP", "1E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "HALF_DOWN", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "HALF_UP", "0E+3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE, -3, "HALF_EVEN", "0E+3");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "FLOOR", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "CEILING", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "DOWN", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "UP", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "HALF_DOWN", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "HALF_UP", "1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 0, "HALF_EVEN", "0");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "FLOOR", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "CEILING", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "DOWN", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "UP", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "HALF_DOWN", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "HALF_UP", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "HALF_EVEN", "0.5000");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(ONE_HALF, 4, "UNNECESSARY", "0.5000");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "FLOOR", "-2");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "CEILING", "-1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "DOWN", "-1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "UP", "-2");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "HALF_DOWN", "-1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "HALF_UP", "-1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 0, "HALF_EVEN", "-1");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "FLOOR", "-1.4");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "CEILING", "-1.3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "DOWN", "-1.3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "UP", "-1.4");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "HALF_DOWN", "-1.3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "HALF_UP", "-1.3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 1, "HALF_EVEN", "-1.3");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "FLOOR", "-1.34");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "CEILING", "-1.33");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "DOWN", "-1.33");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "UP", "-1.34");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "HALF_DOWN", "-1.33");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "HALF_UP", "-1.33");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 2, "HALF_EVEN", "-1.33");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "FLOOR", "-1.334");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "CEILING", "-1.333");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "DOWN", "-1.333");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "UP", "-1.334");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "HALF_DOWN", "-1.333");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "HALF_UP", "-1.333");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(NEGATIVE_FOUR_THIRDS, 3, "HALF_EVEN", "-1.333");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "FLOOR", "3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "CEILING", "4");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "DOWN", "3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "UP", "4");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "HALF_DOWN", "3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "HALF_UP", "3");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 0, "HALF_EVEN", "3");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "FLOOR", "3.1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "CEILING", "3.2");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "DOWN", "3.1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "UP", "3.2");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "HALF_DOWN", "3.1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "HALF_UP", "3.1");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 1, "HALF_EVEN", "3.1");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "FLOOR", "3.14");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "CEILING", "3.15");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "DOWN", "3.14");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "UP", "3.15");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "HALF_DOWN", "3.14");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "HALF_UP", "3.14");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 2, "HALF_EVEN", "3.14");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "FLOOR", "3.141");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "CEILING", "3.142");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "DOWN", "3.141");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "UP", "3.142");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "HALF_DOWN", "3.142");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "HALF_UP", "3.142");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(PI, 3, "HALF_EVEN", "3.142");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(fuzzyRepresentation(Rational.ZERO), 0, "DOWN", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                0,
                "CEILING",
                "0"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(rightFuzzyRepresentation(Rational.ZERO), 0, "FLOOR", "0");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_UP",
                "0"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_DOWN",
                "0"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_EVEN",
                "0"
        );

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(123)),
                0,
                "UP",
                "123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(123)),
                0,
                "DOWN",
                "123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(123)),
                0,
                "CEILING",
                "123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(123)),
                0,
                "FLOOR",
                "123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "HALF_UP",
                "123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "HALF_DOWN",
                "123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "HALF_EVEN",
                "123"
        );

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(-123)),
                0,
                "UP",
                "-123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-123)),
                0,
                "DOWN",
                "-123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-123)),
                0,
                "CEILING",
                "-123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(-123)),
                0,
                "FLOOR",
                "-123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "HALF_UP",
                "-123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "HALF_DOWN",
                "-123"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "HALF_EVEN",
                "-123"
        );

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "UP",
                "1.24E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "DOWN",
                "1.23E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "CEILING",
                "1.24E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "FLOOR",
                "1.23E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(1235)),
                -1,
                "HALF_UP",
                "1.24E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(1235)),
                -1,
                "HALF_DOWN",
                "1.23E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(1235)),
                -1,
                "HALF_EVEN",
                "1.24E+3"
        );

        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "UP",
                "-1.24E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "DOWN",
                "-1.23E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "CEILING",
                "-1.23E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "FLOOR",
                "-1.24E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-1235)),
                -1,
                "HALF_UP",
                "-1.24E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                rightFuzzyRepresentation(Rational.of(-1235)),
                -1,
                "HALF_DOWN",
                "-1.23E+3"
        );
        bigDecimalValueByScaleUnsafe_int_RoundingMode_helper(
                leftFuzzyRepresentation(Rational.of(-1235)),
                -1,
                "HALF_EVEN",
                "-1.24E+3"
        );

        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(ONE, -1, "UNNECESSARY");

        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, -1, "UNNECESSARY");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, 0, "UNNECESSARY");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, 1, "UNNECESSARY");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, 2, "UNNECESSARY");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, 3, "UNNECESSARY");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, 4, "UNNECESSARY");
        bigDecimalValueByScaleUnsafe_int_RoundingMode_fail_helper(PI, 5, "UNNECESSARY");
    }

    private static void bigDecimalValueByScale_int_RoundingMode_Rational_helper(
            @NotNull Real x,
            int scale,
            @NotNull String rm,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByScale(scale, Readers.readRoundingModeStrict(rm).get(), resolution), output);
    }

    private static void bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(
            @NotNull Real x,
            int scale,
            @NotNull String roundingMode,
            @NotNull Rational resolution
    ) {
        try {
            x.bigDecimalValueByScale(scale, Readers.readRoundingModeStrict(roundingMode).get(), resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByScale_int_RoundingMode_Rational() {
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, 0, "FLOOR", DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, 0, "CEILING", DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, 0, "DOWN", DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, 0, "UP", DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, 0, "HALF_UP", DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, 3, "UP", DEFAULT_RESOLUTION, "Optional[0.000]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                3,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0.000]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ZERO, -3, "UP", DEFAULT_RESOLUTION, "Optional[0E+3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ZERO,
                -3,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 0, "FLOOR", DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 0, "CEILING", DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 0, "DOWN", DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 0, "UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 0, "HALF_UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 3, "DOWN", DEFAULT_RESOLUTION, "Optional[1.000]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, 3, "UP", DEFAULT_RESOLUTION, "Optional[1.000]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                3,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[1.000]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                -3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                -3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, -3, "DOWN", DEFAULT_RESOLUTION, "Optional[0E+3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE, -3, "UP", DEFAULT_RESOLUTION, "Optional[1E+3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                -3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                -3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE,
                -3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0E+3]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(ONE_HALF, 0, "UP", DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                ONE_HALF,
                4,
                "UNNECESSARY",
                DEFAULT_RESOLUTION,
                "Optional[0.5000]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-2]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.4]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.4]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                1,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.3]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.34]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.34]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                2,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.33]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.334]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.334]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-1.333]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "FLOOR", DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "CEILING", DEFAULT_RESOLUTION, "Optional[4]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "DOWN", DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "UP", DEFAULT_RESOLUTION, "Optional[4]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "HALF_DOWN", DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "HALF_UP", DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 0, "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3]");

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 1, "FLOOR", DEFAULT_RESOLUTION, "Optional[3.1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 1, "CEILING", DEFAULT_RESOLUTION, "Optional[3.2]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 1, "DOWN", DEFAULT_RESOLUTION, "Optional[3.1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 1, "UP", DEFAULT_RESOLUTION, "Optional[3.2]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                1,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 1, "HALF_UP", DEFAULT_RESOLUTION, "Optional[3.1]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                1,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3.1]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 2, "FLOOR", DEFAULT_RESOLUTION, "Optional[3.14]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                2,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[3.15]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 2, "DOWN", DEFAULT_RESOLUTION, "Optional[3.14]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 2, "UP", DEFAULT_RESOLUTION, "Optional[3.15]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                2,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                2,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                2,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3.14]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 3, "FLOOR", DEFAULT_RESOLUTION, "Optional[3.141]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                3,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 3, "DOWN", DEFAULT_RESOLUTION, "Optional[3.141]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(PI, 3, "UP", DEFAULT_RESOLUTION, "Optional[3.142]");
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                3,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                3,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                PI,
                3,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[3.142]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[1.24E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[1.23E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[1.24E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[1.23E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "UP",
                DEFAULT_RESOLUTION,
                "Optional[-1.24E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "DOWN",
                DEFAULT_RESOLUTION,
                "Optional[-1.23E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "CEILING",
                DEFAULT_RESOLUTION,
                "Optional[-1.23E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "FLOOR",
                DEFAULT_RESOLUTION,
                "Optional[-1.24E+3]"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "HALF_UP",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "HALF_DOWN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_RoundingMode_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                "HALF_EVEN",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, -1, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, 0, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, 1, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, 2, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, 3, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, 4, "UNNECESSARY", DEFAULT_RESOLUTION);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(PI, 5, "UNNECESSARY", DEFAULT_RESOLUTION);

        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                "UNNECESSARY",
                DEFAULT_RESOLUTION
        );

        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(ZERO, 0, "FLOOR", Rational.ZERO);
        bigDecimalValueByScale_int_RoundingMode_Rational_fail_helper(ZERO, 0, "FLOOR", Rational.NEGATIVE_ONE);
    }

    private static void bigDecimalValueByScaleUnsafe_int_helper(@NotNull Real x, int scale, @NotNull String output) {
        aeq(x.bigDecimalValueByScaleUnsafe(scale), output);
    }

    @Test
    public void testBigDecimalValueByScaleUnsafe_int() {
        bigDecimalValueByScaleUnsafe_int_helper(ZERO, 0, "0");
        bigDecimalValueByScaleUnsafe_int_helper(ZERO, 3, "0.000");
        bigDecimalValueByScaleUnsafe_int_helper(ZERO, -3, "0E+3");
        bigDecimalValueByScaleUnsafe_int_helper(ONE, 0, "1");
        bigDecimalValueByScaleUnsafe_int_helper(ONE, 3, "1.000");
        bigDecimalValueByScaleUnsafe_int_helper(ONE, -3, "0E+3");
        bigDecimalValueByScaleUnsafe_int_helper(ONE_HALF, 0, "0");
        bigDecimalValueByScaleUnsafe_int_helper(ONE_HALF, 4, "0.5000");
        bigDecimalValueByScaleUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 0, "-1");
        bigDecimalValueByScaleUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 1, "-1.3");
        bigDecimalValueByScaleUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 3, "-1.333");
        bigDecimalValueByScaleUnsafe_int_helper(PI, 0, "3");
        bigDecimalValueByScaleUnsafe_int_helper(PI, 1, "3.1");
        bigDecimalValueByScaleUnsafe_int_helper(PI, 2, "3.14");
        bigDecimalValueByScaleUnsafe_int_helper(PI, 3, "3.142");

        bigDecimalValueByScaleUnsafe_int_helper(fuzzyRepresentation(Rational.ZERO), 0, "0");
        bigDecimalValueByScaleUnsafe_int_helper(fuzzyRepresentation(Rational.of(123)), 0, "123");
        bigDecimalValueByScaleUnsafe_int_helper(fuzzyRepresentation(Rational.of(-123)), 0, "-123");
        bigDecimalValueByScaleUnsafe_int_helper(rightFuzzyRepresentation(Rational.of(1235)), -1, "1.24E+3");
        bigDecimalValueByScaleUnsafe_int_helper(leftFuzzyRepresentation(Rational.of(-1235)), -1, "-1.24E+3");
    }

    private static void bigDecimalValueByScale_int_Rational_helper(
            @NotNull Real x,
            int scale,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(x.bigDecimalValueByScale(scale, resolution), output);
    }

    private static void bigDecimalValueByScale_int_Rational_fail_helper(
            @NotNull Real x,
            int scale,
            @NotNull Rational resolution
    ) {
        try {
            x.bigDecimalValueByScale(scale, resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBigDecimalValueByScale_int_Rational() {
        bigDecimalValueByScale_int_Rational_helper(ZERO, 0, DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_Rational_helper(ZERO, 3, DEFAULT_RESOLUTION, "Optional[0.000]");
        bigDecimalValueByScale_int_Rational_helper(ZERO, -3, DEFAULT_RESOLUTION, "Optional[0E+3]");
        bigDecimalValueByScale_int_Rational_helper(ONE, 0, DEFAULT_RESOLUTION, "Optional[1]");
        bigDecimalValueByScale_int_Rational_helper(ONE, 3, DEFAULT_RESOLUTION, "Optional[1.000]");
        bigDecimalValueByScale_int_Rational_helper(ONE, -3, DEFAULT_RESOLUTION, "Optional[0E+3]");
        bigDecimalValueByScale_int_Rational_helper(ONE_HALF, 0, DEFAULT_RESOLUTION, "Optional[0]");
        bigDecimalValueByScale_int_Rational_helper(ONE_HALF, 4, DEFAULT_RESOLUTION, "Optional[0.5000]");
        bigDecimalValueByScale_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 0, DEFAULT_RESOLUTION, "Optional[-1]");
        bigDecimalValueByScale_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 1, DEFAULT_RESOLUTION, "Optional[-1.3]");
        bigDecimalValueByScale_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 2, DEFAULT_RESOLUTION, "Optional[-1.33]");
        bigDecimalValueByScale_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 3, DEFAULT_RESOLUTION, "Optional[-1.333]");
        bigDecimalValueByScale_int_Rational_helper(PI, 0, DEFAULT_RESOLUTION, "Optional[3]");
        bigDecimalValueByScale_int_Rational_helper(PI, 1, DEFAULT_RESOLUTION, "Optional[3.1]");
        bigDecimalValueByScale_int_Rational_helper(PI, 2, DEFAULT_RESOLUTION, "Optional[3.14]");
        bigDecimalValueByScale_int_Rational_helper(PI, 3, DEFAULT_RESOLUTION, "Optional[3.142]");

        bigDecimalValueByScale_int_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                0,
                DEFAULT_RESOLUTION,
                "Optional[0]"
        );
        bigDecimalValueByScale_int_Rational_helper(
                fuzzyRepresentation(Rational.of(123)),
                0,
                DEFAULT_RESOLUTION,
                "Optional[123]"
        );
        bigDecimalValueByScale_int_Rational_helper(
                fuzzyRepresentation(Rational.of(-123)),
                0,
                DEFAULT_RESOLUTION,
                "Optional[-123]"
        );
        bigDecimalValueByScale_int_Rational_helper(
                fuzzyRepresentation(Rational.of(1235)),
                -1,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        bigDecimalValueByScale_int_Rational_helper(
                fuzzyRepresentation(Rational.of(-1235)),
                -1,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        bigDecimalValueByScale_int_Rational_fail_helper(ZERO, 0, Rational.ZERO);
        bigDecimalValueByScale_int_Rational_fail_helper(ZERO, 0, Rational.NEGATIVE_ONE);
    }

    private static void bigDecimalValueExact_helper(@NotNull Real input, @NotNull String output) {
        aeq(input.bigDecimalValueExact(), output);
    }

    private static void bigDecimalValueExact_fail_helper(@NotNull Real input) {
        try {
            input.bigDecimalValueExact();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBigDecimalValueExact() {
        bigDecimalValueExact_helper(ZERO, "0");
        bigDecimalValueExact_helper(ONE, "1");
        bigDecimalValueExact_helper(ONE_HALF, "0.5");

        bigDecimalValueExact_helper(of(Rational.SMALLEST_FLOAT),
                "1.4012984643248170709237295832899161312802619418765157717570682838897910826858606014866381883621215" +
                "8203125E-45");
        bigDecimalValueExact_helper(of(Rational.LARGEST_FLOAT), "340282346638528859811704183484516925440");
        bigDecimalValueExact_helper(of(Rational.SMALLEST_DOUBLE),
                "4.9406564584124654417656879286822137236505980261432476442558568250067550727020875186529983636163599" +
                "237979656469544571773092665671035593979639877479601078187812630071319031140452784581716784898210368" +
                "871863605699873072305000638740915356498438731247339727316961514003171538539807412623856559117102665" +
                "855668676818703956031062493194527159149245532930545654440112748012970999954193198940908041656332452" +
                "475714786901472678015935523861155013480352649347201937902681071074917033322268447533357208324319360" +
                "923828934583680601060115061698097530783422773183292479049825247307763759272478746560847782037344696" +
                "995336470179726777175851256605511991315048911014510378627381672509558373897335989936648099411642057" +
                "02637090279242767544565229087538682506419718265533447265625E-324");
        bigDecimalValueExact_helper(of(Rational.LARGEST_DOUBLE),
                "179769313486231570814527423731704356798070567525844996598917476803157260780028538760589558632766878" +
                "171540458953514382464234321326889464182768467546703537516986049910576551282076245490090389328944075" +
                "868508455133942304583236903222948165808559332123348274797826204144723168738177180919299881250404026" +
                "184124858368");

        bigDecimalValueExact_fail_helper(NEGATIVE_FOUR_THIRDS);
        bigDecimalValueExact_fail_helper(PI);
    }

    private static void negate_helper(@NotNull Real input, @NotNull String output) {
        Real x = input.negate();
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testNegate() {
        negate_helper(ZERO, "0");
        negate_helper(ONE, "-1");
        negate_helper(NEGATIVE_ONE, "1");
        negate_helper(ONE_HALF, "-0.5");
        negate_helper(NEGATIVE_FOUR_THIRDS, "1.33333333333333333333...");
        negate_helper(SQRT_TWO, "-1.41421356237309504880...");
        negate_helper(E, "-2.71828182845904523536...");
        negate_helper(PI, "-3.14159265358979323846...");
        negate_helper(leftFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        negate_helper(rightFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        negate_helper(fuzzyRepresentation(Rational.ZERO), "~0");
    }

    private static void abs_helper(@NotNull Real input, @NotNull String output) {
        Real x = input.abs();
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAbs() {
        abs_helper(ZERO, "0");
        abs_helper(ONE, "1");
        abs_helper(NEGATIVE_ONE, "1");
        abs_helper(ONE_HALF, "0.5");
        abs_helper(NEGATIVE_FOUR_THIRDS, "1.33333333333333333333...");
        abs_helper(SQRT_TWO, "1.41421356237309504880...");
        abs_helper(E, "2.71828182845904523536...");
        abs_helper(PI, "3.14159265358979323846...");
        abs_helper(leftFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        abs_helper(rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        abs_helper(fuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
    }

    private static void signumUnsafe_helper(@NotNull Real input, int output) {
        aeq(input.signumUnsafe(), output);
    }

    @Test
    public void testSignumUnsafe() {
        signumUnsafe_helper(ZERO, 0);
        signumUnsafe_helper(ONE, 1);
        signumUnsafe_helper(NEGATIVE_ONE, -1);
        signumUnsafe_helper(ONE_HALF, 1);
        signumUnsafe_helper(NEGATIVE_FOUR_THIRDS, -1);
        signumUnsafe_helper(SQRT_TWO, 1);
        signumUnsafe_helper(SQRT_TWO.negate(), -1);
        signumUnsafe_helper(E, 1);
        signumUnsafe_helper(E.negate(), -1);
        signumUnsafe_helper(PI, 1);
        signumUnsafe_helper(PI.negate(), -1);
    }

    private static void signum_helper(@NotNull Real input, @NotNull Rational resolution, @NotNull String output) {
        aeq(input.signum(resolution), output);
    }

    private static void signum_fail_helper(@NotNull Real input, @NotNull Rational resolution) {
        try {
            input.signum(resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testSignum() {
        signum_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0]");
        signum_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        signum_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1]");
        signum_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[1]");
        signum_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-1]");
        signum_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1]");
        signum_helper(SQRT_TWO.negate(), DEFAULT_RESOLUTION, "Optional[-1]");
        signum_helper(E, DEFAULT_RESOLUTION, "Optional[1]");
        signum_helper(E.negate(), DEFAULT_RESOLUTION, "Optional[-1]");
        signum_helper(PI, DEFAULT_RESOLUTION, "Optional[1]");
        signum_helper(PI.negate(), DEFAULT_RESOLUTION, "Optional[-1]");

        signum_helper(leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        signum_helper(rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        signum_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        signum_fail_helper(ZERO, Rational.ZERO);
        signum_fail_helper(ZERO, Rational.NEGATIVE_ONE);
        signum_fail_helper(ONE, Rational.ZERO);
        signum_fail_helper(ONE, Rational.NEGATIVE_ONE);
        signum_fail_helper(NEGATIVE_ONE, Rational.ZERO);
        signum_fail_helper(NEGATIVE_ONE, Rational.NEGATIVE_ONE);
        signum_fail_helper(PI, Rational.ZERO);
        signum_fail_helper(PI, Rational.NEGATIVE_ONE);
    }

    private static void add_Rational_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        Real x = a.add(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAdd_Rational() {
        add_Rational_helper(ZERO, "0", "0");
        add_Rational_helper(ZERO, "1", "1");
        add_Rational_helper(ZERO, "-1", "-1");
        add_Rational_helper(ZERO, "100/3", "33.33333333333333333333...");
        add_Rational_helper(ZERO, "1/100", "0.01");

        add_Rational_helper(ONE, "0", "1");
        add_Rational_helper(ONE, "1", "2");
        add_Rational_helper(ONE, "-1", "0");
        add_Rational_helper(ONE, "100/3", "34.33333333333333333333...");
        add_Rational_helper(ONE, "1/100", "1.01");

        add_Rational_helper(NEGATIVE_ONE, "0", "-1");
        add_Rational_helper(NEGATIVE_ONE, "1", "0");
        add_Rational_helper(NEGATIVE_ONE, "-1", "-2");
        add_Rational_helper(NEGATIVE_ONE, "100/3", "32.33333333333333333333...");
        add_Rational_helper(NEGATIVE_ONE, "1/100", "-0.99");

        add_Rational_helper(ONE_HALF, "0", "0.5");
        add_Rational_helper(ONE_HALF, "1", "1.5");
        add_Rational_helper(ONE_HALF, "-1", "-0.5");
        add_Rational_helper(ONE_HALF, "100/3", "33.83333333333333333333...");
        add_Rational_helper(ONE_HALF, "1/100", "0.51");

        add_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", "-1.33333333333333333333...");
        add_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", "-0.33333333333333333333...");
        add_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", "-2.33333333333333333333...");
        add_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", "32");
        add_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", "-1.32333333333333333333...");

        add_Rational_helper(SQRT_TWO, "0", "1.41421356237309504880...");
        add_Rational_helper(SQRT_TWO, "1", "2.41421356237309504880...");
        add_Rational_helper(SQRT_TWO, "-1", "0.41421356237309504880...");
        add_Rational_helper(SQRT_TWO, "100/3", "34.74754689570642838213...");
        add_Rational_helper(SQRT_TWO, "1/100", "1.42421356237309504880...");

        add_Rational_helper(E, "0", "2.71828182845904523536...");
        add_Rational_helper(E, "1", "3.71828182845904523536...");
        add_Rational_helper(E, "-1", "1.71828182845904523536...");
        add_Rational_helper(E, "100/3", "36.05161516179237856869...");
        add_Rational_helper(E, "1/100", "2.72828182845904523536...");

        add_Rational_helper(PI, "0", "3.14159265358979323846...");
        add_Rational_helper(PI, "1", "4.14159265358979323846...");
        add_Rational_helper(PI, "-1", "2.14159265358979323846...");
        add_Rational_helper(PI, "100/3", "36.47492598692312657179...");
        add_Rational_helper(PI, "1/100", "3.15159265358979323846...");

        add_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", "-0.00000000000000000000...");
        add_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "0.99999999999999999999...");
        add_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", "-1.00000000000000000000...");
        add_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", "33.33333333333333333333...");
        add_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", "0.00999999999999999999...");

        add_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "0", "0.00000000000000000000...");
        add_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "1.00000000000000000000...");
        add_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", "-0.99999999999999999999...");
        add_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", "33.33333333333333333333...");
        add_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", "0.01000000000000000000...");

        add_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", "~0");
        add_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", "~1");
        add_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", "~-1");
        add_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", "33.33333333333333333333...");
        add_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", "~0.01");
    }

    private static void add_Real_helper(@NotNull Real a, @NotNull Real b, @NotNull String output) {
        Real x = a.add(b);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testAdd_Real() {
        add_Real_helper(ZERO, ZERO, "0");
        add_Real_helper(ZERO, ONE, "1");
        add_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, "-1.33333333333333333333...");
        add_Real_helper(ZERO, SQRT_TWO, "1.41421356237309504880...");
        add_Real_helper(ZERO, E, "2.71828182845904523536...");
        add_Real_helper(ZERO, PI, "3.14159265358979323846...");
        add_Real_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        add_Real_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        add_Real_helper(ZERO, fuzzyRepresentation(Rational.ZERO), "~0");

        add_Real_helper(ONE, ZERO, "1");
        add_Real_helper(ONE, ONE, "2");
        add_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, "-0.33333333333333333333...");
        add_Real_helper(ONE, SQRT_TWO, "2.41421356237309504880...");
        add_Real_helper(ONE, E, "3.71828182845904523536...");
        add_Real_helper(ONE, PI, "4.14159265358979323846...");
        add_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), "0.99999999999999999999...");
        add_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), "1.00000000000000000000...");
        add_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), "~1");

        add_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, "-1.33333333333333333333...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, "-0.33333333333333333333...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, "-2.66666666666666666666...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, "0.08088022903976171546...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, E, "1.38494849512571190202...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, PI, "1.80825932025645990512...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), "-1.33333333333333333333...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), "-1.33333333333333333333...");
        add_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), "-1.33333333333333333333...");

        add_Real_helper(SQRT_TWO, ZERO, "1.41421356237309504880...");
        add_Real_helper(SQRT_TWO, ONE, "2.41421356237309504880...");
        add_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, "0.08088022903976171546...");
        add_Real_helper(SQRT_TWO, SQRT_TWO, "2.82842712474619009760...");
        add_Real_helper(SQRT_TWO, E, "4.13249539083214028416...");
        add_Real_helper(SQRT_TWO, PI, "4.55580621596288828726...");
        add_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), "1.41421356237309504880...");
        add_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), "1.41421356237309504880...");
        add_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), "1.41421356237309504880...");

        add_Real_helper(E, ZERO, "2.71828182845904523536...");
        add_Real_helper(E, ONE, "3.71828182845904523536...");
        add_Real_helper(E, NEGATIVE_FOUR_THIRDS, "1.38494849512571190202...");
        add_Real_helper(E, SQRT_TWO, "4.13249539083214028416...");
        add_Real_helper(E, E, "5.43656365691809047072...");
        add_Real_helper(E, PI, "5.85987448204883847382...");
        add_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), "2.71828182845904523536...");
        add_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), "2.71828182845904523536...");
        add_Real_helper(E, fuzzyRepresentation(Rational.ZERO), "2.71828182845904523536...");

        add_Real_helper(PI, ZERO, "3.14159265358979323846...");
        add_Real_helper(PI, ONE, "4.14159265358979323846...");
        add_Real_helper(PI, NEGATIVE_FOUR_THIRDS, "1.80825932025645990512...");
        add_Real_helper(PI, SQRT_TWO, "4.55580621596288828726...");
        add_Real_helper(PI, E, "5.85987448204883847382...");
        add_Real_helper(PI, PI, "6.28318530717958647692...");
        add_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), "3.14159265358979323846...");
        add_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), "3.14159265358979323846...");
        add_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), "3.14159265358979323846...");

        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, "-0.00000000000000000000...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, "0.99999999999999999999...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "-1.33333333333333333333...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "1.41421356237309504880...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, "2.71828182845904523536...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, "3.14159265358979323846...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO),
                "-0.00000000000000000000...");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO), "~0");
        add_Real_helper(leftFuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, "0.00000000000000000000...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, "1.00000000000000000000...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "-1.33333333333333333333...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "1.41421356237309504880...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, "2.71828182845904523536...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, "3.14159265358979323846...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO), "~0");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO),
                "0.00000000000000000000...");
        add_Real_helper(rightFuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        add_Real_helper(fuzzyRepresentation(Rational.ZERO), ZERO, "~0");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, "~1");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "-1.33333333333333333333...");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, "1.41421356237309504880...");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), E, "2.71828182845904523536...");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, "3.14159265358979323846...");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO), "~0");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO), "~0");
        add_Real_helper(fuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        add_Real_helper(PI, PI.negate(), "~0");
    }

    private static void subtract_Rational_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        Real x = a.subtract(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testSubtract_Rational() {
        subtract_Rational_helper(ZERO, "0", "0");
        subtract_Rational_helper(ZERO, "1", "-1");
        subtract_Rational_helper(ZERO, "-1", "1");
        subtract_Rational_helper(ZERO, "100/3", "-33.33333333333333333333...");
        subtract_Rational_helper(ZERO, "1/100", "-0.01");

        subtract_Rational_helper(ONE, "0", "1");
        subtract_Rational_helper(ONE, "1", "0");
        subtract_Rational_helper(ONE, "-1", "2");
        subtract_Rational_helper(ONE, "100/3", "-32.33333333333333333333...");
        subtract_Rational_helper(ONE, "1/100", "0.99");

        subtract_Rational_helper(NEGATIVE_ONE, "0", "-1");
        subtract_Rational_helper(NEGATIVE_ONE, "1", "-2");
        subtract_Rational_helper(NEGATIVE_ONE, "-1", "0");
        subtract_Rational_helper(NEGATIVE_ONE, "100/3", "-34.33333333333333333333...");
        subtract_Rational_helper(NEGATIVE_ONE, "1/100", "-1.01");

        subtract_Rational_helper(ONE_HALF, "0", "0.5");
        subtract_Rational_helper(ONE_HALF, "1", "-0.5");
        subtract_Rational_helper(ONE_HALF, "-1", "1.5");
        subtract_Rational_helper(ONE_HALF, "100/3", "-32.83333333333333333333...");
        subtract_Rational_helper(ONE_HALF, "1/100", "0.49");

        subtract_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", "-1.33333333333333333333...");
        subtract_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", "-2.33333333333333333333...");
        subtract_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", "-0.33333333333333333333...");
        subtract_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", "-34.66666666666666666666...");
        subtract_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", "-1.34333333333333333333...");

        subtract_Rational_helper(SQRT_TWO, "0", "1.41421356237309504880...");
        subtract_Rational_helper(SQRT_TWO, "1", "0.41421356237309504880...");
        subtract_Rational_helper(SQRT_TWO, "-1", "2.41421356237309504880...");
        subtract_Rational_helper(SQRT_TWO, "100/3", "-31.91911977096023828453...");
        subtract_Rational_helper(SQRT_TWO, "1/100", "1.40421356237309504880...");

        subtract_Rational_helper(E, "0", "2.71828182845904523536...");
        subtract_Rational_helper(E, "1", "1.71828182845904523536...");
        subtract_Rational_helper(E, "-1", "3.71828182845904523536...");
        subtract_Rational_helper(E, "100/3", "-30.61505150487428809797...");
        subtract_Rational_helper(E, "1/100", "2.70828182845904523536...");

        subtract_Rational_helper(PI, "0", "3.14159265358979323846...");
        subtract_Rational_helper(PI, "1", "2.14159265358979323846...");
        subtract_Rational_helper(PI, "-1", "4.14159265358979323846...");
        subtract_Rational_helper(PI, "100/3", "-30.19174067974354009487...");
        subtract_Rational_helper(PI, "1/100", "3.13159265358979323846...");

        subtract_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", "-0.00000000000000000000...");
        subtract_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "-1.00000000000000000000...");
        subtract_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", "0.99999999999999999999...");
        subtract_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", "-33.33333333333333333333...");
        subtract_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", "-0.01000000000000000000...");

        subtract_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "0", "0.00000000000000000000...");
        subtract_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "-0.99999999999999999999...");
        subtract_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", "1.00000000000000000000...");
        subtract_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", "-33.33333333333333333333...");
        subtract_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", "-0.00999999999999999999...");

        subtract_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", "~0");
        subtract_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", "~-1");
        subtract_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", "~1");
        subtract_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", "-33.33333333333333333333...");
        subtract_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", "~-0.01");
    }

    private static void subtract_Real_helper(@NotNull Real a, @NotNull Real b, @NotNull String output) {
        Real x = a.subtract(b);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testSubtract_Real() {
        subtract_Real_helper(ZERO, ZERO, "0");
        subtract_Real_helper(ZERO, ONE, "-1");
        subtract_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, "1.33333333333333333333...");
        subtract_Real_helper(ZERO, SQRT_TWO, "-1.41421356237309504880...");
        subtract_Real_helper(ZERO, E, "-2.71828182845904523536...");
        subtract_Real_helper(ZERO, PI, "-3.14159265358979323846...");
        subtract_Real_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        subtract_Real_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        subtract_Real_helper(ZERO, fuzzyRepresentation(Rational.ZERO), "~0");

        subtract_Real_helper(ONE, ZERO, "1");
        subtract_Real_helper(ONE, ONE, "0");
        subtract_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, "2.33333333333333333333...");
        subtract_Real_helper(ONE, SQRT_TWO, "-0.41421356237309504880...");
        subtract_Real_helper(ONE, E, "-1.71828182845904523536...");
        subtract_Real_helper(ONE, PI, "-2.14159265358979323846...");
        subtract_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), "1.00000000000000000000...");
        subtract_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), "0.99999999999999999999...");
        subtract_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), "~1");

        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, "-1.33333333333333333333...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, "-2.33333333333333333333...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, "0");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, "-2.74754689570642838213...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, E, "-4.05161516179237856869...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, PI, "-4.47492598692312657179...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO),
                "-1.33333333333333333333...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO),
                "-1.33333333333333333333...");
        subtract_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), "-1.33333333333333333333...");

        subtract_Real_helper(SQRT_TWO, ZERO, "1.41421356237309504880...");
        subtract_Real_helper(SQRT_TWO, ONE, "0.41421356237309504880...");
        subtract_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, "2.74754689570642838213...");
        subtract_Real_helper(SQRT_TWO, SQRT_TWO, "0");
        subtract_Real_helper(SQRT_TWO, E, "-1.30406826608595018655...");
        subtract_Real_helper(SQRT_TWO, PI, "-1.72737909121669818966...");
        subtract_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), "1.41421356237309504880...");
        subtract_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), "1.41421356237309504880...");
        subtract_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), "1.41421356237309504880...");

        subtract_Real_helper(E, ZERO, "2.71828182845904523536...");
        subtract_Real_helper(E, ONE, "1.71828182845904523536...");
        subtract_Real_helper(E, NEGATIVE_FOUR_THIRDS, "4.05161516179237856869...");
        subtract_Real_helper(E, SQRT_TWO, "1.30406826608595018655...");
        subtract_Real_helper(E, E, "0");
        subtract_Real_helper(E, PI, "-0.42331082513074800310...");
        subtract_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), "2.71828182845904523536...");
        subtract_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), "2.71828182845904523536...");
        subtract_Real_helper(E, fuzzyRepresentation(Rational.ZERO), "2.71828182845904523536...");

        subtract_Real_helper(PI, ZERO, "3.14159265358979323846...");
        subtract_Real_helper(PI, ONE, "2.14159265358979323846...");
        subtract_Real_helper(PI, NEGATIVE_FOUR_THIRDS, "4.47492598692312657179...");
        subtract_Real_helper(PI, SQRT_TWO, "1.72737909121669818966...");
        subtract_Real_helper(PI, E, "0.42331082513074800310...");
        subtract_Real_helper(PI, PI, "0");
        subtract_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), "3.14159265358979323846...");
        subtract_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), "3.14159265358979323846...");
        subtract_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), "3.14159265358979323846...");

        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, "-0.00000000000000000000...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, "-1.00000000000000000000...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS,
                "1.33333333333333333333...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "-1.41421356237309504880...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, "-2.71828182845904523536...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, "-3.14159265358979323846...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO), "~0");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO),
                "-0.00000000000000000000...");
        subtract_Real_helper(leftFuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, "0.00000000000000000000...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, "-0.99999999999999999999...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS,
                "1.33333333333333333333...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "-1.41421356237309504880...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, "-2.71828182845904523536...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, "-3.14159265358979323846...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO),
                "0.00000000000000000000...");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO), "~0");
        subtract_Real_helper(rightFuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), ZERO, "~0");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, "~-1");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "1.33333333333333333333...");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, "-1.41421356237309504880...");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), E, "-2.71828182845904523536...");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, "-3.14159265358979323846...");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO), "~0");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO), "~0");
        subtract_Real_helper(fuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        subtract_Real_helper(PI, PI.negate().negate(), "~0");
    }

    private static void multiply_int_helper(@NotNull Real a, int b, @NotNull String output) {
        Real x = a.multiply(b);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper(ZERO, 0, "0");
        multiply_int_helper(ZERO, 1, "0");
        multiply_int_helper(ZERO, -1, "0");
        multiply_int_helper(ZERO, 100, "0");

        multiply_int_helper(ONE, 0, "0");
        multiply_int_helper(ONE, 1, "1");
        multiply_int_helper(ONE, -1, "-1");
        multiply_int_helper(ONE, 100, "100");

        multiply_int_helper(NEGATIVE_ONE, 0, "0");
        multiply_int_helper(NEGATIVE_ONE, 1, "-1");
        multiply_int_helper(NEGATIVE_ONE, -1, "1");
        multiply_int_helper(NEGATIVE_ONE, 100, "-100");

        multiply_int_helper(ONE_HALF, 0, "0");
        multiply_int_helper(ONE_HALF, 1, "0.5");
        multiply_int_helper(ONE_HALF, -1, "-0.5");
        multiply_int_helper(ONE_HALF, 100, "50");

        multiply_int_helper(NEGATIVE_FOUR_THIRDS, 0, "0");
        multiply_int_helper(NEGATIVE_FOUR_THIRDS, 1, "-1.33333333333333333333...");
        multiply_int_helper(NEGATIVE_FOUR_THIRDS, -1, "1.33333333333333333333...");
        multiply_int_helper(NEGATIVE_FOUR_THIRDS, 100, "-133.33333333333333333333...");

        multiply_int_helper(SQRT_TWO, 0, "0");
        multiply_int_helper(SQRT_TWO, 1, "1.41421356237309504880...");
        multiply_int_helper(SQRT_TWO, -1, "-1.41421356237309504880...");
        multiply_int_helper(SQRT_TWO, 100, "141.42135623730950488016...");

        multiply_int_helper(E, 0, "0");
        multiply_int_helper(E, 1, "2.71828182845904523536...");
        multiply_int_helper(E, -1, "-2.71828182845904523536...");
        multiply_int_helper(E, 100, "271.82818284590452353602...");

        multiply_int_helper(PI, 0, "0");
        multiply_int_helper(PI, 1, "3.14159265358979323846...");
        multiply_int_helper(PI, -1, "-3.14159265358979323846...");
        multiply_int_helper(PI, 100, "314.15926535897932384626...");

        multiply_int_helper(leftFuzzyRepresentation(Rational.ZERO), 0, "0");
        multiply_int_helper(leftFuzzyRepresentation(Rational.ZERO), 1, "-0.00000000000000000000...");
        multiply_int_helper(leftFuzzyRepresentation(Rational.ZERO), -1, "0.00000000000000000000...");
        multiply_int_helper(leftFuzzyRepresentation(Rational.ZERO), 100, "-0.00000000000000000000...");

        multiply_int_helper(rightFuzzyRepresentation(Rational.ZERO), 0, "0");
        multiply_int_helper(rightFuzzyRepresentation(Rational.ZERO), 1, "0.00000000000000000000...");
        multiply_int_helper(rightFuzzyRepresentation(Rational.ZERO), -1, "-0.00000000000000000000...");
        multiply_int_helper(rightFuzzyRepresentation(Rational.ZERO), 100, "0.00000000000000000000...");

        multiply_int_helper(fuzzyRepresentation(Rational.ZERO), 0, "0");
        multiply_int_helper(fuzzyRepresentation(Rational.ZERO), 1, "~0");
        multiply_int_helper(fuzzyRepresentation(Rational.ZERO), -1, "~0");
        multiply_int_helper(fuzzyRepresentation(Rational.ZERO), 100, "~0");
    }

    private static void multiply_BigInteger_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        Real x = a.multiply(Readers.readBigIntegerStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper(ZERO, "0", "0");
        multiply_BigInteger_helper(ZERO, "1", "0");
        multiply_BigInteger_helper(ZERO, "-1", "0");
        multiply_BigInteger_helper(ZERO, "100", "0");

        multiply_BigInteger_helper(ONE, "0", "0");
        multiply_BigInteger_helper(ONE, "1", "1");
        multiply_BigInteger_helper(ONE, "-1", "-1");
        multiply_BigInteger_helper(ONE, "100", "100");

        multiply_BigInteger_helper(NEGATIVE_ONE, "0", "0");
        multiply_BigInteger_helper(NEGATIVE_ONE, "1", "-1");
        multiply_BigInteger_helper(NEGATIVE_ONE, "-1", "1");
        multiply_BigInteger_helper(NEGATIVE_ONE, "100", "-100");

        multiply_BigInteger_helper(ONE_HALF, "0", "0");
        multiply_BigInteger_helper(ONE_HALF, "1", "0.5");
        multiply_BigInteger_helper(ONE_HALF, "-1", "-0.5");
        multiply_BigInteger_helper(ONE_HALF, "100", "50");

        multiply_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "0", "0");
        multiply_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "1", "-1.33333333333333333333...");
        multiply_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "-1", "1.33333333333333333333...");
        multiply_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "100", "-133.33333333333333333333...");

        multiply_BigInteger_helper(SQRT_TWO, "0", "0");
        multiply_BigInteger_helper(SQRT_TWO, "1", "1.41421356237309504880...");
        multiply_BigInteger_helper(SQRT_TWO, "-1", "-1.41421356237309504880...");
        multiply_BigInteger_helper(SQRT_TWO, "100", "141.42135623730950488016...");

        multiply_BigInteger_helper(E, "0", "0");
        multiply_BigInteger_helper(E, "1", "2.71828182845904523536...");
        multiply_BigInteger_helper(E, "-1", "-2.71828182845904523536...");
        multiply_BigInteger_helper(E, "100", "271.82818284590452353602...");

        multiply_BigInteger_helper(PI, "0", "0");
        multiply_BigInteger_helper(PI, "1", "3.14159265358979323846...");
        multiply_BigInteger_helper(PI, "-1", "-3.14159265358979323846...");
        multiply_BigInteger_helper(PI, "100", "314.15926535897932384626...");

        multiply_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "0", "0");
        multiply_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "-0.00000000000000000000...");
        multiply_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", "0.00000000000000000000...");
        multiply_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "100", "-0.00000000000000000000...");

        multiply_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "0", "0");
        multiply_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "0.00000000000000000000...");
        multiply_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", "-0.00000000000000000000...");
        multiply_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "100", "0.00000000000000000000...");

        multiply_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "0", "0");
        multiply_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "1", "~0");
        multiply_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "-1", "~0");
        multiply_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "100", "~0");
    }

    private static void multiply_Rational_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        Real x = a.multiply(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper(ZERO, "0", "0");
        multiply_Rational_helper(ZERO, "1", "0");
        multiply_Rational_helper(ZERO, "-1", "0");
        multiply_Rational_helper(ZERO, "100/3", "0");
        multiply_Rational_helper(ZERO, "1/100", "0");

        multiply_Rational_helper(ONE, "0", "0");
        multiply_Rational_helper(ONE, "1", "1");
        multiply_Rational_helper(ONE, "-1", "-1");
        multiply_Rational_helper(ONE, "100/3", "33.33333333333333333333...");
        multiply_Rational_helper(ONE, "1/100", "0.01");

        multiply_Rational_helper(NEGATIVE_ONE, "0", "0");
        multiply_Rational_helper(NEGATIVE_ONE, "1", "-1");
        multiply_Rational_helper(NEGATIVE_ONE, "-1", "1");
        multiply_Rational_helper(NEGATIVE_ONE, "100/3", "-33.33333333333333333333...");
        multiply_Rational_helper(NEGATIVE_ONE, "1/100", "-0.01");

        multiply_Rational_helper(ONE_HALF, "0", "0");
        multiply_Rational_helper(ONE_HALF, "1", "0.5");
        multiply_Rational_helper(ONE_HALF, "-1", "-0.5");
        multiply_Rational_helper(ONE_HALF, "100/3", "16.66666666666666666666...");
        multiply_Rational_helper(ONE_HALF, "1/100", "0.005");

        multiply_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", "0");
        multiply_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", "-1.33333333333333333333...");
        multiply_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", "1.33333333333333333333...");
        multiply_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", "-44.44444444444444444444...");
        multiply_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", "-0.01333333333333333333...");

        multiply_Rational_helper(SQRT_TWO, "0", "0");
        multiply_Rational_helper(SQRT_TWO, "1", "1.41421356237309504880...");
        multiply_Rational_helper(SQRT_TWO, "-1", "-1.41421356237309504880...");
        multiply_Rational_helper(SQRT_TWO, "100/3", "47.14045207910316829338...");
        multiply_Rational_helper(SQRT_TWO, "1/100", "0.01414213562373095048...");

        multiply_Rational_helper(E, "0", "0");
        multiply_Rational_helper(E, "1", "2.71828182845904523536...");
        multiply_Rational_helper(E, "-1", "-2.71828182845904523536...");
        multiply_Rational_helper(E, "100/3", "90.60939428196817451200...");
        multiply_Rational_helper(E, "1/100", "0.02718281828459045235...");

        multiply_Rational_helper(PI, "0", "0");
        multiply_Rational_helper(PI, "1", "3.14159265358979323846...");
        multiply_Rational_helper(PI, "-1", "-3.14159265358979323846...");
        multiply_Rational_helper(PI, "100/3", "104.71975511965977461542...");
        multiply_Rational_helper(PI, "1/100", "0.03141592653589793238...");

        multiply_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", "0");
        multiply_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "-0.00000000000000000000...");
        multiply_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", "0.00000000000000000000...");
        multiply_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", "-0.00000000000000000000...");
        multiply_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", "-0.00000000000000000000...");

        multiply_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "0", "0");
        multiply_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "0.00000000000000000000...");
        multiply_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", "-0.00000000000000000000...");
        multiply_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", "0.00000000000000000000...");
        multiply_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", "0.00000000000000000000...");

        multiply_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", "0");
        multiply_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", "~0");
        multiply_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", "~0");
        multiply_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", "~0");
        multiply_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", "~0");
    }

    private static void multiply_Real_helper(@NotNull Real a, @NotNull Real b, @NotNull String output) {
        Real x = a.multiply(b);
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testMultiply_Real() {
        multiply_Real_helper(ZERO, ZERO, "0");
        multiply_Real_helper(ZERO, ONE, "0");
        multiply_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, "0");
        multiply_Real_helper(ZERO, SQRT_TWO, "0");
        multiply_Real_helper(ZERO, E, "0");
        multiply_Real_helper(ZERO, PI, "0");
        multiply_Real_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), "0");
        multiply_Real_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), "0");
        multiply_Real_helper(ZERO, fuzzyRepresentation(Rational.ZERO), "0");

        multiply_Real_helper(ONE, ZERO, "0");
        multiply_Real_helper(ONE, ONE, "1");
        multiply_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, "-1.33333333333333333333...");
        multiply_Real_helper(ONE, SQRT_TWO, "1.41421356237309504880...");
        multiply_Real_helper(ONE, E, "2.71828182845904523536...");
        multiply_Real_helper(ONE, PI, "3.14159265358979323846...");
        multiply_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        multiply_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        multiply_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, "0");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, "-1.33333333333333333333...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, "1.77777777777777777777...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, "-1.88561808316412673173...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, E, "-3.62437577127872698048...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, PI, "-4.18879020478639098461...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO),
                "0.00000000000000000000...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO),
                "-0.00000000000000000000...");
        multiply_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(SQRT_TWO, ZERO, "0");
        multiply_Real_helper(SQRT_TWO, ONE, "1.41421356237309504880...");
        multiply_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, "-1.88561808316412673173...");
        multiply_Real_helper(SQRT_TWO, SQRT_TWO, "~2");
        multiply_Real_helper(SQRT_TWO, E, "3.84423102815911682486...");
        multiply_Real_helper(SQRT_TWO, PI, "4.44288293815836624701...");
        multiply_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        multiply_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        multiply_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(E, ZERO, "0");
        multiply_Real_helper(E, ONE, "2.71828182845904523536...");
        multiply_Real_helper(E, NEGATIVE_FOUR_THIRDS, "-3.62437577127872698048...");
        multiply_Real_helper(E, SQRT_TWO, "3.84423102815911682486...");
        multiply_Real_helper(E, E, "7.38905609893065022723...");
        multiply_Real_helper(E, PI, "8.53973422267356706546...");
        multiply_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        multiply_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        multiply_Real_helper(E, fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(PI, ZERO, "0");
        multiply_Real_helper(PI, ONE, "3.14159265358979323846...");
        multiply_Real_helper(PI, NEGATIVE_FOUR_THIRDS, "-4.18879020478639098461...");
        multiply_Real_helper(PI, SQRT_TWO, "4.44288293815836624701...");
        multiply_Real_helper(PI, E, "8.53973422267356706546...");
        multiply_Real_helper(PI, PI, "9.86960440108935861883...");
        multiply_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), "-0.00000000000000000000...");
        multiply_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
        multiply_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, "0");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, "-0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS,
                "0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "-0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, "-0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, "-0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO),
                "0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO),
                "-0.00000000000000000000...");
        multiply_Real_helper(leftFuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, "0");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, "0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS,
                "-0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, "0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, "0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO),
                "-0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO),
                "0.00000000000000000000...");
        multiply_Real_helper(rightFuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), ZERO, "0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), E, "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO), "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO), "~0");
        multiply_Real_helper(fuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO), "~0");

        multiply_Real_helper(PI, PI.invertUnsafe(), "~1");
    }

    private static void invertUnsafe_helper(@NotNull Real input, @NotNull String output) {
        Real x = input.invertUnsafe();
        x.validate();
        aeq(x, output);
    }

    private static void invertUnsafe_fail_helper(@NotNull Real input) {
        try {
            input.invertUnsafe();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testInvertUnsafe() {
        invertUnsafe_helper(ONE, "1");
        invertUnsafe_helper(NEGATIVE_ONE, "-1");
        invertUnsafe_helper(ONE_HALF, "2");
        invertUnsafe_helper(NEGATIVE_FOUR_THIRDS, "-0.75");
        invertUnsafe_helper(SQRT_TWO, "0.70710678118654752440...");
        invertUnsafe_helper(E, "0.36787944117144232159...");
        invertUnsafe_helper(PI, "0.31830988618379067153...");

        invertUnsafe_fail_helper(ZERO);
    }

    private static void invert_helper(@NotNull Real input, @NotNull Rational resolution, @NotNull String output) {
        Optional<Real> ox = input.invert(resolution);
        if (ox.isPresent()) {
            ox.get().validate();
        }
        aeq(ox, output);
    }

    private static void invert_fail_helper(@NotNull Real input, @NotNull Rational resolution) {
        try {
            input.invert(resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testInvert() {
        invert_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        invert_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[-1]");
        invert_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[2]");
        invert_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-0.75]");
        invert_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[0.70710678118654752440...]");
        invert_helper(E, DEFAULT_RESOLUTION, "Optional[0.36787944117144232159...]");
        invert_helper(PI, DEFAULT_RESOLUTION, "Optional[0.31830988618379067153...]");

        invert_helper(leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        invert_helper(rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        invert_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        invert_fail_helper(ZERO, DEFAULT_RESOLUTION);

        invert_fail_helper(ZERO, Rational.ZERO);
        invert_fail_helper(ZERO, Rational.NEGATIVE_ONE);
        invert_fail_helper(ONE, Rational.ZERO);
        invert_fail_helper(ONE, Rational.NEGATIVE_ONE);
        invert_fail_helper(PI, Rational.ZERO);
        invert_fail_helper(PI, Rational.NEGATIVE_ONE);
    }

    private static void divide_int_helper(@NotNull Real a, int b, @NotNull String output) {
        Real x = a.divide(b);
        x.validate();
        aeq(x, output);
    }

    private static void divide_int_fail_helper(@NotNull Real a, int b) {
        try {
            a.divide(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        divide_int_helper(ZERO, 1, "0");
        divide_int_helper(ZERO, -1, "0");
        divide_int_helper(ZERO, 100, "0");

        divide_int_helper(ONE, 1, "1");
        divide_int_helper(ONE, -1, "-1");
        divide_int_helper(ONE, 100, "0.01");

        divide_int_helper(NEGATIVE_ONE, 1, "-1");
        divide_int_helper(NEGATIVE_ONE, -1, "1");
        divide_int_helper(NEGATIVE_ONE, 100, "-0.01");

        divide_int_helper(ONE_HALF, 1, "0.5");
        divide_int_helper(ONE_HALF, -1, "-0.5");
        divide_int_helper(ONE_HALF, 100, "0.005");

        divide_int_helper(NEGATIVE_FOUR_THIRDS, 1, "-1.33333333333333333333...");
        divide_int_helper(NEGATIVE_FOUR_THIRDS, -1, "1.33333333333333333333...");
        divide_int_helper(NEGATIVE_FOUR_THIRDS, 100, "-0.01333333333333333333...");

        divide_int_helper(SQRT_TWO, 1, "1.41421356237309504880...");
        divide_int_helper(SQRT_TWO, -1, "-1.41421356237309504880...");
        divide_int_helper(SQRT_TWO, 100, "0.01414213562373095048...");

        divide_int_helper(E, 1, "2.71828182845904523536...");
        divide_int_helper(E, -1, "-2.71828182845904523536...");
        divide_int_helper(E, 100, "0.02718281828459045235...");

        divide_int_helper(PI, 1, "3.14159265358979323846...");
        divide_int_helper(PI, -1, "-3.14159265358979323846...");
        divide_int_helper(PI, 100, "0.03141592653589793238...");

        divide_int_helper(leftFuzzyRepresentation(Rational.ZERO), 1, "-0.00000000000000000000...");
        divide_int_helper(leftFuzzyRepresentation(Rational.ZERO), -1, "0.00000000000000000000...");
        divide_int_helper(leftFuzzyRepresentation(Rational.ZERO), 100, "-0.00000000000000000000...");

        divide_int_helper(rightFuzzyRepresentation(Rational.ZERO), 1, "0.00000000000000000000...");
        divide_int_helper(rightFuzzyRepresentation(Rational.ZERO), -1, "-0.00000000000000000000...");
        divide_int_helper(rightFuzzyRepresentation(Rational.ZERO), 100, "0.00000000000000000000...");

        divide_int_helper(fuzzyRepresentation(Rational.ZERO), 1, "~0");
        divide_int_helper(fuzzyRepresentation(Rational.ZERO), -1, "~0");
        divide_int_helper(fuzzyRepresentation(Rational.ZERO), 100, "~0");

        divide_int_fail_helper(ZERO, 0);
        divide_int_fail_helper(ONE, 0);
        divide_int_fail_helper(NEGATIVE_ONE, 0);
        divide_int_fail_helper(ONE_HALF, 0);
        divide_int_fail_helper(NEGATIVE_FOUR_THIRDS, 0);
        divide_int_fail_helper(SQRT_TWO, 0);
        divide_int_fail_helper(E, 0);
        divide_int_fail_helper(PI, 0);
        divide_int_fail_helper(leftFuzzyRepresentation(Rational.ZERO), 0);
        divide_int_fail_helper(rightFuzzyRepresentation(Rational.ZERO), 0);
        divide_int_fail_helper(fuzzyRepresentation(Rational.ZERO), 0);
    }

    private static void divide_BigInteger_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        Real x = a.divide(Readers.readBigIntegerStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull Real a, @NotNull String b) {
        try {
            a.divide(Readers.readBigIntegerStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        divide_BigInteger_helper(ZERO, "1", "0");
        divide_BigInteger_helper(ZERO, "-1", "0");
        divide_BigInteger_helper(ZERO, "100", "0");

        divide_BigInteger_helper(ONE, "1", "1");
        divide_BigInteger_helper(ONE, "-1", "-1");
        divide_BigInteger_helper(ONE, "100", "0.01");

        divide_BigInteger_helper(NEGATIVE_ONE, "1", "-1");
        divide_BigInteger_helper(NEGATIVE_ONE, "-1", "1");
        divide_BigInteger_helper(NEGATIVE_ONE, "100", "-0.01");

        divide_BigInteger_helper(ONE_HALF, "1", "0.5");
        divide_BigInteger_helper(ONE_HALF, "-1", "-0.5");
        divide_BigInteger_helper(ONE_HALF, "100", "0.005");

        divide_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "1", "-1.33333333333333333333...");
        divide_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "-1", "1.33333333333333333333...");
        divide_BigInteger_helper(NEGATIVE_FOUR_THIRDS, "100", "-0.01333333333333333333...");

        divide_BigInteger_helper(SQRT_TWO, "1", "1.41421356237309504880...");
        divide_BigInteger_helper(SQRT_TWO, "-1", "-1.41421356237309504880...");
        divide_BigInteger_helper(SQRT_TWO, "100", "0.01414213562373095048...");

        divide_BigInteger_helper(E, "1", "2.71828182845904523536...");
        divide_BigInteger_helper(E, "-1", "-2.71828182845904523536...");
        divide_BigInteger_helper(E, "100", "0.02718281828459045235...");

        divide_BigInteger_helper(PI, "1", "3.14159265358979323846...");
        divide_BigInteger_helper(PI, "-1", "-3.14159265358979323846...");
        divide_BigInteger_helper(PI, "100", "0.03141592653589793238...");

        divide_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "-0.00000000000000000000...");
        divide_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", "0.00000000000000000000...");
        divide_BigInteger_helper(leftFuzzyRepresentation(Rational.ZERO), "100", "-0.00000000000000000000...");

        divide_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "0.00000000000000000000...");
        divide_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", "-0.00000000000000000000...");
        divide_BigInteger_helper(rightFuzzyRepresentation(Rational.ZERO), "100", "0.00000000000000000000...");

        divide_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "1", "~0");
        divide_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "-1", "~0");
        divide_BigInteger_helper(fuzzyRepresentation(Rational.ZERO), "100", "~0");

        divide_BigInteger_fail_helper(ZERO, "0");
        divide_BigInteger_fail_helper(ONE, "0");
        divide_BigInteger_fail_helper(NEGATIVE_ONE, "0");
        divide_BigInteger_fail_helper(ONE_HALF, "0");
        divide_BigInteger_fail_helper(NEGATIVE_FOUR_THIRDS, "0");
        divide_BigInteger_fail_helper(SQRT_TWO, "0");
        divide_BigInteger_fail_helper(E, "0");
        divide_BigInteger_fail_helper(PI, "0");
        divide_BigInteger_fail_helper(leftFuzzyRepresentation(Rational.ZERO), "0");
        divide_BigInteger_fail_helper(rightFuzzyRepresentation(Rational.ZERO), "0");
        divide_BigInteger_fail_helper(fuzzyRepresentation(Rational.ZERO), "0");
    }

    private static void divide_Rational_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        Real x = a.divide(Rational.readStrict(b).get());
        x.validate();
        aeq(x, output);
    }

    private static void divide_Rational_fail_helper(@NotNull Real a, @NotNull String b) {
        try {
            a.divide(Rational.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper(ZERO, "1", "0");
        divide_Rational_helper(ZERO, "-1", "0");
        divide_Rational_helper(ZERO, "100/3", "0");
        divide_Rational_helper(ZERO, "1/100", "0");

        divide_Rational_helper(ONE, "1", "1");
        divide_Rational_helper(ONE, "-1", "-1");
        divide_Rational_helper(ONE, "100/3", "0.03");
        divide_Rational_helper(ONE, "1/100", "100");

        divide_Rational_helper(NEGATIVE_ONE, "1", "-1");
        divide_Rational_helper(NEGATIVE_ONE, "-1", "1");
        divide_Rational_helper(NEGATIVE_ONE, "100/3", "-0.03");
        divide_Rational_helper(NEGATIVE_ONE, "1/100", "-100");

        divide_Rational_helper(ONE_HALF, "1", "0.5");
        divide_Rational_helper(ONE_HALF, "-1", "-0.5");
        divide_Rational_helper(ONE_HALF, "100/3", "0.015");
        divide_Rational_helper(ONE_HALF, "1/100", "50");

        divide_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", "-1.33333333333333333333...");
        divide_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", "1.33333333333333333333...");
        divide_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", "-0.04");
        divide_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", "-133.33333333333333333333...");

        divide_Rational_helper(SQRT_TWO, "1", "1.41421356237309504880...");
        divide_Rational_helper(SQRT_TWO, "-1", "-1.41421356237309504880...");
        divide_Rational_helper(SQRT_TWO, "100/3", "0.04242640687119285146...");
        divide_Rational_helper(SQRT_TWO, "1/100", "141.42135623730950488016...");

        divide_Rational_helper(E, "1", "2.71828182845904523536...");
        divide_Rational_helper(E, "-1", "-2.71828182845904523536...");
        divide_Rational_helper(E, "100/3", "0.08154845485377135706...");
        divide_Rational_helper(E, "1/100", "271.82818284590452353602...");

        divide_Rational_helper(PI, "1", "3.14159265358979323846...");
        divide_Rational_helper(PI, "-1", "-3.14159265358979323846...");
        divide_Rational_helper(PI, "100/3", "0.09424777960769379715...");
        divide_Rational_helper(PI, "1/100", "314.15926535897932384626...");

        divide_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "-0.00000000000000000000...");
        divide_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", "0.00000000000000000000...");
        divide_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", "-0.00000000000000000000...");
        divide_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", "-0.00000000000000000000...");

        divide_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "0.00000000000000000000...");
        divide_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", "-0.00000000000000000000...");
        divide_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", "0.00000000000000000000...");
        divide_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", "0.00000000000000000000...");

        divide_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", "~0");
        divide_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", "~0");
        divide_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", "~0");
        divide_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", "~0");

        divide_Rational_fail_helper(ZERO, "0");
        divide_Rational_fail_helper(ONE, "0");
        divide_Rational_fail_helper(NEGATIVE_ONE, "0");
        divide_Rational_fail_helper(ONE_HALF, "0");
        divide_Rational_fail_helper(NEGATIVE_FOUR_THIRDS, "0");
        divide_Rational_fail_helper(SQRT_TWO, "0");
        divide_Rational_fail_helper(E, "0");
        divide_Rational_fail_helper(PI, "0");
        divide_Rational_fail_helper(leftFuzzyRepresentation(Rational.ZERO), "0");
        divide_Rational_fail_helper(rightFuzzyRepresentation(Rational.ZERO), "0");
        divide_Rational_fail_helper(fuzzyRepresentation(Rational.ZERO), "0");
    }

    private static void divideUnsafe_helper(@NotNull Real a, @NotNull Real b, @NotNull String output) {
        Real x = a.divideUnsafe(b);
        x.validate();
        aeq(x, output);
    }

    private static void divideUnsafe_fail_helper(@NotNull Real a, @NotNull Real b) {
        try {
            a.divideUnsafe(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideUnsafe() {
        divideUnsafe_helper(ZERO, ONE, "0");
        divideUnsafe_helper(ZERO, NEGATIVE_FOUR_THIRDS, "0");
        divideUnsafe_helper(ZERO, SQRT_TWO, "0");
        divideUnsafe_helper(ZERO, E, "0");
        divideUnsafe_helper(ZERO, PI, "0");

        divideUnsafe_helper(ONE, ONE, "1");
        divideUnsafe_helper(ONE, NEGATIVE_FOUR_THIRDS, "-0.75");
        divideUnsafe_helper(ONE, SQRT_TWO, "0.70710678118654752440...");
        divideUnsafe_helper(ONE, E, "0.36787944117144232159...");
        divideUnsafe_helper(ONE, PI, "0.31830988618379067153...");

        divideUnsafe_helper(NEGATIVE_FOUR_THIRDS, ONE, "-1.33333333333333333333...");
        divideUnsafe_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, "1");
        divideUnsafe_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, "-0.94280904158206336586...");
        divideUnsafe_helper(NEGATIVE_FOUR_THIRDS, E, "-0.49050592156192309546...");
        divideUnsafe_helper(NEGATIVE_FOUR_THIRDS, PI, "-0.42441318157838756205...");

        divideUnsafe_helper(SQRT_TWO, ONE, "1.41421356237309504880...");
        divideUnsafe_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, "-1.06066017177982128660...");
        divideUnsafe_helper(SQRT_TWO, SQRT_TWO, "1");
        divideUnsafe_helper(SQRT_TWO, E, "0.52026009502288889635...");
        divideUnsafe_helper(SQRT_TWO, PI, "0.45015815807855303477...");

        divideUnsafe_helper(E, ONE, "2.71828182845904523536...");
        divideUnsafe_helper(E, NEGATIVE_FOUR_THIRDS, "-2.03871137134428392652...");
        divideUnsafe_helper(E, SQRT_TWO, "1.92211551407955841243...");
        divideUnsafe_helper(E, E, "1");
        divideUnsafe_helper(E, PI, "0.86525597943226508721...");

        divideUnsafe_helper(PI, ONE, "3.14159265358979323846...");
        divideUnsafe_helper(PI, NEGATIVE_FOUR_THIRDS, "-2.35619449019234492884...");
        divideUnsafe_helper(PI, SQRT_TWO, "2.22144146907918312350...");
        divideUnsafe_helper(PI, E, "1.15572734979092171791...");
        divideUnsafe_helper(PI, PI, "1");

        divideUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, "-0.00000000000000000000...");
        divideUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "0.00000000000000000000...");
        divideUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "-0.00000000000000000000...");
        divideUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), E, "-0.00000000000000000000...");
        divideUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), PI, "-0.00000000000000000000...");

        divideUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, "0.00000000000000000000...");
        divideUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS,
                "-0.00000000000000000000...");
        divideUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "0.00000000000000000000...");
        divideUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), E, "0.00000000000000000000...");
        divideUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), PI, "0.00000000000000000000...");

        divideUnsafe_helper(fuzzyRepresentation(Rational.ZERO), ONE, "~0");
        divideUnsafe_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, "~0");
        divideUnsafe_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, "~0");
        divideUnsafe_helper(fuzzyRepresentation(Rational.ZERO), E, "~0");
        divideUnsafe_helper(fuzzyRepresentation(Rational.ZERO), PI, "~0");

        divideUnsafe_helper(PI, PI.invertUnsafe().invertUnsafe(), "~1");

        divideUnsafe_fail_helper(ZERO, ZERO);
        divideUnsafe_fail_helper(ONE, ZERO);
        divideUnsafe_fail_helper(NEGATIVE_ONE, ZERO);
        divideUnsafe_fail_helper(ONE_HALF, ZERO);
        divideUnsafe_fail_helper(NEGATIVE_FOUR_THIRDS, ZERO);
        divideUnsafe_fail_helper(SQRT_TWO, ZERO);
        divideUnsafe_fail_helper(E, ZERO);
        divideUnsafe_fail_helper(PI, ZERO);
        divideUnsafe_fail_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO);
        divideUnsafe_fail_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO);
        divideUnsafe_fail_helper(fuzzyRepresentation(Rational.ZERO), ZERO);
    }

    private static void divide_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        Optional<Real> ox = a.divide(b, resolution);
        if (ox.isPresent()) {
            ox.get().validate();
        }
        aeq(ox, output);
    }

    private static void divide_Real_Rational_fail_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution
    ) {
        try {
            a.divide(b, resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testDivide_Real_Rational() {
        divide_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[0]");
        divide_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[0]");
        divide_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[0]");
        divide_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[0]");
        divide_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[0]");
        divide_Real_Rational_helper(
                ZERO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                ZERO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        divide_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[1]");
        divide_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[-0.75]");
        divide_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[0.70710678118654752440...]");
        divide_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[0.36787944117144232159...]");
        divide_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[0.31830988618379067153...]");
        divide_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        divide_Real_Rational_helper(
                ONE,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                ONE,
                DEFAULT_RESOLUTION,
                "Optional[-1.33333333333333333333...]"
        );
        divide_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[1]");
        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[-0.94280904158206336586...]"
        );
        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                E,
                DEFAULT_RESOLUTION,
                "Optional[-0.49050592156192309546...]"
        );
        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                PI,
                DEFAULT_RESOLUTION,
                "Optional[-0.42441318157838756205...]"
        );
        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        divide_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[1.41421356237309504880...]");
        divide_Real_Rational_helper(
                SQRT_TWO,
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[-1.06066017177982128660...]"
        );
        divide_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1]");
        divide_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[0.52026009502288889635...]");
        divide_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[0.45015815807855303477...]");
        divide_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                SQRT_TWO,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        divide_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[2.71828182845904523536...]");
        divide_Real_Rational_helper(
                E,
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[-2.03871137134428392652...]"
        );
        divide_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1.92211551407955841243...]");
        divide_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[1]");
        divide_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION,"Optional[0.86525597943226508721...]");
        divide_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        divide_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        divide_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        divide_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[3.14159265358979323846...]");
        divide_Real_Rational_helper(
                PI,
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[-2.35619449019234492884...]"
        );
        divide_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[2.22144146907918312350...]");
        divide_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[1.15572734979092171791...]");
        divide_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[1]");
        divide_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        divide_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        divide_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                ONE,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                E,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]");
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                PI,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]");
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                ONE,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                E,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                PI,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        divide_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[~0]");
        divide_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[~0]"
        );
        divide_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[~0]");
        divide_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[~0]");
        divide_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[~0]");
        divide_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        divide_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        divide_Real_Rational_helper(PI, PI.invertUnsafe().invertUnsafe(), DEFAULT_RESOLUTION, "Optional[~1]");

        divide_Real_Rational_fail_helper(ZERO, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(ONE, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(NEGATIVE_ONE, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(ONE_HALF, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(E, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(PI, ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION);
        divide_Real_Rational_fail_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION);

        divide_Real_Rational_fail_helper(ZERO, ONE, Rational.ZERO);
        divide_Real_Rational_fail_helper(ZERO, ONE, Rational.NEGATIVE_ONE);
        divide_Real_Rational_fail_helper(PI, PI, Rational.ZERO);
        divide_Real_Rational_fail_helper(PI, PI, Rational.NEGATIVE_ONE);
        divide_Real_Rational_fail_helper(PI, TWO, Rational.ZERO);
        divide_Real_Rational_fail_helper(PI, TWO, Rational.NEGATIVE_ONE);
        divide_Real_Rational_fail_helper(TWO, PI, Rational.ZERO);
        divide_Real_Rational_fail_helper(TWO, PI, Rational.NEGATIVE_ONE);
        divide_Real_Rational_fail_helper(PI, E, Rational.ZERO);
        divide_Real_Rational_fail_helper(PI, E, Rational.NEGATIVE_ONE);
    }

    private static void shiftLeft_helper(@NotNull Real x, int bits, @NotNull String output) {
        Real y = x.shiftLeft(bits);
        y.validate();
        aeq(y, output);
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper(ZERO, 0, "0");
        shiftLeft_helper(ZERO, 1, "0");
        shiftLeft_helper(ZERO, 2, "0");
        shiftLeft_helper(ZERO, 3, "0");
        shiftLeft_helper(ZERO, 4, "0");
        shiftLeft_helper(ZERO, -1, "0");
        shiftLeft_helper(ZERO, -2, "0");
        shiftLeft_helper(ZERO, -3, "0");
        shiftLeft_helper(ZERO, -4, "0");

        shiftLeft_helper(ONE, 0, "1");
        shiftLeft_helper(ONE, 1, "2");
        shiftLeft_helper(ONE, 2, "4");
        shiftLeft_helper(ONE, 3, "8");
        shiftLeft_helper(ONE, 4, "16");
        shiftLeft_helper(ONE, -1, "0.5");
        shiftLeft_helper(ONE, -2, "0.25");
        shiftLeft_helper(ONE, -3, "0.125");
        shiftLeft_helper(ONE, -4, "0.0625");

        shiftLeft_helper(NEGATIVE_ONE, 0, "-1");
        shiftLeft_helper(NEGATIVE_ONE, 1, "-2");
        shiftLeft_helper(NEGATIVE_ONE, 2, "-4");
        shiftLeft_helper(NEGATIVE_ONE, 3, "-8");
        shiftLeft_helper(NEGATIVE_ONE, 4, "-16");
        shiftLeft_helper(NEGATIVE_ONE, -1, "-0.5");
        shiftLeft_helper(NEGATIVE_ONE, -2, "-0.25");
        shiftLeft_helper(NEGATIVE_ONE, -3, "-0.125");
        shiftLeft_helper(NEGATIVE_ONE, -4, "-0.0625");

        shiftLeft_helper(ONE_HALF, 0, "0.5");
        shiftLeft_helper(ONE_HALF, 1, "1");
        shiftLeft_helper(ONE_HALF, 2, "2");
        shiftLeft_helper(ONE_HALF, 3, "4");
        shiftLeft_helper(ONE_HALF, 4, "8");
        shiftLeft_helper(ONE_HALF, -1, "0.25");
        shiftLeft_helper(ONE_HALF, -2, "0.125");
        shiftLeft_helper(ONE_HALF, -3, "0.0625");
        shiftLeft_helper(ONE_HALF, -4, "0.03125");

        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, 0, "-1.33333333333333333333...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, 1, "-2.66666666666666666666...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, 2, "-5.33333333333333333333...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, 3, "-10.66666666666666666666...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, 4, "-21.33333333333333333333...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, -1, "-0.66666666666666666666...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, -2, "-0.33333333333333333333...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, -3, "-0.16666666666666666666...");
        shiftLeft_helper(NEGATIVE_FOUR_THIRDS, -4, "-0.08333333333333333333...");

        shiftLeft_helper(SQRT_TWO, 0, "1.41421356237309504880...");
        shiftLeft_helper(SQRT_TWO, 1, "2.82842712474619009760...");
        shiftLeft_helper(SQRT_TWO, 2, "5.65685424949238019520...");
        shiftLeft_helper(SQRT_TWO, 3, "11.31370849898476039041...");
        shiftLeft_helper(SQRT_TWO, 4, "22.62741699796952078082...");
        shiftLeft_helper(SQRT_TWO, -1, "0.70710678118654752440...");
        shiftLeft_helper(SQRT_TWO, -2, "0.35355339059327376220...");
        shiftLeft_helper(SQRT_TWO, -3, "0.17677669529663688110...");
        shiftLeft_helper(SQRT_TWO, -4, "0.08838834764831844055...");

        shiftLeft_helper(E, 0, "2.71828182845904523536...");
        shiftLeft_helper(E, 1, "5.43656365691809047072...");
        shiftLeft_helper(E, 2, "10.87312731383618094144...");
        shiftLeft_helper(E, 3, "21.74625462767236188288...");
        shiftLeft_helper(E, 4, "43.49250925534472376576...");
        shiftLeft_helper(E, -1, "1.35914091422952261768...");
        shiftLeft_helper(E, -2, "0.67957045711476130884...");
        shiftLeft_helper(E, -3, "0.33978522855738065442...");
        shiftLeft_helper(E, -4, "0.16989261427869032721...");

        shiftLeft_helper(PI, 0, "3.14159265358979323846...");
        shiftLeft_helper(PI, 1, "6.28318530717958647692...");
        shiftLeft_helper(PI, 2, "12.56637061435917295385...");
        shiftLeft_helper(PI, 3, "25.13274122871834590770...");
        shiftLeft_helper(PI, 4, "50.26548245743669181540...");
        shiftLeft_helper(PI, -1, "1.57079632679489661923...");
        shiftLeft_helper(PI, -2, "0.78539816339744830961...");
        shiftLeft_helper(PI, -3, "0.39269908169872415480...");
        shiftLeft_helper(PI, -4, "0.19634954084936207740...");

        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), 0, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), 1, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), 2, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), 3, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), 4, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), -1, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), -2, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), -3, "-0.00000000000000000000...");
        shiftLeft_helper(leftFuzzyRepresentation(Rational.ZERO), -4, "-0.00000000000000000000...");

        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), 0, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), 1, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), 2, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), 3, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), 4, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), -1, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), -2, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), -3, "0.00000000000000000000...");
        shiftLeft_helper(rightFuzzyRepresentation(Rational.ZERO), -4, "0.00000000000000000000...");

        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), 0, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), 1, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), 2, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), 3, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), 4, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), -1, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), -2, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), -3, "~0");
        shiftLeft_helper(fuzzyRepresentation(Rational.ZERO), -4, "~0");
    }

    private static void shiftRight_helper(@NotNull Real x, int bits, @NotNull String output) {
        Real y = x.shiftRight(bits);
        y.validate();
        aeq(y, output);
    }

    @Test
    public void testShiftRight() {
        shiftRight_helper(ZERO, 0, "0");
        shiftRight_helper(ZERO, 1, "0");
        shiftRight_helper(ZERO, 2, "0");
        shiftRight_helper(ZERO, 3, "0");
        shiftRight_helper(ZERO, 4, "0");
        shiftRight_helper(ZERO, -1, "0");
        shiftRight_helper(ZERO, -2, "0");
        shiftRight_helper(ZERO, -3, "0");
        shiftRight_helper(ZERO, -4, "0");

        shiftRight_helper(ONE, 0, "1");
        shiftRight_helper(ONE, 1, "0.5");
        shiftRight_helper(ONE, 2, "0.25");
        shiftRight_helper(ONE, 3, "0.125");
        shiftRight_helper(ONE, 4, "0.0625");
        shiftRight_helper(ONE, -1, "2");
        shiftRight_helper(ONE, -2, "4");
        shiftRight_helper(ONE, -3, "8");
        shiftRight_helper(ONE, -4, "16");

        shiftRight_helper(NEGATIVE_ONE, 0, "-1");
        shiftRight_helper(NEGATIVE_ONE, 1, "-0.5");
        shiftRight_helper(NEGATIVE_ONE, 2, "-0.25");
        shiftRight_helper(NEGATIVE_ONE, 3, "-0.125");
        shiftRight_helper(NEGATIVE_ONE, 4, "-0.0625");
        shiftRight_helper(NEGATIVE_ONE, -1, "-2");
        shiftRight_helper(NEGATIVE_ONE, -2, "-4");
        shiftRight_helper(NEGATIVE_ONE, -3, "-8");
        shiftRight_helper(NEGATIVE_ONE, -4, "-16");

        shiftRight_helper(ONE_HALF, 0, "0.5");
        shiftRight_helper(ONE_HALF, 1, "0.25");
        shiftRight_helper(ONE_HALF, 2, "0.125");
        shiftRight_helper(ONE_HALF, 3, "0.0625");
        shiftRight_helper(ONE_HALF, 4, "0.03125");
        shiftRight_helper(ONE_HALF, -1, "1");
        shiftRight_helper(ONE_HALF, -2, "2");
        shiftRight_helper(ONE_HALF, -3, "4");
        shiftRight_helper(ONE_HALF, -4, "8");

        shiftRight_helper(NEGATIVE_FOUR_THIRDS, 0, "-1.33333333333333333333...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, 1, "-0.66666666666666666666...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, 2, "-0.33333333333333333333...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, 3, "-0.16666666666666666666...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, 4, "-0.08333333333333333333...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, -1, "-2.66666666666666666666...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, -2, "-5.33333333333333333333...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, -3, "-10.66666666666666666666...");
        shiftRight_helper(NEGATIVE_FOUR_THIRDS, -4, "-21.33333333333333333333...");

        shiftRight_helper(SQRT_TWO, 0, "1.41421356237309504880...");
        shiftRight_helper(SQRT_TWO, 1, "0.70710678118654752440...");
        shiftRight_helper(SQRT_TWO, 2, "0.35355339059327376220...");
        shiftRight_helper(SQRT_TWO, 3, "0.17677669529663688110...");
        shiftRight_helper(SQRT_TWO, 4, "0.08838834764831844055...");
        shiftRight_helper(SQRT_TWO, -1, "2.82842712474619009760...");
        shiftRight_helper(SQRT_TWO, -2, "5.65685424949238019520...");
        shiftRight_helper(SQRT_TWO, -3, "11.31370849898476039041...");
        shiftRight_helper(SQRT_TWO, -4, "22.62741699796952078082...");

        shiftRight_helper(E, 0, "2.71828182845904523536...");
        shiftRight_helper(E, 1, "1.35914091422952261768...");
        shiftRight_helper(E, 2, "0.67957045711476130884...");
        shiftRight_helper(E, 3, "0.33978522855738065442...");
        shiftRight_helper(E, 4, "0.16989261427869032721...");
        shiftRight_helper(E, -1, "5.43656365691809047072...");
        shiftRight_helper(E, -2, "10.87312731383618094144...");
        shiftRight_helper(E, -3, "21.74625462767236188288...");
        shiftRight_helper(E, -4, "43.49250925534472376576...");

        shiftRight_helper(PI, 0, "3.14159265358979323846...");
        shiftRight_helper(PI, 1, "1.57079632679489661923...");
        shiftRight_helper(PI, 2, "0.78539816339744830961...");
        shiftRight_helper(PI, 3, "0.39269908169872415480...");
        shiftRight_helper(PI, 4, "0.19634954084936207740...");
        shiftRight_helper(PI, -1, "6.28318530717958647692...");
        shiftRight_helper(PI, -2, "12.56637061435917295385...");
        shiftRight_helper(PI, -3, "25.13274122871834590770...");
        shiftRight_helper(PI, -4, "50.26548245743669181540...");

        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), 0, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), 1, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), 2, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), 3, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), 4, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), -1, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), -2, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), -3, "-0.00000000000000000000...");
        shiftRight_helper(leftFuzzyRepresentation(Rational.ZERO), -4, "-0.00000000000000000000...");

        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), 0, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), 1, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), 2, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), 3, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), 4, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), -1, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), -2, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), -3, "0.00000000000000000000...");
        shiftRight_helper(rightFuzzyRepresentation(Rational.ZERO), -4, "0.00000000000000000000...");

        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), 0, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), 1, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), 2, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), 3, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), 4, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), -1, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), -2, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), -3, "~0");
        shiftRight_helper(fuzzyRepresentation(Rational.ZERO), -4, "~0");
    }

    private static void sum_helper(@NotNull List<Real> input, @NotNull String output) {
        Real x = sum(input);
        x.validate();
        aeq(x, output);
    }

    private static void sum_fail_helper(@NotNull List<Real> input) {
        try {
            sum(input);
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper(Collections.emptyList(), "0");
        sum_helper(Collections.singletonList(ONE), "1");
        sum_helper(Collections.singletonList(SQRT_TWO), "1.41421356237309504880...");
        sum_helper(Arrays.asList(ZERO, ONE, SQRT_TWO, E, PI), "8.27408804442193352262...");
        sum_helper(Arrays.asList(SQRT_TWO, SQRT_TWO.negate()), "~0");

        sum_fail_helper(Arrays.asList(ZERO, null, SQRT_TWO));
    }

    private static void product_helper(@NotNull List<Real> input, @NotNull String output) {
        Real x = product(input);
        x.validate();
        aeq(x, output);
    }

    private static void product_fail_helper(@NotNull List<Real> input) {
        try {
            product(input);
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        product_helper(Collections.emptyList(), "1");
        product_helper(Collections.singletonList(NEGATIVE_ONE), "-1");
        product_helper(Collections.singletonList(SQRT_TWO), "1.41421356237309504880...");
        product_helper(Arrays.asList(ONE, NEGATIVE_FOUR_THIRDS, SQRT_TWO, E, PI), "-16.10267727568882533260...");
        product_helper(Arrays.asList(ZERO, ONE, SQRT_TWO, E, PI), "0");
        product_helper(Arrays.asList(fuzzyRepresentation(Rational.ZERO), ONE, SQRT_TWO, E, PI), "~0");
        product_helper(Arrays.asList(SQRT_TWO, SQRT_TWO.invertUnsafe()), "~1");

        product_fail_helper(Arrays.asList(ZERO, null, SQRT_TWO));
    }

    private static void delta_helper(@NotNull Iterable<Real> input, @NotNull String output) {
        Iterable<Real> xs = delta(input);
        take(TINY_LIMIT, xs).forEach(Real::validate);
        aeqitLimit(TINY_LIMIT, xs, output);
    }

    private static void delta_fail_helper(@NotNull Iterable<Real> input) {
        try {
            toList(delta(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper(Collections.singletonList(SQRT_TWO), "[]");
        delta_helper(Arrays.asList(ONE, NEGATIVE_FOUR_THIRDS, SQRT_TWO, E, PI),
                "[-2.33333333333333333333..., 2.74754689570642838213..., 1.30406826608595018655...," +
                " 0.42331082513074800310...]");
        delta_helper(map(Real::champernowne, EP.rangeUpIncreasing(IntegerUtils.TWO)),
                "[-0.26328195832962057905..., -0.17284705642732292673..., -0.11537499999999995465...," +
                " -0.07087342529604434366..., -0.04542715072882624597..., -0.03117072298102372410...," +
                " -0.02263983598552001488..., -0.01716818701858465106..., -0.01345678914037061233...," +
                " -0.01082644624309960337..., -0.00889577593990761194..., -0.00743754109134504499...," +
                " -0.00630962444149133416..., -0.00541950113378681436..., -0.00470486111111111025...," +
                " -0.00412251297577854669..., -0.00364176171557947797..., -0.00324031325878047946...," +
                " -0.00290166204986149584..., -0.00261337868480725623..., ...]");

        delta_fail_helper(Collections.emptyList());
        delta_fail_helper(Arrays.asList(ZERO, null, SQRT_TWO));
    }

    private static void powUnsafe_int_helper(@NotNull Real x, int p, @NotNull String output) {
        Real y = x.powUnsafe(p);
        y.validate();
        aeq(y, output);
    }

    private static void powUnsafe_int_fail_helper(@NotNull Real x, int p) {
        try {
            x.powUnsafe(p);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPowUnsafe_int() {
        powUnsafe_int_helper(ZERO, 0, "1");
        powUnsafe_int_helper(ZERO, 1, "0");
        powUnsafe_int_helper(ZERO, 2, "0");
        powUnsafe_int_helper(ZERO, 3, "0");
        powUnsafe_int_helper(ZERO, 100, "0");

        powUnsafe_int_helper(ONE, 0, "1");
        powUnsafe_int_helper(ONE, 1, "1");
        powUnsafe_int_helper(ONE, 2, "1");
        powUnsafe_int_helper(ONE, 3, "1");
        powUnsafe_int_helper(ONE, 100, "1");
        powUnsafe_int_helper(ONE, -1, "1");
        powUnsafe_int_helper(ONE, -2, "1");
        powUnsafe_int_helper(ONE, -3, "1");
        powUnsafe_int_helper(ONE, -100, "1");

        powUnsafe_int_helper(NEGATIVE_ONE, 0, "1");
        powUnsafe_int_helper(NEGATIVE_ONE, 1, "-1");
        powUnsafe_int_helper(NEGATIVE_ONE, 2, "1");
        powUnsafe_int_helper(NEGATIVE_ONE, 3, "-1");
        powUnsafe_int_helper(NEGATIVE_ONE, 100, "1");
        powUnsafe_int_helper(NEGATIVE_ONE, -1, "-1");
        powUnsafe_int_helper(NEGATIVE_ONE, -2, "1");
        powUnsafe_int_helper(NEGATIVE_ONE, -3, "-1");
        powUnsafe_int_helper(NEGATIVE_ONE, -100, "1");

        powUnsafe_int_helper(ONE_HALF, 0, "1");
        powUnsafe_int_helper(ONE_HALF, 1, "0.5");
        powUnsafe_int_helper(ONE_HALF, 2, "0.25");
        powUnsafe_int_helper(ONE_HALF, 3, "0.125");
        powUnsafe_int_helper(ONE_HALF, 100, "0.00000000000000000000...");
        powUnsafe_int_helper(ONE_HALF, -1, "2");
        powUnsafe_int_helper(ONE_HALF, -2, "4");
        powUnsafe_int_helper(ONE_HALF, -3, "8");
        powUnsafe_int_helper(ONE_HALF, -100, "1267650600228229401496703205376");

        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 0, "1");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 1, "-1.33333333333333333333...");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 2, "1.77777777777777777777...");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 3, "-2.37037037037037037037...");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, 100, "3117982410207.94197872148815582983...");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, -1, "-0.75");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, -2, "0.5625");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, -3, "-0.421875");
        powUnsafe_int_helper(NEGATIVE_FOUR_THIRDS, -100, "0.00000000000032072021...");

        powUnsafe_int_helper(SQRT_TWO, 0, "1");
        powUnsafe_int_helper(SQRT_TWO, 1, "1.41421356237309504880...");
        powUnsafe_int_helper(SQRT_TWO, 2, "~2");
        powUnsafe_int_helper(SQRT_TWO, 3, "2.82842712474619009760...");
        powUnsafe_int_helper(SQRT_TWO, 100, "~1125899906842624");
        powUnsafe_int_helper(SQRT_TWO, -1, "0.70710678118654752440...");
        powUnsafe_int_helper(SQRT_TWO, -2, "~0.5");
        powUnsafe_int_helper(SQRT_TWO, -3, "0.35355339059327376220...");
        powUnsafe_int_helper(SQRT_TWO, -100, "0.00000000000000088817...");

        powUnsafe_int_helper(E, 0, "1");
        powUnsafe_int_helper(E, 1, "2.71828182845904523536...");
        powUnsafe_int_helper(E, 2, "7.38905609893065022723...");
        powUnsafe_int_helper(E, 3, "20.08553692318766774092...");
        powUnsafe_int_helper(E, 100, "26881171418161354484126255515800135873611118.77374192241519160861...");
        powUnsafe_int_helper(E, -1, "0.36787944117144232159...");
        powUnsafe_int_helper(E, -2, "0.13533528323661269189...");
        powUnsafe_int_helper(E, -3, "0.04978706836786394297...");
        powUnsafe_int_helper(E, -100, "0.00000000000000000000...");

        powUnsafe_int_helper(PI, 0, "1");
        powUnsafe_int_helper(PI, 1, "3.14159265358979323846...");
        powUnsafe_int_helper(PI, 2, "9.86960440108935861883...");
        powUnsafe_int_helper(PI, 3, "31.00627668029982017547...");
        powUnsafe_int_helper(PI, 100, "51878483143196131920862615246303013562686760680405.78499007184235808218...");
        powUnsafe_int_helper(PI, -1, "0.31830988618379067153...");
        powUnsafe_int_helper(PI, -2, "0.10132118364233777144...");
        powUnsafe_int_helper(PI, -3, "0.03225153443319948918...");
        powUnsafe_int_helper(PI, -100, "0.00000000000000000000...");

        powUnsafe_int_helper(leftFuzzyRepresentation(Rational.ZERO), 0, "1");
        powUnsafe_int_helper(leftFuzzyRepresentation(Rational.ZERO), 1, "-0.00000000000000000000...");
        powUnsafe_int_helper(leftFuzzyRepresentation(Rational.ZERO), 2, "0.00000000000000000000...");
        powUnsafe_int_helper(leftFuzzyRepresentation(Rational.ZERO), 3, "-0.00000000000000000000...");
        powUnsafe_int_helper(leftFuzzyRepresentation(Rational.ZERO), 100, "0.00000000000000000000...");

        powUnsafe_int_helper(rightFuzzyRepresentation(Rational.ZERO), 0, "1");
        powUnsafe_int_helper(rightFuzzyRepresentation(Rational.ZERO), 1, "0.00000000000000000000...");
        powUnsafe_int_helper(rightFuzzyRepresentation(Rational.ZERO), 2, "0.00000000000000000000...");
        powUnsafe_int_helper(rightFuzzyRepresentation(Rational.ZERO), 3, "0.00000000000000000000...");
        powUnsafe_int_helper(rightFuzzyRepresentation(Rational.ZERO), 100, "0.00000000000000000000...");

        powUnsafe_int_helper(fuzzyRepresentation(Rational.ZERO), 0, "1");
        powUnsafe_int_helper(fuzzyRepresentation(Rational.ZERO), 1, "~0");
        powUnsafe_int_helper(fuzzyRepresentation(Rational.ZERO), 2, "0.00000000000000000000...");
        powUnsafe_int_helper(fuzzyRepresentation(Rational.ZERO), 3, "~0");
        powUnsafe_int_helper(fuzzyRepresentation(Rational.ZERO), 100, "0.00000000000000000000...");

        powUnsafe_int_fail_helper(ZERO, -1);
        powUnsafe_int_fail_helper(ZERO, -2);
        powUnsafe_int_fail_helper(ZERO, -3);
    }

    private static void pow_int_Rational_helper(
            @NotNull Real x,
            int p,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        Optional<Real> oy = x.pow(p, resolution);
        if (oy.isPresent()) {
            oy.get().validate();
        }
        aeq(oy, output);
    }

    private static void pow_int_Rational_fail_helper(@NotNull Real x, int p, @NotNull Rational resolution) {
        try {
            x.pow(p, resolution);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testPow_int_Rational() {
        pow_int_Rational_helper(ZERO, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ZERO, 1, DEFAULT_RESOLUTION, "Optional[0]");
        pow_int_Rational_helper(ZERO, 2, DEFAULT_RESOLUTION, "Optional[0]");
        pow_int_Rational_helper(ZERO, 3, DEFAULT_RESOLUTION, "Optional[0]");
        pow_int_Rational_helper(ZERO, 100, DEFAULT_RESOLUTION, "Optional[0]");

        pow_int_Rational_helper(ONE, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, 1, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, 2, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, 3, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, 100, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, -1, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, -2, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, -3, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE, -100, DEFAULT_RESOLUTION, "Optional[1]");

        pow_int_Rational_helper(NEGATIVE_ONE, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(NEGATIVE_ONE, 1, DEFAULT_RESOLUTION, "Optional[-1]");
        pow_int_Rational_helper(NEGATIVE_ONE, 2, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(NEGATIVE_ONE, 3, DEFAULT_RESOLUTION, "Optional[-1]");
        pow_int_Rational_helper(NEGATIVE_ONE, 100, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(NEGATIVE_ONE, -1, DEFAULT_RESOLUTION, "Optional[-1]");
        pow_int_Rational_helper(NEGATIVE_ONE, -2, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(NEGATIVE_ONE, -3, DEFAULT_RESOLUTION, "Optional[-1]");
        pow_int_Rational_helper(NEGATIVE_ONE, -100, DEFAULT_RESOLUTION, "Optional[1]");

        pow_int_Rational_helper(ONE_HALF, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(ONE_HALF, 1, DEFAULT_RESOLUTION, "Optional[0.5]");
        pow_int_Rational_helper(ONE_HALF, 2, DEFAULT_RESOLUTION, "Optional[0.25]");
        pow_int_Rational_helper(ONE_HALF, 3, DEFAULT_RESOLUTION, "Optional[0.125]");
        pow_int_Rational_helper(ONE_HALF, 100, DEFAULT_RESOLUTION, "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(ONE_HALF, -1, DEFAULT_RESOLUTION, "Optional[2]");
        pow_int_Rational_helper(ONE_HALF, -2, DEFAULT_RESOLUTION, "Optional[4]");
        pow_int_Rational_helper(ONE_HALF, -3, DEFAULT_RESOLUTION, "Optional[8]");
        pow_int_Rational_helper(ONE_HALF, -100, DEFAULT_RESOLUTION, "Optional[1267650600228229401496703205376]");

        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 1, DEFAULT_RESOLUTION, "Optional[-1.33333333333333333333...]");
        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 2, DEFAULT_RESOLUTION, "Optional[1.77777777777777777777...]");
        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, 3, DEFAULT_RESOLUTION, "Optional[-2.37037037037037037037...]");
        pow_int_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                100,
                DEFAULT_RESOLUTION,
                "Optional[3117982410207.94197872148815582983...]"
        );
        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, -1, DEFAULT_RESOLUTION, "Optional[-0.75]");
        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, -2, DEFAULT_RESOLUTION, "Optional[0.5625]");
        pow_int_Rational_helper(NEGATIVE_FOUR_THIRDS, -3, DEFAULT_RESOLUTION, "Optional[-0.421875]");
        pow_int_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                -100,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000032072021...]"
        );

        pow_int_Rational_helper(SQRT_TWO, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(SQRT_TWO, 1, DEFAULT_RESOLUTION, "Optional[1.41421356237309504880...]");
        pow_int_Rational_helper(SQRT_TWO, 2, DEFAULT_RESOLUTION, "Optional[~2]");
        pow_int_Rational_helper(SQRT_TWO, 3, DEFAULT_RESOLUTION, "Optional[2.82842712474619009760...]");
        pow_int_Rational_helper(SQRT_TWO, 100, DEFAULT_RESOLUTION, "Optional[~1125899906842624]");
        pow_int_Rational_helper(SQRT_TWO, -1, DEFAULT_RESOLUTION, "Optional[0.70710678118654752440...]");
        pow_int_Rational_helper(SQRT_TWO, -2, DEFAULT_RESOLUTION, "Optional[~0.5]");
        pow_int_Rational_helper(SQRT_TWO, -3, DEFAULT_RESOLUTION, "Optional[0.35355339059327376220...]");
        pow_int_Rational_helper(SQRT_TWO, -100, DEFAULT_RESOLUTION, "Optional[0.00000000000000088817...]");

        pow_int_Rational_helper(E, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(E, 1, DEFAULT_RESOLUTION, "Optional[2.71828182845904523536...]");
        pow_int_Rational_helper(E, 2, DEFAULT_RESOLUTION, "Optional[7.38905609893065022723...]");
        pow_int_Rational_helper(E, 3, DEFAULT_RESOLUTION, "Optional[20.08553692318766774092...]");
        pow_int_Rational_helper(
                E,
                100,
                DEFAULT_RESOLUTION,
                "Optional[26881171418161354484126255515800135873611118.77374192241519160861...]"
        );
        pow_int_Rational_helper(E, -1, DEFAULT_RESOLUTION, "Optional[0.36787944117144232159...]");
        pow_int_Rational_helper(E, -2, DEFAULT_RESOLUTION, "Optional[0.13533528323661269189...]");
        pow_int_Rational_helper(E, -3, DEFAULT_RESOLUTION, "Optional[0.04978706836786394297...]");
        pow_int_Rational_helper(E, -100, DEFAULT_RESOLUTION, "Optional[0.00000000000000000000...]");

        pow_int_Rational_helper(PI, 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(PI, 1, DEFAULT_RESOLUTION, "Optional[3.14159265358979323846...]");
        pow_int_Rational_helper(PI, 2, DEFAULT_RESOLUTION, "Optional[9.86960440108935861883...]");
        pow_int_Rational_helper(PI, 3, DEFAULT_RESOLUTION, "Optional[31.00627668029982017547...]");
        pow_int_Rational_helper(
                PI,
                100,
                DEFAULT_RESOLUTION,
                "Optional[51878483143196131920862615246303013562686760680405.78499007184235808218...]"
        );
        pow_int_Rational_helper(PI, -1, DEFAULT_RESOLUTION, "Optional[0.31830988618379067153...]");
        pow_int_Rational_helper(PI, -2, DEFAULT_RESOLUTION, "Optional[0.10132118364233777144...]");
        pow_int_Rational_helper(PI, -3, DEFAULT_RESOLUTION, "Optional[0.03225153443319948918...]");
        pow_int_Rational_helper(PI, -100, DEFAULT_RESOLUTION, "Optional[0.00000000000000000000...]");

        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), 1, DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), 2, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), 3, DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), 100, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), -1, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), -2, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), -3, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), -100, DEFAULT_RESOLUTION, "Optional.empty");

        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), 1, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), 2, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), 3, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), 100, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), -1, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), -2, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), -3, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), -100, DEFAULT_RESOLUTION, "Optional.empty");

        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), 0, DEFAULT_RESOLUTION, "Optional[1]");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), 1, DEFAULT_RESOLUTION, "Optional[~0]");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), 2, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), 3, DEFAULT_RESOLUTION, "Optional[~0]");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), 100, DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), -1, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), -2, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), -3, DEFAULT_RESOLUTION, "Optional.empty");
        pow_int_Rational_helper(fuzzyRepresentation(Rational.ZERO), -100, DEFAULT_RESOLUTION, "Optional.empty");

        pow_int_Rational_fail_helper(ZERO, -1, DEFAULT_RESOLUTION);
        pow_int_Rational_fail_helper(ZERO, -2, DEFAULT_RESOLUTION);
        pow_int_Rational_fail_helper(ZERO, -3, DEFAULT_RESOLUTION);

        pow_int_Rational_fail_helper(ZERO, 2, Rational.ZERO);
        pow_int_Rational_fail_helper(ZERO, 2, Rational.NEGATIVE_ONE);
        pow_int_Rational_fail_helper(PI, 2, Rational.ZERO);
        pow_int_Rational_fail_helper(PI, 2, Rational.NEGATIVE_ONE);
    }

    private static void rootOfRational_helper(@NotNull String x, int r, @NotNull String output) {
        Real y = rootOfRational(Rational.readStrict(x).get(), r);
        y.validate();
        aeq(y, output);
    }

    private static void rootOfRational_fail_helper(@NotNull String x, int r) {
        try {
            rootOfRational(Rational.readStrict(x).get(), r);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRootOfRational() {
        rootOfRational_helper("0", 1, "0");
        rootOfRational_helper("0", 2, "0");
        rootOfRational_helper("0", 3, "0");
        rootOfRational_helper("0", 10, "0");

        rootOfRational_helper("1", 1, "1");
        rootOfRational_helper("1", 2, "1");
        rootOfRational_helper("1", 3, "1");
        rootOfRational_helper("1", 10, "1");
        rootOfRational_helper("1", -1, "1");
        rootOfRational_helper("1", -2, "1");
        rootOfRational_helper("1", -3, "1");
        rootOfRational_helper("1", -10, "1");

        rootOfRational_helper("-1", 1, "-1");
        rootOfRational_helper("-1", 3, "-1");
        rootOfRational_helper("-1", 9, "-1");
        rootOfRational_helper("-1", -1, "-1");
        rootOfRational_helper("-1", -3, "-1");
        rootOfRational_helper("-1", -9, "-1");

        rootOfRational_helper("1/2", 1, "0.5");
        rootOfRational_helper("1/2", 2, "0.70710678118654752440...");
        rootOfRational_helper("1/2", 3, "0.79370052598409973737...");
        rootOfRational_helper("1/2", 10, "0.93303299153680741598...");
        rootOfRational_helper("1/2", -1, "2");
        rootOfRational_helper("1/2", -2, "1.41421356237309504880...");
        rootOfRational_helper("1/2", -3, "1.25992104989487316476...");
        rootOfRational_helper("1/2", -10, "1.07177346253629316421...");

        rootOfRational_helper("-4/3", 1, "-1.33333333333333333333...");
        rootOfRational_helper("-4/3", 3, "-1.10064241629820889462...");
        rootOfRational_helper("-4/3", 9, "-1.03248103197612033486...");
        rootOfRational_helper("-4/3", -1, "-0.75");
        rootOfRational_helper("-4/3", -3, "-0.90856029641606982944...");
        rootOfRational_helper("-4/3", -9, "-0.96854079545272307711...");

        rootOfRational_helper("4", 4, "1.41421356237309504880...");
        rootOfRational_helper("1728/117649", 12, "0.70347115030070246117...");

        rootOfRational_fail_helper("0", -1);
        rootOfRational_fail_helper("0", -2);
        rootOfRational_fail_helper("0", -3);
        rootOfRational_fail_helper("1", 0);
        rootOfRational_fail_helper("2", 0);
        rootOfRational_fail_helper("-1", 2);
        rootOfRational_fail_helper("-1", -2);
    }

    private static void sqrtOfRational_helper(@NotNull String x, @NotNull String output) {
        Real y = sqrtOfRational(Rational.readStrict(x).get());
        y.validate();
        aeq(y, output);
    }

    private static void sqrtOfRational_fail_helper(@NotNull String x) {
        try {
            sqrtOfRational(Rational.readStrict(x).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSqrtOfRational() {
        sqrtOfRational_helper("0", "0");
        sqrtOfRational_helper("1", "1");
        sqrtOfRational_helper("1/2", "0.70710678118654752440...");

        sqrtOfRational_fail_helper("-1");
    }

    private static void cbrtOfRational_helper(@NotNull String x, @NotNull String output) {
        Real y = cbrtOfRational(Rational.readStrict(x).get());
        y.validate();
        aeq(y, output);
    }

    @Test
    public void testCbrtOfRational() {
        cbrtOfRational_helper("0", "0");
        cbrtOfRational_helper("1", "1");
        cbrtOfRational_helper("-1", "-1");
        cbrtOfRational_helper("1/2", "0.79370052598409973737...");
        cbrtOfRational_helper("-4/3", "-1.10064241629820889462...");
    }

    private static void rootUnsafe_helper(@NotNull Real x, int r, @NotNull String output) {
        Real y = x.rootUnsafe(r);
        y.validate();
        aeq(y, output);
    }

    private static void rootUnsafe_fail_helper(@NotNull Real x, int r) {
        try {
            //noinspection ResultOfMethodCallIgnored
            x.rootUnsafe(r).toString();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRootUnsafe() {
        rootUnsafe_helper(ZERO, 1, "0");
        rootUnsafe_helper(ZERO, 2, "0");
        rootUnsafe_helper(ZERO, 3, "0");
        rootUnsafe_helper(ZERO, 10, "0");

        rootUnsafe_helper(ONE, 1, "1");
        rootUnsafe_helper(ONE, 2, "1");
        rootUnsafe_helper(ONE, 3, "1");
        rootUnsafe_helper(ONE, 10, "1");
        rootUnsafe_helper(ONE, -1, "1");
        rootUnsafe_helper(ONE, -2, "1");
        rootUnsafe_helper(ONE, -3, "1");
        rootUnsafe_helper(ONE, -10, "1");

        rootUnsafe_helper(NEGATIVE_ONE, 1, "-1");
        rootUnsafe_helper(NEGATIVE_ONE, 3, "-1");
        rootUnsafe_helper(NEGATIVE_ONE, 9, "-1");
        rootUnsafe_helper(NEGATIVE_ONE, -1, "-1");
        rootUnsafe_helper(NEGATIVE_ONE, -3, "-1");
        rootUnsafe_helper(NEGATIVE_ONE, -9, "-1");

        rootUnsafe_helper(ONE_HALF, 1, "0.5");
        rootUnsafe_helper(ONE_HALF, 2, "0.70710678118654752440...");
        rootUnsafe_helper(ONE_HALF, 3, "0.79370052598409973737...");
        rootUnsafe_helper(ONE_HALF, 10, "0.93303299153680741598...");
        rootUnsafe_helper(ONE_HALF, -1, "2");
        rootUnsafe_helper(ONE_HALF, -2, "1.41421356237309504880...");
        rootUnsafe_helper(ONE_HALF, -3, "1.25992104989487316476...");
        rootUnsafe_helper(ONE_HALF, -10, "1.07177346253629316421...");

        rootUnsafe_helper(NEGATIVE_FOUR_THIRDS, 1, "-1.33333333333333333333...");
        rootUnsafe_helper(NEGATIVE_FOUR_THIRDS, 3, "-1.10064241629820889462...");
        rootUnsafe_helper(NEGATIVE_FOUR_THIRDS, 9, "-1.03248103197612033486...");
        rootUnsafe_helper(NEGATIVE_FOUR_THIRDS, -1, "-0.75");
        rootUnsafe_helper(NEGATIVE_FOUR_THIRDS, -3, "-0.90856029641606982944...");
        rootUnsafe_helper(NEGATIVE_FOUR_THIRDS, -9, "-0.96854079545272307711...");

        rootUnsafe_helper(of(4), 4, "1.41421356237309504880...");
        rootUnsafe_helper(of(Rational.of(1728, 117649)), 12, "0.70347115030070246117...");

        rootUnsafe_helper(SQRT_TWO, 1, "1.41421356237309504880...");
        rootUnsafe_helper(SQRT_TWO, 2, "1.18920711500272106671...");
        rootUnsafe_helper(SQRT_TWO, 3, "1.12246204830937298143...");
        rootUnsafe_helper(SQRT_TWO, 100, "1.00347174850950278700...");
        rootUnsafe_helper(SQRT_TWO, -1, "0.70710678118654752440...");
        rootUnsafe_helper(SQRT_TWO, -2, "0.84089641525371454303...");
        rootUnsafe_helper(SQRT_TWO, -3, "0.89089871814033930474...");
        rootUnsafe_helper(SQRT_TWO, -100, "0.99654026282786783422...");

        rootUnsafe_helper(E, 1, "2.71828182845904523536...");
        rootUnsafe_helper(E, 2, "1.64872127070012814684...");
        rootUnsafe_helper(E, 3, "1.39561242508608952862...");
        rootUnsafe_helper(E, 100, "1.01005016708416805754...");
        rootUnsafe_helper(E, -1, "0.36787944117144232159...");
        rootUnsafe_helper(E, -2, "0.60653065971263342360...");
        rootUnsafe_helper(E, -3, "0.71653131057378925042...");
        rootUnsafe_helper(E, -100, "0.99004983374916805357...");

        rootUnsafe_helper(PI, 1, "3.14159265358979323846...");
        rootUnsafe_helper(PI, 2, "1.77245385090551602729...");
        rootUnsafe_helper(PI, 3, "1.46459188756152326302...");
        rootUnsafe_helper(PI, 100, "1.01151306991144795553...");
        rootUnsafe_helper(PI, -1, "0.31830988618379067153...");
        rootUnsafe_helper(PI, -2, "0.56418958354775628694...");
        rootUnsafe_helper(PI, -3, "0.68278406325529568146...");
        rootUnsafe_helper(PI, -100, "0.98861797217068499434...");

        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), 1, "0.99999999999999999999...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), 2, "0.99999999999999999999...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), 3, "0.99999999999999999999...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), 100, "0.99999999999999999999...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), -1, "1.00000000000000000000...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), -2, "1.00000000000000000000...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), -3, "1.00000000000000000000...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), -100, "1.00000000000000000000...");

        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), 1, "1.00000000000000000000...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), 2, "1.00000000000000000000...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), 3, "1.00000000000000000000...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), 100, "1.00000000000000000000...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), -1, "0.99999999999999999999...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), -2, "0.99999999999999999999...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), -3, "0.99999999999999999999...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), -100, "0.99999999999999999999...");

        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), 1, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), 2, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), 3, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), 100, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), -1, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), -2, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), -3, "~1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.ONE), -100, "~1");

        rootUnsafe_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), 1, "-1.00000000000000000000...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), 3, "-1.00000000000000000000...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), -1, "-0.99999999999999999999...");
        rootUnsafe_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), -3, "-0.99999999999999999999...");

        rootUnsafe_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), 1, "-0.99999999999999999999...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), 3, "-0.99999999999999999999...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), -1, "-1.00000000000000000000...");
        rootUnsafe_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), -3, "-1.00000000000000000000...");

        rootUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), 1, "~-1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), 3, "~-1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), -1, "~-1");
        rootUnsafe_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), -3, "~-1");

        rootUnsafe_fail_helper(ZERO, -1);
        rootUnsafe_fail_helper(ZERO, -2);
        rootUnsafe_fail_helper(ZERO, -3);
        rootUnsafe_fail_helper(ONE, 0);
        rootUnsafe_fail_helper(TWO, 0);
        rootUnsafe_fail_helper(NEGATIVE_ONE, 2);
        rootUnsafe_fail_helper(NEGATIVE_ONE, -2);
        rootUnsafe_fail_helper(SQRT_TWO, 0);
        rootUnsafe_fail_helper(fuzzyRepresentation(Rational.ONE), 0);
        rootUnsafe_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), 2);
    }

    private static void root_helper(@NotNull Real x, int r, @NotNull Rational resolution, @NotNull String output) {
        Optional<Real> oy = x.root(r, resolution);
        if (oy.isPresent()) {
            oy.get().validate();
        }
        aeq(oy, output);
    }

    private static void root_fail_helper(@NotNull Real x, int r, @NotNull Rational resolution) {
        try {
            //noinspection ResultOfMethodCallIgnored
            x.root(r, resolution).toString();
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testRoot() {
        root_helper(ZERO, 1, DEFAULT_RESOLUTION, "Optional[0]");
        root_helper(ZERO, 2, DEFAULT_RESOLUTION, "Optional[0]");
        root_helper(ZERO, 3, DEFAULT_RESOLUTION, "Optional[0]");
        root_helper(ZERO, 10, DEFAULT_RESOLUTION, "Optional[0]");

        root_helper(ONE, 1, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, 2, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, 3, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, 10, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, -1, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, -2, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, -3, DEFAULT_RESOLUTION, "Optional[1]");
        root_helper(ONE, -10, DEFAULT_RESOLUTION, "Optional[1]");

        root_helper(NEGATIVE_ONE, 1, DEFAULT_RESOLUTION, "Optional[-1]");
        root_helper(NEGATIVE_ONE, 3, DEFAULT_RESOLUTION, "Optional[-1]");
        root_helper(NEGATIVE_ONE, 9, DEFAULT_RESOLUTION, "Optional[-1]");
        root_helper(NEGATIVE_ONE, -1, DEFAULT_RESOLUTION, "Optional[-1]");
        root_helper(NEGATIVE_ONE, -3, DEFAULT_RESOLUTION, "Optional[-1]");
        root_helper(NEGATIVE_ONE, -9, DEFAULT_RESOLUTION, "Optional[-1]");

        root_helper(ONE_HALF, 1, DEFAULT_RESOLUTION, "Optional[0.5]");
        root_helper(ONE_HALF, 2, DEFAULT_RESOLUTION, "Optional[0.70710678118654752440...]");
        root_helper(ONE_HALF, 3, DEFAULT_RESOLUTION, "Optional[0.79370052598409973737...]");
        root_helper(ONE_HALF, 10, DEFAULT_RESOLUTION, "Optional[0.93303299153680741598...]");
        root_helper(ONE_HALF, -1, DEFAULT_RESOLUTION, "Optional[2]");
        root_helper(ONE_HALF, -2, DEFAULT_RESOLUTION, "Optional[1.41421356237309504880...]");
        root_helper(ONE_HALF, -3, DEFAULT_RESOLUTION, "Optional[1.25992104989487316476...]");
        root_helper(ONE_HALF, -10, DEFAULT_RESOLUTION, "Optional[1.07177346253629316421...]");

        root_helper(NEGATIVE_FOUR_THIRDS, 1, DEFAULT_RESOLUTION, "Optional[-1.33333333333333333333...]");
        root_helper(NEGATIVE_FOUR_THIRDS, 3, DEFAULT_RESOLUTION, "Optional[-1.10064241629820889462...]");
        root_helper(NEGATIVE_FOUR_THIRDS, 9, DEFAULT_RESOLUTION, "Optional[-1.03248103197612033486...]");
        root_helper(NEGATIVE_FOUR_THIRDS, -1, DEFAULT_RESOLUTION, "Optional[-0.75]");
        root_helper(NEGATIVE_FOUR_THIRDS, -3, DEFAULT_RESOLUTION, "Optional[-0.90856029641606982944...]");
        root_helper(NEGATIVE_FOUR_THIRDS, -9, DEFAULT_RESOLUTION, "Optional[-0.96854079545272307711...]");

        root_helper(of(4), 4, DEFAULT_RESOLUTION, "Optional[1.41421356237309504880...]");
        root_helper(of(Rational.of(1728, 117649)), 12, DEFAULT_RESOLUTION, "Optional[0.70347115030070246117...]");

        root_helper(SQRT_TWO, 1, DEFAULT_RESOLUTION, "Optional[1.41421356237309504880...]");
        root_helper(SQRT_TWO, 2, DEFAULT_RESOLUTION, "Optional[1.18920711500272106671...]");
        root_helper(SQRT_TWO, 3, DEFAULT_RESOLUTION, "Optional[1.12246204830937298143...]");
        root_helper(SQRT_TWO, 100, DEFAULT_RESOLUTION, "Optional[1.00347174850950278700...]");
        root_helper(SQRT_TWO, -1, DEFAULT_RESOLUTION, "Optional[0.70710678118654752440...]");
        root_helper(SQRT_TWO, -2, DEFAULT_RESOLUTION, "Optional[0.84089641525371454303...]");
        root_helper(SQRT_TWO, -3, DEFAULT_RESOLUTION, "Optional[0.89089871814033930474...]");
        root_helper(SQRT_TWO, -100, DEFAULT_RESOLUTION, "Optional[0.99654026282786783422...]");

        root_helper(E, 1, DEFAULT_RESOLUTION, "Optional[2.71828182845904523536...]");
        root_helper(E, 2, DEFAULT_RESOLUTION, "Optional[1.64872127070012814684...]");
        root_helper(E, 3, DEFAULT_RESOLUTION, "Optional[1.39561242508608952862...]");
        root_helper(E, 100, DEFAULT_RESOLUTION, "Optional[1.01005016708416805754...]");
        root_helper(E, -1, DEFAULT_RESOLUTION, "Optional[0.36787944117144232159...]");
        root_helper(E, -2, DEFAULT_RESOLUTION, "Optional[0.60653065971263342360...]");
        root_helper(E, -3, DEFAULT_RESOLUTION, "Optional[0.71653131057378925042...]");
        root_helper(E, -100, DEFAULT_RESOLUTION, "Optional[0.99004983374916805357...]");

        root_helper(PI, 1, DEFAULT_RESOLUTION, "Optional[3.14159265358979323846...]");
        root_helper(PI, 2, DEFAULT_RESOLUTION, "Optional[1.77245385090551602729...]");
        root_helper(PI, 3, DEFAULT_RESOLUTION, "Optional[1.46459188756152326302...]");
        root_helper(PI, 100, DEFAULT_RESOLUTION, "Optional[1.01151306991144795553...]");
        root_helper(PI, -1, DEFAULT_RESOLUTION, "Optional[0.31830988618379067153...]");
        root_helper(PI, -2, DEFAULT_RESOLUTION, "Optional[0.56418958354775628694...]");
        root_helper(PI, -3, DEFAULT_RESOLUTION, "Optional[0.68278406325529568146...]");
        root_helper(PI, -100, DEFAULT_RESOLUTION, "Optional[0.98861797217068499434...]");

        root_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                1,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]"
        );
        root_helper(leftFuzzyRepresentation(Rational.ZERO), 2, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                3,
                DEFAULT_RESOLUTION,
                "Optional[-0.00000000000000000000...]"
        );
        root_helper(leftFuzzyRepresentation(Rational.ZERO), 100, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(leftFuzzyRepresentation(Rational.ZERO), -1, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(leftFuzzyRepresentation(Rational.ZERO), -2, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(leftFuzzyRepresentation(Rational.ZERO), -3, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(leftFuzzyRepresentation(Rational.ZERO), -100, DEFAULT_RESOLUTION, "Optional.empty");

        root_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                1,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                2,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                3,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                10,
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        root_helper(rightFuzzyRepresentation(Rational.ZERO), -1, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(rightFuzzyRepresentation(Rational.ZERO), -2, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(rightFuzzyRepresentation(Rational.ZERO), -3, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(rightFuzzyRepresentation(Rational.ZERO), -100, DEFAULT_RESOLUTION, "Optional.empty");

        root_helper(fuzzyRepresentation(Rational.ZERO), 1, DEFAULT_RESOLUTION, "Optional[~0]");
        root_helper(fuzzyRepresentation(Rational.ZERO), 2, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(fuzzyRepresentation(Rational.ZERO), 3, DEFAULT_RESOLUTION, "Optional[~0]");
        root_helper(fuzzyRepresentation(Rational.ZERO), 100, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(fuzzyRepresentation(Rational.ZERO), -1, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(fuzzyRepresentation(Rational.ZERO), -2, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(fuzzyRepresentation(Rational.ZERO), -3, DEFAULT_RESOLUTION, "Optional.empty");
        root_helper(fuzzyRepresentation(Rational.ZERO), -100, DEFAULT_RESOLUTION, "Optional.empty");

        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                1,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                2,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                3,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                100,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                -1,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                -2,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                -3,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.ONE),
                -100,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );

        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                1,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                2,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                3,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                100,
                DEFAULT_RESOLUTION,
                "Optional[1.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                -1,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                -2,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                -3,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.ONE),
                -100,
                DEFAULT_RESOLUTION,
                "Optional[0.99999999999999999999...]"
        );

        root_helper(fuzzyRepresentation(Rational.ONE), 1, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), 2, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), 3, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), 100, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), -1, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), -2, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), -3, DEFAULT_RESOLUTION, "Optional[~1]");
        root_helper(fuzzyRepresentation(Rational.ONE), -100, DEFAULT_RESOLUTION, "Optional[~1]");

        root_helper(
                leftFuzzyRepresentation(Rational.NEGATIVE_ONE),
                1,
                DEFAULT_RESOLUTION,
                "Optional[-1.00000000000000000000...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.NEGATIVE_ONE),
                3,
                DEFAULT_RESOLUTION,
                "Optional[-1.00000000000000000000...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.NEGATIVE_ONE),
                -1,
                DEFAULT_RESOLUTION,
                "Optional[-0.99999999999999999999...]"
        );
        root_helper(
                leftFuzzyRepresentation(Rational.NEGATIVE_ONE),
                -3,
                DEFAULT_RESOLUTION,
                "Optional[-0.99999999999999999999...]"
        );

        root_helper(
                rightFuzzyRepresentation(Rational.NEGATIVE_ONE),
                1,
                DEFAULT_RESOLUTION,
                "Optional[-0.99999999999999999999...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.NEGATIVE_ONE),
                3,
                DEFAULT_RESOLUTION,
                "Optional[-0.99999999999999999999...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.NEGATIVE_ONE),
                -1,
                DEFAULT_RESOLUTION,
                "Optional[-1.00000000000000000000...]"
        );
        root_helper(
                rightFuzzyRepresentation(Rational.NEGATIVE_ONE),
                -3,
                DEFAULT_RESOLUTION,
                "Optional[-1.00000000000000000000...]"
        );

        root_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), 1, DEFAULT_RESOLUTION, "Optional[~-1]");
        root_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), 3, DEFAULT_RESOLUTION, "Optional[~-1]");
        root_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), -1, DEFAULT_RESOLUTION, "Optional[~-1]");
        root_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), -3, DEFAULT_RESOLUTION, "Optional[~-1]");

        root_fail_helper(ZERO, -1, DEFAULT_RESOLUTION);
        root_fail_helper(ZERO, -2, DEFAULT_RESOLUTION);
        root_fail_helper(ZERO, -3, DEFAULT_RESOLUTION);
        root_fail_helper(ONE, 0, DEFAULT_RESOLUTION);
        root_fail_helper(TWO, 0, DEFAULT_RESOLUTION);
        root_fail_helper(NEGATIVE_ONE, 2, DEFAULT_RESOLUTION);
        root_fail_helper(NEGATIVE_ONE, -2, DEFAULT_RESOLUTION);
        root_fail_helper(SQRT_TWO, 0, DEFAULT_RESOLUTION);
        root_fail_helper(fuzzyRepresentation(Rational.ONE), 0, DEFAULT_RESOLUTION);
        root_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), 2, DEFAULT_RESOLUTION);

        root_fail_helper(ZERO, 1, Rational.ZERO);
        root_fail_helper(ZERO, 1, Rational.NEGATIVE_ONE);
        root_fail_helper(SQRT_TWO, 1, Rational.ZERO);
        root_fail_helper(SQRT_TWO, 1, Rational.NEGATIVE_ONE);
    }

    private static void sqrtUnsafe_helper(@NotNull Real x, @NotNull String output) {
        Real y = x.sqrtUnsafe();
        y.validate();
        aeq(y, output);
    }

    private static void sqrtUnsafe_fail_helper(@NotNull Real x) {
        try {
            //noinspection ResultOfMethodCallIgnored
            x.sqrtUnsafe().toString();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSqrtUnsafe() {
        sqrtUnsafe_helper(ZERO, "0");
        sqrtUnsafe_helper(ONE, "1");
        sqrtUnsafe_helper(ONE_HALF, "0.70710678118654752440...");
        sqrtUnsafe_helper(SQRT_TWO, "1.18920711500272106671...");
        sqrtUnsafe_helper(E, "1.64872127070012814684...");
        sqrtUnsafe_helper(PI, "1.77245385090551602729...");
        sqrtUnsafe_helper(leftFuzzyRepresentation(Rational.ONE), "0.99999999999999999999...");
        sqrtUnsafe_helper(rightFuzzyRepresentation(Rational.ONE), "1.00000000000000000000...");
        sqrtUnsafe_helper(fuzzyRepresentation(Rational.ONE), "~1");

        sqrtUnsafe_fail_helper(NEGATIVE_ONE);
        sqrtUnsafe_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE));
    }

    private static void sqrt_helper(@NotNull Real x, @NotNull Rational resolution, @NotNull String output) {
        Optional<Real> oy = x.sqrt(resolution);
        if (oy.isPresent()) {
            oy.get().validate();
        }
        aeq(oy, output);
    }

    private static void sqrt_fail_helper(@NotNull Real x, @NotNull Rational resolution) {
        try {
            //noinspection ResultOfMethodCallIgnored
            x.sqrt(resolution).toString();
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSqrt() {
        sqrt_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0]");
        sqrt_helper(ONE, DEFAULT_RESOLUTION, "Optional[1]");
        sqrt_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[0.70710678118654752440...]");
        sqrt_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[1.18920711500272106671...]");
        sqrt_helper(E, DEFAULT_RESOLUTION, "Optional[1.64872127070012814684...]");
        sqrt_helper(PI, DEFAULT_RESOLUTION, "Optional[1.77245385090551602729...]");
        sqrt_helper(leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        sqrt_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]"
        );
        sqrt_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        sqrt_helper(leftFuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[0.99999999999999999999...]");
        sqrt_helper(rightFuzzyRepresentation(Rational.ONE), DEFAULT_RESOLUTION, "Optional[1.00000000000000000000...]");
        root_helper(fuzzyRepresentation(Rational.ONE), 2, DEFAULT_RESOLUTION, "Optional[~1]");

        sqrt_fail_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION);
        sqrt_fail_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), DEFAULT_RESOLUTION);

        sqrt_fail_helper(ZERO, Rational.ZERO);
        sqrt_fail_helper(ZERO, Rational.NEGATIVE_ONE);
        sqrt_fail_helper(SQRT_TWO, Rational.ZERO);
        sqrt_fail_helper(SQRT_TWO, Rational.NEGATIVE_ONE);
    }

    private static void cbrt_helper(@NotNull Real x, @NotNull String output) {
        Real y = x.cbrt();
        y.validate();
        aeq(y, output);
    }

    @Test
    public void testCbrt() {
        cbrt_helper(ZERO, "0");
        cbrt_helper(ONE, "1");
        cbrt_helper(NEGATIVE_ONE, "-1");
        cbrt_helper(ONE_HALF, "0.79370052598409973737...");
        cbrt_helper(NEGATIVE_FOUR_THIRDS, "-1.10064241629820889462...");
        cbrt_helper(SQRT_TWO, "1.12246204830937298143...");
        cbrt_helper(E, "1.39561242508608952862...");
        cbrt_helper(PI, "1.46459188756152326302...");
        cbrt_helper(leftFuzzyRepresentation(Rational.ONE), "0.99999999999999999999...");
        cbrt_helper(rightFuzzyRepresentation(Rational.ONE), "1.00000000000000000000...");
        cbrt_helper(fuzzyRepresentation(Rational.ONE), "~1");
        cbrt_helper(leftFuzzyRepresentation(Rational.NEGATIVE_ONE), "-1.00000000000000000000...");
        cbrt_helper(rightFuzzyRepresentation(Rational.NEGATIVE_ONE), "-0.99999999999999999999...");
        cbrt_helper(fuzzyRepresentation(Rational.NEGATIVE_ONE), "~-1");
    }

    private static void intervalExtensionUnsafe_helper(@NotNull Real a, @NotNull Real b, @NotNull String output) {
        aeq(intervalExtensionUnsafe(a, b), output);
    }

    private static void intervalExtensionUnsafe_fail_helper(@NotNull Real a, @NotNull Real b) {
        try {
            intervalExtensionUnsafe(a, b);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIntervalExtensionUnsafe() {
        intervalExtensionUnsafe_helper(PI.negate(), NEGATIVE_FOUR_THIRDS, "[-651864872/204778785, -4/3]");
        intervalExtensionUnsafe_helper(PI.negate(), ZERO, "[-651864872/204778785, 0]");
        intervalExtensionUnsafe_helper(PI.negate(), ONE_HALF, "[-651864872/204778785, 1/2]");
        intervalExtensionUnsafe_helper(PI.negate(), ONE, "[-651864872/204778785, 1]");
        intervalExtensionUnsafe_helper(PI.negate(), E, "[-651864872/204778785, 4]");
        intervalExtensionUnsafe_helper(PI.negate(), PI, "[-651864872/204778785, 651864872/204778785]");

        intervalExtensionUnsafe_helper(NEGATIVE_FOUR_THIRDS, ZERO, "[-4/3, 0]");
        intervalExtensionUnsafe_helper(NEGATIVE_FOUR_THIRDS, ONE_HALF, "[-4/3, 1/2]");
        intervalExtensionUnsafe_helper(NEGATIVE_FOUR_THIRDS, ONE, "[-4/3, 1]");
        intervalExtensionUnsafe_helper(NEGATIVE_FOUR_THIRDS, E, "[-4/3, 4]");
        intervalExtensionUnsafe_helper(NEGATIVE_FOUR_THIRDS, PI, "[-4/3, 651864872/204778785]");

        intervalExtensionUnsafe_helper(ZERO, ONE_HALF, "[0, 1/2]");
        intervalExtensionUnsafe_helper(ZERO, ONE, "[0, 1]");
        intervalExtensionUnsafe_helper(ZERO, E, "[0, 4]");
        intervalExtensionUnsafe_helper(ZERO, PI, "[0, 651864872/204778785]");

        intervalExtensionUnsafe_helper(ONE_HALF, ONE, "[1/2, 1]");
        intervalExtensionUnsafe_helper(ONE_HALF, E, "[1/2, 7/2]");
        intervalExtensionUnsafe_helper(ONE_HALF, PI, "[1/2, 651864872/204778785]");

        intervalExtensionUnsafe_helper(ONE, E, "[1, 7/2]");
        intervalExtensionUnsafe_helper(ONE, PI, "[1, 651864872/204778785]");

        intervalExtensionUnsafe_helper(E, PI, "[65/24, 5277328977275528/1679825970703125]");

        intervalExtensionUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), ONE_HALF, "[-1/2, 1/2]");
        intervalExtensionUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, "[-1, 1]");
        intervalExtensionUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), E, "[-1, 7/2]");
        intervalExtensionUnsafe_helper(leftFuzzyRepresentation(Rational.ZERO), PI, "[-1, 651864872/204778785]");

        intervalExtensionUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), ONE_HALF, "[0, 1/2]");
        intervalExtensionUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, "[0, 1]");
        intervalExtensionUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), E, "[0, 7/2]");
        intervalExtensionUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), PI, "[0, 651864872/204778785]");

        intervalExtensionUnsafe_helper(fuzzyRepresentation(Rational.ZERO), ONE_HALF, "[-1/8, 1/2]");
        intervalExtensionUnsafe_helper(fuzzyRepresentation(Rational.ZERO), ONE, "[-1/4, 1]");
        intervalExtensionUnsafe_helper(fuzzyRepresentation(Rational.ZERO), E, "[-1/2, 7/2]");
        intervalExtensionUnsafe_helper(fuzzyRepresentation(Rational.ZERO), PI, "[-1, 651864872/204778785]");

        intervalExtensionUnsafe_fail_helper(ZERO, ZERO);
        intervalExtensionUnsafe_fail_helper(PI, PI);
    }

    private static void fractionalPartUnsafe_helper(@NotNull Real input, @NotNull String output) {
        Real x = input.fractionalPartUnsafe();
        x.validate();
        aeq(x, output);
    }

    @Test
    public void testFractionalPartUnsafe() {
        fractionalPartUnsafe_helper(ZERO, "0");
        fractionalPartUnsafe_helper(ONE, "0");
        fractionalPartUnsafe_helper(NEGATIVE_ONE, "0");
        fractionalPartUnsafe_helper(ONE_HALF, "0.5");
        fractionalPartUnsafe_helper(NEGATIVE_FOUR_THIRDS, "0.66666666666666666666...");
        fractionalPartUnsafe_helper(SQRT_TWO, "0.41421356237309504880...");
        fractionalPartUnsafe_helper(E, "0.71828182845904523536...");
        fractionalPartUnsafe_helper(PI, "0.14159265358979323846...");
        fractionalPartUnsafe_helper(rightFuzzyRepresentation(Rational.ZERO), "0.00000000000000000000...");
    }

    private static void fractionalPart_helper(
            @NotNull Real input,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        Optional<Real> ox = input.fractionalPart(resolution);
        if (ox.isPresent()) {
            ox.get().validate();
        }
        aeq(ox, output);
    }

    private static void fractionalPart_fail_helper(@NotNull Real input, @NotNull Rational resolution) {
        try {
            input.fractionalPart(resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFractionalPart() {
        fractionalPart_helper(ZERO, DEFAULT_RESOLUTION, "Optional[0]");
        fractionalPart_helper(ONE, DEFAULT_RESOLUTION, "Optional[0]");
        fractionalPart_helper(NEGATIVE_ONE, DEFAULT_RESOLUTION, "Optional[0]");
        fractionalPart_helper(ONE_HALF, DEFAULT_RESOLUTION, "Optional[0.5]");
        fractionalPart_helper(NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[0.66666666666666666666...]");
        fractionalPart_helper(SQRT_TWO, DEFAULT_RESOLUTION, "Optional[0.41421356237309504880...]");
        fractionalPart_helper(E, DEFAULT_RESOLUTION, "Optional[0.71828182845904523536...]");
        fractionalPart_helper(PI, DEFAULT_RESOLUTION, "Optional[0.14159265358979323846...]");
        fractionalPart_helper(leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        fractionalPart_helper(rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION,
                "Optional[0.00000000000000000000...]");
        fractionalPart_helper(fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        fractionalPart_fail_helper(ZERO, Rational.ZERO);
        fractionalPart_fail_helper(ZERO, Rational.NEGATIVE_ONE);
        fractionalPart_fail_helper(PI, Rational.ZERO);
        fractionalPart_fail_helper(PI, Rational.NEGATIVE_ONE);
    }

    private static void roundToDenominatorUnsafe_helper(
            @NotNull Real x,
            @NotNull String denominator,
            @NotNull String roundingMode,
            @NotNull String output
    ) {
        aeq(
                x.roundToDenominatorUnsafe(
                        Readers.readBigIntegerStrict(denominator).get(),
                        Readers.readRoundingModeStrict(roundingMode).get()
                ),
                output
        );
    }

    private static void roundToDenominatorUnsafe_fail_helper(
            @NotNull Real x,
            @NotNull String denominator,
            @NotNull String roundingMode
    ) {
        try {
            x.roundToDenominatorUnsafe(
                    Readers.readBigIntegerStrict(denominator).get(),
                    Readers.readRoundingModeStrict(roundingMode).get()
            );
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundToDenominatorUnsafe() {
        roundToDenominatorUnsafe_helper(PI, "1", "HALF_EVEN", "3");
        roundToDenominatorUnsafe_helper(PI, "2", "HALF_EVEN", "3");
        roundToDenominatorUnsafe_helper(PI, "3", "HALF_EVEN", "3");
        roundToDenominatorUnsafe_helper(PI, "4", "HALF_EVEN", "13/4");
        roundToDenominatorUnsafe_helper(PI, "5", "HALF_EVEN", "16/5");
        roundToDenominatorUnsafe_helper(PI, "6", "HALF_EVEN", "19/6");
        roundToDenominatorUnsafe_helper(PI, "7", "HALF_EVEN", "22/7");
        roundToDenominatorUnsafe_helper(PI, "8", "HALF_EVEN", "25/8");
        roundToDenominatorUnsafe_helper(PI, "9", "HALF_EVEN", "28/9");
        roundToDenominatorUnsafe_helper(PI, "10", "HALF_EVEN", "31/10");
        roundToDenominatorUnsafe_helper(PI, "100", "HALF_EVEN", "157/50");
        roundToDenominatorUnsafe_helper(PI, "1000", "HALF_EVEN", "1571/500");
        roundToDenominatorUnsafe_helper(NEGATIVE_FOUR_THIRDS, "30", "UNNECESSARY", "-4/3");

        roundToDenominatorUnsafe_fail_helper(PI, "0", "HALF_EVEN");
        roundToDenominatorUnsafe_fail_helper(PI, "-1", "HALF_EVEN");
        roundToDenominatorUnsafe_fail_helper(PI, "7", "UNNECESSARY");
    }

    private static void roundToDenominator_helper(
            @NotNull Real x,
            @NotNull String denominator,
            @NotNull String roundingMode,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(
                x.roundToDenominator(
                        Readers.readBigIntegerStrict(denominator).get(),
                        Readers.readRoundingModeStrict(roundingMode).get(),
                        resolution
                ),
                output
        );
    }

    private static void roundToDenominator_fail_helper(
            @NotNull Real x,
            @NotNull String denominator,
            @NotNull String roundingMode,
            @NotNull Rational resolution
    ) {
        try {
            x.roundToDenominator(
                    Readers.readBigIntegerStrict(denominator).get(),
                    Readers.readRoundingModeStrict(roundingMode).get(),
                    resolution
            );
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testRoundToDenominator() {
        roundToDenominator_helper(PI, "1", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3]");
        roundToDenominator_helper(PI, "2", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3]");
        roundToDenominator_helper(PI, "3", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[3]");
        roundToDenominator_helper(PI, "4", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[13/4]");
        roundToDenominator_helper(PI, "5", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[16/5]");
        roundToDenominator_helper(PI, "6", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[19/6]");
        roundToDenominator_helper(PI, "7", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[22/7]");
        roundToDenominator_helper(PI, "8", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[25/8]");
        roundToDenominator_helper(PI, "9", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[28/9]");
        roundToDenominator_helper(PI, "10", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[31/10]");
        roundToDenominator_helper(PI, "100", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[157/50]");
        roundToDenominator_helper(PI, "1000", "HALF_EVEN", DEFAULT_RESOLUTION, "Optional[1571/500]");
        roundToDenominator_helper(NEGATIVE_FOUR_THIRDS, "30", "UNNECESSARY", DEFAULT_RESOLUTION, "Optional[-4/3]");

        roundToDenominator_fail_helper(PI, "0", "HALF_EVEN", DEFAULT_RESOLUTION);
        roundToDenominator_fail_helper(PI, "-1", "HALF_EVEN", DEFAULT_RESOLUTION);
        roundToDenominator_fail_helper(PI, "7", "UNNECESSARY", DEFAULT_RESOLUTION);
        roundToDenominator_fail_helper(PI, "1", "HALF_EVEN", Rational.ZERO);
        roundToDenominator_fail_helper(PI, "1", "HALF_EVEN", Rational.NEGATIVE_ONE);
    }

    private static void continuedFractionUnsafe_helper(@NotNull Real input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, input.continuedFractionUnsafe(), output);
    }

    @Test
    public void testContinuedFractionUnsafe() {
        continuedFractionUnsafe_helper(ZERO, "[0]");
        continuedFractionUnsafe_helper(ONE, "[1]");
        continuedFractionUnsafe_helper(ONE_HALF, "[0, 2]");
        continuedFractionUnsafe_helper(NEGATIVE_FOUR_THIRDS, "[-2, 1, 2]");
        continuedFractionUnsafe_helper(SQRT_TWO,
                "[1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, ...]");
        continuedFractionUnsafe_helper(SQRT_TWO.negate(),
                "[-2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, ...]");
        continuedFractionUnsafe_helper(E, "[2, 1, 2, 1, 1, 4, 1, 1, 6, 1, 1, 8, 1, 1, 10, 1, 1, 12, 1, 1, ...]");
        continuedFractionUnsafe_helper(PI, "[3, 7, 15, 1, 292, 1, 1, 1, 2, 1, 3, 1, 14, 2, 1, 1, 2, 2, 2, 2, ...]");
        continuedFractionUnsafe_helper(PRIME_CONSTANT,
                "[0, 2, 2, 2, 3, 12, 131, 1, 7, 1, 2, 1, 3, 3, 1, 2, 5, 39, 2, 1, ...]");
        continuedFractionUnsafe_helper(THUE_MORSE,
                "[0, 2, 2, 2, 1, 4, 3, 5, 2, 1, 4, 2, 1, 5, 44, 1, 4, 1, 2, 4, ...]");
        continuedFractionUnsafe_helper(CONTINUED_FRACTION_CONSTANT,
                "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, ...]");
        aeqitLimit(10, CAHEN.continuedFractionUnsafe(),
                "[0, 1, 1, 1, 4, 9, 196, 16641, 639988804, 177227652025317609, ...]");

        continuedFractionUnsafe_helper(champernowne(BigInteger.TEN),
                "[0, 8, 9, 1, 149083, 1, 1, 1, 4, 1, 1, 1, 3, 4, 1, 1, 1, 15," +
                " 45754011139103107648364662824295611859960393971045755500066200439309026265925631493795320774712865" +
                "63138641209375503552094607183089984575801469863148833592141783010987, 6, ...]");
        continuedFractionUnsafe_helper(copelandErdos(BigInteger.TEN),
                "[0, 4, 4, 8, 16, 18, 5, 1, 1, 1, 1, 7, 1, 1, 6, 2, 9, 58, 1, 3, ...]");
    }

    private static void fromContinuedFraction_helper(@NotNull Iterable<BigInteger> input, @NotNull String output) {
        Real x = fromContinuedFraction(input);
        x.validate();
        aeq(x, output);
    }

    private static void fromContinuedFraction_helper(@NotNull String input, @NotNull String output) {
        fromContinuedFraction_helper(readBigIntegerList(input), output);
    }

    private static void fromContinuedFraction_fail_helper(@NotNull String input) {
        try {
            toList(fromContinuedFraction(readBigIntegerListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testFromContinuedFraction() {
        fromContinuedFraction_helper("[0]", "0");
        fromContinuedFraction_helper("[1]", "1");
        fromContinuedFraction_helper("[0, 2]", "0.5");
        fromContinuedFraction_helper("[-2, 1, 2]", "-1.33333333333333333333...");
        fromContinuedFraction_helper(SQRT_TWO.continuedFractionUnsafe(), "1.41421356237309504880...");
        fromContinuedFraction_helper(SQRT_TWO.negate().continuedFractionUnsafe(), "-1.41421356237309504880...");
        fromContinuedFraction_helper(PI.continuedFractionUnsafe(), "3.14159265358979323846...");

        fromContinuedFraction_fail_helper("[]");
        fromContinuedFraction_fail_helper("[1, -2]");
        fromContinuedFraction_fail_helper("[null]");
        fromContinuedFraction_fail_helper("[1, null]");
    }

    private static void convergentsUnsafe_helper(@NotNull Real input, @NotNull String output) {
        aeqitLimit(TINY_LIMIT, input.convergentsUnsafe(), output);
    }

    @Test
    public void testConvergentsUnsafe() {
        convergentsUnsafe_helper(ZERO, "[0]");
        convergentsUnsafe_helper(ONE, "[1]");
        convergentsUnsafe_helper(ONE_HALF, "[0, 1/2]");
        convergentsUnsafe_helper(NEGATIVE_FOUR_THIRDS, "[-2, -1, -4/3]");
        convergentsUnsafe_helper(SQRT_TWO,
                "[1, 3/2, 7/5, 17/12, 41/29, 99/70, 239/169, 577/408, 1393/985, 3363/2378, 8119/5741, 19601/13860," +
                " 47321/33461, 114243/80782, 275807/195025, 665857/470832, 1607521/1136689, 3880899/2744210," +
                " 9369319/6625109, 22619537/15994428, ...]");
        convergentsUnsafe_helper(SQRT_TWO.negate(),
                "[-2, -1, -3/2, -7/5, -17/12, -41/29, -99/70, -239/169, -577/408, -1393/985, -3363/2378, -8119/5741," +
                " -19601/13860, -47321/33461, -114243/80782, -275807/195025, -665857/470832, -1607521/1136689," +
                " -3880899/2744210, -9369319/6625109, ...]");
        convergentsUnsafe_helper(E,
                "[2, 3, 8/3, 11/4, 19/7, 87/32, 106/39, 193/71, 1264/465, 1457/536, 2721/1001, 23225/8544," +
                " 25946/9545, 49171/18089, 517656/190435, 566827/208524, 1084483/398959, 13580623/4996032," +
                " 14665106/5394991, 28245729/10391023, ...]");
        convergentsUnsafe_helper(PI,
                "[3, 22/7, 333/106, 355/113, 103993/33102, 104348/33215, 208341/66317, 312689/99532, 833719/265381," +
                " 1146408/364913, 4272943/1360120, 5419351/1725033, 80143857/25510582, 165707065/52746197," +
                " 245850922/78256779, 411557987/131002976, 1068966896/340262731, 2549491779/811528438," +
                " 6167950454/1963319607, 14885392687/4738167652, ...]");
        convergentsUnsafe_helper(PRIME_CONSTANT,
                "[0, 1/2, 2/5, 5/12, 17/41, 209/504, 27396/66065, 27605/66569, 220631/532048, 248236/598617," +
                " 717103/1729282, 965339/2327899, 3613120/8712979, 11804699/28466836, 15417819/37179815," +
                " 42640337/102826466, 228619504/551312145, 8958800993/21604000121, 18146221490/43759312387," +
                " 27105022483/65363312508, ...]");
        convergentsUnsafe_helper(THUE_MORSE,
                "[0, 1/2, 2/5, 5/12, 7/17, 33/80, 106/257, 563/1365, 1232/2987, 1795/4352, 8412/20395, 18619/45142," +
                " 27031/65537, 153774/372827, 6793087/16469925, 6946861/16842752, 34580531/83840933," +
                " 41527392/100683685, 117635315/285208303, 512068652/1241516897, ...]");
        convergentsUnsafe_helper(CONTINUED_FRACTION_CONSTANT,
                "[0, 1, 2/3, 7/10, 30/43, 157/225, 972/1393, 6961/9976, 56660/81201, 516901/740785, 5225670/7489051," +
                " 57999271/83120346, 701216922/1004933203, 9173819257/13147251985, 129134686520/185066460993," +
                " 1946194117057/2789144166880, 31268240559432/44811373131073, 533506283627401/764582487395121," +
                " 9634381345852650/13807296146243251, 183586751854827751/263103209266016890, ...]");
        aeqitLimit(10, CAHEN.convergentsUnsafe(),
                "[0, 1, 1/2, 2/3, 9/14, 83/129, 16277/25298, 270865640/420984147," +
                " 173350976988310837/269425140741515486," +
                " 30722586627933193342822775612494373/47749585090209528873482531562977121, ...]");

        convergentsUnsafe_helper(champernowne(BigInteger.TEN),
                "[0, 1/8, 9/73, 10/81, 1490839/12075796, 1490849/12075877, 2981688/24151673, 4472537/36227550," +
                " 20871836/169061873, 25344373/205289423, 46216209/374351296, 71560582/579640719," +
                " 260897955/2113273453, 1115152402/9032734531, 1376050357/11146007984, 2491202759/20178742515," +
                " 3867253116/31324750499, 60499999499/490050000000," +
                " 27681176509929784320354051690391884425555022934081108282600816210450296689946785461458755284786726" +
                "6256773225034758719004596454977545868107906654243968030887012242501684078748629/2242175315871747790" +
                "308110301704606459197359106551097248280744152528338832161685571353439694564803980266091124654465515" +
                "703962250073246941371510306436085901829080764534210674750499," +
                " 16608705905957870592212431014235130655333013760448664969560489726270178013968071276875253170872035" +
                "97540639350208552314027578729865275208647439925463808185322073455010164972491273/134530518952304867" +
                "418486618102276387551841546393065834896844649151700329929701134281206381673888238815965467479267930" +
                "94223773500439481648229061838616515410974484587205754098502994, ...]");
        convergentsUnsafe_helper(copelandErdos(BigInteger.TEN),
                "[0, 1/4, 4/17, 33/140, 532/2257, 9609/40766, 48577/206087, 58186/246853, 106763/452940," +
                " 164949/699793, 271712/1152733, 2066933/8768924, 2338645/9921657, 4405578/18690581," +
                " 28772113/122065143, 61949804/262820867, 586320349/2487452946, 34068530046/144535091735," +
                " 34654850395/147022544681, 138033081231/585602725778, ...]");
    }

    private static void digitsUnsafe_helper(@NotNull Real x, @NotNull String base, @NotNull String output) {
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = x.digitsUnsafe(Readers.readBigIntegerStrict(base).get());
        aeq(new Pair<>(digits.a.toString(), itsList(TINY_LIMIT, digits.b)), output);
    }

    private static void digitsUnsafe_fail_helper(@NotNull Real x, @NotNull String base) {
        try {
            x.digitsUnsafe(Readers.readBigIntegerStrict(base).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDigitsUnsafe() {
        digitsUnsafe_helper(ZERO, "2", "([], [])");
        digitsUnsafe_helper(ZERO, "3", "([], [])");
        digitsUnsafe_helper(ZERO, "4", "([], [])");
        digitsUnsafe_helper(ZERO, "10", "([], [])");
        digitsUnsafe_helper(ZERO, "16", "([], [])");
        digitsUnsafe_helper(ZERO, "83", "([], [])");
        digitsUnsafe_helper(ZERO, "100", "([], [])");

        digitsUnsafe_helper(ONE, "2", "([1], [])");
        digitsUnsafe_helper(ONE, "3", "([1], [])");
        digitsUnsafe_helper(ONE, "4", "([1], [])");
        digitsUnsafe_helper(ONE, "10", "([1], [])");
        digitsUnsafe_helper(ONE, "16", "([1], [])");
        digitsUnsafe_helper(ONE, "83", "([1], [])");
        digitsUnsafe_helper(ONE, "100", "([1], [])");

        digitsUnsafe_helper(ONE_HALF, "2", "([], [1])");
        digitsUnsafe_helper(ONE_HALF, "3", "([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])");
        digitsUnsafe_helper(ONE_HALF, "4", "([], [2])");
        digitsUnsafe_helper(ONE_HALF, "10", "([], [5])");
        digitsUnsafe_helper(ONE_HALF, "16", "([], [8])");
        digitsUnsafe_helper(
                ONE_HALF,
                "83",
                "([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])"
        );
        digitsUnsafe_helper(ONE_HALF, "100", "([], [50])");

        digitsUnsafe_helper(PI, "2", "([1, 1], [0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, ...])");
        digitsUnsafe_helper(PI, "3", "([1, 0], [0, 1, 0, 2, 1, 1, 0, 1, 2, 2, 2, 2, 0, 1, 0, 2, 1, 1, 0, 0, ...])");
        digitsUnsafe_helper(PI, "4", "([3], [0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, ...])");
        digitsUnsafe_helper(PI, "10", "([3], [1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, ...])");
        digitsUnsafe_helper(PI, "16", "([3], [2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3, 0, 8, 13, 3, 1, 3, 1, 9, ...])");
        digitsUnsafe_helper(
                PI,
                "83",
                "([3], [11, 62, 35, 69, 50, 19, 79, 18, 33, 82, 74, 23, 59, 17, 3, 18, 29, 47, 35, 11, ...])"
        );
        digitsUnsafe_helper(
                PI,
                "100",
                "([3], [14, 15, 92, 65, 35, 89, 79, 32, 38, 46, 26, 43, 38, 32, 79, 50, 28, 84, 19, 71, ...])"
        );

        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "2",
                "([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "3",
                "([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "10",
                "([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "16",
                "([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "83",
                "([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100",
                "([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );

        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "2",
                "([], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "3",
                "([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "4",
                "([], [2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "10",
                "([], [5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "16",
                "([], [8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "83",
                "([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])"
        );
        digitsUnsafe_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "100",
                "([], [50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])"
        );

        digitsUnsafe_helper(
                leftFuzzyRepresentation(Rational.ONE_HALF),
                "3",
                "([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])"
        );
        digitsUnsafe_helper(
                leftFuzzyRepresentation(Rational.ONE_HALF),
                "83",
                "([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])"
        );

        digitsUnsafe_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "3",
                "([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])"
        );
        digitsUnsafe_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "83",
                "([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])"
        );

        digitsUnsafe_fail_helper(NEGATIVE_ONE, "2");
        digitsUnsafe_fail_helper(NEGATIVE_ONE, "10");
        digitsUnsafe_fail_helper(PI.negate(), "2");
        digitsUnsafe_fail_helper(PI.negate(), "10");
        digitsUnsafe_fail_helper(ZERO, "1");
        digitsUnsafe_fail_helper(ZERO, "0");
        digitsUnsafe_fail_helper(ZERO, "-1");
        digitsUnsafe_fail_helper(PI, "1");
        digitsUnsafe_fail_helper(PI, "0");
        digitsUnsafe_fail_helper(PI, "-1");
    }

    private static void digits_helper(
            @NotNull Real x,
            @NotNull String base,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(
                x.digits(
                        Readers.readBigIntegerStrict(base).get(), resolution)
                                .map(p -> new Pair<>(p.a, itsList(TINY_LIMIT, p.b))
                ),
                output
        );
    }

    private static void digits_fail_helper(@NotNull Real x, @NotNull String base, @NotNull Rational resolution) {
        try {
            x.digits(Readers.readBigIntegerStrict(base).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testDigits() {
        digits_helper(ZERO, "2", DEFAULT_RESOLUTION, "Optional[([], [])]");
        digits_helper(ZERO, "3", DEFAULT_RESOLUTION, "Optional[([], [])]");
        digits_helper(ZERO, "4", DEFAULT_RESOLUTION, "Optional[([], [])]");
        digits_helper(ZERO, "10", DEFAULT_RESOLUTION, "Optional[([], [])]");
        digits_helper(ZERO, "16", DEFAULT_RESOLUTION, "Optional[([], [])]");
        digits_helper(ZERO, "83", DEFAULT_RESOLUTION, "Optional[([], [])]");
        digits_helper(ZERO, "100", DEFAULT_RESOLUTION, "Optional[([], [])]");

        digits_helper(ONE, "2", DEFAULT_RESOLUTION, "Optional[([1], [])]");
        digits_helper(ONE, "3", DEFAULT_RESOLUTION, "Optional[([1], [])]");
        digits_helper(ONE, "4", DEFAULT_RESOLUTION, "Optional[([1], [])]");
        digits_helper(ONE, "10", DEFAULT_RESOLUTION, "Optional[([1], [])]");
        digits_helper(ONE, "16", DEFAULT_RESOLUTION, "Optional[([1], [])]");
        digits_helper(ONE, "83", DEFAULT_RESOLUTION, "Optional[([1], [])]");
        digits_helper(ONE, "100", DEFAULT_RESOLUTION, "Optional[([1], [])]");

        digits_helper(ONE_HALF, "2", DEFAULT_RESOLUTION ,"Optional[([], [1])]");
        digits_helper(
                ONE_HALF,
                "3",
                DEFAULT_RESOLUTION,
                "Optional[([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])]"
        );
        digits_helper(ONE_HALF, "4", DEFAULT_RESOLUTION ,"Optional[([], [2])]");
        digits_helper(ONE_HALF, "10", DEFAULT_RESOLUTION, "Optional[([], [5])]");
        digits_helper(ONE_HALF, "16", DEFAULT_RESOLUTION, "Optional[([], [8])]");
        digits_helper(
                ONE_HALF,
                "83",
                DEFAULT_RESOLUTION,
                "Optional[([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])]"
        );
        digits_helper(ONE_HALF, "100", DEFAULT_RESOLUTION, "Optional[([], [50])]");

        digits_helper(
                PI,
                "2",
                DEFAULT_RESOLUTION,
                "Optional[([1, 1], [0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, ...])]"
        );
        digits_helper(
                PI,
                "3",
                DEFAULT_RESOLUTION,
                "Optional[([1, 0], [0, 1, 0, 2, 1, 1, 0, 1, 2, 2, 2, 2, 0, 1, 0, 2, 1, 1, 0, 0, ...])]"
        );
        digits_helper(
                PI,
                "4",
                DEFAULT_RESOLUTION,
                "Optional[([3], [0, 2, 1, 0, 0, 3, 3, 3, 1, 2, 2, 2, 2, 0, 2, 0, 2, 0, 1, 1, ...])]"
        );
        digits_helper(
                PI,
                "10",
                DEFAULT_RESOLUTION,
                "Optional[([3], [1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, ...])]"
        );
        digits_helper(
                PI,
                "16",
                DEFAULT_RESOLUTION,
                "Optional[([3], [2, 4, 3, 15, 6, 10, 8, 8, 8, 5, 10, 3, 0, 8, 13, 3, 1, 3, 1, 9, ...])]"
        );
        digits_helper(
                PI,
                "83",
                DEFAULT_RESOLUTION,
                "Optional[([3], [11, 62, 35, 69, 50, 19, 79, 18, 33, 82, 74, 23, 59, 17, 3, 18, 29, 47, 35, 11, ...])]"
        );
        digits_helper(
                PI,
                "100",
                DEFAULT_RESOLUTION,
                "Optional[([3]," +
                " [14, 15, 92, 65, 35, 89, 79, 32, 38, 46, 26, 43, 38, 32, 79, 50, 28, 84, 19, 71, ...])]"
        );

        digits_helper(leftFuzzyRepresentation(Rational.ZERO), "2", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(leftFuzzyRepresentation(Rational.ZERO), "3", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(leftFuzzyRepresentation(Rational.ZERO), "10", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(leftFuzzyRepresentation(Rational.ZERO), "16", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(leftFuzzyRepresentation(Rational.ZERO), "83", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(leftFuzzyRepresentation(Rational.ZERO), "100", DEFAULT_RESOLUTION, "Optional.empty");

        digits_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "2",
                DEFAULT_RESOLUTION,
                "Optional[([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "3",
                DEFAULT_RESOLUTION,
                "Optional[([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "10",
                DEFAULT_RESOLUTION,
                "Optional[([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "16",
                DEFAULT_RESOLUTION,
                "Optional[([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "83",
                DEFAULT_RESOLUTION,
                "Optional[([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100",
                DEFAULT_RESOLUTION,
                "Optional[([], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );

        digits_helper(fuzzyRepresentation(Rational.ZERO), "2", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(fuzzyRepresentation(Rational.ZERO), "3", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(fuzzyRepresentation(Rational.ZERO), "10", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(fuzzyRepresentation(Rational.ZERO), "16", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(fuzzyRepresentation(Rational.ZERO), "83", DEFAULT_RESOLUTION, "Optional.empty");
        digits_helper(fuzzyRepresentation(Rational.ZERO), "100", DEFAULT_RESOLUTION, "Optional.empty");

        digits_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "2", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(
                leftFuzzyRepresentation(Rational.ONE_HALF),
                "3",
                DEFAULT_RESOLUTION,
                "Optional[([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])]"
        );
        digits_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "4", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "10", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "16", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(
                leftFuzzyRepresentation(Rational.ONE_HALF),
                "83",
                DEFAULT_RESOLUTION,
                "Optional[([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])]"
        );
        digits_helper(leftFuzzyRepresentation(Rational.ONE_HALF), "100", DEFAULT_RESOLUTION, "Optional[([], [0])]");

        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "2",
                DEFAULT_RESOLUTION,
                "Optional[([], [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "3",
                DEFAULT_RESOLUTION,
                "Optional[([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "4",
                DEFAULT_RESOLUTION,
                "Optional[([], [2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "10",
                DEFAULT_RESOLUTION,
                "Optional[([], [5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "16",
                DEFAULT_RESOLUTION,
                "Optional[([], [8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "83",
                DEFAULT_RESOLUTION,
                "Optional[([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])]"
        );
        digits_helper(
                rightFuzzyRepresentation(Rational.ONE_HALF),
                "100",
                DEFAULT_RESOLUTION,
                "Optional[([], [50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, ...])]"
        );

        digits_helper(fuzzyRepresentation(Rational.ONE_HALF), "2", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "3",
                DEFAULT_RESOLUTION,
                "Optional[([], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, ...])]"
        );
        digits_helper(fuzzyRepresentation(Rational.ONE_HALF), "4", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(fuzzyRepresentation(Rational.ONE_HALF), "10", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(fuzzyRepresentation(Rational.ONE_HALF), "16", DEFAULT_RESOLUTION, "Optional[([], [0])]");
        digits_helper(
                fuzzyRepresentation(Rational.ONE_HALF),
                "83",
                DEFAULT_RESOLUTION,
                "Optional[([], [41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, ...])]"
        );
        digits_helper(fuzzyRepresentation(Rational.ONE_HALF), "100", DEFAULT_RESOLUTION, "Optional[([], [0])]");

        digits_fail_helper(NEGATIVE_ONE, "2", DEFAULT_RESOLUTION);
        digits_fail_helper(NEGATIVE_ONE, "10", DEFAULT_RESOLUTION);
        digits_fail_helper(PI.negate(), "2", DEFAULT_RESOLUTION);
        digits_fail_helper(PI.negate(), "10", DEFAULT_RESOLUTION);
        digits_fail_helper(ZERO, "1", DEFAULT_RESOLUTION);
        digits_fail_helper(ZERO, "0", DEFAULT_RESOLUTION);
        digits_fail_helper(ZERO, "-1", DEFAULT_RESOLUTION);
        digits_fail_helper(PI, "1", DEFAULT_RESOLUTION);
        digits_fail_helper(PI, "0", DEFAULT_RESOLUTION);
        digits_fail_helper(PI, "-1", DEFAULT_RESOLUTION);

        digits_fail_helper(ZERO, "2", Rational.ZERO);
        digits_fail_helper(ZERO, "2", Rational.NEGATIVE_ONE);
        digits_fail_helper(PI, "2", Rational.ZERO);
        digits_fail_helper(PI, "2", Rational.NEGATIVE_ONE);
    }

    private static void fromDigits_helper(
            @NotNull String base,
            @NotNull String beforeDecimal,
            @NotNull Iterable<BigInteger> afterDecimal,
            @NotNull String output
    ) {
        Real x = fromDigits(Readers.readBigIntegerStrict(base).get(), readBigIntegerList(beforeDecimal), afterDecimal);
        x.validate();
        aeq(x, output);
    }

    private static void fromDigits_fail_helper(
            @NotNull String base,
            @NotNull String beforeDecimal,
            @NotNull Iterable<BigInteger> afterDecimal
    ) {
        try {
            //noinspection ResultOfMethodCallIgnored
            fromDigits(
                    Readers.readBigIntegerStrict(base).get(),
                    readBigIntegerListWithNulls(beforeDecimal),
                    afterDecimal
            ).toString();
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testFromDigits() {
        fromDigits_helper("2", "[]", Collections.emptyList(), "0.00000000000000000000...");
        fromDigits_helper("2", "[0]", Arrays.asList(BigInteger.ZERO, BigInteger.ONE), "0.25000000000000000000...");
        fromDigits_helper("2", "[1, 0]", repeat(BigInteger.ZERO), "2.00000000000000000000...");
        fromDigits_helper("2", "[0, 1, 0, 1]", repeat(BigInteger.ONE), "5.99999999999999999999...");
        fromDigits_helper("2", "[0, 1, 0, 1]", cycle(Arrays.asList(BigInteger.ZERO, BigInteger.ONE)),
                "5.33333333333333333333...");
        fromDigits_helper("2", "[1, 1]", PI.digitsUnsafe(IntegerUtils.TWO).b, "3.14159265358979323846...");

        fromDigits_helper("10", "[]", Collections.emptyList(), "0.00000000000000000000...");
        fromDigits_helper("10", "[0]", Arrays.asList(BigInteger.valueOf(3), BigInteger.valueOf(7)),
                "0.37000000000000000000...");
        fromDigits_helper("10", "[4, 5]", repeat(BigInteger.ZERO), "45.00000000000000000000...");
        fromDigits_helper("10", "[0, 2, 9, 8]", repeat(BigInteger.ONE), "298.11111111111111111111...");
        fromDigits_helper(
                "10",
                "[0, 2, 9, 8]",
                cycle(Arrays.asList(BigInteger.ONE, IntegerUtils.TWO, BigInteger.valueOf(3))),
                "298.12312312312312312312..."
        );
        fromDigits_helper("10", "[3]", PI.digitsUnsafe(BigInteger.TEN).b, "3.14159265358979323846...");

        fromDigits_fail_helper("2", "[3]", Collections.singletonList(BigInteger.ONE));
        fromDigits_fail_helper("2", "[1]", PI.digitsUnsafe(BigInteger.TEN).b);
        fromDigits_fail_helper("2", "[-1]", Collections.singletonList(BigInteger.ONE));
        fromDigits_fail_helper("2", "[1]", Collections.singletonList(IntegerUtils.NEGATIVE_ONE));
        fromDigits_fail_helper("2", "[null]", Collections.singletonList(BigInteger.ONE));
        fromDigits_fail_helper("2", "[1]", Collections.singletonList(null));
        fromDigits_fail_helper("1", "[0]", Collections.singletonList(BigInteger.ZERO));
        fromDigits_fail_helper("-1", "[0]", Collections.singletonList(BigInteger.ZERO));
    }

    private static void liouville_helper(@NotNull String input, @NotNull String output) {
        Real x = liouville(Readers.readBigIntegerStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    private static void liouville_fail_helper(@NotNull String input) {
        try {
            liouville(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLiouville() {
        liouville_helper("2", "0.76562505960464477539...");
        liouville_helper("3", "0.44581618656046800382...");
        liouville_helper("4", "0.31274414062500355271...");
        liouville_helper("10", "0.11000100000000000000...");
        liouville_helper("16", "0.06640630960464477539...");
        liouville_helper("83", "0.01219335172319220793...");
        liouville_helper("100", "0.01010000000100000000...");

        liouville_fail_helper("1");
        liouville_fail_helper("0");
        liouville_fail_helper("-1");
    }

    private static void champernowne_helper(@NotNull String input, @NotNull String output) {
        Real x = champernowne(Readers.readBigIntegerStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    private static void champernowne_fail_helper(@NotNull String input) {
        try {
            champernowne(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testChampernowne() {
        champernowne_helper("2", "0.86224012586805457155...");
        champernowne_helper("3", "0.59895816753843399250...");
        champernowne_helper("4", "0.42611111111111106576...");
        champernowne_helper("10", "0.12345678910111213141...");
        champernowne_helper("16", "0.07111111111111111023...");
        champernowne_helper("83", "0.01234384295062462819...");
        champernowne_helper("100", "0.01020304050607080910...");

        champernowne_fail_helper("1");
        champernowne_fail_helper("0");
        champernowne_fail_helper("-1");
    }

    private static void copelandErdos_helper(@NotNull String input, @NotNull String output) {
        Real x = copelandErdos(Readers.readBigIntegerStrict(input).get());
        x.validate();
        aeq(x, output);
    }

    private static void copelandErdos_fail_helper(@NotNull String input) {
        try {
            copelandErdos(Readers.readBigIntegerStrict(input).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCopelandErdos() {
        copelandErdos_helper("2", "0.73412151540828612060...");
        copelandErdos_helper("3", "0.80174949296954506695...");
        copelandErdos_helper("4", "0.70892073664967333384...");
        copelandErdos_helper("10", "0.23571113171923293137...");
        copelandErdos_helper("16", "0.13805753390178350683...");
        copelandErdos_helper("83", "0.02454075723511846217...");
        copelandErdos_helper("100", "0.02030507111317192329...");

        copelandErdos_fail_helper("1");
        copelandErdos_fail_helper("0");
        copelandErdos_fail_helper("-1");
    }

    private static void greedyNormal_helper(int input, @NotNull String output) {
        Real x = greedyNormal(input);
        x.validate();
        aeq(x, output);
    }

    private static void greedyNormal_fail_helper(int input) {
        try {
            greedyNormal(input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testGreedyNormal() {
        greedyNormal_helper(2, "0.30378814626375303291...");
        greedyNormal_helper(3, "0.19487034655056841016...");
        greedyNormal_helper(4, "0.10606849325923363398...");
        greedyNormal_helper(10, "0.01234567890213546879...");
        greedyNormal_helper(16, "0.00444444444444444438...");
        greedyNormal_helper(83, "0.00014872099940511600...");
        greedyNormal_helper(100, "0.00010203040506070809...");

        greedyNormal_fail_helper(1);
        greedyNormal_fail_helper(0);
        greedyNormal_fail_helper(-1);
    }

    private static void toStringBaseUnsafe_helper(
            @NotNull Real x,
            @NotNull String base,
            int scale,
            @NotNull String output
    ) {
        aeq(x.toStringBaseUnsafe(Readers.readBigIntegerStrict(base).get(), scale), output);
    }

    private static void toStringBaseUnsafe_fail_helper(@NotNull Real x, @NotNull String base, int scale) {
        try {
            x.toStringBaseUnsafe(Readers.readBigIntegerStrict(base).get(), scale);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToStringBaseUnsafe() {
        toStringBaseUnsafe_helper(ZERO, "10", 0, "0");
        toStringBaseUnsafe_helper(ZERO, "10", -1, "0");
        toStringBaseUnsafe_helper(ZERO, "10", 1, "0");
        toStringBaseUnsafe_helper(ZERO, "83", 0, "(0)");
        toStringBaseUnsafe_helper(ZERO, "83", -1, "(0)");
        toStringBaseUnsafe_helper(ZERO, "83", 1, "(0)");

        toStringBaseUnsafe_helper(ONE, "10", 0, "1");
        toStringBaseUnsafe_helper(ONE, "10", -1, "0");
        toStringBaseUnsafe_helper(ONE, "10", 1, "1");
        toStringBaseUnsafe_helper(ONE, "83", 0, "(1)");
        toStringBaseUnsafe_helper(ONE, "83", -1, "(0)");
        toStringBaseUnsafe_helper(ONE, "83", 1, "(1)");

        Real oneHundredNinetyEight = of(198);
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "10", 0, "198");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "10", 1, "198");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "10", -1, "190");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "10", -2, "100");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "10", -3, "0");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "83", 0, "(2)(32)");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "83", 1, "(2)(32)");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "83", -1, "(2)(0)");
        toStringBaseUnsafe_helper(oneHundredNinetyEight, "83", -2, "(0)");

        Real negativeOneSeventh = of(Rational.of(-1, 7));
        toStringBaseUnsafe_helper(negativeOneSeventh, "10", -1, "-0");
        toStringBaseUnsafe_helper(negativeOneSeventh, "10", 0, "-0");
        toStringBaseUnsafe_helper(negativeOneSeventh, "10", 5, "-0.14285...");
        toStringBaseUnsafe_helper(negativeOneSeventh, "10", 20, "-0.14285714285714285714...");
        toStringBaseUnsafe_helper(negativeOneSeventh, "83", -1, "-(0)");
        toStringBaseUnsafe_helper(negativeOneSeventh, "83", 0, "-(0)");
        toStringBaseUnsafe_helper(negativeOneSeventh, "83", 5, "-(0).(11)(71)(11)(71)(11)...");
        toStringBaseUnsafe_helper(negativeOneSeventh, "83", 20,
                "-(0).(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)...");

        Real oneThousandth = of(Rational.of(1, 1000));
        toStringBaseUnsafe_helper(oneThousandth, "10", 0, "0");
        toStringBaseUnsafe_helper(oneThousandth, "10", 1, "0.0...");
        toStringBaseUnsafe_helper(oneThousandth, "10", 2, "0.00...");
        toStringBaseUnsafe_helper(oneThousandth, "10", 3, "0.001");
        toStringBaseUnsafe_helper(oneThousandth, "10", 4, "0.001");

        Real oneThousandAndOneTenThousandths = of(Rational.of(1001, 10000));
        toStringBaseUnsafe_helper(oneThousandAndOneTenThousandths, "10", 0, "0");
        toStringBaseUnsafe_helper(oneThousandAndOneTenThousandths, "10", 1, "0.1...");
        toStringBaseUnsafe_helper(oneThousandAndOneTenThousandths, "10", 2, "0.10...");
        toStringBaseUnsafe_helper(oneThousandAndOneTenThousandths, "10", 3, "0.100...");
        toStringBaseUnsafe_helper(oneThousandAndOneTenThousandths, "10", 4, "0.1001");
        toStringBaseUnsafe_helper(oneThousandAndOneTenThousandths, "10", 5, "0.1001");

        Real oneMillionth = of(Rational.of(1, 1000000));
        toStringBaseUnsafe_helper(oneMillionth, "100", 0, "(0)");
        toStringBaseUnsafe_helper(oneMillionth, "100", 1, "(0).(0)...");
        toStringBaseUnsafe_helper(oneMillionth, "100", 2, "(0).(0)(0)...");
        toStringBaseUnsafe_helper(oneMillionth, "100", 3, "(0).(0)(0)(1)");
        toStringBaseUnsafe_helper(oneMillionth, "100", 4, "(0).(0)(0)(1)");

        Real oneMillionAndOneTenMillionths = of(Rational.of(1000001, 10000000));
        toStringBaseUnsafe_helper(oneMillionAndOneTenMillionths, "100", 0, "(0)");
        toStringBaseUnsafe_helper(oneMillionAndOneTenMillionths, "100", 1, "(0).(10)...");
        toStringBaseUnsafe_helper(oneMillionAndOneTenMillionths, "100", 2, "(0).(10)(0)...");
        toStringBaseUnsafe_helper(oneMillionAndOneTenMillionths, "100", 3, "(0).(10)(0)(0)...");
        toStringBaseUnsafe_helper(oneMillionAndOneTenMillionths, "100", 4, "(0).(10)(0)(0)(10)");
        toStringBaseUnsafe_helper(oneMillionAndOneTenMillionths, "100", 5, "(0).(10)(0)(0)(10)");

        toStringBaseUnsafe_helper(PI, "10", -1, "0");
        toStringBaseUnsafe_helper(PI, "10", 0, "3");
        toStringBaseUnsafe_helper(PI, "10", 5, "3.14159...");
        toStringBaseUnsafe_helper(PI, "10", 20, "3.14159265358979323846...");
        toStringBaseUnsafe_helper(PI, "83", -1, "(0)");
        toStringBaseUnsafe_helper(PI, "83", 0, "(3)");
        toStringBaseUnsafe_helper(PI, "83", 5, "(3).(11)(62)(35)(69)(50)...");
        toStringBaseUnsafe_helper(PI, "83", 20,
                "(3).(11)(62)(35)(69)(50)(19)(79)(18)(33)(82)(74)(23)(59)(17)(3)(18)(29)(47)(35)(11)...");

        toStringBaseUnsafe_helper(PI.negate(), "10", -1, "0");
        toStringBaseUnsafe_helper(PI.negate(), "10", 0, "-3");
        toStringBaseUnsafe_helper(PI.negate(), "10", 5, "-3.14159...");
        toStringBaseUnsafe_helper(PI.negate(), "10", 20, "-3.14159265358979323846...");
        toStringBaseUnsafe_helper(PI.negate(), "83", -1, "(0)");
        toStringBaseUnsafe_helper(PI.negate(), "83", 0, "-(3)");
        toStringBaseUnsafe_helper(PI.negate(), "83", 5, "-(3).(11)(62)(35)(69)(50)...");
        toStringBaseUnsafe_helper(PI.negate(), "83", 20,
                "-(3).(11)(62)(35)(69)(50)(19)(79)(18)(33)(82)(74)(23)(59)(17)(3)(18)(29)(47)(35)(11)...");

        toStringBaseUnsafe_fail_helper(ONE_HALF.negate(), "1", 5);
        toStringBaseUnsafe_fail_helper(ONE_HALF.negate(), "0", 5);
        toStringBaseUnsafe_fail_helper(ONE_HALF.negate(), "-1", 5);
    }

    private static void toStringBase_helper(
            @NotNull Real x,
            @NotNull String base,
            int scale,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(x.toStringBase(Readers.readBigIntegerStrict(base).get(), scale, resolution), output);
    }

    private static void toStringBase_fail_helper(
            @NotNull Real x,
            @NotNull String base,
            int scale,
            @NotNull Rational resolution
    ) {
        try {
            x.toStringBase(Readers.readBigIntegerStrict(base).get(), scale, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToStringBase() {
        toStringBase_helper(ZERO, "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(ZERO, "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(ZERO, "10", 1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(ZERO, "83", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(ZERO, "83", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(ZERO, "83", 1, DEFAULT_RESOLUTION, "(0)");

        toStringBase_helper(ONE, "10", 0, DEFAULT_RESOLUTION, "1");
        toStringBase_helper(ONE, "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(ONE, "10", 1, DEFAULT_RESOLUTION, "1");
        toStringBase_helper(ONE, "83", 0, DEFAULT_RESOLUTION, "(1)");
        toStringBase_helper(ONE, "83", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(ONE, "83", 1, DEFAULT_RESOLUTION, "(1)");

        Real oneHundredNinetyEight = of(198);
        toStringBase_helper(oneHundredNinetyEight, "10", 0, DEFAULT_RESOLUTION, "198");
        toStringBase_helper(oneHundredNinetyEight, "10", 1, DEFAULT_RESOLUTION, "198");
        toStringBase_helper(oneHundredNinetyEight, "10", -1, DEFAULT_RESOLUTION, "190");
        toStringBase_helper(oneHundredNinetyEight, "10", -2, DEFAULT_RESOLUTION, "100");
        toStringBase_helper(oneHundredNinetyEight, "10", -3, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(oneHundredNinetyEight, "83", 0, DEFAULT_RESOLUTION, "(2)(32)");
        toStringBase_helper(oneHundredNinetyEight, "83", 1, DEFAULT_RESOLUTION, "(2)(32)");
        toStringBase_helper(oneHundredNinetyEight, "83", -1, DEFAULT_RESOLUTION, "(2)(0)");
        toStringBase_helper(oneHundredNinetyEight, "83", -2, DEFAULT_RESOLUTION, "(0)");

        Real negativeOneSeventh = of(Rational.of(-1, 7));
        toStringBase_helper(negativeOneSeventh, "10", -1, DEFAULT_RESOLUTION, "-0");
        toStringBase_helper(negativeOneSeventh, "10", 0, DEFAULT_RESOLUTION, "-0");
        toStringBase_helper(negativeOneSeventh, "10", 5, DEFAULT_RESOLUTION, "-0.14285...");
        toStringBase_helper(negativeOneSeventh, "10", 20, DEFAULT_RESOLUTION, "-0.14285714285714285714...");
        toStringBase_helper(negativeOneSeventh, "83", -1, DEFAULT_RESOLUTION, "-(0)");
        toStringBase_helper(negativeOneSeventh, "83", 0, DEFAULT_RESOLUTION, "-(0)");
        toStringBase_helper(negativeOneSeventh, "83", 5, DEFAULT_RESOLUTION, "-(0).(11)(71)(11)(71)(11)...");
        toStringBase_helper(negativeOneSeventh, "83", 20, DEFAULT_RESOLUTION,
                "-(0).(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)(11)(71)...");

        Real oneThousandth = of(Rational.of(1, 1000));
        toStringBase_helper(oneThousandth, "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(oneThousandth, "10", 1, DEFAULT_RESOLUTION, "0.0...");
        toStringBase_helper(oneThousandth, "10", 2, DEFAULT_RESOLUTION, "0.00...");
        toStringBase_helper(oneThousandth, "10", 3, DEFAULT_RESOLUTION, "0.001");
        toStringBase_helper(oneThousandth, "10", 4, DEFAULT_RESOLUTION, "0.001");

        Real oneThousandAndOneTenThousandths = of(Rational.of(1001, 10000));
        toStringBase_helper(oneThousandAndOneTenThousandths, "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(oneThousandAndOneTenThousandths, "10", 1, DEFAULT_RESOLUTION, "0.1...");
        toStringBase_helper(oneThousandAndOneTenThousandths, "10", 2, DEFAULT_RESOLUTION, "0.10...");
        toStringBase_helper(oneThousandAndOneTenThousandths, "10", 3, DEFAULT_RESOLUTION, "0.100...");
        toStringBase_helper(oneThousandAndOneTenThousandths, "10", 4, DEFAULT_RESOLUTION, "0.1001");
        toStringBase_helper(oneThousandAndOneTenThousandths, "10", 5, DEFAULT_RESOLUTION, "0.1001");

        Real oneMillionth = of(Rational.of(1, 1000000));
        toStringBase_helper(oneMillionth, "100", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(oneMillionth, "100", 1, DEFAULT_RESOLUTION, "(0).(0)...");
        toStringBase_helper(oneMillionth, "100", 2, DEFAULT_RESOLUTION, "(0).(0)(0)...");
        toStringBase_helper(oneMillionth, "100", 3, DEFAULT_RESOLUTION, "(0).(0)(0)(1)");
        toStringBase_helper(oneMillionth, "100", 4, DEFAULT_RESOLUTION, "(0).(0)(0)(1)");

        Real oneMillionAndOneTenMillionths = of(Rational.of(1000001, 10000000));
        toStringBase_helper(oneMillionAndOneTenMillionths, "100", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(oneMillionAndOneTenMillionths, "100", 1, DEFAULT_RESOLUTION, "(0).(10)...");
        toStringBase_helper(oneMillionAndOneTenMillionths, "100", 2, DEFAULT_RESOLUTION, "(0).(10)(0)...");
        toStringBase_helper(oneMillionAndOneTenMillionths, "100", 3, DEFAULT_RESOLUTION, "(0).(10)(0)(0)...");
        toStringBase_helper(oneMillionAndOneTenMillionths, "100", 4, DEFAULT_RESOLUTION, "(0).(10)(0)(0)(10)");
        toStringBase_helper(oneMillionAndOneTenMillionths, "100", 5, DEFAULT_RESOLUTION, "(0).(10)(0)(0)(10)");

        toStringBase_helper(PI, "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(PI, "10", 0, DEFAULT_RESOLUTION, "3");
        toStringBase_helper(PI, "10", 5, DEFAULT_RESOLUTION, "3.14159...");
        toStringBase_helper(PI, "10", 20, DEFAULT_RESOLUTION, "3.14159265358979323846...");
        toStringBase_helper(PI, "83", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(PI, "83", 0, DEFAULT_RESOLUTION, "(3)");
        toStringBase_helper(PI, "83", 5, DEFAULT_RESOLUTION, "(3).(11)(62)(35)(69)(50)...");
        toStringBase_helper(PI, "83", 20, DEFAULT_RESOLUTION,
                "(3).(11)(62)(35)(69)(50)(19)(79)(18)(33)(82)(74)(23)(59)(17)(3)(18)(29)(47)(35)(11)...");

        toStringBase_helper(PI.negate(), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(PI.negate(), "10", 0, DEFAULT_RESOLUTION, "-3");
        toStringBase_helper(PI.negate(), "10", 5, DEFAULT_RESOLUTION, "-3.14159...");
        toStringBase_helper(PI.negate(), "10", 20, DEFAULT_RESOLUTION, "-3.14159265358979323846...");
        toStringBase_helper(PI.negate(), "83", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(PI.negate(), "83", 0, DEFAULT_RESOLUTION, "-(3)");
        toStringBase_helper(PI.negate(), "83", 5, DEFAULT_RESOLUTION, "-(3).(11)(62)(35)(69)(50)...");
        toStringBase_helper(PI.negate(), "83", 20, DEFAULT_RESOLUTION,
                "-(3).(11)(62)(35)(69)(50)(19)(79)(18)(33)(82)(74)(23)(59)(17)(3)(18)(29)(47)(35)(11)...");

        toStringBase_helper(leftFuzzyRepresentation(Rational.ZERO), "10", 0, DEFAULT_RESOLUTION, "-0");
        toStringBase_helper(leftFuzzyRepresentation(Rational.ZERO), "10", -1, DEFAULT_RESOLUTION, "-0");
        toStringBase_helper(leftFuzzyRepresentation(Rational.ZERO), "10", 1, DEFAULT_RESOLUTION, "-0.0...");
        toStringBase_helper(leftFuzzyRepresentation(Rational.ZERO), "83", 0, DEFAULT_RESOLUTION, "-(0)");
        toStringBase_helper(leftFuzzyRepresentation(Rational.ZERO), "83", -1, DEFAULT_RESOLUTION, "-(0)");
        toStringBase_helper(leftFuzzyRepresentation(Rational.ZERO), "83", 1, DEFAULT_RESOLUTION, "-(0).(0)...");

        toStringBase_helper(rightFuzzyRepresentation(Rational.ZERO), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(rightFuzzyRepresentation(Rational.ZERO), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(rightFuzzyRepresentation(Rational.ZERO), "10", 1, DEFAULT_RESOLUTION, "0.0...");
        toStringBase_helper(rightFuzzyRepresentation(Rational.ZERO), "83", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(rightFuzzyRepresentation(Rational.ZERO), "83", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(rightFuzzyRepresentation(Rational.ZERO), "83", 1, DEFAULT_RESOLUTION, "(0).(0)...");

        toStringBase_helper(fuzzyRepresentation(Rational.ZERO), "10", 0, DEFAULT_RESOLUTION, "~0");
        toStringBase_helper(fuzzyRepresentation(Rational.ZERO), "10", -1, DEFAULT_RESOLUTION, "~0");
        toStringBase_helper(fuzzyRepresentation(Rational.ZERO), "10", 1, DEFAULT_RESOLUTION, "~0");
        toStringBase_helper(fuzzyRepresentation(Rational.ZERO), "83", 0, DEFAULT_RESOLUTION, "~(0)");
        toStringBase_helper(fuzzyRepresentation(Rational.ZERO), "83", -1, DEFAULT_RESOLUTION, "~(0)");
        toStringBase_helper(fuzzyRepresentation(Rational.ZERO), "83", 1, DEFAULT_RESOLUTION, "~(0)");

        Rational threeFourths = Rational.of(3, 4);
        toStringBase_helper(of(threeFourths), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(of(threeFourths), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(of(threeFourths), "10", 6, DEFAULT_RESOLUTION, "0.75");
        toStringBase_helper(of(threeFourths), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(of(threeFourths), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(of(threeFourths), "64", 6, DEFAULT_RESOLUTION, "(0).(48)");

        toStringBase_helper(leftFuzzyRepresentation(threeFourths), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths), "10", 6, DEFAULT_RESOLUTION, "0.749999...");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths), "64", 6, DEFAULT_RESOLUTION,
                "(0).(47)(63)(63)(63)(63)(63)...");

        toStringBase_helper(rightFuzzyRepresentation(threeFourths), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths), "10", 6, DEFAULT_RESOLUTION, "0.750000...");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths), "64", 6, DEFAULT_RESOLUTION,
                "(0).(48)(0)(0)(0)(0)(0)...");

        toStringBase_helper(fuzzyRepresentation(threeFourths), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(fuzzyRepresentation(threeFourths), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(fuzzyRepresentation(threeFourths), "10", 6, DEFAULT_RESOLUTION, "~0.75");
        toStringBase_helper(fuzzyRepresentation(threeFourths), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(fuzzyRepresentation(threeFourths), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(fuzzyRepresentation(threeFourths), "64", 6, DEFAULT_RESOLUTION, "~(0).(48)");

        toStringBase_helper(of(threeFourths.negate()), "10", 0, DEFAULT_RESOLUTION, "-0");
        toStringBase_helper(of(threeFourths.negate()), "10", -1, DEFAULT_RESOLUTION, "-0");
        toStringBase_helper(of(threeFourths.negate()), "10", 6, DEFAULT_RESOLUTION, "-0.75");
        toStringBase_helper(of(threeFourths.negate()), "64", 0, DEFAULT_RESOLUTION, "-(0)");
        toStringBase_helper(of(threeFourths.negate()), "64", -1, DEFAULT_RESOLUTION, "-(0)");
        toStringBase_helper(of(threeFourths.negate()), "64", 6, DEFAULT_RESOLUTION, "-(0).(48)");

        toStringBase_helper(leftFuzzyRepresentation(threeFourths.negate()), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths.negate()), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths.negate()), "10", 6, DEFAULT_RESOLUTION,
                "-0.750000...");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths.negate()), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths.negate()), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(leftFuzzyRepresentation(threeFourths.negate()), "64", 6, DEFAULT_RESOLUTION,
                "-(0).(48)(0)(0)(0)(0)(0)...");

        toStringBase_helper(rightFuzzyRepresentation(threeFourths.negate()), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths.negate()), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths.negate()), "10", 6, DEFAULT_RESOLUTION,
                "-0.749999...");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths.negate()), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths.negate()), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(rightFuzzyRepresentation(threeFourths.negate()), "64", 6, DEFAULT_RESOLUTION,
                "-(0).(47)(63)(63)(63)(63)(63)...");

        toStringBase_helper(fuzzyRepresentation(threeFourths.negate()), "10", 0, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(fuzzyRepresentation(threeFourths.negate()), "10", -1, DEFAULT_RESOLUTION, "0");
        toStringBase_helper(fuzzyRepresentation(threeFourths.negate()), "10", 6, DEFAULT_RESOLUTION, "~-0.75");
        toStringBase_helper(fuzzyRepresentation(threeFourths.negate()), "64", 0, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(fuzzyRepresentation(threeFourths.negate()), "64", -1, DEFAULT_RESOLUTION, "(0)");
        toStringBase_helper(fuzzyRepresentation(threeFourths.negate()), "64", 6, DEFAULT_RESOLUTION, "~-(0).(48)");

        toStringBase_fail_helper(ONE_HALF.negate(), "1", 5, DEFAULT_RESOLUTION);
        toStringBase_fail_helper(ONE_HALF.negate(), "0", 5, DEFAULT_RESOLUTION);
        toStringBase_fail_helper(ONE_HALF.negate(), "-1", 5, DEFAULT_RESOLUTION);
        toStringBase_fail_helper(ONE_HALF, "10", 5, Rational.ZERO);
        toStringBase_fail_helper(ONE_HALF, "10", 5, Rational.NEGATIVE_ONE);
        toStringBase_fail_helper(ONE_HALF, "10", 5, threeFourths);
    }

    private static void equals_fail_helper(@NotNull Real x, @NotNull Real y) {
        try {
            //noinspection ResultOfMethodCallIgnored
            x.equals(y);
            fail();
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testEquals() {
        equals_fail_helper(ZERO, ZERO);
        equals_fail_helper(ZERO, ONE_HALF);
        equals_fail_helper(ZERO, PI);
        equals_fail_helper(ZERO, fuzzyRepresentation(Rational.ZERO));

        equals_fail_helper(ONE_HALF, ZERO);
        equals_fail_helper(ONE_HALF, ONE_HALF);
        equals_fail_helper(ONE_HALF, PI);
        equals_fail_helper(ONE_HALF, fuzzyRepresentation(Rational.ZERO));

        equals_fail_helper(PI, ZERO);
        equals_fail_helper(PI, ONE_HALF);
        equals_fail_helper(PI, PI);
        equals_fail_helper(PI, fuzzyRepresentation(Rational.ZERO));

        equals_fail_helper(fuzzyRepresentation(Rational.ZERO), ZERO);
        equals_fail_helper(fuzzyRepresentation(Rational.ZERO), ONE_HALF);
        equals_fail_helper(fuzzyRepresentation(Rational.ZERO), PI);
        equals_fail_helper(fuzzyRepresentation(Rational.ZERO), fuzzyRepresentation(Rational.ZERO));
    }

    private static void hashCode_fail_helper(@NotNull Real x) {
        try {
            //noinspection ResultOfMethodCallIgnored
            x.hashCode();
            fail();
        } catch (UnsupportedOperationException ignored) {}
    }

    @Test
    public void testHashCode() {
        hashCode_fail_helper(ZERO);
        hashCode_fail_helper(ONE_HALF);
        hashCode_fail_helper(PI);
        hashCode_fail_helper(fuzzyRepresentation(Rational.ZERO));
    }

    private static void compareToUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, @NotNull String output) {
        aeq(Ordering.fromInt(a.compareToUnsafe(Rational.readStrict(b).get())), output);
    }

    @Test
    public void testCompareToUnsafe_Rational() {
        compareToUnsafe_Rational_helper(ZERO, "0", "=");
        compareToUnsafe_Rational_helper(ZERO, "1", "<");
        compareToUnsafe_Rational_helper(ZERO, "-1", ">");
        compareToUnsafe_Rational_helper(ZERO, "100/3", "<");
        compareToUnsafe_Rational_helper(ZERO, "1/100", "<");

        compareToUnsafe_Rational_helper(ONE, "0", ">");
        compareToUnsafe_Rational_helper(ONE, "1", "=");
        compareToUnsafe_Rational_helper(ONE, "-1", ">");
        compareToUnsafe_Rational_helper(ONE, "100/3", "<");
        compareToUnsafe_Rational_helper(ONE, "1/100", ">");

        compareToUnsafe_Rational_helper(NEGATIVE_ONE, "0", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_ONE, "1", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_ONE, "-1", "=");
        compareToUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", "<");

        compareToUnsafe_Rational_helper(ONE_HALF, "0", ">");
        compareToUnsafe_Rational_helper(ONE_HALF, "1", "<");
        compareToUnsafe_Rational_helper(ONE_HALF, "-1", ">");
        compareToUnsafe_Rational_helper(ONE_HALF, "100/3", "<");
        compareToUnsafe_Rational_helper(ONE_HALF, "1/100", ">");

        compareToUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", "<");
        compareToUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", "<");

        compareToUnsafe_Rational_helper(SQRT_TWO, "0", ">");
        compareToUnsafe_Rational_helper(SQRT_TWO, "1", ">");
        compareToUnsafe_Rational_helper(SQRT_TWO, "-1", ">");
        compareToUnsafe_Rational_helper(SQRT_TWO, "100/3", "<");
        compareToUnsafe_Rational_helper(SQRT_TWO, "1/100", ">");

        compareToUnsafe_Rational_helper(E, "0", ">");
        compareToUnsafe_Rational_helper(E, "1", ">");
        compareToUnsafe_Rational_helper(E, "-1", ">");
        compareToUnsafe_Rational_helper(E, "100/3", "<");
        compareToUnsafe_Rational_helper(E, "1/100", ">");

        compareToUnsafe_Rational_helper(PI, "0", ">");
        compareToUnsafe_Rational_helper(PI, "1", ">");
        compareToUnsafe_Rational_helper(PI, "-1", ">");
        compareToUnsafe_Rational_helper(PI, "100/3", "<");
        compareToUnsafe_Rational_helper(PI, "1/100", ">");

        compareToUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", "<");
        compareToUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", ">");
        compareToUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", "<");
        compareToUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", "<");

        compareToUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", "<");
        compareToUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", ">");
        compareToUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", "<");
        compareToUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", "<");

        compareToUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", "<");
        compareToUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", ">");
        compareToUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", "<");
        compareToUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", "<");
    }

    private static void compareTo_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.compareTo(Rational.readStrict(b).get(), resolution).map(Ordering::fromInt), output);
    }

    private static void compareTo_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.compareTo(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCompareTo_Rational_Rational() {
        compareTo_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[<]");

        compareTo_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[<]");

        compareTo_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[<]");

        compareTo_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );

        compareTo_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );

        compareTo_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );

        compareTo_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        compareTo_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        compareTo_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        compareTo_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void eqUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, boolean output) {
        aeq(a.eqUnsafe(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testEqUnsafe_Rational() {
        eqUnsafe_Rational_helper(ZERO, "0", true);
        eqUnsafe_Rational_helper(ZERO, "1", false);
        eqUnsafe_Rational_helper(ZERO, "-1", false);
        eqUnsafe_Rational_helper(ZERO, "100/3", false);
        eqUnsafe_Rational_helper(ZERO, "1/100", false);

        eqUnsafe_Rational_helper(ONE, "0", false);
        eqUnsafe_Rational_helper(ONE, "1", true);
        eqUnsafe_Rational_helper(ONE, "-1", false);
        eqUnsafe_Rational_helper(ONE, "100/3", false);
        eqUnsafe_Rational_helper(ONE, "1/100", false);

        eqUnsafe_Rational_helper(NEGATIVE_ONE, "0", false);
        eqUnsafe_Rational_helper(NEGATIVE_ONE, "1", false);
        eqUnsafe_Rational_helper(NEGATIVE_ONE, "-1", true);
        eqUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", false);
        eqUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", false);

        eqUnsafe_Rational_helper(ONE_HALF, "0", false);
        eqUnsafe_Rational_helper(ONE_HALF, "1", false);
        eqUnsafe_Rational_helper(ONE_HALF, "-1", false);
        eqUnsafe_Rational_helper(ONE_HALF, "100/3", false);
        eqUnsafe_Rational_helper(ONE_HALF, "1/100", false);

        eqUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", false);
        eqUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", false);
        eqUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", false);
        eqUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", false);
        eqUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", false);

        eqUnsafe_Rational_helper(SQRT_TWO, "0", false);
        eqUnsafe_Rational_helper(SQRT_TWO, "1", false);
        eqUnsafe_Rational_helper(SQRT_TWO, "-1", false);
        eqUnsafe_Rational_helper(SQRT_TWO, "100/3", false);
        eqUnsafe_Rational_helper(SQRT_TWO, "1/100", false);

        eqUnsafe_Rational_helper(E, "0", false);
        eqUnsafe_Rational_helper(E, "1", false);
        eqUnsafe_Rational_helper(E, "-1", false);
        eqUnsafe_Rational_helper(E, "100/3", false);
        eqUnsafe_Rational_helper(E, "1/100", false);

        eqUnsafe_Rational_helper(PI, "0", false);
        eqUnsafe_Rational_helper(PI, "1", false);
        eqUnsafe_Rational_helper(PI, "-1", false);
        eqUnsafe_Rational_helper(PI, "100/3", false);
        eqUnsafe_Rational_helper(PI, "1/100", false);

        eqUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", false);
        eqUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", false);
        eqUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", false);
        eqUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", false);

        eqUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", false);
        eqUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", false);
        eqUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", false);
        eqUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", false);

        eqUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", false);
        eqUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", false);
        eqUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", false);
        eqUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", false);
    }

    private static void neUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, boolean output) {
        aeq(a.neUnsafe(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testNeUnsafe_Rational() {
        neUnsafe_Rational_helper(ZERO, "0", false);
        neUnsafe_Rational_helper(ZERO, "1", true);
        neUnsafe_Rational_helper(ZERO, "-1", true);
        neUnsafe_Rational_helper(ZERO, "100/3", true);
        neUnsafe_Rational_helper(ZERO, "1/100", true);

        neUnsafe_Rational_helper(ONE, "0", true);
        neUnsafe_Rational_helper(ONE, "1", false);
        neUnsafe_Rational_helper(ONE, "-1", true);
        neUnsafe_Rational_helper(ONE, "100/3", true);
        neUnsafe_Rational_helper(ONE, "1/100", true);

        neUnsafe_Rational_helper(NEGATIVE_ONE, "0", true);
        neUnsafe_Rational_helper(NEGATIVE_ONE, "1", true);
        neUnsafe_Rational_helper(NEGATIVE_ONE, "-1", false);
        neUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", true);
        neUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", true);

        neUnsafe_Rational_helper(ONE_HALF, "0", true);
        neUnsafe_Rational_helper(ONE_HALF, "1", true);
        neUnsafe_Rational_helper(ONE_HALF, "-1", true);
        neUnsafe_Rational_helper(ONE_HALF, "100/3", true);
        neUnsafe_Rational_helper(ONE_HALF, "1/100", true);

        neUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", true);
        neUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", true);
        neUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", true);
        neUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", true);
        neUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", true);

        neUnsafe_Rational_helper(SQRT_TWO, "0", true);
        neUnsafe_Rational_helper(SQRT_TWO, "1", true);
        neUnsafe_Rational_helper(SQRT_TWO, "-1", true);
        neUnsafe_Rational_helper(SQRT_TWO, "100/3", true);
        neUnsafe_Rational_helper(SQRT_TWO, "1/100", true);

        neUnsafe_Rational_helper(E, "0", true);
        neUnsafe_Rational_helper(E, "1", true);
        neUnsafe_Rational_helper(E, "-1", true);
        neUnsafe_Rational_helper(E, "100/3", true);
        neUnsafe_Rational_helper(E, "1/100", true);

        neUnsafe_Rational_helper(PI, "0", true);
        neUnsafe_Rational_helper(PI, "1", true);
        neUnsafe_Rational_helper(PI, "-1", true);
        neUnsafe_Rational_helper(PI, "100/3", true);
        neUnsafe_Rational_helper(PI, "1/100", true);

        neUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", true);
        neUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", true);
        neUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", true);
        neUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", true);

        neUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", true);
        neUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", true);
        neUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", true);
        neUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", true);

        neUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", true);
        neUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", true);
        neUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", true);
        neUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", true);
    }

    private static void ltUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, boolean output) {
        aeq(a.ltUnsafe(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testLtUnsafe_Rational() {
        ltUnsafe_Rational_helper(ZERO, "0", false);
        ltUnsafe_Rational_helper(ZERO, "1", true);
        ltUnsafe_Rational_helper(ZERO, "-1", false);
        ltUnsafe_Rational_helper(ZERO, "100/3", true);
        ltUnsafe_Rational_helper(ZERO, "1/100", true);

        ltUnsafe_Rational_helper(ONE, "0", false);
        ltUnsafe_Rational_helper(ONE, "1", false);
        ltUnsafe_Rational_helper(ONE, "-1", false);
        ltUnsafe_Rational_helper(ONE, "100/3", true);
        ltUnsafe_Rational_helper(ONE, "1/100", false);

        ltUnsafe_Rational_helper(NEGATIVE_ONE, "0", true);
        ltUnsafe_Rational_helper(NEGATIVE_ONE, "1", true);
        ltUnsafe_Rational_helper(NEGATIVE_ONE, "-1", false);
        ltUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", true);
        ltUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", true);

        ltUnsafe_Rational_helper(ONE_HALF, "0", false);
        ltUnsafe_Rational_helper(ONE_HALF, "1", true);
        ltUnsafe_Rational_helper(ONE_HALF, "-1", false);
        ltUnsafe_Rational_helper(ONE_HALF, "100/3", true);
        ltUnsafe_Rational_helper(ONE_HALF, "1/100", false);

        ltUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", true);
        ltUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", true);
        ltUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", true);
        ltUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", true);
        ltUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", true);

        ltUnsafe_Rational_helper(SQRT_TWO, "0", false);
        ltUnsafe_Rational_helper(SQRT_TWO, "1", false);
        ltUnsafe_Rational_helper(SQRT_TWO, "-1", false);
        ltUnsafe_Rational_helper(SQRT_TWO, "100/3", true);
        ltUnsafe_Rational_helper(SQRT_TWO, "1/100", false);

        ltUnsafe_Rational_helper(E, "0", false);
        ltUnsafe_Rational_helper(E, "1", false);
        ltUnsafe_Rational_helper(E, "-1", false);
        ltUnsafe_Rational_helper(E, "100/3", true);
        ltUnsafe_Rational_helper(E, "1/100", false);

        ltUnsafe_Rational_helper(PI, "0", false);
        ltUnsafe_Rational_helper(PI, "1", false);
        ltUnsafe_Rational_helper(PI, "-1", false);
        ltUnsafe_Rational_helper(PI, "100/3", true);
        ltUnsafe_Rational_helper(PI, "1/100", false);

        ltUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", true);
        ltUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", false);
        ltUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", true);
        ltUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", true);

        ltUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", true);
        ltUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", false);
        ltUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", true);
        ltUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", true);

        ltUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", true);
        ltUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", false);
        ltUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", true);
        ltUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", true);
    }

    private static void gtUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, boolean output) {
        aeq(a.gtUnsafe(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testGtUnsafe_Rational() {
        gtUnsafe_Rational_helper(ZERO, "0", false);
        gtUnsafe_Rational_helper(ZERO, "1", false);
        gtUnsafe_Rational_helper(ZERO, "-1", true);
        gtUnsafe_Rational_helper(ZERO, "100/3", false);
        gtUnsafe_Rational_helper(ZERO, "1/100", false);

        gtUnsafe_Rational_helper(ONE, "0", true);
        gtUnsafe_Rational_helper(ONE, "1", false);
        gtUnsafe_Rational_helper(ONE, "-1", true);
        gtUnsafe_Rational_helper(ONE, "100/3", false);
        gtUnsafe_Rational_helper(ONE, "1/100", true);

        gtUnsafe_Rational_helper(NEGATIVE_ONE, "0", false);
        gtUnsafe_Rational_helper(NEGATIVE_ONE, "1", false);
        gtUnsafe_Rational_helper(NEGATIVE_ONE, "-1", false);
        gtUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", false);
        gtUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", false);

        gtUnsafe_Rational_helper(ONE_HALF, "0", true);
        gtUnsafe_Rational_helper(ONE_HALF, "1", false);
        gtUnsafe_Rational_helper(ONE_HALF, "-1", true);
        gtUnsafe_Rational_helper(ONE_HALF, "100/3", false);
        gtUnsafe_Rational_helper(ONE_HALF, "1/100", true);

        gtUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", false);
        gtUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", false);
        gtUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", false);
        gtUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", false);
        gtUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", false);

        gtUnsafe_Rational_helper(SQRT_TWO, "0", true);
        gtUnsafe_Rational_helper(SQRT_TWO, "1", true);
        gtUnsafe_Rational_helper(SQRT_TWO, "-1", true);
        gtUnsafe_Rational_helper(SQRT_TWO, "100/3", false);
        gtUnsafe_Rational_helper(SQRT_TWO, "1/100", true);

        gtUnsafe_Rational_helper(E, "0", true);
        gtUnsafe_Rational_helper(E, "1", true);
        gtUnsafe_Rational_helper(E, "-1", true);
        gtUnsafe_Rational_helper(E, "100/3", false);
        gtUnsafe_Rational_helper(E, "1/100", true);

        gtUnsafe_Rational_helper(PI, "0", true);
        gtUnsafe_Rational_helper(PI, "1", true);
        gtUnsafe_Rational_helper(PI, "-1", true);
        gtUnsafe_Rational_helper(PI, "100/3", false);
        gtUnsafe_Rational_helper(PI, "1/100", true);

        gtUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", false);
        gtUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", true);
        gtUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", false);
        gtUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", false);

        gtUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", false);
        gtUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", true);
        gtUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", false);
        gtUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", false);

        gtUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", false);
        gtUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", true);
        gtUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", false);
        gtUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", false);
    }

    private static void leUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, boolean output) {
        aeq(a.leUnsafe(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testLeUnsafe_Rational() {
        leUnsafe_Rational_helper(ZERO, "0", true);
        leUnsafe_Rational_helper(ZERO, "1", true);
        leUnsafe_Rational_helper(ZERO, "-1", false);
        leUnsafe_Rational_helper(ZERO, "100/3", true);
        leUnsafe_Rational_helper(ZERO, "1/100", true);

        leUnsafe_Rational_helper(ONE, "0", false);
        leUnsafe_Rational_helper(ONE, "1", true);
        leUnsafe_Rational_helper(ONE, "-1", false);
        leUnsafe_Rational_helper(ONE, "100/3", true);
        leUnsafe_Rational_helper(ONE, "1/100", false);

        leUnsafe_Rational_helper(NEGATIVE_ONE, "0", true);
        leUnsafe_Rational_helper(NEGATIVE_ONE, "1", true);
        leUnsafe_Rational_helper(NEGATIVE_ONE, "-1", true);
        leUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", true);
        leUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", true);

        leUnsafe_Rational_helper(ONE_HALF, "0", false);
        leUnsafe_Rational_helper(ONE_HALF, "1", true);
        leUnsafe_Rational_helper(ONE_HALF, "-1", false);
        leUnsafe_Rational_helper(ONE_HALF, "100/3", true);
        leUnsafe_Rational_helper(ONE_HALF, "1/100", false);

        leUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", true);
        leUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", true);
        leUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", true);
        leUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", true);
        leUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", true);

        leUnsafe_Rational_helper(SQRT_TWO, "0", false);
        leUnsafe_Rational_helper(SQRT_TWO, "1", false);
        leUnsafe_Rational_helper(SQRT_TWO, "-1", false);
        leUnsafe_Rational_helper(SQRT_TWO, "100/3", true);
        leUnsafe_Rational_helper(SQRT_TWO, "1/100", false);

        leUnsafe_Rational_helper(E, "0", false);
        leUnsafe_Rational_helper(E, "1", false);
        leUnsafe_Rational_helper(E, "-1", false);
        leUnsafe_Rational_helper(E, "100/3", true);
        leUnsafe_Rational_helper(E, "1/100", false);

        leUnsafe_Rational_helper(PI, "0", false);
        leUnsafe_Rational_helper(PI, "1", false);
        leUnsafe_Rational_helper(PI, "-1", false);
        leUnsafe_Rational_helper(PI, "100/3", true);
        leUnsafe_Rational_helper(PI, "1/100", false);

        leUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", true);
        leUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", true);
        leUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", false);
        leUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", true);
        leUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", true);

        leUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", true);
        leUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", false);
        leUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", true);
        leUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", true);

        leUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", true);
        leUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", false);
        leUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", true);
        leUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", true);
    }

    private static void geUnsafe_Rational_helper(@NotNull Real a, @NotNull String b, boolean output) {
        aeq(a.geUnsafe(Rational.readStrict(b).get()), output);
    }

    @Test
    public void testGeUnsafe_Rational() {
        geUnsafe_Rational_helper(ZERO, "0", true);
        geUnsafe_Rational_helper(ZERO, "1", false);
        geUnsafe_Rational_helper(ZERO, "-1", true);
        geUnsafe_Rational_helper(ZERO, "100/3", false);
        geUnsafe_Rational_helper(ZERO, "1/100", false);

        geUnsafe_Rational_helper(ONE, "0", true);
        geUnsafe_Rational_helper(ONE, "1", true);
        geUnsafe_Rational_helper(ONE, "-1", true);
        geUnsafe_Rational_helper(ONE, "100/3", false);
        geUnsafe_Rational_helper(ONE, "1/100", true);

        geUnsafe_Rational_helper(NEGATIVE_ONE, "0", false);
        geUnsafe_Rational_helper(NEGATIVE_ONE, "1", false);
        geUnsafe_Rational_helper(NEGATIVE_ONE, "-1", true);
        geUnsafe_Rational_helper(NEGATIVE_ONE, "100/3", false);
        geUnsafe_Rational_helper(NEGATIVE_ONE, "1/100", false);

        geUnsafe_Rational_helper(ONE_HALF, "0", true);
        geUnsafe_Rational_helper(ONE_HALF, "1", false);
        geUnsafe_Rational_helper(ONE_HALF, "-1", true);
        geUnsafe_Rational_helper(ONE_HALF, "100/3", false);
        geUnsafe_Rational_helper(ONE_HALF, "1/100", true);

        geUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", false);
        geUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", false);
        geUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", false);
        geUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", false);
        geUnsafe_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", false);

        geUnsafe_Rational_helper(SQRT_TWO, "0", true);
        geUnsafe_Rational_helper(SQRT_TWO, "1", true);
        geUnsafe_Rational_helper(SQRT_TWO, "-1", true);
        geUnsafe_Rational_helper(SQRT_TWO, "100/3", false);
        geUnsafe_Rational_helper(SQRT_TWO, "1/100", true);

        geUnsafe_Rational_helper(E, "0", true);
        geUnsafe_Rational_helper(E, "1", true);
        geUnsafe_Rational_helper(E, "-1", true);
        geUnsafe_Rational_helper(E, "100/3", false);
        geUnsafe_Rational_helper(E, "1/100", true);

        geUnsafe_Rational_helper(PI, "0", true);
        geUnsafe_Rational_helper(PI, "1", true);
        geUnsafe_Rational_helper(PI, "-1", true);
        geUnsafe_Rational_helper(PI, "100/3", false);
        geUnsafe_Rational_helper(PI, "1/100", true);

        geUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", false);
        geUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "-1", true);
        geUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "100/3", false);
        geUnsafe_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1/100", false);

        geUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "0", true);
        geUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1", false);
        geUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "-1", true);
        geUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "100/3", false);
        geUnsafe_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), "1/100", false);

        geUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", false);
        geUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", true);
        geUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", false);
        geUnsafe_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", false);
    }

    private static void eq_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.eq(Rational.readStrict(b).get(), resolution), output);
    }

    private static void eq_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.eq(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEq_Rational_Rational() {
        eq_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[true]");
        eq_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[true]");
        eq_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        eq_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        eq_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        eq_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        eq_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        eq_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        eq_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", DEFAULT_RESOLUTION, "Optional[false]");
        eq_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        eq_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        eq_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        eq_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        eq_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void ne_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.ne(Rational.readStrict(b).get(), resolution), output);
    }

    private static void ne_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.ne(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNe_Rational_Rational() {
        ne_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[false]");
        ne_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[false]");
        ne_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        ne_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        ne_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        ne_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        ne_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        ne_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        ne_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ne_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        ne_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        ne_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        ne_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void lt_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.lt(Rational.readStrict(b).get(), resolution), output);
    }

    private static void lt_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.lt(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLt_Rational_Rational() {
        lt_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        lt_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        lt_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        lt_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        lt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        lt_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        lt_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        lt_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        lt_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        lt_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        lt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        lt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        lt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        lt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        lt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        lt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        lt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        lt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", DEFAULT_RESOLUTION, "Optional[false]");
        lt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        lt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        lt_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        lt_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        lt_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        lt_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void gt_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.gt(Rational.readStrict(b).get(), resolution), output);
    }

    private static void gt_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.gt(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testGt_Rational_Rational() {
        gt_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        gt_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        gt_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        gt_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        gt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        gt_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        gt_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        gt_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        gt_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        gt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        gt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        gt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        gt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[false]");
        gt_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", DEFAULT_RESOLUTION, "Optional[true]");
        gt_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        gt_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        gt_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        gt_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        gt_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void le_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.le(Rational.readStrict(b).get(), resolution), output);
    }

    private static void le_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.le(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLe_Rational_Rational() {
        le_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        le_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        le_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        le_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        le_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        le_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        le_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        le_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        le_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        le_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        le_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        le_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        le_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", DEFAULT_RESOLUTION, "Optional[false]");
        le_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "100/3", DEFAULT_RESOLUTION, "Optional[true]");
        le_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        le_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        le_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        le_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        le_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void ge_Rational_Rational_helper(
            @NotNull Real a,
            @NotNull String b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.ge(Rational.readStrict(b).get(), resolution), output);
    }

    private static void ge_Rational_Rational_fail_helper(
            @NotNull Real x,
            @NotNull String b,
            @NotNull Rational resolution
    ) {
        try {
            x.ge(Rational.readStrict(b).get(), resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testGe_Rational_Rational() {
        ge_Rational_Rational_helper(ZERO, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ZERO, "1", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(ZERO, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ZERO, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(ZERO, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        ge_Rational_Rational_helper(ONE, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ONE, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ONE, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ONE, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(ONE, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ge_Rational_Rational_helper(NEGATIVE_ONE, "0", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_ONE, "1", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_ONE, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(NEGATIVE_ONE, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_ONE, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        ge_Rational_Rational_helper(ONE_HALF, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ONE_HALF, "1", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(ONE_HALF, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(ONE_HALF, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(ONE_HALF, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ge_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "0", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "-1", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(NEGATIVE_FOUR_THIRDS, "1/100", DEFAULT_RESOLUTION, "Optional[false]");

        ge_Rational_Rational_helper(SQRT_TWO, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(SQRT_TWO, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(SQRT_TWO, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(SQRT_TWO, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(SQRT_TWO, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ge_Rational_Rational_helper(E, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(E, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(E, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(E, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(E, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ge_Rational_Rational_helper(PI, "0", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(PI, "1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(PI, "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(PI, "100/3", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(PI, "1/100", DEFAULT_RESOLUTION, "Optional[true]");

        ge_Rational_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        ge_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Rational_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        ge_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "0",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "-1",
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Rational_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        ge_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "0", DEFAULT_RESOLUTION, "Optional.empty");
        ge_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "1", DEFAULT_RESOLUTION, "Optional[false]");
        ge_Rational_Rational_helper(fuzzyRepresentation(Rational.ZERO), "-1", DEFAULT_RESOLUTION, "Optional[true]");
        ge_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "100/3",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Rational_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                "1/100",
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        ge_Rational_Rational_fail_helper(ZERO, "0", Rational.ZERO);
        ge_Rational_Rational_fail_helper(ZERO, "0", Rational.NEGATIVE_ONE);
        ge_Rational_Rational_fail_helper(E, "1/2", Rational.ZERO);
        ge_Rational_Rational_fail_helper(E, "1/2", Rational.NEGATIVE_ONE);
    }

    private static void compareToUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, @NotNull String output) {
        aeq(Ordering.fromInt(a.compareToUnsafe(b)), output);
    }

    @Test
    public void testCompareToUnsafe_Real() {
        compareToUnsafe_Real_helper(ZERO, ZERO, "=");
        compareToUnsafe_Real_helper(ZERO, ONE, "<");
        compareToUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(ZERO, SQRT_TWO, "<");
        compareToUnsafe_Real_helper(ZERO, E, "<");
        compareToUnsafe_Real_helper(ZERO, PI, "<");

        compareToUnsafe_Real_helper(ONE, ZERO, ">");
        compareToUnsafe_Real_helper(ONE, ONE, "=");
        compareToUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(ONE, SQRT_TWO, "<");
        compareToUnsafe_Real_helper(ONE, E, "<");
        compareToUnsafe_Real_helper(ONE, PI, "<");
        compareToUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), ">");

        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, "=");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), "<");
        compareToUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), "<");

        compareToUnsafe_Real_helper(SQRT_TWO, ZERO, ">");
        compareToUnsafe_Real_helper(SQRT_TWO, ONE, ">");
        compareToUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, "=");
        compareToUnsafe_Real_helper(SQRT_TWO, E, "<");
        compareToUnsafe_Real_helper(SQRT_TWO, PI, "<");
        compareToUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), ">");

        compareToUnsafe_Real_helper(E, ZERO, ">");
        compareToUnsafe_Real_helper(E, ONE, ">");
        compareToUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(E, SQRT_TWO, ">");
        compareToUnsafe_Real_helper(E, E, "=");
        compareToUnsafe_Real_helper(E, PI, "<");
        compareToUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), ">");

        compareToUnsafe_Real_helper(PI, ZERO, ">");
        compareToUnsafe_Real_helper(PI, ONE, ">");
        compareToUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(PI, SQRT_TWO, ">");
        compareToUnsafe_Real_helper(PI, E, ">");
        compareToUnsafe_Real_helper(PI, PI, "=");
        compareToUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), ">");
        compareToUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), ">");

        compareToUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, "<");
        compareToUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "<");
        compareToUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, "<");
        compareToUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, "<");

        compareToUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, "<");
        compareToUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, "<");
        compareToUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, "<");
        compareToUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, "<");

        compareToUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, "<");
        compareToUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, ">");
        compareToUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, "<");
        compareToUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, "<");
        compareToUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, "<");
    }

    private static void compareTo_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.compareTo(b, resolution).map(Ordering::fromInt), output);
    }

    private static void compareTo_Real_Rational_fail_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution
    ) {
        try {
            a.compareTo(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testCompareTo_Real_Rational() {
        compareTo_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                ZERO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                ZERO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        compareTo_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(
                ONE,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );

        compareTo_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Real_Rational_helper(
                SQRT_TWO,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );

        compareTo_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[=]");
        compareTo_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");
        compareTo_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[>]");

        compareTo_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                ZERO,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                ZERO,
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                ONE,
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        compareTo_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        compareTo_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[>]"
        );
        compareTo_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[<]"
        );
        compareTo_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[<]");
        compareTo_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        compareTo_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        compareTo_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        compareTo_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        compareTo_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        compareTo_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        compareTo_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static void eqUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, boolean output) {
        aeq(a.eqUnsafe(b), output);
    }

    @Test
    public void testEqUnsafe_Real() {
        eqUnsafe_Real_helper(ZERO, ZERO, true);
        eqUnsafe_Real_helper(ZERO, ONE, false);
        eqUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(ZERO, SQRT_TWO, false);
        eqUnsafe_Real_helper(ZERO, E, false);
        eqUnsafe_Real_helper(ZERO, PI, false);

        eqUnsafe_Real_helper(ONE, ZERO, false);
        eqUnsafe_Real_helper(ONE, ONE, true);
        eqUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(ONE, SQRT_TWO, false);
        eqUnsafe_Real_helper(ONE, E, false);
        eqUnsafe_Real_helper(ONE, PI, false);
        eqUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), false);

        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, true);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), false);

        eqUnsafe_Real_helper(SQRT_TWO, ZERO, false);
        eqUnsafe_Real_helper(SQRT_TWO, ONE, false);
        eqUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, true);
        eqUnsafe_Real_helper(SQRT_TWO, E, false);
        eqUnsafe_Real_helper(SQRT_TWO, PI, false);
        eqUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), false);

        eqUnsafe_Real_helper(E, ZERO, false);
        eqUnsafe_Real_helper(E, ONE, false);
        eqUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(E, SQRT_TWO, false);
        eqUnsafe_Real_helper(E, E, true);
        eqUnsafe_Real_helper(E, PI, false);
        eqUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), false);

        eqUnsafe_Real_helper(PI, ZERO, false);
        eqUnsafe_Real_helper(PI, ONE, false);
        eqUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(PI, SQRT_TWO, false);
        eqUnsafe_Real_helper(PI, E, false);
        eqUnsafe_Real_helper(PI, PI, true);
        eqUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), false);
        eqUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), false);

        eqUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, false);
        eqUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        eqUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, false);
        eqUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, false);

        eqUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, false);
        eqUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        eqUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, false);
        eqUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, false);

        eqUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, false);
        eqUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        eqUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        eqUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, false);
        eqUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, false);
    }

    private static void neUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, boolean output) {
        aeq(a.neUnsafe(b), output);
    }

    @Test
    public void testNeUnsafe_Real() {
        neUnsafe_Real_helper(ZERO, ZERO, false);
        neUnsafe_Real_helper(ZERO, ONE, true);
        neUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(ZERO, SQRT_TWO, true);
        neUnsafe_Real_helper(ZERO, E, true);
        neUnsafe_Real_helper(ZERO, PI, true);

        neUnsafe_Real_helper(ONE, ZERO, true);
        neUnsafe_Real_helper(ONE, ONE, false);
        neUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(ONE, SQRT_TWO, true);
        neUnsafe_Real_helper(ONE, E, true);
        neUnsafe_Real_helper(ONE, PI, true);
        neUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), true);

        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, false);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), true);

        neUnsafe_Real_helper(SQRT_TWO, ZERO, true);
        neUnsafe_Real_helper(SQRT_TWO, ONE, true);
        neUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, false);
        neUnsafe_Real_helper(SQRT_TWO, E, true);
        neUnsafe_Real_helper(SQRT_TWO, PI, true);
        neUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), true);

        neUnsafe_Real_helper(E, ZERO, true);
        neUnsafe_Real_helper(E, ONE, true);
        neUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(E, SQRT_TWO, true);
        neUnsafe_Real_helper(E, E, false);
        neUnsafe_Real_helper(E, PI, true);
        neUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), true);

        neUnsafe_Real_helper(PI, ZERO, true);
        neUnsafe_Real_helper(PI, ONE, true);
        neUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(PI, SQRT_TWO, true);
        neUnsafe_Real_helper(PI, E, true);
        neUnsafe_Real_helper(PI, PI, false);
        neUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), true);
        neUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), true);

        neUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, true);
        neUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        neUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, true);
        neUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, true);

        neUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, true);
        neUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        neUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, true);
        neUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, true);

        neUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, true);
        neUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        neUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        neUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, true);
        neUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, true);
    }

    private static void ltUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, boolean output) {
        aeq(a.ltUnsafe(b), output);
    }

    @Test
    public void testLtUnsafe_Real() {
        ltUnsafe_Real_helper(ZERO, ZERO, false);
        ltUnsafe_Real_helper(ZERO, ONE, true);
        ltUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(ZERO, SQRT_TWO, true);
        ltUnsafe_Real_helper(ZERO, E, true);
        ltUnsafe_Real_helper(ZERO, PI, true);

        ltUnsafe_Real_helper(ONE, ZERO, false);
        ltUnsafe_Real_helper(ONE, ONE, false);
        ltUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(ONE, SQRT_TWO, true);
        ltUnsafe_Real_helper(ONE, E, true);
        ltUnsafe_Real_helper(ONE, PI, true);
        ltUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), false);

        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), true);
        ltUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), true);

        ltUnsafe_Real_helper(SQRT_TWO, ZERO, false);
        ltUnsafe_Real_helper(SQRT_TWO, ONE, false);
        ltUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, false);
        ltUnsafe_Real_helper(SQRT_TWO, E, true);
        ltUnsafe_Real_helper(SQRT_TWO, PI, true);
        ltUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), false);

        ltUnsafe_Real_helper(E, ZERO, false);
        ltUnsafe_Real_helper(E, ONE, false);
        ltUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(E, SQRT_TWO, false);
        ltUnsafe_Real_helper(E, E, false);
        ltUnsafe_Real_helper(E, PI, true);
        ltUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), false);

        ltUnsafe_Real_helper(PI, ZERO, false);
        ltUnsafe_Real_helper(PI, ONE, false);
        ltUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(PI, SQRT_TWO, false);
        ltUnsafe_Real_helper(PI, E, false);
        ltUnsafe_Real_helper(PI, PI, false);
        ltUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), false);
        ltUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), false);

        ltUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, true);
        ltUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        ltUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, true);
        ltUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, true);

        ltUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, true);
        ltUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        ltUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, true);
        ltUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, true);

        ltUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, true);
        ltUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        ltUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        ltUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, true);
        ltUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, true);
    }

    private static void gtUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, boolean output) {
        aeq(a.gtUnsafe(b), output);
    }

    @Test
    public void testGtUnsafe_Real() {
        gtUnsafe_Real_helper(ZERO, ZERO, false);
        gtUnsafe_Real_helper(ZERO, ONE, false);
        gtUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(ZERO, SQRT_TWO, false);
        gtUnsafe_Real_helper(ZERO, E, false);
        gtUnsafe_Real_helper(ZERO, PI, false);

        gtUnsafe_Real_helper(ONE, ZERO, true);
        gtUnsafe_Real_helper(ONE, ONE, false);
        gtUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(ONE, SQRT_TWO, false);
        gtUnsafe_Real_helper(ONE, E, false);
        gtUnsafe_Real_helper(ONE, PI, false);
        gtUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), true);

        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), false);
        gtUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), false);

        gtUnsafe_Real_helper(SQRT_TWO, ZERO, true);
        gtUnsafe_Real_helper(SQRT_TWO, ONE, true);
        gtUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, false);
        gtUnsafe_Real_helper(SQRT_TWO, E, false);
        gtUnsafe_Real_helper(SQRT_TWO, PI, false);
        gtUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), true);

        gtUnsafe_Real_helper(E, ZERO, true);
        gtUnsafe_Real_helper(E, ONE, true);
        gtUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(E, SQRT_TWO, true);
        gtUnsafe_Real_helper(E, E, false);
        gtUnsafe_Real_helper(E, PI, false);
        gtUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), true);

        gtUnsafe_Real_helper(PI, ZERO, true);
        gtUnsafe_Real_helper(PI, ONE, true);
        gtUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(PI, SQRT_TWO, true);
        gtUnsafe_Real_helper(PI, E, true);
        gtUnsafe_Real_helper(PI, PI, false);
        gtUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), true);
        gtUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), true);

        gtUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, false);
        gtUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        gtUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, false);
        gtUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, false);

        gtUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, false);
        gtUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        gtUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, false);
        gtUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, false);

        gtUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, false);
        gtUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        gtUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        gtUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, false);
        gtUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, false);
        gtUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, false);
    }

    private static void leUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, boolean output) {
        aeq(a.leUnsafe(b), output);
    }

    @Test
    public void testLeUnsafe_Real() {
        leUnsafe_Real_helper(ZERO, ZERO, true);
        leUnsafe_Real_helper(ZERO, ONE, true);
        leUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(ZERO, SQRT_TWO, true);
        leUnsafe_Real_helper(ZERO, E, true);
        leUnsafe_Real_helper(ZERO, PI, true);

        leUnsafe_Real_helper(ONE, ZERO, false);
        leUnsafe_Real_helper(ONE, ONE, true);
        leUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(ONE, SQRT_TWO, true);
        leUnsafe_Real_helper(ONE, E, true);
        leUnsafe_Real_helper(ONE, PI, true);
        leUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), false);

        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), true);
        leUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), true);

        leUnsafe_Real_helper(SQRT_TWO, ZERO, false);
        leUnsafe_Real_helper(SQRT_TWO, ONE, false);
        leUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, true);
        leUnsafe_Real_helper(SQRT_TWO, E, true);
        leUnsafe_Real_helper(SQRT_TWO, PI, true);
        leUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), false);

        leUnsafe_Real_helper(E, ZERO, false);
        leUnsafe_Real_helper(E, ONE, false);
        leUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(E, SQRT_TWO, false);
        leUnsafe_Real_helper(E, E, true);
        leUnsafe_Real_helper(E, PI, true);
        leUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), false);

        leUnsafe_Real_helper(PI, ZERO, false);
        leUnsafe_Real_helper(PI, ONE, false);
        leUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(PI, SQRT_TWO, false);
        leUnsafe_Real_helper(PI, E, false);
        leUnsafe_Real_helper(PI, PI, true);
        leUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), false);
        leUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), false);

        leUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), rightFuzzyRepresentation(Rational.ZERO), true);
        leUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, true);
        leUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        leUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, true);
        leUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, true);

        leUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, true);
        leUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        leUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, true);
        leUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, true);

        leUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, true);
        leUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, false);
        leUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, true);
        leUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, true);
        leUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, true);
    }

    private static void geUnsafe_Real_helper(@NotNull Real a, @NotNull Real b, boolean output) {
        aeq(a.geUnsafe(b), output);
    }

    @Test
    public void testGeUnsafe_Real() {
        geUnsafe_Real_helper(ZERO, ZERO, true);
        geUnsafe_Real_helper(ZERO, ONE, false);
        geUnsafe_Real_helper(ZERO, NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(ZERO, SQRT_TWO, false);
        geUnsafe_Real_helper(ZERO, E, false);
        geUnsafe_Real_helper(ZERO, PI, false);

        geUnsafe_Real_helper(ONE, ZERO, true);
        geUnsafe_Real_helper(ONE, ONE, true);
        geUnsafe_Real_helper(ONE, NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(ONE, SQRT_TWO, false);
        geUnsafe_Real_helper(ONE, E, false);
        geUnsafe_Real_helper(ONE, PI, false);
        geUnsafe_Real_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(ONE, fuzzyRepresentation(Rational.ZERO), true);

        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ZERO, false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, ONE, false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, E, false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, PI, false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, leftFuzzyRepresentation(Rational.ZERO), false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, rightFuzzyRepresentation(Rational.ZERO), false);
        geUnsafe_Real_helper(NEGATIVE_FOUR_THIRDS, fuzzyRepresentation(Rational.ZERO), false);

        geUnsafe_Real_helper(SQRT_TWO, ZERO, true);
        geUnsafe_Real_helper(SQRT_TWO, ONE, true);
        geUnsafe_Real_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(SQRT_TWO, SQRT_TWO, true);
        geUnsafe_Real_helper(SQRT_TWO, E, false);
        geUnsafe_Real_helper(SQRT_TWO, PI, false);
        geUnsafe_Real_helper(SQRT_TWO, leftFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(SQRT_TWO, rightFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), true);

        geUnsafe_Real_helper(E, ZERO, true);
        geUnsafe_Real_helper(E, ONE, true);
        geUnsafe_Real_helper(E, NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(E, SQRT_TWO, true);
        geUnsafe_Real_helper(E, E, true);
        geUnsafe_Real_helper(E, PI, false);
        geUnsafe_Real_helper(E, leftFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(E, rightFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(E, fuzzyRepresentation(Rational.ZERO), true);

        geUnsafe_Real_helper(PI, ZERO, true);
        geUnsafe_Real_helper(PI, ONE, true);
        geUnsafe_Real_helper(PI, NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(PI, SQRT_TWO, true);
        geUnsafe_Real_helper(PI, E, true);
        geUnsafe_Real_helper(PI, PI, true);
        geUnsafe_Real_helper(PI, leftFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(PI, rightFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(PI, fuzzyRepresentation(Rational.ZERO), true);

        geUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, false);
        geUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        geUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), E, false);
        geUnsafe_Real_helper(leftFuzzyRepresentation(Rational.ZERO), PI, false);

        geUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), leftFuzzyRepresentation(Rational.ZERO), true);
        geUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, false);
        geUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        geUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), E, false);
        geUnsafe_Real_helper(rightFuzzyRepresentation(Rational.ZERO), PI, false);

        geUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), ONE, false);
        geUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), NEGATIVE_FOUR_THIRDS, true);
        geUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, false);
        geUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), E, false);
        geUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, false);
        geUnsafe_Real_helper(fuzzyRepresentation(Rational.ZERO), PI, false);
    }

    private static void eq_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.eq(b, resolution), output);
    }

    private static void eq_Real_Rational_fail_helper(@NotNull Real a, @NotNull Real b, @NotNull Rational resolution) {
        try {
            a.eq(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEq_Real_Rational() {
        eq_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        eq_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        eq_Real_Rational_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        eq_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        eq_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        eq_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        eq_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        eq_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        eq_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        eq_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        eq_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[true]");
        eq_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        eq_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[true]");
        eq_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        eq_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        eq_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        eq_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        eq_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        eq_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        eq_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        eq_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        eq_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        eq_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        eq_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        eq_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        eq_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        eq_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        eq_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static void ne_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.ne(b, resolution), output);
    }

    private static void ne_Real_Rational_fail_helper(@NotNull Real a, @NotNull Real b, @NotNull Rational resolution) {
        try {
            a.ne(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNe_Real_Rational() {
        ne_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        ne_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        ne_Real_Rational_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        ne_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        ne_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        ne_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ne_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        ne_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        ne_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        ne_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ne_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[false]");
        ne_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ne_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[false]");
        ne_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ne_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        ne_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        ne_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        ne_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        ne_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        ne_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ne_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        ne_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ne_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        ne_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        ne_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        ne_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        ne_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        ne_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static void lt_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.lt(b, resolution), output);
    }

    private static void lt_Real_Rational_fail_helper(@NotNull Real a, @NotNull Real b, @NotNull Rational resolution) {
        try {
            a.lt(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLt_Real_Rational() {
        lt_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        lt_Real_Rational_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        lt_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        lt_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        lt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        lt_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        lt_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        lt_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        lt_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        lt_Real_Rational_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        lt_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        lt_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        lt_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        lt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        lt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]");
        lt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[true]");
        lt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        lt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        lt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        lt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        lt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        lt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        lt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        lt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        lt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        lt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        lt_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        lt_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        lt_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        lt_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        lt_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static void gt_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.gt(b, resolution), output);
    }

    private static void gt_Real_Rational_fail_helper(@NotNull Real a, @NotNull Real b, @NotNull Rational resolution) {
        try {
            a.gt(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testGt_Real_Rational() {
        gt_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        gt_Real_Rational_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        gt_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        gt_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        gt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        gt_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Real_Rational_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        gt_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        gt_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        gt_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        gt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        gt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        gt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        gt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        gt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        gt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        gt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        gt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        gt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        gt_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        gt_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        gt_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        gt_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        gt_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        gt_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static void le_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.le(b, resolution), output);
    }

    private static void le_Real_Rational_fail_helper(@NotNull Real a, @NotNull Real b, @NotNull Rational resolution) {
        try {
            a.le(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLe_Real_Rational() {
        le_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        le_Real_Rational_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        le_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        le_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );

        le_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Real_Rational_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        le_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        le_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");
        le_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[false]");

        le_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        le_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        le_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        le_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        le_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        le_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        le_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        le_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        le_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        le_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[true]");
        le_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        le_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        le_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        le_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        le_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        le_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        le_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        le_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static void ge_Real_Rational_helper(
            @NotNull Real a,
            @NotNull Real b,
            @NotNull Rational resolution,
            @NotNull String output
    ) {
        aeq(a.ge(b, resolution), output);
    }

    private static void ge_Real_Rational_fail_helper(@NotNull Real a, @NotNull Real b, @NotNull Rational resolution) {
        try {
            a.ge(b, resolution);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testGe_Real_Rational() {
        ge_Real_Rational_helper(ZERO, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ZERO, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ZERO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ZERO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ZERO, E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ZERO, PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ZERO, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ZERO, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");
        ge_Real_Rational_helper(ZERO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional.empty");

        ge_Real_Rational_helper(ONE, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ONE, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ONE, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ONE, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ONE, E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ONE, PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(ONE, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ONE, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(ONE, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ge_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ZERO, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, ONE, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(NEGATIVE_FOUR_THIRDS, PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Real_Rational_helper(
                NEGATIVE_FOUR_THIRDS,
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );

        ge_Real_Rational_helper(SQRT_TWO, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(SQRT_TWO, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(SQRT_TWO, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(SQRT_TWO, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(SQRT_TWO, E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(SQRT_TWO, PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                SQRT_TWO,
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Real_Rational_helper(
                SQRT_TWO,
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Real_Rational_helper(SQRT_TWO, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ge_Real_Rational_helper(E, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, E, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(E, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(E, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ge_Real_Rational_helper(PI, ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, ONE, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, NEGATIVE_FOUR_THIRDS, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, SQRT_TWO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, E, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, PI, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, leftFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, rightFuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(PI, fuzzyRepresentation(Rational.ZERO), DEFAULT_RESOLUTION, "Optional[true]");

        ge_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        ge_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(leftFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ge_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ge_Real_Rational_helper(
                leftFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        ge_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional[true]");
        ge_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                SQRT_TWO,
                DEFAULT_RESOLUTION,
                "Optional[false]"
        );
        ge_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(rightFuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ge_Real_Rational_helper(
                rightFuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        ge_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ZERO, DEFAULT_RESOLUTION, "Optional.empty");
        ge_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), ONE, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                NEGATIVE_FOUR_THIRDS,
                DEFAULT_RESOLUTION,
                "Optional[true]"
        );
        ge_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), SQRT_TWO, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), E, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(fuzzyRepresentation(Rational.ZERO), PI, DEFAULT_RESOLUTION, "Optional[false]");
        ge_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                leftFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ge_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                rightFuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );
        ge_Real_Rational_helper(
                fuzzyRepresentation(Rational.ZERO),
                fuzzyRepresentation(Rational.ZERO),
                DEFAULT_RESOLUTION,
                "Optional.empty"
        );

        ge_Real_Rational_helper(PI, PI.negate().negate(), DEFAULT_RESOLUTION, "Optional.empty");

        ge_Real_Rational_fail_helper(ZERO, ZERO, Rational.ZERO);
        ge_Real_Rational_fail_helper(ZERO, ZERO, Rational.NEGATIVE_ONE);
        ge_Real_Rational_fail_helper(E, PI, Rational.ZERO);
        ge_Real_Rational_fail_helper(E, PI, Rational.NEGATIVE_ONE);
    }

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readListStrict(Readers::readBigIntegerStrict).apply(s).get();
    }

    private static @NotNull List<BigInteger> readBigIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readBigIntegerStrict).apply(s).get();
    }
}
