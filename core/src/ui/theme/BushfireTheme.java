package ui.theme;

import java.awt.Color;

import automata.bushfire.BushfireCell;

public class BushfireTheme extends Theme<BushfireCell> {
	private Color[] colors = new Color[] {Color.GREEN, Color.RED, Color.YELLOW};
	
	@Override
	public Color getColor(BushfireCell cell) {
		return colors[cell.get()];
	}
}
