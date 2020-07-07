package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Endereco;
import model.entities.Fornecedor;
import model.service.FornecedorService;

public class FornecedorListController implements Initializable, DataChangeListener {
	
	private FornecedorService service;
	private ObservableList<Fornecedor> obsList;
	
	@FXML
	private Button btRegistrar;
	@FXML
	private TableView<Fornecedor> tableViewFornecedor;
	@FXML
	private TableColumn<Fornecedor, Integer> tableColumnId;
	@FXML
	private TableColumn<Fornecedor, String> tableColumnNome;
	@FXML
	private TableColumn<Fornecedor, String> tableColumnTelefone;
	@FXML
	private TableColumn<Fornecedor, String> tableColumnEndereco;
	@FXML
	private TableColumn<Fornecedor, Fornecedor> tableColumnEDIT;
	@FXML
	private TableColumn<Fornecedor, Fornecedor> tableColumnREMOVE;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initialazeNodes();
	}
	
	public void setFornecedorService(FornecedorService service) {
		this.service = service;
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	@FXML
	private void onBtRegistrarAction(ActionEvent event) {
		Stage stage = gui.util.Utils.currentStage(event);
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setEndereco(new Endereco());
		createDialogForm(fornecedor, "/gui/FornecedorForm.fxml", stage);
	}

	private void initialazeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFornecedor.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalArgumentException("Service was null");
		}

		List<Fornecedor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFornecedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}	
	
	private void createDialogForm(Fornecedor obj, String absName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absName));
			Pane pane = loader.load();

			FornecedorFormController controller = loader.getController();
			controller.setEntity(obj);
			controller.setService(new FornecedorService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter fornecedor data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Fornecedor, Fornecedor>() {
			private final Button button = new Button("EDITAR");

			@Override
			protected void updateItem(Fornecedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/FornecedorForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Fornecedor, Fornecedor>() {
			private final Button button = new Button("REMOVER");

			@Override
			protected void updateItem(Fornecedor obj, boolean empty) {
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

	private void removeEntity(Fornecedor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Você realmente deseja deletar ?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				service.delete(obj.getId());
				updateTableView();
			} catch (DBIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
