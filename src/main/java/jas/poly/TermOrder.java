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
        switch (evord) {
            case TermOrder.LEX: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return ExpVector.EVILCP(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.INVLEX: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return -ExpVector.EVILCP(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.GRLEX: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return ExpVector.EVIGLC(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.IGRLEX: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return -ExpVector.EVIGLC(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.REVLEX: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return ExpVector.EVRILCP(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.REVILEX: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return -ExpVector.EVRILCP(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.REVTDEG: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return ExpVector.EVRIGLC(e1, e2);
                    }
                };
                break;
            }
            case TermOrder.REVITDG: {
                horder = new EVComparator() {
                    @Override
                    public int compare(ExpVector e1, ExpVector e2) {
                        return -ExpVector.EVRIGLC(e1, e2);
                    }
                };
                break;
            }
            default: {
                horder = null;
            }
        }
        if (horder == null) {
            throw new IllegalArgumentException("invalid term order: " + evord);
        }
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
        switch (evord) { // horder = new EVhorder();
            case TermOrder.LEX: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            case TermOrder.INVLEX: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVILEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVTDEG: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVITDG: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            case TermOrder.GRLEX: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {
                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            case TermOrder.IGRLEX: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
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
                        break;
                    }
                    case TermOrder.REVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVILEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVTDEG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVITDG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            //----- begin reversed -----------
            case TermOrder.REVLEX: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVILEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVTDEG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVITDG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            case TermOrder.REVILEX: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVILEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVTDEG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVITDG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRILCP(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            case TermOrder.REVTDEG: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVILEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVTDEG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVITDG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            case TermOrder.REVITDG: {
                switch (evord2) {
                    case TermOrder.LEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.INVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.GRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.IGRLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVLEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVILEX: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRILCP(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVTDEG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    case TermOrder.REVITDG: {
                        horder = new EVComparator() {


                            @Override
                            public int compare(ExpVector e1, ExpVector e2) {
                                int t = -ExpVector.EVRIGLC(e1, e2, evbeg1, evend1);
                                if (t != 0) {
                                    return t;
                                }
                                return -ExpVector.EVRIGLC(e1, e2, evbeg2, evend2);
                            }
                        };
                        break;
                    }
                    default: {
                        horder = null;
                    }
                }
                break;
            }
            //----- end reversed-----------
            default: {
                horder = null;
            }
        }
        if (horder == null) {
            throw new IllegalArgumentException("invalid term order: " + evord + " 2 " + evord2);
        }
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
     * String representation of TermOrder.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder erg = new StringBuilder();
        if (weight != null) {
            erg.append("W(");
            for (int j = 0; j < weight.length; j++) {
                long[] wj = weight[j];
                erg.append("(");
                for (int i = 0; i < wj.length; i++) {
                    erg.append("").append(wj[wj.length - i - 1]);
                    if (i < wj.length - 1) {
                        erg.append(",");
                    }
                }
                erg.append(")");
                if (j < weight.length - 1) {
                    erg.append(",");
                }
            }
            erg.append(")");
            if (evend1 == evend2) {
                return erg.toString();
            }
            erg.append("[").append(evbeg1).append(",").append(evend1).append("]");
            erg.append("[").append(evbeg2).append(",").append(evend2).append("]");
            return erg.toString();
        }
        switch (evord) {
            case LEX:
                erg.append("LEX");
                break;
            case INVLEX:
                erg.append("INVLEX");
                break;
            case GRLEX:
                erg.append("GRLEX");
                break;
            case IGRLEX:
                erg.append("IGRLEX");
                break;
            case REVLEX:
                erg.append("REVLEX");
                break;
            case REVILEX:
                erg.append("REVILEX");
                break;
            case REVTDEG:
                erg.append("REVTDEG");
                break;
            case REVITDG:
                erg.append("REVITDG");
                break;
            default:
                erg.append("invalid(").append(evord).append(")");
                break;
        }
        if (evord2 <= 0) {
            return erg.toString();
        }
        erg.append("[").append(evbeg1).append(",").append(evend1).append("]");
        switch (evord2) {
            case LEX:
                erg.append("LEX");
                break;
            case INVLEX:
                erg.append("INVLEX");
                break;
            case GRLEX:
                erg.append("GRLEX");
                break;
            case IGRLEX:
                erg.append("IGRLEX");
                break;
            case REVLEX:
                erg.append("REVLEX");
                break;
            case REVILEX:
                erg.append("REVILEX");
                break;
            case REVTDEG:
                erg.append("REVTDEG");
                break;
            case REVITDG:
                erg.append("REVITDG");
                break;
            default:
                erg.append("invalid(").append(evord2).append(")");
                break;
        }
        erg.append("[").append(evbeg2).append(",").append(evend2).append("]");
        return erg.toString();
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
