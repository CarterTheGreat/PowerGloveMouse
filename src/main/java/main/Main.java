package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.*;

final class Main{
	
	static boolean running = true;
	
	static int ind0;
	static int ind1;
	static int ind2;
	static int ind3;
	
	static int accelX;
	static int accelXLast;
	static int accelY;
	static int accelYLast;
	
	static int accelZ;
	static int accelZLast;
	static int accelZLastLast;
	static boolean tapPhaseOne;
	static boolean tapped;
	
	static int xDir;
	static int yDir;
	
	static Robot mouse;	
	
	static int xThresholdPos = 70;
	static int xThresholdNeg = 55;
	static int yThresholdPos = 30;
	static int yThresholdNeg = 60;
	
	static char key;
	static String keyString;
	static String data = "";
	
	static boolean isExists = true;
	//static SerialPort comPort = SerialPort.getCommPorts()[0];
	
	public static void main(String[] args){
		
		String comPortS = JOptionPane.showInputDialog("Enter Com Port Glove Is Connected To  Ex. COM4 , COM15");
		
		System.out.println(comPortS);
		
		SerialPort comPort = SerialPort.getCommPort(comPortS);

		
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

		InputStream in = comPort.getInputStream();
		
		
		try {
			in.read();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not create input stream");
			JOptionPane.showMessageDialog(null, "Input Stream Could Not Be Created, Likely Wrong COMM Port Input");
			isExists = false;
		}
		
		
		System.out.println("Attempted port opening and input stream created");
				
			try
			{
				
				mouse = new Robot();
				
			   while(running && isExists) {
			      
				
				   
			      key = (char)in.read();
			      keyString = Character.toString(key);
			      
			      if(key=='>') {
			    	  data=data.concat(keyString);
			    	  int slashes = 0;
			    	  
			    	  for(int i=0; i<data.length();i++) {
			    		  if(data.charAt(i) == '/') {
			    			  slashes++;
			    		  }
			    	  }
			    	  
			    	  if(data.contains("<") && data.contains(">") && slashes == 2 ) {
			    		
			    		  try { 
			    			  
			    		  System.out.println(data);
			    		  
			    		//INPUT ALLOCATION-------------------------------------------------------------------------
							//Data in format, length of x, y, and z vary from ones place to hundreds place
							//x/y/z#
							//xxx/yyy/zzz/#
								
							//Data from 	
							ind0 = data.indexOf('<');
							ind1 = data.indexOf('/',0);
							ind2 = data.indexOf('/',ind1+1);
							ind3 = data.indexOf(">");
							
							//System.out.println("ind "+ ind1+" "+ind2);
							
							//AccelValue as string
							String x = data.substring(ind0+1,ind1);
							String y = data.substring(ind1+1,ind2);	
							String z = data.substring(ind2+1,ind3);
								
							//System.out.println("String x, y, & z "+x+ " " +y+" "+z);
							
							//Accel value as int
							accelX = Integer.parseInt(x);
							accelY = Integer.parseInt(y);
							accelZ = Integer.parseInt(z);
							
							//System.out.println("Accel x y & z "+accelX+" "+accelY+" "+accelZ);
							
							if(accelX < xThresholdPos && accelX > -xThresholdNeg) {
								accelX=0;
							}
							if(accelY < yThresholdPos && accelY > -yThresholdNeg) {
								accelY=0;
							}
							//INPUT ALLOCATION END--------------------------------------------------------------------
							
							
							
							//Tap functionality EXPIREMENTAL----------------------------------------------------------
							
							//LastLast
							//Last
							//accelZ
							
							//When accelZLast is much less than accelZLastLast then accel z in unmoving
							//Fast down then still
							
							//NEXT TO TRY fast down then Fast up
							
												
							if(accelZLast < accelZLastLast - 12) {
								
								System.out.println("Tap phase 1");
								tapPhaseOne = true;
								if(accelZ > accelZLast+12) {
									
									System.out.println("Tapped");
									
									mouse.mousePress(InputEvent.BUTTON1_DOWN_MASK);
									mouse.mouseRelease(InputEvent.BUTTON1_MASK);
									tapped=true;
									
								}
							}
							
							//System.out.println("accel z's "+accelZ+" "+accelZLast+" "+accelZLastLast);
							
							accelZLastLast = accelZLast;
							accelZLast = accelZ;
							
							//END TAP---------------------------------------------------------------------------------
							
							
							
								//Mouse acceleration------------------------------------------------------------------
							
									//System.out.println("Accel x & y post threshold "+accelX+" "+accelY);
							
								//LVL1
									if(!(accelX == 0)) {
										xDir = accelX/Math.abs(accelX);
									}else xDir = 0;
									if(!(accelY == 0)) {
										yDir = accelY/Math.abs(accelY);
									}else yDir = 0;
									
									
								//LVL 2
									if(accelX > 70 || accelX < -55) {
										xDir = xDir*2;
									}
									if(accelY > 40 || accelY < -60) {
										yDir = yDir*2;
									}
									
								//LVL 3
									if(accelX > 100 || accelX < -65) {
										xDir = xDir*3;
									}
									if(accelY > 60 || accelY < -80) {
										yDir = yDir*3;
									}
									
									
								//LVL 4
									if(accelX > 160 || accelX < -130) {
										xDir = xDir*4;
									}
									if(accelY > 100 || accelY < -100) {
										yDir = yDir*4;
									}
									//END MOUSE ACCELERATION-----------------------------------------------------------
									
									
									
							//MOVE MOUSE-------------------------------------------------------------------------------		
							
							
							//Get mouse location
							PointerInfo a = MouseInfo.getPointerInfo();
							Point b = a.getLocation();
							int mouseX = (int) b.getX();
							int mouseY = (int) b.getY();
							
							
							//System.out.println("Mouse location "+mouseX+" "+mouseY);
							//System.out.println(xDir+ " " +yDir);
							if(!tapped) {
								
								mouse.mouseMove(mouseX+xDir,mouseY-yDir);
								
							}else Thread.sleep(50);
							
							tapPhaseOne = false;
							tapped= false;
							//MOVE MOUSE END------------------------------------------------------------------------------
					//END GLOBAL MAPPING
			    		  
			    		  }
			    		  catch (Exception e) {
			    			  System.out.println("Error: "+ e.getMessage());
			    		  }
							
			    	  }
			    	  			    	  
			    	  data="";
			      }
			      else {
			    	  data = data.concat(keyString);
			      }
			      
			      
			      
			      
			      
			      
			   }   
			   
			   if(isExists) in.close();
				   
			} catch (Exception e) { e.printStackTrace(); }
			comPort.closePort();
	}	
}	