package application;

import java.util.List;

import model.entities.Endereco;
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
		
		// Insert
		Fornecedor obj = new Fornecedor(null, "Copafer", "11956492900", 
				new Endereco(null, "Rua", "Cidade", "Estado", "Complemento", "A45", "CEP"));
		service.saveOrUpdate(obj);
		
		// FindById and Update
		obj = service.findById(2);
		obj.setNome("Mafecom");
		service.saveOrUpdate(obj);
		
		// Delete
		service.delete(1);
		
		// FindAll
		List<Fornecedor> list = service.findAll();
		for (Fornecedor fornecedor : list) {
			System.out.println(fornecedor);
		}
	}
}
