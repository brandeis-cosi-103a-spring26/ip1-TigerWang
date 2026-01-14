package edu.brandeis.cosi103a.ip1;

import java.util.Random;
import java.util.Scanner;

/**
 * Simple 2-player dice game played on the command line.
 */
public class App {
    private static final int TURNS_PER_PLAYER = 10;
    private static final int MAX_REROLLS = 2;
    private static final int SIDES = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        System.out.print("Enter name for Player 1 (press Enter for 'Player 1'): ");
        String name1 = scanner.nextLine().trim();
        if (name1.isEmpty()) name1 = "Player 1";

        System.out.print("Enter name for Player 2 (press Enter for 'Player 2'): ");
        String name2 = scanner.nextLine().trim();
        if (name2.isEmpty()) name2 = "Player 2";

        int score1 = 0;
        int score2 = 0;

        for (int turn = 1; turn <= TURNS_PER_PLAYER; turn++) {
            System.out.println("\n--- Round " + turn + " ---");
            score1 += playTurn(scanner, rand, name1);
            score2 += playTurn(scanner, rand, name2);
            System.out.println(name1 + ": " + score1 + "    " + name2 + ": " + score2);
        }

        System.out.println("\n=== Final Scores ===");
        System.out.println(name1 + ": " + score1);
        System.out.println(name2 + ": " + score2);

        if (score1 > score2) {
            System.out.println("Winner: " + name1);
        } else if (score2 > score1) {
            System.out.println("Winner: " + name2);
        } else {
            System.out.println("The game is a tie.");
        }

        scanner.close();
    }

    private static int playTurn(Scanner scanner, Random rand, String playerName) {
        System.out.println(playerName + "'s turn.");
        int rerollsLeft = MAX_REROLLS;
        int current = rollDie(rand);
        System.out.println("Rolled: " + current);
        while (rerollsLeft > 0) {
            System.out.print("Reroll? (" + rerollsLeft + " left) [y/N]: ");
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("y") || resp.equals("yes")) {
                current = rollDie(rand);
                rerollsLeft--;
                System.out.println("New roll: " + current);
            } else {
                break;
            }
        }
        System.out.println(playerName + " ends turn with " + current + " points.");
        return current;
    }

    private static int rollDie(Random rand) {
        return rand.nextInt(SIDES) + 1;
    }
}
