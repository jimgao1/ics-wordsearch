import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardViewWindow extends JFrame {

    public JPanel pnlWordList;
    public JList lstWordList;
    public DefaultListModel<String> wordListModel;

    public JPanel pnlBoard;
    public JLabel[][] letterGrid;

    public BoardViewWindow(ArrayList<String> words, char[][] board){
        this.setSize(800, 600);
        this.setLocation(200, 200);
        this.setResizable(false);
        this.setTitle("Board View");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //Create the word list section
        pnlWordList = new JPanel();
        pnlWordList.setLayout(new GridLayout(1, 1));
        pnlWordList.setPreferredSize(new Dimension(150, 600));
        pnlWordList.setBorder(BorderFactory.createTitledBorder("Word List"));
        wordListModel = new DefaultListModel<>();
        lstWordList = new JList(wordListModel);
        lstWordList.setFont(new Font("Consolas", Font.PLAIN, 14));
        pnlWordList.add(lstWordList);

        for (String curWord : words){
            wordListModel.addElement(curWord);
        }

        this.add(pnlWordList, BorderLayout.WEST);

        //Create the board section
        pnlBoard = new JPanel();
        pnlBoard.setLayout(new GridLayout(board.length, board[0].length));
        pnlBoard.setBorder(BorderFactory.createTitledBorder("Game Board"));

        letterGrid = new JLabel[board.length][board[0].length];
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                letterGrid[i][j] = new JLabel();
                letterGrid[i][j].setFont(new Font("Consolas", Font.BOLD, 15));
                letterGrid[i][j].setText(Character.toString(board[i][j]));

                pnlBoard.add(letterGrid[i][j]);
            }
        }

        this.add(pnlBoard, BorderLayout.CENTER);

        this.setVisible(true);
        this.repaint();
        this.revalidate();
    }
}
