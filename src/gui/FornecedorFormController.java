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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Endereco;
import model.entities.Fornecedor;
import model.exceptions.ValidationException;
import model.service.FornecedorService;

public class FornecedorFormController implements Initializable {

	private FornecedorService service;
	
	private Fornecedor entity;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtTelefone;
	@FXML
	private TextField txtRua;
	@FXML
	private TextField txtCidade;
	@FXML
	private TextField txtEstado;
	@FXML
	private TextField txtComplemento;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtCep;
	
	@FXML
	private Label labelErroNome;
	@FXML
	private Label labelErroTelefone;
	@FXML
	private Label labelErroRua;
	@FXML
	private Label labelErroCidade;
	@FXML
	private Label labelErroEstado;
	@FXML
	private Label labelErroNumero;
	@FXML
	private Label labelErroCep;
	
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void setEntity(Fornecedor entity) {
		this.entity = entity;
	}

	public void setService(FornecedorService service) {
		this.service = service;
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

	private Fornecedor getFormData() {
		Fornecedor obj = new Fornecedor();
		obj.setEndereco(new Endereco());

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim() == "") {
			exception.addError("nome", "Field can't be empty");
		}
		obj.setNome(txtNome.getText());
		
		if (txtTelefone.getText() == null || txtTelefone.getText().trim() == "") {
			exception.addError("telefone", "Field can't be empty");
		}
		obj.setTelefone(txtTelefone.getText());
		
		if (txtRua.getText() == null || txtRua.getText().trim() == "") {
			exception.addError("rua", "Field can't be empty");
		}
		obj.getEndereco().setRua(txtRua.getText());
		
		if (txtCidade.getText() == null || txtCidade.getText().trim() == "") {
			exception.addError("cidade", "Field can't be empty");
		}
		obj.getEndereco().setCidade(txtCidade.getText());
		
		if (txtEstado.getText() == null || txtEstado.getText().trim() == "") {
			exception.addError("estado", "Field can't be empty");
		}
		obj.getEndereco().setEstado(txtEstado.getText());
		
		if (txtNumero.getText() == null || txtNumero.getText().trim() == "") {
			exception.addError("numero", "Field can't be empty");
		}
		obj.getEndereco().setNumero(txtNumero.getText());
		
		if (txtCep.getText() == null || txtCep.getText().trim() == "") {
			exception.addError("cep", "Field can't be empty");
		}
		obj.getEndereco().setCep(txtCep.getText());
		
		obj.getEndereco().setComplemento(txtComplemento.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(entity.getTelefone());
		
		txtRua.setText(entity.getEndereco().getRua());
		txtCidade.setText(entity.getEndereco().getCidade());
		txtEstado.setText(entity.getEndereco().getEstado());
		txtComplemento.setText(entity.getEndereco().getComplemento());
		txtNumero.setText(entity.getEndereco().getNumero());
		txtCep.setText(entity.getEndereco().getCep());	
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 80);
		Constraints.setTextFieldMaxLength(txtTelefone, 11);
	}
	

	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("nome")) {
			labelErroNome.setText(errors.get("nome"));
		} else {
			labelErroNome.setText("");
		}
		
		if (fields.contains("telefone")) {
			labelErroTelefone.setText(errors.get("telefone"));
		} else {
			labelErroTelefone.setText("");
		}
		
		if (fields.contains("rua")) {
			labelErroRua.setText(errors.get("rua"));
		} else {
			labelErroRua.setText("");
		}
		
		if (fields.contains("cidade")) {
			labelErroCidade.setText(errors.get("cidade"));
		} else {
			labelErroCidade.setText("");
		}
		
		if (fields.contains("estado")) {
			labelErroEstado.setText(errors.get("estado"));
		} else {
			labelErroEstado.setText("");
		}
		
		if (fields.contains("numero")) {
			labelErroNumero.setText(errors.get("numero"));
		} else {
			labelErroNumero.setText("");
		}
		
		if (fields.contains("cep")) {
			labelErroCep.setText(errors.get("cep"));
		} else {
			labelErroCep.setText("");
		}
	}
}
