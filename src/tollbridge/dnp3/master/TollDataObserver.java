package tollbridge.dnp3.master;

import tollbridge.dnp3.master.device.Toll;
import tollbridge.dnp3.outstation.RTUToll;

import com.automatak.dnp3.AnalogInput;
import com.automatak.dnp3.AnalogOutputStatus;
import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.BinaryOutputStatus;
import com.automatak.dnp3.Counter;
import com.automatak.dnp3.DataObserver;

/**
 * DNP3 Data Observer for a Toll
 *     a Data Observer is invoked when a DNP3 message is 
 *     received from a RTU
 * @author Ken LE PRADO
 */
public class TollDataObserver implements DataObserver {
	private Toll m_Device;
	/**
	 * @param myDevice Device referring to
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

    /**
     * Call at the reception of :
     *    Function Code : update
     *    Object Type : BinaryInput
     * @param meas Object received
     * @param index Index
     */
    public void update(BinaryInput meas, long index)
    {
    	System.out.println("Binary: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());

    	switch ((int) index) {
    	case RTUToll.STATUS_BARRIER:
    		m_Device.getPanel().showTollBarrier(meas.getValue());
    		break;
	    }
    
    }

    /**
     * Call at the reception of :
     *    Function Code : update
     *    Object Type : AnalogInput
     * @param meas Object received
     * @param index Index
     */
    public void update(AnalogInput meas, long index)
    {
    	System.out.println("Analog: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());

    	switch ((int) index) {
    	case RTUToll.STATUS_UNIT_ID:
    		m_Device.getPanel().setDeviceLabel((int) meas.getValue());
    		break;
	    }

    }
    
    /**
     * Call at the reception of :
     *    Function Code : update
     *    Object Type : Counter
     * @param meas Object received
     * @param index Index
     */
    public void update(Counter meas, long index)
    {
    	System.out.println("Counter: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    	
    	switch ((int) index) {
    	case RTUToll.STATUS_NB_CARS:
    		m_Device.getPanel().getStatusCars().setText(Long.toString(meas.getValue()));
    		break;
    	case RTUToll.STATUS_NB_COINS:
	    	m_Device.getPanel().getStatusCoins().setText(Long.toString(meas.getValue()));
	    	break;
	    }
    	
    }

    /**
     * Call at the reception of :
     *    Function Code : update
     *    Object Type : BinaryOutputStatus
     * @param meas Object received
     * @param index Index
     */
    public void update(BinaryOutputStatus meas, long index)
    {
    	System.out.println("BinaryOutputStatus: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());
    	
    }

    /**
     * Call at the reception of :
     *    Function Code : update
     *    Object Type : AnalogOutputStatus
     * @param meas Object received
     * @param index Index
     */
    public void update(AnalogOutputStatus meas, long index)
    {
    	System.out.println("AnalogOutputStatus: " + meas.getValue() + " Index: " + index + " Timestamp: " + meas.getMsSinceEpoch());

		switch ((int) index) {
    	case RTUToll.STATUS_MODE:
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
