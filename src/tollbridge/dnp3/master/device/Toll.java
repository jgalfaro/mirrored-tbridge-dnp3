package tollbridge.dnp3.master.device;

import java.awt.Container;
import java.net.UnknownHostException;

import tollbridge.dnp3.master.TollDataObserver;
import tollbridge.dnp3.master.display.TollPanel;

import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.StackState;

public class Toll extends Device {
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

	
	public TollPanel panel;
	
	public Toll (String address, int port) throws UnknownHostException {
    	super(address, port);
	}

	public Toll (String address) throws UnknownHostException {
    	super(address);
	}

	public Toll () {
		super();
	}

	public void initPanel(Container container, int x, int y) {
		panel = new TollPanel(this, container, x, y);
	}
	
	public TollPanel getPanel() {
		return panel;
	}
	
	public void setStatusMode(int value) {
		this.operate(STATUS_MODE, value);
	}
	
	@Override
	public MasterStackConfig getDNP3Config() {
        // You can modify the defaults to change the way the master behaves
        MasterStackConfig config = new MasterStackConfig();
        config.masterConfig.integrityRateMs = 10000; //Update refresh
        config.masterConfig.enableUnsol = true;
		return config;
	}

	@Override
	DataObserver setDNP3DataObserver() {
		myDO = new TollDataObserver((Toll) this);
		return null;
	}

	@Override
	int getUnitId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void setStatus(StackState state) {
        System.out.println("********* * * * * * ***Master state: " + state);
        this.getPanel().changeStatus(state);
		this.status = state;
		
	}

}
