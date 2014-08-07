package tollbridge.dnp3.outstation;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 */
public class RTUTollEV3 extends RTUToll {
	public RTUTollEV3(String deviceAddr, int modbusPort, int modbusUnitId) {
		super(deviceAddr, modbusPort, modbusUnitId);
	}
	public EV3 ev3 = null;

	private RegulatedMotor barrierMotor = null;
	private RegulatedMotor coinMotor = null;
	private EV3ColorSensor coinColorSensor = null;
	private EV3TouchSensor passageTouchSensor = null;
	private EV3UltrasonicSensor distanceUSSensor = null;
	
	private static int BARRIER_ANGLE = 80;
	private static int COIN_ANGLE = 2200;
	

	/*
	 * EV3 Initialisation
	 */
	public void initEV3() {		
		this.ev3 = (EV3) BrickFinder.getDefault();
		this.loadEV3();
	}

	/*
	 * EV3 Initialisation
	 */
	public void loadEV3() {
		
		barrierMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		barrierMotor.setSpeed(60);
		
		coinMotor = new EV3MediumRegulatedMotor(MotorPort.A);
		coinMotor.setSpeed(2000);
		
		coinColorSensor = new EV3ColorSensor(SensorPort.S1);
		passageTouchSensor = new EV3TouchSensor(SensorPort.S2);
		distanceUSSensor = new EV3UltrasonicSensor(SensorPort.S3);
	}

	/**
	 * EV3 Close
	 */
	public void stopEV3() {		
		barrierMotor.close();
		coinMotor.close();
		coinColorSensor.close();
		passageTouchSensor.close();
		distanceUSSensor.close();
	}
	
	/**
	 * Thread to manage the device
	 */
	public void run() {
		int refColorId = coinColorSensor.getColorID();
		int coinColorId = 0;
		int dist = 0;
		
        while (this.procimg.getAnalogInput(RTUToll.STATUS_KEY_PRESS).getValue() != 1) {
			
			drawScreen();
			Delay.msDelay(200);
			
			//Is the toll activated ?
			if (this.procimg.getAnalogOutput(RTUToll.STATUS_MODE).getValue() != RTUToll.MODE_OFF) {
				
				//Is the toll opened freely ?
				if (this.procimg.getAnalogOutput(RTUToll.STATUS_MODE).getValue() == RTUToll.MODE_FREE) {
					Button.LEDPattern(1); //Green
					barrierUp();
					//Increase the car counter
					if (this.procimg.getAnalogInput(RTUToll.STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}
				} else {
					Button.LEDPattern(3); //Orange
					//Is a coin inserted and waiting for action ?
					coinColorId = coinColorSensor.getColorID();
					dist = (int) this.procimg.getAnalogInput(RTUToll.STATUS_CAR_PRESENTING).getValue();
					if (refColorId != coinColorId && dist < 15) {
						//System.out.println("New coin");
						if (isValidCoin(coinColorId)) {
							//System.out.println("Coin accepted");
							eatCoin();
							barrierUp();
							
							//Wait a car to low the barrier
							while (this.procimg.getAnalogInput(RTUToll.STATUS_CAR_PASSAGE).getValue() == 0);
							addCar();
							//some time for the car to go away
							Delay.msDelay(500);
													
							barrierDown();
						}  else {
							//System.out.println("Coin rejected");
							rejectCoin();
						}
					} else {
						barrierDown();
					}
				}
			} else {
				Button.LEDPattern(2); //Red
				//if barrier is up, we low it
				barrierDown();
			}
		}
        Button.LEDPattern(0); //None
		barrierDown();
	}

	/**
	 * draw screen
	 */
	public void drawScreen() {
		LCD.clearDisplay();
		LCD.drawString("TOLL " + procimg.getAnalogInput(RTUToll.STATUS_UNIT_ID).getValue(), 4, 0);
		LCD.drawString("Coins inside : " + procimg.getCounter(RTUToll.STATUS_NB_COINS).getValue(), 1, 2);
		LCD.drawString("Cars viewed  : " + procimg.getCounter(RTUToll.STATUS_NB_CARS).getValue(), 1, 3);
		LCD.drawString("Car Dist (cm): " + procimg.getAnalogInput(RTUToll.STATUS_CAR_PRESENTING).getValue(), 1, 4);
		
		LCD.drawString("ESC Exit", 7, 6);
	}
	
	/**
	 * Raise the barrier
	 */
	public void barrierUp() {
		if (this.procimg.getBinaryInput(RTUToll.STATUS_BARRIER).getValue() == false) {
			barrierMotor.rotate(+ BARRIER_ANGLE);
			this.procimg.setBinaryInput(RTUToll.STATUS_BARRIER, true);
		}
	}

	/**
	 * Low the barrier
	 */
	public void barrierDown() {
		if (this.procimg.getBinaryInput(RTUToll.STATUS_BARRIER).getValue() == true) {
			barrierMotor.rotate(- BARRIER_ANGLE);		
			this.procimg.setBinaryInput(RTUToll.STATUS_BARRIER, false);
		}
	}

/**
 * Activate motors to eat inserted coin (and increase counter associated)
 */
	public void eatCoin() {
		this.procimg.incCounter(RTUToll.STATUS_NB_COINS);

		coinMotor.rotate(+ RTUTollEV3.COIN_ANGLE);
		coinMotor.rotate(- RTUTollEV3.COIN_ANGLE);
	}
	
	/**
	 * Activate motors to reject inserted coin
	 */
	public void rejectCoin() {
		coinMotor.rotate(- RTUTollEV3.COIN_ANGLE);
		coinMotor.rotate(+ RTUTollEV3.COIN_ANGLE);
	}
	
	/**
	 * Increase counter if a car has gone 
	 */
	public void addCar() {
		this.procimg.incCounter(RTUToll.STATUS_NB_CARS);

		// car_passage == 0 : wait for car to free the sensor
		while (this.procimg.getAnalogInput(STATUS_CAR_PASSAGE).getValue() == 1);
	}
	/**
	 * Sounds a beep
	 */
	public void beep() {
		Audio audio = this.ev3.getAudio();
		audio.systemSound(0);
	}

}