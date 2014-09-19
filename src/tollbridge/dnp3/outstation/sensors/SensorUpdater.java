package tollbridge.dnp3.outstation.sensors;

import java.util.Enumeration;
import java.util.Vector;

import tollbridge.dnp3.outstation.ProcessImage;

/**
 * Class that permit to update dynamically the 
 * Device's processimage
 * @author Ken LE PRADO
 *
 */
public class SensorUpdater extends Thread {
	private ProcessImage m_procimg = null;
	private Vector<Sensor> m_sensors = null;
	private boolean runThread = true;
	
	public SensorUpdater(ProcessImage procimg) {
		super();
		m_procimg = procimg;
		m_sensors = new Vector<Sensor>();
	}
	/**
	 * Add a sensor to the sensor to listen
	 * @param sensor Sensor listened
	 * @param analogInputIndex index of the register to update
	 * 
	 */
	public void addSensor(Sensor sensor) {
		m_sensors.addElement(sensor);
	}

	@Override
	public void run() {
		int value = 0;
		while (runThread == true) {
			Enumeration<Sensor> vectorElementsEnum = m_sensors.elements();
		    while(vectorElementsEnum.hasMoreElements()) {
		    	value = vectorElementsEnum.nextElement().getValue();
		    	m_procimg.setAnalogInput(vectorElementsEnum.nextElement().m_analogIndex, value);
		    	System.out.println("New value : " + value);
		    }

		    System.out.println("Has polled sensor updates");
			
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e){}
		}
	}
	
	/**
	 * Stop the thread
	 */
	public void stopCheck() {
		runThread = false;
	}
}
