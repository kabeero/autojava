/*
 * Class: UI.java
 * Package: view
 * Project: Arduino Control System (ACS)
 * Description:
 *   The User Interface (UI) for the ACS that allows visualization of received events from the controller
 *   Subpanels include debug console and drawing canvas
 *   Subclasses include event handlers and actionlisteners to modify UI on user changes
 * Author:  Mahir Gharzai
 * Date: 2/10/2010
 * 
 */

package view;

import view.Console;
import view.Gauge;
import view.Radar;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JButton;

import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class UI extends JComponent implements ActionListener, ChangeListener{
	private static final long serialVersionUID = -5258869948035624669L;

	private static int SPEED_MIN=1, SPEED_MAX=2000, SPEED_INIT=1000, SPEED_PRECISION=10, SPEED_JUMP=100;
	private static int SPEED_SLOW=SPEED_MIN, SPEED_MEDIUM_SLOW=(int)(SPEED_MAX *.25), SPEED_MEDIUM=(int)(SPEED_MAX *.50), SPEED_MEDIUM_FAST=(int)(SPEED_MAX *.75), SPEED_FAST=SPEED_MAX;
	private int speed = SPEED_INIT;
	private Color speedColorSafe=Color.GREEN, speedColorWarning=Color.YELLOW, speedColorDanger=Color.RED;

	private static int MOTOR_MIN=0, MOTOR_MAX=180, MOTOR_INIT=90, MOTOR_PRECISION=10, MOTOR_JUMP=100;
	private static int MOTOR_SLOW=MOTOR_MIN, MOTOR_MEDIUM_SLOW=(int)(MOTOR_MAX *.25), MOTOR_MEDIUM=(int)(MOTOR_MAX *.50), MOTOR_MEDIUM_FAST=(int)(MOTOR_MAX *.75), MOTOR_FAST=MOTOR_MAX;
	private int motor = MOTOR_INIT;
	private Color motorColorSafe=Color.GREEN, motorColorWarning=Color.YELLOW, motorColorDanger=Color.RED;
	
	private static int ANGLE_MIN=0, ANGLE_MAX=180;
	private static int ANGLE_LEFT=ANGLE_MIN, ANGLE_CENTER_LEFT=(int)(ANGLE_MAX *.25), ANGLE_CENTER=(int)(ANGLE_MAX *.50), ANGLE_CENTER_RIGHT=(int)(ANGLE_MAX *.75), ANGLE_RIGHT=ANGLE_MAX, ANGLE_PRECISION=1, ANGLE_JUMP=10;
	private int angle = ANGLE_CENTER;
	private Color angleColorSafe=Color.ORANGE, angleColorWarning=Color.GREEN, angleColorDanger=Color.ORANGE;

	private int width=900,height=600;

	private Console console;
	private Gauge gaugeSpeed, gaugeMotor, gaugeAngle;
	private int gaugeWidth=360, gaugeHeight=40;
	private Radar radar;
	private int radarWidth=360, radarHeight=375;
	private JLabel lblPosition;
	private JFrame frmGUI;
	private JPanel pnlUI, pnlControls, pnlPosition, pnlGauges;

	// Blinking LED
	private JLabel lblSpeed, lblSpeedGraph;
	private JPanel pnlSpeedGraph, pnlSpeed, pnlSpeedControls, pnlSpeedSlide;
	private JSlider slideSpeed;
	private JButton btnSpeedSlow, btnSpeedMediumSlow, btnSpeedMedium, btnSpeedMediumFast, btnSpeedFast;
	
	// Forward/Reverse Servo
	private JLabel lblMotor, lblMotorGraph;
	private JPanel pnlMotorGraph, pnlMotor, pnlMotorControls, pnlMotorSlide;
	private JSlider slideMotor;
	private JButton btnMotorSlow, btnMotorMediumSlow, btnMotorMedium, btnMotorMediumFast, btnMotorFast;
	
	// Left/Right Servo
	private JLabel lblAngle, lblAngleGraph;
	private JPanel pnlAngleGraph, pnlAngle, pnlAngleControls, pnlAngleSlide;
	private JSlider slideAngle;
	private JButton btnAngleLeft, btnAngleCenterLeft, btnAngleCenter, btnAngleCenterRight, btnAngleRight;

	// Console panel
	private JPanel pnlConsole;

	public UI(){
		this.setupUI();
	}

	public void setupUI(){
		frmGUI = new JFrame("Control Interface");
		//frmGUI.setLayout(new BoxLayout(frmGUI, BoxLayout.Y_AXIS));
		frmGUI.setLayout(new FlowLayout());
		pnlUI = new JPanel();
		pnlUI.setLayout(new GridLayout(1,2));
		//frmGUI.setLayout(new GridLayout(2,2));
		//  ----------------
		//  | 1  | 2  | 3  |
		//  ----------------
		//  | 4  | 5  | 6  |
		//  ----------------
		//  | 7  | 8  | 9  |
		//  ----------------
		// 1,2,4,5: Radar
		// 7,8,9: 	Console
		// 3-6: 	Control Box, Speed Graph, Angle Graph

		// Sets up pnlSpeed and pnlAngle which are enclosed in pnlControls
		setupControlBox(); 	
		setupPositionGraph();

		frmGUI.add(pnlUI);

		setupConsole();

		frmGUI.add(this.console);
		// end Console

		frmGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGUI.setVisible(true);
		frmGUI.setSize(width,height);
		//frmGUI.pack();
	}

	private void setupConsole(){
		// Console
		console = new Console("Arduino Output");
		pnlConsole = new JPanel();
		pnlConsole.add(console);

		frmGUI.add(this.pnlConsole);
	}

	private void setupPositionGraph(){
		this.radar = new Radar(this.radarWidth, this.radarHeight);
		this.lblPosition = new JLabel("Radar");
		this.pnlPosition = new JPanel();
		//this.pnlPosition.setSize(width, height);
		this.pnlPosition.setLayout(new BoxLayout(pnlPosition,BoxLayout.Y_AXIS));
		pnlPosition.add(lblPosition);
		pnlPosition.add(radar);

		pnlUI.add(pnlPosition);
		//frmGUI.add(this.pnlPosition);
	}

	private void setupControlBox(){
		setupGraphs();		// Sets up pnlSpeedGraph and pnlAngleGraph

		// Speed		
		pnlSpeedSlide = new JPanel();
		pnlSpeedSlide.setLayout(new BoxLayout(pnlSpeedSlide, BoxLayout.X_AXIS));
		slideSpeed = new JSlider(JSlider.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_INIT);
		slideSpeed.setMinorTickSpacing(SPEED_PRECISION);
		slideSpeed.setMajorTickSpacing(SPEED_JUMP);
		slideSpeed.setMaximumSize(new Dimension(this.gaugeWidth+20, this.gaugeHeight));
		lblSpeed = new JLabel(speed + " ms");
		pnlSpeedSlide.add(slideSpeed);
		pnlSpeedSlide.add(lblSpeed);

		pnlSpeedControls = new JPanel();
		pnlSpeedControls.setLayout(new BoxLayout(pnlSpeedControls, BoxLayout.X_AXIS));
		btnSpeedSlow = new JButton("Slow");
		btnSpeedSlow.addActionListener(this);
		btnSpeedMediumSlow = new JButton("Med Slow");
		btnSpeedMediumSlow.addActionListener(this);
		btnSpeedMedium = new JButton("Medium");
		btnSpeedMedium.addActionListener(this);
		btnSpeedMediumFast = new JButton("Med Fast");
		btnSpeedMediumFast.addActionListener(this);
		btnSpeedFast = new JButton("Fast");
		btnSpeedFast.addActionListener(this);
		pnlSpeedControls.add(btnSpeedSlow);pnlSpeedControls.add(btnSpeedMediumSlow);pnlSpeedControls.add(btnSpeedMedium);pnlSpeedControls.add(btnSpeedMediumFast);pnlSpeedControls.add(btnSpeedFast);

		pnlSpeed = new JPanel();
		pnlSpeed.setLayout(new BoxLayout(pnlSpeed, BoxLayout.Y_AXIS));
		//pnlSpeed.setSize(this.gaugeWidth, this.gaugeHeight*2);
		pnlSpeed.add(pnlSpeedGraph);
		pnlSpeed.add(pnlSpeedSlide);
		pnlSpeed.add(pnlSpeedControls);
		// end Speed

		// Motor
		pnlMotorSlide = new JPanel();
		pnlMotorSlide.setLayout(new BoxLayout(pnlMotorSlide, BoxLayout.X_AXIS));
		slideMotor = new JSlider(JSlider.HORIZONTAL, MOTOR_MIN, MOTOR_MAX, MOTOR_INIT);
		slideMotor.setMinorTickSpacing(MOTOR_PRECISION);
		slideMotor.setMajorTickSpacing(MOTOR_JUMP);
		slideMotor.setMaximumSize(new Dimension(this.gaugeWidth+20, this.gaugeHeight));
		lblMotor = new JLabel(String.valueOf(motor));
		pnlMotorSlide.add(slideMotor);
		pnlMotorSlide.add(lblMotor);
		
		pnlMotorControls = new JPanel();
		pnlMotorControls.setLayout(new BoxLayout(pnlMotorControls, BoxLayout.X_AXIS));
		btnMotorSlow = new JButton("Slow");
		btnMotorSlow.addActionListener(this);
		btnMotorMediumSlow = new JButton("Med Slow");
		btnMotorMediumSlow.addActionListener(this);
		btnMotorMedium = new JButton("Medium");
		btnMotorMedium.addActionListener(this);
		btnMotorMediumFast = new JButton("Med Fast");
		btnMotorMediumFast.addActionListener(this);
		btnMotorFast = new JButton("Fast");
		btnMotorFast.addActionListener(this);
		pnlMotorControls.add(btnMotorSlow);pnlMotorControls.add(btnMotorMediumSlow);pnlMotorControls.add(btnMotorMedium);pnlMotorControls.add(btnMotorMediumFast);pnlMotorControls.add(btnMotorFast);
	
		pnlMotor = new JPanel();
		pnlMotor.setLayout(new BoxLayout(pnlMotor, BoxLayout.Y_AXIS));
		pnlMotor.add(pnlMotorGraph);
		pnlMotor.add(pnlMotorSlide);
		pnlMotor.add(pnlMotorControls);
		// end Motor
		
		// Angle
		pnlAngleSlide = new JPanel();
		pnlAngleSlide.setLayout(new BoxLayout(pnlAngleSlide, BoxLayout.X_AXIS));
		slideAngle = new JSlider(JSlider.HORIZONTAL, ANGLE_LEFT, ANGLE_RIGHT, ANGLE_CENTER);
		slideAngle.setMinorTickSpacing(ANGLE_PRECISION);
		slideAngle.setMajorTickSpacing(ANGLE_JUMP);
		slideAngle.setMaximumSize(new Dimension(this.gaugeWidth+20, this.gaugeHeight));
		lblAngle = new JLabel(angle + " " + String.valueOf((char)176));
		pnlAngleSlide.add(slideAngle);
		pnlAngleSlide.add(lblAngle);

		pnlAngleControls = new JPanel();
		pnlAngleControls.setLayout(new BoxLayout(pnlAngleControls, BoxLayout.X_AXIS));
		btnAngleLeft = new JButton("Left");
		btnAngleLeft.addActionListener(this);
		btnAngleCenterLeft = new JButton(String.valueOf(UI.ANGLE_CENTER_LEFT) + String.valueOf((char)176));
		btnAngleCenterLeft.addActionListener(this);
		btnAngleCenter = new JButton("Center");
		btnAngleCenter.addActionListener(this);
		btnAngleCenterRight = new JButton(String.valueOf(UI.ANGLE_CENTER_RIGHT) + String.valueOf((char)176));
		btnAngleCenterRight.addActionListener(this);
		btnAngleRight = new JButton("Right");
		btnAngleRight.addActionListener(this);
		pnlAngleControls.add(btnAngleLeft);pnlAngleControls.add(btnAngleCenterLeft);pnlAngleControls.add(btnAngleCenter);pnlAngleControls.add(btnAngleCenterRight);pnlAngleControls.add(btnAngleRight);

		pnlAngle = new JPanel();
		pnlAngle.setLayout(new BoxLayout(pnlAngle, BoxLayout.Y_AXIS));
		//pnlAngle.setSize(this.gaugeWidth, this.gaugeHeight*2);
		pnlAngle.add(pnlAngleGraph);
		pnlAngle.add(pnlAngleSlide);
		pnlAngle.add(pnlAngleControls);
		// end Angle
		
		pnlGauges = new JPanel();
		pnlGauges.setLayout(new BoxLayout(pnlGauges, BoxLayout.Y_AXIS));
		//pnlGauges.setSize(this.gaugeWidth, this.gaugeHeight * 4);
		pnlGauges.add(pnlSpeed);
		pnlGauges.add(pnlMotor);
		pnlGauges.add(pnlAngle);

		pnlControls = new JPanel();
		pnlControls.setSize(gaugeWidth, radarHeight);
		pnlControls.setLayout(new BoxLayout(pnlControls, BoxLayout.Y_AXIS));
		pnlControls.add(pnlGauges);
		
		pnlUI.add(pnlControls);
		//frmGUI.add(pnlControls);
	}

	private void setupGraphs(){
		// Sets up pnlSpeedGraph and pnlAngleGraph
		
		// Speed Gauge
		lblSpeedGraph = new JLabel("Blink");
		gaugeSpeed = new Gauge(this.gaugeWidth, this.gaugeHeight, SPEED_MIN, SPEED_MEDIUM_SLOW, SPEED_INIT, SPEED_MEDIUM_FAST, SPEED_MAX, SPEED_INIT);
		gaugeSpeed.setColors(this.speedColorDanger, this.speedColorWarning, this.speedColorSafe);
		pnlSpeedGraph = new JPanel();
		pnlSpeedGraph.setLayout(new BoxLayout(pnlSpeedGraph, BoxLayout.Y_AXIS));
		gaugeSpeed.setMaximumSize(new Dimension((int)(this.gaugeWidth*1.25), (int)(this.gaugeHeight*1.25)));
		pnlSpeedGraph.add(lblSpeedGraph);
		pnlSpeedGraph.add(gaugeSpeed);
		// end Speed Gauge
		
		// Motor Gauge
		lblMotorGraph = new JLabel("Motor");
		gaugeMotor = new Gauge(this.gaugeWidth, this.gaugeHeight, MOTOR_MIN, MOTOR_MEDIUM_SLOW, MOTOR_INIT, MOTOR_MEDIUM_FAST, MOTOR_MAX, MOTOR_INIT);
		gaugeMotor.setColors(this.motorColorSafe, this.motorColorWarning, this.motorColorDanger);
		pnlMotorGraph = new JPanel();
		pnlMotorGraph.setLayout(new BoxLayout(pnlMotorGraph, BoxLayout.Y_AXIS));
		gaugeMotor.setMaximumSize(new Dimension((int)(this.gaugeWidth*1.25), (int)(this.gaugeHeight*1.25)));
		pnlMotorGraph.add(lblMotorGraph);
		pnlMotorGraph.add(gaugeMotor);
		// end Motor Gauge
		
		// Angle Gauge
		lblAngleGraph = new JLabel("Angle");
		gaugeAngle = new Gauge(this.gaugeWidth, this.gaugeHeight, ANGLE_LEFT, ANGLE_CENTER_LEFT, ANGLE_CENTER, ANGLE_CENTER_RIGHT, ANGLE_RIGHT, ANGLE_CENTER);
		gaugeAngle.setColors(this.angleColorSafe, this.angleColorWarning, this.angleColorDanger);
		gaugeAngle.setType(Gauge.TYPE_BALANCE);
		gaugeAngle.setMaximumSize(new Dimension((int)(this.gaugeWidth*1.25), (int)(this.gaugeHeight*1.25)));
		pnlAngleGraph = new JPanel();
		pnlAngleGraph.setLayout(new BoxLayout(pnlAngleGraph, BoxLayout.Y_AXIS));
		pnlAngleGraph.add(lblAngleGraph);
		pnlAngleGraph.add(gaugeAngle);
		// end Angle Gauge
	}

	public int getSpeed(){
		return speed;
	}
	
	public int getMotor(){
		return motor;
	}
	
	public int getAngle(){
		return angle;
	}
	
	public Console getConsole(){
		return this.console;
	}
	
	public void actionPerformed(ActionEvent e) {
		if((JButton)e.getSource()==this.btnSpeedSlow){
			this.slideSpeed.setValue(SPEED_SLOW);
		}
		else if((JButton)e.getSource()==this.btnSpeedMediumSlow){
			this.slideSpeed.setValue(SPEED_MEDIUM_SLOW);
		}
		else if((JButton)e.getSource()==this.btnSpeedMedium){
			this.slideSpeed.setValue(SPEED_MEDIUM);
		}
		else if((JButton)e.getSource()==this.btnSpeedMediumFast){
			this.slideSpeed.setValue(SPEED_MEDIUM_FAST);
		}
		else if((JButton)e.getSource()==this.btnSpeedFast){
			this.slideSpeed.setValue(SPEED_FAST);
		}
		else if((JButton)e.getSource()==this.btnMotorSlow){
			this.slideMotor.setValue(MOTOR_SLOW);
		}
		else if((JButton)e.getSource()==this.btnMotorMediumSlow){
			this.slideMotor.setValue(MOTOR_MEDIUM_SLOW);
		}
		else if((JButton)e.getSource()==this.btnMotorMedium){
			this.slideMotor.setValue(MOTOR_MEDIUM);
		}
		else if((JButton)e.getSource()==this.btnMotorMediumFast){
			this.slideMotor.setValue(MOTOR_MEDIUM_FAST);
		}
		else if((JButton)e.getSource()==this.btnMotorFast){
			this.slideMotor.setValue(MOTOR_FAST);
		}
		else if((JButton)e.getSource()==this.btnAngleLeft){
			this.slideAngle.setValue(ANGLE_LEFT);
		}
		else if((JButton)e.getSource()==this.btnAngleCenterLeft){
			this.slideAngle.setValue(ANGLE_CENTER_LEFT);
		}
		else if((JButton)e.getSource()==this.btnAngleCenter){
			this.slideAngle.setValue(ANGLE_CENTER);
		}
		else if((JButton)e.getSource()==this.btnAngleCenterRight){
			this.slideAngle.setValue(ANGLE_CENTER_RIGHT);
		}
		else if((JButton)e.getSource()==this.btnAngleRight){
			this.slideAngle.setValue(ANGLE_RIGHT);
		}
	}

	public void stateChanged(ChangeEvent e){
		if(e.getSource()==this.slideSpeed){
			this.speed = this.slideSpeed.getValue();
			this.lblSpeed.setText(String.valueOf(this.slideSpeed.getValue()) + " ms");
			this.gaugeSpeed.setCurrVal(this.slideSpeed.getValue());
			this.gaugeSpeed.repaint();
		}
		else if(e.getSource()==this.slideMotor){
			this.motor = this.slideMotor.getValue();
			this.lblMotor.setText(String.valueOf(motor));
			this.gaugeMotor.setCurrVal(this.slideMotor.getValue());
			this.gaugeMotor.repaint();
		}
		else if(e.getSource()==this.slideAngle){
			this.angle = this.slideAngle.getValue();
			this.lblAngle.setText(String.valueOf(angle) + String.valueOf((char)176));
			this.gaugeAngle.setCurrVal(this.angle);
			this.gaugeAngle.repaint();
		}
	}

	public void addSpeedListener(ActionListener e){
		btnSpeedSlow.addActionListener(e);
		btnSpeedMediumSlow.addActionListener(e);
		btnSpeedMedium.addActionListener(e);
		btnSpeedMediumFast.addActionListener(e);
		btnSpeedFast.addActionListener(e);
	}

	public void addAngleListener(ActionListener e){
		btnAngleLeft.addActionListener(e);
		btnAngleCenterLeft.addActionListener(e);
		btnAngleCenter.addActionListener(e);
		btnAngleCenterRight.addActionListener(e);
		btnAngleRight.addActionListener(e);
	}

	public void addSpeedSlideListener(ChangeListener e){
		slideSpeed.addChangeListener(e);
		slideSpeed.addChangeListener(this);
	}
	
	public void addMotorSlideListener(ChangeListener e){
		slideMotor.addChangeListener(e);
		slideMotor.addChangeListener(this);
	}

	public void addAngleSlideListener(ChangeListener e){
		slideAngle.addChangeListener(e);
		slideAngle.addChangeListener(this);
	}
}