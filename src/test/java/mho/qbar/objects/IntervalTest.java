package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static mho.qbar.objects.Interval.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.*;

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
        aeq(convexHull(readIntervalList("[[0, 0]]").get()), "[0, 0]");
        aeq(convexHull(readIntervalList("[[-1, 2]]").get()), "[-1, 2]");
        aeq(convexHull(readIntervalList("[[-1, Infinity)]").get()), "[-1, Infinity)");
        aeq(convexHull(readIntervalList("[(-Infinity, 4]]").get()), "(-Infinity, 4]");
        aeq(convexHull(readIntervalList("[(-Infinity, Infinity)]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(readIntervalList("[[0, 0], [1, 1]]").get()), "[0, 1]");
        aeq(convexHull(readIntervalList("[[1, 2], [3, 4]]").get()), "[1, 4]");
        aeq(convexHull(readIntervalList("[[1, 3], [2, 4]]").get()), "[1, 4]");
        aeq(convexHull(readIntervalList("[(-Infinity, Infinity), [3, 4]]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(readIntervalList("[[-1, Infinity), (-Infinity, 4]]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(readIntervalList("[[1, 2], [3, 4], [5, 6]]").get()), "[1, 6]");
        aeq(convexHull(readIntervalList("[[1, 2], [2, 2], [3, Infinity)]").get()), "[1, Infinity)");
        try {
            convexHull(readIntervalList("[]").get());
            fail();
        } catch (IllegalStateException ignored) {}
        try {
            convexHull(readIntervalListWithNulls("[[1, 2], null]").get());
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
        aeq(makeDisjoint(readIntervalList("[]").get()), "[]");
        aeq(makeDisjoint(readIntervalList("[[0, 0]]").get()), "[[0, 0]]");
        aeq(makeDisjoint(readIntervalList("[[1, 1]]").get()), "[[1, 1]]");
        aeq(makeDisjoint(readIntervalList("[(-Infinity, Infinity)]").get()), "[(-Infinity, Infinity)]");
        aeq(makeDisjoint(readIntervalList("[(-Infinity, 3]]").get()), "[(-Infinity, 3]]");
        aeq(makeDisjoint(readIntervalList("[[2, Infinity)]").get()), "[[2, Infinity)]");
        aeq(makeDisjoint(readIntervalList("[[2, 3]]").get()), "[[2, 3]]");
        aeq(
                makeDisjoint(readIntervalList("[(-Infinity, 3], [4, 5], [6, 7]]").get()),
                "[(-Infinity, 3], [4, 5], [6, 7]]"
        );
        aeq(makeDisjoint(readIntervalList("[(-Infinity, 3], [3, 5], [5, 7]]").get()), "[(-Infinity, 7]]");
        aeq(makeDisjoint(readIntervalList("[(-Infinity, 3], [2, 5], [6, 7]]").get()), "[(-Infinity, 5], [6, 7]]");
        aeq(
                makeDisjoint(readIntervalList("[[1, 2], [4, 6], [10, Infinity), [3, 7], [5, 9]]").get()),
                "[[1, 2], [3, 9], [10, Infinity)]"
        );
        try {
            makeDisjoint(readIntervalListWithNulls("[[1, 3], null, [5, Infinity)]").get());
            fail();
        } catch (NullPointerException ignored) {}
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
        aeq(roundingPreimage(Math.sqrt(2)), "[12738103345051545/9007199254740992, 12738103345051547/9007199254740992]");
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
    public void testEquals() {
        //noinspection EqualsWithItself
        assertTrue(ZERO.equals(ZERO));
        //noinspection EqualsWithItself
        assertTrue(ONE.equals(ONE));
        //noinspection EqualsWithItself
        assertTrue(ALL.equals(ALL));
        assertTrue(read("[-2, 5/3]").equals(read("[-2, 5/3]")));
        assertTrue(read("[4, 4]").equals(read("[4, 4]")));
        assertTrue(read("(-Infinity, 3/2]").equals(read("(-Infinity, 3/2]")));
        assertTrue(read("[-6, Infinity)").equals(read("[-6, Infinity)")));
        assertFalse(ZERO.equals(ONE));
        assertFalse(ZERO.equals(ALL));
        assertFalse(ZERO.equals(read("[-2, 5/3]").get()));
        assertFalse(ZERO.equals(read("[4, 4]").get()));
        assertFalse(ZERO.equals(read("(-Infinity, 3/2]").get()));
        assertFalse(ZERO.equals(read("[-6, Infinity)").get()));
        assertFalse(ONE.equals(ZERO));
        assertFalse(ONE.equals(ALL));
        assertFalse(ONE.equals(read("[-2, 5/3]").get()));
        assertFalse(ONE.equals(read("[4, 4]").get()));
        assertFalse(ONE.equals(read("(-Infinity, 3/2]").get()));
        assertFalse(ONE.equals(read("[-6, Infinity)").get()));
        assertFalse(ALL.equals(ZERO));
        assertFalse(ALL.equals(ONE));
        assertFalse(ALL.equals(read("[-2, 5/3]").get()));
        assertFalse(ALL.equals(read("[4, 4]").get()));
        assertFalse(ALL.equals(read("(-Infinity, 3/2]").get()));
        assertFalse(ALL.equals(read("[-6, Infinity)").get()));
        assertFalse(read("[-2, 5/3]").get().equals(ZERO));
        assertFalse(read("[-2, 5/3]").get().equals(ONE));
        assertFalse(read("[-2, 5/3]").get().equals(ALL));
        assertFalse(read("[-2, 5/3]").get().equals(read("[4, 4]").get()));
        assertFalse(read("[-2, 5/3]").get().equals(read("(-Infinity, 3/2]").get()));
        assertFalse(read("[-2, 5/3]").get().equals(read("[-6, Infinity)").get()));
        assertFalse(read("[4, 4]").get().equals(ZERO));
        assertFalse(read("[4, 4]").get().equals(ONE));
        assertFalse(read("[4, 4]").get().equals(ALL));
        assertFalse(read("[4, 4]").get().equals(read("[-2, 5/3]").get()));
        assertFalse(read("[4, 4]").get().equals(read("(-Infinity, 3/2]").get()));
        assertFalse(read("[4, 4]").get().equals(read("[-6, Infinity)").get()));
        assertFalse(read("(-Infinity, 3/2]").get().equals(ZERO));
        assertFalse(read("(-Infinity, 3/2]").get().equals(ONE));
        assertFalse(read("(-Infinity, 3/2]").get().equals(ALL));
        assertFalse(read("(-Infinity, 3/2]").get().equals(read("[-2, 5/3]").get()));
        assertFalse(read("(-Infinity, 3/2]").get().equals(read("[4, 4]").get()));
        assertFalse(read("(-Infinity, 3/2]").get().equals(read("[-6, Infinity)").get()));
        assertFalse(read("[-6, Infinity)").get().equals(ZERO));
        assertFalse(read("[-6, Infinity)").get().equals(ONE));
        assertFalse(read("[-6, Infinity)").get().equals(ALL));
        assertFalse(read("[-6, Infinity)").get().equals(read("[-2, 5/3]").get()));
        assertFalse(read("[-6, Infinity)").get().equals(read("[4, 4]").get()));
        assertFalse(read("[-6, Infinity)").get().equals(read("(-Infinity, 3/2]").get()));
        //noinspection ObjectEqualsNull
        assertFalse(ZERO.equals(null));
        //noinspection ObjectEqualsNull
        assertFalse(ONE.equals(null));
        //noinspection ObjectEqualsNull
        assertFalse(ALL.equals(null));
        assertTrue(read("[-2, 5/3]").isPresent());
        assertTrue(read("[4, 4]").isPresent());
        assertTrue(read("(-Infinity, 3/2]").isPresent());
        assertTrue(read("[-6, Infinity)").isPresent());
    }

    @Test
    public void testHashCode() {
        aeq(ZERO.hashCode(), 32);
        aeq(ONE.hashCode(), 1024);
        aeq(ALL.hashCode(), 0);
        aeq(read("[-2, 5/3]").hashCode(), -1733);
        aeq(read("[4, 4]").hashCode(), 4000);
        aeq(read("(-Infinity, 3/2]").hashCode(), 95);
        aeq(read("[-6, Infinity)").hashCode(), -5735);
    }

    @Test
    public void testCompareTo() {
        assertTrue(eq(ZERO, ZERO));
        assertTrue(eq(ONE, ONE));
        assertTrue(eq(ALL, ALL));
        assertTrue(eq(read("[-2, 5/3]").get(), read("[-2, 5/3]").get()));
        assertTrue(eq(read("[4, 4]").get(), read("[4, 4]").get()));
        assertTrue(eq(read("(-Infinity, 3/2]").get(), read("(-Infinity, 3/2]").get()));
        assertTrue(eq(read("[-6, Infinity)").get(), read("[-6, Infinity)").get()));
        assertTrue(lt(ZERO, ONE));
        assertTrue(gt(ZERO, ALL));
        assertTrue(gt(ZERO, read("[-2, 5/3]").get()));
        assertTrue(lt(ZERO, read("[4, 4]").get()));
        assertTrue(gt(ZERO, read("(-Infinity, 3/2]").get()));
        assertTrue(gt(ZERO, read("[-6, Infinity)").get()));
        assertTrue(gt(ONE, ZERO));
        assertTrue(gt(ONE, ALL));
        assertTrue(gt(ONE, read("[-2, 5/3]").get()));
        assertTrue(lt(ONE, read("[4, 4]").get()));
        assertTrue(gt(ONE, read("(-Infinity, 3/2]").get()));
        assertTrue(gt(ONE, read("[-6, Infinity)").get()));
        assertTrue(lt(ALL, ZERO));
        assertTrue(lt(ALL, ONE));
        assertTrue(lt(ALL, read("[-2, 5/3]").get()));
        assertTrue(lt(ALL, read("[4, 4]").get()));
        assertTrue(gt(ALL, read("(-Infinity, 3/2]").get()));
        assertTrue(lt(ALL, read("[-6, Infinity)").get()));
        assertTrue(lt(read("[-2, 5/3]").get(), ZERO));
        assertTrue(lt(read("[-2, 5/3]").get(), ONE));
        assertTrue(gt(read("[-2, 5/3]").get(), ALL));
        assertTrue(lt(read("[-2, 5/3]").get(), read("[4, 4]").get()));
        assertTrue(gt(read("[-2, 5/3]").get(), read("(-Infinity, 3/2]").get()));
        assertTrue(gt(read("[-2, 5/3]").get(), read("[-6, Infinity)").get()));
        assertTrue(gt(read("[4, 4]").get(), ZERO));
        assertTrue(gt(read("[4, 4]").get(), ONE));
        assertTrue(gt(read("[4, 4]").get(), ALL));
        assertTrue(gt(read("[4, 4]").get(), read("[-2, 5/3]").get()));
        assertTrue(gt(read("[4, 4]").get(), read("(-Infinity, 3/2]").get()));
        assertTrue(gt(read("[4, 4]").get(), read("[-6, Infinity)").get()));
        assertTrue(lt(read("(-Infinity, 3/2]").get(), ZERO));
        assertTrue(lt(read("(-Infinity, 3/2]").get(), ONE));
        assertTrue(lt(read("(-Infinity, 3/2]").get(), ALL));
        assertTrue(lt(read("(-Infinity, 3/2]").get(), read("[-2, 5/3]").get()));
        assertTrue(lt(read("(-Infinity, 3/2]").get(), read("[4, 4]").get()));
        assertTrue(lt(read("(-Infinity, 3/2]").get(), read("[-6, Infinity)").get()));
        assertTrue(lt(read("[-6, Infinity)").get(), ZERO));
        assertTrue(lt(read("[-6, Infinity)").get(), ONE));
        assertTrue(gt(read("[-6, Infinity)").get(), ALL));
        assertTrue(lt(read("[-6, Infinity)").get(), read("[-2, 5/3]").get()));
        assertTrue(lt(read("[-6, Infinity)").get(), read("[4, 4]").get()));
        assertTrue(gt(read("[-6, Infinity)").get(), read("(-Infinity, 3/2]").get()));
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

    @Test
    public void testToString() {
        aeq(ZERO, "[0, 0]");
        aeq(ONE, "[1, 1]");
        aeq(ALL, "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get(), "[-2, 5/3]");
        aeq(read("[4, 4]").get(), "[4, 4]");
        aeq(read("(-Infinity, 3/2]").get(), "(-Infinity, 3/2]");
        aeq(read("[-6, Infinity)").get(), "[-6, Infinity)");
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }

    private static @NotNull Optional<List<Interval>> readIntervalList(@NotNull String s) {
        return Readers.readList(Interval::findIn, s);
    }

    private static @NotNull Optional<List<Interval>> readIntervalListWithNulls(@NotNull String s) {
        return Readers.readList(t -> Readers.findInWithNulls(Interval::findIn, t), s);
    }
}
