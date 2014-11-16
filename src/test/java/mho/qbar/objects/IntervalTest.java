package mho.qbar.objects;

import mho.wheels.ordering.Ordering;
import org.junit.Assert;
import org.junit.Test;

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
    public void testContains() {
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
    public void testDiameter() {
        aeq(ZERO.diameter(), "0");
        aeq(ONE.diameter(), "0");
        assertNull(ALL.diameter());
        aeq(read("[-2, 5/3]").get().diameter(), "11/3");
        aeq(read("[4, 4]").get().diameter(), "0");
        assertNull(read("(-Infinity, 3/2]").get().diameter());
        assertNull(read("[-6, Infinity)").get().diameter());
    }

    @Test
    public void testConvexHull_Interval_Interval() {
        aeq(convexHull(ZERO, ZERO), "[0, 0]");
        aeq(convexHull(ZERO, ONE), "[0, 1]");
        aeq(convexHull(ZERO, ALL), "(-Infinity, Infinity)");
        aeq(convexHull(ZERO, read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(convexHull(ZERO, read("[4, 4]").get()), "[0, 4]");
        aeq(convexHull(ZERO, read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(convexHull(ZERO, read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(convexHull(ONE, ZERO), "[0, 1]");
        aeq(convexHull(ONE, ONE), "[1, 1]");
        aeq(convexHull(ONE, ALL), "(-Infinity, Infinity)");
        aeq(convexHull(ONE, read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(convexHull(ONE, read("[4, 4]").get()), "[1, 4]");
        aeq(convexHull(ONE, read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(convexHull(ONE, read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(convexHull(ALL, ZERO), "(-Infinity, Infinity)");
        aeq(convexHull(ALL, ONE), "(-Infinity, Infinity)");
        aeq(convexHull(ALL, ALL), "(-Infinity, Infinity)");
        aeq(convexHull(ALL, read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(ALL, read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(ALL, read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(ALL, read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(convexHull(read("[-2, 5/3]").get(), ZERO), "[-2, 5/3]");
        aeq(convexHull(read("[-2, 5/3]").get(), ONE), "[-2, 5/3]");
        aeq(convexHull(read("[-2, 5/3]").get(), ALL), "(-Infinity, Infinity)");
        aeq(convexHull(read("[-2, 5/3]").get(), read("[-2, 5/3]").get()), "[-2, 5/3]");
        aeq(convexHull(read("[-2, 5/3]").get(), read("[4, 4]").get()), "[-2, 4]");
        aeq(convexHull(read("[-2, 5/3]").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, 5/3]");
        aeq(convexHull(read("[-2, 5/3]").get(), read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(convexHull(read("[4, 4]").get(), ZERO), "[0, 4]");
        aeq(convexHull(read("[4, 4]").get(), ONE), "[1, 4]");
        aeq(convexHull(read("[4, 4]").get(), ALL), "(-Infinity, Infinity)");
        aeq(convexHull(read("[4, 4]").get(), read("[-2, 5/3]").get()), "[-2, 4]");
        aeq(convexHull(read("[4, 4]").get(), read("[4, 4]").get()), "[4, 4]");
        aeq(convexHull(read("[4, 4]").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, 4]");
        aeq(convexHull(read("[4, 4]").get(), read("[-6, Infinity)").get()), "[-6, Infinity)");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), ZERO), "(-Infinity, 3/2]");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), ONE), "(-Infinity, 3/2]");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), ALL), "(-Infinity, Infinity)");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), read("[-2, 5/3]").get()), "(-Infinity, 5/3]");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), read("[4, 4]").get()), "(-Infinity, 4]");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        aeq(convexHull(read("(-Infinity, 3/2]").get(), read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), ZERO), "[-6, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), ONE), "[-6, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), ALL), "(-Infinity, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), read("[-2, 5/3]").get()), "[-6, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), read("[4, 4]").get()), "[-6, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(convexHull(read("[-6, Infinity)").get(), read("[-6, Infinity)").get()), "[-6, Infinity)");
    }

    @Test
    public void testConvexHull_SortedSet() {
        SortedSet<Interval> ss;
        ss = new TreeSet<>();
        ss.add(ZERO);
        aeq(convexHull(ss), "[0, 0]");
        ss = new TreeSet<>();
        ss.add(read("[-1, 2]").get());
        aeq(convexHull(ss), "[-1, 2]");
        ss = new TreeSet<>();
        ss.add(read("[-1, Infinity)").get());
        aeq(convexHull(ss), "[-1, Infinity)");
        ss = new TreeSet<>();
        ss.add(read("(-Infinity, 4]").get());
        aeq(convexHull(ss), "(-Infinity, 4]");
        ss = new TreeSet<>();
        ss.add(ALL);
        aeq(convexHull(ss), "(-Infinity, Infinity)");
        ss = new TreeSet<>();
        ss.add(ZERO);
        ss.add(ONE);
        aeq(convexHull(ss), "[0, 1]");
        ss = new TreeSet<>();
        ss.add(read("[1, 2]").get());
        ss.add(read("[3, 4]").get());
        aeq(convexHull(ss), "[1, 4]");
        ss = new TreeSet<>();
        ss.add(read("[1, 3]").get());
        ss.add(read("[2, 4]").get());
        aeq(convexHull(ss), "[1, 4]");
        ss = new TreeSet<>();
        ss.add(ALL);
        ss.add(read("[3, 4]").get());
        aeq(convexHull(ss), "(-Infinity, Infinity)");
        ss = new TreeSet<>();
        ss.add(read("[-1, Infinity)").get());
        ss.add(read("(-Infinity, 4]").get());
        aeq(convexHull(ss), "(-Infinity, Infinity)");
        ss = new TreeSet<>();
        ss.add(read("[1, 2]").get());
        ss.add(read("[3, 4]").get());
        ss.add(read("[5, 6]").get());
        aeq(convexHull(ss), "[1, 6]");
        ss = new TreeSet<>();
        ss.add(read("[1, 2]").get());
        ss.add(read("[2, 2]").get());
        ss.add(read("[3, Infinity)").get());
        aeq(convexHull(ss), "[1, Infinity)");
        try {
            ss = new TreeSet<>();
            convexHull(ss);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEquals() {
        assertTrue(ZERO.equals(ZERO));
        assertTrue(ONE.equals(ONE));
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
        assertFalse(ZERO.equals(null));
        assertFalse(ONE.equals(null));
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
        try {
            read("[5, 4]");
            fail();
        } catch (IllegalArgumentException ignored) {}
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
}
