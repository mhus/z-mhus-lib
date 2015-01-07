package de.mhus.lib.vaadin;

public interface ManagedListEntity {

	void doPostCreate(AbstractListEditor<?> source);
	void doCancel(AbstractListEditor<?> source);
	boolean doPreNew(AbstractListEditor<?> source, Object id);
	void doPreSave(AbstractListEditor<?> source);
	void doPostSave(AbstractListEditor<?> source);
	Object doGetId(AbstractListEditor<?> source);

	void doPreDelete(AbstractListEditor<?> source);
	void doPostDelete(AbstractListEditor<?> source);

	
	
}
