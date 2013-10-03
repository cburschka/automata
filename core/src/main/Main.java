package main;

import java.awt.Dimension;

import ui.SimulatorWindow;
import automata.life.InvalidRuleException;

/**
 * Hauptklasse und Einstiegspunkt fuer das Programm.
 * @author burschka
 *
 */
public class Main {
	public static void main(String[] args) throws InvalidRuleException {
		SimulatorWindow window = new SimulatorWindow(new Dimension(660, 480), new Dimension(60, 60));
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
