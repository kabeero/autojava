/*
 * Class: Console.java
 * Package: view
 * Project: Arduino Control System (ACS)
 * Description:
 *   Debug Console used for monitoring purposes.  Logs events sent by the controller from the driver communicating with the Arduino
 *   Parent is UI.java
 * Author:  Mahir Gharzai
 * Date: 2/10/2010
 * 
 */

package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Console extends JPanel{
	private static final long serialVersionUID = -5663744553978924347L;

	private JScrollPane consoleScroll;
	private JTextArea consoleText;

	// Enable and use this to have Console appear in separate window:
	//private JFrame consoleFrame;

	public Console(){
		initConsole("Debug Console");
	}

	public Console(String comment){
		initConsole(comment);
	}

	private void initConsole(String label){
		consoleText = new JTextArea(">", 10, 60);
		consoleText.setWrapStyleWord(true);
		consoleText.setEditable(false);
		consoleScroll = new JScrollPane(consoleText);
		consoleScroll.setHorizontalScrollBarPolicy(consoleScroll.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		super.add(consoleScroll);
	}

	public void clearText(){
		this.consoleText.setText("> Initialized...");
	}

	public void append(String newMessage){
		this.consoleText.setText(this.consoleText.getText() + "\n> " + newMessage);
		this.consoleText.setCaretPosition(this.consoleText.getText().length());
	}
	
	class serialSpeedListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// Listener to monitor the changes outputted by the Driver class:
			//   if speed changes: Speed: ___
			//   if rate changes: Rate: ___ kbps
			//if(driver.getMessage())
		}		
	}
}