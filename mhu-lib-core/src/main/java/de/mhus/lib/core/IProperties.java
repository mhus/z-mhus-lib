package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Map;

public interface IProperties extends Map<String,Object>, Serializable, Iterable<Map.Entry<String,Object>> {
	
}
