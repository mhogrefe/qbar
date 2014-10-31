package mho.qbar.iterableProviders;

import mho.haskellesque.iterables.ExhaustiveProvider;
import mho.qbar.objects.Rational;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static mho.haskellesque.iterables.IterableUtils.filter;
import static mho.haskellesque.iterables.IterableUtils.map;
import static mho.haskellesque.ordering.Ordering.lt;

public class QBarExhaustiveProvider extends ExhaustiveProvider implements QBarIterableProvider {
    public QBarExhaustiveProvider() {
        super();
    }

    /**
     * @return an <tt>Iterable</tt> that contains every <tt>Rational</tt>. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return p.a.gcd(p.b).equals(BigInteger.ONE);
                        },
                        pairs(bigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * @return an <tt>Iterable</tt> that contains every non-negative <tt>Rational</tt>. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationals() {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return p.a.gcd(p.b).equals(BigInteger.ONE);
                        },
                        pairs(naturalBigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * an <tt>Iterable</tt> that contains every positive <tt>Rational</tt>. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> positiveRationals() {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.a.gcd(p.b).equals(BigInteger.ONE);
                }, pairs(positiveBigIntegers()))
        );
    }

    /**
     * an <tt>Iterable</tt> that contains every negative <tt>Rational</tt>. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> negativeRationals() {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return p.a.gcd(p.b).equals(BigInteger.ONE);
                        },
                        pairs(negativeBigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * an <tt>Iterable</tt> that contains every <tt>Rational</tt> in the interval [0, 1). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return lt(p.a, p.b) && p.a.gcd(p.b).equals(BigInteger.ONE);
                        },
                        pairs(naturalBigIntegers(), positiveBigIntegers())
                )
        );
    }
}
