package org.openjfx.SnakeGame.businessLogic;

import org.openjfx.SnakeGame.model.DataBean;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Use this class to show a specify confirm MsgBox in a JavaFX application.
 * 
 * @author CSD
 *
 */
public class ConfirmMsgBox {
	
	// static member field for the button who was clicked by mouse
	static ButtonType pressedBtn = null;

	/**
	 * Method shows a confirm MsgBox to ask user what he want to do.
	 * This MsgBox ignore all KeyPressed events and can only managed
	 * with mouse.
	 * 
	 * @param titleBar      ->	Title from msgbox
	 * @param headerMessage ->	Header message
	 * @param infoMessage   ->	Info message in messageBox
	 * @param imgTitleBar   ->	Image for Title infobox
	 * @param imgCont       ->	Image for mesageBox content
	 * 
	 * @return 				->	boolean true if YES-Button was clicked, otherwise false
	 * 
	 */
	public static boolean show(Stage mainWindow, Boolean showAndWait, Boolean showCheckBox, String titleBar, String headerMessage,
			String infoMessage, Image imgTitleBar, Image imgCont, DataBean model) {
		
		// create new dialog pane of typ confirmation
		Alert confirmMsg = new Alert(AlertType.CONFIRMATION);
		
		// set height of message box higher to be content text is full showing
		// over two lines -> this property is used on some windows machines
		//confirmMsg.getDialogPane().setPrefHeight(150.0);
		
		// Binding this MsgBox on a stage (for example a primary stage)
		// to show this MsgBox in front of a specify window
		if(mainWindow != null) {		
			confirmMsg.initOwner(mainWindow);
			// blocks all interactions with other windows from application
			confirmMsg.initModality(Modality.APPLICATION_MODAL);
		}
		
		// remove default ok/cancel buttons
		confirmMsg.getButtonTypes().clear();

		// add new yes/no buttons
		confirmMsg.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		// Activate Defaultbehavior for YES-Button and change Text to english
		Button yesBtn = (Button) confirmMsg.getDialogPane().lookupButton(ButtonType.YES);
		yesBtn.setText("Yes");
//		yesBtn.setDefaultButton(true);
		
		// Deactivate Defaultbehavior for NO-Button and change text to english
		Button noBtn = (Button) confirmMsg.getDialogPane().lookupButton(ButtonType.NO);
		noBtn.setText("No");

		// set text in MsgBox
		if(titleBar != null) {
			confirmMsg.setTitle(titleBar);
		}
		if(headerMessage != null) {
			confirmMsg.setHeaderText(headerMessage);
		}
		if(infoMessage != null) {
			confirmMsg.setContentText(infoMessage);
		}
		
		// cast this MsgBox in a scene object to apply
		// new EventFilter on this node
		Scene scene = confirmMsg.getDialogPane().getScene();
		
		// EventHandler for managed the KeyPressed events
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// alle KeyPressed Events ignorieren!!!
				event.consume();
			}
		});
		
		
		// cast this MsgBox to a stage object, to add new title image
		Stage stage = (Stage) confirmMsg.getDialogPane().getScene().getWindow();
		// add new title
		if (imgTitleBar != null) {
			stage.getIcons().add(imgTitleBar);
		}
		// add new image for MsgBox content
		if (imgCont != null) {
			confirmMsg.setGraphic(new ImageView(imgCont));
		}

		// set this MsgBox always on top
		//stage.setAlwaysOnTop(true);
		
		// this flag is for show a checkbox in MsgBox
		if(showCheckBox != null && showCheckBox && model != null) {
			// HBox for the checkBox -> don't show again
			HBox hBoxCheckBox = new HBox();
			// create new CheckBox
			CheckBox checkBox = new CheckBox("Do not show again");
			/*
			 * IMPORTANT:
			 * the min width of this check box must not be set on Double.MAX_VALUE
			 * otherwise the buttons "YES" and "NO" be affected of this checkbox
			 */
			checkBox.setMinWidth(140.0);
			// add checkBox to hBox
			hBoxCheckBox.getChildren().add(checkBox);
			// place the HBox on the right position
			hBoxCheckBox.setTranslateX(10.0);
			hBoxCheckBox.setTranslateY(108.0);
			confirmMsg.getDialogPane().getChildren().add(hBoxCheckBox);
			
			// add InvalidationListener to listens of checkbox was checked or not
			checkBox.selectedProperty().addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable observable) {
					// TODO Auto-generated method stub
					if(checkBox.isSelected()) {
						// change flag in model, to hide infoBox at game start
						model.setFlagForInfoBoxShowing(false);
					}
					else {
						// change flag in model, to show msgBox again
						model.setFlagForInfoBoxShowing(true);
					}
				}
			});
		}
			
		// show MsgBox until a user interaction has taken place
		if (showAndWait != null && showAndWait) {
			// saved pressed button temporarily	
			pressedBtn = confirmMsg.showAndWait().get();
	
		} else if (showAndWait != null && !showAndWait) {
			// saved pressed button temporarily	
			pressedBtn = confirmMsg.getResult();
			// show MsgBox
			confirmMsg.show();
		}
		
		// if YES-Button was clicked with mouse
		// -> return true
		if (pressedBtn == ButtonType.YES) {
			return true;
		}
		
		// otherwise return false
		return false;
		
	}
}
