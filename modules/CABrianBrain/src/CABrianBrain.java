import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;


public class CABrianBrain extends CellularAutomaton<BrainCell> {
	public static final BrainCell OFF = new BrainCell(BrainCell.OFF);
	
	@Override
	public String getDescription() {
		return "Brian's Brain";
	}

	@Override
	public Grid<BrainCell> initialize(int... dimensions) {
		return new Grid2dMatrix<BrainCell>(OFF, dimensions[0], dimensions[1]);
	}

	@Override
	public BrainCell getZeroState() {
		return OFF;
	}
	
	@Override
	public Theme<BrainCell> getTheme() {
		return new BrainTheme();
	}
	
	@Override
	public String[] getSummary(Grid<BrainCell> grid) {
		return new String[0];
	}
	
	@Override
	public BrainCell readCellState(String str) {
		return new BrainCell(Integer.parseInt(str));
	}
	
	public static String getName() {
		return "Brian's Brain";
	}
}
