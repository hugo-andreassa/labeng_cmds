package application;

import java.util.Date;

import model.entities.Endereco;
import model.entities.Fornecedor;
import model.entities.ItemSolicitacao;
import model.entities.Produto;
import model.entities.Solicitacao;
import model.entities.enums.StatusItem;
import model.entities.enums.StatusSolicitacao;

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
		
		Endereco end1 = new Endereco(null, "Rua estrada adutora do rio claro", "Mauá", "SP", "Casa 42", 1501, "900320");
		
		Fornecedor f1 = new Fornecedor(null, "Casa do Material", "11956492600", end1);
		
		Produto p1 = new Produto(1, "Cimento", "Cimento para parde", 20.99, f1);
		Produto p2 = new Produto(2, "Tijolo", "Tijolo para laje", 15.99, f1);
		// Produto p3 = new Produto(3, "Saco de areia", "Lorem ipsum dolor sit amet consectetur.", 34.0, null);
		
		ItemSolicitacao is1 = new ItemSolicitacao(1, p1.getPreco(), StatusItem.NAO_COMPRADO, p1);
		ItemSolicitacao is2 = new ItemSolicitacao(2, p2.getPreco(), StatusItem.NAO_COMPRADO, p2);
		
		Solicitacao s1 = new Solicitacao(null, new Date(), StatusSolicitacao.ABERTO);
		s1.addItens(is1);
		s1.addItens(is2);
		
		System.out.println(s1.total());
		for (ItemSolicitacao item : s1.getItens()) {
			System.out.println(item.subTotal());
		}
	}
}
