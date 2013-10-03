package xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import ui.theme.Theme;
import automata.cell.Cell;
import automata.grid.Boundary;
import automata.grid.BoundaryCondition;
import automata.grid.CellPos;
import automata.grid.Grid;
import automata.grid.Grid2dMatrix;
import automata.grid.PeriodicBoundary;
import automata.grid.RecursiveGrid;
import automata.grid.ReflectiveBoundary;
import biz.source_code.base64Coder.Base64Coder;

public class CellSimProgramReader  {
	private int[] range;
	private BoundaryCondition[] boundaries;
	private Class<Cell<?>> stateClass;
	private Theme<?> theme;
	private int[][][] cellsInt;
	private long[][][] cellsLong;
	private double[][][] cellsDouble;
	private String[][][] cellsString;
	private byte[] serializedGrid;
	
	public CellSimProgramReader(File input) throws IOException, ClassNotFoundException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder builder=null;
		Document document=null;
		
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new FileInputStream(input));
		}
		catch (ParserConfigurationException pce) {
			System.out.println("ParserConfiguration-Exception:"+pce);
		}
		
		catch (SAXException saxe) {
			System.out.println("SAX-Exception: "+saxe);
		}
		
		catch (IOException ioex) {
			System.out.println("IO-Exception: " +ioex);
		}

		Element root = document.getDocumentElement();
		NodeList children=root.getChildNodes();
		
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element)child;
				if (childElement.getTagName().equals("Geometry")) {
					parseGeometry(childElement);
				}
				else if (childElement.getTagName().equals("Boundary")) {
					parseBoundary(childElement);
				}
				else if (childElement.getTagName().equals("State")) {
					parseState(childElement);
				}
				else if (childElement.getTagName().equals("Init")) {
					parseInit(childElement);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Grid initialize() throws IOException, ClassNotFoundException {
		Grid grid = null;
		if (serializedGrid == null) {
			Constructor<Cell<?>> stateConst;
			
			if (cellsInt != null) {
				try {
					stateConst = stateClass.getConstructor(int.class);
					if (range[2] == 0) {
						grid = new Grid2dMatrix(stateConst.newInstance(cellsInt[0][0][0]), range[0], range[1]);
					}
					else {
						grid = new RecursiveGrid(stateConst.newInstance(cellsInt[0][0][0]), range[0], range[1], range[2]);
					}
					for (int i = 0; i < range[0]; i++) {
						for (int j = 0; j < range[1]; j++) {
							for (int k = 0; k < Math.max(range[2], 1); k++) {
								grid.setCell(stateConst.newInstance(cellsInt[i][j][k]), new CellPos(i, j, k));
							}
						}
					}
				}
				catch (NoSuchMethodException e) {
				}
				catch (InvocationTargetException e) {
				}
				catch (IllegalAccessException e) {
				}
				catch (InstantiationException e) {
				}
			}
			else if (cellsLong != null) {
				try {
					stateConst = stateClass.getConstructor(long.class);
					if (range[2] == 0) {
						grid = new Grid2dMatrix(stateConst.newInstance(cellsLong[0][0][0]), range[0], range[1]);
					}
					else {
						grid = new RecursiveGrid(stateConst.newInstance(cellsLong[0][0][0]), range[0], range[1], range[2]);
					}
					for (int i = 0; i < range[0]; i++) {
						for (int j = 0; j < range[1]; j++) {
							for (int k = 0; k < Math.max(range[2], 1); k++) {
								grid.setCell(stateConst.newInstance(cellsLong[i][j][k]), new CellPos(i, j, k));
							}
						}
					}
				}
				catch (NoSuchMethodException e) {
				}
				catch (InvocationTargetException e) {
				}
				catch (IllegalAccessException e) {
				}
				catch (InstantiationException e) {
				}
			}
			else if (cellsDouble != null) {
				try {
					stateConst = stateClass.getConstructor(double.class);
					if (range[2] == 0) {
						grid = new Grid2dMatrix(stateConst.newInstance(cellsDouble[0][0][0]), range[0], range[1]);
					}
					else {
						grid = new RecursiveGrid(stateConst.newInstance(cellsDouble[0][0][0]), range[0], range[1], range[2]);
					}
					for (int i = 0; i < range[0]; i++) {
						for (int j = 0; j < range[1]; j++) {
							for (int k = 0; k < Math.max(range[2], 1); k++) {
								grid.setCell(stateConst.newInstance(cellsDouble[i][j][k]), new CellPos(i, j, k));
							}
						}
					}
				}
				catch (NoSuchMethodException e) {
				}
				catch (InvocationTargetException e) {
				}
				catch (IllegalAccessException e) {
				}
				catch (InstantiationException e) {
				}
			}
			else if (cellsString != null) {
				try {
					stateConst = stateClass.getConstructor(String.class);
					if (range[2] == 0) {
						grid = new Grid2dMatrix(stateConst.newInstance(cellsString[0][0][0]), range[0], range[1]);
					}
					else {
						grid = new RecursiveGrid(stateConst.newInstance(cellsString[0][0][0]), range[0], range[1], range[2]);
					}
					for (int i = 0; i < range[0]; i++) {
						for (int j = 0; j < range[1]; j++) {
							for (int k = 0; k < Math.max(range[2], 1); k++) {
								grid.setCell(stateConst.newInstance(cellsString[i][j][k]), new CellPos(i, j, k));
							}
						}
					}
				}
				catch (NoSuchMethodException e) {
				}
				catch (InvocationTargetException e) {
				}
				catch (IllegalAccessException e) {
				}
				catch (InstantiationException e) {
				}
			}
		}
		else {
			ByteArrayInputStream bais = new ByteArrayInputStream(serializedGrid);
			ObjectInputStream ois;
			ois = new ObjectInputStream(bais);
			grid = (Grid<?>)ois.readObject();
		}
		return grid;
	}
	
	public Boundary getBoundary() {
		return new Boundary(boundaries);
	}

	private void parseGeometry(Element geometry) {
		NodeList children = geometry.getChildNodes();
		for (int i=0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element)child;
				if (childElement.getTagName().equals("CellPos")) {
					String[] ranges = ((Text)(childElement.getFirstChild())).getData().split("\\s+");
					this.range = new int[ranges.length];
					for (int j = 0; j < ranges.length; j++) {
						this.range[j] = Integer.parseInt(ranges[j]);
					}
					return;
				}
			}
		}
	}
	
	private void parseBoundary(Element boundary) {
		if (range[2] == 0) {
			boundaries = new BoundaryCondition[2];			
		}
		else {
			boundaries = new BoundaryCondition[3];			
		}
		NodeList children = boundary.getChildNodes();
		for (int i=0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element)child;
				if (childElement.getTagName().equals("BoundarySegment")) {
					int id = Integer.parseInt(childElement.getAttribute("id"));
					String type = childElement.getAttribute("type");
					if (type.equals("reflect")) {
						boundaries[id] = new ReflectiveBoundary();
					}
					else if (type.equals("periodic")) {
						boundaries[id] = new PeriodicBoundary();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseState(Element state) throws IOException, ClassNotFoundException {
		String className = state.getAttribute("name");
		if (className != null && className.length() > 0) {
			String jarName = state.getAttribute("jar");
			if (jarName.length() != 0) {
				System.out.println(jarName);
				try {
					URLClassLoader cl = new URLClassLoader(new URL[] {new URL(jarName)});
					this.stateClass = (Class<Cell<?>>) cl.loadClass(className);
					NodeList themeElement = state.getElementsByTagName("Theme");
					for (int i = 0; i < themeElement.getLength(); i++) {
						Element tE = (Element)(themeElement.item(i));
						theme = ((Class<Theme>) cl.loadClass(tE.getAttribute("name"))).newInstance();
					}
				}
				catch (MalformedURLException err) {
					err.printStackTrace();
				}
				catch (ClassNotFoundException err) {
					err.printStackTrace();
				}
				catch (IllegalAccessException err) {
					err.printStackTrace();
				}
				catch (InstantiationException err) {
					err.printStackTrace();
				}
			}
			else {
				try {
					this.stateClass = (Class<Cell<?>>)(Class.forName(className));
					NodeList themeElement = state.getElementsByTagName("Theme");
					for (int i = 0; i < themeElement.getLength(); i++) {
						Element tE = (Element)(themeElement.item(i));
						theme = (		  
										  (Class<Theme>)
										  (
												  Class.forName(tE.getAttribute("name"))
										  )
								  
								).newInstance();
					}
				}
				catch (ClassNotFoundException e) {
					System.out.println("CNF-Exception: "+e);
				}
				catch (IllegalAccessException err) {}
				catch (InstantiationException err) {}
			}
		}
		else {
			NodeList children = state.getChildNodes();
			for (int i=0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					Element childElement = (Element)child;
					if (childElement.getTagName().equals("SerializedGrid")) {
						serializedGrid = Base64Coder.decode(childElement.getTextContent().replaceAll("\\s", ""));
					}
				}
			}			
		}
	}
	
	private void parseInit(Element init) {
		NodeList children = init.getChildNodes();
		for (int i=0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element)child;
				if (childElement.getTagName().equals("IntegerArray")) {
					cellsInt = new int[range[0]][range[1]][Math.max(range[2], 1)];
					String[] cellValues = ((Text)(childElement.getFirstChild())).getData().trim().split("\\s+");
					for (int col = 0; col < range[0]; col++) {
						for (int row = 0; row < range[1]; row++) {
							for (int plane = 0; plane < Math.max(range[2], 1); plane++) {
								int cellNumber = plane * range[1] * range[0] + row * range[0] + col;
								cellsInt[col][row][plane] = Integer.parseInt(cellValues[cellNumber]);
							}
						}
					}
					return;
				}
				else if (childElement.getTagName().equals("LongArray")) {
					cellsLong = new long[range[0]][range[1]][Math.max(range[2], 1)];
					String[] cellValues = ((Text)(childElement.getFirstChild())).getData().trim().split("\\s+");
					for (int col = 0; col < range[0]; col++) {
						for (int row = 0; row < range[1]; row++) {
							for (int plane = 0; plane < Math.max(range[2], 1); plane++) {
								int cellNumber = plane * range[1] * range[0] + row * range[0] + col;
								cellsLong[col][row][plane] = Long.parseLong(cellValues[cellNumber]);
							}
						}
					}
					return;
				}
				else if (childElement.getTagName().equals("IntegerArray")) {
					cellsDouble = new double[range[0]][range[1]][Math.max(range[2], 1)];
					String[] cellValues = ((Text)(childElement.getFirstChild())).getData().trim().split("\\s+");
					for (int col = 0; col < range[0]; col++) {
						for (int row = 0; row < range[1]; row++) {
							for (int plane = 0; plane < Math.max(range[2], 1); plane++) {
								int cellNumber = plane * range[1] * range[0] + row * range[0] + col;
								cellsDouble[col][row][plane] = Double.parseDouble(cellValues[cellNumber]);
							}
						}
					}
					return;
				}
				else if (childElement.getTagName().equals("StringArray")) {
					cellsString = new String[range[0]][range[1]][Math.max(range[2], 1)];
					String[] cellValues = ((Text)(childElement.getFirstChild())).getData().trim().split("\\s+");
					for (int col = 0; col < range[0]; col++) {
						for (int row = 0; row < range[1]; row++) {
							for (int plane = 0; plane < Math.max(range[2], 1); plane++) {
								int cellNumber = plane * range[1] * range[0] + row * range[0] + col;
								cellsString[col][row][plane] = cellValues[cellNumber];
							}
						}
					}
					return;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Theme getTheme() {
		return theme;
	}
}