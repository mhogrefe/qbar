package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

import static mho.qbar.objects.ExponentVector.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;

public class ExponentVectorTest {
    @Test
    public void testConstants() {
        aeq(ONE, "1");
    }

    private static void getExponents_helper(@NotNull String x, @NotNull String output) {
        aeq(read(x).get().getExponents(), output);
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
        aeq(read(ev).get().exponent(Variable.read(v).get()), output);
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
        aeq(read(ev).get().size(), output);
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
        aeqit(read(input).get().terms(), output);
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
        fromTerms_helper("[(x, 2), (y, 1), (z, 3)]", "x^2*y*z^3");

        fromTerms_fail_helper("[null]");
        fromTerms_fail_helper("[(a, 1), null]");
        fromTerms_fail_helper("[(a, null)]");
        fromTerms_fail_helper("[(null, 1)]");
        fromTerms_fail_helper("[(a, -1)]");
        fromTerms_fail_helper("[(b, 2), (b, 3)]");
        fromTerms_fail_helper("[(b, 2), (a, 3)]");
    }

    private static void degree_helper(@NotNull String ev, int output) {
        aeq(read(ev).get().degree(), output);
    }

    @Test
    public void testDegree() {
        degree_helper("1", 0);
        degree_helper("a", 1);
        degree_helper("a^2", 2);
        degree_helper("a^3", 3);
        degree_helper("x^2*y*z^3", 6);
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readExponentVectorList("[1, a, a^2, a^3, x^2*y*z^3]"),
                readExponentVectorList("[1, a, a^2, a^3, x^2*y*z^3]")
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
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

    private static void read_helper(@NotNull String input) {
        aeq(read(input).get(), input);
    }

    private static void read_fail_helper(@NotNull String input) {
        assertFalse(read(input).isPresent());
    }

    @Test
    public void testRead() {
        read_helper("1");
        read_helper("a");
        read_helper("a^2");
        read_helper("a^3");
        read_helper("x^2*y*z^3");
        read_helper("ooo");
        read_fail_helper("");
        read_fail_helper(" ");
        read_fail_helper("ab");
        read_fail_helper("3*a");
        read_fail_helper("a^0");
        read_fail_helper("a^1");
        read_fail_helper("a^-1");
        read_fail_helper("b*a");
        read_fail_helper("a*a");
        read_fail_helper("123");
    }

    private static void findIn_helper(@NotNull String input, @NotNull String output, int index) {
        Pair<ExponentVector, Integer> result = findIn(input).get();
        aeq(result.a, output);
        aeq(result.b, index);
    }

    private static void findIn_fail_helper(@NotNull String input) {
        assertFalse(findIn(input).isPresent());
    }

    @Test
    public void testFindIn() {
        findIn_helper("hello", "h", 0);
        findIn_helper("b*a*b", "b", 0);
        findIn_helper("3*a*b", "a*b", 2);
        findIn_helper("2*a^2a", "a^2", 2);
        findIn_helper("123", "1", 0);
        findIn_fail_helper("");
        findIn_fail_helper("234");
    }

    private static @NotNull List<Integer> readIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<Integer> readIntegerListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Readers::readInteger).apply(s).get();
    }

    private static @NotNull List<Pair<Variable, Integer>> readVariableIntegerPairList(@NotNull String s) {
        return Readers.readList(
                u -> Pair.read(
                        u,
                        t -> NullableOptional.fromOptional(Variable.read(t)),
                        t -> NullableOptional.fromOptional(Readers.readInteger(t))
                )
        ).apply(s).get();
    }

    private static @NotNull List<Pair<Variable, Integer>> readVariableIntegerPairListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(
                u -> Pair.read(u, Readers.readWithNulls(Variable::read), Readers.readWithNulls(Readers::readInteger))
        ).apply(s).get();
    }

    private static @NotNull List<ExponentVector> readExponentVectorList(@NotNull String s) {
        return Readers.readList(ExponentVector::read).apply(s).get();
    }
}
