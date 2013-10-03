package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.theme.Theme;
import xml.CellSimProgramReader;
import automata.CellularAutomaton;
import automata.bushfire.Bushfire;
import automata.cell.Cell;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.life.InvalidRuleException;
import automata.life.Life;
import automata.life.LifeVariant;
import controls.Controller;
import controls.DirectoryLoader;

/**
 * Selektiert die Welt, die zu simulieren ist.
 */
public class WorldSwitcher implements ActionListener {
	Controller controller;
	InteractivePanel panel;
	JFileChooser fc;
	DirectoryLoader dirloader;
	JMenu menu;
	private static final int BASE_COUNT = 4;
	private static final String SUBDIR = ".cellsim";
	
	List<Class<CellularAutomaton<?>>> classes;
	
	public WorldSwitcher(Controller controller, InteractivePanel panel, JMenu menu) {
		this.menu = menu;
		this.panel = panel;
		this.controller = controller;
		
		// Füge die Standard-Plugins ein.
		menu.add("Buschfeuer").setActionCommand("bushfire");
		menu.add("Life").setActionCommand("conway");
		menu.add("Life - Custom").setActionCommand("conwayvar");
		menu.add("Datei laden...").setActionCommand("jar");
		for (int i = 0; i < menu.getItemCount(); i++) {
			menu.getItem(i).addActionListener(this);
		}
		
		dirloader = new DirectoryLoader(new File(System.getProperty("user.home") + "/" + SUBDIR));
		refreshMenuPlugins();
		
		fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("JAR-Archive oder XML-Programme", "jar", "xml"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		CellularAutomaton<?> automaton = null;
		if (e.getActionCommand().equals("bushfire")) {
			automaton = new Bushfire();
		} else if (e.getActionCommand().equals("conway")) {
			automaton = new Life();
		} else if (e.getActionCommand().equals("conwayvar")) {
			try {
				String rules = (String)JOptionPane.showInputDialog(
	                    null,
	                    "Life-Variante (Standard-Regelnotation)",
	                    "Life-Varianten",
	                    JOptionPane.QUESTION_MESSAGE,
	                    null,
	                    null,
	                    "B3/S23");
				if (rules != null)
					automaton = new LifeVariant(rules);
			}
			catch (InvalidRuleException ex) {
				controller.setError("Die Eingabe hat kein gueltiges Format. Die Regel muss die Form B{0-9}/S{0-9} haben.");
				return;
			}
		} else if (e.getActionCommand().matches("[0-9]+")) {
			int i = Integer.parseInt(e.getActionCommand());
			try {
				automaton = classes.get(i).newInstance();
			} catch (InstantiationException e1) {
				controller.setError("Automat " + menu.getComponent(BASE_COUNT + i).getName() + " konnte nicht geladen werden.");
			} catch (IllegalAccessException e1) {
				controller.setError("Automat " + menu.getComponent(BASE_COUNT + i).getName() + " konnte nicht geladen werden.");
			}
		}
		else {
			try {
				if (fc.showOpenDialog(panel) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File loadFile = fc.getSelectedFile();
				if (loadFile.getName().substring(loadFile.getName().length()-4).toLowerCase().equals(".jar")) {
					dirloader.addURL(loadFile.toURI().toURL());
					if (!refreshMenuPlugins()) {
						controller.setError("Fehler beim Laden des Plugins");
					}
				}
				else {
					final CellSimProgramReader cspr = new CellSimProgramReader(loadFile);
					final Grid csprGrid = cspr.initialize();
					System.out.println(csprGrid);
					automaton = new CellularAutomaton() {
						@Override
						public String getDescription() {
							return "XML-Programm";
						}

						public String[] getSummary(Grid grid) {
							return new String[0];
						}

						@Override
						public Theme getTheme() {
							Theme theme = cspr.getTheme();
							if (theme == null) {
								theme = new Theme() {
									@Override
									public Color getColor(Cell cell) {
										// TODO Auto-generated method stub
										return null;
									}
								};
							}
							return theme;
						}

						@Override
						public Cell getZeroState() {
							Grid grid;
							try {
								grid = cspr.initialize();
								return grid.getCell(new CellPos(new int[grid.getDimension()]));
							} catch (IOException e) {
								controller.setError("Fehler beim Laden des Zustands");
							} catch (ClassNotFoundException e) {
								controller.setError("Das XML-Programm bezieht sich auf eine nicht verfügbare Klasse.");
							}
							return null;
						}

						@Override
						public Grid initialize(int... dimensions) {
							try {
								Grid grid = cspr.initialize();
								System.out.println(grid);
								return grid;
							} catch (IOException e) {
								controller.setError("Fehler beim Laden des Zustands");
							} catch (ClassNotFoundException e) {
								controller.setError("Das XML-Programm bezieht sich auf eine nicht verfügbare Klasse.");
							}
							return null;
						}

						@Override
						public Cell readCellState(String str) {
							return null;
						}
					};
					controller.setAutomaton(automaton);
					controller.setBounds(cspr.getBoundary());
					controller.setStatus("Automat gewechselt");
					return;
				}
			}
			catch (MalformedURLException err) {
				controller.setError("Dieser Dateipfad ist ungueltig.");
			}
			catch (IOException err) {
				controller.setError("Fehler beim Lesen der XML-Programmdatei");
			}
			catch (ClassNotFoundException err) {
				err.printStackTrace();
				controller.setError("Die XML-Programmdatei bezieht sich auf eine Klasse, die nicht verfügbar ist.");
			}
		}
		if (automaton != null) {
			controller.setStatus("Automat gewechselt");
			controller.setAutomaton(automaton);
		}
	}

	private boolean refreshMenuPlugins() {
		List<Class<CellularAutomaton<?>>> newClasses = dirloader.getClasses();
		if (newClasses.equals(classes)) {
			return false;
		}
		classes = newClasses;
		while (menu.getComponentCount() > BASE_COUNT) {
			menu.remove(BASE_COUNT + 1);
		}
		boolean added = false;
		menu.addSeparator();
		for (int i = 0; i < classes.size(); i++) {
			try {
				String name = (String) classes.get(i).getMethod("getName").invoke(null);
				JMenuItem item = menu.add(name);
				item.setActionCommand(Integer.toString(i));
				item.addActionListener(this);
				added = true;
			}
			catch (Exception e) {System.err.println("Plugin " + classes.get(i) + " does not implement public String getName() and was ignored.");}
		}
		if (!added) {
			menu.remove(BASE_COUNT);
		}
		return added;
	}
}
