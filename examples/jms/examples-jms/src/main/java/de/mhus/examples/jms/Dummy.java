package de.mhus.examples.jms;

import java.io.Serializable;
import java.util.UUID;

public class Dummy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String name = "Me";
	public UUID id = UUID.randomUUID();
	
	@Override
	public String toString() {
		return name + ":" + id + "@Dummy";
	}
}
