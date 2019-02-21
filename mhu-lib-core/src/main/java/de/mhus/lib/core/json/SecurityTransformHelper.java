/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.json;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

public class SecurityTransformHelper extends TransformHelper {

	private Log log;
	private ClassLoader loader;
	private LinkedList<Rule> rules = null;
	
	public SecurityTransformHelper(ClassLoader loader, Log log) {
		this.log = log;
		this.loader = loader;
	}
	
	@Override
	public void log(String string, Throwable t) {
		if (log != null)
			log.t(string,t);
	}
	
	@Override
	public void log(String msg) {
		if (log != null)
			log.t(msg);
	}
	
	@Override
	public Object createObject(Class<?> type) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (!checkSecurityForClass(type.getCanonicalName()))
				throw new IllegalAccessException(type.getCanonicalName());
		return super.createObject(type);
	}

	@Override
	public Class<?> getType(String cName) throws IllegalAccessException {
		if (!checkSecurityForClass(cName))
			throw new IllegalAccessException(cName);
		return super.getType(cName);
	}
	
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
	
	@Override
	public ClassLoader getClassLoader() {
		if (loader != null)
			return loader;
		return super.getClassLoader();
	}
	
	public void setClassLoader(ClassLoader loader) {
		this.loader = loader;
	}

	public void addRule(Rule rule) {
		if (rules == null) rules = new LinkedList<>();
		synchronized (rules) {
			rules.add(rule);
		}
	}
	
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
