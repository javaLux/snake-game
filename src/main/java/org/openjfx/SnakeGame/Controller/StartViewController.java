package org.openjfx.SnakeGame.Controller;

import org.openjfx.SnakeGame.View.StartView;
import org.openjfx.SnakeGame.model.DataBean;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Klasse zum steuern und verwalten der User Interaktionen mit der Start View
 * (GUI) -> sprich hier wird die Logik implementiert was passieren soll wenn der
 * User z.B. auf einen Button klickt etc. Der Controller verbindet die View und
 * das Model. Das heißt Werte aus dem Model werden in der View gelesen oder
 * gesetzt. Außerdem werden benötigte Eventhandler registriert und Aufgabe an
 * die ggf. vorhandene Geschäftslogik weitergeleitet! Des Weiteren steuert der
 * Controller, wann und auf welche UI-Maske die Anwendung wechseln soll.
 * 
 * Wichtigste Aufgaben des View Controllers: -> verbindet View und Model ->
 * enthält Steuerungslogik -> delegierte Geschäftslogik, enthält sie aber nicht!
 * 
 * @author Christian
 *
 */
public class StartViewController {
	// Instanzvariablen
	//
	// Daten (Model)
	private DataBean dataBean;
	// startView (View die beim Start angezeigt werden soll)
	private StartView startView;

	/**
	 * Konstruktor sichert das übergebene Model und initialisiert die View
	 * 
	 * @param dataBean
	 */
	public StartViewController(DataBean dataBean) {
		// Model sichern
		this.dataBean = dataBean;
		// die Start (View) erzeugen und sichern
		this.startView = new StartView();

		// Eventhandler registrieren/zuweisen
		//
		// somit überwacht / registriert der View Controller die Interaktionen des User
		// mit der View
		this.startView.getBtnPlay().setOnMouseClicked(new StartViewEventhandlerBtnPlay());
		this.startView.getBtnHighscore().setOnMouseClicked(new StartViewEventhandlerBtnHighScore());
		this.startView.getBtnSettings().setOnMouseClicked(new StartViewEventhandlerBtnSettings());
	}

	/**
	 * Methode stellt die Verbindung zwischen PrimaryStage (Model) und der InputView
	 * her. So das die InputView in der PrimaryStage angezeigt werden kann
	 */
	public void show() {
		/*
		 * übergeben der primaryStage an die Eingabe View -> Wichtig da es immer nur ein
		 * Hauptfenster innerhalb des Programms geben kann!!! Die primaryStage wird
		 * intern im Model gehalten als wichtigstes Objekt
		 */
		this.startView.show(this.dataBean.getStartStage());
	}

	/**
	 * Hilfsmethode damit über den Controller der Settings-Button wieder aktiviert
	 * werden kann.
	 * 
	 * @return -> Button Settings
	 */
	public Button getSettingsButtonFromStartView() {
		return this.startView.getBtnSettings();
	}
	
	/**
	 * Hilfsmethode damit über den Controller der Highscore-Button wieder aktiviert
	 * werden kann.
	 * 
	 * @return -> Button Highscore
	 */
	public Button getHighscoreButtonFromStartView() {
		return this.startView.getBtnHighscore();
	}
	
	/**
	 * Hilfsmethode damit über den Controller der Play-Button wieder aktiviert
	 * werden kann.
	 * 
	 * @return -> Button Play
	 */
	public Button getPlayButtonFromStartView() {
		return this.startView.getBtnPlay();
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Eventhandling
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Eventhandler -> für den Button-Play -> dieser reagiert sobald mit der Maus
	 * auf diesen geklickt wird
	 * 
	 * @author Christian
	 */
	class StartViewEventhandlerBtnPlay implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// wenn noch keine Instanz des Game-Controller existiert -> dann eine neue
			// erzeugen und im Model speichern
			if (!(dataBean.getGameController() instanceof GameViewController)) {
				
				GameViewController gameViewController = new GameViewController(dataBean);
				dataBean.setGameController(gameViewController);
				// show game scene
				dataBean.getGameController().show();
				
				// buttons der startView deaktivieren -> um diese nicht während des Spiels zu betätigen
				startView.getBtnPlay().setDisable(true);
				startView.getBtnHighscore().setDisable(true);
				startView.getBtnSettings().setDisable(true);

				// Spielanimation und somit das Spiel starten
				dataBean.getGameController().FirstStartGameAnimation();
			}
			// Falls schon eine Instanz des GameControllers (des Spiels) besteht, wird nur die
			// GameView wieder angezeigt!!!
			else {
				dataBean.getGameController().resumeGame();
				dataBean.getGameController().show();
				startView.getBtnSettings().setDisable(true);
			}
		}
	}
	
	/**
	 * Eventhandler -> für den Button-HighScore -> dieser reagiert sobald mit der
	 * Maus auf diesen geklickt wird
	 * 
	 * @author Christian
	 */
	class StartViewEventhandlerBtnHighScore implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			
			// get highscore from model
			Integer highscore = dataBean.getHighScoreAtStart();
			
			// check highscore value -> if is null than got "Not available" otherwise
			// the current integer value as string
			// in this case we use a condition operator (tertiary operator)
			// [condition] ? [Expression if true] : [Expression if false]
			String highscoreAsString = highscore > 0 ? highscore.toString() : "unavailable";
			
			// if currently no instance of HighScoreController exists
			// than create new instance and save this instance in model
			if (!(dataBean.getHighscoreController() instanceof HighScoreViewController)) {
				// create new instance
				HighScoreViewController highscoreViewController = new HighScoreViewController(dataBean);
				// save instance in model
				dataBean.setHighscoreController(highscoreViewController);
				// show highscore stage
				dataBean.getHighscoreController().show(false, highscoreAsString);
				// deactivate the highscore button in startView
				startView.getBtnHighscore().setDisable(true);

			}
			
			// if already exists a instance of HighScoreController
			// ONLY show the highscore stage
			else {
				// ONLY show highscore stage
				dataBean.getHighscoreController().show(true, highscoreAsString);
				// deactivate the highscore button in startView
				startView.getBtnHighscore().setDisable(true);
			}
			
		}	
	}

	/**
	 * Eventhandler -> für den Button-Settings -> dieser reagiert sobald mit der
	 * Maus auf diesen geklickt wird
	 * 
	 * @author Christian
	 */
	class StartViewEventhandlerBtnSettings implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			// wenn noch keine Instanz des Settings-Controller existiert -> dann eine neue
			// erzeugen und im Model speichern
			if (!(dataBean.getSettingsController() instanceof SettingsViewController)) {
				dataBean.setSettingsController(new SettingsViewController(dataBean));
				dataBean.getSettingsController().show();
			}
			// Falls schon eine Instanz besteht, wird nur durch die Methode show() die
			// SettingsView angezeigt,
			// damit die getätigten Änderungen vom User sichtbar bleiben
			else {
				dataBean.getSettingsController().show();
			}
		}
	}

}
