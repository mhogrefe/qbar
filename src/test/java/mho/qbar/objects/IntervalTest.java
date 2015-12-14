package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Interval.*;
import static mho.qbar.objects.Interval.sum;
import static mho.wheels.iterables.IterableUtils.iterate;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class IntervalTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "[0, 0]");
        aeq(ONE, "[1, 1]");
        aeq(ALL, "(-Infinity, Infinity)");
    }

    @Test
    public void testGetLower() {
        aeq(ZERO.getLower().get(), "0");
        aeq(ONE.getLower().get(), "1");
        aeq(read("[-2, 5/3]").get().getLower().get(), "-2");
        assertFalse(ALL.getLower().isPresent());
    }

    @Test
    public void testGetUpper() {
        aeq(ZERO.getUpper().get(), "0");
        aeq(ONE.getUpper().get(), "1");
        aeq(read("[-2, 5/3]").get().getUpper().get(), "5/3");
        assertFalse(ALL.getUpper().isPresent());
    }

    @Test
    public void testOf_Rational_Rational() {
        aeq(of(Rational.read("1/3").get(), Rational.read("1/2").get()), "[1/3, 1/2]");
        aeq(of(Rational.read("-5").get(), Rational.ZERO), "[-5, 0]");
        aeq(of(Rational.read("2").get(), Rational.read("2").get()), "[2, 2]");
        try {
            of(Rational.read("3").get(), Rational.read("2").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLessThanOrEqualTo() {
        aeq(lessThanOrEqualTo(Rational.read("1/3").get()), "(-Infinity, 1/3]");
        aeq(lessThanOrEqualTo(Rational.ZERO), "(-Infinity, 0]");
    }

    @Test
    public void testGreaterThanOrEqualTo() {
        aeq(greaterThanOrEqualTo(Rational.read("1/3").get()), "[1/3, Infinity)");
        aeq(greaterThanOrEqualTo(Rational.ZERO), "[0, Infinity)");
    }

    @Test
    public void testOf_Rational() {
        aeq(of(Rational.ZERO), "[0, 0]");
        aeq(of(Rational.read("5/4").get()), "[5/4, 5/4]");
        aeq(of(Rational.read("-2").get()), "[-2, -2]");
    }

    @Test
    public void testIsFinitelyBounded() {
        assertTrue(ZERO.isFinitelyBounded());
        assertTrue(ONE.isFinitelyBounded());
        assertFalse(ALL.isFinitelyBounded());
        assertTrue(read("[-2, 5/3]").get().isFinitelyBounded());
        assertTrue(read("[4, 4]").get().isFinitelyBounded());
        assertFalse(read("(-Infinity, 3/2]").get().isFinitelyBounded());
        assertFalse(read("[-6, Infinity)").get().isFinitelyBounded());
    }

    @Test
    public void testContains_Rational() {
        assertTrue(ZERO.contains(Rational.ZERO));
        assertFalse(ZERO.contains(Rational.ONE));
        assertTrue(ONE.contains(Rational.ONE));
        assertFalse(ONE.contains(Rational.ZERO));
        assertTrue(ALL.contains(Rational.ZERO));
        assertTrue(ALL.contains(Rational.ONE));
        assertTrue(ALL.contains(Rational.read("-4/3").get()));
        assertTrue(read("[-2, 5/3]").get().contains(Rational.read("-2").get()));
        assertTrue(read("[-2, 5/3]").get().contains(Rational.read("-1").get()));
        assertTrue(read("[-2, 5/3]").get().contains(Rational.ZERO));
        assertTrue(read("[-2, 5/3]").get().contains(Rational.ONE));
        assertTrue(read("[-2, 5/3]").get().contains(Rational.read("5/3").get()));
        assertFalse(read("[-2, 5/3]").get().contains(Rational.read("-3").get()));
        assertFalse(read("[-2, 5/3]").get().contains(Rational.read("2").get()));
        assertTrue(read("[4, 4]").get().contains(Rational.read("4").get()));
        assertFalse(read("[4, 4]").get().contains(Rational.read("3").get()));
        assertFalse(read("[4, 4]").get().contains(Rational.read("5").get()));
        assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.ZERO));
        assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.ONE));
        assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.read("-10").get()));
        assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.read("3/2").get()));
        assertFalse(read("(-Infinity, 3/2]").get().contains(Rational.read("2").get()));
        assertTrue(read("[-6, Infinity)").get().contains(Rational.ZERO));
        assertTrue(read("[-6, Infinity)").get().contains(Rational.ONE));
        assertTrue(read("[-6, Infinity)").get().contains(Rational.read("-4").get()));
        assertTrue(read("[-6, Infinity)").get().contains(Rational.read("5").get()));
        assertFalse(read("[-6, Infinity)").get().contains(Rational.read("-8").get()));
    }

    @Test
    public void testContains_Interval() {
        assertTrue(ZERO.contains(ZERO));
        assertTrue(ONE.contains(ONE));
        assertTrue(ALL.contains(ALL));
        assertTrue(ALL.contains(ZERO));
        assertTrue(ALL.contains(ONE));
        assertFalse(ZERO.contains(ONE));
        assertFalse(ZERO.contains(ALL));
        assertFalse(ONE.contains(ZERO));
        assertFalse(ONE.contains(ALL));
        assertTrue(read("[1, 4]").get().contains(read("[2, 3]").get()));
        assertTrue(read("[1, 4]").get().contains(read("[1, 4]").get()));
        assertFalse(read("[1, 4]").get().contains(read("[0, 2]").get()));
        assertTrue(read("(-Infinity, 1/2]").get().contains(read("(-Infinity, 0]").get()));
        assertTrue(read("(-Infinity, 1/2]").get().contains(read("[0, 0]").get()));
        assertFalse(read("(-Infinity, 1/2]").get().contains(read("(-Infinity, 1]").get()));
        assertTrue(read("[1/2, Infinity)").get().contains(read("[1, Infinity)").get()));
        assertTrue(read("[1/2, Infinity)").get().contains(read("[1, 1]").get()));
        assertFalse(read("[1/2, Infinity)").get().contains(read("[0, Infinity)").get()));
        assertFalse(read("[1/2, Infinity)").get().contains(read("(-Infinity, 1/2]").get()));
    }

    @Test
    public void testDiameter() {
        aeq(ZERO.diameter().get(), "0");
        aeq(ONE.diameter().get(), "0");
        assertFalse(ALL.diameter().isPresent());
        aeq(read("[-2, 5/3]").get().diameter().get(), "11/3");
        aeq(read("[4, 4]").get().diameter().get(), "0");
        assertFalse(read("(-Infinity, 3/2]").get().diameter().isPresent());
        assertFalse(read("[-6, Infinity)").get().diameter().isPresent());
    }

    @Test
    public void testConvexHull_Interval() {
        aeq(ZERO.convexHull(ZERO), "[0, 0]");
        aeq(ZERO.convexHull(ONE), "[0, 1]");
        aeq(ZERO.convexHull(ALL), "(-Infinity, Infinity)");
        aeq(ZERO.convexHull(read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(ZERO.convexHull(read("[4, 4]").get()), "[0, 4]");
        aeq(ZERO.convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(ZERO.convexHull(read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(ONE.convexHull(ZERO), "[0, 1]");
        aeq(ONE.convexHull(ONE), "[1, 1]");
        aeq(ONE.convexHull(ALL), "(-Infinity, Infinity)");
        aeq(ONE.convexHull(read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(ONE.convexHull(read("[4, 4]").get()), "[1, 4]");
        aeq(ONE.convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(ONE.convexHull(read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(ALL.convexHull(ZERO), "(-Infinity, Infinity)");
        aeq(ALL.convexHull(ONE), "(-Infinity, Infinity)");
        aeq(ALL.convexHull(ALL), "(-Infinity, Infinity)");
        aeq(ALL.convexHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ALL.convexHull(read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(ALL.convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ALL.convexHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().convexHull(ZERO), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().convexHull(ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().convexHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().convexHull(read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().convexHull(read("[4, 4]").get()), "[-2, 4]");
        aeq(read("[-2, 5/3]").get().convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, 5/3]");
        aeq(read("[-2, 5/3]").get().convexHull(read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(read("[4, 4]").get().convexHull(ZERO), "[0, 4]");
        aeq(read("[4, 4]").get().convexHull(ONE), "[1, 4]");
        aeq(read("[4, 4]").get().convexHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().convexHull(read("[-2, 5/3]").get()), "[-2, 4]");
        aeq(read("[4, 4]").get().convexHull(read("[4, 4]").get()), "[4, 4]");
        aeq(read("[4, 4]").get().convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, 4]");
        aeq(read("[4, 4]").get().convexHull(read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().convexHull(ZERO), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().convexHull(ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().convexHull(ALL), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().convexHull(read("[-2, 5/3]").get()), "(-Infinity, 5/3]");
        aeq(read("(-Infinity, 3/2]").get().convexHull(read("[4, 4]").get()), "(-Infinity, 4]");
        aeq(read("(-Infinity, 3/2]").get().convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().convexHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(ZERO), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(read("[-2, 5/3]").get()), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(read("[4, 4]").get()), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().convexHull(read("[-6, Infinity)").get()), "[-6, Infinity)");
    }

    @Test
    public void testConvexHull_List_Interval() {
        aeq(convexHull(readIntervalList("[[0, 0]]")), "[0, 0]");
        aeq(convexHull(readIntervalList("[[-1, 2]]")), "[-1, 2]");
        aeq(convexHull(readIntervalList("[[-1, Infinity)]")), "[-1, Infinity)");
        aeq(convexHull(readIntervalList("[(-Infinity, 4]]")), "(-Infinity, 4]");
        aeq(convexHull(readIntervalList("[(-Infinity, Infinity)]")), "(-Infinity, Infinity)");
        aeq(convexHull(readIntervalList("[[0, 0], [1, 1]]")), "[0, 1]");
        aeq(convexHull(readIntervalList("[[1, 2], [3, 4]]")), "[1, 4]");
        aeq(convexHull(readIntervalList("[[1, 3], [2, 4]]")), "[1, 4]");
        aeq(convexHull(readIntervalList("[(-Infinity, Infinity), [3, 4]]")), "(-Infinity, Infinity)");
        aeq(convexHull(readIntervalList("[[-1, Infinity), (-Infinity, 4]]")), "(-Infinity, Infinity)");
        aeq(convexHull(readIntervalList("[[1, 2], [3, 4], [5, 6]]")), "[1, 6]");
        aeq(convexHull(readIntervalList("[[1, 2], [2, 2], [3, Infinity)]")), "[1, Infinity)");
        try {
            convexHull(readIntervalList("[]"));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            convexHull(readIntervalListWithNulls("[[1, 2], null]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testIntersection() {
        aeq(ZERO.intersection(ZERO).get(), "[0, 0]");
        aeq(ONE.intersection(ONE).get(), "[1, 1]");
        aeq(ALL.intersection(ALL).get(), "(-Infinity, Infinity)");
        aeq(ALL.intersection(ZERO).get(), "[0, 0]");
        aeq(ALL.intersection(ONE).get(), "[1, 1]");
        assertFalse(ZERO.intersection(ONE).isPresent());
        aeq(read("[1, 3]").get().intersection(read("[2, 4]").get()).get(), "[2, 3]");
        aeq(read("[1, 2]").get().intersection(read("[2, 4]").get()).get(), "[2, 2]");
        assertFalse(read("[1, 2]").get().intersection(read("[3, 4]").get()).isPresent());
        aeq(ALL.intersection(read("[1, 2]").get()).get(), "[1, 2]");
        aeq(read("(-Infinity, 2]").get().intersection(read("[1, 3]").get()).get(), "[1, 2]");
        aeq(read("(-Infinity, 2]").get().intersection(read("(-Infinity, 3]").get()).get(), "(-Infinity, 2]");
        aeq(read("[2, Infinity)").get().intersection(read("[1, 3]").get()).get(), "[2, 3]");
        aeq(read("[2, Infinity)").get().intersection(read("[3, Infinity)").get()).get(), "[3, Infinity)");
        aeq(read("[2, Infinity)").get().intersection(read("(-Infinity, 3]").get()).get(), "[2, 3]");
        assertFalse(read("[2, Infinity)").get().intersection(read("(-Infinity, 1]").get()).isPresent());
    }

    @Test
    public void testDisjoint() {
        assertFalse(ZERO.disjoint(ZERO));
        assertFalse(ONE.disjoint(ONE));
        assertFalse(ALL.disjoint(ALL));
        assertFalse(ALL.disjoint(ZERO));
        assertFalse(ALL.disjoint(ONE));
        assertTrue(ZERO.disjoint(ONE));
        assertFalse(read("[1, 3]").get().disjoint(read("[2, 4]").get()));
        assertTrue(read("[1, 2]").get().disjoint(read("[3, 4]").get()));
        assertFalse(read("(-Infinity, 2]").get().disjoint(read("[1, 3]").get()));
        assertTrue(read("(-Infinity, 2]").get().disjoint(read("[3, 4]").get()));
        assertFalse(read("[2, Infinity)").get().disjoint(read("[1, 3]").get()));
        assertTrue(read("[2, Infinity)").get().disjoint(read("[0, 1]").get()));
        assertTrue(read("[2, Infinity)").get().disjoint(read("(-Infinity, 1]").get()));
        assertFalse(read("[2, Infinity)").get().disjoint(read("(-Infinity, 3]").get()));
    }

    @Test
    public void testMakeDisjoint() {
        aeq(makeDisjoint(readIntervalList("[]")), "[]");
        aeq(makeDisjoint(readIntervalList("[[0, 0]]")), "[[0, 0]]");
        aeq(makeDisjoint(readIntervalList("[[1, 1]]")), "[[1, 1]]");
        aeq(makeDisjoint(readIntervalList("[(-Infinity, Infinity)]")), "[(-Infinity, Infinity)]");
        aeq(makeDisjoint(readIntervalList("[(-Infinity, 3]]")), "[(-Infinity, 3]]");
        aeq(makeDisjoint(readIntervalList("[[2, Infinity)]")), "[[2, Infinity)]");
        aeq(makeDisjoint(readIntervalList("[[2, 3]]")), "[[2, 3]]");
        aeq(
                makeDisjoint(readIntervalList("[(-Infinity, 3], [4, 5], [6, 7]]")),
                "[(-Infinity, 3], [4, 5], [6, 7]]"
        );
        aeq(makeDisjoint(readIntervalList("[(-Infinity, 3], [3, 5], [5, 7]]")), "[(-Infinity, 7]]");
        aeq(makeDisjoint(readIntervalList("[(-Infinity, 3], [2, 5], [6, 7]]")), "[(-Infinity, 5], [6, 7]]");
        aeq(
                makeDisjoint(readIntervalList("[[1, 2], [4, 6], [10, Infinity), [3, 7], [5, 9]]")),
                "[[1, 2], [3, 9], [10, Infinity)]"
        );
        try {
            makeDisjoint(readIntervalListWithNulls("[[1, 3], null, [5, Infinity)]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testComplement() {
        aeq(ZERO.complement(), "[(-Infinity, Infinity)]");
        aeq(ONE.complement(), "[(-Infinity, Infinity)]");
        aeq(ALL.complement(), "[]");
        aeq(read("[-2, 5/3]").get().complement(), "[(-Infinity, -2], [5/3, Infinity)]");
        aeq(read("[4, 4]").get().complement(), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().complement(), "[[3/2, Infinity)]");
        aeq(read("[-6, Infinity)").get().complement(), "[(-Infinity, -6]]");
    }

    @Test
    public void testMidpoint() {
        aeq(ZERO.midpoint(), "0");
        aeq(ONE.midpoint(), "1");
        aeq(read("[4, 4]").get().midpoint(), "4");
        aeq(read("[1, 2]").get().midpoint(), "3/2");
        aeq(read("[-2, 5/3]").get().midpoint(), "-1/6");
        try {
            ALL.midpoint();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("(-Infinity, 1]").get().midpoint();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1, Infinity)").get().midpoint();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSplit() {
        aeq(ZERO.split(Rational.ZERO), "([0, 0], [0, 0])");
        aeq(ONE.split(Rational.ONE), "([1, 1], [1, 1])");
        aeq(ALL.split(Rational.ONE), "((-Infinity, 1], [1, Infinity))");
        aeq(read("[4, 4]").get().split(Rational.read("4").get()), "([4, 4], [4, 4])");
        aeq(read("[0, 1]").get().split(Rational.read("1/3").get()), "([0, 1/3], [1/3, 1])");
        aeq(read("[0, 1]").get().split(Rational.ZERO), "([0, 0], [0, 1])");
        aeq(read("[0, 1]").get().split(Rational.ONE), "([0, 1], [1, 1])");
        aeq(read("[-2, 5/3]").get().split(Rational.read("1").get()), "([-2, 1], [1, 5/3])");
        aeq(read("(-Infinity, 1]").get().split(Rational.read("-3").get()), "((-Infinity, -3], [-3, 1])");
        aeq(read("[5/3, Infinity)").get().split(Rational.read("10").get()), "([5/3, 10], [10, Infinity))");
        try {
            ZERO.split(Rational.ONE);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[-2, 5/3]").get().split(Rational.read("-4").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("(-Infinity, 1]").get().split(Rational.read("4").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1, Infinity)").get().split(Rational.read("-4").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBisect() {
        aeq(ZERO.bisect(), "([0, 0], [0, 0])");
        aeq(ONE.bisect(), "([1, 1], [1, 1])");
        aeq(read("[4, 4]").get().bisect(), "([4, 4], [4, 4])");
        aeq(read("[1, 2]").get().bisect(), "([1, 3/2], [3/2, 2])");
        aeq(read("[-2, 5/3]").get().bisect(), "([-2, -1/6], [-1/6, 5/3])");
        try {
            ALL.bisect();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("(-Infinity, 1]").get().bisect();
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1, Infinity)").get().bisect();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundingPreimage_float() {
        aeq(
                roundingPreimage(0.0f),
                "[-1/1427247692705959881058285969449495136382746624, 1/1427247692705959881058285969449495136382746624]"
        );
        aeq(
                roundingPreimage(-0.0f),
                "[-1/1427247692705959881058285969449495136382746624, 1/1427247692705959881058285969449495136382746624]"
        );
        aeq(roundingPreimage(Float.POSITIVE_INFINITY), "[340282346638528859811704183484516925440, Infinity)");
        aeq(roundingPreimage(Float.NEGATIVE_INFINITY), "(-Infinity, -340282346638528859811704183484516925440]");
        aeq(roundingPreimage(1.0f), "[33554431/33554432, 16777217/16777216]");
        aeq(roundingPreimage(13.0f), "[27262975/2097152, 27262977/2097152]");
        aeq(roundingPreimage(-5.0f), "[-20971521/4194304, -20971519/4194304]");
        aeq(roundingPreimage(1.5f), "[25165823/16777216, 25165825/16777216]");
        aeq(roundingPreimage(0.15625f), "[20971519/134217728, 20971521/134217728]");
        aeq(roundingPreimage(0.1f), "[26843545/268435456, 26843547/268435456]");
        aeq(roundingPreimage(1.0f / 3.0f), "[22369621/67108864, 22369623/67108864]");
        aeq(roundingPreimage(1.0e10f), "[9999999488, 10000000512]");
        aeq(roundingPreimage(1.0e30f), "[999999977268534356919527145472, 1000000052826398082833850564608]");
        aeq(roundingPreimage((float) Math.PI), "[26353589/8388608, 26353591/8388608]");
        aeq(roundingPreimage((float) Math.E), "[22802599/8388608, 22802601/8388608]");
        aeq(roundingPreimage((float) Math.sqrt(2)), "[23726565/16777216, 23726567/16777216]");
        aeq(
                roundingPreimage(Float.MIN_VALUE),
                "[1/1427247692705959881058285969449495136382746624, 3/1427247692705959881058285969449495136382746624]"
        );
        aeq(
                roundingPreimage(-Float.MIN_VALUE),
                "[-3/1427247692705959881058285969449495136382746624," +
                " -1/1427247692705959881058285969449495136382746624]"
        );
        aeq(
                roundingPreimage(Float.MIN_NORMAL),
                "[16777215/1427247692705959881058285969449495136382746624," +
                " 16777217/1427247692705959881058285969449495136382746624]"
        );
        aeq(
                roundingPreimage(-Float.MIN_NORMAL),
                "[-16777217/1427247692705959881058285969449495136382746624," +
                " -16777215/1427247692705959881058285969449495136382746624]"
        );
        aeq(
                roundingPreimage(Float.MAX_VALUE),
                "[340282336497324057985868971510891282432, 340282346638528859811704183484516925440]"
        );
        aeq(
                roundingPreimage(-Float.MAX_VALUE),
                "[-340282346638528859811704183484516925440, -340282336497324057985868971510891282432]"
        );
        try {
            roundingPreimage(Float.NaN);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundingPreimage_double() {
        aeq(
                roundingPreimage(0.0),
                "[-1/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568," +
                " 1/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568]"
        );
        aeq(
                roundingPreimage(-0.0),
                "[-1/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568," +
                " 1/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568]"
        );
        aeq(
                roundingPreimage(Double.POSITIVE_INFINITY),
                "[17976931348623157081452742373170435679807056752584499659891747680315726078002853876058955863276687" +
                "817154045895351438246423432132688946418276846754670353751698604991057655128207624549009038932894407" +
                "586850845513394230458323690322294816580855933212334827479782620414472316873817718091929988125040402" +
                "6184124858368," +
                " Infinity)"
        );
        aeq(
                roundingPreimage(Double.NEGATIVE_INFINITY),
                "(-Infinity," +
                " -1797693134862315708145274237317043567980705675258449965989174768031572607800285387605895586327668" +
                "781715404589535143824642343213268894641827684675467035375169860499105765512820762454900903893289440" +
                "758685084551339423045832369032229481658085593321233482747978262041447231687381771809192998812504040" +
                "26184124858368]"
        );
        aeq(roundingPreimage(1.0), "[18014398509481983/18014398509481984, 9007199254740993/9007199254740992]");
        aeq(roundingPreimage(13.0), "[14636698788954111/1125899906842624, 14636698788954113/1125899906842624]");
        aeq(roundingPreimage(-5.0), "[-11258999068426241/2251799813685248, -11258999068426239/2251799813685248]");
        aeq(roundingPreimage(1.5), "[13510798882111487/9007199254740992, 13510798882111489/9007199254740992]");
        aeq(roundingPreimage(0.15625), "[11258999068426239/72057594037927936, 11258999068426241/72057594037927936]");
        aeq(roundingPreimage(0.1), "[14411518807585587/144115188075855872, 14411518807585589/144115188075855872]");
        aeq(roundingPreimage(1.0 / 3.0), "[12009599006321321/36028797018963968, 12009599006321323/36028797018963968]");
        aeq(roundingPreimage(1.0e10), "[10485759999999999/1048576, 10485760000000001/1048576]");
        aeq(roundingPreimage(1.0e30), "[999999999999999949515880660992, 1000000000000000090253369016320]");
        aeq(roundingPreimage(Math.PI), "[14148475504056879/4503599627370496, 14148475504056881/4503599627370496]");
        aeq(roundingPreimage(Math.E), "[12242053029736145/4503599627370496, 12242053029736147/4503599627370496]");
        aeq(roundingPreimage(Math.sqrt(2)),
                "[12738103345051545/9007199254740992, 12738103345051547/9007199254740992]");
        aeq(
                roundingPreimage(Double.MIN_VALUE),
                "[1/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568," +
                " 3/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568]"
        );
        aeq(
                roundingPreimage(-Double.MIN_VALUE),
                "[-3/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568," +
                " -1/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568]"
        );
        aeq(
                roundingPreimage(Double.MIN_NORMAL),
                "[9007199254740991/404804506614621236704990693437834614099113299528284236713802716054860679135990693" +
                "783920767402874248990374155728633623822779617474771586953734026799881477019843034848553132722728933" +
                "815484186432682479535356945490137124014966849385397236206711298319112681620113024717539104666829230" +
                "461005064372655017292012526615415482186989568," +
                " 9007199254740993/404804506614621236704990693437834614099113299528284236713802716054860679135990693" +
                "783920767402874248990374155728633623822779617474771586953734026799881477019843034848553132722728933" +
                "815484186432682479535356945490137124014966849385397236206711298319112681620113024717539104666829230" +
                "461005064372655017292012526615415482186989568]"
        );
        aeq(
                roundingPreimage(-Double.MIN_NORMAL),
                "[-9007199254740993/40480450661462123670499069343783461409911329952828423671380271605486067913599069" +
                "378392076740287424899037415572863362382277961747477158695373402679988147701984303484855313272272893" +
                "381548418643268247953535694549013712401496684938539723620671129831911268162011302471753910466682923" +
                "0461005064372655017292012526615415482186989568," +
                " -9007199254740991/40480450661462123670499069343783461409911329952828423671380271605486067913599069" +
                "378392076740287424899037415572863362382277961747477158695373402679988147701984303484855313272272893" +
                "381548418643268247953535694549013712401496684938539723620671129831911268162011302471753910466682923" +
                "0461005064372655017292012526615415482186989568]"
        );
        aeq(
                roundingPreimage(Double.MAX_VALUE),
                "[17976931348623156083532587605810529851620700234165216626166117462586955326729232657453009928794654" +
                "924675063149033587701752208710592698796290627760473556921329019091915239418047621712533496094635638" +
                "726128664019802903779951418360298151175628372777140383052148396392393563313364280213909166945792787" +
                "4464075218944," +
                " 17976931348623157081452742373170435679807056752584499659891747680315726078002853876058955863276687" +
                "817154045895351438246423432132688946418276846754670353751698604991057655128207624549009038932894407" +
                "586850845513394230458323690322294816580855933212334827479782620414472316873817718091929988125040402" +
                "6184124858368]"
        );
        aeq(
                roundingPreimage(-Double.MAX_VALUE),
                "[-1797693134862315708145274237317043567980705675258449965989174768031572607800285387605895586327668" +
                "781715404589535143824642343213268894641827684675467035375169860499105765512820762454900903893289440" +
                "758685084551339423045832369032229481658085593321233482747978262041447231687381771809192998812504040" +
                "26184124858368," +
                " -1797693134862315608353258760581052985162070023416521662616611746258695532672923265745300992879465" +
                "492467506314903358770175220871059269879629062776047355692132901909191523941804762171253349609463563" +
                "872612866401980290377995141836029815117562837277714038305214839639239356331336428021390916694579278" +
                "74464075218944]"
        );
        try {
            roundingPreimage(Double.NaN);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundingPreimage_BigDecimal() {
        aeq(roundingPreimage(BigDecimal.ZERO), "[-1/2, 1/2]");
        aeq(roundingPreimage(BigDecimal.ONE), "[1/2, 3/2]");
        aeq(roundingPreimage(new BigDecimal("3")), "[5/2, 7/2]");
        aeq(roundingPreimage(new BigDecimal("-5")), "[-11/2, -9/2]");
        aeq(roundingPreimage(new BigDecimal("0.1")), "[1/20, 3/20]");
        aeq(roundingPreimage(new BigDecimal("3.14159")), "[628317/200000, 628319/200000]");
        aeq(
                roundingPreimage(new BigDecimal("-2.718281828459045")),
                "[-5436563656918091/2000000000000000, -5436563656918089/2000000000000000]"
        );
        aeq(roundingPreimage(new BigDecimal("0.00000000000001")), "[1/200000000000000, 3/200000000000000]");
        aeq(roundingPreimage(new BigDecimal("1000000000000000")), "[1999999999999999/2, 2000000000000001/2]");
        aeq(roundingPreimage(new BigDecimal("1E15")), "[500000000000000, 1500000000000000]");
    }

    @Test
    public void testFloatRange() {
        aeq(ZERO.floatRange(), "(0.0, 0.0)");
        aeq(ONE.floatRange(), "(1.0, 1.0)");
        aeq(ALL.floatRange(), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().floatRange(), "(-2.0, 1.6666667)");
        aeq(read("[5/3, 4]").get().floatRange(), "(1.6666666, 4.0)");
        aeq(read("[4, 4]").get().floatRange(), "(4.0, 4.0)");
        aeq(read("[1/3, 1/3]").get().floatRange(), "(0.3333333, 0.33333334)");
        aeq(read("(-Infinity, 3/2]").get().floatRange(), "(-Infinity, 1.5)");
        aeq(read("[-6, Infinity)").get().floatRange(), "(-6.0, Infinity)");
        aeq(of(Rational.TEN.pow(-100)).floatRange(), "(0.0, 1.4E-45)");
        aeq(of(Rational.TEN.pow(-100).negate()).floatRange(), "(-1.4E-45, -0.0)");
        aeq(of(Rational.TEN.pow(-100).negate(), Rational.TEN.pow(-100)).floatRange(), "(-1.4E-45, 1.4E-45)");
        aeq(of(Rational.TEN.pow(100)).floatRange(), "(3.4028235E38, Infinity)");
        aeq(of(Rational.TEN.pow(100).negate()).floatRange(), "(-Infinity, -3.4028235E38)");
        aeq(of(Rational.TEN.pow(100).negate(), Rational.TEN.pow(100)).floatRange(), "(-Infinity, Infinity)");
    }

    @Test
    public void testDoubleRange() {
        aeq(ZERO.doubleRange(), "(0.0, 0.0)");
        aeq(ONE.doubleRange(), "(1.0, 1.0)");
        aeq(ALL.doubleRange(), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().doubleRange(), "(-2.0, 1.6666666666666667)");
        aeq(read("[5/3, 4]").get().doubleRange(), "(1.6666666666666665, 4.0)");
        aeq(read("[4, 4]").get().doubleRange(), "(4.0, 4.0)");
        aeq(read("[1/3, 1/3]").get().doubleRange(), "(0.3333333333333333, 0.33333333333333337)");
        aeq(read("(-Infinity, 3/2]").get().doubleRange(), "(-Infinity, 1.5)");
        aeq(read("[-6, Infinity)").get().doubleRange(), "(-6.0, Infinity)");
        aeq(of(Rational.TEN.pow(-1000)).doubleRange(), "(0.0, 4.9E-324)");
        aeq(of(Rational.TEN.pow(-1000).negate()).doubleRange(), "(-4.9E-324, -0.0)");
        aeq(
                of(Rational.TEN.pow(-1000).negate(), Rational.TEN.pow(-1000)).doubleRange(),
                "(-4.9E-324, 4.9E-324)"
        );
        aeq(of(Rational.TEN.pow(1000)).doubleRange(), "(1.7976931348623157E308, Infinity)");
        aeq(of(Rational.TEN.pow(1000).negate()).doubleRange(), "(-Infinity, -1.7976931348623157E308)");
        aeq(of(Rational.TEN.pow(1000).negate(), Rational.TEN.pow(1000)).doubleRange(), "(-Infinity, Infinity)");
    }

    @Test
    public void testAdd() {
        aeq(ZERO.add(ZERO), "[0, 0]");
        aeq(ZERO.add(ONE), "[1, 1]");
        aeq(ZERO.add(ALL), "(-Infinity, Infinity)");
        aeq(ZERO.add(read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(ZERO.add(read("[4, 4]").get()), "[4, 4]");
        aeq(ZERO.add(read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(ZERO.add(read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(ONE.add(ZERO), "[1, 1]");
        aeq(ONE.add(ONE), "[2, 2]");
        aeq(ONE.add(ALL), "(-Infinity, Infinity)");
        aeq(ONE.add(read("[-2, 5/3]").get()), "[-1, 8/3]");
        aeq(ONE.add(read("[4, 4]").get()), "[5, 5]");
        aeq(ONE.add(read("(-Infinity, 3/2]").get()), "(-Infinity, 5/2]");
        aeq(ONE.add(read("[-6, Infinity)").get()), "[-5, Infinity)");
        aeq(ALL.add(ZERO), "(-Infinity, Infinity)");
        aeq(ALL.add(ONE), "(-Infinity, Infinity)");
        aeq(ALL.add(ALL), "(-Infinity, Infinity)");
        aeq(ALL.add(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ALL.add(read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(ALL.add(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ALL.add(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().add(ZERO), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().add(ONE), "[-1, 8/3]");
        aeq(read("[-2, 5/3]").get().add(ALL), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().add(read("[-2, 5/3]").get()), "[-4, 10/3]");
        aeq(read("[-2, 5/3]").get().add(read("[4, 4]").get()), "[2, 17/3]");
        aeq(read("[-2, 5/3]").get().add(read("(-Infinity, 3/2]").get()), "(-Infinity, 19/6]");
        aeq(read("[-2, 5/3]").get().add(read("[-6, Infinity)").get()), "[-8, Infinity)");
        aeq(read("[4, 4]").get().add(ZERO), "[4, 4]");
        aeq(read("[4, 4]").get().add(ONE), "[5, 5]");
        aeq(read("[4, 4]").get().add(ALL), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().add(read("[-2, 5/3]").get()), "[2, 17/3]");
        aeq(read("[4, 4]").get().add(read("[4, 4]").get()), "[8, 8]");
        aeq(read("[4, 4]").get().add(read("(-Infinity, 3/2]").get()), "(-Infinity, 11/2]");
        aeq(read("[4, 4]").get().add(read("[-6, Infinity)").get()), "[-2, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().add(ZERO), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().add(ONE), "(-Infinity, 5/2]");
        aeq(read("(-Infinity, 3/2]").get().add(ALL), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().add(read("[-2, 5/3]").get()), "(-Infinity, 19/6]");
        aeq(read("(-Infinity, 3/2]").get().add(read("[4, 4]").get()), "(-Infinity, 11/2]");
        aeq(read("(-Infinity, 3/2]").get().add(read("(-Infinity, 3/2]").get()), "(-Infinity, 3]");
        aeq(read("(-Infinity, 3/2]").get().add(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().add(ZERO), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().add(ONE), "[-5, Infinity)");
        aeq(read("[-6, Infinity)").get().add(ALL), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().add(read("[-2, 5/3]").get()), "[-8, Infinity)");
        aeq(read("[-6, Infinity)").get().add(read("[4, 4]").get()), "[-2, Infinity)");
        aeq(read("[-6, Infinity)").get().add(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().add(read("[-6, Infinity)").get()), "[-12, Infinity)");
    }

    @Test
    public void testNegate() {
        aeq(ZERO.negate(), "[0, 0]");
        aeq(ONE.negate(), "[-1, -1]");
        aeq(ALL.negate(), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().negate(), "[-5/3, 2]");
        aeq(read("[4, 4]").get().negate(), "[-4, -4]");
        aeq(read("(-Infinity, 3/2]").get().negate(), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().negate(), "(-Infinity, 6]");
    }

    @Test
    public void testAbs() {
        aeq(ZERO.abs(), "[0, 0]");
        aeq(ONE.abs(), "[1, 1]");
        aeq(ALL.abs(), "[0, Infinity)");
        aeq(read("[-2, 5/3]").get().abs(), "[0, 2]");
        aeq(read("[4, 4]").get().abs(), "[4, 4]");
        aeq(read("(-Infinity, 3/2]").get().abs(), "[0, Infinity)");
        aeq(read("(-Infinity, -3/2]").get().abs(), "[3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().abs(), "[0, Infinity)");
        aeq(read("[6, Infinity)").get().abs(), "[6, Infinity)");
    }

    @Test
    public void testSignum() {
        aeq(ZERO.signum(), "Optional[0]");
        aeq(ONE.signum(), "Optional[1]");
        aeq(ALL.signum(), "Optional.empty");
        aeq(read("[-2, 5/3]").get().signum(), "Optional.empty");
        aeq(read("[4, 4]").get().signum(), "Optional[1]");
        aeq(read("(-Infinity, 3/2]").get().signum(), "Optional.empty");
        aeq(read("(-Infinity, -3/2]").get().signum(), "Optional[-1]");
        aeq(read("[-6, Infinity)").get().signum(), "Optional.empty");
        aeq(read("[6, Infinity)").get().signum(), "Optional[1]");
    }

    @Test
    public void testSubtract() {
        aeq(ZERO.subtract(ZERO), "[0, 0]");
        aeq(ZERO.subtract(ONE), "[-1, -1]");
        aeq(ZERO.subtract(ALL), "(-Infinity, Infinity)");
        aeq(ZERO.subtract(read("[-2, 5/3]").get()), "[-5/3, 2]");
        aeq(ZERO.subtract(read("[4, 4]").get()), "[-4, -4]");
        aeq(ZERO.subtract(read("(-Infinity, 3/2]").get()), "[-3/2, Infinity)");
        aeq(ZERO.subtract(read("[-6, Infinity)").get()), "(-Infinity, 6]");
        aeq(ONE.subtract(ZERO), "[1, 1]");
        aeq(ONE.subtract(ONE), "[0, 0]");
        aeq(ONE.subtract(ALL), "(-Infinity, Infinity)");
        aeq(ONE.subtract(read("[-2, 5/3]").get()), "[-2/3, 3]");
        aeq(ONE.subtract(read("[4, 4]").get()), "[-3, -3]");
        aeq(ONE.subtract(read("(-Infinity, 3/2]").get()), "[-1/2, Infinity)");
        aeq(ONE.subtract(read("[-6, Infinity)").get()), "(-Infinity, 7]");
        aeq(ALL.subtract(ZERO), "(-Infinity, Infinity)");
        aeq(ALL.subtract(ONE), "(-Infinity, Infinity)");
        aeq(ALL.subtract(ALL), "(-Infinity, Infinity)");
        aeq(ALL.subtract(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ALL.subtract(read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(ALL.subtract(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ALL.subtract(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().subtract(ZERO), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().subtract(ONE), "[-3, 2/3]");
        aeq(read("[-2, 5/3]").get().subtract(ALL), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().subtract(read("[-2, 5/3]").get()), "[-11/3, 11/3]");
        aeq(read("[-2, 5/3]").get().subtract(read("[4, 4]").get()), "[-6, -7/3]");
        aeq(read("[-2, 5/3]").get().subtract(read("(-Infinity, 3/2]").get()), "[-7/2, Infinity)");
        aeq(read("[-2, 5/3]").get().subtract(read("[-6, Infinity)").get()), "(-Infinity, 23/3]");
        aeq(read("[4, 4]").get().subtract(ZERO), "[4, 4]");
        aeq(read("[4, 4]").get().subtract(ONE), "[3, 3]");
        aeq(read("[4, 4]").get().subtract(ALL), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().subtract(read("[-2, 5/3]").get()), "[7/3, 6]");
        aeq(read("[4, 4]").get().subtract(read("[4, 4]").get()), "[0, 0]");
        aeq(read("[4, 4]").get().subtract(read("(-Infinity, 3/2]").get()), "[5/2, Infinity)");
        aeq(read("[4, 4]").get().subtract(read("[-6, Infinity)").get()), "(-Infinity, 10]");
        aeq(read("(-Infinity, 3/2]").get().subtract(ZERO), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().subtract(ONE), "(-Infinity, 1/2]");
        aeq(read("(-Infinity, 3/2]").get().subtract(ALL), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().subtract(read("[-2, 5/3]").get()), "(-Infinity, 7/2]");
        aeq(read("(-Infinity, 3/2]").get().subtract(read("[4, 4]").get()), "(-Infinity, -5/2]");
        aeq(read("(-Infinity, 3/2]").get().subtract(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().subtract(read("[-6, Infinity)").get()), "(-Infinity, 15/2]");
        aeq(read("[-6, Infinity)").get().subtract(ZERO), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().subtract(ONE), "[-7, Infinity)");
        aeq(read("[-6, Infinity)").get().subtract(ALL), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().subtract(read("[-2, 5/3]").get()), "[-23/3, Infinity)");
        aeq(read("[-6, Infinity)").get().subtract(read("[4, 4]").get()), "[-10, Infinity)");
        aeq(read("[-6, Infinity)").get().subtract(read("(-Infinity, 3/2]").get()), "[-15/2, Infinity)");
        aeq(read("[-6, Infinity)").get().subtract(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
    }

    @Test
    public void testMultiply_Interval() {
        aeq(ZERO.multiply(ZERO), "[0, 0]");
        aeq(ZERO.multiply(ONE), "[0, 0]");
        aeq(ZERO.multiply(ALL), "[0, 0]");
        aeq(ZERO.multiply(read("[-2, 5/3]").get()), "[0, 0]");
        aeq(ZERO.multiply(read("[4, 4]").get()), "[0, 0]");
        aeq(ZERO.multiply(read("(-Infinity, 3/2]").get()), "[0, 0]");
        aeq(ZERO.multiply(read("[-6, Infinity)").get()), "[0, 0]");
        aeq(ONE.multiply(ZERO), "[0, 0]");
        aeq(ONE.multiply(ONE), "[1, 1]");
        aeq(ONE.multiply(ALL), "(-Infinity, Infinity)");
        aeq(ONE.multiply(read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(ONE.multiply(read("[4, 4]").get()), "[4, 4]");
        aeq(ONE.multiply(read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(ONE.multiply(read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(ALL.multiply(ZERO), "[0, 0]");
        aeq(ALL.multiply(ONE), "(-Infinity, Infinity)");
        aeq(ALL.multiply(ALL), "(-Infinity, Infinity)");
        aeq(ALL.multiply(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ALL.multiply(read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(ALL.multiply(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ALL.multiply(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().multiply(ZERO), "[0, 0]");
        aeq(read("[-2, 5/3]").get().multiply(ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().multiply(ALL), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().multiply(read("[-2, 5/3]").get()), "[-10/3, 4]");
        aeq(read("[-2, 5/3]").get().multiply(read("[4, 4]").get()), "[-8, 20/3]");
        aeq(read("[-2, 5/3]").get().multiply(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().multiply(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().multiply(ZERO), "[0, 0]");
        aeq(read("[4, 4]").get().multiply(ONE), "[4, 4]");
        aeq(read("[4, 4]").get().multiply(ALL), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().multiply(read("[-2, 5/3]").get()), "[-8, 20/3]");
        aeq(read("[4, 4]").get().multiply(read("[4, 4]").get()), "[16, 16]");
        aeq(read("[4, 4]").get().multiply(read("(-Infinity, 3/2]").get()), "(-Infinity, 6]");
        aeq(read("[4, 4]").get().multiply(read("[-6, Infinity)").get()), "[-24, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().multiply(ZERO), "[0, 0]");
        aeq(read("(-Infinity, 3/2]").get().multiply(ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().multiply(ALL), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().multiply(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().multiply(read("[4, 4]").get()), "(-Infinity, 6]");
        aeq(read("(-Infinity, 3/2]").get().multiply(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().multiply(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(ZERO), "[0, 0]");
        aeq(read("[-6, Infinity)").get().multiply(ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(ALL), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(read("[4, 4]").get()), "[-24, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 0]").get().multiply(read("(-Infinity, 0]").get()), "[0, Infinity)");
    }

    @Test
    public void testMultiply_Rational() {
        aeq(ZERO.multiply(Rational.ZERO), "[0, 0]");
        aeq(ZERO.multiply(Rational.ONE), "[0, 0]");
        aeq(ZERO.multiply(Rational.read("2/3").get()), "[0, 0]");
        aeq(ZERO.multiply(Rational.read("-7").get()), "[0, 0]");
        aeq(ONE.multiply(Rational.ZERO), "[0, 0]");
        aeq(ONE.multiply(Rational.ONE), "[1, 1]");
        aeq(ONE.multiply(Rational.read("2/3").get()), "[2/3, 2/3]");
        aeq(ONE.multiply(Rational.read("-7").get()), "[-7, -7]");
        aeq(ALL.multiply(Rational.ZERO), "[0, 0]");
        aeq(ALL.multiply(Rational.ONE), "(-Infinity, Infinity)");
        aeq(ALL.multiply(Rational.read("2/3").get()), "(-Infinity, Infinity)");
        aeq(ALL.multiply(Rational.read("-7").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().multiply(Rational.ZERO), "[0, 0]");
        aeq(read("[-2, 5/3]").get().multiply(Rational.ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().multiply(Rational.read("2/3").get()), "[-4/3, 10/9]");
        aeq(read("[-2, 5/3]").get().multiply(Rational.read("-7").get()), "[-35/3, 14]");
        aeq(read("[4, 4]").get().multiply(Rational.ZERO), "[0, 0]");
        aeq(read("[4, 4]").get().multiply(Rational.ONE), "[4, 4]");
        aeq(read("[4, 4]").get().multiply(Rational.read("2/3").get()), "[8/3, 8/3]");
        aeq(read("[4, 4]").get().multiply(Rational.read("-7").get()), "[-28, -28]");
        aeq(read("(-Infinity, 3/2]").get().multiply(Rational.ZERO), "[0, 0]");
        aeq(read("(-Infinity, 3/2]").get().multiply(Rational.ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().multiply(Rational.read("2/3").get()), "(-Infinity, 1]");
        aeq(read("(-Infinity, 3/2]").get().multiply(Rational.read("-7").get()), "[-21/2, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(Rational.ZERO), "[0, 0]");
        aeq(read("[-6, Infinity)").get().multiply(Rational.ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(Rational.read("2/3").get()), "[-4, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(Rational.read("-7").get()), "(-Infinity, 42]");
    }

    @Test
    public void testMultiply_BigInteger() {
        aeq(ZERO.multiply(BigInteger.ZERO), "[0, 0]");
        aeq(ZERO.multiply(BigInteger.ONE), "[0, 0]");
        aeq(ZERO.multiply(BigInteger.valueOf(5)), "[0, 0]");
        aeq(ZERO.multiply(BigInteger.valueOf(-7)), "[0, 0]");
        aeq(ONE.multiply(BigInteger.ZERO), "[0, 0]");
        aeq(ONE.multiply(BigInteger.ONE), "[1, 1]");
        aeq(ONE.multiply(BigInteger.valueOf(5)), "[5, 5]");
        aeq(ONE.multiply(BigInteger.valueOf(-7)), "[-7, -7]");
        aeq(ALL.multiply(BigInteger.ZERO), "[0, 0]");
        aeq(ALL.multiply(BigInteger.ONE), "(-Infinity, Infinity)");
        aeq(ALL.multiply(BigInteger.valueOf(5)), "(-Infinity, Infinity)");
        aeq(ALL.multiply(BigInteger.valueOf(-7)), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().multiply(BigInteger.ZERO), "[0, 0]");
        aeq(read("[-2, 5/3]").get().multiply(BigInteger.ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().multiply(BigInteger.valueOf(5)), "[-10, 25/3]");
        aeq(read("[-2, 5/3]").get().multiply(BigInteger.valueOf(-7)), "[-35/3, 14]");
        aeq(read("[4, 4]").get().multiply(BigInteger.ZERO), "[0, 0]");
        aeq(read("[4, 4]").get().multiply(BigInteger.ONE), "[4, 4]");
        aeq(read("[4, 4]").get().multiply(BigInteger.valueOf(5)), "[20, 20]");
        aeq(read("[4, 4]").get().multiply(BigInteger.valueOf(-7)), "[-28, -28]");
        aeq(read("(-Infinity, 3/2]").get().multiply(BigInteger.ZERO), "[0, 0]");
        aeq(read("(-Infinity, 3/2]").get().multiply(BigInteger.ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().multiply(BigInteger.valueOf(5)), "(-Infinity, 15/2]");
        aeq(read("(-Infinity, 3/2]").get().multiply(BigInteger.valueOf(-7)), "[-21/2, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(BigInteger.ZERO), "[0, 0]");
        aeq(read("[-6, Infinity)").get().multiply(BigInteger.ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(BigInteger.valueOf(5)), "[-30, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(BigInteger.valueOf(-7)), "(-Infinity, 42]");
    }

    @Test
    public void testMultiply_int() {
        aeq(ZERO.multiply(0), "[0, 0]");
        aeq(ZERO.multiply(1), "[0, 0]");
        aeq(ZERO.multiply(5), "[0, 0]");
        aeq(ZERO.multiply(-7), "[0, 0]");
        aeq(ONE.multiply(0), "[0, 0]");
        aeq(ONE.multiply(1), "[1, 1]");
        aeq(ONE.multiply(5), "[5, 5]");
        aeq(ONE.multiply(-7), "[-7, -7]");
        aeq(ALL.multiply(0), "[0, 0]");
        aeq(ALL.multiply(1), "(-Infinity, Infinity)");
        aeq(ALL.multiply(5), "(-Infinity, Infinity)");
        aeq(ALL.multiply(-7), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().multiply(0), "[0, 0]");
        aeq(read("[-2, 5/3]").get().multiply(1), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().multiply(5), "[-10, 25/3]");
        aeq(read("[-2, 5/3]").get().multiply(-7), "[-35/3, 14]");
        aeq(read("[4, 4]").get().multiply(0), "[0, 0]");
        aeq(read("[4, 4]").get().multiply(1), "[4, 4]");
        aeq(read("[4, 4]").get().multiply(5), "[20, 20]");
        aeq(read("[4, 4]").get().multiply(-7), "[-28, -28]");
        aeq(read("(-Infinity, 3/2]").get().multiply(0), "[0, 0]");
        aeq(read("(-Infinity, 3/2]").get().multiply(1), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().multiply(5), "(-Infinity, 15/2]");
        aeq(read("(-Infinity, 3/2]").get().multiply(-7), "[-21/2, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(0), "[0, 0]");
        aeq(read("[-6, Infinity)").get().multiply(1), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(5), "[-30, Infinity)");
        aeq(read("[-6, Infinity)").get().multiply(-7), "(-Infinity, 42]");
    }

    @Test
    public void testInvert() {
        aeq(ZERO.invert(), "[]");
        aeq(ONE.invert(), "[[1, 1]]");
        aeq(ALL.invert(), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().invert(), "[(-Infinity, -1/2], [3/5, Infinity)]");
        aeq(read("[4, 4]").get().invert(), "[[1/4, 1/4]]");
        aeq(read("[0, 4]").get().invert(), "[[1/4, Infinity)]");
        aeq(read("[-4, 0]").get().invert(), "[(-Infinity, -1/4]]");
        aeq(read("(-Infinity, -3/2]").get().invert(), "[[-2/3, 0]]");
        aeq(read("(-Infinity, 3/2]").get().invert(), "[(-Infinity, 0], [2/3, Infinity)]");
        aeq(read("(-Infinity, 0]").get().invert(), "[(-Infinity, 0]]");
        aeq(read("[-6, Infinity)").get().invert(), "[(-Infinity, -1/6], [0, Infinity)]");
        aeq(read("[6, Infinity)").get().invert(), "[[0, 1/6]]");
        aeq(read("[0, Infinity)").get().invert(), "[[0, Infinity)]");
        aeq(read("(-Infinity, 0]").get().invert(), "[(-Infinity, 0]]");
    }

    @Test
    public void testInvertHull() {
        aeq(ONE.invertHull(), "[1, 1]");
        aeq(ALL.invertHull(), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().invertHull(), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().invertHull(), "[1/4, 1/4]");
        aeq(read("[0, 4]").get().invertHull(), "[1/4, Infinity)");
        aeq(read("[-4, 0]").get().invertHull(), "(-Infinity, -1/4]");
        aeq(read("(-Infinity, -3/2]").get().invertHull(), "[-2/3, 0]");
        aeq(read("(-Infinity, 3/2]").get().invertHull(), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 0]").get().invertHull(), "(-Infinity, 0]");
        aeq(read("[-6, Infinity)").get().invertHull(), "(-Infinity, Infinity)");
        aeq(read("[6, Infinity)").get().invertHull(), "[0, 1/6]");
        aeq(read("[0, Infinity)").get().invertHull(), "[0, Infinity)");
        aeq(read("(-Infinity, 0]").get().invertHull(), "(-Infinity, 0]");
        try {
            ZERO.invertHull();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Interval() {
        aeq(ZERO.divide(ZERO), "[]");
        aeq(ZERO.divide(ONE), "[[0, 0]]");
        aeq(ZERO.divide(ALL), "[[0, 0]]");
        aeq(ZERO.divide(read("[-2, 5/3]").get()), "[[0, 0]]");
        aeq(ZERO.divide(read("[4, 4]").get()), "[[0, 0]]");
        aeq(ZERO.divide(read("(-Infinity, 3/2]").get()), "[[0, 0]]");
        aeq(ZERO.divide(read("[-6, Infinity)").get()), "[[0, 0]]");
        aeq(ONE.divide(ZERO), "[]");
        aeq(ONE.divide(ONE), "[[1, 1]]");
        aeq(ONE.divide(ALL), "[(-Infinity, Infinity)]");
        aeq(ONE.divide(read("[-2, 5/3]").get()), "[(-Infinity, -1/2], [3/5, Infinity)]");
        aeq(ONE.divide(read("[4, 4]").get()), "[[1/4, 1/4]]");
        aeq(ONE.divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, 0], [2/3, Infinity)]");
        aeq(ONE.divide(read("[-6, Infinity)").get()), "[(-Infinity, -1/6], [0, Infinity)]");
        aeq(ALL.divide(ZERO), "[]");
        aeq(ALL.divide(ONE), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(ALL), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("[4, 4]").get()), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(ZERO), "[]");
        aeq(read("[-2, 5/3]").get().divide(ONE), "[[-2, 5/3]]");
        aeq(read("[-2, 5/3]").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(read("[4, 4]").get()), "[[-1/2, 5/12]]");
        aeq(read("[-2, 5/3]").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("[4, 4]").get().divide(ZERO), "[]");
        aeq(read("[4, 4]").get().divide(ONE), "[[4, 4]]");
        aeq(read("[4, 4]").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("[4, 4]").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, -2], [12/5, Infinity)]");
        aeq(read("[4, 4]").get().divide(read("[4, 4]").get()), "[[1, 1]]");
        aeq(read("[4, 4]").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, 0], [8/3, Infinity)]");
        aeq(read("[4, 4]").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, -2/3], [0, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(ZERO), "[]");
        aeq(read("(-Infinity, 3/2]").get().divide(ONE), "[(-Infinity, 3/2]]");
        aeq(read("(-Infinity, 3/2]").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("[4, 4]").get()), "[(-Infinity, 3/8]]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(ZERO), "[]");
        aeq(read("[-6, Infinity)").get().divide(ONE), "[[-6, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("[4, 4]").get()), "[[-3/2, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 0]").get().divide(read("(-Infinity, 0]").get()), "[[0, Infinity)]");
    }

    @Test
    public void testDivideHull() {
        aeq(ZERO.divideHull(ONE), "[0, 0]");
        aeq(ZERO.divideHull(ALL), "[0, 0]");
        aeq(ZERO.divideHull(read("[-2, 5/3]").get()), "[0, 0]");
        aeq(ZERO.divideHull(read("[4, 4]").get()), "[0, 0]");
        aeq(ZERO.divideHull(read("(-Infinity, 3/2]").get()), "[0, 0]");
        aeq(ZERO.divideHull(read("[-6, Infinity)").get()), "[0, 0]");
        aeq(ONE.divideHull(ONE), "[1, 1]");
        aeq(ONE.divideHull(ALL), "(-Infinity, Infinity)");
        aeq(ONE.divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ONE.divideHull(read("[4, 4]").get()), "[1/4, 1/4]");
        aeq(ONE.divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ONE.divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(ONE), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(ALL), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(read("[4, 4]").get()), "[-1/2, 5/12]");
        aeq(read("[-2, 5/3]").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(ONE), "[4, 4]");
        aeq(read("[4, 4]").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(read("[4, 4]").get()), "[1, 1]");
        aeq(read("[4, 4]").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("[4, 4]").get()), "(-Infinity, 3/8]");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("[4, 4]").get()), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 0]").get().divideHull(read("(-Infinity, 0]").get()), "[0, Infinity)");
        try {
            ZERO.divideHull(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[-2, 5/3]").get().divideHull(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        aeq(ZERO.divide(Rational.ONE), "[0, 0]");
        aeq(ZERO.divide(Rational.read("2/3").get()), "[0, 0]");
        aeq(ZERO.divide(Rational.read("-7").get()), "[0, 0]");
        aeq(ONE.divide(Rational.ONE), "[1, 1]");
        aeq(ONE.divide(Rational.read("2/3").get()), "[3/2, 3/2]");
        aeq(ONE.divide(Rational.read("-7").get()), "[-1/7, -1/7]");
        aeq(ALL.divide(Rational.ONE), "(-Infinity, Infinity)");
        aeq(ALL.divide(Rational.read("2/3").get()), "(-Infinity, Infinity)");
        aeq(ALL.divide(Rational.read("-7").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divide(Rational.ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divide(Rational.read("2/3").get()), "[-3, 5/2]");
        aeq(read("[-2, 5/3]").get().divide(Rational.read("-7").get()), "[-5/21, 2/7]");
        aeq(read("[4, 4]").get().divide(Rational.ONE), "[4, 4]");
        aeq(read("[4, 4]").get().divide(Rational.read("2/3").get()), "[6, 6]");
        aeq(read("[4, 4]").get().divide(Rational.read("-7").get()), "[-4/7, -4/7]");
        aeq(read("(-Infinity, 3/2]").get().divide(Rational.ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divide(Rational.read("2/3").get()), "(-Infinity, 9/4]");
        aeq(read("(-Infinity, 3/2]").get().divide(Rational.read("-7").get()), "[-3/14, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(Rational.ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(Rational.read("2/3").get()), "[-9, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(Rational.read("-7").get()), "(-Infinity, 6/7]");
        try {
            read("[-2, 5/3]").get().divide(Rational.ZERO);
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        aeq(ZERO.divide(BigInteger.ONE), "[0, 0]");
        aeq(ZERO.divide(BigInteger.valueOf(5)), "[0, 0]");
        aeq(ZERO.divide(BigInteger.valueOf(-7)), "[0, 0]");
        aeq(ONE.divide(BigInteger.ONE), "[1, 1]");
        aeq(ONE.divide(BigInteger.valueOf(5)), "[1/5, 1/5]");
        aeq(ONE.divide(BigInteger.valueOf(-7)), "[-1/7, -1/7]");
        aeq(ALL.divide(BigInteger.ONE), "(-Infinity, Infinity)");
        aeq(ALL.divide(BigInteger.valueOf(5)), "(-Infinity, Infinity)");
        aeq(ALL.divide(BigInteger.valueOf(-7)), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divide(BigInteger.ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divide(BigInteger.valueOf(5)), "[-2/5, 1/3]");
        aeq(read("[-2, 5/3]").get().divide(BigInteger.valueOf(-7)), "[-5/21, 2/7]");
        aeq(read("[4, 4]").get().divide(BigInteger.ONE), "[4, 4]");
        aeq(read("[4, 4]").get().divide(BigInteger.valueOf(5)), "[4/5, 4/5]");
        aeq(read("[4, 4]").get().divide(BigInteger.valueOf(-7)), "[-4/7, -4/7]");
        aeq(read("(-Infinity, 3/2]").get().divide(BigInteger.ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divide(BigInteger.valueOf(5)), "(-Infinity, 3/10]");
        aeq(read("(-Infinity, 3/2]").get().divide(BigInteger.valueOf(-7)), "[-3/14, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(BigInteger.ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(BigInteger.valueOf(5)), "[-6/5, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(BigInteger.valueOf(-7)), "(-Infinity, 6/7]");
        try {
            read("[-2, 5/3]").get().divide(BigInteger.ZERO);
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        aeq(ZERO.divide(1), "[0, 0]");
        aeq(ZERO.divide(5), "[0, 0]");
        aeq(ZERO.divide(-7), "[0, 0]");
        aeq(ONE.divide(1), "[1, 1]");
        aeq(ONE.divide(5), "[1/5, 1/5]");
        aeq(ONE.divide(-7), "[-1/7, -1/7]");
        aeq(ALL.divide(1), "(-Infinity, Infinity)");
        aeq(ALL.divide(5), "(-Infinity, Infinity)");
        aeq(ALL.divide(-7), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divide(1), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divide(5), "[-2/5, 1/3]");
        aeq(read("[-2, 5/3]").get().divide(-7), "[-5/21, 2/7]");
        aeq(read("[4, 4]").get().divide(1), "[4, 4]");
        aeq(read("[4, 4]").get().divide(5), "[4/5, 4/5]");
        aeq(read("[4, 4]").get().divide(-7), "[-4/7, -4/7]");
        aeq(read("(-Infinity, 3/2]").get().divide(1), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divide(5), "(-Infinity, 3/10]");
        aeq(read("(-Infinity, 3/2]").get().divide(-7), "[-3/14, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(1), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(5), "[-6/5, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(-7), "(-Infinity, 6/7]");
        try {
            read("[-2, 5/3]").get().divide(0);
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        aeq(ZERO.shiftLeft(0), "[0, 0]");
        aeq(ZERO.shiftLeft(1), "[0, 0]");
        aeq(ZERO.shiftLeft(2), "[0, 0]");
        aeq(ZERO.shiftLeft(3), "[0, 0]");
        aeq(ZERO.shiftLeft(4), "[0, 0]");
        aeq(ZERO.shiftLeft(-1), "[0, 0]");
        aeq(ZERO.shiftLeft(-2), "[0, 0]");
        aeq(ZERO.shiftLeft(-3), "[0, 0]");
        aeq(ZERO.shiftLeft(-4), "[0, 0]");
        aeq(ONE.shiftLeft(0), "[1, 1]");
        aeq(ONE.shiftLeft(1), "[2, 2]");
        aeq(ONE.shiftLeft(2), "[4, 4]");
        aeq(ONE.shiftLeft(3), "[8, 8]");
        aeq(ONE.shiftLeft(4), "[16, 16]");
        aeq(ONE.shiftLeft(-1), "[1/2, 1/2]");
        aeq(ONE.shiftLeft(-2), "[1/4, 1/4]");
        aeq(ONE.shiftLeft(-3), "[1/8, 1/8]");
        aeq(ONE.shiftLeft(-4), "[1/16, 1/16]");
        aeq(ALL.shiftLeft(0), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(1), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(2), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(3), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(4), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-1), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-2), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-3), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-4), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().shiftLeft(0), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(1), "[-4, 10/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(2), "[-8, 20/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(3), "[-16, 40/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(4), "[-32, 80/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-1), "[-1, 5/6]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-2), "[-1/2, 5/12]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-3), "[-1/4, 5/24]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-4), "[-1/8, 5/48]");
        aeq(read("[4, 4]").get().shiftLeft(0), "[4, 4]");
        aeq(read("[4, 4]").get().shiftLeft(1), "[8, 8]");
        aeq(read("[4, 4]").get().shiftLeft(2), "[16, 16]");
        aeq(read("[4, 4]").get().shiftLeft(3), "[32, 32]");
        aeq(read("[4, 4]").get().shiftLeft(4), "[64, 64]");
        aeq(read("[4, 4]").get().shiftLeft(-1), "[2, 2]");
        aeq(read("[4, 4]").get().shiftLeft(-2), "[1, 1]");
        aeq(read("[4, 4]").get().shiftLeft(-3), "[1/2, 1/2]");
        aeq(read("[4, 4]").get().shiftLeft(-4), "[1/4, 1/4]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(0), "(-Infinity, -3/2]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(1), "(-Infinity, -3]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(2), "(-Infinity, -6]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(3), "(-Infinity, -12]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(4), "(-Infinity, -24]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-1), "(-Infinity, -3/4]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-2), "(-Infinity, -3/8]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-3), "(-Infinity, -3/16]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-4), "(-Infinity, -3/32]");
        aeq(read("[-6, Infinity)").get().shiftLeft(0), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(1), "[-12, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(2), "[-24, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(3), "[-48, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(4), "[-96, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-1), "[-3, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-2), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-3), "[-3/4, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-4), "[-3/8, Infinity)");
    }

    @Test
    public void testShiftRight() {
        aeq(ZERO.shiftRight(0), "[0, 0]");
        aeq(ZERO.shiftRight(1), "[0, 0]");
        aeq(ZERO.shiftRight(2), "[0, 0]");
        aeq(ZERO.shiftRight(3), "[0, 0]");
        aeq(ZERO.shiftRight(4), "[0, 0]");
        aeq(ZERO.shiftRight(-1), "[0, 0]");
        aeq(ZERO.shiftRight(-2), "[0, 0]");
        aeq(ZERO.shiftRight(-3), "[0, 0]");
        aeq(ZERO.shiftRight(-4), "[0, 0]");
        aeq(ONE.shiftRight(0), "[1, 1]");
        aeq(ONE.shiftRight(1), "[1/2, 1/2]");
        aeq(ONE.shiftRight(2), "[1/4, 1/4]");
        aeq(ONE.shiftRight(3), "[1/8, 1/8]");
        aeq(ONE.shiftRight(4), "[1/16, 1/16]");
        aeq(ONE.shiftRight(-1), "[2, 2]");
        aeq(ONE.shiftRight(-2), "[4, 4]");
        aeq(ONE.shiftRight(-3), "[8, 8]");
        aeq(ONE.shiftRight(-4), "[16, 16]");
        aeq(ALL.shiftRight(0), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(1), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(2), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(3), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(4), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-1), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-2), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-3), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-4), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().shiftRight(0), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(1), "[-1, 5/6]");
        aeq(read("[-2, 5/3]").get().shiftRight(2), "[-1/2, 5/12]");
        aeq(read("[-2, 5/3]").get().shiftRight(3), "[-1/4, 5/24]");
        aeq(read("[-2, 5/3]").get().shiftRight(4), "[-1/8, 5/48]");
        aeq(read("[-2, 5/3]").get().shiftRight(-1), "[-4, 10/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(-2), "[-8, 20/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(-3), "[-16, 40/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(-4), "[-32, 80/3]");
        aeq(read("[4, 4]").get().shiftRight(0), "[4, 4]");
        aeq(read("[4, 4]").get().shiftRight(1), "[2, 2]");
        aeq(read("[4, 4]").get().shiftRight(2), "[1, 1]");
        aeq(read("[4, 4]").get().shiftRight(3), "[1/2, 1/2]");
        aeq(read("[4, 4]").get().shiftRight(4), "[1/4, 1/4]");
        aeq(read("[4, 4]").get().shiftRight(-1), "[8, 8]");
        aeq(read("[4, 4]").get().shiftRight(-2), "[16, 16]");
        aeq(read("[4, 4]").get().shiftRight(-3), "[32, 32]");
        aeq(read("[4, 4]").get().shiftRight(-4), "[64, 64]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(0), "(-Infinity, -3/2]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(1), "(-Infinity, -3/4]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(2), "(-Infinity, -3/8]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(3), "(-Infinity, -3/16]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(4), "(-Infinity, -3/32]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-1), "(-Infinity, -3]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-2), "(-Infinity, -6]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-3), "(-Infinity, -12]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-4), "(-Infinity, -24]");
        aeq(read("[-6, Infinity)").get().shiftRight(0), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(1), "[-3, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(2), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(3), "[-3/4, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(4), "[-3/8, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-1), "[-12, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-2), "[-24, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-3), "[-48, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-4), "[-96, Infinity)");
    }

    @Test
    public void testSum() {
        aeq(sum(readIntervalList("[]")), "[0, 0]");
        aeq(sum(readIntervalList("[[-2, 5/3], (-Infinity, 6], [4, 4]]")), "(-Infinity, 35/3]");
        try {
            sum(readIntervalListWithNulls("[[-2, 5/3], null, [4, 4]]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        aeq(product(readIntervalList("[]")), "[1, 1]");
        aeq(product(readIntervalList("[[-2, 5/3], [0, 6], [4, 4]]")), "[-48, 40]");
        aeq(product(readIntervalList("[[-2, 5/3], (-Infinity, 6], [4, 4]]")), "(-Infinity, Infinity)");
        try {
            product(readIntervalListWithNulls("[[-2, 5/3], null, [4, 4]]"));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testDelta() {
        aeq(delta(readIntervalList("[[1/3, 2]]")), "[]");
        aeq(delta(readIntervalList("[[-2, 5/3], (-Infinity, 6], [4, 4]]")), "[(-Infinity, 8], [-2, Infinity)]");
        Interval seed = read("[1/3, 1/2]").get();
        aeqitLimit(TINY_LIMIT, delta(iterate(seed::multiply, seed)),
                "[[-7/18, -1/12], [-23/108, 1/72], [-73/648, 11/432], [-227/3888, 49/2592], [-697/23328, 179/15552]," +
                " [-2123/139968, 601/93312], [-6433/839808, 1931/559872], [-19427/5038848, 6049/3359232]," +
                " [-58537/30233088, 18659/20155392], [-176123/181398528, 57001/120932352]," +
                " [-529393/1088391168, 173051/725594112], [-1590227/6530347008, 523249/4353564672]," +
                " [-4774777/39182082048, 1577939/26121388032], [-14332523/235092492288, 4750201/156728328192]," +
                " [-43013953/1410554953728, 14283371/940369969152]," +
                " [-129074627/8463329722368, 42915649/5642219814912]," +
                " [-387289417/50779978334208, 128878019/33853318889472]," +
                " [-1161999323/304679870005248, 386896201/203119913336832]," +
                " [-3486260113/1828079220031488, 1161212891/1218719480020992]," +
                " [-10459304627/10968475320188928, 3484687249/7312316880125952], ...]");
        try {
            delta(readIntervalListWithNulls("[]"));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readIntervalListWithNulls("[[-2, 5/3], null, [4, 4]]")));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testPow() {
        aeq(ZERO.pow(0), "[[1, 1]]");
        aeq(ZERO.pow(1), "[[0, 0]]");
        aeq(ZERO.pow(2), "[[0, 0]]");
        aeq(ZERO.pow(3), "[[0, 0]]");
        aeq(ONE.pow(0), "[[1, 1]]");
        aeq(ONE.pow(1), "[[1, 1]]");
        aeq(ONE.pow(2), "[[1, 1]]");
        aeq(ONE.pow(3), "[[1, 1]]");
        aeq(ONE.pow(-1), "[[1, 1]]");
        aeq(ONE.pow(-2), "[[1, 1]]");
        aeq(ONE.pow(-3), "[[1, 1]]");
        aeq(ALL.pow(0), "[[1, 1]]");
        aeq(ALL.pow(1), "[(-Infinity, Infinity)]");
        aeq(ALL.pow(2), "[[0, Infinity)]");
        aeq(ALL.pow(3), "[(-Infinity, Infinity)]");
        aeq(ALL.pow(-1), "[(-Infinity, Infinity)]");
        aeq(ALL.pow(-2), "[[0, Infinity)]");
        aeq(ALL.pow(-3), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().pow(0), "[[1, 1]]");
        aeq(read("[-2, 5/3]").get().pow(1), "[[-2, 5/3]]");
        aeq(read("[-2, 5/3]").get().pow(2), "[[0, 4]]");
        aeq(read("[-2, 5/3]").get().pow(3), "[[-8, 125/27]]");
        aeq(read("[-2, 5/3]").get().pow(-1), "[(-Infinity, -1/2], [3/5, Infinity)]");
        aeq(read("[-2, 5/3]").get().pow(-2), "[[1/4, Infinity)]");
        aeq(read("[-2, 5/3]").get().pow(-3), "[(-Infinity, -1/8], [27/125, Infinity)]");
        aeq(read("[4, 4]").get().pow(0), "[[1, 1]]");
        aeq(read("[4, 4]").get().pow(1), "[[4, 4]]");
        aeq(read("[4, 4]").get().pow(2), "[[16, 16]]");
        aeq(read("[4, 4]").get().pow(3), "[[64, 64]]");
        aeq(read("[4, 4]").get().pow(-1), "[[1/4, 1/4]]");
        aeq(read("[4, 4]").get().pow(-2), "[[1/16, 1/16]]");
        aeq(read("[4, 4]").get().pow(-3), "[[1/64, 1/64]]");
        aeq(read("(-Infinity, 3/2]").get().pow(0), "[[1, 1]]");
        aeq(read("(-Infinity, 3/2]").get().pow(1), "[(-Infinity, 3/2]]");
        aeq(read("(-Infinity, 3/2]").get().pow(2), "[[0, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().pow(3), "[(-Infinity, 27/8]]");
        aeq(read("(-Infinity, 3/2]").get().pow(-1), "[(-Infinity, 0], [2/3, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().pow(-2), "[[0, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().pow(-3), "[(-Infinity, 0], [8/27, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(0), "[[1, 1]]");
        aeq(read("[-6, Infinity)").get().pow(1), "[[-6, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(2), "[[0, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(3), "[[-216, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(-1), "[(-Infinity, -1/6], [0, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(-2), "[[0, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(-3), "[(-Infinity, -1/216], [0, Infinity)]");
    }

    @Test
    public void testPowHull() {
        aeq(ZERO.powHull(0), "[1, 1]");
        aeq(ZERO.powHull(1), "[0, 0]");
        aeq(ZERO.powHull(2), "[0, 0]");
        aeq(ZERO.powHull(3), "[0, 0]");
        aeq(ONE.powHull(0), "[1, 1]");
        aeq(ONE.powHull(1), "[1, 1]");
        aeq(ONE.powHull(2), "[1, 1]");
        aeq(ONE.powHull(3), "[1, 1]");
        aeq(ONE.powHull(-1), "[1, 1]");
        aeq(ONE.powHull(-2), "[1, 1]");
        aeq(ONE.powHull(-3), "[1, 1]");
        aeq(ALL.powHull(0), "[1, 1]");
        aeq(ALL.powHull(1), "(-Infinity, Infinity)");
        aeq(ALL.powHull(2), "[0, Infinity)");
        aeq(ALL.powHull(3), "(-Infinity, Infinity)");
        aeq(ALL.powHull(-1), "(-Infinity, Infinity)");
        aeq(ALL.powHull(-2), "[0, Infinity)");
        aeq(ALL.powHull(-3), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().powHull(0), "[1, 1]");
        aeq(read("[-2, 5/3]").get().powHull(1), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().powHull(2), "[0, 4]");
        aeq(read("[-2, 5/3]").get().powHull(3), "[-8, 125/27]");
        aeq(read("[-2, 5/3]").get().powHull(-1), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().powHull(-2), "[1/4, Infinity)");
        aeq(read("[-2, 5/3]").get().powHull(-3), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().powHull(0), "[1, 1]");
        aeq(read("[4, 4]").get().powHull(1), "[4, 4]");
        aeq(read("[4, 4]").get().powHull(2), "[16, 16]");
        aeq(read("[4, 4]").get().powHull(3), "[64, 64]");
        aeq(read("[4, 4]").get().powHull(-1), "[1/4, 1/4]");
        aeq(read("[4, 4]").get().powHull(-2), "[1/16, 1/16]");
        aeq(read("[4, 4]").get().powHull(-3), "[1/64, 1/64]");
        aeq(read("(-Infinity, 3/2]").get().powHull(0), "[1, 1]");
        aeq(read("(-Infinity, 3/2]").get().powHull(1), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().powHull(2), "[0, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().powHull(3), "(-Infinity, 27/8]");
        aeq(read("(-Infinity, 3/2]").get().powHull(-1), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().powHull(-2), "[0, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().powHull(-3), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(0), "[1, 1]");
        aeq(read("[-6, Infinity)").get().powHull(1), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(2), "[0, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(3), "[-216, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(-1), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(-2), "[0, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(-3), "(-Infinity, Infinity)");
        try {
            ZERO.powHull(-1);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.powHull(-2);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.powHull(-3);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testElementCompare() {
        aeq(ZERO.elementCompare(ZERO), "Optional[EQ]");
        aeq(ZERO.elementCompare(ONE), "Optional[LT]");
        aeq(ZERO.elementCompare(ALL), "Optional.empty");
        aeq(ZERO.elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(ZERO.elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(ZERO.elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(ZERO.elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(ONE.elementCompare(ZERO), "Optional[GT]");
        aeq(ONE.elementCompare(ONE), "Optional[EQ]");
        aeq(ONE.elementCompare(ALL), "Optional.empty");
        aeq(ONE.elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(ONE.elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(ONE.elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(ONE.elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(ALL.elementCompare(ZERO), "Optional.empty");
        aeq(ALL.elementCompare(ONE), "Optional.empty");
        aeq(ALL.elementCompare(ALL), "Optional.empty");
        aeq(ALL.elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(ALL.elementCompare(read("[4, 4]").get()), "Optional.empty");
        aeq(ALL.elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(ALL.elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(ZERO), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(ONE), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(ALL), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(read("[-2, 5/3]").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("[4, 4]").get().elementCompare(ZERO), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(ONE), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(ALL), "Optional.empty");
        aeq(read("[4, 4]").get().elementCompare(read("[-2, 5/3]").get()), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(read("[4, 4]").get()), "Optional[EQ]");
        aeq(read("[4, 4]").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(ZERO), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(ONE), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(ALL), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(ZERO), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(ONE), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(ALL), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("[4, 4]").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readIntervalList(
                        "[[0, 0], [1, 1], (-Infinity, Infinity), [-2, 5/3], [4, 4], (-Infinity, 3/2], [-6, Infinity)]"
                ),
                readIntervalList(
                        "[[0, 0], [1, 1], (-Infinity, Infinity), [-2, 5/3], [4, 4], (-Infinity, 3/2], [-6, Infinity)]"
                )
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[0, 0]", 32);
        hashCode_helper("[1, 1]", 1024);
        hashCode_helper("(-Infinity, Infinity)", 0);
        hashCode_helper("[-2, 5/3]", -1733);
        hashCode_helper("[4, 4]", 4000);
        hashCode_helper("(-Infinity, 3/2]", 95);
        hashCode_helper("[-6, Infinity)", -5735);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readIntervalList(
                        "[(-Infinity, 3/2], (-Infinity, Infinity), [-6, Infinity), [-2, 5/3], [0, 0], [1, 1], [4, 4]]"
                )
        );
    }

    @Test
    public void testRead() {
        aeq(read("[0, 0]").get(), ZERO);
        aeq(read("[1, 1]").get(), ONE);
        aeq(read("(-Infinity, Infinity)").get(), ALL);
        aeq(read("[-2, 5/3]").get(), "[-2, 5/3]");
        aeq(read("[4, 4]").get(), "[4, 4]");
        aeq(read("(-Infinity, 3/2]").get(), "(-Infinity, 3/2]");
        aeq(read("[-6, Infinity)").get(), "[-6, Infinity)");
        assertFalse(read("").isPresent());
        assertFalse(read("[").isPresent());
        assertFalse(read("[]").isPresent());
        assertFalse(read("[,]").isPresent());
        assertFalse(read("[1, 1").isPresent());
        assertFalse(read("[12]").isPresent());
        assertFalse(read("[1 1]").isPresent());
        assertFalse(read("[1,  1]").isPresent());
        assertFalse(read("[ 1, 1]").isPresent());
        assertFalse(read("[1, 1 ]").isPresent());
        assertFalse(read("[1, 1] ").isPresent());
        assertFalse(read("[-Infinity, Infinity]").isPresent());
        assertFalse(read("(-Infinity, 4)").isPresent());
        assertFalse(read("[4, Infinity]").isPresent());
        assertFalse(read("(Infinity, -Infinity)").isPresent());
        assertFalse(read("[2, 3-]").isPresent());
        assertFalse(read("[2.0, 4]").isPresent());
        assertFalse(read("[2,4]").isPresent());
        assertFalse(read("[5, 4]").isPresent());
        assertFalse(read("[5, 4/0]").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("abcd[-5, 2/3]xyz").get(), "([-5, 2/3], 4)");
        aeq(findIn("vdfvdf(-Infinity, 3]cds").get(), "((-Infinity, 3], 6)");
        aeq(findIn("gvrgw49((-Infinity, Infinity)Infinity)").get(), "((-Infinity, Infinity), 8)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("hello").isPresent());
        assertFalse(findIn("vdfsvfbf").isPresent());
        assertFalse(findIn("vdfvds[-Infinity, 2]vsd").isPresent());
        assertFalse(findIn("vdfvds(Infinity, 2]vsd").isPresent());
        assertFalse(findIn("abcd[5, 4]xyz").isPresent());
        assertFalse(findIn("abcd[5, 4/0]xyz").isPresent());
    }

    private static void aeq(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }

    private static @NotNull List<Interval> readIntervalList(@NotNull String s) {
        return Readers.readList(Interval::read).apply(s).get();
    }

    private static @NotNull List<Interval> readIntervalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Interval::read).apply(s).get();
    }
}
