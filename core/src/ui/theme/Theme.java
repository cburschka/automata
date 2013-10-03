package ui.theme;

import java.awt.Color;

import automata.cell.Cell;

public abstract class Theme<TCellState extends Cell<TCellState>> {
	public abstract Color getColor(TCellState cell);
}
