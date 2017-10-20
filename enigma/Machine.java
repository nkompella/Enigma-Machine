package enigma;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;


import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Neha Kompella
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        usedRotors = new Rotor[numRotors];
        rotorMap = new HashMap<>();
        for (Rotor element : allRotors) {
            rotorMap.put(element.name().toUpperCase(), element);
        }
        if (_pawls >= _numRotors) {
            throw new EnigmaException("More pawls than rotors");
        }
        if (_pawls <= 0) {
            throw new EnigmaException("0 or less pawls");
        }

    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        int i = 0;
        for (String name : rotors) {
            usedRotors[i] = rotorMap.get(name.toUpperCase());
            i++;
        }
        if (usedRotors.length != _numRotors) {
            throw new EnigmaException("Incorrect number of rotors");
        }
        if (!(usedRotors[0] instanceof Reflector)) {
            throw new EnigmaException("Invalid sequence of rotors");
        }
        for (int j = 1; j < _numRotors - _pawls; j++) {
            if (!(usedRotors[j] instanceof FixedRotor)) {
                throw new EnigmaException("Invalid sequence of rotors");
            }
        }
        for (int k = _numRotors - _pawls; k < usedRotors.length; k++) {
            if (!(usedRotors[k] instanceof MovingRotor)) {
                throw new EnigmaException("Invalid sequence of Rotors");
            }
        }

    }

    /**
     * Set my rotors according to SETTING, which must be a string of four
     * upper-case letters. The first letter refers to the leftmost
     * rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.toCharArray()[i])) {
                throw new EnigmaException("setting not in alphabet");
            }
        }
        for (int i = 1; i < usedRotors.length; i++) {
            int h = _alphabet.toInt(setting.charAt(i - 1));
            usedRotors[i].set(h);

        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        List<Integer> atTheNotch = new LinkedList<>();

        int fix = numRotors() - numPawls();
        int next = fix + 1;
        int len = usedRotors.length - 1;
        for (int i = fix; i < usedRotors.length; i++) {
            if (i != (fix) && usedRotors[i].atNotch()) {
                atTheNotch.add(i);
            } else if (i == (fix) && fix < len && usedRotors[next].atNotch()) {
                atTheNotch.add(i);
            }
        }
        List<Rotor> pushed = new ArrayList<>();
        for (int j : atTheNotch) {
            if (!pushed.contains(usedRotors[j])) {
                usedRotors[j].advance();
                pushed.add(usedRotors[j]);
            }
            if (!pushed.contains(usedRotors[j - 1])) {
                usedRotors[j - 1].advance();
                pushed.add(usedRotors[j - 1]);
            }
        }

        if (!pushed.contains(usedRotors[usedRotors.length - 1])) {
            usedRotors[usedRotors.length - 1].advance();
        }

        c = _plugboard.permute(c);

        for (int i = usedRotors.length - 1; i >= 0; i--) {
            c = usedRotors[i].convertForward(c);
        }

        for (int i = 1; i < usedRotors.length; i++) {
            c = usedRotors[i].convertBackward(c);
        }

        c = _plugboard.permute(c);

        return c;
    }


    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */


    String convert(String msg) {
        msg = msg.replace("(", "");
        msg = msg.replace(")", "");
        msg = msg.toUpperCase();
        char[] msgArray = msg.toCharArray();
        int[] intArray = new int[msgArray.length];
        int[] convertedIntArray = new int[msgArray.length];
        char[] convertedMsgArray = new char[msgArray.length];
        for (int i = 0; i < msgArray.length; i++) {
            intArray[i] = _alphabet.toInt(msgArray[i]);
        }

        for (int i = 0; i < msgArray.length; i++) {
            convertedIntArray[i] = convert(intArray[i]);
        }

        for (int i = 0; i < msgArray.length; i++) {
            convertedMsgArray[i] = _alphabet.toChar(convertedIntArray[i]);
        }

        String morocco = "";
        for (int i = 0; i < convertedMsgArray.length; i++) {
            morocco += convertedMsgArray[i];
        }
        return morocco;

    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;
    /**
     * Number of rotors.
     */
    private int _numRotors;
    /**
     * Number of pawls.
     */
    private int _pawls;
    /**
     * Plugboard permutation.
     */
    private Permutation _plugboard;
    /**
     * Collection of all available rotor permutations.
     */
    private Collection<Rotor> _allRotors;
    /**
     * List of rotors used for this particular machine.
     */
    private Rotor[] usedRotors;
    /**
     * Dictionary where name of Rotor is associated to
     * the information of the rotor.
     */
    private HashMap<String, Rotor> rotorMap;
}
