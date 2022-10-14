/**
 * 
 */
package org.openjfx.SnakeGame.businessLogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.openjfx.SnakeGame.model.DataBean;

import javafx.application.Platform;

/**
 * Class to read and write specify game settings in xml file.
 * 
 * @author CSD
 *
 */
public class GameSettings extends GameScore {

	// implements valid constants for settings attributes
	public static final String ELEMENT_INFOBOX = "showInfoBox";
	
	public static final String ELEMENT_GAME_SPEED = "gameSpeed";
	
	public static final String ELEMENT_BACKGROUND = "gameBackground";
	
	public static final String ELEMENT_BORDER = "hasGameBorder";
	
	public static final String ELEMENT_MUSIC = "musicState";
	
	public static final String ELEMENT_VOLUME_LEVEL = "volumeLevel";
	
	// implements valid constants for startview window properties
	public static final String ELEMENT_STARTVIEW_HEIGHT = "startView_height";
	
	public static final String ELEMENT_STARTVIEW_WIDTH = "startView_width";
	
	public static final String ELEMENT_STARTVIEW_X_POS = "startView_xPos";
	
	public static final String ELEMENT_STARTVIEW_Y_POS = "startView_yPos";

	// final string for "settings" element
	public static final String settingsElement = "settings";
	
	// final string for the "startViewWindow" element
	public static final String startViewElement = "startView";

	// final path object for xml file -> from class GameScore
	private static final Path pathToXml = getAbsPathToXML();

	/**
	 * Method writes all neccessary elements in root element named "settings" in the xml file.
	 *
	 * In this element "settings" will saved all specify user settings with default values.
	 */
	public static void writeSettingsElements() {

		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		// append new elements in xml file
		if (doc != null) {

			// initialize the list for existing entries in xml file
			List<Element> children = doc.getRootElement().getChildren();

			if (!children.isEmpty()) {
				// create new element "settings"
				Element eSettings = new Element(settingsElement);
				// add new element "settings" as child element
				// to the root elemnt "player" in xml
				children.add(eSettings);
				
				// add all neccessary settings elements with default values
				eSettings.setAttribute(ELEMENT_INFOBOX, Boolean.toString(DataBean.DEFAULT_SETTING_INFOBOX));
				eSettings.setAttribute(ELEMENT_GAME_SPEED, Double.toString(DataBean.GAME_SPEED_MEDIUM));
				eSettings.setAttribute(ELEMENT_BACKGROUND, DataBean.GAME_BACKGROUND_NONE);
				eSettings.setAttribute(ELEMENT_BORDER, Boolean.toString(DataBean.DEFAULT_SETTING_FOR_BORDER));
				eSettings.setAttribute(ELEMENT_MUSIC, Boolean.toString(DataBean.DEFAULT_SETTING_MUSIC));
				eSettings.setAttribute(ELEMENT_VOLUME_LEVEL, Double.toString(DataBean.DEFAULT_VOLUME_LEVEL));
			}

			// write data in xml file
			GameSettings.writeDataInXmlFile(doc, pathToXml);
		}

	}
	
	/**
	 * Method to write new element for startView properties in xml file.
	 * It will used the default values from model.
	 */
	public static void writeElementForStartViewWindow() {
		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}
		
		// append new element for startview window in xml file
		if (doc != null) {

			// initialize the list for existing entries in xml file
			List<Element> children = doc.getRootElement().getChildren();

			if (!children.isEmpty()) {
				// create new element "settings"
				Element eStartView = new Element(startViewElement);
				// add new element "startView" as child element
				// to the root elemnt in xml
				children.add(eStartView);
				
				// add all neccessary settings elements with default values
				eStartView.setAttribute(ELEMENT_STARTVIEW_HEIGHT, Double.toString(DataBean.DEFAULT_HEIGHT_STARTVIEW));
				eStartView.setAttribute(ELEMENT_STARTVIEW_WIDTH, Double.toString(DataBean.DEFAULT_WIDTH_STARTVIEW));
				eStartView.setAttribute(ELEMENT_STARTVIEW_X_POS, Double.toString(DataBean.DEFAULT_X_POS_STARTVIEW));
				eStartView.setAttribute(ELEMENT_STARTVIEW_Y_POS, Double.toString(DataBean.DEFAULT_Y_POS_STARTVIEW));
			}

			// write data in xml file
			GameSettings.writeDataInXmlFile(doc, pathToXml);
		}
	}
	
	/**
	 * Method writes new values for the startView Properties in xml.
	 * 
	 * @param height	->	[double] new height of startView
	 * @param width
	 * @param xPos
	 * @param yPos
	 */
	public static void saveStartViewProps(double height, double width, double xPos, double yPos) {
		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		if (doc != null) {

			Element eStartView = doc.getRootElement().getChild(startViewElement);
			
			// if specify element "settings" was found
			if(eStartView != null) {
				
				// modify the startview attributes with given value
				eStartView.getAttribute(ELEMENT_STARTVIEW_HEIGHT).setValue(Double.toString(height));
				eStartView.getAttribute(ELEMENT_STARTVIEW_WIDTH).setValue(Double.toString(width));
				eStartView.getAttribute(ELEMENT_STARTVIEW_X_POS).setValue(Double.toString(xPos));
				eStartView.getAttribute(ELEMENT_STARTVIEW_Y_POS).setValue(Double.toString(yPos));
	
				// write data in xml file
				GameSettings.writeDataInXmlFile(doc, pathToXml);
			}
			
		}
	}
	
	/**
	 * Getter for the startView properties.
	 * 
	 * @return	[Map<String, String] properties of startView
	 */
	public static Map<String, String> getStartViewProps() {
		
		// Map for startView properties
		Map<String, String> mapUserSettings = new HashMap<String, String>();
		
		// document object
		Document doc = null;
		
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		if (doc != null) {

			Element eStartView = doc.getRootElement().getChild(startViewElement);
			
			// if specify element "settings" was found
			if(eStartView != null) {
								
				// get startView values from xml, if no startView values exists than set the default values
				mapUserSettings.put(ELEMENT_STARTVIEW_HEIGHT,
						eStartView.getAttributeValue(ELEMENT_STARTVIEW_HEIGHT) != null 
						? eStartView.getAttributeValue(ELEMENT_STARTVIEW_HEIGHT) : Double.toString(DataBean.DEFAULT_HEIGHT_STARTVIEW));
				
				mapUserSettings.put(ELEMENT_STARTVIEW_WIDTH,
						eStartView.getAttributeValue(ELEMENT_STARTVIEW_WIDTH) != null 
						? eStartView.getAttributeValue(ELEMENT_STARTVIEW_WIDTH) : Double.toString(DataBean.DEFAULT_WIDTH_STARTVIEW));
				
				mapUserSettings.put(ELEMENT_STARTVIEW_X_POS,
						eStartView.getAttributeValue(ELEMENT_STARTVIEW_X_POS) != null 
						? eStartView.getAttributeValue(ELEMENT_STARTVIEW_X_POS) : Double.toString(DataBean.DEFAULT_X_POS_STARTVIEW));
				
				mapUserSettings.put(ELEMENT_STARTVIEW_Y_POS,
						eStartView.getAttributeValue(ELEMENT_STARTVIEW_Y_POS) != null 
						? eStartView.getAttributeValue(ELEMENT_STARTVIEW_Y_POS) : Double.toString(DataBean.DEFAULT_Y_POS_STARTVIEW));
					
			}
			else {
				// if no element named "startView" was found
				// -> write new startView elements with default values in xml file 
				GameSettings.writeElementForStartViewWindow();
				
				// set default values in map
				mapUserSettings.put(ELEMENT_STARTVIEW_HEIGHT, Double.toString(DataBean.DEFAULT_HEIGHT_STARTVIEW));
				mapUserSettings.put(ELEMENT_STARTVIEW_WIDTH, Double.toString(DataBean.DEFAULT_WIDTH_STARTVIEW));
				mapUserSettings.put(ELEMENT_STARTVIEW_X_POS, Double.toString(DataBean.DEFAULT_X_POS_STARTVIEW));
				mapUserSettings.put(ELEMENT_STARTVIEW_Y_POS, Double.toString(DataBean.DEFAULT_Y_POS_STARTVIEW));
			}
		}
		
		return mapUserSettings;
	}
	
	/**
	 * Method writes data from the transferred Document in a given xml file.
	 * 
	 * @param doc		->	JDom Document object
	 * @param xmlToXml	->	Path object to the xml file
	 * 
	 */
	private static void writeDataInXmlFile(Document doc, Path xmlToXml) {
		if(doc != null && xmlToXml != null) {
			// Only write data if xml file really exists
			if (pathToXml.toFile().exists()) {

				// write data in xml file with stream object
				try (FileOutputStream fos = new FileOutputStream(pathToXml.toFile())) {

					XMLOutputter outPutter = new XMLOutputter(Format.getPrettyFormat());
					outPutter.output(doc, fos);

				} catch (IOException ex) {
					// failed to write in file
					String errorMsg = "Failed to write element \"settings\" in xml file.";
					ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent", errorMsg,
							new ImageIcon("./target/classes/snake_img_dock.png"));
					Platform.exit();
					System.exit(1);

				} catch (NullPointerException ex) {
					// document is null
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				}
			}
		}
		
	}
	
	/**
	 * Method modify the value for the attribute "showInfoBox" in xml file.
	 * 
	 * @param isInfoBoxShowing	->	boolean flag for showing info box
	 */
	private static void saveValueForShowInfoBox(boolean isInfoBoxShowing) {
		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		// overwrite existing value of attribute "showInfoBox"
		if (doc != null) {

			Element eSettings = doc.getRootElement().getChild(settingsElement);
			
			// if specify element "settings" was found
			if(eSettings != null) {
				
				// modify the attribute with given boolean value
				eSettings.getAttribute(ELEMENT_INFOBOX).setValue(Boolean.toString(isInfoBoxShowing));
	
				// write data in xml file
				GameSettings.writeDataInXmlFile(doc, pathToXml);
			}
			
		}
	}
	
	/**
	 * Method save all neccessary game settings in the xml file.
	 * 
	 * @param isInfoBoxShowing	->	[boolean] if info box is shown at game start
	 * @param currentGameSpeed
	 * @param currentGameBackground
	 * @param isGameBoder
	 */
	public static void saveGameSettingsInXML(boolean isInfoBoxShowing, double currentGameSpeed,
			String currentGameBackground, boolean isGameBoder, boolean isMusicPlaying, double volumeLevel) {
		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		// overwrite existing value of attribute "showInfoBox"
		if (doc != null) {

			Element eSettings = doc.getRootElement().getChild(settingsElement);
			
			// if specify element "settings" was found
			if(eSettings != null) {
				
				// modify the seetings attributes with given values
				eSettings.getAttribute(ELEMENT_INFOBOX).setValue(Boolean.toString(isInfoBoxShowing));
				eSettings.getAttribute(ELEMENT_GAME_SPEED).setValue(Double.toString(currentGameSpeed));
				eSettings.getAttribute(ELEMENT_BACKGROUND).setValue(currentGameBackground);
				eSettings.getAttribute(ELEMENT_BORDER).setValue(Boolean.toString(isGameBoder));
				eSettings.getAttribute(ELEMENT_MUSIC).setValue(Boolean.toString(isMusicPlaying));
				eSettings.getAttribute(ELEMENT_VOLUME_LEVEL).setValue(Double.toString(volumeLevel));
	
				// write data in xml file
				GameSettings.writeDataInXmlFile(doc, pathToXml);
			}
			
		}
	}
	
	/**
	 * Method to read the boolean value for showInfoBox flag from xml file.
	 * If fails to read the boolean value -> this method sets the value
	 * automatically on true!!! 
	 * @return
	 */
	public static boolean getValueForShowInfoBox() {
		// document object
		Document doc = null;
		
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		// overwrite existing value of attribute "showInfoBox"
		if (doc != null) {

			Element eSettings = doc.getRootElement().getChild(settingsElement);
			
			// if specify element "settings" was found
			if(eSettings != null) {
								
				try {
					// get the boolean value of the attribute
					return eSettings.getAttribute(ELEMENT_INFOBOX).getBooleanValue();
					
				} catch (DataConversionException e) {
					// if failed to convert value in a boolean 
					// -> set the value on default boolean value (true)
					GameSettings.saveValueForShowInfoBox(true);
					return true;
				}	
			}
			// if no element named "settings" was found
			// -> write new element and attribute with value "true" in xml file 
			GameSettings.writeSettingsElements();
		}
		
		return true;
	}
	
	/**
	 * Getter for the user game settings from xml file.
	 * 
	 * @return	-> [Map>String, String] with the game settings
	 */
	public static Map<String, String> getGameSettings() {
		
		// Map for user settings
		Map<String, String> mapUserSettings = new HashMap<String, String>();
		
		// document object
		Document doc = null;
		
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(pathToXml);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		// overwrite existing value of attribute "showInfoBox"
		if (doc != null) {

			Element eSettings = doc.getRootElement().getChild(settingsElement);
			
			// if specify element "settings" was found
			if(eSettings != null) {
								
				// get settings values from xml, if no settings value exists than set the default value
				mapUserSettings.put(ELEMENT_INFOBOX,
						eSettings.getAttributeValue(ELEMENT_INFOBOX) != null 
						? eSettings.getAttributeValue(ELEMENT_INFOBOX) : Boolean.toString(DataBean.DEFAULT_SETTING_INFOBOX));
				
				mapUserSettings.put(ELEMENT_GAME_SPEED,
						eSettings.getAttributeValue(ELEMENT_GAME_SPEED) != null
						? eSettings.getAttributeValue(ELEMENT_GAME_SPEED) : Double.toString(DataBean.GAME_SPEED_MEDIUM));
				
				mapUserSettings.put(ELEMENT_BACKGROUND,
						eSettings.getAttributeValue(ELEMENT_BACKGROUND) != null
						? eSettings.getAttributeValue(ELEMENT_BACKGROUND) : DataBean.GAME_BACKGROUND_NONE);
				
				mapUserSettings.put(ELEMENT_BORDER,
						eSettings.getAttributeValue(ELEMENT_BORDER) != null
						? eSettings.getAttributeValue(ELEMENT_BORDER) : Boolean.toString(DataBean.DEFAULT_SETTING_FOR_BORDER));
				
				mapUserSettings.put(ELEMENT_MUSIC,
						eSettings.getAttributeValue(ELEMENT_MUSIC) != null
						? eSettings.getAttributeValue(ELEMENT_MUSIC) : Boolean.toString(DataBean.DEFAULT_SETTING_MUSIC));
				
				mapUserSettings.put(ELEMENT_VOLUME_LEVEL,
						eSettings.getAttributeValue(ELEMENT_VOLUME_LEVEL) != null
						? eSettings.getAttributeValue(ELEMENT_VOLUME_LEVEL) : Double.toString(DataBean.DEFAULT_VOLUME_LEVEL));
					
			}
			else {
				// if no element named "settings" was found
				// -> write new settings elements with default values in xml file 
				GameSettings.writeSettingsElements();
				
				// set default values in map
				mapUserSettings.put(ELEMENT_INFOBOX, Boolean.toString(DataBean.DEFAULT_SETTING_INFOBOX));
				
				mapUserSettings.put(ELEMENT_GAME_SPEED, Double.toString(DataBean.GAME_SPEED_MEDIUM));
				
				mapUserSettings.put(ELEMENT_BACKGROUND, DataBean.GAME_BACKGROUND_NONE);
				
				mapUserSettings.put(ELEMENT_BORDER, Boolean.toString(DataBean.DEFAULT_SETTING_FOR_BORDER));
				
				mapUserSettings.put(ELEMENT_MUSIC, Boolean.toString(DataBean.DEFAULT_SETTING_MUSIC));
				
				mapUserSettings.put(ELEMENT_VOLUME_LEVEL, Double.toString(DataBean.DEFAULT_VOLUME_LEVEL));
			}
		}
		
		return mapUserSettings;
	}

	/**
	 * Method parse an transfered xml file at a specify attribute.
	 * 
	 * @param xmlFile -> xml file as path object
	 * @param attr    -> searched attribute as String
	 * @return -> returned null if no attr with this name was found, otherwise the
	 *         value of this attr as String
	 */
	public static String readSpecifySettings(Path xmlFile, String attr) {
		if (xmlFile != null && attr != null) {

			if (xmlFile.toFile().exists()) {

				// change in path object in file
				File file = xmlFile.toFile();
				// SAXBuilder to parse xml file
				SAXBuilder builder = new SAXBuilder();

				Document doc = null;

				try {
					// create new document object from parsing xml file
					doc = builder.build(file);
					// get valid root element from xml
					Element root = doc.getRootElement();
					// extract all valid child elements
					List<Element> childElements = root.getChildren();

					// iterate over all valid child elements
					if (!childElements.isEmpty()) {
						for (Element child : childElements) {
							// search for transfered attribute
							if (child.getName().equals(attr)) {
								// matching -> extract value from attr
								String attrValue = child.getAttributeValue("value");
								if (attrValue != null) {
									return attrValue;
								}
							}
						}
					}

				} catch (JDOMException | IllegalStateException | IOException ex) {
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				}
			}
		}
		// if no specify attribute was found -> return null
		return null;
	}
}
