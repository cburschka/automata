package automata.bushfire;

import automata.cell.DiscreteCell;
import automata.grid.CellPos;
import automata.grid.Neighborhood;

public class BushfireCell extends DiscreteCell<BushfireCell> {
	private static final long serialVersionUID = -1533367718948242265L;

	public BushfireCell(int value) {
		super(value);
	}
	
	private static final String[] names = new String[] {
		"Normal",
		"Feuer",
		"Erholung"
	};
	
	public static final int NORMAL = 0;
	public static final int FIRE = 1;
	public static final int RECOVER = 2;
	
	@Override
	public String toString() {
		return names[get()];
	}
	
	@Override
	public void transition(Neighborhood<BushfireCell> nb) {
		switch (get()) {
		case FIRE:
			set(RECOVER);
			return;
		case RECOVER:
			set(NORMAL);
			return;
		case NORMAL:
			int[] vector = new int[nb.getDimension()];
			for (int i = 0; i < vector.length; i++) {
				vector[i] = -1;
				if (nb.get(new CellPos(vector.clone())).get() == FIRE) {
					set(FIRE);
					return;
				}
				vector[i] = 1;
				if (nb.get(new CellPos(vector.clone())).get() == FIRE) {
					set(FIRE);
					return;
				}
				vector[i] = 0;
			}
		}
	}
	
	@Override
	public int getRange() {
		return 3;
	}
	
	@Override
	public BushfireCell copy() {
		return new BushfireCell(get());
	}
}
