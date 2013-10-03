package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import automata.cell.DiscreteCell;
import automata.cell.RealCell;
import automata.grid.CellPos;
import automata.grid.Grid;

public class VTKWriter {
	private int step;
	String prefix;
	XMLStreamWriter writer;
	
	public VTKWriter() {}
	
	public VTKWriter(String prefix) {
		this.prefix = prefix;
		this.step = 0;
	}
	
	public void printState(Grid<?> s) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(prefix + "." + step + ".vti"));
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fos, "UTF-8");
		}
		catch(XMLStreamException e) {
			throw new RuntimeException(e);
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			writeData(writer, s);
			writer.close();
			fos.close();
		}
		catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		step++;
	}
	
	protected void writeData(XMLStreamWriter writer, Grid<?> s) throws XMLStreamException {
		writer.writeStartDocument();
		writer.writeStartElement("VTKFile");
		writer.writeAttribute("type", "ImageData");
		writer.writeStartElement("ImageData");
		writer.writeAttribute("WholeExtent", "0 " + (s.getRange(0)) + " 0 " + (s.getRange(1)) + " 0 1");
		writer.writeAttribute("Origin", "0 0 0");
		writer.writeAttribute("Spacing", "1 1 1");
		writer.writeStartElement("Piece");
		writer.writeAttribute("Extent", "0 " + (s.getRange(0)) + " 0 " + (s.getRange(1)) + " 0 1");
		writer.writeStartElement("PointData");
		writer.writeEndElement();
		writer.writeStartElement("CellData");
		writer.writeStartElement("DataArray");
		writer.writeAttribute("Name", "CellState");
		writer.writeAttribute("type", "Int8");
		writer.writeAttribute("name", "cells");
		writer.writeAttribute("format", "ascii");
		for (int i = 0; i < s.getRange(1); i++) {
			for (int j = 0; j < s.getRange(0); j++) {
				if (s.getCell(new CellPos(j, i)) instanceof DiscreteCell<?>) {
					writer.writeCharacters((((DiscreteCell<?>) (s.getCell(new CellPos(j, i)))).get()+1) + " ");
				}
				else if (s.getCell(new CellPos(j, i)) instanceof RealCell<?>) {
					writer.writeCharacters((((RealCell<?>) (s.getCell(new CellPos(j, i)))).get()+1) + " ");
				}
			}
			writer.writeCharacters("\n");
		}			
		writer.writeEndDocument();
	}
	
	public void writeSummaryFile() {
		try {
			FileOutputStream fos = new FileOutputStream(new File(prefix + ".pvd"));
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fos, "UTF-8");
		}
		catch(XMLStreamException e) {
			throw new RuntimeException(e);
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		try {
			writer.writeStartDocument();
			writer.writeStartElement("VTKFile");
			writer.writeAttribute("type", "Collection");
			writer.writeAttribute("version", "0.1");
			writer.writeStartElement("Collection");
			
			for (int i = 0; i < step; i++) {
				File dataSet = new File(prefix + "." + i + ".vti");
				if (dataSet.exists()) {
					writer.writeEmptyElement("DataSet");
					writer.writeAttribute("timestep", Integer.toString(i));
					writer.writeAttribute("file", dataSet.getName());
				}
			}
			writer.writeEndDocument();
			writer.close();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
}
