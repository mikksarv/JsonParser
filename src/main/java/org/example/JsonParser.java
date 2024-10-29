package org.example;

import java.io.Reader;
import java.io.StringReader;

/// * language=json */
public class JsonParser {
    public Object parse(String input) {
        if (input.equals("null")) return null;
        if (input.matches("-?[0-9]+")) return Integer.valueOf(input);
        if (input.matches("[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?")) return Double.valueOf(input);
        if (input.startsWith("\"") && input.endsWith("\""))  return input.substring(1, input.length() - 1);
        return parse(new StringReader(input));
    }

    public Object parse(Reader input) {
        System.out.println(input);

        return input;
    }


}
