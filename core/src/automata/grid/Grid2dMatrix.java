package automata.grid;

import automata.cell.Cell;

public class Grid2dMatrix<T extends Cell<T>> extends Grid<T> {
	private static final long serialVersionUID = 2412383921731567439L;
	private T[][] matrix;
	private T pad;
	
	@SuppressWarnings("unchecked")
	public Grid2dMatrix(int width, int height) {
		dimensions = new int[] {width, height};
		matrix = (T[][]) new Cell[width][height];
	}

	public Grid2dMatrix(T pad, int width, int height) {
		this(width, height);
		this.pad = pad;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				matrix[i][j] = pad.copy();
			}
		}
	}

	
	@Override
	public T getCell(CellPos position) {
		return matrix[position.getCoordinate(0)][position.getCoordinate(1)];
	}
	
	@Override
	public void setCell(T value, CellPos position) {
		matrix[position.getCoordinate(0)][position.getCoordinate(1)] = value;
	}
	
	@Override
	public boolean transition(Boundary bounds) {
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				matrix[i][j].transition(new Neighborhood<T>(this, bounds, new CellPos(i,j)));
			}
		}
		
		boolean changed = false;
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				changed = matrix[i][j].commit() || changed;
			}
		}
		return changed;
	}
	
	@Override
	public Grid2dMatrix<T> resize(int... dimensions) {
		Grid2dMatrix<T> grid = new Grid2dMatrix<T>(pad, dimensions[0], dimensions[1]);
		int offsetX = 0, offsetY = 0;
		if (grid.getRange(0) > getRange(0)) {
			offsetX = (grid.getRange(0) - getRange(0)) / 2;
		}
		if (grid.getRange(1) > getRange(1)) {
			offsetY = (grid.getRange(1) - getRange(1)) / 2;
		}
		for (int i = 0; i < Math.min(getRange(0), grid.getRange(0)); i++) {
			for (int j = 0; j < Math.min(getRange(1), grid.getRange(1)); j++) {
				grid.setCell(getCell(new CellPos(i, j)), new CellPos(offsetX + i, offsetY + j));
			}
		}
		return grid;
	}
	
	@Override
	public String toString() {
		StringBuffer strb = new StringBuffer();
		for (int i = 0; i < getRange(1); i++) {
			for (int j = 0; j < getRange(0); j++) {
				if (getCell(new CellPos(j,i)) != null) {
					strb.append(getCell(new CellPos(j, i)).toString().charAt(0));
				}
				else {
					strb.append("-");
				}
			}
			strb.append('\n');
		}
		return strb.toString();
	}
	
	@Override
	public boolean isValidPosition(CellPos position) {
		return position.getDimension() == 2 &&
				0 <= position.getCoordinate(0) && position.getCoordinate(0) < getRange(0) &&
				0 <= position.getCoordinate(1) && position.getCoordinate(1) < getRange(1);		
	}
}
