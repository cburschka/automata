package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import xml.CellSimProgramWriter;
import xml.VTKWriter;
import automata.grid.CellPos;
import automata.grid.Grid;

public class GridLoader implements ActionListener {
	private JFileChooser fc;
	private InteractivePanel panel;
	private GUIController controller;
	private VTKWriter writer;
	private boolean recording;
	
	public GridLoader(InteractivePanel panel, GUIController ctrl) {
		this.panel = panel;
		this.controller = ctrl;
		fc = new JFileChooser();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("load")) {
			loadFile();
		}
		else if (arg0.getActionCommand().equals("save")) {
			saveFile();
		}
		else if (arg0.getActionCommand().equals("program")) {
			saveProgram();
		}
		else if (arg0.getActionCommand().equals("record")) {
			if (recording) {
				stopRecording();
			}
			else {
				startRecording();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void loadFile() {
		if (fc.showOpenDialog(panel) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File loadFile = fc.getSelectedFile();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(loadFile));
			List<String[]> dataGrid = new ArrayList<String[]>();
			String[] line = reader.readLine().split("\\s+");
			int width = line.length;
			dataGrid.add(line);
			while (reader.ready()) {
				line = reader.readLine().split("\\s+");
				if (width != line.length) {
					throw new IOException();
				}
				dataGrid.add(line);
			}
			Grid grid = controller.getAutomaton().initialize(width, dataGrid.size());
			for (int i = 0; i < dataGrid.size(); i++) {
				line = dataGrid.get(i);
				for (int j = 0; j < width; j++) {
					grid.setCell(controller.getAutomaton().readCellState(line[j]), new CellPos(j, i));
				}
			}
			controller.setGrid(grid);
			controller.setStatus("Brett geladen");
		}
		catch (FileNotFoundException e) {
			controller.setError("Die angegebene Datei konnte nicht gefunden werden.");
		}
		catch (IOException e) {
			controller.setError("Die angegebene Datei entspricht nicht dem erforderlichen Format.");
		}
		catch (Exception e) {
			controller.setError("Die angegebene Datei entspricht nicht dem erforderlichen Format.");
		}
	}
	
	private void saveFile() {
		if (fc.showSaveDialog(panel) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File saveFile = fc.getSelectedFile();
		try {
			PrintStream writer = new PrintStream(new FileOutputStream(saveFile));
			for (int i = 0; i < controller.getGridSize().height; i++) {
				writer.print(controller.getGrid().getCell(new CellPos(0, i)));
				for (int j = 1; j < controller.getGridSize().width; j++) {
					writer.print(" " + controller.getGrid().getCell(new CellPos(j, i)));
				}
				writer.println();
			}
			controller.setStatus("Brett gespeichert");
		}
		catch (FileNotFoundException e) {
			controller.setError("Die angegebene Datei konnte nicht erstellt werden.");
		}
	}
	
	public boolean isRecording() {
		return recording;
	}
	
	private void startRecording() {
		if (fc.showSaveDialog(panel) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		writer = new VTKWriter(fc.getSelectedFile().toString());
		panel.grid_record.setText("Aufzeichnung stoppen");
		recording = true;
	}
	
	private void stopRecording() {
		writer.writeSummaryFile();
		panel.grid_record.setText("Aufzeichnung starten...");
		recording = false;
	}
	
	public void recordStep() {
		writer.printState(controller.getGrid());
	}
	
	@SuppressWarnings("unchecked")
	private void saveProgram() {
		if (fc.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) {
			try {
				CellSimProgramWriter writer = new CellSimProgramWriter(fc.getSelectedFile());
				writer.writeProgram(controller.getGrid(), controller.getBounds(), controller.getAutomaton().getTheme());
				controller.setStatus("Programmdatei geschrieben.");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				controller.setError("Die Datei ist nicht schreibbar oder konnte nicht angelegt werden.");
			} catch (IOException e) {
				e.printStackTrace();
				controller.setError("Fehler beim Schreiben der Datei.");
			}
		}
	}
}
