import java.util.ArrayList;
import java.util.List;

public class CalculusQuestion extends Question {

	public static void main(String args[]) {
		String template = "Find the derivative of <0>";
		String[] values = { randomIntegerPolynomial(5) };
		CalculusQuestion cq = new CalculusQuestion(template, values);
		System.out.println(cq);
	}

	CalculusQuestion(String template, String[] values) {
		super(template, values);
	}

	public static String randomIntegerPolynomial(int highestPower) {
		// Moves from highest power to 0 with 50% chance of picking each integer power
		// between
		// Also picks a constant in (0, 9] for each term
		List<Integer> constants = new ArrayList<>();
		List<Integer> powers = new ArrayList<>();

		for (int i = highestPower; i >= 0; i--) {
			if (eventOccurs(.5)) {
				constants.add(randomInteger(1, 10));
				powers.add(i);
			}
		}

		return buildPolynomial(constants, powers);
	}

	public static <T extends Number> String buildPolynomial(List<T> constants, List<T> powers)
			throws IllegalArgumentException {
		if (constants.size() != powers.size()) {
			throw new IllegalArgumentException("Number of constants must equal the number of powers.");
		}
		String polynomial = "";
		for (int i = 0; i < constants.size(); i++) {
			polynomial += constants.get(i);

			if (powers.get(i).doubleValue() == 0d) {
				continue;
			}
			else if (powers.get(i).doubleValue() == 1d) {
				polynomial += "x";
			}
			else {
				polynomial += "x^" + powers.get(i);
			}

			if (i < constants.size() - 1) {
				if (eventOccurs(.5)) {
					polynomial += " - ";
				}
				else {
					polynomial += " + ";
				}
			}
		}

		return polynomial;
	}
}
