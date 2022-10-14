package org.openjfx.SnakeGame.model;

import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import org.openjfx.SnakeGame.Controller.GameViewController;
import org.openjfx.SnakeGame.Controller.HighScoreViewController;
import org.openjfx.SnakeGame.Controller.SettingsViewController;
import org.openjfx.SnakeGame.Controller.StartViewController;
import org.openjfx.SnakeGame.businessLogic.GameSettings;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Model Klasse -> hält intern die notwendigen Daten wie die Anwendung wie z.B.
 * die primaryStage(Hauptfenster) und die Liste der kopierten Dateien
 * 
 * Das Model darf keine Referenz auf eine View oder einen Controller haben!
 * 
 * Wichtigste Aufgaben des Model: -> speichert alle notwendigen Daten für die
 * Anwendung -> besitzt nur Getter- und Setter-Methoden für den Zugriff auf die
 * Daten
 * 
 * @author Christian
 *
 */

public class DataBean extends Stage {
	
	// Enum welches die Zustände der Tasten beschreibt
	public enum Direction {
		OBEN, UNTEN, LINKS, RECHTS;
	}

	// privaten Instanzvariablen zum speichern der Daten
	//
	// Hauptfenster
	private Stage startStage = null;
	// Fenster für die Einstellungen
	private Stage settingsStage = null;
	// Fenster für die Spielszene
	private Stage gameStage = null;
	// View for showing highscore
	private Stage highscoreStage = null;

	// Path object zur XML-Datei
	private Path pathToXML = null;
	// Boolean der festlegt ob es einen Rand im Spiel gibt
	private boolean hasBorder;
	// Boolean der festlegt ob music abgespeilt wird oder nicht
	private boolean musicPlay;
	// Laufrichtung der Schlange
	private Direction directionOfSnake;
	// Boolean ob Schlange in Bewegung ist
	private boolean snakeMoved;
	// Boolean für die Game Loop
	private boolean gameRunning;
	// Boolean ob Spiel pausiert wurde
	private boolean gamePause;
	// Boolean flag if user wish to see infoBox at game start
	private boolean showInfoBoxAtGameStart;
	// Zeitlinie für die Animation
	private Timeline timeLine;
	// TimeLine für den Try-Again Button mit null initialisieren
	private Timeline timeLineAfterGameOver;
	// TimeLine for the highscore value animation
	private Timeline timeLineHighscoreValue;
	
	// Die Schlange - Liste mit den Rechtecken
	private ObservableList<Node> snake;
	// MediaPlayer für die Spielmusik
	private MediaPlayer mediaPlayerGameMusic;
	// MediaPlayer für den GameOver Soundtrack
	private MediaPlayer mediaPlayerGameOver;
	// MediaPlayer für den Highscore
	private MediaPlayer mediaPlayerHighscore;
	// Media Object (File) -> GameMusik
	private Media mediaFileGameMusic;
	// Media Object (File) -> GameOver
	private Media mediaFileGameOver;
	// Media Object (File) -> highscore
	private Media mediaFileHighscore;
	// Wert des Score
	private int currentScore;
	// value of highscore at gameplay time
	private int highscoreAtGamePlay;
	// value of highscore at start time of gameplay
	private int highscoreAtStart;
	// Map for the game settings
	private Map<String, String> specifiedGameSettings;

	// Instanz des StartViewControllers
	private StartViewController startViewController;

	// Instanz des SettingsView Controller speichern
	private SettingsViewController settingsController;

	// Instanz des GameView Controller speichern
	private GameViewController gameController;
	
	// private member of HighScoreViewController
	private HighScoreViewController highscoreController;

	// Animation KeyFrame
	private KeyFrame keyFrame;

	// Werte für Settings -> Werte können vom Spieler verändert werden
	//
	// Lautstärke
	private double valueOfVolume;
	// Spielgeschwindigkeit
	private double gameSpeed;
	// String für den gewählten Game Background -> wird in den Settings über die
	// RadioBtn zugewiesen
	private String gameBackground;
	
	// Zähler wie oft die Leertaste im Spiel gedrückt wurde -> zum korrekten
	// pausieren des Spiels notwendig
	private int counterKeySpace;
	
	// X and Y position from startView -> this values used for writing
	// last x and y position in xml
	public static double xPosStartView = 0.0;
	public static double yPosStartView = 0.0;
	
	// current x and y position from startView if app is running
	// this values will change from InvalidationListener from startView
	// IMPORTANT: to place the highscore view always in the startView window
	public static double current_X_Pos_StartView = 0.0;
	public static double current_Y_Pos_StartView = 0.0;
	
	// Height and Width from start view -> this values used for writing
	// last window size in xml
	public static double heightStartView = 0.0;
	public static double widthStartView = 0.0;
	
	// Standard Werte für die Höhe und Breite der StartView
	public static final Double DEFAULT_X_POS_STARTVIEW = 30.0;
	public static final Double DEFAULT_Y_POS_STARTVIEW = 30.0;
	
	// Standard Werte für die Höhe und Breite der StartView
	public static final Double DEFAULT_HEIGHT_STARTVIEW = 350.0;
	public static final Double DEFAULT_WIDTH_STARTVIEW = 650.0;
	
	// Standard Werte für die mindest Größe der startView
	public static final Double MIN_HEIGHT_STARTVIEW = 320.0;
	public static final Double MIN_WIDTH_STARTVIEW = 580.0;
	
	// Feste Werte der Radio Buttons - Speed in der SettingsView
	public static final Double GAME_SPEED_LOW = 0.25;
	public static final Double GAME_SPEED_MEDIUM = 0.15;
	public static final Double GAME_SPEED_HIGH = 0.08;

	// Feste Werte der RadioButtons für den Game Background
	public static final String GAME_BACKGROUND_NONE = "None";
	public static final String GAME_BACKGROUND_GREEN = "Green";
	public static final String GAME_BACKGROUND_GREY = "Grey";
	
	// Standard Wert für Rand im Spiel -> per default ist die Einstellung ohne Rand
	public static final boolean DEFAULT_SETTING_FOR_BORDER = false;
	
	// Standard Wert für das zeigen der InfoBox beim Spielstart -> per default wird die InfoBox gezeigt
	public static final boolean DEFAULT_SETTING_INFOBOX = true;
	
	// Standard Wert für Musik im Spiel -> per default ist die Einstellung ohne Musik
	public static final boolean DEFAULT_SETTING_MUSIC = false;
	
	// Standard Wert für die Lautstärke bei eingeschalteter Musik
	public static final Double DEFAULT_VOLUME_LEVEL = 15.0;
	
	// Pfad zur MP3 für die Spielmusik
	public static final String PATH_MUSIC = "/snakeGameMusic.mp3";
	
	// Pfad zu MP3 für den GameOver Soundtrack
	public static final String PATH_GAMEOVER = "/gameOverSound.mp3";
	
	// Pfad zu WAV für den Highscore Soundtrack
	public static final String PATH_HIGHSCORE = "/highscoreSound.mp3";

	// Block Größe für alle Spielsteine
	public static final int BLOCK_SIZE = 20;
	// Größe des Spielfensters
	public static final int GAME_WIDTH = 30 * BLOCK_SIZE;
	public static final int GAME_HEIGHT = 20 * BLOCK_SIZE;
	
	// Punkte je eingesammelten Essen
	public static final int POINTS = 20;

	// Rechteck für das Essen der Schlange
	private final Rectangle food;

	/**
	 * Konstruktor -> initialisiert die jeweiligen Instanzvariablen
	 * 
	 * @param primaryStage -> Hauptfenster
	 */
	public DataBean(Stage primaryStage) {
		
		// primaryStage initialisieren
		this.startStage = primaryStage;

		// Separate Stage für die Einstellungen erzeugen
		this.settingsStage = new Stage();
		// Window Modality setzen damit das Fenster erst geschlossen werden muss
		this.settingsStage.initModality(Modality.APPLICATION_MODAL);
		// Stage ist nicht vergößerbar
		this.settingsStage.setResizable(false);

		// Separate Stage für die Spielszene erstellen
		this.gameStage = new Stage();
		// Stage ist nicht vergößerbar
		this.gameStage.setResizable(false);
		
		// stage for showing highscore
		this.highscoreStage = new Stage();
		// Window Modality setzen damit das Fenster erst geschlossen werden muss
		this.highscoreStage.initModality(Modality.APPLICATION_MODAL);
		this.highscoreStage.setResizable(false);

		// Instanz des StartViewControllers mit null initialisieren
		this.startViewController = null;

		// Instanz des SettingsviewControllers mit null initialisieren
		this.settingsController = null;

		// Instanz des GameViewControllers mit null initialisieren
		this.gameController = null;
		
		// initialize the Highscore Controller with null
		this.highscoreController = null;
		
		// initialize the map for the specified game settings with null
		this.specifiedGameSettings = null;

		// Größe des Rectangles für das Essen festlegen
		this.food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
		
		// counter for key "space" at beginning with 0 initialize
		this.counterKeySpace = 0;

		// Standard wert bei Spielstart ob Schlange in Bewegung ist
		this.snakeMoved = false;
		// Standard wert für die Game Loop beim Start
		this.gameRunning = false;
		// Spiel ist am Anfang NICHT pausiert
		this.gamePause = false;
		
		// initialized timeLine for the game animation with null
		this.timeLine = null;
		// initialized timeLine for the try again button animation with null
		this.timeLineAfterGameOver = null;
		// initialized timeline for highscore value animation with null
		this.timeLineHighscoreValue = null;
		
		// KeyFrame für die Animation TimeLine mit null initialisieren
		this.keyFrame = null;

		// MediaPlayer für Game Musik initialisieren -> als Parameter wird die Media Datei die
		// abgespielt werden soll
		// übergeben
		URL musicFileUrl = this.getClass().getResource(PATH_MUSIC);
		this.mediaFileGameMusic = new Media(musicFileUrl.toString());
		this.mediaPlayerGameMusic = new MediaPlayer(this.mediaFileGameMusic);
		// die Spielmusik endlos abspielen
		this.mediaPlayerGameMusic.setCycleCount(MediaPlayer.INDEFINITE);
		
		// MediaPlayer für GameOver Soundtrack initialisieren
		URL musicFileUrlGameOver = this.getClass().getResource(PATH_GAMEOVER);
		this.mediaFileGameOver = new Media(musicFileUrlGameOver.toString());
		this.mediaPlayerGameOver = new MediaPlayer(this.mediaFileGameOver);
		// Abspielzeit auf 3 sec. begrenzen
		this.mediaPlayerGameOver.setStopTime(Duration.seconds(3.0));
		
		// MediaPlayer für den Highscore initialisieren
		URL musicFileHighscore = this.getClass().getResource(PATH_HIGHSCORE);
		this.mediaFileHighscore = new Media(musicFileHighscore.toString());
		this.mediaPlayerHighscore = new MediaPlayer(this.mediaFileHighscore);

		// Score and highscore initialize
		this.currentScore = 0;
		this.highscoreAtGamePlay = 0;
		this.highscoreAtStart = 0;

	}
	
	/**
	 * Method to set the read values from xml file for startView Props in model.
	 * 
	 * @param startViewProps -> [Map<String, String] startView props
	 */
	public void setSpecifiedStartViewProps(Map<String, String> startViewProps) {
		
		// iterate over map with startView props
		for(Map.Entry<String, String> entry : startViewProps.entrySet()) {
			
			// set values the height, width, xPos and yPos of the startView window
			if(entry.getKey().equals(GameSettings.ELEMENT_STARTVIEW_HEIGHT)) {
				DataBean.heightStartView = Double.parseDouble(entry.getValue());
			}
			
			if(entry.getKey().equals(GameSettings.ELEMENT_STARTVIEW_WIDTH)) {
				DataBean.widthStartView = Double.parseDouble(entry.getValue());
			}
			
			if(entry.getKey().equals(GameSettings.ELEMENT_STARTVIEW_X_POS)) {
				DataBean.xPosStartView = Double.parseDouble(entry.getValue());
			}
			
			if(entry.getKey().equals(GameSettings.ELEMENT_STARTVIEW_Y_POS)) {
				DataBean.yPosStartView = Double.parseDouble(entry.getValue());
			}			
		}
	}

	/**
	 * Getter für das Start Fenster -> startView
	 * 
	 * @return -> Stage
	 */
	public Stage getStartStage() {
		return this.startStage;
	}

	/**
	 * Getter für die Settings-Stage -> SettingsView
	 * 
	 * @return -> Stage
	 */
	public Stage getSettingsStage() {
		return this.settingsStage;
	}

	/**
	 * Getter für die Game-Stage -> GameView
	 * 
	 * @return -> Stage
	 */
	public Stage getGameStage() {
		return this.gameStage;
	}
	
	/**
	 * Getter for the highscore stage
	 * @return	->	Stage
	 */
	public Stage getHighscoreStage() {
		return this.highscoreStage;
	}

	/**
	 * Getter für das Path object des Benutzer Verezichnis
	 * 
	 * @return
	 */
	public Path getPathToXML() {
		return this.pathToXML;
	}

	public void setPathToXML(Path userDir) {
		this.pathToXML = userDir;
	}

	public Double getGameSpeed() {
		return this.gameSpeed;
	}

	public void setGameSpeed(Double speed) {
		this.gameSpeed = speed;
	}

	public boolean playWithBorder() {
		return this.hasBorder;
	}

	public void setBorderState(boolean state) {
		this.hasBorder = state;
	}

	public Direction getSnakeDirection() {
		return this.directionOfSnake;
	}

	public void setSnakeDirection(Direction direction) {
		this.directionOfSnake = direction;
	}

	public boolean getSnakeMoved() {
		return this.snakeMoved;
	}

	public void setSnakeMoved(boolean moved) {
		this.snakeMoved = moved;
	}

	public boolean getGameRunningState() {
		return this.gameRunning;
	}

	public void setGameIsRunning(boolean run) {
		this.gameRunning = run;
	}

	public Boolean getGameIsPaused() {
		return this.gamePause;
	}

	public void setGameIsPaused(Boolean isPaused) {
		this.gamePause = isPaused;
	}

	public void setTimeLine(Timeline tLine) {
		this.timeLine = tLine;
	}

	public Timeline getTimeLine() {
		return this.timeLine;
	}
	
	public void setTimeLineTryAgain(Timeline tLine) {
		this.timeLineAfterGameOver = tLine;
	}

	public Timeline getTimeLineTryAgain() {
		return this.timeLineAfterGameOver;
	}
	
	public void setTimeLineHighscoreValue(Timeline tline) {
		this.timeLineHighscoreValue = tline;
	}
	
	public Timeline getTimeLineHighscoreValue() {
		return this.timeLineHighscoreValue;
	}

	public MediaPlayer getMediaPlayerGameMusic() {
		return this.mediaPlayerGameMusic;
	}
	
	public MediaPlayer getMediaPlayerGameOver() {
		return this.mediaPlayerGameOver;
	}
	
	public MediaPlayer getMediaPlayerHighscore() {
		return this.mediaPlayerHighscore;
	}

	public int getCurrentScore() {
		return this.currentScore;
	}

	public void addPointsToCurrentScore(int points) {
		this.currentScore += points;
	}

	public void resetCurrentScore() {
		this.currentScore = 0;
	}
	
	public void setHighScoreAtGamePlay(int valueOfHighScore) {
		this.highscoreAtGamePlay = valueOfHighScore;
	}
	
	public int getHighScoreAtGamePlay() {
		return this.highscoreAtGamePlay;
	}
	
	public void setHighScoreAtStart(int valueOfHighscore) {
		this.highscoreAtStart = valueOfHighscore;
	}
	
	public int getHighScoreAtStart() {
		return this.highscoreAtStart;
	}

	public Double getValueOfVolume() {
		return this.valueOfVolume;
	}

	public void setValueOfVolume(double value) {
		this.valueOfVolume = value;
	}

	public StartViewController getStartViewController() {
		return this.startViewController;
	}

	public void setStartViewController(StartViewController startCon) {
		this.startViewController = startCon;
	}

	public SettingsViewController getSettingsController() {
		return this.settingsController;
	}

	public void setSettingsController(SettingsViewController settingsCon) {
		this.settingsController = settingsCon;
	}

	public GameViewController getGameController() {
		return this.gameController;
	}

	public void setGameController(GameViewController gameCon) {
		this.gameController = gameCon;
	}
	
	public void setHighscoreController(HighScoreViewController highscoreCon) {
		this.highscoreController = highscoreCon;
	}
	
	public HighScoreViewController getHighscoreController() {
		return this.highscoreController;
	}

	public Boolean isMusicPlaying() {
		return this.musicPlay;
	}

	public void setMusicPlayState(boolean state) {
		this.musicPlay = state;
	}

	public String getGameBackground() {
		return this.gameBackground;
	}

	public void setGameBackground(String background) {
		this.gameBackground = background;
	}

	public ObservableList<Node> getSnake() {
		return this.snake;
	}

	public void setSnake(ObservableList<Node> snakeRectList) {
		this.snake = snakeRectList;
	}

	public Rectangle getFood() {
		return this.food;
	}

	public KeyFrame getKeyFrame() {
		return this.keyFrame;
	}

	public void setKeyFrame(KeyFrame keyFrame) {
		this.keyFrame = keyFrame;
	}
	
	public int getCounterKeySpace() {
		return this.counterKeySpace;
	}
	
	public void resetCounterKeySpace() {
		this.counterKeySpace = 0;
	}
	
	public void IncrementCounterKeySpace() {
		this.counterKeySpace++;
	}
	
	public boolean getFlagForInfoBoxShowing() {
		return this.showInfoBoxAtGameStart;
	}
	
	public void setFlagForInfoBoxShowing(boolean showInfoBox) {
		this.showInfoBoxAtGameStart = showInfoBox;
	}

	/**
	 * @return the specifiedGameSettings
	 */
	public Map<String, String> getSpecifiedGameSettings() {
		return this.specifiedGameSettings;
	}

	/**
	 * @param specifiedGameSettings the specifiedGameSettings to set
	 */
	public void setSpecifiedGameSettings(Map<String, String> specifiedGameSettings) {
		this.specifiedGameSettings = specifiedGameSettings;
	}
}
