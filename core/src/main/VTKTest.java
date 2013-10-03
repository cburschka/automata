package main;

import java.io.FileNotFoundException;

import xml.VTKWriter;
import automata.bushfire.Bushfire;
import automata.bushfire.BushfireCell;
import automata.grid.Boundary;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.ReflectiveBoundary;

public class VTKTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Bushfire b = new Bushfire();
		Grid<BushfireCell> bfstate = b.initialize(100, 100);
		bfstate.setCell(Bushfire.FIRE, new CellPos(50, 50));
		bfstate.setCell(Bushfire.RECOVER, new CellPos(50, 51));
		/*State s = new ConwayState(100, 100);
		s.setCell(50, 50, ConwayState.ALIVE);
		s.setCell(50, 51, ConwayState.ALIVE);
		s.setCell(50, 52, ConwayState.ALIVE);
		s.setCell(49, 51, ConwayState.ALIVE);
		s.setCell(51, 52, ConwayState.ALIVE);*/
		Boundary bounds = new Boundary(new ReflectiveBoundary(), new ReflectiveBoundary());
		for (int i = 0; i < 100; i++) {
			VTKWriter vw = new VTKWriter("./vtk/testvtk-" + i + ".vti");
			System.out.println(i);
			vw.printState(bfstate);
			bfstate.transition(bounds);
		}
	}

}
