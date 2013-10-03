package automata.grid;


public class ReflectiveBoundary extends BoundaryCondition {
	public ReflectiveBoundary() {}
	
	@Override
	public int map(int range, int coordinate) {
		// This boundary condition is periodic over twice the range.
		coordinate = ((coordinate) % (2*range) + 2*range) % (2*range);
		return (coordinate >= range ? 2*range - coordinate - 1 : coordinate);
	}
	
	@Override
	public String toString() {
		return "reflective";
	}
}
