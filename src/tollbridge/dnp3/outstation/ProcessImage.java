package tollbridge.dnp3.outstation;

import java.util.Vector;

import com.automatak.dnp3.AnalogInput;
import com.automatak.dnp3.AnalogInputQuality;
import com.automatak.dnp3.AnalogOutputStatus;
import com.automatak.dnp3.AnalogOutputStatusQuality;
import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.BinaryInputQuality;
import com.automatak.dnp3.BinaryOutputStatus;
import com.automatak.dnp3.BinaryOutputStatusQuality;
import com.automatak.dnp3.Counter;
import com.automatak.dnp3.CounterInputQuality;
import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.Outstation;

/**
 * Process Image
 * @author Ken LE PRADO ken@leprado.com
 *
 */
public class ProcessImage {
	private Vector<BinaryInput> m_BinaryInput = null;
	private Vector<AnalogInput> m_AnalogInput = null;
	private Vector<Counter> m_Counter = null;
	private Vector<BinaryOutputStatus> m_BinaryOutput = null;
	private Vector<AnalogOutputStatus> m_AnalogOutput = null;

	private DataObserver m_DataObserver = null;
	
	/**
	 * @param outstation Outstation Object
	 */
	public ProcessImage(Outstation outstation) {
		m_DataObserver = outstation.getDataObserver();
		m_BinaryInput = new Vector<BinaryInput>();
		m_AnalogInput = new Vector<AnalogInput>();
		m_Counter = new Vector<Counter>();
		m_BinaryOutput = new Vector<BinaryOutputStatus>();
		m_AnalogOutput = new Vector<AnalogOutputStatus>();
	}
	
	/**
	 * Add a Binary Input
	 * @param value
	 */
	public void addBinaryInput(boolean value) {
		BinaryInput binIn = new BinaryInput(value, BinaryInputQuality.ONLINE.toByte(), now());
		m_BinaryInput.addElement(binIn);
		int ref = this.getBinaryInputCount() - 1;
		m_DataObserver.start();
		m_DataObserver.update(binIn, ref);
		m_DataObserver.end();
	}

	/**
	 * Remove a Binary Input
	 * @param BinIn Binary Input Object
	 */
	public void removeBinaryInput(BinaryInput BinIn) {
		m_BinaryInput.removeElement(BinIn);		
	}

	/**
	 * set a Binary Input value
	 * @param ref Index of the value
	 * @param value Value
	 */
	public void setBinaryInput(int ref, boolean value) {
		//Any modification ?
		if (getBinaryInput(ref).getValue() == value) {
			return;
		}
		BinaryInput binIn = new BinaryInput(value, BinaryInputQuality.ONLINE.toByte(), now());
		m_BinaryInput.setElementAt(binIn, ref);			
		m_DataObserver.start();
		m_DataObserver.update(binIn, ref);
		m_DataObserver.end();
	}
	
	/**
	 * Return the value of the binary
	 * @param ref Index of the value
	 * @return Value
	 */
	public BinaryInput getBinaryInput(int ref) {
		return m_BinaryInput.elementAt(ref);
	}

	/**
	 * Number of binary objects
	 * @return count the binary objects
	 */
	public Integer getBinaryInputCount() {
		return m_BinaryInput.size();
	}
	

	//Analog Input
	
	public void addAnalogInput(double value) {
		AnalogInput anaIn = new AnalogInput(value, AnalogInputQuality.ONLINE.toByte(), now());
		m_AnalogInput.addElement(anaIn);
		int ref = this.getAnalogInputCount() - 1;
		m_DataObserver.start();
		m_DataObserver.update(anaIn, ref);
		m_DataObserver.end();
	}

	public void removeAnalogInput(AnalogInput AnaIn) {
		m_AnalogInput.removeElement(AnaIn);		
	}

	public void setAnalogInput(int ref, double value) {
		//Any modification ?
		if (getAnalogInput(ref).getValue() == value) {
			return;
		}		
		AnalogInput anaIn = new AnalogInput(value, AnalogInputQuality.ONLINE.toByte(), now());
		m_AnalogInput.setElementAt(anaIn, ref);			
		m_DataObserver.start();
		m_DataObserver.update(anaIn, ref);
		m_DataObserver.end();
	}
	public AnalogInput getAnalogInput(int ref) {
		return m_AnalogInput.elementAt(ref);
	}

	public Integer getAnalogInputCount() {
		return m_AnalogInput.size();
	}
	
	
	//Counter
	public void addCounter(long value) {
		Counter cpt = new Counter(value, CounterInputQuality.ONLINE.toByte(), now());
		m_Counter.addElement(cpt);
		int ref = this.getCounterCount() - 1;
		m_DataObserver.start();
		m_DataObserver.update(cpt, ref);
		m_DataObserver.end();
	}

	public void removeCounter(Counter counter) {
		m_Counter.removeElement(counter);		
	}

	public void setCounter(int ref, long value) {
		//Any modification ?
		if (getCounter(ref).getValue() == value) {
			return;
		}		
		Counter cpt = new Counter(value, CounterInputQuality.ONLINE.toByte(), now());
		m_Counter.setElementAt(cpt, ref);			
		m_DataObserver.start();
		m_DataObserver.update(cpt, ref);
		m_DataObserver.end();
	}

	public void incCounter(int ref) {
		long value = this.getCounter(ref).getValue() + 1;
		Counter cpt = new Counter(value, CounterInputQuality.ONLINE.toByte(), now());
		m_Counter.setElementAt(cpt, ref);			
		m_DataObserver.start();
		m_DataObserver.update(cpt, ref);
		m_DataObserver.end();
	}

	public Counter getCounter(int ref) {
		return m_Counter.elementAt(ref);
	}

	public Integer getCounterCount() {
		return m_Counter.size();
	}
	
	
	//Output Binary
	public void addBinaryOutput(boolean value) {
		BinaryOutputStatus binOut = new BinaryOutputStatus(value, BinaryOutputStatusQuality.ONLINE.toByte(), now());
		m_BinaryOutput.addElement(binOut);
		int ref = this.getBinaryOutputCount() - 1;
		m_DataObserver.start();
		m_DataObserver.update(binOut, ref);
		m_DataObserver.end();
	}

	public void removeBinaryOutput(BinaryOutputStatus BinOut) {
		m_BinaryOutput.removeElement(BinOut);		
	}

	public void setBinaryOutput(int ref, boolean value) {
		//Any modification ?
		if (getBinaryOutput(ref).getValue() == value) {
			return;
		}		
		BinaryOutputStatus binOut = new BinaryOutputStatus(value, BinaryOutputStatusQuality.ONLINE.toByte(), now());
		m_BinaryOutput.setElementAt(binOut, ref);			
		m_DataObserver.start();
		m_DataObserver.update(binOut, ref);
		m_DataObserver.end();
	}
	public BinaryOutputStatus getBinaryOutput(int ref) {
		return m_BinaryOutput.elementAt(ref);
	}

	public Integer getBinaryOutputCount() {
		return m_BinaryOutput.size();
	}
	
	

	//Analog Input
	
	public void addAnalogOutput(double value) {
		AnalogOutputStatus anaOut = new AnalogOutputStatus(value, AnalogOutputStatusQuality.ONLINE.toByte(), now());
		m_AnalogOutput.addElement(anaOut);
		int ref = this.getAnalogOutputCount() - 1;
		m_DataObserver.start();
		m_DataObserver.update(anaOut, ref);
		m_DataObserver.end();
	}

	public void removeAnalogOutput(AnalogOutputStatus AnaOut) {
		m_AnalogOutput.removeElement(AnaOut);		
	}

	public void setAnalogOutput(int ref, double value) {
		//Any modification ?
		if (getAnalogOutput(ref).getValue() == value) {
			return;
		}		
		AnalogOutputStatus anaOut = new AnalogOutputStatus(value, AnalogOutputStatusQuality.ONLINE.toByte(), now());
		m_AnalogOutput.setElementAt(anaOut, ref);			
		m_DataObserver.start();
		m_DataObserver.update(anaOut, ref);
		m_DataObserver.end();
	}
	public AnalogOutputStatus getAnalogOutput(int ref) {
		return m_AnalogOutput.elementAt(ref);
	}

	public Integer getAnalogOutputCount() {
		return m_AnalogOutput.size();
	}
	
	
	//Other functions
	
	/**
	 * Get the current timestamp
	 * @return
	 */
	private long now() {
        return System.currentTimeMillis();
        
	}

	/**
	 * Display current values of the process image
	 */
	public void print() {
		int i = 0;
		for (i=0; i<this.getBinaryInputCount(); i++) {
			System.out.println("Binary Input [" + i + "] : " + this.getBinaryInput(i).getValue());
		}

		for (i=0; i<this.getAnalogInputCount(); i++) {
			System.out.println("Analog Input [" + i + "] : " + this.getAnalogInput(i).getValue());
		}

		for (i=0; i<this.getCounterCount(); i++) {
			System.out.println("Counter [" + i + "] : " + this.getCounter(i).getValue());
		}
		
	}
}
