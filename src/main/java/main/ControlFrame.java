package main;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ControlFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8880342242134574593L;
	
	static JLabel label = new JLabel();;
	private ControlPanel controlPanel = new ControlPanel();
	
	public ControlFrame() {
		super();
        
        
        //label.setText("Power Glove Mouse Control");
        //add(label, BorderLayout.SOUTH);
        controlPanel = new ControlPanel();
        add(controlPanel);
        setSize(300,100);
        setResizable(false);
        setBackground(Color.white);
       
	}
	
	
	
}
