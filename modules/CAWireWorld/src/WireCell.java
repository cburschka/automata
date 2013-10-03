import automata.cell.DiscreteCell;
import automata.grid.CellPos;
import automata.grid.Neighborhood;


public class WireCell extends DiscreteCell<WireCell> {
	private static final long serialVersionUID = -6070161235610394980L;

	public WireCell(int current) {
		super(current);
	}
	private static final int ELECTRON_HEAD = 1;
	private static final int ELECTRON_TAIL = 2;
	private static final int CONDUCTOR = 3;
	
	@Override
	public void transition(Neighborhood<WireCell> nb) {
		if (get() == ELECTRON_HEAD)
			set(ELECTRON_TAIL);
		else if (get() == ELECTRON_TAIL)
			set(CONDUCTOR);
		else if (get() == CONDUCTOR) {
			int sum = mooreNeighborSum(nb);
			if (sum == 1 || sum == 2) {
				set(ELECTRON_HEAD);
			}
		}
	}
	

	private int mooreNeighborSum(Neighborhood<WireCell> nb) {
		int sum = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) continue;
				if (((WireCell)(nb.get(new CellPos(i, j)))).get() == ELECTRON_HEAD) {
					sum++;
				}
			}
		}
		return sum;
	}

	@Override
	public int getRange() {
		return 4;
	}

	@Override
	public WireCell copy() {
		return new WireCell(get());
	}

}
