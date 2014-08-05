package tollbridge.dnp3.device.define;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import Ressources.ProcessImage;

import com.automatak.dnp3.Counter;
import com.automatak.dnp3.CounterInputQuality;
import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.EventAnalogResponse;
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
	private static final int STATUS_UNIT_ID = 0;
	private static final int STATUS_COIN_COLOR = 1;
	private static final int STATUS_CAR_PASSAGE = 2;
	private static final int STATUS_KEY_PRESS = 3;
	private static final int STATUS_CAR_PRESENTING = 4;

	//Input Binary
	private static final int STATUS_BARRIER = 0; 
	
	//Output Binary
	private static final int STATUS_ACTIVE = 0;	
	private static final int STATUS_FREE = 1;
	
	//Counter Inputs
	private static final int STATUS_NB_CARS = 0;
	private static final int STATUS_NB_COINS = 1;


	@Override
	public void initDnp3Config() {

        // Create the default outstation configuration
        dnp3Config = new OutstationStackConfig(new DatabaseConfig(1,5,2,0,0));

        dnp3Config.outstationConfig.staticAnalogInput = StaticAnalogResponse.GROUP30_VAR3; //Int 32 bits
        dnp3Config.outstationConfig.staticBinaryInput = StaticBinaryResponse.GROUP1_VAR2;
        dnp3Config.outstationConfig.staticCounter = StaticCounterResponse.GROUP20_VAR1;
        dnp3Config.outstationConfig.eventAnalogInput = EventAnalogResponse.GROUP32_VAR1;
        
        dnp3Config.outstationConfig.disableUnsol = false;
        

	}


	@Override
	public void initDnp3ProcImg() {
        procimg = new ProcessImage(dnp3Outstation);
        
        procimg.addBinaryInput(false); //STATUS_BARRIER
        
        procimg.addAnalogInput(this.dnp3UnitId); //STATUS_UNIT_ID
        procimg.addAnalogInput(0); //STATUS_COIN_COLOR
        procimg.addAnalogInput(0); //STATUS_CAR_PASSAGE
        procimg.addAnalogInput(0); //STATUS_KEY_PRESS
        procimg.addAnalogInput(0); //STATUS_CAR_PRESENTING 
        
        procimg.addCounter(0); //STATUS_NB_CARS
        procimg.addCounter(0); //STATUS_NB_COINS
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

        
        // all this stuff just to read a line of text in Java. Oh the humanity.
        String line = "";
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        
        int i = 0;
        while (true) {
            System.out.println("Enter something to update a counter or type <quit> to exit");
            try {
				line = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
            if(line.equals("quit")) break;
            else {
            	procimg.setBinaryInput(0, true);
            	procimg.incCounter(STATUS_NB_CARS);
            }
        }
  
        //None
        /*
        while (this.spi.getInputRegister(STATUS_KEY_PRESS).getValue() != 1) {
			
			drawScreen();
			
    		try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Is the toll activated ?
			if (this.spi.getDigitalOut(STATUS_ACTIVE).isSet() == true) {
				
				//Is the toll opened freely ?
				if (this.spi.getDigitalOut(STATUS_FREE).isSet() == true) {
					setDisplayColor(1); //Green
					barrierUp();
					//Increase the car counter
					if (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}
				} else {
					setDisplayColor(3); //Orange
					//Is a coin inserted and waiting for action ?
					coinColorId = this.spi.getInputRegister(STATUS_COIN_COLOR).getValue();
					
					if (refColorId != coinColorId) {
						System.out.println("New coin inserted");
						if (isValidCoin(coinColorId)) {
							System.out.println("Coin accepted, waiting a car...");

							eatCoin();
							barrierUp();
							drawScreen();
							
							//Wait a car to low the barrier
							while (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 0);
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
		*/
        setDisplayColor(0); //None
		barrierDown();
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {

		String status = "";
		
		status = "To fill";
		/*
		window.setBarrierStatus(this.spi.getDigitalIn(STATUS_BARRIER).isSet());

		status += "Coins inside : " + this.spi.getRegister(STATUS_NB_COINS).getValue() + "\n";
		status += "Cars viewed : " + this.spi.getRegister(STATUS_NB_CARS).getValue() + "\n";
		status += "-------------\n";
		status += "TOLL : " + this.spi.getInputRegister(STATUS_UNIT_ID).getValue() + "\n";
		status += "Coin color : " + this.spi.getInputRegister(STATUS_COIN_COLOR).getValue() + "\n";
		status += "Car passage : " + this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() + "\n";
		status += "Key : " + this.spi.getInputRegister(STATUS_KEY_PRESS).getValue() + "\n";
		status += "-------------\n";
		status += "Active : " + this.spi.getDigitalOut(STATUS_ACTIVE).isSet() + "\n";
		status += "Free : " + this.spi.getDigitalOut(STATUS_FREE).isSet() + "\n";
		status += "-------------\n";
		status += "Barrier up : " + this.spi.getDigitalIn(STATUS_BARRIER).isSet() + "\n";
		*/
		window.getStatus().setText(status);
		
		
	}
	
	/*
	 * Raise the barrier
	 */
	
	public void barrierUp() {
/*		if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == false) {
//			barrierMotor.rotate(+ BARRIER_ANGLE);
			this.spi.setDigitalIn(STATUS_BARRIER, new SimpleDigitalIn(true));
		}*/
	}

	/*
	 * Low the barrier
	 */
	public void barrierDown() {
	/*
		if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == true) {
//			barrierMotor.rotate(- BARRIER_ANGLE);		
			this.spi.setDigitalIn(STATUS_BARRIER, new SimpleDigitalIn(false));
		}*/
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
	/*
	public void eatCoin() {
		int nbCoins = this.spi.getRegister(STATUS_NB_COINS).getValue();
		this.spi.setRegister(STATUS_NB_COINS, new SimpleRegister(nbCoins + 1));

		setCoin(0);
	}*/
	
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
		this.procimg.incCounter(STATUS_NB_COINS);
		this.procimg.setAnalogInput(STATUS_COIN_COLOR, coinValue);

	}

	public void setExit() {
		System.out.println("Exit");
		this.procimg.setAnalogInput(STATUS_KEY_PRESS, 1);
		System.exit(0);
	}	
	
}