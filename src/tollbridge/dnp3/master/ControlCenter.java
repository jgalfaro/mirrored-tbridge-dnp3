package tollbridge.dnp3.master;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.automatak.dnp3.DNP3Manager;
import com.automatak.dnp3.impl.DNP3ManagerFactory;
import com.automatak.dnp3.mock.PrintingLogSubscriber;

import tollbridge.dnp3.master.device.Bridge;
import tollbridge.dnp3.master.device.Toll;

/**
 * Main class to start the Control Center
 * @author Ken LE PRADO ken@leprado.com
 */
public class ControlCenter {
	private static HomeFrame window;
	private static List<Toll> tolls = new ArrayList<Toll>();
	private static List<Bridge> bridges = new ArrayList<Bridge>();
	private static DNP3Manager DNP3_Manager = null;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

    	initDNP3();

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				window = new HomeFrame();
				window.setVisible(true);
			}
		});

	}	
	
	/**
	 * Exit the program
	 */
	public static void exit() {
		//Closing all tolls connexion
		Iterator<Toll> iter = tolls.iterator();
	    while (iter.hasNext()) {
	    	delToll(iter.next());
	    }
	    		
		//Closing all bridges connexion
		Iterator<Bridge> iterB = bridges.iterator();
	    while (iterB.hasNext()) {
	    	delBridge(iterB.next());
	    }
		
	    DNP3_Manager.shutdown();
		System.exit(0);
	}

	/**
	 * Permit to access to the DNP3 Manager
	 * @return DNP3 Manager
	 */
	public static DNP3Manager getDNP3Manager () {
		return DNP3_Manager ;
	}
	
	/**
	 * Initialize DNP3 Manager
	 */
	private static void initDNP3() {
	    // create the root class with a thread pool size of 1
	    DNP3_Manager = DNP3ManagerFactory.createManager(1);

	    // You can send the log messages anywhere you want
	    // but PrintingLogSubscriber just prints them to the console
	    DNP3_Manager.addLogSubscriber(PrintingLogSubscriber.getInstance());
		
	}
	
	/**
	 * Add a new Toll to the list
	 */
	public static void addToll() {
		Toll myToll = null;
		
		//We connect
		myToll = actionConnectNewToll();
		if (myToll == null) {
			return;
		}
		
		//We add to the graphics
		int y = tolls.size() * 150 + 10;
		myToll.initPanel(window.getContent(), 110, y);
		
		//We add to the collection
		tolls.add(myToll);
		window.addMessage("new toll added");
		
		refreshDevices();
	}
	
	/**
	 * Remove a connected Toll to the list
	 * @param myToll Toll to remove
	 */
	public static void delToll(Toll myToll) {

		//Del graphics
		myToll.getPanel().getContent().removeAll();
		myToll.getPanel().getContent().setVisible(false);
		
		//disconnect
		myToll.close();
		
		//Remove reference
		tolls.remove(myToll);
		window.addMessage("toll removed");
		
		//refresh others Toll
		refreshDevices();
	}

	/**
	 * Refresh status of the Devices
	 */
	public static void refreshDevices() {
		int y = 10;
		Iterator<Toll> iter = tolls.iterator();
	    while (iter.hasNext()) {
	    	Toll toll = (Toll) iter.next();
	    	toll.getPanel().updatePanel(110, y);
	    	y += 150;
	    	toll.integrityScan();
	    }
	    
		y = 10;
		Iterator<Bridge> iterB = bridges.iterator();
	    while (iterB.hasNext()) {
	    	Bridge bridge = (Bridge) iterB.next();
	    	bridge.getPanel().updatePanel(350, y);
	    	y += 250;
	    	bridge.integrityScan();
	    }
	    
		window.repaint();
	}
	
	/**
	 * Add a Bridge to the list
	 */
	public static void addBridge() {
		Bridge myBridge = null;
		
		//We connect
		myBridge = actionConnectNewBridge();
		if (myBridge == null) {
			return;
		}
		
		//We add to the graphics
		int y = bridges.size() * 250 + 10;
		myBridge.initPanel(window.getContent(), 250, y);
		
		//We add to the collection
		bridges.add(myBridge);
		window.addMessage("new bridge added");

		refreshDevices();
	}
	
	/**
	 * Remove a connected bridge 
	 * @param myBridge Bridge to remove
	 */
	public static void delBridge(Bridge myBridge) {

		//Del graphics
		myBridge.getPanel().getContent().removeAll();
		myBridge.getPanel().getContent().setVisible(false);
		
		//disconnect
		myBridge.close();
		
		//Remove reference
		bridges.remove(myBridge);
		window.addMessage("bridge removed");

		//refresh others Toll
		refreshDevices();

	}
	
	/**
	 * connect to a Toll
	 * @return Toll connected
	 */
	private static Toll actionConnectNewToll() {
		Toll myToll = null;
		
		String ipField = JOptionPane.showInputDialog("Toll IP Address:");
		
		
		try {
			myToll = new Toll(ipField);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(window, "Impossible to set IP");
			return null;
		}
					
		try {
			if (!myToll.connect()) {
				return null;
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(window, "Impossible to establish connection to " + ipField);
			return null;
		}
		
		//Test if the DEVICE_TYPE is good
		/*
		 //TODO reproduce it with DNP3
		ModbusDeviceIdentification ident = myToll.getDeviceIdentification(1, 0);
	
		if (!ident.getIdentification(1).equals("LEGO TOLL") & !ident.getIdentification(1).equals("LEGO TOLL SIM")) {
			JOptionPane.showMessageDialog(window, "Unattended device");
			return null;			
		}*/
		
		return myToll;
	}
	
	/**
	 * Connect to a new bridge
	 * @return Bridge connected
	 */
	private static Bridge actionConnectNewBridge() {
		Bridge myDevice = null;
		
		String ipField = JOptionPane.showInputDialog("Bridge IP Address:");

		try {
			myDevice = new Bridge(ipField);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(window, "Impossible to set IP");
			return null;
		}
		
		try {
			if (!myDevice.connect()) {
				return null;
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(window, "Impossible to establish connection to " + ipField);
			return null;
		}

		//Test if the DEVICE_TYPE is good
		 //TODO reproduce it with DNP3
/*
		ModbusDeviceIdentification ident = myDevice.getDeviceIdentification(1, 0);
		
		if (!ident.getIdentification(1).equals("LEGO BRIDGE")) {
			JOptionPane.showMessageDialog(window, "Unattended device");
			return null;			
		}
	*/	
		return myDevice;

	}
	
}
