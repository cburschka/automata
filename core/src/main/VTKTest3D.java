package main;

import java.io.FileNotFoundException;

import xml.VTKWriter3D;
import automata.bushfire.Bushfire;
import automata.bushfire.BushfireCell;
import automata.grid.Boundary;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.ReflectiveBoundary;

public class VTKTest3D {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//VTKWriter vw = new VTKWriter(System.out);
		Bushfire bf = new Bushfire();
		Grid<BushfireCell> s = bf.initialize(10, 10, 10);
		s.setCell(Bushfire.FIRE, new CellPos(5, 5, 0));
		s.setCell(Bushfire.RECOVER, new CellPos(5, 6, 0));

		Boundary bounds = new Boundary(new ReflectiveBoundary(), new ReflectiveBoundary(), new ReflectiveBoundary());
		for (int i = 0; i < 100; i++) {
			VTKWriter3D vw = new VTKWriter3D("./vtk/bushfire-" + i + ".vti");
			System.out.println(i);
			vw.printState(s);
			s.transition(bounds);
		}
	}

}
