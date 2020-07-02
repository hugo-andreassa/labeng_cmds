package application;

import java.util.List;

import model.entities.Fornecedor;
import model.service.FornecedorService;

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
		
		FornecedorService service = new FornecedorService();
		Fornecedor forn = service.findById(2);
		
		System.out.println(forn);
		
		/*List<Fornecedor> list = service.findAll();
		
		for (Fornecedor fornecedor : list) {
			System.out.println(fornecedor);
		}*/
	}
}
