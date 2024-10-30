package org.example;

import org.junit.jupiter.api.Test;

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
    }

    @Test
    void emptyArray() {
        assertEquals(emptyList(), parser.parse("[]"));
    }

    @Test
    void bool() {
        assertEquals(true, parser.parse("true"));
        assertEquals(false, parser.parse("false"));
    }

    @Test
    void emptyObject() {
        assertEquals(emptyMap(), parser.parse("{}"));

    }
}