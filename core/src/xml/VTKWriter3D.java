package xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import automata.cell.DiscreteCell;
import automata.cell.RealCell;
import automata.grid.CellPos;
import automata.grid.Grid;

public class VTKWriter3D extends VTKWriter {
	XMLStreamWriter writer;
	
	public VTKWriter3D(String prefix) {
		super(prefix);
	}
	
	@Override
	protected void writeData(XMLStreamWriter writer, Grid<?> s) throws XMLStreamException {
		try {
			writer.writeStartDocument();
			writer.writeStartElement("VTKFile");
			writer.writeAttribute("type", "ImageData");
			writer.writeStartElement("ImageData");
			writer.writeAttribute("WholeExtent", "0 " + (s.getRange(0)) + " 0 " + (s.getRange(1)) + " 0 " + (s.getRange(2)));
			writer.writeAttribute("Origin", "0 0 0");
			writer.writeAttribute("Spacing", "1 1 1");

			writer.writeStartElement("Piece");
			writer.writeAttribute("Extent", "0 " + (s.getRange(0)) + " 0 " + (s.getRange(1)) + " 0 " + (s.getRange(2)));
			writer.writeStartElement("PointData");
			writer.writeEndElement();
			writer.writeStartElement("CellData");
			writer.writeStartElement("DataArray");
			writer.writeAttribute("Name", "CellState");
			writer.writeAttribute("type", "Int8");
			writer.writeAttribute("name", "cells");
			writer.writeAttribute("format", "ascii");
			
			for (int k = 0; k < s.getRange(2); k++) {
				for (int i = 0; i < s.getRange(1); i++) {
					for (int j = 0; j < s.getRange(0); j++) {
						CellPos position = new CellPos(i, j, k);
						if (s.getCell(position) instanceof DiscreteCell<?>) {
							writer.writeCharacters((((DiscreteCell<?>) (s.getCell(position))).get()+1) + " ");
						}
						else if (s.getCell(position) instanceof RealCell<?>) {
							writer.writeCharacters((((RealCell<?>) (s.getCell(position))).get()+1) + " ");
						}
					}
					writer.writeCharacters("\n");
				}
			}
			writer.writeEndDocument();
			writer.close();
		}
		catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
}
