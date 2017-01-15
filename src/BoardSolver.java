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
    public static int X = 20, Y = 20;

    public static char[][] board;
    public static boolean[][] found;
    public static ArrayList<String> words;

    public static SolverResult[] findWord(char[][] wordGrid, ArrayList<String> wordList){
        board = wordGrid;
        SolverResult[] results = new SolverResult[wordList.size()];

        for (int i = 0; i < wordList.size(); i++){
            results[i] = find(wordList.get(i));
        }

        return results;
    }

    private static SolverResult find(String str){
        System.out.printf("Finding String: %s\n", str);
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                for (int k = 0; k < 8; k++){
                    System.out.printf("i = %d, j = %d, k = %d\n", i, j, k);

                    int nX = i + dx[k] * (str.length() - 1), nY = j + dy[k] * (str.length() - 1);
                    if (nX < 0 || nX >= X || nY < 0 || nY >= Y) continue;
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