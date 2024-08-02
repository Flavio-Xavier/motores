package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.AreaService;

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
		loadView2("/gui/AreaList.fxml");
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
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
		}catch (IOException e) {
			Alerts.showAlert("IOException", "Erros loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void loadView2(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			AreaListController controller = loader.getController();
			controller.setAreaService(new AreaService());
			controller.updateTableView();
			
		}catch (IOException e) {
			Alerts.showAlert("IOException", "Erros loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
