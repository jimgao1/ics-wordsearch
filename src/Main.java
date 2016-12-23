import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;//Import all the libraries required 
public class Main extends JFrame implements ActionListener
{
	JFrame gridFrame=new JFrame();
	JFrame ioFrame=new JFrame();
	JLabel wLabel[]=new JLabel[2];
	JButton opButton[]=new JButton[3];
	JPanel generalPan=new JPanel();
	JPanel buttonPan=new JPanel();
	JPanel xyPan=new JPanel();
	JTextField xField=new JTextField(5);
	JTextField yField=new JTextField(5);
	JTextField inField= new JTextField(20);
	JTextField outField= new JTextField(20);
	JLabel inLabel= new JLabel("Enter the input file name");
	JLabel outLabel= new JLabel("Enter the soluiton file name");
	JPanel inPan = new JPanel();
	JPanel outPan = new JPanel();
	public Main()
	{		
		wLabel[0]=new JLabel("Welcome to the word puzzle generator and solver");
		wLabel[1]=new JLabel("Please select if you want to generate a puzzle or solve a puzzle");
		opButton[0]=new JButton("Generate");
		opButton[0].addActionListener(this);
		opButton[1]=new JButton("Solve");
		opButton[1].addActionListener(this);
		opButton[2]=new JButton("Close");
		opButton[2].addActionListener(this);
		
		BoxLayout generalLayout	=new BoxLayout(generalPan,BoxLayout.Y_AXIS);//Set the general layout
		
		gridFrame.setTitle("Word Puzzle Program");//Set the title of the frame
        gridFrame.setSize(450,130);//Set the size of the frame
        gridFrame.setResizable(false);//Size of the title is unchangeable
        
        buttonPan.setLayout(new FlowLayout());//Set the layout for the Button Panel
        buttonPan.add(opButton[0]);
        buttonPan.add(opButton[1]);
        buttonPan.add(opButton[2]);
        generalPan.setLayout(new FlowLayout());
        generalPan.add(wLabel[0]);
        generalPan.add(wLabel[1]);
        generalPan.add(buttonPan);
        xyPan.setLayout(new FlowLayout());
        xyPan.add(xField);
        xyPan.add(yField);
        
        gridFrame.setLayout(new GridLayout(1, 3));
        gridFrame.add(generalPan);//Add general panel to the frame
        gridFrame.add(buttonPan);
        gridFrame.add(xyPan);
        gridFrame.setVisible(true);//Set visibility to true
        
        ioFrame.setTitle("Input and Output Files");
        ioFrame.setSize(450,130);
        ioFrame.setResizable(false);
        ioFrame.setLocation(450,0);
        
        inPan.setLayout(new FlowLayout());
        inPan.add(inLabel);
        inPan.add(inField);
        outPan.setLayout(new FlowLayout());
        outPan.add(outLabel);
        outPan.add(outLabel);
        
        ioFrame.setLayout(new GridLayout(1, 2));
        ioFrame.add(inPan);
        ioFrame.add(outPan);
        ioFrame.setVisible(true);
	}
	public void actionPerformed(ActionEvent event)//Action Perform Method
    {
        String command=event.getActionCommand();//Declare a string for the command
        if(command=="Close")
        {
        	this.dispose();
        	return;
        }
        else if(command=="Generate")
        {
        	GenerateFrame frameG=new Generateframe();
        	this.dispose();
        	return;
        }
        else if(command=="Sol	ve")
        {
        	GenerateFrame frameG=new Generateframe();
        	this.dispose();
        	return;
        }
    }
	public static void main(String args[])
	{
		Main mainframe=new Main();
		ArrayList<String> words = new ArrayList<>();
		words.add("sample_word");
		
		//To Generate Board
		char[][] board = GenerateBoard.generate(words);
		
		//To Solve Board
		SolveBoard.SolverResult[] results = SolveBoard.findWord(board, words);
		
		//The i'th index in the results refers to the result of the i'th word in the word list
		//The element at index i is null if that word cannot be found in the grid
		
		 /* 	Each of the results contain 3 pieces of information:
		 * 		- The X coord of the first letter
		 * 		- The Y coord of the first letter
		 * 		- The direction of the word (from 0 to 7)
		 * 
		 * 	For example, below is the result for the first word */
		 
		
		if (results[0] != null){
			int X = results[0].locationX;
			int Y = results[0].locationY;
			int direction = results[0].direction;
			
			//For more information on direction, refer to SolveBoard.java
		} else {
			//The word is not found
		}
	}
}
