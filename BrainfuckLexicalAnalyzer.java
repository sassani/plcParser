
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BrainfuckLexicalAnalyzer {

    private Set<Character> validChars = new HashSet<Character>();

    public BrainfuckLexicalAnalyzer() {
        char[] validCharsTemp = { '>', '<', '+', '-', '.', ',', '[', ']' };
        for (char c : validCharsTemp) {
            validChars.add(c);
        }
    }

    public boolean CheckLexical(String str) {
        int errorIndex = -1;
        for (Character c : str.toCharArray()) {
            if (!CheckCharacter(c)) {
                DisplayError(str, errorIndex);
                return false;
            }
            errorIndex++;
        }
        return CheckBraces(str);
    }

    private void DisplayError(String str, int errorIndex) {
        String errPosition = "";
        for (int i = 0; i <= errorIndex; i++) {
            errPosition += " ";
        }
        errPosition += "^";
        System.out.println("Syntax Error:");
        System.out.println(str);
        System.out.println(errPosition);
    }


    private boolean CheckBraces(String str) {
        int errorIndex = -1;
        int bracesCount = 0;
        for (Character c : str.toCharArray()) {
            if (c == '[') {
                bracesCount++;
            } else if (c == ']') {
                if (bracesCount == 0) {
                    DisplayError(str, errorIndex);
                    return false;
                }
                bracesCount--;
            }
            errorIndex++;
        }
        if (bracesCount != 0)
            DisplayError(str, errorIndex);
        return bracesCount == 0;
    }

    private boolean CheckCharacter(Character character) {
        return validChars.contains(character.charValue());
    }

    public static void main(String[] args) {
        System.out.println("Brainfuck Syntax Analizer");
        System.out.println("Author: Aradavan Sassani");
        if (args[0] == null) {
            throw new java.lang.IllegalArgumentException("File name is null");
        }
        File inFile = new File(args[0]);
        try {
            Scanner scanner = new Scanner(inFile);
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                System.out.println("\r\nString: " + str);
                // test function
                BrainfuckLexicalAnalyzer bfla = new BrainfuckLexicalAnalyzer();
                System.out.println("Is a valid syntax: " + bfla.CheckLexical(str));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

}
