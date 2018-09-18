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
package de.mhus.lib.core.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.core.MPassword;
import de.mhus.lib.core.MString;

public class MutableUri extends MUri {

	private static final long serialVersionUID = 1L;
	protected String scheme;
	protected String location;
	protected String username;
	protected String password;
	private String[] params;
	protected Map<String, String> query;
	protected String fragment;
	protected String[] pathParts;
	private String path;

	/*
	   RFC 1808           Relative Uniform Resource Locators          June 1995


	      <scheme>://<net_loc>/<path>;<params>?<query>#<fragment>

	   each of which, except <scheme>, may be absent from a particular URL.
	   These components are defined as follows (a complete BNF is provided
	   in Section 2.2):

	      scheme ":"   ::= scheme name, as per Section 2.1 of RFC 1738 [2].

	      "//" net_loc ::= network location and login information, as per
	                       Section 3.1 of RFC 1738 [2].

	      "/" path     ::= URL path, as per Section 3.1 of RFC 1738 [2].

	      ";" params   ::= object parameters (e.g., ";type=a" as in
	                       Section 3.2.2 of RFC 1738 [2]).

	      "?" query    ::= query information, as per Section 3.3 of
	                       RFC 1738 [2].

	      "#" fragment ::= fragment identifier.
		 */
		public MutableUri(String path) {
			parse(path);
		}
		
		private void parse(String path) {
			if (MString.isEmpty(path)) return;
			// parse path
			int p = path.indexOf(':');
			int ps = path.indexOf('/');
			if (p >= 0 && (ps == -1 || p < ps)) {
				scheme = path.substring(0, p).toLowerCase();
				path = path.substring(p+1);
			}
			// special for //
			if (path.startsWith("//")) { // remove double slashes to be compatible with other schemes
				path = path.substring(2);
				p = path.indexOf('/');
				if (p>=0) {
					location = path.substring(0,p);
					path = path.substring(p+1);
				} else {
					location = path;
					path = "";
				}
				
			}
			// extract user and pw from location
			if (location != null) {
				p = location.indexOf('@');
				if (p >= 0) {
					String x = location.substring(0, p);
					location = location.substring(p+1);
					p = x.indexOf(':');
					if (p>=0) {
						username = x.substring(0, p);
						password = MPassword.encode(x.substring(p+1));
					} else {
						username = x;
					}
				}
			}
			
			// find end of path
			int p1 = path.indexOf(';');
			int p2 = path.indexOf('?');
			int p3 = path.indexOf('#');
			
			p = -1;
			// params
			if (p1>=0 && (p2 == -1 || p1 < p2) && (p3 == -1 || p1 < p3)) {
				int end = p2 >= 0 ? p2 : (p3 >= 0 ? p3 : path.length());
				String params = path.substring(p1+1, end);
				this.params = params.split(";");
				// decode params
				for (int i = 0; i < this.params.length; i++)
					this.params[i] = decode(this.params[i]);
				p = p1;
			}
			
			// query
			if (p2>=0 && (p3 == -1 || p2 < p3) && p2 > p1) {
				int end = p3 >= 0 ? p3 : path.length();
				String q = path.substring(p2+1, end);
				query = explode(q);
				if (p1 == -1)
					p= p2;
			}
			
			// fragment
			if (p3>=0 && p3 > p1 && p3 > p2) {
				fragment = decode(path.substring(p3+1));
				if (p1 == -1 && p2 == -1)
					p = p3;
				
			}

			if (p != -1)
				path = path.substring(0,p);
			
			// special for 'file' and windows ...
			if (SCHEME_FILE.equals(scheme)) {
				path = path.replace('\\', '/');
			}
			this.path = path;
			this.pathParts = path.split("/");
			for (int i = 0; i < this.pathParts.length; i++)
				this.pathParts[i] = decode(this.pathParts[i]);

		}

	@Override
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@Override
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Map<String, String> getQuery() {
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	@Override
	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	@Override
	public String[] getPathParts() {
		return pathParts;
	}

	public void setPath(String[] pathParts) {
		this.pathParts = pathParts;
		StringBuilder path = new StringBuilder();
		for (String p : pathParts) {
			if (path.length() > 0) path.append('/');
			path.append(decode(p));
		}
		this.path = path.toString();
	}

	@Override
	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	@Override
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		this.pathParts = path.split("/");
		for (int i = 0; i < this.pathParts.length; i++)
			this.pathParts[i] = decode(this.pathParts[i]);
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.writeObject(toString());
	}
	private void readObject(java.io.ObjectInputStream in)
	     throws IOException, ClassNotFoundException {
		 parse((String)in.readObject());
	}
	
	/**
	 * Return the params as list or an empty list
	 * @return The list, never null
	 */
	public LinkedList<String> getParamsAsList() {
		if (params == null) return new LinkedList<>();
		LinkedList<String> out = new LinkedList<>();
		for (String p : params)
			out.add(p);
		return out;
	}
	
	/**
	 * Set params to the values of list. If list is null
	 * or empty params is set to null.
	 * @param list
	 */
	public void setParamsAsList(LinkedList<String> list) {
		if (list == null || list.size() == 0) {
			params = null;
			return;
		}
		params = list.toArray(new String[list.size()]);
	}
	
}
