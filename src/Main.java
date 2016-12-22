import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		//I made some changes
		ArrayList<String> words = new ArrayList<>();
		words.add("sample_word");
		
		//To Generate Board
		char[][] board = GenerateBoard.generate(words);
		
		//To Solve Board
		SolveBoard.SolverResult[] results = SolveBoard.findWord(board, words);
		
		//The i'th index in the results refers to the result of the i'th word in the word list
		//The element at index i is null if that word cannot be found in the grid
		
		/*
		 * 	Each of the results contain 3 pieces of information:
		 * 		- The X coord of the first letter
		 * 		- The Y coord of the first letter
		 * 		- The direction of the word (from 0 to 7)
		 * 
		 * 	For example, below is the result for the first word
		 */
		
		if (results[0] != null){
			int X = results[0].locationX;
			int Y = results[0].locationY;
			int direction = results[0].direction;
			
			//For more information on direction, refer to SolveBoard.java
		} else {
			//The word is not found
		}
	}
}
