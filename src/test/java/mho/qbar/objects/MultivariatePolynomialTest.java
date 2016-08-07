package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static mho.qbar.objects.MultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.fail;

public class MultivariatePolynomialTest {
    private static void constant_helper(@NotNull MultivariatePolynomial input, @NotNull String output) {
        input.validate();
        aeq(input, output);
    }

    @Test
    public void testConstants() {
        constant_helper(ZERO, "0");
        constant_helper(ONE, "1");
    }

    private static void iterable_helper(@NotNull String p, @NotNull String mo, @NotNull String output) {
        aeq(toList(readStrict(p).get().iterable(MonomialOrder.readStrict(mo).get())), output);
    }

    @Test
    public void testIterable() {
        iterable_helper("0", "LEX", "[]");
        iterable_helper("0", "GRLEX", "[]");
        iterable_helper("0", "GREVLEX", "[]");

        iterable_helper("1", "LEX", "[(1, 1)]");
        iterable_helper("1", "GRLEX", "[(1, 1)]");
        iterable_helper("1", "GREVLEX", "[(1, 1)]");

        iterable_helper("-17", "LEX", "[(1, -17)]");
        iterable_helper("-17", "GRLEX", "[(1, -17)]");
        iterable_helper("-17", "GREVLEX", "[(1, -17)]");

        iterable_helper("ooo", "LEX", "[(ooo, 1)]");
        iterable_helper("ooo", "GRLEX", "[(ooo, 1)]");
        iterable_helper("ooo", "GREVLEX", "[(ooo, 1)]");

        iterable_helper("a*b*c", "LEX", "[(a*b*c, 1)]");
        iterable_helper("a*b*c", "GRLEX", "[(a*b*c, 1)]");
        iterable_helper("a*b*c", "GREVLEX", "[(a*b*c, 1)]");

        iterable_helper("x^2-4*x+7", "LEX", "[(1, 7), (x, -4), (x^2, 1)]");
        iterable_helper("x^2-4*x+7", "GRLEX", "[(1, 7), (x, -4), (x^2, 1)]");
        iterable_helper("x^2-4*x+7", "GREVLEX", "[(1, 7), (x, -4), (x^2, 1)]");

        iterable_helper("x^2+2*x*y+y^2", "LEX", "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        iterable_helper("x^2+2*x*y+y^2", "GRLEX", "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        iterable_helper("x^2+2*x*y+y^2", "GREVLEX", "[(y^2, 1), (x*y, 2), (x^2, 1)]");

        iterable_helper("a+b+c+d+e+f", "LEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterable_helper("a+b+c+d+e+f", "GRLEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterable_helper("a+b+c+d+e+f", "GREVLEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");

        iterable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX", "[(z^2, 1), (x*y^2*z, 1), (x^2*z^2, 1), (x^3, 1)]");
        iterable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX", "[(z^2, 1), (x^3, 1), (x*y^2*z, 1), (x^2*z^2, 1)]");
        iterable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX", "[(z^2, 1), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");
    }

    private static void iterator_helper(@NotNull String x, @NotNull String output) {
        aeq(toList(readStrict(x).get()), output);
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
        iterator_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(z^2, 1), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");
    }

    private static void toRationalMultivariatePolynomial_helper(@NotNull String input) {
        aeq(readStrict(input).get().toRationalMultivariatePolynomial(), input);
    }

    @Test
    public void testToRationalMultivariatePolynomial() {
        toRationalMultivariatePolynomial_helper("0");
        toRationalMultivariatePolynomial_helper("1");
        toRationalMultivariatePolynomial_helper("-17");
        toRationalMultivariatePolynomial_helper("ooo");
        toRationalMultivariatePolynomial_helper("a*b*c");
        toRationalMultivariatePolynomial_helper("x^2-4*x+7");
        toRationalMultivariatePolynomial_helper("x^2+2*x*y+y^2");
        toRationalMultivariatePolynomial_helper("a+b+c+d+e+f");
        toRationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2");
    }

    private static void coefficient_helper(@NotNull String p, @NotNull String ev, @NotNull String output) {
        aeq(readStrict(p).get().coefficient(Monomial.readStrict(ev).get()), output);
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

    private static void of_List_Pair_Monomial_BigInteger_helper(@NotNull String input, @NotNull String output) {
        MultivariatePolynomial p = of(readMonomialBigIntegerPairList(input));
        p.validate();
        aeq(p, output);
    }

    private static void of_List_Pair_Monomial_BigInteger_fail_helper(@NotNull String input) {
        try {
            of(readMonomialBigIntegerPairListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Pair_Monomial_BigInteger() {
        of_List_Pair_Monomial_BigInteger_helper("[]", "0");
        of_List_Pair_Monomial_BigInteger_helper("[(a*b^2, 0)]", "0");
        of_List_Pair_Monomial_BigInteger_helper("[(1, 1)]", "1");
        of_List_Pair_Monomial_BigInteger_helper("[(1, 1), (x, 0)]", "1");
        of_List_Pair_Monomial_BigInteger_helper("[(ooo, 1)]", "ooo");
        of_List_Pair_Monomial_BigInteger_helper("[(1, -17)]", "-17");
        of_List_Pair_Monomial_BigInteger_helper("[(1, -10), (1, -7)]", "-17");
        of_List_Pair_Monomial_BigInteger_helper("[(a*b*c, 1)]", "a*b*c");
        of_List_Pair_Monomial_BigInteger_helper("[(1, 7), (x, -4), (x^2, 1)]", "x^2-4*x+7");
        of_List_Pair_Monomial_BigInteger_helper("[(x, -4), (1, 7), (x^2, 1)]", "x^2-4*x+7");
        of_List_Pair_Monomial_BigInteger_helper("[(y^2, 1), (x*y, 2), (x^2, 1)]", "x^2+2*x*y+y^2");
        of_List_Pair_Monomial_BigInteger_helper("[(x*y, 1), (x*y, 1), (y^2, 1), (x^2, 1), (z, 0)]",
                "x^2+2*x*y+y^2");
        of_List_Pair_Monomial_BigInteger_helper("[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]",
                "a+b+c+d+e+f");
        of_List_Pair_Monomial_BigInteger_helper("[(f, 1), (b, 1), (d, 2), (d, -1), (c, 1), (e, 1), (a, 1)]",
                "a+b+c+d+e+f");

        of_List_Pair_Monomial_BigInteger_fail_helper("[(ooo, 1), null]");
        of_List_Pair_Monomial_BigInteger_fail_helper("[(ooo, null)]");
        of_List_Pair_Monomial_BigInteger_fail_helper("[(null, 1)]");
    }

    private static void of_Monomial_BigInteger_helper(@NotNull String ev, @NotNull String c, @NotNull String output) {
        MultivariatePolynomial p = of(Monomial.readStrict(ev).get(), Readers.readBigIntegerStrict(c).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testOf_Monomial_BigInteger() {
        of_Monomial_BigInteger_helper("1", "0", "0");
        of_Monomial_BigInteger_helper("1", "1", "1");
        of_Monomial_BigInteger_helper("1", "-1", "-1");
        of_Monomial_BigInteger_helper("1", "3", "3");
        of_Monomial_BigInteger_helper("1", "-5", "-5");
        of_Monomial_BigInteger_helper("ooo", "0", "0");
        of_Monomial_BigInteger_helper("ooo", "1", "ooo");
        of_Monomial_BigInteger_helper("ooo", "-1", "-ooo");
        of_Monomial_BigInteger_helper("ooo", "3", "3*ooo");
        of_Monomial_BigInteger_helper("ooo", "-5", "-5*ooo");
        of_Monomial_BigInteger_helper("a*b^2", "0", "0");
        of_Monomial_BigInteger_helper("a*b^2", "1", "a*b^2");
        of_Monomial_BigInteger_helper("a*b^2", "-1", "-a*b^2");
        of_Monomial_BigInteger_helper("a*b^2", "3", "3*a*b^2");
        of_Monomial_BigInteger_helper("a*b^2", "-5", "-5*a*b^2");
    }

    private static void of_BigInteger_helper(@NotNull String input) {
        MultivariatePolynomial p = of(Readers.readBigIntegerStrict(input).get());
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

    private static void of_Variable(@NotNull String input) {
        MultivariatePolynomial p = of(Variable.readStrict(input).get());
        p.validate();
        aeq(p, input);
    }

    @Test
    public void testOf_Variable() {
        of_Variable("a");
        of_Variable("b");
        of_Variable("c");
        of_Variable("x");
        of_Variable("y");
        of_Variable("z");
        of_Variable("ooo");
    }

    private static void of_Polynomial_Variable_helper(@NotNull String p, @NotNull String v, @NotNull String output) {
        MultivariatePolynomial q = of(Polynomial.readStrict(p).get(), Variable.readStrict(v).get());
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
        aeq(readStrict(input).get().toPolynomial(), output);
    }

    private static void toPolynomial_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().toPolynomial();
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
        aeq(readStrict(input).get().variables(), output);
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
        aeq(readStrict(input).get().variableCount(), output);
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
        variableCount_helper("x*y^2*z+x^2*z^2+x^3+z^2", 3);
    }

    private static void termCount_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().termCount(), output);
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
        termCount_helper("x*y^2*z+x^2*z^2+x^3+z^2", 4);
    }

    private static void maxCoefficientBitLength_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().maxCoefficientBitLength(), output);
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
        maxCoefficientBitLength_helper("x*y^2*z+x^2*z^2+x^3+z^2", 1);
    }

    private static void degree_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().degree(), output);
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
        degree_helper("x*y^2*z+x^2*z^2+x^3+z^2", 4);
    }

    private static void degree_Variable_helper(@NotNull String p, @NotNull String v, int output) {
        aeq(readStrict(p).get().degree(Variable.readStrict(v).get()), output);
    }

    @Test
    public void testDegree_Variable() {
        degree_Variable_helper("0", "a", -1);
        degree_Variable_helper("1", "a", 0);
        degree_Variable_helper("-17", "a", 0);
        degree_Variable_helper("ooo", "a", 0);
        degree_Variable_helper("ooo", "ooo", 1);
        degree_Variable_helper("a*b*c", "a", 1);
        degree_Variable_helper("a*b*c", "b", 1);
        degree_Variable_helper("a*b*c", "c", 1);
        degree_Variable_helper("a*b*c", "d", 0);
        degree_Variable_helper("x^2-4*x+7", "a", 0);
        degree_Variable_helper("x^2-4*x+7", "x", 2);
        degree_Variable_helper("x^2+2*x*y+y^2", "a", 0);
        degree_Variable_helper("x^2+2*x*y+y^2", "x", 2);
        degree_Variable_helper("x^2+2*x*y+y^2", "y", 2);
        degree_Variable_helper("a+b+c+d+e+f", "a", 1);
        degree_Variable_helper("a+b+c+d+e+f", "f", 1);
        degree_Variable_helper("a+b+c+d+e+f", "g", 0);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a", 0);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x", 3);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "y", 2);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "z", 2);
    }

    private static void isHomogeneous_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isHomogeneous(), output);
    }

    @Test
    public void testIsHomogeneous() {
        isHomogeneous_helper("0", true);
        isHomogeneous_helper("1", true);
        isHomogeneous_helper("-17", true);
        isHomogeneous_helper("ooo", true);
        isHomogeneous_helper("a*b*c", true);
        isHomogeneous_helper("x^2-4*x+7", false);
        isHomogeneous_helper("x^2+2*x*y+y^2", true);
        isHomogeneous_helper("a+b+c+d+e+f", true);
        isHomogeneous_helper("x*y^2*z+x^2*z^2+x^3+z^2", false);
    }

    private static void leadingTerm_MonomialOrder_helper(
            @NotNull String p,
            @NotNull String o,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().leadingTerm(MonomialOrder.readStrict(o).get()), output);
    }

    @Test
    public void testLeadingTerm_MonomialOrder() {
        leadingTerm_MonomialOrder_helper("0", "LEX", "Optional.empty");
        leadingTerm_MonomialOrder_helper("0", "GRLEX", "Optional.empty");
        leadingTerm_MonomialOrder_helper("0", "GREVLEX", "Optional.empty");
        leadingTerm_MonomialOrder_helper("1", "LEX", "Optional[(1, 1)]");
        leadingTerm_MonomialOrder_helper("1", "GRLEX", "Optional[(1, 1)]");
        leadingTerm_MonomialOrder_helper("1", "GREVLEX", "Optional[(1, 1)]");
        leadingTerm_MonomialOrder_helper("-17", "LEX", "Optional[(1, -17)]");
        leadingTerm_MonomialOrder_helper("-17", "GRLEX", "Optional[(1, -17)]");
        leadingTerm_MonomialOrder_helper("-17", "GREVLEX", "Optional[(1, -17)]");
        leadingTerm_MonomialOrder_helper("ooo", "LEX", "Optional[(ooo, 1)]");
        leadingTerm_MonomialOrder_helper("ooo", "GRLEX", "Optional[(ooo, 1)]");
        leadingTerm_MonomialOrder_helper("ooo", "GREVLEX", "Optional[(ooo, 1)]");
        leadingTerm_MonomialOrder_helper("a*b*c", "LEX", "Optional[(a*b*c, 1)]");
        leadingTerm_MonomialOrder_helper("a*b*c", "GRLEX", "Optional[(a*b*c, 1)]");
        leadingTerm_MonomialOrder_helper("a*b*c", "GREVLEX", "Optional[(a*b*c, 1)]");
        leadingTerm_MonomialOrder_helper("x^2-4*x+7", "LEX", "Optional[(x^2, 1)]");
        leadingTerm_MonomialOrder_helper("x^2-4*x+7", "GRLEX", "Optional[(x^2, 1)]");
        leadingTerm_MonomialOrder_helper("x^2-4*x+7", "GREVLEX", "Optional[(x^2, 1)]");
        leadingTerm_MonomialOrder_helper("x^2+2*x*y+y^2", "LEX", "Optional[(x^2, 1)]");
        leadingTerm_MonomialOrder_helper("x^2+2*x*y+y^2", "GRLEX", "Optional[(x^2, 1)]");
        leadingTerm_MonomialOrder_helper("x^2+2*x*y+y^2", "GREVLEX", "Optional[(x^2, 1)]");
        leadingTerm_MonomialOrder_helper("a+b+c+d+e+f", "LEX", "Optional[(a, 1)]");
        leadingTerm_MonomialOrder_helper("a+b+c+d+e+f", "GRLEX", "Optional[(a, 1)]");
        leadingTerm_MonomialOrder_helper("a+b+c+d+e+f", "GREVLEX", "Optional[(a, 1)]");
        leadingTerm_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX", "Optional[(x^3, 1)]");
        leadingTerm_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX", "Optional[(x^2*z^2, 1)]");
        leadingTerm_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX", "Optional[(x*y^2*z, 1)]");
    }

    private static void leadingTerm_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().leadingTerm(), output);
    }

    @Test
    public void testLeadingTerm() {
        leadingTerm_helper("0", "Optional.empty");
        leadingTerm_helper("1", "Optional[(1, 1)]");
        leadingTerm_helper("-17", "Optional[(1, -17)]");
        leadingTerm_helper("ooo", "Optional[(ooo, 1)]");
        leadingTerm_helper("a*b*c", "Optional[(a*b*c, 1)]");
        leadingTerm_helper("x^2-4*x+7", "Optional[(x^2, 1)]");
        leadingTerm_helper("x^2+2*x*y+y^2", "Optional[(x^2, 1)]");
        leadingTerm_helper("a+b+c+d+e+f", "Optional[(a, 1)]");
        leadingTerm_helper("x*y^2*z+x^2*z^2+x^3+z^2", "Optional[(x*y^2*z, 1)]");
    }

    private static void leadingCoefficient_MonomialOrder_helper(
            @NotNull String p,
            @NotNull String o,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().leadingCoefficient(MonomialOrder.readStrict(o).get()), output);
    }

    @Test
    public void testLeadingCoefficient_MonomialOrder() {
        leadingCoefficient_MonomialOrder_helper("0", "LEX", "Optional.empty");
        leadingCoefficient_MonomialOrder_helper("0", "GRLEX", "Optional.empty");
        leadingCoefficient_MonomialOrder_helper("0", "GREVLEX", "Optional.empty");
        leadingCoefficient_MonomialOrder_helper("1", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("1", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("1", "GREVLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("-17", "LEX", "Optional[-17]");
        leadingCoefficient_MonomialOrder_helper("-17", "GRLEX", "Optional[-17]");
        leadingCoefficient_MonomialOrder_helper("-17", "GREVLEX", "Optional[-17]");
        leadingCoefficient_MonomialOrder_helper("ooo", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("ooo", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("ooo", "GREVLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("a*b*c", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("a*b*c", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("a*b*c", "GREVLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x^2-4*x+7", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x^2-4*x+7", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x^2-4*x+7", "GREVLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x^2+2*x*y+y^2", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x^2+2*x*y+y^2", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x^2+2*x*y+y^2", "GREVLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("a+b+c+d+e+f", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("a+b+c+d+e+f", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("a+b+c+d+e+f", "GREVLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX", "Optional[1]");
        leadingCoefficient_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX", "Optional[1]");
    }

    private static void leadingCoefficient_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().leadingCoefficient(), output);
    }

    @Test
    public void testLeadingCoefficient() {
        leadingCoefficient_helper("0", "Optional.empty");
        leadingCoefficient_helper("1", "Optional[1]");
        leadingCoefficient_helper("-17", "Optional[-17]");
        leadingCoefficient_helper("ooo", "Optional[1]");
        leadingCoefficient_helper("a*b*c", "Optional[1]");
        leadingCoefficient_helper("x^2-4*x+7", "Optional[1]");
        leadingCoefficient_helper("x^2+2*x*y+y^2", "Optional[1]");
        leadingCoefficient_helper("a+b+c+d+e+f", "Optional[1]");
        leadingCoefficient_helper("x*y^2*z+x^2*z^2+x^3+z^2", "Optional[1]");
    }

    private static void leadingMonomial_MonomialOrder_helper(
            @NotNull String p,
            @NotNull String o,
            @NotNull String output
    ) {
        aeq(readStrict(p).get().leadingMonomial(MonomialOrder.readStrict(o).get()), output);
    }

    @Test
    public void testLeadingMonomial_MonomialOrder() {
        leadingMonomial_MonomialOrder_helper("0", "LEX", "Optional.empty");
        leadingMonomial_MonomialOrder_helper("0", "GRLEX", "Optional.empty");
        leadingMonomial_MonomialOrder_helper("0", "GREVLEX", "Optional.empty");
        leadingMonomial_MonomialOrder_helper("1", "LEX", "Optional[1]");
        leadingMonomial_MonomialOrder_helper("1", "GRLEX", "Optional[1]");
        leadingMonomial_MonomialOrder_helper("1", "GREVLEX", "Optional[1]");
        leadingMonomial_MonomialOrder_helper("-17", "LEX", "Optional[1]");
        leadingMonomial_MonomialOrder_helper("-17", "GRLEX", "Optional[1]");
        leadingMonomial_MonomialOrder_helper("-17", "GREVLEX", "Optional[1]");
        leadingMonomial_MonomialOrder_helper("ooo", "LEX", "Optional[ooo]");
        leadingMonomial_MonomialOrder_helper("ooo", "GRLEX", "Optional[ooo]");
        leadingMonomial_MonomialOrder_helper("ooo", "GREVLEX", "Optional[ooo]");
        leadingMonomial_MonomialOrder_helper("a*b*c", "LEX", "Optional[a*b*c]");
        leadingMonomial_MonomialOrder_helper("a*b*c", "GRLEX", "Optional[a*b*c]");
        leadingMonomial_MonomialOrder_helper("a*b*c", "GREVLEX", "Optional[a*b*c]");
        leadingMonomial_MonomialOrder_helper("x^2-4*x+7", "LEX", "Optional[x^2]");
        leadingMonomial_MonomialOrder_helper("x^2-4*x+7", "GRLEX", "Optional[x^2]");
        leadingMonomial_MonomialOrder_helper("x^2-4*x+7", "GREVLEX", "Optional[x^2]");
        leadingMonomial_MonomialOrder_helper("x^2+2*x*y+y^2", "LEX", "Optional[x^2]");
        leadingMonomial_MonomialOrder_helper("x^2+2*x*y+y^2", "GRLEX", "Optional[x^2]");
        leadingMonomial_MonomialOrder_helper("x^2+2*x*y+y^2", "GREVLEX", "Optional[x^2]");
        leadingMonomial_MonomialOrder_helper("a+b+c+d+e+f", "LEX", "Optional[a]");
        leadingMonomial_MonomialOrder_helper("a+b+c+d+e+f", "GRLEX", "Optional[a]");
        leadingMonomial_MonomialOrder_helper("a+b+c+d+e+f", "GREVLEX", "Optional[a]");
        leadingMonomial_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX", "Optional[x^3]");
        leadingMonomial_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX", "Optional[x^2*z^2]");
        leadingMonomial_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX", "Optional[x*y^2*z]");
    }

    private static void leadingMonomial_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().leadingMonomial(), output);
    }

    @Test
    public void testLeadingMonomial() {
        leadingMonomial_helper("0", "Optional.empty");
        leadingMonomial_helper("1", "Optional[1]");
        leadingMonomial_helper("-17", "Optional[1]");
        leadingMonomial_helper("ooo", "Optional[ooo]");
        leadingMonomial_helper("a*b*c", "Optional[a*b*c]");
        leadingMonomial_helper("x^2-4*x+7", "Optional[x^2]");
        leadingMonomial_helper("x^2+2*x*y+y^2", "Optional[x^2]");
        leadingMonomial_helper("a+b+c+d+e+f", "Optional[a]");
        leadingMonomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "Optional[x*y^2*z]");
    }

    private void coefficientsOfVariable_helper(@NotNull String p, @NotNull String v, @NotNull String output) {
        List<MultivariatePolynomial> ps = readStrict(p).get().coefficientsOfVariable(Variable.readStrict(v).get());
        ps.forEach(MultivariatePolynomial::validate);
        aeq(ps, output);
    }

    @Test
    public void testCoefficientsOfVariable() {
        coefficientsOfVariable_helper("0", "a", "[]");
        coefficientsOfVariable_helper("1", "a", "[1]");
        coefficientsOfVariable_helper("-17", "a", "[-17]");
        coefficientsOfVariable_helper("ooo", "a", "[ooo]");
        coefficientsOfVariable_helper("ooo", "ooo", "[0, 1]");
        coefficientsOfVariable_helper("a*b*c", "z", "[a*b*c]");
        coefficientsOfVariable_helper("a*b*c", "a", "[0, b*c]");
        coefficientsOfVariable_helper("a*b*c", "b", "[0, a*c]");
        coefficientsOfVariable_helper("a*b*c", "c", "[0, a*b]");
        coefficientsOfVariable_helper("x^2-4*x+7", "a", "[x^2-4*x+7]");
        coefficientsOfVariable_helper("x^2-4*x+7", "x", "[7, -4, 1]");
        coefficientsOfVariable_helper("x^2+2*x*y+y^2", "a", "[x^2+2*x*y+y^2]");
        coefficientsOfVariable_helper("x^2+2*x*y+y^2", "x", "[y^2, 2*y, 1]");
        coefficientsOfVariable_helper("x^2+2*x*y+y^2", "y", "[x^2, 2*x, 1]");
        coefficientsOfVariable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x", "[z^2, y^2*z, z^2, 1]");
        coefficientsOfVariable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "y", "[x^2*z^2+x^3+z^2, 0, x*z]");
        coefficientsOfVariable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "z", "[x^3, x*y^2, x^2+1]");
    }

    private void groupVariables_List_Variable_MonomialOrder_helper(
            @NotNull String p,
            @NotNull String vs,
            @NotNull String o,
            @NotNull String output
    ) {
        List<Pair<Monomial, MultivariatePolynomial>> ps = readStrict(p).get()
                .groupVariables(readVariableList(vs), MonomialOrder.readStrict(o).get());
        ps.forEach(q -> q.b.validate());
        aeq(ps, output);
    }

    private void groupVariables_List_Variable_MonomialOrder_fail_helper(
            @NotNull String p,
            @NotNull String vs,
            @NotNull String o
    ) {
        try {
            readStrict(p).get().groupVariables(readVariableListWithNulls(vs), MonomialOrder.readStrict(o).get());
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testGroupVariables_List_Variable_MonomialOrder() {
        groupVariables_List_Variable_MonomialOrder_helper("0", "[]", "LEX", "[]");
        groupVariables_List_Variable_MonomialOrder_helper("0", "[]", "GRLEX", "[]");
        groupVariables_List_Variable_MonomialOrder_helper("0", "[]", "GREVLEX", "[]");
        groupVariables_List_Variable_MonomialOrder_helper("0", "[a]", "LEX", "[]");
        groupVariables_List_Variable_MonomialOrder_helper("0", "[a]", "GRLEX", "[]");
        groupVariables_List_Variable_MonomialOrder_helper("0", "[a]", "GREVLEX", "[]");
        groupVariables_List_Variable_MonomialOrder_helper("1", "[]", "LEX", "[(1, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("1", "[]", "GRLEX", "[(1, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("1", "[]", "GREVLEX", "[(1, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("1", "[a]", "LEX", "[(1, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("1", "[a]", "GRLEX", "[(1, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("1", "[a]", "GREVLEX", "[(1, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("-17", "[]", "LEX", "[(1, -17)]");
        groupVariables_List_Variable_MonomialOrder_helper("-17", "[]", "GRLEX", "[(1, -17)]");
        groupVariables_List_Variable_MonomialOrder_helper("-17", "[]", "GREVLEX", "[(1, -17)]");
        groupVariables_List_Variable_MonomialOrder_helper("-17", "[a]", "LEX", "[(1, -17)]");
        groupVariables_List_Variable_MonomialOrder_helper("-17", "[a]", "GRLEX", "[(1, -17)]");
        groupVariables_List_Variable_MonomialOrder_helper("-17", "[a]", "GREVLEX", "[(1, -17)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[]", "LEX", "[(1, ooo)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[]", "GRLEX", "[(1, ooo)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[]", "GREVLEX", "[(1, ooo)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[ooo]", "LEX", "[(ooo, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[ooo]", "GRLEX", "[(ooo, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[ooo]", "GREVLEX", "[(ooo, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[a, ooo]", "LEX", "[(ooo, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[a, ooo]", "GRLEX", "[(ooo, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("ooo", "[a, ooo]", "GREVLEX", "[(ooo, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[]", "LEX", "[(1, a*b*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[]", "GRLEX", "[(1, a*b*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[]", "GREVLEX", "[(1, a*b*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a]", "LEX", "[(a, b*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a]", "GRLEX", "[(a, b*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a]", "GREVLEX", "[(a, b*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[b]", "LEX", "[(b, a*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[b]", "GRLEX", "[(b, a*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[b]", "GREVLEX", "[(b, a*c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[c]", "LEX", "[(c, a*b)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[c]", "GRLEX", "[(c, a*b)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[c]", "GREVLEX", "[(c, a*b)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, b]", "LEX", "[(a*b, c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, b]", "GRLEX", "[(a*b, c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, b]", "GREVLEX", "[(a*b, c)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, c]", "LEX", "[(a*c, b)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, c]", "GRLEX", "[(a*c, b)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, c]", "GREVLEX", "[(a*c, b)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[b, c]", "LEX", "[(b*c, a)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[b, c]", "GRLEX", "[(b*c, a)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[b, c]", "GREVLEX", "[(b*c, a)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, b, c]", "LEX", "[(a*b*c, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, b, c]", "GRLEX", "[(a*b*c, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("a*b*c", "[a, b, c]", "GREVLEX", "[(a*b*c, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-4*x+7", "[]", "LEX", "[(1, x^2-4*x+7)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-4*x+7", "[]", "GRLEX", "[(1, x^2-4*x+7)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-4*x+7", "[]", "GREVLEX", "[(1, x^2-4*x+7)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-4*x+7", "[x]", "LEX", "[(1, 7), (x, -4), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-4*x+7", "[x]", "GRLEX", "[(1, 7), (x, -4), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-4*x+7", "[x]", "GREVLEX",
                "[(1, 7), (x, -4), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[]", "LEX", "[(1, x^2+2*x*y+y^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[]", "GRLEX", "[(1, x^2+2*x*y+y^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[]", "GREVLEX", "[(1, x^2+2*x*y+y^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[x]", "LEX",
                "[(1, y^2), (x, 2*y), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[x]", "GRLEX",
                "[(1, y^2), (x, 2*y), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[x]", "GREVLEX",
                "[(1, y^2), (x, 2*y), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[y]", "LEX",
                "[(1, x^2), (y, 2*x), (y^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[y]", "GRLEX",
                "[(1, x^2), (y, 2*x), (y^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[y]", "GREVLEX",
                "[(1, x^2), (y, 2*x), (y^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[x, y]", "LEX",
                "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[x, y]", "GRLEX",
                "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+2*x*y+y^2", "[x, y]", "GREVLEX",
                "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[]", "LEX",
                "[(1, x*y^2*z+x^2*z^2+x^3+z^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[]", "GRLEX",
                "[(1, x*y^2*z+x^2*z^2+x^3+z^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[]", "GREVLEX",
                "[(1, x*y^2*z+x^2*z^2+x^3+z^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x]", "LEX",
                "[(1, z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x]", "GRLEX",
                "[(1, z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x]", "GREVLEX",
                "[(1, z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y]", "LEX",
                "[(1, x^2*z^2+x^3+z^2), (y^2, x*z)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y]", "GRLEX",
                "[(1, x^2*z^2+x^3+z^2), (y^2, x*z)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y]", "GREVLEX",
                "[(1, x^2*z^2+x^3+z^2), (y^2, x*z)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[z]", "LEX",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[z]", "GRLEX",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[z]", "GREVLEX",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y]", "LEX",
                "[(1, z^2), (x*y^2, z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y]", "GRLEX",
                "[(1, z^2), (x^2, z^2), (x*y^2, z), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y]", "GREVLEX",
                "[(1, z^2), (x^2, z^2), (x*y^2, z), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, z]", "LEX",
                "[(z^2, 1), (x*z, y^2), (x^2*z^2, 1), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, z]", "GRLEX",
                "[(z^2, 1), (x*z, y^2), (x^3, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, z]", "GREVLEX",
                "[(z^2, 1), (x*z, y^2), (x^3, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y, z]", "LEX",
                "[(1, x^3), (z^2, x^2+1), (y^2*z, x)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y, z]", "GRLEX",
                "[(1, x^3), (z^2, x^2+1), (y^2*z, x)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y, z]", "GREVLEX",
                "[(1, x^3), (z^2, x^2+1), (y^2*z, x)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y, z]", "LEX",
                "[(z^2, 1), (x*y^2*z, 1), (x^2*z^2, 1), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y, z]", "GRLEX",
                "[(z^2, 1), (x^3, 1), (x*y^2*z, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y, z]", "GREVLEX",
                "[(z^2, 1), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");

        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[null]", "LEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[null]", "GRLEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[null]", "GREVLEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[a, null, c]", "LEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[a, null, c]", "GRLEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[a, null, c]", "GREVLEX");
    }

    private void groupVariables_List_Variable_helper(@NotNull String p, @NotNull String vs, @NotNull String output) {
        List<Pair<Monomial, MultivariatePolynomial>> ps = readStrict(p).get().groupVariables(readVariableList(vs));
        ps.forEach(q -> q.b.validate());
        aeq(ps, output);
    }

    private void groupVariables_List_Variable_fail_helper(@NotNull String p, @NotNull String vs) {
        try {
            readStrict(p).get().groupVariables(readVariableListWithNulls(vs));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testGroupVariables_List_Variable() {
        groupVariables_List_Variable_helper("0", "[]", "[]");
        groupVariables_List_Variable_helper("0", "[a]", "[]");
        groupVariables_List_Variable_helper("1", "[]", "[(1, 1)]");
        groupVariables_List_Variable_helper("1", "[a]", "[(1, 1)]");
        groupVariables_List_Variable_helper("-17", "[]", "[(1, -17)]");
        groupVariables_List_Variable_helper("-17", "[a]", "[(1, -17)]");
        groupVariables_List_Variable_helper("ooo", "[]", "[(1, ooo)]");
        groupVariables_List_Variable_helper("ooo", "[ooo]", "[(ooo, 1)]");
        groupVariables_List_Variable_helper("ooo", "[a, ooo]", "[(ooo, 1)]");
        groupVariables_List_Variable_helper("a*b*c", "[]", "[(1, a*b*c)]");
        groupVariables_List_Variable_helper("a*b*c", "[a]", "[(a, b*c)]");
        groupVariables_List_Variable_helper("a*b*c", "[b]", "[(b, a*c)]");
        groupVariables_List_Variable_helper("a*b*c", "[c]", "[(c, a*b)]");
        groupVariables_List_Variable_helper("a*b*c", "[a, b]", "[(a*b, c)]");
        groupVariables_List_Variable_helper("a*b*c", "[a, c]", "[(a*c, b)]");
        groupVariables_List_Variable_helper("a*b*c", "[b, c]", "[(b*c, a)]");
        groupVariables_List_Variable_helper("a*b*c", "[a, b, c]", "[(a*b*c, 1)]");
        groupVariables_List_Variable_helper("x^2-4*x+7", "[]", "[(1, x^2-4*x+7)]");
        groupVariables_List_Variable_helper("x^2-4*x+7", "[x]", "[(1, 7), (x, -4), (x^2, 1)]");
        groupVariables_List_Variable_helper("x^2+2*x*y+y^2", "[]", "[(1, x^2+2*x*y+y^2)]");
        groupVariables_List_Variable_helper("x^2+2*x*y+y^2", "[x]", "[(1, y^2), (x, 2*y), (x^2, 1)]");
        groupVariables_List_Variable_helper("x^2+2*x*y+y^2", "[y]", "[(1, x^2), (y, 2*x), (y^2, 1)]");
        groupVariables_List_Variable_helper("x^2+2*x*y+y^2", "[x, y]", "[(y^2, 1), (x*y, 2), (x^2, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[]", "[(1, x*y^2*z+x^2*z^2+x^3+z^2)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x]",
                "[(1, z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y]", "[(1, x^2*z^2+x^3+z^2), (y^2, x*z)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[z]", "[(1, x^3), (z, x*y^2), (z^2, x^2+1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y]",
                "[(1, z^2), (x^2, z^2), (x*y^2, z), (x^3, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, z]",
                "[(z^2, 1), (x*z, y^2), (x^3, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[y, z]",
                "[(1, x^3), (z^2, x^2+1), (y^2*z, x)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[x, y, z]",
                "[(z^2, 1), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");

        groupVariables_List_Variable_fail_helper("x", "[null]");
        groupVariables_List_Variable_fail_helper("x", "[a, null, c]");
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        MultivariatePolynomial p = readStrict(a).get().add(readStrict(b).get());
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
        add_helper("0", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2");

        add_helper("1", "0", "1");
        add_helper("1", "1", "2");
        add_helper("1", "-17", "-16");
        add_helper("1", "ooo", "ooo+1");
        add_helper("1", "a*b*c", "a*b*c+1");
        add_helper("1", "x^2-4*x+7", "x^2-4*x+8");
        add_helper("1", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2+1");
        add_helper("1", "a+b+c+d+e+f", "a+b+c+d+e+f+1");
        add_helper("1", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2+1");

        add_helper("-17", "0", "-17");
        add_helper("-17", "1", "-16");
        add_helper("-17", "-17", "-34");
        add_helper("-17", "ooo", "ooo-17");
        add_helper("-17", "a*b*c", "a*b*c-17");
        add_helper("-17", "x^2-4*x+7", "x^2-4*x-10");
        add_helper("-17", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2-17");
        add_helper("-17", "a+b+c+d+e+f", "a+b+c+d+e+f-17");
        add_helper("-17", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2-17");

        add_helper("ooo", "0", "ooo");
        add_helper("ooo", "1", "ooo+1");
        add_helper("ooo", "-17", "ooo-17");
        add_helper("ooo", "ooo", "2*ooo");
        add_helper("ooo", "a*b*c", "a*b*c+ooo");
        add_helper("ooo", "x^2-4*x+7", "x^2-4*x+ooo+7");
        add_helper("ooo", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2+ooo");
        add_helper("ooo", "a+b+c+d+e+f", "a+b+c+d+e+f+ooo");
        add_helper("ooo", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2+ooo");

        add_helper("a*b*c", "0", "a*b*c");
        add_helper("a*b*c", "1", "a*b*c+1");
        add_helper("a*b*c", "-17", "a*b*c-17");
        add_helper("a*b*c", "ooo", "a*b*c+ooo");
        add_helper("a*b*c", "a*b*c", "2*a*b*c");
        add_helper("a*b*c", "x^2-4*x+7", "a*b*c+x^2-4*x+7");
        add_helper("a*b*c", "x^2+2*x*y+y^2", "a*b*c+x^2+2*x*y+y^2");
        add_helper("a*b*c", "a+b+c+d+e+f", "a*b*c+a+b+c+d+e+f");
        add_helper("a*b*c", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+a*b*c+x^3+z^2");

        add_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        add_helper("x^2-4*x+7", "1", "x^2-4*x+8");
        add_helper("x^2-4*x+7", "-17", "x^2-4*x-10");
        add_helper("x^2-4*x+7", "ooo", "x^2-4*x+ooo+7");
        add_helper("x^2-4*x+7", "a*b*c", "a*b*c+x^2-4*x+7");
        add_helper("x^2-4*x+7", "x^2-4*x+7", "2*x^2-8*x+14");
        add_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "2*x^2+2*x*y+y^2-4*x+7");
        add_helper("x^2-4*x+7", "a+b+c+d+e+f", "x^2+a+b+c+d+e+f-4*x+7");
        add_helper("x^2-4*x+7", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+x^2+z^2-4*x+7");

        add_helper("x^2+2*x*y+y^2", "0", "x^2+2*x*y+y^2");
        add_helper("x^2+2*x*y+y^2", "1", "x^2+2*x*y+y^2+1");
        add_helper("x^2+2*x*y+y^2", "-17", "x^2+2*x*y+y^2-17");
        add_helper("x^2+2*x*y+y^2", "ooo", "x^2+2*x*y+y^2+ooo");
        add_helper("x^2+2*x*y+y^2", "a*b*c", "a*b*c+x^2+2*x*y+y^2");
        add_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "2*x^2+2*x*y+y^2-4*x+7");
        add_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "2*x^2+4*x*y+2*y^2");
        add_helper("x^2+2*x*y+y^2", "a+b+c+d+e+f", "x^2+2*x*y+y^2+a+b+c+d+e+f");
        add_helper("x^2+2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+x^2+2*x*y+y^2+z^2");

        add_helper("a+b+c+d+e+f", "0", "a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f+1");
        add_helper("a+b+c+d+e+f", "-17", "a+b+c+d+e+f-17");
        add_helper("a+b+c+d+e+f", "ooo", "a+b+c+d+e+f+ooo");
        add_helper("a+b+c+d+e+f", "a*b*c", "a*b*c+a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "x^2-4*x+7", "x^2+a+b+c+d+e+f-4*x+7");
        add_helper("a+b+c+d+e+f", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2+a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "a+b+c+d+e+f", "2*a+2*b+2*c+2*d+2*e+2*f");
        add_helper("a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2+a+b+c+d+e+f");

        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "0", "x*y^2*z+x^2*z^2+x^3+z^2");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "1", "x*y^2*z+x^2*z^2+x^3+z^2+1");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "-17", "x*y^2*z+x^2*z^2+x^3+z^2-17");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "ooo", "x*y^2*z+x^2*z^2+x^3+z^2+ooo");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a*b*c", "x*y^2*z+x^2*z^2+a*b*c+x^3+z^2");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x^2-4*x+7", "x*y^2*z+x^2*z^2+x^3+x^2+z^2-4*x+7");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x^2+2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+x^2+2*x*y+y^2+z^2");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+z^2+a+b+c+d+e+f");
        add_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2", "2*x*y^2*z+2*x^2*z^2+2*x^3+2*z^2");

        add_helper("a*b*c", "-a*b*c", "0");
        add_helper("a*b*c", "-a*b*c+1", "1");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        MultivariatePolynomial p = readStrict(input).get().negate();
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
        negate_helper("x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3-z^2");
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        MultivariatePolynomial p = readStrict(a).get().subtract(readStrict(b).get());
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
        subtract_helper("0", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3-z^2");

        subtract_helper("1", "0", "1");
        subtract_helper("1", "1", "0");
        subtract_helper("1", "-17", "18");
        subtract_helper("1", "ooo", "-ooo+1");
        subtract_helper("1", "a*b*c", "-a*b*c+1");
        subtract_helper("1", "x^2-4*x+7", "-x^2+4*x-6");
        subtract_helper("1", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2+1");
        subtract_helper("1", "a+b+c+d+e+f", "-a-b-c-d-e-f+1");
        subtract_helper("1", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3-z^2+1");

        subtract_helper("-17", "0", "-17");
        subtract_helper("-17", "1", "-18");
        subtract_helper("-17", "-17", "0");
        subtract_helper("-17", "ooo", "-ooo-17");
        subtract_helper("-17", "a*b*c", "-a*b*c-17");
        subtract_helper("-17", "x^2-4*x+7", "-x^2+4*x-24");
        subtract_helper("-17", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2-17");
        subtract_helper("-17", "a+b+c+d+e+f", "-a-b-c-d-e-f-17");
        subtract_helper("-17", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3-z^2-17");

        subtract_helper("ooo", "0", "ooo");
        subtract_helper("ooo", "1", "ooo-1");
        subtract_helper("ooo", "-17", "ooo+17");
        subtract_helper("ooo", "ooo", "0");
        subtract_helper("ooo", "a*b*c", "-a*b*c+ooo");
        subtract_helper("ooo", "x^2-4*x+7", "-x^2+4*x+ooo-7");
        subtract_helper("ooo", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2+ooo");
        subtract_helper("ooo", "a+b+c+d+e+f", "-a-b-c-d-e-f+ooo");
        subtract_helper("ooo", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3-z^2+ooo");

        subtract_helper("a*b*c", "0", "a*b*c");
        subtract_helper("a*b*c", "1", "a*b*c-1");
        subtract_helper("a*b*c", "-17", "a*b*c+17");
        subtract_helper("a*b*c", "ooo", "a*b*c-ooo");
        subtract_helper("a*b*c", "a*b*c", "0");
        subtract_helper("a*b*c", "x^2-4*x+7", "a*b*c-x^2+4*x-7");
        subtract_helper("a*b*c", "x^2+2*x*y+y^2", "a*b*c-x^2-2*x*y-y^2");
        subtract_helper("a*b*c", "a+b+c+d+e+f", "a*b*c-a-b-c-d-e-f");
        subtract_helper("a*b*c", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2+a*b*c-x^3-z^2");

        subtract_helper("x^2-4*x+7", "0", "x^2-4*x+7");
        subtract_helper("x^2-4*x+7", "1", "x^2-4*x+6");
        subtract_helper("x^2-4*x+7", "-17", "x^2-4*x+24");
        subtract_helper("x^2-4*x+7", "ooo", "x^2-4*x-ooo+7");
        subtract_helper("x^2-4*x+7", "a*b*c", "-a*b*c+x^2-4*x+7");
        subtract_helper("x^2-4*x+7", "x^2-4*x+7", "0");
        subtract_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "-2*x*y-y^2-4*x+7");
        subtract_helper("x^2-4*x+7", "a+b+c+d+e+f", "x^2-a-b-c-d-e-f-4*x+7");
        subtract_helper("x^2-4*x+7", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3+x^2-z^2-4*x+7");

        subtract_helper("x^2+2*x*y+y^2", "0", "x^2+2*x*y+y^2");
        subtract_helper("x^2+2*x*y+y^2", "1", "x^2+2*x*y+y^2-1");
        subtract_helper("x^2+2*x*y+y^2", "-17", "x^2+2*x*y+y^2+17");
        subtract_helper("x^2+2*x*y+y^2", "ooo", "x^2+2*x*y+y^2-ooo");
        subtract_helper("x^2+2*x*y+y^2", "a*b*c", "-a*b*c+x^2+2*x*y+y^2");
        subtract_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "2*x*y+y^2+4*x-7");
        subtract_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "0");
        subtract_helper("x^2+2*x*y+y^2", "a+b+c+d+e+f", "x^2+2*x*y+y^2-a-b-c-d-e-f");
        subtract_helper("x^2+2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3+x^2+2*x*y+y^2-z^2");

        subtract_helper("a+b+c+d+e+f", "0", "a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f-1");
        subtract_helper("a+b+c+d+e+f", "-17", "a+b+c+d+e+f+17");
        subtract_helper("a+b+c+d+e+f", "ooo", "a+b+c+d+e+f-ooo");
        subtract_helper("a+b+c+d+e+f", "a*b*c", "-a*b*c+a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "x^2-4*x+7", "-x^2+a+b+c+d+e+f+4*x-7");
        subtract_helper("a+b+c+d+e+f", "x^2+2*x*y+y^2", "-x^2-2*x*y-y^2+a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "a+b+c+d+e+f", "0");
        subtract_helper("a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+z^2", "-x*y^2*z-x^2*z^2-x^3-z^2+a+b+c+d+e+f");

        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "0", "x*y^2*z+x^2*z^2+x^3+z^2");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "1", "x*y^2*z+x^2*z^2+x^3+z^2-1");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "-17", "x*y^2*z+x^2*z^2+x^3+z^2+17");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "ooo", "x*y^2*z+x^2*z^2+x^3+z^2-ooo");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a*b*c", "x*y^2*z+x^2*z^2-a*b*c+x^3+z^2");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x^2-4*x+7", "x*y^2*z+x^2*z^2+x^3-x^2+z^2+4*x-7");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x^2+2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3-x^2-2*x*y-y^2+z^2");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+z^2-a-b-c-d-e-f");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2", "0");

        subtract_helper("a*b*c", "a*b*c-1", "1");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        MultivariatePolynomial p = readStrict(a).get().multiply(b);
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

        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+z^2", 0, "0");
        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+z^2", 1, "x*y^2*z+x^2*z^2+x^3+z^2");
        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+z^2", -3, "-3*x*y^2*z-3*x^2*z^2-3*x^3-3*z^2");
        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+z^2", 4, "4*x*y^2*z+4*x^2*z^2+4*x^3+4*z^2");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        MultivariatePolynomial p = readStrict(a).get().multiply(Readers.readBigIntegerStrict(b).get());
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

        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "0", "0");
        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "1", "x*y^2*z+x^2*z^2+x^3+z^2");
        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "-3", "-3*x*y^2*z-3*x^2*z^2-3*x^3-3*z^2");
        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "4", "4*x*y^2*z+4*x^2*z^2+4*x^3+4*z^2");
    }

    private static void multiply_Monomial_BigInteger_helper(
            @NotNull String p,
            @NotNull String ev,
            @NotNull String c,
            @NotNull String output
    ) {
        MultivariatePolynomial q = readStrict(p).get()
                .multiply(Monomial.readStrict(ev).get(), Readers.readBigIntegerStrict(c).get());
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testMultiply_Monomial_BigInteger() {
        multiply_Monomial_BigInteger_helper("0", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("0", "1", "1", "0");
        multiply_Monomial_BigInteger_helper("0", "ooo", "2", "0");
        multiply_Monomial_BigInteger_helper("0", "a*b*c", "-2", "0");

        multiply_Monomial_BigInteger_helper("1", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("1", "1", "1", "1");
        multiply_Monomial_BigInteger_helper("1", "ooo", "2", "2*ooo");
        multiply_Monomial_BigInteger_helper("1", "a*b*c", "-2", "-2*a*b*c");

        multiply_Monomial_BigInteger_helper("-17", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("-17", "1", "1", "-17");
        multiply_Monomial_BigInteger_helper("-17", "ooo", "2", "-34*ooo");
        multiply_Monomial_BigInteger_helper("-17", "a*b*c", "-2", "34*a*b*c");

        multiply_Monomial_BigInteger_helper("ooo", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("ooo", "1", "1", "ooo");
        multiply_Monomial_BigInteger_helper("ooo", "ooo", "2", "2*ooo^2");
        multiply_Monomial_BigInteger_helper("ooo", "a*b*c", "-2", "-2*a*b*c*ooo");

        multiply_Monomial_BigInteger_helper("a*b*c", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("a*b*c", "1", "1", "a*b*c");
        multiply_Monomial_BigInteger_helper("a*b*c", "ooo", "2", "2*a*b*c*ooo");
        multiply_Monomial_BigInteger_helper("a*b*c", "a*b*c", "-2", "-2*a^2*b^2*c^2");

        multiply_Monomial_BigInteger_helper("x^2-4*x+7", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("x^2-4*x+7", "1", "1", "x^2-4*x+7");
        multiply_Monomial_BigInteger_helper("x^2-4*x+7", "ooo", "2", "2*x^2*ooo-8*x*ooo+14*ooo");
        multiply_Monomial_BigInteger_helper("x^2-4*x+7", "a*b*c", "-2", "-2*a*b*c*x^2+8*a*b*c*x-14*a*b*c");

        multiply_Monomial_BigInteger_helper("x^2+2*x*y+y^2", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("x^2+2*x*y+y^2", "1", "1", "x^2+2*x*y+y^2");
        multiply_Monomial_BigInteger_helper("x^2+2*x*y+y^2", "ooo", "2", "2*x^2*ooo+4*x*y*ooo+2*y^2*ooo");
        multiply_Monomial_BigInteger_helper("x^2+2*x*y+y^2", "a*b*c", "-2",
                "-2*a*b*c*x^2-4*a*b*c*x*y-2*a*b*c*y^2");

        multiply_Monomial_BigInteger_helper("a+b+c+d+e+f", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("a+b+c+d+e+f", "1", "1", "a+b+c+d+e+f");
        multiply_Monomial_BigInteger_helper("a+b+c+d+e+f", "ooo", "2",
                "2*a*ooo+2*b*ooo+2*c*ooo+2*d*ooo+2*e*ooo+2*f*ooo");
        multiply_Monomial_BigInteger_helper("a+b+c+d+e+f", "a*b*c", "-2",
                "-2*a^2*b*c-2*a*b^2*c-2*a*b*c^2-2*a*b*c*d-2*a*b*c*e-2*a*b*c*f");

        multiply_Monomial_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "1", "0", "0");
        multiply_Monomial_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "1", "1", "x*y^2*z+x^2*z^2+x^3+z^2");
        multiply_Monomial_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "ooo", "2",
                "2*x*y^2*z*ooo+2*x^2*z^2*ooo+2*x^3*ooo+2*z^2*ooo");
        multiply_Monomial_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a*b*c", "-2",
                "-2*a*b*c*x*y^2*z-2*a*b*c*x^2*z^2-2*a*b*c*x^3-2*a*b*c*z^2");
    }

    private static void multiply_MultivariatePolynomial_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        MultivariatePolynomial p = readStrict(a).get().multiply(readStrict(b).get());
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
        multiply_MultivariatePolynomial_helper("0", "x*y^2*z+x^2*z^2+x^3+z^2", "0");

        multiply_MultivariatePolynomial_helper("1", "0", "0");
        multiply_MultivariatePolynomial_helper("1", "1", "1");
        multiply_MultivariatePolynomial_helper("1", "-17", "-17");
        multiply_MultivariatePolynomial_helper("1", "ooo", "ooo");
        multiply_MultivariatePolynomial_helper("1", "a*b*c", "a*b*c");
        multiply_MultivariatePolynomial_helper("1", "x^2-4*x+7", "x^2-4*x+7");
        multiply_MultivariatePolynomial_helper("1", "x^2+2*x*y+y^2", "x^2+2*x*y+y^2");
        multiply_MultivariatePolynomial_helper("1", "a+b+c+d+e+f", "a+b+c+d+e+f");
        multiply_MultivariatePolynomial_helper("1", "x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2");

        multiply_MultivariatePolynomial_helper("-17", "0", "0");
        multiply_MultivariatePolynomial_helper("-17", "1", "-17");
        multiply_MultivariatePolynomial_helper("-17", "-17", "289");
        multiply_MultivariatePolynomial_helper("-17", "ooo", "-17*ooo");
        multiply_MultivariatePolynomial_helper("-17", "a*b*c", "-17*a*b*c");
        multiply_MultivariatePolynomial_helper("-17", "x^2-4*x+7", "-17*x^2+68*x-119");
        multiply_MultivariatePolynomial_helper("-17", "x^2+2*x*y+y^2", "-17*x^2-34*x*y-17*y^2");
        multiply_MultivariatePolynomial_helper("-17", "a+b+c+d+e+f", "-17*a-17*b-17*c-17*d-17*e-17*f");
        multiply_MultivariatePolynomial_helper("-17", "x*y^2*z+x^2*z^2+x^3+z^2",
                "-17*x*y^2*z-17*x^2*z^2-17*x^3-17*z^2");

        multiply_MultivariatePolynomial_helper("ooo", "0", "0");
        multiply_MultivariatePolynomial_helper("ooo", "1", "ooo");
        multiply_MultivariatePolynomial_helper("ooo", "-17", "-17*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "ooo", "ooo^2");
        multiply_MultivariatePolynomial_helper("ooo", "a*b*c", "a*b*c*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "x^2-4*x+7", "x^2*ooo-4*x*ooo+7*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "x^2+2*x*y+y^2", "x^2*ooo+2*x*y*ooo+y^2*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "a+b+c+d+e+f", "a*ooo+b*ooo+c*ooo+d*ooo+e*ooo+f*ooo");
        multiply_MultivariatePolynomial_helper("ooo", "x*y^2*z+x^2*z^2+x^3+z^2",
                "x*y^2*z*ooo+x^2*z^2*ooo+x^3*ooo+z^2*ooo");

        multiply_MultivariatePolynomial_helper("a*b*c", "0", "0");
        multiply_MultivariatePolynomial_helper("a*b*c", "1", "a*b*c");
        multiply_MultivariatePolynomial_helper("a*b*c", "-17", "-17*a*b*c");
        multiply_MultivariatePolynomial_helper("a*b*c", "ooo", "a*b*c*ooo");
        multiply_MultivariatePolynomial_helper("a*b*c", "a*b*c", "a^2*b^2*c^2");
        multiply_MultivariatePolynomial_helper("a*b*c", "x^2-4*x+7", "a*b*c*x^2-4*a*b*c*x+7*a*b*c");
        multiply_MultivariatePolynomial_helper("a*b*c", "x^2+2*x*y+y^2", "a*b*c*x^2+2*a*b*c*x*y+a*b*c*y^2");
        multiply_MultivariatePolynomial_helper("a*b*c", "a+b+c+d+e+f",
                "a^2*b*c+a*b^2*c+a*b*c^2+a*b*c*d+a*b*c*e+a*b*c*f");
        multiply_MultivariatePolynomial_helper("a*b*c", "x*y^2*z+x^2*z^2+x^3+z^2",
                "a*b*c*x*y^2*z+a*b*c*x^2*z^2+a*b*c*x^3+a*b*c*z^2");

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
        multiply_MultivariatePolynomial_helper("x^2-4*x+7", "x*y^2*z+x^2*z^2+x^3+z^2",
                "x^3*y^2*z+x^4*z^2+x^5-4*x^2*y^2*z-4*x^3*z^2-4*x^4+7*x*y^2*z+8*x^2*z^2+7*x^3-4*x*z^2+7*z^2");

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
        multiply_MultivariatePolynomial_helper("x^2+2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+z^2",
                "x^3*y^2*z+2*x^2*y^3*z+x*y^4*z+x^4*z^2+2*x^3*y*z^2+x^2*y^2*z^2+x^5+2*x^4*y+x^3*y^2+x^2*z^2+" +
                "2*x*y*z^2+y^2*z^2");

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
        multiply_MultivariatePolynomial_helper("a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+z^2",
                "a*x*y^2*z+b*x*y^2*z+c*x*y^2*z+d*x*y^2*z+e*x*y^2*z+f*x*y^2*z+a*x^2*z^2+b*x^2*z^2+c*x^2*z^2+" +
                "d*x^2*z^2+e*x^2*z^2+f*x^2*z^2+a*x^3+b*x^3+c*x^3+d*x^3+e*x^3+f*x^3+a*z^2+b*z^2+c*z^2+d*z^2+e*z^2+" +
                "f*z^2");

        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "0", "0");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "1", "x*y^2*z+x^2*z^2+x^3+z^2");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "-17",
                "-17*x*y^2*z-17*x^2*z^2-17*x^3-17*z^2");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "ooo",
                "x*y^2*z*ooo+x^2*z^2*ooo+x^3*ooo+z^2*ooo");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a*b*c",
                "a*b*c*x*y^2*z+a*b*c*x^2*z^2+a*b*c*x^3+a*b*c*z^2");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x^2-4*x+7",
                "x^3*y^2*z+x^4*z^2+x^5-4*x^2*y^2*z-4*x^3*z^2-4*x^4+7*x*y^2*z+8*x^2*z^2+7*x^3-4*x*z^2+7*z^2");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x^2+2*x*y+y^2",
                "x^3*y^2*z+2*x^2*y^3*z+x*y^4*z+x^4*z^2+2*x^3*y*z^2+x^2*y^2*z^2+x^5+2*x^4*y+x^3*y^2+x^2*z^2+" +
                "2*x*y*z^2+y^2*z^2");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "a+b+c+d+e+f",
                "a*x*y^2*z+b*x*y^2*z+c*x*y^2*z+d*x*y^2*z+e*x*y^2*z+f*x*y^2*z+a*x^2*z^2+b*x^2*z^2+c*x^2*z^2+" +
                "d*x^2*z^2+e*x^2*z^2+f*x^2*z^2+a*x^3+b*x^3+c*x^3+d*x^3+e*x^3+f*x^3+a*z^2+b*z^2+c*z^2+d*z^2+e*z^2+" +
                "f*z^2");
        multiply_MultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "x*y^2*z+x^2*z^2+x^3+z^2",
                "x^2*y^4*z^2+2*x^3*y^2*z^3+x^4*z^4+2*x^4*y^2*z+2*x^5*z^2+x^6+2*x*y^2*z^3+2*x^2*z^4+2*x^3*z^2+z^4");
    }

    private static void divideExact_BigInteger_helper(@NotNull String p, @NotNull String i, @NotNull String output) {
        MultivariatePolynomial q = readStrict(p).get().divideExact(Readers.readBigIntegerStrict(i).get());
        q.validate();
        aeq(q, output);
    }

    private static void divideExact_BigInteger_fail_helper(@NotNull String p, @NotNull String i) {
        try {
            readStrict(p).get().divideExact(Readers.readBigIntegerStrict(i).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideExact_BigInteger() {
        divideExact_BigInteger_helper("0", "1", "0");
        divideExact_BigInteger_helper("0", "-3", "0");
        divideExact_BigInteger_helper("0", "4", "0");
        divideExact_BigInteger_helper("1", "1", "1");
        divideExact_BigInteger_helper("x", "1", "x");
        divideExact_BigInteger_helper("-17", "1", "-17");
        divideExact_BigInteger_helper("-17", "-17", "1");
        divideExact_BigInteger_helper("x^2-4*x+7", "1", "x^2-4*x+7");
        divideExact_BigInteger_helper("8*x^2+16*x*y+8*y^2", "1", "8*x^2+16*x*y+8*y^2");
        divideExact_BigInteger_helper("8*x^2+16*x*y+8*y^2", "4", "2*x^2+4*x*y+2*y^2");
        divideExact_BigInteger_helper("-1", "-1", "1");

        divideExact_BigInteger_fail_helper("0", "0");
        divideExact_BigInteger_fail_helper("1", "0");
        divideExact_BigInteger_fail_helper("x", "0");
        divideExact_BigInteger_fail_helper("a*b*c", "0");
    }

    private static void divideExact_int_helper(@NotNull String p, int i, @NotNull String output) {
        MultivariatePolynomial q = readStrict(p).get().divideExact(i);
        q.validate();
        aeq(q, output);
    }

    private static void divideExact_int_fail_helper(@NotNull String p, int i) {
        try {
            readStrict(p).get().divideExact(i);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivideExact_int() {
        divideExact_int_helper("0", 1, "0");
        divideExact_int_helper("0", -3, "0");
        divideExact_int_helper("0", 4, "0");
        divideExact_int_helper("1", 1, "1");
        divideExact_int_helper("x", 1, "x");
        divideExact_int_helper("-17", 1, "-17");
        divideExact_int_helper("-17", -17, "1");
        divideExact_int_helper("x^2-4*x+7", 1, "x^2-4*x+7");
        divideExact_int_helper("8*x^2+16*x*y+8*y^2", 1, "8*x^2+16*x*y+8*y^2");
        divideExact_int_helper("8*x^2+16*x*y+8*y^2", 4, "2*x^2+4*x*y+2*y^2");
        divideExact_int_helper("-1", -1, "1");

        divideExact_int_fail_helper("0", 0);
        divideExact_int_fail_helper("1", 0);
        divideExact_int_fail_helper("x", 0);
        divideExact_int_fail_helper("a*b*c", 0);
    }

    private static void shiftLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        MultivariatePolynomial q = readStrict(p).get().shiftLeft(bits);
        q.validate();
        aeq(q, output);
    }

    private static void shiftLeft_fail_helper(@NotNull String p, int bits) {
        try {
            readStrict(p).get().shiftLeft(bits);
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

        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+z^2", 0, "x*y^2*z+x^2*z^2+x^3+z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+z^2", 1, "2*x*y^2*z+2*x^2*z^2+2*x^3+2*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+z^2", 2, "4*x*y^2*z+4*x^2*z^2+4*x^3+4*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+z^2", 3, "8*x*y^2*z+8*x^2*z^2+8*x^3+8*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+z^2", 4, "16*x*y^2*z+16*x^2*z^2+16*x^3+16*z^2");

        shiftLeft_fail_helper("0", -1);
        shiftLeft_fail_helper("1", -1);
        shiftLeft_fail_helper("a*b*c", -1);
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        MultivariatePolynomial p = sum(readMultivariatePolynomialList(input));
        p.validate();
        aeq(p, output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readMultivariatePolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[]", "0");
        sum_helper("[1]", "1");
        sum_helper("[-17]", "-17");
        sum_helper("[-17, ooo, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]", "a*b*c+2*x^2+2*x*y+y^2-4*x+ooo-10");

        sum_fail_helper("[-17, null, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]");
    }

    private static void product_helper(@NotNull String input, @NotNull String output) {
        MultivariatePolynomial p = product(readMultivariatePolynomialList(input));
        p.validate();
        aeq(p, output);
    }

    private static void product_fail_helper(@NotNull String input) {
        try {
            product(readMultivariatePolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        product_helper("[]", "1");
        product_helper("[1]", "1");
        product_helper("[-17]", "-17");
        product_helper("[-17, ooo, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]",
                "-17*a*b*c*x^4*ooo-34*a*b*c*x^3*y*ooo-17*a*b*c*x^2*y^2*ooo+68*a*b*c*x^3*ooo+136*a*b*c*x^2*y*ooo+" +
                "68*a*b*c*x*y^2*ooo-119*a*b*c*x^2*ooo-238*a*b*c*x*y*ooo-119*a*b*c*y^2*ooo");

        product_fail_helper("[-17, null, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]");
    }

    private static void delta_helper(@NotNull Iterable<MultivariatePolynomial> input, @NotNull String output) {
        Iterable<MultivariatePolynomial> ps = delta(input);
        take(TINY_LIMIT, ps).forEach(MultivariatePolynomial::validate);
        aeqitLimit(TINY_LIMIT, ps, output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readMultivariatePolynomialList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readMultivariatePolynomialListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[-17]", "[]");
        delta_helper("[-17, ooo]", "[ooo+17]");
        delta_helper("[-17, ooo, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]",
                "[ooo+17, a*b*c-ooo, -a*b*c+x^2-4*x+7, 2*x*y+y^2+4*x-7]");
        MultivariatePolynomial seed = readStrict("x+y").get();
        delta_helper(map(seed::pow, ExhaustiveProvider.INSTANCE.naturalIntegers()),
                "[x+y-1, x^2+2*x*y+y^2-x-y, x^3+3*x^2*y+3*x*y^2+y^3-x^2-2*x*y-y^2," +
                " x^4+4*x^3*y+6*x^2*y^2+4*x*y^3+y^4-x^3-3*x^2*y-3*x*y^2-y^3," +
                " x^5+5*x^4*y+10*x^3*y^2+10*x^2*y^3+5*x*y^4+y^5-x^4-4*x^3*y-6*x^2*y^2-4*x*y^3-y^4," +
                " x^6+6*x^5*y+15*x^4*y^2+20*x^3*y^3+15*x^2*y^4+6*x*y^5+y^6-x^5-5*x^4*y-10*x^3*y^2-10*x^2*y^3-" +
                "5*x*y^4-y^5," +
                " x^7+7*x^6*y+21*x^5*y^2+35*x^4*y^3+35*x^3*y^4+21*x^2*y^5+7*x*y^6+y^7-x^6-6*x^5*y-15*x^4*y^2-" +
                "20*x^3*y^3-15*x^2*y^4-6*x*y^5-y^6," +
                " x^8+8*x^7*y+28*x^6*y^2+56*x^5*y^3+70*x^4*y^4+56*x^3*y^5+28*x^2*y^6+8*x*y^7+y^8-x^7-7*x^6*y-" +
                "21*x^5*y^2-35*x^4*y^3-35*x^3*y^4-21*x^2*y^5-7*x*y^6-y^7," +
                " x^9+9*x^8*y+36*x^7*y^2+84*x^6*y^3+126*x^5*y^4+126*x^4*y^5+84*x^3*y^6+36*x^2*y^7+9*x*y^8+y^9-x^8-" +
                "8*x^7*y-28*x^6*y^2-56*x^5*y^3-70*x^4*y^4-56*x^3*y^5-28*x^2*y^6-8*x*y^7-y^8," +
                " x^10+10*x^9*y+45*x^8*y^2+120*x^7*y^3+210*x^6*y^4+252*x^5*y^5+210*x^4*y^6+120*x^3*y^7+45*x^2*y^8+" +
                "10*x*y^9+y^10-x^9-9*x^8*y-36*x^7*y^2-84*x^6*y^3-126*x^5*y^4-126*x^4*y^5-84*x^3*y^6-36*x^2*y^7-" +
                "9*x*y^8-y^9," +
                " x^11+11*x^10*y+55*x^9*y^2+165*x^8*y^3+330*x^7*y^4+462*x^6*y^5+462*x^5*y^6+330*x^4*y^7+165*x^3*y^8+" +
                "55*x^2*y^9+11*x*y^10+y^11-x^10-10*x^9*y-45*x^8*y^2-120*x^7*y^3-210*x^6*y^4-252*x^5*y^5-210*x^4*y^6-" +
                "120*x^3*y^7-45*x^2*y^8-10*x*y^9-y^10," +
                " x^12+12*x^11*y+66*x^10*y^2+220*x^9*y^3+495*x^8*y^4+792*x^7*y^5+924*x^6*y^6+792*x^5*y^7+" +
                "495*x^4*y^8+220*x^3*y^9+66*x^2*y^10+12*x*y^11+y^12-x^11-11*x^10*y-55*x^9*y^2-165*x^8*y^3-" +
                "330*x^7*y^4-462*x^6*y^5-462*x^5*y^6-330*x^4*y^7-165*x^3*y^8-55*x^2*y^9-11*x*y^10-y^11," +
                " x^13+13*x^12*y+78*x^11*y^2+286*x^10*y^3+715*x^9*y^4+1287*x^8*y^5+1716*x^7*y^6+1716*x^6*y^7+" +
                "1287*x^5*y^8+715*x^4*y^9+286*x^3*y^10+78*x^2*y^11+13*x*y^12+y^13-x^12-12*x^11*y-66*x^10*y^2-" +
                "220*x^9*y^3-495*x^8*y^4-792*x^7*y^5-924*x^6*y^6-792*x^5*y^7-495*x^4*y^8-220*x^3*y^9-66*x^2*y^10-" +
                "12*x*y^11-y^12," +
                " x^14+14*x^13*y+91*x^12*y^2+364*x^11*y^3+1001*x^10*y^4+2002*x^9*y^5+3003*x^8*y^6+3432*x^7*y^7+" +
                "3003*x^6*y^8+2002*x^5*y^9+1001*x^4*y^10+364*x^3*y^11+91*x^2*y^12+14*x*y^13+y^14-x^13-13*x^12*y-" +
                "78*x^11*y^2-286*x^10*y^3-715*x^9*y^4-1287*x^8*y^5-1716*x^7*y^6-1716*x^6*y^7-1287*x^5*y^8-" +
                "715*x^4*y^9-286*x^3*y^10-78*x^2*y^11-13*x*y^12-y^13," +
                " x^15+15*x^14*y+105*x^13*y^2+455*x^12*y^3+1365*x^11*y^4+3003*x^10*y^5+5005*x^9*y^6+6435*x^8*y^7+" +
                "6435*x^7*y^8+5005*x^6*y^9+3003*x^5*y^10+1365*x^4*y^11+455*x^3*y^12+105*x^2*y^13+15*x*y^14+y^15-" +
                "x^14-14*x^13*y-91*x^12*y^2-364*x^11*y^3-1001*x^10*y^4-2002*x^9*y^5-3003*x^8*y^6-3432*x^7*y^7-" +
                "3003*x^6*y^8-2002*x^5*y^9-1001*x^4*y^10-364*x^3*y^11-91*x^2*y^12-14*x*y^13-y^14," +
                " x^16+16*x^15*y+120*x^14*y^2+560*x^13*y^3+1820*x^12*y^4+4368*x^11*y^5+8008*x^10*y^6+11440*x^9*y^7+" +
                "12870*x^8*y^8+11440*x^7*y^9+8008*x^6*y^10+4368*x^5*y^11+1820*x^4*y^12+560*x^3*y^13+120*x^2*y^14+" +
                "16*x*y^15+y^16-x^15-15*x^14*y-105*x^13*y^2-455*x^12*y^3-1365*x^11*y^4-3003*x^10*y^5-5005*x^9*y^6-" +
                "6435*x^8*y^7-6435*x^7*y^8-5005*x^6*y^9-3003*x^5*y^10-1365*x^4*y^11-455*x^3*y^12-105*x^2*y^13-" +
                "15*x*y^14-y^15, x^17+17*x^16*y+136*x^15*y^2+680*x^14*y^3+2380*x^13*y^4+6188*x^12*y^5+" +
                "12376*x^11*y^6+19448*x^10*y^7+24310*x^9*y^8+24310*x^8*y^9+19448*x^7*y^10+12376*x^6*y^11+" +
                "6188*x^5*y^12+2380*x^4*y^13+680*x^3*y^14+136*x^2*y^15+17*x*y^16+y^17-x^16-16*x^15*y-120*x^14*y^2-" +
                "560*x^13*y^3-1820*x^12*y^4-4368*x^11*y^5-8008*x^10*y^6-11440*x^9*y^7-12870*x^8*y^8-11440*x^7*y^9-" +
                "8008*x^6*y^10-4368*x^5*y^11-1820*x^4*y^12-560*x^3*y^13-120*x^2*y^14-16*x*y^15-y^16," +
                " x^18+18*x^17*y+153*x^16*y^2+816*x^15*y^3+3060*x^14*y^4+8568*x^13*y^5+18564*x^12*y^6+" +
                "31824*x^11*y^7+43758*x^10*y^8+48620*x^9*y^9+43758*x^8*y^10+31824*x^7*y^11+18564*x^6*y^12+" +
                "8568*x^5*y^13+3060*x^4*y^14+816*x^3*y^15+153*x^2*y^16+18*x*y^17+y^18-x^17-17*x^16*y-136*x^15*y^2-" +
                "680*x^14*y^3-2380*x^13*y^4-6188*x^12*y^5-12376*x^11*y^6-19448*x^10*y^7-24310*x^9*y^8-24310*x^8*y^9-" +
                "19448*x^7*y^10-12376*x^6*y^11-6188*x^5*y^12-2380*x^4*y^13-680*x^3*y^14-136*x^2*y^15-17*x*y^16-y^17," +
                " x^19+19*x^18*y+171*x^17*y^2+969*x^16*y^3+3876*x^15*y^4+11628*x^14*y^5+27132*x^13*y^6+" +
                "50388*x^12*y^7+75582*x^11*y^8+92378*x^10*y^9+92378*x^9*y^10+75582*x^8*y^11+50388*x^7*y^12+" +
                "27132*x^6*y^13+11628*x^5*y^14+3876*x^4*y^15+969*x^3*y^16+171*x^2*y^17+19*x*y^18+y^19-x^18-" +
                "18*x^17*y-153*x^16*y^2-816*x^15*y^3-3060*x^14*y^4-8568*x^13*y^5-18564*x^12*y^6-31824*x^11*y^7-" +
                "43758*x^10*y^8-48620*x^9*y^9-43758*x^8*y^10-31824*x^7*y^11-18564*x^6*y^12-8568*x^5*y^13-" +
                "3060*x^4*y^14-816*x^3*y^15-153*x^2*y^16-18*x*y^17-y^18, x^20+20*x^19*y+190*x^18*y^2+1140*x^17*y^3+" +
                "4845*x^16*y^4+15504*x^15*y^5+38760*x^14*y^6+77520*x^13*y^7+125970*x^12*y^8+167960*x^11*y^9+" +
                "184756*x^10*y^10+167960*x^9*y^11+125970*x^8*y^12+77520*x^7*y^13+38760*x^6*y^14+15504*x^5*y^15+" +
                "4845*x^4*y^16+1140*x^3*y^17+190*x^2*y^18+20*x*y^19+y^20-x^19-19*x^18*y-171*x^17*y^2-969*x^16*y^3-" +
                "3876*x^15*y^4-11628*x^14*y^5-27132*x^13*y^6-50388*x^12*y^7-75582*x^11*y^8-92378*x^10*y^9-" +
                "92378*x^9*y^10-75582*x^8*y^11-50388*x^7*y^12-27132*x^6*y^13-11628*x^5*y^14-3876*x^4*y^15-" +
                "969*x^3*y^16-171*x^2*y^17-19*x*y^18-y^19, ...]");

        delta_fail_helper("[]");
        delta_fail_helper("[-17, null, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]");
    }

    private static void pow_helper(@NotNull String p, int exponent, @NotNull String output) {
        MultivariatePolynomial q = readStrict(p).get().pow(exponent);
        q.validate();
        aeq(q, output);
    }

    private static void pow_fail_helper(@NotNull String p, int exponent) {
        try {
            readStrict(p).get().pow(exponent);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPow() {
        pow_helper("0", 0, "1");
        pow_helper("0", 1, "0");
        pow_helper("0", 2, "0");
        pow_helper("0", 3, "0");

        pow_helper("1", 0, "1");
        pow_helper("1", 1, "1");
        pow_helper("1", 2, "1");
        pow_helper("1", 3, "1");

        pow_helper("-17", 0, "1");
        pow_helper("-17", 1, "-17");
        pow_helper("-17", 2, "289");
        pow_helper("-17", 3, "-4913");

        pow_helper("ooo", 0, "1");
        pow_helper("ooo", 1, "ooo");
        pow_helper("ooo", 2, "ooo^2");
        pow_helper("ooo", 3, "ooo^3");

        pow_helper("a*b*c", 0, "1");
        pow_helper("a*b*c", 1, "a*b*c");
        pow_helper("a*b*c", 2, "a^2*b^2*c^2");
        pow_helper("a*b*c", 3, "a^3*b^3*c^3");

        pow_helper("x^2-4*x+7", 0, "1");
        pow_helper("x^2-4*x+7", 1, "x^2-4*x+7");
        pow_helper("x^2-4*x+7", 2, "x^4-8*x^3+30*x^2-56*x+49");
        pow_helper("x^2-4*x+7", 3, "x^6-12*x^5+69*x^4-232*x^3+483*x^2-588*x+343");

        pow_helper("x^2+2*x*y+y^2", 0, "1");
        pow_helper("x^2+2*x*y+y^2", 1, "x^2+2*x*y+y^2");
        pow_helper("x^2+2*x*y+y^2", 2, "x^4+4*x^3*y+6*x^2*y^2+4*x*y^3+y^4");
        pow_helper("x^2+2*x*y+y^2", 3, "x^6+6*x^5*y+15*x^4*y^2+20*x^3*y^3+15*x^2*y^4+6*x*y^5+y^6");

        pow_helper("x*y^2*z+x^2*z^2+x^3+z^2", 0, "1");
        pow_helper("x*y^2*z+x^2*z^2+x^3+z^2", 1, "x*y^2*z+x^2*z^2+x^3+z^2");
        pow_helper("x*y^2*z+x^2*z^2+x^3+z^2", 2,
                "x^2*y^4*z^2+2*x^3*y^2*z^3+x^4*z^4+2*x^4*y^2*z+2*x^5*z^2+x^6+2*x*y^2*z^3+2*x^2*z^4+2*x^3*z^2+z^4");
        pow_helper("x*y^2*z+x^2*z^2+x^3+z^2", 3,
                "x^3*y^6*z^3+3*x^4*y^4*z^4+3*x^5*y^2*z^5+x^6*z^6+3*x^5*y^4*z^2+6*x^6*y^2*z^3+3*x^7*z^4+3*x^7*y^2*z+" +
                "3*x^8*z^2+3*x^2*y^4*z^4+6*x^3*y^2*z^5+3*x^4*z^6+x^9+6*x^4*y^2*z^3+6*x^5*z^4+3*x^6*z^2+3*x*y^2*z^5+" +
                "3*x^2*z^6+3*x^3*z^4+z^6");

        pow_fail_helper("a*b*c", -1);
        pow_fail_helper("0", -1);
        pow_fail_helper("1", -1);
    }

    private static void binomialPower_helper(@NotNull String a, @NotNull String b, int p, @NotNull String output) {
        MultivariatePolynomial q = binomialPower(Variable.readStrict(a).get(), Variable.readStrict(b).get(), p);
        q.validate();
        aeq(q, output);
    }

    private static void binomialPower_fail_helper(@NotNull String a, @NotNull String b, int p) {
        try {
            binomialPower(Variable.readStrict(a).get(), Variable.readStrict(b).get(), p);
            fail();
        } catch (ArithmeticException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testBinomialPower() {
        binomialPower_helper("x", "y", 0, "1");
        binomialPower_helper("x", "y", 1, "x+y");
        binomialPower_helper("x", "y", 2, "x^2+2*x*y+y^2");
        binomialPower_helper("x", "y", 3, "x^3+3*x^2*y+3*x*y^2+y^3");
        binomialPower_helper("x", "y", 4, "x^4+4*x^3*y+6*x^2*y^2+4*x*y^3+y^4");
        binomialPower_helper("x", "y", 10,
                "x^10+10*x^9*y+45*x^8*y^2+120*x^7*y^3+210*x^6*y^4+252*x^5*y^5+210*x^4*y^6+120*x^3*y^7+45*x^2*y^8+" +
                "10*x*y^9+y^10");
        binomialPower_helper("a", "b", 4, "a^4+4*a^3*b+6*a^2*b^2+4*a*b^3+b^4");
        binomialPower_helper("b", "a", 4, "a^4+4*a^3*b+6*a^2*b^2+4*a*b^3+b^4");
        binomialPower_helper("gg", "ooo", 4, "gg^4+4*gg^3*ooo+6*gg^2*ooo^2+4*gg*ooo^3+ooo^4");

        binomialPower_fail_helper("a", "a", 2);
        binomialPower_fail_helper("a", "b", -1);
        binomialPower_fail_helper("a", "b", -2);
    }

    private static void apply_BigInteger_helper(@NotNull String p, @NotNull String xs, @NotNull String output) {
        aeq(readStrict(p).get().applyBigInteger(readVariableBigIntegerMap(xs)), output);
    }

    private static void apply_BigInteger_fail_helper(@NotNull String p, @NotNull String xs) {
        try {
            readStrict(p).get().applyBigInteger(readVariableBigIntegerMapWithNulls(xs));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testApplyBigInteger() {
        apply_BigInteger_helper("1", "[]", "1");
        apply_BigInteger_helper("1", "[(a, 0)]", "1");
        apply_BigInteger_helper("1", "[(x, -2), (y, 4)]", "1");
        apply_BigInteger_helper("a", "[(a, 0)]", "0");
        apply_BigInteger_helper("a", "[(a, -5)]", "-5");
        apply_BigInteger_helper("a", "[(a, -5), (b, 2)]", "-5");
        apply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5), (y, 2), (z, 3)]", "49");
        apply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5), (y, 0), (z, 3), (ooo, 3)]", "109");
        apply_BigInteger_helper("a+b+c", "[(a, 3), (b, 4), (c, 5)]", "12");

        apply_BigInteger_fail_helper("a", "[]");
        apply_BigInteger_fail_helper("a", "[(b, 2)]");
        apply_BigInteger_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5), (y, 2)]");
        apply_BigInteger_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5), (y, 2), (z, null)]");
        apply_BigInteger_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5), (y, 2), (null, 1)]");
    }

    private static void apply_Rational_helper(@NotNull String p, @NotNull String xs, @NotNull String output) {
        aeq(readStrict(p).get().applyRational(readVariableRationalMap(xs)), output);
    }

    private static void apply_Rational_fail_helper(@NotNull String p, @NotNull String xs) {
        try {
            readStrict(p).get().applyRational(readVariableRationalMapWithNulls(xs));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testApplyRational() {
        apply_Rational_helper("1", "[]", "1");
        apply_Rational_helper("1", "[(a, 0)]", "1");
        apply_Rational_helper("1", "[(x, -1/2), (y, 4/5)]", "1");
        apply_Rational_helper("a", "[(a, 0)]", "0");
        apply_Rational_helper("a", "[(a, -5/3)]", "-5/3");
        apply_Rational_helper("a", "[(a, -5/3), (b, 2/7)]", "-5/3");
        apply_Rational_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5/3), (y, 1/2), (z, 3)]", "3037/108");
        apply_Rational_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5/3), (y, 0), (z, 3), (ooo, 3)]", "793/27");
        apply_Rational_helper("a+b+c", "[(a, 1), (b, 1/2), (c, 1/3)]", "11/6");

        apply_Rational_fail_helper("a", "[]");
        apply_Rational_fail_helper("a", "[(b, 1/2)]");
        apply_Rational_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5/3), (y, 1/2)]");
        apply_Rational_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5/3), (y, 1/2), (z, null)]");
        apply_Rational_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, -5/3), (y, 1/2), (null, 1)]");
    }

    private static void substituteMonomial_helper(@NotNull String p, @NotNull String xs, @NotNull String output) {
        MultivariatePolynomial q = readStrict(p).get().substituteMonomial(readVariableMonomialMap(xs));
        q.validate();
        aeq(q, output);
    }

    private static void substituteMonomial_fail_helper(@NotNull String p, @NotNull String xs) {
        try {
            readStrict(p).get().substituteMonomial(readVariableMonomialMapWithNulls(xs));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testSubstituteMonomial() {
        substituteMonomial_helper("1", "[]", "1");
        substituteMonomial_helper("1", "[(a, b)]", "1");
        substituteMonomial_helper("a", "[(a, 1)]", "1");
        substituteMonomial_helper("a", "[(a, a)]", "a");
        substituteMonomial_helper("a", "[(a, b)]", "b");
        substituteMonomial_helper("a", "[(a, x*z)]", "x*z");
        substituteMonomial_helper("a", "[(a, x*z), (b, a)]", "x*z");
        substituteMonomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[]", "x*y^2*z+x^2*z^2+x^3+z^2");
        substituteMonomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, 1)]", "y^2*z+2*z^2+1");
        substituteMonomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, y), (y, z), (z, x)]", "x^2*y^2+x*y*z^2+y^3+x^2");
        substituteMonomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, xx), (y, yy), (z, zz)]",
                "xx*yy^2*zz+xx^2*zz^2+xx^3+zz^2");
        substituteMonomial_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, a*b), (y, a^2), (z, a^3*b^5)]",
                "a^8*b^12+a^6*b^10+a^8*b^6+a^3*b^3");

        substituteMonomial_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, y), (y, z), (z, null)]");
        substituteMonomial_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, y), (y, z), (null, x)]");
    }

    private static void substitute_helper(@NotNull String p, @NotNull String xs, @NotNull String output) {
        MultivariatePolynomial q = readStrict(p).get().substitute(readVariableMultivariatePolynomialMap(xs));
        q.validate();
        aeq(q, output);
    }

    private static void substitute_fail_helper(@NotNull String p, @NotNull String xs) {
        try {
            readStrict(p).get().substitute(readVariableMultivariatePolynomialMapWithNulls(xs));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testSubstitute() {
        substitute_helper("1", "[]", "1");
        substitute_helper("1", "[(a, a+b)]", "1");
        substitute_helper("a", "[(a, 1)]", "1");
        substitute_helper("a", "[(a, a+b)]", "a+b");
        substitute_helper("a", "[(a, b)]", "b");
        substitute_helper("a", "[(a, x*z)]", "x*z");
        substitute_helper("a", "[(a, x*z), (b, a)]", "x*z");
        substitute_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[]", "x*y^2*z+x^2*z^2+x^3+z^2");
        substitute_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, 1)]", "y^2*z+2*z^2+1");
        substitute_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, b^2+a), (y, c), (z, 2*d+1)]",
                "b^6+4*b^4*d^2+3*a*b^4+4*b^4*d+2*b^2*c^2*d+8*a*b^2*d^2+3*a^2*b^2+b^4+b^2*c^2+8*a*b^2*d+2*a*c^2*d+" +
                "4*a^2*d^2+a^3+2*a*b^2+a*c^2+4*a^2*d+a^2+4*d^2+4*d+1");
        substitute_helper("2*a", "[(a, b)]", "2*b");

        substitute_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, b^2+a), (y, c), (z, null)]");
        substitute_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, b^2+a), (y, c), (null, 2*d+1)]");
    }

    private static void sylvesterMatrix_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String v,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().sylvesterMatrix(readStrict(b).get(), Variable.readStrict(v).get()), output);
    }

    private static void sylvesterMatrix_fail_helper(@NotNull String a, @NotNull String b, @NotNull String v) {
        try {
            readStrict(a).get().sylvesterMatrix(readStrict(b).get(), Variable.readStrict(v).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSylvesterMatrix() {
        sylvesterMatrix_helper("1", "1", "x", "[]#0");
        sylvesterMatrix_helper("1", "-17", "x", "[]#0");
        sylvesterMatrix_helper("1", "ooo", "x", "[]#0");
        sylvesterMatrix_helper("1", "ooo", "ooo", "[[1]]");
        sylvesterMatrix_helper("1", "x^2-4*x+7", "x", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("1", "x^2+2*x*y+y^2", "x", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("1", "x^2+2*x*y+y^2", "y", "[[1, 0], [0, 1]]");

        sylvesterMatrix_helper("-17", "1", "x", "[]#0");
        sylvesterMatrix_helper("-17", "-17", "x", "[]#0");
        sylvesterMatrix_helper("-17", "ooo", "x", "[]#0");
        sylvesterMatrix_helper("-17", "ooo", "ooo", "[[-17]]");
        sylvesterMatrix_helper("-17", "x^2-4*x+7", "x", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("-17", "x^2+2*x*y+y^2", "x", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("-17", "x^2+2*x*y+y^2", "y", "[[-17, 0], [0, -17]]");

        sylvesterMatrix_helper("ooo", "1", "x", "[]#0");
        sylvesterMatrix_helper("ooo", "1", "ooo", "[[1]]");
        sylvesterMatrix_helper("ooo", "-17", "x", "[]#0");
        sylvesterMatrix_helper("ooo", "-17", "ooo", "[[-17]]");
        sylvesterMatrix_helper("ooo", "ooo", "x", "[]#0");
        sylvesterMatrix_helper("ooo", "ooo", "ooo", "[[1, 0], [1, 0]]");
        sylvesterMatrix_helper("ooo", "x^2-4*x+7", "x", "[[x, 0], [0, x]]");
        sylvesterMatrix_helper("ooo", "x^2-4*x+7", "ooo", "[[x^2-4*x+7]]");

        sylvesterMatrix_helper("x^2-4*x+7", "1", "x", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("x^2-4*x+7", "-17", "x", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("x^2-4*x+7", "ooo", "x", "[[x, 0], [0, x]]");
        sylvesterMatrix_helper("x^2-4*x+7", "ooo", "ooo", "[[x^2-4*x+7]]");
        sylvesterMatrix_helper("x^2-4*x+7", "x^2-4*x+7", "x",
                "[[1, -4, 7, 0], [0, 1, -4, 7], [1, -4, 7, 0], [0, 1, -4, 7]]");
        sylvesterMatrix_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "x",
                "[[1, -4, 7, 0], [0, 1, -4, 7], [1, 2*x, x^2, 0], [0, 1, 2*x, x^2]]");
        sylvesterMatrix_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "y", "[[x^2-4*x+7, 0], [0, x^2-4*x+7]]");

        sylvesterMatrix_helper("x^2+2*x*y+y^2", "1", "x", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "1", "y", "[[1, 0], [0, 1]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "-17", "x", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "-17", "y", "[[-17, 0], [0, -17]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "x",
                "[[1, 2*x, x^2, 0], [0, 1, 2*x, x^2], [1, -4, 7, 0], [0, 1, -4, 7]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "y", "[[x^2-4*x+7, 0], [0, x^2-4*x+7]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "x",
                "[[1, 2*x, x^2, 0], [0, 1, 2*x, x^2], [1, 2*x, x^2, 0], [0, 1, 2*x, x^2]]");
        sylvesterMatrix_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "y",
                "[[1, 2*x, x^2, 0], [0, 1, 2*x, x^2], [1, 2*x, x^2, 0], [0, 1, 2*x, x^2]]");

        sylvesterMatrix_helper("x", "y", "z", "[]#0");
        sylvesterMatrix_helper("x*y-1", "x^2+y^2-5", "x", "[[x, -1, 0], [0, x, -1], [1, 0, x^2-5]]");
        sylvesterMatrix_helper("x*y-1", "x^2+y^2-5", "y", "[[x, -1, 0], [0, x, -1], [1, 0, x^2-5]]");
        sylvesterMatrix_helper(
                "40*x^3+40*y^3-180*x^2-200*x*y+120*y^2+70*x+420*y+189",
                "56250000*x^2+625000000*y^2-112500000*x-371950000*y-638411279",
                "x",
                "[[40, -180, -200*x+70, 40*x^3+120*x^2+420*x+189, 0]," +
                " [0, 40, -180, -200*x+70, 40*x^3+120*x^2+420*x+189]," +
                " [56250000, -112500000, 625000000*x^2-371950000*x-638411279, 0, 0]," +
                " [0, 56250000, -112500000, 625000000*x^2-371950000*x-638411279, 0]," +
                " [0, 0, 56250000, -112500000, 625000000*x^2-371950000*x-638411279]]"
        );
        sylvesterMatrix_helper(
                "40*x^3+40*y^3-180*x^2-200*x*y+120*y^2+70*x+420*y+189",
                "56250000*x^2+625000000*y^2-112500000*x-371950000*y-638411279",
                "y",
                "[[40, 120, -200*x+420, 40*x^3-180*x^2+70*x+189, 0]," +
                " [0, 40, 120, -200*x+420, 40*x^3-180*x^2+70*x+189]," +
                " [625000000, -371950000, 56250000*x^2-112500000*x-638411279, 0, 0]," +
                " [0, 625000000, -371950000, 56250000*x^2-112500000*x-638411279, 0]," +
                " [0, 0, 625000000, -371950000, 56250000*x^2-112500000*x-638411279]]"
        );

        sylvesterMatrix_fail_helper("0", "0", "x");
        sylvesterMatrix_fail_helper("0", "x", "x");
        sylvesterMatrix_fail_helper("x", "0", "x");
        sylvesterMatrix_fail_helper("x*y", "y", "z");
        sylvesterMatrix_fail_helper("x*y*z", "y", "z");
    }

    private static void resultant_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String v,
            @NotNull String output
    ) {
        aeq(readStrict(a).get().resultant(readStrict(b).get(), Variable.readStrict(v).get()), output);
    }

    private static void resultant_fail_helper(@NotNull String a, @NotNull String b, @NotNull String v) {
        try {
            readStrict(a).get().resultant(readStrict(b).get(), Variable.readStrict(v).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testResultant() {
        resultant_helper("1", "1", "x", "1");
        resultant_helper("1", "-17", "x", "1");
        resultant_helper("1", "ooo", "x", "1");
        resultant_helper("1", "ooo", "ooo", "1");
        resultant_helper("1", "x^2-4*x+7", "x", "1");
        resultant_helper("1", "x^2+2*x*y+y^2", "x", "1");
        resultant_helper("1", "x^2+2*x*y+y^2", "y", "1");

        resultant_helper("-17", "1", "x", "1");
        resultant_helper("-17", "-17", "x", "1");
        resultant_helper("-17", "ooo", "x", "1");
        resultant_helper("-17", "ooo", "ooo", "-17");
        resultant_helper("-17", "x^2-4*x+7", "x", "289");
        resultant_helper("-17", "x^2+2*x*y+y^2", "x", "289");
        resultant_helper("-17", "x^2+2*x*y+y^2", "y", "289");

        resultant_helper("ooo", "1", "x", "1");
        resultant_helper("ooo", "1", "ooo", "1");
        resultant_helper("ooo", "-17", "x", "1");
        resultant_helper("ooo", "-17", "ooo", "-17");
        resultant_helper("ooo", "ooo", "x", "1");
        resultant_helper("ooo", "ooo", "ooo", "0");
        resultant_helper("ooo", "x^2-4*x+7", "x", "x^2");
        resultant_helper("ooo", "x^2-4*x+7", "ooo", "x^2-4*x+7");

        resultant_helper("x^2-4*x+7", "1", "x", "1");
        resultant_helper("x^2-4*x+7", "-17", "x", "289");
        resultant_helper("x^2-4*x+7", "ooo", "x", "x^2");
        resultant_helper("x^2-4*x+7", "ooo", "ooo", "x^2-4*x+7");
        resultant_helper("x^2-4*x+7", "x^2-4*x+7", "x", "0");
        resultant_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "x", "x^4+8*x^3+30*x^2+56*x+49");
        resultant_helper("x^2-4*x+7", "x^2+2*x*y+y^2", "y", "x^4-8*x^3+30*x^2-56*x+49");

        resultant_helper("x^2+2*x*y+y^2", "1", "x", "1");
        resultant_helper("x^2+2*x*y+y^2", "1", "y", "1");
        resultant_helper("x^2+2*x*y+y^2", "-17", "x", "289");
        resultant_helper("x^2+2*x*y+y^2", "-17", "y", "289");
        resultant_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "x", "x^4+8*x^3+30*x^2+56*x+49");
        resultant_helper("x^2+2*x*y+y^2", "x^2-4*x+7", "y", "x^4-8*x^3+30*x^2-56*x+49");
        resultant_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "x", "0");
        resultant_helper("x^2+2*x*y+y^2", "x^2+2*x*y+y^2", "y", "0");

        resultant_helper("x", "y", "z", "1");
        resultant_helper("x*y-1", "x^2+y^2-5", "x", "x^4-5*x^2+1");
        resultant_helper("x*y-1", "x^2+y^2-5", "y", "x^4-5*x^2+1");
        resultant_helper(
                "40*x^3+40*y^3-180*x^2-200*x*y+120*y^2+70*x+420*y+189",
                "56250000*x^2+625000000*y^2-112500000*x-371950000*y-638411279",
                "x",
                "390909765625000000000000000000*x^6-334642968750000000000000000000*x^5-" +
                "820339106250000000000000000000*x^4+484333026034437500000000000000*x^3+" +
                "613612072168527615000000000000*x^2-176255458931123858987760000000*x-161854774015753450833573022400"
        );
        resultant_helper(
                "40*x^3+40*y^3-180*x^2-200*x*y+120*y^2+70*x+420*y+189",
                "56250000*x^2+625000000*y^2-112500000*x-371950000*y-638411279",
                "y",
                "390909765625000000000000000000*x^6-3759397031250000000000000000000*x^5+" +
                "10607821713075187500000000000000*x^4-379173958149800000000000000000*x^3-" +
                "40048005401850319722930000000000*x^2+53353623180055593535860000000000*x-" +
                "20251526018693655247793313022400"
        );

        resultant_fail_helper("0", "0", "x");
        resultant_fail_helper("0", "x", "x");
        resultant_fail_helper("x", "0", "x");
        resultant_fail_helper("x*y", "y", "z");
        resultant_fail_helper("x*y*z", "y", "z");
    }

    private static void powerReduce_helper(
            @NotNull String p,
            @NotNull String minimalPolynomials,
            @NotNull String result
    ) {
        MultivariatePolynomial q = readStrict(p).get().powerReduce(readVariablePolynomialMap(minimalPolynomials));
        q.validate();
        aeq(q, result);
    }

    private static void powerReduce_fail_helper(@NotNull String p, @NotNull String minimalPolynomials) {
        try {
            readStrict(p).get().powerReduce(readVariablePolynomialMapWithNulls(minimalPolynomials));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testPowerReduce() {
        powerReduce_helper("0", "[(a, x)]", "0");
        powerReduce_helper("1", "[(a, x)]", "1");
        powerReduce_helper("a", "[(a, x)]", "0");
        powerReduce_helper("x^2+2*x*y+y^2", "[]", "x^2+2*x*y+y^2");
        powerReduce_helper("x^2+2*x*y+y^2", "[(x, x^2-2)]", "2*x*y+y^2+2");
        powerReduce_helper("x^2+2*x*y+y^2", "[(x, x^2-2), (y, x^2-3)]", "2*x*y+5");
        powerReduce_helper("x*y^2*z+x^2*z^2+x^3+z^2", "[(x, x^2-2), (y, x^2-3), (z, x^2-x-1)]", "3*x*z+2*x+3*z+3");
        powerReduce_helper("40*x^3+40*y^3-180*x^2-200*x*y+120*y^2+70*x+420*y+189", "[(x, x^2-2), (y, x^2-x-1)]",
                "-200*x*y+150*x+620*y-11");

        powerReduce_fail_helper("1", "[(a, 0)]");
        powerReduce_fail_helper("1", "[(a, 1)]");
        powerReduce_fail_helper("1", "[(a, 2*x)]");
        powerReduce_fail_helper("1", "[(null, x)]");
        powerReduce_fail_helper("1", "[(x, null)]");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readMultivariatePolynomialList(
                        "[0, 1, ooo, -17, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2, a+b+c+d+e+f, x*y^2*z+x^2*z^2+x^3+z^2]"
                ),
                readMultivariatePolynomialList(
                        "[0, 1, ooo, -17, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2, a+b+c+d+e+f, x*y^2*z+x^2*z^2+x^3+z^2]"
                )
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
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
        hashCode_helper("x*y^2*z+x^2*z^2+x^3+z^2", 539199972);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readMultivariatePolynomialList(
                        "[0, -17, 1, ooo, a+b+c+d+e+f, x^2-4*x+7, x^2+2*x*y+y^2, a*b*c, x*y^2*z+x^2*z^2+x^3+z^2]"
                )
        );
    }

    private static void readStrict_String_MonomialOrder_helper(
            @NotNull String p,
            @NotNull String mo,
            @NotNull String output
    ) {
        Optional<MultivariatePolynomial> oq = readStrict(p, MonomialOrder.readStrict(mo).get());
        if (oq.isPresent()) {
            oq.get().validate();
        }
        aeq(oq, output);
    }

    @Test
    public void testReadStrict_String_MonomialOrder() {
        readStrict_String_MonomialOrder_helper("0", "LEX", "Optional[0]");
        readStrict_String_MonomialOrder_helper("0", "GRLEX", "Optional[0]");
        readStrict_String_MonomialOrder_helper("0", "GREVLEX", "Optional[0]");
        readStrict_String_MonomialOrder_helper("1", "LEX", "Optional[1]");
        readStrict_String_MonomialOrder_helper("1", "GRLEX", "Optional[1]");
        readStrict_String_MonomialOrder_helper("1", "GREVLEX", "Optional[1]");
        readStrict_String_MonomialOrder_helper("-17", "LEX", "Optional[-17]");
        readStrict_String_MonomialOrder_helper("-17", "GRLEX", "Optional[-17]");
        readStrict_String_MonomialOrder_helper("-17", "GREVLEX", "Optional[-17]");
        readStrict_String_MonomialOrder_helper("ooo", "LEX", "Optional[ooo]");
        readStrict_String_MonomialOrder_helper("ooo", "GRLEX", "Optional[ooo]");
        readStrict_String_MonomialOrder_helper("ooo", "GREVLEX", "Optional[ooo]");
        readStrict_String_MonomialOrder_helper("-ooo", "LEX", "Optional[-ooo]");
        readStrict_String_MonomialOrder_helper("-ooo", "GRLEX", "Optional[-ooo]");
        readStrict_String_MonomialOrder_helper("-ooo", "GREVLEX", "Optional[-ooo]");
        readStrict_String_MonomialOrder_helper("a*b*c", "LEX", "Optional[a*b*c]");
        readStrict_String_MonomialOrder_helper("a*b*c", "GRLEX", "Optional[a*b*c]");
        readStrict_String_MonomialOrder_helper("a*b*c", "GREVLEX", "Optional[a*b*c]");
        readStrict_String_MonomialOrder_helper("x^2-4*x+7", "LEX", "Optional[x^2-4*x+7]");
        readStrict_String_MonomialOrder_helper("x^2-4*x+7", "GRLEX", "Optional[x^2-4*x+7]");
        readStrict_String_MonomialOrder_helper("x^2-4*x+7", "GREVLEX", "Optional[x^2-4*x+7]");
        readStrict_String_MonomialOrder_helper("a+b+c+d+e+f", "LEX", "Optional[a+b+c+d+e+f]");
        readStrict_String_MonomialOrder_helper("a+b+c+d+e+f", "GRLEX", "Optional[a+b+c+d+e+f]");
        readStrict_String_MonomialOrder_helper("a+b+c+d+e+f", "GREVLEX", "Optional[a+b+c+d+e+f]");
        readStrict_String_MonomialOrder_helper("x^3+x^2*z^2+x*y^2*z+z^2", "LEX", "Optional[x*y^2*z+x^2*z^2+x^3+z^2]");
        readStrict_String_MonomialOrder_helper("x^2*z^2+x*y^2*z+x^3+z^2", "GRLEX",
                "Optional[x*y^2*z+x^2*z^2+x^3+z^2]");
        readStrict_String_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX",
                "Optional[x*y^2*z+x^2*z^2+x^3+z^2]");

        readStrict_String_MonomialOrder_helper("", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("hello", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("hello", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("hello", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper(" ", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper(" ", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper(" ", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("00", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("00", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("00", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-0", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-0", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-0", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("1/2", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("1/2", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("1/2", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("1*x", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("1*x", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("1*x", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x*2", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x*2", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x*2", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("0*x", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("0*x", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("0*x", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-1*x", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-1*x", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-1*x", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a*a", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a*a", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a*a", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^1", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^1", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^1", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^0", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^0", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^0", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^-1", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^-1", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a^-1", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("b*a", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("b*a", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("b*a", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-4*x+x^2+7", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-4*x+x^2+7", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("-4*x+x^2+7", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("b+a+c+d+e+f", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("b+a+c+d+e+f", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("b+a+c+d+e+f", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+a", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+a", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+a", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+0", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+0", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+0", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+-1", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+-1", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("a+-1", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("*x", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("*x", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("*x", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+x", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+x", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+x", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+1", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+1", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+1", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+0", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+0", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("+0", "GREVLEX", "Optional.empty");

        readStrict_String_MonomialOrder_helper("x^2*z^2+x*y^2*z+x^3+z^2", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x^3+x^2*z^2+x*y^2*z+z^2", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x^3+x^2*z^2+x*y^2*z+z^2", "GREVLEX", "Optional.empty");
        readStrict_String_MonomialOrder_helper("x^2*z^2+x*y^2*z+x^3+z^2", "GREVLEX", "Optional.empty");
    }

    private static void readStrict_String_helper(@NotNull String input, @NotNull String output) {
        Optional<MultivariatePolynomial> op = readStrict(input);
        if (op.isPresent()) {
            op.get().validate();
        }
        aeq(op, output);
    }

    @Test
    public void testReadStrict_String() {
        readStrict_String_helper("0", "Optional[0]");
        readStrict_String_helper("1", "Optional[1]");
        readStrict_String_helper("-17", "Optional[-17]");
        readStrict_String_helper("ooo", "Optional[ooo]");
        readStrict_String_helper("-ooo", "Optional[-ooo]");
        readStrict_String_helper("a*b*c", "Optional[a*b*c]");
        readStrict_String_helper("x^2-4*x+7", "Optional[x^2-4*x+7]");
        readStrict_String_helper("a+b+c+d+e+f", "Optional[a+b+c+d+e+f]");
        readStrict_String_helper("x*y^2*z+x^2*z^2+x^3+z^2", "Optional[x*y^2*z+x^2*z^2+x^3+z^2]");

        readStrict_String_helper("", "Optional.empty");
        readStrict_String_helper("hello", "Optional.empty");
        readStrict_String_helper(" ", "Optional.empty");
        readStrict_String_helper("00", "Optional.empty");
        readStrict_String_helper("-0", "Optional.empty");
        readStrict_String_helper("1/2", "Optional.empty");
        readStrict_String_helper("1*x", "Optional.empty");
        readStrict_String_helper("x*2", "Optional.empty");
        readStrict_String_helper("0*x", "Optional.empty");
        readStrict_String_helper("-1*x", "Optional.empty");
        readStrict_String_helper("a*a", "Optional.empty");
        readStrict_String_helper("a^1", "Optional.empty");
        readStrict_String_helper("a^0", "Optional.empty");
        readStrict_String_helper("a^-1", "Optional.empty");
        readStrict_String_helper("b*a", "Optional.empty");
        readStrict_String_helper("-4*x+x^2+7", "Optional.empty");
        readStrict_String_helper("b+a+c+d+e+f", "Optional.empty");
        readStrict_String_helper("a+a", "Optional.empty");
        readStrict_String_helper("a+0", "Optional.empty");
        readStrict_String_helper("a+-1", "Optional.empty");
        readStrict_String_helper("*x", "Optional.empty");
        readStrict_String_helper("+x", "Optional.empty");
        readStrict_String_helper("+1", "Optional.empty");
        readStrict_String_helper("+0", "Optional.empty");
    }

    private static void toString_MonomialOrder_helper(@NotNull String p, @NotNull String mo, @NotNull String output) {
        aeq(readStrict(p).get().toString(MonomialOrder.readStrict(mo).get()), output);
    }

    @Test
    public void testToString_MonomialOrder() {
        toString_MonomialOrder_helper("0", "LEX", "0");
        toString_MonomialOrder_helper("0", "GRLEX", "0");
        toString_MonomialOrder_helper("0", "GREVLEX", "0");
        toString_MonomialOrder_helper("1", "LEX", "1");
        toString_MonomialOrder_helper("1", "GRLEX", "1");
        toString_MonomialOrder_helper("1", "GREVLEX", "1");
        toString_MonomialOrder_helper("-17", "LEX", "-17");
        toString_MonomialOrder_helper("-17", "GRLEX", "-17");
        toString_MonomialOrder_helper("-17", "GREVLEX", "-17");
        toString_MonomialOrder_helper("ooo", "LEX", "ooo");
        toString_MonomialOrder_helper("ooo", "GRLEX", "ooo");
        toString_MonomialOrder_helper("ooo", "GREVLEX", "ooo");
        toString_MonomialOrder_helper("a*b*c", "LEX", "a*b*c");
        toString_MonomialOrder_helper("a*b*c", "GRLEX", "a*b*c");
        toString_MonomialOrder_helper("a*b*c", "GREVLEX", "a*b*c");
        toString_MonomialOrder_helper("x^2-4*x+7", "LEX", "x^2-4*x+7");
        toString_MonomialOrder_helper("x^2-4*x+7", "GRLEX", "x^2-4*x+7");
        toString_MonomialOrder_helper("x^2-4*x+7", "GREVLEX", "x^2-4*x+7");
        toString_MonomialOrder_helper("x^2+2*x*y+y^2", "LEX", "x^2+2*x*y+y^2");
        toString_MonomialOrder_helper("x^2+2*x*y+y^2", "GRLEX", "x^2+2*x*y+y^2");
        toString_MonomialOrder_helper("x^2+2*x*y+y^2", "GREVLEX", "x^2+2*x*y+y^2");
        toString_MonomialOrder_helper("a+b+c+d+e+f", "LEX", "a+b+c+d+e+f");
        toString_MonomialOrder_helper("a+b+c+d+e+f", "GRLEX", "a+b+c+d+e+f");
        toString_MonomialOrder_helper("a+b+c+d+e+f", "GREVLEX", "a+b+c+d+e+f");
        toString_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX", "x^3+x^2*z^2+x*y^2*z+z^2");
        toString_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX", "x^2*z^2+x*y^2*z+x^3+z^2");
        toString_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX", "x*y^2*z+x^2*z^2+x^3+z^2");
    }

    private static @NotNull List<Pair<Monomial, BigInteger>> readMonomialBigIntegerPairList(
            @NotNull String s
    ) {
        return Readers.readListStrict(
                u -> Pair.readStrict(
                        u,
                        t -> NullableOptional.fromOptional(Monomial.readStrict(t)),
                        t -> NullableOptional.fromOptional(Readers.readBigIntegerStrict(t))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<Monomial, BigInteger>> readMonomialBigIntegerPairListWithNulls(
            @NotNull String s
    ) {
        return Readers.readListWithNullsStrict(
                u -> Pair.readStrict(
                        u, Readers.readWithNullsStrict(Monomial::readStrict),
                        Readers.readWithNullsStrict(Readers::readBigIntegerStrict)
                )
        ).apply(s).get();
    }

    private static @NotNull List<MultivariatePolynomial> readMultivariatePolynomialList(@NotNull String s) {
        return Readers.readListStrict(MultivariatePolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<MultivariatePolynomial> readMultivariatePolynomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(MultivariatePolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull
    Map<Variable, BigInteger> readVariableBigIntegerMap(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                t -> NullableOptional.fromOptional(Variable.readStrict(t)),
                                t -> NullableOptional.fromOptional(Readers.readBigIntegerStrict(t))
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, BigInteger> readVariableBigIntegerMapWithNulls(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                Readers.readWithNullsStrict(Variable::readStrict),
                                Readers.readWithNullsStrict(Readers::readBigIntegerStrict)
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, Rational> readVariableRationalMap(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                t -> NullableOptional.fromOptional(Variable.readStrict(t)),
                                t -> NullableOptional.fromOptional(Rational.readStrict(t))
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, Rational> readVariableRationalMapWithNulls(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                Readers.readWithNullsStrict(Variable::readStrict),
                                Readers.readWithNullsStrict(Rational::readStrict)
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, Monomial> readVariableMonomialMap(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                t -> NullableOptional.fromOptional(Variable.readStrict(t)),
                                t -> NullableOptional.fromOptional(Monomial.readStrict(t))
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, Monomial> readVariableMonomialMapWithNulls(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                Readers.readWithNullsStrict(Variable::readStrict),
                                Readers.readWithNullsStrict(Monomial::readStrict)
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, MultivariatePolynomial> readVariableMultivariatePolynomialMap(
            @NotNull String s
    ) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                t -> NullableOptional.fromOptional(Variable.readStrict(t)),
                                t -> NullableOptional.fromOptional(readStrict(t))
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, MultivariatePolynomial> readVariableMultivariatePolynomialMapWithNulls(
            @NotNull String s
    ) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                Readers.readWithNullsStrict(Variable::readStrict),
                                Readers.readWithNullsStrict(MultivariatePolynomial::readStrict)
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, Polynomial> readVariablePolynomialMap(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                t -> NullableOptional.fromOptional(Variable.readStrict(t)),
                                t -> NullableOptional.fromOptional(Polynomial.readStrict(t))
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull Map<Variable, Polynomial> readVariablePolynomialMapWithNulls(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.readStrict(
                                u,
                                Readers.readWithNullsStrict(Variable::readStrict),
                                Readers.readWithNullsStrict(Polynomial::readStrict)
                        )
                ).apply(s).get()
        );
    }
}
