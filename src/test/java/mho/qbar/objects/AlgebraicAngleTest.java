package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.AlgebraicAngle.*;
import static mho.wheels.testing.Testing.aeq;
import static mho.wheels.testing.Testing.testCompareToHelper;
import static mho.wheels.testing.Testing.testEqualsHelper;
import static org.junit.Assert.fail;

public class AlgebraicAngleTest {
    private static void constant_helper(@NotNull AlgebraicAngle input, @NotNull String output) {
        input.validate();
        aeq(input, output);
    }

    @Test
    public void testConstants() {
        constant_helper(ZERO, "0");
        constant_helper(PI_OVER_FOUR, "pi/4");
        constant_helper(PI_OVER_TWO, "pi/2");
        constant_helper(THREE_PI_OVER_FOUR, "3*pi/4");
        constant_helper(PI, "pi");
        constant_helper(FIVE_PI_OVER_FOUR, "5*pi/4");
        constant_helper(THREE_PI_OVER_TWO, "3*pi/2");
        constant_helper(SEVEN_PI_OVER_FOUR, "7*pi/4");
    }

    private static void cos_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = readStrict(input).get();
        t.validate();
        aeq(t.cos(), output);
    }

    @Test
    public void testCos() {
        cos_helper("0", "1");

        cos_helper("pi", "-1");

        cos_helper("2*pi/3", "-1/2");
        cos_helper("4*pi/3", "-1/2");

        cos_helper("pi/2", "0");
        cos_helper("3*pi/2", "0");

        cos_helper("2*pi/5", "(-1+sqrt(5))/4");
        cos_helper("4*pi/5", "(-1-sqrt(5))/4");
        cos_helper("6*pi/5", "(-1-sqrt(5))/4");
        cos_helper("8*pi/5", "(-1+sqrt(5))/4");

        cos_helper("pi/3", "1/2");
        cos_helper("5*pi/3", "1/2");

        cos_helper("2*pi/7", "root 2 of 8*x^3+4*x^2-4*x-1");
        cos_helper("4*pi/7", "root 1 of 8*x^3+4*x^2-4*x-1");
        cos_helper("6*pi/7", "root 0 of 8*x^3+4*x^2-4*x-1");
        cos_helper("8*pi/7", "root 0 of 8*x^3+4*x^2-4*x-1");
        cos_helper("10*pi/7", "root 1 of 8*x^3+4*x^2-4*x-1");
        cos_helper("12*pi/7", "root 2 of 8*x^3+4*x^2-4*x-1");

        cos_helper("pi/4", "sqrt(2)/2");
        cos_helper("3*pi/4", "-sqrt(2)/2");
        cos_helper("5*pi/4", "-sqrt(2)/2");
        cos_helper("7*pi/4", "sqrt(2)/2");

        cos_helper("2*pi/9", "root 2 of 8*x^3-6*x+1");
        cos_helper("4*pi/9", "root 1 of 8*x^3-6*x+1");
        cos_helper("8*pi/9", "root 0 of 8*x^3-6*x+1");
        cos_helper("10*pi/9", "root 0 of 8*x^3-6*x+1");
        cos_helper("14*pi/9", "root 1 of 8*x^3-6*x+1");
        cos_helper("16*pi/9", "root 2 of 8*x^3-6*x+1");

        cos_helper("pi/5", "(1+sqrt(5))/4");
        cos_helper("3*pi/5", "(1-sqrt(5))/4");
        cos_helper("7*pi/5", "(1-sqrt(5))/4");
        cos_helper("9*pi/5", "(1+sqrt(5))/4");

        cos_helper("2*pi/11", "root 4 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("4*pi/11", "root 3 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("6*pi/11", "root 2 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("8*pi/11", "root 1 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("10*pi/11", "root 0 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("12*pi/11", "root 0 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("14*pi/11", "root 1 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("16*pi/11", "root 2 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("18*pi/11", "root 3 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");
        cos_helper("20*pi/11", "root 4 of 32*x^5+16*x^4-32*x^3-12*x^2+6*x+1");

        cos_helper("pi/6", "sqrt(3)/2");
        cos_helper("5*pi/6", "-sqrt(3)/2");
        cos_helper("7*pi/6", "-sqrt(3)/2");
        cos_helper("11*pi/6", "sqrt(3)/2");

        cos_helper("2*pi/13", "root 5 of 64*x^6+32*x^5-80*x^4-32*x^3+24*x^2+6*x-1");
        cos_helper("2*pi/17", "root 7 of 256*x^8+128*x^7-448*x^6-192*x^5+240*x^4+80*x^3-40*x^2-8*x+1");
        cos_helper("2*pi/23",
                "root 10 of 2048*x^11+1024*x^10-5120*x^9-2304*x^8+4608*x^7+1792*x^6-1792*x^5-560*x^4+280*x^3+60*x^2-" +
                "12*x-1");
        cos_helper("pi/180",
                "root 47 of 281474976710656*x^48-3377699720527872*x^46+18999560927969280*x^44-" +
                "66568831992070144*x^42+162828875980603392*x^40-295364007592722432*x^38+411985976135516160*x^36-" +
                "452180272956309504*x^34+396366279591591936*x^32-280058255978266624*x^30+160303703377575936*x^28-" +
                "74448984852135936*x^26+28011510450094080*x^24-8500299631165440*x^22+2064791072931840*x^20-" +
                "397107008634880*x^18+59570604933120*x^16-6832518856704*x^14+583456329728*x^12-35782471680*x^10+" +
                "1497954816*x^8-39625728*x^6+579456*x^4-3456*x^2+1");

        cos_helper("arccos(1/3)", "1/3");
        cos_helper("arccos(-1/3)", "-1/3");
        cos_helper("pi+arccos(-1/3)", "1/3");
        cos_helper("pi+arccos(1/3)", "-1/3");
        cos_helper("arccos(sqrt(5)/3)", "sqrt(5)/3");
        cos_helper("arccos(-sqrt(5)/3)", "-sqrt(5)/3");
        cos_helper("pi+arccos(-sqrt(5)/3)", "sqrt(5)/3");
        cos_helper("pi+arccos(sqrt(5)/3)", "-sqrt(5)/3");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readAlgebraicAngleList("[0, arccos(sqrt(5)/3), arccos(1/3), pi/2, arccos(-1/3), 2*pi/3," +
                        " arccos(-sqrt(5)/3), pi, pi+arccos(sqrt(5)/3), 4*pi/3, pi+arccos(1/3), 3*pi/2," +
                        " pi+arccos(-1/3), pi+arccos(-sqrt(5)/3)]"),
                readAlgebraicAngleList("[0, arccos(sqrt(5)/3), arccos(1/3), pi/2, arccos(-1/3), 2*pi/3," +
                        " arccos(-sqrt(5)/3), pi, pi+arccos(sqrt(5)/3), 4*pi/3, pi+arccos(1/3), 3*pi/2," +
                        " pi+arccos(-1/3), pi+arccos(-sqrt(5)/3)]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 32);
        hashCode_helper("pi", 64);
        hashCode_helper("pi/2", 66);
        hashCode_helper("3*pi/2", 128);
        hashCode_helper("2*pi/3", 65);
        hashCode_helper("4*pi/3", 96);
        hashCode_helper("arccos(1/3)", 96);
        hashCode_helper("arccos(-1/3)", 65);
        hashCode_helper("pi+arccos(-1/3)", 189);
        hashCode_helper("pi+arccos(1/3)", 96);
        hashCode_helper("arccos(sqrt(5)/3)", 25057);
        hashCode_helper("arccos(-sqrt(5)/3)", 25057);
        hashCode_helper("pi+arccos(-sqrt(5)/3)", 25150);
        hashCode_helper("pi+arccos(sqrt(5)/3)", 25088);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(readAlgebraicAngleList(
                "[0, arccos(sqrt(5)/3), arccos(1/3), pi/2, arccos(-1/3), 2*pi/3, arccos(-sqrt(5)/3), pi," +
                " pi+arccos(sqrt(5)/3), 4*pi/3, pi+arccos(1/3), 3*pi/2, pi+arccos(-1/3), pi+arccos(-sqrt(5)/3)]"));
    }

    private static void readStrict_String_helper(@NotNull String input, @NotNull String output) {
        Optional<AlgebraicAngle> ot = readStrict(input);
        ot.ifPresent(AlgebraicAngle::validate);
        aeq(ot, output);
    }

    @Test
    public void testReadStrict_String() {
        readStrict_String_helper("0", "Optional[0]");
        readStrict_String_helper("pi", "Optional[pi]");
        readStrict_String_helper("pi/2", "Optional[pi/2]");
        readStrict_String_helper("pi/3", "Optional[pi/3]");
        readStrict_String_helper("pi/100", "Optional[pi/100]");
        readStrict_String_helper("3*pi/100", "Optional[3*pi/100]");
        readStrict_String_helper("7*pi/4", "Optional[7*pi/4]");
        readStrict_String_helper("arccos(1/3)", "Optional[arccos(1/3)]");
        readStrict_String_helper("arccos(-1/3)", "Optional[arccos(-1/3)]");
        readStrict_String_helper("pi+arccos(-1/3)", "Optional[pi+arccos(-1/3)]");
        readStrict_String_helper("pi+arccos(1/3)", "Optional[pi+arccos(1/3)]");
        readStrict_String_helper("arccos(sqrt(5)/3)", "Optional[arccos(sqrt(5)/3)]");
        readStrict_String_helper("arccos(-sqrt(5)/3)", "Optional[arccos(-sqrt(5)/3)]");
        readStrict_String_helper("pi+arccos(-sqrt(5)/3)", "Optional[pi+arccos(-sqrt(5)/3)]");
        readStrict_String_helper("pi+arccos(sqrt(5)/3)", "Optional[pi+arccos(sqrt(5)/3)]");
        readStrict_String_helper("arccos(root 0 of 32*x^5-2*x-1)", "Optional[arccos(root 0 of 32*x^5-2*x-1)]");
        readStrict_String_helper("arccos(root 0 of 32*x^5-2*x+1)", "Optional[arccos(root 0 of 32*x^5-2*x+1)]");
        readStrict_String_helper("pi+arccos(root 0 of 32*x^5-2*x+1)", "Optional[pi+arccos(root 0 of 32*x^5-2*x+1)]");
        readStrict_String_helper("pi+arccos(root 0 of 32*x^5-2*x-1)", "Optional[pi+arccos(root 0 of 32*x^5-2*x-1)]");

        readStrict_String_helper("", "Optional.empty");
        readStrict_String_helper(" ", "Optional.empty");
        readStrict_String_helper("-", "Optional.empty");
        readStrict_String_helper("00", "Optional.empty");
        readStrict_String_helper("01", "Optional.empty");
        readStrict_String_helper("-0", "Optional.empty");
        readStrict_String_helper("+0", "Optional.empty");
        readStrict_String_helper("1", "Optional.empty");
        readStrict_String_helper("-1", "Optional.empty");
        readStrict_String_helper("-pi", "Optional.empty");
        readStrict_String_helper("0*pi", "Optional.empty");
        readStrict_String_helper("1*pi", "Optional.empty");
        readStrict_String_helper("-1*pi", "Optional.empty");
        readStrict_String_helper("2pi", "Optional.empty");
        readStrict_String_helper("2*pi", "Optional.empty");
        readStrict_String_helper("2*pi/2", "Optional.empty");
        readStrict_String_helper("2*pi/4", "Optional.empty");
        readStrict_String_helper("11*pi/4", "Optional.empty");
        readStrict_String_helper("arccos()", "Optional.empty");
        readStrict_String_helper("arccos(", "Optional.empty");
        readStrict_String_helper("arccos", "Optional.empty");
        readStrict_String_helper("arccos((1/3)", "Optional.empty");
        readStrict_String_helper("arcsin(sqrt(5)/3)", "Optional.empty");
        readStrict_String_helper("arccos(0)", "Optional.empty");
        readStrict_String_helper("arccos(1)", "Optional.empty");
        readStrict_String_helper("arccos(1/2)", "Optional.empty");
        readStrict_String_helper("arccos(sqrt(2)/2)", "Optional.empty");
        readStrict_String_helper("arccos(-sqrt(2)/2)", "Optional.empty");
        readStrict_String_helper("arccos(sqrt(3)/2)", "Optional.empty");
        readStrict_String_helper("arccos(-sqrt(3)/2)", "Optional.empty");
        readStrict_String_helper("arccos(2)", "Optional.empty");
        readStrict_String_helper("arccos(-2)", "Optional.empty");
        readStrict_String_helper("arccos(sqrt(2))", "Optional.empty");
        readStrict_String_helper("arccos(-sqrt(2))", "Optional.empty");
        readStrict_String_helper("arccos(sqrt(5)/3)+pi", "Optional.empty");
        readStrict_String_helper("arccos(sqrt(5)/3)-pi", "Optional.empty");
        readStrict_String_helper("pi+", "Optional.empty");
        readStrict_String_helper("pi-arccos(sqrt(5)/3)", "Optional.empty");
        readStrict_String_helper("pi/2+arccos(sqrt(5)/3)", "Optional.empty");
        readStrict_String_helper("pi/2+arcsin(sqrt(5)/3)", "Optional.empty");
        readStrict_String_helper("pi+arccos(sqrt(2)/2)", "Optional.empty");
        readStrict_String_helper("pi+arccos(-sqrt(2)/2)", "Optional.empty");
        readStrict_String_helper("pi+arccos(sqrt(3)/2)", "Optional.empty");
        readStrict_String_helper("pi+arccos(-sqrt(3)/2)", "Optional.empty");
        readStrict_String_helper("pi+arccos(2)", "Optional.empty");
        readStrict_String_helper("pi+arccos(-2)", "Optional.empty");
        readStrict_String_helper("pi+arccos(sqrt(2))", "Optional.empty");
        readStrict_String_helper("pi+arccos(-sqrt(2))", "Optional.empty");
    }

    private static void readStrict_int_String_helper(int maxDegree, @NotNull String input, @NotNull String output) {
        Optional<AlgebraicAngle> ot = readStrict(maxDegree, input);
        ot.ifPresent(AlgebraicAngle::validate);
        aeq(ot, output);
    }

    private static void readStrict_int_String_fail_helper(int maxDegree, @NotNull String input) {
        try {
            readStrict(maxDegree, input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testReadStrict_int_String() {
        readStrict_int_String_helper(2, "0", "Optional[0]");
        readStrict_int_String_helper(2, "pi", "Optional[pi]");
        readStrict_int_String_helper(2, "pi/2", "Optional[pi/2]");
        readStrict_int_String_helper(2, "pi/3", "Optional[pi/3]");
        readStrict_int_String_helper(2, "pi/100", "Optional[pi/100]");
        readStrict_int_String_helper(2, "3*pi/100", "Optional[3*pi/100]");
        readStrict_int_String_helper(2, "7*pi/4", "Optional[7*pi/4]");
        readStrict_int_String_helper(2, "arccos(1/3)", "Optional[arccos(1/3)]");
        readStrict_int_String_helper(2, "arccos(-1/3)", "Optional[arccos(-1/3)]");
        readStrict_int_String_helper(2, "pi+arccos(-1/3)", "Optional[pi+arccos(-1/3)]");
        readStrict_int_String_helper(2, "pi+arccos(1/3)", "Optional[pi+arccos(1/3)]");
        readStrict_int_String_helper(2, "arccos(sqrt(5)/3)", "Optional[arccos(sqrt(5)/3)]");
        readStrict_int_String_helper(2, "arccos(-sqrt(5)/3)", "Optional[arccos(-sqrt(5)/3)]");
        readStrict_int_String_helper(2, "pi+arccos(-sqrt(5)/3)", "Optional[pi+arccos(-sqrt(5)/3)]");
        readStrict_int_String_helper(2, "pi+arccos(sqrt(5)/3)", "Optional[pi+arccos(sqrt(5)/3)]");
        readStrict_int_String_helper(5, "arccos(root 0 of 32*x^5-2*x-1)", "Optional[arccos(root 0 of 32*x^5-2*x-1)]");
        readStrict_int_String_helper(5, "arccos(root 0 of 32*x^5-2*x+1)", "Optional[arccos(root 0 of 32*x^5-2*x+1)]");
        readStrict_int_String_helper(
                5,
                "pi+arccos(root 0 of 32*x^5-2*x+1)",
                "Optional[pi+arccos(root 0 of 32*x^5-2*x+1)]"
        );
        readStrict_int_String_helper(
                5,
                "pi+arccos(root 0 of 32*x^5-2*x-1)",
                "Optional[pi+arccos(root 0 of 32*x^5-2*x-1)]"
        );

        readStrict_int_String_helper(4, "arccos(root 0 of 32*x^5-2*x-1)", "Optional.empty");
        readStrict_int_String_helper(4, "arccos(root 0 of 32*x^5-2*x+1)", "Optional.empty");
        readStrict_int_String_helper(4, "pi+arccos(root 0 of 32*x^5-2*x+1)", "Optional.empty");
        readStrict_int_String_helper(4, "pi+arccos(root 0 of 32*x^5-2*x-1)", "Optional.empty");
        readStrict_int_String_helper(10, "", "Optional.empty");
        readStrict_int_String_helper(10, " ", "Optional.empty");
        readStrict_int_String_helper(10, "-", "Optional.empty");
        readStrict_int_String_helper(10, "00", "Optional.empty");
        readStrict_int_String_helper(10, "01", "Optional.empty");
        readStrict_int_String_helper(10, "-0", "Optional.empty");
        readStrict_int_String_helper(10, "+0", "Optional.empty");
        readStrict_int_String_helper(10, "1", "Optional.empty");
        readStrict_int_String_helper(10, "-1", "Optional.empty");
        readStrict_int_String_helper(10, "-pi", "Optional.empty");
        readStrict_int_String_helper(10, "0*pi", "Optional.empty");
        readStrict_int_String_helper(10, "1*pi", "Optional.empty");
        readStrict_int_String_helper(10, "-1*pi", "Optional.empty");
        readStrict_int_String_helper(10, "2pi", "Optional.empty");
        readStrict_int_String_helper(10, "2*pi", "Optional.empty");
        readStrict_int_String_helper(10, "2*pi/2", "Optional.empty");
        readStrict_int_String_helper(10, "2*pi/4", "Optional.empty");
        readStrict_int_String_helper(10, "11*pi/4", "Optional.empty");
        readStrict_int_String_helper(10, "arccos()", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(", "Optional.empty");
        readStrict_int_String_helper(10, "arccos", "Optional.empty");
        readStrict_int_String_helper(10, "arccos((1/3)", "Optional.empty");
        readStrict_int_String_helper(10, "arcsin(sqrt(5)/3)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(0)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(1)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(1/2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(sqrt(2)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(-sqrt(2)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(sqrt(3)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(-sqrt(3)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(-2)", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(sqrt(2))", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(-sqrt(2))", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(sqrt(5)/3)+pi", "Optional.empty");
        readStrict_int_String_helper(10, "arccos(sqrt(5)/3)-pi", "Optional.empty");
        readStrict_int_String_helper(10, "pi+", "Optional.empty");
        readStrict_int_String_helper(10, "pi-arccos(sqrt(5)/3)", "Optional.empty");
        readStrict_int_String_helper(10, "pi/2+arccos(sqrt(5)/3)", "Optional.empty");
        readStrict_int_String_helper(10, "pi/2+arcsin(sqrt(5)/3)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(sqrt(2)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(-sqrt(2)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(sqrt(3)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(-sqrt(3)/2)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(2)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(-2)", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(sqrt(2))", "Optional.empty");
        readStrict_int_String_helper(10, "pi+arccos(-sqrt(2))", "Optional.empty");

        readStrict_int_String_fail_helper(1, "pi");
        readStrict_int_String_fail_helper(0, "pi");
        readStrict_int_String_fail_helper(-1, "pi");
    }

    private static @NotNull List<AlgebraicAngle> readAlgebraicAngleList(@NotNull String s) {
        return Readers.readListStrict(AlgebraicAngle::readStrict).apply(s).get();
    }
}
