package tollbridge.dnp3.outstation.sensors;

import lejos.hardware.Button;

/**
 * 
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public final class SensorButton extends Sensor {

	public SensorButton(int index) {
		m_analogIndex = index;
	}//constructor

	@Override
	void updateValue() {
		
		int buttons = 0;

		if (Button.UP.isDown()) {
			buttons += Button.ID_UP;
		}

		if (Button.ENTER.isDown()) {
			buttons += Button.ID_ENTER;
		}

		if (Button.DOWN.isDown()) {
			buttons += Button.ID_DOWN;
		}

		if (Button.RIGHT.isDown()) {
			buttons += Button.ID_RIGHT;

		}

		if (Button.LEFT.isDown()) {
			buttons += Button.ID_LEFT;
		}

		if (Button.ESCAPE.isDown()) {
			buttons += Button.ID_ESCAPE;
		}
		this.m_value = buttons;
	}

		
}