package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import automata.CellularAutomaton;
import automata.cell.Cell;
import automata.grid.Boundary;
import automata.grid.BoundaryCondition;
import automata.grid.Grid;
import automata.grid.PeriodicBoundary;
import automata.grid.ReflectiveBoundary;
import controls.Controller;
import controls.Simulator;

/**
 * Implementierung des Simulations-Controllers fuer ein grafisches Fenster.
 * @author burschka
 */
public class GUIController extends Controller implements ActionListener, ChangeListener {
	private InteractivePanel panel;
	private SimulatorWindow window;
	private Simulator simulator;
	private CellularAutomaton<?> automaton;
	private Boundary bounds;
	private Dimension gridSize;
	@SuppressWarnings("unchecked")
	private Grid grid;
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// Diese Methode wird nur aufgerufen, wenn der Slider sich bewegt.
		refreshView();
	}
	
	public GUIController(InteractivePanel panel, SimulatorWindow window) {
		this.panel = panel;
		this.window = window;
		this.simulator = new Simulator(this);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("close")) {
				System.exit(0);
			}
		else if (automaton == null) {
			setError("Es ist noch keine Simulation ausgewählt.");
			return;
		}
		else if (a.getActionCommand().equals("step")) {
			simulator.step();
		}
		else if (a.getActionCommand().equals("start")) {
			start();
		}
		else if (a.getActionCommand().equals("stop")) {
			stop();
		}
		else if (a.getActionCommand().equals("reset")) {
			reset();
		}
		else if (a.getActionCommand().equals("resize")) {
			Dimension c = panel.getGridSize();
			if (c != null) {
				resize(c);
			}
		}
		else if (a.getActionCommand().equals("boundaries")) {
			updateBounds();
		}
	}
	
	@Override
	public void reset() {
		stop();
		grid = automaton.initialize(grid.getRange(0), grid.getRange(1));
		gridSize = new Dimension(grid.getRange(0), grid.getRange(1));
		panel.state.checkResize();
		simulator.resetGeneration();
		window.setStatus("Brett geleert.");
		refreshView();
	}
	
	@Override
	public void resize(Dimension size) {
		this.gridSize = size;
		if (grid != null) {
			size_redraw();
		}
	}
	
	private void size_redraw() {
		grid = grid.resize(gridSize.width, gridSize.height);
		panel.state.checkResize();
		simulator.resetGeneration();
		window.setStatus(String.format("Brettgroesse geaendert auf %dx%d.", gridSize.width, gridSize.height));
		refreshView();
	}

	@Override
	public Grid<?> getGrid() {
		return grid;
	}
	
	@Override
	public int getSpeed() {
		return panel.getSpeed();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refreshView() {
		if (!simulator.isRunning() || 2*simulator.getGeneration() % panel.getSpeed() == 0) {
			panel.updateSummary(automaton.getDescription(), simulator.getGeneration(), automaton.getSummary(grid));
		}
		panel.state.repaint();
		if (panel.gridLoader.isRecording()) {
			panel.gridLoader.recordStep();
		}
	}
	
	@Override
	public void started() {
		panel.setRunning(true);
		window.setStatus("Simulation gestartet...");
	}
	
	@Override
	public void stopped() {
		panel.setRunning(false);
		refreshView();
		window.setStatus("Stabiler Zustand: Simulation automatisch gestoppt.");
	}
	
	@Override
	public void setError(String message) {
		window.setError(message);
	}
	
	@Override
	public void setStatus(String message) {
		window.setStatus(message);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Cell<T>> void setAutomaton(CellularAutomaton<T> automaton) {
		stop();
		this.automaton = automaton;
		this.grid = automaton.initialize(gridSize.width, gridSize.height);
		panel.updateSummary(automaton.getDescription(), 0, automaton.getSummary((Grid<T>) grid));
		panel.state.checkResize();
		refreshView();
	}
	
	@Override
	public void start() {
		Thread t = new Thread(simulator);
		t.start();
	}
	
	@Override
	public void stop() {
		if (simulator.isRunning()) {
			panel.setRunning(false);
			simulator.stop();
			window.setStatus("Simulation gestoppt.");
		}
	}
	
	@Override
	public Boundary getBounds() {
		return bounds;
	}
	
	@Override
	public void setBounds(Boundary bounds) {
		this.bounds = bounds;
	}
	
	@Override
	public <T extends Cell<T>> void setGrid(Grid<T> grid) {
		this.grid = grid;
		gridSize = new Dimension(grid.getRange(0), grid.getRange(1));
		panel.state.checkResize();
		simulator.resetGeneration();
		refreshView();
	}
	
	void updateBounds() {
		boolean[] selection = panel.getBoundaries();
		BoundaryCondition x = selection[0] ? new PeriodicBoundary() : new ReflectiveBoundary();
		BoundaryCondition y = selection[1] ? new PeriodicBoundary() : new ReflectiveBoundary();
		setBounds(new Boundary(x, y));
	}
	
	@SuppressWarnings("unchecked")
	public CellularAutomaton getAutomaton() {
		return automaton;
	}
	
	@Override
	public Dimension getGridSize() {
		return gridSize;
	}
}