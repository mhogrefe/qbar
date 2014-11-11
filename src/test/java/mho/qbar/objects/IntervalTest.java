package mho.qbar.objects;

import org.junit.Assert;
import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static mho.qbar.objects.Interval.*;

public class IntervalTest {
    @Test
    public void testConstants() {
        eq(ZERO, "[0, 0]");
        eq(ONE, "[1, 1]");
        eq(ALL, "(-Infinity, Infinity)");
    }

    @Test
    public void testOf_Rational_Rational() {
        eq(of(Rational.read("1/3").get(), Rational.read("1/2").get()), "[1/3, 1/2]");
        eq(of(Rational.read("-5").get(), Rational.ZERO), "[-5, 0]");
        eq(of(Rational.read("2").get(), Rational.read("2").get()), "[2, 2]");
        try {
            of(Rational.read("3").get(), Rational.read("2").get());
            Assert.fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testLessThanOrEqualTo() {
        eq(lessThanOrEqualTo(Rational.read("1/3").get()), "(-Infinity, 1/3]");
        eq(lessThanOrEqualTo(Rational.ZERO), "(-Infinity, 0]");
    }

    @Test
    public void testGreaterThanOrEqualTo() {
        eq(greaterThanOrEqualTo(Rational.read("1/3").get()), "[1/3, Infinity)");
        eq(greaterThanOrEqualTo(Rational.ZERO), "[0, Infinity)");
    }

    @Test
    public void testOf_Rational() {
        eq(of(Rational.ZERO), "[0, 0]");
        eq(of(Rational.read("5/4").get()), "[5/4, 5/4]");
        eq(of(Rational.read("-2").get()), "[-2, -2]");
    }

    @Test
    public void testIsFinitelyBounded() {
        Assert.assertTrue(ZERO.isFinitelyBounded());
        Assert.assertTrue(ONE.isFinitelyBounded());
        Assert.assertFalse(ALL.isFinitelyBounded());
        Assert.assertTrue(read("[-2, 5/3]").get().isFinitelyBounded());
        Assert.assertTrue(read("[4, 4]").get().isFinitelyBounded());
        Assert.assertFalse(read("(-Infinity, 3/2]").get().isFinitelyBounded());
        Assert.assertFalse(read("[-6, Infinity)").get().isFinitelyBounded());
    }

    @Test
    public void testContains() {
        Assert.assertTrue(ZERO.contains(Rational.ZERO));
        Assert.assertFalse(ZERO.contains(Rational.ONE));
        Assert.assertTrue(ONE.contains(Rational.ONE));
        Assert.assertFalse(ONE.contains(Rational.ZERO));
        Assert.assertTrue(ALL.contains(Rational.ZERO));
        Assert.assertTrue(ALL.contains(Rational.ONE));
        Assert.assertTrue(ALL.contains(Rational.read("-4/3").get()));
        Assert.assertTrue(read("[-2, 5/3]").get().contains(Rational.read("-2").get()));
        Assert.assertTrue(read("[-2, 5/3]").get().contains(Rational.read("-1").get()));
        Assert.assertTrue(read("[-2, 5/3]").get().contains(Rational.ZERO));
        Assert.assertTrue(read("[-2, 5/3]").get().contains(Rational.ONE));
        Assert.assertTrue(read("[-2, 5/3]").get().contains(Rational.read("5/3").get()));
        Assert.assertFalse(read("[-2, 5/3]").get().contains(Rational.read("-3").get()));
        Assert.assertFalse(read("[-2, 5/3]").get().contains(Rational.read("2").get()));
        Assert.assertTrue(read("[4, 4]").get().contains(Rational.read("4").get()));
        Assert.assertFalse(read("[4, 4]").get().contains(Rational.read("3").get()));
        Assert.assertFalse(read("[4, 4]").get().contains(Rational.read("5").get()));
        Assert.assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.ZERO));
        Assert.assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.ONE));
        Assert.assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.read("-10").get()));
        Assert.assertTrue(read("(-Infinity, 3/2]").get().contains(Rational.read("3/2").get()));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().contains(Rational.read("2").get()));
        Assert.assertTrue(read("[-6, Infinity)").get().contains(Rational.ZERO));
        Assert.assertTrue(read("[-6, Infinity)").get().contains(Rational.ONE));
        Assert.assertTrue(read("[-6, Infinity)").get().contains(Rational.read("-4").get()));
        Assert.assertTrue(read("[-6, Infinity)").get().contains(Rational.read("5").get()));
        Assert.assertFalse(read("[-6, Infinity)").get().contains(Rational.read("-8").get()));
    }

    @Test
    public void testDiameter() {
        eq(ZERO.diameter(), "0");
        eq(ONE.diameter(), "0");
        Assert.assertNull(ALL.diameter());
        eq(read("[-2, 5/3]").get().diameter(), "11/3");
        eq(read("[4, 4]").get().diameter(), "0");
        Assert.assertNull(read("(-Infinity, 3/2]").get().diameter());
        Assert.assertNull(read("[-6, Infinity)").get().diameter());
    }

    @Test
    public void testConvexHull_Interval_Interval() {
        eq(convexHull(ZERO, ZERO), "[0, 0]");
        eq(convexHull(ZERO, ONE), "[0, 1]");
        eq(convexHull(ZERO, ALL), "(-Infinity, Infinity)");
        eq(convexHull(ZERO, read("[-2, 5/3]").get()), "[-2, 5/3]");
        eq(convexHull(ZERO, read("[4, 4]").get()), "[0, 4]");
        eq(convexHull(ZERO, read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        eq(convexHull(ZERO, read("[-6, Infinity)").get()), "[-6, Infinity)");
        eq(convexHull(ONE, ZERO), "[0, 1]");
        eq(convexHull(ONE, ONE), "[1, 1]");
        eq(convexHull(ONE, ALL), "(-Infinity, Infinity)");
        eq(convexHull(ONE, read("[-2, 5/3]").get()), "[-2, 5/3]");
        eq(convexHull(ONE, read("[4, 4]").get()), "[1, 4]");
        eq(convexHull(ONE, read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        eq(convexHull(ONE, read("[-6, Infinity)").get()), "[-6, Infinity)");
        eq(convexHull(ALL, ZERO), "(-Infinity, Infinity)");
        eq(convexHull(ALL, ONE), "(-Infinity, Infinity)");
        eq(convexHull(ALL, ALL), "(-Infinity, Infinity)");
        eq(convexHull(ALL, read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        eq(convexHull(ALL, read("[4, 4]").get()), "(-Infinity, Infinity)");
        eq(convexHull(ALL, read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        eq(convexHull(ALL, read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        eq(convexHull(read("[-2, 5/3]").get(), ZERO), "[-2, 5/3]");
        eq(convexHull(read("[-2, 5/3]").get(), ONE), "[-2, 5/3]");
        eq(convexHull(read("[-2, 5/3]").get(), ALL), "(-Infinity, Infinity)");
        eq(convexHull(read("[-2, 5/3]").get(), read("[-2, 5/3]").get()), "[-2, 5/3]");
        eq(convexHull(read("[-2, 5/3]").get(), read("[4, 4]").get()), "[-2, 4]");
        eq(convexHull(read("[-2, 5/3]").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, 5/3]");
        eq(convexHull(read("[-2, 5/3]").get(), read("[-6, Infinity)").get()), "[-6, Infinity)");
        eq(convexHull(read("[4, 4]").get(), ZERO), "[0, 4]");
        eq(convexHull(read("[4, 4]").get(), ONE), "[1, 4]");
        eq(convexHull(read("[4, 4]").get(), ALL), "(-Infinity, Infinity)");
        eq(convexHull(read("[4, 4]").get(), read("[-2, 5/3]").get()), "[-2, 4]");
        eq(convexHull(read("[4, 4]").get(), read("[4, 4]").get()), "[4, 4]");
        eq(convexHull(read("[4, 4]").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, 4]");
        eq(convexHull(read("[4, 4]").get(), read("[-6, Infinity)").get()), "[-6, Infinity)");
        eq(convexHull(read("(-Infinity, 3/2]").get(), ZERO), "(-Infinity, 3/2]");
        eq(convexHull(read("(-Infinity, 3/2]").get(), ONE), "(-Infinity, 3/2]");
        eq(convexHull(read("(-Infinity, 3/2]").get(), ALL), "(-Infinity, Infinity)");
        eq(convexHull(read("(-Infinity, 3/2]").get(), read("[-2, 5/3]").get()), "(-Infinity, 5/3]");
        eq(convexHull(read("(-Infinity, 3/2]").get(), read("[4, 4]").get()), "(-Infinity, 4]");
        eq(convexHull(read("(-Infinity, 3/2]").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, 3/2]");
        eq(convexHull(read("(-Infinity, 3/2]").get(), read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), ZERO), "[-6, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), ONE), "[-6, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), ALL), "(-Infinity, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), read("[-2, 5/3]").get()), "[-6, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), read("[4, 4]").get()), "[-6, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        eq(convexHull(read("[-6, Infinity)").get(), read("[-6, Infinity)").get()), "[-6, Infinity)");
    }

    @Test
    public void testConvexHull_SortedSet() {
        SortedSet<Interval> ss;
        ss = new TreeSet<>();
        ss.add(ZERO);
        eq(convexHull(ss), "[0, 0]");
        ss = new TreeSet<>();
        ss.add(read("[-1, 2]").get());
        eq(convexHull(ss), "[-1, 2]");
        ss = new TreeSet<>();
        ss.add(read("[-1, Infinity)").get());
        eq(convexHull(ss), "[-1, Infinity)");
        ss = new TreeSet<>();
        ss.add(read("(-Infinity, 4]").get());
        eq(convexHull(ss), "(-Infinity, 4]");
        ss = new TreeSet<>();
        ss.add(ALL);
        eq(convexHull(ss), "(-Infinity, Infinity)");
        ss = new TreeSet<>();
        ss.add(ZERO);
        ss.add(ONE);
        eq(convexHull(ss), "[0, 1]");
        ss = new TreeSet<>();
        ss.add(read("[1, 2]").get());
        ss.add(read("[3, 4]").get());
        eq(convexHull(ss), "[1, 4]");
        ss = new TreeSet<>();
        ss.add(read("[1, 3]").get());
        ss.add(read("[2, 4]").get());
        eq(convexHull(ss), "[1, 4]");
        ss = new TreeSet<>();
        ss.add(ALL);
        ss.add(read("[3, 4]").get());
        eq(convexHull(ss), "(-Infinity, Infinity)");
        ss = new TreeSet<>();
        ss.add(read("[-1, Infinity)").get());
        ss.add(read("(-Infinity, 4]").get());
        eq(convexHull(ss), "(-Infinity, Infinity)");
        ss = new TreeSet<>();
        ss.add(read("[1, 2]").get());
        ss.add(read("[3, 4]").get());
        ss.add(read("[5, 6]").get());
        eq(convexHull(ss), "[1, 6]");
        ss = new TreeSet<>();
        ss.add(read("[1, 2]").get());
        ss.add(read("[2, 2]").get());
        ss.add(read("[3, Infinity)").get());
        eq(convexHull(ss), "[1, Infinity)");
        try {
            ss = new TreeSet<>();
            convexHull(ss);
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testEquals() {
        Assert.assertTrue(ZERO.equals(ZERO));
        Assert.assertTrue(ONE.equals(ONE));
        Assert.assertTrue(ALL.equals(ALL));
        Assert.assertTrue(read("[-2, 5/3]").equals(read("[-2, 5/3]")));
        Assert.assertTrue(read("[4, 4]").equals(read("[4, 4]")));
        Assert.assertTrue(read("(-Infinity, 3/2]").equals(read("(-Infinity, 3/2]")));
        Assert.assertTrue(read("[-6, Infinity)").equals(read("[-6, Infinity)")));
        Assert.assertFalse(ZERO.equals(ONE));
        Assert.assertFalse(ZERO.equals(ALL));
        Assert.assertFalse(ZERO.equals(read("[-2, 5/3]").get()));
        Assert.assertFalse(ZERO.equals(read("[4, 4]").get()));
        Assert.assertFalse(ZERO.equals(read("(-Infinity, 3/2]").get()));
        Assert.assertFalse(ZERO.equals(read("[-6, Infinity)").get()));
        Assert.assertFalse(ONE.equals(ZERO));
        Assert.assertFalse(ONE.equals(ALL));
        Assert.assertFalse(ONE.equals(read("[-2, 5/3]").get()));
        Assert.assertFalse(ONE.equals(read("[4, 4]").get()));
        Assert.assertFalse(ONE.equals(read("(-Infinity, 3/2]").get()));
        Assert.assertFalse(ONE.equals(read("[-6, Infinity)").get()));
        Assert.assertFalse(ALL.equals(ZERO));
        Assert.assertFalse(ALL.equals(ONE));
        Assert.assertFalse(ALL.equals(read("[-2, 5/3]").get()));
        Assert.assertFalse(ALL.equals(read("[4, 4]").get()));
        Assert.assertFalse(ALL.equals(read("(-Infinity, 3/2]").get()));
        Assert.assertFalse(ALL.equals(read("[-6, Infinity)").get()));
        Assert.assertFalse(read("[-2, 5/3]").get().equals(ZERO));
        Assert.assertFalse(read("[-2, 5/3]").get().equals(ONE));
        Assert.assertFalse(read("[-2, 5/3]").get().equals(ALL));
        Assert.assertFalse(read("[-2, 5/3]").get().equals(read("[4, 4]").get()));
        Assert.assertFalse(read("[-2, 5/3]").get().equals(read("(-Infinity, 3/2]").get()));
        Assert.assertFalse(read("[-2, 5/3]").get().equals(read("[-6, Infinity)").get()));
        Assert.assertFalse(read("[4, 4]").get().equals(ZERO));
        Assert.assertFalse(read("[4, 4]").get().equals(ONE));
        Assert.assertFalse(read("[4, 4]").get().equals(ALL));
        Assert.assertFalse(read("[4, 4]").get().equals(read("[-2, 5/3]").get()));
        Assert.assertFalse(read("[4, 4]").get().equals(read("(-Infinity, 3/2]").get()));
        Assert.assertFalse(read("[4, 4]").get().equals(read("[-6, Infinity)").get()));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().equals(ZERO));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().equals(ONE));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().equals(ALL));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().equals(read("[-2, 5/3]").get()));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().equals(read("[4, 4]").get()));
        Assert.assertFalse(read("(-Infinity, 3/2]").get().equals(read("[-6, Infinity)").get()));
        Assert.assertFalse(read("[-6, Infinity)").get().equals(ZERO));
        Assert.assertFalse(read("[-6, Infinity)").get().equals(ONE));
        Assert.assertFalse(read("[-6, Infinity)").get().equals(ALL));
        Assert.assertFalse(read("[-6, Infinity)").get().equals(read("[-2, 5/3]").get()));
        Assert.assertFalse(read("[-6, Infinity)").get().equals(read("[4, 4]").get()));
        Assert.assertFalse(read("[-6, Infinity)").get().equals(read("(-Infinity, 3/2]").get()));
        Assert.assertFalse(ZERO.equals(null));
        Assert.assertFalse(ONE.equals(null));
        Assert.assertFalse(ALL.equals(null));
        Assert.assertTrue(read("[-2, 5/3]").isPresent());
        Assert.assertTrue(read("[4, 4]").isPresent());
        Assert.assertTrue(read("(-Infinity, 3/2]").isPresent());
        Assert.assertTrue(read("[-6, Infinity)").isPresent());
    }

    @Test
    public void testHashCode() {
        eq(ZERO.hashCode(), 32);
        eq(ONE.hashCode(), 1024);
        eq(ALL.hashCode(), 0);
        eq(read("[-2, 5/3]").hashCode(), -1733);
        eq(read("[4, 4]").hashCode(), 4000);
        eq(read("(-Infinity, 3/2]").hashCode(), 95);
        eq(read("[-6, Infinity)").hashCode(), -5735);
    }

//    @Test
//    public void testCompareTo() {
//        eq(ZERO.compareTo(ZERO), 0);
//        eq(ONE.compareTo(ONE), 0);
//        eq(ALL.compareTo(ALL), 0);
//        eq(read("[-2, 5/3]").compareTo(read("[-2, 5/3]")), 0);
//        eq(read("[4, 4]").compareTo(read("[4, 4]")), 0);
//        eq(read("(-Infinity, 3/2]").compareTo(read("(-Infinity, 3/2]")), 0);
//        eq(read("[-6, Infinity)").compareTo(read("[-6, Infinity)")), 0);
//        eq(ZERO.compareTo(ONE), -1);
//        eq(ZERO.compareTo(ALL), 1);
//        eq(ZERO.compareTo(read("[-2, 5/3]")), 1);
//        eq(ZERO.compareTo(read("[4, 4]")), -1);
//        eq(ZERO.compareTo(read("(-Infinity, 3/2]")), 1);
//        eq(ZERO.compareTo(read("[-6, Infinity)")), 1);
//        eq(ONE.compareTo(ZERO), 1);
//        eq(ONE.compareTo(ALL), 1);
//        eq(ONE.compareTo(read("[-2, 5/3]")), 1);
//        eq(ONE.compareTo(read("[4, 4]")), -1);
//        eq(ONE.compareTo(read("(-Infinity, 3/2]")), 1);
//        eq(ONE.compareTo(read("[-6, Infinity)")), 1);
//        eq(ALL.compareTo(ZERO), -1);
//        eq(ALL.compareTo(ONE), -1);
//        eq(ALL.compareTo(read("[-2, 5/3]")), -1);
//        eq(ALL.compareTo(read("[4, 4]")), -1);
//        eq(ALL.compareTo(read("(-Infinity, 3/2]")), 1);
//        eq(ALL.compareTo(read("[-6, Infinity)")), -1);
//        eq(read("[-2, 5/3]").compareTo(ZERO), -1);
//        eq(read("[-2, 5/3]").compareTo(ONE), -1);
//        eq(read("[-2, 5/3]").compareTo(ALL), 1);
//        eq(read("[-2, 5/3]").compareTo(read("[4, 4]")), -1);
//        eq(read("[-2, 5/3]").compareTo(read("(-Infinity, 3/2]")), 1);
//        eq(read("[-2, 5/3]").compareTo(read("[-6, Infinity)")), 1);
//        eq(read("[4, 4]").compareTo(ZERO), 1);
//        eq(read("[4, 4]").compareTo(ONE), 1);
//        eq(read("[4, 4]").compareTo(ALL), 1);
//        eq(read("[4, 4]").compareTo(read("[-2, 5/3]")), 1);
//        eq(read("[4, 4]").compareTo(read("(-Infinity, 3/2]")), 1);
//        eq(read("[4, 4]").compareTo(read("[-6, Infinity)")), 1);
//        eq(read("(-Infinity, 3/2]").compareTo(ZERO), -1);
//        eq(read("(-Infinity, 3/2]").compareTo(ONE), -1);
//        eq(read("(-Infinity, 3/2]").compareTo(ALL), -1);
//        eq(read("(-Infinity, 3/2]").compareTo(read("[-2, 5/3]")), -1);
//        eq(read("(-Infinity, 3/2]").compareTo(read("[4, 4]")), -1);
//        eq(read("(-Infinity, 3/2]").compareTo(read("[-6, Infinity)")), -1);
//        eq(read("[-6, Infinity)").compareTo(ZERO), -1);
//        eq(read("[-6, Infinity)").compareTo(ONE), -1);
//        eq(read("[-6, Infinity)").compareTo(ALL), 1);
//        eq(read("[-6, Infinity)").compareTo(read("[-2, 5/3]")), -1);
//        eq(read("[-6, Infinity)").compareTo(read("[4, 4]")), -1);
//        eq(read("[-6, Infinity)").compareTo(read("(-Infinity, 3/2]")), 1);
//    }

    @Test
    public void testRead() {
        eq(read("[0, 0]").get(), ZERO);
        eq(read("[1, 1]").get(), ONE);
        eq(read("(-Infinity, Infinity)").get(), ALL);
        eq(read("[-2, 5/3]").get(), "[-2, 5/3]");
        eq(read("[4, 4]").get(), "[4, 4]");
        eq(read("(-Infinity, 3/2]").get(), "(-Infinity, 3/2]");
        eq(read("[-6, Infinity)").get(), "[-6, Infinity)");
        try {
            read("[5, 4]");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {}
        Assert.assertFalse(read("").isPresent());
        Assert.assertFalse(read("[").isPresent());
        Assert.assertFalse(read("[]").isPresent());
        Assert.assertFalse(read("[,]").isPresent());
        Assert.assertFalse(read("[1, 1").isPresent());
        Assert.assertFalse(read("[12]").isPresent());
        Assert.assertFalse(read("[1 1]").isPresent());
        Assert.assertFalse(read("[1,  1]").isPresent());
        Assert.assertFalse(read("[ 1, 1]").isPresent());
        Assert.assertFalse(read("[1, 1 ]").isPresent());
        Assert.assertFalse(read("[1, 1] ").isPresent());
        Assert.assertFalse(read("[-Infinity, Infinity]").isPresent());
        Assert.assertFalse(read("(-Infinity, 4)").isPresent());
        Assert.assertFalse(read("[4, Infinity]").isPresent());
        Assert.assertFalse(read("(Infinity, -Infinity)").isPresent());
        Assert.assertFalse(read("[2, 3-]").isPresent());
        Assert.assertFalse(read("[2.0, 4]").isPresent());
        Assert.assertFalse(read("[2,4]").isPresent());
    }

    @Test
    public void toStringTest() {
        eq(ZERO, "[0, 0]");
        eq(ONE, "[1, 1]");
        eq(ALL, "(-Infinity, Infinity)");
        eq(read("[-2, 5/3]").get(), "[-2, 5/3]");
        eq(read("[4, 4]").get(), "[4, 4]");
        eq(read("(-Infinity, 3/2]").get(), "(-Infinity, 3/2]");
        eq(read("[-6, Infinity)").get(), "[-6, Infinity)");
    }

    private static void eq(Object a, Object b) {
        Assert.assertEquals(a.toString(), b.toString());
    }
}
