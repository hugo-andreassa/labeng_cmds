package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
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
import model.entities.Fornecedor;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.service.FornecedorService;
import model.service.ProdutoService;

public class ProdutoFormController implements Initializable {

	private ProdutoService service;
	private FornecedorService fornecedorService;
	
	private Produto entity;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	private ObservableList<Fornecedor> obsList;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtDescricao;
	@FXML
	private TextField txtPreco;
	@FXML
	private ComboBox<Fornecedor> comboBoxFornecedor;
	
	@FXML
	private Label labelErroNome;
	@FXML
	private Label labelErroDescricao;
	@FXML
	private Label labelErroPreco;
	
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void setEntity(Produto entity) {
		this.entity = entity;
	}

	public void setServices(ProdutoService service, FornecedorService fornecedorService) {
		this.service = service;
		this.fornecedorService = fornecedorService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtnSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		}

		if (service == null) {
			throw new IllegalArgumentException("Service was null");
		}

		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();

		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch (DBException e) {
			Alerts.showAlert("Erro ao salvar fornecedor", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtnCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Produto getFormData() {
		Produto obj = new Produto();
		obj.setFornecedor(new Fornecedor());

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim() == "") {
			exception.addError("nome", "Field can't be empty");
		}
		obj.setNome(txtNome.getText());
		
		if (txtDescricao.getText() == null || txtDescricao.getText().trim() == "") {
			exception.addError("descricao", "Field can't be empty");
		}
		obj.setDescricao(txtDescricao.getText());
		
		if (txtPreco.getText() == null || txtPreco.getText().trim() == "") {
			exception.addError("preco", "Field can't be empty");
		}
		obj.setPreco(Utils.tryParseToDouble(txtPreco.getText()));
		
		obj.setFornecedor(comboBoxFornecedor.getValue());

		return obj;
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtDescricao.setText(entity.getDescricao());
		txtPreco.setText(String.valueOf(entity.getPreco()));
		if (entity.getFornecedor() == null) {
			comboBoxFornecedor.getSelectionModel().selectFirst();
		} else {
			comboBoxFornecedor.setValue(entity.getFornecedor());
		}
	}

	public void loadAssociatadObjects() {
		if (fornecedorService == null) {
			throw new IllegalArgumentException("Department Service was null");
		}

		List<Fornecedor> list = fornecedorService.findAll();

		obsList = (FXCollections.observableArrayList(list));
		comboBoxFornecedor.setItems(obsList);
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldMaxLength(txtDescricao, 100);
		Constraints.setTextFieldDouble(txtPreco);
		initializeComboBoxDepartment();
	}
	

	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErroNome.setText(errors.get("nome"));
		} else {
			labelErroNome.setText("");
		}
		
		if (fields.contains("descricao")) {
			labelErroDescricao.setText(errors.get("descricao"));
		} else {
			labelErroDescricao.setText("");
		}
		
		if (fields.contains("preco")) {
			labelErroPreco.setText(errors.get("preco"));
		} else {
			labelErroPreco.setText("");
		}
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxFornecedor.setCellFactory(factory);
		comboBoxFornecedor.setButtonCell(factory.call(null));
	}
}
