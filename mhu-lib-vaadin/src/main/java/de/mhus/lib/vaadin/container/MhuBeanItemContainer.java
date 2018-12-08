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
package de.mhus.lib.vaadin.container;

/*
 * Copyright 2000-2013 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;

import de.mhus.lib.annotations.vaadin.Column;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.vaadin.ColumnModel;
import de.mhus.lib.vaadin.MhuTable;
import de.mhus.lib.vaadin.container.MhuBeanItem.PojoPropertyDescriptor;

/**
 * An in-memory container for JavaBeans.
 * 
 * <p>
 * The properties of the container are determined automatically by introspecting
 * the used JavaBean class. Only beans of the same type can be added to the
 * container.
 * </p>
 * 
 * <p>
 * BeanItemContainer uses the beans themselves as identifiers. The
 * {@link Object#hashCode()} of a bean is used when storing and looking up beans
 * so it must not change during the lifetime of the bean (it should not depend
 * on any part of the bean that can be modified). Typically this restricts the
 * implementation of {@link Object#equals(Object)} as well in order for it to
 * fulfill the contract between {@code equals()} and {@code hashCode()}.
 * </p>
 * 
 * <p>
 * To add items to the container, use the methods {@link #addBean(Object)},
 * {@link #addBeanAfter(Object, Object)} and {@link #addBeanAt(int, Object)}.
 * Also {@link #addItem(Object)}, {@link #addItemAfter(Object, Object)} and
 * {@link #addItemAt(int, Object)} can be used as synonyms for them.
 * </p>
 * 
 * <p>
 * It is not possible to add additional properties to the container.
 * </p>
 * 
 * @param <BEANTYPE>
 *            The type of the Bean
 * 
 * @since 5.4
 */
@SuppressWarnings({"serial","deprecation"})
public class MhuBeanItemContainer<BEANTYPE> extends
        MhuAbstractBeanContainer<BEANTYPE, BEANTYPE> {

	
    public void refresh() {
    	fireItemSetChange();
    }

	public void configureTableByAnnotations(Table table, String schema, MNls nls) {
		// collect possible columns
		TreeMap<Integer,String> columns = new TreeMap<Integer, String>();
		int nextOrderId = 100;
		for (String colId : getContainerPropertyIds()) {
			
			PojoPropertyDescriptor<BEANTYPE> descriptor = model.get(colId);
			Column columnDef = descriptor.getPojoAttribute().getAnnotation(Column.class);
			if (columnDef != null) {
				
				if (
						MString.isEmpty(schema) && columnDef.schema().length == 0
						||
						MCollection.contains(columnDef.schema(), schema)
						) {
					int order = columnDef.order();
					if (order < 0)
						order = ++nextOrderId;
					columns.put(order, colId);
				}
				
			}
		}
		
		table.setVisibleColumns(columns.values().toArray());
		
		for (String colId : columns.values()) {
			PojoPropertyDescriptor<BEANTYPE> descriptor = model.get(colId);
			Column columnDef = descriptor.getPojoAttribute().getAnnotation(Column.class);
			
			// find title
			String title = null;
			if (nls != null) {
				String n = colId;
				if (MString.isSet(columnDef.nls())) n = columnDef.nls();
				title = nls.find(n);
			}
			if (title == null && !MString.isEmpty(columnDef.title()))
				title = columnDef.title();
			if (title != null)
				table.setColumnHeader(colId, title);

			// align
			table.setColumnAlignment(colId, mapToVaadin(columnDef.align()));
			
			// collapsed
			table.setColumnCollapsed(colId, !columnDef.elapsed());
			
			// collabsible
			table.setColumnCollapsible(colId, columnDef.collapsible());
			
			if (table instanceof MhuTable) {
				ColumnModel model = ((MhuTable)table).getColumnModel(colId);
				model.configureByAnnotation(columnDef, descriptor.getPojoAttribute().canWrite());
				table.setConverter(colId,model.generateConverter(descriptor.getPropertyType()));
			}
			
		}
		
		
	}
	
    public static Align mapToVaadin(de.mhus.lib.annotations.vaadin.Align align) {
    	switch (align) {
		case CENTER:
			return Align.CENTER;
		case LEFT:
			return Align.LEFT;
		case RIGHT:
			return Align.RIGHT;
    	}
		return Align.LEFT; // default is LEFT but should not happen !!
	}

	/**
     * Bean identity resolver that returns the bean itself as its item
     * identifier.
     * 
     * This corresponds to the old behavior of {@link MhuBeanItemContainer}, and
     * requires suitable (identity-based) equals() and hashCode() methods on the
     * beans.
     * 
     * @param <BT>
     * 
     * @since 6.5
     */
    private static class IdentityBeanIdResolver<BT> implements
            BeanIdResolver<BT, BT> {

        @Override
        public BT getIdForBean(BT bean) {
            return bean;
        }

    }

    /**
     * Constructs a {@code BeanItemContainer} for beans of the given type.
     * 
     * @param type
     *            the type of the beans that will be added to the container.
     * @throws IllegalArgumentException
     *             If {@code type} is null
     */
    public MhuBeanItemContainer(Class<? super BEANTYPE> type)
            throws IllegalArgumentException {
        super(type);
        super.setBeanIdResolver(new IdentityBeanIdResolver<BEANTYPE>());
    }

    /**
     * Constructs a {@code BeanItemContainer} and adds the given beans to it.
     * The collection must not be empty.
     * MhuBeanItemContainer BeanItemContainer(Class) can be used for
     * creating an initially empty {@code BeanItemContainer}.
     * 
     * Note that when using this constructor, the actual class of the first item
     * in the collection is used to determine the bean properties supported by
     * the container instance, and only beans of that class or its subclasses
     * can be added to the collection. If this is problematic or empty
     * collections need to be supported, use BeanItemContainer(Class)
     * and {@link #addAll(Collection)} instead.
     * 
     * @param collection
     *            a non empty {@link Collection} of beans.
     * @throws IllegalArgumentException
     *             If the collection is null or empty.
     * 
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public MhuBeanItemContainer(Collection<? extends BEANTYPE> collection)
            throws IllegalArgumentException {
        // must assume the class is BT
        // the class information is erased by the compiler
        this((Class<BEANTYPE>) getBeanClassForCollection(collection),
                collection);
    }

    /**
     * Internal helper method to support the deprecated {@link Collection}
     * container.
     * 
     * @param <BT>
     * @param collection
     * @return
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    private static <BT> Class<? extends BT> getBeanClassForCollection(
            Collection<? extends BT> collection)
            throws IllegalArgumentException {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(
                    "The collection passed to BeanItemContainer constructor must not be null or empty. Use the other BeanItemContainer constructor.");
        }
        return (Class<? extends BT>) collection.iterator().next().getClass();
    }

    /**
     * Constructs a {@code BeanItemContainer} and adds the given beans to it.
     * 
     * @param type
     *            the type of the beans that will be added to the container.
     * @param collection
     *            a {@link Collection} of beans (can be empty or null).
     * @throws IllegalArgumentException
     *             If {@code type} is null
     */
    public MhuBeanItemContainer(Class<? super BEANTYPE> type,
            Collection<? extends BEANTYPE> collection)
            throws IllegalArgumentException {
        super(type);
        super.setBeanIdResolver(new IdentityBeanIdResolver<BEANTYPE>());

        if (collection != null) {
            addAll(collection);
        }
    }

    /**
     * Adds all the beans from a {@link Collection} in one go. More efficient
     * than adding them one by one.
     * 
     * @param collection
     *            The collection of beans to add. Must not be null.
     */
    @Override
    public void addAll(Collection<? extends BEANTYPE> collection) {
        super.addAll(collection);
    }

    /**
     * Adds the bean after the given bean.
     * 
     * The bean is used both as the item contents and as the item identifier.
     * 
     * @param previousItemId
     *            the bean (of type BT) after which to add newItemId
     * @param newItemId
     *            the bean (of type BT) to add (not null)
     * 
     * com.vaadin.data.Container.Ordered#addItemAfter(Object, Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public MhuBeanItem<BEANTYPE> addItemAfter(Object previousItemId,
            Object newItemId) throws IllegalArgumentException {
        return super.addBeanAfter((BEANTYPE) previousItemId,
                (BEANTYPE) newItemId);
    }

    /**
     * Adds a new bean at the given index.
     * 
     * The bean is used both as the item contents and as the item identifier.
     * 
     * @param index
     *            Index at which the bean should be added.
     * @param newItemId
     *            The bean to add to the container.
     * @return Returns the new BeanItem or null if the operation fails.
     */
    @Override
    @SuppressWarnings("unchecked")
    public MhuBeanItem<BEANTYPE> addItemAt(int index, Object newItemId)
            throws IllegalArgumentException {
        return super.addBeanAt(index, (BEANTYPE) newItemId);
    }

    /**
     * Adds the bean to the Container.
     * 
     * The bean is used both as the item contents and as the item identifier.
     * 
     * com.vaadin.data.Container#addItem(Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public MhuBeanItem<BEANTYPE> addItem(Object itemId) {
        return super.addBean((BEANTYPE) itemId);
    }

    /**
     * Adds the bean to the Container.
     * 
     * The bean is used both as the item contents and as the item identifier.
     * 
     * com.vaadin.data.Container#addItem(Object)
     */
    @Override
    public MhuBeanItem<BEANTYPE> addBean(BEANTYPE bean) {
        return addItem(bean);
    }

    /**
     * Unsupported in BeanItemContainer.
     */
    @Override
    protected void setBeanIdResolver(
            MhuAbstractBeanContainer.BeanIdResolver<BEANTYPE, BEANTYPE> beanIdResolver)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "BeanItemContainer always uses an IdentityBeanIdResolver");
    }

    @Override
	public boolean mergeAll(Collection<? extends BEANTYPE> collection, boolean remove, Comparator<BEANTYPE> comparator) {
    	return super.mergeAll(collection, remove, comparator);
    }

}
