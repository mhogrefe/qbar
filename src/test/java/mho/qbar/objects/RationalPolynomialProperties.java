package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static mho.qbar.objects.RationalPolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class RationalPolynomialProperties {
    private static boolean USE_RANDOM;
    private static final String RATIONAL_POLYNOMIAL_CHARS = "*+-/0123456789^x";
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    @Test
    public void testAllProperties() {
        System.out.println("RationalPolynomial properties");
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("\ttesting " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            propertiesIterator();
            propertiesApply();
            compareImplementationsApply();
            propertiesCoefficient();
            propertiesOf_List_Rational();
            propertiesOf_Rational();
            propertiesOf_Rational_int();
            propertiesDegree();
            propertiesLeading();
            propertiesAdd();
            propertiesNegate();
            propertiesAbs();
            propertiesSignum();
            propertiesEquals();
            propertiesHashCode();
            propertiesCompareTo();
            propertiesRead();
            propertiesFindIn();
            propertiesToString();
        }
        System.out.println("Done");
    }

    private static void propertiesIterator() {
        initialize();
        System.out.println("\t\ttesting iterator() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            List<Rational> rs = toList(p);
            assertTrue(p.toString(), all(r -> r != null, rs));
            assertEquals(p.toString(), of(toList(p)), p);
            try {
                p.iterator().remove();
            } catch (UnsupportedOperationException ignored) {}
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            List<Rational> rs = toList(p);
            assertTrue(p.toString(), last(rs) != Rational.ZERO);
        }
    }

    private static @NotNull Rational apply_horner(@NotNull RationalPolynomial p, @NotNull Rational x) {
        return foldr((c, y) -> y.multiply(x).add(c), Rational.ZERO, p);
    }

    private static void propertiesApply() {
        initialize();
        System.out.println("\t\ttesting apply(Rational) properties...");

        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            Rational y = p.a.apply(p.b);
            assertEquals(p.toString(), y, apply_horner(p.a, p.b));
        }

        for (Rational i : take(LIMIT, P.rationals())) {
            assertEquals(i.toString(), ZERO.apply(i), Rational.ZERO);
            assertEquals(i.toString(), X.apply(i), i);
            assertEquals(i.toString(), of(Rational.of(-1), 1).apply(i), i.negate());
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            assertEquals(p.toString(), p.apply(Rational.ZERO), p.coefficient(0));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p.toString(), p.apply(Rational.ONE), Rational.sum(p));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p.toString(), of(p.a).apply(p.b), p.a);
            assertEquals(p.toString(), of(Arrays.asList(p.a, Rational.ONE)).apply(p.b), p.b.add(p.a));
            assertEquals(p.toString(), of(Arrays.asList(p.a.negate(), Rational.ONE)).apply(p.b), p.b.subtract(p.a));
            assertEquals(p.toString(), of(p.a, 1).apply(p.b), p.b.multiply(p.a));
        }

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is = ((RandomProvider) P).naturalIntegersGeometric(20);
        }
        for (Pair<Integer, Rational> p : take(LIMIT, P.pairs(is, P.rationals()))) {
            assertEquals(p.toString(), of(Rational.ONE, p.a).apply(p.b), p.b.pow(p.a));
        }
    }

    private static void compareImplementationsApply() {
        initialize();
        System.out.println("\t\tcomparing apply(Rational) implementations...");

        long totalTime = 0;
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            long time = System.nanoTime();
            apply_horner(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tHorner: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            long time = System.nanoTime();
            p.a.apply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesCoefficient() {
        initialize();
        System.out.println("\t\ttesting coefficient(int) properties...");

        Iterable<Pair<RationalPolynomial, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.rationalPolynomials(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationalPolynomials(), ((RandomProvider) P).naturalIntegersGeometric(10));
        }
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filter(q -> q.b <= q.a.degree(), ps))) {
            assertEquals(p.toString(), p.a.coefficient(p.b), toList(p.a).get(p.b));
        }

        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, filter(q -> q.b > q.a.degree(), ps))) {
            assertEquals(p.toString(), p.a.coefficient(p.b), Rational.ZERO);
        }

        Iterable<Pair<RationalPolynomial, Integer>> psFail = P.pairs(P.rationalPolynomials(), P.negativeIntegers());
        for (Pair<RationalPolynomial, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.coefficient(p.b);
                fail(p.toString());
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
    }

    private static void propertiesOf_List_Rational() {
        initialize();
        System.out.println("\t\ttesting of(List<Rational>) properties");

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            RationalPolynomial p = of(rs);
            validate(p);
            assertTrue(rs.toString(), length(p) <= rs.size());
        }

        Iterable<List<Rational>> rss = filter(rs -> rs.isEmpty() || last(rs) != Rational.ZERO, P.lists(P.rationals()));
        for (List<Rational> rs : take(LIMIT, rss)) {
            assertEquals(rs.toString(), toList(of(rs)), rs);
        }

        Iterable<List<Rational>> failRss = map(
                p -> toList(insert(p.a, p.b, null)),
                (Iterable<Pair<List<Rational>, Integer>>) P.dependentPairsLogarithmic(
                        P.lists(P.rationals()),
                        rs -> range(0, rs.size())
                )
        );
        for (List<Rational> rs : take(LIMIT, failRss)) {
            try {
                of(rs);
                fail(rs.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void propertiesOf_Rational() {
        initialize();
        System.out.println("\t\ttesting of(Rational) properties");

        for (Rational r : take(LIMIT, P.rationals())) {
            RationalPolynomial p = of(r);
            validate(p);
            assertTrue(r.toString(), p.degree() == 0 || p.degree() == -1);
            assertTrue(r.toString(), length(p) == 1 || length(p) == 0);
        }

        for (Rational r : take(LIMIT, filter(j -> j != Rational.ZERO, P.rationals()))) {
            assertEquals(r.toString(), of(r).coefficient(0), r);
        }
    }

    private static void propertiesOf_Rational_int() {
        initialize();
        System.out.println("\t\ttesting of(Rational, int) properties");

        Iterable<Pair<Rational, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = ((QBarExhaustiveProvider) P).pairsLogarithmicOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            RationalPolynomial q = of(p.a, p.b);
            validate(q);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, filter(q -> q.a != Rational.ZERO, ps))) {
            RationalPolynomial q = of(p.a, p.b);
            List<Rational> coefficients = toList(q);
            assertEquals(p.toString(), length(filter(i -> i != Rational.ZERO, coefficients)), 1);
            assertEquals(p.toString(), q.degree(), p.b.intValue());
            assertEquals(p.toString(), length(q), p.b + 1);
        }

        for (int i : take(LIMIT, P.naturalIntegers())) {
            assertTrue(of(Rational.ZERO, i) == ZERO);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                of(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesDegree() {
        initialize();
        System.out.println("\t\ttesting degree() properties");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int degree = p.degree();
            assertTrue(p.toString(), degree >= -1);
        }
    }

    private static void propertiesLeading() {
        initialize();
        System.out.println("\t\ttesting leading() properties");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            p.leading();
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomialsAtLeast(0))) {
            Rational leading = p.leading().get();
            assertNotEquals(p.toString(), leading, Rational.ZERO);
        }

        for (Rational r : take(LIMIT, filter(j -> j != Rational.ZERO, P.rationals()))) {
            RationalPolynomial p = of(r);
            assertEquals(r.toString(), p.leading().get(), p.coefficient(0));
        }
    }

    private static void propertiesAdd() {
        initialize();
        System.out.println("\t\ttesting add(Polynomial) properties...");

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            RationalPolynomial sum = p.a.add(p.b);
            validate(sum);
            assertEquals(p.toString(), sum, p.b.add(p.a));
            //todo assertEquals(p.toString(), sum.subtract(p.b), p.a);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, Rational>> ts = P.triples(
                P.rationalPolynomials(),
                P.rationalPolynomials(),
                P.rationals()
        );
        for (Triple<RationalPolynomial, RationalPolynomial, Rational> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.add(t.b).apply(t.c), t.a.apply(t.c).add(t.b.apply(t.c)));
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p.toString(), ZERO.add(p), p);
            assertEquals(p.toString(), p.add(ZERO), p);
            assertTrue(p.toString(), p.add(p.negate()) == ZERO);
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts2 =
                P.triples(P.rationalPolynomials());
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts2)) {
            RationalPolynomial sum1 = t.a.add(t.b).add(t.c);
            RationalPolynomial sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t.toString(), sum1, sum2);
        }
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial negative = p.negate();
            validate(negative);
            assertEquals(p.toString(), p, negative.negate());
            assertTrue(p.toString(), p.add(negative) == ZERO);
        }

        for (Pair<RationalPolynomial, Rational> p : take(LIMIT, P.pairs(P.rationalPolynomials(), P.rationals()))) {
            assertEquals(p.toString(), p.a.negate().apply(p.b), p.a.apply(p.b).negate());
        }

        for (RationalPolynomial p : take(LIMIT, filter(q -> q != ZERO, P.rationalPolynomials()))) {
            RationalPolynomial negative = p.negate();
            assertNotEquals(p.toString(), p, negative);
        }
    }

    private static void propertiesAbs() {
        initialize();
        System.out.println("\t\ttesting abs() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            RationalPolynomial abs = p.abs();
            validate(abs);
            assertEquals(p.toString(), abs, abs.abs());
            assertNotEquals(p.toString(), abs.signum(), -1);
            assertTrue(p.toString(), ge(abs, ZERO));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("\t\ttesting signum() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            int signum = p.signum();
            assertEquals(p.toString(), signum, Ordering.compare(p, ZERO).toInt());
            assertTrue(p.toString(), signum == -1 || signum == 0 || signum == 1);
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            //noinspection EqualsWithItself
            assertTrue(p.toString(), p.equals(p));
            //noinspection ObjectEqualsNull
            assertFalse(p.toString(), p.equals(null));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p.toString(), p.hashCode(), p.hashCode());
        }
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("\t\ttesting compareTo(RationalPolynomial) properties...");

        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, P.pairs(P.rationalPolynomials()))) {
            int compare = p.a.compareTo(p.b);
            assertTrue(p.toString(), compare == -1 || compare == 0 || compare == 1);
            assertEquals(p.toString(), p.b.compareTo(p.a), -compare);
            //todo assertEquals(p.toString(), p.a.subtract(p.b).signum(), compare);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            assertEquals(p.toString(), p.compareTo(p), 0);
        }

        Iterable<Pair<RationalPolynomial, RationalPolynomial>> ps = filter(
                r -> r.a.degree() != r.b.degree(),
                P.pairs(filter(q -> q.signum() == 1, P.rationalPolynomials()))
        );
        for (Pair<RationalPolynomial, RationalPolynomial> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), compare(p.a, p.b), compare(p.a.degree(), p.b.degree()));
        }

        Iterable<Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial>> ts = filter(
                t -> lt(t.a, t.b) && lt(t.b, t.c),
                P.triples(P.rationalPolynomials())
        );
        for (Triple<RationalPolynomial, RationalPolynomial, RationalPolynomial> t : take(LIMIT, ts)) {
            assertEquals(t.toString(), t.a.compareTo(t.c), -1);
        }
    }

    private static void propertiesRead() {
        initialize();
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            Optional<RationalPolynomial> op = read(p.toString());
            assertEquals(p.toString(), op.get(), p);
        }

        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_POLYNOMIAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(RATIONAL_POLYNOMIAL_CHARS);
        }
        Iterable<String> ss = filter(s -> read(s).isPresent(), P.strings(cs));
        for (String s : take(LIMIT, ss)) {
            Optional<RationalPolynomial> op = read(s);
            validate(op.get());
        }
    }

    private static void propertiesFindIn() {
        initialize();
        System.out.println("\t\ttesting findIn(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            findIn(s);
        }

        Iterable<Pair<String, Integer>> ps = P.dependentPairsLogarithmic(P.strings(), s -> range(0, s.length()));
        Iterable<String> ss = map(
                p -> take(p.a.b, p.a.a) + p.b + drop(p.a.b, p.a.a),
                P.pairs(ps, P.rationalPolynomials())
        );
        for (String s : take(LIMIT, ss)) {
            Optional<Pair<RationalPolynomial, Integer>> op = findIn(s);
            Pair<RationalPolynomial, Integer> p = op.get();
            assertNotNull(s, p.a);
            assertNotNull(s, p.b);
            assertTrue(s, p.b >= 0 && p.b < s.length());
            String before = take(p.b, s);
            assertFalse(s, findIn(before).isPresent());
            String during = p.a.toString();
            assertTrue(s, s.substring(p.b).startsWith(during));
            String after = drop(p.b + during.length(), s);
            assertTrue(s, after.isEmpty() || !read(during + head(after)).isPresent());
        }
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (RationalPolynomial p : take(LIMIT, P.rationalPolynomials())) {
            String s = p.toString();
            assertTrue(p.toString(), isSubsetOf(s, RATIONAL_POLYNOMIAL_CHARS));
            Optional<RationalPolynomial> readP = read(s);
            assertTrue(p.toString(), readP.isPresent());
            assertEquals(p.toString(), readP.get(), p);
        }
    }

    private static void validate(@NotNull RationalPolynomial p) {
        List<Rational> coefficients = toList(p);
        if (!coefficients.isEmpty()) {
            assertTrue(p.toString(), last(coefficients) != Rational.ZERO);
        }
        if (p.equals(ZERO)) assertTrue(p.toString(), p == ZERO);
        if (p.equals(ONE)) assertTrue(p.toString(), p == ONE);
    }
}
