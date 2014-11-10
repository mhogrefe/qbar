package mho.qbar.iterableProviders;

import mho.haskellesque.iterables.ExhaustiveProvider;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import static mho.haskellesque.iterables.IterableUtils.*;
import static mho.haskellesque.iterables.IterableUtils.filter;
import static mho.haskellesque.iterables.IterableUtils.map;
import static mho.haskellesque.ordering.Ordering.*;
import static mho.haskellesque.ordering.Ordering.gt;
import static mho.haskellesque.ordering.Ordering.lt;

public class QBarExhaustiveProvider extends ExhaustiveProvider implements QBarIterableProvider {
    public static final QBarExhaustiveProvider INSTANCE = new QBarExhaustiveProvider();

    protected QBarExhaustiveProvider() {
        super();
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a) {
        return iterate(r -> Rational.add(r, Rational.ONE), a);
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        if (gt(a, b)) return new ArrayList<>();
        return () -> new Iterator<Rational>() {
            private Rational x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Rational next() {
                reachedEnd = x.equals(b);
                Rational oldX = x;
                x = Rational.add(x, Rational.ONE);
                return oldX;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    @Override
    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i) {
        return () -> new Iterator<Rational>() {
            private Rational x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Rational next() {
                Rational oldX = x;
                x = Rational.add(x, i);
                reachedEnd = i.signum() == 1 ? lt(x, a) : gt(x, a);
                return oldX;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    @Override
    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i, @NotNull Rational b) {
        if (i.signum() == 1 ? gt(a, b) : gt(b, a)) return new ArrayList<>();
        return () -> new Iterator<Rational>() {
            private Rational x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Rational next() {
                Rational oldX = x;
                x = Rational.add(x, i);
                reachedEnd = i.signum() == 1 ? gt(x, b) : lt(x, b);
                return oldX;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * @return an {@link Iterable} that contains every {@link mho.qbar.objects.Rational}. Does not support removal.
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
     * @return an {@code Iterable} that contains every non-negative {@code Rational}. Does not support removal.
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
     * an {@code Iterable} that contains every positive {@code Rational}. Does not support removal.
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
     * an {@code Iterable} that contains every negative {@code Rational}. Does not support removal.
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
     * an {@code Iterable} that contains every {@code Rational} in the interval [0, 1). Does not support removal.
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

    /**
     * an {@code Iterable} that contains every finitely-bounded {@link mho.qbar.objects.Interval}. Does not support
     * removal.
     *
     * Length is infinite
     */
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        return map(p -> {
            assert p.a != null;
            assert p.b != null;
            return Interval.of(p.a, p.b);
        }, filter(p -> le(p.a, p.b), pairs(rationals())));
    }

    /**
     * an {@code Iterable} that contains every {@link mho.qbar.objects.Interval}. Does not support removal.
     *
     * Length is infinite
     */
    public @NotNull Iterable<Interval> intervals() {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get());
                        },
                        pairs(optionals(rationals()))
                )
        );
    }
}
