package tollbridge.dnp3.outstation;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import Ressources.ProcessImage;

import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.EventAnalogResponse;
import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.StaticAnalogResponse;
import com.automatak.dnp3.StaticBinaryResponse;
import com.automatak.dnp3.StaticCounterResponse;

/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 */
public class TollSim extends Device {
	public TollSim(String deviceAddr, int dnp3Port, int dnp3UnitId) {
		super(deviceAddr, dnp3Port, dnp3UnitId);
	}
	public static TollSimFrame window;

	//Input Analog
	public static final int STATUS_UNIT_ID = 0;
	public static final int STATUS_COIN_COLOR = 1;
	public static final int STATUS_CAR_PASSAGE = 2;
	public static final int STATUS_KEY_PRESS = 3;
//	public static final int STATUS_CAR_PRESENTING = 4;

	//Input Binary
	public static final int STATUS_BARRIER = 0; 

	//Output Analog
	public static final int STATUS_MODE = 0;

	//Counter Inputs
	public static final int STATUS_NB_CARS = 0;
	public static final int STATUS_NB_COINS = 1;


	@Override
	public void initDnp3Config() {

        // Create the default outstation configuration
        dnp3Config = new OutstationStackConfig(new DatabaseConfig(1,5,2,0,1));

        dnp3Config.outstationConfig.staticAnalogInput = StaticAnalogResponse.GROUP30_VAR3; //Int 32 bits
        dnp3Config.outstationConfig.staticBinaryInput = StaticBinaryResponse.GROUP1_VAR2;
        dnp3Config.outstationConfig.staticCounter = StaticCounterResponse.GROUP20_VAR1;
        dnp3Config.outstationConfig.eventAnalogInput = EventAnalogResponse.GROUP32_VAR1;
        
        dnp3Config.outstationConfig.disableUnsol = false;
        
        // Create an Outstation instance, pass in a simple a command handler that responds successfully to everything
//        dnp3Outstation = dnp3Channel.addOutstation("outstation", LogLevel.INTERPRET, SuccessCommandHandler.getInstance(), dnp3Config);
        TollCommandHandler cmdHandler = new TollCommandHandler(this);
        dnp3Outstation = dnp3Channel.addOutstation("outstation", LogLevel.INTERPRET, cmdHandler, dnp3Config);

        procimg = new ProcessImage(dnp3Outstation);
        
        procimg.addBinaryInput(false); //STATUS_BARRIER
        
        procimg.addAnalogInput(this.dnp3UnitId); //STATUS_UNIT_ID
        procimg.addAnalogInput(0); //STATUS_COIN_COLOR
        procimg.addAnalogInput(0); //STATUS_CAR_PASSAGE
        procimg.addAnalogInput(0); //STATUS_KEY_PRESS
        procimg.addAnalogInput(0); //STATUS_CAR_PRESENTING 
        
        procimg.addCounter(0); //STATUS_NB_CARS
        procimg.addCounter(0); //STATUS_NB_COINS

        procimg.addAnalogOutput(0); //STATUS_MODE

	}
	
	@Override
	public void initEV3() {
		window = new TollSimFrame(this);
		window.setVisible(true);
	}
	
	/*
	 * EV3 Initialisation
	 */
	public void loadEV3() {
	}

	/*
	 * EV3 Close
	 */
	public void stopEV3() {
		//Nothing to do
	}
	
	/*
	 * Thread to manage the device
	 */
	public void run() {
		int refColorId = 0;
		int coinColorId = 0;
		
        setDisplayColor(0);
        
        while (this.procimg.getAnalogInput(STATUS_KEY_PRESS).getValue() != 1) {
			
			drawScreen();
			
    		try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Is the toll activated ?
			if (this.procimg.getAnalogOutput(STATUS_MODE).getValue() > 0) {
				
				//Is the toll opened freely ?
				if (this.procimg.getAnalogOutput(STATUS_MODE).getValue() == 1) {
					setDisplayColor(1); //Green
					barrierUp();
					//Increase the car counter
					if (this.procimg.getAnalogInput(STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}
				} else {
					setDisplayColor(3); //Orange
					//Is a coin inserted and waiting for action ?
					coinColorId = (int) this.procimg.getAnalogInput(STATUS_COIN_COLOR).getValue();
					
					if (refColorId != coinColorId) {
						System.out.println("New coin inserted");
						if (isValidCoin(coinColorId)) {
							System.out.println("Coin accepted, waiting a car...");

							eatCoin();
							barrierUp();
							drawScreen();
							
							//Wait a car to low the barrier
							while (this.procimg.getAnalogInput(STATUS_CAR_PASSAGE).getValue() == 0);
							addCar();
													
							barrierDown();
						}  else {
							System.out.println("Coin rejected");
							rejectCoin();
						}
					} else {
						barrierDown();
					}
				}
			} else {
				setDisplayColor(2); //Red
				//if barrier is up, we low it
				barrierDown();
			}
		}
		
        setDisplayColor(0); //None
		barrierDown();
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {

		String status = "";

		window.setBarrierStatus(procimg.getBinaryInput(STATUS_BARRIER).getValue());

		status += "Coins inside : " + procimg.getCounter(STATUS_NB_COINS).getValue() + "\n";
		status += "Cars viewed : " + procimg.getCounter(STATUS_NB_CARS).getValue() + "\n";
		status += "-------------\n";
		status += "TOLL : " + procimg.getAnalogInput(STATUS_UNIT_ID).getValue() + "\n";
		status += "Coin color : " + procimg.getAnalogInput(STATUS_COIN_COLOR).getValue() + "\n";
		status += "Car passage : " + procimg.getAnalogInput(STATUS_CAR_PASSAGE).getValue() + "\n";
		status += "Key : " + procimg.getAnalogInput(STATUS_KEY_PRESS).getValue() + "\n";
		status += "-------------\n";
		status += "Mode : " + procimg.getAnalogOutput(STATUS_MODE).getValue() + "\n";
		status += "-------------\n";
		status += "Barrier up : " + procimg.getBinaryInput(STATUS_BARRIER).getValue() + "\n";

		window.getStatus().setText(status);
		
		
	}
	
	/*
	 * Raise the barrier
	 */
	
	public void barrierUp() {
		if (this.procimg.getBinaryInput(STATUS_BARRIER).getValue() == false) {
//			barrierMotor.rotate(+ BARRIER_ANGLE);
			this.procimg.setBinaryInput(STATUS_BARRIER, true);
		}
	}

	/*
	 * Low the barrier
	 */
	public void barrierDown() {
		if (this.procimg.getBinaryInput(STATUS_BARRIER).getValue() == true) {
//			barrierMotor.rotate(- BARRIER_ANGLE);		
			this.procimg.setBinaryInput(STATUS_BARRIER, false);
		}
	}

	/*
	 * Return if the coin inserted is valid to pass
	 * @param coinColorId Color index of the coin inserted
	 * @return true if coin is accepted, else false 
	 */
	public boolean isValidCoin(int coinColorId) {
		if (coinColorId == 1)
			return true;
		else 
			return false;
	}
	
/*
 * Activate motors to eat inserted coin (and increase counter associated)
 */
	
	public void eatCoin() {
		this.procimg.incCounter(STATUS_NB_COINS);

		setCoin(0);
	}
	
	/*
	 * Activate motors to reject inserted coin
	 */
	public void rejectCoin() {
		setCoin(0);
	}
	
	/*
	 * Increase counter if a car has gone 
	 */
	public void addCar() {
		this.procimg.incCounter(STATUS_NB_CARS);
				
		// car_passage == 0 : wait for car to free the sensor
//		while (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1);
		this.procimg.setAnalogInput(STATUS_CAR_PASSAGE, 0);

	}

	@Override
	public void beep() {
		System.out.println("Beep !");
		window.beep();
	}
	
	public void setDisplayColor(int colorId) {
		switch (colorId) {
		case 0 :
			window.getStatus().setBackground(Color.WHITE);
			break;
		case 1 :
			window.getStatus().setBackground(Color.GREEN);
			break;
		case 2 :
			window.getStatus().setBackground(Color.RED);
			break;
		case 3 :
			window.getStatus().setBackground(Color.ORANGE);
			break;
		}
	}
	
	public void setNewCar() {
		System.out.println("Car is on");
		this.procimg.setAnalogInput(STATUS_CAR_PASSAGE, 1);
	}

	public void setCoin(int coinValue) {
		System.out.println("New coin " + coinValue);
		this.procimg.setAnalogInput(STATUS_COIN_COLOR, coinValue);

	}

	public void setExit() {
		System.out.println("Exit");
		this.procimg.setAnalogInput(STATUS_KEY_PRESS, 1);
	}	
	
}