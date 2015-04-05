package mho.qbar.iterableProviders;

import mho.wheels.iterables.IterableUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static mho.wheels.iterables.IterableUtils.take;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class QBarRandomProviderTest {
    private static QBarRandomProvider P;

    @Before
    public void initialize() {
        P = new QBarRandomProvider(new Random(0xe74871c570ec4e13L));
    }

    @Test
    public void testRationals_Int() {
        aeq(take(20, P.withRationalMeanBitSize(6).rationals()),
                "[7, 1, 0, -1/8, -1, -2/3, -1, -5, 0, 1/3, 1, -2/3, -1/29, 4, -1/2, -12, -1, 0, -8/13, -2/7]");
        aeq(take(20, P.withRationalMeanBitSize(8).rationals()),
                "[0, 13, 12/37, -1/14, 30, -2, 1/5, -887/21, 120/43, -1," +
                " -1, -1, -1/2, -1/8, 17/3, -1/14, 2/3, 0, -2, -1/3]");
        aeq(take(20, P.withRationalMeanBitSize(10).rationals()),
                "[-1/6, -1/7, 27, 0, 23/3, -42, 58/9, -1, -1/100, -1/7," +
                " -1/36, -18/479, -1/2, -1, -1/7, 1/71, 0, -3029/228, 12/5, -1/2]");
        aeq(take(20, P.withRationalMeanBitSize(20).rationals()),
                "[20/1327, -1/4964, -89/3, 11/12896222, -37/3257784, 1042026, -1/55, 1192/255, -5/16," +
                " -165510/454753, -1/4, -3752/3, 5/141, 36991, -27338/1397, 47/17546, -2727, -3/5987, 3, 1/7827]");
        aeq(take(20, P.withRationalMeanBitSize(200).rationals()),
                "[7794647805636643454127718981794986731065179168717528319/804103074140120138245329048569086589871948" +
                "685327066372930328248598," +
                " 221570096366670650938512248490/21354527, -15110369239612982353183412694720788925915864165626940238" +
                "48857461438/16606612020383430259242013252751366334858153234716250267454912171," +
                " 50527/1079392977429," +
                " 31098440355702495324052966597287045395071806778002185888479039114438825272794203116481846428077989" +
                "28518611169350444709/178844821276309668541370465341732933396848349320," +
                " -9/73095306738151210803875896447948946641716559617846137522," +
                " -861237387630774251/188814504643444287, 99118670653/1730624552886962413266034936090230188," +
                " -5073913952727835324043438123/16218, 7119112/1377480333757833001641511607," +
                " -16/1460134460914060035822172621127485600334216863307," +
                " -354095404790544114789/36605356388967961637585244419199865807175472282808753867414877," +
                " 6017145870466733640000745183718128/486969463948452063949193282045755," +
                " 22896954610199180446177012264928613415538771406340054726801714578110220887130293718310994198432749" +
                "59386456/281102221," +
                " -72477291133881/45383442686067190747676017901898072429241146226181812379453000," +
                " 17108/89022452291343406294138495824117618001145271085483969981853006421436737731802415768883651282" +
                "93982357069650740378985170712419350184447069, -21540993935236376131660978535829829577/2794118," +
                " -97572928275367678961/581577743664668876842, 24544331/580844203," +
                " -15110091853/4049564061150907586767959329048598]");
        try {
            P.withRationalMeanBitSize(5).rationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(0).rationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(-4).rationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testRationals() {
        aeq(take(20, P.rationals()),
                "[71355446639017567888, 91245879355/27224294621607, -230757/305466977," +
                " -188375/310365574829728880964699825396121, 1060069125/768205638868, 55/5415417863184621," +
                " -1304981404/3961937918623, 71643/16, -56/3, 127, 8/43667, 41585967255/228709, -1/1450144, -94/163," +
                " 9/2107252391, -1/10093020944, 13511/350217731144, 2265370/73, -598721/59318667634," +
                " -6041656257/115663]");
    }

    @Test
    public void testNonNegativeRationals_Int() {
        aeq(take(20, P.withRationalMeanBitSize(6).nonNegativeRationals()),
                "[7, 0, 0, 0, 0, 1, 0, 0, 1, 1/10, 0, 1/6, 0, 0, 1/3, 1, 19, 1/3, 0, 0]");
        aeq(take(20, P.withRationalMeanBitSize(8).nonNegativeRationals()),
                "[1, 5, 1, 0, 2/3, 1, 1/15, 6/5, 3, 7, 3, 1/4, 2/7, 1/16, 3, 9, 0, 0, 0, 120]");
        aeq(take(20, P.withRationalMeanBitSize(10).nonNegativeRationals()),
                "[3, 378, 2, 27, 1/137, 1/10, 23, 16, 3, 4, 1, 0, 0, 2/1275, 22/21, 1, 3/2, 12, 1/1690, 23/4]");
        aeq(take(20, P.withRationalMeanBitSize(20).nonNegativeRationals()),
                "[4/3, 28/7691, 32/127, 5/63789, 6391/13858742509, 38/749, 5/723, 47/3, 12847/1341, 3/5," +
                        " 1/102527, 8, 2726/5, 39/3659, 13/2, 377/2, 2, 0, 163/438702, 4895/2]");
        aeq(take(20, P.withRationalMeanBitSize(200).nonNegativeRationals()),
                "[130930763182270660228663614013295761916501829255314527169021191457514666359558387588238964808959/7" +
                "517586777550828054626795662503, 216821/101419744017795180979313623318," +
                " 8432222209/3472916303802927696667141042973436153514," +
                " 109075443415234998413675268968903162899292281522067008545634886614273778365/1409662300984247073," +
                " 40244563659976491035220893845859613086/183875243," +
                " 50527/99244512490630600984484746207488440272815326551723319543270638504972182417331169219578973600" +
                "20502286025542015027338969477985265317," +
                " 7965040635321302351304013292694477500015437/183750625465175779505276265349101297108607903180002057" +
                "183, 41370335581718261747544169636254603412992954953943640640, 9471485831/15159019072," +
                " 99118670653/739522645111015134336927852406, 3/24608," +
                " 128272421474007609228256640954861882877744/1410063030826718427074900563293759788273051472528258478" +
                "927711, 7767480419577776911791358616454980/5140793647567668921712781661254915271921807," +
                " 31625195462219/1285299460546232934466888651629061955446184873," +
                " 10439/74629226064088438771793747293, 6017145870466733640000745183718128/18592538082321198897469070" +
                "784418778765776524023525522415240483488251998617817033644198190671797926285692061979350263343," +
                " 2657758105757474127110498134402601344575637909949350767497984158488861189/446798755076486851094948" +
                "636315026049797920379417013479720792361972424256512886438489492145430803428272665687896," +
                " 3400089547/20678688052, 6274844981898338/6901207, 110608108733832818689476407123450600294030423224" +
                "6152191886001886094311131611639562713878468940952365683622402882942520413/3618233608561748395950355" +
                "85967598041032]");
        try {
            P.withRationalMeanBitSize(5).nonNegativeRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(0).nonNegativeRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(-4).nonNegativeRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNonNegativeRationals() {
        aeq(take(20, P.nonNegativeRationals()),
                "[9872, 631080105875781179885739711837121221/23345746760540219, 882513915197340422289/3080," +
                " 464006054/118127, 3681/450518, 1627580491192244740391197081/8, 155/3996998099357210587," +
                " 8/26013112309612695, 2/447122575, 76/1254366637715, 83728/275655, 2020863/20807665369603327," +
                " 271669839584144, 1/73865656, 928935/741109, 5/180502, 8432222209/1470837266188852473," +
                " 444877527142133/59297, 1/1981, 182908917044197/119430886247]");
    }

    @Test
    public void testPositiveRationals_Int() {
        aeq(take(20, P.withRationalMeanBitSize(6).positiveRationals()),
                "[15/7, 1/8, 13/20, 1, 1/3, 1, 1/3, 5/14, 1, 1/3, 1/2, 1, 1/3, 5, 1, 1, 3, 3/13, 1/12, 1]");
        aeq(take(20, P.withRationalMeanBitSize(8).positiveRationals()),
                "[101238, 1/4, 7/16, 1/2, 1/8, 1/10, 1, 3, 1/6, 2, 1/5, 59/3, 1, 202, 15/2, 1, 4, 20/3, 1, 4]");
        aeq(take(20, P.withRationalMeanBitSize(10).positiveRationals()),
                "[1/2, 86, 3/15316, 7/44, 9, 151/57, 1, 61/6, 4/9, 100, 1, 7/292, 3/2, 1/212, 1, 27, 7/1437, 100/11," +
                " 16/3, 3717/29]");
        aeq(take(20, P.withRationalMeanBitSize(20).positiveRationals()),
                "[13/102527, 1/109498956, 5258/5, 326/3659, 1, 11987/3, 550, 1/25088720, 1/1212, 8/3, 5480445/8," +
                " 107/1095, 1, 1/97, 4/1056300287, 5/3, 40, 40/1581839, 59120946/5, 3/3053]");
        aeq(take(20, P.withRationalMeanBitSize(200).positiveRationals()),
                "[141502/335231867706868192600337349063996732925210220322146235185200480379729109693," +
                " 82779859525093798968142719774830639518/312671, 43831224199/648874484541," +
                " 17974645478209623401038919939020913285141047338/259, 477347200/101993939639883," +
                " 1784646922309452955045374686002717796/26823," +
                " 172170645343581217752976627718761200/9725146364871011445443688137374275407885573497451826599984408" +
                "963659340293, 7695056843/901026160932673945139952746938267374073697548770," +
                " 5161990387138/368833096542523677655068324323751787727001002507537570471666124263495875362848075734" +
                "3523822221090197119525574855690013789, 1089084504490390797692523504/326811157579," +
                " 12750371229228902590/28265363991229, 125303/14911198131099473," +
                " 275223825703365147091279783/27675328938276548926887345070530837272218148079114," +
                " 112678051232/16952077774158324824975," +
                " 1815951617228423251/113565606437461834104568691668983046255662504685099882006541722255632199965393" +
                "48445004402171746369665878612993060279663, 8228044561649665419461/2143742193959235," +
                " 3146347802622966119593060140756024878108882373217055364573/3047544264420650920414," +
                " 59882112857232/67281415921," +
                " 8391847397286266028663628628121949/4460786916521119836043153499810635802022499595724107621041," +
                " 4954521818195046/12856828686736625]");
        try {
            P.withRationalMeanBitSize(5).positiveRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(0).positiveRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(-4).positiveRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testPositiveRationals() {
        aeq(take(20, P.positiveRationals()),
                "[65649474733/106, 1295694103768239116337643241977293509/3243697156632163029137, 4758973350/69217," +
                " 311/27183283269, 411/13367, 8116, 35575295/834619793005456, 1/3, 1977511/2684558, 13/25612091393," +
                " 653681468/745858870465509, 207340146710045373/35900041414060629676, 1806369509808465/183875243," +
                " 15188529625423281088203241091101924001149/16122, 32765631998/25," +
                " 24/1462799122284050482322521118906122522402619," +
                " 632733247/4062154865179376896726729779273543364381497681353065, 376187/506," +
                " 116321541066754036112/2389, 1/246685945776604142]");
    }

    @Test
    public void testNegativeRationals_Int() {
        aeq(take(20, P.withRationalMeanBitSize(6).negativeRationals()),
                "[-15, -7/3, -1/2, -8, -13/5, -1, -1, -1, -3, -1, -1/3, -1, -3, -1/4, -1, -1/2, -1/3, -2/3, -1, -1]");
        aeq(take(20, P.withRationalMeanBitSize(8).negativeRationals()),
                "[-7, -703/393, -12, -1, -1/3, -12, -13/3, -1/13, -1/17, -1," +
                " -1/6, -1/3, -15/2, -5/7, -1, -3, -1, -248, -2/11, -1/3]");
        aeq(take(20, P.withRationalMeanBitSize(10).negativeRationals()),
                "[-16, -4, -4/3, -1/890, -1/6, -1, -4/283, -1/3, -137/3, -10/87," +
                        " -1/2, -62, -186, -6, -1, -2/19, -1, -1/3, -1, -264]");
        aeq(take(20, P.withRationalMeanBitSize(20).negativeRationals()),
                "[-879/69217, -10/7, -11/795956, -13/7, -31/2, -57/21520, -39, -19979421/3998054, -7691/96," +
                " -127/2014, -292/21, -63789/2, -2468/14583, -13858742509/294, -749/79, -411/13, -723/175, -3/2," +
                " -48807/29231, -1341/11]");
        aeq(take(20, P.withRationalMeanBitSize(200).negativeRationals()),
                "[-102527/17312403, -316463874199/6, -447122575/1176," +
                " -704610823827/314303311930083419864406931013330887951732950353459512916006550760406098384467212407" +
                "2322565195350251261283498014102904063, -7517586777550828054626795662503/741109," +
                " -3472916303802927696667141042973436153514/33523186770686819260033734906399673292521022032214623518" +
                "5200480379729109693, -1409662300984247073/82779859525093798968142719774830639518," +
                " -183875243/312671," +
                " -9924451249063060098448474620748844027281532655172331954327063850497218241733116921957897360020502" +
                "286025542015027338969477985265317/6298430515363845415357007470, -1/43831224199," +
                " -15159019072/648874484541," +
                " -1410063030826718427074900563293759788273051472528258478927711/2853666785371708742591334393333536" +
                "4, -5140793647567668921712781661254915271921807/477347200," +
                " -1285299460546232934466888651629061955446184873/1784646922309452955045374686002717796," +
                " -74629226064088438771793747293/172170645343581217752976627718761200," +
                " -1859253808232119889746907078441877876577652402352552241524048348825199861781703364419819067179792" +
                "6285692061979350263343/9725146364871011445443688137374275407885573497451826599984408963659340293," +
                " -4467987550764868510949486363150260497979203794170134797207923619724242565128864384894921454308034" +
                "28272665687896/7695056843, -6901207/176694600747696211068473992298355492424," +
                " -448/368833096542523677655068324323751787727001002507537570471666124263495875362848075734352382222" +
                "1090197119525574855690013789," +
                " -8470734525663105536958803543893487063052840666166897116597224/326811157579]");
        try {
            P.withRationalMeanBitSize(5).negativeRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(0).negativeRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(-4).negativeRationals();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNegativeRationals() {
        aeq(take(20, P.negativeRationals()),
                "[-65649474733/50, -42640, -1295694103768239116337643241977293509/23345746760540219," +
                " -3243697156632163029137/3080, -4758973350/118127, -69217/450518, -4103460569763005290189445529/8," +
                " -311/4742738, -27183283269/1631119, -411/3996998099357210587, -13367/20607, -6/447122575," +
                " -588/1254366637715, -83643179986/12791135355689241, -1/43665, -35575295/20807665369603327," +
                " -834619793005456, -1/6, -3/73865656, -1977511/741109]");
    }

    @Test
    public void testNonNegativeRationalsLessThanOne_Int() {
        aeq(take(20, P.withRationalMeanBitSize(6).nonNegativeRationalsLessThanOne()),
                "[0, 0, 0, 0, 0, 0, 1/10, 0, 1/6, 0, 0, 1/3, 1/3, 0, 0, 1/12, 0, 1/3, 0, 0]");
        aeq(take(20, P.withRationalMeanBitSize(8).nonNegativeRationalsLessThanOne()),
                "[0, 0, 0, 0, 1/248, 0, 2/55, 0, 0, 1/3, 74/87, 0, 0, 4/5, 1/21, 0, 0, 1/151, 8/13, 1/8]");
        aeq(take(20, P.withRationalMeanBitSize(10).nonNegativeRationalsLessThanOne()),
                "[0, 1/181, 84/1769, 3/61, 1/7, 13/97, 2/7, 0, 3/8, 1/25," +
                " 1/22, 7/264, 1/7, 0, 2/3, 1/52, 1/3, 1/4, 3/125, 0]");
        aeq(take(20, P.withRationalMeanBitSize(20).nonNegativeRationalsLessThanOne()),
                "[1/3, 1/22935, 43/318, 0, 1/6, 1/288537, 8/209, 1/581, 1/14, 0," +
                " 12/8843, 715/16519, 1/52, 7/74, 19/15165, 5/264, 4/389547, 62/77, 1/4, 1/824896]");
        aeq(take(20, P.withRationalMeanBitSize(200).nonNegativeRationalsLessThanOne()),
                "[8/19115413234586613922071872429018658253005645," +
                " 85670910849758892570342055611481509357008599360251517919/43368919404338580948728100858673375441939" +
                "0170232945797696, 6569084480/648874484541, 2706903/176694600747696211068473992298355492424," +
                " 192/3688330965425236776550683243237517877270010025075375704716661242634958753628480757343523822221" +
                "090197119525574855690013789, 173897/275223825703365147091279783," +
                " 951732525077793218487970312495/71925553759405163093812054518105585024068553436," +
                " 4259067536175257580045/367522450405311323573981133621122, 50886674322/16952077774158324824975," +
                " 239767048199442843065659531849756404472293137585348/8855310663888228009861873107670577595111802900" +
                "408961035557172423321370127080848108208785, 90489965462802886/1815951617228423251," +
                " 498744531555493424/1135656064374618341045686916689830462556625046850998820065417222556321999653934" +
                "8445004402171746369665878612993060279663, 527702914570/8228044561649665419461," +
                " 2297640569482110469993/3146347802622966119593060140756024878108882373217055364573," +
                " 225176190354577958893/8391847397286266028663628628121949," +
                " 16463773253254/4460786916521119836043153499810635802022499595724107621041," +
                " 1810679170864420561862755191/126456984828725371321103379756615440565966281542317113182463400903649" +
                "68491127122, 859081323/143284950272832419208508," +
                " 682593906260878534705/151290350198800408473876763059169102633462652," +
                " 86984647404509122/160285187112559912264544581772315655]");
        try {
            P.withRationalMeanBitSize(5).nonNegativeRationalsLessThanOne();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(0).nonNegativeRationalsLessThanOne();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withRationalMeanBitSize(-4).nonNegativeRationalsLessThanOne();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testNonNegativeRationalsLessThanOne() {
        aeq(take(20, P.nonNegativeRationalsLessThanOne()),
                "[3681/450518, 155/3996998099357210587, 8/26013112309612695, 2/447122575, 76/1254366637715," +
                " 83728/275655, 2020863/20807665369603327, 1/73865656, 5/180502, 8432222209/1470837266188852473," +
                " 1/1981, 152963368732/1804440036591445757873649123471162931269," +
                " 8/36459757298634746402877564294850651269, 8224/56893, 8/962838067903582178633832113, 23/115919," +
                " 159320074399/7311977145354052, 10439/74629226064088438771793747293, 7/566560767997026052848," +
                " 221048/105069346758991147]");
    }

    @Test
    public void testFinitelyBoundedIntervals_int() {
        aeq(take(20, P.withIntervalMeanBitSize(12).finitelyBoundedIntervals()),
                "[[0, 0], [-1/3, 12], [-1/7, 0], [-1/9, -1/21], [-1, -1], [-1, -1/4], [-1/9, 0], [-3, -1]," +
                " [-1/3, 0], [-1/20, 1], [-1, 0], [0, 1], [-1, 0], [-1/10, -1/11], [-1, -1], [-1/5, -1/228]," +
                " [-1/77, 0], [-3, 0], [-2/29, 0], [-7, 1]]");
        aeq(take(20, P.withIntervalMeanBitSize(16).finitelyBoundedIntervals()),
                "[[-1/9, 0], [-1/3, 4], [0, 2], [-2/9, 3/4], [1/41, 94/3], [-1/3, 2/3], [-1/6, 2], [-3, -1/4]," +
                " [-1, -1/3], [1/2, 1/2], [1/2, 2/3], [-2, 0], [-101, -1], [-5, 0], [0, 1/3], [-1, -1/3]," +
                " [-2, -1/7], [-2, 0], [-5/2, 2], [-1/5, 1/182]]");
        aeq(take(20, P.withIntervalMeanBitSize(20).finitelyBoundedIntervals()),
                "[[-1/3, -5/97], [-1, 7/3], [-26958, -1/3], [-2/39, 0], [-1/6, -1/432], [-1/7, 0], [-1/6, 5/3]," +
                " [-1, 1], [-16/5, 21], [-2/15, -22/455], [-5/4, -1/3], [-16/287, 1/26], [-2/7, 1/70], [4, 661]," +
                " [3/76, 1/10], [-1/7, 1/2], [2/123, 91], [-14/3, -3], [-4/13, 0], [-2/9, 77/103]]");
        aeq(take(20, P.withIntervalMeanBitSize(40).finitelyBoundedIntervals()),
                "[[-4/131, 219189/2], [-1/13, 1/5], [-4/53, -1/33216], [-17/3, -1/110], [-1/17, 4]," +
                " [-330424, 1632123/64], [-1/10, 59/26], [-13918733/216, -11/26], [55/2, 632803/54]," +
                " [67201/3, 15974091158/7], [-13/140, 1/82], [-627/7, 1/10], [-11/2875, 25/75636], [-1, -1/2]," +
                " [-5544/25, 13/80], [-395/234155341, 27706/1633], [3/11, 215/13], [-1308013/12, -15/29846]," +
                " [-9/40540, 27/1490842], [-67040/259, 674/973991]]");
        aeq(take(20, P.withIntervalMeanBitSize(400).finitelyBoundedIntervals()),
                "[[-926215/16431664650073854891536325743737743, 1269647057469196732113434901261539491465837215270833" +
                "853659409114076315223036062528482350010495831261832116227541734767/16715446852915054025415698125679" +
                "35]," +
                " [-1713410954839215216832662/32983305156931084282922857, 450922190824550/548684970873893370404581]," +
                " [-7012122517468781523685275997701955768893268633041034029875904175247699/11, -21/50379189155788936" +
                "0622325672089418745715242458855989133256762426347497689371690767]," +
                " [-1269567486975079976761629/27004, 42647142203833783933572399651345349284162791860/203453185951]," +
                " [-74752697976827501440079226119/5, -55311456963964128908796638983/24254048764965161476340366301990" +
                "440237601059], [-3791935756459/266417405135653229412748, -179528851/11512487496030678815831036154]," +
                " [-16330825198033798338982981280647075364198503357919003068774700292484173149, 26328607871277882947" +
                "89606321361160228641128435306747693593996250/27226871397984831564871313502089497571]," +
                " [-481424211657729561591944132891816380486089791189/2388710719507763381948550998526849, -7558301750" +
                "3943004561256/1665298797711051551302415618548681462814474315676592006250735551524577152544073781716" +
                "62864064539107272373573054375302870875839]," +
                " [373/1374395997993679780901802800772330528966943360728316832, 381380281848029921057656926427251677" +
                "5750318228317501/7706404660]," +
                " [-38573784139236100759/7745341538573462523, 121792234028108979127507378191346066744348348797432250" +
                "87289/1920231756866729098961547460570]," +
                " [124363388241677/19147629618630610392, 168519553615478507799978066582489053764/113]," +
                " [-23172342127181231148389108579552452307/3335753110, 457668200335972914339390738993391114474200327" +
                "26778504639106220202096389685644694123604455156956345358005267295065302349333849/328360141765885919" +
                "985110067101283196192]," +
                " [-488763065363/396583121, -12656821598982751281369964825084832797516923/18686975852117084310539533" +
                "917987182715290961199322497531917292687526750016103365343036289425284381587947187]," +
                " [-4472046529598644010/86223046907, 8800997073218/109500169029]," +
                " [-138194430279171278814537918102047032802076520155372447576121336004649712765985/50747418179679877" +
                "1492128520049689727022593575420384446, -4094153812563045999624328887069418835162613706953454762750/" +
                "741673084451901547780227091207954696350677311518135840236970673836070934945991637]," +
                " [-11941126889968055396824206247135114681862601502584486668534887486419243984225053674262451/818336" +
                "16212460240653, 54038702192283630043621577197/46740293007322683431565217198097245]," +
                " [-291678547718360127173021/6547, -2/19997641879220583488015798030341016404403964000566876389048941" +
                "197]," +
                " [-216481482409361269119465681830415358854574958343646275783565201007508633114318174736280907941071" +
                "083591563951117/8, -26329301931781/13]," +
                " [-10459079250757188425618579918272044405689712944/59390854069, -3087873860208388074855143839208628" +
                "26/5], [-3071566/125081370382930696447279, 25/126314299223530276037472719461577496440437473573]]");
        try {
            P.withIntervalMeanBitSize(11).finitelyBoundedIntervals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withIntervalMeanBitSize(0).finitelyBoundedIntervals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withIntervalMeanBitSize(-4).finitelyBoundedIntervals();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testFinitelyBoundedIntervals() {
        aeq(take(20, P.finitelyBoundedIntervals()),
                "[[-122225533/173, 2/4470747], [-81166/3, -27338/89455], [-2727/35983, -76/41849]," +
                " [-606/163, 173001], [-12873/704, 116776191/1954], [-23/27001466, 31/1570777433984757]," +
                " [-243397036/71863696713244618325, 680469602965841/378174], [-1/10, 300608/251]," +
                " [-1306646/61, -105/1963], [-52/12688009, 1/6669], [-250547/1523, -155472/5734481]," +
                " [-3942/2045, 854], [1/59173615645, 922/301], [-20, -596515976/551381883402049201]," +
                " [-100506/3450383846202827465459, 1/240179133498771705564], [832674/37957, 3676423311/14]," +
                " [-1/222, 576331051/248], [-15187/15, 55/2], [30/3704443, 22122/187], [-10/429, 3/8]]");
    }

    @Test
    public void testIntervals_int() {
        aeq(take(20, P.withIntervalMeanBitSize(12).intervals()),
                "[[0, 1], [-1/3, -1/3], [0, 0], [-1, -2/3], [0, 0], [0, 3], [-1, -1/2], [-1, 0], [1, 23/3], [0, 1]," +
                " [-2, 0], [-1, 0], [-1/4, 3/17], [-3/2, -1/3], [0, 3/13], [-1/10, 0], [-1/25, 0], [-1/7, 0]," +
                " [-1/23, 1], [-7, 106]]");
        aeq(take(20, P.withIntervalMeanBitSize(16).intervals()),
                "[[-1, -1], [-2, 2/3], [-1/2, -2/9], (-Infinity, -2/13], (-Infinity, -2/7], [-2, -1/21], [-2, 0]," +
                " [-1/42, -1/101], [-1/2, -1/4], [-22, -1/138], [-2, -2/3], [0, 3/5], [-1, 1], [1, 29/3]," +
                " (-Infinity, -1/2], [1/2, 1/2], [1/3, 124/59], [-1, -13/89], [-2, 1/18], [13/1357, Infinity)]");
        aeq(take(20, P.withIntervalMeanBitSize(20).intervals()),
                "[[-1/3, -1/3], [-1/4, 5/498], [-1/30, 1/3], [-1, -1], (-Infinity, -2/993], [-73/12783, 0]," +
                " [-3, 2/71], [-1, 1/88654], [-8/3, -1/152199], [-1, -1/59], [-5/2404, 1], [-1/28, 1/33], [-1, 0]," +
                " [-1/10, -1/55], (-Infinity, -57/31], [-3, 3/7], [25/2, 1157/2], [-1/12, 1/1172], [-1, -1]," +
                " [-3, -1]]");
        aeq(take(20, P.withIntervalMeanBitSize(40).intervals()),
                "[[-1/9215, 936077171636/103], [3/336217, 264], [-83/10, -1/146640], [-13918733/216, 1/16248]," +
                " [-2027/26, -26], [-1, 1694/7093], [-1/4, -2/9775], [-1/161, 1/61], [-1/216564772, 84374/7]," +
                " [2/229, 1/82], [25/75636, 1/7], [-5/6, 7069], [-254884793/3, -1], [-1/27727, 2206/9]," +
                " [-2/13857749, 45/1169], [-1797992175/29, -395/234155341], [-1/229, 3906/11], [-1/251272, 8]," +
                " [-30553462/61, -51/14474], [-1/46, -9/40540]]");
        aeq(take(20, P.withIntervalMeanBitSize(400).intervals()),
                "[[-2932300622767992843722981339082551986476444745085930464/2658384113574453715843608302278670216568" +
                "049, 3694/477901135205056207263719374685047]," +
                " [39715934792773646/6384149652812409395183654221803368013262677, 5772199270980022726104656973113175" +
                "23635743235943/1807117226803081091255]," +
                " [41239731918267379660129135028/203453185951, 58133145853958315969796220202453451461571612/11658170" +
                "920345034542417], [-1134453948586457409, 1263680632507/4592747511577969521646]," +
                " [-4/122702611930022446695427608637802311756213285458120581678547053528717722178952447813548627, 24" +
                "667994125/15427796930458401831517660213494193]," +
                " [-111463527513/183982528883456020025454407762961699052314867040255056605463221, 3589629911797325/7" +
                "8], [-104656597814683134323497605337674102933729152434939987146616257452604090968222434373556939863" +
                "96981198695303968/264311687, 736353882810710572558340/3261191648023201354415497]," +
                " [14901/3199045212885382290510473051695006949675970611168534638849095664905036431834577878594585901" +
                "4689837354838739771920394702514178035137, 37413208329]," +
                " [-7188689105331884/3, -67215049970869454/1859051912206364843246409736216426214046893927]," +
                " [-128180915019741852230939121746453149192136881205294811341470641200471646007138201/217, 605706092" +
                "22559647870481875928214598477/11235098626702884014496194172720416144472006935]," +
                " [125905591/134986414920203678283696874370647669999843334627921819879642505841998444512097261705623" +
                ", 38885932433951997214299046691820204850705603/2270091556160585497150744207789926935731020378658123" +
                "557851677102522786914724054353561745955990478156435004918642340]," +
                " [-1313947671804572357439878089/4224, 309995181129017020395446087404108720093879/80584925]," +
                " [-353883577985348173837699629810090/103278735377, 10/16765568111]," +
                " [-69613881158462261539349355255679/617507627487, -30720262517752671/449703904822]," +
                " [-33873088625616782749920040910673005697076285332259/182607513273987527895875555329543446243402238" +
                "0728646963066004179668081230117, 233067017603786278/147505444980269244967207]," +
                " [2510111740134478095443484037613/46740293007322683431565217198097245, 4990160020534885057898123459" +
                "0/1081373]," +
                " [-5158113169760065405039/1352290807605056128, 606741144138380536571858000456022994436/118071]," +
                " [-8/149550458375738514680261979, 1958295030203427/56906390240]," +
                " [1709/424289276379738268497613661428514, 255758238983893665426384567093480949791313766845545905189" +
                "338865527214404161314331306816571297498773/3632638456768172]," +
                " [-30507538/589984077663450662457305, 252629269838496/3654927805]]");
        try {
            P.withIntervalMeanBitSize(11).intervals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withIntervalMeanBitSize(0).intervals();
            fail();
        } catch (IllegalArgumentException ignored) {}
        try {
            P.withIntervalMeanBitSize(-4).intervals();
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testIntervals() {
        aeq(take(20, P.intervals()),
                "[[-122225533/173, 2/77979], [-89/5, -4/11], [31/11493125, 973840621/21554063]," +
                " [-1108/65, -127404/71863696713244618325], [-1/17589524, 113476/22958079089115918357]," +
                " [-105/1963, -52/12688009], [-338/21, -28/35127415217835589], [-1/66433, 1/3778622118]," +
                " [-1/140, 33659589303407/21], [31/52, 624], [-11/204, 1647], [-1/52, 443]," +
                " [4189/170225917, 460417/3], [334831531/1151077550308, 45/589], [-2/891, -1/1023]," +
                " [61/29782784952, 19251/1658327], [-109810057/12132777, -16/19722731]," +
                " [-11/234155341, 96870442/439], [-5867/1925164216, 0], [-22474609/117497, 6618156975454609/5]]");
    }

    private static void aeq(Iterable<?> a, Object b) {
        assertEquals(IterableUtils.toString(a), b.toString());
    }
}