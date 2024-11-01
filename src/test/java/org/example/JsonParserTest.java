package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

    @Test
    void jsonNull() {
        assertNull(JsonParser.parse("null"));
        assertNull(JsonParser.parse(" null"));
        assertNull(JsonParser.parse("null,"));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("nulla"),
                "Oh nooo! No Null");

        assertEquals("Oh nooo! No Null",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("nulla")).getMessage());
    }

    @Test
    void integer() {
        assertEquals(123, JsonParser.parse("123"));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("1.2.3.4"),
                "Oh nooo! Not a number");
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse(".346.5"),
                "Oh nooo! Not a number");
        assertEquals("Oh nooo! Not a number",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("1..67")).getMessage());
    }

    @Test
    void doubleNumber() {
        assertEquals(234.67, JsonParser.parse("234.67"));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("1.2.3.4"),
                "Oh nooo! Not a number");
        assertEquals("Oh nooo! Not a number",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("1..67")).getMessage());
    }

    @Test
    void negativeNumbers() {
        assertEquals(-123, JsonParser.parse("-123"));
        assertEquals(-234.67, JsonParser.parse("-234.67"));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("--123"),
                "Oh nooo! Not a number");
        assertEquals("Oh nooo! Not a number",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("-.167")).getMessage());
    }

    @Test
    void string() {
        assertEquals("hello", JsonParser.parse("\"hello\""));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("\"hello"),
                "Oh nooo! Not a String");
        assertEquals("Oh nooo! Not a String",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("hello\"")).getMessage());
    }

    @Test
    void bool() {
        assertEquals(true, JsonParser.parse("true"));
        assertEquals(false, JsonParser.parse("false"));
        assertEquals("Oh nooo! Not false",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("fulse")).getMessage());
        assertEquals("Oh nooo! Not true",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("truu")).getMessage());
    }

    @Test
    void array() {
        assertEquals(emptyList(), JsonParser.parse("[]"));
        assertEquals(new ArrayList<>(List.of(1)), JsonParser.parse("[1]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, 2)), JsonParser.parse("[1, 2]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), JsonParser.parse("[1, 2, 3]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, true, "hello")), JsonParser.parse("[1, true, \"hello\"]"));
        assertEquals(new ArrayList<>(Arrays.asList(3, 5, false, null)), JsonParser.parse("[3, 5, false, null]"));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("[3, 5, false, nulla]"),
                "Oh nooo! No Null");
        assertEquals("Oh nooo! Not a Array",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("[3, 5, false, null")).getMessage());
    }

    @Test
    void emptyObject() {
        assertEquals(emptyMap(), JsonParser.parse("{}"));
        assertThrows(IllegalArgumentException.class,
                () -> JsonParser.parse("{"),
                "Oh nooo! Not a Map");
    }

    @Test
    void object() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", "1");
        assertEquals(expectedMap, JsonParser.parse("{ \"key\": \"1\"}"));
        expectedMap.clear();
        expectedMap.put("name", "value");
        assertEquals(expectedMap, JsonParser.parse("{\"name\":\"value\"}"));
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("{\"name\":\"value\""),
                "Oh nooo! Not a Map");
        assertThrows(
                IllegalArgumentException.class,
                () -> JsonParser.parse("{\"name\"\"value\"}"),
                "Oh nooo!");
        assertEquals("Oh nooo! Not a Map",
                assertThrows(IllegalArgumentException.class, () ->
                        JsonParser.parse("{\"name\":\"value\"")).getMessage());
    }

    @Test
    void objectTwo() {
        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);
        employee.put("gt", 35);
        employee.put("gsc", 356);
        assertEquals(employee, JsonParser.parse("""
                {
                    "id": 34,
                    "gt": 35,
                    "gsc": 356
                }
                """));
    }

    @Test
    void objectThree() {
        Map<String, Object> expectedMap = new HashMap<>();
        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);
        expectedMap.put("employee", employee);
        assertEquals(expectedMap, JsonParser.parse("""
                {
                    "employee": {
                        "id": 34
                    }
                }
                """));
    }

    @Test
    void objectFour() {
        Map<String, Object> expectedMap = new HashMap<>();
        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);
        expectedMap.put("e", employee);
        expectedMap.put("i", true);
        assertEquals(expectedMap, JsonParser.parse("""
                {
                    "e": {
                        "id": 34
                    },
                    "i": true
                }
                """));
    }

    @Test
    void objectFive() {
        Map<String, Object> expectedMap = new HashMap<>();
        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);
        employee.put("shoe size", 56.6);
        employee.put("name", "Johnson Burger");
        employee.put("department", "Hamburger engineer");
        employee.put("skills", List.of("Flipping", "Cleaning", "Fork master"));
        expectedMap.put("employee", employee);
        expectedMap.put("isFullTime", true);
        assertEquals(expectedMap, JsonParser.parse("""
                {
                    "employee": {
                        "id": 34,
                        "shoe size": 56.6,
                        "name": "Johnson Burger",
                        "department": "Hamburger engineer",
                        "skills": ["Flipping", "Cleaning", "Fork master"]
                    },
                    "isFullTime": true
                }
                """));
    }

    @Test
    void objectExtra() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", "1");
        assertEquals(expectedMap, JsonParser.parse("{\"key\":\"1\"}"));
        expectedMap.clear();
        expectedMap.put("n", true);
        assertEquals(expectedMap, JsonParser.parse("{\"n\":true}"));
        expectedMap.clear();
        expectedMap.put("n", false);
        assertEquals(expectedMap, JsonParser.parse("{\"n\":false}"));
        expectedMap.clear();
        expectedMap.put("name", null);
        assertEquals(expectedMap, JsonParser.parse("{\"name\":null}"));
        expectedMap.clear();
        expectedMap.put("name", 23);
        assertEquals(expectedMap, JsonParser.parse("{\"name\":23}"));
        expectedMap.clear();
        expectedMap.put("name", 23.35);
        assertEquals(expectedMap, JsonParser.parse("{\"name\":23.35}"));
    }
}