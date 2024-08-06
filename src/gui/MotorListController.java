package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Motor;
import model.services.MotorService;

public class MotorListController implements Initializable, DataChangeListener {

	private MotorService service;

	@FXML
	private TableView<Motor> tableViewMotor;

	@FXML
	private TableColumn<Motor, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Motor, String> tableColumnTensao;
	
	@FXML
	private TableColumn<Motor, String> tableColumnCorrente;
	
	@FXML
	private TableColumn<Motor, String> tableColumnPotenciaCv;
	
	@FXML
	private TableColumn<Motor, String> tableColumnRotacao;
	
	@FXML
	private TableColumn<Motor, String> tableColumnCarcaca;
	
	@FXML
	private TableColumn<Motor, String> tableColumnFatorPotencia;
	
	@FXML
	private TableColumn<Motor, String> tableColumnFatorServico;
	
	@FXML
	private TableColumn<Motor, String> tableColumnFabricante;
	
	@FXML
	private TableColumn<Motor, String> tableColumnCodigoSap;
	
	@FXML
	private TableColumn<Motor, String> tableColumnPotenciaWatts;
	
	@FXML
	private TableColumn<Motor, String> tableColumnGrauProtecao;
	
	@FXML
	private TableColumn<Motor, String> tableColumnFrequencia;
	
	@FXML
	private TableColumn<Motor, String> tableColumnRolamentoDianteiro;
	
	@FXML
	private TableColumn<Motor, String> tableColumnRolamentoTraseiro;
	
	@FXML
	private TableColumn<Motor, String> tableColumnNameArea;
	
	@FXML
	private TableColumn<Motor, String> tableColumnNameEquipamento;

	@FXML
	private TableColumn<Motor, Motor> tableColumnEDIT;

	@FXML
	private TableColumn<Motor, Motor> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Motor> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Motor obj = new Motor();
		createDialogForm(obj, "/gui/MotorForm.fxml", parentStage);
	}

	public void setMotorService(MotorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnTensao.setCellValueFactory(new PropertyValueFactory<>("tensao"));
		tableColumnCorrente.setCellValueFactory(new PropertyValueFactory<>("corrente"));
		//tableColumnPotenciaCv.setCellValueFactory(new PropertyValueFactory<>("potenciaCv"));
		tableColumnRotacao.setCellValueFactory(new PropertyValueFactory<>("rotacao"));
		tableColumnCarcaca.setCellValueFactory(new PropertyValueFactory<>("carcaca"));
		tableColumnFatorPotencia.setCellValueFactory(new PropertyValueFactory<>("fatorPotencia"));
		tableColumnFatorServico.setCellValueFactory(new PropertyValueFactory<>("fatorServico"));
		tableColumnFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
		tableColumnCodigoSap.setCellValueFactory(new PropertyValueFactory<>("codigoSap"));
		tableColumnPotenciaWatts.setCellValueFactory(new PropertyValueFactory<>("potenciaWatts"));
		tableColumnGrauProtecao.setCellValueFactory(new PropertyValueFactory<>("grauProtecao"));
		tableColumnFrequencia.setCellValueFactory(new PropertyValueFactory<>("frequencia"));
		tableColumnRolamentoDianteiro.setCellValueFactory(new PropertyValueFactory<>("rolamentoDianteiro"));
		tableColumnRolamentoTraseiro.setCellValueFactory(new PropertyValueFactory<>("rolamentoTraseiro"));
		tableColumnNameArea.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArea().getName()));	
		tableColumnNameEquipamento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquipamento().getName()));	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMotor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Motor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewMotor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Motor obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//			MotorFormController controller = loader.getController();
//			controller.setMotor(obj);
//			controller.setMotorService(new MotorService());
//			controller.subscribeDataChangeListener(this);
//			controller.updatFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Informe o nome da Área");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Motor, Motor>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Motor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/MotorForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Motor, Motor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Motor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Motor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja deletar?");
		if(result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
