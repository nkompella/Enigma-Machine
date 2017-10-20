package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a rotor that has no ratchet and does not advance.
 *
 * @author Neha Kompella
 */
class FixedRotor extends Rotor {

    /**
     * A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM.
     */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }


    @Override
    boolean rotates() {
        return false;
    }


    @Override
    void advance() {

    }

    @Override
    boolean atNotch() {
        return false;
    }

}
