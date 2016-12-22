import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		ArrayList<String> words = new ArrayList<>();
		words.add("sample_word");
		
		//To Generate Board
		char[][] board = GenerateBoard.generate(words);
		
		//To Solve Board
		boolean found = SolveBoard.findWord(board, words, "string_query");
		//returns true if found, and false otherwise
	}
}
