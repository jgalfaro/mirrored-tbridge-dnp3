package tollbridge.dnp3.outstation.sensors;

import java.util.Vector;


public class SensorUpdater extends Thread {
	private Vector<Sensor> m_sensors = null;
	
	public void addSensor(Sensor sensor) {
		m_sensors.addElement(sensor);
	}
	
	@Override
	public void run() {
		
		while (true) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e){}
		}
	}
}
