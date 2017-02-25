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

    private static void sin_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().sin(), output);
    }

    @Test
    public void testSin() {
        sin_helper("0", "0");

        sin_helper("pi", "0");

        sin_helper("2*pi/3", "sqrt(3)/2");
        sin_helper("4*pi/3", "-sqrt(3)/2");

        sin_helper("pi/2", "1");
        sin_helper("3*pi/2", "-1");

        sin_helper("2*pi/5", "root 3 of 16*x^4-20*x^2+5");
        sin_helper("4*pi/5", "root 2 of 16*x^4-20*x^2+5");
        sin_helper("6*pi/5", "root 1 of 16*x^4-20*x^2+5");
        sin_helper("8*pi/5", "root 0 of 16*x^4-20*x^2+5");

        sin_helper("pi/3", "sqrt(3)/2");
        sin_helper("5*pi/3", "-sqrt(3)/2");

        sin_helper("2*pi/7", "root 4 of 64*x^6-112*x^4+56*x^2-7");
        sin_helper("4*pi/7", "root 5 of 64*x^6-112*x^4+56*x^2-7");
        sin_helper("6*pi/7", "root 3 of 64*x^6-112*x^4+56*x^2-7");
        sin_helper("8*pi/7", "root 2 of 64*x^6-112*x^4+56*x^2-7");
        sin_helper("10*pi/7", "root 0 of 64*x^6-112*x^4+56*x^2-7");
        sin_helper("12*pi/7", "root 1 of 64*x^6-112*x^4+56*x^2-7");

        sin_helper("pi/4", "sqrt(2)/2");
        sin_helper("3*pi/4", "sqrt(2)/2");
        sin_helper("5*pi/4", "-sqrt(2)/2");
        sin_helper("7*pi/4", "-sqrt(2)/2");

        sin_helper("2*pi/9", "root 4 of 64*x^6-96*x^4+36*x^2-3");
        sin_helper("4*pi/9", "root 5 of 64*x^6-96*x^4+36*x^2-3");
        sin_helper("8*pi/9", "root 3 of 64*x^6-96*x^4+36*x^2-3");
        sin_helper("10*pi/9", "root 2 of 64*x^6-96*x^4+36*x^2-3");
        sin_helper("14*pi/9", "root 0 of 64*x^6-96*x^4+36*x^2-3");
        sin_helper("16*pi/9", "root 1 of 64*x^6-96*x^4+36*x^2-3");

        sin_helper("pi/5", "root 2 of 16*x^4-20*x^2+5");
        sin_helper("3*pi/5", "root 3 of 16*x^4-20*x^2+5");
        sin_helper("7*pi/5", "root 0 of 16*x^4-20*x^2+5");
        sin_helper("9*pi/5", "root 1 of 16*x^4-20*x^2+5");

        sin_helper("2*pi/11", "root 6 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("4*pi/11", "root 8 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("6*pi/11", "root 9 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("8*pi/11", "root 7 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("10*pi/11", "root 5 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("12*pi/11", "root 4 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("14*pi/11", "root 2 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("16*pi/11", "root 0 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("18*pi/11", "root 1 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");
        sin_helper("20*pi/11", "root 3 of 1024*x^10-2816*x^8+2816*x^6-1232*x^4+220*x^2-11");

        sin_helper("pi/6", "1/2");
        sin_helper("5*pi/6", "1/2");
        sin_helper("7*pi/6", "-1/2");
        sin_helper("11*pi/6", "-1/2");

        sin_helper("2*pi/13", "root 7 of 4096*x^12-13312*x^10+16640*x^8-9984*x^6+2912*x^4-364*x^2+13");
        sin_helper("2*pi/17",
                "root 9 of 65536*x^16-278528*x^14+487424*x^12-452608*x^10+239360*x^8-71808*x^6+11424*x^4-816*x^2+17");
        sin_helper("2*pi/23",
                "root 12 of 4194304*x^22-24117248*x^20+60293120*x^18-85917696*x^16+76873728*x^14-44843008*x^12+" +
                "17145856*x^10-4209920*x^8+631488*x^6-52624*x^4+2024*x^2-23");
        sin_helper("pi/180",
                "root 24 of 281474976710656*x^48-3377699720527872*x^46+18999560927969280*x^44-" +
                "66568831992070144*x^42+162828875980603392*x^40-295364007592722432*x^38+411985976135516160*x^36-" +
                "452180272956309504*x^34+396366279591591936*x^32-280058255978266624*x^30+160303703377575936*x^28-" +
                "74448984852135936*x^26+28011510450094080*x^24-8500299631165440*x^22+2064791072931840*x^20-" +
                "397107008634880*x^18+59570604933120*x^16-6832518856704*x^14+583456329728*x^12-35782471680*x^10+" +
                "1497954816*x^8-39625728*x^6+579456*x^4-3456*x^2+1");

        sin_helper("arccos(1/3)", "2*sqrt(2)/3");
        sin_helper("arccos(-1/3)", "2*sqrt(2)/3");
        sin_helper("pi+arccos(-1/3)", "-2*sqrt(2)/3");
        sin_helper("pi+arccos(1/3)", "-2*sqrt(2)/3");
        sin_helper("arccos(sqrt(5)/3)", "2/3");
        sin_helper("arccos(-sqrt(5)/3)", "2/3");
        sin_helper("pi+arccos(-sqrt(5)/3)", "-2/3");
        sin_helper("pi+arccos(sqrt(5)/3)", "-2/3");
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

    private static void tan_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().tan(), output);
    }

    private static void tan_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().tan();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testTan() {
        tan_helper("0", "0");

        tan_helper("pi", "0");

        tan_helper("2*pi/3", "-sqrt(3)");
        tan_helper("4*pi/3", "sqrt(3)");

        tan_helper("2*pi/5", "root 3 of x^4-10*x^2+5");
        tan_helper("4*pi/5", "root 1 of x^4-10*x^2+5");
        tan_helper("6*pi/5", "root 2 of x^4-10*x^2+5");
        tan_helper("8*pi/5", "root 0 of x^4-10*x^2+5");

        tan_helper("pi/3", "sqrt(3)");
        tan_helper("5*pi/3", "-sqrt(3)");

        tan_helper("2*pi/7", "root 4 of x^6-21*x^4+35*x^2-7");
        tan_helper("4*pi/7", "root 0 of x^6-21*x^4+35*x^2-7");
        tan_helper("6*pi/7", "root 2 of x^6-21*x^4+35*x^2-7");
        tan_helper("8*pi/7", "root 3 of x^6-21*x^4+35*x^2-7");
        tan_helper("10*pi/7", "root 5 of x^6-21*x^4+35*x^2-7");
        tan_helper("12*pi/7", "root 1 of x^6-21*x^4+35*x^2-7");

        tan_helper("pi/4", "1");
        tan_helper("3*pi/4", "-1");
        tan_helper("5*pi/4", "1");
        tan_helper("7*pi/4", "-1");

        tan_helper("2*pi/9", "root 4 of x^6-33*x^4+27*x^2-3");
        tan_helper("4*pi/9", "root 5 of x^6-33*x^4+27*x^2-3");
        tan_helper("8*pi/9", "root 2 of x^6-33*x^4+27*x^2-3");
        tan_helper("10*pi/9", "root 3 of x^6-33*x^4+27*x^2-3");
        tan_helper("14*pi/9", "root 0 of x^6-33*x^4+27*x^2-3");
        tan_helper("16*pi/9", "root 1 of x^6-33*x^4+27*x^2-3");

        tan_helper("pi/5", "root 2 of x^4-10*x^2+5");
        tan_helper("3*pi/5", "root 0 of x^4-10*x^2+5");
        tan_helper("7*pi/5", "root 3 of x^4-10*x^2+5");
        tan_helper("9*pi/5", "root 1 of x^4-10*x^2+5");

        tan_helper("2*pi/11", "root 6 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("4*pi/11", "root 8 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("6*pi/11", "root 0 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("8*pi/11", "root 2 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("10*pi/11", "root 4 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("12*pi/11", "root 5 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("14*pi/11", "root 7 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("16*pi/11", "root 9 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("18*pi/11", "root 1 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");
        tan_helper("20*pi/11", "root 3 of x^10-55*x^8+330*x^6-462*x^4+165*x^2-11");

        tan_helper("pi/6", "sqrt(3)/3");
        tan_helper("5*pi/6", "-sqrt(3)/3");
        tan_helper("7*pi/6", "sqrt(3)/3");
        tan_helper("11*pi/6", "-sqrt(3)/3");

        tan_helper("2*pi/13", "root 7 of x^12-78*x^10+715*x^8-1716*x^6+1287*x^4-286*x^2+13");
        tan_helper("2*pi/17", "root 9 of x^16-136*x^14+2380*x^12-12376*x^10+24310*x^8-19448*x^6+6188*x^4-680*x^2+17");

        tan_helper("arccos(1/3)", "2*sqrt(2)");
        tan_helper("arccos(-1/3)", "-2*sqrt(2)");
        tan_helper("pi+arccos(-1/3)", "-2*sqrt(2)");
        tan_helper("pi+arccos(1/3)", "2*sqrt(2)");
        tan_helper("arccos(sqrt(5)/3)", "2*sqrt(5)/5");
        tan_helper("arccos(-sqrt(5)/3)", "-2*sqrt(5)/5");
        tan_helper("pi+arccos(-sqrt(5)/3)", "-2*sqrt(5)/5");
        tan_helper("pi+arccos(sqrt(5)/3)", "2*sqrt(5)/5");

        tan_fail_helper("pi/2");
        tan_fail_helper("3*pi/2");
    }

    private static void cot_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().cot(), output);
    }

    private static void cot_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().cot();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testCot() {
        cot_helper("2*pi/3", "-sqrt(3)/3");
        cot_helper("4*pi/3", "sqrt(3)/3");

        cot_helper("pi/2", "0");
        cot_helper("3*pi/2", "0");

        cot_helper("2*pi/5", "root 2 of 5*x^4-10*x^2+1");
        cot_helper("4*pi/5", "root 0 of 5*x^4-10*x^2+1");
        cot_helper("6*pi/5", "root 3 of 5*x^4-10*x^2+1");
        cot_helper("8*pi/5", "root 1 of 5*x^4-10*x^2+1");

        cot_helper("pi/3", "sqrt(3)/3");
        cot_helper("5*pi/3", "-sqrt(3)/3");

        cot_helper("2*pi/7", "root 4 of 7*x^6-35*x^4+21*x^2-1");
        cot_helper("4*pi/7", "root 2 of 7*x^6-35*x^4+21*x^2-1");
        cot_helper("6*pi/7", "root 0 of 7*x^6-35*x^4+21*x^2-1");
        cot_helper("8*pi/7", "root 5 of 7*x^6-35*x^4+21*x^2-1");
        cot_helper("10*pi/7", "root 3 of 7*x^6-35*x^4+21*x^2-1");
        cot_helper("12*pi/7", "root 1 of 7*x^6-35*x^4+21*x^2-1");

        cot_helper("pi/4", "1");
        cot_helper("3*pi/4", "-1");
        cot_helper("5*pi/4", "1");
        cot_helper("7*pi/4", "-1");

        cot_helper("2*pi/9", "root 4 of 3*x^6-27*x^4+33*x^2-1");
        cot_helper("4*pi/9", "root 3 of 3*x^6-27*x^4+33*x^2-1");
        cot_helper("8*pi/9", "root 0 of 3*x^6-27*x^4+33*x^2-1");
        cot_helper("10*pi/9", "root 5 of 3*x^6-27*x^4+33*x^2-1");
        cot_helper("14*pi/9", "root 2 of 3*x^6-27*x^4+33*x^2-1");
        cot_helper("16*pi/9", "root 1 of 3*x^6-27*x^4+33*x^2-1");

        cot_helper("pi/5", "root 3 of 5*x^4-10*x^2+1");
        cot_helper("3*pi/5", "root 1 of 5*x^4-10*x^2+1");
        cot_helper("7*pi/5", "root 2 of 5*x^4-10*x^2+1");
        cot_helper("9*pi/5", "root 0 of 5*x^4-10*x^2+1");

        cot_helper("2*pi/11", "root 8 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("4*pi/11", "root 6 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("6*pi/11", "root 4 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("8*pi/11", "root 2 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("10*pi/11", "root 0 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("12*pi/11", "root 9 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("14*pi/11", "root 7 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("16*pi/11", "root 5 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("18*pi/11", "root 3 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");
        cot_helper("20*pi/11", "root 1 of 11*x^10-165*x^8+462*x^6-330*x^4+55*x^2-1");

        cot_helper("pi/6", "sqrt(3)");
        cot_helper("5*pi/6", "-sqrt(3)");
        cot_helper("7*pi/6", "sqrt(3)");
        cot_helper("11*pi/6", "-sqrt(3)");

        cot_helper("2*pi/13", "root 10 of 13*x^12-286*x^10+1287*x^8-1716*x^6+715*x^4-78*x^2+1");
        cot_helper("2*pi/17",
                "root 14 of 17*x^16-680*x^14+6188*x^12-19448*x^10+24310*x^8-12376*x^6+2380*x^4-136*x^2+1");

        cot_helper("arccos(1/3)", "sqrt(2)/4");
        cot_helper("arccos(-1/3)", "-sqrt(2)/4");
        cot_helper("pi+arccos(-1/3)", "-sqrt(2)/4");
        cot_helper("pi+arccos(1/3)", "sqrt(2)/4");
        cot_helper("arccos(sqrt(5)/3)", "sqrt(5)/2");
        cot_helper("arccos(-sqrt(5)/3)", "-sqrt(5)/2");
        cot_helper("pi+arccos(-sqrt(5)/3)", "-sqrt(5)/2");
        cot_helper("pi+arccos(sqrt(5)/3)", "sqrt(5)/2");

        cot_fail_helper("0");
        cot_fail_helper("pi");
    }

    private static void sec_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().sec(), output);
    }

    private static void sec_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().sec();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSec() {
        sec_helper("0", "1");

        sec_helper("pi", "-1");

        sec_helper("2*pi/3", "-2");
        sec_helper("4*pi/3", "-2");

        sec_helper("2*pi/5", "1+sqrt(5)");
        sec_helper("4*pi/5", "1-sqrt(5)");
        sec_helper("6*pi/5", "1-sqrt(5)");
        sec_helper("8*pi/5", "1+sqrt(5)");

        sec_helper("pi/3", "2");
        sec_helper("5*pi/3", "2");

        sec_helper("2*pi/7", "root 2 of x^3+4*x^2-4*x-8");
        sec_helper("4*pi/7", "root 0 of x^3+4*x^2-4*x-8");
        sec_helper("6*pi/7", "root 1 of x^3+4*x^2-4*x-8");
        sec_helper("8*pi/7", "root 1 of x^3+4*x^2-4*x-8");
        sec_helper("10*pi/7", "root 0 of x^3+4*x^2-4*x-8");
        sec_helper("12*pi/7", "root 2 of x^3+4*x^2-4*x-8");

        sec_helper("pi/4", "sqrt(2)");
        sec_helper("3*pi/4", "-sqrt(2)");
        sec_helper("5*pi/4", "-sqrt(2)");
        sec_helper("7*pi/4", "sqrt(2)");

        sec_helper("2*pi/9", "root 1 of x^3-6*x^2+8");
        sec_helper("4*pi/9", "root 2 of x^3-6*x^2+8");
        sec_helper("8*pi/9", "root 0 of x^3-6*x^2+8");
        sec_helper("10*pi/9", "root 0 of x^3-6*x^2+8");
        sec_helper("14*pi/9", "root 2 of x^3-6*x^2+8");
        sec_helper("16*pi/9", "root 1 of x^3-6*x^2+8");

        sec_helper("pi/5", "-1+sqrt(5)");
        sec_helper("3*pi/5", "-1-sqrt(5)");
        sec_helper("7*pi/5", "-1-sqrt(5)");
        sec_helper("9*pi/5", "-1+sqrt(5)");

        sec_helper("2*pi/11", "root 3 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("4*pi/11", "root 4 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("6*pi/11", "root 0 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("8*pi/11", "root 1 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("10*pi/11", "root 2 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("12*pi/11", "root 2 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("14*pi/11", "root 1 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("16*pi/11", "root 0 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("18*pi/11", "root 4 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");
        sec_helper("20*pi/11", "root 3 of x^5+6*x^4-12*x^3-32*x^2+16*x+32");

        sec_helper("pi/6", "2*sqrt(3)/3");
        sec_helper("5*pi/6", "-2*sqrt(3)/3");
        sec_helper("7*pi/6", "-2*sqrt(3)/3");
        sec_helper("11*pi/6", "2*sqrt(3)/3");

        sec_helper("2*pi/13", "root 3 of x^6-6*x^5-24*x^4+32*x^3+80*x^2-32*x-64");
        sec_helper("2*pi/17", "root 4 of x^8-8*x^7-40*x^6+80*x^5+240*x^4-192*x^3-448*x^2+128*x+256");
        sec_helper("2*pi/23",
                "root 6 of x^11+12*x^10-60*x^9-280*x^8+560*x^7+1792*x^6-1792*x^5-4608*x^4+2304*x^3+5120*x^2-1024*x-" +
                "2048");
        sec_helper("pi/180",
                "root 24 of x^48-3456*x^46+579456*x^44-39625728*x^42+1497954816*x^40-35782471680*x^38+" +
                "583456329728*x^36-6832518856704*x^34+59570604933120*x^32-397107008634880*x^30+" +
                "2064791072931840*x^28-8500299631165440*x^26+28011510450094080*x^24-74448984852135936*x^22+" +
                "160303703377575936*x^20-280058255978266624*x^18+396366279591591936*x^16-452180272956309504*x^14+" +
                "411985976135516160*x^12-295364007592722432*x^10+162828875980603392*x^8-66568831992070144*x^6+" +
                "18999560927969280*x^4-3377699720527872*x^2+281474976710656");

        sec_helper("arccos(1/3)", "3");
        sec_helper("arccos(-1/3)", "-3");
        sec_helper("pi+arccos(-1/3)", "3");
        sec_helper("pi+arccos(1/3)", "-3");
        sec_helper("arccos(sqrt(5)/3)", "3*sqrt(5)/5");
        sec_helper("arccos(-sqrt(5)/3)", "-3*sqrt(5)/5");
        sec_helper("pi+arccos(-sqrt(5)/3)", "3*sqrt(5)/5");
        sec_helper("pi+arccos(sqrt(5)/3)", "-3*sqrt(5)/5");

        sec_fail_helper("pi/2");
        sec_fail_helper("3*pi/2");
    }

    private static void csc_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().csc(), output);
    }

    private static void csc_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().csc();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testCsc() {
        csc_helper("2*pi/3", "2*sqrt(3)/3");
        csc_helper("4*pi/3", "-2*sqrt(3)/3");

        csc_helper("pi/2", "1");
        csc_helper("3*pi/2", "-1");

        csc_helper("2*pi/5", "root 2 of 5*x^4-20*x^2+16");
        csc_helper("4*pi/5", "root 3 of 5*x^4-20*x^2+16");
        csc_helper("6*pi/5", "root 0 of 5*x^4-20*x^2+16");
        csc_helper("8*pi/5", "root 1 of 5*x^4-20*x^2+16");

        csc_helper("pi/3", "2*sqrt(3)/3");
        csc_helper("5*pi/3", "-2*sqrt(3)/3");

        csc_helper("2*pi/7", "root 4 of 7*x^6-56*x^4+112*x^2-64");
        csc_helper("4*pi/7", "root 3 of 7*x^6-56*x^4+112*x^2-64");
        csc_helper("6*pi/7", "root 5 of 7*x^6-56*x^4+112*x^2-64");
        csc_helper("8*pi/7", "root 0 of 7*x^6-56*x^4+112*x^2-64");
        csc_helper("10*pi/7", "root 2 of 7*x^6-56*x^4+112*x^2-64");
        csc_helper("12*pi/7", "root 1 of 7*x^6-56*x^4+112*x^2-64");

        csc_helper("pi/4", "sqrt(2)");
        csc_helper("3*pi/4", "sqrt(2)");
        csc_helper("5*pi/4", "-sqrt(2)");
        csc_helper("7*pi/4", "-sqrt(2)");

        csc_helper("2*pi/9", "root 4 of 3*x^6-36*x^4+96*x^2-64");
        csc_helper("4*pi/9", "root 3 of 3*x^6-36*x^4+96*x^2-64");
        csc_helper("8*pi/9", "root 5 of 3*x^6-36*x^4+96*x^2-64");
        csc_helper("10*pi/9", "root 0 of 3*x^6-36*x^4+96*x^2-64");
        csc_helper("14*pi/9", "root 2 of 3*x^6-36*x^4+96*x^2-64");
        csc_helper("16*pi/9", "root 1 of 3*x^6-36*x^4+96*x^2-64");

        csc_helper("pi/5", "root 3 of 5*x^4-20*x^2+16");
        csc_helper("3*pi/5", "root 2 of 5*x^4-20*x^2+16");
        csc_helper("7*pi/5", "root 1 of 5*x^4-20*x^2+16");
        csc_helper("9*pi/5", "root 0 of 5*x^4-20*x^2+16");

        csc_helper("2*pi/11", "root 8 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("4*pi/11", "root 6 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("6*pi/11", "root 5 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("8*pi/11", "root 7 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("10*pi/11", "root 9 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("12*pi/11", "root 0 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("14*pi/11", "root 2 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("16*pi/11", "root 4 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("18*pi/11", "root 3 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");
        csc_helper("20*pi/11", "root 1 of 11*x^10-220*x^8+1232*x^6-2816*x^4+2816*x^2-1024");

        csc_helper("pi/6", "2");
        csc_helper("5*pi/6", "2");
        csc_helper("7*pi/6", "-2");
        csc_helper("11*pi/6", "-2");

        csc_helper("2*pi/13", "root 10 of 13*x^12-364*x^10+2912*x^8-9984*x^6+16640*x^4-13312*x^2+4096");
        csc_helper("2*pi/17",
                "root 14 of 17*x^16-816*x^14+11424*x^12-71808*x^10+239360*x^8-452608*x^6+487424*x^4-278528*x^2+65536");
        csc_helper("2*pi/23",
                "root 20 of 23*x^22-2024*x^20+52624*x^18-631488*x^16+4209920*x^14-17145856*x^12+44843008*x^10-" +
                "76873728*x^8+85917696*x^6-60293120*x^4+24117248*x^2-4194304");
        csc_helper("pi/180",
                "root 47 of x^48-3456*x^46+579456*x^44-39625728*x^42+1497954816*x^40-35782471680*x^38+" +
                "583456329728*x^36-6832518856704*x^34+59570604933120*x^32-397107008634880*x^30+" +
                "2064791072931840*x^28-8500299631165440*x^26+28011510450094080*x^24-74448984852135936*x^22+" +
                "160303703377575936*x^20-280058255978266624*x^18+396366279591591936*x^16-452180272956309504*x^14+" +
                "411985976135516160*x^12-295364007592722432*x^10+162828875980603392*x^8-66568831992070144*x^6+" +
                "18999560927969280*x^4-3377699720527872*x^2+281474976710656");

        csc_helper("arccos(1/3)", "3*sqrt(2)/4");
        csc_helper("arccos(-1/3)", "3*sqrt(2)/4");
        csc_helper("pi+arccos(-1/3)", "-3*sqrt(2)/4");
        csc_helper("pi+arccos(1/3)", "-3*sqrt(2)/4");
        csc_helper("arccos(sqrt(5)/3)", "3/2");
        csc_helper("arccos(-sqrt(5)/3)", "3/2");
        csc_helper("pi+arccos(-sqrt(5)/3)", "-3/2");
        csc_helper("pi+arccos(sqrt(5)/3)", "-3/2");

        csc_fail_helper("0");
        csc_fail_helper("pi");
    }

    private static void complement_helper(@NotNull String input, @NotNull String output) {
        AlgebraicAngle t = readStrict(input).get().complement();
        t.validate();
        aeq(t, output);
    }

    @Test
    public void testComplement() {
        complement_helper("0", "pi/2");
        complement_helper("pi", "3*pi/2");
        complement_helper("pi/2", "0");
        complement_helper("3*pi/2", "pi");
        complement_helper("2*pi/3", "11*pi/6");
        complement_helper("4*pi/3", "7*pi/6");
        complement_helper("pi/180", "89*pi/180");
        complement_helper("359*pi/180", "91*pi/180");
        complement_helper("arccos(1/3)", "arccos(2*sqrt(2)/3)");
        complement_helper("arccos(-1/3)", "pi+arccos(-2*sqrt(2)/3)");
        complement_helper("pi+arccos(-1/3)", "arccos(-2*sqrt(2)/3)");
        complement_helper("pi+arccos(1/3)", "pi+arccos(2*sqrt(2)/3)");
        complement_helper("arccos(sqrt(5)/3)", "arccos(2/3)");
        complement_helper("arccos(-sqrt(5)/3)", "pi+arccos(-2/3)");
        complement_helper("pi+arccos(-sqrt(5)/3)", "arccos(-2/3)");
        complement_helper("pi+arccos(sqrt(5)/3)", "pi+arccos(2/3)");
    }

    private static void regularPolygonArea_helper(int input, @NotNull String output) {
        aeq(regularPolygonArea(input), output);
    }

    private static void regularPolygonArea_fail_helper(int input) {
        try {
            regularPolygonArea(input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRegularPolygonArea() {
        regularPolygonArea_helper(2, "0");
        regularPolygonArea_helper(3, "sqrt(3)/4");
        regularPolygonArea_helper(4, "1");
        regularPolygonArea_helper(5, "root 3 of 256*x^4-800*x^2+125");
        regularPolygonArea_helper(6, "3*sqrt(3)/2");
        regularPolygonArea_helper(7, "root 5 of 4096*x^6-62720*x^4+115248*x^2-16807");
        regularPolygonArea_helper(8, "2+2*sqrt(2)");
        regularPolygonArea_helper(9, "root 5 of 4096*x^6-186624*x^4+1154736*x^2-177147");
        regularPolygonArea_helper(10, "root 3 of 16*x^4-1000*x^2+3125");
        regularPolygonArea_helper(11,
                "root 9 of 1048576*x^10-118947840*x^8+2518720512*x^6-13605588480*x^4+17148710480*x^2-2357947691");
        regularPolygonArea_helper(12, "6+3*sqrt(3)");
        regularPolygonArea_helper(13,
                "root 11 of 16777216*x^12-3898605568*x^10+185305595904*x^8-2609720475648*x^6+11485488551680*x^4-" +
                "13234415217504*x^2+1792160394037");
        regularPolygonArea_helper(14, "root 5 of 64*x^6-16464*x^4+336140*x^2-823543");
        regularPolygonArea_helper(15, "root 7 of 65536*x^8-25804800*x^6+1736640000*x^4-16767000000*x^2+2562890625");

        regularPolygonArea_fail_helper(1);
        regularPolygonArea_fail_helper(0);
        regularPolygonArea_fail_helper(-1);
    }

    private static void antiprismVolume_helper(int input, @NotNull String output) {
        aeq(antiprismVolume(input), output);
    }

    private static void antiprismVolume_fail_helper(int input) {
        try {
            antiprismVolume(input);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testAntiprismVolume() {
        antiprismVolume_helper(2, "sqrt(2)/12");
        antiprismVolume_helper(3, "sqrt(2)/3");
        antiprismVolume_helper(4, "root 1 of 81*x^4-72*x^2-2");
        antiprismVolume_helper(5, "(5+2*sqrt(5))/6");
        antiprismVolume_helper(6, "root 1 of x^4-4*x^2-8");
        antiprismVolume_helper(7, "root 3 of 46656*x^6-508032*x^4+209916*x^2+2401");
        antiprismVolume_helper(8, "root 5 of 6561*x^8-46656*x^6-1410048*x^4+1511424*x^2-8192");
        antiprismVolume_helper(9, "root 3 of 64*x^6-1872*x^4-648*x^2+81");
        antiprismVolume_helper(10, "root 3 of 6561*x^8+36450*x^6-16503750*x^4+55968750*x^2+390625");
        antiprismVolume_helper(11,
                "root 7 of 60466176*x^10-4267904256*x^8+12978719424*x^6+56354965920*x^4-51212285388*x^2+214358881");
        antiprismVolume_helper(12, "root 5 of x^8+80*x^6-17088*x^4+26624*x^2-2048");
        antiprismVolume_helper(13,
                "root 7 of 2176782336*x^12-296344728576*x^10+815515713792*x^8+36967380607872*x^6-" +
                "102872428710480*x^4+26723338419960*x^2+137858491849");
        antiprismVolume_helper(14,
                "root 7 of 531441*x^12+144670050*x^10-47510930376*x^8+849599194626*x^6-1496571163605*x^4-" +
                "4037136258708*x^2+13841287201");
        antiprismVolume_helper(15, "root 3 of 256*x^8-52800*x^6-1810000*x^4+2062500*x^2+390625");

        antiprismVolume_fail_helper(1);
        antiprismVolume_fail_helper(0);
        antiprismVolume_fail_helper(-1);
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
