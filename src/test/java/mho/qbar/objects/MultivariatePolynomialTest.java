package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.MultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
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
        iterator_helper("-17", "[(1, -17)]");
        iterator_helper("ooo", "[(ooo, 1)]");
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
        MultivariatePolynomial p = of(readExponentVectorBigIntegerPairList(input));
        p.validate();
        aeq(p, output);
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
        MultivariatePolynomial p = of(ExponentVector.read(ev).get(), Readers.readBigInteger(c).get());
        p.validate();
        aeq(p, output);
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
        MultivariatePolynomial p = of(Readers.readBigInteger(input).get());
        p.validate();
        aeq(p, input);
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
        MultivariatePolynomial p = of(input);
        p.validate();
        aeq(p, input);
    }

    @Test
    public void testOf_int() {
        of_int_helper(0);
        of_int_helper(1);
        of_int_helper(-1);
        of_int_helper(3);
        of_int_helper(-5);
    }

    private static void of_Polynomial_Variable_helper(@NotNull String p, @NotNull String v, @NotNull String output) {
        MultivariatePolynomial q = of(Polynomial.read(p).get(), Variable.read(v).get());
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testOf_Polynomial_Variable() {
        of_Polynomial_Variable_helper("0", "x", "0");
        of_Polynomial_Variable_helper("1", "x", "1");
        of_Polynomial_Variable_helper("-17", "x", "-17");
        of_Polynomial_Variable_helper("x", "x", "x");
        of_Polynomial_Variable_helper("x", "ooo", "ooo");
        of_Polynomial_Variable_helper("x^2-4*x+7", "x", "x^2-4*x+7");
        of_Polynomial_Variable_helper("x^2-4*x+7", "ooo", "ooo^2-4*ooo+7");
        of_Polynomial_Variable_helper("x^3-1", "x", "x^3-1");
        of_Polynomial_Variable_helper("x^3-1", "ooo", "ooo^3-1");
        of_Polynomial_Variable_helper("3*x^10", "x", "3*x^10");
        of_Polynomial_Variable_helper("3*x^10", "ooo", "3*ooo^10");
    }

    private static void toPolynomial_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().toPolynomial(), output);
    }

    private static void toPolynomial_fail_helper(@NotNull String input) {
        try {
            read(input).get().toPolynomial();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToPolynomial() {
        toPolynomial_helper("0", "0");
        toPolynomial_helper("1", "1");
        toPolynomial_helper("-17", "-17");
        toPolynomial_helper("ooo", "x");
        toPolynomial_helper("x^2-4*x+7", "x^2-4*x+7");
        toPolynomial_helper("a^2-4*a+7", "x^2-4*x+7");

        toPolynomial_fail_helper("a*b*c");
        toPolynomial_fail_helper("x^2+2*x*y+y^2");
        toPolynomial_fail_helper("a+b+c+d+e+f");
    }

    private static void variables_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().variables(), output);
    }

    @Test
    public void testVariables() {
        variables_helper("0", "[]");
        variables_helper("1", "[]");
        variables_helper("-17", "[]");
        variables_helper("ooo", "[ooo]");
        variables_helper("a*b*c", "[a, b, c]");
        variables_helper("x^2-4*x+7", "[x]");
        variables_helper("x^2+2*x*y+y^2", "[x, y]");
        variables_helper("a+b+c+d+e+f", "[a, b, c, d, e, f]");
    }

    private static void variableCount_helper(@NotNull String input, int output) {
        aeq(read(input).get().variableCount(), output);
    }

    @Test
    public void testVariableCount() {
        variableCount_helper("0", 0);
        variableCount_helper("1", 0);
        variableCount_helper("-17", 0);
        variableCount_helper("ooo", 1);
        variableCount_helper("a*b*c", 3);
        variableCount_helper("x^2-4*x+7", 1);
        variableCount_helper("x^2+2*x*y+y^2", 2);
        variableCount_helper("a+b+c+d+e+f", 6);
    }

    private static void termCount_helper(@NotNull String input, int output) {
        aeq(read(input).get().termCount(), output);
    }

    @Test
    public void testTermCount() {
        termCount_helper("0", 0);
        termCount_helper("1", 1);
        termCount_helper("-17", 1);
        termCount_helper("ooo", 1);
        termCount_helper("a*b*c", 1);
        termCount_helper("x^2-4*x+7", 3);
        termCount_helper("x^2+2*x*y+y^2", 3);
        termCount_helper("a+b+c+d+e+f", 6);
    }

    private static void maxCoefficientBitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().maxCoefficientBitLength(), output);
    }

    @Test
    public void testMaxCoefficientBitLength() {
        maxCoefficientBitLength_helper("0", 0);
        maxCoefficientBitLength_helper("1", 1);
        maxCoefficientBitLength_helper("-17", 5);
        maxCoefficientBitLength_helper("ooo", 1);
        maxCoefficientBitLength_helper("a*b*c", 1);
        maxCoefficientBitLength_helper("x^2-4*x+7", 3);
        maxCoefficientBitLength_helper("x^2+2*x*y+y^2", 2);
        maxCoefficientBitLength_helper("a+b+c+d+e+f", 1);
    }

    private static void degree_helper(@NotNull String input, int output) {
        aeq(read(input).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("0", -1);
        degree_helper("1", 0);
        degree_helper("-17", 0);
        degree_helper("ooo", 1);
        degree_helper("a*b*c", 3);
        degree_helper("x^2-4*x+7", 2);
        degree_helper("x^2+2*x*y+y^2", 2);
        degree_helper("a+b+c+d+e+f", 1);
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        MultivariatePolynomial p = read(a).get().add(read(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testAdd() {
        add_helper("0", "0", "0");
        add_helper("0", "1", "1");
        add_helper("0", "-17", "-17");
        add_helper("0", "ooo", "ooo");
        add_helper("0", "a*b*c", "a*b*c");
        add_helper("0", "x^2-4*x+7", "x^2-4*x+7");
        add_helper("0", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2");
        add_helper("0", "a+b+c+d+e+f", "a+b+c+d+e+f");

        add_helper("1", "0", "1");
        add_helper("1", "1", "2");
        add_helper("1", "-17", "-16");
        add_helper("1", "ooo", "ooo+1");
        add_helper("1", "a*b*c", "a*b*c+1");
        add_helper("1", "x^2-4*x+7", "x^2-4*x+8");
        add_helper("1", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2+1");
        add_helper("1", "a+b+c+d+e+f", "a+b+c+d+e+f+1");

        add_helper("-17", "0", "-17");
        add_helper("-17", "1", "-16");
        add_helper("-17", "-17", "-34");
        add_helper("-17", "ooo", "ooo-17");
        add_helper("-17", "a*b*c", "a*b*c-17");
        add_helper("-17", "x^2-4*x+7", "x^2-4*x-10");
        add_helper("-17", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2-17");
        add_helper("-17", "a+b+c+d+e+f", "a+b+c+d+e+f-17");

        add_helper("ooo", "0", "ooo");
        add_helper("ooo", "1", "ooo+1");
        add_helper("ooo", "-17", "ooo-17");
        add_helper("ooo", "ooo", "2*ooo");
        add_helper("ooo", "a*b*c", "a*b*c+ooo");
        add_helper("ooo", "x^2-4*x+7", "x^2-4*x+ooo+7");
        add_helper("ooo", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2+ooo");
        add_helper("ooo", "a+b+c+d+e+f", "a+b+c+d+e+f+ooo");

        add_helper("a*b*c", "0", "a*b*c");
        add_helper("a*b*c", "1", "a*b*c+1");
        add_helper("a*b*c", "-17", "a*b*c-17");
        add_helper("a*b*c", "ooo", "a*b*c+ooo");
        add_helper("a*b*c", "a*b*c", "2*a*b*c");
        add_helper("a*b*c", "x^2-4*x+7", "a*b*c+x^2-4*x+7");
        add_helper("a*b*c", "x^2+2*x*y+y^2", "a*b*c+x^2+2*x*y+y^2");
        add_helper("a*b*c", "a+b+c+d+e+f", "a*b*c+a+b+c+d+e+f");

        add_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        add_helper("x^2-4*x+7", "1", "x^2-4*x+8");
        add_helper("x^2-4*x+7", "-17", "x^2-4*x-10");
        add_helper("x^2-4*x+7", "ooo", "x^2-4*x+ooo+7");
        add_helper("x^2-4*x+7", "a*b*c", "a*b*c+x^2-4*x+7");
        add_helper("x^2-4*x+7", "x^2-4*x+7", "2*x^2-8*x+14");
        add_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "2*x^2+2*x*y+y^2-4*x+7");
        add_helper("x^2-4*x+7", "a+b+c+d+e+f", "x^2+a+b+c+d+e+f-4*x+7");

        add_helper("x^2+2*x*y+y^2", "0", "x^2+2*x*y+y^2");
        add_helper("x^2+2*x*y+y^2", "1", "x^2+2*x*y+y^2+1");
        add_helper("x^2+2*x*y+y^2", "-17", "x^2+2*x*y+y^2-17");
        add_helper("x^2+2*x*y+y^2", "ooo", "x^2+2*x*y+y^2+ooo");
        add_helper("x^2+2*x*y+y^2", "a*b*c", "a*b*c+x^2+2*x*y+y^2");
        add_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "2*x^2+2*x*y+y^2-4*x+7");
        add_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "2*x^2+4*x*y+2*y^2");
        add_helper("x^2+2*x*y+y^2", "a+b+c+d+e+f", "x^2+2*x*y+y^2+a+b+c+d+e+f");

        add_helper("a+b+c+d+e+f", "0", "a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f+1");
        add_helper("a+b+c+d+e+f", "-17", "a+b+c+d+e+f-17");
        add_helper("a+b+c+d+e+f", "ooo", "a+b+c+d+e+f+ooo");
        add_helper("a+b+c+d+e+f", "a*b*c", "a*b*c+a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "x^2-4*x+7", "x^2+a+b+c+d+e+f-4*x+7");
        add_helper("a+b+c+d+e+f", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2+a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "a+b+c+d+e+f", "2*a+2*b+2*c+2*d+2*e+2*f");

        add_helper("a*b*c", "-a*b*c", "0");
        add_helper("a*b*c", "-a*b*c+1", "1");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        MultivariatePolynomial p = read(input).get().negate();
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testNegate() {
        negate_helper("0", "0");
        negate_helper("1", "-1");
        negate_helper("-17", "17");
        negate_helper("ooo", "-ooo");
        negate_helper("a*b*c", "-a*b*c");
        negate_helper("x^2-4*x+7", "-x^2+4*x-7");
        negate_helper("x^2+2*x*y+y^2", "-x^2-2*x*y-y^2");
        negate_helper("a+b+c+d+e+f", "-a-b-c-d-e-f");
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        MultivariatePolynomial p = read(a).get().subtract(read(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testSubtract() {
        subtract_helper("0", "0", "0");
        subtract_helper("0", "1", "-1");
        subtract_helper("0", "-17", "17");
        subtract_helper("0", "ooo", "-ooo");
        subtract_helper("0", "a*b*c", "-a*b*c");
        subtract_helper("0", "x^2-4*x+7", "-x^2+4*x-7");
        subtract_helper("0", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2");
        subtract_helper("0", "a+b+c+d+e+f", "-a-b-c-d-e-f");

        subtract_helper("1", "0", "1");
        subtract_helper("1", "1", "0");
        subtract_helper("1", "-17", "18");
        subtract_helper("1", "ooo", "-ooo+1");
        subtract_helper("1", "a*b*c", "-a*b*c+1");
        subtract_helper("1", "x^2-4*x+7", "-x^2+4*x-6");
        subtract_helper("1", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2+1");
        subtract_helper("1", "a+b+c+d+e+f", "-a-b-c-d-e-f+1");

        subtract_helper("-17", "0", "-17");
        subtract_helper("-17", "1", "-18");
        subtract_helper("-17", "-17", "0");
        subtract_helper("-17", "ooo", "-ooo-17");
        subtract_helper("-17", "a*b*c", "-a*b*c-17");
        subtract_helper("-17", "x^2-4*x+7", "-x^2+4*x-24");
        subtract_helper("-17", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2-17");
        subtract_helper("-17", "a+b+c+d+e+f", "-a-b-c-d-e-f-17");

        subtract_helper("ooo", "0", "ooo");
        subtract_helper("ooo", "1", "ooo-1");
        subtract_helper("ooo", "-17", "ooo+17");
        subtract_helper("ooo", "ooo", "0");
        subtract_helper("ooo", "a*b*c", "-a*b*c+ooo");
        subtract_helper("ooo", "x^2-4*x+7", "-x^2+4*x+ooo-7");
        subtract_helper("ooo", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2+ooo");
        subtract_helper("ooo", "a+b+c+d+e+f", "-a-b-c-d-e-f+ooo");

        subtract_helper("a*b*c", "0", "a*b*c");
        subtract_helper("a*b*c", "1", "a*b*c-1");
        subtract_helper("a*b*c", "-17", "a*b*c+17");
        subtract_helper("a*b*c", "ooo", "a*b*c-ooo");
        subtract_helper("a*b*c", "a*b*c", "0");
        subtract_helper("a*b*c", "x^2-4*x+7", "a*b*c-x^2+4*x-7");
        subtract_helper("a*b*c", "x^2+2*x*y+y^2", "a*b*c-x^2-2*x*y-y^2");
        subtract_helper("a*b*c", "a+b+c+d+e+f", "a*b*c-a-b-c-d-e-f");

        subtract_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        subtract_helper("x^2-4*x+7", "1", "x^2-4*x+6");
        subtract_helper("x^2-4*x+7", "-17", "x^2-4*x+24");
        subtract_helper("x^2-4*x+7", "ooo", "x^2-4*x-ooo+7");
        subtract_helper("x^2-4*x+7", "a*b*c", "-a*b*c+x^2-4*x+7");
        subtract_helper("x^2-4*x+7", "x^2-4*x+7", "0");
        subtract_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "-2*x*y-y^2-4*x+7");
        subtract_helper("x^2-4*x+7", "a+b+c+d+e+f", "x^2-a-b-c-d-e-f-4*x+7");

        subtract_helper("x^2+2*x*y+y^2", "0", "x^2+2*x*y+y^2");
        subtract_helper("x^2+2*x*y+y^2", "1", "x^2+2*x*y+y^2-1");
        subtract_helper("x^2+2*x*y+y^2", "-17", "x^2+2*x*y+y^2+17");
        subtract_helper("x^2+2*x*y+y^2", "ooo", "x^2+2*x*y+y^2-ooo");
        subtract_helper("x^2+2*x*y+y^2", "a*b*c", "-a*b*c+x^2+2*x*y+y^2");
        subtract_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "2*x*y+y^2+4*x-7");
        subtract_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "0");
        subtract_helper("x^2+2*x*y+y^2", "a+b+c+d+e+f", "x^2+2*x*y+y^2-a-b-c-d-e-f");

        subtract_helper("a+b+c+d+e+f", "0", "a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f-1");
        subtract_helper("a+b+c+d+e+f", "-17", "a+b+c+d+e+f+17");
        subtract_helper("a+b+c+d+e+f", "ooo", "a+b+c+d+e+f-ooo");
        subtract_helper("a+b+c+d+e+f", "a*b*c", "-a*b*c+a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "x^2-4*x+7", "-x^2+a+b+c+d+e+f+4*x-7");
        subtract_helper("a+b+c+d+e+f", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2+a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "a+b+c+d+e+f", "0");

        subtract_helper("a*b*c", "a*b*c-1", "1");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        MultivariatePolynomial p = read(a).get().multiply(b);
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("0", 0, "0");
        multiply_int_helper("0", 1, "0");
        multiply_int_helper("0", -3, "0");
        multiply_int_helper("0", 4, "0");

        multiply_int_helper("1", 0, "0");
        multiply_int_helper("1", 1, "1");
        multiply_int_helper("1", -3, "-3");
        multiply_int_helper("1", 4, "4");

        multiply_int_helper("-17", 0, "0");
        multiply_int_helper("-17", 1, "-17");
        multiply_int_helper("-17", -3, "51");
        multiply_int_helper("-17", 4, "-68");

        multiply_int_helper("ooo", 0, "0");
        multiply_int_helper("ooo", 1, "ooo");
        multiply_int_helper("ooo", -3, "-3*ooo");
        multiply_int_helper("ooo", 4, "4*ooo");

        multiply_int_helper("a*b*c", 0, "0");
        multiply_int_helper("a*b*c", 1, "a*b*c");
        multiply_int_helper("a*b*c", -3, "-3*a*b*c");
        multiply_int_helper("a*b*c", 4, "4*a*b*c");

        multiply_int_helper("x^2-4*x+7", 0, "0");
        multiply_int_helper("x^2-4*x+7", 1, "x^2-4*x+7");
        multiply_int_helper("x^2-4*x+7", -3, "-3*x^2+12*x-21");
        multiply_int_helper("x^2-4*x+7", 4, "4*x^2-16*x+28");

        multiply_int_helper("x^2+2*x*y+y^2", 0, "0");
        multiply_int_helper("x^2+2*x*y+y^2", 1, "x^2+2*x*y+y^2");
        multiply_int_helper("x^2+2*x*y+y^2", -3, "-3*x^2-6*x*y-3*y^2");
        multiply_int_helper("x^2+2*x*y+y^2", 4, "4*x^2+8*x*y+4*y^2");

        multiply_int_helper("a+b+c+d+e+f", 0, "0");
        multiply_int_helper("a+b+c+d+e+f", 1, "a+b+c+d+e+f");
        multiply_int_helper("a+b+c+d+e+f", -3, "-3*a-3*b-3*c-3*d-3*e-3*f");
        multiply_int_helper("a+b+c+d+e+f", 4, "4*a+4*b+4*c+4*d+4*e+4*f");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        MultivariatePolynomial p = read(a).get().multiply(Readers.readBigInteger(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("0", "0", "0");
        multiply_BigInteger_helper("0", "1", "0");
        multiply_BigInteger_helper("0", "-3", "0");
        multiply_BigInteger_helper("0", "4", "0");

        multiply_BigInteger_helper("1", "0", "0");
        multiply_BigInteger_helper("1", "1", "1");
        multiply_BigInteger_helper("1", "-3", "-3");
        multiply_BigInteger_helper("1", "4", "4");

        multiply_BigInteger_helper("-17", "0", "0");
        multiply_BigInteger_helper("-17", "1", "-17");
        multiply_BigInteger_helper("-17", "-3", "51");
        multiply_BigInteger_helper("-17", "4", "-68");

        multiply_BigInteger_helper("ooo", "0", "0");
        multiply_BigInteger_helper("ooo", "1", "ooo");
        multiply_BigInteger_helper("ooo", "-3", "-3*ooo");
        multiply_BigInteger_helper("ooo", "4", "4*ooo");

        multiply_BigInteger_helper("a*b*c", "0", "0");
        multiply_BigInteger_helper("a*b*c", "1", "a*b*c");
        multiply_BigInteger_helper("a*b*c", "-3", "-3*a*b*c");
        multiply_BigInteger_helper("a*b*c", "4", "4*a*b*c");

        multiply_BigInteger_helper("x^2-4*x+7", "0", "0");
        multiply_BigInteger_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        multiply_BigInteger_helper("x^2-4*x+7", "-3", "-3*x^2+12*x-21");
        multiply_BigInteger_helper("x^2-4*x+7", "4", "4*x^2-16*x+28");

        multiply_BigInteger_helper("x^2+2*x*y+y^2", "0", "0");
        multiply_BigInteger_helper("x^2+2*x*y+y^2", "1", "x^2+2*x*y+y^2");
        multiply_BigInteger_helper("x^2+2*x*y+y^2", "-3", "-3*x^2-6*x*y-3*y^2");
        multiply_BigInteger_helper("x^2+2*x*y+y^2", "4", "4*x^2+8*x*y+4*y^2");

        multiply_BigInteger_helper("a+b+c+d+e+f", "0", "0");
        multiply_BigInteger_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        multiply_BigInteger_helper("a+b+c+d+e+f", "-3", "-3*a-3*b-3*c-3*d-3*e-3*f");
        multiply_BigInteger_helper("a+b+c+d+e+f", "4", "4*a+4*b+4*c+4*d+4*e+4*f");
    }

    private static void multiply_ExponentVector_BigInteger_helper(
            @NotNull String p,
            @NotNull String ev,
            @NotNull String c,
            @NotNull String output
    ) {
        MultivariatePolynomial q = read(p).get()
                .multiply(ExponentVector.read(ev).get(), Readers.readBigInteger(c).get());
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testMultiply_ExponentVector_BigInteger() {
        multiply_ExponentVector_BigInteger_helper("0", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("0", "1", "1", "0");
        multiply_ExponentVector_BigInteger_helper("0", "ooo", "2", "0");
        multiply_ExponentVector_BigInteger_helper("0", "a*b*c", "-2", "0");

        multiply_ExponentVector_BigInteger_helper("1", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("1", "1", "1", "1");
        multiply_ExponentVector_BigInteger_helper("1", "ooo", "2", "2*ooo");
        multiply_ExponentVector_BigInteger_helper("1", "a*b*c", "-2", "-2*a*b*c");

        multiply_ExponentVector_BigInteger_helper("-17", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("-17", "1", "1", "-17");
        multiply_ExponentVector_BigInteger_helper("-17", "ooo", "2", "-34*ooo");
        multiply_ExponentVector_BigInteger_helper("-17", "a*b*c", "-2", "34*a*b*c");

        multiply_ExponentVector_BigInteger_helper("ooo", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("ooo", "1", "1", "ooo");
        multiply_ExponentVector_BigInteger_helper("ooo", "ooo", "2", "2*ooo^2");
        multiply_ExponentVector_BigInteger_helper("ooo", "a*b*c", "-2", "-2*a*b*c*ooo");

        multiply_ExponentVector_BigInteger_helper("a*b*c", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("a*b*c", "1", "1", "a*b*c");
        multiply_ExponentVector_BigInteger_helper("a*b*c", "ooo", "2", "2*a*b*c*ooo");
        multiply_ExponentVector_BigInteger_helper("a*b*c", "a*b*c", "-2", "-2*a^2*b^2*c^2");

        multiply_ExponentVector_BigInteger_helper("x^2-4*x+7", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("x^2-4*x+7", "1", "1", "x^2-4*x+7");
        multiply_ExponentVector_BigInteger_helper("x^2-4*x+7", "ooo", "2", "2*x^2*ooo-8*x*ooo+14*ooo");
        multiply_ExponentVector_BigInteger_helper("x^2-4*x+7", "a*b*c", "-2", "-2*a*b*c*x^2+8*a*b*c*x-14*a*b*c");

        multiply_ExponentVector_BigInteger_helper("x^2+2*x*y+y^2", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("x^2+2*x*y+y^2", "1", "1", "x^2+2*x*y+y^2");
        multiply_ExponentVector_BigInteger_helper("x^2+2*x*y+y^2", "ooo", "2", "2*x^2*ooo+4*x*y*ooo+2*y^2*ooo");
        multiply_ExponentVector_BigInteger_helper("x^2+2*x*y+y^2", "a*b*c", "-2",
                "-2*a*b*c*x^2-4*a*b*c*x*y-2*a*b*c*y^2");

        multiply_ExponentVector_BigInteger_helper("a+b+c+d+e+f", "1", "0", "0");
        multiply_ExponentVector_BigInteger_helper("a+b+c+d+e+f", "1", "1", "a+b+c+d+e+f");
        multiply_ExponentVector_BigInteger_helper("a+b+c+d+e+f", "ooo", "2",
                "2*a*ooo+2*b*ooo+2*c*ooo+2*d*ooo+2*e*ooo+2*f*ooo");
        multiply_ExponentVector_BigInteger_helper("a+b+c+d+e+f", "a*b*c", "-2",
                "-2*a^2*b*c-2*a*b^2*c-2*a*b*c^2-2*a*b*c*d-2*a*b*c*e-2*a*b*c*f");
    }

    private static void multiply_MultivariatePolynomial_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        MultivariatePolynomial p = read(a).get().multiply(read(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testMultiply_MultivariatePolynomial() {
        multiply_MultivariatePolynomial_helper("0", "0", "0");
        multiply_MultivariatePolynomial_helper("0", "1", "0");
        multiply_MultivariatePolynomial_helper("0", "-17", "0");
        multiply_MultivariatePolynomial_helper("0", "ooo", "0");
        multiply_MultivariatePolynomial_helper("0", "a*b*c", "0");
        multiply_MultivariatePolynomial_helper("0", "x^2-4*x+7", "0");
        multiply_MultivariatePolynomial_helper("0", "x^2+2*x*y+y^2", "0");
        multiply_MultivariatePolynomial_helper("0", "a+b+c+d+e+f", "0");

        multiply_MultivariatePolynomial_helper("1", "0", "0");
        multiply_MultivariatePolynomial_helper("1", "1", "1");
        multiply_MultivariatePolynomial_helper("1", "-17", "-17");
        multiply_MultivariatePolynomial_helper("1", "ooo", "ooo");
        multiply_MultivariatePolynomial_helper("1", "a*b*c", "a*b*c");
        multiply_MultivariatePolynomial_helper("1", "x^2-4*x+7", "x^2-4*x+7");
        multiply_MultivariatePolynomial_helper("1", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2");
        multiply_MultivariatePolynomial_helper("1", "a+b+c+d+e+f", "a+b+c+d+e+f");

        multiply_MultivariatePolynomial_helper("-17", "0", "0");
        multiply_MultivariatePolynomial_helper("-17", "1", "-17");
        multiply_MultivariatePolynomial_helper("-17", "-17", "289");
        multiply_MultivariatePolynomial_helper("-17", "ooo", "-17*ooo");
        multiply_MultivariatePolynomial_helper("-17", "a*b*c", "-17*a*b*c");
        multiply_MultivariatePolynomial_helper("-17", "x^2-4*x+7", "-17*x^2+68*x-119");
        multiply_MultivariatePolynomial_helper("-17", "x^2+2*x*y+y^2", "-17*x^2-34*x*y-17*y^2");
        multiply_MultivariatePolynomial_helper("-17", "a+b+c+d+e+f", "-17*a-17*b-17*c-17*d-17*e-17*f");

        multiply_MultivariatePolynomial_helper("ooo", "0", "0");
        multiply_MultivariatePolynomial_helper("ooo", "1", "ooo");
        multiply_MultivariatePolynomial_helper("ooo", "-17", "-17*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "ooo", "ooo^2");
        multiply_MultivariatePolynomial_helper("ooo", "a*b*c", "a*b*c*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "x^2-4*x+7", "x^2*ooo-4*x*ooo+7*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "x^2+2*x*y+y^2", "x^2*ooo+2*x*y*ooo+y^2*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "a+b+c+d+e+f", "a*ooo+b*ooo+c*ooo+d*ooo+e*ooo+f*ooo");

        multiply_MultivariatePolynomial_helper("a*b*c", "0", "0");
        multiply_MultivariatePolynomial_helper("a*b*c", "1", "a*b*c");
        multiply_MultivariatePolynomial_helper("a*b*c", "-17", "-17*a*b*c");
        multiply_MultivariatePolynomial_helper("a*b*c", "ooo", "a*b*c*ooo");
        multiply_MultivariatePolynomial_helper("a*b*c", "a*b*c", "a^2*b^2*c^2");
        multiply_MultivariatePolynomial_helper("a*b*c", "x^2-4*x+7", "a*b*c*x^2-4*a*b*c*x+7*a*b*c");
        multiply_MultivariatePolynomial_helper("a*b*c", "x^2+2*x*y+y^2", "a*b*c*x^2+2*a*b*c*x*y+a*b*c*y^2");
        multiply_MultivariatePolynomial_helper("a*b*c", "a+b+c+d+e+f",
                "a^2*b*c+a*b^2*c+a*b*c^2+a*b*c*d+a*b*c*e+a*b*c*f");

        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "0", "0");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "-17", "-17*x^2+68*x-119");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "ooo", "x^2*ooo-4*x*ooo+7*ooo");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "a*b*c", "a*b*c*x^2-4*a*b*c*x+7*a*b*c");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "x^2-4*x+7", "x^4-8*x^3+30*x^2-56*x+49");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "x^2+2*x*y+y^2",
                "x^4+2*x^3*y+x^2*y^2-4*x^3-8*x^2*y-4*x*y^2+7*x^2+14*x*y+7*y^2");
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "a+b+c+d+e+f",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2-4*a*x-4*b*x-4*c*x-4*d*x-4*e*x-4*f*x+7*a+7*b+7*c+7*d+7*e+7*f");

        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "0", "0");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "1", "x^2+2*x*y+y^2");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "-17", "-17*x^2-34*x*y-17*y^2");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "ooo", "x^2*ooo+2*x*y*ooo+y^2*ooo");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "a*b*c", "a*b*c*x^2+2*a*b*c*x*y+a*b*c*y^2");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "x^2-4*x+7",
                "x^4+2*x^3*y+x^2*y^2-4*x^3-8*x^2*y-4*x*y^2+7*x^2+14*x*y+7*y^2");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "x^4+4*x^3*y+6*x^2*y^2+4*x*y^3+y^4");
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "a+b+c+d+e+f",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2+2*a*x*y+2*b*x*y+2*c*x*y+2*d*x*y+2*e*x*y+2*f*x*y+a*y^2+b*y^2+" +
                "c*y^2+d*y^2+e*y^2+f*y^2");

        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "0", "0");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "-17", "-17*a-17*b-17*c-17*d-17*e-17*f");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "ooo", "a*ooo+b*ooo+c*ooo+d*ooo+e*ooo+f*ooo");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "a*b*c",
                "a^2*b*c+a*b^2*c+a*b*c^2+a*b*c*d+a*b*c*e+a*b*c*f");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "x^2-4*x+7",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2-4*a*x-4*b*x-4*c*x-4*d*x-4*e*x-4*f*x+7*a+7*b+7*c+7*d+7*e+7*f");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "x^2+2*x*y+y^2",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2+2*a*x*y+2*b*x*y+2*c*x*y+2*d*x*y+2*e*x*y+2*f*x*y+a*y^2+b*y^2+" +
                "c*y^2+d*y^2+e*y^2+f*y^2");
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "a+b+c+d+e+f",
                "a^2+2*a*b+b^2+2*a*c+2*b*c+c^2+2*a*d+2*b*d+2*c*d+d^2+2*a*e+2*b*e+2*c*e+2*d*e+e^2+2*a*f+2*b*f+2*c*f+" +
                "2*d*f+2*e*f+f^2");
    }

    private static void shiftLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        MultivariatePolynomial q = read(p).get().shiftLeft(bits);
        q.validate();
        aeq(q, output);
    }

    private static void shiftLeft_fail_helper(@NotNull String p, int bits) {
        try {
            read(p).get().shiftLeft(bits);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("0", 0, "0");
        shiftLeft_helper("0", 1, "0");
        shiftLeft_helper("0", 2, "0");
        shiftLeft_helper("0", 3, "0");
        shiftLeft_helper("0", 4, "0");

        shiftLeft_helper("1", 0, "1");
        shiftLeft_helper("1", 1, "2");
        shiftLeft_helper("1", 2, "4");
        shiftLeft_helper("1", 3, "8");
        shiftLeft_helper("1", 4, "16");

        shiftLeft_helper("-17", 0, "-17");
        shiftLeft_helper("-17", 1, "-34");
        shiftLeft_helper("-17", 2, "-68");
        shiftLeft_helper("-17", 3, "-136");
        shiftLeft_helper("-17", 4, "-272");

        shiftLeft_helper("ooo", 0, "ooo");
        shiftLeft_helper("ooo", 1, "2*ooo");
        shiftLeft_helper("ooo", 2, "4*ooo");
        shiftLeft_helper("ooo", 3, "8*ooo");
        shiftLeft_helper("ooo", 4, "16*ooo");

        shiftLeft_helper("a*b*c", 0, "a*b*c");
        shiftLeft_helper("a*b*c", 1, "2*a*b*c");
        shiftLeft_helper("a*b*c", 2, "4*a*b*c");
        shiftLeft_helper("a*b*c", 3, "8*a*b*c");
        shiftLeft_helper("a*b*c", 4, "16*a*b*c");

        shiftLeft_helper("x^2-4*x+7", 0, "x^2-4*x+7");
        shiftLeft_helper("x^2-4*x+7", 1, "2*x^2-8*x+14");
        shiftLeft_helper("x^2-4*x+7", 2, "4*x^2-16*x+28");
        shiftLeft_helper("x^2-4*x+7", 3, "8*x^2-32*x+56");
        shiftLeft_helper("x^2-4*x+7", 4, "16*x^2-64*x+112");

        shiftLeft_helper("x^2+2*x*y+y^2", 0, "x^2+2*x*y+y^2");
        shiftLeft_helper("x^2+2*x*y+y^2", 1, "2*x^2+4*x*y+2*y^2");
        shiftLeft_helper("x^2+2*x*y+y^2", 2, "4*x^2+8*x*y+4*y^2");
        shiftLeft_helper("x^2+2*x*y+y^2", 3, "8*x^2+16*x*y+8*y^2");
        shiftLeft_helper("x^2+2*x*y+y^2", 4, "16*x^2+32*x*y+16*y^2");

        shiftLeft_helper("a+b+c+d+e+f", 0, "a+b+c+d+e+f");
        shiftLeft_helper("a+b+c+d+e+f", 1, "2*a+2*b+2*c+2*d+2*e+2*f");
        shiftLeft_helper("a+b+c+d+e+f", 2, "4*a+4*b+4*c+4*d+4*e+4*f");
        shiftLeft_helper("a+b+c+d+e+f", 3, "8*a+8*b+8*c+8*d+8*e+8*f");
        shiftLeft_helper("a+b+c+d+e+f", 4, "16*a+16*b+16*c+16*d+16*e+16*f");

        shiftLeft_fail_helper("0", -1);
        shiftLeft_fail_helper("1", -1);
        shiftLeft_fail_helper("a*b*c", -1);
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readMultivariatePolynomialList("[0, 1, ooo, -17, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2, a+b+c+d+e+f]"),
                readMultivariatePolynomialList("[0, 1, ooo, -17, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2, a+b+c+d+e+f]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("0", 1);
        hashCode_helper("1", 63);
        hashCode_helper("-17", 45);
        hashCode_helper("ooo", -246313024);
        hashCode_helper("a*b*c", 954336);
        hashCode_helper("x^2-4*x+7", 992701033);
        hashCode_helper("x^2+2*x*y+y^2", 469506170);
        hashCode_helper("a+b+c+d+e+f", -1175206393);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readMultivariatePolynomialList("[0, -17, 1, ooo, a+b+c+d+e+f, x^2-4*x+7, x^2+2*x*y+y^2, a*b*c]")
        );
    }

    private static void read_helper(@NotNull String input) {
        MultivariatePolynomial p = read(input).get();
        p.validate();
        aeq(p, input);
    }

    private static void read_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead() {
        read_helper("0");
        read_helper("1");
        read_helper("-17");
        read_helper("ooo");
        read_helper("-ooo");
        read_helper("a*b*c");
        read_helper("x^2-4*x+7");
        read_helper("a+b+c+d+e+f");
        read_fail_helper("");
        read_fail_helper("hello");
        read_fail_helper(" ");
        read_fail_helper("00");
        read_fail_helper("-0");
        read_fail_helper("1/2");
        read_fail_helper("1*x");
        read_fail_helper("x*2");
        read_fail_helper("0*x");
        read_fail_helper("-1*x");
        read_fail_helper("a*a");
        read_fail_helper("a^1");
        read_fail_helper("a^0");
        read_fail_helper("a^-1");
        read_fail_helper("b*a");
        read_fail_helper("-4*x+x^2+7");
        read_fail_helper("b+a+c+d+e+f");
        read_fail_helper("a+a");
        read_fail_helper("a+0");
        read_fail_helper("a+-1");
        read_fail_helper("*x");
        read_fail_helper("+x");
        read_fail_helper("+1");
        read_fail_helper("+0");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<MultivariatePolynomial, Integer> result = findIn(input).get();
        result.a.validate();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("foobar", "f", 0);
        findIn_helper("oobar", "oo", 0);
        findIn_helper("3*x^2", "3*x^2", 0);
        findIn_helper("03*x^2", "0", 0);
        findIn_helper("^3*x^2", "3*x^2", 1);

        findIn_fail_helper("");
        findIn_fail_helper("*");
        findIn_fail_helper("^");
        findIn_fail_helper("-");
        findIn_fail_helper("+");
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

    private static @NotNull List<MultivariatePolynomial> readMultivariatePolynomialList(@NotNull String s) {
        return Readers.readList(MultivariatePolynomial::read).apply(s).get();
    }
}
