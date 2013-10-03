package ui.theme;

import java.awt.Color;

import automata.cell.DiscreteCell;

public class BinaryTheme<TCellState extends DiscreteCell<TCellState>> extends Theme<TCellState> {
	private Color[] colors = new Color[] {Color.WHITE, Color.BLACK};
	
	@Override
	public Color getColor(TCellState cell) {
		return colors[cell.get()];
	}
}
