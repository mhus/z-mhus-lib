package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoActionList;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.core.logging.Log;

public class AuthConnection extends CaoConnection {

	protected CaoConnection instance;

	public AuthConnection(CaoConnection instance) {
		super(instance.getName(), instance.getDriver());
		this.instance = instance;
	}

	public AuthConnection(String name, CaoConnection instance) {
		super(name, instance.getDriver());
		this.instance = instance;
	}
	
	@Override
	public CaoNode getResourceByPath(String path) {
		return new AuthNode( this, instance.getResourceByPath(path) );
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
		return new AuthNode(this, instance.getResourceById(id) );
	}

	@Override
	public CaoNode getRoot() {
		return new AuthNode(this, instance.getRoot() );
	}

	@Override
	public CaoActionList getActions() {
		MutableActionList out = new MutableActionList();
		for (CaoAction action : instance.getActions() )
			out.add(new AuthAction(this, action));
		return out;
	}

	@Override
	public boolean supportVersions() {
		return instance.supportVersions();
	}

	@Override
	public void addAspectFactory(Class<? extends CaoAspect> ifc, CaoAspectFactory factory) {
		instance.addAspectFactory(ifc, factory);
	}

	@Override
	public <T extends CaoAspectFactory> T getAspectFactory(Class<? extends CaoAspect> ifc) {
		return instance.getAspectFactory(ifc);
	}

	@Override
	public boolean equals(Object obj) {
		return instance.equals(obj);
	}
	
}
