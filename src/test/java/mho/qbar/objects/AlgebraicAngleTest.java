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

    private static void fromTurns_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = fromTurns(Rational.readStrict(input).get());
        t.validate();
        aeq(t, output);
    }

    private static void fromTurns_fail_helper(@NotNull String input) {
        try {
            fromTurns(Rational.readStrict(input).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromTurns() {
        fromTurns_helper("0", "0");
        fromTurns_helper("1/2", "pi");
        fromTurns_helper("1/3", "2*pi/3");
        fromTurns_helper("2/3", "4*pi/3");
        fromTurns_helper("1/4", "pi/2");
        fromTurns_helper("3/4", "3*pi/2");
        fromTurns_helper("1/100", "pi/50");
        fromTurns_helper("10/33", "20*pi/33");

        fromTurns_fail_helper("1");
        fromTurns_fail_helper("2");
        fromTurns_fail_helper("-1");
    }

    private static void fromDegrees_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = fromDegrees(Rational.readStrict(input).get());
        t.validate();
        aeq(t, output);
    }

    private static void fromDegrees_fail_helper(@NotNull String input) {
        try {
            fromDegrees(Rational.readStrict(input).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromDegrees() {
        fromDegrees_helper("0", "0");
        fromDegrees_helper("180", "pi");
        fromDegrees_helper("120", "2*pi/3");
        fromDegrees_helper("240", "4*pi/3");
        fromDegrees_helper("90", "pi/2");
        fromDegrees_helper("270", "3*pi/2");
        fromDegrees_helper("1", "pi/180");
        fromDegrees_helper("359", "359*pi/180");
        fromDegrees_helper("1/3", "pi/540");

        fromDegrees_fail_helper("360");
        fromDegrees_fail_helper("720");
        fromDegrees_fail_helper("-1");
    }

    private static void isRationalMultipleOfPi_helper(@NotNull String input, boolean output) {
        aeq(AlgebraicAngle.readStrict(input).get().isRationalMultipleOfPi(), output);
    }

    @Test
    public void testIsRationalMultipleOfPi() {
        isRationalMultipleOfPi_helper("0", true);
        isRationalMultipleOfPi_helper("pi", true);
        isRationalMultipleOfPi_helper("pi/2", true);
        isRationalMultipleOfPi_helper("3*pi/2", true);
        isRationalMultipleOfPi_helper("2*pi/3", true);
        isRationalMultipleOfPi_helper("4*pi/3", true);
        isRationalMultipleOfPi_helper("arccos(1/3)", false);
        isRationalMultipleOfPi_helper("arccos(-1/3)", false);
        isRationalMultipleOfPi_helper("pi+arccos(-1/3)", false);
        isRationalMultipleOfPi_helper("pi+arccos(1/3)", false);
        isRationalMultipleOfPi_helper("arccos(sqrt(5)/3)", false);
        isRationalMultipleOfPi_helper("arccos(-sqrt(5)/3)", false);
        isRationalMultipleOfPi_helper("pi+arccos(-sqrt(5)/3)", false);
        isRationalMultipleOfPi_helper("pi+arccos(sqrt(5)/3)", false);
    }

    private static void rationalTurns_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().rationalTurns(), output);
    }

    @Test
    public void testRationalTurns() {
        rationalTurns_helper("0", "Optional[0]");
        rationalTurns_helper("pi", "Optional[1/2]");
        rationalTurns_helper("pi/2", "Optional[1/4]");
        rationalTurns_helper("3*pi/2", "Optional[3/4]");
        rationalTurns_helper("2*pi/3", "Optional[1/3]");
        rationalTurns_helper("4*pi/3", "Optional[2/3]");
        rationalTurns_helper("arccos(1/3)", "Optional.empty");
        rationalTurns_helper("arccos(-1/3)", "Optional.empty");
        rationalTurns_helper("pi+arccos(-1/3)", "Optional.empty");
        rationalTurns_helper("pi+arccos(1/3)", "Optional.empty");
        rationalTurns_helper("arccos(sqrt(5)/3)", "Optional.empty");
        rationalTurns_helper("arccos(-sqrt(5)/3)", "Optional.empty");
        rationalTurns_helper("pi+arccos(-sqrt(5)/3)", "Optional.empty");
        rationalTurns_helper("pi+arccos(sqrt(5)/3)", "Optional.empty");
    }

    private static void getQuadrant_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().getQuadrant(), output);
    }

    @Test
    public void testGetQuadrant() {
        getQuadrant_helper("0", 1);
        getQuadrant_helper("pi", 3);
        getQuadrant_helper("pi/2", 2);
        getQuadrant_helper("3*pi/2", 4);
        getQuadrant_helper("2*pi/3", 2);
        getQuadrant_helper("4*pi/3", 3);
        getQuadrant_helper("arccos(1/3)", 1);
        getQuadrant_helper("arccos(-1/3)", 2);
        getQuadrant_helper("pi+arccos(-1/3)", 4);
        getQuadrant_helper("pi+arccos(1/3)", 3);
        getQuadrant_helper("arccos(sqrt(5)/3)", 1);
        getQuadrant_helper("arccos(-sqrt(5)/3)", 2);
        getQuadrant_helper("pi+arccos(-sqrt(5)/3)", 4);
        getQuadrant_helper("pi+arccos(sqrt(5)/3)", 3);
    }

    private static void realTurns_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().realTurns(), output);
    }

    @Test
    public void testRealTurns() {
        realTurns_helper("0", "0");
        realTurns_helper("pi", "0.5");
        realTurns_helper("pi/2", "0.25");
        realTurns_helper("3*pi/2", "0.75");
        realTurns_helper("2*pi/3", "0.33333333333333333333...");
        realTurns_helper("4*pi/3", "0.66666666666666666666...");
        realTurns_helper("arccos(1/3)", "0.19591327601530363508...");
        realTurns_helper("arccos(-1/3)", "0.30408672398469636491...");
        realTurns_helper("pi+arccos(-1/3)", "0.80408672398469636491...");
        realTurns_helper("pi+arccos(1/3)", "0.69591327601530363508...");
        realTurns_helper("arccos(sqrt(5)/3)", "0.11613976359938499462...");
        realTurns_helper("arccos(-sqrt(5)/3)", "0.38386023640061500537...");
        realTurns_helper("pi+arccos(-sqrt(5)/3)", "0.88386023640061500537...");
        realTurns_helper("pi+arccos(sqrt(5)/3)", "0.61613976359938499462...");
    }

    private static void radians_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().radians(), output);
    }

    @Test
    public void testRadians() {
        radians_helper("0", "0");
        radians_helper("pi", "3.14159265358979323846...");
        radians_helper("pi/2", "1.57079632679489661923...");
        radians_helper("3*pi/2", "4.71238898038468985769...");
        radians_helper("2*pi/3", "2.09439510239319549230...");
        radians_helper("4*pi/3", "4.18879020478639098461...");
        radians_helper("arccos(1/3)", "1.23095941734077468213...");
        radians_helper("arccos(-1/3)", "1.91063323624901855632...");
        radians_helper("pi+arccos(-1/3)", "5.05222588983881179479...");
        radians_helper("pi+arccos(1/3)", "4.37255207093056792059...");
        radians_helper("arccos(sqrt(5)/3)", "0.72972765622696636345...");
        radians_helper("arccos(-sqrt(5)/3)", "2.41186499736282687500...");
        radians_helper("pi+arccos(-sqrt(5)/3)", "5.55345765095262011347...");
        radians_helper("pi+arccos(sqrt(5)/3)", "3.87132030981675960191...");
    }

    private static void degrees_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().degrees(), output);
    }

    @Test
    public void testDegrees() {
        degrees_helper("0", "0");
        degrees_helper("pi", "180");
        degrees_helper("pi/2", "90");
        degrees_helper("3*pi/2", "270");
        degrees_helper("2*pi/3", "120");
        degrees_helper("4*pi/3", "240");
        degrees_helper("pi/180", "1");
        degrees_helper("359*pi/180", "359");
        degrees_helper("arccos(1/3)", "70.52877936550930863075...");
        degrees_helper("arccos(-1/3)", "109.47122063449069136924...");
        degrees_helper("pi+arccos(-1/3)", "289.47122063449069136924...");
        degrees_helper("pi+arccos(1/3)", "250.52877936550930863075...");
        degrees_helper("arccos(sqrt(5)/3)", "41.81031489577859806585...");
        degrees_helper("arccos(-sqrt(5)/3)", "138.18968510422140193414...");
        degrees_helper("pi+arccos(-sqrt(5)/3)", "318.18968510422140193414...");
        degrees_helper("pi+arccos(sqrt(5)/3)", "221.81031489577859806585...");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = readStrict(input).get().negate();
        t.validate();
        aeq(t, output);
    }

    @Test
    public void testNegate() {
        negate_helper("0", "0");
        negate_helper("pi", "pi");
        negate_helper("pi/2", "3*pi/2");
        negate_helper("3*pi/2", "pi/2");
        negate_helper("2*pi/3", "4*pi/3");
        negate_helper("4*pi/3", "2*pi/3");
        negate_helper("pi/180", "359*pi/180");
        negate_helper("359*pi/180", "pi/180");
        negate_helper("arccos(1/3)", "pi+arccos(-1/3)");
        negate_helper("arccos(-1/3)", "pi+arccos(1/3)");
        negate_helper("pi+arccos(-1/3)", "arccos(1/3)");
        negate_helper("pi+arccos(1/3)", "arccos(-1/3)");
        negate_helper("arccos(sqrt(5)/3)", "pi+arccos(-sqrt(5)/3)");
        negate_helper("arccos(-sqrt(5)/3)", "pi+arccos(sqrt(5)/3)");
        negate_helper("pi+arccos(-sqrt(5)/3)", "arccos(sqrt(5)/3)");
        negate_helper("pi+arccos(sqrt(5)/3)", "arccos(-sqrt(5)/3)");
    }

    private static void supplement_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = readStrict(input).get().supplement();
        t.validate();
        aeq(t, output);
    }

    @Test
    public void testSupplement() {
        supplement_helper("0", "pi");
        supplement_helper("pi", "0");
        supplement_helper("pi/2", "pi/2");
        supplement_helper("3*pi/2", "3*pi/2");
        supplement_helper("2*pi/3", "pi/3");
        supplement_helper("4*pi/3", "5*pi/3");
        supplement_helper("pi/180", "179*pi/180");
        supplement_helper("359*pi/180", "181*pi/180");
        supplement_helper("arccos(1/3)", "arccos(-1/3)");
        supplement_helper("arccos(-1/3)", "arccos(1/3)");
        supplement_helper("pi+arccos(-1/3)", "pi+arccos(1/3)");
        supplement_helper("pi+arccos(1/3)", "pi+arccos(-1/3)");
        supplement_helper("arccos(sqrt(5)/3)", "arccos(-sqrt(5)/3)");
        supplement_helper("arccos(-sqrt(5)/3)", "arccos(sqrt(5)/3)");
        supplement_helper("pi+arccos(-sqrt(5)/3)", "pi+arccos(sqrt(5)/3)");
        supplement_helper("pi+arccos(sqrt(5)/3)", "pi+arccos(-sqrt(5)/3)");
    }

    private static void addPi_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = readStrict(input).get().addPi();
        t.validate();
        aeq(t, output);
    }

    @Test
    public void testAddPi() {
        addPi_helper("0", "pi");
        addPi_helper("pi", "0");
        addPi_helper("pi/2", "3*pi/2");
        addPi_helper("3*pi/2", "pi/2");
        addPi_helper("2*pi/3", "5*pi/3");
        addPi_helper("4*pi/3", "pi/3");
        addPi_helper("pi/180", "181*pi/180");
        addPi_helper("359*pi/180", "179*pi/180");
        addPi_helper("arccos(1/3)", "pi+arccos(1/3)");
        addPi_helper("arccos(-1/3)", "pi+arccos(-1/3)");
        addPi_helper("pi+arccos(-1/3)", "arccos(-1/3)");
        addPi_helper("pi+arccos(1/3)", "arccos(1/3)");
        addPi_helper("arccos(sqrt(5)/3)", "pi+arccos(sqrt(5)/3)");
        addPi_helper("arccos(-sqrt(5)/3)", "pi+arccos(-sqrt(5)/3)");
        addPi_helper("pi+arccos(-sqrt(5)/3)", "arccos(-sqrt(5)/3)");
        addPi_helper("pi+arccos(sqrt(5)/3)", "arccos(sqrt(5)/3)");
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
