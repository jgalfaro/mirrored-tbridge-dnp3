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

/**
 * Toll Panel
 * @author Ken LE PRADO ken@leprado.com
 */
public class TollPanel extends DevicePanel implements ActionListener {
	private JLabel jlbStatusCars;
	private JLabel jlbStatusCoins;
	private JButton btnTollOff;
	private JButton btnTollFree;
	private JButton btnTollPay;
	private JLabel jlbTollName;
	
	private JLabel picLabelBarrierOpen;
	private JLabel picLabelBarrierClose;

	/**
	 * @param dev Device displayed by this panel
	 * @param con Container of the panel
	 * @param x X position of the panel
	 * @param y Y position of the panel
	 */
	public TollPanel(Device dev, Container con, int x, int y) {
		super(dev, con, x, y);
	}

	/**
	 * Initiate the panel
	 */	
	@Override
	public void initPanel() {
		content = new JPanel();
		content.setLayout(null);
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Toll Name
		jlbTollName = new JLabel(myDevice.getIp() + "...");
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
	    picLabelBarrierOpen = new JLabel( new ImageIcon( "src/Ressources/open_barrier.png"));
		picLabelBarrierOpen.setBounds(90, 25, 60, 60);
		content.add(picLabelBarrierOpen);
		picLabelBarrierClose = new JLabel( new ImageIcon( "src/Ressources/close_barrier.png"));
		picLabelBarrierClose.setBounds(90, 25, 60, 60);
		content.add(picLabelBarrierClose);		
		
		//coins
		JLabel picLabelCoins = new JLabel( new ImageIcon( "src/Ressources/coins.png"));
		picLabelCoins.setBounds(85, 90, 24, 24);
		content.add(picLabelCoins);

		jlbStatusCoins = new JLabel("");
		jlbStatusCoins.setBounds(140, 90, 30, 10);
		content.add(jlbStatusCoins);

		
		//cars
		JLabel picLabelCars = new JLabel( new ImageIcon( "src/Ressources/cars.png"));
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
	
	/**
	 * Remove a panel
	 */
	@Override
	public void removePanel() {
		content.setVisible(false);
	}

	/**
	 * Called on click event on the Panel
	 * @param e Action Performed
	 */
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
	
	/**
	 * Set Toll Mode
	 * @param mode Mode 
	 */
	private void actionSetTollParam(String mode) {
		if (mode == "Off") {
			((Toll) myDevice).setStatusMode(Toll.MODE_OFF);
		} else if (mode == "Free") {
			((Toll) myDevice).setStatusMode(Toll.MODE_FREE);
		} else if (mode == "Pay") {
			((Toll) myDevice).setStatusMode(Toll.MODE_PAY);
		}
	}
	
	/**
	 * Access to the Cars Status Label
	 * @return Status Cars
	 */
	public JLabel getStatusCars(){
		return jlbStatusCars;
	}

	/**
	 * Access to the Coins Status Label
	 * @return Status Coins
	 */
	public JLabel getStatusCoins(){
		return jlbStatusCoins;
	}
		
	/**
	 * Displays the barrier status
	 * @param open Status of the barrier
	 */
	public void showTollBarrier(boolean open) {
		if (open == true) {
			picLabelBarrierOpen.setVisible(true);
			picLabelBarrierClose.setVisible(false);
		} else {
			picLabelBarrierOpen.setVisible(false);
			picLabelBarrierClose.setVisible(true);			
		}
	}
	
	/**
	 * Displays the Toll mode
	 * @param statusMode Mode of the Toll
	 */
	public void showTollMode(int statusMode) {
		switch (statusMode) {
		case Toll.MODE_OFF:
			btnTollOff.setBackground(Color.green);
			btnTollFree.setBackground(Color.gray);
			btnTollPay.setBackground(Color.gray);
			break;
		case Toll.MODE_FREE:
			btnTollOff.setBackground(Color.gray);
			btnTollFree.setBackground(Color.green);
			btnTollPay.setBackground(Color.gray);
			break;
		case Toll.MODE_PAY:
			btnTollOff.setBackground(Color.gray);
			btnTollFree.setBackground(Color.gray);
			btnTollPay.setBackground(Color.green);
			break;
		}
	}
	
	/**
	 * Clears the status of the toll, waiting the real data to refresh the display
	 */
	@Override
	public void clearStatus() {
		btnTollOff.setBackground(null);
		btnTollFree.setBackground(null);
		btnTollPay.setBackground(null);

		picLabelBarrierOpen.setVisible(false);
		picLabelBarrierClose.setVisible(false);

		jlbStatusCoins.setText("");
		jlbStatusCars.setText("");
	}

	/**
	 * Refresh the display of the Device Label
	 * @param unitId Unit Identifier
	 */
	@Override
	public void setDeviceLabel(int unitId) {
		jlbTollName.setText(myDevice.getIp() + " - id : " + unitId);
	}
}
