package org.openjfx.SnakeGame.Controller;

import org.openjfx.SnakeGame.View.HighScoreView;
import org.openjfx.SnakeGame.businessLogic.UndecoratedWindow;
import org.openjfx.SnakeGame.model.DataBean;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class HighScoreViewController {
	
	// instance members
	//
	// model
	private DataBean dataBean;
	// HighScoreView
	private HighScoreView highscoreView;
	
	public HighScoreViewController(DataBean dataBean) {
		// save model
		this.dataBean = dataBean;
		
		//  initialize new Highscore View
		this.highscoreView = new HighScoreView();
		
		// add property to zoom in and zomm out from buttons "move" and "exit"
		this.zoomIn(this.highscoreView.getBtnMove());
		this.zoomOut(this.highscoreView.getBtnMove());
		this.zoomIn(this.highscoreView.getBtnExit());
		this.zoomOut(this.highscoreView.getBtnExit());
		
		// make highscore moveable on desktop with
		// Helper class "UndecoratedWindow"
		UndecoratedWindow stageToMove = new UndecoratedWindow();
		stageToMove.allowDragOnButton(this.dataBean.getHighscoreStage(), this.highscoreView.getBtnMove());
		
		// +++++ start of initialize EventHandler +++++
		//
		this.highscoreView.getBtnExit().setOnMouseClicked(new HighscoreViewEventhandlerBtnExit());
		// +++++ end of initialize EventHandler +++++
	}
	
	/**
	 * Methode erstellt eine separate Stage für die Einstellungen so das die
	 * Settings View angezeigt werden kann
	 * 
	 * @param onlyShowHighscoreStage	->	boolean to controll methods call
	 * 										in class "HighscoreView" to show
	 * 										highscore stage
	 */
	public void show(boolean onlyShowHighscoreStage, String highscoreValue) {
		// update label "highscoreValue" in highscoreView before showing
		this.highscoreView.getLblHighscoreValue().setText(highscoreValue);
		
		/*
		 * transfer the new highscore stage to the highscore view
		 * and transfer the owner stage to place the highscoreView
		 * in the startView window
		 * 
		 */
		this.highscoreView.show(this.dataBean.getHighscoreStage(), this.dataBean.getStartStage(), onlyShowHighscoreStage);
	}
	
	/**
	 * Methode handle the mouse event when cursor entered to the button
	 * Make size from button bigger
	 * 
	 * @param icon	->	the current Button that used
	 */
	private void zoomIn(Button btn) {
		btn.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				btn.setCursor(Cursor.HAND);
				btn.setScaleX(1.0);
				btn.setScaleY(1.0);
			}
		});
	}

	/**
	 * Methode handle the mouse event when cursor exited from the button
	 * Make size of button smaller
	 * 
	 * @param icon	->	the current Button that used
	 */
	private void zoomOut(Button btn) {
		btn.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				btn.setScaleX(0.8);
				btn.setScaleY(0.8);
			}
		});
	}
	
	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Eventhandling
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	
	/**
	 * Eventhandler class that handle mouse click event on the exit button
	 * 
	 * @author CSD
	 *
	 */
	class HighscoreViewEventhandlerBtnExit implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// if primary mouse button was clicked on exit button
			// than close highscore stage
			// Do follow action if primary mouse button is clicked
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				dataBean.getHighscoreStage().close();
				// activate highscore button in startView
				dataBean.getStartViewController().getHighscoreButtonFromStartView().setDisable(false);
			}
		}
		
	}
	

}
