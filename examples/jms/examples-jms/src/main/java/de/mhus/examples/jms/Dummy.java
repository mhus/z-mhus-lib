package de.mhus.examples.jms;

import java.io.Serializable;
import java.util.UUID;

public class Dummy implements Serializable {

	public String name = "Me";
	public UUID id = UUID.randomUUID();
	
	public String toString() {
		return name + ":" + id + "@Dummy";
	}
}
