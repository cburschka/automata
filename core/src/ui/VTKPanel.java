package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import xml.CellSimProgramReader;
import xml.VTKWriter;
import xml.VTKWriter3D;
import automata.grid.Boundary;
import automata.grid.Grid;

public class VTKPanel extends JPanel {
	private static final long serialVersionUID = 3178507843779296212L;
	private JTextField txtSchritte;
	private JTextField txtInput;
	private JTextField txtOutput;
	private JFileChooser fcInput, fcOutput;
	public Grid<?> grid;
	public Boundary bounds;

	/**
	 * Create the frame.
	 */
	public VTKPanel(SimulatorWindow rootFrame) {
		fcInput = new JFileChooser();
		fcInput.setFileFilter(new FileNameExtensionFilter("XML-Dateien", "xml"));
		fcOutput = new JFileChooser();
		final VTKPanel contentPane = this;
		final SimulatorWindow window = rootFrame;
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();

		JLabel lblXmlfile = new JLabel("Schritte:");
		add(lblXmlfile, gbc);

		gbc.gridx = 1;
		
		txtSchritte = new JTextField();
		add(txtSchritte, gbc);
		txtSchritte.setColumns(10);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		add(new JLabel("Eingabe"), gbc);

		gbc.gridx = 1;
		txtInput = new JTextField();
		contentPane.add(txtInput, gbc);
		txtInput.setColumns(10);

		
		gbc.gridx = 2;
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (fcInput.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {	
					File xmlFile = fcInput.getSelectedFile();
					contentPane.txtInput.setText(xmlFile.toString());
				}
			}
		});
		contentPane.add(btnBrowse, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;		
		contentPane.add(new JLabel("Ausgabe"), gbc);
		
		gbc.gridx = 1;
		txtOutput = new JTextField();
		contentPane.add(txtOutput, gbc);
		txtOutput.setColumns(10);

		gbc.gridx = 2;
		JButton btnSpeichern = new JButton("Browse");				
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (fcOutput.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					contentPane.txtOutput.setText(fcOutput.getSelectedFile().toString());
				}
			}
		});
		contentPane.add(btnSpeichern, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						File xmlFile = new File(txtInput.getText());
						try {
							CellSimProgramReader cspr = new CellSimProgramReader(xmlFile);
							contentPane.grid = cspr.initialize();
							contentPane.bounds = cspr.getBoundary();
						} catch (IOException e) {
							window.setError("Fehler beim Lesen der Programm-Datei.");
							return;
						} catch (ClassNotFoundException e) {
							window.setError("Dieses Programm bezieht sich auf eine Klasse, die nicht gefunden wurde.");
							return;
						}
						int steps = contentPane.getSteps();
						String filename = contentPane.txtOutput.getText();
						VTKWriter writer;
						if (contentPane.grid.getDimension() == 3) {
							writer = new VTKWriter3D(filename);
						}
						else {
							writer = new VTKWriter(filename);
						}
						window.setStatus("Simulation läuft...");
						for (int i = 0; i < steps; i++) {
							window.setStatus("Simuliere Schritt " + i);
							writer.printState(contentPane.grid);
							if (i < steps) {
								contentPane.grid.transition(contentPane.bounds);
							}
						}
						writer.writeSummaryFile();
						window.setStatus("Simulation abgeschlossen.");						
					}
				}).start();
			}
		});
		contentPane.add(btnStart, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		JButton btnClose = new JButton("Schliessen");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		contentPane.add(btnClose, gbc);		
	}
	
	public int getSteps() {
		return Integer.parseInt(txtSchritte.getText());
	}
}
