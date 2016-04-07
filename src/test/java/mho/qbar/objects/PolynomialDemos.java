package mho.qbar.objects;

import mho.qbar.testing.QBarDemos;
import mho.wheels.io.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Polynomial.*;
import static mho.qbar.objects.Polynomial.sum;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

@SuppressWarnings("UnusedDeclaration")
public class PolynomialDemos extends QBarDemos {
    private static final @NotNull String POLYNOMIAL_CHARS = "*+-0123456789^x";

    public PolynomialDemos(boolean useRandom) {
        super(useRandom);
    }

    private void demoIterator() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("toList(" + p + ") = " + toList(p));
        }
    }

    private void demoApply_BigInteger() {
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.bigIntegers()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoApply_Rational() {
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.rationals()))) {
            System.out.println(p.a + " at " + p.b + " = " + p.a.apply(p.b));
        }
    }

    private void demoSpecialApply() {
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.rationals()))) {
            System.out.println("specialApply(" + p.a + ", " + p.b + ") = " + p.a.specialApply(p.b));
        }
    }

    private void demoToRationalPolynomial() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("toRationalPolynomial(" + p + ") = " + p.toRationalPolynomial());
        }
    }

    private void demoCoefficient() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("coefficient(" + p.a + ", " + p.b + ") = " + p.a.coefficient(p.b));
        }
    }

    private void demoOf_List_BigInteger() {
        for (List<BigInteger> is : take(LIMIT, P.withScale(4).lists(P.bigIntegers()))) {
            String listString = tail(init(is.toString()));
            System.out.println("of(" + listString + ") = " + of(is));
        }
    }

    private void demoOf_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("of(" + i + ") = " + of(i));
        }
    }

    private void demoOf_BigInteger_int() {
        Iterable<Pair<BigInteger, Integer>> ps = P.pairsLogarithmicOrder(
                P.bigIntegers(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<BigInteger, Integer> p : take(LIMIT, ps)) {
            System.out.println("of(" + p.a + ", " + p.b + ") = " + of(p.a, p.b));
        }
    }

    private void demoFromRoot_BigInteger() {
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            System.out.println("fromRoot(" + i + ") = " + fromRoot(i));
        }
    }

    private void demoFromRoot_Rational() {
        for (Rational r : take(LIMIT, P.rationals())) {
            System.out.println("fromRoot(" + r + ") = " + fromRoot(r));
        }
    }

    private void demoMaxCoefficientBitLength() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("maxCoefficientBitLength(" + p + ") = " + p.maxCoefficientBitLength());
        }
    }

    private void demoDegree() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("degree(" + p + ") = " + p.degree());
        }
    }

    private void demoLeading() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("leading(" + p + ") = " + p.leading());
        }
    }

    private void demoMultiplyByPowerOfX() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("multiplyByPowerOfX(" + p.a + ", " + p.b + ") = " + p.a.multiplyByPowerOfX(p.b));
        }
    }

    private void demoAdd() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomials()))) {
            System.out.println("(" + p.a + ") + (" + p.b + ") = " + p.a.add(p.b));
        }
    }

    private void demoNegate() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("-(" + p + ") = " + p.negate());
        }
    }

    private void demoAbs() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("|" + p + "| = " + p.abs());
        }
    }

    private void demoSignum() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("signum(" + p + ") = " + p.signum());
        }
    }

    private void demoSignum_BigInteger() {
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.bigIntegers()))) {
            System.out.println("signum(" + p.a + ", " + p.b + ") = " + p.a.signum(p.b));
        }
    }

    private void demoSignum_Rational() {
        for (Pair<Polynomial, Rational> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.rationals()))) {
            System.out.println("signum(" + p.a + ", " + p.b + ") = " + p.a.signum(p.b));
        }
    }

    private void demoSubtract() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomials()))) {
            System.out.println("(" + p.a + ") - (" + p.b + ") = " + p.a.subtract(p.b));
        }
    }

    private void demoMultiply_Polynomial() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomials()))) {
            System.out.println("(" + p.a + ") * (" + p.b + ") = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_BigInteger() {
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.bigIntegers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoMultiply_int() {
        for (Pair<Polynomial, Integer> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.integers()))) {
            System.out.println("(" + p.a + ") * " + p.b + " = " + p.a.multiply(p.b));
        }
    }

    private void demoDivideExact_BigInteger() {
        Iterable<Pair<Polynomial, BigInteger>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.withScale(4).polynomials(), P.nonzeroBigIntegers())
        );
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divideExact(p.b));
        }
    }

    private void demoDivideExact_int() {
        Iterable<Pair<Polynomial, Integer>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.b),
                P.pairs(P.withScale(4).polynomials(), P.nonzeroIntegers())
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / " + p.b + " = " + p.a.divideExact(p.b));
        }
    }

    private void demoShiftLeft() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println(p.a + " << " + p.b + " = " + p.a.shiftLeft(p.b));
        }
    }

    private void demoSum() {
        for (List<Polynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Σ(" + listString + ") = " + sum(ps));
        }
    }

    private void demoProduct() {
        for (List<Polynomial> ps : take(LIMIT, P.withScale(4).lists(P.withScale(4).polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Π(" + listString + ") = " + product(ps));
        }
    }

    private void demoDelta() {
        for (List<Polynomial> ps : take(LIMIT, P.withScale(4).listsAtLeast(1, P.withScale(4).polynomials()))) {
            String listString = tail(init(ps.toString()));
            System.out.println("Δ(" + listString + ") = " + its(delta(ps)));
        }
    }

    private void demoPow() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).polynomials(),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") ^ " + p.b + " = " + p.a.pow(p.b));
        }
    }

    private void demoSubstitute() {
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(P.withScale(4).withSecondaryScale(4).polynomials());
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("substitute(" + p.a + ", " + p.b + ") = " + p.a.substitute(p.b));
        }
    }

    private void demoDifferentiate() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("differentiate(" + p + ") = " + p.differentiate());
        }
    }

    private void demoIsMonic() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println(p + " is " + (p.isMonic() ? "" : "not ") + "monic");
        }
    }

    private void demoIsPrimitive() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println(p + " is " + (p.isPrimitive() ? "" : "not ") + "primitive");
        }
    }

    private void demoContentAndPrimitive() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println("contentAndPrimitive(" + p + ") = " + p.contentAndPrimitive());
        }
    }

    private void demoConstantFactor() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println("constantFactor(" + p + ") = " + p.constantFactor());
        }
    }

    private void demoPseudoDivide() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("pseudoDivide(" + p.a + ", " + p.b + ") = " + p.a.pseudoDivide(p.b));
        }
    }

    private void demoPseudoRemainder() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("pseudoRemainder(" + p.a + ", " + p.b + ") = " + p.a.pseudoRemainder(p.b));
        }
    }

    private void demoAbsolutePseudoDivide() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("absolutePseudoDivide(" + p.a + ", " + p.b + ") = " + p.a.absolutePseudoDivide(p.b));
        }
    }

    private void demoAbsolutePseudoRemainder() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("absolutePseudoRemainder(" + p.a + ", " + p.b + ") = " +
                    p.a.absolutePseudoRemainder(p.b));
        }
    }

    private void demoEvenPseudoDivide() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("evenPseudoDivide(" + p.a + ", " + p.b + ") = " + p.a.evenPseudoDivide(p.b));
        }
    }

    private void demoEvenPseudoRemainder() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("evenPseudoRemainder(" + p.a + ", " + p.b + ") = " + p.a.evenPseudoRemainder(p.b));
        }
    }

    private void demoIsDivisibleBy_Polynomial() {
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(
                P.withScale(4).polynomials(),
                P.withScale(4).polynomialsAtLeast(0)
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " is " + (p.a.isDivisibleBy(p.b) ? "" : "not ") + "divisible by " + p.b);
        }
    }

    private void demoDivideExact_Polynomial() {
        Iterable<Pair<Polynomial, Polynomial>> ps = map(
                p -> new Pair<>(p.a.multiply(p.b), p.a),
                P.pairs(P.withScale(4).polynomialsAtLeast(1), P.withScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("(" + p.a + ") / (" + p.b + ") = " + p.a.divideExact(p.b));
        }
    }

    private void demoRemainderExact() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> {
                    Pair<RationalPolynomial, RationalPolynomial> qr =
                            p.a.toRationalPolynomial().divide(p.b.toRationalPolynomial());
                    return qr.a.onlyHasIntegralCoefficients() && qr.b.onlyHasIntegralCoefficients();
                },
                P.pairs(P.withScale(4).polynomials(), P.withScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("remainderExact(" + p.a + ", " + p.b + ") = " + p.a.remainderExact(p.b));
        }
    }

    private void demoTrivialPseudoRemainderSequence() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(SMALL_LIMIT, ps)) {
            System.out.println("trivialPseudoRemainderSequence(" + p.a + ", " + p.b + ") = " +
                    p.a.trivialPseudoRemainderSequence(p.b));
        }
    }

    private void demoPrimitivePseudoRemainderSequence() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("primitivePseudoRemainderSequence(" + p.a + ", " + p.b + ") = " +
                    p.a.primitivePseudoRemainderSequence(p.b));
        }
    }

    private void demoSubresultantSequence() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("subresultantSequence(" + p.a + ", " + p.b + ") = " + p.a.subresultantSequence(p.b));
        }
    }

    private void demoGcd_Polynomial() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("gcd(" + p.a + ", " + p.b + ") = " + p.a.gcd(p.b));
        }
    }

    private void demoGcd_List_Polynomial() {
        Iterable<List<Polynomial>> pss = filterInfinite(
                p -> any(q -> q != ZERO, p),
                P.withScale(4).lists(P.withScale(4).polynomials())
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("gcd(" + listString + ") = " + gcd(ps));
        }
    }

    private void demoLcm() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomials()))) {
            System.out.println("lcm(" + p.a + ", " + p.b + ") = " + p.a.lcm(p.b));
        }
    }

    private void demoIsRelativelyPrimeTo() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a != ZERO || p.b != ZERO,
                P.pairs(P.withScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println(p.a + " is " + (p.a.isRelativelyPrimeTo(p.b) ? "" : "not ") + "relatively prime to " +
                    p.b);
        }
    }

    private void demoIsSquareFree() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println(p + " is " + (p.isSquareFree() ? "" : "not ") + "square-free");
        }
    }

    private void demoSquareFreePart() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println("squareFreePart(" + p + ") = " + p.squareFreePart());
        }
    }

    private void demoSquareFreeFactor() {
        for (Polynomial p : take(LIMIT, P.withScale(4).positivePrimitivePolynomialsAtLeast(0))) {
            System.out.println("squareFreeFactor(" + p + ") = " + p.squareFreeFactor());
        }
    }

    private void demoFactor() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println("factor(" + p + ") = " + p.factor());
        }
    }

    private void demoIsIrreducible() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println(p + " is " + (p.isIrreducible() ? "" : "not ") + "irreducible");
        }
    }

    private void demoInterpolate() {
        Iterable<List<Pair<BigInteger, BigInteger>>> pss = P.withElement(
                Collections.emptyList(),
                map(
                        p -> toList(zip(p.a, p.b)),
                        P.dependentPairsInfinite(
                                P.withScale(4).distinctListsAtLeast(1, P.withScale(4).bigIntegers()),
                                rs -> P.lists(rs.size(), P.withScale(4).bigIntegers())
                        )
                )
        );
        for (List<Pair<BigInteger, BigInteger>> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("interpolate(" + listString + ") = " + interpolate(ps));
        }
    }

    private void demoCompanionMatrix() {
        for (Polynomial p : take(MEDIUM_LIMIT, filterInfinite(Polynomial::isMonic, P.withScale(4).polynomials()))) {
            System.out.println("companionMatrix(" + p + ") = " + p.companionMatrix());
        }
    }

    private void demoCoefficientMatrix() {
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.withScale(4).listsAtLeast(1, P.withScale(4).polynomials())
                )
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("coefficientMatrix(" + listString + ") = " + coefficientMatrix(ps));
        }
    }

    private void demoAugmentedCoefficientMatrix() {
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.withScale(4).listsAtLeast(1, P.withScale(4).polynomials())
                )
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("augmentedCoefficientMatrix(" + listString + ") = " + augmentedCoefficientMatrix(ps));
        }
    }

    private void demoDeterminant() {
        Iterable<List<Polynomial>> pss = P.withElement(
                Collections.emptyList(),
                filterInfinite(
                        ps -> ps.size() <= maximum(map(Polynomial::degree, ps)) + 1,
                        P.withScale(4).listsAtLeast(1, P.withScale(4).polynomials())
                )
        );
        for (List<Polynomial> ps : take(LIMIT, pss)) {
            String listString = tail(init(ps.toString()));
            System.out.println("determinant(" + listString + ") = " + determinant(ps));
        }
    }

    private void demoSylvesterMatrix() {
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(
                P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0)
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("sylvesterMatrix(" + p.a + ", " + p.b + ") = " + p.a.sylvesterMatrix(p.b));
        }
    }

    private void demoResultant() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomialsAtLeast(0)))) {
            System.out.println("resultant(" + p.a + ", " + p.b + ") = " + p.a.resultant(p.b));
        }
    }

    private void demoSylvesterHabichtMatrix() {
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(
                                p -> p.a.degree() > p.b.degree(),
                                P.pairs(P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0))
                        ),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            System.out.println("sylvesterHabichtMatrix(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.sylvesterHabichtMatrix(t.b, t.c));
        }
    }

    private void demoSignedSubresultantCoefficient() {
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(
                                p -> p.a.degree() > p.b.degree(),
                                P.pairs(P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0))
                        ),
                        p -> P.range(0, p.a.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            System.out.println("signedSubresultantCoefficient(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.signedSubresultantCoefficient(t.b, t.c));
        }
    }

    private void demoSylvesterHabichtPolynomialMatrix() {
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(
                                p -> p.a.degree() > p.b.degree(),
                                P.pairs(P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0))
                        ),
                        p -> P.range(0, p.b.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            System.out.println("sylvesterHabichtPolynomialMatrix(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.sylvesterHabichtPolynomialMatrix(t.b, t.c));
        }
    }

    private void demoSignedSubresultant() {
        Iterable<Triple<Polynomial, Polynomial, Integer>> ts = map(p -> new Triple<>(
                p.a.a, p.a.b, p.b),
                P.dependentPairs(
                        filterInfinite(
                                p -> p.a.degree() > p.b.degree(),
                                P.pairs(P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0))
                        ),
                        p -> P.range(0, p.a.degree())
                )
        );
        for (Triple<Polynomial, Polynomial, Integer> t : take(LIMIT, ts)) {
            System.out.println("signedSubresultant(" + t.a + ", " + t.b + ", " + t.c + ") = " +
                    t.a.signedSubresultant(t.b, t.c));
        }
    }

    private void demoSignedSubresultantSequence() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() > p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("signedSubresultantSequence(" + p.a + ", " + p.b + ") = " +
                    p.a.signedSubresultantSequence(p.b));
        }
    }

    private void demoPrimitiveSignedPseudoRemainderSequence() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> (p.a != ZERO || p.b != ZERO) && p.a.degree() >= p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(4).polynomials())
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("primitiveSignedPseudoRemainderSequence(" + p.a + ", " + p.b + ") = " +
                    p.a.primitiveSignedPseudoRemainderSequence(p.b));
        }
    }

    private void demoAbbreviatedSignedSubresultantSequence() {
        Iterable<Pair<Polynomial, Polynomial>> ps = filterInfinite(
                p -> p.a.degree() > p.b.degree(),
                P.pairs(P.withScale(4).withSecondaryScale(4).polynomialsAtLeast(0))
        );
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("abbreviatedSignedSubresultantSequence(" + p.a + ", " + p.b + ") = " +
                    p.a.abbreviatedSignedSubresultantSequence(p.b));
        }
    }

    private void demoRootBound() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println("rootBound(" + p + ") = " + p.rootBound());
        }
    }

    private void demoPowerOfTwoRootBound() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomialsAtLeast(0))) {
            System.out.println("powerOfTwoRootBound(" + p + ") = " + p.powerOfTwoRootBound());
        }
    }

    private void demoRootCount_Interval() {
        Iterable<Pair<Polynomial, Interval>> ps = P.pairs(
                P.withScale(4).squareFreePolynomialsAtLeast(0),
                P.withScale(8).intervals()
        );
        for (Pair<Polynomial, Interval> p : take(LIMIT, ps)) {
            System.out.println("rootCount(" + p.a + ", " + p.b + ") = " + p.a.rootCount(p.b));
        }
    }

    private void demoRootCount() {
        for (Polynomial p : take(LIMIT, P.withScale(4).squareFreePolynomialsAtLeast(0))) {
            System.out.println("rootCount(" + p + ") = " + p.rootCount());
        }
    }

    private void demoIsolatingInterval() {
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).squareFreePolynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("isolatingInterval(" + p.a + ", " + p.b + ") = " + p.a.isolatingInterval(p.b));
        }
    }

    private void demoPowerOfTwoIsolatingInterval() {
        Iterable<Pair<Polynomial, Integer>> ps = P.dependentPairs(
                filterInfinite(p -> p.rootCount() > 0, P.withScale(4).squareFreePolynomialsAtLeast(1)),
                q -> P.range(0, q.rootCount() - 1)
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("powerOfTwoIsolatingInterval(" + p.a + ", " + p.b + ") = " +
                    p.a.powerOfTwoIsolatingInterval(p.b));
        }
    }

    private void demoIsolatingIntervals() {
        for (Polynomial p : take(LIMIT, P.withScale(4).squareFreePolynomials())) {
            System.out.println("isolatingIntervals(" + p + ") = " + p.isolatingIntervals());
        }
    }

    private void demoPowerOfTwoIsolatingIntervals() {
        for (Polynomial p : take(LIMIT, P.withScale(4).squareFreePolynomials())) {
            System.out.println("powerOfTwoIsolatingIntervals(" + p + ") = " + p.powerOfTwoIsolatingIntervals());
        }
    }

    private void demoReflect() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("reflect(" + p + ") = " + p.reflect());
        }
    }

    private void demoTranslate() {
        for (Pair<Polynomial, BigInteger> p : take(LIMIT, P.pairs(P.withScale(4).polynomials(), P.bigIntegers()))) {
            System.out.println("translate(" + p.a + ", " + p.b + ") = " + p.a.translate(p.b));
        }
    }

    private void demoSpecialTranslate() {
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.withScale(4).polynomials(), P.withScale(4).rationals());
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("specialTranslate(" + p.a + ", " + p.b + ") = " + p.a.specialTranslate(p.b));
        }
    }

    private void demoPositivePrimitiveTranslate() {
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(P.withScale(4).polynomials(), P.withScale(4).rationals());
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("positivePrimitiveTranslate(" + p.a + ", " + p.b + ") = " +
                    p.a.positivePrimitiveTranslate(p.b));
        }
    }

    private void demoStretch() {
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(
                P.withScale(4).polynomials(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("stretch(" + p.a + ", " + p.b + ") = " + p.a.stretch(p.b));
        }
    }

    private void demoPositivePrimitiveStretch() {
        Iterable<Pair<Polynomial, Rational>> ps = P.pairs(
                P.withScale(4).polynomials(),
                P.withScale(4).positiveRationals()
        );
        for (Pair<Polynomial, Rational> p : take(LIMIT, ps)) {
            System.out.println("positiveStretch(" + p.a + ", " + p.b + ") = " + p.a.positivePrimitiveStretch(p.b));
        }
    }

    private void demoShiftRootsLeft() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.withScale(4).polynomials(), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("shiftRootsLeft(" + p.a + ", " + p.b + ") = " + p.a.shiftRootsLeft(p.b));
        }
    }

    private void demoShiftRootsRight() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.withScale(4).polynomials(), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("shiftRootsRight(" + p.a + ", " + p.b + ") = " + p.a.shiftRootsRight(p.b));
        }
    }

    private void demoPositivePrimitiveShiftRootsLeft() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.withScale(4).polynomials(), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("positivePrimitiveShiftRootsLeft(" + p.a + ", " + p.b + ") = " +
                    p.a.positivePrimitiveShiftRootsLeft(p.b));
        }
    }

    private void demoPositivePrimitiveShiftRootsRight() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(P.withScale(4).polynomials(), P.naturalIntegersGeometric());
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("positivePrimitiveShiftRootsRight(" + p.a + ", " + p.b + ") = " +
                    p.a.positivePrimitiveShiftRootsRight(p.b));
        }
    }

    private void demoInvertRoots() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("invertRoots(" + p + ") = " + p.invertRoots());
        }
    }

    private void demoAddRoots() {
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(P.withScale(4).withSecondaryScale(0).polynomials());
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("addRoots(" + p.a + ", " + p.b + ") = " + p.a.addRoots(p.b));
        }
    }

    private void demoMultiplyRoots() {
        Iterable<Pair<Polynomial, Polynomial>> ps = P.pairs(P.withScale(4).withSecondaryScale(0).polynomials());
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, ps)) {
            System.out.println("multiplyRoots(" + p.a + ", " + p.b + ") = " + p.a.multiplyRoots(p.b));
        }
    }

    private void demoPowerTable() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairsLogarithmicOrder(
                P.withScale(4).monicPolynomialsAtLeast(1),
                P.withScale(4).naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("powerTable(" + p.a + ", " + p.b + ") = " + p.a.powerTable(p.b));
        }
    }

    private void demoRootPower() {
        Iterable<Pair<Polynomial, Integer>> ps = P.pairs(
                P.withScale(4).monicPolynomialsAtLeast(1),
                P.naturalIntegersGeometric()
        );
        for (Pair<Polynomial, Integer> p : take(LIMIT, ps)) {
            System.out.println("rootPower(" + p.a + ", " + p.b + ") = " + p.a.rootPower(p.b));
        }
    }

    private void demoEquals_Polynomial() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomials()))) {
            System.out.println(p.a + (p.a.equals(p.b) ? " = " : " ≠ ") + p.b);
        }
    }

    private void demoEquals_null() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            //noinspection ObjectEqualsNull
            System.out.println(p + (p.equals(null) ? " = " : " ≠ ") + null);
        }
    }

    private void demoHashCode() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println("hashCode(" + p + ") = " + p.hashCode());
        }
    }

    private void demoCompareTo() {
        for (Pair<Polynomial, Polynomial> p : take(LIMIT, P.pairs(P.withScale(4).polynomials()))) {
            System.out.println(p.a + " " + Ordering.compare(p.a, p.b).toChar() + " " + p.b);
        }
    }

    private void demoRead_String() {
        for (String s : take(LIMIT, P.strings())) {
            System.out.println("read(" + nicePrint(s) + ") = " + read(s));
        }
    }

    private void demoRead_String_targeted() {
        for (String s : take(LIMIT, P.strings(POLYNOMIAL_CHARS))) {
            System.out.println("read(" + s + ") = " + read(s));
        }
    }

    private void demoRead_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(P.strings(), P.positiveIntegersGeometric());
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("read(" + p.b + ", " + nicePrint(p.a) + ") = " + read(p.b, p.a));
        }
    }

    private void demoRead_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                P.strings(POLYNOMIAL_CHARS),
                P.positiveIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("read(" + p.b + ", " + p.a + ") = " + read(p.b, p.a));
        }
    }

    private static @NotNull Optional<String> badString(@NotNull String s) {
        boolean seenX = false;
        boolean seenXCaret = false;
        int exponentDigitCount = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'x') {
                seenX = true;
            } else if (seenX && c == '^') {
                seenXCaret = true;
            } else if (seenXCaret && c >= '0' && c <= '9') {
                exponentDigitCount++;
                if (exponentDigitCount > 3) return Optional.of("");
            } else {
                seenX = false;
                seenXCaret = false;
                exponentDigitCount = 0;
            }
        }
        return Optional.empty();
    }

    private void demoFindIn_String() {
        Iterable<String> ss = filterInfinite(
                s -> !Readers.genericFindIn(PolynomialDemos::badString).apply(s).isPresent(),
                P.strings()
        );
        for (String s : take(LIMIT, ss)) {
            System.out.println("findIn(" + nicePrint(s) + ") = " + findIn(s));
        }
    }

    private void demoFindIn_String_targeted() {
        Iterable<String> ss = filterInfinite(
                s -> !Readers.genericFindIn(PolynomialDemos::badString).apply(s).isPresent(),
                P.strings(POLYNOMIAL_CHARS)
        );
        for (String s : take(LIMIT, ss)) {
            System.out.println("findIn(" + s + ") = " + findIn(s));
        }
    }

    private void demoFindIn_int_String() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(PolynomialDemos::badString).apply(s).isPresent(),
                        P.strings()
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("findIn(" + p.b + ", " + nicePrint(p.a) + ") = " + findIn(p.b, p.a));
        }
    }

    private void demoFindIn_int_String_targeted() {
        Iterable<Pair<String, Integer>> ps = P.pairsLogarithmicOrder(
                filterInfinite(
                        s -> !Readers.genericFindIn(PolynomialDemos::badString).apply(s).isPresent(),
                        P.strings(POLYNOMIAL_CHARS)
                ),
                P.positiveIntegersGeometric()
        );
        for (Pair<String, Integer> p : take(LIMIT, ps)) {
            System.out.println("findIn(" + p.b + ", " + p.a + ") = " + findIn(p.b, p.a));
        }
    }

    private void demoToString() {
        for (Polynomial p : take(LIMIT, P.withScale(4).polynomials())) {
            System.out.println(p);
        }
    }
}
