package org.example;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Stack;


public class Parser {
    private String input;

    public Parser(String input) {
        this.input = input;
    }

    boolean isValid() {
        Stack<Character> stack = new Stack<>();
        char[] chars = this.input.toCharArray();

        if (!((chars[0] == '[' && chars[chars.length - 1] == ']') || (chars[0] == '{' && chars[chars.length - 1] == '}')))
            return false;
        boolean isValid = true;

        for (char c : chars) {
            if (c == '{') {
                stack.push('}');
            }
            else if (c == '[') {
                stack.push(']');
            }
            else if (c == '}' && !stack.isEmpty()) {
                char popped = stack.pop();
                if (c != popped) isValid = false;
            }
            else if (c == ']' && !stack.isEmpty()) {
                char popped = stack.pop();
                if (c != popped) isValid = false;
            }
        }

        return isValid;

    }

    public static int createStringField(String name, String tom) {
     //   ArrayList<Object> aa= new ArrayList<>();
//        Object stringObject = new Object();
//        stringObject.
//
//    }
//    public static Object parse(String json) {
//
//        System.out.println(new Object().toString());
//
//        // Return the input JSON string as-is
//        return new Object();
//    }
//
//    public static void main(String[] args) {
//        System.out.println(parse(""));
//    }
}
