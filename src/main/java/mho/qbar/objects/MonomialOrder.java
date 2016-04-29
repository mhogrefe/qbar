package mho.qbar.objects;

import mho.wheels.ordering.comparators.LexComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;

/**
 * An ordering for {@link Monomial}s.
 */
public enum MonomialOrder implements Comparator<Monomial> {
    /**
     * Lexicographic order
     */
    LEX {
        /**
         * Compares {@code Iterable}s of {@code Integer}s lexicographically.
         */
        private @NotNull Comparator<Iterable<Integer>> INTEGER_LEX = new LexComparator<>();

        /**
         * Compares {@code a} to {@code b}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
         * "equal to", respectively. The {@code Monomial}s are compared by first comparing the exponents of the first
         * variable, then the second, and so on until the tie is broken.
         *
         * <ul>
         *  <li>{@code a} cannot be null.</li>
         *  <li>{@code b} cannot be null.</li>
         *  <li>The result may be –1, 0, or 1.</li>
         * </ul>
         *
         * @param a the first {@code Monomial}
         * @param b the second {@code Monomial}
         * @return {@code a} lex-compared to {@code b}
         */
        @Override
        public int compare(@NotNull Monomial a, @NotNull Monomial b) {
            if (a == b) return 0;
            return INTEGER_LEX.compare(a.getExponents(), b.getExponents());
        }
    },

    /**
     * Graded lexicographic order
     */
    GRLEX {
        /**
         * Compares {@code Iterable}s of {@code Integer}s lexicographically.
         */
        private @NotNull Comparator<Iterable<Integer>> INTEGER_LEX = new LexComparator<>();

        /**
         * Compares {@code a} to {@code b}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
         * "equal to", respectively. The {@code Monomial}s are compared by first comparing their degrees, then the
         * exponents of the first variable, then the second, and so on until the tie is broken.
         *
         * <ul>
         *  <li>{@code a} cannot be null.</li>
         *  <li>{@code b} cannot be null.</li>
         *  <li>The result may be –1, 0, or 1.</li>
         * </ul>
         *
         * @param a the first {@code Monomial}
         * @param b the second {@code Monomial}
         * @return {@code a} grlex-compared to {@code b}
         */
        @Override
        public int compare(@NotNull Monomial a, @NotNull Monomial b) {
            if (a == b) return 0;
            int thisDegree = a.degree();
            int thatDegree = b.degree();
            if (thisDegree > thatDegree) return 1;
            if (thisDegree < thatDegree) return -1;
            return INTEGER_LEX.compare(a.getExponents(), b.getExponents());
        }
    },

    /**
     * Graded reverse lexicographic order. The default for {@code Monomial}s
     */
    GREVLEX {
        /**
         * Compares {@code a} to {@code b}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
         * "equal to", respectively. The {@code Monomial}s are compared by first comparing their degrees. If the
         * degrees are equal, the exponents of the <i>last</i> variables are compared first, and, if they differ, the
         * result is reversed–the smaller exponent results in a greater monomial ordering.
         *
         * <ul>
         *  <li>{@code a} cannot be null.</li>
         *  <li>{@code b} cannot be null.</li>
         *  <li>The result may be –1, 0, or 1.</li>
         * </ul>
         *
         * @param a the first {@code Monomial}
         * @param b the second {@code Monomial}
         * @return {@code a} grevlex-compared to {@code b}
         */
        @Override
        public int compare(@NotNull Monomial a, @NotNull Monomial b) {
            if (a == b) return 0;
            int thisDegree = a.degree();
            int thatDegree = b.degree();
            if (thisDegree > thatDegree) return 1;
            if (thisDegree < thatDegree) return -1;
            int size = b.size();
            if (a.size() > b.size()) return -1;
            if (b.size() < a.size()) return 1;
            for (int i = size - 1; i >= 0; i--) {
                Variable v = Variable.of(i);
                int thisExponent = a.exponent(v);
                int thatExponent = b.exponent(v);
                if (thisExponent > thatExponent) return -1;
                if (thisExponent < thatExponent) return 1;
            }
            return 0;
        }
    };

    /**
     * Reads an {@link MonomialOrder} from a {@code String}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the {@code MonomialOrder} represented by {@code s}, or {@code Optional.empty} if {@code s} does not
     * represent a {@code MonomialOrder}
     */
    public static @NotNull Optional<MonomialOrder> readStrict(@NotNull String s) {
        switch (s) {
            case "LEX":
                return Optional.of(MonomialOrder.LEX);
            case "GRLEX":
                return Optional.of(MonomialOrder.GRLEX);
            case "GREVLEX":
                return Optional.of(MonomialOrder.GREVLEX);
            default:
                return Optional.empty();
        }
    }
}
