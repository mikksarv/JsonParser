package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;


public class JsonParserThreeTest {

    @Test
    void string() {
        assertEquals("hello, Mari & Toomas", JsonParser.parse("\"hello, Mari & Toomas\""));
    }

    @Test
    void integer() {
        assertEquals(123, JsonParser.parse("123"));
    }

    @Test
    void minusNumber() {
        assertEquals(-123, JsonParser.parse("-123"));
        assertEquals(-12.3, JsonParser.parse("-12.3"));
    }

    @Test
    void doubleNumber() {
        assertEquals(1.23, JsonParser.parse("1.23"));
    }

    @Test
    void nullValue() {
        assertNull(JsonParser.parse("null"));
    }

    @Test
    void booleanValue() {
        assertEquals(true, JsonParser.parse("true"));
        assertEquals(false, JsonParser.parse("false"));
    }

    @Test
    void emptyArray() {
        assertEquals(emptyList(), JsonParser.parse("[]"));
    }

    @Test
    void stringArray() {
        var expected = Arrays.asList("apple", "orange", "cherry");
        assertEquals(expected, JsonParser.parse("[\"apple\", \"orange\", \"cherry\"]"));
    }

    @Test
    void integerArray() {
        var expected = Arrays.asList(1, 22, 3);
        assertEquals(expected, JsonParser.parse("[1, 22, 3]"));
    }

    @Test
    void minusNumberArray() {
        var expected = Arrays.asList(-1, 22.3, 3);
        assertEquals(expected, JsonParser.parse("[-1, 22.3, 3]"));
    }

    @Test
    void doubleArray() {
        var expected = Arrays.asList(1.2, 22.2, 3.33);
        assertEquals(expected, JsonParser.parse("[1.2, 22.2, 3.33]"));
    }

    @Test
    void emptyObject() {
        assertEquals(emptyMap(), JsonParser.parse("{}"));
    }

    @Test
    void stringObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", "value");
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": \"value\"}"));
    }

    @Test
    void integerObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", 8);
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": 8}"));
    }

    @Test
    void nullObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", null);
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": null}"));
    }

    @Test
    void doubleObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", 8.8);
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": 8.8}"));
    }

    @Test
    void booleanObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", true);
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": true}"));
    }

    @Test
    void arrayObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", Arrays.asList("apple", "orange", "cherry"));
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": [\"apple\", \"orange\", \"cherry\"]}"));
    }

    @Test
    public void mixedArrayObject() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key", Arrays.asList(12, "orange", null, true, 1.2));
        }};
        assertEquals(expected, JsonParser.parse("{\"key\": [12, \"orange\", null, true, 1.2]}"));
    }

    @Test
    void multipleNestedObjects() {
        var expected = Map.of(
                "key1", Map.of(
                        "key11", Map.of(
                                "key33", Map.of("key44", false)
                        )
                ),
                "key2", Map.of(
                        "key22", List.of(1.2, 22.2, 3.33)
                ),
                "key3", true,
                "key4", "apple pie",
                "key5", 101
        );

        assertEquals(expected, JsonParser.parse(/* language=json */ """
            {
              "key1": {"key11": {"key33": {"key44":  false}}},
              "key2": {"key22": [1.2, 22.2, 3.33]},
              "key3": true,
              "key4": "apple pie",
              "key5": 101
            }"""));
    }

    @Test
    void oneLineMultipleNestedObjects() {
        var expected = Map.of(
                "key1", Map.of(
                        "key2", Map.of(
                                "key3", false)));
        assertEquals(expected, JsonParser.parse(/* language=json */ """
            {"key1": {"key2": {"key3": false}}}"""));
    }

    @Test
    void multipleObjects() {
        var expected = new LinkedHashMap<String, Object>() {{
            put("key1", true);
            put("key2", false);
            put("key3", null);
            put("key4", "apple pie");
            put("key5", 101);
        }};
        assertEquals(expected, JsonParser.parse(/* language=json */ """
            {
              "key1": true,
              "key2": false,
              "key3": null,
              "key4": "apple pie",
              "key5": 101
            }"""));
    }

    @Test
    public void largeObject() {
        var largeJson = new StringBuilder("{");
        for (int i = 0; i < 10000; i++) {
            largeJson.append("\"key").append(i).append("\": ").append(i).append(", ");
        }
        largeJson.delete(largeJson.length() - 2, largeJson.length());
        largeJson.append("}");

        var expected = new LinkedHashMap<>() {{
            for (int i = 0; i < 10000; i++) {
                put("key" + i, i);
            }
        }};
        assertEquals(expected, JsonParser.parse(largeJson.toString()));
    }

    @Test
    public void nestedEmptyObjects() {
        var expected = Map.of(
                "key1", Map.of(
                        "key2", Map.of()));
        assertEquals(expected, JsonParser.parse(/* language=json */ """
            {"key1": {"key2": {}}}"""));
    }
}
