package org.openjfx.SnakeGame.Controller;

import java.util.Map;

import org.openjfx.SnakeGame.View.SettingsView;
import org.openjfx.SnakeGame.businessLogic.GameSettings;
import org.openjfx.SnakeGame.model.DataBean;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 * Klasse zum steuern und verwalten der User Interaktionen mit der Settings View
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
public class SettingsViewController {
	// Instanzvariablen
	//
	// Daten (Model)
	private DataBean dataBean;
	// settingsView (View die bei den Einstellungen angezeigt werden soll)
	private SettingsView settingsView;

	/**
	 * Konstruktor sichert das übergebene Model und initialisiert die View
	 * 
	 * @param dataBean
	 */
	public SettingsViewController(DataBean dataBean) {
		
		// Model sichern
		this.dataBean = dataBean;
		// Settings View initialisieren
		this.settingsView = new SettingsView();

		// den Radio Buttons die Werte für die jeweilige Spiel-Gesachwindigkeit zuweisen
		this.settingsView.getRadioBtnLow().setUserData(DataBean.GAME_SPEED_LOW);
		this.settingsView.getRadioBtnMedium().setUserData(DataBean.GAME_SPEED_MEDIUM);
		this.settingsView.getRadioBtnHigh().setUserData(DataBean.GAME_SPEED_HIGH);

		// den Radio Buttons für den Game Background die jeweilgen Werte zuweisen
		this.settingsView.getRadioBtnBackGrNone().setUserData(DataBean.GAME_BACKGROUND_NONE);
		this.settingsView.getRadioBtnBackGrGreen().setUserData(DataBean.GAME_BACKGROUND_GREEN);
		this.settingsView.getRadioBtnBackGrGrey().setUserData(DataBean.GAME_BACKGROUND_GREY);
		
		// init the user game settings
		this.initGameSettings();
		
		// das label für die musik lautstärke formatieren das keine komma stelle angezeigt wird
		String volumeValuePercent = String.format(String.format("%.0f", this.dataBean.getValueOfVolume()));
		// das prozent zeichen setzen
		this.settingsView.getLblVolumeValue().setText(volumeValuePercent + " %");
				
		// Eventhandler registrieren/zuweisen
		//
		// somit überwacht / registriert der View Controller die Interaktionen des User
		// mit der View
		this.settingsView.getBtnApply().setOnMouseClicked(new SettingsViewEventhandlerBtnApply());
		this.settingsView.getToggleGroupGameSpeed().selectedToggleProperty()
				.addListener(new SettingsViewEventhandlerRadioBtn());
		this.settingsView.getCheckBoxBorder().selectedProperty().addListener(new SettingsViewEventhandlerCheckBox());
		this.settingsView.getToggleGroupMusicBtn().selectedToggleProperty()
				.addListener(new SettingsViewEventhandlerToggleBtnMusic());
		this.settingsView.getToggleGroupBackground().selectedToggleProperty()
				.addListener(new SettingsViewEventhandlerRadioBtnGameBackGr());
		this.settingsView.getVolumeSlider().valueProperty().addListener(new SettingsViewEventhandlerVolumeSlider());
	}
	
	/**
	 * Method to initialized the read game settings from xml to the settings view.
	 */
	public void initGameSettings() {
		
		// iterate over map with settings
		for(Map.Entry<String, String> entry : this.dataBean.getSpecifiedGameSettings().entrySet()) {
			
			// set value in settinsView for checkBox Border
			if(entry.getKey().equals(GameSettings.ELEMENT_BORDER)) {
				// set value for border in model
				this.dataBean.setBorderState(Boolean.parseBoolean(entry.getValue()));
				// select checkbox in settings view
				this.settingsView.getCheckBoxBorder().setSelected(Boolean.parseBoolean(entry.getValue()));
			}
			
			// set value in settingsView for the game speed
			if(entry.getKey().equals(GameSettings.ELEMENT_GAME_SPEED)) {
				
				// check which game speed is set and select the responsible radio button
				if(Double.parseDouble(entry.getValue()) == DataBean.GAME_SPEED_LOW) {
					this.dataBean.setGameSpeed(DataBean.GAME_SPEED_LOW);
					this.settingsView.getRadioBtnLow().setSelected(true);
				}
				else if(Double.parseDouble(entry.getValue()) == DataBean.GAME_SPEED_MEDIUM) {
					this.dataBean.setGameSpeed(DataBean.GAME_SPEED_MEDIUM);
					this.settingsView.getRadioBtnMedium().setSelected(true);
				}
				else if(Double.parseDouble(entry.getValue()) == DataBean.GAME_SPEED_HIGH) {
					this.dataBean.setGameSpeed(DataBean.GAME_SPEED_HIGH);
					this.settingsView.getRadioBtnHigh().setSelected(true);
				}
			}
			
			// set value in settingsView for the game background
			if(entry.getKey().equals(GameSettings.ELEMENT_BACKGROUND)) {
				
				if(entry.getValue().equals(DataBean.GAME_BACKGROUND_NONE)) {
					this.dataBean.setGameBackground(DataBean.GAME_BACKGROUND_NONE);
					this.settingsView.getRadioBtnBackGrNone().setSelected(true);
				}
				else if(entry.getValue().equals(DataBean.GAME_BACKGROUND_GREEN)) {
					this.dataBean.setGameBackground(DataBean.GAME_BACKGROUND_GREEN);
					this.settingsView.getRadioBtnBackGrGreen().setSelected(true);
				}
				else if(entry.getValue().equals(DataBean.GAME_BACKGROUND_GREY)) {
					this.dataBean.setGameBackground(DataBean.GAME_BACKGROUND_GREY);
					this.settingsView.getRadioBtnBackGrGrey().setSelected(true);
				}
			}
			
			// set value in settingsView for the music state toogle btn
			if(entry.getKey().equals(GameSettings.ELEMENT_MUSIC)) {
				// set value for music state in model
				this.dataBean.setMusicPlayState(Boolean.parseBoolean(entry.getValue()));
				// set state of voluem slider dependent on music play state (enable or disable)
				this.settingsView.getVolumeSlider().setDisable(! Boolean.parseBoolean(entry.getValue()));
				// select the responsible toggle button dependent on the music playing state from model
				if(this.dataBean.isMusicPlaying()) {
					this.settingsView.getToggleBtnON().setSelected(true);
					// dem media player die zuletzt eingestellte Lautstärke korrekt übergeben
					this.dataBean.getMediaPlayerGameMusic().setVolume(this.dataBean.getValueOfVolume() / 100);		
					// play game music
//					this.dataBean.getMediaPlayerGameMusic().play();
				}
				else {
					this.settingsView.getToggleBtnOFF().setSelected(true);
					// paused game music
					this.dataBean.getMediaPlayerGameMusic().pause();
				}
			}
			
			// set the volume level for music
			if(entry.getKey().equals(GameSettings.ELEMENT_VOLUME_LEVEL)) {
				this.dataBean.setValueOfVolume(Double.parseDouble(entry.getValue()));		
				// set the volume slider on value from model
				this.settingsView.getVolumeSlider().setValue(this.dataBean.getValueOfVolume());
			}
		}
		
	}

	/**
	 * Methode erstellt eine separate Stage für die Einstellungen so das die
	 * Settings View angezeigt werden kann
	 */
	public void show() {
		/*
		 * übergeben der settingsStage an die Settings View
		 */
		this.settingsView.show(this.dataBean.getSettingsStage());
	}
	
	/*
	 * Hilfmethoden um über den Controller auf den Slider und das Label für die
	 * Musik Lautstärke zuzugreifen wie auch auf die ToggleGroup für GameSpeed und Background
	 */
	public Slider getVolumeSliderFromSettingsView() {
		return this.settingsView.getVolumeSlider();
	}

	public Label getVolumeValueLabelFromSettingsView() {
		return this.settingsView.getLblVolumeValue();
	}
	
	public ToggleGroup getToggleGroupGameSpeedFromSettingsView() {
		return this.settingsView.getToggleGroupGameSpeed();
	}
	
	public ToggleGroup getToggleGroupBackgroungFromSettingsView() {
		return this.settingsView.getToggleGroupBackground();
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Eventhandling
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Eventhandler -> für den Button-Apply -> dieser reagiert sobald mit der Maus
	 * auf diesen geklickt wird
	 * 
	 * @author Christian
	 */
	class SettingsViewEventhandlerBtnApply implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			// Stage der Settings View schließen
			dataBean.getSettingsStage().close();
						
			// nur wenn schon eine Instanz des GameViewControllers besteht -> sprich schon gespielt
			// wurde und aus dem Spiel heraus in die Einstellungen wechselt wird -> dann Spiel fortsetzen
			if(dataBean.getGameController() instanceof GameViewController) {
				dataBean.getGameController().resumeGame();
				dataBean.getGameController().show();
			}
		}
	}

	/**
	 * Eventhandler -> für die ToggleGroup der RadioButtons - Speed -> dieser
	 * reagiert sobald mit der Maus auf einen der RadioButtons geklickt wird, sprich
	 * eine Veränderung eintritt
	 * 
	 * @author Christian
	 */
	class SettingsViewEventhandlerRadioBtn implements ChangeListener<Toggle> {

		@Override
		public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

			// prüfen ob auch wirklich ein Radio Button ausgewählt wurde
			if (settingsView.getToggleGroupGameSpeed().getSelectedToggle() != null) {
				// je nach ausgewähltem RadioBtn die Geschwindigkeit im Model anpassen
				// es wird in einen Double gecastet
				dataBean.setGameSpeed((Double) settingsView.getToggleGroupGameSpeed().getSelectedToggle().getUserData());
			}
		}
	}

	/**
	 * Eventhandler -> für die ToggleGroup der RadioButtons - Game Background ->
	 * dieser reagiert sobald mit der Maus auf einen der RadioButtons geklickt wird,
	 * sprich eine Veränderung eintritt
	 * 
	 * @author Christian
	 */
	class SettingsViewEventhandlerRadioBtnGameBackGr implements ChangeListener<Toggle> {

		@Override
		public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
			// prüfen ob auch wirklich ein Radio Button ausgewählt wurde
			if (settingsView.getToggleGroupBackground().getSelectedToggle() != null) {
				// je nach ausgewählten Radio Button den Game Background im Model anpassen
				// es wird in einen String gecastet
				dataBean.setGameBackground(
						(String) settingsView.getToggleGroupBackground().getSelectedToggle().getUserData());
			}
		}

	}

	/**
	 * Eventhandler -> für die ToggleGroup der RadioButtons -> dieser reagiert
	 * sobald mit der Maus auf einen der RadioButtons geklickt wird, sprich eine
	 * Veränderung eintritt
	 * 
	 * @author Christian
	 */
	class SettingsViewEventhandlerToggleBtnMusic implements ChangeListener<Toggle> {

		@Override
		public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
			// prüfen ob auch wirklich ein Musik Toogle Button ausgewählt wurde
			if (settingsView.getToggleGroupMusicBtn().getSelectedToggle() != null) {
				// je nach ausgewählten Toggle Button die Musik an oder abschalten
				if (settingsView.getToggleBtnON().isSelected()) {
					// Status Musik spielen im Model anpassen
					dataBean.setMusicPlayState(true);
					// Volume Slider aktivieren
					settingsView.getVolumeSlider().setDisable(false);
					// den Standard Wert für die Spiel Musik Lautstärke auf 15% setzen
					//dataBean.setValueOfVolume(DataBean.DEFAULT_VOLUME_LEVEL);
					dataBean.setValueOfVolume(dataBean.getValueOfVolume());
					// dem Slider den Wert der Lautstärke übergeben
					settingsView.getVolumeSlider().setValue(dataBean.getValueOfVolume());
					// dem MediaPlayer(Spielmusik) die Musik Lautstärke übergeben
					// 15.0 / 100 = 0.15
					dataBean.getMediaPlayerGameMusic().setVolume(dataBean.getValueOfVolume() / 100);					
					// Spielmusik abspielen
//					dataBean.getMediaPlayerGameMusic().play();
					// Status Musik spielen im Model anpassen
					dataBean.setMusicPlayState(true);
				}

				else if (settingsView.getToggleBtnOFF().isSelected()) {
					// Slider Wert auf eingestellte Lautstärke setzen
					settingsView.getVolumeSlider().setValue(dataBean.getValueOfVolume());
					// Volume Slider deaktivieren
					settingsView.getVolumeSlider().setDisable(true);
					// Musik pausieren
//					dataBean.getMediaPlayerGameMusic().pause();
					// Status Musik spielen im Model anpassen
					dataBean.setMusicPlayState(false);
				}
			}
		}

	}

	/**
	 * Eventhandler -> für den Volume Slider in den Settings -> dieser reagiert
	 * sobald mit der Maus der Slider bewegt wird. Es wäre auch möglich gewesen
	 * einen ChangeListener zu verwenden, der InvalidationListener ist aber von der
	 * Performance besser da er nicht wie ein changeListener die ganze laufzeit über
	 * den Slider abhört sondern eben nur wenn dieser mit der Maus bewegt wird!!!
	 * 
	 * @author Christian
	 */
	class SettingsViewEventhandlerVolumeSlider implements InvalidationListener {

		@Override
		public void invalidated(Observable observable) {
			// Mediaplayer volume mit dem slider anpassen
			dataBean.getMediaPlayerGameMusic().setVolume(settingsView.getVolumeSlider().getValue() / 100);
			// Label für die Lautstärke neben dem Slider anpassen
			String volumeValuePercent = String.format(String.format("%.0f", settingsView.getVolumeSlider().getValue()));
			settingsView.getLblVolumeValue().setText(volumeValuePercent + " %");

			// denn Wert der Lautstärke im Model anpassen -> damit der Wert von dem Slider
			// in der GameView
			// übernommenwerden kann
			dataBean.setValueOfVolume(settingsView.getVolumeSlider().getValue());
		}

	}

	/**
	 * Eventhandler -> für die CheckBox Border -> dieser reagiert sobald mit der
	 * Maus die CheckBox ausgewählt oder abgewählt wird
	 * 
	 * @author Christian
	 */
	class SettingsViewEventhandlerCheckBox implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			// if checkbox "border" selected -> change value in model
			if (settingsView.getCheckBoxBorder().isSelected()) {
				dataBean.setBorderState(true);
			} else {
				dataBean.setBorderState(false);
			}

		}

	}
}
