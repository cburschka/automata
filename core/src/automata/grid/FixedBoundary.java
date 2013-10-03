package automata.grid;

import automata.cell.Cell;

public class FixedBoundary<TB extends Cell<TB>> extends Boundary {
	public TB constant;
	public FixedBoundary(TB constant) {
		super(new BoundaryCondition[1]);
		this.constant = constant;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Cell<T>> T get(Grid<T> grid, CellPos position) {
		for (int i = 0; i < position.getDimension(); i++) {
			if (position.getCoordinate(i) < 0 || position.getCoordinate(i) >= grid.getRange(i)) {
				return (T) constant;
			}
		}
		return grid.getCell(position);
	}
}
