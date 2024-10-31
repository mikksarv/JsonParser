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
    private Reader input;

    public JsonParser(Reader input) {
        this.input = input;
    }

    public static Object parse(String input) {
        try (var reader = new StringReader(input)) {
            return new JsonParser(reader).parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object parse() throws IOException {
        int n;
        char prev = ' ';
        while ((n = input.read()) != -1) {
            char c = (char) n;
            if (c == 'n' && prev == ' ') return parseNull();
            if ((Character.isDigit(c) || c == '-') && prev == ' ') return parseNumber(c);
            if (c == '"') return parseString();
            if (c == '[' && prev == ' ') return parseArray();
            if (c == 't') return parseTrue();
            if (c == 'f') return parseFalse();
            if (c == '{') return parseObject();
            if (c == '}') return "}";
//            if (Character.isWhitespace(c)) {
//                continue;
//            }
            prev = c;
        }
        throw new IllegalArgumentException("Oh nooo!");
    }

    private Object parseObject() throws IOException {
        var map = new HashMap<String, Object>();
        char n = ' ';
        while (n != '}') {
            String key = (String) parse();
            if (key.equals("}")) return map;
            input.read();
            map.put(key, parse());

            n = (char) input.read();
            if (n == ',') n = (char) input.read();
        }

        if (n == Character.MAX_VALUE) throw new IllegalArgumentException("Oh nooo! Not a Map");
        return map;
    }

    private Boolean parseTrue() throws IOException {
        if (input.read() == 'r' && input.read() == 'u' && input.read() == 'e') {
            if (checkNextChar()) return true;
        }
        throw new IllegalArgumentException("Oh nooo! Not true");
    }


    private Boolean parseFalse() throws IOException {
        if (input.read() == 'a' && input.read() == 'l' && input.read() == 's' && input.read() == 'e') {
            if (checkNextChar()) return false;
        }
        throw new IllegalArgumentException("Oh nooo! Not false");
    }

    private List<Object> parseArray() throws IOException {
        List<Object> a = new ArrayList<>();
        var line = new StringBuilder();

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

    private Object parseNull() throws IOException {
        if (input.read() == 'u' && input.read() == 'l' && input.read() == 'l') {
            if (checkNextChar()) return null;
        }
        throw new IllegalArgumentException("Oh nooo! No Null");
    }

    private Number parseNumber(char first) throws IOException {
        var line = new StringBuilder();
        line.append(first);
        int n;
        while ((n = input.read()) != -1) {
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

    private String parseString() throws IOException {
        var line = new StringBuilder();
        int n;

        while ((n = input.read()) != -1) {
            if ((char) n == '"') break;
            line.append((char) n);
        }
        if (n == -1) throw new IllegalArgumentException("Oh nooo! Not a String");

        return line.toString();
    }

    private boolean checkNextChar() throws IOException {
        int nextChar = input.read();
        return nextChar == -1 || (nextChar == ' ' || nextChar == ',' || nextChar == '\n' || nextChar == '}' || nextChar == ']');
    }
}