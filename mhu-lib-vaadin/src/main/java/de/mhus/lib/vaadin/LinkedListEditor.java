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
			E out = beanClass.newInstance();
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
	protected void doDelete(E entry) {
		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doPreDelete(this);
		if (created.contains(entry))
			created.remove(entry);
		else
			deleted.add(entry);
		list.remove(entry);
		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doPostDelete(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doSave(E entry) {

		if (entry instanceof ManagedListEntity)
			((ManagedListEntity)entry).doPreSave(this);

		Object id = getId(entry);
		E original = getTarget(id);
		for (PojoAttribute<Object> attr : beanModel) {
			try {
				Object value = attr.get(entry);
				attr.set(original, value);
			} catch (Throwable t) {
			}
		}
		
		if (!created.contains(entry))
			changed.add(entry);
		
		if (original instanceof ManagedListEntity)
			((ManagedListEntity)original).doPostSave(this);
		
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

}
