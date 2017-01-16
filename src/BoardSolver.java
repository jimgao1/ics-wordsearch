import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class BoardSolver {

    public static class SolverResult{
        public int locationX;
        public int locationY;
        public int direction;

        public SolverResult(int x, int y, int dir){
            this.locationX = x;
            this.locationY = y;
            this.direction = dir;
        }
    }

    public static int[] dx = {1, -1, 0, 0, 1, -1, 1, -1};
    public static int[] dy = {0, 0, 1, -1, 1, -1, -1, 1};

    public static char[][] board;

    public static SolverResult[] findWord(char[][] wordGrid, ArrayList<String> wordList){
        board = wordGrid;
        SolverResult[] results = new SolverResult[wordList.size()];

        for (int i = 0; i < wordList.size(); i++){
            results[i] = find(wordList.get(i));
        }

        return results;
    }

    public static void writeHTML(char[][] board, SolverResult[] results,
                                 ArrayList<String> wordList, File outputFile) throws Exception{

        int[][] highlight = new int[board.length][board[0].length];

        for (int i = 0; i < wordList.size(); i++){
            if (results[i] == null) continue;
            highlight[results[i].locationX][results[i].locationY] = 1;
            for (int j = 1; j < wordList.get(i).length(); j++){
                int nx = results[i].locationX + j * BoardSolver.dx[results[i].direction];
                int ny = results[i].locationY + j * BoardSolver.dy[results[i].direction];

                if (highlight[nx][ny] == 0) highlight[nx][ny] = 2;
            }
        }

        //Write the result to HTML
        PrintWriter resultWriter = new PrintWriter(new FileWriter(outputFile));

        resultWriter.println("<html>");
        resultWriter.println("<link rel=\"stylesheet\" href=\"https://arch.jimgao.tk/wordsearch/style.css\">");
        resultWriter.println("<table width='100%' border='1px'><tr>");
        //Write the board data
        resultWriter.println("<td width='60%'>");

        resultWriter.println("<table class='board'>");
        for (int i = 0; i < board.length; i++){
            resultWriter.println("<tr>");
            for (int j = 0; j < board[0].length; j++){
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
        resultWriter.flush();
        resultWriter.close();
    }

    private static SolverResult find(String str){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                for (int k = 0; k < dx.length; k++){

                    int nX = i + dx[k] * (str.length() - 1), nY = j + dy[k] * (str.length() - 1);
                    if (nX < 0 || nX >= board.length || nY < 0 || nY >= board[0].length) continue;
                    boolean flag = true;
                    for (int l = 0; l < str.length(); l++){
                        if (str.charAt(l) != board[i + l * dx[k]][j + l * dy[k]]){
                            flag = false;
                            break;
                        }
                    }

                    if (flag) return new SolverResult(i, j, k);
                }
            }
        }
        return null;
    }

}