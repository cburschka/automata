package ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Listener fuer die Zeichenflaeche. Reagiert auf Klicks im Spielfeld
 * und manipuliert daraufhin die Zellen.
 * @author burschka
 *
 */
public class StatePanelListener implements MouseListener, MouseMotionListener {
	private StatePanel state;
	
	public StatePanelListener(StatePanel sp) {
		state = sp;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point position = e.getPoint();
		state.manipulate(position.x, position.y, e.getButton() == MouseEvent.BUTTON1);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		state.resetManipulation();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		Point position = e.getPoint();
		state.manipulate(position.x, position.y, !e.isMetaDown());
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}
}
