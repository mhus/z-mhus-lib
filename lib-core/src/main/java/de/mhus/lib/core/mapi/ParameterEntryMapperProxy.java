package de.mhus.lib.core.mapi;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.logging.ParameterEntryMapper;

public class ParameterEntryMapperProxy implements ParameterEntryMapper {

	private String clazz;
	private ParameterEntryMapper inst;
	private long last;

	public ParameterEntryMapperProxy(String clazz) {
		this.clazz = clazz;
	}

	@Override
	public Object map(Object in) {
		if (inst == null && MPeriod.isTimeOut(last, MPeriod.MINUTE_IN_MILLISECOUNDS)) {
			last = System.currentTimeMillis();
        	try {
        		inst = (ParameterEntryMapper) MApi.get().createActivator().createObject(clazz.trim());
        	} catch (Throwable t) {
        		MApi.dirtyLogDebug("ParameterEntryMapperProxy",clazz,t.getMessage());
        	}
		}
		return inst == null ? in : inst.map(in);
	}

}
