package mho.qbar.iterableProviders;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

public class QBarExhaustiveProvider extends ExhaustiveProvider implements QBarIterableProvider {
    public static final @NotNull QBarExhaustiveProvider INSTANCE = new QBarExhaustiveProvider();

    protected QBarExhaustiveProvider() {
        super();
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a) {
        return iterate(r -> r.add(Rational.ONE), a);
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
                x = x.add(Rational.ONE);
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
                x = x.add(i);
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
                x = x.add(i);
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
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        return map(p -> {
            assert p.a != null;
            assert p.b != null;
            return Interval.of(p.a, p.b);
        }, filter(p -> {
            assert p.a != null;
            return le(p.a, p.b);
        }, pairs(rationals())));
    }

    /**
     * an {@code Iterable} that contains every {@code Interval}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
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
                        (Iterable<Pair<Optional<Rational>, Optional<Rational>>>) pairs(optionals(rationals()))
                )
        );
    }

    @Override
    public @NotNull Iterable<Byte> bytes(@NotNull Interval a) {
        Optional<Interval> intersection = Interval.intersection(
                a,
                Interval.of(Rational.of(Byte.MIN_VALUE), Rational.of(Byte.MAX_VALUE))
        );
        if (!intersection.isPresent()) return new ArrayList<>();
        return map(BigInteger::byteValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<Short> shorts(@NotNull Interval a) {
        Optional<Interval> intersection = Interval.intersection(
                a,
                Interval.of(Rational.of(Short.MIN_VALUE), Rational.of(Short.MAX_VALUE))
        );
        if (!intersection.isPresent()) return new ArrayList<>();
        return map(BigInteger::shortValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<Integer> integers(@NotNull Interval a) {
        Optional<Interval> intersection = Interval.intersection(
                a,
                Interval.of(Rational.of(Integer.MIN_VALUE), Rational.of(Integer.MAX_VALUE))
        );
        if (!intersection.isPresent()) return new ArrayList<>();
        return map(BigInteger::intValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<Long> longs(@NotNull Interval a) {
        Optional<Interval> intersection = Interval.intersection(
                a,
                Interval.of(
                        Rational.of(BigInteger.valueOf(Long.MIN_VALUE)),
                        Rational.of(BigInteger.valueOf(Long.MAX_VALUE))
                )
        );
        if (!intersection.isPresent()) return new ArrayList<>();
        return map(BigInteger::longValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return bigIntegers();
        } else if (!a.getLower().isPresent()) {
            return rangeBy(a.getUpper().get().floor(), BigInteger.valueOf(-1));
        } else if (!a.getUpper().isPresent()) {
            return range(a.getLower().get().ceiling());
        } else {
            return range(a.getLower().get().ceiling(), a.getUpper().get().floor());
        }
    }

    @Override
    public @NotNull Iterable<Rational> rationals(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return rationals();
        } else if (!a.getLower().isPresent()) {
            return map(r -> a.getUpper().get().subtract(r), nonNegativeRationals());
        } else if (!a.getUpper().isPresent()) {
            return map(r -> r.add(a.getLower().get()), nonNegativeRationals());
        } else {
            Rational diameter = a.diameter().get();
            if (diameter == Rational.ZERO) return Arrays.asList(a.getLower().get());
            return concat(
                    Arrays.asList(a.getLower().get(), a.getUpper().get()),
                    tail(
                            map(
                                    r -> r.multiply(diameter).add(a.getLower().get()),
                                    nonNegativeRationalsLessThanOne()
                            )
                    )
            );
        }
    }
}
