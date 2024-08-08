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
	private ComboBox<String> comboBoxTensao;
	
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
	private Label labelErrorTensao;
	
	@FXML
	private Label labelErrorCorrente;
	
	@FXML
	private Label labelErrorPotencia;
	
	@FXML
	private Label labelErrorRotacao;
	
	@FXML
	private Label labelErrorFabricante;
	
	@FXML
	private Label labelErrorArea;
	
	@FXML
	private Label labelErrorEquipamento;
	
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
		
		if (comboBoxTensao.getValue() == null) {
			exception.addError("tensao", "O campo não pode ser vazio!");
		}
		obj.setTensao(comboBoxTensao.getValue());
		if (txtCorrente.getText() == null || txtCorrente.getText().trim().equals("")) {
			exception.addError("corrente", "O campo não pode ser vazio!");
		}
		obj.setCorrente(txtCorrente.getText());
		if (txtRotacao.getText() == null || txtRotacao.getText().trim().equals("")) {
			exception.addError("rotacao", "O campo não pode ser vazio!");
		}
		obj.setRotacao(txtRotacao.getText());
		obj.setCarcaca(txtCarcaca.getText());
		obj.setFatorPotencia(txtFatorPotencia.getText());
		obj.setFatorServico(txtFatorServico.getText());
		if (txtFabricante.getText() == null || txtFabricante.getText().trim().equals("")) {
			exception.addError("fabricante", "O campo não pode ser vazio!");
		}
		obj.setFabricante(txtFabricante.getText());
		obj.setCodigoSap(txtCodigoSap.getText());
		if (txtPotencia.getText() == null || txtPotencia.getText().trim().equals("")) {
			exception.addError("potencia", "O campo não pode ser vazio!");
		}
		obj.setPotenciaWatts(txtPotencia.getText());
		obj.setGrauProtecao(txtGrauProtecao.getText());
		obj.setFrequencia(txtFrequencia.getText());
		obj.setRolamentoDianteiro(txtRolamentoDianteiro.getText());
		obj.setRolamentoTraseiro(txtRolamentoTraseiro.getText());
		if (comboBoxArea.getValue() == null) {
			exception.addError("area", "O campo não pode ser vazio!");
		}
		obj.setEquipamento(comboBoxEquipamento.getValue());
		if (comboBoxEquipamento.getValue() == null) {
			exception.addError("equipamento", "O campo não pode ser vazio!");
		}
		obj.setArea(comboBoxArea.getValue());
		
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
		applyNumericFilters();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		initializeComboBoxTensao();
		initializeComboBoxArea();
	    initializeComboBoxEquipamento();
	}
	
	private void applyNumericFilters() {
        Utils.setNumericTextField(txtRotacao);
        Utils.setNumericTextField(txtPotencia);
        Utils.setNumericTextField(txtFrequencia);
        Utils.setDecimalTextField(txtCorrente);
        Utils.setDecimalTextField(txtFatorPotencia);
        Utils.setDecimalTextField(txtFatorServico);
	}
	
	public void updatFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(entity.getId() == null ? "" : String.valueOf(entity.getId()));
	    comboBoxTensao.setValue(entity.getTensao());
	    txtCorrente.setText(entity.getCorrente() == null ? "" : entity.getCorrente());
	    txtRotacao.setText(entity.getRotacao() == null ? "" : entity.getRotacao());
	    txtCarcaca.setText(entity.getCarcaca() == null ? "" : entity.getCarcaca());
	    txtFatorPotencia.setText(entity.getFatorPotencia() == null ? "" : entity.getFatorPotencia());
	    txtFatorServico.setText(entity.getFatorServico() == null ? "" : entity.getFatorServico());
	    txtFabricante.setText(entity.getFabricante() == null ? "" : entity.getFabricante());
	    txtCodigoSap.setText(entity.getCodigoSap() == null ? "" : entity.getCodigoSap());
	    txtPotencia.setText(entity.getPotenciaWatts() == null ? "" : entity.getPotenciaWatts());
	    txtGrauProtecao.setText(entity.getGrauProtecao() == null ? "" : entity.getGrauProtecao());
	    txtFrequencia.setText(entity.getFrequencia() == null ? "" : entity.getFrequencia());
	    txtRolamentoDianteiro.setText(entity.getRolamentoDianteiro() == null ? "" : entity.getRolamentoDianteiro());
	    txtRolamentoTraseiro.setText(entity.getRolamentoTraseiro() == null ? "" : entity.getRolamentoTraseiro());
	    
		if(entity.getArea() == null) {
			comboBoxArea.getSelectionModel().selectFirst();
		}else {
			comboBoxArea.setValue(entity.getArea());
		}
		if(entity.getEquipamento() == null) {
			comboBoxEquipamento.getSelectionModel().selectFirst();
		}else {
			comboBoxEquipamento.setValue(entity.getEquipamento());
		}
		
		
	}

	
	public void loadAssociatedObjects(boolean isEdit) {
	    if (areaService == null || equipamentoService == null) {
	        throw new IllegalStateException("Services were null");
	    }

	    List<Area> list = areaService.findAll();
	    obsListArea = FXCollections.observableArrayList(list);
	    obsListArea.add(0, null);
	    comboBoxArea.setItems(obsListArea);
		comboBoxArea.getSelectionModel().selectFirst();

	    if (isEdit && entity.getArea() != null) {
	        loadEquipamentosByArea(entity.getArea());
	    }
	}

	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		labelErrorTensao.setText(fields.contains("tensao") ? errors.get("tensao") : "");
		labelErrorCorrente.setText(fields.contains("corrente") ? errors.get("corrente") : "");
		labelErrorPotencia.setText(fields.contains("potencia") ? errors.get("potencia") : "");
		labelErrorFabricante.setText(fields.contains("fabricante") ? errors.get("fabricante") : "");
		labelErrorRotacao.setText(fields.contains("rotacao") ? errors.get("rotacao") : "");
		labelErrorArea.setText(fields.contains("area") ? errors.get("area") : "");
		labelErrorEquipamento.setText(fields.contains("equipamento") ? errors.get("equipamento") : "");
	}
	
	private void loadTensaoOptions() {
        ObservableList<String> tensaoOptions = FXCollections.observableArrayList(
            null, "220", "380", "440","760", "220/380","220/440","440/760" ,"220/380/440/760"
        );
        comboBoxTensao.setItems(tensaoOptions);
        comboBoxTensao.getSelectionModel().selectFirst();
    }
	
	private void initializeComboBoxTensao() {
        loadTensaoOptions();

        Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
	                setText("Selecione uma tensão");
	            } else {
	                setText(item);
	            }
            }
        };
        comboBoxTensao.setCellFactory(factory);
        comboBoxTensao.setButtonCell(factory.call(null));
    }
	
	private void initializeComboBoxArea() {
	    Callback<ListView<Area>, ListCell<Area>> factory = lv -> new ListCell<Area>() {
	        @Override
	        protected void updateItem(Area item, boolean empty) {
	            super.updateItem(item, empty);
	            if (item == null || empty) {
	                setText("Selecione uma área");
	            } else {
	                setText(item.getName());
	            }
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

	    Equipamento selectItem = new Equipamento();
	    selectItem.setName("Selecione um equipamento");
	    listEquipamento.add(0, selectItem);

	    obsListEquipamento = FXCollections.observableArrayList(listEquipamento);
	    comboBoxEquipamento.setItems(obsListEquipamento);
	    
	    comboBoxEquipamento.getSelectionModel().selectFirst();
	}

	private void initializeComboBoxEquipamento() {
	    Callback<ListView<Equipamento>, ListCell<Equipamento>> factory = lv -> new ListCell<Equipamento>() {
	        @Override
	        protected void updateItem(Equipamento item, boolean empty) {
	            super.updateItem(item, empty);
	            if (item == null || empty) {
	                setText("");
	            } else {
	                setText(item.getName());
	            }
	        }
	    };
	    comboBoxEquipamento.setCellFactory(factory);
	    comboBoxEquipamento.setButtonCell(factory.call(null));
	}

}
