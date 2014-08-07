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

public abstract class Device {

	private InetAddress address = null;
	private int port = 0;
	private Channel channel = null;
	private Master master = null;
	public DataObserver myDO = null;
	public StackState status = null;

	public Device (String myAddress, int port) throws UnknownHostException {
    	this.setIp(myAddress);
    	this.setPort(port);
	}

	public Device (String myAddress) throws UnknownHostException {
		this.setIp(myAddress);
		this.setPort(20000);
	}

	public Device () {
		this.setPort(20000);
	}

	
	public void setIp (String myAddress) throws UnknownHostException {
		this.address = InetAddress.getByName(myAddress);
	}
	
	public String getIp() {
		return this.address.getHostAddress();
	}

	public void setPort (int port) {
		this.port = port;
	}

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

	abstract MasterStackConfig getDNP3Config();
	abstract DataObserver setDNP3DataObserver();
	
	public void close() {
		channel.shutdown();
	}
	public boolean isConnected() {
		if (this.status == StackState.COMMS_UP) {
			return true;
		} else {
			return false;
		}
	}
	
	abstract int getUnitId();
	

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
    
    public void integrityScan() {
    	master.performIntegrityScan();    	
    }

    abstract void setStatus(StackState state);
}
