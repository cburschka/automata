package automata.grid;

public class PeriodicBoundary extends BoundaryCondition {
	public PeriodicBoundary() {}

	@Override
	public int map(int range, int coordinate) {
		return (range + (coordinate % range)) % range;
	}
	
	@Override
	public String toString() {
		return "periodic";
	}
}
