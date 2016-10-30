package de.mhus.lib.core.directory;

public interface IResourceProvider<T> {

	public T getResourceByPath(String path);
	
	public T getResourceById(String id);

	String getName();

}
