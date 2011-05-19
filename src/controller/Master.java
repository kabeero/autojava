/*
 * Class: Master.java
 * Project: Arduino Control System (ACS)
 * Description:
 *   Master controller that initializes all parts of the Model-View-Controller (MVC) and displays output to System.out console to log errors
 * Author:  Mahir Gharzai
 * Date: 2/10/2010
 * 
 */

package controller;

import controller.Controller;
import model.Driver;
import view.UI;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Master{
	private static final int rate = 19200;
	private static final String CLASSNAME = "Master";
	
	public static void main(String[] args) throws Exception {
		System.out.println("Master Controller Initialized");
		try{
			Driver driver = null;
			try{
				driver = new Driver(rate, false);
			}catch(Exception e){
				System.out.println(CLASSNAME + ": Error - Attempting Debug Driver");
				try{
					driver = new Driver(rate, true);
				}catch(Exception f){
					System.err.println("Error setting up driver");
				}
			}

			System.out.println("View:");
			//Console console = new Console("Arduino Output");			
			//if(console.isVisible())
				System.out.println("\tConsole Enabled");
			UI ui = new UI();
			if(ui.isVisible())
				System.out.println("\tUser Interface Enabled");
			System.out.println("Controller:");
			Controller controller = new Controller(ui, ui.getConsole(), driver);
			System.out.println("\tInterface Controller Enabled");
			
			try {
				//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	        } catch (UnsupportedLookAndFeelException ex) {
	        	ex.printStackTrace();
	        }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Check Arduino Status");
			//e.printStackTrace();
		}
	}
}