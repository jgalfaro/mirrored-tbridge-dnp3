package tollbridge.dnp3.master.device;

import com.automatak.dnp3.StackState;
import com.automatak.dnp3.StackStateListener;


/**
 * DNP3 Stack Listener
 * @author Ken LE PRADO
 */
public class StackStateDeviceListener implements StackStateListener {
	private Device m_Device;
	
	/**
	 * Constructor
	 * @param myDevice Object Panel corresponding to the device
	 */
	public StackStateDeviceListener (Device myDevice) {
		this.m_Device = myDevice;
	}
	
	/**
	 * Function called if the DNP3 Stack Status change
	 * @param state Status of the connection
	 */
	@Override
	public void onStateChange(StackState state) {
        m_Device.setStatus(state);
	}

}
