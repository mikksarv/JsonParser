package org.example;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/// * language=json */
public class JsonParser {
    private final Reader input;
    private char c;

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

        char prev = ' ';
        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (c == 'n' && prev == ' ') return parseNull();
            if ((Character.isDigit(c) || c == '-') && prev == ' ') return readNumber(c);
            if (c == '"') return parseString();
            if (c == '[' && prev == ' ') return parseArray();
            if (c == 't') return parseTrue();
            if (c == 'f') return parseFalse();
            if (c == '{') return parseObject();
            if (c == '}') return "}";
            prev = c;
        }
        throw new IllegalArgumentException("Oh nooo!");
    }

    private List<Object> parseArray() throws IOException {
        List<Object> a = new ArrayList<>();
        var line = new StringBuilder();

        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (c == ']') {
                if (!line.isEmpty()) a.add(parse(line.toString()));
                break;
            }
            if (c == ',') {
                a.add(parse(line.toString()));
                line.setLength(0);
            }
            line.append(c);
        }
        if (c == Character.MAX_VALUE) throw new IllegalArgumentException("Oh nooo! Not a Array");
        return a;
    }

    private Object parseObject() throws IOException {
        var map = new HashMap<String, Object>();

        while (c != '}') {
            String key = (String) parse();
            if (key.equals("}")) return map;
            input.read();
            map.put(key, parse());
            c = (char) input.read();
            if (c == Character.MAX_VALUE) throw new IllegalArgumentException("Oh nooo! Not a Map");
            if (c == ',') c = (char) input.read();
        }
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

    private Object parseNull() throws IOException {
        if (input.read() == 'u' && input.read() == 'l' && input.read() == 'l') {
            if (checkNextChar()) return null;
        }
        throw new IllegalArgumentException("Oh nooo! No Null");
    }

    private Number readNumber(char first) throws IOException {
        var line = new StringBuilder();
        line.append(first);

        input.mark(1);
        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (c == '}' || c == ']') input.reset();
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
        } else if (dots == 1 && !(line.toString().toCharArray()[0] == '-' && line.toString().toCharArray()[1] == '.')) {
            return Double.valueOf(line.toString());
        }
        throw new IllegalArgumentException("Oh nooo! Not a number");
    }

    private String parseString() throws IOException {
        var line = new StringBuilder();

        while ((c = (char) input.read()) != Character.MAX_VALUE) {
            if (c == '"') break;
            line.append(c);
        }
        if (c == Character.MAX_VALUE) throw new IllegalArgumentException("Oh nooo! Not a String");

        return line.toString();
    }

    private boolean checkNextChar() throws IOException {
        input.mark(1);
        char nextChar = (char) input.read();
        if (nextChar == '}' || nextChar == ']') {
            input.reset();
            return true;
        } else return nextChar == Character.MAX_VALUE || nextChar == ' ' || nextChar == ',' || nextChar == '\n';
    }
}

