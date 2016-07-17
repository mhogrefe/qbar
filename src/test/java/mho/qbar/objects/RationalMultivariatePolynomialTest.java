package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static mho.qbar.objects.RationalMultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.fail;

public class RationalMultivariatePolynomialTest {
    private static void constant_helper(@NotNull RationalMultivariatePolynomial input, @NotNull String output) {
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

        iterable_helper("-4/3", "LEX", "[(1, -4/3)]");
        iterable_helper("-4/3", "GRLEX", "[(1, -4/3)]");
        iterable_helper("-4/3", "GREVLEX", "[(1, -4/3)]");

        iterable_helper("ooo", "LEX", "[(ooo, 1)]");
        iterable_helper("ooo", "GRLEX", "[(ooo, 1)]");
        iterable_helper("ooo", "GREVLEX", "[(ooo, 1)]");

        iterable_helper("a*b*c", "LEX", "[(a*b*c, 1)]");
        iterable_helper("a*b*c", "GRLEX", "[(a*b*c, 1)]");
        iterable_helper("a*b*c", "GREVLEX", "[(a*b*c, 1)]");

        iterable_helper("x^2-7/4*x+1/3", "LEX", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        iterable_helper("x^2-7/4*x+1/3", "GRLEX", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        iterable_helper("x^2-7/4*x+1/3", "GREVLEX", "[(1, 1/3), (x, -7/4), (x^2, 1)]");

        iterable_helper("x^2+1/2*x*y+y^2", "LEX", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        iterable_helper("x^2+1/2*x*y+y^2", "GRLEX", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        iterable_helper("x^2+1/2*x*y+y^2", "GREVLEX", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");

        iterable_helper("a+b+c+d+e+f", "LEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterable_helper("a+b+c+d+e+f", "GRLEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterable_helper("a+b+c+d+e+f", "GREVLEX", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");

        iterable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "LEX", "[(z^2, 22/7), (x*y^2*z, 1), (x^2*z^2, 1), (x^3, 1)]");
        iterable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "GRLEX",
                "[(z^2, 22/7), (x^3, 1), (x*y^2*z, 1), (x^2*z^2, 1)]");
        iterable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "GREVLEX",
                "[(z^2, 22/7), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");
    }

    private static void iterator_helper(@NotNull String x, @NotNull String output) {
        aeq(toList(readStrict(x).get()), output);
    }

    @Test
    public void testIterator() {
        iterator_helper("0", "[]");
        iterator_helper("1", "[(1, 1)]");
        iterator_helper("-4/3", "[(1, -4/3)]");
        iterator_helper("ooo", "[(ooo, 1)]");
        iterator_helper("a*b*c", "[(a*b*c, 1)]");
        iterator_helper("x^2-7/4*x+1/3", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        iterator_helper("x^2+1/2*x*y+y^2", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        iterator_helper("a+b+c+d+e+f", "[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]");
        iterator_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(z^2, 22/7), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");
    }

    private static void onlyHasIntegralCoefficients_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().onlyHasIntegralCoefficients(), output);
    }

    @Test
    public void testOnlyHasIntegralCoefficients() {
        onlyHasIntegralCoefficients_helper("0", true);
        onlyHasIntegralCoefficients_helper("1", true);
        onlyHasIntegralCoefficients_helper("-17", true);
        onlyHasIntegralCoefficients_helper("ooo", true);
        onlyHasIntegralCoefficients_helper("a*b*c", true);
        onlyHasIntegralCoefficients_helper("x^2-4*x+7", true);
        onlyHasIntegralCoefficients_helper("x^2+2*x*y+y^2", true);
        onlyHasIntegralCoefficients_helper("a+b+c+d+e+f", true);
        onlyHasIntegralCoefficients_helper("x*y^2*z+x^2*z^2+x^3+z^2", true);

        onlyHasIntegralCoefficients_helper("-4/3", false);
        onlyHasIntegralCoefficients_helper("x^2-7/4*x+1/3", false);
        onlyHasIntegralCoefficients_helper("x^2+1/2*x*y+y^2", false);
        onlyHasIntegralCoefficients_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", false);
    }

    private static void toMultivariatePolynomial_helper(@NotNull String p) {
        aeq(readStrict(p).get().toMultivariatePolynomial(), p);
    }

    private static void toMultivariatePolynomial_fail_helper(@NotNull String p) {
        try {
            readStrict(p).get().toMultivariatePolynomial();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testToMultivariatePolynomial() {
        toMultivariatePolynomial_helper("0");
        toMultivariatePolynomial_helper("1");
        toMultivariatePolynomial_helper("-17");
        toMultivariatePolynomial_helper("ooo");
        toMultivariatePolynomial_helper("a*b*c");
        toMultivariatePolynomial_helper("x^2-4*x+7");
        toMultivariatePolynomial_helper("x^2+2*x*y+y^2");
        toMultivariatePolynomial_helper("a+b+c+d+e+f");
        toMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+z^2");

        toMultivariatePolynomial_fail_helper("-4/3");
        toMultivariatePolynomial_fail_helper("x^2-7/4*x+1/3");
        toMultivariatePolynomial_fail_helper("x^2+1/2*x*y+y^2");
        toMultivariatePolynomial_fail_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2");
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
        coefficient_helper("x^2-7/4*x+1/3", "a", "0");
        coefficient_helper("x^2-7/4*x+1/3", "x^2", "1");
        coefficient_helper("x^2-7/4*x+1/3", "x", "-7/4");
        coefficient_helper("x^2-7/4*x+1/3", "1", "1/3");
        coefficient_helper("a+b+c+d+e+f", "1", "0");
        coefficient_helper("a+b+c+d+e+f", "a", "1");
        coefficient_helper("a+b+c+d+e+f", "f", "1");
        coefficient_helper("a+b+c+d+e+f", "g", "0");
    }

    private static void of_List_Pair_Monomial_Rational_helper(@NotNull String input, @NotNull String output) {
        RationalMultivariatePolynomial p = of(readMonomialRationalPairList(input));
        p.validate();
        aeq(p, output);
    }

    private static void of_List_Pair_Monomial_Rational_fail_helper(@NotNull String input) {
        try {
            of(readMonomialRationalPairListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Pair_Monomial_Rational() {
        of_List_Pair_Monomial_Rational_helper("[]", "0");
        of_List_Pair_Monomial_Rational_helper("[(a*b^2, 0)]", "0");
        of_List_Pair_Monomial_Rational_helper("[(1, 1)]", "1");
        of_List_Pair_Monomial_Rational_helper("[(1, 1), (x, 0)]", "1");
        of_List_Pair_Monomial_Rational_helper("[(ooo, 1)]", "ooo");
        of_List_Pair_Monomial_Rational_helper("[(1, -4/3)]", "-4/3");
        of_List_Pair_Monomial_Rational_helper("[(1, -10), (1, -7)]", "-17");
        of_List_Pair_Monomial_Rational_helper("[(a*b*c, 1)]", "a*b*c");
        of_List_Pair_Monomial_Rational_helper("[(1, 1/3), (x, -7/4), (x^2, 1)]", "x^2-7/4*x+1/3");
        of_List_Pair_Monomial_Rational_helper("[(x, -7/4), (1, 1/3), (x^2, 1)]", "x^2-7/4*x+1/3");
        of_List_Pair_Monomial_Rational_helper("[(y^2, 1), (x*y, 1/2), (x^2, 1)]", "x^2+1/2*x*y+y^2");
        of_List_Pair_Monomial_Rational_helper("[(x*y, 1), (x*y, -1/2), (y^2, 1), (x^2, 1), (z, 0)]",
                "x^2+1/2*x*y+y^2");
        of_List_Pair_Monomial_Rational_helper("[(f, 1), (e, 1), (d, 1), (c, 1), (b, 1), (a, 1)]",
                "a+b+c+d+e+f");
        of_List_Pair_Monomial_Rational_helper("[(f, 1), (b, 1), (d, 2), (d, -1), (c, 1), (e, 1), (a, 1)]",
                "a+b+c+d+e+f");

        of_List_Pair_Monomial_Rational_fail_helper("[(ooo, 1/3), null]");
        of_List_Pair_Monomial_Rational_fail_helper("[(ooo, null)]");
        of_List_Pair_Monomial_Rational_fail_helper("[(null, 1/3)]");
    }

    private static void of_Monomial_Rational_helper(@NotNull String ev, @NotNull String c, @NotNull String output) {
        RationalMultivariatePolynomial p = of(Monomial.readStrict(ev).get(), Rational.readStrict(c).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testOf_Monomial_Rational() {
        of_Monomial_Rational_helper("1", "0", "0");
        of_Monomial_Rational_helper("1", "1", "1");
        of_Monomial_Rational_helper("1", "-1", "-1");
        of_Monomial_Rational_helper("1", "4/3", "4/3");
        of_Monomial_Rational_helper("1", "-5/2", "-5/2");
        of_Monomial_Rational_helper("ooo", "0", "0");
        of_Monomial_Rational_helper("ooo", "1", "ooo");
        of_Monomial_Rational_helper("ooo", "-1", "-ooo");
        of_Monomial_Rational_helper("ooo", "4/3", "4/3*ooo");
        of_Monomial_Rational_helper("ooo", "-5/2", "-5/2*ooo");
        of_Monomial_Rational_helper("a*b^2", "0", "0");
        of_Monomial_Rational_helper("a*b^2", "1", "a*b^2");
        of_Monomial_Rational_helper("a*b^2", "-1", "-a*b^2");
        of_Monomial_Rational_helper("a*b^2", "4/3", "4/3*a*b^2");
        of_Monomial_Rational_helper("a*b^2", "-5/2", "-5/2*a*b^2");
    }

    private static void of_Rational_helper(@NotNull String input) {
        RationalMultivariatePolynomial p = of(Rational.readStrict(input).get());
        p.validate();
        aeq(p, input);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("0");
        of_Rational_helper("1");
        of_Rational_helper("-1");
        of_Rational_helper("4/3");
        of_Rational_helper("-5/2");
    }

    private static void of_BigInteger_helper(@NotNull String input) {
        RationalMultivariatePolynomial p = of(Readers.readBigIntegerStrict(input).get());
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
        RationalMultivariatePolynomial p = of(input);
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
        RationalMultivariatePolynomial p = of(Variable.readStrict(input).get());
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

    private static void of_RationalPolynomial_Variable_helper(
            @NotNull String p,
            @NotNull String v,
            @NotNull String output
    ) {
        RationalMultivariatePolynomial q = of(RationalPolynomial.readStrict(p).get(), Variable.readStrict(v).get());
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testOf_RationalPolynomial_Variable() {
        of_RationalPolynomial_Variable_helper("0", "x", "0");
        of_RationalPolynomial_Variable_helper("1", "x", "1");
        of_RationalPolynomial_Variable_helper("-4/3", "x", "-4/3");
        of_RationalPolynomial_Variable_helper("x", "x", "x");
        of_RationalPolynomial_Variable_helper("x", "ooo", "ooo");
        of_RationalPolynomial_Variable_helper("x^2-7/4*x+1/3", "x", "x^2-7/4*x+1/3");
        of_RationalPolynomial_Variable_helper("x^2-7/4*x+1/3", "ooo", "ooo^2-7/4*ooo+1/3");
        of_RationalPolynomial_Variable_helper("x^3-1", "x", "x^3-1");
        of_RationalPolynomial_Variable_helper("x^3-1", "ooo", "ooo^3-1");
        of_RationalPolynomial_Variable_helper("1/2*x^10", "x", "1/2*x^10");
        of_RationalPolynomial_Variable_helper("1/2*x^10", "ooo", "1/2*ooo^10");
    }

    private static void toRationalPolynomial_helper(@NotNull String input) {
        aeq(readStrict(input).get().toRationalPolynomial(), input);
    }

    private static void toRationalPolynomial_fail_helper(@NotNull String input) {
        try {
            readStrict(input).get().toRationalPolynomial();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testToRationalPolynomial() {
        toRationalPolynomial_helper("0");
        toRationalPolynomial_helper("1");
        toRationalPolynomial_helper("x");
        toRationalPolynomial_helper("-4/3");
        toRationalPolynomial_helper("x^2-7/4*x+1/3");
        toRationalPolynomial_helper("x^3-1");
        toRationalPolynomial_helper("1/2*x^10");

        toRationalPolynomial_fail_helper("a*b*c");
        toRationalPolynomial_fail_helper("x^2+1/2*x*y+y^2");
        toRationalPolynomial_fail_helper("a+b+c+d+e+f");
    }

    private static void variables_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().variables(), output);
    }

    @Test
    public void testVariables() {
        variables_helper("0", "[]");
        variables_helper("1", "[]");
        variables_helper("-4/3", "[]");
        variables_helper("ooo", "[ooo]");
        variables_helper("a*b*c", "[a, b, c]");
        variables_helper("x^2-7/4*x+1/3", "[x]");
        variables_helper("x^2+1/2*x*y+y^2", "[x, y]");
        variables_helper("a+b+c+d+e+f", "[a, b, c, d, e, f]");
    }

    private static void variableCount_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().variableCount(), output);
    }

    @Test
    public void testVariableCount() {
        variableCount_helper("0", 0);
        variableCount_helper("1", 0);
        variableCount_helper("-4/3", 0);
        variableCount_helper("ooo", 1);
        variableCount_helper("a*b*c", 3);
        variableCount_helper("x^2-7/4*x+1/3", 1);
        variableCount_helper("x^2+1/2*x*y+y^2", 2);
        variableCount_helper("a+b+c+d+e+f", 6);
        variableCount_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 3);
    }

    private static void termCount_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().termCount(), output);
    }

    @Test
    public void testTermCount() {
        termCount_helper("0", 0);
        termCount_helper("1", 1);
        termCount_helper("-4/3", 1);
        termCount_helper("ooo", 1);
        termCount_helper("a*b*c", 1);
        termCount_helper("x^2-7/4*x+1/3", 3);
        termCount_helper("x^2+1/2*x*y+y^2", 3);
        termCount_helper("a+b+c+d+e+f", 6);
        termCount_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 4);
    }

    private static void maxCoefficientBitLength_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().maxCoefficientBitLength(), output);
    }

    @Test
    public void testMaxCoefficientBitLength() {
        maxCoefficientBitLength_helper("0", 0);
        maxCoefficientBitLength_helper("1", 2);
        maxCoefficientBitLength_helper("-4/3", 5);
        maxCoefficientBitLength_helper("ooo", 2);
        maxCoefficientBitLength_helper("a*b*c", 2);
        maxCoefficientBitLength_helper("x^2-7/4*x+1/3", 6);
        maxCoefficientBitLength_helper("x^2+1/2*x*y+y^2", 3);
        maxCoefficientBitLength_helper("a+b+c+d+e+f", 2);
        maxCoefficientBitLength_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 8);
    }

    private static void degree_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("0", -1);
        degree_helper("1", 0);
        degree_helper("-4/3", 0);
        degree_helper("ooo", 1);
        degree_helper("a*b*c", 3);
        degree_helper("x^2-7/4*x+1/3", 2);
        degree_helper("x^2+1/2*x*y+y^2", 2);
        degree_helper("a+b+c+d+e+f", 1);
        degree_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 4);
    }

    private static void degree_Variable_helper(@NotNull String p, @NotNull String v, int output) {
        aeq(readStrict(p).get().degree(Variable.readStrict(v).get()), output);
    }

    @Test
    public void testDegree_Variable() {
        degree_Variable_helper("0", "a", -1);
        degree_Variable_helper("1", "a", 0);
        degree_Variable_helper("-4/3", "a", 0);
        degree_Variable_helper("ooo", "a", 0);
        degree_Variable_helper("ooo", "ooo", 1);
        degree_Variable_helper("a*b*c", "a", 1);
        degree_Variable_helper("a*b*c", "b", 1);
        degree_Variable_helper("a*b*c", "c", 1);
        degree_Variable_helper("a*b*c", "d", 0);
        degree_Variable_helper("x^2-7/4*x+1/3", "a", 0);
        degree_Variable_helper("x^2-7/4*x+1/3", "x", 2);
        degree_Variable_helper("x^2+1/2*x*y+y^2", "a", 0);
        degree_Variable_helper("x^2+1/2*x*y+y^2", "x", 2);
        degree_Variable_helper("x^2+1/2*x*y+y^2", "y", 2);
        degree_Variable_helper("a+b+c+d+e+f", "a", 1);
        degree_Variable_helper("a+b+c+d+e+f", "f", 1);
        degree_Variable_helper("a+b+c+d+e+f", "g", 0);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a", 0);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x", 3);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "y", 2);
        degree_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "z", 2);
    }

    private static void isHomogeneous_helper(@NotNull String input, boolean output) {
        aeq(readStrict(input).get().isHomogeneous(), output);
    }

    @Test
    public void testIsHomogeneous() {
        isHomogeneous_helper("0", true);
        isHomogeneous_helper("1", true);
        isHomogeneous_helper("-4/3", true);
        isHomogeneous_helper("ooo", true);
        isHomogeneous_helper("a*b*c", true);
        isHomogeneous_helper("x^2-7/4*x+1/3", false);
        isHomogeneous_helper("x^2+1/2*x*y+y^2", true);
        isHomogeneous_helper("a+b+c+d+e+f", true);
        isHomogeneous_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", false);
    }

    private void coefficientsOfVariable_helper(@NotNull String p, @NotNull String v, @NotNull String output) {
        List<RationalMultivariatePolynomial> ps =
                readStrict(p).get().coefficientsOfVariable(Variable.readStrict(v).get());
        ps.forEach(RationalMultivariatePolynomial::validate);
        aeq(ps, output);
    }

    @Test
    public void testCoefficientsOfVariable() {
        coefficientsOfVariable_helper("0", "a", "[]");
        coefficientsOfVariable_helper("1", "a", "[1]");
        coefficientsOfVariable_helper("-4/3", "a", "[-4/3]");
        coefficientsOfVariable_helper("ooo", "a", "[ooo]");
        coefficientsOfVariable_helper("ooo", "ooo", "[0, 1]");
        coefficientsOfVariable_helper("a*b*c", "z", "[a*b*c]");
        coefficientsOfVariable_helper("a*b*c", "a", "[0, b*c]");
        coefficientsOfVariable_helper("a*b*c", "b", "[0, a*c]");
        coefficientsOfVariable_helper("a*b*c", "c", "[0, a*b]");
        coefficientsOfVariable_helper("x^2-7/4*x+1/3", "a", "[x^2-7/4*x+1/3]");
        coefficientsOfVariable_helper("x^2-7/4*x+1/3", "x", "[1/3, -7/4, 1]");
        coefficientsOfVariable_helper("x^2+1/2*x*y+y^2", "a", "[x^2+1/2*x*y+y^2]");
        coefficientsOfVariable_helper("x^2+1/2*x*y+y^2", "x", "[y^2, 1/2*y, 1]");
        coefficientsOfVariable_helper("x^2+1/2*x*y+y^2", "y", "[x^2, 1/2*x, 1]");
        coefficientsOfVariable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x", "[22/7*z^2, y^2*z, z^2, 1]");
        coefficientsOfVariable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "y", "[x^2*z^2+x^3+22/7*z^2, 0, x*z]");
        coefficientsOfVariable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "z", "[x^3, x*y^2, x^2+22/7]");
    }

    private void groupVariables_List_Variable_MonomialOrder_helper(
            @NotNull String p,
            @NotNull String vs,
            @NotNull String o,
            @NotNull String output
    ) {
        List<Pair<Monomial, RationalMultivariatePolynomial>> ps = readStrict(p).get()
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
        groupVariables_List_Variable_MonomialOrder_helper("-4/3", "[]", "LEX", "[(1, -4/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("-4/3", "[]", "GRLEX", "[(1, -4/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("-4/3", "[]", "GREVLEX", "[(1, -4/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("-4/3", "[a]", "LEX", "[(1, -4/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("-4/3", "[a]", "GRLEX", "[(1, -4/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("-4/3", "[a]", "GREVLEX", "[(1, -4/3)]");
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
        groupVariables_List_Variable_MonomialOrder_helper("x^2-7/4*x+1/3", "[]", "LEX", "[(1, x^2-7/4*x+1/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-7/4*x+1/3", "[]", "GRLEX", "[(1, x^2-7/4*x+1/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-7/4*x+1/3", "[]", "GREVLEX", "[(1, x^2-7/4*x+1/3)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-7/4*x+1/3", "[x]", "LEX",
                "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-7/4*x+1/3", "[x]", "GRLEX",
                "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2-7/4*x+1/3", "[x]", "GREVLEX",
                "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[]", "LEX", "[(1, x^2+1/2*x*y+y^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[]", "GRLEX", "[(1, x^2+1/2*x*y+y^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[]", "GREVLEX",
                "[(1, x^2+1/2*x*y+y^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[x]", "LEX",
                "[(1, y^2), (x, 1/2*y), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[x]", "GRLEX",
                "[(1, y^2), (x, 1/2*y), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[x]", "GREVLEX",
                "[(1, y^2), (x, 1/2*y), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[y]", "LEX",
                "[(1, x^2), (y, 1/2*x), (y^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[y]", "GRLEX",
                "[(1, x^2), (y, 1/2*x), (y^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[y]", "GREVLEX",
                "[(1, x^2), (y, 1/2*x), (y^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[x, y]", "LEX",
                "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[x, y]", "GRLEX",
                "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x^2+1/2*x*y+y^2", "[x, y]", "GREVLEX",
                "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[]", "LEX",
                "[(1, x*y^2*z+x^2*z^2+x^3+22/7*z^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[]", "GRLEX",
                "[(1, x*y^2*z+x^2*z^2+x^3+22/7*z^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[]", "GREVLEX",
                "[(1, x*y^2*z+x^2*z^2+x^3+22/7*z^2)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x]", "LEX",
                "[(1, 22/7*z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x]", "GRLEX",
                "[(1, 22/7*z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x]", "GREVLEX",
                "[(1, 22/7*z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y]", "LEX",
                "[(1, x^2*z^2+x^3+22/7*z^2), (y^2, x*z)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y]", "GRLEX",
                "[(1, x^2*z^2+x^3+22/7*z^2), (y^2, x*z)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y]", "GREVLEX",
                "[(1, x^2*z^2+x^3+22/7*z^2), (y^2, x*z)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[z]", "LEX",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+22/7)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[z]", "GRLEX",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+22/7)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[z]", "GREVLEX",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+22/7)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y]", "LEX",
                "[(1, 22/7*z^2), (x*y^2, z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y]", "GRLEX",
                "[(1, 22/7*z^2), (x^2, z^2), (x*y^2, z), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y]", "GREVLEX",
                "[(1, 22/7*z^2), (x^2, z^2), (x*y^2, z), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, z]", "LEX",
                "[(z^2, 22/7), (x*z, y^2), (x^2*z^2, 1), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, z]", "GRLEX",
                "[(z^2, 22/7), (x*z, y^2), (x^3, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, z]", "GREVLEX",
                "[(z^2, 22/7), (x*z, y^2), (x^3, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y, z]", "LEX",
                "[(1, x^3), (z^2, x^2+22/7), (y^2*z, x)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y, z]", "GRLEX",
                "[(1, x^3), (z^2, x^2+22/7), (y^2*z, x)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y, z]", "GREVLEX",
                "[(1, x^3), (z^2, x^2+22/7), (y^2*z, x)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y, z]", "LEX",
                "[(z^2, 22/7), (x*y^2*z, 1), (x^2*z^2, 1), (x^3, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y, z]", "GRLEX",
                "[(z^2, 22/7), (x^3, 1), (x*y^2*z, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y, z]", "GREVLEX",
                "[(z^2, 22/7), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");

        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[null]", "LEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[null]", "GRLEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[null]", "GREVLEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[a, null, c]", "LEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[a, null, c]", "GRLEX");
        groupVariables_List_Variable_MonomialOrder_fail_helper("x", "[a, null, c]", "GREVLEX");
    }

    private void groupVariables_List_Variable_helper(@NotNull String p, @NotNull String vs, @NotNull String output) {
        List<Pair<Monomial, RationalMultivariatePolynomial>> ps =
                readStrict(p).get().groupVariables(readVariableList(vs));
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
        groupVariables_List_Variable_helper("-4/3", "[]", "[(1, -4/3)]");
        groupVariables_List_Variable_helper("-4/3", "[a]", "[(1, -4/3)]");
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
        groupVariables_List_Variable_helper("x^2-7/4*x+1/3", "[]", "[(1, x^2-7/4*x+1/3)]");
        groupVariables_List_Variable_helper("x^2-7/4*x+1/3", "[x]", "[(1, 1/3), (x, -7/4), (x^2, 1)]");
        groupVariables_List_Variable_helper("x^2+1/2*x*y+y^2", "[]", "[(1, x^2+1/2*x*y+y^2)]");
        groupVariables_List_Variable_helper("x^2+1/2*x*y+y^2", "[x]", "[(1, y^2), (x, 1/2*y), (x^2, 1)]");
        groupVariables_List_Variable_helper("x^2+1/2*x*y+y^2", "[y]", "[(1, x^2), (y, 1/2*x), (y^2, 1)]");
        groupVariables_List_Variable_helper("x^2+1/2*x*y+y^2", "[x, y]", "[(y^2, 1), (x*y, 1/2), (x^2, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[]",
                "[(1, x*y^2*z+x^2*z^2+x^3+22/7*z^2)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x]",
                "[(1, 22/7*z^2), (x, y^2*z), (x^2, z^2), (x^3, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y]",
                "[(1, x^2*z^2+x^3+22/7*z^2), (y^2, x*z)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[z]",
                "[(1, x^3), (z, x*y^2), (z^2, x^2+22/7)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y]",
                "[(1, 22/7*z^2), (x^2, z^2), (x*y^2, z), (x^3, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, z]",
                "[(z^2, 22/7), (x*z, y^2), (x^3, 1), (x^2*z^2, 1)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[y, z]",
                "[(1, x^3), (z^2, x^2+22/7), (y^2*z, x)]");
        groupVariables_List_Variable_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[x, y, z]",
                "[(z^2, 22/7), (x^3, 1), (x^2*z^2, 1), (x*y^2*z, 1)]");

        groupVariables_List_Variable_fail_helper("x", "[null]");
        groupVariables_List_Variable_fail_helper("x", "[a, null, c]");
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().add(readStrict(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testAdd() {
        add_helper("0", "0", "0");
        add_helper("0", "1", "1");
        add_helper("0", "-4/3", "-4/3");
        add_helper("0", "ooo", "ooo");
        add_helper("0", "a*b*c", "a*b*c");
        add_helper("0", "x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        add_helper("0", "x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2");
        add_helper("0", "a+b+c+d+e+f", "a+b+c+d+e+f");
        add_helper("0", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");

        add_helper("1", "0", "1");
        add_helper("1", "1", "2");
        add_helper("1", "-4/3", "-1/3");
        add_helper("1", "ooo", "ooo+1");
        add_helper("1", "a*b*c", "a*b*c+1");
        add_helper("1", "x^2-7/4*x+1/3", "x^2-7/4*x+4/3");
        add_helper("1", "x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2+1");
        add_helper("1", "a+b+c+d+e+f", "a+b+c+d+e+f+1");
        add_helper("1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+1");

        add_helper("-4/3", "0", "-4/3");
        add_helper("-4/3", "1", "-1/3");
        add_helper("-4/3", "-4/3", "-8/3");
        add_helper("-4/3", "ooo", "ooo-4/3");
        add_helper("-4/3", "a*b*c", "a*b*c-4/3");
        add_helper("-4/3", "x^2-7/4*x+1/3", "x^2-7/4*x-1");
        add_helper("-4/3", "x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2-4/3");
        add_helper("-4/3", "a+b+c+d+e+f", "a+b+c+d+e+f-4/3");
        add_helper("-4/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2-4/3");

        add_helper("ooo", "0", "ooo");
        add_helper("ooo", "1", "ooo+1");
        add_helper("ooo", "-4/3", "ooo-4/3");
        add_helper("ooo", "ooo", "2*ooo");
        add_helper("ooo", "a*b*c", "a*b*c+ooo");
        add_helper("ooo", "x^2-7/4*x+1/3", "x^2-7/4*x+ooo+1/3");
        add_helper("ooo", "x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2+ooo");
        add_helper("ooo", "a+b+c+d+e+f", "a+b+c+d+e+f+ooo");
        add_helper("ooo", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+ooo");

        add_helper("a*b*c", "0", "a*b*c");
        add_helper("a*b*c", "1", "a*b*c+1");
        add_helper("a*b*c", "-4/3", "a*b*c-4/3");
        add_helper("a*b*c", "ooo", "a*b*c+ooo");
        add_helper("a*b*c", "a*b*c", "2*a*b*c");
        add_helper("a*b*c", "x^2-7/4*x+1/3", "a*b*c+x^2-7/4*x+1/3");
        add_helper("a*b*c", "x^2+1/2*x*y+y^2", "a*b*c+x^2+1/2*x*y+y^2");
        add_helper("a*b*c", "a+b+c+d+e+f", "a*b*c+a+b+c+d+e+f");
        add_helper("a*b*c", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+a*b*c+x^3+22/7*z^2");

        add_helper("x^2-7/4*x+1/3", "0", "x^2-7/4*x+1/3");
        add_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+4/3");
        add_helper("x^2-7/4*x+1/3", "-4/3", "x^2-7/4*x-1");
        add_helper("x^2-7/4*x+1/3", "ooo", "x^2-7/4*x+ooo+1/3");
        add_helper("x^2-7/4*x+1/3", "a*b*c", "a*b*c+x^2-7/4*x+1/3");
        add_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "2*x^2-7/2*x+2/3");
        add_helper("x^2-7/4*x+1/3", "x^2+1/2*x*y+y^2", "2*x^2+1/2*x*y+y^2-7/4*x+1/3");
        add_helper("x^2-7/4*x+1/3", "a+b+c+d+e+f", "x^2+a+b+c+d+e+f-7/4*x+1/3");
        add_helper("x^2-7/4*x+1/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+x^2+22/7*z^2-7/4*x+1/3");

        add_helper("x^2+1/2*x*y+y^2", "0", "x^2+1/2*x*y+y^2");
        add_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2+1");
        add_helper("x^2+1/2*x*y+y^2", "-4/3", "x^2+1/2*x*y+y^2-4/3");
        add_helper("x^2+1/2*x*y+y^2", "ooo", "x^2+1/2*x*y+y^2+ooo");
        add_helper("x^2+1/2*x*y+y^2", "a*b*c", "a*b*c+x^2+1/2*x*y+y^2");
        add_helper("x^2+1/2*x*y+y^2", "x^2-7/4*x+1/3", "2*x^2+1/2*x*y+y^2-7/4*x+1/3");
        add_helper("x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2", "2*x^2+x*y+2*y^2");
        add_helper("x^2+1/2*x*y+y^2", "a+b+c+d+e+f", "x^2+1/2*x*y+y^2+a+b+c+d+e+f");
        add_helper("x^2+1/2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+x^2+1/2*x*y+y^2+22/7*z^2");

        add_helper("a+b+c+d+e+f", "0", "a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f+1");
        add_helper("a+b+c+d+e+f", "-4/3", "a+b+c+d+e+f-4/3");
        add_helper("a+b+c+d+e+f", "ooo", "a+b+c+d+e+f+ooo");
        add_helper("a+b+c+d+e+f", "a*b*c", "a*b*c+a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "x^2-7/4*x+1/3", "x^2+a+b+c+d+e+f-7/4*x+1/3");
        add_helper("a+b+c+d+e+f", "x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2+a+b+c+d+e+f");
        add_helper("a+b+c+d+e+f", "a+b+c+d+e+f", "2*a+2*b+2*c+2*d+2*e+2*f");
        add_helper("a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+a+b+c+d+e+f");

        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+1");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-4/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2-4/3");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "ooo", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+ooo");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a*b*c", "x*y^2*z+x^2*z^2+a*b*c+x^3+22/7*z^2");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x^2-7/4*x+1/3", "x*y^2*z+x^2*z^2+x^3+x^2+22/7*z^2-7/4*x+1/3");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x^2+1/2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+x^2+1/2*x*y+y^2+22/7*z^2");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+a+b+c+d+e+f");
        add_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "2*x*y^2*z+2*x^2*z^2+2*x^3+44/7*z^2");

        add_helper("a*b*c", "-a*b*c", "0");
        add_helper("a*b*c", "-a*b*c+1", "1");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(input).get().negate();
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testNegate() {
        negate_helper("0", "0");
        negate_helper("1", "-1");
        negate_helper("-4/3", "4/3");
        negate_helper("ooo", "-ooo");
        negate_helper("a*b*c", "-a*b*c");
        negate_helper("x^2-7/4*x+1/3", "-x^2+7/4*x-1/3");
        negate_helper("x^2+1/2*x*y+y^2", "-x^2-1/2*x*y-y^2");
        negate_helper("a+b+c+d+e+f", "-a-b-c-d-e-f");
        negate_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2-x^3-22/7*z^2");
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().subtract(readStrict(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testSubtract() {
        subtract_helper("0", "0", "0");
        subtract_helper("0", "1", "-1");
        subtract_helper("0", "-4/3", "4/3");
        subtract_helper("0", "ooo", "-ooo");
        subtract_helper("0", "a*b*c", "-a*b*c");
        subtract_helper("0", "x^2-7/4*x+1/3", "-x^2+7/4*x-1/3");
        subtract_helper("0", "x^2+1/2*x*y+y^2", "-x^2-1/2*x*y-y^2");
        subtract_helper("0", "a+b+c+d+e+f", "-a-b-c-d-e-f");
        subtract_helper("0", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2-x^3-22/7*z^2");

        subtract_helper("1", "0", "1");
        subtract_helper("1", "1", "0");
        subtract_helper("1", "-4/3", "7/3");
        subtract_helper("1", "ooo", "-ooo+1");
        subtract_helper("1", "a*b*c", "-a*b*c+1");
        subtract_helper("1", "x^2-7/4*x+1/3", "-x^2+7/4*x+2/3");
        subtract_helper("1", "x^2+1/2*x*y+y^2", "-x^2-1/2*x*y-y^2+1");
        subtract_helper("1", "a+b+c+d+e+f", "-a-b-c-d-e-f+1");
        subtract_helper("1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2-x^3-22/7*z^2+1");

        subtract_helper("-4/3", "0", "-4/3");
        subtract_helper("-4/3", "1", "-7/3");
        subtract_helper("-4/3", "-4/3", "0");
        subtract_helper("-4/3", "ooo", "-ooo-4/3");
        subtract_helper("-4/3", "a*b*c", "-a*b*c-4/3");
        subtract_helper("-4/3", "x^2-7/4*x+1/3", "-x^2+7/4*x-5/3");
        subtract_helper("-4/3", "x^2+1/2*x*y+y^2", "-x^2-1/2*x*y-y^2-4/3");
        subtract_helper("-4/3", "a+b+c+d+e+f", "-a-b-c-d-e-f-4/3");
        subtract_helper("-4/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2-x^3-22/7*z^2-4/3");

        subtract_helper("ooo", "0", "ooo");
        subtract_helper("ooo", "1", "ooo-1");
        subtract_helper("ooo", "-4/3", "ooo+4/3");
        subtract_helper("ooo", "ooo", "0");
        subtract_helper("ooo", "a*b*c", "-a*b*c+ooo");
        subtract_helper("ooo", "x^2-7/4*x+1/3", "-x^2+7/4*x+ooo-1/3");
        subtract_helper("ooo", "x^2+1/2*x*y+y^2", "-x^2-1/2*x*y-y^2+ooo");
        subtract_helper("ooo", "a+b+c+d+e+f", "-a-b-c-d-e-f+ooo");
        subtract_helper("ooo", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2-x^3-22/7*z^2+ooo");

        subtract_helper("a*b*c", "0", "a*b*c");
        subtract_helper("a*b*c", "1", "a*b*c-1");
        subtract_helper("a*b*c", "-4/3", "a*b*c+4/3");
        subtract_helper("a*b*c", "ooo", "a*b*c-ooo");
        subtract_helper("a*b*c", "a*b*c", "0");
        subtract_helper("a*b*c", "x^2-7/4*x+1/3", "a*b*c-x^2+7/4*x-1/3");
        subtract_helper("a*b*c", "x^2+1/2*x*y+y^2", "a*b*c-x^2-1/2*x*y-y^2");
        subtract_helper("a*b*c", "a+b+c+d+e+f", "a*b*c-a-b-c-d-e-f");
        subtract_helper("a*b*c", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2+a*b*c-x^3-22/7*z^2");

        subtract_helper("x^2-7/4*x+1/3", "0", "x^2-7/4*x+1/3");
        subtract_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x-2/3");
        subtract_helper("x^2-7/4*x+1/3", "-4/3", "x^2-7/4*x+5/3");
        subtract_helper("x^2-7/4*x+1/3", "ooo", "x^2-7/4*x-ooo+1/3");
        subtract_helper("x^2-7/4*x+1/3", "a*b*c", "-a*b*c+x^2-7/4*x+1/3");
        subtract_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3", "0");
        subtract_helper("x^2-7/4*x+1/3", "x^2+1/2*x*y+y^2", "-1/2*x*y-y^2-7/4*x+1/3");
        subtract_helper("x^2-7/4*x+1/3", "a+b+c+d+e+f", "x^2-a-b-c-d-e-f-7/4*x+1/3");
        subtract_helper("x^2-7/4*x+1/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "-x*y^2*z-x^2*z^2-x^3+x^2-22/7*z^2-7/4*x+1/3");

        subtract_helper("x^2+1/2*x*y+y^2", "0", "x^2+1/2*x*y+y^2");
        subtract_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2-1");
        subtract_helper("x^2+1/2*x*y+y^2", "-4/3", "x^2+1/2*x*y+y^2+4/3");
        subtract_helper("x^2+1/2*x*y+y^2", "ooo", "x^2+1/2*x*y+y^2-ooo");
        subtract_helper("x^2+1/2*x*y+y^2", "a*b*c", "-a*b*c+x^2+1/2*x*y+y^2");
        subtract_helper("x^2+1/2*x*y+y^2", "x^2-7/4*x+1/3", "1/2*x*y+y^2+7/4*x-1/3");
        subtract_helper("x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2", "0");
        subtract_helper("x^2+1/2*x*y+y^2", "a+b+c+d+e+f", "x^2+1/2*x*y+y^2-a-b-c-d-e-f");
        subtract_helper("x^2+1/2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "-x*y^2*z-x^2*z^2-x^3+x^2+1/2*x*y+y^2-22/7*z^2");

        subtract_helper("a+b+c+d+e+f", "0", "a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f-1");
        subtract_helper("a+b+c+d+e+f", "-4/3", "a+b+c+d+e+f+4/3");
        subtract_helper("a+b+c+d+e+f", "ooo", "a+b+c+d+e+f-ooo");
        subtract_helper("a+b+c+d+e+f", "a*b*c", "-a*b*c+a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "x^2-7/4*x+1/3", "-x^2+a+b+c+d+e+f+7/4*x-1/3");
        subtract_helper("a+b+c+d+e+f", "x^2+1/2*x*y+y^2", "-x^2-1/2*x*y-y^2+a+b+c+d+e+f");
        subtract_helper("a+b+c+d+e+f", "a+b+c+d+e+f", "0");
        subtract_helper("a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-x*y^2*z-x^2*z^2-x^3-22/7*z^2+a+b+c+d+e+f");

        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2-1");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-4/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2+4/3");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "ooo", "x*y^2*z+x^2*z^2+x^3+22/7*z^2-ooo");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a*b*c", "x*y^2*z+x^2*z^2-a*b*c+x^3+22/7*z^2");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x^2-7/4*x+1/3", "x*y^2*z+x^2*z^2+x^3-x^2+22/7*z^2+7/4*x-1/3");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x^2+1/2*x*y+y^2",
                "x*y^2*z+x^2*z^2+x^3-x^2-1/2*x*y-y^2+22/7*z^2");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+22/7*z^2-a-b-c-d-e-f");
        subtract_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0");

        subtract_helper("a*b*c", "a*b*c-1", "1");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().multiply(b);
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

        multiply_int_helper("-4/3", 0, "0");
        multiply_int_helper("-4/3", 1, "-4/3");
        multiply_int_helper("-4/3", -3, "4");
        multiply_int_helper("-4/3", 4, "-16/3");

        multiply_int_helper("ooo", 0, "0");
        multiply_int_helper("ooo", 1, "ooo");
        multiply_int_helper("ooo", -3, "-3*ooo");
        multiply_int_helper("ooo", 4, "4*ooo");

        multiply_int_helper("a*b*c", 0, "0");
        multiply_int_helper("a*b*c", 1, "a*b*c");
        multiply_int_helper("a*b*c", -3, "-3*a*b*c");
        multiply_int_helper("a*b*c", 4, "4*a*b*c");

        multiply_int_helper("x^2-7/4*x+1/3", 0, "0");
        multiply_int_helper("x^2-7/4*x+1/3", 1, "x^2-7/4*x+1/3");
        multiply_int_helper("x^2-7/4*x+1/3", -3, "-3*x^2+21/4*x-1");
        multiply_int_helper("x^2-7/4*x+1/3", 4, "4*x^2-7*x+4/3");

        multiply_int_helper("x^2+1/2*x*y+y^2", 0, "0");
        multiply_int_helper("x^2+1/2*x*y+y^2", 1, "x^2+1/2*x*y+y^2");
        multiply_int_helper("x^2+1/2*x*y+y^2", -3, "-3*x^2-3/2*x*y-3*y^2");
        multiply_int_helper("x^2+1/2*x*y+y^2", 4, "4*x^2+2*x*y+4*y^2");

        multiply_int_helper("a+b+c+d+e+f", 0, "0");
        multiply_int_helper("a+b+c+d+e+f", 1, "a+b+c+d+e+f");
        multiply_int_helper("a+b+c+d+e+f", -3, "-3*a-3*b-3*c-3*d-3*e-3*f");
        multiply_int_helper("a+b+c+d+e+f", 4, "4*a+4*b+4*c+4*d+4*e+4*f");

        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 0, "0");
        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 1, "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -3, "-3*x*y^2*z-3*x^2*z^2-3*x^3-66/7*z^2");
        multiply_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 4, "4*x*y^2*z+4*x^2*z^2+4*x^3+88/7*z^2");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().multiply(Readers.readBigIntegerStrict(b).get());
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

        multiply_BigInteger_helper("-4/3", "0", "0");
        multiply_BigInteger_helper("-4/3", "1", "-4/3");
        multiply_BigInteger_helper("-4/3", "-3", "4");
        multiply_BigInteger_helper("-4/3", "4", "-16/3");

        multiply_BigInteger_helper("ooo", "0", "0");
        multiply_BigInteger_helper("ooo", "1", "ooo");
        multiply_BigInteger_helper("ooo", "-3", "-3*ooo");
        multiply_BigInteger_helper("ooo", "4", "4*ooo");

        multiply_BigInteger_helper("a*b*c", "0", "0");
        multiply_BigInteger_helper("a*b*c", "1", "a*b*c");
        multiply_BigInteger_helper("a*b*c", "-3", "-3*a*b*c");
        multiply_BigInteger_helper("a*b*c", "4", "4*a*b*c");

        multiply_BigInteger_helper("x^2-7/4*x+1/3", "0", "0");
        multiply_BigInteger_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        multiply_BigInteger_helper("x^2-7/4*x+1/3", "-3", "-3*x^2+21/4*x-1");
        multiply_BigInteger_helper("x^2-7/4*x+1/3", "4", "4*x^2-7*x+4/3");

        multiply_BigInteger_helper("x^2+1/2*x*y+y^2", "0", "0");
        multiply_BigInteger_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2");
        multiply_BigInteger_helper("x^2+1/2*x*y+y^2", "-3", "-3*x^2-3/2*x*y-3*y^2");
        multiply_BigInteger_helper("x^2+1/2*x*y+y^2", "4", "4*x^2+2*x*y+4*y^2");

        multiply_BigInteger_helper("a+b+c+d+e+f", "0", "0");
        multiply_BigInteger_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        multiply_BigInteger_helper("a+b+c+d+e+f", "-3", "-3*a-3*b-3*c-3*d-3*e-3*f");
        multiply_BigInteger_helper("a+b+c+d+e+f", "4", "4*a+4*b+4*c+4*d+4*e+4*f");

        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0", "0");
        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-3", "-3*x*y^2*z-3*x^2*z^2-3*x^3-66/7*z^2");
        multiply_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "4", "4*x*y^2*z+4*x^2*z^2+4*x^3+88/7*z^2");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().multiply(Rational.readStrict(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("0", "0", "0");
        multiply_Rational_helper("0", "1", "0");
        multiply_Rational_helper("0", "-3", "0");
        multiply_Rational_helper("0", "4/5", "0");

        multiply_Rational_helper("1", "0", "0");
        multiply_Rational_helper("1", "1", "1");
        multiply_Rational_helper("1", "-3", "-3");
        multiply_Rational_helper("1", "4/5", "4/5");

        multiply_Rational_helper("-4/3", "0", "0");
        multiply_Rational_helper("-4/3", "1", "-4/3");
        multiply_Rational_helper("-4/3", "-3", "4");
        multiply_Rational_helper("-4/3", "4/5", "-16/15");

        multiply_Rational_helper("ooo", "0", "0");
        multiply_Rational_helper("ooo", "1", "ooo");
        multiply_Rational_helper("ooo", "-3", "-3*ooo");
        multiply_Rational_helper("ooo", "4/5", "4/5*ooo");

        multiply_Rational_helper("a*b*c", "0", "0");
        multiply_Rational_helper("a*b*c", "1", "a*b*c");
        multiply_Rational_helper("a*b*c", "-3", "-3*a*b*c");
        multiply_Rational_helper("a*b*c", "4/5", "4/5*a*b*c");

        multiply_Rational_helper("x^2-7/4*x+1/3", "0", "0");
        multiply_Rational_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        multiply_Rational_helper("x^2-7/4*x+1/3", "-3", "-3*x^2+21/4*x-1");
        multiply_Rational_helper("x^2-7/4*x+1/3", "4/5", "4/5*x^2-7/5*x+4/15");

        multiply_Rational_helper("x^2+1/2*x*y+y^2", "0", "0");
        multiply_Rational_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2");
        multiply_Rational_helper("x^2+1/2*x*y+y^2", "-3", "-3*x^2-3/2*x*y-3*y^2");
        multiply_Rational_helper("x^2+1/2*x*y+y^2", "4/5", "4/5*x^2+2/5*x*y+4/5*y^2");

        multiply_Rational_helper("a+b+c+d+e+f", "0", "0");
        multiply_Rational_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        multiply_Rational_helper("a+b+c+d+e+f", "-3", "-3*a-3*b-3*c-3*d-3*e-3*f");
        multiply_Rational_helper("a+b+c+d+e+f", "4/5", "4/5*a+4/5*b+4/5*c+4/5*d+4/5*e+4/5*f");

        multiply_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0", "0");
        multiply_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        multiply_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-3", "-3*x*y^2*z-3*x^2*z^2-3*x^3-66/7*z^2");
        multiply_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "4/5", "4/5*x*y^2*z+4/5*x^2*z^2+4/5*x^3+88/35*z^2");
    }

    private static void multiply_Monomial_Rational_helper(
            @NotNull String p,
            @NotNull String ev,
            @NotNull String c,
            @NotNull String output
    ) {
        RationalMultivariatePolynomial q = readStrict(p).get()
                .multiply(Monomial.readStrict(ev).get(), Rational.readStrict(c).get());
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testMultiply_Monomial_Rational() {
        multiply_Monomial_Rational_helper("0", "1", "0", "0");
        multiply_Monomial_Rational_helper("0", "1", "1", "0");
        multiply_Monomial_Rational_helper("0", "ooo", "2", "0");
        multiply_Monomial_Rational_helper("0", "a*b*c", "-2/3", "0");

        multiply_Monomial_Rational_helper("1", "1", "0", "0");
        multiply_Monomial_Rational_helper("1", "1", "1", "1");
        multiply_Monomial_Rational_helper("1", "ooo", "2", "2*ooo");
        multiply_Monomial_Rational_helper("1", "a*b*c", "-2/3", "-2/3*a*b*c");

        multiply_Monomial_Rational_helper("-4/3", "1", "0", "0");
        multiply_Monomial_Rational_helper("-4/3", "1", "1", "-4/3");
        multiply_Monomial_Rational_helper("-4/3", "ooo", "2", "-8/3*ooo");
        multiply_Monomial_Rational_helper("-4/3", "a*b*c", "-2/3", "8/9*a*b*c");

        multiply_Monomial_Rational_helper("ooo", "1", "0", "0");
        multiply_Monomial_Rational_helper("ooo", "1", "1", "ooo");
        multiply_Monomial_Rational_helper("ooo", "ooo", "2", "2*ooo^2");
        multiply_Monomial_Rational_helper("ooo", "a*b*c", "-2/3", "-2/3*a*b*c*ooo");

        multiply_Monomial_Rational_helper("a*b*c", "1", "0", "0");
        multiply_Monomial_Rational_helper("a*b*c", "1", "1", "a*b*c");
        multiply_Monomial_Rational_helper("a*b*c", "ooo", "2", "2*a*b*c*ooo");
        multiply_Monomial_Rational_helper("a*b*c", "a*b*c", "-2/3", "-2/3*a^2*b^2*c^2");

        multiply_Monomial_Rational_helper("x^2-7/4*x+1/3", "1", "0", "0");
        multiply_Monomial_Rational_helper("x^2-7/4*x+1/3", "1", "1", "x^2-7/4*x+1/3");
        multiply_Monomial_Rational_helper("x^2-7/4*x+1/3", "ooo", "2", "2*x^2*ooo-7/2*x*ooo+2/3*ooo");
        multiply_Monomial_Rational_helper("x^2-7/4*x+1/3", "a*b*c", "-2/3", "-2/3*a*b*c*x^2+7/6*a*b*c*x-2/9*a*b*c");

        multiply_Monomial_Rational_helper("x^2+1/2*x*y+y^2", "1", "0", "0");
        multiply_Monomial_Rational_helper("x^2+1/2*x*y+y^2", "1", "1", "x^2+1/2*x*y+y^2");
        multiply_Monomial_Rational_helper("x^2+1/2*x*y+y^2", "ooo", "2", "2*x^2*ooo+x*y*ooo+2*y^2*ooo");
        multiply_Monomial_Rational_helper("x^2+1/2*x*y+y^2", "a*b*c", "-2/3",
                "-2/3*a*b*c*x^2-1/3*a*b*c*x*y-2/3*a*b*c*y^2");

        multiply_Monomial_Rational_helper("a+b+c+d+e+f", "1", "0", "0");
        multiply_Monomial_Rational_helper("a+b+c+d+e+f", "1", "1", "a+b+c+d+e+f");
        multiply_Monomial_Rational_helper("a+b+c+d+e+f", "ooo", "2",
                "2*a*ooo+2*b*ooo+2*c*ooo+2*d*ooo+2*e*ooo+2*f*ooo");
        multiply_Monomial_Rational_helper("a+b+c+d+e+f", "a*b*c", "-2/3",
                "-2/3*a^2*b*c-2/3*a*b^2*c-2/3*a*b*c^2-2/3*a*b*c*d-2/3*a*b*c*e-2/3*a*b*c*f");

        multiply_Monomial_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "0", "0");
        multiply_Monomial_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        multiply_Monomial_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "ooo", "2",
                "2*x*y^2*z*ooo+2*x^2*z^2*ooo+2*x^3*ooo+44/7*z^2*ooo");
        multiply_Monomial_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a*b*c", "-2/3",
                "-2/3*a*b*c*x*y^2*z-2/3*a*b*c*x^2*z^2-2/3*a*b*c*x^3-44/21*a*b*c*z^2");
    }

    private static void multiply_RationalMultivariatePolynomial_helper(
            @NotNull String a,
            @NotNull String b,
            @NotNull String output
    ) {
        RationalMultivariatePolynomial p = readStrict(a).get().multiply(readStrict(b).get());
        p.validate();
        aeq(p, output);
    }

    @Test
    public void testMultiply_MultivariatePolynomial() {
        multiply_RationalMultivariatePolynomial_helper("0", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "1", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "-4/3", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "ooo", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "a*b*c", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "x^2-7/4*x+1/3", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "x^2+1/2*x*y+y^2", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "a+b+c+d+e+f", "0");
        multiply_RationalMultivariatePolynomial_helper("0", "x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0");

        multiply_RationalMultivariatePolynomial_helper("1", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("1", "1", "1");
        multiply_RationalMultivariatePolynomial_helper("1", "-4/3", "-4/3");
        multiply_RationalMultivariatePolynomial_helper("1", "ooo", "ooo");
        multiply_RationalMultivariatePolynomial_helper("1", "a*b*c", "a*b*c");
        multiply_RationalMultivariatePolynomial_helper("1", "x^2-7/4*x+1/3", "x^2-7/4*x+1/3");
        multiply_RationalMultivariatePolynomial_helper("1", "x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2");
        multiply_RationalMultivariatePolynomial_helper("1", "a+b+c+d+e+f", "a+b+c+d+e+f");
        multiply_RationalMultivariatePolynomial_helper("1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "x*y^2*z+x^2*z^2+x^3+22/7*z^2");

        multiply_RationalMultivariatePolynomial_helper("-4/3", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "1", "-4/3");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "-4/3", "16/9");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "ooo", "-4/3*ooo");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "a*b*c", "-4/3*a*b*c");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "x^2-7/4*x+1/3", "-4/3*x^2+7/3*x-4/9");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "x^2+1/2*x*y+y^2", "-4/3*x^2-2/3*x*y-4/3*y^2");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "a+b+c+d+e+f", "-4/3*a-4/3*b-4/3*c-4/3*d-4/3*e-4/3*f");
        multiply_RationalMultivariatePolynomial_helper("-4/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "-4/3*x*y^2*z-4/3*x^2*z^2-4/3*x^3-88/21*z^2");

        multiply_RationalMultivariatePolynomial_helper("ooo", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("ooo", "1", "ooo");
        multiply_RationalMultivariatePolynomial_helper("ooo", "-4/3", "-4/3*ooo");
        multiply_RationalMultivariatePolynomial_helper("ooo", "ooo", "ooo^2");
        multiply_RationalMultivariatePolynomial_helper("ooo", "a*b*c", "a*b*c*ooo");
        multiply_RationalMultivariatePolynomial_helper("ooo", "x^2-7/4*x+1/3", "x^2*ooo-7/4*x*ooo+1/3*ooo");
        multiply_RationalMultivariatePolynomial_helper("ooo", "x^2+1/2*x*y+y^2", "x^2*ooo+1/2*x*y*ooo+y^2*ooo");
        multiply_RationalMultivariatePolynomial_helper("ooo", "a+b+c+d+e+f", "a*ooo+b*ooo+c*ooo+d*ooo+e*ooo+f*ooo");
        multiply_RationalMultivariatePolynomial_helper("ooo", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "x*y^2*z*ooo+x^2*z^2*ooo+x^3*ooo+22/7*z^2*ooo");

        multiply_RationalMultivariatePolynomial_helper("a*b*c", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "1", "a*b*c");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "-4/3", "-4/3*a*b*c");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "ooo", "a*b*c*ooo");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "a*b*c", "a^2*b^2*c^2");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "x^2-7/4*x+1/3", "a*b*c*x^2-7/4*a*b*c*x+1/3*a*b*c");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "x^2+1/2*x*y+y^2",
                "a*b*c*x^2+1/2*a*b*c*x*y+a*b*c*y^2");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "a+b+c+d+e+f",
                "a^2*b*c+a*b^2*c+a*b*c^2+a*b*c*d+a*b*c*e+a*b*c*f");
        multiply_RationalMultivariatePolynomial_helper("a*b*c", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "a*b*c*x*y^2*z+a*b*c*x^2*z^2+a*b*c*x^3+22/7*a*b*c*z^2");

        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "-4/3", "-4/3*x^2+7/3*x-4/9");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "ooo", "x^2*ooo-7/4*x*ooo+1/3*ooo");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "a*b*c", "a*b*c*x^2-7/4*a*b*c*x+1/3*a*b*c");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "x^2-7/4*x+1/3",
                "x^4-7/2*x^3+179/48*x^2-7/6*x+1/9");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "x^2+1/2*x*y+y^2",
                "x^4+1/2*x^3*y+x^2*y^2-7/4*x^3-7/8*x^2*y-7/4*x*y^2+1/3*x^2+1/6*x*y+1/3*y^2");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "a+b+c+d+e+f",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2-7/4*a*x-7/4*b*x-7/4*c*x-7/4*d*x-7/4*e*x-7/4*f*x+1/3*a+1/3*b+" +
                "1/3*c+1/3*d+1/3*e+1/3*f");
        multiply_RationalMultivariatePolynomial_helper("x^2-7/4*x+1/3", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "x^3*y^2*z+x^4*z^2+x^5-7/4*x^2*y^2*z-7/4*x^3*z^2-7/4*x^4+1/3*x*y^2*z+73/21*x^2*z^2+1/3*x^3-" +
                "11/2*x*z^2+22/21*z^2");

        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "-4/3", "-4/3*x^2-2/3*x*y-4/3*y^2");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "ooo", "x^2*ooo+1/2*x*y*ooo+y^2*ooo");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "a*b*c",
                "a*b*c*x^2+1/2*a*b*c*x*y+a*b*c*y^2");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "x^2-7/4*x+1/3",
                "x^4+1/2*x^3*y+x^2*y^2-7/4*x^3-7/8*x^2*y-7/4*x*y^2+1/3*x^2+1/6*x*y+1/3*y^2");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "x^2+1/2*x*y+y^2",
                "x^4+x^3*y+9/4*x^2*y^2+x*y^3+y^4");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "a+b+c+d+e+f",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2+1/2*a*x*y+1/2*b*x*y+1/2*c*x*y+1/2*d*x*y+1/2*e*x*y+1/2*f*x*y+" +
                "a*y^2+b*y^2+c*y^2+d*y^2+e*y^2+f*y^2");
        multiply_RationalMultivariatePolynomial_helper("x^2+1/2*x*y+y^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "x^3*y^2*z+1/2*x^2*y^3*z+x*y^4*z+x^4*z^2+1/2*x^3*y*z^2+x^2*y^2*z^2+x^5+1/2*x^4*y+x^3*y^2+" +
                "22/7*x^2*z^2+11/7*x*y*z^2+22/7*y^2*z^2");

        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "-4/3", "-4/3*a-4/3*b-4/3*c-4/3*d-4/3*e-4/3*f");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "ooo", "a*ooo+b*ooo+c*ooo+d*ooo+e*ooo+f*ooo");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "a*b*c",
                "a^2*b*c+a*b^2*c+a*b*c^2+a*b*c*d+a*b*c*e+a*b*c*f");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "x^2-7/4*x+1/3",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2-7/4*a*x-7/4*b*x-7/4*c*x-7/4*d*x-7/4*e*x-7/4*f*x+1/3*a+1/3*b+" +
                "1/3*c+1/3*d+1/3*e+1/3*f");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "x^2+1/2*x*y+y^2",
                "a*x^2+b*x^2+c*x^2+d*x^2+e*x^2+f*x^2+1/2*a*x*y+1/2*b*x*y+1/2*c*x*y+1/2*d*x*y+1/2*e*x*y+1/2*f*x*y+" +
                "a*y^2+b*y^2+c*y^2+d*y^2+e*y^2+f*y^2");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "a+b+c+d+e+f",
                "a^2+2*a*b+b^2+2*a*c+2*b*c+c^2+2*a*d+2*b*d+2*c*d+d^2+2*a*e+2*b*e+2*c*e+2*d*e+e^2+2*a*f+2*b*f+2*c*f+" +
                "2*d*f+2*e*f+f^2");
        multiply_RationalMultivariatePolynomial_helper("a+b+c+d+e+f", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "a*x*y^2*z+b*x*y^2*z+c*x*y^2*z+d*x*y^2*z+e*x*y^2*z+f*x*y^2*z+a*x^2*z^2+b*x^2*z^2+c*x^2*z^2+" +
                "d*x^2*z^2+e*x^2*z^2+f*x^2*z^2+a*x^3+b*x^3+c*x^3+d*x^3+e*x^3+f*x^3+22/7*a*z^2+22/7*b*z^2+22/7*c*z^2+" +
                "22/7*d*z^2+22/7*e*z^2+22/7*f*z^2");

        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "0", "0");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1",
                "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-4/3",
                "-4/3*x*y^2*z-4/3*x^2*z^2-4/3*x^3-88/21*z^2");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "ooo",
                "x*y^2*z*ooo+x^2*z^2*ooo+x^3*ooo+22/7*z^2*ooo");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a*b*c",
                "a*b*c*x*y^2*z+a*b*c*x^2*z^2+a*b*c*x^3+22/7*a*b*c*z^2");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x^2-7/4*x+1/3",
                "x^3*y^2*z+x^4*z^2+x^5-7/4*x^2*y^2*z-7/4*x^3*z^2-7/4*x^4+1/3*x*y^2*z+73/21*x^2*z^2+1/3*x^3-" +
                "11/2*x*z^2+22/21*z^2");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x^2+1/2*x*y+y^2",
                "x^3*y^2*z+1/2*x^2*y^3*z+x*y^4*z+x^4*z^2+1/2*x^3*y*z^2+x^2*y^2*z^2+x^5+1/2*x^4*y+x^3*y^2+" +
                "22/7*x^2*z^2+11/7*x*y*z^2+22/7*y^2*z^2");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "a+b+c+d+e+f",
                "a*x*y^2*z+b*x*y^2*z+c*x*y^2*z+d*x*y^2*z+e*x*y^2*z+f*x*y^2*z+a*x^2*z^2+b*x^2*z^2+c*x^2*z^2+" +
                "d*x^2*z^2+e*x^2*z^2+f*x^2*z^2+a*x^3+b*x^3+c*x^3+d*x^3+e*x^3+f*x^3+22/7*a*z^2+22/7*b*z^2+22/7*c*z^2+" +
                "22/7*d*z^2+22/7*e*z^2+22/7*f*z^2");
        multiply_RationalMultivariatePolynomial_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "x*y^2*z+x^2*z^2+x^3+22/7*z^2",
                "x^2*y^4*z^2+2*x^3*y^2*z^3+x^4*z^4+2*x^4*y^2*z+2*x^5*z^2+x^6+44/7*x*y^2*z^3+44/7*x^2*z^4+" +
                "44/7*x^3*z^2+484/49*z^4");
    }

    private static void divide_int_helper(@NotNull String a, int b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().divide(b);
        p.validate();
        aeq(p, output);
    }

    private static void divide_int_fail_helper(@NotNull String a, int b) {
        try {
            readStrict(a).get().divide(b);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        divide_int_helper("0", 1, "0");
        divide_int_helper("0", -3, "0");
        divide_int_helper("0", 4, "0");

        divide_int_helper("1", 1, "1");
        divide_int_helper("1", -3, "-1/3");
        divide_int_helper("1", 4, "1/4");

        divide_int_helper("-4/3", 1, "-4/3");
        divide_int_helper("-4/3", -3, "4/9");
        divide_int_helper("-4/3", 4, "-1/3");

        divide_int_helper("ooo", 1, "ooo");
        divide_int_helper("ooo", -3, "-1/3*ooo");
        divide_int_helper("ooo", 4, "1/4*ooo");

        divide_int_helper("a*b*c", 1, "a*b*c");
        divide_int_helper("a*b*c", -3, "-1/3*a*b*c");
        divide_int_helper("a*b*c", 4, "1/4*a*b*c");

        divide_int_helper("x^2-7/4*x+1/3", 1, "x^2-7/4*x+1/3");
        divide_int_helper("x^2-7/4*x+1/3", -3, "-1/3*x^2+7/12*x-1/9");
        divide_int_helper("x^2-7/4*x+1/3", 4, "1/4*x^2-7/16*x+1/12");

        divide_int_helper("x^2+1/2*x*y+y^2", 1, "x^2+1/2*x*y+y^2");
        divide_int_helper("x^2+1/2*x*y+y^2", -3, "-1/3*x^2-1/6*x*y-1/3*y^2");
        divide_int_helper("x^2+1/2*x*y+y^2", 4, "1/4*x^2+1/8*x*y+1/4*y^2");

        divide_int_helper("a+b+c+d+e+f", 1, "a+b+c+d+e+f");
        divide_int_helper("a+b+c+d+e+f", -3, "-1/3*a-1/3*b-1/3*c-1/3*d-1/3*e-1/3*f");
        divide_int_helper("a+b+c+d+e+f", 4, "1/4*a+1/4*b+1/4*c+1/4*d+1/4*e+1/4*f");

        divide_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 1, "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        divide_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -3, "-1/3*x*y^2*z-1/3*x^2*z^2-1/3*x^3-22/21*z^2");
        divide_int_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 4, "1/4*x*y^2*z+1/4*x^2*z^2+1/4*x^3+11/14*z^2");

        divide_int_fail_helper("0", 0);
        divide_int_fail_helper("1/3*ooo", 0);
    }

    private static void divide_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
        p.validate();
        aeq(p, output);
    }

    private static void divide_BigInteger_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Readers.readBigIntegerStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        divide_BigInteger_helper("0", "1", "0");
        divide_BigInteger_helper("0", "-3", "0");
        divide_BigInteger_helper("0", "4", "0");

        divide_BigInteger_helper("1", "1", "1");
        divide_BigInteger_helper("1", "-3", "-1/3");
        divide_BigInteger_helper("1", "4", "1/4");

        divide_BigInteger_helper("-4/3", "1", "-4/3");
        divide_BigInteger_helper("-4/3", "-3", "4/9");
        divide_BigInteger_helper("-4/3", "4", "-1/3");

        divide_BigInteger_helper("ooo", "1", "ooo");
        divide_BigInteger_helper("ooo", "-3", "-1/3*ooo");
        divide_BigInteger_helper("ooo", "4", "1/4*ooo");

        divide_BigInteger_helper("a*b*c", "1", "a*b*c");
        divide_BigInteger_helper("a*b*c", "-3", "-1/3*a*b*c");
        divide_BigInteger_helper("a*b*c", "4", "1/4*a*b*c");

        divide_BigInteger_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        divide_BigInteger_helper("x^2-7/4*x+1/3", "-3", "-1/3*x^2+7/12*x-1/9");
        divide_BigInteger_helper("x^2-7/4*x+1/3", "4", "1/4*x^2-7/16*x+1/12");

        divide_BigInteger_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2");
        divide_BigInteger_helper("x^2+1/2*x*y+y^2", "-3", "-1/3*x^2-1/6*x*y-1/3*y^2");
        divide_BigInteger_helper("x^2+1/2*x*y+y^2", "4", "1/4*x^2+1/8*x*y+1/4*y^2");

        divide_BigInteger_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        divide_BigInteger_helper("a+b+c+d+e+f", "-3", "-1/3*a-1/3*b-1/3*c-1/3*d-1/3*e-1/3*f");
        divide_BigInteger_helper("a+b+c+d+e+f", "4", "1/4*a+1/4*b+1/4*c+1/4*d+1/4*e+1/4*f");

        divide_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        divide_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-3", "-1/3*x*y^2*z-1/3*x^2*z^2-1/3*x^3-22/21*z^2");
        divide_BigInteger_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "4", "1/4*x*y^2*z+1/4*x^2*z^2+1/4*x^3+11/14*z^2");

        divide_BigInteger_fail_helper("0", "0");
        divide_BigInteger_fail_helper("1/3*ooo", "0");
    }

    private static void divide_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        RationalMultivariatePolynomial p = readStrict(a).get().divide(Rational.readStrict(b).get());
        p.validate();
        aeq(p, output);
    }

    private static void divide_Rational_fail_helper(@NotNull String a, @NotNull String b) {
        try {
            readStrict(a).get().divide(Rational.readStrict(b).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        divide_Rational_helper("0", "1", "0");
        divide_Rational_helper("0", "-3", "0");
        divide_Rational_helper("0", "4/5", "0");

        divide_Rational_helper("1", "1", "1");
        divide_Rational_helper("1", "-3", "-1/3");
        divide_Rational_helper("1", "4/5", "5/4");

        divide_Rational_helper("-4/3", "1", "-4/3");
        divide_Rational_helper("-4/3", "-3", "4/9");
        divide_Rational_helper("-4/3", "4/5", "-5/3");

        divide_Rational_helper("ooo", "1", "ooo");
        divide_Rational_helper("ooo", "-3", "-1/3*ooo");
        divide_Rational_helper("ooo", "4/5", "5/4*ooo");

        divide_Rational_helper("a*b*c", "1", "a*b*c");
        divide_Rational_helper("a*b*c", "-3", "-1/3*a*b*c");
        divide_Rational_helper("a*b*c", "4/5", "5/4*a*b*c");

        divide_Rational_helper("x^2-7/4*x+1/3", "1", "x^2-7/4*x+1/3");
        divide_Rational_helper("x^2-7/4*x+1/3", "-3", "-1/3*x^2+7/12*x-1/9");
        divide_Rational_helper("x^2-7/4*x+1/3", "4/5", "5/4*x^2-35/16*x+5/12");

        divide_Rational_helper("x^2+1/2*x*y+y^2", "1", "x^2+1/2*x*y+y^2");
        divide_Rational_helper("x^2+1/2*x*y+y^2", "-3", "-1/3*x^2-1/6*x*y-1/3*y^2");
        divide_Rational_helper("x^2+1/2*x*y+y^2", "4/5", "5/4*x^2+5/8*x*y+5/4*y^2");

        divide_Rational_helper("a+b+c+d+e+f", "1", "a+b+c+d+e+f");
        divide_Rational_helper("a+b+c+d+e+f", "-3", "-1/3*a-1/3*b-1/3*c-1/3*d-1/3*e-1/3*f");
        divide_Rational_helper("a+b+c+d+e+f", "4/5", "5/4*a+5/4*b+5/4*c+5/4*d+5/4*e+5/4*f");

        divide_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "1", "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        divide_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "-3", "-1/3*x*y^2*z-1/3*x^2*z^2-1/3*x^3-22/21*z^2");
        divide_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "4/5", "5/4*x*y^2*z+5/4*x^2*z^2+5/4*x^3+55/14*z^2");

        divide_Rational_fail_helper("0", "0");
        divide_Rational_fail_helper("1/3*ooo", "0");
    }

    private static void shiftLeft_helper(@NotNull String p, int bits, @NotNull String output) {
        RationalMultivariatePolynomial q = readStrict(p).get().shiftLeft(bits);
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testShiftLeft() {
        shiftLeft_helper("0", 0, "0");
        shiftLeft_helper("0", 1, "0");
        shiftLeft_helper("0", 2, "0");
        shiftLeft_helper("0", 3, "0");
        shiftLeft_helper("0", 4, "0");
        shiftLeft_helper("0", -1, "0");
        shiftLeft_helper("0", -2, "0");
        shiftLeft_helper("0", -3, "0");
        shiftLeft_helper("0", -4, "0");

        shiftLeft_helper("1", 0, "1");
        shiftLeft_helper("1", 1, "2");
        shiftLeft_helper("1", 2, "4");
        shiftLeft_helper("1", 3, "8");
        shiftLeft_helper("1", 4, "16");
        shiftLeft_helper("1", -1, "1/2");
        shiftLeft_helper("1", -2, "1/4");
        shiftLeft_helper("1", -3, "1/8");
        shiftLeft_helper("1", -4, "1/16");

        shiftLeft_helper("-4/3", 0, "-4/3");
        shiftLeft_helper("-4/3", 1, "-8/3");
        shiftLeft_helper("-4/3", 2, "-16/3");
        shiftLeft_helper("-4/3", 3, "-32/3");
        shiftLeft_helper("-4/3", 4, "-64/3");
        shiftLeft_helper("-4/3", -1, "-2/3");
        shiftLeft_helper("-4/3", -2, "-1/3");
        shiftLeft_helper("-4/3", -3, "-1/6");
        shiftLeft_helper("-4/3", -4, "-1/12");

        shiftLeft_helper("ooo", 0, "ooo");
        shiftLeft_helper("ooo", 1, "2*ooo");
        shiftLeft_helper("ooo", 2, "4*ooo");
        shiftLeft_helper("ooo", 3, "8*ooo");
        shiftLeft_helper("ooo", 4, "16*ooo");
        shiftLeft_helper("ooo", -1, "1/2*ooo");
        shiftLeft_helper("ooo", -2, "1/4*ooo");
        shiftLeft_helper("ooo", -3, "1/8*ooo");
        shiftLeft_helper("ooo", -4, "1/16*ooo");

        shiftLeft_helper("a*b*c", 0, "a*b*c");
        shiftLeft_helper("a*b*c", 1, "2*a*b*c");
        shiftLeft_helper("a*b*c", 2, "4*a*b*c");
        shiftLeft_helper("a*b*c", 3, "8*a*b*c");
        shiftLeft_helper("a*b*c", 4, "16*a*b*c");
        shiftLeft_helper("a*b*c", -1, "1/2*a*b*c");
        shiftLeft_helper("a*b*c", -2, "1/4*a*b*c");
        shiftLeft_helper("a*b*c", -3, "1/8*a*b*c");
        shiftLeft_helper("a*b*c", -4, "1/16*a*b*c");

        shiftLeft_helper("x^2-7/4*x+1/3", 0, "x^2-7/4*x+1/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 1, "2*x^2-7/2*x+2/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 2, "4*x^2-7*x+4/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 3, "8*x^2-14*x+8/3");
        shiftLeft_helper("x^2-7/4*x+1/3", 4, "16*x^2-28*x+16/3");
        shiftLeft_helper("x^2-7/4*x+1/3", -1, "1/2*x^2-7/8*x+1/6");
        shiftLeft_helper("x^2-7/4*x+1/3", -2, "1/4*x^2-7/16*x+1/12");
        shiftLeft_helper("x^2-7/4*x+1/3", -3, "1/8*x^2-7/32*x+1/24");
        shiftLeft_helper("x^2-7/4*x+1/3", -4, "1/16*x^2-7/64*x+1/48");

        shiftLeft_helper("x^2+1/2*x*y+y^2", 0, "x^2+1/2*x*y+y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", 1, "2*x^2+x*y+2*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", 2, "4*x^2+2*x*y+4*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", 3, "8*x^2+4*x*y+8*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", 4, "16*x^2+8*x*y+16*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", -1, "1/2*x^2+1/4*x*y+1/2*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", -2, "1/4*x^2+1/8*x*y+1/4*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", -3, "1/8*x^2+1/16*x*y+1/8*y^2");
        shiftLeft_helper("x^2+1/2*x*y+y^2", -4, "1/16*x^2+1/32*x*y+1/16*y^2");

        shiftLeft_helper("a+b+c+d+e+f", 0, "a+b+c+d+e+f");
        shiftLeft_helper("a+b+c+d+e+f", 1, "2*a+2*b+2*c+2*d+2*e+2*f");
        shiftLeft_helper("a+b+c+d+e+f", 2, "4*a+4*b+4*c+4*d+4*e+4*f");
        shiftLeft_helper("a+b+c+d+e+f", 3, "8*a+8*b+8*c+8*d+8*e+8*f");
        shiftLeft_helper("a+b+c+d+e+f", 4, "16*a+16*b+16*c+16*d+16*e+16*f");
        shiftLeft_helper("a+b+c+d+e+f", -1, "1/2*a+1/2*b+1/2*c+1/2*d+1/2*e+1/2*f");
        shiftLeft_helper("a+b+c+d+e+f", -2, "1/4*a+1/4*b+1/4*c+1/4*d+1/4*e+1/4*f");
        shiftLeft_helper("a+b+c+d+e+f", -3, "1/8*a+1/8*b+1/8*c+1/8*d+1/8*e+1/8*f");
        shiftLeft_helper("a+b+c+d+e+f", -4, "1/16*a+1/16*b+1/16*c+1/16*d+1/16*e+1/16*f");

        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 0, "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 1, "2*x*y^2*z+2*x^2*z^2+2*x^3+44/7*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 2, "4*x*y^2*z+4*x^2*z^2+4*x^3+88/7*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 3, "8*x*y^2*z+8*x^2*z^2+8*x^3+176/7*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 4, "16*x*y^2*z+16*x^2*z^2+16*x^3+352/7*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -1, "1/2*x*y^2*z+1/2*x^2*z^2+1/2*x^3+11/7*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -2, "1/4*x*y^2*z+1/4*x^2*z^2+1/4*x^3+11/14*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -3, "1/8*x*y^2*z+1/8*x^2*z^2+1/8*x^3+11/28*z^2");
        shiftLeft_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -4, "1/16*x*y^2*z+1/16*x^2*z^2+1/16*x^3+11/56*z^2");
    }

    private static void shiftRight_helper(@NotNull String p, int bits, @NotNull String output) {
        RationalMultivariatePolynomial q = readStrict(p).get().shiftRight(bits);
        q.validate();
        aeq(q, output);
    }

    @Test
    public void testShiftRight() {
        shiftRight_helper("0", 0, "0");
        shiftRight_helper("0", 1, "0");
        shiftRight_helper("0", 2, "0");
        shiftRight_helper("0", 3, "0");
        shiftRight_helper("0", 4, "0");
        shiftRight_helper("0", -1, "0");
        shiftRight_helper("0", -2, "0");
        shiftRight_helper("0", -3, "0");
        shiftRight_helper("0", -4, "0");

        shiftRight_helper("1", 0, "1");
        shiftRight_helper("1", 1, "1/2");
        shiftRight_helper("1", 2, "1/4");
        shiftRight_helper("1", 3, "1/8");
        shiftRight_helper("1", 4, "1/16");
        shiftRight_helper("1", -1, "2");
        shiftRight_helper("1", -2, "4");
        shiftRight_helper("1", -3, "8");
        shiftRight_helper("1", -4, "16");

        shiftRight_helper("-4/3", 0, "-4/3");
        shiftRight_helper("-4/3", 1, "-2/3");
        shiftRight_helper("-4/3", 2, "-1/3");
        shiftRight_helper("-4/3", 3, "-1/6");
        shiftRight_helper("-4/3", 4, "-1/12");
        shiftRight_helper("-4/3", -1, "-8/3");
        shiftRight_helper("-4/3", -2, "-16/3");
        shiftRight_helper("-4/3", -3, "-32/3");
        shiftRight_helper("-4/3", -4, "-64/3");

        shiftRight_helper("ooo", 0, "ooo");
        shiftRight_helper("ooo", 1, "1/2*ooo");
        shiftRight_helper("ooo", 2, "1/4*ooo");
        shiftRight_helper("ooo", 3, "1/8*ooo");
        shiftRight_helper("ooo", 4, "1/16*ooo");
        shiftRight_helper("ooo", -1, "2*ooo");
        shiftRight_helper("ooo", -2, "4*ooo");
        shiftRight_helper("ooo", -3, "8*ooo");
        shiftRight_helper("ooo", -4, "16*ooo");

        shiftRight_helper("a*b*c", 0, "a*b*c");
        shiftRight_helper("a*b*c", 1, "1/2*a*b*c");
        shiftRight_helper("a*b*c", 2, "1/4*a*b*c");
        shiftRight_helper("a*b*c", 3, "1/8*a*b*c");
        shiftRight_helper("a*b*c", 4, "1/16*a*b*c");
        shiftRight_helper("a*b*c", -1, "2*a*b*c");
        shiftRight_helper("a*b*c", -2, "4*a*b*c");
        shiftRight_helper("a*b*c", -3, "8*a*b*c");
        shiftRight_helper("a*b*c", -4, "16*a*b*c");

        shiftRight_helper("x^2-7/4*x+1/3", 0, "x^2-7/4*x+1/3");
        shiftRight_helper("x^2-7/4*x+1/3", 1, "1/2*x^2-7/8*x+1/6");
        shiftRight_helper("x^2-7/4*x+1/3", 2, "1/4*x^2-7/16*x+1/12");
        shiftRight_helper("x^2-7/4*x+1/3", 3, "1/8*x^2-7/32*x+1/24");
        shiftRight_helper("x^2-7/4*x+1/3", 4, "1/16*x^2-7/64*x+1/48");
        shiftRight_helper("x^2-7/4*x+1/3", -1, "2*x^2-7/2*x+2/3");
        shiftRight_helper("x^2-7/4*x+1/3", -2, "4*x^2-7*x+4/3");
        shiftRight_helper("x^2-7/4*x+1/3", -3, "8*x^2-14*x+8/3");
        shiftRight_helper("x^2-7/4*x+1/3", -4, "16*x^2-28*x+16/3");

        shiftRight_helper("x^2+1/2*x*y+y^2", 0, "x^2+1/2*x*y+y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", 1, "1/2*x^2+1/4*x*y+1/2*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", 2, "1/4*x^2+1/8*x*y+1/4*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", 3, "1/8*x^2+1/16*x*y+1/8*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", 4, "1/16*x^2+1/32*x*y+1/16*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", -1, "2*x^2+x*y+2*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", -2, "4*x^2+2*x*y+4*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", -3, "8*x^2+4*x*y+8*y^2");
        shiftRight_helper("x^2+1/2*x*y+y^2", -4, "16*x^2+8*x*y+16*y^2");

        shiftRight_helper("a+b+c+d+e+f", 0, "a+b+c+d+e+f");
        shiftRight_helper("a+b+c+d+e+f", 1, "1/2*a+1/2*b+1/2*c+1/2*d+1/2*e+1/2*f");
        shiftRight_helper("a+b+c+d+e+f", 2, "1/4*a+1/4*b+1/4*c+1/4*d+1/4*e+1/4*f");
        shiftRight_helper("a+b+c+d+e+f", 3, "1/8*a+1/8*b+1/8*c+1/8*d+1/8*e+1/8*f");
        shiftRight_helper("a+b+c+d+e+f", 4, "1/16*a+1/16*b+1/16*c+1/16*d+1/16*e+1/16*f");
        shiftRight_helper("a+b+c+d+e+f", -1, "2*a+2*b+2*c+2*d+2*e+2*f");
        shiftRight_helper("a+b+c+d+e+f", -2, "4*a+4*b+4*c+4*d+4*e+4*f");
        shiftRight_helper("a+b+c+d+e+f", -3, "8*a+8*b+8*c+8*d+8*e+8*f");
        shiftRight_helper("a+b+c+d+e+f", -4, "16*a+16*b+16*c+16*d+16*e+16*f");

        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 0, "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 1, "1/2*x*y^2*z+1/2*x^2*z^2+1/2*x^3+11/7*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 2, "1/4*x*y^2*z+1/4*x^2*z^2+1/4*x^3+11/14*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 3, "1/8*x*y^2*z+1/8*x^2*z^2+1/8*x^3+11/28*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 4, "1/16*x*y^2*z+1/16*x^2*z^2+1/16*x^3+11/56*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -1, "2*x*y^2*z+2*x^2*z^2+2*x^3+44/7*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -2, "4*x*y^2*z+4*x^2*z^2+4*x^3+88/7*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -3, "8*x*y^2*z+8*x^2*z^2+8*x^3+176/7*z^2");
        shiftRight_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", -4, "16*x*y^2*z+16*x^2*z^2+16*x^3+352/7*z^2");
    }

    private static void sum_helper(@NotNull String input, @NotNull String output) {
        RationalMultivariatePolynomial p = sum(readRationalMultivariatePolynomialList(input));
        p.validate();
        aeq(p, output);
    }

    private static void sum_fail_helper(@NotNull String input) {
        try {
            sum(readRationalMultivariatePolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testSum() {
        sum_helper("[]", "0");
        sum_helper("[1]", "1");
        sum_helper("[-4/3]", "-4/3");
        sum_helper("[-4/3, ooo, a*b*c, x^2-7/4*x+1/3, x^2+1/2*x*y+y^2]", "a*b*c+2*x^2+1/2*x*y+y^2-7/4*x+ooo-1");

        sum_fail_helper("[-4/3, null, a*b*c, x^2-7/4*x+1/3, x^2+1/2*x*y+y^2]");
    }

    private static void product_helper(@NotNull String input, @NotNull String output) {
        RationalMultivariatePolynomial p = product(readRationalMultivariatePolynomialList(input));
        p.validate();
        aeq(p, output);
    }

    private static void product_fail_helper(@NotNull String input) {
        try {
            product(readRationalMultivariatePolynomialListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testProduct() {
        product_helper("[]", "1");
        product_helper("[1]", "1");
        product_helper("[-4/3]", "-4/3");
        product_helper("[-4/3, ooo, a*b*c, x^2-7/4*x+1/3, x^2+1/2*x*y+y^2]",
                "-4/3*a*b*c*x^4*ooo-2/3*a*b*c*x^3*y*ooo-4/3*a*b*c*x^2*y^2*ooo+7/3*a*b*c*x^3*ooo+7/6*a*b*c*x^2*y*ooo+" +
                "7/3*a*b*c*x*y^2*ooo-4/9*a*b*c*x^2*ooo-2/9*a*b*c*x*y*ooo-4/9*a*b*c*y^2*ooo");

        product_fail_helper("[-4/3, null, a*b*c, x^2-7/4*x+1/3, x^2+1/2*x*y+y^2]");
    }

    private static void delta_helper(@NotNull Iterable<RationalMultivariatePolynomial> input, @NotNull String output) {
        Iterable<RationalMultivariatePolynomial> ps = delta(input);
        take(TINY_LIMIT, ps).forEach(RationalMultivariatePolynomial::validate);
        aeqitLimit(TINY_LIMIT, ps, output);
    }

    private static void delta_helper(@NotNull String input, @NotNull String output) {
        delta_helper(readRationalMultivariatePolynomialList(input), output);
    }

    private static void delta_fail_helper(@NotNull String input) {
        try {
            toList(delta(readRationalMultivariatePolynomialListWithNulls(input)));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testDelta() {
        delta_helper("[-4/3]", "[]");
        delta_helper("[-4/3, ooo]", "[ooo+4/3]");
        delta_helper("[-4/3, ooo, a*b*c, x^2-7/4*x+1/3, x^2+1/2*x*y+y^2]",
                "[ooo+4/3, a*b*c-ooo, -a*b*c+x^2-7/4*x+1/3, 1/2*x*y+y^2+7/4*x-1/3]");
        RationalMultivariatePolynomial seed = readStrict("x+1/2*y").get();
        delta_helper(map(seed::pow, ExhaustiveProvider.INSTANCE.naturalIntegers()),
                "[x+1/2*y-1, x^2+x*y+1/4*y^2-x-1/2*y, x^3+3/2*x^2*y+3/4*x*y^2+1/8*y^3-x^2-x*y-1/4*y^2," +
                " x^4+2*x^3*y+3/2*x^2*y^2+1/2*x*y^3+1/16*y^4-x^3-3/2*x^2*y-3/4*x*y^2-1/8*y^3," +
                " x^5+5/2*x^4*y+5/2*x^3*y^2+5/4*x^2*y^3+5/16*x*y^4+1/32*y^5-x^4-2*x^3*y-3/2*x^2*y^2-1/2*x*y^3-" +
                "1/16*y^4," +
                " x^6+3*x^5*y+15/4*x^4*y^2+5/2*x^3*y^3+15/16*x^2*y^4+3/16*x*y^5+1/64*y^6-x^5-5/2*x^4*y-5/2*x^3*y^2-" +
                "5/4*x^2*y^3-5/16*x*y^4-1/32*y^5," +
                " x^7+7/2*x^6*y+21/4*x^5*y^2+35/8*x^4*y^3+35/16*x^3*y^4+21/32*x^2*y^5+7/64*x*y^6+1/128*y^7-x^6-" +
                "3*x^5*y-15/4*x^4*y^2-5/2*x^3*y^3-15/16*x^2*y^4-3/16*x*y^5-1/64*y^6," +
                " x^8+4*x^7*y+7*x^6*y^2+7*x^5*y^3+35/8*x^4*y^4+7/4*x^3*y^5+7/16*x^2*y^6+1/16*x*y^7+1/256*y^8-x^7-" +
                "7/2*x^6*y-21/4*x^5*y^2-35/8*x^4*y^3-35/16*x^3*y^4-21/32*x^2*y^5-7/64*x*y^6-1/128*y^7," +
                " x^9+9/2*x^8*y+9*x^7*y^2+21/2*x^6*y^3+63/8*x^5*y^4+63/16*x^4*y^5+21/16*x^3*y^6+9/32*x^2*y^7+" +
                "9/256*x*y^8+1/512*y^9-x^8-4*x^7*y-7*x^6*y^2-7*x^5*y^3-35/8*x^4*y^4-7/4*x^3*y^5-7/16*x^2*y^6-" +
                "1/16*x*y^7-1/256*y^8," +
                " x^10+5*x^9*y+45/4*x^8*y^2+15*x^7*y^3+105/8*x^6*y^4+63/8*x^5*y^5+105/32*x^4*y^6+15/16*x^3*y^7+" +
                "45/256*x^2*y^8+5/256*x*y^9+1/1024*y^10-x^9-9/2*x^8*y-9*x^7*y^2-21/2*x^6*y^3-63/8*x^5*y^4-" +
                "63/16*x^4*y^5-21/16*x^3*y^6-9/32*x^2*y^7-9/256*x*y^8-1/512*y^9," +
                " x^11+11/2*x^10*y+55/4*x^9*y^2+165/8*x^8*y^3+165/8*x^7*y^4+231/16*x^6*y^5+231/32*x^5*y^6+" +
                "165/64*x^4*y^7+165/256*x^3*y^8+55/512*x^2*y^9+11/1024*x*y^10+1/2048*y^11-x^10-5*x^9*y-45/4*x^8*y^2-" +
                "15*x^7*y^3-105/8*x^6*y^4-63/8*x^5*y^5-105/32*x^4*y^6-15/16*x^3*y^7-45/256*x^2*y^8-5/256*x*y^9-" +
                "1/1024*y^10," +
                " x^12+6*x^11*y+33/2*x^10*y^2+55/2*x^9*y^3+495/16*x^8*y^4+99/4*x^7*y^5+231/16*x^6*y^6+99/16*x^5*y^7+" +
                "495/256*x^4*y^8+55/128*x^3*y^9+33/512*x^2*y^10+3/512*x*y^11+1/4096*y^12-x^11-11/2*x^10*y-" +
                "55/4*x^9*y^2-165/8*x^8*y^3-165/8*x^7*y^4-231/16*x^6*y^5-231/32*x^5*y^6-165/64*x^4*y^7-" +
                "165/256*x^3*y^8-55/512*x^2*y^9-11/1024*x*y^10-1/2048*y^11," +
                " x^13+13/2*x^12*y+39/2*x^11*y^2+143/4*x^10*y^3+715/16*x^9*y^4+1287/32*x^8*y^5+429/16*x^7*y^6+" +
                "429/32*x^6*y^7+1287/256*x^5*y^8+715/512*x^4*y^9+143/512*x^3*y^10+39/1024*x^2*y^11+13/4096*x*y^12+" +
                "1/8192*y^13-x^12-6*x^11*y-33/2*x^10*y^2-55/2*x^9*y^3-495/16*x^8*y^4-99/4*x^7*y^5-231/16*x^6*y^6-" +
                "99/16*x^5*y^7-495/256*x^4*y^8-55/128*x^3*y^9-33/512*x^2*y^10-3/512*x*y^11-1/4096*y^12," +
                " x^14+7*x^13*y+91/4*x^12*y^2+91/2*x^11*y^3+1001/16*x^10*y^4+1001/16*x^9*y^5+3003/64*x^8*y^6+" +
                "429/16*x^7*y^7+3003/256*x^6*y^8+1001/256*x^5*y^9+1001/1024*x^4*y^10+91/512*x^3*y^11+" +
                "91/4096*x^2*y^12+7/4096*x*y^13+1/16384*y^14-x^13-13/2*x^12*y-39/2*x^11*y^2-143/4*x^10*y^3-" +
                "715/16*x^9*y^4-1287/32*x^8*y^5-429/16*x^7*y^6-429/32*x^6*y^7-1287/256*x^5*y^8-715/512*x^4*y^9-" +
                "143/512*x^3*y^10-39/1024*x^2*y^11-13/4096*x*y^12-1/8192*y^13," +
                " x^15+15/2*x^14*y+105/4*x^13*y^2+455/8*x^12*y^3+1365/16*x^11*y^4+3003/32*x^10*y^5+5005/64*x^9*y^6+" +
                "6435/128*x^8*y^7+6435/256*x^7*y^8+5005/512*x^6*y^9+3003/1024*x^5*y^10+1365/2048*x^4*y^11+" +
                "455/4096*x^3*y^12+105/8192*x^2*y^13+15/16384*x*y^14+1/32768*y^15-x^14-7*x^13*y-91/4*x^12*y^2-" +
                "91/2*x^11*y^3-1001/16*x^10*y^4-1001/16*x^9*y^5-3003/64*x^8*y^6-429/16*x^7*y^7-3003/256*x^6*y^8-" +
                "1001/256*x^5*y^9-1001/1024*x^4*y^10-91/512*x^3*y^11-91/4096*x^2*y^12-7/4096*x*y^13-1/16384*y^14," +
                " x^16+8*x^15*y+30*x^14*y^2+70*x^13*y^3+455/4*x^12*y^4+273/2*x^11*y^5+1001/8*x^10*y^6+715/8*x^9*y^7+" +
                "6435/128*x^8*y^8+715/32*x^7*y^9+1001/128*x^6*y^10+273/128*x^5*y^11+455/1024*x^4*y^12+" +
                "35/512*x^3*y^13+15/2048*x^2*y^14+1/2048*x*y^15+1/65536*y^16-x^15-15/2*x^14*y-105/4*x^13*y^2-" +
                "455/8*x^12*y^3-1365/16*x^11*y^4-3003/32*x^10*y^5-5005/64*x^9*y^6-6435/128*x^8*y^7-6435/256*x^7*y^8-" +
                "5005/512*x^6*y^9-3003/1024*x^5*y^10-1365/2048*x^4*y^11-455/4096*x^3*y^12-105/8192*x^2*y^13-" +
                "15/16384*x*y^14-1/32768*y^15, x^17+17/2*x^16*y+34*x^15*y^2+85*x^14*y^3+595/4*x^13*y^4+" +
                "1547/8*x^12*y^5+1547/8*x^11*y^6+2431/16*x^10*y^7+12155/128*x^9*y^8+" +
                "12155/256*x^8*y^9+2431/128*x^7*y^10+1547/256*x^6*y^11+1547/1024*x^5*y^12+595/2048*x^4*y^13+" +
                "85/2048*x^3*y^14+17/4096*x^2*y^15+17/65536*x*y^16+1/131072*y^17-x^16-8*x^15*y-30*x^14*y^2-" +
                "70*x^13*y^3-455/4*x^12*y^4-273/2*x^11*y^5-1001/8*x^10*y^6-715/8*x^9*y^7-6435/128*x^8*y^8-" +
                "715/32*x^7*y^9-1001/128*x^6*y^10-273/128*x^5*y^11-455/1024*x^4*y^12-35/512*x^3*y^13-" +
                "15/2048*x^2*y^14-1/2048*x*y^15-1/65536*y^16, x^18+9*x^17*y+153/4*x^16*y^2+102*x^15*y^3+" +
                "765/4*x^14*y^4+1071/4*x^13*y^5+4641/16*x^12*y^6+1989/8*x^11*y^7+21879/128*x^10*y^8+" +
                "12155/128*x^9*y^9+21879/512*x^8*y^10+1989/128*x^7*y^11+4641/1024*x^6*y^12+1071/1024*x^5*y^13+" +
                "765/4096*x^4*y^14+51/2048*x^3*y^15+153/65536*x^2*y^16+9/65536*x*y^17+1/262144*y^18-x^17-" +
                "17/2*x^16*y-34*x^15*y^2-85*x^14*y^3-595/4*x^13*y^4-1547/8*x^12*y^5-1547/8*x^11*y^6-" +
                "2431/16*x^10*y^7-12155/128*x^9*y^8-12155/256*x^8*y^9-2431/128*x^7*y^10-1547/256*x^6*y^11-" +
                "1547/1024*x^5*y^12-595/2048*x^4*y^13-85/2048*x^3*y^14-17/4096*x^2*y^15-17/65536*x*y^16-" +
                "1/131072*y^17," +
                " x^19+19/2*x^18*y+171/4*x^17*y^2+969/8*x^16*y^3+969/4*x^15*y^4+2907/8*x^14*y^5+6783/16*x^13*y^6+" +
                "12597/32*x^12*y^7+37791/128*x^11*y^8+46189/256*x^10*y^9+46189/512*x^9*y^10+37791/1024*x^8*y^11+" +
                "12597/1024*x^7*y^12+6783/2048*x^6*y^13+2907/4096*x^5*y^14+969/8192*x^4*y^15+969/65536*x^3*y^16+" +
                "171/131072*x^2*y^17+19/262144*x*y^18+1/524288*y^19-x^18-9*x^17*y-153/4*x^16*y^2-102*x^15*y^3-" +
                "765/4*x^14*y^4-1071/4*x^13*y^5-4641/16*x^12*y^6-1989/8*x^11*y^7-21879/128*x^10*y^8-" +
                "12155/128*x^9*y^9-21879/512*x^8*y^10-1989/128*x^7*y^11-4641/1024*x^6*y^12-1071/1024*x^5*y^13-" +
                "765/4096*x^4*y^14-51/2048*x^3*y^15-153/65536*x^2*y^16-9/65536*x*y^17-1/262144*y^18," +
                " x^20+10*x^19*y+95/2*x^18*y^2+285/2*x^17*y^3+4845/16*x^16*y^4+969/2*x^15*y^5+4845/8*x^14*y^6+" +
                "4845/8*x^13*y^7+62985/128*x^12*y^8+20995/64*x^11*y^9+46189/256*x^10*y^10+20995/256*x^9*y^11+" +
                "62985/2048*x^8*y^12+4845/512*x^7*y^13+4845/2048*x^6*y^14+969/2048*x^5*y^15+4845/65536*x^4*y^16+" +
                "285/32768*x^3*y^17+95/131072*x^2*y^18+5/131072*x*y^19+1/1048576*y^20-x^19-19/2*x^18*y-" +
                "171/4*x^17*y^2-969/8*x^16*y^3-969/4*x^15*y^4-2907/8*x^14*y^5-6783/16*x^13*y^6-12597/32*x^12*y^7-" +
                "37791/128*x^11*y^8-46189/256*x^10*y^9-46189/512*x^9*y^10-37791/1024*x^8*y^11-12597/1024*x^7*y^12-" +
                "6783/2048*x^6*y^13-2907/4096*x^5*y^14-969/8192*x^4*y^15-969/65536*x^3*y^16-171/131072*x^2*y^17-" +
                "19/262144*x*y^18-1/524288*y^19, ...]");

        delta_fail_helper("[]");
        delta_fail_helper("[-17, null, a*b*c, x^2-4*x+7, x^2+2*x*y+y^2]");
    }

    private static void pow_helper(@NotNull String p, int exponent, @NotNull String output) {
        RationalMultivariatePolynomial q = readStrict(p).get().pow(exponent);
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

        pow_helper("-4/3", 0, "1");
        pow_helper("-4/3", 1, "-4/3");
        pow_helper("-4/3", 2, "16/9");
        pow_helper("-4/3", 3, "-64/27");

        pow_helper("ooo", 0, "1");
        pow_helper("ooo", 1, "ooo");
        pow_helper("ooo", 2, "ooo^2");
        pow_helper("ooo", 3, "ooo^3");

        pow_helper("a*b*c", 0, "1");
        pow_helper("a*b*c", 1, "a*b*c");
        pow_helper("a*b*c", 2, "a^2*b^2*c^2");
        pow_helper("a*b*c", 3, "a^3*b^3*c^3");

        pow_helper("x^2-7/4*x+1/3", 0, "1");
        pow_helper("x^2-7/4*x+1/3", 1, "x^2-7/4*x+1/3");
        pow_helper("x^2-7/4*x+1/3", 2, "x^4-7/2*x^3+179/48*x^2-7/6*x+1/9");
        pow_helper("x^2-7/4*x+1/3", 3, "x^6-21/4*x^5+163/16*x^4-567/64*x^3+163/48*x^2-7/12*x+1/27");

        pow_helper("x^2+1/2*x*y+y^2", 0, "1");
        pow_helper("x^2+1/2*x*y+y^2", 1, "x^2+1/2*x*y+y^2");
        pow_helper("x^2+1/2*x*y+y^2", 2, "x^4+x^3*y+9/4*x^2*y^2+x*y^3+y^4");
        pow_helper("x^2+1/2*x*y+y^2", 3, "x^6+3/2*x^5*y+15/4*x^4*y^2+25/8*x^3*y^3+15/4*x^2*y^4+3/2*x*y^5+y^6");

        pow_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 0, "1");
        pow_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 1, "x*y^2*z+x^2*z^2+x^3+22/7*z^2");
        pow_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 2,
                "x^2*y^4*z^2+2*x^3*y^2*z^3+x^4*z^4+2*x^4*y^2*z+2*x^5*z^2+x^6+44/7*x*y^2*z^3+44/7*x^2*z^4+" +
                "44/7*x^3*z^2+484/49*z^4");
        pow_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", 3,
                "x^3*y^6*z^3+3*x^4*y^4*z^4+3*x^5*y^2*z^5+x^6*z^6+3*x^5*y^4*z^2+6*x^6*y^2*z^3+3*x^7*z^4+3*x^7*y^2*z+" +
                "3*x^8*z^2+66/7*x^2*y^4*z^4+132/7*x^3*y^2*z^5+66/7*x^4*z^6+x^9+132/7*x^4*y^2*z^3+132/7*x^5*z^4+" +
                "66/7*x^6*z^2+1452/49*x*y^2*z^5+1452/49*x^2*z^6+1452/49*x^3*z^4+10648/343*z^6");

        pow_fail_helper("a*b*c", -1);
        pow_fail_helper("0", -1);
        pow_fail_helper("1", -1);
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
        apply_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(x, -5/3), (y, 1/2), (z, 3)]", "35839/756");
        apply_Rational_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(x, -5/3), (y, 0), (z, 3), (ooo, 3)]", "9196/189");
        apply_Rational_helper("a+b+c", "[(a, 1), (b, 1/2), (c, 1/3)]", "11/6");

        apply_Rational_fail_helper("a", "[]");
        apply_Rational_fail_helper("a", "[(b, 1/2)]");
        apply_Rational_fail_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(x, -5/3), (y, 1/2)]");
        apply_Rational_fail_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(x, -5/3), (y, 1/2), (z, null)]");
        apply_Rational_fail_helper("x*y^2*z+x^2*z^2+x^3+22/7*z^2", "[(x, -5/3), (y, 1/2), (null, 1)]");
    }

    private static @NotNull List<Pair<Monomial, Rational>> readMonomialRationalPairList(
            @NotNull String s
    ) {
        return Readers.readListStrict(
                u -> Pair.readStrict(
                        u,
                        t -> NullableOptional.fromOptional(Monomial.readStrict(t)),
                        t -> NullableOptional.fromOptional(Rational.readStrict(t))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<Monomial, Rational>> readMonomialRationalPairListWithNulls(
            @NotNull String s
    ) {
        return Readers.readListWithNullsStrict(
                u -> Pair.readStrict(
                        u, Readers.readWithNullsStrict(Monomial::readStrict),
                        Readers.readWithNullsStrict(Rational::readStrict)
                )
        ).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalMultivariatePolynomial> readRationalMultivariatePolynomialList(
            @NotNull String s
    ) {
        return Readers.readListStrict(RationalMultivariatePolynomial::readStrict).apply(s).get();
    }

    private static @NotNull List<RationalMultivariatePolynomial> readRationalMultivariatePolynomialListWithNulls(
            @NotNull String s
    ) {
        return Readers.readListWithNullsStrict(RationalMultivariatePolynomial::readStrict).apply(s).get();
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
}
