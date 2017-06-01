package de.mhus.lib.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;

import de.mhus.lib.core.logging.Log;

public class MSystem {	
	
	private static Log log = Log.getLog(MSystem.class);
	
	/**
	 * Returns the name of the current system. COMPUTERNAME or HOSTNAME.
	 * 
	 * @return
	 */
	public static String getHostname() 
	{
		String out = System.getenv().get("COMPUTERNAME");
		if (out == null)
			out = System.getenv().get("HOSTNAME");
		if (out == null)
		{
			RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
			String name = rt.getName();
			out = MString.afterIndex(name, '@');
		}
		return out;
	}
	
	/**
	 * Returns the process id of the current application.
	 * 
	 * @return
	 */
	public static String getPid() 
	{
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		String name = rt.getName();
		return MString.beforeIndex(name, '@');
	}
	
	/**
	 * Load and return a properties file. If the file not exists it will only
	 * log the impact and return a empty properties object.
	 * 
	 * If the properties object is not null this instance will be used to load
	 * the file entries.
	 * 
	 * 1. Find by system property {propertyname}.file=
	 * 2. Find in {current dir}
	 * 3. Find in {current dir}/config
	 * 4. Find in {CONFIGURATION}/config
	 * 5. Find in classpath without package name
	 * 6. Find in classpath with package of the owner (if set)
	 * 7. throw an error
	 * 
	 * @param owner null or reference object for the class path
	 * @param properties A pre-instanciated properties object
	 * @param propertyFile Name of the properties file, e.g. something.properties
	 * @return The loaded properties instance
	 */
	public static Properties loadProperties(Object owner,Properties properties, String propertyFile) {
       log.d("Loading properties",propertyFile);
       // get resource
       if (properties == null ) properties = new Properties();
       try {
	       URL m_url = locateResource(owner,propertyFile);
	       if (m_url==null) {
	    	   log.w("Properties file not found",propertyFile);
	    	   return properties;
	       }
	       log.i("load",m_url);
	       InputStream stream = m_url.openStream();
	       properties.load(stream);
	       stream.close();
       } catch (IOException e) {
    	   log.i("Error loading properties file", propertyFile,e.toString());
    	   //logger.t(e);
       }
       return properties;
	}
       
	/**
	 * 
	 * 1. Find by system property {propertyname}.file=
	 * 2. Find in {current dir}
	 * 3. Find in {current dir}/config
	 * 4. Find in {CONFIGURATION}/config
	 * 5. Find in classpath without package name
	 * 6. Find in classpath with package of the owner (if set)
	 * 7. throw an error
	 * @param owner
	 * @param propertyFile
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static URL locateResource(Object owner,String propertyFile) throws IOException {

		  URL url = null;
		  
		  String location = System.getProperty(propertyFile + ".file");
	      if (url == null && location != null) {
	         File f = new File(location);
	         if ( f.exists() && f.isFile() )
	        	 url = f.toURL();
	      }
	      
	      {
		      File f = new File(propertyFile);
		      if ( f.exists() && f.isFile() )
		    	  return f.toURL();
	      }
	      
	      {
		      File f = new File("config/" + propertyFile);
		      if ( f.exists() && f.isFile() )
		    	  return f.toURL();
	      }
	      
	      try {
		      String configurationPath = System.getenv("CONFIGURATION");
		      if ( url == null && configurationPath != null ) {
		    	  File f = new File(configurationPath + "/" + propertyFile);
		          if ( f.exists() && f.isFile() )
		         	 url = f.toURL();    	  
		      }
	      } catch (SecurityException e) {}
	      
	      ClassLoader loader = Thread.currentThread().getContextClassLoader();
	      if (url == null && loader != null)
	         url = loader.getResource(propertyFile);
	      
	      Class<?> ownerClass = null;
	      if (owner != null) {
	    	  if (owner instanceof Class)
	    		  ownerClass = (Class<?>) owner;
	    	  else
	    		  ownerClass = owner.getClass();
	      }
	      
	      if (ownerClass !=null && url == null) {
	         url = ownerClass.getResource("/" + ownerClass.getPackage().getName().replaceAll( "\\.", "/" ) + "/" + propertyFile );
	      }
	      if (ownerClass !=null && url == null) {
		         url = ownerClass.getResource( ownerClass.getPackage().getName().replaceAll( "\\.", "/" ) + "/" + propertyFile );
		  }
	      
	      if (url != null) return url;
	      throw new FileNotFoundException("Cannot locate resource: " + propertyFile);
	   }
	
	/**
	 * makeshift system beep if awt.Toolkit.beep is not available. Works also in
	 * JDK 1.02.
	 */
	public static void beep() {
		System.out.print("\007");
		System.out.flush();
	} // end beep

	public static String findSource(int returns) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (stack.length > returns)
			return stack[returns].getClassName();
		return "?";
	}

	public static String findSource() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (StackTraceElement step : stack) {
			String n = step.getClassName();
			if (!n.startsWith("java.lang") && !n.startsWith("de.mhus.lib.core"))
				return n;
		}
		return "?";
	}
	
	/**
	 * Return the name of the main class or null if not found.
	 * @return
	 */
	public static String getMainClassName()
	{
	  for(final Map.Entry<String, String> entry : System.getenv().entrySet())
	  {
	    if(entry.getKey().startsWith("JAVA_MAIN_CLASS"))
	      return entry.getValue();
	  }
	  return null;
	}

	/**
	 * Return the system temp directory.
	 * 
	 * @return
	 */
	public static String getTmpDirectory() {
		return System.getProperty("java.io.tmpdir");
	}
	
	public static String toString(Object sender, Object ... attributes) {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		if (sender != null)
			sb.append(sender instanceof String ? sender : sender.getClass().getSimpleName()  ).append(':');
		boolean first = true;
		for (Object a : attributes) {
			if (!first) sb.append(','); else first = false;
			serialize(sb, a, null);
		}
		sb.append(']');
		return sb.toString();
	}
	
	public static Throwable serialize(StringBuffer sb, Object o, Throwable error) {
    	try {
	    	if (o == null) {
				sb.append("[null]");
	    	} else
			if (o instanceof Throwable) {
				if (error == null) return (Throwable)o;
				// another error
				sb.append("[").append(o).append("]");
			} else
	    	if (o.getClass().isArray()) {
	    		sb.append("{");
	    		for (Object p : (Object[])o) {
	    			error = serialize(sb, p, error);
	    		}
	    		sb.append("}");
	    	} else
	    		sb.append("[").append(o).append("]");
    	} catch (Throwable t) {}
		return error;
	}

	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotation) {
		Class<?> current = clazz;
		while (current != null) {
			if (current.isAnnotationPresent(annotation))
				return current.getAnnotation(annotation);
			current = current.getSuperclass();
		}
		return null;
	}

	public static boolean equals(Object a, Object b) {
		if (a == null && b == null) return true;
		if (a == null) return false;
		return a.equals(b);
	}
	
	public static ScriptResult startScript(File dir, String script, long timeout) {
		log.i("script",dir,script);
		ProcessBuilder pb = new ProcessBuilder(new File(dir, script).getAbsolutePath() );
		 @SuppressWarnings("unused")
		Map<String, String> env = pb.environment();
		 pb.directory(dir);
		 ScriptResult out = new ScriptResult();
		 try {
			 Process p = pb.start();
			 
			 out.output = new BufferedReader(new InputStreamReader(p.getInputStream()));
			 out.error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			 
			 p.waitFor();
			 p.destroy();
			 
			 out.rc = p.exitValue();
			 
		 } catch (Throwable t) {
			 out.exception = t;
		 }
		 return out;
	}
	
	public static class ScriptResult {

		public Throwable exception;
		public int rc;
		public BufferedReader error;
		public BufferedReader output;
		
	}

	public static String getAppIdent() {
		return getHostname() + ":" + getPid();
	}
	
	public static String getObjectId(Object o) {
		if (o == null) return "null";
		return o.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(o));
	}

	public static String getClassName(Object obj) {
		Class<? extends Object> clazz = getMainClass(obj);
		if (clazz == null) return "null";
		return clazz.getCanonicalName();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Class<?> getMainClass(Object obj) {
		if (obj == null) return null;
		Class<? extends Object> clazz = obj.getClass();
		if (obj instanceof Class)
			clazz = (Class)obj;
		while (clazz != null && clazz.isAnonymousClass())
			clazz = clazz.getEnclosingClass();
		if (clazz == null) return null;
		return clazz;
	}

	public static <T extends Comparable<T>> int compareTo(T s1, T s2) {
		if (s1 == null && s2 == null) return 0;
		if (s1 == null) return -1;
		if (s2 == null) return 1;
		return s1.compareTo(s2);
	}

	public static String freeMemoryAsString() {
		long free = freeMemory();
		return MString.toByteDisplayString(free);
	}

	public static String maxMemoryAsString() {
		return MString.toByteDisplayString(Runtime.getRuntime().maxMemory());
	}
	
	public static String memDisplayString() {
		return freeMemoryAsString() + " / " + maxMemoryAsString();
	}
	
	public static long freeMemory() {
		Runtime r = Runtime.getRuntime();
		return r.maxMemory() - r.totalMemory() + r.freeMemory();
	}

	public static Field getDeclaredField(Class<?> clazz, String name) {
		if (clazz == null || name == null) return null;
		try {
			Field field = clazz.getDeclaredField(name);
			return field;
		} catch (NoSuchFieldException e) {}
		return getDeclaredField(clazz.getSuperclass(), name);
	}
	
}
