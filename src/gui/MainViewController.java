package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.EquipamentoService;
import model.services.MotorService;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemArea;
	
	@FXML
	private MenuItem menuItemEquipamento;
	
	@FXML
	private MenuItem menuItemMotor;
	
	@FXML
	private MenuItem menuItemSearchMotor;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemAreaAction() {
		loadView("/gui/AreaList.fxml", (AreaListController controller) -> {
			controller.setAreaService(new AreaService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemEquipamentoAction() {
		loadView("/gui/EquipamentoList.fxml", (EquipamentoListController controller) -> {
			controller.setEquipamentoService(new EquipamentoService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemMotorAction() {
		loadView("/gui/MotorList.fxml", (MotorListController controller) -> {
			controller.setMotorService(new MotorService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemMotorSearchAction() {
	    loadView("/gui/MotorSearchList.fxml", (MotorSearchListController controller) -> {
	        controller.setMotorService(new MotorService());
	        controller.updateTableView();
	    });
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		}catch (IOException e) {
			Alerts.showAlert("IOException", "Erros loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
