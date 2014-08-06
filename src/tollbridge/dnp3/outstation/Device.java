package tollbridge.dnp3.outstation;

import Ressources.ProcessImage;

import com.automatak.dnp3.Channel;
import com.automatak.dnp3.ChannelState;
import com.automatak.dnp3.ChannelStateListener;
import com.automatak.dnp3.DNP3Manager;
import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.Outstation;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.impl.DNP3ManagerFactory;
import com.automatak.dnp3.mock.PrintingLogSubscriber;
import com.automatak.dnp3.mock.SuccessCommandHandler;

/**
 * Defines a device
 * @author Ken LE PRADO
 * @version 1.0
 */
abstract class Device {
	
	private String deviceAddr = "";
	
	public DNP3Manager dnp3Manager = null;
	public Channel dnp3Channel = null;
	public OutstationStackConfig dnp3Config = null;
	public Outstation dnp3Outstation = null;
	public DataObserver dnp3Observer = null; 
	
	private int dnp3Port = 20000;
	private int dnp3NbThread = 2;
	public ProcessImage procimg = null;
	public int dnp3UnitId = 1;

	public Device(String deviceAddr, int dnp3Port, int dnp3UnitId) {
		this.deviceAddr = deviceAddr;

		this.dnp3Port = dnp3Port;
		this.dnp3UnitId = dnp3UnitId;

	}
	
	

	/*
	 * DNP3 initialisation
	 */
	public void initDnp3() {
        // create the root class with a thread pool size of 1
        dnp3Manager = DNP3ManagerFactory.createManager(dnp3NbThread);

        // You can send the log messages anywhere you want
        // but PrintingLogSubscriber just prints them to the console
        dnp3Manager.addLogSubscriber(PrintingLogSubscriber.getInstance());

        // Create a TCP channel class that will connect to the address
        dnp3Channel = dnp3Manager.addTCPServer("client", LogLevel.INFO, 5000, deviceAddr, dnp3Port);

        
        // You can optionally add a listener to receive state changes on the channel
        dnp3Channel.addStateListener(new ChannelStateListener() {
            @Override
            public void onStateChange(ChannelState state) {
                System.out.println("server state: " + state);
            }
        });

        initDnp3Config();
        
        // You can optionally add a listener to receive state changes on the stack
        /*
        dnp3Outstation.addStateListener(new StackStateListener() {
            @Override
            public void onStateChange(StackState state) {
                System.out.println("Outstation state: " + state);
            }
        });*/

        // This sub-interface allows us to load data into the outstation
        dnp3Observer = dnp3Outstation.getDataObserver();
		
	}

	/*
	 * DNP3 unload
	 */
	public void stopDnp3() {
		dnp3Manager.shutdown();
	}

	
	abstract public void initDnp3Config();
		
	/*
	 * EV3 Initialisation
	 */
	abstract public void initEV3();
	
	abstract public void loadEV3();

	/*
	 * EV3 Close
	 */
	abstract public void stopEV3();
	
	/*
	 * Thread to manage the device
	 */
	abstract public void run();

	/*
	 * Sounds a beep
	 */
	abstract public void beep();

}