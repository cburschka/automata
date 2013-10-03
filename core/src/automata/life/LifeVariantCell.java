package automata.life;

import automata.grid.Neighborhood;

public class LifeVariantCell extends LifeCell {
	private static final long serialVersionUID = -1454760960141441669L;
	private LifeRule rule;
	public static final int ALIVE = 1;
	public static final int DEAD = 0;
	
	public LifeVariantCell(LifeRule rule, int value) {
		super(value);
		this.rule = rule;
	}
	
	@Override
	public void transition(Neighborhood<LifeCell> nb) {
		int sum = mooreNeighborSum(nb);
		set(rule.apply(get(), sum));
	}
	
	@Override
	public LifeVariantCell copy() {
		return new LifeVariantCell(this.rule, get());
	}
	
	@Override
	public int getRange() {
		return 2;
	}
}
