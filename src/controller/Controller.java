/*
 * Class: Controller.java
 * Package: controller
 * Project: Arduino Control System (ACS)
 * Description:
 *   Responsible for handling requests sent by the user controlled UI to the driver that communicates with the Arduino
 *   Subclasses include event handlers and actionlisteners
 * Author:  Mahir Gharzai
 * Date: 2/10/2010
 * 
 */


package controller;

import view.UI;
import view.Console;
import model.Driver;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Controller{
	private UI viewControl;
	private Console viewConsole;
	private Driver modelDriver;

	Controller(UI uiView, Console uiCon, Driver driver){
		viewControl = uiView;
		viewConsole = uiCon;
		modelDriver = driver;
		
		viewConsole.clearText();
		//viewControl.addSpeedListener(new SpeedListener());
		viewControl.addSpeedSlideListener(new SpeedSlideListener());
		viewControl.addMotorSlideListener(new MotorSlideListener());
		//viewControl.addAngleListener(new AngleListener());
		viewControl.addAngleSlideListener(new AngleSlideListener());
	}

	// Listeners:  These listeners are used to link the events on the UI to the Driver

	class SpeedListener implements ActionListener{
		public void actionPerformed(ActionEvent e){			
			int speed = viewControl.getSpeed();
			viewConsole.append(String.valueOf(speed));
			modelDriver.setSpeed(speed);
		}
	}

	class AngleListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int angle = viewControl.getAngle();
			viewConsole.append(String.valueOf(angle));
			modelDriver.setAngle(angle);
		}
	}

	class SpeedSlideListener implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			int speed = viewControl.getSpeed();
			modelDriver.setSpeed(speed);
			viewConsole.append("Speed: " + String.valueOf(speed));
		}
	}

	class MotorSlideListener implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			int motor = viewControl.getMotor();
			modelDriver.setMotor(motor);
			viewConsole.append("Motor: " + String.valueOf(motor));
		}	
	}

	class AngleSlideListener implements ChangeListener{
		public void stateChanged(ChangeEvent e){
			int angle = viewControl.getAngle();
			modelDriver.setAngle(angle);
			viewConsole.append("Angle: " + String.valueOf(angle));
		}
	}

	class RateListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int rate = Integer.parseInt(((JComboBox)e.getSource()).getSelectedItem().toString());
			try{
				//modelDriver.close();
				//modelDriver = new Driver(rate);
			}catch(Exception v){
				System.out.println("Unable to re-create driver with desired speed");
			}
			viewConsole.append("Rate: " + rate + " bps");
		}
	}

	class TableListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// perform an operation to show that the cell has been modified
		}
	}
	
	class SerialListener implements SerialPortEventListener{
		public void serialEvent(SerialPortEvent arg0) {
			viewConsole.append("Command acknowledged");
		}
	}
}