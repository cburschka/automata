package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 * Dieses Fenster erweitert den Standard-JFrame.
 * Wird es erzeugt, dann erstellt es alle zusätzlichen UI-Elemente 
 * (Kontrollen, etc.) und fügt sie zu seinem eigenen Fenster hinzu.
 * @author burschka
 *
 */
public class InteractivePanel extends JPanel {
	private static final long serialVersionUID = 295357062628724259L;
	
	/*
	 * Diese Kontrollen sind im Objekt enthalten, damit
	 * von den eigenen Methoden darauf zugegriffen werden kann.
	 */
	private JTextField jt_width, jt_height;
	private JButton jb_start, jb_stop;
	private JLabel summary;
	private JSlider js_speed;
	private JRadioButton jrb_boundary_x_wrap, jrb_boundary_y_wrap, jrb_boundary_x_reflect, jrb_boundary_y_reflect;
	private GUIController controller;
	public JMenuItem grid_record;
	private Dimension lastKnownSize;
	public GridLoader gridLoader;
	WorldSwitcher worldSwitcher;
	
	/**
	 * Die Basis für die exponentielle Geschwindigkeitseinstellung.
	 * Ein Wert von 1 bedeutet eine lineare Skala.
	 */
	public static double SPEED_BASE = 2.0;
	
	/**
	 * Das Zeichenfenster, in dem die Matrix dargestellt wird.
	 */
	public StatePanel state;
	
	/**
	 * Erstelle ein neues Matrix-Fenster mit allen enthaltenen Elementen.
	 */
	public InteractivePanel(Dimension windowSize, Dimension gridSize, SimulatorWindow window) {
		lastKnownSize = windowSize;
		controller = new GUIController(this, window);
		controller.resize(gridSize);

		setLayout(new BorderLayout());
		add(getTopMenuBar(), BorderLayout.NORTH);
		add(getScrollPane());
		add(getControls(), BorderLayout.EAST);
		setRunning(false);

		setSize(lastKnownSize);
	}
	
	@Override
	public void paint(Graphics g) {
		if (!this.getSize().equals(lastKnownSize)) {
			lastKnownSize = this.getSize();
			state.checkResize();
		}
		super.paint(g);
	}
	
	/**
	 * Gibt die Anzahl der zu zeichnenden Objekte zurueck.
	 * @return ein Dimension-Objekt (zur einfacheren Rueckgabe beider Zahlen), 
	 *  oder null, wenn ein Fehler aufgetreten ist.  
	 */
	public Dimension getGridSize() {
		try {
			Dimension d = new Dimension(Integer.parseInt(jt_width.getText()), Integer.parseInt(jt_height.getText()));
			if (d.width <= 0 || d.height <= 0) {
				controller.setError("Negative Dimensionen.");
				return null;
			}
			return d;
		}
		catch (NumberFormatException e) {
			controller.setError("Ungueltige Dimensionen.");
			return null;
		}
	}
	
	/**
	 * Die Geschwindigkeit (Position des Sliders)
	 * @return Geschwindigkeit in Schritten pro Sekunde.
	 */
	public int getSpeed() {
		if (SPEED_BASE > 1) 
			return (int)(Math.pow(SPEED_BASE, js_speed.getValue()));
		else
			return js_speed.getValue();
	}
	
	/**
	 * Kontrolliere den Status der Start/Stop-Knöpfe.
	 * @param running
	 */
	public void setRunning(boolean running) {
		jb_start.setEnabled(!running);
		jb_stop.setEnabled(running);
	}
	
	/**
	 * Aktualisiere den beschreibenden Text. Er enthaelt den Typ der Welt, die Zahl
	 * der simulierten Generationen seit dem Reset, und eine Statistik ueber die
	 * Verteilung der verschiedenen Zellen. 
	 * @param world die Beschreibung des Welt-Typs.
	 * @param generation die Generationenzahl
	 * @param labels die Namen aller moeglichen Zustaende
	 * @param stats wieviele Zellen sich in jedem moeglichen Zustand befinden.
	 */
	public void updateSummary(String world, int generation, String...extra) {
		String summaryText = "<HTML>";
		summaryText += "<I>" + world + "</I><P>";
		summaryText += "<BOLD>Generation:</BOLD> " + generation + "<P>";
		for (int i = 0; i < extra.length; i++) {
			summaryText += extra[i] + "<P>";
		}
		String speed = "" + getSpeed();
		if (1000/getSpeed() == 0)
			speed = "\u221E";
		summaryText += speed + " pro Sekunde";
		this.summary.setText(summaryText);
	}
	
	public boolean[] getBoundaries() {
		return new boolean[] {
				jrb_boundary_x_wrap.isSelected(),
				jrb_boundary_y_wrap.isSelected()
		};
	}
	
	private JMenuBar getTopMenuBar() {
		JMenuBar jmb = new JMenuBar();
		jmb.add(getWorldMenu());
		jmb.add(getGridMenu());
		return jmb;
	}
	
	private JMenu getWorldMenu() {
		JMenu jm_switcher = new JMenu("Simulation waehlen");
		worldSwitcher = new WorldSwitcher(controller, this, jm_switcher);
		return jm_switcher;
	}
	
	private JMenu getGridMenu() {
		JMenu grid_menu = new JMenu("Zustand");
		gridLoader = new GridLoader(this, controller);
		JMenuItem grid_load = new JMenuItem("Laden");
		JMenuItem grid_save = new JMenuItem("Speichern");
		JMenuItem grid_program = new JMenuItem("Programm speichern");
		grid_record = new JMenuItem("Aufzeichnung starten...");
		grid_load.setActionCommand("load");
		grid_save.setActionCommand("save");
		grid_program.setActionCommand("program");
		grid_record.setActionCommand("record");
		grid_load.addActionListener(gridLoader);
		grid_save.addActionListener(gridLoader);
		grid_program.addActionListener(gridLoader);
		grid_record.addActionListener(gridLoader);
		grid_menu.add(grid_load);
		grid_menu.add(grid_save);
		grid_menu.add(grid_program);
		grid_menu.add(grid_record);
		return grid_menu;
	}
	
	private JScrollPane getScrollPane() {
		JScrollPane scroll = new JScrollPane(getStatePanel());
		scroll.setBackground(Color.LIGHT_GRAY);
		return scroll;
	}
	
	private StatePanel getStatePanel() {
		state = new StatePanel(controller);
		StatePanelListener spl = new StatePanelListener(state);
		state.addMouseListener(spl);
		state.addMouseMotionListener(spl);
		state.setBackground(Color.LIGHT_GRAY);
		return state;
	}
	
	private JPanel getControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=2;
		controls.add(getSpeedSlider(), gbc);
				
		gbc.gridx=0;
		gbc.gridy=1;
		controls.add(getBoundaryPicker(),gbc);
		controller.updateBounds();
		
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.gridwidth=2;
		controls.add(getCommandButtons(), gbc);
		
		gbc.gridx=0;
		gbc.gridy=3;
		controls.add(getSummary(), gbc);
		return controls;
	}
	
	private JSlider getSpeedSlider() {
		js_speed = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
		js_speed.setToolTipText("Geschwindigkeit: Logarithmische Skala");
		js_speed.setMajorTickSpacing(1);
		js_speed.setMinorTickSpacing(1);
		js_speed.setPaintTicks(true);
		js_speed.setSnapToTicks(true);
		js_speed.setSize(40, 10);
		js_speed.addChangeListener(controller);
		return js_speed;
	}
	
	private JPanel getBoundaryPicker() {
		JPanel boundaryPicker = new JPanel(); 
		ButtonGroup bg_boundaries_x = new ButtonGroup();
		ButtonGroup bg_boundaries_y = new ButtonGroup();
		jrb_boundary_x_wrap = new JRadioButton();
		jrb_boundary_x_reflect = new JRadioButton();
		jrb_boundary_y_wrap = new JRadioButton();
		jrb_boundary_y_reflect = new JRadioButton();
		jrb_boundary_x_wrap.setActionCommand("boundaries");
		jrb_boundary_x_reflect.setActionCommand("boundaries");
		jrb_boundary_y_wrap.setActionCommand("boundaries");
		jrb_boundary_y_reflect.setActionCommand("boundaries");
		jrb_boundary_x_wrap.addActionListener(controller);
		jrb_boundary_x_reflect.addActionListener(controller);
		jrb_boundary_y_wrap.addActionListener(controller);
		jrb_boundary_y_reflect.addActionListener(controller);
		bg_boundaries_x.add(jrb_boundary_x_wrap);
		bg_boundaries_x.add(jrb_boundary_x_reflect);
		bg_boundaries_y.add(jrb_boundary_y_wrap);
		bg_boundaries_y.add(jrb_boundary_y_reflect);
		boundaryPicker.setLayout(new GridLayout(3,3));
		boundaryPicker.add(new JPanel());
		boundaryPicker.add(new JLabel("X"));
		boundaryPicker.add(new JLabel("Y"));
		boundaryPicker.add(new JLabel("Periodisch"));
		boundaryPicker.add(jrb_boundary_x_wrap);
		boundaryPicker.add(jrb_boundary_y_wrap);
		boundaryPicker.add(new JLabel("Reflektiv"));
		boundaryPicker.add(jrb_boundary_x_reflect);
		boundaryPicker.add(jrb_boundary_y_reflect);
		
		jrb_boundary_y_wrap.setSelected(true);
		jrb_boundary_x_wrap.setSelected(true);
		return boundaryPicker;
	}
	
	/**
	 * Erzeugt alle Kontroll-Elemente.
	 * @param settings
	 */
	private JPanel getCommandButtons() {
		JPanel settings = new JPanel();
		
		// Erzeuge die Kontrollen.
		jt_width = new JTextField();
		jt_height = new JTextField();
		jt_width.setText("" + controller.getGridSize().width);
		jt_height.setText("" + controller.getGridSize().height);
		JButton jb_reset = new JButton("Neu");
		JButton jb_resize = new JButton("Neue Groesse");
		jb_start = new JButton("Start");
		jb_stop = new JButton("Stop");
		JButton jb_step = new JButton("Schritt");
		JButton jb_close = new JButton("Schliessen");
		jb_reset.addActionListener(controller);
		jb_reset.setActionCommand("reset");
		jb_resize.addActionListener(controller);
		jb_resize.setActionCommand("resize");
		jb_start.addActionListener(controller);
		jb_start.setActionCommand("start");
		jb_stop.addActionListener(controller);
		jb_stop.setActionCommand("stop");
		jb_step.addActionListener(controller);
		jb_step.setActionCommand("step");
		jb_close.addActionListener(controller);
		jb_close.setActionCommand("close");
		
		settings.setLayout(new GridLayout(5,2));
		settings.add(jt_width);
		settings.add(jt_height);
		settings.add(jb_reset);
		settings.add(jb_resize);
		settings.add(jb_start);
		settings.add(jb_stop);
		settings.add(jb_step);
		settings.add(jb_close);
		
		return settings; 
	}
	
	private JLabel getSummary() {
		summary = new JLabel();
		return summary;
	}
}
