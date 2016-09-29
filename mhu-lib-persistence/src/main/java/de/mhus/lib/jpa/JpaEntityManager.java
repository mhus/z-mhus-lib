package de.mhus.lib.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import org.apache.openjpa.persistence.OpenJPAEntityManager;

import de.mhus.lib.core.lang.MObject;

public class JpaEntityManager extends MObject implements EntityManager {

	private JpaManager manager;
	private EntityManager entityManager;

	@SuppressWarnings("rawtypes")
	public JpaEntityManager(JpaManager manager, EntityManagerFactory factory, Map map) {
		this.manager = manager;
		entityManager = factory.createEntityManager(map);
	}

	public <T> T injectObject(T object) {
		if (object != null && object instanceof JpaInjection) {
			((JpaInjection)object).doInjectJpa(this);
		}
		return object;
	}

	public JpaManager getManager() {
		return manager;
	}

	@Override
	public void close() {
		if (entityManager == null) return;
		log().t("close");
		entityManager.close();
		entityManager = null;
	}


	@Override
	public void persist(Object entity) {
		log().t("persist");
		injectObject(entity);
		entityManager.persist(entity);
	}


	@Override
	public <T> T merge(T entity) {
		log().t("merge",entity);
		injectObject(entity);
		return entityManager.merge(entity);
	}


	@Override
	public void remove(Object entity) {
		log().t("remove",entity);
		injectObject(entity);
		entityManager.remove(entity);
	}


	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		log().t("find",entityClass,primaryKey);
		return injectObject( entityManager.find(entityClass, primaryKey) );
	}


	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey,
			Map<String, Object> properties) {
		log().t("find",entityClass,primaryKey,properties);
		return injectObject(entityManager.find(entityClass, primaryKey, properties));
	}


	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode) {
		log().t("find",entityClass,primaryKey,lockMode);
		return injectObject(entityManager.find(entityClass, primaryKey, lockMode));
	}


	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode, Map<String, Object> properties) {
		log().t("find",entityClass,primaryKey,lockMode,properties);
		return entityManager
				.find(entityClass, primaryKey, lockMode, properties);
	}


	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		log().t("getReference",entityClass,primaryKey);
		return injectObject(entityManager.getReference(entityClass, primaryKey));
	}


	@Override
	public void flush() {
		log().t("flush");
		entityManager.flush();
	}


	@Override
	public void setFlushMode(FlushModeType flushMode) {
		log().t("flush mode",flushMode);
		entityManager.setFlushMode(flushMode);
	}


	@Override
	public FlushModeType getFlushMode() {
		return entityManager.getFlushMode();
	}


	@Override
	public void lock(Object entity, LockModeType lockMode) {
		log().t("lock",entity,lockMode);
		injectObject(entity);
		entityManager.lock(entity, lockMode);
	}


	@Override
	public void lock(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		log().t("lock",entity,lockMode,properties);
		injectObject(entity);
		entityManager.lock(entity, lockMode, properties);
	}


	@Override
	public void refresh(Object entity) {
		log().t("refresh",entity);
		injectObject(entity);
		entityManager.refresh(entity);
	}


	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		log().t("refresh",entity,properties);
		injectObject(entity);
		entityManager.refresh(entity, properties);
	}


	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		log().t("refresh",entity,lockMode);
		injectObject(entity);
		entityManager.refresh(entity, lockMode);
	}


	@Override
	public void refresh(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		log().t("refresh",entity,lockMode,properties);
		injectObject(entity);
		entityManager.refresh(entity, lockMode, properties);
	}


	@Override
	public void clear() {
		log().t("clear");
		entityManager.clear();
	}


	@Override
	public void detach(Object entity) {
		log().t("detach",entity);
		injectObject(entity);
		entityManager.detach(entity);
	}


	@Override
	public boolean contains(Object entity) {
		log().t("contains",entity);
		injectObject(entity);
		return entityManager.contains(entity);
	}


	@Override
	public LockModeType getLockMode(Object entity) {
		injectObject(entity);
		return entityManager.getLockMode(entity);
	}


	@Override
	public void setProperty(String propertyName, Object value) {
		log().t("property",propertyName,value);
		entityManager.setProperty(propertyName, value);
	}


	@Override
	public Map<String, Object> getProperties() {
		return entityManager.getProperties();
	}


	@Override
	public JpaQuery<?> createQuery(String qlString) {
		log().t("create query",qlString);
		return new JpaQuery<Object>( this, entityManager.createQuery(qlString) );
	}


	@Override
	public <T> JpaQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		log().t("create query",criteriaQuery);
		return new JpaQuery<T>( this, entityManager.createQuery(criteriaQuery));
	}


	@Override
	public <T> JpaQuery<T> createQuery(String qlString, Class<T> resultClass) {
		log().t("create query",qlString,resultClass);
		return new JpaQuery<T>(this, entityManager.createQuery(qlString, resultClass));
	}


	@Override
	public Query createNamedQuery(String name) {
		log().t("create named query",name);
		return new JpaQuery<Object>( this, entityManager.createNamedQuery(name) );
	}


	@Override
	public <T> JpaQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		log().t("create named query",name,resultClass);
		return new JpaQuery<T>(this, entityManager.createNamedQuery(name, resultClass) );
	}


	@Override
	public Query createNativeQuery(String sqlString) {
		log().t("create native query",sqlString);
		return entityManager.createNativeQuery(sqlString);
	}


	@Override
	@SuppressWarnings("rawtypes")
	public Query createNativeQuery(String sqlString, Class resultClass) {
		log().t("create native query",sqlString,resultClass);
		return entityManager.createNativeQuery(sqlString, resultClass);
	}


	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		log().t("create native query",sqlString,resultSetMapping);
		return entityManager.createNativeQuery(sqlString, resultSetMapping);
	}


	@Override
	public void joinTransaction() {
		log().t("join transaction");
		entityManager.joinTransaction();
	}


	@Override
	public <T> T unwrap(Class<T> cls) {
		log().t("unwrap", cls);
		return injectObject(entityManager.unwrap(cls));
	}


	@Override
	public Object getDelegate() {
		return entityManager.getDelegate();
	}


	@Override
	public boolean isOpen() {
		return entityManager != null && entityManager.isOpen();
	}


	@Override
	public EntityTransaction getTransaction() {
		return entityManager.getTransaction();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}


	@Override
	public Metamodel getMetamodel() {
		return entityManager.getMetamodel();
	}

	public void begin() {
		log().t("begin");
		getTransaction().begin();
	}

	public void commit() {
		log().t("commit");
		getTransaction().commit();
	}

	public void rollback() {
		log().t("rollback");
		getTransaction().rollback();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return manager;
	}

	public <T> T copy(T object) {
		log().t("copy",object);
		return injectObject(((OpenJPAEntityManager)entityManager).detachCopy(object));
	}

}
