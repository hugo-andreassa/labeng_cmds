package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {
	
	public static void showAlert(String title, String header, String content, AlertType type) {
		// Instância o Alert passando o tipo
		Alert alert = new Alert(type);
		
		// Coloca o título do Alert
		alert.setTitle(title);
		// Coloca o cabeçalho do Alert
		alert.setHeaderText(header);
		// Coloca o conteúdo do Alert
		alert.setContentText(content);
		// Mostra o Alert
		alert.show();		
	}
	
	public static Optional<ButtonType> showConfirmation(String title, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		
		return alert.showAndWait();
	}
}
