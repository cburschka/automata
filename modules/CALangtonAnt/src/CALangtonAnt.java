import javax.swing.JOptionPane;

import ui.theme.BinaryTheme;
import ui.theme.Theme;

import automata.CellularAutomaton;
import automata.grid.CellPos;
import automata.grid.Grid;


public class CALangtonAnt extends CellularAutomaton<AntCell> {
	private boolean[] rule;
	private String ruleString;
	private static final int LINE_WIDTH = 20;
	
	public CALangtonAnt() {
		String rules = (String)JOptionPane.showInputDialog(
                null,
                "Langton's Ant",
                "Variante",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "RL");
		rule = new boolean[rules.length()];
		for (int i = 0; i < rule.length; i++) {
			rule[i] = rules.charAt(i) == 'R';
		}
		this.ruleString = rules;
	}

	@Override
	public String getDescription() {
		return "Langton's Ant (" + ruleString.length() + ")";
	}

	@Override
	public Grid<AntCell> initialize(int... dimensions) {
		// TODO Auto-generated method stub
		Grid<AntCell> g = new AntGrid(new Ant(rule, new CellPos(dimensions[0]/2, dimensions[1]/2), 0), dimensions[0], dimensions[1]);
		return g;
	}

	@Override
	public AntCell getZeroState() {
		//System.out.println("Bad");
		return null;
	}

	@Override
	public Theme<AntCell> getTheme() {
		if (rule.length > 2)
			return new HueTheme<AntCell>();
		else
			return new BinaryTheme<AntCell>();
	}
	
	@Override
	public String[] getSummary(Grid<AntCell> grid) {
		String[] out = new String[(ruleString.length()+LINE_WIDTH-1)/LINE_WIDTH]; 
		for (int i = 0; i < out.length; i++) {
			out[i] = ruleString.substring(LINE_WIDTH*i, Math.min(ruleString.length(), LINE_WIDTH*(i+1)));
		}
		return out;
	}
	
	@Override
	public AntCell readCellState(String str) {
		return new AntCell(Integer.parseInt(str), rule.length);
	}
	
	public static String getName() {
		return "Langton's Ant";
	}
}
