package jas.poly;

import jas.arith.BigRational;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

/**
 * GenPolynomial Tokenizer. Used to read rational polynomials and lists of
 * polynomials from input streams. Arbitrary polynomial rings and coefficient
 * rings can be read with RingFactoryTokenizer. <b>Note:</b> Can no more read
 * QuotientRing since end of 2010, revision 3441. Quotient coefficients and
 * others can still be read if the respective factory is provided via the
 * constructor.
 *
 * @author Heinz Kredel
 */
class GenPolynomialTokenizer {
    private String[] vars;

    private int nvars = 1;

    private TermOrder tord;

    private final StreamTokenizer tok;

    private RingFactory fac;

    private GenPolynomialRing pfac;

    private GenSolvablePolynomialRing spfac;


    public GenPolynomialTokenizer(GenPolynomialRing rf, Reader r) {
        this(r);
        System.out.println("USED");
        if (rf == null) {
            return;
        }
        if (rf instanceof GenSolvablePolynomialRing) {
            pfac = rf;
            spfac = (GenSolvablePolynomialRing) rf;
        } else {
            pfac = rf;
            spfac = null;
        }
        fac = rf.coFac;
        vars = rf.vars;
        if (vars != null) {
            nvars = vars.length;
        }
        tord = rf.tord;
    }


    /**
     * constructor with Reader.
     *
     * @param r reader stream.
     */
    @SuppressWarnings("unchecked")
    private GenPolynomialTokenizer(Reader r) {
        System.out.println("USED");
        //BasicConfigurator.configure();
        vars = null;
        tord = new TermOrder();
        nvars = 1;
        fac = new BigRational(1);

        pfac = new GenPolynomialRing<>(fac, nvars, tord, vars);
        spfac = new GenSolvablePolynomialRing_BigRational(fac, nvars, tord, vars);

        tok = new StreamTokenizer(r);
        tok.resetSyntax();
        // tok.eolIsSignificant(true); no more
        tok.eolIsSignificant(false);
        tok.wordChars('0', '9');
        tok.wordChars('a', 'z');
        tok.wordChars('A', 'Z');
        tok.wordChars('_', '_'); // for subscripts x_i
        tok.wordChars('/', '/'); // wg. rational numbers
        tok.wordChars(128 + 32, 255);
        tok.whitespaceChars(0, ' ');
        tok.commentChar('#');
        tok.quoteChar('"');
        tok.quoteChar('\'');
        //tok.slashStarComments(true); does not work

    }


    /**
     * Parsing method for GenPolynomial. syntax ? (simple)
     *
     * @return the next polynomial.
     * @throws java.io.IOException
     */
    @SuppressWarnings("unchecked")
    public GenPolynomial nextPolynomial() throws IOException {
        GenPolynomial a = pfac.getZERO();
        GenPolynomial a1 = pfac.getONE();
        ExpVector leer = pfac.evzero;

        GenPolynomial b = a1;
        GenPolynomial c;
        int tt; //, oldtt;
        //String rat = "";
        char first;
        RingElem r;
        ExpVector e;
        int ix;
        long ie;
        while (true) {
            // next input. determine next action
            tt = tok.nextToken();
            //System.out.println("while tt = " + tok);
            if (tt == StreamTokenizer.TT_EOF)
                break;
            switch (tt) {
                case ')':
                case ',':
                    return a; // do not change or remove
                case '-':
                    b = b.negate();
                case '+':
                case '*':
                    tt = tok.nextToken();
                    break;
                default: // skip
            }
            // read coefficient, monic monomial and polynomial
            if (tt == StreamTokenizer.TT_EOF)
                break;
            switch (tt) {
                // case '_': removed
                case '}':
                    throw new RuntimeException("mismatch of braces after " + a + ", error at " + b);
                case '{': // recursion
                    StringBuilder rf = new StringBuilder();
                    int level = 0;
                    do {
                        tt = tok.nextToken();
                        //System.out.println("token { = " + ((char)tt) + ", " + tt + ", level = " + level);
                        if (tt == StreamTokenizer.TT_EOF) {
                            throw new RuntimeException("mismatch of braces after " + a + ", error at "
                                    + b);
                        }
                        if (tt == '{') {
                            level++;
                        }
                        if (tt == '}') {
                            level--;
                            if (level < 0) {
                                continue; // skip last closing brace
                            }
                        }
                        if (tok.sval != null) {
                            if (rf.length() > 0 && rf.charAt(rf.length() - 1) != '.') {
                                rf.append(" ");
                            }
                            rf.append(tok.sval); // " " +
                        } else {
                            rf.append((char) tt);
                        }
                    } while (level >= 0);
                    //System.out.println("coeff{} = " + rf.toString() );
                    try {
                        r = (RingElem) fac.parse(rf.toString());
                    } catch (NumberFormatException re) {
                        throw new RuntimeException("not a number " + rf, re);
                    }
                    ie = nextExponent();
                    r = Power.positivePower(r, ie);
                    b = b.multiply(r, leer);
                    tt = tok.nextToken();
                    //no break;
                    break;

                case StreamTokenizer.TT_WORD:
                    //System.out.println("TT_WORD: " + tok.sval);
                    if (tok.sval == null || tok.sval.length() == 0)
                        break;
                    // read coefficient
                    first = tok.sval.charAt(0);
                    if (digit(first)) {
                        //System.out.println("coeff 0 = " + tok.sval );
                        StringBuilder df = new StringBuilder();
                        df.append(tok.sval);
                        if (tok.sval.charAt(tok.sval.length() - 1) == 'i') { // complex number
                            tt = tok.nextToken();
                            if (tok.sval != null || tt == '-') {
                                if (tok.sval != null) {
                                    df.append(tok.sval);
                                } else {
                                    df.append("-");
                                }
                                if (tt == '-') {
                                    tok.nextToken(); // todo: decimal number
                                    if (tok.sval != null && digit(tok.sval.charAt(0))) {
                                        df.append(tok.sval);

                                    } else {
                                        tok.pushBack();
                                    }
                                }
                            } else {
                                tok.pushBack();
                            }
                        }
                        tt = tok.nextToken();
                        if (tt == '.') { // decimal number
                            tok.nextToken();
                            if (tok.sval != null) {
                                df.append(".");
                                df.append(tok.sval);
                            } else {
                                tok.pushBack();
                                tok.pushBack();
                            }
                        } else {
                            tok.pushBack();
                        }
                        try {
                            r = (RingElem) fac.parse(df.toString());
                        } catch (NumberFormatException re) {
                            throw new RuntimeException("not a number " + df, re);
                        }
                        //System.out.println("r = " + r.toScriptFactory());
                        ie = nextExponent();
                        // r = r^ie;
                        r = Power.positivePower(r, ie);
                        b = b.multiply(r, leer);
                        tt = tok.nextToken();
                    }
                    if (tt == StreamTokenizer.TT_EOF)
                        break;
                    if (tok.sval == null)
                        break;
                    // read monomial or recursion
                    first = tok.sval.charAt(0);
                    if (letter(first)) {
                        ix = leer.indexVar(tok.sval, vars); //indexVar( tok.sval );
                        if (ix < 0) { // not found
                            try {
                                r = (RingElem) fac.parse(tok.sval);
                            } catch (NumberFormatException re) {
                                throw new RuntimeException("recursively unknown variable " + tok.sval);
                            }
                            //if (r.isONE() || r.isZERO()) {
                            //
                            //done = true;
                            //break;
                            //throw new InvalidExpressionException("recursively unknown variable " + tok.sval);
                            //}
                            ie = nextExponent();
                            //  System.out.println("ie: " + ie);
                            r = Power.positivePower(r, ie);
                            b = b.multiply(r);
                        } else { // found
                            //  System.out.println("ix: " + ix);
                            ie = nextExponent();
                            //  System.out.println("ie: " + ie);
                            e = ExpVector.create(vars.length, ix, ie);
                            b = b.multiply(e);
                        }
                        tt = tok.nextToken();
                    }
                    break;

                case '(':
                    c = nextPolynomial();
                    ie = nextExponent();
                    c = Power.positivePower(c, ie);
                    b = b.multiply(c);
                    tt = tok.nextToken();
                    //no break;
                    break;

                default: //skip
            }
            if (tt == StreamTokenizer.TT_EOF)
                break;
            // complete polynomial
            tok.pushBack();
            switch (tt) {
                case '-':
                case '+':
                case ')':
                case ',':
                    a = a.sum(b);
                    b = a1;
                    break;
                case '*':
                    //a = a.sum(b);
                    //b = a1;
                    break;
                case '\n':
                    tok.nextToken();
                    break;
                default: // skip or finish ?
            }
        }
        a = a.sum(b);
        // b = a1;
        return a;
    }


    /**
     * Parsing method for exponent (of variable). syntax: ^long | **long.
     *
     * @return the next exponent or 1.
     * @throws IOException
     */
    long nextExponent() throws IOException {
        long e = 1;
        char first;
        int tt;
        tt = tok.nextToken();
        if (tt == '^') {
            tt = tok.nextToken();
            if (tok.sval != null) {
                first = tok.sval.charAt(0);
                if (digit(first)) {
                    e = Long.parseLong(tok.sval);
                    return e;
                }
            }
        }
        if (tt == '*') {
            tt = tok.nextToken();
            if (tt == '*') {
                tok.nextToken();
                if (tok.sval != null) {
                    first = tok.sval.charAt(0);
                    if (digit(first)) {
                        e = Long.parseLong(tok.sval);
                        return e;
                    }
                }
            }
            tok.pushBack();
        }
        tok.pushBack();
        return e;
    }


    /**
     * Parsing method for solvable polynomial. syntax: p.
     *
     * @return the next polynomial.
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public GenSolvablePolynomial nextSolvablePolynomial() throws IOException {
        GenPolynomial p = nextPolynomial();
        // comments += nextComment();

        //System.out.println("ps = " + ps);
        return new GenSolvablePolynomial(spfac, p.val);
    }


    private static boolean digit(char x) {
        return '0' <= x && x <= '9';
    }


    private static boolean letter(char x) {
        return ('a' <= x && x <= 'z') || ('A' <= x && x <= 'Z');
    }


}
