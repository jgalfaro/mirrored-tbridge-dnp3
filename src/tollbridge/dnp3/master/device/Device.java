package tollbridge.dnp3.master.device;

import java.net.InetAddress;
import java.net.UnknownHostException;

import tollbridge.dnp3.master.ControlCenter;

import com.automatak.dnp3.AnalogOutputInt32;
import com.automatak.dnp3.Channel;
import com.automatak.dnp3.ChannelState;
import com.automatak.dnp3.ChannelStateListener;
import com.automatak.dnp3.CommandProcessor;
import com.automatak.dnp3.CommandStatus;
import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.ListenableFuture;
import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.Master;
import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.StackState;

/**
 * Device definition abstraction
 * @author Ken LE PRADO
 *
 */
public abstract class Device {
	private InetAddress address = null;
	private int port = 0;
	private Channel channel = null;
	private Master master = null;
	protected DataObserver myDO = null;
	protected StackState status = null;

	/**
	 * @param myAddress IP or DNS address of the remote device
	 * @param port Number of the remote device (Default : 20000)
	 * @throws UnknownHostException
	 */
	public Device (String myAddress, int port) throws UnknownHostException {
    	this.setIp(myAddress);
    	this.setPort(port);
	}

	/**
	 * @param myAddress IP or DNS address of the remote device
	 * @throws UnknownHostException
	 */
	public Device (String myAddress) throws UnknownHostException {
		this.setIp(myAddress);
		this.setPort(20000);
	}

	/**
	 * 
	 */
	public Device () {
		this.setPort(20000);
	}

	/**
	 * Set IP or DNS address of the remote device
	 * @param myAddress IP or DNS (Ex : 127.0.0.1 or localhost)
	 * @throws UnknownHostException
	 */
	public void setIp (String myAddress) throws UnknownHostException {
		this.address = InetAddress.getByName(myAddress);
	}
	
	/**
	 * Get IP address set for the remote device
	 * @return IP Address
	 */
	public String getIp() {
		return this.address.getHostAddress();
	}

	/**
	 * Set Port of the remote device
	 * @param port Remote device port (Ex : 20000)
	 */
	public void setPort (int port) {
		this.port = port;
	}

	/**
	 * Connect to the remote device
	 * @return true
	 * @throws Exception
	 */
	public boolean connect() throws Exception {
        // Create a tcp channel class that will connect to the loopback
        channel = ControlCenter.getDNP3Manager().addTCPClient("client " + this.getIp(), LogLevel.INFO, 50000, this.getIp(), this.port);
        
        // You can optionally add a listener to receive state changes on the channel
        channel.addStateListener(new ChannelStateListener() {
            @Override
            public void onStateChange(ChannelState state) {
                System.out.println("Client state: " + state);
            }
        });
	
        setDNP3DataObserver();
        
        // Create a master instance, pass in a simple singleton to print received values to the console
        master = channel.addMaster("master", LogLevel.INTERPRET, myDO, getDNP3Config());

        // You can optionally add a listener to receive state changes on the stack
        
        master.addStateListener(new StackStateDeviceListener(this));
        
		return true;
	}

	/**
	 * Configure the Master Stack Configuration
	 * @return configuration object
	 */
	abstract MasterStackConfig getDNP3Config();
	
	/**
	 * Configure the Data Observer
	 * @return Data Observer Object
	 */
	abstract DataObserver setDNP3DataObserver();
	
	/**
	 * Close an established connection
	 */
	public void close() {
		channel.shutdown();
	}
	
	/**
	 * Returns if the device is connected
	 * @return Status of the connection
	 */
	public boolean isConnected() {
		if (this.status == StackState.COMMS_UP) {
			return true;
		} else {
			return false;
		}
	}
	

	/**
	 * Perform a DNP3 directOperate
	 * @param ref Analog Output Reference
	 * @param value Analog Output value
	 */
    public void operate(int ref, int value) {
    	    	
	    CommandProcessor processor = master.getCommandProcessor();
    	    	
    	AnalogOutputInt32 aop_mode = new AnalogOutputInt32 (value, CommandStatus.SUCCESS);
    	
	    System.out.println("Begin operate");
	    
	    processor.directOperate(aop_mode, ref).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
	        @Override
	        public void onComplete(CommandStatus value) {
	    	    System.out.println("End operate " + value.toString());
	        }
	    });
    }
    
    /**
     * Perform a DNP3 Integrity Scan (reception of all values)
     */
    public void integrityScan() {
    	master.performIntegrityScan();    	
    }

    /**
     * Set status of the connection
     * @param state Connection state
     */
    abstract void setStatus(StackState state);
}
