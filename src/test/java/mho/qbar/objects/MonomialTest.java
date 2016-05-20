package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static mho.qbar.objects.Monomial.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class MonomialTest {
    @Test
    public void testConstants() {
        aeq(ONE, "1");
    }

    private static void getExponents_helper(@NotNull String x, @NotNull String output) {
        aeq(readStrict(x).get().getExponents(), output);
    }

    @Test
    public void testGetExponents() {
        getExponents_helper("1", "[]");
        getExponents_helper("a", "[1]");
        getExponents_helper("a^2", "[2]");
        getExponents_helper("a^3", "[3]");
        getExponents_helper("x^2*y*z^3",
                "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 3]");
    }

    private static void exponent_helper(@NotNull String m, @NotNull String v, int output) {
        aeq(readStrict(m).get().exponent(Variable.readStrict(v).get()), output);
    }

    @Test
    public void testExponent() {
        exponent_helper("1", "a", 0);
        exponent_helper("1", "z", 0);
        exponent_helper("a", "a", 1);
        exponent_helper("a", "z", 0);
        exponent_helper("a^2", "a", 2);
        exponent_helper("a^2", "z", 0);
        exponent_helper("a^3", "a", 3);
        exponent_helper("a^3", "z", 0);
        exponent_helper("x^2*y*z^3", "a", 0);
        exponent_helper("x^2*y*z^3", "x", 2);
        exponent_helper("x^2*y*z^3", "y", 1);
        exponent_helper("x^2*y*z^3", "z", 3);
    }

    private static void size_helper(@NotNull String m, int output) {
        aeq(readStrict(m).get().size(), output);
    }

    @Test
    public void testSize() {
        size_helper("1", 0);
        size_helper("a", 1);
        size_helper("a^2", 1);
        size_helper("a^3", 1);
        size_helper("x^2*y*z^3", 26);
    }

    private static void terms_helper(@NotNull String input, @NotNull String output) {
        aeqit(readStrict(input).get().terms(), output);
    }

    @Test
    public void testTerms() {
        terms_helper("1", "[]");
        terms_helper("a", "[(a, 1)]");
        terms_helper("a^2", "[(a, 2)]");
        terms_helper("a^3", "[(a, 3)]");
        terms_helper("x^2*y*z^3", "[(x, 2), (y, 1), (z, 3)]");
    }

    private static void of_List_Integer_helper(@NotNull String input, @NotNull String output) {
        aeq(of(readIntegerList(input)), output);
    }

    private static void of_List_Integer_fail_helper(@NotNull String input) {
        try {
            of(readIntegerListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testOf_List_Integer() {
        of_List_Integer_helper("[]", "1");
        of_List_Integer_helper("[0]", "1");
        of_List_Integer_helper("[0, 0, 0]", "1");
        of_List_Integer_helper("[1]", "a");
        of_List_Integer_helper("[0, 0, 0, 4]", "d^4");
        of_List_Integer_helper("[1, 2, 3, 4, 5]", "a*b^2*c^3*d^4*e^5");
        of_List_Integer_helper("[1, 2, 3, 4, 5, 0]", "a*b^2*c^3*d^4*e^5");

        of_List_Integer_fail_helper("[1, -1, 3]");
        of_List_Integer_fail_helper("[1, null, 3]");
    }

    private static void of_Variable(@NotNull String input) {
        Monomial m = of(Variable.readStrict(input).get());
        m.validate();
        aeq(m, input);
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

    private static void fromTerms_helper(@NotNull String input, @NotNull String output) {
        aeq(fromTerms(readVariableIntegerPairList(input)), output);
    }

    private static void fromTerms_fail_helper(@NotNull String input) {
        try {
            fromTerms(readVariableIntegerPairListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testFromTerms() {
        fromTerms_helper("[]", "1");
        fromTerms_helper("[(a, 1)]", "a");
        fromTerms_helper("[(a, 2)]", "a^2");
        fromTerms_helper("[(a, 3)]", "a^3");
        fromTerms_helper("[(b, 2), (b, 3)]", "b^5");
        fromTerms_helper("[(b, 2), (a, 3)]", "a^3*b^2");
        fromTerms_helper("[(x, 2), (y, 1), (z, 3)]", "x^2*y*z^3");

        fromTerms_fail_helper("[null]");
        fromTerms_fail_helper("[(a, 1), null]");
        fromTerms_fail_helper("[(a, null)]");
        fromTerms_fail_helper("[(null, 1)]");
        fromTerms_fail_helper("[(a, -1)]");
        fromTerms_fail_helper("[(a, 0)]");
    }

    private static void degree_helper(@NotNull String m, int output) {
        aeq(readStrict(m).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("1", 0);
        degree_helper("a", 1);
        degree_helper("a^2", 2);
        degree_helper("a^3", 3);
        degree_helper("x^2*y*z^3", 6);
    }

    private static void variables_helper(@NotNull String input, @NotNull String output) {
        aeq(readStrict(input).get().variables(), output);
    }

    @Test
    public void testVariables() {
        variables_helper("1", "[]");
        variables_helper("a", "[a]");
        variables_helper("a^2", "[a]");
        variables_helper("a^3", "[a]");
        variables_helper("a*b", "[a, b]");
        variables_helper("ooo", "[ooo]");
        variables_helper("x^2*y*z^3", "[x, y, z]");
    }

    private static void variableCount_helper(@NotNull String input, int output) {
        aeq(readStrict(input).get().variableCount(), output);
    }

    @Test
    public void testVariableCount() {
        variableCount_helper("1", 0);
        variableCount_helper("a", 1);
        variableCount_helper("a^2", 1);
        variableCount_helper("a^3", 1);
        variableCount_helper("a*b", 2);
        variableCount_helper("ooo", 1);
        variableCount_helper("x^2*y*z^3", 3);
    }

    private static void removeVariable_helper(@NotNull String m, @NotNull String v, @NotNull String output) {
        aeq(readStrict(m).get().removeVariable(Variable.readStrict(v).get()), output);
    }

    @Test
    public void testRemoveVariable() {
        removeVariable_helper("1", "a", "1");
        removeVariable_helper("1", "ooo", "1");
        removeVariable_helper("a", "a", "1");
        removeVariable_helper("a", "b", "a");
        removeVariable_helper("a", "ooo", "a");
        removeVariable_helper("a^2", "a", "1");
        removeVariable_helper("a^2", "b", "a^2");
        removeVariable_helper("a^2", "ooo", "a^2");
        removeVariable_helper("x^2*y*z^3", "a", "x^2*y*z^3");
        removeVariable_helper("x^2*y*z^3", "x", "y*z^3");
        removeVariable_helper("x^2*y*z^3", "y", "x^2*z^3");
        removeVariable_helper("x^2*y*z^3", "z", "x^2*y");
    }

    private static void removeVariables_helper(@NotNull String m, @NotNull String vs, @NotNull String output) {
        aeq(readStrict(m).get().removeVariables(readVariableList(vs)), output);
    }

    private static void removeVariables_fail_helper(@NotNull String m, @NotNull String vs) {
        try {
            readStrict(m).get().removeVariables(readVariableListWithNulls(vs));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testRemoveVariables() {
        removeVariables_helper("1", "[]", "1");
        removeVariables_helper("1", "[a]", "1");
        removeVariables_helper("1", "[a, z]", "1");
        removeVariables_helper("a", "[]", "a");
        removeVariables_helper("a", "[b, z]", "a");
        removeVariables_helper("a", "[a, z]", "1");
        removeVariables_helper("x^2*y*z^3", "[]", "x^2*y*z^3");
        removeVariables_helper("x^2*y*z^3", "[x]", "y*z^3");
        removeVariables_helper("x^2*y*z^3", "[y]", "x^2*z^3");
        removeVariables_helper("x^2*y*z^3", "[z]", "x^2*y");
        removeVariables_helper("x^2*y*z^3", "[x, y]", "z^3");
        removeVariables_helper("x^2*y*z^3", "[x, z]", "y");
        removeVariables_helper("x^2*y*z^3", "[y, z]", "x^2");
        removeVariables_helper("x^2*y*z^3", "[x, y, z]", "1");

        removeVariables_fail_helper("1", "[a, null, b]");
        removeVariables_fail_helper("a", "[a, null, b]");
    }

    private static void retainVariables_helper(@NotNull String m, @NotNull String vs, @NotNull String output) {
        aeq(readStrict(m).get().retainVariables(readVariableList(vs)), output);
    }

    private static void retainVariables_fail_helper(@NotNull String m, @NotNull String vs) {
        try {
            readStrict(m).get().retainVariables(readVariableListWithNulls(vs));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testRetainVariables() {
        retainVariables_helper("1", "[]", "1");
        retainVariables_helper("1", "[a]", "1");
        retainVariables_helper("1", "[a, z]", "1");
        retainVariables_helper("a", "[]", "1");
        retainVariables_helper("a", "[b, z]", "1");
        retainVariables_helper("a", "[a, z]", "a");
        retainVariables_helper("x^2*y*z^3", "[]", "1");
        retainVariables_helper("x^2*y*z^3", "[x]", "x^2");
        retainVariables_helper("x^2*y*z^3", "[y]", "y");
        retainVariables_helper("x^2*y*z^3", "[z]", "z^3");
        retainVariables_helper("x^2*y*z^3", "[x, y]", "x^2*y");
        retainVariables_helper("x^2*y*z^3", "[x, z]", "x^2*z^3");
        retainVariables_helper("x^2*y*z^3", "[y, z]", "y*z^3");
        retainVariables_helper("x^2*y*z^3", "[x, y, z]", "x^2*y*z^3");

        retainVariables_fail_helper("1", "[a, null, b]");
        retainVariables_fail_helper("a", "[a, null, b]");
    }

    private static void multiply_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(readStrict(a).get().multiply(readStrict(b).get()), output);
    }

    @Test
    public void testMultiply() {
        multiply_helper("1", "1", "1");
        multiply_helper("1", "a", "a");
        multiply_helper("1", "a^2", "a^2");
        multiply_helper("1", "x^2*y*z^3", "x^2*y*z^3");
        multiply_helper("1", "ooo", "ooo");

        multiply_helper("a", "1", "a");
        multiply_helper("a", "a", "a^2");
        multiply_helper("a", "a^2", "a^3");
        multiply_helper("a", "x^2*y*z^3", "a*x^2*y*z^3");
        multiply_helper("a", "ooo", "a*ooo");

        multiply_helper("a^2", "1", "a^2");
        multiply_helper("a^2", "a", "a^3");
        multiply_helper("a^2", "a^2", "a^4");
        multiply_helper("a^2", "x^2*y*z^3", "a^2*x^2*y*z^3");
        multiply_helper("a^2", "ooo", "a^2*ooo");

        multiply_helper("x^2*y*z^3", "1", "x^2*y*z^3");
        multiply_helper("x^2*y*z^3", "a", "a*x^2*y*z^3");
        multiply_helper("x^2*y*z^3", "a^2", "a^2*x^2*y*z^3");
        multiply_helper("x^2*y*z^3", "x^2*y*z^3", "x^4*y^2*z^6");
        multiply_helper("x^2*y*z^3", "ooo", "x^2*y*z^3*ooo");

        multiply_helper("ooo", "1", "ooo");
        multiply_helper("ooo", "a", "a*ooo");
        multiply_helper("ooo", "a^2", "a^2*ooo");
        multiply_helper("ooo", "x^2*y*z^3", "x^2*y*z^3*ooo");
        multiply_helper("ooo", "ooo", "ooo^2");
    }

    private static void product_helper(@NotNull String input, @NotNull String output) {
        Monomial m = product(readMonomialList(input));
        m.validate();
        aeq(m, output);
    }

    private static void product_fail_helper(@NotNull String input) {
        try {
            product(readMonomialListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        product_helper("[]", "1");
        product_helper("[a]", "a");
        product_helper("[a, b, c]", "a*b*c");
        product_helper("[a, a^2, ooo, x^2*y*z^3, x]", "a^3*x^3*y*z^3*ooo");

        product_fail_helper("[a, a^2, ooo, null, x]");
    }

    private static void pow_helper(@NotNull String p, int exponent, @NotNull String output) {
        aeq(readStrict(p).get().pow(exponent), output);
    }

    private static void pow_fail_helper(@NotNull String p, int exponent) {
        try {
            readStrict(p).get().pow(exponent);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testPow() {
        pow_helper("1", 0, "1");
        pow_helper("1", 1, "1");
        pow_helper("1", 2, "1");
        pow_helper("1", 3, "1");

        pow_helper("a", 0, "1");
        pow_helper("a", 1, "a");
        pow_helper("a", 2, "a^2");
        pow_helper("a", 3, "a^3");

        pow_helper("a^2", 0, "1");
        pow_helper("a^2", 1, "a^2");
        pow_helper("a^2", 2, "a^4");
        pow_helper("a^2", 3, "a^6");

        pow_helper("x^2*y*z^3", 0, "1");
        pow_helper("x^2*y*z^3", 1, "x^2*y*z^3");
        pow_helper("x^2*y*z^3", 2, "x^4*y^2*z^6");
        pow_helper("x^2*y*z^3", 3, "x^6*y^3*z^9");

        pow_helper("ooo", 0, "1");
        pow_helper("ooo", 1, "ooo");
        pow_helper("ooo", 2, "ooo^2");
        pow_helper("ooo", 3, "ooo^3");

        pow_fail_helper("1", -1);
        pow_fail_helper("x^2*y*z^3", -1);
    }

    private static void apply_BigInteger_helper(@NotNull String m, @NotNull String xs, @NotNull String output) {
        aeq(readStrict(m).get().applyBigInteger(readVariableBigIntegerMap(xs)), output);
    }

    private static void apply_BigInteger_fail_helper(@NotNull String m, @NotNull String xs) {
        try {
            readStrict(m).get().applyBigInteger(readVariableBigIntegerMapWithNulls(xs));
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
        apply_BigInteger_helper("x^2*y*z^3", "[(x, -5), (y, 2), (z, 3)]", "1350");
        apply_BigInteger_helper("x^2*y*z^3", "[(x, -5), (y, 0), (z, 3), (ooo, 3)]", "0");

        apply_BigInteger_fail_helper("a", "[]");
        apply_BigInteger_fail_helper("a", "[(b, 2)]");
        apply_BigInteger_fail_helper("x^2*y*z^3", "[(x, -5), (y, 2)]");
        apply_BigInteger_fail_helper("x^2*y*z^3", "[(x, -5), (y, 2), (z, null)]");
        apply_BigInteger_fail_helper("x^2*y*z^3", "[(x, -5), (y, 2), (null, 1)]");
    }

    private static void apply_Rational_helper(@NotNull String m, @NotNull String xs, @NotNull String output) {
        aeq(readStrict(m).get().applyRational(readVariableRationalMap(xs)), output);
    }

    private static void apply_Rational_fail_helper(@NotNull String m, @NotNull String xs) {
        try {
            readStrict(m).get().applyRational(readVariableRationalMapWithNulls(xs));
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
        apply_Rational_helper("x^2*y*z^3", "[(x, -5/3), (y, 1/2), (z, 3)]", "75/2");
        apply_Rational_helper("x^2*y*z^3", "[(x, -5/3), (y, 0), (z, 3), (ooo, 3)]", "0");

        apply_Rational_fail_helper("a", "[]");
        apply_Rational_fail_helper("a", "[(b, 1/2)]");
        apply_Rational_fail_helper("x^2*y*z^3", "[(x, -5/3), (y, 1/2)]");
        apply_Rational_fail_helper("x^2*y*z^3", "[(x, -5/3), (y, 1/2), (z, null)]");
        apply_Rational_fail_helper("x^2*y*z^3", "[(x, -5/3), (y, 1/2), (null, 1)]");
    }

    private static void substitute_helper(@NotNull String m, @NotNull String xs, @NotNull String output) {
        aeq(readStrict(m).get().substitute(readVariableMonomialMap(xs)), output);
    }

    private static void substitute_fail_helper(@NotNull String m, @NotNull String xs) {
        try {
            readStrict(m).get().substitute(readVariableMonomialMapWithNulls(xs));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testSubstitute() {
        substitute_helper("1", "[]", "1");
        substitute_helper("1", "[(a, b)]", "1");
        substitute_helper("a", "[(a, 1)]", "1");
        substitute_helper("a", "[(a, a)]", "a");
        substitute_helper("a", "[(a, b)]", "b");
        substitute_helper("a", "[(a, x*z)]", "x*z");
        substitute_helper("a", "[(a, x*z), (b, a)]", "x*z");
        substitute_helper("x^2*y*z^3", "[]", "x^2*y*z^3");
        substitute_helper("x^2*y*z^3", "[(x, 1)]", "y*z^3");
        substitute_helper("x^2*y*z^3", "[(x, y), (y, z), (z, x)]", "x^3*y^2*z");
        substitute_helper("x^2*y*z^3", "[(x, xx), (y, yy), (z, zz)]", "xx^2*yy*zz^3");
        substitute_helper("x^2*y*z^3", "[(x, a*b), (y, a^2), (z, a^3*b^5)]", "a^13*b^17");

        substitute_fail_helper("x^2*y*z^3", "[(x, y), (y, z), (z, null)]");
        substitute_fail_helper("x^2*y*z^3", "[(x, y), (y, z), (null, x)]");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readMonomialList("[1, a, a^2, a^3, x^2*y*z^3]"),
                readMonomialList("[1, a, a^2, a^3, x^2*y*z^3]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(readStrict(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("1", 1);
        hashCode_helper("a", 32);
        hashCode_helper("a^2", 33);
        hashCode_helper("a^3", 34);
        hashCode_helper("x^2*y*z^3", 961615973);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readMonomialList("[1, ooo, b, a, z^2, y^2, x*y, x^2, x*y^2, x^2*y, x^3, x^2*z^2, x*y^2*z, a*b*c*d]")
        );
    }

    private static void readStrict_helper(@NotNull String input) {
        aeq(readStrict(input).get(), input);
    }

    private static void readStrict_fail_helper(@NotNull String input) {
        assertFalse(readStrict(input).isPresent());
    }

    @Test
    public void testReadStrict() {
        readStrict_helper("1");
        readStrict_helper("a");
        readStrict_helper("a^2");
        readStrict_helper("a^3");
        readStrict_helper("x^2*y*z^3");
        readStrict_helper("ooo");
        readStrict_fail_helper("");
        readStrict_fail_helper(" ");
        readStrict_fail_helper("ab");
        readStrict_fail_helper("3*a");
        readStrict_fail_helper("a^0");
        readStrict_fail_helper("a^1");
        readStrict_fail_helper("a^-1");
        readStrict_fail_helper("b*a");
        readStrict_fail_helper("a*a");
        readStrict_fail_helper("123");
        readStrict_fail_helper("aa*");
        readStrict_fail_helper("*aa");
    }

    private static @NotNull List<Integer> readIntegerList(@NotNull String s) {
        return Readers.readListStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Readers::readIntegerStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableList(@NotNull String s) {
        return Readers.readListStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<Variable> readVariableListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Variable::readStrict).apply(s).get();
    }

    private static @NotNull List<Pair<Variable, Integer>> readVariableIntegerPairList(@NotNull String s) {
        return Readers.readListStrict(
                u -> Pair.read(
                        u,
                        t -> NullableOptional.fromOptional(Variable.readStrict(t)),
                        t -> NullableOptional.fromOptional(Readers.readIntegerStrict(t))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<Variable, Integer>> readVariableIntegerPairListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(
                u -> Pair.read(
                        u,
                        Readers.readWithNullsStrict(Variable::readStrict),
                        Readers.readWithNullsStrict(Readers::readIntegerStrict)
                )
        ).apply(s).get();
    }

    private static @NotNull Map<Variable, BigInteger> readVariableBigIntegerMap(@NotNull String s) {
        return IterableUtils.toMap(
                Readers.readListStrict(
                        u -> Pair.read(
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
                        u -> Pair.read(
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
                        u -> Pair.read(
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
                        u -> Pair.read(
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
                        u -> Pair.read(
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
                        u -> Pair.read(
                                u,
                                Readers.readWithNullsStrict(Variable::readStrict),
                                Readers.readWithNullsStrict(Monomial::readStrict)
                        )
                ).apply(s).get()
        );
    }

    private static @NotNull List<Monomial> readMonomialList(@NotNull String s) {
        return Readers.readListStrict(Monomial::readStrict).apply(s).get();
    }

    private static @NotNull List<Monomial> readMonomialListWithNulls(@NotNull String s) {
        return Readers.readListWithNullsStrict(Monomial::readStrict).apply(s).get();
    }
}
