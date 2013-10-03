import java.awt.Color;

import ui.theme.Theme;


public class BrusselatorTheme extends Theme<BrusselatorCell> {
	@Override
	public Color getColor(BrusselatorCell cell) {
		return new Color(Math.min(1, (float)(cell.get()[0]/(3))), Math.min(1, (float)((cell.get()[1]/(3)))), 0);
	}
}
