package de.mhus.lib.core.strategy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.Version;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.form.definition.FmText;

public abstract class OperationToIfcProxy extends AbstractOperation {

	public static final String METHOD = "method";
	public static final String PARAMETER = "parameter";
	public static final String TYPE = "type";
	public static final String SERIALISED = ":s";
	public static final String PARAMETERTYPE = "ptype";

	protected abstract Class<?> getInterfaceClass();
	
	protected abstract Object getInterfaceObject();
	
	protected abstract Version getInterfaceVersion();
	
	protected abstract void initOperationDescription(HashMap<String, String> parameters);

	@Override
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		
		Class<?> clazz = getInterfaceClass();
		ClassLoader cl = getClass().getClassLoader();
		
		IProperties p = context.getParameters();
		String methodName = p.getString(METHOD);
		Method method = null;
		
		for (Method m : clazz.getMethods()) {
//			if (m.isAccessible()) {
				if (m.getName().equals(methodName)) {
					// check parameters
					Parameter[] mp = m.getParameters();
					for (int i = 0; i < mp.length; i++) {
						String mpType = mp[i].getType().getCanonicalName();
						String reqType = p.getString(PARAMETERTYPE + i);
						if (!mpType.equals(reqType)) continue;
					}
					// check for more parameters
					if (p.containsKey(PARAMETERTYPE + mp.length)) continue;
					// found the right method with correct parameter types
					method = m;
					break;
				}
//			}
		}
		
		if (method == null) throw new NotFoundException("Method not found",methodName);

		int pcount = method.getParameterCount();
		Object[] params = new Object[pcount];
		for (int i = 0; i < pcount; i++) {
			params[i] = toObject(p.get(PARAMETER + i), p.getString(TYPE + i), cl);
		}
		
		Object obj = getInterfaceObject();
		try {
			Object ret = method.invoke(obj, params);
			
			return new Successful(this, "", ret);
		} catch (InvocationTargetException e) {
			Throwable t = e;
			if (e.getCause() != null) t = e.getCause();
			if (t instanceof Exception) throw (Exception)t;
			if (t instanceof RuntimeException) throw (RuntimeException)t;
			throw new MException(e.toString());
		}
	}

	private Object toObject(Object value, String type, ClassLoader cl) throws ClassNotFoundException, IOException {
		if (value == null) MCast.getDefaultPrimitive(type);
		if (type != null && type.equals(SERIALISED)) {
			return MCast.unserializeFromString(String.valueOf(value), cl);
		}
		Class<?> t = cl.loadClass(type);
		return MCast.toType(value, t, null);
	}

	@Override
	protected OperationDescription createDescription() {
		Class<?> clazz = getInterfaceClass();
		DefRoot form = new DefRoot();
		for (Method m : clazz.getMethods()) {
//			if (m.isAccessible()) {
				StringBuffer desc = new StringBuffer();
				desc.append( m.getReturnType().getCanonicalName() );
				for (Parameter p : m.getParameters()) {
					desc.append(",");
					desc.append(p.getName()).append(":").append(p.getType());
				}
				FmText def = new FmText(m.getName(), m.getName(), desc.toString() );
				form.addDefinition(def);
//			}
		}
		
		OperationDescription out = new OperationDescription(this,getInterfaceVersion(), clazz.getCanonicalName(), form );
		out.setParameters(new HashMap<>());
		initOperationDescription(out.getParameters());
		return out;
	}

}
