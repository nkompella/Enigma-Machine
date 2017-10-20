package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a reflector in the enigma.
 *
 * @author Neha Kompella
 */
class Reflector extends FixedRotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM.
     */
    Reflector(String name, Permutation perm) {
        super(name, perm);

    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    int convertBackward(int e) {
        throw new UnsupportedOperationException();

    }

}
