import de.mhus.lib.core.matcher.Matcher;
import de.mhus.lib.errors.MException;


public class TestMatcher {

	public static void main(String[] args) throws MException {
		
		String rule = "ext_.* and !\".*[Pp]assword.*\"";
//		String rule = "ext_.* and !.*[Pp]assword.*";
		Matcher matcher = new Matcher(rule);

		String c = "";
		
		c = "ext_voipPassword";
		System.out.println(c + ": " + matcher.matches(c));
		
		
	}
	
}
