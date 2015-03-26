package de.mhus.lib.core.pojo;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

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

		   public static Set<Method> getMethodsForMethod(Class<?> cls, String methodName) {
			  Set<Method> result = new HashSet<>();
		      traverseMethodsForMethod(cls,result,methodName);
		      return result;
		   }
	   
}
