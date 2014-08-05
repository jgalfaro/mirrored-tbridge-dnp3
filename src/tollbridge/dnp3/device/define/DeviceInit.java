package tollbridge.dnp3.device.define;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DeviceInit {
	//Network address (127.0.0.1, etc.)
	private static String deviceAddr = "";
	
	//DNP3 params
	private static int dnp3Port = 20000;
	private static int dnp3UnitId = 1;

	//Type of device (Toll, Bridge, TollSim, Rover)
	private static String deviceType = "";
	
	
	/**
	 * Toll run
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) {
		Device device = null;
		
		//Init config file
		read_init("Device.ini");
	    
		/*if (deviceType.equals("TOLL")) {
		    device = new Toll(deviceAddr, dnp3Active, dnp3Port, dnp3UnitId);
		} else*/ if (deviceType.equals("TOLL_SIM")) {
		    device = new TollSim(deviceAddr, dnp3Port, dnp3UnitId);
		} /*else if (deviceType.equals("BRIDGE")) {
		    device = new Bridge(deviceAddr, dnp3Active, dnp3Port, dnp3UnitId);
		} else if (deviceType.equals("ROVER")) {
		    device = new Rover(deviceAddr, dnp3Active, dnp3Port, dnp3UnitId);
		} */ else {
			System.err.println("Device type ("+deviceType+") unknown");
		}
		
		if (device != null) {
			device.initEV3();
			
			device.initDnp3();
			
			
			device.beep();
			device.run();
			device.beep();
			
			device.stopDnp3();

			device.stopEV3();
		}
		
		System.exit(0);
	}
	
	public static void read_init(String fileName) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open Device.ini file");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Cannot read Device.ini file");
			System.exit(1);
		}
		deviceAddr = p.getProperty("DEVICE_IP");

		dnp3UnitId = Integer.parseInt(p.getProperty("DEVICE_DNP_ID"));
		dnp3Port = Integer.parseInt(p.getProperty("DEVICE_DNP_PORT"));

		deviceType = p.getProperty("DEVICE_TYPE");
	}
}
