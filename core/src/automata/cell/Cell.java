package automata.cell;

import java.io.Serializable;

import automata.grid.Neighborhood;

public interface Cell<T extends Cell<T>> extends Serializable {
	public abstract void increment();
	public abstract void decrement();
	
	/**
	 * Bestimme den zukünftigen Zustand der Zelle.
	 * @param nb die aktuelle Nachbarschaft der Zelle.
	 */
	public abstract void transition(Neighborhood<T> nb);
	
	/**
	 * Führe die berechnete Transition durch.
	 * @return true, falls der Zustand sich dadurch ändert.
	 */
	public abstract boolean commit();
	
	/**
	 * Die String-Repräsentation der Zelle muss eindeutig sein, so dass
	 * jede dieselbe String erzeugende Zelle mit dieser Zelle identisch sein muss.
	 * Es darf kein whitespace enthalten sein. 
	 * @return die String-Repräsentation.
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Erzeuge eine Kopie der Zelle.
	 * @return die Kopie
	 */
	public abstract T copy();
}
