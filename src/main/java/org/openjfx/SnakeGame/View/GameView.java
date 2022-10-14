package org.openjfx.SnakeGame.View;

import org.openjfx.SnakeGame.model.DataBean;

import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Klasse die das Game View(GUI) erzeugt -> das View ist von Model und
 * Controller entkoppelt so das es beliebig ausgetauscht werden kann.
 * 
 * Wichtigste Aufgaben der View: -> ist für die Präsentation von Daten zuständig
 * -> initialisiert, organisiert und speichert alle Oberflächen Elemente -> ist
 * für Style und Layout zuständig
 * 
 * @author Christian
 *
 */
public class GameView {
	
	// Instanzvariable zum speichern der Nodes-> private final
	// Layout container
	private final VBox root;

	// Game Zone
	private final Pane gameZone;

	// Group - für das anzeigen der Rechtecke für die Schlange
	private final Group snakeGroup;

	// Volume Slider
	private final Slider volumeSlider;

	// VBoxe's
	private final VBox vBoxTop;
	private final VBox vBoxMusic;
	private final VBox vBoxMusicSlider;
	private final VBox vBoxScoreLabels;
	private final VBox vBoxBtnAndLabelInfo;

	// HBoxe's
	private final HBox hBoxTop;
	private final HBox hBoxSliderAndLblMusic;
	private final HBox hBoxScore;
	private final HBox hBoxHighScore;
	private final HBox hBoxBtnBackBtnTryAgain;
	
	// Labels
	private final Label lblInfo;
	private final Label lblMusic;
	private final Label lblInfoMusicIsOff;
	private final Label lblCurrentScore;
	private final Label lblCurrentScoreValue;
	private final Label lblHighScore;
	private final Label lblHighScoreValue;
	private final Label lblVolumeValue;
	private final Label lblGameOver;

	// Separoters
	private final Separator sepMusic;
	private final Separator sepBetweenScore;
	private final Separator sepTop;
	private final Separator sepLeft;
	private final Separator sepRight;

	// ImageView für den Back-Button
	private final ImageView imageViewBtnChange;
	// ImageView für Tooltip Button-ChangeSettings
	private final ImageView imageViewTooltipBtnChange;
	// ImageView GameOver
	private final ImageView imageViewLblGameOver;

	// Button - Change Settings
	private final Button btnChangeSettings;
	
	// Button - Try again after game over
	private final Button btnTryAgain;
	
	// Tooltip Btn-ChangeSettings
	private final Tooltip tooltipBtnChange;
	
	// Image für Stage
	private final Image imageForWindowIcon;

	private final Scene scene;

	/**
	 * Konstruktor zum initialisieren der Nodes
	 */
	public GameView() {
		
		// Image für Dockbar und links oben im Hauptfenster
		this.imageForWindowIcon = new Image("/snake_img_dock.png");
		// ImageView für den back-Button
		this.imageViewBtnChange = new ImageView(new Image("/back.png"));
		// ImageView für den Tooltip Btn-Change
		this.imageViewTooltipBtnChange = new ImageView(new Image("/settings16px.png"));
		// ImageView für das GameOver Label
		this.imageViewLblGameOver = new ImageView(new Image("/gameOver.png"));

		// root Node -> Main Layout Container
		this.root = new VBox();
		// set fix width for the root layout container
		this.root.setMinWidth(DataBean.GAME_WIDTH);
		this.root.setMaxWidth(DataBean.GAME_WIDTH);

		// Layout Container - game zone mit festgelegter Größe
		this.gameZone = new Pane();
		this.gameZone.setMinWidth(DataBean.GAME_WIDTH);
		this.gameZone.setMinHeight(DataBean.GAME_HEIGHT);
		this.gameZone.setMaxWidth(DataBean.GAME_WIDTH);
		this.gameZone.setMaxHeight(DataBean.GAME_HEIGHT);
		
		// Group für die Rechtecke der Schlange initialisieren
		this.snakeGroup = new Group();

		// VBoxe's
		this.vBoxTop = new VBox();
		this.vBoxTop.setStyle("-fx-alignment: center");
		
		this.vBoxBtnAndLabelInfo = new VBox();
		this.vBoxMusic = new VBox();
		this.vBoxScoreLabels = new VBox();
		this.vBoxMusicSlider = new VBox(5.0);

		// Hboxe's
		this.hBoxTop = new HBox(10.0);
		this.hBoxSliderAndLblMusic = new HBox(10.0);
		this.hBoxScore = new HBox(10.0);
		this.hBoxHighScore = new HBox(10.0);
		this.hBoxBtnBackBtnTryAgain = new HBox(20.0);

		// Separators
		this.sepTop = new Separator(Orientation.HORIZONTAL);
		this.sepLeft = new Separator(Orientation.VERTICAL);
		this.sepRight = new Separator(Orientation.VERTICAL);
		this.sepMusic = new Separator(Orientation.HORIZONTAL);
		this.sepBetweenScore = new Separator(Orientation.HORIZONTAL);
		this.sepBetweenScore.setMinWidth(150.0);
		
		// Tooltip Btn-ChangeSettings
		this.tooltipBtnChange = new Tooltip("Change game play settings");
		this.tooltipBtnChange.setGraphic(this.imageViewTooltipBtnChange);
		this.tooltipBtnChange.setShowDelay(Duration.seconds(0.2));
		
		// Button ChangeSettings
		this.btnChangeSettings = new Button("", this.imageViewBtnChange);
		this.btnChangeSettings.setTooltip(this.tooltipBtnChange);	
		// Button Try again
		this.btnTryAgain = new Button("Try again");
		this.btnTryAgain.setMaxHeight(Double.MAX_VALUE);

		// Slider
		this.volumeSlider = new Slider();
		this.volumeSlider.setCursor(Cursor.HAND);
		this.volumeSlider.setPrefWidth(80.0);

		// Labels
		this.lblInfoMusicIsOff = new Label("Music off");
		
		this.lblCurrentScore = new Label("Current score:");
		this.lblCurrentScore.setMinWidth(100.0);
		
		this.lblCurrentScoreValue = new Label();
		this.lblCurrentScoreValue.setMaxWidth(Double.MAX_VALUE);
		
		this.lblHighScore = new Label("Highscore:");
		this.lblHighScore.setMinWidth(100.0);
		
		this.lblHighScoreValue = new Label();
		this.lblHighScoreValue.setMaxWidth(Double.MAX_VALUE);
		
		this.lblMusic = new Label("Music volume");
		
		this.lblInfo = new Label(">ESC<        exit game\n>SPACE<   paused game");
		
		this.lblVolumeValue = new Label();
		this.lblVolumeValue.setMinWidth(35.0);
		
		this.lblGameOver = new Label("", this.imageViewLblGameOver);

		// HBox - Musik Slider + Label befüllen
		this.hBoxSliderAndLblMusic.getChildren().addAll(this.volumeSlider, this.lblVolumeValue);
		// HBox - Score befüllen
		this.hBoxScore.getChildren().addAll(this.lblCurrentScore, this.lblCurrentScoreValue);
		// HBox - HighScore befüllen
		this.hBoxHighScore.getChildren().addAll(this.lblHighScore, this.lblHighScoreValue);
		// HBox für Btn - Change Settings und Btn - Try Again
		this.hBoxBtnBackBtnTryAgain.getChildren().addAll(this.btnChangeSettings, this.btnTryAgain);
		
		// VBox - Btn Back + Btn Try again und Label info befüllen
		this.vBoxBtnAndLabelInfo.getChildren().addAll(this.hBoxBtnBackBtnTryAgain, this.lblInfo);
		// VBox - Musik MusicOff Label + Slider
		this.vBoxMusicSlider.getChildren().addAll(this.hBoxSliderAndLblMusic, this.lblInfoMusicIsOff);
		// VBox - Musik komplett befüllen
		this.vBoxMusic.getChildren().addAll(this.lblMusic, this.sepMusic, this.vBoxMusicSlider);
		// VBox - Score and Highscore befüllen
		this.vBoxScoreLabels.getChildren().addAll(this.hBoxScore, this.sepBetweenScore, this.hBoxHighScore);

		// HBox - BorderPane Top befüllen
		this.hBoxTop.getChildren().addAll(this.vBoxBtnAndLabelInfo, this.sepLeft, this.vBoxMusic, this.sepRight,
				this.vBoxScoreLabels);

		// VBox - BorderPane Top befüllen
		this.vBoxTop.getChildren().addAll(this.hBoxTop, this.sepTop);
		
		// Spielfeld das GameOver Label hinzufügen
		this.gameZone.getChildren().add(this.lblGameOver);
		// Auf der X-Achse mittig im Spielfeld platzieren
		this.lblGameOver.setTranslateX(((DataBean.GAME_WIDTH / 2) - 127));
		// Label - GameOver unsichtbar machen -> beim Start
		this.lblGameOver.setVisible(false);

		// root Node - befüllen
		this.root.getChildren().addAll(this.vBoxTop, this.gameZone);	
		
		Parent rootNode = this.root;
		
		// create scene -> mit dem Parent root Node
		this.scene = new Scene(rootNode);

		// Stylesheet CSS laden
		this.scene.getStylesheets().add(this.getClass().getResource("/application.css").toExternalForm());

		// Styling Labels via CSS custom style class
		this.lblInfo.getStyleClass().add("labelInfoGameView");
		this.lblMusic.getStyleClass().add("settingsLabels");
		this.lblCurrentScore.getStyleClass().add("settingsLabels");
		this.lblCurrentScoreValue.getStyleClass().add("settingsLabels");
		this.lblHighScore.getStyleClass().add("settingsLabels");
		this.lblHighScoreValue.getStyleClass().add("labelHighscore");
		this.lblInfoMusicIsOff.getStyleClass().add("musicOffLabel");

		// Styling VBox's
		this.vBoxBtnAndLabelInfo.getStyleClass().add("vBox");
		this.vBoxMusic.getStyleClass().add("vBox");
		this.vBoxScoreLabels.getStyleClass().add("vBox");
		this.vBoxTop.getStyleClass().add("vBox");
		// Styling Background from VBox Top above the GameZone
		this.vBoxTop.getStyleClass().add("gameTopVBox");

		// Styling Button Back
		this.btnChangeSettings.getStyleClass().add("buttonsSnakeGame");
		this.btnTryAgain.getStyleClass().add("buttonsSnakeGame");
		
		// Styling Tooltip für Btn-ChangeSettings
		this.btnChangeSettings.getStyleClass().add("tooltipBtn");

	}

	/**
	 * Methode welche die hier erstellte View (Scene) mit einer Stage koppelt, damit
	 * diese Eingabe UI auch angezeigt werden kann!!!
	 * 
	 * @param primaryStage -> MainWindow
	 */
	public void show(Stage gameStage) {
		
		gameStage.setTitle("Snake - Game");
		gameStage.setScene(this.scene);
		
		// Set window icon
		gameStage.getIcons().add(this.imageForWindowIcon);
		gameStage.show();
	}

	/*
	 * Jetzt kommen ausschließlich die Getter-Methoden um den zugriff auf relevante
	 * Nodes zu gewährleisten -> sprich nur Nodes mit denen der Nutzer interagiert
	 * werden über die Getter nach Außen bereitgestellt
	 */
	public Scene getScene() {
		return this.scene;
	}
	
	public VBox getRootNode() {
		return this.root;
	}

	public Pane getGamePane() {
		return this.gameZone;
	}

	public Label getLblCurrentScoreValue() {
		return this.lblCurrentScoreValue;
	}

	public Label getLblHighScoreValue() {
		return this.lblHighScoreValue;
	}

	public Label getLblMusicIsOff() {
		return this.lblInfoMusicIsOff;
	}

	public Label getLblInfo() {
		return this.lblInfo;
	}
	
	public Label getLblGameOver() {
		return this.lblGameOver;
	}

	public Button getBtnBack() {
		return this.btnChangeSettings;
	}
	
	public Button getBtnTryAgain() {
		return this.btnTryAgain;
	}
	
	public Slider getVolumeSlider() {
		return this.volumeSlider;
	}

	public Label getLblVolumeValue() {
		return this.lblVolumeValue;
	}

	public Group getSnakeGroup() {
		return this.snakeGroup;
	}
}
