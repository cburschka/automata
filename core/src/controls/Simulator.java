package controls;

import automata.grid.Boundary;


/**
 * Simulator, der in einem anderen Thread laeuft. 
 * @author burschka
 *
 */
public class Simulator implements Runnable {
	private Controller controller;
	private boolean running;
	private int generation;

	public Simulator(Controller c) {
		controller = c;
		generation = 0;
	}
	
	@Override
	public void run() {
		controller.started();
		running = true;
		while (running) {
			if (!step()) {
				running = false;
				controller.stopped();
			}
			try {
				Thread.sleep(Math.max(0, 1000 / controller.getSpeed()));
			} catch(InterruptedException e) {
				running = false;
				controller.stopped();
			}
		}
	}
	
	/**
	 * Iteriert genau einmal.
	 * @return true falls der neue Zustand anders ist als der alte.
	 */
	public boolean step() {
		boolean changed = controller.getGrid().transition((Boundary)(controller.getBounds()));
		generation++;
		controller.refreshView();
		return changed;
	}
	
	public void stop() {
		running = false;
		controller.refreshView();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void resetGeneration() {
		generation = 0;
	}
	
	public int getGeneration() {
		return generation;
	}
}
