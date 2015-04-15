/*
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * The class is a parser for program argument lists, like you get in the main(args)
 * method. You can also put a usage definition to the constructor to define the
 * possible methods.
 * 
 * The parser will parse all arguments. If a minus is the first character the next 
 * argument will be stored under this key. A argument key can be there multiple times.
 * If there no value after the key the key is marked as existing. A value without a key
 * on front of it is stored under the DEFAUTL key.
 * 
 * The "usage" feature is not finished yet!
 * 
 * @author mhu
 *
 */

public class MArgs {

	public static final String DEFAULT = "";

	private Hashtable<String,Vector<String>> values = new Hashtable<String,Vector<String>>();
	private Hashtable<String,String> usage;

	/**
	 * Use the argument array to parse arguments.
	 * 
	 * @param args
	 */
	public MArgs(String[] args) {
		this(args, null);
	}

	public MArgs(String[] args, String[] pUsage) {

		String name = DEFAULT;
		if (pUsage != null) {
			usage = new Hashtable<String,String>();
			for (int i = 0; i < pUsage.length; i += 3)
				usage.put(pUsage[i], pUsage[i + 1]);
		}
		// parse
		boolean printUsage = false;

		for (int i = 0; i < args.length; i++) {

			String n = args[i];

			if (n.startsWith("-") && n.length() > 1) {
				// it's a new key

				name = n.substring(1);
				if (name.startsWith("\"") && name.endsWith("\"")
						|| name.startsWith("'") && name.endsWith("'"))
					name = name.substring(1, name.length() - 1);

				if (values.get(name) == null) {
					values.put(name, new Vector<String>());
				}

				if (usage != null && !usage.containsKey(name))
					printUsage = true;

			} else {
				// it's a value

				if (usage != null
						&& (usage.get(name) == null || ((String) usage
								.get(name)).length() != 0))
					printUsage = true;

				if (n.startsWith("\"") && n.endsWith("\"") || n.startsWith("'")
						&& n.endsWith("'"))
					n = n.substring(1, n.length() - 1);

				Vector<String> v = values.get(name);
				if (v == null) {
					// for DEFAULT !!!
					v = new Vector<String>();
					values.put(name, v);
				}
				v.add(n);
				name = DEFAULT;
			}

		}

		if (usage != null && (printUsage || contains("?"))) {
			// print usage
			System.out.print("Usage: ");
			if (usage.containsKey(DEFAULT))
				System.out.println(usage.get(DEFAULT));
			else
				System.out.println();

			for (int i = 0; i < pUsage.length; i += 3) {
				if (pUsage[i].length() != 0) {
					String u = pUsage[i] + ' ' + pUsage[i + 1];
					System.out.print("  -" + u + "   ");
					for (int j = u.length(); j < 20; j++)
						System.out.print(' ');
					System.out.println(pUsage[i + 2]);
				}
			}
			System.exit(0);
		}

	}

	/**
	 * Returns true if the argument list contains the key.
	 * 
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		return values.get(name) != null;
	}

	/**
	 * Returns a List of the arguments for the given key. If the key was not set it 
	 * returns null. Do not change the list.
	 * 
	 * @param name
	 * @return
	 */
	protected List<String> getArgValues(String name) {
		return values.get(name);
	}

	/**
	 * Returns the amount of attributes for this key.
	 * 
	 * @param name
	 * @return
	 */
	public int getSize(String name) {
		if (!contains(name))
			return 0;
		return getArgValues(name).size();
	}

	/**
	 * Returns the "index" parameter for this key. If the parameter is not
	 * set in this index it returns "def".
	 * 
	 * @param name
	 * @param def
	 * @param index
	 * @return
	 */
	public String getValue(String name, String def, int index) {
		String ret = getValue(name, index);
		return ret == null ? def : ret;
	}

	/**
	 * Returns the "index" parameter for this key. If the parameter is not
	 * set in this index it returns null.
	 * 
	 * @param name
	 * @param index
	 * @return
	 */
	public String getValue(String name, int index) {
		String[] ret = getValues(name);
		if (ret == null)
			return null;
		if (ret.length <= index)
			return null;
		return ret[index];
	}

	/**
	 * Return all values for this parameter as a array. If not set it will return an empty
	 * array.
	 * 
	 * @param name
	 * @return
	 */
	public String[] getValues(String name) {
		if (!contains(name))
			return new String[0];
		return (String[]) getArgValues(name).toArray(new String[0]);
	}

	/**
	 * Return a iterable set of existing keys.
	 * 
	 * @return
	 */
	public Set<String> getKeys() {
		return values.keySet();
	}

	@Override
	public String toString() {
		return values.toString();
	}
}
