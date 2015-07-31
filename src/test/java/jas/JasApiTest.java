package jas;

import mho.wheels.io.Readers;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JasApiTest {
    @Test
    public void testFactorPolynomial() throws Exception {
        // 6 -> [6]
        aeq(fp("[6]"), "[[6]]");

        // -x -> [-1, x]
        aeq(fp("[0, -1]"), "[[-1], [0, 1]]");

        // x^2-1 -> [x-1, x+1]
        aeq(fp("[-1, 0, 1]"), "[[-1, 1], [1, 1]]");

        // 3*x -> [3, x]
        aeq(fp("[0, 3]"), "[[3], [0, 1]]");

        // x^10 -> [x, x, x, x, x, x, x, x, x, x]
        aeq(fp("[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]"),
                "[[0, 1], [0, 1], [0, 1], [0, 1], [0, 1], [0, 1], [0, 1], [0, 1], [0, 1], [0, 1]]");

        // 3*x^6+24*x+2 -> [3*x^6+24*x+2]
        aeq(fp("[2, 24, 0, 0, 0, 0, 3]"), "[[2, 24, 0, 0, 0, 0, 3]]");

        // x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5 -> [x^8+x^6-3*x^4-3*x^3+8*x^2+2*x-5]
        aeq(fp("[-5, 2, 8, -3, -3, 0, 1, 0, 1]"), "[[-5, 2, 8, -3, -3, 0, 1, 0, 1]]");

        // x^5+x^4+x^2+x+2 -> [x^2+x+1, x^3-x+2]
        aeq(fp("[2, 1, 1, 0, 1, 1]"), "[[1, 1, 1], [2, -1, 0, 1]]");

        // x^6-41*x^5+652*x^4-5102*x^3+20581*x^2-40361*x+30031 -> [x^6-41*x^5+652*x^4-5102*x^3+20581*x^2-40361*x+30031]
        aeq(fp("[30031, -40361, 20581, -5102, 652, -41, 1]"), "[[30031, -40361, 20581, -5102, 652, -41, 1]]");

        // 3*x^8+25*x^7+12*x^6-24*x^5+86*x^4-3*x^3-3*x^2+35*x+12 -> [x^4+7*x^3-4*x^2+5*x+4, 3*x^4+4*x^3-4*x^2+5*x+3]
        aeq(fp("[12, 35, -3, -3, 86, -24, 12, 25, 3]"), "[[4, 5, -4, 7, 1], [3, 5, -4, 4, 3]]");

        // 114041041*x^8+75072771976*x^7+14078766732040*x^6+620936816292544*x^5+8987291129562544*x^4
        // -12309633851062016*x^3-1181581086409765760*x^2-4560536778732921344*x+24667056244891853056 ->
        // [10679*x^4+3055132*x^3+67216932*x^2-139102448*x-7673059216,
        // 10679*x^4+3974812*x^3+113997732*x^2+652636432*x-3214761616]
        aeq(
                fp("[24667056244891853056, -4560536778732921344, -1181581086409765760, -12309633851062016," +
                   " 8987291129562544, 620936816292544, 14078766732040, 75072771976, 114041041]"),
                "[[-7673059216, -139102448, 67216932, 3055132, 10679], [-3214761616, 652636432, 113997732, 3974812," +
                " 10679]]"
        );

        // 65536*x^72-2621440*x^70+1048576*x^69+47972352*x^68-38535168*x^67-525139968*x^66+645922816*x^65+
        // 3752198144*x^64-6507790336*x^63-17625923584*x^62+43573182464*x^61+48623316992*x^60-201052356608*x^59-
        // 21592313856*x^58+630585999360*x^57-459350970368*x^56-1192397180928*x^55+2265417959424*x^54+
        // 410196217856*x^53-5724183067136*x^52+5285853506560*x^51+7473679587840*x^50-18629447326720*x^49+
        // 2044069048384*x^48+32661373259776*x^47-32480258869952*x^46-23274174418432*x^45+73889897585728*x^44-
        // 36917063556736*x^43-79660313695104*x^42+131308121971584*x^41-2531471576720*x^40-166963370333920*x^39+
        // 140326535864464*x^38+56931043778176*x^37-192191445581264*x^36+127348114793552*x^35+69536352863328*x^34-
        // 187066595647440*x^33+104667809105817*x^32+53907642497700*x^31-126633132192912*x^30+88104329874684*x^29+
        // 2496586906368*x^28-61647822912952*x^27+56851517573884*x^26-26192073465540*x^25-207802230696*x^24+
        // 15501451719492*x^23-17905145192578*x^22+14637241748190*x^21-9783804452778*x^20+3753647987988*x^19+
        // 276759138184*x^18-1703416482184*x^17+1741593353124*x^16-1076237434256*x^15+506022665406*x^14-
        // 217962361777*x^13+45152941216*x^12+25163688232*x^11-34238911848*x^10+23261035580*x^9-7927272793*x^8+
        // 494427179*x^7+771191070*x^6-470115239*x^5+133329032*x^4-13719648*x^3-552480*x^2+235008*x-19440 ->
        // [65536*x^72-2621440*x^70+1048576*x^69+47972352*x^68-38535168*x^67-525139968*x^66+645922816*x^65+
        // 3752198144*x^64-6507790336*x^63-17625923584*x^62+43573182464*x^61+48623316992*x^60-201052356608*x^59-
        // 21592313856*x^58+630585999360*x^57-459350970368*x^56-1192397180928*x^55+2265417959424*x^54+
        // 410196217856*x^53-5724183067136*x^52+5285853506560*x^51+7473679587840*x^50-18629447326720*x^49+
        // 2044069048384*x^48+32661373259776*x^47-32480258869952*x^46-23274174418432*x^45+73889897585728*x^44-
        // 36917063556736*x^43-79660313695104*x^42+131308121971584*x^41-2531471576720*x^40-166963370333920*x^39+
        // 140326535864464*x^38+56931043778176*x^37-192191445581264*x^36+127348114793552*x^35+69536352863328*x^34-
        // 187066595647440*x^33+104667809105817*x^32+53907642497700*x^31-126633132192912*x^30+88104329874684*x^29+
        // 2496586906368*x^28-61647822912952*x^27+56851517573884*x^26-26192073465540*x^25-207802230696*x^24+
        // 15501451719492*x^23-17905145192578*x^22+14637241748190*x^21-9783804452778*x^20+3753647987988*x^19+
        // 276759138184*x^18-1703416482184*x^17+1741593353124*x^16-1076237434256*x^15+506022665406*x^14-
        // 217962361777*x^13+45152941216*x^12+25163688232*x^11-34238911848*x^10+23261035580*x^9-7927272793*x^8+
        // 494427179*x^7+771191070*x^6-470115239*x^5+133329032*x^4-13719648*x^3-552480*x^2+235008*x-19440]
        aeq(
                fp(
                        "[-19440, 235008, -552480, -13719648, 133329032, -470115239, 771191070, 494427179," +
                        " -7927272793, 23261035580, -34238911848, 25163688232, 45152941216, -217962361777," +
                        " 506022665406, -1076237434256, 1741593353124, -1703416482184, 276759138184, 3753647987988," +
                        " -9783804452778, 14637241748190, -17905145192578, 15501451719492, -207802230696," +
                        " -26192073465540, 56851517573884, -61647822912952, 2496586906368, 88104329874684," +
                        " -126633132192912, 53907642497700, 104667809105817, -187066595647440, 69536352863328," +
                        " 127348114793552, -192191445581264, 56931043778176, 140326535864464, -166963370333920," +
                        " -2531471576720, 131308121971584, -79660313695104, -36917063556736, 73889897585728," +
                        " -23274174418432, -32480258869952, 32661373259776, 2044069048384, -18629447326720," +
                        " 7473679587840, 5285853506560, -5724183067136, 410196217856, 2265417959424, -1192397180928," +
                        " -459350970368, 630585999360, -21592313856, -201052356608, 48623316992, 43573182464," +
                        " -17625923584, -6507790336, 3752198144, 645922816, -525139968, -38535168, 47972352," +
                        " 1048576, -2621440, 0, 65536]"),
                        "[[-19440, 235008, -552480, -13719648, 133329032, -470115239, 771191070, 494427179," +
                        " -7927272793, 23261035580, -34238911848, 25163688232, 45152941216, -217962361777," +
                        " 506022665406, -1076237434256, 1741593353124, -1703416482184, 276759138184, 3753647987988," +
                        " -9783804452778, 14637241748190, -17905145192578, 15501451719492, -207802230696," +
                        " -26192073465540, 56851517573884, -61647822912952, 2496586906368, 88104329874684," +
                        " -126633132192912, 53907642497700, 104667809105817, -187066595647440, 69536352863328," +
                        " 127348114793552, -192191445581264, 56931043778176, 140326535864464, -166963370333920," +
                        " -2531471576720, 131308121971584, -79660313695104, -36917063556736, 73889897585728," +
                        " -23274174418432, -32480258869952, 32661373259776, 2044069048384, -18629447326720," +
                        " 7473679587840, 5285853506560, -5724183067136, 410196217856, 2265417959424, -1192397180928," +
                        " -459350970368, 630585999360, -21592313856, -201052356608, 48623316992, 43573182464," +
                        " -17625923584, -6507790336, 3752198144, 645922816, -525139968, -38535168, 47972352," +
                        " 1048576, -2621440, 0, 65536]]"
                );
    }

    private static void aeq(Object a, Object b) {
        assertEquals(a.toString(), b.toString());
    }

    private static @NotNull List<BigInteger> readBigIntegerList(@NotNull String s) {
        return Readers.readList(Readers::readBigInteger).apply(s).get();
    }

    private static List<List<BigInteger>> fp(String s) {
        return JasApi.factorPolynomial(readBigIntegerList(s));
    }
}