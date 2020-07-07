package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	
	public static void main(String[] args) {
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
}
