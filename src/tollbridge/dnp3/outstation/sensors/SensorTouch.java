package tollbridge.dnp3.outstation.sensors;

import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;


/**
 * 
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public final class SensorTouch extends Sensor {

	private EV3TouchSensor sensor;

	public SensorTouch(EV3TouchSensor mySensor) {
	    this.sensor = mySensor;
	}

	@Override
	void updateValue() {
		float[] touchSample = new float[this.sensor.sampleSize()];
		this.sensor.fetchSample(touchSample, 0);
		this.m_value = (int) touchSample[0]; 
	}
}