package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.entities.Area;
import model.entities.Equipamento;
import model.entities.Motor;
import model.exceptions.ValidationException;
import model.services.AreaService;
import model.services.EquipamentoService;
import model.services.MotorService;

public class MotorFormController implements Initializable{

	private Motor entity;
	private MotorService service;
	private EquipamentoService equipamentoService;
	private AreaService areaService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtTensao;
	
	@FXML
	private TextField txtCorrente;
	
	@FXML
	private TextField txtRotacao;
	
	@FXML
	private TextField txtCarcaca;
	
	@FXML
	private TextField txtFatorPotencia;
	
	@FXML
	private TextField txtFatorServico;
	
	@FXML
	private TextField txtFabricante;
	
	@FXML
	private TextField txtCodigoSap;
	
	@FXML
	private TextField txtPotencia;
	
	@FXML
	private TextField txtGrauProtecao;
	
	@FXML
	private TextField txtFrequencia;
	
	@FXML
	private TextField txtRolamentoDianteiro;
	
	@FXML
	private TextField txtRolamentoTraseiro;
	
	@FXML
	private ComboBox<Equipamento> comboBoxEquipamento;
	
	@FXML
	private ComboBox<Area> comboBoxArea;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private ObservableList<Area> obsListArea;
	
	private ObservableList<Equipamento> obsListEquipamento;
	
	public void setMotor(Motor entity) {
		this.entity = entity;
	}
	
	public void setServices(MotorService service, AreaService areaService , EquipamentoService equipamentoService) {
		this.service = service;
		this.areaService = areaService;
		this.equipamentoService = equipamentoService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	private void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData(); 
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();	
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Motor getFormData() {
		Motor obj = new Motor();
		ValidationException exception = new ValidationException("Validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtTensao.getText() == null || txtTensao.getText().trim().equals("")) {
			exception.addError("tensao", "O campo não pode ser vazio!");
		}
		obj.setTensao(txtTensao.getText());
		
		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	private void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		//applyNumericFilters();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtTensao, 50);
		initializeComboBoxArea();
	    initializeComboBoxEquipamento();
	}
	
//	private void applyNumericFilters() {
//        Utils.setNumericTextField(txtName);
//	}
	
	public void updatFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		if(entity.getEquipamento() == null) {
			comboBoxEquipamento.getSelectionModel().selectFirst();
		}else {
			comboBoxEquipamento.setValue(entity.getEquipamento());
		}
		if(entity.getArea() == null) {
			comboBoxArea.getSelectionModel().selectFirst();
		}else {
			comboBoxArea.setValue(entity.getArea());
		}
		
	}
	
	public void loadAssociatedObjects() {
	    if (areaService == null) {
	        throw new IllegalStateException("AreaService was null");
	    }
	    List<Area> list = areaService.findAll();
	    obsListArea = FXCollections.observableArrayList(list);
	    comboBoxArea.setItems(obsListArea);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if(fields.contains("nome")) {
			labelErrorName.setText(errors.get("tensao"));
		}
	}
	
	private void initializeComboBoxArea() {
	    Callback<ListView<Area>, ListCell<Area>> factory = lv -> new ListCell<Area>() {
	        @Override
	        protected void updateItem(Area item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(empty ? "" : item.getName());
	        }
	    };
	    comboBoxArea.setCellFactory(factory);
	    comboBoxArea.setButtonCell(factory.call(null));

	    comboBoxArea.setOnAction(event -> {
	        Area selectedArea = comboBoxArea.getSelectionModel().getSelectedItem();
	        if (selectedArea != null) {
	            loadEquipamentosByArea(selectedArea);
	        }
	    });
	}
	
	private void loadEquipamentosByArea(Area area) {
	    if (equipamentoService == null) {
	        throw new IllegalStateException("EquipamentoService was null");
	    }
	    List<Equipamento> listEquipamento = equipamentoService.findByArea(area);
	    obsListEquipamento = FXCollections.observableArrayList(listEquipamento);
	    comboBoxEquipamento.setItems(obsListEquipamento);
	}


	private void initializeComboBoxEquipamento() {
	    Callback<ListView<Equipamento>, ListCell<Equipamento>> factory = lv -> new ListCell<Equipamento>() {
	        @Override
	        protected void updateItem(Equipamento item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(empty ? "" : item.getName());
	        }
	    };
	    comboBoxEquipamento.setCellFactory(factory);
	    comboBoxEquipamento.setButtonCell(factory.call(null));
	}
}
