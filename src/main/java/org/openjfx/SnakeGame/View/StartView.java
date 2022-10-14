package org.openjfx.SnakeGame.View;

import org.openjfx.SnakeGame.model.DataBean;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasse die das Start View(GUI) erzeugt -> das View ist von Model und
 * Controller entkoppelt so das es beliebig ausgetauscht werden kann.
 * 
 * Wichtigste Aufgaben der View: -> ist für die Präsentation von Daten zuständig
 * -> initialisiert, organisiert und speichert alle Oberflächen Elemente -> ist
 * für Style und Layout zuständig
 * 
 * @author Christian
 *
 */
public class StartView {
	// Instanzvariable zum speichern der Nodes-> private final
	private final Scene scene;
	private final GridPane root;
	private final VBox vBoxBtn;
	private final VBox vBoxPlaceHolder;
	private final Button btnPlay;
	private final Button btnHighscore;
	private final Button btnSettings;
	private final BackgroundImage startImageView;
	private final Image imageForWindowIcon;
	private final ImageView imageViewBtnPlay;
	private final ImageView imageViewBtnCup;
	private final ImageView imageViewBtnSettings;

	/**
	 * Konstruktor zum initialisieren der Nodes
	 */
	public StartView() {
		// Image für Dockbar links oben im Hauptfenster
		this.imageForWindowIcon = new Image("/snake_img_dock.png");
		
		// Background Image für das start Fenster
		// der letzte Parameter new BackgroundSize() bewirkt das dass Bild sich mit
		// vergrößern des Fensters mit vergrößert
		this.startImageView = new BackgroundImage(new Image("/snake_start.jpg"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		
		// ImageView für Buttons
		this.imageViewBtnPlay = new ImageView(new Image("/play.png"));
		this.imageViewBtnCup = new ImageView(new Image("/cup.png"));
		this.imageViewBtnSettings = new ImageView(new Image("/settings32px.png"));

		// root Node -> Main Layout Container
		this.root = new GridPane();
		this.root.setBackground(new Background(this.startImageView));

		this.root.setPadding(new Insets(10.0));
		this.root.setVgap(10.0);
		this.root.setHgap(10.0);
		// placing the root node always in center
		this.root.setAlignment(Pos.CENTER);

		// VBox Container for buttons
		this.vBoxBtn = new VBox(20.0);
		// VBox Container as PlaceHolder
		this.vBoxPlaceHolder = new VBox();
		this.vBoxPlaceHolder.setPrefSize(150.0, 150.0);

		// Buttons
		//
		// Button -> Play
		this.btnPlay = new Button("Play", this.imageViewBtnPlay);
		this.btnPlay.setMaxHeight(Double.MAX_VALUE);
		this.btnPlay.setMaxWidth(Double.MAX_VALUE);
		
		// Button -> Highscore
		this.btnHighscore = new Button("Highscore", this.imageViewBtnCup);		
		this.btnHighscore.setMaxHeight(Double.MAX_VALUE);
		this.btnHighscore.setMaxWidth(Double.MAX_VALUE);
		
		// Button -> Settings
		this.btnSettings = new Button("Settings", this.imageViewBtnSettings);		
		this.btnSettings.setMaxHeight(Double.MAX_VALUE);
		this.btnSettings.setMaxWidth(Double.MAX_VALUE);

		// add buttons to VBox
		this.vBoxBtn.getChildren().addAll(this.btnPlay, this.btnHighscore, this.btnSettings);

		// add VBox with buttons to root node
		this.root.add(this.vBoxBtn, 0, 2);
		this.root.add(this.vBoxPlaceHolder, 1, 2);

		// create scene
		this.scene = new Scene(this.root);
		// Stylesheet CSS laden
		this.scene.getStylesheets().add(this.getClass().getResource("/application.css").toExternalForm());
		
		// Buttons die Stylesheets zuweisen
		this.btnPlay.getStyleClass().add("buttonsSnakeGame");
		this.btnHighscore.getStyleClass().add("buttonsSnakeGame");
		this.btnSettings.getStyleClass().add("buttonsSnakeGame");
	}

	/**
	 * Methode welche die hier erstellte View (Scene) mit einer Stage koppelt, damit
	 * diese Start UI auch angezeigt werden kann!!!
	 * 
	 * @param primaryStage -> MainWindow
	 */
	public void show(Stage primaryStage) {
		
		primaryStage.setTitle("Snake - Game");
		primaryStage.setScene(this.scene);
		// Set window icon
		primaryStage.getIcons().add(this.imageForWindowIcon);
		
		// Mindest Größe des Hauptfensters festlegen
		primaryStage.setMinWidth(DataBean.MIN_WIDTH_STARTVIEW);
		primaryStage.setMinHeight(DataBean.MIN_HEIGHT_STARTVIEW);
		
		this.root.setPrefSize(DataBean.widthStartView, DataBean.heightStartView);
		
		// positionieren des start fensters
		if(DataBean.xPosStartView == DataBean.DEFAULT_X_POS_STARTVIEW
				&& DataBean.yPosStartView == DataBean.DEFAULT_Y_POS_STARTVIEW) {
			// place start view always in center of screen ONLY if no others values set in xml
			primaryStage.centerOnScreen();
		}
		
		else {
			primaryStage.setX(DataBean.xPosStartView);
			primaryStage.setY(DataBean.yPosStartView);
		}			
	
		primaryStage.show();
		
	}

	/*
	 * Jetzt kommen ausschließlich die Getter-Methoden um den zugriff auf relevante
	 * Nodes zu gewährleisten -> sprich nur Nodes mit denen der Nutzer interagiert
	 * werden über die Getter nach Außen bereitgestellt
	 */

	public Button getBtnPlay() {
		return this.btnPlay;
	}

	public Button getBtnHighscore() {
		return this.btnHighscore;
	}
	
	public Button getBtnSettings() {
		return this.btnSettings;
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
