package tollbridge.dnp3.master.device;

import com.automatak.dnp3.StackState;
import com.automatak.dnp3.StackStateListener;

public class StackStateDeviceListener implements StackStateListener {
	private Device m_Device;
	
	public StackStateDeviceListener (Device myDevice) {
		this.m_Device = myDevice;
	}
	
	@Override
	public void onStateChange(StackState state) {
        m_Device.setStatus(state);
	}

}
