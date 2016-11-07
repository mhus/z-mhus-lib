package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDataSource;

public class SharedDataSource implements CaoDataSource {

	private CaoConnection shared;
	private String name;
	private String type;

	public SharedDataSource(CaoConnection shared) {
		this(shared.getName(), shared);
	}
	
	public SharedDataSource(String name, CaoConnection shared) {
		this.shared = shared;
		this.name = name;
		this.type = shared.getClass().getSimpleName();
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

}
