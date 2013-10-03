package automata.grid;

public class DimensionMismatchException extends GridException {
	private static final long serialVersionUID = -8001659475775610616L;
	private int good, bad;

	public DimensionMismatchException(int realDimension, int badDimension) {
		good = realDimension;
		bad = badDimension;
	}
	
	@Override
	public String getMessage() {
		return "Attempted to access a grid of dimension " + good + " at a position with dimension " + bad + ".";
	}
	
	public static void main(String[] args) throws GridException {
		throw new DimensionMismatchException(2, 3);
	}
}
