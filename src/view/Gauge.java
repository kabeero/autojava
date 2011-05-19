package view;

import java.awt.*;

public class Gauge extends Canvas{
	
	private static final long serialVersionUID = 505773127383463860L;
	private int minVal, lowVal, medVal, highVal, maxVal, initVal, currVal;
	private int width=400, height=25;
	private Color lowColor = Color.GREEN, medColor = Color.YELLOW, highColor = Color.RED, backColor = Color.WHITE, tickColor = Color.RED;
	private int type=0, tickWidth=10;
	public static int TYPE_BAR=0, TYPE_BALANCE=1;
	
	public Gauge(int width, int height, int min, int low, int med, int high, int max, int init){
		this.minVal = min;
		this.lowVal = low;
		this.medVal = med;
		this.highVal = high;
		this.maxVal = max;
		this.initVal = init;
		
		this.currVal = init;

		this.width = width;
		this.height = height;
		this.setSize(width,height);
	}
	
	public void setType(int type){
		this.type = type;
	}

	public void setCurrVal(int v){
		this.currVal = v;
	}
	
	public int getCurrVal(){
		return this.currVal;
	}

	public void setColors(Color low, Color med, Color high){
		this.lowColor = low;
		this.medColor = med;
		this.highColor = high;
	}

	public void setRandomColors(){
		this.lowColor = randomColor();
		this.medColor = randomColor();
		this.highColor = randomColor();
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
		if(type==Gauge.TYPE_BAR){
			if(currVal<=lowVal)
		    	gfx.setColor(this.lowColor);
		    else if(currVal>=highVal)
		    	gfx.setColor(this.highColor);
		    else
		    	gfx.setColor(this.medColor);
		    //gfx.draw3DRect(5,0,currVal/maxVal*width, height, true);
		    gfx.fill3DRect(5,0,(int)(((double)(currVal)/maxVal)*width),height,true);
		}else if(type==Gauge.TYPE_BALANCE){
			gfx.setColor(this.backColor);
			gfx.fill3DRect(5,0,width,height,true);
			if(currVal<=lowVal)
		    	gfx.setColor(this.lowColor);
		    else if(currVal>=highVal)
		    	gfx.setColor(this.highColor);
		    else
		    	gfx.setColor(this.medColor);
			gfx.fill3DRect(5+(int)(((double)(currVal)/maxVal)*width)-(int)(tickWidth/2),0,tickWidth,height,true);
		}
	}
}