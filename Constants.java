
public class Constants {
        /* Character classes */
        public static final int EOF = -1;
        public static final int LETTER = 0;
        public static final int DIGIT = 1;
        public static final int IDENT_SCL = 2;
        public static final int IDENT_MAP = 3;
        public static final int IDENT_ARR = 4;
        public static final int LITERAL_NUM = 5;
        public static final int LITERAL_INT = 6;       // integer literals
        public static final int LITERAL_FLP = 7;       // floating point literals
        public static final int LITERAL_CHR = 8;       // character literals
        public static final int UNKNOWN = 99;
    
        /** Token codes */
        /* Literals codes */
        public static final int IDENT = 10;
        public static final int STR_LIT = 14;       // string literals
    
        /* Operation codes */
        public static final int ASSIGN_OP = 20;     // =
        public static final int ADD_OP = 21;        // +
        public static final int SUB_OP = 22;        // -
        public static final int MULT_OP = 23;       // *
        public static final int DIV_OP = 24;        // /
        public static final int LEFT_PAREN = 25;    // (
        public static final int RIGHT_PAREN = 26;   // )
        public static final int LEFT_CURLE = 27;    // {
        public static final int RIGHT_CURLE = 28;   // }
        public static final int LEFT_BRACK = 29;    // [
        public static final int RIGHT_BRACK = 30;   // ]
        public static final int LGNOT_OP = 31;      // !
        public static final int INCRE_OP = 32;      // ++
        public static final int DECRE_OP = 33;      // --
        public static final int MODUL_OP = 34;      // %
        public static final int LGAND_OP = 35;      // &&
        public static final int LGOR_OP = 36;       // ||
        public static final int SEMICOLON = 49;     // ;
    
        /* Reserved Keywords */
        public static final int WHILE_CODE = 50;
        public static final int IF_CODE = 51;
        public static final int ELSE_CODE = 52;
        public static final int STMT_CODE = 53;
        public static final int TRUE_CODE = 80;
        public static final int FALSE_CODE = 81;
}
