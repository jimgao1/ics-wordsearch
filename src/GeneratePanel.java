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

    public GridBagConstraints constraints;

    public JLabel lblTitle;

    public JPanel pnlWordList;
    public JLabel lblWordListPath;
    public JButton btnChangeWordListPath;

    public JPanel pnlResults;
    public JLabel lblResultsPath;
    public JButton btnChangeResultPath;

    public JPanel pnlOptions;
    public JLabel lblRows;
    public JLabel lblColumns;
    public JTextField txtRows;
    public JTextField txtColumns;

    public JButton btnGenerate;

    public JFileChooser fileChooser;
    public File wordListFile;
    public File resultFile;

    public GeneratePanel() {
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

        lblTitle = new JLabel("Generate Puzzle");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        this.add(lblTitle, constraints);

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

        btnGenerate = new JButton("Generate Puzzle");
        btnGenerate.setActionCommand("generate");
        btnGenerate.addActionListener(this);
        this.add(btnGenerate, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("choose_wordlist")) {
            //Get the word list path and update label
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
            //Generate the board
            int rows = 0, cols = 0;

            try {
                rows = Integer.parseInt(txtRows.getText());
                cols = Integer.parseInt(txtColumns.getText());
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Grid dimension values are invalid");
                return;
            }

            if (this.wordListFile == null || this.resultFile == null){
                JOptionPane.showMessageDialog(this, "You must specify the files.");
                return;
            }

            //Read the words from the file
            ArrayList<String> words = new ArrayList<>();
            try {
                Scanner reader = new Scanner(this.wordListFile);
                while (reader.hasNext()) {
                    //TODO: Change to avoid getting screwed
                    words.add(reader.next().toUpperCase());
                }

                System.out.printf("%d word(s) are imported.\n", words.size());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed reading from file: " + ex.getLocalizedMessage());
            }

            char[][] board = BoardGenerator.generate(words, rows, cols);
            if (board == null){
                JOptionPane.showMessageDialog(this, "One of more words cannot be fitted onto the board");
                return;
            }

            new BoardViewWindow(words, board);

            //Print the fucking thing
            try {
                PrintWriter writer = new PrintWriter(new FileOutputStream(this.resultFile));
                for (int i = 0; i < board.length; i++) {
                    String curLine = "";
                    for (int j = 0; j < board[i].length; j++) {
                        System.out.printf("%c ", board[i][j]);
                        curLine += board[i][j];
                    }
                    System.out.println();
                    writer.println(curLine);
                }

                writer.close();

                JOptionPane.showMessageDialog(this, "The puzzle has been generated successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot write to file: " + ex.getLocalizedMessage());
            }
        }
    }
}
