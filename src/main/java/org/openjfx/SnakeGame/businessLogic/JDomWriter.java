package org.openjfx.SnakeGame.businessLogic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.ImageIcon;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class JDomWriter {

	/*
	 * Instanzvariable für das Pfad Objekt der Datei in die geschrieben werden soll
	 */
	private final Path xmlFile;

	/**
	 * Konstruktor initialisiert das Pfad Objekt
	 * 
	 * @param file -> Pfad Objekt für die Datei
	 */
	public JDomWriter(Path file) {
		this.xmlFile = file;
	}

	/**
	 * Method generate a new root element for the xml file
	 * 
	 * @param rootElement	->	Name for the new root element
	 * @return 				->	returns the JDOM document object
	 *         					
	 */
	private Document createDoc(String rootElement) {

		if (rootElement != null && rootElement.length() > 1) {
			Document doc = new Document();
			Element root = new Element(rootElement);
			doc.setRootElement(root);
			return doc;
		}

		return null;

	}

	/**
	 * Method write the transferred data in the xml file
	 * 
	 * @param rootElement ->	Name of the root element from xml file
	 * @param element     -> 	Name of the child element that add to the root element
	 * @param score       -> 	Value of points to write in xml file
	 * @param appending   -> 	flag to determine whether the entries should be overwritten
	 * 							or appended to the existing elements in the xml file
	 */
	public void writeToXML(String rootElement, String element, Integer score, boolean appending) {

		// document object
		Document doc = null;
		// get document object from xml file
		try {
			doc = JDomReader.getDoc(xmlFile);
			
		} catch (JDOMException ex) {
			
			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
		} catch (IOException ex) {
			
			ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
		}

		// Liste für die bestehenden Einträge in der XML-Datei
		List<Element> children = null;

		// falls Einträge angehangen werden sollen
		if (doc != null && appending) {
			// bestehende Einträge holen
			children = doc.getRootElement().getChildren();

			if (!children.isEmpty()) {
				// neuen Eintrag erzeugen
				Element e1 = new Element(element);
				e1.setAttribute("value", score.toString());
				// den neuen Eintrag dem Wurzel-Element als Kind-Element
				// hinzufügen
				children.add(e1);
			}
		}
		// alle bestehenden Einträge im XML File überschreiben
		else {
			doc = createDoc(rootElement);
			Element e1 = new Element(element);
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
						new ImageIcon(getClass().getResource("/snake_img_dock.png")));
			}catch (NullPointerException ex) {
				// document is null
				ErrorBoxSwing.showExceptionStacktrace(ex, "Snake - Game: An error occurent", null);
			}

		}

	}
}
