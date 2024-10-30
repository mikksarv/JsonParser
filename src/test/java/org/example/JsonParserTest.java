package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        assertEquals(new ArrayList<>(Arrays.asList(1, "mjau", false, 45.7)), parser.parse("[1, \"mjau\", false, 45.7]"));

    }


    @Test
    void emptyObject() {
        assertEquals(emptyMap(), parser.parse("{}"));

    }
}