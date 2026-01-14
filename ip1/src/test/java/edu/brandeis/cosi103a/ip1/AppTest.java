package edu.brandeis.cosi103a.ip1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class AppTest {

    // Helper to invoke private static rollDie(Random)
    private int invokeRollDie(Random rand) throws Exception {
        Method m = App.class.getDeclaredMethod("rollDie", java.util.Random.class);
        m.setAccessible(true);
        return (Integer) m.invoke(null, rand);
    }

    // Helper to invoke private static playTurn(Scanner, Random, String)
    private int invokePlayTurn(Scanner scanner, Random rand, String playerName) throws Exception {
        Method m = App.class.getDeclaredMethod("playTurn", Scanner.class, Random.class, String.class);
        m.setAccessible(true);
        return (Integer) m.invoke(null, scanner, rand, playerName);
    }

    @Test
    public void testRollDieRange() throws Exception {
        Random rand = new Random(12345L);
        int v = invokeRollDie(rand);
        assertTrue(v >= 1 && v <= 6, "rollDie should return value in [1,6]");
    }

    @Test
    public void testPlayTurn_noReroll_returnsInitialRoll() throws Exception {
        long seed = 42L;
        Random randForExpected = new Random(seed);
        Random randForCall = new Random(seed);

        int expected = invokeRollDie(randForExpected);

        Scanner scanner = new Scanner(new ByteArrayInputStream("n\n".getBytes()));
        int result = invokePlayTurn(scanner, randForCall, "Tester");
        scanner.close();

        assertEquals(expected, result, "When player declines reroll, returned value should equal initial roll");
    }

    @Test
    public void testPlayTurn_twoRerolls_returnsLastRoll() throws Exception {
        long seed = 99L;
        Random randForExpected = new Random(seed);
        Random randForCall = new Random(seed);

        // advance expected RNG for initial + two rerolls
        invokeRollDie(randForExpected); // initial
        invokeRollDie(randForExpected); // first reroll
        int expectedLast = invokeRollDie(randForExpected); // second reroll

        Scanner scanner = new Scanner(new ByteArrayInputStream("y\ny\n".getBytes()));
        int result = invokePlayTurn(scanner, randForCall, "Tester");
        scanner.close();

        assertEquals(expectedLast, result, "After two rerolls the returned value should be the last roll");
    }

    @Test
    public void testMain_runsToCompletion_andPrintsFinalScores() throws Exception {
        StringBuilder input = new StringBuilder();
        input.append("Alice\n");
        input.append("Bob\n");
        for (int i = 0; i < 10 * 2; i++) {
            input.append("n\n");
        }

        ByteArrayInputStream in = new ByteArrayInputStream(input.toString().getBytes());
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;

        try {
            System.setIn(in);
            System.setOut(new PrintStream(outBytes));

            App.main(new String[0]);

            String output = outBytes.toString();
            assertTrue(output.contains("=== Final Scores ==="), "Main should print final scores header");
            assertTrue(output.contains("Alice:") && output.contains("Bob:"), "Final scores should include both player names");
            assertTrue(output.contains("Winner:") || output.contains("The game is a tie."), "Output should indicate a winner or a tie");
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }
}
