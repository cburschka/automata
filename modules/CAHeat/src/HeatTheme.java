import java.awt.Color;

import ui.theme.Theme;


public class HeatTheme extends Theme<HeatCell> {
	@Override
	public Color getColor(HeatCell cell) {
		return new Color((float)cell.get(), 0, (float)(1-cell.get()));
	}
}
