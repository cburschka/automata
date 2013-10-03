package automata.grid;

import java.io.Serializable;

import automata.cell.Cell;


public abstract class Grid<T extends Cell<T>> implements Serializable {
	private static final long serialVersionUID = 7994789741032850557L;
	protected int[] dimensions;
	
	public int getDimension() {
		return dimensions.length;
	}
	
	public int getRange(int i) {
		if (i < 0 || i >= dimensions.length) {
			throw new DimensionMismatchException(dimensions.length, i);
		}
		return dimensions[i];
	}
	
	public abstract T getCell(CellPos position);
	public abstract void setCell(T value, CellPos position);
	
	/**
	 * Apply the transition operation to every cell on the board.
	 * Transition is done in-place with two distinct operations:
	 * The grid will first need to apply the transition() method on every cell,
	 * then the commit() method on every cell.
	 * @param bounds the boundary to use for neighborhood extension.
	 * @return true if the transition has changed the grid in any way.
	 */
	public abstract boolean transition(Boundary bounds);
	
	/**
	 * Resize the grid with new dimensions. The grid can be either enlarged or
	 * reduced along each axis. If it is reduced, it will be truncated on the outside
	 * (away from the origin). If it is enlarged, it will be padded equally on both sides.
	 * The cell object to copy for padding should be determined by the extending type.
	 * @param dimensions the dimensions to resize to.
	 * @return
	 */
	public abstract Grid<T> resize(int... dimensions);
	
	/**
	 * Prüfe eine eingegebene Position auf Korrektheit: Prüfe die Dimension
	 * und den Bereich der Koordinaten.
	 * @param position
	 * @return true, wenn die Koordinate gültig ist.
	 */
	public abstract boolean isValidPosition(CellPos position);
}
