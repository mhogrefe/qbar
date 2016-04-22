package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.ExponentVector.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class ExponentVectorTest {
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

    private static void exponent_helper(@NotNull String ev, @NotNull String v, int output) {
        aeq(readStrict(ev).get().exponent(Variable.readStrict(v).get()), output);
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

    private static void size_helper(@NotNull String ev, int output) {
        aeq(readStrict(ev).get().size(), output);
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

    private static void of_helper(@NotNull String input, @NotNull String output) {
        aeq(of(readIntegerList(input)), output);
    }

    private static void of_fail_helper(@NotNull String input) {
        try {
            of(readIntegerListWithNulls(input));
            fail();
        } catch (IllegalArgumentException | NullPointerException ignored) {}
    }

    @Test
    public void testOf() {
        of_helper("[]", "1");
        of_helper("[0]", "1");
        of_helper("[0, 0, 0]", "1");
        of_helper("[1]", "a");
        of_helper("[0, 0, 0, 4]", "d^4");
        of_helper("[1, 2, 3, 4, 5]", "a*b^2*c^3*d^4*e^5");
        of_helper("[1, 2, 3, 4, 5, 0]", "a*b^2*c^3*d^4*e^5");

        of_fail_helper("[1, -1, 3]");
        of_fail_helper("[1, null, 3]");
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

    private static void degree_helper(@NotNull String ev, int output) {
        aeq(readStrict(ev).get().degree(), output);
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

    private static void removeVariable_helper(@NotNull String ev, @NotNull String v, @NotNull String output) {
        aeq(readStrict(ev).get().removeVariable(Variable.readStrict(v).get()), output);
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

    private static void removeVariables_helper(@NotNull String ev, @NotNull String vs, @NotNull String output) {
        aeq(readStrict(ev).get().removeVariables(readVariableList(vs)), output);
    }

    private static void removeVariables_fail_helper(@NotNull String ev, @NotNull String vs) {
        try {
            readStrict(ev).get().removeVariables(readVariableListWithNulls(vs));
            fail();
        } catch (NullPointerException ignored) {}
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

    @Test
    public void testEquals() {
        testEqualsHelper(
                readExponentVectorList("[1, a, a^2, a^3, x^2*y*z^3]"),
                readExponentVectorList("[1, a, a^2, a^3, x^2*y*z^3]")
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
                readExponentVectorList(
                        "[1, ooo, b, a, z^2, y^2, x*y, x^2, x*y^2, x^2*y, x^3, x^2*z^2, x*y^2*z, a*b*c*d]"
                )
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

    private static @NotNull List<ExponentVector> readExponentVectorList(@NotNull String s) {
        return Readers.readListStrict(ExponentVector::readStrict).apply(s).get();
    }
}
