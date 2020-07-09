package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import db.DBException;
import db.DBIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.ItemSolicitacao;
import model.entities.Produto;
import model.entities.Solicitacao;
import model.entities.enums.StatusItem;
import model.entities.enums.StatusSolicitacao;
import model.exceptions.ValidationException;
import model.service.ProdutoService;
import model.service.SolicitacaoService;

public class SolicitacaoFormController implements Initializable {

	private SolicitacaoService service;
	private ProdutoService produtoService;
	
	private Solicitacao entity;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	private ObservableList<Produto> obsList;
	private ObservableList<ItemSolicitacao> obsListItens;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtQuantidade;
	@FXML
	private ComboBox<Produto> comboBoxProduto;
	
	@FXML
	private TableView<ItemSolicitacao> tableViewItemSolicitacao;
	@FXML
	private TableColumn<ItemSolicitacao, Integer> tableColumnQuantidade;
	@FXML
	private TableColumn<ItemSolicitacao, Double> tableColumnPreco;
	@FXML
	private TableColumn<ItemSolicitacao, String> tableColumnProduto;
	@FXML
	private TableColumn<ItemSolicitacao, ItemSolicitacao> tableColumnREMOVE;
	
	@FXML
	private Button btnAdicionar;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnCancelar;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void setEntity(Solicitacao entity) {
		this.entity = entity;
	}

	public void setServices(SolicitacaoService service, ProdutoService produtoService) {
		this.service = service;
		this.produtoService = produtoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtnAdicionarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		}

		if (service == null) {
			throw new IllegalArgumentException("Service was null");
		}
	
		try {
			ItemSolicitacao is = new ItemSolicitacao();
			is.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
			is.setPreco(comboBoxProduto.getValue().getPreco());
			is.setStatus(StatusItem.NAO_COMPRADO);
			is.setProduto(comboBoxProduto.getValue());	
			
			entity.addItens(is);
			
			txtQuantidade.setText("");
			comboBoxProduto.getSelectionModel().selectFirst();
			
			updateTableView();
		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch (DBException e) {
			Alerts.showAlert("Erro ao salvar item", null, e.getMessage(), AlertType.ERROR);
		}
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
			entity.setStatus(StatusSolicitacao.ABERTO);
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

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId()));
	}

	public void loadAssociatadObjects() {
		if (produtoService == null) {
			throw new IllegalArgumentException("Department Service was null");
		}

		List<Produto> list = produtoService.findAll();

		obsList = (FXCollections.observableArrayList(list));
		comboBoxProduto.setItems(obsList);
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));	

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewItemSolicitacao.prefWidthProperty().bind(stage.widthProperty());
		
		initializeComboBoxProduto();
	}
	
	public void updateTableView() {
		List<ItemSolicitacao> list = entity.getItens();
		obsListItens = FXCollections.observableArrayList(list);
		tableViewItemSolicitacao.setItems(obsListItens);
		initRemoveButtons();
	}	
	
	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();		
	}
	
	private void initializeComboBoxProduto() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxProduto.setCellFactory(factory);
		comboBoxProduto.setButtonCell(factory.call(null));
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<ItemSolicitacao, ItemSolicitacao>() {
			private final Button button = new Button("REMOVER");

			@Override
			protected void updateItem(ItemSolicitacao obj, boolean empty) {
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

	private void removeEntity(ItemSolicitacao obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Você realmente deseja deletar ?");

		if (result.get() == ButtonType.OK) {			
			try {
				entity.getItens().remove(obj);
				updateTableView();
			} catch (DBIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
