import java.io.Serializable;

import automata.grid.CellPos;


public class Ant implements Serializable {
	private static final long serialVersionUID = -3076434370047807135L;
	private boolean[] rule;
	private int heading;
	private CellPos position;
	
	public Ant(boolean[] rule, CellPos position, int heading) {
		this.rule = rule;
		this.position = position;
		this.heading = heading;
	}
	
	void turn(int color) {
		heading = (4 + heading + (rule[color] ? 1 : -1)) % 4;
	}
	
	/**
	 *  0
	 * 3 1
	 *  2
	 */
	void forward(int width, int height) {
		int x = position.getCoordinate(0);
		int y = position.getCoordinate(1);
		if (heading % 2 > 0)
			x += 2 - heading;
		else
			y += heading - 1;
		
		x = (width + x) % width;
		y = (height + y) % height;
		this.position = new CellPos(x, y);
	}
	
	public CellPos getPosition() {
		return position;
	}
	
	public int getHeading() {
		return heading;
	}
	
	public int getRange() {
		return rule.length;
	}
}
