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
	
	public void close() {
		if (entityManager == null) return;
		log().t("close");
		entityManager.close();
		entityManager = null;
	}


	public void persist(Object entity) {
		log().t("persist");
		injectObject(entity);
		entityManager.persist(entity);
	}


	public <T> T merge(T entity) {
		log().t("merge",entity);
		injectObject(entity);
		return entityManager.merge(entity);
	}


	public void remove(Object entity) {
		log().t("remove",entity);
		injectObject(entity);
		entityManager.remove(entity);
	}


	public <T> T find(Class<T> entityClass, Object primaryKey) {
		log().t("find",entityClass,primaryKey);
		return injectObject( entityManager.find(entityClass, primaryKey) );
	}


	public <T> T find(Class<T> entityClass, Object primaryKey,
			Map<String, Object> properties) {
		log().t("find",entityClass,primaryKey,properties);
		return injectObject(entityManager.find(entityClass, primaryKey, properties));
	}


	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode) {
		log().t("find",entityClass,primaryKey,lockMode);
		return injectObject(entityManager.find(entityClass, primaryKey, lockMode));
	}


	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode, Map<String, Object> properties) {
		log().t("find",entityClass,primaryKey,lockMode,properties);
		return entityManager
				.find(entityClass, primaryKey, lockMode, properties);
	}


	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		log().t("getReference",entityClass,primaryKey);
		return injectObject(entityManager.getReference(entityClass, primaryKey));
	}


	public void flush() {
		log().t("flush");
		entityManager.flush();
	}


	public void setFlushMode(FlushModeType flushMode) {
		log().t("flush mode",flushMode);
		entityManager.setFlushMode(flushMode);
	}


	public FlushModeType getFlushMode() {
		return entityManager.getFlushMode();
	}


	public void lock(Object entity, LockModeType lockMode) {
		log().t("lock",entity,lockMode);
		injectObject(entity);
		entityManager.lock(entity, lockMode);
	}


	public void lock(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		log().t("lock",entity,lockMode,properties);
		injectObject(entity);
		entityManager.lock(entity, lockMode, properties);
	}


	public void refresh(Object entity) {
		log().t("refresh",entity);
		injectObject(entity);
		entityManager.refresh(entity);
	}


	public void refresh(Object entity, Map<String, Object> properties) {
		log().t("refresh",entity,properties);
		injectObject(entity);
		entityManager.refresh(entity, properties);
	}


	public void refresh(Object entity, LockModeType lockMode) {
		log().t("refresh",entity,lockMode);
		injectObject(entity);
		entityManager.refresh(entity, lockMode);
	}


	public void refresh(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		log().t("refresh",entity,lockMode,properties);
		injectObject(entity);
		entityManager.refresh(entity, lockMode, properties);
	}


	public void clear() {
		log().t("clear");
		entityManager.clear();
	}


	public void detach(Object entity) {
		log().t("detach",entity);
		injectObject(entity);
		entityManager.detach(entity);
	}


	public boolean contains(Object entity) {
		log().t("contains",entity);
		injectObject(entity);
		return entityManager.contains(entity);
	}


	public LockModeType getLockMode(Object entity) {
		injectObject(entity);
		return entityManager.getLockMode(entity);
	}


	public void setProperty(String propertyName, Object value) {
		log().t("property",propertyName,value);
		entityManager.setProperty(propertyName, value);
	}


	public Map<String, Object> getProperties() {
		return entityManager.getProperties();
	}


	public JpaQuery<?> createQuery(String qlString) {
		log().t("create query",qlString);
		return new JpaQuery<Object>( this, entityManager.createQuery(qlString) );
	}


	public <T> JpaQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		log().t("create query",criteriaQuery);
		return new JpaQuery<T>( this, entityManager.createQuery(criteriaQuery));
	}


	public <T> JpaQuery<T> createQuery(String qlString, Class<T> resultClass) {
		log().t("create query",qlString,resultClass);
		return new JpaQuery<T>(this, entityManager.createQuery(qlString, resultClass));
	}


	public Query createNamedQuery(String name) {
		log().t("create named query",name);
		return new JpaQuery<Object>( this, entityManager.createNamedQuery(name) );
	}


	public <T> JpaQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		log().t("create named query",name,resultClass);
		return new JpaQuery<T>(this, entityManager.createNamedQuery(name, resultClass) );
	}


	public Query createNativeQuery(String sqlString) {
		log().t("create native query",sqlString);
		return entityManager.createNativeQuery(sqlString);
	}


	@SuppressWarnings("rawtypes")
	public Query createNativeQuery(String sqlString, Class resultClass) {
		log().t("create native query",sqlString,resultClass);
		return entityManager.createNativeQuery(sqlString, resultClass);
	}


	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		log().t("create native query",sqlString,resultSetMapping);
		return entityManager.createNativeQuery(sqlString, resultSetMapping);
	}


	public void joinTransaction() {
		log().t("join transaction");
		entityManager.joinTransaction();
	}


	public <T> T unwrap(Class<T> cls) {
		log().t("unwrap", cls);
		return injectObject(entityManager.unwrap(cls));
	}


	public Object getDelegate() {
		return entityManager.getDelegate();
	}


	public boolean isOpen() {
		return entityManager != null && entityManager.isOpen();
	}


	public EntityTransaction getTransaction() {
		return entityManager.getTransaction();
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}


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
