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
package de.mhus.lib.vaadin;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.mhus.lib.core.pojo.PojoAttribute;

public class LinkedListEditor<E> extends AbstractBeanListEditor<E> {

	private static final long serialVersionUID = 1L;
	protected LinkedList<E> list = new LinkedList<>();
	protected HashSet<E> deleted = new HashSet<>();
	protected HashSet<E> changed = new HashSet<>();
	protected HashSet<E> created = new HashSet<>();
	private PojoAttribute<Object> idAttribute;
	
	@SuppressWarnings("unchecked")
	public LinkedListEditor(Class<E> beanClass, String schema, String idAttribute) {
		super(beanClass, schema);
		this.idAttribute = beanModel.getAttribute(idAttribute);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected E getEditableTarget(Object id) {
		E original = getTarget(id);
		E clone = createTarget();
		for (PojoAttribute<Object> attr : beanModel) {
			try {
				Object value = attr.get(original);
				attr.set(clone, value);
			} catch (Throwable t) {
			}
		}
		return clone;
	}

	@Override
	protected E createTarget() {
		try {
			E out = beanClass.getDeclaredConstructor().newInstance();
			if (out instanceof ManagedListEntity)
				((ManagedListEntity)out).doPostCreate(this);
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void doCancel(E entry) {
		// nothing to do
		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doCancel(this);
	}

	@Override
	public void doDelete(E entry) {
		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doPreDelete(this);
		if (created.contains(entry))
			created.remove(entry);
		else
			deleted.add(entry);
		list.remove(entry);
		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doPostDelete(this);
		setModified(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doSave(E entry) {

		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doPreSave(this);

		Object id = getId(entry);
		E original = id != null ? getTarget(id) : null;

		// in case of a new one ... (for external callers)
		if (original == null) {
			doSaveNew(entry);
			setModified(true);
			return;
		}
		
		for (PojoAttribute<Object> attr : beanModel) {
			try {
				Object value = attr.get(entry);
				attr.set(original, value);
			} catch (Throwable t) {
			}
		}
		
		if (!created.contains(original))
			changed.add(original);
		
		if (original instanceof ManagedListEntity)
			((ManagedListEntity)original).doPostSave(this);
	
		setModified(true);
	}

	@Override
	protected E getTarget(Object id) {
		for (E item : list)
			if (getId(item).equals(id)) return item;
		return null;
	}

	@Override
	protected Object getId(E entry) {
		try {
			if (entry instanceof ManagedListEntity) {
				Object id = ((ManagedListEntity)entry).doGetId(this);
				if (id != null) return id;
			}
			return idAttribute.get(entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected List<E> createBeanDataList() {
		return list;
	}
	
	@Override
	protected void doSaveNew(E entry) {
		Object id = createId();
		try {
			boolean done = false;
			if (entry instanceof ManagedListEntity)
				done = ((ManagedListEntity)entry).doPreNew(this, id);

			if (!done)
				idAttribute.set(entry, id);
			
			list.add(entry);
			created.add(entry);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Object createId() {
		return UUID.randomUUID().toString();
	}
	
	public void applyChanges() {
		created.clear();
		deleted.clear();
		changed.clear();
	}
	
}
