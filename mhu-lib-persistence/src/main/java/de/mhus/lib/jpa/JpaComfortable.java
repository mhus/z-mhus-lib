package de.mhus.lib.jpa;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
/**
 * <p>JpaComfortable class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JpaComfortable implements JpaInjection {

	@Transient
	protected JpaEntityManager entityManager;

	/** {@inheritDoc} */
	@Override
	public void doInjectJpa(JpaEntityManager entityManager) {
		this.entityManager = entityManager;
	}


	/**
	 * <p>isManaged.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isManaged() {
		return entityManager != null && entityManager.isOpen();
	}

	/**
	 * <p>save.</p>
	 */
	public void save() {
		entityManager.persist(this);
	}

	/**
	 * <p>revert.</p>
	 */
	public void revert() {
		entityManager.refresh(this);
	}

	/**
	 * <p>copy.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object copy() {
		return entityManager.copy(this);
	}

	/**
	 * <p>remove.</p>
	 */
	public void remove() {
		entityManager.remove(this);
	}

	/**
	 * <p>detach.</p>
	 */
	public void detach() {
		entityManager.detach(this);
	}

}
