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
package de.mhus.lib.core.crypt.pem;

public class PemKeyPair implements PemPair {
	private PemPriv priv;
	private PemPub pub;
	
	public PemKeyPair(PemPriv priv, PemPub pub) {
		super();
		this.priv = priv;
		this.pub = pub;
	}

	@Override
	public PemPriv getPrivate() {
		return priv;
	}

	@Override
	public PemPub getPublic() {
		return pub;
	}
	
	@Override
	public String toString() {
		return (pub != null ? pub.toString() : "No Public Key\n") + (priv != null ? priv.toString() : "No Private Key\n");
	}

}
