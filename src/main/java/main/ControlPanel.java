package main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel extends JPanel implements ActionListener{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 163636841968587889L;
	
	
	static GridBagConstraints c = new GridBagConstraints();	
	
	public ControlPanel() {
		setPreferredSize(new Dimension(300,100));
		setLayout(new GridBagLayout());
		build();
	}
	
	void build() {
		
		JLabel text = new JLabel("Close this tab to end mouse control");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		c.gridx = 0;
		c.gridy = 0;
		this.add(text, c);
		/*
		JButton end = new JButton("end");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		c.gridx = 1;
		c.gridy = 1;
		end.addActionListener(this);
		this.add(end, c);
		*/
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
