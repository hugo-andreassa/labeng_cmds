package application;

import java.util.Date;

import model.entities.Endereco;
import model.entities.Fornecedor;
import model.entities.ItemSolicitacao;
import model.entities.Produto;
import model.entities.Solicitacao;
import model.entities.enums.StatusItem;
import model.entities.enums.StatusSolicitacao;
import model.service.FornecedorService;
import model.service.ProdutoService;

public class Main /*extends Application*/ {
	
	/*@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) {
		// launch(args);
		
		FornecedorService sf = new FornecedorService();
		ProdutoService sp = new ProdutoService();
		
		// Insert
		Fornecedor f = new Fornecedor(null, "Copafer", "11956492900", 
				new Endereco(null, "Rua", "Cidade", "Estado", "Complemento", "A45", "CEP"));
		sf.saveOrUpdate(f);
		
		// FindById and Update
		f = sf.findById(1);
		
		// FindById
		Produto p = new Produto(null, "Tijolo", "Para contruir casas", 30.10, f); 
		p = sp.findById(1);
		
		ItemSolicitacao is = new ItemSolicitacao(3, p.getPreco(), StatusItem.NAO_COMPRADO, p);
		Solicitacao s = new Solicitacao(null, new Date(), StatusSolicitacao.ABERTO);
		s.addItens(is);
		
		
		
	}
}
