// Denton Smith smi02055
// Charley Hatterman hatte064

//Import Section
import java.util.Scanner;
import java.util.InputMismatchException;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 *
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        boolean debugMode = false; // Flag to indicate whether the game is in debug mode or not


        System.out.println("Welcome to Minefield!");

        Minefield minefield = null;

        // Get a valid game mode based off user input
        while (true) {
            System.out.println("Enter E for easy, M for medium, or H for hard:");
            String userInput = scanner.next().toUpperCase();

            if (userInput.equals("E")) {
                System.out.println("You selected Easy mode.");
                minefield = new Minefield(5, 5, 5); // Adjust parameters based on your requirements
                break;
            } else if (userInput.equals("M")) {
                System.out.println("You selected Medium mode.");
                minefield = new Minefield(9, 9, 12); // Adjust parameters based on your requirements
                break;
            } else if (userInput.equals("H")) {
                System.out.println("You selected Hard mode.");
                minefield = new Minefield(20, 20, 40); // Adjust parameters based on your requirements
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }

        // Determine weather the user wants to use debug mode or standard mode
        System.out.println("Enter 'D' for debug mode or a different character for standard mode");
        String mode = scanner.next().toUpperCase();
        if(mode.equals("D")){
            System.out.println("You've selected the debug mode");
            debugMode=true;
        }else{
            System.out.println("You are in standard mode");
        }


        // Gets users starting x coordinate
        int xStart, yStart;
        while (true) {
            try {
                System.out.println("Enter X coordinate:");
                xStart = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer for X coordinate:");
                scanner.next();
            }
        }

        // Gets users starting y coordinate
        while (true) {
            try {
                System.out.println("Enter Y coordinate:");
                yStart = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer for Y coordinate:");
                scanner.next();
            }
        }
        while(true){ //Checks xStart and yStart are within minefield range
            if(xStart>=0 && xStart<minefield.length() && yStart>=0 && yStart<minefield.length()){
                minefield.revealStartingArea(xStart, yStart);
                break;
            }
            else{
                System.out.println("Input coordinates are out of range. Try Again");
                while (true) {
                    // Get X coordinate
                    try {
                        System.out.println("Enter X coordinate:");
                        xStart = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid integer for X coordinate:");
                        scanner.next();
                    }
                }

                while (true) {
                    // Get Y coordinate
                    try {
                        System.out.println("Enter Y coordinate:");
                        yStart = scanner.nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid integer for Y coordinate:");
                        scanner.next();
                    }
                }
            }
        }


        while (!minefield.gameOver()) {
            // Displays minefield in debug mode
            if(debugMode){
                minefield.debug();
            }
            System.out.println(minefield.toString()); // Print minefield

            // Get X coordinate
            int x;
            while (true) {
                try {
                    System.out.println("Enter X coordinate:");
                    x = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid integer for X coordinate:");
                    scanner.next();
                }
            }

            // Get Y coordinate
            int y;
            while (true) {
                try {
                    System.out.println("Enter Y coordinate:");
                    y = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid integer for Y coordinate:");
                    scanner.next();
                }
            }
            // Prompts user to flag, reveal, or quit
            System.out.println("Do you want to flag (F), reveal (R) the cell? Or press (Q) to quit.");
            char action = scanner.next().toUpperCase().charAt(0);
            if (action == 'Q') {
                System.out.println("Quitting the game. Goodbye!");
                break;
            }

            boolean flag = action == 'F';

            // Checks if user hit a mine
            boolean mineHit = minefield.guess(x, y, flag);

            if (mineHit) {
                System.out.println("Game over! You hit a mine.");
                break;
            }
        }
        scanner.close();
    }
}