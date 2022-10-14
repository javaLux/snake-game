package org.openjfx.SnakeGame.main;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.openjfx.SnakeGame.Controller.SettingsViewController;
import org.openjfx.SnakeGame.Controller.StartViewController;
import org.openjfx.SnakeGame.businessLogic.ErrorBoxSwing;
import org.openjfx.SnakeGame.businessLogic.GameScore;
import org.openjfx.SnakeGame.businessLogic.GameSettings;
import org.openjfx.SnakeGame.businessLogic.JDomReader;
import org.openjfx.SnakeGame.businessLogic.LogCurrentDateTime;
import org.openjfx.SnakeGame.model.DataBean;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

	// private instance variable for the primary stage
	private Stage primaryStage = null;
	
	// private instance variable for the data model
	private DataBean dataBean = null;
	
	// Pfad zur XML-Datei für den HighScore
	private Path pathToXML = null;
	
	// path to logFile
	private Path pathToLogFile = null;

	// parsed integer value for the highscore from xml file
	private int highscore = 0;
	
	// boolean for flag to be show info box at game start
	private boolean isInfoBoxShowing = false;
	
	// temporäre Map für die gelesen Game Settings aus der XML-Datei
	private Map<String, String> tempMapGameSettings = null;
	
	// temporäre Map für die startView Window properties
	private Map<String, String> tempMapStartViewProps = null;
	
	/**
	 * Method starts before App is running
	 */
	@Override
	public void init() {
		
		// check which java version is installed on host system
		String javaVersion = System.getProperty("java.version");
		
		// split java version on dot
		List<String> list = Arrays.asList(javaVersion.split("[.]"));
				
		// check if list not empty
		if(! list.isEmpty()) {
			try {
				int firstVersionNumber = Integer.parseInt(list.get(0));
				
				if(firstVersionNumber < 11) {
					ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
							"Unsupported java version detected.\nCurrent version  :\t" + javaVersion
							+ "\nExpected version:\t11"
							+ "\n\nPlease install OpenJDK 11 or higher\nto play Snake - Game.",
							new ImageIcon(getClass().getResource("/snake_img_dock.png")));
					// quit game
					System.exit(1);
				}
				
			} catch (NumberFormatException ex) {
				// can not parse the integer -> get info to user
				// if the first number in the list equal or higher than 11
				ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
						"Can not detect the current java version on this machine.\n"
						+ "Please make sure that you have installed Java correctly",
						new ImageIcon(getClass().getResource("/snake_img_dock.png")));
			}
			
		}
		else {
			// can not parse the integer -> get info to user
			// if the first number in the list equal or higher than 11
			ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
					"Can not detect the current java version on this machine.\n"
					+ "Please make sure that you have installed Java correctly",
					new ImageIcon(getClass().getResource("/snake_img_dock.png")));
			// quit game
			System.exit(1);
		}
		
		try {
			// get absolute path to xml file
			this.pathToXML = GameScore.getAbsPathToXML();
			// get path to logFile
			this.pathToLogFile = GameScore.getPathToLogFile();
			

		} catch (InvalidPathException ex) {
			// it throws an InvalidPathException if path can not resolved to a valid path (path contains invalid characters)
			// show exception stacktrace with swing msg box
			if (ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: an error occurred", null)) {
				System.exit(1);
			}
		}
		
		// value of highscore as string read from xml file
		String stringHighscoreValue = "";
		
		if (this.pathToXML != null) {
			
			// if xml file already exists -> check if they a valid xml file (JDom)
			if (GameScore.existsXML(this.pathToXML)) {
				
				// create logfile and write dateTime of executing in logfile
				LogCurrentDateTime.logExecutedProgram(this.pathToLogFile);
				
				stringHighscoreValue = GameScore.getHighScoreFromXML(this.pathToXML, "highscore");
				
				try {
					// try to parse highscore value from xml
					this.highscore = Integer.parseInt(stringHighscoreValue);
					// try to read boolean value for showing info box at game start from xml
					this.isInfoBoxShowing = GameSettings.getValueForShowInfoBox();

					// get all game settings from xml file
					this.tempMapGameSettings = GameSettings.getGameSettings();
					
					// get values for startView
					this.tempMapStartViewProps = GameSettings.getStartViewProps();
					
				} catch (NumberFormatException ex) {
					// if failed to parse string value from xml to an integer
					// info at user and run game with highscore value of zero
					ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
							"Failed to read highscore from xml file.\n\nHighscore reset to zero.",
							new ImageIcon(getClass().getResource("/snake_img_dock.png")));
					// if no integer has parsed from xml than create new valid xml with highscore of zero
					GameScore.createValidXML(this.pathToXML);
					// write new element "settings" in xml with attribute
					// for showing infobox with value "true" at game start
					GameSettings.writeSettingsElements();
					
					// write new element for startView in xml
					GameSettings.writeElementForStartViewWindow();
					
					this.isInfoBoxShowing = GameSettings.getValueForShowInfoBox();
					
					// get all default game settings from xml file
					this.tempMapGameSettings = GameSettings.getGameSettings();
					
					// get values for startView
					this.tempMapStartViewProps = GameSettings.getStartViewProps();
				}
				
			// if no xml file exists, create new xml file with root and child elements (JDOM)
			} else {		
				
				try {
					
					if(GameScore.createNewXML_File(this.pathToXML)) {
						
						// create logfile and write dateTime of executing in logfile
						LogCurrentDateTime.logExecutedProgram(this.pathToLogFile);
						
						// create new root element with highscore 0 for new xml file
						GameScore.writeToXML(this.pathToXML, GameScore.XML_ROOT_ELEMENT, "highscore", 0, false);
						// check if xml has an highscore, if this true -> read the value and try to parse it to an Integer object
						stringHighscoreValue = JDomReader.getHighScoreFromXML(this.pathToXML, "highscore");
						// and try to parse string value to an Integer object						
						this.highscore = Integer.parseInt(stringHighscoreValue);
						// write new element "settings" in xml with attribute
						// for showing infobox with value "true" at game start
						GameSettings.writeSettingsElements();
						
						GameSettings.writeElementForStartViewWindow();
						
						this.isInfoBoxShowing = GameSettings.getValueForShowInfoBox();
						
						// get all default game settings from xml file
						this.tempMapGameSettings = GameSettings.getGameSettings();
						
						// get values for startView
						this.tempMapStartViewProps = GameSettings.getStartViewProps();
					}
					
				} catch (SecurityException ex) {
					// falls durch AntiViren Programm der Zugriff verweigert wurde
					// Info an user geben
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					System.exit(1);

				} catch (IOException ex) {
					// falls ein Fehler beim erstellen der Datei aufgetreten ist
					// Info an User ausgeben
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					System.exit(1);
				}
				catch (NumberFormatException ex) {
					// if failed to parse string value from xml to an integer
					// info at user and run game with highscore value of zero
					ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
							"Failed to read highscore from xml file.\n\nHighscore reset to zero.",
							new ImageIcon(getClass().getResource("/snake_img_dock.png")));
					// if no integer has parsed from xml than create new valid xml with highscore of zero
					GameScore.createValidXML(this.pathToXML);
					// write new element "settings" in xml with attribute
					// for showing infobox with value "true" at game start
					GameSettings.writeSettingsElements();
					
					GameSettings.writeElementForStartViewWindow();
					
					this.isInfoBoxShowing = GameSettings.getValueForShowInfoBox();
					
					// get all default game settings from xml file
					this.tempMapGameSettings = GameSettings.getGameSettings();
					
					// get values for startView
					this.tempMapStartViewProps = GameSettings.getStartViewProps();
				}
			}
		}
	}

	/**
	 * Method starts the application -> main entry point for all JavaFX applications
	 */
	@Override
	public void start(Stage primaryStage) {
		// primaryStage speichern
		this.primaryStage = primaryStage;
			
		// Model erzeuegn -> intern wird hier die primaryStage gehalten
		this.dataBean = new DataBean(this.primaryStage);

		// Controller für die Start View erzeugen und das Model
		// übergeben damit die View in der primaryStage angezeigt werden kann, da das
		// Model die
		// primaryStage intern hält
		StartViewController startViewController = new StartViewController(this.dataBean);
		
		// Neu erzeugte Instanz des StartViewControllers im Model sichern
		this.dataBean.setStartViewController(startViewController);
		
		// Pfad zur XML-Datei im Model sichern
		this.dataBean.setPathToXML(this.pathToXML);
		
		// save start HighScore in Model
		this.dataBean.setHighScoreAtStart(this.highscore);
		
		// save flag for showing info box from xml file in model
		this.dataBean.setFlagForInfoBoxShowing(this.isInfoBoxShowing);
		
		// save read settings from xml
		this.dataBean.setSpecifiedGameSettings(this.tempMapGameSettings);
		
		// save read props for the startView
		this.dataBean.setSpecifiedStartViewProps(this.tempMapStartViewProps);
		
		// initialized the settings view controller -> this is important to have the instance
		// of object in GameView controller if user not set the settings menu before gaming
		this.dataBean.setSettingsController(new SettingsViewController(this.dataBean));
		
		// Add InvalidationListener to primary stage -> listens to x and y position on screen
		primaryStage.xProperty().addListener(new primaryStagePosListener());
		primaryStage.yProperty().addListener(new primaryStagePosListener());
		
//		// Add InvalidationListener to primaryStage -> listens width and height
//		primaryStage.widthProperty().addListener(new primaryStageSizeListener());
//		primaryStage.heightProperty().addListener(new primaryStageSizeListener());
		
		// last but not least -> show the startView window
		startViewController.show();
	}
	
	/**
	 * Method called automatically if JavaFX App will closed.
	 * Saved the Highscore, Settings and properties of the startView window in xml.
	 */
	@Override
	public void stop() {
		// first save highscore -> in this method it will be checked if xml file exists and if
		// them an valid xml file
		// save current points as new highscore in xml
		GameScore.saveHighScoreInXML(this.dataBean.getPathToXML(), this.dataBean.getHighScoreAtStart());
		
		// at next step -> write valid settings elements in xml file to save the user settings
		GameSettings.writeSettingsElements();
		// save user settings
		// write current game settings in xml file
		GameSettings.saveGameSettingsInXML(this.dataBean.getFlagForInfoBoxShowing(),
				(Double) this.dataBean.getSettingsController().getToggleGroupGameSpeedFromSettingsView().getSelectedToggle().getUserData(),
				(String) this.dataBean.getSettingsController().getToggleGroupBackgroungFromSettingsView().getSelectedToggle().getUserData(),
				this.dataBean.playWithBorder(), this.dataBean.isMusicPlaying(), this.dataBean.getValueOfVolume());
		
		// write always elemnts for the startView props in xml before app will closed
		GameSettings.writeElementForStartViewWindow();
		// save the last position and size of the startView window before app will closed
		GameSettings.saveStartViewProps(this.dataBean.getStartStage().getHeight(), this.dataBean.getStartStage().getWidth(),
				this.dataBean.getStartStage().getX(), this.dataBean.getStartStage().getY());
	}
	
	// Main method -> main entry point for JVM
	public static void main(String[] args) {
		launch(args);
	}

//	// InvalidationListener -> listens to changes of size of the primaryStage
//	// hat eine bessere Performance als der ChangeListener!!!
//	class primaryStageSizeListener implements InvalidationListener {
//
//		@Override
//		public void invalidated(Observable observable) {
//			// wenn sich die Größe der primaryStage verändert wird die veränderte Höhe und
//			// Breite
//			// ausgegeben -> weiterhin kann hierdurch die Fenstergrößer immer an den User
//			// angepasst werden
//			// sprich beim wechseln in verschiedene Fenster mit der gleichen primaryStage
//			// kann somit
//			// die Fenstergröße festgehalten werden
//			
//			// save height and width value in model if this changed
//			DataBean.heightStartView = primaryStage.getHeight();
//			DataBean.widthStartView = primaryStage.getWidth();
//		}
//	}
	
	// this InvalidationListener listens to the x and y position of a stage on screen
	// if stage is moved (drag&drop) on desktop -> you can get the changed x and y position
	class primaryStagePosListener implements InvalidationListener {

		@Override
		public void invalidated(Observable observable) {
			
			// save current position of startView (stage) in model if this is changing
			// IMPORTANT: to place the highscore view always in the startView window
			DataBean.current_X_Pos_StartView = primaryStage.getX();
			DataBean.current_Y_Pos_StartView = primaryStage.getY();

		}
	}
}