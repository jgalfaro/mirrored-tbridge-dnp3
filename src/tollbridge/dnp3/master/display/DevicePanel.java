package tollbridge.dnp3.master.display;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.automatak.dnp3.StackState;

import tollbridge.dnp3.master.device.Device;

/**
 * Device Panel
 * @author Ken LE PRADO ken@leprado.com
 */
public abstract class DevicePanel {
	protected int X;
	protected int Y;
	protected Device myDevice;
	
	protected JButton btnClose;
	protected JPanel content;
	protected Container container;

	/**
	 * Constructor of a device
	 * 
	 * @param dev Device displayed
	 * @param con Container
	 * @param x X position
	 * @param y Y position
	 */
	public DevicePanel (Device dev, Container con, int x, int y) {
		super();
		container = con;
		myDevice = dev;
		X = x;
		Y = y;
		
		initPanel();
	}
	
	/**
	 * Initiate the panel
	 */
	abstract void initPanel();

	/**
	 * Remove a panel
	 */
	abstract void removePanel();

	/**
	 * Set the label of the Device
	 * @param unitId Unit Identifier
	 */
	abstract void setDeviceLabel(int unitId);
	
	/**
	 * Redisplay a panel
	 * @param x X position
	 * @param y Y position
	 */
	public void updatePanel(int x, int y) {
		X = x;
		Y = y;
		removePanel();
		initPanel();
	}
	

	/**
	 * Clears the status of the device, waiting the real data to refresh the display
	 */
	abstract public void clearStatus();

	
	/**
	 * Access to the panel
	 * @return Content panel
	 */
	public JPanel getContent() {
		return content;
	}
	
	/**
	 * Displays the status of the device
	 * @param status Device status
	 */
	public void changeStatus(StackState status) {
		if (status.equals(StackState.COMMS_UP)) {
			this.getContent().setBackground(Color.white);
		} else if(status.equals(StackState.COMMS_DOWN)) {
			this.getContent().setBackground(Color.red);
		} else if (status.equals(StackState.UNKNOWN)) {
			this.getContent().setBackground(Color.lightGray);
		}
	}
}
