import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    //Frames
    GeneratePanel generatePanel;

    public Main(){
        this.setSize(600, 400);
        this.setTitle("Fuck");
        this.setLayout(new GridLayout(1, 2));
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        generatePanel = new GeneratePanel();
        this.add(generatePanel);

        this.add(new SolverPanel());

        this.repaint();
        this.revalidate();
    }

    public static void main(String[] args){
        new Main();
    }
}