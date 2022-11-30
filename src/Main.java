import java.io.*;
import java.text.ParseException;

class LexicalAnalyzer {

    public enum Token {
        KEYWORD, IDENTIFIER, OPERATOR, CONSTANT, SpecialCharacter, BLANK
    }

    InputStream inputStream;
    int currentChar;
    int currentPosition;
    Token currentToken;

    public LexicalAnalyzer(InputStream inputStream) {
        this.inputStream = inputStream;
        currentPosition = 0;
        nextChar();
    }

    private boolean isBlank(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    private boolean isOperator(int c) {
        return c == '*' || c == '/' || c == '-' || c == '+' || c == '>'
                || c == '<' || c == '&' || c == '|' || c == '!' || c == '~'
                || c == '=';
    }

    private boolean isSpecialChar(int c) {
        return c == ',' || c == '.' || c == ':' || c == ';' || c == '(' || c == ')' || c == '{' || c == '}';
    }

    private boolean isIdentifier(int c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    private boolean isKeyWord(String s) {
        return s.toLowerCase().equals("for")
                || s.toLowerCase().equals("while")
                || s.toLowerCase().equals("if")
                || s.toLowerCase().equals("do")
                || s.toLowerCase().equals("which")
                || s.toLowerCase().equals("public")
                || s.toLowerCase().equals("return")
                || s.toLowerCase().equals("Integer")
                || s.toLowerCase().equals("int")
                || s.toLowerCase().equals("Boolean")
                || s.toLowerCase().equals("import")
                || s.toLowerCase().equals("char")
                || s.toLowerCase().equals("String")
                || s.toLowerCase().equals("private");
    }

    private void nextChar() {
        currentPosition++;
        try {
            currentChar = inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextToken() {
        String temp = "";
        while (isBlank(currentChar)) {
            nextChar();
            currentToken = Token.BLANK;
        }

        while (isIdentifier(currentChar)) {
            temp += (char) currentChar;
            currentToken = Token.IDENTIFIER;
            nextChar();
        }

        // if temp contains string
        if (!temp.equals("")) {
            if (isKeyWord(temp)) {
                currentToken = Token.KEYWORD;
            }
            return;
        }
        if (currentChar >= '0' && currentChar <= '9') {
            currentToken = Token.CONSTANT;
        } else if (isOperator(currentChar)) {
            currentToken = Token.OPERATOR;
        } else if (isSpecialChar(currentChar)) {
            currentToken = Token.SpecialCharacter;
        }
        nextChar();
    }

    public Token curToken() {
        return currentToken;
    }

    public int curPos() {
        return currentPosition;
    }

    public static void main(String[] args) {

        String str = "for (i = 1; i = 5.1e3; i = i + 1) fun1 (a)";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(inputStream);

        while (lexicalAnalyzer.curPos() <= str.length()) {
            lexicalAnalyzer.nextToken();
            System.out.println(lexicalAnalyzer.curToken());
        }
    }
}
