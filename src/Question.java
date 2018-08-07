import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Question {
	private static final String regex = "<\\d+>";
	private static final Pattern pattern = Pattern.compile(regex);
	protected String template;
	protected String[] parsedTemplate;
	protected int numValues;
	
	
	public static void main(String[] args) {
		String template = "<0> Test <1> text <2> for the question <3> <> <<>> <12345> <12d34> <5>";
		Question q = new Question(template);
		System.out.println(q.insertValues(new String[]{"0", "1", "2", "3", "4", "5"}));
	}
	
	public Question(String template) {
		this.template = template;
		this.parsedTemplate = parseTemplate(template);
		this.numValues = countMatches(template);
	}
	
	public static String[] parseTemplate(String template) {
		// Every substring with the form < + any number of digits + >
		return pattern.split(template);
	}
	
	public static int countMatches(String template) {
		Matcher matcher = pattern.matcher(template);
		
		int numMatches = 0;
		while(matcher.find()) {
			numMatches++;
		}
		return numMatches;
	}
	
	public static int randomInteger(int lower, int upper) throws IllegalArgumentException {
		int range = upper - lower;
		if (range < 0) {
			throw new IllegalArgumentException("Upper value must be greater than lower");
		}
		double randomValue = Math.random();
		return (int)(range * randomValue + lower);
	}
	
	public static double randomReal(double lower, double upper) throws IllegalArgumentException {
		double range = upper - lower;
		if (range < 0d) {
			throw new IllegalArgumentException("Upper value must be greater than lower");
		}
		double randomValue = Math.random();
		return range * randomValue + lower;
	}
	
	public static boolean eventOccurs(double probabilityOfSuccess) throws IllegalArgumentException {
		if (probabilityOfSuccess > 1d || probabilityOfSuccess < 0d) {
			throw new IllegalArgumentException("Probabilities must be in [0, 1]");
		}
		double randomValue = Math.random();
		if (randomValue <= probabilityOfSuccess) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String insertValues(String[] values) throws IllegalArgumentException {
		if (values.length != this.numValues) {
			throw new IllegalArgumentException("Incorrect number of values");
		}
		String completeQuestion = "";
		for (int i = 0; i < this.numValues; i++) {
			completeQuestion += parsedTemplate[i];
			completeQuestion += values[i];
		}
		return completeQuestion;
	}
	
	
}
