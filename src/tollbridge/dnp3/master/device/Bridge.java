package tollbridge.dnp3.master.device;

import java.awt.Container;
import java.net.UnknownHostException;

import tollbridge.dnp3.master.display.BridgePanel;

import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.StackState;

public class Bridge extends Device {

	private static int BRIDGE_ANGLE = 12;

	//Discrete Input
	public static final int STATUS_BARRIER = 0;
	public static final int STATUS_WAITING_BOAT = 1;

	//Discrete Output
	public static final int STATUS_ACTIVE = 0;	
	public static final int STATUS_BRIDGE_MOVE = 1;
	public static final int STATUS_BRIDGE_RAISE = 2;
	public static final int STATUS_BARRIER_OPENED = 3;
	
	//Holding Register	
	public static final int STATUS_NB_CARS = 0;		
	
	//Input registers
	public static final int STATUS_UNIT_ID = 0;
	public static final int STATUS_SENSOR_BUTTON = 1;
	public static final int STATUS_SENSOR_GYRO = 2;
	public static final int STATUS_SENSOR_PASSAGE = 3;
	public static final int STATUS_SENSOR_BOAT = 4;
	public static final int STATUS_SENSOR_MOVE = 5;
	public static final int STATUS_SENSOR_ANGLE = 6;
	public static final int STATUS_BOAT_COLOR = 7;
	public static final int STATUS_BOAT_QUEUE = 8;

	
	public BridgePanel panel;

	public Bridge (String address, int port) throws UnknownHostException {
    	super(address, port);
	}

	public Bridge (String address) throws UnknownHostException {
    	super(address);
	}

	public Bridge () {
		super();
	}

	public void initPanel(Container container, int x, int y) {
		panel = new BridgePanel(this, container, x, y);
	}
	
	public BridgePanel getPanel() {
		return panel;
	}
	
	/*
	 * Return in degres the inclinaison of the bridge
	 */
	public int getBridgeAngle() {
		//TODO int path = getStatusBridgeAngle();
		int path = 0;
		int value = (int) path * 90 / BRIDGE_ANGLE;
		return value;
	}

	/*
	 * Return true if bridge is up
	 */	
	public boolean bridgeIsUp() {
		if (getBridgeAngle() > 75) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Return true if bridge is down
	 */	
	public boolean bridgeIsDown() {
		if (getBridgeAngle() < 5) {
			return true;
		} else {
			return false;
		}
	}	
	/*
	//Access to coils (Read / Write)
	public boolean getStatusActivate() {
		return this.getBoolRW(Bridge.STATUS_ACTIVE);
	}
	
	public void setStatusActivate(boolean value) {
		this.setBool(Bridge.STATUS_ACTIVE, value);
	}
	
	public boolean getStatusBridgeMove() {
		return this.getBoolRW(Bridge.STATUS_BRIDGE_MOVE);
	}
	
	public void setStatusBridgeMove(boolean value) {
		this.setBool(Bridge.STATUS_BRIDGE_MOVE, value);
	}
	
	public boolean getStatusBridgeRaise() {
		return this.getBoolRW(Bridge.STATUS_BRIDGE_RAISE);
	}
	
	public void setStatusBridgeRaise(boolean value) {
		this.setBool(Bridge.STATUS_BRIDGE_RAISE, value);
	}
	
	public boolean getStatusBarrierOpen() {
		return this.getBoolRW(Bridge.STATUS_BARRIER_OPENED);
	}
	
	public void setStatusBarrierOpen(boolean value) {
		this.setBool(Bridge.STATUS_BARRIER_OPENED, value);
	}

	//Read Discrete Inputs
	public boolean getStatusBarrier() {
		return this.getBoolRO(Bridge.STATUS_BARRIER);
	}

	public boolean getStatusWaitingBoat() {
		return this.getBoolRO(Bridge.STATUS_WAITING_BOAT);
	}
	
	
	//Input register
	public int getStatusKeyPress() {
		return this.getIntRO(Bridge.STATUS_SENSOR_BUTTON);
	}
	public int getStatusBridgeGyro() {
		return this.getIntRO(Bridge.STATUS_SENSOR_GYRO);		
	}	
	public int getStatusCarPassage() {
		return this.getIntRO(Bridge.STATUS_SENSOR_PASSAGE);
	}
	public int getStatusBoatWaiting() {
		return this.getIntRO(Bridge.STATUS_SENSOR_BOAT);
	}		
	*/
	/*
	 *  0 : stopped
	 * +1 : go up
	 * +2 : go bottom
	 */
	/*
	public int getBridgeMoveDirection() {
		return this.getIntRO(Bridge.STATUS_SENSOR_MOVE);
	}	
	
	public int getStatusBridgeAngle() {
		return this.getIntRO(Bridge.STATUS_SENSOR_ANGLE);		
	}	

	public int getStatusBoatColor() {
		return this.getIntRO(Bridge.STATUS_BOAT_COLOR);		
	}	

	public int getStatusBoatQueue() {
		return this.getIntRO(Bridge.STATUS_BOAT_QUEUE);		
	}	

	//Register
	public int getStatusNbCars() {
		return this.getIntRW(Bridge.STATUS_NB_CARS);
	}

	public void setStatusNbCars(int value) {
		this.setInt(Bridge.STATUS_NB_CARS, value);
	}
*/
	@Override
	MasterStackConfig getDNP3Config() {
        // You can modify the defaults to change the way the master behaves
        MasterStackConfig config = new MasterStackConfig();
        config.masterConfig.integrityRateMs = 10000; //Update refresh
        config.masterConfig.enableUnsol = true;
		return config;
	}

	@Override
	DataObserver setDNP3DataObserver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatus(StackState state) {
        System.out.println("********* * * * * * ***Master state: " + state);
        //TODO Add this to the Bridge Panel
       // this.getPanel().changeStatus(state);
		this.status = state;
		
	}
	
}
