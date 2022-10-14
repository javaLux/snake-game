package org.openjfx.SnakeGame.View;

import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasse die das Settings View(GUI) erzeugt -> das View ist von Model und
 * Controller entkoppelt so das es beliebig ausgetauscht werden kann.
 * 
 * Wichtigste Aufgaben der View: -> ist für die Präsentation von Daten zuständig
 * -> initialisiert, organisiert und speichert alle Oberflächen Elemente -> ist
 * für Style und Layout zuständig
 * 
 * @author Christian
 *
 */
public class SettingsView {

	// Instanzvariable zum speichern der Nodes-> private final
	private final BorderPane root;
	private final VBox vBoxTop;
	private final VBox vBoxCenter;
	private final VBox vBoxLeft;
	private final VBox vBoxRight;
	private final HBox hBoxBottom;
	private final VBox vBoxSpeed;
	private final VBox vBoxBorder;
	private final VBox vBoxMusic;
	private final VBox vBoxVolume;
	private final VBox vBoxGameBackground;
	private final HBox hBoxToggleBtn;
	private final HBox hBoxSliderAndLbl;

	private final Slider volumeSlider;

	private final Button btnApply;
	private final CheckBox checkBoxBorder;

	private final ToggleGroup toggleGroupBtnMusic;
	private final ToggleButton toggleBtnMusicOn;
	private final ToggleButton toogleBtnMusicOff;

	private final ToggleGroup toggleGroupGameSpeed;
	private final RadioButton radioLow;
	private final RadioButton radioMedium;
	private final RadioButton radioHigh;

	private final ToggleGroup toggleGroupBackground;
	private final RadioButton radioBackGrNone;
	private final RadioButton radioBackGrGreen;
	private final RadioButton radioBackGrGrey;

	private final Separator sepTop;
	private final Separator sepSpeed;
	private final Separator sepBorder;
	private final Separator sepMusic;
	private final Separator sepVolume;
	private final Separator sepGameBackground;

	private final Label lblVolume;
	private final Label lblSpeed;
	private final Label lblSettings;
	private final Label lblMusic;
	private final Label lblBorder;
	private final Label lblVolumeValue;
	private final Label lblGameBackground;

	private final Scene scene;
	private final Image imageForWindowIcon;
	private final ImageView imageViewBtnApply;
	private final ImageView imageViewToggleBtnOn;
	private final ImageView imageViewToggleBtnOff;

	/**
	 * Konstruktor zum initialisieren der Nodes
	 */
	public SettingsView() {
		// Image für Dockbar und links oben im Hauptfenster
		this.imageForWindowIcon = new Image("/snake_img_dock.png");
		// ImageView für den button Apply
		this.imageViewBtnApply = new ImageView(new Image("/apply.png"));
		// ImageView für den Togglebutton ON
		this.imageViewToggleBtnOn = new ImageView(new Image("/soundOn.png"));
		// ImageView für den Togglebutton OFF
		this.imageViewToggleBtnOff = new ImageView(new Image("/soundMute.png"));

		// root Node -> Main Layout Container
		this.root = new BorderPane();

		// VBoxes and HBoxes
		this.vBoxTop = new VBox(10.0);
		this.vBoxTop.setStyle("-fx-alignment: center");
		this.vBoxCenter = new VBox(10.0);
		this.vBoxLeft = new VBox(10.0);
		this.vBoxRight = new VBox(10.0);
		this.hBoxBottom = new HBox(10.0);
		this.hBoxBottom.setStyle("-fx-alignment: center");
		this.vBoxSpeed = new VBox(10.0);
		this.vBoxBorder = new VBox(10.0);
		this.vBoxMusic = new VBox(10.0);
		this.vBoxVolume = new VBox(10.0);
		this.vBoxGameBackground = new VBox(10.0);

		// HBox für Toggle Buttons
		this.hBoxToggleBtn = new HBox(10.0);
		// HBox für Slider und VolumeLabel
		this.hBoxSliderAndLbl = new HBox(10.0);

		// ToggleGroup für Radio Buttons - Speed
		this.toggleGroupGameSpeed = new ToggleGroup();
		// ToggleGroup für Radio Buttons - Game Background
		this.toggleGroupBackground = new ToggleGroup();
		// ToggleGroup für Toggle Buttons
		this.toggleGroupBtnMusic = new ToggleGroup();

		// Radio Buttons - Speed
		this.radioLow = new RadioButton("Low");
		this.radioMedium = new RadioButton("Medium");
		this.radioHigh = new RadioButton("High");
		// Add all Radiobuttons to togglegroup
		this.radioLow.setToggleGroup(this.toggleGroupGameSpeed);
		this.radioMedium.setToggleGroup(this.toggleGroupGameSpeed);
		this.radioHigh.setToggleGroup(this.toggleGroupGameSpeed);

		// Radio Buttons - Game Background
		this.radioBackGrNone = new RadioButton("None");
		this.radioBackGrGreen = new RadioButton("Green tiles");
		this.radioBackGrGrey = new RadioButton("Grey tiles");
		// Add all Radiobuttons to togglegroup
		this.radioBackGrNone.setToggleGroup(this.toggleGroupBackground);
		this.radioBackGrGreen.setToggleGroup(this.toggleGroupBackground);
		this.radioBackGrGrey.setToggleGroup(this.toggleGroupBackground);

		// Toggle Buttons
		this.toggleBtnMusicOn = new ToggleButton("", this.imageViewToggleBtnOn);
		this.toogleBtnMusicOff = new ToggleButton("", this.imageViewToggleBtnOff);
		// Add allTogglebuttons to togglegroup
		this.toggleBtnMusicOn.setToggleGroup(this.toggleGroupBtnMusic);
		this.toogleBtnMusicOff.setToggleGroup(this.toggleGroupBtnMusic);
		// Add Togglebuttons to HBox
		this.hBoxToggleBtn.getChildren().addAll(this.toggleBtnMusicOn, this.toogleBtnMusicOff);

		// Button Apply
		this.btnApply = new Button("Apply", this.imageViewBtnApply);
		// Add to HBox
		this.hBoxBottom.getChildren().add(this.btnApply);

		// Slider
		this.volumeSlider = new Slider();
		this.volumeSlider.setCursor(Cursor.HAND);
		this.volumeSlider.setPrefWidth(80.0);

		// CheckBox Border
		this.checkBoxBorder = new CheckBox("Border");

		// Labels
		this.lblSettings = new Label("Settings");
		this.lblSpeed = new Label("Game speed");
		this.lblBorder = new Label("Play with border");
		this.lblMusic = new Label("Game music");
		this.lblVolume = new Label("Volume level");
		this.lblGameBackground = new Label("Game background");
		this.lblVolumeValue = new Label();
		this.lblVolumeValue.setMinWidth(35.0);

		// Separoters
		this.sepTop = new Separator(Orientation.HORIZONTAL);
		this.sepSpeed = new Separator(Orientation.HORIZONTAL);
		this.sepMusic = new Separator(Orientation.HORIZONTAL);
		this.sepBorder = new Separator(Orientation.HORIZONTAL);
		this.sepVolume = new Separator(Orientation.HORIZONTAL);
		this.sepGameBackground = new Separator(Orientation.HORIZONTAL);

		// VBox - Speed befüllen
		this.vBoxSpeed.getChildren().addAll(this.lblSpeed, this.sepSpeed, this.radioLow, this.radioMedium,
				this.radioHigh);
		// VBox - Border befüllen
		this.vBoxBorder.getChildren().addAll(this.lblBorder, this.sepBorder, this.checkBoxBorder);
		// VBox - Music befüllen
		this.vBoxMusic.getChildren().addAll(this.lblMusic, this.sepMusic, this.hBoxToggleBtn);
		// HBox für Slider und VolumeValue Label befüllen
		this.hBoxSliderAndLbl.getChildren().addAll(this.volumeSlider, this.lblVolumeValue);
		// VBox - Volume befüllen
		this.vBoxVolume.getChildren().addAll(this.lblVolume, this.sepVolume, this.hBoxSliderAndLbl);
		// VBox - Game Background befüllen
		this.vBoxGameBackground.getChildren().addAll(this.lblGameBackground, this.sepGameBackground,
				this.radioBackGrNone, this.radioBackGrGreen, this.radioBackGrGrey);

		// root Node layout container hinzufügen
		// VBox - Top befüllen
		this.vBoxTop.getChildren().addAll(this.lblSettings, this.sepTop);
		this.vBoxLeft.getChildren().addAll(this.vBoxSpeed, this.vBoxBorder);
		this.vBoxCenter.getChildren().add(this.vBoxGameBackground);
		this.vBoxRight.getChildren().addAll(this.vBoxMusic, this.vBoxVolume);

		// root Node - BorderPane befüllen
		this.root.setTop(this.vBoxTop);
		this.root.setLeft(this.vBoxLeft);
		this.root.setCenter(this.vBoxCenter);
		this.root.setRight(this.vBoxRight);
		this.root.setBottom(this.hBoxBottom);

		// create scene
		this.scene = new Scene(this.root);
		// Stylesheet CSS laden
		this.scene.getStylesheets().add(this.getClass().getResource("/application.css").toExternalForm());

		// Styling BorderPane -> root node
		this.root.getStyleClass().add("borderPaneSettings");

		// Styling VBoxe's
		this.vBoxLeft.getStyleClass().add("vBox");
		this.vBoxCenter.getStyleClass().add("vBox");
		this.vBoxRight.getStyleClass().add("vBox");

		// Styling Labels
		this.lblSettings.getStyleClass().add("headerLabel");
		this.lblBorder.getStyleClass().add("settingsLabels");
		this.lblSpeed.getStyleClass().add("settingsLabels");
		this.lblMusic.getStyleClass().add("settingsLabels");
		this.lblVolume.getStyleClass().add("settingsLabels");
		this.lblGameBackground.getStyleClass().add("settingsLabels");

		// Styling ToggleButtons für Musik
		this.toggleBtnMusicOn.getStyleClass().add("toggleButton");
		this.toogleBtnMusicOff.getStyleClass().add("toggleButton");

		// Styling Button Apply
		this.btnApply.getStyleClass().add("buttonsSnakeGame");
	}

	/**
	 * Methode welche die hier erstellte View (Scene) mit einer Stage koppelt, damit
	 * diese Start UI auch angezeigt werden kann!!!
	 * 
	 * @param Stage -> Settings Stage from DataBean
	 */
	public void show(Stage stage) {
		// dieser Befehl bewirkt das dass Close Event per X oben rechts ignoriert wird
		// es wird eine Lambda Expression verwendet
		// diese ist gleich dem auskommentierten Eventhandler darunter
		stage.setOnCloseRequest(e -> e.consume());

//		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                event.consume();
//            }
//        });

		stage.setScene(scene);
		// Set window icon
		stage.getIcons().add(this.imageForWindowIcon);
		stage.show();
		// Mindest Größe des Fensters festlegen
		stage.setMinWidth(450.0);
		stage.setMinHeight(400.0);
	}

	/*
	 * Jetzt kommen ausschließlich die Getter-Methoden um den zugriff auf relevante
	 * Nodes zu gewährleisten -> sprich nur Nodes mit denen der Nutzer interagiert
	 * werden über die Getter nach Außen bereitgestellt
	 */
	public Slider getVolumeSlider() {
		return this.volumeSlider;
	}

	public Label getLblVolumeValue() {
		return this.lblVolumeValue;
	}

	public Button getBtnApply() {
		return this.btnApply;
	}

	public ToggleButton getToggleBtnON() {
		return this.toggleBtnMusicOn;
	}

	public ToggleButton getToggleBtnOFF() {
		return this.toogleBtnMusicOff;
	}

	public CheckBox getCheckBoxBorder() {
		return this.checkBoxBorder;
	}

	public RadioButton getRadioBtnLow() {
		return this.radioLow;
	}

	public RadioButton getRadioBtnMedium() {
		return this.radioMedium;
	}

	public RadioButton getRadioBtnHigh() {
		return this.radioHigh;
	}

	public ToggleGroup getToggleGroupGameSpeed() {
		return this.toggleGroupGameSpeed;
	}

	public ToggleGroup getToggleGroupMusicBtn() {
		return this.toggleGroupBtnMusic;
	}
	
	public ToggleGroup getToggleGroupBackground() {
		return this.toggleGroupBackground;
	}
	
	public RadioButton getRadioBtnBackGrNone() {
		return this.radioBackGrNone;
	}
	
	public RadioButton getRadioBtnBackGrGreen() {
		return this.radioBackGrGreen;
	}
	
	public RadioButton getRadioBtnBackGrGrey() {
		return this.radioBackGrGrey;
	}
}
