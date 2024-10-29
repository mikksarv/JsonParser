package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JsonParserTest {
    JsonParser parser = new JsonParser();

    @Test
    void jsonNull() {
        assertNull(parser.parse("null"));
    }

    @Test
    void integer() {
        assertEquals(123, parser.parse("123"));
    }

    @Test
    void doubleNumber() {
        assertEquals(234.67, parser.parse("234.67"));
    }

    @Test
    void string() {
        assertEquals("hello", parser.parse("\"hello\""));
    }


//    @Test
//    void emptyArray() {
//        assertEquals(emptyList(), parser.parse("[]"));
//    }

}