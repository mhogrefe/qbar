package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.math.BinaryFraction;

import java.math.BigDecimal;
import java.math.BigInteger;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class RealDemos extends QBarDemos {
    public RealDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoOf_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("of(" + r + ") = " + of(r));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_long() {
        for (long l : take(LIMIT, P.longs())) {
            System.out.println("of(" + l + ") = " + of(l));
        }
    }

    private void demoOf_int() {
        for (int i : take(LIMIT, P.integers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_BinaryFraction() {
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            System.out.println("of(" + bf + ") = " + of(bf));
        }
    }

    private void demoOf_float() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("of(" + f + ") = " + of(f));
        }
    }

    private void demoOf_double() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("of(" + d + ") = " + of(d));
        }
    }

    private void demoOfExact_float() {
        for (float f : take(LIMIT, P.floats())) {
            System.out.println("ofExact(" + f + ") = " + ofExact(f));
        }
    }

    private void demoOfExact_double() {
        for (double d : take(LIMIT, P.doubles())) {
            System.out.println("ofExact(" + d + ") = " + ofExact(d));
        }
    }

    private void demoOf_BigDecimal() {
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            System.out.println("of(" + bd + ") = " + of(bd));
        }
    }

    private void demoFuzzyRepresentation() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("fuzzyRepresentation(" + r + ") = " + fuzzyRepresentation(r));
        }
    }

    private void demoLeftFuzzyRepresentation() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("leftFuzzyRepresentation(" + r + ") = " + leftFuzzyRepresentation(r));
        }
    }

    private void demoRightFuzzyRepresentation() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("rightFuzzyRepresentation(" + r + ") = " + rightFuzzyRepresentation(r));
        }
    }

    private void demoIterator() {
        for (Real r : take(MEDIUM_LIMIT, P.withScale(4).reals())) {
            System.out.println(r + ": " + its(r));
        }
    }

    private void demoIsExact() {
        for (Real r : take(LIMIT, P.withScale(4).reals())) {
            System.out.println(r + " is " + (r.isExact() ? "" : "not ") + "exact");
        }
    }

    private void demoRationalValue() {
        for (Real r : take(LIMIT, P.withScale(4).reals())) {
            System.out.println("rationalValue(" + r +") = " + r.rationalValue());
        }
    }
}
