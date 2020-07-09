package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.FornecedorService;
import model.service.ProdutoService;
import model.service.SolicitacaoService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemFornecedor;
	@FXML
	private MenuItem menuItemProduto;
	@FXML
	private MenuItem menuItemSolicitacao;
	@FXML
	private MenuItem menuItemAbout;
	
	// Esse método vem da Interface Initializable e serve 
	// para executar coisas ao carregar a página
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@FXML
	public void onMenuItemSolicitacaoAction() {
		loadView("/gui/SolicitacaoList.fxml", (SolicitacaoListController controller) -> {
			controller.setSolicitacaoService(new SolicitacaoService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemProdutoAction() {
		loadView("/gui/ProdutoList.fxml", (ProdutoListController controller) -> {
			controller.setProdutoService(new ProdutoService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemFornecedorAction() {
		loadView("/gui/FornecedorList.fxml", (FornecedorListController controller) -> {
			controller.setFornecedorService(new FornecedorService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", controller -> {});
	}
	
	private synchronized <T> void loadView(String absName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absName));
			VBox newVBox = loader.load();
			
			// Pega a cena principal
			Scene mainScene = Main.getMainScene();
			// Pega o VBox da cena principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			// Pega o menu principal do VBox
			Node mainMenu = mainVBox.getChildren().get(0);
			// Limpa o vbox
			mainVBox.getChildren().clear();
			// Add o menu principal dnv
			mainVBox.getChildren().add(mainMenu);
			// Add os filhos do newVBox
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("Error loading view", null, e.getMessage(), AlertType.ERROR);
		}
	}
}
