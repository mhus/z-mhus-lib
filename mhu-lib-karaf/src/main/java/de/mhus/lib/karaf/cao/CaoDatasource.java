package de.mhus.lib.karaf.cao;

import de.mhus.lib.cao.CaoConnection;

public interface CaoDatasource {

	String getName();

	String getType();

	CaoConnection getConnection();
	
}
