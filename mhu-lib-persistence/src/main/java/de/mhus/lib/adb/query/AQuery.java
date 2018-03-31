/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.adb.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.mhus.lib.core.parser.AttributeMap;
import de.mhus.lib.core.pojo.MPojo;

/**
 * <p>AQuery class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @param <T> 
 */
public class AQuery<T> extends APrint {

	private LinkedList<AOperation> operations;
	private Class<? extends T> type;
	private ACreateContext context;
	
	/**
	 * <p>Constructor for AQuery.</p>
	 *
	 * @param type a {@link java.lang.Class} object.
	 * @param operations a {@link de.mhus.lib.adb.query.AOperation} object.
	 */
	public AQuery(Class<T> type, AOperation ... operations) {
		this.type = type;
		this.operations = new LinkedList<>();
		for (AOperation o : operations)
			this.operations.add(o);
	}

	/**
	 * 
	 * @param mask Masquerading Base Type
	 * @param type Real Type querying for
	 * @param operations Initial operations
	 */
	public AQuery(Class<T> mask, Class<? extends T> type, AOperation ... operations) {
		// mask is never used, it's only for the template type definition
		this.type = type;
		this.operations = new LinkedList<>();
		for (AOperation o : operations)
			this.operations.add(o);
	}
	
	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	public Class<? extends T> getType() {
		return type;
	}

	/**
	 * <p>getAttributes.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Object> getAttributes() {
		AttributeMap map = new AttributeMap();
		getAttributes(map);
		return map;
	}

	/** {@inheritDoc} */
	@Override
	public void getAttributes(AttributeMap map) {
		for (AOperation operation : operations)
			operation.getAttributes(map);
	}

	/**
	 * <p>eq.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> eq(AAttribute left, AAttribute right) {
		operations.add(Db.eq(left, right));
		return this;
	}

	/**
	 * <p>eq.</p>
	 *
	 * @param attr a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> eq(String attr, Object value) {
		operations.add(Db.eq(attr, value));
		return this;
	}

	/**
	 * <p>eq.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> eq(Function<T,?> getter, Object value) {
		operations.add(Db.eq(MPojo.toAttributeNameWithCache(getter), value));
		return this;
	}
	
	/**
	 * <p>ne.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> ne(AAttribute left, AAttribute right) {
		operations.add(Db.ne(left, right));
		return this;
	}

	/**
	 * <p>ne.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param right a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> ne(String left, Object right) {
		operations.add(Db.ne(Db.attr(left), Db.value(right)));
		return this;
	}

	/**
	 * <p>ne.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> ne(Function<T,?> getter, Object value) {
		operations.add(Db.ne(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>lt.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> lt(AAttribute left, AAttribute right) {
		operations.add(Db.lt(left, right));
		return this;
	}

	/**
	 * <p>lt.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param right a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> lt(String left, Object right) {
		operations.add(Db.lt(Db.attr(left), Db.value(right)));
		return this;
	}
	
	/**
	 * <p>lt.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> lt(Function<T,?> getter, Object value) {
		operations.add(Db.lt(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>le.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> le(AAttribute left, AAttribute right) {
		operations.add(Db.le(left, right));
		return this;
	}

	/**
	 * <p>le.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param right a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> le(String left, Object right) {
		operations.add(Db.le(Db.attr(left), Db.value(right)));
		return this;
	}
	
	/**
	 * <p>le.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> le(Function<T,?> getter, Object value) {
		operations.add(Db.le(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>gt.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> gt(AAttribute left, AAttribute right) {
		operations.add(Db.gt(left, right));
		return this;
	}

	/**
	 * <p>gt.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param right a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> gt(String left, Object right) {
		operations.add(Db.gt(Db.attr(left), Db.value(right)));
		return this;
	}
	
	/**
	 * <p>gt.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> gt(Function<T,?> getter, Object value) {
		operations.add(Db.gt(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>ge.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> ge(AAttribute left, AAttribute right) {
		operations.add(Db.ge(left, right));
		return this;
	}

	/**
	 * <p>ge.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param right a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> ge(String left, Object right) {
		operations.add(Db.ge(Db.attr(left), Db.value(right)));
		return this;
	}
	
	/**
	 * <p>ge.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> ge(Function<T,?> getter, Object value) {
		operations.add(Db.ge(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>el.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> el(AAttribute left, AAttribute right) {
		operations.add(Db.el(left, right));
		return this;
	}

	/**
	 * <p>el.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> el(Function<T,?> getter, Object value) {
		operations.add(Db.el(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>eg.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> eg(AAttribute left, AAttribute right) {
		operations.add(Db.eg(left, right));
		return this;
	}

	/**
	 * <p>eg.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> eg(Function<T,?> getter, Object value) {
		operations.add(Db.eg(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>like.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param right a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> like(String left, String right) {
		operations.add(Db.like(Db.attr(left), Db.value(right)));
		return this;
	}

	/**
	 * <p>like.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> like(AAttribute left, AAttribute right) {
		operations.add(Db.like(left, right));
		return this;
	}
	
	/**
	 * <p>like.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> like(Function<T,?> getter, Object value) {
		operations.add(Db.like(Db.attr(MPojo.toAttributeNameWithCache(getter)), Db.value(value)));
		return this;
	}

	/**
	 * <p>and.</p>
	 *
	 * @param parts a {@link de.mhus.lib.adb.query.APart} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> and(APart ... parts) {
		operations.add(Db.and(parts));
		return this;
	}

	/**
	 * <p>or.</p>
	 *
	 * @param parts a {@link de.mhus.lib.adb.query.APart} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> or(APart ... parts) {
		operations.add(Db.or(parts));
		return this;
	}

	/**
	 * <p>order.</p>
	 *
	 * @param order a {@link de.mhus.lib.adb.query.AOrder} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> order(AOrder order) {
		operations.add(order);
		return this;
	}

	/**
	 * <p>asc.</p>
	 *
	 * @param attr a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> asc(String attr) {
		operations.add(new AOrder(type, attr, true));
		return this;
	}

	/**
	 * <p>asc.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> asc(Function<T,?> getter) {
		return asc(MPojo.toAttributeNameWithCache(getter));
	}

	
	/**
	 * <p>desc.</p>
	 *
	 * @param attr a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> desc(String attr) {
		operations.add(new AOrder(type, attr, false));
		return this;
	}

	/**
	 * <p>desc.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> desc(Function<T,?> getter) {
		return desc(MPojo.toAttributeNameWithCache(getter));
	}
	
	/**
	 * <p>literal.</p>
	 *
	 * @param parts a {@link de.mhus.lib.adb.query.APart} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> literal(APart ... parts) {
		operations.add(Db.literal(parts));
		return this;
	}

	/**
	 * <p>literal.</p>
	 *
	 * @param literal a {@link de.mhus.lib.adb.query.ALiteral} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> literal(ALiteral literal) {
		operations.add(Db.literal(literal));
		return this;
	}

	/**
	 * <p>not.</p>
	 *
	 * @param part a {@link de.mhus.lib.adb.query.APart} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> not(APart part) {
		operations.add(Db.not(part));
		return this;
	}

	/**
	 * <p>in.</p>
	 *
	 * @param left a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> in(AAttribute left, AAttribute ... right) {
		operations.add(Db.in(left, new AList(right) ));
		return this;
	}

	/**
	 * <p>in.</p>
	 *
	 * @param left a {@link java.util.function.Function} object.
	 * @param right a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	@Deprecated
	public AQuery<T> in(Function<T,?> left, AAttribute ... right) {
		operations.add(Db.in( Db.attr(MPojo.toAttributeNameWithCache(left)) , new AList(right) ));
		return this;
	}
	
	/**
	 * Append a sub query compare element.
	 *
	 * @param left Name of the Attribute in the base query (WHERE [left] IN (...) )
	 * @param projection Name of the attribute in the sub query (select [projection] FROM)
	 * @param subQuery The subquery itself
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> in(AAttribute left, AAttribute projection, AQuery<?> subQuery) {
		operations.add(Db.in(left, projection, subQuery ));
		return this;
	}
	
	/**
	 * <p>in.</p>
	 *
	 * @param left a {@link java.lang.String} object.
	 * @param projection a {@link java.lang.String} object.
	 * @param subQuery a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> in(String left, String projection, AQuery<?> subQuery) {
		operations.add(Db.in(Db.attr(left), Db.attr(projection), subQuery ));
		return this;
	}
	
	/**
	 * <p>limit.</p>
	 *
	 * @param limit a int.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> limit(int limit) {
		operations.add(Db.limit(limit));
		return this;
	}
	
	/**
	 * <p>isNull.</p>
	 *
	 * @param attr a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> isNull(AAttribute attr) {
		operations.add(Db.isNull(attr));
		return this;
	}
	
	/**
	 * <p>isNull.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> isNull(Function<T,?> getter) {
		operations.add(Db.isNull(Db.attr(MPojo.toAttributeNameWithCache(getter))));
		return this;
	}
	
	/**
	 * <p>isNotNull.</p>
	 *
	 * @param attr a {@link de.mhus.lib.adb.query.AAttribute} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> isNotNull(AAttribute attr) {
		operations.add(Db.isNotNull(attr));
		return this;
	}
	
	/**
	 * <p>isNotNull.</p>
	 *
	 * @param getter a {@link java.util.function.Function} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 * @since 3.3.0
	 */
	@Deprecated
	public AQuery<T> isNotNull(Function<T,?> getter) {
		operations.add(Db.isNotNull(Db.attr(MPojo.toAttributeNameWithCache(getter))));
		return this;
	}
	
	/**
	 * <p>isNull.</p>
	 *
	 * @param attr a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> isNull(String attr) {
		operations.add(Db.isNull(Db.attr(attr)));
		return this;
	}
	
	/**
	 * <p>isNotNull.</p>
	 *
	 * @param attr a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.adb.query.AQuery} object.
	 */
	public AQuery<T> isNotNull(String attr) {
		operations.add(Db.isNotNull(Db.attr(attr)));
		return this;
	}

	public ACreateContext getContext() {
		return context;
	}

	public void setContext(ACreateContext context) {
		this.context = context;
	}

	public List<AOperation> getOperations() {
		return operations;
	}

//	@SuppressWarnings("unchecked")
//	private synchronized void initRecorder() {
//		if (recorder == null)
//			recorder = (Recorder<T>) RecordingObject.create(type);
//	}

}
