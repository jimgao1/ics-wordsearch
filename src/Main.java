import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        /*
            Initialize the main window containing the 2 frames: solver and generator
         */
        this.setSize(600, 320);
        this.setTitle("WordSearch Generator & Solver");
        this.setLayout(new GridLayout(1, 2));
        this.setResizable(false);
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