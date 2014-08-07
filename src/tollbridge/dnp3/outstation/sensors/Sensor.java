package tollbridge.dnp3.outstation.sensors;

/**
 * 
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public abstract class Sensor {
	protected int m_value;
	  		
	abstract void updateValue();

	public int getValue() {
		updateValue();
		return m_value;
	}

}