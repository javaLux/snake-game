package org.openjfx.SnakeGame.businessLogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.ImageIcon;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javafx.application.Platform;

/**
 * Klasse zum erzeugen des Pfades für die XML Datei in dem der HighScore
 * gespeichert wird, und zum erzeugen des Verzeichnisses für die XML Datei.
 * Weiterhin wird dur
 * 
 * @author christianschmidt
 *
 */
public class GameScore {
	
	// xml file name
	private static final String xmlFileName = "_gameProperties.xml";
	// log file name
	private static final String logFileName = "_logFile.log";
	
	// xml root element
	public static final String XML_ROOT_ELEMENT = "properties";
	
	// class variables (static)
	private static final String USER_DIR = FileSystems.getDefault().getPath(UserDir.getUserDir().toString()).toString();
		
	/**
	 * Method build the path to the game dir.
	 * 
	 * @return	->	Path object [contains the game directory]
	 */
	public static Path getGameDirPath() {
		return FileSystems.getDefault().getPath(USER_DIR, "Snake-Game");
	}
	
	/**
	 * Method builds and return the path to the logfile.
	 * 
	 * @return	->	Path object [contains path to logFile]
	 */
	public static Path getPathToLogFile() {
		Path pathToLogFile = FileSystems.getDefault().getPath(getGameDirPath().toString(), logFileName);
		return pathToLogFile;
	}

	/**
	 * Methode creates the absolut path, for the current OS,
	 * where XML file to be saved an disk
	 * 
	 * @return	->	absolute path to xml file
	 * @throws		InvalidPathException
	 */
	public static Path getAbsPathToXML() throws InvalidPathException {
		
		Path pathToXml = FileSystems.getDefault().getPath(getGameDirPath().toString(), xmlFileName);
		return pathToXml;
	}

	/**
	 * Method creates new game dir "Snake-Game" and xml file for the highscore in
	 * user dir.
	 * 
	 * @param pathToXML	->	Path to xml file
	 * @return			-> 	true if game dir and xml file created sucessfully, otherwise false
	 * @throws 				SecurityException
	 * @throws 				IOException
	 */
	public static boolean createNewXML_File(Path pathToXML) throws SecurityException, IOException {
		// lokale Variablen für den Status der Datei bzw. Verzeichnis Erstellung
		boolean gameDirCreated = false;
		boolean xmlFileCreated = false;

		if (pathToXML != null) {
			File file = new File(pathToXML.toString());

			// Verzeichnis(Ordner) Struktur bis zur XML-Datei speichern
			String dirPath = file.getParent();

			// falls eine Verzeichnis Struktur ermittelt werden konnte und der Game-Folder
			// noch nicht existiert -> neuen Game-Folder erzeugen
			if ((dirPath != null) && !(new File(dirPath).isDirectory())) {
				gameDirCreated = new File(dirPath).mkdir();
			}

			// falls ein neuer Game-Folder erfolgreich erstellt wurde ODER
			// dieser schon vorhanden ist -> XML erstellen
			if ((gameDirCreated) || new File(dirPath).isDirectory()) {
				xmlFileCreated = new File(pathToXML.toString()).createNewFile();
			}

			// wenn XML File erfolgreich erstellt wurde -> true zurückgeben
			if (xmlFileCreated) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Methode checks whether the transfered path exists
	 * 
	 * @param pathToXML	-> 	Path object das geprüft werden soll
	 * @return 			-> 	return true wenn es existiert
	 */
	public static boolean existsXML(Path pathToXML) {
		File xml = null;

		if (pathToXML != null) {
			xml = new File(pathToXML.toString());
			// prüfen ob Pfad existiert
			if (xml.exists()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Methode get the document object for an xml file
	 * 
	 * @param xmlFile	->	Path to xml file
	 * @return 			-> 	return null if no document within xml file otherwise the document
	 *         				object
	 * @throws 				JDOMException
	 * @throws 				IOException
	 */
	public static Document getDocFromXML(Path xmlFile) throws JDOMException, IOException {

		if (xmlFile != null) {
			if (xmlFile.toFile().exists() && xmlFile.toFile().length() > 0) {

				SAXBuilder builder = new SAXBuilder();

				Document doc = builder.build(xmlFile.toFile());
				return doc;

			}
		}

		return null;

	}

	/**
	 * Method checks whether the transfered xml file an valid
	 * xml file within an JDom root element
	 * 
	 * @param xmlFile	->	Path to xml file
	 * @return
	 * @throws 				JDOMException
	 * @throws 				IOException
	 */
	private static boolean isValidXmlFile(Path xmlFile) {

		if (xmlFile != null) {
			if (xmlFile.toFile().exists() && xmlFile.toFile().length() > 0) {

				SAXBuilder builder = new SAXBuilder();

				try {

					Document doc = builder.build(xmlFile.toFile());

					Element rootElement = doc.getRootElement();

					if (rootElement != null) {
						// if an root element was found -> return true
						return true;
					}

					// if no root element within xml file -> thrown exception and return false
				} catch (IllegalStateException | JDOMException ex) {
					return false;

				} catch (IOException ex) {
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				}
			}
		}
		return false;
	}

	// **** XML File READER method ****
	//
	/**
	 * Method returns the value of the element that was given as parameter, when
	 * this element exists in the xml file.
	 * 
	 * @param xmlFile     	-> 	XML-File as Path object
	 * @param elementName 	-> 	Element where searching for
	 * @return 				-> 	return null if no element exists, otherwise the value of the
	 *         					element
	 * 
	 */
	public static String getHighScoreFromXML(Path xmlFile, String elementName) {
		// nur ausführen wenn alle Parameter korrekt übergeben wurden
		if (xmlFile != null && elementName != null) {
			if (xmlFile.toFile().exists()) {

				File file = xmlFile.toFile();
				SAXBuilder builder = new SAXBuilder();

				// doc object aus xml erstellen
				Document doc = null;

				try {
					// checks if the xml file is valid (within a root element)
					if (!GameScore.isValidXmlFile(xmlFile)) {
						// if xml file not valid -> info to user
						ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
								"Failed to read highscore from xml file.\n\nHighscore reset to zero.",
								new ImageIcon("./target/classes/snake_img_dock.png"));
						
						// overwrite invalid xml file -> set highscore to 0
						GameScore.createValidXML(xmlFile);
					}

					doc = builder.build(file);
					Element root = doc.getRootElement();

					// alle child elemente extrahieren
					List<Element> listOfChilds = root.getChildren();

					// wenn child elemente gefunden wurden
					if (!listOfChilds.isEmpty()) {
						// Liste durchlaufen
						for (Element e : listOfChilds) {
							// nach übergebenen Element suchen
							if (e.getName().equals(elementName)) {
								// wert extrahieren
								String attrValue = e.getAttributeValue("value");
								// wenn ein Wert ermittelt werden konnte, diesen zurückgeben
								if (attrValue != null) {
									return attrValue;
								}
							}
						}
					}

				} catch (JDOMException | IllegalStateException ex) {
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);

				} catch (IOException ex) {
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				}

			}
		}

		return null;
	}

	// **** XML File WRITER method ****
	//
	/**
	 * Method generate a new root element for the xml file
	 * 
	 * @param rootElement	->	Name for the new root element
	 * @return 				-> 	returns the JDOM document object
	 * 
	 */
	private static Document createNewDoc(String rootElement) {

		if (rootElement != null && rootElement.length() > 1) {
			Document doc = new Document();
			Element root = new Element(rootElement);
			doc.setRootElement(root);
			return doc;
		}

		return null;

	}

	/**
	 * Method creates a valid xml file with correct root and child elements
	 * 
	 * @param xmlFile	->	Path to the xml file
	 */
	public static void createValidXML(Path xmlFile) {
		if (xmlFile != null) {
			if (xmlFile.toFile().exists()) {
				// add new jdom document with root element to xml
				Document newDoc = GameScore.createNewDoc(XML_ROOT_ELEMENT);
				// Integer object for the score
				Integer score = 0;
				// create new child element "highscore"
				Element e1 = new Element("highscore");
				e1.setAttribute("value", score.toString());
				if (newDoc != null) {
					// add child element to root element
					newDoc.getRootElement().addContent(e1);
				}

				// try to write elements in xml file
				try (FileOutputStream fos = new FileOutputStream(xmlFile.toFile())) {

					XMLOutputter outPutter = new XMLOutputter(Format.getPrettyFormat());
					outPutter.output(newDoc, fos);

				} catch (IOException ex) {
					// failed to write in file
					String errorMsg = "Failed to create a new valid xml file.";
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
	 * Method write the transferred data in the xml file
	 * 
	 * @param rootElement  -> 	Name of the root element from xml file
	 * @param childElement -> 	Name of the child element that add to the root element
	 * @param score        -> 	Value of points to write in xml file
	 * @param appending    -> 	flag to determine whether the entries should be
	 *                     		overwritten or appended to the existing elements in the
	 *                     		xml file
	 */
	public static void writeToXML(Path xmlFile, String rootElement, String childElement, Integer score,
			boolean appending) {

		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = GameScore.getDocFromXML(xmlFile);

		} catch (JDOMException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);

		} catch (IOException ex) {

			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			Platform.exit();
			System.exit(1);
		}

		// Liste für die bestehenden Einträge in der XML-Datei
		List<Element> children = null;

		// falls Einträge angehangen werden sollen
		if (doc != null && appending) {
			// bestehende Einträge holen
			children = doc.getRootElement().getChildren();

			if (!children.isEmpty()) {
				// neuen Eintrag erzeugen
				Element e1 = new Element(childElement);
				e1.setAttribute("value", score.toString());
				// den neuen Eintrag dem Wurzel-Element als Kind-Element
				// hinzufügen
				children.add(e1);
			}
		}
		// alle bestehenden Einträge überschreiben
		else {
			doc = createNewDoc(rootElement);
			Element e1 = new Element(childElement);
			e1.setAttribute("value", score.toString());
			if (doc != null) {
				doc.getRootElement().addContent(e1);
			}
		}

		if (xmlFile.toFile().exists()) {

			/*
			 * Schreiben der Daten in die XML-Datei
			 * 
			 * Wird der Stream nicht wie hier mit einem try-with-resources-Block erzeugt ->
			 * also den Stream in die () nach dem try Keyword schreiben, so müssen dessen
			 * Ressourcen abschließend üblicherweise mit flush() freigegeben und die Datei
			 * mit close() geschlossen werden. Die Aufgabe der Ressourcen-Freigabe wird bei
			 * Verwendung eines XMLOutputter durch dessen Methode output übernommen. Sie
			 * schließt jedoch nicht die Datei!‚
			 */
			try (FileOutputStream fos = new FileOutputStream(xmlFile.toFile())) {

				XMLOutputter outPutter = new XMLOutputter(Format.getPrettyFormat());
				outPutter.output(doc, fos);

			} catch (IOException ex) {
				// failed to write in file
				String errorMsg = "Failed to write highscore in xml file.";
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

	// ********************************************************
	// Methods for read and write highscore at game play
	// ********************************************************

	/**
	 * Method read in game play the current value of highscore from xml file.
	 * 
	 * @param xmlFile	->	Path to xml file
	 * @return 			-> 	return value of highscore as int
	 */
	public static int readHighScore(Path xmlFile) {
		String stringHighscoreValue = "";
		int highscore = 0;

		if (xmlFile != null) {

			// if xml file already exists -> check if they a valid xml file (JDom)
			if (GameScore.existsXML(xmlFile)) {

				stringHighscoreValue = GameScore.getHighScoreFromXML(xmlFile, "highscore");

				// and try to parse string value to an Integer object
				try {
					highscore = Integer.parseInt(stringHighscoreValue);
					return highscore;

				} catch (NumberFormatException ex) {
					// if failed to parse string value from xml to an integer
					// info at user and run game with highscore value of zero
					ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
							"Failed to read highscore from xml file.\n\nHighscore reset to zero.",
							new ImageIcon("./target/classes/snake_img_dock.png"));
					// if no integer has parsed from xml than create new valid xml with highscore of
					// zero
					GameScore.createValidXML(xmlFile);
				}

				// if no xml file exists, create new xml file with root and child elements
				// (JDOM)
			} else {

				try {
					if (GameScore.createNewXML_File(xmlFile)) {
						// create new root element with highscore 0 for new xml file
						GameScore.writeToXML(xmlFile, XML_ROOT_ELEMENT, "highscore", 0, false);
						// check if xml has an highscore, if this true -> read the value and try to
						// parse it to an Integer object
						stringHighscoreValue = JDomReader.getHighScoreFromXML(xmlFile, "highscore");
						// and try to parse string value to an Integer object
						highscore = Integer.parseInt(stringHighscoreValue);
						return highscore;

					}

				} catch (SecurityException ex) {
					// falls durch AntiViren Programm der Zugriff verweigert wurde
					// Info an user geben
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);

				} catch (IOException ex) {
					// falls ein Fehler beim erstellen der Datei aufgetreten ist
					// Info an User ausgeben
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
					// if failed to parse string value from xml to an integer
					// info at user and run game with highscore value of zero
					ErrorBoxSwing.showErrorMessage("Snake - Game: An error occurent",
							"Failed to read highscore from xml file.\n\nHighscore reset to zero.",
							new ImageIcon("./target/classes/snake_img_dock.png"));
					// if no integer has parsed from xml than create new valid xml with highscore of
					// zero
					GameScore.createValidXML(xmlFile);
				}
			}
		}
		return highscore;
	}

	/**
	 * Method saved the new highscore in xml file
	 * 
	 * @param xmlFile	-> 	Path to xml file
	 */
	public static void saveHighScoreInXML(Path xmlFile, int points) {
		if (xmlFile != null) {
			if (!xmlFile.toFile().exists()) {
				// if xml file doesn't exists -> create new valid xml file
				// and write new highscore in this file
				try {
					if (GameScore.createNewXML_File(xmlFile)) {
						// a new valid xml file will be created, to preduce the procedure is secure
						GameScore.createValidXML(xmlFile);
						// save new highscore
						GameScore.writeToXML(xmlFile, XML_ROOT_ELEMENT, "highscore", points, false);
						
					}
				} catch (SecurityException ex) {
					// Exception Handling if is not allowed the create new file in user dir
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				} catch (IOException ex) {
					// Exception Handling for Input / Output Error
					ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
					Platform.exit();
					System.exit(1);
				}
			} else {
				// check if xml file valid
				if (!GameScore.isValidXmlFile(xmlFile)) {
					GameScore.createValidXML(xmlFile);
				}
				// save new highscore
				GameScore.writeToXML(xmlFile, XML_ROOT_ELEMENT, "highscore", points, false);
			}
		}
	}
}
