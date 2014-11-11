package mho.qbar.iterableProviders;

import mho.wheels.iterables.IterableProvider;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import org.jetbrains.annotations.NotNull;

public interface QBarIterableProvider extends IterableProvider {
    public abstract @NotNull Iterable<Rational> range(@NotNull Rational a);

    public abstract @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b);

    public abstract @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i);

    public abstract @NotNull Iterable<Rational> rangeBy(
            @NotNull Rational a,
            @NotNull Rational i,
            @NotNull Rational b
    );

    public abstract @NotNull Iterable<Rational> rationals();

    public abstract @NotNull Iterable<Rational> nonNegativeRationals();

    public abstract @NotNull Iterable<Rational> positiveRationals();

    public abstract @NotNull Iterable<Rational> negativeRationals();

    public abstract @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();

    public @NotNull Iterable<Interval> finitelyBoundedIntervals();

    public @NotNull Iterable<Interval> intervals();
}
