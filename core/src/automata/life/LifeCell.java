package automata.life;

import automata.cell.DiscreteCell;
import automata.grid.CellPos;
import automata.grid.Neighborhood;

public class LifeCell extends DiscreteCell<LifeCell> {
	private static final long serialVersionUID = 8406213315702484492L;
	public static final int DEAD = 0;
	public static final int ALIVE = 1;
	
	public LifeCell(int value) {
		super(value);
	}

	@Override
	public LifeCell copy() {
		return new LifeCell(get());
	}

	@Override
	public int getRange() {
		return 2;
	}

	@Override
	public void transition(Neighborhood<LifeCell> nb) {
		int sum = mooreNeighborSum(nb);
		set((sum == 3 || (sum == 2 && get() == ALIVE) ? ALIVE : DEAD));
	}
	
	public static int mooreNeighborSum(Neighborhood<LifeCell> nb) {
		int sum = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) continue;
				if ((nb.get(new CellPos(i, j))).get() == ALIVE) {
					sum++;
				}
			}
		}
		return sum;
	}
}
