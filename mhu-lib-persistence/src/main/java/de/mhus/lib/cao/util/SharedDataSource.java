package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDataSource;
import de.mhus.lib.core.MSystem;

public class SharedDataSource implements CaoDataSource {

	private CaoConnection shared;
	private String name;
	private String type;

	public SharedDataSource() {
		
	}
	
	public SharedDataSource(CaoConnection shared) {
		this(shared.getName(), shared);
	}
	
	public SharedDataSource(String name, CaoConnection shared) {
		this.shared = shared;
		this.name = name;
		this.type = shared.getClass().getSimpleName();
	}

	public void setConnection(CaoConnection connection) {
		this.shared = connection;
		this.type = connection.getClass().getCanonicalName();
		if (name == null) name = connection.getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public CaoConnection getConnection() {
		return shared;
	}

	@Override
	public String toString() {
		return MSystem.toString(this, shared );
	}

}
