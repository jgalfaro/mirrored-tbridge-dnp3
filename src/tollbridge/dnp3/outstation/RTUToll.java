package tollbridge.dnp3.outstation;

import ressources.ProcessImage;

import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.EventAnalogResponse;
import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.StaticAnalogResponse;
import com.automatak.dnp3.StaticBinaryResponse;
import com.automatak.dnp3.StaticCounterResponse;

public abstract class RTUToll extends RTUDevice {
	public static final int MODE_OFF = 0;
	public static final int MODE_FREE = 1;
	public static final int MODE_PAY = 2;


	//Input Analog
	public static final int STATUS_UNIT_ID = 0;
	public static final int STATUS_COIN_COLOR = 1;
	public static final int STATUS_CAR_PASSAGE = 2;
	public static final int STATUS_KEY_PRESS = 3;
	public static final int STATUS_CAR_PRESENTING = 4;

	//Input Binary
	public static final int STATUS_BARRIER = 0; 

	//Output Analog
	public static final int STATUS_MODE = 0;

	//Counter Inputs
	public static final int STATUS_NB_CARS = 0;
	public static final int STATUS_NB_COINS = 1;
	
	public RTUToll(String deviceAddr, int dnp3Port, int dnp3UnitId) {
		super(deviceAddr, dnp3Port, dnp3UnitId);
	}
	
	@Override
	public void initDnp3Config() {
		// TODO Auto-generated method stub

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
	abstract public void initEV3();
	
	@Override
	abstract public void loadEV3();
	
	@Override
	abstract public void stopEV3();
	
	@Override
	abstract public void run();

	@Override
	abstract public void beep();

	abstract public void drawScreen();

	abstract public void barrierUp();
	abstract public void barrierDown();

	abstract public void eatCoin();
	abstract public void rejectCoin();

	abstract public void addCar();

	public void setExit() {
		this.procimg.setAnalogInput(RTUToll.STATUS_KEY_PRESS, 1);
	}
	
	/**
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
	
}
