package de.mhus.tool.split;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSwing;

public class SplitUiTool {

	private static JTextArea result;
	private static JTextField source;
	private static JTextField split;

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		MSwing.halfFrame(frame);
		MSwing.centerFrame(frame);
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		frame.setContentPane(p);
		
		JPanel panel = new JPanel();
		p.add(panel,BorderLayout.NORTH);
		panel.setLayout(new GridLayout(5, 1));
		panel.add(new JLabel("Split Expression:"));
		split = new JTextField("");
		panel.add(split);
		
		panel.add(new JLabel("Text to split"));
		source = new JTextField();
		panel.add(source);
		
		JButton bExecute = new JButton("Execute");
		panel.add(bExecute);
		bExecute.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				result.setText( MString.join(source.getText().split(split.getText()), '\n') );
			}
		});
		
		result = new JTextArea();
		p.add(result,BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static Component JLabel(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
