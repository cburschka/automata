package controls;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import automata.CellularAutomaton;

public class DirectoryLoader extends URLClassLoader {
	private List<String> classNames;
	
	public DirectoryLoader(File path) {
		super(new URL[0]);
		File[] contents = path.listFiles(new FilenameFilter() {@Override
			public boolean accept(File dir, String name) {
				return name != null && name.length() > 4 && name.substring(name.length() - 4).equalsIgnoreCase(".jar");
			}
		});
		if (contents != null) {
			classNames = new ArrayList<String>(contents.length);
			for (int i = 0; i < contents.length; i++) {
				try {
					classNames.add(contents[i].getName().split("[^A-Za-z0-9]", 2)[0]);
					super.addURL(contents[i].toURI().toURL());
				} catch (MalformedURLException e) {System.err.println("Error converting " + contents[i].toURI() + " to URL while scanning for plugins.");}
			}
		}
		else {
			classNames = new ArrayList<String>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Class<CellularAutomaton<?>>> getClasses() {
		List<Class<CellularAutomaton<?>>> classes = new ArrayList<Class<CellularAutomaton<?>>>();
		for (int i = 0; i < classNames.size(); i++) {
			try {
				classes.add((Class<CellularAutomaton<?>>)super.loadClass(classNames.get(i)));
			}
			catch (ClassNotFoundException e) {}
			catch (ClassCastException e) {}
		}
		return classes;
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
		classNames.add(new File(url.getPath()).getName().split("[^A-Za-z0-9]", 2)[0]);
	}
}
