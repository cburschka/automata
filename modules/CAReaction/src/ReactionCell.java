import automata.cell.RealCell;
import automata.grid.CellPos;
import automata.grid.Neighborhood;


public class ReactionCell extends RealCell<ReactionCell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1457659105205442327L;

	public ReactionCell(double current, double stepSize) {
		super(current, stepSize);
	}

	@Override
	public void transition(Neighborhood<ReactionCell> nb) {
		double diffusion = CAReaction.DIFFUSION * (nb.get(new CellPos(1,0)).get() + nb.get(new CellPos(0,-1)).get() + nb.get(new CellPos(0,1)).get() + nb.get(new CellPos(-1,0)).get() - 4 * get());
		//double reaction = CAReaction.GROWTH * get() * (CAReaction.SATURATION - get());
		double reaction = CAReaction.GROWTH * (get()) * (CAReaction.SATURATION - get()) * (0.5 * CAReaction.SATURATION - get());
		set(Math.max(0,  get() + diffusion*0 - reaction));
	}

	@Override
	public double getRange() {
		return CAReaction.SATURATION * 2;
	}

	@Override
	public ReactionCell copy() {
		return new ReactionCell(get(), getStepSize());
	}
}
