package tollbridge.dnp3.outstation.sensors;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * 
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public final class SensorUltrasonic extends Sensor {

	private EV3UltrasonicSensor sensor;
	private float[] distance = { 0 };
	private SampleProvider distanceProvider;

	public SensorUltrasonic(EV3UltrasonicSensor mySensor) {
	    this.sensor = mySensor;
	    
        this.distanceProvider = sensor.getDistanceMode();

	}

	@Override
	void updateValue() {
		distanceProvider.fetchSample(distance, 0);
		
		int value = (int)(distance[0] * 100); 

		this.m_value = value;
	}
}
