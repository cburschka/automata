import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;


public class CAWireWorld extends CellularAutomaton<WireCell> {

	@Override
	public String getDescription() {
		return "WireWorld";
	}

	@Override
	public Grid<WireCell> initialize(int... dimensions) {
		return new Grid2dMatrix<WireCell>(new WireCell(0), dimensions[0], dimensions[1]);
	}

	@Override
	public WireCell getZeroState() {
		return new WireCell(0);
	}

	@Override
	public Theme<WireCell> getTheme() {
		return new WireTheme();
	}
	
	@Override
	public String[] getSummary(Grid<WireCell> grid) {
		return new String[0];
	}
	
	@Override
	public WireCell readCellState(String str) {
		return new WireCell(Integer.parseInt(str));
	}
	
	public static String getName() {
		return "WireWorld";
	}
}
