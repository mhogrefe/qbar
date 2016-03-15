package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.MultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.aeq;
import static org.junit.Assert.fail;

public class MultivariatePolynomialTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "0");
        aeq(ONE, "1");
    }

    private static void iterator_helper(@NotNull String x, @NotNull String output) {
        aeq(toList(read(x).get()), output);
    }

    @Test
    public void testIterator() {
        iterator_helper("0", "[]");
        iterator_helper("1", "[(1, 1)]");
        iterator_helper("ooo", "[(ooo, 1)]");
        iterator_helper("-17", "[(1, -17)]");
        iterator_helper("a*b*c", "[(a*b*c, 1)]");
        iterator_helper("x^2-4*x+7", "[(1, 7), (x, -4), (x^2, 1)]");
        iterator_helper("x^2+2*x*y+y^2", "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        iterator_helper("a+b+c+d+e+f", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
    }

    private static void coefficient_helper(@NotNull String p, @NotNull String ev, @NotNull String output) {
        aeq(read(p).get().coefficient(ExponentVector.read(ev).get()), output);
    }

    @Test
    public void testCoefficient() {
        coefficient_helper("0", "1", "0");
        coefficient_helper("0", "a", "0");
        coefficient_helper("1", "1", "1");
        coefficient_helper("1", "a", "0");
        coefficient_helper("ooo", "1", "0");
        coefficient_helper("ooo", "a", "0");
        coefficient_helper("ooo", "ooo", "1");
        coefficient_helper("a*b*c", "1", "0");
        coefficient_helper("a*b*c", "a", "0");
        coefficient_helper("a*b*c", "a*b", "0");
        coefficient_helper("a*b*c", "a*b*c", "1");
        coefficient_helper("x^2-4*x+7", "a", "0");
        coefficient_helper("x^2-4*x+7", "x^2", "1");
        coefficient_helper("x^2-4*x+7", "x", "-4");
        coefficient_helper("x^2-4*x+7", "1", "7");
        coefficient_helper("a+b+c+d+e+f", "1", "0");
        coefficient_helper("a+b+c+d+e+f", "a", "1");
        coefficient_helper("a+b+c+d+e+f", "f", "1");
        coefficient_helper("a+b+c+d+e+f", "g", "0");
    }

    private static void of_List_Pair_ExponentVector_BigInteger_helper(@NotNull String input, @NotNull String output) {
        aeq(of(readExponentVectorBigIntegerPairList(input)), output);
    }

    private static void of_List_Pair_ExponentVector_BigInteger_fail_helper(@NotNull String input) {
        try {
            of(readExponentVectorBigIntegerPairListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Pair_ExponentVector_BigInteger() {
        of_List_Pair_ExponentVector_BigInteger_helper("[]", "0");
        of_List_Pair_ExponentVector_BigInteger_helper("[(a*b^2, 0)]", "0");
        of_List_Pair_ExponentVector_BigInteger_helper("[(1, 1)]", "1");
        of_List_Pair_ExponentVector_BigInteger_helper("[(1, 1), (x, 0)]", "1");
        of_List_Pair_ExponentVector_BigInteger_helper("[(ooo, 1)]", "ooo");
        of_List_Pair_ExponentVector_BigInteger_helper("[(1, -17)]", "-17");
        of_List_Pair_ExponentVector_BigInteger_helper("[(1, -10), (1, -7)]", "-17");
        of_List_Pair_ExponentVector_BigInteger_helper("[(a*b*c, 1)]", "a*b*c");
        of_List_Pair_ExponentVector_BigInteger_helper("[(1, 7), (x, -4), (x^2, 1)]", "x^2-4*x+7");
        of_List_Pair_ExponentVector_BigInteger_helper("[(x, -4), (1, 7), (x^2, 1)]", "x^2-4*x+7");
        of_List_Pair_ExponentVector_BigInteger_helper("[(y^2, 1), (x*y, 2), (x^2, 1)]", "x^2+2*x*y+y^2");
        of_List_Pair_ExponentVector_BigInteger_helper("[(x*y, 1), (x*y, 1), (y^2, 1), (x^2, 1), (z, 0)]",
                "x^2+2*x*y+y^2");
        of_List_Pair_ExponentVector_BigInteger_helper("[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]",
                "a+b+c+d+e+f");
        of_List_Pair_ExponentVector_BigInteger_helper("[(f, 1), (b, 1), (d, 2), (d, -1), (c, 1), (e, 1), (a, 1)]",
                "a+b+c+d+e+f");

        of_List_Pair_ExponentVector_BigInteger_fail_helper("[(ooo, 1), null]");
        of_List_Pair_ExponentVector_BigInteger_fail_helper("[(ooo, null)]");
        of_List_Pair_ExponentVector_BigInteger_fail_helper("[(null, 1)]");
    }

    private static void of_ExponentVector_BigInteger_helper(
            @NotNull String ev,
            @NotNull String c,
            @NotNull String output
    ) {
        aeq(of(ExponentVector.read(ev).get(), Readers.readBigInteger(c).get()), output);
    }

    @Test
    public void testOf_ExponentVector_BigInteger() {
        of_ExponentVector_BigInteger_helper("1", "0", "0");
        of_ExponentVector_BigInteger_helper("1", "1", "1");
        of_ExponentVector_BigInteger_helper("1", "-1", "-1");
        of_ExponentVector_BigInteger_helper("1", "3", "3");
        of_ExponentVector_BigInteger_helper("1", "-5", "-5");
        of_ExponentVector_BigInteger_helper("ooo", "0", "0");
        of_ExponentVector_BigInteger_helper("ooo", "1", "ooo");
        of_ExponentVector_BigInteger_helper("ooo", "-1", "-ooo");
        of_ExponentVector_BigInteger_helper("ooo", "3", "3*ooo");
        of_ExponentVector_BigInteger_helper("ooo", "-5", "-5*ooo");
        of_ExponentVector_BigInteger_helper("a*b^2", "0", "0");
        of_ExponentVector_BigInteger_helper("a*b^2", "1", "a*b^2");
        of_ExponentVector_BigInteger_helper("a*b^2", "-1", "-a*b^2");
        of_ExponentVector_BigInteger_helper("a*b^2", "3", "3*a*b^2");
        of_ExponentVector_BigInteger_helper("a*b^2", "-5", "-5*a*b^2");
    }

    private static void of_BigInteger_helper(@NotNull String input) {
        aeq(of(Readers.readBigInteger(input).get()), input);
    }

    @Test
    public void testOf_BigInteger() {
        of_BigInteger_helper("0");
        of_BigInteger_helper("1");
        of_BigInteger_helper("-1");
        of_BigInteger_helper("3");
        of_BigInteger_helper("-5");
    }

    private static void of_int_helper(int input) {
        aeq(of(input), input);
    }

    @Test
    public void testOf_int() {
        of_int_helper(0);
        of_int_helper(1);
        of_int_helper(-1);
        of_int_helper(3);
        of_int_helper(-5);
    }

    private static @NotNull List<Pair<ExponentVector, BigInteger>> readExponentVectorBigIntegerPairList(
            @NotNull String s
    ) {
        return Readers.readList(
                u -> Pair.read(
                        u,
                        t -> NullableOptional.fromOptional(ExponentVector.read(t)),
                        t -> NullableOptional.fromOptional(Readers.readBigInteger(t))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<ExponentVector, BigInteger>> readExponentVectorBigIntegerPairListWithNulls(
            @NotNull String s
    ) {
        return Readers.readListWithNulls(
                u -> Pair.read(
                        u, Readers.readWithNulls(ExponentVector::read),
                        Readers.readWithNulls(Readers::readBigInteger)
                )
        ).apply(s).get();
    }
}
