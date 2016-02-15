package jas.poly;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public final class TermOrder implements Serializable {
    private static final int LEX = 1;

    private static final int INVLEX = 2;

    private static final int GRLEX = 3;

    private static final int IGRLEX = 4;

    private static final int REVLEX = 5;

    private static final int REVILEX = 6;

    private static final int REVTDEG = 7;

    private static final int REVITDG = 8;

    private final static int DEFAULT_EVORD = IGRLEX;

    private final int evord;

    private final int evord2;

    private final int evbeg1;

    private final int evend1;

    private final int evbeg2;

    private final int evend2;

    private final long[][] weight;

    private final EVComparator horder;

    public static abstract class EVComparator implements Comparator<ExpVector>, Serializable {
        public abstract int compare(ExpVector e1, ExpVector e2);
    }

    public TermOrder() {
        this(DEFAULT_EVORD);
    }

    private TermOrder(int evord) {
        if (evord < LEX || REVITDG < evord) {
            throw new IllegalArgumentException("invalid term order: " + evord);
        }
        this.evord = evord;
        this.evord2 = 0;
        weight = null;
        evbeg1 = 0;
        evend1 = Integer.MAX_VALUE;
        evbeg2 = evend1;
        evend2 = evend1;
        if (evord != IGRLEX) System.out.println(evord);
        horder = new EVComparator() {
            @Override
            public int compare(ExpVector e1, ExpVector e2) {
                return -ExpVector.EVIGLC(e1, e2);
            }
        };
    }

    private TermOrder(long[][] w) {
        if (w == null || w.length == 0) {
            throw new IllegalArgumentException("invalid term order weight");
        }
        weight = Arrays.copyOf(w, w.length); // > Java-5
        this.evord = 0;
        this.evord2 = 0;
        evbeg1 = 0;
        evend1 = weight[0].length;
        evbeg2 = evend1;
        evend2 = evend1;

        horder = new EVComparator() {
            @Override
            public int compare(ExpVector e1, ExpVector e2) {
                return -ExpVector.EVIWLC(weight, e1, e2);
            }
        };
    }

    private TermOrder(int ev1, int ev2, int r, int split) {
        if (ev1 < LEX || REVITDG < ev1) {
            throw new IllegalArgumentException("invalid term order: " + ev1);
        }
        if (ev2 < LEX || REVITDG < ev2) {
            throw new IllegalArgumentException("invalid term order: " + ev2);
        }
        this.evord = ev1;
        this.evord2 = ev2;
        weight = null;
        evbeg1 = 0;
        evend1 = split; // excluded
        evbeg2 = split;
        evend2 = r;
        if (evbeg2 > evend2) {
            throw new IllegalArgumentException("invalid term order split, r = " + r + ", split = " + split);
        }
        if (evord != IGRLEX) System.out.println(evord);
        if (evord2 != IGRLEX) System.out.println(evord2);
        horder = new EVComparator() {
            @Override
            public int compare(ExpVector e1, ExpVector e2) {
                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                if (t != 0) {
                    return t;
                }
                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
            }
        };
    }

    /**
     * Get the first defined order indicator.
     *
     * @return evord.
     */
    int getEvord() {
        return evord;
    }

    /**
     * Get the descending order comparator. Sorts the highest terms first.
     *
     * @return horder.
     */
    public EVComparator getDescendComparator() {
        return horder;
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object B) {
        if (!(B instanceof TermOrder)) {
            return false;
        }
        TermOrder b = (TermOrder) B;
        boolean t = evord == b.getEvord() && evord2 == b.evord2 && evbeg1 == b.evbeg1 && evend1 == b.evend1
                && evbeg2 == b.evbeg2 && evend2 == b.evend2;
        return t && Arrays.equals(weight, b.weight);
    }

    /**
     * Hash code.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int h = evord;
        h = (h << 3) + evord2;
        h = (h << 4) + evbeg1;
        h = (h << 4) + evend1;
        h = (h << 4) + evbeg2;
        h = (h << 4) + evend2;
        if (weight == null) {
            return h;
        }
        h = h * 7 + Arrays.deepHashCode(weight);
        return h;
    }

    /**
     * Contract variables. Used e.g. in module embedding. Contract TermOrder to
     * non split status.
     *
     * @param k   position of first element to be copied.
     * @param len new length.
     * @return contracted TermOrder.
     */
    public TermOrder contract(int k, int len) {
        if (weight != null) {
            long[][] w = new long[weight.length][];
            for (int i = 0; i < weight.length; i++) {
                long[] wi = weight[i];
                long[] wj = new long[len];
                System.arraycopy(wi, k, wj, 0, len);
                w[i] = wj;
            }
            return new TermOrder(w);
        }
        if (evord2 == 0) {
            return new TermOrder(evord);
        }
        if (evend1 > k) { // < IntMax since evord2 != 0
            int el = evend1 - k;
            while (el > len) {
                el -= len;
            }
            if (el == 0L) {
                return new TermOrder(evord);
            }
            if (el == len) {
                return new TermOrder(evord);
            }
            return new TermOrder(evord, evord2, len, el);
        }
        return new TermOrder(evord2);
    }
}
