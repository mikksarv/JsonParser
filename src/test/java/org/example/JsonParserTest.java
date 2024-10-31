package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {
    JsonParser parser = new JsonParser();

    //todo add test exeptions
    @Test
    void jsonNull() {
        //todo does not work with anull or nulla
        assertNull(parser.parse("null"));
        assertNull(parser.parse(" null"));
        assertNull(parser.parse("null,"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("nulla"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("anull"));
    }

    @Test
    void integer() {
        //todo negative values
        assertEquals(123, parser.parse("123"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.3.4"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1..67"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(".346.5"));
    }

    @Test
    void doubleNumber() {
        assertEquals(234.67, parser.parse("234.67"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1.2.3.4"));

    }

    @Test
    void string() {
        assertEquals("hello", parser.parse("\"hello\""));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("\"hello"));
        assertThrows(IllegalArgumentException.class, () -> parser.parse("hello\""));

    }

    @Test
    void bool() {
        assertEquals(true, parser.parse("true"));
        assertEquals(false, parser.parse("false"));
    }

    @Test
    void array() {
        assertEquals(emptyList(), parser.parse("[]"));
        assertEquals(new ArrayList<>(List.of(1)), parser.parse("[1]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, 2)), parser.parse("[1, 2]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), parser.parse("[1, 2, 3]"));
        assertEquals(new ArrayList<>(Arrays.asList(1, true)), parser.parse("[1, true]"));


    }


    @Test
    void object() {
        assertEquals(emptyMap(), parser.parse("{}"));

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key", 1);
        assertEquals(expectedMap, parser.parse("{\"key\": 1}"));
        expectedMap.clear();
        expectedMap.put("name", "value");
        assertEquals(expectedMap, parser.parse("{\"name\": \"value\"}"));
    }

    @Test
    void mixedTestTypeThird() {

        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);
        employee.put("gt", 35);

        assertEquals(employee, parser.parse("""
                {
                    "id": 34,
                    "gt": 35
                }
                """));
    }

    @Test
    void stringObject() {
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
    void mixedTestType() {
        Map<String, Object> expectedMap = new HashMap<>();

        Map<String, Object> employee = new HashMap<>();
        employee.put("id", 34);

        expectedMap.put("e", employee);
        expectedMap.put("isFullTime", true);

        assertEquals(expectedMap, parser.parse("""
                {
                    "e": {
                        "id": 34
                    },
                    "isFullTime": true
                }
                """));
    }



//    @Test
//    void mixedTestType() {
//        Map<String, Object> expectedMap = new HashMap<>();
//
//        Map<String, Object> employee = new HashMap<>();
//        employee.put("id", 34);
//        employee.put("shoe size", "56.6"); // corrected to string for proper JSON format
//        employee.put("name", "Johnson Burger");
//        employee.put("department", "Hamburger engineer");
//        employee.put("skills", List.of("Flipping", "Cleaning", "Fork master"));
//        expectedMap.put("employee", employee);
//        expectedMap.put("isFullTime", true);
//
//        assertEquals(expectedMap, parser.parse("""
//                {
//                    "employee": {
//                        "id": 34,
//                        "shoe size": 56.6,
//                        "name": "Johnson Burger",
//                        "department": "Hamburger engineer",
//                        "skills": ["Flipping", "Cleaning", "Fork master"]
//                    },
//                    "isFullTime": true
//                }
//                """));
//    }

//    @Test
//    void mixedTestTypeSecond() {
//        Map<String, Object> expectedMap = new HashMap<>();
//
//        Map<String, Object> employee = new HashMap<>();
//        employee.put("id", 34);
//        employee.put("shoe size", "56.6"); // corrected to string for proper JSON format
//        employee.put("name", "Johnson Burger");
//        employee.put("department", "Hamburger engineer");
//        employee.put("skills", List.of("Flipping", "Cleaning", "Fork master"));
//        expectedMap.put("employee", employee);
//        expectedMap.put("isFullTime", true);
//        assertEquals(expectedMap, parser.parse("{\"employee\":{\"id\":34,\"shoe size\":56.6,\"name\":\"Johnson Burger\",\"department\":\"Hamburger engineer\",\"skills\":[\"Flipping\",\"Cleaning\",\"Fork master\"]},\"isFullTime\":true}"));
//    }
}