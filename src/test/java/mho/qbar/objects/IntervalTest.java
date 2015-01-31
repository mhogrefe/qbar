package mho.qbar.objects;

import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

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
