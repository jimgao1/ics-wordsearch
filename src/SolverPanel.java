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
    public File wordListFile;
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
            if (this.boardFile == null) return;
            this.lblBoardFilePath.setText(this.boardFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("choose_wordlist")) {
            this.fileChooser.showOpenDialog(this);
            this.wordListFile = fileChooser.getSelectedFile();
            if (this.wordListFile == null) return;
            this.lblWordListFile.setText(this.wordListFile.getAbsolutePath().toString());
        }  else if (e.getActionCommand().equals("choose_result")) {
            this.fileChooser.showOpenDialog(this);
            this.outputFile = fileChooser.getSelectedFile();
            if (this.outputFile == null) return;
            this.lblOutputPath.setText(this.outputFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("solve")) {
            if (this.boardFile == null || this.wordListFile == null || this.outputFile == null){
                JOptionPane.showMessageDialog(this, "You must specify the files.");
                return;
            }

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
                Scanner wordListReader = new Scanner(this.wordListFile);
                ArrayList<String> wordList = new ArrayList<>();

                while(wordListReader.hasNext()){
                    wordList.add(wordListReader.next().toUpperCase());
                }

                System.out.println("Finished word list");

                //Solve for the board
                BoardSolver.SolverResult[] results = BoardSolver.findWord(board, wordList);
                BoardSolver.writeHTML(board, results, wordList, this.outputFile);

                JOptionPane.showMessageDialog(this, "The solution has been written to the file");
            } catch (Throwable t){
                JOptionPane.showMessageDialog(this, "Error writing file: " + t.getLocalizedMessage());
            }
        }
    }
}
