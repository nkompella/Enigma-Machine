package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Neha Kompella
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters not
     * included in any cycle map to themselves. Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        dictCycles = new HashMap<>();
        if (_cycles != null) {
            cyclesArray = _cycles.toCharArray();

        }
        for (int i = 0; i < _alphabet.size(); i++) {
            char same = _alphabet.toChar(i);
            dictCycles.put(same, same);
        }
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        int k = wrap(p);
        char saved = 0;
        if (cyclesArray == null) {
            return k;
        } else {
            for (int i = 0; i < cyclesArray.length - 1; i++) {
                if (cyclesArray[i] == '(') {
                    saved = cyclesArray[i + 1];
                } else if (cyclesArray[i + 1] == ')') {
                    dictCycles.put(cyclesArray[i], saved);
                } else if (cyclesArray[i] != ')') {
                    dictCycles.put(cyclesArray[i], cyclesArray[i + 1]);
                }
            }

            char letter = _alphabet.toChar(k);
            char permuted = dictCycles.get(letter);
            int result = 0;
            for (int i = 0; i < _alphabet.size(); i++) {
                if (_alphabet.toChar(i) == permuted) {
                    result = i;
                }
            }
            return result;
        }
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        int k = wrap(c);
        char saved = 0;
        if (_cycles == null) {
            return k;
        } else {
            for (int i = cyclesArray.length - 1; i > 0; i--) {
                if (cyclesArray[i] == ')') {
                    saved = cyclesArray[i - 1];
                } else if (cyclesArray[i - 1] == '(') {
                    dictCycles.put(cyclesArray[i], saved);
                } else if (cyclesArray[i] != '(') {
                    dictCycles.put(cyclesArray[i], cyclesArray[i - 1]);
                }
            }

            char letter = _alphabet.toChar(k);
            char permuted = dictCycles.get(letter);
            int result = 0;
            for (int i = 0; i < _alphabet.size(); i++) {
                if (_alphabet.toChar(i) == permuted) {
                    result = i;
                }
            }
            return result;
        }
    }


    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {

        char saved = 0;
        for (int i = 0; i < cyclesArray.length; i++) {
            if (cyclesArray[i] == '(') {
                saved = cyclesArray[i + 1];
            } else if (cyclesArray[i + 1] == ')') {
                dictCycles.put(cyclesArray[i], saved);
            } else if (cyclesArray[i] != ')') {
                dictCycles.put(cyclesArray[i], cyclesArray[i + 1]);
            }
        }
        return dictCycles.get(p);
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    int invert(char c) {
        char saved = 0;
        for (int i = cyclesArray.length - 1; i > -1; i--) {
            if (cyclesArray[i] == ')') {
                saved = cyclesArray[i - 1];
            } else if (cyclesArray[i - 1] == '(') {
                dictCycles.put(cyclesArray[i], saved);
            } else if (cyclesArray[i] != '(') {
                dictCycles.put(cyclesArray[i], cyclesArray[i - 1]);
            }
        }
        return (int) dictCycles.get(c);
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        for (char key : dictCycles.keySet()) {
            if (dictCycles.get(key).equals(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;
    /**
     * Cycles of permutations.
     */
    private String _cycles;
    /**
     * Array of characters comprised of letters in _cycles.
     */
    private char[] cyclesArray;
    /**
     * Dictionary that maps each character to its permuted
     * character.
     */
    private HashMap<Character, Character> dictCycles;

}

