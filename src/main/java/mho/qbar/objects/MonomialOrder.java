package mho.qbar.objects;

import mho.wheels.ordering.comparators.LexComparator;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * An ordering for monomials, or {@link ExponentVector}s.
 */
public enum MonomialOrder implements Comparator<ExponentVector> {
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
         * "equal to", respectively. The {@code ExponentVector}s are compared by first comparing the exponents of the
         * first variable, then the second, and so on until the tie is broken.
         *
         * <ul>
         *  <li>{@code a} cannot be null.</li>
         *  <li>{@code b} cannot be null.</li>
         *  <li>The result may be –1, 0, or 1.</li>
         * </ul>
         *
         * @param a the first {@code ExponentVector}
         * @param b the second {@code ExponentVector}
         * @return {@code a} lex-compared to {@code b}
         */
        @Override
        public int compare(@NotNull ExponentVector a, @NotNull ExponentVector b) {
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
         * "equal to", respectively. The {@code ExponentVector}s are compared by first comparing their degrees, then
         * the exponents of the first variable, then the second, and so on until the tie is broken.
         *
         * <ul>
         *  <li>{@code a} cannot be null.</li>
         *  <li>{@code b} cannot be null.</li>
         *  <li>The result may be –1, 0, or 1.</li>
         * </ul>
         *
         * @param a the first {@code ExponentVector}
         * @param b the second {@code ExponentVector}
         * @return {@code a} grlex-compared to {@code b}
         */
        @Override
        public int compare(@NotNull ExponentVector a, @NotNull ExponentVector b) {
            if (a == b) return 0;
            int thisDegree = a.degree();
            int thatDegree = b.degree();
            if (thisDegree > thatDegree) return 1;
            if (thisDegree < thatDegree) return -1;
            return INTEGER_LEX.compare(a.getExponents(), b.getExponents());
        }
    },

    /**
     * Graded reverse lexicographic order. The default for {@code ExponentVector}s
     */
    GREVLEX {
        /**
         * Compares {@code a} to {@code b}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
         * "equal to", respectively. The {@code ExponentVector}s are compared by first comparing their degrees. If the
         * degrees are equal, the exponents of the <i>last</i> variables are compared first, and, if they differ, the
         * result is reversed–the smaller exponent results in a greater monomial ordering.
         *
         * <ul>
         *  <li>{@code a} cannot be null.</li>
         *  <li>{@code b} cannot be null.</li>
         *  <li>The result may be –1, 0, or 1.</li>
         * </ul>
         *
         * @param a the first {@code ExponentVector}
         * @param b the second {@code ExponentVector}
         * @return {@code a} grevlex-compared to {@code b}
         */
        @Override
        public int compare(@NotNull ExponentVector a, @NotNull ExponentVector b) {
            if (a == b) return 0;
            int thisDegree = a.degree();
            int thatDegree = b.degree();
            if (thisDegree > thatDegree) return 1;
            if (thisDegree < thatDegree) return -1;
            int size = b.size();
            if (a.size() > b.size()) return -1;
            if (b.size() < a.size()) return 1;
            for (int i = size - 1; i >= 0; i--) {
                int thisExponent = a.exponent(Variable.of(i));
                int thatExponent = b.exponent(Variable.of(i));
                if (thisExponent > thatExponent) return -1;
                if (thisExponent < thatExponent) return 1;
            }
            return 0;
        }
    }
}
