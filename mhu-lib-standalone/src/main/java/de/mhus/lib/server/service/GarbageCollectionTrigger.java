package de.mhus.lib.server.service;

import de.mhus.lib.server.Task;



public class GarbageCollectionTrigger extends Task {

	@Override
	public void pass() throws Exception {
		log().i("Garbage Collection");
		System.gc();
	}

}
