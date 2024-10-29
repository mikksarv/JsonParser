package org.example;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/// * language=json */
public class JsonParser {

    public Object parse(String input) {
        try {
            return parse(new StringReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(Reader input) throws IOException {
        int theCharnum = input.read();

        while (theCharnum != -1) {
            char c = (char) theCharnum;
            if (c == 'n') return checkNull(input);
            if (Character.isDigit(c)) return checkNumber(input, c);

//            if (c == '"') return checkNumber(input);
//            if (c == '{') return checkObject(input);
//            if (c == '[') return checkArray(input);
//            if (c == 't' || c == 'f') return checkBool(input);
            theCharnum = input.read();

        }

        return new IllegalArgumentException("Oh nooo!");
    }

    private Object checkNull(Reader value) throws IOException {
        if (value.read() == 'u' && value.read() == 'l' && value.read() == 'l') return null;
        return new IllegalArgumentException("Oh nooo! No Null");
    }


    private Object checkNumber(Reader value, char first) throws IOException {

        StringBuilder line = new StringBuilder();
        line.append(first);
        int theCharnum = value.read();

        while (theCharnum != -1) {
            char c = (char) theCharnum;

            if (c == '.') line.append(c);
            else if (!Character.isDigit(c)) break;
            if (Character.isDigit(c)) line.append(c);
            theCharnum = value.read();
        }

        int dots = (int) line.toString().chars().filter(c -> c == '.').count();
        if (!line.toString().contains(".")) {
            return Integer.valueOf(line.toString());
        } else if (dots > 1) {
            return line.toString();
        } else if (dots == 1) {
            return Double.valueOf(line.toString());
        } else {
            return new IllegalArgumentException("Oh nooo! Not a number");
        }
    }

    private Object checkString(Reader input) {
        return "";
    }


}


//        if (input.equals("null")) return null;
//        if (input.matches("-?[0-9]+")) return Integer.valueOf(input);
//        if (input.matches("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?")) return Double.valueOf(input);
//        if (input.startsWith("\"") && input.endsWith("\"")) return input.substring(1, input.length() - 1);
//        if (input.toCharArray()[0] == '[' && input.toCharArray()[input.length() - 1] == ']') return new ArrayList<>();
//        if (input.equals("true") || input.equals("false")) return Boolean.parseBoolean(input);


//        StringBuilder line = new StringBuilder();
//        int theCharnum = value.read();
//
//        char[] nulls = new char[]{'u', 'l', 'l'};
//        int i = 0;
//
//        while (value.read() != -1) {
//            char c = (char) theCharnum;
//            if (c != nulls[i++]) return "";
//
//            line.append(c);
//            if (i == 3) break;
//            theCharnum = value.read();
//        }
//        return line.toString().equals("ull") ? null : new IllegalArgumentException("Oh nooo! No Null");