package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalVector.*;
import static mho.wheels.iterables.IterableUtils.*;
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