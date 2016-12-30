package ro.sandorr;

import javafx.util.Pair;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        final String fileName = "input.txt";
        final String pifPath = "pif.txt";
        final Scanner scanner = new Scanner();
        scanner.readProgramInternalForm(pifPath);
        scanner.scan(fileName);

        for (String error : scanner.getErrors()) {
            System.err.println(error);
        }

        System.out.println("\nProgram :");
        for (Pair entry : scanner.getProgram()) {
            System.out.printf("%4s --> %4s \n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nSymbol Table :");
        for (Map.Entry<Integer, String> entry : scanner.getSymbolTable().entrySet()) {
            System.out.printf("%4s --> %4s \n", entry.getKey(), entry.getValue());
        }
    }
}
