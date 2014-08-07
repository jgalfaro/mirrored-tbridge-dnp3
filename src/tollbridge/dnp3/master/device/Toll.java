package tollbridge.dnp3.master.device;

import java.awt.Container;
import java.net.UnknownHostException;

import tollbridge.dnp3.master.TollDataObserver;
import tollbridge.dnp3.master.display.TollPanel;
import tollbridge.dnp3.outstation.RTUToll;

import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.StackState;

public class Toll extends Device {
	
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
		this.operate(RTUToll.STATUS_MODE, value);
	}
	
	@Override
	public MasterStackConfig getDNP3Config() {
        // You can modify the defaults to change the way the master behaves
        MasterStackConfig config = new MasterStackConfig();
        config.masterConfig.integrityRateMs = 3000; //Update refresh
        config.masterConfig.enableUnsol = true;
		return config;
	}

	@Override
	DataObserver setDNP3DataObserver() {
		myDO = new TollDataObserver((Toll) this);
		return null;
	}
	
	@Override
	public void setStatus(StackState state) {
        this.getPanel().changeStatus(state);
		this.status = state;
	}

}
