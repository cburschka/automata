import java.awt.Color;

import ui.theme.Theme;


public class BrainTheme extends Theme<BrainCell> {
	private static final Color[] colors = new Color[] {Color.BLUE, Color.RED, Color.YELLOW};

	@Override
	public Color getColor(BrainCell cell) {
		return colors[cell.get()];
	}

}
