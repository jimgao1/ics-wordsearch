import java.util.ArrayList;

public class BoardGenerator {

    /*
        Below are the vectors of the 8 potential directions where the words can be placed in.

        Each time, if the current location is (x, y), we can simply obtain the next position
        by adding the current coordinates with that of the vectors.

        For example, when going up, we can add (0, -1) to the current location to make (x, y - 1).
     */
    public static int[] dx = {1, -1, 0, 0, 1, -1, 1, -1};
    public static int[] dy = {0, 0, 1, -1, 1, -1, -1, 1};

    /*
        Dimensions of the board: the number of rows and columns
     */
    public static int boardWidth, boardHeight;

    /*
        The list of the words in the wordlist, used when placing the words onto the grid
     */
    public static ArrayList<String> words;

    /*
        The 2-dimensional array containing the word grid
     */
    public static char[][] board;

    /*
        The public method used in other parts of the software to generate the board.
        The method requires the wordlist as well as the number of rows and columns of the grid
     */
    public static char[][] generate(ArrayList<String> wordList, int R, int C) {
        //Assign the rows and columns to the field in the class
        boardWidth = R;
        boardHeight = C;

        //Instantiate and initialize the board and wordlist
        words = (ArrayList<String>) wordList.clone();
        board = new char[boardWidth][boardHeight];

        //Verify that the argument is valid
        if (wordList == null)
            throw new IllegalArgumentException("Argument is null");

        //Calls the overloaded generate method to generate the grid
        if (generate(0)) {
            return board;
        }

        //If it fails, then return null
        return null;
    }

    /*
        Overloaded recursive generate method used to generate the grid
     */
    private static boolean generate(int curIndex) {
        /*
            Terminating condition: when all the words have been generated successfully.

            The board is iterated through, and all the grids containing null ('\0') is
            replaced with a random letter.
         */
        if (curIndex == words.size()) {
            for (int i = 0; i < boardWidth; i++) {
                for (int j = 0; j < boardHeight; j++) {
                    //If the current cell has not been assigned, then place a random letter
                    if (board[i][j] == '\0') {
                        board[i][j] = (char) ('A' + (int) (Math.random() * 26));
                    }
                }
            }
            return true;
        }

        /*
            Boolean array to store whether the current configuration has been tried before
            in order to avoid recomputation.

            In addition, when the grid is completely filled,
            it indicates that every possible configuration of the current word has been tried,
            and it is impossible to place the word anywhere on the grid
         */
        boolean[][][] positionsTested = new boolean[boardWidth][boardHeight][8];
        while (true) {
            /*
                Generates the current configuration, including the location and direction
                of the current word.

                Since there is a higher probability of a successful placement
             */

            int direction = (int) (Math.random() * 4) + (Math.random() > 0.4 ? 0 : 4);
            int locX = (int) (Math.random() * boardWidth);
            int locY = (int) (Math.random() * boardHeight);

            //Checks if every combination of location and direction has been tried before
            boolean allTested = true;
            for (int i = 0; i < boardWidth; i++) {
                for (int j = 0; j < boardHeight; j++) {
                    for (int k = 0; k < dx.length; k++) {
                        //If the current position has not been tested, then not all configurations
                        //are tested.
                        if (!positionsTested[i][j][k]) {
                            allTested = false;
                        }
                    }
                }
            }

            //If everything is tested, then this placement fails
            if (allTested) return false;

            //If the current configuration has been tested, generate another set
            if (positionsTested[locX][locY][direction]) continue;
            positionsTested[locX][locY][direction] = true;

            /*
                Calculate the position of the ending of the words by multiplying
                the direction vectors with a scalar, and adding that with the
                current position.

                If the resulting position lies outside the board, then there is no
                need to try the current configuration since there is no way that
                the word can be placed.
             */
            String curWord = words.get(curIndex);
            int endX = locX + dx[direction] * curWord.length();
            int endY = locY + dy[direction] * curWord.length();

            if (endX < 0 || endX >= boardWidth || endY < 0 || endY >= boardHeight) continue;

            /*
                Checks if there is any word in the place of the current word that is
                to be placed.

                The updated array is used to keep track of which cell has been modified
                in order to revert should the generation fail.
             */
            boolean[][] updated = new boolean[boardWidth][boardHeight];
            boolean valid = true;
            for (int i = 0; i < curWord.length(); i++) {
                int curX = locX + dx[direction] * i;
                int curY = locY + dy[direction] * i;
                if (board[curX][curY] == '\0' || board[curX][curY] == curWord.charAt(i)) {
                    if (board[curX][curY] == '\0') updated[curX][curY] = true;
                    board[curX][curY] = curWord.charAt(i);
                } else {
                    valid = false;
                    break;
                }
            }

            /*
                If the current placement is valid, then recursively try to place the next word
             */
            if (valid && generate(curIndex + 1)) return true;

            /*
                If the generation fails, roll back the changes and start over
             */
            for (int i = 0; i < boardWidth; i++) {
                for (int j = 0; j < boardHeight; j++) {
                    if (updated[i][j])
                        board[i][j] = '\0';
                }
            }
        }
    }
}