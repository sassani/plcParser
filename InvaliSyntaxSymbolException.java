public class InvaliSyntaxSymbolException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public InvaliSyntaxSymbolException(String errorMessage, Throwable err) {
        // String message = "Syntax Error:\n";
        super("Syntax Error:\n" + errorMessage, err);
    }
}
