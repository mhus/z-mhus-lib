package de.mhus.lib.core.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

import de.mhus.lib.annotations.pojo.Embedded;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>FunctionsStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FunctionsStrategy extends MObject implements PojoStrategy {

	private boolean embedded;
	private boolean toLower = true;
	private String embedGlue;
	private boolean actionsOnly;
	private Class<? extends Annotation>[] annotationMarker;
	
	/**
	 * <p>Constructor for FunctionsStrategy.</p>
	 */
	@SuppressWarnings("unchecked")
	public FunctionsStrategy() {
		this(true,true, ".", false);
	}
	
	/**
	 * <p>Constructor for FunctionsStrategy.</p>
	 *
	 * @param actionsOnly a boolean.
	 */
	@SuppressWarnings("unchecked")
	public FunctionsStrategy(boolean actionsOnly) {
		this(true,true, ".", actionsOnly);
	}
	
	/**
	 * <p>Constructor for FunctionsStrategy.</p>
	 *
	 * @param embedded a boolean.
	 * @param toLower a boolean.
	 * @param embedGlue a {@link java.lang.String} object.
	 * @param actionsOnly a boolean.
	 * @param annotationMarker a {@link java.lang.Class} object.
	 */
	public FunctionsStrategy(boolean embedded, boolean toLower, String embedGlue, boolean actionsOnly, @SuppressWarnings("unchecked") Class<? extends Annotation> ... annotationMarker) {
		this.embedded = embedded;
		this.toLower = toLower;
		this.embedGlue = embedGlue;
		this.actionsOnly = actionsOnly;
		this.annotationMarker = annotationMarker;
	}
	
	/** {@inheritDoc} */
	@Override
	public void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model) {
		parse("", null, parser, clazz, model, 0);
	}
	
	/**
	 * <p>parse.</p>
	 *
	 * @param prefix a {@link java.lang.String} object.
	 * @param parent a {@link de.mhus.lib.core.pojo.FunctionAttribute} object.
	 * @param parser a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 * @param clazz a {@link java.lang.Class} object.
	 * @param model a {@link de.mhus.lib.core.pojo.PojoModelImpl} object.
	 * @param level a int.
	 */
	@SuppressWarnings("unchecked")
	protected void parse(String prefix, FunctionAttribute<Object> parent, PojoParser parser, Class<?> clazz, PojoModelImpl model, int level) {

		if (level > 10 ) return; // logging ?
		
		for (Method m : getMethods(clazz)) {

			// ignore static methods
			if (Modifier.isStatic(m.getModifiers()))
					continue;

			try {
				String mName = m.getName();
				String s = (toLower ? mName.toLowerCase() : mName);
				if (s.startsWith("get") || s.startsWith("set")) s = s.substring(3);
				else
				if (s.startsWith("is")) s = s.substring(2);
				String name = prefix + s;
				Method getter = null;
				Method setter = null;
				if (mName.startsWith("get") && m.getParameterCount() == 0) {
					mName = mName.substring(3);
					getter = m;
					try {
						setter = clazz.getMethod("set" + mName,getter.getReturnType());
					} catch (NoSuchMethodException nsme) {}
				} else
				if (mName.startsWith("set") && m.getParameterCount() == 1) {
					mName = mName.substring(3);
					setter = m;
					try {
						getter = clazz.getMethod("get" + mName);
					} catch (NoSuchMethodException nsme) {
						try {
							getter = clazz.getMethod("is" + mName);
						} catch (NoSuchMethodException nsme2) {}
					}
				} else
				if (mName.startsWith("is") && m.getParameterCount() == 0) {
					mName = mName.substring(2);
					getter = m;
					try {
						setter = clazz.getMethod("set" + mName,getter.getReturnType());
					} catch (NoSuchMethodException nsme) {}
				} else {
//					log().d("field is not a getter/setter", mName);
					// it's an action
					FunctionAction action = new FunctionAction(clazz,m,name,parent);
					model.addAction(action);
					continue;
				}
				
//				if (getter == null) {
//					log().d("getter not found",mName);
//					continue;
//				}

//				Class<?> ret = getter.getReturnType();
//				if (ret == void.class) {
//					log().d("Value type is void - ignore");
//					continue;
//				}
				
				if (	!actionsOnly 
						&& 
						(
							isEmbedded(getter,setter)
							||
							isMarker(getter,setter)
						)
					) {
					
					@SuppressWarnings({ "rawtypes" })
					FunctionAttribute attr = new FunctionAttribute(clazz, getter, setter, name, parent);
					if (isEmbedded(getter,setter)) {
						parse(prefix + name + embedGlue, attr, parser, attr.getType(), model, level+1);
					} else {
						if (!model.hasAttribute(name))
							model.addAttribute(attr);
					}
				
				}
			} catch (Exception e) {
				log().d(e);
			}
		}
	}

	private boolean isEmbedded(Method getter, Method setter) {
		if (!embedded) return false;
		if (getter != null) {
			if (getter.isAnnotationPresent(Embedded.class)) return true;
		}	
		return false;
	}
	
	private boolean isMarker(Method getter, Method setter) {
		if (annotationMarker == null || annotationMarker.length == 0) return true;
		if (getter != null) {
			for (Class<? extends Annotation> a :annotationMarker)
				if (getter.isAnnotationPresent(a)) return true;
		}
		if (setter != null) {
			for (Class<? extends Annotation> a :annotationMarker)
				if (setter.isAnnotationPresent(a)) return true;
		}
		return false;
	}

	/**
	 * <p>getMethods.</p>
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a {@link java.util.LinkedList} object.
	 */
	protected LinkedList<Method> getMethods(Class<?> clazz) {
		LinkedList<Method> out = new LinkedList<Method>();
//		HashSet<String> names = new HashSet<String>();
		do {
			for (Method m : clazz.getMethods()) {
//				if (!names.contains(m.getName())) {
					out.add(m);
//					names.add(m.getName());
//				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public void parseObject(PojoParser parser, Object pojo, PojoModelImpl model) {
		Class<?> clazz = pojo.getClass();
		parse(parser, clazz, model);
	}
	
}
