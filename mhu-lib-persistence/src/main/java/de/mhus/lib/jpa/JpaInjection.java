package de.mhus.lib.jpa;

/**
 * <p>JpaInjection interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface JpaInjection {

	/**
	 * <p>doInjectJpa.</p>
	 *
	 * @param entityManager a {@link de.mhus.lib.jpa.JpaEntityManager} object.
	 */
	void doInjectJpa(JpaEntityManager entityManager);

}
