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

import de.mhus.lib.core.MValidator;
import de.mhus.lib.core.matcher.ModelPattern.CONDITION;
import de.mhus.lib.core.parser.StringTokenizerParser;
import de.mhus.lib.core.parser.TechnicalStringParser;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.SyntaxError;

/**
 * Matches a single value against a condition. The Matcher will compile the condition and speedup if
 * you use the same condition often.
 *
 * <p>Syntax:
 *
 * <p>[ [type] [not] pattern ]* with operator.
 *
 * <p>Types: - fs - file sysytem like pattern with * - sql - sql like pattern with % - regex
 * (default) - regular expression
 *
 * <p>Operators: - and, && - And - or, || - Or - Brackets - How brackets work ... - not, ! as
 * negative operator
 *
 * <p>e.g. .*aaa.* .*aaa.* or .*bbb.* .*aaa.* and .*bbb.* .*aaa.* and not .*bbb.* not (.*aaa.* or
 * .*bbb.*) .*xyz.* or (.*aaa.* and .*bbb.*) fs *aaa* sql %aaa%
 *
 * @author mikehummel
 */
public class Matcher {

    ModelComposit root = null;

    public Matcher(String condition) throws MException {
        TechnicalStringParser tokenizer = new TechnicalStringParser(condition);
        tokenizer.setBreakableCharacters("()!");
        parse(tokenizer);
    }

    public boolean matches(String str) {
        if (str == null || root == null) return false;
        return root.m(null, str);
    }

    protected void parse(StringTokenizerParser condition) throws MException {

        root = null;

        ModelPattern pattern = null;

        Context context = new Context();
        CONDITION cond = CONDITION.EQ;

        for (String part : condition) {
            if (part == null) continue;

            // System.out.println(part);
            String lp = part.toLowerCase();

            boolean isPattern = false;
            if (condition.isTokenEncapsulated()) {
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

            if (isPattern) {

                if (context.current == null && context.first != null)
                    throw new SyntaxError("pattern after pattern without operation");
                if (pattern == null) {
                    if (part.startsWith("${")) pattern = new ModelVariable();
                    else if (MValidator.isNumber(part)) pattern = new ModelNumber();
                    else pattern = new ModelRegex();
                }
                pattern.setCondition(cond);
                pattern.setPattern(part);
                pattern.setNot(context.not);
                if (context.current == null) context.first = pattern;
                else context.current.add(pattern);
                context.not = false;
                pattern = null;
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
