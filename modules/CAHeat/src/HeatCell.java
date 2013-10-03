import automata.cell.RealCell;
import automata.grid.CellPos;
import automata.grid.Neighborhood;


public class HeatCell extends RealCell<HeatCell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1457659105205442327L;

	public HeatCell(double current, double stepSize) {
		super(current, stepSize);
	}

	@Override
	public void transition(Neighborhood<HeatCell> nb) {
		set(get() + CAHeat.DIFFUSION_CONSTANT * (nb.get(new CellPos(1,0)).get() + nb.get(new CellPos(0,-1)).get() + nb.get(new CellPos(0,1)).get() + nb.get(new CellPos(-1,0)).get() - 4 * get()));	
	}

	@Override
	public double getRange() {
		return 1.0;
	}

	@Override
	public HeatCell copy() {
		return new HeatCell(get(), getStepSize());
	}
}
