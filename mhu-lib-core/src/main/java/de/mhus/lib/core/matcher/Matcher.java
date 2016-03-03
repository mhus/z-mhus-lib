package de.mhus.lib.core.matcher;

import de.mhus.lib.core.parser.StringTokenizerParser;
import de.mhus.lib.core.parser.TechnicalStringParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.SyntaxError;

/**
 * <p>Matcher class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class Matcher {

	ModelComposit root = null;
	
	/**
	 * <p>Constructor for Matcher.</p>
	 *
	 * @param condition a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Matcher(String condition) throws MException {
		TechnicalStringParser tokenizer = new TechnicalStringParser(condition);
		tokenizer.setBreakableCharacters("()!");
		parse(tokenizer);
	}

	/**
	 * <p>matches.</p>
	 *
	 * @param str a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean matches(String str) {
		if (str == null) return false;
		return root.m(str);
	}
	
	/**
	 * <p>parse.</p>
	 *
	 * @param condition a {@link de.mhus.lib.core.parser.StringTokenizerParser} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	protected void parse(StringTokenizerParser condition) throws MException {
	
		root = null;
		
		ModelPattern pattern = null;
		
		Context context = new Context();
		
		
		for (String part : condition) {
			if (part == null) continue;
			
			//System.out.println(part);
			String lp = part.toLowerCase();
			
			boolean isPattern = false;
			if (condition.isTokenEncapsulated()) {
				isPattern = true;
			} else {
				switch (lp) {
				case "not":
				case "!": {
					context.not = true;
				} break;
				case "and":
				case "&&":
					if (context.not) throw new SyntaxError("not before operator");
					if (pattern != null) throw new SyntaxError("type before operator");
					
					if (context.current == null || !(context.current instanceof ModelAnd)) {
						ModelAnd next = new ModelAnd();
						context.append(next);
					}
					break;
				case "or":
				case "||":
					if (context.not) throw new SyntaxError("not before operator");
					if (pattern != null) throw new SyntaxError("type before operator");
					
					if (context.current == null || !(context.current instanceof ModelAnd)) {
						ModelOr next = new ModelOr();
						context.append(next);
					}
					break;
				case "(": {
					
					Context next = new Context();
					next.parentContext = context;
					context = next;
				} break;
				case ")": {
					
					if (context.parent == null) throw new SyntaxError("can't close bracket");
					Context last = context;
					context = context.parentContext;
					ModelComposit next = last.findRoot();
					if (last.current == null && last.first != null) {
						ModelAnd and = new ModelAnd();
						and.setNot(last.not);
						last.current = and;
					} else {
						next.setNot(context.not);
					}
					
					context.append(next);
					
				} break;
				case "fs":
					if (pattern != null) throw new SyntaxError("type before type");
					pattern = new ModelFs();
					break;
				case "sql":
					if (pattern != null) throw new SyntaxError("type before type");
					pattern = new ModelSql();
					break;
				case "regex":
					if (pattern != null) throw new SyntaxError("type before type");
					pattern = new ModelRegex();
					break;
					
				default:
					isPattern = true;
				}
			}
			
			if (isPattern) {
				
				if (context.current == null && context.first != null) throw new SyntaxError("pattern after pattern without operation");
				if (pattern == null) pattern = new ModelRegex();
				pattern.setPattern(part);
				pattern.setNot(context.not);
				if (context.current == null)
					context.first = pattern;
				else
					context.current.add(pattern);
				context.not = false;
				pattern = null;
			}
			
			
		}
		
		if (context.parentContext != null) throw new SyntaxError("bracked not closed");
		
		root = context.findRoot();
	}
	
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return root == null ? "[null]" : root.toString();
	}


}
