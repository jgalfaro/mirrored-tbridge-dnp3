package tollbridge.dnp3.outstation.sensors;

import java.util.Enumeration;
import java.util.Vector;


public class SensorUpdater extends Thread {
	private Vector<Sensor> m_sensors = null;
	private boolean runThread = true;
	
	/**
	 * Add a sensor to the sensor to listen
	 * @param sensor
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
//TODO Update the correct register
		    	System.out.println(value);
		    }
		    
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
