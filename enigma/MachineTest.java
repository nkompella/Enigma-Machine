package enigma;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
/**
 * Created by nayco on 10/10/16.
 */
public class MachineTest {

    public MachineTest() {
        allRotors.add(uno);
        allRotors.add(dos);
        allRotors.add(tres);
        allRotors.add(cuatro);
        allRotors.add(cinco);
        allRotors.add(siete);
        allRotors.add(seis);
        allRotors.add(ocho);
        allRotors.add(beta);
        allRotors.add(gamma);
        allRotors.add(b);
        allRotors.add(c);
        m = new Machine(alphaTest, numRotors, pawls, allRotors);
    }



    private Alphabet alphaTest = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    private Collection<Rotor> allRotors = new ArrayList<>();
    private int numRotors = 5;
    private int pawls = 3;

    private String unoString = "(AELTPHQXRU)(BKNW)(CMOY)(DFG)(IV)(JZ)(S)";
    private Permutation unoPerm = new Permutation(unoString, alphaTest);
    private Rotor uno = new MovingRotor("I", unoPerm, "Q");

    private String dosString = "(FIXVYOMW)(CDKLHUP)(ESZ)(BJ)(GR)(NT)(A)(Q)";
    private Permutation dosPerm = new Permutation(dosString, alphaTest);
    private Rotor dos = new MovingRotor("II", dosPerm, "E");

    private String tresString = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";
    private Permutation tresPerm = new Permutation(tresString, alphaTest);
    private Rotor tres = new MovingRotor("III", tresPerm, "V");

    private String cuatroString = "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)";
    private Permutation cuatroPerm = new Permutation(cuatroString, alphaTest);
    private Rotor cuatro = new MovingRotor("IV", cuatroPerm, "J");

    private String cincoString = "(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)";
    private Permutation cincoPerm = new Permutation(cincoString, alphaTest);
    private Rotor cinco = new MovingRotor("V", cincoPerm, "Z");

    private String seisString = "(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)";
    private Permutation seisPerm = new Permutation(seisString, alphaTest);
    private Rotor seis = new MovingRotor("VI", seisPerm, "ZM");

    private String sieteString = "(ANOUPFRIMBZTLWKSVEGCJYDHXQ)";
    private Permutation sietePerm = new Permutation(sieteString, alphaTest);
    private Rotor siete = new MovingRotor("VII", sietePerm, "ZM");

    private String ochoString = "(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)";
    private Permutation ochoPerm = new Permutation(ochoString, alphaTest);
    private Rotor ocho = new MovingRotor("VIII", ochoPerm, "ZM");

    private String betaString = "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
    private Permutation betaPerm = new Permutation(betaString, alphaTest);
    private Rotor beta = new FixedRotor("Beta", betaPerm);

    private String gammaStr = "(AFNIRLBSQWVXGUZDKMTPCOYJHE)";
    private Permutation gammaPerm = new Permutation(gammaStr, alphaTest);
    private Rotor gamma = new FixedRotor("Gamma", gammaPerm);

    private String bSt = "(AE)(BN)(CK)(DQ)(FU)(GY)(HW)(IJ)(LO)(MP)(RX)(SZ)(TV)";
    private Permutation bPerm = new Permutation(bSt, alphaTest);
    private Rotor b = new Reflector("B", bPerm);

    private String cSt = "(AR)(BD)(CO)(EJ)(FN)(GT)(HK)(IV)(LM)(PW)(QZ)(SX)(UY)";
    private Permutation cPerm = new Permutation(cSt, alphaTest);
    private Rotor c = new Reflector("C", cPerm);

    private Permutation plugboard = new Permutation(("(AQ) (EP)"), alphaTest);
    private Machine m;



    @Test
    public void checkNumbers() {
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());

    }


    @Test
    public void checkIntConvert() {
        String[] names = {"B", "BETA", "I", "II", "III"};
        m.insertRotors(names);
        String setting = "AAAA";
        m.setRotors(setting);
        m.setPlugboard(plugboard);
        assertEquals(8 , m.convert(7));
    }


    @Test
    public void checkStringConvert() {
        String[] names = {"B", "BETA", "I", "II", "III"};
        m.insertRotors(names);
        String setting = "AAAA";
        m.setRotors(setting);
        m.setPlugboard(plugboard);
        assertEquals("I", m.convert("H"));
    }

}


