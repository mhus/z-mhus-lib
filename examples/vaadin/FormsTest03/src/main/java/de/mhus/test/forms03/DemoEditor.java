package de.mhus.test.forms03;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.util.FilterRequest;
import de.mhus.lib.vaadin.AbstractBeanListEditor;

public class DemoEditor extends AbstractBeanListEditor<DemoEntity>{

	public DemoEditor(Class<DemoEntity> beanClass, String schema) {
		super(beanClass, schema);
		setNeedSortUpdate(true);
	}

	HashMap<String, DemoEntity> index = new HashMap<>();
	{
		for (int i = 0; i < 100; i++) {
			DemoEntity e = new DemoEntity();
			e.setId("" + i);
			e.setFirstName("First Name " + i);
			e.setLastName("Last Name " + i);
			index.put(e.getId(), e);
		}
	}
	
	@Override
	protected List<DemoEntity> createBeanDataList() {
		LinkedList<DemoEntity> list = new LinkedList<>();
		for (DemoEntity e : index.values())
			list.add(e);
		return list;
	}

	@Override
	protected DemoEntity createTarget() {
		return new DemoEntity();
	}

	@Override
	protected void doCancel(DemoEntity entry) throws Exception {
		// bye
	}

	@Override
	protected void doDelete(DemoEntity entry) throws Exception {
		index.remove(entry.getId());
	}

	@Override
	protected void doSave(DemoEntity entry) throws Exception {
		index.get(entry.getId()).save(entry);
	}

	@Override
	protected DemoEntity getTarget(Object id) {
		return new DemoEntity( index.get(String.valueOf(id)) );
	}

	@Override
	protected Object getId(DemoEntity entry) {
		return entry.getId();
	}

}
