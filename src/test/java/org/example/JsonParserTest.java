package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {
    JsonParser parser = new JsonParser();

    @Test
    void jsonNull() {
        assertNull(parser.parse("null"));
        assertNull(parser.parse(" null"));
        assertNull(parser.parse("null,"));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("nulla"),
                "Oh nooo! No Null");
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("anull"),
                "Oh nooo! No Null");
    }

    @Test
    void integer() {
        assertEquals(123, parser.parse("123"));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("1.2.3.4"),
                "Oh nooo! Not a number");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("1..67"),
                "Oh nooo! Not a number");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(".346.5"),
                "Oh nooo! Not a number");
    }


    @Test
    void doubleNumber() {
        assertEquals(234.67, parser.parse("234.67"));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("1.2.3.4"),
                "Oh nooo! Not a number");

    }

    @Test
    void negativeNumbers(){
        assertEquals(-123, parser.parse("-123"));
        assertEquals(-234.67, parser.parse("-234.67"));
        assertThrows(
        IllegalArgumentException.class,
                () -> parser.parse("--123"),
                "Oh nooo! Not a number");

    }

    @Test
    void string() {
        assertEquals("hello", parser.parse("\"hello\""));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("\"hello"),
                "Oh nooo! Not a String");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("hello\""),
                "Oh nooo! Not a String");

    }

    @Test
    void bool() {
        assertEquals(true, parser.parse("true"));
        assertEquals(false, parser.parse("false"));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("truu"),
                "Oh nooo! Not true");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("fulse"),
                "Oh nooo! Not false");

    }

    @Test
    void array() {
        assertEquals(emptyList(), parser.parse("[]"));
        assertEquals(new ArrayList<>(List.of(1)), parser.parse("[1]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, 2)), parser.parse("[1, 2]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), parser.parse("[1, 2, 3]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, true, "hello")), parser.parse("[1, true, \"hello\"]"));
        assertEquals(new ArrayList<>(Arrays.asList(3, 5, false, null)), parser.parse("[3, 5, false, null]"));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("[3, 5, false, nulla]"),
                "Oh nooo! No Null");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("[3, 5, false, null"),
                "Oh nooo! Not a Array");

    }


    @Test
    void emptyObject() {
        assertEquals(emptyMap(), parser.parse("{}"));
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("{"),
                "Oh nooo! Not a Map");
    }

    @Test
    void object() {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", "1");
        assertEquals(expectedMap, parser.parse("{ \"key\": \"1\"}"));
        expectedMap.clear();
        expectedMap.put("name", "value");
        assertEquals(expectedMap, parser.parse("{\"name\":\"value\"}"));
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("{\"name\":\"value\""),
                "Oh nooo! Not a Map");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse("{\"name\"\"value\"}"),
                "Oh nooo!");


    }

    @Test
    void objectTwo() {
        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);
        employee.put("gt", 35);
        employee.put("gsc", 356);
        assertEquals(employee, parser.parse("""
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
        assertEquals(expectedMap, parser.parse("""
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
        assertEquals(expectedMap, parser.parse("""
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
        assertEquals(expectedMap, parser.parse("""
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
}