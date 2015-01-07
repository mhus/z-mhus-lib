package de.mhus.lib.jpa;

import java.util.Map;
import java.util.UUID;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import de.mhus.lib.core.lang.MObject;


public class JpaManager extends MObject implements EntityManagerFactory {

	private JpaSchema schema;
	private JpaProperties properties;
	private EntityManagerFactory entityManagerFactory;
	private UUID unitId;

	public JpaManager(JpaProperties properties) {
		this(properties, null);
	}
	
	public JpaManager(JpaProperties properties, JpaSchema schema) {
		if (schema != null) properties.setSchema(schema);
		this.schema = properties.getSchema();
		this.properties = properties;
		initJpa();
	}

	protected void initJpa() {
		log().t("init");
		unitId = UUID.randomUUID();
		schema.doInit(properties.getConfig());
		properties.configureTypes();
		entityManagerFactory = Persistence.createEntityManagerFactory(schema.getSchemaName() + "-" + unitId.toString(), properties);
		schema.doPostInit(this);
	}
	
	public JpaSchema getSchema() {
		return schema;
	}
	
	public JpaEntityManager createEntityManager() {
	    return new JpaEntityManager(this, entityManagerFactory, null);
	}
	
	public void close() {
 		if (entityManagerFactory == null) return;
 		log().t("close");
		entityManagerFactory.close();
		entityManagerFactory = null;
	}

	@Override
	public EntityManager createEntityManager(@SuppressWarnings("rawtypes") Map map) {
		log().t("create entity manager",map);
		return new JpaEntityManager(this, entityManagerFactory, map);
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManagerFactory.getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return entityManagerFactory.getMetamodel();
	}

	@Override
	public boolean isOpen() {
		return entityManagerFactory != null && entityManagerFactory.isOpen();
	}

	@Override
	public Map<String, Object> getProperties() {
		return entityManagerFactory.getProperties();
	}

	@Override
	public Cache getCache() {
		return entityManagerFactory.getCache();
	}

	@Override
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return entityManagerFactory.getPersistenceUnitUtil();
	}
	
}
