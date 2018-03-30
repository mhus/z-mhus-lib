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
package de.mhus.lib.core.security;

import de.mhus.lib.basics.Ace;

public class MutableAce extends Ace {

	public MutableAce() {}
	
	public MutableAce(String rights) {
		super(rights);
	}
	
	public void setFlag(char f) {
		if (rights == null) { rights = String.valueOf(f); return; }
		if (rights.indexOf(f) > -1) return;
		rights = rights + f;
	}

	public void removeFlag(char f) {
		if (rights == null) return;
		int p = rights.indexOf(f);
		if (p < 0) return;
		rights = rights.substring(0,p) + rights.substring(p+1);
	}
	
	public static Ace unionOfAces(Ace first, Ace second) {
		if (first == null && second == null) return new Ace(RIGHTS_NONE);
		if (first == null) return second;
		if (second == null) return first;
		MutableAce next = new MutableAce(first.getRights());
		for (int i = 0; i < second.getRights().length(); i++)
			next.setFlag(second.getRights().charAt(i));
		return next;
	}

}
