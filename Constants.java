
public class Constants {
        /* Character classes */
        public static final int EOF = -1;
        public static final int LETTER = 0;
        public static final int DIGIT = 1;
        public static final int IDENT_SCL = 2;
        public static final int IDENT_MAP = 3;
        public static final int IDENT_ARR = 4;

        public static final int LITERAL_NUM = 5;
        public static final int LITERAL_INT_DEC = 6;    // decimal integer literals
        public static final int LITERAL_INT_BIN = 7;    // binary integer literals
        public static final int LITERAL_INT_OCT = 8;    // octal integer literals
        public static final int LITERAL_INT_HEX = 9;    // hexadecimal integer literals
        public static final int LITERAL_FLP_DEC = 10;   // floating point literals
        public static final int LITERAL_FLP_HEX = 11;   // floating point literals
        
        public static final int LITERAL_STR = 12;       // string literals
        
        public static final int LITERAL_CHR = 13;           // character literals general
        public static final int LITERAL_CHR_PLN = 14;       // character literals plain
        public static final int LITERAL_CHR_UNI = 15;       // character literals universal
        public static final int LITERAL_CHR_SCP_BKS = 160;  // character literals escape for backslash charachter
        public static final int LITERAL_CHR_SCP_SGQ = 161;  // character literals escape for single qoute charachter
        public static final int LITERAL_CHR_SCP_DBQ = 162;  // character literals escape for double qoute charachter
        public static final int LITERAL_CHR_SCP_QSM = 163;  // character literals escape for question mark
        public static final int LITERAL_CHR_SCP_ALT = 164;  // character literals escape for alert or bell
        public static final int LITERAL_CHR_SCP_BCS = 165;  // character literals escape for backspace
        public static final int LITERAL_CHR_SCP_FRF = 166;  // character literals escape for form feed
        public static final int LITERAL_CHR_SCP_NWL = 167;  // character literals escape for new line
        public static final int LITERAL_CHR_SCP_CGR = 168;  // character literals escape for carriage return
        public static final int LITERAL_CHR_SCP_HRT = 169;  // character literals escape for horizental tab
        public static final int LITERAL_CHR_SCP_VRT = 170;  // character literals escape for vertical tab
        public static final int LITERAL_CHR_SCP_OCT = 171;  // character literals escape for octal number
        public static final int LITERAL_CHR_SCP_HEX = 172;  // character literals escape for hexadecimal number

        public static final int UNKNOWN = 99;
    
        /** Token codes */
        /* Literals codes */
        public static final int IDENT = 100;
    
        /* Operation codes */
        public static final int ASSIGN_OP = 20;     // =    Assignment Operator
        public static final int ADD_OP = 21;        // +    Addition Operator
        public static final int SUB_OP = 22;        // -    Subtraction Operator
        public static final int MULT_OP = 23;       // *    Multiplication Operator
        public static final int DIV_OP = 24;        // /    Division Operator
        public static final int LEFT_PAREN = 25;    // (    Open Parantheses
        public static final int RIGHT_PAREN = 26;   // )    Close Parantheses
        public static final int LEFT_CURLE = 27;    // {    Open Curle bracket
        public static final int RIGHT_CURLE = 28;   // }    Close Curle bracket
        public static final int LEFT_BRACK = 29;    // [    Open square bracket
        public static final int RIGHT_BRACK = 30;   // ]    close square bracket
        public static final int LGNOT_OP = 31;      // !    logical not Operator
        public static final int INCRE_OP = 32;      // ++   increament Operator
        public static final int DECRE_OP = 33;      // --   decreament Operator
        public static final int MODUL_OP = 34;      // %    modulo Operator
        public static final int LGAND_OP = 35;      // &&   logical and Operator
        public static final int LGOR_OP = 36;       // ||   logical or Operator
        public static final int SEMICOLON = 49;     // ;    end of assignment
    
        /* Reserved Keywords */
        public static final int WHILE_CODE = 50;    // while statement
        public static final int IF_CODE = 51;       // if statement
        public static final int ELSE_CODE = 52;     // else statement
        public static final int STMT_CODE = 53;     // statement
        public static final int TRUE_CODE = 80;     // true
        public static final int FALSE_CODE = 81;    // false
}
