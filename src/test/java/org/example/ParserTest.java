package org.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void isValidTest(){
        assertTrue(new Parser("{}").isValid());
        assertFalse(new Parser("{").isValid());
        //todo add last part of test
//        assertFalse(Parser.isValid("{}}"));
    }
    @Test
    void stringFieldTest(){

        assertEquals(Objects.hash("name", "Tom"), Parser.createStringField("name", "Tom"));
    }

}



