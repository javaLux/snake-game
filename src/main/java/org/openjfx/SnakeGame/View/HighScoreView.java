package org.openjfx.SnakeGame.View;

import org.openjfx.SnakeGame.model.DataBean;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Class shows the current highscore in a undecorated window, that means
 * the window have no border or a rand.
 * 
 * @author CSD
 *
 */
public class HighScoreView {
	
	// instance members
	private final Scene scene;
	private final Group root;
	private final HBox hBoxImages;
	private final ImageView dockImageView;
	private final ImageView moveImageView;
	private final ImageView exitImageView;
	private final ImageView cupImageView;
	private final Label lblHighscoreValue;
	private final Button btnMove;
	private final Button btnExit;
	private final Tooltip tooltipBtnMove;
	private final Tooltip tooltipBtnExit;
	
	/**
	 * Constructor initialize instance members
	 */
	public HighScoreView() {
		
		// root node for all nodes
		this.root = new Group();
		
		// horizontal layout for all Images (Buttons, Label)
		this.hBoxImages = new HBox(5.0);
		
		// all images
		this.dockImageView = new ImageView(new Image("/dock.png"));
		this.moveImageView = new ImageView(new Image("/move.png"));
		this.exitImageView = new ImageView(new Image("/check_btn.png"));
		this.cupImageView = new ImageView(new Image("/cup.png"));
		// add Effect (Reflection) to image cup
		this.cupImageView.setEffect(new Reflection());
		
		// highscore label
		this.lblHighscoreValue = new Label();		
		// add Effect (Reflection)
		this.lblHighscoreValue.setEffect(new Reflection());
		
		// tooltips
		// for btn move
		this.tooltipBtnMove = new Tooltip("Move on desktop");
		this.tooltipBtnMove.setFont(Font.font("Arial", 10.0));
		this.tooltipBtnMove.setShowDelay(Duration.seconds(0.2));
		// for btn exit
		this.tooltipBtnExit = new Tooltip("Exit");
		this.tooltipBtnExit.setFont(Font.font("Arial", 10.0));
		this.tooltipBtnExit.setShowDelay(Duration.seconds(0.2));
		
		// buttons
		// btnMove
		this.btnMove = new Button();
		this.btnMove.setPrefSize(32.0, 32.0);
		this.btnMove.setGraphic(this.moveImageView);
		this.btnMove.setTooltip(this.tooltipBtnMove);
		this.btnMove.setScaleX(0.8);
		this.btnMove.setScaleY(0.8);
		// add Effect (Reflection)
		this.btnMove.setEffect(new Reflection());
		
		// btnExit
		this.btnExit = new Button();
		this.btnExit.setPrefSize(32.0, 32.0);
		this.btnExit.setGraphic(this.exitImageView);
		this.btnExit.setTooltip(this.tooltipBtnExit);
		this.btnExit.setScaleX(0.8);
		this.btnExit.setScaleY(0.8);
		// add Effect (Reflection)
		this.btnExit.setEffect(new Reflection());
		
		// add all nodes to horizontal layout
		this.hBoxImages.getChildren().addAll(this.btnMove, this.btnExit,
				this.cupImageView, this.lblHighscoreValue);
		
		// Add ImageView with contains the icon images to the root node
		// first add black table dock
		this.root.getChildren().add(this.dockImageView);
		// second add hbox contains the images
		this.root.getChildren().add(this.hBoxImages);
		
		// place all nodes at hbox in right place
		this.dockImageView.setTranslateY(20.0);
		this.cupImageView.setTranslateX(35.0);
		this.cupImageView.setTranslateY(30.0);		
		this.lblHighscoreValue.setTranslateX(35.0);
		this.lblHighscoreValue.setTranslateY(30.0);
		this.btnMove.setTranslateX(10.0);
		
		
		// place the hbox in the middle of black table dock 		
		this.hBoxImages.setTranslateX(60.0);
		
		// create scene on transfer to the primary stage
		this.scene = new Scene(this.root);
		
		// load and take stylesheet informations from CSS file
		this.scene.getStylesheets().add(this.getClass().getResource("/application.css").toExternalForm());
		
		// styling label highscore value
		this.lblHighscoreValue.getStyleClass().add("shiny-orange");
		
		// styling all nodes who need a transparent style
		this.btnMove.setStyle("-fx-background-color: transparent");
		this.cupImageView.setStyle("-fx-background-color: transparent");
		this.btnExit.setStyle("-fx-background-color: transparent");
		
	}
	
	/**
	 * Methode welche die hier erstellte View (Scene) mit einer Stage koppelt, damit
	 * diese Highscore UI auch angezeigt werden kann!!!
	 * Und es wird eine weitere Stage übergeben die als Eigner fungiert
	 * So kann die Highscore stage an eine andere Stage (startView Stage)
	 * gebunden werden. Damit die Highscore Stage vor der StartView Stage
	 * angezeigt werden kann!!!
	 * 
	 * @param	primaryStage	->	MainWindow for the highscore View
	 * @param	ownerStage		->	owner stage for the highscore stage
	 */
	public void show(Stage primaryStage, Stage ownerStage, boolean onlyShowHighscoreStage) {
		
		// IMPORTANT Flag >onlyShowHighscoreStage<
		// this flag controlls how show the highscore stage
		if(!onlyShowHighscoreStage) {
			// only once call of this methods are valid in JavaFX
			//
			// set transparent level on highscore stage and scene		
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			this.scene.setFill(Color.TRANSPARENT);				
			primaryStage.setScene(this.scene);
			// set owner stage to highscore stage
			primaryStage.initOwner(ownerStage);
		}
		
		// show highscore stage
		primaryStage.show();
		
		// place the highscoreView always in the startView stage
		primaryStage.setX(DataBean.current_X_Pos_StartView + 70.0);
		primaryStage.setY(DataBean.current_Y_Pos_StartView + 50.0);
		
	}
	
	// follows the getter methods for this nodes
	//
	public Label getLblHighscoreValue() {
		return this.lblHighscoreValue;
	}
	
	public Button getBtnMove() {
		return this.btnMove;
	}
	
	public Button getBtnExit() {
		return this.btnExit;
	}

}
