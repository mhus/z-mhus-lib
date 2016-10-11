package de.mhus.lib.core.matcher;

import java.util.Map;

import de.mhus.lib.core.parser.StringTokenizerParser;
import de.mhus.lib.core.parser.TechnicalStringParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.SyntaxError;

/**
 * e.g.
 * $param1 regex .*test.*
 * $param1 mr or $param1 mrs
 * 
 * @author mikehummel
 *
 */
public class Condition {

	ModelComposit root = null;
	
	public Condition(String condition) throws MException {
		TechnicalStringParser tokenizer = new TechnicalStringParser(condition);
		tokenizer.setBreakableCharacters("()!");
		parse(tokenizer);
	}

	public boolean matches(Map<String,?> map) {
		if (map == null) return false;
		return root.m(map);
	}
	
	protected void parse(StringTokenizerParser condition) throws MException {
	
		root = null;
		
		ModelPattern pattern = null;
		String param = null;
		
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
						if (param == null && context.current != null) param = context.current.getParamName();
						next.setParamName(param);
						context.append(next);
					}
					break;
				case "or":
				case "||":
					if (context.not) throw new SyntaxError("not before operator");
					if (pattern != null) throw new SyntaxError("type before operator");
					
					if (context.current == null || !(context.current instanceof ModelAnd)) {
						ModelOr next = new ModelOr();
						if (param == null && context.current != null) param = context.current.getParamName();
						next.setParamName(param);
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
				case "null":
					pattern = new NullPattern();
					isPattern = true;
					break;
				default:
					isPattern = true;
				}
			}
			
			if (isPattern && lp.startsWith("$")) {
				param = part.substring(1);
				if (param.endsWith("$")) param = param.substring(0, param.length()-1);
				isPattern = false;
			}
			
			if (isPattern) {
				
				if (context.current == null && context.first != null) throw new SyntaxError("pattern after pattern without operation");
				if (param == null) param = context.current.getParamName();
				if (param == null) throw new SyntaxError("pattern without parameter name");
				if (pattern == null) pattern = new ModelRegex();
				pattern.setParamName(param);
				pattern.setPattern(part);
				pattern.setNot(context.not);
				if (context.current == null)
					context.first = pattern;
				else
					context.current.add(pattern);
				context.not = false;
				pattern = null;
				param = null;
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
