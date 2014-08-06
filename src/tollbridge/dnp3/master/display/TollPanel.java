package tollbridge.dnp3.master.display;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tollbridge.dnp3.master.ControlCenter;
import tollbridge.dnp3.master.device.Device;
import tollbridge.dnp3.master.device.Toll;

public class TollPanel extends DevicePanel implements ActionListener {
	public TollPanel(Device dev, Container con, int x, int y) {
		super(dev, con, x, y);
	}

	private JLabel jlbStatusCars;
	private JLabel jlbStatusCoins;
	private JButton btnTollOff;
	private JButton btnTollFree;
	private JButton btnTollPay;
	private JLabel jlbTollName;
	
	private JLabel picLabelBarrierOpen;
	private JLabel picLabelBarrierClose;
	
	public void initPanel() {
		content = new JPanel();
		content.setLayout(null);
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Toll Name
		jlbTollName = new JLabel(myDevice.getLabel());
		jlbTollName.setBounds(5, 5, 130, 15);
		content.add(jlbTollName);
		
		//Button status
		btnTollOff = new JButton("Off");
		btnTollOff.addActionListener(this);
		btnTollOff.setBounds(10, 25, 70, 20);
		content.add(btnTollOff);
		btnTollFree = new JButton("Free");
		btnTollFree.addActionListener(this);
		btnTollFree.setBounds(10 , 45, 70, 20);
		content.add(btnTollFree);
		btnTollPay = new JButton("Pay");
		btnTollPay.addActionListener(this);
		btnTollPay.setBounds(10, 65, 70, 20);
		content.add(btnTollPay);
		
		//Barrier up/down
	    picLabelBarrierOpen = new JLabel( new ImageIcon( "/home/user/workspace/tollbridge-dnp3/src/Ressources/open_barrier.png"));
		picLabelBarrierOpen.setBounds(90, 25, 60, 60);
		content.add(picLabelBarrierOpen);
		picLabelBarrierClose = new JLabel( new ImageIcon( "/home/user/workspace/tollbridge-dnp3/src/Ressources/close_barrier.png"));
		picLabelBarrierClose.setBounds(90, 25, 60, 60);
		content.add(picLabelBarrierClose);		
		
		//coins
		JLabel picLabelCoins = new JLabel( new ImageIcon( "/home/user/workspace/tollbridge-dnp3/src/Ressources/coins.png"));
		picLabelCoins.setBounds(85, 90, 24, 24);
		content.add(picLabelCoins);

		jlbStatusCoins = new JLabel("");
		jlbStatusCoins.setBounds(140, 90, 30, 10);
		content.add(jlbStatusCoins);

		
		//cars
		JLabel picLabelCars = new JLabel( new ImageIcon( "/home/user/workspace/tollbridge-dnp3/src/Ressources/cars.png"));
		picLabelCars.setBounds( 170, 25, 40, 25);
		content.add(picLabelCars);

		jlbStatusCars = new JLabel("");
		jlbStatusCars.setBounds( 180, 55, 30, 10);
		content.add(jlbStatusCars);

		//Btn Disconnect
		btnClose = new JButton("Close");
		btnClose.setBounds(10, 115, 80, 20);
		btnClose.addActionListener(this);
		content.add(btnClose);
		
		//Adds the content
		content.setBounds(X, Y, 230, 140);
		content.setVisible(true);
		container.add(content);
				
	}
		
	public void removePanel() {
		content.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if(source == btnTollOff){
			actionSetTollParam("Off");
		} else if(source == btnTollFree){
			actionSetTollParam("Free");
		} else if(source == btnTollPay){
			actionSetTollParam("Pay");			
		} else if(source == btnClose){
			ControlCenter.delToll((Toll) myDevice);			
		}
	}
	
	private void actionSetTollParam(String mode) {
		if (mode == "Off") {
			((Toll) myDevice).setStatusMode(0);
		} else if (mode == "Free") {
			((Toll) myDevice).setStatusMode(1);
		} else if (mode == "Pay") {
			((Toll) myDevice).setStatusMode(2);
		}
	}
	

	public JLabel getStatusCars(){
		return jlbStatusCars;
	}

	public JLabel getStatusCoins(){
		return jlbStatusCoins;
	}
	

	public void showTollBarrier(boolean open) {
		if (open == true) {
			picLabelBarrierOpen.setVisible(true);
			picLabelBarrierClose.setVisible(false);
		} else {
			picLabelBarrierOpen.setVisible(false);
			picLabelBarrierClose.setVisible(true);			
		}
	}
	

	public void showTollMode(int statusMode) {
		switch (statusMode) {
		case 0:
			btnTollOff.setBackground(Color.green);
			btnTollFree.setBackground(Color.gray);
			btnTollPay.setBackground(Color.gray);
			break;
		case 1:
			btnTollOff.setBackground(Color.gray);
			btnTollFree.setBackground(Color.green);
			btnTollPay.setBackground(Color.gray);
			break;
		case 2:
			btnTollOff.setBackground(Color.gray);
			btnTollFree.setBackground(Color.gray);
			btnTollPay.setBackground(Color.green);
			break;
		}
	}
	
	public void clearStatus() {
		btnTollOff.setBackground(null);
		btnTollFree.setBackground(null);
		btnTollPay.setBackground(null);

		picLabelBarrierOpen.setVisible(false);
		picLabelBarrierClose.setVisible(false);

		jlbStatusCoins.setText("");
		jlbStatusCars.setText("");
	}

}
