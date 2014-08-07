package tollbridge.dnp3.master;

import tollbridge.dnp3.master.device.Device;

import com.automatak.dnp3.AnalogInput;
import com.automatak.dnp3.AnalogOutputStatus;
import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.BinaryOutputStatus;
import com.automatak.dnp3.Counter;
import com.automatak.dnp3.DataObserver;

/**
 * Data Observer for a bridge
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public class BridgeDataObserver implements DataObserver {
	private Device m_Device;
	
	public BridgeDataObserver(Device myDevice) {
		m_Device = myDevice;
	}
	
    public void start()
    {
        System.out.println("Ystart");
    }

    public void update(BinaryInput meas, long index)
    {
    	System.out.println("YBinary: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    }

    public void update(AnalogInput meas, long index)
    {
    	System.out.println("YAnalog: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    }

    public void update(Counter meas, long index)
    {
    	System.out.println("YCounter: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    }

    public void update(BinaryOutputStatus meas, long index)
    {
    	System.out.println("YBinaryOutputStatus: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    }

    public void update(AnalogOutputStatus meas, long index)
    {
    	System.out.println("YAnalogOutputStatus: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    }

    public void end()
    {
    	System.out.println("Yend");
    }

}
