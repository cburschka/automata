import automata.grid.Boundary;
import automata.grid.CellPos;
import automata.grid.Grid;


public class AntGrid extends Grid<AntCell> {
	private static final long serialVersionUID = 66889110802387698L;
	private Ant ant;
	private AntCell[][] matrix;
	
	public AntGrid(Ant ant, int width, int height) {
		dimensions = new int[] {width, height};
		matrix = new AntCell[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				matrix[i][j] = new AntCell(0, ant.getRange());
			}
		}
		this.ant = ant;
	}
	
	@Override
	public boolean transition(Boundary bounds) {
		AntCell c = getCell(ant.getPosition());
		c.transition(null);
		ant.turn(c.get());
		c.commit();
		ant.forward(getRange(0), getRange(1));
		return true;
	}
	
	@Override
	public AntGrid resize(int... dimensions) {
		AntGrid grid = new AntGrid(ant, dimensions[0], dimensions[1]);
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
	public AntCell getCell(CellPos position) {
		return matrix[position.getCoordinate(0)][position.getCoordinate(1)];
	}

	@Override
	public boolean isValidPosition(CellPos position) {
		return 0 < position.getCoordinate(0) && position.getCoordinate(0) < dimensions[0] &&
			   0 < position.getCoordinate(1) && position.getCoordinate(1) < dimensions[1];
	}

	@Override
	public void setCell(AntCell value, CellPos position) {
		matrix[position.getCoordinate(0)][position.getCoordinate(1)] = value;
	}
}
