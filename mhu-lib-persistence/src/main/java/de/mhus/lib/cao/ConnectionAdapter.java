package de.mhus.lib.cao;

import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MException;

public class ConnectionAdapter extends CaoConnection {

	private CaoCore core;

	public ConnectionAdapter(CaoCore core) {
		this.core = core;
	}

	public CaoConnection getConnection() {
		return core.getConnection();
	}

	@Override
	public String toString() {
		return core.toString();
	}

	@Override
	public CaoNode getResourceByPath(String path) {
		return core.getResourceByPath(path);
	}

	@Override
	public CaoNode getResourceById(String id) {
		return core.getResourceById(id);
	}

	@Override
	public CaoDriver getDriver() {
		return core.getDriver();
	}

	@Override
	public CaoNode getRoot() {
		return core.getRoot();
	}

	@Override
	public CaoActionList getActions() {
		return core.getActions();
	}

	@Override
	public String getName() {
		return core.getName();
	}

	@Override
	public CaoList executeQuery(String space, String query) throws MException {
		return core.executeQuery(space, query);
	}

	@Override
	public <T> CaoList executeQuery(String space, AQuery<T> query) throws MException {
		return core.executeQuery(space, query);
	}

	@Override
	public CaoAction getAction(String name) {
		return core.getAction(name);
	}

	@Override
	public <T extends CaoAspect> CaoAspectFactory<T> getAspectFactory(Class<T> ifc) {
		return core.getAspectFactory(ifc);
	}
	
}
