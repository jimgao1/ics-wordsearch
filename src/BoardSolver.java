/*
    [BoardSolver.java]

    Date: Jan 16th, 2016
    Author: Jim Gao, Steven Ye, Tianqi Huang
    Purpose: The class that provides utilities to solve board and output HTML containing the solution
 */

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BoardSolver {
    /*
        The SolverResult class which contains 3 pieces of information:
            - The X, Y coordinates where the word is found, and
            - The direction that the word is facing
     */
    public static class SolverResult{
        //Fields / state of the class
        public int locationX;
        public int locationY;
        public int direction;

        //A constructor for easy initialization of the fields
        public SolverResult(int x, int y, int dir){
            this.locationX = x;
            this.locationY = y;
            this.direction = dir;
        }
    }

    /*
        Below are the vectors of the 8 potential directions where the words can be placed in.

        Each time, if the current location is (x, y), we can simply obtain the next position
        by adding the current coordinates with that of the vectors.

        For example, when going up, we can add (0, -1) to the current location to make (x, y - 1).
     */
    public static int[] dx = {1, -1, 0, 0, 1, -1, 1, -1};
    public static int[] dy = {0, 0, 1, -1, 1, -1, -1, 1};

    //The 2-dimensional array containing the board
    public static char[][] board;

    /*
        The method called by other components of the program to call to solve for a puzzle.

        This method takes 2 parameters: the 2-dimensional grid containing the board, and
        an ArrayList containing the word list
     */
    public static SolverResult[] findWord(char[][] wordGrid, ArrayList<String> wordList){
        board = wordGrid;

        /*
            Array containing the results where the i'th result corresponds to the i'th word
         */
        SolverResult[] results = new SolverResult[wordList.size()];

        //Iterate through the list of words and try to solve for each one
        for (int i = 0; i < wordList.size(); i++){
            //Solving for the i'th word
            results[i] = find(wordList.get(i));
        }

        return results;
    }

    /*
        The method to find a single word in the grid

        It returns a single SolverResult if the word is found, containing the location
        as well as the direction where the word is found.

        Otherwise, it returns null.
     */
    private static SolverResult find(String str){
        /*
            i: the x-value of the current starting position
            j: the y-value of the current starting position
            k: the direction for the current iteration
         */

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                for (int k = 0; k < dx.length; k++){
                    //Check if the end of the word goes out of bounds
                    int nX = i + dx[k] * (str.length() - 1), nY = j + dy[k] * (str.length() - 1);
                    if (nX < 0 || nX >= board.length || nY < 0 || nY >= board[0].length) continue;

                    boolean flag = true;
                    for (int l = 0; l < str.length(); l++){
                        //If the letter at the current position does not match
                        //that of the word, then break
                        if (str.charAt(l) != board[i + l * dx[k]][j + l * dy[k]]){
                            flag = false;
                            break;
                        }
                    }

                    //If the word is found successfully, then return the result
                    if (flag) return new SolverResult(i, j, k);
                }
            }
        }

        //If it is not found, then return null
        return null;
    }

    /*
        Method to output the solutions to a HTML file. This method takes a number of arguments:
            - board: a 2-dimensional array containing the game board
            - results: an array with the solved positions of the words
            - wordList: the list of words
            - outputFile: the file where the HTML page will be written to

        This method does not return anything, but it would throw exceptions if exceptions are
        thrown during its execution.
     */
    public static void writeHTML(char[][] board, SolverResult[] results,
                                 ArrayList<String> wordList, File outputFile) throws Exception{

        /*I
            A 2-dimensional array containing the information of whether a cell should be highlighted
            as well as how it should be highlighted.

            The first letter of the word will be highlighted with a red background, and the rest of the
            word will have a yellow background.
         */
        int[][] highlight = new int[board.length][board[0].length];

        for (int i = 0; i < wordList.size(); i++){
            if (results[i] != null) {
                //Highlight the first letter with .firstletter
                highlight[results[i].locationX][results[i].locationY] = 1;
                for (int j = 1; j < wordList.get(i).length(); j++) {
                    //Calculate the positions of the letters for the rest of the word
                    int nx = results[i].locationX + j * BoardSolver.dx[results[i].direction];
                    int ny = results[i].locationY + j * BoardSolver.dy[results[i].direction];

                    //Color them with .letter
                    if (highlight[nx][ny] == 0) highlight[nx][ny] = 2;
                }
            }
        }

        /*
            Writing the data to a HTML file contain the grid, the solution and the list of words.

            A sample of the page can be found here:
                            https://arch.jimgao.tk/wordsearch/sample.html]

            The HTML will contain 2 main parts: the grid and the word list.

            The grid will be placed on the left and occupying 60% of the page. The word list will take up
            the rest 40% of space. This is accomplished by using a table with weighted cells.
         */
        PrintWriter resultWriter = new PrintWriter(new FileWriter(outputFile));

        resultWriter.println("<html>");
        //Link to the style sheet that defines the colors of the highlights as well as the font used
        resultWriter.println("<link rel=\"stylesheet\" href=\"https://arch.jimgao.tk/wordsearch/style.css\">");

        //Construct the outer-frame of the page
        resultWriter.println("<table width='100%' border='1px'><tr>");

        //Write the board data
        resultWriter.println("<td width='60%'>");

        resultWriter.println("<table class='board'>");
        /*
            Printed below is the main game board, with the class .board.

            Each cell has a size of 20x20 pixels, with a padding of 10px.
         */
        for (int i = 0; i < board.length; i++){
            resultWriter.println("<tr>");
            for (int j = 0; j < board[0].length; j++){
                /*
                    For each word cell, the class is used to determine how the grid
                    will be colored.

                    According to the CSS, the class ".firstletter" will color the grid red
                    while the class ".letter" will color the grid yellow. If no class is applied,
                    then the cell will not be colored.

                    In order to determine how the cell should be colored, we can simply refer
                    to the "highlight" array.
                 */

                if (highlight[i][j] == 0){
                    resultWriter.print("<td>");
                } else if (highlight[i][j] == 1){
                    resultWriter.print("<td class='firstletter'>");
                } else {
                    resultWriter.print("<td class='letter'>");
                }

                resultWriter.print(board[i][j]);
                resultWriter.println("</td>");
            }

            resultWriter.println("</tr>");
        }
        resultWriter.println("</table>");
        resultWriter.println("</td>");


        //Write the word list
        resultWriter.println("<td width='40%' >");
        resultWriter.println("<h2>Word List</h2>");
        resultWriter.println("<ul>");
        for (int i = 0; i < wordList.size(); i++){
            if (results[i] == null){
                resultWriter.println("<li><del>" + wordList.get(i) + "</del></li>");
            } else {
                resultWriter.println("<li>" + wordList.get(i) + "</li>");
            }
        }

        resultWriter.println("</ul>");
        resultWriter.println("</td>");
        resultWriter.println("</tr>");

        resultWriter.println("</html>");

        //Close and finish writing the file
        resultWriter.flush();
        resultWriter.close();
    }


}