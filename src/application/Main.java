package application;

import entities.Endereco;
import entities.Fornecedor;
import entities.Produto;

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
		
		Produto p1 = new Produto(1, "Cimento", "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", 20.99, f1);
		Produto p2 = new Produto(2, "Tijolo", "Lorem ipsum dolor sit amet.", 15.99, f1);
		Produto p3 = new Produto(3, "Saco de areia", "Lorem ipsum dolor sit amet consectetur.", 34.0, null);
		
		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		
	}
}
