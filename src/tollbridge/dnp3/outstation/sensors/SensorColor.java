package tollbridge.dnp3.outstation.sensors;

import lejos.hardware.sensor.EV3ColorSensor;

/**
 * 
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public final class SensorColor extends Sensor {

	private EV3ColorSensor sensor;

	public SensorColor(EV3ColorSensor mySensor) {
	    this.sensor = mySensor;
	}

	@Override
	void updateValue() {
		this.m_value = sensor.getColorID();
	}
}