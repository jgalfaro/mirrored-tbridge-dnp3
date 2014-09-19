package tollbridge.dnp3.outstation.sensors;

import lejos.hardware.sensor.EV3ColorSensor;

/**
 * 
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public final class SensorColor extends Sensor {

	private EV3ColorSensor sensor;

	public SensorColor(EV3ColorSensor mySensor, int index) {
	    this.sensor = mySensor;
	    this.m_analogIndex = index;
	}

	@Override
	void updateValue() {
		this.m_value = sensor.getColorID();
	}
}