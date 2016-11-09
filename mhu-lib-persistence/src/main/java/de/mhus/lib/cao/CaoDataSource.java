package de.mhus.lib.cao;

public interface CaoDataSource {

	String getName();

	String getType();

	CaoConnection getConnection() throws Exception;
	
}
