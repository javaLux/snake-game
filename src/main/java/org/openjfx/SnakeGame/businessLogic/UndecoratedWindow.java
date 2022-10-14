package org.openjfx.SnakeGame.businessLogic;

import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UndecoratedWindow {
	/**
	 * Instanzvariablen Koordinaten der stage / des Mauszeigers: mouseDragDeltaX
	 * mouseDragDeltaY
	 */
	private double mouseDragDeltaX = 0;
	private double mouseDragDeltaY = 0;
	private EventHandler<MouseEvent> mousePressedHandler;
	private EventHandler<MouseEvent> mouseDraggedHandler;
	private WeakEventHandler<MouseEvent> weakMousePressedHandler;
	private WeakEventHandler<MouseEvent> weakMouseDraggedHandler;

	/**
	 * Der Methode wird das node (Layoutmanager) und die stage übergeben die per
	 * Mausklick bewegt werden soll.
	 *
	 * @param node  -> z.B. ein Layoutmanager vom Typ HBox
	 * @param stage -> die stage (Hauptbühne)
	 */
	public void allowDrag(Node node, Stage stage) {
		mousePressedHandler = (MouseEvent event) -> {
			mouseDragDeltaX = node.getLayoutX() - event.getSceneX();
			mouseDragDeltaY = node.getLayoutY() - event.getSceneY();
		};
		weakMousePressedHandler = new WeakEventHandler<>(mousePressedHandler);
		node.setOnMousePressed(weakMousePressedHandler);

		mouseDraggedHandler = (MouseEvent event) -> {
			stage.setX(event.getScreenX() + mouseDragDeltaX);
			stage.setY(event.getScreenY() + mouseDragDeltaY);
		};
		weakMouseDraggedHandler = new WeakEventHandler<>(mouseDraggedHandler);
		node.setOnMouseDragged(weakMouseDraggedHandler);
	}

	/**
	 * Methode verschiebt die stage auf dem desktop über einen Button. Dabei wird
	 * das MousePressedEvent auf dem übergeben Button aufgerufen, die X und Y
	 * koordinaten werden gespeichert und beim los lassen der Maustaste werden die
	 * jeweilgen Koordinaten an die stage übergeben.
	 * 
	 * @param stage -> Hauptfenster / Bühne
	 * @param btn   -> Button der zum verschieben der Stage genutzt werden soll
	 */
	public void allowDragOnButton(Stage stage, Button btn) {
		btn.setOnMousePressed(e -> {
			mouseDragDeltaX = btn.getLayoutX() - e.getSceneX();
			mouseDragDeltaY = btn.getLayoutY() - e.getSceneY();
		});

		btn.setOnMouseDragged(e -> {
			stage.setX(e.getScreenX() + mouseDragDeltaX);
			stage.setY(e.getScreenY() + mouseDragDeltaY);
		});
	}
}

