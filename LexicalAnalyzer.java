
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** a lexical analyzer system for simple arithmetic expressions */
public class LexicalAnalyzer {
    // #region Global declarations
    /* Variables */
    private int charClass;
    private StringBuilder lexeme = new StringBuilder();
    private char nextChar = ' ';
    private char nextChar2 = '\0';
    private BufferedReader bufferedReader;
    private int nextToken;
    private int identClass;
    // // #endregion

    public LexicalAnalyzer(String fileName) {
        try {
            File f = new File(fileName);
            FileReader fr = new FileReader(f);
            bufferedReader = new BufferedReader(fr);
        } catch (IOException e) {
            System.out.println("Invalid file path");
        }
    }

    public int NextToken() {
        lexeme.setLength(0);
        removeBlankOrNewLine();
        switch (charClass) {
            /* Parse identifiers */
            case Constants.IDENT:
                identifierTokenizerPerl();
                break;
            case Constants.DIGIT:
                addChar();
                getChar();
                while (charClass == Constants.DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = Constants.INT_LIT;
                break;
            /* Parentheses and operators */
            case Constants.UNKNOWN:
                lookup(nextChar);
                getChar();
                break;
            /* EOF */
            case Constants.EOF:
                nextToken = Constants.EOF;
                break;
            default:
                throw new InvaliSyntaxSymbolException("Invalid character");
        } /* End of switch */
        System.out.printf("Next token is: %d, Next lexeme is %s\n", nextToken, lexeme);
        return nextToken;
    }

    /**
     * A method to create an identifier token in perl style
     * https://perldoc.perl.org/perlintro
     */
    private void identifierTokenizerPerl() {
        int maxSize = 251;
        addChar();
        getChar();
        if (isValidIdentifierCharInPerl(nextChar)) {
            addChar();
            getChar();
            while (charClass == Constants.LETTER || charClass == Constants.DIGIT
                    || isValidIdentifierCharInPerl(nextChar)) {
                if (lexeme.length() > maxSize + 1)
                    throw new InvaliSyntaxSymbolException("Too long name. Maximum length: " + maxSize);
                addChar();
                getChar();
            }
            nextToken = identClass;
            lookupResWord(lexeme.toString());
        } else {
            throw new InvaliSyntaxSymbolException("needs to have an alphabet  or _ after identifier symbol");
        }
    }

    private int lookup(char ch) {
        switch (ch) {
            case '=':
                addChar();
                nextToken = Constants.ASSIGN_OP;
                break;
            case '(':
                addChar();
                nextToken = Constants.LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = Constants.RIGHT_PAREN;
                break;
            case '+':
                readExtraChar();
                if (nextChar2 == '+') {
                    addExtraChar();
                    nextToken = Constants.INCRE_OP;
                } else {
                    addChar();
                    nextToken = Constants.ADD_OP;
                }
                break;
            case '-':
                readExtraChar();
                if (nextChar2 == '-') {
                    addExtraChar();
                    nextToken = Constants.DECRE_OP;
                } else {
                    addChar();
                    nextToken = Constants.SUB_OP;
                }
                break;
            case '*':
                addChar();
                nextToken = Constants.MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = Constants.DIV_OP;
                break;
            case '{':
                addChar();
                nextToken = Constants.LEFT_CURLE;
                break;
            case '}':
                addChar();
                nextToken = Constants.RIGHT_CURLE;
                break;
            case ';':
                addChar();
                nextToken = Constants.SEMICOLON;
                break;
            case '%':
                addChar();
                nextToken = Constants.MODUL_OP;
                break;
            case '!':
                addChar();
                nextToken = Constants.LGNOT_OP;
                break;
            case '&':
                readExtraChar();
                if (nextChar2 == '&') {
                    addExtraChar();
                    nextToken = Constants.LGAND_OP;
                } else {
                    throw new InvaliSyntaxSymbolException("missing '&'");
                }
                break;
            case '|':
                readExtraChar();
                if (nextChar2 == '|') {
                    addExtraChar();
                    nextToken = Constants.LGOR_OP;
                } else {
                    throw new InvaliSyntaxSymbolException("missing '|'");
                }
                break;
            default:
                addChar();
                nextToken = Constants.EOF;
                break;
        }
        return nextToken;
    }

    private int lookupResWord(String word) {
        switch (word) {
            case "while":
                nextToken = Constants.WHILE_CODE;
                break;
            case "if":
                nextToken = Constants.IF_CODE;
                break;
            case "else":
                nextToken = Constants.ELSE_CODE;
                break;
            case "true":
                nextToken = Constants.TRUE_CODE;
                break;
            case "false":
                nextToken = Constants.FALSE_CODE;
                break;
            default:
                break;
        }
        return nextToken;
    }

    private void addChar() {

        lexeme.append(nextChar);
    }

    private void addExtraChar() {
        addChar();
        lexeme.append(nextChar2);
        nextChar2 = '\0';
    }

    private void readExtraChar() {
        try {
            nextChar2 = (char) bufferedReader.read();
        } catch (Exception e) {

        }
    }

    private void getChar() {
        if (nextChar2 == '\0') {
            try {
                nextChar = (char) bufferedReader.read();
                if (nextChar == -1)
                    charClass = Constants.EOF;
                else if (nextChar == '$') {
                    charClass = Constants.IDENT;
                    identClass = Constants.IDENT_SCL;
                } else if (nextChar == '%') {
                    charClass = Constants.IDENT;
                    identClass = Constants.IDENT_MAP;
                } else if (nextChar == '@') {
                    charClass = Constants.IDENT;
                    identClass = Constants.IDENT_ARR;
                } else {
                    if (isAlpha(nextChar))
                        charClass = Constants.LETTER;
                    else if (isDigit(nextChar))
                        charClass = Constants.DIGIT;
                    else
                        charClass = Constants.UNKNOWN;
                }

            } catch (IOException e) {
                System.out.println("File error");
            }
        } else {

            nextChar = nextChar2;
            nextChar2 = '\0';
        }

    }

    private void removeBlankOrNewLine() {
        while (nextChar == ' ' || nextChar == '\r' || nextChar == '\n') {
            getChar();
        }
    }

    private boolean isAlpha(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    private boolean isValidIdentifierCharInPerl(char c) {
        return (isAlpha(c) || c == '_');
    }

    private boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    public static void main(String[] args) {
        System.out.println("Lexical Analyzer");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("test2.in");
        int t;
        do {
            t = lexicalAnalyzer.NextToken();
        } while (t != Constants.EOF);

    }
}
