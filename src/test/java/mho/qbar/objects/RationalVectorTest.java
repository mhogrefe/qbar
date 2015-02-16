package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
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
    public void testOf_Rational() {
        aeq(of(Rational.read("1/2").get()), "[1/2]");
        aeq(of(Rational.read("-5").get()), "[-5]");
        aeq(of(Rational.read("0").get()), "[0]");
    }

    @Test
    public void testDimension() {
        aeq(ZERO_DIMENSIONAL.dimension(), 0);
        aeq(read("[1/2]").get().dimension(), 1);
        aeq(read("[5/3, -1/4, 23]").get().dimension(), 3);
    }

    @Test
    public void testZero() {
        assertTrue(zero(0) == ZERO_DIMENSIONAL);
        aeq(zero(1), "[0]");
        aeq(zero(3), "[0, 0, 0]");
        aeq(zero(10), "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]");
        try {
            zero(-1);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIdentity() {
        aeq(identity(1, 0), "[1]");
        aeq(identity(3, 0), "[1, 0, 0]");
        aeq(identity(3, 1), "[0, 1, 0]");
        aeq(identity(3, 2), "[0, 0, 1]");
        aeq(identity(10, 6), "[0, 0, 0, 0, 0, 0, 1, 0, 0, 0]");
        try {
            identity(2, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            identity(-3, -4);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            identity(2, 3);
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            identity(0, 0);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIsZero() {
        assertTrue(ZERO_DIMENSIONAL.isZero());
        assertTrue(read("[0]").get().isZero());
        assertFalse(read("[5]").get().isZero());
        assertTrue(read("[0, 0, 0]").get().isZero());
        assertFalse(read("[0, 0, 3]").get().isZero());
    }

    @Test
    public void testNegate() {
        aeq(ZERO_DIMENSIONAL.negate(), "[]");
        aeq(read("[1/2]").get().negate(), "[-1/2]");
        aeq(read("[5/3, -1/4, 23]").get().negate(), "[-5/3, 1/4, -23]");
    }

    @Test
    public void testAdd() {
        assertTrue(ZERO_DIMENSIONAL.add(ZERO_DIMENSIONAL) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().add(read("[3]").get()), "[5]");
        aeq(read("[5/3, 4, 0]").get().add(read("[-2, 1, 3]").get()), "[-1/3, 5, 3]");
        aeq(read("[5/3, 4, 0]").get().add(read("[0, 0, 0]").get()), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().add(read("[-5/3, -4, 0]").get()), "[0, 0, 0]");
        try {
            ZERO_DIMENSIONAL.add(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().add(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().add(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSubtract() {
        assertTrue(ZERO_DIMENSIONAL.subtract(ZERO_DIMENSIONAL) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().subtract(read("[3]").get()), "[-1]");
        aeq(read("[5/3, 4, 0]").get().subtract(read("[-2, 1, 3]").get()), "[11/3, 3, -3]");
        aeq(read("[5/3, 4, 0]").get().subtract(read("[0, 0, 0]").get()), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().subtract(read("[5/3, 4, 0]").get()), "[0, 0, 0]");
        aeq(read("[0, 0, 0]").get().subtract(read("[5/3, 4, 0]").get()), "[-5/3, -4, 0]");
        try {
            ZERO_DIMENSIONAL.subtract(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().subtract(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().subtract(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMultiply_Rational() {
        assertTrue(ZERO_DIMENSIONAL.multiply(Rational.ZERO) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.multiply(Rational.read("-3/2").get()) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().multiply(Rational.ZERO), "[0]");
        aeq(read("[2]").get().multiply(Rational.read("-3/2").get()), "[-3]");
        aeq(read("[5/3, 4, 0]").get().multiply(Rational.ZERO), "[0, 0, 0]");
        aeq(read("[5/3, 4, 0]").get().multiply(Rational.read("-3/2").get()), "[-5/2, -6, 0]");
    }

    @Test
    public void testMultiply_BigInteger() {
        assertTrue(ZERO_DIMENSIONAL.multiply(BigInteger.ZERO) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.multiply(BigInteger.valueOf(5)) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().multiply(BigInteger.ZERO), "[0]");
        aeq(read("[2]").get().multiply(BigInteger.valueOf(5)), "[10]");
        aeq(read("[5/3, 4, 0]").get().multiply(BigInteger.ZERO), "[0, 0, 0]");
        aeq(read("[5/3, 4, 0]").get().multiply(BigInteger.valueOf(5)), "[25/3, 20, 0]");
    }

    @Test
    public void testMultiply_int() {
        assertTrue(ZERO_DIMENSIONAL.multiply(0) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.multiply(5) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().multiply(0), "[0]");
        aeq(read("[2]").get().multiply(5), "[10]");
        aeq(read("[5/3, 4, 0]").get().multiply(0), "[0, 0, 0]");
        aeq(read("[5/3, 4, 0]").get().multiply(5), "[25/3, 20, 0]");
    }

    @Test
    public void testDivide_Rational() {
        assertTrue(ZERO_DIMENSIONAL.divide(Rational.ONE) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.divide(Rational.read("-3/2").get()) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().divide(Rational.ONE), "[2]");
        aeq(read("[2]").get().divide(Rational.read("-3/2").get()), "[-4/3]");
        aeq(read("[5/3, 4, 0]").get().divide(Rational.ONE), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().divide(Rational.read("-3/2").get()), "[-10/9, -8/3, 0]");
        try {
            ZERO_DIMENSIONAL.divide(Rational.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            aeq(read("[5/3, 4, 0]").get().divide(Rational.ZERO), "[-10/9, -8/3, 0]");
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        assertTrue(ZERO_DIMENSIONAL.divide(BigInteger.ONE) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.divide(BigInteger.valueOf(5)) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().divide(BigInteger.ONE), "[2]");
        aeq(read("[2]").get().divide(BigInteger.valueOf(5)), "[2/5]");
        aeq(read("[5/3, 4, 0]").get().divide(BigInteger.ONE), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().divide(BigInteger.valueOf(5)), "[1/3, 4/5, 0]");
        try {
            ZERO_DIMENSIONAL.divide(BigInteger.ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            aeq(read("[5/3, 4, 0]").get().divide(BigInteger.ZERO), "[-10/9, -8/3, 0]");
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        assertTrue(ZERO_DIMENSIONAL.divide(1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.divide(5) == ZERO_DIMENSIONAL);
        aeq(read("[2]").get().divide(1), "[2]");
        aeq(read("[2]").get().divide(5), "[2/5]");
        aeq(read("[5/3, 4, 0]").get().divide(1), "[5/3, 4, 0]");
        aeq(read("[5/3, 4, 0]").get().divide(5), "[1/3, 4/5, 0]");
        try {
            ZERO_DIMENSIONAL.divide(0);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            aeq(read("[5/3, 4, 0]").get().divide(0), "[-10/9, -8/3, 0]");
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(0) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(4) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftLeft(-4) == ZERO_DIMENSIONAL);
        aeq(read("[1/2]").get().shiftLeft(0), "[1/2]");
        aeq(read("[1/2]").get().shiftLeft(1), "[1]");
        aeq(read("[1/2]").get().shiftLeft(2), "[2]");
        aeq(read("[1/2]").get().shiftLeft(3), "[4]");
        aeq(read("[1/2]").get().shiftLeft(4), "[8]");
        aeq(read("[1/2]").get().shiftLeft(-1), "[1/4]");
        aeq(read("[1/2]").get().shiftLeft(-2), "[1/8]");
        aeq(read("[1/2]").get().shiftLeft(-3), "[1/16]");
        aeq(read("[1/2]").get().shiftLeft(-4), "[1/32]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(0), "[5/3, -1/4, 23]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(1), "[10/3, -1/2, 46]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(2), "[20/3, -1, 92]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(3), "[40/3, -2, 184]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(4), "[80/3, -4, 368]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-1), "[5/6, -1/8, 23/2]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-2), "[5/12, -1/16, 23/4]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-3), "[5/24, -1/32, 23/8]");
        aeq(read("[5/3, -1/4, 23]").get().shiftLeft(-4), "[5/48, -1/64, 23/16]");
    }

    @Test
    public void testShiftRight() {
        assertTrue(ZERO_DIMENSIONAL.shiftRight(0) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(4) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-1) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-2) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-3) == ZERO_DIMENSIONAL);
        assertTrue(ZERO_DIMENSIONAL.shiftRight(-4) == ZERO_DIMENSIONAL);
        aeq(read("[1/2]").get().shiftRight(0), "[1/2]");
        aeq(read("[1/2]").get().shiftRight(1), "[1/4]");
        aeq(read("[1/2]").get().shiftRight(2), "[1/8]");
        aeq(read("[1/2]").get().shiftRight(3), "[1/16]");
        aeq(read("[1/2]").get().shiftRight(4), "[1/32]");
        aeq(read("[1/2]").get().shiftRight(-1), "[1]");
        aeq(read("[1/2]").get().shiftRight(-2), "[2]");
        aeq(read("[1/2]").get().shiftRight(-3), "[4]");
        aeq(read("[1/2]").get().shiftRight(-4), "[8]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(0), "[5/3, -1/4, 23]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(1), "[5/6, -1/8, 23/2]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(2), "[5/12, -1/16, 23/4]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(3), "[5/24, -1/32, 23/8]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(4), "[5/48, -1/64, 23/16]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-1), "[10/3, -1/2, 46]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-2), "[20/3, -1, 92]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-3), "[40/3, -2, 184]");
        aeq(read("[5/3, -1/4, 23]").get().shiftRight(-4), "[80/3, -4, 368]");
    }

    @Test
    public void testSum() {
        assertTrue(sum(readRationalVectorList("[[]]").get()) == ZERO_DIMENSIONAL);
        assertTrue(sum(readRationalVectorList("[[], [], []]").get()) == ZERO_DIMENSIONAL);
        aeq(sum(readRationalVectorList("[[5/3, 1/4, 23]]").get()), "[5/3, 1/4, 23]");
        aeq(
                sum(readRationalVectorList("[[5/3, 1/4, 23], [0, 2/3, -1/8], [32, -45/2, 9]]").get()),
                "[101/3, -259/12, 255/8]"
        );
        aeq(sum(readRationalVectorList("[[1/2], [2/3], [3/4]]").get()), "[23/12]");
        try {
            sum(readRationalVectorList("[]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            sum(readRationalVectorListWithNulls("[[1/2, 3], null]").get());
            fail();
        } catch (NullPointerException ignored) {}
        try {
            sum(readRationalVectorList("[[1/2], [3, 4]]").get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDelta() {
        aeq(delta(readRationalVectorList("[[]]").get()), "[]");
        aeq(delta(readRationalVectorList("[[], [], []]").get()), "[[], []]");
        aeq(delta(readRationalVectorList("[[5/3, 1/4, 23]]").get()), "[]");
        aeq(
                delta(readRationalVectorList("[[5/3, 1/4, 23], [0, 2/3, -1/8], [32, -45/2, 9]]").get()),
                "[[-5/3, 5/12, -185/8], [32, -139/6, 73/8]]"
        );
        aeq(delta(readRationalVectorList("[[1/2], [2/3], [3/4]]").get()), "[[1/6], [1/12]]");
        try {
            delta(readRationalVectorList("[]").get());
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readRationalVectorListWithNulls("[[1/2, 3], null]").get()));
            fail();
        } catch (NullPointerException ignored) {}
        try {
            toList(delta(readRationalVectorList("[[1/2], [3, 4]]").get()));
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDot() {
        assertTrue(ZERO_DIMENSIONAL.dot(ZERO_DIMENSIONAL) == Rational.ZERO);
        aeq(read("[2]").get().dot(read("[3]").get()), "6");
        aeq(read("[5/3, 4, 0]").get().dot(read("[-2, 1, 3]").get()), "2/3");
        aeq(read("[5/3, 4, 0]").get().dot(read("[0, 0, 0]").get()), "0");
        aeq(read("[5/3, 4, 0]").get().dot(read("[-5/3, -4, 0]").get()), "-169/9");
        try {
            ZERO_DIMENSIONAL.dot(read("[1/2]").get());
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2]").get().dot(ZERO_DIMENSIONAL);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[1/2, 4, -4]").get().dot(read("[5/6, 2/3]").get());
            fail();
        } catch (ArithmeticException ignored) {}
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

    private static @NotNull Optional<List<RationalVector>> readRationalVectorList(@NotNull String s) {
        return Readers.readList(RationalVector::findIn, s);
    }

    private static @NotNull Optional<List<RationalVector>> readRationalVectorListWithNulls(@NotNull String s) {
        return Readers.readList(t -> Readers.findInWithNulls(RationalVector::findIn, t), s);
    }
}