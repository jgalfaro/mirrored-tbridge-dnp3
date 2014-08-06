package tollbridge.dnp3.master.display;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.automatak.dnp3.StackState;
import com.automatak.dnp3.StackStateListener;

import tollbridge.dnp3.master.device.Device;

public abstract class DevicePanel {
	protected int X;
	protected int Y;
	protected Device myDevice;
	
	protected JButton btnClose;
	protected JPanel content;
	protected Container container;

	public DevicePanel (Device dev, Container con, int x, int y) {
		super();
		container = con;
		myDevice = dev;
		X = x;
		Y = y;
		
		initPanel();
	}
	
	abstract void initPanel();

	abstract void removePanel();

	
	public void updatePanel(int x, int y) {
		X = x;
		Y = y;
		removePanel();
		initPanel();
	}
	
	public JPanel getContent() {
		return content;
	}
	
	public void changeStatus(StackState status) {
		if (status.equals(StackState.COMMS_UP)) {
			this.getContent().setBackground(Color.white);
			System.out.println("########## Change bg to UP");			
		} else if(status.equals(StackState.COMMS_DOWN)) {
			this.getContent().setBackground(Color.red);
			System.out.println("########## Change bg to DOWN");			
		} else if (status.equals(StackState.UNKNOWN)) {
			this.getContent().setBackground(Color.lightGray);
			System.out.println("########## Change bg to UNKNOWN");			
		}
	}
}
