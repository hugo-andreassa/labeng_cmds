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
import model.service.SolicitacaoService;

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
		SolicitacaoService ss = new SolicitacaoService();
		
		// Insert
		Fornecedor f = new Fornecedor(null, "Copafer", "11956492900", 
				new Endereco(null, "Rua", "Cidade", "Estado", "Complemento", "A45", "CEP"));
		sf.saveOrUpdate(f);
		f = sf.findById(1);
		
		// FindById
		Produto p1 = new Produto(null, "Tijolo", "Para contruir casas", 30.10, f); 
		Produto p2 = new Produto(null, "Porta de ferro", "Para fechar casas", 100.10, f);
		sp.saveOrUpdate(p1);
		sp.saveOrUpdate(p2);
		p1 = sp.findById(1);
		p2 = sp.findById(2);
		
		ItemSolicitacao is1 = new ItemSolicitacao(1, p1.getPreco(), StatusItem.NAO_COMPRADO, p1);
		ItemSolicitacao is2 = new ItemSolicitacao(1, p2.getPreco(), StatusItem.NAO_COMPRADO, p2);
		Solicitacao s = new Solicitacao(null, new Date(), StatusSolicitacao.ABERTO);
		s.addItens(is1);
		s.addItens(is2);
		ss.saveOrUpdate(s);
		
		s = ss.findById(1);
		s.setStatus(StatusSolicitacao.CANCELADO);
		s.getItens().get(1).setStatus(StatusItem.COMPRADO);
		ss.saveOrUpdate(s);
		
		for (Solicitacao sol : ss.findAll()) {
			System.out.println(sol);
			for (ItemSolicitacao i : sol.getItens()) {
				System.out.println(i);
			}
		}		
	}
}
