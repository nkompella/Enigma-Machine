package enigma;

import static enigma.EnigmaException.*;

/* Extra Credit Only */

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Neha Kompella
 */
class Alphabet {

    /** @param _chars represents the string that represents the alphabet
     * of the particular input file. */
    private String _chars;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        char[] alphabetArray = _chars.toCharArray();
        for (int i = 0; i < alphabetArray.length; i++) {
            if (alphabetArray[i] == c) {
                return true;

            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        char[] alphabetArray = _chars.toCharArray();
        return alphabetArray[index];
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        char[] alphabetArray = _chars.toCharArray();
        int saved = 0;
        for (int i = 0; i < alphabetArray.length; i++) {
            if (alphabetArray[i] == c) {
                saved = i;
            }
        }
        return saved;
    }

}
