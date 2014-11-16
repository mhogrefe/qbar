package mho.qbar.iterableProviders;

import mho.wheels.iterables.IterableProvider;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public interface QBarIterableProvider extends IterableProvider {
    public @NotNull Iterable<Rational> range(@NotNull Rational a);

    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b);

    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i);

    public @NotNull Iterable<Rational> rangeBy(
            @NotNull Rational a,
            @NotNull Rational i,
            @NotNull Rational b
    );

    public @NotNull Iterable<Rational> rationals();

    public @NotNull Iterable<Rational> nonNegativeRationals();

    public @NotNull Iterable<Rational> positiveRationals();

    public @NotNull Iterable<Rational> negativeRationals();

    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();

    public @NotNull Iterable<Interval> finitelyBoundedIntervals();

    public @NotNull Iterable<Interval> intervals();

    public @NotNull Iterable<Byte> bytes(@NotNull Interval a);

    public @NotNull Iterable<Short> shorts(@NotNull Interval a);

    public @NotNull Iterable<Integer> integers(@NotNull Interval a);

    public @NotNull Iterable<Long> longs(@NotNull Interval a);

    public @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a);

    public @NotNull Iterable<Rational> rationals(@NotNull Interval a);
}
