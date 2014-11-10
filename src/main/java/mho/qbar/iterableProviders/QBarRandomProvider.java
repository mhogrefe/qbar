package mho.qbar.iterableProviders;

import mho.haskellesque.iterables.RandomProvider;
import mho.qbar.objects.Rational;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Random;

import static mho.haskellesque.iterables.IterableUtils.*;
import static mho.haskellesque.ordering.Ordering.lt;

public class QBarRandomProvider extends RandomProvider implements QBarIterableProvider {
    public QBarRandomProvider(@NotNull Random generator) {
        super(generator);
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i, @NotNull Rational b) {
        return null;
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every <tt>Rational</tt>. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately <tt>meanBitSize</tt> (The
     * ratio between the actual mean bit size and <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases).
     * Does not support removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>Rational</tt>s' numerators and denominators
     */
    public @NotNull Iterable<Rational> rationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(bigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every <tt>Rational</tt>. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        return rationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every non-negative <tt>Rational</tt>. The numerator's
     * and denominator's bit size is chosen from a geometric distribution with mean approximately <tt>meanBitSize</tt>
     * (The ratio between the actual mean bit size and <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt>
     * increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>Rational</tt>s' numerators and denominators
     */
    public @NotNull Iterable<Rational> nonNegativeRationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(naturalBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every non-negative <tt>Rational</tt>. The numerator's
     * and denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li>The result is an infinite pseudorandom sequence of all non-negative <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Rational> nonNegativeRationals() {
        return nonNegativeRationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every positive <tt>Rational</tt>. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately <tt>meanBitSize</tt> (The
     * ratio between the actual mean bit size and <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all positive <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>Rational</tt>s' numerators and denominators
     */
    public @NotNull Iterable<Rational> positiveRationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(positiveBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every positive <tt>Rational</tt>. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li>The result is an infinite pseudorandom sequence of all positive <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Rational> positiveRationals() {
        return positiveRationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every negative <tt>Rational</tt>. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately <tt>meanBitSize</tt> (The
     * ratio between the actual mean bit size and <tt>meanBitSize</tt> decreases as <tt>meanBitSize</tt> increases).
     * Does not support removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all negative <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>Rational</tt>s' numerators and denominators
     */
    public @NotNull Iterable<Rational> negativeRationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(negativeBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every negative <tt>Rational</tt>. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li>The result is an infinite pseudorandom sequence of all negative <tt>Rational</tt>s.</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Rational> negativeRationals() {
        return negativeRationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every <tt>Rational</tt> in the interval [0, 1). The
     * numerator's and denominator's bit size is chosen from a geometric distribution with mean approximately
     * <tt>meanBitSize</tt> (The ratio between the actual mean bit size and <tt>meanBitSize</tt> decreases as
     * <tt>meanBitSize</tt> increases). Does not support removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li><tt>meanBitSize</tt> must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all <tt>Rational</tt>s in the interval [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the <tt>Rational</tt>s' numerators and denominators
     */
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> lt(q.a, q.b) && q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(naturalBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * @return a pseudorandom <tt>Iterable</tt> that generates every <tt>Rational</tt> in the interval [0, 1). The
     * numerator's and denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does
     * not support removal.
     *
     * <ul>
     *  <li><tt>p</tt> must contain an adequate random number generator.</li>
     *  <li>The result is an infinite pseudorandom sequence of <tt>Rational</tt>s in the interval [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     */
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        return nonNegativeRationalsLessThanOne(BIG_INTEGER_MEAN_BIT_SIZE);
    }
}
