package automata.bushfire;

import ui.theme.BushfireTheme;
import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.RecursiveGrid;

/**
 * Simuliert ein Buschfeuer
 * @author burschka
 */
public class Bushfire extends CellularAutomaton<BushfireCell> {
	public static final BushfireCell NORMAL = new BushfireCell(BushfireCell.NORMAL);
	public static final BushfireCell RECOVER = new BushfireCell(BushfireCell.RECOVER);
	public static final BushfireCell FIRE = new BushfireCell(BushfireCell.FIRE);
	
	@Override
	public BushfireCell getZeroState() {
		return NORMAL;
	}
	
	@Override
	public Grid<BushfireCell> initialize(int... dimensions) {
		return new RecursiveGrid<BushfireCell>(NORMAL, dimensions);
	}
	
	@Override
	public String getDescription() {
		return "Buschfeuer";
	}
	
	@Override
	public Theme<BushfireCell> getTheme() {
		return new BushfireTheme();
	}
	
	public static void main(String[] args) {
		Bushfire bf = new Bushfire();
		Grid<BushfireCell> g = bf.initialize(10, 10);
		g.setCell(FIRE.copy(), new CellPos(5, 5));
	}
	
	@Override
	public String[] getSummary(Grid<BushfireCell> grid) {
		return new String[0];
	}
	
	@Override
	public BushfireCell readCellState(String str) {
		int value = Integer.parseInt(str);
		return new BushfireCell(value);
	}
}
