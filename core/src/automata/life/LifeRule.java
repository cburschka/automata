package automata.life;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LifeRule {
	private boolean[][] rules = new boolean[2][9];
	private static final Pattern rulePattern = Pattern.compile("B([0-8]*)/S([0-8]*)");
	private String ruleNotation;
	
	public LifeRule(boolean[][] rules) {
		this.rules = rules;
	}
	
	public LifeRule(String ruleNotation) throws InvalidRuleException {
		this.ruleNotation = ruleNotation;
		if (ruleNotation == null) {
			throw new InvalidRuleException();
		}
		Matcher m = rulePattern.matcher(ruleNotation);
		if (!m.matches()) {
			throw new InvalidRuleException();
		}
		for (int i = 0; i < m.group(1).length(); i++) {
			rules[LifeCell.DEAD][Integer.parseInt("" + m.group(1).charAt(i))] = true;
		}
		for (int i = 0; i < m.group(2).length(); i++) {
			rules[LifeCell.ALIVE][Integer.parseInt("" + m.group(2).charAt(i))] = true;
		}
	}
	
	public int apply(int current, int sum) {
		return rules[current][sum] ? LifeCell.ALIVE : LifeCell.DEAD;
	}
	
	@Override
	public String toString() {
		if (this.ruleNotation == null) {
			String born = "B", survive = "S";
			for (int i = 0; i < 9; i++) {
				if (rules[0][i])
					born += i;
				if (rules[1][i])
					survive += i;
			}
			this.ruleNotation = born + "/" + survive;
		}
		return ruleNotation;
	}
}
