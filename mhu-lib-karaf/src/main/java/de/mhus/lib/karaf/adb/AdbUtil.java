package de.mhus.lib.karaf.adb;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.Rfc1738;
import de.mhus.lib.errors.MException;

public class AdbUtil {

	public static Class<? extends Persistable> getType(DbManagerService service, String typeName) throws IOException {
		for (Class<? extends Persistable> item : service.getManager().getSchema().getObjectTypes())
			if (item.getSimpleName().equals(typeName)) {
				return item; 
			}
		throw new IOException("Type not found in service: " + typeName);
	}
	
	public static List<DbManagerServiceProvider> getServiceProviders(BundleContext context) {
		LinkedList<DbManagerServiceProvider> out = new LinkedList<>();
		try {
			for ( ServiceReference<DbManagerServiceProvider> sr : context.getServiceReferences(DbManagerServiceProvider.class, null)) {
				DbManagerServiceProvider service = context.getService(sr);
				out.add(service);
			}
		} catch (InvalidSyntaxException e) {
			// should not happen
		}
		return out;
	}
	
	public static List<DbManagerService> getServices(BundleContext context, boolean connectedOnly) {

		LinkedList<DbManagerService> out = new LinkedList<>();
		for (DbManagerServiceProvider provider : getServiceProviders(context)) {
			for (DbManagerService service : provider.getServices()) {
				if (!connectedOnly || service.isConnected())
					out.add(service);
			}
		}
		return out;
	}
	
	public static DbManagerService getService(BundleContext context, String serviceName) throws IOException, InvalidSyntaxException {
		int cnt = 0;
		for (DbManagerServiceProvider provider : getServiceProviders(context)) {
			for (DbManagerService service : provider.getServices()) {
				if (serviceName.equals("*") || serviceName.equals("*" + cnt) || serviceName.equals(service.getServiceName()))
					return service;
				cnt++;
			}
		}
		throw new IOException("Service not found: " + serviceName);

	}

	public static Object createAttribute(Class<?> type, Object value) {

		//TODO use escape -ing
		if (value == null || value.equals("[null]")) return null;
		if (value.equals("[uuid]")) return UUID.randomUUID();
		
		if (value instanceof String) {
			String str = (String)value;
			if (str.startsWith("[") && str.endsWith("]")) {
				String[] parts = str.substring(1, str.length()-1).split(",");
				for (int i = 0; i < parts.length; i++)
					parts[i] = Rfc1738.decode(parts[i]);
				value = parts;
			} else {
				value = Rfc1738.decode(str);
			}
		}
		
		if (type == value.getClass()) return value;
		
		if (type == int.class || type == Integer.class)
			return MCast.toint(value, 0);
		
		if (type == long.class || type == Long.class)
			return MCast.tolong(value, 0);
		
		if (type == float.class || type == Float.class)
			return MCast.tofloat(value, 0);

		if (type == double.class || type == Double.class)
			return MCast.todouble(value, 0);

		if (type == boolean.class || type == Boolean.class)
			return MCast.toboolean(value, false);
		
		if (type == Date.class )
			return MCast.toDate(String.valueOf(value), null);
		
		if (type == UUID.class )
			return UUID.fromString(String.valueOf(value));
		
		if (type.isEnum())
			return String.valueOf(value);
		
		return null;
	}

	public static List<Object> getObjects(DbManagerService service, Class<?> type, String id) throws MException {
		LinkedList<Object> out = new LinkedList<>();
		if (MString.isEmptyTrim(id)) return out;
		
		if (id.startsWith("(") && id.endsWith(")")) {
			String aql = id.substring(1, id.length()-1);
			for (Object o : service.getManager().getByQualification(type, aql, null))
				out.add(o);
		} else {
			// TODO separate PKs, currently only one PK is supported
			Object obj = service.getManager().getObject(type, id);
			if (obj != null)
				out.add(obj);
		}
		
		return out;
	}

}
