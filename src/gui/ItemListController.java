package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.entities.ItemSolicitacao;
import model.entities.Solicitacao;
import model.service.SolicitacaoService;

public class ItemListController implements Initializable, DataChangeListener {
	
	private SolicitacaoService service;
	
	private Solicitacao entity;
	
	private ObservableList<ItemSolicitacao> obsList;
	
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

	public void setService(SolicitacaoService service) {
		this.service = service;
	}
	
	public void setEntity(Solicitacao entity) {
		this.entity = entity;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initialazeNodes();
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initialazeNodes() {
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("produto"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewItemSolicitacao.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		List<ItemSolicitacao> list = entity.getItens();
		obsList = FXCollections.observableArrayList(list);
		tableViewItemSolicitacao.setItems(obsList);
		initRemoveButtons();
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
				service.saveOrUpdate(entity);
				updateTableView();
			} catch (DBIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
