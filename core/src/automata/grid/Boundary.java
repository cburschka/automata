package automata.grid;

import automata.cell.Cell;


public class Boundary {
	public BoundaryCondition[] conditions;
	
	public Boundary(BoundaryCondition... boundaryConditions) {
		conditions = boundaryConditions;
	}
		
	public <T extends Cell<T>> T get(Grid<T> grid, CellPos position) {
		if (conditions.length != position.getDimension()) {
			throw new DimensionMismatchException(conditions.length, position.getDimension());
		}
		int[] fixedCoordinates = new int[position.getDimension()];
		for (int i = 0; i < position.getDimension(); i++) {
			fixedCoordinates[i] = conditions[i].map(grid.getRange(i), position.getCoordinate(i));
		}
		return grid.getCell(new CellPos(fixedCoordinates));
	}
	
	@Override
	public String toString() {
		String value = "Boundary[" + conditions.length + "]";
		for (int i = 0; i < conditions.length; i++) {
			value += "<" + conditions[i] + ">";
		}
		return value;
	}
}
