package automata.grid;

import java.util.ArrayList;
import java.util.List;

import automata.cell.Cell;

public class RecursiveGrid<T extends Cell<T>> extends Grid<T> {
	private static final long serialVersionUID = 6629037629598438376L;
	private List<T> values;
	private List<RecursiveGrid<T>> innerMatrix;
	private T pad;
	
	public RecursiveGrid(T pad, int... dimensions) {
		this.pad = pad;
		this.dimensions = dimensions;
		if (dimensions.length > 1) {
			int outerDimension = dimensions[0];
			int[] innerDimensions = shift(new CellPos(dimensions)).getCoordinates();
			innerMatrix = new ArrayList<RecursiveGrid<T>>(outerDimension);
			for (int i = 0; i < outerDimension; i++) {
				innerMatrix.add(new RecursiveGrid<T>(pad, innerDimensions));
			}
		}
		else {
			values = new ArrayList<T>(dimensions[0]);
			for (int i = 0; i < dimensions[0]; i++) {
				values.add(pad.copy());
			}
		}
	}
	
	public RecursiveGrid(int... dimensions) {
		this.dimensions = dimensions;
		if (dimensions.length > 1) {
			int outerDimension = dimensions[0];
			int[] innerDimensions = shift(new CellPos(dimensions)).getCoordinates();
			innerMatrix = new ArrayList<RecursiveGrid<T>>(outerDimension);
			for (int i = 0; i < outerDimension; i++) {
				innerMatrix.add(new RecursiveGrid<T>(innerDimensions));
			}
		}
		else {
			values = new ArrayList<T>(dimensions[0]);
			for (int i = 0; i < dimensions[0]; i++) {
				values.add(null);
			}
		}
	}

	@Override
	public T getCell(CellPos position) {
		if (position.getDimension() != dimensions.length) {
			throw new DimensionMismatchException(dimensions.length, position.getDimension());
		}
		
		if (position.getDimension() > 1) {
			int outerCoordinate = position.getCoordinate(0);
			CellPos innerCoordinates = shift(position);
			return innerMatrix.get(outerCoordinate).getCell(innerCoordinates);
		}
		else {
			return values.get(position.getCoordinate(0));
		}
	}

	@Override
	public void setCell(T value, CellPos position) {
		if (position.getDimension() != dimensions.length) {
			throw new DimensionMismatchException(dimensions.length, position.getDimension());
		}
		
		if (position.getDimension() > 1) {
			int outerCoordinate = position.getCoordinate(0);
			CellPos innerCoordinates = shift(position);
			innerMatrix.get(outerCoordinate).setCell(value, innerCoordinates);
		}
		else {
			values.set(position.getCoordinate(0), value);
		}

	}

	@Override
	public boolean transition(Boundary bounds) {
		__transition(this, bounds, new int[dimensions.length]);
		return commit();
	}
	
	private boolean commit() {
		boolean changed = false;
		if (dimensions.length > 1) {
			for (int i = 0; i < dimensions[0]; i++) {
				changed = innerMatrix.get(i).commit() || changed;
			}
		}
		else {
			for (int i = 0; i < dimensions[0]; i++) {
				changed = values.get(i).commit() || changed;
			}
		}
		return changed;
	}
	
	private void __transition(RecursiveGrid<T> root, Boundary bounds, int[] branch) {
		int level = branch.length - dimensions.length;
		if (dimensions.length == 1) {
			for (branch[level] = 0; branch[level] < dimensions[0]; branch[level]++) {
				values.get(branch[branch.length-1]).transition(new Neighborhood<T>(root, bounds, new CellPos(branch)));
			}
		}
		else {
			for (branch[level] = 0; branch[level] < dimensions[0]; branch[level]++) {
				innerMatrix.get(branch[level]).__transition(root, bounds, branch);
			}
		}
	}

	private static CellPos shift(CellPos position) {
		int[] innerCoordinates = new int[position.getDimension()-1];
		for (int i = 0; i < innerCoordinates.length; i++) {
			innerCoordinates[i] = position.getCoordinate(i+1); 
		}
		return new CellPos(innerCoordinates);
	}
	
	@Override
	public RecursiveGrid<T> resize(int... dimensions) {
		RecursiveGrid<T> grid = new RecursiveGrid<T>(pad, dimensions);
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
	public boolean isValidPosition(CellPos position) {
		if (position.getDimension() != getDimension())
			return false;
		for (int i = 0; i < position.getDimension(); i++)
			if (position.getCoordinate(i) < 0 || position.getCoordinate(i) >= getRange(i))
				return false;
		return true;
	}
}