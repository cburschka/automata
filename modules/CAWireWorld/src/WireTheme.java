import java.awt.Color;

import ui.theme.Theme;


public class WireTheme extends Theme<WireCell> {
	private static final Color[] colors = new Color[] {
		Color.BLACK,
		Color.BLUE,
		Color.RED,
		Color.YELLOW
	};

	@Override
	public Color getColor(WireCell cell) {
		return colors[cell.get()];
	}

}
