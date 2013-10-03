import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;

public class CAReaction extends CellularAutomaton<ReactionCell> {
	public static final double DIFFUSION = 0.2;
	public static final double GROWTH = 0.01;
	public static final double SATURATION = 1.0;

	@Override
	public String getDescription() {
		return "Growth Simulation";
	}

	@Override
	public Grid<ReactionCell> initialize(int... dimensions) {
		Grid2dMatrix<ReactionCell> g = new Grid2dMatrix<ReactionCell>(new ReactionCell(0, 0.25), dimensions[0], dimensions[1]);
		for (int i=dimensions[0]/6; i < dimensions[0]*4/8; i++) {
			for (int j=dimensions[1]/6; j < dimensions[1]*4/8; j++) {
				ReactionCell c = g.getCell(new CellPos(i,j));
				c.set(2);
				c.commit();
			}
		}
		return g;
	}

	@Override
	public ReactionCell getZeroState() {
		return null;
	}

	@Override
	public Theme<ReactionCell> getTheme() {
		// TODO Auto-generated method stub
		return new ReactionTheme();
	}

	@Override
	public String[] getSummary(Grid<ReactionCell> grid) {
		return new String[] {""};
	}

	@Override
	public ReactionCell readCellState(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getName() {
		return "Growth";
	}
}
