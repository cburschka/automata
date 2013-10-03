package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import automata.grid.CellPos;

/**
 * Die Zeichenfläche für die Simulation ist ein erweitertes JPanel,
 * das in der paint()-Methode sein Gitter zeichnet.
 * Es muss außerdem auf Änderungen in der Fenstergröße und in der Gittergröße
 * reagieren, und jeweils die Zellgröße sowie seine eigene Größe anpassen.
 */
public class StatePanel extends JPanel {
	private static final long serialVersionUID = -3688806841006732251L;
	private CellPos lastManipulated;
	private Point offset;
	private int cellSize;
	private GUIController ctrl;

	/**
	 * Die minimale Zellengröße in Pixeln.
	 */
	private static final int CELL_MIN_SIZE = 1;
	
	public StatePanel() {}
	
	/**
	 * Erzeugt eine neue Zeichenflaeche fuer den Zustand
	 * @param attachedState der zu zeichnende Zustand
	 * @param minSize die minimale Groesse der Zeichenflaeche
	 */
	public StatePanel(GUIController ctrl) {
		lastManipulated = new CellPos(-1, -1);
		this.ctrl = ctrl;
	}
	
	/**
	 * Berechne die Groesse der Zellen und des Zeichenfensters, und die zentrierte
	 * Position des Gitters auf dem Zeichenfensters.
	 */
	void checkResize() {
		if (ctrl.getGrid() != null) {
			// Validiere zweimal, um auf Änderung in den Scroll-Leisten zu reagieren.
			for (int i = 0; i < 2; i++) {
				Dimension viewportSize = this.getParent().getSize();
				Dimension gridSize = new Dimension(ctrl.getGrid().getRange(0), ctrl.getGrid().getRange(1));
				cellSize = Math.max(CELL_MIN_SIZE, Math.min(viewportSize.width / gridSize.width, viewportSize.height / gridSize.height));
				Dimension requiredSize = new Dimension(cellSize * gridSize.width, cellSize * gridSize.height);
				
				// Das Panel ist mindestens so groß wie der darstellbare Bereich oder die Gittergröße.
				Dimension panelSize = new Dimension(Math.max(viewportSize.width, requiredSize.width), Math.max(viewportSize.height, requiredSize.height));
				offset = new Point((panelSize.width - gridSize.width * cellSize) / 2, (panelSize.height - gridSize.height * cellSize) / 2 );
				setPreferredSize(panelSize);
				
				// Markiere das Panel als geändert, und validiere dann die ScrollPane.
				revalidate();
				getParent().getParent().validate();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (ctrl != null && ctrl.getAutomaton() != null && ctrl.getGrid() != null) {
			for (int i = 0; i < ctrl.getGrid().getRange(0); i++) {
				for (int j = 0; j < ctrl.getGrid().getRange(1); j++) {
					g.setColor(ctrl.getAutomaton().getTheme().getColor(ctrl.getGrid().getCell(new CellPos(i, j))));
					g.fillRect(offset.x + i*cellSize, offset.y + j*cellSize, cellSize, cellSize);
					if (cellSize > 5) {
						g.setColor(Color.BLACK);
						g.drawRect(offset.x + i*cellSize, offset.y + j*cellSize, cellSize, cellSize);
					}
				}
			}
		}
	}
	
	/**
	 * Eine Manipulation (gewoehnlich durch Mausklick) in einer
	 * bestimmten Position des Zeichenfensters aendert den Zustand in der dortigen Zelle.
	 * Bei Welten mit mehr als zwei moeglichen Zustaenden wechselt die Zelle zyklisch durch
	 * alle Moeglichkeiten. 
	 * @param x die horizontale Koordinate der Manipulation
	 * @param y die vertikale Koordinate des Manipulation
	 * @param increment ob der Wert inkrementiert oder dekrementiert werden soll (bei binären Zellen irrelevant).
	 */
	public void manipulate(int x, int y, boolean increment) {
		if (ctrl.getGrid() != null) {
			CellPos position = new CellPos((x - offset.x) / cellSize, (y - offset.y) / cellSize);
			if (ctrl.getGrid().isValidPosition(position)) {
				if (!position.equals(lastManipulated)) {
					if (increment)
						ctrl.getGrid().getCell(position).increment();
					else
						ctrl.getGrid().getCell(position).decrement();
					lastManipulated = position;
					ctrl.refreshView();
				}
			}
		}
	}
	
	public void resetManipulation() {
		lastManipulated = new CellPos(-1, -1);
	}
}
