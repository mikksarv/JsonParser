package org.example;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
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
        int n;
        char prev = ' ';
        while ((n = input.read()) != -1) {

            char c = (char) n;
            if (c == 'n' && prev == ' ') return checkNull(input);
            if (Character.isDigit(c) && prev == ' ') return checkNumber(input, c);

//            if (c == '"') return checkString(input);
//            if (c == '{') return checkObject(input);
//            if (c == '[') return checkArray(input);
//            if (c == 't' || c == 'f') return checkBool(input);
            prev = c;
        }
        throw new IllegalArgumentException("Oh nooo!");
    }

    private Object checkNull(Reader value) throws IOException {
        if (value.read() == 'u' && value.read() == 'l' && value.read() == 'l') {
            int nextChar = value.read();
            if (nextChar == -1 || (nextChar == ' ' || nextChar == ',')) {
                return null;
            }
        }

        throw new IllegalArgumentException("Oh nooo! No Null");
    }

    private Object checkNumber(Reader value, char first) throws IOException {

        StringBuilder line = new StringBuilder();
        line.append(first);
        int n;
        while ((n = value.read()) != -1) {
            char c = (char) n;
            if (c == '.') line.append(c);
            else if (!Character.isDigit(c)) break;
            if (Character.isDigit(c)) line.append(c);
        }
        return readNumType(line);
    }


    private static Number readNumType(StringBuilder line) {
        int dots = (int) line.toString().chars().filter(c -> c == '.').count();
        if (!line.toString().contains(".")) {
            return Integer.valueOf(line.toString());
        } else if (dots == 1) {
            return Double.valueOf(line.toString());
        } else {
            throw new IllegalArgumentException("Oh nooo! Not a number");
        }
    }

    private Object checkString(Reader input) {
        return "";
    }

}


