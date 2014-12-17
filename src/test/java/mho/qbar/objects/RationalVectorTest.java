package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.Readers;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.*;

public class RationalVectorTest {
    @Test
    public void testConstants() {
        aeq(ZERO_DIMENSIONAL, "[]");
    }

    @Test
    public void testIterator() {
        aeq(toList(ZERO_DIMENSIONAL), "[]");
        aeq(toList(read("[1/2]").get()), "[1/2]");
        aeq(toList(read("[5/3, -1/4, 23]").get()), "[5/3, -1/4, 23]");
    }

    @Test
    public void testGetX() {
        aeq(read("[1/2]").get().x(), "1/2");
        aeq(read("[5/3, -1/4, 23]").get().x(), "5/3");
        try {
            read("[]").get().x();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testGetY() {
        aeq(read("[5/3, -1/4, 23]").get().y(), "-1/4");
        try {
            read("[1/2]").get().y();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            read("[]").get().y();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testGetZ() {
        aeq(read("[5/3, -1/4, 23]").get().z(), "23");
        try {
            read("[1/2]").get().z();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            read("[]").get().z();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testGetW() {
        aeq(read("[5/3, -1/4, 23, 58/7]").get().w(), "58/7");
        try {
            read("[5/3, -1/4, 23]").get().w();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            read("[1/2]").get().w();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            read("[]").get().w();
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testGetCoordinate() {
        aeq(read("[1/2]").get().x(0), "1/2");
        aeq(read("[5/3, -1/4, 23]").get().x(0), "5/3");
        aeq(read("[5/3, -1/4, 23]").get().x(1), "-1/4");
        aeq(read("[5/3, -1/4, 23]").get().x(2), "23");
        try {
            read("[5/3, -1/4, 23]").get().x(4);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            read("[1/2]").get().x(3);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            read("[]").get().x(0);
            fail();
        } catch (IndexOutOfBoundsException ignored) {}
    }

    @Test
    public void testOf_List_Rational() {
        assertTrue(of(readRationalList("[]").get()) == ZERO_DIMENSIONAL);
        aeq(of(readRationalList("[1/2]").get()), "[1/2]");
        aeq(of(readRationalList("[5/3, -1/4, 23]").get()), "[5/3, -1/4, 23]");
        try {
            of(readRationalListWithNulls("[5/3, null, 23]").get());
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void test_Rational() {
        aeq(of(Rational.read("1/2").get()), "[1/2]");
        aeq(of(Rational.read("-5").get()), "[-5]");
        aeq(of(Rational.read("0").get()), "[0]");
    }

    @Test
    public void testDimension() {
        aeq(read("[]").get().dimension(), 0);
        aeq(read("[1/2]").get().dimension(), 1);
        aeq(read("[5/3, -1/4, 23]").get().dimension(), 3);
    }

    @Test
    public void testCompareTo() {
        assertTrue(eq(ZERO_DIMENSIONAL, ZERO_DIMENSIONAL));
        assertTrue(lt(ZERO_DIMENSIONAL, read("[1/2]").get()));
        assertTrue(lt(ZERO_DIMENSIONAL, read("[5/3, -1/4, 23]").get()));
        assertTrue(lt(ZERO_DIMENSIONAL, read("[5/3, 1/4, 23]").get()));
        assertTrue(gt(read("[1/2]").get(), ZERO_DIMENSIONAL));
        assertTrue(eq(read("[1/2]").get(), read("[1/2]").get()));
        assertTrue(lt(read("[1/2]").get(), read("[5/3, -1/4, 23]").get()));
        assertTrue(lt(read("[1/2]").get(), read("[5/3, 1/4, 23]").get()));
        assertTrue(gt(read("[5/3, -1/4, 23]").get(), ZERO_DIMENSIONAL));
        assertTrue(gt(read("[5/3, -1/4, 23]").get(), read("[1/2]").get()));
        assertTrue(eq(read("[5/3, -1/4, 23]").get(), read("[5/3, -1/4, 23]").get()));
        assertTrue(lt(read("[5/3, -1/4, 23]").get(), read("[5/3, 1/4, 23]").get()));
        assertTrue(gt(read("[5/3, 1/4, 23]").get(), ZERO_DIMENSIONAL));
        assertTrue(gt(read("[5/3, 1/4, 23]").get(), read("[1/2]").get()));
        assertTrue(gt(read("[5/3, 1/4, 23]").get(), read("[5/3, -1/4, 23]").get()));
        assertTrue(eq(read("[5/3, 1/4, 23]").get(), read("[5/3, 1/4, 23]").get()));
    }

    @Test
    public void testEquals() {
        //noinspection EqualsWithItself
        assertTrue(ZERO_DIMENSIONAL.equals(ZERO_DIMENSIONAL));
        assertFalse(ZERO_DIMENSIONAL.equals(read("[1/2]").get()));
        assertFalse(ZERO_DIMENSIONAL.equals(read("[5/3, -1/4, 23]").get()));
        assertFalse(ZERO_DIMENSIONAL.equals(read("[5/3, 1/4, 23]").get()));
        assertFalse(read("[1/2]").get().equals(ZERO_DIMENSIONAL));
        assertTrue(read("[1/2]").get().equals(read("[1/2]").get()));
        assertFalse(read("[1/2]").get().equals(read("[5/3, -1/4, 23]").get()));
        assertFalse(read("[1/2]").get().equals(read("[5/3, 1/4, 23]").get()));
        assertFalse(read("[5/3, -1/4, 23]").get().equals(ZERO_DIMENSIONAL));
        assertFalse(read("[5/3, -1/4, 23]").get().equals(read("[1/2]").get()));
        assertTrue(read("[5/3, -1/4, 23]").get().equals(read("[5/3, -1/4, 23]").get()));
        assertFalse(read("[5/3, -1/4, 23]").get().equals(read("[5/3, 1/4, 23]").get()));
        assertFalse(read("[5/3, 1/4, 23]").get().equals(ZERO_DIMENSIONAL));
        assertFalse(read("[5/3, 1/4, 23]").get().equals(read("[1/2]").get()));
        assertFalse(read("[5/3, 1/4, 23]").get().equals(read("[5/3, -1/4, 23]").get()));
        assertTrue(read("[5/3, 1/4, 23]").get().equals(read("[5/3, 1/4, 23]").get()));
    }

    @Test
    public void testHashCode() {
        aeq(ZERO_DIMENSIONAL.hashCode(), 1);
        aeq(read("[1/2]").get().hashCode(), 64);
        aeq(read("[5/3, -1/4, 23]").get().hashCode(), 181506);
        aeq(read("[5/3, 1/4, 23]").get().hashCode(), 183428);
    }

    @Test
    public void testRead() {
        assertTrue(read("[]").get() == ZERO_DIMENSIONAL);
        aeq(read("[1/2]").get(), "[1/2]");
        aeq(read("[0, -23/4, 7/8]").get(), "[0, -23/4, 7/8]");
        assertFalse(read("").isPresent());
        assertFalse(read("[ 1/2]").isPresent());
        assertFalse(read("[1/3, 2/4]").isPresent());
        assertFalse(read("[1/3, 2/0]").isPresent());
        assertFalse(read("hello").isPresent());
        assertFalse(read("][").isPresent());
        assertFalse(read("1/2, 2/3").isPresent());
        assertFalse(read("vfbdb ds").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("fr24rev[]evfre").get(), "([], 7)");
        assertTrue(findIn("fr24rev[]evfre").get().a == ZERO_DIMENSIONAL);
        aeq(findIn("]]][[3/4, 45/7][]dsvdf").get(), "([3/4, 45/7], 4)");
        aeq(findIn("]]][[3/4, 45/0][]dsvdf").get(), "([], 15)");
        aeq(findIn("]]][[3/4, 45/3][]dsvdf").get(), "([], 15)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("]]][[3/4, 45/0]dsvdf").isPresent());
        assertFalse(findIn("]]][[3/4, 2/4]dsvdf").isPresent());
        assertFalse(findIn("hello").isPresent());
    }

    @Test
    public void testToString() {
        aeq(ZERO_DIMENSIONAL, "[]");
        aeq(of(Arrays.asList(Rational.of(1, 2))), "[1/2]");
        aeq(of(Arrays.asList(Rational.of(5, 3), Rational.of(-1, 4), Rational.of(23))), "[5/3, -1/4, 23]");
        aeq(of(Arrays.asList(Rational.of(5, 3), Rational.of(1, 4), Rational.of(23))), "[5/3, 1/4, 23]");
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }

    private static @NotNull Optional<List<Rational>> readRationalList(@NotNull String s) {
        return Readers.readList(Rational::findIn, s);
    }

    private static @NotNull Optional<List<Rational>> readRationalListWithNulls(@NotNull String s) {
        return Readers.readList(t -> Readers.findInWithNulls(Rational::findIn, t), s);
    }
}