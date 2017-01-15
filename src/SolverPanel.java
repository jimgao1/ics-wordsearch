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

    public JPanel pnlOutput;
    public JLabel lblOutputPath;
    public JButton btnChangeOutputPath;

    public JButton btnGenerate;

    public JFileChooser fileChooser;
    public File boardFile;
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
        pnlBoardFile.setBorder(BorderFactory.createTitledBorder("Word List File"));
        pnlBoardFile.setLayout(new BorderLayout());
        lblBoardFilePath = new JLabel("Not Selected");
        pnlBoardFile.add(lblBoardFilePath, BorderLayout.CENTER);
        btnChangeBoardFilePath = new JButton("Choose File");
        btnChangeBoardFilePath.setActionCommand("choose_wordlist");
        btnChangeBoardFilePath.addActionListener(this);
        pnlBoardFile.add(btnChangeBoardFilePath, BorderLayout.EAST);
        this.add(pnlBoardFile, constraints);

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
            this.boardFile = fileChooser.getSelectedFile();
            this.lblBoardFilePath.setText(this.boardFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("choose_result")) {
            this.fileChooser.showOpenDialog(this);
            this.outputFile = fileChooser.getSelectedFile();
            this.lblOutputPath.setText(this.outputFile.getAbsolutePath().toString());
        } else if (e.getActionCommand().equals("generate")) {

            try {
                Scanner boardReader = new Scanner(this.boardFile);
                ArrayList<String> lines = new ArrayList<>();

                while(boardReader.hasNext()){
                    lines.add(boardReader.nextLine());
                }

                char[][] board = new char[lines.size()][lines.get(0).split(" ").length];
                for (int i = 0; i < lines.size(); i++){
                    String[] items = lines.get(i).split(" ");
                    for (int j = 0; j < items.length; j++){
                        board[i][j] = items[j].charAt(0);
                    }
                }


            } catch (Throwable t){

            }
        }
    }
}
