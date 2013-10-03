import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;

public class CAHeat extends CellularAutomaton<HeatCell> {
	public static final double DIFFUSION_CONSTANT = 0.1;

	@Override
	public String getDescription() {
		return "Diffusion Simulation";
	}

	@Override
	public Grid<HeatCell> initialize(int... dimensions) {
		Grid2dMatrix<HeatCell> g = new Grid2dMatrix<HeatCell>(new HeatCell(0, 0.25), dimensions[0], dimensions[1]);
		for (int i=dimensions[0]/4; i < dimensions[0]*3/4; i++) {
			for (int j=dimensions[1]/4; j < dimensions[1]*3/4; j++) {
				HeatCell c = g.getCell(new CellPos(i,j));
				c.set(1);
				c.commit();
			}
		}
		return g;
	}

	@Override
	public HeatCell getZeroState() {
		return null;
	}

	@Override
	public Theme<HeatCell> getTheme() {
		// TODO Auto-generated method stub
		return new HeatTheme();
	}

	@Override
	public String[] getSummary(Grid<HeatCell> grid) {
		return new String[] {""};
	}

	@Override
	public HeatCell readCellState(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getName() {
		return "Heat Diffusion";
	}
}
