package org.openjfx.SnakeGame.businessLogic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * KLasse zum einlesen der Daten aus einer XML-Datei
 * 
 * @author CSD
 *
 */
public class JDomReader {
	
	/**
	 * Methode create new document object for an xml file
	 * 
	 * @param xmlFile			->	Path to xml file
	 * @return					->	return null if no document within xml file otherwise the document object
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Document getDoc(Path xmlFile) throws JDOMException, IOException {

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
	 * Method check if the xml file an valid xml file within an JDom root element
	 * @param xmlFile
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static boolean isValidXmlFile(Path xmlFile) throws JDOMException, IOException {
		
		if (xmlFile != null) {
			if (xmlFile.toFile().exists() && xmlFile.toFile().length() > 0) {

				SAXBuilder builder = new SAXBuilder();
				
				Document doc = builder.build(xmlFile.toFile());
				
				try {
					Element rootElement = doc.getRootElement();
					if(rootElement != null) {
						// if an rrot element was found -> return true
						return true;
					}
					
				// if no root element within xml file -> thrown exception and return false
				} catch (IllegalStateException e) {
					return false;
				}
				
				
			}
		}
		return false;
	}

	/**
	 * Methode gibt den Wert des Highscore Elementes in der XML zurück Falls es
	 * dieses Element als Child in der XML-Datei gibt
	 * 
	 * @param xmlFile     -> XML-File als Path object
	 * @param elementName -> Element nach dem gesucht wird
	 * @return -> String mit dem Wert oder null wenn kein Wert ausgelesen werden
	 *         konnte oder das Element nicht existiert
	 */
	public static String getHighScoreFromXML(Path xmlFile, String elementName) {
		// nur ausführen wenn alle Parameter korrekt übergeben wurden
		if (xmlFile != null && elementName != null) {
			if (xmlFile.toFile().exists()) {

				File file = xmlFile.toFile();
				SAXBuilder builder = new SAXBuilder();
				
				// doc object aus xml erstellen
				Document doc;
				try {
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
					/*
					 *  falls kein root element gelesen werden konnte oder keines erstellt wurde
					 *  also die XML-Datei noch leer ist
					 *  ->	dann wird die XML-Datei mit JDOM neu erstellt
					 */
					JDomWriter xmlWriter = new JDomWriter(xmlFile);
					xmlWriter.writeToXML("score", "highscore", 0, false);
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		return null;
	}

}
