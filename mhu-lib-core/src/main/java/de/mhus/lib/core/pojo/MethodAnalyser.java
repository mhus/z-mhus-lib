package de.mhus.lib.core.pojo;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>MethodAnalyser class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MethodAnalyser {

	   private static void traverseInterfacesForMethod(Class<?> cls, Set<Class<?>> result, String methodName) {
	      for (Class<?> c : cls.getInterfaces()) {
	         for (Method m : c.getDeclaredMethods()) {
	            if (methodName.equals(m.getName())) {
	               result.add(c);
	            }
	         }

	         traverseInterfacesForMethod(c, result, methodName);
	      }
	   }

	   /**
	    * <p>getInterfacesForMethod.</p>
	    *
	    * @param cls a {@link java.lang.Class} object.
	    * @param methodName a {@link java.lang.String} object.
	    * @return a {@link java.util.Set} object.
	    */
	   public static Set<Class<?>> getInterfacesForMethod(Class<?> cls, String methodName) {
		  Set<Class<?>> result = new HashSet<>();
	      traverseInterfacesForMethod(cls,result,methodName);
	      return result;
	   }
	   
	   private static void traverseMethodsForMethod(Class<?> cls, Set<Method> result, String methodName) {
		      for (Class<?> c : cls.getInterfaces()) {
		         for (Method m : c.getDeclaredMethods()) {
		            if (methodName.equals(m.getName())) {
		               result.add(m);
		            }
		         }

		         traverseMethodsForMethod(c, result, methodName);
		      }
		   }

		   /**
		    * <p>getMethodsForMethod.</p>
		    *
		    * @param cls a {@link java.lang.Class} object.
		    * @param methodName a {@link java.lang.String} object.
		    * @return a {@link java.util.Set} object.
		    */
		   public static Set<Method> getMethodsForMethod(Class<?> cls, String methodName) {
			  Set<Method> result = new HashSet<>();
		      traverseMethodsForMethod(cls,result,methodName);
		      return result;
		   }
	   
}
