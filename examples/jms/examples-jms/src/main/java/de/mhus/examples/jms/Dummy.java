package de.mhus.examples.jms;

import java.util.UUID;

public class Dummy {

	public String name = "Me";
	public UUID id = UUID.randomUUID();
	
	public String toString() {
		return name + ":" + id + "@Dummy";
	}
}
