package ro.sandorr;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Created by sando on 12/28/2016.
 */
class Scanner {
    public static final String SPACE = " ";
    public static final int CONSTANT_CODE = 1;
    public static final int IDENTIFIER_CODE = 0;
    private List<String> errors;
    private Map<String, Integer> programInternalForm;
    private List<Pair<Integer, Integer>> program;
    private Map<Integer, String> symbolTable;

    public List<String> getErrors() {
        return errors;
    }

    public List<Pair<Integer, Integer>> getProgram() {
        return program;
    }

    public Map<Integer, String> getSymbolTable() {
        return symbolTable;
    }

    Scanner() {
        this.errors = new ArrayList<>();
        this.programInternalForm = new HashMap<>();
        this.program = new ArrayList<>();
        this.symbolTable = new HashMap<>();
    }

    void readProgramInternalForm(final String pifPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pifPath))) {
            String line = reader.readLine();
            while (line != null) {
                final String[] parts = line.split(SPACE);

                if (parts.length >= 2) {
                    final String token = parts[0];
                    final Integer code = Integer.parseInt(parts[1]);
                    programInternalForm.put(token, code);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void scan(final String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            int currentLine = 1;
            while (line != null) {
                tokenize(line, currentLine);
                line = reader.readLine();
                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tokenize(String line, int currentLine) {
        String[] tokens = line.split(SPACE);
        for (String token : tokens) {
            if (Objects.equals(token.trim(), "")) continue;

            if (programInternalForm.containsKey(token)) {
                program.add(new Pair<>(programInternalForm.get(token), -1));
            } else if (token.matches("[+-]?[1-9][0-9]*")) {
                // match numbers
                addConstant(token);
            } else if (token.matches("'.?'")) {
                // match chars
                addConstant(token);
            } else if (token.matches("\".*\"")) {
                addConstant(token);
            } else if (token.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
                addIdentifier(token);
            } else {
                addError(currentLine, token);
            }
        }
    }

    private void addError(int currentLine, final String token) {
        errors.add("[ Error ] : line " + currentLine + " invalid token < " + token + " found!");
    }

    private void addIdentifier(String token) {
        final Integer index = symbolTable.size();
        symbolTable.put(index, token);
        program.add(new Pair<>(IDENTIFIER_CODE, index));
    }

    private void addConstant(String token) {
        final Integer index = symbolTable.size();
        symbolTable.put(index, token);
        program.add(new Pair<>(CONSTANT_CODE, index));
    }
}
