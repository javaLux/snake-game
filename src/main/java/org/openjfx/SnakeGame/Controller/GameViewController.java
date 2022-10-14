package org.openjfx.SnakeGame.Controller;

import org.openjfx.SnakeGame.View.GameView;
import org.openjfx.SnakeGame.businessLogic.ConfirmMsgBox;
import org.openjfx.SnakeGame.model.DataBean;
import org.openjfx.SnakeGame.model.DataBean.Direction;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * Klasse zum steuern und verwalten der User Interaktionen mit der Game View
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
public class GameViewController {
	// Instanzvariablen
	//
	// Daten (Model)
	private DataBean dataBean;
	// gameView (View die beim Spielen angezeigt werden soll)
	private GameView gameView;

	// Instanz Variable für den EventHandler des KeyFrame(Animation der Schlange)
	private EventHandler<ActionEvent> keyFrameHandler;
	
	// instance variable for the Eventhandler to reset size of
	// label "highscore value" after animation
	private EventHandler<ActionEvent> resetLabelHighScoreValueHandler;

	// Instanz Variable für den EventHandler des Back-Buttons
	private EventHandler<MouseEvent> backButtonHandler;

	// Instanz Variable für den EventHandler des TryAgain-Buttons
	private EventHandler<MouseEvent> tryAgainButtonHandler;

	// Instanz Variable für den InvalidationListener des Volume Sliders
	private InvalidationListener volumeSliderHandler;
	
	// Instanz Variable für den MouseExisting EventHandler des Volume Sliders
	private EventHandler<KeyEvent> ignoreKeyEventsOnSlider;

	// Instanz Variable für den EventHandler zum bewegen der Schlange
	private EventHandler<KeyEvent> moveSnakeHandler;

	// Instanz Variable für den EventHandler der das erneute
	// drücken der Leertaste während des Spiels steuert
	private EventHandler<KeyEvent> resumeGameHandler;

	// Instanz Variable für den EventHandler der das CloseEvent der GameStage
	// steuert
	private EventHandler<WindowEvent> gameStageCloseHandler;
	
	// Flag for the highscore was broken event at gameplay
	private boolean highscoreBrokenAtGamePlay;
	
	// Flag if highscore was first broken at game time
	// this flag is important for save new highscore when app quit
	private boolean firstBrokenHighscore;

	// Hilfs Variable für die Entscheidung ob das HighScore Value Label nach einem
	// Game Over sprich wenn auf den TryAgain Btn gedrückt wurde, mit dem erreichten Punkten
	// aktualisiert wird
	//private boolean isUpdateHighScoreValue;
	
	/**
	 * Konstruktor sichert das übergebene Model und initialisiert die View und die
	 * Instanzvariablen
	 * 
	 * @param dataBean
	 */
	public GameViewController(DataBean dataBean) {
		// Model sichern
		this.dataBean = dataBean;
		// Game View Instanz erstellen und GameView Content erzeugen
		this.gameView = new GameView();
		
		// at beginning of gameplay set flags for highscore broken event to false
		this.highscoreBrokenAtGamePlay = false;
		this.firstBrokenHighscore = false;

		// Focus vom Slider entfernen -> damit dieser beim Start nicht mit den
		// Pfeiltasten
		// gesteuert werden kann und somit die Steuerung der Schlange beeinträchtigt ->
		// diese wird ja
		// ebenfalls mit den Pfeiltasten gesteuert
		this.gameView.getVolumeSlider().setFocusTraversable(false);

		// aktuellen Start-score anzeigen = 0
		this.gameView.getLblCurrentScoreValue().setText(Integer.toString(this.dataBean.getCurrentScore()));
		//this.gameView.setLblCurrentScoreValue(this.dataBean.getCurrentScore());

		// den Slider für die Lautstärke auf den Wert (Model) aus den Settings setzen
		this.gameView.getVolumeSlider().setValue(this.dataBean.getValueOfVolume());
		// das Label für die Lautstärke auf den Wert (Model) aus den Settings setzen
		String volumeValuePercent = String.format(String.format("%.0f", this.dataBean.getValueOfVolume()));
		this.gameView.getLblVolumeValue().setText(volumeValuePercent + " %");

		// die ObservableList aus dem Model hält die Rechtecke für die Schlange
		// da aber eine ObservableList nicht im Pane sichtbar gemacht werden kann muss
		// das über die Group
		// geschehen -> hier speichern wir die Rechtecke aus der Group in der Liste
		// (Model)
		this.dataBean.setSnake(this.gameView.getSnakeGroup().getChildren());

		// dem Rectangle für das Essen ein Image geben -> definiert in CSS-File
		this.dataBean.getFood().getStyleClass().add("foodRectSnake");

		// falls user in den Settings die Musik ausgeschaltet hat -> Info anzeigen +
		// Slider auf 0 setzen +
		// Slider deaktivieren
		if (!(dataBean.isMusicPlaying())) {
			this.gameView.getVolumeSlider().setValue(this.dataBean.getValueOfVolume());
			// Label für die Lautstärke neben dem Slider anpassen
			String volumeValuePercentGame = String
					.format(String.format("%.0f", this.gameView.getVolumeSlider().getValue()));
			this.gameView.getLblVolumeValue().setText(volumeValuePercentGame + " %");
			this.gameView.getVolumeSlider().setDisable(true);
			this.gameView.getLblMusicIsOff().setVisible(true);
		}
		else {
			this.gameView.getVolumeSlider().setValue(this.dataBean.getValueOfVolume());
			// Label für die Lautstärke neben dem Slider anpassen
			String volumeValuePercentGame = String
					.format(String.format("%.0f", this.gameView.getVolumeSlider().getValue()));
			this.gameView.getLblVolumeValue().setText(volumeValuePercentGame + " %");
			this.gameView.getVolumeSlider().setDisable(false);
			this.gameView.getLblMusicIsOff().setVisible(false);
			// play game music
			this.dataBean.getMediaPlayerGameMusic().play();
		}

		// falls user mit Border spielen will -> diese um das Pane anzeigen
		if (dataBean.playWithBorder()) {
			this.gameView.getGamePane().getStyleClass().add("paneGameBorder");
		}

		// falls user einen grünen oder grauen Spielhintergrund gewählt hat
		if (dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_GREEN)) {
			this.gameView.getGamePane().getStyleClass().add("paneGameBackGrGreen");
		} else if (dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_GREY)) {
			this.gameView.getGamePane().getStyleClass().add("paneGameBackGrGrey");
		}
		
		// if highscore at beginning of game zero than set text to "Not available"
		// otherwise to highscore value
		if(this.dataBean.getHighScoreAtStart() > 0) {
			this.gameView.getLblHighScoreValue().setText(Integer.toString(this.dataBean.getHighScoreAtStart()));
		}
		else {
			this.gameView.getLblHighScoreValue().setText("unavailable");
		}

		// +++++ EventHandler initialisieren +++++
		//
		
		this.resetLabelHighScoreValueHandler = new ResetHighscoreValue();
		
		this.keyFrameHandler = new GameViewEventhandlerKeyFrame();
		this.backButtonHandler = new GameViewEventhandlerBtnBack();
		this.tryAgainButtonHandler = new GameViewEventhandlerBtnTryAgain();
		this.volumeSliderHandler = new GameViewEventhandlerVolumeSlider();
		this.ignoreKeyEventsOnSlider = new GameViewEventhandlerVolumeSliderIgnoreKeyEvents();
		this.moveSnakeHandler = new GameViewEventhandlerMoveSnake();
		this.resumeGameHandler = new GameViewEventhandlerResumeGame();
		this.gameStageCloseHandler = new GameViewGameStageCloseHandler();
		//
		// +++++ EventHandler fertig initialisiert +++++

		// prepare timeline and keyframes for highscore value animation -> with 10 seconds of duration
		this.prepareHighScoreValueAnimation(10);
		
		// prepare timeline for try again button animation
		this.prepareTryAgainButtonAnimation();
		
		// KeyFrame initialisieren -> für die Animation mit Spielgeschwindigkeit aus dem
		// Model
		this.dataBean.setKeyFrame(new KeyFrame(Duration.seconds(this.dataBean.getGameSpeed()), this.keyFrameHandler));

		// neue TimeLine für die Animation erzeugen und im Model speichern
		this.dataBean.setTimeLine(new Timeline());

		// Der Timeline für die Animation das Keyframe hinzufügen
		this.dataBean.getTimeLine().getKeyFrames().add(this.dataBean.getKeyFrame());
		// die Timeline soll ohne Zeitbeschränkung laufen -> bis diese durch die
		// Anwendung gestoppt wird
		this.dataBean.getTimeLine().setCycleCount(Timeline.INDEFINITE);

		// Eventhandler bzw. Listener zugehörigen Node registrieren
		//
		// somit überwacht / registriert der View Controller die Interaktionen des User
		// mit der View
		this.gameView.getBtnBack().setOnMouseClicked(this.backButtonHandler);
		this.gameView.getBtnTryAgain().setOnMouseClicked(this.tryAgainButtonHandler);
		this.gameView.getVolumeSlider().valueProperty().addListener(this.volumeSliderHandler);
		this.gameView.getVolumeSlider().addEventFilter(KeyEvent.KEY_PRESSED, this.ignoreKeyEventsOnSlider);
		/*
		 * damit die gedrückten Tasten auf der Tastatur fehlerfrei erkannt werden ->
		 * muss hier an dir Spielszene ein >Eventfilter< registriert werden ANSTATT
		 * eines Eventhandlers -> das hat mit den zwei gleichzeitig angezeigten Stage's
		 * zutun, wird nur eine Stage im Programm -> verwendet, kann eine Eventhandler
		 * benutzt werden!!!
		 */
		this.gameView.getScene().addEventFilter(KeyEvent.KEY_PRESSED, this.moveSnakeHandler);
		// Close Event der GameStage steuern
		this.dataBean.getGameStage().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this.gameStageCloseHandler);
		
	}

	/**
	 * Methode stellt die Verbindung zwischen PrimaryStage (Model) und der InputView
	 * her. So das die InputView in der PrimaryStage angezeigt werden kann
	 */
	public void show() {
		/*
		 * übergeben der primaryStage an die Game View -> Wichtig da es immer nur ein
		 * Hauptfenster innerhalb des Programms geben kann!!! Die primaryStage wird
		 * intern im Model gehalten als wichtigstes Objekt
		 */
		this.gameView.show(dataBean.getGameStage());
		
	}
	
	/**
	 * Method creates a black rectangle that builds the snake body.
	 * 
	 * @return	->	Rectangle 
	 */
	private Rectangle makeSnakeRectangle() {
		Rectangle snakeBody = new Rectangle(DataBean.BLOCK_SIZE, DataBean.BLOCK_SIZE, Color.BLACK);
		return snakeBody;
	}

	/**
	 * Method saves the current score as highscore in xml file
	 */
	private void saveHighscore() {
		// save only the highscore if the was broken once in game
		if(this.firstBrokenHighscore) {
			// update highscore value in model, for display the correct value
			// if user press in startView on "HighScore-Button"
			this.dataBean.setHighScoreAtStart(this.dataBean.getHighScoreAtGamePlay());
		}
	}
	
	/**
	 * Method to check if highscore was broken at game play.
	 * 
	 * @param points	->	current points
	 * @return			->	boolean
	 * 						
	 */
	private boolean highscoreBroken(int points) {
		
		if(points > dataBean.getHighScoreAtStart()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Method prepares the animation of lable "highscore value"
	 * if highscore was broken by player
	 * 
	 * @param seconds	->	Integer to definied how long to be run the animation
	 */
	private void prepareHighScoreValueAnimation(int seconds) {
		
		// initialized the timeline for highscore value animation
		this.dataBean.setTimeLineHighscoreValue(new Timeline());

		// add timeline new keyframe for animation of highscore value
		this.dataBean.getTimeLineHighscoreValue().getKeyFrames().addAll(
								
				// den TryAgain Btn von startzeit 0 sekunden mit normaler Größe anzeigen ->
				// scalePropertyX + Y
				new KeyFrame(Duration.ZERO, new KeyValue(this.gameView.getLblHighScoreValue().scaleXProperty(), 1),
						new KeyValue(this.gameView.getLblHighScoreValue().scaleYProperty(), 1)),
				// enlarge the label by 40% after 0.8 seconds
				new KeyFrame(Duration.seconds(0.8), new KeyValue(this.gameView.getLblHighScoreValue().scaleXProperty(), 1.6),
						new KeyValue(this.gameView.getLblHighScoreValue().scaleYProperty(), 1.6)),
				// important when timeline ends -> reset the label size after duration of 0.8 seconds 
				new KeyFrame(Duration.seconds(0.8), this.resetLabelHighScoreValueHandler));

		// timeline runs until seconds are over, than stops the timeline automatically
		this.dataBean.getTimeLineHighscoreValue().setCycleCount(seconds);
	}
	
	/**
	 * Method prepares the animation for the TryAgain Button after Game Over
	 * if highscore was broken by player
	 */
	private void prepareTryAgainButtonAnimation() {
		// TimeLine für die Animation des TryAgain-Buttons initialisieren
		this.dataBean.setTimeLineTryAgain(new Timeline());

		// Der Timeline für die Animation des TryAgain-Btn das Keyframe hinzufügen
		this.dataBean.getTimeLineTryAgain().getKeyFrames().addAll(
				// den TryAgain Btn von startzeit 0 sekunden mit normaler Größe anzeigen ->
				// scalePropertyX + Y
				new KeyFrame(Duration.ZERO, new KeyValue(this.gameView.getBtnTryAgain().scaleXProperty(), 1),
						new KeyValue(this.gameView.getBtnTryAgain().scaleYProperty(), 1)),
				// den TryAgain Btn nach 0.8 sekunden um 30% vergößeren
				new KeyFrame(Duration.seconds(0.8), new KeyValue(this.gameView.getBtnTryAgain().scaleXProperty(), 1.3),
						new KeyValue(this.gameView.getBtnTryAgain().scaleYProperty(), 1.3)));

		// die Timeline soll ohne Zeitbeschränkung laufen -> bis diese durch die
		// Anwendung gestoppt wird
		this.dataBean.getTimeLineTryAgain().setCycleCount(Timeline.INDEFINITE);
	}
	
	/**
	 * Methode erstellt an einer zufälligen Position im Spielfeld ein ein beliebiges
	 * Node in dieseer Anwendung wird es für das Rectangle für das Essen verwendet
	 * 
	 * @param food
	 */
	private void createRandomRect(Node node) {
		// Zufallszahl für die X-Pos erzeugen -> innerhalb des Spielfeldes
		node.setTranslateX((int) (Math.random() * (DataBean.GAME_WIDTH - DataBean.BLOCK_SIZE)) / DataBean.BLOCK_SIZE
				* DataBean.BLOCK_SIZE);

		// Zufallszahl für die Y-Pos erzeugen -> innerhalb des Spielfeldes
		node.setTranslateY((int) (Math.random() * (DataBean.GAME_HEIGHT - DataBean.BLOCK_SIZE)) / DataBean.BLOCK_SIZE
				* DataBean.BLOCK_SIZE);
	}

	/**
	 * Methode zum steuern des Spiels mit einem Rand um das Spielfeld
	 * 
	 * @param snakeHead
	 */
	private void gameHasBorder(Node snakeHead) {
		// Falls Schlange gegen den Rand des Spielfeldes bewegt wird
		if (snakeHead.getTranslateX() < 0 || snakeHead.getTranslateX() >= DataBean.GAME_WIDTH
				|| snakeHead.getTranslateY() < 0 || snakeHead.getTranslateY() >= DataBean.GAME_HEIGHT) {

			// GameOver -> Spielende
			this.gameOver();
		}
	}

	/**
	 * Methode zum steuern des Spiels OHNE einem Rand um das Spielfeld
	 * 
	 * @param snakeHead
	 */
	private void gameHasNoBorder(Node snakeHead) {
		// Falls Schlange sich über linken Rand bewegt -> Rechts wieder rauskommen
		if (snakeHead.getTranslateX() < 0) {
			snakeHead.setTranslateX(DataBean.GAME_WIDTH - DataBean.BLOCK_SIZE);
		}
		// Falls Schlange sich über rechten Rand bewegt -> Links wieder rauskommen
		if (snakeHead.getTranslateX() > DataBean.GAME_WIDTH) {
			snakeHead.setTranslateX(0);
		}
		// Falls Schlange sich über oberen Rand bewegt -> Unten wieder rauskommen
		if (snakeHead.getTranslateY() < 0) {
			snakeHead.setTranslateY(DataBean.GAME_HEIGHT - DataBean.BLOCK_SIZE);
		}
		// Falls Schlange sich über unteren Rand bewegt -> Oben wieder rauskommen
		if (snakeHead.getTranslateY() > DataBean.GAME_HEIGHT) {
			snakeHead.setTranslateY(0);
		}
	}
	
	/**
	 * Method plays the sound track if player broken the highscore
	 */
	private void playHighscoreSound() {
		// die highscore music musik immer abspielen, egal ob Spielmusik aktiviert war oder nicht!!!
		// IMPORTANT: den mediaplayer nach dem abspielen wieder auf Anfang setzen damit der Highscore Soundtrack
		// erneut von vorne abgespielt wird
		this.dataBean.getMediaPlayerHighscore().seek(this.dataBean.getMediaPlayerHighscore().getStartTime());
		this.dataBean.getMediaPlayerHighscore().setVolume(this.dataBean.getValueOfVolume() / 100);
		
		// run the media player in new java fx application thread, for better game play
		Platform.runLater( () -> {
			this.dataBean.getMediaPlayerHighscore().play();
		});
	}
	
	/**
	 * Method plays the sound track if game is over
	 */
	private void playGameOverSound() {
		// die Game over musik immer abspielen, egal ob Spielmusik aktiviert war oder nicht!!!
		// GameOver Soundtrack abspielen
		this.dataBean.getMediaPlayerGameOver().seek(this.dataBean.getMediaPlayerGameOver().getStartTime());
		this.dataBean.getMediaPlayerGameOver().setVolume(this.dataBean.getValueOfVolume() / 100);
		this.dataBean.getMediaPlayerGameOver().play();
	}

	/**
	 * Methode zum starten des Spiels bzw. der Animation
	 */
	public void FirstStartGameAnimation() {

		// Label für Musik Off -> sichtbar machen falls Musik in den Settings
		// ausgeschaltet wurde
		if (!dataBean.isMusicPlaying()) {
			this.gameView.getLblMusicIsOff().setVisible(true);
		}

		// Try-Again Btn deaktivieren und unsichtbar machen
		this.gameView.getBtnTryAgain().setDisable(true);
		this.gameView.getBtnTryAgain().setVisible(false);

		// check flag for showing infoBox at game start
		// if flag == false -> do not show infoBox and start game
		if(!dataBean.getFlagForInfoBoxShowing()) {
			// Start Laufrichtung der Schlange bestimmen -> Immer rechts von der Wand weg
			this.dataBean.setSnakeDirection(Direction.RECHTS);

			// das erste Rechteck für den Kopf der Schlange beim Start(beim ersten Programm
			// Start) in die Liste hinzufügen
			Rectangle snakeRect = this.makeSnakeRectangle();
			// snake rechteck der liste hinzufügen
			this.dataBean.getSnake().add(snakeRect);

			// Das Essen zufällig bei Start der Games platzieren
			this.createRandomRect(this.dataBean.getFood());

			// die Schlange startet(beim ersten Programm Start) in der Hälfte der Höhe des
			// Spielfeldes
			snakeRect.setTranslateY((DataBean.GAME_HEIGHT / 2) - DataBean.BLOCK_SIZE);
			
			// das Essen (Rectangle) der Spielszene hinzufügen
			this.gameView.getGamePane().getChildren().add(this.dataBean.getFood());

			// falls Essen noch (GameOver Methode) unsichtbar ist -> sichtbar machen
			if (!dataBean.getFood().isVisible()) {
				dataBean.getFood().setVisible(true);
			}

			// die Schlange der Spielszene beim Start hinzufügen
			this.gameView.getGamePane().getChildren().add(this.gameView.getSnakeGroup());

			// Timeline für die Animation starten
			this.dataBean.getTimeLine().play();
			// Spiel auf running setzen
			this.dataBean.setGameIsRunning(true);
			
			// leave this method
			return;
		}
		
		//
		// if flag for showing infoBox at game start == true
		// than showing infoBox
		Boolean resultMsgBox = ConfirmMsgBox.show(dataBean.getGameStage(), true, true ,"Snake - Game",
				"Use arrow keys to move snake:", "Are you ready to play?", new Image("/snake_img_dock.png"),
				new Image("/keyArrows.png"), this.dataBean);

		// hat der Spieler auf den YES-Btn der MessageBox geklickt wird Spiel gestartet
		if (resultMsgBox) {
			// Start Laufrichtung der Schlange bestimmen -> Immer rechts von der Wand weg
			this.dataBean.setSnakeDirection(Direction.RECHTS);

			// das erste Rechteck für den Kopf der Schlange beim Start(beim ersten Programm
			// Start) in die Liste hinzufügen
			Rectangle snakeRect = this.makeSnakeRectangle();
			// snake rechteck der liste hinzufügen
			this.dataBean.getSnake().add(snakeRect);

			// Das Essen zufällig bei Start der Games platzieren
			this.createRandomRect(this.dataBean.getFood());

			// die Schlange startet(beim ersten Programm Start) in der Hälfte der Höhe des
			// Spielfeldes
			snakeRect.setTranslateY((DataBean.GAME_HEIGHT / 2) - DataBean.BLOCK_SIZE);		

			// das Essen (Rectangle) der Spielszene hinzufügen
			this.gameView.getGamePane().getChildren().add(this.dataBean.getFood());

			// falls Essen noch (GameOver Methode) unsichtbar ist -> sichtbar machen
			if (!dataBean.getFood().isVisible()) {
				dataBean.getFood().setVisible(true);
			}

			// die Schlange der Spielszene beim Start hinzufügen
			this.gameView.getGamePane().getChildren().add(this.gameView.getSnakeGroup());

			// Timeline für die Animation starten
			this.dataBean.getTimeLine().play();
			// Spiel auf running setzen
			this.dataBean.setGameIsRunning(true);

		}
		// Falls Spieler auf den NO-Btn geklickt -> Spiel beenden
		else {
			// Falls StartView Fenster geschlossen wurde
			if (!(dataBean.getStartStage().isShowing())) {
				// Game Animation stoppen
				stopGameAnimation();
				// Spielszene beenden
				shutDownGame();
				// Startszene wieder anzeigen
				dataBean.getStartViewController().show();
			}
			// wenn die StartView noch angezeigt wird -> aber Minimiert ist dann nur
			// GameView schließen
			// und StartView wieder auf den Desktop holen
			else if ((dataBean.getStartStage().isShowing()) && (dataBean.getStartStage().isIconified())) {
				// Game Animation stoppen
				stopGameAnimation();
				// Spielszene beenden
				shutDownGame();
				// Startszene aus minimierten Zustand wieder sichtbar machen
				dataBean.getStartStage().setIconified(false);
			}
			// Falls primaryStage noch im Hintergrund angezeigt wird -> nur gameView
			// schließen
			else if ((dataBean.getStartStage().isShowing()) && !(dataBean.getStartStage().isIconified())) {
				// Game Animation stoppen
				stopGameAnimation();
				// Spielszene beenden
				shutDownGame();
				// Startszene aus minimierten Zustand wieder sichtbar machen
				dataBean.getStartStage().setIconified(false);
			}
		}
	}

	/**
	 * Methode startet erneut die Animation/das Spiel nach dem GameOver
	 */
	private void restartGameAnimation() {
		
		// falls highscore vor dem GameOver 0 war und der Spieler eine höhere
		// Punktzahl im Spiel bis zum GamOver erreicht hat, dann den erreichten Punktestand
		// als neuen Highscore anzeigen.
		if(this.firstBrokenHighscore) {
			this.gameView.getLblHighScoreValue().setText(Integer.toString(this.dataBean.getCurrentScore()));
		}
		else if(this.highscoreBrokenAtGamePlay) {
			this.gameView.getLblHighScoreValue().setText(Integer.toString(this.dataBean.getCurrentScore()));
		}
		
		// set flags always on default value after game over event 
		this.firstBrokenHighscore = false;
		this.highscoreBrokenAtGamePlay = false;
		
		// current score + Label auf null setzen
		this.dataBean.resetCurrentScore();
		this.gameView.getLblCurrentScoreValue().setText(Integer.toString(this.dataBean.getCurrentScore()));
		
		// GameOver Label unsichtbar machen
		this.gameView.getLblGameOver().setVisible(false);

		// Try-Again Btn deaktivieren und unsichtbar machen
		this.gameView.getBtnTryAgain().setDisable(true);
		this.gameView.getBtnTryAgain().setVisible(false);
		
		// beim Neustart des Spiels -> z.B. nach dem drücken auf den TryAgain-Btn den MediaPlayer
		// für die GameOver Musik auf stop() setzen, so wird dieser zurückgesetzt und beim
		// nächsten GameOver wird die GameOver Musik erneut von Anfang an abgespielt!!!
		if(dataBean.getMediaPlayerGameOver().getStatus() == MediaPlayer.Status.PLAYING) {
			dataBean.getMediaPlayerGameOver().stop();
		}
		
		// je nachdem ob Spielmusik eingeschaltet oder ausgeschaltet ist fortfahren
		if(this.dataBean.isMusicPlaying()) {
			this.dataBean.getMediaPlayerGameMusic().play();
		}

		// Start Laufrichtung der Schlange bestimmen -> Immer rechts von der Wand weg
		this.dataBean.setSnakeDirection(Direction.RECHTS);

		// das erste Rechteck für den Kopf der Schlange beim Start
		// in die Liste hinzufügen
		Rectangle snakeRect = this.makeSnakeRectangle();
		// snake rechteck der liste hinzufügen
		this.dataBean.getSnake().add(snakeRect);

		// Das Essen zufällig bei Start der Games platzieren
		this.createRandomRect(this.dataBean.getFood());

		// die Schlange startet wieder in der Hälfte der Höhe des
		// Spielfeldes
		snakeRect.setTranslateY((DataBean.GAME_HEIGHT / 2) - DataBean.BLOCK_SIZE);

		// falls Essen noch (GameOver Methode) unsichtbar ist -> sichtbar machen
		if (!dataBean.getFood().isVisible()) {
			dataBean.getFood().setVisible(true);
		}

		// Timeline für die Animation fortsetzen
		this.dataBean.getTimeLine().play();
		// Spiel auf running setzen
		this.dataBean.setGameIsRunning(true);
	}

	/**
	 * Methode stopt die Animation der Schlange und somit die Spielszene
	 */
	private void stopGameAnimation() {
		// Spiel auf gestoppt setzen
		this.dataBean.setGameIsRunning(false);
		// Timeline stoppen
		this.dataBean.getTimeLine().pause();
		// Liste für die Rechtecke der Schlange leeren
		this.dataBean.getSnake().clear();
	}

	/**
	 * Methode beendet die Spielszene, schließt das Fenster und derigistrtiert alle
	 * Eventhandler wie auch Listener
	 */
	private void shutDownGame() {
		
		// Spielanimation stoppen
		this.stopGameAnimation();
		
		// stop game music
		if(this.dataBean.isMusicPlaying()) {
			this.dataBean.getMediaPlayerGameMusic().stop();
		}
		
		// Eventhandler von Button Back entfernen
		this.gameView.getBtnBack().removeEventHandler(MouseEvent.MOUSE_CLICKED, this.backButtonHandler);
		// Eventhandler von Button Back entfernen
		this.gameView.getBtnTryAgain().removeEventHandler(MouseEvent.MOUSE_CLICKED, this.tryAgainButtonHandler);
		// Listener + Eventfilter vom VoluemSlider entfernen
		this.gameView.getVolumeSlider().valueProperty().removeListener(this.volumeSliderHandler);
		// EventFilter von der Spielszene entfernen -> Steuern der Schlange
		this.gameView.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, this.moveSnakeHandler);
		this.gameView.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, this.ignoreKeyEventsOnSlider);
		
		// EventHandler für das pausieren entfernen
		this.gameView.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, this.resumeGameHandler);
		// EventHandler für das CloseEvent der GameView entfernen
		this.dataBean.getGameStage().removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this.gameStageCloseHandler);
		// Eventhandler für Animation -> KeyFrame zurücksetzen
		this.dataBean.setKeyFrame(null);
		
		// set all timelines to null
		this.dataBean.setTimeLine(null);
		this.dataBean.setTimeLineTryAgain(null);
		this.dataBean.setTimeLineHighscoreValue(null);
		
		// Spiel Fenster schließen
		this.dataBean.getGameStage().close();
		// aktuelle Instanz des GameViewControllers löschen -> null
		this.dataBean.setGameController(null);
		// Score im Model zurücksetzen
		this.dataBean.resetCurrentScore();
		// Zähler für die Leertaste auf 0 setzen
		this.dataBean.resetCounterKeySpace();
		// StartView - Buttons wieder aktivieren
		this.dataBean.getStartViewController().getPlayButtonFromStartView().setDisable(false);
		this.dataBean.getStartViewController().getHighscoreButtonFromStartView().setDisable(false);
		this.dataBean.getStartViewController().getSettingsButtonFromStartView().setDisable(false);
	}

	/**
	 * Methode kehrt nach dem die Einstellungen während des Spiels geändert wurden
	 * und auf den Apply Btn gedrückt wurde, zur Spielszene zurück
	 */
	public void resumeGame() {
		
		// falls user in den Settings die Musik ausgeschaltet hat -> Info anzeigen +
		// Slider deaktivieren
		if (!(this.dataBean.isMusicPlaying())) {
			this.dataBean.getMediaPlayerGameMusic().pause();
			this.gameView.getVolumeSlider().setValue(this.dataBean.getValueOfVolume());
			// Label für die Lautstärke neben dem Slider anpassen
			String volumeValuePercentGame = String
					.format(String.format("%.0f", this.gameView.getVolumeSlider().getValue()));
			this.gameView.getLblVolumeValue().setText(volumeValuePercentGame + " %");
			this.gameView.getVolumeSlider().setDisable(true);
			this.gameView.getLblMusicIsOff().setVisible(true);
		}
		// Falls Musik eingeschaltet ist
		else {
			this.dataBean.getMediaPlayerGameMusic().play();
			// den Slider für die Lautstärke auf den Wert (Model) aus den Settings setzen
			this.gameView.getVolumeSlider().setValue(this.dataBean.getValueOfVolume());
			// das Label für die Lautstärke auf den Wert (Model) aus den Settings setzen
			String volumeValuePercent = String.format(String.format("%.0f", this.dataBean.getValueOfVolume()));
			this.gameView.getLblVolumeValue().setText(volumeValuePercent + " %");
			this.gameView.getVolumeSlider().setDisable(false);
			this.gameView.getLblMusicIsOff().setVisible(false);
		}

		// hier müssen direkt die CSS Befehle angewendet werden -> da hier verschiedene
		// CSS-Befehle
		// miteinander kombiniert werden müssen
		//
		// Grüner Hintergrund mit Rand
		if ((this.dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_GREEN)) && (this.dataBean.playWithBorder())) {
			this.gameView.getGamePane()
					.setStyle("-fx-background-image: url(/gameBackgroundGreen.png);" + "-fx-background-repeat: repeat;"
							+ "-fx-background-size: 20 20;" + "-fx-border-color: #e90003;" + "-fx-border-width: 3px;"
							+ "-fx-border-style: dashed;");
		}
		// Grüner Hintergrund ohne Rand
		if ((this.dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_GREEN)) && ! (this.dataBean.playWithBorder())) {
			this.gameView.getGamePane()
					.setStyle("-fx-background-image: url(/gameBackgroundGreen.png);" + "-fx-background-repeat: repeat;"
							+ "-fx-background-size: 20 20;" + "-fx-border-color: transparent;");
		}
		// Grauer Hintergrund mit Rand
		if ((this.dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_GREY)) && (this.dataBean.playWithBorder())) {
			this.gameView.getGamePane()
					.setStyle("-fx-background-image: url(/gameBackgroundGrey.png);" + "-fx-background-repeat: repeat;"
							+ "-fx-background-size: 20 20;" + "-fx-border-color: #e90003;" + "-fx-border-width: 3px;"
							+ "-fx-border-style: dashed;");
		}
		// Grauer Hintergrund ohne Rand
		if ((this.dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_GREY)) && ! (this.dataBean.playWithBorder())) {
			this.gameView.getGamePane()
					.setStyle("-fx-background-image: url(/gameBackgroundGrey.png);" + "-fx-background-repeat: repeat;"
							+ "-fx-background-size: 20 20;" + "-fx-border-color: transparent;");
		}
		// Ohne Hintergrund mit Rand
		if ((this.dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_NONE)) && (this.dataBean.playWithBorder())) {
			this.gameView.getGamePane().setStyle("-fx-background-image: null;"
					+ "-fx-background-color: transparent;" + "-fx-border-color: #e90003;"
					+ "-fx-border-width: 3px;" + "-fx-border-style: dashed;");
		}
		// Ohne Hintergrund ohne Rand
		if ((this.dataBean.getGameBackground().equals(DataBean.GAME_BACKGROUND_NONE)) && ! (this.dataBean.playWithBorder())) {
			this.gameView.getGamePane().setStyle("-fx-background-image: null;"
					+ "-fx-background-color: transparent;" + "-fx-border-color: transparent;");
		}

		// Damit eventuell neu gewählte Spielgeschwindigkeit auch angewendet wird,
		// muss eine neuen KeyFrame + TimeLine Instanz für die Spiel Animation erzeugt werden
		
		// alte KeyFrame Instanz löschen
		this.dataBean.setKeyFrame(null);
		// Neue KeyFrame Instanz initialisieren -> für die Animation mit
		// Spielgeschwindigkeit aus dem Model
		this.dataBean.setKeyFrame(new KeyFrame(Duration.seconds(this.dataBean.getGameSpeed()), this.keyFrameHandler));
		this.dataBean.setTimeLine(null);
		// neue TimeLine für die Animation erzeugen und im Model speichern
		this.dataBean.setTimeLine(new Timeline());
		// Der Timeline für die Animation das Keyframe hinzufügen
		this.dataBean.getTimeLine().getKeyFrames().add(this.dataBean.getKeyFrame());
		// die Timeline soll ohne Zeitbeschränkung laufen -> bis diese durch die
		// Anwendung gestoppt wird
		this.dataBean.getTimeLine().setCycleCount(Timeline.INDEFINITE);
		
		// Spielanimation wieder starten aber nur wenn Spiel NICHT vom Spieler pausiert wurde!!!
		if(! this.dataBean.getGameIsPaused()) {
			this.dataBean.getTimeLine().play();
		}
	}

	/**
	 * 
	 */
	private void gameOver() {
		// set flag for highscore was broken to false
		// important for update after gameover only the current score label
		// and not the highscore label
		this.highscoreBrokenAtGamePlay = false;
		
		// Spielanimation stoppen
		this.stopGameAnimation();

		// Essen für die Schlange ausblenden
		this.dataBean.getFood().setVisible(false);
		
		// save highscore if there was broken at gameplay time
		this.saveHighscore();

		// GameOver Label sichtbar machen
		this.gameView.getLblGameOver().setVisible(true);

		// Falls Spielmusik lief -> media player pausieren
		if (dataBean.isMusicPlaying()) {
			// Game Musik pausieren
			this.dataBean.getMediaPlayerGameMusic().pause();
		}
		
		// play game over soundtrack
		this.playGameOverSound();

		// Try-Again Btn aktivieren und sichtbar machen
		this.gameView.getBtnTryAgain().setDisable(false);
		this.gameView.getBtnTryAgain().setVisible(true);

		// Animation des TryAgain-Buttons starten
		this.dataBean.getTimeLineTryAgain().play();
	}

	/**
	 * Methode handelt das Vorgehen wenn der Spieler das Spiel beendet.
	 * 
	 * @return true wenn spieler über msgBox yes geklickt hat um Spiel zu beenden
	 */
	private Boolean userQuitEvent() {

		// prüfen ob TimeLine für TryAgain-Button läuft -> wenn ja diese anhalten
		if (dataBean.getTimeLineTryAgain() != null && dataBean.getTimeLineTryAgain().getStatus() == Status.RUNNING) {
			dataBean.getTimeLineTryAgain().pause();
		}

		// prüfen ob TimeLine für das GamePlay läuft -> wenn ja diese anhalten
		if (dataBean.getTimeLine() != null && dataBean.getTimeLine().getStatus() == Status.RUNNING) {
			dataBean.getTimeLine().pause();
		}

		// Spieler fragen ob wirklich beendet werden soll?
		Boolean quitGame = ConfirmMsgBox.show(dataBean.getGameStage(), true, false, "Snake - Game", "Really wanna quit?",
				"If you have broken the highscore or set a new highscore, it will saved automatically.",
				new Image("/snake_img_dock.png"), new Image("/emoji_unhappy.png"), null);
		
		// Falls Yes-Btn gedrückt wurde -> Spiel beenden
		if (quitGame) {
			// if player set a new highscore, than save this highscore in model
			this.saveHighscore();
			
			// Falls StartView Fenster geschlossen wurde
			if (!(dataBean.getStartStage().isShowing())) {
				// Game Animation stoppen
				stopGameAnimation();
				// Spielszene beenden
				shutDownGame();
				// Startszene wieder anzeigen
				dataBean.getStartViewController().show();

			}
			// wenn die StartView noch angezeigt wird -> aber Minimiert ist dann nur
			// GameView schließen
			// und StartView wieder auf den Desktop holen
			else if ((dataBean.getStartStage().isShowing()) && (dataBean.getStartStage().isIconified())) {
				// Game Animation stoppen
				stopGameAnimation();
				// Spielszene beenden
				shutDownGame();
				// Startszene aus minimierten Zustand wieder sichtbar machen
				dataBean.getStartStage().setIconified(false);
			}
			// Falls primaryStage noch im Hintergrund angezeigt wird -> nur gameView
			// schließen
			else if ((dataBean.getStartStage().isShowing()) && !(dataBean.getStartStage().isIconified())) {
				// Game Animation stoppen
				stopGameAnimation();
				// Spielszene beenden
				shutDownGame();
				// Startszene aus minimierten Zustand wieder sichtbar machen
				dataBean.getStartStage().setIconified(false);
			}
			return true;
		} else {
			// Falls NO-Btn gedrückt -> TimeLines wieder starten
			if (dataBean.getTimeLineTryAgain() != null && dataBean.getTimeLineTryAgain().getStatus() == Status.PAUSED) {
				dataBean.getTimeLineTryAgain().play();
			}

			if (dataBean.getTimeLine() != null && dataBean.getTimeLine().getStatus() == Status.PAUSED) {
				dataBean.getTimeLine().play();
			}
		}

		return false;
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Eventhandling
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * EventHandler -> für den TryAgain Button in der GameView -> dieser wird per
	 * Maus Klick ausgelöst
	 * 
	 * @author Admin
	 *
	 */
	class GameViewEventhandlerBtnTryAgain implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// Animation des TryAgain Buttons anhalten
			dataBean.getTimeLineTryAgain().pause();

			// Spiel neu starten
			restartGameAnimation();
		}

	}

	/**
	 * Eventhandler -> für den Volume Slider in der GameView -> dieser reagiert
	 * sobald mit der Maus der Slider bewegt wird. Es wäre auch möglich gewesen
	 * einen ChangeListener zu verwenden, der InvalidationListener ist aber von der
	 * Performance besser da er nicht wie ein changeListener die ganze laufzeit über
	 * den Slider abhört sondern eben nur wenn dieser mit der Maus bewegt wird!!!
	 * 
	 * @author Christian
	 */
	class GameViewEventhandlerVolumeSlider implements InvalidationListener {

		@Override
		public void invalidated(Observable observable) {
			// Mediaplayer volume mit dem slider anpassen
			dataBean.getMediaPlayerGameMusic().setVolume(gameView.getVolumeSlider().getValue() / 100);
			// Label für die Lautstärke neben dem Slider anpassen
			String volumeValuePercentGame = String.format(String.format("%.0f", gameView.getVolumeSlider().getValue()));
			gameView.getLblVolumeValue().setText(volumeValuePercentGame + " %");

			// denn Wert der Lautstärke im Model anpassen
			dataBean.setValueOfVolume(gameView.getVolumeSlider().getValue());

			// den Slider + Label für die Lautstärke in den Settings synchronisieren
			dataBean.getSettingsController().getVolumeSliderFromSettingsView().setValue(dataBean.getValueOfVolume());
			String volumeValuePercentSettings = String.format(String.format("%.0f", dataBean.getValueOfVolume()));
			dataBean.getSettingsController().getVolumeValueLabelFromSettingsView()
					.setText(volumeValuePercentSettings + " %");
		}

	}
	
	/**
	 * Eventhandler -> für den Volume Slider in der GameView
	 * Dieser Eventfilter reagiert auf KeyEvents auf der Tastatur.
	 * Es werden alle KeyEvents mit den Pfeiltasten herausgefiltert und ignoriert,
	 * so das beim weiterspielen nicht mit den Pfeiltasten der Slider und nicht die
	 * Schlange bewegt wird.
	 * 
	 * @author Christian
	 */
	class GameViewEventhandlerVolumeSliderIgnoreKeyEvents implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
				event.consume();
			}
			
		}
		
	}

	/**
	 * Eventhandler -> für den Button-Back(Open Settings) -> dieser reagiert sobald mit der Maus
	 * auf diesen geklickt wird
	 * 
	 * @author Christian
	 */
	class GameViewEventhandlerBtnBack implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			
			// falls game musik eingeschaltet ist, diese anhalten
			if(dataBean.isMusicPlaying()) {
				dataBean.getMediaPlayerGameMusic().pause();
			}
			
			// Spiel Animation pausieren
			dataBean.getTimeLine().pause();

			// wenn noch keine Instanz des Settings-Controller existiert -> dann eine neue
			// erzeugen und im Model speichern
			if (!(dataBean.getSettingsController() instanceof SettingsViewController)) {
				// GameView(Stage) ausblenden
				dataBean.getGameStage().hide();
				// neue Instanz des SettingsViewControllers erzeugen
				dataBean.setSettingsController(new SettingsViewController(dataBean));
				dataBean.getSettingsController().show();
			}
			// Falls schon eine Instanz besteht, wird nur durch die Methode show() die
			// SettingsView angezeigt,
			// damit die getätigten Änderungen vom User sichtbar bleiben
			else {
				// GameView(Stage) ausblenden
				dataBean.getGameStage().hide();
				// SettingsView anzeigen
				dataBean.getSettingsController().show();
			}
		}
	}
	
	/**
	 * Eventhandler -> für das zurücksetzen der Größe des Labels "highscore value" nach der
	 * Animation. Die Animation des labels wird durch die Methode "startHighscoreValueAnimation()"
	 * nach dem brechen des aktuellen highscores gestartet.
	 * 
	 * Wird das Label nicht auf seine normale Größe durch diesen Eventhandler zurückgesetzt,
	 * kann es sein das dass Label nach Ende der Animation vergrößert stehen bleibt!!!
	 * 
	 * @author Christian
	 *
	 */
	class ResetHighscoreValue implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// reset size of label
			gameView.getLblHighScoreValue().setScaleX(1.0);
			gameView.getLblHighScoreValue().setScaleY(1.0);
		}
	}

	/**
	 * Eventhandler -> für das KeyFrame(Game Animation) -> alle Aktionen innerhalb der
	 * Handle-Methode werden nach der jeweiligen Zeitspanne (gameSpeed) ausgeführt
	 * Die Zeitspanne (DURATION) bestimmt die Häufigkeit der Wiederholungen der
	 * Animation Siehe KeyFrame Initialisierung im Konstruktor der Klasse
	 * GameViewController
	 * 
	 * @author Christian
	 */
	class GameViewEventhandlerKeyFrame implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {			
			
			// prüfen ob Spiel läuft -> wenn nicht dann aus der Handle Methode returnen
			if (!dataBean.getGameRunningState()) {
				return;
			}

			// Falls Spiel läuft -> prüfen ob es in der Liste mit den Rechtecken für die
			// Schlange mindestens 1 oder mehr Elemente vorhanden sind
			boolean toRemove = dataBean.getSnake().size() > 1;

			// Schlangen Kopf aus Liste holen -> dieser wird bewegt
			Node snakeHead;

			// prüfen ob es mehr als eine Rechteck für die Schlange gibt
			if (toRemove) {
				// falls mehr als 1 Rechteck existiert, das letzte aus der Liste entfernen
				snakeHead = dataBean.getSnake().remove(dataBean.getSnake().size() - 1);
			}
			// wenn nicht dann erstes Rechteck aus der Liste holen + speichern
			else {
				snakeHead = dataBean.getSnake().get(0);
			}

			// Position der Schlange speichern
			double snakPosX = snakeHead.getTranslateX();
			double snakPosY = snakeHead.getTranslateY();

			// je nachdem welche Pfeiltaste gedrückt wurde -> Schlange danach bewegen
			switch (dataBean.getSnakeDirection()) {
			case OBEN:
				snakeHead.setTranslateX(dataBean.getSnake().get(0).getTranslateX());
				snakeHead.setTranslateY(dataBean.getSnake().get(0).getTranslateY() - DataBean.BLOCK_SIZE);
				break;
			case UNTEN:
				snakeHead.setTranslateX(dataBean.getSnake().get(0).getTranslateX());
				snakeHead.setTranslateY(dataBean.getSnake().get(0).getTranslateY() + DataBean.BLOCK_SIZE);
				break;
			case LINKS:
				snakeHead.setTranslateX(dataBean.getSnake().get(0).getTranslateX() - DataBean.BLOCK_SIZE);
				snakeHead.setTranslateY(dataBean.getSnake().get(0).getTranslateY());
				break;
			case RECHTS:
				snakeHead.setTranslateX(dataBean.getSnake().get(0).getTranslateX() + DataBean.BLOCK_SIZE);
				snakeHead.setTranslateY(dataBean.getSnake().get(0).getTranslateY());
				break;
			default:
				break;
			}

			// Schlange bewegt sich -> Variable in Model anpassen
			dataBean.setSnakeMoved(true);

			// das Rechteck für den Schlangenkörper der Liste hinzufügen
			if (toRemove) {
				dataBean.getSnake().add(0, snakeHead);
			}

			// Kollision prüfen
			// Schleife durchläuft alle Rechtecke des Schlangen Körpers in der Liste
			for (Node rectSnake : dataBean.getSnake()) {

				// prüfen ob schlange mit sich selbst kollidiert ist
				if (rectSnake != snakeHead && snakeHead.getTranslateX() == rectSnake.getTranslateX()
						&& snakeHead.getTranslateY() == rectSnake.getTranslateY()) {

					// Game Over -> Spielende
					gameOver();
					break;
				}
			}

			// prüfen ob ein Rand um das Spielfeld gewählt wurde
			if (dataBean.playWithBorder()) {
				gameHasBorder(snakeHead);
			} else {
				gameHasNoBorder(snakeHead);
			}

			// Essen einsammlen -> wenn Schlangenkopf gleich der Pos des essens ist
			if (snakeHead.getTranslateX() == dataBean.getFood().getTranslateX()
					&& snakeHead.getTranslateY() == dataBean.getFood().getTranslateY()) {
				// neues essen erstellen
				createRandomRect(dataBean.getFood());
				
				// Punkte (Score) erhöhen
				dataBean.addPointsToCurrentScore(DataBean.POINTS);
				
				// was highscore at beginning of gameplay zero than
				// update ONLY the current score value and save current score as
				// new "highscoreAtGamePlay" value in model
				if(dataBean.getHighScoreAtStart() == 0) {
					// save once new highscore in model
					dataBean.setHighScoreAtGamePlay(dataBean.getCurrentScore());
					/*
					 *  this flag specify that highscore was broken once at game time
					 *  important to say the highscore must save at quit event 
					 */
					firstBrokenHighscore = true;
					// update label current score
					gameView.getLblCurrentScoreValue().setText(Integer.toString(dataBean.getCurrentScore()));
					
				}
				// was game started with a highscore higher than zero
				else if (dataBean.getHighScoreAtStart() > 0) {
					// save if highscore was broken once at game time and highscore at the beginning of gameplay
					// greater than 0
					if(!highscoreBrokenAtGamePlay && highscoreBroken(dataBean.getCurrentScore())) {
						
						// save once new highscore in model
						dataBean.setHighScoreAtGamePlay(dataBean.getCurrentScore());
						
						/*
						 *  this flag specify that highscore was broken once at game time
						 *  IMPORTANT to say the highscore must save at quit event 
						 */
						firstBrokenHighscore = true;
						
						/*
						 *  set flag to true to specify highscore was broken
						 *  IMPORTANT for update label in gameView and save values in model
						 */ 
						highscoreBrokenAtGamePlay = true;
						
						// start animation of label highscore value -> with 10 seconds of duration
						playHighscoreSound();
						dataBean.getTimeLineHighscoreValue().play();
						
					}						
				}
				
				// if highscore was broken at gameplay time -> set
				// highscore value to the same value as current score
				if(highscoreBrokenAtGamePlay) {
					
					// save changes in model and update gameView label
					dataBean.setHighScoreAtGamePlay(dataBean.getCurrentScore());
					
					// current score Value Label and highscore label updated
					gameView.getLblHighScoreValue().setText(Integer.toString(dataBean.getCurrentScore()));
					gameView.getLblCurrentScoreValue().setText(Integer.toString(dataBean.getCurrentScore()));
				}
				else {
					// only update current score Value label
					gameView.getLblCurrentScoreValue().setText(Integer.toString(dataBean.getCurrentScore()));
				}

				// make snake bigger for each collected feed
				Rectangle snakeRect = makeSnakeRectangle();
				snakeRect.setTranslateX(snakPosX);
				snakeRect.setTranslateY(snakPosY);
				// das neue Rect nach dem Essen aufsammeln -> dieses dem Schlangen Körper der
				// Liste
				// hinzufügen
				dataBean.getSnake().add(snakeRect);				
			}
		}
	}

	/**
	 * EventHandler zum steuern des KeyPressed Events der Leertaste.
	 * 
	 * Kommt zum Einsatz innerhalb des EventHandlers zum steuern der Schlange. Wird
	 * beim erneuten drücken der Leertaste angesteuert -> sprich wenn Spiel aus dem
	 * Pause Modus geholt wird.
	 * 
	 * @author Christian
	 *
	 */
	class GameViewEventhandlerResumeGame implements EventHandler<KeyEvent> {
		
		@Override
		public void handle(KeyEvent event) {
			
			if (event.getCode() == KeyCode.SPACE) {
				// increment counter key space
				dataBean.IncrementCounterKeySpace();

				// Falls Spiel pausiert wurde UND das zweite mal auf die Leertaste
				// gedrückt wurde -> dann Spiel fortsetzen
				if (dataBean.getGameIsPaused() && dataBean.getCounterKeySpace() == 2) {
					dataBean.getTimeLine().play();
					dataBean.setGameIsPaused(false);
					// reset counter key space!!!!
					dataBean.resetCounterKeySpace();
				}
			}
			
			// Falls während des pausierten Spiels die ESC Taste gedrückt wird
			// -> soll Spiel ebenfalls beendet werden		
			if (event.getCode() == KeyCode.ESCAPE) {
				// Spieler via MsgBox fragen ob Spiel wirklich beendet werden soll?
				userQuitEvent();
			}		
		}		
	}

		/**
		 * Eventhandler für das steuern der Schlange mit der Tastatur bzw. das
		 * beenden/pausieren des Spiels
		 * 
		 * @author Christian
		 *
		 */
		class GameViewEventhandlerMoveSnake implements EventHandler<KeyEvent> {

			@Override
			public void handle(KeyEvent event) {
				// Fokus vom VolumeSlider entfernen
				gameView.getVolumeSlider().setFocusTraversable(false);
				
				// wenn Schlange nicht in Bewegung ist -> z.B Spiel ist pausiert
				// dann aus der Handle Methode returnen
				if (!dataBean.getSnakeMoved()) {
					return;
				}
				
				// Eventfilter -> zum Spiel pausieren, von der GameScene deregistrieren um ein
				// überschneiden mit diesem Eventhandler zu vermeiden. Das Deregistrieren wird immer dann
				// ausgeführt wenn eine Taste auf der Tastatur gerdückt wird. Geschieht dies nicht kommen sich
				// die beiden EventHandler die für die KeyEvents zuständig sind in die Quere und beim
				// beenden des Spiels via ESC-Taste wird die Quit-MsgBox zweimal aufgerufen!!!
				if(resumeGameHandler != null) {
					gameView.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, resumeGameHandler);
				}
				
				// je nach gedrückter Taste die Bewegungsrichtung der Schlange verändern
				switch (event.getCode()) {

				// mit Taste UP oder W Schlange nach oben bewegen
				case W:
				case UP:
					// es kann nur nach oben gesteuert werden wenn die Bewegungsrichtung der
					// Schlange
					// NICHT nach unten ist -> sondern Links oder Rechts
					if (dataBean.getSnakeDirection() != Direction.UNTEN) {
						dataBean.setSnakeDirection(Direction.OBEN);
					}
					break;

				// mit Taste DOWN oder S Schlange nach unten bewegen
				case S:
				case DOWN:
					// es kann nur nach unten gesteuert werden wenn die Bewegungsrichtung der
					// Schlange
					// NICHT nach oben ist -> sondern Links oder Rechts
					if (dataBean.getSnakeDirection() != Direction.OBEN) {
						dataBean.setSnakeDirection(Direction.UNTEN);
					}
					break;

				// mit Taste LEFT oder A Schlange nach links bewegen
				case A:
				case LEFT:
					// es kann nur nach links gesteuert werden wenn die Bewegungsrichtung der
					// Schlange
					// NICHT nach rechts ist -> sondern Unten oder Oben
					if (dataBean.getSnakeDirection() != Direction.RECHTS) {
						dataBean.setSnakeDirection(Direction.LINKS);
					}
					break;

				// mit Taste RIGHT oder D Schlange nach rechts bewegen
				case D:
				case RIGHT:
					// es kann nur nach rechts gesteuert werden wenn die Bewegungsrichtung der
					// Schlange
					// NICHT nach links ist -> sondern Unten oder Oben
					if (dataBean.getSnakeDirection() != Direction.LINKS) {
						dataBean.setSnakeDirection(Direction.RECHTS);
					}
					break;

				// mit Leertaste Spiel pausieren
				case SPACE:

					// Spiel Animation pausieren
					dataBean.getTimeLine().pause();
					// Spiel wurde pausiert -> Wert im Model ändern
					dataBean.setGameIsPaused(true);

					// wird erneut auf SPACE gedrückt und Spiel ist pausiert -> Spielanimation
					// fortsetzen -> Logik im EventHandler ResumeGameHandler
					gameView.getScene().addEventFilter(KeyEvent.KEY_PRESSED, resumeGameHandler);
					break;

				// mit Taste ESC Spiel beenden, äquivalent zu Close-Event
				case ESCAPE:
					// Spieler fragen ob Spiel wirklich beendet werden soll?
					if (userQuitEvent()) {
						break;
					}
					break;

				default:
					break;
				}

				// Bewegung der Schlange auf False setzen -> da ja solange auf der Tastatur
				// gerdrückt wird
				// keine Bewegung stattfinden soll
				dataBean.setSnakeMoved(false);
			}

		}

		/**
		 * Eventhandler für das steuern des Close Events der GameView (wenn z.B. auf das
		 * X oben rechts bzw. bei Linux links geklickt wird)
		 * 
		 * @author Christian
		 *
		 */
		class GameViewGameStageCloseHandler implements EventHandler<WindowEvent> {

			@Override
			public void handle(WindowEvent event) {

				// Spieler fragen ob Spiel wirklich beendet werden soll?
				if (!userQuitEvent()) {
					// Falls NO-Btn gedrückt dann Close-Event ignorieren!!!
					event.consume();
				}
			}
		}
}
