package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.RationalVector.*;
import static org.junit.Assert.*;

public class RationalVectorTest {
    @Test
    public void testConstants() {
        aeq(ZERO_DIMENSIONAL, "[]");
    }

    @Test
    public void testGetCoordinates() {
        aeq(ZERO_DIMENSIONAL.getCoordinates(), "[]");
        aeq(read("[1/2]").get(), "[1/2]");
        aeq(read("[5/3, -1/4, 23]").get(), "[5/3, -1/4, 23]");
    }

    @Test
    public void testOf_List_Rational() {
        assertTrue(of(readRationalList("[]").get()) == ZERO_DIMENSIONAL);
        aeq(of(readRationalList("[1/2]").get()), "[1/2]");
        aeq(of(readRationalList("[5/3, -1/4, 23]").get()), "[5/3, -1/4, 23]");
    }

    private static void aeq(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
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