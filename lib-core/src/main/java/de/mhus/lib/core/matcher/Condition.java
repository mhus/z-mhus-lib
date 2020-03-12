/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.matcher;

import java.util.Map;

import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.matcher.ModelPattern.CONDITION;
import de.mhus.lib.core.parser.StringTokenizerParser;
import de.mhus.lib.core.parser.TechnicalStringParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.SyntaxError;

/**
 * e.g. $param1 regex .*test.* $param1 mr or $param1 mrs
 *
 * <p>Support also: $paramName ${paramName}
 *
 * @author mikehummel
 */
public class Condition {

    ModelComposit root = null;

    public Condition(String condition) throws MException {
        TechnicalStringParser tokenizer = new TechnicalStringParser(condition);
        tokenizer.setBreakableCharacters("()!");
        parse(tokenizer);
    }

    public boolean matches(Map<String, ?> map) {
        if (map == null) return false;
        return root.m(map);
    }

    protected void parse(StringTokenizerParser condition) throws MException {

        root = null;

        ModelPattern pattern = null;
        String param = null;

        Context context = new Context();
        CONDITION cond = CONDITION.EQ;

        for (String part : condition) {
            if (part == null) continue;

            // System.out.println(part);
            String lp = part.toLowerCase();

            boolean isPattern = false;
            if (condition.isEnclosuredToken()) {
                isPattern = true;
            } else {
                switch (lp) {
                    case "not":
                    case "!":
                        {
                            context.not = true;
                        }
                        break;
                    case "and":
                    case "&&":
                        if (context.not) throw new SyntaxError("not before operator");
                        if (pattern != null) throw new SyntaxError("type before operator");

                        if (context.current == null || !(context.current instanceof ModelAnd)) {
                            ModelAnd next = new ModelAnd();
                            if (param == null && context.current != null)
                                param = context.current.getParamName();
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
                            if (param == null && context.current != null)
                                param = context.current.getParamName();
                            next.setParamName(param);
                            context.append(next);
                        }
                        break;
                    case "(":
                        {
                            Context next = new Context();
                            next.parentContext = context;
                            context = next;
                        }
                        break;
                    case ")":
                        {
                            if (context.parent == null)
                                throw new SyntaxError("can't close bracket");
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
                        }
                        break;
                    case "number":
                        if (pattern != null) throw new SyntaxError("type before type");
                        pattern = new ModelNumber();
                        break;
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
                    case "var":
                        if (pattern != null) throw new SyntaxError("type before type");
                        pattern = new ModelVariable();
                        break;
                    case "is":
                    case "==":
                        cond = CONDITION.EQ;
                        break;
                    case "lt":
                    case "<":
                        cond = CONDITION.LT;
                        break;
                    case "le":
                    case "<=":
                        cond = CONDITION.LE;
                        break;
                    case "ge":
                    case ">=":
                        cond = CONDITION.GE;
                        break;
                    case "gr":
                    case ">":
                        cond = CONDITION.GR;
                        break;
                    default:
                        isPattern = true;
                }
            }

            if (isPattern
                    && param == null
                    && !condition.isEnclosuredToken()
                    && lp.startsWith("$")) {
                param = part.substring(1);
                if (param.startsWith("{") && param.endsWith("}"))
                    param = param.substring(1, param.length() - 1);
                //				else // no more supports $paramName$
                //				if (param.endsWith("$"))
                //				    param = param.substring(0, param.length()-1);
                isPattern = false;
            }

            if (isPattern) {

                if (context.current == null && context.first != null)
                    throw new SyntaxError("pattern after pattern without operation");
                if (param == null) param = context.current.getParamName();
                if (param == null) throw new SyntaxError("pattern without parameter name");
                if (pattern == null) {
                    if (part.startsWith("${")) pattern = new ModelVariable();
                    else if (MValidator.isNumber(part)) pattern = new ModelNumber();
                    else pattern = new ModelRegex();
                }
                pattern.setCondition(cond);
                pattern.setParamName(param);
                pattern.setPattern(part);
                pattern.setNot(context.not);
                if (context.current == null) context.first = pattern;
                else context.current.add(pattern);
                context.not = false;
                pattern = null;
                param = null;
                cond = CONDITION.EQ;
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
