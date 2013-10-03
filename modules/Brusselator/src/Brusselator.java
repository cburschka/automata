import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;

public class Brusselator extends CellularAutomaton<BrusselatorCell> {
	public static final double[] DIFFUSION = new double[] {0.01, 0.1};
	public static final double REACTION = 0.05;
	public static final double[] CONVECTION = new double[] {0.1, 0.1};
	public static final double A = 0.6;
	public static final double B = 1.26;

	@Override
	public String getDescription() {
		return "Brusselator";
	}

	@Override
	public Grid<BrusselatorCell> initialize(int... dimensions) {
		Grid2dMatrix<BrusselatorCell> g = new Grid2dMatrix<BrusselatorCell>(new BrusselatorCell(0, 0), dimensions[0], dimensions[1]);
		for (int i=dimensions[0]*0; i < dimensions[0]; i++) {
			for (int j=dimensions[1]*0; j < dimensions[1]; j++) {
				BrusselatorCell c = g.getCell(new CellPos(i,j));
				c.set(i < dimensions[0]/2 ? 1 : 0, i < dimensions[0]/2 ? 0 : 1);
				c.commit();
			}
		}
		return g;
	}

	@Override
	public BrusselatorCell getZeroState() {
		return null;
	}

	@Override
	public Theme<BrusselatorCell> getTheme() {
		// TODO Auto-generated method stub
		return new BrusselatorTheme();
	}

	@Override
	public String[] getSummary(Grid<BrusselatorCell> grid) {
		return new String[] {""};
	}

	@Override
	public BrusselatorCell readCellState(String str) {
		String[] contents = str.substring(1, str.length()-1).split(",");
		return new BrusselatorCell(Double.parseDouble(contents[0]), Double.parseDouble(contents[0]));
	}

	public static String getName() {
		return "Brusselator";
	}
}
