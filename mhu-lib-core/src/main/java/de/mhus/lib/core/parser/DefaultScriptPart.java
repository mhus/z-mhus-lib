package de.mhus.lib.core.parser;

import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import de.mhus.lib.core.lang.MObject;

public class DefaultScriptPart extends MObject implements StringPart {

	private String part;
	ScriptEngineManager manager = new ScriptEngineManager();

	public DefaultScriptPart(String part) {
		if (part.startsWith(">>>")) part = part.substring(3);
		this.part = part;
	}

	@Override
	public void execute(StringBuffer out, Map<String, Object> attributes) {
        ScriptEngine engine = manager.getEngineByName ("js");
        try {
        	if (attributes != null) {
	        	Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
	        	for (Map.Entry<String, Object> entry : attributes.entrySet())
	        		bindings.put(entry.getKey(), entry.getValue());
        	}
			Object ret = engine.eval(part);
			if (ret != null) {
				out.append(ret);
			}
		} catch (ScriptException e) {
			log().d(e,part);
		}
	}

	@Override
	public void dump(int level, StringBuffer out) {
		
	}

}
