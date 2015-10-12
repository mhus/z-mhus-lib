package de.mhus.tools.base64;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.apache.geronimo.mail.util.Base64;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSwing;
//import de.mhus.lib.core.util.Base64;

public class Base64UiTool {

	private static JTextArea source;
	private static JTextArea result;

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		MSwing.halfFrame(frame);
		MSwing.centerFrame(frame);
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		frame.setContentPane(p);

		JButton bExecuteToBase = new JButton("\\/ Source -> Base64");
		p.add(bExecuteToBase, BorderLayout.SOUTH);
		bExecuteToBase.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String s = source.getText();
//				String r = Base64.encode( s );
				String r = new String( Base64.encode(s.getBytes()) );
				result.setText( r );
				
			}
		});

		JButton bExecuteToSource = new JButton("/\\ Base64 -> Source");
		p.add(bExecuteToSource, BorderLayout.NORTH);
		bExecuteToSource.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String s = result.getText();
//				String r = new String( Base64.decode( s ) );
				String r = new String( Base64.decode(s.getBytes()) );
				source.setText( r );
			}
		});
		
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		p.add(split, BorderLayout.CENTER);
		
		source = new JTextArea();
		result = new JTextArea();
		
		source.setLineWrap(true);
		source.setWrapStyleWord(true);
		
		result.setLineWrap(true);
		result.setWrapStyleWord(true);
		
		
		source.setBorder( BorderFactory.createTitledBorder("Source") );
		result.setBorder( BorderFactory.createTitledBorder("Base64") );
		
		JScrollPane sourceScroll = new JScrollPane(source);
		JScrollPane resultScroll = new JScrollPane(result);
		
		split.setLeftComponent(sourceScroll);
		split.setRightComponent(resultScroll);
		split.setDividerLocation(0.5);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
