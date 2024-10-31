package org.example;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// * language=json */
public class JsonParser {

    public Object parse(String input) {
        try (var reader = new StringReader(input)) {
            return parse(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse(Reader input) throws IOException {
        int n;
        char prev = ' ';
        while ((n = input.read()) != -1) {

            char c = (char) n;
            if (c == 'n' && prev == ' ') return parseNull(input);
            if ((Character.isDigit(c) || c == '-') && prev == ' ') return parseNumber(input, c);
            if (c == '"') return parseString(input);
            if (c == '[' && prev == ' ') return parseArray(input);
            if (c == 't') return parseTrue(input);
            if (c == 'f') return parseFalse(input);
            if (c == '{') return parseObject(input);
            if (c == '}') return "}";
//            if (Character.isWhitespace(c)) {
//                continue;
//            }
            prev = c;
        }
        throw new IllegalArgumentException("Oh nooo!");
    }

    private Object parseObject(Reader input) throws IOException {
        Map<String, Object> map = new HashMap<>();
        char n = ' ';
        while (n != '}') {

            String key = (String) parse(input);
            if (key.equals("}")) return map;
            input.read();
            Object value = parse(input);
            map.put(key, value);

            n = (char) input.read();
            if (n == ',') n = (char) input.read();
        }

        if (n == -1) throw new IllegalArgumentException("Oh nooo! Not a Map");
        return map;
    }

    private Boolean parseTrue(Reader value) throws IOException {
        if (value.read() == 'r' && value.read() == 'u' && value.read() == 'e') {
            if (checkNextChar(value)) return true;
        }
        throw new IllegalArgumentException("Oh nooo! Not true");
    }


    private Boolean parseFalse(Reader value) throws IOException {
        if (value.read() == 'a' && value.read() == 'l' && value.read() == 's' && value.read() == 'e') {
            if (checkNextChar(value)) return false;
        }
        throw new IllegalArgumentException("Oh nooo! Not false");
    }

    private List<Object> parseArray(Reader input) throws IOException {
        List<Object> a = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        int n;
        while ((n = input.read()) != -1) {
            if ((char) n == ']') {
                if (!line.isEmpty()) a.add(parse(line.toString()));
                break;
            }

            if (n == ',') {
                a.add(parse(line.toString()));
                line.setLength(0);
            }
            line.append((char) n);
        }
        if (n == -1) throw new IllegalArgumentException("Oh nooo! Not a Array");
        return a;
    }

    private Object parseNull(Reader value) throws IOException {
        if (value.read() == 'u' && value.read() == 'l' && value.read() == 'l') {
            if (checkNextChar(value)) return null;
        }
        throw new IllegalArgumentException("Oh nooo! No Null");
    }

    private Number parseNumber(Reader value, char first) throws IOException {

        StringBuilder line = new StringBuilder();
        line.append(first);
        int n;
        while ((n = value.read()) != -1) {
            char c = (char) n;
            if (c == '.') line.append(c);
            else if (!Character.isDigit(c)) break;
            if (Character.isDigit(c)) line.append(c);
        }
        return parseNumber(line);
    }

    private static Number parseNumber(StringBuilder line) {
        int dots = (int) line.toString().chars().filter(c -> c == '.').count();
        if (!line.toString().contains(".")) {
            return Integer.valueOf(line.toString());
        } else if (dots == 1) {
            return Double.valueOf(line.toString());
        } else {
            throw new IllegalArgumentException("Oh nooo! Not a number");
        }
    }

    private String parseString(Reader input) throws IOException {
        StringBuilder line = new StringBuilder();
        int n;

        while ((n = input.read()) != -1) {
            if ((char) n == '"') break;
            line.append((char) n);
        }
        if (n == -1) throw new IllegalArgumentException("Oh nooo! Not a String");

        return line.toString();
    }

    private static boolean checkNextChar(Reader value) throws IOException {
        int nextChar = value.read();
        if (nextChar == -1 || (nextChar == ' ' || nextChar == ',' || nextChar == '\n' || nextChar == '}' || nextChar == ']')) {

            return true;
        }
        return false;
    }
}