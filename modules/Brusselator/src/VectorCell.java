import automata.cell.Cell;

/**
 * 
 */

/**
 * @author christoph
 *
 */
public abstract class VectorCell<T extends VectorCell<T>> implements Cell<T> {
	private double[] current;
	private double[] future;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3399178234487378245L;

	/**
	 * 
	 */
	public VectorCell(double... vector) {
		current = new double[vector.length];
		future = new double[vector.length];
		for (int i = 0; i < vector.length; i++) {
			current[i] = vector[i];
			future[i] = vector[i];
		}
	}

	public void set(double... vector) {
		for (int i = 0; i < vector.length || i < future.length; i++) {
			future[i] = vector[i];
		}
	}
	
	public double[] get() {
		return current;
	}
	
	public boolean commit() {
		boolean changed = false;
		for (int i = 0; i < current.length; i++) {
			changed = changed || current[i] != future[i];
			current[i] = future[i];
		}
		return changed;
	}
	

	@Override
	public void increment() {
		current[0]+=0.2;
	}

	@Override
	public void decrement() {
		current[1]+=0.2;
	}
}
