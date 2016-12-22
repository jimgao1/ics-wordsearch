import java.util.ArrayList;
import java.util.Scanner;

public class GenerateBoard {
	
	public static int[] dx = {1, -1, 0, 0, 1, -1, 1, -1};
	public static int[] dy = {0, 0, 1, -1, 1, -1, -1, 1};
	
	public static int X = 20, Y = 20;

	public static ArrayList<String> words;
	public static char[][] board;
	
	public static char[][] generate(ArrayList<String> wordList){
		for (String str : wordList) words.add(str);
		if (generate(0)){
			return board;
		} 
		return null;
	}
	
	private static boolean generate(int curIndex){
		System.out.printf("curIndex = %d\n", curIndex);
		
		if (curIndex == words.size()) {
			for (int i = 0; i < X; i++){
				for (int j = 0; j < Y; j++){
					if (board[i][j] == '\0'){
						board[i][j] = (char)('a' + (int)(Math.random() * 26));
					}
				}
			}
			return true;
		}
		
		for (;;){
			//Generate random shit
			int direction = (int)(Math.random() * 4) + (Math.random() > 0.8 ? 0 : 4);
			int locX = (int)(Math.random() * X);
			int locY = (int)(Math.random() * Y);
			
			//Check out of bounds
			String curWord = words.get(curIndex);
			int endX = locX + dx[direction] * curWord.length();
			int endY = locY + dy[direction] * curWord.length();
			
			if (endX < 0 || endX >= X || endY < 0 || endY >= Y) continue;
			
			//Check for collisions
			boolean[][] updated = new boolean[X][Y];
			boolean valid = true;
			for (int i = 0; i < curWord.length(); i++){
				int curX = locX + dx[direction] * i;
				int curY = locY + dy[direction] * i;
				if (board[curX][curY] == '\0' || board[curX][curY] == curWord.charAt(i)){
					if (board[curX][curY] == '\0') updated[curX][curY] = true;
					board[curX][curY] = curWord.charAt(i);
				} else {
					valid = false;
					break;
				}
			}
			
			//Try it
			if (valid && generate(curIndex + 1)) return true;
			
			//If fails, then erase changes and start over
			for (int i = 0; i < X; i++){
				for (int j = 0; j < Y; j++){
					if (updated[i][j])
						board[i][j] = '\0';
				}
			}
		}
	}
}
