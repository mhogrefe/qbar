package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

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
}
