import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenSpecifierRegex {
    private static String integerSuffix = "([uU]{1}[lL]{0,2}|[lL]{1,2}[uU]?)";

    private Map<Integer, String> tokenPatterns = new HashMap<Integer, String>();

    public TokenSpecifierRegex() {
        tokenPatterns.put(Constants.LITERAL_INT_HEX, "^(0[xX]){1}[a-fA-F\\d]+" + integerSuffix + "?$");
        tokenPatterns.put(Constants.LITERAL_INT_DEC, "^[1-9][0-9]*" + integerSuffix + "?$");
        tokenPatterns.put(Constants.LITERAL_INT_BIN, "^(0[bB]){1}[01]*" + integerSuffix + "?$");
        tokenPatterns.put(Constants.LITERAL_INT_OCT, "^0[0-7]*" + integerSuffix + "?$");
        tokenPatterns.put(Constants.LITERAL_FLP_HEX,
                "^(0[xX]){1}(([A-Fa-f\\d]+\\.{1}|[A-Fa-f\\d]*\\.{1}[A-Fa-f\\d]+){1}([pP]{1}[-+]?\\d)?|[A-Fa-f\\d]+([pP]{1}[-+]?\\d){1})[fFlL]?$");

    }

    public int getNumberClass(String literal) {
        for (int tokenKey : tokenPatterns.keySet()) {
            Pattern patern = Pattern.compile(tokenPatterns.get(tokenKey));
            Matcher macher = patern.matcher(literal);
            if (macher.find())
                return tokenKey;
        }
        return Constants.UNKNOWN;
    }

    public boolean checkLiteralPattern(String literal, int literalToken) {
        if (tokenPatterns.containsKey(literalToken)) {
            Pattern patern = Pattern.compile(tokenPatterns.get(literalToken));
            Matcher macher = patern.matcher(literal);
            return macher.find();
        }
        return false;
    }

    public static void main(String[] args) {
        TokenSpecifierRegex lar = new TokenSpecifierRegex();
        String[] testLiterals = { "0xa3", "0xa", "0xa.", "0x.af", "0", "123", "0b0101", "023", "0b102", "0345u" };
        for (String string : testLiterals) {
            System.out.println("token of " + string + "\tis\t" + lar.getNumberClass(string));
        }
        // System.out.println(lar.checkLiteralPattern("0xa3", 11));
    }
}
