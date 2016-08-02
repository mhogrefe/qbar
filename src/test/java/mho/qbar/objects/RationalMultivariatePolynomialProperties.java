package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static mho.qbar.objects.RationalMultivariatePolynomial.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

public class RationalMultivariatePolynomialProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_MULTIVARIATE_POLYNOMIAL_CHARS =
            "*+-/0123456789^abcdefghijklmnopqrstuvwxyz";

    private static final @NotNull Comparator<RationalMultivariatePolynomial> TERM_SHORTLEX_COMPARATOR = (p, q) -> {
        int c = Integer.compare(p.termCount(), q.termCount());
        if (c != 0) return c;
        return p.compareTo(q);
    };

    public RationalMultivariatePolynomialProperties() {
        super("RationalMultivariatePolynomial");
    }

    @Override
    protected void testBothModes() {
        propertiesIterable();
        propertiesIterator();
        propertiesOnlyHasIntegralCoefficients();
        propertiesToMultivariatePolynomial();
        propertiesCoefficient();
        propertiesOf_List_Pair_Monomial_Rational();
        propertiesOf_Monomial_Rational();
        compareImplementationsOf_Monomial_Rational();
        propertiesOf_Rational();
        propertiesOf_BigInteger();
        propertiesOf_int();
    }

    private void propertiesIterable() {
        initialize("iterable(MonomialOrder)");
        Iterable<Pair<RationalMultivariatePolynomial, MonomialOrder>> ps = P.pairsLogarithmicOrder(
                P.rationalMultivariatePolynomials(),
                P.monomialOrders()
        );
        for (Pair<RationalMultivariatePolynomial, MonomialOrder> p : take(LIMIT, ps)) {
            Iterable<Pair<Monomial, Rational>> termIterable = p.a.iterable(p.b);
            List<Pair<Monomial, Rational>> terms = toList(termIterable);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing(p.b, (Iterable<Monomial>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<Monomial, Rational>> ts) -> of(ts), p.a);
            testNoRemove(termIterable);
            testHasNext(termIterable);
        }
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            List<Pair<Monomial, Rational>> terms = toList(p);
            assertFalse(p, any(t -> t == null || t.a == null || t.b == null, terms));
            //noinspection RedundantCast
            assertTrue(p, increasing((Iterable<Monomial>) map(t -> t.a, terms)));
            //noinspection Convert2MethodRef
            inverse(IterableUtils::toList, (List<Pair<Monomial, Rational>> ts) -> of(ts), p);
            testNoRemove(p);
            testHasNext(p);
        }
    }

    private void propertiesOnlyHasIntegralCoefficients() {
        initialize("onlyHasIntegralCoefficients()");
        for (RationalMultivariatePolynomial p : take(LIMIT, P.rationalMultivariatePolynomials())) {
            p.onlyHasIntegralCoefficients();
        }

        Iterable<RationalMultivariatePolynomial> ps = map(
                MultivariatePolynomial::toRationalMultivariatePolynomial,
                P.multivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            assertTrue(p, p.onlyHasIntegralCoefficients());
        }
    }

    private void propertiesToMultivariatePolynomial() {
        initialize("toMultivariatePolynomial()");
        Iterable<RationalMultivariatePolynomial> ps = map(
                MultivariatePolynomial::toRationalMultivariatePolynomial,
                P.multivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, ps)) {
            MultivariatePolynomial rp = p.toMultivariatePolynomial();
            assertEquals(p, p.toString(), rp.toString());
            assertEquals(p, p.degree(), rp.degree());
            inverse(
                    RationalMultivariatePolynomial::toMultivariatePolynomial,
                    MultivariatePolynomial::toRationalMultivariatePolynomial,
                    p
            );
        }

        Iterable<Pair<RationalMultivariatePolynomial, Map<Variable, Rational>>> ps2 = P.dependentPairsInfinite(
                filterInfinite(q -> q.degree() > 0, ps),
                p -> P.maps(p.variables(), P.rationals())
        );
        for (Pair<RationalMultivariatePolynomial, Map<Variable, Rational>> p : take(LIMIT, ps2)) {
            assertEquals(p, p.a.applyRational(p.b), p.a.toMultivariatePolynomial().applyRational(p.b));
        }

        Iterable<RationalMultivariatePolynomial> psFail = filterInfinite(
                p -> !p.onlyHasIntegralCoefficients(),
                P.rationalMultivariatePolynomials()
        );
        for (RationalMultivariatePolynomial p : take(LIMIT, psFail)) {
            try {
                p.toMultivariatePolynomial();
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesCoefficient() {
        initialize("coefficient(Monomial)");
        Iterable<Pair<RationalMultivariatePolynomial, Monomial>> ps = P.pairs(
                P.rationalMultivariatePolynomials(),
                P.monomials()
        );
        for (Pair<RationalMultivariatePolynomial, Monomial> p : take(LIMIT, ps)) {
            p.a.coefficient(p.b);
        }
    }

    private void propertiesOf_List_Pair_Monomial_Rational() {
        initialize("of(List<Pair<Monomial, Rational>>)");
        Iterable<List<Pair<Monomial, Rational>>> pss = P.lists(P.pairs(P.monomials(), P.rationals()));
        for (List<Pair<Monomial, Rational>> ps : take(LIMIT, pss)) {
            RationalMultivariatePolynomial p = of(ps);
            p.validate();
            for (List<Pair<Monomial, Rational>> qs : take(TINY_LIMIT, P.permutationsFinite(ps))) {
                assertEquals(ps, of(qs), p);
            }
        }

        Iterable<List<Pair<Monomial, Rational>>> pssFail = filterInfinite(
                ps -> any(p -> p == null || p.a == null || p.b == null, ps),
                P.lists(P.withNull(P.pairs(P.withNull(P.monomials()), P.withNull(P.rationals()))))
        );
        for (List<Pair<Monomial, Rational>> ps : take(LIMIT, pssFail)) {
            try {
                of(ps);
                fail(ps);
            } catch (NullPointerException ignored) {}
        }
    }

    private static @NotNull RationalMultivariatePolynomial of_Monomial_Rational_simplest(
            @NotNull Monomial ev,
            @NotNull Rational c
    ) {
        return of(Collections.singletonList(new Pair<>(ev, c)));
    }

    private void propertiesOf_Monomial_Rational() {
        initialize("of(Monomial, Rational)");
        for (Pair<Monomial, Rational> p : take(LIMIT, P.pairs(P.monomials(), P.rationals()))) {
            RationalMultivariatePolynomial q = of(p.a, p.b);
            q.validate();
            assertEquals(p, of_Monomial_Rational_simplest(p.a, p.b), q);
            assertTrue(p, q.termCount() < 2);
        }
    }

    private void compareImplementationsOf_Monomial_Rational() {
        Map<String, Function<Pair<Monomial, Rational>, RationalMultivariatePolynomial>> functions =
                new LinkedHashMap<>();
        functions.put("simplest", p -> of_Monomial_Rational_simplest(p.a, p.b));
        functions.put("standard", p -> of(p.a, p.b));
        Iterable<Pair<Monomial, Rational>> ps = P.pairs(P.monomials(), P.rationals());
        compareImplementations("of(Monomial, Rational)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            RationalMultivariatePolynomial p = of(r);
            p.validate();
            assertTrue(r, p.degree() <= 0);
            assertTrue(r, p.termCount() <= 1);
            assertEquals(r, p.toString(), r.toString());
            inverse(
                    RationalMultivariatePolynomial::of,
                    (RationalMultivariatePolynomial q) -> q.coefficient(Monomial.ONE),
                    r
            );
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            RationalMultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), i.toString());
            inverse(
                    RationalMultivariatePolynomial::of,
                    (RationalMultivariatePolynomial q) -> q.coefficient(Monomial.ONE).bigIntegerValueExact(),
                    i
            );
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        for (int i : take(LIMIT, P.integers())) {
            RationalMultivariatePolynomial p = of(i);
            p.validate();
            assertTrue(i, p.degree() <= 0);
            assertTrue(i, p.termCount() <= 1);
            assertEquals(i, p.toString(), Integer.toString(i));
            inverse(
                    RationalMultivariatePolynomial::of,
                    (RationalMultivariatePolynomial q) -> q.coefficient(Monomial.ONE).intValueExact(),
                    i
            );
        }
    }
}
