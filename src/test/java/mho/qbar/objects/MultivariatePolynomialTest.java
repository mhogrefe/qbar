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
import static mho.wheels.testing.Testing.testCompareToHelper;
import static mho.wheels.testing.Testing.testEqualsHelper;
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

    private static void of_Polynomial_Variable_helper(@NotNull String p, @NotNull String v, @NotNull String output) {
        aeq(of(Polynomial.read(p).get(), Variable.read(v).get()), output);
    }

    @Test
    public void testOf_Polynomial_Variable() {
        of_Polynomial_Variable_helper("0", "x", "0");
        of_Polynomial_Variable_helper("1", "x", "1");
        of_Polynomial_Variable_helper("x", "x", "x");
        of_Polynomial_Variable_helper("x", "ooo", "ooo");
        of_Polynomial_Variable_helper("-17", "x", "-17");
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
        toPolynomial_helper("ooo", "x");
        toPolynomial_helper("-17", "-17");
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
        variables_helper("ooo", "[ooo]");
        variables_helper("-17", "[]");
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
        variableCount_helper("ooo", 1);
        variableCount_helper("-17", 0);
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
        termCount_helper("ooo", 1);
        termCount_helper("-17", 1);
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
        maxCoefficientBitLength_helper("ooo", 1);
        maxCoefficientBitLength_helper("-17", 5);
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
        degree_helper("ooo", 1);
        degree_helper("-17", 0);
        degree_helper("a*b*c", 3);
        degree_helper("x^2-4*x+7", 2);
        degree_helper("x^2+2*x*y+y^2", 2);
        degree_helper("a+b+c+d+e+f", 1);
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
        hashCode_helper("ooo", -246313024);
        hashCode_helper("-17", 45);
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
