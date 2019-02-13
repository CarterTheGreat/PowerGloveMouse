package main;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.*;

final class Main{
	
	//Begin Input Control
	
	static char key;
	static String keyString;
	static String data = "";
	
	static int ind0;
	static int ind1;
	static int ind2;
	static int ind3;
	static int ind4;
	static int ind5;
	static int ind6;
	//End Input Control 
	
	
	//Begin Mouse
	
	static int accelX;
	static int accelXLast;
	static int accelY;
	static int accelYLast;
	
	static int accelZ;
	static int accelZLast;
	static int accelZLastLast;
	static boolean tapPhaseOne;
	static boolean tapped;
	
	static int flex1;
	static int flex2;
	
	static int xDir;
	static int yDir;
	
	static Robot mouse;	
	
	static int xThresholdPos = 70;
	static int xThresholdNeg = 55;
	static int yThresholdPos = 30;
	static int yThresholdNeg = 60;
	//End Mouse
	
	//Begin Com
	
	static SerialPort comPort;
	static InputStream in;
	
	static boolean isExists = true;
	static boolean autoConnect = true;
	static int autoRead = 1;
	//End Com
	
	//Begin Control Panel
	
	static ControlFrame controlFrame;
	static boolean running = true;
	//End Control Panel
	
	public static void main(String[] args) throws IOException {
		
		
		
	//COM------------------------------------------------------------------------------------------	
		//COM opening and automatic assignment
		System.out.println("Auto Connect Attempt");
		comPort = SerialPort.getCommPorts()[0];
		System.out.println("Com Port Chosen Automaticly: "+comPort);
		comPort.openPort();
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		
		in = comPort.getInputStream();
		//End automatic
		
		try {
			autoRead = in.read();
			System.out.println("Auto read : "+autoRead);
			System.out.println("Input read auto");
			
		} catch (Exception e1) {
			System.out.println("Auto Connect Failed");
			autoConnect = false;
			comPort.closePort();
		}
		
		//COM opening and manual assignment
		if(!autoConnect || autoRead == 0) {
			System.out.println("Manual Connect Attempt");
			String comPortS = JOptionPane.showInputDialog("Auto Connect Failed \nEnter Com Port Glove Is Connected To \nEx. COM4 , COM15");
			comPort = SerialPort.getCommPort(comPortS);		
			comPort.openPort();
			comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
	
			in = comPort.getInputStream();
		
		//End manual
		
		//Test for if manual connected correctly
			try {
				in.read();
				System.out.println("Input read manual");
			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("Could not create input stream");
				JOptionPane.showMessageDialog(null, "Input Stream Could Not Be Created\nLikely Wrong COM Port Input or Port Already In Use");
				comPort.closePort();
				System.exit(0);
				isExists = false;
			}
		//End Test
		}
		System.out.println("Finished port opening and input stream created");
		
	//Control Panel-----------------------------------------------------------------------------------	
		
		controlFrame = new ControlFrame();
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.setVisible(true);
        controlFrame.setTitle("Dot Evolution");
        controlFrame.setLocationRelativeTo(null);
		
		
		
		
		
			try
			{
				//Mouse control object
				mouse = new Robot();
				
			//Mouse control all within this while loop	
			   while(isExists && running) {
			      
				
				  //Input Stream assignment and counting 
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
			    	  
			    	  if(data.contains("<") && data.contains(">") && slashes == 5 ) {
			    		
			    		  try { 
			    			  
			    		  System.out.println(data);
			    		  
			    		//INPUT ALLOCATION-------------------------------------------------------------------------
							//Data in format, length of x, y, and z vary from ones place to hundreds place Ex.
							//<on/xxx/yyy/zzz/f1/f2>
								
							//Data from 	
							ind0 = data.indexOf('<');
							ind1 = data.indexOf('/',0);
							ind2 = data.indexOf('/',ind1+1);
							ind3 = data.indexOf('/',ind2+1);
							ind4 = data.indexOf('/',ind3+1);
							ind5 = data.indexOf('/',ind4+1);
							ind6 = data.indexOf('>');
							
							//System.out.println("ind "+ ind1+" "+ind2);
							
							//AccelValue as string
							String x = data.substring(ind1+1,ind2);
							String y = data.substring(ind2+1,ind3);	
							String z = data.substring(ind3+1,ind4);
								
							String f1 = data.substring(ind4+1,ind5);
							String f2 = data.substring(ind5+1,ind6);
							//System.out.println("String x, y, & z "+x+ " " +y+" "+z);
							
							//Accel value as int
							accelX = Integer.parseInt(x);
							accelY = Integer.parseInt(y);
							accelZ = Integer.parseInt(z);
							
							flex1 = Integer.parseInt(f1);
							flex2 = Integer.parseInt(f2);
							//System.out.println("Accel x y & z "+accelX+" "+accelY+" "+accelZ);
							
							if(accelX < xThresholdPos && accelX > -xThresholdNeg) {
								accelX=0;
							}
							if(accelY < yThresholdPos && accelY > -yThresholdNeg) {
								accelY=0;
							}
							//INPUT ALLOCATION END--------------------------------------------------------------------
							
							
							
							//Tap functionality-----------------------------------------------------------------------
																										
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
							if(flex1 > 150) {
								mouse.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							}else mouse.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
							mouse.mouseMove(mouseX+xDir,mouseY-yDir);
							
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
			   
			   
			   //RIP
			   if(!isExists) in.close(); comPort.closePort(); System.exit(0);
			   
			} catch (Exception e) { e.printStackTrace(); }
			
			in.close(); comPort.closePort(); System.exit(0);
	}	
}	