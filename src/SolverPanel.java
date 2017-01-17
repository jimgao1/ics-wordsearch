/*
    [SolverPanel.java]

    Date: Jan 16th, 2016
    Author: Jim Gao, Steven Ye, Tianqi Huang
    Purpose: The GUI panel that handles solving boards
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SolverPanel extends JPanel implements ActionListener {

    //Constraints for the main window to align the elements
    public GridBagConstraints constraints;

    public JLabel lblTitle;

    //Components for board file selection
    public JPanel pnlBoardFile;
    public JLabel lblBoardFilePath;
    public JButton btnChangeBoardFilePath;

    //Components for word list file selection
    public JPanel pnlWordListFile;
    public JLabel lblWordListFile;
    public JButton btnChangeWordListFilePath;

    //Components for output file selection
    public JPanel pnlOutput;
    public JLabel lblOutputPath;
    public JButton btnChangeOutputPath;

    //Button to generate the puzzle
    public JButton btnSolve;

    //Definition of file paths
    public JFileChooser fileChooser;
    public File boardFile;
    public File wordListFile;
    public File outputFile;

    public SolverPanel() {
        fileChooser = new JFileChooser();
        /*
            The constraint for the grid is initialized. The weight of each file selection boxes
            are defined such that the screen is divided into 3 parts.

            In addition, padding has been added to each of the cells to make it more visually
            pleasing.
         */
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

        //Add the title
        lblTitle = new JLabel("Solve Puzzle");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(lblTitle, constraints);

        //Add the board file selection
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

        //Add the word list file selection
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

        //Add the output file selection
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

        //Add the solve button
        btnSolve = new JButton("Solve Puzzle");
        btnSolve.setActionCommand("solve");
        btnSolve.addActionListener(this);
        this.add(btnSolve, constraints);
    }

    /*
        This method is responsible for handling the GUI events, including all the button clicks.

        Each button has a action command, which will be read in the method below and the corresponding
        action will take place.
     */
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
                //Record the starting time for benchmarking
                long startingTime = System.currentTimeMillis();

                //Read the board content
                Scanner boardReader = new Scanner(this.boardFile);
                ArrayList<String> lines = new ArrayList<>();

                //Read all the lines in the file to obtain the number of rows
                while(boardReader.hasNext()){
                    lines.add(boardReader.nextLine());
                }

                //Creates the 2-dimensional array containing the actual board
                char[][] board = new char[lines.size()][lines.get(0).length()];
                for (int i = 0; i < lines.size(); i++){
                    for (int j = 0; j < lines.get(i).length(); j++){
                        board[i][j] = lines.get(i).charAt(j);
                    }
                }

                //Read the wordlist content
                Scanner wordListReader = new Scanner(this.wordListFile);
                ArrayList<String> wordList = new ArrayList<>();

                //Reads the content of the wordlist file and converts each
                //word into uppercase for easy matching
                while(wordListReader.hasNext()){
                    wordList.add(wordListReader.next().toUpperCase());
                }

                //Solve for the board
                BoardSolver.SolverResult[] results = BoardSolver.findWord(board, wordList);

                //Writes the HTML to the file
                BoardSolver.writeHTML(board, results, wordList, this.outputFile);

                //Finally, output the success message as well as the running time
                JOptionPane.showMessageDialog(this, String.format("The puzzle was solved in %d milliseconds.",
                        (System.currentTimeMillis() - startingTime)));
            } catch (Exception ex){
                //If something fails, output a error message with the exception message
                JOptionPane.showMessageDialog(this, "Error writing file: " + ex.getLocalizedMessage());
            }
        }
    }
}
