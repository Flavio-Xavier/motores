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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Area;
import model.entities.Equipamento;
import model.exceptions.ValidationException;
import model.services.AreaService;
import model.services.EquipamentoService;

public class EquipamentoFormController implements Initializable {

	private Equipamento entity;
	private EquipamentoService service;
	private AreaService areaService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private ComboBox<Area> comboBoxArea;

	@FXML
	private Label labelErrorName;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Area> obsList;

	public void setEquipamento(Equipamento entity) {
		this.entity = entity;
	}

	public void setServices(EquipamentoService service, AreaService areaService) {
		this.service = service;
		this.areaService = areaService;
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
		} catch (ValidationException e) {
			setErrorMessages(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Equipamento getFormData() {
		Equipamento obj = new Equipamento();
		ValidationException exception = new ValidationException("Validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("nome", "O campo não pode ser vazio!");
		}
		obj.setName(txtName.getText());

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
		// applyNumericFilters();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 50);
		initializeComboBoxArea();
	}

//	private void applyNumericFilters() {
//        Utils.setNumericTextField(txtName);
//	}

	public void updatFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
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
		obsList = FXCollections.observableArrayList(list);
		comboBoxArea.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("nome")) {
			labelErrorName.setText(errors.get("nome"));
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
	}

}
