import java.util.HashMap;
import java.util.Map;

public class ProbabilityQuestion extends Question {
	private static final Map<String, String[]> PDFs;
	private static final Map<String, String[]> PMFs;

	ProbabilityQuestion(String template, String[] values) {
		super(template, values);

	}

	static {

		PMFs = new HashMap<>();
		// Of the form name : {mass function, mean, variance, moment generating
		// function}
		PMFs.put("Binomial", new String[] { "(n|k)*p^k*(1-p)^(n-k)", "n*p", "n*p*(1-p)", "(1-p+p*e^t)^n" });
		PMFs.put("Poisson", new String[] { "(l^k*e^(-l))/k!", "l", "l", "e^(l*(e^t-1))" });
		PMFs.put("", new String[] {});
		PMFs.put("", new String[] {});
		PMFs.put("", new String[] {});

		PDFs = new HashMap<>();
		// Of the form name : {density function, mean, variance, moment generating
		// function}
		PDFs.put("Normal", new String[] { "(1/sqrt(2*pi*s^2))*e^(-(x-u)^2/(2*s^2))", "u", "s", "e^(u*t+(s^2*t^2)/2)" });
		PDFs.put("Gamma",
				new String[] { "(B^a/gamma(a))*x^(a-1)*e^(-Bx)", "(a-1)/B, 1<a", "a/B^2", "(1-t/B)^(-a), t<B" });
		PDFs.put("Uniform", new String[] { "1/(b-a", "(a+b)/2", "(b-a)^2/12", "(e^(t*b)-e^(t*a))/(t*(b-a))" });
		PDFs.put("", new String[] {});
		PDFs.put("", new String[] {});

	}

}
