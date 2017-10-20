package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Neha Kompella
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        set(0);
    }

    /**
     * Make all the changes necessary when the rotor moves forward by one.
     */


    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            if (_notches.charAt(i) == alphabet().toChar(setting())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Notches corresponding to given rotor.
     */
    private String _notches;
}
