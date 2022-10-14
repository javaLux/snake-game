package org.openjfx.SnakeGame.businessLogic;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Use this class to show a specify info MsgBox in a JavaFX application.
 * 
 * @author CSD
 *
 */
public class InfoBox {

	/**
	 * Method shows a info MsgBox to tell user important informations.
	 * 
	 * @param titleBar      ->	Title form msgbox
	 * @param headerMessage ->	Header message
	 * @param infoMessage   ->	Info message in messageBox
	 * @param imgTitleBar   ->	Image for Title infobox
	 * @param imgCont       ->	Image for mesageBox content
	 * 
	 * @return 				->	boolean true if YES-Button was clicked, otherwise false
	 * 
	 */
	public static boolean showInfoBox(Stage mainWindow, Boolean showAndWait, String titleBar, String headerMessage,
			String infoMessage, Image imgTitleBar, Image imgCont) {
		
		// create new dialog pane of typ information			
		Alert infoMsg = new Alert(AlertType.INFORMATION);
		
		// Binding this MsgBox on a stage (for example a primary stage)
		// to show this MsgBox in front of a specify window
		if(mainWindow != null) {		
			infoMsg.initOwner(mainWindow);
			// blocks all interactions with other windows from application
			infoMsg.initModality(Modality.APPLICATION_MODAL);
				}
		
		// set text to MsgBox
		if (titleBar != null) {
			infoMsg.setTitle(titleBar);
		}
		
		if (headerMessage != null) {
			infoMsg.setHeaderText(headerMessage);
		}
		
		if (infoMessage != null) {
			infoMsg.setContentText(infoMessage);
		}
		
//		stage.setAlwaysOnTop(true);
			
		// cast this MsgBox to a stage object, to add new title image
		Stage stage = (Stage) infoMsg.getDialogPane().getScene().getWindow();
		
		// add new title
		if (imgTitleBar != null) {
			stage.getIcons().add(imgTitleBar);
		}
		// add new image for MsgBox content
		if (imgCont != null) {
			infoMsg.setGraphic(new ImageView(imgCont));
		}

		// get the value if ok button is pressed
		Optional<ButtonType> result = infoMsg.showAndWait();
		ButtonType button = result.orElse(ButtonType.OK);
		// if ok button was clicked -> return true
		if(button == ButtonType.OK) {
			return true;
		}
		
		return false;
	}
	
}
