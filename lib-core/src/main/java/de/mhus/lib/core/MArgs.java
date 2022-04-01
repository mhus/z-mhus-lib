/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.mhus.lib.annotations.cmd.CmdArgument;
import de.mhus.lib.annotations.cmd.CmdDescription;
import de.mhus.lib.annotations.cmd.CmdOption;
import de.mhus.lib.core.pojo.MPojo;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.errors.UsageException;

/**
 * The class is a parser for program argument lists, like you get in the main(args) method. You can
 * also put a usage definition to the constructor to define the possible methods.
 *
 * <p>The parser will parse all arguments. If a minus is the first character the next argument will
 * be stored under this key. A argument key can be there multiple times. If there no value after the
 * key the key is marked as existing. A value without a key on front of it is stored under the
 * DEFAUTL key.
 *
 * <p>The "usage" feature is not finished yet!
 *
 * @author mhu
 */
public class MArgs extends MLog {

    public static final String ALLOW_OTHER_OPTIONS = "allowOtherOptions";
    private Map<String, Usage> values = new HashMap<>();
    private Usage[] usage;
    private boolean showHelp = false;
    private String error;

    /**
     * Use the argument array to parse arguments.
     *
     * @param args
     */
    public MArgs(String[] args) {
        this(args, (Usage[]) null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public MArgs(Object pojo, String[] args) {

        PojoModel model = MPojo.getAttributesModelFactory().createPojoModel(pojo.getClass());
        List<Usage> u = new ArrayList<>();
        Usage[] a = new Usage[50]; // max 50 should be ok
        Map<String, Usage> map = new HashMap<>();
        CmdDescription cmd = pojo.getClass().getAnnotation(CmdDescription.class);
        if (cmd != null) {
            u.add(new Help(cmd.description()));
            for (String flag : cmd.flags()) u.add(new Flag(flag, null));
        }
        for (PojoAttribute<?> entry : model) {
            CmdOption cOpt = entry.getAnnotation(CmdOption.class);
            if (cOpt != null) {
                Option opt =
                        new Option(
                                cOpt.shortcut(),
                                cOpt.name(),
                                cOpt.multi() ? -1 : cOpt.value() ? 1 : cOpt.valueCnt(),
                                cOpt.mandatory(),
                                cOpt.description());
                map.put(entry.getName(), opt);
                u.add(opt);
            } else {
                CmdArgument cArg = entry.getAnnotation(CmdArgument.class);
                if (cArg != null) {
                    Argument arg =
                            new Argument(
                                    cArg.name(),
                                    cArg.multi() ? -1 : cArg.valueCnt(),
                                    cArg.mandatory(),
                                    cArg.description());
                    map.put(entry.getName(), arg);
                    a[cArg.index()] = arg;
                }
            }
        }

        for (Usage entry : a) {
            if (entry != null) u.add(entry);
        }

        this.usage = u.toArray(new Usage[u.size()]);
        init(args);

        for (Map.Entry<String, Usage> entry : map.entrySet()) {
            Class type = null;
            try {

                PojoAttribute attr = model.getAttribute(entry.getKey());
                Object value = null;
                List<String> values = entry.getValue().getValues();
                type = attr.getType();
                if (type == Boolean.class || type == boolean.class)
                    value = MCast.toboolean(values.get(0), false);
                else if (type == Integer.class || type == int.class)
                    value = MCast.toint(values.get(0), 0);
                else if (type == Long.class || type == long.class)
                    value = MCast.tolong(values.get(0), 0);
                else if (type == String.class) value = values.get(0);
                else if (type == String[].class) value = values.toArray(new String[0]);
                else if (type == Date.class) value = MCast.toDate(values.get(0), null);
                else if (type == UUID.class) value = UUID.fromString(values.get(0));
                else if (List.class.isAssignableFrom(type)) value = values;

                attr.set(pojo, value, true);
            } catch (Throwable e) {
                log().e("cast of {1} to {2} failed", entry, type, e);
            }
        }
    }

    /**
     * Usage: : <param1> : Command description\ncontaining line breaks :: param1 : Parameter
     * description option1: Option description option2: <param> : Option with mandatory parameter
     * option3: [param] : Option with optional parameter option4: <param>* : Option allowed multiple
     * times A line without colon will be printed in the usage as it is. e.g.
     *
     * <p>:ls [options] [file]*: list directory contents ::file: For each operand that names a file
     * of a type other than directory, ls displays its name as well as any requested, associated
     * information. l: List in long format. (See below.) A total sum for all the file sizes is
     * output on a line before the long listing.
     *
     * @param args
     * @param pUsage
     */
    public MArgs(String[] args, Usage... pUsage) {

        this.usage = pUsage;
        init(args);
    }

    private void init(String[] args) {
        if (usage != null) {
            int index = 1;
            for (Usage u : usage) {
                if (u.getIntName() != null) {
                    if (u instanceof Argument) {
                        ((Argument) u).setIndex(index);
                        index++;
                    }
                    values.put(u.getIntName(), u);
                }
                if (u.getAliasName() != null) values.put(u.getAliasName(), u);
            }
        }

        // parse

        boolean allowOtherOptions = usage == null || getFlag(ALLOW_OTHER_OPTIONS) != null;

        Usage current = null;
        Usage currentArg = null;
        int index = 1;

        try {
            for (int i = 0; i < args.length; i++) {

                String n = args[i];

                if (n.startsWith("--") && n.length() > 2) {
                    // it's a new key

                    if (current != null) {
                        current.noMoreValues();
                        current = null;
                    }

                    n = n.substring(2);
                    n = encaps(n);
                    if (n.equals("help")) {
                        showHelp = true;
                        break;
                    }

                    Usage opt = values.get("--" + n);

                    if (opt == null) throw new UsageException("unknown operation", n);

                    opt.set();

                    if (opt.hasValue()) current = opt;

                } else if (n.startsWith("-") && n.length() > 1) {
                    // it's a new key

                    if (current != null) {
                        current.noMoreValues();
                        current = null;
                    }

                    n = n.substring(1);
                    n = encaps(n);

                    String[] parts = n.split("");

                    for (String p : parts) {

                        if (current != null) {
                            current.noMoreValues();
                            current = null;
                        }

                        Usage opt = values.get("-" + p);
                        if (opt == null && allowOtherOptions) {
                            opt = new Option(p);
                            values.put(opt.getIntName(), opt);
                        }
                        if (opt == null) throw new UsageException("unknown option", n);

                        opt.set();

                        if (opt.hasValue()) current = opt;
                    }
                } else if (current != null) {
                    n = encaps(n);
                    current.add(n);
                    if (!current.moreValues()) current = null;
                } else if (currentArg != null) {
                    n = encaps(n);
                    currentArg.add(n);
                    if (!currentArg.moreValues()) currentArg = null;
                } else {

                    Usage arg = values.get("#" + index);

                    if (arg == null && usage == null) {
                        arg = new Argument("" + index);
                        ((Argument) arg).setIndex(index);
                        values.put(arg.getIntName(), arg);
                    }

                    if (arg == null) throw new UsageException("Argument not supported", index);

                    n = encaps(n);
                    arg.add(n);
                    if (arg.hasValue()) currentArg = arg;
                    index++;
                }
            }

            values.forEach((k, v) -> v.noMoreValues());

        } catch (UsageException e) {
            showHelp = true;
            error = "Usage error: " + e.getMessage();
            //        	e.printStackTrace();
        }
    }

    public void printUsage() {
        printUsage(System.out);
    }

    public void printUsage(PrintStream out) {
        if (error != null) out.println(error);
        if (usage != null) {
            System.out.println("Usage: ");
            for (Usage u : usage) u.printUsage(out);
        } else {
            System.out.println("Unknown usage");
        }
    }

    private String encaps(String name) {
        if (name.startsWith("\"") && name.endsWith("\"")
                || name.startsWith("'") && name.endsWith("'"))
            name = name.substring(1, name.length() - 1);
        return name;
    }

    /**
     * Returns true if the options list contains the key and the option is set.
     *
     * @param name
     * @return if is included
     */
    public boolean hasOption(String name) {
        Usage opt = values.get("-" + name);
        if (opt == null) return false;
        return opt.isSet();
    }

    /**
     * Will always return an option even if it's not configured. In this case the value is null.
     *
     * @param name
     * @return An Option
     */
    public Option getOption(String name) {
        Usage opt = values.get("-" + name);
        if (opt == null) opt = new Option(name);
        return (Option) opt;
    }

    /**
     * Return the argument from index. The first index is 1 and NOT 0 - like it's printed in usage.
     *
     * @param index
     * @return The Argument object
     */
    public Argument getArgument(int index) {
        Usage arg = values.get("#" + index);
        return (Argument) arg;
    }

    /**
     * Return an argument by name.
     *
     * @param name
     * @return The Argument object
     */
    public Argument getArgument(String name) {
        if (usage == null) return null;
        for (Usage u : usage)
            if (u instanceof Argument && name.equals(u.getName())) return (Argument) u;
        return null;
    }

    public Flag getFlag(String name) {
        return (Flag) values.get("+" + name);
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public abstract static class Usage {

        protected String name;
        protected List<String> values = new LinkedList<String>();
        protected boolean set = false;
        protected boolean more = false;
        protected boolean hasValue = false;

        public Usage(String name) {
            this.name = name;
        }

        public abstract String getIntName();

        public abstract String getAliasName();

        protected Object getName() {
            return name;
        }

        public List<String> getValues() {
            return values;
        }

        public boolean isSet() {
            return set;
        }

        protected boolean moreValues() {
            return more;
        }

        protected void add(String n) {
            values.add(n);
        }

        protected abstract void noMoreValues();

        protected boolean hasValue() {
            return hasValue;
        }

        protected void set() {
            set = true;
        }

        protected abstract void printUsage(PrintStream out);

        @Override
        public String toString() {
            return getClass().getSimpleName() + ":" + name + "=" + values;
        }
    }

    public static class Option extends Usage {

        private String desc;
        private int valueCnt;
        private boolean mandatory;
        private String alias;

        public Option(String name) {
            this(
                    name.length() == 1 ? name.charAt(0) : (char) 0,
                    name.length() > 1 ? name : null,
                    -1,
                    false,
                    null);
        }

        public Option(char name, String alias, int valueCnt, boolean mandatory, String desc) {
            super(name == 0 ? null : String.valueOf(name));
            this.desc = desc;
            this.valueCnt = valueCnt;
            this.mandatory = mandatory;
            this.alias = alias;
            hasValue = valueCnt != 0;
            more = false;
        }

        @Override
        public String getIntName() {
            return "-" + name;
        }

        @Override
        public String getAliasName() {
            if (alias == null) return null;
            return "--" + alias;
        }

        @Override
        protected void noMoreValues() {
            if (valueCnt > 0 && mandatory && values.size() < valueCnt)
                throw new UsageException("Option " + name + " is mandatory");
        }

        @Override
        protected void printUsage(PrintStream out) {
            out.println(
                    "-"
                            + name
                            + (alias != null ? ", --" + alias : "")
                            + (valueCnt > 0 ? " <value> (" + valueCnt + ")" : "")
                            + (mandatory ? "*" : ""));
            if (desc != null) out.println(toDesc(desc));
        }

        public String getValue() {
            if (values.size() == 0) return null;
            return values.get(0);
        }

        public String getValue(String def) {
            if (values.size() == 0) return def;
            return values.get(0);
        }

        @Override
        protected void add(String n) {
            if (valueCnt > -1 && values.size() >= valueCnt)
                throw new UsageException("Too much values for option", name);
            super.add(n);
        }
    }

    public static class Argument extends Usage {

        private String desc;
        private int valueCnt;
        private boolean mandatory;

        public Argument(String name) {
            this(name, 0, false, null);
        }

        public Argument(String name, int valueCnt, boolean mandatory, String desc) {
            super(name);
            this.desc = desc;
            this.valueCnt = valueCnt;
            this.mandatory = mandatory;
            if (valueCnt < 0 || valueCnt > 1) hasValue = true;
        }

        private int index;

        private void setIndex(int index) {
            this.index = index;
        }

        @Override
        public String getIntName() {
            return "#" + index;
        }

        @Override
        protected void noMoreValues() {
            if (valueCnt > 0 && mandatory && values.size() < valueCnt)
                throw new UsageException("Argument " + index + " not set");
        }

        @Override
        protected void printUsage(PrintStream out) {
            out.println("Argument #" + index + " " + name + (mandatory ? "*" : ""));
            if (desc != null) out.println(toDesc(desc));
        }

        @Override
        public String getAliasName() {
            return null;
        }

        @Override
        protected void add(String n) {
            if (valueCnt > 1 && values.size() >= valueCnt)
                throw new UsageException("Too much values for option", name);
            super.add(n);
            more = valueCnt == -1 || values.size() < valueCnt;
        }

        public String getValue() {
            if (values.size() == 0) return null;
            return values.get(0);
        }

        public String getValue(String def) {
            if (values.size() == 0) return def;
            return values.get(0);
        }

        public String getValue(int index, String def) {
            if (values.size() <= index) return def;
            return values.get(index);
        }
    }

    public static class Help extends Usage {

        public Help(String help) {
            super(help);
        }

        @Override
        public String getIntName() {
            return null;
        }

        @Override
        protected void noMoreValues() {}

        @Override
        protected void printUsage(PrintStream out) {
            out.println(name);
        }

        @Override
        public String getAliasName() {
            return null;
        }
    }

    public static class Flag extends Usage {

        private String flag;

        public Flag(String flag, String help) {
            super(help);
            this.flag = flag;
        }

        @Override
        public String getIntName() {
            return "+" + flag;
        }

        @Override
        protected void noMoreValues() {}

        @Override
        protected void printUsage(PrintStream out) {
            out.println("+ " + (name != null ? name : flag));
        }

        @Override
        public String getAliasName() {
            return null;
        }

        public String getFlag() {
            return flag;
        }
    }

    protected static String toDesc(String desc) {
        return "    " + desc.replace("\n", "\n    ");
    }

    public List<Argument> getArguments() {
        LinkedList<Argument> out = new LinkedList<>();
        for (int index = 1; values.containsKey("#" + index); index++) {
            Argument arg = (Argument) values.get("#" + index);
            if (arg.getValues().size() > 0) out.add(arg);
        }
        return out;
    }

    public Map<String, Option> getOptions() {
        final HashMap<String, Option> out = new HashMap<>();
        values.forEach(
                (k, v) -> {
                    if (k.startsWith("-")) out.put(k, (Option) v);
                });
        return out;
    }

    public boolean isValid() {
        return error == null;
    }

    public static Argument argAll(String name, String desc) {
        return new Argument(name, -1, false, desc);
    }

    public static Argument arg(String name, String desc) {
        return new Argument(name, 1, false, desc);
    }

    public static Argument arg(String name, boolean mandatory, String desc) {
        return new Argument(name, 1, mandatory, desc);
    }

    public static Argument arg(String name, int valueCnt, boolean mandatory, String desc) {
        return new Argument(name, valueCnt, mandatory, desc);
    }

    public static Help help(String help) {
        return new Help(help);
    }

    public static Flag optOther(String help) {
        return new Flag(ALLOW_OTHER_OPTIONS, help);
    }

    public static Flag allowOtherOptions() {
        return new Flag(ALLOW_OTHER_OPTIONS, "More options are possible");
    }

    public static Option opt(
            char name, String alias, int valueCnt, boolean mandatory, String desc) {
        return new Option(name, alias, valueCnt, mandatory, desc);
    }

    public static Option opt(char name, String desc) {
        return new Option(name, null, 0, false, desc);
    }

    public static Option optVal(char name, String desc) {
        return new Option(name, null, 1, false, desc);
    }

    public boolean isPrintUsage() {
        return showHelp;
    }
}
