package automata.cell;

public abstract class RealCell<TCellState extends RealCell<TCellState>> implements Cell<TCellState> {
	private static final long serialVersionUID = 1196748342062419384L;
	private double current, future, stepSize;

	public RealCell(double current, double stepSize) {
		this.stepSize = stepSize;
		this.current = Math.min(current, getRange());
	}
	
	public double get() {
		return current;
	}
	
	public void set(double value) {
		future = Math.min(getRange(), Math.max(0, value));
	}
	
	@Override
	public boolean commit() {
		if (current != future) {
			current = future;
			return true;
		}
		else return false;
	}
	
	@Override
	public void increment() {
		current = Math.min(current + stepSize, getRange());
	}
	
	@Override
	public void decrement() {
		current = Math.max(current - stepSize, 0);
	}
	
	public abstract double getRange();
	
	public double getStepSize() {
		return stepSize;
	}
	
	@Override
	public abstract TCellState copy();
	
	@Override
	public String toString() {
		return Double.toString(current);
	}

}
