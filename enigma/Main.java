package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Collection;

import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 *
 * @author Neha Kompella
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        Machine m = readConfig();
        while (_input.hasNextLine()) {
            String a = _input.nextLine();
            if (!a.isEmpty()) {
                setUp(m, a);
                while (_input.hasNextLine() && !_input.hasNext("\\*")) {
                    String line = _input.nextLine();
                    Scanner linesc = new Scanner(line);
                    while (linesc.hasNext()) {
                        String inputter = linesc.next();
                        String outputter = m.convert(inputter);
                        message = message + outputter;
                    }
                    message += "\n";
                }
            } else {
                message += "\n";
            }
        }
        if (_input.hasNext()) {
            message += "\n";
        }
        printMessageLine(message);
    }


    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            if (_alphabet.contains('*')) {
                throw new EnigmaException("invalid characters in alphabet");
            } else if (_alphabet.contains('(') || _alphabet.contains(')')) {
                throw new EnigmaException("invalid characters in alphabet");
            }

            _numRotors = _config.nextInt();
            _numPawls = _config.nextInt();
            _allRotors = new ArrayList<>();

            while (_config.hasNext()) {
                _allRotors.add(readRotor());

            }

            return new Machine(_alphabet, _numRotors, _numPawls, _allRotors);

        } catch (NoSuchElementException excp) {
            excp.printStackTrace();
            throw error("configuration file truncated");
        }

    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            if (name.contains("(") || name.contains(")")) {
                throw new EnigmaException("invalid rotor name");
            }
            String saved = _config.next();
            String notches = "";
            String cycles = "";
            if (saved.charAt(0) == 'M') {
                for (int i = 1; i < saved.length(); i++) {
                    if (!_alphabet.contains(saved.charAt(i))) {
                        throw new EnigmaException("notch not in alphabet");
                    }
                    notches += saved.charAt(i);
                }
                if (notches.length() == 0) {
                    throw new EnigmaException("no notches for moving rotor");
                }
                while (_config.hasNext("\\(.*")) {
                    cycles = cycles + _config.next();
                }
                Permutation permutation = new Permutation(cycles, _alphabet);
                rotor = new MovingRotor(name, permutation, notches);
            } else if (saved.charAt(0) == 'N') {
                while (_config.hasNext("\\(.*")) {
                    cycles = cycles + _config.next();
                }
                char[] cyclesArray = cycles.toCharArray();
                int len = cyclesArray.length;
                if (len > 0 && cyclesArray[len - 1] != ')') {
                    throw new EnigmaException("wrong format for cycles");
                }
                Permutation permutation = new Permutation(cycles, _alphabet);
                rotor = new FixedRotor(name, permutation);
            } else if (saved.charAt(0) == 'R') {
                while (_config.hasNext("\\(.*")) {
                    cycles = cycles + _config.next();
                }
                Permutation permutation = new Permutation(cycles, _alphabet);
                rotor = new Reflector(name, permutation);
            } else {
                throw new EnigmaException("Rotor type is not valid");
            }
            char[] cyclesArray = cycles.toCharArray();
            int len = cyclesArray.length;
            if (len > 0 && cyclesArray[len - 1] != ')') {
                throw new EnigmaException("wrong format for cycles");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return rotor;
    }


    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        String[] arr = settings.split(" ");
        if (!arr[0].equals("*")) {
            throw new EnigmaException("No '*' as first character");
        }
        String[] arrNoStar = new String[arr.length - 1];

        for (int i = 0; i < arr.length - 1; i++) {
            arrNoStar[i] = arr[i + 1];
        }

        String[] rotorNames = new String[_numRotors];

        for (int i = 0; i < _numRotors; i++) {
            rotorNames[i] = arrNoStar[i];
        }

        M.insertRotors(rotorNames);

        String setting = arrNoStar[_numRotors];

        M.setRotors(setting);

        String plugboard = "";

        for (int i = _numRotors + 1; i < arrNoStar.length; i++) {
            plugboard = plugboard + " " + arrNoStar[i];
        }
        Permutation plugPerm = new Permutation(plugboard, _alphabet);

        M.setPlugboard(plugPerm);

    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        int i = 0;
        int antman = 0;
        String massage = "";
        for (char c : msg.toCharArray()) {
            if (i == 5) {
                massage += " ";
                i = 0;
            }
            if (c != '\n') {
                massage += c;
                i++;
            } else {
                _output.print(massage);
                _output.println();
                massage = "";
                i = 0;
                antman += 1;
            }
        }
    }


    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /**
     * Stores the final message to be printed.
     */
    private String message = "";

    /**
     * Number of rotors.
     */
    private int _numRotors;

    /**
     * Number of pawls.
     */
    private int _numPawls;

    /**
     * Collection of all rotors possible from config file.
     */
    private Collection<Rotor> _allRotors;

    /**
     * The individual rotor to be returned by readRotor.
     */
    private Rotor rotor;

}
