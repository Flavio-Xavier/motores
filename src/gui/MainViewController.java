package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemArea;
	
	@FXML
	private MenuItem menuItemEquipamento;
	
	@FXML
	private MenuItem menuItemMotor;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemAreaAction() {
		System.out.println("Area");
	}
	
	@FXML
	public void onMenuItemEquipamentoAction() {
		System.out.println("Equipamento");
	}
	
	@FXML
	public void onMenuItemMotorAction() {
		System.out.println("Motor");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		System.out.println("About");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}

}
