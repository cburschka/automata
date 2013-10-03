import java.awt.Color;

import ui.theme.Theme;
import automata.cell.DiscreteCell;


public class HueTheme<T extends DiscreteCell<T>> extends Theme<T> {
/*	private static final Color[] colors = new Color[] {
		Color.WHITE,
		Color.BLACK,
		Color.BLUE,
		Color.GREEN,
		Color.YELLOW,
		Color.CYAN,
		Color.MAGENTA,
		Color.ORANGE,
	};
	
	@SuppressWarnings("unchecked")*/
	@Override
	public Color getColor(T cell) {
		//return new Color(0, (int)(255.0*c.get()/c.getRange()), (int)(255-255.0*c.get()/c.getRange()));
		float hue = (float)(6.0 * cell.get() / cell.getRange());
		float inner = (float)(hue % 1);
		float z = 0;
		float o = 1;
		switch ((int)(hue)) {
		case 0:
			return new Color(z, inner, o);
		case 1:
			return new Color(z, o, 1-inner);
		case 2:
			return new Color(inner, o, z);
		case 3:
			return new Color(o, 1-inner, z);
		case 4:
			return new Color(o, z, inner);
		case 5:
			return new Color(1-inner, z, o);
		}
		return null;
	}
	
	public static void main(String[] args) {
		AntCell a = new AntCell(0, 512);
		HueTheme<AntCell> calat = new HueTheme<AntCell>();
		for (int i = 0; i < 512; i++) {
			System.out.println(calat.getColor(a));
			a.increment();
		}
	}
}
