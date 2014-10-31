package mho.qbar.iterableProviders;

import mho.haskellesque.iterables.IterableProvider;
import mho.qbar.objects.Rational;
import org.jetbrains.annotations.NotNull;

public interface QBarIterableProvider extends IterableProvider {
    public abstract @NotNull Iterable<Rational> rationals();

    public abstract @NotNull Iterable<Rational> nonNegativeRationals();

    public abstract @NotNull Iterable<Rational> positiveRationals();

    public abstract @NotNull Iterable<Rational> negativeRationals();

    public abstract @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();
}
