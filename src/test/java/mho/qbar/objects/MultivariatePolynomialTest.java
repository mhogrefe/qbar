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
        MultivariatePolynomial q = readStrict(p, MonomialOrder.readStrict(mo).get()).get();
        q.validate();
        aeq(q, output);
    }

    private static void readStrict_String_MonomialOrder_fail_helper(@NotNull String p, @NotNull String mo) {
        assertFalse(readStrict(p, MonomialOrder.readStrict(mo).get()).isPresent());
    }

    @Test
    public void testReadStrict_String_MonomialOrder() {
        readStrict_String_MonomialOrder_helper("0", "LEX", "0");
        readStrict_String_MonomialOrder_helper("0", "GRLEX", "0");
        readStrict_String_MonomialOrder_helper("0", "GREVLEX", "0");
        readStrict_String_MonomialOrder_helper("1", "LEX", "1");
        readStrict_String_MonomialOrder_helper("1", "GRLEX", "1");
        readStrict_String_MonomialOrder_helper("1", "GREVLEX", "1");
        readStrict_String_MonomialOrder_helper("-17", "LEX", "-17");
        readStrict_String_MonomialOrder_helper("-17", "GRLEX", "-17");
        readStrict_String_MonomialOrder_helper("-17", "GREVLEX", "-17");
        readStrict_String_MonomialOrder_helper("ooo", "LEX", "ooo");
        readStrict_String_MonomialOrder_helper("ooo", "GRLEX", "ooo");
        readStrict_String_MonomialOrder_helper("ooo", "GREVLEX", "ooo");
        readStrict_String_MonomialOrder_helper("-ooo", "LEX", "-ooo");
        readStrict_String_MonomialOrder_helper("-ooo", "GRLEX", "-ooo");
        readStrict_String_MonomialOrder_helper("-ooo", "GREVLEX", "-ooo");
        readStrict_String_MonomialOrder_helper("a*b*c", "LEX", "a*b*c");
        readStrict_String_MonomialOrder_helper("a*b*c", "GRLEX", "a*b*c");
        readStrict_String_MonomialOrder_helper("a*b*c", "GREVLEX", "a*b*c");
        readStrict_String_MonomialOrder_helper("x^2-4*x+7", "LEX", "x^2-4*x+7");
        readStrict_String_MonomialOrder_helper("x^2-4*x+7", "GRLEX", "x^2-4*x+7");
        readStrict_String_MonomialOrder_helper("x^2-4*x+7", "GREVLEX", "x^2-4*x+7");
        readStrict_String_MonomialOrder_helper("a+b+c+d+e+f", "LEX", "a+b+c+d+e+f");
        readStrict_String_MonomialOrder_helper("a+b+c+d+e+f", "GRLEX", "a+b+c+d+e+f");
        readStrict_String_MonomialOrder_helper("a+b+c+d+e+f", "GREVLEX", "a+b+c+d+e+f");
        readStrict_String_MonomialOrder_helper("x^3+x^2*z^2+x*y^2*z+z^2", "LEX", "x*y^2*z+x^2*z^2+x^3+z^2");
        readStrict_String_MonomialOrder_helper("x^2*z^2+x*y^2*z+x^3+z^2", "GRLEX", "x*y^2*z+x^2*z^2+x^3+z^2");
        readStrict_String_MonomialOrder_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GREVLEX", "x*y^2*z+x^2*z^2+x^3+z^2");

        readStrict_String_MonomialOrder_fail_helper("", "LEX");
        readStrict_String_MonomialOrder_fail_helper("", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("hello", "LEX");
        readStrict_String_MonomialOrder_fail_helper("hello", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("hello", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper(" ", "LEX");
        readStrict_String_MonomialOrder_fail_helper(" ", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper(" ", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("00", "LEX");
        readStrict_String_MonomialOrder_fail_helper("00", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("00", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("-0", "LEX");
        readStrict_String_MonomialOrder_fail_helper("-0", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("-0", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("1/2", "LEX");
        readStrict_String_MonomialOrder_fail_helper("1/2", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("1/2", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("1*x", "LEX");
        readStrict_String_MonomialOrder_fail_helper("1*x", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("1*x", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("x*2", "LEX");
        readStrict_String_MonomialOrder_fail_helper("x*2", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("x*2", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("0*x", "LEX");
        readStrict_String_MonomialOrder_fail_helper("0*x", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("0*x", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("-1*x", "LEX");
        readStrict_String_MonomialOrder_fail_helper("-1*x", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("-1*x", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a*a", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a*a", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a*a", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a^1", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a^1", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a^1", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a^0", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a^0", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a^0", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a^-1", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a^-1", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a^-1", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("b*a", "LEX");
        readStrict_String_MonomialOrder_fail_helper("b*a", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("b*a", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("-4*x+x^2+7", "LEX");
        readStrict_String_MonomialOrder_fail_helper("-4*x+x^2+7", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("-4*x+x^2+7", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("b+a+c+d+e+f", "LEX");
        readStrict_String_MonomialOrder_fail_helper("b+a+c+d+e+f", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("b+a+c+d+e+f", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a+a", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a+a", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a+a", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a+0", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a+0", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a+0", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("a+-1", "LEX");
        readStrict_String_MonomialOrder_fail_helper("a+-1", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("a+-1", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("*x", "LEX");
        readStrict_String_MonomialOrder_fail_helper("*x", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("*x", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("+x", "LEX");
        readStrict_String_MonomialOrder_fail_helper("+x", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("+x", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("+1", "LEX");
        readStrict_String_MonomialOrder_fail_helper("+1", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("+1", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("+0", "LEX");
        readStrict_String_MonomialOrder_fail_helper("+0", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("+0", "GREVLEX");

        readStrict_String_MonomialOrder_fail_helper("x^2*z^2+x*y^2*z+x^3+z^2", "LEX");
        readStrict_String_MonomialOrder_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "LEX");
        readStrict_String_MonomialOrder_fail_helper("x^3+x^2*z^2+x*y^2*z+z^2", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("x*y^2*z+x^2*z^2+x^3+z^2", "GRLEX");
        readStrict_String_MonomialOrder_fail_helper("x^3+x^2*z^2+x*y^2*z+z^2", "GREVLEX");
        readStrict_String_MonomialOrder_fail_helper("x^2*z^2+x*y^2*z+x^3+z^2", "GREVLEX");
    }

    private static void readStrict_String_helper(@NotNull String input) {
        MultivariatePolynomial p = readStrict(input).get();
        p.validate();
        aeq(p, input);
    }

    private static void readStrict_String_fail_helper(@NotNull String input) {
        assertFalse(readStrict(input).isPresent());
    }

    @Test
    public void testReadStrict_String() {
        readStrict_String_helper("0");
        readStrict_String_helper("1");
        readStrict_String_helper("-17");
        readStrict_String_helper("ooo");
        readStrict_String_helper("-ooo");
        readStrict_String_helper("a*b*c");
        readStrict_String_helper("x^2-4*x+7");
        readStrict_String_helper("a+b+c+d+e+f");
        readStrict_String_helper("x*y^2*z+x^2*z^2+x^3+z^2");

        readStrict_String_fail_helper("");
        readStrict_String_fail_helper("hello");
        readStrict_String_fail_helper(" ");
        readStrict_String_fail_helper("00");
        readStrict_String_fail_helper("-0");
        readStrict_String_fail_helper("1/2");
        readStrict_String_fail_helper("1*x");
        readStrict_String_fail_helper("x*2");
        readStrict_String_fail_helper("0*x");
        readStrict_String_fail_helper("-1*x");
        readStrict_String_fail_helper("a*a");
        readStrict_String_fail_helper("a^1");
        readStrict_String_fail_helper("a^0");
        readStrict_String_fail_helper("a^-1");
        readStrict_String_fail_helper("b*a");
        readStrict_String_fail_helper("-4*x+x^2+7");
        readStrict_String_fail_helper("b+a+c+d+e+f");
        readStrict_String_fail_helper("a+a");
        readStrict_String_fail_helper("a+0");
        readStrict_String_fail_helper("a+-1");
        readStrict_String_fail_helper("*x");
        readStrict_String_fail_helper("+x");
        readStrict_String_fail_helper("+1");
        readStrict_String_fail_helper("+0");
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
                u -> Pair.read(
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
                u -> Pair.read(
                        u, Readers.readWithNullsStrict(Monomial::readStrict),
                        Readers.readWithNullsStrict(Readers::readBigIntegerStrict)
                )
        ).apply(s).get();
    }

    private static @NotNull List<MultivariatePolynomial> readMultivariatePolynomialList(@NotNull String s) {
        return Readers.readListStrict(MultivariatePolynomial::readStrict).apply(s).get();
    }
}
