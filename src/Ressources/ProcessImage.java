package Ressources;

import java.util.Vector;

import com.automatak.dnp3.AnalogInput;
import com.automatak.dnp3.AnalogInputQuality;
import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.BinaryInputQuality;
import com.automatak.dnp3.Counter;
import com.automatak.dnp3.CounterInputQuality;
import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.Outstation;

public class ProcessImage {
	private Vector<BinaryInput> m_BinaryInput = null;
	private Vector<AnalogInput> m_AnalogInput = null;
	private Vector<Counter> m_Counter = null;
/*	private Vector m_outBinary = null;
	private Vector m_outAnalog = null;
*/	
	private DataObserver m_DataObserver = null;
	
	public ProcessImage(Outstation outstation) {
		m_DataObserver = outstation.getDataObserver();
		m_BinaryInput = new Vector<BinaryInput>();
		m_AnalogInput = new Vector<AnalogInput>();
		m_Counter = new Vector<Counter>();
/*		m_outBinary = new Vector();
		m_outAnalog = new Vector();*/
	}
	
	public void addBinaryInput(boolean value) {
		BinaryInput binIn = new BinaryInput(value, BinaryInputQuality.ONLINE.toByte(), now());
		m_BinaryInput.addElement(binIn);
		int ref = this.getBinaryInputCount() - 1;
		m_DataObserver.start();
		m_DataObserver.update(binIn, ref);
		m_DataObserver.end();
	}

	public void removeBinaryInput(BinaryInput BinIn) {
		m_BinaryInput.removeElement(BinIn);		
	}

	public void setBinaryInput(int ref, boolean value) {
		BinaryInput binIn = new BinaryInput(value, BinaryInputQuality.ONLINE.toByte(), now());
		m_BinaryInput.setElementAt(binIn, ref);			
		m_DataObserver.start();
		m_DataObserver.update(binIn, ref);
		m_DataObserver.end();
	}
	public BinaryInput getBinaryInput(int ref) {
		return m_BinaryInput.elementAt(ref);
	}

	public Integer getBinaryInputCount() {
		return m_BinaryInput.size();
	}
	

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
