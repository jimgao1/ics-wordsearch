import java.util.ArrayList;
import java.util.Arrays;

public class SolveBoard {
	
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
		for (int i = 0; i < X; i++){
			for (int j = 0; j < Y; j++){
				for (int k = 0; k < 8; k++){
					int nX = i + dx[k] * (str.length() - 1), nY = j + dy[k] * (str.length() - 1);
					if (nX < 0 || nX >= X || nY < 0 || nY >= Y) continue;
					boolean flag = true;
					for (int l = 0; l < str.length(); l++){
						if (str.charAt(l) != board[i + l * dx[i]][j + l * dy[i]]){
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

