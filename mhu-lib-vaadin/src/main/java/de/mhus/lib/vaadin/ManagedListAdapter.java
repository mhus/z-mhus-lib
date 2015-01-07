package de.mhus.lib.vaadin;

public class ManagedListAdapter implements ManagedListEntity {

	@Override
	public void doPostCreate(AbstractListEditor<?> source) {
	}

	@Override
	public void doCancel(AbstractListEditor<?> source) {
	}

	@Override
	public boolean doPreNew(AbstractListEditor<?> source, Object id) {
		return false;
	}

	@Override
	public void doPreSave(AbstractListEditor<?> source) {
	}

	@Override
	public void doPostSave(AbstractListEditor<?> source) {
	}

	@Override
	public Object doGetId(AbstractListEditor<?> source) {
		return null;
	}

	@Override
	public void doPreDelete(AbstractListEditor<?> source) {
	}

	@Override
	public void doPostDelete(AbstractListEditor<?> source) {
	}

}
