import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SolverPanel extends JPanel implements ActionListener {

    public GridBagConstraints constraints;

    public JLabel lblTitle;

    public JPanel pnlBoardFile;
    public JLabel lblBoardFilePath;
    public JButton btnChangeBoardFilePath;

    public JPanel pnlWordListFile;
    public JLabel lblWordListFile;
    public JButton btnChangeWordListFilePath;

    public JPanel pnlOutput;
    public JLabel lblOutputPath;
    public JButton btnChangeOutputPath;

    public JButton btnGenerate;

    public JFileChooser fileChooser;
    public File boardFile;
    public File wordlistFile;
    public File outputFile;

    public SolverPanel() {
        fileChooser = new JFileChooser();

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipady = 10;
        constraints.gridx = 0;

        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setAlignmentY(Component.TOP_ALIGNMENT);

        lblTitle = new JLabel("Solve Puzzle");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(lblTitle, constraints);

        pnlBoardFile = new JPanel();
        pnlBoardFile.setBorder(BorderFactory.createTitledBorder("Board File"));
        pnlBoardFile.setLayout(new BorderLayout());
        lblBoardFilePath = new JLabel("Not Selected");
        pnlBoardFile.add(lblBoardFilePath, BorderLayout.CENTER);
        btnChangeBoardFilePath = new JButton("Choose File");
        btnChangeBoardFilePath.setActionCommand("choose_board");
        btnChangeBoardFilePath.addActionListener(this);
        pnlBoardFile.add(btnChangeBoardFilePath, BorderLayout.EAST);
        this.add(pnlBoardFile, constraints);

        pnlWordListFile = new JPanel();
        pnlWordListFile.setBorder(BorderFactory.createTitledBorder("Word List File"));
        pnlWordListFile.setLayout(new BorderLayout());
        lblWordListFile = new JLabel("Not Selected");
        pnlWordListFile.add(lblWordListFile, BorderLayout.CENTER);
        btnChangeWordListFilePath = new JButton("Choose File");
        btnChangeWordListFilePath.setActionCommand("choose_wordlist");
        btnChangeWordListFilePath.addActionListener(this);
        pnlWordListFile.add(btnChangeWordListFilePath, BorderLayout.EAST);
        this.add(pnlWordListFile, constraints);

        pnlOutput = new JPanel();
        pnlOutput.setBorder(BorderFactory.createTitledBorder("Output File"));
        pnlOutput.setLayout(new BorderLayout());
        lblOutputPath = new JLabel("Not Selected");
        pnlOutput.add(lblOutputPath, BorderLayout.CENTER);
        btnChangeOutputPath = new JButton("Choose File");
        btnChangeOutputPath.setActionCommand("choose_result");
        btnChangeOutputPath.addActionListener(this);
        pnlOutput.add(btnChangeOutputPath, BorderLayout.EAST);
        this.add(pnlOutput, constraints);

        btnGenerate = new JButton("Solve Puzzle");
        btnGenerate.setActionCommand("solve");
        btnGenerate.addActionListener(this);
        this.add(btnGenerate, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("choose_board")) {
            this.fileChooser.showOpenDialog(this);
            this.boardFile = fileChooser.getSelectedFile();
            this.lblBoardFilePath.setText(this.boardFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("choose_wordlist")) {
            this.fileChooser.showOpenDialog(this);
            this.wordlistFile = fileChooser.getSelectedFile();
            this.lblWordListFile.setText(this.wordlistFile.getAbsolutePath().toString());
        }  else if (e.getActionCommand().equals("choose_result")) {
            this.fileChooser.showOpenDialog(this);
            this.outputFile = fileChooser.getSelectedFile();
            this.lblOutputPath.setText(this.outputFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("solve")) {

            try {
                //Read the board content
                Scanner boardReader = new Scanner(this.boardFile);
                ArrayList<String> lines = new ArrayList<>();

                while(boardReader.hasNext()){
                    lines.add(boardReader.nextLine());
                }

                char[][] board = new char[lines.size()][lines.get(0).length()];
                for (int i = 0; i < lines.size(); i++){
                    for (int j = 0; j < lines.get(i).length(); j++){
                        board[i][j] = lines.get(i).charAt(j);
                    }
                }

                System.out.println("Finished reading board");

                //Read the wordlist content
                Scanner wordlistReader = new Scanner(this.wordlistFile);
                ArrayList<String> wordlist = new ArrayList<>();

                while(wordlistReader.hasNext()){
                    wordlist.add(wordlistReader.next().toUpperCase());
                }

                System.out.println("Finished word list");

                //Solve for the board
                BoardSolver.SolverResult[] results = BoardSolver.findWord(board, wordlist);
                int[][] highlight = new int[board.length][board[0].length];

                for (int i = 0; i < wordlist.size(); i++){
                    highlight[results[i].locationX][results[i].locationY] = 1;
                    for (int j = 1; j < wordlist.get(i).length(); j++){
                        int nx = results[i].locationX + j * BoardSolver.dx[results[i].direction];
                        int ny = results[i].locationY + j * BoardSolver.dy[results[i].direction];

                        if (highlight[nx][ny] == 0) highlight[nx][ny] = 2;
                    }
                }

                //Write the result to HTML
                PrintWriter resultWriter = new PrintWriter(new FileWriter(this.outputFile));

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
                for (String word : wordlist){
                    resultWriter.printf("<li>%s</li>\r\n", word);
                }
                resultWriter.println("</ul>");
                resultWriter.println("</td>");
                resultWriter.println("</tr>");

                resultWriter.println("</html>");
                resultWriter.flush();
                resultWriter.close();

                JOptionPane.showMessageDialog(null, "This shit has been written to file");
            } catch (Throwable t){

            }
        }
    }
}
