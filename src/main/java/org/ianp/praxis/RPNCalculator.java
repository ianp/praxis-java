// Copyright © 2013 Ian Phillips.
// See the accompanying LICENSE.md file for further information.
package org.ianp.praxis;

import java.io.Console;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A simple reverse-polish notation calculator.
 * <p>
 * A solution to <a href="http://programmingpraxis.com/2009/02/19/rpn-calculator/">this problem</a>.
 */
public class RPNCalculator {

    private static final Console console = System.console();
    private static final Deque<BigDecimal> stack = new ArrayDeque<>();
   
    private static void processLine(String line) {
        for (String s : line.split("\\s+")) {
            switch (s) {
            case "+":
                stack.push(stack.pop().add(stack.pop()));
                break;
            case "-":
                stack.push(stack.pop().subtract(stack.pop()));
                break;
            case "*":
                stack.push(stack.pop().multiply(stack.pop()));
                break;
            case "/":
                stack.push(stack.pop().divide(stack.pop()));
                break;
            case "quit":
            case "exit":
                System.exit(0);
            default:
                stack.push(new BigDecimal(s));
            }
        }
        console.format("%s%n", stack.peek()).flush();
    }

    public static void main(String[] args) {
        try {
            String line;
            while ((line = console.readLine("> ")) != null) {
                processLine(line);
            }
        } catch (Exception e) {
            console.format("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
        }
    }

}

