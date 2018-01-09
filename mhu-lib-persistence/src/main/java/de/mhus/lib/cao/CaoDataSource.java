package de.mhus.lib.cao;

public interface CaoDataSource {

	String getName();

	String getType();

	CaoConnection getConnection() throws Exception;
	
	/**
	 * Returns a new CaoCore object if the action is not allowed it will throw an AccessDeniedException
	 * @return the core object
	 * @throws Exception
	 */
	CaoCore getCore() throws Exception;
}
