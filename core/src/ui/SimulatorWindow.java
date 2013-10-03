package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;

public class SimulatorWindow extends JFrame {
	private static final long serialVersionUID = 320480792939234637L;
	private JLabel status;
	
	public SimulatorWindow(Dimension windowSize, Dimension gridSize) {
		// Beende das Programm, wenn das Fenster geschlossen wird.
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();		
		tabbedPane.addTab("Interaktiv", new InteractivePanel(windowSize, gridSize, this));
		tabbedPane.addTab("Batch-Modus", new VTKPanel(this));
		
		add(tabbedPane, BorderLayout.CENTER);
		add(getStatusBar(), BorderLayout.SOUTH);
		
		setTitle("Simulator");		
		setStatus("Programm gestartet.");
		this.pack();		
		this.setSize(windowSize);
	}
	
	private JLabel getStatusBar() {
		status = new JLabel();
		status.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return status;
	}
	
	/**
	 * Schreibt einen Fehler in die Status-Zeile. Er wird rot formatiert.
	 * @param message der auszugebende Fehler.
	 */
	public void setError(String message) {
		status.setText(message);
		status.setForeground(Color.red);
	}
	
	/**
	 * Schreibt in die Status-Zeile. Der Status wird als "erfolgreiche Aktion"
	 * formatiert, mit schwarzer Textferbe. 
	 * @param message die auszugebende Nachricht.
	 */
	public void setStatus(String message) {
		status.setText(message);
		status.setForeground(Color.black);
	}
}
