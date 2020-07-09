package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Solicitacao;
import model.service.ProdutoService;
import model.service.SolicitacaoService;

public class SolicitacaoListController implements Initializable, DataChangeListener {

	private SolicitacaoService service;
	private ObservableList<Solicitacao> obsList;

	@FXML
	private Button btRegistrar;
	@FXML
	private TableView<Solicitacao> tableViewSolicitacao;
	@FXML
	private TableColumn<Solicitacao, Integer> tableColumnId;
	@FXML
	private TableColumn<Solicitacao, Date> tableColumnDataAbertura;
	@FXML
	private TableColumn<Solicitacao, Solicitacao> tableColumnSHOW;
	@FXML
	private TableColumn<Solicitacao, Solicitacao> tableColumnEDIT;
	@FXML
	private TableColumn<Solicitacao, Solicitacao> tableColumnREMOVE;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initialazeNodes();
	}

	public void setSolicitacaoService(SolicitacaoService service) {
		this.service = service;
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	@FXML
	private void onBtRegistrarAction(ActionEvent event) {
		Stage stage = gui.util.Utils.currentStage(event);
		Solicitacao obj = new Solicitacao();
		 
		createDialogForm(obj, "/gui/SolicitacaoForm.fxml", stage); 
	}

	private void initialazeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDataAbertura.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));
		Utils.formatTableColumnDate(tableColumnDataAbertura, "dd/MM/yyyy");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSolicitacao.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalArgumentException("Service was null");
		}

		List<Solicitacao> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSolicitacao.setItems(obsList);
		initShowButtons();
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Solicitacao obj, String absName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absName));
			Pane pane = loader.load();

			SolicitacaoFormController controller = loader.getController();
			controller.setEntity(obj);
			controller.setServices(new SolicitacaoService(), new ProdutoService());
			controller.loadAssociatadObjects();
			controller.updateTableView();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter produto data");
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

	private void createDialogList(Solicitacao obj, String absName, Stage parentStage) {
		try {
			if(obj.getItens().size() == 0) {
				Alerts.showAlert("Atenção", null, "Não existe nenhum item cadastrado nesta solicição!", AlertType.WARNING);
			} else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absName));
				Pane pane = loader.load();

				ItemListController controller = loader.getController();
				controller.setEntity(obj);
				controller.setService(new SolicitacaoService());
				controller.updateTableView();

				Stage dialogStage = new Stage();
				dialogStage.setTitle("Lista de itens da solicitação");
				dialogStage.setScene(new Scene(pane));
				dialogStage.setResizable(false);
				dialogStage.initOwner(parentStage);
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.showAndWait();	
			}
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void initShowButtons() {
		tableColumnSHOW.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnSHOW.setCellFactory(param -> new TableCell<Solicitacao, Solicitacao>() {
			private final Button button = new Button("MOSTRAR ITENS");

			@Override
			protected void updateItem(Solicitacao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogList(obj, "/gui/ItemList.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Solicitacao, Solicitacao>() {
			private final Button button = new Button("EDITAR");

			@Override
			protected void updateItem(Solicitacao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SolicitacaoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Solicitacao, Solicitacao>() {
			private final Button button = new Button("REMOVER");

			@Override
			protected void updateItem(Solicitacao obj, boolean empty) {
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

	private void removeEntity(Solicitacao obj) {
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
