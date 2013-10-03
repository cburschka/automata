package controls;

import java.awt.Dimension;

import automata.CellularAutomaton;
import automata.cell.Cell;
import automata.grid.Boundary;
import automata.grid.Grid;

/**
 * Eine abstrakte Vorlage fuer die Kontrollverbindung.
 * Diese muss von der Seite der grafischen Oberflaeche implementiert werden.
 * @author burschka
 *
 */
public abstract class Controller {
	
	/**
	 * Refresh the view using a new state, and setting a generation counter.
	 */
	public abstract void refreshView();
	
	/**
	 * Fetch the current state of the simulation.
	 * @return the state
	 */
	public abstract Grid<?> getGrid();
	
	/**
	 * The desired speed of a continuous simulation.
	 * @return an integer specifying how many iterations should be run per second.
	 */
	public abstract int getSpeed();
	
	/**
	 * Notify the view that the continuous simulation has been stopped.
	 */
	public abstract void stopped();
	
	/**
	 * Notify the view that the continuous simulation has been started.
	 */
	public abstract void started();
	
	/**
	 * React to a user-specified reset action.
	 */
	public abstract void reset();
	
	/**
	 * Resize the board.
	 */
	public abstract void resize(Dimension size);
	
	/**
	 * Show the user an error message.
	 * @param message the message to display.
	 */
	public abstract void setError(String message);
	
	/**
	 * Show the user a status message.
	 * @param message the message to display.
	 */
	public abstract void setStatus(String message);
	
	/**
	 * Replace the model's current automaton with a new one.
	 * @param automaton the new automaton
	 */
	public abstract <T extends Cell<T>> void setAutomaton(CellularAutomaton<T> automaton);
	
	public abstract <T extends Cell<T>> void setGrid(Grid<T> grid);
	
	/**
	 * Stop the running simulation.
	 */
	public abstract void stop();
	
	/**
	 * Start the running simulation.
	 */
	public abstract void start();
	
	public abstract Boundary getBounds();
	public abstract void setBounds(Boundary bounds);
	
	public abstract Dimension getGridSize();
}
