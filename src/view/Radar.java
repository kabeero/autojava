package view;

import java.awt.*;
import java.util.*;

public class Radar extends Canvas{

	private static final long serialVersionUID = -353887950010309446L;
	private int minVal, lowVal, medVal, highVal, maxVal, initVal, currVal;
	private int width=360, height=400, concentric=5;
	private Color lineColor=Color.BLACK, tickColor=Color.LIGHT_GRAY, tickColorAlt=Color.GRAY;
	private Boolean zebraStriped=true;
	private ArrayList targetList, targetColors, targetData;
	
	public Radar(int width, int height){
		this.setSize(width,height);
		this.targetList = new ArrayList();
		this.targetColors = new ArrayList();
		this.targetData = new ArrayList();
	}
	
	public void addTarget(String name, Color color, int[] data){
		targetList.add(name);
		targetColors.add(color);
		targetData.add(data);
	}

	public void setRandomColors(){
		this.lineColor = randomColor();
		this.tickColor = randomColor();
	}

	private Color randomColor(){
		int colorChoice = (int) Math.ceil((Math.random() * 7.0));
		Color newColor;
		switch(colorChoice){
		case 1:
			newColor = Color.RED;
			break;
		case 2:
			newColor = Color.ORANGE;
			break;
		case 3:
			newColor = Color.YELLOW;
			break;
		case 4:
			newColor = Color.GREEN;
			break;
		case 5:
			newColor = Color.BLUE;
			break;
		case 6:
			newColor = Color.PINK;
			break;
		default:
			newColor = Color.BLACK;
			break;	
		}
		return newColor;
	}

	public void paint(Graphics gfx){
		gfx.setColor(this.lineColor);
		int[] circles = new int[concentric];
		for(int i=0;i<concentric;i++){
			circles[i]= width - i * width/concentric;
			gfx.drawOval((width-circles[i])/2, (width-circles[i])/2, circles[i], circles[i]);
		}
		gfx.setColor(this.tickColor);
		//gfx.drawLine(width/2, 0, width/2, width);
		//gfx.drawLine(0, width/2, width, width/2);
		int angleDetail = 36;
		double[] angles = new double[angleDetail];
		int length = width/2;
		int origin = length;
		for(int i=0;i<(angleDetail);i++){
			angles[i] = i * (double)((2*Math.PI)/angleDetail);
			if(zebraStriped){
				if(gfx.getColor()==this.tickColor)
					gfx.setColor(this.tickColorAlt);
				else
					gfx.setColor(this.tickColor);
			}
			gfx.drawLine(origin, origin, (int)(origin + length * Math.cos(angles[i])), (int)(origin + length * Math.sin(angles[i])));
		}
	}
}