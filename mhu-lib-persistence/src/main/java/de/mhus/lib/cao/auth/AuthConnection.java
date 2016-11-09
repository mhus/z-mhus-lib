package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoActionList;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MException;

public class AuthConnection extends CaoConnection {

	protected CaoConnection instance;
	private Authorizator auth;
	private AuthNode root;

	public AuthConnection(CaoConnection instance, Authorizator auth) throws MException {
		super(instance.getName(), instance.getDriver());
		this.instance = instance;
		this.auth = auth;
		registerAspectFactory(AuthAccess.class, new AuthAccessFactory(this));
	}

	public AuthConnection(String name, CaoConnection instance) {
		super(name, instance.getDriver());
		this.instance = instance;
	}
	
	@Override
	public CaoNode getResourceByPath(String path) {
		CaoNode n = instance.getResourceByPath(path);
		if (n == null || !hasReadAccess(n)) return null;
		return new AuthNode( this, n );
	}

	@Override
	public String toString() {
		return instance.toString();
	}

	@Override
	public Log log() {
		return instance.log();
	}

	@Override
	public CaoNode getResourceById(String id) {
		CaoNode n = instance.getResourceById(id);
		if (n == null || !hasReadAccess(n)) return null;
		return new AuthNode(this, n );
	}

	@Override
	public CaoNode getRoot() {
		if (root == null)
			root = new AuthNode(this, instance.getRoot() );
		if (!hasReadAccess(instance.getRoot())) return null;
		return root;
	}

	@Override
	public CaoActionList getActions() {
		MutableActionList out = new MutableActionList();
		for (CaoAction action : instance.getActions() )
			if (hasActionAccess(action))
				out.add(new AuthAction(this, action));
		return out;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CaoAspect> CaoAspectFactory<T> getAspectFactory(Class<T> ifc) {
		CaoAspectFactory<T> mine = super.getAspectFactory(ifc);
		if (mine != null) return mine;
		return instance.getAspectFactory(ifc);
	}

	@Override
	public boolean equals(Object obj) {
		return instance.equals(obj);
	}
	
	public Authorizator getAuthorizator() {
		return auth;
	}
	
	public boolean hasReadAccess(CaoNode node) {
		if (auth == null) return true;
		return auth.hasReadAccess(node);
	}
	
	public boolean hasWriteAccess(CaoNode node) {
		if (auth == null) return true;
		return auth.hasWriteAccess(node);
	}
	
	public boolean hasActionAccess(CaoAction action) {
		if (auth == null) return true;
		return auth.hasActionAccess(action);
	}

	public boolean hasReadAccess(CaoNode node, String name) {
		if (auth == null) return true;
		return auth.hasReadAccess(node, name);
	}

	public boolean hasWriteAccess(CaoNode node, String name) {
		if (auth == null) return true;
		return auth.hasWriteAccess(node, name);
	}
	
	public boolean hasContentAccess(CaoNode node, String rendition) {
		if (auth == null) return true;
		return auth.hasContentAccess(node, rendition);
	}

	public boolean hasAspectAccess(CaoNode node, Class<? extends CaoAspect> ifc) {
		if (auth == null) return true;
		return auth.hasAspectAccess(node, ifc);
	}
}
