/*
 * Class: Driver.java
 * Package: model
 * Project: Arduino Control System (ACS)
 * Description:
 *   Responsible for communicating with the Arduino and establishing a reliable connection
 *   Subclasses include actionlisteners that watch for SerialEvents
 *   Throws exception if communication could not be established
 * Author:  Mahir Gharzai
 * Date: 2/10/2010
 * 
 */

package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;

public class Driver implements SerialPortEventListener{
	private static final String CLASSNAME = "Driver";
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A6008lIf", "/dev/ttyUSB0", "COM4", "COM3" };
	private SerialPort serialPort;
	private InputStream input;
	private OutputStream output;
	private PrintWriter writer;
	private static int TIME_OUT = 2000;	/** Milliseconds to block while waiting for port open */
	private static int DATA_RATE = 9600;
	
	public Driver(int r, boolean debug) throws Exception{
		DATA_RATE = r;
		if(debug){
			initializeDebug();
			System.out.println(DATA_RATE);
		}
		else
			initialize();
	}

	@SuppressWarnings("unchecked")
	public void initialize() throws Exception{
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.err.println(CLASSNAME + ": Could not find COM port");
			throw new Exception();
		}

		serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
		serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		input = serialPort.getInputStream();
		output = serialPort.getOutputStream();
		writer = new PrintWriter(output);
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
		System.out.println(CLASSNAME + ": Driver Initialized");
	}
	
	public void initializeDebug() throws Exception{
		writer = new PrintWriter(System.out);
		System.out.println(CLASSNAME + ": Debug Driver Initialized");
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public void addSerialListener(SerialPortEventListener e){
		try{
			serialPort.addEventListener(e);
		}catch(Exception f){
			System.err.println("Could not add serial listener");
		}
	}

	public int getSpeed(){
		// read from serial by polling the Arduino
		// needs to be done
		// need to qry:speed arduino
		int speed = 1000;
		return speed;
	}
	
	public synchronized void setSpeed(int s){
		// set speed by telling Arduino it's otw
		String speed = "00000";
		if(String.valueOf(s).length()==5)
			speed = String.valueOf(s);
		else if(String.valueOf(s).length()==4)
			speed = "0" + String.valueOf(s);
		else if(String.valueOf(s).length()==3)
			speed = "00" + String.valueOf(s);
		else if(String.valueOf(s).length()==2)
			speed = "000" + String.valueOf(s);
		else
			speed = "0000" + String.valueOf(s);
		writer.write("sp" + speed);
		writer.flush();
		//int newSpeed = Integer.to
	}
	
	public synchronized void setMotor(int m){
		String motor = "00000";
		if(String.valueOf(m).length()==5)
			motor = String.valueOf(m);
		else if(String.valueOf(m).length()==4)
			motor = "0" + String.valueOf(m);
		else if(String.valueOf(m).length()==3)
			motor = "00" + String.valueOf(m);
		else if(String.valueOf(m).length()==2)
			motor = "000" + String.valueOf(m);
		else
			motor = "0000" + String.valueOf(m);
		writer.write("mo" + motor);
		writer.flush();
	}
	
	public synchronized void setAngle(int a){
		String angle = "00000";
		if(String.valueOf(a).length()==5)
			angle = String.valueOf(a);
		else if(String.valueOf(a).length()==4)
			angle = "0" + String.valueOf(a);
		else if(String.valueOf(a).length()==3)
			angle = "00" + String.valueOf(a);
		else if(String.valueOf(a).length()==2)
			angle = "000" + String.valueOf(a);
		else
			angle = "0000" + String.valueOf(a);
		writer.write("ag" + angle);
		writer.flush();
	}
	
	public int getRate(){
		// return the polling rate of the connection
		return DATA_RATE;
	}
	
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = input.available();
				byte chunk[] = new byte[available];
				input.read(chunk, 0, available);

				System.out.print(new String(chunk));
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
}