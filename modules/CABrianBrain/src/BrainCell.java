import automata.cell.DiscreteCell;
import automata.grid.CellPos;
import automata.grid.Neighborhood;


public class BrainCell extends DiscreteCell<BrainCell> {
	private static final long serialVersionUID = 776758648059543542L;
	public static final int OFF = 0;
	public static final int ON = 1;
	public static final int DYING = 2;
	
	public BrainCell(int value) {
		super(value);
	}

	@Override
	public void transition(Neighborhood<BrainCell> nb) {
		if (get() == ON)
			set(DYING);
		else if (get() == DYING)
			set(OFF);
		else if (mooreNeighborSum(nb) == 2)
			set(ON);
	}
	
	private int mooreNeighborSum(Neighborhood<BrainCell> nb) {
		int sum = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) continue;
				if (nb.get(new CellPos(i, j)).get() == ON) {
					sum++;
				}
			}
		}
		return sum;
	}


	@Override
	public int getRange() {
		return 3;
	}

	@Override
	public BrainCell copy() {
		return new BrainCell(get());
	}
}
