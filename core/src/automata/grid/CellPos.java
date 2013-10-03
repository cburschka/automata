package automata.grid;

import java.io.Serializable;

public class CellPos implements Serializable {
	private static final long serialVersionUID = 1128437724196377224L;
	private int[] coordinates;
		
	public CellPos(int... coordinates) {
		this.coordinates = coordinates;
	}
	
	public CellPos add(CellPos shift) {
		int[] newCoords = new int[getDimension()];
		if (shift.getDimension() == getDimension()) {
			for (int i = 0; i < getDimension(); i++) {
				newCoords[i] = coordinates[i] + shift.coordinates[i];
			}
		}
		return new CellPos(newCoords);
	}
	
	public int getCoordinate(int i) {
		return coordinates[i];
	}
	
	public int[] getCoordinates() {
		return coordinates;
	}
	
	public int getDimension() {
		return coordinates.length;
	}
	
	@Override
	public String toString() {
		String out = "<" + coordinates[0];
		for (int i = 1; i < coordinates.length; i++)
			out += ", " + coordinates[i];
		out += ">";
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CellPos))
			return false;
		if (this.getDimension() != ((CellPos)obj).getDimension())
			return false;
		for (int i = 0; i < this.getDimension(); i++)
			if (this.getCoordinate(i) != ((CellPos)obj).getCoordinate(i))
				return false;
		return true;
				
	}
}
