package mho.qbar.objects;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static mho.qbar.objects.Interval.*;
import static mho.qbar.objects.Interval.sum;
import static mho.wheels.iterables.IterableUtils.iterate;
import static mho.wheels.iterables.IterableUtils.toList;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class IntervalTest {
    @Test
    public void testConstants() {
        aeq(ZERO, "[0, 0]");
        aeq(ONE, "[1, 1]");
        aeq(ALL, "(-Infinity, Infinity)");
    }

    private static void getLower_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().getLower(), output);
    }

    @Test
    public void testGetLower() {
        getLower_helper("[0, 0]", "Optional[0]");
        getLower_helper("[1, 1]", "Optional[1]");
        getLower_helper("[-2, 5/3]", "Optional[-2]");
        getLower_helper("(-Infinity, Infinity)", "Optional.empty");
    }

    private static void getUpper_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().getUpper(), output);
    }

    @Test
    public void testGetUpper() {
        getUpper_helper("[0, 0]", "Optional[0]");
        getUpper_helper("[1, 1]", "Optional[1]");
        getUpper_helper("[-2, 5/3]", "Optional[5/3]");
        getUpper_helper("(-Infinity, Infinity)", "Optional.empty");
    }

    private static void of_Rational_Rational_helper(
            @NotNull String lower,
            @NotNull String upper,
            @NotNull String output
    ) {
        aeq(of(Rational.read(lower).get(), Rational.read(upper).get()), output);
    }

    private static void of_Rational_Rational_fail_helper(@NotNull String lower, @NotNull String upper) {
        try {
            of(Rational.read(lower).get(), Rational.read(upper).get());
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testOf_Rational_Rational() {
        of_Rational_Rational_helper("1/3", "1/2", "[1/3, 1/2]");
        of_Rational_Rational_helper("-5", "0", "[-5, 0]");
        of_Rational_Rational_helper("2", "2", "[2, 2]");
        of_Rational_Rational_fail_helper("3", "2");
    }

    private static void lessThanOrEqualTo_helper(@NotNull String upper, @NotNull String output) {
        aeq(lessThanOrEqualTo(Rational.read(upper).get()), output);
    }

    @Test
    public void testLessThanOrEqualTo() {
        lessThanOrEqualTo_helper("1/3", "(-Infinity, 1/3]");
        lessThanOrEqualTo_helper("0", "(-Infinity, 0]");
    }

    private static void greaterThanOrEqualTo_helper(@NotNull String lower, @NotNull String output) {
        aeq(greaterThanOrEqualTo(Rational.read(lower).get()), output);
    }

    @Test
    public void testGreaterThanOrEqualTo() {
        greaterThanOrEqualTo_helper("1/3", "[1/3, Infinity)");
        greaterThanOrEqualTo_helper("0", "[0, Infinity)");
    }

    private static void of_Rational_helper(@NotNull String input, @NotNull String output) {
        aeq(of(Rational.read(input).get()), output);
    }

    @Test
    public void testOf_Rational() {
        of_Rational_helper("0", "[0, 0]");
        of_Rational_helper("5/4", "[5/4, 5/4]");
        of_Rational_helper("2", "[2, 2]");
    }

    private static void bitLength_helper(@NotNull String input, int output) {
        aeq(read(input).get().bitLength(), output);
    }

    @Test
    public void testBitLength() {
        bitLength_helper("[0, 0]", 2);
        bitLength_helper("[1, 1]", 4);
        bitLength_helper("(-Infinity, Infinity)", 0);
        bitLength_helper("[-2, 5/3]", 8);
        bitLength_helper("[4, 4]", 8);
        bitLength_helper("(-Infinity, 3/2]", 4);
        bitLength_helper("[-6, Infinity)", 4);
    }

    private static void isFinitelyBounded_helper(@NotNull String input, boolean output) {
        aeq(read(input).get().isFinitelyBounded(), output);
    }

    @Test
    public void testIsFinitelyBounded() {
        isFinitelyBounded_helper("[0, 0]", true);
        isFinitelyBounded_helper("[1, 1]", true);
        isFinitelyBounded_helper("(-Infinity, Infinity)", false);
        isFinitelyBounded_helper("[-2, 5/3]", true);
        isFinitelyBounded_helper("[4, 4]", true);
        isFinitelyBounded_helper("(-Infinity, 3/2]", false);
        isFinitelyBounded_helper("[-6, Infinity)", false);
    }

    private static void contains_Rational_helper(@NotNull String a, @NotNull String r, boolean output) {
        aeq(read(a).get().contains(Rational.read(r).get()), output);
    }

    @Test
    public void testContains_Rational() {
        contains_Rational_helper("[0, 0]", "0", true);
        contains_Rational_helper("[0, 0]", "1", false);

        contains_Rational_helper("[1, 1]", "1", true);
        contains_Rational_helper("[1, 1]", "0", false);

        contains_Rational_helper("(-Infinity, Infinity)", "1", true);
        contains_Rational_helper("(-Infinity, Infinity)", "-4/3", true);

        contains_Rational_helper("[-2, 5/3]", "-2", true);
        contains_Rational_helper("[-2, 5/3]", "-1", true);
        contains_Rational_helper("[-2, 5/3]", "0", true);
        contains_Rational_helper("[-2, 5/3]", "1", true);
        contains_Rational_helper("[-2, 5/3]", "5/3", true);
        contains_Rational_helper("[-2, 5/3]", "-3", false);
        contains_Rational_helper("[-2, 5/3]", "2", false);

        contains_Rational_helper("[4, 4]", "4", true);
        contains_Rational_helper("[4, 4]", "3", false);
        contains_Rational_helper("[4, 4]", "5", false);

        contains_Rational_helper("(-Infinity, 3/2]", "0", true);
        contains_Rational_helper("(-Infinity, 3/2]", "1", true);
        contains_Rational_helper("(-Infinity, 3/2]", "-10", true);
        contains_Rational_helper("(-Infinity, 3/2]", "3/2", true);
        contains_Rational_helper("(-Infinity, 3/2]", "2", false);

        contains_Rational_helper("[-6, Infinity)", "0", true);
        contains_Rational_helper("[-6, Infinity)", "1", true);
        contains_Rational_helper("[-6, Infinity)", "-4", true);
        contains_Rational_helper("[-6, Infinity)", "5", true);
        contains_Rational_helper("[-6, Infinity)", "-8", false);
    }

    private static void contains_Interval_helper(@NotNull String a, @NotNull String b, boolean output) {
        aeq(read(a).get().contains(read(b).get()), output);
    }

    @Test
    public void testContains_Interval() {
        contains_Interval_helper("[0, 0]", "[0, 0]", true);
        contains_Interval_helper("[0, 0]", "[1, 1]", false);
        contains_Interval_helper("[0, 0]", "(-Infinity, Infinity)", false);

        contains_Interval_helper("[1, 1]", "[1, 1]", true);
        contains_Interval_helper("[1, 1]", "[0, 0]", false);
        contains_Interval_helper("[1, 1]", "(-Infinity, Infinity)", false);

        contains_Interval_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", true);
        contains_Interval_helper("(-Infinity, Infinity)", "[0, 0]", true);
        contains_Interval_helper("(-Infinity, Infinity)", "[1, 1]", true);

        contains_Interval_helper("[1, 4]", "[2, 3]", true);
        contains_Interval_helper("[1, 4]", "[1, 4]", true);
        contains_Interval_helper("[1, 4]", "[0, 2]", false);

        contains_Interval_helper("(-Infinity, 1/2]", "(-Infinity, 0]", true);
        contains_Interval_helper("(-Infinity, 1/2]", "[0, 0]", true);
        contains_Interval_helper("(-Infinity, 1/2]", "(-Infinity, 1]", false);

        contains_Interval_helper("[1/2, Infinity)", "[1, Infinity)", true);
        contains_Interval_helper("[1/2, Infinity)", "[1, 1]", true);
        contains_Interval_helper("[1/2, Infinity)", "[0, Infinity)", false);
        contains_Interval_helper("[1/2, Infinity)", "(-Infinity, 1/2]", false);
    }

    private static void diameter_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().diameter().get(), output);
    }

    private static void diameter_fail_helper(@NotNull String input) {
        assertFalse(read(input).get().diameter().isPresent());
    }

    @Test
    public void testDiameter() {
        diameter_helper("[0, 0]", "0");
        diameter_helper("[1, 1]", "0");
        diameter_fail_helper("(-Infinity, Infinity)");
        diameter_helper("[-2, 5/3]", "11/3");
        diameter_helper("[4, 4]", "0");
        diameter_fail_helper("(-Infinity, 3/2]");
        diameter_fail_helper("[-6, Infinity)");
    }

    private static void convexHull_Interval_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().convexHull(read(b).get()), output);
    }

    @Test
    public void testConvexHull_Interval() {
        convexHull_Interval_helper("[0, 0]", "[0, 0]", "[0, 0]");
        convexHull_Interval_helper("[0, 0]", "[1, 1]", "[0, 1]");
        convexHull_Interval_helper("[0, 0]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("[0, 0]", "[-2, 5/3]", "[-2, 5/3]");
        convexHull_Interval_helper("[0, 0]", "[4, 4]", "[0, 4]");
        convexHull_Interval_helper("[0, 0]", "(-Infinity, 3/2]", "(-Infinity, 3/2]");
        convexHull_Interval_helper("[0, 0]", "[-6, Infinity)", "[-6, Infinity)");

        convexHull_Interval_helper("[1, 1]", "[0, 0]", "[0, 1]");
        convexHull_Interval_helper("[1, 1]", "[1, 1]", "[1, 1]");
        convexHull_Interval_helper("[1, 1]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("[1, 1]", "[-2, 5/3]", "[-2, 5/3]");
        convexHull_Interval_helper("[1, 1]", "[4, 4]", "[1, 4]");
        convexHull_Interval_helper("[1, 1]", "(-Infinity, 3/2]", "(-Infinity, 3/2]");
        convexHull_Interval_helper("[1, 1]", "[-6, Infinity)", "[-6, Infinity)");

        convexHull_Interval_helper("(-Infinity, Infinity)", "[0, 0]", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, Infinity)", "[1, 1]", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, Infinity)", "[-2, 5/3]", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, Infinity)", "[4, 4]", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, Infinity)", "[-6, Infinity)", "(-Infinity, Infinity)");

        convexHull_Interval_helper("[-2, 5/3]", "[0, 0]", "[-2, 5/3]");
        convexHull_Interval_helper("[-2, 5/3]", "[1, 1]", "[-2, 5/3]");
        convexHull_Interval_helper("[-2, 5/3]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("[-2, 5/3]", "[-2, 5/3]", "[-2, 5/3]");
        convexHull_Interval_helper("[-2, 5/3]", "[4, 4]", "[-2, 4]");
        convexHull_Interval_helper("[-2, 5/3]", "(-Infinity, 3/2]", "(-Infinity, 5/3]");
        convexHull_Interval_helper("[-2, 5/3]", "[-6, Infinity)", "[-6, Infinity)");

        convexHull_Interval_helper("[4, 4]", "[0, 0]", "[0, 4]");
        convexHull_Interval_helper("[4, 4]", "[1, 1]", "[1, 4]");
        convexHull_Interval_helper("[4, 4]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("[4, 4]", "[-2, 5/3]", "[-2, 4]");
        convexHull_Interval_helper("[4, 4]", "[4, 4]", "[4, 4]");
        convexHull_Interval_helper("[4, 4]", "(-Infinity, 3/2]", "(-Infinity, 4]");
        convexHull_Interval_helper("[4, 4]", "[-6, Infinity)", "[-6, Infinity)");

        convexHull_Interval_helper("(-Infinity, 3/2]", "[0, 0]", "(-Infinity, 3/2]");
        convexHull_Interval_helper("(-Infinity, 3/2]", "[1, 1]", "(-Infinity, 3/2]");
        convexHull_Interval_helper("(-Infinity, 3/2]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("(-Infinity, 3/2]", "[-2, 5/3]", "(-Infinity, 5/3]");
        convexHull_Interval_helper("(-Infinity, 3/2]", "[4, 4]", "(-Infinity, 4]");
        convexHull_Interval_helper("(-Infinity, 3/2]", "(-Infinity, 3/2]", "(-Infinity, 3/2]");
        convexHull_Interval_helper("(-Infinity, 3/2]", "[-6, Infinity)", "(-Infinity, Infinity)");

        convexHull_Interval_helper("[-6, Infinity)", "[0, 0]", "[-6, Infinity)");
        convexHull_Interval_helper("[-6, Infinity)", "[1, 1]", "[-6, Infinity)");
        convexHull_Interval_helper("[-6, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        convexHull_Interval_helper("[-6, Infinity)", "[-2, 5/3]", "[-6, Infinity)");
        convexHull_Interval_helper("[-6, Infinity)", "[4, 4]", "[-6, Infinity)");
        convexHull_Interval_helper("[-6, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        convexHull_Interval_helper("[-6, Infinity)", "[-6, Infinity)", "[-6, Infinity)");
    }

    private static void convexHull_List_Interval_helper(@NotNull String input, @NotNull String output) {
        aeq(convexHull(readIntervalList(input)), output);
    }

    private static void convexHull_List_Interval_fail_helper(@NotNull String input) {
        try {
            convexHull(readIntervalListWithNulls(input));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testConvexHull_List_Interval() {
        convexHull_List_Interval_helper("[[0, 0]]", "[0, 0]");
        convexHull_List_Interval_helper("[[-1, 2]]", "[-1, 2]");
        convexHull_List_Interval_helper("[[-1, Infinity)]", "[-1, Infinity)");
        convexHull_List_Interval_helper("[(-Infinity, 4]]", "(-Infinity, 4]");
        convexHull_List_Interval_helper("[(-Infinity, Infinity)]", "(-Infinity, Infinity)");
        convexHull_List_Interval_helper("[[0, 0], [1, 1]]", "[0, 1]");
        convexHull_List_Interval_helper("[[1, 2], [3, 4]]", "[1, 4]");
        convexHull_List_Interval_helper("[[1, 3], [2, 4]]", "[1, 4]");
        convexHull_List_Interval_helper("[(-Infinity, Infinity), [3, 4]]", "(-Infinity, Infinity)");
        convexHull_List_Interval_helper("[[-1, Infinity), (-Infinity, 4]]", "(-Infinity, Infinity)");
        convexHull_List_Interval_helper("[[1, 2], [3, 4], [5, 6]]", "[1, 6]");
        convexHull_List_Interval_helper("[[1, 2], [2, 2], [3, Infinity)]", "[1, Infinity)");
        convexHull_List_Interval_fail_helper("[]");
        convexHull_List_Interval_fail_helper("[[1, 2], null]");
    }

    private static void intersection_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().intersection(read(b).get()).get(), output);
    }

    private static void intersection_empty_helper(@NotNull String a, @NotNull String b) {
        assertFalse(read(a).get().intersection(read(b).get()).isPresent());
    }

    @Test
    public void testIntersection() {
        intersection_helper("[0, 0]", "[0, 0]", "[0, 0]");
        intersection_helper("[1, 1]", "[1, 1]", "[1, 1]");
        intersection_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        intersection_helper("(-Infinity, Infinity)", "[0, 0]", "[0, 0]");
        intersection_helper("(-Infinity, Infinity)", "[1, 1]", "[1, 1]");
        intersection_empty_helper("[0, 0]", "[1, 1]");
        intersection_helper("[1, 3]", "[2, 4]", "[2, 3]");
        intersection_helper("[1, 2]", "[2, 4]", "[2, 2]");
        intersection_empty_helper("[1, 2]", "[3, 4]");
        intersection_helper("(-Infinity, Infinity)", "[1, 2]", "[1, 2]");
        intersection_helper("(-Infinity, 2]", "[1, 3]", "[1, 2]");
        intersection_helper("(-Infinity, 2]", "(-Infinity, 3]", "(-Infinity, 2]");
        intersection_helper("[2, Infinity)", "[1, 3]", "[2, 3]");
        intersection_helper("[2, Infinity)", "[3, Infinity)", "[3, Infinity)");
        intersection_helper("[2, Infinity)", "(-Infinity, 3]", "[2, 3]");
        intersection_empty_helper("[2, Infinity)", "(-Infinity, 1]");
    }

    private static void disjoint_helper(@NotNull String a, @NotNull String b, boolean output) {
        aeq(read(a).get().disjoint(read(b).get()), output);
    }

    @Test
    public void testDisjoint() {
        disjoint_helper("[0, 0]", "[0, 0]", false);
        disjoint_helper("[1, 1]", "[1, 1]", false);
        disjoint_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", false);
        disjoint_helper("(-Infinity, Infinity)", "[0, 0]", false);
        disjoint_helper("(-Infinity, Infinity)", "[1, 1]", false);
        disjoint_helper("[0, 0]", "[1, 1]", true);
        disjoint_helper("[1, 3]", "[2, 4]", false);
        disjoint_helper("[1, 2]", "[3, 4]", true);
        disjoint_helper("(-Infinity, 2]", "[1, 3]", false);
        disjoint_helper("(-Infinity, 2]", "[3, 4]", true);
        disjoint_helper("[2, Infinity)", "[1, 3]", false);
        disjoint_helper("[2, Infinity)", "[0, 1]", true);
        disjoint_helper("[2, Infinity)", "(-Infinity, 1]", true);
        disjoint_helper("[2, Infinity)", "(-Infinity, 3]", false);
    }

    private static void union_helper(@NotNull String input, @NotNull String output) {
        aeq(union(readIntervalList(input)), output);
    }

    private static void union_fail_helper(@NotNull String input) {
        try {
            union(readIntervalListWithNulls(input));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testUnion() {
        union_helper("[]", "[]");
        union_helper("[[0, 0]]", "[[0, 0]]");
        union_helper("[[1, 1]]", "[[1, 1]]");
        union_helper("[(-Infinity, Infinity)]", "[(-Infinity, Infinity)]");
        union_helper("[(-Infinity, 3]]", "[(-Infinity, 3]]");
        union_helper("[[2, Infinity)]", "[[2, Infinity)]");
        union_helper("[[2, 3]]", "[[2, 3]]");
        union_helper("[(-Infinity, 3], [4, 5], [6, 7]]", "[(-Infinity, 3], [4, 5], [6, 7]]");
        union_helper("[(-Infinity, 3], [3, 5], [5, 7]]", "[(-Infinity, 7]]");
        union_helper("[(-Infinity, 3], [2, 5], [6, 7]]", "[(-Infinity, 5], [6, 7]]");
        union_helper("[(-Infinity, 3], [2, 5], [2, 6], [6, 7]]", "[(-Infinity, 7]]");
        union_helper("[[1, 2], [4, 6], [10, Infinity), [3, 7], [5, 9]]", "[[1, 2], [3, 9], [10, Infinity)]");
        union_fail_helper("[[1, 3], null, [5, Infinity)]");
    }

    private static void complement_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().complement(), output);
    }

    @Test
    public void testComplement() {
        complement_helper("[0, 0]", "[(-Infinity, Infinity)]");
        complement_helper("[1, 1]", "[(-Infinity, Infinity)]");
        complement_helper("(-Infinity, Infinity)", "[]");
        complement_helper("[-2, 5/3]", "[(-Infinity, -2], [5/3, Infinity)]");
        complement_helper("[4, 4]", "[(-Infinity, Infinity)]");
        complement_helper("(-Infinity, 3/2]", "[[3/2, Infinity)]");
        complement_helper("[-6, Infinity)", "[(-Infinity, -6]]");
    }

    private static void midpoint_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().midpoint(), output);
    }

    private static void midpoint_fail_helper(@NotNull String input) {
        try {
            read(input).get().midpoint();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testMidpoint() {
        midpoint_helper("[0, 0]", "0");
        midpoint_helper("[1, 1]", "1");
        midpoint_helper("[4, 4]", "4");
        midpoint_helper("[1, 2]", "3/2");
        midpoint_helper("[-2, 5/3]", "-1/6");
        midpoint_fail_helper("(-Infinity, Infinity)");
        midpoint_fail_helper("(-Infinity, 1]");
        midpoint_fail_helper("[1, Infinity)");
    }

    private static void split_helper(@NotNull String a, @NotNull String x, @NotNull String output) {
        aeq(read(a).get().split(Rational.read(x).get()), output);
    }

    private static void split_fail_helper(@NotNull String a, @NotNull String x) {
        try {
            read(a).get().split(Rational.read(x).get());
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testSplit() {
        split_helper("[0, 0]", "0", "([0, 0], [0, 0])");
        split_helper("[1, 1]", "1", "([1, 1], [1, 1])");
        split_helper("(-Infinity, Infinity)", "1", "((-Infinity, 1], [1, Infinity))");
        split_helper("[4, 4]", "4", "([4, 4], [4, 4])");
        split_helper("[0, 1]", "1/3", "([0, 1/3], [1/3, 1])");
        split_helper("[0, 1]", "0", "([0, 0], [0, 1])");
        split_helper("[0, 1]", "1", "([0, 1], [1, 1])");
        split_helper("[-2, 5/3]", "1", "([-2, 1], [1, 5/3])");
        split_helper("(-Infinity, 1]", "-3", "((-Infinity, -3], [-3, 1])");
        split_helper("[5/3, Infinity)", "10", "([5/3, 10], [10, Infinity))");
        split_fail_helper("[0, 0]", "1");
        split_fail_helper("[-2, 5/3]", "-4");
        split_fail_helper("(-Infinity, 1]", "4");
        split_fail_helper("[1, Infinity)", "-4");
    }

    private static void bisect_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().bisect(), output);
    }

    private static void bisect_fail_helper(@NotNull String input) {
        try {
            read(input).get().bisect();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testBisect() {
        bisect_helper("[0, 0]", "([0, 0], [0, 0])");
        bisect_helper("[1, 1]", "([1, 1], [1, 1])");
        bisect_helper("[4, 4]", "([4, 4], [4, 4])");
        bisect_helper("[1, 2]", "([1, 3/2], [3/2, 2])");
        bisect_helper("[-2, 5/3]", "([-2, -1/6], [-1/6, 5/3])");
        bisect_fail_helper("(-Infinity, Infinity)");
        bisect_fail_helper("(-Infinity, 1]");
        bisect_fail_helper("[1, Infinity)");
    }

    private static void roundingPreimage_float_helper(float f, @NotNull String output) {
        aeq(roundingPreimage(f), output);
    }

    private static void roundingPreimage_float_fail_helper(float f) {
        try {
            roundingPreimage(f);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundingPreimage_float() {
        roundingPreimage_float_helper(
                0.0f,
                "[-1/1427247692705959881058285969449495136382746624, 1/1427247692705959881058285969449495136382746624]"
        );
        roundingPreimage_float_helper(
                -0.0f,
                "[-1/1427247692705959881058285969449495136382746624, 1/1427247692705959881058285969449495136382746624]"
        );
        roundingPreimage_float_helper(Float.POSITIVE_INFINITY, "[340282346638528859811704183484516925440, Infinity)");
        roundingPreimage_float_helper(
                Float.NEGATIVE_INFINITY,
                "(-Infinity, -340282346638528859811704183484516925440]"
        );
        roundingPreimage_float_helper(1.0f, "[33554431/33554432, 16777217/16777216]");
        roundingPreimage_float_helper(13.0f, "[27262975/2097152, 27262977/2097152]");
        roundingPreimage_float_helper(-5.0f, "[-20971521/4194304, -20971519/4194304]");
        roundingPreimage_float_helper(1.5f, "[25165823/16777216, 25165825/16777216]");
        roundingPreimage_float_helper(0.15625f, "[20971519/134217728, 20971521/134217728]");
        roundingPreimage_float_helper(0.1f, "[26843545/268435456, 26843547/268435456]");
        roundingPreimage_float_helper(1.0f / 3.0f, "[22369621/67108864, 22369623/67108864]");
        roundingPreimage_float_helper(1.0e10f, "[9999999488, 10000000512]");
        roundingPreimage_float_helper(1.0e30f, "[999999977268534356919527145472, 1000000052826398082833850564608]");
        roundingPreimage_float_helper((float) Math.PI, "[26353589/8388608, 26353591/8388608]");
        roundingPreimage_float_helper((float) Math.E, "[22802599/8388608, 22802601/8388608]");
        roundingPreimage_float_helper((float) Math.sqrt(2), "[23726565/16777216, 23726567/16777216]");
        roundingPreimage_float_helper(
                Float.MIN_VALUE,
                "[1/1427247692705959881058285969449495136382746624, 3/1427247692705959881058285969449495136382746624]"
        );
        roundingPreimage_float_helper(
                -Float.MIN_VALUE,
                "[-3/1427247692705959881058285969449495136382746624," +
                " -1/1427247692705959881058285969449495136382746624]"
        );
        roundingPreimage_float_helper(
                Float.MIN_NORMAL,
                "[16777215/1427247692705959881058285969449495136382746624," +
                " 16777217/1427247692705959881058285969449495136382746624]"
        );
        roundingPreimage_float_helper(
                -Float.MIN_NORMAL,
                "[-16777217/1427247692705959881058285969449495136382746624," +
                " -16777215/1427247692705959881058285969449495136382746624]"
        );
        roundingPreimage_float_helper(
                Float.MAX_VALUE,
                "[340282336497324057985868971510891282432, 340282346638528859811704183484516925440]"
        );
        roundingPreimage_float_helper(
                -Float.MAX_VALUE,
                "[-340282346638528859811704183484516925440, -340282336497324057985868971510891282432]"
        );
        roundingPreimage_float_fail_helper(Float.NaN);
    }

    private static void roundingPreimage_double_helper(double d, @NotNull String output) {
        aeq(roundingPreimage(d), output);
    }

    private static void roundingPreimage_double_fail_helper(double d) {
        try {
            roundingPreimage(d);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testRoundingPreimage_double() {
        roundingPreimage_double_helper(
                0.0,
                "[-1/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568," +
                " 1/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568]"
        );
        roundingPreimage_double_helper(
                -0.0,
                "[-1/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568," +
                " 1/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568]"
        );
        roundingPreimage_double_helper(
                Double.POSITIVE_INFINITY,
                "[17976931348623157081452742373170435679807056752584499659891747680315726078002853876058955863276687" +
                "817154045895351438246423432132688946418276846754670353751698604991057655128207624549009038932894407" +
                "586850845513394230458323690322294816580855933212334827479782620414472316873817718091929988125040402" +
                "6184124858368," +
                " Infinity)"
        );
        roundingPreimage_double_helper(
                Double.NEGATIVE_INFINITY,
                "(-Infinity," +
                " -1797693134862315708145274237317043567980705675258449965989174768031572607800285387605895586327668" +
                "781715404589535143824642343213268894641827684675467035375169860499105765512820762454900903893289440" +
                "758685084551339423045832369032229481658085593321233482747978262041447231687381771809192998812504040" +
                "26184124858368]"
        );
        roundingPreimage_double_helper(
                1.0,
                "[18014398509481983/18014398509481984, 9007199254740993/9007199254740992]"
        );
        roundingPreimage_double_helper(
                13.0,
                "[14636698788954111/1125899906842624, 14636698788954113/1125899906842624]"
        );
        roundingPreimage_double_helper(
                -5.0,
                "[-11258999068426241/2251799813685248, -11258999068426239/2251799813685248]"
        );
        roundingPreimage_double_helper(
                1.5,
                "[13510798882111487/9007199254740992, 13510798882111489/9007199254740992]"
        );
        roundingPreimage_double_helper(
                0.15625,
                "[11258999068426239/72057594037927936, 11258999068426241/72057594037927936]"
        );
        roundingPreimage_double_helper(
                0.1,
                "[14411518807585587/144115188075855872, 14411518807585589/144115188075855872]"
        );
        roundingPreimage_double_helper(
                1.0 / 3.0,
                "[12009599006321321/36028797018963968, 12009599006321323/36028797018963968]"
        );
        roundingPreimage_double_helper(1.0e10, "[10485759999999999/1048576, 10485760000000001/1048576]");
        roundingPreimage_double_helper(1.0e30, "[999999999999999949515880660992, 1000000000000000090253369016320]");
        roundingPreimage_double_helper(
                Math.PI,
                "[14148475504056879/4503599627370496, 14148475504056881/4503599627370496]"
        );
        roundingPreimage_double_helper(
                Math.E,
                "[12242053029736145/4503599627370496, 12242053029736147/4503599627370496]"
        );
        roundingPreimage_double_helper(
                Math.sqrt(2),
                "[12738103345051545/9007199254740992, 12738103345051547/9007199254740992]"
        );
        roundingPreimage_double_helper(
                Double.MIN_VALUE,
                "[1/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568," +
                " 3/404804506614621236704990693437834614099113299528284236713802716054860679135990693783920767402874" +
                "248990374155728633623822779617474771586953734026799881477019843034848553132722728933815484186432682" +
                "479535356945490137124014966849385397236206711298319112681620113024717539104666829230461005064372655" +
                "017292012526615415482186989568]"
        );
        roundingPreimage_double_helper(
                -Double.MIN_VALUE,
                "[-3/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568," +
                " -1/40480450661462123670499069343783461409911329952828423671380271605486067913599069378392076740287" +
                "424899037415572863362382277961747477158695373402679988147701984303484855313272272893381548418643268" +
                "247953535694549013712401496684938539723620671129831911268162011302471753910466682923046100506437265" +
                "5017292012526615415482186989568]"
        );
        roundingPreimage_double_helper(
                Double.MIN_NORMAL,
                "[9007199254740991/404804506614621236704990693437834614099113299528284236713802716054860679135990693" +
                "783920767402874248990374155728633623822779617474771586953734026799881477019843034848553132722728933" +
                "815484186432682479535356945490137124014966849385397236206711298319112681620113024717539104666829230" +
                "461005064372655017292012526615415482186989568," +
                " 9007199254740993/404804506614621236704990693437834614099113299528284236713802716054860679135990693" +
                "783920767402874248990374155728633623822779617474771586953734026799881477019843034848553132722728933" +
                "815484186432682479535356945490137124014966849385397236206711298319112681620113024717539104666829230" +
                "461005064372655017292012526615415482186989568]"
        );
        roundingPreimage_double_helper(
                -Double.MIN_NORMAL,
                "[-9007199254740993/40480450661462123670499069343783461409911329952828423671380271605486067913599069" +
                "378392076740287424899037415572863362382277961747477158695373402679988147701984303484855313272272893" +
                "381548418643268247953535694549013712401496684938539723620671129831911268162011302471753910466682923" +
                "0461005064372655017292012526615415482186989568," +
                " -9007199254740991/40480450661462123670499069343783461409911329952828423671380271605486067913599069" +
                "378392076740287424899037415572863362382277961747477158695373402679988147701984303484855313272272893" +
                "381548418643268247953535694549013712401496684938539723620671129831911268162011302471753910466682923" +
                "0461005064372655017292012526615415482186989568]"
        );
        roundingPreimage_double_helper(
                Double.MAX_VALUE,
                "[17976931348623156083532587605810529851620700234165216626166117462586955326729232657453009928794654" +
                "924675063149033587701752208710592698796290627760473556921329019091915239418047621712533496094635638" +
                "726128664019802903779951418360298151175628372777140383052148396392393563313364280213909166945792787" +
                "4464075218944," +
                " 17976931348623157081452742373170435679807056752584499659891747680315726078002853876058955863276687" +
                "817154045895351438246423432132688946418276846754670353751698604991057655128207624549009038932894407" +
                "586850845513394230458323690322294816580855933212334827479782620414472316873817718091929988125040402" +
                "6184124858368]"
        );
        roundingPreimage_double_helper(
                -Double.MAX_VALUE,
                "[-1797693134862315708145274237317043567980705675258449965989174768031572607800285387605895586327668" +
                "781715404589535143824642343213268894641827684675467035375169860499105765512820762454900903893289440" +
                "758685084551339423045832369032229481658085593321233482747978262041447231687381771809192998812504040" +
                "26184124858368," +
                " -1797693134862315608353258760581052985162070023416521662616611746258695532672923265745300992879465" +
                "492467506314903358770175220871059269879629062776047355692132901909191523941804762171253349609463563" +
                "872612866401980290377995141836029815117562837277714038305214839639239356331336428021390916694579278" +
                "74464075218944]"
        );
        roundingPreimage_double_fail_helper(Double.NaN);
    }

    private static void roundingPreimage_BigDecimal_helper(@NotNull String input, @NotNull String output) {
        aeq(roundingPreimage(Readers.readBigDecimal(input).get()), output);
    }

    @Test
    public void testRoundingPreimage_BigDecimal() {
        roundingPreimage_BigDecimal_helper("0", "[-1/2, 1/2]");
        roundingPreimage_BigDecimal_helper("1", "[1/2, 3/2]");
        roundingPreimage_BigDecimal_helper("3", "[5/2, 7/2]");
        roundingPreimage_BigDecimal_helper("-5", "[-11/2, -9/2]");
        roundingPreimage_BigDecimal_helper("0.1", "[1/20, 3/20]");
        roundingPreimage_BigDecimal_helper("3.14159", "[628317/200000, 628319/200000]");
        roundingPreimage_BigDecimal_helper(
                "-2.718281828459045",
                "[-5436563656918091/2000000000000000, -5436563656918089/2000000000000000]"
        );
        roundingPreimage_BigDecimal_helper("1E-14", "[1/200000000000000, 3/200000000000000]");
        roundingPreimage_BigDecimal_helper("1000000000000000", "[1999999999999999/2, 2000000000000001/2]");
        roundingPreimage_BigDecimal_helper("1E+15", "[500000000000000, 1500000000000000]");
    }

    private static void add_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().add(read(b).get()), output);
    }

    @Test
    public void testAdd() {
        add_helper("[0, 0]", "[0, 0]", "[0, 0]");
        add_helper("[0, 0]", "[1, 1]", "[1, 1]");
        add_helper("[0, 0]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("[0, 0]", "[-2, 5/3]", "[-2, 5/3]");
        add_helper("[0, 0]", "[4, 4]", "[4, 4]");
        add_helper("[0, 0]", "(-Infinity, 3/2]", "(-Infinity, 3/2]");
        add_helper("[0, 0]", "[-6, Infinity)", "[-6, Infinity)");

        add_helper("[1, 1]", "[0, 0]", "[1, 1]");
        add_helper("[1, 1]", "[1, 1]", "[2, 2]");
        add_helper("[1, 1]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("[1, 1]", "[-2, 5/3]", "[-1, 8/3]");
        add_helper("[1, 1]", "[4, 4]", "[5, 5]");
        add_helper("[1, 1]", "(-Infinity, 3/2]", "(-Infinity, 5/2]");
        add_helper("[1, 1]", "[-6, Infinity)", "[-5, Infinity)");

        add_helper("(-Infinity, Infinity)", "[0, 0]", "(-Infinity, Infinity)");
        add_helper("(-Infinity, Infinity)", "[1, 1]", "(-Infinity, Infinity)");
        add_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("(-Infinity, Infinity)", "[-2, 5/3]", "(-Infinity, Infinity)");
        add_helper("(-Infinity, Infinity)", "[4, 4]", "(-Infinity, Infinity)");
        add_helper("(-Infinity, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        add_helper("(-Infinity, Infinity)", "[-6, Infinity)", "(-Infinity, Infinity)");

        add_helper("[-2, 5/3]", "[0, 0]", "[-2, 5/3]");
        add_helper("[-2, 5/3]", "[1, 1]", "[-1, 8/3]");
        add_helper("[-2, 5/3]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("[-2, 5/3]", "[-2, 5/3]", "[-4, 10/3]");
        add_helper("[-2, 5/3]", "[4, 4]", "[2, 17/3]");
        add_helper("[-2, 5/3]", "(-Infinity, 3/2]", "(-Infinity, 19/6]");
        add_helper("[-2, 5/3]", "[-6, Infinity)", "[-8, Infinity)");

        add_helper("[4, 4]", "[0, 0]", "[4, 4]");
        add_helper("[4, 4]", "[1, 1]", "[5, 5]");
        add_helper("[4, 4]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("[4, 4]", "[-2, 5/3]", "[2, 17/3]");
        add_helper("[4, 4]", "[4, 4]", "[8, 8]");
        add_helper("[4, 4]", "(-Infinity, 3/2]", "(-Infinity, 11/2]");
        add_helper("[4, 4]", "[-6, Infinity)", "[-2, Infinity)");

        add_helper("(-Infinity, 3/2]", "[0, 0]", "(-Infinity, 3/2]");
        add_helper("(-Infinity, 3/2]", "[1, 1]", "(-Infinity, 5/2]");
        add_helper("(-Infinity, 3/2]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("(-Infinity, 3/2]", "[-2, 5/3]", "(-Infinity, 19/6]");
        add_helper("(-Infinity, 3/2]", "[4, 4]", "(-Infinity, 11/2]");
        add_helper("(-Infinity, 3/2]", "(-Infinity, 3/2]", "(-Infinity, 3]");
        add_helper("(-Infinity, 3/2]", "[-6, Infinity)", "(-Infinity, Infinity)");

        add_helper("[-6, Infinity)", "[0, 0]", "[-6, Infinity)");
        add_helper("[-6, Infinity)", "[1, 1]", "[-5, Infinity)");
        add_helper("[-6, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        add_helper("[-6, Infinity)", "[-2, 5/3]", "[-8, Infinity)");
        add_helper("[-6, Infinity)", "[4, 4]", "[-2, Infinity)");
        add_helper("[-6, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        add_helper("[-6, Infinity)", "[-6, Infinity)", "[-12, Infinity)");
    }

    private static void negate_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().negate(), output);
    }

    @Test
    public void testNegate() {
        negate_helper("[0, 0]", "[0, 0]");
        negate_helper("[1, 1]", "[-1, -1]");
        negate_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)");
        negate_helper("[-2, 5/3]", "[-5/3, 2]");
        negate_helper("[4, 4]", "[-4, -4]");
        negate_helper("(-Infinity, 3/2]", "[-3/2, Infinity)");
        negate_helper("[-6, Infinity)", "(-Infinity, 6]");
    }

    private static void abs_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().abs(), output);
    }

    @Test
    public void testAbs() {
        abs_helper("[0, 0]", "[0, 0]");
        abs_helper("[1, 1]", "[1, 1]");
        abs_helper("(-Infinity, Infinity)", "[0, Infinity)");
        abs_helper("[-2, 5/3]", "[0, 2]");
        abs_helper("[4, 4]", "[4, 4]");
        abs_helper("(-Infinity, 3/2]", "[0, Infinity)");
        abs_helper("(-Infinity, -3/2]", "[3/2, Infinity)");
        abs_helper("[-6, Infinity)", "[0, Infinity)");
        abs_helper("[6, Infinity)", "[6, Infinity)");
    }

    private static void signum_helper(@NotNull String input, @NotNull String output) {
        aeq(read(input).get().signum(), output);
    }

    @Test
    public void testSignum() {
        signum_helper("[0, 0]", "Optional[0]");
        signum_helper("[1, 1]", "Optional[1]");
        signum_helper("(-Infinity, Infinity)", "Optional.empty");
        signum_helper("[-2, 5/3]", "Optional.empty");
        signum_helper("[4, 4]", "Optional[1]");
        signum_helper("(-Infinity, 3/2]", "Optional.empty");
        signum_helper("(-Infinity, -3/2]", "Optional[-1]");
        signum_helper("[-6, Infinity)", "Optional.empty");
        signum_helper("[6, Infinity)", "Optional[1]");
    }

    private static void subtract_helper(@NotNull String a, @NotNull String b, @NotNull String output) {
        aeq(read(a).get().subtract(read(b).get()), output);
    }

    @Test
    public void testSubtract() {
        subtract_helper("[0, 0]", "[0, 0]", "[0, 0]");
        subtract_helper("[0, 0]", "[1, 1]", "[-1, -1]");
        subtract_helper("[0, 0]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("[0, 0]", "[-2, 5/3]", "[-5/3, 2]");
        subtract_helper("[0, 0]", "[4, 4]", "[-4, -4]");
        subtract_helper("[0, 0]", "(-Infinity, 3/2]", "[-3/2, Infinity)");
        subtract_helper("[0, 0]", "[-6, Infinity)", "(-Infinity, 6]");

        subtract_helper("[1, 1]", "[0, 0]", "[1, 1]");
        subtract_helper("[1, 1]", "[1, 1]", "[0, 0]");
        subtract_helper("[1, 1]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("[1, 1]", "[-2, 5/3]", "[-2/3, 3]");
        subtract_helper("[1, 1]", "[4, 4]", "[-3, -3]");
        subtract_helper("[1, 1]", "(-Infinity, 3/2]", "[-1/2, Infinity)");
        subtract_helper("[1, 1]", "[-6, Infinity)", "(-Infinity, 7]");

        subtract_helper("(-Infinity, Infinity)", "[0, 0]", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, Infinity)", "[1, 1]", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, Infinity)", "[-2, 5/3]", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, Infinity)", "[4, 4]", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, Infinity)", "[-6, Infinity)", "(-Infinity, Infinity)");

        subtract_helper("[-2, 5/3]", "[0, 0]", "[-2, 5/3]");
        subtract_helper("[-2, 5/3]", "[1, 1]", "[-3, 2/3]");
        subtract_helper("[-2, 5/3]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("[-2, 5/3]", "[-2, 5/3]", "[-11/3, 11/3]");
        subtract_helper("[-2, 5/3]", "[4, 4]", "[-6, -7/3]");
        subtract_helper("[-2, 5/3]", "(-Infinity, 3/2]", "[-7/2, Infinity)");
        subtract_helper("[-2, 5/3]", "[-6, Infinity)", "(-Infinity, 23/3]");

        subtract_helper("[4, 4]", "[0, 0]", "[4, 4]");
        subtract_helper("[4, 4]", "[1, 1]", "[3, 3]");
        subtract_helper("[4, 4]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("[4, 4]", "[-2, 5/3]", "[7/3, 6]");
        subtract_helper("[4, 4]", "[4, 4]", "[0, 0]");
        subtract_helper("[4, 4]", "(-Infinity, 3/2]", "[5/2, Infinity)");
        subtract_helper("[4, 4]", "[-6, Infinity)", "(-Infinity, 10]");

        subtract_helper("(-Infinity, 3/2]", "[0, 0]", "(-Infinity, 3/2]");
        subtract_helper("(-Infinity, 3/2]", "[1, 1]", "(-Infinity, 1/2]");
        subtract_helper("(-Infinity, 3/2]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, 3/2]", "[-2, 5/3]", "(-Infinity, 7/2]");
        subtract_helper("(-Infinity, 3/2]", "[4, 4]", "(-Infinity, -5/2]");
        subtract_helper("(-Infinity, 3/2]", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        subtract_helper("(-Infinity, 3/2]", "[-6, Infinity)", "(-Infinity, 15/2]");

        subtract_helper("[-6, Infinity)", "[0, 0]", "[-6, Infinity)");
        subtract_helper("[-6, Infinity)", "[1, 1]", "[-7, Infinity)");
        subtract_helper("[-6, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        subtract_helper("[-6, Infinity)", "[-2, 5/3]", "[-23/3, Infinity)");
        subtract_helper("[-6, Infinity)", "[4, 4]", "[-10, Infinity)");
        subtract_helper("[-6, Infinity)", "(-Infinity, 3/2]", "[-15/2, Infinity)");
        subtract_helper("[-6, Infinity)", "[-6, Infinity)", "(-Infinity, Infinity)");
    }

    private static void multiply_Interval_helper(@NotNull String a, @NotNull String b, @NotNull String result) {
        aeq(read(a).get().multiply(read(b).get()), result);
    }

    @Test
    public void testMultiply_Interval() {
        multiply_Interval_helper("[0, 0]", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("[0, 0]", "[1, 1]", "[0, 0]");
        multiply_Interval_helper("[0, 0]", "(-Infinity, Infinity)", "[0, 0]");
        multiply_Interval_helper("[0, 0]", "[-2, 5/3]", "[0, 0]");
        multiply_Interval_helper("[0, 0]", "[4, 4]", "[0, 0]");
        multiply_Interval_helper("[0, 0]", "(-Infinity, 3/2]", "[0, 0]");
        multiply_Interval_helper("[0, 0]", "[-6, Infinity)", "[0, 0]");

        multiply_Interval_helper("[1, 1]", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("[1, 1]", "[1, 1]", "[1, 1]");
        multiply_Interval_helper("[1, 1]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("[1, 1]", "[-2, 5/3]", "[-2, 5/3]");
        multiply_Interval_helper("[1, 1]", "[4, 4]", "[4, 4]");
        multiply_Interval_helper("[1, 1]", "(-Infinity, 3/2]", "(-Infinity, 3/2]");
        multiply_Interval_helper("[1, 1]", "[-6, Infinity)", "[-6, Infinity)");

        multiply_Interval_helper("(-Infinity, Infinity)", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("(-Infinity, Infinity)", "[1, 1]", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, Infinity)", "[-2, 5/3]", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, Infinity)", "[4, 4]", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, Infinity)", "[-6, Infinity)", "(-Infinity, Infinity)");

        multiply_Interval_helper("[-2, 5/3]", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("[-2, 5/3]", "[1, 1]", "[-2, 5/3]");
        multiply_Interval_helper("[-2, 5/3]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("[-2, 5/3]", "[-2, 5/3]", "[-10/3, 4]");
        multiply_Interval_helper("[-2, 5/3]", "[4, 4]", "[-8, 20/3]");
        multiply_Interval_helper("[-2, 5/3]", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        multiply_Interval_helper("[-2, 5/3]", "[-6, Infinity)", "(-Infinity, Infinity)");

        multiply_Interval_helper("[4, 4]", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("[4, 4]", "[1, 1]", "[4, 4]");
        multiply_Interval_helper("[4, 4]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("[4, 4]", "[-2, 5/3]", "[-8, 20/3]");
        multiply_Interval_helper("[4, 4]", "[4, 4]", "[16, 16]");
        multiply_Interval_helper("[4, 4]", "(-Infinity, 3/2]", "(-Infinity, 6]");
        multiply_Interval_helper("[4, 4]", "[-6, Infinity)", "[-24, Infinity)");

        multiply_Interval_helper("(-Infinity, 3/2]", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("(-Infinity, 3/2]", "[1, 1]", "(-Infinity, 3/2]");
        multiply_Interval_helper("(-Infinity, 3/2]", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, 3/2]", "[-2, 5/3]", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, 3/2]", "[4, 4]", "(-Infinity, 6]");
        multiply_Interval_helper("(-Infinity, 3/2]", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, 3/2]", "[-6, Infinity)", "(-Infinity, Infinity)");

        multiply_Interval_helper("[-6, Infinity)", "[0, 0]", "[0, 0]");
        multiply_Interval_helper("[-6, Infinity)", "[1, 1]", "[-6, Infinity)");
        multiply_Interval_helper("[-6, Infinity)", "(-Infinity, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("[-6, Infinity)", "[-2, 5/3]", "(-Infinity, Infinity)");
        multiply_Interval_helper("[-6, Infinity)", "[4, 4]", "[-24, Infinity)");
        multiply_Interval_helper("[-6, Infinity)", "(-Infinity, 3/2]", "(-Infinity, Infinity)");
        multiply_Interval_helper("[-6, Infinity)", "[-6, Infinity)", "(-Infinity, Infinity)");
        multiply_Interval_helper("(-Infinity, 0]", "(-Infinity, 0]", "[0, Infinity)");
    }

    private static void multiply_Rational_helper(@NotNull String a, @NotNull String b, @NotNull String result) {
        aeq(read(a).get().multiply(Rational.read(b).get()), result);
    }

    @Test
    public void testMultiply_Rational() {
        multiply_Rational_helper("[0, 0]", "0", "[0, 0]");
        multiply_Rational_helper("[0, 0]", "1", "[0, 0]");
        multiply_Rational_helper("[0, 0]", "2/3", "[0, 0]");
        multiply_Rational_helper("[0, 0]", "-7", "[0, 0]");

        multiply_Rational_helper("[1, 1]", "0", "[0, 0]");
        multiply_Rational_helper("[1, 1]", "1", "[1, 1]");
        multiply_Rational_helper("[1, 1]", "2/3", "[2/3, 2/3]");
        multiply_Rational_helper("[1, 1]", "-7", "[-7, -7]");

        multiply_Rational_helper("(-Infinity, Infinity)", "0", "[0, 0]");
        multiply_Rational_helper("(-Infinity, Infinity)", "1", "(-Infinity, Infinity)");
        multiply_Rational_helper("(-Infinity, Infinity)", "2/3", "(-Infinity, Infinity)");
        multiply_Rational_helper("(-Infinity, Infinity)", "-7", "(-Infinity, Infinity)");

        multiply_Rational_helper("[-2, 5/3]", "0", "[0, 0]");
        multiply_Rational_helper("[-2, 5/3]", "1", "[-2, 5/3]");
        multiply_Rational_helper("[-2, 5/3]", "2/3", "[-4/3, 10/9]");
        multiply_Rational_helper("[-2, 5/3]", "-7", "[-35/3, 14]");

        multiply_Rational_helper("[4, 4]", "0", "[0, 0]");
        multiply_Rational_helper("[4, 4]", "1", "[4, 4]");
        multiply_Rational_helper("[4, 4]", "2/3", "[8/3, 8/3]");
        multiply_Rational_helper("[4, 4]", "-7", "[-28, -28]");

        multiply_Rational_helper("(-Infinity, 3/2]", "0", "[0, 0]");
        multiply_Rational_helper("(-Infinity, 3/2]", "1", "(-Infinity, 3/2]");
        multiply_Rational_helper("(-Infinity, 3/2]", "2/3", "(-Infinity, 1]");
        multiply_Rational_helper("(-Infinity, 3/2]", "-7", "[-21/2, Infinity)");

        multiply_Rational_helper("[-6, Infinity)", "0", "[0, 0]");
        multiply_Rational_helper("[-6, Infinity)", "1", "[-6, Infinity)");
        multiply_Rational_helper("[-6, Infinity)", "2/3", "[-4, Infinity)");
        multiply_Rational_helper("[-6, Infinity)", "-7", "(-Infinity, 42]");
    }

    private static void multiply_BigInteger_helper(@NotNull String a, @NotNull String b, @NotNull String result) {
        aeq(read(a).get().multiply(Readers.readBigInteger(b).get()), result);
    }

    @Test
    public void testMultiply_BigInteger() {
        multiply_BigInteger_helper("[0, 0]", "0", "[0, 0]");
        multiply_BigInteger_helper("[0, 0]", "1", "[0, 0]");
        multiply_BigInteger_helper("[0, 0]", "5", "[0, 0]");
        multiply_BigInteger_helper("[0, 0]", "-7", "[0, 0]");

        multiply_BigInteger_helper("[1, 1]", "0", "[0, 0]");
        multiply_BigInteger_helper("[1, 1]", "1", "[1, 1]");
        multiply_BigInteger_helper("[1, 1]", "5", "[5, 5]");
        multiply_BigInteger_helper("[1, 1]", "-7", "[-7, -7]");

        multiply_BigInteger_helper("(-Infinity, Infinity)", "0", "[0, 0]");
        multiply_BigInteger_helper("(-Infinity, Infinity)", "1", "(-Infinity, Infinity)");
        multiply_BigInteger_helper("(-Infinity, Infinity)", "5", "(-Infinity, Infinity)");
        multiply_BigInteger_helper("(-Infinity, Infinity)", "-7", "(-Infinity, Infinity)");

        multiply_BigInteger_helper("[-2, 5/3]", "0", "[0, 0]");
        multiply_BigInteger_helper("[-2, 5/3]", "1", "[-2, 5/3]");
        multiply_BigInteger_helper("[-2, 5/3]", "5", "[-10, 25/3]");
        multiply_BigInteger_helper("[-2, 5/3]", "-7", "[-35/3, 14]");

        multiply_BigInteger_helper("[4, 4]", "0", "[0, 0]");
        multiply_BigInteger_helper("[4, 4]", "1", "[4, 4]");
        multiply_BigInteger_helper("[4, 4]", "5", "[20, 20]");
        multiply_BigInteger_helper("[4, 4]", "-7", "[-28, -28]");

        multiply_BigInteger_helper("(-Infinity, 3/2]", "0", "[0, 0]");
        multiply_BigInteger_helper("(-Infinity, 3/2]", "1", "(-Infinity, 3/2]");
        multiply_BigInteger_helper("(-Infinity, 3/2]", "5", "(-Infinity, 15/2]");
        multiply_BigInteger_helper("(-Infinity, 3/2]", "-7", "[-21/2, Infinity)");

        multiply_BigInteger_helper("[-6, Infinity)", "0", "[0, 0]");
        multiply_BigInteger_helper("[-6, Infinity)", "1", "[-6, Infinity)");
        multiply_BigInteger_helper("[-6, Infinity)", "5", "[-30, Infinity)");
        multiply_BigInteger_helper("[-6, Infinity)", "-7", "(-Infinity, 42]");
    }

    private static void multiply_int_helper(@NotNull String a, int b, @NotNull String result) {
        aeq(read(a).get().multiply(b), result);
    }

    @Test
    public void testMultiply_int() {
        multiply_int_helper("[0, 0]", 0, "[0, 0]");
        multiply_int_helper("[0, 0]", 1, "[0, 0]");
        multiply_int_helper("[0, 0]", 5, "[0, 0]");
        multiply_int_helper("[0, 0]", -7, "[0, 0]");

        multiply_int_helper("[1, 1]", 0, "[0, 0]");
        multiply_int_helper("[1, 1]", 1, "[1, 1]");
        multiply_int_helper("[1, 1]", 5, "[5, 5]");
        multiply_int_helper("[1, 1]", -7, "[-7, -7]");

        multiply_int_helper("(-Infinity, Infinity)", 0, "[0, 0]");
        multiply_int_helper("(-Infinity, Infinity)", 1, "(-Infinity, Infinity)");
        multiply_int_helper("(-Infinity, Infinity)", 5, "(-Infinity, Infinity)");
        multiply_int_helper("(-Infinity, Infinity)", -7, "(-Infinity, Infinity)");

        multiply_int_helper("[-2, 5/3]", 0, "[0, 0]");
        multiply_int_helper("[-2, 5/3]", 1, "[-2, 5/3]");
        multiply_int_helper("[-2, 5/3]", 5, "[-10, 25/3]");
        multiply_int_helper("[-2, 5/3]", -7, "[-35/3, 14]");

        multiply_int_helper("[4, 4]", 0, "[0, 0]");
        multiply_int_helper("[4, 4]", 1, "[4, 4]");
        multiply_int_helper("[4, 4]", 5, "[20, 20]");
        multiply_int_helper("[4, 4]", -7, "[-28, -28]");

        multiply_int_helper("(-Infinity, 3/2]", 0, "[0, 0]");
        multiply_int_helper("(-Infinity, 3/2]", 1, "(-Infinity, 3/2]");
        multiply_int_helper("(-Infinity, 3/2]", 5, "(-Infinity, 15/2]");
        multiply_int_helper("(-Infinity, 3/2]", -7, "[-21/2, Infinity)");

        multiply_int_helper("[-6, Infinity)", 0, "[0, 0]");
        multiply_int_helper("[-6, Infinity)", 1, "[-6, Infinity)");
        multiply_int_helper("[-6, Infinity)", 5, "[-30, Infinity)");
        multiply_int_helper("[-6, Infinity)", -7, "(-Infinity, 42]");
    }

    @Test
    public void testInvert() {
        aeq(ZERO.invert(), "[]");
        aeq(ONE.invert(), "[[1, 1]]");
        aeq(ALL.invert(), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().invert(), "[(-Infinity, -1/2], [3/5, Infinity)]");
        aeq(read("[4, 4]").get().invert(), "[[1/4, 1/4]]");
        aeq(read("[0, 4]").get().invert(), "[[1/4, Infinity)]");
        aeq(read("[-4, 0]").get().invert(), "[(-Infinity, -1/4]]");
        aeq(read("(-Infinity, -3/2]").get().invert(), "[[-2/3, 0]]");
        aeq(read("(-Infinity, 3/2]").get().invert(), "[(-Infinity, 0], [2/3, Infinity)]");
        aeq(read("(-Infinity, 0]").get().invert(), "[(-Infinity, 0]]");
        aeq(read("[-6, Infinity)").get().invert(), "[(-Infinity, -1/6], [0, Infinity)]");
        aeq(read("[6, Infinity)").get().invert(), "[[0, 1/6]]");
        aeq(read("[0, Infinity)").get().invert(), "[[0, Infinity)]");
        aeq(read("(-Infinity, 0]").get().invert(), "[(-Infinity, 0]]");
    }

    @Test
    public void testInvertHull() {
        aeq(ONE.invertHull(), "[1, 1]");
        aeq(ALL.invertHull(), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().invertHull(), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().invertHull(), "[1/4, 1/4]");
        aeq(read("[0, 4]").get().invertHull(), "[1/4, Infinity)");
        aeq(read("[-4, 0]").get().invertHull(), "(-Infinity, -1/4]");
        aeq(read("(-Infinity, -3/2]").get().invertHull(), "[-2/3, 0]");
        aeq(read("(-Infinity, 3/2]").get().invertHull(), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 0]").get().invertHull(), "(-Infinity, 0]");
        aeq(read("[-6, Infinity)").get().invertHull(), "(-Infinity, Infinity)");
        aeq(read("[6, Infinity)").get().invertHull(), "[0, 1/6]");
        aeq(read("[0, Infinity)").get().invertHull(), "[0, Infinity)");
        aeq(read("(-Infinity, 0]").get().invertHull(), "(-Infinity, 0]");
        try {
            ZERO.invertHull();
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Interval() {
        aeq(ZERO.divide(ZERO), "[]");
        aeq(ZERO.divide(ONE), "[[0, 0]]");
        aeq(ZERO.divide(ALL), "[[0, 0]]");
        aeq(ZERO.divide(read("[-2, 5/3]").get()), "[[0, 0]]");
        aeq(ZERO.divide(read("[4, 4]").get()), "[[0, 0]]");
        aeq(ZERO.divide(read("(-Infinity, 3/2]").get()), "[[0, 0]]");
        aeq(ZERO.divide(read("[-6, Infinity)").get()), "[[0, 0]]");
        aeq(ONE.divide(ZERO), "[]");
        aeq(ONE.divide(ONE), "[[1, 1]]");
        aeq(ONE.divide(ALL), "[(-Infinity, Infinity)]");
        aeq(ONE.divide(read("[-2, 5/3]").get()), "[(-Infinity, -1/2], [3/5, Infinity)]");
        aeq(ONE.divide(read("[4, 4]").get()), "[[1/4, 1/4]]");
        aeq(ONE.divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, 0], [2/3, Infinity)]");
        aeq(ONE.divide(read("[-6, Infinity)").get()), "[(-Infinity, -1/6], [0, Infinity)]");
        aeq(ALL.divide(ZERO), "[]");
        aeq(ALL.divide(ONE), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(ALL), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("[4, 4]").get()), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(ALL.divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(ZERO), "[]");
        aeq(read("[-2, 5/3]").get().divide(ONE), "[[-2, 5/3]]");
        aeq(read("[-2, 5/3]").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(read("[4, 4]").get()), "[[-1/2, 5/12]]");
        aeq(read("[-2, 5/3]").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("[4, 4]").get().divide(ZERO), "[]");
        aeq(read("[4, 4]").get().divide(ONE), "[[4, 4]]");
        aeq(read("[4, 4]").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("[4, 4]").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, -2], [12/5, Infinity)]");
        aeq(read("[4, 4]").get().divide(read("[4, 4]").get()), "[[1, 1]]");
        aeq(read("[4, 4]").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, 0], [8/3, Infinity)]");
        aeq(read("[4, 4]").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, -2/3], [0, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(ZERO), "[]");
        aeq(read("(-Infinity, 3/2]").get().divide(ONE), "[(-Infinity, 3/2]]");
        aeq(read("(-Infinity, 3/2]").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("[4, 4]").get()), "[(-Infinity, 3/8]]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(ZERO), "[]");
        aeq(read("[-6, Infinity)").get().divide(ONE), "[[-6, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(ALL), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("[-2, 5/3]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("[4, 4]").get()), "[[-3/2, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("(-Infinity, 3/2]").get()), "[(-Infinity, Infinity)]");
        aeq(read("[-6, Infinity)").get().divide(read("[-6, Infinity)").get()), "[(-Infinity, Infinity)]");
        aeq(read("(-Infinity, 0]").get().divide(read("(-Infinity, 0]").get()), "[[0, Infinity)]");
    }

    @Test
    public void testDivideHull() {
        aeq(ZERO.divideHull(ONE), "[0, 0]");
        aeq(ZERO.divideHull(ALL), "[0, 0]");
        aeq(ZERO.divideHull(read("[-2, 5/3]").get()), "[0, 0]");
        aeq(ZERO.divideHull(read("[4, 4]").get()), "[0, 0]");
        aeq(ZERO.divideHull(read("(-Infinity, 3/2]").get()), "[0, 0]");
        aeq(ZERO.divideHull(read("[-6, Infinity)").get()), "[0, 0]");
        aeq(ONE.divideHull(ONE), "[1, 1]");
        aeq(ONE.divideHull(ALL), "(-Infinity, Infinity)");
        aeq(ONE.divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ONE.divideHull(read("[4, 4]").get()), "[1/4, 1/4]");
        aeq(ONE.divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ONE.divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(ONE), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(ALL), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("[4, 4]").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(ALL.divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(read("[4, 4]").get()), "[-1/2, 5/12]");
        aeq(read("[-2, 5/3]").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(ONE), "[4, 4]");
        aeq(read("[4, 4]").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(read("[4, 4]").get()), "[1, 1]");
        aeq(read("[4, 4]").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("[4, 4]").get()), "(-Infinity, 3/8]");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(ALL), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("[-2, 5/3]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("[4, 4]").get()), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("(-Infinity, 3/2]").get()), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().divideHull(read("[-6, Infinity)").get()), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 0]").get().divideHull(read("(-Infinity, 0]").get()), "[0, Infinity)");
        try {
            ZERO.divideHull(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            read("[-2, 5/3]").get().divideHull(ZERO);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_Rational() {
        aeq(ZERO.divide(Rational.ONE), "[0, 0]");
        aeq(ZERO.divide(Rational.read("2/3").get()), "[0, 0]");
        aeq(ZERO.divide(Rational.read("-7").get()), "[0, 0]");
        aeq(ONE.divide(Rational.ONE), "[1, 1]");
        aeq(ONE.divide(Rational.read("2/3").get()), "[3/2, 3/2]");
        aeq(ONE.divide(Rational.read("-7").get()), "[-1/7, -1/7]");
        aeq(ALL.divide(Rational.ONE), "(-Infinity, Infinity)");
        aeq(ALL.divide(Rational.read("2/3").get()), "(-Infinity, Infinity)");
        aeq(ALL.divide(Rational.read("-7").get()), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divide(Rational.ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divide(Rational.read("2/3").get()), "[-3, 5/2]");
        aeq(read("[-2, 5/3]").get().divide(Rational.read("-7").get()), "[-5/21, 2/7]");
        aeq(read("[4, 4]").get().divide(Rational.ONE), "[4, 4]");
        aeq(read("[4, 4]").get().divide(Rational.read("2/3").get()), "[6, 6]");
        aeq(read("[4, 4]").get().divide(Rational.read("-7").get()), "[-4/7, -4/7]");
        aeq(read("(-Infinity, 3/2]").get().divide(Rational.ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divide(Rational.read("2/3").get()), "(-Infinity, 9/4]");
        aeq(read("(-Infinity, 3/2]").get().divide(Rational.read("-7").get()), "[-3/14, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(Rational.ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(Rational.read("2/3").get()), "[-9, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(Rational.read("-7").get()), "(-Infinity, 6/7]");
        try {
            read("[-2, 5/3]").get().divide(Rational.ZERO);
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_BigInteger() {
        aeq(ZERO.divide(BigInteger.ONE), "[0, 0]");
        aeq(ZERO.divide(BigInteger.valueOf(5)), "[0, 0]");
        aeq(ZERO.divide(BigInteger.valueOf(-7)), "[0, 0]");
        aeq(ONE.divide(BigInteger.ONE), "[1, 1]");
        aeq(ONE.divide(BigInteger.valueOf(5)), "[1/5, 1/5]");
        aeq(ONE.divide(BigInteger.valueOf(-7)), "[-1/7, -1/7]");
        aeq(ALL.divide(BigInteger.ONE), "(-Infinity, Infinity)");
        aeq(ALL.divide(BigInteger.valueOf(5)), "(-Infinity, Infinity)");
        aeq(ALL.divide(BigInteger.valueOf(-7)), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divide(BigInteger.ONE), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divide(BigInteger.valueOf(5)), "[-2/5, 1/3]");
        aeq(read("[-2, 5/3]").get().divide(BigInteger.valueOf(-7)), "[-5/21, 2/7]");
        aeq(read("[4, 4]").get().divide(BigInteger.ONE), "[4, 4]");
        aeq(read("[4, 4]").get().divide(BigInteger.valueOf(5)), "[4/5, 4/5]");
        aeq(read("[4, 4]").get().divide(BigInteger.valueOf(-7)), "[-4/7, -4/7]");
        aeq(read("(-Infinity, 3/2]").get().divide(BigInteger.ONE), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divide(BigInteger.valueOf(5)), "(-Infinity, 3/10]");
        aeq(read("(-Infinity, 3/2]").get().divide(BigInteger.valueOf(-7)), "[-3/14, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(BigInteger.ONE), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(BigInteger.valueOf(5)), "[-6/5, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(BigInteger.valueOf(-7)), "(-Infinity, 6/7]");
        try {
            read("[-2, 5/3]").get().divide(BigInteger.ZERO);
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testDivide_int() {
        aeq(ZERO.divide(1), "[0, 0]");
        aeq(ZERO.divide(5), "[0, 0]");
        aeq(ZERO.divide(-7), "[0, 0]");
        aeq(ONE.divide(1), "[1, 1]");
        aeq(ONE.divide(5), "[1/5, 1/5]");
        aeq(ONE.divide(-7), "[-1/7, -1/7]");
        aeq(ALL.divide(1), "(-Infinity, Infinity)");
        aeq(ALL.divide(5), "(-Infinity, Infinity)");
        aeq(ALL.divide(-7), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().divide(1), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().divide(5), "[-2/5, 1/3]");
        aeq(read("[-2, 5/3]").get().divide(-7), "[-5/21, 2/7]");
        aeq(read("[4, 4]").get().divide(1), "[4, 4]");
        aeq(read("[4, 4]").get().divide(5), "[4/5, 4/5]");
        aeq(read("[4, 4]").get().divide(-7), "[-4/7, -4/7]");
        aeq(read("(-Infinity, 3/2]").get().divide(1), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().divide(5), "(-Infinity, 3/10]");
        aeq(read("(-Infinity, 3/2]").get().divide(-7), "[-3/14, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(1), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(5), "[-6/5, Infinity)");
        aeq(read("[-6, Infinity)").get().divide(-7), "(-Infinity, 6/7]");
        try {
            read("[-2, 5/3]").get().divide(0);
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testShiftLeft() {
        aeq(ZERO.shiftLeft(0), "[0, 0]");
        aeq(ZERO.shiftLeft(1), "[0, 0]");
        aeq(ZERO.shiftLeft(2), "[0, 0]");
        aeq(ZERO.shiftLeft(3), "[0, 0]");
        aeq(ZERO.shiftLeft(4), "[0, 0]");
        aeq(ZERO.shiftLeft(-1), "[0, 0]");
        aeq(ZERO.shiftLeft(-2), "[0, 0]");
        aeq(ZERO.shiftLeft(-3), "[0, 0]");
        aeq(ZERO.shiftLeft(-4), "[0, 0]");
        aeq(ONE.shiftLeft(0), "[1, 1]");
        aeq(ONE.shiftLeft(1), "[2, 2]");
        aeq(ONE.shiftLeft(2), "[4, 4]");
        aeq(ONE.shiftLeft(3), "[8, 8]");
        aeq(ONE.shiftLeft(4), "[16, 16]");
        aeq(ONE.shiftLeft(-1), "[1/2, 1/2]");
        aeq(ONE.shiftLeft(-2), "[1/4, 1/4]");
        aeq(ONE.shiftLeft(-3), "[1/8, 1/8]");
        aeq(ONE.shiftLeft(-4), "[1/16, 1/16]");
        aeq(ALL.shiftLeft(0), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(1), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(2), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(3), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(4), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-1), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-2), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-3), "(-Infinity, Infinity)");
        aeq(ALL.shiftLeft(-4), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().shiftLeft(0), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(1), "[-4, 10/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(2), "[-8, 20/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(3), "[-16, 40/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(4), "[-32, 80/3]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-1), "[-1, 5/6]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-2), "[-1/2, 5/12]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-3), "[-1/4, 5/24]");
        aeq(read("[-2, 5/3]").get().shiftLeft(-4), "[-1/8, 5/48]");
        aeq(read("[4, 4]").get().shiftLeft(0), "[4, 4]");
        aeq(read("[4, 4]").get().shiftLeft(1), "[8, 8]");
        aeq(read("[4, 4]").get().shiftLeft(2), "[16, 16]");
        aeq(read("[4, 4]").get().shiftLeft(3), "[32, 32]");
        aeq(read("[4, 4]").get().shiftLeft(4), "[64, 64]");
        aeq(read("[4, 4]").get().shiftLeft(-1), "[2, 2]");
        aeq(read("[4, 4]").get().shiftLeft(-2), "[1, 1]");
        aeq(read("[4, 4]").get().shiftLeft(-3), "[1/2, 1/2]");
        aeq(read("[4, 4]").get().shiftLeft(-4), "[1/4, 1/4]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(0), "(-Infinity, -3/2]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(1), "(-Infinity, -3]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(2), "(-Infinity, -6]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(3), "(-Infinity, -12]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(4), "(-Infinity, -24]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-1), "(-Infinity, -3/4]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-2), "(-Infinity, -3/8]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-3), "(-Infinity, -3/16]");
        aeq(read("(-Infinity, -3/2]").get().shiftLeft(-4), "(-Infinity, -3/32]");
        aeq(read("[-6, Infinity)").get().shiftLeft(0), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(1), "[-12, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(2), "[-24, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(3), "[-48, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(4), "[-96, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-1), "[-3, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-2), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-3), "[-3/4, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftLeft(-4), "[-3/8, Infinity)");
    }

    @Test
    public void testShiftRight() {
        aeq(ZERO.shiftRight(0), "[0, 0]");
        aeq(ZERO.shiftRight(1), "[0, 0]");
        aeq(ZERO.shiftRight(2), "[0, 0]");
        aeq(ZERO.shiftRight(3), "[0, 0]");
        aeq(ZERO.shiftRight(4), "[0, 0]");
        aeq(ZERO.shiftRight(-1), "[0, 0]");
        aeq(ZERO.shiftRight(-2), "[0, 0]");
        aeq(ZERO.shiftRight(-3), "[0, 0]");
        aeq(ZERO.shiftRight(-4), "[0, 0]");
        aeq(ONE.shiftRight(0), "[1, 1]");
        aeq(ONE.shiftRight(1), "[1/2, 1/2]");
        aeq(ONE.shiftRight(2), "[1/4, 1/4]");
        aeq(ONE.shiftRight(3), "[1/8, 1/8]");
        aeq(ONE.shiftRight(4), "[1/16, 1/16]");
        aeq(ONE.shiftRight(-1), "[2, 2]");
        aeq(ONE.shiftRight(-2), "[4, 4]");
        aeq(ONE.shiftRight(-3), "[8, 8]");
        aeq(ONE.shiftRight(-4), "[16, 16]");
        aeq(ALL.shiftRight(0), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(1), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(2), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(3), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(4), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-1), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-2), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-3), "(-Infinity, Infinity)");
        aeq(ALL.shiftRight(-4), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().shiftRight(0), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(1), "[-1, 5/6]");
        aeq(read("[-2, 5/3]").get().shiftRight(2), "[-1/2, 5/12]");
        aeq(read("[-2, 5/3]").get().shiftRight(3), "[-1/4, 5/24]");
        aeq(read("[-2, 5/3]").get().shiftRight(4), "[-1/8, 5/48]");
        aeq(read("[-2, 5/3]").get().shiftRight(-1), "[-4, 10/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(-2), "[-8, 20/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(-3), "[-16, 40/3]");
        aeq(read("[-2, 5/3]").get().shiftRight(-4), "[-32, 80/3]");
        aeq(read("[4, 4]").get().shiftRight(0), "[4, 4]");
        aeq(read("[4, 4]").get().shiftRight(1), "[2, 2]");
        aeq(read("[4, 4]").get().shiftRight(2), "[1, 1]");
        aeq(read("[4, 4]").get().shiftRight(3), "[1/2, 1/2]");
        aeq(read("[4, 4]").get().shiftRight(4), "[1/4, 1/4]");
        aeq(read("[4, 4]").get().shiftRight(-1), "[8, 8]");
        aeq(read("[4, 4]").get().shiftRight(-2), "[16, 16]");
        aeq(read("[4, 4]").get().shiftRight(-3), "[32, 32]");
        aeq(read("[4, 4]").get().shiftRight(-4), "[64, 64]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(0), "(-Infinity, -3/2]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(1), "(-Infinity, -3/4]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(2), "(-Infinity, -3/8]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(3), "(-Infinity, -3/16]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(4), "(-Infinity, -3/32]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-1), "(-Infinity, -3]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-2), "(-Infinity, -6]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-3), "(-Infinity, -12]");
        aeq(read("(-Infinity, -3/2]").get().shiftRight(-4), "(-Infinity, -24]");
        aeq(read("[-6, Infinity)").get().shiftRight(0), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(1), "[-3, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(2), "[-3/2, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(3), "[-3/4, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(4), "[-3/8, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-1), "[-12, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-2), "[-24, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-3), "[-48, Infinity)");
        aeq(read("[-6, Infinity)").get().shiftRight(-4), "[-96, Infinity)");
    }

    @Test
    public void testSum() {
        aeq(sum(readIntervalList("[]")), "[0, 0]");
        aeq(sum(readIntervalList("[[-2, 5/3], (-Infinity, 6], [4, 4]]")), "(-Infinity, 35/3]");
        try {
            sum(readIntervalListWithNulls("[[-2, 5/3], null, [4, 4]]"));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testProduct() {
        aeq(product(readIntervalList("[]")), "[1, 1]");
        aeq(product(readIntervalList("[[-2, 5/3], [0, 6], [4, 4]]")), "[-48, 40]");
        aeq(product(readIntervalList("[[-2, 5/3], (-Infinity, 6], [4, 4]]")), "(-Infinity, Infinity)");
        try {
            product(readIntervalListWithNulls("[[-2, 5/3], null, [4, 4]]"));
            fail();
        } catch (NullPointerException | IllegalArgumentException ignored) {}
    }

    @Test
    public void testDelta() {
        aeqit(delta(readIntervalList("[[1/3, 2]]")), "[]");
        aeqit(delta(readIntervalList("[[-2, 5/3], (-Infinity, 6], [4, 4]]")), "[(-Infinity, 8], [-2, Infinity)]");
        Interval seed = read("[1/3, 1/2]").get();
        aeqitLimit(TINY_LIMIT, delta(iterate(seed::multiply, seed)),
                "[[-7/18, -1/12], [-23/108, 1/72], [-73/648, 11/432], [-227/3888, 49/2592], [-697/23328, 179/15552]," +
                " [-2123/139968, 601/93312], [-6433/839808, 1931/559872], [-19427/5038848, 6049/3359232]," +
                " [-58537/30233088, 18659/20155392], [-176123/181398528, 57001/120932352]," +
                " [-529393/1088391168, 173051/725594112], [-1590227/6530347008, 523249/4353564672]," +
                " [-4774777/39182082048, 1577939/26121388032], [-14332523/235092492288, 4750201/156728328192]," +
                " [-43013953/1410554953728, 14283371/940369969152]," +
                " [-129074627/8463329722368, 42915649/5642219814912]," +
                " [-387289417/50779978334208, 128878019/33853318889472]," +
                " [-1161999323/304679870005248, 386896201/203119913336832]," +
                " [-3486260113/1828079220031488, 1161212891/1218719480020992]," +
                " [-10459304627/10968475320188928, 3484687249/7312316880125952], ...]");
        try {
            delta(readIntervalListWithNulls("[]"));
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            toList(delta(readIntervalListWithNulls("[[-2, 5/3], null, [4, 4]]")));
            fail();
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testPow() {
        aeq(ZERO.pow(0), "[[1, 1]]");
        aeq(ZERO.pow(1), "[[0, 0]]");
        aeq(ZERO.pow(2), "[[0, 0]]");
        aeq(ZERO.pow(3), "[[0, 0]]");
        aeq(ONE.pow(0), "[[1, 1]]");
        aeq(ONE.pow(1), "[[1, 1]]");
        aeq(ONE.pow(2), "[[1, 1]]");
        aeq(ONE.pow(3), "[[1, 1]]");
        aeq(ONE.pow(-1), "[[1, 1]]");
        aeq(ONE.pow(-2), "[[1, 1]]");
        aeq(ONE.pow(-3), "[[1, 1]]");
        aeq(ALL.pow(0), "[[1, 1]]");
        aeq(ALL.pow(1), "[(-Infinity, Infinity)]");
        aeq(ALL.pow(2), "[[0, Infinity)]");
        aeq(ALL.pow(3), "[(-Infinity, Infinity)]");
        aeq(ALL.pow(-1), "[(-Infinity, Infinity)]");
        aeq(ALL.pow(-2), "[[0, Infinity)]");
        aeq(ALL.pow(-3), "[(-Infinity, Infinity)]");
        aeq(read("[-2, 5/3]").get().pow(0), "[[1, 1]]");
        aeq(read("[-2, 5/3]").get().pow(1), "[[-2, 5/3]]");
        aeq(read("[-2, 5/3]").get().pow(2), "[[0, 4]]");
        aeq(read("[-2, 5/3]").get().pow(3), "[[-8, 125/27]]");
        aeq(read("[-2, 5/3]").get().pow(-1), "[(-Infinity, -1/2], [3/5, Infinity)]");
        aeq(read("[-2, 5/3]").get().pow(-2), "[[1/4, Infinity)]");
        aeq(read("[-2, 5/3]").get().pow(-3), "[(-Infinity, -1/8], [27/125, Infinity)]");
        aeq(read("[4, 4]").get().pow(0), "[[1, 1]]");
        aeq(read("[4, 4]").get().pow(1), "[[4, 4]]");
        aeq(read("[4, 4]").get().pow(2), "[[16, 16]]");
        aeq(read("[4, 4]").get().pow(3), "[[64, 64]]");
        aeq(read("[4, 4]").get().pow(-1), "[[1/4, 1/4]]");
        aeq(read("[4, 4]").get().pow(-2), "[[1/16, 1/16]]");
        aeq(read("[4, 4]").get().pow(-3), "[[1/64, 1/64]]");
        aeq(read("(-Infinity, 3/2]").get().pow(0), "[[1, 1]]");
        aeq(read("(-Infinity, 3/2]").get().pow(1), "[(-Infinity, 3/2]]");
        aeq(read("(-Infinity, 3/2]").get().pow(2), "[[0, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().pow(3), "[(-Infinity, 27/8]]");
        aeq(read("(-Infinity, 3/2]").get().pow(-1), "[(-Infinity, 0], [2/3, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().pow(-2), "[[0, Infinity)]");
        aeq(read("(-Infinity, 3/2]").get().pow(-3), "[(-Infinity, 0], [8/27, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(0), "[[1, 1]]");
        aeq(read("[-6, Infinity)").get().pow(1), "[[-6, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(2), "[[0, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(3), "[[-216, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(-1), "[(-Infinity, -1/6], [0, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(-2), "[[0, Infinity)]");
        aeq(read("[-6, Infinity)").get().pow(-3), "[(-Infinity, -1/216], [0, Infinity)]");
    }

    @Test
    public void testPowHull() {
        aeq(ZERO.powHull(0), "[1, 1]");
        aeq(ZERO.powHull(1), "[0, 0]");
        aeq(ZERO.powHull(2), "[0, 0]");
        aeq(ZERO.powHull(3), "[0, 0]");
        aeq(ONE.powHull(0), "[1, 1]");
        aeq(ONE.powHull(1), "[1, 1]");
        aeq(ONE.powHull(2), "[1, 1]");
        aeq(ONE.powHull(3), "[1, 1]");
        aeq(ONE.powHull(-1), "[1, 1]");
        aeq(ONE.powHull(-2), "[1, 1]");
        aeq(ONE.powHull(-3), "[1, 1]");
        aeq(ALL.powHull(0), "[1, 1]");
        aeq(ALL.powHull(1), "(-Infinity, Infinity)");
        aeq(ALL.powHull(2), "[0, Infinity)");
        aeq(ALL.powHull(3), "(-Infinity, Infinity)");
        aeq(ALL.powHull(-1), "(-Infinity, Infinity)");
        aeq(ALL.powHull(-2), "[0, Infinity)");
        aeq(ALL.powHull(-3), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().powHull(0), "[1, 1]");
        aeq(read("[-2, 5/3]").get().powHull(1), "[-2, 5/3]");
        aeq(read("[-2, 5/3]").get().powHull(2), "[0, 4]");
        aeq(read("[-2, 5/3]").get().powHull(3), "[-8, 125/27]");
        aeq(read("[-2, 5/3]").get().powHull(-1), "(-Infinity, Infinity)");
        aeq(read("[-2, 5/3]").get().powHull(-2), "[1/4, Infinity)");
        aeq(read("[-2, 5/3]").get().powHull(-3), "(-Infinity, Infinity)");
        aeq(read("[4, 4]").get().powHull(0), "[1, 1]");
        aeq(read("[4, 4]").get().powHull(1), "[4, 4]");
        aeq(read("[4, 4]").get().powHull(2), "[16, 16]");
        aeq(read("[4, 4]").get().powHull(3), "[64, 64]");
        aeq(read("[4, 4]").get().powHull(-1), "[1/4, 1/4]");
        aeq(read("[4, 4]").get().powHull(-2), "[1/16, 1/16]");
        aeq(read("[4, 4]").get().powHull(-3), "[1/64, 1/64]");
        aeq(read("(-Infinity, 3/2]").get().powHull(0), "[1, 1]");
        aeq(read("(-Infinity, 3/2]").get().powHull(1), "(-Infinity, 3/2]");
        aeq(read("(-Infinity, 3/2]").get().powHull(2), "[0, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().powHull(3), "(-Infinity, 27/8]");
        aeq(read("(-Infinity, 3/2]").get().powHull(-1), "(-Infinity, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().powHull(-2), "[0, Infinity)");
        aeq(read("(-Infinity, 3/2]").get().powHull(-3), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(0), "[1, 1]");
        aeq(read("[-6, Infinity)").get().powHull(1), "[-6, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(2), "[0, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(3), "[-216, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(-1), "(-Infinity, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(-2), "[0, Infinity)");
        aeq(read("[-6, Infinity)").get().powHull(-3), "(-Infinity, Infinity)");
        try {
            ZERO.powHull(-1);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.powHull(-2);
            fail();
        } catch (ArithmeticException ignored) {}
        try {
            ZERO.powHull(-3);
            fail();
        } catch (ArithmeticException ignored) {}
    }

    @Test
    public void testElementCompare() {
        aeq(ZERO.elementCompare(ZERO), "Optional[EQ]");
        aeq(ZERO.elementCompare(ONE), "Optional[LT]");
        aeq(ZERO.elementCompare(ALL), "Optional.empty");
        aeq(ZERO.elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(ZERO.elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(ZERO.elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(ZERO.elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(ONE.elementCompare(ZERO), "Optional[GT]");
        aeq(ONE.elementCompare(ONE), "Optional[EQ]");
        aeq(ONE.elementCompare(ALL), "Optional.empty");
        aeq(ONE.elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(ONE.elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(ONE.elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(ONE.elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(ALL.elementCompare(ZERO), "Optional.empty");
        aeq(ALL.elementCompare(ONE), "Optional.empty");
        aeq(ALL.elementCompare(ALL), "Optional.empty");
        aeq(ALL.elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(ALL.elementCompare(read("[4, 4]").get()), "Optional.empty");
        aeq(ALL.elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(ALL.elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(ZERO), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(ONE), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(ALL), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(read("[-2, 5/3]").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(read("[-2, 5/3]").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("[4, 4]").get().elementCompare(ZERO), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(ONE), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(ALL), "Optional.empty");
        aeq(read("[4, 4]").get().elementCompare(read("[-2, 5/3]").get()), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(read("[4, 4]").get()), "Optional[EQ]");
        aeq(read("[4, 4]").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional[GT]");
        aeq(read("[4, 4]").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(ZERO), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(ONE), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(ALL), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("[4, 4]").get()), "Optional[LT]");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(read("(-Infinity, 3/2]").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(ZERO), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(ONE), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(ALL), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("[-2, 5/3]").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("[4, 4]").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("(-Infinity, 3/2]").get()), "Optional.empty");
        aeq(read("[-6, Infinity)").get().elementCompare(read("[-6, Infinity)").get()), "Optional.empty");
    }

    @Test
    public void testEquals() {
        testEqualsHelper(
                readIntervalList(
                        "[[0, 0], [1, 1], (-Infinity, Infinity), [-2, 5/3], [4, 4], (-Infinity, 3/2], [-6, Infinity)]"
                ),
                readIntervalList(
                        "[[0, 0], [1, 1], (-Infinity, Infinity), [-2, 5/3], [4, 4], (-Infinity, 3/2], [-6, Infinity)]"
                )
        );
    }

    private static void hashCode_helper(@NotNull String input, int hashCode) {
        aeq(read(input).get().hashCode(), hashCode);
    }

    @Test
    public void testHashCode() {
        hashCode_helper("[0, 0]", 32);
        hashCode_helper("[1, 1]", 1024);
        hashCode_helper("(-Infinity, Infinity)", 0);
        hashCode_helper("[-2, 5/3]", -1733);
        hashCode_helper("[4, 4]", 4000);
        hashCode_helper("(-Infinity, 3/2]", 95);
        hashCode_helper("[-6, Infinity)", -5735);
    }

    @Test
    public void testCompareTo() {
        testCompareToHelper(
                readIntervalList(
                        "[(-Infinity, 3/2], (-Infinity, Infinity), [-6, Infinity), [-2, 5/3], [0, 0], [1, 1], [4, 4]]"
                )
        );
    }

    @Test
    public void testRead() {
        aeq(read("[0, 0]").get(), ZERO);
        aeq(read("[1, 1]").get(), ONE);
        aeq(read("(-Infinity, Infinity)").get(), ALL);
        aeq(read("[-2, 5/3]").get(), "[-2, 5/3]");
        aeq(read("[4, 4]").get(), "[4, 4]");
        aeq(read("(-Infinity, 3/2]").get(), "(-Infinity, 3/2]");
        aeq(read("[-6, Infinity)").get(), "[-6, Infinity)");
        assertFalse(read("").isPresent());
        assertFalse(read("[").isPresent());
        assertFalse(read("[]").isPresent());
        assertFalse(read("[,]").isPresent());
        assertFalse(read("[1, 1").isPresent());
        assertFalse(read("[12]").isPresent());
        assertFalse(read("[1 1]").isPresent());
        assertFalse(read("[1,  1]").isPresent());
        assertFalse(read("[ 1, 1]").isPresent());
        assertFalse(read("[1, 1 ]").isPresent());
        assertFalse(read("[1, 1] ").isPresent());
        assertFalse(read("[-Infinity, Infinity]").isPresent());
        assertFalse(read("(-Infinity, 4)").isPresent());
        assertFalse(read("[4, Infinity]").isPresent());
        assertFalse(read("(Infinity, -Infinity)").isPresent());
        assertFalse(read("[2, 3-]").isPresent());
        assertFalse(read("[2.0, 4]").isPresent());
        assertFalse(read("[2,4]").isPresent());
        assertFalse(read("[5, 4]").isPresent());
        assertFalse(read("[5, 4/0]").isPresent());
    }

    @Test
    public void testFindIn() {
        aeq(findIn("abcd[-5, 2/3]xyz").get(), "([-5, 2/3], 4)");
        aeq(findIn("vdfvdf(-Infinity, 3]cds").get(), "((-Infinity, 3], 6)");
        aeq(findIn("gvrgw49((-Infinity, Infinity)Infinity)").get(), "((-Infinity, Infinity), 8)");
        assertFalse(findIn("").isPresent());
        assertFalse(findIn("hello").isPresent());
        assertFalse(findIn("vdfsvfbf").isPresent());
        assertFalse(findIn("vdfvds[-Infinity, 2]vsd").isPresent());
        assertFalse(findIn("vdfvds(Infinity, 2]vsd").isPresent());
        assertFalse(findIn("abcd[5, 4]xyz").isPresent());
        assertFalse(findIn("abcd[5, 4/0]xyz").isPresent());
    }

    private static @NotNull List<Interval> readIntervalList(@NotNull String s) {
        return Readers.readList(Interval::read).apply(s).get();
    }

    private static @NotNull List<Interval> readIntervalListWithNulls(@NotNull String s) {
        return Readers.readListWithNulls(Interval::read).apply(s).get();
    }
}
