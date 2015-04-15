package de.mhus.lib.jpa;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class JpaComfortable implements JpaInjection {

	@Transient
	protected JpaEntityManager entityManager;

	@Override
	public void doInjectJpa(JpaEntityManager entityManager) {
		this.entityManager = entityManager;
	}


	public boolean isManaged() {
		return entityManager != null && entityManager.isOpen();
	}

	public void save() {
		entityManager.persist(this);
	}

	public void revert() {
		entityManager.refresh(this);
	}

	public Object copy() {
		return entityManager.copy(this);
	}

	public void remove() {
		entityManager.remove(this);
	}

	public void detach() {
		entityManager.detach(this);
	}

}
