package de.mhus.lib.jpa;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

public class JpaQuery<T> implements TypedQuery<T> {

	private JpaEntityManager entityManager;
	private Query query;

	public JpaQuery(JpaEntityManager entityManager, Query query) {
		this.entityManager = entityManager;
		this.query = query;
	}

	public JpaEntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> getResultList() {
		return new JpaList<T>(entityManager, query.getResultList() );
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getSingleResult() {
		return (T) query.getSingleResult();
	}

	@Override
	public int executeUpdate() {
		return query.executeUpdate();
	}

	@Override
	public JpaQuery<T> setMaxResults(int maxResult) {
		return new JpaQuery<T>( entityManager, query.setMaxResults(maxResult) );
	}

	@Override
	public int getMaxResults() {
		return query.getMaxResults();
	}

	@Override
	public JpaQuery<T> setFirstResult(int startPosition) {
		return new JpaQuery<T>(entityManager,query.setFirstResult(startPosition));
	}

	@Override
	public int getFirstResult() {
		return query.getFirstResult();
	}

	@Override
	public JpaQuery<T> setHint(String hintName, Object value) {
		return new JpaQuery<T>(entityManager,query.setHint(hintName, value));
	}

	@Override
	public Map<String, Object> getHints() {
		return query.getHints();
	}

	@Override
	public JpaQuery<T> setParameter(Parameter<Calendar> param, Calendar value,
			TemporalType temporalType) {
		return new JpaQuery<T>(entityManager,query.setParameter(param, value, temporalType));
	}

	@Override
	public JpaQuery<T> setParameter(Parameter<Date> param, Date value,
			TemporalType temporalType) {
		return new JpaQuery<T>(entityManager,query.setParameter(param, value, temporalType));
	}

	@Override
	public JpaQuery<T> setParameter(String name, Object value) {
		return new JpaQuery<T>(entityManager,query.setParameter(name, value));
	}

	@Override
	public JpaQuery<T> setParameter(String name, Calendar value,
			TemporalType temporalType) {
		return new JpaQuery<T>(entityManager,query.setParameter(name, value, temporalType));
	}

	@Override
	public TypedQuery<T> setParameter(String name, Date value, TemporalType temporalType) {
		return new JpaQuery<T>(entityManager,query.setParameter(name, value, temporalType));
	}

	@Override
	public TypedQuery<T> setParameter(int position, Object value) {
		return new JpaQuery<T>(entityManager,query.setParameter(position, value));
	}

	@Override
	public TypedQuery<T> setParameter(int position, Calendar value,
			TemporalType temporalType) {
		return new JpaQuery<T>(entityManager,query.setParameter(position, value, temporalType));
	}

	@Override
	public TypedQuery<T> setParameter(int position, Date value,
			TemporalType temporalType) {
		return new JpaQuery<T>(entityManager,query.setParameter(position, value, temporalType));
	}

	@Override
	public Set<Parameter<?>> getParameters() {
		return query.getParameters();
	}

	@Override
	public Parameter<?> getParameter(String name) {
		return query.getParameter(name);
	}

	@Override
	public <E> Parameter<E> getParameter(String name, Class<E> type) {
		return query.getParameter(name, type);
	}

	@Override
	public Parameter<?> getParameter(int position) {
		return query.getParameter(position);
	}

	@Override
	public <E> Parameter<E> getParameter(int position, Class<E> type) {
		return query.getParameter(position, type);
	}

	@Override
	public boolean isBound(Parameter<?> param) {
		return query.isBound(param);
	}

	@Override
	public <E> E getParameterValue(Parameter<E> param) {
		return query.getParameterValue(param);
	}

	@Override
	public Object getParameterValue(String name) {
		return query.getParameterValue(name);
	}

	@Override
	public Object getParameterValue(int position) {
		return query.getParameterValue(position);
	}

	@Override
	public JpaQuery<T> setFlushMode(FlushModeType flushMode) {
		return new JpaQuery<T>(entityManager,query.setFlushMode(flushMode));
	}

	@Override
	public FlushModeType getFlushMode() {
		return query.getFlushMode();
	}

	@Override
	public JpaQuery<T> setLockMode(LockModeType lockMode) {
		return new JpaQuery<T>(entityManager,query.setLockMode(lockMode));
	}

	@Override
	public LockModeType getLockMode() {
		return query.getLockMode();
	}

	@Override
	public <E> E unwrap(Class<E> cls) {
		return query.unwrap(cls);
	}

	@Override
	public <E> TypedQuery<T> setParameter(Parameter<E> param,E value) {
		return new JpaQuery<T>(entityManager,query.setParameter(param, value));
	}

}
