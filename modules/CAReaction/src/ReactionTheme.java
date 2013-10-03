import java.awt.Color;

import ui.theme.Theme;


public class ReactionTheme extends Theme<ReactionCell> {
	@Override
	public Color getColor(ReactionCell cell) {
		return new Color((float)(cell.get()/cell.getRange()), 0, (float)((1-cell.get()/cell.getRange())));
	}
}
