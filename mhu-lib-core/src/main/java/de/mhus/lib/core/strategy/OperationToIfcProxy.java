package de.mhus.lib.core.strategy;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.util.Version;
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
					method = m;
					break;
				}
//			}
		}
		
		if (method == null) throw new NotFoundException("Method not found",methodName);

		int pcount = method.getParameterCount();
		Object[] params = new Object[pcount];
		for (int i = 0; i < pcount; i++) {
			params[i] = toObject(p.get(PARAMETER + i), p.get(TYPE + i), cl);
		}
		
		Object obj = getInterfaceObject();
		Object ret = method.invoke(obj, params);
		
		return new Successful(this, "", ret);
	}

	private Object toObject(Object value, Object type, ClassLoader cl) throws ClassNotFoundException, IOException {
		if (type != null && type.equals(SERIALISED)) {
			return MCast.unserializeFromString((String)value, cl);
		}
		return value;
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

		return out;
	}

}
