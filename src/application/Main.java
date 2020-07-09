package application;

import java.util.Date;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import model.entities.Endereco;
import model.entities.Fornecedor;
import model.entities.ItemSolicitacao;
import model.entities.Produto;
import model.entities.Solicitacao;
import model.entities.enums.StatusItem;
import model.entities.enums.StatusSolicitacao;
import model.service.FornecedorService;
import model.service.ProdutoService;
import model.service.SolicitacaoService;

public class Main extends Application {
	
	private static Scene mainScene;
	
	public static void main(String[] args) {
		// loadDatabase();
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Instância o MainView no loader
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			// Carrega a MainView que estava no loader para um Parent
			ScrollPane scrollPane = loader.load();
			
			// Coloca para encaixar na altura e na largura
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			// Carrega o Parent como uma cena
			mainScene = new Scene(scrollPane);
			// Set o mainScene no palco
			primaryStage.setScene(mainScene);
			// Coloca um título
			primaryStage.setTitle("Construction Demand Managment System");
			// Mostra o palco
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	private static void loadDatabase() {
		FornecedorService fs = new FornecedorService();
		ProdutoService ps = new ProdutoService();
		SolicitacaoService ss = new SolicitacaoService();
		
		Endereco e1 = new Endereco(null, "Rua das Monções", "SP", "SP", "", "1510", "00000000");
		Endereco e2 = new Endereco(null, "Rua das Figueiras", "SP", "SP", "", "1300", "00000000");
		
		Fornecedor f1 = new Fornecedor(null, "Loja de materiais", "11956492900", e1);
		Fornecedor f2 = new Fornecedor(null, "Copafer", "11956492900", e2);
		fs.saveOrUpdate(f1);
		fs.saveOrUpdate(f2);
		f1 = fs.findById(1);
		f2 = fs.findById(2);
		
		Produto p1 = new Produto(null, "Tijolo", "Usado para erguer paredes", 20.10, f1);
		Produto p2 = new Produto(null, "Cimento", "Usado para reforçar paredes", 19.90, f2);
		ps.saveOrUpdate(p1);
		ps.saveOrUpdate(p2);
		p1 = ps.findById(1);
		p2 = ps.findById(2);
		
		ItemSolicitacao is1 = new ItemSolicitacao(1000, p1.getPreco(), StatusItem.NAO_COMPRADO, p1);
		ItemSolicitacao is2 = new ItemSolicitacao(10, p2.getPreco(), StatusItem.NAO_COMPRADO, p2);
		
		Solicitacao s1 = new Solicitacao(null, new Date(), StatusSolicitacao.ABERTO);
		s1.addItens(is1);
		s1.addItens(is2);
		ss.saveOrUpdate(s1);
		s1 = ss.findById(1);	
		
	}
}






