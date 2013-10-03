package automata.life;

import ui.theme.BinaryTheme;
import ui.theme.Theme;
import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;


public class LifeVariant extends CellularAutomaton<LifeCell> {
	private LifeRule rule;
	private LifeVariantCell DEAD;
	
	@Override
	public String getDescription() {
		return "Life Variant " + rule;
	}
	
	public LifeVariant(LifeRule rule) {
		this.rule = rule;
		DEAD = new LifeVariantCell(rule, LifeVariantCell.DEAD);
	}
	
	public LifeVariant(String ruleNotation) throws InvalidRuleException {
		this.rule = new LifeRule(ruleNotation);
		DEAD = new LifeVariantCell(rule, LifeVariantCell.DEAD);
	}
	
	@Override
	public LifeVariantCell getZeroState() {
		return DEAD;
	}
	
	@Override
	public Grid<LifeCell> initialize(int... dimensions) {
		return new Grid2dMatrix<LifeCell>(DEAD, dimensions[0], dimensions[1]);
	}

	@Override
	public Theme<LifeCell> getTheme() {
		return new BinaryTheme<LifeCell>();
	}
	
	@Override
	public String[] getSummary(Grid<LifeCell> grid) {
		int total = grid.getRange(0) * grid.getRange(1);
		int alive = 0;
		for (int i = 0; i < grid.getRange(0); i++) {
			for (int j = 0; j < grid.getRange(1); j++) {
				if (!grid.getCell(new CellPos(i, j)).equals(DEAD)) {
					alive++;
				}
			}
		}
		return new String[] {
				"Lebend: " + alive,
				"Tot:" + (total-alive)
		};
	}
	
	@Override
	public LifeVariantCell readCellState(String str) {
		int value = Integer.parseInt(str);
		return new LifeVariantCell(rule, value);
	}

}
