import java.util.ArrayList;
import java.util.Arrays;

public class SolveBoard {
	
	public static int[] dx = {1, -1, 0, 0, 1, -1, 1, -1};
	public static int[] dy = {0, 0, 1, -1, 1, -1, -1, 1};
	public static int X = 20, Y = 20;
	
	public static char[][] board;
	public static boolean[][] found;
	public static ArrayList<String> words;
	
	public static boolean findWord(char[][] wordGrid, ArrayList<String> wordList, String query){
		board = wordGrid;
		words = wordList;
		Arrays.fill(found, false);
		
		return find(query);
	}
	
	private static boolean find(String str){
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
					
					if (flag) return true;
				}
			}
		}
		return false;
	}

}

