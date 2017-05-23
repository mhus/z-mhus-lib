package de.mhus.lib.core.matcher;

import de.mhus.lib.core.parser.StringTokenizerParser;
import de.mhus.lib.core.parser.TechnicalStringParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.SyntaxError;

/**
 * Matches a single value against a condition. The Matcher will compile the condition
 * and speedup if you use the same condition often.
 * 
 * Syntax:
 * 
 * [ [type] [not] pattern ]* with operator.
 * 
 * Types:
 * - fs - file sysytem like pattern with *
 * - sql - sql like pattern with %
 * - regex (default) - regular expression
 * 
 * Operators:
 * - and, && - And
 * - or, || - Or
 * - Brackets - How brackets work ...
 * - not, ! as negative operator
 * 
 * e.g.
 * .*aaa.*
 * .*aaa.* or .*bbb.*
 * .*aaa.* and .*bbb.*
 * .*aaa.* and not .*bbb.*
 * not (.*aaa.* or .*bbb.*)
 * .*xyz.* or (.*aaa.* and .*bbb.*)
 * fs *aaa*
 * sql %aaa%
 * 
 * 
 * @author mikehummel
 *
 */
public class Matcher {

	ModelComposit root = null;
	
	public Matcher(String condition) throws MException {
		TechnicalStringParser tokenizer = new TechnicalStringParser(condition);
		tokenizer.setBreakableCharacters("()!");
		parse(tokenizer);
	}

	public boolean matches(String str) {
		if (str == null) return false;
		return root.m(str);
	}
	
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
					if (pattern != null) throw new SyntaxError("type after pattern");
					pattern = new ModelFs();
					break;
				case "sql":
					if (pattern != null) throw new SyntaxError("type after pattern");
					pattern = new ModelSql();
					break;
				case "regex":
					if (pattern != null) throw new SyntaxError("type after pattern");
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
	
	
	@Override
	public String toString() {
		return root == null ? "[null]" : root.toString();
	}


}
