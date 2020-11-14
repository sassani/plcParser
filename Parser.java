public class Parser {
    private LexicalAnalyzer lexicalAnalyzer;
    private int nextToken;

    public Parser(String fileName) {
        lexicalAnalyzer = new LexicalAnalyzer(fileName);
    }

    public void Run() {
        System.out.println("Start parsing ...");
        try {
            lex();
            switch (nextToken) {
                case Constants.IDENT:
                stmt();
                break;
                case Constants.WHILE_CODE:
                whileStmt();
                break;
                
                default:
                throw new java.lang.IllegalArgumentException("Unknown command!");
                
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("... End of parsing");

    }

    /**
     * <while_stmt> --> while ‘(’ <bool_expr> ‘)’ <block>
     */
    private void whileStmt() {
        System.out.println("Enter <While>");
        lex();
        if (nextToken != Constants.LEFT_PAREN)
            throw new java.lang.IllegalArgumentException("Missing '('");
        boolExpr();
        lex();
        if (nextToken != Constants.RIGHT_PAREN)
            throw new java.lang.IllegalArgumentException("Missing ')'");
        block();
        System.out.println("Exit <While>");

    }

    /**
     * <block> --> <stmt> ‘;’| ‘{’ <stmt> ‘;’{<stmt> ‘;’} ‘}’
     */
    private void block() {
        System.out.println("Enter <block>");
        lex();
        if (nextToken == Constants.LEFT_CURLE) {
            lex();
            stmt();
            lex();
            while (nextToken == Constants.IDENT) {
                stmt();
                lex();
            }
            if (nextToken != Constants.RIGHT_CURLE)
                throw new java.lang.IllegalArgumentException("Missing '}'");
        } else {
            stmt();
        }
        System.out.println("Exit <block>");
    }

    /**
     * <stmt> --> id ‘=’ <expr> ';'
     */
    private void stmt() {
        lex();
        if (nextToken != Constants.ASSIGN_OP)
            throw new java.lang.IllegalArgumentException("missing '='");
        expr();
        if (nextToken != Constants.SEMICOLON)
            throw new java.lang.IllegalArgumentException("missing ';'");
        System.out.println("");
    }

    /**
     * <expr> --> <term> {(+ | -) <term>}
     */
    private void expr() {
        System.out.println("Enter <expr>");
        term();
        while (nextToken == Constants.ADD_OP || nextToken == Constants.SUB_OP) {
            term();
        }
        System.out.println("Exit <expr>");

    }

    /**
     * <term> --> <factor> {(* | /) <factor>}
     */
    private void term() {
        System.out.println("Enter <term>");
        factor();
        /*
         * As long as the next token is * or /, get the next token and parse the next
         * factor
         */
        while (nextToken == Constants.MULT_OP || nextToken == Constants.DIV_OP) {
            factor();
        }
        System.out.println("Exit <term>");
    }

    /**
     * <factor> --> id | int_constant | ( <expr> )
     */
    private void factor() {
        System.out.println("Enter <factor>");
        lex();
        /* Determine which RHS */
        if (nextToken == Constants.IDENT || nextToken == Constants.LITERAL_INT_DEC) {
            /* Get the next token */
            lex();

            /*
             * If the RHS is ( <expr> ), call lex to pass over the left parenthesis, call
             * expr, and check for the right parenthesis
             */
        } else if (nextToken == Constants.LEFT_PAREN) {
            expr();
            if (nextToken != Constants.RIGHT_PAREN)
                throw new java.lang.IllegalArgumentException("Missing ')'");
            lex();
        } else {
            // It was not an id, an integer literal, or a left parenthesis
            throw new java.lang.IllegalArgumentException("Missing ( .");
        }
        System.out.println("Exit <factor>");
    }

    /**
     * <bool_expr> --> <bool> | <...> // TODO: this function is not compelete!
     */
    private void boolExpr() {
        System.out.println("Enter <bool_expr>");
        lex();
        if (nextToken != Constants.TRUE_CODE && nextToken != Constants.FALSE_CODE)
            throw new java.lang.IllegalArgumentException("it is not a boolean expresion");
        System.out.println("Exit <bool_expr>");
    }

    private void lex() {
        nextToken = lexicalAnalyzer.NextToken();
    }

    public static void main(String[] args) {
        System.out.println("Parser for java EBNF");
        Parser parser = new Parser(args[0]);
        System.out.println("Parssing (" + args[0] + ")");
        parser.Run();
    }

}
