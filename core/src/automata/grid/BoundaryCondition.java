package automata.grid;

public abstract class BoundaryCondition {
	public BoundaryCondition() {}
	
	public abstract int map(int range, int coordinate);
}
