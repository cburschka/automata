package automata.cell;



public abstract class DiscreteCell<TCellState extends DiscreteCell<TCellState>> implements Cell<TCellState> {
	private static final long serialVersionUID = 8409494898297420175L;
	private int current, future;

	public DiscreteCell(int current) {
		this.current = current % getRange();
		this.future = this.current;
	}
	
	public DiscreteCell(int current, int range) {
		this.current = current % range;
	}
	
	public int get() {
		return current;
	}
	
	public void set(int value) {
		this.future = value % getRange();
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
		this.current = (current + 1) % getRange();
		this.future = this.current;
	}
	
	@Override
	public void decrement() {
		this.current = (current + getRange() - 1) % getRange();
		this.future = this.current;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DiscreteCell<?>))
			return false;
		return ((DiscreteCell<?>)obj).get() == get();
	}
	
	public abstract int getRange();
	
	public abstract TCellState copy();
	
	@Override
	public String toString() {
		return Integer.toString(current);
	}
}
