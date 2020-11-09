
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Lexical {

    public static List<String> CheckLexical(String str) {
        List<String> lexemes = new LinkedList<String>();
        List<Character> lexem = new LinkedList<Character>();
        for (char c : str.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                lexem.add(c);
            } else {
                if (lexem.size() > 0) {
                    lexemes.add(lexem.stream().map(e -> e.toString()).collect(Collectors.joining()));
                    lexem.clear();
                }
                if (c == ' ')
                    continue;
                lexemes.add(String.valueOf(c));
            }
        }
        if (lexem.size() > 0) {
            lexemes.add(lexem.stream().map(e -> e.toString()).collect(Collectors.joining()));
        }
        return lexemes;
    }

    public static String FuckBrainLexical(String str){
        // for (char c : str.toCharArray()) {
            
        // }
        return "";
    }

    public static void main(String[] args) {
        System.out.println("Lexical tester in Java");
        if (args[0] == null) {
            throw new java.lang.IllegalArgumentException("File name is null");
        }
        File inFile = new File(args[0]);
        try {
            Scanner scanner = new Scanner(inFile);
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                System.out.println("String: " + str);
                // test function
                System.out.println("Lexemes: " + Lexical.CheckLexical(str));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
