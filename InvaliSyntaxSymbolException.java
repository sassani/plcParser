public class InvaliSyntaxSymbolException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public InvaliSyntaxSymbolException(String errorMessage) {
        // String message = "Syntax Error:\n";
        super("\n\rSyntax Error:\n" + errorMessage);
    }
}
