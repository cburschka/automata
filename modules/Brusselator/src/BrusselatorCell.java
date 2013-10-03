import automata.grid.CellPos;
import automata.grid.Neighborhood;


public class BrusselatorCell extends VectorCell<BrusselatorCell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1457659105205442327L;

	public BrusselatorCell(double x, double y) {
		super(new double[] {x,y});
	}

	@Override
	public void transition(Neighborhood<BrusselatorCell> nb) {
		double[] diffusion = new double[2];
		for (int i = 0; i < 2; i++) {
			diffusion[i] = Brusselator.DIFFUSION[i] * (nb.get(new CellPos(1,0)).get()[i] + nb.get(new CellPos(0,-1)).get()[i] + nb.get(new CellPos(0,1)).get()[i] + nb.get(new CellPos(-1,0)).get()[i] - 4 * get()[i]);
		}
		//double reaction = CAReaction.GROWTH * get() * (CAReaction.SATURATION - get());
		double dx = Brusselator.REACTION * (Brusselator.A + Math.pow(get()[0], 2)*get()[1] - Brusselator.B* get()[0] - get()[0]);
		double dy = Brusselator.REACTION * (Brusselator.B * get()[0] - Math.pow(get()[0], 2)*get()[1]);
		set(Math.max(0, get()[0]+diffusion[0]+dx), Math.max(0, get()[1]+diffusion[1]+dy));
		//System.out.println(get()[0] + " " + get()[1]);
	}

	@Override
	public BrusselatorCell copy() {
		return new BrusselatorCell(get()[0], get()[1]);
	}
	
	@Override
	public String toString() {
		return "(" + get()[0] + "," + get()[1] + ")";
	}
}
