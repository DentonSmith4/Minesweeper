// Denton Smith smi02055
// Charley Hatterman hatte064
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Minefield {
    /**
     Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m"; //0
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";   //1
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";   //F
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";   // 2
    public static final String ANSI_PURPLE = "\u001b[35m";  //3 or more
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";   //-

    /*
     * Class Variable Section
     *
     */

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     *
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    int flagMax;
    int flags = 0;
    private Cell[][] minefield;
    public Minefield(int rows, int columns, int flags){
        minefield = new Cell[rows][columns];
        flagMax = flags;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = new Cell(false,false, "0");
            }
        }
        createMines(rows, columns, flags);
        evaluateField();
    }

    /**
     * evaluateField
     *
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     *
     */
    public void evaluateField() {
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[0].length; j++) {
                if (minefield[i][j].getStatus().equals("M")) { // A mine was found
                    // The eight possible directions
                    int[] changeX = {-1, -1, -1, 0, 0, 1, 1, 1};
                    int[] changeY = {-1, 0, 1, -1, 1, -1, 0, 1};

                    // Iterate through all possible directions
                    for (int dir = 0; dir < 8; dir++) {
                        int newX = i + changeX[dir];
                        int newY = j + changeY[dir];

                        if (newX >= 0 && newX < minefield.length && newY >= 0 && newY < minefield[0].length) {
                            if (!minefield[newX][newY].getStatus().equals("M")) {
                                String status = minefield[newX][newY].getStatus();
                                int updatedStatus = Integer.parseInt(status) + 1;
                                minefield[newX][newY].setStatus(String.valueOf(updatedStatus));
                            }

                        }
                    }
                }
            }
        }
    }


    // Helper method that checks if a string represents a positive integer
//    private boolean isNonNegativeInteger(String str) {
//        return str.matches("\\d+");
//    }

    /**
     * createMines
     *
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    // may fill up the entire board with mines, unsure how to fix
    public void createMines(int x, int y, int mines) {
        Random rand = new Random();
        int totalCells = minefield.length * minefield[0].length;

        //mines = Math.min(mines, totalCells - 1);// mines do not exceed the total number of cells
        for (int i = 0; i < mines; i++) {
            int mineX, mineY;

            mineX = rand.nextInt(minefield.length);
            mineY = rand.nextInt(minefield[0].length);

            boolean isValid = mineX != x || mineY != y; // not equal to the starting point

            boolean isNotMine = !minefield[mineX][mineY].getStatus().equals("M"); //cell is not already a mine

            // If either condition is false, try again with a new random location
            while (!isValid || !isNotMine) {
                mineX = rand.nextInt(minefield.length);
                mineY = rand.nextInt(minefield[0].length);
                isValid = mineX != x || mineY != y;
                isNotMine = !minefield[mineX][mineY].getStatus().equals("M");
            }

            // Mark the cell as a mine
            minefield[mineX][mineY].setStatus("M");
        }
    }
    /**
     * guess
     *
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        // Check if the guessed cell is in bounds
        if (x >= 0 && x < minefield.length && y >= 0 && y < minefield[0].length) {
            Cell cell = minefield[x][y];

            // If flag is true, toggle the flag on the cell

            if (flag) {
                flags++;
                if(flags>flagMax){  //check if max number of flags already
                    System.out.println("You're at max number of flags, you need to reveal");
                    return false;
                }
                // Toggle the flag
                //cell.setRevealed(true);
                cell.setFlagged(true);
            } else {
                gameOver();
                // If the cell has a mine, end the game
                if (cell.getStatus().equals("M")) {
                    return true; // Mine hit
                }

                // If the cell has a zero, call the revealZeroes() method
                if (cell.getStatus().equals("0")) {
                    revealZeroes(x, y);
                }

                // Reveal the cell to the user
                cell.setRevealed(true);

            }

            return false;
        } else {
            System.out.println("Invalid cell coordinates.");
            return false;
        }
    }


    /**
     * gameOver
     *
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[0].length; j++) {
                Cell cell = minefield[i][j];
                if (cell.getStatus().equals("M") && cell.getRevealed()){ // reveals a mine
                    System.out.println("Game Over! There was a mine at (" + i + ", " + j + ")");
                    return true;
                }
                if (!cell.getStatus().equals("M") && !cell.getRevealed()){
                    return false;
                }

            }
        }
        System.out.println("Congratulations! You win!");
        return true;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack<int[]> stack = new Stack<>();

        stack.push(new int[]{x, y}); // Push the starting cell onto the stack

        while (!stack.isEmpty()) {
            int[] currentCell = stack.pop();
            int currentX = currentCell[0];
            int currentY = currentCell[1];

            // Iterate through adjacent cells
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newX = currentX + i;
                    int newY = currentY + j;

                    // Checks if the adjacent cell is in bounds
                    if (newX >= 0 && newX < minefield.length && newY >= 0 && newY < minefield[0].length) {
                        Cell adjacentCell = minefield[newX][newY];

                        // Checks if the adjacent cell has status "0" and is not revealed
                        if (adjacentCell.getStatus().equals("0") && !adjacentCell.getRevealed()) {
                            adjacentCell.setRevealed(true); // Reveals adjacent cell
                            stack.push(new int[]{newX, newY}); // Push the adjacent cell onto the stack
                        }
                    }
                }
            }
        }
    }



    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {

        Queue<int[]> queue = new LinkedList<>();

        // Enqueue the starting cell
        queue.offer(new int[]{x, y});

        while (!queue.isEmpty()) {
            int[] currentCell = queue.poll();
            int currentX = currentCell[0];
            int currentY = currentCell[1];

            // Iterate through adjacent cells
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newX = currentX + i;
                    int newY = currentY + j;

                    // Checks if the adjacent cell is in bounds
                    if (newX >= 0 && newX < minefield.length && newY >= 0 && newY < minefield[0].length) {
                        Cell adjacentCell = minefield[newX][newY];

                        // Reveals the adjacent cell if it's not a mine and it's not revealed
                        if (!adjacentCell.getStatus().equals("M") && !adjacentCell.getRevealed()) {
                            adjacentCell.setRevealed(true);

                            // If the revealed cell is 0 enqueue it
                            if (adjacentCell.getStatus().equals("0")) {
                                queue.offer(new int[]{newX, newY});
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * For both printing methods utilize the ANSI colour codes provided!
     *
     *
     *
     *
     *
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public void debug() {
        String result = "";

        for (int i = 0; i < minefield.length; i++) {
            Cell cell;
            for (int j = 0; j < minefield[0].length; j++) {
                cell = minefield[i][j];
                String status = cell.getStatus();
                // Print the cell status with corresponding ANSI color
                if(status.equals("M")) {
                    result += ANSI_RED_BRIGHT + "M ";
                } else if (status.equals("0")) {
                    result += ANSI_YELLOW + status + " ";
                } else if (status.equals("1")) {
                    result += ANSI_BLUE_BRIGHT + status + " ";
                } else if (status.equals("2")) {
                    result += ANSI_GREEN + status + " ";
                } else if (Integer.parseInt(status) >= 3) {
                    result += ANSI_PURPLE + status + " ";
                }
                // Move to the next row
            }
            result += "\n";
        }
        System.out.println(result);
    }


    public int length(){
        return minefield.length;
    }
    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        String result = "";

        for (int i = 0; i < minefield.length; i++) {
            Cell cell;
            for (int j = 0; j < minefield[0].length; j++) {
                cell = minefield[i][j];
                if (cell.getRevealed()) {
                    String status = cell.getStatus();
                    // Print the cell status with corresponding ANSI color
                    if (status.equals("0")) {
                        result+=ANSI_YELLOW+ status + " ";
                    } else if(status.equals("1")){
                        result+=ANSI_BLUE_BRIGHT + status + " ";
                    }else if(status.equals("2")){
                        result+=ANSI_GREEN + status + " ";
                    } else if(Integer.parseInt(status)>=3){
                        result+=ANSI_PURPLE + status + " ";
                    }
                    // If the cell is revealed or flagged, append its status
                }
                else if (cell.isFlagged()) {
                    result +=ANSI_RED_BRIGHT + "F ";

                } else {
                    // If the cell is not revealed or flagged, append a placeholder
                    result += ANSI_RESET+"- ";
                }
            }
            result+="\n"; // Move to the next row
        }

        return result;
    }
}