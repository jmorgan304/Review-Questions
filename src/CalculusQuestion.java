import java.util.ArrayList;
import java.util.List;

public class CalculusQuestion extends Question {
	private static final char[] variables = { 'x', 'y', 'z', 'u', 'v', 't', 'w' };

	public static void main(String args[]) {
		CalculusQuestion cq = randomPartialDerivative(3, 1, 2, false, false);
		// RPD with highest power = 3, 1 term with 2 vars per term, not exponential, not
		// integer powers
		System.out.println(cq);
		cq = randomIntegral(3, 1, 2, false, false);
		// RI with highest power = 3, 1 term with 2 vars per term, not exponential, not
		// integer powers
		System.out.println(cq);
		cq = randomPartialDerivative(3, 1, 2, true, true);
		// RPD with highest power = 3, 1 term with 2 vars per term, exponential, only
		// integer powers
		System.out.println(cq);
		cq = randomIntegral(3, 1, 2, true, true);
		// RI with highest power = 3, 1 term with 2 vars per term, exponential, only
		// integer powers
		System.out.println(cq);
	}

	CalculusQuestion(String template, String[] values) {
		super(template, values);
	}

	public static CalculusQuestion randomIntegral(int highestPower, int numOfTerms, int numOfVariables, boolean exp,
			boolean integers) {
		String polynomial;
		if (integers) {
			polynomial = randomIntegerPolynomial(highestPower, numOfTerms, numOfVariables);
		}
		else {
			polynomial = randomRationalPolynomial(highestPower, numOfTerms, numOfVariables);
		}

		if (exp) {
			polynomial = exp(polynomial);
		}

		String differentials = "";
		for (int i = 0; i < numOfVariables; i++) {
			differentials += "d" + variables[i];
		}

		String integral = polynomial + differentials;
		String template = "Find the integral of <0>";
		CalculusQuestion cq = new CalculusQuestion(template, new String[] { integral });
		return cq;
	}

	public static CalculusQuestion randomPartialDerivative(int highestPower, int numOfTerms, int numOfVariables,
			boolean exp, boolean integers) {
		String polynomial;
		if (integers) {
			polynomial = randomIntegerPolynomial(highestPower, numOfTerms, numOfVariables);
		}
		else {
			polynomial = randomRationalPolynomial(highestPower, numOfTerms, numOfVariables);
		}

		if (exp) {
			polynomial = exp(polynomial);
		}

		int randomIndex = randomInteger(0, numOfVariables);
		String withRespectTo = "" + variables[randomIndex];
		String template = "Find the derivative of <0> with respect to <1>";
		CalculusQuestion cq = new CalculusQuestion(template, new String[] { polynomial, withRespectTo });
		return cq;
	}

	public static String randomIntegerPolynomial(int highestPower, int numOfTerms, int numOfVariables) {
		// Moves from highest power to 0 with 50% chance of picking each integer power
		// between
		// Also picks a constant in (0, 9] for each term
		List<Integer> powers = generateIntegerPowers(highestPower, numOfTerms, numOfVariables);
		List<Integer> constants = generateIntegerConstants(numOfTerms);

		return buildPolynomial(constants, powers, numOfVariables, true);
	}

	public static String randomRationalPolynomial(int highestPower, int numOfTerms, int numOfVariables) {
		List<Integer> powers = generateRationalPowers(numOfTerms, numOfVariables);
		List<Integer> constants = generateIntegerConstants(numOfTerms);

		return buildPolynomial(constants, powers, numOfVariables, false);
	}

	public static String exp(String power) {
		power = "(" + power + ")";
		String exp = "e^" + power;
		return exp;
	}

	public static <T extends Number> String buildPolynomial(List<T> constants, List<T> powers, int numOfVariables,
			boolean integers) throws IllegalArgumentException {
		// Powers are indexed according to vars per term, so if there are 2 vars per
		// term, the first two powers go with the first term
		if ((constants.size() != (powers.size() / numOfVariables) && integers)
				&& (constants.size() != 2 * (powers.size() / numOfVariables) && !integers)) {
			throw new IllegalArgumentException(
					"Number of constants must equal (number of powers / number of variables).");
		}

		if (numOfVariables > variables.length) {
			throw new IllegalArgumentException("Not enough unique variables available.");
		}

		String polynomial = "";
		for (int i = 0; i < constants.size(); i++) {
			if (Double.valueOf(constants.get(i).toString()) != 1d) {
				polynomial += constants.get(i);
			}

			int powerIndex;
			if (integers) {
				for (int j = 0; j < numOfVariables; j++) {
					char varLetter = variables[j];

					powerIndex = i * numOfVariables + j;
					if (powers.get(powerIndex).doubleValue() == 0d) {
						polynomial += "";
					}
					else if (powers.get(powerIndex).doubleValue() == 1d) {
						polynomial += varLetter;
					}
					else {
						polynomial += varLetter + "^" + powers.get(powerIndex);
					}

				}
			}
			else {
				// Rational powers
				for (int j = 0; j < 2 * numOfVariables; j++) {
					char varLetter = variables[j / 2];

					powerIndex = i * numOfVariables + j;

					T power = powers.get(powerIndex);

					if (j % 2 == 0) {
						// Numerator
						polynomial += varLetter + "^(";
						polynomial += power + "/";
					}
					else {
						// Denominator
						if (Double.valueOf(power.toString()) == 1d) {
							polynomial = polynomial.substring(0, polynomial.length() - 3);
							polynomial += powers.get(powerIndex - 1);
						}
						else {
							polynomial += power + ")";
						}
					}

				}

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

	private static List<Integer> generateRationalPowers(int numOfTerms, int numOfVariables) {
		List<Integer> powers = new ArrayList<>();

		int totalPowers = 2 * numOfTerms * numOfVariables;
		int termCount = 0;

		while (termCount < totalPowers) {
			int numerator = randomInteger(1, 10);
			int denominator = randomInteger(1, 10);
			while (numerator == denominator) {
				denominator = randomInteger(1, 10);
			}
			powers.add(numerator);
			powers.add(denominator);
			termCount += 2;
		}

		return powers;
	}

	private static List<Integer> generateIntegerPowers(int highestPower, int numOfTerms, int numOfVariables) {
		List<Integer> powers = new ArrayList<>();

		int totalPowers = numOfTerms * numOfVariables;
		int termCount = 0;
		int currentPower = highestPower;

		while (termCount < totalPowers) {
			if (eventOccurs(.50)) {
				powers.add(currentPower);
				termCount++;
				currentPower--;
			}
			double prob = (termCount / ((double) 2 * totalPowers));
			// prob = .25;
			if (eventOccurs(prob)) {
				currentPower--;
			}
		}

		return powers;
	}

	private static List<Integer> generateIntegerConstants(int numberOfConstants) {
		List<Integer> constants = new ArrayList<>();
		for (int i = 0; i < numberOfConstants; i++) {
			constants.add(randomInteger(1, 10));
		}
		return constants;
	}
}
