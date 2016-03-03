package de.mhus.lib.core.json;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

/**
 * <p>SecurityTransformHelper class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SecurityTransformHelper extends TransformHelper {

	private Log log;
	private ClassLoader loader;
	private LinkedList<Rule> rules = null;
	
	/**
	 * <p>Constructor for SecurityTransformHelper.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public SecurityTransformHelper(ClassLoader loader, Log log) {
		this.log = log;
		this.loader = loader;
	}
	
	/** {@inheritDoc} */
	@Override
	public void log(String string, Throwable t) {
		if (log != null)
			log.t(string,t);
	}
	
	/** {@inheritDoc} */
	@Override
	public void log(String msg) {
		if (log != null)
			log.t(msg);
	}
	
	/** {@inheritDoc} */
	@Override
	public Object createObject(Class<?> type) throws InstantiationException, IllegalAccessException {
		if (!checkSecurityForClass(type.getCanonicalName()))
				throw new IllegalAccessException(type.getCanonicalName());
		return super.createObject(type);
	}

	/** {@inheritDoc} */
	@Override
	public Class<?> getType(String cName) throws IllegalAccessException {
		if (!checkSecurityForClass(cName))
			throw new IllegalAccessException(cName);
		return super.getType(cName);
	}
	
	/**
	 * <p>checkSecurityForClass.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean checkSecurityForClass(String type) {
		if (rules == null)
			return true;
		synchronized (rules) {
			for (Rule rule : rules) {
				if (rule.isAllowed(type)) return true;
				if (rule.isDenied(type) ) return false;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public ClassLoader getClassLoader() {
		if (loader != null)
			return loader;
		return super.getClassLoader();
	}
	
	/**
	 * <p>setClassLoader.</p>
	 *
	 * @param loader a {@link java.lang.ClassLoader} object.
	 */
	public void setClassLoader(ClassLoader loader) {
		this.loader = loader;
	}

	/**
	 * <p>addRule.</p>
	 *
	 * @param rule a {@link de.mhus.lib.core.json.SecurityTransformHelper.Rule} object.
	 */
	public void addRule(Rule rule) {
		if (rules == null) rules = new LinkedList<>();
		synchronized (rules) {
			rules.add(rule);
		}
	}
	
	/**
	 * <p>Getter for the field <code>rules</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Rule> getRules() {
		if (rules == null) rules = new LinkedList<>();
		return rules;
	}

	public abstract static class Rule {

		public abstract boolean isDenied(String type);
//			return false;

		public abstract boolean isAllowed(String type);
//			return false;
		
	}
	
	public static class RuleAllow extends Rule {
		
		private String pattern;

		public RuleAllow(String allow) {
			pattern = allow;
		}
		
		@Override
		public boolean isDenied(String type) {
			return false;
		}

		@Override
		public boolean isAllowed(String type) {
			return MString.compareFsLikePattern(type, pattern);
		}

	}
	
	public static class RuleDeny extends Rule {
		
		private String pattern;

		public RuleDeny(String deny) {
			pattern = deny;
		}
		
		@Override
		public boolean isDenied(String type) {
			return MString.compareFsLikePattern(type, pattern);
		}

		@Override
		public boolean isAllowed(String type) {
			return false;
		}

	}
	
	public static class RuleRegexAllow extends Rule {

		private Pattern matcher;

		public RuleRegexAllow(String allow) {
			matcher = java.util.regex.Pattern.compile(allow);
		}
		
		@Override
		public boolean isDenied(String type) {
			return false;
		}

		@Override
		public boolean isAllowed(String type) {
			return matcher.matcher(type).matches();
		}
		
	}

	public static class RuleRegexDeny extends Rule {

		private Pattern matcher;

		public RuleRegexDeny(String allow) {
			matcher = java.util.regex.Pattern.compile(allow);
		}
		
		@Override
		public boolean isDenied(String type) {
			return matcher.matcher(type).matches();
		}

		@Override
		public boolean isAllowed(String type) {
			return false;
		}
		
	}

}
