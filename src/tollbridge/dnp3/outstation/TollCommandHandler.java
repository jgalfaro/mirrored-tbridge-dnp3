package tollbridge.dnp3.outstation;

import com.automatak.dnp3.AnalogOutputDouble64;
import com.automatak.dnp3.AnalogOutputFloat32;
import com.automatak.dnp3.AnalogOutputInt16;
import com.automatak.dnp3.AnalogOutputInt32;
import com.automatak.dnp3.CommandHandler;
import com.automatak.dnp3.CommandStatus;
import com.automatak.dnp3.ControlRelayOutputBlock;

public class TollCommandHandler implements CommandHandler{
	private RTUToll m_toll = null;
	
	public TollCommandHandler(RTUToll toll) {
		m_toll = toll;
	}
	@Override
	public CommandStatus directOperate(ControlRelayOutputBlock arg0, long arg1) {
		// TODO Auto-generated method stub
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus directOperate(AnalogOutputInt32 arg0, long arg1) {
		System.out.println("Yop " + (int) arg1 + "-"+ arg0.value);
		m_toll.procimg.setAnalogOutput((int) arg1, arg0.value);
		return CommandStatus.SUCCESS;
	}

	@Override
	public CommandStatus directOperate(AnalogOutputInt16 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus directOperate(AnalogOutputFloat32 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus directOperate(AnalogOutputDouble64 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus operate(ControlRelayOutputBlock arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus operate(AnalogOutputInt32 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus operate(AnalogOutputInt16 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus operate(AnalogOutputFloat32 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus operate(AnalogOutputDouble64 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus select(ControlRelayOutputBlock arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus select(AnalogOutputInt32 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus select(AnalogOutputInt16 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus select(AnalogOutputFloat32 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

	@Override
	public CommandStatus select(AnalogOutputDouble64 arg0, long arg1) {
		System.out.println("Undefined command");
		return CommandStatus.UNDEFINED;
	}

}
