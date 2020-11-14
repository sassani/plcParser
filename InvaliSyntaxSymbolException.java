public class InvaliSyntaxSymbolException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvaliSyntaxSymbolException(String errorMessage, int rowNum, int colNum) {
        // String message = "Syntax Error:\n";
        super("\n\rSyntax Error:\n" + errorMessage + " At (Row " + rowNum + ", Column " + colNum + ")");
    }
}
