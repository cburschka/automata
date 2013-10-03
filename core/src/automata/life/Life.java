package automata.life;

import ui.theme.BinaryTheme;
import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;

public class Life extends CellularAutomaton<LifeCell> {
	public static final LifeCell DEAD = new LifeCell(LifeCell.DEAD);
	public static final LifeCell ALIVE = new LifeCell(LifeCell.ALIVE);

	@Override
	public String getDescription() {
		return "Game of Life";
	}

	@Override
	public LifeCell getZeroState() {
		return DEAD;
	}

	@Override
	public Grid<LifeCell> initialize(int... dimensions) {
		return new Grid2dMatrix<LifeCell>(DEAD, dimensions[0], dimensions[1]);
	}

	@Override
	public Theme<LifeCell> getTheme() {
		return new BinaryTheme<LifeCell>();
	}
	
	@Override
	public String[] getSummary(Grid<LifeCell> grid) {
		int total = grid.getRange(0) * grid.getRange(1);
		int alive = 0;
		for (int i = 0; i < grid.getRange(0); i++) {
			for (int j = 0; j < grid.getRange(1); j++) {
				if (grid.getCell(new CellPos(i, j)).equals(ALIVE)) {
					alive++;
				}
			}
		}
		return new String[] {
				"Lebend: " + alive,
				"Tot:" + (total-alive)
		};
	}
	
	@Override
	public LifeCell readCellState(String str) {
		int value = Integer.parseInt(str);
		return new LifeCell(value);
	}
}
