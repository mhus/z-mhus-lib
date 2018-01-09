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
