package tollbridge.dnp3.outstation;

import java.awt.Color;
import java.util.concurrent.TimeUnit;


/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 */
public class RTUTollSim extends RTUToll {
	public static TollSimFrame window;

	/**
	 * 
	 */
	public RTUTollSim(String deviceAddr, int dnp3Port, int dnp3UnitId) {
		super(deviceAddr, dnp3Port, dnp3UnitId);
	}


	
	@Override
	public void initEV3() {
		window = new TollSimFrame(this);
		window.setVisible(true);
	}
	
	/**
	 * EV3 Initialisation
	 */
	@Override
	public void loadEV3() {
		//Nothing to do
	}

	/**
	 * EV3 Close
	 */
	@Override
	public void stopEV3() {
		//Nothing to do
	}
	
	/**
	 * Thread to manage the device
	 */
	@Override
	public void run() {
		int refColorId = 0;
		int coinColorId = 0;
		
        setDisplayColor(0);
        
        while (this.procimg.getAnalogInput(RTUToll.STATUS_KEY_PRESS).getValue() != 1) {
			
			drawScreen();
			
    		try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Is the toll activated ?
			if (this.procimg.getAnalogOutput(RTUToll.STATUS_MODE).getValue() != RTUToll.MODE_OFF) {
				
				//Is the toll opened freely ?
				if (this.procimg.getAnalogOutput(RTUToll.STATUS_MODE).getValue() == RTUToll.MODE_FREE) {
					setDisplayColor(1); //Green
					barrierUp();
					//Increase the car counter
					if (this.procimg.getAnalogInput(RTUToll.STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}
				} else {
					setDisplayColor(3); //Orange
					//Is a coin inserted and waiting for action ?
					coinColorId = (int) this.procimg.getAnalogInput(RTUToll.STATUS_COIN_COLOR).getValue();
					
					if (refColorId != coinColorId) {
						System.out.println("New coin inserted");
						if (isValidCoin(coinColorId)) {
							System.out.println("Coin accepted, waiting a car...");

							eatCoin();
							barrierUp();
							drawScreen();
							
							//Wait a car to low the barrier
							while (this.procimg.getAnalogInput(RTUToll.STATUS_CAR_PASSAGE).getValue() == 0);
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

	/**
	 * draw screen
	 */
	public void drawScreen() {
		
		String status = "";
		
		status += "Coins inside : " + procimg.getCounter(RTUToll.STATUS_NB_COINS).getValue() + "\n";
		status += "Cars viewed : " + procimg.getCounter(RTUToll.STATUS_NB_CARS).getValue() + "\n";
		status += "-------------\n";
		status += "TOLL : " + procimg.getAnalogInput(RTUToll.STATUS_UNIT_ID).getValue() + "\n";
		status += "Coin color : " + procimg.getAnalogInput(RTUToll.STATUS_COIN_COLOR).getValue() + "\n";
		status += "Car passage : " + procimg.getAnalogInput(RTUToll.STATUS_CAR_PASSAGE).getValue() + "\n";
		status += "Key : " + procimg.getAnalogInput(RTUToll.STATUS_KEY_PRESS).getValue() + "\n";
		status += "-------------\n";
		status += "Mode : " + procimg.getAnalogOutput(RTUToll.STATUS_MODE).getValue() + "\n";
		status += "-------------\n";
		status += "Barrier up : " + procimg.getBinaryInput(RTUToll.STATUS_BARRIER).getValue() + "\n";
		
		window.setBarrierStatus(procimg.getBinaryInput(RTUToll.STATUS_BARRIER).getValue());
		window.getStatus().setText(status);
	}
	
	/**
	 * Raise the barrier
	 */
	
	public void barrierUp() {
		if (this.procimg.getBinaryInput(RTUToll.STATUS_BARRIER).getValue() == false) {
			this.procimg.setBinaryInput(RTUToll.STATUS_BARRIER, true);
		}
	}

	/**
	 * Low the barrier
	 */
	public void barrierDown() {
		if (this.procimg.getBinaryInput(RTUToll.STATUS_BARRIER).getValue() == true) {
			this.procimg.setBinaryInput(RTUToll.STATUS_BARRIER, false);
		}
	}


	/**
	 * Activate motors to eat inserted coin (and increase counter associated)
	 */
	public void eatCoin() {
		this.procimg.incCounter(RTUToll.STATUS_NB_COINS);

		setCoin(0);
	}
	
	/**
	 * Activate motors to reject inserted coin
	 */
	public void rejectCoin() {
		setCoin(0);
	}
	
	/**
	 * Increase counter if a car has gone 
	 */
	public void addCar() {
		this.procimg.incCounter(RTUToll.STATUS_NB_CARS);				
		this.procimg.setAnalogInput(RTUToll.STATUS_CAR_PASSAGE, 0);

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
		this.procimg.setAnalogInput(RTUToll.STATUS_CAR_PASSAGE, 1);
	}

	public void setCoin(int coinValue) {
		System.out.println("New coin " + coinValue);
		this.procimg.setAnalogInput(RTUToll.STATUS_COIN_COLOR, coinValue);

	}


	@Override
	public void associateSensors() {
		//No sensor to listen to
	}	
}