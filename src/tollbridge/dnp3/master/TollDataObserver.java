package tollbridge.dnp3.master;

import tollbridge.dnp3.master.device.Toll;

import com.automatak.dnp3.AnalogInput;
import com.automatak.dnp3.AnalogOutputStatus;
import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.BinaryOutputStatus;
import com.automatak.dnp3.Counter;
import com.automatak.dnp3.DataObserver;

/**
 * Data Observer for a Toll
 * @author Ken LE PRADO
 *
 */
public class TollDataObserver implements DataObserver {
	private Toll m_Device;
	/**
	 * @param myDevice Device refering to
	 */
	public TollDataObserver(Toll myDevice) {
		m_Device = myDevice;
	}
	
	/**
	 * Call at the beginning of the DNP3 transaction
	 */
    public void start()
    {
        System.out.println("Start transaction");
    }

    public void update(BinaryInput meas, long index)
    {
    	System.out.println("Binary: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());

    	switch ((int) index) {
    	case Toll.STATUS_BARRIER:
    		m_Device.getPanel().showTollBarrier(meas.getValue());
    		break;
	    }
    
    }

    public void update(AnalogInput meas, long index)
    {
    	System.out.println("Analog: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());

    	switch ((int) index) {
    	case Toll.STATUS_UNIT_ID:
    		m_Device.getPanel().setDeviceLabel((int) meas.getValue());
    		break;
	    }

    }

    public void update(Counter meas, long index)
    {
    	System.out.println("Counter: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    	
    	switch ((int) index) {
    	case Toll.STATUS_NB_CARS:
    		m_Device.getPanel().getStatusCars().setText(Long.toString(meas.getValue()));
    		break;
    	case Toll.STATUS_NB_COINS:
	    	m_Device.getPanel().getStatusCoins().setText(Long.toString(meas.getValue()));
	    	break;
	    }
    	
    }

    public void update(BinaryOutputStatus meas, long index)
    {
    	System.out.println("BinaryOutputStatus: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    	
    }

    public void update(AnalogOutputStatus meas, long index)
    {
    	System.out.println("AnalogOutputStatus: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());

		switch ((int) index) {
    	case Toll.STATUS_MODE:
    		m_Device.getPanel().showTollMode((int) meas.getValue());
    		break;
	    }
    }

	/**
	 * Call at the end of the DNP3 transaction
	 */
    public void end()
    {
    	System.out.println("End transaction");
    }

}
