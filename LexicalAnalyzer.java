
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
    private char nextNextChar = '\0';
    private BufferedReader bufferedReader;
    private int nextToken;
    private int identClass;
    private int literalClass;
    private int rowNum;
    private int colNum;
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
            case Constants.LITERAL_NUM:
                literalNumTokenizer();
                break;
            case Constants.DIGIT:
                addGetChar();
                while (charClass == Constants.DIGIT) {
                    addGetChar();
                }
                nextToken = Constants.LITERAL_INT_DEC;
                break;
            /* Parentheses and operators */
            case Constants.UNKNOWN:
                lookup();
                getChar();
                break;
            /* EOF */
            case Constants.EOF:
                nextToken = Constants.EOF;
                break;
            default:
                sendError("Invalid character (" + nextChar + ")");
        } /* End of switch */
        System.out.printf("Next token is: %d, Next lexeme is %s\n", nextToken, lexeme);
        literalClass = Constants.UNKNOWN;// reset literal class for next literal generation
        return nextToken;
    }

    /**
     * A method to create an identifier token in perl style
     * https://perldoc.perl.org/perlintro
     */
    private void identifierTokenizerPerl() {
        int maxSize = 251;
        addGetChar();
        if (isNoneDigit()) {
            addGetChar();
            while (isNoneDigit() || isDigit()) {
                if (lexeme.length() > maxSize + 1)
                    sendError("Too long name. Maximum length: " + maxSize);
                addGetChar();
            }
            nextToken = identClass;
            lookupResWord(lexeme.toString());
        } else {
            sendError("needs to have an alphabet  or _ after identifier symbol");
        }
    }

    private void literalNumTokenizer() {
        if (nextChar == '.') {// this must be floating point literal
            addGetChar();
            while (isDigit()) {
                addGetChar();
            }
            if (isExponent()) {
                addGetChar();
                if (isDigit()) {
                    getDigitSequence();
                    if (isFloatingSuffix() || isNewlineOrSpace()) {
                        addGetChar();
                    } else {
                        sendError("only f Or F or l or L is valid after exponent number");
                    }
                } else if (isSign()) {
                    addGetChar();
                    if (isDigit()) {
                        getDigitSequence();
                    } else {
                        sendError("needs to have number after sign");
                    }
                    if (isFloatingSuffix())
                        addGetChar();
                } else {
                    sendError("needs to have number or sign after exponent");
                }
            }
            nextToken = Constants.LITERAL_FLP;

        } else if (isNoneZeroDigit()) {
            getDigitSequence();
            nextToken = Constants.LITERAL_INT_DEC;
        }

    }

    private void addGetChar() {
        addChar();
        getChar();
    }

    private void getDigitSequence() {
        addGetChar();
        while (isDigit()) {
            addGetChar();
        }
    }

    /**
     * A method to create a Floating-Point token in C style
     * https://docs.microsoft.com/en-us/cpp/c-language/lexical-grammar?view=msvc-160
     */
    private void integerTokenizer() {

    }

    /**
     * A method to create a Floating-Point token in C style
     * https://docs.microsoft.com/en-us/cpp/c-language/lexical-grammar?view=msvc-160
     */
    private void floatingPointTokenizer() {

    }

    private int lookup() {
        switch (nextChar) {
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
                if (nextNextChar == '+') {
                    addExtraChar();
                    nextToken = Constants.INCRE_OP;
                } else {
                    addChar();
                    nextToken = Constants.ADD_OP;
                }
                break;
            case '-':
                readExtraChar();
                if (nextNextChar == '-') {
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
                if (nextNextChar == '&') {
                    addExtraChar();
                    nextToken = Constants.LGAND_OP;
                } else {
                    sendError("missing '&'");
                }
                break;
            case '|':
                readExtraChar();
                if (nextNextChar == '|') {
                    addExtraChar();
                    nextToken = Constants.LGOR_OP;
                } else {
                    sendError("missing '|'");
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
        lexeme.append(nextNextChar);
        nextNextChar = '\0';
    }

    private void readExtraChar() {
        try {
            nextNextChar = (char) bufferedReader.read();
        } catch (Exception e) {

        }
    }

    private void getChar() {
        if (nextNextChar == '\0') {
            try {
                nextChar = (char) bufferedReader.read();
                colNum++;
                if (nextChar == -1)
                    charClass = Constants.EOF;
                else if (isIdentFlag())
                    charClass = Constants.IDENT;
                else if (isLiteralNumFlag(nextChar))
                    charClass = Constants.LITERAL_NUM;
                else {
                    if (isNoneDigit())
                        charClass = Constants.LETTER;
                    else if (isDigit())
                        charClass = Constants.DIGIT;
                    else
                        charClass = Constants.UNKNOWN;
                }

            } catch (IOException e) {
                System.out.println("File error");
            }
        } else {
            nextChar = nextNextChar;
            nextNextChar = '\0';
        }

    }

    private boolean isLiteralNumFlag(char nextChar) {
        return (isDigit() || nextChar == '.');
    }

    private boolean isIdentFlag() {
        if (nextChar == '$')
            identClass = Constants.IDENT_SCL;
        else if (nextChar == '%')
            identClass = Constants.IDENT_MAP;
        else if (nextChar == '@')
            identClass = Constants.IDENT_ARR;
        else
            return false;
        return true;
    }

    private boolean isLiteralStringFlag() {
        return false;
    }

    private void removeBlankOrNewLine() {
        while (isNewlineOrSpace()) {
            if (isNewline()) {
                colNum = 0;
                rowNum++;
            }
            getChar();
        }
    }

    private boolean isNewlineOrSpace() {
        return (nextChar == ' ' || isNewline());
    }

    private boolean isNewline() {
        return (nextChar == '\r' || nextChar == '\n');
    }

    private boolean isSign() {
        return (nextChar == '+' || nextChar == '-');
    }

    private boolean isExponent() {
        return (nextChar == 'e' || nextChar == 'E');
    }

    private boolean isFloatingSuffix() {
        return (nextChar == 'f' || nextChar == 'F' || nextChar == 'l' || nextChar == 'L');
    }

    private boolean isAlpha() {
        return ((nextChar >= 'a' && nextChar <= 'z') || (nextChar >= 'A' && nextChar <= 'Z'));
    }

    private boolean isNoneDigit() {
        return (isAlpha() || nextChar == '_');
    }

    private boolean isDigit() {
        return (isNoneZeroDigit() || nextChar == '0');
    }

    private boolean isNoneZeroDigit() {
        return (nextChar >= '1' && nextChar <= '9');
    }

    private void sendError(String message) {
        throw new InvaliSyntaxSymbolException(message, rowNum+1, colNum);
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
