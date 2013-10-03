package automata.grid;

import automata.cell.Cell;

public class Neighborhood<T extends Cell<T>> {
	private Grid<T> grid;
	private Boundary bounds;
	private CellPos origin;
	
	public int getDimension() {
		return grid.getDimension();
	}
	
	public Neighborhood(Grid<T> grid, Boundary bounds, CellPos origin) {
		if (origin.getDimension() != grid.getDimension()) {
			throw new DimensionMismatchException(grid.getDimension(), origin.getDimension());
		}
		this.grid = grid;
		this.origin = origin;
		this.bounds = bounds;
	}
	
	public T get(CellPos relativePosition) {
		if (relativePosition.getDimension() != grid.getDimension()) {
			throw new DimensionMismatchException(grid.getDimension(), relativePosition.getDimension());
		}
		return bounds.get(grid, origin.add(relativePosition));
	}
}
