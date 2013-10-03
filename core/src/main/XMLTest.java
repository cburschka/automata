package main;

import java.io.File;
import java.io.IOException;

import xml.CellSimProgramReader;
import automata.grid.Boundary;
import automata.grid.Grid;

public class XMLTest {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		CellSimProgramReader cspr = new CellSimProgramReader(new File("../life-12-4.xml"));
		Grid<?> g = cspr.initialize();
		Boundary bounds = cspr.getBoundary();
		System.out.println(g);
		for (int i = 0; i < 10; i++) {
			g.transition(bounds);
			System.out.println(g);
		}
	}

}
