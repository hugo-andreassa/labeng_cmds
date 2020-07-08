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
import model.entities.Fornecedor;
import model.entities.Produto;
import model.service.FornecedorService;
import model.service.ProdutoService;

public class ProdutoListController implements Initializable, DataChangeListener {
	
	private ProdutoService service;
	private ObservableList<Produto> obsList;
	
	@FXML
	private Button btRegistrar;
	@FXML
	private TableView<Produto> tableViewProduto;
	@FXML
	private TableColumn<Produto, Integer> tableColumnId;
	@FXML
	private TableColumn<Produto, String> tableColumnNome;
	@FXML
	private TableColumn<Produto, String> tableColumnDescricao;
	@FXML
	private TableColumn<Produto, Double> tableColumnPreco;
	@FXML
	private TableColumn<Produto, String> tableColumnFornecedor;
	@FXML
	private TableColumn<Produto, Produto> tableColumnEDIT;
	@FXML
	private TableColumn<Produto, Produto> tableColumnREMOVE;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initialazeNodes();
	}
	
	public void setProdutoService(ProdutoService service) {
		this.service = service;
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	@FXML
	private void onBtRegistrarAction(ActionEvent event) {
		Stage stage = gui.util.Utils.currentStage(event);
		Produto produto = new Produto();
		produto.setFornecedor(new Fornecedor());
		createDialogForm(produto, "/gui/ProdutoForm.fxml", stage);
	}

	private void initialazeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tableColumnFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalArgumentException("Service was null");
		}

		List<Produto> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProduto.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}	
	
	private void createDialogForm(Produto obj, String absName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absName));
			Pane pane = loader.load();

			ProdutoFormController controller = loader.getController();
			controller.setEntity(obj);
			controller.setServices(new ProdutoService(), new FornecedorService());
			controller.loadAssociatadObjects();
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("EDITAR");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ProdutoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("REMOVER");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
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

	private void removeEntity(Produto obj) {
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
