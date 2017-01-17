/*
    [GeneratePanel.java]

    Date: Jan 16th, 2016
    Author: Jim Gao, Steven Ye, Tianqi Huang
    Purpose: The GUI Panel that handles the creation of grids
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneratePanel extends JPanel implements ActionListener {

    //Constraints for the main window to align the elements
    public GridBagConstraints constraints;

    public JLabel lblTitle;

    //Components for word list file selection
    public JPanel pnlWordList;
    public JLabel lblWordListPath;
    public JButton btnChangeWordListPath;

    //Components for output file selection
    public JPanel pnlResults;
    public JLabel lblResultsPath;
    public JButton btnChangeResultPath;

    //Components for entering the rows and columns
    public JPanel pnlOptions;
    public JLabel lblRows;
    public JLabel lblColumns;
    public JTextField txtRows;
    public JTextField txtColumns;

    //Button to generate the grid
    public JButton btnGenerate;

    //Definition of file paths
    public JFileChooser fileChooser;
    public File wordListFile;
    public File resultFile;

    public GeneratePanel() {
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
        lblTitle = new JLabel("Generate Puzzle");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(lblTitle, constraints);

        //Add the word list file selection
        pnlWordList = new JPanel();
        pnlWordList.setBorder(BorderFactory.createTitledBorder("Word List File"));
        pnlWordList.setLayout(new BorderLayout());
        lblWordListPath = new JLabel("Not Selected");
        pnlWordList.add(lblWordListPath, BorderLayout.CENTER);
        btnChangeWordListPath = new JButton("Choose File");
        btnChangeWordListPath.setActionCommand("choose_wordlist");
        btnChangeWordListPath.addActionListener(this);
        pnlWordList.add(btnChangeWordListPath, BorderLayout.EAST);
        this.add(pnlWordList, constraints);

        //Add the result file selection
        pnlResults = new JPanel();
        pnlResults.setBorder(BorderFactory.createTitledBorder("Output File"));
        pnlResults.setLayout(new BorderLayout());
        lblResultsPath = new JLabel("Not Selected");
        pnlResults.add(lblResultsPath, BorderLayout.CENTER);
        btnChangeResultPath = new JButton("Choose File");
        btnChangeResultPath.setActionCommand("choose_result");
        btnChangeResultPath.addActionListener(this);
        pnlResults.add(btnChangeResultPath, BorderLayout.EAST);
        this.add(pnlResults, constraints);

        //Add the options panel where rows and columns are entered
        pnlOptions = new JPanel();
        pnlOptions.setBorder(BorderFactory.createTitledBorder("Dimensions"));
        pnlOptions.setLayout(new FlowLayout());
        lblRows = new JLabel("Rows: ");
        lblColumns = new JLabel("Cols: ");
        txtRows = new JTextField("15");
        txtRows.setPreferredSize(new Dimension(40, 25));
        txtColumns = new JTextField("15");
        txtColumns.setPreferredSize(new Dimension(40, 25));
        pnlOptions.add(lblRows);
        pnlOptions.add(txtRows);
        pnlOptions.add(lblColumns);
        pnlOptions.add(txtColumns);
        this.add(pnlOptions, constraints);

        //Add the button to generate the puzzle
        btnGenerate = new JButton("Generate Puzzle");
        btnGenerate.setActionCommand("generate");
        btnGenerate.addActionListener(this);
        this.add(btnGenerate, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("choose_wordlist")) {
            this.fileChooser.showOpenDialog(this);
            this.wordListFile = fileChooser.getSelectedFile();
            if (this.wordListFile == null) return;
            this.lblWordListPath.setText(this.wordListFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("choose_result")) {
            this.fileChooser.showOpenDialog(this);
            this.resultFile = fileChooser.getSelectedFile();
            if (this.resultFile == null) return;
            this.lblResultsPath.setText(this.resultFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("generate")) {
            /*
                First of all, the rows and columns are checked for being valid integers
                as well as being in the valid range of [10, 20].

                If any problems occur, prompt the user to reenter those values.
             */
            int rows = 0, cols = 0;

            try {
                rows = Integer.parseInt(txtRows.getText());
                cols = Integer.parseInt(txtColumns.getText());

                if (rows < 10 || rows > 20 || cols < 10 || cols > 20){
                    JOptionPane.showMessageDialog(this, "Grid dimensions must be within [10, 20]");
                    return;
                }
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Grid dimension values are invalid");
                return;
            }

            if (this.wordListFile == null || this.resultFile == null){
                JOptionPane.showMessageDialog(this, "You must specify the files.");
                return;
            }

            /*
                Open the wordlist file, and read all the lines from it.

                After reading each line, the word is converted into upper-case
                for easier searching and processing.

                If the file cannot be read, prompt the user with an error.
             */
            ArrayList<String> words = new ArrayList<>();
            try {
                Scanner reader = new Scanner(this.wordListFile);
                while (reader.hasNext()) {
                    words.add(reader.next().toUpperCase());
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed reading from file: " + ex.getLocalizedMessage());
            }

            /*
                Calls the "generate" function in the BoardGenerator class. The wordlist, as well
                as the number of rows and columns are provided as parameters.

                If the return from the method call is null, it means that no valid grid can be
                generated with the given dimensions and words. In this case, the user is prompted
                with the problem.
             */
            char[][] board = BoardGenerator.generate(words, rows, cols);
            if (board == null){
                JOptionPane.showMessageDialog(this, "One of more words cannot be fitted onto the board");
                return;
            }

            /*
                Open another window containing the board to show the user
             */
            new BoardViewWindow(words, board);

            try {
                //Initialize the PrintWriter that writes the finished board
                PrintWriter boardWriter = new PrintWriter(new FileOutputStream(this.resultFile));

                //Initialize the PrintWriter that writes the board solution
                File solutionFile = new File(this.resultFile.getAbsolutePath().replaceAll(".txt", "_solution.txt"));
                PrintWriter solutionWriter = new PrintWriter(new FileOutputStream(solutionFile));

                for (int i = 0; i < board.length; i++) {
                    //Temporary variables to store the current line to write
                    String boardLine = "";
                    String solutionLine = "";

                    for (int j = 0; j < board[i].length; j++) {
                        //Add current character to the solution line
                        if (board[i][j] != '\0') {
                            solutionLine += board[i][j];
                        } else {
                            solutionLine += " ";
                        }

                        //Add the current character into the board file, and a random letter if its not defined.
                        if (board[i][j] == '\0'){
                            board[i][j] = (char)('A' + (int)(Math.random() * 26));
                        }
                        boardLine += board[i][j];
                    }
                    System.out.println();
                    boardWriter.println(boardLine);
                    solutionWriter.println(solutionLine);
                }

                boardWriter.close();
                solutionWriter.close();

                JOptionPane.showMessageDialog(this, "The puzzle has been generated successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot write to file: " + ex.getLocalizedMessage());
            }
        }
    }
}
