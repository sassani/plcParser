
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** a lexical analyzer system for simple arithmetic expressions */
public class LexicalAnalyzer {
    // Global declarations
    private TokenSpecifierRegex tokenSpecifier;
    private int charClass;
    private StringBuilder lexeme = new StringBuilder();
    private char nextChar = ' ';
    private char nextNextChar = '\0';
    private BufferedReader bufferedReader;
    private int nextToken;
    private int identClass;
    private int rowNum;
    private int colNum;

    public LexicalAnalyzer(String fileName) {
        try {
            File f = new File(fileName);
            FileReader fr = new FileReader(f);
            bufferedReader = new BufferedReader(fr);
            tokenSpecifier = new TokenSpecifierRegex();
        } catch (IOException e) {
            System.out.println("Invalid file path");
        }
    }

    public int NextToken() {
        lexeme.setLength(0);
        removeBlankOrNewLine();
        switch (charClass) {
            /* Parse keywords */
            case Constants.LETTER:
                addChar();
                getChar();
                while (charClass == Constants.LETTER || charClass == Constants.DIGIT) {
                    addChar();
                    getChar();
                }
                lookupResWord(lexeme.toString());
                break;
            /* Parse identifiers */
            case Constants.IDENT:
                identifierTokenizerPerl();
                break;
            /* Parse numbers */
            case Constants.LITERAL_NUM:
                literalNumTokenizer();
                break;
            /* Parentheses and operators */
            case Constants.UNKNOWN:
                lookup();
                getChar();
                break;
            /* EOF */
            case Constants.EOF:
                nextToken = Constants.EOF;
                addChar();
                break;
            default:
                sendInvalidCharacterError();
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
        addGetChar();
        if (isNoneDigit()) {
            addGetChar();
            while (isNoneDigit() || isDigit()) {
                if (lexeme.length() > maxSize + 1)
                    sendError("Too long name. Maximum length: " + maxSize);
                addGetChar();
            }
            nextToken = identClass;
        } else {
            sendError("needs to have an alphabet  or _ after identifier symbol");
        }
    }

    private void literalNumTokenizer() {
        addGetChar();
        while (isValidNumericChar() || nextChar == '.') {
            addGetChar();
        }

        nextToken = tokenSpecifier.getNumberClass(lexeme.toString());

    }

    private void addGetChar() {
        addChar();
        getChar();
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
            case '￿':
                addChar();
                nextToken = Constants.EOF;
                break;
            default:
                break;
        }
        return nextToken;
    }

    private void lookupResWord(String word) {
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
                sendError("Invalid reserved word (" + word + ")");
                break;
        }
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
        try {
            if (nextNextChar == '\0') {
                nextChar = (char) bufferedReader.read();
            } else {
                nextChar = nextNextChar;
                nextNextChar = '\0';
            }
            colNum++;
            if (nextChar == '￿')
                charClass = Constants.EOF;
            else if (isIdentFlag())
                charClass = Constants.IDENT;
            else if (isLiteralNumFlag())
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

    }

    private boolean isLiteralNumFlag() {
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

    private boolean isValidNumericChar() {
        return (isSign() || isFloatingSuffix() || isUnsignedSuffix() || isLongSuffix() || isExponent(true)
                || isExponent(false) || isHexadecimalPrefix() || isBinaryPrefix() || isHexDigit());
    }

    private boolean isSign() {
        return (nextChar == '+' || nextChar == '-');
    }

    private boolean isUnsignedSuffix() {
        return (nextChar == 'u' || nextChar == 'U');
    }

    private boolean isLongSuffix() {
        return (nextChar == 'l' || nextChar == 'L');
    }

    private boolean isExponent(boolean isHex) {
        if (isHex)
            return (nextChar == 'p' || nextChar == 'P');
        return (nextChar == 'e' || nextChar == 'E');
    }

    private boolean isBinaryPrefix() {
        return (nextChar == 'b' || nextChar == 'B');
    }

    private boolean isHexadecimalPrefix() {
        return (nextChar == 'x' || nextChar == 'X');
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

    private boolean isHexDigit() {
        return (isDigit() || (nextChar >= 'a' && nextChar <= 'f') || (nextChar >= 'A' && nextChar <= 'F'));
    }

    private boolean isDigit() {
        return (isNoneZeroDigit() || nextChar == '0');
    }

    private boolean isNoneZeroDigit() {
        return (isNoneZeroOctal() || nextChar == '8' || nextChar == '9');
    }

    private boolean isNoneZeroOctal() {
        return (nextChar >= '1' && nextChar <= '7');
    }

    private void sendError(String message) {
        throw new InvaliSyntaxSymbolException(message, rowNum + 1, colNum);
    }

    private void sendInvalidCharacterError() {
        throw new InvaliSyntaxSymbolException("Invalid character (" + nextChar + ")", rowNum + 1, colNum);
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
