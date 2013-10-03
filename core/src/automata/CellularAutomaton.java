package automata;

import ui.theme.Theme;
import automata.cell.Cell;
import automata.grid.Grid;

public abstract class CellularAutomaton<T extends Cell<T>> {
	public abstract String getDescription();
	public abstract Grid<T> initialize(int... dimensions);
	public abstract T getZeroState();
	public abstract Theme<T> getTheme();
	public abstract String[] getSummary(Grid<T> grid);
	
	/**
	 * Diese Methode muss einen Zustand erzeugen, der in jeder Hinsicht identisch zu jedem Zustand
	 * ist, dessen toString()-Methode die übergebene String erzeugt. 
	 * @param str die String-Repräsentation des Zustands.
	 * @return den entsprechenden Zustand.
	 */
	public abstract T readCellState(String str);
}
