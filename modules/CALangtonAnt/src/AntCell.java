import automata.cell.DiscreteCell;
import automata.grid.Neighborhood;


public class AntCell extends DiscreteCell<AntCell> {
	private static final long serialVersionUID = -4071903982261339673L;
	private int range;
	
	public AntCell(int current, int range) {
		super(current, range);
		this.range = range;
	}
	
	@Override
	public void transition(Neighborhood<AntCell> nb) {
		set(get()+1);
	}	

	@Override
	public int getRange() {
		return range;
	}
	
	@Override
	public AntCell copy() {
		return new AntCell(get(), range);
	}
}
