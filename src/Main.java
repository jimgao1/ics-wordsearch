import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        this.setSize(600, 300);
        this.setTitle("WordSearch Generator & Solver");
        this.setLayout(new GridLayout(1, 2));
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.add(new GeneratePanel());
        this.add(new SolverPanel());

        this.repaint();
        this.revalidate();
    }

    public static void main(String[] args) {
        new Main();
    }
}