package xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ui.theme.Theme;

import automata.cell.Cell;
import automata.cell.DiscreteCell;
import automata.cell.RealCell;
import automata.grid.Boundary;
import automata.grid.CellPos;
import automata.grid.FixedBoundary;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;
import automata.grid.RecursiveGrid;
import biz.source_code.base64Coder.Base64Coder;

public class CellSimProgramWriter {
	private XMLStreamWriter writer;
	
	public CellSimProgramWriter(OutputStream out) {
		try {
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out, "UTF-8");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
	}
	
	public CellSimProgramWriter(File outputFile) throws FileNotFoundException {
		this(new FileOutputStream(outputFile));
	}
	
	public <T extends Cell<T>> void writeProgram(Grid<T> grid, Boundary bounds, Theme<T> theme) throws IOException {
		try {
			T cell = grid.getCell(new CellPos(new int[grid.getDimension()]));
			writer.writeStartDocument();
			writer.writeStartElement("CellSimProgram");
			writer.writeStartElement("Geometry");
			writer.writeStartElement("CellPos");
			writer.writeCharacters(Integer.toString(grid.getRange(0)) + " ");
			writer.writeCharacters(Integer.toString(grid.getRange(1)) + " ");
			writer.writeCharacters(Integer.toString(grid.getDimension() > 2 ? grid.getRange(0) : 0));
			writer.writeEndElement();
			writer.writeEndElement();
			
			writer.writeStartElement("Boundary");
			if (bounds instanceof FixedBoundary<?>) {
				writer.writeEmptyElement("Fixed");
				writer.writeAttribute("state", ((FixedBoundary<?>)bounds).constant.toString());
			}
			else {
				for (int i = 0; i < bounds.conditions.length; i++) {
					writer.writeEmptyElement("BoundarySegment");
					writer.writeAttribute("id", Integer.toString(i));
					writer.writeAttribute("type", bounds.conditions[i].toString());
					
				}
			}
			writer.writeEndElement();
			
			writer.writeStartElement("State");
			if (grid instanceof Grid2dMatrix<?> || grid instanceof RecursiveGrid<?>) {
				writer.writeAttribute("name", cell.getClass().getCanonicalName());
				writer.writeEmptyElement("Theme");
				writer.writeAttribute("name", theme.getClass().getCanonicalName());
			}
			else {
				writer.writeStartElement("SerializedGrid");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(grid);
				writer.writeCharacters(Base64Coder.encodeLines(baos.toByteArray()));
				writer.writeEndDocument();
				return;
			}
			writer.writeEndElement();
			
			writer.writeStartElement("Init");
			if (cell instanceof DiscreteCell<?>) {
				writer.writeStartElement("IntegerArray");
				if (grid.getDimension() == 3) {
					for (int z = 0; z < grid.getRange(2); z++) {
						for (int y = 0; y < grid.getRange(1); y++) {
							for (int x = 0; x < grid.getRange(0); x++) {
								writer.writeCharacters(Integer.toString(((DiscreteCell<?>)grid.getCell(new CellPos(x,y,z))).get()) + " ");
							}
							writer.writeCharacters("\n");
						}
						writer.writeCharacters("\n");
					}
				}
				else if (grid.getDimension() == 2) {
					for (int y = 0; y < grid.getRange(1); y++) {
						for (int x = 0; x < grid.getRange(0); x++) {
							writer.writeCharacters(Integer.toString(((DiscreteCell<?>)grid.getCell(new CellPos(x,y))).get()) + " ");
						}
						writer.writeCharacters("\n");
					}
				}
			}
			else if (cell instanceof RealCell<?>) {
				writer.writeStartElement("DoubleArray");
				if (grid.getDimension() == 3) {
					for (int z = 0; z < grid.getRange(2); z++) {
						for (int y = 0; y < grid.getRange(1); y++) {
							for (int x = 0; x < grid.getRange(0); x++) {
								writer.writeCharacters(Double.toString(((RealCell<?>)grid.getCell(new CellPos(x,y))).get()) + " ");
							}
							writer.writeCharacters("\n");
						}
						writer.writeCharacters("\n");
					}
				}
				else if (grid.getDimension() == 2) {
					for (int y = 0; y < grid.getRange(1); y++) {
						for (int x = 0; x < grid.getRange(0); x++) {
							writer.writeCharacters(Integer.toString(((DiscreteCell<?>)grid.getCell(new CellPos(x,y))).get()) + " ");
						}
						writer.writeCharacters("\n");
					}
				}
			}
			else {
				writer.writeStartElement("StringArray");
				if (grid.getDimension() == 3) {
					for (int z = 0; z < (grid.getDimension() > 2 ? grid.getRange(2) : 1); z++) {
						for (int y = 0; y < grid.getRange(1); y++) {
							for (int x = 0; x < grid.getRange(0); x++) {
								writer.writeCharacters(grid.getCell(new CellPos(x,y,z)).toString() + " ");
							}
							writer.writeCharacters("\n");
						}
						writer.writeCharacters("\n");
					}
				}
				else if (grid.getDimension() == 2) {
					for (int y = 0; y < grid.getRange(1); y++) {
						for (int x = 0; x < grid.getRange(0); x++) {
							writer.writeCharacters(grid.getCell(new CellPos(x,y)).toString() + " ");
						}
						writer.writeCharacters("\n");
					}
				}
			}
			writer.writeEndDocument();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}		
	}
}
